package com.kayak.pms.basePublish.service;

import java.util.List;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.dao.DisclosureSourceDao;
import com.kayak.pms.basePublish.model.DisclosureSource;



@Service
@APIDefine(desc = "信披字段服务", model = DisclosureSource.class)
public class DisclosureSourceService {

    @Autowired
    private DisclosureSourceDao disclosureSourceDao;

    @API(desc = "查询信披字段信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<DisclosureSource> findDisclosureSourcesAuth(SqlParam<DisclosureSource> params) throws Exception {
        params.setMakeSql(true);
        return disclosureSourceDao.findDisclosureSources(params);
    }

    @API(desc = "添加信披字段", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addDisclosureSource(SqlParam<DisclosureSource> params) throws Exception {
        //校验字段是否重复
        try {
            Integer count = disclosureSourceDao.findDisclosureSourcesDupKey(params);
            if (count >0){
                return RequestSupport.updateReturnJson(false,"字段key已存在，请勿提交已存在字段",null).toString();
            }
            if (StringUtils.isNotEmpty(params.getModel().getDataLength())){
                if (!StringUtils.isNumeric(params.getModel().getDataLength())){
                    return RequestSupport.updateReturnJson(false,"数据长度输入有误，不允许输入非数字字符",null).toString();
                }
            }
            String date = DateUtil.getSysWordDay();
            String time = DateUtil.getNowTime();
            //获取登录用户id
            String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
            String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称
            params.getModel().setCrtDate(date); //创建日期
            params.getModel().setCrtTime(time);//创建时间
            params.getModel().setCrtUserId(userid);//创建人
            params.getModel().setCrtUserName(username);//创建人
            disclosureSourceDao.addDisclosureSource(params).getEffect();
            return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"操作失败",null).toString();
        }
    }
	
	@API(desc = "修改信披字段", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateDisclosureSource(SqlParam<DisclosureSource> params) throws Exception {
		
		String date = DateUtil.getSysWordDay();
        String time = DateUtil.getNowTime();
        //获取登录用户id
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号		
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称
        params.getModel().setUpdDate(date);
        params.getModel().setUpdTime(time);
        params.getModel().setUpdUserId(userid);
        params.getModel().setUpdUserName(username);
        return disclosureSourceDao.updateDisclosureSource(params).getEffect();
    }

    @API(desc = "删除信披字段",  auth = APIAuth.YES, operation = APIOperation.DELETE)
    public int deleteDisclosureSource(SqlParam<DisclosureSource> params) throws Exception {
        return disclosureSourceDao.deleteDisclosureSource(params).getEffect();
    }


    @API(desc = "模板扫描时查询信披字段信息", auth = APIAuth.NO)
    public List<SqlRow> findDisclosureSourcesBykeys(String keys) throws Exception {

        return disclosureSourceDao.findDisclosureSourcesBykeys(keys);
    }
}
