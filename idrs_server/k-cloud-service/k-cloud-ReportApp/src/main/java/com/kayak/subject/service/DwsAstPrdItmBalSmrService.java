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
import com.kayak.subject.model.DwsMonthInvRaise;
import com.kayak.subject.model.DwsMonthNavInf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.kayak.subject.dao.DwsAstPrdItmBalSmrDao;
import com.kayak.subject.model.DwsAstPrdItmBalSmr;

@Service
@APIDefine(desc = "月度资产负债信息中间表服务", model = DwsAstPrdItmBalSmr.class)
@Slf4j
public class DwsAstPrdItmBalSmrService implements ExcelImportService<DwsAstPrdItmBalSmr> {

	@Autowired
	private DwsAstPrdItmBalSmrDao dwsAstPrdItmBalSmrDao;
	@Autowired
	private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;

	@Autowired
	private DwsProdTTRDBefService taskService;

	@API(desc = "查询月度资产负债信息中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsAstPrdItmBalSmr> findDwsAstPrdItmBalSmrs(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
		return dwsAstPrdItmBalSmrDao.findDwsAstPrdItmBalSmrs(params);
	}

	@API(desc = "添加月度资产负债信息中间表", params = "prod_cd,prod_intr_cd,ccy_cd,ctg_cd,amt_bal,act_dt", auth = APIAuth.NO)
	public int addDwsAstPrdItmBalSmr(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
		String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(params.getModel().getProdIntrCd());
		params.getModel().setProdCd(prdcCdPbc);
		return dwsAstPrdItmBalSmrDao.addDwsAstPrdItmBalSmr(params).getEffect();
	}
	
	@API(desc = "修改月度资产负债信息中间表", params = "prod_cd,prod_intr_cd,ccy_cd,ctg_cd,amt_bal,act_dt", auth = APIAuth.NO)
	public int updateDwsAstPrdItmBalSmr(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
		return dwsAstPrdItmBalSmrDao.updateDwsAstPrdItmBalSmr(params).getEffect();
	}
	
	@API(desc = "删除月度资产负债信息中间表", params = "prod_cd,prod_intr_cd,ccy_cd,ctg_cd,amt_bal,act_dt", auth = APIAuth.NO)
	public int deleteDwsAstPrdItmBalSmr(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
		return dwsAstPrdItmBalSmrDao.deleteDwsAstPrdItmBalSmr(params).getEffect();
	}

	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
		String reportDate = params.getModel().getActDt();
		String paraId = "90000052003";
		return taskService.execTaskApp(reportDate, paraId);
	}

	public int deleteDwsAstPrdItmBalSmr(DwsAstPrdItmBalSmr params) throws Exception {
		return dwsAstPrdItmBalSmrDao.deleteDwsAstPrdItmBalSmr(params).getEffect();
	}

	public String importDwsAstPrdItmBalSmr(MultipartFile file, Map<String, Object> params) throws Exception {
    		long startTime = System.currentTimeMillis();
    		log.info("导入月度资产负债信息中间表【{}】开始",file.getOriginalFilename());
    		final DwsAstPrdItmBalSmrService dwsAstPrdItmBalSmrService = this;
    		ExcelImportListener<DwsAstPrdItmBalSmr> excelImportListener = new ExcelImportListener<DwsAstPrdItmBalSmr>(params) {
    			@Override
    			protected ExcelImportService<DwsAstPrdItmBalSmr> getImportService() {
    				return dwsAstPrdItmBalSmrService;
    			}
    		};
    		try {
    			EasyExcel.read(file.getInputStream())
    					.head(DwsAstPrdItmBalSmr.class)
    					.registerReadListener(excelImportListener)
    					.sheet()
    					.doRead();
    		} catch (Exception e) {
    			throw new Exception(excelImportListener.getStopMsg());
    		}
    		log.info("导入月度资产负债信息中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
    		return excelImportListener.getStopMsg();
    	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsAstPrdItmBalSmr> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "REPLACE INTO dws_ast_prd_itm_bal_smr (prod_cd,prod_intr_cd,ccy_cd,ctg_cd,amt_bal,act_dt) VALUES(?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                for (DwsAstPrdItmBalSmr info : list) {
					// TODO 待优化
					String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(info.getProdIntrCd());
					info.setProdCd(prdcCdPbc);
                    ps.setString(1, info.getProdCd());
                    ps.setString(2, info.getProdIntrCd());
                    ps.setString(3, info.getCcyCd());
                    ps.setString(4, info.getCtgCd());
                    ps.setString(5, info.getAmtBal());
                    ps.setString(6, map.get("dealDate").toString());
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
