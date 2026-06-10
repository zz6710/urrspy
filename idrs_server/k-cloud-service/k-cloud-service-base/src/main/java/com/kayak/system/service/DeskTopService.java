package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.system.dao.DeskTopDao;
import com.kayak.system.model.DeskTopModel;
import com.kayak.system.model.OtherDeskTopModel;
import com.spire.ms.System.Collections.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
@APIDefine(desc = "首页管理服务", model = DeskTopModel.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DeskTopService {

    @Resource
    private DeskTopDao deskTopDao;

    @API(desc = "接口接入情况", auth = APIAuth.YES)
    public SqlResult<DeskTopModel> findPortDeskTopInformation(SqlParam<DeskTopModel> params) throws Exception {
        return deskTopDao.findPortLogInfo(params);
    }

//    @API(desc = "其他待办事项", auth = APIAuth.YES)
//    public SqlResult<OtherDeskTopModel> findOtherTopInfo(SqlParam<OtherDeskTopModel> params) throws Exception {
//
//            return deskTopDao.findOtherTopInfo(params);
//    }
//    @API(desc = "其他待办事项", auth = APIAuth.YES)
//    public String confirmTopSend(SqlParam<DeskTopModel> params) throws Exception {
//
//
//        try {
//            params.getModel().setCrtDate(DateUtil.getNowDate());
//            params.getModel().setCrtTime(DateUtil.getNowTime());
//            deskTopDao.saveOtherTopInfoCode(params);
//            return RequestSupport.updateReturnJson(true, "确认成功！", null).toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
//        }
//
//    }
    @API(desc = "接口接入情况（失败）", auth = APIAuth.YES)
    public SqlResult<DeskTopModel> findPortDeskTopErrInformation(SqlParam<DeskTopModel> params) throws Exception {
        params.setMakeSql(false);
        SqlResult<DeskTopModel> portErrLogInfo = deskTopDao.findPortErrLogInfo(params);

        return deskTopDao.findPortErrLogInfo(params);
    }
    @API(desc = "信息披露情况", auth = APIAuth.YES)
    public SqlResult<DeskTopModel> findDisclosureNDetails(SqlParam<DeskTopModel> params) throws Exception {
        String nextPlanDate="";
        String planFbDate=params.getModel().getPlanFbDate();
        nextPlanDate = deskTopDao.getNextWorkday(planFbDate, "001").getString("workday");
        return deskTopDao.findDisclosureNDetails(params,planFbDate,nextPlanDate);
    }

    @API(desc = "监管报送情况", auth = APIAuth.YES)
    public SqlResult<DeskTopModel> findReportResultInfo(SqlParam<DeskTopModel> params) throws Exception {
        params.setMakeSql(false);
        String check = params.getModel().getCheckType();
        if("1".equals(check)){
            return deskTopDao.findReportResultInfo(params);
        }else if("2".equals(check)){
            return deskTopDao.findNextReportResultInfo(params);
        }else if("3".equals(check)){
            return deskTopDao.findTodayReportResultInfo(params);
        }else{
            return deskTopDao.findExpiryReportResultInfo(params);
        }
    }
    @API(desc = "首页报送提醒数据汇总", auth = APIAuth.YES)
    public SqlResult<DeskTopModel> findReportResultInfoNum(SqlParam<DeskTopModel> params) throws Exception {
        params.setMakeSql(false);
        return deskTopDao.findNextReportResultInfoNum(params);
    }

    @API(desc = "数据补录汇总", auth = APIAuth.YES)
    public SqlResult<DeskTopModel> findSubmitRemindNum(SqlParam<DeskTopModel> params) throws Exception {
        params.setMakeSql(false);
        return deskTopDao.findSubmitRemindNum(params);
    }

    @API(desc = "查询科技联系人信息", auth = APIAuth.NO)
    public SqlResult<DeskTopModel> findTechConnectInfo(SqlParam<DeskTopModel> params) throws Exception {
        String teckConnectTelStr =  SysUtil.getSystemParamsByParaid("teckConnectTel");
        List<DeskTopModel> list =new ArrayList();
        SqlResult<DeskTopModel> result = new SqlResult<>();
        DeskTopModel rs = new DeskTopModel();
        //赋值到一个字段，不会再向表中插入，此处仅做查询存储
        rs.setMsg(teckConnectTelStr);
        list.add(rs);
        result.setRows(list);
        return result;
    }
}
