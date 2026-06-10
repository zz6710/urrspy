package com.kayak.dps.outLands.util;

import com.kayak.graphql.annotation.GraphQLField;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class impOutLandsUtil {
    private static final Map<Class<?>, Field[]> DECLAREDFIELD_CACHE = new HashMap<>();
    private static Logger log =LogManager.getLogger(impOutLandsUtil.class);


    /**
     * 读取2003文件
     * @param inputStream
     * @param sheetNumber
     * @param headerNumber
     * @param rowStart
     * @param closeInputStream
     * @return
     * @throws IOException
     */
    public static List<Map<String, Object>> readExcelData2003(InputStream inputStream, int sheetNumber, String headerNumber, int rowStart, boolean closeInputStream,String[] fieldArrs) throws IOException {
        try {
            List<Map<String, Object>> result = new LinkedList<>();
            List<String> headers = new ArrayList<>();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                boolean flag = true;
                Map<String, Object> dataMap = new HashMap<>();
                for (int j = 0; j < row.getLastCellNum(); ++j) {
                    Cell cell = row.getCell(j);
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
                                    	//20211019意味着是科目代码不需要科学计数法
                                    	if(j==0) {
                                    		DecimalFormat df=new DecimalFormat("0");//20211019 因为得到的科目代码都科学计数法影响配置不生效
                                        	String str=df.format(cell.getNumericCellValue());
                                            dataMap.put(headers.get(j), str);
                                    	}else {//默认转换
                                    		dataMap.put(headers.get(j), String.valueOf(cell.getNumericCellValue()));
                                    	}
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

    public static List<Map<String, Object>> readExcelData(InputStream inputStream, int sheetNumber, String headerNumber, int rowStart, boolean closeInputStream,String[] fieldArrs) throws IOException, InvalidFormatException {
        try {
            List<Map<String, Object>> result = new LinkedList<>();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNumber);
//            Row header = sheet.getRow(headerNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
//            for (int i = 0; i < header.getLastCellNum(); i++) {
//                Cell cell = header.getCell(i);
//                String cells = dataFormatter.formatCellValue(cell);
//                headers.add(cells);
//            }

            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                boolean flag = true;
                Map<String, Object> dataMap = new HashMap<>();
               for (int j = 0; j < row.getLastCellNum(); ++j) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        dataMap.put(fieldArrs[j], "");
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                    dataMap.put(fieldArrs[j], cell.getRichStringCellValue().getString().trim());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        dataMap.put(fieldArrs[j], String.valueOf(cell.getDateCellValue()));
                                    } else {
                                        dataMap.put(fieldArrs[j], String.valueOf(cell.getNumericCellValue()));
                                    }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                    dataMap.put(fieldArrs[j], String.valueOf(cell.getCellFormula()).trim());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                    dataMap.put(fieldArrs[j], String.valueOf(cell.getBooleanCellValue()));
                                break;
                            default:
                                    dataMap.put(fieldArrs[j], "");

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


    public static List<Map<String, Object>> readExcelData2003(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, boolean closeInputStream,String[] fieldArrs) throws IOException, InvalidFormatException {
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
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        dataMap.put(fieldArrs[j], "");
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                    dataMap.put(fieldArrs[j], cell.getRichStringCellValue().getString().trim());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        dataMap.put(fieldArrs[j], String.valueOf(cell.getDateCellValue()));
                                    } else {
                                        dataMap.put(fieldArrs[j], String.valueOf(cell.getNumericCellValue()));
                                    }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                    dataMap.put(fieldArrs[j], String.valueOf(cell.getCellFormula()).trim());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                    dataMap.put(fieldArrs[j], String.valueOf(cell.getBooleanCellValue()));
                            default:
                                    dataMap.put(fieldArrs[j], "");
                        }
                    }
                }
                    result.add(dataMap);
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

    public static List<Map<String, Object>> readExcelData(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, boolean closeInputStream,String[] fieldArrs) throws IOException, InvalidFormatException {
        try {
            List<Map<String, Object>> result = new LinkedList<>();
            Workbook workbook = WorkbookFactory.create(inputStream);
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
                Map<String, Object> dataMap = new HashMap<>();
                for (int j = 0; j < row.getLastCellNum(); ++j) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        dataMap.put(fieldArrs[j], "");
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                    dataMap.put(fieldArrs[j], cell.getRichStringCellValue().getString().trim());

                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                                        String s = format.format(date);
                                        dataMap.put(fieldArrs[j], s);
                                    } else {
                                        dataMap.put(fieldArrs[j], String.valueOf(cell.getNumericCellValue()));
                                    }
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                    dataMap.put(fieldArrs[j], String.valueOf(cell.getCellFormula()).trim());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                    dataMap.put(fieldArrs[j], String.valueOf(cell.getBooleanCellValue()));
                                break;
                            default:
                                    dataMap.put(fieldArrs[j], "");
                        }
                    }
                }
                    result.add(dataMap);

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


    public static  List<Map<String, Object>> readExcelData(InputStream inputStream, int sheetNumber, int headerNumber, int rowStart, boolean closeInputStream, String fileType,String[] fieldArrs) throws IOException, InvalidFormatException {
        List<Map<String, Object>> data = null;
        if("XLS".equals(fileType)){
            data = readExcelData2003(inputStream, sheetNumber, headerNumber, rowStart, closeInputStream,fieldArrs);
        }else{
            data = readExcelData(inputStream, sheetNumber, headerNumber, rowStart, closeInputStream,fieldArrs);
        }
        return data;
    }

    private static Field[] getDeclaredFields(Class<?> tClass) {
        Field[] fields = DECLAREDFIELD_CACHE.get(tClass);
        if (fields != null) {
            return fields;
        }
        Field[] declaredFields = tClass.getDeclaredFields();
        synchronized (impOutLandsUtil.class) {
            DECLAREDFIELD_CACHE.put(tClass, declaredFields);
            return declaredFields;
        }
    }
}
