package com.kayak.dps.direct.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.check.service.ReportDataValidateService;
import com.kayak.dps.direct.dao.CheckDataDao;
import com.kayak.dps.direct.dao.DataFileDao;
import com.kayak.dps.direct.dao.ReportClearDao;
import com.kayak.dps.direct.model.ExSeat;
import com.kayak.dps.direct.util.DirectAnalysisUtil;
import com.kayak.dps.direct.util.DirectParams;
import com.kayak.dps.direct.util.DirectUtils;
import fpr.dmsg.client.FprDClient;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class ReportClearService {

    @Autowired
    public ReportClearDao reportClearDao;

    @Autowired
    public CheckDataDao checkDataDao;

    @Autowired
    public CheckDataService checkDataService;

    @Autowired
    public DataFileDao dataFileDao;

    @Autowired
    DataFileService dataFileService;// 本地
    @Autowired
    DataFileThreeService dataFileThreeService;// 本地
    @Autowired
    ReportDataValidateService reportDataValidateService;

    DirectParams directParams = new DirectParams();

    /**
     * 清算流程启动之前 调用
     * 验证清算检查前 的 准备工作是否完成
     * 包含数据初始化工作 （axin）
     * @throws Exception
     */
    public void initParams(String taskDate) throws Exception{

        //参数初始化
        directParams.initVar(taskDate);

    }

    /**
     * 检查一、二期登记数据
     *
     * @throws Exception
     */
    public Boolean checkRegisterDataA(String reportTable, String dealDate) throws Exception{
        Map<String,String> params = new HashMap<>();
        Map<String,Object> params1 = new HashMap<>();
        Map<String,Object> params2 = new HashMap<>();
        String reportType = checkDataDao.getReportType(reportTable);
        params.put("reportTable",reportTable);
        params.put("reportType",reportType);
        params.put("deal_date",dealDate);
        params.put("isBatch","01");
        reportDataValidateService.execute(dealDate,params);
        return true;
    }


    /**
     * 检查登记数据
     *
     * @throws Exception
     */
    public void checkRegisterData() throws Exception{
        String error_desc = "";


        //初始化正则表达式
        String whiteregex = DirectParams.whiteregex;

        // 合法性校验 201--权益人身份信息登记
        int error_cnt_201 = checkDataService.custInfoChecking("201" , DirectParams.preWorkDate , whiteregex);
        error_desc += (error_cnt_201 > 0) ? ("投资者身份信息校验有误，不合法数据条数:" + error_cnt_201 + "。") : "";

        // 合法性校验  202--权益人持有信息登记
        int error_cnt_202 = checkDataService.transRegisterChecking("202" , DirectParams.preWorkDate , whiteregex);
        error_desc += (error_cnt_202 > 0) ? ("投资者持有信息校验有误，不合法数据条数:" + error_cnt_202 + "。") : "";


        // 合法性校验  203--投资者信息明细登记
        int error_cnt_203 = checkDataService.transInfoChecking("203" , DirectParams.preWorkDate , whiteregex);
        error_desc += (error_cnt_203>0)?("投资者信息明细登记校验有误，不合法数据条数:"+error_cnt_203+"。"):"";

        if(!"".equals(error_desc)){
            throw new Exception(error_desc);
        }
    }


    /**
     * 生成中债一二期报送文件
     *
     * @throws Exception
     */
    public void exportRegisterFile(String isRegisterFile) throws Exception {
        // 文件导出
        List<ExSeat> exSeatList = dataFileDao.getExSeat(isRegisterFile);
        List<SqlRow> dateList = dataFileDao.findDataReportByDate(exSeatList.get(0).getExtab(), DirectParams.workDate);
        String app_asset_check = SysUtil.getSystemParamsByParaid("app_asset_check");
        for (SqlRow sqlRow : dateList){
            if("1".equals(app_asset_check)){
                if("108".equals(isRegisterFile) || "110".equals(isRegisterFile) || "117".equals(isRegisterFile)){
                    String assets = dealAssetCheck(sqlRow.getString("report_date"),isRegisterFile);
                    if(!"".equals(assets)){
                        throw new Exception("请先报送资产负债要素登记,资产代码： "+assets);
                    }
                }
            }
            String reportNo = dataFileService.exportData(exSeatList,sqlRow.getString("report_date"),isRegisterFile);
            getResultFile(exSeatList.get(0).getExtab(),exSeatList.get(0).getExtab(),sqlRow.getString("report_date"),isRegisterFile,reportNo);
        }
    }


    /**
     * 生成三期报送文件
     * @throws Exception
     */
    public void exportRegisterThreeFile(String isRegisterFile) throws Exception{
        List<ExSeat> exSeatList = dataFileDao.getExSeat(isRegisterFile);
        List<SqlRow> dateList = dataFileDao.findDataReportByDate(exSeatList.get(0).getExtab(), DirectParams.workDate);
        String sys_date = SysUtil.getSystemParamsByParaid("10004");
        for (SqlRow sqlRow : dateList){
            String report_table = (!sqlRow.getString("report_date").equals(sys_date)) && ("202".equals(isRegisterFile) || "203".equals(isRegisterFile)) ? exSeatList.get(0).getExtab() + "_"+sqlRow.getString("report_date") : exSeatList.get(0).getExtab();
            String reportNo=dataFileThreeService.exportData(exSeatList,sqlRow.getString("report_date"),isRegisterFile,report_table); // 执行导出
            getResultFile(exSeatList.get(0).getExtab(),report_table,sqlRow.getString("report_date"),isRegisterFile,reportNo);
        }

    }

    /**
     * 发送接口文件
     * @throws Exception
     */
    public void sendRegisterFile(String isRegisterFile) throws Exception{

        //TODO 上传文件到直线系统
        String[] busiCode = isRegisterFile.split(",");
        for (String busiC:busiCode) {
            //文件导出
            String filepath = DirectUtils.getFilePath();

            //此处重新定义文件状态  0-未处理     1-发送成功      2-接收成功   3-处理失败(目前定义仅发送失败时存在该状态) 4-消息获取失败（目前定义仅接收文件时存在该状态） 6-文件下载成功    5-文件下载失败
            //查询未发送及发送失败的文件
            String status = "0,3";
            List<SqlRow> selectinfo = reportClearDao.selectFileInfoEx(busiC, DirectParams.preWorkDate, status);
            if (selectinfo != null && selectinfo.size() > 0 && "1".equals(DirectParams.connFlag)) {
                //发送同步文件
                sendRegisterFileSync( filepath, selectinfo.get(0));
            }
        }
    }

    /**
     * 获取结果文件
     * @throws Exception
     */
    public void getResultFile(String real_table,String report_table,String report_date,String isRegisterFile,String batch_no) throws Exception{
        int trytimes = Integer.parseInt(DirectParams.trytimes);	//轮询次数
        int sleeptime = Integer.parseInt(DirectParams.sleeptime);	//每次轮询等待时间
        int div_limit =  Integer.parseInt(DirectParams.divLimit);
        int i = 0;
        boolean flag = "202".equals(isRegisterFile) || "203".equals(isRegisterFile) || "117".equals(isRegisterFile);
        String now_date = DateUtil.today().replaceAll("-","");
        int data_flag=0;
        while (true) {
            Calendar c = Calendar.getInstance();
            int h = c.get(Calendar.HOUR_OF_DAY);
            if (h < 1 || h > 5) {
                if(!"".equals(report_table)){
                    int file_mark = reportClearDao.getReultFile(isRegisterFile,now_date);
                    if(file_mark>0){
                        data_flag = 1;
                    }else{
                        data_flag = 0;
                        int fail_num = reportClearDao.isReultFileError(isRegisterFile,now_date,report_date);
                        // 读取记录数量
                        String status="5";
                        if(flag){
                            status="2";
                        }
                        List<SqlRow> sqlRows = dataFileDao.findTabMaxId(report_table, report_date,status);
                        Long minId = sqlRows.get(0).getLong("minId");
                        Long maxId = sqlRows.get(0).getLong("maxId");
                        Long recod = maxId - minId;
                        Long size = recod / div_limit + 1;
                        //获取更新数据状态为报送文件已生成
                        for (int j = 0; j < size; j++) {
                            Long start = minId + div_limit * j;
                            if(fail_num>0){
                                if(flag){
                                    reportClearDao.updateDataStatusByTr(real_table,report_table,now_date,report_date,"6", start , start + div_limit);
                                }else{
                                    reportClearDao.updateDataStatus(report_table,now_date,report_date,"6", start , start + div_limit);
                                }
                            }else{
                                if(flag){
                                    reportClearDao.updateDataStatusByTr(real_table,report_table,now_date,report_date,"3", start , start + div_limit);
                                }else{
                                    reportClearDao.updateDataStatus(report_table,now_date,report_date,"3", start , start + div_limit);
                                }
                            }
                        }
                        if(fail_num>0){
                            reportClearDao.updateFileStatusBy(isRegisterFile,now_date,report_date,"1","4");
                        }
                    }
                }
            }
            String app_stop_zz = SysUtil.getSystemParamsByParaid("app_stop_zz"); //中止直联报送
            if("1".equals(app_stop_zz)){
                if(flag){
                    reportClearDao.clearReportDataNo(report_table);
                }
                if(!"".equals(batch_no)){
                    List<SqlRow> dataList = reportClearDao.getReportDataSum(isRegisterFile,now_date,report_date,batch_no);
                    reportClearDao.setReportDataSum(dataList,isRegisterFile,now_date,report_date,"2",batch_no);
                }
                throw new Exception("中止直联报文报送 !");
            }
            if(data_flag==1){
                Thread.sleep(sleeptime * 1000);// 避免抛服务器繁忙.
                data_flag = 0;
            }else{
                if(flag){
                    reportClearDao.clearReportDataNo(real_table);
                }
                if(!"".equals(batch_no)){
                    List<SqlRow> dataList = reportClearDao.getReportDataSum(isRegisterFile,now_date,report_date,batch_no);
                    reportClearDao.setReportDataSum(dataList,isRegisterFile,now_date,report_date,"1",batch_no);
                }
                break;
            }
            i++;
            if(i>trytimes){
                if(flag){
                    reportClearDao.clearReportDataNo(real_table);
                }
                reportClearDao.updateFileStatus(isRegisterFile,now_date,"2","5");
                if(!"".equals(batch_no)){
                    List<SqlRow> dataList = reportClearDao.getReportDataSum(isRegisterFile,now_date,report_date,batch_no);
                    reportClearDao.setReportDataSum(dataList,isRegisterFile,now_date,report_date,"2",batch_no);
                }
                throw new Exception("未获取到发送文件对应的反馈报文 !");
            }
        }
    }

    /**
     * 数据归档
     * @throws Exception
     */
    public void dataArchiving(String isRegisterFile) throws Exception{
        String[] busiCode = isRegisterFile.split(",");
        for (String busiC:busiCode) {
            reportClearDao.dataArchiving(busiC , DirectParams.preWorkDate);
        }
        //切日
    }

    /**
     * 解析结果文件
     * @throws Exception
     */
    public int parseResultFile(String filepath, String zipfilename, String msg_type, String busiC, Map<String, String> exSeatMap) throws Exception{
        //创建时间准备
        String createTime = DateUtil.now();

        int err_cnt = 0;
        String filename = zipfilename.replace("zip", "xml");

        //解压文件
        DirectUtils.unZipFile(filepath+zipfilename, filepath);

        //解析文件
        SAXReader reader = new SAXReader();  //创建SAXReader对象
        Document document = reader.read(new File(filepath+filename));  	//读取文件 转换成Document
        Element root = document.getRootElement();  //获取根节点元素对象

        Map<String,Object> param = new HashMap<>();
        param.put("fileType",msg_type);

        //一二期当前返回信息
        if ("WMRS.001.001.01".equalsIgnoreCase(msg_type)) {
            //错误返回的数据行
            Set<String> errorKeys = DirectAnalysisUtil.getErrorPrimaryKeyList(root,"RegistrationSerialNumber");
            //数据更新为报送完成
            reportClearDao.updateStatusOne(exSeatMap.get(busiC) ,DirectParams.preWorkDate);
            //更新错误的数据状态
            if (ObjectUtil.isNotEmpty(errorKeys)) {
                reportClearDao.updateErrorStatusOne(exSeatMap.get(busiC) ,DirectParams.preWorkDate, errorKeys);
            }
        }

        //modify by yangcw  40类消息处理
        if ("WMRS.002.001.01".equalsIgnoreCase(msg_type)) {
            List<Element> prodInformations = root.elements("ProductRegistrationCodeInformationTuple");
            for (int i = 0; i < prodInformations.size(); i++) {
                Element prod_info = prodInformations.get(i);
                String reg_code = prod_info.elementText("ProductRegistrationCode");
                //20180905 判断取值
                String prod_code = "";

                if(prod_info.elementText("ProductCode") != null && !"".equals(prod_info.elementText("ProductCode"))  ) {
                    prod_code = prod_info.elementText("ProductCode");
                }else{
                    prod_code = prod_info.elementText("PreIdentificationCode");
                }

                err_cnt ++;

                param.put("prod_code",prod_code);
                param.put("reg_code",reg_code);
                param.put("workDate",DirectParams.preWorkDate);
                param.put("msg_type",msg_type);
                param.put("createTime",createTime);

                reportClearDao.insertOrUpdateProd(param);
            }
        }else {
            Element correctInformation = root.element("CorrectInformationTuple");//成功信息
            List<Element> errorInformations = root.elements("ErrorInformationTuple");
            //更新数据状态
            reportClearDao.updateRegisterStatus(exSeatMap.get(busiC) ,DirectParams.preWorkDate);
            if(errorInformations!=null && !errorInformations.isEmpty()){
                //获取错误信息
                for (int i = 0; i < errorInformations.size(); i++) {
                    Element error_info = errorInformations.get(i);
                    //String instanceFileName = error_info.elementText("InstanceFileName");
                    String instanceFileName = filename;
                    String instanceFileNo = error_info.elementText("InstanceFileNo");
                    String registrationSerialNumber = error_info.elementText("RegistrationSerialNumber");
                    String errorMessage = error_info.elementText("ErrorMessage");
                    String errorCode = "0000";
                    param.put("msg_type",msg_type);
                    param.put("workDate",DirectParams.preWorkDate);
                    param.put("register_serno",registrationSerialNumber);

                    if("WMRS.201.001.01".equals(msg_type) && (StringUtils.isBlank(errorMessage) || (!errorMessage.contains("投资者信息已登记") && !errorMessage.contains("投资者信息重复")))){
                        err_cnt ++;
                        errorCode = "9999";
                        reportClearDao.updateErrorRegisterStatus(param);
                    }
                    if(("WMRS.202.001.01".equals(msg_type)||"WMRS.203.001.01".equals(msg_type)) && (StringUtils.isBlank(errorMessage) || !errorMessage.contains("系统中已存在"))){
                        err_cnt ++;
                        errorCode = "9999";
                        reportClearDao.updateErrorRegisterStatus(param);
                    }
                    //截取表id
                    String tableId = instanceFileName.substring(instanceFileName.indexOf("-") + 1, instanceFileName.indexOf("-") + 4);

                    param.put("fileName",instanceFileName);
                    param.put("zzTable",tableId + "_" + DirectParams.dict_name_sys.get("zz_table_ch_name").get(tableId));
                    param.put("fileNo",instanceFileNo);
                    param.put("instanceFileName",instanceFileName);
                    param.put("instanceFileNo",instanceFileNo);
                    param.put("errorMessage",errorMessage);
                    param.put("errorCode",errorCode);
                    param.put("createTime",createTime);


                    reportClearDao.insertResultError(param);
                }

            }
        }
        return err_cnt;
    }

    /**
     * 同步发送文件
     * @param result
     * @throws Exception
     */
    public String sendRegisterFileSync(String filepath, SqlRow result) throws Exception{
        FprDClient fdc=new FprDClient();

        String origmsgid = "";
        String msgType= result.getString("msgtype");
        String fileName = result.getString("origfilename");
        String filePath=filepath + fileName;//上传文件路径
        String res=fdc.sendFileMsg(msgType, filePath);

        Document doc = DocumentHelper.parseText(res);	// 将字符串转为XML
        Element rootElt = doc.getRootElement(); // 获取根节点

        Element sysXcptn = rootElt.element("Document").element("SysXcptn");

        String xcptnRtrCd = sysXcptn.elementText("XcptnRtrCd");
        Element bizInfo = rootElt.element("MsgHeader").element("BizInfo");
        if(StringUtils.isBlank(origmsgid)){
            origmsgid = bizInfo.elementText("OrigMsgId");
        }else{
            origmsgid = origmsgid+","+bizInfo.elementText("OrigMsgId");
        }
        String xcptnRtrTx = sysXcptn.elementText("XcptnRtrTx");

        if(!"0000".equals(xcptnRtrCd)){
            if(DirectUtils.conUtilRe(xcptnRtrCd,"XTYC9001,XTYC9002,XTYC9004,XTYC9011,XTYC8013,XTYC8099,XTYC0004,XTYC0005,XTYC0006,XTYC0007,"
                    + "XTYC0010,XTYC0012,XTYC9014,XTYC9016,XTYC8999,XTYC9999")>0){
                xcptnRtrTx += ",请联系理财中心";
            }
            reportClearDao.updateFileStatusEx(DirectParams.preWorkDate,msgType,fileName,"","3", "send");
            throw new Exception(xcptnRtrTx);
        }
        String fileId = sysXcptn.elementText("FileId");
        reportClearDao.updateFileStatusEx(DirectParams.preWorkDate,msgType,fileName,fileId,"1", "send");
        return origmsgid;
    }

    /**
     * 下载结果文件
     * @param params
     * @throws Exception
     */
    public void downloadResuleFile(Map<String, Object> params,String resultPath , String workDate){
        FprDClient fdc = new FprDClient();
        String fileID = (String)params.get("fileId");//文件ID
        String savePath = "";
        try {
            savePath = fdc.downloadFile(fileID, resultPath);
            if(savePath == null){
                throw new Exception("文件下载异常！");
            }
            reportClearDao.updateFileStatusEx(workDate,"","",fileID, "6", "download");
        } catch (Exception e) {
            try {
                reportClearDao.updateFileStatusEx(workDate,"","",fileID, "5", "download");
            } catch (Exception e1) {
            }
        }
        //TODO 下载完的后续处理
    }

    /**
     * 检查资产负债要素是否报送
     * @param report_date
     * @param isRegisterFile
     * @return
     * @throws Exception
     */
    public String dealAssetCheck(String report_date,String isRegisterFile) throws Exception{
        String assets="";
        String app_btm_check = SysUtil.getSystemParamsByParaid("app_btm_check");
        List<SqlRow> assetList=new ArrayList<>();
        if("108".equals(isRegisterFile)){
            assetList=dataFileDao.dealAssetTradeCheck(report_date);
        }
        if("110".equals(isRegisterFile)){
            assetList=dataFileDao.dealAssetBTMCheck(report_date);
            if(assetList.size()==0 && "1".equals(app_btm_check))
                assetList=dataFileDao.dealAssetBTMCheckB(report_date);
        }
        if("117".equals(isRegisterFile)){
            assetList=dataFileDao.dealAssetHoldCheck(report_date);
            if(assetList.size()==0 && "1".equals(app_btm_check))
                assetList=dataFileDao.dealAssetHoldCheckB(report_date);
        }
        if(assetList.size()>0){
            for(SqlRow sqlRow : assetList){
                assets = assets+sqlRow.getString("asset_code")+"|";
            }
        }
        return assets;
    }

}
