<!--
 * @Author: litao
 * @Date: 2022-07-13 10:18:46
 * @LastEditTime: 2022-07-19 16:10:33
 * @LastEditors: litao
 * @Description: 资产负债信息
 * @FilePath: \idrs_web\src\pages\report\rhzj\M07RHZJ03.vue
-->
<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="tabClick">
      <el-tab-pane label="资产负债信息" name="1">
          <k-form-search-customize data-model-name="ReportPVD" data-target="reportPVDGrid" v-model="queryParam">
            <k-form-item label="行内产品代码" data-label-width="150px">
              <k-field-text v-model="prodSearchParam.prodCode" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="查询日期">
              <k-field-date v-model="prodSearchParam.reportDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
            </k-form-item>
            <k-btn ref="assetsPVDSend" slot="button" style="width: 120px" class="md-success"
              data-descript="资产负债数据报送" data-size="small" @click="generatePBFile('assetsPVDSend')">
              <!-- <md-icon>cloud_download</md-icon> -->
              资产负债数据报送
            </k-btn>
            <k-btn slot="button" style="width: 120px" class="btn-custom-plain" data-target="reportPVDGrid" :data-export-name="'资产负债信息'"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ReportPVD.findReportPVDs">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
            <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </k-form-search-customize>
          <div>
            <k-grid ref="reportPVDGrid" @data-row-select="selectRow" data-action="ReportPVD.findReportPVDs"  data-fixed="right">
                <k-grid-column data-header="报送日期" data-name="reportDate" data-type="date"></k-grid-column>
                <k-grid-column data-header="资产池代码" data-name="pbcAssetscode"></k-grid-column>
                <k-grid-column data-header="行内产品代码" data-name="prodCode"></k-grid-column>
                <k-grid-column data-header="数据种类" data-name="dataType" data-dict="t8_zf_data_type"></k-grid-column>
                <k-grid-column data-header="币种代码" data-name="cny"></k-grid-column>
                <k-grid-column data-header="期末余额" data-name="endAmount" data-type="money" data-align="right"></k-grid-column>
                <k-grid-column data-header="期末余额折人民币" data-name="endAmountRmb" data-type="money" data-align="right"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                <k-btn class="md-info md-just-icon md-simple" data-descript="修改资产负债信息" data-functype="POPUP" data-size="mini"
                    data-target="editReportPVDPopup">
                    <md-icon>edit</md-icon>
                </k-btn>
                <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ReportPVD.deleteReportPVD" data-size="mini"
                    data-type="danger" data-target="reportPVDGrid" :data-confirm="true" data-descript="删除">
                    <md-icon>close</md-icon>
                </k-btn>
                </template>
            </k-grid>
          </div>
          <!--    修改资产负债信息弹出框   -->
          <k-popup ref="editReportPVDPopup" data-title="修改资产负债信息">
            <k-form ref="editReportPVDForm" :data-col="2">
                <k-form-item label="报送日期">
                    <k-field-text v-model="formData.reportDate" :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="产品代码">
                    <k-field-text v-model="formData.prodCode"  :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="资产池代码">
                    <k-field-text v-model="formData.pbcAssetscode" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="数据种类">
                    <k-field-select v-model="formData.dataType" data-dict="t8_zf_data_type" :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="币种代码">
                    <k-field-select v-model="formData.cny" data-dict="money_flag" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="期末余额">
                    <k-field-text v-model="formData.endAmount" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="期末余额折人民币">
                    <k-field-text v-model="formData.endAmountRmb" :data-allowblank="false"/>
                </k-form-item>
                <k-form-footer data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportPVD.updateReportPVD" data-from="editReportPVDForm"
                    :data-model="formData" data-target="reportPVDGrid">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                    <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                </k-form-footer>
            </k-form>
          </k-popup>
      </el-tab-pane>
      <el-tab-pane label="股票及其他股权资产" name="2">
        <k-form-search-customize data-model-name="ReportPVD3" data-target="reportPVD3Grid" v-model="queryOtherParam">
            <k-form-item label="行内产品代码" data-label-width="150px">
              <k-field-text v-model="prodOtherSearchParam.prodCode" data-validate-type="text"/>
            </k-form-item>
            <k-form-item label="查询日期">
              <k-field-date v-model="prodOtherSearchParam.reportDate" data-type="month"  data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
            </k-form-item>
            <k-btn slot="button" style="width: 120px" class="btn-custom-plain" data-target="reportPVD3Grid" :data-export-name="'股票及其他股权资产'"
              data-descript="报送数据导出" data-functype="EXPORT" data-size="small"
              data-url="ReportPVD3.findReportPVD3s">
              <md-icon>cloud_download</md-icon>
              报送数据导出
            </k-btn>
            <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-plain"
                data-target="addPopup">
              <md-icon>cloud_upload</md-icon>
              报送数据导入
            </k-btn>
          </k-form-search-customize>
          <k-grid ref="reportPVD3Grid" @data-row-select="selectRow" data-action="ReportPVD3.findReportPVD3s"  data-fixed="right">
            <k-grid-column data-header="日期" data-name="reportDate" data-type="date"></k-grid-column>
            <k-grid-column data-header="行内产品代码" data-name="prodCode"></k-grid-column>
            <k-grid-column data-header="资产池代码" data-name="pbcAssetscode"></k-grid-column>
            <k-grid-column data-header="股权种类" data-name="stockType" data-dict="t8_stock_type_dat"></k-grid-column>
            <k-grid-column data-header="信托产品发起机构编码" data-name="orgno"></k-grid-column>
            <k-grid-column data-header="信托产品代码" data-name="productCode"></k-grid-column>
            <k-grid-column data-header="币种代码" data-name="cny"></k-grid-column>
            <k-grid-column data-header="产品金额" data-name="prodAmount" data-type="money" data-align="right"></k-grid-column>
            <k-grid-column data-header="产品金额折人民币" data-name="prodAmountRmb" data-type="money" data-align="right"></k-grid-column>
            <template slot="operate" slot-scope="scope">
            <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP" data-size="mini"
                data-target="editReportPVD3Popup">
                <md-icon>edit</md-icon>
            </k-btn>
            <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ReportPVD3.deleteReportPVD3" data-size="mini"
                data-type="danger" data-target="reportPVD3Grid" :data-confirm="true" data-descript="删除">
                <md-icon>close</md-icon>
            </k-btn>
            </template>
          </k-grid>
          <!--    修改人行资金信托PVD3弹出框   -->
          <k-popup ref="editReportPVD3Popup" data-title="修改">
            <k-form ref="editReportPVD3Form" :data-col="2">
                <k-form-item label="id">
                    <k-field-text v-model="formOtherData.id" :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="日期">
                    <k-field-text v-model="formOtherData.reportDate" :data-allowblank="false" :data-disabled="true"/>
                </k-form-item>
                <k-form-item label="资产池代码">
                    <k-field-text v-model="formOtherData.pbcAssetscode" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="股权种类">
                    <k-field-select v-model="formOtherData.stockType" :data-allowblank="false" data-dict="t8_stock_type_dat"/>
                </k-form-item>
                <k-form-item label="产品发起机构编码">
                    <k-field-text v-model="formOtherData.orgno" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="产品代码">
                    <k-field-text v-model="formOtherData.prodCode" :data-allowblank="false" />
                </k-form-item>
                <k-form-item label="币种代码">
                    <k-field-select v-model="formOtherData.cny" data-dict="money_flag" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="产品金额">
                    <k-field-text v-model="formOtherData.prodAmount" :data-digits="2" data-validate-type="number" :data-max-length="16"/>
                </k-form-item>
                <k-form-item label="产品金额折人民币">
                    <k-field-text v-model="formOtherData.prodAmountRmb"  :data-digits="2" data-validate-type="number" :data-max-length="16"/>
                </k-form-item>
                <k-form-footer data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportPVD3.updateReportPVD3" data-from="editReportPVD3Form"
                    :data-model="formOtherData" data-target="reportPVD3Grid">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                    <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                </k-form-footer>
            </k-form>
          </k-popup>
      </el-tab-pane>
    </el-tabs>
    <k-popup ref="addPopup" title="报送数据导入">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                :data-upload-url="`/upload/server/RptApp/reportPVD/${this.activeName === '1'? 'uploadPVD.json':'uploadPVD3.json'}`">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-target="this.activeName === '1' ? 'reportPVDGrid': 'reportPVD3Grid'" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
          </k-form-footer>
        </k-form>
    </k-popup>
  </div>
</template>

<script>
export default {
    name: "M07RHZJ03",
    data () {
        return {
            activeName: '1',
            prodSearchParam: {
                prodCode: '',
                reportDate: localStorage.getItem('currentWorkday')
            },
            prodOtherSearchParam: {
                prodCode: '',
                reportDate: localStorage.getItem('currentWorkday').substring(0, 6)
            },
            formData: {},
            formOtherData: {}
        }
    },
    mounted() {
        console.log('--mounted--')
        this.tabClick()
    },
    computed: {
      queryParam () {
        return {
            ...this.prodSearchParam
        }
      },
      queryOtherParam () {
        return {
            ...this.prodOtherSearchParam
        }
      }
    },
    methods: {
        submitUploadParam() {
			//文件上传校验
			let validate = this.$refs.addForm.validate();
			if (validate) {
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					this.$refs.uploadRef.upload();
				} else {
					this.$message.error("上传文件不能为空!");
					return false;
				}
			}
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.addPopup.close();
			this.$refs[this.activeName === '1' ? 'reportPVDGrid' : 'reportPVD3Grid'].load();
		},
		onSubmitError() {
			this.$refs.uploadRef.doReset();
			this.$refs.submitBtn.setIconStyle(1, []);
		},
        tabClick(tab, event) {
            if (this.activeName === '1') {
                this.$refs.reportPVDGrid.load(this.prodSearchParam)
            } else {
                this.$refs.reportPVD3Grid.load(this.prodOtherSearchParam)
            }
        },
        selectRow(row, column, event) {
            if (this.activeName === '1') {
                this.formData = Object.assign({}, row)
            } else {
                this.formOtherData = Object.assign({}, row)
            }
        },
        generatePBFile(type) {
            if (this.prodSearchParam.reportDate == null || this.prodSearchParam.reportDate == "") {
                this.$message.error('请在查询框中选择查询日期')
                return;
            }
            this.$refs.assetsPVDSend.setLoading(true)
            let message = ''
            this.httpUtil.comnQuery({
              action: "ReportPVD.validateReportPVDsAmount",
              params: { reportDate: this.queryParam.reportDate }
            }).then(data => {
                if (data.rows.length > 0) {
                    if (data.rows.length <= 100) {
                        data.rows.forEach(r => {
                            message += "产品代码:" + r.prodCode + "人行代码:" + r.pbcAssetscode + "数据类型:" + r.dataType + "余额为负;"
                        })
                    } else {
                        message = "余额为负的数据超过100条";
                    }
                }
            })
            this.httpUtil.comnQuery({
              action: "ReportPVD.validateReportPVDsSum",
              params: { reportDate: this.queryParam.reportDate }
            }).then(data => {
                if (data.rows.length > 0) {
                    data.rows.forEach(r => {
                        var isEqual = r.isEqual;
                        if (isEqual == 0) {
                            message +="产品代码：" + r.prod_code +",人行代码:" + r.pbc_assetscode+",校验项:" + r.data_type ;
                        }
                    })
                }
            })
            if (message) {
                this.$message.warning(message)
            }
            this.httpUtil.download({
                url: '/download/server/RptApp/reportPPI/download.json',
                params: { ...this.prodSearchParam, sendType: type },
                callback: () => {
                    this.$refs.assetsPVDSend.setLoading(false)
                }
            })
        }
    }

}
</script>
