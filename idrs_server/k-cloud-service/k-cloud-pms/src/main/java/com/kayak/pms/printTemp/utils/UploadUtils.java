package com.kayak.pms.printTemp.utils;

import com.alibaba.fastjson.JSONObject;
import com.kayak.pms.printTemp.service.HtmlService;
import com.spire.doc.*;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

public class UploadUtils {
    @Autowired
    private HtmlService htmlService;

    @Autowired
    private UploadUtils2 uploadUtils2;

    public static String wordToHtml(String wordPath, String jqueryPah) throws Exception {
        File wordFile=new File(wordPath);
        //没有后缀名的文件名
        String fileName=wordFile.getName().substring(0,wordFile.getName().lastIndexOf("."));
        //如果是docx，那么转换一份doc存同一目录下（方便转换html）
        if(wordPath.endsWith(".docx") || wordPath.endsWith(".DOCX")){
            Document document = new Document();
            document.loadFromFile(wordPath, FileFormat.Docx);
            //保存结果文件
            File f= new File(wordPath.substring(0,wordPath.lastIndexOf(".")+1)+"doc") ;    // 声明File对象
            OutputStream outFile=new FileOutputStream(f);
            document.saveToFile(outFile, FileFormat.Doc);
            outFile.close();
            document.close();
        }
        //转换HTML
        UploadUtils2.toHTML(wordFile.getParent(),fileName);
        File f= new File(wordFile.getParent()+ File.separator + fileName+".html");

//        com.spire.doc.Document document = new com.spire.doc.Document();
//        document.loadFromFile(wordPath, FileFormat.Doc);
//        //保存结果文件
//        File f= new File(wordPath.substring(0,wordPath.lastIndexOf(".")+1)+"html") ;    // 声明File对象
//        OutputStream outFile=new FileOutputStream(f);
//        document.saveToFile(outFile, FileFormat.Html);
//        outFile.close();
//        document.close();




        BufferedReader reader = null;
        FileReader fileReader = null;
        StringBuffer sbf = new StringBuffer();
        fileReader = new FileReader(f);
        reader = new BufferedReader(fileReader);

        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            sbf.append(tempStr);
        }
        fileReader.close();
        reader.close();

        String s="";
        //替换文本域字符
        int toc_index=sbf.indexOf(" TOC \\o \"1-1\" \\h \\z \\u ");
        if(toc_index != -1){
            sbf=sbf.replace(toc_index,toc_index+23,"");
        }
        //替换HYPERLINK \l
        while (sbf.indexOf(" HYPERLINK   \\l ") > -1){
            int index=sbf.indexOf(" HYPERLINK   \\l ");
            sbf=sbf.replace(index,index+16,"");
        }
        //替换"_Tocxxxxxxxx"
        while (sbf.indexOf("\"_Toc") > -1){
            int _Toc=sbf.indexOf("\"_Toc");
            int _Toc2=sbf.indexOf("\"",_Toc+1);
            sbf=sbf.replace(_Toc,_Toc2+1,"");
        }
        s = sbf.toString();
        //替换工具包自动生成的字符
        s=s.replaceAll("Evaluation Warning: The document was created with Spire.Doc for JAVA.","");

        HtmlService.Result b=new HtmlService().process(s, jqueryPah);

        FileWriter fw=null;
        BufferedWriter bw=null;
        fw=new FileWriter(f.getAbsoluteFile(),false);  //true表示可以追加新内容
        bw=new BufferedWriter(fw);
        bw.write(b.getHtml());
        bw.close();
        return JSONObject.toJSONString(b.getKeys());
    }

    public static void toHTML(String path, String fileName) throws Exception {
        InputStream input = new FileInputStream(path + File.separator + fileName+".docx");
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            //*重写此方法可实现对图片src进行修改*//*
            public String savePicture(byte[] content, PictureType pictureType,
                                      String suggestedName, float widthInches, float heightInches) {
                return "img/" + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        List pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            //图片保存路径
            String imgPath = path + File.separator + "img" + File.separator;
            File imgFile = new File(imgPath);
            if (imgFile.isFile() || !imgFile.exists()) {
                imgFile.mkdirs();
            }
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                try {
                    //保存图片
                    pic.writeImageContent(new FileOutputStream(imgPath
                            + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Document htmlDocument = (Document) wordToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource((Node) htmlDocument);
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
