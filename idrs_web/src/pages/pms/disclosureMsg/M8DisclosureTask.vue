<template>
  <div>
    <div>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="报告检查" name="tab1">
          <k-grid ref="t8ObjectGrid1" :dataData="dataList1" :data-operate-column="false">
            <k-grid-column data-header="公司名称" data-name="name"/>
            <k-grid-column data-header="信披类型" data-name="type"/>
            <k-grid-column data-header="所处阶段" data-name="dbtype"/>
            <k-grid-column data-header="状态" data-name="taskDate"/>
          </k-grid>
        </el-tab-pane>
        <el-tab-pane label="数据补录" name="tab2">
          <k-grid ref="t8ObjectGrid2" :dataData="dataList2">
            <k-grid-column data-header="公司名称" data-name="name"/>
            <k-grid-column data-header="计划补录完成日期" data-name="jhDate"/>
            <k-grid-column data-header="待办类型" data-name="dbtype"/>
            <template slot="operate" slot-scope="scope">
              <k-btn data-size="mini" :data-handler="openPage"
                     class="btn-custom-plain" data-descript="查看">
                查看
              </k-btn>
            </template>
          </k-grid>
        </el-tab-pane>
        <el-tab-pane label="托管行复核" name="tab3">
          <k-grid ref="t8ObjectGrid3" :dataData="dataList3" :data-operate-column="false">
            <k-grid-column data-header="公司名称" data-name="name"/>
            <k-grid-column data-header="信披类型" data-name="type"/>
            <k-grid-column data-header="待办类型" data-name="dbtype"/>
            <k-grid-column data-header="任务日期" data-name="taskDate"/>
          </k-grid>
        </el-tab-pane>
      </el-tabs>

    </div>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "M8DisclosureTask",
  data() {
    return {
      activeName: 'tab1',
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        prodCode: '',
        prodName: '',
        feeJson:'',
      },
      dataList1:{
        rows:[
          {name:'天利01发行公告',type:'发行公告',dbtype:'数据补录',taskDate:'未补录'},
        ]
      },
      dataList2:{
        rows:[
          {name:'天利01发行公告',jhDate:'2021-01-27',dbtype:'未补录'},
        ]
      },
      dataList3:{
        rows:[
        ]
      },
    }
  },
  watch:{
  },
  created() {
  },
  methods: {
    openPage(){
      this.$router.push({
        path: "/main/pms/disclosureMsg/M8DisclosureData",
        query: {},
      });
    },
    saveRule(params){
      Tools.alert("保存成功","success");
      this.$refs.addPopup.close();
      return false;
    },
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
}
</script>

<style scoped>

</style>
