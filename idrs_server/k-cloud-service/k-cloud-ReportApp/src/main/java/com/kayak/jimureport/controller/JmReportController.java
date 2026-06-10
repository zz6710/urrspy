package com.kayak.jimureport.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;

import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.FileUtil;
import com.kayak.core.util.Tools;
import com.kayak.jimureport.report.dao.JmReportDao;
import com.kayak.jimureport.report.dao.XmlDao;
import com.kayak.rpt.rhjrjgtj.service.AppAssetA1413DepStrucService;
import com.kayak.rpt.rhlc.service.AppAssetUnincorporatedEntityService;
import com.kayak.rpt.zz.manage.dao.ProdTransRegistInfoDao;
import com.kayak.rpt.zz.manage.service.AppOverseasInvInfo1Service;
import com.kayak.rpt.zz.manage.service.AppOverseasInvInfo2Service;
import com.kayak.rpt.zz.manage.service.AppOverseasInvInfoService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/JmReport")
@Scope("prototype")
public class JmReportController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(JmReportController.class);

    @Resource(name = "jmReportDao")
    private JmReportDao jmReportDao;

    @Resource(name = "xmlDao")
    private XmlDao xmlDao;

    @Autowired
    private ProdTransRegistInfoDao prodTransRegistInfoDao;

    @Autowired
    private ComnDao comnDao;

    @Autowired
    private AppAssetA1413DepStrucService appAssetA1413DepStrucService;

    @Autowired
    private AppOverseasInvInfoService appOverseasInvInfoService;

    @Autowired
    private AppOverseasInvInfo1Service appOverseasInvInfo1Service;

    @Autowired
    private AppOverseasInvInfo2Service appOverseasInvInfo2Service;

    @Autowired
    private AppAssetUnincorporatedEntityService appAssetUnincorporatedEntityService;

    /*@Autowired
    private CommonService commonService;*/

    private static Map<String, Object> PubParams = null;

    Map<String, Object> params = RequestSupport.getParameters();

    /*循环获取参数*/
    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }
        return res;
    }

    /*参数格式化*/
    public Map<String, Object> fun(HttpServletRequest request) {
        Map<String, String> param = getAllRequestParam(request);
        for (String key : param.keySet()) {
            if ("null".equals(param.get(key)) || "".equals(param.get(key))) {
                params.put(key, null);
            } else {
                params.put(key, param.get(key));
            }
        }
        return params;
    }

    /*查询SQL保存接口*/
    @ResponseBody
    @RequestMapping(value = "/sqlSave.json")
    public Object sqlSave() {
        try {
            getSql();
            List<SqlRow> count = jmReportDao.selectSQLDICTCOUNT(params);

            if (!count.isEmpty()) {
                if (count.size() == 1) {
                    params.put("count", count.get(0).getString("jimu_report_id"));
                } else {
                    return updateFailure("SQL保存失败");
                }
            }
            setJmReProSql(params);
            return updateSuccess("SQL保存成功");
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setJmReProSql(Map<String, Object> params)
            throws Exception {
        try {
            xmlDao.setJmReProSql(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    /*查询SQL保存接口*/
    @ResponseBody
    @RequestMapping(value = "/querySave.json")
    public Object querySave() {
        getSql();
        try {
            setQuerySql(params);
            return updateSuccess("SQL保存成功");
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void setQuerySql(Map<String, Object> params)
            throws Exception {
        try {
            // 校验下是不是已经存在
            String sql = " select query_name from T8_QUERY_SQL where query_name = $S{query_name} and menuid = $S{menuid} ";
            params.put("newSql", sql);
            List<SqlRow> query_names = jmReportDao.selectSqlInfos(sql, params);

            if (!CollectionUtils.isEmpty(query_names) || query_names.size() > 0) {
                throw new Exception("查询名称以存在");
            }
            xmlDao.setQuerySql(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    /*保存SQL接口*/
    @ResponseBody
    @RequestMapping(value = "/updateSQL.json")
    public Object uptQuerySql() {
        getSql();
        try {
            uptQuerySql(params);
            return updateSuccess("SQL保存成功");
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void uptQuerySql(Map<String, Object> params)
            throws Exception {
        try {

            xmlDao.uptQuerySql(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    /*保存SQL接口*/
    @ResponseBody
    @RequestMapping(value = "/updateQuery.json")
    public Object updateQuery() {
        getSql();
        try {
            updateQuery(params);
            return updateSuccess("SQL保存成功");
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateQuery(Map<String, Object> params)
            throws Exception {
        try {
            xmlDao.updateQuery(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("sql数据插入到数据库报错");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/saveMenuInfo.json")
    public Object saveMenu() {
        Map<String, Object> returndata = new HashMap<>();
        try {
            if (!ObjectUtils.isEmpty(params.get("type"))) {
                if ("update".equals(params.get("type"))) {
                    uptMenuInfo(params);
                } else if ("insert".equals(params.get("type"))) {
                    SqlRow sqlResul = jmReportDao.selectmenu(params);
                    if (null == sqlResul || sqlResul.size() == 0) {
                        insertMenuInfo(params);
                    } else {
                        returndata.put("msg", "菜单ID已经存在" );
                       return updateSuccess(returndata);
                    }
                }
            }
           // return updateSuccess("菜单保存成功");
            returndata.put("msg", "菜单保存成功" );
            return updateSuccess(returndata);
        } catch (Exception e) {
           // return updateFailure("菜单保存失败");
            returndata.put("msg", "菜单保存失败" );
            return updateSuccess(returndata);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void uptMenuInfo(Map<String, Object> params)
            throws Exception {
        try {
            String st =
                    "update SYS_MENU set MENUNAME=$S{menuname}," +
                            "UPPERID=$S{upperid},LOADORDER=$S{upperid},PAGEID=$S{upperid} " +
                            "where MENUID=$S{menuid}";
            params.put("newSql", st);
            jmReportDao.update(st, params);

            st = "update sys_role_right set BUTTONDESC=$S{menuname}," +
                            "UPPERID=$S{upperid} " +
                            "where MENUID=$S{menuid} " +
                            "and ROLEID='*'";
            params.put("newSql", st);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("菜单更新报错");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertMenuInfo(Map<String, Object> params)
            throws Exception {
        try {
            String st =
                    "insert into SYS_MENU(MODULEID,MENUID,MENUNAME,UPPERID,URL,LOADORDER,STATUS,PAGEID,FUNCTYPE,MENUTYPE,ICON) " +
                            "values('8',$S{menuid},$S{menuname},$S{upperid},'page/M8/jmreport/M860004.html',$S{upperid}," +
                            "'N',$S{upperid},'1','0','images/M8/comnmenu-M841.png')";
            params.put("newSql", st);
            jmReportDao.update(st, params);

            st = "insert into sys_role_right(ROLEID,MODULEID,MENUID,BUTTONID,EXEID,BUTTONDESC,UPPERID,RTYPE,FUNCTYPE,REMARK) " +
                    "values('*','8',$S{menuid},$S{menuid},'null',$S{menuname},$S{upperid},'0',null,null)";
            params.put("newSql", st);
            jmReportDao.update(st, params);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("菜单新增报错");
        }
    }

    /*保存SQL接口*/
    @ResponseBody
    @RequestMapping(value = "/deleteMenuAndUrl.json")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Object deleteMenuAndUrl() {
        try {
            String sql = "";
            //删除报表
            if (!StringUtils.isEmpty(params.get("menuid"))) {
                sql = "update JIMU_REPORT set DEL_FLAG='1' " +
                      " where ID=(select DISTINCT JIMU_REPORT_ID FROM JIMU_SQLDICT WHERE ID=$S{menuid}) ";
                jmReportDao.update(sql, params);
            }
            //删除默认查询条件
            if (!StringUtils.isEmpty(params.get("menuid"))) {
                //comnDao.update("JMREPORT0038", params);
            }
            //删除关联地址
            //comnDao.update("test021",params);
            //删除菜单数据
            if (!StringUtils.isEmpty(params.get("menuid"))) {
                //comnDao.update("JMREPORT0014", params);
                sql = "delete from SYS_MENU where MENUID=$S{menuid} ";
                jmReportDao.update(sql, params);
                sql = "delete from sys_role_right where menuid=$S{menuid}";
                jmReportDao.update(sql, params);
            }
            //删除报表SQL
            if (StringUtils.isEmpty(params.get("menuid"))) {
                sql = "delete from JIMU_SQLDICT where id=$S{menuid}";
                jmReportDao.update(sql, params);
            }
            // 删除查询条件
            if (!StringUtils.isEmpty(params.get("jmmenuid"))) {
                sql = "delete from t8_query_sql where menuid = $S{jmmenuid}";
                jmReportDao.update(sql, params);
            }
            return updateSuccess("菜单删除成功");
        } catch (Exception e) {
            return updateFailure("菜单删除失败");
        }
    }

    public void getSql() {
        params = RequestSupport.getParameters();
        String sql = params.get("sql").toString();
        params.put("sql", sql);
    }

    @GetMapping("/JMReportApi")
    public String  getJmReportApi(HttpServletRequest request,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10000") Integer pageSize,
                                     @RequestParam(name = "printAll", required = false) String printAll)
            throws Exception {
        /*获取默认值*/
        List<SqlRow> sqlResult = jmReportDao.findJmDefaultInfo(params);
        getDeFaultParam(sqlResult);
        /*获取查询条件*/
        fun(request);
        /*获取法人权限*/
        if (MapUtils.isNotEmpty(PubParams)) {
            if (PubParams.containsKey("loginuser")) {
                params.put("sys_user_loginname", PubParams.get("loginuser"));
            }
        }
        if (PubParams.containsKey("click")) { //如果是点击跳转值
            params.putAll(PubParams);
        }
        JSONObject jsonObject = new JSONObject();
        /*获取查询SQL*/
        SqlRow sql = jmReportDao.selectReportSql(params);
        String str = sql.getString("report_sql");
        /*执行SQL*/
        params.put("newSql", str);
        List<SqlRow> sqlResult1 = jmReportDao.selectSqlInfos(str, params);
        /*自定义分页插件*/
        pageNo = pageNo - 1;
        int fromIndex = pageNo * pageSize;
        int count = sqlResult1.size();
        int toIndex = (pageNo + 1) * pageSize;
        if (toIndex > count) {
            toIndex = count;
        }
        int total = count / pageSize;
        //List<SqlRow> list = sqlResult1.getRows().subList(fromIndex, toIndex);
        //jsonObject.put("data", list);
        //jsonObject.put("total", total + 1);
        //jsonObject.put("count", count);

        String Stringjson="";
        int totalpage=total + 1;
        //Stringjson="{\"data\":"+getData(sqlResult1)+"\",total\":"+totalpage+",\"count\":"+count+"}";
        return Stringjson;
    }

    @GetMapping("/querySQLApi")
    public String getQuerySqlApi(HttpServletRequest request) throws Exception {

        if (MapUtils.isNotEmpty(PubParams)) {
            if (PubParams.containsKey("loginuser")) {
                params.put("sys_user_loginname", PubParams.get("loginuser"));
            }
        }
        /*获取查询SQL*/
        params.put("menuid", params.get("id").toString());
        SqlRow sql = jmReportDao.selectQuerySql(params);
        String str = sql.getString("query_sql");
        params.put("newSql", str);
        List<SqlRow> sqlResult1 = jmReportDao.selectSqlInfos(str, params);

       // return sqlResult1.getRows();
        return ResulttoString(sqlResult1);
    }


    /**
     * 返回数据字典
     * @param dictCode 字典值code（是报表动态报表配置明细的字段名称）
     */
    @GetMapping("/getDict")
    public String getDictSex(@RequestParam(name="dictCode") String dictCode) throws Exception {
       String sql = "select itemkey as value,itemval as text from sys_dict_item where dict='" +dictCode+ "'";
       List<SqlRow> sqlRows = jmReportDao.selectSqlInfos(sql);
       return ResulttoString(sqlRows);
    }

    public String ResulttoString(List<SqlRow> sqlRows) throws Exception{
        String jsonArrayStr="[";
        for(SqlRow sqlRow : sqlRows) {
            jsonArrayStr=jsonArrayStr+"{";
            jsonArrayStr=jsonArrayStr+"\"text\":\""+sqlRow.getString("text")+"\",\"value\":\""+sqlRow.getString("value")+"\"";
            jsonArrayStr=jsonArrayStr+"}";
            if (sqlRows.size() == 1) {
                jsonArrayStr = jsonArrayStr+",";
            }
        }
        jsonArrayStr=jsonArrayStr+"]";

        return jsonArrayStr;
    }


//    public String getData(SqlResult sr) throws Exception {
//
//        JSONArray array = new JSONArray();
//        JSONObject object = new JSONObject();
//        List<String> columns=sr.getColumns();
//        while (sr != null && sr.next()) {
//            for(String cl:columns) {
//                String name=cl.toUpperCase();
//                object.put(name, sr.getString(name));
//            }
//            array.add(object);
//        }
//        return JSON.toJSONString(array);
//    }

//    public void getControl() throws Exception {
//        /*获取控制值*/
//        SqlResult control = comnDao.exeQuery("JMREPORT0016", params);
//        if (!ObjectUtils.isEmpty(control.getRows()) && control.getRows().size() == 1) {
//            params.put("conTrlCount", control.getRows().get(0).get("count"));
//        }
//    }

    public void getDeFaultParam(List<SqlRow> sqlRows) {
        for (SqlRow sqlRow: sqlRows) {
            params.put(sqlRow.get("defaultname").toString(), sqlRow.get("defaultvalue"));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getCount.json")
    public String getCount() {
        try {
            JSONObject jsonObject = new JSONObject();
            params.put("id", params.get("menuid"));
            PubParams = RequestSupport.getParameters();
            List<SqlRow> count = jmReportDao.selectSQLDICTCOUNT(params);
            if (!count.isEmpty()) {
                if (count.size() == 1) {
                    JSONArray rowArr = new JSONArray();
                    jsonObject.put("rows", rowArr);
                    jsonObject.put("count", count.get(0).getString("jimu_report_id"));
                    jsonObject.put("results", 0);
                }
            }
            return jsonObject.toString();
        } catch (Exception e) {
            return updateFailure("Count查询失败");
        }
    }


    @ResponseBody
    @RequestMapping(value = "/getEditParams.json")
    public String getEditParams() {
        try {
            JSONObject jsonObject = new JSONObject();
            PubParams = RequestSupport.getParameters();
            funEditParams(PubParams, jsonObject);
            return jsonObject.toString();
        } catch (Exception e) {
            return updateFailure("SQL保存失败");
        }
    }

    public void funEditParams(Map<String, Object> map, JSONObject jsonObject) {
        if (MapUtils.isNotEmpty(map)) {
            JSONArray rowArr = new JSONArray();
            jsonObject.put("rows", rowArr);
            jsonObject.put("results", 0);
        }
    }

    @ResponseBody
    @GetMapping("/exportPdf.josn")
    public JSONObject exportPdf(HttpServletResponse response) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String savePath = this.getPath();
        //进来先删除文件夹里面之前打印的文件
        File dir = new File(savePath);
        try {
            deleteFile(dir);
        } catch (Exception e) {
        }
        ;
        //配置打印路径
        File printFile = new File(savePath);
        //log.info(String.valueOf(params));
        //创建url路径
        //String url = "http://127.0.0.1:9999/jmreport/exportPdf";
        String url = params.get("url").toString() + "jmreport/exportPdf";
        Map<String, Object> map = new HashMap<>();
        JSONObject object = new JSONObject();
        params.get("excelConfigId");
        String id = (String) params.get("ids");
        String[] ids = id.split(",");
        for (int i = 0; i < ids.length; i++) {
            //此处为示例，需要传递api和sql的正确参数值
            //queryParam中有个特殊属性 dpi每英寸点数,windows是96，可不用传
            object.put("dpi", "96");
            object.put("t8_profit_loss_id", ids[i]);
            map.put("excelConfigId", params.get("excelConfigId"));
            map.put("queryParam", object);
            HttpHeaders headers = new HttpHeaders();
            //如果有字典需要传token
            headers.add("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjY2NjAzNzcsInVzZXJuYW1lIjoiYWRtaW4ifQ.6VdYrH2UChHeJLLKLiiuOu1Mos40CIN3zbigyI2O978");
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
            String apiResult = restTemplate.postForObject(url, httpEntity, String.class);
            JSONObject jsonObject = JSONObject.parseObject(apiResult);
            // jsonObject.put("results",jsonObject.get("result"));
            JSONObject result = jsonObject.getJSONObject("result");
            //获取64base
            String file = result.getString("file");
            //文件名称
            String name = ids[i] + result.getString("name");
            //64base转换成文件并保存文件 固定路径D:\print\
            base64ToFile(file, name, savePath);
        }

        //获取D:\print文件夹里的打印文件，并打印。
        this.filesDirs(printFile);
        JSONObject jsonObjects = new JSONObject();
        jsonObjects.put("results", "打印成功");
        jsonObjects.put("rows", 1);
        return jsonObjects;

    }

    /**
     * A1413存款期限结构及相关业务情况表下载报送文件
     *
     * @param  response
     * @param  reportDate
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importTemplate/downloadA1413.json",produces = {"application/json;charset=UTF-8"})
    public void downloadA1413(HttpServletResponse response, @RequestParam(value = "reportDate") String reportDate) throws Exception {
        // DAT文件写入
        File tempDat = File.createTempFile("prefix", ".DAT");
        File fileDat = new File(tempDat.getParent() + File.separator + "BJk4090100000" + reportDate + "431.DAT");

        List<SqlRow> listDat = appAssetA1413DepStrucService.findAppAssetA1413DepStrucsByExeId(reportDate, "R107QU01");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileDat))) {
            for (int i = 0; i < listDat.size(); i++) {
//                if (i > 0) writer.newLine();
                SqlRow sqlRow = listDat.get(i);
                if(i == listDat.size()-1){
                    writer.write(sqlRow.getString("column_info"));
                }else{
                    writer.write(sqlRow.getString("column_info")+"\r\n");
                }


            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // IDX文件写入
        File tempIdx = File.createTempFile("prefix", ".IDX");
        File fileIdx = new File(tempIdx.getParent() + File.separator + "BIk4090100000" + reportDate + "431.IDX");

        List<SqlRow> listIdx = appAssetA1413DepStrucService.findAppAssetA1413DepStrucsByExeId(reportDate, "R107QU02");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileIdx))) {
            for (int i = 0; i < listIdx.size(); i++) {
//                if (i > 0) writer.newLine();
                SqlRow sqlRow = listIdx.get(i);
                if(i == listDat.size()-1){
                    writer.write(sqlRow.getString("column_info"));
                }else{
                    writer.write(sqlRow.getString("column_info")+"\r\n");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // 压缩文件
        File zipFile = File.createTempFile("prefix", ".zip");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            FileUtil.compress(fileDat, zipOutputStream, "");
            FileUtil.compress(fileIdx, zipOutputStream, "");
            // 下载文件
            zipOutputStream.close();
            String fileName = "BIk4090100000" + reportDate + "431.zip";
            FileUtil.downloadFile(zipFile.getAbsolutePath(), fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.delFile(tempDat);
            FileUtil.delFile(fileDat);
            FileUtil.delFile(tempIdx);
            FileUtil.delFile(fileIdx);
            FileUtil.delFile(zipFile);
        }
    }

    /**
     * 境外投资资产情况表导出
     *
     * @param  response
     * @param  reportDate
     * @param  year
     * @param  month
     * @param  monthStr
     * @param  day
     * @param  fileName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importTemplate/downloadInvestinfo.json",produces = {"application/json;charset=UTF-8"})
    public void downloadInvestinfo(HttpServletResponse response,
                                   @RequestParam(value = "reportDate") String reportDate,
                                   @RequestParam(value = "year") int year,
                                   @RequestParam(value = "month") int month,
                                   @RequestParam(value = "month") String monthStr,
                                   @RequestParam(value = "day") int day, @RequestParam(value = "fileName") String fileName) throws Exception {
        try {
            File templaFile = File.createTempFile("prefix", ".xlsx");
            File outputFile = File.createTempFile("prefix", ".xlsx");
            String outputFilePath = outputFile.getAbsolutePath();
            ClassPathResource classPathResource = new ClassPathResource("static/" + fileName);

            String dateStr = month == 1 ? month+"" : 1+"-"+month;
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", reportDate);

            // 1、查询资管公司境外投资情况表
            List<SqlRow> sqlRowList = appOverseasInvInfoService.findAppOverseasInvInfos(params);
            // 2、查询境外投资情况明细表总体收益情况1信息
            List<SqlRow> sqlRowList1 = appOverseasInvInfo1Service.findAppOverseasInvInfo1s(params);
            // 3、查询境外投资情况明细表总体收益情况2信息
            List<SqlRow> sqlRowList2 = appOverseasInvInfo2Service.findAppOverseasInvInfo2s(params);
            // 4、填充到excl模板文件并返回
            try (FileInputStream fis = convert(templaFile, classPathResource.getInputStream());
                 XSSFWorkbook workbook = new XSSFWorkbook(fis);
                 FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                // sheet1：查询资管公司境外投资情况表
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowNum = 4; // 第5行，索引从0开始

                Font font = workbook.createFont();
                font.setBold(true);
                font.setFontName("仿宋");
                font.setFontHeightInPoints((short) 14);
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFont(font);

                Font font1 = workbook.createFont();
                font1.setFontName("仿宋");
                font1.setFontHeightInPoints((short) 12);
                DataFormat format = workbook.createDataFormat();
                CellStyle cellStyle01 = workbook.createCellStyle();
                cellStyle01.setFont(font1);
                cellStyle01.setDataFormat(format.getFormat("0.00"));
                cellStyle01.setBorderTop(BorderStyle.THIN);
                cellStyle01.setBorderBottom(BorderStyle.THIN);
                cellStyle01.setBorderLeft(BorderStyle.THIN);
                cellStyle01.setBorderRight(BorderStyle.THIN);

                CellStyle cellStyle1 = workbook.createCellStyle();
                cellStyle1.setFont(font1);
                cellStyle1.setBorderTop(BorderStyle.THIN);
                cellStyle1.setBorderBottom(BorderStyle.THIN);
                cellStyle1.setBorderLeft(BorderStyle.THIN);
                cellStyle1.setBorderRight(BorderStyle.THIN);
                // 数据日期填充
                Row dateRow = sheet.getRow(2);
                if (dateRow != null) {
                    Cell dateCell = dateRow.createCell(0);
                    dateCell.setCellStyle(cellStyle);
                    dateCell.setCellValue("数据截至"+year+"年"+month+"月"+day+"日");
                }
                for (SqlRow sqlRow : sqlRowList) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) row = sheet.createRow(rowNum);
                    for (int i = 0;i < 32;i++) {
                        Cell cell = row.createCell(i); // 第一列，索引从0开始
                        if ((i == 24 || i == 25) && !ObjectUtils.isEmpty(sqlRow.get("c"+i))) { // 金额字段显示为数字类型
                            cell.setCellStyle(cellStyle01);
                            cell.setCellValue(sqlRow.getDouble("c"+i));
                        } else {
                            cell.setCellStyle(cellStyle1);
                            cell.setCellValue(sqlRow.getString("c"+i));
                        }
                    }
                    rowNum++;
                }
                // sheet2：查询境外投资情况明细表总体收益情况1信息
                XSSFSheet sheet1 = workbook.getSheetAt(1);
                int rowNum1 = 2; // 第3行，索引从0开始

                Font font2 = workbook.createFont();
                font2.setBold(true);
                font2.setFontName("宋体");
                font2.setFontHeightInPoints((short) 11);
                CellStyle cellStyle2 = workbook.createCellStyle();
                cellStyle2.setFont(font2);
                cellStyle2.setWrapText(true);
                cellStyle2.setBorderTop(BorderStyle.THIN);
                cellStyle2.setBorderBottom(BorderStyle.THIN);
                cellStyle2.setBorderLeft(BorderStyle.THIN);
                cellStyle2.setBorderRight(BorderStyle.THIN);

                Font font3 = workbook.createFont();
                font3.setFontName("宋体");
                font3.setFontHeightInPoints((short) 11);
                CellStyle cellStyle03 = workbook.createCellStyle();
                cellStyle03.setFont(font3);
                cellStyle03.setDataFormat(format.getFormat("0.00"));
                cellStyle03.setBorderTop(BorderStyle.THIN);
                cellStyle03.setBorderBottom(BorderStyle.THIN);
                cellStyle03.setBorderLeft(BorderStyle.THIN);
                cellStyle03.setBorderRight(BorderStyle.THIN);

                CellStyle cellStyle3 = workbook.createCellStyle();
                cellStyle3.setFont(font3);
                cellStyle3.setBorderTop(BorderStyle.THIN);
                cellStyle3.setBorderBottom(BorderStyle.THIN);
                cellStyle3.setBorderLeft(BorderStyle.THIN);
                cellStyle3.setBorderRight(BorderStyle.THIN);
                // 数据日期填充
                Row dateRow1 = sheet1.getRow(1);
                if (dateRow1 != null) {
                    Cell dateCell1_2 = dateRow1.createCell(2);
                    Cell dateCell1_3 = dateRow1.createCell(3);
                    dateCell1_2.setCellStyle(cellStyle2);
                    dateCell1_3.setCellStyle(cellStyle2);
                    dateCell1_2.setCellValue("投资资产余额"+year+monthStr+"（万元）");
                    dateCell1_3.setCellValue("加权累计净值增长率"+year+"年"+dateStr+"月（%）");
                }
                Row dateRow2 = sheet1.getRow(3);
                if (dateRow2 != null) {
                    Cell dateCell2_2 = dateRow2.createCell(2);
                    Cell dateCell2_3 = dateRow2.createCell(3);
                    dateCell2_2.setCellStyle(cellStyle2);
                    dateCell2_3.setCellStyle(cellStyle2);
                    dateCell2_2.setCellValue("投资资产余额"+year+monthStr+"（万元）");
                    dateCell2_3.setCellValue("投资收益率"+year+"年"+dateStr+"月（%）");
                }
                for (SqlRow sqlRow : sqlRowList1) {
                    Row row = sheet1.getRow(rowNum1);
                    if (row == null) row = sheet1.createRow(rowNum1);
                    for (int i = 2;i < 4;i++) {
                        Cell cell = row.createCell(i); // 第3列，索引从0开始
                        if ((i == 2 || i == 3) && !ObjectUtils.isEmpty(sqlRow.get("c"+i))) { // 金额、增长率显示为数字类型
                            cell.setCellStyle(cellStyle03);
                            cell.setCellValue(sqlRow.getDouble("c"+i));
                        } else {
                            cell.setCellStyle(cellStyle3);
                            cell.setCellValue(sqlRow.getString("c"+i));
                        }
                    }
                    rowNum1++;
                }
                // sheet3：查询境外投资情况明细表总体收益情况2信息
                XSSFSheet sheet2 = workbook.getSheetAt(2);
                int rowNum2 = 2; // 第3行，索引从0开始

                Font font4 = workbook.createFont();
                font4.setBold(true);
                font4.setFontName("宋体");
                font4.setFontHeightInPoints((short) 11);
                CellStyle cellStyle4 = workbook.createCellStyle();
                cellStyle4.setFont(font4);
                cellStyle4.setWrapText(true);
                cellStyle4.setBorderTop(BorderStyle.THIN);
                cellStyle4.setBorderBottom(BorderStyle.THIN);
                cellStyle4.setBorderLeft(BorderStyle.THIN);
                cellStyle4.setBorderRight(BorderStyle.THIN);

                Font font5 = workbook.createFont();
                font5.setFontName("宋体");
                font5.setFontHeightInPoints((short) 11);
                CellStyle cellStyle05 = workbook.createCellStyle();
                cellStyle05.setFont(font5);
                cellStyle05.setDataFormat(format.getFormat("0.00"));
                cellStyle05.setBorderTop(BorderStyle.THIN);
                cellStyle05.setBorderBottom(BorderStyle.THIN);
                cellStyle05.setBorderLeft(BorderStyle.THIN);
                cellStyle05.setBorderRight(BorderStyle.THIN);

                CellStyle cellStyle5 = workbook.createCellStyle();
                cellStyle5.setFont(font5);
                cellStyle5.setBorderTop(BorderStyle.THIN);
                cellStyle5.setBorderBottom(BorderStyle.THIN);
                cellStyle5.setBorderLeft(BorderStyle.THIN);
                cellStyle5.setBorderRight(BorderStyle.THIN);
                // 数据日期填充
                Row dateRow3 = sheet2.getRow(1);
                if (dateRow3 != null) {
                    Cell dateCell3_2 = dateRow3.createCell(2);
                    Cell dateCell3_3 = dateRow3.createCell(3);
                    dateCell3_2.setCellStyle(cellStyle4);
                    dateCell3_3.setCellStyle(cellStyle4);
                    dateCell3_2.setCellValue("投资资产余额"+year+monthStr+"（万元）");
                    dateCell3_3.setCellValue("加权累计净值增长率"+year+"年"+dateStr+"月（%）");
                }
                for (SqlRow sqlRow : sqlRowList2) {
                    Row row = sheet2.getRow(rowNum2);
                    if (row == null) row = sheet2.createRow(rowNum2);
                    for (int i = 1;i < 4;i++) {
                        Cell cell = row.createCell(i); // 第2列，索引从0开始
                        if ((i == 2 || i == 3) && !ObjectUtils.isEmpty(sqlRow.get("c"+i))) { // 金额、增长率显示为数字类型
                            cell.setCellStyle(cellStyle05);
                            cell.setCellValue(sqlRow.getDouble("c"+i));
                        } else {
                            cell.setCellStyle(cellStyle5);
                            cell.setCellValue(sqlRow.getString("c"+i));
                        }
                    }
                    rowNum2++;
                }
                // sheet4：境外机构和人员情况
                XSSFSheet sheet3 = workbook.getSheetAt(3);

                Font font6 = workbook.createFont();
                font6.setBold(true);
                font6.setFontName("宋体");
                font6.setFontHeightInPoints((short) 16);
                CellStyle cellStyle6 = workbook.createCellStyle();
                cellStyle6.setFont(font6);
                cellStyle6.setWrapText(true);
                cellStyle6.setBorderTop(BorderStyle.THIN);
                cellStyle6.setBorderBottom(BorderStyle.THIN);
                cellStyle6.setBorderLeft(BorderStyle.THIN);
                cellStyle6.setBorderRight(BorderStyle.THIN);
                cellStyle6.setAlignment(HorizontalAlignment.CENTER);
                cellStyle6.setVerticalAlignment(VerticalAlignment.CENTER);

                // 数据日期填充
                Row dateRow0 = sheet3.getRow(0);
                if (dateRow0 != null) {
                    Cell dateCell0_0 = dateRow0.createCell(0);
                    dateCell0_0.setCellStyle(cellStyle6);
                    dateCell0_0.setCellValue("境外机构和人员情况"+year+monthStr);
                }

                workbook.write(fos);
                FileUtil.downloadFile(outputFilePath, "资产管理机构境外投资情况表-浦银理财-"+reportDate+".xlsx", response);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                FileUtil.delFile(templaFile);
                FileUtil.delFile(outputFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 交易信息登记管理批量修改文件导出
     *
     * @param  response
     * @param  reportDate
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importTemplate/downloadProdTransRegist.json",produces = {"application/json;charset=UTF-8"})
    public void downloadProdTransRegist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("id", request.getParameter("id"));
            List<SqlRow> sqlRows = prodTransRegistInfoDao.findImportMenuFileManage(param);
            if (CollectionUtil.isNotEmpty(sqlRows)) {
                String fileName = (String) sqlRows.get(0).get("file_name");
                String localFilePath = (String) sqlRows.get(0).get("local_file_path");
                // 从oss下载
                File tmpFile = File.createTempFile("prefix", fileName);
                File file = new File(tmpFile.getParent() + File.separator + fileName);
                FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
                fileTransfer.downloadFileAndDisconnect(localFilePath, file.getAbsolutePath());
                // 返回前端
                FileUtil.downloadFile(file.getAbsolutePath(), fileName, response);
                FileUtil.delFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 非法人财务数据报表导出
     *
     * @param  response
     * @param  reportDate
     * @param  dtDt
     * @param  prdcNm
     * @throws Exception
     */
    @RequestMapping(value = "/exportTemplate/downloadUnincorporatedEntity.json",produces = {"application/json;charset=UTF-8"})
    public void downloadUnincorporatedEntity(HttpServletResponse response,
                                             @RequestParam(value = "reportDate") String reportDate,
                                             @RequestParam(value = "dtDt") String dtDt, @RequestParam(value = "prdcNm") String prdcNm) throws Exception {
        try {
            // 查询当前符合条件的非法人财务数据
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", reportDate);
            params.put("dtDt", dtDt);
            params.put("prdcNm", prdcNm);

            List<SqlRow> appAssetUnincorporatedEntitys = appAssetUnincorporatedEntityService.queryAppAssetUnincorporatedEntitys(params);

            // 查询远程模板的地址并下载
            String sql = "select a.template_name, a.template_file_name, a.template_file_path, a.oss_file_path, a.row_start " +
                    "       from import_template_manage a " +
                    "       where exists(select 1 from app_table_info b " +
                    "                   where a.system_table_name = b.id " +
                    "                     and b.system_table_name = 'app_asset_unincorporated_entity') " +
                    "       order by a.imp_date desc limit 1 ";
            List<SqlRow> sqlRows = comnDao.findRows(sql);

            String local_file_path = "";
            String oss_file_path = "";

            String row_start = "";
            String template_name = "";

            String template_file_path = "";
            String template_file_name = "";
            if (sqlRows != null && sqlRows.size() > 0) {
                row_start = sqlRows.get(0).getString("row_start");
                template_name = sqlRows.get(0).getString("template_name");

                template_file_path = sqlRows.get(0).getString("template_file_path");
                template_file_name = sqlRows.get(0).getString("template_file_name");

                local_file_path = template_file_path + template_file_name;
                oss_file_path = sqlRows.get(0).getString("oss_file_path");
            }

            String[] fileNameArr = template_file_name.split("\\.");
            String suffix = StringUtils.isEmpty(template_file_name) ? ".xlsx" : "." + fileNameArr[fileNameArr.length - 1];
            File localFile = new File(local_file_path);
            File outputFile = File.createTempFile("prefix", suffix);
            String outputFilePath = outputFile.getAbsolutePath();
            if (!localFile.exists() && Tools.isNotEmpty(oss_file_path)) {
                if(!localFile.getParentFile().exists()) {
                    localFile.getParentFile().mkdirs();
                }
                FileTransfer transfer = FileTransferHelpler.getTransfer();
                transfer.downloadFileAndDisconnect(oss_file_path, local_file_path);
            }

            // 查询远程模板的配置字段
            String sql1 = "select a.database_column_code, a.template_column_serial " +
                    "       from import_template_manage_field_01 a " +
                    "       where exists(select 1 from app_table_info b " +
                    "                   where a.system_table_name = b.id " +
                    "                     and b.system_table_name = 'app_asset_unincorporated_entity') " +
                    "       order by a.template_column_serial ";
            List<SqlRow> field01 = comnDao.findRows(sql1);

            // 写入文件流并返回
            try (FileInputStream fis = new FileInputStream(localFile);
                 Workbook workbook = WorkbookFactory.create(fis);
                 FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                // Sheet1
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
                int rowNum = Integer.parseInt(row_start);

                Font font = workbook.createFont();
                font.setBold(false);
                font.setFontName("等线");
                font.setFontHeightInPoints((short) 11);
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFont(font);

                for (SqlRow sqlRow : appAssetUnincorporatedEntitys) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) row = sheet.createRow(rowNum);
                    for (int i = 0;i < field01.size();i++) {
                        Cell cell = row.createCell(i); // 第一列，索引从0开始
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(sqlRow.getString(field01.get(i).getString("database_column_code").toLowerCase()));
                    }
                    rowNum++;
                }

                workbook.write(fos);
                FileUtil.downloadFile(outputFilePath, template_name + reportDate + suffix, response);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                FileUtil.delFile(localFile);
                FileUtil.delFile(outputFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileInputStream convert(File tempFile, InputStream inputStream) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            int length;
            byte[] buffer = new byte[1024];
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
        return new FileInputStream(tempFile);
    }

    /**
     * 通过本机默认打印机打印pdf文件
     *
     * @throws Exception
     */
    public void defaultPrintPDF(File file) throws Exception {
        //File file = new File("D:\\基金投资交易明细台账.pdf"); // 获取选择的文件
        // 构建打印请求属性集
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        // 设置打印格式，因为未确定类型，所以选择autosense
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        log.info("打印文件类型为：===================" + flavor);
        //pras.add(MediaName.ISO_A4_TRANSPARENT);//A4纸张
        //遍历
        PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);

        for (PrintService printService2 : printService) {
            log.info("本机可使用打印机列表：===================" + printService2);
        }
        // 定位默认的打印服务
        PrintService defaultService = PrintServiceLookup
                .lookupDefaultPrintService();
        //显示打印对话框
        //PrintService service = ServiceUI.printDialog(null, 200, 200, printService,
        //        defaultService, flavor, pras);
        log.info("打印工具选择打印机为：===================" + defaultService);
        try {
            DocPrintJob job = defaultService.createPrintJob(); // 创建打印作业
            FileInputStream fis = new FileInputStream(file); // 构造待打印的文件流
            DocAttributeSet das = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(fis, flavor, das);
            job.print(doc, pras);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("打印异常", e);
            throw new Exception();
        }
    }

    /**
     * 把base64转化为文件.
     *
     * @param base64 base64
     * @return boolean isTrue
     */
    public static void base64ToFile(String base64, String fileName, String savePath) {
        File file = null;
        //创建文件目录
        String filePath = savePath;
        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //使用递归遍历文件夹及子文件夹中文件
    public void filesDirs(File file) throws Exception {
        //File对象是文件或文件夹的路径，第一层判断路径是否为空
        if (file != null) {
            //第二层路径不为空，判断是文件夹还是文件
            if (file.isDirectory()) {
                //进入这里说明为文件夹，此时需要获得当前文件夹下所有文件，包括目录
                File[] files = file.listFiles();//注意:这里只能用listFiles()，不能使用list()
                //files下的所有内容，可能是文件夹，也可能是文件，那么需要一个个去判断是文件还是文件夹，这个判断过程就是这里封装的方法
                //因此可以调用自己来判断，实现递归
                for (File flies2 : files) {
                    filesDirs(flies2);
                }
            } else {
                System.out.println("文件名字" + file);
                defaultPrintPDF(file);
            }
        } else {
            System.out.println("文件不存在");
        }


    }

    /**
     * File[] listFiles()
     * 递归删除。
     */
    private static void deleteFile(File file) throws IOException {
        /**
         * File[] listFiles()
         *  返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件。
         */
        File[] files = file.listFiles();
        if (files != null) {//如果包含文件进行删除操作
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    //删除子文件
                    files[i].delete();
                } else if (files[i].isDirectory()) {
                    //通过递归的方法找到子目录的文件
                    deleteFile(files[i]);
                }
                files[i].delete();//删除子目录
            }
        }
    }

    public String getPath() {
        String path = "";
        String sqlid = "";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            sqlid = "select paravalue from sys_param where moduleid = '8' and paraid = '80000080121'";
        } else {
            sqlid = "select paravalue from sys_param where moduleid = '8' and paraid = '80000080122'";
        }
        Map<String, Object> paraMap = new HashMap<>();
        try {
            SqlRow rst = jmReportDao.selectSqlInfo(sqlid, paraMap);
            path = rst.getString("paravalue");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        if (!"/".equals(path.substring(path.length() - 1))) {
            path = path + "/";
        }
        return path;
    }
}
