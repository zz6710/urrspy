package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.system.dao.DeptDao;
import com.kayak.system.model.Dept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Service
@Slf4j
@APIDefine(desc = "部门服务", model = Dept.class)
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DeptService {

    @Autowired
    private DeptDao deptDao;

    @API(desc = "删除部门", auth = APIAuth.NO, operation = APIOperation.DELETE)
    public String delete(SqlParam<Dept> params) throws Exception {
        if (deptDao.isDeptChildren(params).getRows().size() > 0) {
            return RequestSupport.updateReturnJson(false, "删除失败，当前部门存在子部门, 需先删除子部门", null).toString();
        }

        if (deptDao.isUser(params).getRows().get(0).getCount() > 0) {
            return RequestSupport.updateReturnJson(false, "删除失败，当前部门存在已存在用户", null).toString();
        }

        deptDao.delete(params);
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    @API(desc = "修改部门", auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public String update(SqlParam<Dept> params) throws Exception {
        Dept model = params.getModel();
        if (Tools.isBlank(model.getParentdeptno())) {
            return RequestSupport.updateReturnJson(false, "修改失败, 父部门参数错误", null).toString();
        }
        if (model.getParentdeptno().equals(model.getDeptno())) {
            return RequestSupport.updateReturnJson(false, "修改失败, 父部门不能是自己", null).toString();
        }
        if (deptDao.isDeptNoNotAin(params).getRows().size() > 0) {
            return RequestSupport.updateReturnJson(false, "修改失败, 部门代码已存在", null).toString();
        }

        deptDao.update(params);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

    @API(desc = "查询部门",auth = APIAuth.YES,operation = APIOperation.SELECT)
    public SqlResult<Dept> find1(SqlParam<Dept> params) throws Exception {
    
        return find(params);
    }

    @API(desc = "查询所有部门(移动审批使用)",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Dept> findDeptOnApp(SqlParam<Dept> params) throws Exception {
        return deptDao.findDeptOnApp(params);
    }

    @API(desc = "查询部门",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Dept> find(SqlParam<Dept> params) throws Exception {
        params.setMakeSql(true);
        SqlResult<Dept> deptSqlResult = deptDao.find(params);
        List<Dept> rows =  deptSqlResult.getRows();
        rows.forEach(dept->{
        	if(dept.getParentdeptno().length()>0) {
        		SqlRow sqlRow;
				try {
					sqlRow = deptDao.findParentDeptName(dept.getParentdeptno());
					if(sqlRow!=null)
						dept.setParentDeptName(sqlRow.getString("deptname"));
	        		
				} catch (Exception e) {
					log.error("查询dept异常");
					e.printStackTrace();
				}
        		
        	}
        });
      
        deptSqlResult.setRows(rows);
        return deptSqlResult;
    }

    @API(desc = "查询子部门",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Dept> findChildren(SqlParam<Dept> params) throws Exception {
        Dept dept = deptDao.get(params.getModel().getDeptno());
        params.getModel().setDeptid(dept.getDeptid());
        return deptDao.findChildren(params);
    }

    @API(desc = "添加部门",auth = APIAuth.YES,operation = APIOperation.INSTER)
    public String add(SqlParam<Dept> params) throws Exception {
        Dept model = params.getModel();
        // 部门级别暂时无用 应要求暂时隐藏赋予默认值
        model.setDeptlevel("1");

        if (deptDao.isDeptNo(params).getRows().size() > 0) {
            return RequestSupport.updateReturnJson(false, "添加失败, 部门代码已存在", null).toString();
        }
        if (deptDao.isDeptname(params).getRows().size() > 0) {
            return RequestSupport.updateReturnJson(false, "添加失败, 部门名称已存在", null).toString();
        }
        //顶级部门
        if (Tools.isBlank(model.getParentdeptno())) {
            model.setParentdeptno("ROOT");
            model.setDeptid(model.getDeptno());
            if (deptDao.add(params) < 1) {
                throw new PromptException("添加失败");
            }
            return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
        }
        if (Tools.isBlank(model.getDeptid())) {
            Dept parentDept = this.get(model.getParentdeptno());
            model.setDeptid(parentDept.getDeptid() + model.getDeptno() + "_");
        }
        if (deptDao.add(params) < 1) {
            throw new PromptException("添加失败");
        }
        return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
    }

    @API(desc = "是否已存在部门代码",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Dept> isExistDeptNoNotAin(SqlParam<Dept> params) throws Exception {
        return deptDao.isDeptNo(params);
    }

    @API(desc = "是否已存在部门名称",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Dept> isExistDeptnameNotAin(SqlParam<Dept> params) throws Exception {
        return deptDao.isDeptname(params);
    }
    @API(desc = "获取部门信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    private Dept get(String deptNo) throws Exception {
        Map<String, Object> mapParams = new HashMap<>(1);
        mapParams.put("deptno", deptNo);
        FetcherData<Dept> params = new FetcherData<>(mapParams, Dept.class);
        params.setMakeSql(true);
        SqlResult<Dept> deptSqlResult = deptDao.find(params);
        List<Dept> rows = deptSqlResult.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return null;
        }
        return rows.get(0);
    }




}
