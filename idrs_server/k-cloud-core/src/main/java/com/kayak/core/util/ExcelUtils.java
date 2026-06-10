package com.kayak.core.util;

import com.kayak.core.dao.DaoService;
import com.kayak.core.exception.PromptException;
import com.kayak.core.model.Excel;
import com.kayak.core.model.TradeField;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public abstract class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);
    private final DaoService daoService = SysBeans.getBean("daoService");

    //文件
    private MultipartFile file;
    //主配置ID
    private Integer id;
    //主配置名称
    private String modName;
    //数据库默认为公共数据库
    private int sharding;
    //文件名
    private String fileName;
    //exl主配置
    private Excel excel ;
    //exl字段
    private List<TradeField> tradeFields ;
    //文件存储路径
    private String saveFile;
    //解析结果
    private List<Map<String,Object>> l;
    //数据库
    private static int PUB=0;

    public static List<Map<String,Object>> parseExcel(MultipartFile f,ExcelUtils e) throws Exception {
        return e.readExcel(f);
    }
    public List<Map<String,Object>> readExcel(MultipartFile file) throws Exception {
        try {
            init(file);
            //文件验证
            checkFile();
            //初始化数据
            findConfig();
            //转存文件
            fileSaveToLocal();
            //文件解析
            readExcel();
            //存储数据
            saveData();
        }catch (Exception e){

            throw new Exception(e.getMessage());

        }finally {

            if(StringUtils.isNotBlank(saveFile)) {
                saveFileData();
            }

        }

        return l ;
    }

    //保存数据
    public void saveParseData() throws Exception {

        if (StringUtils.isBlank(excel.getTableName())) {
            return;
        }
        try (AutoCloseable ca = daoService.selectDataSource(sharding)) {
            SqlRow sqlRows ;
            SqlRow maxId = null;
            String id = "";
            StringBuilder key = new StringBuilder();
            StringBuilder val = new StringBuilder();
            SqlRow row =daoService.query(SqlRow.class,"select database()", null);
            String database = row.getString("database()");
            List<SqlRow> str = daoService.list(SqlRow.class, "SELECT DISTINCT DATA_TYPE,COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+excel.getTableName()+"' AND TABLE_SCHEMA = '"+database+"'", null);

            for (SqlRow o : str) {
                if (o.getString("COLUMN_NAME").equalsIgnoreCase("id")) {
                    id = o.toString();
                }else{
                    key.append(o.getString("COLUMN_NAME").toUpperCase()).append(",");
                    val.append("$S{").append(o.getString("COLUMN_NAME").toUpperCase()).append("},");
                }
            }

            key = new StringBuilder(key.substring(0, key.length() - 1));
            val = new StringBuilder(val.substring(0, val.length() - 1));

            if (StringUtils.isNotBlank(id)){
                key.append(",ID");
                val.append(",$S{id} ");
            }

            aaa: for (SqlRow sqlRow : str) {
                for (TradeField t : tradeFields) {
                    if (t.getFieldLabel().equalsIgnoreCase(sqlRow.get("COLUMN_NAME").toString())){
                        t.setType(sqlRow.get("DATA_TYPE").toString());
                        continue aaa;
                    }
                }
            }

            String sql = "insert into " + excel.getTableName() + " (" + key + ") values ( " + val + ")";


            for (Map<String,Object> m : l) {
                Set set = m.keySet();
                for (Object o : set) {
                    for (TradeField t : tradeFields) {
                        if ("1".equals(t.getMappingType())&&t.getMappingType()!=null&&t.getFieldLabel().equalsIgnoreCase(o.toString())) {
                            sqlRows = daoService.query(SqlRow.class, "select left(REGEXP_REPLACE(REPLACE(REGEXP_REPLACE(a.result, '[^[:digit:]]', ' '), ' ', ','), ',{2,}', ','),LOCATE(',',REGEXP_REPLACE(REPLACE(REGEXP_REPLACE(a.result, '[^[:digit:]]', ' '), ' ', ','), ',{2,}', ','))) result from (select '" + m.get(o).toString() + "' result from dual ) a", null);
                            m.put(o.toString(),String.valueOf(sqlRows.getString("result")));
                            if (sqlRows.getString("result").endsWith(","))
                                m.put(o.toString(),sqlRows.getString("result").substring(0,sqlRows.getString("result").length()-1));
                        }
                        if ("2".equals(t.getMappingType())&&t.getMappingType()!=null&&t.getFieldLabel().equalsIgnoreCase(o.toString())) {
                            sqlRows = daoService.query(SqlRow.class, "select substr(REGEXP_REPLACE(REPLACE(REGEXP_REPLACE(a.result, '[^[:digit:]]', ' '), ' ', ','), ',{2,}', ','),1,length(REGEXP_REPLACE(REPLACE(REGEXP_REPLACE(a.result, '[^[:digit:]]', ' '), ' ', ','), ',{2,}', ','))) result from (select '" + m.get(o).toString() + "' result from dual ) a", null);
                            m.put(o.toString(),String.valueOf(sqlRows.getString("result")));
                            if (sqlRows.getString("result").endsWith(","))
                                m.put(o.toString(),sqlRows.getString("result").substring(0,sqlRows.getString("result").length()-1));
                        }
                        if ("3".equals(t.getMappingType())&&t.getMappingType()!=null&&t.getFieldLabel().equalsIgnoreCase(o.toString())) {
                            sqlRows = daoService.query(SqlRow.class, "select substr(REGEXP_REPLACE(REPLACE(REGEXP_REPLACE(a.result, '[^a-z0-9]', ' '), ' ', ','), ',{2,}', ','),1,length(REGEXP_REPLACE(REPLACE(REGEXP_REPLACE(a.result, '[^a-z0-9]', ' '), ' ', ','), ',{2,}', ','))) result  from (select '" + m.get(o).toString() + "' result from dual ) a", null);
                            m.put(o.toString(),String.valueOf(sqlRows.getString("result")));
                            if (sqlRows.getString("result").endsWith(","))
                                m.put(o.toString(),sqlRows.getString("result").substring(0,sqlRows.getString("result").length()-1));
                        }
//                        if ("false".equals(m.get(o).toString())){
//                            m.put(o.toString(),"");
//                        }
                        if (t.getFieldLabel().equalsIgnoreCase(o.toString()) && "varchar".equals(t.getType())){
                            m.put(o.toString(),String.valueOf(m.get(o).toString()));
                        }
                        if (t.getFieldLabel().equalsIgnoreCase(o.toString()) && "decimal".equals(t.getType())&& "".equals(m.get(o).toString()) ){
                            m.put(o.toString(),"0");
                        }
                    }
                }
                if (StringUtils.isNotBlank(id)){
                    maxId =daoService.query(SqlRow.class,"select ifnull(max(id+0)+1,1) maxid from "+excel.getTableName().toLowerCase(Locale.ROOT), null);
                    m.put("id",maxId.getString("maxid"));
                }
                daoService.update(sql,m);
                if (maxId!=null)
                daoService.update("INSERT INTO sys_sequence(tablename,maxid) VALUE('"+excel.getTableName().toLowerCase(Locale.ROOT)+"','1') ON DUPLICATE KEY UPDATE maxid= '"+maxId.getString("maxid")+"'");
            }
        }

    }
    //存储文件
    public void saveFileData() {

        Map<String ,Object> map = new HashMap<>();
        map.put("sysExlimpId",id);
        map.put("filePath",saveFile);
        map.put("fileName",fileName);
        map.put("crtDate", DateUtil.getNowDate());
        String str = "insert into sys_import_file (id, sys_exlimp_id, file_name, file_path, crt_date) " +
                "values ($AUTOIDS{id}, $S{sysExlimpId}, $S{fileName}, $S{filePath}, $S{crtDate})";
        try (AutoCloseable ca = daoService.selectDataSource(sharding)) {
            daoService.update(str,map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init(MultipartFile file){
        //file
        this.file = file;
        //导入主配置名称
        this.modName = setModName();

        this.sharding = setSharding();

        this.fileName = file.getOriginalFilename();
    }

    /**
     * 文件验证
     * @throws Exception
     */
    private void checkFile () throws Exception {
        //空文件验证
        if (file == null) {
            throw new Exception("上传文件为空");
        }

        if (modName == null) {
            throw new Exception("未设置Excel导入名称[modName]");
        }

        //文件类型验证
        String fileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            throw new Exception("上传文件不为Excel");
        }
    }

    private void findConfig() throws Exception {
        try (AutoCloseable ca = daoService.selectDataSource(sharding)) {
            excel = daoService.list(Excel.class, "SELECT * FROM SYS_EXLIMP WHERE MOD_NAME = '" + modName + "'", null).get(0);
            id =Integer.parseInt(excel.getId());
            tradeFields = daoService.list(TradeField.class, "SELECT * FROM SYS_EXLIMP_DETAIL WHERE SYS_EXLIMP_ID =  '" + id + "'", null);
        }
    }

    private void fileSaveToLocal () throws Exception {
        String s = "";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            s = "80000080002";
        } else {
            s = "80000080003";
        }
        String path = SysUtil.getSystemParamsByParaid(s);
        path=path.endsWith("/") ? path : path + "/";

        String filePath = path + "import" + File.separator + excel.getModName() + File.separator + DateUtil.getTimestamp14();

        try {

            File localPathFile = new File(filePath);
            //文件夹不存在的话创建文件夹
            if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                localPathFile.mkdirs();
            }

            File localFile = new File(filePath, fileName);
            //转存文件
            file.transferTo(localFile.toPath());
            saveFile = localFile.getPath();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private void readExcel() throws Exception {
        l = ExcelUtils.readExcel(excel, tradeFields, new File(saveFile)) ;
    }




    public static List<Map<String,Object>> readExcel(Excel excel, List<TradeField> tradeFields, File file) throws Exception {

        String fileName = file.getName();

        if (isExcel2003(fileName)) {
            return readHSSFExcel(excel, tradeFields, file);
        }
        if (isExcel2007(fileName)){
            return readXSSFExcel(excel, tradeFields, file);
        }
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
                //获取标题行
                HSSFRow hssfFirstRow = hssfSheet.getRow(headerrow);

                // 循环列Cell
                for(int i=0;i<hssfFirstRow.getLastCellNum();i++){
                    HSSFCell firstCell = hssfFirstRow.getCell(i);
                    //获取首行的中文名
                    String cellValue = replace(getValue(firstCell));
                    for(TradeField tradeField:tradeFields){
                        if (cellValue.equals(replace(tradeField.getFieldName()))){
                            int row = Integer.parseInt(tradeField.getApprowVal());
                            int col = Integer.parseInt(tradeField.getAppcolVal());
                            if (row == (-1)) {row = rowNum;}
                            if (col == (-1)) {col = i;}
                            HSSFRow approw = hssfSheet.getRow(row);
                            HSSFCell appxh = approw.getCell(col);
                            setCellValue(appxh,map,paremtmap,tradeField,rowNum);
                        }
                    }
                }
                list.add(map);
                //如果校验通过，则将此行数据添加进集合
                if(isValid(map, tradeFields)){

                }
            }

        }
        return list;
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
     * 读取单元格的值，返回string字符串
     * @param cell
     * @return
     */
    public static String getValue(HSSFCell cell) {
        DecimalFormat df = new DecimalFormat("################.########");
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                //日期类型
//			if(HSSFDateUtil.isCellDateFormatted(cell)){
//				return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
//			}
                // 处理日期格式、时间格式
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
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
        if (getValue01 == null) {
            getValue01 = "";
        }
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


    private static String getParse01(String value01){

        String resString = "^\\d*-\\d*-\\d*\\s+([\u4e00-\u9fa5]{1,4})\\d*:\\d*$";

        Date dd= new Date();
        DateFormat dd01 = new SimpleDateFormat("yyyyMMdd");
        String xx = dd01.format(dd);
        xx = xx.substring(0,2);

        String returnStr = "";
        //格式"16-1-5 上午12:00"
        if(value01.matches(resString)){
            //时间等于12点
            if(value01.substring((value01.lastIndexOf(":")-2)).compareTo("12:00") == 0){
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
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

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
            //获取标题行
            XSSFRow xssFirstRow = xssfSheet.getRow(headerrow);

            // 循环列Cell
            for(int i=0;i<xssFirstRow.getPhysicalNumberOfCells();i++){
                XSSFCell titleRow = xssFirstRow.getCell(i);
                //获取标题中文名
                String cellValue = getString(titleRow);

                for(TradeField tradeField:tradeFields){
                    if (cellValue.equals(replace(tradeField.getFieldName()))){
                        int row = Integer.parseInt(tradeField.getApprowVal());
                        int col = Integer.parseInt(tradeField.getAppcolVal());
                        if (row == (-1)) {row = rowIndex;}
                        if (col == (-1)) {col = i;}
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

    /**
     * 保存数据 ：解析数据
     *
     * @throws Exception
     */
    public abstract void saveData() throws Exception ;
    /**
     * 设置excel主配置ID
     *
     * @return
     */
//    public abstract Integer setId ();
    public abstract String setModName ();

    //设置数据库
    public abstract int setSharding();

    public Excel getExcel() {
        return excel;
    }

    public void setExcel(Excel excel) {
        this.excel = excel;
    }


    public List<TradeField> getTradeFields() {
        return tradeFields;
    }

    public void setTradeFields(List<TradeField> tradeFields) {
        this.tradeFields = tradeFields;
    }


    public List<Map<String, Object>> getL() {
        return l;
    }

    public void setL(List<Map<String, Object>> l) {
        this.l = l;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }
}
