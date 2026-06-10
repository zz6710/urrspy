<template>
  <div>
    <div>
      <k-form-search data-model-name="T8ProdSalesTarget" data-target="t8ProdSalesTargetGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT8ProdSalesTargetPopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="t8ProdSalesTargetGrid" @data-row-select="selectRow" data-action="T8ProdSalesTarget.findT8ProdSalesTargets" >
		<k-grid-column data-header="序号id" data-name="id"></k-grid-column>
		<k-grid-column data-header="年度" data-name="year"></k-grid-column>
		<k-grid-column data-header="季度" data-name="season"></k-grid-column>
		<k-grid-column data-header="月份" data-name="month"></k-grid-column>
		<k-grid-column data-header="年度销售目标" data-name="yearSalesTarget"></k-grid-column>
		<k-grid-column data-header="季度销售目标" data-name="seasonSalesTarget"></k-grid-column>
		<k-grid-column data-header="月度销售目标" data-name="monthSalesTarget"></k-grid-column>
		<k-grid-column data-header="创建时间" data-name="updateTime"></k-grid-column>
		<k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改驾驶舱销售目标表" data-functype="POPUP" data-size="mini"
            data-target="editT8ProdSalesTargetPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdSalesTarget.deleteT8ProdSalesTarget" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除驾驶舱销售目标表">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加驾驶舱销售目标表弹出框   -->
	<k-popup ref="addT8ProdSalesTargetPopup" data-title="新增">
    	<k-form ref="addT8ProdSalesTargetForm" :data-col="2">
			<k-form-item label="序号id">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="年度">
	        	<k-field-text v-model="formData.year"/>
	     	</k-form-item>
			<k-form-item label="季度">
	        	<k-field-text v-model="formData.season"/>
	     	</k-form-item>
			<k-form-item label="月份">
	        	<k-field-text v-model="formData.month"/>
	     	</k-form-item>
			<k-form-item label="年度销售目标">
	        	<k-field-text v-model="formData.yearSalesTarget"/>
	     	</k-form-item>
			<k-form-item label="季度销售目标">
	        	<k-field-text v-model="formData.seasonSalesTarget"/>
	     	</k-form-item>
			<k-form-item label="月度销售目标">
	        	<k-field-text v-model="formData.monthSalesTarget"/>
	     	</k-form-item>
			<k-form-item label="创建时间">
	        	<k-field-text v-model="formData.updateTime"/>
	     	</k-form-item>
			<k-form-item label="备注">
	        	<k-field-text v-model="formData.remark"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSalesTarget.addT8ProdSalesTarget" data-from="addT8ProdSalesTargetForm"
		               :data-model="formData" data-target="t8ProdSalesTargetGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改驾驶舱销售目标表弹出框   -->
	<k-popup ref="editT8ProdSalesTargetPopup" data-title="修改">
	  <k-form ref="editT8ProdSalesTargetForm" :data-col="2">
		<k-form-item label="序号id">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="年度">
        	<k-field-text v-model="formData.year"/>
     	</k-form-item>
		<k-form-item label="季度">
        	<k-field-text v-model="formData.season"/>
     	</k-form-item>
		<k-form-item label="月份">
        	<k-field-text v-model="formData.month"/>
     	</k-form-item>
		<k-form-item label="年度销售目标">
        	<k-field-text v-model="formData.yearSalesTarget"/>
     	</k-form-item>
		<k-form-item label="季度销售目标">
        	<k-field-text v-model="formData.seasonSalesTarget"/>
     	</k-form-item>
		<k-form-item label="月度销售目标">
        	<k-field-text v-model="formData.monthSalesTarget"/>
     	</k-form-item>
		<k-form-item label="创建时间">
        	<k-field-text v-model="formData.updateTime"/>
     	</k-form-item>
		<k-form-item label="备注">
        	<k-field-text v-model="formData.remark"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSalesTarget.updateT8ProdSalesTarget" data-from="editT8ProdSalesTargetForm"
	        :data-model="formData" data-target="t8ProdSalesTargetGrid">
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
        selectRowData: {}
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    }
  };
</script>
