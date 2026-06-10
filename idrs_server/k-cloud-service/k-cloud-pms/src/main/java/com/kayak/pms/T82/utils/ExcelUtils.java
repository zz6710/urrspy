package com.kayak.pms.T82.utils;

import com.kayak.core.util.Tools;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.apache.poi.ss.usermodel.Cell.*;

public class ExcelUtils {
    /**
     * 总行数
     */
    private int totalRows = 0;
    /**
     * 总列数
     */
    private int totalCells = 0;
    /**
     * 错误信息
     */
    private String errorInfo;
    /**
     * 错误信息
     */
    private String errorCode;

    /**
     * 得到总行数
     *
     * @return int
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * 得到总列数
     *
     * @return int
     */
    public int getTotalCells() {
        return totalCells;
    }

    /**
     * 得到错误信息
     *
     * @return
     */
    public String getErrorInfo() {
        return errorInfo;
    }

    /**
     * 得到错误码
     *
     * @return
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 验证excel文件
     *
     * @param filePath 文件完整路径
     * @return boolean true-是 false-否
     */
    public boolean validateExcel(String filePath) {

        //检查文件名是否为空或者是否是Excel格式的文件
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorCode = "0001";
            errorInfo = "文件名不是excel格式";
            return false;
        }
        //检查文件是否存在
        File file = new File(filePath);

        if (file == null || !file.exists()) {
            errorCode = "0002";
            errorInfo = "文件不存在";
            return false;
        }
        return true;

    }

    /**
     * 是否是2003的excel，返回true是2003
     *
     * @param filePath 　文件完整路径
     * @return boolean true-是 false-否
     */
    public boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是2007的excel，返回true是2007
     *
     * @param filePath 　文件完整路径
     * @return boolean true-是 false-否
     */
    public boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 判断对象是不是空的...
     *
     * @param object
     * @return
     */
    public static Boolean isNullObj(Object object) {

        Class clazz = object.getClass(); // 获取类对象
        Field fields[] = clazz.getDeclaredFields(); // 获取所有属性
        boolean flag = true; //返回结果，默认为true
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(object); //属性值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (fieldValue != null) {  //只要有一个属性值不为null 就返回false 表示对象不为null
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 读取单元格的值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String cellValue = null;
        if (null != cell) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                case CELL_TYPE_NUMERIC: // 数字 格式化掉小数点后面
                    DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                    cellValue = decimalFormat.format(cell.getNumericCellValue()) + "";
                    break;

                case CELL_TYPE_STRING: // 字符串
                    cellValue = cell.getStringCellValue();
                    break;

                case CELL_TYPE_BOOLEAN: // Boolean
                    cellValue = cell.getBooleanCellValue() + "";
                    break;

                case CELL_TYPE_FORMULA: // 公式
                    cellValue = cell.getCellFormula() + "";
                    break;

                case CELL_TYPE_BLANK: // 空值
                    cellValue = null;
                    break;

                case CELL_TYPE_ERROR: // 故障
                    cellValue = "非法字符";
                    break;

                default:
                    cellValue = "未知类型";
                    break;
            }
        }
        return cellValue;
    }

    public static String buildFilePathByExtension(String path, String extension) {
        return getFilePath(path) + generateFileNameByExtension(extension);
    }

    private static String getFilePath(String path) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formatDate = format.format(new Date());
        String filePath = "";
        if("/".equals(Character.toString(path.charAt(path.length()-1))) || "\\".equals(Character.toString(path.charAt(path.length()-1)))){
            filePath = path + formatDate + "/";
        }else{
            filePath = path + "/" + formatDate + "/";
        }
        return filePath;
    }

    private static String generateFileNameByExtension(String extension) {
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        String formatDate = format.format(new Date());
        int random = (new Random()).nextInt(10000);
        return Tools.strIsEmpty(extension)?formatDate + random:formatDate + random + "." + extension;
    }
}
