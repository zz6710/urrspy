package com.kayak.system.model;

import com.kayak.core.desensitized.BaseDesensitized;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "userPwdService", table = "sys_user")
public class UserInfo {

	private String orgname;

	private String deptname;

	@GraphQLField
	private String parentOrgno;

	@GraphQLField
	private String parentDeptno;

	@GraphQLField(key = true, sql = "userid = $S{userid}", field = "userid")
	private String userid;
	@GraphQLField(kkhtml = "KFieldText", label = "登录名称",kkhtmlDefault = true,sql = "loginname like '%$U{loginname}%'", field = "loginname")
	private String loginname;
	@GraphQLField(sql = "passwd = $S{passwd}", field = "passwd", desensitized = BaseDesensitized.class)
	private String passwd;
	@GraphQLField(kkhtml = "KFieldText", label = "用户名称",kkhtmlDefault = true, sql = "username like '%$U{username}%'", field = "username")
	private String username;
	@GraphQLField(kkhtml = "KFieldCascader", kkhtmlExt="{\"data-diffcondition\":\"deptno,parentdeptno\",\"data-graphql\":\"{queryDept(action:\\\"find\\\") {rows{deptno, deptname, parentdeptno, deptid},results}}\",\"data-display-child\":\"children\",\"data-check-strictly\":true,\"data-show-num\":true,\"data-props\":\"{ expandTrigger: 'hover'}\",\"data-size\":\"medium\",\"data-clearable\":true,\"data-fileterable\":true,\"data-display-field\":\"deptname\",\"data-value-field\":\"deptno\"}", label = "所属部门", sql = "deptno = $S{deptno}", field = "deptno")
	private String deptno;
	@GraphQLField(kkhtml = "KFieldCascader", kkhtmlExt="{\"data-diffcondition\":\"orgno,parentorgno\",\"data-graphql\":\"{queryOrg(action:\\\"find\\\") {rows{orgno, orgname, parentorgno, orgid},results}}\",\"data-display-child\":\"children\",\"data-check-strictly\":true,\"data-show-num\":true,\"data-props\":\"{ expandTrigger: 'hover'}\",\"data-size\":\"medium\",\"data-clearable\":true,\"data-fileterable\":true,\"data-display-field\":\"orgname\",\"data-value-field\":\"orgno\"}", label = "所属机构", sql = "orgno = $S{orgno}", field = "orgno")
	private String orgno;
	@GraphQLField(kkhtml = "KFieldText", label = "工号", kkhtmlDefault = true,sql = "jobno like '%$U{jobno}%'", field = "jobno")
	private String jobno;
	@GraphQLField(kkhtml = "KFieldSelect", label = "用户状态", kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"userstatus\"}",sql = "userstatus = $S{userstatus}", field = "userstatus")
	private String userstatus;
	@GraphQLField(sql = "idtype = $S{idtype}", field = "idtype")
	private String idtype;
	@GraphQLField(sql = "idno = $S{idno}", field = "idno")
	private String idno;
	@GraphQLField(kkhtml = "KFieldSelect", label = "性别",
			sql = "sex = $S{sex}", field = "sex", kkhtmlExt="{\"data-dict\": \"sex\"}" )
	private String sex;
	@GraphQLField(kkhtml = "KFieldText", label = "移动电话", sql = "mobileno like '%$U{mobileno}%'", field = "mobileno",kkhtmlDefault = true)
	private String mobileno;
	@GraphQLField(sql = "officeno = $S{officeno}", field = "officeno")
	private String officeno;
	@GraphQLField(sql = "homeno = $S{homeno}", field = "homeno")
	private String homeno;
	@GraphQLField(sql = "faxno = $S{faxno}", field = "faxno")
	private String faxno;
	@GraphQLField(kkhtml = "KFieldText", label = "电子邮箱",kkhtmlDefault = true, sql = "email like '%$U{email}%'", field = "email")
	private String email;
	@GraphQLField(sql = "postcode = $S{postcode}", field = "postcode")
	private String postcode;
	@GraphQLField(sql = "address = $S{address}", field = "address")
	private String address;
	@GraphQLField(sql = "createdate = $S{createdate}", field = "createdate")
	private String createdate;
	@GraphQLField(sql = "modifydate = $S{modifydate}", field = "modifydate")
	private String modifydate;
	@GraphQLField(sql = "pwderrtimes = $S{pwderrtimes}", field = "pwderrtimes")
	private String pwderrtimes;
	@GraphQLField(sql = "pwderrlockdt = $S{pwderrlockdt}", field = "pwderrlockdt")
	private String pwderrlockdt;
	@GraphQLField(sql = "pwdsetdate = $S{pwdsetdate}", field = "pwdsetdate")
	private String pwdsetdate;
	@GraphQLField(sql = "lastlogintime = $S{lastlogintime}", field = "lastlogintime")
	private String lastlogintime;
	@GraphQLField(sql = "lastloginstation = $S{lastloginstation}", field = "lastloginstation")
	private String lastloginstation;
	
	@GraphQLField(sql = "workOrg = $S{workOrg}", field = "work_org")
	private String workOrg;
	@GraphQLField(sql = "employeeType = $S{employeeType}", field = "employee_type")
	private String employeeType;
	@GraphQLField(sql = "status = $S{status}", field = "status")
	private String status;
	@GraphQLField(sql = "endTime = $S{endTime}", field = "end_time")
	private String endTime;
	@GraphQLField(sql = "birthday = $S{birthday}", field = "birthday")
	private String birthday;
	@GraphQLField(sql = "religion = $S{religion}", field = "religion")
	private String religion;
	@GraphQLField(sql = "nation = $S{nation}", field = "nation")
	private String nation;
	@GraphQLField(sql = "duty = $S{duty}", field = "duty")
	private String duty;
	@GraphQLField(sql = "supporterCorpName = $S{supporterCorpName}", field = "supporter_corp_name")
	private String supporterCorpName;
	
	@GraphQLField(sql = "is_initial_pwd = $S{isInitialPwd}", field = "is_initial_pwd")
	private String isInitialPwd;
	
	@GraphQLField(sql = "o.orgid like '$U{orgid}%'", field = "orgid")
	private String orgid;

	@GraphQLField
	private String roleids;

	@GraphQLField
	private String roleId;
	
	@GraphQLField
	private String oldPwd;
}