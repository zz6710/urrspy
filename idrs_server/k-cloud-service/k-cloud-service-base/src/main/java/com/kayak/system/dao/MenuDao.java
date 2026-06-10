package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kayak.core.system.constants.UserConstants.SUPER_ROLE_ID;

@Repository
public class MenuDao extends ComnDao {

    public SqlResult<Menu> find(SqlParam<Menu> params) throws Exception {
        return super.findRows("SELECT * FROM sys_menu", params);
    }

    public int add(SqlParam<Menu> params) throws Exception {
        return super.update("INSERT INTO sys_menu (moduleid, menuid, menuname, shortname, model, " +
                        "upperid, url, iconcls, loadorder, icon, status, remark, auth_server) VALUES " +
                        "($S{moduleid}, $S{menuid}, $S{menuname}, $S{shortname}, $S{model}, $S{upperid}, $S{url}, " +
                        "$S{iconcls}, $S{loadorder}, $S{icon}, $S{status}, $S{remark}, $S{authServer})",
                params.getModel()).getEffect();
    }

    public int update(SqlParam<Menu> params) throws Exception {
        return super.update("UPDATE sys_menu SET menuname=$S{menuname}, shortname=$S{shortname}, " +
                        "model=$S{model}, upperid=$S{upperid}, url=$S{url}, iconcls= $S{iconcls}, " +
                        "loadorder= $S{loadorder}, icon=$S{icon}, status=$S{status}, remark= $S{remark}, " +
                        "auth_server= $S{authServer} WHERE moduleid=$S{moduleid} AND menuid=$S{menuid}",
                params.getModel()).getEffect();
    }

    public void delete(SqlParam<Menu> params) throws Exception {
        super.doTrans(() -> {
            super.update("DELETE FROM sys_menu WHERE moduleid=$S{moduleid} AND upperid=$S{menuid}", params.getModel());
            super.update("DELETE FROM sys_menu WHERE moduleid=$S{moduleid} AND menuid=$S{menuid}", params.getModel());
        });
    }

    public List<Menu> findByRoleId(String roleId) throws Exception {
        if (SUPER_ROLE_ID.equals(roleId)) {
            return super.findRows(Menu.class, "SELECT * FROM sys_menu WHERE status = 'N' ORDER BY loadorder", 0, null);
        } else {
            return super.findRows(Menu.class, "SELECT m.* FROM sys_menu m " +
                    " JOIN sys_role_menu rm ON m.menuid = rm.menuid " +
                    " WHERE rm.roleid = '" + roleId + "' AND m.status = 'N' ORDER BY loadorder", 0, null);
        }
    }
}
