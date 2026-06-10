package com.kayak.pms.disclosureControl.dao;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.disclosureControl.model.DisclosureProdTask;
import com.kayak.pms.disclosureControl.model.IdbNoticeGridConfigSource;
import com.kayak.pms.disclosureControl.model.ScheduleNotice;
import com.kayak.pms.global.constants.DisclosureSonType;
import com.kayak.pms.global.constants.TaskStatus;
import com.kayak.pms.global.constants.XpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DisclosureProdTaskDao extends ComnDao {

    public SqlResult<DisclosureProdTask> findT8DisclosureProdTasks(SqlParam<DisclosureProdTask> params) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT  " +
                "  task.id, " +
                "  task.prod_code, " +
                "  tpi.PROD_NM prod_name, " +
                "  task.crt_task_date, " +
                "  task.t8_disclosure_prod_rule_id, " +
                "  task.t8_disclosure_rule_id, " +
                "  task.notice_title, " +
                "  task.prod_base_date, " +
                "  task.sys_crt_date, " +
                "  task.task_month, " +
                "  task.status, " +
                "  task.crt_date, " +
                "  task.crt_time, " +
                "  task.crt_user_id, " +
                "  task.crt_user_name, " +
                "  task.upd_date, " +
                "  task.upd_time, " +
                "  task.upd_user_id, " +
                "  task.upd_user_name, " +
                "  task.remark, " +
                "  task.data_source, " +
                "  task.disclosure_type, " +
                "  task.disclosure_son_type " +
                " FROM " +
                "  idb_disclosure_prod_task task  " +
                "  LEFT JOIN APP_PRD_BAS_INF tpi  " +
                "    ON task.prod_code = tpi.prod_cd  where 1 = 1 and task.disclosure_son_type not in ('"+ DisclosureSonType.netValueEntity.getItemKey()+"')" );


        if(StringUtils.isNotBlank(params.getModel().getDisclosureType())){
            sql.append(" and task.disclosure_type = '"+params.getModel().getDisclosureType()+"' ");
        }
        if(StringUtils.isNotBlank(params.getModel().getDisclosureSonType())){
            sql.append(" and task.disclosure_son_type = '"+params.getModel().getDisclosureSonType()+"' ");
        }
        if(StringUtils.isNotBlank(params.getModel().getStartMonth())){
            sql.append(" AND task.task_month >=  '" + params.getModel().getStartMonth() + "' ");
        }

        if (StringUtils.isNotBlank(params.getModel().getEndMonth())) {
            sql.append(" AND task.task_month <= '" + params.getModel().getEndMonth() + "' ");
        }

        if (StringUtils.isNotBlank(params.getModel().getProdName())) {
            sql.append(" and tpi.prod_nm like '%" + params.getModel().getProdName() + "%'");
        }
        if (StringUtils.isNotEmpty(params.getModel().getProdBaseDate())) {
            sql.append(" and task.prod_base_date=$S{prodBaseDate}");
        }
        if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
            sql.append(" and task.prod_code=$S{prodCode}");
        }
        if (StringUtils.isNotEmpty(params.getModel().getStatus())) {
            sql.append(" and task.status=$S{status}");
        }
        if (StringUtils.isNotEmpty(params.getModel().getDataSource())) {
            sql.append(" and task.data_source=$S{dataSource}");
        }
        sql.append(" order by task.id desc ");
        return super.findRows(sql.toString(),
                DataSourceProperty.IDB, params);
    }

    public List<DisclosureProdTask> findExistProcess(String taskId) throws Exception {
        String sql = "select process.id from ( " +
                " select notice.id from idb_disclosure_notice notice " +
                " where notice.task_id = $S{taskId} " +
                " )notice " +
                " join idb_disclosure_notice_process process " +
                " on notice.id = process.t8_disclosure_notice_id " +
                " where process.input_status = '1' ";

        return super.findRows(DisclosureProdTask.class, sql, 0, taskId);

    }

    public void addT8DisclosureProdTask(DisclosureProdTask task) throws Exception {
        super.update("INSERT INTO idb_disclosure_prod_task(id,prod_code," +
                        "crt_task_date,t8_disclosure_rule_id,t8_disclosure_prod_rule_id,notice_title,prod_base_date,sys_crt_date,task_month,status," +
                        "crt_date,crt_time,crt_user_id,crt_user_name," +
                        "remark,data_source,report_date,disclosure_type,disclosure_son_type) " +
                        "VALUES($AUTOIDS{id},$S{prodCode}," +
                        " $S{crtTaskDate},$S{t8DisclosureRuleId},$S{t8DisclosureProdRuleId},$S{noticeTitle},$S{prodBaseDate},$S{sysCrtDate},$S{taskMonth},$S{status}," +
                        " $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
                        " $S{remark},$S{dataSource},$S{reportDate},$S{disclosureType},$S{disclosureSonType})",
                DataSourceProperty.IDB,task);
    }

    /**
     * @功能描述:该方法执行插入任务前删除未生成公告的任务数据
     * @params:[task]
     * @return:void
     * @Athor:ouyifan
     * @date:2022/9/1
     */
    public void deleteBeforeAdd(DisclosureProdTask task) throws Exception {
        String sql = "DELETE FROM idb_disclosure_prod_task task WHERE  1=1 AND task.status='"+TaskStatus.forGenerate.getItemKey()+"' and year(task.prod_base_date) = year($S{startDate})  ";
        //基准日期
        if (StringUtils.isNotEmpty(task.getProdBaseDate())) {
            sql+=" and task.prod_base_date=$S{prodBaseDate}";
        }
        //信披类型
        if (StringUtils.isNotEmpty(task.getDisclosureType())) {
            sql+=" and task.disclosure_type=$S{disclosureType}";
        }
        //信披子类型
        if (StringUtils.isNotEmpty(task.getDisclosureSonType())) {
            sql+=" and task.disclosure_son_type=$S{disclosureSonType}";
        }
        //产品代码
        if (StringUtils.isNotEmpty(task.getProdCode())) {
            sql+=" and task.prod_code=$S{prodCode}";
        }
        super.update(sql,
                DataSourceProperty.IDB,task);
    }

    public void deleteById(String taskId) throws Exception {
        String sql = "DELETE FROM idb_disclosure_prod_task task WHERE task.status='"+TaskStatus.forGenerate.getItemKey()+"'" +"and task.id = '" +taskId+"'";
        super.update(sql,
                DataSourceProperty.IDB,taskId);
    }


    public void addT8DisclosureProdTaskZT(DisclosureProdTask task) throws Exception {
        super.update("INSERT INTO idb_disclosure_prod_task(id,prod_code," +
                        "crt_task_date,t8_disclosure_rule_id,t8_disclosure_prod_rule_id,notice_title,prod_base_date,sys_crt_date,task_month,status," +
                        "crt_date,crt_time,crt_user_id,crt_user_name,PROD_CLC_MTH,PROD_FORM,INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_SER_CD,PROD_INV_TYP," +
                        "remark,data_source,report_date,disclosure_type,disclosure_son_type) " +
                        "VALUES($AUTOIDS{id},$S{prodCode}," +
                        " $S{crtTaskDate},$S{t8DisclosureRuleId},$S{t8DisclosureProdRuleId},$S{noticeTitle},$S{prodBaseDate},$S{sysCrtDate},$S{taskMonth},$S{status}," +
                        " $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{prodClcMth},$S{prodForm},$S{invPrdDime},$S{invPrdLen},$S{prodObj},$S{prodSerCd},$S{prodInvTyp}," +
                        " $S{remark},$S{dataSource},$S{reportDate},$S{disclosureType},$S{disclosureSonType})",
                DataSourceProperty.IDB,task);
    }

    /**
     * 功能：更新信披任务
     * 作者：rennannan
     * 日期：20211023
     *
     * @param task
     * @throws Exception
     */
    public void updateT8DisclosureProdTask(DisclosureProdTask task) throws Exception {
        String sql = " update idb_disclosure_prod_task " +
                " set prod_code=$S{prodCode}," +
                " notice_title=$S{noticeTitle},t8_disclosure_rule_id = $S{t8DisclosureRuleId},t8_disclosure_prod_rule_id = $S{t8DisclosureProdRuleId}," +
                " prod_base_date=$S{prodBaseDate},sys_crt_date=$S{sysCrtDate},task_month=$S{taskMonth}," +
                " status=$S{status},upd_date=$S{updDate},upd_time=$S{updTime},upd_user_id=$S{updUserId}," +
                " upd_user_name=$S{updUserName},report_date=$S{reportDate}," +
                " disclosure_type=$S{disclosureType},disclosure_son_type=$S{disclosureSonType}" +
                " where id=$S{id}";
        super.update(sql,DataSourceProperty.IDB, task);
    }

    /**
     * 功能：根据条件查询信披任务
     *
     * @param prodTask
     * @return
     * @throws Exception
     */
    public List<DisclosureProdTask> findProdTasks(DisclosureProdTask prodTask) throws Exception {
        StringBuilder sql = new StringBuilder(
                " select id,prod_code,crt_task_date,t8_disclosure_prod_rule_id,notice_title," +
                        " prod_base_date,report_date,sys_crt_date,task_month,status" +
                        " data_source,disclosure_type,disclosure_son_type" +
                        " from idb_disclosure_prod_task" +
                        " where 1=1 ");
        if (StringUtils.isNotEmpty(prodTask.getSysCrtDate())) {
            sql.append(" and sys_crt_date=$S{sysCrtDate} ");
        }
        if (StringUtils.isNotEmpty(prodTask.getDisclosureType())) {
            sql.append(" and disclosure_type=$S{disclosureType}");
        }
        if (StringUtils.isNotEmpty(prodTask.getId())) {
            sql.append(" and id=$S{id}");
        }

        return super.findRows(DisclosureProdTask.class, sql.toString(), 0, prodTask);
    }

    /**
     * 功能：根据所属月份与信披类型查询任务
     * 作者：rennannan
     * 日期：20210603
     *
     * @param prodTask
     * @return
     * @throws Exception
     */
    public List<DisclosureProdTask> findExistMonthTasks(DisclosureProdTask prodTask) throws Exception {
        String sql = "  SELECT task.id,task.prod_code,task.crt_task_date,task.t8_disclosure_prod_rule_id,task.notice_title," +
                " task.prod_base_date,task.sys_crt_date,task.task_month,task.status," +
                " task.disclosure_type,task.disclosure_son_type" +
                " FROM idb_disclosure_prod_task task " +
                " where task.task_month=$S{taskMonth}";
        if (StringUtils.isNotEmpty(prodTask.getDisclosureType())) {
            sql += " and task.disclosure_type=$S{disclosureType} ";
        }
        if (StringUtils.isNotEmpty(prodTask.getDisclosureSonType())) {
            sql += " and task.disclosure_son_type=$S{disclosureSonType}";
        }
        return super.findRows(DisclosureProdTask.class, sql, 0, prodTask);
    }

    /**
     * @功能描述:查询系统自动生成日需要生成公告的任务，包含手动生成过公告的及未生成公告的任务
     * @params:[prodTask]
     * @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureProdTask>
     * @Athor:ouyifan
     * @date:2022/8/27
     */
    public List<DisclosureProdTask> findAutoGenTask(String nowDate, String disclosureType) throws Exception {
        String sql = " SELECT * FROM idb_disclosure_prod_task WHERE sys_crt_date = '"+nowDate+"'  and disclosure_type ='"+disclosureType+"'";
        return super.findRows(DisclosureProdTask.class, sql,
                DataSourceProperty.IDB, nowDate);
    }

    /**
     * 功能：修改信披任务状态
     * 作者：rennannan
     * 日期：20210608
     *
     * @param prodTask
     * @return
     * @throws Exception
     */
    public int updateTaskStatus(DisclosureProdTask prodTask) throws Exception {
        String sql = "update idb_disclosure_prod_task set status=$S{status} where id=$S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,prodTask).getEffect();
    }

    /*
     * 功能：修改公告状态
     */
    public int updateNoticeStatus(DisclosureProdTask prodTask) throws Exception {
        String sql = "update idb_disclosure_notice set disclosure_status=$S{createStatus}  where task_id=$S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,prodTask).getEffect();
    }

    public UpdateResult updDisclosureTaskTitle(ScheduleNotice params) throws Exception {
        return super.update("update idb_disclosure_prod_task set  notice_title = $S{noticeTitle}  WHERE 1=1 " +
                        "AND disclosure_type =$S{disclosureType} " +
                        "AND disclosure_son_type =$S{disclosureSonType} " +
                        "AND PROD_CLC_MTH =$S{prodClcMth} " +
                        "AND PROD_FORM =$S{prodForm} " +
                        "AND INV_PRD_DIME =$S{invPrdDime} " +
                        "AND INV_PRD_LEN =$S{invPrdLen} " +
                        "AND PROD_OBJ =$S{prodObj} " +
                        "AND PROD_SER_CD =$S{prodSerCd} " +
                        "AND prod_inv_typ =$S{prodInvTyp} " +
                        "AND prod_base_date = $S{prodBaseDate} "+
                        "AND prod_code = $S{prodCode} ",
                DataSourceProperty.IDB,params);
    }

    /**
     * 查询信披任务如果已经存在就更新，存在返回false，否则返回true
     *
     * @param prodTask
     * @return
     * @throws Exception
     */
    public Boolean checkTask(DisclosureProdTask prodTask) throws Exception {
        String sql = "SELECT id,status FROM idb_disclosure_prod_task WHERE  disclosure_type=$S{disclosureType} " +
                "and prod_base_date=$S{prodBaseDate} " ;
        if (StringUtils.isNotEmpty(prodTask.getDisclosureSonType())) {
            sql += " and disclosure_son_type=$S{disclosureSonType}";
        }
        if (StringUtils.isNotEmpty(prodTask.getDisclosureSonType())) {
            sql += " and disclosure_son_type=$S{disclosureSonType}";
        }
        if (StringUtils.isNotEmpty(prodTask.getProdCode())) {
            sql += " and prod_code=$S{prodCode}";
        }
        List<SqlRow> rows = super.findRows(sql,
                DataSourceProperty.IDB,prodTask);
        if (rows.size() >= 1) {
            String id = rows.get(0).get("id").toString();
            String status = rows.get(0).get("status").toString();
            prodTask.setId(id);
            //如果存在且状态是未生成公告则更新，否则不做操作；更新任务不更新任务来源
            if(TaskStatus.forGenerate.getItemKey().equals(status))
                updateT8DisclosureProdTask(prodTask);
            return false;
        }
        return true;
    }
    /**
     * 查询信披任务如果已经存在就更新，存在返回false，否则返回true
     *
     * @param prodTask
     * @return
     * @throws Exception
     */
    public Boolean checkTaskCP(DisclosureProdTask prodTask) throws Exception {
        String sql = "SELECT id,status FROM idb_disclosure_prod_task WHERE  disclosure_type=$S{disclosureType} " ;
        if (StringUtils.isNotEmpty(prodTask.getDisclosureSonType())) {
            sql += " and disclosure_son_type=$S{disclosureSonType}";
        }
        if (StringUtils.isNotEmpty(prodTask.getProdCode())) {
            sql += " and prod_code=$S{prodCode}";
        }
        List<SqlRow> rows = super.findRows(sql,
                DataSourceProperty.IDB,prodTask);
        if (rows.size() >= 1) {
            String id = rows.get(0).get("id").toString();
            String status = rows.get(0).get("status").toString();
            prodTask.setId(id);
            //如果存在且状态是未生成公告则更新，否则不做操作；更新任务不更新任务来源
            if(TaskStatus.forGenerate.getItemKey().equals(status))
                updateT8DisclosureProdTask(prodTask);
            return false;
        }
        return true;
    }
    /**
     * 查询信披任务如果已经存在就更新，存在返回false，否则返回true
     *
     * @param prodTask
     * @return
     * @throws Exception
     */
    public Boolean checkTaskZT(DisclosureProdTask prodTask) throws Exception {
        String sql = "SELECT id,status FROM idb_disclosure_prod_task WHERE  disclosure_type=$S{disclosureType} " +
                "and prod_base_date=$S{prodBaseDate} " ;
        if (StringUtils.isNotEmpty(prodTask.getDisclosureSonType())) {
            sql += " and disclosure_son_type=$S{disclosureSonType}";
        }
        if (StringUtils.isNotBlank(prodTask.getProdClcMth())) {
            sql = sql+"AND PROD_CLC_MTH =$S{prodClcMth} ";
        }
        if (StringUtils.isNotBlank(prodTask.getProdForm())) {
            sql = sql+"AND PROD_FORM =$S{prodForm} ";
        }
        if (StringUtils.isNotBlank(prodTask.getInvPrdDime())) {
            sql = sql+"AND INV_PRD_DIME =$S{invPrdDime} ";
        }
        if (StringUtils.isNotBlank(prodTask.getInvPrdLen())) {
            sql = sql+"AND INV_PRD_LEN =$S{invPrdLen} ";
        }
        if (StringUtils.isNotBlank(prodTask.getProdObj())) {
            sql = sql+"AND PROD_OBJ =$S{prodObj} ";
        }
        if (StringUtils.isNotBlank(prodTask.getProdSerCd())) {
            sql = sql+"AND PROD_SER_CD =$S{prodSerCd} ";
        }
        if (StringUtils.isNotBlank(prodTask.getProdInvTyp())) {
            sql = sql+"AND PROD_INV_TYP =$S{prodInvTyp} ";
        }
        List<SqlRow> rows = super.findRows(sql,
                DataSourceProperty.IDB,prodTask);
        if (rows.size() >= 1) {
            String id = rows.get(0).get("id").toString();
            String status = rows.get(0).get("status").toString();
            prodTask.setId(id);
            //如果存在且状态是未生成公告则更新，否则不做操作；更新任务不更新任务来源
            if (TaskStatus.forGenerate.getItemKey().equals(status))
                updateT8DisclosureProdTask(prodTask);
            return false;
        }
        return true;
    }

    /**
     * @功能描述:校验任务状态，以确认是否生成公告以返回提示
     * @params:[prodTask]
     * @return:java.lang.Boolean
     * @Athor:ouyifan
     * @date:2022/7/4
     */
    public String checkTaskStatus(DisclosureProdTask prodTask,boolean forUpdate) throws Exception {
        String sql = "select task.status,prod.prod_nm prod_name,task.disclosure_type from idb_disclosure_prod_task task " +
                "left join APP_PRD_BAS_INF prod  on prod.prod_cd = task.prod_code where task.id = $S{id} " ;
        List<SqlRow> rows = super.findRows(sql,
                DataSourceProperty.IDB,prodTask);
        if (rows.size() >0) {
            String status = rows.get(0).get("status").toString();
            if (forUpdate){//数据变更的
                if (status.equals(TaskStatus.forGenerate.getItemKey())){//找状态为未生成的
                    return rows.get(0).get("prod_name").toString()+"产品的"+rows.get(0).get("disclosure_type").toString();
                }
            }else {
                if (status.equals(TaskStatus.alreadyGenerate.getItemKey())){//生成公告按钮的，找已生成的
                    return rows.get(0).get("prod_name").toString()+"产品的"+rows.get(0).get("disclosure_type").toString();
                }
            }
            return null;
        }
        return null;
    }
    public Integer checkTaskProdRuleId(DisclosureProdTask prodTask) throws Exception {
        String sql = "select count(*) count  from  idb_disclosure_prod_rule where 1=1 " ;
        if (StringUtils.isNotBlank(prodTask.getDisclosureType())){
            sql +=" AND disclosure_type = $S{disclosureType}";
        }
        if (StringUtils.isNotBlank(prodTask.getDisclosureSonType())){
            sql +=" AND disclosure_son_type = $S{disclosureSonType}";
        }
        if (StringUtils.isNotBlank(prodTask.getProdCode())){
            sql +=" AND prod_code = $S{prodCode}";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,prodTask).getInteger("count");

    }
    public Integer checkTaskRuleId(DisclosureProdTask Task) throws Exception {
        String sql = "select count(*) count  from  idb_disclosure_rule where 1=1 AND status ='"+ XpStatus.start.getItemKey() +"' " +
                "AND disclosure_type = $S{disclosureType} " +
                "AND PROD_CLC_MTH =$S{prodClcMth} " +
                "AND PROD_FORM =$S{prodForm} " +
                "AND INV_PRD_DIME =$S{invPrdDime} " +
                "AND INV_PRD_LEN =$S{invPrdLen} " +
                "AND PROD_OBJ =$S{prodObj} " +
                "AND PROD_SER_CD =$S{prodSerCd} " +
                "AND PROD_INV_TYP =$S{prodInvTyp} " ;
        if (StringUtils.isNotBlank(Task.getDisclosureSonType())){
            sql +=" AND disclosure_son_type = $S{disclosureSonType} ";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,Task).getInteger("count");

    }
    public String checkNoticeStatus(DisclosureProdTask Task) throws Exception {
        String sql = "SELECT  " +
                "notice.disclosure_status, " +
                "notice.id " +
                "FROM  " +
                "(SELECT MAX(t.id+0) id  " +
                "FROM  " +
                "idb_disclosure_notice_version t  " +
                "GROUP BY t.t8_disclosure_notice_id ) t " +
                "LEFT JOIN idb_disclosure_notice_version notVer  " +
                "ON t.id = notVer.id " +
                "LEFT JOIN idb_disclosure_notice notice  " +
                "ON notice.id = notVer.t8_disclosure_notice_id  " +
                "WHERE 1=1  " +
                "AND notice.PROD_CLC_MTH=$S{prodClcMth} " +
                "AND notice.PROD_OBJ=$S{prodObj} " +
                "AND notice.PROD_FORM=$S{prodForm} " +
                "AND notice.PROD_SER_CD=$S{prodSerCd} " +
                "AND notice.prod_inv_typ=$S{prodInvTyp} " +
                "AND notice.INV_PRD_DIME=$S{invPrdDime} " +
                "AND notice.INV_PRD_LEN=$S{invPrdLen} " +
                "AND notice.prod_base_date=$S{prodBaseDate} " +
                "AND notice.prod_code=$S{prodCode} " +
                "AND notice.disclosure_son_type=$S{disclosureSonType} " +
                "AND notice.disclosure_type=$S{disclosureType} " ;
        return super.findRow(sql,
                DataSourceProperty.IDB,Task).getString("is_notice_pub");

    }

    /**
     * @功能描述:获取名称，以返回提示
     * @params:[prodTask]
     * @return:java.lang.Boolean
     * @Athor:ouyifan
     * @date:2022/7/4
     */
    public String checkProd(DisclosureProdTask prodTask) throws Exception {
        String sql = "select prod.prod_nm prod_name from idb_disclosure_prod_task task " +
                "left join APP_PRD_BAS_INF prod  on prod.prod_cd = task.prod_code where task.id = $S{id} " ;
        return super.findRow(sql,
                DataSourceProperty.IDB,prodTask).getString("prod_name");
    }

    /**
     * 根据信披类型和信批子类型查询公告模板表格配置
     *
     * @return
     * @params:[task]
     */
    public SqlResult<IdbNoticeGridConfigSource> queryTaskByTaskGroup(SqlParam<IdbNoticeGridConfigSource> noticeGrid) throws Exception {
        String sql = "SELECT *  FROM idb_notice_grid_config_source   WHERE disclosure_type = $S{disclosureType} AND disclosure_son_type = $S{disclosureSonType} ";
        return super.findRows(sql, DataSourceProperty.IDB, noticeGrid);
    }

    /**
     * @功能描述:插入公告模板表格配置数据表
     * @params:[task]
     */
    public void addIdbNoticeGridConfig(DisclosureProdTask Task, ScheduleNotice scheduleNotice) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("disclosureType", Task.getDisclosureType());
        map.put("disclosureSonType", Task.getDisclosureSonType());
        map.put("disclosureModVersionId", scheduleNotice.getDisclosureModVersionId());
        FetcherData param = new FetcherData(map, IdbNoticeGridConfigSource.class);
        SqlResult result = this.queryTaskByTaskGroup(param);
        List<IdbNoticeGridConfigSource> list = result.getRows();
        //先删除
        super.update("delete from idb_notice_grid_config where disclosure_mod_version_id = $S{disclosureModVersionId} ", DataSourceProperty.IDB, map);
        for (IdbNoticeGridConfigSource taskSet : list) {
            taskSet.setDisclosureModVersionId(scheduleNotice.getDisclosureModVersionId());
            super.update("INSERT INTO idb_notice_grid_config(id,disclosure_mod_version_id,replace_str,column_name,row_order,column_order,value_table_name,value_column_code,exeid,merge_row_num,merge_column_num,remark) " +
                            "VALUES($AUTOIDS{id},$S{disclosureModVersionId}," +
                            " $S{replaceStr},$S{columnName},$S{rowOrder},$S{columnOrder},$S{valueTableName},$S{valueColumnCode},$S{exeid},$S{mergeRowNum}," +
                            " $S{mergeColumnNum},$S{remark})",
                    DataSourceProperty.IDB, taskSet);
        }
    }

    /**
     * 根据报告生成表单数据
     * 定期报告/整体报告
     * 根据信批类型和子类型查询表单配置中所有的查询语句分别进行表单处理
     * 在下载公告前优先处理表单数据,便于公告进行预览查看
     * @param scheduleNotice
     * @throws Exception
     */
    public void genGridBaseData(ScheduleNotice scheduleNotice) throws Exception {
        List<SqlRow> sqlRes = super.findRows("select distinct data_execute_id as execute_id,ps.sqlStr as execute_sql from idb_notice_grid_config_source gc \n" +
                "  left join base_port_sql_info ps on ps.exeid = gc.data_execute_id " +
                " where gc.disclosure_type = '" + scheduleNotice.getDisclosureType() + "' and gc.disclosure_son_type = '" + scheduleNotice.getDisclosureSonType() + "' " +
                "   and gc.effect_date <= '" + scheduleNotice.getProdBaseDate() + "' and expiry_date > '" + scheduleNotice.getProdBaseDate() + "' " +
                "   and gc.data_execute_id is not null ",DataSourceProperty.IDB);//查询该公告需要查询的所有表单语句

        Map<String, Object> gridParams = BeanUtil.beanToMap(scheduleNotice);/** 将参数转换 */

        DaoUtil.doTrans(() -> {
            for (SqlRow sqlRow : sqlRes) {
                if (StringUtils.isNotBlank(sqlRow.getString("execute_sql"))) {
                    super.update(sqlRow.getString("execute_sql"), DataSourceProperty.IDB, gridParams);
                }
            }
        });
    }
}
