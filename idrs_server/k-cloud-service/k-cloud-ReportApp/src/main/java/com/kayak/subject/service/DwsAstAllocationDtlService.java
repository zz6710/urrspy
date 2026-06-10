package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.base.dao.ComnDao;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.DwsAstAllocationDtlDao;
import com.kayak.subject.model.DwsAstAllocationDtl;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@APIDefine(desc = "资产配置情况明细表服务", model = DwsAstAllocationDtl.class)
public class DwsAstAllocationDtlService implements ExcelImportService<DwsAstAllocationDtl> {

	@Autowired
	private DwsAstAllocationDtlDao dwsAstAllocationDtlDao;

	@Autowired
	private DwsProdTTRDBefService taskService;

	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		String reportDate = params.getModel().getReportDate();
		String paraId = "90000052008";
		return taskService.execTaskApp(reportDate, paraId);
	}

	@API(desc = "查询资产配置情况明细表信息", auth = APIAuth.YES)
	public SqlResult<DwsAstAllocationDtl> findDwsAstAllocationDtls(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		params.setMakeSql(true);
		return dwsAstAllocationDtlDao.findDwsAstAllocationDtls(params);
	}

	@API(desc = "添加资产配置情况明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,fin_deb_sec,non_fin_deb_sec,rat,non_fin_lab,bank_cap_sup,mkt_vol", auth = APIAuth.NO)
	public int addDwsAstAllocationDtl(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		return dwsAstAllocationDtlDao.addDwsAstAllocationDtl(params).getEffect();
	}
	
	@API(desc = "修改资产配置情况明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,fin_deb_sec,non_fin_deb_sec,rat,non_fin_lab,bank_cap_sup,mkt_vol", auth = APIAuth.NO)
	public int updateDwsAstAllocationDtl(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		return dwsAstAllocationDtlDao.updateDwsAstAllocationDtl(params).getEffect();
	}
	
	@API(desc = "删除资产配置情况明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,fin_deb_sec,non_fin_deb_sec,rat,non_fin_lab,bank_cap_sup,mkt_vol", auth = APIAuth.NO)
	public int deleteDwsAstAllocationDtl(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		return dwsAstAllocationDtlDao.deleteDwsAstAllocationDtl(params).getEffect();
	}

	/**
	 * 删除资产配置情况明细表
	 * @param dealDate 处理日期
	 * @return
	 */
	public int deleteDwsAstAllocationDtl(String dealDate) throws Exception{
		return dwsAstAllocationDtlDao.deleteDwsAstAllocationDtl(dealDate).getEffect();
	}

	/**
	 * 文件导入到资产配置情况明细表
	 * @param file 文件对象
	 * @param params 参数Map
	 * @return
	 * @throws Exception
	 */
	public String importDwsAstAllocationDtl(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入资产配置情况明细表【{}】开始",file.getOriginalFilename());
		final DwsAstAllocationDtlService importService = this;
		ExcelImportListener<DwsAstAllocationDtl> excelImportListener = new ExcelImportListener<DwsAstAllocationDtl>(params) {
			@Override
			protected ExcelImportService<DwsAstAllocationDtl> getImportService() {
				return importService;
			}
		};
		try {
			EasyExcel.read(file.getInputStream())
					.head(DwsAstAllocationDtl.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			throw new Exception(excelImportListener.getStopMsg());
		}
		log.info("导入资产配置情况明细表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

	@Autowired
	private ComnDao comnDao;

	@Override
	public void importFile(List<DwsAstAllocationDtl> list, Map map) throws Exception {
		String dealDate = map.get("dealDate").toString(); //处理日期
		long startTime = System.currentTimeMillis();
		String batchSql = "insert INTO dws_ast_allocation_dtl (report_date,prod_cd,prod_nm,scr_cd,scr_nm,fin_deb_sec,non_fin_deb_sec,rat,non_fin_lab,bank_cap_sup,mkt_vol) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (DwsAstAllocationDtl info : list) {
					ps.setString(1, dealDate);
					ps.setString(2, info.getProdCd());
					ps.setString(3, info.getProdNm());
					ps.setString(4, info.getScrCd());
					ps.setString(5,info.getScrNm());
					ps.setString(6, info.getFinDebSec() == null? null: info.getFinDebSec().split(" ")[0]); //是否债务证券（金融）
					ps.setString(7, info.getNonFinDebSec() == null? null: info.getNonFinDebSec().split(" ")[0]); //是否债务证券（非金融）
					ps.setString(8, info.getRat() == null? null: info.getRat().split(" ")[0]); //评级
					ps.setString(9, info.getNonFinLab() == null? null: info.getNonFinLab().split(" ")[0]); //非金融企业债分类
					ps.setString(10, info.getBankCapSup() == null? null: info.getBankCapSup().split(" ")[0]); //银行资本补充工具分类
					ps.setString(11,info.getMktVol());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
			} catch (Exception e) {
				log.error("导入资产配置情况明细表异常!", e);
				throw new Exception(e.getMessage());
			} finally {
				ps.close();
			}
		});
	}
}
