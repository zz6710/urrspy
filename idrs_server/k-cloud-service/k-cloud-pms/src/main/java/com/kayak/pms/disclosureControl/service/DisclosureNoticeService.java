package com.kayak.pms.disclosureControl.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.ResponseResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.PublicUtils;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.basePublish.dao.DisclosureChannelRuleDao;
import com.kayak.pms.basePublish.dao.DisclosureRuleDao;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.basePublish.model.DisclosureModVersion;
import com.kayak.pms.basePublish.model.DisclosureRegularAsset;
import com.kayak.pms.basePublish.model.DisclosureRule;
import com.kayak.pms.basePublish.service.DisclosureModVersionService;
import com.kayak.pms.disclosureControl.dao.*;
import com.kayak.pms.disclosureControl.disclousreEnum.ErrorTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.NoticeFileTypeEnum;
import com.kayak.pms.disclosureControl.model.*;
import com.kayak.pms.global.constants.DisclosureStatus;
import com.kayak.pms.global.constants.IsDocking;
import com.kayak.pms.global.constants.XpStatus;
import com.kayak.pms.printTemp.utils.WordToPdfUtil;
import com.kayak.pms.prod.dao.T8ProdCustodianEmailDao;
import com.kayak.pms.prod.model.T8ProdCustodianEmail;
import com.kayak.utils.ErrorMessageHandlerUtil;
import com.kayak.utils.OnlineUtils;
import com.kayak.utils.SftpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.docx4j.TraversalUtil;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.finders.RangeFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.docx4j.wml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@APIDefine(desc = "产品信披公告服务", model = DisclosureNotice.class)
@RefreshScope
@SuppressWarnings({"rawtypes","unchecked","deprecation"})
public class DisclosureNoticeService {
	private Logger logger = LoggerFactory.getLogger(DisclosureNoticeService.class);

	ArrayBlockingQueue<Runnable> queues = new ArrayBlockingQueue<Runnable>(1000);

	private final ExecutorService es = new ThreadPoolExecutor(2, 10, 2000,
			TimeUnit.MILLISECONDS, queues);
	// 文件分隔符
	private static final String separate = File.separator;
	@Autowired
	private DisclosureNoticeDao disclosureNoticeDao;
	@Autowired
	private DisclosureNoticeProcessDao disNoticeProcessDao;

	@Autowired
	private T8ProdCustodianEmailDao t8ProdCustodianEmailDao;

	@Autowired
	private DisclosureNoticeDocDao disclosureNoticeDocDao;

	@Autowired
	private DisclosureNoticeChannelDao disclosureNoticeChannelDao;

	@Autowired
	private DisclosureNoticeChannelService disclosureNoticeChannelService;

	@Autowired
	private DisclosureRuleDao disclosureRuleDao;

	@Autowired
	private DisclosureModVersionService disclosureModVersionService;

	@Autowired
	private DisclosureNoticeVersionDao disclosureNoticeVersionDao;

	@Autowired
	private DisclosureOperationService disclosureOperationService;

	@Autowired
	private DisclosureNoticeDocService disclosureNoticeDocService;

	@Autowired
	private DisclosureChannelRuleDao disclosureChannelRuleDao;

	private String fileStorePath;

	static String onlineUrl = Strings.EMPTY;

	@Value("${path.word}")
	private String basePath;

	@Autowired
	private WordToPdfUtil wordToPdfUtil;

	@API(desc = "查询产品信披公告信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNotice> findDisclosureNoticesAuth(SqlParam<DisclosureNotice> params) throws Exception {
		return findDisclosureNotices(params);
	}

	@API(desc = "查询产品信披公告信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNotice> findDisclosureNotices(SqlParam<DisclosureNotice> params) throws Exception {
		SqlResult<DisclosureNotice> result = disclosureNoticeDao.findDisclosureNotices(params);
		return result;
	}

	@API(desc = "查询信息批量详情信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNotice> findDisclosureNDetails(SqlParam<DisclosureNotice> params) throws Exception {
		String prePlanDate="";
		String nextPlanDate="";
		String planFbDate=params.getModel().getPlanFbDate();
		nextPlanDate = disclosureNoticeDao.getNextWorkday(planFbDate, "001").getString("workday");
		SqlResult<DisclosureNotice> result = disclosureNoticeDao.findDisclosureNDetails(params,planFbDate,nextPlanDate);
		return result;
	}

	@API(desc = "添加产品信披公告", params = "id,t8_prod_info_id,prod_code,prod_name,t8_disclosure_rule_id,notice_title,plan_fb_date,plan_sp_date,plan_bl_date,real_bl_date,real_sp_date,real_fb_date,stage,prod_base_date,publish_status,approval_status,eba_status,review_status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name", auth = APIAuth.NO)
	public int addDisclosureNotice(SqlParam<DisclosureNotice> params) throws Exception {
		return disclosureNoticeDao.addDisclosureNotice(params).getEffect();
	}

	@API(desc = "修改产品信披公告", params = "id,t8_prod_info_id,prod_code,prod_name,t8_disclosure_rule_id,notice_title,plan_fb_date,plan_sp_date,plan_bl_date,real_bl_date,real_sp_date,real_fb_date,stage,prod_base_date,publish_status,approval_status,eba_status,review_status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name", auth = APIAuth.NO)
	public int updateDisclosureNotice(SqlParam<DisclosureNotice> params) throws Exception {
		return disclosureNoticeDao.updateDisclosureNotice(params).getEffect();
	}

	@API(desc = "修改手工公告状态", params = "id,t8_prod_info_id,prod_code,prod_name,t8_disclosure_rule_id,notice_title,plan_fb_date,plan_sp_date,plan_bl_date,real_bl_date,real_sp_date,real_fb_date,stage,prod_base_date,publish_status,approval_status,eba_status,review_status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name", auth = APIAuth.NO)
	public int updateDisclosureNoticeForDelete(SqlParam<DisclosureNotice> params) throws Exception {
		return disclosureNoticeDao.updateDisclosureNoticeForDelete(params).getEffect();
	}

	@API(desc = "删除产品信披公告", params = "id,t8_prod_info_id,prod_code,prod_name,t8_disclosure_rule_id,notice_title,plan_fb_date,plan_sp_date,plan_bl_date,real_bl_date,real_sp_date,real_fb_date,stage,prod_base_date,publish_status,approval_status,eba_status,review_status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name", auth = APIAuth.NO)
	public int deleteDisclosureNotice(SqlParam<DisclosureNotice> params) throws Exception {
		return disclosureNoticeDao.deleteDisclosureNotice(params).getEffect();
	}

	@API(desc = "查询产品托管行信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNotice> findTrusteeName(SqlParam<DisclosureNotice> params) throws Exception {
		SqlResult<DisclosureNotice> result = disclosureNoticeDao.findTrusteeName(params);
		return result;
	}
	/**
	 * 此方法作用是让上传文档可以进行权限控制
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@API(desc = "上传文档", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String upadteDoc(SqlParam<DisclosureNotice> param) throws Exception {
		return null;
	}

	@API(desc = "定期公告审批拒绝回调", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public ResponseResult DisclosureNoticeRejectFlow(DisclosureNotice param) throws Exception {
		DaoUtil.doTrans(() -> {
			disclosureNoticeDao.updateDisclosureNoticeStatusReject(param);
		});
		logger.info(
				"*********************************************--拒绝审批回调函数调用成功!!!!!--********************************************");
		ResponseResult responseResult = new ResponseResult<>("200");
		responseResult.setStatus("200");
		responseResult.setMessage("审批完成!");
		return responseResult;
	}

	@API(desc = "信披公告发起审批流", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public ResponseResult startFlowDisclosure(DisclosureNotice param) throws Exception {
		DaoUtil.doTrans(() -> {
			disclosureNoticeDao.updateDisclosureNoticeStatusStart(param);
		});
		logger.info(
				"*********************************************--发起审批流回调函数调用成功!!!!!--********************************************");
		ResponseResult responseResult = new ResponseResult<>("200");
		responseResult.setStatus("200");
		responseResult.setMessage("审批完成!");
		return responseResult;
	}

	@API(desc = "定期公告复核拒绝回调", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public ResponseResult DisclosureNoticeTrusteeRejectFlow(DisclosureTruteeApproval param) throws Exception {
		DisclosureNotice dis = new DisclosureNotice();
		dis.setId(param.getDisclosureNoticeId());
		dis.setProdCode(param.getProdCode());
		// dis.setTrusteeApprovalId(param.getId());
		DaoUtil.doTrans(() -> {
			disclosureNoticeDao.updateDisclosureNoticeTrusteeStatusReject(dis);
		});
		System.out.println(
				"*********************************************--拒绝审批回调函数调用成功!!!!!--********************************************");
		ResponseResult responseResult = new ResponseResult<>("200");
		responseResult.setStatus("200");
		responseResult.setMessage("审批完成!");
		return responseResult;
	}

	@API(desc = "定期公告复核审批通过回调", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public ResponseResult DisclosureNoticeTrusteeAgreeFlow(DisclosureTruteeApproval param) throws Exception {
		DisclosureNotice dis = new DisclosureNotice();
		dis.setId(param.getDisclosureNoticeId());
		dis.setProdCode(param.getProdCode());;
		//dis.setTrusteeApprovalId(param.getId());
		disclosureNoticeDao.updateDisclosureNoticeTrusteeStatusAgree(dis);
		System.out.println(
				"*********************************************--同意审批回调函数调用成功!!!!!--********************************************");
		ResponseResult responseResult = new ResponseResult<>("200");
		responseResult.setStatus("200");
		responseResult.setMessage("审批完成!");
		return responseResult;
	}

	@API(desc = "修改产品信披公告状态", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String updateDisclosureNoticeStatus(SqlParam<DisclosureNotice> params) throws Exception {

		disclosureNoticeDao.updateDisclosureNoticeStatus(params);
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "修改产品信披公告状态(无审批流)", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String updateDisclosureNoticeStatusNo(SqlParam<DisclosureNotice> params) throws Exception {

		disclosureNoticeDao.updateDisclosureNoticeStatus2(params);
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "修改产品信披公告及渠道状态", auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public String updateDisclosureNoticeChannel(SqlParam<DisclosureNotice> params) throws Exception {

		disclosureNoticeDao.updateDisclosureNoticeChannel(params);
		return RequestSupport.updateReturnJson(true, "流程开启成功", null).toString();
	}

	/**
	 * 功能：查询信披渠道规则信息列表 作者：zls 日期：20210524
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询信披渠道规则信息列表", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNotice> findChannelRuleList(SqlParam<DisclosureNotice> param) throws Exception {
		// param.setMakeSql(true);
		return disclosureNoticeDao.findChannelRuleList(param);
	}

	/**
	 * 功能：查询私募资管产品明细信息 作者：zls 日期：20210524
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询私募资管产品明细信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public List<SqlRow> findDetailList(String prodCode, String ntoiceId, String baseDate) throws Exception {
		// param.setMakeSql(true);
		return disclosureNoticeDao.findDetailListForNotice(prodCode, ntoiceId, baseDate);
	}

	/**
	 * 功能：查询私募资管产品明细信息 作者：zls 日期：20210524
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询私募资管产品明细信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public String findDetail(String ntoiceId) throws Exception {
		// param.setMakeSql(true);
		return disclosureNoticeDao.findDetail(ntoiceId);
	}

	/**
	 * 功能：查询十大资产明细信息 作者：zls 日期：20210524
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询十大资产明细信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public List<SqlRow> findTenDetailList(String param) throws Exception {
		// param.setMakeSql(true);
		return disclosureNoticeDao.findTenDetailList(param);
	}

	/**
	 * 功能：公告模板下载,病填充数据 作者：zls 日期：20210525
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询公告数据源信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlRow findDataInfoByType(Map<String, Object> params) throws Exception {
		// param.setMakeSql(true);
		return disclosureNoticeDao.findDataInfoByType(params);
	}

	@API(desc = "查询是否为用户组用户", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<SqlRow> findUserInfo(SqlParam<DisclosureNotice> param) throws Exception {
		if (StringUtils.isBlank(param.getModel().getCrtUserId())) {
			param.getModel().setCrtUserId((String) SysUtil.getSysUserParamValue("sys_user_userid"));
		}
		return disclosureNoticeDao.findUserInfo(param);
	}

	@API(desc = "查询用户组用户角色", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<SqlRow> findUserInfoInGroup(SqlParam<DisclosureNotice> param) throws Exception {

		return disclosureNoticeDao.findUserInfoInGroup(param);
	}


	@API(desc = "查询可录入角色", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<SqlRow> findUserInGroup(SqlParam<DisclosureNotice> param) throws Exception {
		SqlResult result = new SqlResult();
		DisclosureNoticeProcess process = new DisclosureNoticeProcess();
		process.setUserId(param.getModel().getCrtUserId());
		process.setT8DisclosureNoticeId(param.getModel().getId());
		List<SqlRow> list = disNoticeProcessDao.findProcesssRole(process);
		result.setRows(list);
		result.setResults(list.size());
		return result;
	}

	@API(desc = "查询资产配置数据是否一致", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public String findAssetsInfo(SqlParam<DisclosureNotice> param) throws Exception {
		Map<String, Object> map = disclosureNoticeDao.findAssetsInfo(param);

		return RequestSupport.updateReturnJson(true, "查询成功", map).toString();
	}

	/**
	 * 功能：根据信披规则id查找模板字段对应的角色 作者：rennannan 日期：20210604
	 *
	 * @param ruleId
	 * @return
	 * @throws Exception
	 */
	public List<String> findRoleList(String ruleId) throws Exception {
		List<DisclosureNotice> scheduleList = disclosureNoticeDao.findRoleIdsByRuleId(ruleId);
		List<String> rolesList = new ArrayList();
		if (scheduleList.size() > 0) {
			String rolesStr = "";
			for (DisclosureNotice notice : scheduleList) {
				rolesStr += notice.getCrtUserId() + ",";
			}
			String[] strs = rolesStr.split(",");

			for (int i = 0; i < strs.length; i++) {
				if (!rolesList.contains(strs[i])) {
					rolesList.add(strs[i]);
				}
			}
		}
		return rolesList;
	}

	@API(desc = "批量取消", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String batchCancel(SqlParam<Object> object) throws Exception {
		Map<String, Object> paramsDirect = RequestSupport.getParameters();
		String obj = (String) paramsDirect.get("list");
		List<HashMap> compareList = JSONObject.parseArray(obj, HashMap.class);
		List<HashMap> disProds = new ArrayList();
		DaoUtil.doTrans(() -> {
			compareList.forEach(notice -> {
				Map<String, Object> upd_params = new HashMap<>();
				List<DisclosureNotice> noticeList = null;
				try {
					// 批量取消，除了公告已发布，都可以取消
					noticeList = disclosureNoticeDao.findCountsByCode(notice.get("id").toString());
					if (CollectionUtil.isEmpty(noticeList)) {
						upd_params.put("disclosureStatusAfter", DisclosureStatus.close.getItemKey());
						upd_params.put("changeReason", "批量取消发布操作");
						upd_params.put("disclosureStatusAhead", notice.get("disclosureStatus"));
						upd_params.put("noticeVersionId", notice.get("noticeVersionId"));
						upd_params.put("t8DisclosureNoticeId", notice.get("id"));
						upd_params.put("crtDate", PublicUtils.getSysWordDay());
						upd_params.put("crtTime", DateUtil.formatDate(new Date(),DateFormatEnum.TIME_FORMAT));
						upd_params.put("crtUserId", SysUtil.getSysUserParamValue("sys_user_userid"));
						upd_params.put("crtUserName", SysUtil.getSysUserParamValue("sys_user_username"));
						disclosureNoticeDao.updateDisclosureNoticeStatus(upd_params);//批量变更公告状态
						disclosureNoticeDao.updateDisclosureVersionStatus(upd_params);//批量变更最新版本状态
						disclosureNoticeDao.updateDisclosureChannelStatus(upd_params);//批量变更版本发布的所有渠道状态
						disclosureNoticeDao.insertDisclosureNoticeRecord(upd_params);//插入记录
					} else {
						disProds.add(notice);
					}
				} catch (Exception e) {
					log.error("数据库操作异常{}", e);
				}

			});
		});
		String msg = disProds.stream().map((data) -> {
			return (String) data.get("prodCode");
		}).collect(Collectors.joining(","));
		String[] split = msg.split(",");
		if (msg.length() > 0) {
			if (split.length == compareList.size()) {
				return RequestSupport.updateReturnJson(false, "操作失败,产品" + msg + "已发布,请勿取消", null).toString();
			}
			return RequestSupport.updateReturnJson(true, "操作成功,产品" + msg + "已发布,请勿取消", null).toString();
		} else {
			return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
		}
	}


	@API(desc = "获取公告数量", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public String queryCount(SqlParam<Object> object) throws Exception {

		int count = 0;
		Map<String, Object> paramsDirect = RequestSupport.getParameters();
		String obj = (String) paramsDirect.get("list");
		List<Map> jsonList = JSONObject.parseArray(obj, Map.class);
		List<DisclosureNotice> noticeList = new ArrayList<DisclosureNotice>();

		for (Map json : jsonList) {
			DisclosureNotice notice = BeanUtil.mapToBean(json, DisclosureNotice.class, false);
			noticeList.add(notice);
		}

		if (CollectionUtil.isEmpty(jsonList)) {
			SqlParam<DisclosureNotice> params = new FetcherData<DisclosureNotice>(paramsDirect, DisclosureNotice.class);
			params.setLimit(Integer.MAX_VALUE);
			SqlResult<DisclosureNotice> rs = findDisclosureNoticesAuth(params);
			noticeList = rs.getRows();
		}

		noticeList = noticeList.parallelStream().filter((notice)->{
			SqlRow row =null;
			try {
				row = disclosureNoticeDao.noticeFilter(notice);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("查询数据库异常[{}]",e);
			}
			if (row!=null)
				return true;
			return false;
		}).collect(Collectors.toList());
		Set<String> tghs = new HashSet<String>();
		noticeList.forEach(notice -> {
			T8ProdCustodianEmail tgh;
			try {
				tgh = t8ProdCustodianEmailDao.findT8ProdCustodianEmailByCode(notice.getProdCode());
				if(tgh==null)
					return;
				//notice.setHandlingMailbox(tgh.getHandlingMailbox());
				//notice.setOperatingAgency(tgh.getOperatingAgency());
				tghs.add(tgh.getHandlingMailbox());
			} catch (Exception e) {
				log.error("查询托管行邮箱异常{}", e);
			}
		});

		for (String tgh : tghs) {//此处变更
			for (DisclosureNotice notice : noticeList) {
				if (tgh.equals(notice.getIsSendEmail())) {

					count++;
				}
			}
		}
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("count", count);
		return RequestSupport.updateReturnJson(true, "操作成功", data).toString();
	}


	@API(desc = "批量发布渠道总数", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public String queryPublishCount(SqlParam<Object> object) throws Exception {
		//根据前端查询条件/勾选的信批组装 notice集合
		Map<String, Object> paramsDirect = RequestSupport.getParameters();
		String obj = (String) paramsDirect.get("list");
		List<Map> jsonList = JSONObject.parseArray(obj, Map.class);
		List<DisclosureNotice> noticeList = new ArrayList<DisclosureNotice>();

		for (Map json : jsonList) {
			DisclosureNotice notice = BeanUtil.mapToBean(json, DisclosureNotice.class, false);
			noticeList.add(notice);
		}

		if (CollectionUtil.isEmpty(jsonList)) {
			SqlParam<DisclosureNotice> params = new FetcherData<DisclosureNotice>(paramsDirect, DisclosureNotice.class);
			params.setLimit(Integer.MAX_VALUE);
			SqlResult<DisclosureNotice> rs = findDisclosureNoticesAuth(params);
			noticeList = rs.getRows();
		}

		Map<String,Object> data = new HashMap<String,Object>();

		String noticeIds = Strings.EMPTY;
		for(DisclosureNotice notice : noticeList) {
			if(noticeList.indexOf(notice)==noticeList.size()-1) {
				noticeIds +=notice.getId();
			}else {
				noticeIds = noticeIds+notice.getId()+",";
			}
		}
		SqlRow sqlRow = disclosureNoticeDao.queryPublishCount(noticeIds);

		data.put("count", sqlRow.getString("count"));
		return RequestSupport.updateReturnJson(true, "操作成功", data).toString();
	}

	/**
	 * 信批公告批量发布渠道
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@API(desc = "批量发布渠道", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String batchPublishChannel(SqlParam<Object> object) throws Exception {
		try {
			//根据前端查询条件/勾选的信批组装  notice集合
			Map<String, Object> paramsDirect = RequestSupport.getParameters();
			String obj = (String) paramsDirect.get("list");
			List<Map<String,Object>> pubInfoList = new ArrayList<>();
//			String pub_date = String.valueOf(paramsDirect.get("pubDate"));
			String pub_date = DateUtil.getNowDate();

			List<Map> jsonList = JSONObject.parseArray(obj, Map.class);//获取所有公告List集合
			List<DisclosureNotice> noticeList = new ArrayList<>();

			for (Map json : jsonList) {
				DisclosureNotice notice = BeanUtil.mapToBean(json, DisclosureNotice.class, false);
				noticeList.add(notice);
			}

			if (CollectionUtil.isEmpty(jsonList)) {//list为空
				SqlParam<DisclosureNotice> params = new FetcherData<DisclosureNotice>(paramsDirect, DisclosureNotice.class);
				params.setLimit(Integer.MAX_VALUE);
				SqlResult<DisclosureNotice> rs = findDisclosureNoticesAuth(params);
				noticeList = rs.getRows();
			}

			//获取notice所属的渠道
			String noticeIds = Strings.EMPTY;
			for(DisclosureNotice notice : noticeList) {
				if(noticeList.indexOf(notice)==noticeList.size()-1) {
					noticeIds += notice.getId();
				}else {
					noticeIds = noticeIds + notice.getId() + ",";
				}
			}

			/**
			 * 查询需要发布的渠道信息,根据渠道对公告进行轮循处理
			 */
			List<SqlRow> channels = disclosureNoticeDao.findChannels(noticeIds);
			/**
			 * 当前遍历渠道iD
			 */
			String nowChannelId = "";
			/**
			 * 获取当日信批文件提交批次信息
			 */
			String batch_no = disclosureNoticeDao.getNoticeFileSubmitBatchNo(pub_date);
			/**
			 * 当前遍历公告版本id
			 */
			String nowNoticeVersionId =  "";
			for (SqlRow channelInfo :channels) {
				nowChannelId = channelInfo.getString("disclosure_notice_channel_id");//渠道id
				String disclosureNoticeList = channelInfo.getString("disclosure_notice_id");//渠道需要发布的公告id集
				String[] noticeIdList = disclosureNoticeList.split(",");
				/** 获取需要发布的渠道信息 */
				DisclosureChannel disclosureChannel = new DisclosureChannel();
				disclosureChannel.setHostIp(channelInfo.getString("host_ip"));
				disclosureChannel.setId(nowChannelId);
				disclosureChannel.setChannelName(channelInfo.getString("channel_name"));
				disclosureChannel.setPortCode(channelInfo.getString("port_code"));
				disclosureChannel.setUserName(channelInfo.getString("user_name"));
				disclosureChannel.setPassword(channelInfo.getString("password"));
				disclosureChannel.setFilePath(channelInfo.getString("file_path"));
				disclosureChannel.setIsDocking(channelInfo.getString("is_docking"));
				disclosureChannel.setStatus(channelInfo.getString("status"));
				/**
				 * 遍历公告，获取指定渠道指定公告进行发布
				 */
				for(int i = 0; i< noticeIdList.length;i++){
					String disclosureNoticeId = noticeIdList[i];//信批公告id
					//获取公告最大版本号
					String notice_version_id = disclosureNoticeDocService.getDisclosureMaxVersionId(disclosureNoticeId);//获取公告版本号,发布取最新版本号
					nowNoticeVersionId = notice_version_id;
					//通过公告id获得公告信息
					SqlRow disclosureNoticeInfo = disclosureNoticeChannelDao.findDisclosureNoticeById(notice_version_id);
					if(disclosureNoticeInfo.isEmpty()){//若公告信息为空，则跳出
						ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "公告id为" + disclosureNoticeId +"的公告找不到公告信息!");//插入错误日志
						continue;
					}

					/**
					 * 在文件发布上传路径检索最新版本的公告文件是否存在，
					 * 若不存在则调用生成方法并返回上传docx文件路径
					 */
					String noticeLatestVersionId = disclosureNoticeDocService.getDisclosureMaxVersionId(disclosureNoticeId);//获取公告版本号,发布取最新版本号
					String doc_name = disclosureNoticeDocService.getNoticeDocName(disclosureNoticeId);//获取公告标题
					Map<String, Object> pubFile_params = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(disclosureNoticeId,noticeLatestVersionId,doc_name, false);

					/**
					 * 根据信批类型、子类型及发布的渠道以确定需要发布的文件类型，文件名(文件名为文件名_需要转换类型.docx文件)及发布路径等信息，最后根据该配置信息转换发布路径下docx文件为对应文件(文件名称，文件格式、文件类型)并复制命名文件
					 * pubInfoList存在多条数据时必定为同一公告发布同一渠道存在多个文件的情况
					 * */
					pubFile_params.put("channelId",channelInfo.getString("disclosure_notice_channel_id"));
					pubInfoList.addAll(disclosureNoticeDocService.getPubFileInfoByNoticeChannel(pubFile_params, disclosureChannel));//需要推送的文件list集合
				}
				List<String> pubFileInfoList = new ArrayList<>();//同一渠道下某一公告发布文件各个类型文件名称记录集合，所以其发布的ftp地址及路径都相同
				//List<String> versionIdList = new ArrayList<>();
				/** 公告发布渠道(公告文件上传服务器) */
				for (Map<String, Object> pubMap : pubInfoList) {
					try {
						if (IsDocking.no.getItemKey().equals(String.valueOf(pubMap.get("is_docking"))) || XpStatus.stop.getItemKey().equals(pubMap.get("status"))) {//无需发布的渠道直接跳过
							continue;
						}

						SftpUtils.putFile(disclosureChannel.getHostIp(), disclosureChannel.getUserName(), disclosureChannel.getPassword(),
								String.valueOf(pubMap.get("file_path")).replace("{deal_date}", pub_date), String.valueOf(pubMap.get("local_path")),
								String.valueOf(pubMap.get("file_name")));
						SftpUtils.putFile(disclosureChannel.getHostIp(), disclosureChannel.getUserName(), disclosureChannel.getPassword(),
								String.valueOf(pubMap.get("file_path")).replace("{deal_date}", pub_date), String.valueOf(pubMap.get("local_ok_path")),
								String.valueOf(pubMap.get("ok_file_name")));
						//变更公告状态等
						pubMap.put("pub_date", pub_date);
						pubMap.put("pub_time", DateUtil.formatDate(new Date(), DateFormatEnum.TIME_FORMAT));
						pubMap.put("id", disclosureNoticeDocService.getFncSendFileMarkId(pubMap));
						String lcxsFileMess = disclosureNoticeDocService.getOldDisclosureType(pubMap);//推送理财txt清单文件类型
						//pubFileInfoList.add(lcxsFileMess);//每一个推送的文件信息作为记录储存起来
						//versionIdList.add(String.valueOf(pubMap.get("notice_version_id")));//记录推送文件的公告版本号id，用于记录推送文件及批次
						try {
							disclosureNoticeDocService.saveNoticePubFileInfo(disclosureChannel.getId(), lcxsFileMess, String.valueOf(pubMap.get("notice_version_id")), pub_date, batch_no, String.valueOf(pubMap.get("file_name")));
						} catch (Exception e){
							ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(),
									pub_date+"日 信批公告推送 "+disclosureChannel.getChannelName()+"渠道 异常: 异常文件内容为"+lcxsFileMess);//插入错误日志
						}
						pubMap.put("pub_status", DisclosureStatus.overSend.getItemKey());
						disclosureNoticeDocService.updateNoticeStatus(pubMap);
					}catch (Exception e){
						//文件发送异常判定为发布失败,修改当前公告版本状态为发布失败
						pubMap.put("pub_status", DisclosureStatus.failure.getItemKey());
						//pubMap.put("notice_version_id", nowNoticeVersionId);
						//pubMap.put("channel_id", nowChannelId);
						disclosureNoticeDocService.updateNoticeStatus(pubMap);
					}
				}

				/**
				 * 目前仅仅理财销售渠道需要发送一个txt文档记录该渠道所有发布公告文件
				 */
				try{
					String txt_file_name = "fnc_announcemen_" + pub_date + NoticeFileTypeEnum.TXT.getName();//当天推送文件说明文档名称:理财销售+推送日期(每天只推送一个文件，重复推送则覆盖源文件)
					//确定当日是否存在已推送的文件记录，若存在则需要传入
					pubFileInfoList = disclosureNoticeDocService.handleTxtFileContext(disclosureChannel.getId(), pub_date);
					disclosureNoticeDocService.generatePubTxtFile(pubFileInfoList, String.valueOf(pubInfoList.get(0).get("local_path")), txt_file_name);
					if (pubInfoList.size() > 0 && "1".equals(channelInfo.getString("is_lcxs_channel"))) {//理财销售渠道才推送
						SftpUtils.putFile(disclosureChannel.getHostIp(), disclosureChannel.getUserName(),
								disclosureChannel.getPassword(), pubInfoList.get(0).get("file_path").toString().replace("{deal_date}", pub_date),
								String.valueOf(pubInfoList.get(0).get("local_path")), txt_file_name);
					}
				}catch (Exception e){
					ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "发送txt文档记录出错!" + e.getMessage());//插入错误日志
				}finally {
					pubInfoList.clear();//每个渠道发布后清空集合，避免重复上传
				}
			}

			/**发布渠道后，按照公告维度校验该公告下每条渠道是否都发布成功*/
			String[] noticeIdListAll = noticeIds.split(",");
			for(int i = 0; i< noticeIdListAll.length;i++){
				String notice_id = noticeIdListAll[i];
				String noticeLatestVersionId = disclosureNoticeDocService.getDisclosureMaxVersionId(notice_id);//获取公告版本号,发布取最新版本号
				int count =disclosureNoticeDao.findChannelsForNoticeId(notice_id,noticeLatestVersionId);
				if (!(count>0)){
					Map<String,Object> mapper = new HashMap<>();
					mapper.put("pub_date", pub_date);
					mapper.put("pub_time", DateUtil.formatDate(new Date(),DateFormatEnum.TIME_FORMAT));
					mapper.put("notice_id", notice_id);
					mapper.put("pub_status", DisclosureStatus.overSend.getItemKey());
					//所有渠道都发布成功后，才将该公告的公告状态变更为已发布
					disclosureNoticeDocDao.updateNoticeStatusById(mapper);
				}else{
					Map<String,Object> mapper = new HashMap<>();
					mapper.put("pub_date", pub_date);
					mapper.put("pub_time", DateUtil.formatDate(new Date(),DateFormatEnum.TIME_FORMAT));
					mapper.put("notice_id", notice_id);
					mapper.put("pub_status", DisclosureStatus.failure.getItemKey());
					mapper.put("notice_version_id", noticeLatestVersionId);
					//任意渠道发布失败公告的公告状态变更为发布失败
					disclosureNoticeDocDao.updateNoticeStatusById(mapper);
					//公告版本状态同样改为失败
					disclosureNoticeDocDao.updateNoticeVersionStatus(mapper);
				}
			}
		} catch (Exception e) {
			ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "批量发布失败!" + e.getMessage());//插入错误日志
			return RequestSupport.updateReturnJson(false, "批量发布失败!" + e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "批量发布成功!", null).toString();
	}
	/**
	* @功能描述:自动批量发布公告
	* @params:[params]
	* @return:java.lang.String
	* @Athor:ouyifan
	* @date:2022/8/29
	*/
	@API(desc = "自动批量发布公告", auth = APIAuth.NO, operation = APIOperation.INSTER)
	public String batchSendNotice(SqlParam<DisclosureNotice> params) throws Exception {
		try {
			String nowDate = DateUtil.getNowDate();
			Map<String,Object> paramsMap = new HashMap<>();
			List<SqlRow> noticeId = disclosureNoticeDao.findNoticeIdForPub(nowDate,params.getModel().getDisclosureType());//根据公告查询公告所需发布所有渠道
			String batch_no = disclosureNoticeDao.getNoticeFileSubmitBatchNo(nowDate);//获取当日信批文件提交批次信息
			if (noticeId.size()<=0)
				logger.info("当日["+nowDate+"]无公告发布");
			for (SqlRow sqlRow:noticeId) {
				paramsMap.put("id",sqlRow.getString("id"));
				SqlParam<DisclosureNotice> param = new FetcherData<>(paramsMap, DisclosureNotice.class);
				param.getModel().setAutoFlag("true");
				param.getModel().setBatchNo(batch_no);
				this.sendNotice(param);
			}
		} catch (Exception e) {
			return RequestSupport.updateReturnJson(false, e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}


	@API(desc = "公告发布", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String sendNotice(SqlParam<DisclosureNotice> params) throws Exception {

		//自动发布标识，用于区别返回自动发布报错信息
		String autoFlag = params.getModel().getAutoFlag();
		try {
			//公告详情信息
			String notice_id = params.getModel().getId();
			String notice_version_id = disclosureNoticeDocService.getDisclosureMaxVersionId(notice_id);//获取公告版本号,发布取最新版本号;//公告版本id
			String pub_date = DateUtil.getNowDate();
			List<Map<String,Object>> pubInfoList = new ArrayList<>();
			AtomicReference<String> channel_id = new AtomicReference<>("");
			/**获取发布信批文件批次号，若不存在则冲农信获取*/
			String batch_no = params.getModel().getBatchNo();
			if ("".equals(batch_no) || null == batch_no || "null".equals(batch_no)) {
				params.getModel().setBatchNo(disclosureNoticeDao.getNoticeFileSubmitBatchNo(pub_date));//获取当日信批文件提交批次信息
			}
			/**
			 * 查询需要发布的渠道信息,根据渠道划分
			 */
			List<SqlRow> channels = disclosureNoticeDao.findChannels(notice_id,notice_version_id);//根据公告查询公告所需发布所有渠道
//			new Thread(() -> {
				for (SqlRow channelInfo : channels) {
//					es.submit(() -> {
						try {
							DaoUtil.doTrans(() -> {
								String disclosureNoticeList = channelInfo.getString("disclosure_notice_id");//渠道需要发布的公告id集
								channel_id.set(channelInfo.getString("disclosure_notice_channel_id"));//渠道id
								String[] noticeIdList = disclosureNoticeList.split(",");
								/** 获取需要发布的渠道信息 */
								DisclosureChannel disclosureChannel = new DisclosureChannel();
								disclosureChannel.setHostIp(channelInfo.getString("host_ip"));
								disclosureChannel.setId(channelInfo.getString("disclosure_notice_channel_id"));
								disclosureChannel.setChannelName(channelInfo.getString("channel_name"));
								disclosureChannel.setPortCode(channelInfo.getString("port_code"));
								disclosureChannel.setUserName(channelInfo.getString("user_name"));
								disclosureChannel.setPassword(channelInfo.getString("password"));
								disclosureChannel.setFilePath(channelInfo.getString("file_path"));
								disclosureChannel.setIsDocking(channelInfo.getString("is_docking"));
								disclosureChannel.setStatus(channelInfo.getString("status"));

								/**
								 * 遍历公告，根据渠道对公告进行轮循处理
								 */
								for (int i = 0; i < noticeIdList.length; i++) {
									String disclosureNoticeId = noticeIdList[i];//信批公告id

									//通过公告版本id获得公告信息
									SqlRow disclosureNoticeInfo = disclosureNoticeChannelDao.findDisclosureNoticeById(notice_version_id);
									if (disclosureNoticeInfo.isEmpty()) {//若公告信息为空，则跳出
										ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "公告id为" + disclosureNoticeId + "的公告找不到公告信息!");//插入错误日志
										continue;
									}

									/** 在文件发布上传路径检索最新版本的公告文件是否存在，若不存在则调用生成方法并返回上传docx文件路径 */
//									String noticeLatestVersionId = disclosureNoticeDocService.getDisclosureMaxVersionId(disclosureNoticeId);//获取公告版本号,发布取最新版本号
									String doc_name = disclosureNoticeDocService.getNoticeDocName(disclosureNoticeId);//获取公告标题
									Map<String, Object> pubFile_params = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(disclosureNoticeId, notice_version_id, doc_name, false);

									/**
									 * 根据信批类型、子类型及发布的渠道以确定需要发布的文件类型，文件名(文件名为文件名_需要转换类型.docx文件)及发布路径等信息，最后根据该配置信息转换发布路径下docx文件为对应文件(文件名称，文件格式、文件类型)并复制命名文件
									 * pubInfoList存在多条数据时必定为同一公告发布同一渠道存在多个文件的情况
									 * */
									pubFile_params.put("channelId", channelInfo.getString("disclosure_notice_channel_id"));
									pubInfoList.addAll(disclosureNoticeDocService.getPubFileInfoByNoticeChannel(pubFile_params, disclosureChannel));
								}

								List<String> pubFileInfoList = new ArrayList<>();//同一渠道下某一公告发布文件各个类型文件名称记录集合，所以其发布的ftp地址及路径都相同
								/** 公告发布渠道(公告文件上传服务器) */
								for (Map<String, Object> pubMap : pubInfoList) {
									if (IsDocking.no.getItemKey().equals(String.valueOf(pubMap.get("is_docking"))) || XpStatus.stop.getItemKey().equals(pubMap.get("status"))) {//无需发布的渠道直接跳过
										continue;
									}

									try{
										SftpUtils.putFile(String.valueOf(pubMap.get("host_ip")), String.valueOf(pubMap.get("user_name")), String.valueOf(pubMap.get("password")),
												String.valueOf(pubMap.get("file_path")).replace("{deal_date}", pub_date), String.valueOf(pubMap.get("local_path")), String.valueOf(pubMap.get("file_name")));

										//变更公告状态等
										pubMap.put("pub_date", pub_date);
										pubMap.put("pub_time", DateUtil.formatDate(new Date(), DateFormatEnum.TIME_FORMAT));
										pubMap.put("id", disclosureNoticeDocService.getFncSendFileMarkId(pubMap));
										pubMap.put("pub_status", DisclosureStatus.overSend.getItemKey());
										String lcxsFileMess = disclosureNoticeDocService.getOldDisclosureType(pubMap);
										//pubFileInfoList.add(lcxsFileMess);
										try {
											disclosureNoticeDocService.saveNoticePubFileInfo(disclosureChannel.getId(), lcxsFileMess, String.valueOf(pubMap.get("notice_version_id")), pub_date, params.getModel().getBatchNo(), String.valueOf(pubMap.get("file_name")));
										} catch (Exception e) {
											ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(),
													pub_date+"日 信批公告推送 "+disclosureChannel.getChannelName()+"渠道 异常: 异常文件内容为"+lcxsFileMess);//插入错误日志
										}
									}catch (Exception e){
										logger.info("公告文件上传服务器失败");
										pubMap.put("pub_status", DisclosureStatus.failure.getItemKey());
									}
									disclosureNoticeDocService.updateNoticeStatus(pubMap);
								}

								/**
								 * 每个渠道发送一个txt文档记录该渠道所有发布公告文件
								 */
								try{
									String txt_file_name = "fnc_announcemen_" + pub_date + NoticeFileTypeEnum.TXT.getName();//当天推送文件说明文档名称:理财销售+推送日期(每天只推送一个文件，重复推送则覆盖源文件)
									//确定当日是否存在已推送的文件记录，若存在则需要传入
									pubFileInfoList = disclosureNoticeDocService.handleTxtFileContext(disclosureChannel.getId(), pub_date);
									disclosureNoticeDocService.generatePubTxtFile(pubFileInfoList, String.valueOf(pubInfoList.get(0).get("local_path")), txt_file_name);

									if (pubInfoList.size() > 0 && "1".equals(channelInfo.getString("is_lcxs_channel"))) {//理财销售渠道才推送
										SftpUtils.putFile(disclosureChannel.getHostIp(), disclosureChannel.getUserName(),
												disclosureChannel.getPassword(), pubInfoList.get(0).get("file_path").toString().replace("{deal_date}", pub_date),
												String.valueOf(pubInfoList.get(0).get("local_path")), txt_file_name);
									}
								}catch (Exception e){
									logger.info("渠道记录txt文件发送失败");
								}finally {
									pubInfoList.clear();//每个渠道发布后清空集合，避免重复上传
								}

							});
						} catch (Exception e) {
							Map<String,Object> mapper = new HashMap<>();
							mapper.put("pub_date", pub_date);
							mapper.put("pub_time", DateUtil.formatDate(new Date(),DateFormatEnum.TIME_FORMAT));
							mapper.put("notice_id", notice_id);
							mapper.put("pub_status", DisclosureStatus.failure.getItemKey());
							mapper.put("t8DisclosureNoticeId", notice_id);
							mapper.put("noticeVersionId", notice_version_id);
							mapper.put("notice_version_id", notice_version_id);
							mapper.put("channel_id", channel_id.get());
							mapper.put("disclosureStatusAfter", DisclosureStatus.failure.getItemKey());

							//只要有一条渠道发布失败，将该公告的公告状态变更为发布失败
							try {
								disclosureNoticeDocDao.updateNoticeStatusById(mapper);
								disclosureNoticeDao.updateDisclosureVersionStatus(mapper);//变更最新版本状态
								disclosureNoticeDocDao.updateNoticeChannelStatus(mapper);//公告渠道信息状态变更

							} catch (Exception exception) {
								exception.printStackTrace();
								if (autoFlag.equals("true")) {
									throw new Exception("公告发布失败!" + exception.getMessage());
								}
								return RequestSupport.updateReturnJson(false, "公告发布失败!详情请查看错误信息日志", null).toString();
							}
							ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "公告发布失败!" + e.getMessage());//插入错误日志
							if (autoFlag.equals("true")){
								throw new Exception("公告发布失败!" +e.getMessage());
							}
							return RequestSupport.updateReturnJson(false, "公告发布失败!详情请查看错误信息日志", null).toString();
						}finally {
							pubInfoList.clear();
						}
//					});
				}
//			}).start();

			/**发布渠道后，按照公告维度校验该公告下每条渠道是否都发布成功*/
//			String noticeLatestVersionId = disclosureNoticeDocService.getDisclosureMaxVersionId(notice_id);//获取公告版本号,发布取最新版本号
			int count =disclosureNoticeDao.findChannelsForNoticeId(notice_id,notice_version_id);
			if (!(count>0)) {
				Map<String, Object> mapper = new HashMap<>();
				mapper.put("pub_date", pub_date);
				mapper.put("pub_time", DateUtil.formatDate(new Date(), DateFormatEnum.TIME_FORMAT));
				mapper.put("notice_id", notice_id);
				mapper.put("pub_status", DisclosureStatus.overSend.getItemKey());
				mapper.put("t8DisclosureNoticeId", notice_id);
				mapper.put("noticeVersionId", notice_version_id);
				mapper.put("disclosureStatusAfter", DisclosureStatus.overSend.getItemKey());
				//所有渠道都发布成功后，才将该公告的公告状态变更为已发布
				disclosureNoticeDocDao.updateNoticeStatusById(mapper);
				disclosureNoticeDao.updateDisclosureVersionStatus(mapper);//变更最新版本状态
			}
		} catch (Exception e) {
			ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "公告发布失败!" + e.getMessage());//插入错误日志
			if ("true".equals(autoFlag)) {
				throw new Exception("公告发布失败!" + e.getMessage());
			}
			return RequestSupport.updateReturnJson(false, "公告发布失败!"+ e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "公告发布完成!", null).toString();
	}

	/**
	 * 单渠道发布公告(和上面方法类似，去掉了遍历环节)
	 *
	 * @param params
	 * @return
	 */
	public String sendNoticeByChannel(DisclosureNoticeChannel params) {
		StringBuffer message = new StringBuffer();
		try {
			//公告id
			String notice_id = params.getDisclosureNoticeId();
			String notice_version_id = disclosureNoticeDocService.getDisclosureMaxVersionId(notice_id);//获取公告版本号,发布取最新版本号;//公告版本id
			String pub_date = DateUtil.getNowDate();
			List<Map<String, Object>> pubInfoList = new ArrayList<>();
			AtomicReference<String> channel_id = new AtomicReference<>("");
			try {
				DaoUtil.doTrans(() -> {
					/**
					 * 组装渠道信息
					 */
					DisclosureChannel disclosureChannel = new DisclosureChannel();
					disclosureChannel.setHostIp(params.getHostIp());
					disclosureChannel.setId(params.getDisclosureNoticeChannelId());
					disclosureChannel.setPortCode(params.getPortCode());
					disclosureChannel.setUserName(params.getUserName());
					disclosureChannel.setPassword(params.getPassword());
					disclosureChannel.setFilePath(params.getFilePath());
					disclosureChannel.setIsDocking(params.getIsDocking());
					disclosureChannel.setStatus(params.getStatus());
					//通过公告版本id获得公告信息
					SqlRow disclosureNoticeInfo = disclosureNoticeChannelDao.findDisclosureNoticeById(notice_version_id);
					if (disclosureNoticeInfo.isEmpty()) {//若公告信息为空，则跳出
						ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "公告id为" + notice_id + "的公告找不到公告信息!");//插入错误日志
					}
					/** 在文件发布上传路径检索最新版本的公告文件是否存在，若不存在则调用生成方法并返回上传docx文件路径 */
					String doc_name = disclosureNoticeDocService.getNoticeDocName(notice_id);//获取公告标题
					Map<String, Object> pubFile_params = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(notice_id, notice_version_id, doc_name, false);
					/**
					 * 根据信批类型、子类型及发布的渠道以确定需要发布的文件类型，文件名(文件名为文件名_需要转换类型.docx文件)及发布路径等信息，最后根据该配置信息转换发布路径下docx文件为对应文件(文件名称，文件格式、文件类型)并复制命名文件
					 * pubInfoList存在多条数据时必定为同一公告发布同一渠道存在多个文件的情况
					 * */
					pubFile_params.put("channelId", disclosureChannel.getId());
					pubInfoList.addAll(disclosureNoticeDocService.getPubFileInfoByNoticeChannel(pubFile_params, disclosureChannel));
					List<String> pubFileInfoList = new ArrayList<>();//同一渠道下某一公告发布文件各个类型文件名称记录集合，所以其发布的ftp地址及路径都相同
					int id = 0;
					/** 公告发布渠道(公告文件上传服务器) */
					for (Map<String, Object> pubMap : pubInfoList) {
						if (IsDocking.no.getItemKey().equals(String.valueOf(pubMap.get("is_docking"))) || XpStatus.stop.getItemKey().equals(pubMap.get("status"))) {//无需发布的渠道直接跳过
							continue;
						}
						try {
							SftpUtils.putFile(String.valueOf(pubMap.get("host_ip")), String.valueOf(pubMap.get("user_name")), String.valueOf(pubMap.get("password")),
									String.valueOf(pubMap.get("file_path")).replace("{deal_date}", pub_date), String.valueOf(pubMap.get("local_path")), String.valueOf(pubMap.get("file_name")));

							//变更公告状态等
							pubMap.put("pub_date", pub_date);
							pubMap.put("pub_time", DateUtil.formatDate(new Date(), DateFormatEnum.TIME_FORMAT));
							id++;
							pubMap.put("id", id);
							pubMap.put("pub_status", DisclosureStatus.overSend.getItemKey());
							String lcxsFileMess = disclosureNoticeDocService.getOldDisclosureType(pubMap);
							pubFileInfoList.add(lcxsFileMess);
							disclosureNoticeDocService.updateNoticeStatus(pubMap);
						} catch (Exception e) {
							logger.error("公告文件上传服务器失败");
							logger.error(e.getMessage(),e);
							pubMap.put("pub_status", DisclosureStatus.failure.getItemKey());
							pubMap.put("remark", "公告文件上传服务器失败："+e.getMessage());
							message.append("公告文件上传服务器失败："+e.getMessage()+"\r\n");
							disclosureNoticeDocService.updateNoticeStatus(pubMap);
						}

					}

					/**
					 * 每个渠道发送一个txt文档记录该渠道所有发布公告文件
					 */
					try {
						String txt_file_name = "fnc_announcemen_" + pub_date + NoticeFileTypeEnum.TXT.getName();//当天推送文件说明文档名称:理财销售+推送日期(每天只推送一个文件，重复推送则覆盖源文件)
						//确定当日是否存在已推送的文件记录，若存在则需要传入
						pubFileInfoList = disclosureNoticeDocService.handleTxtFileContext(disclosureChannel.getId(), pub_date);
						disclosureNoticeDocService.generatePubTxtFile(pubFileInfoList, String.valueOf(pubInfoList.size() > 0 ? pubInfoList.get(0).get("local_path") : ""), txt_file_name);

						if (pubInfoList.size() > 0&&"41".equals(params.getDisclosureNoticeChannelId())) {//理财销售渠道才推送
							SftpUtils.putFile(disclosureChannel.getHostIp(), disclosureChannel.getUserName(),
									disclosureChannel.getPassword(), pubInfoList.get(0).get("file_path").toString().replace("{deal_date}", pub_date),
									String.valueOf(pubInfoList.get(0).get("local_path")), txt_file_name);
						}
					} catch (Exception e) {
						logger.error("渠道记录txt文件发送失败");
						message.append("渠道记录txt文件发送失败："+e.getMessage());
						logger.error(e.getMessage(),e);

					}finally {
						pubInfoList.clear();//每个渠道发布后清空集合，避免重复上传
					}

				});
			} catch (Exception e) {
				Map<String, Object> mapper = new HashMap<>();
				mapper.put("pub_date", pub_date);
				mapper.put("pub_time", DateUtil.formatDate(new Date(), DateFormatEnum.TIME_FORMAT));
				mapper.put("notice_id", notice_id);
				mapper.put("pub_status", DisclosureStatus.failure.getItemKey());
				mapper.put("t8DisclosureNoticeId", notice_id);
				mapper.put("noticeVersionId", notice_version_id);
				mapper.put("notice_version_id", notice_version_id);
				mapper.put("channel_id", channel_id.get());
				mapper.put("disclosureStatusAfter", DisclosureStatus.failure.getItemKey());

				//只要有一条渠道发布失败，将该公告的公告状态变更为发布失败
				try {
					disclosureNoticeDocDao.updateNoticeStatusById(mapper);
					disclosureNoticeDao.updateDisclosureVersionStatus(mapper);//变更最新版本状态
					disclosureNoticeDocDao.updateNoticeChannelStatus(mapper);//公告渠道信息状态变更
				} catch (Exception exception) {
					exception.printStackTrace();
					return RequestSupport.updateReturnJson(false, "公告发布失败!"+ e.getMessage(), null).toString();
				}
				ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "公告发布失败!" + e.getMessage());//插入错误日志
				return RequestSupport.updateReturnJson(false, "公告发布失败!"+ e.getMessage(), null).toString();
			} finally {
				pubInfoList.clear();
			}
			/**发布渠道后，按照公告维度校验该公告下每条渠道是否都发布成功*/
			int count = disclosureNoticeDao.findChannelsForNoticeId(notice_id, notice_version_id);
			if (!(count > 0)) {
				Map<String, Object> mapper = new HashMap<>();
				mapper.put("pub_date", pub_date);
				mapper.put("pub_time", DateUtil.formatDate(new Date(), DateFormatEnum.TIME_FORMAT));
				mapper.put("notice_id", notice_id);
				mapper.put("pub_status", DisclosureStatus.overSend.getItemKey());
				mapper.put("t8DisclosureNoticeId", notice_id);
				mapper.put("noticeVersionId", notice_version_id);
				mapper.put("disclosureStatusAfter", DisclosureStatus.overSend.getItemKey());
				//所有渠道都发布成功后，才将该公告的公告状态变更为已发布
				disclosureNoticeDocDao.updateNoticeStatusById(mapper);
				disclosureNoticeDao.updateDisclosureVersionStatus(mapper);//变更最新版本状态
			}
		} catch (Exception e) {

			ErrorMessageHandlerUtil.ErrorInfoRecordHandle(ErrorTypeEnum.DISCLOSURE_NOTICE_PUB_ERR.getValue(), "公告发布失败!" + e.getMessage());//插入错误日志
			return RequestSupport.updateReturnJson(false, "公告发布失败!"+ e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(message.toString().equals(""), message.toString().equals("")?"公告发布完成!":message.toString(), null).toString();
	}


	// 更新信当前阶段为：托管行审核，当前阶段状态：托管复核中, 信披版本已发送托管行状态 为“是”
	public void updateNoticeStatus(List<DisclosureNotice> noticeList, boolean flag) throws Exception {

		DaoUtil.doTrans(() -> {
			for (DisclosureNotice notice : noticeList) {
				// 发送成功则所有公告状态为已披露

				// 发送邮件没有失败，且信批版本文件已存在，则更新状态
				if (flag && Strings.isNotBlank(notice.getCrtPath())) {
					//notice.setStage("3");
					//notice.setCurrentStageStatus("10");
					notice.setIsSendEmail("1");
					disclosureNoticeDao.updateNoticeStageStatus(notice);
					disclosureNoticeVersionDao.updateEmailStatus(notice);
				}

				// 设置首页待办
	//			disclosureTruteeApprovalDao.updateDisclosureNoticeStatusTrustee(notice.getId(),notice.getProdCode());
//				// 新逻辑,更新公告状态,同时更新公告子状态为托管复核中
//				disclosureNoticeDao.updateStageStatus(notice.getId(), "3");
				// 插入t8_disclosure_trutee_approval表数据


				//notice.setApprovalStatus("1");
				//disclosureTruteeApprovalDao.savaTruteeApproval(notice);
				DisclosureNotice disclosureNotice = new DisclosureNotice();
				//更新信披版本表状态为已发送托管行
				disclosureNotice.setId(notice.getId());
				disclosureNotice.setIsSendEmail("1");
				disclosureNoticeVersionDao.updateEmailStatus(disclosureNotice);
			}
		});

	}

	@API(desc = "公告取消", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String cancelNotice(SqlParam<Object> object) throws Exception {
		Map param = RequestSupport.getParameters();
		//除了公告已发布，都可以取消
		try {
			Map<String, Object> upd_params = new HashMap<>();
			List<DisclosureNotice> noticeList = null;
			noticeList = disclosureNoticeDao.findCountsByCode(param.get("id").toString());

			if (CollectionUtil.isEmpty(noticeList)) {
				upd_params.put("disclosureStatusAfter", DisclosureStatus.close.getItemKey());
				upd_params.put("changeReason", "公告详情取消发布操作");
				upd_params.put("disclosureStatusAhead", param.get("disclosureStatus"));
				upd_params.put("t8DisclosureNoticeId", param.get("id"));
				upd_params.put("noticeVersionId", param.get("noticeVersionId"));
				upd_params.put("crtDate", PublicUtils.getSysWordDay());
				upd_params.put("crtTime", DateUtil.formatDate(new Date(),DateFormatEnum.TIME_FORMAT));
				upd_params.put("crtUserId", SysUtil.getSysUserParamValue("sys_user_userid"));
				upd_params.put("crtUserName", SysUtil.getSysUserParamValue("sys_user_username"));
				disclosureNoticeDao.updateDisclosureNoticeStatus(upd_params);//变更公告状态
				disclosureNoticeDao.updateDisclosureVersionStatus(upd_params);//变更公告最新版本状态
				disclosureNoticeDao.updateDisclosureChannelStatus(upd_params);//变更公告版本发布的所有渠道状态
				disclosureNoticeDao.insertDisclosureNoticeRecord(upd_params);//插入记录
			} else {
				return RequestSupport.updateReturnJson(false, "取消发布失败,产品" + param.get("prodCode") + "已发布,请勿取消", null).toString();
			}
		} catch (Exception e) {
			log.error("数据库操作异常{}", e);
		}
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "查询当前用户是否有补录分发待办", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public String findOperatorForDivided(SqlParam<DisclosureNotice> params) throws Exception {
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
		params.getModel().setCrtUserId(userid);
		// 查询当前用户是否有补录分发待办
		List<SqlRow> noticeList = disclosureNoticeDao.findOperatorForDivided(params);
		Map<String, Object> map = new HashMap<>();
		if (CollectionUtil.isEmpty(noticeList)) {
			map.put("result", "failed");
			return RequestSupport.updateReturnJson(true, "无代办不能点击", map).toString();
		} else {
			map.put("result", "success");
			return RequestSupport.updateReturnJson(true, "有代办可以点击", map).toString();
		}
	}

	@API(desc = "补录分发", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public void updateDisNoticeProcess(SqlParam<DisclosureNotice> params) {
		// 此方法只作用于权限控制
	}

	public String insertNotice(SqlRow sqlRow) throws Exception {
		return disclosureNoticeDao.insertNotice(sqlRow);
	}

	@API(desc = "公告预览", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<Map<String, Object>> previewXP(SqlParam<DisclosureNotice> params) throws Exception {
		SqlResult<Map<String, Object>> sqlResult = new SqlResult<>();
		Map<String, Object> param = RequestSupport.getParameters();
		String noticeId = (String) param.get("id");
		onlineUrl = (String) param.get("onlineUrl");
		if (StringUtils.isEmpty(noticeId))
			return null;

		SqlRow sqlRow = disclosureNoticeDao.queryModByNoticeId(noticeId);
		if (StringUtils.isEmpty(sqlRow.getString("id")))
			return null;
		DisclosureNotice disclosureNotice = disclosureNoticeDao.queryNoticeById(noticeId);
		// 判断产品是否为份额分类产品
		String isShareSort = disclosureNoticeDao.isShareSort(disclosureNotice.getProdCode());
		//disclosureNotice.setIsShareSort(isShareSort);

		String viewUrl = onlineUrl + xpFindPath(disclosureNotice);
		log.info("pdf预览路径{}", viewUrl);
		param.put("t8DisclosureNoticeId", noticeId);
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("viewUrl", viewUrl);
		data.add(map);
		sqlResult.setRows(data);
		return sqlResult;
	}

	// 信批预览获取转换后的html
	public String xpFindPath(DisclosureNotice disclosureNotice) {
		OutputStream ostream = null;
		FileInputStream fileInputStream = null;
		String processInstanceId = "";
		try {
			DisclosureRule dis = disclosureRuleDao.findGGDisclosureRules(disclosureNotice.getId());
			DisclosureModVersion disclosureModVersion = disclosureModVersionService
					.getPrintXPVersionById((String) dis.getId());
			Map<String, Object> map = new HashMap<>();
			map.put("prodCode", disclosureNotice.getProdCode());
			map.put("prodBaseDate", disclosureNotice.getProdBaseDate());
			map.put("disclosureSonType", dis.getDisclosureSonType());
			map.put("t8DisclosureNoticeId", disclosureNotice.getId());
			// map.put("version", "");
			map.put("isComplete", "0");// 修改
			SqlRow infoMap = findDataInfoByType(map);
			if (infoMap != null && infoMap.size() > 0) {
				fileStorePath = OnlineUtils.getOnlinepath(basePath);
				String newFilePath = fileStorePath + separate + "xpTemp" + separate + disclosureNotice.getId()
						+ separate + "html";// 修改
				String filePath = fileStorePath + separate + "xpTemp" + separate + disclosureModVersion.getId();
				log.info("初始化获取模板url-------》{}", filePath);
				File word = null;
				word = new File(filePath, disclosureModVersion.getDocName());// 修改
				log.info("文档路径：----------{}", word);

				fileInputStream = new FileInputStream(word);

				// 载入模板文件
				WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(fileInputStream);
				// 提取正文
				MainDocumentPart mainDocumentPart = wPackage.getMainDocumentPart();
				org.docx4j.wml.Document wmlDoc = (org.docx4j.wml.Document) mainDocumentPart.getJaxbElement();
				org.docx4j.wml.Body body = wmlDoc.getBody();
				// 提取正文中所有段落
				List<Object> paragraphs = body.getContent();
				// 提取书签并创建书签的游标
				RangeFinder rt = new RangeFinder("CTBookmark", "CTMarkupRange");
				new TraversalUtil(paragraphs, rt);
				// 判断是否份额分类
				if ("1".equals(disclosureNotice.getIsSendEmail())) {/**此处代码变更*/
					// 判断书签
					for (CTBookmark bm : rt.getStarts()) {
						if (bm.getName().equals("comparisonChart")) {
							// 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
							// 根据noticeID查询t8_disclosure_share_image
							List<SqlRow> shareImages = disclosureNoticeDao.queryShareImage(disclosureNotice.getId(),
									"0");
							if (CollectionUtils.isEmpty(shareImages))
								continue;

							shareImages.forEach((image) -> {
								InputStream is = null;
								try {

									String path = image.getString("share_image_path");
									String shareName = image.getString("share_name");
									log.info("imagePath---------->{}", path);
									if (Strings.isEmpty(path) || !(new File(path).exists()))
										return;
									is = new FileInputStream(path);
									byte[] bytes = IOUtils.toByteArray(is);
									// 穿件一个行内图片
									BinaryPartAbstractImage imagePart = BinaryPartAbstractImage
											.createImagePart(wPackage, bytes);

									Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 6000);
									P p = (P) (bm.getParent());
									ObjectFactory factory = new ObjectFactory();

									R run = factory.createR();
									run = factory.createR();
									Text text = factory.createText();
									text.setValue(shareName);
									run.getContent().add(text);
									Br br = factory.createBr();
									run.getContent().add(br);
									p.getContent().add(run);

									run = factory.createR();
									Drawing drawing = factory.createDrawing();
									drawing = factory.createDrawing();
									drawing.getAnchorOrInline().add(inline);
									run.getContent().add(drawing);
									br = factory.createBr();
									run.getContent().add(br);
									p.getContent().add(run);

								} catch (Exception e) {
									log.error("ioException{}", e);
								} finally {
									if (is != null) {
										try {
											is.close();
										} catch (IOException e) {
											log.error("ioException{}", e);
										}
									}
								}
							});

						} else if (bm.getName().equals("comparisonChart2")) {
							// 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片

							// 根据noticeID查询t8_disclosure_share_image
							List<SqlRow> shareImages = disclosureNoticeDao.queryShareImage(disclosureNotice.getId(),
									"1");
							if (CollectionUtils.isEmpty(shareImages))
								continue;

							shareImages.forEach((image) -> {
								InputStream is = null;
								try {

									String path = image.getString("share_image_path");
									String shareName = image.getString("share_name");
									log.info("imagePath---------->{}", path);
									if (Strings.isEmpty(path) || !(new File(path).exists()))
										return;
									is = new FileInputStream(path);
									byte[] bytes = IOUtils.toByteArray(is);
									// 穿件一个行内图片
									BinaryPartAbstractImage imagePart = BinaryPartAbstractImage
											.createImagePart(wPackage, bytes);

									Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 6000);
									P p = (P) (bm.getParent());
									ObjectFactory factory = new ObjectFactory();

									R run = factory.createR();
									run = factory.createR();
									Text text = factory.createText();
									text.setValue(shareName);
									run.getContent().add(text);
									Br br = factory.createBr();
									run.getContent().add(br);
									p.getContent().add(run);

									run = factory.createR();
									Drawing drawing = factory.createDrawing();
									drawing = factory.createDrawing();
									drawing.getAnchorOrInline().add(inline);
									run.getContent().add(drawing);
									br = factory.createBr();
									run.getContent().add(br);
									p.getContent().add(run);

								} catch (Exception e) {
									log.error("ioException{}", e);
								} finally {
									if (is != null) {
										try {
											is.close();
										} catch (IOException e) {
											log.error("ioException{}", e);
										}
									}
								}
							});

						}
					}
				} else {
					InputStream is = null;
					// 遍历书签
					for (CTBookmark bm : rt.getStarts()) {
						// 这儿可以对单个书签进行操作，也可以用一个map对所有的书签进行处理
						if (bm.getName().equals("comparisonChart")) {
							// 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
							String os = System.getProperty("os.name");
							String path = "";
							Iterator<Map.Entry<String, Object>> it = infoMap.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry<String, Object> entry = it.next();
								// System.out.println("key:" + entry.getKey() + " key:" + entry.getValue());
								if ("image_path".equals(entry.getKey()) && entry.getValue() != null
										&& Strings.isNotBlank(String.valueOf(entry.getValue()))) {
									path = entry.getValue().toString();
								}

							}
							if (!os.toLowerCase().startsWith("win")) {

								String prodCode = disclosureNotice.getProdCode();
								String baseDate = disclosureNotice.getProdBaseDate();
								//去除自动生成图片
								//String realPath = echartsAction.navMarketCreateImg(prodCode, baseDate);
								if (Tools.isNotEmpty(path) && new File(path).exists()) {
									is = new FileInputStream(path);
								}
								/*else {
									if (realPath != null && new File(realPath).exists()) {
										is = new FileInputStream(realPath);
									}

								}*/
							} else {
								String prodCode = disclosureNotice.getProdCode();
								String baseDate = disclosureNotice.getProdBaseDate();
								//去除自动生成图片
								//String realPath = echartsAction.navMarketCreateImg(prodCode, baseDate);
								if (Tools.isNotEmpty(path) && new File(path).exists()) {
									is = new FileInputStream(path);
								}
								/*else {
									if (realPath != null && new File(realPath).exists()) {
										is = new FileInputStream(realPath);
									}

								}*/
							}
							if (is != null) {
								byte[] bytes = IOUtils.toByteArray(is);
								// 穿件一个行内图片
								BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage,
										bytes);
								// createImageInline函数的前四个参数我都没有找到具体啥意思，，，，
								// 最有一个是限制图片的宽度，缩放的依据
								Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 6000);
								// 获取该书签的父级段落
								P p = (P) (bm.getParent());
								ObjectFactory factory = new ObjectFactory();
								// R对象是匿名的复杂类型，然而我并不知道具体啥意思，估计这个要好好去看看ooxml才知道
								R run = factory.createR();
								// drawing理解为画布？
								Drawing drawing = factory.createDrawing();
								drawing.getAnchorOrInline().add(inline);
								run.getContent().add(drawing);
								p.getContent().add(run);
								// is.close();
							}

						} else if (bm.getName().equals("comparisonChart2")) {
							// 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
							String os = System.getProperty("os.name");
							String path = "";
							Iterator<Map.Entry<String, Object>> it = infoMap.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry<String, Object> entry = it.next();
								// System.out.println("key:"+entry.getKey()+" key:"+entry.getValue());
								if ("image_path2".equals(entry.getKey()) && entry.getValue() != null) {
									path = entry.getValue().toString();
								} else {

								}

							}
							if (os.toLowerCase().startsWith("win")) {
								String prodCode = disclosureNotice.getProdCode();
								String baseDate = disclosureNotice.getProdBaseDate();
								String realPath = "";
								String startDate = disclosureNotice.getReportStartDate();
								// is = new FileInputStream("D:\\ChromDownloads\\净值表现与市场对比.png");
								if (Tools.isNotEmpty(path) && new File(path).exists()) {
									is = new FileInputStream(path);
								}
								//去除自动生成图片
								/*else {
									realPath = echartsAction.navMarketAreaCreateImg(prodCode, startDate, baseDate);
									if (Tools.isNotEmpty(realPath) && new File(realPath).exists()) {
										is = new FileInputStream(realPath);
									}
								}*/

							} else {

								String prodCode = disclosureNotice.getProdCode();
								String baseDate = disclosureNotice.getProdBaseDate();
								String startDate = disclosureNotice.getReportStartDate();
								String realPath = "";
								// is = new
								// FileInputStream("/home/ftpuser/test/echarts/EB1844/20210529/202105291622270542574.jpg");
								if (Tools.isNotEmpty(path) && new File(path).exists()) {
									is = new FileInputStream(path);
								}
								/*else {
									realPath = echartsAction.navMarketAreaCreateImg(prodCode, startDate, baseDate);
									if (Tools.isNotEmpty(realPath) && new File(realPath).exists()) {
										is = new FileInputStream(realPath);
									}
								}*/
							}
							if (is != null) {
								byte[] bytes = IOUtils.toByteArray(is);
								// 穿件一个行内图片
								BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage,
										bytes);
								// createImageInline函数的前四个参数我都没有找到具体啥意思，，，，
								// 最有一个是限制图片的宽度，缩放的依据
								Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 6000);
								// 获取该书签的父级段落
								P p = (P) (bm.getParent());
								ObjectFactory factory = new ObjectFactory();
								// R对象是匿名的复杂类型，然而我并不知道具体啥意思，估计这个要好好去看看ooxml才知道
								R run = factory.createR();
								// drawing理解为画布？
								Drawing drawing = factory.createDrawing();
								drawing.getAnchorOrInline().add(inline);
								run.getContent().add(drawing);
								// setParagraphStyle(p);
								p.getContent().add(run);
								// is.close();
							}
						}
					}
					if (is != null) {
						is.close();

					}
				}

				File localPathFile = new File(newFilePath + separate + "temp");

				if (!localPathFile.exists() && !localPathFile.isDirectory()) {
					localPathFile.mkdirs();
				}
				log.info("添加imge后的文件路径---》{}",
						newFilePath + separate + "temp" + separate + disclosureModVersion.getDocName());
				// 添加imge后的文件路径
				wPackage.save(new FileOutputStream(
						newFilePath + separate + "temp" + separate + disclosureModVersion.getDocName()));

				wordToPdfUtil.getLicense();
				com.aspose.words.Document document = new com.aspose.words.Document(
						newFilePath + separate + "temp" + separate + disclosureModVersion.getDocName());
				// 获得要替换的word模板
				Range range = document.getRange();// range获取word中的内容
				Iterator<Map.Entry<String, Object>> it = infoMap.entrySet().iterator();
				// 年报或者半年报 不显示其他关联交易
				if ("2".equals(dis.getDisclosureSonType()) || "3".equals(dis.getDisclosureSonType())) {
					infoMap.put("other_transaction1", "");
					infoMap.put("other_transaction2", "");
					infoMap.put("other_transaction3", "");
					infoMap.put("other_relation_transaction", "");
				}

				// 是否份额分类产品
				//log.info("是否份额分类产品: {}", disclosureNotice.getIsShareSort());
				if ("1".equals(disclosureNotice.getIsSendEmail())) {/**此处代码变更*/
					document = this.isShareSort(document, disclosureNotice, disclosureModVersion.getDocName(), infoMap);
				}
				while (it.hasNext()) {
					Map.Entry<String, Object> entry = it.next();

					String str = "";
					if ("brief".equals(entry.getKey()) && entry.getValue() != null
							&& "".equals(entry.getValue().toString().replaceAll("\\s*", ""))) {
						entry.setValue("");
					}
					if (entry.getValue() == null)
						entry.setValue("");
					String value = entry.getValue().toString();
					if ("image_path".equals(entry.getKey()) && Tools.isNotEmpty(entry.getValue().toString())) {
						value = "";
					}
					if ("image_path2".equals(entry.getKey()) && Tools.isNotEmpty(entry.getValue().toString())) {
						value = "";
					}
					if ("assets_desc".equals(entry.getKey())) {
						Map<String,Object> paramMap = new HashMap<>();
						paramMap.put("t8DisclosureNoticeId",disclosureNotice.getId());
						SqlParam<DisclosureRegularAsset> params=new FetcherData<>(paramMap,DisclosureRegularAsset.class);
					}
//					if ("assets_desc".equals(entry.getKey())) {
//						String result = disclosureNoticeService.findDetail(disclosureNotice.getId());
//						if (!"success".equals(result)) {
//							value = "";
//						}
//					}
					if ("establish_date".equals(entry.getKey())) {
						String establishDate = entry.getValue().toString();
						if (entry.getValue() != null && entry.getValue() != "") {
							String netvalDate = entry.getValue().toString();
							if (netvalDate.length() > 5) {
								String year = establishDate.substring(0, 4);
								String month = establishDate.substring(4, 6);
								String day = establishDate.substring(6, 8);
								if (!"1".equals(month.substring(0, 1))) {
									month = month.substring(1, 2);
								}
								if ("0".equals(day.substring(0, 1))) {

									day = day.substring(1, 2);
								}
								value = year + "年" + month + "月" + day + "日";
							}
						}
					}
					if ("end_date".equals(entry.getKey())) {
						String endDate = entry.getValue().toString();
						if (entry.getValue() != null && entry.getValue() != "") {
							String netvalDate = entry.getValue().toString();
							if (netvalDate.length() > 5) {
								String year = endDate.substring(0, 4);
								String month = endDate.substring(4, 6);
								String day = endDate.substring(6, 8);
								if (!"1".equals(month.substring(0, 1))) {
									month = month.substring(1, 2);
								}
								if ("0".equals(day.substring(0, 1))) {

									day = day.substring(1, 2);
								}
								value = year + "年" + month + "月" + day + "日";
							}
						}
					}
					if ("report_date".equals(entry.getKey())) {
						String reportDate = entry.getValue().toString();
						if (entry.getValue() != null && entry.getValue() != "") {
							String netvalDate = entry.getValue().toString();
							if (netvalDate.length() > 5) {
								String year = reportDate.substring(0, 4);
								String month = reportDate.substring(4, 6);
								String day = reportDate.substring(6, 8);
								if (!"1".equals(month.substring(0, 1))) {
									month = month.substring(1, 2);
								}
								if ("0".equals(day.substring(0, 1))) {

									day = day.substring(1, 2);
								}
								value = year + "年" + month + "月" + day + "日";
							}
						}
					}
					if ("netval_date".equals(entry.getKey())) {
						if (entry.getValue() != null && !("".equals(entry.getValue().toString().trim()))) {
							String netvalDate = entry.getValue().toString();
							if (netvalDate.length() > 5) {
								String year = netvalDate.substring(0, 4);
								String month = netvalDate.substring(4, 6);
								String day = netvalDate.substring(6, 8);
								if (!"1".equals(month.substring(0, 1))) {
									month = month.substring(1, 2);
								}
								if ("0".equals(day.substring(0, 1))) {

									day = day.substring(1, 2);
								}
								value = year + "年" + month + "月" + day + "日";
							}
						}
					}
					if (entry.getValue() != null) {
						str = value;
						if ("brief,financier,term".contains(entry.getKey())) {
							str = str.replaceAll("\n\r", ControlChar.LINE_BREAK);
							str = str.replaceAll("\r\n", ControlChar.LINE_BREAK);
							str = str.replaceAll("\n", ControlChar.LINE_BREAK);
						} else {
							if (str.contains("\n\r")) {
								str = formatStr(str, "\n\r");
							}
							if (str.contains("\r\n")) {
								str = formatStr(str, "\r\n");
							}
							if (str.contains("\n")) {
								str = formatStr(str, "\n");
							}
						}

					}
					logger.info("key------------------>{},value------------------->{}", entry.getKey(), str);
					range.replace("${" + entry.getKey() + "}", str, true, false);
				}

				// 定期报告资产配置明细
				Table table = (Table) document.getChild(NodeType.TABLE, 3, true);
				if (table == null) {
					Table table4 = (Table) document.getChild(NodeType.TABLE, 1, true);
					if (table4 != null) {

						List<SqlRow> list = disclosureNoticeDao.findDetailListForNotice1(disclosureNotice.getProdCode(),
								disclosureNotice.getId(), disclosureNotice.getProdBaseDate());
						Integer i = 0;
						if (list != null && list.size() > 0) {
							for (SqlRow map2 : list) {
								i = i + 1;
								Node deepClone = table4.getLastRow().deepClone(true);
								Range range2 = table4.getLastRow().getRange();
								if (map2.get("list_id") != null && StringUtils.isNotBlank(map2.get("list_id").toString())) {
									if ("合计".equals(map2.get("list_id").toString())) {
										range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
									} else {
										if (Float.parseFloat(map2.get("list_id").toString()) > 6) {
											range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
										} else {
											if (map2.get("list_id").toString().contains(".")) {
												range2.replace("${list_id}",
														map2.get("list_id").toString().substring(0,
																map2.get("list_id").toString().indexOf(".")),
														true, false);
											} else {
												range2.replace("${list_id}", map2.get("list_id").toString(), true,
														false);
											}
										}
									}
								} else {
									range2.replace("${list_id}", "", true, false);
								}
								if (map2.get("list_assets_type") != "" && map2.get("list_assets_type") != null) {
									// range2.replace("${list_assets_type}",
									// map2.get("list_assets_type").toString(), true, false);
									if ("总资产".equals(map2.get("list_assets_type").toString())) {
										range2.replace("${list_assets_type}", "", true, false);
									} else {
										range2.replace("${list_assets_type}", map2.get("list_assets_type").toString(),
												true, false);
									}
								} else {
									range2.replace("${list_assets_type}", "", true, false);
								}
								if (map2.get("list_amount") != "" && map2.get("list_amount") != null) {
									range2.replace("${list_amount}", map2.get("list_amount").toString(), true, false);
								} else {
									range2.replace("${list_amount}", "", true, false);
								}
								if (map2.get("list_asset_ratio") != "" && map2.get("list_asset_ratio") != null) {
									range2.replace("${list_asset_ratio}", map2.get("list_asset_ratio").toString(), true,
											false);
								} else {
									range2.replace("${list_asset_ratio}", "", true, false);
								}

								table4.getRows().add(deepClone);
							}
							table4.getLastRow().remove();
						} else {
							Node deepClone = table4.getLastRow().deepClone(true);
							Range range2 = table4.getLastRow().getRange();
							range2.replace("${list_id}", "", true, false);
							range2.replace("${list_assets_type}", "", true, false);
							range2.replace("${list_amount}", "", true, false);
							range2.replace("${list_asset_ratio}", "", true, false);
							table4.getRows().add(deepClone);
							table4.getLastRow().remove();
						}
					}
				} else {
					int counts = table.getFirstRow().getCount();
					if (counts > 4) {
						table = (Table) document.getChild(NodeType.TABLE, 2, true);
					}
					List<SqlRow> list = disclosureNoticeDao.findDetailListForNotice1(disclosureNotice.getProdCode(),
							disclosureNotice.getId(), disclosureNotice.getProdBaseDate());
					Integer i = 0;
					if (list != null && list.size() > 0) {
						for (SqlRow map2 : list) {
							i = i + 1;
							Node deepClone = table.getLastRow().deepClone(true);
							Range range2 = table.getLastRow().getRange();
							if (map2.get("list_id") != null &&  StringUtils.isNotBlank(map2.get("list_id").toString())) {
								if ("合计".equals(map2.get("list_id").toString())) {
									range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
								} else {
									if (Float.parseFloat(map2.get("list_id").toString()) > 6) {
										range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
									} else {
										if (map2.get("list_id").toString().contains(".")) {
											range2.replace("${list_id}", map2.get("list_id").toString().substring(0,
													map2.get("list_id").toString().indexOf(".")), true, false);
										} else {
											range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
										}
									}
								}
							} else {
								range2.replace("${list_id}", "", true, false);
							}
							if (map2.get("list_assets_type") != "" && map2.get("list_assets_type") != null) {
								// range2.replace("${list_assets_type}",
								// map2.get("list_assets_type").toString(), true, false);
								if ("总资产".equals(map2.get("list_assets_type").toString())) {
									range2.replace("${list_assets_type}", "", true, false);
								} else {
									range2.replace("${list_assets_type}", map2.get("list_assets_type").toString(), true,
											false);
								}
							} else {
								range2.replace("${list_assets_type}", "", true, false);
							}
							if (map2.get("list_amount") != "" && map2.get("list_amount") != null) {
								range2.replace("${list_amount}", map2.get("list_amount").toString(), true, false);
							} else {
								range2.replace("${list_amount}", "", true, false);
							}
							if (map2.get("list_asset_ratio") != "" && map2.get("list_asset_ratio") != null) {
								range2.replace("${list_asset_ratio}", map2.get("list_asset_ratio").toString(), true,
										false);
							} else {
								range2.replace("${list_asset_ratio}", "", true, false);
							}

							table.getRows().add(deepClone);
						}
						table.getLastRow().remove();
					} else {
						Node deepClone = table.getLastRow().deepClone(true);
						Range range2 = table.getLastRow().getRange();
						range2.replace("${list_id}", "", true, false);
						range2.replace("${list_assets_type}", "", true, false);
						range2.replace("${list_amount}", "", true, false);
						range2.replace("${list_asset_ratio}", "", true, false);
						table.getRows().add(deepClone);
						table.getLastRow().remove();
					}
				}

				// 定期报告十大资产明细
				Table table2 = (Table) document.getChild(NodeType.TABLE, 4, true);
				if (table2 != null) {
					int counts = table2.getFirstRow().getCount();
					if (counts < 5 || (disclosureModVersion.getDocName().contains("现金") && counts == 5)) { // 兼容份额分类模板
						table2 = (Table) document.getChild(NodeType.TABLE, 3, true);
					}
					List<SqlRow> list2 = findTenDetailList(disclosureNotice.getId());
					if (list2 != null && list2.size() > 0) {
						for (Map map3 : list2) {
							Node deepClone2 = table2.getLastRow().deepClone(true);
							Range range2 = table2.getLastRow().getRange();
							if (map3.get("ten_list_id") != "" && map3.get("ten_list_id") != null) {
								range2.replace("${ten_list_id}", map3.get("ten_list_id").toString(), true, false);
							} else {
								range2.replace("${ten_list_id}", "", true, false);
							}
							if (map3.get("ten_assets_code") != "" && map3.get("ten_assets_code") != null) {
								range2.replace("${ten_assets_code}", map3.get("ten_assets_code").toString(), true,
										false);
							} else {
								range2.replace("${ten_assets_code}", "", true, false);
							}
							if (map3.get("ten_assets_name") != "" && map3.get("ten_assets_name") != null) {
								range2.replace("${ten_assets_name}", map3.get("ten_assets_name").toString(), true,
										false);
							} else {
								range2.replace("${ten_assets_name}", "", true, false);
							}
							if (map3.get("ten_assets_scale") != "" && map3.get("ten_assets_scale") != null) {
								range2.replace("${ten_assets_scale}", map3.get("ten_assets_scale").toString(), true,
										false);
							} else {
								range2.replace("${ten_assets_scale}", "", true, false);
							}
							if (map3.get("ten_asset_ratio") != "" && map3.get("ten_asset_ratio") != null) {
								range2.replace("${ten_asset_ratio}", map3.get("ten_asset_ratio").toString(), true,
										false);
							} else {
								range2.replace("${ten_asset_ratio}", "", true, false);
							}

							table2.getRows().add(deepClone2);
						}
						table2.getLastRow().remove();
					} else {
						Node deepClone2 = table2.getLastRow().deepClone(true);
						Range range2 = table2.getLastRow().getRange();
						range2.replace("${ten_list_id}", "", true, false);
						range2.replace("${ten_assets_code}", "", true, false);
						range2.replace("${ten_assets_name}", "", true, false);
						range2.replace("${ten_assets_scale}", "", true, false);
						range2.replace("${ten_asset_ratio}", "", true, false);
						table2.getRows().add(deepClone2);

						table2.getLastRow().remove();
					}
				}

				String isFormal = infoMap.getString("is_formal");
				String fileName = disclosureModVersion.getDocName();
				log.info("非标状态{},文件名称{}", isFormal, fileName);
				// 非标判断 ：1 是非标
				log.info("非标状态{},文件名称{}", isFormal, fileName);
				if (Strings.isNotEmpty(isFormal) && "1".equals(isFormal)) {

				} else {
					log.info("去除表格");
					// 1 non_default_desc设置为空
					range.replace("${non_default_desc}", "", true, false);
					// 2 去除表格
					if (fileName.contains("公募封闭固收") || fileName.contains("公募开放固收") || fileName.contains("私募开放固收")) {
						Table rmTable = (Table) document.getChild(NodeType.TABLE, 5, true);
						rmTable.getRows().removeAt(0);
						rmTable.getRows().removeAt(0);
					}

					if (fileName.contains("私募封闭固收")) {
						Table rmTable = (Table) document.getChild(NodeType.TABLE, 2, true);
						rmTable.getRows().removeAt(0);
						rmTable.getRows().removeAt(0);
					}
				}
				File localPathFile2 = new File(newFilePath + separate + "temp2");

				if (!localPathFile2.exists() && !localPathFile2.isDirectory()) {
					localPathFile2.mkdirs();
				}
				String savePath = newFilePath + separate + "temp2" + separate + disclosureModVersion.getDocName();
				// newFilePath + separate + "temp" + separate +
				// disclosureModVersion.getDocName()
				document.save(savePath, SaveFormat.DOCX);
				log.info("savePath------->{},jqueryPath--------->{}", savePath, onlineUrl + separate + "jquery.min.js");
				String pdfPath = savePath.replaceFirst("docx", "pdf");
				wordToPdfUtil.doc2pdf(savePath, pdfPath);
				new File(newFilePath + separate + "temp" + separate + disclosureModVersion.getDocName()).delete();
				new File(savePath).delete();
				log.info("转存pdf地址{}", pdfPath);
				return pdfPath;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (ostream != null) {
					ostream.close();
				}
				if (fileInputStream != null) {
					fileInputStream.close();
				}

			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	// 份额分类模板处理
	private com.aspose.words.Document isShareSort(com.aspose.words.Document document, DisclosureNotice disclosureNotice,
												  String fileName, Map<String, Object> infoMap) throws Exception {
		// 所有模板中产品概况循环添加下属份额分类
		List<SqlRow> sqlRows = disclosureNoticeDao.findDisclosureRegularShareSort(disclosureNotice.getProdCode(),
				disclosureNotice.getProdBaseDate(), disclosureNotice.getId());
		Table table1 = (Table) document.getChild(NodeType.TABLE, 0, true);
		Table table2 = null;
		Table table3 = null;
		Table table7 = null;

		// 产品净值表现
		if (!fileName.contains("单一资产") && !fileName.contains("现金") && !fileName.contains("私募封闭")) {
			table2 = (Table) document.getChild(NodeType.TABLE, 1, true);
		}
		// 财务指标
		if (fileName.contains("现金")) {
			table3 = (Table) document.getChild(NodeType.TABLE, 1, true);
		} else if (fileName.contains("公募") || fileName.contains("私募开放")) {
			table3 = (Table) document.getChild(NodeType.TABLE, 2, true);
		}
		// 开放式份额信息
		if (fileName.contains("公募开放非固收") || fileName.contains("私募开放非固收")) {
			table7 = (Table) document.getChild(NodeType.TABLE, 5, true);
		} else if (fileName.contains("公募开放固收") || fileName.contains("私募开放固收")) {
			table7 = (Table) document.getChild(NodeType.TABLE, 6, true);
		} else if (fileName.contains("现金")) {
			table7 = (Table) document.getChild(NodeType.TABLE, 4, true);
		}

		if (sqlRows != null && sqlRows.size() > 0) {
			for (Map map1 : sqlRows) {
				Node deepClone1 = table1.getLastRow().deepClone(true);
				Range range1 = table1.getLastRow().getRange();

				if (map1.get("sales_name") != "" && map1.get("sales_name") != null) {
					range1.replace("${list_sale_name}", map1.get("sales_name").toString(), true, false);
				} else {
					range1.replace("${list_sale_name}", "", true, false);
				}

				if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
					range1.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
				} else {
					range1.replace("${list_sale_code}", "", true, false);
				}

				if (map1.get("share_value") != "" && map1.get("share_value") != null) {
					range1.replace("${netval}", map1.get("share_value").toString(), true, false);
				} else {
					range1.replace("${netval}", "", true, false);
				}

				if (map1.get("share_total_value") != "" && map1.get("share_total_value") != null) {
					range1.replace("${netval_total}", map1.get("share_total_value").toString(), true, false);
				} else {
					range1.replace("${netval_total}", "", true, false);
				}

				if (map1.get("end_asset_value") != "" && map1.get("end_asset_value") != null) {
					range1.replace("${netval_vol}", map1.get("end_asset_value").toString(), true, false);
				} else {
					range1.replace("${netval_vol}", "", true, false);
				}

				table1.getRows().add(deepClone1);

				// 产品净值表现
				Node deepClone2;
				Range range2;
				if (table2 != null) {
					deepClone2 = table2.getLastRow().deepClone(true);
					range2 = table2.getLastRow().getRange();
					if (map1.get("sales_name") != "" && map1.get("sales_name") != null) {
						range2.replace("${list_sale_name}", map1.get("sales_name").toString(), true, false);
					} else {
						range2.replace("${list_sale_name}", "", true, false);
					}
					if (map1.get("dur_net_growth") != "" && map1.get("dur_net_growth") != null) {
						range2.replace("${list_report}", map1.get("dur_net_growth").toString()+"%", true, false);
					} else {
						range2.replace("${list_report}", "", true, false);
					}
					if (map1.get("sur_net_growth") != "" && map1.get("sur_net_growth") != null) {
						range2.replace("${list_subsist}", map1.get("sur_net_growth").toString()+"%", true, false);
					} else {
						range2.replace("${list_subsist}", "", true, false);
					}
					table2.getRows().add(deepClone2);
				}

				// 产品财务指标
				Node deepClone3;
				Range range3;
				if (table3 != null) {
					deepClone3 = table3.getLastRow().deepClone(true);
					range3 = table3.getLastRow().getRange();
					if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
						range3.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
					} else {
						range3.replace("${list_sale_code}", "", true, false);
					}
					if (map1.get("end_share_value") != "" && map1.get("end_share_value") != null) {
						range3.replace("${list_netval_end}", map1.get("end_share_value").toString(), true, false);
					} else {
						range3.replace("${list_netval_end}", "", true, false);
					}
					if (map1.get("end_total_value") != "" && map1.get("end_total_value") != null) {
						range3.replace("${list_netval_total}", map1.get("end_total_value").toString(), true, false);
					} else {
						range3.replace("${list_netval_total}", "", true, false);
					}
					if (map1.get("end_prod_share") != "" && map1.get("end_prod_share") != null) {
						range3.replace("${list_end_vol}", map1.get("end_prod_share").toString(), true, false);
					} else {
						range3.replace("${list_end_vol}", "", true, false);
					}
					if (map1.get("end_asset_value") != "" && map1.get("end_asset_value") != null) {
						range3.replace("${list_netval_vol}", map1.get("end_asset_value").toString(), true, false);
					} else {
						range3.replace("${list_netval_vol}", "", true, false);
					}
					if (infoMap.get("list_income") != "" && infoMap.get("list_income") != null) {
						range3.replace("${list_income}", infoMap.get("list_income").toString(), true, false);
					} else {
						range3.replace("${list_income}", "", true, false);
					}
					if (infoMap.get("list_profit") != "" && infoMap.get("list_profit") != null) {
						range3.replace("${list_profit}", infoMap.get("list_profit").toString(), true, false);
					} else {
						range3.replace("${list_profit}", "", true, false);
					}
					table3.getRows().add(deepClone3);
				}

				// 开放式份额
				Node deepClone7;
				Range range7;
				if (table7 != null) {
					deepClone7 = table7.getLastRow().deepClone(true);
					range7 = table7.getLastRow().getRange();

					if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
						range7.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
					} else {
						range7.replace("${list_sale_code}", "", true, false);
					}
					if (map1.get("begin_total_share") != "" && map1.get("begin_total_share") != null) {
						range7.replace("${list_begin_vol}", map1.get("begin_total_share").toString(), true, false);
					} else {
						range7.replace("${list_begin_vol}", "", true, false);
					}
					if (map1.get("dur_purch_share") != "" && map1.get("dur_purch_share") != null) {
						range7.replace("${list_sub_vol}", map1.get("dur_purch_share").toString(), true, false);
					} else {
						range7.replace("${list_sub_vol}", "", true, false);
					}
					if (map1.get("dur_redem_share") != "" && map1.get("dur_redem_share") != null) {
						range7.replace("${list_sub_redeem}", map1.get("dur_redem_share").toString(), true, false);
					} else {
						range7.replace("${list_sub_redeem}", "", true, false);
					}
					if (map1.get("end_total_share") != "" && map1.get("end_total_share") != null) {
						range7.replace("${list_end_vol}", map1.get("end_total_share").toString(), true, false);
					} else {
						range7.replace("${list_end_vol}", "", true, false);
					}
					table7.getRows().add(deepClone7);
				}

			}

			if (table1 != null) {
				table1.getLastRow().remove();
				// 合并资产净值单元格
				//20220406 去除单元格合并
				/*if (fileName.contains("单一资产")) {
					Cell cellStartRange = table1.getRows().get(15).getCells().get(5); // 第15行第6列
					Cell cellEndRange = table1.getRows().get(15 + sqlRows.size() - 1).getCells().get(5); // 第n行第6列
					mergeCells(cellStartRange, cellEndRange);
				}
				if (fileName.contains("私募封闭")) {
					Cell cellStartRange = table1.getRows().get(14).getCells().get(5); // 第14行第6列
					Cell cellEndRange = table1.getRows().get(14 + sqlRows.size() - 1).getCells().get(5); // 第n行第6列
					mergeCells(cellStartRange, cellEndRange);
				}*/
			}
			if (table2 != null)
				table2.getLastRow().remove();
			if (table3 != null) {
				table3.getLastRow().remove();
				// 合并最后两列单元格
				Cell cellStartRange1 = table3.getRows().get(3).getCells().get(5); // 第3行第6列
				Cell cellEndRange1 = table3.getRows().get(3 + sqlRows.size() - 1).getCells().get(5); // 第n行第6列
				mergeCells(cellStartRange1, cellEndRange1);

				Cell cellStartRange2 = table3.getRows().get(3).getCells().get(6); // 第3行第7列
				Cell cellEndRange2 = table3.getRows().get(3 + sqlRows.size() - 1).getCells().get(6); // 第n行第7列
				mergeCells(cellStartRange2, cellEndRange2);

			}
			if (table7 != null) {
				table7.getLastRow().remove();
			}

		} else {
			if (table1 != null) {
				Node deepClone1 = table1.getLastRow().deepClone(true);
				Range range1 = table1.getLastRow().getRange();
				range1.replace("${list_sale_name}", "", true, false);
				range1.replace("${list_sale_code}", "", true, false);
				range1.replace("${netval}", "", true, false);
				range1.replace("${netval_total}", "", true, false);
				range1.replace("${netval_vol}", "", true, false);
				table1.getRows().add(deepClone1);
				table1.getLastRow().remove();
			}
			if (table2 != null) {
				Node deepClone2 = table2.getLastRow().deepClone(true);
				Range range2 = table2.getLastRow().getRange();
				range2.replace("${list_sale_name}", "", true, false);
				range2.replace("${list_report}", "", true, false);
				range2.replace("${list_subsist}", "", true, false);
				table2.getRows().add(deepClone2);
				table2.getLastRow().remove();
			}

			if (table3 != null) {
				Node deepClone3 = table3.getLastRow().deepClone(true);
				Range range3 = table3.getLastRow().getRange();
				range3.replace("${list_sale_code}", "", true, false);
				range3.replace("${list_netval_end}", "", true, false);
				range3.replace("${list_netval_total}", "", true, false);
				range3.replace("${list_end_vol}", "", true, false);
				range3.replace("${list_netval_vol}", "", true, false);
				range3.replace("${list_income}", "", true, false);
				range3.replace("${list_profit}", "", true, false);
				table3.getRows().add(deepClone3);
				table3.getLastRow().remove();
			}
			if (table7 != null) {
				Node deepClone7 = table7.getLastRow().deepClone(true);
				Range range7 = table7.getLastRow().getRange();
				range7.replace("${list_sale_code}", "", true, false);
				range7.replace("${list_begin_vol}", "", true, false);
				range7.replace("${list_sub_vol}", "", true, false);
				range7.replace("${list_sub_redeem}", "", true, false);
				range7.replace("${list_end_vol}", "", true, false);
				table7.getRows().add(deepClone7);
				table7.getLastRow().remove();
			}

		}
		return document;
	}

	/**
	 * @Description 合并单元格
	 * @Date 2021/11/30 16:08
	 * @Param [startCell, endCell] 开始cell 结束cell
	 * @Return void
	 */
	private static void mergeCells(Cell startCell, Cell endCell) {
		Table parentTable = startCell.getParentRow().getParentTable();

		Point startCellPos = new Point(startCell.getParentRow().indexOf(startCell),
				parentTable.indexOf(startCell.getParentRow()));
		Point endCellPos = new Point(endCell.getParentRow().indexOf(endCell),
				parentTable.indexOf(endCell.getParentRow()));
		Rectangle mergeRange = new Rectangle(Math.min(startCellPos.x, endCellPos.x),
				Math.min(startCellPos.y, endCellPos.y), Math.abs(endCellPos.x - startCellPos.x) + 1,
				Math.abs(endCellPos.y - startCellPos.y) + 1);

		for (Row row : parentTable.getRows()) {
			for (Cell cell : row.getCells()) {
				Point currentPos = new Point(row.indexOf(cell), parentTable.indexOf(row));

				if (mergeRange.contains(currentPos)) {
					if (currentPos.x == mergeRange.x)
						cell.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
					else
						cell.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);

					if (currentPos.y == mergeRange.y)
						cell.getCellFormat().setVerticalMerge(CellMerge.FIRST);
					else
						cell.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
				}
			}
		}
	}

	@API(desc = "批量下载", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public void batchDownLoad(SqlParam<Object> object) throws Exception {

	}

	@API(desc = "下载文档", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public void download(SqlParam<Object> params) throws Exception {
	}

	@API(desc = "数据补录", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public void RecordInfo(SqlParam<Object> params) throws Exception {
	}

	@API(desc = "查询公告状态", auth = APIAuth.NO)
	public SqlResult<DisclosureNotice> findDisclosureNoticeStatus(SqlParam<DisclosureNotice> param) throws Exception {
		return disclosureNoticeDao.findDisclosureNoticeStatus(param);
	}

	/**
	 * 信披公告复核
	 * 信披公告状态变更为
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "公告复核", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String checkDisclosureNotice(SqlParam<DisclosureNotice> params) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("id", params.getModel().getId());
		try {
			disclosureNoticeDao.updateDisclosureStatus(param);
		} catch (Exception e) {
			return RequestSupport.updateReturnJson(true, "复核失败:" + e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "复核成功", null).toString();
	}

	/**
	 * 将发布成功和取消发布的数据重新变更为生成待补录或生成待发布
	 * 插入记录表中
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@API(desc = "批量状态变更", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String batchChangeNoticeStatus(SqlParam<Object> object) throws Exception {
		Map<String, Object> paramsFront = RequestSupport.getParameters();
		AtomicBoolean is_success = new AtomicBoolean(true);
		String err_message = "";

		String change_reason = String.valueOf(paramsFront.get("change_reason"));//变更原因
		String disclosure_status = String.valueOf(paramsFront.get("disclosure_status"));//变更后状态
		String listObj = (String) paramsFront.get("list");
		List<HashMap> disclosureNoticeList = JSONObject.parseArray(listObj, HashMap.class);
		//查询公告是否需要生成待补录
		if (!CollectionUtils.isEmpty(disclosureNoticeList) && StringUtils.equals(disclosure_status,"1")) {
			for (HashMap map:disclosureNoticeList) {
				Map<String,Object> check_param = new HashMap<>();
				check_param.put("prodCode",map.get("prodCode"));
				check_param.put("disclosureType",map.get("disclosureType"));
				check_param.put("disclosureSonType",map.get("disclosureSonType"));
				SqlRow sqlRow = null;
				try {
					sqlRow = disclosureNoticeChannelDao.getIfClearing(check_param);
					if(sqlRow != null){
						if(StringUtils.equals(sqlRow.getString("if_clearing"),"0")){
							err_message += "产品代码:["+sqlRow.getString("prod_code")+"]信披类型:["+sqlRow.getString("disclosure_type")+"]信披子类型:["+sqlRow.getString("disclosure_son_type")+"],";
						}
					}
				} catch (Exception e) {
					log.error("操作信披公告状态变更数据库操作异常{}", e);
				}
			}
			if(StringUtils.isNotBlank(err_message)){
				return RequestSupport.updateReturnJson(true, err_message+"不需要生成待补录", null).toString();
			}
			err_message = "";
		}
		DaoUtil.doTrans(() -> {
			disclosureNoticeList.forEach(notice -> {
				Map<String,Object> record_param = new HashMap<>();
				try {//参数处理，记入变更记录表
					record_param.put("changeReason", change_reason);
					record_param.put("disclosureStatusAfter", disclosure_status);
					record_param.put("disclosureStatusAhead", notice.get("disclosureStatus"));
					record_param.put("t8DisclosureNoticeId", notice.get("id"));
					record_param.put("noticeVersionId", notice.get("noticeVersionId"));
					record_param.put("crtDate", PublicUtils.getSysWordDay());
					record_param.put("crtTime", DateUtil.formatDate(new Date(),DateFormatEnum.TIME_FORMAT));
					record_param.put("crtUserId", SysUtil.getSysUserParamValue("sys_user_userid"));
					record_param.put("crtUserName", SysUtil.getSysUserParamValue("sys_user_username"));
					disclosureNoticeDao.updateDisclosureNoticeStatus(record_param);//更新公告状态
					disclosureNoticeDao.updateDisclosureVersionStatus(record_param);//更新版本状态
					record_param.put("disclosureStatusAfter", DisclosureStatus.waitPub.getItemKey());//状态变更后渠道状态都保持待发布状态
					disclosureNoticeDao.updateDisclosureChannelStatus(record_param);//变更公告版本发布的所有渠道状态
					record_param.put("disclosureStatusAfter", disclosure_status);
					disclosureNoticeDao.insertDisclosureNoticeRecord(record_param);//插入记录
				} catch (Exception e) {
					is_success.set(false);
					log.error("操作信披公告插入状态变更数据库操作异常{}", e);
				}
			});
		});
		if (is_success.get()) {
			return RequestSupport.updateReturnJson(true, "批量状态变更完成", null).toString();
		} else {
			return RequestSupport.updateReturnJson(true, "批量状态变更异常", null).toString();
		}
	}

	public String formatStr(String str, String type) {
		String arrays[] = null;
		String mStr = Strings.EMPTY;
		arrays = str.split(type);
		for (int i = 0; i < arrays.length; i++) {
			if (i == 0) {
				mStr = arrays[i];
			} else {
				if ("".equals(arrays[i]) || arrays[i].length() == 0)
					continue;
				mStr = mStr + ControlChar.LINE_BREAK + ControlChar.NON_BREAKING_SPACE + ControlChar.NON_BREAKING_SPACE
						+ ControlChar.NON_BREAKING_SPACE + ControlChar.NON_BREAKING_SPACE
						+ ControlChar.NON_BREAKING_SPACE + ControlChar.NON_BREAKING_SPACE
						+ ControlChar.NON_BREAKING_SPACE + arrays[i];
			}

		}

		if (arrays != null)
			str = mStr;
		return str;
	}

	public String getReportType(String str) throws Exception {
		String reportType = str;
		List<SqlRow> accRow2 = disclosureNoticeChannelDao.findReportType(str);
		if(accRow2!=null && accRow2.size()>0){
			for(int i=0;i<accRow2.size(); i++){
				if(str.equals(accRow2.get(i).getString("itemkey"))){
					reportType= accRow2.get(i).getString("itemval");
				}
			}
		}
		return reportType;
	}

	@API(desc = "查询产品其他公告信披公告信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNotice> findDisclosureOtherNoticesAuth(SqlParam<DisclosureNotice> params)
			throws Exception {
		SqlResult<DisclosureNotice> result = disclosureNoticeDao.findDisclosureOtherNoticesAuth(params);
		return result;
	}

	@API(desc = "查询手工公告信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureNotice> findDisclosureManualNoticesAuth(SqlParam<DisclosureNotice> params)
			throws Exception {
		SqlResult<DisclosureNotice> result = disclosureNoticeDao.findDisclosureManualNoticesAuth(params);
		return result;
	}

}
