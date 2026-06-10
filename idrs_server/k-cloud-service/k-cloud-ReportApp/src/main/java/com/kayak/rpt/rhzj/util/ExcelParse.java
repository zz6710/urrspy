package com.kayak.rpt.rhzj.util;

import com.kayak.graphql.annotation.GraphQLField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * <li>完成日期：2020/1/14</li>
 * <li>修改记录: 无</li>
 *
 * @author yangzh
 * @version 1.0.0
 */
@Slf4j
public class ExcelParse {
    private static final Map<Class<?>, Field[]> DECLAREDFIELD_CACHE = new HashMap<>();

    /**
     * 读取2003文件
     * @param inputStream
     * @param sheetNumber
     * @param row
     * @param columns
     * @param closeInputStream
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static String readExcelCell2003(InputStream inputStream, int sheetNumber, int row, int columns, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
        	HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            Sheet sheetAt = hssfWorkbook.getSheetAt(sheetNumber);
            try {
            	return sheetAt.getRow(row).getCell(columns).getStringCellValue();
			} catch (Exception e) {
				Date date = sheetAt.getRow(row).getCell(columns).getDateCellValue();
				return com.kayak.core.util.DateUtil.dateFormate(date, "yyyyMMdd");
			}
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


    public static String readExcelCell(InputStream inputStream, int sheetNumber, int row, int columns, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheetAt = workbook.getSheetAt(sheetNumber);
            return sheetAt.getRow(row).getCell(columns).getStringCellValue();
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
     * 读取2003文件
     * @param inputStream
     * @param sheetNumber
     * @param headerNumber
     * @param rowStart
     * @param closeInputStream
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static List<Map<String, Object>> readExcelData2003(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            List<Map<String, Object>> result = new LinkedList<>();
            List<String> headers = new ArrayList<>();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                String cells = dataFormatter.formatCellValue(cell).trim();
                headers.add(cells);
            }
            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                boolean flag = true;
                Map<String, Object> dataMap = new HashMap<>();
                for (int j = 0; j < row.getLastCellNum(); ++j) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cell == null) {
                        dataMap.put(headers.get(j), "");
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), cell.getRichStringCellValue().getString().trim());
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        dataMap.put(headers.get(j), String.valueOf(cell.getDateCellValue()));
                                    } else {
                                        BigDecimal bd = new BigDecimal(Double.toString(cell.getNumericCellValue()));
                                        if (bd.stripTrailingZeros().scale() <= 0) {//这是整数
                                            dataMap.put(headers.get(j), numberFormat.format(bd));
                                        } else if (String.valueOf(cell.getNumericCellValue()).contains("E")){
                                            dataMap.put(headers.get(j), BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString());
                                        } else {
                                            dataMap.put(headers.get(j), String.valueOf(cell.getNumericCellValue()));
                                        }
                                    }
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    CellValue cellValue = evaluator.evaluate(cell);
                                   switch (cellValue.getCellType()){
                                       case Cell.CELL_TYPE_STRING:
                                           dataMap.put(headers.get(j), String.valueOf(cellValue.getStringValue()).trim());
                                           break;
                                       case Cell.CELL_TYPE_NUMERIC:
                                           if (DateUtil.isCellDateFormatted(cell)) {
                                               dataMap.put(headers.get(j), String.valueOf(cell.getDateCellValue()));
                                           } else {
                                               BigDecimal bd = new BigDecimal(Double.toString(cell.getNumericCellValue()));
                                               if (bd.stripTrailingZeros().scale() <= 0) {//这是整数
                                                   dataMap.put(headers.get(j), numberFormat.format(bd));
                                               } else if (String.valueOf(cell.getNumericCellValue()).contains("E")){
                                                   dataMap.put(headers.get(j), BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString());
                                               }  else {
                                                   dataMap.put(headers.get(j), String.valueOf(cell.getNumericCellValue()));
                                               }
                                           }
                                           break;
                                       case Cell.CELL_TYPE_BOOLEAN:
                                           dataMap.put(headers.get(j), String.valueOf(cellValue.getBooleanValue()).trim());
                                           break;
                                       default:
                                           dataMap.put(headers.get(j), String.valueOf(cell.getCellFormula()).trim());
                                           break;
                                   }
//                                    dataMap.put(headers.get(j), String.valueOf(cell.getCellFormula()).trim());
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), String.valueOf(cell.getBooleanCellValue()));
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

    public static List<Map<String, Object>> readExcelData(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            List<Map<String, Object>> result = new LinkedList<>();
            List<String> headers = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(inputStream);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                String cells = dataFormatter.formatCellValue(cell).trim();
                headers.add(cells);
            }
            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                boolean flag = true;
                Map<String, Object> dataMap = new HashMap<>();
                for (int j = 0; j < headers.size(); ++j) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cell == null) {
                        dataMap.put(headers.get(j), "");
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), cell.getRichStringCellValue().getString().trim());
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        dataMap.put(headers.get(j), String.valueOf(cell.getDateCellValue()));
                                    } else {
                                        BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                        if (bd.stripTrailingZeros().scale() <= 0) {//这是整数
                                            dataMap.put(headers.get(j), numberFormat.format(bd));
                                        } else if (String.valueOf(cell.getNumericCellValue()).contains("E")){
                                            dataMap.put(headers.get(j), BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString());
                                        }  else {
                                            dataMap.put(headers.get(j), String.valueOf(cell.getNumericCellValue()));
                                        }
                                    }
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    CellValue cellValue = evaluator.evaluate(cell);
                                    switch (cellValue.getCellType()){
                                        case Cell.CELL_TYPE_STRING:
                                            dataMap.put(headers.get(j), String.valueOf(cellValue.getStringValue()).trim());
                                            break;
                                        case Cell.CELL_TYPE_NUMERIC:
                                            if (DateUtil.isCellDateFormatted(cell)) {
                                                dataMap.put(headers.get(j), String.valueOf(cell.getDateCellValue()));
                                            } else {
                                                BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                                if (bd.stripTrailingZeros().scale() <= 0) {//这是整数
                                                    dataMap.put(headers.get(j), numberFormat.format(bd));
                                                } else if (String.valueOf(cell.getNumericCellValue()).contains("E")){
                                                    dataMap.put(headers.get(j), BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString());
                                                }  else {
                                                    dataMap.put(headers.get(j), String.valueOf(cell.getNumericCellValue()));
                                                }
                                            }
                                            break;
                                        case Cell.CELL_TYPE_BOOLEAN:
                                            dataMap.put(headers.get(j), String.valueOf(cellValue.getBooleanValue()).trim());
                                            break;
                                        default:
                                            dataMap.put(headers.get(j), String.valueOf(cell.getCellFormula()).trim());
                                            break;
                                    }
//                                    dataMap.put(headers.get(j), String.valueOf(cell.getCellFormula()).trim());
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), String.valueOf(cell.getBooleanCellValue()));
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



    public static <T> List<T> readExcelData(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, Class<T> tClass, boolean closeInputStream, String fileType) throws IOException, InvalidFormatException {
    	List<Map<String, Object>> data = null;
    	if("XLS".equals(fileType)){
    		data = readExcelData2003(inputStream, sheetNumber, headerNumber, rowStart, closeInputStream);
    	}else{
    		data = readExcelData(inputStream, sheetNumber, headerNumber, rowStart, closeInputStream);
    	}
        List<T> result = new ArrayList<>();
        for (Map<String, Object> datum : data) {
            T instantiate = BeanUtils.instantiate(tClass);
            for (String head : datum.keySet()) {
                Field[] declaredFields = getDeclaredFields(tClass);
                for (Field declaredField : declaredFields) {
                    GraphQLField annotation = declaredField.getAnnotation(GraphQLField.class);
                    if (annotation != null) {
                        String label = annotation.label();
                        if (label.equals(head.replace("*",""))) {
                            Object value = datum.get(head);
                            declaredField.setAccessible(true);
                            ReflectionUtils.setField(declaredField, instantiate, value);
                            break;
                        }
                    } else {
                        if (declaredField.getName().equals(head.replace("*",""))) {
                            Object value = datum.get(head);
                            declaredField.setAccessible(true);
                            ReflectionUtils.setField(declaredField, instantiate, value);
                            break;
                        }
                    }
                }
            }
            result.add(instantiate);
        }
        return result;
    }

    private static Field[] getDeclaredFields(Class<?> tClass) {
        Field[] fields = DECLAREDFIELD_CACHE.get(tClass);
        if (fields != null) {
            return fields;
        }
        Field[] declaredFields = tClass.getDeclaredFields();
        synchronized (ExcelParse.class) {
            DECLAREDFIELD_CACHE.put(tClass, declaredFields);
            return declaredFields;
        }
    }

    public static List<List<String>>  ReadExcelRowData(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, boolean closeInputStream, String fileType) throws IOException, InvalidFormatException {
        List<List<String>> data = null;
        if("XLS".equals(fileType)){
            data = readExcelRowData2003(inputStream, sheetNumber, headerNumber, rowStart, closeInputStream);
        }else{
            data = readExcelRowData(inputStream, sheetNumber,headerNumber, rowStart, closeInputStream);
        }
        return data;
    }

    private static List<List<String>> readExcelRowData(InputStream inputStream, int sheetNumber,int headerNumber, int rowStart, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            List<List<String>> result = new ArrayList<>();
            List<String> headers = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
           for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                String cells = dataFormatter.formatCellValue(cell).trim();
                headers.add(cells);
            }
            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                boolean flag = true;
                List<String> rowDatas = new ArrayList<>();
                for (int j = 0; j < row.getLastCellNum(); ++j) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cell == null) {
                        rowDatas.add("");
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    rowDatas.add(cell.getRichStringCellValue().getString().trim());
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        rowDatas.add(String.valueOf(cell.getDateCellValue()));
                                    } else {
                                        BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                        if (bd.stripTrailingZeros().scale() <= 0) {//这是整数
                                            rowDatas.add(numberFormat.format(bd));
                                        } else if (String.valueOf(cell.getNumericCellValue()).contains("E")){
                                            rowDatas.add(BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString());
                                        } else {
                                            rowDatas.add(String.valueOf(cell.getNumericCellValue()));
                                        }
                                    }
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    rowDatas.add(String.valueOf(cell.getCellFormula()).trim());
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    rowDatas.add(String.valueOf(cell.getBooleanCellValue()));
                                    flag = false;
                                }
                                break;
                            default:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    rowDatas.add("");
                                }

                        }
                    }
                }
                if (!flag) {
                    result.add(rowDatas);
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

    private static List<List<String>> readExcelRowData2003(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
            NumberFormat numberFormat = NumberFormat.getInstance();
            List<List<String>> result = new ArrayList<>();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            List<String> headers = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                boolean flag = true;
                List<String> rowDatas = new ArrayList<>();
                for (int j = 0; j < row.getLastCellNum(); ++j) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cell == null) {
                        rowDatas.add("");
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    rowDatas.add(cell.getRichStringCellValue().getString().trim());
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        rowDatas.add(String.valueOf(cell.getDateCellValue()));
                                    } else {
                                        BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                                        if (bd.stripTrailingZeros().scale() <= 0) {//这是整数
                                            rowDatas.add(numberFormat.format(bd));
                                        } else if (String.valueOf(cell.getNumericCellValue()).contains("E")){
                                            rowDatas.add(BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString());
                                        }  else {
                                            rowDatas.add(String.valueOf(cell.getNumericCellValue()));
                                        }
                                    }
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    // 先尝试获取计算后的值
                                    if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                                    rowDatas.add(String.valueOf(cell.getCellFormula()).trim());
                                    }
                                    flag = false;
                                }

                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    rowDatas.add(String.valueOf(cell.getCellFormula()).trim());
                                    flag = false;
                                }
                                break;
                            default:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    rowDatas.add("");
                                }

                        }
                    }
                }
                if (!flag) {
                    result.add(rowDatas);
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
     * 判断数字是否以科学计数法显示
     */
    private static boolean isScientificNotation(double value) {
        return Math.abs(value) >= 1.0E3 || (Math.abs(value) < 1.0E-3 && value != 0);
    }

    /**
     * 判断是否为大数字（可能被显示为科学计数法）
     */
    private static boolean isLargeNumber(double value) {
        return Math.abs(value) >= 1.0E10;
    }

    /**
     * 判断字符串是否为科学计数法格式
     */
    private static boolean isScientificNotationString(String str) {
        return str.matches("^-?\\d+(\\.\\d+)?[Ee][+-]?\\d+$");
    }

    /**
     * 格式化大数字，避免科学计数法
     */
    private static String formatLargeNumber(double value) {
        try {
            // 使用BigDecimal避免精度丢失
            BigDecimal bd = BigDecimal.valueOf(value);

            // 对于整数
            if (bd.stripTrailingZeros().scale() <= 0) {
                return bd.toBigInteger().toString();
            }

            // 对于小数，使用合适的格式
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(12);
            df.setGroupingUsed(false); // 不使用千位分隔符

            return df.format(bd);

        } catch (NumberFormatException e) {
            // 如果BigDecimal处理失败，使用字符串格式
            return String.format("%.0f", value);
        }
    }

    /**
     * 读取2003文件
     * @param inputStream
     * @param sheetNumber
     * @param headerNumber
     * @param closeInputStream
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static List<String> readExcelData2003Headers(InputStream inputStream, int sheetNumber, int headerNumber, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
            List<String> headers = new ArrayList<>();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                String cells = dataFormatter.formatCellValue(cell).trim();
                headers.add(cells);
            }

            return headers;
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

    public static List<String> readExcelDataHeaders(InputStream inputStream, int sheetNumber, int headerNumber, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
            List<String> headers = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                String cells = dataFormatter.formatCellValue(cell).trim();
                headers.add(cells);
            }
            return headers;
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
     * 获取excel列
     * @param inputStream
     * @param sheetNumber
     * @param headerNumber 传1读取的excel第二行
     * @param
     * @param closeInputStream
     * @param fileType
     * @return
     * @throws Exception
     */
    public static List<String>  readExcelHears(InputStream inputStream, int sheetNumber, int headerNumber,  boolean closeInputStream, String fileType) throws Exception{
        List<String> data = null;
        if("XLS".equals(fileType)){
            data = readExcelData2003Headers(inputStream, sheetNumber, headerNumber, closeInputStream);
        }else{
            data = readExcelDataHeaders(inputStream, sheetNumber, headerNumber, closeInputStream);
        }
       return data;
    }

}
