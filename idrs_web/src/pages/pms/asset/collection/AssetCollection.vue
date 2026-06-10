<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AssetCollection" data-target="assetCollectionGrid"  v-model = "searchParam">
        <k-form-item label="补录页面">
          <k-field-select v-model="searchParam.page"  data-dict = "page"/>
        </k-form-item>
        <k-form-item label=角色>
          <k-field-select v-model="searchParam.roles"  :data-data = "addRolesDict" data-value-field="roleid" data-display-field="rolename"/>
        </k-form-item>
        <k-form-item label=补录字段类型>
          <k-field-select v-model="searchParam.fieldType"  data-dict = "sysSupplyFieldType"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addAssetCollectionPopup" slot="button"
               v-if="global.isShowAuthorityButton('AssetCollection.addAssetCollection')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="assetCollectionGrid" @data-row-select="selectRow" data-operate-width="120px" data-action="AssetCollection.findAssetCollections"
              v-if="global.isShowAuthorityButton('AssetCollection.findAssetCollections')">
		<k-grid-column data-header="补录页面" data-name="page" data-dict = "page"></k-grid-column>
		<k-grid-column data-header="角色" data-name="roles" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="角色" data-name="rolename"></k-grid-column>
		<k-grid-column data-header="补录字段类型" data-name="fieldType" data-dict="sysSupplyFieldType"></k-grid-column>
		<k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="修改资产补录配置" data-functype="POPUP" data-size="mini" @click="initDialogData(scope.row.row)"
                 v-if="global.isShowAuthorityButton('AssetCollection.updateAssetCollection')">
            修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="AssetCollection.deleteAssetCollection" data-size="mini"
               data-type="danger" data-target="assetCollectionGrid" :data-confirm="true" data-descript="删除资产补录配置"
                 v-if="global.isShowAuthorityButton('AssetCollection.deleteAssetCollection')">
          	删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加资产补录配置弹出框   -->
	<k-popup ref="addAssetCollectionPopup" data-title="新增" @data-opened="addAssetColl">
    	<k-form ref="addAssetCollectionForm" :data-col="2" isFormBodyScreen>
			<k-form-item label="补录页面">
	        	<k-field-select v-model="formData.page" :data-allowblank="false" data-dict = "page" @data-on-change="findColunmInfo"/>
	     	</k-form-item>
			<k-form-item label="角色">
	        	<k-field-select v-model="formData.roles" :data-allowblank="false" :data-data = "addRolesDict" data-value-field="roleid" data-display-field="rolename" @data-on-change="findColunmInfo"/>
	     	</k-form-item>
			<k-form-item label="补录字段类型">
	        	<k-field-select v-model="formData.fieldType" :data-allowblank="false" data-dict="sysSupplyFieldType" @data-on-change="findColunmInfo"/>
	     	</k-form-item>
			<k-form-item label="备注" :data-col="2" >
	        	<k-field-text v-model="formData.remark" :data-max-length="256"/>
	     	</k-form-item>
			<k-form-item label="补录页面字段"  :data-col="2">
			      <el-button @click="handleSelectAll" style="margin-right:10px" type="warning" icon="el-icon-date" size="mini">全选</el-button>
	        	<k-field-checkbox ref="addBox" v-model="formData.pageField" :data-data="columnName" :data-allowblank="false" data-display-field="label" data-value-field="value"/>
	     	</k-form-item>

      <k-form-footer slot="footer" data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetCollection.addAssetCollection" data-from="addAssetCollectionForm"
                :data-model="formData" data-target="assetCollectionGrid">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </k-form>
	</k-popup>

	<!--    修改资产补录配置弹出框   -->
	<k-popup ref="editAssetCollectionPopup" data-title="修改">
	  <k-form ref="editAssetCollectionForm" :data-col="2" isFormBodyScreen>
		<k-form-item label="补录页面" >
        	<k-field-select v-model="formData.page" data-dict="page" :data-disabled ="true"/>
     	</k-form-item>
		<k-form-item label="角色">
        	<k-field-select v-model="formData.roles" :data-allowblank="false" :data-disabled ="true" :data-data="addRolesDict" data-value-field="roleid" data-display-field="rolename"/>
     	</k-form-item>
      <k-form-item label="补录字段类型">
        <k-field-select v-model="formData.fieldType" :data-allowblank="false" :data-disabled ="true" data-dict="sysSupplyFieldType"/>
      </k-form-item>
		<k-form-item label="备注" :data-col="2" >
        	<k-field-text v-model="formData.remark" :data-max-length="256"/>
     	</k-form-item>
		<k-form-item label="补录页面字段" :data-col="2">
      <el-button @click="handleSelectAll" style="margin-right:10px" type="warning" icon="el-icon-date" size="mini">全选</el-button>
        	<k-field-checkbox ref="editBox" v-model="formData.pageField" :data-data="columnName" :data-allowblank="false" data-display-field="label" data-value-field="value"/>
    </k-form-item>
	    <k-form-footer slot="footer" data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetCollection.updateAssetCollection" data-from="editAssetCollectionForm"
	        :data-model="formData" data-target="assetCollectionGrid">
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
import KFieldCheckbox from "../../../../components/k-element/k-field-checkbox/k-field-checkbox";
import KFieldRadio from "@/components/k-element/k-field-radio/k-field-radio";
  export default {
    name: "AssetCollection",
    data() {
      return {
        formData: {
          pageField: [],
          page: '',
          roles: '',
          fieldType: ''
        },
        selectRowData: {},
        addRolesDict: {},
        columnName:[],
        pageFieldRef:'',
        searchParam:{}, //查询条件
        options:[
          {
            value: '1',
            label: '全选/反选',
          }
        ],
        add:true
      };
    },
    created() {
      this.findRoles();
    },
    methods: {
      handleSelectAll() {
        if (this.formData.pageField == '') {
          this.$set(this.formData, 'pageField', this.pageFieldRef);
        } else {
          this.$set(this.formData, 'pageField', '');
        }
      },
      showColunms(){

      },
      initDialogData(row){
        this.add = false
        this.$refs['editAssetCollectionPopup'].popup()
        setTimeout(() =>{
          this.findRoles()
          this.findColunmInfo(row.page);
        },200)
      },
      addAssetColl(){
        this.add = true
        this.columnName = []
        this.findRoles()
      },
      findColunmInfo(value){
        console.log("value=:>>>",value);
        if(value=='' || value==null || value==undefined){
          this.columnName = [];
          //this.$set(this,'columnName',[])
          this.$refs.addBox.loadData();
          return ;
        }
        // 查询字段信息
        let $this = this
        let time
        this.httpUtil.comnQuery({
          action: "AssetCollection.findTableColumns",
          params: {page: this.formData.page, fieldType: this.formData.fieldType, roles: this.formData.roles},
        }).then(data => {
          $this.pageFieldRef = ''
          $this.columnName = JSON.parse(JSON.stringify(data.rows || []));
          for(var i=0;i< $this.columnName.length;i++){
            $this.pageFieldRef +=  $this.columnName[i].value;
            if( $this.columnName.length - 1 != i){
              $this.pageFieldRef += ","
            }
          }
          console.log("---------------",value)
          $this.$refs[$this.add ? 'addAssetCollectionPopup' : 'editAssetCollectionPopup'].close()
          clearTimeout(time)
          time = window.setTimeout(() => {
            if ($this.add && $this.formData.pageField) {
              $this.formData.pageField = []
            }
            $this.$refs[$this.add ? 'addAssetCollectionPopup' : 'editAssetCollectionPopup'].popup()
            if (!$this.add) {
              this.findRoles()
            }
          }, 50);
          console.log("-------columnName--------",$this.columnName);
        }).catch({})
      },
      findRoles(){
        // 查询角色信息
        this.httpUtil.comnQuery({
          action: "AssetCollection.findSysRoles",
          params: null
        }).then(data => {
          this.addRolesDict = data.rows || [];
          console.log("-------addRolesDict--------",data.rows)
        }).catch({})
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    }
  };
</script>
<style scoped>
>>> .el-table__cell {
  padding: 0px 0 !important;
}
.specialClass {
  min-width: 40px !important;
}
>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
