package com.kayak.pms.excel.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.excel.model.Excel;
import com.kayak.pms.excel.model.TradeField;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ExcelDao  extends ComnDao {

    public SqlResult<Excel> findExcel(SqlParam<Excel> params) throws Exception {
        return super.findRows("SELECT * FROM sys_exlimp", params);
    }

    public SqlResult<TradeField> findTradeField(SqlParam<TradeField> params) throws Exception {
        return super.findRows("SELECT * FROM SYS_EXLIMP_DETAIL", params);
    }

    public List<Excel> findExcelId(Integer excelId) throws Exception {
        return super.findRows(Excel.class, "SELECT * FROM sys_exlimp WHERE id = '" + excelId + "'",
                0, null);
    }

    public List<TradeField> findTradeFieldId(Integer excelId ) throws Exception {
        return super.findRows(TradeField.class, "SELECT * FROM SYS_EXLIMP_DETAIL WHERE sys_exlimp_id = '" + excelId + "'",
                0, null);
    }


    /**
     *
     * @param map Map
     * @return
     */

    //保存文件
    public void saveFileData(Map<String ,Object> map) throws Exception {
        String str = "insert into sys_import_file (id, sys_exlimp_id, file_name, file_path, crt_date) " +
                "values ($AUTOIDS{id}, $S{sysExlimpId}, $S{fileName}, $S{filePath}, $S{crtDate})";
        super.update(str, map);
    }

    public void saveParseData (String sql ,  int sharding, List<Map<String,Object>> l ) throws Exception {

        for (Map<String,Object> m : l) {
            m.put("crt_date" , DateUtil.getNowDate());
            m.put("crt_time" , DateUtil.getNowTime());
            m.put("crt_user" , Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            super.update(sql, sharding, m);
        }

    }


}
