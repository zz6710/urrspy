package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.dao.DisclosureRuleDao;
import com.kayak.pms.disclosureControl.dao.DisclosureProdRuleDao;
import com.kayak.pms.disclosureControl.dao.DisclosureProdTaskDao;
import com.kayak.pms.disclosureControl.model.DisclosureProdRule;
import com.kayak.pms.global.constants.DisclosureSonType;
import com.kayak.pms.global.constants.DisclosureType;
import com.kayak.pms.global.constants.RuleDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@APIDefine(desc = "产品信披规则服务", model = DisclosureProdRule.class)
public class DisclosureProdRuleService {

	@Autowired
	private DisclosureProdRuleDao disclosureProdRuleDao;
	@Autowired
	private DisclosureProdTaskService disclosureProdTaskService;
	@Autowired
	private DisclosureProdTaskDao t8DisclosureProdTaskDao;
	@Autowired
	private DisclosureRuleDao disclosureRuleDao;

	@API(desc = "查询产品信披规则信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<DisclosureProdRule> findDisclosureProdRulesAuth(SqlParam<DisclosureProdRule> params) throws Exception {
		return findDisclosureProdRules(params);
	}

	@API(desc = "查询产品信披规则信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureProdRule> findDisclosureProdRules(SqlParam<DisclosureProdRule> params) throws Exception {
		return disclosureProdRuleDao.findDisclosureProdRules(params);
	}
	/**
	* @功能描述:根据信披类型数据或信披子类型数据查询相应的信披生成规则信息是否已存在
	* @params:[params]
	* @return:java.lang.String
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	@API(desc = "查询信披生成规则信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public String findRulesByDisclosureForExist(SqlParam<DisclosureProdRule> params) throws Exception {
		DisclosureProdRule disclosureProdRule = params.getModel();
		if (DisclosureType.regular.getItemKey().equals(disclosureProdRule.getDisclosureType())||DisclosureType.ensemble.getItemKey().equals(disclosureProdRule.getDisclosureType())||
				DisclosureType.preSale.getItemKey().equals(disclosureProdRule.getDisclosureType())||DisclosureType.net.getItemKey().equals(disclosureProdRule.getDisclosureType())){
			if ("".equals(disclosureProdRule.getDisclosureSonType())||disclosureProdRule.getDisclosureSonType()==null){
				return RequestSupport.updateReturnJson(true, "", null).toString();
			}else if("".equals(disclosureProdRule.getProdCode())||disclosureProdRule.getProdCode()==null){
				return RequestSupport.updateReturnJson(true, "", null).toString();
			}else {
				Integer count = disclosureProdRuleDao.findRulesByDisclosureForExist(params);
				if (count>0){
					return RequestSupport.updateReturnJson(false, "该产品的此类型的信披规则已存在", null).toString();
				}
				return RequestSupport.updateReturnJson(true, "", null).toString();
			}
		}else if ("".equals(disclosureProdRule.getDisclosureType())||disclosureProdRule.getDisclosureType()==null){
			return RequestSupport.updateReturnJson(true, "", null).toString();
		}else{
			if ("".equals(disclosureProdRule.getProdCode())||disclosureProdRule.getProdCode()==null){
				return RequestSupport.updateReturnJson(true, "", null).toString();
			}else{
				Integer count = disclosureProdRuleDao.findRulesByDisclosureForExist(params);
				if (count>0){
					return RequestSupport.updateReturnJson(false, "该产品的此类型的信披规则已存在", null).toString();
				}
				return RequestSupport.updateReturnJson(true, "", null).toString();
			}
		}
	}

	/**
	 * 功能：查询已经维护过产品信披规则的产品作为复制功能下拉框
	 * 作者：rennannan
	 * 日期：20210518
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询已有规则产品", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureProdRule> findDisclosureProdDict(SqlParam<DisclosureProdRule> params) throws Exception {
		return disclosureProdRuleDao.findDisclosureProdDict(params);
	}

	/**
	 * 功能：查询不存在产品信息规则表中的产品作为下拉框
	 * 作者：rennannan
	 * 日期：20210518
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询需复制规则的产品", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<DisclosureProdRule> findNeedCopyProds(SqlParam<DisclosureProdRule> params) throws Exception {
		return disclosureProdRuleDao.findNeedCopyProds(params);
	}

	@API(desc = "添加产品信披规则", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String addDisclosureProdRule(SqlParam<DisclosureProdRule> params) throws Exception {
		//验证是否已经存在记录，如果已经存在进行提示
		// 净值整体公告也不能新增产品信披规则
		if (params.getModel().getDisclosureType().equals(DisclosureType.ensemble.getItemKey())||params.getModel().getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())){
			return RequestSupport.updateReturnJson(false, "不支持新增整体报告或净值整体报告的产品信披规则", null).toString();
		}
		String date = DateUtil.getSysWordDay();;
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
		Integer result = disclosureProdRuleDao.findRulesByDisclosureForExist(params);
		if (result> 0) {
			return RequestSupport.updateReturnJson(false, "该产品已存在相同信披类型的规则", null).toString();
		}
		params.getModel().setCrtDate(date);
		params.getModel().setCrtTime(time);
		params.getModel().setCrtUserId(userid);
		params.getModel().setCrtUserName(username);
		params.getModel().setSource(RuleDataSource.manual.getItemKey());//手工新增
		DaoUtil.doTrans(() -> {
			//插入产品信披规则表
			disclosureProdRuleDao.addDisclosureProdRule(params.getModel());
		});
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}
	/**
	 * 功能：复制产品规则
	 * 作者：rennannan
	 * 日期：20210518
	 *
	 * @return
	 */
	@API(desc = "复制产品规则", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public String copyDisclosureProdRule(SqlParam<DisclosureProdRule> params) throws Exception {
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
		String prodCd =  params.getModel().getProdCode();
		//根据产品id查询规则id
		List<DisclosureProdRule> disclosureProdRuleList = disclosureProdRuleDao.findDisclosureProdRuleList(prodCd);
		//for循环产品 循环插入多个规则
		String prodCodes = params.getModel().getProdCodes();
		if (StringUtils.isBlank(prodCodes))
			return RequestSupport.updateReturnJson(false, "没有可复制产品", null).toString();
		String[] prodCode = prodCodes.split(",");
		DaoUtil.doTrans(() -> {
			for (DisclosureProdRule rul: disclosureProdRuleList) {
				for (int i = 0; i < prodCode.length; i++) {
					if (prodCd.equals(prodCode[i]))
						continue;
					rul.setProdCode(prodCode[i]);
					rul.setSource(RuleDataSource.copy.getItemKey());//"3"复制产品信披规则
					rul.setCrtDate(date);//创建日期
					rul.setCrtTime(time);//创建时间
					rul.setCrtUserId(userid);//创建人id
					rul.setCrtUserName(username);//创建人姓名
					rul.setUpdDate("");
					rul.setUpdTime("");
					rul.setUpdUserId("");
					rul.setUpdUserName("");
					//覆盖该产品该类型已存在的产品信披规则
					this.disclosureProdRuleDao.deleteProdRuleByType(rul);
					this.disclosureProdRuleDao.addDisclosureProdRule(rul);
				}
			}
		});
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}
	/**
	* @功能描述:修改产品信披规则
	* @params:[params]
	* @return:java.lang.String
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	@API(desc = "修改产品信披规则",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateDisclosureProdRule(SqlParam<DisclosureProdRule> params) throws Exception {
		String date = DateUtil.getSysWordDay();;
		String time = DateUtil.getNowTime();
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
		String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
		params.getModel().setUpdDate(date);
		params.getModel().setUpdTime(time);
		params.getModel().setUpdUserId(userid);
		params.getModel().setUpdUserName(username);
		params.getModel().setSource(RuleDataSource.manual.getItemKey());//手工新增

		Integer result = disclosureProdRuleDao.findRulesByDisclosureForExist(params);
		if (result> 0) {
			return RequestSupport.updateReturnJson(false, "该产品已存在相同信披类型的规则", null).toString();
		}

		try {
			DaoUtil.doTrans(() -> {
				disclosureProdRuleDao.updateDisclosureProdRule(params);
				//修改了产品规则的已生成的相关未生成公告的任务需要及时更新
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("disclosureType",params.getModel().getDisclosureType());
				parameters.put("disclosureSonType", params.getModel().getDisclosureSonType());
				parameters.put("t8DisclosureProdRuleId",params.getModel().getId());
				disclosureProdTaskService.autoUpdateDisclosureTasks(parameters);
			});
		} catch (Exception e) {
			return RequestSupport.updateReturnJson(false, "操作失败", null).toString();
		}
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}
	/**
	* @功能描述:删除
	* @params:[params]
	* @return:int
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	@API(desc = "删除产品信披规则",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteDisclosureProdRule(SqlParam<DisclosureProdRule> params) throws Exception {
		DaoUtil.doTrans(() -> {
			disclosureProdRuleDao.deleteDisclosureProdRule(params).getEffect();
		});
		return 1;
	}

	/**
	 * 产品信披规则-导出权限控制
	 * @return
	 */
	@API(desc = "导出",auth = APIAuth.YES)
	public String exportProdRuleRightControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	@API(desc = "根据产品名称查询产品信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlRow findBassInfoByProdCode(SqlParam<DisclosureProdRule> params) throws Exception {
		SqlRow sqlRow =disclosureProdRuleDao.findBassInfoByProdCode(params);
		if(sqlRow != null)
			sqlRow.put("success",true);
		return sqlRow;
	}
}
