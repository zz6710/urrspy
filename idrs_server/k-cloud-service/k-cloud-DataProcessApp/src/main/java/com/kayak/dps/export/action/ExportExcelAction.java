package com.kayak.dps.export.action;

import com.alibaba.fastjson.JSON;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.dps.export.dao.ExportExcelDao;
import com.kayak.dps.export.util.ExportExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * 描述：导出文件action
 *
 * @author zhaojie
 */
@RestController
public class ExportExcelAction {

    private static final Logger log = LoggerFactory.getLogger(ExportExcelAction.class);

    @Autowired
    private ExportExcelDao exportExcelDao;


    /**
     * 描述：excel导出
     *
     */
    @RequestMapping(value = "/expoertExcel.json")
    public void expoertExcel(HttpServletResponse response) throws Exception {
        StringBuilder colStr = new StringBuilder();
        String tableName = "";
        String tableComment = "";
        String tableSchema = "";//数据源
        Map<String, Object> params = RequestSupport.getParameters();
        Map<String,String> pathParams = new HashMap<>();
        List<Map<String, String>> filePaths = new ArrayList<>();
        pathParams.put("win","70000010002");
        pathParams.put("os","70000010009");
        String rootPath = ExportExcelUtil.getRootPath(pathParams) + UUID.randomUUID().toString().replace("-","").toUpperCase() + "/";
        String searchColumnJson = (String) params.get("searchColumn");
        List<Map> paramsLt = JSON.parseArray(searchColumnJson,Map.class);
        tableName = (String) params.get("tableName");
        //获取表名注释
        List<SqlRow> tableInfo = exportExcelDao.getTableComment(tableName);
        for (SqlRow sqlRow:tableInfo) {
            tableComment = (String) sqlRow.get("tableComment");
            tableSchema = (String) sqlRow.get("tableSchema");
        }
        if(tableComment.isEmpty()) {
            tableComment = tableName;
        } else {
            tableComment = tableName + "-" + tableComment;
        }
        //获取字段
        List<SqlRow>  fieldList = exportExcelDao.getCols(tableName,tableSchema);
        List<String> headList = new ArrayList<>();
        for (SqlRow sqlRow:fieldList) {
            colStr.append(sqlRow.get("col")).append(",");
            String header = sqlRow.getString("col") + "\n" + sqlRow.getString("colName");
            headList.add(header);
        }
        if(colStr.length() > 0) {
            colStr = new StringBuilder(colStr.substring(0, colStr.length() - 1));
        }
        StringBuilder sql = new StringBuilder("select * from " + tableName + " where 1=1 ");
        // 拼接sql查询条件
        appendSql(paramsLt, sql);
        // 拼接limit
        String limit = (String) params.get("limit");
        String offset = (String) params.get("offset");
        appendLimit(sql, limit, offset);
        StringBuilder sqlCount = new StringBuilder(" select count(*) count from (").append(sql).append(") t");
        // 查询表所有字典
        List<SqlRow>  dictList = exportExcelDao.getDictListByTableName(tableName);
        String[] filedArr = colStr.toString().split(",");
        // 查询数据总数
        int count = Integer.parseInt(exportExcelDao.getDataCount(sqlCount.toString(),tableSchema));
        int maxRowNum = ExportExcelUtil.getMaxRowNum();
        BigDecimal countBig = new BigDecimal(count);
        BigDecimal maxRowNBig = new BigDecimal(maxRowNum);
        //生成文件个数
        int fileNum = (int) Math.ceil(countBig.divide(maxRowNBig).doubleValue());
        fileNum = fileNum == 0 ? 1 : fileNum;
        for (int i = 0; i < fileNum; i++) {
            List<List<String>> dataList = new ArrayList<>();
            //分页查询生成多个文件,最大行数读取参数表10000
            String pageSql = "select * from (" + sql + ") s limit " + maxRowNBig.multiply(new BigDecimal(i)) + "," + maxRowNum;
            List<SqlRow>  dataLt = exportExcelDao.getDatas(pageSql, tableSchema);
            for (SqlRow sqlRow:dataLt) {
                List<String> rowList = new ArrayList<>();
                for (String s : filedArr) {
                    //遍历匹配字段字典值
                    getFieldDict(s, dictList, sqlRow.get(s), rowList);
                }
                dataList.add(rowList);
            }
            ExportExcelUtil.setSheet(tableComment);
            ExportExcelUtil.createHead(headList);
            ExportExcelUtil.createContent(dataList);
            File file = new File(rootPath);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            String createFileName = tableComment + "_" + (i+1) + ".xls";
            ExportExcelUtil.writeToFile(rootPath + createFileName);
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("filePath",rootPath + createFileName);
            fileMap.put("fileName",createFileName);
            filePaths.add(fileMap);
        }
        if (filePaths.size() == 1) {
            String fileName = filePaths.get(0).get("fileName");
            String filePath = filePaths.get(0).get("filePath");
            ExportExcelUtil.downloadFile(filePath ,fileName ,response);
        } else {
            String zipFileName = tableComment + ".zip";
            ExportExcelUtil.createZip(filePaths,rootPath+zipFileName,false);
            ExportExcelUtil.downloadFile(rootPath + zipFileName ,zipFileName ,response);
            ExportExcelUtil.deleteFolder(new File(rootPath));
        }
    }

    private void appendLimit(StringBuilder sql, String limit, String offset) {
        if (Tools.isEmpty(limit)) {
            limit = "0";
        }
        if (Tools.isEmpty(offset)) {
            offset = "10000";
        }
        sql.append(" limit ").append(offset).append(",").append(limit);
    }

    /**拼接sql查询条件*/
    private void appendSql(List<Map> paramsLt , StringBuilder sql) {
        String sqlTmp = "";
        for (Map map: paramsLt) {
            if(Tools.isNotEmpty((String) map.get("symbolF"))) {
                switch (map.get("symbolF").toString()) {
                    case "01":
                        sqlTmp = " and " +  map.get("nameF") + " < '" + map.get("valueF")+"' ";
                        break;
                    case "02":
                        sqlTmp =  " and " +  map.get("nameF") + " <= '" + map.get("valueF")+"' " ;
                        break;
                    case "03":
                        sqlTmp = " and " +  map.get("nameF") + " = '" + map.get("valueF")+"' ";
                        break;
                    case "04":
                        sqlTmp = " and " +  map.get("nameF") + " >= '" + map.get("valueF")+"' ";
                        break;
                    case "05":
                        sqlTmp = " and " +  map.get("nameF") + " > '" + map.get("valueF")+"' ";
                        break;
                    case "06":
                        sqlTmp = " and " + map.get("nameF") + " like '%" + map.get("valueF") + "%' ";
                    default:
                }
                sql.append(sqlTmp);
            }
        }
    }

    /**获取字段的字典值*/
    private void getFieldDict(String field,  List<SqlRow>  DictList, Object str,List<String> rowList) {
        Boolean flag = false;
        for ( SqlRow sRow:DictList) {
            if(StringUtils.equals(sRow.getString("field"),field)){
                if (StringUtils.equals((String) sRow.get("itemkey"), String.valueOf(str))){
                    rowList.add((String) sRow.get("itemval"));
                    flag = true;
                    break;
                }
            }
        }
        if(!flag) rowList.add(String.valueOf(str));
    }
}
