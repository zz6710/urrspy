package com.kayak.pms.printTemp.utils;

import com.aspose.words.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: k-cloud
 * @description: word转Pdf工具类
 * @author: WangZhenXin
 * @create: 2021-01-08 16:19
 * @memo 备注信息
 */
@Repository
public class WordToPdfUtil {
    private static final Logger logger = LoggerFactory.getLogger(WordToPdfUtil.class);


    public void getLicense() {
        try {
            // license.xml应放在Resources路径下
            InputStream is = WordToPdfUtil.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            //aposeLic.setLicense(is);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * word文档转换pdf格式文件
     * @param inPath 需要转换路径+文件名
     * @param outPath 转换后路径+文件名
     */
    public void doc2pdf(String inPath, String outPath) {
    	logger.info("freeMemory--------->{},totalMemory------------>{},maxmemory-------------->{}",
    			Runtime.getRuntime().freeMemory(),
    			Runtime.getRuntime().totalMemory(),
    			Runtime.getRuntime().maxMemory());

        FileOutputStream os=null;
        try {

            long old = System.currentTimeMillis();
            File file = new File(outPath); // 新建一个空白pdf文档
            os = new FileOutputStream(file);
            Document doc = new Document(inPath);
            doc.save(os, SaveFormat.PDF);
            long now = System.currentTimeMillis();
            logger.info("word2Pdf共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
