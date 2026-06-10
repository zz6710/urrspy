package com.kayak.dps.valtabimp.excel;

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

public class ExcelParse {
    private static final Map<Class<?>, Field[]> DECLAREDFIELD_CACHE = new HashMap<>();
    private static Logger log =LogManager.getLogger(ExcelParse.class);
    private static final String[] especialFields = { "人行二级分类", "人行与G06分类", "中债二级分类", "交易流通场所",
            "具体类别", "主体评级", "债项评级", "发行机构类型（按规模划分）", "发行机构类型（按技术领域划分）","发行机构类型（按经济类型划分）", "发行机构所属行业（二级分类）","登记托管机构","登记备案机构" };


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
    public static List<Map<String, Object>> readExcelData2003(InputStream inputStream, int sheetNumber, String headerNumber, int rowStart, boolean closeInputStream) throws IOException {
        try {
            List<Map<String, Object>> result = new LinkedList<>();
            List<String> headers = new ArrayList<>();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            int rowEnd = sheet.getLastRowNum();
            DataFormatter dataFormatter = new DataFormatter();
//            Row header = sheet.getRow(headerNumber);
//            for (int i = 0; i < header.getLastCellNum(); i++) {
//                Cell cell = header.getCell(i);
//                String cells = dataFormatter.formatCellValue(cell);
//                headers.add(cells.replaceAll(" ", ""));//去除所有空格
//            }
            //20210820为了应对标题行相同，使用两行作为标题拼接
            String[] headrs = headerNumber.split(",");
            int idnum=0;
            for(String headern:headrs) {
            	Row header = sheet.getRow(Integer.valueOf(headern));
            	
            	for (int i = 0; i < header.getLastCellNum(); i++) {
                    Cell cell = header.getCell(i);
                    String cells = dataFormatter.formatCellValue(cell);
                    if(idnum==0) {
                    	String str = cells.replaceAll(" ", "").replaceAll("\\(","").replaceAll("\\)","");
                    	str="".compareTo(str)==0?"空"+i:str;//如果列名为空 则强制性赋予列名
                        headers.add(i,str);//去除所有空格括号
                    }else {
                    	if(headers.size()>i) {
                    		String mb=cells.replaceAll(" ", "").replaceAll("\\(","").replaceAll("\\)","");
                    		if(!headers.get(i).equals(mb)){//不相等 别名才会结合相加
                    			 headers.set(i,headers.get(i)+mb);//本身下标为的标题加上新标题行字段
                    		}
                    	}
                    }

                }
            	//记录使用过几个标题行
            	idnum++;
            }
            log.info("-------组装后的数据标题："+headers.toString());
            
            
            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                boolean flag = true;
                Map<String, Object> dataMap = new HashMap<>();
                //20210508 优化修改
                for (int j = 0; j < headers.size(); ++j) {
//                for (int j = 0; j < row.getLastCellNum(); ++j) {
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

    public static List<Map<String, Object>> readExcelData(InputStream inputStream, int sheetNumber, String headerNumber, int rowStart, boolean closeInputStream) throws IOException, InvalidFormatException {
        try {
            List<Map<String, Object>> result = new LinkedList<>();
            List<String> headers = new ArrayList<>();
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
            
            //20210820为了应对标题行相同，使用两行作为标题拼接
            String[] headrs=headerNumber.split(",");
            int idnum=1;
            for(String headern:headrs) {
            	Row header = sheet.getRow(Integer.valueOf(headern));
            	
            	for (int i = 0; i < header.getLastCellNum(); i++) {
                    Cell cell = header.getCell(i);
                    String cells = dataFormatter.formatCellValue(cell);

                    String str=cells.replaceAll(" ", "").replaceAll("\\(","").replaceAll("\\)","");//去除所有空格
                        if( idnum==1){

                            str="".compareTo(str)==0?"K"+i:str;//如果列名为空 则强制性赋予列名
                            headers.add(i,str);
                        }else {
                            String oldheadname=headers.get(i); //取出之前的列名  第二次不可能为空
                            String newname="".compareTo(str)==0?oldheadname:oldheadname.replaceAll(("K"+i),"")+str;//如果列名为空 则强制性赋予列名
                            headers.set(i,newname);//去除所有空格
                        }


                }
            	//记录使用过几个标题行
            	idnum++;

            }
            
            log.info("-------组装后的数据标题："+headers.toString());
            
            for (int i = rowStart; i <= rowEnd; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                boolean flag = true;
                Map<String, Object> dataMap = new HashMap<>();
                //20210508 优化修改
                for (int j = 0; j < headers.size(); ++j) {
//                for (int j = 0; j < row.getLastCellNum(); ++j) {
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
                                        Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                                        String s = format.format(date);
                                        // String s = new SimpleDateFormat("yyyyMMdd").format((Date) cell);
                                        dataMap.put(headers.get(j), s);
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
                            for (String especialField:especialFields) {
                                if(label.equals(especialField)){
                                    value = datum.get(head).toString().split(" ")[0];
                                }
                            }
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
