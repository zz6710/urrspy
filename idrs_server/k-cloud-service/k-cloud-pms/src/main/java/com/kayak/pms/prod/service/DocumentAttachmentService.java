package com.kayak.pms.prod.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.graphql.model.FetcherData;
import com.kayak.utils.DateHelper;
import com.kayak.pms.printTemp.dao.PrintTempDao;
import com.kayak.pms.prod.dao.DocumentAttachmentDao;
import com.kayak.pms.prod.model.DocumentAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/1/28 14:20
 */
@Service
@APIDefine(desc = "附件信息", model = DocumentAttachment.class)
public class DocumentAttachmentService {
    @Autowired
    private DocumentAttachmentDao attachmentDao;

    @Autowired
    private PrintTempDao tempDao;

    @API(desc = "新增文件信息", auth = APIAuth.NO,operation = APIOperation.INSTER)
    public int addDocumentAttachmentInfo(DocumentAttachment documentAttachment) throws Exception {
        documentAttachment.setInputuser(SysUtil.getSysUserParamValue("sys_user_userid").toString());
        documentAttachment.setCrtDate(DateHelper.getCurrentDate());
        documentAttachment.setCrtTime(DateHelper.getCurrentTime());
        return attachmentDao.addDocumentAttachmentInfo(documentAttachment);
    }
    @API(desc="插入附件信息",auth=APIAuth.NO,operation = APIOperation.INSTER)
    public int addDocumentAttachmentInfoList(List<DocumentAttachment> documentAttachmentList) throws Exception {
        AtomicInteger flag = new AtomicInteger();
        String sys_user_userid = (String) SysUtil.getSysUserParamValue("sys_user_userid");

            for (DocumentAttachment documentAttachment : documentAttachmentList) {
                documentAttachment.setInputuser(sys_user_userid);
                documentAttachment.setCrtDate(DateHelper.getCurrentDate());
                documentAttachment.setCrtTime(DateHelper.getCurrentTime());
                Integer count = attachmentDao.findIsExists(documentAttachment);
                if(count>0){
                    flag.addAndGet(attachmentDao.updateDocumentAttachmentInfo(documentAttachment));
                }else{
                    flag.addAndGet(attachmentDao.addDocumentAttachmentInfo(documentAttachment));
                }
            }
        return flag.get();
    }
    @API(desc = "查询附件信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Map<String, Object>> getAttachmentInfo(SqlParam<DocumentAttachment> param) throws Exception {
       /* PrintTemp printTemp = new PrintTemp();
        printTemp.setDistributorCode(param.getModel().getDistributorCode());
        printTemp.setTempType(param.getModel().getDocumentType());
        printTemp = tempDao.getPrintTempByDocumentType(printTemp);*/
        return attachmentDao.getAttachmentInfo(param);
    }

    @API(desc="插入附件信息",auth=APIAuth.NO,operation = APIOperation.INSTER)
    public void addDocumentAttachment(DocumentAttachment documentAttachment) throws Exception {
        documentAttachment.setInputuser((String)SysUtil.getSysUserParamValue("sys_user_userid"));
        documentAttachment.setCrtDate(DateHelper.getCurrentDate());
        documentAttachment.setCrtTime(DateHelper.getCurrentTime());
        attachmentDao.addDocumentAttachmentInfo(documentAttachment);
    }

    @API(desc = "删除会议附件记录", auth = APIAuth.YES,operation = APIOperation.DELETE)
    public String deleteFile(SqlParam<DocumentAttachment> param) throws Exception {
        deleteFileIsExist(param.getModel().getPath());
        DaoUtil.doTrans(() ->{
            attachmentDao.delete(param);
        });
        return RequestSupport.updateReturnJson(true, "附件删除成功", null).toString();
    }


    public String deleteEscrowAgreementAttached(DocumentAttachment param) throws Exception {
         attachmentDao.deleteEscrowAgreementAttached(param);
        return RequestSupport.updateReturnJson(true, "附件删除成功", null).toString();
    }

    @API(desc = "查询产品评分附件信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<DocumentAttachment> getCreateMeetingAttachmentInfos(SqlParam<DocumentAttachment> param) throws Exception {
        param.getModel().setAttachmentType("9999");
        return attachmentDao.findMeetCreateAttachments(param);
    }

    @API(desc = "附件信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<DocumentAttachment> findAttachments(SqlParam<DocumentAttachment> param) throws Exception {
        return attachmentDao.findAttachments(param);
    }

    @API(desc = "创意附件信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<DocumentAttachment> findOriginalityAttachments(SqlParam<DocumentAttachment> param) throws Exception {
        return attachmentDao.findAttachments(param);
    }

  //更新附件信息
    public String updateFile(SqlParam<DocumentAttachment> param) throws Exception {
        deleteFileIsExist(param.getModel().getPath());
        DaoUtil.doTrans(() ->{
            attachmentDao.delete(param);
        });
        return RequestSupport.updateReturnJson(true, "附件删除成功", null).toString();
    }
    /**
     * 通过文件名称判断该文件是否存在,存在则删除
     * @param
     * @param filePath 文件路径
     */
    private Boolean deleteFileIsExist(String filePath) {
        boolean delete = false;
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            delete = file.delete();
        }
        return delete;
    }

    public Boolean deleteFiles(String fileName,String attchType) throws Exception {

        attachmentDao.delete(fileName,attchType);
        return  true;
    }

    public Boolean deleteSeminarFile(String parentId,String attchType) throws Exception {

        attachmentDao.deleteSeminarFile(parentId,attchType);
        return  true;
    }

    //上传备案审批表
    public int addOrUpdateDocumentAttachment(List<DocumentAttachment> documentAttachmentList) throws Exception {
        AtomicInteger flag = new AtomicInteger();
        String sys_user_userid = (String) SysUtil.getSysUserParamValue("sys_user_userid");
        DaoUtil.doTrans(()->{
            for (DocumentAttachment documentAttachment : documentAttachmentList) {
            	Map<String,Object> param = BeanUtil.beanToMap(documentAttachment);
            	SqlParam<DocumentAttachment> sqlParam = new FetcherData<>(param,DocumentAttachment.class);
            	if(attachmentDao.getRiskScoreFiletInfo(sqlParam).getRows().size()>0) {
            		attachmentDao.deleteSeminarFile(documentAttachment.getParentId(),documentAttachment.getAttachmentType());
            	}
            	documentAttachment.setInputuser(sys_user_userid);
                documentAttachment.setCrtDate(DateHelper.getCurrentDate());
                documentAttachment.setCrtTime(DateHelper.getCurrentTime());
                flag.addAndGet(attachmentDao.addDocumentAttachmentInfo(documentAttachment));
            }
        });
        return flag.get();
    }
    //上传用印扫描件
    public int addOrUpdateDocumentAttachment(List<DocumentAttachment> documentAttachmentList,String t8_prod_document_version_id) throws Exception {
        AtomicInteger flag = new AtomicInteger();
        String sys_user_userid = (String) SysUtil.getSysUserParamValue("sys_user_userid");
        DaoUtil.doTrans(()->{
            for (DocumentAttachment documentAttachment : documentAttachmentList) {
            	Map<String,Object> param = BeanUtil.beanToMap(documentAttachment);
            	SqlParam<DocumentAttachment> sqlParam = new FetcherData<>(param,DocumentAttachment.class);
            	if(attachmentDao.getRiskScoreFiletInfo(sqlParam).getRows().size()>0) {
            		attachmentDao.deleteSeminarFile(documentAttachment.getParentId(),documentAttachment.getAttachmentType());
            	}
            	documentAttachment.setInputuser(sys_user_userid);
                documentAttachment.setCrtDate(DateHelper.getCurrentDate());
                documentAttachment.setCrtTime(DateHelper.getCurrentTime());
                flag.addAndGet(attachmentDao.addDocumentAttachmentInfo(documentAttachment));
            }
            //修改用印状态
            if(t8_prod_document_version_id!=null) {
            	attachmentDao.updAudiopinionStatus(t8_prod_document_version_id,4);
            }
        });
        return flag.get();
    }


    public SqlResult<DocumentAttachment> findAttachbyCodeType(SqlParam<DocumentAttachment> param) throws Exception {
        return attachmentDao.findAttachbyCodeType(param);
    }

    //上传创设方案材料
    public int addCreatePlanAttachment(List<DocumentAttachment> documentAttachmentList) throws Exception {
        AtomicInteger flag = new AtomicInteger();
        String sys_user_userid = (String) SysUtil.getSysUserParamValue("sys_user_userid");
        DaoUtil.doTrans(()->{
            for (DocumentAttachment documentAttachment : documentAttachmentList) {
            	documentAttachment.setInputuser(sys_user_userid);
                documentAttachment.setCrtDate(DateHelper.getCurrentDate());
                documentAttachment.setCrtTime(DateHelper.getCurrentTime());
                flag.addAndGet(attachmentDao.addDocumentAttachmentInfo(documentAttachment));
            }
        });
        return flag.get();
    }
    //上传产品分红方案模板
    public void addPlanFile(DocumentAttachment  attachement)throws Exception {
    	DaoUtil.doTrans(()->{
    		attachmentDao.deletePlanFile(attachement.getProdCode(), attachement.getAttachmentType());
    		attachmentDao.addDocumentAttachmentInfo(attachement);
        });
    }
}
