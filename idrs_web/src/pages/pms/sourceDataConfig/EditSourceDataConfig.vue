<template>
  <div>
    <k-form ref="addSourceDataConfigForm" :data-col="2" isFormBodyScreen>
      <div class ="tableLine2" ><span class="leftText2">基础信息</span></div>
      <k-form-item label="数据库表">
        <k-field-select v-model="formData.tableName" :data-allowblank="false" data-action="SourceDataConfigModel.findTables"
                        data-display-field="tablesName" data-value-field="tables" @input="changeField" :data-disabled="isEdit"/>
      </k-form-item>
      <k-form-item label="数据确认规则">
        <k-field-select v-model="formData.remindType" data-dict="remindType" :data-allowblank="false" @data-on-change="changeRemindType" :data-disabled="disabledVal"/>
      </k-form-item>
      <k-form-item label="涉及报表">
        <k-field-select v-model="formData.relatedReport" data-multiple="true" data-action="SourceDataConfigModel.findTablesForApp" :data-params="{'tableName':'app'}"
                        data-value-field="tables" data-display-field="tables,tablesName" :data-allowblank="formData.remindType!=='01'" :data-disabled="disabledVal?disabledVal:false===false?formData.remindType!=='01':true"/>
      </k-form-item>
      <k-form-item label="对比规则">
        <k-field-select v-model="formData.comparisonRules" data-dict="ComparisonRules" :data-allowblank="false"  :data-disabled="disabledVal"/>
      </k-form-item>
<!--      <k-form-item label="关联任务组">-->
<!--        <k-field-select v-model="formData.taskGroup" data-action="SourceDataConfigModel.findTaskGroup" data-value-field="taskGroup" data-display-field="taskGroupName"-->
<!--                        :data-allowblank="true"/>-->
<!--      </k-form-item>-->
      <k-form-item label=" " :data-col="2" v-if="disabledVal?false:true">
        <el-button @click="addAll" style="margin-right:10px" type="warning" icon="el-icon-date" size="mini" >添加所有字段</el-button>
      </k-form-item>
      <template>
        <div class ="tableLine" ><span class="leftText">字段配置</span><div class="itemsCorn"></div></div>
        <div class="continue-select" @click="pushnew" v-if="disabledVal?false:true">
          <svg-icon icon-class="add"></svg-icon>添加字段信息
        </div>
        <k-grid data-fixed="right" ref="fieldCodeFGrid" :data-data="fieldCodeGridData" id="fieldCodeGridData" :dataPageSize="0" :data-display="false"
                data-operate-width="50px" class="continue-ele" max-height="2000px">
          <k-grid-column data-header="提醒字段" data-name="remindField" data-width="240px" >
            <template slot-scope="scope">
              <k-field-select v-model="scope.row.row.remindField" :data-data="remindFields" data-value-field="remindfields" :data-disabled="disabledVal"
                              data-display-field="remindfields,remindfieldsName" :data-allowblank="false" @input="changeRemindField(scope.row.row)"></k-field-select>
            </template>
          </k-grid-column>
          <k-grid-column data-header="数据类型" data-name="fieldType"  data-width="150px">
            <template slot-scope="scope">
              <k-field-text v-model="scope.row.row.fieldType" data-allowblank="false" :data-disabled="true"></k-field-text>
            </template>
          </k-grid-column>
          <k-grid-column data-header="数据字典" data-name="outDict"  data-width="190px">
            <template slot-scope="scope">
              <k-field-select v-model="scope.row.row.outDict" data-allowblank="false" data-dict="out_dicts" :data-disabled="disabledVal"></k-field-select>
            </template>
          </k-grid-column>
          <template slot="operate" slot-scope="scope" v-if="disabledVal?false:true">
            <k-btn class="md-danger md-just-icon md-simple"
                   :data-handler="()=>fieldCodeGridData.rows.splice(scope.row.row.row_index-1,1)"
                   data-size="mini" data-type="danger"
                   data-descript="删除">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
      </template>

      <k-form-footer slot="footer" data-align="center" v-if="disabledVal?false:true">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="magSubmit" data-from="addSourceDataConfigForm"
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
    name: "EditSourceDataConfig",
    props: {
      fieldCodeFGrid: {
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
      remindFields2 : {
        type:Object,
      },
    },
    data() {
      return {
        fieldCodeGridData: {},
        formData: {},
        selectRowData: {},
        remindFields: [],
        reports: []
      };
    },
    watch:{
      remindFields2: {
        handler(){
          this.remindFields = this.remindFields2;
        },
      }
    },
    created() {
      this.formData = this.info;
      this.fieldCodeGridData = this.fieldCodeFGrid;
      this.remindFields = this.remindFields2;
    },
    methods: {
      magSubmit(param){
        let value = {};
        if (this.fieldCodeGridData.rows && this.fieldCodeGridData.rows.length > 0) {
          this.fieldCodeGridData.rows.forEach((t) => {
            if(!t.remindField){
              Tools.alert("请输入提醒字段！", "danger");
              return;
            }
          });
        }
        value = JSON.stringify(this.fieldCodeGridData.rows);
        param["fieldCodeGridData"] = value;
        param.isEdit = this.isEdit;
        this.httpUtil.comnUpdate({
          action: "SourceDataConfigModel.addSourceDataConfigModel",
          params: param,
        }).then(data => {
          if(data.success){
            this.$emit('loadGriding',this.formData);
          }
        });
      },
      changeField(tables){
        this.fieldCodeGridData.rows.splice(0,this.fieldCodeGridData.rows.length);
        this.$set(this.formData, 'remindFields', '');
        this.$set(this.formData, 'taskGroup', '');
        this.$set(this.formData, 'remindType', '');
        this.$set(this.formData, 'relatedReport', '');
        this.httpUtil.comnQuery({
          action: "SourceDataConfigModel.findRemindField",
          params: {tableName: tables},
          successAlert: false
        }).then(data => {
          if(data.rows.length>0){
            this.remindFields=data.rows;
          }
        });
      },
      changeRemindField(value){
        let params = {};
        params.tableName = this.formData.tableName;
        params.remindField = value.remindField;
        this.httpUtil.comnQuery({
          action: "SourceDataConfigModel.findRemindFieldMsg",
          params: params,
          successAlert: false
        }).then(data => {
          if(data.rows.length>0) {
            this.fieldCodeGridData.rows.forEach((t) => {
              if (t.row_index === value.row_index) {
                t.fieldType = data.rows[0].fieldType;
              }
            });
          }
        });
      },
      changeRemindType(value){
        this.$set(this.formData, 'relatedReport', '');
      },
      pushnew(){
        this.fieldCodeGridData.rows.push({"remindField":"","fieldType":"","outDict":""});
      },
      addAll(){
        this.fieldCodeGridData.rows.splice(0,this.fieldCodeGridData.rows.length);
        this.httpUtil.comnQuery({
          action: "SourceDataConfigModel.findRemindFieldAll",
          params: {tableName: this.formData.tableName},
          successAlert: false
        }).then(data => {
          if(data.rows.length>0){
            for (let i = 0; i < data.rows.length; i++) {
              this.fieldCodeGridData.rows.push({"remindField":data.rows[i].remindfields,"fieldType":data.rows[i].fieldType,"outDict":""});
            }
          }
        });
      }
    }
  };
</script>

<style scoped>
@import "../asset/AssetComFunction.css";
</style>
