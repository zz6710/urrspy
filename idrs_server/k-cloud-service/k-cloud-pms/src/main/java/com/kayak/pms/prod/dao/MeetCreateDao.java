package com.kayak.pms.prod.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.prod.model.MeetCreate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class MeetCreateDao extends ComnDao {


    public SqlResult<MeetCreate> findMeetCreate(SqlParam<MeetCreate> params) throws Exception {

        return super.findRows("select t.id,t.meet_name,t.meet_site,t.meet_date,t.meet_time,t.username,t.approval_status,t.crt_date,t.crt_time,\n" +
                "(select username from sys_user where userid = t.crt_user ) crt_user,\n" +
                "t.upd_date,t.upd_time,\n" +
                "(select username from sys_user where userid = t.upd_user ) upd_user \n" +
                "from t8_meet_create t"  , params);

    }

    public SqlResult<MeetCreate> findMeetCreate2(SqlParam<MeetCreate> params) throws Exception {
        String sql = "select t.id,t.meet_name,t.meet_site,t.meet_date,t.meet_time,t.username,t.approval_status,t.crt_date,t.crt_time,\n" +
                "(select username from sys_user where userid = t.crt_user ) crt_user,\n" +
                "t.upd_date,t.upd_time,\n" +
                "(select username from sys_user where userid = t.upd_user ) upd_user \n" +
                "from t8_meet_create t " +
                " where 1=1 ";
        if(Tools.isNotEmpty(params.getModel().getMeetName())){
            sql = sql + " and meet_name like '%"+params.getModel().getMeetName()+"%'";
        }
        if(Tools.isNotEmpty(params.getModel().getCreateStartDate())){
            sql = sql + " and meet_date BETWEEN $S{createStartDate} and $S{createEndDate}";
        }
        return super.findRows(sql, params);

    }

    //新增产品操作类型组件
    public int addMeetCreate(SqlParam<MeetCreate> params) throws Exception {
        String meetName = params.getModel().getMeetName();
        List<MeetCreate> meetCreates = super.findRows("select * from t8_meet_create where meet_name = '" + meetName +"'" , params).getRows();
        if (!CollectionUtils.isEmpty(meetCreates)) {
            throw new PromptException("存在会议记录，请核对");
        }
        return super.update(
                " insert into t8_meet_create (id,meet_name,meet_site,meet_date,meet_time,username, " +
                        "approval_status,crt_date,crt_time,crt_user)  " +
                        "values ($AUTOIDS{id},$S{meetName},$S{meetSite},$S{meetDate},$S{meetTime},$S{username}" +
                        ",$S{approvalStatus},$S{crtDate},$S{crtTime},$S{crtUser})",
                params.getModel()).getEffect();

    }

    public int updateMeetCreate(SqlParam<MeetCreate> params) throws Exception {
        return super.update(
                "update t8_meet_create set meet_name = $S{meetName} ,meet_site = $S{meetSite}," +
                        "meet_date = $S{meetDate},meet_time = $S{meetTime},username = $S{username}," +
                        "upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser} " +
                        " where id = $S{id}",

                params.getModel()).getEffect();
    }

    //删除页面组件
    public int deleteMeetCreate(SqlParam<MeetCreate> params) throws Exception {
        return super.update(
                "delete from t8_meet_create where id = $S{id} and meet_name = $S{meetName}",
                params.getModel()).getEffect();

    }
    /**
     * 功能：费用优惠确认页面显示会议下拉列表，按照会议日期倒序排序
     * 作者：rnn
     * 日期：20210203
     */
    public SqlResult<MeetCreate> findMeetDict(SqlParam<MeetCreate> params) throws Exception {
        return super.findRows("select meet.id, meet.meet_name " +
                                    "from t8_meet_create meet " +
                                "order by meet.meet_date desc "  , params);

    }
}
