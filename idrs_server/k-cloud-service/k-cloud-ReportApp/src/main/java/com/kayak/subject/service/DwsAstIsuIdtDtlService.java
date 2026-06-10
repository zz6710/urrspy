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
import com.kayak.subject.dao.DwsAstIsuIdtDtlDao;
import com.kayak.subject.model.DwsAstIsuIdtDtl;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@APIDefine(desc = "企业债按行业、企业规模统计明细表服务", model = DwsAstIsuIdtDtl.class)
public class DwsAstIsuIdtDtlService implements ExcelImportService<DwsAstIsuIdtDtl> {

	@Autowired
	private DwsAstIsuIdtDtlDao dwsAstIsuIdtDtlDao;

	@Autowired
	private DwsProdTTRDBefService taskService;

	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		String reportDate = params.getModel().getReportDate();
		String paraId = "90000052006";
		return taskService.execTaskApp(reportDate, paraId);
	}

	@API(desc = "查询企业债按行业、企业规模统计明细表信息", auth = APIAuth.YES)
	public SqlResult<DwsAstIsuIdtDtl> findDwsAstIsuIdtDtls(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		params.setMakeSql(true);
		return dwsAstIsuIdtDtlDao.findDwsAstIsuIdtDtls(params);
	}

	@API(desc = "添加企业债按行业、企业规模统计明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,isu_org_nm,isu_org_idt,isu_org_vol,mkt_vol", auth = APIAuth.NO)
	public int addDwsAstIsuIdtDtl(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		return dwsAstIsuIdtDtlDao.addDwsAstIsuIdtDtl(params).getEffect();
	}
	
	@API(desc = "修改企业债按行业、企业规模统计明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,isu_org_nm,isu_org_idt,isu_org_vol,mkt_vol", auth = APIAuth.NO)
	public int updateDwsAstIsuIdtDtl(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		return dwsAstIsuIdtDtlDao.updateDwsAstIsuIdtDtl(params).getEffect();
	}
	
	@API(desc = "删除企业债按行业、企业规模统计明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,isu_org_nm,isu_org_idt,isu_org_vol,mkt_vol", auth = APIAuth.NO)
	public int deleteDwsAstIsuIdtDtl(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		return dwsAstIsuIdtDtlDao.deleteDwsAstIsuIdtDtl(params).getEffect();
	}

	/**
	 * 根据处理日期删除数据
	 * @param dealDate 处理日期
	 * @return
	 * @throws Exception
	 */
	public int deleteDwsAstIsuIdtDtlByDealDate(String dealDate) throws Exception {
		return dwsAstIsuIdtDtlDao.deleteDwsAstIsuIdtDtl(dealDate).getEffect();
	}

	public String importDwsAstIsuIdtDtl(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入企业债按行业、企业规模统计明细表中间表【{}】开始",file.getOriginalFilename());
		final DwsAstIsuIdtDtlService importService = this;
		ExcelImportListener<DwsAstIsuIdtDtl> excelImportListener = new ExcelImportListener<DwsAstIsuIdtDtl>(params) {
			@Override
			protected ExcelImportService<DwsAstIsuIdtDtl> getImportService() {
				return importService;
			}
		};
		try {
			EasyExcel.read(file.getInputStream())
					.head(DwsAstIsuIdtDtl.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			throw new Exception(excelImportListener.getStopMsg());
		}
		log.info("导入企业债按行业、企业规模统计明细表中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

	@Autowired
	private ComnDao comnDao;

	@Override
	public void importFile(List<DwsAstIsuIdtDtl> list, Map map) throws Exception {
		String dealDate = map.get("dealDate").toString(); //处理日期
		long startTime = System.currentTimeMillis();
		String batchSql = "insert INTO dws_ast_isu_idt_dtl (report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,isu_org_nm,isu_org_idt,isu_org_vol,mkt_vol) VALUES(?,?,?,?,?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (DwsAstIsuIdtDtl info : list) {
					ps.setString(1, dealDate);
					ps.setString(2, info.getProdCd());
					ps.setString(3, info.getProdNm());
					ps.setString(4, info.getScrCd());
					ps.setString(5,info.getScrNm());
					ps.setString(6, info.getAsstThrKnd());
					ps.setString(7, info.getIsuOrgNm());
					ps.setString(8, info.getIsuOrgIdt() == null? null: info.getIsuOrgIdt().split(" ")[0]); //发行人行业
					ps.setString(9, info.getIsuOrgVol() == null? null: info.getIsuOrgVol().split(" ")[0]); //发行人规模
					ps.setString(10, info.getMktVol());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
			} catch (Exception e) {
				log.error("导入企业债按行业、企业规模统计明细表中间表异常!", e);
				throw new Exception(e.getMessage());
			} finally {
				ps.close();
			}
		});
	}
}
