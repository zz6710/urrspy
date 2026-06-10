package com.kayak.converter.pdf;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-09-30 14:31
 **/

import com.kayak.common.enums.FontEnum;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Doc2Html {
    private static final Logger logger = LoggerFactory.getLogger(Doc2Html.class);
    // 不闭合标签列表
    private static List<String> _targetTagName = Arrays.asList("((<br)([^<>]*?)(>))", "((<hr)([^<>]*?)(>))", "((<img)([^<>]*?)(>))",
            "((<input)([^<>]*?)(>))", "((<link)([^<>]*?)(>))", "((<meta)([^<>]*?)(>))",
            "((<area)([^<>]*?)(>))", "((<base)([^<>]*?)(>))", "((<col)([^<>]*?)(>))",
            "((<wbr)([^<>]*?)(>))", "((<command)([^<>]*?)(>))", "((<embed)([^<>]*?)(>))",
            "((<keygen)([^<>]*?)(>))", "((<param)([^<>]*?)(>))", "((<source)([^<>]*?)(>))",
            "((<track)([^<>]*?)(>))");

    /**
     * @param docFileName  doc文件名
     * @param htmlFileName 输出html文件名
     * @param catalogue    文件存储目录
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws Exception
     */
    public static void convert(String docFileName, String htmlFileName, String catalogue) {
        ByteArrayOutputStream out = null;
        InputStream in = null;
        try {
            String docFilePath = catalogue + File.separator + docFileName;
            String htmlFilePath = catalogue + File.separator + htmlFileName;
            // 输入流
            in = new FileInputStream(docFilePath);
            // 输出流
            out = new ByteArrayOutputStream();

            HWPFDocument wordDocument = new HWPFDocument(in);
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder()
                            .newDocument());
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                public String savePicture(byte[] content,
                                          PictureType pictureType, String suggestedName,
                                          float widthInches, float heightInches) {
                    return suggestedName;
                }
            });
            // 保存图片
            savePictures(wordDocument, catalogue);
            // 转换对象
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);

            StreamResult streamResult = new StreamResult(out);

            TransformerFactory tf = TransformerFactory.newInstance();
            // 设置编码格式，及转换文档类型
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "HTML");
            serializer.transform(domSource, streamResult);
            // 输出转换完成的html内容
            writeFile(new String(out.toByteArray()), htmlFilePath);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    // 关闭流
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    // 关闭流
                    out.close();
                } catch (IOException e) {
                }
            }
        }

    }

    /**
     * 保存文档图片
     *
     * @param wordDocument 文档
     * @param catalogue    保存目录
     */
    public static void savePictures(HWPFDocument wordDocument, String catalogue) {
        List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = pics.get(i);
                try {
                    String picturePath = catalogue + File.separator + pic.suggestFullFileName();
                    pic.writeImageContent(new FileOutputStream(picturePath));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 输出html文件
     *
     * @param content 文件内容
     * @param path    输出路径
     */
    public static void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        String utf8Content;
        try {
            utf8Content = new String(content.getBytes("UTF-8"), "UTF-8");
            org.jsoup.nodes.Document doc = Jsoup.parse(utf8Content);
            utf8Content = doc.outerHtml();
            utf8Content = setHtmlFont(utf8Content);
            utf8Content = handleTagClose(utf8Content);
            utf8Content = "<!DOCTYPE html>" + utf8Content;
            File file = new File(path);
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bw.write(utf8Content);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ie) {
            }
        }
    }

    /**
     * 正则处理html字符串标签闭合 关闭不闭合的标签
     *
     * @param htmlStr html文件内容字符串
     * @return
     */
    public static String handleTagClose(String htmlStr) {
        // 需要替换的字符串
        String oldStr;
        // 替换成的字符串
        String newStr;
        // 需要处理闭合的标签表达式
        String reg;
        Pattern p;
        Matcher m;
        int num = _targetTagName.size();
        for (int i = 0; i < num; i++) {
            reg = _targetTagName.get(i);
            p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
            m = p.matcher(htmlStr);
            while (m.find()) {
                oldStr = m.group(1);
                newStr = oldStr.substring(0, oldStr.length() - 1) + " />";
                htmlStr = htmlStr.replace(oldStr, newStr);
            }
        }
        return htmlStr;
    }

    /**
     * 正则替换html字符串中的字体为指定字体
     *
     * @param htmlStr html文件内容字符串
     * @return
     */
    public static String setHtmlFont(String htmlStr) {
        // 字体替换正则表达式
        String reg = "((font-family\\s?:\\s?)([a-zA-z\\u4E00-\\u9FA5]*?)([;}]))";
        Pattern fontP = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher fontM = fontP.matcher(htmlStr);
        String fontName; // 匹配到的字体名称
        String oldStr; // 匹配到的字体样式字符串
        // 替换全文字体
        while (fontM.find()) {
            fontName = fontM.group(3);
            oldStr = fontM.group(1);
            // 和指定的字体相同，则不做处理
            if (fontName.equalsIgnoreCase(FontEnum.SIM_SUN.getId())) {
                break;
            }
            // 判断替换的字符串位置是否是末尾
            if (oldStr.indexOf(";") != -1) {
                htmlStr = htmlStr.replace(oldStr, "font-family:" + FontEnum.SIM_SUN.getId() + ";");
            } else {
                htmlStr = htmlStr.replace(oldStr, "font-family:" + FontEnum.SIM_SUN.getId() + ";");
            }
        }
        // 设置body的字体，此处必须设置全局字体文件，不设置全局字体则设置的字体无效
        reg = "<style type=\"text/css\">";
        // 设置正则表达式和匹配规则为不区分大小写
        Pattern cssP = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher cssM = cssP.matcher(htmlStr);
        // 匹配body
        if (cssM.find()) {
            oldStr = cssM.group();
            htmlStr = htmlStr.replace(oldStr, oldStr + "body{font-family:" + FontEnum.SIM_SUN.getId() + ";}");
        }
        return htmlStr;
    }

    public static void main(String[] args) throws Exception {
        convert("入职培训测试题及正确答案(1).doc", "入职培训测试题及正确答案(1).html", "C:\\Users\\yuanjinqiao\\Desktop\\");
    }
}
