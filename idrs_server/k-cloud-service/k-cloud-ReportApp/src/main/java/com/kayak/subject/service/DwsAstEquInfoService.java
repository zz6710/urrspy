package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.cache.dao.CacheDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.DwdPrdPrdBasInfDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.kayak.subject.dao.DwsAstEquInfoDao;
import com.kayak.subject.model.DwsAstEquInfo;

@Service
@APIDefine(desc = "贷款明细和收益权明细中间表服务", model = DwsAstEquInfo.class)
@Slf4j
public class DwsAstEquInfoService implements ExcelImportService<DwsAstEquInfo> {

	@Autowired
	private DwsAstEquInfoDao dwsAstEquInfoDao;
	@Autowired
	private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;
	@Autowired
	private CacheDao cacheDao;

	@Autowired
	private DwsProdTTRDBefService taskService;


	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<DwsAstEquInfo> params) throws Exception {
		String reportDate = params.getModel().getActDt();
		String paraId = "90000052005";
		return taskService.execTaskApp(reportDate, paraId);
	}

	@API(desc = "查询贷款明细和收益权明细中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsAstEquInfo> findDwsAstEquInfos(SqlParam<DwsAstEquInfo> params) throws Exception {
		return dwsAstEquInfoDao.findDwsAstEquInfos(params);
	}

	@API(desc = "添加贷款明细和收益权明细中间表", auth = APIAuth.NO)
	public int addDwsAstEquInfo(SqlParam<DwsAstEquInfo> params) throws Exception {
		String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(params.getModel().getProdIntrCd());
		params.getModel().setProdCd(prdcCdPbc);
		List<SqlRow> asst3KndRows = cacheDao.findDictItems("asst_3_knd");
		Map<String, String> asst3KndDict = asst3KndRows.stream().collect(Collectors.toMap(row -> row.getString("itemkey"), row -> row.getString("itemval")));
		String asstThrNm = asst3KndDict.get(params.getModel().getAstThrCd());
		params.getModel().setAstThrNm(asstThrNm);
		params.getModel().setCrtDate(DateUtil.getNowDate());
		params.getModel().setCrtTime(DateUtil.getNowTime());
		return dwsAstEquInfoDao.addDwsAstEquInfo(params).getEffect();
	}
	
	@API(desc = "修改贷款明细和收益权明细中间表", auth = APIAuth.NO)
	public String updateDwsAstEquInfo(SqlParam<DwsAstEquInfo> params) throws Exception {
		try {
			List<SqlRow> asst3KndRows = cacheDao.findDictItems("asst_3_knd");
			Map<String, String> asst3KndDict = asst3KndRows.stream().collect(Collectors.toMap(row -> row.getString("itemkey"), row -> row.getString("itemval")));
			String asstThrNm = asst3KndDict.get(params.getModel().getAstThrCd());
			params.getModel().setAstThrNm(asstThrNm);
			params.getModel().setUpdDate(DateUtil.getNowDate());
			params.getModel().setUpdTime(DateUtil.getNowTime());
			dwsAstEquInfoDao.updateDwsAstEquInfo(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch(Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
		}

	}
	
	@API(desc = "删除贷款明细和收益权明细中间表", auth = APIAuth.NO)
	public String deleteDwsAstEquInfo(SqlParam<DwsAstEquInfo> params) throws Exception {
		try {
			dwsAstEquInfoDao.deleteDwsAstEquInfo(params).getEffect();
			return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
		}
	}

	public int deleteDwsAstEquInfo(DwsAstEquInfo params) throws Exception {
		return dwsAstEquInfoDao.deleteDwsAstEquInfo(params).getEffect();
	}

	public String importDwsAstEquInfo(MultipartFile file, Map<String, Object> params) throws Exception {
    		long startTime = System.currentTimeMillis();
    		log.info("导入贷款明细和收益权明细中间表【{}】开始",file.getOriginalFilename());
    		final DwsAstEquInfoService dwsAstEquInfoService = this;
    		ExcelImportListener<DwsAstEquInfo> excelImportListener = new ExcelImportListener<DwsAstEquInfo>(params) {
    			@Override
    			protected ExcelImportService<DwsAstEquInfo> getImportService() {
    				return dwsAstEquInfoService;
    			}
    		};
    		try {
    			EasyExcel.read(file.getInputStream())
    					.head(DwsAstEquInfo.class)
    					.registerReadListener(excelImportListener)
    					.sheet()
    					.doRead();
    		} catch (Exception e) {
    			throw new Exception(excelImportListener.getStopMsg());
    		}
    		log.info("导入贷款明细和收益权明细中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
    		return excelImportListener.getStopMsg();
    	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsAstEquInfo> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
        String batchSql = "REPLACE INTO dws_ast_equ_info (prod_cd,prod_intr_cd,ast_cd,ast_thr_cd,ast_thr_nm,ast_typ_dbt_pjt,zon_cd,dbt_nm,dbt_typ,dbt_cd,idt_typ,inv_icm_pct,etp_scl,opn_dt,end_dt,exp_end_dt,intr_rt_typ,intr_rt,gur_typ,ccy_cd,intr_amt_bal,amt_bal,rgt_trd_plc,rgt_trd_plc_cd,shr_hld_inv_typ,shr_hld_tsf_cd,shr_hld_tsf_nm,inv_out_typ,act_dt,crt_date,crt_time,tech_flag,green_flag,spec_flag,aged_flag,num_core_flag,trans_org_out_table_f,trans_org_buy_back_f,base_asset_trans_dep,base_asset_ori_prot_amt,base_asset_inv_obj_idt,base_asset_inv_obj_scale) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String actDtParam = DateUtil.getLastDayOfMonth(map.get("dealDate").toString());
		comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                for (DwsAstEquInfo info : list) {
					// TODO 待优化
					String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(info.getProdIntrCd());
					info.setProdCd(prdcCdPbc);
                    ps.setString(1, info.getProdCd());
                    ps.setString(2, info.getProdIntrCd());
                    ps.setString(3, info.getAstCd());
                    ps.setString(4, info.getAstThrCd());
                    ps.setString(5, info.getAstThrNm());
                    ps.setString(6, info.getAstTypDbtPjt());
                    ps.setString(7, info.getZonCd()== null ? null : info.getZonCd().split(" ")[0]);
                    ps.setString(8, info.getDbtNm());
                    ps.setString(9, info.getDbtTyp()== null ? null : info.getDbtTyp().split(" ")[0]);
                    ps.setString(10, info.getDbtCd());
                    ps.setString(11, info.getIdtTyp()== null ? null : info.getIdtTyp().split(" ")[0]);
                    ps.setString(12, info.getInvIcmPct()== null ? null : info.getInvIcmPct().split(" ")[0]);
                    ps.setString(13, info.getEtpScl()== null ? null : info.getEtpScl().split(" ")[0]);
					ps.setString(14, info.getOpnDt());
					ps.setString(15, info.getEndDt());
					ps.setString(16, info.getExpEndDt());
					ps.setString(17, info.getIntrRtTyp()== null ? null : info.getIntrRtTyp().split(" ")[0]);
					ps.setString(18, info.getIntrRt());
					ps.setString(19, info.getGurTyp()== null ? null : info.getGurTyp().split(" ")[0]);
					ps.setString(20, info.getCcyCd());
					ps.setString(21, info.getIntrAmtBal());
					ps.setString(22, info.getAmtBal());
					ps.setString(23, info.getRgtTrdPlc()== null ? null : info.getRgtTrdPlc().split(" ")[0]);
					ps.setString(24, info.getRgtTrdPlcCd());
					ps.setString(25, info.getShrHldInvTyp()== null ? null : info.getShrHldInvTyp().split(" ")[0]);
					ps.setString(26, info.getShrHldTsfCd());
					ps.setString(27, info.getShrHldTsfNm());
					ps.setString(28, info.getInvOutTyp()== null ? null : info.getInvOutTyp().split(" ")[0]);
                    ps.setString(29, actDtParam);
					ps.setString(30, DateUtil.getNowDate());
					ps.setString(31, DateUtil.getNowTime());
					ps.setString(32, info.getTechFlag()== null ? null : info.getTechFlag().split(" ")[0]);
					ps.setString(33, info.getGreenFlag()== null ? null : info.getGreenFlag().split(" ")[0]);
					ps.setString(34, info.getSpecFlag()== null ? null : info.getSpecFlag().split(" ")[0]);
					ps.setString(35, info.getAgedFlag()== null ? null : info.getAgedFlag().split(" ")[0]);
					ps.setString(36, info.getNumCoreFlag()== null ? null : info.getNumCoreFlag().split(" ")[0]);
					ps.setString(37, info.getTransOrgOutTableF()== null ? null : info.getTransOrgOutTableF().split(" ")[0]);
					ps.setString(38, info.getTransOrgBuyBackF()== null ? null : info.getTransOrgBuyBackF().split(" ")[0]);
					ps.setString(39, info.getBaseAssetTransDep()== null ? null : info.getBaseAssetTransDep().split(" ")[0]);
					ps.setString(40, info.getBaseAssetOriProtAmt()== null ? null : info.getBaseAssetOriProtAmt().split(" ")[0]);
					ps.setString(41, info.getBaseAssetInvObjIdt()== null ? null : info.getBaseAssetInvObjIdt().split(" ")[0]);
					ps.setString(42, info.getBaseAssetInvObjScale()== null ? null : info.getBaseAssetInvObjScale().split(" ")[0]);
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
