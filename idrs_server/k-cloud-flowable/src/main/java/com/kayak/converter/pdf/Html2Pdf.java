package com.kayak.converter.pdf;

import com.kayak.common.enums.FontEnum;
import com.lowagie.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Html2Pdf {
    private static final Logger logger = LoggerFactory.getLogger(Html2Pdf.class);

    /**
     * html转换成pdf
     *
     * @param inputFileName  待转换的文档名称
     * @param outputFileName 输出的文档名称
     * @param catalogue      操作目录
     * @throws Exception
     */
    public static void convert(String inputFileName, String outputFileName, String catalogue) {
        OutputStream os = null;
        try {
            String inputFile = catalogue + File.separator + inputFileName;
            String outputFile = catalogue + File.separator + outputFileName;

            os = new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();
            String url = new File(inputFile).toURI().toURL().toString();
            renderer.setDocument(url);
            // 解决中文支持问题
            ITextFontResolver fontResolver = renderer.getFontResolver();
            // windows 环境 添加字体
            // String fontPath = ResourceUtils.getFile("classpath:" + FontEnum.SIM_SUN.getPath()).getPath();

            Resource resource = new ClassPathResource(FontEnum.SIM_SUN.getPath());
            String fontPath = resource.getURL().getPath();
            fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            //解决图片的相对路径问题
            renderer.getSharedContext().setBaseURL("file:/" + catalogue + "/");
            renderer.layout();
            renderer.createPDF(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        convert("入职培训测试题及正确答案(1).html", "入职培训测试题及正确答案(1).pdf", "C:\\Users\\yuanjinqiao\\Desktop\\");
    }
}
