package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ReportExportDao extends ComnDao {

    @Value("${xml.database}")
    private String database;

    public String createApplicationConfigXML(String[] tableNames, String menuid, String rootPath) throws Exception {
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        // 建立document对象
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("DATAPACKET");// 添加文档根
        root.addAttribute("Version", "2.0");
        boolean menuidIsNotNull = !"".equals(menuid) && menuid != null && !"null".equals(menuid);
        for (String name : tableNames) {
            String tableName = name;//获得表名
            // 根据表名查出表的字段名及属性
            String sql = "select table_name,column_name,data_type,character_maximum_length data_length,table_schema from information_schema.columns where table_schema =(select distinct t.table_schema from information_schema.columns t where t.table_schema = '" + database + "') and table_name = '"
                    + tableName + "'";
            List<SqlRow> rs = super.findRows(sql);
            Element tablename = root.addElement("TABLENAME"); // 添加root的子节点
            tablename.addAttribute("TBLNAME", tableName);

            List<Map<String, String>> tableFields = new ArrayList<Map<String, String>>();//存储表中所有的字段及属性
            for (SqlRow r : rs) {
                Map<String, String> tableField = new HashMap<String, String>();//存储表中单个的字段及属性
                tableField.put("table_name", r.getString("TABLE_NAME"));//表名
                tableField.put("column_name", r.getString("COLUMN_NAME"));//字段名
                tableField.put("data_type", r.getString("DATA_TYPE"));//字段类型
                tableField.put("data_length", r.getString("data_length"));//字段长度
                tableFields.add(tableField);
            }

            Element metaData = tablename.addElement("METADATA"); // 添加tablename的子节点 METADATA
            Element rowData = tablename.addElement("ROWDATA"); // 添加tablename的子节点 ROWDATA

            Element fields = metaData.addElement("FIELDS"); // 添加metaData的子节点FIELDS

            StringBuilder allFields = new StringBuilder();
            // 将表中的字段全部解析到fields节点的子节点field中
            for (Map<String, String> tableField : tableFields) {
                Element field = fields.addElement("FIELD"); // 添加fields的子节点FIELD
                field.addAttribute("attrname", tableField.get("column_name"));//字段名
                field.addAttribute("fieldtype", tableField.get("data_type"));//字段类型
                field.addAttribute("WIDTH", StringUtils.isBlank(tableField.get("data_length")) ? "0" : tableField.get("data_length"));//字段长度
                allFields.append(tableName).append(".").append(tableField.get("column_name")).append(",");
            }
            allFields = new StringBuilder(allFields.substring(0, allFields.length() - 1));//处理sql的尾部逗号


            //根据表名查询出表中的所有数据
            String sql1 = "select " + allFields + " from " + tableName;

            //如果是单张报表导出，sys_report_xml_css、sys_report_xml、sys_report_xml_sql、sys_report_condition则根据menuid导出
            if (menuidIsNotNull && ("sys_report_xml_css".equalsIgnoreCase(tableName) || "sys_report_xml".equalsIgnoreCase(tableName) ||
                    "sys_report_xml_sql".equalsIgnoreCase(tableName) || "sys_report_condition".equalsIgnoreCase(tableName))) {
                sql1 = sql1 + " where for_table = '" + menuid + "'";
            }
            //如果是单张报表导出，sys_report_css
            if (menuidIsNotNull && "sys_report_css".equalsIgnoreCase(tableName)) {
                sql1 = sql1 + " right join sys_report_xml_css on sys_report_css.id=sys_report_xml_css.sys_report_css_id where sys_report_xml_css.for_table = '" + menuid + "'";
            }
            //如果是单张报表导出，sys_report_sql
            if (menuidIsNotNull && "sys_report_sql".equalsIgnoreCase(tableName)) {
                sql1 = sql1 + " right join sys_report_xml_sql on sys_report_xml_sql.exeid=sys_report_sql.exeid where sys_report_xml_sql.for_table = '" + menuid + "'";
            }

            sql1 = sql1 + " order by id";

            List<SqlRow> rs1 = super.findRows(sql1);
            for (SqlRow r1 : rs1) {

                Element row = rowData.addElement("ROW"); // 添加rowData的子节点ROW

                //添加子节点的属性
                for (Map<String, String> map : tableFields) {
                    String s = r1.getString(map.get("column_name"));
                    if (!"".equals(s) && s != null) {
                        //当字段的属性是CLOB时，此字段的数据做特殊处理
                        if (map.get("data_type") != null && ("CLOB".equalsIgnoreCase(map.get("data_type")) || "longtext".equalsIgnoreCase(map.get("data_type")))) {
                            s = s.replace("\n", "&#013;&#010;");//回车
                            s = s.replace("<", "&llt;");//标准xml中，<的转义字符应该是&lt;,但是此处转成&llt;，是为了区别CLOB中本来就有的&lt;字段
                            s = s.replace(">", "&ggt;");//标准xml中，>的转义字符应该是&gt;,但是此处转成&ggt;，是为了区别CLOB中本来就有的&gt;字段
                            s = s.replace("'", "&aapos;");//标准xml中，'的转义字符应该是&apos;,但是此处转成&aapos;，是为了区别CLOB中本来就有的&apos;字段
                        }
                        row.addAttribute(map.get("column_name"), s);//将字段的数据添加到Row列
                    }
                }
            }

        }
        //Map加入菜单id
        params.put("menuid", menuid);
        //新增SQL节点
        root = add_SQL_Element(root, params);
        String chartset = "UTF-8";//Global.getGlobalConf("Global.exportReportCharset");
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(chartset);// 根据需要设置编码
        // 输出全部原始数据，并用它生成新的我们需要的XML文件
        File filePath = new File(rootPath + "");
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        File file = null;
        if (menuidIsNotNull) {//如果是单表导出 文件名为菜单ID 文件名自动加上日期时间
            List<SqlRow> rs2 = super.findRows("select menuname from sys_menu where menuid = $S{menuid}", menuid);
            String chinName = "";
            for (SqlRow r2 : rs2) {
                chinName = r2.getString("menuname");
            }
            file = new File(rootPath , chinName + "(" + menuid + ")_" + sdf.format(new Date()) + ".xml");
        } else {
            file = new File(rootPath , "demo" + "_" + sdf.format(new Date()) + ".xml");
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            Writer xmlwriter = new OutputStreamWriter(new FileOutputStream(file), chartset);
            XMLWriter writer2 = new XMLWriter(xmlwriter, format);//想要导出什么名字的xml，自己改
            writer2.write(document); // 输出到文件
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getName();

    }

    //新增SQL节点
    private Element add_SQL_Element(Element root, Map<String, Object> map) throws Exception {
        Element sqls = root.addElement("SQLS"); // 添加root的子节点
        //存放数据库中取出来的额SQL
        String sql_str = "";
        if (map.get("menuid") != null && !"".equals(map.get("menuid")) && !"null".equals(map.get("menuid"))) {//导出单个报表
            String forTable = map.get("menuid").toString();
            List<SqlRow> srs = super.findRows("select t.init_sql from sys_report_condition  t where t.for_table= $S{forTable}", forTable);
            //填充新增菜单sql
            for (SqlRow sr : srs) {
                sql_str = sr.getString("init_sql");
            }
            if (sql_str == null || sql_str.equals("")) {
                StringBuilder role_sql_str = new StringBuilder("begin delete from sys_role_right s where s.menuid='" + map.get("menuid").toString() + "' and s.roleid='*';");
                role_sql_str.append("delete from sys_menu m where m.menuid='").append(map.get("menuid").toString()).append("';");
                //插入角色权限表
                role_sql_str.append(" INSERT INTO sys_role_right(roleid,moduleid,menuid,buttonid,exeid,buttondesc,upperid,rtype,functype,remark) VALUES (");
                //插入菜单SQL语句
                StringBuilder insert_sql_str = new StringBuilder("insert into sys_menu(MODULEID,MENUID,MENUNAME,UPPERID,URL,ICONCLS,ICON,LOADORDER,STATUS,PAGEID,FASTCODE,FUNCTYPE,REMARK,REPORTURL,MENUTYPE) values (");
                //根据菜单id查询出该菜单的信息
                List<SqlRow> ssr = super.findRows("select * from sys_menu m where m.menuid=$S{forTable}", forTable);
                //填充新增菜单sql
                for (SqlRow sr : ssr) {
                    role_sql_str.append("'*',");
                    role_sql_str.append("'").append(sr.getString("moduleid")).append("',");
                    role_sql_str.append("'").append(sr.getString("menuid")).append("',");
                    role_sql_str.append("'").append(sr.getString("menuid")).append("',");
                    role_sql_str.append("null,");
                    role_sql_str.append("'").append(sr.getString("menuname")).append("',");
                    role_sql_str.append("'").append(sr.getString("upperid")).append("',");
                    role_sql_str.append("'0',");
                    role_sql_str.append("'',");
                    role_sql_str.append("'');");
                    insert_sql_str.append("'").append(sr.getString("moduleid")).append("',");
                    insert_sql_str.append("'").append(sr.getString("menuid")).append("',");
                    insert_sql_str.append("'").append(sr.getString("menuname")).append("',");
                    insert_sql_str.append("'").append(sr.getString("upperid")).append("',");
                    insert_sql_str.append("'").append(sr.getString("url")).append("',");
                    insert_sql_str.append("'").append(sr.getString("iconcls")).append("',");
                    insert_sql_str.append("'").append(sr.getString("icon")).append("',");
                    insert_sql_str.append("'").append(sr.getString("loadorder")).append("',");
                    insert_sql_str.append("'").append(sr.getString("status")).append("',");
                    insert_sql_str.append("'").append(sr.getString("pageid")).append("',");
                    insert_sql_str.append("'").append(sr.getString("fastcode")).append("',");
                    insert_sql_str.append("'").append(sr.getString("functype")).append("',");
                    insert_sql_str.append("'").append(sr.getString("remark")).append("',");
                    insert_sql_str.append("'").append(sr.getString("reporturl")).append("',");
                    insert_sql_str.append("'").append(sr.getString("menutype")).append("'); end;");
                }
                //sql=权限角色+菜单（用分号分隔）
                String sql_strs = role_sql_str + insert_sql_str.toString();
                //设置节点text
                sqls.setText(sql_strs);
            } else {
                //设置节点text
                sqls.setText(sql_str);
            }
        } else {//导出全部报表
            List<SqlRow> allQuerys = super.findRows("select t.init_sql, t1.*  from sys_report_condition t join sys_menu t1 on t.for_table = t1.menuid");
            StringBuilder stringBuilder = new StringBuilder("begin ");
            for (SqlRow allQuery : allQuerys) {
                String init_sql = allQuery.getString("init_sql");
                if (StringUtils.isNotEmpty(init_sql)) {
                    stringBuilder.append(init_sql);
                }
                String menuid = allQuery.getString("menuid");
                stringBuilder.append("delete from sys_role_right s where s.menuid='").append(menuid).append("' and s.roleid='*';");
                stringBuilder.append("delete from sys_menu m where m.menuid='").append(menuid).append("';");
                stringBuilder.append("INSERT INTO sys_role_right(roleid,moduleid,menuid,buttonid,exeid,buttondesc,upperid,rtype,functype,remark) VALUES (");
                stringBuilder.append("'*',");
                stringBuilder.append("'").append(allQuery.getString("moduleid")).append("',");
                stringBuilder.append("'").append(allQuery.getString("menuid")).append("',");
                stringBuilder.append("'").append(allQuery.getString("menuid")).append("',");
                stringBuilder.append("null,");
                stringBuilder.append("'").append(allQuery.getString("menuname")).append("',");
                stringBuilder.append("'").append(allQuery.getString("upperid")).append("',");
                stringBuilder.append("'0',");
                stringBuilder.append("'',");
                stringBuilder.append("'');");
                stringBuilder.append("insert into sys_menu(MODULEID,MENUID,MENUNAME,UPPERID,URL,ICONCLS,ICON,LOADORDER,STATUS,PAGEID,FASTCODE,FUNCTYPE,REMARK,MENUTYPE) values (");
                stringBuilder.append("'").append(allQuery.getString("moduleid")).append("',");
                stringBuilder.append("'").append(allQuery.getString("menuid")).append("',");
                stringBuilder.append("'").append(allQuery.getString("menuname")).append("',");
                stringBuilder.append("'").append(allQuery.getString("upperid")).append("',");
                stringBuilder.append("'").append(allQuery.getString("url")).append("',");
                stringBuilder.append("'").append(allQuery.getString("iconcls")).append("',");
                stringBuilder.append("'").append(allQuery.getString("icon")).append("',");
                stringBuilder.append("'").append(allQuery.getString("loadorder")).append("',");
                stringBuilder.append("'").append(allQuery.getString("status")).append("',");
                stringBuilder.append("'").append(allQuery.getString("pageid")).append("',");
                stringBuilder.append("'").append(allQuery.getString("fastcode")).append("',");
                stringBuilder.append("'").append(allQuery.getString("functype")).append("',");
                stringBuilder.append("'").append(allQuery.getString("remark")).append("',");
                stringBuilder.append("'").append(allQuery.getString("menutype")).append("');");
            }
            stringBuilder.append(" end;");
            sqls.setText(stringBuilder.toString());
        }
        return root;
    }
}
