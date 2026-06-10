package com.kayak.pms.printTemp.utils;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

/**
 * @program: k-cloud
 * @description: 文档差异化比较工具
 * @author: WangZhenXin
 * @create: 2020-12-30 10:18
 * @memo 备注信息
 */

public class WordCompareUtil {
    private static final Logger logger = LoggerFactory.getLogger(WordCompareUtil.class);



    /**
     * 根据文件路径进行差异化比对
     * @param docPath1 比对文件1
     * @param docPath2 比对文件2
     *@param fileStorePath 差异文件保存路径
     */
    public static void compareWordByPath(String docPath1, String docPath2, String fileStorePath) {
        //创建Document实例，加载第一个Word示例文档
        Document doc1 = new Document();
        doc1.loadFromFile(docPath1);
        //创建Document实例，加载第二个Word示例文档
        Document doc2 = new Document();
        doc2.loadFromFile(docPath2);
        //比较两个示例文档的内容差异
        doc1.compare(doc2, "admin");
        generateCompareFile(doc1, fileStorePath);
    }

    /**
     * 根据文件流比对文件
     * @param file1 文件1
     * @param file2 文件2
     * @param fileStorePath 差异文件保存路径
     */
    public static void compareWordByInputStream(InputStream file1,InputStream file2, String fileStorePath){
        //创建Document实例，加载第一个Word示例文档
        Document doc1 = new Document();
        doc1.loadFromStream(file1,FileFormat.Html);
        //创建Document实例，加载第二个Word示例文档
        Document doc2 = new Document();
        doc2.loadFromStream(file2,FileFormat.Html);
        //比较两个示例文档的内容差异
        doc1.compare(doc2, "admin");
        generateCompareFile(doc1, fileStorePath);
    }

    /**
     *
     * 生成比对文件结果Html
     * @param document 结果文件操作对象
     */
    public static void generateCompareFile(Document document, String fileStorePath) {
        //String rootPath = PrintTempUtil.getRootPath();
        /*对比文件存放目录*/
        String path = fileStorePath + "staticFile";
        File file = new File(path);
        /*目录不存在创建目录*/
        if (file.isFile() || !file.exists()) {
            file.mkdirs();
        }
        String compareFilePath = path + "/printCompare.html";
        logger.info("对比差异文件保存路径:{}", compareFilePath);
        //清空指定文件内容，以便重新写入
        WordToHtmlUtil.clearInfoForFile(compareFilePath);
        FileWriter fw = null;
        BufferedWriter bw = null;
        BufferedReader reader = null;
        FileReader fileReader = null;
        try {
            //保存结果文件
            File compareFile = new File(compareFilePath);    // 声明File对象
            OutputStream outFile = new FileOutputStream(compareFile);
            document.saveToFile(outFile, FileFormat.Html);
            document.close();
            StringBuffer sbf = new StringBuffer();
            fileReader = new FileReader(compareFile);
            reader = new BufferedReader(fileReader);
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            String s = "";
            s = sbf.toString();
            org.jsoup.nodes.Document document1 = Jsoup.parse(s);
            //删除生成对比文档中style自带的color
            Elements elements = document1.select("del");
            for (Element element : elements) {
                String replaceAll = element.attributes().get("style").replaceAll("color:#000000;", "");
                element.attributes().put("style",replaceAll);
            }
            Elements elements1 = document1.select("ins");
            for (Element element : elements1) {
                String replaceAll = element.attributes().get("style").replaceAll("color:#000000;", "");
                element.attributes().put("style",replaceAll);
            }
            s = document1.html();
            s = s.replaceAll("Evaluation Warning: The document was created with Spire.Doc for JAVA.", "");
            String s1 = s.substring(0, s.lastIndexOf("</style>"));
            String s2 = s.substring(s.lastIndexOf("</style>"));
            fw = new FileWriter(compareFile.getAbsoluteFile(), false);  //true表示可以追加新内容
            bw = new BufferedWriter(fw);
            //让文档比对修改内容显示蓝色，删除内容显示红色
            bw.write(s1 + " ins{color: blue} del{color: red} " + s2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }



}
