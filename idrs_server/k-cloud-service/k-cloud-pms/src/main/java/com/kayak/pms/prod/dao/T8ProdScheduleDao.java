package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.prod.model.T8ProdSchedule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/4/20 19:30
 */
@Repository
public class T8ProdScheduleDao extends ComnDao {
    public SqlResult<T8ProdSchedule> T8ProdSchedule(SqlParam<T8ProdSchedule> params) throws Exception {
        StringBuilder sql = new StringBuilder("select a.id, a.prod_code,a.prod_schedule_status, substring(a.apply_start_date,1,6) sell_month, a.prod_status, a.current_progress, a.prod_position," +
                "  a.apply_start_date, a.apply_end_date, a.establish_open_date, a.close_start_date," +
                "  a.close_end_date, a.perf_method_explain, a.current_quota, a.current_scale," +
                "  a.invest_manage_name, a.prod_manage_name, (SELECT GROUP_CONCAT( distributor_name SEPARATOR ',' ) type FROM t8_distributor_info " +
                " WHERE FIND_IN_SET(distributor_code,a.distributor_code)) distributor_name,a.distributor_code, a.issue_date," +
                "  a.process_instance_id, a.process_status, a.crt_date, a.crt_time, a.crt_user, a.upd_date," +
                "  a.upd_time, a.upd_user, b.prod_name from t8_prod_schedule a left join t8_prod_info b on a.prod_code = b.prod_code  where 1=1 ");
        if (StringUtils.isNotBlank(params.getModel().getProdName())) {
                    sql.append(" and b.prod_name like '%" + params.getModel().getProdName() + "%'");
                }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append("and a.prod_code = $S{prodCode} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getProdPosition())) {
            sql.append("and a.prod_position =$S{prodPosition} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getProdStatus())) {
            sql.append("and a.prod_status =$S{prodStatus} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getDataType())) {
            sql.append("and a.data_type = $S{dataType} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getQueryStartDate())) {
            sql.append("and a.establish_open_date >= $S{queryStartDate} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getQueryEndDate())) {
            sql.append("and a.establish_open_date <= $S{queryEndDate} ");
        }
        String sysWordDay ;
        if (StringUtils.isBlank(params.getModel().getQueryStartDate()) && StringUtils.isBlank(params.getModel().getQueryEndDate())) {
            /*获取是否启用系统工作日*/
            String systemParamsByParaid = SysUtil.getSystemParamsByParaid("10006");
            if ("0".equals(systemParamsByParaid)) {
                sysWordDay = DateUtil.getNowDate();
            } else {
                sysWordDay = SysUtil.getSystemParamsByParaid("10004");
            }
           sql.append(" and a.id in (select min(convert(t1.id, signed )) id from t8_prod_schedule t1 where t1.establish_open_date >= '"+ sysWordDay +"' group by t1.prod_code)");

        }
        if (params.getModel().getIsRecycleCode() != null && params.getModel().getIsRecycleCode() != "") {
            if("0".equals(params.getModel().getIsRecycleCode())){
                sql.append(" and (b.is_recycle_code != '1' or b.is_recycle_code is null ) ");
            }else{
                sql.append(" and b.is_recycle_code ='"+params.getModel().getIsRecycleCode()+"' ");
            }
        }else{
            sql.append(" and (b.is_recycle_code != '1' or b.is_recycle_code is null ) ");
        }
        sql.append(" order by CONVERT(a.id,SIGNED) desc");
        return  super.findRows(sql.toString(), params);
    }

    public SqlResult<T8ProdSchedule> findT8ProdSchedule(SqlParam<T8ProdSchedule> params) throws Exception {
        StringBuilder sql = new StringBuilder("select a.id, a.prod_code, substring(a.apply_start_date,1,6) sell_month, a.prod_status, a.current_progress, a.prod_position," +
                "  a.apply_start_date, a.apply_end_date, a.establish_open_date, a.close_start_date," +
                "  a.close_end_date, a.perf_method_explain, a.current_quota, a.current_scale," +
                "  a.invest_manage_name, a.prod_manage_name, a.distributor_code, a.issue_date," +
                "  a.process_instance_id, a.process_status, a.crt_date, a.crt_time, a.crt_user, a.upd_date," +
                "  a.upd_time, a.upd_user, b.prod_name from t8_prod_schedule a left join t8_prod_info b on a.prod_code = b.prod_code  where 1=1 ");

        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append("and a.prod_code = $S{prodCode} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getProdPosition())) {
            sql.append("and a.prod_position =$S{prodPosition} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getProdStatus())) {
            sql.append("and a.prod_status =$S{prodStatus} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getDataType())) {
            sql.append("and a.data_type = $S{dataType} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getQueryStartDate())) {
            sql.append("and a.establish_open_date >= $S{queryStartDate} ");
        }

        if (StringUtils.isNotBlank(params.getModel().getQueryEndDate())) {
            sql.append("and a.establish_open_date <= $S{queryEndDate} ");
        }
        sql.append(" order by CONVERT(a.id,SIGNED) desc");
        return  super.findRows(sql.toString(), params);
    }

    public int addProdSchedule(SqlParam<T8ProdSchedule> params) throws Exception {
        return insert(params.getModel());
    }

    public int insert(T8ProdSchedule t8ProdSchedule) throws Exception {
        return super.update("INSERT INTO t8_prod_schedule (id, prod_code, sell_month, prod_status, current_progress, prod_position,\n" +
                "                                         apply_start_date, apply_end_date, establish_open_date, close_start_date,\n" +
                "                                         close_end_date, perf_method_explain, current_quota, current_scale,\n" +
                "                                         invest_manage_name, prod_manage_name, distributor_code, issue_date,\n" +
                "                                         process_instance_id, process_status, crt_date, crt_time, crt_user, upd_date,\n" +
                "                                         upd_time, upd_user, data_type,prod_schedule_status)\n" +
                "VALUES ($AUTOIDS{prodScheduleId}, $S{prodCode}, $S{sellMonth}, $S{prodStatus}, (select max(prod_son_status) from t8_prod_info where prod_code = $S{prodCode}),\n" +
                "               $S{prodPosition}, $S{applyStartDate}, $S{applyEndDate}, $S{establishOpenDate}, $S{closeStartDate},\n" +
                "               $S{closeEndDate}, $S{perfMethodExplain}, $S{currentQuota}, $S{currentScale}, (select max(invest_manage_name) from t8_prod_declara where prod_code = $S{prodCode}),\n" +
                "               (select max(designer_name) from t8_prod_declara where prod_code = $S{prodCode}), $S{distributorCode}, $S{issueDate}, $S{processInstanceId},\n" +
                "               $S{processStatus}, $S{crtDate}, $S{crtTime}, $S{crtUser}, $S{updDate}, $S{updTime}, $S{updUser},$S{dataType},(select * from (select IFNULL(max(prod_schedule_status),'01') prod_schedule_status " +
                "                                                                       from t8_prod_schedule " +
                "                                                                       where prod_code = $S{prodCode} " +
                "                                                                         and prod_status = '01') temp))", t8ProdSchedule).getEffect();
    }

    public String insertReturnId(T8ProdSchedule t8ProdSchedule) throws Exception {
        return super.update("INSERT INTO t8_prod_schedule (id, prod_code, sell_month, prod_status, current_progress, prod_position,\n" +
                "                                         apply_start_date, apply_end_date, establish_open_date, close_start_date,\n" +
                "                                         close_end_date, perf_method_explain, current_quota, current_scale,\n" +
                "                                         invest_manage_name, prod_manage_name, distributor_code, issue_date,\n" +
                "                                         process_instance_id, process_status, crt_date, crt_time, crt_user, upd_date,\n" +
                "                                         upd_time, upd_user, data_type,prod_schedule_status)\n" +
                "VALUES ($AUTOIDS{prodScheduleId}, $S{prodCode}, $S{sellMonth}, $S{prodStatus}, (select max(prod_son_status) from t8_prod_info where prod_code = $S{prodCode}),\n" +
                "               $S{prodPosition}, $S{applyStartDate}, $S{applyEndDate}, $S{establishOpenDate}, $S{closeStartDate},\n" +
                "               $S{closeEndDate}, $S{perfMethodExplain}, $S{currentQuota}, $S{currentScale}, (select max(invest_manage_name) from t8_prod_declara where prod_code = $S{prodCode}),\n" +
                "               (select max(designer_name) from t8_prod_declara where prod_code = $S{prodCode}), $S{distributorCode}, $S{issueDate}, $S{processInstanceId},\n" +
                "               $S{processStatus}, $S{crtDate}, $S{crtTime}, $S{crtUser}, $S{updDate}, $S{updTime}, $S{updUser},$S{dataType},(select * from (select IFNULL(max(prod_schedule_status),'01') prod_schedule_status " +
                "                                                                       from t8_prod_schedule " +
                "                                                                       where prod_code = $S{prodCode} " +
                "                                                                         and prod_status = '01') temp))", t8ProdSchedule).getAutoId();
    }



    public int updateProdSchedule(SqlParam<T8ProdSchedule> params) throws Exception {
        return super.update("UPDATE t8_prod_schedule\n" +
                "SET prod_code = $S{prodCode},\n" +
                "        sell_month = $S{sellMonth}, prod_status = $S{prodStatus}, current_progress = $S{currentProgress}, prod_position = $S{prodPosition}, apply_start_date = $S{applyStartDate},\n" +
                "        apply_end_date = $S{applyEndDate}, establish_open_date = $S{establishOpenDate}, close_start_date = $S{closeStartDate}, close_end_date = $S{closeEndDate},\n" +
                "        perf_method_explain = $S{perfMethodExplain}, current_quota = $S{currentQuota}, current_scale = $S{currentScale}, invest_manage_name = $S{investManageName},\n" +
                "        prod_manage_name = $S{prodManageName}, distributor_code = $S{distributorCode}, issue_date = $S{issueDate}, process_instance_id = $S{processInstanceId},\n" +
                "        process_status = $S{processStatus}, crt_date = $S{crtDate}, crt_time = $S{crtTime}, crt_user = $S{crtUser}, upd_date = $S{updDate}, upd_time = $S{updTime},\n" +
                "        upd_user = $S{updUser}\n" +
                "WHERE id = $S{id}", params.getModel()).getEffect();
    }

    public int delProdSchedule(SqlParam<T8ProdSchedule> params) throws Exception {
        return super.update("delete from t8_prod_schedule where id = $S{id}", params.getModel()).getEffect();
    }

    public void batchDel(String prodCode) throws Exception {
        String sql = "delete from t8_prod_schedule where prod_code = '"+prodCode+"' and data_type = '2'";
        super.update(sql);
    }

    public List<SqlRow>  findByprodCode(T8ProdSchedule model) throws Exception {
        String sql = "select id, prod_code, sell_month, prod_status from t8_prod_schedule where prod_code = $S{prodCode}";
        return super.findRows(sql, model);
    }

    //修改新发状态下的产品排期信息-成立日、封闭投资周期起始日结束日、客户端发行日
    public void updateScheduleDate(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set  current_progress=$S{currentProgress},distributor_code = $S{distributorCode},establish_open_date = $S{establishOpenDate}, close_start_date = $S{closeStartDate}, close_end_date = $S{closeEndDate},issue_date=$S{issueDate} where id = $S{id}", t8ProdSchedule);
    }

    /**
     * 会后参数确认修改产品端发行日期
     */
    public void updateScheduleByMeetingConfirm(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set  issue_date = $S{issueDate} where prod_code = $S{prodCode} and data_type = '1'", t8ProdSchedule);
    }

    /**
     * 修改销售商代码
     */
    public void updatedistributorCode(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set  distributor_code = $S{distributorCode} where prod_code = $S{prodCode} ", t8ProdSchedule);
    }
    /**
     * 申报登记送审时更新产品排期信息
     */
    public void updateScheduleByDeclareConfirm(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set  prod_manage_name = $S{prodManageName},invest_manage_name=$S{investManageName} where prod_code = $S{prodCode}", t8ProdSchedule);
    }

    /**
     * 修改产品份额时更新产品排期当前规模信息
     * @param t8ProdSchedule
     */
    public void updateScheduleBySales(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set current_progress = $S{currentProgress},current_scale=(select sum(ifnull(subs_vol,0)-ifnull(redeem_vol,0)) val from ods_amng_prod_impinfo where prod_code = $S{prodCode} and change_date <= establish_open_date) where prod_code = $S{prodCode}", t8ProdSchedule);
    }

    /**
     * 修改产品排期本期额度
     * @param t8ProdSchedule
     */
    public void updateScheduleQuota(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set current_quota = $S{currentQuota} where prod_code = $S{prodCode} and establish_open_date = $S{establishOpenDate}", t8ProdSchedule);

    }

    /**
     * 修改产品排期状态
     * @param t8ProdSchedule
     */
    public void updateScheduleSonStatus(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set current_progress = $S{currentProgress} where prod_code = $S{prodCode}", t8ProdSchedule);
    }

    /**
     * 封闭式产品保存周期信息时，修改开发日、客户端发行日期
     * @param t8ProdSchedule
     */
    public void updateScheduleOnClose(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set  issue_date = $S{issueDate},establish_open_date = $S{establishOpenDate} where prod_code = $S{prodCode}", t8ProdSchedule);

    }

    /**
     * 封闭式产品保存周期信息时，修改开发日、客户端发行日期、销售商代码
     * @param t8ProdSchedule
     */
    public void updateScheduleOnClose1(T8ProdSchedule t8ProdSchedule) throws Exception {
        super.update("update t8_prod_schedule set  current_progress = $S{currentProgress},issue_date = $S{issueDate},establish_open_date = $S{establishOpenDate},distributor_code = $S{distributorCode} where prod_code = $S{prodCode}", t8ProdSchedule);

    }

    public int confirmSchedule(SqlParam<T8ProdSchedule> params) throws Exception {
        return super.update("update t8_prod_schedule set  prod_schedule_status = '02' where prod_code = $S{prodCode}", params.getModel()).getEffect();
    }

    public SqlResult<T8ProdSchedule> findProdScheduleIsConfirm(SqlParam<T8ProdSchedule> params) throws Exception {
        return super.findRows("select id from t8_prod_schedule where prod_code = $S{prodCode} and prod_schedule_status='02'", params);
    }

    //根据销售商代码查询数据
    public List<SqlRow> findByDistributorCode(String distributorCode) throws Exception {

        Map<String, Object> params = new HashMap<>(1);
        params.put("distributorCode", distributorCode);
        return super.findRows("select t.id,t.distributor_code from t8_prod_schedule t where FIND_IN_SET($S{distributorCode},t.distributor_code)", params);
    }

    public Integer updateDistributorCode(T8ProdSchedule t8ProdSchedule) throws Exception {

        String sql = " UPDATE t8_prod_schedule SET distributor_code = $S{distributorCode} WHERE id = $S{id}";
        return super.update(sql, t8ProdSchedule).getEffect();
    }
}
