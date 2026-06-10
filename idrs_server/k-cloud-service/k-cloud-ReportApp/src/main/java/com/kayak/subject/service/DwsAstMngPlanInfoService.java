package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.lang3.StringUtils;
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
import com.kayak.core.util.FileUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.model.DwsMonthNavInf;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.kayak.subject.dao.DwsAstMngPlanInfoDao;
import com.kayak.subject.model.DwsAstMngPlanInfo;

@Service
@APIDefine(desc = "公开spv信息服务", model = DwsAstMngPlanInfo.class)
@Slf4j
public class DwsAstMngPlanInfoService implements ExcelImportService<DwsAstMngPlanInfo> {

	@Autowired
	private DwsAstMngPlanInfoDao dwsAstMngPlanInfoDao;

	@API(desc = "查询公开spv信息信息", auth = APIAuth.YES)
	public SqlResult<DwsAstMngPlanInfo> findDwsAstMngPlanInfos(SqlParam<DwsAstMngPlanInfo> params) throws Exception {
		return dwsAstMngPlanInfoDao.findDwsAstMngPlanInfos(params);
	}

	@API(desc = "添加公开spv信息", params = "prod_cd,prod_nm,issuer_orgn_cd,issuer_orgn_nm,prod_bred_cd,prod_opn_dt,prod_up_dt,prod_expc_end_dt,prod_actl_end_dt,act_dt", auth = APIAuth.NO)
	public int addDwsAstMngPlanInfo(SqlParam<DwsAstMngPlanInfo> params) throws Exception {
		return dwsAstMngPlanInfoDao.addDwsAstMngPlanInfo(params).getEffect();
	}
	
	@API(desc = "修改公开spv信息", params = "prod_cd,prod_nm,issuer_orgn_cd,issuer_orgn_nm,prod_bred_cd,prod_opn_dt,prod_up_dt,prod_expc_end_dt,prod_actl_end_dt,act_dt", auth = APIAuth.NO)
	public int updateDwsAstMngPlanInfo(SqlParam<DwsAstMngPlanInfo> params) throws Exception {
		return dwsAstMngPlanInfoDao.updateDwsAstMngPlanInfo(params).getEffect();
	}
	
	@API(desc = "删除公开spv信息", params = "prod_cd,prod_nm,issuer_orgn_cd,issuer_orgn_nm,prod_bred_cd,prod_opn_dt,prod_up_dt,prod_expc_end_dt,prod_actl_end_dt,act_dt", auth = APIAuth.NO)
	public int deleteDwsAstMngPlanInfo(SqlParam<DwsAstMngPlanInfo> params) throws Exception {
		return dwsAstMngPlanInfoDao.deleteDwsAstMngPlanInfo(params).getEffect();
	}

	public int deleteDwsAstMngPlanInfo(DwsAstMngPlanInfo params) throws Exception {
		return dwsAstMngPlanInfoDao.deleteDwsAstMngPlanInfo(params).getEffect();
	}

	public int truncateDwsAstMngPlanInfo(DwsAstMngPlanInfo params) throws Exception {
		return dwsAstMngPlanInfoDao.truncateDwsAstMngPlanInfo(params).getEffect();
	}

	public String importDwsAstMngPlanInfo(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入公开spv信息【{}】开始",file.getOriginalFilename());
		final DwsAstMngPlanInfoService dwsAstMngPlanInfoService = this;
		ExcelImportListener<DwsAstMngPlanInfo> excelImportListener = new ExcelImportListener<DwsAstMngPlanInfo>(params) {
			@Override
			protected ExcelImportService<DwsAstMngPlanInfo> getImportService() {
				return dwsAstMngPlanInfoService;
			}
		};
		try {
			EasyExcel.read(file.getInputStream())
					.head(DwsAstMngPlanInfo.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			log.error(excelImportListener.getStopMsg(), e);
			throw new Exception(excelImportListener.getStopMsg());
		}
		log.info("导入公开spv信息【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsAstMngPlanInfo> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
		String batchSql = "REPLACE INTO dws_ast_mng_plan_info (prod_cd,prod_nm,issuer_orgn_cd,issuer_orgn_nm,prod_bred_cd,prod_opn_dt,prod_up_dt,prod_expc_end_dt,prod_actl_end_dt,act_dt,crt_date,crt_time)" +
				" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			DateTimeFormatter fromFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
			DateTimeFormatter toFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (DwsAstMngPlanInfo info : list) {
					ps.setString(1, info.getProdCd());
					ps.setString(2, info.getProdNm());
					ps.setString(3, info.getIssuerOrgnCd());
					ps.setString(4, info.getIssuerOrgnNm());
					ps.setString(5, info.getProdBredCd());
					String prodOpnDt = info.getProdOpnDt() == null ? "" : LocalDate.parse(info.getProdOpnDt(), fromFormatter).format(toFormatter);
					ps.setString(6, prodOpnDt);
					String prodUpDt = info.getProdUpDt() == null ? "" : LocalDate.parse(info.getProdUpDt(), fromFormatter).format(toFormatter);
					ps.setString(7, prodUpDt);
					String prodExpcEndDt = info.getProdExpcEndDt() == null ? "" : LocalDate.parse(info.getProdExpcEndDt(), fromFormatter).format(toFormatter);
					ps.setString(8, prodExpcEndDt);
					String prodActlEndDt = info.getActDt() == null ? "" : LocalDate.parse(info.getProdActlEndDt(), fromFormatter).format(toFormatter);
					ps.setString(9, prodActlEndDt);
					String actDt = info.getActDt() == null ? "" : LocalDate.parse(info.getActDt(), fromFormatter).format(toFormatter);
					ps.setString(10, actDt);
					ps.setString(11, info.getCrtDate());
					ps.setString(12, info.getCrtTime());
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

	/**
	 * 读取CSV文件行数据
	 * @param file
	 * @param charset
	 * @return
	 */
	public static String readFile(MultipartFile file, String charset) {
		String content;
		try (InputStream fr = file.getInputStream(); BufferedReader input = new BufferedReader(new InputStreamReader(fr, charset))) {
			StringBuilder buffer = new StringBuilder();
			String text;
			while ((text = input.readLine()) != null) {
				if (buffer.length() > 0) {
					buffer.append("\n");
				}
				buffer.append(text);
			}
			content = buffer.toString();
		} catch (Exception e) {
			content = "";
		}
		return content;
	}

	/**
	 * 导入CSV文件
	 * @param file
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String importDwsAstMngPlanInfoCsv(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入公开spv信息【{}】开始",file.getOriginalFilename());

		List<DwsAstMngPlanInfo> dwsAstMngPlanInfoList = new ArrayList<>();
		try{
			String  fileData = readFile(file,"utf-8");
			if(StringUtils.isNotBlank(fileData)){
				String[] rows01 = fileData.split("\n");
				int startRow = 1;
				for (int a  = startRow;a<rows01.length;a++){
					String row = rows01[a];
					String[] values01 = row.split(",");
					DwsAstMngPlanInfo dwsAstMngPlanInfo = new DwsAstMngPlanInfo();
					int len = values01.length;
					dwsAstMngPlanInfo.setProdCd(values01[1]);
					dwsAstMngPlanInfo.setIssuerOrgnCd(values01[2]);
					dwsAstMngPlanInfo.setIssuerOrgnNm(values01[3]);
					dwsAstMngPlanInfo.setProdBredCd(values01[4]);
					dwsAstMngPlanInfo.setProdNm(values01[5]);
					dwsAstMngPlanInfo.setProdOpnDt(values01[6]);
					dwsAstMngPlanInfo.setProdUpDt(len>7?values01[7]:null);
					dwsAstMngPlanInfo.setProdExpcEndDt(len>8?values01[8]:null);
					dwsAstMngPlanInfo.setProdActlEndDt(len>9?values01[9]:null);
					dwsAstMngPlanInfoList.add(dwsAstMngPlanInfo);

					if(a%1000 == 0){
						this.csvImportFile(dwsAstMngPlanInfoList);
						dwsAstMngPlanInfoList.clear();
					}
				}
			}
			this.csvImportFile(dwsAstMngPlanInfoList);
		} catch (Exception e) {
			throw new Exception("导入公开spv信息【{}】异常：" +e.getMessage());
		}
		log.info("导入公开spv信息【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return "";
	}

	/**
	 * 批量导入csv数据
	 * @param list
	 * @throws Exception
	 */
	public void csvImportFile(List<DwsAstMngPlanInfo> list) throws Exception {
		long startTime = System.currentTimeMillis();
		if (list == null || list.isEmpty()) {
			throw new Exception("没有数据");
		}
		String batchSql = "REPLACE INTO dws_ast_mng_plan_info (prod_cd,prod_nm,issuer_orgn_cd,issuer_orgn_nm,prod_bred_cd,prod_opn_dt,prod_up_dt,prod_expc_end_dt,prod_actl_end_dt,act_dt,crt_date,crt_time)" +
				" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			SimpleDateFormat toFormatter = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat fromFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String crt_date = DateUtil.getNowDate();
			String crt_time = DateUtil.getNowTime();

			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (DwsAstMngPlanInfo info : list) {
					ps.setString(1, info.getProdCd());
					ps.setString(2, info.getProdNm());
					ps.setString(3, info.getIssuerOrgnCd());
					ps.setString(4, info.getIssuerOrgnNm());
					ps.setString(5, info.getProdBredCd());
					ps.setString(6, info.getProdOpnDt() == null || "".equals(info.getProdOpnDt()) ? "" : toFormatter.format(fromFormatter.parse(info.getProdOpnDt())));
					ps.setString(7, info.getProdUpDt() == null || "".equals(info.getProdUpDt()) ? "" : toFormatter.format(fromFormatter.parse(info.getProdUpDt())));
					ps.setString(8, info.getProdExpcEndDt() == null || "".equals(info.getProdExpcEndDt()) ? "" : toFormatter.format(fromFormatter.parse(info.getProdExpcEndDt())));
					ps.setString(9, info.getProdActlEndDt() == null || "".equals(info.getProdActlEndDt()) ? "" : toFormatter.format(fromFormatter.parse(info.getProdActlEndDt())));
					ps.setString(10, info.getActDt() == null || "".equals(info.getActDt()) ? "" : toFormatter.format(fromFormatter.parse(info.getActDt())));
					ps.setString(11, crt_date);
					ps.setString(12, crt_time);
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
			} catch (Exception e) {
				log.error("导入公开SPV数据异常!", e);
				throw new Exception(e.getMessage());
			} finally {
				ps.close();
			}
		});
	}


}
