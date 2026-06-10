package com.kayak.dps.ods.util;

import com.kayak.core.system.SysUtil;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.dao.DealValuePortDao;
import com.kayak.dps.ods.exception.DbfFileReadException;
import com.kayak.dps.ods.exception.DbfFileWriteException;
import com.kayak.dps.pub.ICallback;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBFFileUtil {

    static Logger logger = LoggerFactory.getLogger(DBFFileUtil.class);
    /**
     * 生成DBF文件
     * @param header
     * @param values
     * @param filePath  文件路径(含文件名)
     */
    public static void write(DBFField[] header, List<Object[]> values, String filePath){

        if(header == null){
            throw new DbfFileWriteException("文件头不能为空");
        }


        try(FileOutputStream fos = new FileOutputStream(filePath);
            DBFWriter writer = new DBFWriter(fos, Charset.forName("GBK"))) {

            // 文件头
            writer.setFields(header);

            // 文件体
            int i= 1;
            for(Object[] row : values){
                logger.info(" 明细 {} : {}", i, (Object) row); i++;

                if(values != null && header.length != row.length){
                    logger.error("文件体字段数量与文件头字段数量不一致: {}", row);
                    throw new DbfFileWriteException("文件体字段数量与文件头字段数量不一致");
                }
                writer.addRecord(row);
            }

            logger.info(" 生成DBF文件完成: 共{}行数据, {}个字段, 生成文件路径: {}", values.size(), header.length, filePath);
        }catch (Exception e){
            logger.error("DBF文件生成失败: ", e);

            if(e instanceof DbfFileWriteException){
                throw (DbfFileWriteException)e;
            }
            throw new DbfFileWriteException("DBF文件生成失败: "+ e.getMessage() + " 文件: " + filePath);
        }
    }

    /**
     * 读取DBF文件
     * @param filePath          文件路径(含文件名)
     * @param printFileContent 是否打印文件体
     * @return
     */
    public static List<Object[]> read(String filePath, boolean printFileContent){
        try(InputStream inputStream  = new FileInputStream(filePath);
            DBFReader reader = new DBFReader(inputStream, Charset.forName("GBK"));
        ) {
            int headerNum = reader.getFieldCount();

            // 文件头
            logger.info("================文件头 START================");
            for( int i=0; i<headerNum; i++) {
                DBFField field = reader.getField(i);
                logger.info("field {} : {}", i, field);
            }
            logger.info("================文件头 END  ================");

            // 文件体
            List<Object[]> values = new ArrayList<>();
            Object[] row;
            while( (row = reader.nextRecord()) != null) {
                values.add(row);
                if(printFileContent){
                    logger.info("文件体: {}", (Object)row);
                }
            }

            logger.info(" 文件解析成功: 共{}行数据, {}个字段, 解析文件路径: {}", values.size(), headerNum, filePath);
            return values;
        } catch (Exception e){
            logger.error(" 文件读取失败: {}", e);
            throw new DbfFileReadException("文件读取失败: " + e.getMessage());
        }
    }

    /**
     * 读取DBF文件
     * @param filePath          文件路径(含文件名)
     * @param printFileContent 是否打印文件体
     * @return 返回读取数据总数
     */
    public static int readMore(String filePath, boolean printFileContent, ICallback handle, String id, DealValuePortDao dealValuePortDao,String charset){
        Charset charset1=null;
        if ("0".equals(charset)) {
            charset1=StandardCharsets.UTF_8;
        }else if ("1".equals(charset)) {
            charset1= Charset.forName("GBK");
        }

        try(InputStream inputStream  = new FileInputStream(filePath);
            DBFReader reader = new DBFReader(inputStream, charset1);
        ) {
            int headerNum = reader.getFieldCount();

            // 文件头
            logger.info("================文件头 START================");
            for( int i=0; i<headerNum; i++) {
                DBFField field = reader.getField(i);
                logger.info("field {} : {}", i, field);
            }
            logger.info("================文件头 END  ================");

            // 文件体
            List<Object[]> values = new ArrayList<>();
            Object[] row;
            int rowNum = 1;
            while( (row = reader.nextRecord()) != null) {
                values.add(row);
                if(printFileContent){
                    logger.info("文件体: {}", (Object)row);
                }
                if (rowNum % 10000 == 0) {
                    handle.call(values);
                    values.clear();
                }
                rowNum++;
            }

            if(values.size()>0){
                handle.call(values);
            }

            logger.info(" 文件解析成功: 共{}行数据, {}个字段, 解析文件路径: {}", values.size(), headerNum, filePath);
            values.clear();
            return rowNum-1;
        } catch (Exception e){
            logger.error(" 文件读取失败: {}", e);
            try {
                updateFileLog(id, Constants.FILE_STATE_02, "处理失败:" + e.getMessage(), 0, dealValuePortDao);
            } catch (Exception e1) {
                logger.error(" 文件流水更新失败: ", e);
            }
            return -1;
            ///throw new DbfFileReadException("文件读取失败: " + e.getMessage());
        }
    }

    public static void updateFileLog(String id, String fileState, String message, Integer totalNum, DealValuePortDao dealValuePortDao) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("message", message);
        params.put("fileState", fileState);
        params.put("totalNum", totalNum);
        params.put("deal_user_id", SysUtil.getSysUserParamValue("sys_user_userid"));
        dealValuePortDao.updateFileLog(params);
    }
}
