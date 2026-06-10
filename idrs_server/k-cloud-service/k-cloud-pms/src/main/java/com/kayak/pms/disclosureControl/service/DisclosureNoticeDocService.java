package com.kayak.pms.disclosureControl.service;

import com.google.common.io.Files;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.connect.utils.ZipUtil;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeDocDao;
import com.kayak.pms.disclosureControl.disclousreEnum.NoticeFileTypeEnum;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeValue;
import com.kayak.pms.global.constants.DisclosureSonType;
import com.kayak.pms.printTemp.utils.WordToPdfUtil;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DisclosureNoticeDocService {

    private static final String separator = "/";
    private static String FILE_TYPE_PDF = "1";
    private static String FILE_TYPE_DOC = "2";
    private static String FILE_TYPE_XLS = "3";
    private static final ThreadLocal<Map<String, String>> TIME_STAMP_NOTICE = ThreadLocal.withInitial(HashMap::new);
    /**
     * 根据公告ID获取TIME_STAMP，保证同一个公告再不同渠道下的时间戳一致性
     * @param notice_id
     */
    public static String getTimeTamp(String notice_id) {
        Map<String, String> local = TIME_STAMP_NOTICE.get();
        String time_stamp_notice = local.get(notice_id);
        if (time_stamp_notice == null) {
            time_stamp_notice = DateUtil.getTimestamp17();
            local.put(notice_id, time_stamp_notice);
        }
        return time_stamp_notice;
    }
    @Autowired
    private DisclosureNoticeDocDao disclosureNoticeDocDao;

    @Autowired
    private DisclosureNoticeDocOperateService disclosureNoticeDocOperateService;

    @Autowired
    private WordToPdfUtil wordToPdfUtil;

    /**
     * 信批公告获取文档进行替换
     * 并生成docx文件到指定上传目录
     * @param params 传入参数:
     * @throws Exception
     */
    public void generateNoticeDocument(Map<String, Object> params) throws Exception {
        String doc_type = "1";//模板替换类型:1-字符不含表格/2-字符含表格
        /* 先找到模板，在找到需要替换的占位符，进行替换下载 */
        List<DisclosureNoticeValue> t8NoticeValueList = this.getWordValueListByNoticeVersionId(params);//获取需要替换的数据
        String disclosureSonType = disclosureNoticeDocDao.getDisclosureTypeInfo(params);
        if(DisclosureSonType.netValueEntity.getItemKey().equals(disclosureSonType) || DisclosureSonType.netNormal.getItemKey().equals(disclosureSonType) ||
                DisclosureSonType.netSpecial.getItemKey().equals(disclosureSonType) ||DisclosureSonType.quarter.getItemKey().equals(disclosureSonType) ||
                DisclosureSonType.seAnnual.getItemKey().equals(disclosureSonType) || DisclosureSonType.annual.getItemKey().equals(disclosureSonType)){
            doc_type = "2";/** 需要处理表格的情形*/
        }
        //1.下载公告模板到本地
            String remotePath = SysUtil.getSystemParamsByParaid("70000010003");//信批文档模板sftp存储路径
            //文档暂存路径(公告模板下载路径,完成后删除该文件)
            String system = System.getProperty("os.name");
            String temPath;//公告模板下载目录
            if (system.toLowerCase().startsWith("win")) {
                temPath = SysUtil.getSystemParamsByParaid("70000010002");
            } else {
                temPath = SysUtil.getSystemParamsByParaid("70000010009");
            }
           String fileModName = String.valueOf(params.get("mod_doc_name"));//下载公告模板名称
            String disclosureModVersionId = String.valueOf(params.get("disclosure_mod_version_id"));//信批文档上传文件名

            String dealRemotePath = remotePath + disclosureModVersionId + "/";
            FileTransfer transfer=new FileTransferHelpler().getTransfer();
            transfer.downloadFileAndDisconnect(dealRemotePath+fileModName,temPath+fileModName);
        //2替换文档
        String fileSavePath = String.valueOf(params.get("filePath"));//信批文档上传路径(保管替换后的公告文档)+文件名

        String modSuffix = (fileModName.contains("."))?(fileModName.substring(fileModName.lastIndexOf("."))):" ";//文件模板后缀
        fileSavePath = fileSavePath  + modSuffix;
            if (NoticeFileTypeEnum.DOC.getName().equals(modSuffix)) {
                disclosureNoticeDocOperateService.replaceNoticeDocCharContent(temPath, fileModName, fileSavePath, t8NoticeValueList,doc_type);//替换doc文档方法
            } else if (NoticeFileTypeEnum.DOCX.getName().equals(modSuffix)) {
                disclosureNoticeDocOperateService.replaceNoticeDocxCharContent(temPath, fileModName, fileSavePath, t8NoticeValueList,doc_type);//替换docx文档中中文字符
            } else {
                throw new Exception("不支持的模板的文件类型:" + modSuffix);
            }
            if ("2".equals(doc_type)) {/** 处理信息披露文档中表格部分 */
                /** 处理需要替换的表格生成部分：根据公告基准日期和公告类型/子类型选择基准日期前生效的最新表单数据配置 */
                //disclosureNoticeDocOperateService.generateGridConfigByDisclosureTypeAndLatestDate(params);
                disclosureNoticeDocOperateService.replaceNoticeDocGridContent(temPath, fileModName, fileSavePath, String.valueOf(params.get("disclosureNoticeVersionId")));
            }
    }

    /**
     * 根据公告版本id获取需要替换的数据对象
     * @param params
     * @return
     */
    private List<DisclosureNoticeValue> getWordValueListByNoticeVersionId(Map<String, Object> params) throws Exception {

        return disclosureNoticeDocDao.getNoticeVersionValue(params);
    }

    public void okFileGenerate(String filePath , String fileName) {
        String file = filePath + fileName;
        File okFile = new File(file);
        if (okFile.exists()) {
            okFile.delete();
            log.info("删除文件：{}", fileName);
        }
        try {
            okFile.createNewFile();
        }catch (IOException e){
            log.error("未找到此路径：{}",filePath);
        }
    }


    /**
    * @功能描述:公告id, 公告版本id, 文件名, 渠道id, 是否区分版本
    * @params:[disclosureNoticeId, noticeLatestVersionId, doc_name, channelId, Versioned]
    * @return:java.util.Map<java.lang.String,java.lang.Object>
    * @Athor:
    * @date:2022/9/2
    */
    public Map<String, Object> judgeNoticeDocIsExistAndGenerateDoc(String disclosureNoticeId,String noticeLatestVersionId,String doc_name,Boolean versioned) throws  PromptException{
        Map<String, Object> pubFile_params = new HashMap<>();
        //查询公告最新版本及文件上传路径
        String root_path = "";//本地文件上传根目录
        String filePath = "";//本地文件上传根目录+版本id+文件名
        try {
            String system = System.getProperty("os.name");
            if (system.toLowerCase().startsWith("win")) {
                root_path = CacheUtil.getSystemParam("70000010000"); //windows系统
            } else {
                root_path = CacheUtil.getSystemParam("70000010001"); //linux或其他操作系统
            }

            //重新替换生成文本，避免因其他问题导致错误文本已生成无法重新生成发布
            Map<String, Object> params = new HashMap<>();//生成文档组建参数
            params.put("fileName", doc_name);//发布公告文件名
            params.put("disclosureNoticeId", disclosureNoticeId);//发布公告id
            params.put("disclosureNoticeVersionId", noticeLatestVersionId);//发布公告最新版本id

            params =  this.getNoticeModInfo(params);

            //上传路径规则:是否区分版本？不区分则 [根路径+公告版本号id+文件名]，区分则 [根路径+公告版本号id+文件名+版本号]
            filePath = !versioned?
                    root_path + noticeLatestVersionId + "/" + doc_name
                    :root_path + noticeLatestVersionId + "/" + doc_name+ params.get("versionNum").toString();
            params.put("filePath", filePath);//下载模板替换字段后文件存放路径
            /**
             * 替换文件内容
             */
            this.generateNoticeDocument(params);

            pubFile_params.put("pub_file_path", root_path + noticeLatestVersionId + "/");
            pubFile_params.put("pub_file_name", doc_name);
            pubFile_params.put("noticeLatestVersionId", noticeLatestVersionId);
            pubFile_params.put("disclosureNoticeId", disclosureNoticeId);
            pubFile_params.put("filePath", filePath);//返回替换文件值后的文件路径
            String modDocName = params.get("mod_doc_name").toString();
            pubFile_params.put("modDocName", modDocName);//模板文件名称，带后缀
            /**该文件后缀为公告生成后替换的公告文件后缀，应与模板一致，若无，则 .docx */
            String suffix = (modDocName.contains("."))?(modDocName.substring(modDocName.lastIndexOf("."))):".docx";//文件名后缀
            pubFile_params.put("suffix", suffix);//后缀


        } catch (Exception e) {
            log.error("获取信批公告发布文件路径异常:" + e.getMessage(),e);
            throw new PromptException("获取信批公告发布文件路径异常:" + e.getMessage());
        }
        return pubFile_params;//返回本地上传路径+文件名
    }


    /**
     * 根据规则确定指定公告id指定某一渠道的上传文件名称
     * @param disclosureNoticeId
     * @return
     */
    public String getNoticeDocName (String disclosureNoticeId) {

        return disclosureNoticeDocDao.getNoticeNameByNoticeId(disclosureNoticeId);//获取公告标题
    }

    /**
     * 获取信批公告相关数据:最新版本号
     * @param disclosureNoticeId
     * @return
     */
    public String getDisclosureMaxVersionId (String disclosureNoticeId) throws Exception {

        return disclosureNoticeDocDao.getNoticeLatestVersionId(disclosureNoticeId);
    }

    /**
     * 根据公告版本id获取其模板信息系
     * @param params
     * @return
     */
    public Map<String, Object> getNoticeModInfo (Map<String, Object> params) throws Exception {

        return disclosureNoticeDocDao.getNoticeModInfo(params);
    }

    /**
     * 获取发布公告的文件名称
     * @param params:notice_version_id/upload_file_name_type/notice_title
     * @return
     * @throws Exception
     */
    public String confirmPublishName(Map<String, Object> params) throws Exception {
        String upload_file_name_type = String.valueOf(params.get("upload_file_name_type"));//文件名字符串:替换完后返回
        String replace_key = "";
        SqlRow fileInfoRes = disclosureNoticeDocDao.queryFileInfByNoticeId(String.valueOf(params.get("notice_version_id")));//获取文件其他信息
        params.put("prod_code", fileInfoRes.getString("prod_code"));
        params.put("prod_name", fileInfoRes.getString("prod_name"));
        params.put("disclosure_type", fileInfoRes.getString("disclosure_type"));
        params.put("disclosure_son_type", fileInfoRes.getString("disclosure_son_type"));
        params.put("TIME_STAMP_NOTICE",getTimeTamp(params.get("notice_id").toString()));

        Pattern pattern = Pattern.compile("\\[.*?\\]");//正则匹配字符中所有[]的内容
        Matcher matcher = pattern.matcher(upload_file_name_type);
        while(matcher.find()){
            String replace_value = "";
            System.out.println(matcher.group(0));
            String replace_str = matcher.group(0);
            replace_key = replace_str.substring(1, replace_str.indexOf("]"));
            Map<String, String> value_param = disclosureNoticeDocDao.queryAndRepalceColumnValue(replace_key, params);
            if(!"".equals(value_param.get(replace_key))&&!"TIME_STAMP_NOTICE".equals(replace_key)){//时间戳取上面处理的，不再查询
                replace_value = value_param.get(replace_key);
            } else {
                replace_value = String.valueOf(params.get(replace_key));
            }
            upload_file_name_type = upload_file_name_type.replace(replace_str, replace_value);//替换文件名字符串
        }
        return upload_file_name_type;
    }


    /**
     * 根据公告id和渠道id确定需要发布的文件格式、文件名及发布路径等
     * 根据文件类型对文件进行复制重命名(原文件名+_后缀)操作，根据需要的文件后缀类型对复制后的文件进行转换保存操作
     * @param pubFile_params
     * @return
     */
    public List<Map<String,Object>> getPubFileInfoByNoticeChannel(Map<String,Object> pubFile_params, DisclosureChannel disclosureChannel) throws Exception {
        List<Map<String,Object>> pubFileInfoList = new ArrayList<>();
        String notice_id = String.valueOf(pubFile_params.get("disclosureNoticeId"));//公告id
        String channel_id = String.valueOf(pubFile_params.get("channelId"));//渠道id
        String pub_path = String.valueOf(pubFile_params.get("pub_file_path"));//文件上传目录(已带版本id)
        String doc_name = String.valueOf(pubFile_params.get("pub_file_name"));//公告标题(未特殊处理上传文件名称)
        String notice_version_id = String.valueOf(pubFile_params.get("noticeLatestVersionId"));//公告最新版本id

        SqlRow fileTypesRes = disclosureNoticeDocDao.getNoticeFilePubType(notice_version_id, channel_id);//查询公告需要发布渠道的文件类型和发布文件名称
        Map<String, Object> params = new HashMap<>();
        params.put("notice_version_id", notice_version_id);
        params.put("notice_id", notice_id);
        params.put("channel_id", channel_id);
        //当upload_file_name_type为空时，默认为公告标题
        params.put("upload_file_name_type", "".equals(fileTypesRes==null?"":fileTypesRes.getString("upload_file_name_type"))?doc_name:fileTypesRes.getString("upload_file_name_type"));
        params.put("notice_title", doc_name);
        String mod_name = String.valueOf(pubFile_params.get("modDocName"));//公告模板名称(带后缀)
        String ori_regex = mod_name.contains(".docx")?".docx":".doc";//初始文件后缀(模板)
        String pub_name = this.confirmPublishName(params);//确认该公告发布渠道的文件名称(不带后缀)
        String final_ori_regex = "";
        //获取某一公告版本需要发布该渠道的文件类型
        if (fileTypesRes==null)
            return pubFileInfoList;
        String[] typeLt = fileTypesRes.getString("upload_file_type").split(",");
        for(String fileType : typeLt){
            try{
                String ori_path = pub_path + doc_name + ori_regex;
                String final_path = pub_path + pub_name;//不带后缀
                File ori_file = new File(ori_path);//读取已生成doc文件
                File final_file = new File(final_path + ori_regex);//生成需发布的doc或docx文件
                if(!pub_name.equals(doc_name) && final_file.exists()){
                    final_file.delete();
                    final_file.createNewFile();
                    log.info("删除并重新生成目录文件:" + final_path + ori_regex);
                }
                if(!pub_name.equals(doc_name)){
                    Files.copy(ori_file, final_file);//生成发布路径下发布名称的doc或docx文件
                }

                final_ori_regex = ori_regex;

                if (NoticeFileTypeEnum.PDF.getVal().equals(fileType)) {//转dbf文件
                    wordToPdfUtil.doc2pdf(ori_path, final_path + NoticeFileTypeEnum.PDF.getName());
                    final_ori_regex = NoticeFileTypeEnum.PDF.getName();
                } else if (NoticeFileTypeEnum.ZIP.getVal().equals(fileType)) {//压缩zip文件
                    File zip_file = new File(final_path + NoticeFileTypeEnum.ZIP.getName());
                    if (!zip_file.exists()) {
                        zip_file.createNewFile();
                        log.info("删除并重新生成目录文件:" + final_path + NoticeFileTypeEnum.ZIP.getName());
                    }
                    ZipUtil.conmpress(zip_file, final_file);//压缩ori_file文件至zip_file
                    final_ori_regex = NoticeFileTypeEnum.ZIP.getName();
                }
                String suffix_file_name = fileTypesRes.getString("suffix_file_name");
                String ok_file_name = null;
                if (suffix_file_name != null) {
                     ok_file_name = pub_name + final_ori_regex + suffix_file_name;//生成确认文件名称(带后缀)
                    this.okFileGenerate(pub_path,ok_file_name);
                }

                /** 对文件进行复制转换操作 */
                Map<String,Object> type_param = new HashMap<>();
                type_param.put("channel_id", disclosureChannel.getId());
                type_param.put("fileType", fileType);
                type_param.put("pub_path", final_path + final_ori_regex);//公告本地发布路径+文件名
                type_param.put("local_path", pub_path);//公告本地发布路径
                type_param.put("file_name", pub_name + final_ori_regex);//文件名
                type_param.put("host_ip", disclosureChannel.getHostIp());
                type_param.put("port_code", disclosureChannel.getPortCode());
                type_param.put("user_name", disclosureChannel.getUserName());
                type_param.put("password", disclosureChannel.getPassword());
                type_param.put("file_path", disclosureChannel.getFilePath());//服务器路径
                type_param.put("is_docking", disclosureChannel.getIsDocking());//是否发布渠道
                type_param.put("status", disclosureChannel.getStatus());//渠道状态0-停用，1-启用
                type_param.put("notice_id", notice_id);//公告id
                type_param.put("notice_version_id", notice_version_id);//公告版本id
                type_param.put("ok_file_name", ok_file_name);//确认文件名：例如：浙农商_EW0077_发行成立公告_母子产品_20230601.ok
                type_param.put("local_ok_path", pub_path);//确认文件名本地发布路径
                pubFileInfoList.add(type_param);
            } catch (Exception e) {
                throw new Exception("生成信批公告文件时,转换" + fileType + "文件格式异常: " + e.getMessage());
            }
        }
        return pubFileInfoList;
    }

    /**
     * 获取推送理财文件绑定唯一标识id号
     * @param params
     * @return
     * @throws Exception
     */
    public String getFncSendFileMarkId (Map<String,Object> params) throws Exception {
        String mark_id = String.valueOf(params.get("notice_version_id"));
        String file_type = String.valueOf(params.get("fileType"));
        return mark_id + file_type;
    }

    /**
     * 公告发布后变更公告状态
     * @param params
     * @throws Exception
     */
    public void updateNoticeStatus(Map<String, Object> params) throws Exception{

        params.put("user_id", SysUtil.getSysUserParamValue("sys_user_userid"));
        params.put("user_name", SysUtil.getSysUserParamValue("sys_user_username"));

        try{
            //公告版本状态变更为已发布
            disclosureNoticeDocDao.updateNoticeVersionStatus(params);
            //公告渠道信息状态变更为已发布
            disclosureNoticeDocDao.updateNoticeChannelStatus(params);
            //发布记录入表
            disclosureNoticeDocDao.insertNoticeRecord(params);
        } catch (Exception e){
            throw new Exception("公告状态变更异常:" + e.getMessage());
        }
    }

    /**
     *
     * @param channel_id
     * @param pub_date
     * @return
     * @throws Exception
     */
    public List<String> handleTxtFileContext(String channel_id, String pub_date) throws Exception {
        List<String> fileList = new ArrayList<>();
        List<SqlRow> sqlReList = disclosureNoticeDocDao.getPubFileList(pub_date, channel_id);
        for (SqlRow sqlRow : sqlReList) {
            fileList.add(sqlRow.getString("pub_path"));
        }
        return fileList;
    }

    /**
     * 公告发布txt文件生成
     * @param pubFileNameList
     * @param file_path
     * @param file_name
     * @return 返回txt路径
     * @throws Exception
     */
    public void generatePubTxtFile (List<String> pubFileNameList, String file_path, String file_name) throws Exception {
        String txt_file_name = file_path  + file_name;//txt文档路径
        BufferedWriter bufferedWriter = null;

        try{
            File txt_file = new File(txt_file_name);
            if (!txt_file.exists()) {//若文件已存在，则先删除后新建
                txt_file.delete();
                txt_file.createNewFile();
                log.info("删除并重新生成推送理财目录文件:" + txt_file_name);
            }

            bufferedWriter = new BufferedWriter(new FileWriter(txt_file));//内容写入缓存
            //生成txt文件内容,每行的内容为推送文件名+换行符
            for (String content_name : pubFileNameList) {
                bufferedWriter.write(content_name + "\r\n");
            }
            bufferedWriter.flush();//缓存写入文件
        }catch (Exception e) {
            throw new Exception("信批公告写入txt文档异常:" + e.getMessage());
        }finally {
            bufferedWriter.close();//关闭流
        }

    }

    /**
     * 推送理财文件说明,每行数据规则如下
     * ID|产品代码|文件类型|产品名称|公告标题|公告内容(-)|发布日期|发布时间|修改日期(-)|修改时间(-)|附件路径|附件名称|生效时间|失效时间(-)|公告文件类型(1)|公告紧急级别
     * @param pubMap
     * @return
     * @throws Exception
     */
    public String getOldDisclosureType (Map<String, Object> pubMap) throws Exception {
        String fileMessStr = "";
        SqlRow infoRes = disclosureNoticeDocDao.queryPubTAIntegrateMessInfo(pubMap);
        if (infoRes != null) {
            fileMessStr = pubMap.get("id") + "|" + infoRes.get("prod_code") + "|" + infoRes.get("disclosure_type") + "|" + infoRes.get("prod_name") +
                    "|" + infoRes.get("notice_title") + "||" + pubMap.get("pub_date") + "|" + pubMap.get("pub_time") + "|||" + pubMap.get("file_path") +
                    "|" + pubMap.get("file_name") + "|" + pubMap.get("pub_time") + "||" + "1" + "|";
        }

        return fileMessStr;
    }

    /**
     * 插入推送渠道文件列表信息
     * @param channel_id
     * @param fileMessage
     * @param notice_version_id
     * @param pub_date
     */
    public void saveNoticePubFileInfo (String channel_id, String fileMessage, String notice_version_id, String pub_date, String batch_no, String file_name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("disclosure_channel_id", channel_id);
        params.put("file_name", file_name);
        params.put("pub_path", fileMessage);
        params.put("disclosure_notice_version_id", notice_version_id);
        params.put("pub_date", pub_date);
        params.put("batch_no", batch_no);
        disclosureNoticeDocDao.saveNoticePubFileInfo(params);
    }
}
