
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG10" data-target="ZG10Grid" v-model="queryParam">
            <k-form-item label="数据日期">
              <k-field-date v-model="prodSearchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="产品品种_资管">
              <k-field-select v-model="prodSearchParam.prodCate" data-dict="pbc_prd_typ" data-dict-type="1"/>
            </k-form-item>
          </k-form-search-customize>
          <div class="py-page-container">
            <div class="table-top-btns">
              <div class="left">
                <k-btn ref="assetsZG10Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG10')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG10Grid" :data-export-name="exportName('ZG10', '债券等资产配置情况信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG10.findZG10s" data-export-form="searchRef">
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
            <k-grid ref="ZG10Grid" @data-row-select="selectRow" data-action="ZG10.findZG10s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构代码" data-name="isuOrgCd" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品品种_资管" data-name="prodCate" data-dict="pbc_prd_typ" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h10000_一、债务证券（按评级分类）" data-name="h10000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h15000_其中：债务证券（金融）" data-name="h15000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h15100_AA+（含）以上" data-name="h15100" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h15200_AA+以下" data-name="h15200" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h15300_无评级" data-name="h15300" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h16000_债务证券（非金融）" data-name="h16000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h16100_AA+（含）以上" data-name="h16100" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h16200_AA+以下" data-name="h16200" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h16300_无评级" data-name="h16300" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h20000_二、非金融企业债券按券种分类" data-name="h20000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h21000_其中：企业债" data-name="h21000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h22000_公司债" data-name="h22000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h23000_非金融企业债务融资工具" data-name="h23000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h30000_三、银行资本补充工具合计" data-name="h30000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h31000_其中：优先股" data-name="h31000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h32000_永续债（债性）" data-name="h32000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="h33000_永续债（股性）" data-name="h33000" data-type="money"  data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="h34000_二级资本债" data-name="h34000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h40000_四、债券逆回购" data-name="h40000" data-type="money"  data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="h41000_住户" data-name="h41000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h42000_广义政府" data-name="h42000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h43000_非金融企业" data-name="h43000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h44000_银行业存款类金融机构" data-name="h44000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h45000_银行业非存款类金融机构" data-name="h45000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h46000_非银行业金融机构" data-name="h46000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h46100_其中：中央交易对手方" data-name="h46100" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47000_特定目的载体" data-name="h47000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47100_银行非保本理财" data-name="h47100" data-type="money"  data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47200_信托公司信托产品" data-name="h47200" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47300_证券公司及其子公司资管产品" data-name="h47300" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47400_基金管理公司及其子公司专户" data-name="h47400" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47500_期货公司及其子公司资管产品" data-name="h47500" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47600_保险资管产品" data-name="h47600" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47700_金融资产投资公司资管产品" data-name="h47700" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47800_公募基金" data-name="h47800" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47900_私募机构私募基金" data-name="h47900" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h47a00_其他特定目的载体" data-name="h47a00" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h48000_境外" data-name="h48000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h50000_五、资产减值准备" data-name="h50000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h51000_存款" data-name="h51000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h52000_存单" data-name="h52000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h53000_债务证券" data-name="h53000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h54000_除回购和拆借外贷款" data-name="h54000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h55000_回购和拆借（含借款）" data-name="h55000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h5b000_其他债权" data-name="h5b000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h57000_股权及特定目的载体份额" data-name="h57000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h58000_金融衍生工具" data-name="h58000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h59000_应收账款" data-name="h59000" data-type="money"  data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="h5a000_其他" data-name="h5a000" data-type="money"  data-width="150"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG10Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG10.deleteZG10" data-size="mini"
                    data-type="danger" data-target="ZG10Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改债券等资产配置情况信息弹出框   -->
          <k-popup ref="editZG10Popup" data-title="修改">
            <k-form ref="editZG10Form" :data-col="3" isFormBodyScreen>
                <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                    <k-field-date v-model="formData.reportDate" :data-allowblank="true" :data-disabled="true" data-type="date" data-value-format="yyyyMMdd"/>
                </k-form-item>
                <k-form-item label="发行机构代码" :class="[handleItemDiff('isuOrgCd')]">
                    <k-field-text v-model="formData.isuOrgCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>

                <k-form-item label="产品品种_资管" :class="[handleItemDiff('prodCate')]">
                    <k-field-select v-model="formData.prodCate" :data-allowblank="false" data-dict="pbc_prd_typ" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
                 <k-form-item label="h10000_一、债务证券（按评级分类）" :class="[handleItemDiff('h10000')]"><k-field-text v-model="formData.h10000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h15000_其中：债务证券（金融）" :class="[handleItemDiff('h15000')]"><k-field-text v-model="formData.h15000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h15100_AA+（含）以上" :class="[handleItemDiff('h15100')]"><k-field-text v-model="formData.h15100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h15200_AA+以下" :class="[handleItemDiff('h15200')]"><k-field-text v-model="formData.h15200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h15300_无评级" :class="[handleItemDiff('h15300')]"><k-field-text v-model="formData.h15300"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h16000_债务证券（非金融）" :class="[handleItemDiff('h16000')]"><k-field-text v-model="formData.h16000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h16100_AA+（含）以上" :class="[handleItemDiff('h16100')]"><k-field-text v-model="formData.h16100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h16200_AA+以下" :class="[handleItemDiff('h16200')]"><k-field-text v-model="formData.h16200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h16300_无评级" :class="[handleItemDiff('h16300')]"><k-field-text v-model="formData.h16300"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h20000_二、非金融企业债券按券种分类" :class="[handleItemDiff('h20000')]"><k-field-text v-model="formData.h20000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h21000_其中：企业债" :class="[handleItemDiff('h21000')]"><k-field-text v-model="formData.h21000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h22000_公司债" :class="[handleItemDiff('h22000')]"><k-field-text v-model="formData.h22000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h23000_非金融企业债务融资工具" :class="[handleItemDiff('h23000')]"><k-field-text v-model="formData.h23000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h30000_三、银行资本补充工具合计" :class="[handleItemDiff('h30000')]"><k-field-text v-model="formData.h30000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h31000_其中：优先股" :class="[handleItemDiff('h31000')]"><k-field-text v-model="formData.h31000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h32000_永续债（债性）" :class="[handleItemDiff('h32000')]"><k-field-text v-model="formData.h32000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h33000_永续债（股性）" :class="[handleItemDiff('h33000')]"><k-field-text v-model="formData.h33000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h34000_二级资本债" :class="[handleItemDiff('h34000')]"><k-field-text v-model="formData.h34000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h40000_四、债券逆回购" :class="[handleItemDiff('h40000')]"><k-field-text v-model="formData.h40000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h41000_住户" :class="[handleItemDiff('h41000')]"><k-field-text v-model="formData.h41000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h42000_广义政府" :class="[handleItemDiff('h42000')]"><k-field-text v-model="formData.h42000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h43000_非金融企业" :class="[handleItemDiff('h43000')]"><k-field-text v-model="formData.h43000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h44000_银行业存款类金融机构" :class="[handleItemDiff('h44000')]"><k-field-text v-model="formData.h44000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h45000_银行业非存款类金融机构" :class="[handleItemDiff('h45000')]"><k-field-text v-model="formData.h45000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h46000_非银行业金融机构" :class="[handleItemDiff('h46000')]"><k-field-text v-model="formData.h46000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h46100_其中：中央交易对手方" :class="[handleItemDiff('h46100')]"><k-field-text v-model="formData.h46100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47000_特定目的载体" :class="[handleItemDiff('h47000')]"><k-field-text v-model="formData.h47000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47100_银行非保本理财" :class="[handleItemDiff('h47100')]"><k-field-text v-model="formData.h47100"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47200_信托公司信托产品" :class="[handleItemDiff('h47200')]"><k-field-text v-model="formData.h47200"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47300_证券公司及其子公司资管产品" :class="[handleItemDiff('h47300')]"><k-field-text v-model="formData.h47300"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47400_基金管理公司及其子公司专户" :class="[handleItemDiff('h47400')]"><k-field-text v-model="formData.h47400"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47500_期货公司及其子公司资管产品" :class="[handleItemDiff('h47500')]"><k-field-text v-model="formData.h47500"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47600_保险资管产品" :class="[handleItemDiff('h47600')]"><k-field-text v-model="formData.h47600"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47700_金融资产投资公司资管产品" :class="[handleItemDiff('h47700')]"><k-field-text v-model="formData.h47700"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47800_公募基金" :class="[handleItemDiff('h47800')]"><k-field-text v-model="formData.h47800"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47900_私募机构私募基金" :class="[handleItemDiff('h47900')]"><k-field-text v-model="formData.h47900"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h47a00_其他特定目的载体" :class="[handleItemDiff('h47a00')]"><k-field-text v-model="formData.h47a00"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h48000_境外" :class="[handleItemDiff('h48000')]"><k-field-text v-model="formData.h48000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h50000_五、资产减值准备" :class="[handleItemDiff('h50000')]"><k-field-text v-model="formData.h50000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h51000_存款" :class="[handleItemDiff('h51000')]"><k-field-text v-model="formData.h51000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h52000_存单" :class="[handleItemDiff('h52000')]"><k-field-text v-model="formData.h52000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h53000_债务证券" :class="[handleItemDiff('h53000')]"><k-field-text v-model="formData.h53000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h54000_除回购和拆借外贷款" :class="[handleItemDiff('h54000')]"><k-field-text v-model="formData.h54000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h55000_回购和拆借（含借款）" :class="[handleItemDiff('h55000')]"><k-field-text v-model="formData.h55000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h5b000_其他债权" :class="[handleItemDiff('h5b000')]"><k-field-text v-model="formData.h5b000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h57000_股权及特定目的载体份额" :class="[handleItemDiff('h57000')]"><k-field-text v-model="formData.h57000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h58000_金融衍生工具" :class="[handleItemDiff('h58000')]"><k-field-text v-model="formData.h58000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h59000_应收账款" :class="[handleItemDiff('h59000')]"><k-field-text v-model="formData.h59000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                 <k-form-item label="h5a000_其他" :class="[handleItemDiff('h5a000')]"><k-field-text v-model="formData.h5a000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>







                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG10.updateZG10" data-from="editZG10Form"
                    :data-model="formData" data-target="ZG10Grid" :handle-before="handleBefore">
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
              <k-field-date v-model="beginDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false"></k-field-date>
      </k-form-item>
      <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
        <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError" :data-success="onSubmitSuccess"
            :data-auto-upload="false"
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG10.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG10Grid" ref="submitBtn"
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
    name: "M07RHZG10",
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
      if(this.$route.query.directedData && this.$route.query.directedData.dataId){
           this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
           this.directedData = this.$route.query.directedData
           this.prodSearchParam.id = this.$route.query.directedData.dataId;
           this.$nextTick(()=>{
              this.$refs.ZG10Grid.load(this.prodSearchParam);
           });
      }
    },
    activated() {
         if(this.$route.query.directedData && this.$route.query.directedData.dataId){
                    this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
                    this.directedData = this.$route.query.directedData
                    this.prodSearchParam.id = this.$route.query.directedData.dataId;
                    this.$nextTick(()=>{
                       this.$refs.ZG10Grid.load(this.prodSearchParam);
                    });
               }
     },
    mounted() {
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
          this.$refs.editZG10Popup.close();
          return false
        }
        return true
      },
      submitUploadParam() {
        //文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = { beginDate: this.lastDayBeginDate };
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
          this.$refs.ZG10Grid.load(this.queryParam);
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
          this.$refs.ZG10Grid.load(this.prodSearchParam)

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
