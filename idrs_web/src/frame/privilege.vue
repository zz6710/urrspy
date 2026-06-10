<template>
  <div class="app-container" style="text-align: -webkit-center;">
    <el-card style="width: 98%; height: 593px">
      <el-row :gutter="20">
        <el-form :inline="false" style="text-align:-webkit-left;">
         <el-form-item label="filepath:  ">
            <el-input type="textarea" :rows="2" style="width: 100%" v-model="filepath"  placeholder="请输入路径" key="filePath" ></el-input>
          </el-form-item>
          <el-form-item label="filename:  ">
            <el-input type="textarea" :rows="2" style="width: 100%" v-model="filename"  placeholder="请输入文件名" key="filename" ></el-input>
          </el-form-item>

          <el-form-item>

            <el-button type="primary" @click="getSql()" size="mini" >执行</el-button>
          </el-form-item>
         
        </el-form>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import Tools from '@/utils/tools.js'
  export default {
    name: "privilege",
    data(){
      return{
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        filename:'',
        filepath:'',
        tableData:[],
        tableHeadData:[],
        msg:"",
        loading: true,
      }
    },
    components :{
    },
    created() {
    },
    methods:{
      getSql() {
          

          this.httpUtil.download({
              url: "privilege/download.json",
              params: {filename:this.filename,filepath:this.filepath},
              callback: response => {
                Tools.alert("下载完成");
                //this.$refs.trusteeBtn.setIconStyle(1, []);
              }
            }, this.filename);
      },
    }
  }
</script>
