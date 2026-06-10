package com.kayak.rpt.email.util;
import org.springframework.stereotype.Service;

/**
 * 公共参数通用类，支持数据初始化，后直接使用
 */
@Service
public class CommonParamUtilsBak {

   /* @Resource
    private DisCommonMapper disCommonMapper;

    private static final Logger logger = LoggerFactory.getLogger(CommonParamUtils.class);

    private Map<String,String> sysParamConfigMap;
    private Map<String,List<SysDict>> sysDictMap;
    private List<DisclosureCodeMapping> disclosureCodeMappingList;

    public static final String SEPARATOR = "/";
    *//**
     * 数据初始化
     *//*
    public void init(){
        initSysParamConfig();
        initSysDictMap();
        initDisclosureCodeMappingList();
    }

    public String getSysParamConfig(String paramKey){
        if(sysParamConfigMap == null || sysParamConfigMap.isEmpty()){
            initSysParamConfig();
        }
        if(sysParamConfigMap.containsKey(paramKey.toUpperCase())){
            return sysParamConfigMap.get(paramKey.toUpperCase());
        }else{
            List<SysParamConfig> configList = disCommonMapper.querySystemParamConfigByKey(paramKey.toUpperCase());
            if(configList != null && configList.size() > 0){
                sysParamConfigMap.put(configList.get(0).getParamKey().toUpperCase(),configList.get(0).getParamValue());
                return configList.get(0).getParamValue();
            }
        }
        return "";
    }
    private void initSysParamConfig(){
        clearSysParamConfig();
        List<SysParamConfig> configList = disCommonMapper.querySystemParamConfigList();
        if(configList == null || configList.size() <= 0){
            logger.info("未获取到系统参数！");
            return;
        }
        for (SysParamConfig config: configList) {
            sysParamConfigMap.put(config.getParamKey().toUpperCase(),config.getParamValue());
        }
    }

    private void clearSysParamConfig(){
        if(sysParamConfigMap == null){
            sysParamConfigMap = new HashMap<>();
        }
        sysParamConfigMap.clear();
    }

    public List<SysDict> getSysDictByName(String dictName){
        if(sysDictMap == null || sysDictMap.isEmpty()){
            initSysDictMap();
        }
        if(sysDictMap.containsKey(dictName.toUpperCase())){
            return sysDictMap.get(dictName.toUpperCase());
        }else{
            List<SysDict> dictList = disCommonMapper.queryDictListByName(dictName.toUpperCase());
            sysDictMap.put(dictName.toUpperCase(),dictList);
            return dictList;
        }
    }
    private void initSysDictMap(){
        clearSysDictMap();
        List<SysDict> dictList = disCommonMapper.queryAllDictList();
        if(dictList == null || dictList.size() <= 0){
            logger.info("未获取到数据字典！");
            return;
        }
        for (SysDict dict: dictList) {
            if(sysDictMap.containsKey(dict.getDictName().toUpperCase())){
                List<SysDict> tempList = sysDictMap.get(dict.getDictName().toUpperCase());
                tempList.add(dict);
            }else{
                List<SysDict> tempList = new ArrayList<>();
                tempList.add(dict);
                sysDictMap.put(dict.getDictName().toUpperCase(),tempList);
            }
        }
    }
    private void clearSysDictMap(){
        if(sysDictMap == null){
            sysDictMap = new HashMap<>();
        }
        sysDictMap.clear();
    }
    public String getOuterCodeBySonCode(String subCode){
        if(subCode == null || subCode.length() <= 0){
            return "";
        }
        if(disclosureCodeMappingList == null || disclosureCodeMappingList.size() <= 0){
            initDisclosureCodeMappingList();
        }
        for (DisclosureCodeMapping mapper: disclosureCodeMappingList) {
            if(subCode.equals(mapper.getSonTypeCode())){
                return mapper.getTypeCodeOut();
            }
        }
        return "";
    }
    public String getOuterSonCodeBySonCode(String subCode){
        if(subCode == null || subCode.length() <= 0){
            return "";
        }
        if(disclosureCodeMappingList == null || disclosureCodeMappingList.size() <= 0){
            initDisclosureCodeMappingList();
        }
        for (DisclosureCodeMapping mapper: disclosureCodeMappingList) {
            if(subCode.equals(mapper.getSonTypeCode())){
                return mapper.getSonTypeCodeOut();
            }
        }
        return "";
    }

    public String getOuterSonCodeByCode(String code,String subCode){
        if(disclosureCodeMappingList == null || disclosureCodeMappingList.size() <= 0){
            initDisclosureCodeMappingList();
        }
        if(code == null || code.length() <= 0){
            return "";
        }
        if(subCode == null || subCode.length() <= 0){
            subCode = "";
        }
        for (DisclosureCodeMapping mapper: disclosureCodeMappingList) {
            if(code.equals(mapper.getTypeCode()) && subCode.equals(mapper.getSonTypeCode())){
                return mapper.getSonTypeCodeOut();
            }
        }
        return getOuterSonCodeBySonCode(subCode);
    }
    public String getOuterCodeByCode(String code,String subCode){
        if(disclosureCodeMappingList == null || disclosureCodeMappingList.size() <= 0){
            initDisclosureCodeMappingList();
        }
        if(code == null || code.length() <= 0){
            return "";
        }
        if(subCode == null || subCode.length() <= 0){
            subCode = "";
        }
        for (DisclosureCodeMapping mapper: disclosureCodeMappingList) {
            if(code.equals(mapper.getTypeCode()) && subCode.equals(mapper.getSonTypeCode())){
                return mapper.getTypeCodeOut();
            }
        }
        return "";
    }
    private void initDisclosureCodeMappingList(){
        disclosureCodeMappingList = disCommonMapper.queryDisclosureCodeMappingList();
        if(disclosureCodeMappingList == null){
            disclosureCodeMappingList = new ArrayList<>();
        }
    }

    private void clearDisclosureCodeMappingList(){
        if(disclosureCodeMappingList != null){
            disclosureCodeMappingList.clear();
        }
    }

    public PublishFileVO getFileInfoByIdAndType(String id, String flag){
        List<PublishFileVO> fileInfoList = new ArrayList<>();
        if(OnlyOfficeFileTypeEnum.NOTICE_INIT_FILE.getVal().equals(flag)){
            //原始公告表根据ID获取公告信息，文件名称及文件路径
            fileInfoList = disCommonMapper.queryNoticeInitFileListById(id);
        }else if(OnlyOfficeFileTypeEnum.NOTICE_PDF_FILE.getVal().equals(flag)){
            //PDF公告表根据ID获取公告信息，文件名称及文件路径
            fileInfoList = disCommonMapper.queryNoticePDFFileListById(id);
        }else if(OnlyOfficeFileTypeEnum.CHANNEL_NOTICE_FILE.getVal().equals(flag)){
            //渠道分发公告表，文件名称及文件路径
            fileInfoList = disCommonMapper.queryChannelNoticeFileListById(id);
        }else if(OnlyOfficeFileTypeEnum.CHANNEL_RECORD_FILE.getVal().equals(flag)){
            //渠道发布记录表，均为zip文件，暂不进行预览，文件名称及文件路径
            fileInfoList = disCommonMapper.queryChannelNoticeRecordFileListById(id);
        }else if(OnlyOfficeFileTypeEnum.TEMPLATE_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.queryTemplateFileListById(id);
        }else if(OnlyOfficeFileTypeEnum.TEMP_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.queryTempFileListById(id);
        }else if(OnlyOfficeFileTypeEnum.REGULAR_ZIP_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.queryImportZipFileListById(id);
        }else if(OnlyOfficeFileTypeEnum.SYSTEM_RECORD_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.querySystemRecordFileListById(id);
        }else if (OnlyOfficeFileTypeEnum.ZIP_IMPORT_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.queryImportFileListById(id);
        }else if (OnlyOfficeFileTypeEnum.NOTICE_MODEL_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.queryNoticeModelFileListById(id);
        }else if (OnlyOfficeFileTypeEnum.TRUSTEE_CHECK_SEND_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.queryTrusteeCheckSendFileListById(id);
        }else if (OnlyOfficeFileTypeEnum.TRUSTEE_CHECK_RECEIVE_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.queryTrusteeCheckReceiveFileListById(id);
        }
        else if (OnlyOfficeFileTypeEnum.PROD_MULTI_NOTICE_FILE.getVal().equals(flag)){
            fileInfoList = disCommonMapper.queryProdMultiNoticeFileListById(id);
        }
        PublishFileVO fileInfo = new PublishFileVO();
        if(fileInfoList == null || fileInfoList.size() <= 0){
            logger.error("文件没有找到记录,fileid:" + id);
        }else{
            fileInfo = fileInfoList.get(0);
        }
        if(fileInfo == null || fileInfo.getFilePath() == null){
            logger.error("文件没有找到记录,fileid:" + id);
        }
        return fileInfo;
    }

    public String getLocalParentDirPath(String fileType){
        String localBasePath = getSysParamConfig("dis_file_path");
        if(localBasePath.endsWith(SEPARATOR)){
            return localBasePath + fileType + File.separator + DateUtil.getTimestamp14() + File.separator;
        }else{
            return localBasePath + File.separator + fileType + File.separator + DateUtil.getTimestamp14() + File.separator;
        }
    }
    public String getRemoteParentDirPath(String fileType){
        String remoteBasePath = getSysParamConfig("dis_s3_base_path");
        if(remoteBasePath.endsWith(SEPARATOR)){
            return remoteBasePath + fileType + SEPARATOR + DateUtil.getTimestamp14() + SEPARATOR;
        }else{
            return remoteBasePath + SEPARATOR + fileType + SEPARATOR + DateUtil.getTimestamp14() + SEPARATOR;
        }
    }

    public String createTempFile(String fileName,String filePath){
        TempFile tempFile = new TempFile();
        tempFile.setId(CommonUtil.getUUID());
        tempFile.setFileName(fileName);
        tempFile.setFilePath(filePath);
        tempFile.setEffectflag(EffectFlagEnum.DATA_USE.getVal());
        tempFile.setCreatedate(DateConstant.YYYYMMDD.format());
        tempFile.setCreatetime(DateConstant.HHMMSS.format());
        tempFile.setCreateuser(DisCommonUtils.getCurrentUserId());
        tempFile.setUpdatedate(DateConstant.YYYYMMDD.format());
        tempFile.setUpdatetime(DateConstant.HHMMSS.format());
        tempFile.setUpdateuser(DisCommonUtils.getCurrentUserId());
        if(disCommonMapper.insertTempFile(tempFile) > 0){
            return tempFile.getId();
        }
        return "";
    }


    *//**
     * 审批流程提醒邮件发送：
     * 收件人ID，流程信息
     * 根据收件人查询
     * ID 主键ID
     * businessId 业务流水号
     * flowType 业务类型
     *//*
    public List<FlowNextNodeEmailInfoVO> getNextNodeUser(NoticeFlowTypeEnum flowType){
        //1、根据流程信息获取下一节点，根据节点数据获取关联用户信息，给各用户发邮件
        List<FlowNextNodeEmailInfoVO> nodeList = disCommonMapper.selectFlowNextNodeEmailInfo(flowType.getModuleId(),flowType.getFlowName(),flowType.getNodeName());
        if(nodeList == null || nodeList.size() <= 0){
            logger.info("未获取到流程下一节点用户信息！");
            return nodeList;
        }
        String currentUserId = DisCommonUtils.getCurrentUserId();
        List<FlowNextNodeEmailInfoVO> resultList = new ArrayList<>();
        Map<String,Integer> countMap = new HashMap<>();
        for (FlowNextNodeEmailInfoVO userInfo : nodeList) {
            if(StringUtil.isNotEmpty(currentUserId) && currentUserId.equals(userInfo.getUserId())){
                logger.info("当前用户不提醒！");
                continue;
            }
            if(countMap.containsKey(userInfo.getUserId())){
                countMap.put(userInfo.getUserId(), countMap.get(userInfo.getUserId()) + 1);
            }else{
                countMap.put(userInfo.getUserId(), 1);
                resultList.add(userInfo);
            }
        }

        return resultList;
    }

    *//**
     * 根据数据字典和码值获取文字描述
     * @param dict 数据字典名称
     * @param code 码值
     * @return 文字描述
     *//*
    public String getDictStrByCode(String dict,String code){
        if(StringUtil.isEmpty(dict) || StringUtil.isEmpty(code)){
            return "";
        }
        List<SysDict> dictList = getSysDictByName(dict);
        if(dictList == null || dictList.size() <= 0){
            return "";
        }
        for (SysDict entity : dictList) {
            if(entity == null){
                return "";
            }
            if(code.equals(entity.getDictKey())){
                return entity.getDictValue();
            }
        }
        return "";
    }

    *//**
     * 根据配置获取业务流水号
     * @param params 配置信息
     * @return 业务流水号
     *//*
    public String getDealNO(SequenceParamEnum params){
        if(params.isNeedDatePre()){
            String currentDate = DateUtil.getNowDate();
            return currentDate + getSequenceByParams(params.getTableName(),params.getColumnName(),currentDate,params.getSequenceLength());
        }else{
            return getSequenceByParams(params.getTableName(),params.getColumnName(),"",params.getSequenceLength());
        }
    }
    *//**
     * 生成业务流水号
     * @param tableName 表名
     * @param columnName 列明
     * @param dealDate 业务日期
     * @param length 流水号长度
     * @return 更新后的流水号
     *//*
    private String getSequenceByParams(String tableName,String columnName,String dealDate,int length){
        Long currentMaxId = disCommonMapper.getCurrentSequenceForUpdate(tableName,columnName,dealDate);
        if(currentMaxId == null){
            currentMaxId = 1L;
            disCommonMapper.insertSequence(tableName,columnName,dealDate,currentMaxId);
        }else{
            currentMaxId += 1;
            disCommonMapper.updateSequence(tableName,columnName,dealDate,currentMaxId);
        }
        String tempCode = String.valueOf(currentMaxId);
        // 长度小于配置的最大长度时左边补0,否则不改变
        if (tempCode.length() < length) {
            tempCode = StringUtil.leftPad(tempCode, length, "0");
        }
        return tempCode;
    }

    *//**
     * 这里的ID是业务ID对应businessID
     * @param flowType
     * @param id
     * @return
     *//*
    public List<String> getFileListForFlow(String flowType,String id){
        List<String> resultList = new ArrayList<>();
        if(StringUtil.isEmpty(flowType) || StringUtil.isEmpty(id)){
            return resultList;
        }
        List<PublishFileVO> fileInfoList = new ArrayList<>();
        if(NoticeFlowTypeEnum.OTHER_NOTICE_FLOW.getModuleId().equals(flowType)){
            //根据公告表ID获取公告文件列表
            fileInfoList = disCommonMapper.queryNoticeInitFileListByNoticeId(id);
        }else if(NoticeFlowTypeEnum.BATCH_NOTICE_FLOW.getModuleId().equals(flowType)){
            //根据批量表ID获取文件路径
            fileInfoList = disCommonMapper.queryBatchNoticeFileListByNoticeId(id);
        }else if(NoticeFlowTypeEnum.MULTI_PROD_FLOW.getModuleId().equals(flowType)){
            //根据一对多公告表ID获取文件路径
            fileInfoList = disCommonMapper.queryMultiNoticeFileListByNoticeId(id);
        }else if(NoticeFlowTypeEnum.CHANNEL_NOTICE_PUBLISH_FLOW.getModuleId().equals(flowType)){
            //根据渠道分发公告表ID获取公告文件列表
            fileInfoList = disCommonMapper.queryChannelNoticeFileListByNoticeId(id);
        }else if(NoticeFlowTypeEnum.REGULAR_REPORT_FLOW.getModuleId().equals(flowType)){
            //根据公告表ID获取公告文件列表
            fileInfoList = disCommonMapper.queryNoticeInitFileListByNoticeId(id);
        }
        if(fileInfoList != null && fileInfoList.size() > 0){
            for (PublishFileVO file: fileInfoList) {
                resultList.add(file.getFilePath());
            }
        }
        return resultList;
    }*/
}
