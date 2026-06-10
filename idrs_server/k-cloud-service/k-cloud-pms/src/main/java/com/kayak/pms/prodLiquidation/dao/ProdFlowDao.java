package com.kayak.pms.prodLiquidation.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.prodLiquidation.model.ProdFlow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shexianyu
 * @date 2022/9/28 15:03
 * @desc
 */
@Repository
public class ProdFlowDao extends ComnDao {
    /**
     * 查询产品清盘数据
     *
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<ProdFlow> findProdFlowLiquidation(SqlParam<ProdFlow> param) throws Exception {
        String userid = SysUtil.getLoginUserid();
        String sql = "SELECT t.prod_code,t.prod_name,t.prod_status,tpp.establish_date,tpp.end_date,t.prod_series,\n" +
                "(SELECT series_name FROM t8_prod_series WHERE series_code = t.prod_series) series_name," +
                "tpf.process_status,tpf.op_process_id,tpf.type\n" +
                "FROM t8_prod_info t \n" +
                "LEFT JOIN t8_prod_period tpp ON t.prod_code = tpp.prod_code\n" +
                "LEFT JOIN t8_prod_flow tpf ON tpf.prod_code = t.prod_code\n" +
                "WHERE 1 = 1 AND tpf.process_status IS NOT NULL\n";
        if (StringUtils.isNotBlank(param.getModel().getProdStatus())) {
            sql += "\tAND t.prod_status = '" + param.getModel().getProdStatus() + "'";
        } else {
            sql += "\tAND (t.prod_status = '5' OR t.prod_status = '6')";
        }
        if (StringUtils.isNotBlank(param.getModel().getType())) {
            sql += "\tAND FIND_IN_SET(tpf.type, '"+param.getModel().getType()+"')";
        }
        if (StringUtils.isNotBlank(param.getModel().getProdCode())) {
            sql += "\tAND t.prod_code = '" + param.getModel().getProdCode() + "'";
        }
        if (StringUtils.isNotBlank(param.getModel().getProcessStatus())) {
            sql += "\tAND FIND_IN_SET(tpf.process_status, '" + param.getModel().getProcessStatus() + "')";
        }
        //判断是否该产品的产品经理
        sql += "\tAND ('"+userid+"' IN (SELECT userid_a FROM t8_prod_user WHERE t8_prod_info_id = t.id AND role_id = '3' AND user_status = '1') \n" +
                "\tOR '"+userid+"' IN  (SELECT userid_b FROM t8_prod_user WHERE t8_prod_info_id = t.id AND role_id = '3' AND user_status = '1'))";
        return super.findRows(sql, param);
    }

    public SqlResult<ProdFlow> findProdFlowLiquidation1(SqlParam<ProdFlow> param) throws Exception {
        String userid = SysUtil.getLoginUserid();
        String sql = "select * from (SELECT t.prod_code,t.prod_name,t.prod_status,t.prod_son_status,tpp.establish_date,tpp.end_date,tpp.real_end_date,t.prod_series,\n" +
                "(SELECT series_name FROM t8_prod_series WHERE series_code = t.prod_series) series_name,tpl.status process_status," +
                "tpl.process_instance_id op_process_id,p.creator,date_format(p.process_deadline,'%Y-%m-%d') process_deadline," +
                "(SELECT GROUP_CONCAT(CONCAT_WS(',',userid_a,userid_b)) FROM t8_prod_user WHERE t8_prod_info_id = t.id AND role_id = '3' AND user_status = '1') prod_manager_id," +
                "(SELECT GROUP_CONCAT(username) FROM sys_user WHERE FIND_IN_SET(userid,(SELECT GROUP_CONCAT(CONCAT_WS(',',userid_a,userid_b)) FROM t8_prod_user WHERE t8_prod_info_id = t.id AND role_id = '3' AND user_status = '1'))) prod_manager_name," +
                "(('"+userid+"' IN (SELECT userid_a FROM t8_prod_user WHERE t8_prod_info_id = t.id AND role_id = '3' AND user_status = '1')) OR ('"+userid+"' IN (SELECT userid_b FROM t8_prod_user WHERE t8_prod_info_id = t.id AND role_id = '3' AND user_status = '1'))) prod_manager,\n" +
                " ifnull(tpl.param_status,'6') param_status \n"+
                "FROM t8_prod_info t \n" +
                "LEFT JOIN t8_prod_period tpp ON t.prod_code = tpp.prod_code\n" +
                "left join t8_prod_liquidation tpl on tpl.prod_code = t.prod_code " +
                "left join wf_business_process p on p.id = tpl.process_instance_id " +
                "WHERE 1 = 1 \n";
        if (StringUtils.isNotBlank(param.getModel().getProdStatus())) {
            sql += "\tAND t.prod_status = '" + param.getModel().getProdStatus() + "'";
        } else {
            sql += "\tAND (t.prod_status = '5' OR t.prod_status = '6')";
        }
        if (StringUtils.isNotBlank(param.getModel().getProdCode())) {
            sql += "\tAND t.prod_code = '" + param.getModel().getProdCode() + "'";
        }
        if (StringUtils.isNotBlank(param.getModel().getProdManagerId())){
            sql += " AND FIND_IN_SET('"+param.getModel().getProdManagerId()+"',(SELECT GROUP_CONCAT(CONCAT_WS(',',userid_a,userid_b)) FROM t8_prod_user WHERE t8_prod_info_id = t.id AND role_id = '3' AND user_status = '1')) ";
        }

        sql = sql +") temp";
        if (StringUtils.isNotBlank(param.getModel().getProcessStatus())) {
            sql += " where temp.process_status = '" + param.getModel().getProcessStatus() + "'";
        }
        return super.findRows(sql, param);
    }

    /**
     * 针对流程中没有产品代码的数据
     * type = 1:产品清盘
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<ProdFlow> findProdCodeFlow(SqlParam<ProdFlow> param) throws Exception {
        String sql = "SELECT t.prod_code,$S{type} AS type FROM t8_prod_info t \n" +
                "WHERE (t.prod_status = '5' OR t.prod_status = '6') " +
                "AND NOT EXISTS(SELECT * FROM t8_prod_flow tpf WHERE tpf.prod_code = t.prod_code AND tpf.type = $S{type})";
        return super.findRows(sql,param);
    }

    public UpdateResult addProdFlowLiquidation(ProdFlow param) throws Exception {
        String sql = "INSERT INTO t8_prod_flow(`prod_code`, `process_status`, `op_process_id`, `type`, `crt_user`, `crt_date`, `upd_user`, `upd_date`) \n" +
                "VALUES ($S{prodCode}, '1', $S{opProcessId}, $S{type}, $S{crtUser}, $S{crtDate}, $S{updUser}, $S{updDate})";
        return super.update(sql,param);
    }

    public UpdateResult addProdFlowLiquidation1(ProdFlow param) throws Exception {
        String sql = "INSERT INTO t8_prod_flow(`prod_code`, `process_status`, `op_process_id`, `type`, `crt_user`, `crt_date`, `upd_user`, `upd_date`) \n" +
                "VALUES ($S{prodCode}, $S{processStatus}, $S{opProcessId}, $S{type}, $S{crtUser}, $S{crtDate}, $S{updUser}, $S{updDate})";
        return super.update(sql,param);
    }

    //根据操作流程实例id获取提交清盘的数据
    public ProdFlow findProdFlowByProcessId(String processInstancedId) throws Exception {
        String sql = "SELECT * FROM t8_prod_flow WHERE op_process_id = '"+processInstancedId+"'";
        return super.findRow(ProdFlow.class,sql,0,processInstancedId);
    }

    //终止：更新流程id为空 流程状态为已终止 5(且推送到首页待办)
    public UpdateResult updateProdFlowById(String id) throws Exception {
        String sql = "UPDATE t8_prod_flow SET op_process_id = '', process_status = '5', type = '3' WHERE op_process_id = '"+id+"'";
        return super.update(sql,id);
    }

    /**
     * 更新产品操作流数据
     * @param param
     * @return
     * @throws Exception
     */
    public UpdateResult updateProdFlowInfo(ProdFlow param) throws Exception {
        String sql = "UPDATE t8_prod_flow \n" +
                "SET process_status = $S{processStatus}, op_process_id = $S{opProcessId}, \n" +
                "upd_user = $S{updUser}, upd_date = $S{updDate} \n" +
                "WHERE prod_code = $S{prodCode} AND type = $S{type}";
        return super.update(sql,param);
    }

    public List<SqlRow> getSubmitParamsByProcessInstanceId(String  processInstanceId) throws Exception{
        String sql = "SELECT process_instance_id,submit_params FROM wf_submit_params  WHERE process_instance_id='"+processInstanceId+"' ORDER BY create_date DESC, create_time DESC";
        return super.findRows(sql);
    }

    //转交更新流程信息
    public UpdateResult updateProdFlowStatusAndProcess(String processStatus,String processInstanceId,String prodCode) throws Exception {
        String sql = "UPDATE t8_prod_flow \n" +
                "SET process_status = '"+processStatus+"'," +
                "op_process_id = '"+processInstanceId+"' " +
                " WHERE prod_code = '"+prodCode+"' AND type != '2'";
        return super.update(sql,processStatus);
    }


    public UpdateResult updateProdFlowStatus(ProdFlow param) throws Exception {
        String sql = "UPDATE t8_prod_flow \n" +
                "SET process_status = $S{processStatus},\n" +
                "upd_user = $S{updUser}, upd_date = $S{updDate} \n" +
                "WHERE prod_code = $S{prodCode} AND FIND_IN_SET(type,$S{type})";
        return super.update(sql,param);
    }

    public UpdateResult deleteOpfLog(String processInstanceId) throws Exception {
        String sql = "DELETE FROM opf_log WHERE process_instance_id = '"+ processInstanceId +"'";
        return super.update(sql,processInstanceId);
    }
    public UpdateResult deleteOpfProcessInstance(String processInstanceId) throws Exception {
        String sql = "DELETE FROM opf_process_instance WHERE process_instance_id = '"+ processInstanceId +"'";
        return super.update(sql,processInstanceId);
    }
    public UpdateResult deleteOpfHisTask(String processInstanceId) throws Exception {
        String sql = "DELETE FROM opf_his_task WHERE process_instance_id = '"+ processInstanceId +"'";
        return super.update(sql,processInstanceId);
    }
    public UpdateResult deleteOpfFormData(String processInstanceId) throws Exception {
        String sql = "DELETE FROM opf_form_data WHERE process_instance_id = '"+ processInstanceId +"'";
        return super.update(sql,processInstanceId);
    }
    public UpdateResult deleteOpfSubmitParams(String processInstanceId) throws Exception {
            String sql = "DELETE FROM opf_submit_params WHERE process_instance_id = '"+ processInstanceId +"'";
        return super.update(sql,processInstanceId);
    }
    public UpdateResult deleteOpfTask(String processInstanceId) throws Exception {
        String sql = "DELETE FROM opf_task WHERE process_instance_id = '"+ processInstanceId +"'";
        return super.update(sql,processInstanceId);
    }

    //查询产品为存续期的产品(已到期)
    public List<SqlRow> findProdCodeAndEndDate() throws Exception {
        return super.findRows("SELECT t.prod_code,tpp.end_date,t.prod_status FROM t8_prod_info t \n" +
                "LEFT JOIN t8_prod_period tpp ON t.prod_code = tpp.prod_code \n" +
                "WHERE t.prod_status = '5' OR t.prod_status = '6'");
    }

    public SqlRow findProdFlowByCode(String prodCode) throws Exception{
       return super.findRow("SELECT COUNT(*) count FROM t8_prod_flow WHERE prod_code = '"+prodCode+"' LIMIT 1",prodCode);
    }

    public ProdFlow findProdFlowInfoByCode(String prodCode) throws Exception{
        return super.findRow(ProdFlow.class,"SELECT process_status,type FROM t8_prod_flow WHERE prod_code = '"+prodCode+"'",0,prodCode);
    }

    public void updateProdFlowType(ProdFlow prodFlowInfo) throws Exception {
        super.update("UPDATE t8_prod_flow SET type = $S{type} WHERE prod_code = $S{prodCode} AND type ='1'",prodFlowInfo);
    }
}
