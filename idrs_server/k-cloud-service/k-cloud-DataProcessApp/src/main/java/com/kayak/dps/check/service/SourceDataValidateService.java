package com.kayak.dps.check.service;

import com.kayak.dps.check.dao.DataValidateDao;
import com.kayak.dps.check.model.SourceDataValidateModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SourceDataValidateService {

    private static Logger log = LogManager.getLogger(SourceDataValidateService.class);

    @Resource(name = "dataValidateDao")
    private DataValidateDao dataValidateDao;

    /**
     * 执行源数据表字段基础校验
     * @param deal_date 校验日期
     */
    public void execute(String deal_date){
        Map<String, Object> params = new HashMap<>();
        boolean checkReuslt = false;
        params.put("topic", "1");//topic: 1-模型层加工 2-通用汇总层加工 3-报表集市层加工
        params.put("deal_date", deal_date);//topic: 1-模型层加工 2-通用汇总层加工 3-报表集市层加工

        try{
            //List<SourceDataValidateModel> validateModelList = dataValidateDao.getColumnCheckInfoByTopic(params);//获取校验数据组
            //doColumnCheck(validateModelList, params);//对数据组进行校验
        } catch (Exception e){
            log.info(e.getMessage());
        }
    }

    /**
     * 源数据字段校验规则
     * @param params 查询日期参数
     */
    private void doColumnCheck(List<SourceDataValidateModel> validateModelList, Map<String, Object> params) throws Exception {

        for(SourceDataValidateModel checkModel : validateModelList){
            params.put("tableName", checkModel.getTableName());
            params.put("columnName", checkModel.getColumnName());
            params.put("columnCode", checkModel.getColumnCode());
            params.put("indexType", checkModel.getIndexType());
            params.put("express", checkModel.getExpress());
            params.put("remark", checkModel.getRemark());
            //dataValidateDao.checkSourceDataBySingleColumn(params);
        }
    }

}
