package com.kayak.pms.declare.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;

import com.kayak.pms.declare.dao.MaterialTemplateDao;
import com.kayak.pms.declare.model.MaterialDocument;
import com.kayak.pms.declare.model.MaterialTemplate;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.FileUtil;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@APIDefine(desc = "申报模板服务", model = MaterialTemplate.class)
public class MaterialTemplateService {

    @Autowired
    private MaterialTemplateDao materialTemplateDao;

    @API(desc = "父模板数据字典", auth = APIAuth.NO)
    public SqlResult<MaterialTemplate> getTemplateTypeDict(SqlParam<MaterialTemplate> params) throws Exception {
        return materialTemplateDao.getTemplateTypeDict(params);
    }
    @API(desc = "子模板数据字典", auth = APIAuth.NO)
    public SqlResult<MaterialTemplate> getTemplateSonTypeDict(SqlParam<MaterialTemplate> params) throws Exception {
        return materialTemplateDao.getTemplateSonTypeDict(params);
    }

    @API(desc = "查询模板", auth = APIAuth.NO)
    public SqlResult<MaterialTemplate> findMaterialTemplate(SqlParam<MaterialTemplate> params) throws Exception {
        params.setMakeSql(false);
        return materialTemplateDao.findMaterialTemplate(params);
    }

    @API(desc = "添加模板", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addMaterialTemplate(SqlParam<MaterialTemplate> params){
        return addMaterialTemplate(params.getModel());
    }
    public String addMaterialTemplate(MaterialTemplate params){
        try {
            materialTemplateDao.addMaterialTemplate(params);
            return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "修改模板", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateMaterialTemplate(SqlParam<MaterialTemplate> params) throws Exception {
        return  updateMaterialTemplate(params.getModel());
    }

    public String updateMaterialTemplate(MaterialTemplate params) throws Exception {

        try {
            materialTemplateDao.updateMaterialTemplate(params);
            return RequestSupport.updateReturnJson(true,"修改成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"修改失败！" + e.getMessage() ,null).toString();
        }
    }


    @API(desc = "启用模板", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String startTemplate(SqlParam<MaterialTemplate> params) throws Exception {
        try {
            params.getModel().setTemplateStart("1");
            materialTemplateDao.updateTemplateStart(params.getModel());
            return RequestSupport.updateReturnJson(true,"启用成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"启用失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "停用模板", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String stopTemplate(SqlParam<MaterialTemplate> params) throws Exception {
        try {
            params.getModel().setTemplateStart("0");
            materialTemplateDao.updateTemplateStart(params.getModel());
            return RequestSupport.updateReturnJson(true,"停用成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"停用失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "查询版本", auth = APIAuth.NO)
    public SqlResult<MaterialTemplate> findMaterialVersion(SqlParam<MaterialTemplate> params) throws Exception {
        params.setMakeSql(false);
        return materialTemplateDao.findMaterialVersion(params);
    }

    @API(desc = "添加模板版本", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addMaterialVersion(SqlParam<MaterialTemplate> params){
        return addMaterialVersion(params.getModel());
    }
    public String addMaterialVersion(MaterialTemplate params){
        try {
            materialTemplateDao.addMaterialVersion(params);
            return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
        }
    }
    @API(desc = "启用版本", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String startVersion(SqlParam<MaterialTemplate> params) throws Exception {
        try {
            params.getModel().setVersionStart("1");
            materialTemplateDao.updateVersionStart(params.getModel());
            return RequestSupport.updateReturnJson(true,"启用成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"启用失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "停用版本", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String stopVersion(SqlParam<MaterialTemplate> params) throws Exception {
        try {
            params.getModel().setVersionStart("0");
            materialTemplateDao.updateVersionStart(params.getModel());
            return RequestSupport.updateReturnJson(true,"停用成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"停用失败！" + e.getMessage() ,null).toString();
        }
    }
    @API(desc = "模板下载", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public void MaterialDownload(SqlParam<MaterialTemplate> params) throws Exception {
        //权限控制
    }


    @API(desc = "下载权限", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public void downloadDocument(SqlParam<MaterialDocument> params){
        //下载权限
    }


    @API(desc = "查询sql配置", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<MaterialTemplate> findMaterialConfig(SqlParam<MaterialTemplate> params) throws Exception {
        params.setMakeSql(false);
        return materialTemplateDao.findMaterialConfig(params);
    }
    @API(desc = "添加配置", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addMaterialConfig(SqlParam<MaterialTemplate> params){
        try {
            materialTemplateDao.addMaterialConfig(params.getModel());
            return RequestSupport.updateReturnJson(true,"停用成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"停用失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "删除整个模板", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String deleteMaterialTemplate(SqlParam<MaterialTemplate> params){
        try {
            //查询模板下的所有版本
            List<MaterialTemplate> l = materialTemplateDao.findMaterialVersion(params).getRows();

            DaoUtil.doTrans(() -> {

                for (MaterialTemplate m : l) {
                    deleteMaterialVersion(m);
                }

                materialTemplateDao.deleteMaterialTemplate(params.getModel());

            });

            return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "删除版本", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String deleteMaterialVersion(SqlParam<MaterialTemplate> params){
        try {
            MaterialTemplate m  = params.getModel();
            deleteMaterialVersion(m);
            return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
        }
    }

    public void deleteMaterialVersion (MaterialTemplate m) throws Exception {
        String f = m.getVersionPath();

        File temp = new File(f);
        //删除文件
        if (temp.exists() && temp.isFile() && !temp.delete())
            throw new Exception("文件["+m.getVersionName()+"]删除失败！！！！");

        materialTemplateDao.deleteMaterialVersion(m);
    }

    @API(desc = "删除配置", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String deleteMaterialConfig(SqlParam<MaterialTemplate> params){
        try {
            materialTemplateDao.deleteMaterialConfig(params.getModel());
            return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
        }
    }

    //文件转存
    public void saveMaterialTemplate (MultipartFile file , Map<String, Object> params) throws Exception {

        String fileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        //得到存储对象，用于保存信息
        params = this.getLocalPath(params);
        String filePath = (String) params.get("filePath");
        List<MaterialTemplate> listVersion = (List<MaterialTemplate>) params.get("listVersion");
        MaterialTemplate m = (MaterialTemplate) params.get("MaterialTemplate");
        //转存文件
        String savePath = FileUtil.fileSaveToLocal(file, fileName, filePath);
        //数据保存
        m.setVersionName(fileName);
        m.setVersionPath(savePath);
        m.setTemplateStart("1");
        m.setVersionStart("1");
        //m.setVersionNum(version);
        m.setTemplateName((String)params.get("templateName"));
        m.setTruteeBank((String)params.get("truteeBank"));
        m.setProdMod((String)params.get("prodMod"));
        m.setTemplateType((String)params.get("templateType"));
        m.setTemplateSonType((String)params.get("templateSonType"));
        m.setRemark((String)params.get("remark"));
        m.setCrtDate(DateUtil.getNowDate());
        m.setCrtTime(DateUtil.getNowTime());
        m.setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_loginname"));

        if(listVersion.size() == 0){//存储模板主数据
            m.setTemplateId(materialTemplateDao.addMaterialTemplate(m));
        }else{//修改模板
            m.setUpdDate(DateUtil.getNowDate());
            m.setUpdTime(DateUtil.getNowTime());
            m.setUpdUser((String) SysUtil.getSysUserParamValue("sys_user_loginname"));
            materialTemplateDao.updateMaterialTemplate(m);
        }
        String remotePath = "";
        //远端sftp文件服务器根路径
        remotePath = SysUtil.getSystemParamsByParaid("70000010012");
        //文件转存到指定文件目录
        FileTransfer transfer=new FileTransferHelpler().getTransfer();
        transfer.uploadFileAndDisconnect(savePath,remotePath+File.separator+m.getTemplateId()+File.separator+fileName);

        //存储版本信息
        materialTemplateDao.addMaterialVersion(m);

    }

    @API(desc = "获取本地存储路径", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public Map<String, Object> getLocalPath(Map<String, Object> params) throws Exception {
        MaterialTemplate m =  new MaterialTemplate();
        List<MaterialTemplate> listVersion = materialTemplateDao.findMaterialVersion(params);
        //保存文件根目录
        String root = FileUtil.getFileStorePath();
        //文件存储全路径：文件根目录 + “document/template/” + 产品模式 + 模板类型 + 文件版本
        String filePath = root + FileUtil.DECLAREPATH + File.separator + FileUtil.TEMPLATEPATH;

        //参数决定路径部分
        if (!(params.get("prodMod") == null) && !params.get("prodMod").equals("")){
            filePath += File.separator + params.get("prodMod");
        }

        if (!(params.get("templateType") == null) && !params.get("templateType").equals("")){
            filePath += File.separator + params.get("templateType");
        }

        if (!(params.get("templateSonType") == null) && !params.get("templateSonType").equals("")){
            filePath += File.separator + params.get("templateSonType");
        }
        if (!(params.get("truteeBank") == null) && !params.get("truteeBank").equals("")){
            filePath += File.separator + params.get("truteeBank");
        }

        //获取版本号
        String version = "V1";
        m.setVersionNum(version);
        if(listVersion.size() > 0){
            m = listVersion.get(0);
            version = m.getVersionNum();
            version = "V" + (Integer.parseInt(version.substring(1)) + 1);
            m.setVersionNum(version);
        }
        params.put("MaterialTemplate",m);
        filePath += File.separator + version;
        params.put("filePath",filePath);
        params.put("listVersion",listVersion);
        return params;
    }


}
