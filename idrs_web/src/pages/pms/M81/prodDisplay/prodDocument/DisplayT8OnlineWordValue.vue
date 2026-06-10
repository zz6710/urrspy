<template>
  <div>
    <k-form ref="addForm" data-ui="element"  style="width: 100%">
      <k-form-item label="产品代码">
        <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="文档版本">
        <k-field-text v-model="formData.version"  :data-disabled="true" />
      </k-form-item>
      <k-form-item label="文档类型">
        <k-field-text v-model="formData.documentName"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否模板文件">
        <k-field-text v-model="formData.isTemplateFile"  :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="创建人">
        <k-field-text v-model="formData.createUserName"  :data-disabled="true"/>
      </k-form-item>
    </k-form>
    <div  class="form-item prod-panel" style="height:70px;"  />
    <div class="edit">
<!--      <div class="word">
        <iframe name="onlineEdit" id="onlineEdit" :src="formData.viewUrl"></iframe>
      </div>-->
      <div class="form">
        <k-form style="width:100%" :data-col="3">
          <el-col :span="12" v-if="hasOldData">
            <h4>原数据:</h4>
            <k-form-item
              v-for="(submitData,index) in oldDatas"
              :key="index"
              :label="submitData.label"
              :dataColor = "submitData.color"
            >
              <k-field-text  :value="submitData.value" :data-disabled="true" :data-clearable="false" />
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
              <k-field-text :class="submitData.color=='red'?'color-red':''" :value="submitData.value" :data-disabled="true" :data-clearable="false" />
            </k-form-item>
          </el-col>
        </k-form>
      </div>
    </div>
  </div>

</template>

<script>
    import Tools from "@/utils/tools";

    export default {
        name: "DisplayT8OnlineWordValue",
      props:{
        formData:{
          onlineEditDataParams:[],
          oldOnlineEditDataParams:[],
        },
      },
      data(){
        return{
          list:[],
          oldList:[],
          dataColor:'red',
          hasOldData: false,
          oldDatas: [],
          submitDatas: []
        }
      },
      mounted() {
        window.addEventListener('message', (e)=>{
          if(e.data.key){
            let refName=e.data.key
            this.$refs[refName][0].focus()
          }
        })
      },
      methods:{
        formateData(core,right){
          let newArray = new Array();
          let rdatas = new Map();
          right.forEach((data,i=0)=>{
            rdatas.set(data.wordKey,data);
          });
          for(let i = 0,j = 0 ; i < core.length && j <right.length;i++,j++){
            if(core[i].wordKey === right[j].wordKey){
              if(core[i].wordValue === right[j].wordValue){
                newArray.push({label:core[i].wordComment+"：",value:core[i].wordValue,color:'#606266'});
              }else{
                newArray.push({label:core[i].wordComment+"：",value:core[i].wordValue,color:'red'});
              }
            }else{
              if(rdatas.get(core[i].wordKey)){
                newArray.push({label:"",value:"",color:'#606266'});
                i--;
              }else{
                newArray.push({label:core[i].wordComment+"：",value:core[i].wordValue,color:'#606266'});
                j--;
              }
            }
            if(j===right.length-1){
              for(i++;i<core.length;i++){
                newArray.push({label:core[i].wordComment+":",value:core[i].wordValue,color:'#606266'});
              }
            }
          }
          if(right.length === 0){
            core.forEach(data=>{
              newArray.push({label:data.wordComment+":",value:data.wordValue,color:'#606266'});
            })
          }
          return newArray;
        },
      },
      created() {
        let array = JSON.parse(this.formData.onlineEditDataParams);
        this.list = array;
        this.hasOldData = false;
        let submitdata = Tools.str2Json(this.formData.onlineEditDataParams);
        if (this.formData.oldOnlineEditDataParams) {
          let olddata = Tools.str2Json(Tools.array2str(this.formData.oldOnlineEditDataParams));
          if(!olddata && olddata.length===0){
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
    }
</script>

<style lang="scss" scoped>
::v-deep .color-red{
  input{
    color:red !important;
  }
}
.edit{
  display: inline-block !important;
  flex-direction: row;
  width: 100%;
  height: 600px;
  .word{
    width: 70%;
    iframe{
      width: 100%;
      height: 100%;
    }
  }
  .form{
    padding-left: 20px;
    overflow-y:auto;
    .form-item{
      display: flex;
      align-items: center;
      margin-bottom: 10px;
      .form-item-span{
        margin-right: 5px;
        width: 100px;
        text-align: left;
      }
      .k-field-text{
        margin-left: 5px;
        width: 300px;
        height: 30px;
      }
    }
  }
}
</style>
