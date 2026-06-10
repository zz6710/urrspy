package com.kayak.pms.home.dao;

import com.kayak.base.dao.ComnDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.kayak.pms.home.dao
 * user:rennannan
 * date:2021/3/9 20:26
 * function:
 */
@Repository
public class HomeWorkFlowDao extends ComnDao {

    /**
     * 功能：查询当前用户待办工作流
     * 作者：rennannan
     * 日期：20210309
     *
     * @param params
     * @return
     * @throws Exception
     */
    public List<HashMap> findHomeWorkFlowInfos(Map<String, Object> params) throws Exception {
        String sql = "select node_id,nodes.prod_code,role_id,prod_name,node_name \n" +
                "    from (\n" +
                "\t\t\t\tselect node.node_id,node.prod_code,node.roleid,node.node_name\n" +
                "          from t8_prod_task_nodes_info node\n" +
                "\t       where state='2'\n" +
                "\t\t) nodes \n" +
                "\t  join (\n" +
                "\t\t\t\t\tselect prodUser.role_id,prod.prod_cd as prod_code,prodUser.userid_a,prod.prod_nm as prod_name\n" +
                "\t\t\t\t\t\tfrom t8_prod_user prodUser \n" +
                "\t\t\t left join dwd_prd_prd_bas_inf prod\n" +
                "\t\t\t\t\t\t\ton prodUser.prod_code = prod.prod_cd\n" +
                "\t\t\t\t\t where prodUser.userid_a = $S{userId}\n" +
                "\t\t     ) users\n" +
                "\t\t  on nodes.prod_code = users.prod_code\n" +
                "\t\t and FIND_IN_SET(users.role_id,nodes.roleid)\n" +
                "\t\t and users.userid_a=$S{userId} where 1=1 ";
        //add by zhangchangsi 移动审批添加节点名称查询条件
        if (StringUtils.isNotBlank((String)params.get("nodeName"))) {
            sql += " and nodes.node_name like '%$U{nodeName}%'";
        }
        return super.findRows(HashMap.class, sql, 0, params);
    }
}
