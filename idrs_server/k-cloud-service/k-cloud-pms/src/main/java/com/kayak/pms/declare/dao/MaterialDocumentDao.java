package com.kayak.pms.declare.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import com.kayak.pms.declare.model.MaterialDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MaterialDocumentDao extends ComnDao {

    public SqlResult<MaterialDocument> findProdInfo(SqlParam<MaterialDocument> params) throws Exception {
        MaterialDocument m = params.getModel();
        StringBuilder sql = new StringBuilder("select t.prod_cd prod_code, t.prod_nm prod_name, t.prod_mod,t.found_dt found_date," +
                "t.mtu_dt mtu_date,t.prod_sts ,t.dms_trst_org_nm trutee_bank " +
                "from app_prd_bas_inf t " +
                "where 1 = 1 ");
        if(StringUtils.isNotBlank(m.getProdCode())){
            sql.append("and t.prod_cd = $S{prodCode} ");
        }
        if(StringUtils.isNotBlank(m.getProdMod())){
            sql.append("and t.prod_mod = $S{prodMod} ");
        }
        if(StringUtils.isNotBlank(m.getFoundDate())){
            sql.append("and t.found_dt = $S{foundDate} ");
        }

        sql.append("order by t.found_dt desc ");

        return super.findRows(sql.toString(), DataSourceProperty.IDB,  params);
    }

    public SqlResult<MaterialDocument> findMaterialDocument(SqlParam<MaterialDocument> params) throws Exception {
        MaterialDocument m = params.getModel();
        StringBuilder sql = new StringBuilder("select tt.template_type document_num, tt.document_id, tt.prod_code, t1.prod_mod,tt.hand_upload,  tt.template_type, tt.template_son_type, tt.document_name, tt.document_path, tt.crt_date, tt.crt_time, tt.crt_user, " +
                "t1.prod_nm prod_name ,t1.dms_trst_org_nm trutee_bank, t2.template_label , t2.template_son_label  from (" +
                "(select t.document_id, t.prod_code, t.hand_upload, t.template_type, t.template_son_type, t.document_name, " +
                "t.document_path, t.crt_date, t.crt_time, t.crt_user " +
                "from pids_material_document t where t.template_type != '06') " +
                "UNION ALL " +
                "(select t.document_id, t.prod_code, t.hand_upload, t.template_type, t.template_son_type, t.document_name, " +
                "t.document_path, t.crt_date, t.crt_time, t.crt_user " +
                "from pids_material_document t where t.template_type = '06' group by t.prod_code ) " +
                ") tt " +
                "left join app_prd_bas_inf t1 on t1.prod_cd = tt.prod_code " +
                "left join pids_declare_type t2 on tt.template_type = t2.template_type and ifnull(tt.template_son_type,'') = ifnull(t2.template_son_type,'') " +
                "where 1 = 1 ");
        if(StringUtils.isNotBlank(m.getDocumentId())){
            sql.append("and tt.document_id = $S{documentId} ");
        }
        if(StringUtils.isNotBlank(m.getProdCode())){
            sql.append("and tt.prod_code = $S{prodCode} ");
        }
        if(StringUtils.isNotBlank(m.getProdMod())){
            sql.append("and t1.prod_mod = $S{prodMod} ");
        }
        if(StringUtils.isNotBlank(m.getTemplateType())){
            sql.append("and tt.template_type = $S{templateType} ");
        }
        sql.append("order by template_type ");

        return super.findRows(sql.toString(),DataSourceProperty.IDB, params);
    }

    public StringBuilder getDocumentSql(){
        return new StringBuilder("select t.template_son_type document_num ,t.hand_upload, t.document_id, t.prod_code, t1.prod_mod, " +
                "t1.prod_nm prod_name,t1.dms_trst_org_nm trutee_bank, t.template_type, t.template_son_type, t.document_name, " +
                "t.document_path, t.crt_date, t.crt_time, t.crt_user , t2.template_label , t2.template_son_label " +
                "from pids_material_document t " +
                "left join app_prd_bas_inf t1 on t1.prod_cd = t.prod_code " +
                "left join pids_declare_type t2 on t.template_type = t2.template_type and ifnull(t.template_son_type,'') = ifnull(t2.template_son_type,'') " +
                "where 1 = 1 ");
    }

    public StringBuilder getDocumentSqlHis(){
        return new StringBuilder("select t.template_son_type document_num ,t.hand_upload, t.document_id, t.prod_code, " +
                "t1.prod_mod, t1.prod_nm prod_name, t1.dms_trst_org_nm trutee_bank, t.template_type, t.template_son_type, t.document_name, " +
                "t.document_path, t.crt_date, t.crt_time, t.crt_user, t2.template_label , t2.template_son_label  " +
                "from pids_material_document_his t " +
                "left join app_prd_bas_inf t1 on t1.prod_cd = t.prod_code " +
                "left join pids_declare_type t2 on t.template_type = t2.template_type and ifnull(t.template_son_type,'') = ifnull(t2.template_son_type,'') " +
                "where 1 = 1 ");
    }

    //查询子文档
    public SqlResult<MaterialDocument> findSalesDocument(SqlParam<MaterialDocument> params) throws Exception {
        MaterialDocument m = params.getModel();

        StringBuilder sql = getDocumentSql();
        if(StringUtils.isNotBlank(m.getDocumentId())){
            sql.append("and t.document_id = $S{documentId} ");
        }
        if(StringUtils.isNotBlank(m.getProdCode())){
            sql.append("and t.prod_code = $S{prodCode} ");
        }
        if(StringUtils.isNotBlank(m.getProdMod())){
            sql.append("and t1.prod_mod = $S{prodMod} ");
        }
        if(StringUtils.isNotBlank(m.getTemplateType())){
            sql.append("and t.template_type = $S{templateType} ");
        }

        sql.append("order by t.template_son_type ");

        return super.findRows(sql.toString(),DataSourceProperty.IDB, params);
    }

    //查询子文档
    public SqlResult<MaterialDocument> findHisDocumentByType(SqlParam<MaterialDocument> params) throws Exception {
        StringBuilder sql = getDocumentSqlHis();
        sql.append("and t.template_type = $S{templateType} ");
        sql.append("and t.prod_code = $S{prodCode} ");
        sql.append("order by crt_date ");
        return super.findRows(sql.toString(),DataSourceProperty.IDB, params);
    }

    //查找历史信息
    public List<MaterialDocument> findMaterialDocumentHisById(MaterialDocument m) throws Exception {

        StringBuilder sql = getDocumentSqlHis();
        sql.append("and t.document_id = $S{documentId} ");

        return super.findRows(MaterialDocument.class,sql.toString(), DataSourceProperty.IDB,m);
    }

    public int addMaterialDocument(MaterialDocument params) throws Exception {
        return super.update("INSERT INTO pids_material_document (document_id,prod_code,hand_upload,template_type,template_son_type,document_name,document_path,crt_date,crt_time,crt_user) " +
                        " VALUES ($AUTOIDS{documentId},$S{prodCode},$S{handUpload},$S{templateType},$S{templateSonType},$S{documentName},$S{documentPath},$S{crtDate},$S{crtTime},$S{crtUser})",
                DataSourceProperty.IDB,params).getEffect();
    }
    public int addMaterialDocumentHis(MaterialDocument params) throws Exception {
        return super.update("INSERT INTO pids_material_document_his (document_id,prod_code,hand_upload,template_type,template_son_type,document_name,document_path,crt_date,crt_time,crt_user) " +
                        " VALUES ($S{documentId},$S{prodCode},$S{handUpload},$S{templateType},$S{templateSonType},$S{documentName},$S{documentHisPath},$S{crtDate},$S{crtTime},$S{crtUser})",
                DataSourceProperty.IDB,params).getEffect();
    }


    public int deleteMaterialDocument(MaterialDocument params) throws Exception {

        String sql = "DELETE FROM pids_material_document WHERE document_id = $S{documentId} ";

        if (StringUtils.isNotBlank(params.getTemplateType())){
            sql += " and template_type = $S{templateType} ";
        }
        if (StringUtils.isNotBlank(params.getTemplateSonType())){
            sql += " and template_son_type = $S{templateSonType}";
        }
        return super.update(sql,DataSourceProperty.IDB, params).getEffect();
    }

    public int deleteHisDocumentByType(MaterialDocument params) throws Exception {
        String sql = "DELETE FROM pids_material_document_his WHERE template_type = $S{templateType} and prod_code = $S{prodCode}";
        return super.update(sql,DataSourceProperty.IDB, params).getEffect();
    }
    public int deleteHisDocumentById(MaterialDocument params) throws Exception {
        String sql = "DELETE FROM pids_material_document_his WHERE document_id = $S{documentId}";
        return super.update(sql,DataSourceProperty.IDB, params).getEffect();
    }

    //查找文档是否存在
    public List<MaterialDocument> findMaterialDocument (MaterialDocument m) throws Exception {

        StringBuilder sql = getDocumentSql();

        if (StringUtils.isNotBlank(m.getProdCode())){
            sql.append("and t.prod_code = $S{prodCode} ");
        }
        if (StringUtils.isNotBlank(m.getTemplateType())){
            sql.append("and t.template_type = $S{templateType} ");
        }
        if (StringUtils.isNotBlank(m.getTemplateSonType())){
            sql.append("and t.template_son_type = $S{templateSonType} ");
        }
        return super.findRows(MaterialDocument.class,sql.toString(), DataSourceProperty.IDB, m );
    }


    public StringBuilder getDocumentTempSql(){
        return new StringBuilder ("select t.document_id , t.module, t.prod_code , t.template_type, t.template_son_type, t.document_name," +
                "t.document_path, t.sys_name, t.crt_date " +
                "from pids_document_temp t where 1 = 1 ");
    }

    public List<MaterialDocument> findDocumentTemp(MaterialDocument params) throws Exception {

        StringBuilder sql = getDocumentTempSql();

        if (StringUtils.isNotBlank(params.getProdCode())){
            sql.append("and t.prod_code = $S{prodCode} ");
        }
        if (StringUtils.isNotBlank(params.getTemplateType())){
            sql.append("and t.template_type = $S{templateType} ");
        }
        if (StringUtils.isNotBlank(params.getTemplateSonType())){
            sql.append("and t.template_son_type = $S{templateSonType} ");
        }
        sql.append("and t.module = '1' order by t.document_id desc ");

        return super.findRows(MaterialDocument.class,sql.toString(), DataSourceProperty.IDB,params);
    }

}
