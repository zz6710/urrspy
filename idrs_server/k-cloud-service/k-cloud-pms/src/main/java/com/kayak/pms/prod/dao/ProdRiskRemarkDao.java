package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.prod.model.ProdRiskRemark;
import org.springframework.stereotype.Repository;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/9/1 16:41
 */
@Repository
public class ProdRiskRemarkDao extends ComnDao {
    public void saveRemark(ProdRiskRemark prodRiskRemark) throws Exception {
        /*存在则更新，不存在则插入*/
        super.update("INSERT INTO t8_risk_remark (t8_prod_info_id, risk_remark, inputuser, crt_date, crt_time, upt_date,\n" +
                "                                         upt_time)\n" +
                "VALUES ($S{t8ProdInfoId}, $S{riskRemark}, $S{inputuser}, $S{crtDate}, $S{crtTime}, $S{uptDate}, $S{uptTime}) " +
                "ON DUPLICATE KEY UPDATE risk_remark = $S{riskRemark},upt_date = $S{uptDate},upt_time = $S{uptTime},inputuser=$S{inputuser}", prodRiskRemark);
    }

    public SqlResult<ProdRiskRemark> queryRemark(SqlParam<ProdRiskRemark> param) throws Exception {
        return super.findRows("select risk_remark from t8_risk_remark where t8_prod_info_id = $S{t8ProdInfoId}", param);
    }
}
