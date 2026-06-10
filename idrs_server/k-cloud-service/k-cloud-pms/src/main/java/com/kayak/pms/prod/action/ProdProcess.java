package com.kayak.pms.prod.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.G6NodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProdProcess {

    private static Logger log = LoggerFactory.getLogger(ProdProcess.class);
    @Autowired
    private ComnDao comnDao;
    /**
     * 定时调度产品流程任务
     * */

    @RequestMapping(value = "/flow/process.json",produces = { "application/json;charset=UTF-8"})
    public String  prodProcess()  {
    	try {
            DaoUtil.doTrans(()->{
                List<SqlRow> results = comnDao.findRows("select t.id as t8_prod_task_nodes_info_id,p.id t8_prod_info_id,t.prod_code,t.send_msg_state,t.phase,t.node_id,t.t8_task_func_info_id,t.state,f.entry_checksql,f.entry_conditions,f.checksql,f.conditions,it.id as wf_flow_template_item_id,ti.model_id,i.ignore_state from t8_prod_task_nodes_info t left join t8_prod_info p on p.prod_code=t.prod_code left join t8_task_func_info f on f.id=t.t8_task_func_info_id left join t8_prod_task_info ti on ti.prod_code=t.prod_code left join wf_flow_template_item it on it.template_id=ti.model_id and it.phase=t.phase left join t8_prod_ignore_nodes_info i on i.prod_code=i.prod_code and i.node_id=t.node_id where t.state in(2) order by t.id");
                for (SqlRow prodConfig :results){
                    try {
                        //是否跳过当前检查节点（0：否 1：是）,或者检查语句为空
                        if(prodConfig.get("ignore_state") != null && prodConfig.getString("ignore_state").equals("1")
                                || prodConfig.getString("checksql").equals("")){
                            //直接执行通过方法修改状态以及开启下一个任务
                            this.pass(prodConfig,"1");
                        }else{
                            //是否有准入检查，如果有则需要判断准入条件是否通过
                            boolean isEntryPass=true;//是否通过（默认通过即可）
                            if(prodConfig.getString("entry_checksql") != null && !prodConfig.getString("entry_checksql").equals("")){
                                //为了防止每次检查都需要检查准入，加入是否已发送通知来判断是否需要再次检查准入条件（已发送通知证明已经检查过了，不用重复检查了）
                                if(prodConfig.getString("send_msg_state") != null && prodConfig.getString("send_msg_state").equals("0")){
                                    //检查准入条件是否通过
                                    isEntryPass=this.exCheck(prodConfig.getString("t8_prod_info_id"),prodConfig.getString("prod_code"),prodConfig.getString("entry_checksql"),prodConfig.getString("entry_conditions"));
                                }
                            }
                            if(isEntryPass){
                                //是否已经发送通知（已准入或不需要准入才会发送通知，并且根据状态判断只需要发送一次通知）
                                if(prodConfig.getString("send_msg_state") != null && prodConfig.getString("send_msg_state").equals("0")){
                                    //获取到通知人并发送通知
                                    System.out.println("发送通知消息。。。");
                                    //修改当前产品流程节点表为已发送通知
                                    comnDao.update("update t8_prod_task_nodes_info t set t.send_msg_state='1' where t.id="+prodConfig.getString("t8_prod_task_nodes_info_id"));
//                                comnDao.daoService.commit();
                                }
                                //检查是否通过节点检查条件
                                boolean isPass=this.exCheck(prodConfig.getString("t8_prod_info_id"),prodConfig.getString("prod_code"),prodConfig.getString("checksql"),prodConfig.getString("conditions"));
                                log.info("检查最终返回结果："+isPass);
                                //全部条件通过
                                if(isPass){
                                    //执行通过方法修改状态以及开启下一个任务
                                    this.pass(prodConfig,"3");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                    comnDao.daoService.end();
                    }
                }
                //清除缓存
                results.clear();
            });
		} catch (Exception e) {
			log.error("流程任务异常【{}】",e.toString());
		 return RequestSupport.updateReturnJson(false,  e.toString() , null).toString();
		}
    	 return RequestSupport.updateReturnJson(true,  "SUCCESS" , null).toString();
    }

    /**
     * 检查通过并开始执行下一个任务
     * @param prodConfig 配置对象
     * @param state 状态
     * */
    public void pass(SqlRow prodConfig, String state) throws Exception {
        synchronized (this){
            //重新去数据库获取新的json
            String jsonStr="";
            List<SqlRow> results = comnDao.findRows("select f.json from wf_flow_template_item f where f.id='"+prodConfig.getString("wf_flow_template_item_id")+"'");
            if(CollectionUtils.isEmpty(results))
            	return;
            jsonStr=results.get(0).getString("json");
            //获取整个流程图JSON对象
            JSONObject jsonObject=JSONObject.parseObject(jsonStr);
            //遍历节点数组获取当前节点并修改状态
            JSONArray nodesArr=jsonObject.getJSONObject("orignal").getJSONArray("nodes");
            for (int i = 0;i < nodesArr.size();i++){
                JSONObject nodeMap=(JSONObject) nodesArr.get(i);
                //匹配节点并改成完成状态
                if(nodeMap.getString("id").equals(prodConfig.getString("node_id"))){
                    nodeMap.put("state",state);//修改当前节点状态
                    break;
                }
            }
            //修改当前产品流程节点表为完成状态
            comnDao.update("update t8_prod_task_nodes_info t set t.state='"+state+"' where t.id="+prodConfig.getString("t8_prod_task_nodes_info_id"));
//            comnDao.daoService.commit();
            //得到修改状态后的JSON字符串，保存回去
            comnDao.update("update wf_flow_template_item t set t.json='"+jsonObject.toString()+"' where t.id='"+prodConfig.getString("wf_flow_template_item_id")+"'");
//            comnDao.daoService.commit();

            //得到下一个任务节点集合并遍历启动下一个任务节点
            List<String> nextNodeList= G6NodeUtil.findNodeIdList2(prodConfig.getString("wf_flow_template_item_id"),prodConfig.getString("node_id"));
            //如果下一个节点是结束节点则需要开启下一个阶段的第一个任务
            if(nextNodeList.size() > 0 && nextNodeList.get(0).equals("end")){
                //查询下一个阶段的数据
                SqlRow nextPhase = comnDao.findRow("select i.id,i.phase,i.json from wf_flow_template_item i where i.template_id='"+prodConfig.getString("model_id")+"' and i.phase=(select min(t.phase) from wf_flow_template_item t where t.template_id='"+prodConfig.getString("model_id")+"' and t.phase>'"+prodConfig.getString("phase")+"' and t.json is not null)",new HashMap<>());
                //找不到下一阶段的流程图则视为整个产品流程结束
                if(nextPhase == null || nextPhase.get("id") == null){
                    nextNodeList.clear();
                }else{
                    //切换成下一个阶段的参数
                    jsonObject=JSONObject.parseObject(nextPhase.getString("json"));
                    prodConfig.put("phase",nextPhase.getString("phase"));
                    prodConfig.put("wf_flow_template_item_id",nextPhase.getString("id"));
                    //查询阶段中的第一个节点
                    JSONArray nodeArr= jsonObject.getJSONObject("orignal").getJSONArray("nodes");
                    for (int i = 0; i < nodeArr.size(); i++) {
                        JSONObject nodeMap=(JSONObject) nodeArr.get(i);
                        if(nodeMap.getJSONObject("attrs").getString("type").equals("startevent-none")){
                            prodConfig.put("node_id",nodeMap.get("id"));
                            break;
                        }
                    }
                    //重新查找下一个任务节点ID
                    nextNodeList= G6NodeUtil.findNodeIdList2(prodConfig.getString("wf_flow_template_item_id"),prodConfig.getString("node_id"));
                }
            }
            //遍历下一个需要开启的任务节点
            for (String node_id :nextNodeList){
                //遍历节点数组匹配下一个任务节点
                JSONArray nodeArr= jsonObject.getJSONObject("orignal").getJSONArray("nodes");
                for (int i = 0; i < nodeArr.size(); i++) {
                    JSONObject nodeMap=(JSONObject) nodeArr.get(i);
                    //匹配下一个需要启动的任务节点
                    if(nodeMap.getString("id").equals(node_id)
                            && nodeMap.getJSONObject("attrs").getString("funcId") != null
                            && !nodeMap.getJSONObject("attrs").getString("funcId").equals("")){
                        try {
                            //修改节点状态为进行中
                            nodeMap.put("state","2");
                            //修改下一个产品流程节点表的状态为进行中
                            comnDao.update("update t8_prod_task_nodes_info t set t.state='2',t.send_msg_state='0' where t.node_id='"+node_id+"' and t.prod_code='"+prodConfig.getString("prod_code")+"' and t.phase='"+prodConfig.getString("phase")+"'");
//                            comnDao.daoService.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
//                            comnDao.daoService.end();
                        }
                    }
                }
            }
            //找到下一个节点时，需要重新保存修改后的状态
            if(nextNodeList.size() > 0){
                //得到修改状态后的JSON字符串，保存回去
                comnDao.update("update wf_flow_template_item t set t.json='"+jsonObject.toString()+"' where t.id='"+prodConfig.getString("wf_flow_template_item_id")+"'");
//                comnDao.daoService.commit();
                //修改当前产品流程阶段
                comnDao.update("update t8_prod_task_info t set t.curr_phase='"+prodConfig.getString("phase")+"' where t.prod_code='"+prodConfig.getString("prod_code")+"'");
//                comnDao.daoService.commit();
            }
            jsonObject.clear();//释放json对象
        }
    }

    /**
     * 检查条件是否通过
     * */
    public boolean exCheck(String t8_prod_info_id,String prod_code,String checksql,String conditions) throws Exception {
        //检查语句所需要的参数
        Map<String,Object> params=new HashMap<>();
        //产品代码
        params.put("t8_prod_info_id",t8_prod_info_id);
        params.put("prod_code",prod_code);
        //执行检查语句，得到检查结果
        SqlRow rows = comnDao.findRow(checksql, params);
        //解析判断条件
        JSONObject conditionJson=JSONObject.parseObject(conditions);
        //定义所有条件是否全部通过标识
        boolean isPass=false;
        //循环每一个条件进行判断，一般只会有一个条件
        for (String conditionKey : conditionJson.keySet()){
            //传入检查结果以及条件，调用判断方法，返回是否通过
            boolean isOk=this.assertionVal(rows.get(conditionKey).toString(),conditionJson.get(conditionKey).toString());
            if(isOk){
                //通过
                isPass=true;
            }else{
                //不通过
                isPass=false;
                //只要有一项不符合条件直接退出该节点检查
                break;
            }
        }
        params.clear();//清除对象内存
        conditionJson.clear();//释放JSON对象
        rows.clear();//使用对象缓存
        return isPass;
    }
    /**
     * 判断检查结果是否符合条件
     * @param val 检查语句返回结果
     * @param condition 检查条件
     * @return boolean true通过 false不通过
     * */
    public boolean assertionVal(String val,String condition){
        log.info("检查查询结果："+val+",检查条件："+condition);
        //是否检查通过
        boolean isOk=false;
        //返回值不能为空
        if(val != null && !val.equals("")){
            //切割开运算符与条件
            String[] conditionArr=condition.split("\\|");
            //运算符
            String operator=conditionArr[0];
            switch (operator){
                case ">":
                    if(Integer.parseInt(val) > Integer.parseInt(conditionArr[1])){
                        isOk=true;
                    }
                    break;
                case ">=":
                    if(Integer.parseInt(val) >= Integer.parseInt(conditionArr[1])){
                        isOk=true;
                    }
                    break;
                case "=":
                    if(val.equals(conditionArr[1])){
                        isOk=true;
                    }
                    break;
                case "<":
                    if(Integer.parseInt(val) < Integer.parseInt(conditionArr[1])){
                        isOk=true;
                    }
                    break;
                case "<=":
                    if(Integer.parseInt(val) <= Integer.parseInt(conditionArr[1])){
                        isOk=true;
                    }
                    break;
                default:
                    break;
            }
        }
        return isOk;
    }
}
