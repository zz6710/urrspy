package com.kayak.dps.adjustment.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.dps.adjustment.dao.DealAdjustmentDao;
import com.kayak.dps.app.utils.ReportDataGenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DealAdjustmentService {

    @Resource(name = "dealAdjustmentDao")
    private DealAdjustmentDao dealAdjustmentDao;

    /**
     * 处理月度报表调差
     * */
    public void adjust(Map<String, Object> params) throws Exception {
        String dealDate = params.get("dealDate").toString();
        List<SqlRow> allMsg = dealAdjustmentDao.findThisMsg(params);
        String row_id_flag = "1";
        BigDecimal balanceAll = new BigDecimal("0");
        BigDecimal balanceMh = new BigDecimal("0");
        BigDecimal balanceZz = new BigDecimal("0");
        for (int i = 0; i < allMsg.size(); i++) {
            //行ID
            String row_id = allMsg.get(i).getString("row_id");
            //列ID
            String column_id = allMsg.get(i).getString("column_id");
            String data_value = allMsg.get(i).getString("data_value");
            if(!row_id_flag.equals(row_id)){
                dealAdjustMsg(balanceAll,balanceMh,balanceZz,row_id_flag,dealDate);
                row_id_flag = row_id;
            }
            //同行内判断
            switch(column_id){
                case "1" :
                    //为总量余额
                    balanceAll = new BigDecimal(data_value);
                    break;
                case "7" :
                    //为母行划转余额
                    balanceMh = new BigDecimal(data_value);
                    break;
                case "10":
                    //为自主发行余额
                    balanceZz = new BigDecimal(data_value);
                    break;
                default:
            }
        }
        if(allMsg.size()>0){
            //最后一行处理
            dealAdjustMsg(balanceAll,balanceMh,balanceZz, row_id_flag, dealDate);
        }
    }

    private void dealAdjustMsg(BigDecimal balanceAll, BigDecimal balanceMh, BigDecimal balanceZz, String row_id_flag, String data_end_date) throws Exception {
        Map<String, Object> dealMsg = new HashMap<>();
        //进入下一行前进行数据对比
        BigDecimal adjust = balanceAll.subtract(balanceMh).subtract(balanceZz);
        balanceZz = balanceZz.add(adjust);
        //更新数据
        if(adjust.compareTo(new BigDecimal("0"))!=0){
            //消除科学计数法影响
            String balanceZz_ = balanceZz.toString();
            dealMsg.put("balanceZz",balanceZz_);
            dealMsg.put("row_id",row_id_flag);
            dealMsg.put("dealDate",data_end_date);
            dealAdjustmentDao.dealAdjust(dealMsg);
        }
    }
}
