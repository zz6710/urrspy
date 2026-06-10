package com.kayak.pms.declare.service;

import com.aspose.words.Document;
import com.aspose.words.Range;
import com.google.common.io.Files;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;


import com.kayak.pms.T82.dao.T8DictDao;
import com.kayak.pms.declare.dao.MaterialDocumentDao;
import com.kayak.pms.declare.dao.MaterialTemplateDao;
import com.kayak.pms.declare.model.MaterialDocument;
import com.kayak.pms.declare.model.MaterialTemplate;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.FileUtil;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@APIDefine(desc = "申报文档服务", model = MaterialDocument.class)
public class MaterialDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialDocumentService.class);

    @Autowired
    MaterialDocumentDao materialDocumentDao;

    @Autowired
    MaterialTemplateDao materialTemplateDao;

    @Autowired
    T8DictDao t8DictDao;

    @API(desc = "查询", auth = APIAuth.NO)
    public SqlResult<MaterialDocument> findProdInfo(SqlParam<MaterialDocument> params) throws Exception {
        params.setMakeSql(false);
        return materialDocumentDao.findProdInfo(params);
    }

    @API(desc = "查询", auth = APIAuth.NO)
    public SqlResult<MaterialDocument> findMaterialDocument(SqlParam<MaterialDocument> params) throws Exception {
        params.setMakeSql(false);
        return materialDocumentDao.findMaterialDocument(params);
    }
    @API(desc = "查询销售文件", auth = APIAuth.NO)
    public SqlResult<MaterialDocument> findSalesDocument(SqlParam<MaterialDocument> params) throws Exception {
        params.setMakeSql(false);
        return materialDocumentDao.findSalesDocument(params);
    }

    @API(desc = "查询历史", auth = APIAuth.NO)
    public SqlResult<MaterialDocument> findHisDocumentByType(SqlParam<MaterialDocument> params) throws Exception {
        params.setMakeSql(false);
        return materialDocumentDao.findHisDocumentByType(params);
    }
    @API(desc = "删除历史（单文件）", auth = APIAuth.NO)
    public String deleteHisDocumentById(SqlParam<MaterialDocument> params) throws Exception {
        try {
            //先删除文档
            String documentPath = params.getModel().getDocumentPath();
            FileUtil.delAllFile(documentPath);
            //在删数据
            materialDocumentDao.deleteHisDocumentById(params.getModel());
            return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
        }
    }
    @API(desc = "删除历史（整个类型）", auth = APIAuth.NO)
    public String deleteHisDocumentByType(SqlParam<MaterialDocument> params) throws Exception {
        MaterialDocument m = params.getModel();
        try {
            //先删除文档
            //文件存储全路径：文件根目录 + “declare/history/” + 产品代码 + 模板类型
            String newPath = FileUtil.getFileStorePath() + FileUtil.DECLAREPATH + File.separator + FileUtil.HISTORYPATH;
            //参数决定路径部分:产品代码 + 模板类型 + ID
            newPath += File.separator + m.getProdCode() + File.separator + m.getTemplateType();
            FileUtil.delAllFile(newPath);
            //在删数据
            materialDocumentDao.deleteHisDocumentByType(m);
            return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
        }
    }
    @API(desc = "回滚文档", auth = APIAuth.NO)
    public String rollBackMaterialDocument(SqlParam<MaterialDocument> params) throws Exception {
        MaterialDocument m = params.getModel();
        try {
            //先备份文档到历史记录
            backupsMaterialDocument(m);
            //将历史文档返回到当前使用文档
            rollBackMaterialDocument(m);
            return RequestSupport.updateReturnJson(true,"操作成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"操作失败！" + e.getMessage() ,null).toString();
        }
    }

    //备份到历史文件夹
    public MaterialDocument backupsMaterialDocument(MaterialDocument m) throws Exception {

        List<MaterialDocument> docList = materialDocumentDao.findMaterialDocument(m);

        //没有数据就直接返回对象
        if (docList.size() <= 0){
            return m;
        }

        m = docList.get(0);

        String oldPath = m.getDocumentPath();
        File temp = new File(oldPath);
        //没有文件只删除数据
        if(!temp.exists() || !temp.isFile()){
            //删除文档数据即可
            materialDocumentDao.deleteMaterialDocument(m);
            return m;
        }

        //转存文件到备份，并删除当前文件
        //文件存储全路径：文件根目录 + “declare/history/” + 产品代码 + 模板类型
        String newPath = FileUtil.getFileStorePath() + FileUtil.DECLAREPATH + File.separator + FileUtil.HISTORYPATH;
        //参数决定路径部分:产品代码 + 模板类型 + ID
        newPath += File.separator + m.getProdCode() + File.separator + m.getTemplateType() + File.separator +
                m.getDocumentId() + File.separator ;

        File f = new File(newPath);

        //申报文档生成后上传到远程服务器
        String remotePath = "";
        //远端sftp文件服务器根路径
        remotePath = SysUtil.getSystemParamsByParaid("70000010013");
        //文件转存到指定文件目录
        FileTransfer transfer=new FileTransferHelpler().getTransfer();
        transfer.deleteFileAndDisconnect(remotePath+ m.getProdCode() + File.separator + m.getTemplateType()+File.separator+m.getDocumentName());
        if (!f.exists() )
            f.mkdirs();

        Files.copy(temp,new File(newPath , m.getDocumentName()));

        //删除数据
        if (!temp.delete())
            logger.error("文件删除失败！！！！");

        m.setDocumentHisPath(newPath + m.getDocumentName());
        //新增历史数据
        materialDocumentDao.addMaterialDocumentHis(m);
        //删除之前的数据和文件
        materialDocumentDao.deleteMaterialDocument(m);

        return m;
    }

    //回滚文件
    public void rollBackMaterialDocument(MaterialDocument m) throws Exception {

        //获取历史
        List<MaterialDocument> docList = materialDocumentDao.findMaterialDocumentHisById(m);

        //没有数据就直接新增默认数据
        if (docList.size() <= 0){
            materialDocumentDao.addMaterialDocument(m);
            return;
        }

        m = docList.get(0);

        String savePath = FileUtil.getFileStorePath() + FileUtil.DECLAREPATH + File.separator + FileUtil.DOCUMENTPATH + File.separator
                + m.getProdCode()+ File.separator + m.getTemplateType() + File.separator + m.getDocumentName();

        File newTemp = new File(savePath);
        File oldTemp = new File(m.getDocumentPath());

        if(oldTemp.isFile()){

            Files.copy(oldTemp , newTemp);
            //删除数据
            if (!oldTemp.delete())
                logger.error("文件删除失败！！！！");
        }
        m.setDocumentPath(savePath);
        materialDocumentDao.deleteHisDocumentById(m);
        materialDocumentDao.addMaterialDocument(m);
    }

    @API(desc = "下载权限", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public void downloadDocument(SqlParam<MaterialDocument> params){
        //下载权限
    }
    @API(desc = "上传权限", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public void uploadDocument(SqlParam<MaterialDocument> params){
        //上传权限
    }
    public void uploadDocument(MultipartFile file , Map<String, Object> params) throws Exception {

        String fileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();

        MaterialDocument m = new MaterialDocument();
        //给对象先复制页面参数
        m.setTemplateType((String)params.get("templateType"));
        m.setProdCode((String)params.get("prodCode"));
        m.setTemplateSonType((String)params.get("templateSonType"));
        //文件备份到历史
        m = backupsMaterialDocument(m);

        //申报文档生成后上传到远程服务器
        String remotePath = "";
        //远端sftp文件服务器根路径
        remotePath = SysUtil.getSystemParamsByParaid("70000010013");
        //文件转存到指定文件目录
        FileTransfer transfer=new FileTransferHelpler().getTransfer();


        try {
            //保存文件 文件存储全路径：文件根目录 + “declare/template/” + 产品代码 + 模板类型
            String filePath = FileUtil.getFileStorePath() + FileUtil.DECLAREPATH + File.separator + FileUtil.DOCUMENTPATH;
            //参数决定路径部分
            filePath += File.separator + params.get("prodCode") + File.separator + params.get("templateType");

            //转存文件
            String savePath = FileUtil.fileSaveToLocal(file, fileName, filePath);

            transfer.uploadFileAndDisconnect(savePath,remotePath+params.get("prodCode")+ File.separator  + params.get("templateType")+ File.separator + fileName);

            //存储数据
            m.setHandUpload("1");
            m.setDocumentName(fileName);
            m.setDocumentPath(savePath);
            m.setCrtDate(DateUtil.getNowDate());
            m.setCrtTime(DateUtil.getNowTime());
            m.setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_loginname"));
            materialDocumentDao.addMaterialDocument(m);
        }catch (Exception e){
            //回滚文件
            rollBackMaterialDocument(m);
            throw new Exception(e.getMessage());
        }finally {
            //文件操作信息

        }

    }


    @API(desc = "重新生成文档", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addMaterialDocument(SqlParam<MaterialDocument> params) throws Exception {
        //组建模板数据
        MaterialDocument d = params.getModel();

        try {
            if (StringUtils.isBlank(d.getProdCode())){
                throw new Exception("请输入产品代码！");
            }
            if (StringUtils.isBlank(d.getProdMod())){
                throw new Exception("请选择产品模式！");
            }
            if (StringUtils.isBlank(d.getTemplateType())){
                throw new Exception("请选择模板类型！");
            }

            boolean bool = findMaterialDocument(d);

            return RequestSupport.updateReturnJson(bool,bool ? "操作成功" : "操作失败！未找到对应模板。",null).toString();

        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"操作失败！" + e.getMessage() ,null).toString();
        }



    }

    @API(desc = "一键生成文档", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addMaterialDocumentAll(SqlParam<MaterialDocument> params){

        try {
            //组建模板数据
            MaterialDocument d = params.getModel();

            if (StringUtils.isBlank(d.getProdCode())){
                throw new Exception("请输入产品代码！");
            }
            if (StringUtils.isBlank(d.getProdMod())){
                throw new Exception("请选择产品模式！");
            }

            findMaterialDocument(d);

            return RequestSupport.updateReturnJson(true,"操作成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"操作失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "模板下载", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public void MaterialDownload(SqlParam<MaterialTemplate> params) throws Exception {
        //权限控制
    }

    public boolean findMaterialDocument(MaterialDocument d) throws Exception {
        //获取模板类型
        List<MaterialTemplate> templateTypes =  materialTemplateDao.findTemplateType(d.getTemplateType());

        //获取模板类型最大版本模板
        List<MaterialTemplate> maxVersion = materialTemplateDao.findMaxVersion(d.getTemplateType());

        Map<String , MaterialTemplate> maxVersionMap = new HashMap<>();

        for (MaterialTemplate v : maxVersion ) {
            //key为产品模板类型+子类型+产品模式+托管行
            String key = v.getTemplateType() + " " + v.getTemplateSonType() + " " + v.getProdMod() + " " + v.getTruteeBank();
            maxVersionMap.put(key,v);
        }

        boolean bool = true;

        for (MaterialTemplate t : templateTypes) {
            boolean b = addMaterialDocument( d , t , maxVersionMap);
            bool = bool && b ;
        }

        return bool;
    }

    public boolean addMaterialDocument(MaterialDocument d , MaterialTemplate t , Map<String , MaterialTemplate> maxVersionMap) throws Exception {

        SqlRow res = t8DictDao.findTempTypeByDict("pids_trutee_bank",d.getTruteeBank());
        if(res !=null){
            d.setTruteeBankKey(res.getString("itemkey"));
        }

        boolean bool = false;

        d.setTemplateType(t.getTemplateType());
        d.setTemplateSonType(t.getTemplateSonType());

        //获取满足要求的模板：先根据产品模式取最大版本，没有就取通用模板
        String key1 = nullToString(t.getTemplateType()) + " " + nullToString(t.getTemplateSonType()) + " " + nullToString(d.getProdMod()) + " " + nullToString(d.getTruteeBankKey());
        String key2 = nullToString(t.getTemplateType()) + " " + nullToString(t.getTemplateSonType()) + " " + nullToString(d.getProdMod()) + " -1" ;
        String key3 = nullToString(t.getTemplateType()) + " " + nullToString(t.getTemplateSonType()) + " 99 " + nullToString(d.getTruteeBankKey());
        String key4 = nullToString(t.getTemplateType()) + " " + nullToString(t.getTemplateSonType()) + " 99 -1" ;

        MaterialTemplate v = maxVersionMap.get(key1);
        if ( v == null )
            v = maxVersionMap.get(key2);

        if ( v == null )
            v = maxVersionMap.get(key3);

        if ( v == null )
            v = maxVersionMap.get(key4);

        //文件备份到历史
        d = backupsMaterialDocument(d);

        //获取存储路径文件根目录 + “declare/document/” + 产品代码 + 文档类型
        String savePath = FileUtil.getFileStorePath() + FileUtil.DECLAREPATH + File.separator + FileUtil.DOCUMENTPATH + File.separator
                + d.getProdCode()+ File.separator  + d.getTemplateType();
        String saveName = "生成失败！模板不存在或数据源执行错误！";

        File f = new File(savePath);
        if (!f.exists() )
            f.mkdirs();

        try {
            //文档取值方式：1-模板表；0-其他系统交互
            if ("0".equals(t.getValueMethod())){
                //从系统交互临时中转表取文件
                //TODO

                String manualPath = SysUtil.getSystemParamsByParaid("70000010015");
                manualPath = manualPath+File.separator+d.getProdCode()+File.separator+d.getProdName()+"销售文件.docx";
                saveName = d.getProdName()+ "(" + d.getProdCode() + ")_" +"说明书.docx";
                Files.copy(new File(manualPath) , new File(savePath , saveName));
                bool = true;
                /*List<MaterialDocument> temp = materialDocumentDao.findDocumentTemp(d);
                if (temp.size() > 0){
                    saveName = temp.get(0).getDocumentName();
                    Files.copy(new File(temp.get(0).getDocumentPath()) , new File(savePath , saveName));
                    bool = true;
                }*/
            }else{
                if ( v != null ) {
                    //获取模板路劲
                    String filePath = v.getVersionPath();

                    String remotePath = "";
                    //远端sftp文件服务器根路径
                    remotePath = SysUtil.getSystemParamsByParaid("70000010012");
                    //获取最新模板到本地
                    FileTransfer transfer=new FileTransferHelpler().getTransfer();
                    transfer.downloadFileAndDisconnect(remotePath+v.getTemplateId()+File.separator+v.getVersionName(),filePath);

                    File documentFile = new File(filePath);
                    if (documentFile.exists() && documentFile.isFile()) {

                        String suffix = v.getVersionName().substring(v.getVersionName().lastIndexOf("."));
                        saveName = d.getProdName() + "(" + d.getProdCode() + ")_" + (StringUtils.isNotBlank(t.getTemplateSonLabel()) ? t.getTemplateSonLabel() : t.getTemplateLabel()) + suffix ;

                        if ("0".equals(t.getIsPlaceholder())){//是否有占位符
                            //不需要替换就直接复制文件即可
                            Files.copy(documentFile , new File(savePath , saveName));
                        }else{
                            v.setProdCode(d.getProdCode());
                            v.setProdMod(d.getProdMod());
                            replaceWord(v , savePath + File.separator + saveName);
                        }
                        bool = true;
                    }
                }
            }

            //申报文档生成后上传到远程服务器
            String remotePath = "";
            //远端sftp文件服务器根路径
            remotePath = SysUtil.getSystemParamsByParaid("70000010013");
            //文件转存到指定文件目录
            if(saveName.contains("生成失败")){
                if("03".equals(d.getTemplateType())){
                    saveName="产品内部审核文件需要手动上传！";
                }

            }else{
                FileTransfer transfer=new FileTransferHelpler().getTransfer();
                transfer.uploadFileAndDisconnect(savePath+ File.separator + saveName,remotePath+d.getProdCode()+ File.separator  + d.getTemplateType()+ File.separator + saveName);

            }
            //数据保存
            d.setDocumentPath(bool ? savePath + File.separator + saveName : "");
            d.setDocumentName(saveName);
            d.setHandUpload("0");
            d.setCrtDate(DateUtil.getNowDate());
            d.setCrtTime(DateUtil.getNowTime());
            d.setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_loginname"));
            materialDocumentDao.addMaterialDocument(d);
            /*
            if(!bool)//回滚文件
                rollBackMaterialDocument(d);
            */

        }catch (Exception e){
            saveName = "生成失败！模板不存在或数据源执行错误！";
            d.setDocumentPath(bool ? savePath + File.separator + saveName : "");
            d.setDocumentName(saveName);
            d.setHandUpload("0");
            //回滚文件
            rollBackMaterialDocument(d);
            bool = false;
            logger.error("文件转存失败: \n " + e.getMessage(),e);
        }

        return bool;

    }

    public void replaceWord (MaterialTemplate v, String filePath) throws Exception {
        //查询替换字段的sql配置
        List<MaterialTemplate> sqlList = materialTemplateDao.findMaterialConfig(v);
        //执行替换sql
        Map<String, Object> wordValues = new HashMap<>();
        wordValues.put("templateType" , v.getTemplateType());
        wordValues.put("templateSonType" , v.getTemplateSonType());
        wordValues.put("prodCode" , v.getProdCode());

        for (MaterialTemplate s: sqlList) {
            List<Map<String, Object>> val = materialTemplateDao.findSqlValue(s.getSqlSelect() , wordValues);
            if (val.size() > 0 )
                wordValues.putAll(val.get(0));
        }

        Document document = new Document(v.getVersionPath());
        //替换模板字段
        Range range = document.getRange();// range获取word中的内容
        for (Map.Entry<String, Object> entry : wordValues.entrySet()) {
            String key = nullToString(entry.getKey());
            String val = nullToString(entry.getValue());
            val = val.replaceAll("\n", "\u000B");
            try {
                range.replace("${" + key + "}", val, true,
                        false);
            } catch (Exception e) {
                logger.error("字段" + key + "替换失败:" + e.getMessage());
            }

        }
        //转存指定目录
        document.save(filePath);

    }


    public static String nullToString(Object o){
        if(o != null){
            return o.toString();
        }
        return "";
    }




}
