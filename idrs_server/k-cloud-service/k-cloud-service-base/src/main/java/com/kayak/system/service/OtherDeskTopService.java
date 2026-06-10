package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.system.dao.DeskTopDao;
import com.kayak.system.model.DeskTopModel;
import com.kayak.system.model.OtherDeskTopModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
@APIDefine(desc = "首页管理服务", model = OtherDeskTopModel.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OtherDeskTopService {

    @Resource
    private DeskTopDao deskTopDao;


    @API(desc = "其他待办事项", auth = APIAuth.YES)
    public SqlResult<OtherDeskTopModel> findOtherTopInfo(SqlParam<OtherDeskTopModel> params) throws Exception {

            return deskTopDao.findOtherTopInfo(params);
    }
    @API(desc = "其他待办事项", auth = APIAuth.YES)
    public SqlResult<OtherDeskTopModel> findOtherTopInfoAll(SqlParam<OtherDeskTopModel> params) throws Exception {

        return deskTopDao.findOtherTopInfoAll(params);
    }


    @API(desc = "其他待办事项", auth = APIAuth.YES)
    public String confirmTopSend(SqlParam<OtherDeskTopModel> params) throws Exception {

        try {
            params.getModel().setCrtDate(DateUtil.getNowDate());
            params.getModel().setCrtTime(DateUtil.getNowTime());
            deskTopDao.updateOtherTopInfoStatus(params);
            return RequestSupport.updateReturnJson(true, "确认成功！", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }

    }


}
