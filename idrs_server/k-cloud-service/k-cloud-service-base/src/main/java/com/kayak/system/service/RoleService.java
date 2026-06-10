package com.kayak.system.service;

import com.google.common.collect.Sets;
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
import com.kayak.system.dao.RoleDao;
import com.kayak.system.model.Role;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yinwanxiong
 * @date 2020/4/9 16:26
 * @description
 */

@Service
@APIDefine(desc = "角色服务", model = Role.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleService {

    private final RoleDao roleDao;


    @API(desc = "查询子角色信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Role> findRoleList(SqlParam<Role> params) throws Exception {
        return roleDao.findRoleList(params);
    }

    @API(desc = "查询子角色信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<Role> findChildren1(SqlParam<Role> params) throws Exception {
       SqlResult<Role> sqlResult = roleDao.findRoleList(params);
       List<Role> roles = sqlResult.getRows();
       roles.parallelStream().forEach(t->{
    	   try {
			Role role = roleDao.findRoleByRoleId(t.getParentroleid());
			if(role!=null)
				t.setParentRoleName(role.getRolename());	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
       });
       sqlResult.setRows(roles);
       return sqlResult;
    }
    @API(desc = "查询父节点", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Role> findParents(SqlParam<Role> params) throws Exception {
    	
       return roleDao.findParents(params);
    }
    @API(desc = "查询子角色信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Role> findChildren(SqlParam<Role> params) throws Exception {
        String roleIds = params.getModel().getRoleids();
        String[] roles = roleIds.split(",");
        if (roles.length < 1) {
            return null;
        }
        Set<Role> roleSet = buildChildRoles(roles);

        SqlResult<Role> roleSqlResult = new SqlResult<>();
        roleSqlResult.setResults(roleSet.size());
        List<Role> roleList = filterList(params.getModel(), new ArrayList<>(roleSet));

        // 因为是内存分页，所以需要排序，保证翻页数据的准确性
        Collections.sort(roleList);
        if (params.getLimit() != 0) {
            roleSqlResult.setRows(Tools.getSubList(params.getStart(), params.getLimit(), roleList));
        } else {
            roleSqlResult.setRows(roleList);
        }
        return roleSqlResult;
    }

    /**
     * 根据parentRoleId数组构建子Role集合（不包括传入的role）
     * @param roles
     * @throws Exception
     */
    private Set<Role> buildChildRoles(String[] roles) throws Exception {
        if (ArrayUtils.isEmpty(roles)) {
            return Collections.emptySet();
        }

        List<Role> allRole = roleDao.findAll();
        if (CollectionUtils.isEmpty(allRole)) {
            return Collections.emptySet();
        }

        Map<String, Set<Role>> allRoleMap =
                allRole.stream().collect(Collectors.toMap(Role::getParentroleid, r -> Sets.newHashSet(r),
                        (Set<Role> newValueList, Set<Role> oldValueList)->
                        {
                            oldValueList.addAll(newValueList);
                            return oldValueList;
                        }
                        )
                );
        Set<Role> roleSet = new HashSet<>();
        for (String roleId : roles) {
            buildChildrenRoles(allRoleMap, roleSet, roleId);
        }

        return roleSet;
    }

    /**
     * 递归构建传入roleId的子Role集合
     * @param allRoleMap key:parentId  value:childRoles
     * @param roles 最终结果集
     * @param roleId
     */
    private void buildChildrenRoles(Map<String, Set<Role>> allRoleMap, Set<Role> roles, String roleId) {
        Set<Role> childRoles = allRoleMap.get(roleId);
        if (CollectionUtils.isEmpty(childRoles)) {
            return;
        }
        for (Role childRole : childRoles) {
            roles.add(childRole);
            buildChildrenRoles(allRoleMap, roles, childRole.getRoleid());
        }
    }

    private List<Role> filterList(Role condition, List<Role> target) {
        if (condition == null || CollectionUtils.isEmpty(target)) {
            return target;
        }

        return target.stream().filter(t -> {
            boolean result = true;

            String roleName = condition.getRolename();
            String descript = condition.getDescript();
            if (Tools.isNotBlank(roleName)) {
                if (Tools.isNotBlank(descript)) {
                    result =  t.getRolename().contains(roleName);
                    if(result){
                        result =  t.getDescript().contains(descript);
                    }else{
                        result = false;
                    }
                }else {
                    result =  t.getRolename().contains(roleName);
                }
            }else if(Tools.isNotBlank(descript)){
                result =  t.getDescript().contains(descript);
            }else{

            }

            return result;
        }).collect(Collectors.toList());
    }

    @API(desc = "查询自己以及子角色", auth = APIAuth.NO)
    public SqlResult<Role> find(SqlParam<Role> params) throws Exception {

        SqlResult<Role> children = findChildren(params);
        List<Role> rows = children.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return null;
        }
        String roleIds = params.getModel().getRoleids();
        String[] roles = roleIds.split(",");

        for (String role : roles) {
            Role roleById = roleDao.findRoleById(role);
            if (roleById != null && !rows.contains(roleById)) {
                rows.add(roleById);
            }
        }
        children.setRows(rows);
        return children;
    }

    @API(desc = "删除角色", operation = APIOperation.DELETE)
    public String deleteRole(SqlParam<Role> params) throws Exception {
        String roleId = params.getModel().getRoleid();
        List<Role> roleByParentId = roleDao.findRoleByParentId(roleId);
        if (CollectionUtils.isEmpty(roleByParentId)) {
            Role roleById = roleDao.findRoleById(roleId);
            roleDao.deleteRole(roleById.getRoleid());
            return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
        } else {
            throw new PromptException("删除失败！请先删除子角色");
        }
    }

    @API(desc = "新增角色", auth = APIAuth.YES,operation = APIOperation.INSTER)
    public String addRole(SqlParam<Role> params) throws Exception {
        String resMsg = "";
        if (roleDao.findRoleByRoleName(params.getModel().getRolename()) != null) {
            return RequestSupport.updateReturnJson(false, "角色名称重复", null).toString();
        }
        boolean result = roleDao.addRole(params) > 0;
        if (result == true) {
            resMsg = "添加成功";
        }
        return RequestSupport.updateReturnJson(result, resMsg, null).toString();
    }

    @API(desc = "修改角色", operation = APIOperation.UPDATE)
    public String updateRole(SqlParam<Role> params) throws Exception {
        Integer count = roleDao.findCountRole(params);
        if(count>0){
            return RequestSupport.updateReturnJson(false, "修改失败,角色名已存在!", null).toString();
        }else{
            boolean result = roleDao.updateRole(params) > 0;
            return RequestSupport.updateReturnJson(result, result ? "修改成功" : "修改失败", null).toString();
        }
    }

    @API(desc = "设置文档权限", operation = APIOperation.UPDATE)
    public String setDocRole(SqlParam<Role> params) throws Exception {
        params.getModel().setDocReTypes(getNewDocType(params.getModel().getDocTypes()));
        boolean result = roleDao.updateRoleDoc(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "文档权限更新成功" : "文档权限更新失败", null).toString();
    }

    /**
     * 转换文档类型
     * @return
     */
    public static String getNewDocType(String str){
        if(str.contains("10001")){
            str = str.replace("10001","10101,10001,20001,30001,40001,50001,60001,70001");
        }
        if(str.contains("10002")){
            str = str.replace("10002","10102,10002,20002,30002,40002,50002,60002,70002");
        }
        if(str.contains("10003")){
            str = str.replace("10003","10103,10003,20003,30003,40003,50003,60003,70003");
        }
        if(str.contains("10004")){
            str = str.replace("10004","10104,10004,20004,30004,40004,50004,60004,70004");
        }
        if(str.contains("10005")){
            str = str.replace("10005","10105,10005,20005,30005,40005,50005,60005,70005");
        }
        if(str.contains("10006")){
            str = str.replace("10006","10106,10006,20006,30006,40006,50006,60006,70006");
        }
        if(str.contains("10007")){
            str = str.replace("10007","10107,10007,20007,30007,40007,50007,60007,70007");
        }
        if(str.contains("10008")){
            str = str.replace("10008","10108,10008,20008,30008,40008,50008,60008,70008");
        }
        if(str.contains("10009")){
            str = str.replace("10009","10109,10009,20009,30009,40009,50009,60009,70009");
        }
        if(str.contains("10010")){
            str = str.replace("10010","10110,10010,20010,30010,40010,50010,60010,70010");
        }
        if(str.contains("10011")){
            str = str.replace("10011","10111,10011,20011,30011,40011,50011,60011,70011");
        }
        if(str.contains("10012")){
            str = str.replace("10012","10112,10012,20012,30012,40012,50012,60012,70012");
        }
        return str;
    }

    @API(desc = "查询当前登录用户拥有的角色及子角色", auth = APIAuth.NO)
    public SqlResult<Role> findRoleByLoginUser(SqlParam<Role> params) throws Exception {
        //此处默认查询所有的角色,所有赋值为admin进行查询_仅针对光大理财特殊设置_yangxl
        //String roleisd = SysUtil.getSysUserParams().get("roleids").toString();
        String roleisd = "0";
        params.getModel().setRoleids(roleisd);
        // 传入 自身拥有的角色作为参数。去查询子角色
        SqlResult<Role> children = findChildren(params);
        List<Role> rows = children.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return null;
        }
        String roleIds = params.getModel().getRoleids();
        String[] roles = roleIds.split(",");
        // 把查到的子角色也赋给List
        for (String role : roles) {
            Role roleById = roleDao.findRoleById(role);
            if (roleById != null && !rows.contains(roleById)) {
                rows.add(roleById);
            }
        }
        children.setRows(rows);
        return children;
    }
    
    
    @API(desc = "查询角色", auth = APIAuth.NO)
    public SqlResult<SqlRow> findAll(SqlParam<Role> params) throws Exception {
    	
        SqlResult<SqlRow> result = new SqlResult<SqlRow>();
        List<SqlRow> roles = roleDao.findRoles();
        result.setRows(roles);
        return result;
    }
    @API(desc = "查询角色下所有用户", auth = APIAuth.NO)
    public SqlResult<SqlRow> findAllUser(SqlParam<Role> params) throws Exception {
        SqlResult<SqlRow> result = new SqlResult<SqlRow>();
        List<SqlRow> roles = roleDao.findAllUser(params);
        result.setRows(roles);
        return result;
    }

    @API(desc = "查询指定角色用户", auth = APIAuth.NO)
    public SqlResult<SqlRow> findRoleUser(SqlParam<Role> params) throws Exception {

        SqlResult<SqlRow> result = new SqlResult<SqlRow>();
        List<SqlRow> roles = roleDao.findRoleUser(params.getModel().getRoleid());
        result.setRows(roles);
        return result;
    }
}
