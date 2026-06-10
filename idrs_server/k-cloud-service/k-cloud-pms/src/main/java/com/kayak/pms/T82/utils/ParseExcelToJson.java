package com.kayak.pms.T82.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kayak.pms.T82.model.BizBodyConfig;
import com.kayak.pms.T82.model.ExcelJSON;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kayak.pms.T82.utils.*;

public class ParseExcelToJson {
    /**
     * 功能:传入一个文件路径,识别文件,读取文件流,返回一个POI对象
     *
     * @return Excel 解析对象
     */
    public static Map loadTemplate(Map<String,String> map) {
        String file_path = map.get("interface_file_route");
        String outFilePath = map.get("outFilePath");
        String interface_version = map.get("interface_version");
        Map resultMap = new HashMap();
        com.kayak.pms.T82.utils.ExcelUtils excelUtils = new ExcelUtils();
       // RtnMsgDao rtnMsgDao = new RtnMsgDao();

        if (!excelUtils.validateExcel(file_path)) {
         //   rtnMsgDao.setRtnCode(excelUtils.getErrorCode());
         //   rtnMsgDao.setRtnMsg(excelUtils.getErrorInfo());
            resultMap.put("code",excelUtils.getErrorCode());
            resultMap.put("msg",excelUtils.getErrorInfo());
        } else {
            try {
                resultMap.put("data",readFile(file_path, outFilePath,interface_version));
                resultMap.put("code","0000");
                resultMap.put("msg","文件读取操作完成");
            } catch (Exception e) {
                resultMap.put("data",e);
                resultMap.put("code","9999");
                resultMap.put("msg","文件读取操作完成");
            }
        }
        return resultMap;
    }

    /**
     * 功能:读取文件,按照模板写好的规则指定位置读取内容,拼接成对象
     *
     * @param file_path 文件路径
     */
    public static Map readFile(String file_path, String outFilePath, String interface_version) throws IOException {
        // 总行数
        int totalRows = 0;
        // 总列数
        int totalCells = 0;

        // 打开指定位置的Excel文件
        Workbook wb = new XSSFWorkbook(new FileInputStream(new File(file_path)));
        // 获得模板总sheet页数
        int totalSheets = wb.getNumberOfSheets();

        HashMap result = new HashMap<String,Object>();
        List<String> interface_json_type = new ArrayList<>();      //JSON文件类型
        List<String> interface_json_name = new ArrayList<>();      //JSON文件名称
        List<String> interface_json_route = new ArrayList<>();     //JSON文件路径

        for (int j = 0; j < totalSheets; j++) {
            // 打开Excel中的第j个Sheet
            Sheet sheet = wb.getSheetAt(j);

            // 2.解析得到内容,赋值json对象
            String version = ExcelUtils.getCellValue(sheet.getRow(0).getCell(1));
            System.out.println("版本号:" + version);
            System.out.println("sheet页名称:" + sheet.getSheetName());

            ExcelJSON excelToJso = new ExcelJSON();
            List<BizBodyConfig> bizBodyConfigs = new ArrayList();

            excelToJso.setOrganizationCode(ExcelUtils.getCellValue(sheet.getRow(1).getCell(2)));
            excelToJso.setBusinessCode(ExcelUtils.getCellValue(sheet.getRow(2).getCell(2)));
            excelToJso.setSliceSize(ExcelUtils.getCellValue(sheet.getRow(3).getCell(2)));

            List<String> headList = new ArrayList<>();
            // head字段,第4行开始13行结束
            int i = 13;
            for (int k = 4; k < i; k++) {
                headList.add(ExcelUtils.getCellValue(sheet.getRow(k).getCell(2)));
            }
            String[] head = new String[headList.size()];
            excelToJso.setHead(headList.toArray(head));

            excelToJso.setCreateOkFile(("true").equals(ExcelUtils.getCellValue(sheet.getRow(19).getCell(2))) ? true : false);
            excelToJso.setDatasouce(ExcelUtils.getCellValue(sheet.getRow(13).getCell(2)));

            String[] tail = {sheet.getRow(14).getCell(2).getStringCellValue()};
            excelToJso.setTail(tail);
            excelToJso.setSlicefield(ExcelUtils.getCellValue(sheet.getRow(15).getCell(2)));
            excelToJso.setErrCodeParamName(ExcelUtils.getCellValue(sheet.getRow(16).getCell(2)));
            excelToJso.setErrMsgParamName(ExcelUtils.getCellValue(sheet.getRow(17).getCell(2)));
            excelToJso.setFileSernoParamName(ExcelUtils.getCellValue(sheet.getRow(18).getCell(2)));
            excelToJso.setFileEncoding(ExcelUtils.getCellValue(sheet.getRow(20).getCell(2)));
            excelToJso.setProtocol(ExcelUtils.getCellValue(sheet.getRow(21).getCell(2)));
            excelToJso.setHeadSql(ExcelUtils.getCellValue(sheet.getRow(22).getCell(1)));
            excelToJso.setSliceSql(ExcelUtils.getCellValue(sheet.getRow(23).getCell(1)));
            excelToJso.setBodySql(ExcelUtils.getCellValue(sheet.getRow(24).getCell(1)));
            excelToJso.setValidatorSql(ExcelUtils.getCellValue(sheet.getRow(25).getCell(1)));

            // 得到Excel的行数
            totalRows = sheet.getPhysicalNumberOfRows();
            // 得到Excel的列数
            if (totalRows >= 1 && sheet.getRow(0) != null) {
                totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            }

            // 循环Excel的行 TODO 0~25从第27行开始
            for (int r = 27; r < totalRows; r++) {

                Row row = sheet.getRow(r);

                if (row == null) continue;

                BizBodyConfig bizBodyConfig = new BizBodyConfig();
                String range = null;
                String accuracy = null;
                // 循环Excel的列
                for (int c = 0; c < totalCells; c++) {

                    Cell cell = row.getCell(c);

                    String cellValue = ExcelUtils.getCellValue(cell);

                    // set对应值
                    if (c == 1) { // 字段名[英文]
                        if (null != cellValue) bizBodyConfig.setName(cellValue);
                    }
                    if (c == 2) { // 类型
                        if (null != cellValue) bizBodyConfig.setType(JsonTypeEnums.getKeyToValue(cellValue));
                    }
                    if (c == 3) { // 长度 TODO 需要拼接 精度,括号
                        range = cellValue;
                    }
                    if (c == 4) { // 精度
                        if (null != range && null == cellValue) {
                            accuracy = "0";
                        } else if ((null == range) && (null != cellValue)) {
                            // 有长度,没精度,不读取
                            accuracy = null;
                        } else {
                            accuracy = cellValue;
                        }
                    }
                    // 长度拼接 [长度,精度],循环到精度的时候拼接长度值
                    if (null != range && c == 4) {
                        bizBodyConfig.setRange("[" + range + "," + accuracy + "]");
                    }
                    if (c == 5) { // 描述 需要转移 NY 转true/false
                        if (null != cellValue && cellValue.equals("Y")) {
                            bizBodyConfig.setRequired(true);
                        } else {
                            bizBodyConfig.setRequired(null);
                        }
                    }
                    if (c == 6) { // 描述
                        if (null != cellValue) bizBodyConfig.setDesc(cellValue);
                    }
                }
                if (!ExcelUtils.isNullObj(bizBodyConfig)) bizBodyConfigs.add(bizBodyConfig);
            }
            if (bizBodyConfigs.size() == 0){
                excelToJso.setBizBodyConfig(null);
            }else {
                excelToJso.setBizBodyConfig(bizBodyConfigs);
            }

            String fullPath = toJsonFile(outFilePath, wb.getSheetName(j), toJson(excelToJso),interface_version);
            interface_json_name.add(wb.getSheetName(j) + "_" + interface_version + ".json");
            interface_json_type.add(wb.getSheetName(j).substring(0, wb.getSheetName(j).indexOf("_" + excelToJso.getOrganizationCode())));
            interface_json_route.add(fullPath);

        }

        result.put("interface_json_type",interface_json_type.toArray(new String[interface_json_type.size()]));
        result.put("interface_json_name",interface_json_name.toArray(new String[interface_json_name.size()]));
        result.put("interface_json_route",interface_json_route.toArray(new String[interface_json_route.size()]));
        // 关文件
        wb.close();
        return result;

    }

    /**
     * 传入一个字符串,返回json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        // 4.将集合数据转换为json字符串(当然map集合亦可以)：格式化输出
        String pretty = JSON.toJSONString(object,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.SortField
        );

        System.out.println(pretty);
        return pretty;
    }

    /**
     * 指定路径生成 json 文件
     *
     * @param outFilePath 输出路径
     * @param fileName    文件名称
     * @param pretty      文件内容
     */
    public static String toJsonFile(String outFilePath, String fileName, String pretty, String interface_version) throws IOException {

        // 拼接文件完整路径
        String fullPath;
        String returnPath;
        if("/".equals(Character.toString(outFilePath.charAt(outFilePath.length()-1))) || "\\".equals(Character.toString(outFilePath.charAt(outFilePath.length()-1)))){
            fullPath = outFilePath + interface_version + "/" + fileName + "_" +interface_version +".json";
            returnPath = outFilePath + interface_version + "/";
        }else{
            fullPath = outFilePath + "/" + interface_version + "/" + fileName + "_" +interface_version +".json";
            returnPath = outFilePath  + "/" + interface_version + "/" ;
        }


        // 保证创建一个新文件
        File out_json_file = new File(fullPath);
        if (!out_json_file.getParentFile().exists()) {
            // 如果父目录不存在，创建父目录
            out_json_file.getParentFile().mkdirs();
        }
        if (out_json_file.exists()) {
            // 如果已存在,删除旧文件
            out_json_file.delete();
        }
        Writer write = null;
        out_json_file.createNewFile();

        // 将格式化后的字符串写入文件
        write = new OutputStreamWriter(new FileOutputStream(out_json_file), "UTF-8");
        write.write(pretty);

        // 关流
        write.flush();
        write.close();
        return returnPath;
    }

}
