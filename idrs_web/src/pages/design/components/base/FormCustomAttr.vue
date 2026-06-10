<template>
  <div ref="formCustomAttr">
    <!-- <el-form-item label="" label-width="auto"> -->
      <div class="customAttrs" v-for="(item,index) in customAttrs" :key="index">
        <el-input v-model="customAttrs[index]"></el-input>
        <el-button @click="customAttrs.splice(index, 1)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 8px;margin-left: 10px;"></el-button>
      </div>
    <!-- </el-form-item> -->
    <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { customAttrs.push('') }">添加属性</el-button>
  </div>
</template>

<script>

export default {
  name: "formCodeEditor",
  components: {
    
  },
  props: {
    customAttrs: {
      type: Array,
      default: () => {return []}
    }
  },
  data () {
    return {
    }
  },
  created (){
    
  },
  mounted () {

  },
  methods: {
    
  },
  watch: {
    
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  .customAttrs {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    margin-bottom: 5px;
  }

</style>
