package com.kayak.pms.T8ProdDeal.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.T81.dao.T8ProdInfoDao;
import com.kayak.pms.T81.model.T8ProdInfo;
import com.kayak.pms.T8ProdDeal.dao.T8ProdSalesInfoDao;
import com.kayak.pms.T8ProdDeal.model.T8ProdSalesInfo;
import com.kayak.utils.DateHelper;
import com.kayak.pms.prod.dao.T8ProdScheduleDao;
import com.kayak.pms.prod.model.T8ProdSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: k-cloud
 * @description: 产品份额服务
 * @author: axin
 * @create: 2021-03-16 16:23
 * @memo 备注信息
 */
@Service
@APIDefine(desc = "产品份额服务",model = T8ProdSalesInfo.class)
public class T8ProdSalesInfoService {
    private static final Logger logger = LoggerFactory.getLogger(T8ProdSalesInfoService.class);

    @Autowired
    private T8ProdSalesInfoDao t8ProdSalesInfoDao;

    @Autowired
    private T8ProdInfoDao t8ProdInfoDao;

    @Autowired
    private T8ProdScheduleDao t8ProdScheduleDao;

    @API(desc = "查询产品份额", auth = APIAuth.YES)
    public SqlResult<T8ProdSalesInfo> findT8ProdSalesInfos1(SqlParam<T8ProdSalesInfo> params) throws Exception {
        return findT8ProdSalesInfos(params);
    }

    @API(desc = "查询产品份额", auth = APIAuth.NO)
    public SqlResult<T8ProdSalesInfo> findT8ProdSalesInfos(SqlParam<T8ProdSalesInfo> params) throws Exception {
        return t8ProdSalesInfoDao.findT8ProdSalesInfos(params);
    }

    @API(desc = "添加产品份额",  auth = APIAuth.YES,operation = APIOperation.INSTER)
    public int addT8ProdSalesInfo(SqlParam<T8ProdSalesInfo> params) throws Exception {
        String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
        String time = DateHelper.getCurrentTime();
        String date = DateHelper.getCurrentDate();
        params.getModel().setCrtUser(userId);
        params.getModel().setCrtDate(date);
        params.getModel().setCrtTime(time);
        params.getModel().setUpdUser(userId);
        params.getModel().setUpdDate(date);
        params.getModel().setUpdTime(time);
        T8ProdInfo prodInfo = new T8ProdInfo();
        prodInfo.setProdStatus("6");
        prodInfo.setProdSonStatus("14");
        prodInfo.setProdCode(params.getModel().getProdCode());
        AtomicReference<Integer> count = new AtomicReference<>(0);
        DaoUtil.doTrans(() -> {
            //修改产品状态
            t8ProdInfoDao.updateT8ProdInfoStatus(prodInfo);
            count.set(t8ProdSalesInfoDao.addT8ProdSalesInfo(params).getEffect());
            /*修改产品排期份额与状态 add by 张昌思 20210422*/
            T8ProdSchedule t8ProdSchedule = new T8ProdSchedule();
            t8ProdSchedule.setProdCode(params.getModel().getProdCode());
            t8ProdSchedule.setCurrentProgress(prodInfo.getProdSonStatus());
            t8ProdScheduleDao.updateScheduleBySales(t8ProdSchedule);
        });

        return count.get();
    }

    @API(desc = "修改产品份额", auth = APIAuth.YES,operation = APIOperation.INSTER)
    public int updateT8ProdSalesInfo(SqlParam<T8ProdSalesInfo> params) throws Exception {
        String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
        String time = DateHelper.getCurrentTime();
        String date = DateHelper.getCurrentDate();
        params.getModel().setUpdUser(userId);
        params.getModel().setUpdDate(date);
        params.getModel().setUpdTime(time);
        return t8ProdSalesInfoDao.updateT8ProdSalesInfo(params).getEffect();
    }

    @API(desc = "删除产品份额",  auth = APIAuth.YES,operation = APIOperation.INSTER)
    public int deleteT8ProdSalesInfo(SqlParam<T8ProdSalesInfo> params) throws Exception {
        return t8ProdSalesInfoDao.deleteT8ProdSalesInfo(params).getEffect();
    }

    @API(desc = "添加销售商份额",  auth = APIAuth.YES,operation = APIOperation.INSTER)
    public int addT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfo> params)throws Exception{
    	
    	String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
		String time = DateHelper.getCurrentTime();
		String date = DateHelper.getCurrentDate();
		params.getModel().setCrtUser(userId);
		params.getModel().setCrtDate(date);
		params.getModel().setCrtTime(time);
		params.getModel().setUpdUser(userId);
		params.getModel().setUpdDate(date);
		params.getModel().setUpdTime(time);
		return t8ProdSalesInfoDao.addT8ProdSalesInfoDistributor(params).getEffect();
        //此方法只作用于权限控制

    }

    @API(desc = "修改销售商份额",  auth = APIAuth.YES,operation = APIOperation.INSTER)
    public int updateT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfo> params)throws Exception{
    	String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
		String time = DateHelper.getCurrentTime();
		String date = DateHelper.getCurrentDate();
		params.getModel().setUpdUser(userId);
		params.getModel().setUpdDate(date);
		params.getModel().setUpdTime(time);
		return t8ProdSalesInfoDao.updateT8ProdSalesInfoDistributor(params).getEffect();

    }

    @API(desc = "删除销售商份额",  auth = APIAuth.YES,operation = APIOperation.INSTER)
    public int deleteT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfo> params)throws Exception{
    	return t8ProdSalesInfoDao.deleteT8ProdSalesInfoDistributor(params).getEffect();
    }
}
