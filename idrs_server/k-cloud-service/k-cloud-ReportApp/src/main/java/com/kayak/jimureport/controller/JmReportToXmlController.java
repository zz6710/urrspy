package com.kayak.jimureport.controller;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.action.BaseController;
import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.jimureport.report.dao.JmReportDao;
import com.kayak.jimureport.util.FileCharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Scope("prototype")
@Controller
public class JmReportToXmlController extends BaseController {

    @Resource(name = "jmReportToXmlBiz")
    private JmReportToXmlBiz jmReportToXmlBiz;

    @Resource(name = "jmReportDao")
    private JmReportDao jmReportDao;

    private String rootPath;

    private void initJmReport() {
        String system = System.getProperty("os.name");
        try {
            if (system.toLowerCase().startsWith("win")) {
                rootPath = SysUtil.getSystemParamsByParaid("70000010002");
            } else {
                rootPath = SysUtil.getSystemParamsByParaid("70000010009");
            }
            if(!rootPath.substring(rootPath.length()-1).equals("/")){
                rootPath = rootPath + "/";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "downloadJmReportXML.action")
    public void downloadJmReportXML(HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        initJmReport();
        //避免中文名字文件乱码
        String menuid = request.getParameter("menuid").toString();
        String type = request.getParameter("type");
        String jmmenuid = request.getParameter("jmmenuid");
        //报表涉及的数据库表的导出顺序数组
        if ("dataReport".equals(request.getParameter("type"))) {
            String[] tableNames = {"SYS_MENU", "JIMU_SQLDICT", "T8_DEFAULT_PARAM", "JIMU_REPORT", "JIMU_REPORT_DB", "JIMU_REPORT_DB_FIELD", "JIMU_REPORT_DB_PARAM", "T8_QUERY_SQL"};
            String filename = this.createJmApplicationConfigXML(tableNames, menuid, rootPath, type, jmmenuid);
            request.setAttribute("doc_name", filename);
            downloadJmReport(request, response);
        } else if ("printReport".equals(request.getParameter("type"))) {
            String[] tableNames = {"SYS_MENU", "SYS_REPORT_CONDITION", "T8_JMPRINT_SQL", "T8_DEFAULT_PRINTPARAM", "JIMU_REPORT", "JIMU_REPORT_DB", "JIMU_REPORT_DB_FIELD", "JIMU_REPORT_DB_PARAM", "T8_QUERY_SQL"};
            String filename = this.createJmApplicationConfigXML(tableNames, menuid, rootPath, type, jmmenuid);
            request.setAttribute("doc_name", filename);
            downloadJmReport(request, response);
        }
    }

    @RequestMapping(value = "downloadJmReport.action")
    public void downloadJmReport(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        //避免中文名字文件乱码
        String fileName = (String) request.getAttribute("doc_name");
        if ("".equals(fileName) || fileName == null) {
            System.out.println("前台的文档名称：" + request.getParameter("doc_name"));
            fileName = new String(request.getParameter("doc_name"));
        }
        if (!fileName.contains(".")) {
            fileName += ".xls";
        }
        System.err.println("-----------------文件名称:" + fileName);

        try {
            String system = System.getProperty("os.name");
            String rootPath = "";
            if (system.toLowerCase().startsWith("win")) {
                rootPath = SysUtil.getSystemParamsByParaid("70000010002");
            } else {
                rootPath = SysUtil.getSystemParamsByParaid("70000010009");
            }
            if(!rootPath.substring(rootPath.length()-1).equals("/")){
                rootPath = rootPath + "/";
            }
            String downLoadPath = rootPath + fileName;
            System.err.println("文件下载路径：" + downLoadPath);
            String brose = request.getParameter("brose");
            long fileLength = new File(downLoadPath).length();
            //下载文件固定步骤
            response.setContentType("application/x-octetstream;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            //设置文件名时要将其转化为 “ISO-8859-1” 编码
            String headFileName = null;
            if ("1".equals(brose)) {
                headFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            } else {//IE浏览器中文显示
                headFileName = URLEncoder.encode(fileName, "utf-8");
            }
            headFileName = headFileName.replace("transfer%2F", "");
            System.out.println("传出的文件头：" + headFileName);
            response.setHeader("Content-disposition", "attachment;filename=" + headFileName);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            //response输出流
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024 * 100];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // if (bis != null)
            assert bis != null;
            bis.close();
            // if (bos != null)
            assert bos != null;
            bos.close();
        }
    }

    public String createJmApplicationConfigXML(String[] tableNames, String menuid, String path, String type, String jmmenuid) throws Exception {
        initJmReport();
        //参数
        Map<String, Object> params = new HashMap<>();
        // 建立document对象
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("DATAPACKET");
        // 添加文档根
        root.addAttribute("Version", "2.0");
        for (String name : tableNames) {
            String tableName = name.toUpperCase();
            //获得表名
            // 根据表名查出表的字段名及属性
            Map<String, String> key = new HashMap<>();
            key.put("tableName", tableName);
            List<SqlRow> tableInfos = jmReportDao.selectTableInfo(key);
            Element tablename = root.addElement("TABLENAME");
            // 添加root的子节点
            tablename.addAttribute("TBLNAME", tableName);

            List<Map<String, String>> tableFields = new ArrayList<>();
            //存储表中所有的字段及属性
            for (SqlRow sqlRow : tableInfos) {
                Map<String, String> tableField = new HashMap<>();
                //存储表中单个的字段及属性
                tableField.put("table_name", sqlRow.getString("table_name"));
                //表名
                tableField.put("column_name", sqlRow.getString("column_name"));
                //字段名
                tableField.put("data_type", sqlRow.getString("data_type"));
                //字段类型
                tableField.put("data_length", sqlRow.getString("data_length"));
                //字段长度
                tableFields.add(tableField);

            }

            Element metaData = tablename.addElement("METADATA");
            // 添加tablename的子节点 METADATA
            Element rowData = tablename.addElement("ROWDATA");
            // 添加tablename的子节点 ROWDATA

            Element fields = metaData.addElement("FIELDS");
            // 添加metaData的子节点FIELDS

            StringBuilder allFields = new StringBuilder();
            // 将表中的字段全部解析到fields节点的子节点field中
            for (Map<String, String> tableField : tableFields) {
                Element field = fields.addElement("FIELD");
                // 添加fields的子节点FIELD
                field.addAttribute("attrname", tableField.get("column_name"));
                //字段名
                field.addAttribute("fieldtype", tableField.get("data_type"));
                //字段类型
                field.addAttribute("WIDTH", tableField.get("data_length"));
                //字段长度
                allFields.append(tableName).append(".").append(tableField.get("column_name")).append(",");
            }
            allFields = new StringBuilder(allFields.substring(0, allFields.length() - 1));
            //处理sql的尾部逗号
            //根据表名查询出表中的所有数据
            String sql1 = "select " + allFields + " from " + tableName;
            String str1 = null;
            String str2 = null;
            if ("printReport".equals(type)) {
                str1 = "(SELECT DISTINCT COUNT FROM  T8_JMPRINT_SQL WHERE ID ='" + menuid + "')";
                str2 = "(SELECT DISTINCT a.ID FROM JIMU_REPORT_DB a INNER JOIN T8_JMPRINT_SQL b ON b.COUNT=a.JIMU_REPORT_ID WHERE b.ID='" + menuid + "')";
                if ((!"".equals(menuid) && menuid != null) && "SYS_REPORT_CONDITION".equals(tableName)) {
                    sql1 = sql1 + " where FOR_TABLE = '" + menuid + "'";
                }
                //如果是单张报表导出，JIMU_SQLDICT、T8_DEFAULT_PARAM,根据menuid导出
                if ((!"".equals(menuid) && menuid != null) && ("T8_JMPRINT_SQL".equalsIgnoreCase(tableName) || "T8_DEFAULT_PRINTPARAM".equalsIgnoreCase(tableName))) {
                    sql1 = sql1 + " where ID = '" + menuid + "'" + " ORDER BY UPTTIME";
                }
            } else if ("dataReport".equals(type)) {
                str1 = "(SELECT DISTINCT COUNT FROM  JIMU_SQLDICT WHERE ID ='" + menuid + "')";
                str2 = "(SELECT DISTINCT a.ID FROM JIMU_REPORT_DB a INNER JOIN JIMU_SQLDICT b ON b.COUNT=a.JIMU_REPORT_ID WHERE b.ID='" + menuid + "')";
                //如果是单张报表导出，JIMU_SQLDICT、T8_DEFAULT_PARAM,根据menuid导出
                if ((!"".equals(menuid) && menuid != null) && ("JIMU_SQLDICT".equalsIgnoreCase(tableName) || "T8_DEFAULT_PARAM".equalsIgnoreCase(tableName))) {
                    sql1 = sql1 + " where ID = '" + menuid + "'" + " ORDER BY UPTTIME";
                }
            }
            //如果是单张报表导出，JIMU_REPORT
            if ((!"".equals(menuid) && menuid != null) && "JIMU_REPORT".equalsIgnoreCase(tableName)) {
                sql1 = sql1 + " WHERE ID=" + str1 + " ORDER BY JIMU_REPORT.ID";
                ;
            }
            if ((!"".equals(menuid) && menuid != null) && "SYS_MENU".equalsIgnoreCase(tableName)) {
                sql1 = sql1 + " where MENUID = '" + menuid + "' and MODULEID='8'";
            }
            //如果是单张报表导出，JIMU_REPORT_DB
            if ((!"".equals(menuid) && menuid != null) && "JIMU_REPORT_DB".equalsIgnoreCase(tableName) && StringUtils.isNotBlank(str1)) {
                sql1 = sql1 + " WHERE JIMU_REPORT_ID=" + str1 + " ORDER BY JIMU_REPORT_DB.JIMU_REPORT_ID";
            }
            //如果是单张报表导出，JIMU_REPORT_DB_FIELD
            if ((!"".equals(menuid) && menuid != null) && "JIMU_REPORT_DB_FIELD".equalsIgnoreCase(tableName) && StringUtils.isNotBlank(str2)) {
                sql1 = sql1 + " WHERE JIMU_REPORT_DB_ID in" + str2 + " ORDER BY ORDER_NUM";
            }
            //如果是单张报表导出，JIMU_REPORT_DB_PARAM
            if ((!"".equals(menuid) && menuid != null) && "JIMU_REPORT_DB_PARAM".equalsIgnoreCase(tableName) && StringUtils.isNotBlank(str2)) {
                sql1 = sql1 + " WHERE JIMU_REPORT_HEAD_ID in" + str2 + " ORDER BY ORDER_NUM";
            }
            //如果是单张报表导出，JIMU_REPORT_DB_PARAM
            if ((!"".equals(menuid) && menuid != null) && "T8_QUERY_SQL".equalsIgnoreCase(tableName) && StringUtils.isNotBlank(str2)) {
                sql1 = sql1 + " WHERE MENUID='" + jmmenuid + "' ";
            }
            params.put("newSql", sql1);
            List<SqlRow> sqlRows = jmReportDao.selectSqlInfos(sql1, params);
//				List<SqlRow> sqlRows=commonService.selectBySql(params);
            //List<SqlRow> sqlRows = sqlResult.getRows();
            int n = 0;
            for (SqlRow sqlRow : sqlRows) {
                Element row = rowData.addElement("ROW");
                //添加子节点的属性
                for (Map<String, String> map : tableFields) {
                    String s = sqlRow.getString(map.get("column_name"));
                    if (!"".equals(s) && s != null) {
                        //当字段的属性是CLOB时，此字段的数据做特殊处理
                        if (map.get("data_type") != null && "CLOB".equalsIgnoreCase(map.get("data_type"))) {
                            s = sqlRow.getString(map.get("column_name"));
                            s = s.replace("\n", "&#013;&#010;");
                            s = s.replace("<", "&llt;");
                            s = s.replace(">", "&ggt;");
                            s = s.replace("'", "&aapos;");
                        }
                        //当字段的属性是CLOB时，此字段的数据做特殊处理
                        if (map.get("data_type") != null && "LONG".equalsIgnoreCase(map.get("data_type"))) {
                            s = s.replace("\n", "&#013;&#010;");
                            s = s.replace("<", "&llt;");
                            s = s.replace(">", "&ggt;");
                            s = s.replace("'", "&aapos;");
                        }
                        //当字段的属性是NCLOB时，此字段的数据做特殊处理
                        if (map.get("data_type") != null && "NCLOB".equalsIgnoreCase(map.get("data_type"))) {
                            s = nclobToString((NClob) sqlRow.get(map.get("column_name")));
                            s = s.replace("\n", "&#013;&#010;");
                            s = s.replace("<", "&llt;");
                            s = s.replace(">", "&ggt;");
                            s = s.replace("'", "&aapos;");
                        }
                        row.addAttribute((String) map.get("column_name"), s);
                    }
                }
                n++;
            }
            System.out.println(tableName + "表总共导出了" + n + "条记录");
        }
        //Map加入菜单id
        params.put("menuid", menuid);
        //新增SQL节点
        if ("dataReport".equals(type)) {
            root = Jm_add_SQL_Element(root, params);
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("GBK");
        // 输出全部原始数据，并用它生成新的我们需要的XML文件
        File filePath = new File(rootPath + "");
        if (!filePath.exists()) {
            boolean createDir = filePath.mkdirs();
            System.out.println("下载:Root文件夹创建" + (createDir ? "成功" : "失败"));
        } else {
            System.out.println("下载:Root文件夹已存在");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        File file = null;
        if (!"".equals(menuid) && menuid != null) {
            //如果是单表导出 文件名为菜单ID 文件名自动加上日期时间

            SqlRow menuname = jmReportDao.selectMenuName(params);
            String chinName = "";
            if (!menuname.isEmpty()) {
                chinName = menuname.getString("menuname");
            }
            file = new File(rootPath + chinName + "(" + menuid + ")_" + sdf.format(new Date()) + ".xml");
        } else {
            file = new File(rootPath + "demo" + "_" + sdf.format(new Date()) + ".xml");
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            XMLWriter writer2 = new XMLWriter(new FileWriter(file), format);
            //想要导出什么名字的xml，自己改
            writer2.write(document);
            // 输出到文件
            writer2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getName();

    }

    //新增SQL节点
    private Element Jm_add_SQL_Element(Element root, Map<String, Object> map) throws Exception {
        Element sqls = root.addElement("SQLS");
        // 添加root的子节点
        //存放数据库中取出来的额SQL
        String sql = "begin delete from sys_menu m where m.menuid='" + map.get("menuid").toString() + "';";
        sql += "delete from sys_role_right where menuid='" + map.get("menuid").toString() + "' and roleid='*';";
        SqlRow menuInfo = jmReportDao.selectMenuInfo(map);
        if (!menuInfo.isEmpty()) {
            sql += "insert into sys_menu(MODULEID,MENUID,MENUNAME,UPPERID,URL,ICONCLS,ICON,LOADORDER,STATUS,PAGEID,FASTCODE,FUNCTYPE,REMARK,MENUTYPE) values (";
            sql += "'" + menuInfo.getString("moduleid") + "',";
            sql += "'" + menuInfo.getString("menuid") + "',";
            sql += "'" + menuInfo.getString("menuname") + "',";
            sql += "'" + menuInfo.getString("upperid") + "',";
            sql += "'" + menuInfo.getString("url") + "',";
            sql += "null,";
            sql += "'" + menuInfo.getString("icon") + "',";
            sql += "'" + menuInfo.getString("loadorder") + "',";
            sql += "'" + menuInfo.getString("status") + "',";
            sql += "'" + menuInfo.getString("pageid") + "',";
            sql += "null,";
            sql += "'" + menuInfo.getString("functype") + "',";
            sql += "null,";
            sql += "'" + menuInfo.getString("menutype") + "');";
        }
        sqls.setText(sql);
        SqlRow role_right = jmReportDao.selectRoleRight(map);
        if (!role_right.isEmpty()) {
            sql += "insert into sys_role_right(ROLEID,MODULEID,MENUID,BUTTONID,EXEID,BUTTONDESC,UPPERID,RTYPE,FUNCTYPE,REMARK) values (";
            sql += "'" + role_right.getString("roleid") + "',";
            sql += "'" + role_right.getString("moduleid") + "',";
            sql += "'" + role_right.getString("menuid") + "',";
            sql += "'" + role_right.getString("buttonid") + "',";
            sql += "'null',";
            sql += "'" + role_right.getString("buttondesc") + "',";
            sql += "'" + role_right.getString("upperid") + "',";
            sql += "'" + role_right.getString("rtype") + "',";
            sql += "null,";
            sql += "null); ";
        }
        sql = sql + "end;";
        sqls.setText(sql);
        return root;
    }

    @RequestMapping(value = "uploadJmReportXML.action", method = RequestMethod.POST)
    public @ResponseBody
    String uploadJmReportXML(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        Map<String, Object> returndata = new HashMap<String, Object>();
        //定义返回的结果集
        initJmReport();
        //按钮打印标志，若果是打印的xml,不插入菜单表
        Map<String, Object> param = RequestSupport.getParameters();
        String is_print = param.get("is_print").toString();
        boolean print = false;
        if ("true".equals(is_print)) {
            print = true;
        }
        String single = request.getParameter("single");
        //值1全部导入 值0单个表导入
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("file");
        boolean upload = false;
        if ("1".equalsIgnoreCase(single)) {
            //全部报表导入
            // 报表涉及的数据库表的删除顺序数组
            String[] tables = {"JIMU_SQLDICT", "T8_DEFAULT_PARAM", "JIMU_REPORT", "JIMU_REPORT_DB", "JIMU_REPORT_DB_FIELD", "JIMU_REPORT_DB_PARAM", "T8_QUERY_SQL"};
            int m = 0;
            for (String table : tables) {
                String sql = "delete from " + table;
                jmReportDao.deleteTableInfo(sql);
                m++;
            }
            System.out.println("删除成功" + m + "张表");
        }
        for (int i = 0; i < files.size(); i++) {
            if ("0".equals(single) && i > 0) {
                break;
            }
            MultipartFile file = files.get(i);
            File filef = JmReportToXmlBiz.multipartFileToFile(files.get(i));
            String javaEncode = FileCharsetUtil.getJavaEncode(filef);
            JmReportToXmlBiz.deleteFile(filef);
            InputStream in = null;
            try {
                in = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                jmReportToXmlBiz.insertXMLInfo(in, print, javaEncode);
                upload = true;
            } catch (SQLException e) {
                upload = false;
                e.printStackTrace();
                break;
            } catch (Exception e) {
                returndata.put("msg", "上传失败:" + e.getMessage());
                return updateSuccess(returndata);
            }
        }
        if (upload) {
            returndata.put("msg", "上传完成");
        } else {
            returndata.put("msg", "上传失败");
        }
        return updateSuccess(returndata);
    }

    public String clobToString(Clob clob) throws SQLException, IOException {
        Reader is = clob.getCharacterStream();
        return changeToStr(is);
    }

    public String nclobToString(NClob nClob) throws SQLException, IOException {
        Reader is = nClob.getCharacterStream();
        return changeToStr(is);
    }

    public String changeToStr(Reader is) throws IOException {
        String reString = "";
        // 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuilder sb = new StringBuilder();
        while (s != null) {
            // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s).append("\n");
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }
}
