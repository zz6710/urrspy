package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "workdayProgramService", table = "sys_workday_pgm")
@Data
public class WorkdayProgram {

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "方案编号", sql = "pgmno LIKE '%$U{pgmno}%'", field = "pgmno")
    private String pgmno;

    @GraphQLField(kkhtml = "KFieldText", label = "方案名称", sql = "pgmname LIKE '%$U{pgmname}%'", field = "pgmname")
    private String pgmname;

    @GraphQLField(kkhtml = "KFieldSelect", label = "方案类型", sql = "pgmtype = $S{pgmtype}",
            field = "pgmtype", kkhtmlExt="{\"data-dict\": \"pgmtype\"}")
    private String pgmtype;

    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark LIKE '%$U{remark}%'", field = "remark")
    private String remark;

}
