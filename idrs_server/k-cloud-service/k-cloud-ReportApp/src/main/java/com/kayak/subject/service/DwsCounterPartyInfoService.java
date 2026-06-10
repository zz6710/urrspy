package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.DwdPrdPrdBasInfDao;
import com.kayak.subject.dao.DwsAstMngPlanInfoDao;
import com.kayak.subject.dao.DwsCounterPartyInfoDao;
import com.kayak.subject.model.DwsAstMngPlanInfo;
import com.kayak.subject.model.DwsCounterPartyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@APIDefine(desc = "月度交易对手中间表服务", model = DwsCounterPartyInfo.class)
@Slf4j
public class DwsCounterPartyInfoService implements ExcelImportService<DwsCounterPartyInfo> {

	@Autowired
	private DwsCounterPartyInfoDao dwsCounterPartyInfoDao;
	@Autowired
	private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;
	@Autowired
	private DwsAstMngPlanInfoDao dwsAstMngPlanInfoDao;

	@Autowired
	private DwsProdTTRDBefService taskService;

	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询月度交易对手中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsCounterPartyInfo> findDwsCounterPartyInfos(SqlParam<DwsCounterPartyInfo> params) throws Exception {
		return dwsCounterPartyInfoDao.findDwsCounterPartyInfos(params);
	}

	@API(desc = "添加月度交易对手中间表", params = "prod_cd,prod_intr_cd,bred_cd,asset_cd,cntr_prod_type,cntr_org_cd,cntr_org_nm,cntr_prod_cd,cntr_prod_nm,ccy_cd,amt_bal,amt_bal_cny,act_dt", auth = APIAuth.NO)
	public int addDwsCounterPartyInfo(SqlParam<DwsCounterPartyInfo> params) throws Exception {
		String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(params.getModel().getProdIntrCd());
		params.getModel().setProdCd(prdcCdPbc);
		// 资产代码
		String assetCd = params.getModel().getAssetCd();
		if (Tools.isNotEmpty(assetCd)) {
			String actDt = DateUtil.getLastDayOfYearMonth(params.getModel().getActDt().replaceAll("-","")).replaceAll("-",""); //数据日期
			params.getModel().setActDt(actDt);
			String lastMonthLastDay = DateUtil.geMonthEndDay(actDt);
			String cntrProdNm = params.getModel().getCntrProdNm();// 交易对手产品名称
			// 查询历史报送数据
			Map<String, DwsCounterPartyInfo> historyMap = getHistoryMap(assetCd, lastMonthLastDay);
			DwsCounterPartyInfo dwsCounterPartyInfo = historyMap.get(assetCd);
			// 通过“交易对手产品名称”匹配SPV中的“产品名称”
			List<DwsAstMngPlanInfo> list = dwsAstMngPlanInfoDao.findDwsAstMngPlanInfoByProdNm(cntrProdNm, lastMonthLastDay);
			if (!list.isEmpty()) {
				// 将附件中的产品代码、发行机构代码 分配至 交易对手产品代码、交易对手机构编码
				String spvProdCd = list.get(0).getProdCd(); //产品代码
				String spvIssuerOrgnCd = list.get(0).getIssuerOrgnCd(); //发行机构代码
				params.getModel().setCntrProdCd(spvProdCd);//交易对手产品编码
				params.getModel().setCntrOrgCd(spvIssuerOrgnCd);//交易对手机构编码
				// 匹配
				if (Tools.isNotEmpty(spvProdCd) && dwsCounterPartyInfo != null && spvProdCd.equals(dwsCounterPartyInfo.getCntrProdCd())) {
					params.getModel().setStatus("1");// 成功
				} else if (Tools.isEmpty(spvProdCd) || dwsCounterPartyInfo == null || Tools.isEmpty(dwsCounterPartyInfo.getCntrProdCd())) {
					params.getModel().setStatus("2");
				} else {
					params.getModel().setStatus("2");
					params.getModel().setException("校验失败！上一期交易对手产品代码为：" + dwsCounterPartyInfo.getCntrProdCd());
				}
			}else{
				params.getModel().setStatus("1");// 成功
			}
		}
		params.getModel().setCrtDate(DateUtil.getNowDate());
		params.getModel().setCrtTime(DateUtil.getNowTime());
		return dwsCounterPartyInfoDao.addDwsCounterPartyInfo(params).getEffect();
	}
	
	@API(desc = "修改月度交易对手中间表", params = "prod_cd,prod_intr_cd,bred_cd,asset_cd,cntr_prod_type,cntr_org_cd,cntr_org_nm,cntr_prod_cd,cntr_prod_nm,ccy_cd,amt_bal,amt_bal_cny,act_dt", auth = APIAuth.NO)
	public int updateDwsCounterPartyInfo(SqlParam<DwsCounterPartyInfo> params) throws Exception {
		return dwsCounterPartyInfoDao.updateDwsCounterPartyInfo(params).getEffect();
	}

	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<DwsCounterPartyInfo> params) throws Exception {
		String reportDate = params.getModel().getActDt();
		String paraId = "90000052004";
		return taskService.execTaskApp(reportDate, paraId);
	}
	
	@API(desc = "删除月度交易对手中间表", params = "prod_cd,prod_intr_cd,bred_cd,asset_cd,cntr_prod_type,cntr_org_cd,cntr_org_nm,cntr_prod_cd,cntr_prod_nm,ccy_cd,amt_bal,amt_bal_cny,act_dt", auth = APIAuth.NO)
	public int deleteDwsCounterPartyInfo(SqlParam<DwsCounterPartyInfo> params) throws Exception {
		return dwsCounterPartyInfoDao.deleteDwsCounterPartyInfo(params).getEffect();
	}

	// 获取历史交易对手代码，交易对手机构编码
	public Map<String, DwsCounterPartyInfo> getHistoryMap(String assetCd, String lastMonthLastDay) throws Exception {
		List<DwsCounterPartyInfo> list = dwsCounterPartyInfoDao.findDwsCounterPartyInfoHistory(assetCd, lastMonthLastDay);
		return list.stream().collect(Collectors.toMap(DwsCounterPartyInfo::getAssetCd, row -> row));
	}

	public int deleteDwsCounterPartyInfo(DwsCounterPartyInfo params) throws Exception {
		return dwsCounterPartyInfoDao.deleteDwsCounterPartyInfo(params).getEffect();
	}

	public String importDwsCounterPartyInfo(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入月度交易对手中间表【{}】开始",file.getOriginalFilename());
		final DwsCounterPartyInfoService dwsCounterPartyInfoService = this;
		ExcelImportListener<DwsCounterPartyInfo> excelImportListener = new ExcelImportListener<DwsCounterPartyInfo>(params) {
			@Override
			protected ExcelImportService<DwsCounterPartyInfo> getImportService() {
				return dwsCounterPartyInfoService;
			}
		};
		try {
			EasyExcel.read(file.getInputStream())
					.head(DwsCounterPartyInfo.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			throw new Exception(excelImportListener.getStopMsg());
		}
		log.info("导入月度交易对手中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

    @Override
    public void importFile(List<DwsCounterPartyInfo> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "REPLACE INTO dws_counter_party_info (prod_cd,prod_intr_cd,bred_cd,asset_cd,cntr_prod_type,cntr_org_cd,cntr_org_nm,cntr_prod_cd,cntr_prod_nm,ccy_cd,amt_bal,amt_bal_cny,status,act_dt,crt_date,crt_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
		String actDtParam = DateUtil.getLastDayOfMonth(map.get("dealDate").toString());
		comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                for (DwsCounterPartyInfo info : list) {
					String prdcCdPbc = dwdPrdPrdBasInfDao.findPrdcCdPbc(info.getProdIntrCd());
					info.setProdCd(prdcCdPbc);
                    ps.setString(1, info.getProdCd());
                    ps.setString(2, info.getProdIntrCd());
                    ps.setString(3, info.getBredCd()== null ? null : info.getBredCd().split(" ")[0]);
                    ps.setString(4, info.getAssetCd());
                    ps.setString(5, info.getCntrProdType()== null ? null : info.getCntrProdType().split(" ")[0]);
                    ps.setString(6, info.getCntrOrgCd());
                    ps.setString(7, info.getCntrOrgNm());
                    ps.setString(8, info.getCntrProdCd());
                    ps.setString(9, info.getCntrProdNm());
                    ps.setString(10, info.getCcyCd() == null ? null : info.getCcyCd().split(" ")[0]);
                    ps.setString(11, info.getAmtBal());
                    ps.setString(12, info.getAmtBalCny());
                    ps.setString(13, "1");
                    ps.setString(14,actDtParam);
					ps.setString(15, DateUtil.getNowDate());
					ps.setString(16, DateUtil.getNowTime());
                    ps.addBatch();
                }
                ps.executeBatch();
                log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                log.error("导入月度交易对手信息异常!", e);
                throw new Exception(e.getMessage());
            } finally {
                ps.close();
            }
        });
    }

}
