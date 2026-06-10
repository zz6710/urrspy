<template>
  <div class="app-container" style="text-align: -webkit-center;">
    <el-card style="width: 98%; ">
      <el-row :gutter="20">
        <el-form :inline="false" style="text-align:-webkit-left;">
         <el-form-item label="文件路径:  ">
            <el-input type="text" :rows="2" style="width: 100%" v-model="formData.filePath" data-type="text" placeholder="请输入linux文件路径" @change="getFileName(formData.filePath)" clearable size="small"></el-input>
          </el-form-item>
        </el-form>
      </el-row>
    </el-card>
    <k-btn slot="button" style="width: 120px" class="btn-custom-primary" :data-download-name="formData.fileNames"
           data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"
           data-url="/download/server/PmsApp/privilege/downFile.json" :data-model="formData">
      <md-icon>cloud_download</md-icon>下载文件
    </k-btn>

  </div>
</template>

<script>

  export default {
    name: "privilegeLog",
    data(){
      return{
        formData: {},
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        filePath:undefined,
        fileName:"pms.log",
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
      getFileName(filePath){
        let winIndex = filePath.lastIndexOf("\\");
        let linuxIndex = filePath.lastIndexOf("/");
        console.log("winIndex=:>",winIndex);
        console.log("linuxIndex=:>",linuxIndex);
        let tempPath = "";
        if(winIndex>0){
          this.$set(this.formData,'fileNames',filePath.substring(winIndex+1,filePath.length))
        }else{
          this.$set(this.formData,'fileNames',filePath.substring(linuxIndex+1,filePath.length))
        }
        console.log("formData.fileNames",this.formData.fileNames);
      },
      getSql() {
          this.httpUtil.query({
            url: "privilege/downFile.json",
            params: {"filePath": this.filePath}
          })
          .then(data => {
            this.data = data;
            this.tableHeadData = data.columns;
            this.tableData = data.rows;
            this.msg = data.msg;
          });
      },
    }
  }
</script>
