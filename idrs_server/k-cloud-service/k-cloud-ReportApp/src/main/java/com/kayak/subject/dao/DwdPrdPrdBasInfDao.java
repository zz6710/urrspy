package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.exception.PromptException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DwdPrdPrdBasInfDao extends ComnDao {

    // 获取母产品人行代码
    public String findPrdcCdPbc(String motherFundCode) throws Exception {
        String sql = "SELECT PBC_CD FROM dwd_prd_prd_bas_inf where PROD_CD=$S{motherFundCode}";
        List<String> list = super.findRows(String.class, sql, DataSourceProperty.PUB, motherFundCode);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        throw new PromptException("未能获取到人行代码，内部产品代码为：" + motherFundCode);
    }
}
