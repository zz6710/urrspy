package com.kayak.pms.printTemp.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.utils.DateHelper;
import com.kayak.pms.printTemp.dao.StaticTempDao;
import com.kayak.pms.printTemp.dao.StaticTempVersionDao;
import com.kayak.pms.printTemp.model.StaticTemp;
import com.kayak.pms.printTemp.model.StaticTempVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.kayak.base.dao.util.DaoUtil.doTrans;

/**
 * @program: k-cloud
 * @description: 静态模板服务
 * @author: WangZhenXin
 * @create: 2021-01-02 10:12
 * @memo 备注信息
 */
@Service
                @APIDefine(desc = "静态模板服务",model = StaticTemp.class)
public class StaticTempService {
    private static final Logger logger = LoggerFactory.getLogger(StaticTempService.class);

    @Autowired
    private StaticTempDao staticTempDao;

    @Autowired
    private StaticTempVersionDao staticTempVersionDao;


    @API(desc = "查询静态模板",auth = APIAuth.YES,operation = APIOperation.SELECT)
    public SqlResult<StaticTemp> getStaticTempList1(SqlParam<StaticTemp> param) throws Exception {
        return getStaticTempList(param);
    }

    @API(desc = "查询静态模板",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<StaticTemp> getStaticTempList(SqlParam<StaticTemp> param) throws Exception {
        param.setMakeSql(true);
        return staticTempDao.find(param);
    }

    @API(desc = "更新版本",auth = APIAuth.YES,operation = APIOperation.INSTER)
    public void saveStaticTempVersion(SqlParam<StaticTemp> param){
     //此方法只作用于权限控制
    }


    @API(desc = "校验文档模板基础信息是否存在",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public Integer checkStaticTemp(StaticTemp staticTemp){
        return staticTempDao.checkStaticTemp(staticTemp);
    }

    @API(desc = "上传静态文档",auth = APIAuth.YES,operation = APIOperation.INSTER)
    public void saveStaticTempAndStaticTempVersion(StaticTemp staticTemp, StaticTempVersion staticTempVersion) throws Exception {
        //保存文档模板基础信息
        doTrans(()->{
            //设置创建时间
            String currentDate = DateHelper.getCurrentDate();
            String currentTime = DateHelper.getCurrentTime();
            String userId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
            String userName = (String) SysUtil.getSysUserParamValue("sys_user_username");

            staticTemp.setCreateDate(currentDate);
            staticTemp.setCreateTime(currentTime);
            staticTemp.setCreateUserId(userId);
            staticTemp.setCreateUserName(userName);
            //保存文档模板
            String staticTempId = staticTempDao.saveStaticTemp(staticTemp);
            staticTempVersion.setT8StaticTempId(staticTempId);
            staticTempVersion.setCreateDate(currentDate);
            staticTempVersion.setCreateTime(currentTime);
            staticTempVersion.setCreateUserId(userId);
            staticTempVersion.setCreateUserName(userName);
            //保存文档模板
            staticTempVersionDao.saveStaticTempVersion(staticTempVersion);
        });
    }

    @API(desc = "根据id获取静态文档信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public StaticTemp getStaticTempByTempId(String staticTempId) throws Exception {
        return staticTempDao.getStaticTempByTempId(staticTempId);
    }


}
