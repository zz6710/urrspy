package com.kayak.excel.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.kayak.core.system.constants.ExcelDownloadConstants;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import java.time.Duration;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.graphql.model.FetcherData;

@Controller
@RefreshScope
public class ExcelAction extends BaseController {

    @Value("${excel.maxlen:10000}")
    private int maxLen;

    @Value("${excel.tem.path:null}")
    private String temPath;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("excel/download.json")
    public void download(HttpServletResponse response) {
        try {
            Map<String, Object> params = RequestSupport.getParameters();
            if (Strings.isNotEmpty(Tools.obj2Str(params.get("dataExportName")))){
                String dataExportName = Tools.obj2Str(params.get("dataExportName"));
                response.setHeader("dataExportName", dataExportName);
            }

            String modelClassName = Tools.obj2Str(params.get("modelClassName"));

            Class<?> modelClass = Class.forName(modelClassName);
            String headers = Tools.obj2Str(params.get("headers"));

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
            action = action.substring(0, action.length() - ExcelDownloadConstants.SUFFIX.length());

            Object fetcherBean = SysBeans.getBean(fetcher);

            if (fetcherBean == null) {
                log.error("获取操作对象失败，无fetcher配置对应实例，fetcher值：" + fetcher);
                return;
            }

            Method method = fetcherBean.getClass().getMethod(action, SqlParam.class);

            SqlResult<?> sqlResult = (SqlResult<?>) method.invoke(fetcherBean, new FetcherData(_params, modelClass));

            // 获取数据
            List<?> list = sqlResult.getRows();

            // 解析请求头配置
            String[] headerConfigs = headers.split(",");
            boolean dataExportDict = "true".equals(params.get("dataExportDict"));
            // 头部数据
            List<List<String>> excelHeaders = new ArrayList<List<String>>();
            List<String> headKeys = new ArrayList<>();
            Map<String, Map<String, Object>> colMaps = new HashMap<>();
            for (String headerConfig : headerConfigs) {
                String[] _headerConfigs = headerConfig.split(":");
                List<String> head = new ArrayList<String>();
                Map<String, Object> keyMap = new HashMap<>();
                head.add(_headerConfigs[0]);
                excelHeaders.add(head);
                headKeys.add(_headerConfigs[1]);
                keyMap.put("key", _headerConfigs[1]);
                if (_headerConfigs.length == 3 && Tools.isNotBlank(_headerConfigs[2])) {
                    keyMap.put("type", _headerConfigs[2]);
                }
                if (dataExportDict && _headerConfigs.length == 4 && Tools.isNotBlank(_headerConfigs[3])) {
                    keyMap.put("dict", _headerConfigs[3]);
                }
                colMaps.put(_headerConfigs[1], keyMap);
            }
            downFile(list, excelHeaders, headKeys, colMaps, response, params);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    /**
     * 支持分页查询的excel下载
     */
    @RequestMapping("excel/downloadex.json")
    public void downloadEx(HttpServletResponse response) {

        try {
            Map<String, Object> params = RequestSupport.getParameters();
            if (Strings.isNotEmpty(Tools.obj2Str(params.get("dataExportName")))){
                String dataExportName = Tools.obj2Str(params.get("dataExportName"));
                response.setHeader("dataExportName", dataExportName);
            }

            String modelClassName = Tools.obj2Str(params.get("modelClassName"));

            Class<?> modelClass = Class.forName(modelClassName);
            String headers = Tools.obj2Str(params.get("headers"));

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
            action = action.substring(0, action.length() - ExcelDownloadConstants.SUFFIX.length());

            Object fetcherBean = SysBeans.getBean(fetcher);

            if (fetcherBean == null) {
                log.error("获取操作对象失败，无fetcher配置对应实例，fetcher值：" + fetcher);
                return;
            }

            Method method = fetcherBean.getClass().getMethod(action, SqlParam.class);



            // 解析请求头配置
            String[] headerConfigs = headers.split(",");

            // 头部数据
            List<List<String>> excelHeaders = new ArrayList<List<String>>();
            List<String> headKeys = new ArrayList<>();
            Map<String, Map<String, Object>> colMaps = new HashMap<>();
            for (String headerConfig : headerConfigs) {
                String[] _headerConfigs = headerConfig.split(":");
                List<String> head = new ArrayList<String>();
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
            downFileEx(method,modelClass,fetcherBean, excelHeaders, headKeys, colMaps, response, params);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
    private void downFileEx(Method method ,Class<?> modelClass,Object fetcherBean, List<List<String>> excelHeaders, List<String> headKeys,
                            Map<String, Map<String, Object>> colMaps, HttpServletResponse response, Map<String, Object> params) throws Exception {
        String unToDict = params.get("unToDict").toString();
        String dataExportDictval = Tools.obj2Str(params.get("dataExportDictval"));

        ExecutorService executorService=new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1000));
        Instant startTime = Instant.now();
        //1 多线程生成文件
        Map<String, Object> _params = new HashMap<>();
        _params.put("start", 0);
        _params.put("limit",maxLen);
        SqlResult<?> sqlResult = (SqlResult<?>) method.invoke(fetcherBean, new FetcherData(_params, modelClass));
        int total = (int) sqlResult.getResults(); //总记录数
        Map<String,File> fileMap=new HashMap<String,File>();
        Map<String,Integer> countMap=new HashMap<String,Integer>();
        for (int i = 0; i < total /maxLen+1; i++) {
            Integer index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Integer start = maxLen * index;
                    Map<String, Object> _params = new HashMap<>();
                    _params.put("start", start);
                    _params.put("limit",maxLen);
                    try {
                        SqlResult<?> sqlResult = (SqlResult<?>) method.invoke(fetcherBean, new FetcherData(_params, modelClass));
                        // 获取数据
                        List<?> list = sqlResult.getRows();

                        countMap.put(String.valueOf(index),list.size());
                        File temFile = makeExcel(list, excelHeaders, headKeys, colMaps, 0, list.size(), unToDict, dataExportDictval);
                        fileMap.put(String.valueOf(index),temFile);
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
        //下载压缩包
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "filename");

        // 数据长度过长，需要拆分成多个Excel打包导出
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=download.zip");
        response.setHeader("filename", "download.zip");
        File dir=new File(temPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }

        String temZip = temPath + "/" + UUID.randomUUID().toString() + ".zip";

        File temZipFile = new File(temZip);
        temZipFile.createNewFile();

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(temZipFile)); // 创建ZipOutputStream类对象
        for (Map.Entry<String, File> fileEntry : fileMap.entrySet()) {
            Integer key = Integer.parseInt(fileEntry.getKey());
            File file = fileEntry.getValue();
            //压缩包内部文件名修改，需谨慎
            if (Strings.isNotEmpty(response.getHeader("dataExportName"))){
                String dataExportName = response.getHeader("dataExportName");
                // 用前端传来的文件名
                out.putNextEntry(new ZipEntry(dataExportName+"（" + (maxLen * key  +1) + "~" +  (maxLen * key+countMap.get(String.valueOf(key))) + "）.xlsx")); // 创建新的进入点
            }else {
                out.putNextEntry(new ZipEntry("download（" + (maxLen * key +1) + "~" + (maxLen * key+countMap.get(key)) + "）.xlsx")); // 创建新的进入点
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
        OutputStream os = response.getOutputStream();
        os.write(FileUtils.readFileToByteArray(temZipFile));
        os.close();
        temZipFile.delete();
        // 获取当前系统时间点
        Instant endTime1 = Instant.now();
        // 计算时间间隔
        Duration duration1 = Duration.between(startTime, endTime1);
        long seconds1 = duration1.getSeconds();
        log.info("本次压缩用时{}秒",seconds1);
    }

    private void downFile(List<?> datas, List<List<String>> excelHeaders, List<String> headKeys,
                          Map<String, Map<String, Object>> colMaps, HttpServletResponse response, Map<String, Object> params) throws Exception {

        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Expose-Headers", "filename");

        String unToDict = params.get("unToDict").toString();
        String dataExportDictval = Tools.obj2Str(params.get("dataExportDictval"));

        if (datas.size() > maxLen) {// 数据长度过长，需要拆分成多个Excel打包导出
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=download.zip");
            response.setHeader("filename", "download.zip");

            File dir=new File(temPath);
            if (!dir.exists()) {// 判断目录是否存在
                dir.mkdirs();
            }

            String temZip = temPath + "/" + UUID.randomUUID().toString() + ".zip";

            File temZipFile = new File(temZip);
            temZipFile.createNewFile();

            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(temZipFile)); // 创建ZipOutputStream类对象

            int start = 0;
            int end = maxLen;

            while (end <= datas.size()) {
                File temFile = makeExcel(datas, excelHeaders, headKeys, colMaps, start, end, unToDict, dataExportDictval);
                //压缩包内部文件名修改，需谨慎
                if (Strings.isNotEmpty(response.getHeader("dataExportName"))){
                    String dataExportName = response.getHeader("dataExportName");
                    // 用前端传来的文件名
                    out.putNextEntry(new ZipEntry(dataExportName+"（" + start + "~" + end + "）.xlsx")); // 创建新的进入点
                }else {
                    out.putNextEntry(new ZipEntry("download（" + start + "~" + end + "）.xlsx")); // 创建新的进入点
                }
                // 创建FileInputStream对象
                FileInputStream in = new FileInputStream(temFile);
                int b; // 定义int型变量
                while ((b = in.read()) != -1) { // 如果没有到达流的尾部
                    out.write(b); // 将字节写入当前ZIP条目
                }
                in.close(); // 关闭流

                temFile.delete();

                if (end == datas.size()) {
                    break;
                }

                start = end;
                end += maxLen;
                if (end > datas.size()) {
                    end = datas.size();
                }
            }

            out.close();
            OutputStream os = response.getOutputStream();
            os.write(FileUtils.readFileToByteArray(temZipFile));
            temZipFile.delete();

        } else {// 单个Excel文件导出
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=download.xlsx");
            response.setHeader("filename", "download.xlsx");


            OutputStream os = response.getOutputStream();
            File temFile = makeExcel(datas, excelHeaders, headKeys, colMaps, 0, datas.size(), unToDict, dataExportDictval);
            os.write(FileUtils.readFileToByteArray(temFile));
            temFile.delete();
        }
    }

    private File makeExcel(List<?> datas, List<List<String>> excelHeaders, List<String> headKeys,
                           Map<String, Map<String, Object>> colMaps, int start, int end,String unToDict, String dataExportDictval) throws Exception {
        File dir=new File(temPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }
        // 生成临时文件
        String temExcel = temPath + "/" + UUID.randomUUID().toString() + ".xlsx";
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
        List<List<String>> excelDatas = new ArrayList<List<String>>();

        for (int i = start; i < end; i++) {
            Object data = datas.get(i);

            List<String> _datas = new ArrayList<String>();

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
                        value = dictConvert(colMaps, value, headKey, dataExportDictval);
                    }
                }

                _datas.add(value);
            }
            excelDatas.add(_datas);
        }

        excelBuilder.doWrite(excelDatas);

        return temExcelFile;
    }

    private String dictConvert(Map<String, Map<String, Object>> colMaps, String value, String headKey, String dataExportDictval) {
        String dict = (String) colMaps.get(headKey).get("dict");
        String key = value;
        if(value.contains(",")){
            String[] arr = value.split(",");
            String realValue = "";
            for(int i=0;i<arr.length;i++){
                value = CacheUtil.getDictItem(dict, arr[i]);
                if(value !=null && !value.startsWith(arr[i] + " ")){
                    value = arr[i] + " " + value;
                }
                if(value !=null && "true".equals(dataExportDictval)){
                    value = value.replaceAll(Pattern.quote(arr[i] + " "), "");
                }
                realValue = realValue + "," + value;
            }
            return realValue.substring(1);
        }else if(value.contains("/")){
            String[] arr = value.split("/");
            String realValue = "";
            for(int i=0;i<arr.length;i++){
                value = CacheUtil.getDictItem(dict, arr[i]);
                if(value !=null && !value.startsWith(arr[i] + " ")){
                    value = arr[i] + " " + value;
                }
                if(value !=null && "true".equals(dataExportDictval)){
                    value = value.replaceAll(Pattern.quote(arr[i] + " "), "");
                }
                realValue = realValue + "/" + value;
            }
            return realValue.substring(1);
        }else{
            value = CacheUtil.getDictItem(dict, value);
            if(value !=null && !value.startsWith(key + " ")){
                value = key + " " + value;
            }
            if(value !=null && "true".equals(dataExportDictval)){
                value = value.replaceAll(Pattern.quote(key + " "), "");
            }
            return value;
        }
    }

    private String typeConvert(Map<String, Map<String, Object>> colMaps, String value, String headKey) {
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

    private Object getFieldValueByName(String fieldName, Object o) {
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
