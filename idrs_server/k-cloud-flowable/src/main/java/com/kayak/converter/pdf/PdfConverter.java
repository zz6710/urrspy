package com.kayak.converter.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * pdf转换器
 */
public class PdfConverter {

    private static final Logger logger = LoggerFactory.getLogger(PdfConverter.class);

    /**
     * 判断资源类型文档类
     */
    private static String getResourceTypesDocument(String suffix) {
        String type = null;
        switch (suffix) {
            //文档类型
            case ".doc":
            case ".docx":
            case ".txt":
                type = "word";
                break;
            case ".xls":
            case ".xlsx":
                type = "excel";
                break;
            case ".ppt":
            case ".pptx":
                type = "ppt";
                break;
        }
        return type;
    }

    public static boolean converter(String inFileName, String outFileName, String catalogue) {
        String suffix = inFileName.substring(inFileName.lastIndexOf("."));
        if (".docx".equals(suffix)) {
            Docx2Pdf.convert(inFileName, outFileName, catalogue);
        } else if (".txt".equals(suffix)) {
            Txt2Pdf.convert(inFileName, outFileName, catalogue);
        }
//        else if (".doc".equals(suffix)) {
//            String fileName = inFileName.substring(0, inFileName.lastIndexOf("."));
//            Doc2Html.convert(inFileName, fileName + ".html", catalogue);
//            Html2Pdf.convert(fileName + ".html", outFileName, catalogue);
//        }
        else {
            return false;
        }
        return true;
    }

}
