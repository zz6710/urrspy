package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureEvaluateEmpService", table = "idb_disclosure_evaluate_emp")
public class DisclosureEvaluateEmp {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}", field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "员工号", sql = "emp_no = $S{empNo}", field = "emp_no")
    private String empNo;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人id", sql = "create_user_id = $S{createUserId}", field = "create_user_id")
    private String createUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "create_user_name = $S{createUserName}", field = "create_user_name")
    private String createUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_date_time = $S{createDateTime}", field = "create_date_time")
    private String createDateTime;
    @GraphQLField(label = "产品名称", field = "prod_code")
    private String prodName;
    @GraphQLField(label = "员工名称", field = "emp_name")
    private String empName;
    @GraphQLField(label = "用户编号", field = "user_id")
    private String userId;
    @GraphQLField(label = "修改人id", field = "update_user_id")
    private String updateUserId;
    @GraphQLField(label = "修改人姓名", field = "update_user_name")
    private String updateUserName;
    @GraphQLField(label = "修改时间", field = "update_date_time")
    private String updateDateTime;
}