package com.kayak.dps.check.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.clear.utils.DateUtils;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.app.model.CheckIndexModel;
import com.kayak.dps.check.dao.DataValidateDao;
import com.kayak.dps.check.enums.ReportCoordinateEnum;
import com.kayak.dps.check.model.ExpressDictDTO;
import com.kayak.dps.check.util.ExpressDictUtil;
import com.kayak.dps.check.util.PrimaryDataCheckUtil;
import com.kayak.dps.direct.dao.CheckDataDao;
import com.kayak.dps.direct.model.dto.IndexCodeDTO;
import com.kayak.dps.direct.util.DirectParams;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportDataValidateService {

    @Resource(name = "dataValidateDao")
    private DataValidateDao dataValidateDao;

    @Autowired
    public CheckDataDao checkDataDao;

    private static String REPORT_COORDINATE_TYPE_01 = "1";//报表报送数据维度-一维报表
    private static String REPORT_COORDINATE_TYPE_02 = "2";//报表报送数据维度-二维报表

    /**
     * 执行报送数据表字段基础校验
     * @param deal_date 校验日期
     */
    public void execute(String deal_date, Map<String, String> params) throws Exception {
        boolean checkReuslt = false;
        params.put("topic", "3");//topic: 1-模型层加工 2-通用汇总层加工 3-报表集市层加工,可去除
        params.put("deal_date", deal_date);//topic: 1-模型层加工 2-通用汇总层加工 3-报表集市层加工
        //String report_category = "02";//报表大类 report_type：01-中债三期 02-中债一二期 03-银保监报表 04-理财与资金信托 05-金融机构资产管理产品报告 06-普益报表 07-人行数据采集 08-自营报表 99-自定义 11-25新增净值份额登记
        Map<String, Object> param1 = new HashMap<>();//校验一维报表的存值Map集合
        Map<String, Object> param2 = new HashMap<>();//校验二维报表的存值Map集合

        try{
            /** 根据报表大类组装所有一维报表数据Map集合 */
            //if (StringUtils.isNotEmpty(params.get("report_freq")) && REPORT_COORDINATE_TYPE_02.equals(params.get("report_freq"))) {
            param2 = this.getCoordinateTwoByType(REPORT_COORDINATE_TYPE_02, params.get("reportType"), deal_date);//二维报表参数集合Map<表名,Map<"row_column",value>>
            //}
            //param1 = this.getCoordinateOneByType(REPORT_COORDINATE_TYPE_01 params.get("report_freq"),, deal_date);//一维报表参数集合Map<表名,List<Map<表名.字段名,value>>>
            doIndexCheck(params, param1, param2);//对数据组进行校验
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 报送数据字段校验规则
     * @param params 查询日期参数
     * @param params1 一维报表数据集合 Map<表名, List<Map<字段名, 值>>>
     * @param params2 二维报表数据集合 Map<表名, Map<"行_列",值>>
     */
    private void doIndexCheck(Map<String, String> params, Map<String, Object> params1, Map<String, Object> params2) throws Exception {
        long startTime = System.currentTimeMillis();
        // 加载全局dict字典参数
        loadDict();
        // 获取校验指标集合
        Map<String, List<CheckIndexModel>> validateModelList = dataValidateDao.getIndexCheckInfoByTopic(params);
        // 获取校验日志字段信息
        Map<String, String> reflectColumnList = dataValidateDao.getReportTableTagColumns(params.get("reportType"));
        // 待校验的报表
        Set<String> reportList = validateModelList.keySet();
        if (reportList.size() <= 0) {
            log.info("无报送指标！");
        }
        // 初始的校验日期
        String  FirstDealDate = params.get("deal_date");
        // 以报送表为维度进行处理
        for (String reportCoordinate : reportList) {
            params.put("deal_date", FirstDealDate);
            String reportTable = reportCoordinate.split("-")[0];
            log.info("当前执行报表:" + reportTable + "指标校验");
            String reportFreq = reportCoordinate.split("-")[2];
            String checkReportDate = reportCoordinate.split("-")[3];
            // 中债一二期清算任务调用校验指标时，根据任务应执行日期计算数据日期进行覆盖
            // 根据报表的维度重新赋值deal_date,日表根据清算任务应执行日期以及是否需要转换处理，月表：如果日期不是月末，则赋值为上月末，
            if("2".equals(reportFreq)&&"01".equals(params.get("isBatch"))){
                //上个月最后一天
                Boolean isLastDayOfMonth = DateUtils.isLastDayOfMonth(params.get("deal_date"));
                if(!isLastDayOfMonth){
                    String lastMonthEndDay = DateUtils.getLastDayOfLastMonth(params.get("deal_date"));
                    params.put("deal_date", lastMonthEndDay);
                }
            }else if("1".equals(reportFreq)&&"02".equals(params.get("reportType"))&&"01".equals(params.get("isBatch"))&&"1".equals(checkReportDate)){
                params.put("reportTable", reportTable);//报表名称
                params.put("reportDate", params.get("shouldExecDate"));//清算任务应执行日期
                String deal_date = calDataDateByReportDate(params);
                params.put("deal_date", deal_date);
            }
            log.info("报送数据表: " + reportTable + " 校验数据日期："+params.get("deal_date"));
            //todo
            // 1：（中债三期）根据校验报表与校验日期删除校验日志
            // 2：更改报送数据状态为 2：校验成功。
            int now_id = StringUtils.isNotEmpty(params.get("id")) ? Integer.parseInt(params.get("id")) : 0;
            String taId = params.get("taId");
            String custNo = params.get("custNo");
            dataValidateDao.updateValidateData(reportTable,params.get("deal_date"),now_id,taId,custNo);
            String coordinateType = reportCoordinate.split("-")[1];
            List<CheckIndexModel> index_model_table = validateModelList.get(reportCoordinate);//当前报表的所有指标

            if (ReportCoordinateEnum.COOR_B.getVal().equals(coordinateType)) {
                /**
                 * 二维坐标报表(人行/G06)
                 * 轮循校验每个指标字段
                 */
                Map<String, Object> c2RowDataMap = (Map<String, Object>) params2.get(reportTable);
                if (c2RowDataMap.size() <= 0) {
                    log.info("报送数据表: " + reportTable + " 无待报送数据, 跳过该表校验");
                    continue;//报送数据表校验日不存在数据，跳过该报表校验并提示
                }
                // 遍历当前报表所有指标
                for (CheckIndexModel checkModel : index_model_table) {
                    params.put("indexCode", checkModel.getIndexCode());
                    params.put("reportTable", checkModel.getReportTable());
                    params.put("rowNum", checkModel.getRowNum());
                    params.put("columnNum", checkModel.getColumnNum());
                    params.put("rowName", checkModel.getRowName());
                    params.put("listName", checkModel.getListName());
                    params.put("indexType", checkModel.getIndexType());
                    params.put("express", checkModel.getExpress());
                    params.put("allowDeviation", checkModel.getAllowDeviation());
                    params.put("indexDetail", checkModel.getIndexDetail());
                    params.put("indexName", checkModel.getIndexName());
                    params.put("indexRule", checkModel.getIndexRule());
                    params.put("correctPrompt", checkModel.getCorrectPrompt());
                    params.put("errorPrompt", checkModel.getErrorPrompt());
                    params.put("remark", checkModel.getRemark());
                    params.put("relation_tables", checkModel.getRelationTables());
                    params.put("tables_relationships", checkModel.getTablesRelationships());
                    params.put("coordinate_type", dataValidateDao.getReportTableCoordinateType(reportTable));//报表维度
                    params.put("reflect_column", dataValidateDao.getReflectColumnByColumnNum(reportTable, checkModel.getColumnNum()));//映射报表字段

                    // 获取拼装sql, 查询出要校验的指标数据
                    IndexCodeDTO indexCodeDTO = IndexCodeDTO.dto().initIndexCodeDto(checkModel).setDealDateRe(params.get("deal_date"));
                    List<Map<String, String>> indexDataList=null;//指标定制查询的数据
                    if(StringUtils.isNotEmpty(indexCodeDTO.getRelationTables())){
                        indexDataList = dataValidateDao.indexTablesSqlHandler(
                                getIndexCodeSql(indexCodeDTO,"0", 0, 0, ""),indexCodeDTO );
                    }
                    // 二维报表每天只有一条汇总数据
                    Map<String, Object> resParam = new HashMap<>();
                    if (StringUtils.isNotEmpty(indexCodeDTO.getRelationTables()) && CollectionUtil.isNotEmpty(indexDataList)) {
                        resParam = dataValidateDao.checkC2ReportDataByColumnCode(params, null, indexDataList, params2);
                    }
                    // 清空报送数据
                    if(indexDataList!=null) indexDataList.clear();
                    // base_data_validation记录数据校验不通过信息
                    if (ObjectUtil.isNotEmpty(resParam)) {
                        PrimaryDataCheckUtil.unsatisfiedValidateDataRecord(resParam);
                    }
                }
            } else if (ReportCoordinateEnum.COOR_D.getVal().equals(coordinateType)) {
                /**
                 * 一维坐标报表(中债/资金信托/行内客制化报表等)
                 * 遍历数据行,对每一行数据进行校验
                 */
                List<Map<String, Object>> c1RowDataList = (List<Map<String, Object>>) params1.get(reportTable);
                if (c1RowDataList.size() <= 0) {
                    log.info("报送数据表: " + reportTable + " 无待报送数据, 跳过该表校验");
                    continue;// 报送数据表校验日不存在数据，跳过该报表校验并提示
                }

                for (Map<String, Object> c1_params : c1RowDataList) {//每行的数据map集合，key为字段名，value为字段值
                    /**
                     * 遍历所有指标
                     */
                    for (CheckIndexModel checkModel : index_model_table) {
                        params.put("indexCode", checkModel.getIndexCode());
                        params.put("reportTable", checkModel.getReportTable());
                        params.put("rowNum", checkModel.getRowNum());
                        params.put("columnNum", checkModel.getColumnNum());
                        params.put("rowName", checkModel.getRowName());
                        params.put("listName", checkModel.getListName());
                        params.put("indexType", checkModel.getIndexType());
                        params.put("express", checkModel.getExpress());
                        params.put("allowDeviation", checkModel.getAllowDeviation());
                        params.put("indexDetail", checkModel.getIndexDetail());
                        params.put("indexRule", checkModel.getIndexRule());
                        params.put("correctPrompt", checkModel.getCorrectPrompt());
                        params.put("errorPrompt", checkModel.getErrorPrompt());
                        params.put("remark", checkModel.getRemark());
                        params.put("relation_tables", checkModel.getRelationTables());
                        params.put("tables_relationships", checkModel.getTablesRelationships());
                        params.put("coordinate_type", dataValidateDao.getReportTableCoordinateType(reportTable));//报表维度
                        params.put("reflect_column", dataValidateDao.getReflectColumnByColumnNum(reportTable, checkModel.getColumnNum()));//映射报表字段

                        dataValidateDao.checkC1ReportDataByColumnCode(params, c1_params);
                    }
                }
            } else if (ReportCoordinateEnum.COOR_C.getVal().equals(coordinateType) || ReportCoordinateEnum.COOR_A.getVal().equals(coordinateType)) { //人行dat文件校验
                int counts = 0;
                ExecutorService taskPools = Executors.newFixedThreadPool(Integer.parseInt(SysUtil.getSystemParamsByParaid("10022")));
                //if(index_model_table.size()>0 )dataValidateDao.updateSuccess(reportTable, params.get("deal_date"));//有指标才更新状态

                /**
                 * 根据校验指标遍历处理
                 * 每个指标单独根据表
                 */
                for (CheckIndexModel checkModel : index_model_table) {

                    taskPools.execute(new Runnable() {
                        @SneakyThrows
                        public void run() {
                            log.info("指标[" + checkModel.getIndexCode() + "]校验,当前线程:" + Thread.currentThread().getName() + "序号:" + counts);
                            long index_start_time = System.currentTimeMillis();
                            List<Map<String, Object>> paramsList = new ArrayList<>();
                            Map<String, String> params_ep = new HashMap<>();
                            params_ep.putAll(params);
                            params_ep.put("indexCode", checkModel.getIndexCode());
                            params_ep.put("reportTable", checkModel.getReportTable());
                            params_ep.put("rowNum", checkModel.getRowNum());
                            params_ep.put("columnNum", checkModel.getColumnNum());
                            params_ep.put("rowName", checkModel.getRowName());
                            params_ep.put("listName", checkModel.getListName());
                            params_ep.put("indexName", checkModel.getIndexName());
                            params_ep.put("indexType", checkModel.getIndexType());
                            params_ep.put("express", checkModel.getExpress());
                            params_ep.put("allowDeviation", checkModel.getAllowDeviation());
                            params_ep.put("indexDetail", checkModel.getIndexDetail());
                            params_ep.put("correctPrompt", checkModel.getCorrectPrompt());
                            params_ep.put("indexRule", checkModel.getIndexRule());
                            params_ep.put("errorPrompt", checkModel.getErrorPrompt());
                            params_ep.put("remark", checkModel.getRemark());
                            params_ep.put("relation_tables", checkModel.getRelationTables());
                            params_ep.put("tables_relationships", checkModel.getTablesRelationships());
                            params_ep.put("coordinate_type", coordinateType);//报表维度
                            params_ep.put("remark_column", reflectColumnList.get(params_ep.get("reportTable")));
                            List<Map<String, String>> indexDataList;
                            String indexCodeSql;
                            // 获取拼装sql
                            IndexCodeDTO indexCodeDTO = IndexCodeDTO.dto().initIndexCodeDto(checkModel).setDealDateRe(params.get("deal_date"));
                            int last_id=0;
                            int total_size = 0;
                            while(!"1".equals(SysUtil.getSystemParamsByParaid("90000051212")) ){//实时查询系统参数，避免锁死
                                indexCodeSql=getIndexCodeSql(indexCodeDTO,checkModel.getSplitFlag(),last_id,now_id,taId);
                                // 查询出要校验的指标数据
                                indexDataList= dataValidateDao.indexTablesSqlHandler(indexCodeSql, indexCodeDTO);
                                total_size = indexDataList.size();
                                if(indexDataList!= null && total_size==0){
                                    log.info("本条指标[{}]未查到报送数据，退出校验",checkModel.getIndexCode());
                                    break;
                                }
                                // 循环校验指标
                                for (Map<String, String> row_map : indexDataList) {
                                    //如果结果集中有ID字段，那么得到本次查询的最大ID
                                    String id=row_map.get(checkModel.getReportTable()+".ID");
                                    if(id!=null && last_id<Integer.parseInt(id)) last_id=Integer.parseInt(id);
                                    Map<String, Object> paramsAa = dataValidateDao.checkPBCReportDataByColumnCode(params_ep, row_map);
                                    if (ObjectUtil.isNotEmpty(paramsAa)) {
                                        paramsList.add(paramsAa);
                                    }
                                }
                                //base_data_validation记录数据校验不通过信息
                                PrimaryDataCheckUtil.unsatisfiedValidateDataRecord(params_ep, paramsList, checkModel.getIndexCode(), params.get("deal_date"));
                                if(!"1".equals(checkModel.getSplitFlag())) break;//如果不分页，第一次就退出
                                params_ep = null ;
                                paramsList = null ;
                            }

                            long cost_time = (System.currentTimeMillis() - index_start_time);
                            if(cost_time > 10000){/*记录超过10秒的所有指标*/
                                PrimaryDataCheckUtil.recordLongCostIndexInfo(checkModel.getIndexCode(), cost_time, params.get("deal_date"), checkModel.getReportTable(), total_size);
                            }
                            log.info("指标[" + checkModel.getIndexCode() + "]校验结束,当前线程:" + Thread.currentThread().getName() + ",序号:" + counts + ",耗时:" + cost_time);
                        }

                    });

                }
                taskPools.shutdown();
                taskPools.awaitTermination(1800, TimeUnit.SECONDS);
                log.info(" ##### 指标校验完成！总耗时: {} ms",  System.currentTimeMillis() - startTime);
            }
        }
    }


    /**
     * 根据报表大类组装一维报表报送数据map集合
     * @param coordinate_type 报表维度
     * @return
     * @throws Exception
     */
    public Map<String, Object> getCoordinateOneByType (String coordinate_type, String report_category, String report_date) throws Exception {
        Map<String, Object> params_o = new HashMap<>();
        List<String> reportList = dataValidateDao.getReportInfoList(coordinate_type, report_category);//获取所有一维/台账报表有序集合
        for (String report_table : reportList) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            mapList = dataValidateDao.getCoOneReportDataConfig(report_table, report_date);
            params_o.put(report_table, mapList);
        }
        return params_o;
    }

    /**
     * 根据报表大类组装二维报表报送数据map集合
     * @param coordinate_type 报表维度
     * @return
     * @throws Exception
     */
    public Map<String, Object> getCoordinateTwoByType (String coordinate_type, String report_category, String report_date) throws Exception {
        Map<String, Object> params_t = new HashMap<>();
        List<String> reportList = dataValidateDao.getReportInfoList(coordinate_type, report_category);
        for (String report_table : reportList) {
            Map<String, Object> map = new HashMap<>();
            map = dataValidateDao.getCoTwoReportDataConfig(report_table, report_date);
            params_t.put(report_table, map);
        }
        return params_t;
    }

    /**
     * 查询指标是否存在
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Boolean exitIndexCode(Map<String, String> params) throws Exception {
        return dataValidateDao.exitIndexCode(params);
    }

    public void loadDict() throws Exception {
        if (ObjectUtil.isNotEmpty(DirectParams.dict_map_check)) {
            return;
        }
        List<SqlRow> sqlRows = dataValidateDao.loadDict();
        ExpressDictUtil.dictMap = paramToMap(sqlRows);
    }

    /**
     * 处理指标拼装sql
     *
     * @return
     */
    public String getIndexCodeSql(IndexCodeDTO indexCodeDTO, String splitFlag, int last_id, int id, String taId) throws Exception {
        // 参数准备
        String subjectTable = "";
        StringBuilder querySql = new StringBuilder();
        String coordinateType = dataValidateDao.getReportTableCoordinateType(indexCodeDTO.getReportTable());
        String dealDate = indexCodeDTO.getDealDate();
        String relationTables = indexCodeDTO.getRelationTables();
        String tablesRelationships = indexCodeDTO.getTableRelationships();
        boolean isCondition = relationTables.contains("sub(");

        //切割表合集
        String[] tables = relationTables.split(";");

        // 多表查询情况
        if (tables.length > 1) {
            // 关联关系合集
            if(StringUtils.isEmpty(tablesRelationships) ) throw new PromptException("字段[tablesRelationships]不能为空");
            String[] relationships = tablesRelationships.split(",");
            querySql.append("select * from ");
            for (int i = 0; i < tables.length; i++) {//遍历
                if (tables[i].contains("sub(")) {
                    //找到sub需要加时间条件的表
                    subjectTable = tables[i].substring(4, tables[i].length() - 1);
                    tables[i] = tables[i].substring(4, tables[i].length() - 1);

                    if (subjectTable.contains("select")) {
                        //子查询取表别名
                        subjectTable = subjectTable.contains(") ") ? subjectTable.substring(subjectTable.lastIndexOf(" ") + 1) : subjectTable;
                    } else {
                        //单表取表别名
                        subjectTable = subjectTable.contains(" ") ? subjectTable.substring(subjectTable.lastIndexOf(" ") + 1) : subjectTable;
                    }
                }
                if (i > 0) {
                    querySql.append(" left join ").append(tables[i]).append(" on ").append(relationships[i - 1]).append(" ");
                } else {
                    querySql.append(tables[i]);
                }

            }

        } else {
            // 单表查询
            String query_table = relationTables;
            if (relationTables.contains("sub(")) {
                //找到sub需要加时间条件的表
                subjectTable = relationTables.substring(4, relationTables.length() - 1);
                query_table = subjectTable;
                //取表别名
                subjectTable = subjectTable.contains(" ") ? subjectTable.substring(subjectTable.lastIndexOf(" ") + 1) : subjectTable;
            }
            querySql.append("select * from ").append(query_table);

        }
        if (isCondition) {
            if ("2".equals(coordinateType)) {
                querySql.append(" where ").append(subjectTable).append(".report_date = '").append(dealDate).append("'");
            } else {
                querySql.append(" where ").append(subjectTable).append(".theory_report_start_date = '").append(dealDate).append("'");
            }
            // 特殊：投资者身份页面修改，字需要单条数据校验（为了给前端调用指标使用的）
            if ("app_cust_register_info".equals(indexCodeDTO.getReportTable()) && id > 0) {
                querySql.append(" and ").append(subjectTable).append(".id = "+id);
            }
            // 特殊：投资者持有、子产品、明细需要单条数据校验（为了给前端调用指标使用的）
            if (("app_cust_vol_register_info".equals(indexCodeDTO.getReportTable()) ||
                    "app_cust_vol_register_sub_info".equals(indexCodeDTO.getReportTable()) ||
                    "app_cust_trans_info".equals(indexCodeDTO.getReportTable())) && StringUtils.isNotEmpty(taId)) {
                querySql.append(" and ").append(subjectTable).append(".ta_id = '"+taId+"'");
            }

            querySql.append(" and ").append(subjectTable).append(".register_status not in ('3','9') ");
            querySql.append(" and ").append(subjectTable).append(".sys_data_status ='1' ");

            if ("021018".equals(indexCodeDTO.getIndexCode())) {
                querySql.append(" and ").append(subjectTable).append(".PROD_IDENT_CODE not in (select distinct prod_cd from dwd_prd_prd_bas_inf where is_prod_transfer = '01') ");
            }
        }
        if("1".equals(splitFlag)){//需要分页
            String limit = SysUtil.getSystemParamsByParaid("90000051211"); // 查询分页数
            querySql.append(" and id>").append(last_id).append(" limit ").append(limit);
        }
        // 校验规则替换占位符
        String sql = querySql.toString();
        if (sql.contains("$S{report_date}") || sql.contains("$S{theory_report_start_date}")) {
            sql = sql.replace("$S{report_date}", "'" + dealDate + "'");
            sql = sql.replace("$S{theory_report_start_date}", "'" + dealDate + "'");
        }

        return sql;
    }

    public Map<String, List<ExpressDictDTO>> paramToMap(List<SqlRow> sqlRow){
        Map<String ,List<ExpressDictDTO>> listToMap = new HashMap<>();
        List<String> str = sqlRow.stream().map(map -> map.getString("dict")).distinct().collect(Collectors.toList());
        for (String dict : str ) {
            List<ExpressDictDTO> list = new ArrayList<>();
            for (SqlRow sr : sqlRow) {
                String dict1 = sr.getString("dict");
                if (dict.equals(dict1)){
                    ExpressDictDTO dictDTO = new ExpressDictDTO();
                    dictDTO.setDictKey(sr.getString("k"));
                    dictDTO.setDictVal(sr.getString("v"));
                    dictDTO.setDictKv(sr.getString("k") + " " + sr.getString("v"));
                    list.add(dictDTO);
                }
            }
            listToMap.put(dict,list);
        }
        return listToMap ;
    }

    /**
     * 根据报送日期查询数据日期
     * @param dParams
     * @return
     */
    public String calDataDateByReportDate (Map<String, String> dParams) throws Exception {
        return dataValidateDao.checkDataDate(dParams);
    }

    /**
     * 判断根据基准日期顺延工作日天数是否满足报送数据出数日期
     * @param base_date
     * @param delay_days
     * @return
     * @throws Exception
     */
    public boolean isFitTheDelayDay (String base_date, int delay_days, String deal_date, String delay_type) {
        String cal_date = "";
        try {
            cal_date = dataValidateDao.calReportGenerDate(base_date, delay_days, delay_type);
        } catch (Exception e) {
            log.error("根据基准日期顺延工作日天数计算报送数据出数日期异常:", e.getMessage());
        }
        log.info("计算报表数据生成日期为:" + cal_date);
        return deal_date.equals(cal_date);
    }

    /**
     * 查询报表大类
     * @param params
     */
    public List<SqlRow> getReportTable(Map<String, Object> params) throws Exception {
        return dataValidateDao.getReportTable(params);
    }

    /**
     * 查询报表集合
     * @param params
     */
    public List<SqlRow> getReportTableList(Map<String, String> params) throws Exception {
        return dataValidateDao.getReportTableList(params);
    }

}
