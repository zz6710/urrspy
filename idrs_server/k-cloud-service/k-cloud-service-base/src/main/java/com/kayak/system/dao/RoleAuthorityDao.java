package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.graphql.model.FetcherData;
import com.kayak.system.model.RoleMenu;
import com.kayak.system.model.RoleServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Repository
public class RoleAuthorityDao extends ComnDao {

    public void save(String roleId,
                     Set<RoleMenu> roleMenus,
                     Set<RoleServer> roleServers, Map<String, String> serverCollect, Map<String, String> menuCollect) throws Exception {
        doTrans(() -> {
            super.update("DELETE FROM sys_role_menu WHERE roleid = '" + roleId +  "'");
            for (RoleMenu roleMenu : roleMenus) {
                super.update("INSERT INTO sys_role_menu (menuid, moduleid, roleid) " +
                        " VALUES ($S{menuid},$S{moduleid},$S{roleid}) ", roleMenu);
                //每次循环都删除map中的权限信息
                menuCollect.remove(roleMenu.getMenuid());
            }

            super.update("DELETE FROM sys_role_server WHERE roleid = '" + roleId +  "'");
            for (RoleServer roleServer : roleServers) {
                super.update("INSERT INTO sys_role_server (roleid, server) " +
                        " VALUES ($S{roleid},$S{server}) ", roleServer);
                //每次循环都删除map中的权限信息,最后剩下的为本次取消勾选的权限,子角色需要删除父角色取消勾选的权限
                serverCollect.remove(roleServer.getServer());
            }
            //删除子角色权限
            if (!CollectionUtils.isEmpty(serverCollect) || !CollectionUtils.isEmpty(menuCollect)) {
                //获取map中的key-服务名称
                Set<String> delServers = serverCollect.keySet();
                //拼接服务名称,使用逗号分隔
                String server = String.join("','" , delServers);
                Set<String> delMenu = menuCollect.keySet();
                String menu = String.join("','", delMenu);
                //这种写法会报错,所以注释调
                // RoleServer roleServer = new RoleServer();
                //roleServer.setRoleid(roleId);
                //roleServer.setServer(server);
                //UpdateResult update = super.update("DELETE FROM sys_role_server WHERE roleid = '10' and server in ($S{server})", roleServer);
                //角色集合
                Set<String> delRoles = new HashSet<>();
                //递归查询子角色
                recursionDelAuthority(roleId, delRoles );
                //删除当前角色
                delRoles.remove(roleId);
                String roles = String.join("','", delRoles);
                //删除子角色对应的菜单权限
                super.update("DELETE FROM sys_role_menu WHERE roleid in ('" + roles + "') and menuid in ('" + menu + "')");
                //删除子角色对应的服务权限
                super.update("DELETE FROM sys_role_server WHERE roleid in ('" + roles + "') and server in ('" + server + "')");

            }
        });
    }

    /**
     * 递归查询子孙角色
     * @param roleId 父角色
     * @param delRoles 父子角色集合
     * @throws Exception 异常
     */
    public void recursionDelAuthority(String roleId, Set<String> delRoles) throws Exception {
        //往set集合添加当前角色
        delRoles.add(roleId);
        //查询当前传入的角色id是否有子角色
        List<SqlRow> rows = super.findRows("select DISTINCT roleid from sys_role where parentroleid = $S{roleId}", roleId);
        if (!CollectionUtils.isEmpty(rows)) {//当前角色存在子角色
            for (SqlRow sqlRow : rows) {//循环子角色
                //递归调用,传入每个子角色的id
                recursionDelAuthority(sqlRow.getString("roleid"), delRoles);
            }
        }
    }


    public SqlResult<RoleServer> findARoleServerAuthority(HashMap<String, Object> map) throws Exception {
        FetcherData<RoleServer> fetcherData = new FetcherData<>(map, RoleServer.class);
        return super.findRows("select roleid,server from sys_role_server where roleid = $S{roleid}", fetcherData);

    }

    public SqlResult<RoleMenu> findARoleMenuAuthority(HashMap<String, Object> map) throws Exception {
        String roleid = (String)map.get("roleid");
        FetcherData<RoleMenu> fetcherData = new FetcherData<>(map, RoleMenu.class);
        return super.findRows("select roleid,menuid from sys_role_menu where roleid = '" + roleid + "'", fetcherData);
    }
}
