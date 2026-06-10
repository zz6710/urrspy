package com.kayak.pms.T82.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import com.kayak.pms.T82.model.BizBodyConfig;

public class ExcelJSON {
    /**
     * 机构代码
     */
    @JSONField(ordinal = 1)
    private String organizationCode;
    /**
     * 业务代码
     */
    @JSONField(ordinal = 2)
    private String businessCode;
    /**
     * 长度 TODO 不知道什么长度
     */
    @JSONField(ordinal = 3)
    private String sliceSize;

    @JSONField(ordinal = 4)
    private String sliceSql;
    /**
     * head_sql
     */
    @JSONField(ordinal = 5)
    private String headSql;
    /**
     * 数据源
     */
    @JSONField(ordinal = 6)
    private String datasouce;
    /**
     * head_字段信息
     */
    @JSONField(ordinal = 7)
    private String[] head;
    /**
     * body_sql
     */
    @JSONField(ordinal = 8)
    private String bodySql;
    /**
     * body_字段信息
     */
    @JSONField(ordinal = 9)
    private List<BizBodyConfig> bizBodyConfig;
    /**
     * tail_文件尾
     */
    @JSONField(ordinal = 10)
    private String[] tail;

    @JSONField(ordinal = 11)
    private String slicefield;
    /**
     * 错误码参数名称
     */
    @JSONField(ordinal = 12)
    private String errCodeParamName;
    /**
     * 错误信息参数名称
     */
    @JSONField(ordinal = 13)
    private String errMsgParamName;
    /**
     * 文件批次号参数名称
     */
    @JSONField(ordinal = 14)
    private String fileSernoParamName;
    /**
     * 校验sql
     */
    @JSONField(ordinal = 15)
    private String validatorSql;

    @JSONField(ordinal = 16)
    private Boolean createOkFile;
    /**
     * 文件字符集
     */
    @JSONField(ordinal = 17)
    private String fileEncoding;
    /**
     * 协议标准
     */
    @JSONField(ordinal = 18)
    private String protocol;
    /**
     * 字段分隔符标记
     */
    @JSONField(ordinal = 19)
    private String columnSplit;
    /**
     * 行标记
     */
    @JSONField(ordinal = 20)
    private String lineBreak;

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getSliceSize() {
        return sliceSize;
    }

    public void setSliceSize(String sliceSize) {
        this.sliceSize = sliceSize;
    }

    public String getSliceSql() {
        return sliceSql;
    }

    public void setSliceSql(String sliceSql) {
        this.sliceSql = sliceSql;
    }

    public String getHeadSql() {
        return headSql;
    }

    public void setHeadSql(String headSql) {
        this.headSql = headSql;
    }

    public String getDatasouce() {
        return datasouce;
    }

    public void setDatasouce(String datasouce) {
        this.datasouce = datasouce;
    }

    public String[] getHead() {
        return head;
    }

    public void setHead(String[] head) {
        this.head = head;
    }

    public String getBodySql() {
        return bodySql;
    }

    public void setBodySql(String bodySql) {
        this.bodySql = bodySql;
    }

    public List<BizBodyConfig> getBizBodyConfig() {
        return bizBodyConfig;
    }

    public void setBizBodyConfig(List<BizBodyConfig> bizBodyConfig) {
        this.bizBodyConfig = bizBodyConfig;
    }

    public String[] getTail() {
        return tail;
    }

    public void setTail(String[] tail) {
        this.tail = tail;
    }

    public String getSlicefield() {
        return slicefield;
    }

    public void setSlicefield(String slicefield) {
        this.slicefield = slicefield;
    }

    public String getErrCodeParamName() {
        return errCodeParamName;
    }

    public void setErrCodeParamName(String errCodeParamName) {
        this.errCodeParamName = errCodeParamName;
    }

    public String getErrMsgParamName() {
        return errMsgParamName;
    }

    public void setErrMsgParamName(String errMsgParamName) {
        this.errMsgParamName = errMsgParamName;
    }

    public String getFileSernoParamName() {
        return fileSernoParamName;
    }

    public void setFileSernoParamName(String fileSernoParamName) {
        this.fileSernoParamName = fileSernoParamName;
    }

    public String getValidatorSql() {
        return validatorSql;
    }

    public void setValidatorSql(String validatorSql) {
        this.validatorSql = validatorSql;
    }

    public Boolean getCreateOkFile() {
        return createOkFile;
    }

    public void setCreateOkFile(Boolean createOkFile) {
        this.createOkFile = createOkFile;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getColumnSplit() {
        return columnSplit;
    }

    public void setColumnSplit(String columnSplit) {
        this.columnSplit = columnSplit;
    }

    public String getLineBreak() {
        return lineBreak;
    }

    public void setLineBreak(String lineBreak) {
        this.lineBreak = lineBreak;
    }
}
