package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.dao.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ModelDataProcessDao extends ComnDao {

    @Autowired
    protected DaoService daoService;
    @Autowired
    protected ComnDao comnDao;

}
