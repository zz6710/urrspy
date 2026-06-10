package com.kayak.dps.valtabimp.dbfUtils;

import com.kayak.core.system.SysBeans;
import com.kayak.dps.valtabimp.repository.DealGzFileDataDao;
import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DbfFileUtils {
    private static DealGzFileDataDao dealGzFileDataDao = SysBeans.getBean("dealGzFileDataDao");

    private static final Map<Class<?>, Field[]> DECLAREDFIELD_CACHE = new HashMap<>();
    private static Logger log = LoggerFactory.getLogger(DbfFileUtils.class);

    /**
     * 处理委外估值表数据入库
     * @param objList
     * @param params
     */
    private static void handleValueData(List<Object[]> objList, Map<String, Object> params){
        //获取字段配置

    }


    /**
     * 读取委外估值表dbf文件
     * @param inputStream
     * @param closeInputStream
     * @return
     * @throws IOException
     */
    public static List<Map<String, Object>> readDbfFileValData(InputStream inputStream, boolean closeInputStream, Map<String, Object> params)
            throws IOException {

        try (DBFReader reader = new DBFReader(inputStream, Charset.forName("GBK"))) {
            List<Map<String, Object>> result = new LinkedList<>();
            List<String> headers = new ArrayList<>();

            //dbf文件列字段信息(文件头)
            int headerNum = reader.getFieldCount();//总列数
            for (int i = 0; i < headerNum; i++) {
                DBFField field = reader.getField(i);
                log.info("field {} : {}", i, field);
                String field_name = field.getName();
                String str = field_name.replaceAll(" ", "").replaceAll("\\(","").replaceAll("\\)","");
                str="".compareTo(str)==0?"空"+i:str;//如果列名为空 则强制性赋予列名
                headers.add(i,str);//去除所有空格括号
            }
            //记录使用过几个标题行
            log.info("-------组装后的数据标题："+headers);

            //读取文件内容
            Object[] row;
            while ((row = reader.nextRecord()) != null) {//dbf文件按行读取数值
                if (row == null) {
                    continue;//空行跳过
                }
                boolean flag = true;
                Map<String, Object> dataMap = new HashMap<>();
                /** 遍历处理每一行数据,组装参数 */
                for (int j = 0; j < headerNum; ++j) {
                    DBFField field = reader.getField(j);
                    Object cell = row[j];

                    if (cell == null) {
                        dataMap.put(headers.get(j), "");
                    } else {
                        switch (field.getType()) {
                            case VARCHAR: case CHARACTER: //字符
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), cell.toString().trim());
                                    flag = false;
                                }
                                break;
                            case NUMERIC: case FLOATING_POINT: //数值
                                if (StringUtils.isNotBlank(cell.toString().trim())) {//若数值型字段为空，则跳过
                                    if(j==1) {
                                        DecimalFormat df=new DecimalFormat("0");//20211019 因为得到的科目代码都科学计数法影响配置不生效
                                        String str=df.format(cell);
                                        dataMap.put(headers.get(j), str);
                                    }else {//默认转换
                                        dataMap.put(headers.get(j), cell);
                                    }
                                    flag = false;
                                }
                                break;
                            case DATE: //日期
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), dateToStrFormat((Date)cell));
                                    flag = false;
                                }
                                break;
                            case UNKNOWN: //其他
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), String.valueOf(cell));
                                    flag = false;
                                }
                                break;
                            default:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), "");
                                }
                        }
                    }
                }
                if (!flag) {
                    result.add(dataMap);
                }

            }
            return result;
        } finally {
            if (inputStream != null && closeInputStream) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    //流关闭异常
                }
            }
        }
    }

    /**
     * MultipartFile转File
     * @param mFile
     * @return
     */
    public static File multipartFileTransferToFile (MultipartFile mFile) {
        File file = null;
        if (!mFile.equals("") && mFile.getSize() > 0) {
            InputStream inputStream = null;
            try {
                inputStream = mFile.getInputStream();
                file = new File(mFile.getOriginalFilename());

                try{
                    OutputStream outputStream = new FileOutputStream(file);
                    int bytesRead = 0;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.close();
                    inputStream.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                inputStream.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return file;
    }

    /**
     * 日期格式转字符串yyyyMMdd格式
     * @param date
     * @return
     */
    public static String dateToStrFormat(Date date) {
        String dateStr = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        dateStr = simpleDateFormat.format(date);
        return dateStr ;
    }


}
