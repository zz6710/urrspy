package com.kayak.dps.ods.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.ods.dao.DealDwdPortDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class DealDwdService {
    private static Logger logger = LoggerFactory.getLogger(DealDwdService.class);
    @Resource(name = "dealDwdPortDao")
    private DealDwdPortDao dealDwdPortDao;

    /**
     * 处理产品限制信息表
     * @param params
     *          prod_exeid  对应得表的sql查询id
     *          status 更新 1 新增 0
     * @throws Exception
     */
    public void dealPordLimitInfo(Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        //日期处理
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            if ("1".equals(params.get("status"))) {
                //更新时间
                //tparam.put("UPD_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                //todo
                dealDwdPortDao.updateDPPLIF(new HashMap<>());
            } else {
                //第一次插入
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT02PRODLIM"),sqlRow);
            }
        }
    }

    /**
     * 处理产品监管信息表
     * @param params
     *          prod_exeid  对应得表的sql查询id
     *          status 更新 1 新增 0
     * @throws Exception
     */

    public void dealPordCbrdat(Map<String, Object> params) throws Exception {
        //将贴源层数据查出来
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            if ("1".equals(params.get("status"))) {
                //更新时间
                //todo
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.updateDPPSI(sqlRow);
            } else {
                //第一次插入
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT01PRODCBR"),sqlRow);
            }
        }
    }

    /**
     * 删除产品监管信息表
     * @param params
     *            对应id
     * @throws Exception
     */
    public void deletePordCbrdat(Map<String, Object> params) throws Exception {
        dealDwdPortDao.deleteInfo(ExeQuery.queryExeId("DE01PRODCBR"),(String)params.get("prod_code"));
    }
    /**
     * 删除产品限制信息表
     * @param params
     *            对应id
     * @throws Exception
     */
    public void deletePordLimitInfo(Map<String, Object> params) throws Exception {
        dealDwdPortDao.deleteInfo(ExeQuery.queryExeId("DE02PRODLIM"),(String)params.get("prod_code"));
    }


    /**
     * 处理客户基本信息
     * @param params
     *              prod_exeid  对应得表的sql查询id
     *              status 更新 1 新增 0
     * @throws Exception
     */
    public void dealInvestorIdentity(Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            //日期处理
            if ("1".equals(params.get("status"))) {
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                // dealDwdPortDao.insertToDPCDI("",sqlRow);
            } else {
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT03INVEIDE"),sqlRow);
            }
        }
    }

    /**
     * 删除客户基本信息
     * @param params
     *           对应id
     * @throws Exception
     */
    public void deleteInvestorIdentity(Map<String, Object> params) throws Exception {
        dealDwdPortDao.deleteInfo(ExeQuery.queryExeId("DE03INVEIDE"),(String)params.get("cuit_code"));
    }

    /**
     * 处理客户交易明细
     * @param params
     *              prod_exeid  对应得表的sql查询id
     *              status 更新 1 新增 0
     * @throws Exception
     */
    public void dealInvestorDetail(Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            if ("1".equals(params.get("status"))) {
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                //dealDwdPortDao.insertToDECTD(sqlRow);
            } else {
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT04INVEDETAIL"),sqlRow);
            }
        }
    }

    /**
     * 处理客户份额持仓明细
     * @param params
     *              prod_exeid  对应得表的sql查询id
     *              status 更新 1 新增 0
     * @throws Exception
     */
    public void dealVolBalanceSum(Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            if ("1".equals(params.get("status"))) {
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                //dealDwdPortDao.insertToDACLPD(sqlRow);
            } else {
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT05VOLSUM"),sqlRow);
            }
        }
    }

    /**
     * 删除客户交易明细
     * @param params
     *            对应id
     * @throws Exception
     */
    public void deleteInvestorDetail(Map<String, Object> params) throws Exception {
        dealDwdPortDao.deleteInfo(ExeQuery.queryExeId("DE04INVEDETAIL"),(String) params.get("srl_nbr"));
    }

    /**
     * 处理产品持仓明细
     * @param params
     *              prod_exeid  对应得表的sql查询id
     *              status 更新 1 新增 0
     * @throws Exception
     */
    public void dealAssetHodingDetail(Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            if ("1".equals(params.get("status"))) {
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                //dealDwdPortDao.insertToDPAPD(sqlRow);
            } else {
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT06PRODPOS"),sqlRow);
            }
        }
    }

    /**
     * 处理净值信息
     * @param params
     *              prod_exeid  对应得表的sql查询id
     *              status 更新 1 新增 0
     * @throws Exception
     */
    public void dealNetProdInfo(Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            if ("1".equals(params.get("status"))) {
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                //dealDwdPortDao.insertToDPPBI(sqlRow);
            } else {
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT07NETPRODINFO"),sqlRow);
            }
        }
    }
    /**
     * 处理产品费用信息
     * @param params
     *              prod_exeid  对应得表的sql查询id
     *              status 更新 1 新增 0
     * @throws Exception
     */
    public void dealProdFeeInfo(Map<String, Object> params) throws Exception{
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            if ("1".equals(params.get("status"))) {
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                //dealDwdPortDao.insertToDPPFI(sqlRow);
            } else {
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT08PRODFEEINFO"),sqlRow);
            }
        }
    }
    /**
     * 处理产品交易明细
     * @param params
     *              prod_exeid  对应得表的sql查询id
     *              status 更新 1 新增 0
     * @throws Exception
     */
    public void dealProdPayDetails(Map<String, Object> params) throws Exception{
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        for (SqlRow sqlRow : sqlRows) {
            if ("1".equals(params.get("status"))) {
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                //dealDwdPortDao.insertToDEPFPD(sqlRow);
            } else {
                sqlRow.put("CRT_DT", simpleDateFormat.format(new Date()));
                sqlRow.put("UPD_DT", simpleDateFormat.format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT09PRODPAYDETAILS"),sqlRow);
            }
        }
    }

    /**
     * 处理产品基本信息
     * @param params
     *              prod_exeid  对应得表的sql查询id
     *              status 更新 1 新增 0
     * @throws Exception
     */
    public void dealProdBaseInfo(Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        for (SqlRow sqlRow:sqlRows) {
            if ("1".equals(params.get("status"))) {
                //更新时间
                sqlRow.put("UPD_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                dealDwdPortDao.updateDPPBI(ExeQuery.queryExeId("UP09PRODBASEINFO"),sqlRow);
            } else {
                //第一次插入
                sqlRow.put("CRT_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("IT09PRODBASEINFO"),sqlRow);
            }
        }
    }

    /**
     * 处理债券基本信息
     */

    public void dealBondInfo(Map<String, Object> params) throws Exception{
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        for (SqlRow sqlRow:sqlRows) {
            //处理数据转化成数据字典
            SqlRow s = dealDwdPortDao.dealMainRat(sqlRow.get("MAIN_RAT").toString());
            sqlRow.put("MAIN_RAT", s.get("itemkey"));
            if ("1".equals(params.get("status"))) {
                //更新时间
                sqlRow.put("UPD_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                dealDwdPortDao.updateDPPBI(ExeQuery.queryExeId("UP09PRODBASEINFO"),sqlRow);
            } else {
                //第一次插入
                sqlRow.put("CRT_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("2"),sqlRow);
            }
        }
    }


    /**
     * 处理基金基本信息
     */
    public void dealFundInfo(Map<String, Object> params) throws Exception{
        List<SqlRow> sqlRows = dealDwdPortDao.selectInfo(ExeQuery.queryExeId(params.get("prod_exeid").toString()),params);
        for (SqlRow sqlRow:sqlRows) {
            if ("1".equals(params.get("status"))) {
                //更新时间
                sqlRow.put("UPD_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                dealDwdPortDao.updateDPPBI(ExeQuery.queryExeId("UP09PRODBASEINFO"),sqlRow);
            } else {
                //第一次插入
                sqlRow.put("CRT_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));
                dealDwdPortDao.insertToTable(ExeQuery.queryExeId("4"),sqlRow);
            }
        }
    }
}
