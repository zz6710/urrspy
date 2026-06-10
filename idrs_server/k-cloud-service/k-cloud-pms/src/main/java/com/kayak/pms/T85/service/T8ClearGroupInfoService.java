package com.kayak.pms.T85.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.kayak.core.util.Tools;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.T85.dao.T8ClearGroupDao;
import com.kayak.pms.T85.model.T8ClearGroupInfo;

/**
 * 文件名: T8ClearGroupInfoService.java
 * 描述:   清算组信息操作
 * 创建人: zengzt
 * 创建时间:2020年4月29日下午2:55:20
 */

@Service
@APIDefine(desc = "清算组信息操作服务", model = T8ClearGroupInfo.class)
public class T8ClearGroupInfoService {

    //前台m-select、checkbox多数据连接的分隔符
    public static final String webCheckBoxSpe = ",";

    @Autowired
    private T8ClearGroupDao t8ClearGroupOperDao;

    @API(desc = "查询清算任务组信息", auth = APIAuth.NO)
    public SqlResult<T8ClearGroupInfo> findT8ClearGroupInfos(SqlParam<T8ClearGroupInfo> params) throws Exception {

        params.setMakeSql(true);
        return t8ClearGroupOperDao.queryT8ClearGroupInfos(params);
    }

    @API(desc = "查询清算任务组信息（不包含某个组）", auth = APIAuth.NO)
    public SqlResult<T8ClearGroupInfo> findT8ClearGroupInfosExceptGroup(SqlParam<T8ClearGroupInfo> params) throws Exception {

        return t8ClearGroupOperDao.findT8ClearGroupInfosExceptGroup(params);
    }


    @API(desc = "新增清算任务组信息", auth = APIAuth.YES)
    public String addT8ClearGroupInfo(SqlParam<T8ClearGroupInfo> params) throws Exception {

        //插入前先递归查找前置批次组，判断是否有出现组收尾相接死循环，并登记所有前置批次信息到库里面
        params.setMakeSql(false);
        List<T8ClearGroupInfo> T8ClearGroupInfoList  = t8ClearGroupOperDao.queryT8ClearGroupInfos(params).getRows();
        //记录每个批组的前置批组信息
        Map<String, String> preGroupInfos = T8ClearGroupInfoList.stream().collect(Collectors.toMap(m -> m.getTaskGroup(), m -> m.getPreTaskGroup()==null?"":m.getPreTaskGroup()));

        Set<String> existsgroupSet = new HashSet<>();
        addCheckPreTaskGroup(null,params.getModel().getPreTaskGroup(), existsgroupSet, preGroupInfos);

        //将返回得到的全量前置批次组Set转成字符串重新设置到清算任务信息组中
        String newPreTaskGroup = existsgroupSet.toString().replaceAll("\\[|\\]| ", "");
        params.getModel().setPreTaskGroup(newPreTaskGroup);

        //插入清算组信息
        t8ClearGroupOperDao.insertT8ClearGroupInfo(params);

        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    @API(desc = "修改清算任务组信息", auth = APIAuth.YES)
    public String modifyT8ClearGroupInfo(SqlParam<T8ClearGroupInfo> params) throws Exception {

        params.setMakeSql(false);
        List<T8ClearGroupInfo> T8ClearGroupInfoList  = t8ClearGroupOperDao.queryT8ClearGroupInfos(params).getRows();
        //记录每个批组的前置批组信息
        Map<String, String> preGroupInfos = T8ClearGroupInfoList.stream().collect(Collectors.toMap(m -> m.getTaskGroup(), m -> m.getPreTaskGroup()==null?"":m.getPreTaskGroup()));

        Set<String> existsgroupSet = new HashSet<>();
        modifyCheckPreTaskGroup(params.getModel().getTaskGroup(),null,params.getModel().getPreTaskGroup(), existsgroupSet, preGroupInfos);

        //将返回得到的全量前置批次组Set转成字符串重新设置到清算任务信息组中
        String newPreTaskGroup = existsgroupSet.toString().replaceAll("\\[|\\]| ", "");
        params.getModel().setPreTaskGroup(newPreTaskGroup);

        //修改清算组信息
        t8ClearGroupOperDao.updateT8ClearGroupInfo(params);

        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }


    @API(desc = "删除清算任务组信息", auth = APIAuth.YES)
    public String deleteT8ClearGroupInfo(SqlParam<T8ClearGroupInfo> params) throws Exception {

        //如果有其他组以该组为前置组，则不允许删除该组
        SqlResult<T8ClearGroupInfo> queryIsPreTaClearGroup = t8ClearGroupOperDao.queryIsPreTaClearGroup(params);

        if(queryIsPreTaClearGroup.getRows().size()>0){
            String afterGroupStr = "";
            List<T8ClearGroupInfo> afterGroupList = queryIsPreTaClearGroup.getRows();
            for (T8ClearGroupInfo afterGroup : afterGroupList) {
                afterGroupStr = afterGroupStr + afterGroup.getTaskGroupName() + "  ";
            }
            throw new PromptException("该组是["+afterGroupStr+"]等组的前置任务组，不允许删除");
        }

        //删除清算组信息
        t8ClearGroupOperDao.deleteT8ClearGroupInfo(params);

        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    /**
     * 递归查找前置任务批次组，将任务组添加到Set中
     * @param preTaskGroupStr
     * @param existsgroupSet
     */
    private void addCheckPreTaskGroup(String currTaskGroup,String preTaskGroupStr, Set<String> existsgroupSet,Map<String, String> preGroupInfos) throws PromptException {

        if(!Tools.strIsEmpty(currTaskGroup)){
            existsgroupSet.add(currTaskGroup);
        }

        if(!Tools.strIsEmpty(preTaskGroupStr)){
            //直接存的前台传过来的前置组数据，是以逗号分隔
            String[] split = preTaskGroupStr.split(this.webCheckBoxSpe);
            for (int i = 0; i < split.length; i++) {
                addCheckPreTaskGroup(split[i],preGroupInfos.get(split[i]), existsgroupSet, preGroupInfos);
            }
        }

    }


    /**
     * 递归查找前置任务批次组，将任务组添加到Set中，并校验是否有前置任务组又依赖了本任务组构成批次死循环
     * @param preTaskGroupStr
     * @param existsgroupSet
     */
    private void modifyCheckPreTaskGroup(String oriTaskGroup,String currTaskGroup,String preTaskGroupStr, Set<String> existsgroupSet,Map<String, String> preGroupInfos) throws PromptException {

        if(!Tools.strIsEmpty(currTaskGroup)){
            existsgroupSet.add(currTaskGroup);
        }

        if(!Tools.strIsEmpty(oriTaskGroup) && !Tools.strIsEmpty(currTaskGroup) && currTaskGroup.equals(oriTaskGroup)){
            //前置组又依赖了本任务组，就出现了闭环，不允许
            throw new PromptException("依赖关系出现首尾衔接，保存失败");
        }

        //还有前置组，递归处理
        if(!Tools.strIsEmpty(preTaskGroupStr)){
            //直接存的前台传过来的前置组数据，是以逗号分隔
            String[] split = preTaskGroupStr.split(this.webCheckBoxSpe);
            for (int i = 0; i < split.length; i++) {
                modifyCheckPreTaskGroup(oriTaskGroup,split[i],preGroupInfos.get(split[i]), existsgroupSet, preGroupInfos);
            }
        }

    }

}
