<template>
  <md-card>
    <md-card-header>编辑TreeList表头XML</md-card-header>
    <md-card-content>
      <k-form ref="editTreeListXmlForm" :data-col="1" data-input-width="800px">
        <k-form-item label="单个数据源关系">
          <k-field-text v-model="formData.exeid"/>
        </k-form-item>
        <k-form-item label="TreeList列排序">
          <k-field-text v-model="formData.tableSort"/>
        </k-form-item>
        <k-form-item label="TreeList">
          <k-field-text data-type="textarea" v-model="formData.xml" :data-min-row="20" :data-max-row="50"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" @click.native="saveTreeListXML">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存xml信息
          </k-btn>
        </k-form-footer>
      </k-form>
    </md-card-content>
  </md-card>
</template>

<script>
  import httpUtil from "@/frame/httpUtil";

  export default {
    name: "TreeListEdit",
    data() {
      return {
        formData: {
          exeid: '',
          tableSort: '',
          xml: ''
        }
      }
    },
    activated() {
      this.loadTreeListData();
    },
    methods: {
      loadTreeListData(){
        httpUtil.comnQuery({
          action: 'ReportXml.findReportXmls',
          params: {
            forTable: this.$route.query.forTable,
          }
        }).then(res => {
          this.$set(this.formData,"exeid",res.exeid);
          this.$set(this.formData,"tableSort",res.tableSort);
          this.$set(this.formData,"xml",res.xml);
        })
      },
      saveTreeListXML() {
        if (this.formData.exeid === '' || this.formData.exeid == null || this.formData.exeid.length === 0) {
          this.formData.exeid = "null";
        }
        if (this.formData.tableSort === '' || this.formData.tableSort == null || this.formData.tableSort.length === 0) {
          this.formData.tableSort = "null";
        }
        httpUtil.comnUpdate({
          action: 'ReportXml.addTreeListXml',
          params: {
            forTable: this.$route.query.forTable,
            ...this.formData
          }
        }).then(() => {
          this.$router.push({
            path: "/main/report/develop/ReportCondition",
          })
        })
      }
    }
  }
</script>

<style scoped>

</style>
