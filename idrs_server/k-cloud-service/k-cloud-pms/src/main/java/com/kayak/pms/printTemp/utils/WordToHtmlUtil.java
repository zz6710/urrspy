package com.kayak.pms.printTemp.utils;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @program: k-cloud
 * @description: Word文档转换Html工具类
 * @author: WangZhenXin
 * @create: 2020-12-28 09:53
 * @memo 备注信息
 */
public class WordToHtmlUtil {
    private static final Logger logger = LoggerFactory.getLogger(WordToHtmlUtil.class);

    /**
     * word 转html
     * @param inputStream 文件流
     * @param docVersion word版本
     * @return
     * @throws Exception
     */
    public static String wordToHtml(InputStream inputStream, String docVersion) throws Exception{
        HWPFDocument wordDocument = new HWPFDocument(new POIFSFileSystem(inputStream));
        return docToHtml(inputStream,wordDocument);
    }
    /**
     * word 转html
     * @param inputStream 文件流
     * @return
     * @throws Exception
     */
    public static String wordToHtml(InputStream inputStream) throws Exception{
        HWPFDocument wordDocument = new HWPFDocument(new POIFSFileSystem(inputStream));
        return docToHtml(inputStream,wordDocument);
    }


    public static String docToHtml(InputStream inputStream,HWPFDocument wordDocument) throws Exception{
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType,
                                      String suggestedName, float widthInches, float heightInches) {
                return "test/" + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        org.apache.commons.io.output.ByteArrayOutputStream out = new org.apache.commons.io.output.ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        String  html = new String(out.toString("UTF-8"));
        return html;
    }

    /**
     * 写入文件内容
     * @param content   文件内容
     * @param path  文件路径
     */
    public static void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if(!file.canWrite()){
                logger.info("文件没有写入权限===="+path);
            }
            if(!file.canRead()){
                logger.info("文件没有读取权限===="+path);
            }
            /*if(file.exists()){
                log.info("文件没有读取权限===="+path);
                file.delete();
            }
            if(!file.exists()){
                log.info("文件没有创建权限===="+path);
                file.createNewFile();
            }*/
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
            bw.write(content);
        } catch (FileNotFoundException fnfe) {
            logger.error(fnfe.getMessage(), fnfe);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        } finally {
            try {
                if (bw != null) {
                    bw.flush();
                    bw.close();
                }
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException ie) {
            }
        }
    }

    /**
     * 清空文件内容
     * @param path
     */
    public static void clearInfoForFile(String path) {
        File file =new File(path);
        FileWriter fileWriter = null;
        try {
            if(file.exists()) {
                fileWriter =new FileWriter(file);
                fileWriter.write("");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 单条数据的替换
     * @param temp_html
     * @param params
     * @return
     */
    public static String FillToWordData(String temp_html, Map<String,Object> params){
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String keyName = entry.getKey();
            if(entry.getValue() == null){
                temp_html = temp_html.replace("${" + keyName + "}", "-");
            } else {
                String val=entry.getValue().toString();
                temp_html = temp_html.replace("${" + keyName + "}", val);
            }
        }
        return temp_html;
    }

    /**
     * 动态增加打印模板的表格行
     * @param temp_html     wordhtml
     * @param groupName     分组名
     * @param listData      数据源大小
     * @return
     * @throws Exception
     */
    public static String changeWordHtmlInfo(String temp_html,String groupName,List<Map<String,Object>> listData) throws Exception{
        org.jsoup.nodes.Document document = Jsoup.parse(temp_html);
        Elements spanelements =document.getElementsByTag("span");
        int i =0;
        for (Element element:spanelements){
            String keyValue = element.text();
            if(keyValue.contains("${")&&keyValue.contains(".")){
                keyValue = keyValue.replace("${","").replace("}","");
                keyValue = keyValue.substring(0, keyValue.indexOf("."));
                if(groupName.equals(keyValue)&&listData.size()>0 && i==0){
                    i=1;
                    String parent_html = element.parent().parent().parent().html();
                    for(Map<String,Object> map:listData){
                        String group_html = parent_html;
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if(entry.getValue() != null){
                                group_html = group_html.replace("${" +groupName+"."+entry.getKey() + "}", entry.getValue().toString());
                            }else{
                                group_html = group_html.replace("${" +groupName+"."+entry.getKey() + "}", "-");
                            }
                        }
                        element.parent().parent().parent().before(group_html);
                    }
                    element.parent().parent().parent().remove();
                } else if(groupName.equals(keyValue) && listData.size() == 0 && i==0){
                    i=1;
                    element.parent().parent().parent().remove();
                }
            }
        }
        return document.toString();
    }


}
