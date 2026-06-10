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
import com.kayak.subject.model.DwsMonthPrdDtl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.kayak.subject.dao.DwsDailyPrdDtlDao;
import com.kayak.subject.model.DwsDailyPrdDtl;

@Service
@APIDefine(desc = "产品明细日中间表服务", model = DwsDailyPrdDtl.class)
@Slf4j
public class DwsDailyPrdDtlService implements ExcelImportService<DwsDailyPrdDtl> {

	@Autowired
	private DwsDailyPrdDtlDao dwsDailyPrdDtlDao;
    @Autowired
    private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;

	@API(desc = "查询产品明细日中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsDailyPrdDtl> findDwsDailyPrdDtls(SqlParam<DwsDailyPrdDtl> params) throws Exception {
		return dwsDailyPrdDtlDao.findDwsDailyPrdDtls(params);
	}

	@API(desc = "添加产品明细日中间表", params = "id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,ssp_amt,ssb_amt,rdm_amt,exp_amt,sbt_amt_f,inv_yld_amt_dly,rdm_yld_amt,exp_yld_amt,shr_sbt_yld_amt_f,csh_dvd,shr_rvt_dvd,crt_dt,crt_tm", auth = APIAuth.NO)
	public int addDwsDailyPrdDtl(SqlParam<DwsDailyPrdDtl> params) throws Exception {
        params.getModel().setCrtDt(DateUtil.getNowDate());
        params.getModel().setCrtTm(DateUtil.getNowTime());
		return dwsDailyPrdDtlDao.addDwsDailyPrdDtl(params).getEffect();
	}
	
	@API(desc = "修改产品明细日中间表", params = "id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,ssp_amt,ssb_amt,rdm_amt,exp_amt,sbt_amt_f,inv_yld_amt_dly,rdm_yld_amt,exp_yld_amt,shr_sbt_yld_amt_f,csh_dvd,shr_rvt_dvd,crt_dt,crt_tm", auth = APIAuth.NO)
	public int updateDwsDailyPrdDtl(SqlParam<DwsDailyPrdDtl> params) throws Exception {
		return dwsDailyPrdDtlDao.updateDwsDailyPrdDtl(params).getEffect();
	}
	
	@API(desc = "删除产品明细日中间表", params = "id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,ssp_amt,ssb_amt,rdm_amt,exp_amt,sbt_amt_f,inv_yld_amt_dly,rdm_yld_amt,exp_yld_amt,shr_sbt_yld_amt_f,csh_dvd,shr_rvt_dvd,crt_dt,crt_tm", auth = APIAuth.NO)
	public int deleteDwsDailyPrdDtl(SqlParam<DwsDailyPrdDtl> params) throws Exception {
		return dwsDailyPrdDtlDao.deleteDwsDailyPrdDtl(params).getEffect();
	}

    public int deleteDwsDailyPrdDtl(DwsDailyPrdDtl params) throws Exception {
        return dwsDailyPrdDtlDao.deleteDwsDailyPrdDtl(params).getEffect();
    }

	public String importDwsDailyPrdDtl(MultipartFile file, Map<String, Object> params) throws Exception {
    		long startTime = System.currentTimeMillis();
    		log.info("导入产品明细日中间表【{}】开始",file.getOriginalFilename());
    		final DwsDailyPrdDtlService dwsDailyPrdDtlService = this;
    		ExcelImportListener<DwsDailyPrdDtl> excelImportListener = new ExcelImportListener<DwsDailyPrdDtl>(params) {
    			@Override
    			protected ExcelImportService<DwsDailyPrdDtl> getImportService() {
    				return dwsDailyPrdDtlService;
    			}
    		};
    		try {
    			EasyExcel.read(file.getInputStream())
    					.head(DwsDailyPrdDtl.class)
    					.registerReadListener(excelImportListener)
    					.sheet()
    					.doRead();
    		} catch (Exception e) {
    			throw new Exception(excelImportListener.getStopMsg());
    		}
    		log.info("导入产品明细日中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
    		return excelImportListener.getStopMsg();
    	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsDailyPrdDtl> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "REPLACE INTO dws_daily_prd_dtl (deal_date,prdc_cd,prdc_nm,mother_prdc_cd,ssp_amt,ssb_amt,rdm_amt,exp_amt,sbt_amt_f,inv_yld_amt_dly,rdm_yld_amt,exp_yld_amt,shr_sbt_yld_amt_f,csh_dvd,shr_rvt_dvd,crt_dt,crt_tm) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (list == null || list.isEmpty()) {
            throw new PromptException("没有数据");
        }
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                for (DwsDailyPrdDtl info : list) {
                    ps.setString(1, info.getDealDate());
                    ps.setString(2, info.getPrdcCd());
                    ps.setString(3, info.getPrdcNm());
                    ps.setString(4, info.getMotherPrdcCd());
                    ps.setString(5, info.getSspAmt());
                    ps.setString(6, info.getSsbAmt());
                    ps.setString(7, info.getRdmAmt());
                    ps.setString(8, info.getExpAmt());
                    ps.setString(9, info.getSbtAmtF());
                    ps.setString(10, info.getInvYldAmtDly());
                    ps.setString(11, info.getRdmYldAmt());
                    ps.setString(12, info.getExpYldAmt());
                    ps.setString(13, info.getShrSbtYldAmtF());
                    ps.setString(14, info.getCshDvd());
                    ps.setString(15, info.getShrRvtDvd());
                    ps.setString(16, DateUtil.getNowDate());
                    ps.setString(17, DateUtil.getNowTime());
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
