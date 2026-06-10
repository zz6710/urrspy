package com.kayak.system.action;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.system.dao.*;
import com.kayak.system.model.Org;
import com.kayak.system.model.SystemParam;
import com.kayak.system.model.User;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供给工作流的接口
 */
@RestController
public class ServerAction extends BaseController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private OrgDao orgDao;

    @Autowired
    private SystemParamDao systemParamDao;

    /**
     * 查询用户所属角色
     *
     * @throws Exception
     */
    @RequestMapping(value = "/base/user/findUserRoleById.json")
    public @ResponseBody String findUserRoleByIds() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        List<String> userRoleList = userRoleDao.getRoleIdsByUserId((String) params.get("userid"));
        return updateSuccess(new JSONArray(userRoleList));
    }

    /**
     * 批量查询用户信息
     *
     * @throws Exception
     */
    @RequestMapping(value = "/base/user/findUserByIds.json")
    public @ResponseBody String findUserByIds() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        List<SqlRow> userList = userDao.findRowsByIds(JSONObject.parseArray((String) params.get("userids"), String.class));
        return updateSuccess(userList);
    }

    /**
     * 批量查询角色信息
     *
     * @throws Exception
     */
    @RequestMapping(value = "/base/role/findRoleByIds.json")
    public @ResponseBody String findRoleByIds() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        List<SqlRow> roleList = roleDao.findRowsByByIds(JSONObject.parseArray((String) params.get("roleids"), String.class));
        return updateSuccess(roleList);
    }

    /**
     * 角色下拉选项
     *
     * @throws Exception
     */
    @RequestMapping(value = "/base/role/select.json")
    public @ResponseBody String findRoleList() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        SqlParam<SqlRow> sqlParam = new FetcherData<>(params, SqlRow.class);
        List<SqlRow> userList = roleDao.findRowsBySelect(sqlParam).getRows();
        return updateSuccess(userList);
    }

    /**
     * 根据角色Id获取用户
     *
     * @throws Exception
     */
    @RequestMapping(value = "/base/user/getUserByRoleIds.json")
    public @ResponseBody String getUserByRoleIds() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        List<SqlRow> userList = userDao.getUserByRoleIds(params);
        return updateSuccess(userList);
    }

    /**
     * 用户下拉选项
     *
     * @throws Exception
     */
    @RequestMapping(value = "/base/user/select.json")
    public @ResponseBody String findUserList() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        params.entrySet().removeIf(entry -> StringUtils.isEmpty((String) entry.getValue()));
        SqlParam<User> sqlParam = new FetcherData<>(params, User.class);
        sqlParam.setMakeSql(true);
        List<SqlRow> userList = userDao.findRowsBySelect(sqlParam);
        return updateSuccess(userList);
    }

    /**
     * 用户下拉选项
     *
     * @throws Exception
     */
    @RequestMapping(value = "/base/org/getLowerOrgs.json")
    public @ResponseBody String getLowerOrgs() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        SqlParam<Org> sqlParam = new FetcherData<>(params, Org.class);
        List<Org> rows = orgDao.findChildren2(sqlParam).getRows();
        Map<String, Object> resMap = new HashMap<>();
        if(rows!=null && rows.size()>0){
            resMap.put("rows",rows);
        }
        return updateSuccess(resMap);

    }

    @RequestMapping(value = "/base/org/getOrg.json")
    public @ResponseBody String getOrg() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        SqlParam<Org> sqlParam = new FetcherData<>(params, Org.class);
        List<Org> rows = orgDao.findOrg(sqlParam).getRows();
        Map<String, Object> resMap = new HashMap<>();
        if(rows!=null && rows.size()>0){
            resMap.put("rows",rows);
        }
        return updateSuccess(resMap);

    }

    @RequestMapping(value = "/base/sys/getSysParam.json")
    public @ResponseBody String getSysParam() throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        SqlParam<SystemParam> sqlParam = new FetcherData<>(params, SystemParam.class);
        List<SystemParam> rows = systemParamDao.findAllParamsByParaid(sqlParam).getRows();
        Map<String, Object> resMap = new HashMap<>();
        if(rows!=null && rows.size()>0){
            resMap.put("rows",rows);
        }
        return updateSuccess(resMap);

    }

}
