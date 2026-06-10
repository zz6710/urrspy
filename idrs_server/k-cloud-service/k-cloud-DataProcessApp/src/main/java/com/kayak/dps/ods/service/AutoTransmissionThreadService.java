package com.kayak.dps.ods.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.rdf.file.interfaces.FileFactory;
import com.alipay.rdf.file.interfaces.FileWriter;
import com.alipay.rdf.file.model.FileConfig;
import com.alipay.rdf.file.model.StorageConfig;
import com.kayak.base.dao.ComnDao;
import com.kayak.config.utils.DbopChange;

import com.kayak.dps.check.dao.T8portInfoDao;
import com.kayak.dps.check.model.T8PortInfoModel;
import com.kayak.graphql.model.FetcherData;
import com.kayakwise.kcloud.batch.configure.BatchBaseConfigure;
import com.kayakwise.kcloud.batch.constant.BatchGlobalConstants;
import com.kayakwise.kcloud.batch.model.req.BatchSliceRequest;
import com.kayakwise.kcloud.batch.model.resp.BatchTRespone;
import com.kayakwise.kcloud.batch.service.BaseSliceThread;
import com.kayakwise.kcloud.db.Dbop;
import com.kayakwise.kcloud.db.SqlResult;
import com.kayakwise.kcloud.db.SqlRow;
import com.kayakwise.kcloud.db.util.ParamMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kayak.dps.ods.util.TxtFileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Component
@Scope("prototype")
public class AutoTransmissionThreadService extends BaseSliceThread {

    String workDate = "";
    @Autowired
    public ComnDao comnDao;
    @Autowired
    private DbopChange dbopChange;
    @Autowired
    private BatchBaseConfigure batchBaseConfigure;
    @Autowired
    private T8portInfoDao t8portInfoDao;
    private static final ParamMap[] EMPTY_PARAMMAP_ARRAY = new ParamMap[0];
    @Autowired
    private DealValuePortService dealValuePortService;

    @Override
    protected void doCheckInput(BatchSliceRequest request) throws Exception {

        /*ClassLoader classLoader = getClass().getClassLoader();
        String str = classLoader.getResource("config/template").getPath();
        String path = str;
        Map<String, Object> map = new HashMap<>();
        map.put("pid",request.getTaskId());
        FetcherData fetcherData = new FetcherData(map, T8PortInfoModel.class);
        com.kayak.core.sql.SqlResult<T8PortInfoModel> res = t8portInfoDao.queryPortAndField(fetcherData);
        T8PortInfoModel t8PortInfoModel = res.getRows().get(0);
        String contentJson = t8PortInfoModel.getTempContent();
        String tableName = t8PortInfoModel.getPortTable();
        String sliceExecid = request.getSliceExecid();
        File templateFile = new File(path+ File.separator+ tableName+"_"+sliceExecid+".json");
        TxtFileUtil.writeJsonFile(contentJson,templateFile.getPath());*/
    }


    protected BatchTRespone doProcess4(BatchSliceRequest request) throws Exception {
        log.info(" 执行切片线程",request.toString());
        SqlResult sResult = null;
        File file=new File("/usr/share/test");
        FileConfig config = new FileConfig(new File(file, request.getSliceExecid()+"test.txt").getAbsolutePath(), "config/template/testAaa.json", new StorageConfig("nas"));
        FileFactory factory = new FileFactory();
        config.setSummaryEnable(true);
        Map<String, Object> map = new HashMap<>();
        FileWriter fileWriter = FileFactory.createWriter(config);
        Integer sourceNo = comnDao.findRow("select itemkey from sys_dict_item where dict='source_trans_from' and itemval='"+request.getDatasource()+"'",0,map).getInteger("itemkey");
        map.put("pid",request.getTaskId());
        FetcherData fetcherData = new FetcherData(map, T8PortInfoModel.class);
        com.kayak.core.sql.SqlResult<T8PortInfoModel> res = t8portInfoDao.queryPortAndField(fetcherData);
        T8PortInfoModel t8PortInfoModel = res.getRows().get(0);
        String sliceSelectSql = t8PortInfoModel.getSliceSelectSql();
        try {
            // 头使用数据定义模板的常量
            Map<String, Object> head = new HashMap<String, Object>();
            fileWriter.writeHead(head);
            Map<String, Object> body = new HashMap<String, Object>();
            Date testDate = DateUtil.parse("2017-01-03 12:22:33", "yyyy-MM-dd HH:mm:ss");
            JSONObject json = JSONObject.parseObject(request.getBusiParams());
            Dbop.setDataSourceName("test");
            //查询目标数据库数据
            //sResult = dbop.select("898989", new ParamMap().on("start", request.getSliceStart()).on("end", request.getSliceEnd()));
            //按照模板组装数据
            ParamMap paramMap = new ParamMap().on("start", request.getSliceStart()).on("end", request.getSliceEnd()).on("TRADE_DT",json.getString("TRADE_DT"));
            //sResult = dbop.select(sliceSelectSql, new ParamMap().on("start", request.getSliceStart()).on("end", request.getSliceEnd()).on("TRADE_DT",json.getString("TRADE_DT")),(Integer)null, (Integer)null);
            List<com.kayak.core.sql.SqlRow> sqlrows= comnDao.findRows(sliceSelectSql,sourceNo,paramMap);

            for(int i=0;i<sqlrows.size();i++){
                com.kayak.core.sql.SqlRow row = sqlrows.get(i);
                body.put("id", row.getString("id"));
                body.put("busi_date", row.getString("busi_date"));
                body.put("num", row.getString("num"));
                fileWriter.writeRow(body);
            }
            // 根据汇总信息写入尾部
            fileWriter.writeTail(fileWriter.getSummary().summaryTailToMap());
        } finally {
            fileWriter.close();
        }
        BatchTRespone batchTRespone = new BatchTRespone();
        //batchTRespone.setThreadNo(Integer.parseInt(request.getThreadId()));
        batchTRespone.setRtnDesc(BatchGlobalConstants.RTN_DESC_SUCCESS);
        batchTRespone.setRtnCode(BatchGlobalConstants.RTN_CODE_SUCCESS);
        batchTRespone.setSuccess(true);
        return batchTRespone;
    }


    protected BatchTRespone doProcess2(BatchSliceRequest request) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("pid",request.getTaskId());
        FetcherData fetcherData = new FetcherData(map, T8PortInfoModel.class);
        com.kayak.core.sql.SqlResult<T8PortInfoModel> res = t8portInfoDao.queryPortAndField(fetcherData);
        T8PortInfoModel t8PortInfoModel = res.getRows().get(0);
        String sliceFilePath = t8PortInfoModel.getSliceFilePath();
        String sliceSelectSql = t8PortInfoModel.getSliceSelectSql();
        String tableName = t8PortInfoModel.getPortTable();
        String dataSource = t8PortInfoModel.getSliceDataSource();
        log.info(" 执行切片线程"+request.toString());
        File file=new File(sliceFilePath);
        SqlResult sResult = null;
        FileConfig config = new FileConfig(new File(file, request.getSliceExecid()+"_data.txt").getAbsolutePath(), "config/template"+ File.separator+ tableName+".json", new StorageConfig("nas"));
        config.setSummaryEnable(true);
        FileWriter fileWriter = FileFactory.createWriter(config);
        //Integer sourceNo = comnDao.findRow("select itemkey from sys_dict_item where dict='source_trans_from' and itemval='"+request.getDatasource()+"'",0,map).getInteger("itemkey");
        try {
            // 头使用数据定义模板的常量
            Map<String, Object> head = new HashMap<String, Object>();
            fileWriter.writeHead(head);
            Map<String, Object> body = new HashMap<String, Object>();
            Date testDate = DateUtil.parse("2023-05-04 12:22:33", "yyyy-MM-dd HH:mm:ss");
            JSONObject json = JSONObject.parseObject(request.getBusiParams());
            //查询目标数据库数据
            ParamMap paramMap = new ParamMap().on("start", request.getSliceStart()).on("end", request.getSliceEnd()).on("deal_date",json.getString("deal_date"));
            //List<SqlRow> sqlrows= comnDao.findRows(sliceSelectSql,sourceNo,paramMap);
            List<com.kayak.core.sql.SqlRow> fieldRows= comnDao.findRows("SELECT * FROM base_port_field_manage WHERE PORT_CODE='"+tableName+"' ORDER BY FIELD_SEQ",0,paramMap);
            DbopChange.setDataSourceName(dataSource);
            //查询目标数据库数据
            sResult = dbopChange.select(sliceSelectSql, new ParamMap().on("start", request.getSliceStart()).on("end", request.getSliceEnd()).on("deal_date",json.getString("deal_date")),(Integer)null, (Integer)null);

            //按照模板组装数据
            for(int i=0;i<sResult.getRowSize();i++){
                SqlRow row = sResult.getRow(i);
                for(int j=0;j<fieldRows.size();j++){
                    com.kayak.core.sql.SqlRow fieldRow = fieldRows.get(j);
                    String code=fieldRow.getString("field_code");
                    body.put(code, row.getString(code.toLowerCase()));
                }
                body.put("UPDATE_TIMESTAMP", testDate);
                fileWriter.writeRow(body);
            }
            // 根据汇总信息写入尾部
            fileWriter.writeTail(fileWriter.getSummary().summaryTailToMap());
        } finally {
            fileWriter.close();
        }
        BatchTRespone batchTRespone = new BatchTRespone();
        batchTRespone.setRtnDesc(BatchGlobalConstants.RTN_DESC_SUCCESS);
        batchTRespone.setRtnCode(BatchGlobalConstants.RTN_CODE_SUCCESS);
        batchTRespone.setSuccess(true);
        return batchTRespone;
    }
    @Override
    protected BatchTRespone doProcess(BatchSliceRequest request) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("pid",request.getTaskId());
        FetcherData fetcherData = new FetcherData(map, T8PortInfoModel.class);
        com.kayak.core.sql.SqlResult<T8PortInfoModel> res = t8portInfoDao.queryPortAndField(fetcherData);
        String testDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        JSONObject json = JSONObject.parseObject(request.getBusiParams());
        T8PortInfoModel t8PortInfoModel = res.getRows().get(0);
        String selectSql = t8PortInfoModel.getSelectSql();
        String selectWhere = t8PortInfoModel.getSplitWhere();
        String splitKey = t8PortInfoModel.getSplitKey();
        String taskDate = request.getTaskDate();
        String tableName = t8PortInfoModel.getPortTable();
        String portCode = t8PortInfoModel.getPortCode();
        String sliceTableName = t8PortInfoModel.getSliceTableName();
        String dataSource = t8PortInfoModel.getSliceDataSource();
        StringBuffer newSelectSql = dealValuePortService.getSelectSql(tableName,sliceTableName);
        String sliceSelectSql = newSelectSql+" where 1=1 and "+selectWhere+" and "+splitKey+" >= :start and "+splitKey+" <= :end";

        log.info(" 执行切片线程"+request.toString());
        SqlResult sResult = null;
        try {
            DbopChange.setDataSourceName(dataSource);
            ParamMap paramMap = new ParamMap().on("start", request.getSliceStart()).on("end", request.getSliceEnd()).on("deal_date",json.getString("deal_date"));
            //查询目标数据库数据
            sResult = dbopChange.selectSqlChange(sliceSelectSql, new ParamMap().on("start", request.getSliceStart()).on("end", request.getSliceEnd()).on("deal_date",json.getString("deal_date")),(Integer)null, (Integer)null);
            List<com.kayak.core.sql.SqlRow> fieldRows= comnDao.findRows("SELECT field_code,field_type FROM base_port_field_manage WHERE PORT_CODE='"+tableName+"' ORDER BY FIELD_SEQ",0,paramMap);
            Map<String, Object> body = new HashMap<String, Object>();
            Collection<ParamMap> listParamsMaps2 = new java.util.ArrayList<>();
            //按照模板组装数据
            for(int i=0;i<sResult.getRowSize();i++){
                body.clear();
                SqlRow row = sResult.getRow(i);
                ParamMap paramsMap = new ParamMap();
                for(int j=0;j<fieldRows.size();j++){
                    com.kayak.core.sql.SqlRow fieldRow = fieldRows.get(j);
                    String code=fieldRow.getString("field_code");
                    String fieldType =fieldRow.getString("field_type");

                    if ((fieldType.equalsIgnoreCase("number")||fieldType.equalsIgnoreCase("decimal")||fieldType.equalsIgnoreCase("date")||fieldType.equalsIgnoreCase("double"))
                            && ( "".equals(row.getString(code.toLowerCase())) ||  row.getString(code.toLowerCase()) == null)){
                        //ps.setString(i+1,null);
                        body.put(code, null);
                    }else {
                        body.put(code, row.getString(code.toLowerCase()));
                    }

                }
                body.put("DEAL_DATE", taskDate);
                paramsMap.putAll(body);
                listParamsMaps2.add(paramsMap);
            }
            DbopChange.setDataSourceName("default");
            StringBuffer sql = dealValuePortService.getInsertSql(tableName,portCode);
            dbopChange.updateBatchSqlChange(sql.toString(),(ParamMap[])listParamsMaps2.toArray(EMPTY_PARAMMAP_ARRAY));

        }catch(Exception e){
            dbopChange.rollback();
        }finally {
            BatchTRespone batchTRespone = new BatchTRespone();
            batchTRespone.setRtnDesc(BatchGlobalConstants.RTN_DESC_SUCCESS);
            batchTRespone.setRtnCode(BatchGlobalConstants.RTN_CODE_SUCCESS);
            batchTRespone.setSuccess(true);
            return batchTRespone;
        }

    }
}