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
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.dao.DisclosureChannelDao;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.global.constants.IsDocking;
import com.kayak.pms.global.constants.XpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "渠道管理", model = DisclosureChannel.class)
public class DisclosureChannelService {

    @Autowired
    private DisclosureChannelDao disclosureChannelDao;

    /**
     * 功能：输入渠道名称模糊查询渠道信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "查询渠道信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureChannel> findDisclosureChannel(SqlParam<DisclosureChannel> params) throws Exception {
        params.setMakeSql(true);
        return findDisclosureChannelAuth(params);
    }
    /**
     * @功能描述:信披渠道信息查询
     * @params:[params]
     * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureChannel>
     * @Athor:ouyifan
     * @date:2022/6/20
     */
    @API(desc = "查询渠道信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureChannel> findDisclosureChannelAuth(SqlParam<DisclosureChannel> params) throws Exception {
        params.setMakeSql(true);
        return disclosureChannelDao.findDisclosureChannel(params);
    }
    /**
     * 功能：查询是否已经存在某类型的数据
     * 作者：rennannan
     * 日期：20210609
     * @param param
     * @return
     * @throws Exception
     */
    public Integer findExistsByChannelNameAndId(SqlParam<DisclosureChannel> param) throws Exception {
        return disclosureChannelDao.findExistsByChannelNameAndId(param);
    }

    /**
     * @功能描述:查询状态为启动的渠道信息
     * @params:[param]
     * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureChannel>
     * @Athor:ouyifan
     * @date:2022/6/20
     */
    @API(desc = "查询渠道信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureChannel> findDisChannel(SqlParam<DisclosureChannel> param) throws Exception {
        param.setMakeSql(true);
        return disclosureChannelDao.findDisChannel(param);
    }
    /**
     * @功能描述:查询渠道信息详情
     * @params:[param]
     * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureChannel>
     * @Athor:ouyifan
     * @date:2022/6/20
     */
    @API(desc = "查询渠道信息详情", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DisclosureChannel> findDisChannelDetail(SqlParam<DisclosureChannel> param) throws Exception {
        param.setMakeSql(true);
        return disclosureChannelDao.findDisChannel(param);
    }
    /**
     * @功能描述:新增渠道信息，新增渠道默认启动
     * @params:[param]
     * @return:java.lang.String
     * @Athor:ouyifan
     * @date:2022/6/20
     */
    @API(desc = "新增信披渠道信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String insertDisclosureChannel(SqlParam<DisclosureChannel> param) throws Exception {
        //查询是否有重复名字,有直接返回
        if (findExistsByChannelNameAndId(param) >0) {
            return RequestSupport.updateReturnJson(false, "渠道名称不可重复", null).toString();
        }
        String date = DateUtil.getSysWordDay();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称

        DisclosureChannel disclosureChannel = param.getModel();
        //新增渠道默认启用
        disclosureChannel.setStatus(XpStatus.start.getItemKey());
        changeBlankParam(disclosureChannel);

        disclosureChannel.setCrtDate(date);
        disclosureChannel.setCrtTime(time);
        disclosureChannel.setCrtUserId(userid);
        disclosureChannel.setCrtUserName(username);


        disclosureChannelDao.insertDisclosureChannel(disclosureChannel);
        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    /**
     * 根据ID修改信披渠道信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @API(desc = "修改渠道信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateDisclosureChannel(SqlParam<DisclosureChannel> param) throws Exception {
        //查询是否有重复名字,有直接返回
        if (findExistsByChannelNameAndId(param)> 0) {
            return RequestSupport.updateReturnJson(false, "渠道名称不可重复", null).toString();
        }
        try {

            String date = DateUtil.getSysWordDay();
            String time = DateUtil.getNowTime();
            String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
            String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称

            DisclosureChannel disclosureChannel = param.getModel();
            //不对接则将信息滞空
            changeBlankParam(disclosureChannel);

            disclosureChannel.setUpdDate(date);
            disclosureChannel.setUpdTime(time);
            disclosureChannel.setUpdUserId(userid);
            disclosureChannel.setUpdUserName(username);

            disclosureChannelDao.updateDisclosureChannel(disclosureChannel);
            return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "修改失败", null).toString();
        }
    }

    private void changeBlankParam(DisclosureChannel disclosureChannel) {
        if (IsDocking.no.getItemKey().equals(disclosureChannel.getIsDocking())){
            disclosureChannel.setPortCode("");
            disclosureChannel.setDockingWay("");
            disclosureChannel.setFilePath("");
            disclosureChannel.setHostIp("");
            disclosureChannel.setPassword("");
            disclosureChannel.setProtocol("");
            disclosureChannel.setUserName("");
        }
    }

    /**
     * @功能描述:删除渠道信息，并校验该渠道是否已被引用
     * @params:[param]
     * @return:java.lang.String
     * @Athor:ouyifan
     * @date:2022/6/20
     */
    @API(desc = "删除渠道信息", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String deleteDisclosureChannelById(SqlParam<DisclosureChannel> param) throws Exception {
        Integer con = disclosureChannelDao.checkChannel(param.getParams(),1);
        if (con > 0) {
            return RequestSupport.updateReturnJson(false, "该渠道在渠道配置中已被引用，无法删除", null).toString();
        }
        Integer cont = disclosureChannelDao.checkChannelInRule(param.getParams());
        if (cont > 0) {
            return RequestSupport.updateReturnJson(false, "该渠道在产品信披规则中已被引用，无法删除", null).toString();
        }
        disclosureChannelDao.deleteDisclosureChannelById(param.getModel());
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }
    /**
     * @功能描述:启用渠道
     * @params:[param]
     * @return:java.lang.String
     * @Athor:ouyifan
     * @date:2022/6/20
     */
    @API(desc = "启用渠道", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String enableChannel(SqlParam<DisclosureChannel> param) throws Exception {
        disclosureChannelDao.enableChannel(param.getModel());
        return RequestSupport.updateReturnJson(true, "启用成功", null).toString();
    }
    /**
     * @功能描述:停用渠道，停用校验->是否存在被配置
     * @params:[param]
     * @return:java.lang.String
     * @Athor:ouyifan
     * @date:2022/6/20
     */
    @API(desc = "停用渠道", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String stopChannel(SqlParam<DisclosureChannel> param) throws Exception {
        //查询是否被引用
        Integer con= disclosureChannelDao.checkChannel(param.getParams(),0);
        if (con > 0) {
            return RequestSupport.updateReturnJson(false, "该渠道已被引用且启用，无法停用", null).toString();
        }
        disclosureChannelDao.stopChannel(param.getModel());
        return RequestSupport.updateReturnJson(true, "停用成功", null).toString();
    }

    @API(desc = "通过id查询渠道信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<DisclosureChannel> findDisclosureChannelById(SqlParam<DisclosureChannel> params) throws Exception {
        return disclosureChannelDao.findDisclosureChannelById(params);
    }
}
