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
import com.kayak.subject.dao.DwsAstDebPbnkDtlDao;
import com.kayak.subject.model.DwsAstDebPbnkDtl;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@APIDefine(desc = "资产负债剩余期限明细表服务", model = DwsAstDebPbnkDtl.class)
public class DwsAstDebPbnkDtlService implements ExcelImportService<DwsAstDebPbnkDtl> {

	@Autowired
	private DwsAstDebPbnkDtlDao dwsAstDebPbnkDtlDao;

	@Autowired
	private DwsProdTTRDBefService taskService;

	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		String reportDate = params.getModel().getReportDate();
		String paraId = "90000052007";
		return taskService.execTaskApp(reportDate, paraId);
	}

	@API(desc = "查询资产负债剩余期限明细表信息", auth = APIAuth.YES)
	public SqlResult<DwsAstDebPbnkDtl> findDwsAstDebPbnkDtls(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		params.setMakeSql(true);
		return dwsAstDebPbnkDtlDao.findDwsAstDebPbnkDtls(params);
	}

	@API(desc = "添加资产负债剩余期限明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,asst_type,asst_clss,mtu_dt,prod_trm_pbnk,mkt_vol", auth = APIAuth.NO)
	public int addDwsAstDebPbnkDtl(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		return dwsAstDebPbnkDtlDao.addDwsAstDebPbnkDtl(params).getEffect();
	}
	
	@API(desc = "修改资产负债剩余期限明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,asst_type,asst_clss,mtu_dt,prod_trm_pbnk,mkt_vol", auth = APIAuth.NO)
	public int updateDwsAstDebPbnkDtl(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		return dwsAstDebPbnkDtlDao.updateDwsAstDebPbnkDtl(params).getEffect();
	}
	
	@API(desc = "删除资产负债剩余期限明细表", params = "id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,asst_type,asst_clss,mtu_dt,prod_trm_pbnk,mkt_vol", auth = APIAuth.NO)
	public int deleteDwsAstDebPbnkDtl(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		return dwsAstDebPbnkDtlDao.deleteDwsAstDebPbnkDtl(params).getEffect();
	}

	/**
	 * 根据处理日期删除资产负债剩余期限明细表
	 * @param dealDate 处理日期
	 * @return
	 */
	public int deleteDwsAstDebPbnkDtl(String dealDate) throws Exception{
		return dwsAstDebPbnkDtlDao.deleteDwsAstDebPbnkDtl(dealDate).getEffect();
	}

	/**
	 * 导入资产负债剩余期限明细表
	 * @param file 文件对象
	 * @param params 参数
	 * @return
	 * @throws Exception
	 */
	public String importDwsAstDebPbnkDtl(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入资产负债剩余期限明细表【{}】开始",file.getOriginalFilename());
		final DwsAstDebPbnkDtlService importService = this;
		ExcelImportListener<DwsAstDebPbnkDtl> excelImportListener = new ExcelImportListener<DwsAstDebPbnkDtl>(params) {
			@Override
			protected ExcelImportService<DwsAstDebPbnkDtl> getImportService() {
				return importService;
			}
		};
		try {
			EasyExcel.read(file.getInputStream())
					.head(DwsAstDebPbnkDtl.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			throw new Exception(excelImportListener.getStopMsg());
		}
		log.info("导入资产负债剩余期限明细表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

	@Autowired
	private ComnDao comnDao;

	@Override
	public void importFile(List<DwsAstDebPbnkDtl> list, Map map) throws Exception {
		String dealDate = map.get("dealDate").toString(); //处理日期
		long startTime = System.currentTimeMillis();
		String batchSql = "insert INTO dws_ast_deb_pbnk_dtl (report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,asst_type,asst_clss,mtu_dt,prod_trm_pbnk,mkt_vol) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (DwsAstDebPbnkDtl info : list) {
					ps.setString(1, dealDate);
					ps.setString(2, info.getProdCd());
					ps.setString(3, info.getProdNm());
					ps.setString(4, info.getScrCd());
					ps.setString(5,info.getScrNm());
					ps.setString(6, info.getAsstThrKnd());
					ps.setString(7, info.getAsstType() == null? null:info.getAsstType().split(" ")[0]); //资产方/负债方
					ps.setString(8, info.getAsstClss() == null? null:info.getAsstClss().split(" ")[0]); //资产类型
					ps.setString(9, info.getMtuDt());
					ps.setString(10, info.getProdTrmPbnk() == null? null: info.getProdTrmPbnk().split(" ")[0]); //剩余期限
					ps.setString(11,info.getMktVol());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
			} catch (Exception e) {
				log.error("导入资产负债剩余期限明细表异常!", e);
				throw new Exception(e.getMessage());
			} finally {
				ps.close();
			}
		});
	}
}
