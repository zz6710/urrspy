package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//企业债券分行业和企业规模情况信息
@Data
@GraphQLModel(fetcher = "ZG11Service",table = "app_pbc_report_zg11")
public class ZG11 {

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
    private String id;

    @ExcelProperty(value = "发行机构代码")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "isu_org_cd like '%$U{isuOrgCd}%'" ,field = "isu_org_cd")
    private String isuOrgCd;

    @ExcelProperty(value = "数据日期")
    @GraphQLField(kkhtml = "KFieldText", label = "实际报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;

    @ExcelProperty(value = "产品品种_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "产品品种", sql = "prod_cate = $S{prodCate}" ,field = "prod_cate")
    private String prodCate;

    @ExcelProperty(value = "c00000_非金融企业债券余额总计")
    @GraphQLField(kkhtml = "KFieldText", label = "c00000_企业债券余额总计", sql = "c00000 = $S{c00000}" ,field = "c00000")
    private String c00000;
    @ExcelProperty(value = "c01000_非金融企业债券余额总计-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c01000_企业债券余额总计-大型企业", sql = "c01000 = $S{c01000}" ,field = "c01000")
    private String c01000;
    @ExcelProperty(value = "c02000_非金融企业债券余额总计-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c02000_企业债券余额总计-中型企业", sql = "c02000 = $S{c02000}" ,field = "c02000")
    private String c02000;
    @ExcelProperty(value = "c03000_非金融企业债券余额总计-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c03000_企业债券余额总计-小型企业", sql = "c03000 = $S{c03000}" ,field = "c03000")
    private String c03000;
    @ExcelProperty(value = "c04000_非金融企业债券余额总计-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c04000_企业债券余额总计-微型企业", sql = "c04000 = $S{c04000}" ,field = "c04000")
    private String c04000;
    @ExcelProperty(value = "c10000_农、林、牧、渔业")
    @GraphQLField(kkhtml = "KFieldText", label = "c10000_农、林、牧、渔业", sql = "c10000 = $S{c10000}" ,field = "c10000")
    private String c10000;
    @ExcelProperty(value = "c11000_农、林、牧、渔业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c11000_农、林、牧、渔业-大型企业", sql = "c11000 = $S{c11000}" ,field = "c11000")
    private String c11000;
    @ExcelProperty(value = "c12000_农、林、牧、渔业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c12000_农、林、牧、渔业-中型企业", sql = "c12000 = $S{c12000}" ,field = "c12000")
    private String c12000;
    @ExcelProperty(value = "c13000_农、林、牧、渔业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c13000_农、林、牧、渔业-小型企业", sql = "c13000 = $S{c13000}" ,field = "c13000")
    private String c13000;
    @ExcelProperty(value = "c14000_农、林、牧、渔业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c14000_农、林、牧、渔业-微型企业", sql = "c14000 = $S{c14000}" ,field = "c14000")
    private String c14000;
    @ExcelProperty(value = "c20000_采矿业")
    @GraphQLField(kkhtml = "KFieldText", label = "c20000_采矿业", sql = "c20000 = $S{c20000}" ,field = "c20000")
    private String c20000;
    @ExcelProperty(value = "c21000_采矿业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c21000_采矿业-大型企业", sql = "c21000 = $S{c21000}" ,field = "c21000")
    private String c21000;
    @ExcelProperty(value = "c22000_采矿业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c22000_采矿业-中型企业", sql = "c22000 = $S{c22000}" ,field = "c22000")
    private String c22000;
    @ExcelProperty(value = "c23000_采矿业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c23000_采矿业-小型企业", sql = "c23000 = $S{c23000}" ,field = "c23000")
    private String c23000;
    @ExcelProperty(value = "c24000_采矿业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c24000_采矿业-微型企业", sql = "c24000 = $S{c24000}" ,field = "c24000")
    private String c24000;
    @ExcelProperty(value = "c30000_制造业")
    @GraphQLField(kkhtml = "KFieldText", label = "c30000_制造业", sql = "c30000 = $S{c30000}" ,field = "c30000")
    private String c30000;
    @ExcelProperty(value = "c31000_制造业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c31000_制造业-大型企业", sql = "c31000 = $S{c31000}" ,field = "c31000")
    private String c31000;
    @ExcelProperty(value = "c32000_制造业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c32000_制造业-中型企业", sql = "c32000 = $S{c32000}" ,field = "c32000")
    private String c32000;
    @ExcelProperty(value = "c33000_制造业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c33000_制造业-小型企业", sql = "c33000 = $S{c33000}" ,field = "c33000")
    private String c33000;
    @ExcelProperty(value = "c34000_制造业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c34000_制造业-微型企业", sql = "c34000 = $S{c34000}" ,field = "c34000")
    private String c34000;
    @ExcelProperty(value = "c40000_电力、热力、燃气及水生产和供应业")
    @GraphQLField(kkhtml = "KFieldText", label = "c40000_电力、热力、燃气及水生产和供应业", sql = "c40000 = $S{c40000}" ,field = "c40000")
    private String c40000;
    @ExcelProperty(value = "c41000_电力、热力、燃气及水生产和供应业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c41000_电力、热力、燃气及水生产和供应业-大型企业", sql = "c41000 = $S{c41000}" ,field = "c41000")
    private String c41000;
    @ExcelProperty(value = "c42000_电力、热力、燃气及水生产和供应业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c42000_电力、热力、燃气及水生产和供应业-中型企业", sql = "c42000 = $S{c42000}" ,field = "c42000")
    private String c42000;
    @ExcelProperty(value = "c43000_电力、热力、燃气及水生产和供应业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c43000_电力、热力、燃气及水生产和供应业-小型企业", sql = "c43000 = $S{c43000}" ,field = "c43000")
    private String c43000;
    @ExcelProperty(value = "c44000_电力、热力、燃气及水生产和供应业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c44000_电力、热力、燃气及水生产和供应业-微型企业", sql = "c44000 = $S{c44000}" ,field = "c44000")
    private String c44000;
    @ExcelProperty(value = "c50000_建筑业")
    @GraphQLField(kkhtml = "KFieldText", label = "c50000_建筑业", sql = "c50000 = $S{c50000}" ,field = "c50000")
    private String c50000;
    @ExcelProperty(value = "c51000_建筑业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c51000_建筑业-大型企业", sql = "c51000 = $S{c51000}" ,field = "c51000")
    private String c51000;
    @ExcelProperty(value = "c52000_建筑业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c52000_建筑业-中型企业", sql = "c52000 = $S{c52000}" ,field = "c52000")
    private String c52000;
    @ExcelProperty(value = "c53000_建筑业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c53000_建筑业-小型企业", sql = "c53000 = $S{c53000}" ,field = "c53000")
    private String c53000;
    @ExcelProperty(value = "c54000_建筑业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c54000_建筑业-微型企业", sql = "c54000 = $S{c54000}" ,field = "c54000")
    private String c54000;
    @ExcelProperty(value = "c60000_批发和零售业")
    @GraphQLField(kkhtml = "KFieldText", label = "c60000_批发和零售业", sql = "c60000 = $S{c60000}" ,field = "c60000")
    private String c60000;
    @ExcelProperty(value = "c61000_批发和零售业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c61000_批发和零售业-大型企业", sql = "c61000 = $S{c61000}" ,field = "c61000")
    private String c61000;
    @ExcelProperty(value = "c62000_批发和零售业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c62000_批发和零售业-中型企业", sql = "c62000 = $S{c62000}" ,field = "c62000")
    private String c62000;
    @ExcelProperty(value = "c63000_批发和零售业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c63000_批发和零售业-小型企业", sql = "c63000 = $S{c63000}" ,field = "c63000")
    private String c63000;
    @ExcelProperty(value = "c64000_批发和零售业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c64000_批发和零售业-微型企业", sql = "c64000 = $S{c64000}" ,field = "c64000")
    private String c64000;
    @ExcelProperty(value = "c70000_交通运输、仓储和邮政业")
    @GraphQLField(kkhtml = "KFieldText", label = "c70000_交通运输、仓储和邮政业", sql = "c70000 = $S{c70000}" ,field = "c70000")
    private String c70000;
    @ExcelProperty(value = "c71000_交通运输、仓储和邮政业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c71000_交通运输、仓储和邮政业-大型企业", sql = "c71000 = $S{c71000}" ,field = "c71000")
    private String c71000;
    @ExcelProperty(value = "c72000_交通运输、仓储和邮政业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c72000_交通运输、仓储和邮政业-中型企业", sql = "c72000 = $S{c72000}" ,field = "c72000")
    private String c72000;
    @ExcelProperty(value = "c73000_交通运输、仓储和邮政业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c73000_交通运输、仓储和邮政业-小型企业", sql = "c73000 = $S{c73000}" ,field = "c73000")
    private String c73000;
    @ExcelProperty(value = "c74000_交通运输、仓储和邮政业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c74000_交通运输、仓储和邮政业-微型企业", sql = "c74000 = $S{c74000}" ,field = "c74000")
    private String c74000;
    @ExcelProperty(value = "c80000_住宿和餐饮业")
    @GraphQLField(kkhtml = "KFieldText", label = "c80000_住宿和餐饮业", sql = "c80000 = $S{c80000}" ,field = "c80000")
    private String c80000;
    @ExcelProperty(value = "c81000_住宿和餐饮业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c81000_住宿和餐饮业-大型企业", sql = "c81000 = $S{c81000}" ,field = "c81000")
    private String c81000;
    @ExcelProperty(value = "c82000_住宿和餐饮业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c82000_住宿和餐饮业-中型企业", sql = "c82000 = $S{c82000}" ,field = "c82000")
    private String c82000;
    @ExcelProperty(value = "c83000_住宿和餐饮业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c83000_住宿和餐饮业-小型企业", sql = "c83000 = $S{c83000}" ,field = "c83000")
    private String c83000;
    @ExcelProperty(value = "c84000_住宿和餐饮业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c84000_住宿和餐饮业-微型企业", sql = "c84000 = $S{c84000}" ,field = "c84000")
    private String c84000;
    @ExcelProperty(value = "c90000_信息传输、软件和信息技术服务业")
    @GraphQLField(kkhtml = "KFieldText", label = "c90000_信息传输、软件和信息技术服务业", sql = "c90000 = $S{c90000}" ,field = "c90000")
    private String c90000;
    @ExcelProperty(value = "c91000_信息传输、软件和信息技术服务业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c91000_信息传输、软件和信息技术服务业-大型企业", sql = "c91000 = $S{c91000}" ,field = "c91000")
    private String c91000;
    @ExcelProperty(value = "c92000_信息传输、软件和信息技术服务业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c92000_信息传输、软件和信息技术服务业-中型企业", sql = "c92000 = $S{c92000}" ,field = "c92000")
    private String c92000;
    @ExcelProperty(value = "c93000_信息传输、软件和信息技术服务业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c93000_信息传输、软件和信息技术服务业-小型企业", sql = "c93000 = $S{c93000}" ,field = "c93000")
    private String c93000;
    @ExcelProperty(value = "c94000_信息传输、软件和信息技术服务业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "c94000_信息传输、软件和信息技术服务业-微型企业", sql = "c94000 = $S{c94000}" ,field = "c94000")
    private String c94000;
    @ExcelProperty(value = "ca0000_金融业")
    @GraphQLField(kkhtml = "KFieldText", label = "ca0000_金融业", sql = "ca0000 = $S{ca0000}" ,field = "ca0000")
    private String ca0000;
    @ExcelProperty(value = "ca1000_金融业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ca1000_金融业-大型企业", sql = "ca1000 = $S{ca1000}" ,field = "ca1000")
    private String ca1000;
    @ExcelProperty(value = "ca2000_金融业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ca2000_金融业-中型企业", sql = "ca2000 = $S{ca2000}" ,field = "ca2000")
    private String ca2000;
    @ExcelProperty(value = "ca3000_金融业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ca3000_金融业-小型企业", sql = "ca3000 = $S{ca3000}" ,field = "ca3000")
    private String ca3000;
    @ExcelProperty(value = "ca4000_金融业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ca4000_金融业-微型企业", sql = "ca4000 = $S{ca4000}" ,field = "ca4000")
    private String ca4000;
    @ExcelProperty(value = "cb0000_房地产业")
    @GraphQLField(kkhtml = "KFieldText", label = "cb0000_房地产业", sql = "cb0000 = $S{cb0000}" ,field = "cb0000")
    private String cb0000;
    @ExcelProperty(value = "cb1000_房地产业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cb1000_房地产业-大型企业", sql = "cb1000 = $S{cb1000}" ,field = "cb1000")
    private String cb1000;
    @ExcelProperty(value = "cb2000_房地产业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cb2000_房地产业-中型企业", sql = "cb2000 = $S{cb2000}" ,field = "cb2000")
    private String cb2000;
    @ExcelProperty(value = "cb3000_房地产业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cb3000_房地产业-小型企业", sql = "cb3000 = $S{cb3000}" ,field = "cb3000")
    private String cb3000;
    @ExcelProperty(value = "cb4000_房地产业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cb4000_房地产业-微型企业", sql = "cb4000 = $S{cb4000}" ,field = "cb4000")
    private String cb4000;
    @ExcelProperty(value = "cc0000_租赁和商务服务业")
    @GraphQLField(kkhtml = "KFieldText", label = "cc0000_租赁和商务服务业", sql = "cc0000 = $S{cc0000}" ,field = "cc0000")
    private String cc0000;
    @ExcelProperty(value = "cc1000_租赁和商务服务业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cc1000_租赁和商务服务业-大型企业", sql = "cc1000 = $S{cc1000}" ,field = "cc1000")
    private String cc1000;
    @ExcelProperty(value = "cc2000_租赁和商务服务业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cc2000_租赁和商务服务业-中型企业", sql = "cc2000 = $S{cc2000}" ,field = "cc2000")
    private String cc2000;
    @ExcelProperty(value = "cc3000_租赁和商务服务业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cc3000_租赁和商务服务业-小型企业", sql = "cc3000 = $S{cc3000}" ,field = "cc3000")
    private String cc3000;
    @ExcelProperty(value = "cc4000_租赁和商务服务业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cc4000_租赁和商务服务业-微型企业", sql = "cc4000 = $S{cc4000}" ,field = "cc4000")
    private String cc4000;
    @ExcelProperty(value = "cd0000_科学研究和技术服务业")
    @GraphQLField(kkhtml = "KFieldText", label = "cd0000_科学研究和技术服务业", sql = "cd0000 = $S{cd0000}" ,field = "cd0000")
    private String cd0000;
    @ExcelProperty(value = "cd1000_科学研究和技术服务业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cd1000_科学研究和技术服务业-大型企业", sql = "cd1000 = $S{cd1000}" ,field = "cd1000")
    private String cd1000;
    @ExcelProperty(value = "cd2000_科学研究和技术服务业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cd2000_科学研究和技术服务业-中型企业", sql = "cd2000 = $S{cd2000}" ,field = "cd2000")
    private String cd2000;
    @ExcelProperty(value = "cd3000_科学研究和技术服务业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cd3000_科学研究和技术服务业-小型企业", sql = "cd3000 = $S{cd3000}" ,field = "cd3000")
    private String cd3000;
    @ExcelProperty(value = "cd4000_科学研究和技术服务业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cd4000_科学研究和技术服务业-微型企业", sql = "cd4000 = $S{cd4000}" ,field = "cd4000")
    private String cd4000;
    @ExcelProperty(value = "ce0000_水利、环境和公共设施管理业")
    @GraphQLField(kkhtml = "KFieldText", label = "ce0000_水利、环境和公共设施管理业", sql = "ce0000 = $S{ce0000}" ,field = "ce0000")
    private String ce0000;
    @ExcelProperty(value = "ce1000_水利、环境和公共设施管理业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ce1000_水利、环境和公共设施管理业-大型企业", sql = "ce1000 = $S{ce1000}" ,field = "ce1000")
    private String ce1000;
    @ExcelProperty(value = "ce2000_水利、环境和公共设施管理业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ce2000_水利、环境和公共设施管理业-中型企业", sql = "ce2000 = $S{ce2000}" ,field = "ce2000")
    private String ce2000;
    @ExcelProperty(value = "ce3000_水利、环境和公共设施管理业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ce3000_水利、环境和公共设施管理业-小型企业", sql = "ce3000 = $S{ce3000}" ,field = "ce3000")
    private String ce3000;
    @ExcelProperty(value = "ce4000_水利、环境和公共设施管理业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ce4000_水利、环境和公共设施管理业-微型企业", sql = "ce4000 = $S{ce4000}" ,field = "ce4000")
    private String ce4000;
    @ExcelProperty(value = "cf0000_居民服务、修理和其他服务业")
    @GraphQLField(kkhtml = "KFieldText", label = "cf0000_居民服务、修理和其他服务业", sql = "cf0000 = $S{cf0000}" ,field = "cf0000")
    private String cf0000;
    @ExcelProperty(value = "cf1000_居民服务、修理和其他服务业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cf1000_居民服务、修理和其他服务业-大型企业", sql = "cf1000 = $S{cf1000}" ,field = "cf1000")
    private String cf1000;
    @ExcelProperty(value = "cf2000_居民服务、修理和其他服务业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cf2000_居民服务、修理和其他服务业-中型企业", sql = "cf2000 = $S{cf2000}" ,field = "cf2000")
    private String cf2000;
    @ExcelProperty(value = "cf3000_居民服务、修理和其他服务业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cf3000_居民服务、修理和其他服务业-小型企业", sql = "cf3000 = $S{cf3000}" ,field = "cf3000")
    private String cf3000;
    @ExcelProperty(value = "cf4000_居民服务、修理和其他服务业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cf4000_居民服务、修理和其他服务业-微型企业", sql = "cf4000 = $S{cf4000}" ,field = "cf4000")
    private String cf4000;
    @ExcelProperty(value = "cg0000_教育")
    @GraphQLField(kkhtml = "KFieldText", label = "cg0000_教育", sql = "cg0000 = $S{cg0000}" ,field = "cg0000")
    private String cg0000;
    @ExcelProperty(value = "cg1000_教育-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cg1000_教育-大型企业", sql = "cg1000 = $S{cg1000}" ,field = "cg1000")
    private String cg1000;
    @ExcelProperty(value = "cg2000_教育-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cg2000_教育-中型企业", sql = "cg2000 = $S{cg2000}" ,field = "cg2000")
    private String cg2000;
    @ExcelProperty(value = "cg3000_教育-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cg3000_教育-小型企业", sql = "cg3000 = $S{cg3000}" ,field = "cg3000")
    private String cg3000;
    @ExcelProperty(value = "cg4000_教育-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cg4000_教育-微型企业", sql = "cg4000 = $S{cg4000}" ,field = "cg4000")
    private String cg4000;
    @ExcelProperty(value = "ch0000_卫生和社会工作")
    @GraphQLField(kkhtml = "KFieldText", label = "ch0000_卫生和社会工作", sql = "ch0000 = $S{ch0000}" ,field = "ch0000")
    private String ch0000;
    @ExcelProperty(value = "ch1000_卫生和社会工作-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ch1000_卫生和社会工作-大型企业", sql = "ch1000 = $S{ch1000}" ,field = "ch1000")
    private String ch1000;
    @ExcelProperty(value = "ch2000_卫生和社会工作-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ch2000_卫生和社会工作-中型企业", sql = "ch2000 = $S{ch2000}" ,field = "ch2000")
    private String ch2000;
    @ExcelProperty(value = "ch3000_卫生和社会工作-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ch3000_卫生和社会工作-小型企业", sql = "ch3000 = $S{ch3000}" ,field = "ch3000")
    private String ch3000;
    @ExcelProperty(value = "ch4000_卫生和社会工作-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ch4000_卫生和社会工作-微型企业", sql = "ch4000 = $S{ch4000}" ,field = "ch4000")
    private String ch4000;
    @ExcelProperty(value = "ci0000_文化、体育和娱乐业")
    @GraphQLField(kkhtml = "KFieldText", label = "ci0000_文化、体育和娱乐业", sql = "ci0000 = $S{ci0000}" ,field = "ci0000")
    private String ci0000;
    @ExcelProperty(value = "ci1000_文化、体育和娱乐业-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ci1000_文化、体育和娱乐业-大型企业", sql = "ci1000 = $S{ci1000}" ,field = "ci1000")
    private String ci1000;
    @ExcelProperty(value = "ci2000_文化、体育和娱乐业-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ci2000_文化、体育和娱乐业-中型企业", sql = "ci2000 = $S{ci2000}" ,field = "ci2000")
    private String ci2000;
    @ExcelProperty(value = "ci3000_文化、体育和娱乐业-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ci3000_文化、体育和娱乐业-小型企业", sql = "ci3000 = $S{ci3000}" ,field = "ci3000")
    private String ci3000;
    @ExcelProperty(value = "ci4000_文化、体育和娱乐业-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "ci4000_文化、体育和娱乐业-微型企业", sql = "ci4000 = $S{ci4000}" ,field = "ci4000")
    private String ci4000;
    @ExcelProperty(value = "cj0000_公共管理、社会保障和社会组织")
    @GraphQLField(kkhtml = "KFieldText", label = "cj0000_公共管理、社会保障和社会组织", sql = "cj0000 = $S{cj0000}" ,field = "cj0000")
    private String cj0000;
    @ExcelProperty(value = "cj1000_公共管理、社会保障和社会组织-大型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cj1000_公共管理、社会保障和社会组织-大型企业", sql = "cj1000 = $S{cj1000}" ,field = "cj1000")
    private String cj1000;
    @ExcelProperty(value = "cj2000_公共管理、社会保障和社会组织-中型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cj2000_公共管理、社会保障和社会组织-中型企业", sql = "cj2000 = $S{cj2000}" ,field = "cj2000")
    private String cj2000;
    @ExcelProperty(value = "cj3000_公共管理、社会保障和社会组织-小型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cj3000_公共管理、社会保障和社会组织-小型企业", sql = "cj3000 = $S{cj3000}" ,field = "cj3000")
    private String cj3000;
    @ExcelProperty(value = "cj4000_公共管理、社会保障和社会组织-微型企业")
    @GraphQLField(kkhtml = "KFieldText", label = "cj4000_公共管理、社会保障和社会组织-微型企业", sql = "cj4000 = $S{cj4000}" ,field = "cj4000")
    private String cj4000;

    @ExcelProperty(value = "信托产品口径")
    @GraphQLField(kkhtml = "KFieldText", label = "信托产品口径", sql = "trust_prod = $S{trustProd}" ,field = "trust_prod")
    private String trustProd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsuOrgCd() {
        return isuOrgCd;
    }

    public void setIsuOrgCd(String isuOrgCd) {
        this.isuOrgCd = isuOrgCd;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getTheoryReportStartDate() {
        return theoryReportStartDate;
    }

    public void setTheoryReportStartDate(String theoryReportStartDate) {
        this.theoryReportStartDate = theoryReportStartDate;
    }

    public String getTheoryReportEndDate() {
        return theoryReportEndDate;
    }

    public void setTheoryReportEndDate(String theoryReportEndDate) {
        this.theoryReportEndDate = theoryReportEndDate;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getProdCate() {
        return prodCate;
    }

    public void setProdCate(String prodCate) {
        this.prodCate = prodCate;
    }

    public String getC00000() {
        return c00000;
    }

    public void setC00000(String c00000) {
        this.c00000 = c00000;
    }

    public String getC01000() {
        return c01000;
    }

    public void setC01000(String c01000) {
        this.c01000 = c01000;
    }

    public String getC02000() {
        return c02000;
    }

    public void setC02000(String c02000) {
        this.c02000 = c02000;
    }

    public String getC03000() {
        return c03000;
    }

    public void setC03000(String c03000) {
        this.c03000 = c03000;
    }

    public String getC04000() {
        return c04000;
    }

    public void setC04000(String c04000) {
        this.c04000 = c04000;
    }

    public String getC10000() {
        return c10000;
    }

    public void setC10000(String c10000) {
        this.c10000 = c10000;
    }

    public String getC11000() {
        return c11000;
    }

    public void setC11000(String c11000) {
        this.c11000 = c11000;
    }

    public String getC12000() {
        return c12000;
    }

    public void setC12000(String c12000) {
        this.c12000 = c12000;
    }

    public String getC13000() {
        return c13000;
    }

    public void setC13000(String c13000) {
        this.c13000 = c13000;
    }

    public String getC14000() {
        return c14000;
    }

    public void setC14000(String c14000) {
        this.c14000 = c14000;
    }

    public String getC20000() {
        return c20000;
    }

    public void setC20000(String c20000) {
        this.c20000 = c20000;
    }

    public String getC21000() {
        return c21000;
    }

    public void setC21000(String c21000) {
        this.c21000 = c21000;
    }

    public String getC22000() {
        return c22000;
    }

    public void setC22000(String c22000) {
        this.c22000 = c22000;
    }

    public String getC23000() {
        return c23000;
    }

    public void setC23000(String c23000) {
        this.c23000 = c23000;
    }

    public String getC24000() {
        return c24000;
    }

    public void setC24000(String c24000) {
        this.c24000 = c24000;
    }

    public String getC30000() {
        return c30000;
    }

    public void setC30000(String c30000) {
        this.c30000 = c30000;
    }

    public String getC31000() {
        return c31000;
    }

    public void setC31000(String c31000) {
        this.c31000 = c31000;
    }

    public String getC32000() {
        return c32000;
    }

    public void setC32000(String c32000) {
        this.c32000 = c32000;
    }

    public String getC33000() {
        return c33000;
    }

    public void setC33000(String c33000) {
        this.c33000 = c33000;
    }

    public String getC34000() {
        return c34000;
    }

    public void setC34000(String c34000) {
        this.c34000 = c34000;
    }

    public String getC40000() {
        return c40000;
    }

    public void setC40000(String c40000) {
        this.c40000 = c40000;
    }

    public String getC41000() {
        return c41000;
    }

    public void setC41000(String c41000) {
        this.c41000 = c41000;
    }

    public String getC42000() {
        return c42000;
    }

    public void setC42000(String c42000) {
        this.c42000 = c42000;
    }

    public String getC43000() {
        return c43000;
    }

    public void setC43000(String c43000) {
        this.c43000 = c43000;
    }

    public String getC44000() {
        return c44000;
    }

    public void setC44000(String c44000) {
        this.c44000 = c44000;
    }

    public String getC50000() {
        return c50000;
    }

    public void setC50000(String c50000) {
        this.c50000 = c50000;
    }

    public String getC51000() {
        return c51000;
    }

    public void setC51000(String c51000) {
        this.c51000 = c51000;
    }

    public String getC52000() {
        return c52000;
    }

    public void setC52000(String c52000) {
        this.c52000 = c52000;
    }

    public String getC53000() {
        return c53000;
    }

    public void setC53000(String c53000) {
        this.c53000 = c53000;
    }

    public String getC54000() {
        return c54000;
    }

    public void setC54000(String c54000) {
        this.c54000 = c54000;
    }

    public String getC60000() {
        return c60000;
    }

    public void setC60000(String c60000) {
        this.c60000 = c60000;
    }

    public String getC61000() {
        return c61000;
    }

    public void setC61000(String c61000) {
        this.c61000 = c61000;
    }

    public String getC62000() {
        return c62000;
    }

    public void setC62000(String c62000) {
        this.c62000 = c62000;
    }

    public String getC63000() {
        return c63000;
    }

    public void setC63000(String c63000) {
        this.c63000 = c63000;
    }

    public String getC64000() {
        return c64000;
    }

    public void setC64000(String c64000) {
        this.c64000 = c64000;
    }

    public String getC70000() {
        return c70000;
    }

    public void setC70000(String c70000) {
        this.c70000 = c70000;
    }

    public String getC71000() {
        return c71000;
    }

    public void setC71000(String c71000) {
        this.c71000 = c71000;
    }

    public String getC72000() {
        return c72000;
    }

    public void setC72000(String c72000) {
        this.c72000 = c72000;
    }

    public String getC73000() {
        return c73000;
    }

    public void setC73000(String c73000) {
        this.c73000 = c73000;
    }

    public String getC74000() {
        return c74000;
    }

    public void setC74000(String c74000) {
        this.c74000 = c74000;
    }

    public String getC80000() {
        return c80000;
    }

    public void setC80000(String c80000) {
        this.c80000 = c80000;
    }

    public String getC81000() {
        return c81000;
    }

    public void setC81000(String c81000) {
        this.c81000 = c81000;
    }

    public String getC82000() {
        return c82000;
    }

    public void setC82000(String c82000) {
        this.c82000 = c82000;
    }

    public String getC83000() {
        return c83000;
    }

    public void setC83000(String c83000) {
        this.c83000 = c83000;
    }

    public String getC84000() {
        return c84000;
    }

    public void setC84000(String c84000) {
        this.c84000 = c84000;
    }

    public String getC90000() {
        return c90000;
    }

    public void setC90000(String c90000) {
        this.c90000 = c90000;
    }

    public String getC91000() {
        return c91000;
    }

    public void setC91000(String c91000) {
        this.c91000 = c91000;
    }

    public String getC92000() {
        return c92000;
    }

    public void setC92000(String c92000) {
        this.c92000 = c92000;
    }

    public String getC93000() {
        return c93000;
    }

    public void setC93000(String c93000) {
        this.c93000 = c93000;
    }

    public String getC94000() {
        return c94000;
    }

    public void setC94000(String c94000) {
        this.c94000 = c94000;
    }

    public String getCa0000() {
        return ca0000;
    }

    public void setCa0000(String ca0000) {
        this.ca0000 = ca0000;
    }

    public String getCa1000() {
        return ca1000;
    }

    public void setCa1000(String ca1000) {
        this.ca1000 = ca1000;
    }

    public String getCa2000() {
        return ca2000;
    }

    public void setCa2000(String ca2000) {
        this.ca2000 = ca2000;
    }

    public String getCa3000() {
        return ca3000;
    }

    public void setCa3000(String ca3000) {
        this.ca3000 = ca3000;
    }

    public String getCa4000() {
        return ca4000;
    }

    public void setCa4000(String ca4000) {
        this.ca4000 = ca4000;
    }

    public String getCb0000() {
        return cb0000;
    }

    public void setCb0000(String cb0000) {
        this.cb0000 = cb0000;
    }

    public String getCb1000() {
        return cb1000;
    }

    public void setCb1000(String cb1000) {
        this.cb1000 = cb1000;
    }

    public String getCb2000() {
        return cb2000;
    }

    public void setCb2000(String cb2000) {
        this.cb2000 = cb2000;
    }

    public String getCb3000() {
        return cb3000;
    }

    public void setCb3000(String cb3000) {
        this.cb3000 = cb3000;
    }

    public String getCb4000() {
        return cb4000;
    }

    public void setCb4000(String cb4000) {
        this.cb4000 = cb4000;
    }

    public String getCc0000() {
        return cc0000;
    }

    public void setCc0000(String cc0000) {
        this.cc0000 = cc0000;
    }

    public String getCc1000() {
        return cc1000;
    }

    public void setCc1000(String cc1000) {
        this.cc1000 = cc1000;
    }

    public String getCc2000() {
        return cc2000;
    }

    public void setCc2000(String cc2000) {
        this.cc2000 = cc2000;
    }

    public String getCc3000() {
        return cc3000;
    }

    public void setCc3000(String cc3000) {
        this.cc3000 = cc3000;
    }

    public String getCc4000() {
        return cc4000;
    }

    public void setCc4000(String cc4000) {
        this.cc4000 = cc4000;
    }

    public String getCd0000() {
        return cd0000;
    }

    public void setCd0000(String cd0000) {
        this.cd0000 = cd0000;
    }

    public String getCd1000() {
        return cd1000;
    }

    public void setCd1000(String cd1000) {
        this.cd1000 = cd1000;
    }

    public String getCd2000() {
        return cd2000;
    }

    public void setCd2000(String cd2000) {
        this.cd2000 = cd2000;
    }

    public String getCd3000() {
        return cd3000;
    }

    public void setCd3000(String cd3000) {
        this.cd3000 = cd3000;
    }

    public String getCd4000() {
        return cd4000;
    }

    public void setCd4000(String cd4000) {
        this.cd4000 = cd4000;
    }

    public String getCe0000() {
        return ce0000;
    }

    public void setCe0000(String ce0000) {
        this.ce0000 = ce0000;
    }

    public String getCe1000() {
        return ce1000;
    }

    public void setCe1000(String ce1000) {
        this.ce1000 = ce1000;
    }

    public String getCe2000() {
        return ce2000;
    }

    public void setCe2000(String ce2000) {
        this.ce2000 = ce2000;
    }

    public String getCe3000() {
        return ce3000;
    }

    public void setCe3000(String ce3000) {
        this.ce3000 = ce3000;
    }

    public String getCe4000() {
        return ce4000;
    }

    public void setCe4000(String ce4000) {
        this.ce4000 = ce4000;
    }

    public String getCf0000() {
        return cf0000;
    }

    public void setCf0000(String cf0000) {
        this.cf0000 = cf0000;
    }

    public String getCf1000() {
        return cf1000;
    }

    public void setCf1000(String cf1000) {
        this.cf1000 = cf1000;
    }

    public String getCf2000() {
        return cf2000;
    }

    public void setCf2000(String cf2000) {
        this.cf2000 = cf2000;
    }

    public String getCf3000() {
        return cf3000;
    }

    public void setCf3000(String cf3000) {
        this.cf3000 = cf3000;
    }

    public String getCf4000() {
        return cf4000;
    }

    public void setCf4000(String cf4000) {
        this.cf4000 = cf4000;
    }

    public String getCg0000() {
        return cg0000;
    }

    public void setCg0000(String cg0000) {
        this.cg0000 = cg0000;
    }

    public String getCg1000() {
        return cg1000;
    }

    public void setCg1000(String cg1000) {
        this.cg1000 = cg1000;
    }

    public String getCg2000() {
        return cg2000;
    }

    public void setCg2000(String cg2000) {
        this.cg2000 = cg2000;
    }

    public String getCg3000() {
        return cg3000;
    }

    public void setCg3000(String cg3000) {
        this.cg3000 = cg3000;
    }

    public String getCg4000() {
        return cg4000;
    }

    public void setCg4000(String cg4000) {
        this.cg4000 = cg4000;
    }

    public String getCh0000() {
        return ch0000;
    }

    public void setCh0000(String ch0000) {
        this.ch0000 = ch0000;
    }

    public String getCh1000() {
        return ch1000;
    }

    public void setCh1000(String ch1000) {
        this.ch1000 = ch1000;
    }

    public String getCh2000() {
        return ch2000;
    }

    public void setCh2000(String ch2000) {
        this.ch2000 = ch2000;
    }

    public String getCh3000() {
        return ch3000;
    }

    public void setCh3000(String ch3000) {
        this.ch3000 = ch3000;
    }

    public String getCh4000() {
        return ch4000;
    }

    public void setCh4000(String ch4000) {
        this.ch4000 = ch4000;
    }

    public String getCi0000() {
        return ci0000;
    }

    public void setCi0000(String ci0000) {
        this.ci0000 = ci0000;
    }

    public String getCi1000() {
        return ci1000;
    }

    public void setCi1000(String ci1000) {
        this.ci1000 = ci1000;
    }

    public String getCi2000() {
        return ci2000;
    }

    public void setCi2000(String ci2000) {
        this.ci2000 = ci2000;
    }

    public String getCi3000() {
        return ci3000;
    }

    public void setCi3000(String ci3000) {
        this.ci3000 = ci3000;
    }

    public String getCi4000() {
        return ci4000;
    }

    public void setCi4000(String ci4000) {
        this.ci4000 = ci4000;
    }

    public String getCj0000() {
        return cj0000;
    }

    public void setCj0000(String cj0000) {
        this.cj0000 = cj0000;
    }

    public String getCj1000() {
        return cj1000;
    }

    public void setCj1000(String cj1000) {
        this.cj1000 = cj1000;
    }

    public String getCj2000() {
        return cj2000;
    }

    public void setCj2000(String cj2000) {
        this.cj2000 = cj2000;
    }

    public String getCj3000() {
        return cj3000;
    }

    public void setCj3000(String cj3000) {
        this.cj3000 = cj3000;
    }

    public String getCj4000() {
        return cj4000;
    }

    public void setCj4000(String cj4000) {
        this.cj4000 = cj4000;
    }
}
