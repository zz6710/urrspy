package com.kayak.pms.basePublish.service;

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
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.basePublish.dao.DisclosureRuleDao;
import com.kayak.pms.basePublish.model.DisclosureChannelRule;
import com.kayak.pms.basePublish.model.DisclosureRule;
import com.kayak.pms.disclosureControl.dao.DisclosureProdRuleDao;
import com.kayak.pms.disclosureControl.model.DisclosureProdRule;
import com.kayak.pms.disclosureControl.model.DisclosureProdTask;
import com.kayak.pms.disclosureControl.service.DisclosureProdTaskService;
import com.kayak.pms.global.constants.DisclosureSonType;
import com.kayak.pms.global.constants.DisclosureType;
import com.kayak.pms.global.constants.RuleDataSource;
import com.kayak.pms.global.constants.XpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@APIDefine(desc = "信披规则实体类服务", model = DisclosureRule.class)
public class DisclosureRuleService {

    @Autowired
    private DisclosureRuleDao disclosureRuleDao;
    @Autowired
    private DisclosureProdRuleDao disclosureProdRuleDao;
    @Autowired
    private DisclosureProdTaskService disclosureProdTaskService;

    /**
    * @功能描述:查询信披规则信息,查询信披规则对应模板,查询信披规则对应模板版本号,查询信披规则对应模板版本是否补录
    * @params:[params]
    * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureRule>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "查询信披规则信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<DisclosureRule> findDisclosureRulesAuth(SqlParam<DisclosureRule> params) throws Exception {
        return findDisclosureRules(params);
    }
    @API(desc = "查询信披规则对应模板", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureRule> findDisclosureModsWithRule(SqlParam<DisclosureRule> params) throws Exception {
        return disclosureRuleDao.findDisclosureModsWithRule(params);
    }
    @API(desc = "查询信披规则对应模板版本号", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureRule> findDisclosureModsVWithRule(SqlParam<DisclosureRule> params) throws Exception {
        return disclosureRuleDao.findDisclosureModsVWithRule(params);
    }
    @API(desc = "查询信披规则对应模板版本是否补录", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureRule> clearingOrNot(SqlParam<DisclosureRule> params) throws Exception {
        return disclosureRuleDao.clearingOrNot(params);
    }
    @API(desc = "查询信披规则信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureRule> findDisclosureRules(SqlParam<DisclosureRule> params) throws Exception {
//        params.setMakeSql(true);
        return disclosureRuleDao.findDisclosureRules(params);
    }

    /**
    * @功能描述:新增信披规则，默认启动
    * @params:[params]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "新增信披规则信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addDisclosureRule(SqlParam<DisclosureRule> params) throws Exception {
        if (disclosureRuleDao.checkNameIsExists(params).getRows().size() > 0) {
            return RequestSupport.updateReturnJson(false, "规则名称已存在", null).toString();
        }
        String date = DateUtil.getSysWordDay();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
        params.getModel().setCrtDate(date);
        params.getModel().setCrtTime(time);
        params.getModel().setCrtUserId(userid);
        params.getModel().setCrtUserName(username);
        //默认启动
        params.getModel().setStatus(XpStatus.start.getItemKey());
        //判断是否已经存在该类型的数据
        Integer count = disclosureRuleDao.checkIsExists(params);
        if (count>0){
            return RequestSupport.updateReturnJson(false, "该适用类型的信披规则已存在", null).toString();
        }
        int effect = disclosureRuleDao.addDisclosureRule(params).getEffect();
        if (effect > 0) {
            return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
        } else {
            return RequestSupport.updateReturnJson(false, "操作失败", null).toString();
        }
    }
    /**
    * @功能描述:修改信披规则
    * @params:[params]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "修改信披规则信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateDisclosureRule(SqlParam<DisclosureRule> params) throws Exception {
        String date = DateUtil.getSysWordDay();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
        params.getModel().setUpdDate(date);
        params.getModel().setUpdTime(time);
        params.getModel().setUpdUserId(userid);
        params.getModel().setUpdUserName(username);
        SqlResult<DisclosureRule> result = disclosureRuleDao.checkNameIsExists(params);
        if (result.getRows().size() > 0) {
            return RequestSupport.updateReturnJson(false, "规则名称已存在", null).toString();
        }
        //判断是否已经存在改类型的数据
        Integer count  = disclosureRuleDao.checkIsExists(params);
        if (count>0){
            return RequestSupport.updateReturnJson(false, "该适用类型的信披规则已存在", null).toString();
        }
        try {
            DaoUtil.doTrans(() -> {
                disclosureRuleDao.updateDisclosureRule(params).getEffect();
                //修改了产品规则的，且已生成的相关未完成任务需要及时更新
                // 整体公告修改信披规则时需要实时更新未生成公告任务
                if (params.getModel().getDisclosureType().equals(DisclosureType.ensemble.getItemKey())
                        ||params.getModel().getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())) {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("disclosureType", params.getModel().getDisclosureType());
                    parameters.put("disclosureSonType", params.getModel().getDisclosureSonType());
                    parameters.put("t8DisclosureRuleId", params.getModel().getId());
                    parameters.put("prodForm", params.getModel().getProdForm());
                    parameters.put("prodObj", params.getModel().getProdObj());
                    parameters.put("prodClcMth", params.getModel().getProdClcMth());
                    parameters.put("invPrdDime", params.getModel().getInvPrdDime());
                    parameters.put("invPrdLen", params.getModel().getInvPrdLen());
                    parameters.put("prodInvTyp", params.getModel().getProdInvTyp());
                    parameters.put("prodSerCd", params.getModel().getProdSerCd());
                    disclosureProdTaskService.autoUpdateDisclosureTasks(parameters);
                }
            });
        } catch (Exception e) {
            return RequestSupport.updateReturnJson(false, "操作失败", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }
    /**
    * @功能描述:删除信披规则
    * @params:[params]
    * @return:String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "删除信披规则信息", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String deleteDisclosureRule(SqlParam<DisclosureRule> params) throws Exception {
        try{
            disclosureRuleDao.deleteDisclosureRule(params).getEffect();
        }catch (Exception e) {
            return RequestSupport.updateReturnJson(false, "操作失败", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }
    /**
    * @功能描述:启用信披规则信息
    * @params:[params]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/7/14
    */
    @API(desc = "启用信披规则信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateStatusOnEnable(SqlParam<DisclosureRule> params) throws Exception {
            //修改信披规则状态,启用时如果是整体公告则更新公告任务
        DaoUtil.doTrans(() -> {
            params.getModel().setStatus(XpStatus.start.getItemKey());//1代表已启用
            disclosureRuleDao.updateDisRuleStatus(params.getModel());
            // 整体公告启用时信披规则时需要实时更新未生成公告任务
            if (params.getModel().getDisclosureType().equals(DisclosureType.ensemble.getItemKey())
                    ||params.getModel().getDisclosureSonType().equals(DisclosureSonType.netValueEntity.getItemKey())) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("disclosureType", params.getModel().getDisclosureType());
                parameters.put("disclosureSonType", params.getModel().getDisclosureSonType());
                parameters.put("t8DisclosureRuleId", params.getModel().getId());
                parameters.put("prodForm", params.getModel().getProdForm());
                parameters.put("prodObj", params.getModel().getProdObj());
                parameters.put("prodClcMth", params.getModel().getProdClcMth());
                parameters.put("invPrdDime", params.getModel().getInvPrdDime());
                parameters.put("invPrdLen", params.getModel().getInvPrdLen());
                parameters.put("prodInvTyp", params.getModel().getProdInvTyp());
                parameters.put("prodSerCd", params.getModel().getProdSerCd());
                disclosureProdTaskService.autoUpdateDisclosureTasks(parameters);
            }
        });
            return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    /**
    * @功能描述:定时任务信披规则信息匹配产品
    * @params:[params]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/7/14
    */
    @API(desc = "定时任务信披规则信息匹配产品", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String getProdDisRule(SqlParam<DisclosureRule> params) throws Exception {

        try {
            //查询所有产品
            List<SqlRow> allProdByCode = disclosureRuleDao.prodIDByProdParamsConfig(params);
            //规则里现有产品
            List<SqlRow> prodCodeList = disclosureRuleDao.disclosureProd(params);
            //查询所有生成规则里启动状态的信披类型（除整体公告和净值整体报告）
            List<SqlRow> prodDisclosureType = disclosureRuleDao.prodDisclosureType(params);
            //没有启动的信披规则就结束定时任务
            if (prodDisclosureType.size()==0){
                return null;
            }
            DaoUtil.doTrans(() -> {
                //待插入
                for (SqlRow row: prodDisclosureType) {
                    Map<String, Object> param = new HashMap<>();
                    String disclosureType = row.getString("disclosure_type");
                    String disclosureSonType = row.getString("disclosure_son_type");
                    param.put("disclosureType",disclosureType);
                    param.put("disclosureSonType",disclosureSonType);
                    SqlParam<DisclosureRule> ruleParam = new FetcherData<>(param,DisclosureRule.class);
                    //产品信披规则中针对某种信披类型存在的产品
                    List<SqlRow> prodIDByProdRule = disclosureRuleDao.prodIDByProdRule(ruleParam);

                    List<SqlRow> forInsert =allProdByCode.stream().filter(item -> !prodIDByProdRule.contains(item)).collect(Collectors.toList());
                    //将信披规则中属性设置到产品信披规则属性中
                    DisclosureProdRule prodRule = new DisclosureProdRule();
                    if (!CollectionUtils.isEmpty(forInsert)) {
                        forInsert.forEach(item -> { });
                        for (int i = 0; i < forInsert.size(); i++) {
                            String prodCode = forInsert.get(i).getString("prod_code");
                            //设置产品id,代码
                            prodRule.setProdCode(prodCode);
                            //匹配该条产品信息规则渠道
                            getDisclosureChannelRule(prodCode,disclosureType,disclosureSonType,false);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "生成产品信披规则失败："+e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }
    /**
    * @功能描述:查询满足条件的信披规则
    * @params:[params]
    * @return:com.kayak.core.sql.SqlRow
    * @Athor:ouyifan
    * @date:2022/7/14
    */
    @API(desc = "查询满足条件的信披规则id", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public SqlRow getProdDisRuleForAdd(SqlParam<DisclosureRule> params) throws Exception {
                    //匹配该条产品信息规则渠道
        String prodCode = params.getModel().getProdCode();
        String disclosureType = params.getModel().getDisclosureType();
        String disclosureSonType ="";
        SqlRow sqlRow = new SqlRow();
        if (StringUtils.isNotEmpty(params.getModel().getDisclosureSonType())){
            disclosureSonType =params.getModel().getDisclosureSonType();
        }
        DisclosureProdRule disclosureProdRule = getDisclosureChannelRule(prodCode,disclosureType,disclosureSonType,true);
        if (StringUtils.isNotEmpty(disclosureProdRule.getT8DisclosureRuleId())){
            Map<String, Object> param = new HashMap<>();
            param.put("id",disclosureProdRule.getT8DisclosureRuleId());
            SqlParam<DisclosureProdRule> taskParams = new FetcherData<DisclosureProdRule>(param,DisclosureProdRule.class);
            sqlRow =disclosureProdRuleDao.findDisclosureRules(taskParams);
            sqlRow.put("channelIds",disclosureProdRule.getChannelIds());
            sqlRow.put("prodCode",prodCode);
            sqlRow.put("disclosureType",disclosureType);
            sqlRow.put("disclosureSonType",disclosureSonType);
            sqlRow.put("success",true);
            sqlRow.put("flag",false);
            return sqlRow;
        }
        //返回前端true 用于拦截信息
        sqlRow.put("success",true);
        sqlRow.put("flag",true);
        return sqlRow;
    }

    /**
    * @功能描述:查询并匹配参数
    * @params:[prodCode, disclosureType, disclosureSonType, forCheck]
    * @return:com.kayak.pms.disclosureControl.model.DisclosureProdRule
    * @Athor:ouyifan
    * @date:2022/7/14
    */
    private DisclosureProdRule getDisclosureChannelRule(String prodCode,String disclosureType,String disclosureSonType,Boolean forCheck) throws Exception {
        DisclosureRule disclosureRule = new DisclosureRule();
        List<SqlRow> ProdParamsConfigByCode= disclosureProdRuleDao.ProdParamsConfigByCode(prodCode);

        disclosureRule.setInvPrdDime((String) ProdParamsConfigByCode.get(0).get("INV_PRD_DIME"));//周期维度
        disclosureRule.setInvPrdLen((String) ProdParamsConfigByCode.get(0).get("INV_PRD_LEN"));//周期长度
        disclosureRule.setProdObj((String) ProdParamsConfigByCode.get(0).get("PROD_OBJ"));//销售对象
        disclosureRule.setProdClcMth((String) ProdParamsConfigByCode.get(0).get("PROD_CLC_MTH"));//募集方式
        disclosureRule.setProdForm((String) ProdParamsConfigByCode.get(0).get("PROD_FORM"));//产品形态
        disclosureRule.setProdSerCd((String) ProdParamsConfigByCode.get(0).get("PROD_SER_CD"));//产品系列
        disclosureRule.setProdInvTyp((String) ProdParamsConfigByCode.get(0).get("PROD_INV_TYP"));//产品系列
        disclosureRule.setProdFullName((String) ProdParamsConfigByCode.get(0).get("PROD_NM"));//产品全称
        //分级产品标志
        disclosureRule.setMotherFundFlag((String) ProdParamsConfigByCode.get(0).get("MOTHER_FUND_FLAG"));
        disclosureRule.setDisclosureType(disclosureType);
        disclosureRule.setDisclosureSonType(disclosureSonType);
        if (forCheck){
            return addDisclosureChannelRule(prodCode,disclosureRule,true);
        }
        addDisclosureChannelRule(prodCode,disclosureRule,false);
        return null;
    }
    /**
    * @功能描述: 产品匹配信披规则id、渠道配置规则id
    * @params:[prodCode, disclosureRule, forCheck]
    * @return:com.kayak.pms.disclosureControl.model.DisclosureProdRule
    * @Athor:ouyifan
    * @date:2022/7/14
    */
    private DisclosureProdRule addDisclosureChannelRule(String prodCode,DisclosureRule disclosureRule,boolean forCheck) throws Exception {
        DisclosureRule dis = new DisclosureRule();

        //查询 该类型配置的规则id结果集，进行最优比较
        List<SqlRow> disclosureIDByDisclosureType= disclosureRuleDao.disclosureIDByDisclosureType(disclosureRule);
        if (disclosureIDByDisclosureType.size()!=0){
            //取结果集首条数据作为当前比较基准
            String ruleId = (String) disclosureIDByDisclosureType.get(0).get("id");
            disclosureRule.setId(ruleId);
            dis.setId(ruleId);
            //取符合条件的参数数量
            Integer ruleCount = disclosureRuleDao.cumCount(dis);
            String ruleId_j="";
            //插入渠道ids,每条产品信披规则仅匹配一条渠道规则,没有匹配的可为空,匹配的渠道规则应满足条件->参数匹配数量最高,若数量相等需满足参数优先级
             for (int j = 0; j <disclosureIDByDisclosureType.size() ; j++) {
                 //结果集等于一条或第k条大于长度时跳出
                if (j+1 == disclosureIDByDisclosureType.size())
                    break;
                 ruleId_j =(String) disclosureIDByDisclosureType.get(j+1).get("id");
                 dis.setId(ruleId_j);//作比较变量
                 Integer ruleCount_j = disclosureRuleDao.cumCount(dis);
                if (ruleCount_j>ruleCount){
                    ruleCount = ruleCount_j;//将计数作为当前比较基准
                    disclosureRule.setId(ruleId_j);//作比较结果
                } else if (ruleCount_j.equals(ruleCount)){//相等的参数数量则进行优先级比较
                    List<DisclosureRule> sqlRowsJ =disclosureRuleDao.findRuleParams(ruleId_j);
                    List<DisclosureRule> sqlRows =disclosureRuleDao.findRuleParams(ruleId);
                    //TODO 优先级判断递归执行参数比较
                    //顺序执行 形态—>销售对象->募集方式->产品系列->产品投资性质
                    if ((StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdForm())&&StringUtils.isNotEmpty(sqlRows.get(0).getProdForm()))
                            ||(StringUtils.isEmpty(sqlRowsJ.get(0).getProdForm())&&StringUtils.isEmpty(sqlRows.get(0).getProdForm()))){//形态

                        if ((StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdObj())&&StringUtils.isNotEmpty(sqlRows.get(0).getProdObj()))
                                ||(StringUtils.isEmpty(sqlRowsJ.get(0).getProdObj())&&StringUtils.isEmpty(sqlRows.get(0).getProdObj()))){//销售对象

                            if ((StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdClcMth())&&StringUtils.isNotEmpty(sqlRows.get(0).getProdClcMth()))
                                    ||(StringUtils.isEmpty(sqlRowsJ.get(0).getProdClcMth())&&StringUtils.isEmpty(sqlRows.get(0).getProdClcMth()))){//募集方式

                                if ((StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdSerCd())&&StringUtils.isNotEmpty(sqlRows.get(0).getProdSerCd()))
                                        ||(StringUtils.isEmpty(sqlRowsJ.get(0).getProdSerCd())&&StringUtils.isEmpty(sqlRows.get(0).getProdSerCd()))){//产品系列

                                    if ((StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdInvTyp())&&StringUtils.isNotEmpty(sqlRows.get(0).getProdInvTyp()))
                                            ||(StringUtils.isEmpty(sqlRowsJ.get(0).getProdInvTyp())&&StringUtils.isEmpty(sqlRows.get(0).getProdInvTyp()))){//产品投资性质
                                        //分级产品标志
                                        if ((StringUtils.isNotEmpty(sqlRowsJ.get(0).getMotherFundFlag())&&StringUtils.isNotEmpty(sqlRows.get(0).getMotherFundFlag()))
                                                ||(StringUtils.isEmpty(sqlRowsJ.get(0).getMotherFundFlag())&&StringUtils.isEmpty(sqlRows.get(0).getMotherFundFlag()))){
                                            continue;
                                        }else if (StringUtils.isNotEmpty(sqlRowsJ.get(0).getMotherFundFlag())&&StringUtils.isEmpty(sqlRows.get(0).getMotherFundFlag())){
                                            disclosureRule.setId(ruleId_j);
                                            continue;
                                        }else {
                                            continue;
                                        }
                                    }else if (StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdInvTyp())&&StringUtils.isEmpty(sqlRows.get(0).getProdInvTyp())){
                                        disclosureRule.setId(ruleId_j);
                                        continue;
                                    }else {
                                        continue;
                                    }
                                }else if (StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdSerCd())&&StringUtils.isEmpty(sqlRows.get(0).getProdSerCd())){
                                    disclosureRule.setId(ruleId_j);
                                    continue;
                                }else {
                                    continue;
                                }
                            }else if (StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdClcMth())&&StringUtils.isEmpty(sqlRows.get(0).getProdClcMth())){
                                disclosureRule.setId(ruleId_j);
                                continue;
                            }else {
                                continue;
                            }
                        }else if (StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdObj())&&StringUtils.isEmpty(sqlRows.get(0).getProdObj())){
                            disclosureRule.setId(ruleId_j);
                            continue;
                        }else {
                            continue;
                        }
                    }else if (StringUtils.isNotEmpty(sqlRowsJ.get(0).getProdForm())&&StringUtils.isEmpty(sqlRows.get(0).getProdForm())){
                        disclosureRule.setId(ruleId_j);
                        continue;
                    }else {
                        continue;
                    }
              }
            }
        }
        //生成规则转换产品生成规则
        DisclosureProdRule prodRuleInfo = new DisclosureProdRule();
        List<DisclosureRule> listDis =  disclosureRuleDao.findDisclosureRules2(disclosureRule);
        if (listDis.size()!=0)
            prodRuleInfo = getDisclosureProdRule(listDis);
        prodRuleInfo.setProdCode(prodCode);
        prodRuleInfo.setProdFullName(disclosureRule.getProdFullName());
        //查询 该类型配置的渠道及渠道规则id结果集，进行最优比较
        List<SqlRow> channelIDByProdParamsConfig= disclosureRuleDao.ChannelIDByProdParamsConfig(disclosureRule);

         if (channelIDByProdParamsConfig.size()!=0){
            String channel_ids =(String) channelIDByProdParamsConfig.get(0).get("channel_ids");
            prodRuleInfo.setChannelIds(channel_ids);
        }
        //forCheck 仅产品信披规则添加页查询
        if (forCheck){
            return prodRuleInfo;
        }
        addDisclosureRuleEx(prodRuleInfo);
        return null;
    }
    /**
    * @功能描述:插入产品信披规则
    * @params:[prodRuleInfo]
    * @return:void
    * @Athor:ouyifan
    * @date:2022/7/14
    */
    private void addDisclosureRuleEx(DisclosureProdRule prodRuleInfo) throws Exception {
        //设置创建时间
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        prodRuleInfo.setCrtDate(date);
        prodRuleInfo.setCrtTime(time);
        //必须有对应信披规则id才可生成产品信披规则，可以没有对应的渠道id
        if (Strings.isNotBlank(prodRuleInfo.getT8DisclosureRuleId())) {
        disclosureProdRuleDao.addDisclosureProdRuleOnEnable(prodRuleInfo);
        }
    }
    /**
    * @功能描述:生成规则->产品信披规则
    * @params:[params]
    * @return:com.kayak.pms.disclosureControl.model.DisclosureProdRule
    * @Athor:ouyifan
    * @date:2022/7/14
    */
    private DisclosureProdRule getDisclosureProdRule(List<DisclosureRule> params) {
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
        DisclosureProdRule prodRule = new DisclosureProdRule();
        DisclosureRule paramsModel = params.get(0);
        //信披规则id
        prodRule.setT8DisclosureRuleId(paramsModel.getId());
        prodRule.setRuleName(paramsModel.getRuleName());
        prodRule.setStartRule(paramsModel.getStartRule());
        prodRule.setDisclosureType(paramsModel.getDisclosureType());
        prodRule.setDisclosureSonType(paramsModel.getDisclosureSonType());
        prodRule.setDisclosureModId(paramsModel.getDisclosureModId());
        prodRule.setDisclosureModVersionId(paramsModel.getDisclosureModVersionId());
        prodRule.setNoticeTitle(paramsModel.getNoticeTitle());
        prodRule.setStatus(paramsModel.getStatus());
        prodRule.setNoticeRoleid(paramsModel.getNoticeRoleid());
        prodRule.setStatus(paramsModel.getStatus());
        prodRule.setBaseDate(paramsModel.getBaseDate());
        prodRule.setExpCreateRule(paramsModel.getExpCreateRule());
        prodRule.setExpCreateDays(paramsModel.getExpCreateDays());
        prodRule.setExpCreateAttribute(paramsModel.getExpCreateAttribute());
        prodRule.setExpSupplementRule(paramsModel.getExpSupplementRule());
        prodRule.setExpSupplementDays(paramsModel.getExpSupplementDays());
        prodRule.setExpSupplementAttribute(paramsModel.getExpSupplementAttribute());
        prodRule.setExpApprovalRule(paramsModel.getExpApprovalRule());
        prodRule.setExpApprovalDays(paramsModel.getExpApprovalDays());
        prodRule.setExpApprovalAttribute(paramsModel.getExpApprovalAttribute());
        prodRule.setExpPublishRule(paramsModel.getExpPublishRule());
        prodRule.setExpPublishDays(paramsModel.getExpPublishDays());
        prodRule.setExpPublishAttribute(paramsModel.getExpPublishAttribute());
        prodRule.setNetValueDateRule(paramsModel.getNetValueDateRule());//净值披露日期规则
        prodRule.setNetValueDate(paramsModel.getNetValueDate());//净值披露基准日期
        //设置数据来源
        prodRule.setSource(RuleDataSource.auto.getItemKey());//1-自动生成,2-手工新增,3-复制新增
        prodRule.setCrtUserId(paramsModel.getCrtUserId());
        prodRule.setCrtUserName(paramsModel.getCrtUserName());
        prodRule.setRemark(paramsModel.getRemark());
        prodRule.setIfClearing(paramsModel.getIfClearing());
        prodRule.setIfCondition(paramsModel.getIfCondition());
        prodRule.setVersionNumber(paramsModel.getVersionNumber());
        return prodRule;
    }
    /**
    * @功能描述:停用信披规则信息
    * @params:[params]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/7/14
    */
    @API(desc = "停用信披规则信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateStatusOnStop(SqlParam<DisclosureRule> params) throws Exception {
        params.getModel().setStatus(XpStatus.stop.getItemKey());
        disclosureRuleDao.updateDisRuleStatus(params.getModel());
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    /**
     * 信披生成规则-导出权限控制
     * @return
     */
    @API(desc = "导出",auth = APIAuth.YES)
    public String exportDisclosureRuleRightControl() {
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }
}
