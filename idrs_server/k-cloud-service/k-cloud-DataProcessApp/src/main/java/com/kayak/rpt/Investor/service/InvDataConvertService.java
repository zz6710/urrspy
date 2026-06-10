package com.kayak.rpt.Investor.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.rpt.Investor.dao.InvDataConvertDao;
import com.kayak.rpt.Investor.model.InvIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "中债三期数据处理", model = InvIdentity.class)
public class InvDataConvertService {

    private static final Logger log = LoggerFactory.getLogger(InvDataConvertService.class);

    @Resource
    private InvDataConvertDao invDataConvertDao;

    /**
     * 查询投资者信息集合
     * @param params
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> getInvDataMap(Map<String, Object> params) throws Exception {

        return invDataConvertDao.getInvIdentityList(params);
    }

    /**
     * 更新投资者生僻字
     * @param params
     * @throws Exception
     */
    public void doUpdateInvName(Map<String, String> params) throws Exception {
        invDataConvertDao.doUpdateInvName(params);
    }

}
