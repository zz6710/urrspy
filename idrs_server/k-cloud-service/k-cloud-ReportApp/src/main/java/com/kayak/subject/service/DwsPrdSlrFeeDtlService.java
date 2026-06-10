package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.cache.dao.CacheDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.DwdPrdPrdBasInfDao;
import com.kayak.subject.model.DwsDailyPrdDtl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.kayak.subject.dao.DwsPrdSlrFeeDtlDao;
import com.kayak.subject.model.DwsPrdSlrFeeDtl;

@Service
@APIDefine(desc = "产品销售商费用明细表服务", model = DwsPrdSlrFeeDtl.class)
@Slf4j
public class DwsPrdSlrFeeDtlService implements ExcelImportService<DwsPrdSlrFeeDtl> {

	@Autowired
	private DwsPrdSlrFeeDtlDao dwsPrdSlrFeeDtlDao;
	@Autowired
	private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;

	@API(desc = "查询产品销售商费用明细表信息", auth = APIAuth.YES)
	public SqlResult<DwsPrdSlrFeeDtl> findDwsPrdSlrFeeDtls(SqlParam<DwsPrdSlrFeeDtl> params) throws Exception {
		return dwsPrdSlrFeeDtlDao.findDwsPrdSlrFeeDtls(params);
	}

	@API(desc = "添加产品销售商费用明细表", params = "id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,fee_type,fee_amt,slr_cd,slr_nm,crt_dt,crt_tm", auth = APIAuth.NO)
	public int addDwsPrdSlrFeeDtl(SqlParam<DwsPrdSlrFeeDtl> params) throws Exception {
		params.getModel().setCrtDt(DateUtil.getNowDate());
		params.getModel().setCrtTm(DateUtil.getNowTime());
		return dwsPrdSlrFeeDtlDao.addDwsPrdSlrFeeDtl(params).getEffect();
	}
	
	@API(desc = "修改产品销售商费用明细表", params = "id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,fee_type,fee_amt,slr_cd,slr_nm,crt_dt,crt_tm", auth = APIAuth.NO)
	public int updateDwsPrdSlrFeeDtl(SqlParam<DwsPrdSlrFeeDtl> params) throws Exception {
		return dwsPrdSlrFeeDtlDao.updateDwsPrdSlrFeeDtl(params).getEffect();
	}
	
	@API(desc = "删除产品销售商费用明细表", params = "id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,fee_type,fee_amt,slr_cd,slr_nm,crt_dt,crt_tm", auth = APIAuth.NO)
	public int deleteDwsPrdSlrFeeDtl(SqlParam<DwsPrdSlrFeeDtl> params) throws Exception {
		return dwsPrdSlrFeeDtlDao.deleteDwsPrdSlrFeeDtl(params).getEffect();
	}

	public int deleteDwsPrdSlrFeeDtl(DwsPrdSlrFeeDtl params) throws Exception {
		return dwsPrdSlrFeeDtlDao.deleteDwsPrdSlrFeeDtl(params).getEffect();
	}

	public String importDwsPrdSlrFeeDtl(MultipartFile file, Map<String, Object> params) throws Exception {
    		long startTime = System.currentTimeMillis();
    		log.info("导入产品销售商费用明细表【{}】开始",file.getOriginalFilename());
    		final DwsPrdSlrFeeDtlService dwsPrdSlrFeeDtlService = this;
    		ExcelImportListener<DwsPrdSlrFeeDtl> excelImportListener = new ExcelImportListener<DwsPrdSlrFeeDtl>(params) {
    			@Override
    			protected ExcelImportService<DwsPrdSlrFeeDtl> getImportService() {
    				return dwsPrdSlrFeeDtlService;
    			}
    		};
    		try {
    			EasyExcel.read(file.getInputStream())
    					.head(DwsPrdSlrFeeDtl.class)
    					.registerReadListener(excelImportListener)
    					.sheet()
    					.doRead();
    		} catch (Exception e) {
    			throw new Exception(excelImportListener.getStopMsg());
    		}
    		log.info("导入产品销售商费用明细表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
    		return excelImportListener.getStopMsg();
    	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsPrdSlrFeeDtl> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "REPLACE INTO dws_prd_slr_fee_dtl (deal_date,prdc_cd,prdc_nm,mother_prdc_cd,fee_type,fee_amt,slr_cd,slr_nm,crt_dt,crt_tm) VALUES(?,?,?,?,?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new PromptException("没有数据");
		}
		comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                for (DwsPrdSlrFeeDtl info : list) {
                    ps.setString(1, info.getDealDate());
                    ps.setString(2, info.getPrdcCd());
                    ps.setString(3, info.getPrdcNm());
                    ps.setString(4, info.getMotherPrdcCd());
                    ps.setString(5, info.getFeeType());
                    ps.setString(6, info.getFeeAmt());
                    ps.setString(7, info.getSlrCd());
                    ps.setString(8, info.getSlrNm());
                    ps.setString(9, DateUtil.getNowDate());
                    ps.setString(10, DateUtil.getNowTime());
                    ps.addBatch();
                }
                ps.executeBatch();
                log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                log.error("导入报表指标科目映射异常!", e);
                throw new Exception(e.getMessage());
            } finally {
                ps.close();
            }
        });
    }

}
