package com.kayak.pms.T85.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.dao.T8ClearGroupMemberDao;
import com.kayak.pms.T85.model.T8ClearGroupMember;
import com.kayak.pms.global.constants.BatchTaskType;

/**
 * 文件名: TaClearGroupMemberService.java
 * 描述:   清算组产品销售商配置操作
 * 创建人: zengzt
 * 创建时间:2020年5月6日下午4:57:41
 */
@Service
@APIDefine(desc = "清算组产品销售商配置操作服务", model = T8ClearGroupMember.class)
public class T8ClearGroupMemberService {

    @Autowired
    private T8ClearGroupMemberDao t8ClearGroupMemberOperDao;

    @API(desc = "查询清算组产品销售商配置信息", auth = APIAuth.NO)
    public SqlResult<T8ClearGroupMember> findTaClearGroupMembers(SqlParam<T8ClearGroupMember> params) throws Exception {

        //不自动追加条件
        params.setMakeSql(false);
        if(params.getModel().getExecTaskType()==null){
            throw new PromptException("清算执行类型为空，无法查询组成员信息");
        }
        if(params.getModel().getTaskGroup()==null){
            throw new PromptException("清算任务组为空，无法查询组成员信息");
        }

        if(BatchTaskType.PRODUCT.equals(params.getModel().getExecTaskType())){
            //产品清算类型查询产品代码
            return t8ClearGroupMemberOperDao.queryGroupMemberProds(params);
        }else if(BatchTaskType.DISTRIBUTOR_FILE_IMP.equals(params.getModel().getExecTaskType()) || BatchTaskType.DISTRIBUTOR_FILE_EXP.equals(params.getModel().getExecTaskType())){

            //销售商清算类型查询销售商代码
            return t8ClearGroupMemberOperDao.queryGroupMemberDists(params);

        }else if(BatchTaskType.ZG_FILE_IMP.equals(params.getModel().getExecTaskType())){

            //资管导入清算查询资管文件类型
            return t8ClearGroupMemberOperDao.queryGroupMemberZgImps(params);
        }else if(BatchTaskType.ZG_FILE_EXP.equals(params.getModel().getExecTaskType())){

            //资管导出清算查询资管文件类型
            return t8ClearGroupMemberOperDao.queryGroupMemberZgExps(params);
        }else{
            throw new PromptException("清算任务类型有误");
        }

    }


    @API(desc = "查询产品批次", auth = APIAuth.YES)
    public SqlResult<T8ClearGroupMember> queryTaClearGroupMember(SqlParam<T8ClearGroupMember> params) throws Exception {

        params.setMakeSql(true);
        return t8ClearGroupMemberOperDao.queryTaClearGroupMember(params);
    }

    @API(desc = "添加产品批次", auth = APIAuth.YES)
    public int addTaClearGroupMember(SqlParam<T8ClearGroupMember> params) throws Exception {

        params.setMakeSql(true);
        return t8ClearGroupMemberOperDao.insertTaClearGroupMember(params.getModel());
    }


}
