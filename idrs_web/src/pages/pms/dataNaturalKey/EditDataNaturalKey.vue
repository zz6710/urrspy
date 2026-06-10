<template>
  <div>
    <k-form ref="addDataNaturalKeyForm" :data-col="2" isFormBodyScreen>
      <div class ="tableLine2" ><span class="leftText2">基础信息</span></div>
      <k-form-item label="所属层级">
        <k-field-select v-model="formData.hierarchy" data-dict="dataLayer" :data-allowblank="false" @input="changeHierarchy" :data-disabled="isEdit"/>
      </k-form-item>
      <k-form-item label="数据库表">
        <k-field-select v-model="formData.tableName" :data-allowblank="false" :data-data="tableNames" data-value-field="tables"
                        data-display-field="tablesName" @data-on-change="changeTable" :data-disabled="isEdit"/>
      </k-form-item>
      <template>
        <div class ="tableLine" ><span class="leftText">主键配置</span><div class="itemsCorn"></div></div>
        <div class="continue-select" @click="pushnew" v-if="disabledVal?false:true">
          <svg-icon icon-class="add"></svg-icon>添加字段信息
        </div>
        <k-grid data-fixed="right" ref="naturalKeyFGrid" :data-data="naturalKeyGridData" id="naturalKeyGridData" :dataPageSize="0" :data-display="false"
                data-operate-width="50px" class="continue-ele" max-height="2000px">
          <k-grid-column data-header="业务主键" data-name="naturalKey" data-width="240px" >
            <template slot-scope="scope">
              <k-field-select v-model="scope.row.row.naturalKey" :data-data="naturalKeys" data-value-field="naturalKey" :data-disabled="disabledVal"
                              data-display-field="naturalKey,naturalKeyName" :data-allowblank="false"></k-field-select>
            </template>
          </k-grid-column>
          <k-grid-column data-header="标准主键" data-name="standardKey"  data-width="150px">
            <template slot-scope="scope">
              <k-field-text v-model="scope.row.row.standardKey" data-allowblank="false" :data-disabled="disabledVal"></k-field-text>
            </template>
          </k-grid-column>
          <k-grid-column data-header="数据字典" data-name="outDict"  data-width="190px">
            <template slot-scope="scope">
              <k-field-select v-model="scope.row.row.outDict" data-allowblank="false" data-dict="out_dicts" :data-disabled="disabledVal"></k-field-select>
            </template>
          </k-grid-column>
          <template slot="operate" slot-scope="scope" v-if="disabledVal?false:true">
            <k-btn class="md-danger md-just-icon md-simple"
                   :data-handler="()=>naturalKeyGridData.rows.splice(scope.row.row.row_index-1,1)"
                   data-size="mini" data-type="danger"
                   data-descript="删除">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
      </template>

      <k-form-footer slot="footer" data-align="center" v-if="disabledVal?false:true">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="magSubmit" data-from="addDataNaturalKeyForm"
               :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  export default {
    name: "EditDataNaturalKey",
    props: {
      naturalKeyFGrid: {
        type:Object,
      },
      info : {
        type:Object,
      },
      //是否是修改页面
      isEdit: {
        type:Boolean,
        default:false
      },
      //是否是详情页面
      disabledVal: {
        type:Boolean,
        default:false
      },
      naturalKeys2 : {
        type:Object,
      },
    },
    data() {
      return {
        naturalKeyGridData: {},
        formData: {},
        selectRowData: {},
        naturalKeys: [],
        tableNames: []
      };
    },
    watch:{
      naturalKeys2: {
        handler(){
          this.naturalKeys = this.naturalKeys2;
          this.httpUtil.comnQuery({
            action: "DataNaturalKeyModel.findTables",
            params: {hierarchy: this.formData.hierarchy},
            successAlert: false
          }).then(data => {
            this.tableNames = data.rows;
          });
        },
      },
    },
    created() {
      this.formData = this.info;
      this.naturalKeyGridData = this.naturalKeyFGrid;
    },
    methods: {
      magSubmit(param){
        let value = {};
        if (this.naturalKeyGridData.rows && this.naturalKeyGridData.rows.length > 0) {
          this.naturalKeyGridData.rows.forEach((t) => {
            if(!t.naturalKey){
              Tools.alert("请输入主键字段！", "danger");
              return;
            }
          });
        }
        value = JSON.stringify(this.naturalKeyGridData.rows);
        param["naturalKeyGridData"] = value;
        param.isEdit = this.isEdit;
        param.tableName = this.formData.tableName;
        param.hierarchy = this.formData.hierarchy;
        this.httpUtil.comnQuery({
          action: "DataNaturalKeyModel.findDataNarutalKeyModel",
          params: {tableName: this.formData.tableName},
          successAlert: false
        }).then(data => {
          if(data.rows.length>0&&!this.isEdit){
            Tools.alert("该数据库表已存在业务主键！","danger");
            this.$refs.addSubmitBtn.loading = false;
          } else {
            this.httpUtil.comnUpdate({
              action: "DataNaturalKeyModel.addDataNarutalKeyModel",
              params: param,
            }).then(data => {
              this.$emit('loadGriding',this.formData);
            });
          }
        });
      },
      changeHierarchy(param){
        this.naturalKeyGridData.rows.splice(0,this.naturalKeyGridData.rows.length);
        this.$set(this.formData, 'tableName', '');
        this.httpUtil.comnQuery({
          action: "DataNaturalKeyModel.findTables",
          params: {hierarchy: param},
          successAlert: false
        }).then(data => {
          this.tableNames = data.rows;
        });
      },
      changeTable(value){
        this.naturalKeyGridData.rows.splice(0,this.naturalKeyGridData.rows.length);
        this.httpUtil.comnQuery({
          action: "DataNaturalKeyModel.findNaturalKey",
          params: {tableName: value},
          successAlert: false
        }).then(data => {
          this.naturalKeys = data.rows;
        });
      },
      pushnew(){
        this.naturalKeyGridData.rows.push({"naturalKey":"","standardKey":"","outDict":""});
      },
    }
  };
</script>

<style scoped>
@import "../asset/AssetComFunction.css";
</style>
