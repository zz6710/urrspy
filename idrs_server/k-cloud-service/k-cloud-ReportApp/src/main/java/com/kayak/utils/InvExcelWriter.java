package com.kayak.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.excel.action.ColumnWidthStyleStrategy;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.graphql.model.FetcherData;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RefreshScope
public class InvExcelWriter {

    @Value("${excel.maxlen:10000}")
    private int maxLen;

    @Value("${excel.invmaxlen:10000}")
    private int invMaxLen;

    @Value("${excel.tem.path}")
    private String temPath;

    @Value("${excel.remote.path}")
    private String remotePath;

    private static final int EXECUTOR_TIMEOUT = 100;

    public Map<String, String> downloadEx(Map<String, Object> params) {
        Map<String, String> resMap = new HashMap<>();
        String dataExportName = Tools.obj2Str(params.get("dataExportName"));
        String modelClassName = Tools.obj2Str(params.get("modelClassName"));
        String headers = Tools.obj2Str(params.get("headers"));
        try {
            Class<?> modelClass = Class.forName(modelClassName);
            Map<String, Object> _params;

            String actionParams = Tools.obj2Str(params.get("action_params"));
            if (Tools.strIsEmpty(actionParams)) {
                _params = new HashMap<>();
            } else {
                _params = Tools.json2map(new JSONObject(actionParams));
            }
            // 获取操作对象实例
            GraphQLModel graphQLModel = modelClass.getAnnotation(GraphQLModel.class);
            String fetcher = graphQLModel.fetcher();
            String action = Tools.obj2Str(params.get("action"));
            String actionID = Tools.obj2Str(params.get("action") + "ID");
            Object fetcherBean = SysBeans.getBean(fetcher);
            if (fetcherBean == null) {
                log.error("获取操作对象失败，无fetcher配置对应实例，fetcher值：" + fetcher);
                return resMap;
            }
            Method method = fetcherBean.getClass().getMethod(action, SqlParam.class);
            Method methodID = fetcherBean.getClass().getMethod(actionID, SqlParam.class);
            Map<String, Map<String, Object>> colMaps = new HashMap<>();
            // 头部数据
            List<List<String>> excelHeaders = new ArrayList<>();
            List<String> headKeys = new ArrayList<>();
            // 解析请求头配置
            String[] headerConfigs = headers.split(",");
            for (String headerConfig : headerConfigs) {
                String[] _headerConfigs = headerConfig.split(":");
                List<String> head = new ArrayList<>();
                Map<String, Object> keyMap = new HashMap<>();
                head.add(_headerConfigs[0]);
                excelHeaders.add(head);
                headKeys.add(_headerConfigs[1]);
                keyMap.put("key", _headerConfigs[1]);
                if (_headerConfigs.length == 3 && Tools.isNotBlank(_headerConfigs[2])) {
                    keyMap.put("type", _headerConfigs[2]);
                }
                if (_headerConfigs.length == 4 && Tools.isNotBlank(_headerConfigs[3])) {
                    keyMap.put("dict", _headerConfigs[3]);
                }
                colMaps.put(_headerConfigs[1], keyMap);
            }
            String unToDict = params.get("unToDict").toString();
            return downFileEx(method, methodID, modelClass, fetcherBean, excelHeaders, headKeys, colMaps, unToDict, _params, dataExportName);/**获取查询数据*/
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return resMap;
        }
    }

    private Map<String, String> downFileEx(Method method, Method methodID, Class<?> modelClass, Object fetcherBean, List<List<String>> excelHeaders, List<String> headKeys,
                                   Map<String, Map<String, Object>> colMaps, String unToDict, Map<String, Object> _params, String dataExportName) throws Exception {
        Instant startTime = Instant.now();
        // 如果是配置的method.name，则采用配置的参数
        int qunt = maxLen;
        String methodName = SysUtil.getSystemParamsByParaid("90000051708");
        if (StringUtils.isNotEmpty(methodName) && methodName.contains(method.getName())) {
            qunt = invMaxLen;
        }

        ConcurrentHashMap<String,File> fileMap = new ConcurrentHashMap();
        ConcurrentHashMap<String,Integer> countMap = new ConcurrentHashMap();

        // 多线程生成文件，
        _params.put("start", 0);
        _params.put("limit", 1);

        SqlResult<?> sqlResult = (SqlResult<?>) methodID.invoke(fetcherBean, new FetcherData(_params, modelClass));
        List<?> list = sqlResult.getRows();

        int minId = 0;// 最小ID
        int maxId = 0;// 最大ID

        if (CollectionUtil.isNotEmpty(list)) {
            Object data = list.get(0);
            if (data instanceof SqlRow) {// SqlRow对象
                SqlRow sqlRowData = (SqlRow) data;
                minId = Integer.parseInt(sqlRowData.getString("minId"));
                maxId = Integer.parseInt(sqlRowData.getString("maxId"));
            } else {// model对象
                Object minObj = getFieldValueByName("minId", data);
                Object maxObj = getFieldValueByName("maxId", data);
                minId = Integer.parseInt(Tools.obj2Str(minObj));
                maxId = Integer.parseInt(Tools.obj2Str(maxObj));
            }

            // 多线程生成文件
            int maxPoolSize = Integer.parseInt(SysUtil.getSystemParamsByParaid("90000051113"));
            ExecutorService executorService = new ThreadPoolExecutor(maxPoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000));
            List<Future<?>> futures = new ArrayList<>();

            int idsz = maxId - minId;
            int size = idsz / maxPoolSize + 1;
            int page = size > qunt ? size : qunt;

            for (int i = 0; i < maxPoolSize; i++) {
                // 先判断最小id是否已经超过最大id
                if (minId > maxId) break;

                int table = i;
                int qunt1 = qunt;

                int m1 = minId;
                minId += page;
                int m2 = minId;

                Future<?> future = executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, Object> _paramsT = new HashMap<>();
                            _paramsT.putAll(_params);
                            _paramsT.put("minId", m1);
                            _paramsT.put("maxId", m2);
                            _paramsT.put("start", 0);
                            _paramsT.put("limit", 1);

                            SqlResult<?> sqlResultT = (SqlResult<?>) method.invoke(fetcherBean, new FetcherData(_paramsT, modelClass));
                            int totalT = (int) sqlResultT.getResults(); // 总记录数

                            for (int j = 0; j < totalT / qunt1 + 1; j++) {
                                Map<String, Object> _paramsP = new HashMap<>();
                                _paramsP.putAll(_params);
                                _paramsP.put("minId", m1);
                                _paramsP.put("maxId", m2);
                                _paramsP.put("start", qunt1 * j);
                                _paramsP.put("limit", qunt1);

                                SqlResult<?> sqlResultP = (SqlResult<?>) method.invoke(fetcherBean, new FetcherData(_paramsP, modelClass));
                                // 获取数据
                                List<?> listP = sqlResultP.getRows();
                                if (listP.size() > 0) {
                                    countMap.put(table + "" + j, listP.size());
                                    File temFile = makeExcel(listP, excelHeaders, headKeys, colMaps, 0, listP.size(), unToDict, temPath, String.valueOf(UUID.randomUUID()));
                                    fileMap.put(table + "" + j, temFile);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            throw new RuntimeException(e);
                        }
                    }
                });
                futures.add(future);
            }

            // 等待所有任务完成
            for (Future<?> future : futures) {
                try {
                    future.get(); // 等待每个任务完成
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            // 关闭线程池
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(EXECUTOR_TIMEOUT, TimeUnit.MINUTES)) {
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(EXECUTOR_TIMEOUT, TimeUnit.MINUTES)) {
                        log.error("线程池未能在指定时间内关闭，部分任务可能未完成");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executorService.shutdownNow();
            }
        }

        // 获取当前系统时间点
        Instant endTime = Instant.now();
        // 计算时间间隔
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        log.info("本次生成excel用时{}秒",seconds);
        //下载压缩包
        File dir=new File(temPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }

        String temZip = temPath + "/" + UUID.randomUUID() + ".zip";

        File temZipFile = new File(temZip);
        temZipFile.createNewFile();

        // 统计总数
        Integer total = 0;
        for (Map.Entry<String, Integer> countEntry : countMap.entrySet()) {
            total += countEntry.getValue();
        }

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(temZipFile)); // 创建ZipOutputStream类对象
        for (Map.Entry<String, File> fileEntry : fileMap.entrySet()) {
            String key = fileEntry.getKey();
            File file = fileEntry.getValue();
            //压缩包内部文件名修改，需谨慎
            String totalName = "00".equals(key) ? "-共" + total : "";
            if (Strings.isNotEmpty(dataExportName)) {
                String reportDate = _params.get("reportDate") == null ? "" : (String) _params.get("reportDate");
                out.putNextEntry(new ZipEntry(dataExportName + reportDate + "（" + countMap.get(key) + "-" + key + totalName + "）.xlsx")); // 创建新的进入点
            } else {
                out.putNextEntry(new ZipEntry("download" + "（" + countMap.get(key) + "-" + key + totalName + "）.xlsx")); // 创建新的进入点
            }
            // 创建FileInputStream对象
            FileInputStream in = new FileInputStream(file);
            // 创建缓冲区
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close(); // 关闭流
            file.delete();
        }
        out.close();
        // 获取当前系统时间点
        Instant endTime1 = Instant.now();
        // 计算时间间隔
        Duration duration1 = Duration.between(startTime, endTime1);
        long seconds1 = duration1.getSeconds();
        log.info("本次压缩用时{}秒", seconds1);

        Map<String, String> result = new HashMap<>();
        result.put("local", temZip);
        result.put("remote", uploadS3Files(temZipFile, temZip));
        return result;
    }

    /**
     * 循环生成excel文件
     * @param params
     * @return
     */
    public Map<String, String> downloadLoopEx(Map<String, Object> params) {
        Map<String, String> resMap = new HashMap<>();
        String dataExportName = Tools.obj2Str(params.get("dataExportName"));
        String modelClassName = Tools.obj2Str(params.get("modelClassName"));
        String headers = Tools.obj2Str(params.get("headers"));
        try {
            Class<?> modelClass = Class.forName(modelClassName);
            Map<String, Object> _params;

            String actionParams = Tools.obj2Str(params.get("action_params"));
            if (Tools.strIsEmpty(actionParams)) {
                _params = new HashMap<>();
            } else {
                _params = Tools.json2map(new JSONObject(actionParams));
            }
            // 获取操作对象实例
            GraphQLModel graphQLModel = modelClass.getAnnotation(GraphQLModel.class);
            String fetcher = graphQLModel.fetcher();
            String action = Tools.obj2Str(params.get("action"));
            Object fetcherBean = SysBeans.getBean(fetcher);
            if (fetcherBean == null) {
                log.error("获取操作对象失败，无fetcher配置对应实例，fetcher值：" + fetcher);
                return resMap;
            }
            Method method = fetcherBean.getClass().getMethod(action, SqlParam.class);
            Map<String, Map<String, Object>> colMaps = new HashMap<>();
            // 头部数据
            List<List<String>> excelHeaders = new ArrayList<>();
            List<String> headKeys = new ArrayList<>();
            // 解析请求头配置
            String[] headerConfigs = headers.split(",");
            for (String headerConfig : headerConfigs) {
                String[] _headerConfigs = headerConfig.split(":");
                List<String> head = new ArrayList<>();
                Map<String, Object> keyMap = new HashMap<>();
                head.add(_headerConfigs[0]);
                excelHeaders.add(head);
                headKeys.add(_headerConfigs[1]);
                keyMap.put("key", _headerConfigs[1]);
                if (_headerConfigs.length == 3 && Tools.isNotBlank(_headerConfigs[2])) {
                    keyMap.put("type", _headerConfigs[2]);
                }
                if (_headerConfigs.length == 4 && Tools.isNotBlank(_headerConfigs[3])) {
                    keyMap.put("dict", _headerConfigs[3]);
                }
                colMaps.put(_headerConfigs[1], keyMap);
            }
            String unToDict = params.get("unToDict").toString();
            _params.put("file_name", params.get("dataExportName"));//压缩前文件名
            //若为多个文件下载后再压缩上传走该方法
            Map<String, String> returnMap = downLoopFile(method, modelClass, fetcherBean, excelHeaders, headKeys, colMaps, unToDict, _params, dataExportName);/**获取查询数据*/
            returnMap.put("mrg_typ", String.valueOf(params.get("mrg_typ")));
            returnMap.put("order_mark", params.get("cstmAccF") + "_" + params.get("cstmAccT"));
            return returnMap;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return resMap;
        }
    }

    /**
     * 生成文件保存到指定目录
     * @param method
     * @param modelClass
     * @param fetcherBean
     * @param excelHeaders
     * @param headKeys
     * @param colMaps
     * @param unToDict
     * @param _params
     * @param dataExportName
     * @return
     * @throws Exception
     */
    private Map<String, String> downLoopFile(Method method, Class<?> modelClass, Object fetcherBean, List<List<String>> excelHeaders, List<String> headKeys,
                                           Map<String, Map<String, Object>> colMaps, String unToDict, Map<String, Object> _params, String dataExportName) throws Exception {
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1000));
        Instant startTime = Instant.now();
        Map<String, String> result = new HashMap<>();
        // 如果是配置的method.name，则采用配置的参数
        int qunt = maxLen;
        String methodName = SysUtil.getSystemParamsByParaid("90000051708");
        if (StringUtils.isNotEmpty(methodName) && methodName.contains(method.getName())) {
            qunt = invMaxLen;
        }
        //1 多线程生成文件，
        _params.put("start", 0);
        _params.put("limit", maxLen);

        SqlResult<?> sqlResult = (SqlResult<?>) method.invoke(fetcherBean, new FetcherData(_params, modelClass));
        int total = (int) sqlResult.getResults(); //总记录数
        //Map<String,Integer> countMap= new HashMap<>();
        for (int i = 0; i < total / qunt+1; i++) {
            Integer index = i;
            int qunt1 = qunt;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Integer start = qunt1 * index;
                    Map<String, Object> _paramsP = new HashMap<>();
                    _paramsP.putAll(_params);
                    _paramsP.put("start", start);
                    _paramsP.put("limit", qunt1);
                    try {
                        SqlResult<?> sqlResult = (SqlResult<?>) method.invoke(fetcherBean, new FetcherData(_paramsP, modelClass));
                        // 获取数据
                        List<?> list = sqlResult.getRows();
                        //countMap.put(String.valueOf(index), list.size());
                        File temFile = makeExcel(list, excelHeaders, headKeys, colMaps, 0, list.size(), unToDict, temPath, String.valueOf(_params.get("file_name")));
                        result.put("loop_path", temFile.getPath());
                        result.put("loop_name", temFile.getName());
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(600000, TimeUnit.SECONDS);
        // 获取当前系统时间点
        Instant endTime = Instant.now();
        // 计算时间间隔
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        log.info("本次生成excel用时{}秒",seconds);

        return result;
    }

    /**
     * 压缩三期文件至指定目录
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, String> doZipUploadS3(List<Map<String, String>> params) throws Exception {
        Map<String, String> result = new HashMap<>();

        Instant startTime = Instant.now();
        //下载压缩包
        File dir=new File(temPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }

        //压缩文件输出流
        String report_name = "投资者报表账户合并" + ("1".equals(params.get(0).get("mrg_typ"))?"后":"前") + "文件_" + params.get(0).get("order_mark");
        String temZip = temPath + "/" + report_name + ".zip"; //命名标识为：合并客户号(从)_合并客户号(到)_操作日期
        File temZipFile = new File(temZip);
        temZipFile.createNewFile();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(temZipFile)); // 创建ZipOutputStream类对象

        for (Map<String, String> fileMap : params) {
            File file = new File(fileMap.get("loop_path"));

            // 创建FileInputStream对象
            FileInputStream in = new FileInputStream(file);
            ZipEntry ze = new ZipEntry(file.getName());
            out.putNextEntry(ze);
            // 创建缓冲区
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close(); // 关闭流
            out.closeEntry();
            file.delete();
        }
        out.close();
        // 获取当前系统时间点
        Instant endTime1 = Instant.now();
        // 计算时间间隔
        Duration duration1 = Duration.between(startTime, endTime1);
        long seconds1 = duration1.getSeconds();
        log.info("本次压缩用时{}秒", seconds1);

        result.put("local", temZip);
        result.put("reportName", report_name);
        result.put("remote", uploadS3Files(temZipFile, temZip));
        return result;
    }

    /**
     * 文件上传S3指定目录
     * @param temZipFile
     * @param temZip
     * @return
     * @throws Exception
     */
    private String uploadS3Files(File temZipFile, String temZip) throws Exception {
        FileTransfer transfer =  FileTransferHelpler.getTransfer();
        String remoteFile = null;
        // 对象存储
        try {
            if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
                remoteFile = remotePath + "/" + temZipFile.getName();
            } else {
                remoteFile = remotePath + temZipFile.getName();
            }
            transfer.uploadFileAndDisconnect(temZip, remoteFile);
        } catch (Exception e) {
            log.error("文件上传失败！", e);
        }

        return remoteFile;
    }

    private static File makeExcel(List<?> datas, List<List<String>> excelHeaders, List<String> headKeys,
                                  Map<String, Map<String, Object>> colMaps, int start, int end, String unToDict, String temPath, String file_name) throws Exception {
        File dir=new File(temPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }
        // 生成临时文件
        String temExcel = temPath + "/" + file_name + ".xlsx";
        File temExcelFile = new File(temExcel);

        if(!temExcelFile.exists()){
            temExcelFile.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(temExcelFile);

        // 设置文本格式
        WriteCellStyle textCellStyle = new WriteCellStyle();
        textCellStyle.setDataFormat((short) 49);
        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(textCellStyle, textCellStyle);

        ExcelWriterSheetBuilder excelBuilder = EasyExcel.write(out)
                .registerWriteHandler(new ColumnWidthStyleStrategy()).registerWriteHandler(styleStrategy).sheet("数据");

        // 添加头部信息
        excelBuilder.head(excelHeaders);

        // 写入数据
        List<List<String>> excelDatas = new ArrayList<>();

        for (int i = start; i < end; i++) {
            Object data = datas.get(i);

            List<String> _datas = new ArrayList<>();

            String value = null;
            for (String headKey : headKeys) {
                if (data instanceof SqlRow) {// SqlRow对象
                    SqlRow sqlRowData = (SqlRow) data;
                    value = sqlRowData.getString(headKey);
                } else {// model对象
                    Object valueObj = getFieldValueByName(headKey, data);
                    value = Tools.obj2Str(valueObj);
                }

                if (Tools.isNotEmpty(value)) {
                    // 普通类型转换
                    if (colMaps.get(headKey).containsKey("type")) {
                        value = typeConvert(colMaps, value, headKey);
                    }
                    // 数字字典转换
                    if (colMaps.get(headKey).containsKey("dict")&&(unToDict.indexOf(headKey)==-1)) {
                        value = dictConvert(colMaps, value, headKey);
                    }
                }

                _datas.add(value);
            }
            excelDatas.add(_datas);
        }
        excelBuilder.doWrite(excelDatas);
        return temExcelFile;
    }

    private static String dictConvert(Map<String, Map<String, Object>> colMaps, String value, String headKey) {
        String dict = (String) colMaps.get(headKey).get("dict");
        if(value.contains(",")) {
            String[] arr = value.split(",");
            StringBuilder realValue = new StringBuilder();
            for (String s : arr) {
                realValue.append(CacheUtil.getDictItem(dict, s));
            }
            return realValue.toString();
        } else {
            value = CacheUtil.getDictItem(dict, value);
            return value;
        }
    }

    private static String typeConvert(Map<String, Map<String, Object>> colMaps, String value, String headKey) {
        String type = (String) colMaps.get(headKey).get("type");
        if (type.equals("date")) {
            if (value.split("").length == 6) {
                value = value.substring(0, 4) + "-" + value.substring(4, 6);
            }
            if (value.split("").length == 8) {
                value = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8);
            }
        }
        if (type.equals("time")) {
            if (value.split("").length == 6) {
                value = value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4, 6);
            }
        }
        return value;
    }

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}
