package com.kayak.jimureport.controller;

import com.kayak.base.dao.BaseDao;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;

import com.kayak.jimureport.report.dao.JmReportDao;
import com.kayak.jimureport.util.UnicodeInputStream;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

@Scope("prototype")
@Service(value = "jmReportToXmlBiz")
public class JmReportToXmlBiz extends BaseDao {
    /**
     * 用于访问数据库，调用SQL配置的数据连接对象
     */
    @Resource(name = "jmReportDao")
    private JmReportDao jmReportDao;


    /**
     * 将xml文件的文件流数据导入到表中
     *
     * @param in     xml文件文件流
     * @param single 值1全部导入 值0单个表导入
     * @throws Exception
     */
    @SuppressWarnings("all")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertXMLInfo(InputStream in, boolean is_print, String Encode)
            throws Exception {
        SAXReader reader = new SAXReader();
        Document document = null;
        Map<String, Object> map = new HashMap<>();
        try {
            //原来为GBK
            UnicodeInputStream uin = new UnicodeInputStream(in, Encode);
            Encode = uin.getEncoding();//干掉bom头
            Reader read = new InputStreamReader(uin, Encode);
            document = reader.read(read);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        // 获取根元素 DATAPACKET
        List<Element> tableNames = root.elements();
        // 获取元素下面的elements集合
        String id = "";
        String count1 = "";
        String count2 = "(";
        List<String> countList = new ArrayList<>();
        boolean over = false;
        if (!is_print) {
            //找出SQL节点，并取相应内容
            for (int k = 0; k < tableNames.size(); k++) {
                //得到第k个节点
                Element e_note = tableNames.get(k);
                //获取节点名
                String e_name = e_note.getName();
                //如果是SQL节点，通常都会执行SQL语句
                if (e_name.equals("SQLS")) {
                    String sql_text = e_note.getText();
                    if (!"".equals(sql_text)) {
                        try {
                            jmReportDao.update(sql_text, map);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new Exception("SQL语句有误：" + sql_text);
                        }
                    }
                }
            }
        }
        List<String> tableList = new ArrayList<>();
        //获取for_table
        for (int k = 0; k < tableNames.size(); k++) {
			/*if(over==true){
				break;
			}*/
            Element tableName = tableNames.get(k);
            // 获取 TABLENAME
            //如果是sql节点，则跳过，因为SQL节点中不执行以下代码
            if (tableName.getName().equals("SQLS")) {
                continue;
            }
            String table_name = tableName.attributeValue("TBLNAME");
            //tableList.add(table_name);
            // 获取表名
            if (is_print) {
                if ("SYS_REPORT_CONDITION".equalsIgnoreCase(table_name)) {
                    Element rowdata = tableName.element("ROWDATA");
                    // 获取METADATA
                    List<Element> fieldList = rowdata.elements();
                    // 获取FIELD的集合
                    Element field = null;
                    for (int i = 0; i < fieldList.size(); i++) {
                        field = fieldList.get(i);
                        id = field.attributeValue("FOR_TABLE");
                        if (StringUtils.isEmpty(id)) {
                            id = field.attributeValue("for_table");
                        }
                        over = true;
                        break;
                    }
                }
            } else {
                if ("JIMU_SQLDICT".equalsIgnoreCase(table_name)) {
                    Element rowdata = tableName.element("ROWDATA");
                    // 获取METADATA
                    List<Element> fieldList = rowdata.elements();
                    // 获取FIELD的集合
                    Element field = null;
                    for (int i = 0; i < fieldList.size(); i++) {
                        field = fieldList.get(i);
                        id = field.attributeValue("ID");
                        if (StringUtils.isEmpty(id)) {
                            id = field.attributeValue("id");
                        }
                        over = true;
                        break;
                    }
                }
            }
            //增加导入控制
            if ("SYS_MENU".equals(table_name)) {
                Element rowdata = tableName.element("ROWDATA");
                // 获取METADATA
                List<Element> fieldList = rowdata.elements();
                // 获取FIELD的集合
                Element field = null;
                for (int i = 0; i < fieldList.size(); i++) {
                    field = fieldList.get(i);
                    String menuflag = "0";
                    if (StringUtils.isEmpty(menuflag)) {
                        throw new RuntimeException("导出的文件出错！");
                    } else {
                        if (!"0".equals(menuflag)) {
                            throw new RuntimeException("导入的文件不是当前系统报表！");
                        }
                    }
                    break;
                }
            }
            if ("JIMU_REPORT".equalsIgnoreCase(table_name)) {
                Element rowdata = tableName.element("ROWDATA");
                // 获取METADATA
                List<Element> fieldList = rowdata.elements();
                // 获取FIELD的集合
                Element field = null;
                for (int i = 0; i < fieldList.size(); i++) {
                    field = fieldList.get(i);
                    count1 = field.attributeValue("ID");
                    if (StringUtils.isEmpty(count1)) {
                        count1 = field.attributeValue("id");
                    }
                    over = true;
                    break;
                }
            }

            if ("JIMU_REPORT_DB".equalsIgnoreCase(table_name)) {
                Element rowdata = tableName.element("ROWDATA");
                // 获取METADATA
                List<Element> fieldList = rowdata.elements();
                // 获取FIELD的集合
                Element field = null;
                for (int i = 0; i < fieldList.size(); i++) {
                    field = fieldList.get(i);
                    countList.add(field.attributeValue("ID"));
                    //count2 = field.attributeValue("ID");
                    over = true;
                }
            }
        }
//		if (!tableList.contains("SYS_MENU")){
//			throw new RuntimeException("文件不支持导入！");
//		}
        String deletesql;
        if (StringUtils.isNotBlank(id)) {
            if (is_print) {
                /*删除T8_DEFAULT_PARAM表*/
                deletesql = "delete from T8_DEFAULT_PRINTPARAM where id = '" + id + "'";
                map.put("newSql", deletesql);
                jmReportDao.update(deletesql, map);
                /*删除SYS_REPORT_CONDITION表*/
                deletesql = "delete from SYS_REPORT_CONDITION where for_table = '" + id + "'";
                map.put("newSql", deletesql);
                jmReportDao.update(deletesql, map);
                /*删除T8_JMPRINT_SQL表*/
                deletesql = "delete from T8_JMPRINT_SQL where id = '" + id + "'";
                map.put("newSql", deletesql);
                jmReportDao.update(deletesql, map);
            } else {
                /*删除T8_DEFAULT_PARAM表*/
                deletesql = "delete from T8_DEFAULT_PARAM where id = '" + id + "'";
                map.put("newSql", deletesql);
                jmReportDao.update(deletesql, map);
                /*删除JIMU_SQLDICT表*/
                deletesql = "delete from JIMU_SQLDICT where id = '" + id + "'";
                map.put("newSql", deletesql);
                jmReportDao.update(deletesql, map);
            }
        }
        /*删除JIMU_REPORT表*/
        if (StringUtils.isNotBlank(count1)) {
            deletesql = "delete from JIMU_REPORT where id = '" + count1 + "'";
            map.put("newSql", deletesql);
            jmReportDao.update(deletesql, map);
            /*删除JIMU_REPORT_DB表*/
            deletesql = "delete from JIMU_REPORT_DB where JIMU_REPORT_ID = '" + count1 + "'";
            map.put("newSql", deletesql);
            jmReportDao.update(deletesql, map);
        }
        if (!CollectionUtils.isEmpty(countList)) {
            for (String s : countList) {
                count2 += "'" + s + "',";
            }
            count2 = count2.substring(0, count2.length() - 1);
            count2 = count2.concat(")");
            /*删除JIMU_REPORT_DB_FIELD表*/
            deletesql = "delete from JIMU_REPORT_DB_FIELD where JIMU_REPORT_DB_ID in " + count2;
            map.put("newSql", deletesql);
            jmReportDao.update(deletesql, map);
            /*删除JIMU_REPORT_DB_PARAM表*/
            deletesql = "delete from JIMU_REPORT_DB_PARAM where JIMU_REPORT_HEAD_ID in " + count2;
            map.put("newSql", deletesql);
            jmReportDao.update(deletesql, map);
        }
        List<String> list = new ArrayList<String>();
        //存放SYS_REPORT_CSS表生成的自增长ID集合
        for (int k = 0; k < tableNames.size(); k++) {
            String sql = "";
            // 导入数据的sql
            Element tableName = tableNames.get(k);
            // 获取 TABLENAME
            //如果是sql节点，则跳过，因为SQL节点中不执行以下代码
            if (tableName.getName().equals("SQLS")) {
                continue;
            }
            Element metadata = tableName.element("METADATA");
            // 获取METADATA
            Element fields = metadata.element("FIELDS");
            // 获取FIELDS
            String table_name = tableName.attributeValue("TBLNAME");
            // 获取FIELD的集合
            List<Element> fieldList = fields.elements();
            // 获取表名
            if (!"SYS_MENU".equalsIgnoreCase(table_name)) {
                if ("JIMU_REPORT".equals(table_name)) {
                    sql = "DECLARE\n" +
                            "  EXPSTR CLOB:=$S{json_str};\n" +
                            "BEGIN\n" +
                            "  INSERT INTO " + table_name.toLowerCase() + "(";
                    //sql = "insert into " + table_name.toLowerCase() + "(";
                    Element field = null;
                    // 由于CLOB字段如果过长的话，直接用insert语句插入会报错ORA-01704: string literal too long
                    // 此处直接用prepareStatement导入参数可以避免此类问题
                    String sqltmp = "";
                    for (int i = 0; i < fieldList.size(); i++) {
                        field = fieldList.get(i);
                        String attrname = field.attributeValue("attrname").toLowerCase();
                        String fieldtype = field.attributeValue("fieldtype").toLowerCase();
                        sql = sql + attrname + ",";
                        // 获取表的字段名称，并把字段名传入sql语句
                        if ("json_str".equals(attrname)) {
                            sqltmp += "EXPSTR,";
                        } else {
                            sqltmp = sqltmp + "$S{" + attrname + "},";
                        }
                    }
                    sql = sql.substring(0, sql.length() - 1);
                    // 处理sql的尾部逗号
                    sqltmp = sqltmp.substring(0, sqltmp.length() - 1);
                    // 处理sqltmp的尾部逗号
                    sql = sql + ") values (" + sqltmp + ");\n" +
                            "END;\n";
                } else {
                    sql = "insert into " + table_name.toLowerCase() + "(";
                    Element field = null;
                    // 由于CLOB字段如果过长的话，直接用insert语句插入会报错ORA-01704: string literal too long
                    // 此处直接用prepareStatement导入参数可以避免此类问题
                    String sqltmp = "";
                    for (int i = 0; i < fieldList.size(); i++) {
                        field = fieldList.get(i);
                        String attrname = field.attributeValue("attrname").toLowerCase();
                        String fieldtype = field.attributeValue("fieldtype").toLowerCase();
                        sql = sql + attrname + ",";
                        // 获取表的字段名称，并把字段名传入sql语句
                        sqltmp = sqltmp + "$S{" + attrname + "},";
                    }
                    sql = sql.substring(0, sql.length() - 1);
                    // 处理sql的尾部逗号
                    sqltmp = sqltmp.substring(0, sqltmp.length() - 1);
                    // 处理sqltmp的尾部逗号
                    sql = sql + ") values (" + sqltmp + ")";
                }
                Element rowdata = tableName.element("ROWDATA");
                // 获取ROWDATA
                List<Element> rows = rowdata.elements();
                // 获取ROW的集合
                //int n = 0;// 记录插入正确的条数
                //int m = 0;// 记录插入错误的条数
                for (int i = 0; i < rows.size(); i++) {
                    Map<String, Object> maps = new HashMap<>();
                    Element row = rows.get(i);
                    for (int j = 0; j < fieldList.size(); j++) {
                        // 按照字段顺序传入值
                        String attrname = fieldList.get(j).attributeValue(
                                "attrname");
                        String value = row.attributeValue(attrname);
                        if (value != null && !"".equals(value)) {
                            // 当字段的属性是CLOB时，此字段的数据做特殊处理
                            String str = fieldList.get(j).attributeValue("fieldtype");
                            if ("CLOB".equalsIgnoreCase(fieldList.get(j).attributeValue("fieldtype"))
                                    || "LONG".equals(fieldList.get(j).attributeValue("fieldtype"))) {
                                value = value.replace("&#013;&#010;", "\n");
                                // 回车
                                value = value.replace("&llt;", "<");
                                value = value.replace("&ggt;", ">");
                                value = value.replace("&aapos;", "'");// '
                                maps.put(attrname.toLowerCase(), value);
                            } else if ("TIMESTAMP(6)".equals(fieldList.get(j).attributeValue("fieldtype"))) {
							/*Timestamp timestamp=Timestamp.valueOf(value);
							maps.put(attrname.toLowerCase(), Timestamp.valueOf(value));*/
                                /*maps.put(attrname.toLowerCase(), "TO_TIMESTAMP('"+value+"', 'SYYYY-MM-DD HH24:MI:SS:FF6')");*/
                                maps.put(attrname.toLowerCase(), null);
                            } else if ("NUMBER".equals(fieldList.get(j).attributeValue("fieldtype"))) {
                                maps.put(attrname.toLowerCase(), Integer.parseInt(value));
                            } else {
                                maps.put(attrname.toLowerCase(), value);
                            }
                        } else {
                            maps.put(attrname.toLowerCase(), "");
                        }
                    }
                    maps.put("newSql", sql);
                    jmReportDao.update(sql, maps);
                }
            }
        }
    }

    /**
     * MultipartFile类型转File类型
     * 注意：如果只是操作这个文件，并非要保存这个文件，操作完成后需要调用SztFileUtil.deleteFile(File file)
     *
     * @param multipartFile 需要转换的文件
     * @return
     */
    public static File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        //获得输入流
        InputStream inputStream = multipartFile.getInputStream();
        //根据文件名创建文件
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        //将输入流写入到文件中
        inputStreamToFile(inputStream, file);
        return file;
    }

    /**
     * InputStream类型转换为File类型
     *
     * @param inputStream
     * @param file
     * @throws IOException
     */
    public static void inputStreamToFile(InputStream inputStream, File file) throws IOException {
        //拿到输出流，往里面写数据
        OutputStream os = new FileOutputStream(file);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        inputStream.close();
    }

    /**
     * 删除文件
     *
     * @param file 需要删除的文件
     */
    public static void deleteFile(File file) {
        //用URI创建对象
        File deleteFile = new File(file.toURI());
        //执行删除
        deleteFile.delete();
    }
}
