package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.system.model.Org;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrgDao extends ComnDao {

    public int delete(SqlParam<Org> params) throws Exception {
        String orgId = params.getModel().getOrgid();
        if (Tools.isBlank(orgId)) {
            return 0;
        }
        return super.update("DELETE FROM sys_org WHERE  orgid = '" + orgId + "'").getEffect();
    }

    public void update(SqlParam<Org> params) throws Exception {
        Org newOrg = params.getModel();
        String oldOrgId = newOrg.getOrgid();
        List<Org> orgs = super.findRows(Org.class, "SELECT * FROM sys_org WHERE orgid like '" + oldOrgId + "%'",
                0, null);
        if (CollectionUtils.isEmpty(orgs)) {
            return;
        }

        // 1、判断parentno是否为自己当前的子机构；拆分父机构和子机构
        List<Org> childOrg = new ArrayList<>();
        Org oldOrg = null;
        for (Org org : orgs) {
            if (org.getOrgid().equals(newOrg.getOrgid())) {
                oldOrg = org;
            } else {
                if (org.getOrgno().equals(newOrg.getParentorgno())) {
                    throw new PromptException("不能将机构移动到自己的子机构下");
                }
                childOrg.add(org);
            }
        }
        Org finalCurrentOrg = oldOrg;
        doTrans(() -> {
            // 2、保存新的Org
            super.update("UPDATE sys_org SET " +
                    " parentorgno = $S{parentorgno}, org_status = $S{orgStatus}, " +
                    " orgname = $S{orgname}, orglevel = $S{orglevel}, " +
                    " orgtype = $S{orgtype}," +
                    " address = $S{address}, telno = $S{telno}, orgno = $S{orgno} " +
                    " WHERE orgid = $S{orgid}", newOrg);
            if (logoChange(newOrg, finalCurrentOrg)) {
                // 3、计算新的orgid
                Org newParentOrg = this.get(newOrg.getParentorgno());
                String newParentOrgOrgid = newParentOrg.getOrgid();
                String newOrgId = newParentOrgOrgid + newOrg.getOrgno() + "_";

                // 4、子机构id及parentno替换
                for (Org org : childOrg) {
                    String orgId = org.getOrgid();
                    org.setOrgid(orgId.replaceFirst(oldOrgId, newOrgId));
                    org.setParentorgno(newOrg.getOrgno());
                    super.update("UPDATE sys_org " +
                            " SET orgid = $S{orgid},parentorgno = $S{parentorgno} " +
                            " WHERE orgno = $S{orgno}", org);
                }

                newOrg.setOrgid(newOrgId);
                super.update("UPDATE sys_org " +
                        " SET orgid = $S{orgid},parentorgno = $S{parentorgno} " +
                        " WHERE orgno = $S{orgno}", newOrg);
            }
        });
    }

    private boolean logoChange(Org newOrg, Org oldOrg) {
        return !newOrg.getParentorgno().equals(oldOrg.getParentorgno()) ||
                !newOrg.getOrgno().equals(oldOrg.getOrgno());
    }

    public SqlResult<Org> find(SqlParam<Org> params) throws Exception {
        return super.findRows("SELECT * FROM sys_org", params);
    }

    public SqlResult<Org> findChildren(SqlParam<Org> params) throws Exception {
        String orgId = params.getModel().getOrgid();
        if (Tools.isBlank(orgId)) {
            return null;
        }
        return super.findRows("SELECT * FROM sys_org WHERE orgid like'" + orgId + "%'", params);
    }

    public SqlResult<Org> findChildren2(SqlParam<Org> params) throws Exception {
        return super.findRows("SELECT * FROM sys_org WHERE parentorgno= $S{orgno}", params);
    }

    public SqlResult<Org> findOrg(SqlParam<Org> params) throws Exception {
        return super.findRows("SELECT * FROM sys_org order by orgid asc limit 1", params);
    }

    public Org get(String orgno) throws Exception {
        Map<String, Object> mapParams = new HashMap<>(1);
        mapParams.put("orgno", orgno);

        FetcherData<Org> params = new FetcherData<>(mapParams, Org.class);
        params.setMakeSql(true);
        SqlResult<Org> orgSqlResult = this.find(params);
        List<Org> rows = orgSqlResult.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return null;
        } else {
            return rows.get(0);
        }
    }

    public int add(SqlParam<Org> params) throws Exception {
        return super.update(
                "INSERT INTO sys_org " +
                        "(orgid, orgno, orgname, orglevel, " +
                        "orgtype, parentorgno, address, contect, telno, org_status) " +
                        " VALUES " +
                        "($S{orgid},$S{orgno},$S{orgname},$S{orglevel}," +
                        "$S{orgtype},$S{parentorgno},$S{address},$S{contect},$S{telno},$S{orgStatus})",
                params.getModel()).getEffect();
    }

    public SqlResult<Org> isBlankOrglevel(SqlParam<Org> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("select orgid from sys_org where  orgno = $S{orgno}",params);
    }
    public SqlResult<Org> queryOrgWL(SqlParam<Org> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("select orgid from sys_org where  orglevel = $S{orglevel} ",params);
    }

    public SqlResult<Org> findWL(SqlParam<Org> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("SELECT * FROM sys_org where orglevel < $S{orglevel}", params);
    }

    public SqlResult<Org> checkIsFather(SqlParam<Org> params) throws Exception {
        params.setMakeSql(false);
        String orgId = params.getModel().getOrgid();
        if (Tools.isBlank(orgId)) {
            return null;
        }
        return super.findRows("SELECT * FROM sys_org WHERE PARENTORGNO = $S{orgno}", params);
    }
}
