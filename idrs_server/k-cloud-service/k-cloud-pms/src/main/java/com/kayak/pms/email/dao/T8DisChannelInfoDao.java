package com.kayak.pms.email.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.email.model.T8DisChannelInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;
import com.kayak.pms.basePublish.model.DisclosureChannel;

import java.util.List;
import java.util.Map;

@Repository
public class T8DisChannelInfoDao extends ComnDao {


    public List<SqlRow> findT8DisChannelInfoBytype1(String channel_code) throws Exception {
        return super.findRows("select channel_type,emails from idb_disclosure_channel_info ");
    }

    /**
     * 功能：根据查询条件查询渠道信息最为下拉框
     * 作者：rennannan
     * 日期：20210524
     *
     * @param param
     * @return
     */
    public SqlResult<T8DisChannelInfo> findSelectDisChannel(SqlParam<T8DisChannelInfo> param) throws Exception {
        String sql = " select id,channel_type,channel_code,channel_name,emails from idb_disclosure_channel_info where  channel_type in ('0','1') ";
        return super.findRows(sql, param);
    }

    /*页面信披渠道多选框查询*/
    public SqlResult<T8DisChannelInfo> findDisChannel(SqlParam<T8DisChannelInfo> param) throws Exception {
        String sql = "select id,channel_name from idb_disclosure_channel where status='1'";
        return super.findRows(sql, param);
    }

    /**
     * 功能：根据id查询渠道信息
     * 作者：rennannan
     * 日期：20210524
     *
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<T8DisChannelInfo> findDisChannelsByIds(SqlParam<T8DisChannelInfo> param) throws Exception {
        String sql = "select id,channel_type,channel_code,channel_name,emails from idb_disclosure_channel_info where FIND_IN_SET(id,$S{customChannels})";
        return super.findRows(sql, param);
    }

    public int updateT8DisChannelInfoByType(T8DisChannelInfo T8DisChannelInfo) throws Exception {
        StringBuilder sql = new StringBuilder("   UPDATE idb_disclosure_channel_info  SET emails = $S{emails},  ");
        //判断表单一是否录入邮箱密码
        if (T8DisChannelInfo.getId().equals("1") && T8DisChannelInfo.getChannelType().equals("1")) {
            if (!T8DisChannelInfo.getEmailPasswd().equals("")) {
                sql.append("  email_passwd = $S{emailPasswd},   ");
            }
        }
        sql.append("   upd_date = $S{updDate}, upd_time = $S{updTime},upd_user_id = $S{updUserId},upd_user_name = $S{updUserName}  WHERE channel_type  = $S{channelType}  AND id  = $S{id}    ");
        return super.update(sql.toString(), T8DisChannelInfo).getEffect();
    }

    /**
     * 功能：新增渠道信息
     * 作者：rennannan
     * 日期：20210524
     *
     * @param
     * @return
     * @throws Exception
     */
    public String insertChannelInfo(T8DisChannelInfo t8DisChannelInfo) throws Exception {
        String sql = "insert into idb_disclosure_channel_info(id,channel_type,channel_code,channel_name,emails,email_passwd," +
                " crt_date,crt_time,crt_user_id,crt_user_name,remark)" +
                "        values($AUTOIDS{id},$S{channelType},$S{channelCode},$S{channelName},$S{emails},$S{emailPasswd}," +
                "                        $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{remark})";
        return super.update(sql, t8DisChannelInfo).getAutoId();
    }

    /**
     * 功能：修改渠道信息
     * 作者：rennannan
     * 日期：20210524
     *
     * @param t8DisChannelInfo
     * @return
     * @throws Exception
     */
    public int updateChannelInfo(T8DisChannelInfo t8DisChannelInfo) throws Exception {
        String sql = " update idb_disclosure_channel_info" +
                " set channel_type=$S{channelType}," +
                " channel_code=$S{channelCode}," +
                " channel_name=$S{channelName}," +
                " emails=$S{emails}," +
                " email_passwd=$S{emailPasswd}," +
                " remark=$S{remark}," +
                " upd_date = $S{updDate}, " +
                " upd_time = $S{updTime}," +
                " upd_user_id = $S{updUserId}," +
                " upd_user_name = $S{updUserName}" +
                " where id = $S{id}";
        return super.update(sql, t8DisChannelInfo).getEffect();
    }

    public SqlResult<T8DisChannelInfo> findChannelName(SqlParam<T8DisChannelInfo> params) throws Exception {
        return super.findRows(" select id,channel_type,channel_code,channel_name from idb_disclosure_channel_info ORDER BY channel_type ", params);
    }

    public SqlResult<T8DisChannelInfo> findT8DisChannelInfoAll(SqlParam<T8DisChannelInfo> params) throws Exception {
        return super.findRows(" select id,channel_type,channel_code,channel_name,emails,email_passwd,status,ifnull(upd_date,crt_date) crt_date,ifnull(upd_time, crt_time) crt_time,ifnull(upd_user_name, crt_user_name) crt_user_name from idb_disclosure_channel_info where channel_type in ('0','1')", params);
    }

    public int stopStatus(SqlParam<T8DisChannelInfo> params) throws Exception {
        return super.update(
                "UPDATE idb_disclosure_channel_info SET status='D' WHERE id=$S{id}",
                params.getModel()).getEffect();
    }

    public int recoverStatus(SqlParam<T8DisChannelInfo> params) throws Exception {
        return super.update(
                "UPDATE idb_disclosure_channel_info SET status='N' WHERE id=$S{id}",
                params.getModel()).getEffect();
    }

    public UpdateResult deleteRowDateById(SqlParam<T8DisChannelInfo> params) throws Exception {
        return super.update("DELETE FROM idb_disclosure_channel_info WHERE  id=$S{id} ",
                params.getModel());
    }

    public void deleteRowDateById(String id) throws Exception {
         super.update("DELETE FROM idb_disclosure_channel_info WHERE  id='"+id+"';",id);
    }

    public int findT8DisChannelInfoCount(Map<String,Object> params) throws Exception {
        List<SqlRow> datas=super.findRows("SELECT COUNT(*) count FROM idb_disclosure_channel_info WHERE channel_type = $S{channelType}", params);
        return datas.get(0).getInteger("count");
    }

    public UpdateResult t8DisChannelInfoAdd( T8DisChannelInfo params) throws Exception {
        String sql = "insert into idb_disclosure_channel_info(id,channel_type,channel_name,emails,email_passwd," +
                " crt_date,crt_time,crt_user_id,crt_user_name,status)" +
                "        values($AUTOIDS{idb_disclosure_channel_info},$S{channelType},$S{channelName},$S{emails},$S{emailPasswd}," +
                "                        $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},'N')";
        return super.update(sql, params );
    }

    public UpdateResult t8DisChannelInfoUpdate( T8DisChannelInfo params) throws Exception {
        String sql = " update idb_disclosure_channel_info" +
                " set channel_type=$S{channelType}," +
                " channel_name=$S{channelName}," +
                " emails=$S{emails}," +
                " email_passwd=$S{emailPasswd}," +
                " remark=$S{remark}," +
                " upd_date = $S{updDate}, " +
                " upd_time = $S{updTime}," +
                " upd_user_id = $S{updUserId}," +
                " upd_user_name = $S{updUserName}" +
                " where id = $S{id}";
        return super.update(sql, params);
    }

    /**
     * 功能：查询渠道信息list
     * 作者：rennannan
     * 日期：20210628
     *
     * @param params
     * @return
     * @throws Exception
     */
    public List<T8DisChannelInfo> findChannelInfoList(T8DisChannelInfo params) throws Exception {
        String sql = " select id,channel_type,channel_code,channel_name,emails " +
                " from idb_disclosure_channel_info" +
                " where 1=1 ";
        if (StringUtils.isNotEmpty(params.getChannelType())) {
            sql += " and channel_type=$S{channelType}";
        }
        return super.findRows(T8DisChannelInfo.class, sql, 0, params);
    }

    public List<DisclosureChannel> findChannelsByIds(String channelIds) throws Exception {
        return super.findRows(DisclosureChannel.class, "select *  from idb_disclosure_channel  where id in ("+channelIds+")",
                DataSourceProperty.IDB, null);
    }
}
