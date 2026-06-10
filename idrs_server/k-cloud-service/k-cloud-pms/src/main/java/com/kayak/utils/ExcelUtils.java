package com.kayak.utils;

import com.kayak.core.exception.PromptException;
import com.kayak.pms.excel.model.Excel;
import com.kayak.pms.excel.model.TradeField;
import com.kayak.pms.excel.service.ExcelService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    public static List<Map<String,Object>> parseExcel(MultipartFile f,ExcelService e) throws Exception {
        return e.readExcel(f);
    }

    /**
     * EXL解析主方法
     * @param excel
     * @param tradeFields
     * @param file
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> readExcel(Excel excel, List<TradeField> tradeFields, File file) throws Exception {

        String fileName = file.getName();

        if (isExcel2003(fileName))
            return readHSSFExcel(excel, tradeFields, file);

        if (isExcel2007(fileName))
            return readXSSFExcel(excel, tradeFields, file);

        return new ArrayList<>();
    }

    /**
     * 是否是2003的excel，返回true是2003
     *
     * @param fileName 　文件名/路劲
     * @return boolean true-是 false-否
     */
    public static boolean isExcel2003(String fileName) {
        return fileName.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是2007的excel，返回true是2007
     * @param fileName 　文件名/路劲
     * @return boolean true-是 false-否
     */
    public static boolean isExcel2007(String fileName) {
        return fileName.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 读取Excel文件的内容
     * @param file
     * @return 以List返回excel中内容
     */
    public static List<Map<String,Object>> readXSSFExcel(Excel excel, List<TradeField> tradeFields, File file) throws Exception {
        Integer firstrow = excel.getFirstrow();
        Integer headerrow = excel.getHeaderrow();
        InputStream inputStream = new FileInputStream(file);
        //存放所有表单数据的集合
        List<Map<String,Object>> list = new ArrayList<>();

        //定义工作簿
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            throw new PromptException("解析excel文件流出错:"+e.getMessage());
        }
        XSSFSheet  xssfSheet = xssfWorkbook.getSheetAt(0);

        if (xssfSheet == null) {
            throw new PromptException("解析excel文件流出错");
        }
        //循环取每行的数据
        for (int rowIndex = firstrow; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
            //存放一个表单数据
            HashMap<String,Object> map = new HashMap<String,Object>();
            XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }
            XSSFRow xssFirstRow = xssfSheet.getRow(headerrow);//获取标题行

            // 循环列Cell
            for(int i=0;i<xssFirstRow.getPhysicalNumberOfCells();i++){
                XSSFCell titleRow = xssFirstRow.getCell(i);
                String cellValue = getString(titleRow);//获取标题中文名

                for(TradeField tradeField:tradeFields){
                    if (cellValue.equals(replace(tradeField.getFieldName()))){
                        int row = Integer.parseInt(tradeField.getApprowVal());
                        int col = Integer.parseInt(tradeField.getAppcolVal());
                        if (row == (-1)) row = rowIndex;
                        if (col == (-1)) col = i;
                        XSSFRow approw = xssfSheet.getRow(row);
                        XSSFCell appxh = approw.getCell(col);
                        map.put(tradeField.getFieldLabel(),getString(appxh));

                    }
                }


            }
            list.add(map);
        }
        return list;
    }

    public static List<Map<String,Object>> readHSSFExcel(Excel excel, List<TradeField> tradeFields, File file) throws Exception {
        InputStream in = new FileInputStream(file);
        Integer firstrow = excel.getFirstrow();
        Integer headerrow = excel.getHeaderrow();
        //存放所有表单数据的集合
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        HSSFWorkbook hssfWorkbook;
        try {
            hssfWorkbook = new HSSFWorkbook(in);

        } catch (IOException e) {
            throw new PromptException("解析excel文件流出错:"+e.getMessage());
        }

        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            if(numSheet != 0){
                continue;
            }

            for (int rowNum = firstrow; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                //存放一个表单数据
                HashMap<String,Object> map = new HashMap<String,Object>();
                //存放公式核算需要的map数据
                HashMap<String,Object> paremtmap = new HashMap<String,Object>();
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                HSSFRow hssfFirstRow = hssfSheet.getRow(headerrow);//获取标题行

                // 循环列Cell
                for(int i=0;i<hssfFirstRow.getLastCellNum();i++){
                    HSSFCell firstCell = hssfFirstRow.getCell(i);
                    String cellValue = replace(getValue(firstCell));//获取首行的中文名
                    for(TradeField tradeField:tradeFields){
                        if (cellValue.equals(replace(tradeField.getFieldName()))){
                            int row = Integer.parseInt(tradeField.getApprowVal());
                            int col = Integer.parseInt(tradeField.getAppcolVal());
                            if (row == (-1)) row = rowNum;
                            if (col == (-1)) col = i;
                            HSSFRow approw = hssfSheet.getRow(row);
                            HSSFCell appxh = approw.getCell(col);
                            setCellValue(appxh,map,paremtmap,tradeField,rowNum);
                        }
                    }
                }
                list.add(map);
                if(isValid(map, tradeFields)){//如果校验通过，则将此行数据添加进集合

                }
            }

        }
        return list;
    }



        /**
         * 生成execl模板(xls格式的模板)
         * @param titles execl表头内容
         * @param out 输出流
         * @throws Exception
         */
    public static void export(String[] titles, ServletOutputStream out) {
        try{
            // 第一步，创建一个workbook，对应一个Excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet hssfSheet = workbook.createSheet("sheet1");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = hssfSheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
            HSSFCell hssfCell = null;
            for (int i = 0; i < titles.length; i++) {
                hssfCell = row.createCell(i);//列索引从0开始
                hssfCell.setCellValue(titles[i]);//列名1
                hssfCell.setCellStyle(hssfCellStyle);//列居中显示
            }
            // 第五步，将文件输出到客户端浏览器
                workbook.write(out);
                out.flush();
        }catch(Exception e){
            log.error("导出失败!,错误信息:{}", e.getMessage());

        }finally {
            try {
                if (out != null) {
                    out.close();
                }
            }catch (Exception e) {
               log.error("方法:ExcelUtils.export流关闭异常！");
            }

        }
    }



    /**
     * 把内容写入Excel
     * @param list 传入要写的内容，此处以一个List内容为例，先把要写的内容放到一个list中
     * @param outputStream 把输出流怼到要写入的Excel上，准备往里面写数据
     */
    public static void writeExcel(List<List> list, OutputStream outputStream) {
        //创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

        //创建工作表
        XSSFSheet xssfSheet = xssfWorkbook.createSheet();

        //创建行
        XSSFRow xssfRow;

        //创建列，即单元格Cell
        XSSFCell xssfCell;

        //把List里面的数据写到excel中
        for (int i=0;i<list.size();i++) {
            //从第一行开始写入
            xssfRow = xssfSheet.createRow(i);
            //创建每个单元格Cell，即列的数据
            List sub_list =list.get(i);
            for (int j=0;j<sub_list.size();j++) {
                xssfCell = xssfRow.createCell(j); //创建单元格
                xssfCell.setCellValue((String)sub_list.get(j)); //设置单元格内容
            }
        }

        //用输出流写到excel
        try {
            xssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static List<List> convertMapToList(Map map) {
        List<List> list = new ArrayList<List>();
        List<String> key_list = new LinkedList<String>();
        List<String> value_list = new LinkedList<String>();

        Set<Map.Entry<String,String>> set = map.entrySet();
        for (Map.Entry<String, String> stringStringEntry : set) {
            key_list.add(stringStringEntry.getKey());
        }
        list.add(key_list);

        Collection<String> value = map.values();
        for (String s : value) {
            value_list.add(s);
        }
        list.add(value_list);
        return list;
    }


    private static Map<String, String> getReplaceChar(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("，", ",");
        map.put("。", ".");
        map.put("；", ";");
        map.put("〈", "<");
        map.put("〉", ">");
        map.put("‖", "|");
        map.put("《", "<");
        map.put("》", ">");
        map.put("〔", "[");
        map.put("〕", "]");
        map.put("﹖", "?");
        map.put("？", "?");
        map.put("“", "\"");
        map.put("”", "\"");
        map.put("’", "'");
        map.put("‘", "'");
        map.put("：", ":");
        map.put("、", ",");
        map.put("（", "(");
        map.put("）", ")");
        map.put("【", "[");
        map.put("】", "]");
        map.put("—", "-");
        map.put("～", "~");
        map.put("！", "!");
        map.put("‵", "'");
        map.put("①", "1");
        map.put("②", "2");
        map.put("③", "3");
        map.put("④", "4");
        map.put("⑤", "5");
        map.put("⑥", "6");
        map.put("⑦", "7");
        map.put("⑧", "8");
        map.put("⑨", "9");
        return map;
    }
    public static String replace(String line){
        Map<String, String> map = getReplaceChar();
        int length = line.length();
        for (int i = 0; i < length; i++)
        {
            String charat = line.substring(i, i + 1);
            if (map.get(charat) != null)
            {
                line = line.replace(charat, (String) map.get(charat));
            }
        }
        return line;
    }


    /**
     * 把单元格的内容转为字符串
     * @param xssfCell 单元格
     * @return 字符串
     */
    public static String getString(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            return String.valueOf(xssfCell.getNumericCellValue());
        } else if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else {
            return xssfCell.getStringCellValue();
        }
    }

    private static boolean isValid(Map<String, Object> map, List<TradeField> tradeFields){
        for (String key : map.keySet()) {
            for(TradeField tradeField:tradeFields){
                if (key.equals(tradeField.getFieldName())){
                    if (!"".equals(map.get(key).toString().replace(" ", ""))){
                        return true;
                    }
                }
            }
        }
        return false;
    }




    /**
     * 校验单元格的数据，并将数据放入到map中
     * @param xh
     * @param map
     * @param tradeField
     */
    public static void setCellValue(HSSFCell xh, Map<String, Object> map, Map<String, Object> paremtmap, TradeField tradeField, int rowNum) throws PromptException {
        String errorMessage = "";
        String getValue01 ="";
        getValue01 = getValue(xh);
        log.info("xh------------------:"+xh);
        log.info("getValue01----------:"+getValue01);
//		getValue01 = crp.replace(getValue01);
        String value = getParse01(getValue01);
        log.info("value---------------:"+value);
        //判断是否有默认值
        if("".equals(value) && !"".equals(tradeField.getDefaultValue())){
            value = tradeField.getDefaultValue();
        }

        String[] arr = tradeField.getFieldLabel().split("\\(");
        String label = arr[0];
        if (getValue01 == null) getValue01 = "";
        map.put(tradeField.getFieldLabel(), getValue01);
        paremtmap.put(label, value);
//        //判断字段是否必填
//        if(tradeField.getIsAllowblank() .equals("1")  && "".equals(value)){//不能为空
//            errorMessage = "第"+rowNum+"行的"+tradeField.getFieldLabel()+"值是必填项";
//            throw new PromptException(errorMessage);
//        }else if(tradeField.getIsAllowblank() .equals("0") && "".equals(value)){//能为空，并且值为空，则直接给map赋空值
//            //去掉label中括号部分
//            String[] arr = tradeField.getFieldLabel().split("\\(");
//            String label = arr[0];
//            String s = "";
//            map.put(tradeField.getFieldName(), s);
//            paremtmap.put(label, s);
//            return;
//        }
    }

    private static String getParse01(String value01){

        String resString = "^\\d*-\\d*-\\d*\\s+([\u4e00-\u9fa5]{1,4})\\d*:\\d*$";

        Date dd= new Date();
        DateFormat dd01 = new SimpleDateFormat("yyyyMMdd");
        String xx = dd01.format(dd);
        xx = xx.substring(0,2);

        String returnStr = "";
        if(value01.matches(resString)){//格式"16-1-5 上午12:00"
            if(value01.substring((value01.lastIndexOf(":")-2)).compareTo("12:00") == 0){//时间等于12点
                returnStr = xx + value01.substring(0,value01.lastIndexOf('-')+3).trim();
                try {
                    SimpleDateFormat dFormat01 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat dFormat02 = new SimpleDateFormat("yyyyMMdd");
                    Date date = new Date();
                    date= dFormat01.parse(returnStr);
                    returnStr =	dFormat02.format(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                //如果时间为下午的话就加12拼接成整体(例 下午5:30转换成17:30)
                if(value01.contains("下午")){
                    Integer hh = Integer.parseInt(value01.substring((value01.lastIndexOf("午")+1),(value01.lastIndexOf(":"))));
                    String mm = value01.substring((value01.lastIndexOf(":")));
                    Integer h = hh+12;
                    String xs = h.toString();
                    returnStr = xs + mm;
                }else{
//					returnStr = value01.substring((value01.lastIndexOf(":")-1));
                    returnStr = value01.substring((value01.lastIndexOf("午")+1));
                }
            }
        }else{
            returnStr = value01;
        }

        return returnStr;
    }



    /**
     * 读取单元格的值，返回string字符串
     * @param cell
     * @return
     */
    public static String getValue(HSSFCell cell) {
        DecimalFormat df = new DecimalFormat("################.########");
        if (cell == null)
            return "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
//			if(HSSFDateUtil.isCellDateFormatted(cell)){//日期类型
//				return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
//			}
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                            .getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyyMMdd");
                    }
                    Date date = cell.getDateCellValue();
                    return sdf.format(date);
                }
                return df.format(cell.getNumericCellValue());
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case HSSFCell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case HSSFCell.CELL_TYPE_BLANK:

            case HSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() + "";
            case HSSFCell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue() + "";
            default:
                return "";
        }
    }


}
