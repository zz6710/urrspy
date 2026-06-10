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
import com.kayak.subject.dao.BaseSubjectMapDao;
import com.kayak.subject.model.BaseSubjectMap;
import com.kayak.subject.model.DwsAstMngPlanInfo;
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
@APIDefine(desc = "报表指标科目映射表服务", model = BaseSubjectMap.class)
@Slf4j
public class BaseSubjectMapService implements ExcelImportService<BaseSubjectMap> {

	@Autowired
	private BaseSubjectMapDao baseSubjectMapDao;

	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询报表指标科目映射表信息", auth = APIAuth.YES)
	public SqlResult<BaseSubjectMap> findBaseSubjectMaps(SqlParam<BaseSubjectMap> params) throws Exception {
		return baseSubjectMapDao.findBaseSubjectMaps(params);
	}

	@API(desc = "添加报表指标科目映射表", params = "id,report_name,account_code,asst_3_knd,ctg_cd,asst_cd,remark,inputuser,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int addBaseSubjectMap(SqlParam<BaseSubjectMap> params) throws Exception {
		params.getModel().setCrtDate(DateUtil.getNowDate());
		params.getModel().setCrtTime(DateUtil.getNowTime());
		return baseSubjectMapDao.addBaseSubjectMap(params).getEffect();
	}
	
	@API(desc = "修改报表指标科目映射表", params = "id,report_name,account_code,asst_3_knd,ctg_cd,asst_cd,remark,inputuser,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int updateBaseSubjectMap(SqlParam<BaseSubjectMap> params) throws Exception {
		params.getModel().setUpdDate(DateUtil.getNowDate());
		params.getModel().setUpdTime(DateUtil.getNowTime());
		return baseSubjectMapDao.updateBaseSubjectMap(params).getEffect();
	}
	
	@API(desc = "删除报表指标科目映射表", params = "id,report_name,account_code,asst_3_knd,ctg_cd,asst_cd,remark,inputuser,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int deleteBaseSubjectMap(SqlParam<BaseSubjectMap> params) throws Exception {
		return baseSubjectMapDao.deleteBaseSubjectMap(params).getEffect();
	}

	public int truncateBaseSubjectMap(BaseSubjectMap params) throws Exception {
		return baseSubjectMapDao.truncateBaseSubjectMap(params).getEffect();
	}

	public String importBaseSubjectMap(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入报表指标科目映射【{}】开始",file.getOriginalFilename());
		final BaseSubjectMapService baseSubjectMapService = this;
		ExcelImportListener<BaseSubjectMap> excelImportListener = new ExcelImportListener<BaseSubjectMap>(params) {
			@Override
			protected ExcelImportService<BaseSubjectMap> getImportService() {
				return baseSubjectMapService;
			}
		};
		truncateBaseSubjectMap(new BaseSubjectMap());
		try {
			EasyExcel.read(file.getInputStream())
					.head(BaseSubjectMap.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			throw new Exception(excelImportListener.getStopMsg());
		}
		log.info("导入报表指标科目映射【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

	@Autowired
	private CacheDao cacheDao;

	@Override
	public void importFile(List<BaseSubjectMap> list, Map map) throws Exception {
		long startTime = System.currentTimeMillis();
		List<SqlRow> asst3KndRows = cacheDao.findDictItems("asst_3_knd");
		Map<String, String> asst3KndDict = asst3KndRows.stream().collect(Collectors.toMap(row -> row.getString("itemkey"), row -> row.getString("itemval")));
		List<SqlRow> ctgCdRows = cacheDao.findDictItems("ctg_cd");
		Map<String, String> ctgCdRowsDict = ctgCdRows.stream().collect(Collectors.toMap(row -> row.getString("itemval"), row -> row.getString("itemkey")));
		String batchSql = "REPLACE INTO base_subject_map (report_name, account_code, asst_3_knd, ctg_cd, asst_cd, remark, inputuser, crt_date, crt_time) VALUES(?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
				for (BaseSubjectMap info : list) {
					String asst3Knd = info.getAsst3Knd();
					if (Tools.isNotEmpty(asst3Knd) && Tools.isEmpty(asst3KndDict.get(asst3Knd))) {
						throw new PromptException(asst3Knd + "的码值不存在！");
					}
					String ctgCd = info.getCtgCd();
					if (Tools.isNotEmpty(ctgCd) && Tools.isEmpty(ctgCdRowsDict.get(ctgCd))) {
						throw new PromptException(ctgCd + "的码值不存在！");
					}
					ps.setString(1, info.getReportName());
					ps.setString(2, info.getAccountCode());
					ps.setString(3, info.getAsst3Knd());
					ps.setString(4, ctgCdRowsDict.get(ctgCd));
					ps.setString(5, info.getAsstCd());
					ps.setString(6, info.getRemark());
					ps.setString(7, userid);
					ps.setString(8, DateUtil.getNowDate());
					ps.setString(9, DateUtil.getNowTime());
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
