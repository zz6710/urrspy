package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.constants.ServerMethodType;
import com.kayak.core.util.Tools;
import com.kayak.system.model.ServerMethod;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kayak.core.system.constants.UserConstants.SUPER_ROLE_ID;

@Repository
public class ServerMethodDao extends ComnDao {

    public SqlResult<ServerMethod> list(SqlParam<ServerMethod> params) throws Exception {
        return super.findRows("SELECT * FROM sys_server_method", params);
    }

    public ServerMethod get(String server) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("server", server);
        return super.findRow(ServerMethod.class, "SELECT * FROM sys_server_method WHERE server = $S{server}", 0, server);
    }

    public List<ServerMethod> findInModelName(List<String> modelNames) throws Exception {
        if (CollectionUtils.isEmpty(modelNames)) {
            return Collections.emptyList();
        }
        return super.findRows(ServerMethod.class,
                "SELECT * FROM sys_server_method WHERE model_name IN " + modelNames.stream().collect(Collectors.joining("','","('", "')"))+
                        " AND type = " + ServerMethodType.CHILD,
                0,
                null);
    }

    public List<ServerMethod> findByRoleId(String roleId) throws Exception {
        if (Tools.isBlank(roleId)) {
            return Collections.emptyList();
        }
        if (SUPER_ROLE_ID.equals(roleId)) {
            return super.findRows(ServerMethod.class,
                    "SELECT * FROM sys_server_method WHERE  type = " + ServerMethodType.CHILD,
                    0,
                    null);
        } else {
            return super.findRows(ServerMethod.class,
                    "SELECT m.* FROM sys_server_method m " +
                            " JOIN sys_role_server s ON s.roleid = '" + roleId + "' AND m.server = s.server " +
                            " WHERE m.type = " + ServerMethodType.CHILD,
                    0,
                    null);
        }
    }

    public List<ServerMethod> findByRoleId2(String roleId) throws Exception {
        if (Tools.isBlank(roleId)) {
            return Collections.emptyList();
        }
        if (SUPER_ROLE_ID.equals(roleId)) {
            return super.findRows(ServerMethod.class,
                    "SELECT * FROM sys_server_method WHERE need_auth=1 and type = " + ServerMethodType.CHILD,
                    0,
                    null);
        } else {
            return super.findRows(ServerMethod.class,
                    "SELECT m.* FROM sys_server_method m " +
                            " JOIN sys_role_server s ON s.roleid = '" + roleId + "' AND m.server = s.server " +
                            " WHERE m.type = " + ServerMethodType.CHILD,
                    0,
                    null);
        }
    }

}
