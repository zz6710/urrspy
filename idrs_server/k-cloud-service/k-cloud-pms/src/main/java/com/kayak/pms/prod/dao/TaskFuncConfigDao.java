package com.kayak.pms.prod.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.TaskFuncConfig;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class TaskFuncConfigDao extends ComnDao {

    /**
     * 查询功能配置
     * */
    public SqlResult<TaskFuncConfig> queryTaskFuncConfig(SqlParam<TaskFuncConfig> params) throws Exception {
        params.setMakeSql(true);
        return super.findRows("select f.id,f.name,f.url,f.checksql,f.conditions,f.entry_checksql," +
                                        " f.entry_conditions,f.hide_button_ids " +
                                   " from t8_task_func_info f " +
                               " order by f.id desc", params);
    }
    /**
     * 查询功能配置
     * */
    public SqlResult<TaskFuncConfig> queryProcessTask(SqlParam<TaskFuncConfig> params) throws Exception {
        return super.findRows("select t.id,t.name from wf_flow_template t order by id desc", params);
    }

    /**
     * 查询功能配置
     * */
    public SqlResult<TaskFuncConfig> queryProcessTaskMod(SqlParam<TaskFuncConfig> params) throws Exception {
        return super.findRows("select t.id,t.name from wf_flow_template t where t.name in('pms_mod','pms_mod2') order by id desc", params);
    }

    /**
     * 查询功能配置
     * */
    public SqlRow queryProcessJson(String wf_flow_template_id,String phase) throws Exception {
        return super.findRow("select i.json from wf_flow_template_item i where i.template_id='"+wf_flow_template_id+"' and i.phase='"+phase+"'",new HashMap<>());
    }
    /**
     * 查询功能配置
     * */
    public SqlRow queryTaskPhase(String wf_flow_template_id) throws Exception {
        return super.findRow("select a.phase from wf_flow_template a where a.id='"+wf_flow_template_id+"'",new HashMap<>());
    }
    /**
     * 查询功能配置
     * */
    public String openTask(String prod_code,String phase) throws Exception {
        SqlRow taskMinID=super.findRow("select min(t.id) id from t8_prod_task_nodes_info t where t.prod_code='"+prod_code+"' and t.phase='"+phase+"'",new HashMap<>());
        super.update("update t8_prod_task_nodes_info n set n.state=2 where n.id = '"+taskMinID.getString("id")+"'");
        SqlRow taskNode=super.findRow("select node_id from t8_prod_task_nodes_info t where t.id='"+taskMinID.getString("id")+"'",new HashMap<>());
        return taskNode.getString("node_id");
    }

    /**
     * 保存功能配置
     * @return*/
    public void openTaskToJson(String wf_flow_template_id,String phase,String json) throws Exception {
        super.update("update wf_flow_template_item t set t.json='"+json+"' where t.template_id='"+wf_flow_template_id+"' and t.phase='"+phase+"'");
    }
    /**
     * 保存功能配置
     * @return*/
    public void deleteProcessPlan(String prod_code,String phase) throws Exception {
        super.update("delete from t8_prod_task_nodes_info where prod_code='"+prod_code+"' and phase='"+phase+"'");
        super.update("delete from t8_prod_ignore_nodes_info where prod_code='"+prod_code+"' and phase='"+phase+"'");
    }
    /**
     * 保存功能配置
     * @return*/
    public void saveTaskFuncConfig(SqlParam<TaskFuncConfig> params) throws Exception {
        super.update("insert into t8_task_func_info(id,name,url,entry_checksql,entry_conditions,checksql,conditions,hide_button_ids) values" +
                "($AUTOIDS{t8_task_func_info},$S{name},$S{url},$S{entryChecksql},$S{entryConditions},$S{checksql},$S{conditions},$S{hideButtonIds})",params.getModel());
    }
    /**
     * 保存功能配置
     * @return*/
    /**
     * 保存功能配置
     * @return*
     * 修改人：rennannan
     * 修改日期：20210310
     * 修改内容：添加字段节点名称node_name
     * */
    public void saveProd_task_nodes_info(String prod_code, String tos, String funcId, JSONArray roleid, String state, String phase,String nodeName) throws Exception {
         String roleStr="";
         if(roleid != null){
             for (int i = 0; i < roleid.size(); i++) {
                 roleStr+=roleid.getString(i)+",";
             }
             if(roleid.size() > 0){
                 roleStr=roleStr.substring(0,roleStr.length()-1);
             }
         }
        super.update("insert into t8_prod_task_nodes_info(id,prod_code,phase,node_id,t8_task_func_info_id,roleid,state,node_name) values($AUTOIDS{t8_prod_task_nodes_info},'"+prod_code+"','"+phase+"','"+tos+"','"+funcId+"','"+roleStr+"','"+state+"','"+nodeName+"')");
    }
    /**
     * 增加跳过节点
     * @return*/
    public void setIgnoreNode(String prod_code,String phase,String node_id) throws Exception {
        super.update("insert into t8_prod_ignore_nodes_info(id,prod_code,phase,node_id,ignore_state) values($AUTOIDS{t8_prod_ignore_nodes_info},'"+prod_code+"','"+phase+"','"+node_id+"','1')");
    }

    /**
     * 增加跳过节点
     * @return*/
    public void startFlow(String prod_code) throws Exception {
        UpdateResult result2=super.update("update t8_prod_info set flow_template='1' where prod_code='"+prod_code+"'");
    }

    /**
     * 删除功能配置
     * @return*/
    public void deleteTaskFuncConfig(SqlParam<TaskFuncConfig> params) throws Exception {
        super.update("delete from t8_task_func_info where id=$S{id}",params.getModel());
    }
    /**
     * 修改功能配置
     * @return*/
    public void updateTaskFuncConfig(SqlParam<TaskFuncConfig> params) throws Exception {
        super.update("update t8_task_func_info f " +
                            " set f.name=$S{name}," +
                            "     f.url=$S{url}," +
                            "     f.entry_checksql=$S{entryChecksql}," +
                            "     f.entry_conditions=$S{entryConditions}," +
                            "     f.checksql=$S{checksql}," +
                            "     f.conditions=$S{conditions}," +
                            "     f.hide_button_ids = $S{hideButtonIds} " +
                         "  where f.id=$S{id}",params.getModel());
    }

    public List<SqlRow> getWfFlowTemplateByProdCode(String prodCode) throws Exception {
        return super.findRows("select id wf_flow_template_id from wf_flow_template where name = $S{prodCode}",prodCode);
    }


}
