package com.kayak.rpt.email.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.ExcelUtils;
import com.kayak.core.util.Tools;
import com.kayak.rpt.email.model.AppProdBondPropertyInfoVo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 根据已有的 数据List，向EXCEL表格中写入内容（包含固定的表头）。
 */
public  class ExcelUtil {


    /**
     * 有数据对象的采用此方法  需要在字段上加 excel注解
     * @param filePath  文件路径  带文件名
     * @param sheetName 工作表sheet名
     * @param dataList   列表数据
     */
    public static void writeExcelByVo(String filePath, String sheetName, List<AppProdBondPropertyInfoVo> dataList){
         //写Excel
         EasyExcel.write().autoCloseStream(true)
                 .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) //自动调整列宽
                 .sheet(sheetName)
                 .doWrite(dataList);
    }


    /**
     * 新建EXCEL文件，向EXCEL中写入内容，
     *
     * @return
     */
    public static String writeExcel(String fileName, String filePath, String headers, List<Map<String, String>> rowList, OutputStream outputStream) {

        //创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

        //创建工作表
        XSSFSheet xssfSheet = xssfWorkbook.createSheet();

        //创建行
        XSSFRow xssfRow;

        //创建列，即单元格Cell
        XSSFCell xssfCell;
        int change = 0;
        if(!StringUtils.isEmpty(headers)){
            String[] headStr = headers.split(EmailDict.SymbolType.T_COMMA);
            //先写第一行头信息内容   需要头信息和业务表查出来的字段顺序一致。否则还需要额外的判断比对操作
            xssfRow = xssfSheet.createRow(1);
            change = 1;
            for (int i = 0; i < headStr.length; i++) {
                xssfCell = xssfRow.createCell(i+1); //创建单元格
                xssfCell.setCellValue(headStr[i]); //设置单元格内容
            }
        }


        //把List里面的数据写到excel中
        for (int i = 0; i < rowList.size(); i++) {
            //从第一行开始写入
            xssfRow = xssfSheet.createRow(i+change);
            //创建每个单元格Cell，即列的数据
            Map<String,String> mapParam = rowList.get(i);
            int j = 0;
            //TODO  需要调整为list 或者 依次按key取值
            for (Map.Entry entry : mapParam.entrySet()) {

                xssfCell = xssfRow.createCell(j+1); //创建单元格
                xssfCell.setCellValue((String) entry.getValue()); //设置单元格内容
                j++;
            }
        }

        //用输出流写到excel
        try {
            xssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }






    public static File makeExcel(List<?> datas, List<List<String>> excelHeaders, List<String> headKeys,
                           Map<String, Map<String, Object>> colMaps, int start, int end,
                           Map<String, Object> params,String temPath) throws Exception {
        File dir = new File(temPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();
        }
        // 生成临时文件
        String temExcel = temPath + "/" + UUID.randomUUID().toString() + ".xlsx";
        File temExcelFile = new File(temExcel);

        if (!temExcelFile.exists()) {
            temExcelFile.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(temExcelFile);

        ExcelWriterSheetBuilder excelBuilder = EasyExcel.write(out)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("数据");

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
                } else if (data instanceof LinkedHashMap) {
                    value = Tools.obj2Str(((LinkedHashMap) data).get(headKey));
                } else {// model对象
                    Object valueObj = getFieldValueByName(headKey, data);
                    value = Tools.obj2Str(valueObj);
                }

                /*if (Tools.isNotEmpty(value)) {
                    // 普通类型转换
                    if (colMaps.get(headKey).containsKey("type")) {
                        value = typeConvert(colMaps, value, headKey);
                    }
                    // 数字字典转换
                    if (colMaps.get(headKey).containsKey("dict") && (unToDict.indexOf(headKey) == -1)) {
                        value = dictConvert(colMaps, value, headKey);
                    }
                }*/

                _datas.add(value);
            }
            excelDatas.add(_datas);
        }
        /**
         * 是否是根据模板生成excel，如果是，用原生poi
         */
//        if (!ObjectUtils.isEmpty(params.get("dataExcelTemplate"))) {
//            customWriteByPoi(out, params, excelDatas);
//            return temExcelFile;
//        }

        // 添加头部信息
        excelBuilder.head(excelHeaders);

        excelBuilder.doWrite(excelDatas);

        return temExcelFile;
    }

    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }


}
