package com.kayak.pms.basePublish.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.basePublish.model.DisclosureWordDate;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeProcess;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/5/14 15:08
 */
@Repository
public class DisclosureWordDateDao extends ComnDao {
    public void insert(DisclosureWordDate disclosureWordDate) throws Exception {
        super.update("INSERT INTO idb_disclosure_word_date (id, prod_code, disclosure_type, report_date, column_key, column_value,\n" +
                "                                             crt_date, crt_time, crt_user, upd_date, upd_time, upd_user)\n" +
                "VALUES ($AUTOIDS{disclosureWordId}, $S{prodCode}, $S{disclosureType}, $S{reportDate}, $S{columnKey}, $S{columnValue}, $S{crtDate},\n" +
                "        $S{crtTime}, $S{crtUser}, $S{updDate}, $S{updTime}, $S{updUser})", disclosureWordDate);
    }

    public void update(DisclosureWordDate disclosureWordDate) throws Exception {
        super.update("update idb_disclosure_notice_value set column_value=$S{columnValue},upd_date=$S{updDate}, upd_time=$S{updTime}, upd_user_id=$S{updUser} where t8_disclosure_notice_id=$S{t8DisclosureNoticeId} " +
                " and column_key=$S{columnKey}", disclosureWordDate);
    }

    public void insertValue(DisclosureWordDate disclosureWordDate) throws Exception {
        super.update("INSERT INTO idb_disclosure_notice_value(id,t8_disclosure_notice_id,prod_code,column_key,column_value,roleids) VALUES($AUTOIDS{id},$S{t8DisclosureNoticeId},$S{prodCode},$S{columnKey},$S{columnValue},'14')", disclosureWordDate);
    }

    public Integer updateDisNoticeProcess(DisclosureNoticeProcess params) throws Exception {
        return super.update("UPDATE idb_disclosure_notice_process SET input_status=$S{inputStatus} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} WHERE  t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and  (user_id = $S{userId} or to_user_id = $S{userId})",
                params).getEffect();
    }

    public Integer updateDisNoticeProcessByRoleIds(DisclosureNoticeProcess params) throws Exception {
        return super.update("UPDATE idb_disclosure_notice_process SET input_status=$S{inputStatus} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} WHERE  t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and role_id IN ($U{roleIds}) ",
                params).getEffect();
    }

    public int updateUserId(String newUserId,String oldUserid,String noticeId) throws Exception {
        return super.update("update idb_disclosure_notice_process set user_id = '"+newUserId+"' where user_id= '"+oldUserid+"' and role_id = '14' and t8_disclosure_notice_id='"+noticeId+"'",newUserId).getEffect();
    }

    //更新 补录分发中 投资经理信息
    public Integer updNoticeProcessInfo(DisclosureNoticeProcess disclosureNoticeProcess) throws Exception {
        return super.update("update idb_disclosure_notice_process set upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} WHERE t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and role_id = '14' and user_id =$S{userId}",
                disclosureNoticeProcess).getEffect();
    }


	public SqlRow find(DisclosureWordDate disclosureWordDate) throws Exception {

		return super.findRow("SELECT t8_disclosure_notice_id from idb_disclosure_notice_value  where t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and column_key=$S{columnKey}", disclosureWordDate);

	}

    public SqlRow findProdBaseDate(String id) throws Exception {

        return super.findRow("SELECT prod_base_date from idb_disclosure_notice where id=$S{id}", id);

    }

    public List<SqlRow> getNoticeId(String prodCode) throws Exception {
        return super.findRows("SELECT id from idb_disclosure_notice where prod_code=$S{prodCode} and current_stage_status != '13'", prodCode);
    }

    public List<SqlRow> getNoticeId1(String t8ProdInfoId) throws Exception {
        //获取 当前日期 属于 第几季度
        String baseDate = DateUtil.getCurrentQuarterNumbers();
        return super.findRows("SELECT id from idb_disclosure_notice  where t8_prod_info_id=$S{t8ProdInfoId} and prod_base_date = '"+baseDate+"'", t8ProdInfoId);
    }
    public List<SqlRow> getUserId(String t8DisclosureNoticeId) throws Exception {

        return super.findRows("SELECT user_id from idb_disclosure_notice_process where t8_disclosure_notice_id=$S{t8DisclosureNoticeId}", DataSourceProperty.IDB, t8DisclosureNoticeId);
    }

    public int addDisclosureNoticeProcess(DisclosureNoticeProcess disclosureNoticeProcess) throws Exception {
        return super.update("INSERT INTO idb_disclosure_notice_process(id,t8_disclosure_notice_id,role_id,user_id,input_status,crt_date,crt_time) VALUES($AUTOIDS{id},$S{t8DisclosureNoticeId},$S{roleId},$S{userId},$S{inputStatus},$S{crtDate},$S{crtTime})",
                disclosureNoticeProcess).getEffect();
    }
    public SqlRow getProcessId(DisclosureNoticeProcess disclosureNoticeProcess) throws Exception {
        return super.findRow("select id from idb_disclosure_notice_process where t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and user_id =$S{userId} and role_id = '14' ", disclosureNoticeProcess);
    }

    //更新产品基本信息表数据
    public void updtProdInfo(String prodDesc,String prodCode) throws Exception {
        super.update("update t8_prod_info set prod_desc = '"+prodDesc+"' where prod_code = '"+prodCode+"' ");
    }

    public int deleteUserId(String userId,String noticeId) throws Exception {
        return super.update("DELETE FROM idb_disclosure_notice_process WHERE user_id=$S{userId} and t8_disclosure_notice_id ='"+noticeId+"' and role_id = '14'",
                userId).getEffect();
    }

    public int deleteByNoticeId(String noticeId) throws Exception {
        return super.update("DELETE FROM idb_disclosure_notice_process WHERE t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and role_id = '14'",
                noticeId).getEffect();
    }

}
