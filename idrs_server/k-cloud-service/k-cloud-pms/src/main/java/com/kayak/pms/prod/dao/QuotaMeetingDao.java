package com.kayak.pms.prod.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.pms.prod.model.ProdStatusChange;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.QuotaMeeting;

@Repository
public class QuotaMeetingDao extends ComnDao {

	public SqlResult<QuotaMeeting> findQuotaMeetings(SqlParam<QuotaMeeting> params) throws Exception {
		return super.findRows("SELECT id,t8_prod_info_id,meeting_name,meeting_date,meeting_time,meeting_addr,decision_date,decision_maker,participant,meeting_state,type,prod_code,inputuser,updateuser,crt_date,crt_time,upd_date,upd_time FROM t8_quota_meeting", params);
	}

	/**
	 * 功能：根据产品代码与决策类型查询会议信息
	 * 作者：rennannan
	 * 日期：20210421
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<QuotaMeeting> findMeetByTypeAndCode(SqlParam<QuotaMeeting> params) throws Exception {
		String sql = "select id,meeting_name from t8_quota_meeting where FIND_IN_SET($S{prodCode},PROD_CODE) and type = $S{type}";
		return super.findRows(sql, params);
	}

	public SqlRow findeUsernameByUserIds(String userids) throws Exception {
		String sql = "select GROUP_CONCAT(username) userNames from sys_user where userid in (" + userids + ")";
		return super.findRow(sql, userids);
	}

	public UpdateResult addQuotaMeeting(SqlParam<QuotaMeeting> params) throws Exception {
		return super.update("INSERT INTO t8_quota_meeting(id,t8_prod_info_id,meeting_name,meeting_date,meeting_time,meeting_addr,decision_date,decision_maker,participant,meeting_state,type,prod_code,inputuser,updateuser,crt_date,crt_time,upd_date,upd_time) VALUES($AUTOIDS{id},$S{t8ProdInfoId},$S{meetingName},$S{meetingDate},$S{meetingTime},$S{meetingAddr},$S{decisionDate},$S{decisionMaker},$S{participant},$S{meetingState},$S{type},$S{prodCode},$S{inputuser},$S{updateuser},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime})",
				params.getModel());
	}
	
	public UpdateResult updateQuotaMeeting(SqlParam<QuotaMeeting> params) throws Exception {
		return super.update("UPDATE t8_quota_meeting SET t8_prod_info_id=$S{t8ProdInfoId} ,meeting_name=$S{meetingName} ,meeting_date=$S{meetingDate} ,meeting_time=$S{meetingTime} ,meeting_addr=$S{meetingAddr} ,decision_date=$S{decisionDate} ,decision_maker=$S{decisionMaker} ,participant=$S{participant} ,meeting_state=$S{meetingState} ,type=$S{type} ,prod_code=$S{prodCode} ,inputuser=$S{inputuser} ,updateuser=$S{updateuser} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,upd_date=$S{updDate} ,upd_time=$S{updTime}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteQuotaMeeting(SqlParam<QuotaMeeting> params) throws Exception {
		return super.update("DELETE FROM t8_quota_meeting WHERE  id=$S{id} ",
				params.getModel());
	}
	/**
	 * 功能：根据产品编号查询会议名称与决策类型
	 * 作者：rennannan
	 * 日期：20210203
	 */
	public SqlResult<QuotaMeeting> findMeetingByProdCode(SqlParam<QuotaMeeting> params)throws Exception {
		return super.findRows("select meet.MEETING_NAME,meet.TYPE " +
								"   from t8_quota_meeting meet " +
								"  where FIND_IN_SET($S{prodCode},meet.prod_code)",params);
	}
}
