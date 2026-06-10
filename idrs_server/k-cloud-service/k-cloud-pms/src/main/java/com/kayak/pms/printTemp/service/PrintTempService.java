package com.kayak.pms.printTemp.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.printTemp.dao.PrintTempDao;
import com.kayak.pms.printTemp.dao.PrintTempVersionDao;
import com.kayak.pms.printTemp.model.PrintTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: k-cloud
 * @description: 文档模板信息服务
 * @author: WangZhenXin
 * @create: 2020-12-26 11:44
 * @memo 备注信息
 */
@Service
@APIDefine(desc = "文档模板信息服务",model = PrintTemp.class)
public class PrintTempService {
    private static final Logger logger = LoggerFactory.getLogger(PrintTempService.class);

    @Autowired
    private PrintTempDao printTempDao;

    @Autowired
    private PrintTempVersionDao printTempVersionDao;
    @Autowired
    private PrintTempVersionService printTempVersionService;


    @API(desc = "查询文档模板",auth = APIAuth.YES,operation = APIOperation.SELECT)
    public SqlResult<PrintTemp> getPrintTempList1(SqlParam<PrintTemp> param) throws Exception {
        return getPrintTempList(param);
    }

    @API(desc = "查询文档模板",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<PrintTemp> getPrintTempList(SqlParam<PrintTemp> param) throws Exception {
        param.setMakeSql(true);
        SqlResult<PrintTemp> printTempSqlResult = printTempDao.find(param);
        //判非空
        if(printTempSqlResult.getRows() != null && !printTempSqlResult.getRows().isEmpty()){
            for(PrintTemp printTemp : printTempSqlResult.getRows()){
                PrintTemp newPrintTempVersion = printTempDao.findPrintTempVersionsByPrintId(printTemp.getId());
                printTemp.setStatus(null != newPrintTempVersion? newPrintTempVersion.getStatus():null);
                printTemp.setTempVersionUpdateDate(null != newPrintTempVersion? newPrintTempVersion.getTempVersionUpdateDate():null);
                printTemp.setTempVersionUpdateTime(null != newPrintTempVersion? newPrintTempVersion.getTempVersionUpdateTime():null);
            }
        }
        return printTempSqlResult;
    }

    @API(desc = "上传文档模版",auth = APIAuth.YES,operation = APIOperation.INSTER)
    public String savePrintTempInfo(PrintTemp printTemp) throws Exception {
        //设置创建时间(保存文档模板相关数据到数据库)
        Date now = new Date();
        printTemp.setCreateDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        //保存文档模板
        return printTempDao.savePrintTemp(printTemp);
    }
    
    @API(desc = "修改文档模版",auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public String updatePrintTempInfo(SqlParam<PrintTemp> param) throws Exception {
        int count = printTempDao.updatePrintTempInfo(param.getModel());
        if(count<1){
            return RequestSupport.updateReturnJson(false, "修改模板失败", null).toString();
        }else{
            return RequestSupport.updateReturnJson(true, "修改模板成功", null).toString();
        }
    }

    @API(desc = "上传文档模板子模版",auth = APIAuth.YES,operation = APIOperation.INSTER)
    public void savePrintTempVersion(SqlParam<PrintTemp> param){}

    @API(desc = "根据文档类型获取模板类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getTempTypeByDocType(SqlParam<PrintTemp> param) throws Exception {
//        FetcherData fetcherData = (FetcherData) param;
//        fetcherData.getParamsDirect().get("temp_type");
        String docType = param.getModel().getDocType();
        List<SqlRow> tempTypeByDocType = printTempDao.getTempTypeByDocType(docType);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }

    /**
     * 校验文档信息是否存在，若存在则返回文档模板id,若不存在则先新增在返回id
     * @param printTemp 文档模板对象
     * @return printTempId
     */
    @API(desc = "校验文档模板基础信息是否存在",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public Integer checkPrintTemp(PrintTemp printTemp){
        return printTempDao.checkPrintTemp(printTemp);
    }

    /**
     * 校验文档信息是否存在，若存在则返回文档模板id,若不存在则先新增在返回id
     * @param printTemp 文档模板对象
     * @return printTempId
     */
    @API(desc = "校验审批流文档模板基础信息是否存在",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public String checkFlowPrintTemp(SqlParam<PrintTemp> printTemp){
        Integer count = printTempDao.checkFlowPrintTemp(printTemp.getModel());
        if(count>0){
            return RequestSupport.updateReturnJson(false, "上传文档模板失败,已存在该模板", null).toString();
        }else{
            return RequestSupport.updateReturnJson(true, "文件上传成功", null).toString();
        }
    }




}
