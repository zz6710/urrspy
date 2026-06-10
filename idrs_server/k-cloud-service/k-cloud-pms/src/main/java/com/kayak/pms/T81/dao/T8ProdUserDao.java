package com.kayak.pms.T81.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.T81.model.T8ProdUser;
import com.kayak.utils.SqlUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class T8ProdUserDao extends ComnDao {

    public SqlResult<T8ProdUser> findT8ProdUsers(SqlParam<T8ProdUser> params) throws Exception {
        return super.findRows("SELECT id,t8_prod_info_id,role_id,userid_b,upperid,statu_a,inputuser,crt_date,crt_time,upd_date,upd_time,userid_a FROM t8_prod_user", params);
    }

    public UpdateResult addT8ProdUser(SqlParam<T8ProdUser> params) throws Exception {
        return super.update("INSERT INTO t8_prod_user(id,t8_prod_info_id,role_id,userid_b,upperid,statu_a,inputuser,crt_date,crt_time,upd_date,upd_time,userid_a) VALUES($AUTOIDS{id},$S{T8ProdInfoId},$S{roleId},$S{useridB},$S{upperid},$S{statuA},$S{inputuser},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime},$S{useridA})",
                params.getModel());
    }

    public UpdateResult updateT8ProdUser(SqlParam<T8ProdUser> params) throws Exception {
        return super.update("UPDATE t8_prod_user SET t8_prod_info_id=$S{T8ProdInfoId} ,role_id=$S{roleId} ,userid_b=$S{useridB} ,upperid=$S{upperid} ,statu_a=$S{statuA} ,inputuser=$S{inputuser} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,userid_a=$S{useridA}  WHERE  id=$S{id} ",
                params.getModel());
    }

    public UpdateResult deleteT8ProdUser(SqlParam<T8ProdUser> params) throws Exception {
        return super.update("DELETE FROM t8_prod_user WHERE  id=$S{id} ",
                params.getModel());
    }

//    public List<T8ProdUser> findT8ProdUserAll(String userid) throws Exception {
//        return super.findRows(T8ProdUser.class, "select userid from ( " +
//                        "select userid_a userid ,t8_prod_info_id  from t8_prod_user " +
//                        "union all  " +
//                        "select userid_b userid ,t8_prod_info_id from t8_prod_user " +
//                        ") t  where t.userid = '" +userid+"' "
//                , 0, null);
//    }

    public SqlResult<T8ProdUser> findT8ProdUserAll(SqlParam<T8ProdUser> params) throws Exception {
        return super.findRows("select t8_prod_info_id from t8_prod_user where userid_a = $S{userid} or userid_b = $S{userid} ", params);
    }

    public SqlResult<Map<String,Object>> countProdUserByProdId(SqlParam<T8ProdUser> params) throws Exception {
        String sql = "select id cont from t8_prod_user t where t.t8_prod_info_id=$S{t8ProdInfoId}";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, params.getParams(), this);
    }

    public SqlResult<T8ProdUser> getProdUserByProdId(SqlParam<T8ProdUser> params) throws Exception {
        return super.findRows("SELECT id,t8_prod_info_id,role_id,userid_b,upperid,statu_a,inputuser,crt_date,crt_time,upd_date,upd_time,userid_a FROM t8_prod_user where t8_prod_info_id = $S{t8ProdInfoId} ", params);
    }

    public List<T8ProdUser> getProdUserByProdId(String t8ProdInfoId) throws Exception {
        return super.findRows(T8ProdUser.class,"SELECT id,t8_prod_info_id,role_id,userid_b,upperid,statu_a,inputuser,crt_date,crt_time,upd_date,upd_time,userid_a FROM t8_prod_user where t8_prod_info_id = $S{t8ProdInfoId} ",0, t8ProdInfoId);
    }

    public int deleteByT8ProdInfoId(String t8ProdInfoId) throws Exception {
        return super.update("delete from t8_prod_user where t8_prod_info_id=$S{t8ProdInfoId}", t8ProdInfoId).getEffect();
    }

    public int addT8ProdUser(T8ProdUser t8ProdUser) throws Exception {
        return super.update("INSERT INTO t8_prod_user(id,t8_prod_info_id,role_id,userid_b,upperid,statu_a,inputuser,crt_date,crt_time,upd_date,upd_time,userid_a) VALUES($AUTOIDS{id},$S{t8ProdInfoId},$S{roleId},$S{useridB},$S{upperid},$S{statuA},$S{inputuser},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime},$S{useridA})",
                t8ProdUser).getEffect();
    }

    public int deleteT8ProdUserIdA(String userIdA,String prodInfoId) throws Exception {
        return super.update("DELETE FROM t8_prod_user WHERE userid_a=$S{userIdA} and t8_prod_info_id ='"+prodInfoId+"'and role_id = '14'",
                userIdA).getEffect();
    }

    
    public int updateUserIdA(String newUserId,String oldUserid) throws Exception {
        return super.update("update t8_prod_user set userid_a = $S{newUserId} where userid_a="+oldUserid+" and role_id = '14'",newUserId).getEffect();
    }

    public int updateUserInfo(T8ProdUser t8ProdUser) throws Exception {
        return super.update("update t8_prod_user set t8_prod_info_id = $S{t8ProdInfoId},statu_a = $S{statuA},inputuser = $S{inputuser},\n"+
                "crt_date = $S{crtDate},crt_time = $S{crtTime} where userid_a = $S{useridA} and role_id = '14'",t8ProdUser).getEffect();
    }
    /**
     * 功能：根据用户id与角色id查询对应的产品id
     * 作者：rennannan
     * 日期：20210615
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8ProdUser> findProdByUserRoleId(SqlParam<T8ProdUser> params) throws Exception {
        String sql = "select t8_prod_info_id,role_id from t8_prod_user where userid_a = $S{userid}";
        return super.findRows(sql, params);

    }
    
    public SqlRow findUserInfo(String userId) throws Exception {
        String sql = "select jobno from sys_user where  userid = $S{userid}";
        return super.findRow(sql, userId);
    }

    public SqlRow findMAXDateTime(String newInvestId) throws Exception {
        String sql = "SELECT\n" +
                "\tmax(concat(create_date, create_time)) maxTime\n" +
                "FROM\n" +
                "\tt8_prod_invest_manager \n" +
                "WHERE new_invest_id = $S{newInvestId}";
        return super.findRow(sql, newInvestId);
    }
    
    
    public List<SqlRow> findInvestManages(String prodCode) throws Exception {
        String sql = "select userid_a from  t8_prod_user u left join t8_prod_info t on u.t8_prod_info_id = t.id  where u.role_id='14' and t.prod_code = '"+prodCode+"'";
        return super.findRows(sql);

    }
    
}
