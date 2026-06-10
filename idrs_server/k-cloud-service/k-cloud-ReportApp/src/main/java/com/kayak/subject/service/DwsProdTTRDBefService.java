package com.kayak.subject.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.BaseReportReloadLogDao;
import com.kayak.subject.dao.DwsProdTTRDBefDao;
import com.kayak.subject.dao.ReportDataGenDao;
import com.kayak.subject.dao.ReportMenuConfigDao;
import com.kayak.subject.model.BaseReportReloadLog;
import com.kayak.subject.model.DwsProdTTRDBef;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@APIDefine(desc = "资产负载情况穿透前修改后服务", model = DwsProdTTRDBef.class)
@Slf4j
public class DwsProdTTRDBefService implements ExcelImportService<DwsProdTTRDBef> {

    @Autowired
    private ComnDao comnDao;

    @Autowired
    private DwsProdTTRDBefDao dwsProdTTRDBefDao;

    @Autowired
    private ReportMenuConfigDao reportMenuConfigDao;

    @Autowired
    private RptBusinessBaseTaskService rptBusinessBaseTaskService;

    @Autowired
    private BaseReportReloadLogDao baseReportReloadLoDao;

    private static ReportDataGenDao reportDataGenDao = SysBeans.getBean("reportDataGenDao");

    @API(desc = "查询资产负载情况穿透前修改后信息", auth = APIAuth.YES)
    public SqlResult<DwsProdTTRDBef> findDwsProdTTRDBefs(SqlParam<DwsProdTTRDBef> params) throws Exception {
        params.setMakeSql(true);
        return dwsProdTTRDBefDao.findDwsProdTTRDBefs(params);
    }

    @API(desc = "添加资产负载情况穿透前修改后", params = "id,product_code,i_code,asset_third_type,i_name,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date,pen_inv_f,per_pen_inv_f", auth = APIAuth.NO)
    public int addDwsProdTTRDBef(SqlParam<DwsProdTTRDBef> params) throws Exception {
        return dwsProdTTRDBefDao.addDwsProdTTRDBef(params).getEffect();
    }

    @API(desc = "修改资产负载情况穿透前修改后", params = "id,product_code,i_code,asset_third_type,i_name,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date,pen_inv_f,per_pen_inv_f", auth = APIAuth.NO)
    public int updateDwsProdTTRDBef(SqlParam<DwsProdTTRDBef> params) throws Exception {
        return dwsProdTTRDBefDao.updateDwsProdTTRDBef(params).getEffect();
    }

    @API(desc = "删除资产负载情况穿透前修改后", params = "id,product_code,i_code,asset_third_type,i_name,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date", auth = APIAuth.NO)
    public int deleteDwsProdTTRDBef(SqlParam<DwsProdTTRDBef> params) throws Exception {
        return dwsProdTTRDBefDao.deleteDwsProdTTRDBef(params).getEffect();
    }

    public int deleteDwsProdTTRDBef(DwsProdTTRDBef params) throws Exception {
        return dwsProdTTRDBefDao.deleteDwsProdTTRDBef(params).getEffect();
    }

    public String importDwsProdTTRDBef(MultipartFile file, Map<String, Object> params) throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("导入G06穿透前报表（调整后）【{}】开始", file.getOriginalFilename());
        final DwsProdTTRDBefService dwsProdTTRDBefService = this;
        ExcelImportListener<DwsProdTTRDBef> excelImportListener = new ExcelImportListener<DwsProdTTRDBef>(params) {
            @Override
            protected ExcelImportService<DwsProdTTRDBef> getImportService() {
                return dwsProdTTRDBefService;
            }
        };
            String reportDate = (String) params.get("reportDate");
            DwsProdTTRDBef deleteCondition = new DwsProdTTRDBef();
            deleteCondition.setReportDate(reportDate);
            //先删后插
            deleteDwsProdTTRDBef(deleteCondition);
            log.info("删除 dws_prod_ttrd_bef_g06a2 表数据，日期为：{}", reportDate);
        try {
            EasyExcel.read(file.getInputStream())
                    .head(DwsProdTTRDBef.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入G06穿透前报表（调整后）【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    @Override
    public void importFile(List<DwsProdTTRDBef> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        comnDao.doTrans(() -> {
            PreparedStatement ps = getPreparedStatement(list);
            try {
                String reportDate = (String) map.get("reportDate");
                for (DwsProdTTRDBef dwsProdTTRDBef : list) {
                    resolveDwsProdTTRDBef(dwsProdTTRDBef);
                    dwsProdTTRDBef.setReportDate(reportDate);
                    ps.setString(1, dwsProdTTRDBef.getProductCode());
                    ps.setString(2, dwsProdTTRDBef.getIcode());
                    ps.setString(3, dwsProdTTRDBef.getAssetThirdType());
                    ps.setString(4, dwsProdTTRDBef.getIname());
                    ps.setString(5, dwsProdTTRDBef.getAmount());
                    ps.setString(6, dwsProdTTRDBef.getChangerate());
                    ps.setString(7, dwsProdTTRDBef.getInvestedamountcny());
                    ps.setString(8, dwsProdTTRDBef.getOrgClassific());
                    ps.setString(9, dwsProdTTRDBef.getOrderfreemanage());
                    ps.setString(10, dwsProdTTRDBef.getNewClassific());
                    ps.setString(11, dwsProdTTRDBef.getIsoverdue());
                    ps.setString(12, dwsProdTTRDBef.getCashtodomain());
                    ps.setString(13, dwsProdTTRDBef.getVcintfund());
                    ps.setString(14, dwsProdTTRDBef.getGovintfund());
                    ps.setString(15, dwsProdTTRDBef.getIsnostandard());
                    ps.setString(16, dwsProdTTRDBef.getBondrating());
                    ps.setString(17, dwsProdTTRDBef.getSpecialbond());
                    ps.setString(18, dwsProdTTRDBef.getInmarketornot());
                    ps.setString(19, dwsProdTTRDBef.getCashtogovernment());
                    ps.setString(20, dwsProdTTRDBef.getCashtopublic());
                    ps.setString(21, dwsProdTTRDBef.getCashtorelateproduct());
                    ps.setString(22, dwsProdTTRDBef.getMoneyofoverdueasset());
                    ps.setString(23, dwsProdTTRDBef.getMoneyofproperty());
                    ps.setString(24, dwsProdTTRDBef.getSecondlevelcaptialbond());
                    ps.setString(25, dwsProdTTRDBef.getContinuebondforever());
                    ps.setString(26, dwsProdTTRDBef.getSeniorbond());
                    ps.setString(27, dwsProdTTRDBef.getConvertbond());
                    ps.setString(28, dwsProdTTRDBef.getOtherbanksupplementtools());
                    ps.setString(29, dwsProdTTRDBef.getAssettype());
                    ps.setString(30, dwsProdTTRDBef.getDealDate());
                    ps.setString(31, dwsProdTTRDBef.getReportDate());
                    ps.setString(32, dwsProdTTRDBef.getCshMngF());
                    ps.setString(33, dwsProdTTRDBef.getRecvblPrnc());
                    ps.setString(34, dwsProdTTRDBef.getHldnQntt());
                    ps.setString(35, dwsProdTTRDBef.getPenInvF());
                    ps.setString(36, dwsProdTTRDBef.getPerPenInvF());
                    ps.setString(37, dwsProdTTRDBef.getHkInv());
                    ps.setString(38, dwsProdTTRDBef.getQdiiInv());
                    ps.addBatch();
                }
                ps.executeBatch();
                createImportLog(reportDate);
                log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                log.error("导入报表指标科目映射异常!", e);
                throw new Exception(e.getMessage());
            } finally {
                ps.close();
            }
        });
    }

    private PreparedStatement getPreparedStatement(List<DwsProdTTRDBef> list) throws Exception {
        String batchSql = "INSERT INTO dws_prod_ttrd_bef_g06a2\n" +
                "(PRODUCT_CODE, I_CODE, ASSET_THIRD_TYPE, I_NAME, AMOUNT, CHANGERATE, INVESTEDAMOUNTCNY, ORG_CLASSIFIC, ORDERFREEMANAGE, NEW_CLASSIFIC, ISOVERDUE, CASHTODOMAIN, VCINTFUND, GOVINTFUND, ISNOSTANDARD, BONDRATING, " +
                "SPECIALBOND, INMARKETORNOT, CASHTOGOVERNMENT, CASHTOPUBLIC, CASHTORELATEPRODUCT, MONEYOFOVERDUEASSET, MONEYOFPROPERTY, SECONDLEVELCAPTIALBOND, CONTINUEBONDFOREVER, SENIORBOND, CONVERTBOND, OTHERBANKSUPPLEMENTTOOLS," +
                " ASSETTYPE, DEAL_DATE, report_date,csh_mng_f,recvbl_prnc,hldn_qntt,pen_inv_f,per_pen_inv_f,hk_inv,qdii_inv)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        if (list == null || list.isEmpty()) {
            throw new Exception("没有数据");
        }
        Connection connection = comnDao.getConnection();
        PreparedStatement ps = connection.prepareStatement(batchSql);
        return ps;
    }

    private void resolveDwsProdTTRDBef(DwsProdTTRDBef dwsProdTTRDBef){
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getOrgClassific())) {
            dwsProdTTRDBef.setOrgClassific(dwsProdTTRDBef.getOrgClassific().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getNewClassific())) {
            dwsProdTTRDBef.setNewClassific(dwsProdTTRDBef.getNewClassific().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getIsoverdue())) {
            dwsProdTTRDBef.setIsoverdue(dwsProdTTRDBef.getIsoverdue().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getSecondlevelcaptialbond())) {
            dwsProdTTRDBef.setSecondlevelcaptialbond(dwsProdTTRDBef.getSecondlevelcaptialbond().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getContinuebondforever())) {
            dwsProdTTRDBef.setContinuebondforever(dwsProdTTRDBef.getContinuebondforever().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getSeniorbond())) {
            dwsProdTTRDBef.setSeniorbond(dwsProdTTRDBef.getSeniorbond().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getConvertbond())) {
            dwsProdTTRDBef.setConvertbond(dwsProdTTRDBef.getConvertbond().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getOtherbanksupplementtools())) {
            dwsProdTTRDBef.setOtherbanksupplementtools(dwsProdTTRDBef.getOtherbanksupplementtools().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getAssettype())) {
            dwsProdTTRDBef.setAssettype(dwsProdTTRDBef.getAssettype().split(" ")[0]);
        }

        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getOrderfreemanage())) {
            dwsProdTTRDBef.setOrderfreemanage(dwsProdTTRDBef.getOrderfreemanage().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getCashtodomain())) {
            dwsProdTTRDBef.setCashtodomain(dwsProdTTRDBef.getCashtodomain().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getVcintfund())) {
            dwsProdTTRDBef.setVcintfund(dwsProdTTRDBef.getVcintfund().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getGovintfund())) {
            dwsProdTTRDBef.setGovintfund(dwsProdTTRDBef.getGovintfund().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getIsnostandard())) {
            dwsProdTTRDBef.setIsnostandard(dwsProdTTRDBef.getIsnostandard().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getBondrating())) {
            String bondrating = "";
            String value = dwsProdTTRDBef.getBondrating();
            if (value.contains("/")) {
                String[] valueArr = value.split("/");
                for (String str : valueArr) {
                    if (StringUtils.isEmpty(bondrating)) {
                        bondrating = str.split(" ")[0];
                    } else {
                        bondrating += "/" + str.split(" ")[0];
                    }
                }
            } else if (value.contains(",")) {
                String[] valueArr = value.split(",");
                for (String str : valueArr) {
                    if (StringUtils.isEmpty(bondrating)) {
                        bondrating = str.split(" ")[0];
                    } else {
                        bondrating += "," + str.split(" ")[0];
                    }
                }
            } else {
                bondrating = value.split(" ")[0];
            }
            dwsProdTTRDBef.setBondrating(bondrating);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getSpecialbond())) {
            dwsProdTTRDBef.setSpecialbond(dwsProdTTRDBef.getSpecialbond().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getInmarketornot())) {
            dwsProdTTRDBef.setInmarketornot(dwsProdTTRDBef.getInmarketornot().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getCashtogovernment())) {
            dwsProdTTRDBef.setCashtogovernment(dwsProdTTRDBef.getCashtogovernment().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getCshMngF())) {
            dwsProdTTRDBef.setCshMngF(dwsProdTTRDBef.getCshMngF().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getMoneyofproperty())) {
            dwsProdTTRDBef.setMoneyofproperty(dwsProdTTRDBef.getMoneyofproperty().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getPenInvF())) {
            dwsProdTTRDBef.setPenInvF(dwsProdTTRDBef.getPenInvF().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getPerPenInvF())) {
            dwsProdTTRDBef.setPerPenInvF(dwsProdTTRDBef.getPerPenInvF().split(" ")[0]);
        }

        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getHkInv())) {
            dwsProdTTRDBef.setHkInv(dwsProdTTRDBef.getHkInv().split(" ")[0]);
        }
        if (StringUtils.isNotEmpty(dwsProdTTRDBef.getQdiiInv())) {
            dwsProdTTRDBef.setQdiiInv(dwsProdTTRDBef.getQdiiInv().split(" ")[0]);
        }

    }

    @API(desc = "生资产端报表", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String updateTaskApp(SqlParam<DwsProdTTRDBef> params) throws Exception {
        String maxId = "";
        String reports = "";

        Map<String, Object> map = new HashMap<>();
        map.put("paravalue","1");
        map.put("oldParavalue","0");
        map.put("paraid","90000061002");

        BaseReportReloadLog baseReportReloadLog = new BaseReportReloadLog();
        baseReportReloadLog.setMenuId(params.getModel().getMenuId());
        baseReportReloadLog.setReportDate(params.getModel().getReportDate());
        baseReportReloadLog.setStartDate(DateUtil.getNowDate());
        baseReportReloadLog.setStartTime(DateUtil.getNowTime());
        baseReportReloadLog.setResultStatus("正在"+params.getModel().getButtonName()+"中");
        baseReportReloadLog.setUserName(SysUtil.getLoginUserid());

        try {
            if (rptBusinessBaseTaskService.upTaskStatus(map) > 0) {
                baseReportReloadLoDao.addBaseReportReloadLog(baseReportReloadLog);
                List<SqlRow> sqlRows = baseReportReloadLoDao.findIdBaseReportReloadLogs(baseReportReloadLog);
                if (CollectionUtil.isNotEmpty(sqlRows)) {
                    maxId = sqlRows.get(0).getString("id");
                }

                reports = reportMenuConfigDao.reportMenuConfigHandler(params.getParams());

                // 执行完成后更新数据分布式锁
                map.put("paravalue","0");
                map.put("oldParavalue","1");
                rptBusinessBaseTaskService.upTaskStatus(map);

                baseReportReloadLog.setId(maxId);
                baseReportReloadLog.setEndDate(DateUtil.getNowDate());
                baseReportReloadLog.setEndTime(DateUtil.getNowTime());
                baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+"成功");
                baseReportReloadLog.setResultInfo(reports);
                baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);
            } else {
                return RequestSupport.updateReturnJson(false, "正在"+params.getModel().getButtonName()+"，请稍后重试！", null).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 执行失败后更新数据分布式锁
            map.put("paravalue","0");
            map.put("oldParavalue","1");
            rptBusinessBaseTaskService.upTaskStatus(map);

            baseReportReloadLog.setId(maxId);
            baseReportReloadLog.setEndDate(DateUtil.getNowDate());
            baseReportReloadLog.setEndTime(DateUtil.getNowTime());
            baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+"失败");
            baseReportReloadLog.setResultInfo(e.getMessage());
            baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);

            return RequestSupport.updateReturnJson(false, params.getModel().getButtonName()+"失败！", null).toString();
        }
        return RequestSupport.updateReturnJson(true, params.getModel().getButtonName()+"成功！具体报表如下："+reports, null).toString();
    }

    @API(desc = "生资产端报表查询", auth = APIAuth.NO)
    public String updateTaskAppQuery(SqlParam<DwsProdTTRDBef> params) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("paraid","90000061002");
            List<SqlRow> sqlRowList = rptBusinessBaseTaskService.quTaskStatus(map);
            if (CollectionUtil.isEmpty(sqlRowList) || (CollectionUtil.isNotEmpty(sqlRowList) && "1".equals(sqlRowList.get(0).getString("paravalue")))) {
                    return RequestSupport.updateReturnJson(false, "正在"+params.getModel().getButtonName()+"，请稍后重试！", null).toString();
            }
            if ("1".equals(SysUtil.getSystemParamsByParaid("90000061000"))) {
                return RequestSupport.updateReturnJson(false, "系统清算流程正在执行中，请稍后重试！", null).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, params.getModel().getButtonName()+"失败！", null).toString();
        }
        return updateTaskApp(params);
    }

    /**
     * 重新生成报表【从generateFormAgainTaskApp派生出来，提取必输的两个入参】
     * @param reportDate 重跑日期
     * @param paraId 重跑所需配置的key
     * @return
     * @throws Exception
     */
    public String execTaskApp(String reportDate, String paraId) throws Exception{
        Map<String,Object> mapData = new HashMap<>();
        mapData.put("reportDate",reportDate);
        mapData.put("paraid",paraId);
        FetcherData<DwsProdTTRDBef> param = new FetcherData<DwsProdTTRDBef>(mapData,DwsProdTTRDBef.class);
        return generateFormAgainTaskApp(param);
    }

    private String getNextMothFlag() throws Exception{
        String result = "";
        String strSql = "select paravalue from sys_param where paraid = '90000062001'";
        SqlRow row = comnDao.findRow(strSql, null);
        if(row != null){
            result = row.getString("paravalue");
        }
        return result;
    }

    /**
     * 获取下一个最后一个自然日
     * @param date
     * @param index
     * @return
     */
    public String getNextMonthLastDay(String date, int index){
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
        Calendar cal=Calendar.getInstance();
        Date d;
        try {
            d = df.parse(date);
            cal.setTime(d);
            cal.add(Calendar.MONTH, index);
            cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {

        }
        return df.format(cal.getTime());
    }

    @API(desc = "重新生成报表", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String generateFormAgainTaskApp(SqlParam<DwsProdTTRDBef> params) throws Exception {
        return updateTaskApp(params);
    }

    /**
     * 获取重跑报表维度
     * @param task_id
     * @return
     * @throws Exception
     */
    public String getCoordinateType(String task_id) throws Exception{
        String coordinate_type ="";
        String sql = "select k.coordinate_type from base_report_info k where k.task_id='"+task_id+"'";
        List<SqlRow> list = comnDao.findRows(sql);
        if (list.size()>0){
            coordinate_type = list.get(0).getString("coordinate_type");
            return coordinate_type;
        }
        return null;
    }

    public void createImportLog(String reportDate) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("create_date", DateUtil.getNowDate());
        params.put("summit_user", SysUtil.getSysUserParamValue("sys_user_userid"));
        params.put("create_time", DateUtil.getNowTime());
        params.put("input_dt", reportDate);
        params.put("table_nm", "dws_prod_ttrd_bef_g06a2");
        String sql = "insert into G06_mezzanine_remark(create_date,summit_user,create_time,input_dt,table_nm)values($S{create_date},$S{summit_user},$S{create_time},$S{input_dt},$S{table_nm})";
        comnDao.update(sql,params);
    }
}
