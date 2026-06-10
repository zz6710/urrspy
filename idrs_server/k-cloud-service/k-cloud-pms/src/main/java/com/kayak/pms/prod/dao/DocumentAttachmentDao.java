package com.kayak.pms.prod.dao;

import com.google.common.base.Strings;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.DocumentAttachment;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/1/28 14:29
 */
@Repository
public class DocumentAttachmentDao extends ComnDao {

    public int findDocumentAttachmentCount(String prodCode) throws Exception {
        String sql=" select count(*) count from t8_prod_document_attachment where prod_code = $S{prodCode}";
        return super.findRow(sql,prodCode).getInteger("count");
    }

    public int addDocumentAttachmentInfo(DocumentAttachment documentAttachment) throws Exception {
        if("20".equals(documentAttachment.getAttachmentType())){
            super.update(
                    "delete from t8_prod_document_attachment where parent_id = '"+documentAttachment.getParentId()+"' and attachment_type='20'",
                    documentAttachment);
        }
        return super.update("insert into t8_prod_document_attachment (id,prod_document_id,prod_code, parent_id,path, file_name, attachment_type,t8_trutee_info_id,distributor_code, inputuser, crt_date, crt_time,approval_advice) values\n" +
                "($AUTOIDS{attachmentId},$S{prodDocumentId},$S{prodCode},$S{parentId}, $S{path}, $S{fileName},$S{attachmentType},$S{t8TruteeInfoId},$S{distributorCode},$S{inputuser}, $S{crtDate},$S{crtTime},$S{approvalAdvice})", documentAttachment).getEffect();
    }

    public SqlResult<Map<String, Object>> getAttachmentInfo(SqlParam<DocumentAttachment> param) throws Exception {
        StringBuilder sql = new StringBuilder("select a.id,a.parent_id,a.path,a.file_name,a.attachment_type,a.crt_date,a.crt_time\n" +
                "from t8_prod_document_attachment a\n" +
                "         left join t8_prod_info b on a.parent_id = b.id\n" +
                "where b.prod_code = $S{prodCode}\n" +
                "  and a.attachment_type in ($U{attachmentType})");
        if (StringUtils.isNotBlank(param.getModel().getDistributorCode())) {
            sql.append(" and a.distributor_code = $S{distributorCode}");
        }
        if (StringUtils.isNotBlank(param.getModel().getT8TruteeInfoId())) {
            sql.append(" and a.t8_trutee_info_id = $S{t8TruteeInfoId}");
        }
        SqlResult<Map<String, Object>> mapSqlResult = SqlUtils.sqlPackage(sql.toString(), DataSourceProperty.PUB, param, this);
        return mapSqlResult;
    }

    public int findIsExists(DocumentAttachment documentAttachment) throws Exception{
        StringBuilder sql = new StringBuilder("select count(*) count from t8_prod_document_attachment where attachment_type=$S{attachmentType} and " +
                " parent_id=$S{parentId} and file_name=$S{fileName} ");
        if (StringUtils.isNotBlank(documentAttachment.getDistributorCode())) {
            sql.append(" and distributor_code=$S{distributorCode}");
        }
        if (StringUtils.isNotBlank(documentAttachment.getT8TruteeInfoId())) {
            sql.append(" and t8_trutee_info_id=$S{t8TruteeInfoId}");
        }
        return super.findRow(sql.toString(), documentAttachment).getInteger("count");
    }

    public int updateDocumentAttachmentInfo(DocumentAttachment documentAttachment) throws Exception {

       return super.update("UPDATE t8_prod_document_attachment SET inputuser=$S{inputuser}, crt_date=$S{crtDate}, crt_time=$S{crtTime} WHERE attachment_type =$S{attachmentType}" +
                " AND file_name =$S{fileName} and parent_id=$S{parentId} ",documentAttachment).getEffect();
    }


    public SqlResult<Map<String, Object>> getRiskScoreFiletInfo(SqlParam<DocumentAttachment> param) throws Exception {
        String sql = "select path,file_name from t8_prod_document_attachment where prod_code = $S{prodCode} and attachment_type = $S{attachmentType}";
        SqlResult<Map<String, Object>> sqlResult = SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, param, this);
        return sqlResult;
    }

    //删除会议附件记录
    public UpdateResult delete(SqlParam<DocumentAttachment> params) throws Exception {
        return super.update(
                "delete from t8_prod_document_attachment where id = $S{id}",
                params.getModel());

    }

    //通过id删除会议附件记录
    public UpdateResult delete(String params) throws Exception {
        return super.update(
                "delete from t8_prod_document_attachment where id = '"+params+"'");

    }

    //查询创设会议附件信息
    public SqlResult<DocumentAttachment> findMeetCreateAttachments(SqlParam<DocumentAttachment> params) throws Exception {

        return super.findRows("SELECT\n" +
                "\ttpda.id,\n" +
                "\ttpda.parent_id,\n" +
                "\ttpda.file_name,\n" +
                "\ttpda.attachment_type,\n" +
                "\ttpda.path,\n" +
                "\ttpda.crt_date,\n" +
                "\ttpda.crt_time \n" +
                "FROM\n" +
                "\tt8_create_meeting tcm\n" +
                "\tLEFT JOIN t8_prod_document_attachment tpda ON tcm.id = tpda.parent_id \n" +
                "WHERE\n" +
                "\ttpda.attachment_type = $S{attachmentType} and tpda.parent_id=$S{parentId}"  , params);

    }
    //查询附件信息
    public SqlResult<DocumentAttachment> findAttachments(SqlParam<DocumentAttachment> params) throws Exception {

    	StringBuffer sql =  new StringBuffer ("SELECT " +
                "tpda.id," +
                "tpda.parent_id," +
                "tpda.file_name," +
                "tpda.attachment_type," +
                "tpda.path," +
                "tpda.crt_date," +
                "tpda.crt_time " +
                "FROM t8_prod_document_attachment tpda " +
                "WHERE  tpda.parent_id=$S{parentId}" ) ;
    	if(params.getModel()!=null&&!Strings.isNullOrEmpty(params.getModel().getProdCode()))
    		sql.append(" and prod_code =$S{prodCode}");
        if(params.getModel()!=null&&!Strings.isNullOrEmpty(params.getModel().getAttachmentType()))
            sql.append(" and attachment_type =$S{attachmentType}");
        return super.findRows(sql.toString(), params);

    }
    public UpdateResult deleteEscrowAgreementAttached(DocumentAttachment param) throws Exception {
        StringBuilder sql = new StringBuilder("delete from t8_prod_document_attachment where parent_id = $S{parentId} and attachment_type = $S{attachmentType} and file_name = $S{fileName}");
        if (StringUtils.isNotBlank(param.getT8TruteeInfoId())) {
            sql.append(" and t8_trutee_info_id=$S{t8TruteeInfoId}");
        }
        if(StringUtils.isNotBlank(param.getDistributorCode())){
            sql.append(" and distributor_code=$S{distributorCode}");
        }

        return super.update(sql.toString(), param);
    }
  //删除
    public UpdateResult delete(String fileName,String attchType) throws Exception {
        return super.update(
                "delete from t8_prod_document_attachment where file_name = '"+fileName+"' and attachment_type='"+attchType+"'",
               null);

    }

    //删除
    public UpdateResult deleteSeminarFile(String parentId,String attchType) throws Exception {
        return super.update(
                "delete from t8_prod_document_attachment where parent_id = '"+parentId+"' and attachment_type='"+attchType+"'",
               null);

    }
  //查询附件信息
    public SqlResult<DocumentAttachment> findAttachbyCodeType(SqlParam<DocumentAttachment> params) throws Exception {
    	StringBuffer sql =  new StringBuffer ("SELECT " +
                "tpda.id," +
                "tpda.parent_id," +
                "tpda.file_name," +
                "tpda.attachment_type," +
                "tpda.path," +
                "tpda.crt_date," +
                "tpda.crt_time " +
                "FROM t8_prod_document_attachment tpda " +
                "WHERE 1=1 " ) ;
    	if(params.getModel()!=null&&!Strings.isNullOrEmpty(params.getModel().getAttachmentType()))
    		sql.append(" and tpda.attachment_type =$S{attachmentType}");
    	if(params.getModel()!=null&&!Strings.isNullOrEmpty(params.getModel().getProdCode()))
    		sql.append(" and tpda.prod_code=$S{prodCode}");
        return super.findRows(sql.toString(), params);

    }

    //修改状态为已用印
    public int updAudiopinionStatus(String t8ProdDocumentVersionId, int document_status) throws Exception {
        return super.update("update t8_prod_document_version v set v.document_status="+document_status+" where v.id='"+t8ProdDocumentVersionId+"'").getEffect();
    }

    public List<SqlRow> getAttachmentInfoByParentId (DocumentAttachment documentAttachment) throws Exception {
        return  super.findRows("select a.id, a.parent_id, a.path, a.file_name, a.attachment_type, a.distributor_code, a.t8_trutee_info_id,b.prod_code\n" +
                "from t8_prod_document_attachment a\n" +
                "         left join t8_prod_info b on a.parent_id = b.id where a.attachment_type in ('1','2','4','6','7') and parent_id=$S{parentId}", documentAttachment);
    }
    
  //删除
    public UpdateResult deletePlanFile(String prodCode,String attchType) throws Exception {
        return super.update(
                "delete from t8_prod_document_attachment where prod_code = '"+prodCode+"' and attachment_type='"+attchType+"'",
               null);

    }

}
