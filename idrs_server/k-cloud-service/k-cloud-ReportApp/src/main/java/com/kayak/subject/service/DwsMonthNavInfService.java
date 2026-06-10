package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.DwdPrdPrdBasInfDao;
import com.kayak.subject.dao.DwsMonthNavInfDao;
import com.kayak.subject.model.DwsMonthInvRaise;
import com.kayak.subject.model.DwsMonthNavInf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "月度净值信息中间表服务", model = DwsMonthNavInf.class)
@Slf4j
public class DwsMonthNavInfService implements ExcelImportService<DwsMonthNavInf> {

	@Autowired
	private DwsMonthNavInfDao dwsMonthNavInfDao;
	@Autowired
	private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;

	@Autowired
	private DwsProdTTRDBefService taskService;

	@API(desc = "查询月度净值信息中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsMonthNavInf> findDwsMonthNavInfs(SqlParam<DwsMonthNavInf> params) throws Exception {
		return dwsMonthNavInfDao.findDwsMonthNavInfs(params);
	}

	@API(desc = "添加月度净值信息中间表", params = "id,deal_date,prdc_cd_pbc,prdc_cd,unt_nav,acm_nav,rct_1m_grw_rat,crt_dt,crt_tm,remaining_days,remaining_term", auth = APIAuth.NO)
	public int addDwsMonthNavInf(SqlParam<DwsMonthNavInf> params) throws Exception {
		String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(params.getModel().getPrdcCd());
		params.getModel().setPrdcCdPbc(prdcCdPbc);
		params.getModel().setCrtDt(DateUtil.getNowDate());
		params.getModel().setCrtTm(DateUtil.getNowTime());
		return dwsMonthNavInfDao.addDwsMonthNavInf(params).getEffect();
	}
	
	@API(desc = "修改月度净值信息中间表", params = "id,deal_date,prdc_cd_pbc,prdc_cd,unt_nav,acm_nav,rct_1m_grw_rat,crt_dt,crt_tm,remaining_days,remaining_term", auth = APIAuth.NO)
	public int updateDwsMonthNavInf(SqlParam<DwsMonthNavInf> params) throws Exception {
		return dwsMonthNavInfDao.updateDwsMonthNavInf(params).getEffect();
	}
	
	@API(desc = "删除月度净值信息中间表", params = "id,deal_date,prdc_cd_pbc,prdc_cd,unt_nav,acm_nav,rct_1m_grw_rat,crt_dt,crt_tm,remaining_days,remaining_term", auth = APIAuth.NO)
	public int deleteDwsMonthNavInf(SqlParam<DwsMonthNavInf> params) throws Exception {
		return dwsMonthNavInfDao.deleteDwsMonthNavInf(params).getEffect();
	}

	public int deleteDwsMonthNavInf(DwsMonthNavInf params) throws Exception {
		return dwsMonthNavInfDao.deleteDwsMonthNavInf(params).getEffect();
	}

	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<DwsMonthNavInf> params) throws Exception {
		String reportDate = params.getModel().getDealDate();
		String paraId = "90000052002";
		return taskService.execTaskApp(reportDate, paraId);
	}

	public String importDwsMonthNavInf(MultipartFile file, Map<String, Object> params) throws Exception {
    		long startTime = System.currentTimeMillis();
    		log.info("导入月度净值信息中间表【{}】开始",file.getOriginalFilename());
    		final DwsMonthNavInfService dwsMonthNavInfService = this;
    		ExcelImportListener<DwsMonthNavInf> excelImportListener = new ExcelImportListener<DwsMonthNavInf>(params) {
    			@Override
    			protected ExcelImportService<DwsMonthNavInf> getImportService() {
    				return dwsMonthNavInfService;
    			}
    		};
    		try {
    			EasyExcel.read(file.getInputStream())
    					.head(DwsMonthNavInf.class)
    					.registerReadListener(excelImportListener)
    					.sheet()
    					.doRead();
    		} catch (Exception e) {
    			throw new Exception(excelImportListener.getStopMsg());
    		}
    		log.info("导入月度净值信息中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
    		return excelImportListener.getStopMsg();
    	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsMonthNavInf> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "REPLACE INTO dws_month_nav_inf (deal_date,prdc_cd_pbc,prdc_cd,unt_nav,acm_nav,rct_1m_grw_rat,crt_dt,crt_tm,remaining_days,remaining_term) VALUES(?,?,?,?,?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
				// 缓存
				HashMap<String,String> cache =new HashMap<>();
                for (DwsMonthNavInf info : list) {
					if(StringUtils.isEmpty(info.getPrdcCdPbc())) {
						String prdcCdPbc = cache.get(info.getPrdcCd());
						if(StringUtils.isEmpty( prdcCdPbc)) {
							prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(info.getPrdcCd());
							cache.put(info.getPrdcCd(), prdcCdPbc);
						}
						info.setPrdcCdPbc(prdcCdPbc);
					}
                    ps.setString(1, map.get("dealDate").toString());
                    ps.setString(2, info.getPrdcCdPbc());
                    ps.setString(3, info.getPrdcCd());
                    ps.setString(4, info.getUntNav());
                    ps.setString(5, info.getAcmNav());
                    ps.setString(6, info.getRct1mGrwRat());
                    ps.setString(7, DateUtil.getNowDate());
                    ps.setString(8, DateUtil.getNowTime());
                    ps.setString(9, info.getRemainingDays());
                    ps.setString(10, info.getRemainingTerm() == null ? null : info.getRemainingTerm().split(" ")[0]);
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
