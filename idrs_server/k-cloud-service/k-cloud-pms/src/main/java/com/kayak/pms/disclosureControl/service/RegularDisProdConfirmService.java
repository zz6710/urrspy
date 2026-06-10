package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.T81.dao.T8ProdUserDao;
import com.kayak.pms.T81.model.T8ProdUser;
import com.kayak.pms.disclosureControl.dao.DisclosureOperationDao;
import com.kayak.pms.disclosureControl.dao.RegularDisProdConfirmDao;
import com.kayak.pms.disclosureControl.disclousreEnum.DisclosureTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.OperationTypeEnum;
import com.kayak.pms.disclosureControl.model.DisclosureOperation;
import com.kayak.pms.disclosureControl.model.RegularDisProdConfirm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * com.kayak.pms.disclosureControl.service
 * user:rennannan
 * date:2021/5/20 17:07
 * function:定期报告产品披露确认服务
 */
@Service
@APIDefine(model = RegularDisProdConfirm.class)
public class RegularDisProdConfirmService {
    @Autowired
    private RegularDisProdConfirmDao regularDisProdConfirmDao;
    @Autowired
    private DisclosureOperationDao disclosureOperationDao;
    @Autowired
    private T8ProdUserDao t8ProdUserDao;


    @API(desc = "查询定期报告产品披露确认", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public SqlResult<RegularDisProdConfirm> findRegularDisProdConfirmAuth(SqlParam<RegularDisProdConfirm> param) throws Exception {
        return findRegularDisProdConfirm(param);
    }

    @API(desc = "查询定期报告产品披露确认", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<RegularDisProdConfirm> findRegularDisProdConfirm(SqlParam<RegularDisProdConfirm> param) throws Exception {
        //param.setMakeSql(true);
        SqlResult<RegularDisProdConfirm> result = regularDisProdConfirmDao.findRegularDisProdConfirm(param);
        return result;
    }

    @API(desc = "投资确认", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public int investConfirm(SqlParam<RegularDisProdConfirm> param) throws Exception {
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
        param.getModel().setInvestApprovalDate(date);
        param.getModel().setInvestApprovalTime(time);
        param.getModel().setInvestApprovalUserId(userid);
        param.getModel().setInvestApprovalUserName(username);
        String prodId = param.getModel().getT8ProdInfoId();
        //修改本条数据对应待办状态为已办结
        DisclosureOperation operation = new DisclosureOperation();
        operation.setDealId(param.getModel().getDisProdTaskId());
        operation.setUserid(userid);
        operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
        operation.setOperationType(OperationTypeEnum.ONE.getVal());
        operation.setStatus("1");
        operation.setEndDate(date);
        operation.setEndTime(time);
        DaoUtil.doTrans(() -> {
            disclosureOperationDao.updateOperationStatus(operation);
            if (StringUtils.isEmpty(param.getModel().getDisclosureApprovalResult())) {//信披经理还未选择是否披露才产生待办
                DisclosureOperation ope = new DisclosureOperation();
                ope.setDealId(param.getModel().getDisProdTaskId());
                ope.setCrtDate(date);
                ope.setCrtTime(time);
                ope.setCrtUserId(userid);
                ope.setCrtUserName(username);
                ope.setProdCode(prodId);
                deleteAndAddOperation(ope, prodId);
            }
            this.regularDisProdConfirmDao.updateRegularDisProdConfirm(param.getModel());
        });
        return 1;
    }

    public void deleteAndAddOperation(DisclosureOperation ope, String prodId) throws Exception {
        //插入待办
        ope.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
        ope.setStatus("0");
        ope.setDealTable("idb_disclosure_prod_task");
        ope.setOperationType(OperationTypeEnum.ELEVEN.getVal());
        ope.setRoleid("8");
        //先删除后插入
        disclosureOperationDao.deleteByTypeAndDealId(ope);
        //查询信披经理
        List<T8ProdUser> prodUsers = t8ProdUserDao.getProdUserByProdId(prodId);
        if (prodUsers.size() > 0 && prodUsers != null) {
            for (T8ProdUser user : prodUsers) {
                if (user.getRoleId().equals("8")) {
                    ope.setUserid(user.getUseridA());
                    break;
                }
            }
        }
        disclosureOperationDao.insertDisOperation(ope);
    }

    @API(desc = "信披确认", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public int disclosureConfirm(SqlParam<RegularDisProdConfirm> param) throws Exception {
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
        param.getModel().setDisclosureApprovalDate(date);
        param.getModel().setDisclosureApprovalTime(time);
        param.getModel().setDisclosureUserId(userid);
        param.getModel().setDisclosureUserName(username);
        //修改本条数据对应待办状态为已办结
        DisclosureOperation operation = new DisclosureOperation();
        operation.setDealId(param.getModel().getDisProdTaskId());
        operation.setUserid(userid);
        operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
        operation.setOperationType(OperationTypeEnum.ELEVEN.getVal());
        operation.setStatus("1");
        operation.setEndDate(date);
        operation.setEndTime(time);
        DaoUtil.doTrans(() -> {
            disclosureOperationDao.updateOperationStatus(operation);
            this.regularDisProdConfirmDao.updateRegularDisProdConfirm(param.getModel());
        });
        return 1;
    }
    /**
     * 定期报告台账-导出权限控制
     * @return
     */
    @API(desc = "导出",auth = APIAuth.YES)
    public String exportRightControl() {
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

}
