<template>
  <div class="py-page">
    <k-form-search data-target="systemOperationLogGrid" data-model-name="SystemOperationLog" data-label-width="100px" />
    <div class="py-page-container">
      <k-grid
        ref="systemOperationLogGrid"
        data-action="SystemOperationLog.find1"
        @data-row-select="selectRow"
      >
        <k-grid-column data-header="操作人姓名" data-name="username" />
        <k-grid-column data-header="服务" 		data-name="server"  data-hidden="true"/>
        <k-grid-column data-header="操作服务" 	data-name="serverDesc" data-hidden="true"/>
        <k-grid-column data-header="方法" 		data-name="method"  data-hidden="true"/>
        <k-grid-column data-header="操作方法" 	data-name="methodDesc" />
        <k-grid-column data-header="操作时间" data-name="operationDate" data-render="renderDateTimeCreate"/>
        <k-grid-column data-header="操作结果" 	data-name="result" />
        <k-grid-column data-header="错误信息" 	data-name="errorMsg" />
        <template slot="operate">
          <k-btn
            data-functype="POPUP"
            data-target="log"
            class="md-warning md-just-icon md-simple"
            data-descript="查看"
          >
            <md-icon md-src="/static/svg/log.svg" />
          </k-btn>
          <!-- <span>查看</span> -->
        </template>
      </k-grid>
    </div>
    <div id="view"></div>

    <k-popup ref="log" data-title="操作日志" dataWidthPercent="60%" :dataDialogDrag="true">
      <el-row :gutter="15">
        <k-form style="width:100%" :data-col="3">
          <el-col :span="12" v-if="hasOldData">
            <h4>原数据:</h4>
            <k-form-item
              v-for="(submitData,index) in oldDatas"
              :key="index"
              :label="submitData.label"
              :dataColor="submitData.color"
            >
              <k-field-display :value="submitData.value" :data-clearable="false" />
            </k-form-item>
          </el-col>
          <el-col :span="hasOldData ? 12 : 24">
            <h4>提交数据:</h4>
            <k-form-item
              v-for="(submitData,index) in submitDatas"
              :key="index"
              :label="submitData.label "
              :dataColor = "submitData.color"
            >
              <k-field-display :value="submitData.value" :data-clearable="false" />
            </k-form-item>
          </el-col>
        </k-form>
      </el-row>
    </k-popup>
  </div>
</template>

<script>
  import Tools from "../../utils/tools.js";

  import { assign } from "lodash";
  export default {
    name:"op-log",
    data() {
      return {
        selectRowData: {},
        hasOldData: false,
        oldDatas: [],
        submitDatas: []
      };
    },
    methods: {
      renderDateTimeCreate(row) {
        return Tools.formatDateTime(row.operationDate, row.operationTime);
      },
      selectRow(row, column, event) {
        this.hasOldData = false;
        let submitdata = Tools.str2Json(row.submitData);
        if (row.submitOldData) {
          let olddata = Tools.str2Json(row.submitOldData);
          if (!olddata && olddata.length === 0) {
            this.submitDatas = submitdata;
            return;
          }
          this.hasOldData = true;
          this.oldDatas = this.formateData(olddata,submitdata);
          this.submitDatas = this.formateData(submitdata,olddata);
          return;
        }
        this.submitDatas = submitdata;
      },
      formateData(core,right){
        let newArray = new Array();
        let rdatas = new Map();
        right.forEach((data,i=0)=>{
          rdatas.set(data.label,data);
        });
        for(let i = 0,j = 0 ; i < core.length && j <right.length;i++,j++){
          if(core[i].label === right[j].label){
            if(core[i].value === right[j].value){
              newArray.push({label:core[i].label+"：",value:core[i].value,color:'#606266'});
            }else{
              newArray.push({label:core[i].label+"：",value:core[i].value,color:'red'});
            }
          }else{
            if(rdatas.get(core[i].label)){
              newArray.push({label:"",value:"",color:'#606266'});
              i--;
            }else{
              newArray.push({label:core[i].label+"：",value:core[i].value,color:'#606266'});
              j--;
            }
          }
          if(j===right.length-1){
            for(i++;i<core.length;i++){
              newArray.push({label:core[i].label+":",value:core[i].value,color:'#606266'});
            }
          }
        }
        if(right.length === 0){
          core.forEach(data=>{
            newArray.push({label:data.label+":",value:data.value,color:'#606266'});
          })
        }
        return newArray;
      }
    }
  };
</script>

<style scoped>
  .c-search-table beauty-Scroll {
    height: 200px;
  }
</style>
