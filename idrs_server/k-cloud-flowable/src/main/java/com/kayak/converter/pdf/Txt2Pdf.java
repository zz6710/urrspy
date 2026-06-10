package com.kayak.converter.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Txt2Pdf {
    private static final Logger logger = LoggerFactory.getLogger(Txt2Pdf.class);

    public static void convert(String inFileName, String outFileName, String catalogue) {
        try {
            String inPath = catalogue + File.separator + inFileName;
            String outPath = catalogue + File.separator + outFileName;
            Document document = new Document(PageSize.A4);
            InputStream is = new FileInputStream(inPath);
            //读取文本内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            PdfWriter.getInstance(document, new FileOutputStream(outPath));

            /** 新建一个字体,iText的方法
             * STSongStd-Light 是字体，在iTextAsian.jar 中以property为后缀
             * UniGB-UCS2-H   是编码，在iTextAsian.jar 中以cmap为后缀
             * H 代表文字版式是 横版， 相应的 V 代表 竖版
             */
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font fontChinese = new Font(bfChinese);

            //打开文档，将要写入内容
            document.open();
            String line = reader.readLine();
            while (line != null) {
                Paragraph pg = new Paragraph(line, fontChinese);
                document.add(pg);
                line = reader.readLine();
            }
            document.close();
            reader.close();
            is.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws DocumentException, IOException {
        convert("17336.txt", "17336.pdf", "C:\\Users\\yuanjinqiao\\Desktop\\");
    }
}

