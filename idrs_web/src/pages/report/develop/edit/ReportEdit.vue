<template>
  <div>
    <md-card class="report-div">
      <md-card-content style="padding: 0">
        <k-report ref="report" :data-html="report.html" :data-menu-id="$route.query.forTable" :data-edit="true"
                  @saveReportXml="saveReportXml" :dataMenuResize="true"></k-report>
      </md-card-content>
    </md-card>
  </div>
</template>

<script>
  import httpUtil from "@/frame/httpUtil";

  export default {
    name: "ReportEdit",
    data() {
      return {
        report: {
          html: "static/report/report.html",
        },
      }
    },
    mounted() {
      this.$refs.report.loadHandler()
    },
    methods: {
      saveReportXml(data) {
        httpUtil.comnUpdate({
          action: 'ReportXml.addReportXml',
          params: data
        }).then(res => {
          if(res.success){
            this.tools.removeTab(this.$route.meta.componentName,false)
            this.$router.push({
              path: "/main/report/develop/ReportCondition",
            })
          }
        })
      }
    },
  }
</script>

<style scoped lang="scss">
</style>
