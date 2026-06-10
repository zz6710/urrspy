<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="DwdProdFsfaConfirm" data-label-width="80px" v-model="searchParam" data-target="dwdProdFsfaConfirmGrid">
			<k-form-item label="产品代码/名称">
				<k-field-select
					v-model="searchParam.prdcCd"
					data-action="ProdInfoOds.findProdInfoOds"
					style="width: 100%"
					data-display-field="prodCode,prodName"
					data-value-field="prodCode"
					:data-remote="true"
					:data-remote-paging="true"
				/>
			</k-form-item>
			<k-form-item label="估值日期">
                <k-field-date v-model="searchParam.dtDt" data-type="date" data-date-format="yyyy-MM-dd"
                              data-value-format="yyyyMMdd"/>
            </k-form-item>
			<k-form-item label="估值表状态">
				<k-field-select v-model="searchParam.gzbStt" data-dict="gzbStt" data-dict-type="1"/>
			</k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="left">
        <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-export-dict="true" data-target="dwdProdFsfaConfirmGrid" data-export-name="日间导入估值表数据">
          <md-icon>cloud_download</md-icon>导出
        </k-btn>
      </div>
      <k-grid ref="dwdProdFsfaConfirmGrid" @data-row-select="selectRow" data-action="DwdProdFsfaConfirm.findDwdProdFsfaConfirms" data-operate-column="false" >
		<k-grid-column data-header="产品代码" data-name="prdcCd"></k-grid-column>
		<k-grid-column data-header="产品名称" data-name="prdcNm"></k-grid-column>
		<k-grid-column data-header="估值日期" data-name="dtDt"></k-grid-column>
		<k-grid-column data-header="估值表状态" data-name="gzbStt" data-dict="gzbStt" data-dict-type="1"></k-grid-column>
		<k-grid-column data-header="导入时间" data-name="gnrtTm"></k-grid-column>
		<k-grid-column data-header="操作员" data-name="optNaem"></k-grid-column>
      </k-grid>
    </div>
    
	<!--    添加日间导入估值表数据查询弹出框   -->
	<k-popup ref="addDwdProdFsfaConfirmPopup" data-title="添加">
    	<k-form ref="addDwdProdFsfaConfirmForm" :data-col="2">
			<k-form-item label="主键">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="处理日期">
	        	<k-field-text v-model="formData.dealDate"/>
	     	</k-form-item>
			<k-form-item label="产品代码">
	        	<k-field-text v-model="formData.prdcCd"/>
	     	</k-form-item>
			<k-form-item label="产品名称">
	        	<k-field-text v-model="formData.prdcNm"/>
	     	</k-form-item>
			<k-form-item label="估值日期">
	        	<k-field-text v-model="formData.dtDt"/>
	     	</k-form-item>
			<k-form-item label="估值表状态">
	        	<k-field-text v-model="formData.gzbStt"/>
	     	</k-form-item>
			<k-form-item label="导入时间">
	        	<k-field-text v-model="formData.gnrtTm"/>
	     	</k-form-item>
			<k-form-item label="操作员">
	        	<k-field-text v-model="formData.optNaem"/>
	     	</k-form-item>
	  	
	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwdProdFsfaConfirm.addDwdProdFsfaConfirm" data-from="addDwdProdFsfaConfirmForm"
                     :data-model="formData" data-target="dwdProdFsfaConfirmGrid">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>
    
	<!--    修改日间导入估值表数据查询弹出框   -->
	<k-popup ref="editDwdProdFsfaConfirmPopup" data-title="编辑">
	  <k-form ref="editDwdProdFsfaConfirmForm" :data-col="2">
		<k-form-item label="主键">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="处理日期">
        	<k-field-text v-model="formData.dealDate"/>
     	</k-form-item>
		<k-form-item label="产品代码">
        	<k-field-text v-model="formData.prdcCd"/>
     	</k-form-item>
		<k-form-item label="产品名称">
        	<k-field-text v-model="formData.prdcNm"/>
     	</k-form-item>
		<k-form-item label="估值日期">
        	<k-field-text v-model="formData.dtDt"/>
     	</k-form-item>
		<k-form-item label="估值表状态">
        	<k-field-text v-model="formData.gzbStt"/>
     	</k-form-item>
		<k-form-item label="导入时间">
        	<k-field-text v-model="formData.gnrtTm"/>
     	</k-form-item>
		<k-form-item label="操作员">
        	<k-field-text v-model="formData.optNaem"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwdProdFsfaConfirm.updateDwdProdFsfaConfirm" data-from="editDwdProdFsfaConfirmForm"
	        :data-model="formData" data-target="dwdProdFsfaConfirmGrid">
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
  export default {
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam: {
			dtDt: this.getCurrentDate()
		}
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
	  getCurrentDate() {
		const now = new Date();
		const timeOne = new Date(now.getTime() - 86400000);
		const year = timeOne.getFullYear();
		let month = timeOne.getMonth() + 1;
		let day = timeOne.getDate();
		month = month < 10 ? '0' + month : month;
		day = day < 10 ? '0' + day : day;
		return year + '' + month + '' + day;
	  },
    }
  };
</script>
