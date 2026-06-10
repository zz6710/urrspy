<template>
  <div>
    <ReportQueryForm ref="treelistQuery" data-target="treelist" :data-for-table="dataForTable" @loadFormData="loadFormData"></ReportQueryForm>
    <md-card class="report-div">
      <md-card-content>
        <k-report ref="treelist" :data-html="report.html" :data-menu-id="dataForTable"
                  :data-swkrntpomzqa="report.swkrntpomzqa"/>
      </md-card-content>
    </md-card>
  </div>
</template>

<script>
  import ReportQueryForm from "@/pages/report/develop/component/reportQueryForm";
  import KReport from "@/components/k-element/k-report/k-report.vue"

  export default {
    name: "M87Tree",
    components: {KReport, ReportQueryForm},
    data() {
      return {
        report: {
          html: "static/report/treelist.html",
          swkrntpomzqa: "1, 2, 4d, 8, 16, 32, 128",
        },
      }
    },
    computed: {
      dataForTable() {
        return this.$route.meta.componentName
      },
    },
    methods: {
      loadFormData(data) {
        this.$refs.treelist.loadDataHandler(data);
      }
    }
  }
</script>

<style scoped>

</style>
