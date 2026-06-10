package com.kayak.pms.indexInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.indexInfo.model.T8IndexInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: k-cloud
 * @description: 指数信息Dao
 * @author: WangZhenXin
 * @create: 2021-01-13 17:34
 * @memo 备注信息
 */
@Repository
public class T8IndexInfoDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(T8IndexInfoDao.class);

    public SqlResult<T8IndexInfo> find(SqlParam<T8IndexInfo> param) throws Exception {
        param.setMakeSql(true);
        return super.findRows("select id, " +
                "       index_code, " +
                "       index_name, " +
                "       base_date, " +
                "       base_points, " +
                "       constituent_stocks, " +
                "       create_date, " +
                "       create_time, " +
                "       update_date, " +
                "       update_time, " +
                "       create_user_id, " +
                "       create_user_name " +
                "from t8_index_info",param);
    }

    public int add(SqlParam<T8IndexInfo> param) throws Exception {
        AtomicInteger flag = new AtomicInteger();
        doTrans(()->{
            T8IndexInfo t8IndexInfo = param.getModel();
            flag.set(super.update("insert into t8_index_info(id, index_code, index_name, base_date, base_points, constituent_stocks, create_date, " +
                    "                          create_time, create_user_id, create_user_name) " +
                    "VALUES ($AUTOIDS{t8_index_info}, $S{indexCode}, $S{indexName}, $S{baseDate}, $D{basePoints}, $S{constituentStocks}, " +
                    "        $S{createDate}, $S{createTime}, " +
                    "        $S{createUserId}, $S{createUserName})", t8IndexInfo).getEffect());
        });
        return flag.get();
    }

    public int update(SqlParam<T8IndexInfo> param) throws Exception {
        AtomicInteger flag= new AtomicInteger();
        doTrans(()->{
            T8IndexInfo t8IndexInfo = param.getModel();
            flag.set(super.update("update t8_index_info " +
                    "set index_name=$S{indexName}, " +
                    "    base_date=$S{baseDate}, " +
                    "    base_points=$D{basePoints}, " +
                    "    constituent_stocks=$S{constituentStocks}, " +
                    "    update_date=$S{updateDate}, " +
                    "    update_time=$S{updateTime} " +
                    "where id=$S{id}", t8IndexInfo).getEffect());
        });
        return flag.get();
    }

    public int delete(String id) throws Exception {
        AtomicInteger flag= new AtomicInteger();
        doTrans(()->{
          flag.set(super.update("delete from t8_index_info where id=$S{id} ", id).getEffect());
        });
        return flag.get();
    }

    public SqlRow checkIndexInfo(SqlParam<T8IndexInfo> param) throws Exception {
        String indexCode = param.getModel().getIndexCode();
        return super.findRow("select count(*) con from t8_prod_info t where t.prod_code = $S{indexCode}",indexCode);
    }

}
