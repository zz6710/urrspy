package com.kayak.dps.ods.service;

import com.alipay.rdf.file.interfaces.FileFactory;
import com.alipay.rdf.file.interfaces.FileReader;
import com.alipay.rdf.file.interfaces.FileSplitter;
import com.alipay.rdf.file.model.FileConfig;
import com.alipay.rdf.file.model.FileDefaultConfig;
import com.alipay.rdf.file.model.FileSlice;
import com.alipay.rdf.file.model.StorageConfig;
import com.alipay.rdf.file.loader.TemplateLoader;
import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.utils.Tools;
import com.kayak.config.utils.DbopChange;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.check.dao.T8portInfoDao;
import com.kayak.dps.check.model.T8PortInfoModel;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.dao.DealValuePortDao;
import com.kayak.dps.ods.util.DBFFileUtil;
import com.kayak.dps.ods.util.SourceDataChgUtil;
import com.kayak.dps.ods.util.TxtFileUtil;
import com.kayak.dps.ods.util.XmlFileUtil;
import com.kayak.graphql.model.FetcherData;
import com.kayakwise.kcloud.db.util.ParamMap;
import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class DealValuePortService {
    private static final Logger logger = LoggerFactory.getLogger(DealValuePortService.class);
    @Resource(name = "dealValuePortDao")
    private DealValuePortDao dealValuePortDao;
    private static String data_sources = "";

    @Autowired
    private DbopChange dbopChange;

    @Autowired
    private T8portInfoDao t8portInfoDao;

    @Autowired
    public ComnDao comnDao;
    private static final ParamMap[] EMPTY_PARAMMAP_ARRAY = new ParamMap[0];
    @Value("${kcloud.batch.threadpool.slice.max_pool_size}")
    private  Integer maxPoolSize;
    @Autowired
    private ThreadPoolTaskExecutor threadPool;
    /**
     * 获取接口信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getAllPortInfo(Map<String, Object> params) throws Exception {
        return dealValuePortDao.getAllPortInfo(params);
    }

    /**
     * 记录估值文件流水
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
        dealValuePortDao.createFileLog(params);
    }

    /**
     * 组装DBF文件接口头信息
     * @return
     * @throws Exception
     */
    public DBFField[] getPortField(Map<String,Object> params) throws Exception{
        List<SqlRow> rs = dealValuePortDao.queryFieldList(params);

        // 初始化文件头对象
        List<DBFField> header = new ArrayList<>();

        // 转换字段信息
        for (SqlRow sqlRow : rs) {
            DBFField field = new DBFField();
            if (sqlRow.getString("field_code").length() < 10) {
                field.setName(sqlRow.getString("field_code"));                            // 字段名
            } else {
                field.setName(sqlRow.getString("field_code").substring(0, 10));                            // 字段名
            }
            if (StringUtils.equals(sqlRow.getString("field_type"), "VARCHAR2")) {    // 字段类型
                field.setType(DBFDataType.CHARACTER);
            } else if (StringUtils.equals(sqlRow.getString("field_type"), "INTEGER")) {
                field.setType(DBFDataType.NUMERIC);
            } else if (StringUtils.equals(sqlRow.getString("field_type"), "NUMBER")) {
                field.setType(DBFDataType.NUMERIC);
            }
            if (StringUtils.isNotBlank(sqlRow.getString("field_length"))) {// 字段长度
                field.setLength(sqlRow.getInteger("field_length"));
            }
            if (StringUtils.isNotBlank(sqlRow.getString("field_dights"))) {// 字段小数位
                field.setDecimalCount(sqlRow.getInteger("field_dights"));
            }
            header.add(field);
        }

        if(CollectionUtils.isEmpty(header)){
            throw new RuntimeException("文件头信息未配置");
        }

        return header.toArray(new DBFField[header.size()]);
    }

    /**
     * 组装接口内容
     * @return
     * @throws Exception
     */
    public List<Object[]> queryPortContent(Map<String,Object> params) throws Exception{
        List<String> fieldCodeList = dealValuePortDao.queryAllFieldCode(params);
        if(CollectionUtils.isEmpty(fieldCodeList)){
            throw new RuntimeException("接口字段未配置");
        }
        String limit = SysUtil.getSystemParamsByParaid("90000051211"); // 查询分页数
        String sql = "select * from (" + ExeQuery.queryExeId(params.get("port_exeid").toString()) + ") t" + " limit " + limit;
        List<SqlRow> rs = dealValuePortDao.queryPortContent(sql, params);
        params.put("totalNum", rs.size());
        if (CollectionUtils.isNotEmpty(rs)) {
            params.put("last_id", rs.get(rs.size()-1).get("ID"));
        }
        List<Object[]> portContentList =new ArrayList<>();
        for (SqlRow sqlRow : rs) {
            Object[] portContent = new Object[fieldCodeList.size()];
            for (int i = 0; i < fieldCodeList.size(); i++) {
                portContent[i] = sqlRow.get(fieldCodeList.get(i));
            }
            portContentList.add(portContent);
        }
        return portContentList;
    }

    public Integer queryPortContentCount(Map<String,Object> params) throws Exception{
        List<String> fieldCodeList = dealValuePortDao.queryAllFieldCode(params);
        if(CollectionUtils.isEmpty(fieldCodeList)){
            throw new RuntimeException("接口字段未配置");
        }
        String sql = ExeQuery.queryExeId(params.get("port_exeid").toString());
        sql = "select count(1) as count from (" + sql + ") s";
        List<SqlRow> rs = dealValuePortDao.queryPortContent(sql, params);
        params.put("totalNum", rs.size());
        List<Object[]> portContentList =new ArrayList<>();
        if (!rs.isEmpty()) {
            return rs.get(0).getInteger("count");
        }
        return 0;
    }

    /**
     * 组装TXT文件接口头信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<String> queryFieldList(Map<String,Object> params) throws Exception {
        return dealValuePortDao.queryAllFieldCode(params);
    }

    /**
     * 更新估值文件流水状态
     * @param sequence
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
        dealValuePortDao.updateFileLog(params);
    }

    /**
     * 回传文件批量入库
     * @param params
     * @throws Exception
     */
    public void batchSave(Map<String, Object> params) throws Exception {

        params = SourceDataChgUtil.initData(params,dealValuePortDao);
        StringBuilder delSql = new StringBuilder().append("delete from ").append(params.get("port_table")).append(" where 1=1 ");
        if ("1".equals(params.get("synch_type").toString())) {
            // 先删后增
            delSql.append(" and deal_date = $S{deal_date}");
            dealValuePortDao.deleteFromDate(delSql.toString(), params);
        } else if ("3".equals(params.get("synch_type").toString())) {
            //全删全插
            delSql = new StringBuilder().append("truncate table ").append(params.get("port_table"));
            dealValuePortDao.deleteFromDate(delSql.toString(), params);
        }

        String sliceFlag = params.get("slice_flag").toString();
        // 解析入库字段，拼接SQL
        List<String> fieldList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        StringBuffer vars = new StringBuffer();
        sb.append(" insert into ").append(params.get("port_table")).append(" ( ");
        List<SqlRow> rs = dealValuePortDao.queryFieldList(params);
        boolean flag = false;
        List<String> fieldType = new ArrayList<>();
        // 每个表表字段字数
        int size = 0;
        for (SqlRow sqlRow : rs) {
            // 拼接逗号
            if (flag) {
                sb.append(",");
                vars.append(",");
            } else {
                flag = true;
            }
            size++;
            // 字段
            sb.append(sqlRow.getString("field_code"));
            fieldType.add(sqlRow.getString("field_type"));
            // 占位变量
            vars.append("?");

            // 字段名
            fieldList.add(sqlRow.getString("field_code"));
        }
        sb.append(" ) values ( ").append(vars).append(" )");
        log.info(" 入库SQL："+sb);
        if(CollectionUtils.isEmpty(fieldList)){
            throw new DBFException("未配置字段信息");
        }
        params.put("sb",sb);
        params.put("fieldType",fieldType);
        String split_flag = (String) params.get("split_flag");//文件切分标志
        // 判断用什么分隔符切分文件
        String separator = (String)params.get("separator");
        String separatorReg = "";
        String key = (String) params.get("separator");
        SqlRow sqlRow = dealValuePortDao.getSeparator(key);
        switch (key) {
            case "01":
                separator="|";
                separatorReg = "\\|";
                break;
            case "02":
                separator=".";
                separatorReg =  "\\." ;
                break;
            case "03":
                separator=",";
                separatorReg =  "\\,";
                break;
            case "04":
            case "05":
                //05  \U001\U001
                char val= (char) Integer.parseInt("1");
                separator=String.valueOf(val)+ val;
                separatorReg =  String.valueOf(val)+ val;
                break;
            case "06":
                //06  0x0f
                char value = 0x0f;
                separator=String.valueOf(value);
                separatorReg =  String.valueOf(value);
                break;
            case "07":
                //07  !^
                separator="!^";
                separatorReg =  "\\!\\^" ;
                break;
        }

        // 判断文件类型并解析入库数据
        int total_num = 0;//处理数据条数
        if (StringUtils.equalsIgnoreCase(Constants.FILE_TYPE_DBF, (String) params.get("file_type"))){
            try {
                total_num = DBFFileUtil.readMore((String) params.get("fileName"), false,new CallbackService(params),(String) params.get("id"),dealValuePortDao,(String) params.get("charset"));
            }catch (Exception e){
                throw new Exception(Tools.getExceptionInfo(e));
            }

        }else if (StringUtils.equalsIgnoreCase(Constants.FILE_TYPE_TXT, (String) params.get("file_type")) || StringUtils.equals(Constants.FILE_TYPE_DAT, (String) params.get("file_type"))){
            if("1".equals(sliceFlag)){
                Map<String, Object> fileParams = new HashMap<>();
                fileParams.put("fileName",(String) params.get("fileName"));
                fileParams.put("separator",separator);
                fileParams.put("separatorReg",separatorReg);
                fileParams.put("port_table",params.get("port_table"));
                fileParams.put("port_code",params.get("port_code"));
                fileParams.put("has_end_separator",String.valueOf(params.get("has_end_separator")));
                fileParams.put("deal_date",params.get("deal_date").toString());
                fileParams.put("addcolumsflag",params.get("addcolumsflag").toString());
                fileParams.put("skip_no_file",params.get("skip_no_file").toString());
                total_num = this.splitFiles(fileParams);
            }else{
                try {
                    total_num = TxtFileUtil.readMore((String) params.get("fileName"),separator, false,separatorReg,size-1,new CallbackService(params),
                            Integer.parseInt(String.valueOf(params.get("skip_rows"))), String.valueOf(params.get("has_end_separator")),(String) params.get("id"),dealValuePortDao,(String) params.get("charset"),(String) params.get(("port_type")),(String) params.get("skip_no_file")); // size-1 是因为表有dealDate字段 需要-1

                }catch(Exception e){
                    throw new Exception(Tools.getExceptionInfo(e));
                }
            }
        }else if(StringUtils.equalsIgnoreCase(Constants.FILE_TYPE_XML, (String) params.get("file_type"))){
            // 新增 xml文件解析的支持 xmlNodeInfo
            total_num = XmlFileUtil.readMore((String) params.get("fileName"),new CallbackService(params),rs,(String) params.get("xml_node_info"),(String) params.get("skip_no_file"),(String) params.get("port_table"));

        }else {
            throw new Exception("不支持的文件类型");
        }
        if (Integer.parseInt(String.valueOf(params.get("port_type"))) == 4) {
            // wind日志处理数据条数需要减去跳过行数与dat.ok文件空白行一条

            if(total_num != -1) this.updateFileLog((String) params.get("sequence"), Constants.FILE_STATE_03, "文件处理成功", total_num - Integer.parseInt(String.valueOf(params.get("skip_rows"))) - 1);
            if(total_num==-1){
                throw new Exception("DBF文件读取失败!");
            }
        } else {
            // 其他不作处理
            if(total_num != -1&&total_num!=-2) this.updateFileLog((String) params.get("sequence"), Constants.FILE_STATE_03, "文件处理成功", total_num);
            if(total_num==-2){
                this.updateFileLog((String) params.get("sequence"), Constants.FILE_STATE_03, "服务器文件不存在,跳过执行", 0);
                return;
            }
        }
        //差异数据变化入口
        SourceDataChgUtil.dealSourceDataChg(params,dealValuePortDao);

    }

    public List<SqlRow> getDealDateList(String startDate, String endDate) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return dealValuePortDao.getDealDateList(params);
    }

    public StringBuffer getInsertSql(String portTable, String portCode) throws Exception {
        List<String> fieldList = new ArrayList<>();
        Map<String,Object> params = new HashMap<>();
        params.put("port_table",portTable);
        params.put("port_code",portCode);
        StringBuffer sb = new StringBuffer();
        StringBuffer vars = new StringBuffer();
        sb.append(" insert into ").append(portTable).append(" ( ");
        List<SqlRow> rs = dealValuePortDao.queryFieldList(params);
        boolean flag = false;
        for (SqlRow sqlRow : rs) {
            // 拼接逗号
            if (flag) {
                sb.append(",");
                vars.append(",");
            } else {
                flag = true;
            }
            // 字段
            sb.append(sqlRow.getString("field_code"));
            // 占位变量
            vars.append(":"+sqlRow.getString("field_code"));
        }
        sb.append(" ) values ( ").append(vars).append(" )");
        log.info(" 入库SQL："+sb);
        return sb;
    }

    public StringBuffer getSelectSql(String portTable,String originTable) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("port_table",portTable);
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        List<SqlRow> rs = dealValuePortDao.queryFieldListNoDealDate(params);
        SqlRow sqlRow = null;
        for(int i=0;i<rs.size();i++){
            sqlRow = rs.get(i);
            if (i==0 || (i==(rs.size()))) {

            } else  {
                sb.append(",");
            }
            sb.append(sqlRow.getString("field_code"));
        }
        sb.append(" from ").append(originTable).append(" ");
        log.info(" 查询SQL："+sb);
        return sb;
    }

    public Integer splitFiles(Map<String, Object> params) throws Exception{
        try {
            long startTime = System.currentTimeMillis();
            Map<String, Object> map = new HashMap<>();
            String tableName = params.get("port_table").toString();
            String portCode = params.get("port_code").toString();
            String skipNoFile = params.get("skip_no_file").toString();
            ClassLoader classLoader = getClass().getClassLoader();
            String str = classLoader.getResource("config/template").getPath();
            File delFile = new File(str+ File.separator+ tableName+"_.json");
            //FileDefaultConfig fileDefaultConfig = new FileDefaultConfig();
            //fileDefaultConfig.setRdfTemplatePath("/home");


            delFile.delete();
            String path = str;
            map.put("portTable",tableName);
            FetcherData fetcherData = new FetcherData(map, T8PortInfoModel.class);
            com.kayak.core.sql.SqlResult<T8PortInfoModel> res = t8portInfoDao.queryPortAndFieldByTbleName(fetcherData);
            T8PortInfoModel t8PortInfoModel = res.getRows().get(0);
            String contentJson = t8PortInfoModel.getTempContent();
            //File templateFile = new File(path+File.separator+ tableName+"_.json");
            //TxtFileUtil.writeJsonFile(contentJson,templateFile.getPath());
            //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/template"+File.separator+ tableName+"_.json");
            //File file = new File(File.separator+"home"+File.separator+"template"+File.separator+ tableName+"_.json");
            String templatePath = SysUtil.getSystemParamsByParaid("70000010014");
            //File tempFile = new File(templatePath+ tableName+"_.json");
            //FileUtils.copyInputStreamToFile(inputStream, tempFile);
            TxtFileUtil.writeJsonFile(contentJson,templatePath+ tableName+"_.json");
            TemplateLoader.CACHE.clear();
            String filePath = params.get("fileName").toString();
            File file = new File(filePath);
            if (!file.exists()) {
                if("1".equals(skipNoFile)){
                    logger.info(" >>>>> 服务器文件不存在,跳过执行: " + filePath);
                    return -2 ;
                }
                throw new Exception("未获取到合法文件:" + filePath);
            }
            //合并后的文件路径 模板路径 存储类型默认nas
            String os = System.getProperty("os.name");
            FileConfig config;
            FileDefaultConfig.RDF_TEMPLATE_PATH="file:"+templatePath;
            config = new FileConfig(filePath,  tableName+"_.json", new StorageConfig("nas"));
            //FileConfig config = new FileConfig(filePath, "config/template"+File.separator+ tableName+"_.json", new StorageConfig("nas"));
            // 创建分解分割器
            FileSplitter splitter = FileFactory.createSplitter(config.getStorageConfig());
            AtomicInteger total=new AtomicInteger();//总数量
            String insertSql = this.getInsertSql(tableName,portCode).toString();
            String spliteFileSize = SysUtil.getSystemParamsByParaid("90000050001");//文件分片大小单位是字节
            List<FileSlice> slices = splitter.getBodySlices(config, Integer.parseInt(spliteFileSize));//单位字节 1M大概1到3千条
            List<List<FileSlice>> list = TxtFileUtil.averageAssign(slices,maxPoolSize);
            if(list.size()>=maxPoolSize){
                List<Future<?>> futureList = new ArrayList<>();
                for(int k=0;k<maxPoolSize;k++){
                    int finalK = k;
                    Runnable runnable = () -> {
                        try{
                            // 分片读取数据
                            for (FileSlice slice : list.get(finalK)) {
                                Collection<ParamMap> listParamsMaps2 = new ArrayList<>();
                                FileConfig sliceConfig = config.clone();
                                sliceConfig.setPartial(slice.getStart(), slice.getLength(), slice.getFileDataType());
                                FileReader reader = FileFactory.createReader(sliceConfig);
                                try {
                                    Map<String, Object> row = null;
                                    while (null != (row = reader.readRow(HashMap.class))) {
                                        total.addAndGet(1);
                                        if("1".equals(params.get("addcolumsflag").toString())){
                                            row.put("IS_EFFECTIVE", "2");
                                        }
                                        row.put("DEAL_DATE", params.get("deal_date").toString());
                                        ParamMap paramMap = new ParamMap();
                                        paramMap.putAll(row);
                                        listParamsMaps2.add(paramMap);
                                    }
                                    try {
                                        log.info("插入数据开始:");
                                        dbopChange.updateBatchSqlChange(insertSql, listParamsMaps2.toArray(EMPTY_PARAMMAP_ARRAY));
                                        listParamsMaps2.clear();
                                        log.info("插入数据结束:");
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                } catch(Exception e){
                                    log.error(" 子线程分片读取文件插入失败: ", e);
                                    throw new RuntimeException(e);
                                }finally {
                                    reader.close();
                                }
                            }
                        }catch (Exception e){
                            log.error(" 子线程分片读取文件失败: ", e);
                            String err_msg = "";
                            if(e.getMessage().contains("模板定义列数") && e.getMessage().contains("实际列数")){
                                err_msg = "接口字段定义列数与处理文件实际列数不符！";
                            }
                            throw new RuntimeException(err_msg + e);
                        }
                    };
                    Future<?> future = threadPool.submit(runnable);
                    futureList.add(future);
                }
                try {
                    for (Future<?> future : futureList) {
                        future.get();
                    }
                } catch (Exception e) {
                    log.error(" 分片读取文件失败: ", e);
                    throw new RuntimeException(e);
                }
            }else{
                Collection<ParamMap> listParamsMaps2 = new java.util.ArrayList<>();
                FileSlice bodySlice = splitter.getBodySlice(config);
                FileConfig bodyConfig = config.clone();
                bodyConfig.setPartial(0, file.length(),bodySlice.getFileDataType());
                FileReader bodyReader = FileFactory.createReader(bodyConfig);
                try {
                    Map<String, Object> row = null;
                    ParamMap paramMap = new ParamMap().on("port_table", params.get("port_table").toString()).on("deal_date",params.get("deal_date"));
                    List<com.kayak.core.sql.SqlRow> fieldRows= comnDao.findRows("SELECT field_code,field_type FROM base_port_field_manage WHERE PORT_CODE='"+portCode+"' ORDER BY FIELD_SEQ",0,paramMap);
                    //ParamMap paramMap = new ParamMap();
                    Map<String, Object> body = new HashMap<String, Object>();
                    while (null != (row = bodyReader.readRow(HashMap.class))) {
                        total.addAndGet(1);
                        body.clear();
                        ParamMap paramsMap = new ParamMap();
                        for(int j=0;j<fieldRows.size();j++){
                            com.kayak.core.sql.SqlRow fieldRow = fieldRows.get(j);
                            String code=fieldRow.getString("field_code");
                            String fieldType =fieldRow.getString("field_type");

                            if ((fieldType.equalsIgnoreCase("number")||fieldType.equalsIgnoreCase("decimal")||fieldType.equalsIgnoreCase("date")||fieldType.equalsIgnoreCase("double"))
                                    && ( "".equals(row.get(code)) ||  row.get(code) == null)){
                                //ps.setString(i+1,null);
                                body.put(code, null);
                            }else {
                                body.put(code, row.get(code));
                            }
                        }
                        body.put("DEAL_DATE", params.get("deal_date").toString());
                        if("1".equals(params.get("addcolumsflag").toString())){
                            body.put("IS_EFFECTIVE", "2");
                        }
                        paramsMap.putAll(body);
                        listParamsMaps2.add(paramsMap);
                    }
                    dbopChange.updateBatchSqlChange(insertSql, listParamsMaps2.toArray(EMPTY_PARAMMAP_ARRAY));
                    listParamsMaps2.clear();
                } catch(Exception e){
                    log.error(" 单线程分片读取文件失败: ", e);
                    dbopChange.rollback();
                    throw new Exception(Tools.getExceptionInfo(e));
                }finally {
                    bodyReader.close();
                }
            }
            log.info(" ##### 分片读取文件入库耗时: {}", System.currentTimeMillis() - startTime);
            return total.get();
        }catch (Exception e){
            throw new Exception(Tools.getExceptionInfo(e));
        }
    }

    /**
     * 执行接口信息入库后数据的处理语句
     * 暂时不根据同步类型是否历史表增量插入判断是否执行语句,默认增加了语句的贴源层任务都执行其对应语句
     * @param request
     * @throws Exception
     */
    public void handleIncrementData (PubReq request, String workDate) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 贴源数据加工开始 Start -----------");
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("deal_date", workDate);

        //查询需要执行的语句进行排序
        List<SqlRow> list = ExeQuery.queryPortSqlByTaskId(request.getTaskId());

        StringBuffer exeid=new StringBuffer();
        try {
            comnDao.doTrans( () ->{
                for (SqlRow sqlRow:list) {
                    exeid.setLength(0);
                    exeid.append(sqlRow.get("exeid"));
                    log.info("执行语句EXEID[{}]",sqlRow.get("exeid"));
                    comnDao.update(sqlRow.getString("sqlstr"),params);
                }
            });
        }catch (Exception e){
            throw new SQLException("执行SQL["+exeid+"]报错："+e.getMessage(),e);
        }
        log.info("---------- 任务: " + request.getTaskId() +" 贴源数据加工结束 End-----------");

    }


}
