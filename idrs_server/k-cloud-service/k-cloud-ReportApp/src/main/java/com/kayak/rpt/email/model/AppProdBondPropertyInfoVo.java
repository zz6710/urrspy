package com.kayak.rpt.email.model;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 打标为房地产行业的债券投资明细 汇总表-对象  变动-发邮件
 */
@Data
public class AppProdBondPropertyInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name="id",value = "主键id")
    private String id;
    //report_date
    @ApiModelProperty(name="reportDate",value = "数据日期")
    private String reportDate;
    //bond_code
    @ApiModelProperty(name="bondCode",value = "债券wind代码")
//    @ExcelProperty("债券wind代码")
    private String bondCode;
    //bond_qntt
    @ApiModelProperty(name="bondQntt",value = "债券持仓数量")
//    @ExcelProperty("成本(理财产品估值表中的【数量】)")
    private String bondQntt;
    //bond_qntt_xp
    @ApiModelProperty(name="bondQnttXp",value = "债券持仓数量（信评）")
//    @ExcelProperty("债券持仓数量(额度系统债券持仓文件中【数量】)")
    private String bondQnttXp;
    //issr_nm
    @ApiModelProperty(name="issrNm",value = "发行人")
//    @ExcelProperty("发行人")
    private String issrNm;
    //quota_OCC
    @ApiModelProperty(name="quotaOcc",value = "额度占用方")
//    @ExcelProperty("额度占用方")
    private String quotaOcc;
    //moneyofproperty
    @ApiModelProperty(name="moneyofproperty",value = "是否投向房地产行业")
//    @ExcelProperty("是否投向房地产行业")
    private String moneyofproperty;
    //prod_name
    @ApiModelProperty(name="prodName",value = "产品名称/委外专户名称")
//    @ExcelProperty("产品名称/委外专户名称")
    private String prodName;
    @ApiModelProperty(name="rate",value = "投资估值表比例 decimal(31,8)")
//    @ExcelProperty("投资估值表比例 decimal(31,8)")
    private String rate;
    //bond_face
    @ApiModelProperty(name="bondFace",value = "券面金额（万元）")
//    @ExcelProperty("券面金额（万元）")
    private String bondFace;

    public AppProdBondPropertyInfoVo(){}
//    public AppProdBondPropertyInfoVo(String bondCode, String bondQntt,String bondQnttXp,String issrNm,String quotaOcc,String moneyofproperty,String prodName,String rate,String bondFace){
//
//    }
}
