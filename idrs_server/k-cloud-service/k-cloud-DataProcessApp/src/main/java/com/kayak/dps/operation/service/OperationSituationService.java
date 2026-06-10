package com.kayak.dps.operation.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.operation.dao.OperationSituationDao;
import com.kayak.dps.operation.model.OperationSituation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 运营情况分析
 * axin
 * 20230529
 */
@Service
@APIDefine(desc = "申报文档服务", model = OperationSituation.class)
public class OperationSituationService {

    private static final Logger logger = LoggerFactory.getLogger(OperationSituationService.class);

    @Autowired
    OperationSituationDao operationSituationDao;

    @API(desc = "产品下拉框", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findProdInfo(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        return operationSituationDao.findProdInfo(params);
    }

    @API(desc = "查询资产占比情况", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findAssetPositions(SqlParam<OperationSituation> params) throws Exception {

        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getAssetPositionsTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        SqlResult<OperationSituation> result = operationSituationDao.findAssetPositions(params);

        BigDecimal sum = operationSituationDao.getAssetPositionsTotal(params.getModel());

        //计算比例
        for (OperationSituation o : result.getRows()) {

            BigDecimal b = new BigDecimal(o.getPositionAmt()).multiply(new BigDecimal("100"));
            String val = b.divide(sum,4,BigDecimal.ROUND_HALF_UP).toString();

            o.setValue(val);
        }
        return result;
    }


    @API(desc = "查询产品总份额", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findProdScaleTotal(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getProdScaleTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        SqlResult<OperationSituation> result = operationSituationDao.findProdScaleTotal(params);

        return calculationProdScale(params , result);
    }

    @API(desc = "查询产品份额详情", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findProdScaleDetails(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getProdScaleTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        SqlResult<OperationSituation> result = operationSituationDao.findProdScaleDetails(params);

        return calculationProdScale(params , result);
    }

    private SqlResult<OperationSituation> calculationProdScale(SqlParam<OperationSituation> params ,SqlResult<OperationSituation> result) throws Exception {

        String title = "产品现有总规模";
        String tip = "同比上月末";
        String unit = "元";
        String value = "0.00";
        String status = "equal";//dowm //up
        String percent = "0.00";
        String proportion = "0.00";

        for (OperationSituation o : result.getRows()) {

            unit = "元";
            value = o.getExistingScale();

            if(o.getExistingScale().length() > 8){
                value = new BigDecimal(o.getExistingScale()).divide(new BigDecimal("10000"),2,BigDecimal.ROUND_HALF_UP).toString();
                unit = "万元";
            }
            if(o.getExistingScale().length() > 12){
                value = new BigDecimal(value).divide(new BigDecimal("10000"),2,BigDecimal.ROUND_HALF_UP).toString();
                unit = "亿元";
            }

            //计算百分比
            BigDecimal sub = new BigDecimal(o.getExistingScale()).subtract(new BigDecimal(o.getHistoryScale()));

            if(StringUtils.isNotBlank(o.getHistoryScale()) && Double.parseDouble(o.getHistoryScale()) != 0.00){
                if(sub.doubleValue() < 0 ){//降低
                    status = "dowm";
                    proportion = sub.multiply(new BigDecimal("100")).divide(new BigDecimal(o.getHistoryScale()),4,BigDecimal.ROUND_HALF_UP).toString();
                    percent = new BigDecimal(proportion).multiply(new BigDecimal("-1")).toString();
                }else if (sub.doubleValue() > 0) {
                    status = "up";
                    proportion = sub.multiply(new BigDecimal("100")).divide(new BigDecimal(o.getHistoryScale()),4,BigDecimal.ROUND_HALF_UP).toString();
                    percent = proportion;
                }
            }
            o.setValue(value);
            o.setTitle(title);
            o.setUnit(unit);
            o.setStatus(status);
            o.setPercent(percent);
            o.setProportion(proportion);
            o.setTip(tip);
        }

        return result;
    }


    @API(desc = "机构下拉框", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findOrgDict(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getOrgHoldTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        return operationSituationDao.findOrgDict(params);
    }

    @API(desc = "查询机构持有量详情", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findOrgHoldDetails(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getOrgHoldTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        return operationSituationDao.findOrgHoldDetails(params);
    }

    @API(desc = "查询机构持有量", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findOrgHold(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getOrgHoldTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);
        //获取数据
        SqlResult<OperationSituation> result = operationSituationDao.findOrgHold(params);
        //树状图颜色
        List<String> color = new ArrayList<>();
        color.add("#7bede8");
        //标题栏参数
        List<String> axisData = new ArrayList<>();
        //Data
        List<String> paramNum = new ArrayList<>();

        for (OperationSituation o : result.getRows()) {
            axisData.add(o.getOrgName());
            String holdScale = new BigDecimal(o.getHoldScale()).divide(new BigDecimal("100000000"),2,BigDecimal.ROUND_HALF_UP).toString();
            paramNum.add(holdScale);
        }

        //数据集
        OperationSituation.SeriesData s = new OperationSituation.SeriesData();
        s.setData(paramNum);
        s.setName("持有量");
        s.setUnit("亿元");
        s.setType("bar");

        List<OperationSituation.SeriesData> seriesData = new ArrayList<>();
        seriesData.add(s);
        //返回参数集
        return getOperationSituationSqlResult(result, color, axisData, seriesData);
    }

    private SqlResult<OperationSituation> getOperationSituationSqlResult(SqlResult<OperationSituation> result,
                            List<String> color, List<String> axisData, List<OperationSituation.SeriesData> seriesData) {
        List<OperationSituation> list = new ArrayList<>();

        OperationSituation o = new OperationSituation();
        o.setAxisData(axisData);
        o.setSeriesData(seriesData);
        o.setColor(color);

        list.add(o);
        result.setRows(list);

        return result;
    }

    @API(desc = "查询债券分布情况详情", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findAssetDistributeDetails(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getAssetGradeTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        return operationSituationDao.findAssetDetails(params);
    }

    @API(desc = "查询债券分布情况", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findAssetDistribute(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getAssetGradeTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        SqlResult<OperationSituation> result;
        String type = params.getModel().getValue();
        if ("1".equals(type)){
            result = operationSituationDao.findAssetGrade(params);
        }else if ("2".equals(type)){
            result = operationSituationDao.findAssetIndustry(params);
        }else if ("3".equals(type)){
            result = operationSituationDao.findAssetType(params);
        }else{
            throw new Exception("不支持的类型：" + params.getModel().getLabel());
        }

        //树状图颜色
        List<String> color = new ArrayList<>();
        color.add("#eb4e7a");
        color.add("#4869ea");

        //标题栏参数
        List<String> axisData = new ArrayList<>();
        //穿透前数据集
        List<String> pierceBefore = new ArrayList<>();
        //穿透后数据集
        List<String> pierceLater = new ArrayList<>();
        for (OperationSituation o : result.getRows()) {

            axisData.add(o.getLabel());

            String pierce = new BigDecimal(o.getPierceBefore()).divide(new BigDecimal("100000000"),2,BigDecimal.ROUND_HALF_UP).toString();
            pierceBefore.add(pierce);

            pierce = new BigDecimal(o.getPierceLater()).divide(new BigDecimal("100000000"),2,BigDecimal.ROUND_HALF_UP).toString();
            pierceLater.add(pierce);
        }

        //数据集
        OperationSituation.SeriesData s1 = new OperationSituation.SeriesData();
        s1.setData(pierceBefore);
        s1.setName("穿透前");
        s1.setUnit("亿元");
        s1.setType("bar");

        //数据集
        OperationSituation.SeriesData s2 = new OperationSituation.SeriesData();
        s2.setData(pierceLater);
        s2.setName("穿透后");
        s2.setUnit("亿元");
        s2.setType("bar");

        List<OperationSituation.SeriesData> seriesData = new ArrayList<>();
        seriesData.add(s1);
        seriesData.add(s2);
        //返回参数集
        return getOperationSituationSqlResult(result, color, axisData, seriesData);
    }

    @API(desc = "行业下拉框", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findIndustryDict(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getAssetGradeTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        return operationSituationDao.findIndustryDict(params);
    }
    @API(desc = "评级下拉框", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findGradeDict(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getAssetGradeTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        return operationSituationDao.findGradeDict(params);
    }
    @API(desc = "资产类型下拉框", auth = APIAuth.NO)
    public SqlResult<OperationSituation> findTypeDict(SqlParam<OperationSituation> params) throws Exception {
        params.setMakeSql(false);
        //查询日期
        String sysDate = DateUtil.getSysWordDay();//系统工作日
        String tradeDate = operationSituationDao.getAssetGradeTradeDate(sysDate);//获取当前日期之前的最大日期
        params.getModel().setTradeDate(tradeDate);

        return operationSituationDao.findTypeDict(params);
    }


}
