<template>
  <el-container style="padding: 0;margin: 0;width: 98%;height: 98%;position: absolute">
    <el-aside style="border: 1px solid #EBEEF5;background-color: #FFFFFF">
      <k-search-panel style="height: 100%;overflow-y: auto" data-lazy v-model="value" data-titles="API列表" data-action="Method.findMethodList" data-display-field="name" data-value-field="server" data-params="{'upper':''}" data-parent-field="upper" @data-on-node-click="handleNodeClick" />
    </el-aside>
    <el-container>
      <el-main style="border: 1px solid #EBEEF5;background-color: #FFFFFF">
        <k-api-rpanel style="height: 100%;overflow-y: auto" :method-model="methodModel" data-action="Model.findModel"></k-api-rpanel>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import kayak from '@/frame/kayak.js'
import KApiRpanel from '@/components/k-element/k-api-rpanel/k-api-rpanel.vue'
export default {
  name: "KApiPanel",
  componentName: 'KSearchPanel',
  components: {KApiRpanel},
  data() {
    return {
      value:'',
      methodModel: {}
    }
  },
  methods:{
    handleNodeClick(data, node, obj){
      console.log(data)
      if(node.level > 1){
        this.methodModel = data
      }
    }
  }
}
</script>

<style scoped>

</style>
