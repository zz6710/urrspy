package com.kayak.pms.disclosureControl.service;

import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeProcessDao;
import com.kayak.pms.disclosureControl.dao.DisclosureOperationDao;
import com.kayak.pms.disclosureControl.disclousreEnum.DisclosureTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.OperationTypeEnum;
import com.kayak.pms.disclosureControl.model.DisclosureNotice;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeProcess;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersion;
import com.kayak.pms.disclosureControl.model.DisclosureOperation;
import com.kayak.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@APIDefine(desc = "公告数据补录进程服务", model = DisclosureNoticeProcess.class)
public class DisclosureNoticeProcessService {

    @Autowired
    private DisclosureNoticeProcessDao disNoticeProcessDao;
    @Autowired
    private DisclosureOperationDao disclosureOperationDao;
    @Autowired
    private DisclosureNoticeDao disclosureNoticeDao;

    @API(desc = "查询公告数据补录进程信息", auth = APIAuth.NO)
    public SqlResult<DisclosureNoticeProcess> findDisNoticeProcess(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        params.setMakeSql(true);
        return disNoticeProcessDao.findDisNoticeProcesss(params);
    }

    @API(desc = "查询公告版本", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public String findMaxVersions(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        DisclosureNoticeVersion disclosureNoticeVersion = new DisclosureNoticeVersion();
        disclosureNoticeVersion.setT8DisclosureNoticeId(params.getModel().getT8DisclosureNoticeId());
        Map map = disNoticeProcessDao.findMaxVersions(disclosureNoticeVersion);
        return RequestSupport.updateReturnJson(true, "操作成功", map).toString();
    }

    @API(desc = "根据id查询公告版本", auth = APIAuth.NO, operation = APIOperation.SELECT)//根据公告id查询公告最大版本号
    public String findMaxVersionById(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        DisclosureNoticeVersion disclosureNoticeVersion = new DisclosureNoticeVersion();
        disclosureNoticeVersion.setId(params.getModel().getId());
        disclosureNoticeVersion.setT8DisclosureNoticeId(params.getModel().getT8DisclosureNoticeId());
        Map<String, Object> map =  disNoticeProcessDao.findMaxVersionById(disclosureNoticeVersion);
        return RequestSupport.updateReturnJson(true, "操作成功", map).toString();
    }

    @API(desc = "添加公告数据补录进程", auth = APIAuth.NO)
    public int addDisNoticeProcess(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        return disNoticeProcessDao.addDisNoticeProcess(params).getEffect();

    }

    @API(desc = "补录分发", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public int updateDisNoticeProcess(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        String jsonData = params.getModel().getJsonData();
        String prodCode = params.getModel().getProdCode();
        List<DisclosureNoticeProcess> list;
        list = JSONObject.parseArray(jsonData, DisclosureNoticeProcess.class);
        DaoUtil.doTrans(() -> {
            String t8DisclosureNoticeId = "";
            String date = DateHelper.getCurrentDate();
            String time = DateHelper.getCurrentTime();
            String currentUserId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
            DisclosureOperation disclosureOperation = new DisclosureOperation();
            disclosureOperation.setOperationType(OperationTypeEnum.TWO.getVal());
            if (list.size() > 0) {
                t8DisclosureNoticeId = list.get(0).getT8DisclosureNoticeId();
                //删除当前公告待办类型为“发起审批”的待办
                DisclosureOperation delOperation = new DisclosureOperation();
                delOperation.setOperationType(OperationTypeEnum.SEVEN.getVal());
                delOperation.setDealId(t8DisclosureNoticeId);
                delOperation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
                disclosureOperationDao.deleteDisclosureOperation(delOperation);

                //删除当前公告待办类型为“发起托管行审核”的待办
                delOperation.setOperationType(OperationTypeEnum.EIGHT.getVal());
                disclosureOperationDao.deleteDisclosureOperation(delOperation);

                //删除 6-托管拒绝后分发 与 5-补录分发 与 3-审批拒绝分发的待办
                delOperation.setOperationType(OperationTypeEnum.THREE.getVal());
                disclosureOperationDao.deleteDisclosureOperation(delOperation);

                delOperation.setOperationType(OperationTypeEnum.FIVE.getVal());
                disclosureOperationDao.deleteDisclosureOperation(delOperation);

                delOperation.setOperationType(OperationTypeEnum.SIX.getVal());
                disclosureOperationDao.deleteDisclosureOperation(delOperation);


                List<String> roleIds = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    /*修改公告录入进程表*/
                    //只要有一个角色进行补录，则把所有拥有这个角色的用户补录状态改为0 未补录
                    DisclosureNoticeProcess disclosureNoticeProcess = new DisclosureNoticeProcess();
                    disclosureNoticeProcess.setT8DisclosureNoticeId(list.get(i).getT8DisclosureNoticeId());
                    disclosureNoticeProcess.setRoleId(list.get(i).getRoleId());
                    disclosureNoticeProcess.setInputStatus("0");
                    disclosureNoticeProcess.setUpdDate(date);
                    disclosureNoticeProcess.setUpdTime(time);
                    disclosureNoticeProcess.setUpdUserId(currentUserId);
                    disclosureNoticeDao.updateDisNoticeProcessByRoleIds1(disclosureNoticeProcess);

                    DisclosureNoticeProcess process = list.get(i);
                    process.setInputStatus("0");
                    roleIds.add(list.get(i).getRoleId());
                    disNoticeProcessDao.updateProcessInfo(process);

                    //用户id集合，保存用户id
                    String userId = list.get(i).getUserId();
                    String toUserId = list.get(i).getToUserId();
                    //定义首页待办对象
                    DisclosureOperation operation = new DisclosureOperation();
                    operation.setDealId(t8DisclosureNoticeId);
                    operation.setDealTable("idb_disclosure_notice");
                    operation.setProdCode(prodCode);
                    operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
                    operation.setStatus("0");
                    operation.setCrtDate(date);
                    operation.setCrtTime(time);
                    operation.setCrtUserId(currentUserId);
                    operation.setOperationType(OperationTypeEnum.TWO.getVal());
                    operation.setRoleid(list.get(i).getRoleId());
                    //设置需要删除待办的用户id
                    operation.setUserid(userId);
                    try {
                        //删除当前已经存在的补录分发的待办
                        disclosureOperationDao.deleteOperationS(operation);
                        //插入
                        operation.setUserid(toUserId);//设置需要插入待办的用户id
                        disclosureOperationDao.insertDisOperation(operation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                //更新公告状态
                if (!CollectionUtils.isEmpty(roleIds)) {
                    String noticeId = list.get(0).getT8DisclosureNoticeId();
                    //根据公告id、补录状态(0未录入)查询process表中的数据
                    DisclosureNoticeProcess pQuery = new DisclosureNoticeProcess();
                    pQuery.setT8DisclosureNoticeId(noticeId);
                    List<DisclosureNoticeProcess> dbProcessList = this.disNoticeProcessDao.findProcessList(pQuery);
                    boolean existsInvestRole = false;//是否存在投资经理未补录
                    boolean existsAsseRole = false;//是否存在估值核算岗未补录
                    for (DisclosureNoticeProcess p : dbProcessList) {
                        if (p.getRoleId().equals("14")) {
                            existsInvestRole = true;
                            break;
                        }
                    }
                    for (DisclosureNoticeProcess p2 : dbProcessList) {
                        if (p2.getRoleId().equals("9")) {
                            existsAsseRole = true;
                            break;
                        }
                    }
                    String status = "8";
                    if (roleIds.contains("14") && roleIds.contains("9")) {//包含投资经理和估值核算岗  公告状态改为：8-数据数据未确认
                        status = "8";
                    } else if (roleIds.contains("14") && !roleIds.contains("9")) {//包含投资经理不包含估值核算岗  6估值已确认-投资未确认
                        //判断估值核算岗数据是否已经导入
                        SqlRow processCount = disNoticeProcessDao.findProcessCount(pQuery);
                        if (processCount.getInteger("count2") == 0) { //count2为0说明没有导入估值核算岗
                            status = "8";
                        } else {
                            //判断库中是否存在估值核算岗未补录的数据，存在则状态更改为8-数据数据未确认，否则6估值已确认-投资未确认
                            if (existsAsseRole) {
                                status = "8";
                            } else {
                                status = "6";
                            }
                        }
                    } else if (!roleIds.contains("14") && roleIds.contains("9")) {//不包含投资经理包含估值核算岗  7估值未确认-投资已确认
                        //判断库中是否投资经理岗未补录的数据，存在将状态更改为8-数据数据未确认，否则状态更新为7估值未确认-投资已确认
                        if (existsInvestRole) {
                            status = "8";
                        } else {
                            status = "7";
                        }
                    }
                    //更新公告状态
                    DisclosureNotice notice = new DisclosureNotice();
                    notice.setId(noticeId);
                    //notice.setCurrentStageStatus(status);
                    //notice.setStage("1");
                    disclosureNoticeDao.updateNoticeStatus(notice);
                }
            }
        });
        return 1;
    }

    @API(desc = "删除公告数据补录进程", auth = APIAuth.NO)
    public int deleteDisNoticeProcess(SqlParam<DisclosureNoticeProcess> params) throws Exception {
        return disNoticeProcessDao.deleteDisNoticeProcess(params).getEffect();
    }

}
