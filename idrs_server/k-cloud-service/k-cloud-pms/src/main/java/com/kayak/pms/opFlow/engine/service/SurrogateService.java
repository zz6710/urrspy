package com.kayak.pms.opFlow.engine.service;

import com.kayak.helper.StringHelper;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.dao.SurrogateDao;
import com.kayak.pms.opFlow.engine.entity.Surrogate;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 17/04/2017.
 */
@Service
@Transactional
public class SurrogateService {
    public static final String ENABLE = "1";
    public static final String DISENABLE = "0";

    @Autowired
    SurrogateDao surrogateDao;

    public void save(Surrogate surrogate) {
        surrogate.setId(StringHelper.getPrimaryKey());
        surrogate.setCreateDate(DateHelper.getCurrentDate());
        surrogate.setCreateTime(DateHelper.getCurrentTime());
        surrogateDao.save(surrogate);
    }

    public void remove(String id) {
        surrogateDao.remove(id);
    }

    public void update(Surrogate surrogate) {
        surrogate.setUpdateDate(DateHelper.getCurrentDate());
        surrogate.setUpdateTime(DateHelper.getCurrentTime());
        surrogateDao.update(surrogate);
    }

    public Surrogate get(String id) {
        return surrogateDao.get(id);
    }

    public BootstrapTableVO<Surrogate> list(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<Surrogate> allSurrogate = surrogateDao.list(queryCriteria);
//        PageInfo page = new PageInfo<Surrogate>(allSurrogate);
        return new BootstrapTableVO<>(allSurrogate, allSurrogate.size());
    }

    public String getUserIdByUser(String userName) {
        return surrogateDao.getUserIdByUser(userName);
    }

}
