package com.kayak.dps.ods.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kayak.clear.utils.Tools;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.dao.DealValuePortDao;
import com.kayak.dps.ods.exception.DbfFileReadException;
import com.kayak.dps.ods.exception.DbfFileWriteException;
import com.kayak.dps.ods.exception.TxtFileException;
import com.kayak.dps.pub.ICallback;
import com.kayak.dps.pub.WriteCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TxtFileUtil {

    private static Logger logger = LoggerFactory.getLogger(TxtFileUtil.class);

    // 列分隔符
    private static final byte[] COLUMN_SPLIT = {0x01, 0x01};

    // 列分隔符
    private static final String COLUMN_SPLIT_2 = "|";

    // 列分隔符
    private static final String ZT_COLUMN_SPLIT = "!^";

    // 文件行数据末端带有分隔符
    private static final String HAS_END_SEPARATOR = "1";

    // 行分隔符
    private static final byte[] LINE_SEPARATOR = {'\r', '\n'};

    // 文件开始标识
    private static final String FILE_FLAG_START = "BTATOGZDAT";

    // 文件结束标识
    private static final String FILE_FLAG_END = "BTATOGZEND";

    // 文件版本号
    private static final String FILE_VERSION = "10";

    /**
     * 生成TXT文件
     *
     * @param values
     * @param filePath
     *            文件路径(含文件名)
     * @param params
     *            其他参数
     */
    public static void write(List<String> headers, List<Object[]> values, String filePath, Map<String,Object> params) {
        if(headers == null){
            throw new DbfFileWriteException("文件头不能为空");
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // 写文件头 文件开始标识、文件版本号、处理日期、数据行数
            fos.write(FILE_FLAG_START.getBytes("GBK"));
            fos.write(LINE_SEPARATOR);

            fos.write(FILE_VERSION.getBytes("GBK"));
            fos.write(LINE_SEPARATOR);

            fos.write(((String)params.get("deal_date")).getBytes("GBK"));
            fos.write(LINE_SEPARATOR);

            fos.write(String.valueOf(values.size()).getBytes("GBK"));
            fos.write(LINE_SEPARATOR);

            // 写字段名
//            for (int col = 0; col < headers.size(); col++) {
//                if (col > 0) {
//                    fos.write(COLUMN_SPLIT);
//                }
//                String value = headers.get(col);
//                fos.write(value.getBytes("GBK"));
//            }
//            fos.write(LINE_SEPARATOR);

            // 写文件体
            for (Object[] row : values) {
                for (int col = 0; col < row.length; col++) {
                    if (col > 0) {
                        fos.write(COLUMN_SPLIT);
                    }
                    Object value = row[col];
                    if(value == null){
                        value = "";
                    }
                    fos.write(value.toString().getBytes("GBK"));
                }
                fos.write(LINE_SEPARATOR);
            }

            // 写文件结束标识
            fos.write(FILE_FLAG_END.getBytes("GBK"));
            fos.write(LINE_SEPARATOR);

            logger.info(" 生成TXT文件完成: 共{}行数据, 生成文件路径: {}", values.size(), filePath);
        }catch (Exception e){
            logger.error("TXT文件生成失败: ", e);
            throw new TxtFileException("TXT文件生成失败: " + filePath);
        }
    }

    public static void writeMore(List<String> headers, WriteCallback<Object[]> writeCallback, String filePath, Map<String,Object> params, Integer count) {
        if(headers == null){
            throw new DbfFileWriteException("文件头不能为空");
        }

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath, false), 128 * 1024)) {//参数false为重写文件
            List<Object[]> list = writeCallback.call(params);
            while (!list.isEmpty()) {
                // 写文件体
                for (Object[] row : list) {
                    for (int col = 0; col < row.length; col++) {
                        if (col > 0) {
                            bos.write(params.get("separator_val").toString().getBytes(StandardCharsets.UTF_8));
                        }
                        Object value = row[col];
                        if(value == null){
                            value = "";
                        }
                        bos.write(value.toString().getBytes(StandardCharsets.UTF_8));
                    }
                    bos.write(LINE_SEPARATOR);
                }
                list = writeCallback.call(params);
            }

            logger.info(" 生成TXT文件完成: 共{}行数据, 生成文件路径: {}", count, filePath);
        }catch (Exception e){
            logger.error("TXT文件生成失败: ", e);
            throw new TxtFileException("TXT文件生成失败: " + filePath);
        }
    }

    /**
     * 读取txt 文件
     * @param filePath          文件路径(含文件名)
     * @param printFileContent 是否打印文件体
     * @return
     */
    public static List<Object[]> read(String filePath, boolean printFileContent) throws IOException {
        File file  = new File(filePath);
        if(!file.exists()){
            throw new DbfFileReadException("文件不存在");
        }
        List<Object[]> testSet = new ArrayList<>();
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);//避免中文乱码
        BufferedReader br = new BufferedReader(isr);
        String str_line="";
        //逐行读取文本
        while ((str_line = br.readLine()) != null) {
            str_line = str_line.trim();
            if (!str_line.isEmpty()&&str_line.contains(COLUMN_SPLIT_2)) {
                logger.info("================文件头 START  ================");
                //TODO 是否存在文件头，文件头读取形式
                logger.info("================文件头 END  ================");
                logger.info("================文件体 START  ================");
                Object[] data = str_line.split("\\|");
                testSet.add(data);
                if (printFileContent) {
                    logger.info("文件体: {}", (Object) data);
                }
                logger.info("================文件体 END  ================");
            }
        }
        logger.info(" 文件解析成功: 共{}行数据, 解析文件路径: {}", testSet.size(), filePath);
        return testSet;
    }

    /**
     * 读取txt 文件
     * @param filePath          文件路径(含文件名)
     * @param printFileContent 是否打印文件体
     * @param skipRows 读取文件跳过行数
     * @param hasEndSeparator 文件行数据末端是否带多余分隔符
     * @return
     */
    public static int readMore(String filePath,String separator, boolean printFileContent,String separatorReg,int size, ICallback handle, int skipRows, String hasEndSeparator, String id, DealValuePortDao dealValuePortDao,String charset,String portType,String skipNoFile) throws Exception {
        Boolean flag = true;
        String [] fileSpl=filePath.split("/");
        String fileShotNameWithExtention=fileSpl[fileSpl.length-1];
        String fileShotNameWithOutExtention=fileShotNameWithExtention.replaceFirst("\\..*","");
        String fileExtention=fileShotNameWithExtention.replace(fileShotNameWithOutExtention,""); //文件扩展名带点号 如“.txt”
        String directory=filePath.replace(fileShotNameWithExtention,"");//目录
        List<Object[]> testSet = new ArrayList<>();
        File fileDir  = new File(directory);
        File [] files=fileDir.listFiles();
        ArrayList<File> legalFiles=new ArrayList<File>();//合法文件
        int total_num = 0;
        String fileName ="";
        for(File file:files) {
            //产品管理系统推送的文件名特殊处理：CPDM-20230804111547249.txt
            if("7".equals(portType)){
                fileName = file.getName().substring(0,file.getName().lastIndexOf("-")+9)+fileExtention;
            }
            if(file.getName().equals(fileShotNameWithExtention)||fileName.equals(fileShotNameWithExtention)){//全文件匹配
                legalFiles.add(file);
            }
        }
        if (legalFiles.size() == 0) {
            if("1".equals(skipNoFile)){
                logger.info(" >>>>> 服务器文件不存在,跳过执行: " + filePath + fileName);
                return -2 ;
            }
            throw new Exception("未获取到合法文件:" + filePath);
        }
        //产品管理系统会出现一天同文件多次推送，时间戳不同，取最新的文件
        if("7".equals(portType)&&legalFiles.size()>1){
            for (int i = 0; i < legalFiles.size()-1; i++) {
                for (int j = 0; j < legalFiles.size()-i-1; j++) {
                    //文件名中的时间戳
                    int startIndex = legalFiles.get(j).getName().lastIndexOf("-");
                    int endIndex = legalFiles.get(j).getName().lastIndexOf(".");
                    long date1 = Long.parseLong((legalFiles.get(j).getName().substring(startIndex+1,endIndex)));
                    long date2 = Long.parseLong((legalFiles.get(j+1).getName().substring(startIndex+1,endIndex)));
                    //比较大小
                    if (date1 < date2) {
                        //排序
                        File thisFile = legalFiles.get(j);
                        legalFiles.set(j,legalFiles.get(j+1));
                        legalFiles.set(j+1,thisFile);
                    }
                }
            }
            //取时间最大的文件
            File legalLastFiles = legalFiles.get(0);
            legalFiles.clear();
            legalFiles.add(legalLastFiles);
        }
        //遍历合法文件
        for(File file:legalFiles) {
            FileInputStream fis=null;
            InputStreamReader isr=null;
            BufferedReader br=null;
            try{
                 fis = new FileInputStream(file);
                //避免中文乱码
                if ("0".equals(charset)) {
                    isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                }else if ("1".equals(charset)) {
                    isr = new InputStreamReader(fis, Charset.forName("GBK"));
                }

                 br = new BufferedReader(isr);
                logger.info("================开始处理：" + file + " ================");
                String str_line = "";
                int rowNum = 1;
                // 逐行读取文本
                while ((str_line = br.readLine()) != null) {
                    //str_line = str_line.trim(); //wind文件数据读取去空格有问题,放在sql替换字段增加去空格
                    rowNum++;
                    if (!str_line.isEmpty() && str_line.contains(separator) && rowNum > (skipRows+1)) {
                        //若行数据末端多带一个分隔符，则去掉最后一个分隔符
                        if(HAS_END_SEPARATOR.equals(hasEndSeparator)) {
                            str_line = str_line.substring(0, str_line.lastIndexOf(separator));
                        }
                        Object[] data = str_line.split(separatorReg, size);
                        testSet.add(data);
                        if (printFileContent) {
                            logger.info("文件体: {}", (Object) data);
                        }
                        if (rowNum % 10000 == 0) {
                            handle.call(testSet);
                            testSet.clear();
                        }
                    }
                }
                // 处理文件
                handle.call(testSet);
                total_num += (rowNum-1);
                logger.info("================完成处理：" + file + " ================");
                testSet.clear();

            }catch (Exception e){
                logger.error(e.getMessage(),e);
                try {
                    flag = false;
                    updateFileLog(id, Constants.FILE_STATE_02, "处理失败:" + e.getMessage(), total_num, dealValuePortDao);
                } catch (Exception e1) {
                    logger.error(" 文件流水更新失败: ", e);
                }
                throw new Exception(Tools.getExceptionInfo(e));
            }finally {
                br.close();
                isr.close();
                fis.close();
            }

        }
        return flag == true ? total_num : -1;
    }

    public static void updateFileLog(String id, String fileState, String message, Integer totalNum, DealValuePortDao dealValuePortDao) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("message", message);
        params.put("fileState", fileState);
        params.put("totalNum", totalNum);
        params.put("deal_user_id", SysUtil.getSysUserParamValue("sys_user_userid"));
        dealValuePortDao.updateFileLog(params);
    }

    public static void writeJsonFile(JSONObject jobj){
        String content = JSON.toJSONString(jobj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        try {
            File file = new File(System.getProperty("user.dir")+ File.separator+ "New.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            // 写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(content);
            write.flush();
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeJsonFile(String content,String path){
        try {
            File file = new File(path);
            file.mkdirs();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            // 写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(content);
            write.flush();
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> List<List<T>> averageAssign(List<T> list,int n){
        List<List<T>> result=new ArrayList<List<T>>();
        int remaider=list.size()%n;  //(先计算出余数)
        int number=list.size()/n;  //然后是商
        int offset=0;//偏移量
        for(int i=0;i<n;i++){
            List<T> value=null;
            if(remaider>0){
                value=list.subList(i*number+offset, (i+1)*number+offset+1);
                remaider--;
                offset++;
            }else{
                value=list.subList(i*number+offset, (i+1)*number+offset);
            }
            result.add(value);
        }
        return result;
    }
}
