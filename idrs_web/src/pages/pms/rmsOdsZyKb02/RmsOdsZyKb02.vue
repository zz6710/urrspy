<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="RmsOdsZyKb02" data-label-width="80px" v-model="searchParam" data-target="rmsOdsZyKb02Grid">
			<k-form-item label="数据日期">
        <k-field-date v-model="searchParam.dataDate" data-type="date" data-date-format="yyyy-MM-dd"
                      data-value-format="yyyyMMdd"/>
			</k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
			<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addRmsOdsZyKb02Popup">
				<md-icon md-src="/static/svg/add.svg" />新增</k-btn>
		</div>
      <k-grid ref="rmsOdsZyKb02Grid" @data-row-select="selectRow" data-action="RmsOdsZyKb02.findRmsOdsZyKb02s" >
		<k-grid-column data-header="主键" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="数据日期" data-name="dataDate"></k-grid-column>
		<k-grid-column data-header="企业从业人员人工成本-本年累计（元）" data-name="d01"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-functype="POPUP" data-size="mini"
            data-target="editRmsOdsZyKb02Popup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="RmsOdsZyKb02.deleteRmsOdsZyKb02" data-size="mini"
               data-type="danger" data-target="rmsOdsZyKb02Grid" :data-confirm="true" data-descript="删除">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加KB02.主要经济指标表（企月快02表）补录表弹出框   -->
	<k-popup ref="addRmsOdsZyKb02Popup" data-title="新增">
    	<k-form ref="addRmsOdsZyKb02Form" :data-col="2">
			<k-form-item label="数据日期">
	        	<k-field-date v-model="formData.dataDate" data-type="date" data-date-format="yyyy-MM-dd" @data-on-change="dateOnChange"
                          data-value-format="yyyyMMdd"/>
	     	</k-form-item>
			<k-form-item label="企业从业人员人工成本-本年累计（元）">
	        	<k-field-text v-model="formData.d01"/>
	     	</k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="RmsOdsZyKb02.addRmsOdsZyKb02" data-from="addRmsOdsZyKb02Form"
                 :data-model="formData" data-target="rmsOdsZyKb02Grid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改KB02.主要经济指标表（企月快02表）补录表弹出框   -->
	<k-popup ref="editRmsOdsZyKb02Popup" data-title="修改">
	  <k-form ref="editRmsOdsZyKb02Form" :data-col="2">
    <k-form-item label="主键" v-show="false">
      <k-field-text v-model="formData.id"/>
    </k-form-item>
		<k-form-item label="数据日期" :class="[handleItemDiff('dataDate')]">
        	<k-field-date v-model="formData.dataDate" data-type="date" data-date-format="yyyy-MM-dd"
                        data-value-format="yyyyMMdd"/>
     	</k-form-item>
		<k-form-item label="企业从业人员人工成本-本年累计（元）" :class="[handleItemDiff('d01')]">
        	<k-field-text v-model="formData.d01"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="RmsOdsZyKb02.updateRmsOdsZyKb02" data-from="editRmsOdsZyKb02Form"
	        :data-model="formData" data-target="rmsOdsZyKb02Grid" :handle-before="handleBefore">
			  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
			  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  export default {
    data() {
      return {
        formData: {},
        formDataCopy: {},
        selectRowData: {},
        searchParam: {}
      };
    },
    methods: {
      handleBefore() {
        if (this.formNoChangeCb()) {
          this.$refs.editRmsOdsZyKb02Popup.close();
          return false
        }
        return true
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
        this.formDataCopy = Object.assign({}, row)
      },

      dateOnChange(date) {
        let curDate = new Date(Tools.formatDate(date));
        let nextDate = new Date(curDate.getTime() + 24*60*60*1000).getDate();
        if(nextDate !== 1) {
          Tools.alert("请选择月份最后一天", "danger");
        }
      }

    }
  };
</script>
