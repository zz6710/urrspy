package com.kayak.report.dao;

import com.alibaba.fastjson.JSONObject;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.config.model.AppProdBondPropertyInfoConfig;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.report.model.EmailBizCheckSubTableDto;
import com.kayak.report.model.ReportDataLockRecord;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ReportDataLockRecordDao extends ComnDao {
    private static Logger logger = LogManager.getLogger(ReportDataLockRecordDao.class);
    @Autowired
    AppProdBondPropertyInfoConfig appProdBondPropertyInfoConfig;

    /**
     * 查询报送数据锁表记录子查询
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<ReportDataLockRecord> findReportDataLockRecordInfo(SqlParam<ReportDataLockRecord> params) throws Exception {
        String sql = "SELECT distinct a.id,a.report_table,a.table_name,a.report_date,a.lock_status,a.opt_user,a.opt_date,a.opt_time " +
                "       FROM base_report_data_lock_record a " +
                "       LEFT JOIN base_report_data_lock_config b on b.report_table = a.report_table " +
                "      WHERE a.report_table = $S{reportTable} ";
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql = sql + " and a.report_date = '" + DateUtil.getMonthEndDate(params.getModel().getReportDate()) + "'";//获取指定月份最后一个自然日
        }

        sql = sql + " order by a.report_date desc ";
        return super.findRows(sql, params);
    }

    /**
     * 添加or解除报表数据锁定
     *
     * @param params
     * @throws Exception
     */
    public void operateReportData(SqlParam<ReportDataLockRecord> params, String username) throws Exception {
        String sql = "update base_report_data_lock_record b " +
                "         set b.lock_status = '" + params.getModel().getLockStatus() + "', " +
                "             b.opt_user = '" + username + "', " +
                "             b.opt_date = date_format(sysdate(), '%Y%m%d')," +
                "             b.opt_time = date_format(sysdate(), '%H%i%s') " +
                "       where b.report_date = '" + params.getModel().getReportDate() + "'" +
                "         and b.report_table = '" + params.getModel().getReportTable() + "'";
        String sql2 = "update base_report_data_lock_config a " +
                "         set a.is_lst_lock = '" + params.getModel().getLockStatus() + "' " +
                "       where a.report_table = '" + params.getModel().getReportTable() + "' " +
                "         and a.latest_date = '" + params.getModel().getReportDate() + "' ";
        super.update(sql, DataSourceProperty.PUB, null);//锁定/解锁记录表信息
        super.update(sql2, DataSourceProperty.PUB, null);//更新主表信息最近报送日锁定/解锁字段信息
    }

    /**
     * 锁定报送表，检查其依赖上游中间表是否锁定
     *
     * @param params
     * @return
     * @throws Exception
     */
    public String checkUpperGradeValidate(SqlParam<ReportDataLockRecord> params) throws Exception {
        String err_msg = "";
        String sql = "select a.report_table,a.table_name as table_name1, c.lock_status, c.report_date, b.table_name " +
                "       from base_report_data_lock_config a " +
                "       left join base_report_data_lock_config b on a.upper_grade = b.task_id " +
                "       left join base_report_data_lock_record c on c.report_date = '" + params.getModel().getReportDate() + "' and c.report_table = b.report_table " +
                "      where a.report_table = '" + params.getModel().getReportTable() + "' " +
                "        and c.lock_status = '02' ";
        List<SqlRow> rowsList = super.findRows(sql, DataSourceProperty.PUB);
        for (SqlRow row : rowsList) {
            params.getModel().setTableName(row.getString("table_name1"));
            err_msg = err_msg + row.getString("table_name") + '，';
        }
        if (err_msg.length() > 0) {
            err_msg = err_msg.substring(0, err_msg.length() - 1);
        }
        return err_msg;
    }

    /**
     * 解锁中间表，检查中间表下游报表是否未锁
     *
     * @param params
     * @return
     * @throws Exception
     */
    public String checkLowerGradeValidate(SqlParam<ReportDataLockRecord> params) throws Exception {
        String err_msg = "";

        String sql = "select c.table_name as table_name,a.table_name as table_name1  " +
                "  from base_report_data_lock_config a " +
                "  left join base_report_data_lock_config b on b.upper_grade = a.task_id " +
                "  left join base_report_data_lock_record c on c.report_table = b.report_table and c.report_date = '" + params.getModel().getReportDate() + "' " +
                " where a.report_table = '" + params.getModel().getReportTable() + "' " +
                "   and c.lock_status = '01' ";
        List<SqlRow> rowsList = super.findRows(sql, DataSourceProperty.PUB);
        for (SqlRow row : rowsList) {
            params.getModel().setTableName(row.getString("table_name1"));
            err_msg = err_msg + row.getString("table_name") + ',';
        }
        if (err_msg.length() > 0) {
            err_msg = err_msg.substring(0, err_msg.length() - 1);
        }
        return err_msg;
    }

    /**
     * set集合转字符串
     *
     * @param set
     * @param symbol 连接符号
     * @return
     */
    public String setToStr(Set<String> set, String symbol) {
        if (set.size() == 0) {
            return "";
        }

        String str = "";
        for (String mem : set) {
            str = str + mem + symbol;
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 将set中元素转、转移至目标Set中
     *
     * @param fromSet
     * @param toSet
     * @return
     */
    public Set<String> setToSet(Set<String> fromSet, Set<String> toSet) {
        if (fromSet.size() == 0) {
            return toSet;
        }

        for (String mem : fromSet) {
            toSet.add(mem);
        }
        return toSet;
    }


    //锁定 G06穿透前（调整后） 和 底层估值明细（调整后） 时，需要触发 新增业务报表app_prod_bond_property_info数据  及 数据清理（若有对应date的历史数据）
    //			String delete =""; //先删report-date月数据
    //			String insert =""; //汇总插入 report-date月数据
    //			String insert = "";//新增一条发邮件任务记录
    // 不可影响现有的业务，最好能调整为异步任务处理，并支持重试

    /***
     *
     * @param subTable  操作的子表
     * @param reportDate 数据业务日期
     */
    public void generateDataAndTaskRecord(String subTable, String reportDate) {
        if (reportDate == null || reportDate == "") {
            logger.error("generateDataAndTaskRecord failed ; cause reportDate is" + reportDate);
            return;
        }
        Map<String, String> params = new HashMap<>();
        String createDate = DateUtil.getNowDate();
        String createTime = DateUtil.getNowTime();
        String bizType = appProdBondPropertyInfoConfig.getTableUpperName();
        String bizName = appProdBondPropertyInfoConfig.getBizName();
        //多个以英文分号分割，可为空  单个方便配置-对应业务，多个不方便，存在一一对应问题
//        String fileName = appProdBondPropertyInfoConfig.getFileName();
        String bizDate = reportDate;
        // 取 系统配置
//        String remotePath = "/uurs/email/";
        String remotePath = appProdBondPropertyInfoConfig.getRemotePath();
        //取 系统配置
        String bizTable = appProdBondPropertyInfoConfig.getTableUpperName();
        //取数的sql 对应的配置ID BOND_RE
        String exeId = appProdBondPropertyInfoConfig.getExeId();
        //需要执行发邮件任务  1发送  0不发
        String taskFlag = "1";
        //备用记录状态字段  1启用  0停用
        String status = "1";
        try {
            String delSql = "delete from app_prod_bond_property_info where report_date ='" + reportDate + "'";
            String addSql = ExeQuery.queryExeId(exeId);
//            logger.info(exeId+":"+addSql);
            String addTaskSql = formatAddEmailBizTaskSql(bizType, bizName, bizDate, remotePath, bizTable, taskFlag, status);
            //无法从表中判断唯一  可以先以主表记录去查询
            String bizReportCheckTable = "dwd_sum_buttom_asset,dws_prod_ttrd_bef_g06a2";
            //汇总数据的总表
            String bizReportTable = bizTable;
            //插入业务操作记录（锁定子表记录时）
            UpdateResult subAddResult = super.update(formatAddEmailBizCheckTableInfoSql(bizTable, bizReportCheckTable, subTable, reportDate), new HashMap<>());
            logger.info("subAddResult:" + subAddResult.getEffect());
            //判断是否需要取数 新增邮件任务触发记录  ,复合标准，去取数并操作记录
            Boolean flag = checkSubTableLockWholeTable(bizReportCheckTable, bizTable, reportDate);
            if (flag) {
                super.daoService.doTrans(() -> {
                    Map<String, String> dateParams = new HashMap<>();
                    //应对查询到的sql未做model映射时大小写敏感问题 （model在rpt中，不引入此处）
                    dateParams.put("report_date", reportDate);
                    dateParams.put("REPORT_DATE", reportDate);
                    //删除主表数据
                    UpdateResult delResult = super.update(delSql, dateParams);
                    //生成主表数据
                    UpdateResult addSqlResult = super.update(addSql,dateParams);
                    logger.info("addSqlResult:" + addSqlResult + "; addSqlResult count:" + addSqlResult.getEffect());
                    UpdateResult addTaskResult = new UpdateResult();
                    if (addSqlResult.getEffect() > 0) {
                        addTaskResult = super.update(addTaskSql, dateParams);
                    }
                    logger.info("delResult:" + delResult.getEffect() + "; addSqlResult:" + addSqlResult.getEffect() + "; addTaskResult:" + addTaskResult.getEffect());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" 打标为房地产明细数据锁定 汇总数据异常 generateDataAndTaskRecord failed; Excption:{}", e.getMessage());
        }
    }

    /**
     * 从dps模块中 新增任务表记录 对应sql
     *
     * @param bizType
     * @param bizName
     * @param bizDate
     * @param remotePath
     * @param bizTable
     * @param taskFlag
     * @param status     //     * @param fileName 本地临时文件名-发邮件时的附件名-远程S3上的文件名
     * @return
     */
    public String formatAddEmailBizTaskSql(String bizType, String bizName, String bizDate, String remotePath, String bizTable, String taskFlag, String status) {
        String createTime = DateUtil.getNowTime();
        String createDate = DateUtil.getNowDate();
        StringBuilder addTaskSql = new StringBuilder("INSERT INTO email_biz_task(biz_type,biz_name,biz_date,remote_path,biz_table,task_flag,status,create_time,create_date) VALUES(");
//        StringBuilder addTaskSql = new StringBuilder("INSERT INTO email_biz_task(biz_type,biz_name,biz_date,file_name,remote_path,biz_table,task_flag,status,create_time,create_date) VALUES(");
        addTaskSql.append("'").append(bizType).append("',");
        addTaskSql.append("'").append(bizName).append("',");
        addTaskSql.append("'").append(bizDate).append("',");
//        addTaskSql.append("'").append(fileName).append("',");
        addTaskSql.append("'").append(remotePath).append("',");
        addTaskSql.append("'").append(bizTable).append("',");
        addTaskSql.append("'").append(taskFlag).append("',");
        addTaskSql.append("'").append(status).append("',");
        addTaskSql.append("'").append(createTime).append("',");
        addTaskSql.append("'").append(createDate).append("')");
//        logger.info("addTaskSql:"+addTaskSql);
        return addTaskSql.toString();
    }

    /**
     * 向业务锁定校验表中新增一条子表锁定记录
     *
     * @param bizReportTable    业务汇总表
     * @param bizCheckTableInfo 校验子表 拼接的多个子表，逗号分割
     * @param bizReportSubTable 操作的单个业务子表
     * @param reportDate
     */
    public String formatAddEmailBizCheckTableInfoSql(String bizReportTable, String bizCheckTableInfo, String bizReportSubTable, String reportDate) {
//        String bizReportTable ="";
//        String bizCheckTableInfo ="";
//        String bizReportSubTable ="";
//        String reportDate =reportDate;
        String createTime = DateUtil.getNowTime();
        String createDate = DateUtil.getNowDate();
        StringBuilder subAddSql = new StringBuilder("INSERT INTO email_biz_check_table_info(biz_report_table,biz_check_table_info,biz_report_sub_table,report_date,create_time,create_date) VALUES(");
        subAddSql.append("'").append(bizReportTable).append("',");
        subAddSql.append("'").append(bizCheckTableInfo).append("',");
        subAddSql.append("'").append(bizReportSubTable).append("',");
        subAddSql.append("'").append(reportDate).append("',");
        subAddSql.append("'").append(createTime).append("',");
        subAddSql.append("'").append(createDate).append("')");
        logger.info("subAddSql:" + subAddSql);
        return subAddSql.toString();
    }

    /**
     * 判断是否已有对应子表的锁定(页面锁定)记录
     *
     * @param subTable
     * @param reportDate
     * @return
     */
    public boolean checkBizSubTableInfo(String subTable, String reportDate) {
        String bizReportTable = "";
        String bizCheckTableInfo = "";
        String bizReportSubTable = "";
//        String reportDate =reportDate;
        StringBuilder countSubTableSql = new StringBuilder("SELECT biz_report_sub_table from email_biz_check_table_info  where ");
        countSubTableSql.append(" biz_report_table ='").append(bizReportTable).append("' ");
        countSubTableSql.append(" and biz_check_table_info ='").append(bizCheckTableInfo).append("' ");
        countSubTableSql.append(" and biz_report_sub_table = '").append(bizReportSubTable).append("' ");
        countSubTableSql.append(" and report_date = '").append(reportDate).append("' ");
        try {
            SqlResult reuslt = super.findRows(countSubTableSql.toString(), null);
            if (reuslt.getRows().size() > 0) {
                logger.info("checkBizSubTableInfo 存在子表锁定记录 reportDate:" + reportDate + "; subTable:" + subTable);
                return true;
            } else {
                logger.info("checkBizSubTableInfo 不存在子表锁定记录 reportDate:" + reportDate + "; subTable:" + subTable);
            }
        } catch (Exception e) {
            logger.info("checkBizSubTableInfo 不存在子表锁定记录 reportDate:" + reportDate + "; subTable:" + subTable + "; Exception:" + e.getMessage());
        }
        return false;
    }

    /**
     * 1.查询获取到子表信息，表记录中都有子表锁定记录，子表都有且只有一条reportDate的。 第一次锁定。
     * 2.查询获取到子表信息，表记录中都有子表锁定记录，单个子表不只有一条记录，再次锁定。 【两种情况都需要发邮件，固定只要锁定触发查找 -distinct子表记录都存在，就新增一条发邮件记录】
     *
     * @param bizCheckTableInfo 要校验的子表汇总  多个以逗号隔开
     * @param bizTable          业务主表
     * @param reportDate        业务数据日期
     * @return
     */
    public boolean checkSubTableLockWholeTable(String bizCheckTableInfo, String bizTable, String reportDate) {

        StringBuilder wholeSubTableSql = new StringBuilder("SELECT distinct biz_report_sub_table from email_biz_check_table_info  where ");
        wholeSubTableSql.append(" biz_report_table ='").append(bizTable).append("' ");
        wholeSubTableSql.append(" and report_date ='").append(reportDate).append("' ");
        try {
            //要校验的子表为空，不做校验
            if (StringUtils.isEmpty(bizCheckTableInfo)) {
                return true;
            }
            logger.info("wholeSubTableSql:" + wholeSubTableSql.toString());
            List<EmailBizCheckSubTableDto> dtolist = super.findRows(EmailBizCheckSubTableDto.class, wholeSubTableSql.toString(), DataSourceProperty.PUB, null);
            if (dtolist == null || dtolist.size() == 0) {
                return false;
            }
            logger.info("maplist:" + dtolist + "; reuslt.get(0):" + dtolist.get(0));
            StringBuilder stb = new StringBuilder();
            for (EmailBizCheckSubTableDto dto : dtolist) {
                if (!bizCheckTableInfo.contains(dto.getBizReportSubTable())) {
                    logger.info("dto:" + dto);
                    return false;
                }
//                stb.append(dto.getBizReportSubTable()).append(",");
            }
            return true;
//            if(bizCheckTableInfo.equals(stb.substring(0,stb.length()-1))){
//                return true;
//            }
        } catch (Exception e) {
            logger.info("checkBizSubTableInfo 缺少关联子表锁定记录 reportDate:" + reportDate + "; bizTable:" + bizTable + "; Exception:" + e.getMessage());
        }
        return false;
    }

}