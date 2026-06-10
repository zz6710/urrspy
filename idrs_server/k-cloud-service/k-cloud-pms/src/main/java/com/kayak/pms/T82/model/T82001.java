package com.kayak.pms.T82.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t82001Service", table = "T8_DISTRIBUTOR_INFO")
public class T82001 {

    @GraphQLField(label = "销售商id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "销售商代码", kkhtmlDefault = true, sql = " distributor_code LIKE '%$U{distributorCode}%' ", field = "distributor_code")
    private String distributorCode;
    @GraphQLField(kkhtml = "KFieldText", label = "销售商名称", kkhtmlDefault = true, sql = " distributor_name LIKE '%$U{distributorName}%' ", field = "distributor_name")
    private String distributorName;
    @GraphQLField(label = "销售商简称", sql = "distributor_simplify_name = $S{distributorSimplifyName}", field = "distributor_simplify_name")
    private String distributorSimplifyName;

    @GraphQLField(label = "销售商状态", sql = "status = $S{status}", field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldSelect", label = "销售商类型", kkhtmlDefault = true, sql = "distributor_type = $S{distributorType}", field = "distributor_type", kkhtmlExt = "{\"data-dict\":\"t8_distributor_type\"}")
    private String distributorType;
    @GraphQLField(label = "个人渠道所属部门", kkhtmlDefault = true, sql = "manager_dept = $S{managerDept}", field = "manager_dept")
    private String managerDept;
    @GraphQLField(label = "机构渠道所属部门", kkhtmlDefault = true, sql = "org_manage_dept = $S{orgManageDept}", field = "org_manage_dept")
    private String orgManageDept;
    @GraphQLField(label = "同业渠道所属部门", kkhtmlDefault = true, sql = "inter_manage_dept = $S{interManageDept}", field = "inter_manage_dept")
    private String interManageDept;
    @GraphQLField(label = "所属部门 ", sql = "dept = $S{dept}")
    private String dept;
    @GraphQLField(label = "接口方式 ", sql = "interface_type = $S{interfaceType}")
    private String interfaceType;
    @GraphQLField(label = "产品代码", sql = " prod_code IN ($S{prodCode})", field = "prod_code")
    private String prodCode;

    @GraphQLField(label = "", sql = "oper_flag = $S{operFlag}", field = "oper_flag")
    private String operFlag;
    @GraphQLField(label = "统一社会信用代码", kkhtmlDefault = true, sql = "n_legal_code = $S{nLegalCode}", field = "n_legal_code")
    private String nLegalCode;
    @GraphQLField(label = "法人代表证件类型", kkhtmlDefault = true, sql = "n_legal_type = $S{nLegalType}", field = "n_legal_type")
    private String nLegalType;
    @GraphQLField(label = "法人代表证件号码", kkhtmlDefault = true, sql = "n_legal_id_code = $S{nLegalIdCode}", field = "n_legal_id_code")
    private String nLegalIdCode;
    @GraphQLField(label = "技术联系人姓名", sql = "tech_connector = $S{techConnector}", field = "tech_connector")
    private String techConnector;
    @GraphQLField(label = "技术联系人电话", kkhtmlDefault = true, sql = "tech_connector_mobile = $S{techConnectorMobile}", field = "tech_connector_mobile")
    private String techConnectorMobile;
    @GraphQLField(label = "业务联系人姓名", kkhtmlDefault = true, sql = "busi_connector = $S{busiConnector}", field = "busi_connector")
    private String busiConnector;
    @GraphQLField(label = "业务联系人电话", sql = "busi_connector_mobile = $S{busiConnectorMobile}", field = "busi_connector_mobile")
    private String busiConnectorMobile;
    @GraphQLField(label = "销售商地址", kkhtmlDefault = true, sql = "address = $S{address}", field = "address")
    private String address;
    @GraphQLField(label = "邮件地址", kkhtmlDefault = true, sql = "email = $S{email}", field = "email")
    private String email;
    @GraphQLField(label = "邮编", kkhtmlDefault = true, sql = "postcode = $S{postcode}", field = "postcode")
    private String postcode;
    @GraphQLField(label = "传真号码", kkhtmlDefault = true, sql = "fax = $S{fax}", field = "fax")
    private String fax;


    @GraphQLField(label = "接口配置版本 ", sql = "interface_version = $S{interfaceVersion}", field = "interface_version")
    private String interfaceVersion;
    @GraphQLField(label = "是否导出参数文件(C1-C5)", sql = "is_export_c1c5_file = $S{isExportC1c5File}", field = "is_export_c1c5_file")
    private String isExportC1c5File;
    @GraphQLField(label = "是否导出C6、26文件", sql = "is_export_c6_26_file = $S{isExportC626File}", field = "is_export_c6_26_file")
    private String isExportC626File;

    @GraphQLField(label = "是否导出销售服务费文件", sql = "is_export_sale_fee_file = $S{isExportSaleFeeFile}", field = "is_export_sale_fee_file")
    private String isExportSaleFeeFile;
    @GraphQLField(label = "是否允许发起违约赎回", sql = "allow_break_redeem = $S{allowBreakRedeem}", field = "allow_break_redeem")
    private String allowBreakRedeem;
    @GraphQLField(label = "是否支持多交易账号", sql = "is_trans_much_acct = $S{isTransMuchAcct}", field = "is_trans_much_acct")
    private String isTransMuchAcct;
    @GraphQLField(label = "是否支持单步转托管", sql = "is_single_trust = $S{isSingleTrust}", field = "is_single_trust")
    private String isSingleTrust;
    @GraphQLField(label = "转换确认方式", sql = "convert_ack_method = $S{convertAckMethod}", field = "convert_ack_method")
    private String convertAckMethod;
    @GraphQLField(label = "是否有份额明细", sql = "is_vol_list = $S{isVolList}", field = "is_vol_list")
    private String isVolList;
    @GraphQLField(label = "对账方式", sql = "check_type = $S{checkType}", field = "check_type")
    private String checkType;
    @GraphQLField(label = "是否支持预分配账号", sql = "is_predistribution_acct = $S{isPredistributionAcct}", field = "is_predistribution_acct")
    private String isPredistributionAcct;
    @GraphQLField(label = "当前确认号", sql = "present_confirm_num = $S{presentConfirmNum}", field = "present_confirm_num")
    private String presentConfirmNum;
    @GraphQLField(label = "创建日期", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(label = "创建人", sql = "crt_user = $S{crtUser}", field = "crt_user")
    private String crtUser;
    @GraphQLField(label = "更新日期", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(label = "更新人", sql = "upd_user = $S{updUser}", field = "upd_user")
    private String updUser;
    @GraphQLField(label = "备注", kkhtmlDefault = true, sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(label = "任务分组", sql = "task_group = $S{taskGroup}", field = "task_group")
    private String taskGroup;
    @GraphQLField(label = "申请文件导出方式", sql = "file_imp_flag = $S{fileImpFlag}", field = "file_imp_flag")
    private String fileImpFlag;
    @GraphQLField(label = "文件批次", sql = "batch_no = $S{batchNo}", field = "batch_no")
    private String batchNo;
    @GraphQLField(label = "是否节假日推送", sql = "is_holidays_send = $S{isHolidaysSend}", field = "is_holidays_send")
    private String isHolidaysSend;
    @GraphQLField(label = "行情文件路径", sql = "fundday_file_path = $S{funddayFilePath}", field = "fundday_file_path")
    private String funddayFilePath;
    @GraphQLField(label = "确认文件路径", sql = "cfm_file_path = $S{cfmFilePath}", field = "cfm_file_path")
    private String cfmFilePath;
    @GraphQLField(label = "申请文件路径", sql = "req_file_path = $S{reqFilePath}", field = "req_file_path")
    private String reqFilePath;
    @GraphQLField(label = "解冻扣款文件类型", sql = "freez_file_type = $S{freezFileType}", field = "freez_file_type")
    private String freezFileType;
    @GraphQLField(label = "节假日方案", sql = "pgmno = $S{pgmno}", field = "pgmno")
    private String pgmno;
    @GraphQLField(label = "导入批次", field = "impTaskGroup")
    private String impTaskGroup;
    @GraphQLField(label = "导出批次", field = "expTaskGroup")
    private String expTaskGroup;
    @GraphQLField(label = "数据状态", sql = " data_status = ($S{dataStatus})", field = "data_status")
    private String dataStatus;
    @GraphQLField(label = "工作流ID", field = "process_instance_id")
    private String processInstanceId;
    @GraphQLField(label = "流程状态", field = "process_status")
    private String processStatus;
    @GraphQLField(label = "销售商官网", field = "official_website")
    private String officialWebsite;
    @GraphQLField(label = "客服热线", field = "customer_service_hotline")
    private String customerServiceHotline;
    @GraphQLField(label = "主要职责", field = "main_duty")
    private String mainDuty;

    public String getDistributorCode() {
        return distributorCode;
    }

    public void setDistributorCode(String distributorCode) {
        this.distributorCode = distributorCode;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorSimplifyName() {
        return distributorSimplifyName;
    }

    public void setDistributorSimplifyName(String distributorSimplifyName) {
        this.distributorSimplifyName = distributorSimplifyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistributorType() {
        return distributorType;
    }

    public void setDistributorType(String distributorType) {
        this.distributorType = distributorType;
    }

    public String getManagerDept() {
        return managerDept;
    }

    public void setManagerDept(String managerDept) {
        this.managerDept = managerDept;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getOperFlag() {
        return operFlag;
    }

    public void setOperFlag(String operFlag) {
        this.operFlag = operFlag;
    }

    public String getnLegalCode() {
        return nLegalCode;
    }

    public void setnLegalCode(String nLegalCode) {
        this.nLegalCode = nLegalCode;
    }

    public String getnLegalType() {
        return nLegalType;
    }

    public void setnLegalType(String nLegalType) {
        this.nLegalType = nLegalType;
    }

    public String getnLegalIdCode() {
        return nLegalIdCode;
    }

    public void setnLegalIdCode(String nLegalIdCode) {
        this.nLegalIdCode = nLegalIdCode;
    }

    public String getTechConnector() {
        return techConnector;
    }

    public void setTechConnector(String techConnector) {
        this.techConnector = techConnector;
    }

    public String getTechConnectorMobile() {
        return techConnectorMobile;
    }

    public void setTechConnectorMobile(String techConnectorMobile) {
        this.techConnectorMobile = techConnectorMobile;
    }

    public String getBusiConnector() {
        return busiConnector;
    }

    public void setBusiConnector(String busiConnector) {
        this.busiConnector = busiConnector;
    }

    public String getBusiConnectorMobile() {
        return busiConnectorMobile;
    }

    public void setBusiConnectorMobile(String busiConnectorMobile) {
        this.busiConnectorMobile = busiConnectorMobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public String getIsExportC1c5File() {
        return isExportC1c5File;
    }

    public void setIsExportC1c5File(String isExportC1c5File) {
        this.isExportC1c5File = isExportC1c5File;
    }

    public String getIsExportC626File() {
        return isExportC626File;
    }

    public void setIsExportC626File(String isExportC626File) {
        this.isExportC626File = isExportC626File;
    }

    public String getIsExportSaleFeeFile() {
        return isExportSaleFeeFile;
    }

    public void setIsExportSaleFeeFile(String isExportSaleFeeFile) {
        this.isExportSaleFeeFile = isExportSaleFeeFile;
    }

    public String getAllowBreakRedeem() {
        return allowBreakRedeem;
    }

    public void setAllowBreakRedeem(String allowBreakRedeem) {
        this.allowBreakRedeem = allowBreakRedeem;
    }

    public String getIsTransMuchAcct() {
        return isTransMuchAcct;
    }

    public void setIsTransMuchAcct(String isTransMuchAcct) {
        this.isTransMuchAcct = isTransMuchAcct;
    }

    public String getIsSingleTrust() {
        return isSingleTrust;
    }

    public void setIsSingleTrust(String isSingleTrust) {
        this.isSingleTrust = isSingleTrust;
    }

    public String getConvertAckMethod() {
        return convertAckMethod;
    }

    public void setConvertAckMethod(String convertAckMethod) {
        this.convertAckMethod = convertAckMethod;
    }

    public String getIsVolList() {
        return isVolList;
    }

    public void setIsVolList(String isVolList) {
        this.isVolList = isVolList;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getIsPredistributionAcct() {
        return isPredistributionAcct;
    }

    public void setIsPredistributionAcct(String isPredistributionAcct) {
        this.isPredistributionAcct = isPredistributionAcct;
    }

    public String getPresentConfirmNum() {
        return presentConfirmNum;
    }

    public void setPresentConfirmNum(String presentConfirmNum) {
        this.presentConfirmNum = presentConfirmNum;
    }

    public String getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getFileImpFlag() {
        return fileImpFlag;
    }

    public void setFileImpFlag(String fileImpFlag) {
        this.fileImpFlag = fileImpFlag;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIsHolidaysSend() {
        return isHolidaysSend;
    }

    public void setIsHolidaysSend(String isHolidaysSend) {
        this.isHolidaysSend = isHolidaysSend;
    }

    public String getFunddayFilePath() {
        return funddayFilePath;
    }

    public void setFunddayFilePath(String funddayFilePath) {
        this.funddayFilePath = funddayFilePath;
    }

    public String getCfmFilePath() {
        return cfmFilePath;
    }

    public void setCfmFilePath(String cfmFilePath) {
        this.cfmFilePath = cfmFilePath;
    }

    public String getReqFilePath() {
        return reqFilePath;
    }

    public void setReqFilePath(String reqFilePath) {
        this.reqFilePath = reqFilePath;
    }

    public String getFreezFileType() {
        return freezFileType;
    }

    public void setFreezFileType(String freezFileType) {
        this.freezFileType = freezFileType;
    }

    public String getPgmno() {
        return pgmno;
    }

    public void setPgmno(String pgmno) {
        this.pgmno = pgmno;
    }

    public String getImpTaskGroup() {
        return impTaskGroup;
    }

    public void setImpTaskGroup(String impTaskGroup) {
        this.impTaskGroup = impTaskGroup;
    }

    public String getExpTaskGroup() {
        return expTaskGroup;
    }

    public void setExpTaskGroup(String expTaskGroup) {
        this.expTaskGroup = expTaskGroup;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }

    public void setOfficialWebsite(String officialWebsite) {
        this.officialWebsite = officialWebsite;
    }

    public String getCustomerServiceHotline() {
        return customerServiceHotline;
    }

    public void setCustomerServiceHotline(String customerServiceHotline) {
        this.customerServiceHotline = customerServiceHotline;
    }

    public String getMainDuty() {
        return mainDuty;
    }

    public void setMainDuty(String mainDuty) {
        this.mainDuty = mainDuty;
    }
}