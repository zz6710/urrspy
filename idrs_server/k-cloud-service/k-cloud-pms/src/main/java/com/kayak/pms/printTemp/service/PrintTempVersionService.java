package com.kayak.pms.printTemp.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.dao.DaoService;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.printTemp.dao.PrintTempVersionDao;
import com.kayak.pms.printTemp.model.PrintTempVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: k-cloud
 * @description: 文档模板版本Service
 * @author: WangZhenXin
 * @create: 2020-12-28 09:49
 * @memo 备注信息
 */
@Service
@APIDefine(desc = "文档模板版本服务", model = PrintTempVersion.class)
public class PrintTempVersionService {
    private static final Logger logger = LoggerFactory.getLogger(PrintTempVersionService.class);

    @Autowired
    private PrintTempVersionDao printTempVersionDao;

    @Autowired
    protected DaoService daoService;

    @API(desc = "保存文档模板版本及HtmlTemp",auth = APIAuth.NO)
    public String savePrintTempVersion(PrintTempVersion printTempVersion) throws Exception {
        //设置创建时间
        Date now = new Date();
        printTempVersion.setCreateDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        printTempVersion.setCreateTime(DateUtil.getNowTime());
        printTempVersion.setCreateUserId((String) SysUtil.getSysUserParamValue("sys_user_userid"));
        printTempVersion.setCreateUserName((String) SysUtil.getSysUserParamValue("sys_user_username"));
        //保存文档模板
        return printTempVersionDao.savePrintTempVersion(printTempVersion);
    }

    @API(desc = "根据模板Id获取文档模板版本列表",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<PrintTempVersion> getPrintTempVersionByTempId(SqlParam<PrintTempVersion> param) throws Exception {
        return printTempVersionDao.getPrintTempVersionByTempId(param);
    }

    //调整方法名称,避免权限扫描方法名冲突
    @API(desc = "获取最新的产品模板版本号",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> getNewestPrintTempVersion2(SqlParam<PrintTempVersion> param) throws Exception {
        PrintTempVersion printTempVersion = param.getModel();
        String t8PrintTempId = printTempVersion.getT8PrintTempId();
        List<SqlRow> newestPrintTempVersion = printTempVersionDao.getNewestPrintTempVersion(t8PrintTempId);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(newestPrintTempVersion.size());
        sqlRowSqlResult.setRows(newestPrintTempVersion);
        sqlRowSqlResult.setDesensitized(false);
        return sqlRowSqlResult;
    }
    @API(desc = "查询文档最新版本号",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public String getNewestPrintTempVersion(String t8PrintTempId) throws Exception {
        List<SqlRow> newestPrintTempVersion = printTempVersionDao.getNewestPrintTempVersion(t8PrintTempId);
        if (newestPrintTempVersion != null && newestPrintTempVersion.size() > 0) {
            return newestPrintTempVersion.get(0).getString("version");
        } else {
            throw new Exception("查询文档最新版本号失败");
        }
    }

    @API(desc = "文档模板版本生效",params = "id,status,effective_date" ,auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public String updatePrintTempVersionStatus(SqlParam<PrintTempVersion> param) throws Exception {
        String id = param.getModel().getId();
        Integer integer = printTempVersionDao.updatePrintTempVersionStatus(id,DateUtil.getTimestamp19());
        if (integer < 1) {
            throw new PromptException("修改失败");
        } else {
            return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
        }
    }
    
    @API(desc = "文档子模板修改",params = "id,remark,status,effective_date" ,auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public String updatePrintTempVersion(SqlParam<PrintTempVersion> param) throws Exception {
        String id = param.getModel().getId();
        Integer integer = printTempVersionDao.updatePrintTempVersion(id,param.getModel().getRemark());
        if (integer < 1) {
            throw new PromptException("修改失败");
        } else {
            return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
        }
    }
    
    @API(desc = "文档模板作废",auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public String deletePrintTempVersionStatus(SqlParam<PrintTempVersion> param) throws Exception {
        String id = param.getModel().getId();
        Integer integer = printTempVersionDao.deletePrintTempVersionStatus(id,DateUtil.getTimestamp19());
        if (integer < 1) {
            throw new PromptException("修改失败");
        } else {
            return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
        }
    }

    @API(desc = "根据id获取文档模板信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public PrintTempVersion getPrintTempVersionById(String id,String processInstanceId) throws Exception {
        return printTempVersionDao.getPrintTempVersionById(id,processInstanceId);
    }

    @API(desc = "根据参数获取唯一版本信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public PrintTempVersion getPrintTempVersionByPrintTempVersion(Map<String,Object> params) throws Exception {
        //根据模板Id和版本信息只能获取唯一一条数据
        return printTempVersionDao.getPrintTempVersionByParams(params).get(0);
    }

    @API(desc = "文档比对根据参数获取唯一版本信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public PrintTempVersion getPrintTempVersionByPrintTempVersionForCompare(Map<String,Object> params) throws Exception {
        //根据模板Id和版本信息只能获取唯一一条数据
        return printTempVersionDao.getPrintTempVersionByParamsForCompare(params).get(0);
    }

    @API(desc = "产品关联文档子模板",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<PrintTempVersion> getPrintTempVersionName(SqlParam<PrintTempVersion> param) throws Exception {
        param.setMakeSql(true);
        return printTempVersionDao.getPrintTempVersionName(param);
    }


    @API(desc = "文档模板提交审批流",auth = APIAuth.NO,operation = APIOperation.INSTER)
    public String insDocumentFlow(SqlParam<PrintTempVersion> param) throws Exception {

        return RequestSupport.updateReturnJson(true, "流程开启成功", null).toString();
    }

    @API(desc = "文档子模板提交审批流",auth = APIAuth.NO,operation = APIOperation.INSTER)
    public String insChildDocumentFlow(SqlParam<PrintTempVersion> param) throws Exception {

        return RequestSupport.updateReturnJson(true, "流程开启成功", null).toString();
    }

    @API(desc = "回滚审批流",auth = APIAuth.NO,operation = APIOperation.DELETE)
    public String rollbackFlow(SqlParam<PrintTempVersion> param) throws Exception {
        daoService.doTrans(() -> {
            printTempVersionDao.deleteWfProcessInstance(param.getModel().getProcessInstanceId());
            printTempVersionDao.deleteWfBusiExtend(param.getModel().getProcessInstanceId());
            Integer count = printTempVersionDao.deleteWfTaskActor(param.getModel().getProcessInstanceId());
            if(count>0){
                printTempVersionDao.deleteWfTask(param.getModel().getProcessInstanceId());
            }
        });
        return RequestSupport.updateReturnJson(true, "流程关闭成功", null).toString();
    }

}
