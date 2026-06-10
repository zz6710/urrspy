package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.disclousreEnum.DisclosureTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.OperationTypeEnum;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeProcess;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersion;
import com.kayak.pms.disclosureControl.model.DisclosureOperation;
import com.kayak.utils.DateHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DisclosureNoticeProcessDao extends ComnDao {
    @Autowired
    private DisclosureOperationDao disclosureOperationDao;



    public SqlResult<DisclosureNoticeProcess> findDisNoticeProcesss(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        return super.findRows("SELECT id,t8_disclosure_notice_id,role_id,user_id,to_user_id,open_status,input_status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name FROM idb_disclosure_notice_process", params);
    }

    public List<DisclosureNoticeProcess> findProcessList(DisclosureNoticeProcess process) throws Exception {
        List<DisclosureNoticeProcess> dbProcessList = super.findRows(DisclosureNoticeProcess.class, " select id,t8_disclosure_notice_id,role_id,user_id,to_user_id from idb_disclosure_notice_process where t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and input_status='0'", 0, process);
        return dbProcessList;
    }

    /*
     * 查询 投资经理 id 以及 姓名;
     * */
    public SqlRow findUserInfo(String t8DisclosureNoticeId) throws Exception {
        return super.findRow(" select GROUP_CONCAT(tdnp.user_id separator ',') as userid,GROUP_CONCAT(su.username separator ',') as username from idb_disclosure_notice_process tdnp LEFT JOIN sys_user su on tdnp.user_id = su.userid where t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and role_id = '14'", t8DisclosureNoticeId);
    }

    /**
     * 查找估值核算岗的公告进程信息
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> findProcesss(DisclosureNoticeProcess params) throws Exception {
        return super.findRows("SELECT id,role_id,user_id,to_user_id FROM idb_disclosure_notice_process where t8_disclosure_notice_id = $S{t8DisclosureNoticeId} and role_id='9'", params);
    }

    public List<SqlRow> findProcesssRoleInfo(DisclosureNoticeProcess params) throws Exception {
        return super.findRows("SELECT*FROM idb_disclosure_notice_process WHERE t8_disclosure_notice_id=$S{t8DisclosureNoticeId} AND  (user_id = $S{userId} or to_user_id=$S{userId} )", params);
    }

    public boolean findProcesssStatus(DisclosureNoticeProcess params) throws Exception {
        //判断当前公告是否是 公告审核 拒绝状态
        List<SqlRow> list = super.findRows("SELECT count(input_status = '0' or null) count0, count(input_status ='1' or null) count1,count(role_id ='9' or null) count2, " +
                " count(role_id ='14' or null) count3,count(id) count4 FROM idb_disclosure_notice_process where t8_disclosure_notice_id = $S{t8DisclosureNoticeId}", params);
        //int count0 = list.get(0).getInteger("count0"); //未录入的数量
        int count1 = list.get(0).getInteger("count1"); //已录入的数量
        int count2 = list.get(0).getInteger("count2"); //估值核算岗补录分发数量
        int count3 = list.get(0).getInteger("count3"); //投资经理补录分发数量
        int count4 = list.get(0).getInteger("count4"); //补录分发总数量

        String date = DateHelper.getCurrentDate();
        String time = DateHelper.getCurrentTime();
        String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));
        //所有补录人员都已经补录完成,更新公告状态
        //如有未补录角色,更新单一状态
        boolean flag = false;
        //投资经理和估值核算岗都补录完成走这里
        if(count1 == count4 && count2 != 0){
            List<SqlRow> list2 = super.findRows("SELECT tdpr.trustee_examine,tdr.notice_roleid,tpu.userid_a,tdn.id FROM idb_disclosure_notice tdn " +
                    "LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id " +
                    "LEFT JOIN idb_disclosure_rule tdr ON tdpr.t8_disclosure_rule_id=tdr.id " +
                    "LEFT JOIN t8_prod_user tpu ON tpu.role_id=tdr.notice_roleid " +
                    "WHERE tdn.id=$S{t8DisclosureNoticeId} AND tpu.t8_prod_info_id=$S{t8ProdInfoId}",params);
            //查询产品信披规则看是否需要发送托管行
            SqlRow row = super.findRow("select b.trustee_examine from idb_disclosure_notice a join idb_disclosure_prod_rule b on a.t8_disclosure_rule_id = b.id where a.id = $S{t8DisclosureNoticeId}", params);
            //判断是否是否托管行审核
            String trusteeExamine = "";
            if (row != null) {
                trusteeExamine = row.getString("trustee_examine");
            }

            if ("0".equals(trusteeExamine)) {
                //不需要托管行审核,状态到发起审批阶段
                super.update("update  idb_disclosure_notice set stage='3',current_stage_status='11',upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+userId+"', upd_user_name = '"+username+"'  where id=$S{t8DisclosureNoticeId}",params);
            } else {
                //是托管行审核,状态到发送托管行阶段
                super.update("update  idb_disclosure_notice set stage='1',current_stage_status='9',upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+userId+"', upd_user_name = '"+username+"'  where id=$S{t8DisclosureNoticeId}",params);

            }

            DisclosureNoticeVersion disclosureNoticeVersion = new DisclosureNoticeVersion();
            disclosureNoticeVersion.setT8DisclosureNoticeId(params.getT8DisclosureNoticeId());
            Map<String,String> map = findMaxVersions(disclosureNoticeVersion);
            List<SqlRow> versionsInfo = findVersionsInfo(disclosureNoticeVersion);
            disclosureNoticeVersion.setNoticeVersion(map.get("version"));
            disclosureNoticeVersion.setDocType("0");
            disclosureNoticeVersion.setFileName(versionsInfo.get(0).getString("file_name"));
            disclosureNoticeVersion.setFilePath(versionsInfo.get(0).getString("upload_path"));
            disclosureNoticeVersion.setProdCode(versionsInfo.get(0).getString("prod_code"));
            disclosureNoticeVersion.setCrtDate(date);
            disclosureNoticeVersion.setCrtTime(time);
            disclosureNoticeVersion.setCrtUserId(userId);
            disclosureNoticeVersion.setCrtUserName(username);
            super.update("INSERT INTO idb_disclosure_notice_version(id,prod_code,t8_disclosure_notice_id,version,doc_type," +
                    "crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,file_path,file_name,remark) " +
                    "VALUES($AUTOIDS{id},$S{prodCode},$S{t8DisclosureNoticeId},$S{version},$S{docType},$S{crtDate},$S{crtTime}," +
                    "$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{filePath}," +
                    "$S{fileName},$S{remark})", disclosureNoticeVersion);

            super.update("INSERT INTO idb_disclosure_notice_version_value (id,t8_disclosure_notice_id,t8_disclosure_version_id,t8_disclosure_notice_version_id,prod_code,data_date,column_key,column_value)\n" +
                    "SELECT (@i :=@i+1) AS id,temp.t8_disclosure_notice_id,temp.t8_disclosure_version_id,temp.version t8_disclosure_notice_version_id,temp.prod_code,temp.data_date,temp.column_key,temp.column_value FROM (\n" +
                    "SELECT $S{t8DisclosureNoticeId} AS t8_disclosure_notice_id,t8_disclosure_version_id,$S{version} version,prod_code,data_date,column_key,column_value FROM idb_disclosure_notice_value WHERE t8_disclosure_notice_id=$S{t8DisclosureNoticeId}) temp,(\n" +
                    "SELECT @i :=(select ifnull(max(id+0),0) from idb_disclosure_notice_version_value)) AS t", disclosureNoticeVersion);

            //生成文档

            flag = true;

            if(list2!=null && list2.size()>0){
                //新版逻辑,补录完成后添加信披经理代办,提示发起托管行审批
                for (SqlRow sqlRow : list2) {
                    DisclosureOperation operation = new DisclosureOperation();
                    operation.setDealId(sqlRow.getString("id"));
                    operation.setRoleid(sqlRow.getString("notice_roleid"));
                    operation.setUserid(sqlRow.getString("userid_a"));
                    operation.setProdCode(params.getProdCode());
                    operation.setOperationType(OperationTypeEnum.EIGHT.getVal());
                    operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
                    operation.setStatus("0");
                    operation.setCrtDate(date);
                    operation.setCrtTime(time);
                    operation.setCrtUserId(userId);
                    operation.setDealTable("idb_disclosure_notice");
                    disclosureOperationDao.insertDisOperation(operation);
                }
            }
        } else if (count3 == count4) {//投资经理已经补录，估值核算岗没有进行补录
            //将公告状态更新为估值未确认，投资已确认
            super.update("update  idb_disclosure_notice set stage='1',current_stage_status='7' ,upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+userId+"', upd_user_name = '"+username+"'  where id = $S{t8DisclosureNoticeId}",params);
        } else{
            List<DisclosureNoticeProcess> list1 = super.findRows(DisclosureNoticeProcess.class,"SELECT (CASE WHEN group_concat(t.role_id) = '9' THEN '7' WHEN group_concat(t.role_id) = '14' THEN '6' ELSE '8' END) open_status from (SELECT role_id,t8_disclosure_notice_id  FROM idb_disclosure_notice_process where t8_disclosure_notice_id = '"+params.getT8DisclosureNoticeId()+"' and input_status !='1'  GROUP BY role_id ) t GROUP BY t.t8_disclosure_notice_id",0,null);
            log.info("list长度，{}",list1.size());
            super.update("update  idb_disclosure_notice set stage='1',current_stage_status='"+list1.get(0).getOpenStatus()+"',upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+userId+"', upd_user_name = '"+username+"'  where id=$S{t8DisclosureNoticeId}",params);
        }
        return flag;
    }

    /**
     * 功能：根据公告id和用户id查询未完成补录的角色
     * 查询补录人或者被转交人的记录
     *
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> findProcesssRole(DisclosureNoticeProcess params) throws Exception {
        return super.findRows("SELECT role_id " +
                " FROM idb_disclosure_notice_process " +
                " where t8_disclosure_notice_id = $S{t8DisclosureNoticeId} " +
                " AND user_id = $S{userId}  and input_status !='1'", DataSourceProperty.IDB, params);
    }

    public UpdateResult addDisNoticeProcess(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        return super.update("INSERT INTO idb_disclosure_notice_process(id,t8_disclosure_notice_id,role_id,user_id,to_user_id,open_status,input_status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name) VALUES($AUTOIDS{id},$S{t8DisclosureNoticeId},$S{roleId},$S{userId},$S{toUserId},$S{openStatus},$S{inputStatus},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName})",
                DataSourceProperty.IDB, params.getModel());
    }

    /**
     * 功能：插入公告数据录入人员信息
     * 作者：rennannan
     * 日期：20210604
     *
     * @param notice
     * @throws Exception
     */
    public String insertNoticeProcess(DisclosureNoticeProcess notice) throws Exception {
        return super.update("INSERT INTO idb_disclosure_notice_process(id,t8_disclosure_notice_id,role_id,user_id,to_user_id,open_status,input_status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name) VALUES($AUTOIDS{id},$S{t8DisclosureNoticeId},$S{roleId},$S{userId},$S{toUserId},$S{openStatus},$S{inputStatus},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName})", DataSourceProperty.IDB, notice).getAutoId();
    }


    public UpdateResult updateProcessInfo(DisclosureNoticeProcess process) throws Exception {
        return super.update("UPDATE idb_disclosure_notice_process SET role_id=$S{roleId} ,user_id=$S{userId} ,to_user_id=$S{toUserId} ,open_status=$S{openStatus} ,input_status=$S{inputStatus} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName}  WHERE  id=$S{id} ", DataSourceProperty.IDB, process);
    }

    public UpdateResult deleteDisNoticeProcess(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        return super.update("DELETE FROM idb_disclosure_notice_process WHERE  id=$S{id} ",
                DataSourceProperty.IDB, params.getModel());
    }

    /**
     * 功能：根据公告id删除process表中的数据
     * 作者：rennannan
     * 日期：202106069
     *
     * @param process
     * @return
     * @throws Exception
     */
    public int deleteProcessByNoticeId(DisclosureNoticeProcess process) throws Exception {
        StringBuilder sql = new StringBuilder("delete from idb_disclosure_notice_process where t8_disclosure_notice_id=$S{t8DisclosureNoticeId} ");
        if (StringUtils.isNotBlank(process.getRoleId())) {
            sql.append("and role_id = $S{roleId}");
        }
        return super.update(sql.toString(), DataSourceProperty.IDB, process).getEffect();
    }

    /**
     * 功能：根据公告生成日期、信披类型批量删除process表中数据
     * 作者：rennannan
     * 日期：20210609
     *
     * @return
     */
    public int deleteProcessByCrtDate(DisclosureNoticeProcess process) throws Exception {
        String sql = "delete  from idb_disclosure_notice_process \n" +
                "  where t8_disclosure_notice_id in ( select notice.id from idb_disclosure_notice notice" +
                " join idb_disclosure_prod_rule rule" +
                " on notice.t8_disclosure_rule_id=rule.id" +
                " where notice.crt_date = $S{crtDate} " +
                " and rule.disclosure_type=$S{disclosureType} ";
        if (StringUtils.isNotEmpty(process.getNotInNoticeIds())) {
            sql += " and id not in $U{notInNoticeIds}";
        }
        sql += ")";
        return super.update(sql, DataSourceProperty.IDB, process).getEffect();
    }

    /**
     * 功能：根据日期查询当日生成的公告数据指定信披类型的已经存在补录的数据
     * 作者：rennannan
     * 日期：20210630
     *
     * @return
     */
    public List<DisclosureNoticeProcess> findInputProcess(DisclosureNoticeProcess process) throws Exception {

        StringBuilder sql = new StringBuilder("select t8_disclosure_notice_id,notice.task_id from idb_disclosure_notice_process process " +
                "  join idb_disclosure_notice notice " +
                "    on process.t8_disclosure_notice_id = notice.id " +
                "  join idb_disclosure_prod_rule rule" +
                "    on notice.t8_disclosure_rule_id=rule.id " +
                " where notice.crt_date=$S{crtDate} " +
                "   and process.input_status in('1','2') ");
        if (StringUtils.isNotEmpty(process.getDisclosureType())) {
            sql.append(" and rule.disclosure_type=$S{disclosureType}");
        }
        sql.append(" group by t8_disclosure_notice_id,task_id ");
        return super.findRows(DisclosureNoticeProcess.class, sql.toString(), DataSourceProperty.IDB, process);

    }

    public Map<String, String> findMaxLatestVersions(DisclosureNoticeVersion params) throws Exception {
        List<SqlRow> rows = super.findRows("SELECT version,doc_type,id,file_path,file_name FROM idb_disclosure_notice_version WHERE version=(\n" +
                "SELECT IFNULL(max(version),'V1.0') version FROM idb_disclosure_notice_version WHERE t8_disclosure_notice_id=$S{t8DisclosureNoticeId}) AND t8_disclosure_notice_id=$S{t8DisclosureNoticeId}", DataSourceProperty.IDB, params);
        String version = "";
        Map<String, String> map = new HashMap<>();
        if (rows != null && rows.size() > 0) {
            map.put("version", rows.get(0).getString("version"));
            map.put("docType", rows.get(0).getString("doc_type"));
            map.put("id", rows.get(0).getString("id"));
            map.put("fileName", rows.get(0).getString("file_name"));
            map.put("filePath",rows.get(0).getString("file_path"));
            map.put("isComplete","1");
        }else{
            List<SqlRow> rows2 = super.findRows("SELECT tdmv.doc_name as file_name FROM idb_disclosure_notice tdn LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id " +
                    "LEFT JOIN idb_disclosure_mod_version tdmv ON tdpr.t8_disclosure_version_id=tdmv.id WHERE tdn.id=$S{t8DisclosureNoticeId}", DataSourceProperty.IDB, params);
            map.put("fileName",rows2.get(0).getString("file_name"));
            map.put("version","V1.0");
            map.put("isComplete","0");
        }

        return map;
    }

    public Map<String,String> findMaxVersions(DisclosureNoticeVersion params) throws Exception {
        List<SqlRow> rows = super.findRows("SELECT IFNULL(MAX(notice_version),'V1.0') notice_version,doc_type,id,file_path,file_name " +
                "FROM idb_disclosure_notice_version WHERE t8_disclosure_notice_id=$S{t8DisclosureNoticeId}", DataSourceProperty.IDB, params);
        String version = "";
        Map<String,String> map = new HashMap<>();
        if (rows != null && rows.size() > 0) {
            String maxVersion = rows.get(0).getString("notice_version");
            if(Tools.isNotEmpty(maxVersion) && maxVersion!=null && maxVersion!="" ){
                String[] data = maxVersion.split("V");
                String nowVersion = data[1];
                String[] number = nowVersion.split("\\.");
                String prefix = number[0];
                String suffix = number[1];
                if("9".equals(suffix)){
                    Integer pre = Integer.parseInt(prefix)+1;
                    version = "V"+pre+".0";
                }else{
                    Integer suf = Integer.parseInt(suffix)+1;
                    version = "V"+prefix+"."+suf;
                }
            }else{
                version="V1.0";
            }
            map.put("version",version);
            map.put("docType",rows.get(0).getString("doc_type"));
            map.put("id",rows.get(0).getString("id"));
            map.put("fileName",rows.get(0).getString("file_name"));
            map.put("filePath",rows.get(0).getString("file_path"));
        }else{
            version="V1.0";
            map.put("version",version);
            map.put("docType","");
            map.put("id","");
            map.put("fileName","");
            map.put("filePath","");
        }
        return map;
    }

    //公告版本下载功能获取最大版本号id
    public Map<String,Object> findMaxVersionById(DisclosureNoticeVersion params) throws Exception {
        Map<String,Object> map = new HashMap<>();
        SqlRow rowRes = super.findRow("SELECT " +
                "  IFNULL(notice.notice_version, 'V1.0') notice_version, " +
                "  notice.doc_type, " +
                "  notice.id AS max_version_id, " +
                "  notice.file_path, " +
                "  notice.file_name  " +
                "FROM " +
                "(SELECT MAX(CONVERT(id,SIGNED)) id FROM idb_disclosure_notice_version WHERE t8_disclosure_notice_id=$S{t8DisclosureNoticeId}) t " +
                "LEFT JOIN idb_disclosure_notice_version notice ON  t.id = notice.id ", DataSourceProperty.IDB, params);
        if (rowRes.size() > 0) {
            map.put("maxVersionId", rowRes.getString("max_version_id"));
            map.put("maxVersion", rowRes.getString("notice_version"));
            map.put("filePath", rowRes.getString("file_path"));
            map.put("fileName", rowRes.getString("file_name"));
        }
        return map;
    }

    public List<SqlRow> findVersionsInfo(DisclosureNoticeVersion params) throws Exception {
        List<SqlRow> rows =  super.findRows("SELECT tdn.prod_code,tdmc.file_name,tdmc.upload_path FROM idb_disclosure_notice tdn LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id LEFT JOIN idb_disclosure_mod_version tdmv ON tdmv.id=tdpr.t8_disclosure_version_id LEFT JOIN idb_disclosure_mod_column tdmc ON tdmc.t8_disclosure_version_id=tdmv.id WHERE tdn.id=$S{t8DisclosureNoticeId} LIMIT 1", DataSourceProperty.IDB, params);
        if(rows!=null && rows.size()>0){
            return rows;
        }else{
            return null;
        }
    }

    //查询公告进程数据的count
    public SqlRow findProcessCount(DisclosureNoticeProcess pQuery) throws Exception {
        return super.findRow("SELECT count(input_status = '0' or null) count0, count(input_status ='1' or null) count1,count(role_id ='9' or null) count2, " +
                " count(role_id ='14' or null) count3,count(id) count4 FROM idb_disclosure_notice_process where t8_disclosure_notice_id = $S{t8DisclosureNoticeId}", DataSourceProperty.IDB, pQuery);
    }

    public int updateUser(HashMap<String, Object> dataMap) throws Exception {
        return super.update("update idb_disclosure_notice_process set user_id  = (select userid from sys_user where jobno = $S{empNo}) where id in ('"+dataMap.get("userIds")+"')", DataSourceProperty.IDB, dataMap).getEffect();
    }

    public void executeSql(String sql,DisclosureNoticeVersion disclosureNoticeVersion) throws Exception{
        super.update(sql, DataSourceProperty.IDB, disclosureNoticeVersion);
    }
}
