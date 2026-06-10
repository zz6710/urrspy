package com.kayak.pms.declare.dao;

import cn.hutool.core.map.MapUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.declare.model.MaterialTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MaterialTemplateDao extends ComnDao {

    //符模板数据字典
    public SqlResult<MaterialTemplate> getTemplateTypeDict(SqlParam<MaterialTemplate> params) throws Exception {
        return super.findRows("select distinct t.template_type, t.template_label from pids_declare_type t where t.module = '1'", DataSourceProperty.IDB,params);
}
    //子模板数据字典
    public SqlResult<MaterialTemplate> getTemplateSonTypeDict(SqlParam<MaterialTemplate> params) throws Exception {
        return super.findRows("select distinct t.template_son_type, t.template_son_label from pids_declare_type t " +
                "where t.module = '1' and is_son_template = '1' and t.template_type = $S{templateType} ", DataSourceProperty.IDB,  params);
    }


    private StringBuilder getTemplateTypeSql (){
        return new StringBuilder("select t.id, t.module, t.template_type, t.template_son_type, t.template_label, t.template_son_label," +
                " t.is_son_template, t.value_method, t.is_placeholder, t.crt_date, t.crt_time, t.crt_user " +
                "from pids_declare_type t where t.module = '1' ");
    }
    public List<MaterialTemplate> findTemplateType(String templateType) throws Exception {

        StringBuilder sql = getTemplateTypeSql();

        if (StringUtils.isNotBlank(templateType)){
            sql.append("and t.template_type = $S{templateType} ");
        }
        sql.append(" order by template_type,template_son_type");
        return super.findRows(MaterialTemplate.class,sql.toString(), DataSourceProperty.IDB ,templateType);

    }



    private StringBuilder getTemplateSql (){
        return new StringBuilder("select t.template_id, t.prod_mod, t.template_type, t.template_son_type, t.template_name, " +
                "t.template_start,t.trutee_bank,(select itemval from sys_dict_item where dict='pids_trutee_bank' and itemkey=t.trutee_bank) trutee_bank_name, t.remark, t.crt_date, t.crt_time, t.crt_user, t.upd_date, t.upd_time, t.upd_user, " +
                "t2.template_label , t2.template_son_label " +
                "from pids_material_template t " +
                "left join pids_declare_type t2 on t.template_type = t2.template_type and ifnull(t.template_son_type,'') = ifnull(t2.template_son_type,'') " +
                " where 1 = 1 ");
    }

    public List<MaterialTemplate> findMaterialTemplate(Map<String,Object> params) throws Exception {

        StringBuilder sql = getTemplateSql();

        if (!(params.get("templateType") == null) && !params.get("templateType").equals("")){
            sql.append("and t.template_type = $S{templateType} ");
        }
        if (!(params.get("templateSonType") == null) && !params.get("templateSonType").equals("")){
            sql.append("and t.template_son_type = $S{templateSonType} ");
        }
        if (!(params.get("prodMod") == null) && !params.get("prodMod").equals("")){
            sql.append("and t.prod_mod = $S{prodMod} ");
        }

        return super.findRows(MaterialTemplate.class,sql.toString(), DataSourceProperty.IDB,params);
    }

    public SqlResult<MaterialTemplate> findMaterialTemplate(SqlParam<MaterialTemplate> params) throws Exception {
        MaterialTemplate m = params.getModel();
        StringBuilder sql = getTemplateSql();
        if(StringUtils.isNotBlank(m.getTemplateId())){
            sql.append("and t.template_id = $S{templateId} ");
        }
        if(StringUtils.isNotBlank(m.getTemplateType())){
            sql.append("and t.template_type = $S{templateType} ");
        }
        if(StringUtils.isNotBlank(m.getProdMod())){
            sql.append("and t.prod_mod = $S{prodMod} ");
        }
        if(StringUtils.isNotBlank(m.getTemplateStart())){
            sql.append("and t.template_start = $S{templateStart} ");
        }
        if(StringUtils.isNotBlank(m.getTemplateName())){
            sql.append("and t.template_name like '%" + m.getTemplateName() + "%' ");
        }
        sql.append("order by t.template_type ,template_son_type ");
        return super.findRows(sql.toString(), DataSourceProperty.IDB, params);
    }


    public String addMaterialTemplate(MaterialTemplate params) throws Exception {
        return super.update("INSERT INTO pids_material_template (template_id,prod_mod,trutee_bank,template_type,template_son_type,template_name,template_start,remark,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) " +
                        " VALUES ($AUTOIDS{templateId},$S{prodMod},$S{truteeBank},$S{templateType},$S{templateSonType},$S{templateName},$S{templateStart},$S{remark},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
                DataSourceProperty.IDB,params).getAutoId();
    }

    public int updateTemplateStart(MaterialTemplate params) throws Exception {
        return super.update("UPDATE pids_material_template SET template_start = $S{templateStart} WHERE template_id=$S{templateId} " ,DataSourceProperty.IDB,
                params).getEffect();
    }
    public int updateMaterialTemplate(MaterialTemplate params) throws Exception {
        return super.update("UPDATE pids_material_template SET " +
                        "prod_mod = $S{prodMod}, " +
                        "trutee_bank = $S{truteeBank}, " +
                        "template_type = $S{templateType}," +
                        "template_son_type = $S{templateSonType}," +
                        "template_name = $S{templateName}," +
                        "template_start = $S{templateStart}," +
                        "remark = $S{remark}," +
                        "upd_date = $S{updDate}," +
                        "upd_time = $S{updTime}," +
                        "upd_user = $S{updUser}" +
                        " WHERE template_id=$S{templateId} ",DataSourceProperty.IDB,
                params).getEffect();
    }


    public StringBuilder getFindVersionSql(){

        return new StringBuilder("select t.version_id, t.template_id, t.version_name, t.version_path, t.version_start, " +
                "t.version_num, t.remark, t.crt_date, t.crt_time, t.crt_user, t.upd_date, t.upd_time, t.upd_user, " +
                "t2.template_type , t2.template_son_type, t2.trutee_bank,t2.prod_mod " +
                "from pids_material_version t " +
                "left join pids_material_template t2 on t.template_id = t2.template_id " +
                " where 1 = 1 ");

    }
    public List<MaterialTemplate> findMaterialVersion(Map<String, Object> params) throws Exception {

        StringBuilder sql = getFindVersionSql();

        if (!(params.get("templateType") == null) && !params.get("templateType").equals("")){
            sql.append("and t2.template_type = $S{templateType} ");
        }
        if (!(params.get("templateSonType") == null) && !params.get("templateSonType").equals("")){
            sql.append(" and t2.template_son_type = $S{templateSonType} ");
        }

        if (!(params.get("prodMod") == null) && !params.get("prodMod").equals("")){
            sql.append(" and t2.prod_mod = $S{prodMod} ");
        }

        if (!(params.get("truteeBank") == null) && !params.get("truteeBank").equals("")){
            sql.append(" and t2.trutee_bank = $S{truteeBank} ");
        }

        sql.append(" order by version_num desc " +
               " limit 0 ,10 ");

        return super.findRows(MaterialTemplate.class,sql.toString(), DataSourceProperty.IDB,params);
    }


    public List<MaterialTemplate> findMaxVersion(String templateType) throws Exception {

        StringBuilder sql = getFindVersionSql();

        sql.append(" and t.version_id in ( select max(version_id) version_id from pids_material_version where t.version_start = '1' GROUP BY template_id) " );

        if (StringUtils.isNotBlank(templateType)){
            sql.append(" and t2.template_type = $S{templateType} ");
        }
        sql.append(" order by t2.template_type , t2.template_son_type ") ;

        return super.findRows(MaterialTemplate.class,sql.toString(), DataSourceProperty.IDB ,templateType);
    }

    public SqlResult<MaterialTemplate> findMaterialVersion(SqlParam<MaterialTemplate> params) throws Exception {

        MaterialTemplate m = params.getModel();
        StringBuilder sql = getFindVersionSql();

        if(StringUtils.isNotBlank(m.getTemplateId())){
            sql.append("and t.template_id = $S{templateId} ");
        }

        if(StringUtils.isNotBlank(m.getVersionId())){
            sql.append("and t.version_id = $S{versionId} ");
        }

        if(StringUtils.isNotBlank(m.getVersionStart())){
            sql.append("and t.version_start = $S{versionStart} ");
        }

        if(StringUtils.isNotBlank(m.getVersionName())){
            sql.append("and t.version_name like '%" + m.getVersionName() + "%'");
        }

        sql.append("order by t.version_id desc ");

        return super.findRows(sql.toString(),DataSourceProperty.IDB, params);
    }


    public int addMaterialVersion(MaterialTemplate params) throws Exception {
        return super.update("INSERT INTO pids_material_version (version_id, template_id, version_name, version_path,version_num, version_start,remark,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) " +
                        " VALUES ($AUTOIDS{versionId},$S{templateId},$S{versionName},$S{versionPath},$S{versionNum},$S{versionStart},$S{remark},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
                DataSourceProperty.IDB, params).getEffect();
    }

    public int updateVersionStart(MaterialTemplate params) throws Exception {
        return super.update("UPDATE pids_material_version SET " +
                        "version_start = $S{versionStart} " +
                        "WHERE version_id = $S{versionId}" ,DataSourceProperty.IDB,
                params).getEffect();
    }

    public int updateMaterialVersion(MaterialTemplate params) throws Exception {
        return super.update("UPDATE pids_material_version SET " +
                        "template_id = $S{templateId}, " +
                        "version_name = $S{versionName}," +
                        "version_path = $S{versionPath}," +
                        "version_start = $S{versionStart}," +
                        "version_num = $S{versionNum}," +
                        "remark = $S{remark}," +
                        "upd_date = $S{updDate}," +
                        "upd_time = $S{updTime}," +
                        "upd_user = $S{updUser}" +
                        " WHERE version_id = $S{versionId} ", DataSourceProperty.IDB,
                params).getEffect();
    }

    public int deleteMaterialTemplate(MaterialTemplate params) throws Exception {
        return super.update("DELETE FROM pids_material_template WHERE template_id=$S{templateId} ",DataSourceProperty.IDB,
                params).getEffect();
    }

    public int deleteMaterialVersion(MaterialTemplate params) throws Exception {
        return super.update("DELETE FROM pids_material_version WHERE version_id = $S{versionId} ", DataSourceProperty.IDB,
                params).getEffect();
    }

    public String getConfigSql(){
        return "select t1.id sql_id, t1.value_sql sql_select, t1.status sql_start, t1.column_label , t1.column_key, t2.template_id,t2.sql_order, " +
                "t2.crt_date, t2.crt_user from pids_material_config_template t2\n" +
                "left join idb_disclosure_source t1 on t1.id = t2.sql_id\n" +
                "where t2.template_id = $S{templateId}\n" +
                "order by t2.sql_order";
    }

    public SqlResult<MaterialTemplate> findMaterialConfig(SqlParam<MaterialTemplate> params) throws Exception {
        String sql = getConfigSql();
        return super.findRows(sql, DataSourceProperty.IDB, params);
    }

    public List<MaterialTemplate> findMaterialConfig(MaterialTemplate params) throws Exception {
        return super.findRows(MaterialTemplate.class,getConfigSql(), DataSourceProperty.IDB,params);
    }

    public int addMaterialConfig(MaterialTemplate params) throws Exception {
        return super.update("INSERT INTO pids_material_config_template (sql_id, template_id, sql_order,crt_date,crt_time,crt_user) " +
                        " VALUES ($S{sqlId},$S{templateId},$S{sqlOrder},$S{crtDate},$S{crtTime},$S{crtUser})", DataSourceProperty.IDB,
                params).getEffect();
    }

    public int deleteMaterialConfig(MaterialTemplate params) throws Exception {
        return super.update("DELETE FROM pids_material_config_template WHERE sql_id = $S{sqlId} and template_id = $S{templateId} ", DataSourceProperty.IDB, params).getEffect();
    }

    //查询出配置sql并按规定驼峰命名
    public List<Map<String, Object>> findSqlValue (String sql , Map<String, Object> params) throws Exception {
        return super.findRows( sql, params)
                .stream().map(MapUtil::toCamelCaseMap).collect(Collectors.toList());
    }

}
