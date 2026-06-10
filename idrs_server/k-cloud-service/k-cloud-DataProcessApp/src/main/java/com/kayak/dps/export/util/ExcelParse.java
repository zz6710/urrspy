package com.kayak.dps.export.util;

import com.kayak.graphql.annotation.GraphQLField;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <li>完成日期：2020/1/14</li>
 * <li>修改记录: 无</li>
 *
 * @author yangzh
 * @version 1.0.0
 */
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
            List<Map<String, Object>> result = new LinkedList<>();
            List<String> headers = new ArrayList<>();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                String cells = dataFormatter.formatCellValue(cell);
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
                                        dataMap.put(headers.get(j), String.valueOf(cell.getNumericCellValue()));
                                    }
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), String.valueOf(cell.getCellFormula()).trim());
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
            List<Map<String, Object>> result = new LinkedList<>();
            List<String> headers = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            Row header = sheet.getRow(headerNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                String cells = dataFormatter.formatCellValue(cell);
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
                                        dataMap.put(headers.get(j), String.valueOf(cell.getNumericCellValue()));
                                    }
                                    flag = false;
                                }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                if (StringUtils.isNotBlank(headers.get(j))) {
                                    dataMap.put(headers.get(j), String.valueOf(cell.getCellFormula()).trim());
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
}
