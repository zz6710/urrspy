package com.kayak.dps.check.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.utils.Tools;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.check.model.BaseExSeatModel;
import org.springframework.stereotype.Repository;

@Repository
public class BaseExSeatDao extends ComnDao {
    /**
     * 查询信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<BaseExSeatModel> findBaseExSeats(SqlParam<BaseExSeatModel> params) throws Exception {
        String sql = " select FCODE,EXTPID,EXMODE,FNMFMT,OHEADER,OITMNM,OITMNMFL,ORECCNT,OSYMBOL,OFLDDEF,EXTAB,EXFMTID,INDEXFILE,TCODE,id from base_ex_seat p " +
                " where 1 = 1 ";
        if(Tools.strIsNotEmpty(params.getModel().getId())){
            sql += "  and p.id = $S{id} " ;
        }
        sql += " order by  p.id" ;
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 更新
     * @param params
     * @return
     * @throws Exception
     */
    public int upBaseExSeat(SqlParam<BaseExSeatModel> params) throws Exception {
        return super.update("update base_ex_seat set fcode=$S{fcode}, extpid=$S{extpid}, exmode=$S{exmode}, fnmfmt=$S{fnmfmt},  oflddef=$S{oflddef}, extab=$S{extab}, exfmtid=$S{exfmtid}, tcode=$S{tcode} where id=$S{id}", DataSourceProperty.PUB, params.getParams()).getEffect();
    }
    /**
     * 删除
     * @param params
     * @return
     * @throws Exception
     */
    public void  delBaseExSeat(SqlParam<BaseExSeatModel> params) throws Exception {
        String sql = " delete from base_ex_seat p " +
                " where   p.id = $S{id}  ";
      super.update(sql, DataSourceProperty.PUB, params.getParams()).getEffect();

    }

    /**
     * 新增
     * @param params
     * @return
     * @throws Exception
     */
    public int addBaseExSeat(SqlParam<BaseExSeatModel> params) throws Exception {
        String sql = " insert into  base_ex_seat (fcode, extpid, exmode, fnmfmt, oflddef, extab, exfmtid, tcode) values($S{fcode},$S{extpid}, $S{exmode}, $S{fnmfmt},  $S{oflddef},  $S{extab},$S{exfmtid}, $S{tcode}) ";
       return super.update(sql, DataSourceProperty.PUB, params.getModel()).getEffect();
    }
}
