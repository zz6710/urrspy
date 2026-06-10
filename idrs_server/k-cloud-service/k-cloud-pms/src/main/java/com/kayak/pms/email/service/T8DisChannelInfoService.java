package com.kayak.pms.email.service;

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
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.email.dao.T8DisChannelInfoDao;
import com.kayak.pms.email.model.T8DisChannelInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@APIDefine(desc = "渠道信息表服务", model = T8DisChannelInfo.class)
public class T8DisChannelInfoService {

    private static Logger logger = LoggerFactory.getLogger(T8DisChannelInfoService.class);

    @Autowired
    private T8DisChannelInfoDao t8DisChannelInfoDao;


    /*页面信批渠道多选框查询*/
//    @API(desc = "根据查询条件查询渠道信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
//    public SqlResult<T8DisChannelInfo> findDisChannel(SqlParam<T8DisChannelInfo> param) throws Exception {
//        param.setMakeSql(true);
//        return t8DisChannelInfoDao.findDisChannel(param);
//    }

    /**
     * 功能：根据id查询渠道信息
     * 作者：rennannan
     * 日期：20210524
     *
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<T8DisChannelInfo> findDisChannelsByIds(SqlParam<T8DisChannelInfo> param) throws Exception {
        return t8DisChannelInfoDao.findDisChannelsByIds(param);
    }

    @API(desc = "查询信披公共邮箱信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8DisChannelInfo> findT8DisChannelInfoAllAuth(SqlParam<T8DisChannelInfo> params) throws Exception {
        return findT8DisChannelInfoAll(params);
    }

    @API(desc = "查询信披公共邮箱信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8DisChannelInfo> findT8DisChannelInfoAll(SqlParam<T8DisChannelInfo> params) throws Exception {
        params.setMakeSql(true);
        return t8DisChannelInfoDao.findT8DisChannelInfoAll(params);
    }

    @API(desc = "停用", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String stopStatus(SqlParam<T8DisChannelInfo> params) throws Exception {
        boolean result = t8DisChannelInfoDao.stopStatus(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "停用成功" : "停用失败", null).toString();
    }

    @API(desc = "启用", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String recoverStatus(SqlParam<T8DisChannelInfo> params) throws Exception {
        boolean result = t8DisChannelInfoDao.recoverStatus(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "启用成功" : "启用失败", null).toString();
    }

    @API(desc = "删除字段", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public int deleteRowDateById(SqlParam<T8DisChannelInfo> params) throws Exception {
        return t8DisChannelInfoDao.deleteRowDateById(params).getEffect();
    }


    @API(desc = "添加信披邮箱数据", auth = APIAuth.NO, operation = APIOperation.INSTER)
    public String t8DisChannelInfoAdd(SqlParam<T8DisChannelInfo> params) throws Exception {
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
        String updUserName = (String) SysUtil.getSysUserParamValue("sys_user_username");
        Date now = new Date();
        String msg = "保存成功";
        boolean flag = true;
        try {
            if (params.getModel().getChannelType().equals("0")) {
                int count = t8DisChannelInfoDao.findT8DisChannelInfoCount(Tools.makeParams().put("channelType", params.getModel().getChannelType()).build());
                if (count > 0) {
                    flag = true;
                    msg = "发件人邮箱已存在";
                } else {
                    //校验密码
                    params.getModel().setCrtDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
                    params.getModel().setCrtTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));
                    params.getModel().setCrtUserId(userid);
                    params.getModel().setCrtUserName(updUserName);
                    t8DisChannelInfoDao.t8DisChannelInfoAdd(params.getModel()).getEffect();
                }
            } else {
                t8DisChannelInfoDao.t8DisChannelInfoAdd(params.getModel()).getEffect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msg = "保存失败";
            flag = false;
        }
        return RequestSupport.updateReturnJson(flag, msg, null).toString();
    }

    @API(desc = "修改信披邮箱数据", auth = APIAuth.NO, operation = APIOperation.INSTER)
    public String t8DisChannelInfoEdit(SqlParam<T8DisChannelInfo> params) throws Exception {
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
        String updUserName = (String) SysUtil.getSysUserParamValue("sys_user_username");
        Date now = new Date();
        String msg = "保存成功";
        boolean flag = true;
        try {
            params.getModel().setUpdDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
            params.getModel().setUpdTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));
            params.getModel().setUpdUserId(userid);
            params.getModel().setUpdUserName(updUserName);
            t8DisChannelInfoDao.t8DisChannelInfoUpdate(params.getModel()).getEffect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }
}
