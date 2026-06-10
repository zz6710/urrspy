package com.kayak.dps.ods.util;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.dao.DealValuePortDao;
import com.kayak.dps.ods.exception.DbfFileReadException;
import com.kayak.dps.pub.ICallback;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(XmlFileUtil.class);

    /**
     * 读取xml 文件
     * @param filePath       文件路径
     * @param fileName     文件名
     * @param str 节点信息 英文逗号分割
     * @return  “”
     */
    public static List<Map<String, String>>  read(String filePath,String fileName, String str) throws IOException {

        // 切割节点信息
        String [] elementNames =  str.split(",");

        // 0. 获取目录下所有符合条件的文件
        List<File> xmlFiles = PublicFileUtil.getFilesWithPrefixAndExtension(filePath,fileName,".xml");
        List<Map<String, String>> resList =  new ArrayList<>();

        //1.将目标xml 指定节点解析成 List<Map> 数据
            for(File file : xmlFiles){
                convertXmlToMap(file,elementNames,resList);
            }
             return  resList;
    }


    public static void main(String[] args) {


        try {
            List<Map<String, String>> resList =  read("\\data\\NY\\","user","list");
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        try {
////            List<Object[]>   resList =    read("\\data\\NY\\user.xml",true);
////            for (Object[] ob : resList) {
////                System.out.println(ob.toString());
////            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        String filePath = "\\data\\NY\\user.xml";
//        Map<String, String> xmlData = convertXmlToMap(filePath);
//
//        for (Map.Entry<String, String> entry : xmlData.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }

    }

    /**
     * 读取xml 文件
     * @param filePath          文件路径(带文件名)
     * @param handle  数据库实体
     * @param rs  字段映射数据
     * @param elementNameStr  xml节点信息
     * @param port_table
     * @return 返回读取数据总数
     */
    public static int readMore(String filePathAndName, ICallback handle, List<SqlRow> rs, String elementNameStr, String skipNoFile, String port_table) throws Exception {

            // 1.数据装备
                //① 分离文件名和文件路径
                String [] filePathStr=filePathAndName.split("/");
                //获取文件名
                String fileName=filePathStr[filePathStr.length-1];
                //获取无后缀的文件名
                String startName = fileName.replaceAll(".xml","");
                //获取文件路径
                String filePath = filePathAndName.replaceAll(fileName,"");
                // ②.切割节点信息
                String [] elementNames =  elementNameStr.split(",");

            // 2. 获取目录下所有符合条件的文件
            List<File> xmlFiles = PublicFileUtil.getFilesWithPrefixAndExtension(filePath,startName,".xml",port_table);
            if (xmlFiles.size() == 0) {
                if("1".equals(skipNoFile)){
                    logger.info(" >>>>> 服务器文件不存在,跳过执行: " + filePath + fileName);
                    return -2 ;
                }
                throw new Exception("未获取到合法文件:" + filePath);
            }
            //3.将目标xml 指定节点解析成 List<Map> 数据
            List<Map<String, String>> resList = new ArrayList<>();
            for(File file : xmlFiles){
                convertXmlToMap(file,elementNames,resList);
            }
            //4.开始填充 sql数据

            // 文件体
            List<Object[]> values = new ArrayList<>();
            // 数据量
            int rowNum = 0;
            // 遍历行数据
            for (Map<String, String> map : resList) {
                // 行数据
                List<Object> row = new ArrayList<>();
                // 遍历当前行的列数据
                for (SqlRow field : rs) {
                    String tableFieldName = field.getString("file_field_code");
                    if ("deal_date".equals(tableFieldName) || "DEAL_DATE".equals(tableFieldName)) {
                        continue;
                        //跳过特殊字段
                    }
                    row.add(map.get(tableFieldName));
                }
                values.add(row.toArray());
                // 每1万行数据 进行一次入库
                if (rowNum % 10000 == 0) {
                    handle.call(values);
                    values.clear();
                }
                rowNum++;
            }
            //全部读取完毕 写入数据库
            if (values.size() > 0) {
                handle.call(values);
                values.clear();
            }
            return rowNum;
        }


    /**
     *@param @param id
     * @param fileState
     * @param message
     * @param totalNum
     * @param dealValuePortDao
     *@return void
     *@date 2023/7/11  10:35
     *@description  记录日志
     *
     */
    public static Map<String,Map<String,Object>>  getXmlField(String table, DealValuePortDao dealValuePortDao) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("table", table);
        //查询映射表
        List<SqlRow> list = dealValuePortDao.getXmlDateList(params);
        //行转列
        Map<String,Map<String,Object>> map  = new HashMap<>();
        for (SqlRow sqlRow: list){
            String key = sqlRow.getString("data_table_field");
                if(!map.containsKey(key)){
                    map.put(key,sqlRow);
                }
        }
        return map ;
    }



    /**
    *@param @param id
     * @param fileState
     * @param message
     * @param totalNum
     * @param dealValuePortDao
    *@return void
    *@date 2023/7/11  10:35
    *@description  记录日志
    *
    */
    public static void updateFileLog(String id, String fileState, String message, Integer totalNum, DealValuePortDao dealValuePortDao) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("message", message);
        params.put("fileState", fileState);
        params.put("totalNum", totalNum);
        params.put("deal_user_id", SysUtil.getSysUserParamValue("sys_user_userid"));
        dealValuePortDao.updateFileLog(params);
    }













//
//    /**
//     *  读取xml文件并解析成集合
//     * @param filePath          文件路径(含文件名)
//     * @param printFileContent 是否打印文件体
//     * @return  “”
//     */
//    public static  List<Map<String,Object>> analysisXmlFileToList(String filePath, boolean printFileContent) throws IOException {
//
//        // 返回文件
//        List<Map<String,Object>> xmlDataList = new ArrayList<>();
//
//        // 0. 获取目录下所有符合条件的文件
//        List<File> xmlFiles = PublicFileUtil.getFilesWithPrefixAndExtension(filePath,"",".xml");
//
//        // 解析所有文件
//        for(File file : xmlFiles){
//
//            //1.创建解析器对象
//            SAXReader saxReader = new SAXReader();
//
//            try {
//                //2. 读取文件
//                Document document = saxReader.read(filePath);
//                //3.1 获取XML文件的根节点
//                Element rootElement = document.getRootElement();
//
//                List<Element> dataElementList = rootElement.elements("list");
//
//                List<Element> dataElementVDKLL = dataElementList.get(0).elements();
//
//                // 文件体
//                List<Object[]> values = new ArrayList<>();
//                // 行数据
//                List<Object> row = new ArrayList<>();
//                for (Element element : dataElementVDKLL) {
//                    List<Element> dataElementListFiled  =    element.elements();
//                    for (Element filed : dataElementListFiled) {
//                        row.add(filed.getData());
//                    }
//                    values.add(row.toArray());
//                    if(printFileContent){
//                        logger.info("文件体: {}", (Object)row);
//                    }
//                }
//                xmlDataList.add(values) ;
//            } catch (DocumentException e) {
//                logger.error(" 文件读取失败: {}", e);
//                throw new DbfFileReadException("文件读取失败: " + e.getMessage());
//            }
//        }
//        return  xmlDataList;
//    }







    /**
    *@param @param file
     * @param elementsName 节点信息
     * @param resList 返回结果
    *@return Map<String,String>
    *@date 2023/8/4  11:35
    *@description  xml文件转map
    *
    */
    public static void convertXmlToMap(File file,String [] elementNames,List<Map<String, String>> resList) {

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element child = document.getRootElement();
             //遍历获取 最内部的dom树
            for (int i = 0; i < elementNames.length; i++) {
                child = (Element)child.elements(elementNames[i]).get(0);
            }
            List<Element> xmlDataList = child.elements();
            // 遍历解析转换 最内部的dom树
            for(Element xmlData:xmlDataList ){
                Map<String, String> resultMap =  new HashMap<>();
                parseElement(xmlData, resultMap);
                resList.add(resultMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void parseElement(Element element, Map<String, String> resultMap) {
        List<Element> children = element.elements();
        if (children.isEmpty()) {
            resultMap.put(element.getName(), element.getText());
        } else {
            for (Element child : children) {
                parseElement(child, resultMap);
            }
        }
    }

}
