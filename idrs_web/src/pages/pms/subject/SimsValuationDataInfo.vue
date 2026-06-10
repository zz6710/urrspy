<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="searchFormRef" data-model-name="SimsValuationDataInfo" data-target="tableGrid" data-label-width="80px" v-model="queryParam">
        <k-form-item label="估值日期">
          <k-field-date v-model="searchParam.inputDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="资产代码">
          <k-field-text v-model="searchParam.icode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="底层代码">
          <k-field-text v-model="searchParam.bottomCode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="资产分类">
          <k-field-select v-model="searchParam.assetType" data-dict="sims_asset_type"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" ref="exportRef" class="btn-custom-plain"  data-functype="EXPORT" data-target="tableGrid" data-export-dict="true"
                :data-export-name="'SIMS底层估值明细表导入'" @downSuccess="downSuccess" :data-handler="handleExport" data-export-form="searchFormRef">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>

          <k-btn
					slot="button"
					ref="reloadBtnRef"
					class="btn-custom-plain"
					data-functype="POPUP"
					data-target="handleTaskPopup"
					loading-tip="正在导入委外专户估值表解析数据，请稍后重试！"
				>
					<md-icon>cloud_download</md-icon>
					导入委外专户估值表解析数据
				</k-btn>
        </div>
        <div class="right" v-show="vShow">
          &nbsp;&nbsp;导入开始时间：<span class="detail">{{ startTime }}</span>
          &nbsp;&nbsp;导入结束时间：<span class="detail">{{ endTime }}</span>
          &nbsp;&nbsp;导入日期：<span class="detail">{{ reportDate }}</span>
          &nbsp;&nbsp;导入结果：<a href="javascript:void(0)" class="detail1" @click="handleClick">{{ resultStatus }}</a>
          <k-btn class="btn-custom-icon" @click="queryRelust"> <md-icon md-src="/static/svg/reset.svg"></md-icon></k-btn>
        </div>
      </div>

      <k-grid ref="tableGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="150px" data-operate-column="false" :data-autoload="false" data-action="SimsValuationDataInfo.findSimsValuationDataInfos" >
        <k-grid-column data-align="left" data-header="资产代码" data-name="icode" data-width="200" ></k-grid-column>
        <k-grid-column data-align="left" data-header="组合代码" data-name="comcode" data-width="200" ></k-grid-column>
        <k-grid-column data-align="left" data-header="底层代码" data-name="bottomCode" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产分类" data-name="assetType" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="净价金额" data-name="netValue" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="市值" data-name="amount" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="成本" data-name="cost" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="币种" data-name="currency" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="科目代码" data-name="itemId" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="科目名称" data-name="itemName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易场所" data-name="tradePlace" data-width="100"></k-grid-column >
        <k-grid-column data-align="left" data-header="中债报送类别" data-name="zzReportType"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="估值日期" data-name="inputDate" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="导入日期" data-name="importDate" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="底层资产首次入库日期" data-name="dataInsrDt"   data-width="180"></k-grid-column>
      </k-grid>
    </div>

    <k-popup ref="handleTaskPopup" data-title="导入委外专户估值表解析数据">
        <k-form ref="handleTaskAppForm" data-ui="element">
          <k-form-item label="导入日期" data-ui="element" data-input-width="500px">
            <k-field-date v-model="formData.dealDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-default-value="currentDate" :data-allowblank="false"/>
          </k-form-item>
          <k-form-footer data-align="center">
           <k-btn ref="createFormBtnRef" class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
							<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
    </k-popup>

  </div>
</template>

<style lang="scss" scoped>
.right {
	font-size: 12px;
  font-weight: bold;
	.detail {
		margin: 0 2px;
		font-size: 12px;
		font-weight: normal;
	}
  .detail1 {
		margin: 0 2px;
		font-size: 12px;
		font-weight: normal;
    color: #417fffff;
	}
  .btn-custom-icon {
    background: #fff !important;
    border: 1px solid #fff !important;
    color: #417fffff !important;
    box-shadow: none;
    /deep/ path {
      fill: #417fffff !important;
    }
  }
}
</style>

<script>
import Tools from "@/utils/tools";
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js"
export default {
  name: "SimsValuationDataInfo",
  mixins: [ProdMixin],
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{},
      infoPop: {},
      queryParamDateRange: [],
      comfirmExportParam: {},
      currentDate:'',//定义当前日期回显使用
      menuId: "M061809",
      buttonName: "导入委外专户估值表解析数据",
      vShow: false,
      startTime: "",
      endTime: "",
      reportDate: "",
      resultStatus: "",
      resultInfo: "",
    };
  },
  created() {
    this.getCurrentDate();
    this.queryRelust();
  },
  computed: {
    queryParam () {
      return {
          'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          'inputDate': this.searchParam.inputDate,
          'icode': this.searchParam.icode,
          'bottomCode': this.searchParam.bottomCode,
          'assetType': this.searchParam.assetType,
      }
    }
  },
  watch: {
	},
  methods: {
    setConfirmExportParam() {
      this.comfirmExportParam = {
        beginDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        queryDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
      };
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    handleTaskApp() {
			if (this.$refs.handleTaskAppForm.validate()) {
        this.startTime = Tools.getCurrentTime("/", ":");
        this.endTime = "";
        this.reportDate = this.formData.dealDate;
        this.resultStatus = "正在"+this.buttonName+"中";
        this.resultInfo = "";

				this.$refs.reloadBtnRef.setIconStyle(0);
				this.httpUtil
					.comnUpdate({
						action: "SimsValuationDataInfo.impSimsValuationDataInfos",
						async: true,
						params: { 
              menuId: this.menuId,
              buttonName: this.buttonName,
              dealDate: this.formData.dealDate 
            },
            successAlert: false
					})
					.then((data) => {
            this.queryRelust();
						this.$refs.reloadBtnRef.setIconStyle(1);
					})
					.catch((err) => {
						console.log(err, "err");
            this.queryRelust();
						this.$refs.reloadBtnRef.setIconStyle(1);
					});
				setTimeout(() => {
					this.$refs.handleTaskPopup.close();
				}, 300);
			}
	  },
    getCurrentDate() {
        const timeOne = new Date();
        const year = timeOne.getFullYear();
        let month = timeOne.getMonth() + 1;
        let day = timeOne.getDate();
        month = month < 10 ? '0' + month : month;
        day = day < 10 ? '0' + day : day;
        this.currentDate = year + '' + month + '' + day;
    },
    queryRelust() {
        this.startTime = "";
        this.endTime = "";
        this.reportDate = "";
        this.resultStatus = "";
        this.resultInfo = "";

        this.httpUtil.comnQuery({
          action: "BaseReportReloadLog.findBaseReportReloadLogs",
          params: {
            menuId: this.menuId
          }
        }).then(data => {
          var rows = data.rows;
          if(rows.length>0) {
            this.vShow = true;
            this.startTime = rows[0].startTime;
            this.endTime = rows[0].endTime;
            this.reportDate = rows[0].reportDate;
            this.resultStatus = rows[0].resultStatus;
            this.resultInfo = rows[0].resultInfo;
          } else {
            this.vShow = false;
          }
        }).catch({});
    },
    handleClick() {
        if (this.resultInfo) {
          if (this.resultStatus.indexOf("成功") > 0) {
            Tools.alertTime(this.resultStatus + "，具体报表如下：<br>" + this.resultInfo, "success", 5000);
          } else if (this.resultStatus.indexOf("失败") > 0) {
            Tools.alertTime(this.resultStatus + "，具体原因如下：<br>" + this.resultInfo, "danger", 5000);
          }
        } else {
          if (this.resultStatus.indexOf("中") > 0) {
            Tools.alertTime(this.resultStatus + "，请稍后", "warning", 5000);
          }
        }
    },
  }
};
</script>
