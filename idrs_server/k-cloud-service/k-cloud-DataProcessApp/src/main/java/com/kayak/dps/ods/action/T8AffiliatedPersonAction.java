package com.kayak.dps.ods.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.app.model.T8AffiliatedPerson;
import com.kayak.dps.app.utils.SftpUtils;
import com.kayak.dps.ods.dao.T8AffiliatedPersonDao;
import com.kayak.dps.valtabimp.excel.ExcelParse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class T8AffiliatedPersonAction extends BaseController {


    @Autowired
    private T8AffiliatedPersonDao t8AffiliatedPersonDao;

    @RequestMapping(value = "/priceTempUpload.json",produces = { "application/json;charset=UTF-8"})
    //
    public String priceTempUpload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("affilate_start_date", request.getParameter("affilate_start_date"));
            params.put("affilate_end_date", request.getParameter("affilate_end_date"));
            List<T8AffiliatedPerson> t8AffiliatedPeople = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, T8AffiliatedPerson.class, true, null);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // 移除第一标题行
            t8AffiliatedPeople.remove(0);
            for (T8AffiliatedPerson tp : t8AffiliatedPeople) {
                String effectiveDate = tp.getEffectiveDate();
                String expiryDate = tp.getExpiryDate();
                String affiliatedType = tp.getAffiliatedType();
                if(!StringUtils.isEmpty(effectiveDate)){
                   // custRegisterInfo.setTelPhone(telPhone.indexOf("E")!=-1?reverseToString(telPhone):telPhone);
                    if(effectiveDate.indexOf("E")!=-1){
                        tp.setEffectiveDate(reverseToString(effectiveDate));
                    }else{
                        tp.setEffectiveDate(effectiveDate.indexOf("/")!=-1?getDatas(String.valueOf(effectiveDate)):effectiveDate);
                    }
                }
                if(!StringUtils.isEmpty(expiryDate)){
                    if(expiryDate.indexOf("E")!=-1){
                        tp.setExpiryDate(reverseToString(expiryDate));
                    }else{
                        tp.setExpiryDate(expiryDate.indexOf("/")!=-1?getDatas(String.valueOf(expiryDate)):expiryDate);
                    }
                }
                tp.setAffiliatedType(StringUtils.isEmpty(affiliatedType)?null:affiliatedType.split(" ")[0]);
            }
            t8AffiliatedPersonDao.importT8AffiliatedPerson(t8AffiliatedPeople);
//            //信批表单关联法人处理
//            t8AffiliatedPersonDao.handelBaseData(params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/handelBaseData.json",produces = { "application/json;charset=UTF-8"})
    //
    public String handelBaseData() {
        try {
            Map<String, Object> params = RequestSupport.getParameters();
            t8AffiliatedPersonDao.handelBaseData(params);
            return updateSuccess("处理成功！");
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
    }

    private String getDatas(String str) {
        String[] arrs = str.split("/");
        String tmp = "";
        tmp = arrs[0];
        tmp += Integer.valueOf(arrs[1])<10?"0"+arrs[1]:arrs[1];
        tmp += Integer.valueOf(arrs[2])<10?"0"+arrs[2]:arrs[2];
        return tmp;
    }

    private String reverseToString(String actualProdTerDate) {
        BigDecimal bd = new BigDecimal(actualProdTerDate);
        return bd.toPlainString();
    }


    /*@MethodAnnotation(desc = "关联法人导入")
    @RequestMapping(value = "/priceTempUpload.json", produces = {"application/json;charset=UTF-8"})
    public String uploadFile(@RequestParam(value = "file") MultipartFile[] files)  {

        Map<String, Object> params = RequestSupport.getParameters();
        FileInputStream fis = null;
        T8AffiliatedPerson t8AffiliatedPerson = new T8AffiliatedPerson();
        Date date1 = new Date();//获取当前系统时间
        String effectiveDate = params.get("effectiveDate").toString();//获取页面手动设置的生效日期
        try {
            //获取上传的跟路径
            String filePath = SysUtil.getSystemParamsByParaid("80000080001");
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                filePath = filePath + File.separator + "printTemp" + File.separator;
                //判断时间
                String nowDate = DateUtil.getNowDate();
                int date = Integer.parseInt(nowDate);
                String oldDate = (String) params.get("effectiveDate");
                int oldDate1 = Integer.parseInt(oldDate);
                if (date > oldDate1) {
                    return RequestSupport.updateReturnJson(false, "生效时间必须大于或等于当前时间", null).toString();
                }
                FileUtil.fileSaveToLocal(file, fileName, filePath);
                fis = new FileInputStream(filePath + fileName);
                XSSFWorkbook book = new XSSFWorkbook(fis);
                XSSFSheet sheet = book.getSheetAt(0);//获取Excel模板第一页权限
                int lastRowNum = sheet.getLastRowNum();//获取Excel模板有数据的最大行号
                t8AffiliatedPersonDao.updateT8AffiliatedPersonS(oldDate);//填充无生效日的法人数据
                t8AffiliatedPersonDao.updateT8AffiliatedPersonValS(oldDate);//统一更改有数据的
                for(int i=1; i<=lastRowNum; i++){
                    XSSFRow row1 = sheet.getRow(i);
                    if((row1==null)){
                        continue;//填充excel模板数据中间有空的跳出本次循环
                    }
                    String s = row1.getCell(0).toString();
                        t8AffiliatedPerson.setNameOperson(s);
                        t8AffiliatedPerson.setEffectiveDate(oldDate);
                        t8AffiliatedPersonDao.addT8AffiliatedPerson(t8AffiliatedPerson);//循环添加

                }
                book.close();//关闭流资源
                //保存数据
                if(fis!=null){
                    fis.close();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "文件上传失败"+e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件上传成功", null).toString();
    }
*/
    /**
     * 下载模板
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/downloadScheduleTemp.json", produces = {"application/json;charset=UTF-8"})
    public void downloadScheduleTemp(HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String fileName = "T8AffiliatedPerson.xlsx";
        //获取公共模板文件路径

        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;

        try{
            if (fileName != null) {
                response.addHeader("Access-Control-Expose-Headers","*");
                response.setHeader("filename",fileName);
                response.setHeader("content-disposition", "attachment;filename=" +
                        new String(fileName.getBytes("GB2312"), "ISO8859-1"));


                /**指定下载模板文件的文件服务器的ip、用户名、用户密码等*/
                //远端服务器ip地址
                String remoteIp = SysUtil.getSystemParamsByParaid("70000010004");
                //远端服务器用户名
                String remoteUserName = SysUtil.getSystemParamsByParaid("70000010005");
                //远端服务器密码
                String remoteUserPassword = SysUtil.getSystemParamsByParaid("70000010006");
                //远端服务器文档模板sftp存储路径
                String remotePath = SysUtil.getSystemParamsByParaid("90000010000");
                String dealRemotePath = remotePath;
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