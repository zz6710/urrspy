package com.kayak.pms.T82.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.T82.model.T82001ProdAuthority;
import org.springframework.stereotype.Repository;

@Repository
public class T8ProdAuthorityDao extends ComnDao {

    public SqlResult<T82001ProdAuthority> findProdAuthority(SqlParam<T82001ProdAuthority> params) throws Exception {
        return findRows(" SELECT PROD_CODE,DISTRIBUTOR_CODE,HANDLER_MODE,STATUS,INTEREST_CODE FROM t8_PROD_DISTRIBUTOR", params);
    }

    public UpdateResult addTaProdAuthority(SqlParam<T82001ProdAuthority> params) throws Exception {
        return super.update("INSERT INTO t8_PROD_DISTRIBUTOR(PROD_CODE, DISTRIBUTOR_CODE, HANDLER_MODE, INTEREST_CODE, STATUS, data_status) VALUES($S{prodCode},$S{distributorCode},$S{handlerMode},$S{interestCode},$S{status},'A')",
                params.getModel());
    }

    public UpdateResult updateTaProdAuthorityRelation(SqlParam<T82001ProdAuthority> params) throws Exception{
        return super.update("update  t8_PROD_DISTRIBUTOR set HANDLER_MODE = $S{handlerMode}, INTEREST_CODE = $S{interestCode}, STATUS = $S{status} where PROD_CODE = $S{prodCode} and DISTRIBUTOR_CODE = $S{distributorCode} ",
                params.getModel());
    }

    public int delTaByProdCodeAndDistributorCode(SqlParam<T82001ProdAuthority> params) throws Exception{
        return super.update("DELETE FROM t8_PROD_DISTRIBUTOR WHERE  PROD_CODE = $S{prodCode}  and DISTRIBUTOR_CODE=$S{distributorCode} ",
                params.getModel()).getEffect();
    }

    public SqlResult<T82001ProdAuthority> isprodCodeAndDistributorCode(SqlParam<T82001ProdAuthority> params) throws Exception {
        params.setMakeSql(false);
        return findRows("SELECT PROD_CODE from t8_PROD_DISTRIBUTOR where PROD_CODE = $S{prodCode}  and DISTRIBUTOR_CODE=$S{distributorCode}", params);
    }

}
