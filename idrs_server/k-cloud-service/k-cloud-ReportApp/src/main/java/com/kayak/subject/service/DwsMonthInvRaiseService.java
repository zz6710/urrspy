package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.cache.dao.CacheDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.DwdPrdPrdBasInfDao;
import com.kayak.subject.dao.DwsMonthInvRaiseDao;
import com.kayak.subject.model.DwsMonthInvRaise;
import com.kayak.subject.model.DwsProdTTRDBef;
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
@APIDefine(desc = "月度募集信息中间表服务", model = DwsMonthInvRaise.class)
@Slf4j
public class DwsMonthInvRaiseService implements ExcelImportService<DwsMonthInvRaise> {

	@Autowired
	private DwsMonthInvRaiseDao dwsMonthInvRaiseDao;
	@Autowired
	private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;

	@Autowired
	private DwsProdTTRDBefService taskService;

	@Autowired
	private CacheDao cacheDao;

	@API(desc = "查询月度募集信息中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsMonthInvRaise> findDwsMonthInvRaises(SqlParam<DwsMonthInvRaise> params) throws Exception {
		return dwsMonthInvRaiseDao.findDwsMonthInvRaises(params);
	}

	@API(desc = "添加月度募集信息中间表", params = "id,deal_date,prdc_cd_pbc,prdc_cd,zon_cd,inv_typ,orgn_inv_type,busi_type,hold_amt,hold_vol,crt_dt,crt_tm", auth = APIAuth.NO)
	public int addDwsMonthInvRaise(SqlParam<DwsMonthInvRaise> params) throws Exception {
		String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(params.getModel().getPrdcCd());
		params.getModel().setPrdcCdPbc(prdcCdPbc);
		params.getModel().setCrtDt(DateUtil.getNowDate());
		params.getModel().setCrtTm(DateUtil.getNowTime());
		return dwsMonthInvRaiseDao.addDwsMonthInvRaise(params).getEffect();
	}
	
	@API(desc = "修改月度募集信息中间表", params = "id,deal_date,prdc_cd_pbc,prdc_cd,zon_cd,inv_typ,orgn_inv_type,busi_type,hold_amt,hold_vol,crt_dt,crt_tm", auth = APIAuth.NO)
	public int updateDwsMonthInvRaise(SqlParam<DwsMonthInvRaise> params) throws Exception {
		return dwsMonthInvRaiseDao.updateDwsMonthInvRaise(params).getEffect();
	}
	
	@API(desc = "删除月度募集信息中间表", params = "id,deal_date,prdc_cd_pbc,prdc_cd,zon_cd,inv_typ,orgn_inv_type,busi_type,hold_amt,hold_vol,crt_dt,crt_tm", auth = APIAuth.NO)
	public int deleteDwsMonthInvRaise(SqlParam<DwsMonthInvRaise> params) throws Exception {
		return dwsMonthInvRaiseDao.deleteDwsMonthInvRaise(params).getEffect();
	}

	public int deleteDwsMonthInvRaise(DwsMonthInvRaise dwsMonthInvRaise) throws Exception {
		return dwsMonthInvRaiseDao.deleteDwsMonthInvRaise(dwsMonthInvRaise).getEffect();
	}

	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<DwsMonthInvRaise> params) throws Exception {
		String reportDate = params.getModel().getDealDate();
		String paraId = "90000052001";
		return taskService.execTaskApp(reportDate, paraId);
	}

	public String importDwsMonthInvRaise(MultipartFile file, Map<String, Object> params) throws Exception {
    		long startTime = System.currentTimeMillis();
    		log.info("导入月度募集信息中间表【{}】开始",file.getOriginalFilename());
    		final DwsMonthInvRaiseService dwsMonthInvRaiseService = this;
    		ExcelImportListener<DwsMonthInvRaise> excelImportListener = new ExcelImportListener<DwsMonthInvRaise>(params) {
    			@Override
    			protected ExcelImportService<DwsMonthInvRaise> getImportService() {
    				return dwsMonthInvRaiseService;
    			}
    		};
    		try {
    			EasyExcel.read(file.getInputStream())
    					.head(DwsMonthInvRaise.class)
    					.registerReadListener(excelImportListener)
    					.sheet()
    					.doRead();
    		} catch (Exception e) {
    			throw new Exception(excelImportListener.getStopMsg());
    		}
    		log.info("导入月度募集信息中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
    		return excelImportListener.getStopMsg();
    	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsMonthInvRaise> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "REPLACE INTO dws_month_inv_raise (deal_date,prdc_cd_pbc,prdc_cd,zon_cd,inv_typ,orgn_inv_type,busi_type,hold_amt,hold_vol,crt_dt,crt_tm)" +
				" VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
		comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
				// 缓存
				HashMap<String,String> cache =new HashMap<>();
                for (DwsMonthInvRaise info : list) {
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
                    ps.setString(4, info.getZonCd());
                    ps.setString(5, info.getInvTyp());
                    ps.setString(6, info.getOrgnInvType());
                    ps.setString(7, info.getBusiType());
                    ps.setString(8, info.getHoldAmt());
                    ps.setString(9, info.getHoldVol());
					ps.setString(10, DateUtil.getNowDate());
					ps.setString(11, DateUtil.getNowTime());
                    ps.addBatch();
                }
                ps.executeBatch();
                log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                log.error("导入月度募集信息中间表异常!", e);
                throw new Exception(e.getMessage());
            } finally {
                ps.close();
            }
        });
    }

}
