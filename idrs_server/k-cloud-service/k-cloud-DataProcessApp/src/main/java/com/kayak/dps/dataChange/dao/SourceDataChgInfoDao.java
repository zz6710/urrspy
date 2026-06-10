package com.kayak.dps.dataChange.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.dataChange.model.SourceDataChgInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SourceDataChgInfoDao extends ComnDao {

    public SqlResult<SourceDataChgInfoModel> findSourceDataChgInfoModelForOne(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        String sql = "select distinct t1.RELATED_REPORT report_name, t.tables,t.natural_keys," +
                "(select port_name from base_port_manage where port_table=t.TABLES) table_name, t.status, t.crt_dt, t.upd_dt,t.deal_date,t1.task_group,t.newid " +
                "from rem_sourcedata_chginfo t left join rem_sourcedata_config t1 on t.TABLES = t1.TABLE_NAME " +
                "where 1=1 and t.port_type not in ('5','8')";
        if(Strings.isNotBlank(params.getModel().getStatus())){
            sql += " and t.status = $S{status}";
        }
        if(Strings.isNotBlank(params.getModel().getDealDate())){
            sql += " and t.deal_date = $S{dealDate}";
        }
        if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate()) && StringUtils.isNotBlank(params.getModel().getTheoryReportEndDate())) {
            sql += " and t.deal_date between "+"'"+params.getModel().getTheoryReportStartDate()+"'"+" and "+"'"+params.getModel().getTheoryReportEndDate()+"'";
        }
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }
    public SqlResult<SourceDataChgInfoModel> findSourceDataChgInfoModelForUnconfirmed(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        String sql = "select distinct t1.RELATED_REPORT report_name, t.tables,t.natural_keys," +
                "(select port_name from base_port_manage where port_table=t.TABLES) table_name, t.status, t.crt_dt, t.upd_dt,t.deal_date " +
                "from rem_sourcedata_chginfo t left join rem_sourcedata_config t1 on t.TABLES = t1.TABLE_NAME " +
                "where 1=1 and t.port_type not in ('5','8') and t.status = '0'";

        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public SqlResult<SourceDataChgInfoModel> findAssetSourceDataChgInfoModelForOne(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        String sql = "select distinct t1.RELATED_REPORT report_name, t.tables,t.natural_keys," +
                "(select port_name from base_port_manage where port_table=t.TABLES) table_name, t.status, t.crt_dt, t.upd_dt,t.deal_date " +
                "from rem_sourcedata_chginfo t left join rem_sourcedata_config t1 on t.TABLES = t1.TABLE_NAME " +
                "where 1=1 and t.port_type in ('5','8')";
        if(Strings.isNotBlank(params.getModel().getStatus())){
            sql += " and t.status = $S{status}";
        }
        if(Strings.isNotBlank(params.getModel().getDealDate())){
            sql += " and t.deal_date = $S{dealDate}";
        }
        if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate()) && StringUtils.isNotBlank(params.getModel().getTheoryReportEndDate())) {
            sql += " and t.deal_date between "+"'"+params.getModel().getTheoryReportStartDate()+"'"+" and "+"'"+params.getModel().getTheoryReportEndDate()+"'";
        }
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }
    public SqlResult<SourceDataChgInfoModel> findAssetSourceDataChgInfoModelForUnconfirmed(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        String sql = "select distinct t1.RELATED_REPORT report_name, t.tables,t.natural_keys," +
                "(select port_name from base_port_manage where port_table=t.TABLES) table_name, t.status, t.crt_dt, t.upd_dt,t.deal_date " +
                "from rem_sourcedata_chginfo t left join rem_sourcedata_config t1 on t.TABLES = t1.TABLE_NAME " +
                "where 1=1 and t.port_type in ('5','8') and t.status = '0'";

        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public UpdateResult confirmSourceDataChgInfoModel(SourceDataChgInfoModel params) throws Exception {
        return super.update("UPDATE rem_sourcedata_chginfo set status = '1',editstatus = '"+params.getEditstatus()+"',upd_dt=DATE_FORMAT(NOW(), '%Y%m%d') where id = '"+params.getId()+"' and status = '0'",
                DataSourceProperty.PUB,null);
    }

    public SqlResult<SourceDataChgInfoModel> findSourceDataChgInfoModelAll(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        String sql = "select tt.id,tt.report_name,tt.natural_keys,tt.change_field,  tt.change_field_name,tt.tables,tt.oldid,tt.newid,tt.table_name,tt.field_old,tt.field_new,tt.status,tt.editstatus, tt.crt_dt, tt.upd_dt,ifnull(t4.itemval,tt.field_old) as dict_old,ifnull(t5.itemval,tt.field_new) as dict_new  from (" +
                "select t.id, t1.RELATED_REPORT report_name, t.natural_keys, t.change_field, t3.FIELD_NAME as change_field_name, t.tables,t.oldid,t.newid," +
                "(select port_name from base_port_manage where port_table=t.TABLES) table_name, (case when t.field_old_var is not null then t.field_old_var when t.field_old_int is not null then t.field_old_int " +
                "when t.field_old_db is not null then t.field_old_db when t.field_old_dec is not null then t.field_old_dec when t.field_old_tm is not null then t.field_old_tm " +
                "when t.field_old_dts is not null then t.field_old_dts when t.field_old_bool is not null then t.field_old_bool when t.field_old_blob is not null then t.field_old_blob " +
                "when t.field_old_dt is not null then t.field_old_dt when t.field_old_geo is not null then t.field_old_geo else t.FIELD_OLD end) field_old, (case when t.field_new_var is not null then t.field_new_var when t.field_new_int is not null then t.field_new_int " +
                "when t.field_new_db is not null then t.field_new_db when t.field_new_dec is not null then t.field_new_dec when t.field_new_tm is not null then t.field_new_tm " +
                "when t.field_new_dts is not null then t.field_new_dts when t.field_new_bool is not null then t.field_new_bool when t.field_new_blob is not null then t.field_new_blob " +
                "when t.field_new_dt is not null then t.field_new_dt when t.field_new_geo is not null then t.field_new_geo else t.FIELD_new end) field_new, t.status,t.editstatus, t.crt_dt, t.upd_dt,t1.out_dict " +
                "from rem_sourcedata_chginfo t left join rem_sourcedata_config t1 on t.TABLES = t1.TABLE_NAME and t.CHANGE_FIELD = t1.REMIND_FIELD " +
                "left join base_port_manage t2 on t.TABLES = t2.port_table " +
                "left join base_port_field_manage t3 on t2.port_code = t3.PORT_CODE and t3.FIELD_CODE = t.CHANGE_FIELD " +
                "where t.tables = $S{tables} and t.status = '0' and t.natural_keys = $S{naturalKeys} and t.newid=$S{newid}) tt " +
                "left join sys_dict_item t4 on tt.out_dict = t4.dict and tt.field_old=t4.itemkey " +
                "left join sys_dict_item t5 on tt.out_dict = t5.dict and tt.field_new=t5.itemkey";
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public SqlResult<SourceDataChgInfoModel> findSourceDataChgInfoModelAll2(SqlParam<SourceDataChgInfoModel> params) throws Exception {
        String sql = "select tt.id,tt.report_name,tt.natural_keys,tt.change_field,tt.change_field_name,tt.tables,tt.oldid,tt.newid,tt.table_name,tt.field_old,tt.field_new,tt.status,tt.editstatus, tt.crt_dt, tt.upd_dt,ifnull(t4.itemval,tt.field_old) as dict_old,ifnull(t5.itemval,tt.field_new) as dict_new  from (" +
                "select t.id, t1.RELATED_REPORT report_name, t.natural_keys, t.change_field, t3.FIELD_NAME as change_field_name, t.tables,t.oldid,t.newid," +
                "(select port_name from base_port_manage where port_table=t.TABLES) table_name, (case when t.field_old_var is not null then t.field_old_var when t.field_old_int is not null then t.field_old_int " +
                "when t.field_old_db is not null then t.field_old_db when t.field_old_dec is not null then t.field_old_dec when t.field_old_tm is not null then t.field_old_tm " +
                "when t.field_old_dts is not null then t.field_old_dts when t.field_old_bool is not null then t.field_old_bool when t.field_old_blob is not null then t.field_old_blob " +
                "when t.field_old_geo is not null then t.field_old_geo else t.FIELD_OLD end) field_old, (case when t.field_new_var is not null then t.field_new_var when t.field_new_int is not null then t.field_new_int " +
                "when t.field_new_db is not null then t.field_new_db when t.field_new_dec is not null then t.field_new_dec when t.field_new_tm is not null then t.field_new_tm " +
                "when t.field_new_dts is not null then t.field_new_dts when t.field_new_bool is not null then t.field_new_bool when t.field_new_blob is not null then t.field_new_blob " +
                "when t.field_new_geo is not null then t.field_new_geo else t.FIELD_new end) field_new, t.status,t.editstatus, t.crt_dt, t.upd_dt,t1.out_dict " +
                "from rem_sourcedata_chginfo t left join rem_sourcedata_config t1 on t.TABLES = t1.TABLE_NAME and t.CHANGE_FIELD = t1.REMIND_FIELD " +
                "left join base_port_manage t2 on t.TABLES = t2.port_table " +
                "left join base_port_field_manage t3 on t2.port_code = t3.PORT_CODE and t3.FIELD_CODE = t.CHANGE_FIELD " +
                "where t.tables = $S{tables} and t.natural_keys = $S{naturalKeys} and t.status=$S{status} and t.deal_date=$S{dealDate} and t.newid=$S{newid}) tt " +
                "left join sys_dict_item t4 on tt.out_dict = t4.dict and tt.field_old=t4.itemkey " +
                "left join sys_dict_item t5 on tt.out_dict = t5.dict and tt.field_new=t5.itemkey";
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public UpdateResult updateField(SourceDataChgInfoModel params) throws Exception {
        return super.update("update $U{tables} set "+ params.getChangeField() +" = '"+params.getFieldOld()+"' where id = $S{newid} ",
                DataSourceProperty.PUB,params);
    }

    public UpdateResult newChg(SourceDataChgInfoModel params,String is_effective) throws Exception {
        return super.update("update $U{tables} set is_effective = '"+is_effective+"' where id = $S{newid} ",
                DataSourceProperty.PUB,params);
    }

    public UpdateResult oldChg(SourceDataChgInfoModel params,String is_effective) throws Exception {
        return super.update("update $U{tables} set is_effective = '"+is_effective+"' where id = $S{oldid} ",
                DataSourceProperty.PUB,params);
    }

    public SqlRow findKeyName(String key, String tables) throws Exception {
        String sql = "select t1.FIELD_NAME from base_port_manage t left join base_port_field_manage t1 on t.port_code = t1.PORT_CODE " +
                "where t.port_table = '"+tables+"' and t1.FIELD_CODE ='"+key+"'";
        return super.findRow(sql,null);
    }

    public SqlRow findReportName(String whererelatedReport) throws Exception {
        String sql = "select GROUP_CONCAT(TABLE_COMMENT) as reports_name from information_schema.tables " +
                "where TABLE_NAME in ("+whererelatedReport+") and TABLE_SCHEMA = (select schema())";
        return super.findRow(sql,null);
    }

    public SqlRow findValueName(String key, String value, String tables) throws Exception {
        String sql = "select ifnull(sdi.itemval,'"+value+"') itemval from rem_datanatural_key rdk left join sys_dict_item sdi on rdk.OUT_DICT = sdi.dict and sdi.itemkey ='"+value+"' " +
                "where rdk.TABLE_NAME ='"+tables+"' and rdk.NATURAL_KEY ='"+key+"'";
        return super.findRow(sql,null);
    }

    public List<SqlRow> findComType(Map<String, Object> params) throws Exception {
        String table  =  params.get("port_table").toString();
        String sql = "select  t.comparison_rules  from  rem_sourcedata_config t  where t.TABLE_NAME  =  '"+table+"' ";
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public SqlRow findValueNameContrast(String key, String value, String tables) throws Exception {
        String sql = " select ifnull(tt.itemval,'"+value+"') as itemval  from  rem_sourcedata_config t  left  join  (select *  from   sys_dict_item where itemkey = '"+value+"' )  tt on tt.dict  = t.OUT_DICT    where  t.TABLE_NAME = '"+tables+"'  and  t.REMIND_FIELD  =  '"+key+"'  ";
        return super.findRow(sql,null);
    }

}
