<template>
  <div :style="{
    'height':this.$vnode.data.key == this.$route.params.id?'100%':'0px',
    'visibility':this.$vnode.data.key == this.$route.params.id?'':'hidden'
  }">
    <keep-alive>
      <ReportQueryForm :key="report.menuid" ref="reportQuery" :data-for-table="report.menuid" data-target="report"
                     @loadFormData="loadFormData"></ReportQueryForm>
    </keep-alive>
      <md-card class="report-div">
      <md-card-content>
        <k-report ref="report" :data-html="report.html" :data-menu-id="report.menuid"
                  :data-swkrntpomzqa="report.swkrntpomzqa"/>
      </md-card-content>
    </md-card>
  </div>
</template>

<script>
  import ReportQueryForm from "@/pages/report/develop/component/reportQueryForm";
  import KReport from "@/components/k-element/k-report/k-report.vue"
  export default {
    name: "M87Report",
    components: {ReportQueryForm, KReport},
    data() {
      return {
        report: {
          html: "static/report/report.html",
          menuid:this.$vnode.data.key,
          swkrntpomzqa: "1, 2, 4d, 8, 16, 32, 128",
        },
      }
    },
    computed: {
      dataForTable() {
        return this.$route.meta.componentName
      }
    },
    mounted() {
    },
    methods: {
      loadFormData(data) {
        this.$refs.report.loadDataHandler(data);
      }
    },
    watch: {
      $route(data) {
        this.$nextTick(() => {
        })
      }
    }
  }
</script>

<style scoped>

</style>
