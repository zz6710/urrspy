package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.dps.app.model.AssT8OrgSheetModel;
import org.springframework.stereotype.Repository;

@Repository
public class AssT8OrgSheetModelDao extends ComnDao {

    public void deleteT8OrgSheetModel(SqlParam<AssT8OrgSheetModel> params) throws Exception {
        String sql = " delete from ods_supply_org_bas_inf where ORG_NBR = $S{orgNbr}";
        super.update(sql, DataSourceProperty.PUB, params.getModel());
    }

    public void addT8OrgSheetModel(SqlParam<AssT8OrgSheetModel> params) throws Exception {
        String sql = "update ods_supply_org_bas_inf t \n" +
                "set \n" +
                "t.ORG_TYP = $S{orgTyp},/**机构类型*/\n" +
                "t.SAM_BUS_ORG_TYP = $S{samBusOrgTyp},/**同业机构类型*/\n" +
                "t.UPD_DT = date_format(CURDATE(),'%Y%m%d'),\n" +
                "t.version = t.version + 1\n" +
                "where \n" +
                " ORG_NBR_EXT = $S{orgNbr}";
        super.update(sql, DataSourceProperty.PUB,params.getModel());
    }
}
