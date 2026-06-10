package com.kayak.dps.app.utils;

import com.kayak.dps.ods.exception.DbfFileWriteException;
import com.kayak.dps.ods.exception.TxtFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class TxtFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(TxtFileUtil.class);
    // 列分隔符
    private static final String COLUMN_SPLIT ="|" ;
    // 行分隔符
    private static final byte[] LINE_SEPARATOR = {'\r', '\n'};
    /**
     * 生成TXT文件
     *
     */
    public static void write(List<Object[]> values, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            for (Object[] row : values) {
                for (int col = 0; col < row.length; col++) {
                    if (col > 0) {
                        fos.write(Integer.parseInt(COLUMN_SPLIT));
                    }
                    Object value = row[col];
                    if(value == null){
                        value = "";
                    }
                    fos.write(value.toString().getBytes(StandardCharsets.UTF_8));
                }
                fos.write(LINE_SEPARATOR);
            }
            logger.info(" 生成TXT文件完成: 共{}行数据, 生成文件路径: {}", values.size(), filePath);
        }catch (Exception e){
            logger.error("TXT文件生成失败: ", e);
            throw new TxtFileException("TXT文件生成失败: " + filePath);
        }
    }
}
