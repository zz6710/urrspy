<template>
  <div class="py-page">
    <div>
      <k-form-search-customize  v-model="queryParam"   data-target="trusteeGrid">
        <k-form-item label="托管行代码">
          <k-field-text v-model="queryParam.trusteeCode"  ></k-field-text>
        </k-form-item>
        <k-form-item label="托管行名称">
          <k-field-text v-model="queryParam.trusteeName"  ></k-field-text>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" slot="button" :data-handler="()=>this.formData={}" data-target="addTrusteePopup" v-if="global.isShowAuthorityButton('TrusteeModel.addTrustee')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="trusteeGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="300px" data-action="TrusteeModel.findTrustee"
              v-if="global.isShowAuthorityButton('TrusteeModel.findTrustee')">
      <k-grid-column data-align="left" data-header="组织机构代码" data-name="trusteeAcctCode"></k-grid-column>
<!--      <k-grid-column data-align="left" data-header="托管行性质" data-name="trusteeProperty" data-dict="trustee_property" ></k-grid-column>-->
      <k-grid-column data-align="left" data-header="托管行代码" data-name="trusteeCode"></k-grid-column>
      <k-grid-column data-align="left" data-header="托管行名称" data-name="trusteeName" ></k-grid-column>
      <k-grid-column data-align="left" data-header="所在地区" data-name="trusteeCountry" data-dict="tr_in_out_sign"></k-grid-column>
      <k-grid-column  data-align="left" data-header="国家名称" data-name="trusteeCountryName"  data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope">
         <k-btn class="btn-custom-text" data-descript="修改托管行信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('TrusteeModel.updateTrustee')"
                 data-target="editTrusteePopup">
            修改
          </k-btn>
<!--          <k-btn class="btn-custom-plain" data-descript="详情" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('TrusteeModel.addTrusteeModel')"-->
<!--                 data-target="collectTrusteePopup">-->
<!--            详情-->
<!--          </k-btn>-->
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="TrusteeModel.deleteTrustee" data-size="mini" v-if="global.isShowAuthorityButton('TrusteeModel.deleteTrustee')"
                 data-type="danger" data-target="trusteeGrid" :data-confirm="true" data-descript="删除托管行信息">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

<!--    <k-popup ref="collectTrusteePopup" data-title="详情" >-->
<!--      <t8-org-sheet-collection  :parentFormData="formData"-->
<!--      ></t8-org-sheet-collection>-->
<!--    </k-popup>-->

	<!--    添加Trustee弹出框   -->
	<k-popup ref="addTrusteePopup" data-title="新增托管行信息">
    	<k-form ref="addTrusteeForm" :data-col="2">
			<k-form-item label="托管行机构代码">
	        	<k-field-text v-model="formData.trusteeCode" :data-allowblank="false" />
	     	</k-form-item>
			<k-form-item label="托管行名称">
	        	<k-field-text v-model="formData.trusteeName" :data-allowblank="false" />
	     	</k-form-item>

        <k-form-item label="组织机构代码">
          <k-field-text v-model="formData.trusteeAcctCode" :data-allowblank="false"/>
        </k-form-item>

			<k-form-item label="所在地区">
	        	<k-field-select v-model="formData.trusteeCountry" data-dict="tr_in_out_sign" :data-allowblank="false" />
	     	</k-form-item>
<!--			<k-form-item label="托管行性质">-->
<!--	        	<k-field-select v-model="formData.trusteeProperty" data-dict="trustee_property" :data-allowblank="false" />-->
<!--	     	</k-form-item>-->



	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrusteeModel.addTrustee" data-from="addTrusteeForm"
		               :data-model="formData" data-target="trusteeGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    补录Trustee弹出框   -->
	<k-popup ref="editTrusteePopup" data-title="修改托管行信息">
	  <k-form ref="editTrusteeForm" :data-col="2">
      <k-form-item label="托管行机构代码">
        <k-field-text v-model="formData.trusteeCode" :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="托管行名称">
        <k-field-text v-model="formData.trusteeName" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="组织机构代码">
        <k-field-text v-model="formData.trusteeAcctCode" :data-allowblank="false" />
      </k-form-item>

      <k-form-item label="所在地区">
        <k-field-select v-model="formData.trusteeCountry" data-dict="tr_in_out_sign" :data-allowblank="false" />
      </k-form-item>
<!--      <k-form-item label="托管行性质">-->
<!--        <k-field-select v-model="formData.trusteeProperty" data-dict="trustee_property" :data-allowblank="false" />-->
<!--      </k-form-item>-->

	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrusteeModel.updateTrustee" data-from="editTrusteeForm"
	        :data-model="formData" data-target="trusteeGrid">
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
  // import TrusteeCollection from "@/pages/pms/trustee/TrusteeCollection";
  export default {
    name: "Trustee",
    data() {
      return {
        formData: {},
        selectRowData: {},
        queryParam:{},
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
