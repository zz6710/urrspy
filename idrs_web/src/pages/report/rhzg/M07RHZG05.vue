
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG05" data-target="ZG05Grid" v-model="prodSearchParam">
            <k-form-item label="数据日期">
              <k-field-date v-model="prodSearchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="产品代码_资管">
              <k-field-text v-model="prodSearchParam.prodCd" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="数据类型">
              <k-field-select v-model="prodSearchParam.dataTyp" data-dict='pbc_cuy_typ' data-dict-type="1"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG05Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG05')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG05Grid" :data-export-name="exportName('ZG05', '资管产品资产负债信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG05.findZG05s" data-export-form="searchRef">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
                <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </div>
          </div>
            <k-grid ref="ZG05Grid" @data-row-select="selectRow" data-action="ZG05.findZG05s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品代码_资管" data-name="prodCd" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="币种_资管05表" data-name="transCcy" data-width="80"></k-grid-column>
                <k-grid-column data-align="left" data-header="数据类型" data-name="dataTyp" data-dict="pbc_cuy_typ" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A0000_资产合计" data-name="a0000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A1000_现金" data-name="a1000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A2000_存款" data-name="a2000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A2100_境内存款" data-name="a2100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A2110_活期存款" data-name="a2110" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A2120_定期和其他存款" data-name="a2120" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A2200_境外存款" data-name="a2200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A2210_活期存款" data-name="a2210" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A2220_定期和其他存款" data-name="a2220" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A3000_存单" data-name="a3000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A3100_同业存单" data-name="a3100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A3200_大额存单" data-name="a3200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4000_债务证券" data-name="a4000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4100_政府债券" data-name="a4100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4200_中央银行票据" data-name="a4200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4900_银行业存款类金融机构债券" data-name="a4900" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4910_其中：政策性金融债券" data-name="a4910" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4a00_政府支持机构债券（金融）" data-name="a4a00" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4400_其他金融债券" data-name="a4400" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4500_非金融企业债券" data-name="a4500" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4600_票据" data-name="a4600" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4700_资产支持证券" data-name="a4700" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A4800_境外机构债券" data-name="a4800" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5000_贷款" data-name="a5000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5100_除回购和拆借外贷款" data-name="a5100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5200_回购和拆借（含借款）" data-name="a5200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5210_住户" data-name="a5210" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5220_广义政府" data-name="a5220" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5230_非金融企业" data-name="a5230" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5240_银行业存款类金融机构" data-name="a5240" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5250_银行业非存款类金融机构" data-name="a5250" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5260_非银行业金融机构" data-name="a5260" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5261_其中：中央交易对手方" data-name="a5261" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5270_特定目的载体" data-name="a5270" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5271_银行非保本理财" data-name="a5271" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5272_信托公司信托产品" data-name="a5272" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5273_证券公司及其子公司资管产品" data-name="a5273" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5274_基金管理公司及其子公司专户" data-name="a5274" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5275_期货公司及其子公司资管产品" data-name="a5275" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5276_保险资管产品" data-name="a5276" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5277_金融资产投资公司资管产品" data-name="a5277" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5278_公募基金" data-name="a5278" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5279_私募机构私募基金" data-name="a5279" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A527a_其他特定目的载体" data-name="a527a" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A5280_境外" data-name="a5280" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD000_其他债权" data-name="ad000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD100_资产收益权" data-name="ad100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD110_贷款收益权" data-name="ad110" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD120_债务证券收益权" data-name="ad120" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD130_应收账款收益权" data-name="ad130" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD140_其他债权收益权" data-name="ad140" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD150_股票收益权" data-name="ad150" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD160_其他股权收益权" data-name="ad160" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD170_物权资产收益权" data-name="ad170" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AD200_除资产收益权外其他债权" data-name="ad200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7000_股权及特定目的载体份额" data-name="a7000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7100_股票" data-name="a7100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7110_非金融企业股票" data-name="a7110" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7120_金融机构股票" data-name="a7120" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7200_特定目的载体份额" data-name="a7200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7210_银行非保本理财" data-name="a7210" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7220_信托公司信托产品" data-name="a7220" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7230_证券公司及其子公司资管产品" data-name="a7230" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7240_基金管理公司及其子公司专户" data-name="a7240" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7250_期货公司及其子公司资管产品" data-name="a7250" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7260_保险资管产品" data-name="a7260" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7270_金融资产投资公司资管产品" data-name="a7270" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7280_公募基金" data-name="a7280" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7290_私募机构私募基金" data-name="a7290" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A72a0_其他特定目的载体" data-name="a72a0" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7300_其他股权" data-name="a7300"  data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7310_非金融企业股权" data-name="a7310" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7320_金融机构股权" data-name="a7320" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A7400_境外" data-name="a7400" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A8000_金融衍生工具" data-name="a8000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A8100_境内" data-name="a8100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A8200_境外" data-name="a8200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="A9000_应收账款" data-name="a9000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AA000_黄金" data-name="aa000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AB000_非金融资产" data-name="ab000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="AC000_资产减值准备（减）" data-name="ac000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="D0000_负债及权益合计" data-name="d0000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B0000_负债合计" data-name="b0000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1000_贷款" data-name="b1000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1100_除回购和拆借外贷款" data-name="b1100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1200_回购和拆借（含借款）" data-name="b1200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1210_住户" data-name="b1210" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1220_广义政府" data-name="b1220" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1230_非金融企业" data-name="b1230" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1240_银行业存款类金融机构" data-name="b1240" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1250_银行业非存款类金融机构" data-name="b1250" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1260_非银行业金融机构" data-name="b1260" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1261_其中：中央交易对手方" data-name="b1261" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1270_特定目的载体" data-name="b1270" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1271_银行非保本理财" data-name="b1271" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1272_信托公司信托产品" data-name="b1272" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1273_证券公司及其子公司资管产品" data-name="b1273" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1274_基金管理公司及其子公司专户" data-name="b1274" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1275_期货公司及其子公司资管产品" data-name="b1275" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1276_保险资管产品" data-name="b1276" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1277_金融资产投资公司资管产品" data-name="b1277" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1278_公募基金" data-name="b1278" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1279_私募机构私募基金" data-name="b1279" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B127a_其他特定目的载体" data-name="b127a" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B1280_境外" data-name="b1280" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B2000_资产支持证券" data-name="b2000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B3000_金融衍生工具" data-name="b3000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B3100_境内" data-name="b3100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B3200_境外" data-name="b3200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B4000_应付账款" data-name="b4000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="B5000_其他负债" data-name="b5000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C0000_权益合计" data-name="c0000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1000_实收本金" data-name="c1000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1100_优先级" data-name="c1100" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1110_住户" data-name="c1110" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1120_广义政府" data-name="c1120" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1130_非金融企业" data-name="c1130" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1140_银行业存款类金融机构" data-name="c1140" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1150_银行业非存款类金融机构" data-name="c1150" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1160_非银行业金融机构" data-name="c1160" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1170_特定目的载体" data-name="c1170" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1171_银行非保本理财" data-name="c1171" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1172_信托公司信托产品" data-name="c1172" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1173_证券公司及其子公司资管产品" data-name="c1173" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1174_基金管理公司及其子公司专户" data-name="c1174" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1175_期货公司及其子公司资管产品" data-name="c1175" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1176_保险资管产品" data-name="c1176" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1177_金融资产投资公司资管产品" data-name="c1177" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1178_公募基金" data-name="c1178" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1179_私募机构私募基金" data-name="c1179" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C117a_其他特定目的载体" data-name="c117a" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1180_境外" data-name="c1180" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1200_劣后级" data-name="c1200" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1210_住户" data-name="c1210" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1220_广义政府" data-name="c1220" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1230_非金融企业" data-name="c1230" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1240_银行业存款类金融机构" data-name="c1240" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1250_银行业非存款类金融机构" data-name="c1250" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1260_非银行业金融机构" data-name="c1260" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1270_特定目的载体" data-name="c1270" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1271_银行非保本理财" data-name="c1271" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1272_信托公司信托产品" data-name="c1272" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1273_证券公司及其子公司资管产品" data-name="c1273" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1274_基金管理公司及其子公司专户" data-name="c1274" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1275_期货公司及其子公司资管产品" data-name="c1275" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1276_保险资管产品" data-name="c1276" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1277_金融资产投资公司资管产品" data-name="c1277" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1278_公募基金" data-name="c1278" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1279_私募机构私募基金" data-name="c1279" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C127a_其他特定目的载体" data-name="c127a" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C1280_境外" data-name="c1280" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C3000_未分配利润" data-name="c3000" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="C4000_其他综合收益" data-name="c4000" data-width="120"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG05Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG05.deleteZG05" data-size="mini"
                    data-type="danger" data-target="ZG05Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改资管产品资产负债信息弹出框   -->
          <k-popup ref="editZG05Popup" data-title="修改">
            <k-form ref="editZG05Form" :data-col="3" isFormBodyScreen>
                <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                    <k-field-date v-model="formData.reportDate" :data-allowblank="true" data-type="date" data-value-format="yyyyMMdd" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品代码_资管" :class="[handleItemDiff('prodCd')]">
                    <k-field-text v-model="formData.prodCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>

                <k-form-item label="币种_资管05表" :class="[handleItemDiff('transCcy')]">
                    <k-field-text v-model="formData.transCcy" :data-allowblank="false" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="数据类型" :class="[handleItemDiff('dataTyp')]">
                    <k-field-select v-model="formData.dataTyp" :data-allowblank="false" data-dict="pbc_cuy_typ" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                <k-form-item label="A0000_资产合计" :class="[handleItemDiff('a0000')]"><k-field-text v-model="formData.a0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A1000_现金" :class="[handleItemDiff('a1000')]"><k-field-text v-model="formData.a1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A2000_存款" :class="[handleItemDiff('a2000')]"><k-field-text v-model="formData.a2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A2100_境内存款" :class="[handleItemDiff('a2100')]"><k-field-text v-model="formData.a2100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A2110_活期存款" :class="[handleItemDiff('a2110')]"><k-field-text v-model="formData.a2110"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A2120_定期和其他存款" :class="[handleItemDiff('a2120')]"><k-field-text v-model="formData.a2120"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A2200_境外存款" :class="[handleItemDiff('a2200')]"><k-field-text v-model="formData.a2200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A2210_活期存款" :class="[handleItemDiff('a2210')]"><k-field-text v-model="formData.a2210"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A2220_定期和其他存款" :class="[handleItemDiff('a2220')]"><k-field-text v-model="formData.a2220"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A3000_存单" :class="[handleItemDiff('a3000')]"><k-field-text v-model="formData.a3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A3100_同业存单" :class="[handleItemDiff('a3100')]"><k-field-text v-model="formData.a3100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A3200_大额存单" :class="[handleItemDiff('a3200')]"><k-field-text v-model="formData.a3200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4000_债务证券" :class="[handleItemDiff('a4000')]"><k-field-text v-model="formData.a4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4100_政府债券" :class="[handleItemDiff('a4100')]"><k-field-text v-model="formData.a4100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4200_中央银行票据" :class="[handleItemDiff('a4200')]"><k-field-text v-model="formData.a4200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4900_银行业存款类金融机构债券" :class="[handleItemDiff('a4900')]"><k-field-text v-model="formData.a4900"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4910_其中：政策性金融债券" :class="[handleItemDiff('a4910')]"><k-field-text v-model="formData.a4910"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4a00_政府支持机构债券（金融）" :class="[handleItemDiff('a4a00')]"><k-field-text v-model="formData.a4a00"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4400_其他金融债券" :class="[handleItemDiff('a4400')]"><k-field-text v-model="formData.a4400"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4500_非金融企业债券" :class="[handleItemDiff('a4500')]"><k-field-text v-model="formData.a4500"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4600_票据" :class="[handleItemDiff('a4600')]"><k-field-text v-model="formData.a4600"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4700_资产支持证券" :class="[handleItemDiff('a4700')]"><k-field-text v-model="formData.a4700"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A4800_境外机构债券" :class="[handleItemDiff('a4800')]"><k-field-text v-model="formData.a4800"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5000_贷款" :class="[handleItemDiff('a5000')]"><k-field-text v-model="formData.a5000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5100_除回购和拆借外贷款" :class="[handleItemDiff('a5100')]"><k-field-text v-model="formData.a5100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5200_回购和拆借（含借款）" :class="[handleItemDiff('a5200')]"><k-field-text v-model="formData.a5200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5210_住户" :class="[handleItemDiff('a5210')]"><k-field-text v-model="formData.a5210"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5220_广义政府" :class="[handleItemDiff('a5220')]"><k-field-text v-model="formData.a5220"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5230_非金融企业" :class="[handleItemDiff('a5230')]"><k-field-text v-model="formData.a5230"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5240_银行业存款类金融机构" :class="[handleItemDiff('a5240')]"><k-field-text v-model="formData.a5240"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5250_银行业非存款类金融机构" :class="[handleItemDiff('a5250')]"><k-field-text v-model="formData.a5250"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5260_非银行业金融机构" :class="[handleItemDiff('a5260')]"><k-field-text v-model="formData.a5260"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5261_其中：中央交易对手方" :class="[handleItemDiff('a5261')]"><k-field-text v-model="formData.a5261"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5270_特定目的载体" :class="[handleItemDiff('a5270')]"><k-field-text v-model="formData.a5270"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5271_银行非保本理财" :class="[handleItemDiff('a5271')]"><k-field-text v-model="formData.a5271"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5272_信托公司信托产品" :class="[handleItemDiff('a5272')]"><k-field-text v-model="formData.a5272"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5273_证券公司及其子公司资管产品" :class="[handleItemDiff('a5273')]"><k-field-text v-model="formData.a5273"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5274_基金管理公司及其子公司专户" :class="[handleItemDiff('a5274')]"><k-field-text v-model="formData.a5274"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5275_期货公司及其子公司资管产品" :class="[handleItemDiff('a5275')]"><k-field-text v-model="formData.a5275"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5276_保险资管产品" :class="[handleItemDiff('a5276')]"><k-field-text v-model="formData.a5276"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5277_金融资产投资公司资管产品" :class="[handleItemDiff('a5277')]"><k-field-text v-model="formData.a5277"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5278_公募基金" :class="[handleItemDiff('a5278')]"><k-field-text v-model="formData.a5278"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5279_私募机构私募基金" :class="[handleItemDiff('a5279')]"><k-field-text v-model="formData.a5279"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A527a_其他特定目的载体" :class="[handleItemDiff('a527a')]"><k-field-text v-model="formData.a527a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A5280_境外" :class="[handleItemDiff('a5280')]"><k-field-text v-model="formData.a5280"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD000_其他债权" :class="[handleItemDiff('ad000')]"><k-field-text v-model="formData.ad000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD100_资产收益权" :class="[handleItemDiff('ad100')]"><k-field-text v-model="formData.ad100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD110_贷款收益权" :class="[handleItemDiff('ad110')]"><k-field-text v-model="formData.ad110"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD120_债务证券收益权" :class="[handleItemDiff('ad120')]"><k-field-text v-model="formData.ad120"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD130_应收账款收益权" :class="[handleItemDiff('ad130')]"><k-field-text v-model="formData.ad130"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD140_其他债权收益权" :class="[handleItemDiff('ad140')]"><k-field-text v-model="formData.ad140"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD150_股票收益权" :class="[handleItemDiff('ad150')]"><k-field-text v-model="formData.ad150"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD160_其他股权收益权" :class="[handleItemDiff('ad160')]"><k-field-text v-model="formData.ad160"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD170_物权资产收益权" :class="[handleItemDiff('ad170')]"><k-field-text v-model="formData.ad170"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AD200_除资产收益权外其他债权" :class="[handleItemDiff('ad200')]"><k-field-text v-model="formData.ad200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7000_股权及特定目的载体份额" :class="[handleItemDiff('a7000')]"><k-field-text v-model="formData.a7000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7100_股票" :class="[handleItemDiff('a7100')]"><k-field-text v-model="formData.a7100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7110_非金融企业股票" :class="[handleItemDiff('a7110')]"><k-field-text v-model="formData.a7110"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7120_金融机构股票" :class="[handleItemDiff('a7120')]"><k-field-text v-model="formData.a7120"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7200_特定目的载体份额" :class="[handleItemDiff('a7200')]"><k-field-text v-model="formData.a7200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7210_银行非保本理财" :class="[handleItemDiff('a7210')]"><k-field-text v-model="formData.a7210"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7220_信托公司信托产品" :class="[handleItemDiff('a7220')]"><k-field-text v-model="formData.a7220"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7230_证券公司及其子公司资管产品" :class="[handleItemDiff('a7230')]"><k-field-text v-model="formData.a7230"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7240_基金管理公司及其子公司专户" :class="[handleItemDiff('a7240')]"><k-field-text v-model="formData.a7240"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7250_期货公司及其子公司资管产品" :class="[handleItemDiff('a7250')]"><k-field-text v-model="formData.a7250"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7260_保险资管产品" :class="[handleItemDiff('a7260')]"><k-field-text v-model="formData.a7260"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7270_金融资产投资公司资管产品" :class="[handleItemDiff('a7270')]"><k-field-text v-model="formData.a7270"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7280_公募基金" :class="[handleItemDiff('a7280')]"><k-field-text v-model="formData.a7280"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7290_私募机构私募基金" :class="[handleItemDiff('a7290')]"><k-field-text v-model="formData.a7290"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A72a0_其他特定目的载体" :class="[handleItemDiff('a72a0')]"><k-field-text v-model="formData.a72a0"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7300_其他股权" :class="[handleItemDiff('a7300')]"><k-field-text v-model="formData.a7300"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7310_非金融企业股权" :class="[handleItemDiff('a7310')]"><k-field-text v-model="formData.a7310"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7320_金融机构股权" :class="[handleItemDiff('a7320')]"><k-field-text v-model="formData.a7320"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A7400_境外" :class="[handleItemDiff('a7400')]"><k-field-text v-model="formData.a7400"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A8000_金融衍生工具" :class="[handleItemDiff('a8000')]"><k-field-text v-model="formData.a8000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A8100_境内" :class="[handleItemDiff('a8100')]"><k-field-text v-model="formData.a8100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A8200_境外" :class="[handleItemDiff('a8200')]"><k-field-text v-model="formData.a8200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="A9000_应收账款" :class="[handleItemDiff('a9000')]"><k-field-text v-model="formData.a9000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AA000_黄金" :class="[handleItemDiff('aa000')]"><k-field-text v-model="formData.aa000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AB000_非金融资产" :class="[handleItemDiff('ab000')]"><k-field-text v-model="formData.ab000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="AC000_资产减值准备（减）" :class="[handleItemDiff('ac000')]"><k-field-text v-model="formData.ac000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="D0000_负债及权益合计" :class="[handleItemDiff('d0000')]"><k-field-text v-model="formData.d0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B0000_负债合计" :class="[handleItemDiff('b0000')]"><k-field-text v-model="formData.b0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1000_贷款" :class="[handleItemDiff('b1000')]"><k-field-text v-model="formData.b1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1100_除回购和拆借外贷款" :class="[handleItemDiff('b1100')]"><k-field-text v-model="formData.b1100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1200_回购和拆借（含借款）" :class="[handleItemDiff('b1200')]"><k-field-text v-model="formData.b1200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1210_住户" :class="[handleItemDiff('b1210')]"><k-field-text v-model="formData.b1210"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1220_广义政府" :class="[handleItemDiff('b1220')]"><k-field-text v-model="formData.b1220"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1230_非金融企业" :class="[handleItemDiff('b1230')]"><k-field-text v-model="formData.b1230"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1240_银行业存款类金融机构" :class="[handleItemDiff('b1240')]"><k-field-text v-model="formData.b1240"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1250_银行业非存款类金融机构" :class="[handleItemDiff('b1250')]"><k-field-text v-model="formData.b1250"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1260_非银行业金融机构" :class="[handleItemDiff('b1260')]"><k-field-text v-model="formData.b1260"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1261_其中：中央交易对手方" :class="[handleItemDiff('b1261')]"><k-field-text v-model="formData.b1261"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1270_特定目的载体" :class="[handleItemDiff('b1270')]"><k-field-text v-model="formData.b1270"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1271_银行非保本理财" :class="[handleItemDiff('b1271')]"><k-field-text v-model="formData.b1271"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1272_信托公司信托产品" :class="[handleItemDiff('b1272')]"><k-field-text v-model="formData.b1272"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1273_证券公司及其子公司资管产品" :class="[handleItemDiff('b1273')]"><k-field-text v-model="formData.b1273"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1274_基金管理公司及其子公司专户" :class="[handleItemDiff('b1274')]"><k-field-text v-model="formData.b1274"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1275_期货公司及其子公司资管产品" :class="[handleItemDiff('b1275')]"><k-field-text v-model="formData.b1275"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1276_保险资管产品" :class="[handleItemDiff('b1276')]"><k-field-text v-model="formData.b1276"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1277_金融资产投资公司资管产品" :class="[handleItemDiff('b1277')]"><k-field-text v-model="formData.b1277"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1278_公募基金" :class="[handleItemDiff('b1278')]"><k-field-text v-model="formData.b1278"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1279_私募机构私募基金" :class="[handleItemDiff('b1279')]"><k-field-text v-model="formData.b1279"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B127a_其他特定目的载体" :class="[handleItemDiff('b127a')]"><k-field-text v-model="formData.b127a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B1280_境外" :class="[handleItemDiff('b1280')]"><k-field-text v-model="formData.b1280"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B2000_资产支持证券" :class="[handleItemDiff('b2000')]"><k-field-text v-model="formData.b2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B3000_金融衍生工具" :class="[handleItemDiff('b3000')]"><k-field-text v-model="formData.b3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B3100_境内" :class="[handleItemDiff('b3100')]"><k-field-text v-model="formData.b3100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B3200_境外" :class="[handleItemDiff('b3200')]"><k-field-text v-model="formData.b3200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B4000_应付账款" :class="[handleItemDiff('b4000')]"><k-field-text v-model="formData.b4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="B5000_其他负债" :class="[handleItemDiff('b5000')]"><k-field-text v-model="formData.b5000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C0000_权益合计" :class="[handleItemDiff('c0000')]"><k-field-text v-model="formData.c0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1000_实收本金" :class="[handleItemDiff('c1000')]"><k-field-text v-model="formData.c1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1100_优先级" :class="[handleItemDiff('c1100')]"><k-field-text v-model="formData.c1100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1110_住户" :class="[handleItemDiff('c1110')]"><k-field-text v-model="formData.c1110"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1120_广义政府" :class="[handleItemDiff('c1120')]"><k-field-text v-model="formData.c1120"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1130_非金融企业" :class="[handleItemDiff('c1130')]"><k-field-text v-model="formData.c1130"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1140_银行业存款类金融机构" :class="[handleItemDiff('c1140')]"><k-field-text v-model="formData.c1140"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1150_银行业非存款类金融机构" :class="[handleItemDiff('c1150')]"><k-field-text v-model="formData.c1150"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1160_非银行业金融机构" :class="[handleItemDiff('c1160')]"><k-field-text v-model="formData.c1160"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1170_特定目的载体" :class="[handleItemDiff('c1170')]"><k-field-text v-model="formData.c1170"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1171_银行非保本理财" :class="[handleItemDiff('c1171')]"><k-field-text v-model="formData.c1171"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1172_信托公司信托产品" :class="[handleItemDiff('c1172')]"><k-field-text v-model="formData.c1172"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1173_证券公司及其子公司资管产品" :class="[handleItemDiff('c1173')]"><k-field-text v-model="formData.c1173"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1174_基金管理公司及其子公司专户" :class="[handleItemDiff('c1174')]"><k-field-text v-model="formData.c1174"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1175_期货公司及其子公司资管产品" :class="[handleItemDiff('c1175')]"><k-field-text v-model="formData.c1175"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1176_保险资管产品" :class="[handleItemDiff('c1176')]"><k-field-text v-model="formData.c1176"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1177_金融资产投资公司资管产品" :class="[handleItemDiff('c1177')]"><k-field-text v-model="formData.c1177"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1178_公募基金" :class="[handleItemDiff('c1178')]"><k-field-text v-model="formData.c1178"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1179_私募机构私募基金" :class="[handleItemDiff('c1179')]"><k-field-text v-model="formData.c1179"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C117a_其他特定目的载体" :class="[handleItemDiff('c117a')]"><k-field-text v-model="formData.c117a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1180_境外" :class="[handleItemDiff('c1180')]"><k-field-text v-model="formData.c1180"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1200_劣后级" :class="[handleItemDiff('c1200')]"><k-field-text v-model="formData.c1200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1210_住户" :class="[handleItemDiff('c1210')]"><k-field-text v-model="formData.c1210"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1220_广义政府" :class="[handleItemDiff('c1220')]"><k-field-text v-model="formData.c1220"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1230_非金融企业" :class="[handleItemDiff('c1230')]"><k-field-text v-model="formData.c1230"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1240_银行业存款类金融机构" :class="[handleItemDiff('c1240')]"><k-field-text v-model="formData.c1240"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1250_银行业非存款类金融机构" :class="[handleItemDiff('c1250')]"><k-field-text v-model="formData.c1250"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1260_非银行业金融机构" :class="[handleItemDiff('c1260')]"><k-field-text v-model="formData.c1260"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1270_特定目的载体" :class="[handleItemDiff('c1270')]"><k-field-text v-model="formData.c1270"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1271_银行非保本理财" :class="[handleItemDiff('c1271')]"><k-field-text v-model="formData.c1271"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1272_信托公司信托产品" :class="[handleItemDiff('c1272')]"><k-field-text v-model="formData.c1272"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1273_证券公司及其子公司资管产品" :class="[handleItemDiff('c1273')]"><k-field-text v-model="formData.c1273"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1274_基金管理公司及其子公司专户" :class="[handleItemDiff('c1274')]"><k-field-text v-model="formData.c1274"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1275_期货公司及其子公司资管产品" :class="[handleItemDiff('c1275')]"><k-field-text v-model="formData.c1275"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1276_保险资管产品" :class="[handleItemDiff('c1276')]"><k-field-text v-model="formData.c1276"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1277_金融资产投资公司资管产品" :class="[handleItemDiff('c1277')]"><k-field-text v-model="formData.c1277"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1278_公募基金" :class="[handleItemDiff('c1278')]"><k-field-text v-model="formData.c1278"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1279_私募机构私募基金" :class="[handleItemDiff('c1279')]"><k-field-text v-model="formData.c1279"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C127a_其他特定目的载体" :class="[handleItemDiff('c127a')]"><k-field-text v-model="formData.c127a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C1280_境外" :class="[handleItemDiff('c1280')]"><k-field-text v-model="formData.c1280"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C3000_未分配利润" :class="[handleItemDiff('c3000')]"><k-field-text v-model="formData.c3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-item label="C4000_其他综合收益" :class="[handleItemDiff('c4000')]"><k-field-text v-model="formData.c4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG05.updateZG05" data-from="editZG05Form"
                    :data-model="formData" data-target="ZG05Grid" :handle-before="handleBefore">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                    <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                </k-form-footer>
            </k-form>
          </k-popup>


     <k-popup ref="addPopup" title="报送数据导入" @data-opened="uploadOpened()">
    <k-form ref="addForm" data-ui="element">
      <k-form-item label="数据日期">
              <k-field-date v-model="beginDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM"  :data-allowblank="false"></k-field-date>
      </k-form-item>
      <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
        <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError" :data-success="onSubmitSuccess"
            :data-auto-upload="false"
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG05.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG05Grid" ref="submitBtn"
              :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </k-form-footer>
    </k-form>
    </k-popup>


  </div>
</template>
<script>
import Tools from '@/utils/tools.js';
import ZgMixin from "@/pages/report/rhzg/zgMixin.js";
export default {
    name: "M07RHZG05",
    mixins: [ZgMixin],
    data () {
        return {
            uploadBeginDate: '',
            uploadQueryDate: '',
            activeName: '1',
            prodSearchParam: {
                reportDate: Tools.getPreviousMonth(),
                prodCd: '',
                theoryReportStartDate: localStorage.getItem('currentWorkday'),
                id: '',
                dataTyp: '',
            },
            formData: {},
            formDataCopy: {},
            beginDate:'',
            directedData:{},
            nowDate:'',
        }
    },
    created() {
        this.getNowDate();
    },
    computed: {
      queryParam () {
        return {
            ...this.prodSearchParam
        }
      }
    },
    methods: {
        handleBefore() {
          if (this.formNoChangeCb()) {
            this.$refs.editZG05Popup.close();
            return false
          }
          return true
        },
        submitUploadParam() {
          //文件上传校验
          let validate = this.$refs.addForm.validate();
          if (validate) {
            let formData = { beginDate: this.lastDayBeginDate};
            let temp = document.getElementsByClassName('upload-demo');
            let lis = temp[0].childNodes[1].childNodes.length;
            if (lis > 0) {
              this.$refs.uploadRef.upload(formData);
            } else {
              this.$message.error("上传文件不能为空!");
              return false;
            }
          }
        },
        onSubmitSuccess() {
          this.beginDate = '';
          this.$refs.uploadRef.doReset();
          this.$refs.addForm.reset();
          this.$refs.addPopup.close();
          if (this.prodSearchParam.reportDate) {
            this.$refs.ZG05Grid.load(this.prodSearchParam);
          }
        },
        onSubmitError() {
          this.$refs.uploadRef.doReset();
          this.$refs.submitBtn.setIconStyle(1, [])
        },
        uploadOpened() {
          this.beginDate = '';
        },




      tabClick(tab, event) {
          this.$refs.ZG05Grid.load(this.prodSearchParam)

      },
      selectRow(row, column, event) {
        this.formData = Object.assign({}, row)
        this.formDataCopy = Object.assign({}, row)
      },
      getNowDate() {
        const timeOne = new Date();
        const year = timeOne.getFullYear();
        let month = timeOne.getMonth() + 1;
        let day = timeOne.getDate();
        month = month < 10 ? '0' + month : month;
        day = day < 10 ? '0' + day : day;
        this.nowDate=year+''+month+''+day;
      }
  }

}
</script>
