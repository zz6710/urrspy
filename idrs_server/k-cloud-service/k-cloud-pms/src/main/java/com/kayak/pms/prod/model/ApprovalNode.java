package com.kayak.pms.prod.model;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**sss
 * 审批节点实体
 * Created by zhanghao on 09/03/2022.
 */
@Alias("approvalNode")
public class ApprovalNode implements Serializable {
    private static final long serialVersionUID = -2797429216098306855L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 流程id
     */
    private String processId;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 审批节点等级
     */
    private Integer nodeLevel;
    /**
     * 审批人员
     */
    private String actorId;
    /**
     * 审批类型
     */
    private String nodeType;
    /**
     * 审批状态  没用
     */
    private String approvalStatus;

    private String createTime;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 是否部门领导审批 0是 1否
     */
    private String leaderApproval;

    //审批人员专用信息

    private String prodCode;
    private String isNeedProd;
    private String bindProd;

    //标志是否存在领导
    private String existLeader;


    //leaderId

    private String leaderId;

    //执行接口
    private String actionMethod;

    public String getIsCancle() {
        return isCancle;
    }

    public void setIsCancle(String isCancle) {
        this.isCancle = isCancle;
    }

    //是否允许撤销
    private String isCancle;


    public String getIsUploadFile() {
        return isUploadFile;
    }

    public void setIsUploadFile(String isUploadFile) {
        this.isUploadFile = isUploadFile;
    }

    //详情是否允许上传附件
    private String isUploadFile;

    public String getAddCondition() {
        return addCondition;
    }

    public void setAddCondition(String addCondition) {
        this.addCondition = addCondition;
    }

    //生成节点条件
    private String addCondition;

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getExistLeader() {
        return existLeader;
    }

    public void setExistLeader(String existLeader) {
        this.existLeader = existLeader;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getIsNeedProd() {
        return isNeedProd;
    }

    public void setIsNeedProd(String isNeedProd) {
        this.isNeedProd = isNeedProd;
    }

    public String getBindProd() {
        return bindProd;
    }

    public void setBindProd(String bindProd) {
        this.bindProd = bindProd;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getLeaderApproval() {
        return leaderApproval;
    }

    public void setLeaderApproval(String leaderApproval) {
        this.leaderApproval = leaderApproval;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    @Override
    public String toString() {
        return "ApprovalNode{" +
                "id='" + id + '\'' +
                ", processId='" + processId + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodeLevel=" + nodeLevel +
                ", actorId='" + actorId + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", createTime='" + createTime + '\'' +
                ", roleId='" + roleId + '\'' +
                ", leaderApproval='" + leaderApproval + '\'' +
                ", actionMethod='" + actionMethod + '\'' +
                ", isCancle='" + isCancle + '\'' +
                ", addCondition='" + addCondition + '\'' +
                ", isUploadFile='" + isUploadFile + '\'' +
                '}';
    }
}
