package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.Announce;
import com.kayak.system.model.AnnounceInfo;
import com.kayak.system.model.AnnounceRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnounceDao extends ComnDao {

    public SqlResult<Announce> find(SqlParam<Announce> params) throws Exception {
        return super.findRows("SELECT a.*,u.username as createuser_name FROM sys_announce a" +
                " LEFT JOIN sys_user u ON u.userid = a.createuserid ", params);
    }

    public Announce get(String annid) throws Exception {
        return super.findRow(Announce.class, "SELECT * FROM sys_announce a WHERE annid = $S{annid}",0, annid);
    }

    public SqlResult<AnnounceInfo> show(SqlParam<AnnounceInfo> params) throws Exception {
        return super.findRows("SELECT a.* FROM sys_announce a " +
                " JOIN sys_announce_role r ON a.annid = r.annid " +
                " WHERE r.roleid = $S{roleid} " +
                " AND effective_date <= $S{effectiveDate} " +
                " AND invalid_date >= $S{invalidDate}", params);
    }

    public void add(SqlParam<AnnounceInfo> param) throws Exception {
        doTrans(() -> {
            AnnounceInfo model = param.getModel();
            String id = super.update(
                    "INSERT INTO sys_announce (annid, title, content, createdate, createtime" +
                            ", createuserid, editdate, edittime, edituserid, annfilepath" +
                            ", annfilename, annfilecode, effective_date, invalid_date, orgno) " +
                            " VALUES ($AUTOIDS{annid}, $S{title}, $S{content}, $S{createdate}, $S{createtime}" +
                            ", $S{createuserid}, $S{editdate}, $S{edittime}, $S{edituserid}" +
                            ", $S{annfilepath}, $S{annfilename}, $S{annfilecode}, $S{effectiveDate}, $S{invalidDate}" +
                            ", $S{orgno})", model).getAutoId();
            List<String> roleIds = model.getRoleIds();

            bulkInsertAnnounceRole(id, roleIds);
        });
    }


    public void edit(SqlParam<AnnounceInfo> param) throws Exception {
        doTrans(() -> {
            AnnounceInfo model = param.getModel();
            super.update(
                    "UPDATE sys_announce SET title = $S{title}, content = $S{content}, editdate = $S{editdate}" +
                            ", edittime = $S{edittime}, edituserid = $S{edituserid}, annfilepath = $S{annfilepath}" +
                            ", annfilename = $S{annfilename}, annfilecode = $S{annfilecode}" +
                            ", effective_date = $S{effectiveDate}, invalid_date = $S{invalidDate}" +
                            ", orgno = $S{orgno} WHERE annid = $S{annid}", model);

            super.update(
                    "DELETE FROM sys_announce_role WHERE annid = $S{annid}", model.getAnnid());

            bulkInsertAnnounceRole(model.getAnnid(), model.getRoleIds());
        });
    }

    public int delete(String annid) throws Exception {
        return super.update("DELETE FROM sys_announce WHERE annid = $S{annid}", annid).getEffect();
    }

    /**
     * 批量插入公告-角色关系表
     * @param annid
     * @param roleIds
     * @throws Exception
     */
    private void bulkInsertAnnounceRole(String annid, List<String> roleIds) throws Exception {
        if (roleIds == null || roleIds.size() == 0) {
            return;
        }

        for (String roleId : roleIds) {
            super.update("INSERT INTO sys_announce_role (annid, roleid) " +
                    " VALUES ($S{annid}, $S{roleid}); ", new AnnounceRole(annid, roleId));
        }
    }
}
