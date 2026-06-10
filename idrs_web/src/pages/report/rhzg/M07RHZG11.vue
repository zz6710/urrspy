
<template>
  <div class="py-page">
          <k-form-search-customize ref="searchRef" data-model-name="ZG11" data-target="ZG11Grid" v-model="queryParam">
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
                <k-btn ref="assetsZG11Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small"  @click="creatZipFile('ZG11')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG11Grid" :data-export-name="exportName('ZG11', '企业债券分行业和企业规模情况信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG11.findZG11s" data-export-form="searchRef">
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
            <k-grid ref="ZG11Grid" @data-row-select="selectRow" data-action="ZG11.findZG11s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
               <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
               <k-grid-column data-align="left" data-header="发行机构代码" data-name="isuOrgCd" data-width="130"></k-grid-column>
               <k-grid-column data-align="left" data-header="产品品种_资管" data-name="prodCate" data-dict="pbc_prd_typ" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c00000_非金融企业债券余额总计" data-name="c00000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c01000_非金融企业债券余额总计-大型企业" data-name="c01000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c02000_非金融企业债券余额总计-中型企业" data-name="c02000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c03000_非金融企业债券余额总计-小型企业" data-name="c03000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c04000_非金融企业债券余额总计-微型企业" data-name="c04000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c10000_农、林、牧、渔业" data-name="c10000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c11000_农、林、牧、渔业-大型企业" data-name="c11000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c12000_农、林、牧、渔业-中型企业" data-name="c12000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c13000_农、林、牧、渔业-小型企业" data-name="c13000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c14000_农、林、牧、渔业-微型企业" data-name="c14000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c20000_采矿业" data-name="c20000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c21000_采矿业-大型企业" data-name="c21000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c22000_采矿业-中型企业" data-name="c22000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c23000_采矿业-小型企业" data-name="c23000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c24000_采矿业-微型企业" data-name="c24000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c30000_制造业" data-name="c30000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c31000_制造业-大型企业" data-name="c31000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c32000_制造业-中型企业" data-name="c32000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c33000_制造业-小型企业" data-name="c33000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c34000_制造业-微型企业" data-name="c34000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c40000_电力、热力、燃气及水生产和供应业" data-name="c40000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c41000_电力、热力、燃气及水生产和供应业-大型企业" data-name="c41000" data-type="money" data-width="180"></k-grid-column>
               <k-grid-column data-align="left" data-header="c42000_电力、热力、燃气及水生产和供应业-中型企业" data-name="c42000" data-type="money" data-width="180"></k-grid-column>
               <k-grid-column data-align="left" data-header="c43000_电力、热力、燃气及水生产和供应业-小型企业" data-name="c43000" data-type="money" data-width="180"></k-grid-column>
               <k-grid-column data-align="left" data-header="c44000_电力、热力、燃气及水生产和供应业-微型企业" data-name="c44000" data-type="money" data-width="180"></k-grid-column>
               <k-grid-column data-align="left" data-header="c50000_建筑业" data-name="c50000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c51000_建筑业-大型企业" data-name="c51000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c52000_建筑业-中型企业" data-name="c52000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c53000_建筑业-小型企业" data-name="c53000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c54000_建筑业-微型企业" data-name="c54000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="c60000_批发和零售业" data-name="c60000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c61000_批发和零售业-大型企业" data-name="c61000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c62000_批发和零售业-中型企业" data-name="c62000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c63000_批发和零售业-小型企业" data-name="c63000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c64000_批发和零售业-微型企业" data-name="c64000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c70000_交通运输、仓储和邮政业" data-name="c70000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c71000_交通运输、仓储和邮政业-大型企业" data-name="c71000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c72000_交通运输、仓储和邮政业-中型企业" data-name="c72000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c73000_交通运输、仓储和邮政业-小型企业" data-name="c73000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c74000_交通运输、仓储和邮政业-微型企业" data-name="c74000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c80000_住宿和餐饮业" data-name="c80000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c81000_住宿和餐饮业-大型企业" data-name="c81000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c82000_住宿和餐饮业-中型企业" data-name="c82000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c83000_住宿和餐饮业-小型企业" data-name="c83000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c84000_住宿和餐饮业-微型企业" data-name="c84000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c90000_信息传输、软件和信息技术服务业" data-name="c90000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="c91000_信息传输、软件和信息技术服务业-大型企业" data-name="c91000" data-type="money" data-width="180"></k-grid-column>
               <k-grid-column data-align="left" data-header="c92000_信息传输、软件和信息技术服务业-中型企业" data-name="c92000" data-type="money" data-width="180"></k-grid-column>
               <k-grid-column data-align="left" data-header="c93000_信息传输、软件和信息技术服务业-小型企业" data-name="c93000" data-type="money" data-width="180"></k-grid-column>
               <k-grid-column data-align="left" data-header="c94000_信息传输、软件和信息技术服务业-微型企业" data-name="c94000" data-type="money" data-width="180"></k-grid-column>
               <k-grid-column data-align="left" data-header="ca0000_金融业" data-name="ca0000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ca1000_金融业-大型企业" data-name="ca1000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="ca2000_金融业-中型企业" data-name="ca2000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="ca3000_金融业-小型企业" data-name="ca3000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="ca4000_金融业-微型企业" data-name="ca4000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cb0000_房地产业" data-name="cb0000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cb1000_房地产业-大型企业" data-name="cb1000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="cb2000_房地产业-中型企业" data-name="cb2000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="cb3000_房地产业-小型企业" data-name="cb3000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="cb4000_房地产业-微型企业" data-name="cb4000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="cc0000_租赁和商务服务业" data-name="cc0000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cc1000_租赁和商务服务业-大型企业" data-name="cc1000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="cc2000_租赁和商务服务业-中型企业" data-name="cc2000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="cc3000_租赁和商务服务业-小型企业" data-name="cc3000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="cc4000_租赁和商务服务业-微型企业" data-name="cc4000" data-type="money" data-width="170"></k-grid-column>
               <k-grid-column data-align="left" data-header="cd0000_科学研究和技术服务业" data-name="cd0000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cd1000_科学研究和技术服务业-大型企业" data-name="cd1000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cd2000_科学研究和技术服务业-中型企业" data-name="cd2000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cd3000_科学研究和技术服务业-小型企业" data-name="cd3000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cd4000_科学研究和技术服务业-微型企业" data-name="cd4000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ce0000_水利、环境和公共设施管理业" data-name="ce0000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="ce1000_水利、环境和公共设施管理业-大型企业" data-name="ce1000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="ce2000_水利、环境和公共设施管理业-中型企业" data-name="ce2000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="ce3000_水利、环境和公共设施管理业-小型企业" data-name="ce3000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="ce4000_水利、环境和公共设施管理业-微型企业" data-name="ce4000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cf0000_居民服务、修理和其他服务业" data-name="cf0000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cf1000_居民服务、修理和其他服务业-大型企业" data-name="cf1000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cf2000_居民服务、修理和其他服务业-中型企业" data-name="cf2000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cf3000_居民服务、修理和其他服务业-小型企业" data-name="cf3000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cf4000_居民服务、修理和其他服务业-微型企业" data-name="cf4000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cg0000_教育" data-name="cg0000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cg1000_教育-大型企业" data-name="cg1000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cg2000_教育-中型企业" data-name="cg2000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cg3000_教育-小型企业" data-name="cg3000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cg4000_教育-微型企业" data-name="cg4000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ch0000_卫生和社会工作" data-name="ch0000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="ch1000_卫生和社会工作-大型企业" data-name="ch1000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ch2000_卫生和社会工作-中型企业" data-name="ch2000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ch3000_卫生和社会工作-小型企业" data-name="ch3000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ch4000_卫生和社会工作-微型企业" data-name="ch4000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ci0000_文化、体育和娱乐业" data-name="ci0000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ci1000_文化、体育和娱乐业-大型企业" data-name="ci1000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ci2000_文化、体育和娱乐业-中型企业" data-name="ci2000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ci3000_文化、体育和娱乐业-小型企业" data-name="ci3000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="ci4000_文化、体育和娱乐业-微型企业" data-name="ci4000" data-type="money" data-width="150"></k-grid-column>
               <k-grid-column data-align="left" data-header="cj0000_公共管理、社会保障和社会组织" data-name="cj0000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cj1000_公共管理、社会保障和社会组织-大型企业" data-name="cj1000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cj2000_公共管理、社会保障和社会组织-中型企业" data-name="cj2000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cj3000_公共管理、社会保障和社会组织-小型企业" data-name="cj3000" data-type="money" data-width="160"></k-grid-column>
               <k-grid-column data-align="left" data-header="cj4000_公共管理、社会保障和社会组织-微型企业" data-name="cj4000" data-type="money" data-width="160"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG11Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG11.deleteZG11" data-size="mini"
                    data-type="danger" data-target="ZG11Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改企业债券分行业和企业规模情况信息弹出框   -->
          <k-popup ref="editZG11Popup" data-title="修改">
            <k-form ref="editZG11Form" :data-col="3" isFormBodyScreen>
                <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                    <k-field-date v-model="formData.reportDate" :data-allowblank="true" :data-disabled="true" data-type="date" data-value-format="yyyyMMdd" />
                </k-form-item>
                <k-form-item label="发行机构代码" :class="[handleItemDiff('isuOrgCd')]">
                    <k-field-text v-model="formData.isuOrgCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品品种_资管" :class="[handleItemDiff('prodCate')]">
                    <k-field-select v-model="formData.prodCate" :data-allowblank="false" data-dict="pbc_prd_typ" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
               <k-form-item label="c00000_非金融企业债券余额总计" :class="[handleItemDiff('c00000')]"><k-field-text v-model="formData.c00000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c01000_非金融企业债券余额总计-大型企业" :class="[handleItemDiff('c01000')]"><k-field-text v-model="formData.c01000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c02000_非金融企业债券余额总计-中型企业" :class="[handleItemDiff('c02000')]"><k-field-text v-model="formData.c02000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c03000_非金融企业债券余额总计-小型企业" :class="[handleItemDiff('c03000')]"><k-field-text v-model="formData.c03000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c04000_非金融企业债券余额总计-微型企业" :class="[handleItemDiff('c04000')]"><k-field-text v-model="formData.c04000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c10000_农、林、牧、渔业" :class="[handleItemDiff('c10000')]"><k-field-text v-model="formData.c10000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c11000_农、林、牧、渔业-大型企业" :class="[handleItemDiff('c11000')]"><k-field-text v-model="formData.c11000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c12000_农、林、牧、渔业-中型企业" :class="[handleItemDiff('c12000')]"><k-field-text v-model="formData.c12000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c13000_农、林、牧、渔业-小型企业" :class="[handleItemDiff('c13000')]"><k-field-text v-model="formData.c13000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c14000_农、林、牧、渔业-微型企业" :class="[handleItemDiff('c14000')]"><k-field-text v-model="formData.c14000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c20000_采矿业" :class="[handleItemDiff('c20000')]"><k-field-text v-model="formData.c20000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c21000_采矿业-大型企业" :class="[handleItemDiff('c21000')]"><k-field-text v-model="formData.c21000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c22000_采矿业-中型企业" :class="[handleItemDiff('c22000')]"><k-field-text v-model="formData.c22000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c23000_采矿业-小型企业" :class="[handleItemDiff('c23000')]"><k-field-text v-model="formData.c23000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c24000_采矿业-微型企业"><k-field-text v-model="formData.c24000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c30000_制造业" :class="[handleItemDiff('c30000')]"><k-field-text v-model="formData.c30000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c31000_制造业-大型企业" :class="[handleItemDiff('c31000')]"><k-field-text v-model="formData.c31000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c32000_制造业-中型企业" :class="[handleItemDiff('c32000')]"><k-field-text v-model="formData.c32000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c33000_制造业-小型企业" :class="[handleItemDiff('c33000')]"><k-field-text v-model="formData.c33000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c34000_制造业-微型企业" :class="[handleItemDiff('c34000')]"><k-field-text v-model="formData.c34000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c40000_电力、热力、燃气及水生产和供应业" :class="[handleItemDiff('c40000')]"><k-field-text v-model="formData.c40000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c41000_电力、热力、燃气及水生产和供应业-大型企业" :class="[handleItemDiff('c41000')]"><k-field-text v-model="formData.c41000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c42000_电力、热力、燃气及水生产和供应业-中型企业" :class="[handleItemDiff('c42000')]"><k-field-text v-model="formData.c42000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c43000_电力、热力、燃气及水生产和供应业-小型企业" :class="[handleItemDiff('c43000')]"><k-field-text v-model="formData.c43000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c44000_电力、热力、燃气及水生产和供应业-微型企业" :class="[handleItemDiff('c44000')]"><k-field-text v-model="formData.c44000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c50000_建筑业" :class="[handleItemDiff('c50000')]"><k-field-text v-model="formData.c50000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c51000_建筑业-大型企业" :class="[handleItemDiff('c51000')]"><k-field-text v-model="formData.c51000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c52000_建筑业-中型企业" :class="[handleItemDiff('c52000')]"><k-field-text v-model="formData.c52000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c53000_建筑业-小型企业" :class="[handleItemDiff('c53000')]"><k-field-text v-model="formData.c53000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c54000_建筑业-微型企业" :class="[handleItemDiff('c54000')]"><k-field-text v-model="formData.c54000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c60000_批发和零售业" :class="[handleItemDiff('c60000')]"><k-field-text v-model="formData.c60000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c61000_批发和零售业-大型企业" :class="[handleItemDiff('c61000')]"><k-field-text v-model="formData.c61000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c62000_批发和零售业-中型企业" :class="[handleItemDiff('c62000')]"><k-field-text v-model="formData.c62000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c63000_批发和零售业-小型企业" :class="[handleItemDiff('c63000')]"><k-field-text v-model="formData.c63000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c64000_批发和零售业-微型企业" :class="[handleItemDiff('c64000')]"><k-field-text v-model="formData.c64000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c70000_交通运输、仓储和邮政业" :class="[handleItemDiff('c70000')]"><k-field-text v-model="formData.c70000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c71000_交通运输、仓储和邮政业-大型企业" :class="[handleItemDiff('c71000')]"><k-field-text v-model="formData.c71000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c72000_交通运输、仓储和邮政业-中型企业" :class="[handleItemDiff('c72000')]"><k-field-text v-model="formData.c72000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c73000_交通运输、仓储和邮政业-小型企业" :class="[handleItemDiff('c73000')]"><k-field-text v-model="formData.c73000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c74000_交通运输、仓储和邮政业-微型企业" :class="[handleItemDiff('c74000')]"><k-field-text v-model="formData.c74000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c80000_住宿和餐饮业" :class="[handleItemDiff('c80000')]"><k-field-text v-model="formData.c80000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c81000_住宿和餐饮业-大型企业" :class="[handleItemDiff('c81000')]"><k-field-text v-model="formData.c81000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c82000_住宿和餐饮业-中型企业" :class="[handleItemDiff('c82000')]"><k-field-text v-model="formData.c82000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c83000_住宿和餐饮业-小型企业" :class="[handleItemDiff('c83000')]"><k-field-text v-model="formData.c83000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c84000_住宿和餐饮业-微型企业" :class="[handleItemDiff('c84000')]"><k-field-text v-model="formData.c84000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c90000_信息传输、软件和信息技术服务业" :class="[handleItemDiff('c90000')]"><k-field-text v-model="formData.c90000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c91000_信息传输、软件和信息技术服务业-大型企业" :class="[handleItemDiff('c91000')]"><k-field-text v-model="formData.c91000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c92000_信息传输、软件和信息技术服务业-中型企业" :class="[handleItemDiff('c92000')]"><k-field-text v-model="formData.c92000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c93000_信息传输、软件和信息技术服务业-小型企业" :class="[handleItemDiff('c93000')]"><k-field-text v-model="formData.c93000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="c94000_信息传输、软件和信息技术服务业-微型企业" :class="[handleItemDiff('c94000')]"><k-field-text v-model="formData.c94000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ca0000_金融业" :class="[handleItemDiff('ca0000')]"><k-field-text v-model="formData.ca0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ca1000_金融业-大型企业" :class="[handleItemDiff('ca1000')]"><k-field-text v-model="formData.ca1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ca2000_金融业-中型企业" :class="[handleItemDiff('ca2000')]"><k-field-text v-model="formData.ca2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ca3000_金融业-小型企业" :class="[handleItemDiff('ca3000')]"><k-field-text v-model="formData.ca3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ca4000_金融业-微型企业" :class="[handleItemDiff('ca4000')]"><k-field-text v-model="formData.ca4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cb0000_房地产业" :class="[handleItemDiff('cb0000')]"><k-field-text v-model="formData.cb0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cb1000_房地产业-大型企业" :class="[handleItemDiff('cb1000')]"><k-field-text v-model="formData.cb1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cb2000_房地产业-中型企业" :class="[handleItemDiff('cb2000')]"><k-field-text v-model="formData.cb2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cb3000_房地产业-小型企业" :class="[handleItemDiff('cb3000')]"><k-field-text v-model="formData.cb3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cb4000_房地产业-微型企业" :class="[handleItemDiff('cb4000')]"><k-field-text v-model="formData.cb4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cc0000_租赁和商务服务业" :class="[handleItemDiff('cc0000')]"><k-field-text v-model="formData.cc0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cc1000_租赁和商务服务业-大型企业" :class="[handleItemDiff('cc1000')]"><k-field-text v-model="formData.cc1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cc2000_租赁和商务服务业-中型企业" :class="[handleItemDiff('cc2000')]"><k-field-text v-model="formData.cc2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cc3000_租赁和商务服务业-小型企业" :class="[handleItemDiff('cc3000')]"><k-field-text v-model="formData.cc3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cc4000_租赁和商务服务业-微型企业" :class="[handleItemDiff('cc4000')]"><k-field-text v-model="formData.cc4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cd0000_科学研究和技术服务业" :class="[handleItemDiff('cd0000')]"><k-field-text v-model="formData.cd0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cd1000_科学研究和技术服务业-大型企业" :class="[handleItemDiff('cd1000')]"><k-field-text v-model="formData.cd1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cd2000_科学研究和技术服务业-中型企业" :class="[handleItemDiff('cd2000')]"><k-field-text v-model="formData.cd2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cd3000_科学研究和技术服务业-小型企业" :class="[handleItemDiff('cd3000')]"><k-field-text v-model="formData.cd3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cd4000_科学研究和技术服务业-微型企业" :class="[handleItemDiff('cd4000')]"><k-field-text v-model="formData.cd4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ce0000_水利、环境和公共设施管理业" :class="[handleItemDiff('ce0000')]"><k-field-text v-model="formData.ce0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ce1000_水利、环境和公共设施管理业-大型企业" :class="[handleItemDiff('ce1000')]"><k-field-text v-model="formData.ce1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ce2000_水利、环境和公共设施管理业-中型企业" :class="[handleItemDiff('ce2000')]"><k-field-text v-model="formData.ce2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ce3000_水利、环境和公共设施管理业-小型企业" :class="[handleItemDiff('ce3000')]"><k-field-text v-model="formData.ce3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ce4000_水利、环境和公共设施管理业-微型企业" :class="[handleItemDiff('ce4000')]"><k-field-text v-model="formData.ce4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cf0000_居民服务、修理和其他服务业" :class="[handleItemDiff('cf0000')]"><k-field-text v-model="formData.cf0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cf1000_居民服务、修理和其他服务业-大型企业" :class="[handleItemDiff('cf1000')]"><k-field-text v-model="formData.cf1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cf2000_居民服务、修理和其他服务业-中型企业" :class="[handleItemDiff('cf2000')]"><k-field-text v-model="formData.cf2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cf3000_居民服务、修理和其他服务业-小型企业" :class="[handleItemDiff('cf3000')]"><k-field-text v-model="formData.cf3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cf4000_居民服务、修理和其他服务业-微型企业" :class="[handleItemDiff('cf4000')]"><k-field-text v-model="formData.cf4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cg0000_教育" :class="[handleItemDiff('cg0000')]"><k-field-text v-model="formData.cg0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cg1000_教育-大型企业" :class="[handleItemDiff('cg1000')]"><k-field-text v-model="formData.cg1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cg2000_教育-中型企业" :class="[handleItemDiff('cg2000')]"><k-field-text v-model="formData.cg2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cg3000_教育-小型企业" :class="[handleItemDiff('cg3000')]"><k-field-text v-model="formData.cg3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cg4000_教育-微型企业" :class="[handleItemDiff('cg4000')]"><k-field-text v-model="formData.cg4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ch0000_卫生和社会工作" :class="[handleItemDiff('ch0000')]"><k-field-text v-model="formData.ch0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ch1000_卫生和社会工作-大型企业" :class="[handleItemDiff('ch1000')]"><k-field-text v-model="formData.ch1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ch2000_卫生和社会工作-中型企业" :class="[handleItemDiff('ch2000')]"><k-field-text v-model="formData.ch2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ch3000_卫生和社会工作-小型企业" :class="[handleItemDiff('ch3000')]"><k-field-text v-model="formData.ch3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ch4000_卫生和社会工作-微型企业" :class="[handleItemDiff('ch4000')]"><k-field-text v-model="formData.ch4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ci0000_文化、体育和娱乐业" :class="[handleItemDiff('ci0000')]"><k-field-text v-model="formData.ci0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ci1000_文化、体育和娱乐业-大型企业" :class="[handleItemDiff('ci1000')]"><k-field-text v-model="formData.ci1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ci2000_文化、体育和娱乐业-中型企业" :class="[handleItemDiff('ci2000')]"><k-field-text v-model="formData.ci2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ci3000_文化、体育和娱乐业-小型企业" :class="[handleItemDiff('ci3000')]"><k-field-text v-model="formData.ci3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="ci4000_文化、体育和娱乐业-微型企业" :class="[handleItemDiff('ci4000')]"><k-field-text v-model="formData.ci4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cj0000_公共管理、社会保障和社会组织" :class="[handleItemDiff('cj0000')]"><k-field-text v-model="formData.cj0000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cj1000_公共管理、社会保障和社会组织-大型企业" :class="[handleItemDiff('cj1000')]"><k-field-text v-model="formData.cj1000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cj2000_公共管理、社会保障和社会组织-中型企业" :class="[handleItemDiff('cj2000')]"><k-field-text v-model="formData.cj2000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cj3000_公共管理、社会保障和社会组织-小型企业" :class="[handleItemDiff('cj3000')]"><k-field-text v-model="formData.cj3000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="cj4000_公共管理、社会保障和社会组织-微型企业" :class="[handleItemDiff('cj4000')]"><k-field-text v-model="formData.cj4000"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG11.updateZG11" data-from="editZG11Form"
                    :data-model="formData" data-target="ZG11Grid" :handle-before="handleBefore">
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
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG11.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG11Grid" ref="submitBtn"
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
    name: "M07RHZG11",
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
              this.$refs.ZG11Grid.load(this.prodSearchParam);
           });
      }
    },
    activated() {
      if(this.$route.query.directedData && this.$route.query.directedData.dataId){
            this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
            this.directedData = this.$route.query.directedData
            this.prodSearchParam.id = this.$route.query.directedData.dataId;
            this.$nextTick(()=>{
               this.$refs.ZG11Grid.load(this.prodSearchParam);
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
          this.$refs.editZG11Popup.close();
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
          this.$refs.ZG11Grid.load(this.queryParam);
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
          this.$refs.ZG11Grid.load(this.prodSearchParam)

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
