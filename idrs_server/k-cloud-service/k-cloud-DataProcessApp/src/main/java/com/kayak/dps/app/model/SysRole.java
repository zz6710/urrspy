package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "sysRoleService",table = "sys_role")
public class SysRole {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "角色ID", sql = "roleid = $S{roleId}" ,field = "roleid")
   private String roleId;
   @GraphQLField(kkhtml = "KFieldText", label = "父级角色", sql = "parentroleid = $S{parentroleId}" ,field = "parentroleid")
   private String parentroleId;
   @GraphQLField(kkhtml = "KFieldText", label = "角色名称", sql = "rolename = $S{roleName}" ,field = "rolename")
   private String roleName;
   @GraphQLField(kkhtml = "KFieldText", label = "角色类型", sql = "roletype = $S{roleType}" ,field = "roletype")
   private String roleType;
   @GraphQLField(kkhtml = "KFieldText", label = "描述", sql = "descript = $S{descript}" ,field = "descript")
   private String descript;
   @GraphQLField(kkhtml = "KFieldText", label = "流程模板id", sql = "flowtemplateid = $S{flowtemplateId}" ,field = "flowtemplateid")
   private String flowtemplateId;
   
  	public String getRoleid() {
        return roleId;
    }

    public void setRoleid(String roleid) {
        this.roleId = roleid;
    }
  	public String getParentroleid() {
        return parentroleId;
    }

    public void setParentroleid(String parentroleid) {
        this.parentroleId = parentroleid;
    }
  	public String getRolename() {
        return roleName;
    }

    public void setRolename(String rolename) {
        this.roleName = rolename;
    }
  	public String getRoletype() {
        return roleType;
    }

    public void setRoletype(String roletype) {
        this.roleType = roletype;
    }
  	public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
  	public String getFlowtemplateid() {
        return flowtemplateId;
    }

    public void setFlowtemplateid(String flowtemplateid) {
        this.flowtemplateId = flowtemplateid;
    }

}