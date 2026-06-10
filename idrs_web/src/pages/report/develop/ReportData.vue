<template>
  <div>

      <md-card-header>控件列表</md-card-header>
        <k-grid ref="reportPropertyGrid" data-action="ReportCss.findById"
                :data-params="{forTable:this.$route.query.forTable}" :data-operate-column="false" :data-page-size="0"
                :data-card="false">
          <k-grid-column data-header="for_table" data-name="forTable"></k-grid-column>
          <k-grid-column data-header="name" data-name="cssName"></k-grid-column>
          <k-grid-column data-header="label_discrible" data-name="labelDiscrible"></k-grid-column>
        </k-grid>
    <md-card>
      <md-card-header>编辑用于查询的sql语句</md-card-header>
      <md-card-content>
        <k-form ref="editQuerySQLForm" :data-col="3">
          <k-form-item label="menuid">
            <k-field-text v-model="formData.forTable" :data-allowblank="false" :data-disabled="true"
                          :data-clearable="false"/>
          </k-form-item>
          <k-form-item label="sql_exeid">
            <k-field-text v-model="formData.exeid" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="sql_id">
            <k-field-text v-model="formData.sqlid" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="sql语句" :data-col="3">
            <k-field-text inputType="textarea" v-model="formData.reportSql" :data-allowblank="false"
                                      :data-min-row="5" :data-max-row="10"/>
          </k-form-item>
          <k-form-footer data-align="center" >
            <k-btn class="btn-custom-primary" @click.native="saveSQL" style="width: 140px">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存以上SQL信息
            </k-btn>
          </k-form-footer>
        </k-form>
      </md-card-content>
    </md-card>

      <md-card-header>sql语句列表</md-card-header>
        <k-grid ref="reportSqlGrid" @data-row-select="selectRow" :dataData="gridRows" :data-page-size="0"
                :data-card="false">
          <k-grid-column data-header="sql_exeid" data-name="exeid"></k-grid-column>
          <k-grid-column data-header="sql_id" data-name="sqlid"></k-grid-column>
          <k-grid-column data-header="report_sql" data-name="reportSql"></k-grid-column>
          <k-grid-column data-header="id" data-name="id"></k-grid-column>
          <template slot="operate" slot-scope="scope">
            <k-btn class="md-danger md-just-icon md-simple" @click.native.stop="deleteRow(scope.row)"
                   data-type="danger" data-descript="删除">
              <md-icon>delete_sweep</md-icon>
            </k-btn>
          </template>
        </k-grid>
        <div class="bottom-btn">
          <k-btn class="btn-custom-primary" :data-handler="saveData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
          </k-btn>
          <k-btn class="btn-custom-primary" :data-handler="backtrack">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>返回
          </k-btn>
        </div>
  </div>
</template>

<script>
  import httpUtil from "@/frame/httpUtil";

  export default {
    name: "reportData",
    data() {
      return {
        formData: {},
        gridRows: {
          rows: [],
          total: 0
        }
      }
    },
    activated() {
      this.initFormData()
      this.loadGridData()
    },
    methods: {
      selectRow(row, column, event) {
        row["forTable"] = this.$route.query.forTable;
        this.formData = Object.assign({}, row)
      },
      initFormData() {
        this.formData = {
          forTable: this.$route.query.forTable,
          exeid: '',
          sqlid: '',
          reportSql: ''
        }
      },
      loadGridData() {
        httpUtil.comnQuery({
          action: 'ReportSql.findReportSqls',
          params: {
            forTable: this.$route.query.forTable,
          }
        }).then(res => {
          this.$set(this.gridRows, "rows", res.rows)
          this.$set(this.gridRows, "total", res.rows.length)
          this.$refs.reportSqlGrid.initData()
        })
      },
      saveSQL() {
        this.gridRows.rows = this.gridRows.rows.filter((row) => {
          return row.exeid != this.formData.exeid
        })
        this.gridRows.rows.push(this.formData);
        this.gridRows.total++;
        this.initFormData();
        this.$refs.reportSqlGrid.initData()
      },
      deleteRow(row) {
        this.$confirm('确定删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.gridRows.rows.splice(row.$index, 1);
          this.gridRows.total--;
          this.$refs.reportSqlGrid.initData()
        }).catch(()=>{
          this.initFormData()
        })
      },
      saveData() {
        let rows = this.gridRows.rows;
        let exeid = "";
        let sqlid = "";
        let reportSql = "";
        for (let i = 0; i < rows.length; i++) {
          if (i === 0) {
            exeid = (rows[i].exeid);
            sqlid = (rows[i].sqlid);
            reportSql = (rows[i].reportSql);
          } else {
            exeid += "&&&" + (rows[i].exeid);
            sqlid += "&&&" + (rows[i].sqlid);
            reportSql += "&&&" + (rows[i].reportSql);
          }
        }
        httpUtil.comnUpdate({
          action: 'ReportSql.addReportSql',
          params: {
            forTable: this.$route.query.forTable,
            exeid: exeid,
            sqlid: sqlid,
            reportSql: reportSql,
          }
        }).then(() => {
          this.loadGridData()
        })
      },
      backtrack() {
        this.$router.push({
          path: "/main/report/develop/ReportCondition",
        })
      }
    }
  }
</script>

<style scoped lang="scss">
  .bottom-btn {
    text-align: center;
  }
</style>
