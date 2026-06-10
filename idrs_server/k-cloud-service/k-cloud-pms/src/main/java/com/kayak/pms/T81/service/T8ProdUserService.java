package com.kayak.pms.T81.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.T81.dao.T8ProdInfoDao;
import com.kayak.pms.T81.dao.T8ProdUserDao;
import com.kayak.pms.T81.model.T8ProdUser;
import com.kayak.pms.basePublish.dao.DisclosureWordDateDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeDao;
import com.kayak.pms.disclosureControl.dao.DisclosureOperationDao;
import com.kayak.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@APIDefine(desc = "产品用户服务", model = T8ProdUser.class)
public class T8ProdUserService {

    @Autowired
    private T8ProdUserDao T8ProdUserDao;
    @Autowired
    private T8ProdInfoDao T8ProdInfoDao;
    @Autowired
    private DisclosureNoticeDao disclosureNoticeDao;
    @Autowired
    private DisclosureWordDateDao wordDateDao;
    @Autowired
    private DisclosureOperationDao disclosureOperationDao;

    @API(desc = "查询产品用户信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<T8ProdUser> findT8ProdUsers(SqlParam<T8ProdUser> params) throws Exception {
        params.setMakeSql(true);
        return T8ProdUserDao.findT8ProdUsers(params);
    }

    @API(desc = "添加产品用户", params = "id,t8_prod_base_id,role_id,userid_b,upperid,statu_a,inputuser,crt_date,crt_time,upd_date,upd_time,userid_a", auth = APIAuth.NO,operation = APIOperation.INSTER)
    public int addT8ProdUser(SqlParam<T8ProdUser> params) throws Exception {
        return T8ProdUserDao.addT8ProdUser(params).getEffect();
    }

    @API(desc = "修改产品用户", params = "id,t8_prod_base_id,role_id,userid_b,upperid,statu_a,inputuser,crt_date,crt_time,upd_date,upd_time,userid_a", auth = APIAuth.NO,operation = APIOperation.UPDATE)
    public int updateT8ProdUser(SqlParam<T8ProdUser> params) throws Exception {
        return T8ProdUserDao.updateT8ProdUser(params).getEffect();
    }

    @API(desc = "删除产品用户", params = "id,t8_prod_base_id,role_id,userid_b,upperid,statu_a,inputuser,crt_date,crt_time,upd_date,upd_time,userid_a", auth = APIAuth.NO,operation = APIOperation.DELETE)
    public int deleteT8ProdUser(SqlParam<T8ProdUser> params) throws Exception {
        return T8ProdUserDao.deleteT8ProdUser(params).getEffect();
    }

    @API(desc = "查询产品用户信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdUser> findT8ProdUserAll(SqlParam<T8ProdUser> params) throws Exception {
        params.setMakeSql(false);
        //   List<T8ProdUser> t8ProdUsers = T8ProdUserString userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
////        Dao.findT8ProdUserAll(params);
//        SqlResult<T8ProdUser> mapSqlResult = new SqlResult<T8ProdUser>();
//        mapSqlResult.setRows(t8ProdUsers);
        return T8ProdUserDao.findT8ProdUserAll(params);
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
    @API(desc = "查询用户所属产品信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdUser> findProdByUserRoleId(SqlParam<T8ProdUser> params) throws Exception {
        return T8ProdUserDao.findProdByUserRoleId(params);
    }

    @API(desc = "判断产品是否有用户组", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<Map<String, Object>> countProdUserByProdId(SqlParam<T8ProdUser> params) throws Exception {
        return T8ProdUserDao.countProdUserByProdId(params);
    }

    @API(desc = "查询产品用户组", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<T8ProdUser> getProdUserByProdId(SqlParam<T8ProdUser> params) throws Exception {
        return T8ProdUserDao.getProdUserByProdId(params);
    }

    @API(desc = "删除产品用户", auth = APIAuth.NO,operation = APIOperation.DELETE)
    public int deleteByT8ProdInfoId(String t8ProdInfoId) throws Exception {
        return T8ProdUserDao.deleteByT8ProdInfoId(t8ProdInfoId);
    }

}
