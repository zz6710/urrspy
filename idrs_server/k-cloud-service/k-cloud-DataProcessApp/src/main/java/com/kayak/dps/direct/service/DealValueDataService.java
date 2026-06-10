package com.kayak.dps.direct.service;


import com.kayak.clear.req.PubReq;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.direct.dao.DealValueDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//axin  估值回落
@Service
public class DealValueDataService {

    @Autowired
    DealValueDataDao dealValueDataDao;


    public void dealProdAssetBln (PubReq request) throws Exception {

        //查询今日估值系统托送过来的产品数量
        List<SqlRow> sr = dealValueDataDao.findProdInfo(request.getTaskDate());

        //循环产品
        for (SqlRow sqlRow:sr) {

            //根据映射科目生成估值持仓信息
            List<SqlRow> valueData = dealValueDataDao.findValueDataByProdCode(request.getTaskDate(),sqlRow.getString("prod_code"));

            //操作list，重新组装插入的参数
            List<Map<String, Object>> mapList = valueData.stream().map(this::setScrId).collect(Collectors.toList());

            //插入产品资产负债持仓表 及 SPV净值信息
            dealValueDataDao.insertValueData(mapList,request.getTaskDate());

        }

        //查找SPV信息
        dealValueDataDao.addSPVNetInfo(request.getTaskDate());

    }

    //获取证券编号
    public SqlRow setScrId(SqlRow sqlRow){

        String scr_id = sqlRow.getString("scr_id");

        //估值表3代表回购？查询交易编号
        if("3".equals(sqlRow.getString("bred_cd"))){

            //不知怎么取值

        }else{

            //回购以外拼接证券编码，拼接方式：交易市场代码.证券代码.品种
            scr_id = sqlRow.getString("trx_mkt_cd") + "." + sqlRow.getString("scr_cd") + "." + sqlRow.getString("bred_cd");

        }

        sqlRow.put("scr_id",scr_id);
        return sqlRow;
    }
}
