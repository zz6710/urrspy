<template>
    <div>
      <el-table
        fit
        :data="tableData"
        empty-text="数据库异常，服务不显示"
        style="width: 100%"
        border
        >
        <el-table-column
          prop="app_name"
          label="服务"
          style="width: 50%;"
          >
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          style="width: 50%;"
          >
          <template slot-scope="scope">
            <span v-text="getStatus(scope)"></span>
          </template>

        </el-table-column>
      </el-table>
    </div>
</template>

<script>
    export default {
        name: "healthy",
        data(){
          return{
            tableData:[]
          }
        },
        methods:{
          getStatus(row){
              if(row.row.status=="1"){
                return "服务异常"
              }else{
                return "服务正常"
              }
          }
        },
        created() {
          setInterval(()=>{
            this.httpUtil.getHealthyApp({url:"getAppHealthy.json"}).then(res=>{
              this.tableData=res.data.rows
            })
          },1000)
        }
    }
</script>

<style scoped>

</style>
