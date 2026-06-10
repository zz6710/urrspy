<template>
    <el-card class="box-card">
      <div slot="header" class="shadow" >
        <span class="shadow-label"><i class="el-icon-view"></i>{{ methodUServer }}</span>
        <span class="shadow-label">{{ methodName }}</span>
        <span class="shadow-label" style="float: right; padding: 3px 0"><i class="el-icon-paperclip"></i>{{ needAuth }}</span>
      </div>
      <div class="shadow">
        <el-row :gutter="24">
          <el-col :span="18"><span class="shadow-label"><i class="el-icon-box"></i>{{ serverName }}</span></el-col>
          <el-col :span="6"><span class="shadow-label" style="float: right; padding: 6px 0"><i class="el-icon-shopping-cart-1"></i>{{ appName }}</span></el-col>
        </el-row>
        <el-row :gutter="24" style="margin-top: 20px" v-if="serverParams.length > 0">
          <el-col :span="24"> <span class="shadow-label" ><i class="el-icon-info"></i>请求参数:</span></el-col>
        </el-row>
        <el-row :gutter="24" style="margin-top: 20px" v-if="serverParams.length > 0">
          <el-col v-for="(item,index) in serverParams" :key="index" :span="6"> <el-link :underline="false" type="primary">{{ item }}</el-link> </el-col>
        </el-row>
        <el-row :gutter="24" style="display: flex; margin-top: 20px;" v-if="selectModel && selectModel.length > 0">
          <el-col :span="24"> <span class="shadow-label" ><i class="el-icon-info"></i>请求对象:</span></el-col>
        </el-row>
        <el-row class="head-shadow-label" v-for="(field,index) in selectModel" :key="index" :gutter="24" style="display: flex; margin-top: 5px;" >
          <el-col :span="1"> <span class="infinite-list-item"  >{{ index}}</span></el-col>
          <el-col :span="5"> <span class="infinite-list-item"  >{{ field[0]}}</span></el-col>
          <el-col :span="8"> <span class="infinite-list-item"  >{{ field[1]}}</span></el-col>
          <el-col :span="10"> <span class="infinite-list-item" >{{ field[2]}}</span></el-col>
        </el-row>
      </div>
    </el-card>
</template>

<script>
import 'element-ui/lib/theme-chalk/base.css';
export default {
  name: "KApiRpanel",
  componentName: 'KSearchPanel',
  props: {
    methodModel: {
      type: Object,
      default: () => {}
    },
    dataAction: {
      type: String,
      default: undefined
    },
  },
  computed:{
    methodName() {
      return 'name' in this.methodModel && this.methodModel['name'].length > 0 ? this.methodModel['name'] : ''
    },
    methodUServer() {
      return 'upper' in this.methodModel && this.methodModel['upper'].length > 0 ? this.methodModel['upper'] : ''
    },
    needAuth() {
      return 'needAuth' in this.methodModel && this.methodModel['needAuth'].length > 0 ? this.methodModel['needAuth'] === '1'? '需要授权': '不需要授权' : ''
    },
    serverName() {
      console.log('server' in this.methodModel && this.methodModel['server'].length > 0? '服务名称:' + this.methodModel['server'] : '')
      return  'server' in this.methodModel && this.methodModel['server'].length > 0? '服务名称:' + this.methodModel['server'] : ''
    },
    appName(){
      return  'appName' in this.methodModel && this.methodModel['appName'].length > 0 ? '微服务名:' + this.methodModel['appName'] : ''
    },
    serverParams(){
      return 'serverParams' in this.methodModel && this.methodModel['serverParams'].length > 0 ? this.methodModel['serverParams'].split(',') : []
    },
    selectModel(){
      if('modelName' in this.methodModel && this.methodModel['modelName'].length > 0 ){
        let files = 'dict:java.lang.String:字典标识,dictname:java.lang.String:字典名称'
        let file = files.split(',')
        let _files = new Array()
        for(let s in file){
          let text = file[s].split(':')
          _files.push([...text])
        }
        return _files
        //setTimeout(() => {
            //this.getModel(this.methodModel['modelName'])
        //})
      }
    }
  },
  data() {
    return {
      params: {},
      row: {}
    }
  },
  methods: {
    getModel(modelName){
      if(this.dataAction){
        this.loadActionData(modelName)
      }
    },
    loadActionData(modelName) {
      this.params['model_name'] = modelName
      console.log(this.params)
      this.httpUtil.comnQuery({
        action: this.dataAction,
        params: this.params
      }).then(data => {
        this.row = data.rows
      });
    },
  }
}
</script>

<style scoped>
  .shadow .shadow-label {
    font-size: 14px;
    color: #303133;
    font-weight: 500;
    cursor: pointer;
  }
  .shadow .shadow-label span {
    position: absolute;
    right: 15px;
    color: #909399;
    font-size: 12px;
    font-weight: 500
  }
  .shadow .head-shadow-label {
    font-size: 14px;
    color: #303133;
    font-weight: 500
  }
  .shadow .head-shadow-label .infinite-list-item {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    background: #030303;
    margin: 2px;
    color: #f8fcfa;
  }

  .transition-box {
    margin-bottom: 10px;
    width: auto;
    height: 40px;
    border-radius: 4px;
    background-color: #409EFF;
    text-align: center;
    color: #fff;
    padding: 10px 10px;
    box-sizing: border-box;
    margin-right: 20px;
  }
</style>
