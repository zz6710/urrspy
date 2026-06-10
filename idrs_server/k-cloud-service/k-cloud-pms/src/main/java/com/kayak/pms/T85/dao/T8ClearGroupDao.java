package com.kayak.pms.T85.dao;

import com.kayak.core.sql.Sql;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.T85.model.T8ClearGroupInfo;

/**
 * 文件名: TaClearGroupOperDao.java
 * 描述:   TA清算组信息操作
 * 创建人: zengzt
 * 创建时间:2020年4月29日下午2:31:57
 */
@Repository
public class T8ClearGroupDao extends ComnDao{

    /**
     *
     * 方法描述:查询清算任务组信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8ClearGroupInfo> queryT8ClearGroupInfos(SqlParam<T8ClearGroupInfo> params) throws Exception {

        String sql = "SELECT task_group,task_group_name,exec_task_type,pre_task_group,display_order FROM t8_clear_group_info ORDER BY display_order";
        return super.findRows(sql, params);
    }


    /**
     *
     * 方法描述:查询清算任务组信息 （不包含某个组）
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8ClearGroupInfo> findT8ClearGroupInfosExceptGroup(SqlParam<T8ClearGroupInfo> params) throws Exception {

        String sql = "SELECT task_group,task_group_name,exec_task_type,pre_task_group,display_order FROM t8_clear_group_info ";

        if(params.getModel().getTaskGroup()!=null && !"".equals(params.getModel().getTaskGroup())){
            sql = sql + " WHERE task_group <> $S{taskGroup}";
        }

        sql = sql + " ORDER BY display_order";

        return super.findRows(sql, params);
    }


    /**
     *
     * 方法描述:插入清算组信息
     * @param params
     * @return
     * @throws Exception
     */
    public int insertT8ClearGroupInfo(SqlParam<T8ClearGroupInfo> params) throws Exception {

        String sqlAll = "INSERT INTO t8_clear_group_info(task_group,task_group_name,exec_task_type,pre_task_group,crt_date,upt_date,display_order)"
                + "VALUES(lpad(seq_task_group.nextVal,12,'0'),$S{taskGroupName},$S{execTaskType},$S{preTaskGroup},current_timestamp,current_timestamp,$S{displayOrder})";
        String sqlDb2 = "INSERT INTO t8_clear_group_info(task_group,task_group_name,exec_task_type,pre_task_group,crt_date,upt_date,display_order)"
                + "VALUES(lpad(seq_task_group.nextVal,12,'0'),$S{taskGroupName},$S{execTaskType},$S{preTaskGroup},current timestamp,current timestamp,$S{displayOrder})";
        Sql sql = Sql.build().mysqlSql(sqlAll).db2Sql(sqlDb2);
        return super.update(sql, params.getModel()).getEffect();

    }

    /**
     *
     * 方法描述:修改清算组信息
     * @param params
     * @return
     * @throws Exception
     */
    public int updateT8ClearGroupInfo(SqlParam<T8ClearGroupInfo> params) throws Exception {

        String sqlAll = "UPDATE t8_clear_group_info"
                + " SET task_group_name = $S{taskGroupName},"
                + "		pre_task_group = $S{preTaskGroup}, "
                + "		upt_date = current_timestamp, "
                + "		display_order = $S{displayOrder} "
                + " WHERE task_group = $S{taskGroup} ";
        String sqlDb2 = "UPDATE t8_clear_group_info"
                + " SET task_group_name = $S{taskGroupName},"
                + "		pre_task_group = $S{preTaskGroup}, "
                + "		upt_date = current timestamp, "
                + "		display_order = $S{displayOrder} "
                + " WHERE task_group = $S{taskGroup} ";
        Sql sql = Sql.build().mysqlSql(sqlAll).db2Sql(sqlDb2);

        return super.update(sql, params.getModel()).getEffect();

    }

    /**
     *
     * 方法描述:删除清算组信息
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteT8ClearGroupInfo(SqlParam<T8ClearGroupInfo> params) throws Exception {

        //删除清算组信息，同时还需要删除该组下的成员信息、任务配置信息等
        doTrans(() ->{
            super.update("DELETE FROM t8_clear_group_info WHERE task_group = $S{taskGroup} ", params.getModel()).getEffect();
            super.update("DELETE FROM ta_clear_group_member WHERE task_group = $S{taskGroup} ", params.getModel()).getEffect();
            super.update("DELETE FROM ta_clear_task_set WHERE task_group = $S{taskGroup} ", params.getModel()).getEffect();
        });

    }


    /**
     *
     * 方法描述:查询是否有以本组未前置的清算组
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8ClearGroupInfo> queryIsPreTaClearGroup(SqlParam<T8ClearGroupInfo> params) throws Exception {

        String sql = "SELECT task_group,task_group_name,exec_task_type,pre_task_group FROM t8_clear_group_info where instr(pre_task_group,$S{taskGroup}) > 0";

        return super.findRows(sql, params);
    }

}
