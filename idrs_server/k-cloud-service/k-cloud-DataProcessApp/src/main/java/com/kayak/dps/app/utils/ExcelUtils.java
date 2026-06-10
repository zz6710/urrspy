package com.kayak.dps.app.utils;

import com.kayak.core.exception.PromptException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);


    /**
     * 读取Excel文件的内容
     *
     * @param file
     * @return 以List返回excel中内容
     */
    public static List<Object[]> readXSSFExcel(File file, String sheetName) throws Exception {

        FileInputStream fileInputStream = new FileInputStream(file);

        //存放所有表单数据的集合
        List<Object[]> data = new ArrayList<>();

        //定义工作簿
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            throw new PromptException("解析excel文件流出错:" + e.getMessage());
        }

        // 获取工作表
        Sheet sheet;
        if(StringUtils.isNotEmpty(sheetName)){
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new PromptException("解析excel文件流出错，工作表：【" + sheetName + "】不存在！");
            }
        }else{
            // 默认获取第一个工作表
            sheet = workbook.getSheetAt(0);
        }

        if (sheet == null) {
            throw new PromptException("解析excel文件流出错，工作表内容读取失败！");
        }
        //循环取每行的数据
        for (Row row : sheet) {
            //根据行数据长度申明数组
            if (isRowEmpty(row) || row.getLastCellNum() < 0) {
                data.add(new Object[0]);// 加入空行，防止错位
                continue;
            }
            Object[] rowData = new Object[row.getLastCellNum()];
            //循环取每列的数据
            for (Cell cell : row) {
                String s;
                // 以下是判断数据的类型
                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    if (cell.getCellStyle().getDataFormatString().contains("%")) {
                        s = cell.getNumericCellValue() * 100 + "%";
                    } else {
                        cell.setCellType(CellType.STRING);
                        s = cell.getStringCellValue();
                    }
                } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
                    try {
                        cell.setCellType(CellType.STRING);
                        s = cell.getStringCellValue();
                    } catch (Exception ex) {
                        FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
                        CellValue cellValue = formulaEvaluator.evaluate(cell);
                        s = new BigDecimal(cellValue.formatAsString()).toPlainString();
                    }
                } else {
                    cell.setCellType(CellType.STRING);
                    s = cell.getStringCellValue();
                }
                rowData[cell.getColumnIndex()] = s;
            }
            data.add(rowData);
        }
        //关闭文件
        workbook.close();
        return data;
    }

    /**
     * 读取Excel文件的内容
     *
     * @param file
     * @return 以List返回excel中内容
     */
    public static List<Object[]> readHSSFExcel(File file, String sheetName) throws Exception {

        FileInputStream fileInputStream = new FileInputStream(file);

        //存放所有表单数据的集合
        List<Object[]> data = new ArrayList<>();

        //定义工作簿
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            throw new PromptException("解析excel文件流出错:" + e.getMessage());
        }
        // 获取工作表
        Sheet sheet;
        if(StringUtils.isNotEmpty(sheetName)){
            sheet = workbook.getSheet(sheetName);
        }else{
            // 默认获取第一个工作表
            sheet = workbook.getSheetAt(0);
        }

        if (sheet == null) {
            throw new PromptException("解析excel文件流出错，工作表内容读取失败！");
        }
        //循环取每行的数据
        for (Row row : sheet) {
            if (isRowEmpty(row) || row.getLastCellNum() < 0) {
                data.add(new Object[0]);// 加入空行，防止错位
                continue;
            }
            //根据行数据长度申明数组
            Object[] rowData = new Object[row.getLastCellNum()];
            //循环取每列的数据
            for (Cell cell : row) {
                String s;
                Object value;
                // 以下是判断数据的类型
                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    if (cell.getCellStyle().getDataFormatString().contains("%")) {
                        s = cell.getNumericCellValue() * 100 + "%";
                    } else {
                        cell.setCellType(CellType.STRING);
                        s = cell.getStringCellValue();
                    }
                } else if(cell.getCellTypeEnum() == CellType.FORMULA) {
                    try {
                        cell.setCellType(CellType.STRING);
                        s = cell.getStringCellValue();
                    } catch (Exception ex) {
                        FormulaEvaluator formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
                        CellValue cellValue = formulaEvaluator.evaluate(cell);
                        s = new BigDecimal(cellValue.formatAsString()).toPlainString();
                    }
                } else {
                    cell.setCellType(CellType.STRING);
                    s = cell.getStringCellValue();
                }
                rowData[cell.getColumnIndex()] = s;
            }
            data.add(rowData);
        }
        //关闭文件
        workbook.close();
        return data;
    }


    /**
     * 读取Excel文件的内容
     *
     * @param reportList
     * @return 以List返回excel中内容
     */
    public static XSSFWorkbook  createXSSFExcel(List<Object[]>  reportList) throws Exception {


        // 创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作表对象
        Sheet sheet = workbook.createSheet("Sheet1");

        for (int i = 0; i < reportList.size() ; i++) {
            // 创建第i行对象
            Row header = sheet.createRow(i);
            Object[] str = reportList.get(i);
            for (int j = 0; j < str.length; j++) {
                Cell cell = header.createCell(j);
                String tempStr =  " ";
                if(str[j] != null){
                    tempStr = str[j].toString();
                }
                cell.setCellValue(tempStr);
            }
        }
        return workbook;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

}
