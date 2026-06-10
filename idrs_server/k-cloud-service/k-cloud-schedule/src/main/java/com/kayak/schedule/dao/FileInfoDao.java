package com.kayak.schedule.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;


@Repository
public class FileInfoDao extends ComnDao {


	
	
	// 理财登 “发布队列”中公告
	public List<SqlRow> queryLCDNotices() throws Exception {
		return super.findRows("select n.id from idb_disclosure_notice n "
									+"  LEFT JOIN idb_disclosure_notice_channel c "
									+"  	on n.id = c.disclosure_notice_id " 
									+"  LEFT JOIN idb_disclosure_channel_info i "
									+"  	on c.disclosure_notice_channel_id = i.id " 
									+"  where channel_name ='光大理财官网' and lcd_status = '1' ");
	}
	
	
	// 当前公告最大版本信息
	public SqlRow queryNoticeMaxV(Map<String,Object> param) throws Exception {
    return super.findRow(
        "SELECT\n"
            + "\tv.version      version   ,\n"
            + "\tv.file_name   fileName   ,\n"
            + "\tv.crt_path filePath   ,\n"
            + "\tn.disclosure_type    disclosureType  ,\n"
            + "\tn.disclosure_son_type   disclosureSonType  ,\n"
            + "\tn.prod_code           prodCode    ,\n"
            + "\tv.t8_disclosure_notice_id    t8DisclosureNoticeId  ,\n"
            + "\tinfo.regist_code       registCode \n"
            + "FROM\n"
            + "\tidb_disclosure_notice_version v\n"
            + "\tLEFT JOIN idb_disclosure_notice n ON v.t8_disclosure_notice_id = n.id \n"
            + "\tLEFT JOIN t8_prod_info info on n.prod_code=info.prod_code\n"
            + "WHERE\n"
            + "\tv.t8_disclosure_notice_id = $S{id}  ORDER BY v.id+0 desc \n"
            + "\tLIMIT 1 ",
        param);
	}

}