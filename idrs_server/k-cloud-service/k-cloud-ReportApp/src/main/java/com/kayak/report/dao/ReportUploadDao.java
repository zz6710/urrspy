package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.report.util.UnicodeInputStream;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportUploadDao  extends ComnDao {

    public void insertXMLInfo(InputStream in,String Encode)
            throws Exception {
        SAXReader reader = new SAXReader();
        Document document = null;

        try {
            UnicodeInputStream uin = new UnicodeInputStream(in,Encode);
            Encode = uin.getEncoding();//干掉bom头
            Reader read = new InputStreamReader(uin,Encode);
            document = reader.read(read);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Element root = document != null ? document.getRootElement() : null; // 获取根元素 DATAPACKET

        List<Element> tableNames = root.elements();// 获取元素下面的elements集合

        String for_table = "";
        boolean over = false;
        //找出SQL节点，并取相应内容
        for (Element e_note : tableNames) {
            //得到第k个节点
            //获取节点名
            String e_name = e_note.getName();
            //如果是SQL节点，通常都会执行SQL语句
            if (e_name.equals("SQLS")) {
                String sql_text = e_note.getText();
                if (!sql_text.equals("")) {
                    try {
                        sql_text = sql_text.replace("begin ", "");
                        sql_text = sql_text.replace("end;", "");
                        String[] sqlArr = sql_text.split(";");
                        for (int i = 0; i < sqlArr.length; i++) {
                            if (sqlArr[i].trim().length() > 0) {
                                int finalI = i;
                                doTrans(() -> {
                                    super.update(sqlArr[finalI]);
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("SQL语句有误：" + sql_text);
                    }

                }
            }
        }
        //获取for_table
        for (Element name : tableNames) {
            if (over) {
                break;
            }
            //如果是sql节点，则跳过，因为SQL节点中不执行以下代码
            if (name.getName().equals("SQLS")) {
                continue;
            }
            String table_name = name.attributeValue("TBLNAME");// 获取表名
            if ("SYS_REPORT_XML".equalsIgnoreCase(table_name)) {
                Element rowdata = name.element("ROWDATA"); // 获取METADATA
                List<Element> fieldList = rowdata.elements(); // 获取FIELD的集合
                Element field = null;
                for (Element element : fieldList) {
                    field = element;
                    for_table = field.attributeValue("FOR_TABLE").toLowerCase();
                    over = true;
                }
            }
        }
        if("".equals(for_table)){
            throw new Exception("SYS_REPORT_XML中查询到的for_table为空");
        }
        String forTable = for_table;
        doTrans(()->{
            //删除sys_REPORT_XML_CSS和SYS_REPORT_CSS
            String sys_report_xml_css_sql_select = "select sys_report_css_id from sys_report_xml_css where lower(for_table) = lower($S{forTable})";
            List<SqlRow> rs = super.findRows(sys_report_xml_css_sql_select, forTable);
            List<String> sys_report_css_sql_updates = new ArrayList<>();
            for (SqlRow r : rs) {
                String id =  r.getString("sys_report_css_id");
                String sys_report_css_sql_delete = "delete from sys_report_css where id = '"+id+"'";
                sys_report_css_sql_updates.add(sys_report_css_sql_delete);
            }
            String sys_report_xml_css_sql_delete = "delete from sys_report_xml_css where lower(for_table) = lower($S{forTable})";
            super.update(sys_report_xml_css_sql_delete, forTable);
            for (String sys_report_css_sql_update : sys_report_css_sql_updates) {
                super.update(sys_report_css_sql_update);
            }

            //删除sys_REPORT_XML_SQL和SYS_REPORT_SQL
            String sys_report_xml_sql_sql_select = "select exeid from sys_report_xml_sql where lower(for_table) = lower($S{forTable})";
            List<SqlRow> rs1 = super.findRows(sys_report_xml_sql_sql_select, forTable);
            for (SqlRow r : rs1) {
                String exeid =  r.getString("exeid");
                String sys_report_sql_sql_delete = "delete from sys_report_sql where lower(exeid) = lower($S{exeid})";
                super.update(sys_report_sql_sql_delete,exeid);
            }
            String sys_report_xml_sql_sql_delete = "delete from sys_report_xml_sql where lower(for_table) = lower($S{forTable})";
            super.update(sys_report_xml_sql_sql_delete, forTable);

            //删除sys_report_condition和SYS_REPORT_XML
            String sys_report_condition_sql_delete = "delete from sys_report_condition where lower(for_table) = lower($S{forTable})";
            super.update(sys_report_condition_sql_delete, forTable);
            String sys_report_xml_sql_delete = "delete from sys_report_xml where lower(for_table) = lower($S{forTable})";
            super.update(sys_report_xml_sql_delete, forTable);
        });


        List<String> list = new ArrayList<>();//存放SYS_REPORT_CSS表生成的自增长ID集合

        for (Element name : tableNames) {
            StringBuilder sql = new StringBuilder();// 导入数据的sql
            //如果是sql节点，则跳过，因为SQL节点中不执行以下代码
            if (name.getName().equals("SQLS")) {
                continue;
            }
            String table_name = name.attributeValue("TBLNAME");// 获取表名
            sql = new StringBuilder("insert into " + table_name.toLowerCase() + "(");

            Element metadata = name.element("METADATA"); // 获取METADATA
            Element fields = metadata.element("FIELDS"); // 获取FIELDS
            List<Element> fieldList = fields.elements(); // 获取FIELD的集合

            Element field = null;
            // 由于CLOB字段如果过长的话，直接用insert语句插入会报错ORA-01704: string literal too long
            // 此处直接用prepareStatement导入参数可以避免此类问题
            StringBuilder sqltmp = new StringBuilder();
            for (Element element : fieldList) {
                field = element;
                String attrname = field.attributeValue("attrname").toLowerCase();
                String fieldtype = field.attributeValue("fieldtype").toLowerCase();
                sql.append(attrname).append(",");// 获取表的字段名称，并把字段名传入sql语句
                if ("id".equalsIgnoreCase(attrname)) {//当属性是id时，在insert语句中设置成自增
                    sqltmp.append("$AUTOIDI{").append(table_name.toLowerCase()).append("_id},");
                    continue;
                }
                if ("NUMBER".equalsIgnoreCase(fieldtype) || "Integer".equals(fieldtype)) {
                    sqltmp.append("$I{").append(attrname).append("},");
                } else {
                    sqltmp.append("$S{").append(attrname).append("},");
                }
            }
            sql = new StringBuilder(sql.substring(0, sql.length() - 1));// 处理sql的尾部逗号
            sqltmp = new StringBuilder(sqltmp.substring(0, sqltmp.length() - 1));// 处理sqltmp的尾部逗号
            sql.append(") values (").append(sqltmp).append(")");

            Element rowdata = name.element("ROWDATA"); // 获取ROWDATA
            List<Element> rows = rowdata.elements(); // 获取ROW的集合
            //int n = 0;// 记录插入正确的条数
            //int m = 0;// 记录插入错误的条数
            for (int i = 0; i < rows.size(); i++) {

                Map<String, Object> maps = new HashMap<String, Object>();

                Element row = rows.get(i);
                for (Element element : fieldList) {// 按照字段顺序传入值
                    String attrname = element.attributeValue(
                            "attrname");
                    String value = row.attributeValue(attrname);

                    if ("SYS_REPORT_CSS_ID".equalsIgnoreCase(attrname)) {//当属性是SYS_REPORT_XML_CSS表的SYS_REPORT_CSS_ID字段时，去list集合的数据
                        maps.put(attrname.toLowerCase(), list.get(i));
                        continue;
                    }

                    if (value != null && !"".equals(value)) {
                        // 当字段的属性是CLOB时，此字段的数据做特殊处理
                        if ("CLOB".equalsIgnoreCase(element
                                .attributeValue("fieldtype")) || "longtext".equalsIgnoreCase(element.attributeValue("fieldtype"))) {
                            value = value.replace("&#013;&#010;", "\n");// 回车
                            value = value.replace("&llt;", "<");
                            value = value.replace("&ggt;", ">");
                            value = value.replace("&aapos;", "'");// '
                            // 的ascii是chr(39)
                        }

                        maps.put(attrname.toLowerCase(), value);
                    } else {
                        maps.put(attrname.toLowerCase(), "");
                    }
                }

                if ("SYS_REPORT_SQL".equalsIgnoreCase(table_name)) {//SYS_REPORT_SQL表插入数据前，判断exeid在表中是否存在
                    String exeid = (String) maps.get("exeid");
                    if (exeid != null) {
                        List<SqlRow> sqlRows = super.findRows("select count(*) count_number from sys_report_sql where exeid = $S{exeid}", exeid);
                        int number = 0;
                        for (SqlRow sqlRow : sqlRows) {
                            number = sqlRow.getInteger("count_number");
                        }
                        if (number > 0) {
                            continue;
                        }
                    }
                }
                String finalSql = sql.toString();
                doTrans(()->{
                    super.update(finalSql,maps);
                });
                if ("SYS_REPORT_CSS".equalsIgnoreCase(table_name)) {//将SYS_REPORT_CSS表自增长的ID值放入集合中
                    list.add((String) maps.get(table_name.toLowerCase() + "_id"));
                }
            }
        }
    }
}
