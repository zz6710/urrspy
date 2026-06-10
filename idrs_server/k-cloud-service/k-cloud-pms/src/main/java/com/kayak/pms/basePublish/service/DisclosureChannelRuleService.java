package com.kayak.pms.basePublish.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.dao.DisclosureChannelDao;
import com.kayak.pms.basePublish.dao.DisclosureChannelRuleDao;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.basePublish.model.DisclosureChannelRule;
import com.kayak.pms.email.dao.T8DisChannelInfoDao;
import com.kayak.pms.global.constants.XpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

    /**
     * com.kayak.pms.basePublish.service
     * user:rennannan
     * date:2021/5/11 15:56
     * function:
     */
@Service
@APIDefine(desc = "信披渠道规则服务", model = DisclosureChannelRule.class)
public class DisclosureChannelRuleService {
    @Autowired
    private DisclosureChannelRuleDao disclosureChannelRuleDao;
    @Autowired
    private T8DisChannelInfoDao t8DisChannelInfoDao;
    @Autowired
    private DisclosureChannelDao disclosureChannelDao;


        /**
         * 查询文件后缀名
         * @param param
         * @return
         * @throws Exception
         */
        @API(desc = "查询文件后缀名", auth = APIAuth.YES, operation = APIOperation.SELECT)
        public SqlResult<DisclosureChannelRule> findSuffixFileName(SqlParam<DisclosureChannelRule> param) throws Exception {
//        param.setMakeSql(true);
            return disclosureChannelRuleDao.findSuffixFileName(param);
        }
    /**
     * 功能：查询信披渠道规则列表
     * 作者：rennannan
     * 日期：20210511
     *
     * @param param
     * @return
     * @throws Exception
     */
    @API(desc = "查询信披渠道规则信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<DisclosureChannelRule> findChannelRuleAuth(SqlParam<DisclosureChannelRule> param) throws Exception {
//        param.setMakeSql(true);
        return disclosureChannelRuleDao.findChannelRule(param);
    }
    /**
    * @功能描述:新增渠道配置信息，校验是否存在重复渠道规则名称，默认启动
    * @params:[param]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    @API(desc = "新增信披渠道规则信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String insertChannelRule(SqlParam<DisclosureChannelRule> param) throws Exception {
        try {
            if (disclosureChannelRuleDao.findExistsByRuleName(param)>0){
                return RequestSupport.updateReturnJson(false, "渠道规则名称重复，请确认", null).toString();
            }
            String date = DateUtil.getSysWordDay();
            String time = DateUtil.getNowTime();
            String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
            String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
            param.getModel().setCrtDate(date);
            param.getModel().setCrtTime(time);
            param.getModel().setCrtUserId(userid);
            param.getModel().setCrtUserName(username);
            //默认启动
            param.getModel().setStatus(XpStatus.start.getItemKey());
            //checkName有值则渠道重复绑定
            String checkName = selectDisclosureTypeHasChannels(param);

            if (!"".equals(checkName)&&checkName!=null) {
                return RequestSupport.updateReturnJson(false, "该信披类型的"+checkName+"渠道已被维护", null).toString();
            }
            disclosureChannelRuleDao.insertChannelRule(param.getModel());
            return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "操作失败", null).toString();
        }
    }

    /**
     * 功能：根据id修改信披渠道规则信息
     * 作者：rennannan
     * 日期：20210511
     *
     * @param
     * @return
     * @throws Exception
     */
    @API(desc = "修改信披渠道规则信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateChannelRule(SqlParam<DisclosureChannelRule> param) throws Exception {
        if (disclosureChannelRuleDao.findExistsByRuleNameForUpdate(param)>0){
            return RequestSupport.updateReturnJson(false, "渠道规则名称重复，请确认", null).toString();
        }
        String date = DateUtil.getSysWordDay();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
        param.getModel().setUpdDate(date);
        param.getModel().setUpdTime(time);
        param.getModel().setUpdUserId(userid);
        param.getModel().setUpdUserName(username);
        //checkName有值则渠道重复绑定
        String checkName = selectDisclosureTypeHasChannels(param);

        if (!"".equals(checkName)&&checkName!=null) {
            return RequestSupport.updateReturnJson(false, "该信披类型的"+checkName+"渠道已被维护", null).toString();
        }
        disclosureChannelRuleDao.updateChannelRule(param.getModel());
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    /**
     * 功能：启用渠道规则信息
     * 作者：rennannan
     * 日期：20210512
     *
     * @param param
     * @return
     * @throws Exception
     */
    @API(desc = "启用渠道规则信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateStatusOnEnable(SqlParam<DisclosureChannelRule> param) throws Exception {
        param.getModel().setStatus("1");
        disclosureChannelRuleDao.updateChannelStatus(param.getModel());
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    /**
     * 功能：停用渠道规则信息
     * 作者：rennannan
     * 日期：20210512
     *
     * @param param
     * @return
     * @throws Exception
     */
    @API(desc = "停用渠道规则信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateStatusOnStop(SqlParam<DisclosureChannelRule> param) throws Exception {
        param.getModel().setStatus("0");
        disclosureChannelRuleDao.updateChannelStatus(param.getModel());
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    /**
     * 功能：删除信披渠道规则信息
     * 作者：rennannan
     * 日期：20210511
     *
     * @param
     * @return
     * @throws Exception
     */
    @API(desc = "删除信披渠道规则信息", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String deleteChannelRule(SqlParam<DisclosureChannelRule> param) throws Exception {
        int i = disclosureChannelRuleDao.deleteChannelRule(param.getModel());
        Boolean result = i > 0;
        return RequestSupport.updateReturnJson(result, result ? "操作成功" : "操作失败", null).toString();
    }
    /**
    * @功能描述:校验新增或修改数据是否存在，存在则返回渠道名称
    * @params:[param]
    * @return:java.lang.String
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    private String selectDisclosureTypeHasChannels(SqlParam<DisclosureChannelRule> param) throws Exception {
        DisclosureChannelRule disclosureChannelRule = param.getModel();
        String[] channelIds = disclosureChannelRule.getChannelIds().split(",");
        DisclosureChannelRule checkRule = new DisclosureChannelRule();
        checkRule.setDisclosureType(disclosureChannelRule.getDisclosureType());
        checkRule.setDisclosureSonType(disclosureChannelRule.getDisclosureSonType());
        checkRule.setProdForm(disclosureChannelRule.getProdForm());
        checkRule.setProdInvTyp(disclosureChannelRule.getProdInvTyp());
        checkRule.setProdObj(disclosureChannelRule.getProdObj());
        checkRule.setProdClcMth(disclosureChannelRule.getProdClcMth());
        checkRule.setProdSerCd(disclosureChannelRule.getProdSerCd());
        checkRule.setId(disclosureChannelRule.getId());
        int i = 0;
        for (String channelId : channelIds) {
            checkRule.setChannelIds(channelId);
            SqlRow sqlRow= disclosureChannelRuleDao.selectDisclosureTypeHasChannels(checkRule);
            i += Integer.parseInt(sqlRow.get("con").toString());
            if (i>0){
                DisclosureChannel disclosureChannel = new DisclosureChannel();
                disclosureChannel.setId(channelId);
                //已存在则获取该渠道名称
                String channelName = disclosureChannelDao.findDuplicateDisChannelName(disclosureChannel);
                return channelName;
            }
        }
        return null;
    }
}
