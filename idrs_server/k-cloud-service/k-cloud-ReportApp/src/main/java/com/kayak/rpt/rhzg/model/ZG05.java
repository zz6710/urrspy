package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//资管产品资产负债信息
@Data
@GraphQLModel(fetcher = "ZG05Service",table = "app_pbc_report_zg05")
public class ZG05 {

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
    private String id;
    @ExcelProperty(value = "产品代码_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd like '%$U{prodCd}%'" ,field = "prod_cd")
    private String prodCd;
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
    @ExcelProperty(value = "币种_资管05表")
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "trans_ccy = $S{transCcy}" ,field = "trans_ccy")
    private String transCcy;
    @ExcelProperty(value = "数据类型")
    @GraphQLField(kkhtml = "KFieldText", label = "数据类型", sql = "data_typ = $S{dataTyp}" ,field = "data_typ")
    private String dataTyp;
    @ExcelProperty(value = "A0000_资产合计")
    @GraphQLField(kkhtml = "KFieldText", label = "A0000_资产合计", sql = "a0000 = $S{a0000}" ,field = "a0000")
    private String a0000;
    @ExcelProperty(value = "A1000_现金")
    @GraphQLField(kkhtml = "KFieldText", label = "A1000_现金", sql = "a1000 = $S{a1000}" ,field = "a1000")
    private String a1000;
    @ExcelProperty(value = "A2000_存款")
    @GraphQLField(kkhtml = "KFieldText", label = "A2000_存款", sql = "a2000 = $S{a2000}" ,field = "a2000")
    private String a2000;
    @ExcelProperty(value = "A2100_境内存款")
    @GraphQLField(kkhtml = "KFieldText", label = "A2100_境内存款", sql = "a2100 = $S{a2100}" ,field = "a2100")
    private String a2100;
    @ExcelProperty(value = "A2110_活期存款")
    @GraphQLField(kkhtml = "KFieldText", label = "A2110_活期存款", sql = "a2110 = $S{a2110}" ,field = "a2110")
    private String a2110;
    @ExcelProperty(value = "A2120_定期和其他存款")
    @GraphQLField(kkhtml = "KFieldText", label = "A2120_定期和其他存款", sql = "a2120 = $S{a2120}" ,field = "a2120")
    private String a2120;
    @ExcelProperty(value = "A2200_境外存款")
    @GraphQLField(kkhtml = "KFieldText", label = "A2200_境外存款", sql = "a2200 = $S{a2200}" ,field = "a2200")
    private String a2200;
    @ExcelProperty(value = "A2210_活期存款")
    @GraphQLField(kkhtml = "KFieldText", label = "A2210_活期存款", sql = "a2210 = $S{a2210}" ,field = "a2210")
    private String a2210;
    @ExcelProperty(value = "A2220_定期和其他存款")
    @GraphQLField(kkhtml = "KFieldText", label = "A2220_定期和其他存款", sql = "a2220 = $S{a2220}" ,field = "a2220")
    private String a2220;
    @ExcelProperty(value = "A3000_存单")
    @GraphQLField(kkhtml = "KFieldText", label = "A3000_存单", sql = "a3000 = $S{a3000}" ,field = "a3000")
    private String a3000;
    @ExcelProperty(value = "A3100_同业存单")
    @GraphQLField(kkhtml = "KFieldText", label = "A3100_同业存单", sql = "a3100 = $S{a3100}" ,field = "a3100")
    private String a3100;
    @ExcelProperty(value = "A3200_大额存单")
    @GraphQLField(kkhtml = "KFieldText", label = "A3200_大额存单", sql = "a3200 = $S{a3200}" ,field = "a3200")
    private String a3200;
    @ExcelProperty(value = "A4000_债务证券")
    @GraphQLField(kkhtml = "KFieldText", label = "A4000_债务证券", sql = "a4000 = $S{a4000}" ,field = "a4000")
    private String a4000;
    @ExcelProperty(value = "A4100_政府债券")
    @GraphQLField(kkhtml = "KFieldText", label = "A4100_政府债券", sql = "a4100 = $S{a4100}" ,field = "a4100")
    private String a4100;
    @ExcelProperty(value = "A4200_中央银行票据")
    @GraphQLField(kkhtml = "KFieldText", label = "A4200_中央银行票据", sql = "a4200 = $S{a4200}" ,field = "a4200")
    private String a4200;
    @ExcelProperty(value = "A4900_银行业存款类金融机构债券")
    @GraphQLField(kkhtml = "KFieldText", label = "A4900_银行业存款类金融机构债券", sql = "a4900 = $S{a4900}" ,field = "a4900")
    private String a4900;
    @ExcelProperty(value = "A4910_其中：政策性金融债券")
    @GraphQLField(kkhtml = "KFieldText", label = "A4910_其中：政策性金融债券", sql = "a4910 = $S{a4910}" ,field = "a4910")
    private String a4910;
    @ExcelProperty(value = "A4a00_政府支持机构债券（金融）")
    @GraphQLField(kkhtml = "KFieldText", label = "A4a00_政府支持机构债券(金融）", sql = "a4a00 = $S{a4a00}" ,field = "a4a00")
    private String a4a00;
    @ExcelProperty(value = "A4400_其他金融债券")
    @GraphQLField(kkhtml = "KFieldText", label = "A4400_其他金融债券", sql = "a4400 = $S{a4400}" ,field = "a4400")
    private String a4400;
    @ExcelProperty(value = "A4500_非金融企业债券")
    @GraphQLField(kkhtml = "KFieldText", label = "A4500_非金融企业债券", sql = "a4500 = $S{a4500}" ,field = "a4500")
    private String a4500;
    @ExcelProperty(value = "A4600_票据")
    @GraphQLField(kkhtml = "KFieldText", label = "A4600_票据", sql = "a4600 = $S{a4600}" ,field = "a4600")
    private String a4600;
    @ExcelProperty(value = "A4700_资产支持证券")
    @GraphQLField(kkhtml = "KFieldText", label = "A4700_资产支持证券", sql = "a4700 = $S{a4700}" ,field = "a4700")
    private String a4700;
    @ExcelProperty(value = "A4800_境外机构债券")
    @GraphQLField(kkhtml = "KFieldText", label = "A4800_境外机构债券", sql = "a4800 = $S{a4800}" ,field = "a4800")
    private String a4800;
    @ExcelProperty(value = "A5000_贷款")
    @GraphQLField(kkhtml = "KFieldText", label = "A5000_贷款", sql = "a5000 = $S{a5000}" ,field = "a5000")
    private String a5000;
    @ExcelProperty(value = "A5100_除回购和拆借外贷款")
    @GraphQLField(kkhtml = "KFieldText", label = "A5100_除回购和拆借外贷款", sql = "a5100 = $S{a5100}" ,field = "a5100")
    private String a5100;
    @ExcelProperty(value = "A5200_回购和拆借（含借款）")
    @GraphQLField(kkhtml = "KFieldText", label = "A5200_回购和拆借（含借款）", sql = "a5200 = $S{a5200}" ,field = "a5200")
    private String a5200;
    @ExcelProperty(value = "A5210_住户")
    @GraphQLField(kkhtml = "KFieldText", label = "A5210_住户", sql = "a5210 = $S{a5210}" ,field = "a5210")
    private String a5210;
    @ExcelProperty(value = "A5220_广义政府")
    @GraphQLField(kkhtml = "KFieldText", label = "A5220_广义政府", sql = "a5220 = $S{a5220}" ,field = "a5220")
    private String a5220;
    @ExcelProperty(value = "A5230_非金融企业")
    @GraphQLField(kkhtml = "KFieldText", label = "A5230_非金融企业", sql = "a5230 = $S{a5230}" ,field = "a5230")
    private String a5230;
    @ExcelProperty(value = "A5240_银行业存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "A5240_银行业存款类金融机构", sql = "a5240 = $S{a5240}" ,field = "a5240")
    private String a5240;
    @ExcelProperty(value = "A5250_银行业非存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "A5250_银行业非存款类金融机构", sql = "a5250 = $S{a5250}" ,field = "a5250")
    private String a5250;
    @ExcelProperty(value = "A5260_非银行业金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "A5260_非银行业金融机构", sql = "a5260 = $S{a5260}" ,field = "a5260")
    private String a5260;
    @ExcelProperty(value = "A5261_其中：中央交易对手方")
    @GraphQLField(kkhtml = "KFieldText", label = "A5261_其中：中央交易对手方", sql = "a5261 = $S{a5261}" ,field = "a5261")
    private String a5261;
    @ExcelProperty(value = "A5270_特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "A5270_特定目的载体", sql = "a5270 = $S{a5270}" ,field = "a5270")
    private String a5270;
    @ExcelProperty(value = "A5271_银行非保本理财")
    @GraphQLField(kkhtml = "KFieldText", label = "A5271_银行非保本理财", sql = "a5271 = $S{a5271}" ,field = "a5271")
    private String a5271;
    @ExcelProperty(value = "A5272_信托公司信托产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A5272_信托公司信托产品", sql = "a5272 = $S{a5272}" ,field = "a5272")
    private String a5272;
    @ExcelProperty(value = "A5273_证券公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A5273_证券公司及其子公司资管产品", sql = "a5273 = $S{a5273}" ,field = "a5273")
    private String a5273;
    @ExcelProperty(value = "A5274_基金管理公司及其子公司专户")
    @GraphQLField(kkhtml = "KFieldText", label = "A5274_基金管理公司及其子公司专户", sql = "a5274 = $S{a5274}" ,field = "a5274")
    private String a5274;
    @ExcelProperty(value = "A5275_期货公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A5275_期货公司及其子公司资管产品", sql = "a5275 = $S{a5275}" ,field = "a5275")
    private String a5275;
    @ExcelProperty(value = "A5276_保险资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A5276_保险资管产品", sql = "a5276 = $S{a5276}" ,field = "a5276")
    private String a5276;
    @ExcelProperty(value = "A5277_金融资产投资公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A5277_金融资产投资公司资管产品", sql = "a5277 = $S{a5277}" ,field = "a5277")
    private String a5277;
    @ExcelProperty(value = "A5278_公募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "A5278_公募基金", sql = "a5278 = $S{a5278}" ,field = "a5278")
    private String a5278;
    @ExcelProperty(value = "A5279_私募机构私募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "A5279_私募机构私募基金", sql = "a5279 = $S{a5279}" ,field = "a5279")
    private String a5279;
    @ExcelProperty(value = "A527a_其他特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "A527a_其他特定目的载体", sql = "a527a = $S{a527a}" ,field = "a527a")
    private String a527a;
    @ExcelProperty(value = "A5280_境外")
    @GraphQLField(kkhtml = "KFieldText", label = "A5280_境外", sql = "a5280 = $S{a5280}" ,field = "a5280")
    private String a5280;
    @ExcelProperty(value = "AD000_其他债权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD000_其他债权", sql = "ad000 = $S{ad000}" ,field = "ad000")
    private String ad000;
    @ExcelProperty(value = "AD100_资产收益权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD100_资产收益权", sql = "ad100 = $S{ad100}" ,field = "ad100")
    private String ad100;
    @ExcelProperty(value = "AD110_贷款收益权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD110_贷款收益权", sql = "ad110 = $S{ad110}" ,field = "ad110")
    private String ad110;
    @ExcelProperty(value = "AD120_债务证券收益权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD120_债务证券收益权", sql = "ad120 = $S{ad120}" ,field = "ad120")
    private String ad120;
    @ExcelProperty(value = "AD130_应收账款收益权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD130_应收账款收益权", sql = "ad130 = $S{ad130}" ,field = "ad130")
    private String ad130;
    @ExcelProperty(value = "AD140_其他债权收益权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD140_其他债权收益权", sql = "ad140 = $S{ad140}" ,field = "ad140")
    private String ad140;
    @ExcelProperty(value = "AD150_股票收益权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD150_股票收益权", sql = "ad150 = $S{ad150}" ,field = "ad150")
    private String ad150;
    @ExcelProperty(value = "AD160_其他股权收益权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD160_其他股权收益权", sql = "ad160 = $S{ad160}" ,field = "ad160")
    private String ad160;
    @ExcelProperty(value = "AD170_物权资产收益权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD170_物权资产收益权", sql = "ad170 = $S{ad170}" ,field = "ad170")
    private String ad170;
    @ExcelProperty(value = "AD200_除资产收益权外其他债权")
    @GraphQLField(kkhtml = "KFieldText", label = "AD200_除资产收益权外其他债权", sql = "ad200 = $S{ad200}" ,field = "ad200")
    private String ad200;
    @ExcelProperty(value = "A7000_股权及特定目的载体份额")
    @GraphQLField(kkhtml = "KFieldText", label = "A7000_股权及特定目的载体份额", sql = "a7000 = $S{a7000}" ,field = "a7000")
    private String a7000;
    @ExcelProperty(value = "A7100_股票")
    @GraphQLField(kkhtml = "KFieldText", label = "A7100_股票", sql = "a7100 = $S{a7100}" ,field = "a7100")
    private String a7100;
    @ExcelProperty(value = "A7110_非金融企业股票")
    @GraphQLField(kkhtml = "KFieldText", label = "A7110_非金融企业股票", sql = "a7110 = $S{a7110}" ,field = "a7110")
    private String a7110;
    @ExcelProperty(value = "A7120_金融机构股票")
    @GraphQLField(kkhtml = "KFieldText", label = "A7120_金融机构股票", sql = "a7120 = $S{a7120}" ,field = "a7120")
    private String a7120;
    @ExcelProperty(value = "A7200_特定目的载体份额")
    @GraphQLField(kkhtml = "KFieldText", label = "A7200_特定目的载体份额", sql = "a7200 = $S{a7200}" ,field = "a7200")
    private String a7200;
    @ExcelProperty(value = "A7210_银行非保本理财")
    @GraphQLField(kkhtml = "KFieldText", label = "A7210_银行非保本理财", sql = "a7210 = $S{a7210}" ,field = "a7210")
    private String a7210;
    @ExcelProperty(value = "A7220_信托公司信托产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A7220_信托公司信托产品", sql = "a7220 = $S{a7220}" ,field = "a7220")
    private String a7220;
    @ExcelProperty(value = "A7230_证券公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A7230_证券公司及其子公司资管产品", sql = "a7230 = $S{a7230}" ,field = "a7230")
    private String a7230;
    @ExcelProperty(value = "A7240_基金管理公司及其子公司专户")
    @GraphQLField(kkhtml = "KFieldText", label = "A7240_基金管理公司及其子公司专户", sql = "a7240 = $S{a7240}" ,field = "a7240")
    private String a7240;
    @ExcelProperty(value = "A7250_期货公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A7250_期货公司及其子公司资管产品", sql = "a7250 = $S{a7250}" ,field = "a7250")
    private String a7250;
    @ExcelProperty(value = "A7260_保险资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A7260_保险资管产品", sql = "a7260 = $S{a7260}" ,field = "a7260")
    private String a7260;
    @ExcelProperty(value = "A7270_金融资产投资公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "A7270_金融资产投资公司资管产品", sql = "a7270 = $S{a7270}" ,field = "a7270")
    private String a7270;
    @ExcelProperty(value = "A7280_公募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "A7280_公募基金", sql = "a7280 = $S{a7280}" ,field = "a7280")
    private String a7280;
    @ExcelProperty(value = "A7290_私募机构私募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "A7290_私募机构私募基金", sql = "a7290 = $S{a7290}" ,field = "a7290")
    private String a7290;
    @ExcelProperty(value = "A72a0_其他特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "A72a0_其他特定目的载体", sql = "a72a0 = $S{a72a0}" ,field = "a72a0")
    private String a72a0;
    @ExcelProperty(value = "A7300_其他股权")
    @GraphQLField(kkhtml = "KFieldText", label = "A7300_其他股权", sql = "a7300 = $S{a7300}" ,field = "a7300")
    private String a7300;
    @ExcelProperty(value = "A7310_非金融企业股权")
    @GraphQLField(kkhtml = "KFieldText", label = "A7310_非金融企业股权", sql = "a7310 = $S{a7310}" ,field = "a7310")
    private String a7310;
    @ExcelProperty(value = "A7320_金融机构股权")
    @GraphQLField(kkhtml = "KFieldText", label = "A7320_金融机构股权", sql = "a7320 = $S{a7320}" ,field = "a7320")
    private String a7320;
    @ExcelProperty(value = "A7400_境外")
    @GraphQLField(kkhtml = "KFieldText", label = "A7400_境外", sql = "a7400 = $S{a7400}" ,field = "a7400")
    private String a7400;
    @ExcelProperty(value = "A8000_金融衍生工具")
    @GraphQLField(kkhtml = "KFieldText", label = "A8000_金融衍生品工具", sql = "a8000 = $S{a8000}" ,field = "a8000")
    private String a8000;
    @ExcelProperty(value = "A8100_境内")
    @GraphQLField(kkhtml = "KFieldText", label = "A8100_境内", sql = "a8100 = $S{a8100}" ,field = "a8100")
    private String a8100;
    @ExcelProperty(value = "A8200_境外")
    @GraphQLField(kkhtml = "KFieldText", label = "A8200_境外", sql = "a8200 = $S{a8200}" ,field = "a8200")
    private String a8200;
    @ExcelProperty(value = "A9000_应收账款")
    @GraphQLField(kkhtml = "KFieldText", label = "A9000_应收账款", sql = "a9000 = $S{a9000}" ,field = "a9000")
    private String a9000;
    @ExcelProperty(value = "AA000_黄金")
    @GraphQLField(kkhtml = "KFieldText", label = "AA000_黄金", sql = "aa000 = $S{aa000}" ,field = "aa000")
    private String aa000;
    @ExcelProperty(value = "AB000_非金融资产")
    @GraphQLField(kkhtml = "KFieldText", label = "AB000_非金融资产", sql = "ab000 = $S{ab000}" ,field = "ab000")
    private String ab000;
    @ExcelProperty(value = "AC000_资产减值准备（减）")
    @GraphQLField(kkhtml = "KFieldText", label = "AC000_资产减值准备（减）", sql = "ac000 = $S{ac000}" ,field = "ac000")
    private String ac000;
    @ExcelProperty(value = "D0000_负债及权益合计")
    @GraphQLField(kkhtml = "KFieldText", label = "D0000_负债及权益合计", sql = "d0000 = $S{d0000}" ,field = "d0000")
    private String d0000;
    @ExcelProperty(value = "B0000_负债合计")
    @GraphQLField(kkhtml = "KFieldText", label = "B0000_负债合计", sql = "b0000 = $S{b0000}" ,field = "b0000")
    private String b0000;
    @ExcelProperty(value = "B1000_贷款")
    @GraphQLField(kkhtml = "KFieldText", label = "B1000_贷款", sql = "b1000 = $S{b1000}" ,field = "b1000")
    private String b1000;
    @ExcelProperty(value = "B1100_除回购和拆借外贷款")
    @GraphQLField(kkhtml = "KFieldText", label = "B1100_除回购和拆借外贷款", sql = "b1100 = $S{b1100}" ,field = "b1100")
    private String b1100;
    @ExcelProperty(value = "B1200_回购和拆借（含借款）")
    @GraphQLField(kkhtml = "KFieldText", label = "B1200_回购和拆借(含借款)", sql = "b1200 = $S{b1200}" ,field = "b1200")
    private String b1200;
    @ExcelProperty(value = "B1210_住户")
    @GraphQLField(kkhtml = "KFieldText", label = "B1210_住户", sql = "b1210 = $S{b1210}" ,field = "b1210")
    private String b1210;
    @ExcelProperty(value = "B1220_广义政府")
    @GraphQLField(kkhtml = "KFieldText", label = "B1220_广义政府", sql = "b1220 = $S{b1220}" ,field = "b1220")
    private String b1220;
    @ExcelProperty(value = "B1230_非金融企业")
    @GraphQLField(kkhtml = "KFieldText", label = "B1230_非金融企业", sql = "b1230 = $S{b1230}" ,field = "b1230")
    private String b1230;
    @ExcelProperty(value = "B1240_银行业存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "B1240_银行业存款类金融机构", sql = "b1240 = $S{b1240}" ,field = "b1240")
    private String b1240;
    @ExcelProperty(value = "B1250_银行业非存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "B1250_银行业非存款类金融机构", sql = "b1250 = $S{b1250}" ,field = "b1250")
    private String b1250;
    @ExcelProperty(value = "B1260_非银行业金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "B1260_非银行业金融机构", sql = "b1260 = $S{b1260}" ,field = "b1260")
    private String b1260;
    @ExcelProperty(value = "B1261_其中：中央交易对手方")
    @GraphQLField(kkhtml = "KFieldText", label = "B1261_其中：中央交易对手方", sql = "b1261 = $S{b1261}" ,field = "b1261")
    private String b1261;
    @ExcelProperty(value = "B1270_特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "B1270_特定目的载体", sql = "b1270 = $S{b1270}" ,field = "b1270")
    private String b1270;
    @ExcelProperty(value = "B1271_银行非保本理财")
    @GraphQLField(kkhtml = "KFieldText", label = "B1271_银行非保本理财", sql = "b1271 = $S{b1271}" ,field = "b1271")
    private String b1271;
    @ExcelProperty(value = "B1272_信托公司信托产品")
    @GraphQLField(kkhtml = "KFieldText", label = "B1272_信托公司资管产品", sql = "b1272 = $S{b1272}" ,field = "b1272")
    private String b1272;
    @ExcelProperty(value = "B1273_证券公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "B1273_证券公司及其子公司资管产品", sql = "b1273 = $S{b1273}" ,field = "b1273")
    private String b1273;
    @ExcelProperty(value = "B1274_基金管理公司及其子公司专户")
    @GraphQLField(kkhtml = "KFieldText", label = "B1274_基金管理公司及其子公司专户", sql = "b1274 = $S{b1274}" ,field = "b1274")
    private String b1274;
    @ExcelProperty(value = "B1275_期货公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "B1275_期货公司及其子公司资管产品", sql = "b1275 = $S{b1275}" ,field = "b1275")
    private String b1275;
    @ExcelProperty(value = "B1276_保险资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "B1276_保险资管产品", sql = "b1276 = $S{b1276}" ,field = "b1276")
    private String b1276;
    @ExcelProperty(value = "B1277_金融资产投资公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "B1277_金融资产投资公司资管产品", sql = "b1277 = $S{b1277}" ,field = "b1277")
    private String b1277;
    @ExcelProperty(value = "B1278_公募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "B1278_公募基金", sql = "b1278 = $S{b1278}" ,field = "b1278")
    private String b1278;
    @ExcelProperty(value = "B1279_私募机构私募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "B1279_私募机构私募基金", sql = "b1279 = $S{b1279}" ,field = "b1279")
    private String b1279;
    @ExcelProperty(value = "B127a_其他特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "B127a_其他特定目的载体", sql = "b127a = $S{b127a}" ,field = "b127a")
    private String b127a;
    @ExcelProperty(value = "B1280_境外")
    @GraphQLField(kkhtml = "KFieldText", label = "B1280_境外", sql = "b1280 = $S{b1280}" ,field = "b1280")
    private String b1280;
    @ExcelProperty(value = "B2000_资产支持证券")
    @GraphQLField(kkhtml = "KFieldText", label = "B2000_资产支持证券", sql = "b2000 = $S{b2000}" ,field = "b2000")
    private String b2000;
    @ExcelProperty(value = "B3000_金融衍生工具")
    @GraphQLField(kkhtml = "KFieldText", label = "B3000_金融衍生品工具", sql = "b3000 = $S{b3000}" ,field = "b3000")
    private String b3000;
    @ExcelProperty(value = "B3100_境内")
    @GraphQLField(kkhtml = "KFieldText", label = "B3100_境内", sql = "b3100 = $S{b3100}" ,field = "b3100")
    private String b3100;
    @ExcelProperty(value = "B3200_境外")
    @GraphQLField(kkhtml = "KFieldText", label = "B3200_境外", sql = "b3200 = $S{b3200}" ,field = "b3200")
    private String b3200;
    @ExcelProperty(value = "B4000_应付账款")
    @GraphQLField(kkhtml = "KFieldText", label = "B4000_应付账款", sql = "b4000 = $S{b4000}" ,field = "b4000")
    private String b4000;
    @ExcelProperty(value = "B5000_其他负债")
    @GraphQLField(kkhtml = "KFieldText", label = "B5000_其他负债", sql = "b5000 = $S{b5000}" ,field = "b5000")
    private String b5000;
    @ExcelProperty(value = "C0000_权益合计")
    @GraphQLField(kkhtml = "KFieldText", label = "C0000_权益合计", sql = "c0000 = $S{c0000}" ,field = "c0000")
    private String c0000;
    @ExcelProperty(value = "C1000_实收本金")
    @GraphQLField(kkhtml = "KFieldText", label = "C1000_实收本金", sql = "c1000 = $S{c1000}" ,field = "c1000")
    private String c1000;
    @ExcelProperty(value = "C1100_优先级")
    @GraphQLField(kkhtml = "KFieldText", label = "C1100_优先级", sql = "c1100 = $S{c1100}" ,field = "c1100")
    private String c1100;
    @ExcelProperty(value = "C1110_住户")
    @GraphQLField(kkhtml = "KFieldText", label = "C1110_住户", sql = "c1110 = $S{c1110}" ,field = "c1110")
    private String c1110;
    @ExcelProperty(value = "C1120_广义政府")
    @GraphQLField(kkhtml = "KFieldText", label = "C1120_广义政府", sql = "c1120 = $S{c1120}" ,field = "c1120")
    private String c1120;
    @ExcelProperty(value = "C1130_非金融企业")
    @GraphQLField(kkhtml = "KFieldText", label = "C1130_非金融企业", sql = "c1130 = $S{c1130}" ,field = "c1130")
    private String c1130;
    @ExcelProperty(value = "C1140_银行业存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "C1140_银行业存款类金融机构", sql = "c1140 = $S{c1140}" ,field = "c1140")
    private String c1140;
    @ExcelProperty(value = "C1150_银行业非存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "C1150_银行业非存款类金融机构", sql = "c1150 = $S{c1150}" ,field = "c1150")
    private String c1150;
    @ExcelProperty(value = "C1160_非银行业金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "C1160_非银行业金融机构", sql = "c1160 = $S{c1160}" ,field = "c1160")
    private String c1160;
    @ExcelProperty(value = "C1170_特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "C1170_特定目的载体", sql = "c1170 = $S{c1170}" ,field = "c1170")
    private String c1170;
    @ExcelProperty(value = "C1171_银行非保本理财")
    @GraphQLField(kkhtml = "KFieldText", label = "C1171_银行非保本理财", sql = "c1171 = $S{c1171}" ,field = "c1171")
    private String c1171;
    @ExcelProperty(value = "C1172_信托公司信托产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1172_信托公司信托产品", sql = "c1172 = $S{c1172}" ,field = "c1172")
    private String c1172;
    @ExcelProperty(value = "C1173_证券公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1173_证券公司及其子公司资管产品", sql = "c1173 = $S{c1173}" ,field = "c1173")
    private String c1173;
    @ExcelProperty(value = "C1174_基金管理公司及其子公司专户")
    @GraphQLField(kkhtml = "KFieldText", label = "C1174_基金管理公司及其子公司专户", sql = "c1174 = $S{c1174}" ,field = "c1174")
    private String c1174;
    @ExcelProperty(value = "C1175_期货公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1175_期货公司及其子公司资管产品", sql = "c1175 = $S{c1175}" ,field = "c1175")
    private String c1175;
    @ExcelProperty(value = "C1176_保险资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1176_保险资管产品", sql = "c1176 = $S{c1176}" ,field = "c1176")
    private String c1176;
    @ExcelProperty(value = "C1177_金融资产投资公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1177_金融资产投资公司资管产品", sql = "c1177 = $S{c1177}" ,field = "c1177")
    private String c1177;
    @ExcelProperty(value = "C1178_公募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "C1178_公募基金", sql = "c1178 = $S{c1178}" ,field = "c1178")
    private String c1178;
    @ExcelProperty(value = "C1179_私募机构私募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "C1179_私募机构私募基金", sql = "c1179 = $S{c1179}" ,field = "c1179")
    private String c1179;
    @ExcelProperty(value = "C117a_其他特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "C117a_其他特定目的载体", sql = "c117a = $S{c117a}" ,field = "c117a")
    private String c117a;
    @ExcelProperty(value = "C1180_境外")
    @GraphQLField(kkhtml = "KFieldText", label = "C1180_境外", sql = "c1180 = $S{c1180}" ,field = "c1180")
    private String c1180;
    @ExcelProperty(value = "C1200_劣后级")
    @GraphQLField(kkhtml = "KFieldText", label = "C1200_劣后级", sql = "c1200 = $S{c1200}" ,field = "c1200")
    private String c1200;
    @ExcelProperty(value = "C1210_住户")
    @GraphQLField(kkhtml = "KFieldText", label = "C1210_住户", sql = "c1210 = $S{c1210}" ,field = "c1210")
    private String c1210;
    @ExcelProperty(value = "C1220_广义政府")
    @GraphQLField(kkhtml = "KFieldText", label = "C1220_广义政府", sql = "c1220 = $S{c1220}" ,field = "c1220")
    private String c1220;
    @ExcelProperty(value = "C1230_非金融企业")
    @GraphQLField(kkhtml = "KFieldText", label = "C1230_非金融企业", sql = "c1230 = $S{c1230}" ,field = "c1230")
    private String c1230;
    @ExcelProperty(value = "C1240_银行业存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "C1240_银行业存款类金融机构", sql = "c1240 = $S{c1240}" ,field = "c1240")
    private String c1240;
    @ExcelProperty(value = "C1250_银行业非存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "C1250_银行业非存款类金融机构", sql = "c1250 = $S{c1250}" ,field = "c1250")
    private String c1250;
    @ExcelProperty(value = "C1260_非银行业金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "C1260_非银行业金融机构", sql = "c1260 = $S{c1260}" ,field = "c1260")
    private String c1260;
    @ExcelProperty(value = "C1270_特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "C1270_特定目的载体", sql = "c1270 = $S{c1270}" ,field = "c1270")
    private String c1270;
    @ExcelProperty(value = "C1271_银行非保本理财")
    @GraphQLField(kkhtml = "KFieldText", label = "C1271_银行非保本理财", sql = "c1271 = $S{c1271}" ,field = "c1271")
    private String c1271;
    @ExcelProperty(value = "C1272_信托公司信托产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1272_信托公司信托产品", sql = "c1272 = $S{c1272}" ,field = "c1272")
    private String c1272;
    @ExcelProperty(value = "C1273_证券公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1273_证券公司及其子公司资管产品", sql = "c1273 = $S{c1273}" ,field = "c1273")
    private String c1273;
    @ExcelProperty(value = "C1274_基金管理公司及其子公司专户")
    @GraphQLField(kkhtml = "KFieldText", label = "C1274_基金管理公司及其子公司专户", sql = "c1274 = $S{c1274}" ,field = "c1274")
    private String c1274;
    @ExcelProperty(value = "C1275_期货公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1275_期货公司及其子公司资管产品", sql = "c1275 = $S{c1275}" ,field = "c1275")
    private String c1275;
    @ExcelProperty(value = "C1276_保险资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1276_保险资管产品", sql = "c1276 = $S{c1276}" ,field = "c1276")
    private String c1276;
    @ExcelProperty(value = "C1277_金融资产投资公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "C1277_金融资产投资公司资管产品", sql = "c1277 = $S{c1277}" ,field = "c1277")
    private String c1277;
    @ExcelProperty(value = "C1278_公募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "C1278_公募基金", sql = "c1278 = $S{c1278}" ,field = "c1278")
    private String c1278;
    @ExcelProperty(value = "C1279_私募机构私募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "C1279_私募机构私募基金", sql = "c1279 = $S{c1279}" ,field = "c1279")
    private String c1279;
    @ExcelProperty(value = "C127a_其他特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "C127a_其他特定目的载体", sql = "c127a = $S{c127a}" ,field = "c127a")
    private String c127a;
    @ExcelProperty(value = "C1280_境外")
    @GraphQLField(kkhtml = "KFieldText", label = "C1280_境外", sql = "c1280 = $S{c1280}" ,field = "c1280")
    private String c1280;
    @ExcelProperty(value = "C3000_未分配利润")
    @GraphQLField(kkhtml = "KFieldText", label = "C3000_未分配利润", sql = "c3000 = $S{c3000}" ,field = "c3000")
    private String c3000;
    @ExcelProperty(value = "C4000_其他综合收益")
    @GraphQLField(kkhtml = "KFieldText", label = "C4000_其他综合收益", sql = "c4000 = $S{c4000}" ,field = "c4000")
    private String c4000;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdCd() {
        return prodCd;
    }

    public void setProdCd(String prodCd) {
        this.prodCd = prodCd;
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

    public String getTransCcy() {
        return transCcy;
    }

    public void setTransCcy(String transCcy) {
        this.transCcy = transCcy;
    }

    public String getDataTyp() {
        return dataTyp;
    }

    public void setDataTyp(String dataTyp) {
        this.dataTyp = dataTyp;
    }

    public String getA0000() {
        return a0000;
    }

    public void setA0000(String a0000) {
        this.a0000 = a0000;
    }

    public String getA1000() {
        return a1000;
    }

    public void setA1000(String a1000) {
        this.a1000 = a1000;
    }

    public String getA2000() {
        return a2000;
    }

    public void setA2000(String a2000) {
        this.a2000 = a2000;
    }

    public String getA2100() {
        return a2100;
    }

    public void setA2100(String a2100) {
        this.a2100 = a2100;
    }

    public String getA2110() {
        return a2110;
    }

    public void setA2110(String a2110) {
        this.a2110 = a2110;
    }

    public String getA2120() {
        return a2120;
    }

    public void setA2120(String a2120) {
        this.a2120 = a2120;
    }

    public String getA2200() {
        return a2200;
    }

    public void setA2200(String a2200) {
        this.a2200 = a2200;
    }

    public String getA2210() {
        return a2210;
    }

    public void setA2210(String a2210) {
        this.a2210 = a2210;
    }

    public String getA2220() {
        return a2220;
    }

    public void setA2220(String a2220) {
        this.a2220 = a2220;
    }

    public String getA3000() {
        return a3000;
    }

    public void setA3000(String a3000) {
        this.a3000 = a3000;
    }

    public String getA3100() {
        return a3100;
    }

    public void setA3100(String a3100) {
        this.a3100 = a3100;
    }

    public String getA3200() {
        return a3200;
    }

    public void setA3200(String a3200) {
        this.a3200 = a3200;
    }

    public String getA4000() {
        return a4000;
    }

    public void setA4000(String a4000) {
        this.a4000 = a4000;
    }

    public String getA4100() {
        return a4100;
    }

    public void setA4100(String a4100) {
        this.a4100 = a4100;
    }

    public String getA4200() {
        return a4200;
    }

    public void setA4200(String a4200) {
        this.a4200 = a4200;
    }

    public String getA4900() {
        return a4900;
    }

    public void setA4900(String a4900) {
        this.a4900 = a4900;
    }

    public String getA4910() {
        return a4910;
    }

    public void setA4910(String a4910) {
        this.a4910 = a4910;
    }

    public String getA4a00() {
        return a4a00;
    }

    public void setA4a00(String a4a00) {
        this.a4a00 = a4a00;
    }

    public String getA4400() {
        return a4400;
    }

    public void setA4400(String a4400) {
        this.a4400 = a4400;
    }

    public String getA4500() {
        return a4500;
    }

    public void setA4500(String a4500) {
        this.a4500 = a4500;
    }

    public String getA4600() {
        return a4600;
    }

    public void setA4600(String a4600) {
        this.a4600 = a4600;
    }

    public String getA4700() {
        return a4700;
    }

    public void setA4700(String a4700) {
        this.a4700 = a4700;
    }

    public String getA4800() {
        return a4800;
    }

    public void setA4800(String a4800) {
        this.a4800 = a4800;
    }

    public String getA5000() {
        return a5000;
    }

    public void setA5000(String a5000) {
        this.a5000 = a5000;
    }

    public String getA5100() {
        return a5100;
    }

    public void setA5100(String a5100) {
        this.a5100 = a5100;
    }

    public String getA5200() {
        return a5200;
    }

    public void setA5200(String a5200) {
        this.a5200 = a5200;
    }

    public String getA5210() {
        return a5210;
    }

    public void setA5210(String a5210) {
        this.a5210 = a5210;
    }

    public String getA5220() {
        return a5220;
    }

    public void setA5220(String a5220) {
        this.a5220 = a5220;
    }

    public String getA5230() {
        return a5230;
    }

    public void setA5230(String a5230) {
        this.a5230 = a5230;
    }

    public String getA5240() {
        return a5240;
    }

    public void setA5240(String a5240) {
        this.a5240 = a5240;
    }

    public String getA5250() {
        return a5250;
    }

    public void setA5250(String a5250) {
        this.a5250 = a5250;
    }

    public String getA5260() {
        return a5260;
    }

    public void setA5260(String a5260) {
        this.a5260 = a5260;
    }

    public String getA5261() {
        return a5261;
    }

    public void setA5261(String a5261) {
        this.a5261 = a5261;
    }

    public String getA5270() {
        return a5270;
    }

    public void setA5270(String a5270) {
        this.a5270 = a5270;
    }

    public String getA5271() {
        return a5271;
    }

    public void setA5271(String a5271) {
        this.a5271 = a5271;
    }

    public String getA5272() {
        return a5272;
    }

    public void setA5272(String a5272) {
        this.a5272 = a5272;
    }

    public String getA5273() {
        return a5273;
    }

    public void setA5273(String a5273) {
        this.a5273 = a5273;
    }

    public String getA5274() {
        return a5274;
    }

    public void setA5274(String a5274) {
        this.a5274 = a5274;
    }

    public String getA5275() {
        return a5275;
    }

    public void setA5275(String a5275) {
        this.a5275 = a5275;
    }

    public String getA5276() {
        return a5276;
    }

    public void setA5276(String a5276) {
        this.a5276 = a5276;
    }

    public String getA5277() {
        return a5277;
    }

    public void setA5277(String a5277) {
        this.a5277 = a5277;
    }

    public String getA5278() {
        return a5278;
    }

    public void setA5278(String a5278) {
        this.a5278 = a5278;
    }

    public String getA5279() {
        return a5279;
    }

    public void setA5279(String a5279) {
        this.a5279 = a5279;
    }

    public String getA527a() {
        return a527a;
    }

    public void setA527a(String a527a) {
        this.a527a = a527a;
    }

    public String getA5280() {
        return a5280;
    }

    public void setA5280(String a5280) {
        this.a5280 = a5280;
    }

    public String getAd000() {
        return ad000;
    }

    public void setAd000(String ad000) {
        this.ad000 = ad000;
    }

    public String getAd100() {
        return ad100;
    }

    public void setAd100(String ad100) {
        this.ad100 = ad100;
    }

    public String getAd110() {
        return ad110;
    }

    public void setAd110(String ad110) {
        this.ad110 = ad110;
    }

    public String getAd120() {
        return ad120;
    }

    public void setAd120(String ad120) {
        this.ad120 = ad120;
    }

    public String getAd130() {
        return ad130;
    }

    public void setAd130(String ad130) {
        this.ad130 = ad130;
    }

    public String getAd140() {
        return ad140;
    }

    public void setAd140(String ad140) {
        this.ad140 = ad140;
    }

    public String getAd150() {
        return ad150;
    }

    public void setAd150(String ad150) {
        this.ad150 = ad150;
    }

    public String getAd160() {
        return ad160;
    }

    public void setAd160(String ad160) {
        this.ad160 = ad160;
    }

    public String getAd170() {
        return ad170;
    }

    public void setAd170(String ad170) {
        this.ad170 = ad170;
    }

    public String getAd200() {
        return ad200;
    }

    public void setAd200(String ad200) {
        this.ad200 = ad200;
    }

    public String getA7000() {
        return a7000;
    }

    public void setA7000(String a7000) {
        this.a7000 = a7000;
    }

    public String getA7100() {
        return a7100;
    }

    public void setA7100(String a7100) {
        this.a7100 = a7100;
    }

    public String getA7110() {
        return a7110;
    }

    public void setA7110(String a7110) {
        this.a7110 = a7110;
    }

    public String getA7120() {
        return a7120;
    }

    public void setA7120(String a7120) {
        this.a7120 = a7120;
    }

    public String getA7200() {
        return a7200;
    }

    public void setA7200(String a7200) {
        this.a7200 = a7200;
    }

    public String getA7210() {
        return a7210;
    }

    public void setA7210(String a7210) {
        this.a7210 = a7210;
    }

    public String getA7220() {
        return a7220;
    }

    public void setA7220(String a7220) {
        this.a7220 = a7220;
    }

    public String getA7230() {
        return a7230;
    }

    public void setA7230(String a7230) {
        this.a7230 = a7230;
    }

    public String getA7240() {
        return a7240;
    }

    public void setA7240(String a7240) {
        this.a7240 = a7240;
    }

    public String getA7250() {
        return a7250;
    }

    public void setA7250(String a7250) {
        this.a7250 = a7250;
    }

    public String getA7260() {
        return a7260;
    }

    public void setA7260(String a7260) {
        this.a7260 = a7260;
    }

    public String getA7270() {
        return a7270;
    }

    public void setA7270(String a7270) {
        this.a7270 = a7270;
    }

    public String getA7280() {
        return a7280;
    }

    public void setA7280(String a7280) {
        this.a7280 = a7280;
    }

    public String getA7290() {
        return a7290;
    }

    public void setA7290(String a7290) {
        this.a7290 = a7290;
    }

    public String getA72a0() {
        return a72a0;
    }

    public void setA72a0(String a72a0) {
        this.a72a0 = a72a0;
    }

    public String getA7300() {
        return a7300;
    }

    public void setA7300(String a7300) {
        this.a7300 = a7300;
    }

    public String getA7310() {
        return a7310;
    }

    public void setA7310(String a7310) {
        this.a7310 = a7310;
    }

    public String getA7320() {
        return a7320;
    }

    public void setA7320(String a7320) {
        this.a7320 = a7320;
    }

    public String getA7400() {
        return a7400;
    }

    public void setA7400(String a7400) {
        this.a7400 = a7400;
    }

    public String getA8000() {
        return a8000;
    }

    public void setA8000(String a8000) {
        this.a8000 = a8000;
    }

    public String getA8100() {
        return a8100;
    }

    public void setA8100(String a8100) {
        this.a8100 = a8100;
    }

    public String getA8200() {
        return a8200;
    }

    public void setA8200(String a8200) {
        this.a8200 = a8200;
    }

    public String getA9000() {
        return a9000;
    }

    public void setA9000(String a9000) {
        this.a9000 = a9000;
    }

    public String getAa000() {
        return aa000;
    }

    public void setAa000(String aa000) {
        this.aa000 = aa000;
    }

    public String getAb000() {
        return ab000;
    }

    public void setAb000(String ab000) {
        this.ab000 = ab000;
    }

    public String getAc000() {
        return ac000;
    }

    public void setAc000(String ac000) {
        this.ac000 = ac000;
    }

    public String getD0000() {
        return d0000;
    }

    public void setD0000(String d0000) {
        this.d0000 = d0000;
    }

    public String getB0000() {
        return b0000;
    }

    public void setB0000(String b0000) {
        this.b0000 = b0000;
    }

    public String getB1000() {
        return b1000;
    }

    public void setB1000(String b1000) {
        this.b1000 = b1000;
    }

    public String getB1100() {
        return b1100;
    }

    public void setB1100(String b1100) {
        this.b1100 = b1100;
    }

    public String getB1200() {
        return b1200;
    }

    public void setB1200(String b1200) {
        this.b1200 = b1200;
    }

    public String getB1210() {
        return b1210;
    }

    public void setB1210(String b1210) {
        this.b1210 = b1210;
    }

    public String getB1220() {
        return b1220;
    }

    public void setB1220(String b1220) {
        this.b1220 = b1220;
    }

    public String getB1230() {
        return b1230;
    }

    public void setB1230(String b1230) {
        this.b1230 = b1230;
    }

    public String getB1240() {
        return b1240;
    }

    public void setB1240(String b1240) {
        this.b1240 = b1240;
    }

    public String getB1250() {
        return b1250;
    }

    public void setB1250(String b1250) {
        this.b1250 = b1250;
    }

    public String getB1260() {
        return b1260;
    }

    public void setB1260(String b1260) {
        this.b1260 = b1260;
    }

    public String getB1261() {
        return b1261;
    }

    public void setB1261(String b1261) {
        this.b1261 = b1261;
    }

    public String getB1270() {
        return b1270;
    }

    public void setB1270(String b1270) {
        this.b1270 = b1270;
    }

    public String getB1271() {
        return b1271;
    }

    public void setB1271(String b1271) {
        this.b1271 = b1271;
    }

    public String getB1272() {
        return b1272;
    }

    public void setB1272(String b1272) {
        this.b1272 = b1272;
    }

    public String getB1273() {
        return b1273;
    }

    public void setB1273(String b1273) {
        this.b1273 = b1273;
    }

    public String getB1274() {
        return b1274;
    }

    public void setB1274(String b1274) {
        this.b1274 = b1274;
    }

    public String getB1275() {
        return b1275;
    }

    public void setB1275(String b1275) {
        this.b1275 = b1275;
    }

    public String getB1276() {
        return b1276;
    }

    public void setB1276(String b1276) {
        this.b1276 = b1276;
    }

    public String getB1277() {
        return b1277;
    }

    public void setB1277(String b1277) {
        this.b1277 = b1277;
    }

    public String getB1278() {
        return b1278;
    }

    public void setB1278(String b1278) {
        this.b1278 = b1278;
    }

    public String getB1279() {
        return b1279;
    }

    public void setB1279(String b1279) {
        this.b1279 = b1279;
    }

    public String getB127a() {
        return b127a;
    }

    public void setB127a(String b127a) {
        this.b127a = b127a;
    }

    public String getB1280() {
        return b1280;
    }

    public void setB1280(String b1280) {
        this.b1280 = b1280;
    }

    public String getB2000() {
        return b2000;
    }

    public void setB2000(String b2000) {
        this.b2000 = b2000;
    }

    public String getB3000() {
        return b3000;
    }

    public void setB3000(String b3000) {
        this.b3000 = b3000;
    }

    public String getB3100() {
        return b3100;
    }

    public void setB3100(String b3100) {
        this.b3100 = b3100;
    }

    public String getB3200() {
        return b3200;
    }

    public void setB3200(String b3200) {
        this.b3200 = b3200;
    }

    public String getB4000() {
        return b4000;
    }

    public void setB4000(String b4000) {
        this.b4000 = b4000;
    }

    public String getB5000() {
        return b5000;
    }

    public void setB5000(String b5000) {
        this.b5000 = b5000;
    }

    public String getC0000() {
        return c0000;
    }

    public void setC0000(String c0000) {
        this.c0000 = c0000;
    }

    public String getC1000() {
        return c1000;
    }

    public void setC1000(String c1000) {
        this.c1000 = c1000;
    }

    public String getC1100() {
        return c1100;
    }

    public void setC1100(String c1100) {
        this.c1100 = c1100;
    }

    public String getC1110() {
        return c1110;
    }

    public void setC1110(String c1110) {
        this.c1110 = c1110;
    }

    public String getC1120() {
        return c1120;
    }

    public void setC1120(String c1120) {
        this.c1120 = c1120;
    }

    public String getC1130() {
        return c1130;
    }

    public void setC1130(String c1130) {
        this.c1130 = c1130;
    }

    public String getC1140() {
        return c1140;
    }

    public void setC1140(String c1140) {
        this.c1140 = c1140;
    }

    public String getC1150() {
        return c1150;
    }

    public void setC1150(String c1150) {
        this.c1150 = c1150;
    }

    public String getC1160() {
        return c1160;
    }

    public void setC1160(String c1160) {
        this.c1160 = c1160;
    }

    public String getC1170() {
        return c1170;
    }

    public void setC1170(String c1170) {
        this.c1170 = c1170;
    }

    public String getC1171() {
        return c1171;
    }

    public void setC1171(String c1171) {
        this.c1171 = c1171;
    }

    public String getC1172() {
        return c1172;
    }

    public void setC1172(String c1172) {
        this.c1172 = c1172;
    }

    public String getC1173() {
        return c1173;
    }

    public void setC1173(String c1173) {
        this.c1173 = c1173;
    }

    public String getC1174() {
        return c1174;
    }

    public void setC1174(String c1174) {
        this.c1174 = c1174;
    }

    public String getC1175() {
        return c1175;
    }

    public void setC1175(String c1175) {
        this.c1175 = c1175;
    }

    public String getC1176() {
        return c1176;
    }

    public void setC1176(String c1176) {
        this.c1176 = c1176;
    }

    public String getC1177() {
        return c1177;
    }

    public void setC1177(String c1177) {
        this.c1177 = c1177;
    }

    public String getC1178() {
        return c1178;
    }

    public void setC1178(String c1178) {
        this.c1178 = c1178;
    }

    public String getC1179() {
        return c1179;
    }

    public void setC1179(String c1179) {
        this.c1179 = c1179;
    }

    public String getC117a() {
        return c117a;
    }

    public void setC117a(String c117a) {
        this.c117a = c117a;
    }

    public String getC1180() {
        return c1180;
    }

    public void setC1180(String c1180) {
        this.c1180 = c1180;
    }

    public String getC1200() {
        return c1200;
    }

    public void setC1200(String c1200) {
        this.c1200 = c1200;
    }

    public String getC1210() {
        return c1210;
    }

    public void setC1210(String c1210) {
        this.c1210 = c1210;
    }

    public String getC1220() {
        return c1220;
    }

    public void setC1220(String c1220) {
        this.c1220 = c1220;
    }

    public String getC1230() {
        return c1230;
    }

    public void setC1230(String c1230) {
        this.c1230 = c1230;
    }

    public String getC1240() {
        return c1240;
    }

    public void setC1240(String c1240) {
        this.c1240 = c1240;
    }

    public String getC1250() {
        return c1250;
    }

    public void setC1250(String c1250) {
        this.c1250 = c1250;
    }

    public String getC1260() {
        return c1260;
    }

    public void setC1260(String c1260) {
        this.c1260 = c1260;
    }

    public String getC1270() {
        return c1270;
    }

    public void setC1270(String c1270) {
        this.c1270 = c1270;
    }

    public String getC1271() {
        return c1271;
    }

    public void setC1271(String c1271) {
        this.c1271 = c1271;
    }

    public String getC1272() {
        return c1272;
    }

    public void setC1272(String c1272) {
        this.c1272 = c1272;
    }

    public String getC1273() {
        return c1273;
    }

    public void setC1273(String c1273) {
        this.c1273 = c1273;
    }

    public String getC1274() {
        return c1274;
    }

    public void setC1274(String c1274) {
        this.c1274 = c1274;
    }

    public String getC1275() {
        return c1275;
    }

    public void setC1275(String c1275) {
        this.c1275 = c1275;
    }

    public String getC1276() {
        return c1276;
    }

    public void setC1276(String c1276) {
        this.c1276 = c1276;
    }

    public String getC1277() {
        return c1277;
    }

    public void setC1277(String c1277) {
        this.c1277 = c1277;
    }

    public String getC1278() {
        return c1278;
    }

    public void setC1278(String c1278) {
        this.c1278 = c1278;
    }

    public String getC1279() {
        return c1279;
    }

    public void setC1279(String c1279) {
        this.c1279 = c1279;
    }

    public String getC127a() {
        return c127a;
    }

    public void setC127a(String c127a) {
        this.c127a = c127a;
    }

    public String getC1280() {
        return c1280;
    }

    public void setC1280(String c1280) {
        this.c1280 = c1280;
    }

    public String getC3000() {
        return c3000;
    }

    public void setC3000(String c3000) {
        this.c3000 = c3000;
    }

    public String getC4000() {
        return c4000;
    }

    public void setC4000(String c4000) {
        this.c4000 = c4000;
    }
}
