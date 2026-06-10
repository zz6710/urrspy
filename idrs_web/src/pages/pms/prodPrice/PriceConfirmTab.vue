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
import M82002_1 from '../M82/M82002_1'
import T8ProdPriceConfirm from './T8ProdPriceConfirm'
import T8FeeConcessionConfirm from '../feeConcession/T8FeeConcessionConfirm'

export default {
  name: "PriceConfirmTab",
  components: {
    M82002_1,
    T8ProdPriceConfirm,
    T8FeeConcessionConfirm
  },
  data() {
    return {
      tabs: [
        {
          title: "额度需求确认",
          name: "M82002_1",
          param: {}
        },
        {
          title: "业绩基准确认",
          name: "T8ProdPriceConfirm",
          param: {}
        },
        {
          title: "费用优惠确认",
          name: "T8FeeConcessionConfirm",
          param: {}
        }
      ],
      selected: {
        title: "额度需求确认",
      },
    }
  },
  methods: {},
  created() {
    //接收路由中的title，决定激活哪个tab页
    let title = this.$route.query.title;
    if (title != '' && title != undefined) {
      this.$set(this.selected, 'title', title);
    }
  }
}
</script>

<style scoped lang="scss" scoped>
::v-deep .el-tabs__item {
  background-color: white;
}
</style>
