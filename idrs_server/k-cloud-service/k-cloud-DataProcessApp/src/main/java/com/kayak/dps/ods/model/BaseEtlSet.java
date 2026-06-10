package com.kayak.dps.ods.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * 功能：数仓同步配置表
 */
@Data
@GraphQLModel(fetcher = "baseEtlSetService", table = "base_etl_set")
public class BaseEtlSet {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "源database", sql = "`database` = $S{database}", field = "database")
    private String database;
    @GraphQLField(kkhtml = "KFieldText", label = "源系统描述", sql = "sysdesc = $S{sysdesc}", field = "sysdesc")
    private String sysdesc;
    @GraphQLField(kkhtml = "KFieldText", label = "数据源编号", sql = "sharding = $S{sharding}", field = "sharding")
    private String sharding;
    @GraphQLField(kkhtml = "KFieldText", label = "源表名", sql = "tablename = $S{tablename}", field = "tablename")
    private String tablename;
    @GraphQLField(kkhtml = "KFieldText", label = "sql语句字段", sql = "table_cloumns = $S{tableCloumns}", field = "table_cloumns")
    private String tableCloumns;
    @GraphQLField(kkhtml = "KFieldText", label = "排序", sql = "sql_order = $S{sqlOrder}", field = "sql_order")
    private String sqlOrder;
    @GraphQLField(kkhtml = "KFieldText", label = "查询条件", sql = "select_condition = $S{selectCondition}", field = "select_condition")
    private String selectCondition;
    @GraphQLField(kkhtml = "KFieldText", label = "日期条件", sql = "date_condition = $S{dateCondition}", field = "date_condition")
    private String dateCondition;
    @GraphQLField(kkhtml = "KFieldText", label = "日期条件", sql = "date_end_condition = $S{dateEndCondition}", field = "date_end_condition")
    private String dateEndCondition;
    @GraphQLField(kkhtml = "KFieldText", label = "每日执行时间", sql = "exec_time = $S{execTime}", field = "exec_time")
    private String execTime;
    @GraphQLField(kkhtml = "KFieldText", label = "增量标识", sql = "increment_flag = $S{incrementFlag}", field = "increment_flag")
    private String incrementFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "条数约束", sql = "limits = $S{limits}", field = "limits")
    private String limits;
    @GraphQLField(kkhtml = "KFieldText", label = "配置状态", sql = "status = $S{status}", field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}", field = "crt_user_id")
    private String crtUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}", field = "upd_user_id")
    private String updUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "increment_key = $S{incrementKey}", field = "increment_key")
    private String incrementKey;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "pms_tablename = $S{pmsTablename}", field = "pms_tablename")
    private String pmsTablename;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "increment_update_sql = $S{incrementUpdateSql}", field = "increment_update_sql")
    private String incrementUpdateSql;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "unique_key = $S{uniqueKey}", field = "unique_key")
    private String uniqueKey;
    @GraphQLField
    private String delay;

}