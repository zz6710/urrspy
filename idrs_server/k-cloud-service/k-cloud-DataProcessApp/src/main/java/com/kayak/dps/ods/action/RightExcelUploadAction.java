package com.kayak.dps.ods.action;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExcelUtils;
import com.kayak.core.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Map;


@RestController
@RequestMapping(value = "/excelUploadAction")
public class RightExcelUploadAction extends BaseController {


    @Autowired
    protected ComnDao comnDao;

    @RequestMapping(value = "/rightExcelUploadAction.json")
    @ResponseBody
    public String fundUploadAction(@RequestParam(value = "file") MultipartFile file) {

        try {
            ExcelUtils.parseExcel(file, new ExcelUtils() {
                @Override
                public void saveData() throws Exception {
                    String str = "";
                    for (Map<String,Object> m : this.getL()) {
                        m.put("CRT_DATE" , DateUtil.getNowDate());
                        m.put("DEAL_DATE" , DateUtil.getNowDate());
                        m.put("CRT_USER" , Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));

                        SqlRow sqlRes = comnDao.findRow("SELECT ORG_NBR_EXT FROM ODS_ORG_INFO WHERE ORG_FULL_NAME = $S{ORG_NBR_EXT} OR ORG_SHT_NM = $S{ORG_NBR_EXT}", DataSourceProperty.PUB, m);
                        if (sqlRes != null) {
                            str=sqlRes.getString("ORG_NBR_EXT");
                        }
                        m.put("ORG_NBR_EXT" ,str);
                        comnDao.update("DELETE FROM ODS_ASS_RIGHT_BAS_INF WHERE ASS_NBR_EXT = $S{ASS_NBR_EXT}", DataSourceProperty.PUB, m);
                    }
                    saveParseData();
                }

                @Override
                public String setModName() {
                    return "股权信息导入";
                }
                @Override
                public int setSharding() {return 0;}
            });

            return RequestSupport.updateReturnJson(true,"导入成功！",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return RequestSupport.updateReturnJson(false,"导入失败！" + e.getMessage() ,null).toString();
        }


    }
    @RequestMapping(value = "/TrmExcelUploadAction.json")
    @ResponseBody
    public String TrmExcelUploadAction(@RequestParam(value = "file") MultipartFile file) {
        MultipartFile file2 = file;
        try {
            ExcelUtils.parseExcel(file, new ExcelUtils() {
                @Override
                public void saveData() throws Exception {
                    for (Map<String,Object> m : this.getL()) {
                        m.put("TRX_MKT", m.get("TRX_MKT")==null?m.get("trx_mkt")==null?"99":m.get("trx_mkt").toString():m.get("TRX_MKT").toString());
                        m.put("CRT_DATE" , DateUtil.getNowDate());
                        m.put("DEAL_DATE" , DateUtil.getNowDate());
                        m.put("SCR_ID" , m.get("SCR_CD").toString()+"."+m.get("TRX_MKT")+".5");
                        m.put("CRT_USER" , Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
                        comnDao.update("DELETE FROM ODS_TRM_BAS_INF WHERE SCR_ID = $S{SCR_ID}", DataSourceProperty.PUB, m);
                    }
                    saveParseData();
                }

                @Override
                public String setModName() {
                    return "定期存款基本信息导入";
                }
                @Override
                public int setSharding() {return 0;}
            });

//            this.TrmBlExcelUploadAction(file2);

            return RequestSupport.updateReturnJson(true,"导入成功！",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return RequestSupport.updateReturnJson(false,"导入失败！" + e.getMessage() ,null).toString();
        }


    }


    @RequestMapping(value = "/TrmBlExcelUploadAction.json")
    @ResponseBody
    public String TrmBlExcelUploadAction(MultipartFile file) {

        try {
            ExcelUtils.parseExcel(file, new ExcelUtils() {
                @Override
                public void saveData() throws Exception {
                    for (Map<String, Object> m : this.getL()) {
                        m.put("TRX_MKT", m.get("TRX_MKT")==null?m.get("trx_mkt")==null?"99":m.get("trx_mkt").toString():m.get("TRX_MKT").toString());
                        m.put("CRT_DATE", DateUtil.getNowDate());
                        m.put("DEAL_DATE", DateUtil.getNowDate());
                        m.put("CRT_USER", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
                        m.put("SCR_ID" , m.get("SCR_CD").toString()+"."+m.get("TRX_MKT")+".5");
                        comnDao.update("DELETE FROM ODS_TRM_SUPPLY_INF WHERE SCR_ID = $S{SCR_ID}", DataSourceProperty.PUB, m);
                    }
                    saveParseData();
                }

                @Override
                public String setModName() {
                    return "定期存款补录信息导入";
                }

                @Override
                public int setSharding() {
                    return 0;
                }
            });

            return RequestSupport.updateReturnJson(true, "导入成功！", null).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return RequestSupport.updateReturnJson(false, "导入失败！" + e.getMessage(), null).toString();
        }
    }
    @RequestMapping(value = "/underFundUploadAction.json")
    @ResponseBody
    public String underFundUploadAction(@RequestParam(value = "file") MultipartFile file) {

        try {

            ExcelUtils.parseExcel(file, new ExcelUtils() {
                @Override
                public void saveData() throws Exception {
                    String str = "";
                    for (Map<String,Object> m : this.getL()) {
                        m.put("CRT_DATE" , DateUtil.getNowDate());
                        m.put("REPORT_DATE" , m.get("REPORT_DATE").toString());
                        m.put("CRT_USER" , Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
                        comnDao.update("DELETE FROM app_under_fund_info WHERE REPORT_DATE = $S{REPORT_DATE}", DataSourceProperty.PUB, m);
                    }
                    saveParseData();
                }

                @Override
                public String setModName() {
                    return "私募基金底层持仓信息导入";
                }
                @Override
                public int setSharding() {return 0;}
            });
            return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
        }


    }

    @RequestMapping(value = "/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileMidTrmDpsInf(String fileName, HttpServletResponse response) {
        fileName = "TimeDeposit.xlsx";
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        try{
            response.setContentType("application/x-octetstream;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Access-Control-Expose-Headers","*");
            response.setHeader("filename",fileName);
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
//            response.addHeader("Access-Control-Expose-Headers","*");
//            response.setHeader("filename",fileName);
//            response.setHeader("Content-Disposition", "attachment;filename=" +
//                    new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            //本机或本地服务器模板文档存储路径
            String temPath =  System.getProperty("os.name").toLowerCase().startsWith("win")
                    ?SysUtil.getSystemParamsByParaid("90000010001")//本机存放根路径
                    :SysUtil.getSystemParamsByParaid("90000010002");//服务器存放根路径
            File dir = new File(temPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            //从本地路径输出前端head中响应IO
            String pathFile = temPath + fileName;
            File file = new File(pathFile);
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, buffer.length);
                outputStream.flush();
                i = bufferedInputStream.read(buffer);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e2) {
                log.error("io关闭异常[{}]", e2);
            }
        }
    }
}
