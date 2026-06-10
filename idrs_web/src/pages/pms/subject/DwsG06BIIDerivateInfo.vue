<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="DwsG06BIIDerivateInfo" v-model="searchParam" data-target="tableGrid" data-label-width="150px">
      <k-form-item label="数据日期">
        <k-field-date v-model="searchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
      </k-form-item>
			<k-form-item label="理财投资资产代码">
          <k-field-text v-model="searchParam.icode"/>
      </k-form-item>
			<k-form-item label="底层代码">
          <k-field-text v-model="searchParam.assetCode"/>
      </k-form-item>
			<k-form-item label="G06穿透底层分类">
          <k-field-select v-model="searchParam.g06Type" data-dict="g06_scd_type_ysp" data-dict-type="1" />
      </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-export-dict="true" data-target="tableGrid" data-export-name="G06bII衍生品明细表">
            <md-icon>cloud_download</md-icon>导出
          </k-btn>
          <k-btn slot="button" ref="reloadBtnRef" class="btn-custom-plain" data-functype="POPUP" data-target="handleTaskPopup" loading-tip="正在重新生成报表，请稍后重试！">
            <md-icon>cloud_download</md-icon>重新生成报表
				  </k-btn>
        </div>
        <ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
      </div>
      <k-grid ref="tableGrid" @data-row-select="selectRow" data-action="DwsG06BIIDerivateInfo.findDwsG06BIIDerivateInfo" :dataAutoload="false" data-dict-type="1"
        data-operate-width="120px" data-fixed="right">
        <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
		    <k-grid-column data-header="理财投资资产代码" data-name="icode"></k-grid-column>
		    <k-grid-column data-header="资管产品名称（第一层）" data-name="inamec1"></k-grid-column>
		    <k-grid-column data-header="底层代码" data-name="assetCode" ></k-grid-column>
		    <k-grid-column data-header="G06穿透底层分类" data-name="g06Type" data-dict="g06_scd_type_ysp"></k-grid-column>
		    <k-grid-column data-header="投资经理" data-name="invManager"></k-grid-column>
		    <k-grid-column data-header="衍生品业务类型" data-name="derBusTyp" data-dict="ysp_asset_typ"></k-grid-column>
		    <k-grid-column data-header="是否标准化衍生品" data-name="ifStandDer" data-dict="1yes2no"></k-grid-column>
		    <k-grid-column data-header="填报数据说明" data-name="dataInfo"></k-grid-column>
		    <k-grid-column data-header="填报数据（元）" data-name="netValue" data-digits="2"></k-grid-column>
		    <k-grid-column data-header="系数" data-name="coef"></k-grid-column>
		    <k-grid-column data-header="投资规模（元）" data-name="invValue" data-digits="2"></k-grid-column>
        <k-grid-column data-header="数据日期" data-type="date" data-name="reportDate"></k-grid-column>

        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改g06bII衍生品明细中间表" data-functype="POPUP" data-size="mini"
            data-target="editPopup">修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DwsG06BIIDerivateInfo.deleteDwsG06BIIDerivateInfo" data-size="mini"
                 data-type="danger" data-target="tableGrid" :data-confirm="true" data-descript="删除G06bII衍生品明细中间表">删除
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--  修改g06bII衍生品明细中间表弹出框   -->
	<k-popup ref="editPopup" data-title="修改">
	  <k-form ref="editForm" :data-col="2">

      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.id"/>
      </k-form-item>
      <k-form-item label="理财投资资产代码" :class="[handleItemDiff('icode')]">
        <k-field-text v-model="formData.icode" />
      </k-form-item>
      <k-form-item label="资管产品名称（第一层）" :class="[handleItemDiff('inamec1')]">
          <k-field-text v-model="formData.inamec1" />
      </k-form-item>
      <k-form-item label="底层代码" :class="[handleItemDiff('assetCode')]">
          <k-field-text v-model="formData.assetCode" />
      </k-form-item>
      <k-form-item label="G06穿透底层分类" :class="[handleItemDiff('g06Type')]">
          <k-field-select v-model="formData.g06Type" data-dict="g06_scd_type" data-dict-type="1"/>
      </k-form-item>
      <k-form-item label="投资经理" :class="[handleItemDiff('invManager')]">
          <k-field-text v-model="formData.invManager"/>
      </k-form-item>
      <k-form-item label="衍生品业务类型" :class="[handleItemDiff('derBusTyp')]">
          <k-field-select v-model="formData.derBusTyp" data-dict="ysp_asset_typ" data-dict-type="1" @data-on-change="dataOnChange"/>
      </k-form-item>
      <k-form-item label="是否标准化衍生品" :class="[handleItemDiff('ifStandDer')]">
          <k-field-select v-model="formData.ifStandDer" data-dict="1yes2no" data-dict-type="1"/>
      </k-form-item>
      <k-form-item label="填报数据说明" :class="[handleItemDiff('dataInfo')]">
          <k-field-text v-model="formData.dataInfo" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="填报数据（元）" :class="[handleItemDiff('netValue')]">
          <k-field-text v-model="formData.netValue" @data-on-change="dataOnChange"/>
      </k-form-item>
      <k-form-item label="系数" :class="[handleItemDiff('coef')]">
          <k-field-text v-model="formData.coef" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资规模" :class="[handleItemDiff('invValue')]">
          <k-field-text v-model="formData.invValue" />
      </k-form-item>

	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsG06BIIDerivateInfo.updateDwsG06BIIDerivateInfo" data-from="editForm"
	        :data-model="formData" data-target="tableGrid" :handle-before="handleBefore">
			  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
			  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

  <k-popup ref="handleTaskPopup" data-title="重新生成报表">
			<k-form ref="handleTaskAppForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
					<k-field-date
						v-model="formData.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
import AssetMixin from "@/pages/pms/subject/mixins/assetMixin.js";
import moment from "moment";
import ReReport from "@/utils/ReReport.vue";
  export default {
    name: "DwsG06BIIDerivateInfo",
    components: {
      ReReport
    },
    mixins: [AssetMixin],
    data() {
      return {
        formData: {},
        formDataCopy: {},
        searchParam: {},
        menuId: "M061806",
        buttonName: "重新生成报表",
      }
    },
    computed: {
      lastDay() {
        if (this.formData.reportDate) {
          return moment([this.formData.reportDate.split("-")[0], this.formData.reportDate.split("-")[1] - 1])
            .endOf("month")
            .format("YYYYMMDD");
        }
        return "";
      },
    },
    methods: {
      handleBefore() {
        if (this.formNoChangeCb()) {
          this.$refs.editPopup.close();
          return false
        }
        return true
      },
      handleTaskApp() {
        this.$refs.reReportRef.handleReports(this.lastDay);
      },
        dataOnChange(){
            if (this.formData.derBusTyp == '01') {/*债券远期01*/
              this.$set(this.formData, 'dataInfo', '合约名义价值总额');
              this.$set(this.formData, 'coef', '50%');
            } else if (this.formData.derBusTyp == '02') {/*国债期货02*/
              this.$set(this.formData, 'dataInfo', '合约名义价值总额');
              this.$set(this.formData, 'coef', '5%');
            } else if (this.formData.derBusTyp == '03') {/*利率互换03*/
              this.$set(this.formData, 'dataInfo', '合约名义价值总额');
              this.$set(this.formData, 'coef', '3%');
            } else if (this.formData.derBusTyp == '04') {/*股指期货04*/
              this.$set(this.formData, 'dataInfo', '合约名义价值总额');
              this.$set(this.formData, 'coef', '15%');
            } else if (this.formData.derBusTyp == '05') {/*权益互换05*/
               this.$set(this.formData, 'dataInfo', '合约名义价值总额');
               this.$set(this.formData, 'coef', '10%');
            } else if (this.formData.derBusTyp == '06') {/*商品衍生品06*/
               this.$set(this.formData, 'dataInfo', '合约名义价值总额');
               this.$set(this.formData, 'coef', '15%');
            } else if (this.formData.derBusTyp == '07') {/*外汇衍生品07*/
               this.$set(this.formData, 'dataInfo', '合约名义价值总额');
               this.$set(this.formData, 'coef', '3%');
            } else if (this.formData.derBusTyp == '08') {/*买入期权08*/
               this.$set(this.formData, 'dataInfo', '权利金价格之和');
               this.$set(this.formData, 'coef', '100%');
            } else if (this.formData.derBusTyp == '09') {/*卖出场内期权09*/
               this.$set(this.formData, 'dataInfo', '相关工具本金值*Delta值');
               this.$set(this.formData, 'coef', '15%');
            } else if (this.formData.derBusTyp == '10') {/*卖出场外期权10*/
               this.$set(this.formData, 'dataInfo', '该期权在给定压力情形下的最大损失的5倍计算，且不低于名义金额的5‰；给定压力情形为期权标的价格以当前价格为基础上下波动20%');
               this.$set(this.formData, 'coef', '100%');
            } else if (this.formData.derBusTyp == '11') {/*买入信用衍生品11*/
               this.$set(this.formData, 'dataInfo', '账面价值');
               this.$set(this.formData, 'coef', '100%');
            } else if (this.formData.derBusTyp == '12') {/*其他衍生品12*/
               this.$set(this.formData, 'dataInfo', '合约名义价值');
               this.$set(this.formData, 'coef', '100%');
            } else {
               this.$set(this.formData, 'dataInfo', null);
               this.$set(this.formData, 'coef', null);
            }
            debugger;
            this.$set(this.formData, 'invValue', (this.formData.netValue * this.formData.coef.substring(0, this.formData.coef.length - 1) / 100).toFixed(2));/*填报数据（元）x 系数,保留两位小数*/
        },
    }
  };
</script>
