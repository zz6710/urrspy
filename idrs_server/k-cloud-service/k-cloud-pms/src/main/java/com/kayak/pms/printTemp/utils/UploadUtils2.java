package com.kayak.pms.printTemp.utils;

import com.kayak.utils.WordUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

public class UploadUtils2 {

    public static void toHTML(String path, String fileName) throws Exception {
        InputStream input = new FileInputStream(path + File.separator + fileName+".doc");
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            /*重写此方法可实现对图片src进行修改*/
            public String savePicture(byte[] content, PictureType pictureType,
                                      String suggestedName, float widthInches, float heightInches) {
                if (suggestedName.endsWith(".wmf")) {
                    //将wmf格式的图片转换为svg格式
                    suggestedName = suggestedName.replace(".wmf", ".svg");
                }
                return "img/" + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        List pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            //图片保存路径
            String imgDir = path + File.separator + "img" + File.separator;
            File imgFile = new File(imgDir);
            if (imgFile.isFile() || !imgFile.exists()) {
                imgFile.mkdirs();
            }
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                FileOutputStream outputStream = null;
                String imgPath = imgDir + pic.suggestFullFileName();
                try {
                    //文件输出流
                     outputStream = new FileOutputStream(imgPath);
                    //保存图片
                    pic.writeImageContent(outputStream);
                    if (pic.suggestFullFileName().endsWith(".wmf")) {
                        //wmf转svg
                        WordUtils.wmfToSvg(imgPath, imgPath.replace(".wmf", ".svg"));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    //关闭输出流
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (imgPath.endsWith(".wmf")) {
                        //删除wmf格式图片
                        File file = new File(imgPath);
                        System.out.println(imgPath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            }
        }
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
        String content = new String(outStream.toByteArray());
        FileUtils.writeStringToFile(new File(path, fileName+".html"), content, "utf-8");
    }
}
