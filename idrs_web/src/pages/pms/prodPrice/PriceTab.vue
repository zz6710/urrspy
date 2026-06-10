<template>
  <div>
    <el-tabs type="card" v-model="selected.title">
      <el-tab-pane v-for="tab in tabs" :label="tab.title" :name="tab.title" :key="tab.title">
        <component :is="tab.name" :param="tab.param"></component>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import M82002 from '../M82/M82002'
import T8ProdPrice from './T8ProdPrice'
import T8FeeConcession from '../feeConcession/T8FeeConcession'

export default {
  name: "PriceTab",
  components: {
    M82002,
    T8ProdPrice,
    T8FeeConcession
  },
  data() {
    return {
      tabs: [
        {
          title: "额度需求",
          name: "M82002",
          param: {}
        },
        {
          title: "业绩基准",
          name: "T8ProdPrice",
          param: {}
        },
        {
          title: "费用优惠",
          name: "T8FeeConcession",
          param: {}
        }
      ],
      selected: {
        title: "额度需求",
      },
      title: '',
    }
  },
  methods: {},
  created() {
    //接收路由中的title，决定激活哪个tab页
    this.title = this.$route.query.title;
    if (this.title != '' && this.title != undefined) {
      this.$set(this.selected, 'title', this.title);
    }
  }
}
</script>

<style scoped lang="scss" scoped>
::v-deep .el-tabs__item {
  background-color: white;
}
</style>
