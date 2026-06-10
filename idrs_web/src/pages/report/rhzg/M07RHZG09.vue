
<template>
  <div class="py-page">

          <k-form-search-customize ref="searchRef" data-model-name="ZG09" data-target="ZG09Grid" v-model="queryParam">
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
                <k-btn ref="assetsZG09Send" slot="button" class="btn-custom-plain"
              data-descript="数据报送" data-size="small" @click="creatZipFile('ZG09')">
               <md-icon>cloud_download</md-icon>
              数据报送
            </k-btn>
                <k-btn slot="button" class="btn-custom-plain" data-target="ZG09Grid" :data-export-name="exportName('ZG09', '资产负债剩余期限信息')"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ZG09.findZG09s" data-export-form="searchRef">
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
            <k-grid ref="ZG09Grid" @data-row-select="selectRow" data-action="ZG09.findZG09s" data-fixed="right" data-operate-width="120px" :data-autoload="false" data-dict-type="1">
                <k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="100" data-export="false"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构代码" data-name="isuOrgCd" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品品种_资管" data-name="prodCate" data-dict="pbc_prd_typ" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G000A_资产方-债务工具-总计" data-name="g000a" data-type="money" data-width="190"></k-grid-column>
                <k-grid-column data-align="left" data-header="G000B_资产方-股权-总计" data-name="g000b" data-type="money" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="G000C_资产方-特定目的载体份额-总计" data-name="g000c" data-type="money" data-width="190"></k-grid-column>
                <k-grid-column data-align="left" data-header="G000D_负债及权益方-债务工具-总计" data-name="g000d" data-type="money" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="G000E_负债及权益方-实收本金-总计" data-name="g000e" data-type="money" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="G100A_资产方-债务工具-1个月（含）以下" data-name="g100a" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G100B_资产方-股权-1个月（含）以下" data-name="g100b" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G100C_资产方-特定目的载体份额-1个月（含）以下" data-name="g100c" data-type="money" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="G100D_负债及权益方-债务工具-1个月（含）以下" data-name="g100d" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G100E_负债及权益方-实收本金-1个月（含）以下" data-name="g100e" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G200A_资产方-债务工具-1-3个月（含）" data-name="g200a" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G200B_资产方-股权-1-3个月（含）" data-name="g200b" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G200C_资产方-特定目的载体份额-1-3个月（含）" data-name="g200c" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G200D_负债及权益方-债务工具-1-3个月（含）" data-name="g200d" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G200E_负债及权益方-实收本金-1-3个月（含）" data-name="g200e" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G300A_资产方-债务工具-3-6个月（含）" data-name="g300a" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G300B_资产方-股权-3-6个月（含）" data-name="g300b" data-type="money" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="G300C_资产方-特定目的载体份额-3-6个月（含）" data-name="g300c" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G300D_负债及权益方-债务工具-3-6个月（含）" data-name="g300d" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G300E_负债及权益方-实收本金-3-6个月（含）" data-name="g300e" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G400A_资产方-债务工具-6-12个月（含）" data-name="g400a" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G400B_资产方-股权-6-12个月（含）" data-name="g400b" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G400C_资产方-特定目的载体份额-6-12个月（含）" data-name="g400c" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G400D_负债及权益方-债务工具-6-12个月（含）" data-name="g400d" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G400E_负债及权益方-实收本金-6-12个月（含）" data-name="g400e" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G500A_资产方-债务工具-1-2年（含）" data-name="g500a" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G500B_资产方-股权-1-2年（含）" data-name="g500b" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G500C_资产方-特定目的载体份额-1-2年（含）" data-name="g500c" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G500D_负债及权益方-债务工具-1-2年（含）" data-name="g500d" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G500E_负债及权益方-实收本金-1-2年（含）" data-name="g500e" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G600A_资产方-债务工具-2-3年（含）" data-name="g600a" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G600B_资产方-股权-2-3年（含）" data-name="g600b" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G600C_资产方-特定目的载体份额-2-3年（含）" data-name="g600c" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="G600D_负债及权益方-债务工具-2-3年（含）" data-name="g600d" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G600E_负债及权益方-实收本金-2-3年（含）" data-name="g600e" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G700A_资产方-债务工具-3年以上" data-name="g700a" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G700B_资产方-股权-3年以上" data-name="g700b" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G700C_资产方-特定目的载体份额-3年以上" data-name="g700c" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G700D_负债及权益方-债务工具-3年以上" data-name="g700d" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G700E_负债及权益方-实收本金-3年以上" data-name="g700e" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G800A_资产方-债务工具-无固定期限" data-name="g800a" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G800B_资产方-股权-无固定期限" data-name="g800b" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G800C_资产方-特定目的载体份额-无固定期限" data-name="g800c" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G800D_负债及权益方-债务工具-无固定期限" data-name="g800d" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="G800E_负债及权益方-实收本金-无固定期限" data-name="g800e" data-type="money" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="00001_附报-金融机构表内资产余额（本外币）" data-name="g0001" data-type="money" data-width="170"></k-grid-column>
                <k-grid-column data-align="left" data-header="00002_附报-金融机构表内金融资产余额（本外币）" data-name="g0002" data-type="money" data-width="170"></k-grid-column>


                <template slot="operate" slot-scope="scope">
                <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                    data-target="editZG09Popup">
                    修改
                </k-btn>
                <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ZG09.deleteZG09" data-size="mini"
                    data-type="danger" data-target="ZG09Grid" :data-confirm="true" data-descript="删除">
                    删除
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改资产负债剩余期限信息弹出框   -->
          <k-popup ref="editZG09Popup" data-title="修改">
            <k-form ref="editZG09Form" :data-col="3" isFormBodyScreen data-label-width="180px" class="form-edit">
                <k-form-item label="数据日期" :class="[handleItemDiff('reportDate')]">
                    <k-field-date v-model="formData.reportDate" :data-allowblank="true" :data-disabled="true" data-type="date" data-value-format="yyyyMMdd"  />
                </k-form-item>
                <k-form-item label="发行机构代码" :class="[handleItemDiff('isuOrgCd')]">
                    <k-field-text v-model="formData.isuOrgCd"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品品种_资管" :class="[handleItemDiff('prodCate')]">
                    <k-field-select v-model="formData.prodCate" :data-allowblank="false" data-dict="pbc_prd_typ" data-dict-type="1" :data-disabled="false"/>
                </k-form-item>
               <k-form-item label="G000A_资产方-债务工具-总计" :class="[handleItemDiff('g000a')]"><k-field-text v-model="formData.g000a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G000B_资产方-股权-总计" :class="[handleItemDiff('g000b')]"><k-field-text v-model="formData.g000b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G000C_资产方-特定目的载体份额-总计" :class="[handleItemDiff('g000c')]"><k-field-text v-model="formData.g000c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G000D_负债及权益方-债务工具-总计" :class="[handleItemDiff('g000d')]"><k-field-text v-model="formData.g000d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G000E_负债及权益方-实收本金-总计" :class="[handleItemDiff('g000e')]"><k-field-text v-model="formData.g000e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G100A_资产方-债务工具-1个月（含）以下" :class="[handleItemDiff('g100a')]"><k-field-text v-model="formData.g100a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G100B_资产方-股权-1个月（含）以下" :class="[handleItemDiff('g100b')]"><k-field-text v-model="formData.g100b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G100C_资产方-特定目的载体份额-1个月（含）以下" :class="[handleItemDiff('g100c')]"><k-field-text v-model="formData.g100c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G100D_负债及权益方-债务工具-1个月（含）以下" :class="[handleItemDiff('g100d')]"><k-field-text v-model="formData.g100d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G100E_负债及权益方-实收本金-1个月（含）以下" :class="[handleItemDiff('g100e')]"><k-field-text v-model="formData.g100e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G200A_资产方-债务工具-1-3个月（含）" :class="[handleItemDiff('g200a')]"><k-field-text v-model="formData.g200a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G200B_资产方-股权-1-3个月（含）" :class="[handleItemDiff('g200b')]"><k-field-text v-model="formData.g200b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G200C_资产方-特定目的载体份额-1-3个月（含）" :class="[handleItemDiff('g200c')]"><k-field-text v-model="formData.g200c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G200D_负债及权益方-债务工具-1-3个月（含）" :class="[handleItemDiff('g200d')]"><k-field-text v-model="formData.g200d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G200E_负债及权益方-实收本金-1-3个月（含）" :class="[handleItemDiff('g200e')]"><k-field-text v-model="formData.g200e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G300A_资产方-债务工具-3-6个月（含）" :class="[handleItemDiff('g300a')]"><k-field-text v-model="formData.g300a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G300B_资产方-股权-3-6个月（含）" :class="[handleItemDiff('g300b')]"><k-field-text v-model="formData.g300b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G300C_资产方-特定目的载体份额-3-6个月（含）" :class="[handleItemDiff('g300c')]"><k-field-text v-model="formData.g300c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G300D_负债及权益方-债务工具-3-6个月（含）" :class="[handleItemDiff('g300d')]"><k-field-text v-model="formData.g300d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G300E_负债及权益方-实收本金-3-6个月（含）" :class="[handleItemDiff('g300e')]"><k-field-text v-model="formData.g300e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G400A_资产方-债务工具-6-12个月（含）" :class="[handleItemDiff('g400a')]"><k-field-text v-model="formData.g400a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G400B_资产方-股权-6-12个月（含）" :class="[handleItemDiff('g400b')]"><k-field-text v-model="formData.g400b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G400C_资产方-特定目的载体份额-6-12个月（含）" :class="[handleItemDiff('g400c')]"><k-field-text v-model="formData.g400c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G400D_负债及权益方-债务工具-6-12个月（含）" :class="[handleItemDiff('g400d')]"><k-field-text v-model="formData.g400d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G400E_负债及权益方-实收本金-6-12个月（含）" :class="[handleItemDiff('g400e')]"><k-field-text v-model="formData.g400e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G500A_资产方-债务工具-1-2年（含）" :class="[handleItemDiff('g500a')]"><k-field-text v-model="formData.g500a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G500B_资产方-股权-1-2年（含）" :class="[handleItemDiff('g500b')]"><k-field-text v-model="formData.g500b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G500C_资产方-特定目的载体份额-1-2年（含）" :class="[handleItemDiff('g500c')]"><k-field-text v-model="formData.g500c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G500D_负债及权益方-债务工具-1-2年（含）" :class="[handleItemDiff('g500d')]"><k-field-text v-model="formData.g500d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G500E_负债及权益方-实收本金-1-2年（含）" :class="[handleItemDiff('g500e')]"><k-field-text v-model="formData.g500e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G600A_资产方-债务工具-2-3年（含）" :class="[handleItemDiff('g600a')]"><k-field-text v-model="formData.g600a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G600B_资产方-股权-2-3年（含）" :class="[handleItemDiff('g600b')]"><k-field-text v-model="formData.g600b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G600C_资产方-特定目的载体份额-2-3年（含）" :class="[handleItemDiff('g600c')]"><k-field-text v-model="formData.g600c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G600D_负债及权益方-债务工具-2-3年（含）" :class="[handleItemDiff('g600d')]"><k-field-text v-model="formData.g600d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G600E_负债及权益方-实收本金-2-3年（含）" :class="[handleItemDiff('g600e')]"><k-field-text v-model="formData.g600e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G700A_资产方-债务工具-3年以上" :class="[handleItemDiff('g700a')]"><k-field-text v-model="formData.g700a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G700B_资产方-股权-3年以上" :class="[handleItemDiff('g700b')]"><k-field-text v-model="formData.g700b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G700C_资产方-特定目的载体份额-3年以上" :class="[handleItemDiff('g700c')]"><k-field-text v-model="formData.g700c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G700D_负债及权益方-债务工具-3年以上" :class="[handleItemDiff('g700d')]"><k-field-text v-model="formData.g700d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G700E_负债及权益方-实收本金-3年以上" :class="[handleItemDiff('g700e')]"><k-field-text v-model="formData.g700e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G800A_资产方-债务工具-无固定期限" :class="[handleItemDiff('g800a')]"><k-field-text v-model="formData.g800a"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G800B_资产方-股权-无固定期限" :class="[handleItemDiff('g800b')]"><k-field-text v-model="formData.g800b"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G800C_资产方-特定目的载体份额-无固定期限" :class="[handleItemDiff('g800c')]"><k-field-text v-model="formData.g800c"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G800D_负债及权益方-债务工具-无固定期限" :class="[handleItemDiff('g800d')]"><k-field-text v-model="formData.g800d"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="G800E_负债及权益方-实收本金-无固定期限" :class="[handleItemDiff('g800e')]"><k-field-text v-model="formData.g800e"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="00001_附报-金融机构表内资产余额（本外币）" :class="[handleItemDiff('g0001')]"><k-field-text v-model="formData.g0001"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
               <k-form-item label="00002_附报-金融机构表内金融资产余额（本外币）" :class="[handleItemDiff('g0002')]"><k-field-text v-model="formData.g0002"  :data-allowblank="true" :data-disabled="false" data-validate-type="number" :data-max-length="22" data-digits="2"/></k-form-item>
                <k-form-footer data-align="center" slot="footer">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ZG09.updateZG09" data-from="editZG09Form"
                    :data-model="formData" data-target="ZG09Grid" :handle-before="handleBefore">
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
            data-upload-url="/upload/server/RptApp/rhzg/uploadZG09.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="ZG09Grid" ref="submitBtn"
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
    name: "M07RHZG09",
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
          this.$refs.ZG09Grid.load(this.prodSearchParam);
        });
      }
    },
     activated() {
        if(this.$route.query.directedData && this.$route.query.directedData.dataId){
          this.$set(this.prodSearchParam, 'theoryReportStartDate', '');
          this.directedData = this.$route.query.directedData
          this.prodSearchParam.id = this.$route.query.directedData.dataId;
          this.$nextTick(()=>{
              this.$refs.ZG09Grid.load(this.prodSearchParam);
          });
        }
     },
    mounted() {},
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
          this.$refs.editZG09Popup.close();
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
          this.$refs.ZG09Grid.load(this.queryParam);
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
          this.$refs.ZG09Grid.load(this.prodSearchParam)

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
<style lang="scss" scoped>
.form-edit {
  /deep/ .el-form-item {
    display: flex;
    align-items: center;
    .el-form-item__label {
      line-height: 20px;
    }
    .el-form-item__content {
      margin-left: 0 !important;
    }
  }
}
</style>
