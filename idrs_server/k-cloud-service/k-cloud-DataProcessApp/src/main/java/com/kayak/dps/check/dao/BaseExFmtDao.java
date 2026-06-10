package com.kayak.dps.check.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.utils.Tools;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.check.model.BaseExFmtModel;
import com.kayak.dps.check.model.T8PortInfoModel;
import org.springframework.stereotype.Repository;

@Repository
public class BaseExFmtDao extends ComnDao {
    /**
     * 查询信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<BaseExFmtModel> findBaseExFmts(SqlParam<BaseExFmtModel> params) throws Exception {
        String sql = " select EXFMTID,ITMNM,ITMMEM,ITMPRC,ITMSCL,FLD,FLDPK,SN,ID,ITMTP,ITMDSC,ITMDIC,ITMUP from base_ex_fmt p " +
                " where 1 = 1 ";

        if(Tools.strIsNotEmpty(params.getModel().getId())){
            sql += "  and p.id = $S{id} " ;
        }if(Tools.strIsNotEmpty(params.getModel().getExfmtid()))
        sql += "  and  p.EXFMTID = $S{exfmtid}" ;
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }
    /**
     * 更新
     * @param params
     * @return
     * @throws Exception
     */
    public int upBaseExFmt(SqlParam<BaseExFmtModel> params) throws Exception {

        return super.update(" update  base_ex_fmt set exfmtid=$S{exfmtid}, itmnm=$S{itmnm}, itmmem=$S{itmmem}, itmprc=$S{itmprc}, itmscl=$S{itmscl}, fld=$S{fld}, fldpk=$S{fldpk}, sn=$S{sn}, itmtp=$S{itmtp}, itmdsc=$S{itmdsc}, itmdic=$S{itmdic}, itmup=$S{itmup} where id=$S{id}", DataSourceProperty.PUB, params.getParams()).getEffect();
    }
    /**
     * 删除
     * @param params
     * @return
     * @throws Exception
     */
    public int delBaseExFmt(SqlParam<BaseExFmtModel> params) throws Exception {
        String sql = " delete from base_ex_fmt p " +
                " where   p.id = $S{id}  ";

        return   super.update(sql, DataSourceProperty.PUB, params.getParams()).getEffect();

    }/**
     * 新增
     * @param params
     * @return
     * @throws Exception
     */
    public int  addBaseExFmt(SqlParam<BaseExFmtModel> params) throws Exception {
        String sql = " insert into  base_ex_fmt (exfmtid, itmnm, itmmem, itmprc, itmscl, fld, fldpk, sn,   itmtp, itmdsc, itmdic, itmup) values($S{exfmtid},$S{itmnm}, $S{itmmem}, $S{itmprc}, $S{itmscl},$S{fld},$S{fldpk},$S{sn},   $S{itmtp}, $S{itmdsc}, $S{itmdic}, $S{itmup})";
        return    super.update(sql, DataSourceProperty.PUB, params.getParams()).getEffect();

    }

}
