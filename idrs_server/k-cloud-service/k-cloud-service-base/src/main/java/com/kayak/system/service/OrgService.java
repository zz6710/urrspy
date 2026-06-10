package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.system.dao.OrgDao;
import com.kayak.system.model.Org;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@APIDefine(desc = "机构服务", model = Org.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrgService {

    private final OrgDao orgDao;

    @API(desc = "删除机构信息", params = "dict", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String delete(SqlParam<Org> params) throws Exception {
        Object obj = orgDao.findChildren(params).getRows();
        if(orgDao.checkIsFather(params).getRows().size() > 0){
            return RequestSupport.updateReturnJson(false, "删除失败，请先删除子机构", null).toString();
        }
        if( orgDao.delete(params) > 0){
            return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
        }else{
            return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
        }
    }

    @API(desc = "修改机构信息", params = "dict", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String update(SqlParam<Org> params) throws Exception {
        //判断是否修改总行，添加总行需校验
        if( "100000".equals(params.getModel().getOrglevel())){//1为修改为总行
            List<Org> orgLists =  orgDao.queryOrgWL(params).getRows();
            if(!orgLists.get(0).getOrgid().equals(params.getModel().getOrgid())){
                return RequestSupport.updateReturnJson(false, "修改失败，总行已经存在", null).toString();
            }
        }
        List<Org> orgLists =  orgDao.isBlankOrglevel(params).getRows();
        if(orgLists.size()>0  && !orgLists.get(0).getOrgid().equals(params.getModel().getOrgid())){
            return RequestSupport.updateReturnJson(false, "修改失败，机构代码已存在", null).toString();
        }
        orgDao.update(params);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

    @API(desc = "查询机构信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<Org> find1(SqlParam<Org> params) throws Exception {
        return find(params);
    }

    @API(desc = "查询机构信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Org> find(SqlParam<Org> params) throws Exception {
        params.setMakeSql(true);
        return orgDao.find(params);
    }
    @API(desc = "查询机构", auth = APIAuth.NO)
    public SqlResult<Org> findWL(SqlParam<Org> params) throws Exception {
        params.setMakeSql(true);
        return orgDao.findWL(params);
    }


    @API(desc = "查询子机构", auth = APIAuth.NO)
    public SqlResult<Org> findChildren(SqlParam<Org> params) throws Exception {
        Org org = orgDao.get(params.getModel().getOrgno());
        params.getModel().setOrgid(org.getOrgid());
        return orgDao.findChildren(params);
    }

    @API(desc = "新增机构信息", auth = APIAuth.YES,operation = APIOperation.INSTER)
    public String add(SqlParam<Org> params) throws Exception {
        //判断是否添加总行，添加总行需校验
        if ( "100000".equals(params.getModel().getOrglevel())&& orgDao.queryOrgWL(params).getRows().size()>0) {
            return RequestSupport.updateReturnJson(false, "添加失败,总行已经存在", null).toString();
        }
        List<Org> orgLists =  orgDao.isBlankOrglevel(params).getRows();
        if(orgLists.size()>0 ){
            return RequestSupport.updateReturnJson(false, "修改失败，机构代码已存在", null).toString();
        }
        Org model = params.getModel();
        if (Tools.isBlank(model.getOrgid())) {
            Org parentOrg = this.get(model.getParentorgno());
            if("100000".equals(params.getModel().getOrglevel())){
                model.setParentorgno("ROOT");
                model.setOrgid( model.getOrgno() + "_");
            }else{
                model.setOrgid(parentOrg.getOrgid() + model.getOrgno() + "_");
            }
        }
        if(orgDao.add(params) < 1) {
            throw new PromptException("添加失败");
        }
        return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
    }

    @API(desc="检查唯一性", auth = APIAuth.NO)
    public SqlResult<Org> checkOrgno(SqlParam<Org> params) throws Exception {
        params.setMakeSql(true);
        return orgDao.isBlankOrglevel(params);
    }

    @API(desc="一般用来检查总行唯一", auth = APIAuth.NO)
    public SqlResult<Org> queryOrgWL(SqlParam<Org> params) throws  Exception{
        params.setMakeSql(false);
        return orgDao.queryOrgWL(params);
    }

    private Org get(String orgNo) throws Exception {
        Map<String, Object> mapParams = new HashMap<>(1);
        mapParams.put("orgno", orgNo);
        FetcherData<Org> params = new FetcherData<>(mapParams, Org.class);
        params.setMakeSql(true);
        SqlResult<Org> orgSqlResult = orgDao.find(params);
        List<Org> rows = orgSqlResult.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return null;
        }
        return rows.get(0);
    }



}
