<template>
  <div>
    <div v-show="isShow==='1'">
      <k-form-search data-model-name="DisclosureRegularMajorAsset" data-target="disclosureRegularMajorAssetGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addDisclosureRegularMajorAssetPopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="disclosureRegularMajorAssetGrid" @data-row-select="selectRow" data-action="DisclosureRegularMajorAsset.findDisclosureRegularMajorAssets"
			  :data-autoload="false" data-operate-width="100px">
		<k-grid-column data-header="id" data-name="id" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="产品定期报告数据表id" data-name="t8DisclosureNoticeId" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="资产代码" data-name="assetsCode"></k-grid-column>
		<k-grid-column data-header="资产名称" data-name="assetsName"></k-grid-column>
		<k-grid-column data-header="资产规模" data-name="assetsScale"></k-grid-column>
		<k-grid-column data-header="占产品总资产比例(%)" data-name="assetRatio"></k-grid-column>
		<k-grid-column data-header="创建日期" data-name="crtDate" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="创建时间" data-name="crtTime" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="创建人" data-name="crtUserId" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="创建人名称" data-name="crtUserName" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="更新日期" data-name="updDate" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="更新时间" data-name="updTime" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="更新人" data-name="updUserId" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="更新人名称" data-name="updUserName" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="备注" data-name="remark" :data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope">
<!--          <k-btn class="md-info md-just-icon md-simple" data-descript="修改期末十大资产" data-functype="POPUP" data-size="mini"-->
<!--            data-target="editDisclosureRegularMajorAssetPopup">-->
<!--            <md-icon>edit</md-icon>-->
<!--          </k-btn>-->
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="DisclosureRegularMajorAsset.deleteDisclosureRegularMajorAsset" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" :data-after-success="loadData" :data-disabled="!isShowSDZC" data-descript="删除期末十大资产">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加期末十大资产弹出框   -->
	<k-popup ref="addDisclosureRegularMajorAssetPopup" data-title="新增">
    	<k-form ref="addDisclosureRegularMajorAssetForm" :data-col="2">
			<k-form-item label="id">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="产品定期报告数据表id">
	        	<k-field-text v-model="formData.t8DisclosureProdRegularId"/>
	     	</k-form-item>
			<k-form-item label="资产代码">
	        	<k-field-text v-model="formData.assetsCode"/>
	     	</k-form-item>
			<k-form-item label="资产名称">
	        	<k-field-text v-model="formData.assetsName"/>
	     	</k-form-item>
			<k-form-item label="资产规模">
	        	<k-field-text v-model="formData.assetsScale"/>
	     	</k-form-item>
			<k-form-item label="占产品总资产比例">
	        	<k-field-text v-model="formData.assetRatio"/>
	     	</k-form-item>
			<k-form-item label="创建日期">
	        	<k-field-text v-model="formData.crtDate"/>
	     	</k-form-item>
			<k-form-item label="创建时间">
	        	<k-field-text v-model="formData.crtTime"/>
	     	</k-form-item>
			<k-form-item label="创建人">
	        	<k-field-text v-model="formData.crtUserId"/>
	     	</k-form-item>
			<k-form-item label="创建人名称">
	        	<k-field-text v-model="formData.crtUserName"/>
	     	</k-form-item>
			<k-form-item label="更新日期">
	        	<k-field-text v-model="formData.updDate"/>
	     	</k-form-item>
			<k-form-item label="更新时间">
	        	<k-field-text v-model="formData.updTime"/>
	     	</k-form-item>
			<k-form-item label="更新人">
	        	<k-field-text v-model="formData.updUserId"/>
	     	</k-form-item>
			<k-form-item label="更新人名称">
	        	<k-field-text v-model="formData.updUserName"/>
	     	</k-form-item>
			<k-form-item label="备注">
	        	<k-field-text v-model="formData.remark"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureRegularMajorAsset.addDisclosureRegularMajorAsset" data-from="addDisclosureRegularMajorAssetForm"
		               :data-model="formData" data-target="disclosureRegularMajorAssetGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改期末十大资产弹出框   -->
	<k-popup ref="editDisclosureRegularMajorAssetPopup" data-title="修改">
	  <k-form ref="editDisclosureRegularMajorAssetForm" :data-col="2">
		<k-form-item label="id">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="产品定期报告数据表id">
        	<k-field-text v-model="formData.t8DisclosureProdRegularId"/>
     	</k-form-item>
		<k-form-item label="资产代码">
        	<k-field-text v-model="formData.assetsCode"/>
     	</k-form-item>
		<k-form-item label="资产名称">
        	<k-field-text v-model="formData.assetsName"/>
     	</k-form-item>
		<k-form-item label="资产规模">
        	<k-field-text v-model="formData.assetsScale"/>
     	</k-form-item>
		<k-form-item label="占产品总资产比例">
        	<k-field-text v-model="formData.assetRatio"/>
     	</k-form-item>
		<k-form-item label="创建日期">
        	<k-field-text v-model="formData.crtDate"/>
     	</k-form-item>
		<k-form-item label="创建时间">
        	<k-field-text v-model="formData.crtTime"/>
     	</k-form-item>
		<k-form-item label="创建人">
        	<k-field-text v-model="formData.crtUserId"/>
     	</k-form-item>
		<k-form-item label="创建人名称">
        	<k-field-text v-model="formData.crtUserName"/>
     	</k-form-item>
		<k-form-item label="更新日期">
        	<k-field-text v-model="formData.updDate"/>
     	</k-form-item>
		<k-form-item label="更新时间">
        	<k-field-text v-model="formData.updTime"/>
     	</k-form-item>
		<k-form-item label="更新人">
        	<k-field-text v-model="formData.updUserId"/>
     	</k-form-item>
		<k-form-item label="更新人名称">
        	<k-field-text v-model="formData.updUserName"/>
     	</k-form-item>
		<k-form-item label="备注">
        	<k-field-text v-model="formData.remark"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureRegularMajorAsset.updateDisclosureRegularMajorAsset" data-from="editDisclosureRegularMajorAssetForm"
	        :data-model="formData" data-target="disclosureRegularMajorAssetGrid">
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
    model: {
      prop: 'DisclosureRegularMajorAsset',
      event: 'input'
    },
    props: {
      isShow: {
        type: String,
        default: ''
      },
      t8DisclosureNoticeId: {
        type: String,
        default: ''
      },
      DisclosureRegularMajorAsset: {
      },
		isShowSDZC: {
      		type:Boolean,
			default: false,
		}
    },
    data() {
      return {
        formData: {},
        selectRowData: {},
      };
    },
    created() {
        //console.log("DisclosureRegularMajorAsset=>>>>>",this.DisclosureRegularMajorAsset);
      this.$nextTick(()=>{
        this.$refs.disclosureRegularMajorAssetGrid.load({t8DisclosureNoticeId:this.t8DisclosureNoticeId});
      });
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
		loadData() {
      		this.$refs.disclosureRegularMajorAssetGrid.load({t8DisclosureNoticeId:this.t8DisclosureNoticeId});
		}
    }
  };
</script>
