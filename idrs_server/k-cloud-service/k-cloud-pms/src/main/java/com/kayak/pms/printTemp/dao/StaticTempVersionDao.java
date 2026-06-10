package com.kayak.pms.printTemp.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.printTemp.model.PrintTempVersion;
import com.kayak.pms.printTemp.model.StaticTempVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @program: k-cloud
 * @description: 静态文档版本Dao
 * @author: WangZhenXin
 * @create: 2021-01-02 10:17
 * @memo 备注信息
 */
@Repository
public class StaticTempVersionDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(StaticTempVersionDao.class);

    public void saveStaticTempVersion(StaticTempVersion staticTempVersion) throws Exception {
        super.update("insert into t8_static_temp_version(id, t8_static_temp_id, version, file_path,temp_name, remark,status, create_date, create_time, create_user_id, create_user_name) " +
                "VALUES($AUTOIDS{t8_static_temp_version},$S{t8StaticTempId},$S{version},$S{filePath},$S{tempName}, $S{remark},'1', $S{createDate}, $S{createTime}, $S{createUserId}, $S{createUserName})", staticTempVersion);
    }

    public void saveStaticTempVersionByTrans(StaticTempVersion staticTempVersion) throws Exception {
        //将所有子版本设置为未生效状态
        //super.update("update t8_static_temp_version set status = '0' where t8_static_temp_id = $S{t8StaticTempId}", staticTempVersion);
        super.update("insert into t8_static_temp_version(id, t8_static_temp_id, version, status, file_path, temp_name, remark, create_date, create_time, create_user_id, create_user_name) " +
                    "VALUES($AUTOIDS{t8_static_temp_version},$S{t8StaticTempId},$S{version},'1', $S{filePath}, $S{tempName}, $S{remark}, $S{createDate}, $S{createTime}, $S{createUserId}, $S{createUserName})", staticTempVersion);

    }
    //更新为作废状态
    public void updateStaticTempVersionStatus(String t8StaticTempId, String oldVersion) throws Exception {
        String sql="UPDATE `t8_static_temp_version` SET `status` = '2' WHERE t8_static_temp_id='"+t8StaticTempId+"' AND version='"+oldVersion+"'";
           super.update(sql, null);

    }

    public SqlResult<StaticTempVersion> getStaticTempVersionByTempId(SqlParam<StaticTempVersion> param) throws Exception {
        param.setMakeSql(true);
        return super.findRows("select t.id,t.t8_static_temp_id,t.file_path,t.temp_name,t.version,t.remark,t.create_date,t.create_time,t.create_user_name,t.status from t8_static_temp_version t order by t.version desc", param);
    }

    public List<SqlRow> getNewestStaticTempVersion(String t8StaticTempId) throws Exception {
        return super.findRows("select t.version from t8_static_temp_version t where t.id = (select max(CONVERT(f.id,SIGNED))  id from " +
                "t8_static_temp_version f where f.t8_static_temp_id = $S{t8StaticTempId})", t8StaticTempId);

    }

    public Integer updateStaticTempVersionStatus(String id) throws Exception {
        return super.update("update t8_static_temp_version t set t.status = '1' where t.id= $S{id}", id).getEffect();
    }
    
    public Integer deleteTempVersion(String id) throws Exception {
        return super.update("update t8_static_temp_version t set t.status = '2' where t.id= $S{id}", id).getEffect();
    }

}
