package com.kayak.pms.schedule.service;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.FileUtil;
import com.kayak.pms.T85.dao.SysParamDao;
import com.kayak.pms.schedule.model.LogBackup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipOutputStream;

@Service
@APIDefine(desc = "日志备份", model = LogBackup.class)
public class LogBackupService {

    @Autowired
    private SysParamDao sysParamDao;

    private static final Logger logger = LoggerFactory.getLogger(LogBackupService.class);


    /**
     * @param:  (param参数可为空)
     * @Describe：备份前两个🈷之前所有的日志_定时(每月1号)
     */
    @API(desc = "备份前两个月之前所有的日志", auth = APIAuth.NO)
    public void LogBackup(SqlParam<LogBackup> params) throws Exception {
        //压缩后日志存放的路径,存在sys_param系统参数表里
        String  AFTER_LOG_ROUTE = (String)sysParamDao.findBackupPath("9000001","9").get("paravalue");
        //压缩日志前的路径,存在sys_param系统参数表里
        String  FRONT_LOG_ROUTE = (String)sysParamDao.findBackupPath("9000002","9").get("paravalue");
        File source = new File(FRONT_LOG_ROUTE);
        if (source.isDirectory()) {
            String[] children = source.list();
            //递归获取日志目录中的子目录下
            for (int f=0; f<children.length; f++) {
                File ss = new File(source, children[f]);
                if(ss.isDirectory()){
                    //获取前两个月的日期
                    SimpleDateFormat dateFormatx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatTime =DateUtil.dateFormate(DateUtil.getDate(DateUtil.getTwoMonthBefore(DateUtil.getTimestamp19()),dateFormatx),"yyyyMMdd");
                    File[] list = ss.listFiles();
                    if(list.length<=0){continue;}
                    List path = new ArrayList();
                    //遍历源文件夹所有日志
                    for(File file:list) {
                        //获取文件名
                        String fileName = file.getName();
                        //截取文件名获取文件时间
                        int i = fileName.indexOf(".");
                        if(i++<=0){continue;}
                        int j = fileName.indexOf(".",i+1);
                        if(j++<=0){continue;}
                        String str = fileName.substring(i,j-1);
                        //判断截取到的日期是否正确
                        Pattern pattern = Pattern.compile("[0-9]{1,}");
                        Matcher matcher = pattern.matcher((CharSequence)str);
                        boolean result=matcher.matches();
                        //筛选出前两个月之前所有的日志
                        BigDecimal a = new BigDecimal(str);
                        BigDecimal b = new BigDecimal(formatTime);
                        //a大于等于b结束本此循环
                        if(a.compareTo(b) == 1){continue;}
                        String name = fileName.substring(0, i - 1);
                        if(!path.contains(fileName.substring(0,i-1))){
                            path.add(name);
                        }
                        if(result!=true){continue;}
                        //生成的新文件（指定路径如果没有则创建）
                        String strs = "";
                        if("pms".equals(fileName.substring(0,i-1)) || "pms_err".equals(fileName.substring(0,i-1))){
                            strs=FRONT_LOG_ROUTE+ss.getName()+"/"+ name + "_" + formatTime+"/"+fileName;
                        } else if("base".equals(fileName.substring(0,i-1)) || "base_err".equals(fileName.substring(0,i-1))){
                            strs =FRONT_LOG_ROUTE+ss.getName()+"/"+ name + "_" + formatTime+"/"+fileName;
                        } else if("gateway".equals(fileName.substring(0,i-1)) || "gateway_err".equals(fileName.substring(0,i-1))){
                            strs =FRONT_LOG_ROUTE+ss.getName()+"/"+ name + "_" + formatTime+"/"+fileName;
                        } else if("schedule".equals(fileName.substring(0,i-1)) || "schedule_err".equals(fileName.substring(0,i-1))){
                            strs =FRONT_LOG_ROUTE+ss.getName()+"/"+ name + "_" + formatTime+"/"+fileName;
                        }else if("kflow".equals(fileName.substring(0,i-1)) || "kflow_err".equals(fileName.substring(0,i-1))){
                            strs =FRONT_LOG_ROUTE+ss.getName()+"/"+ name + "_" + formatTime+"/"+fileName;
                        }else if("api".equals(fileName.substring(0,i-1)) || "api_err".equals(fileName.substring(0,i-1))){
                            strs=FRONT_LOG_ROUTE+ss.getName()+"/"+ name + "_" + formatTime+"/"+fileName;
                        } else if("healthy".equals(fileName.substring(0,i-1)) || "healthy_err".equals(fileName.substring(0,i-1))){
                            strs=FRONT_LOG_ROUTE+ss.getName()+"/"+ name + "_" + formatTime+"/"+fileName;
                        } else {
                            continue;
                        }
                        File copy = new File(strs);
                        //获取父目录
                        File fileParent = copy.getParentFile();
                        //判断是否存在
                        if (!fileParent.exists()) {
                            // 创建父目录文件夹
                            fileParent.mkdirs();
                        }
                        //判断文件是否存在
                        if (!copy.exists()) {
                            //创建文件
                            copy.createNewFile();
                        }
                        if (DateUtil.computeTwoDateDays(str, DateUtil.getNowDate(), "yyyyMMdd") >= 60) {
                            //文件输入流，用于读取要复制的文件
                            FileInputStream fileInputStream = new FileInputStream(file);
                            //新文件输出流
                            FileOutputStream fileOutputStream = new FileOutputStream (copy);
                            byte[] buffer= new byte[1024];
                            int len;
                            //将文件流信息读取文件缓存区，如果读取结果不为-1就代表文件没有读取完毕，反之已经读取完毕
                            while ((len=fileInputStream.read(buffer))!=-1) {
                                fileOutputStream.write(buffer, 0, len);
                                fileOutputStream.flush();
                            }
                            file.delete();
                            fileInputStream.close();
                            fileOutputStream.close();
                        }
                    }
                    //判断子目录下有没有带日期的log文件,没有的话循环下个子目录
                    if(path==null && path.size()<=0){continue;}
                    //遍历所有要转存的日志所存放的文件夹
                    for(int i=0;i<path.size();i++){
                        //要压缩的文件夹
                        File Backup = new File(FRONT_LOG_ROUTE+ss.getName()+"/"+path.get(i)+"_"+formatTime);
                        //生成zip文件名
                        String zipFileName =AFTER_LOG_ROUTE + path.get(i) +"."+ formatTime + ".tar";
                        File zipFile = new File(zipFileName);
                        FileOutputStream zipfos = null;
                        ZipOutputStream zipOs = null;
                        CheckedOutputStream cos = null;
                        try {
                            zipfos = new FileOutputStream(zipFile);
                            cos = new CheckedOutputStream(zipfos, new CRC32());
                            zipOs = new ZipOutputStream(cos);
                            FileUtil.compress(Backup, zipOs, "");
                            if (zipFile.exists()) {
                                // 写完的日志文件权限改为400
                                try {
                                    //linux上才可以运行,windows上需要装cygwin并且把cygwin的bin目录加到环境变量的path中才可以
                                    Runtime.getRuntime().exec("chmod 400 -R " + zipFile);
                                    //压缩后删除旧文件夹
                                    boolean isDelete = FileUtil.deleteDir(Backup);
                                } catch (IOException e) {
                                    logger.error("set archive file:{} permision catch an error: {}", zipFile, e);
                                }
                            }
                        } finally {
                            if (null != zipOs) {
                                zipOs.close();
                            }
                            if (null != cos) {
                                cos.close();
                            }
                            if (null != zipfos) {
                                zipfos.close();
                            }
                        }
                    }
                } else{
                    continue;
                }
            }
        }
    }
}
