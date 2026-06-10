package com.kayak.dps.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.service.business.BusinessBaseTaskService;
import com.kayak.clear.utils.Tools;
import com.kayak.common.SliceExecDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.app.dao.BaseReportReloadLogDao;
import com.kayak.dps.app.dao.ProdInfoOdsDao;
import com.kayak.dps.app.model.BaseReportReloadLog;
import com.kayak.dps.app.model.ProdInfoOds;
import com.kayak.dps.app.utils.AESUtils;
import com.kayak.dps.check.service.ReportDataValidateService;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.exception.TxtFileException;
import com.kayak.dps.ods.service.JCConfigService;
import com.kayak.dps.ods.util.zz.SFtpHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kayak.base.dao.util.DaoUtil.doTrans;

@Service
@APIDefine(desc = "贴源层产品信息表服务", model = ProdInfoOds.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProdInfoOdsService {
    private static final Logger logger = LoggerFactory.getLogger(ProdInfoOdsService.class);
    private static final Map<String, String> redoAppMap = new HashMap<>(); //重跑产品端报表任务
    private static final Map<String, String> redoAppTab = new HashMap<>(); //产品端对应报表

    static {
        redoAppMap.put("0","R041");//公私募产品申报登记
        redoAppMap.put("1","R022");//公私募产品发行登记
        redoAppMap.put("2","R023");//募集期总量登记
        redoAppMap.put("3","R052");//ZG01
        redoAppMap.put("4","R053");//ZG02
        redoAppTab.put("R041","app_prod_regist_filing_info");
        redoAppTab.put("R022","app_prod_issuance_regist_info");
        redoAppTab.put("R023","app_initial_sub_regist_info");
        redoAppTab.put("R052","APP_PBC_REPORT_ZG01");
        redoAppTab.put("R053","APP_PBC_REPORT_ZG02");

    }
    @Autowired
    private ProdInfoOdsDao prodInfoOdsDao;
    @Autowired
    private JCConfigService jcConfigService;
    @Autowired
    public ReportDataValidateService reportDataValidateService;
    @Autowired
    public BusinessBaseTaskService businessBaseTaskService;
    @Autowired
    public BaseReportReloadLogDao baseReportReloadLoDao;

    private final SliceExecDao sliceExecDao;

    private final ComnDao comnDao;

    public SqlResult<ProdInfoOds> findProdSeries(SqlParam<ProdInfoOds> params) throws Exception {
        return prodInfoOdsDao.findProdSeries(params);
    }


    public SqlResult<ProdInfoOds> findProdInfoOds(SqlParam<ProdInfoOds> params) throws Exception {
        return prodInfoOdsDao.findProdInfoOds(params);
    }

    public SqlResult<ProdInfoOds> findSubProdInfoOds(SqlParam<ProdInfoOds> params) throws Exception {
        return prodInfoOdsDao.findSubProdInfoOds(params);
    }

    //@API(desc = "修改产品信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateProdInfoOds(SqlParam<ProdInfoOds> params){
        try {
            prodInfoOdsDao.updateProdInfoOds(params.getModel());
            return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
        }catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "操作失败:" + e.getMessage(), null).toString();
        }
    }
    @API(desc = "修改产品信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateProdInfo(SqlParam<ProdInfoOds> params){
        try {
            ProdInfoOds prodInfoOds = params.getModel();
            String udpDt = DateUtil.getNowDate();
            prodInfoOds.setUpdDt(udpDt);
            prodInfoOds.setUpdTime(DateUtil.getNowTime());
            JSONObject submitData = (JSONObject) JSONObject.parse(prodInfoOdsDao.findWorkflowValues(prodInfoOds.getProdCode(),"updateProdInfo"));//提交标新数据
            JSONObject oldDate = submitData.getJSONObject("oldData");//历史表单数据
            //System.out.println("oldDate----------"+oldDate);
            Set<String> keys =new HashSet<>();
            String sql ="";
            //循环，找出修改的字段，拼接对应的修改标识data_flag
            for (String key:submitData.keySet()) {
                if(!submitData.getString(key).equals(oldDate.getString(key))){
                    if(!submitData.getString(key).equals("") || oldDate.getString(key) !=null){
                        sql = "select data_flag from base_prod_field_config  where field_code = '"+key+"'";
                        List<SqlRow> rs = comnDao.findRows(sql);
                        for (SqlRow row : rs) {
                            keys.add(row.getString("data_flag"));
                        }
                    }
                }
            }
            /*获取截止当前更新操作的当日修改的标识-产品维度*/
            String sql2 = "select data_flag,prod_code from ods_prod_base_info t where t.upd_dt = '"+DateUtil.getNowDate()+"' and t.prod_code = $S{prodCode} ";
            List<SqlRow> rs = comnDao.findRows(sql2,prodInfoOds);
            for (SqlRow row : rs) {
                keys.add(row.getString("data_flag"));
            }
            String dataFlag = StringUtils.join(keys.toArray(),",");

            //处理日期逻辑，清除掉标志位
            String subs_bdate = submitData.getString("subsBdate") , issuance_date = "" , issuance_regist_flag = "0";
            String establish_date = submitData.getString("establishDate") , establish_flag = "0";
            if(StringUtils.isNotBlank(subs_bdate)) {
                sql = "select workday from sys_workday_set sws where pgmno ='001' and workday < "+subs_bdate+" order by workday desc limit 1 ";
                List<SqlRow> rs01 = comnDao.findRows(sql);
                for (SqlRow row : rs01) {
                    issuance_date = row.getString("workday");
                }
                //发行登记 是upddt 与募集起始日前一工作日，进行判断，大于等于，才写入data_flag = 1，逻辑本身有data_flag判断，因此判断后清除标志位
                if(udpDt.compareTo(issuance_date) < 0){
                    issuance_regist_flag = "1";
                }
            }
            //募集总量和ZG01是成立后报送,2和3 逻辑本身有data_flag判断，因此判断后清除标志位
            if(StringUtils.isNotBlank(establish_date) && udpDt.compareTo(establish_date) <= 0) {
                establish_flag = "1";
            }

            // TODO 比较1.0版本和1.1版本数据，若数据无差异，则不生成该1.1版本数据对应表的标识

            if(issuance_regist_flag.equals("1")) {
                //针对延后发行这种，已经生成发行登记的情况，直接删除已经生成的发行登记即可。
                prodInfoOdsDao.deleteIssuanceRegist(prodInfoOds);
                //排除提前已经生成并报送成功的发行登记的情况，存在已经报送的发行登记时，不可清除标识
                String cnt = prodInfoOdsDao.findIssuanceRegist(prodInfoOds);
                if(cnt.compareTo("1") < 0){
                    dataFlag = dataFlag.replace("1,","").replace(",1","").replace("1","");
                }
            }

            if(establish_flag.equals("1")) {
                dataFlag = dataFlag.replace("2,","").replace(",2","").replace("2","");
                dataFlag = dataFlag.replace("3,","").replace(",3","").replace("3","");
            }

            prodInfoOds.setDataFlag(dataFlag);
            prodInfoOdsDao.updateProdInfo(prodInfoOds);
            return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "操作失败:" + e.getMessage(), null).toString();
        }
    }

    //@API(desc = "修改登记申报信息", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateProdInfoReport (SqlParam<ProdInfoOds> params) {
        try {
            prodInfoOdsDao.updateProdInfoReport(params.getModel());
            return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
        }catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "操作失败:" + e.getMessage(), null).toString();
        }
    }
    //@API(desc = "批量发送文件", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String batchSendProdFile(SqlParam<ProdInfoOds> object) throws Exception {
        try {
            //根据前端查询条件/勾选的产品信息集合组装
            //文件时间戳
            String time_stamp = DateUtil.getTimestamp17();
            String sysDate = DateUtil.getSysWordDay();
            Map<String, Object> params = RequestSupport.getParameters();
            params.put("time_stamp",time_stamp);
            params.put("deal_date",sysDate);
            String obj = (String) params.get("list");
            List<Map> list = JSONObject.parseArray(obj, Map.class);
            List<ProdInfoOds> prodList = new ArrayList<>();
            for (Map json : list) {
                ProdInfoOds prod = BeanUtil.mapToBean(json, ProdInfoOds.class, false);
                prodList.add(prod);
            }
            doSendFileAction(prodList, params);
        } catch (Exception e) {
            Map<String, Object> params = RequestSupport.getParameters();
            updateFileLog((String) params.get("sequence"), Constants.FILE_STATE_02, "处理失败:" + e.getMessage(), 0);
            return RequestSupport.updateReturnJson(false, "批量发送文件失败!" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "批量发送文件成功!", null).toString();
    }

    //@API(desc = "自动发送文件", auth = APIAuth.NO)
    public String autoGenerateProdFileTask(SqlParam<ProdInfoOds> params) throws Exception {
        try {
            //文件时间戳
            String time_stamp = DateUtil.getTimestamp17();
            String sysDate = DateUtil.getSysWordDay();
            Map<String, Object> param = new HashMap<>();
            param.put("time_stamp",time_stamp);
            param.put("deal_date",sysDate);
            ProdInfoOds prodRule = new ProdInfoOds();
            List<ProdInfoOds> prodList = prodInfoOdsDao.findProdList(prodRule);
            if (prodList.size() == 0) {
                logger.info("未查询到相关信息");
            }else{
                doSendFileAction(prodList,param);
                logger.info("自动发送文件成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "文件发送失败："+e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件发送成功", null).toString();
    }

    public void  doSendFileAction (List<ProdInfoOds> prodList, Map<String, Object> params) throws Exception {
        try {
            logger.info("****************批量发送文件 Start*********************");
            Map<String, Object> configMap = jcConfigService.getConfigInfo(Constants.CONFIG_TYPE_CPTS);
            // 文件名称、路径处理，替换占位符
            String fileName = this.fileNamePretreatment((String) configMap.get(Constants.FILE_NAME), params);
            String localPath = this.fileNamePretreatment((String) configMap.get(Constants.LOCAL_PATH), params);
            String userName = AESUtils.AESDecrypted((String)configMap.get(Constants.USERNAMES));
            String passWord = AESUtils.AESDecrypted((String)configMap.get(Constants.PASSWORD));
            String okFileName = fileName + ".ok";
            // 本地路径生成
            File file = new File(localPath);
            file.mkdirs();
            // 记录文件流水
            createFileLog(params, localPath + fileName);
            writeSimpleFile(prodList, localPath + fileName, params);
            // 创建ok文件
            File okFile = new File(localPath, okFileName);
            okFile.createNewFile();
            // 文件上传
            if (StringUtils.equals(Constants.Y, (String) configMap.get(Constants.IS_UPLOAD))) {
                String remotePath = this.fileNamePretreatment((String) configMap.get(Constants.REMOTE_PATH), params);
                SFtpHelper.putFile((String) configMap.get(Constants.SFTP_IP), userName, passWord, remotePath, localPath, fileName, "1");
                SFtpHelper.putFile((String) configMap.get(Constants.SFTP_IP), userName, passWord, remotePath, localPath, okFileName, "1");
            }
            // 更新推送文件流水-成功
            updateFileLog((String) params.get("sequence"), Constants.FILE_STATE_03, "处理成功", prodList.size());
            // 更新产品文件发送状态
            updateProdStatus(prodList);
            logger.info("****************批量发送文件 end*********************");
        }catch (Exception e) {
            e.printStackTrace();
            logger.info("****************批量发送文件失败*********************"+e.getMessage());
        }
    }
    /**
     * 文件名称变量处理
     */
    public String fileNamePretreatment(String fileName, Map<String, Object> params) {
        String regex = "\\[(.*?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(fileName);

        // 获取文件名变量
        List<String> variables = new ArrayList<>();
        while (m.find()) {
            variables.add(m.group(1));
        }

        if (CollectionUtils.isNotEmpty(variables)) {
            for (String variable : variables) {
                if (params.get(variable) == null) {
                    throw new RuntimeException("文件名变量 " + variable + " 未赋值，请检查参数");
                }
                fileName = fileName.replace("[" + variable + "]", (String) params.get(variable));
            }
        }
        return fileName;
    }

    /**
     * 对接产品系统生成TXT文件
     *
     */
    public static void writeSimpleFile(List<ProdInfoOds> values, String filePath, Map<String,Object> params) {
        // 列分隔符
        final String COLUMN_SPLIT = "|";
        // 行分隔符
        final byte[] LINE_SEPARATOR = {'\r', '\n'};
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // 写文件体
            int i =1;
            for (ProdInfoOds prodInfos : values) {
                fos.write(prodInfos.getProdCode().getBytes(StandardCharsets.UTF_8));
                fos.write(COLUMN_SPLIT.getBytes(StandardCharsets.UTF_8));
                fos.write(prodInfos.getCheckInon().getBytes(StandardCharsets.UTF_8));
                fos.write(COLUMN_SPLIT.getBytes(StandardCharsets.UTF_8));
                fos.write(prodInfos.getProdStatus().getBytes(StandardCharsets.UTF_8));
                if(i!=values.size()){
                    fos.write(LINE_SEPARATOR);
                }
                i++;
            }
            logger.info(" 生成TXT文件完成: 共{}行数据, 生成文件路径: {}", values.size(), filePath);
        }catch (Exception e){
            logger.error("TXT文件生成失败: ", e);
            throw new TxtFileException("TXT文件生成失败: " + filePath);
        }
    }

    /**
     * 记录推送文件流水
     * @param params
     * @param fileName
     */
    public void createFileLog(Map<String, Object> params, String fileName) throws Exception {
        // 获取唯一序列
        String sequence = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        params.put("sequence", sequence);
        params.put("file_address", fileName);
        params.put("file_state", Constants.FILE_STATE_01);
        params.put("deal_user_id", SysUtil.getSysUserParamValue("sys_user_userid"));
        prodInfoOdsDao.createFileLog(params);
    }
    /**
     * 更新推送文件流水状态
     * @param
     * @param fileState
     * @param message
     * @param totalNum
     */
    public void updateFileLog(String sequence, String fileState, String message, Integer totalNum) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("sequence", sequence);
        params.put("message", message);
        params.put("fileState", fileState);
        params.put("totalNum", totalNum);
        params.put("deal_user_id", SysUtil.getSysUserParamValue("sys_user_userid"));
        prodInfoOdsDao.updateFileLog(params);
    }

    public void updateProdStatus(List<ProdInfoOds> prods) throws Exception {
        Map<String, Object> params = new HashMap<>();
        for (ProdInfoOds prodInfos : prods) {
            params.put("id",prodInfos.getId());
            prodInfoOdsDao.updateProdStatus(params);
        }
    }
    @API(desc = "生成产品端报表", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateTask(SqlParam<ProdInfoOds> params) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("paraid","90000061002");
            List<SqlRow> sqlRowList = businessBaseTaskService.quTaskStatus(map);
            if (CollectionUtil.isEmpty(sqlRowList) || (CollectionUtil.isNotEmpty(sqlRowList) && "1".equals(sqlRowList.get(0).getString("paravalue")))) {
                return RequestSupport.updateReturnJson(true, "正在生成产品端报表，请稍后重试！", null).toString();
            }
            if ("1".equals(SysUtil.getSystemParamsByParaid("90000061000"))) {
                return RequestSupport.updateReturnJson(true, "系统清算流程正在执行中，请稍后重试！", null).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "生成产品端报表失败！", null).toString();
        }
        return updateProductTask(params);
    }

    public String updateProductTask(SqlParam<ProdInfoOds> params) throws Exception {
        String maxId = "";
        String reports = "";

        Map<String, Object> map = new HashMap<>();
        map.put("paravalue","1");
        map.put("oldParavalue","0");
        map.put("paraid","90000061002");

        BaseReportReloadLog baseReportReloadLog = new BaseReportReloadLog();
        baseReportReloadLog.setMenuId("M0605");
        baseReportReloadLog.setReportDate(DateUtil.getLastSysWordDay(DateUtil.getNowDate()));
        baseReportReloadLog.setStartDate(DateUtil.getNowDate());
        baseReportReloadLog.setStartTime(DateUtil.getNowTime());
        baseReportReloadLog.setResultStatus("正在生成产品端报表中");
        baseReportReloadLog.setUserName(SysUtil.getLoginUserid());

        try {
            if (businessBaseTaskService.upTaskStatus(map) > 0) {
                baseReportReloadLoDao.addBaseReportReloadLog(baseReportReloadLog);
                List<SqlRow> sqlRows = baseReportReloadLoDao.findIdBaseReportReloadLogs(baseReportReloadLog);
                if (CollectionUtil.isNotEmpty(sqlRows)) {
                    maxId = sqlRows.get(0).getString("id");
                }

                //生成产品端报表
                Map<String, Object> param=new HashMap<>();
                param.put("deal_date",DateUtil.getLastSysWordDay(DateUtil.getNowDate()));
                String sqlDwd = "select paravalue from sys_param where paraid = '90000051701' ";
                List<SqlRow> rs = comnDao.findRows(sqlDwd);
                doTrans(() -> {
                    // 重跑产品基本信息表
                    for (SqlRow row : rs) {
                        String [] modules = row.getString("paravalue").split(",");
                        for (String taskId: modules) {
                            List<SqlRow> sqlStrs = ExeQuery.queryPortSqlByTaskId(taskId);
                            for (SqlRow sqlStr : sqlStrs) {
                                comnDao.update(sqlStr.getString("sqlstr"));
                            }
                        }
                    }
                    //查询当天修改的产品信息涉及到哪些报表需要重跑
                    String sqlFlow = "select data_flag,prod_code from ods_prod_base_info t where t.upd_dt = '"+DateUtil.getNowDate()+"'";
                    String sqlWhere = "select sql_id,sql_where from base_prod_where_config ";
                    String fianlSqlstr ="";
                    List<SqlRow> rs1 = comnDao.findRows(sqlFlow);
                    List<SqlRow> rs2 = comnDao.findRows(sqlWhere);
                    String [] dataFlags;
                    for (SqlRow row : rs1) {
                        dataFlags=row.getString("data_flag").split(",");
                        Set<String> set = new LinkedHashSet<>(Arrays.asList(dataFlags));
                        dataFlags = set.toArray(new String[0]);
                        param.put("prodCode",row.getString("prod_code"));
                        //匹配任务,重新生成报表
                        for (String key : dataFlags) {
                            List<SqlRow> sqlToApp = ExeQuery.queryPortSqlByTaskId(redoAppMap.get(key));
                            for (SqlRow sqlStr : sqlToApp) {
                                for (SqlRow row2 : rs2) {
                                    if(sqlStr.getString("sqlid").equals(row2.getString("sql_id"))){
                                        fianlSqlstr = sqlStr.getString("sqlstr")+row2.getString("sql_where");
                                    }
                                }
                                comnDao.update(fianlSqlstr, DataSourceProperty.PUB, param);
                            }
                            if (sqlToApp.size() > 0) {
                                this.getDateVersionByDateAndUpdate(key, DateUtil.getNowDate(), row.getString("prod_code"));
                            }

                            //根据申报、发行、募集期总量数据删除高版本重复数据
                            checkUnchangedData(key, param);

                        }
                    }
                    //更新跑批状态
                    String upStatus = "update sys_param set paravalue = '1' where paraid = '90000051601'";
                    comnDao.update(upStatus);
                });

                //发起指标校验
                //查询当天修改的产品信息涉及到哪些报表需要重跑
                String [] dataFlags;
                Set<String> set;
                String sqlFlowEdit = "select group_concat(data_flag) data_flag from ods_prod_base_info t where t.upd_dt = '"+DateUtil.getNowDate()+"' group by upd_dt";
                Map<String, String> params_check = new HashMap<>();
                List<SqlRow> rs3 = comnDao.findRows(sqlFlowEdit);
                for (SqlRow row : rs3) {
                    dataFlags=row.getString("data_flag").split(",");
                    set = new LinkedHashSet<>(Arrays.asList(dataFlags));
                    dataFlags = set.toArray(new String[0]);
                    for (String key : dataFlags) {
                        if(StringUtils.isNotEmpty(key)){
                            List<SqlRow> thDate = ExeQuery.queryThdateByReportTable(redoAppTab.get(redoAppMap.get(key)));
                            for (SqlRow sqlStr : thDate) {
                                params_check.put("reportType", "02");//报表大类
                                params_check.put("reportTable", redoAppTab.get(redoAppMap.get(key)));//报表名称
                                reportDataValidateService.execute(sqlStr.getString("theory_report_start_date"), params_check);
                            }
                        }
                    }
                }
                //更新人行登记编码
                updatePbcTask(params);

                // 执行完成后更新数据分布式锁
                map.put("paravalue","0");
                map.put("oldParavalue","1");
                businessBaseTaskService.upTaskStatus(map);

                baseReportReloadLog.setId(maxId);
                baseReportReloadLog.setEndDate(DateUtil.getNowDate());
                baseReportReloadLog.setEndTime(DateUtil.getNowTime());
                baseReportReloadLog.setResultStatus("生成产品端报表成功");
                baseReportReloadLog.setResultInfo("生成产品端报表成功");
                baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);
            } else {
                return RequestSupport.updateReturnJson(true, "正在生成产品端报表，请稍后重试！", null).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 执行失败后更新数据分布式锁
            map.put("paravalue","0");
            map.put("oldParavalue","1");
            businessBaseTaskService.upTaskStatus(map);

            baseReportReloadLog.setId(maxId);
            baseReportReloadLog.setEndDate(DateUtil.getNowDate());
            baseReportReloadLog.setEndTime(DateUtil.getNowTime());
            baseReportReloadLog.setResultStatus("生成产品端报表失败");
            baseReportReloadLog.setResultInfo(e.getMessage());
            baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);

            return RequestSupport.updateReturnJson(false, "生成产品端报表失败！", null).toString();
        }

        return RequestSupport.updateReturnJson(true, "生成产品端报表成功！", null).toString();
    }

    /**
     * 更新人行编码
     * @param params
     * @return
     * @throws Exception
     */
    public void updatePbcTask(SqlParam<ProdInfoOds> params) throws Exception {
        doTrans(() -> {
            //获取更新人行编码配置表的信息
            //update 'table_name' t1 join '产品表' prod on t1.product_code_col = prod.product_code
            List<HashMap<String, Object>> list = new ArrayList<>();
            String updateConfig = "select table_name,product_code_col ,register_code_col from report_update_config where enable_flag  = '1'";
            List<SqlRow> updateRows = comnDao.findRows(updateConfig);
            for (SqlRow updateRow : updateRows) {
                HashMap<String, Object> updateParams = new HashMap<>();
                String tableNameKey = "table_name";
                String tableName = updateRow.getString(tableNameKey);
                updateParams.put(tableNameKey,tableName);
                String productCodeColKey = "product_code_col";
                String productCodeCol = updateRow.getString(productCodeColKey);
                updateParams.put(productCodeColKey,productCodeCol);
                String registerCodeColKey = "register_code_col";
                String registerCodeCol = updateRow.getString(registerCodeColKey);
                updateParams.put(registerCodeColKey,registerCodeCol);
                list.add(updateParams);
            }
            prodInfoOdsDao.updateReport(list);
            //更新跑批状态
            String upStatus = "update sys_param set paravalue = '1' where paraid = '90000051601'";
            comnDao.update(upStatus);
        });
        //return RequestSupport.updateReturnJson(true, "处理成功", null).toString();
    }

    @API(desc = "查看重跑任务状态", auth = APIAuth.NO)
    public String findTask(SqlParam<ProdInfoOds> params) throws Exception {
        Map<String, Object> param=new HashMap<>();
        Map<String, Object> rtnParam=new HashMap<>();
        String queryStr="select paravalue from sys_param where paraid = '90000051601'";
        String cnt = comnDao.findRow(queryStr, param).getString("paravalue");
        if (Integer.valueOf(cnt) == 0) {
            rtnParam.put("loading","false");
        } else {
            rtnParam.put("loading","true");
        }
        return RequestSupport.updateReturnJson(true, "查询成功", rtnParam).toString();
    }

    /**
     *@description  根据App表，日期，唯一主键 更新数据最新版本号
     *
     */
    private  void   getDateVersionByDateAndUpdate (String key,String dealDate,String prodCode) {
        String version = "1.0";
        try {
            //中债
            StringBuilder sql =  new StringBuilder(" SELECT  max(sys_data_version) as  sys_data_version from  "+redoAppTab.get(redoAppMap.get(key))+" ");
            StringBuffer  sqlWhere = new StringBuffer();
            if(key.equals("0")){
                sql.append("  t left join dwd_prd_prd_bas_inf t1 on t1.prod_cd   = t.ident_code where  register_status ='3' and t1.prod_cd = '"+prodCode+"'");
                sqlWhere.append(" and ident_code='"+prodCode+"'");
            }else if(key.equals("1")){
                sql.append("  t left join dwd_prd_prd_bas_inf t1 on t1.prod_reg_enc  = t.prod_code where  register_status ='3' and t1.prod_cd =  '"+prodCode+"'");
                sqlWhere.append(" and prod_code = (select prod_reg_enc from dwd_prd_prd_bas_inf where prod_cd =  '"+prodCode+"') ");
            }else if(key.equals("2")){
                sql.append("  t left join dwd_prd_prd_bas_inf t1 on t1.prod_reg_enc  = t.prod_code where  register_status ='3' and t1.prod_cd = '"+prodCode+"'");
                sqlWhere.append(" and prod_code = (select prod_reg_enc from dwd_prd_prd_bas_inf where prod_cd =  '"+prodCode+"') ");
            }else{
                sql.append("   t left join dwd_prd_prd_bas_inf t1 on t1.prod_cd  = t.isu_org_prod_cd where  register_status ='3'  and t1.prod_cd = '" + prodCode + "'");
                sqlWhere.append(" and isu_org_prod_cd = (select prod_cd from dwd_prd_prd_bas_inf where prod_cd =  '"+prodCode+"') ");
            }
            List<SqlRow> list = comnDao.findRows(sql.toString());
            if(list.isEmpty()  || Tools.strIsEmpty(list.get(0).getString("sys_data_version"))){
                return ;
            }
            // 增加新的版本号
            BigDecimal existVersion =  new BigDecimal(list.get(0).getString("sys_data_version"));
            existVersion = existVersion.add(new BigDecimal("0.1"));
            String upDateSql = "update "+redoAppTab.get(redoAppMap.get(key))+" set sys_data_version = '"+existVersion+"' where  create_date = '"+dealDate+"' and REGISTER_STATUS <>'3'  "+sqlWhere+" ";
            comnDao.update(upDateSql);
            if(key.equals("3")){
                /*更新信息类型*/
                String upDateTypSql = "update "+redoAppTab.get(redoAppMap.get(key))+" set MSG_TYP = '2' where  create_date = '"+dealDate+"' and REGISTER_STATUS <>'3' "+sqlWhere+" ";
                String upReportDate = "update "+redoAppTab.get(redoAppMap.get(key))+" set REPORT_DATE = '"+dealDate+"' where  create_date = '"+dealDate+"' and REGISTER_STATUS <>'3' "+sqlWhere+" ";
                comnDao.update(upReportDate);
                comnDao.update(upDateTypSql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查并删除多版本号报表当日重复数据
     * @param key
     * @param params
     */
    public void checkUnchangedData (String key, Map<String, Object> params) {
        try {
            if ("1".equals(key)) {//公私募产品申报登记
                String repeatQuery = "select a.ident_code,a.sys_data_version from app_prod_regist_filing_info a where a.sys_data_status = '1' and a.sys_data_version > '1.0' and a.create_date = " + DateUtil.getNowDate();
                List<SqlRow> rowList = comnDao.findRows(repeatQuery, DataSourceProperty.PUB, params);
                for(SqlRow sqlRow : rowList) {
                    //获取公私募产品申报登记报表数据
                    BigDecimal minus_version = new BigDecimal(sqlRow.getString("sys_data_version")).subtract(BigDecimal.valueOf(0.1));
                    String sb_sql = "select count(*) as cnt,b.ident_code from app_prod_regist_filing_info b where b.ident_code = '" + sqlRow.getString("ident_code") + "'" +
                            " and b.sys_data_version >= '" + minus_version + "' group by " +
                            " prod_name,ifnull(prod_brand,''),prod_term_no,bank_code,prod_aprv_nm,approver_id_code,prod_dsn_nm,designer_id_code,inv_mng_nm,manager_id_code,contact_name,"+
                            " contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,ifnull(invert_country,''),ifnull(service_mode,''),"+
                            " operation_mode,ifnull(min_hold_period,''),ifnull(min_hold_day,''),ifnull(option_redempt_period,''),ifnull(cash_manager,''),asset_ac_method,prod_mana_mode,ifnull(ac_mana_name,''),price_method,invest_type,"+
                            " cooperate_mode,ifnull(cooperator,''),invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,"+
                            " start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,ifnull(dc_cd_name,''),ifnull(dc_cd_ident_code,''),ifnull(seas_cd_nation,''),"+
                            " ifnull(seas_cd_name,''),cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,ifnull(crt_ins_type,''),ifnull(prod_crt_method,''),ifnull(details,''),ifnull(new_prod,''),ifnull(prod_esp_prpt,'') ";
                    if (comnDao.findRow(sb_sql, DataSourceProperty.PUB, null).getString("cnt").compareTo("1") > 0) {
                        comnDao.update("delete from app_prod_regist_filing_info where ident_code = '" + sqlRow.getString("ident_code") + "' and sys_data_version = '" + sqlRow.getString("sys_data_version") + "' ");
                    }
                }
            } else if ("2".equals(key)) {//发行登记
                String repeatQuery = "select a.prod_ident_code,a.sys_data_version from app_prod_issuance_regist_info a where a.sys_data_status = '1' and a.sys_data_version > '1.0' and a.create_date = " + DateUtil.getNowDate();
                List<SqlRow> rowList = comnDao.findRows(repeatQuery, DataSourceProperty.PUB, params);
                for(SqlRow sqlRow : rowList) {
                    //获取公私募产品申报登记报表数据
                    BigDecimal minus_version = new BigDecimal(sqlRow.getString("sys_data_version")).subtract(BigDecimal.valueOf(0.1));
                    String sb_sql = "select count(*) as cnt,prod_ident_code from app_prod_issuance_regist_info b where b.prod_ident_code = '" + sqlRow.getString("prod_ident_code") + "'" +
                            " and b.sys_data_version >= '" + minus_version + "' group by " +
                            "PROD_CODE,BANK_CODE,PROD_IDENT_CODE,SUBSCRIPTION_START_DATE,SUBSCRIPTION_END_DATE,PROD_VALUE_DATE,PROD_MATURITY_DATE,MANAGEMENT_METHOD,STRUCTURED_PROD," +
                            "ifnull(DETAILS_PER_RATE,''),OPENING_MODE,UP_LIMIT_PER_RATE,LOW_LIMIT_PER_RATE,ifnull(REGULAR_OPEN_PERIOD,'')," +
                            "REGULAR_OPEN_PERIOD_DAY,OTHER_OPEN_PERIOD,ifnull(DISORDER_OPEN_PERIOD,''),ifnull(FIRST_OPEN_DAY,''),ifnull(HOLIDAY_OPEN_TYPE,''),AVERAGE_OPEN_NO," +
                            "ifnull(BUSI_OPEN_PERIOD,''),ifnull(DETAILS_BUSI_OP_PERIOD,'')," +
                            "ifnull(CUSTODY_ACCT_NO,''),ifnull(CUSTODY_ACCT_NAME,''),CLSF_STO ";
                    if (comnDao.findRow(sb_sql, DataSourceProperty.PUB, null).getString("cnt").compareTo("1") > 0) {
                        comnDao.update("delete from app_prod_issuance_regist_info where prod_ident_code = '" + sqlRow.getString("prod_ident_code") + "' and sys_data_version = '" + sqlRow.getString("sys_data_version") + "' ");
                    }
                }
            } else if ("3".equals(key)) {//募集总量登记
                String repeatQuery = "select a.prod_code,a.sys_data_version from app_initial_sub_regist_info a where a.sys_data_status = '1' and a.sys_data_version > '1.0' and a.create_date = " + DateUtil.getNowDate();
                List<SqlRow> rowList = comnDao.findRows(repeatQuery, DataSourceProperty.PUB, params);
                for(SqlRow sqlRow : rowList) {
                    //获取公私募产品申报登记报表数据
                    BigDecimal minus_version = new BigDecimal(sqlRow.getString("sys_data_version")).subtract(BigDecimal.valueOf(0.1));
                    String sb_sql = "select count(*) as cnt,prod_code from app_initial_sub_regist_info b where b.prod_code = '" + sqlRow.getString("prod_code") + "'" +
                            " and b.sys_data_version >= '" + minus_version + "' group by " +
                            "bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,ifnull(details,'')," +
                            "actual_subscribed_amt,subscribed_vol,amt_other_db_agents,FND_TRST_ACT_NBR,FND_TRST_ACT,zon_clc_amt,prod_ccy,FOUND_DT ";
                    if (comnDao.findRow(sb_sql, DataSourceProperty.PUB, null).getString("cnt").compareTo("1") > 0) {
                        comnDao.update("delete from app_initial_sub_regist_info where prod_code = '" + sqlRow.getString("prod_code") + "' and sys_data_version = '" + sqlRow.getString("sys_data_version") + "' ");
                    }
                }
            }

        } catch (Exception e) {
            logger.info("删除当日生成产品端报表高版本数据异常:产品代码 " + params.get("prod_code") + ","+ e.getMessage());
        }

    }

    /**
     *@description  发行要素【募集起始日期】是否在申报登记募集日期区间
     *
     */
    public String prodInfoCheck(SqlParam<ProdInfoOds> params) throws Exception {
        try {
            SqlResult<ProdInfoOds> sqlResult = prodInfoOdsDao.prodInfoCheck(params);
            if (CollectionUtil.isNotEmpty(sqlResult.getRows())) {
                return RequestSupport.updateReturnJson(true, "此产品发行要素【募集起始日期】不在申报登记【募集起始日期（从）】和【募集起始日期（到）】之间！", null).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "发行要素【募集起始日期】校验失败，请联系管理员！", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "", null).toString();
    }

}