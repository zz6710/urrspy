package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.system.model.Dept;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DeptDao extends ComnDao {

    public int delete(SqlParam<Dept> params) throws Exception {
        String deptId = params.getModel().getDeptid();
        if (Tools.isBlank(deptId)) {
            return 0;
        }
        return super.update("DELETE FROM sys_dept WHERE deptid = '" + deptId + "'").getEffect();
    }

    public void update(SqlParam<Dept> params) throws Exception {
        Dept newDept = params.getModel();
        String oldDeptId = newDept.getDeptid();
        List<Dept> depts = super.findRows(Dept.class, "SELECT * FROM sys_dept WHERE deptid like '" + oldDeptId + "%'",
                0, null);
        if (CollectionUtils.isEmpty(depts)) {
            return;
        }

        // 1、判断parentno是否为自己当前的子部门；拆分父部门和子部门
        List<Dept> childDept = new ArrayList<>();
        Dept oldDept = null;
        for (Dept dept : depts) {
            if (dept.getDeptid().equals(newDept.getDeptid())) {
                oldDept = dept;
            } else {
                if (dept.getDeptno().equals(newDept.getParentdeptno())) {
                    throw new PromptException("不能部门移动到自己的子部门下");
                }
                childDept.add(dept);
            }
        }
        Dept finalCurrentDept = oldDept;
        doTrans(() -> {
            // 2、保存新的Dept
            super.update("UPDATE sys_dept SET " +
                    " parentdeptno = $S{parentdeptno}, dept_status = $S{deptStatus}, " +
                    " deptname = $S{deptname}, deptlevel = $S{deptlevel}, " +
                    " depttype = $S{depttype}, " +
                    " address = $S{address}, telno = $S{telno}, deptno = $S{deptno} " +
                    " WHERE deptid = $S{deptid}", newDept);
            if (logoChange(newDept, finalCurrentDept)) {
                // 3、计算新的deptid
                Dept newParentDept = this.get(newDept.getParentdeptno());
                String newParentDeptDeptid = newParentDept.getDeptid();
                String newDeptId = newParentDeptDeptid + newDept.getDeptno() + "_";

                // 4、子部门id及parentno替换
                for (Dept dept : childDept) {
                    String deptId = dept.getDeptid();
                    dept.setDeptid(deptId.replaceFirst(oldDeptId, newDeptId));
                    dept.setParentdeptno(newDept.getDeptno());
                    super.update("UPDATE sys_dept " +
                            " SET deptid = $S{deptid},parentdeptno = $S{parentdeptno} " +
                            " WHERE deptno = $S{deptno}", dept);
                }

                newDept.setDeptid(newDeptId);
                super.update("UPDATE sys_dept " +
                        " SET deptid = $S{deptid},parentdeptno = $S{parentdeptno} " +
                        " WHERE deptno = $S{deptno}", newDept);
            }
        });
    }

    private boolean logoChange(Dept newDept, Dept oldDept) {
        return !newDept.getParentdeptno().equals(oldDept.getParentdeptno()) ||
                !newDept.getDeptno().equals(oldDept.getDeptno());
    }

    public SqlResult<Dept> find(SqlParam<Dept> params) throws Exception {
        return super.findRows("SELECT * FROM sys_dept order by deptno", params);
    }

    public SqlResult<Dept> findChildren(SqlParam<Dept> params) throws Exception {
        String deptId = params.getModel().getDeptid();
        if (Tools.isBlank(deptId)) {
            return null;
        }
        return super.findRows("SELECT * FROM sys_dept WHERE deptid like'" + deptId + "%'", params);
    }

    public Dept get(String deptno) throws Exception {
        Map<String, Object> mapParams = new HashMap<>(1);
        mapParams.put("deptno", deptno);

        FetcherData<Dept> params = new FetcherData<>(mapParams, Dept.class);
        params.setMakeSql(true);
        SqlResult<Dept> deptSqlResult = this.find(params);
        List<Dept> rows = deptSqlResult.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return null;
        } else {
            return rows.get(0);
        }
    }

    public int add(SqlParam<Dept> params) throws Exception {
        return super.update(
                "INSERT INTO sys_dept " +
                        "(deptid, deptno, deptname, deptlevel, " +
                        "depttype, parentdeptno, address, contect, telno, dept_status) " +
                        " VALUES " +
                        "($S{deptid},$S{deptno},$S{deptname},$S{deptlevel}," +
                        "$S{depttype},$S{parentdeptno},$S{address},$S{contect},$S{telno},$S{deptStatus})",
                params.getModel()).getEffect();
    }

    public SqlResult<Dept> isDeptNo(SqlParam<Dept> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("select * from  sys_dept where deptno = $S{deptno}",params);
    }

    public SqlResult<Dept> isDeptname(SqlParam<Dept> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("select * from  sys_dept where deptname = $S{deptname}",params);
    }

    public SqlResult<Dept> isDeptChildren(SqlParam<Dept> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("SELECT t.* FROM sys_dept t where parentdeptno  = $S{deptno}",params);
    }

    public SqlResult<Dept> isUser(SqlParam<Dept> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("SELECT count(userid) count FROM sys_user t where deptno  = $S{deptno}",params);
    }

    public SqlResult<Dept> isDeptNoNotAin(SqlParam<Dept> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("select * from  sys_dept where deptno = $S{deptno} and deptid != $S{deptid}",params);
    }
    
    public SqlRow getDept(String parentDeptName) throws Exception {
      
        return super.findRow("select * from  sys_dept where deptname = $S{parentDeptName}",parentDeptName);
    }

	public SqlRow findParentDeptName(String deptNo) throws Exception  {
		
		 return super.findRow("select * from  sys_dept where deptno = '"+deptNo+"'",null);
		
	}

    public SqlResult<Dept> findDeptOnApp(SqlParam<Dept> params) throws Exception {
        return super.findRows("select deptid,deptno,deptname,parentdeptno from  sys_dept where dept_status = '1'", params);
    }
}
