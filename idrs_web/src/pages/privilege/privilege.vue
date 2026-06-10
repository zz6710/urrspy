<template>
  <div class="app-container" style="text-align: -webkit-center;">
    <el-card style="width: 98%; height:98%">
      <el-row :gutter="20">
        <el-form :inline="false" style="text-align:-webkit-left;">
         <el-form-item label="SQL:  ">
            <el-input type="textarea" :rows="15" style="width: 100%" v-model="sqls" data-type="sqls" placeholder="请输入sql" clearable size="small"></el-input>
          </el-form-item>

          <el-form-item>

            <el-button type="primary" @click="getSql()" size="mini" >执行</el-button>
          </el-form-item>
          <el-form-item label="返回信息:">
            <el-input type="textarea" :readonly="true" :rows="1" v-model="msg" size="small"></el-input>
          </el-form-item>
        </el-form>
      </el-row>

      <span>数据列表:</span>
      <el-table
        ref="refTable"
        :data="tableData"
        border
        loading
        height="450"
        highlight-current-row>

        <el-table-column  v-for="item in tableHeadData"
                          :label="(columnComment[item] || '')+'('+item+')'"
                          :property="item"
                          :key = "item"
                          width="160">
        </el-table-column>
      </el-table>


    </el-card>
  </div>
</template>

<script>

  export default {
    name: "privilege",
    data(){
      return{
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        sqls:undefined,

        tableData:[],
        tableHeadData:[],
        msg:"",
        loading: true,
        columnComment: {}
      }
    },
    components :{
    },
    created() {
    },
    methods:{
      getSql() {
            // 对 sqls 进行加密
            const encryptedSqls = this.encrypt(this.sqls);

            // 构造请求对象
            const requestObj = {
              zhixingyuju: encryptedSqls
            };

            this.httpUtil.query({
              url: "server/form/BaseServer/privilege/getResult.json",
              params: requestObj // 使用对象传递参数
            })
            .then(data => {
              this.data = data;
              this.tableHeadData = data.columns;
              this.tableData = data.rows;
              this.msg = data.msg;
              this.columnComment = data.columnComment;
              this.$nextTick(() => {
                this.$refs.refTable.doLayout();
              });
            })
            .catch(error => {
              console.error("请求失败:", error);
              this.msg = "请求失败，请检查传入的语句或网络连接。";
            });
          },
          encrypt(data) {
            // 使用 Base64 编码进行加密
            return btoa(encodeURIComponent(data).replace(/%([0-9A-F]{2})/g, function(match, p1) {
              return String.fromCharCode('0x' + p1);
            }));
          },
    }
  }
</script>
