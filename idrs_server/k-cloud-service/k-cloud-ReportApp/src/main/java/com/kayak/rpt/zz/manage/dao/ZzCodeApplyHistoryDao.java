package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;
import com.kayak.rpt.zz.manage.model.ZzCodeApplyHistory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class ZzCodeApplyHistoryDao extends ComnDao {

    public SqlResult<ZzCodeApplyHistory> findZzCodeApplyHistorys(SqlParam<ZzCodeApplyHistory> params) throws Exception {
        String innerCode = params.getModel().getInnerCode();
        String sql = "SELECT  PROD_REG_ENC,INNER_CODE,PROD_NM,DIRECT_ZIP_DIR,DIRECT_ZIP_NM,CHEK_RESULT,CHEK_OPINION,crt_time FROM";
        sql += " zz_code_apply_history where 1=1 order by id desc";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }
}
