package com.kayak.dps.check.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.utils.Tools;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.check.model.PortLogInfoModel;
import com.kayak.dps.check.model.T8PortInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class T8portInfoDao extends ComnDao {

    /**
     * 查询接口信息管理信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8PortInfoModel> findPortInformation(SqlParam<T8PortInfoModel> params) throws Exception {
        String sql = "select p.id, p.port_code, p.port_name, p.port_exeid, p.port_address, p.port_state, p.file_type, p.port_type, p.port_dir, p.port_table " +
                     ",p.inputuser, p.crt_date, p.crt_time, p.upd_date, p.upd_time, p.separator, ifnull(p.skip_rows, 0) as skip_rows, ifnull(p.has_end_separator, '0') as has_end_separator ,p.pid,p.slice_data_source,p.split_key,p.split_where,p.select_sql,p.slice_select_sql,p.slice_flag,p.slice_count,p.temp_content,p.slice_file_path,p.slice_merge_path,p.insert_sql,p.source_from,p.slice_table_name,p.charset,p.xml_node_info,p.synch_type,skip_no_file " +
                     "  from base_port_manage p " +
                     " where 1 = 1 ";
        if (StringUtils.isNotBlank(params.getModel().getPortType())) {
            sql = sql + " and p.port_type = '" + params.getModel().getPortType() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getPortName())) {
            sql = sql + " and p.port_name like '%"+params.getModel().getPortName()+"%' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getPortState())) {
            sql = sql + " and p.port_state = '"+params.getModel().getPortState()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getPortTable())) {
            sql = sql + " and p.port_table like '%"+params.getModel().getPortTable()+"%' ";
        }
        if(Tools.strIsNotEmpty(params.getModel().getPid())){
            sql += "  and p.pid = $S{pid} " ;
        }
        sql += " order by  p.pid" ;
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 查询接口调用日志信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<PortLogInfoModel> findPortLogInfo(SqlParam<PortLogInfoModel> params) throws Exception {
        String sql = "select f.id, f.port_code, f.port_name, f.port_type, f.port_dir, f.deal_date, f.file_state, f.exec_message, f.total_num, u.username as user_name, " +
                     "       f.crt_date, f.crt_time, f.upd_date, f.upd_time " +
                     "  from base_port_file_log f " +
                     "  left join sys_user u on u.userid = f.deal_user_id" +
                     " where 1 = 1 ";
        String sql1 = "select f.id,f.port_name,f.port_type,'"+params.getModel().getDealDate()+"' deal_date,'00' file_state from base_port_manage f where not exists (" +
                                "select 1 from base_port_file_log t where f.port_code  = t.port_code and t.deal_date='"+params.getModel().getDealDate()+"')";
        if (StringUtils.isNotBlank(params.getModel().getPortType())) {
            sql = sql + " and f.port_type like '%" + params.getModel().getPortType() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getPortName())) {
            sql = sql + " and f.port_name like '%" + params.getModel().getPortName() + "%'";
        }
        /*if (StringUtils.isNotBlank(params.getModel().getFileState())&&StringUtils.isNotBlank(params.getModel().getDealDate())) {
            if(!params.getModel().getFileState().equals("00")){
                sql = sql + " and f.file_state like '%" + params.getModel().getFileState() + "%'";
            }else{
                return super.findRows(sql1, DataSourceProperty.PUB, params);
            }
        }*/
        if (StringUtils.isNotBlank(params.getModel().getFileState())) {
            sql = sql + " and f.file_state = '" + params.getModel().getFileState() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getStartCrtDate())) {
            sql = sql + " and f.crt_date >= '" + params.getModel().getStartCrtDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
            sql = sql + " and f.crt_date <= '" + params.getModel().getEndCrtDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getStartDealDate())) {
            sql = sql + " and f.deal_date >= '" + params.getModel().getStartDealDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getEndDealDate())) {
            sql = sql + " and f.deal_date <= '" + params.getModel().getEndDealDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getDealDate())) {
            sql = sql + " and f.deal_date = '" + params.getModel().getDealDate() + "'";
        }
        sql += " order by f.id desc ";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 更新接口管理信息语句执行
     * @param params
     * @throws Exception
     */
    public int updPortInformation(SqlParam<T8PortInfoModel> params) throws Exception {
        String sql = "update base_port_manage " +
                     "set port_code = $S{portCode}, port_name = $S{portName}, port_exeid = $S{portExeid}, " +
                     "    port_address = $S{portAddress}, file_type = $S{fileType},  `separator` = $S{separator}, " +
                     "    port_type = $S{portType}, port_dir = $S{portDir}, port_table = $S{portTable}, " +
                     "    skip_rows = $S{skipRows}, has_end_separator = $S{hasEndSeparator}, " +
                     "    inputuser = $S{inputuser}, upd_date = DATE_FORMAT(NOW(), '%Y%m%d'), upd_time = DATE_FORMAT(NOW(), '%H%i%s') ,pid = $S{pid},slice_data_source=$S{sliceDataSource},split_key=$S{splitKey},split_where=$S{splitWhere},select_sql=$S{selectSql},slice_select_sql=$S{sliceSelectSql},slice_flag=$S{sliceFlag},slice_count=$S{sliceCount},temp_content=$S{tempContent},slice_file_path=$S{sliceFilePath},slice_merge_path=$S{sliceMergePath},insert_sql=$S{insertSql},source_from=$S{sourceFrom},slice_table_name=$S{sliceTableName},charset=$S{charset},xml_node_info=$S{xmlNodeInfo},synch_type=$S{synchType},skip_no_file = $S{skipNoFile}" +
                     "where id = $S{id}";
        return super.update(sql, DataSourceProperty.PUB, params.getModel()).getEffect();
    }

    /**
     * 新增接口管理信息语句执行
     * 接口状态默认为启用
     * @param params
     * @throws Exception
     */
    public int addPortInformation (SqlParam<T8PortInfoModel> params) throws Exception {
        String sql = "insert into base_port_manage (port_code,port_name,port_exeid,port_address,port_state,file_type,`separator`,port_type,port_dir,port_table," +
                     "skip_rows, has_end_separator, inputuser, crt_date, crt_time, pid,slice_data_source,split_key,split_where,select_sql,slice_select_sql,slice_flag,slice_count,temp_content,slice_file_path,slice_merge_path,insert_sql,source_from,slice_table_name,charset,xml_node_info,synch_type,skip_no_file)  " +
                     "values ($S{portCode},$S{portName},$S{portExeid},$S{portAddress},'1',$S{fileType},$S{separator},$S{portType},$S{portDir},$S{portTable}," +
                     "$S{skipRows}, $S{hasEndSeparator}, $S{inputuser}, DATE_FORMAT(NOW(), '%Y%m%d'), DATE_FORMAT(NOW(), '%H%i%s'),$S{pid},$S{sliceDataSource},$S{splitKey},$S{splitWhere},$S{selectSql},$S{sliceSelectSql},$S{sliceFlag},$S{sliceCount},$S{tempContent},$S{sliceFilePath},$S{sliceMergePath},$S{insertSql},$S{sourceFrom},$S{sliceTableName},$S{charset},$S{xmlNodeInfo},$S{synchType},$S{skipNoFile}) ";
        return super.update(sql, DataSourceProperty.PUB, params.getModel()).getEffect();
    }

    /**
     * 删除接口管理信息语句执行
     * @param params
     * @throws Exception
     */
    public void delPortInformation(SqlParam<T8PortInfoModel> params) throws Exception {
        String sql = "delete from base_port_manage where id = $S{id}";
        super.update(sql, DataSourceProperty.PUB, params.getParams());
    }

    /**
     * 状态失效
     * @param params
     * @return
     * @throws Exception
     */
    public int stopStatus(SqlParam<T8PortInfoModel> params) throws Exception {
        return super.update("UPDATE base_port_manage SET port_state='0' WHERE id = $S{id}", DataSourceProperty.PUB, params.getParams()).getEffect();
    }

    /**
     * 状态生效
     * @param params
     * @return
     * @throws Exception
     */
    public int recoverStatus(SqlParam<T8PortInfoModel> params) throws Exception {
        return super.update("UPDATE base_port_manage SET port_state='1' WHERE id = $S{id}", DataSourceProperty.PUB, params.getParams()).getEffect();
    }

    /**
     * 根据接口类型返回接口绑定的获取路径及文件名
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8PortInfoModel> queryPortAddressByPortType(SqlParam<T8PortInfoModel> params) throws Exception {
        String sqlStr = "SELECT GROUP_CONCAT((CASE WHEN c.config_name = 'SFTP_IP' THEN c.config_code END)) AS sftp_ip, " +
                        "       GROUP_CONCAT((CASE WHEN c.config_name = 'SFTP_PORT' THEN c.config_code END)) AS sftp_port, " +
                        "       GROUP_CONCAT((CASE WHEN c.config_name = 'USERNAME' THEN c.config_code END)) AS username, " +
                        "       GROUP_CONCAT((CASE WHEN c.config_name = 'PASSWORD' THEN c.config_code END)) AS password, " +
                        "       GROUP_CONCAT((CASE WHEN c.config_name = 'LOCAL_PATH' THEN c.config_code END)) AS local_path, " +
                        "       GROUP_CONCAT((CASE WHEN c.config_name = 'REMOTE_PATH' THEN c.config_code END)) AS remote_path " +
                        "  FROM base_port_config_info c " +
                        " WHERE c.config_type = selOneBaseExMap('tr_port_type_transfer', $S{portType}, 'key') " +
                        "   AND c.status = '1' ";
        return super.findRows(sqlStr, DataSourceProperty.PUB, params);
    }

    /**
     * 根据接口类型返回接口绑定的获取路径及文件名
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> queryPortManageInfoByPortType(Map<String, Object> params) throws Exception {
        String sqlStr = "SELECT port_address,file_type FROM base_port_manage WHERE port_type = $S{portType} AND port_state = '1'";
        return super.findRows(sqlStr, DataSourceProperty.PUB, params);
    }

    public SqlResult<T8PortInfoModel> queryPortAndField(SqlParam<T8PortInfoModel> params) throws Exception {
        String sqlStr = "select m.port_code,m.port_name,m.pid,m.slice_flag,m.port_table,m.slice_data_source,m.split_key,m.split_where,m.select_sql,m.slice_select_sql,m.insert_sql,m.slice_table_name," +
                "m.slice_count,m.temp_content,m.slice_file_path,m.slice_merge_path,f.field_code,f.field_name,f.field_type,f.field_seq,m.synch_type,skip_no_file from base_port_manage m LEFT JOIN base_port_field_manage f " +
                "on m.port_code=f.PORT_CODE where FIND_IN_SET('"+params.getModel().getPid()+"',m.pid) order by f.field_seq asc";
        return super.findRows(sqlStr, DataSourceProperty.PUB, params);
    }

    public SqlResult<T8PortInfoModel> queryPortAndFieldByTbleName(SqlParam<T8PortInfoModel> params) throws Exception {
        String sqlStr = "select m.port_code,m.port_name,m.pid,m.slice_flag,m.port_table,m.slice_data_source,m.split_key,m.split_where,m.select_sql,m.slice_select_sql,m.insert_sql,m.slice_table_name," +
                "m.slice_count,m.temp_content,m.slice_file_path,m.slice_merge_path,f.field_code,f.field_name,f.field_type,f.field_seq,m.synch_type,skip_no_file from base_port_manage m LEFT JOIN base_port_field_manage f " +
                "on m.port_code=f.PORT_CODE where m.port_table='"+params.getModel().getPortTable()+"' order by f.field_seq asc";
        return super.findRows(sqlStr, DataSourceProperty.PUB, params);
    }
}
