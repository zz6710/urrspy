package com.kayak.pms.T82.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t82001ProdAuthorityService",table = "T8_DISTRIBUTOR_INFO")
public class T82001ProdAuthority implements Cloneable{

    @GraphQLField(label = "产品代码", sql = " prod_code like '%$U{prodCode}%'" ,field = "prod_code")
    private String prodCode;

    @GraphQLField(label = "销售商代码", sql = " distributor_code =$S{distributorCode} " ,field = "distributor_code")
    private String distributorCode;

    @GraphQLField(label = "状态", sql = "status = $S{status}" ,field = "status")
    private String status;
    @GraphQLField(label = "资金处理模式", sql = "handler_mode = $S{handlerMode}" ,field = "handler_mode")
    private String handlerMode;

    @GraphQLField(label = "利率方案代码", sql = "interest_code = $S{interestCode}" ,field = "interest_code")
    private String interestCode;

    @GraphQLField(label = "利率方案名称", sql = "interest_class = $S{interestClass}" ,field = "interest_class")
    private String interestClass;

    @GraphQLField(label = "数据状态", sql = " data_status = ($S{dataStatus})" ,field = "data_status")
    private String dataStatus;

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getDistributorCode() {
        return distributorCode;
    }

    public void setDistributorCode(String distributorCode) {
        this.distributorCode = distributorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHandlerMode() {
        return handlerMode;
    }

    public void setHandlerMode(String handlerMode) {
        this.handlerMode = handlerMode;
    }

    public String getInterestCode() {
        return interestCode;
    }

    public void setInterestCode(String interestCode) {
        this.interestCode = interestCode;
    }

    public String getInterestClass() {
        return interestClass;
    }

    public void setInterestClass(String interestClass) {
        this.interestClass = interestClass;
    }

    public T82001ProdAuthority clone() throws CloneNotSupportedException{
        return (T82001ProdAuthority) super.clone();
    }
}
