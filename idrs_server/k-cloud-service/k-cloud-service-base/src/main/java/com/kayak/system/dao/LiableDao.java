package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.system.model.Liable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LiableDao extends ComnDao {

    public SqlResult<Liable> find(SqlParam<Liable> params) throws Exception {
        String sql = "SELECT l.liableid,\n" +
                " l.deptno," +
                " l.userid," +
                " u2.username as leadername,l.leadername as leaderid, " +
                " l.remarks,d.deptname,u.username FROM sys_liable l " +
                " left join sys_dept d on l.deptno = d.deptno " +
                " left join sys_user u ON u.userid = l.userid " +
                " left join sys_user u2 on u2.userid = l.leadername where 1 =1 ";
        if(StringUtils.isNotBlank(params.getModel().getDeptname())){
            sql = sql + " and d.deptname LIKE '%"+params.getModel().getDeptname()+"%'";
        }

        if(StringUtils.isNotBlank(params.getModel().getUsername())){
            sql = sql + " and u.username LIKE '%"+params.getModel().getUsername()+"%'";
        }

        if(StringUtils.isNotBlank(params.getModel().getLeadername())){
            sql = sql + " and u2.username LIKE '%"+params.getModel().getLeadername()+"%'";
        }
        if(StringUtils.isNotBlank(params.getModel().getLiableid())){
            sql = sql + " and l.liableid = '"+params.getModel().getLiableid()+"' ";
        }
        return super.findRows(sql, params);
    }


    public List<Liable>  find_l(SqlParam<Liable> params) throws Exception {
        String deptno = params.getModel().getDeptno();

        return super.findRows(Liable.class, "SELECT l.* FROM sys_liable l where deptno = '" + deptno+"'",   0, null);
    }

    public int add(SqlParam<Liable> params) throws Exception {
        return super.update(
                "INSERT INTO sys_liable(liableid,deptno,userid,leadername,remarks)  " +
                        "VALUES ($AUTOIDS{liableid},$S{deptno},$S{userid},$S{leadername},$S{remarks})",
                params.getModel()).getEffect();
    }


    public int update(SqlParam<Liable> params) throws Exception {
        return super.update(
                "UPDATE sys_liable SET userid=$S{userid},leadername=$S{leadername},remarks=$S{remarks}  WHERE liableid=$S{liableid}",
                params.getModel()).getEffect();
    }

    public int delete(SqlParam<Liable> params) throws Exception {
        String liableid = params.getModel().getLiableid();
        if (Tools.isBlank(liableid)) {
            return 0;
        }
        return super.update("DELETE FROM sys_liable WHERE  liableid = '" + liableid + "'").getEffect();
    }






}
