package com.kayak.pms.printTemp.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.printTemp.dao.PrintTempDataDao;
import com.kayak.pms.printTemp.model.PrintTempData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: k-cloud
 * @description: 文档模板数据源Service
 * @author: WangZhenXin
 * @create: 2020-12-29 09:06
 * @memo 备注信息
 */
@Service
@APIDefine(desc = "文档模板数据源服务", model = PrintTempData.class)
public class PrintTempDataService {
    private static final Logger logger = LoggerFactory.getLogger(PrintTempDataService.class);

    @Autowired
    private PrintTempDataDao printTempDataDao;


    @API(desc = "查询模板数据源",auth = APIAuth.YES,operation = APIOperation.SELECT)
    public SqlResult<PrintTempData> getPrintTempDataList1(SqlParam<PrintTempData> param) throws Exception {
        return getPrintTempDataList(param);
    }

    @API(desc = "查询模板数据源",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<PrintTempData> getPrintTempDataList(SqlParam<PrintTempData> param) throws Exception {
        param.setMakeSql(true);
        if("0".equals(param.getModel().getIsXpData())){
            param.getModel().setXpDocType(null);
        }
        return printTempDataDao.getPrintTempDataList(param);
    }

    @API(desc = "新增模板数据源",auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addPrintTempData(SqlParam<PrintTempData> param) throws Exception {
        PrintTempData printTempData = param.getModel();
            Integer printTempDataMapCount = printTempDataDao.getPrintTempDataMapCount(printTempData);
            if (printTempDataMapCount >0){
                return RequestSupport.updateReturnJson(false, "已存在该类型数据源!", null).toString();
            }
        //设置创建时间
        Date now = new Date();
        printTempData.setCreateDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        printTempData.setCrtTime(DateUtil.getNowTime());
        printTempData.setCrtUser((String)SysUtil.getSysUserParamValue("sys_user_userid"));
        int tempData = printTempDataDao.addPrintTempData(printTempData);
        if(tempData < 1) {
            throw new PromptException("添加失败");
        }
        return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
    }

    @API(desc = "修改模板数据源",auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updatePrintTempData(SqlParam<PrintTempData> param) throws Exception {
        PrintTempData printTempData = param.getModel();
        //设置更新时间
        Date now = new Date();
        printTempData.setUpdateDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        printTempData.setUpdTime(DateUtil.getNowTime());
        printTempData.setUpdUser((String)SysUtil.getSysUserParamValue("sys_user_userid"));
        int tempData = printTempDataDao.updatePrintTempData(printTempData);
        if(tempData < 1) {
            throw new PromptException("修改失败");
        }
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }



}
