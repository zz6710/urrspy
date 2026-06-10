package com.kayak.pms.indexInfo.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.indexInfo.model.DesktopIndex;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DesktopIndexDao extends ComnDao {

    //任务  axin
    public List<SqlRow> findDesktopIndexList() throws Exception {
        return super.findRows("select id,task_desc,sql_str,content,is_top,top_count,url from sys_home_task t where t.is_top = 0 order by t.top_count desc");
    }

    //提醒任务  axin
    public List<SqlRow> findHomeTaskList(String sql) throws Exception {
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
        sql = "select DISTINCT t.* from (" + sql;
        sql += ") t " +
                "left join t8_prod_user t2 " +
                "on t.t8_prod_info_id = t2.t8_prod_info_id " +
                "where t2.userid_b = '"+userid+"' " +
                "or 'admin' = '"+userid+"' " +
                "or t2.userid_a = '"+userid+"' ";
        return super.findRows(sql);
    }

    //查询可设置的常用菜单
    public SqlResult<DesktopIndex> findCommonlyUsedMenu(SqlParam<DesktopIndex> param) throws Exception {
        DesktopIndex desktopIndex =param.getModel();
        desktopIndex.setUserid(SysUtil.getLoginUserid());//获取当前用户
        String sql = "select m.menuid,m.menuname from sys_menu m where m.url != ''  and m.status = 'N' " +
                " and m.menuid not in (select c.menuid from sys_home_used_menu c  where c.userid=$S{userid} and c.isopen = '0')" +
                " and m.menuid not in (select c.menuid from sys_home_used_menu c  where c.isopen = '1') ";
        //"select m.menuid,m.menuname from sys_menu m where m.url != '' " +
        //                " and m.status = 'N' and m.menuid not in(select c.menuid from sys_home_used_menu c " +
        //                " where c.userid=$S{userid})"
        return super.findRows(sql,param);
    }
    //查询可设置的常用菜单
    public SqlResult<DesktopIndex> findCommonlyUsedMenuByuser(SqlParam<DesktopIndex> param) throws Exception {
        DesktopIndex desktopIndex =param.getModel();
        desktopIndex.setUserid(SysUtil.getLoginUserid());//获取当前用户
        String sql = "select t.* from ( " +
                "select c.id,c.menuid,c.isOpen,m.menuname,m.url from sys_home_used_menu c " +
                " left join sys_menu m on m.menuid=c.menuid where c.userid=$S{userid} and c.isopen = '0' " +
                " union " +
                " select d.id,d.menuid,d.isOpen,n.menuname,n.url from sys_home_used_menu d " +
                " left join sys_menu n on n.menuid=d.menuid where d.isopen = '1' " +
                ") t order by t.id+0 ";
        //"select c.id,c.menuid,m.menuname,m.url from sys_home_used_menu c
        // left join sys_menu m on m.menuid=c.menuid where c.userid=$S{userid} order by c.id "
        return super.findRows(sql, DataSourceProperty.PUB,param);
    }
    //保存用户常用菜单
    public UpdateResult saveCommonlyUsedMenu(SqlParam<DesktopIndex> param) throws Exception {
        DesktopIndex desktopIndex =param.getModel();
        desktopIndex.setUserid(SysUtil.getLoginUserid());//获取当前用户
        return super.update("insert into sys_home_used_menu(id,userid,menuid,isopen) " +
                "values($AUTOIDS{sys_home_used_menu},$S{userid},$S{menuid},'0')",param.getModel());
    }

    //删除用户常用菜单
    public UpdateResult delCommonlyUsedMenu(SqlParam<DesktopIndex> param) throws Exception {
        return super.update("delete from sys_home_used_menu where id=$S{id} and isopen = '0'",param.getModel());
    }

}
