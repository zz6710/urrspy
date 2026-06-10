package com.kayak.dps.direct.util;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.direct.enums.DataFileEnum;
import com.kayak.dps.direct.model.ExFmt;
import com.kayak.dps.direct.model.ExSeat;
import fpr.dmsg.client.FprDClient;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

//axin
//中债直连工具类
public class DirectUtils {
    private static final Logger log = LoggerFactory.getLogger(DirectUtils.class);

    /**
     * 校验dataname是否包含regex中字符集
     * @param dataname
     * dataname.contains(regexs[i]  并不能完全判定值域包含 例如 001 新增  {00,01,02}  将返回true
     * @param regex
     * @return
     */
    public static int conUtil(String dataname,String regex){
        int ret = -1;
        if(dataname.contains(" ")){//如果存在空格  01 新增
            dataname=dataname.substring(0,dataname.indexOf(" ")); //01
        }
        String[] split = regex.split(",");//得到字符串数组
        List<String> strings = Arrays.asList(split);//将数组转化为List
        return strings.contains(dataname)?0:-1;
    }


    /**
     * 获取修正后的值
     * @return
     */
    public static Object getCorrectValue(SqlRow row, String key, Object value){
        Object val = value==null?"":value;
        if(row!=null){
            if(row.get(key)!=null&&!"".equals(row.get(key))){
                val = row.get(key);
            }
        }

        return val.toString();
    }


    /**
     * 检查字段是否在字典范围内
     * @param dict
     * @param value
     * @return
     */
    public static String checkDict(String dict, String value) throws Exception {
        String desc = "";
        if("".equals(value) || value==null){
            return desc;
        }
        if(!getDictValueCheck(dict, value)){
            desc = "不在值域范围内。<br/>";
        }
        return desc;
    }

    /**
     * 检查字段是否在字典范围内
     * @param dict
     * @param value
     * @return
     */
    public static String checkDict(String dict, String value, String name,boolean isExport) throws Exception {
        String desc = "";
        if ("".equals(value) || value == null) {
            return desc;
        }
        //如果是导入的校验 value格式 01 新增，如果是数据生成的 格式 01
        if(isExport){
            if (!getDictValueEx(dict, value)) {
                desc =getErrInt()+name + "不在值域范围内。<br/>";
            }
        }else{
            if (!get202DictValue(dict, value)) {
                desc = getErrInt()+name + "不在值域范围内。<br/>";
            }
        }

        return desc;
    }

    public static String getErrInt(){
        return ++DirectParams.errInt+"：";
    }


    /**
     * 获取数据字典值， 用逗号隔开
     *
     * @param dict
     * @return
     */
    public static boolean getDictValueEx(String dict, String value) throws Exception {
        if (DirectParams.dict_mapex.get(dict) == null)
            throw new Exception("数据字典base_ex_map：" + dict + "不存在");
        return DirectParams.dict_mapex.get(dict).contains(value);
    }

    /**
     * 获取数据字典值， 用逗号隔开
     *
     * @param dict
     * @return
     */
    public static boolean get202DictValue(String dict, String value) throws Exception {
        if (DirectParams.dict_map_vol.get(dict) == null)
            throw new Exception("数据字典base_ex_map：" + dict + "不存在");
        return DirectParams.dict_map_vol.get(dict).contains(value);
    }


    public static boolean getDictValueCheck(String dict, String value) throws Exception {
        if (DirectParams.dict_map_check.get(dict) == null)
            throw new Exception("数据字典sys_dict_item：" + dict + "不存在");
        return DirectParams.dict_map_check.get(dict).contains(value);
    }



    /**
     * 获取字典名称
     * @param dict
     * @param key
     * @return
     * @throws Exception
     */
    public static String getDictName(String dict, String key) throws Exception {
        String out_value = "";

        if("".equals(key)||key==null){
            return out_value;
        }
        if(key.contains(" ")){
            key = key.split(" ")[0];
        }

        if (DirectParams.dict_name.get(dict) == null) {
            throw new Exception("数据字典base_ex_map：" + dict + "不存在");
        }
        out_value = DirectParams.dict_name.get(dict).get(key);
        out_value = out_value==null?"":out_value;
        return out_value;
    }


    /**
     * 获取系统中的字典值
     * @param dict
     * @param key
     * @return
     * @throws Exception
     */
    public static String getDictNameSys(String dict, String key) throws Exception {
        /*if (DirectParams.dict_name.get(dict) != null && ObjectUtil.isNotEmpty(DirectParams.dict_name.get(dict).get(key))) {
            return DirectParams.dict_name.get(dict).get(key);
        }*/
        if (DirectParams.dict_name_sys.get(dict) != null && ObjectUtil.isNotEmpty(DirectParams.dict_name_sys.get(dict).get(key))) {
            return DirectParams.dict_name_sys.get(dict).get(key);
        }

        throw new Exception("数据字典sys_dict_item、base_ex_map：" + dict + ":" + key + "不存在");
    }


    /**
     * 检查数值类型
     * @param exfmts
     * @param key
     * @param value
     * @return
     */
    public static String checkFMT(List<SqlRow> exfmts, String key, Object value){
        String desc = "";
        SqlRow exFmt = null;
        for(int i=0; i<exfmts.size(); i++){
            SqlRow tmp = exfmts.get(i);
            if(tmp.getString("fld").equals(key.toUpperCase())){
                exFmt = tmp;
                break;
            }
        }
        if(exFmt!=null && value!=null && !"".equals(value)){
            if(exFmt.getString("itmtp").equals("C")){
                int len = value.toString().length();
                if(Double.parseDouble(exFmt.getString("itmprc")) - len!=0){
                    log.info(exFmt.getString("itmprc") + "|" + value.toString().length());
                    return "长度不对";
                }
            }
        }

        return desc;
    }

    /**
     * 检查数值类型
     *
     * @param exfmts
     * @param key
     * @param value
     * @return
     */
    public static String checkFMTEx(List<ExFmt> exfmts, String key,
                                       Object value, String name) {
        String desc = "";
        ExFmt exFmt = null;
        for (int i = 0; i < exfmts.size(); i++) {
            ExFmt tmp = exfmts.get(i);
            if (tmp.getFld().equals(key.toUpperCase())) {
                exFmt = tmp;
                break;
            }
        }
        if (exFmt != null && value != null && !"".equals(value)) {
            if ("C,V".indexOf(exFmt.getDictItmtp()) > -1) {
                int len =  value.toString().getBytes().length;//length((String) value);
                if (exFmt.getItmprc() - len < 0) {
                    log.info(exFmt.getItmprc() + "|"
                            + value.toString().getBytes().length);
                    return getErrInt()+name + "过长。<br/>";
                }
            }
        }

        return desc;
    }


    /**
     * 将20200101转变为2020-01-01
     * @param date
     * @return  date.matches("/^\d{8}$/")  for update   zhanglei  限定格式更改只限用于 yyyymmdd格式
     */
    public static String getS_d(String date){
        if(date.length() == 8 && date.matches("^\\d{8}$")){
            GregorianCalendar cal = new GregorianCalendar();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                cal.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateFormat.format(cal.getTime());
        }else{
            return date;
        }

    }


    /**
     * 对.zip文件进行解压缩
     * @param zippath
     * @param targetpath
     * @throws Exception
     */
    public static void unZipFile(String zippath, String targetpath) throws Exception{
        byte[] _byte = new byte[1024];
        File zipFile = new File(zippath);
        List<File> _list = new ArrayList<File>() ;
        try {
            ZipFile _zipFile = new ZipFile(zipFile) ;
            for(Enumeration<? extends ZipEntry> entries = _zipFile.entries(); entries.hasMoreElements() ; ){
                ZipEntry entry = entries.nextElement();
                File _file = new File(targetpath + File.separator + entry.getName()) ;
                if( entry.isDirectory() ){
                    _file.mkdirs() ;
                }else{
                    File _parent = _file.getParentFile() ;
                    if( !_parent.exists() ){
                        _parent.mkdirs() ;
                    }
                    InputStream _in = _zipFile.getInputStream(entry);
                    OutputStream _out = new FileOutputStream(_file) ;
                    int len = 0 ;
                    while( (len = _in.read(_byte)) > 0){
                        _out.write(_byte, 0, len);
                    }
                    _in.close();
                    _out.flush();
                    _out.close();
                    _list.add(_file) ;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("解压文件失败:"+e.getMessage());
        }


    }


    public static String getFilePath() {
        return getFilePath(DirectParams.localfilePath +File.separator+ DateUtil.getNowDate()+File.separator);
    }

    public static String getFilePath(String filePath,String procDate, String tcode) {
        if (filePath == null)
            return "";
        if (!filePath.endsWith(File.separator))
            return filePath + File.separator;

        filePath += tcode + File.separator;
        filePath += procDate + File.separator;
        return getFilePath(filePath);
    }

    public static String getFilePath(String filePath){
        File f = new File(filePath);
        if (f.isFile() || !f.exists()) {
            /* 主目录不存在则重新创建 */
            try {
                f.mkdirs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return filePath;
    }


    /**
     * 校验dataname是否包含regex中字符集
     * @param dataname
     * @param regex
     * @return
     */
    public static int conUtilRe(String dataname,String regex){
        int ret = -1;
        if(regex.contains(",")){
            String[] regexs = regex.split(",");
            for(int i=0;i<regexs.length;i++){
                if (dataname.contains(regexs[i])) {
                    ret = i;
                    break;
                }else{
                    ret = -1;
                }
            }
        }else{
            if (dataname.contains(regex)) {
                ret = 0;
            }else{
                ret = -1;
            }
        }
        return ret;
    }


    public static String replace(String text, String searchString,
                                 String replacement) {
        return StringUtils.replace(text, searchString, replacement);
    }

    /**
     * 解析指定文件名称格式串为实际文件名称
     *
     * @param filename
     * @param packid
     * @param fcode
     * @param index
     * @param procDate
     * @return
     * @throws Exception
     */
    public static String parseFileName(String filename, String packid, String index, String fcode, String procDate) throws Exception {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        if (packid == null) {
            packid = "";
        }
        filename = replace(filename, DirectParams.CREATERFLAG, fcode);
        filename = replace(filename, DirectParams.FILETYPEFLAG,
                DataFileEnum.EXTPID_118.getValue().equals(packid) ? "reg001" + packid : packid);
        filename = replace(filename, DirectParams.PROCDATEFLAG, procDate);
        filename = replace(filename, DirectParams.FILESERNO, index);
        return filename;
    }


    public static String format(String date, String format) {
        String year = "";
        String month = "";
        String day = "";
        String retDate = "";

        if (date == null)
            return "";

        date = date.trim();
        if (date.length() < 4) {
            return date;
        }
        if (date.length() >= 4) {
            year = date.substring(0, 4);
        }
        if (date.length() >= 6) {
            month = date.substring(4, 6);
        }
        if (date.length() >= 8) {
            day = date.substring(6, 8);
        }
        retDate = format;
        retDate = retDate.toUpperCase();
        retDate = StringUtils.replaceOnce(retDate, "YYYY", year);
        retDate = StringUtils.replaceOnce(retDate, "MM", month);
        retDate = StringUtils.replaceOnce(retDate, "DD", day);

        return retDate;
    }


    /**
     * 生成压缩文件名称
     * @param fcode
     * @param procDate
     * @param index
     * @return
     */
    public static String parseZipFileName(String fcode, String packid, String procDate, String index, String fileType){
        return fcode + "-" + (DataFileEnum.EXTPID_118.getValue().equals(packid) ? "reg001" + packid : packid) +
                "-" + procDate + "-" + index + fileType;
    }


    public static void ZipFiles(String dirPath, String targetName, List<String> files) throws Exception {
        if (dirPath == null || dirPath.equals("")) { // 判断根目录是否存在
            System.out.println("压缩失败" + dirPath + "目录不存在");
            return;
        }
        File baseDir = new File(dirPath); // 判断dirPath是不是目录
        if (!baseDir.exists() || (!baseDir.isDirectory())) {
            log.info("目录不存在:" + baseDir + " 创建此目录！");
            baseDir.mkdir();
        }
        String zipFilePath = dirPath+targetName;
        File zipFile = new File(zipFilePath);
        if (zipFile.exists()) {
            // 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
            // SecurityManager securityManager = new SecurityManager();
            // securityManager.checkDelete(zipFilePath);
            // 删除已存在的目标文件
            zipFile.delete();
        }

        String basicRootDir = baseDir.getAbsolutePath();
        log.info("basicRootDir" + basicRootDir);
        File targetFile = new File(zipFilePath); // 创建zip文件
        try {
            // 创建一个zip输出流来压缩数据并写入到zip文件
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath));
            for(String e:files) {
                //File file = new File(files.get(e));
                File file = new File(e);
                //String ident_code=e[2];
                if (file.isFile()) {
                    compressFileToZip( file, out);
                } else {
                    throw new Exception("读取文件" + e+ "失败!");
                }
            }
            out.close();
            log.info("文件压缩成功，压缩包的文件名为：" + targetName);
        } catch (IOException e) {
            log.info("压缩失败：" + e);
            e.printStackTrace();
        }
    }


    // 利用ZipOutputStream对文件的压缩
    private static void compressFileToZip(File file, ZipOutputStream out) {
        FileInputStream in = null;
        ZipEntry entry = null;
        byte[] buffer = new byte[4096]; // 创建复制缓冲区
        int bytes_read;
        if (file.isFile()) {
            try {
                in = new FileInputStream(file); // 创建一个文件输入流
                // 根据压缩文件的名字构造一个ZipEntry对象，此类表示zip包中的文件条目

                entry = new ZipEntry(file.getName());

                out.putNextEntry(entry); // 存储项信息到压缩文件
                // 将文件的内容通过字节数组复制到压缩文件中
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
                in.close();
                log.info("添加文件" + file.getAbsolutePath() + "到ZIP文件中！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩为ZIP文件
     * @param filePath
     * @param zipFileName
     * @param fileNameList
     */
    public static void zipFile(String filePath, String zipFileName, List<String> fileNameList) throws Exception {
        String normPath = filePath.endsWith(File.separator) ? filePath : filePath + File.separator;
        File zipFile = new File(normPath + zipFileName);

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            // 10KB 缓冲
            byte[] buffer = new byte[1024 * 10];

            // 创建ZIP实体，并添加进压缩包，读取待压缩的文件并写进压缩包里
            for (String fileName : fileNameList) {
                File file = new File(normPath + fileName);
                log.info("开始压缩文件："+normPath + fileName);
                if (!file.isFile()) {
                    log.error("文件不存在或不可读: {}", file.getAbsolutePath());
                    throw new FileNotFoundException("文件不存在或不可读: " + file.getAbsolutePath());
                }
                // 为每个文件建个新的实体
                zos.putNextEntry(new ZipEntry(fileName));

                try (FileInputStream fis = new FileInputStream(file)) {
                    int read;
                    while ((read = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, read);
                    }
                } finally {
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            log.error("压缩文件失败:{}", e.getMessage(), e);
            throw new Exception("压缩文件失败:" + e.getMessage(), e);
        }
    }


    /**
     * 压缩zip文件
     * @param filepath
     * @param zipFileName
     * @param fileName
     * @throws Exception
     */
    public static void zipFile(String filepath, String zipFileName, String fileName) throws Exception{
        //压缩文件
        String zipFilePath = filepath+zipFileName;
        File zipFile = new File(zipFilePath);
        if (zipFile.exists())  {
            //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
            //SecurityManager securityManager = new SecurityManager();
            //securityManager.checkDelete(zipFilePath);
            //删除已存在的目标文件
//            zipFile.delete();
        }
        FileInputStream fis = null;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
//            if (!zipFile.exists()) {
//                zos = new ZipOutputStream(new FileOutputStream(zipFile));
//            }
            byte[] bufs = new byte[1024*10];

            String filepathname = filepath+fileName;
            zos.putNextEntry(new ZipEntry(fileName));  			//创建ZIP实体，并添加进压缩包
            fis = new FileInputStream(new File(filepathname));  	//读取待压缩的文件并写进压缩包里

            int read = 0;
            while((read=fis.read(bufs)) > 0){
                zos.write(bufs,0,read);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("压缩文件失败:"+e.getMessage());
        } finally{
            fis.close();
            zos.close();
        }
    }


    public static String createSql(ExSeat exseat,String report_date) {
        StringBuilder sqlbuf = new StringBuilder("select ");
        StringBuilder sqlbufrn = new StringBuilder("select ");
        //exFmt配置表字段值排重
        Set<String> exFmtSet = new HashSet<>();
        for (int i = 0; i < exseat.getFieldList().size(); i++) {
            ExFmt exfmt = (ExFmt) exseat.getFieldList().get(i);
            if (!exFmtSet.add(exfmt.getFld())) {
                continue;
            }
            if (!"*".equals(exfmt.getFld())) {
                sqlbuf.append("REG.").append(exfmt.getFld());
                sqlbufrn.append(exfmt.getFld());
                if (i < exseat.getFieldList().size() - 1) {
                    sqlbufrn.append(",");
                }
                sqlbuf.append(",");
            }
        }

        String selsql = sqlbuf.toString();
        String sqlrn = sqlbufrn.toString();
        if (selsql.charAt(selsql.length() - 1) == ',') {
            selsql = selsql.substring(0, selsql.length() - 1);
        }
        if (sqlrn.charAt(sqlrn.length() - 1) == ',') {
            sqlrn = sqlrn.substring(0, sqlrn.length() - 1);
        }
        //已复核状态下，报送状态为0、1、2、4的数据都可生成；未复核状态下只生成报送状态为2、4的数据
        //注意exseat.getExtab 与 别名REG中间放置空格
        String status="5";
        if("app_prod_regist_filing_info".equals(exseat.getExtab()) || "app_asset_regist_info".equals(exseat.getExtab())){
            status="2";
        }
        sqlrn += "\nfrom (";
        selsql += " from " + exseat.getExtab() + " REG LEFT JOIN base_report_data_audit_results ARS  ON ARS.table_id = '" + exseat.getExtab() + "'AND REG.theory_report_start_date = ARS.report_date  where theory_report_start_date='" + report_date
                + "' and sys_data_status='1' AND ARS.audit_status = 1 AND REG.register_status IN ('"+status+"') ";
        if("app_prod_regist_filing_info".equals(exseat.getExtab()) || "app_prod_issuance_regist_info".equals(exseat.getExtab()) || "app_initial_sub_regist_info".equals(exseat.getExtab())){
            selsql = sqlrn + selsql + " and sys_data_version='1.0') t ";
        }else if("app_asset_debt_register_info".equals(exseat.getExtab())){
            selsql = sqlrn + selsql + " and data_change_type='0') t ";
        }else{
            selsql = sqlrn + selsql + " ) t ";
        }
        return selsql;
    }

    /**
     * 获取更新语句
     * @param exseat
     * @param report_date
     * @return
     */
    public static String createUpdateSql(ExSeat exseat,String report_date) {
        String selsql = "";
        if("app_prod_regist_filing_info".equals(exseat.getExtab()) || "app_initial_sub_regist_info".equals(exseat.getExtab()) || "app_prod_issuance_regist_info".equals(exseat.getExtab())){
            selsql = "update "+ exseat.getExtab() + " REG LEFT JOIN base_report_data_audit_results ARS  ON ARS.table_id = '" + exseat.getExtab() + "'AND REG.theory_report_start_date = ARS.report_date " +
                    "  set REG.register_status = '5'  where theory_report_start_date='" + report_date
                    + "' and sys_data_status='1' AND ARS.audit_status = 1 AND REG.register_status ='2' and sys_data_version='1.0' ";
        }else{
             selsql = "update "+ exseat.getExtab() + " REG LEFT JOIN base_report_data_audit_results ARS  ON ARS.table_id = '" + exseat.getExtab() + "'AND REG.theory_report_start_date = ARS.report_date " +
                    "  set REG.register_status = '5'  where theory_report_start_date='" + report_date
                    + "' and sys_data_status='1' AND ARS.audit_status = 1 AND REG.register_status ='2' ";
        }
        return selsql;
    }

    public static void addUnit(Element xbrl, HashMap<String, Element> unit, String itmtp) {
        String measure = itmtp.split(":")[1];
        int index = 3;
        if (!unit.containsKey("context")) {
            index = 2;
        }
        if ("wemax-unit:Month/wemax-uinit:Count".equals(itmtp)) {
            measure = "MonthPerCount";
        }
        if ("xbrli:pure".equals(itmtp)) {
            measure = "PURE";
        }
        if (!unit.containsKey(measure)) {
            Element a = DocumentHelper.createElement("xbrli:unit").addAttribute("id", measure);
            xbrl.elements().add(index, a);
            a.addElement("xbrli:measure").addText(itmtp);
            unit.put(measure, a);
        }
    }

    public static Element createInstantId(Element xbrl, String instant_id) {

        // Element context = xbrl.addElement("xbrli:context");
        Element context = DocumentHelper.createElement("xbrli:context");
        xbrl.elements().add(2, context);
        context.addAttribute("id", instant_id);
        Element entity = context.addElement("xbrli:entity");
        Element identifier = entity.addElement("xbrli:identifier");
        identifier.addAttribute("scheme", "http://www.pbc.gov.cn/").addText(DirectParams.bankCode);
        Element period = context.addElement("xbrli:period");
        Element instant = period.addElement("xbrli:instant");
        instant.addText(DirectUtils.format(DirectParams.preWorkDate, "YYYY-MM-DD"));
        return context;
    }
    public static String addNumField(String fieldval, Element field, Long scale, String unitRef) {
        fieldval = new BigDecimal(fieldval).setScale(scale.intValue(), BigDecimal.ROUND_HALF_DOWN)
                .toPlainString();
        field.addAttribute("unitRef", unitRef);
        field.addAttribute("decimals", scale.toString());

        return fieldval;
    }

    public static String dateAdd(String start, int days) {
        int year;
        int month;
        int day = 0;
        start = start.trim();
        if (start.length() >= 6) {
            year = Long.valueOf(start.substring(0, 4)).intValue();
            month = Long.valueOf(start.substring(4, 6)).intValue();
            if (start.length() >= 8) {
                day = Long.valueOf(start.substring(6, 8)).intValue();
            }
        } else {
            return start;
        }
        GregorianCalendar curCal = new GregorianCalendar(year, month - 1, day);
        if (start.length() >= 8) {
            curCal.add(GregorianCalendar.DAY_OF_MONTH, days);
        } else {
            curCal.add(GregorianCalendar.MONTH, days + 1);
        }

        String date = "";
        String tmpStr = "";
        date = String.valueOf(curCal.get(Calendar.YEAR));
        tmpStr = String.valueOf(curCal.get(Calendar.MONTH) + 1);
        if (tmpStr.length() < 2) {
            tmpStr = "0" + tmpStr;
        }
        date += tmpStr;
        tmpStr = String.valueOf(curCal.get(Calendar.DAY_OF_MONTH));
        if (tmpStr.length() < 2) {
            tmpStr = "0" + tmpStr;
        }
        if (day > 0) {
            date += tmpStr;
        }

        return date;
    }


    /**
     * 轮询获取消息
     * @param msgType
     * @throws Exception
     */
    public static List<Map<String, Object>> getRegisterMsg(String msgType , String workDate) throws Exception{
        List<Map<String, Object>> result_list = new ArrayList<>();
        int trytimes = Integer.parseInt(DirectParams.trytimes);	//轮询次数
        int sleeptime = Integer.parseInt(DirectParams.sleeptime);	//每次轮询等待时间
        int i = 0;
        while (true) {// 也可以通过TimerTask实现
            Calendar c = Calendar.getInstance();
            int h = c.get(Calendar.HOUR_OF_DAY);
            if (h < 1 || h > 5) {// 避开DI日终时间
                result_list = dealMessage(msgType, workDate);
                if(result_list.size()>0){//信息获取完毕
                    break;
                }
                Thread.sleep(sleeptime * 1000);// 避免抛服务器繁忙.
            }
            i++;
            if(i>trytimes){
                break;
            }
        }
        return result_list;
    }

    /**
     * 获取消息
     */
    public static List<Map<String, Object>> dealMessage(String msgType, String workdate) throws Exception {
        List<Map<String, Object>> result_list = new ArrayList<>();
        Map<String, Object> results = null;

        FprDClient fdc = new FprDClient();
        List<?> list = fdc.getMessage(msgType);
        for (int i = 0; i < list.size(); i++) {
            String s = (String) list.get(i);
            results = new HashMap<>();
            log.info(s);// 打印消息

            // TODO 处理消息, 确认返回情况
            Document doc = DocumentHelper.parseText(s);	// 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点

            Element document = rootElt.element("Document");

            String sysType = document.elementText("SysType");
            String msgType_ = document.elementText("MsgType");
            String fileId = document.elementText("FileId");
            String fileName = document.elementText("FileName");
            String origFileName = document.elementText("OrigFileName");
            String successCount = document.elementText("SuccessCount");
            String failedCount = document.elementText("FailedCount");
            String totalCount = document.elementText("TotalCount");
            String errorCode = document.elementText("ErrorCode");
            String errorText = document.elementText("ErrorText");
            String status = "2";
            if(!"0000".equals(errorCode)){
                status = "4";
                log.info("理财中心返回错误信息：" + errorText);
                //throw new Exception(errorText);
            }

            results.put("sysType", sysType);
            results.put("msgType_", msgType_);
            results.put("fileId", fileId);
            results.put("fileName", fileName);
            results.put("origFileName", origFileName);
            results.put("successCount", successCount);
            results.put("failedCount", failedCount);
            results.put("totalCount", totalCount);
            results.put("errorCode", errorCode);
            results.put("errorText", errorText);
            results.put("status", status);
            results.put("workdate", workdate);
            Element bizInfo = rootElt.element("MsgHeader").element("BizInfo");
            results.put("origmsgid", bizInfo.elementText("OrigMsgId"));
            result_list.add(results);

        }

        return result_list;
    }

    /**
     * 用于增加空值，null值及字符串“null”、“NULL”等值的判断
     * @return
     */
    public static Boolean NotEmptyAndNull(Object val) {
        if (ObjectUtil.isEmpty(val)) {
            return false;
        }
        if ("null".equals(String.valueOf(val).toLowerCase())) {
            return false;
        }
        return true;
    }
}
