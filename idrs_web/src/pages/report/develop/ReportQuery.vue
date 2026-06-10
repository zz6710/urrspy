<template>
  <div>
    <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="resetFormData"
           data-target="addReportQueryPopup">
      <md-icon md-src="/static/svg/add.svg" />新增
    </k-btn>
    <k-grid ref="reportCssGrid" @data-row-select="selectRow" :data-data="gridRows">
      <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="name" data-name="cssName"></k-grid-column>
      <k-grid-column data-header="type" data-name="cssType" data-dict="input_type"></k-grid-column>
      <k-grid-column data-header="class" data-name="cssClass"></k-grid-column>
      <k-grid-column data-header="label_discrible" data-name="labelDiscrible"></k-grid-column>
      <template slot="operate" slot-scope="scope">
<!--        <k-btn class="md-info md-just-icon md-simple" data-descript="上移" @click.native.stop="moveUpRow(scope.row.row)">-->
<!--          <md-icon md-src="/static/svg/move_up.svg"/>-->
<!--        </k-btn>-->
<!--        <k-btn class="md-info md-just-icon md-simple" data-descript="下移"-->
<!--               @click.native.stop="moveDownRow(scope.row.row)">-->
<!--          <md-icon md-src="/static/svg/move_down.svg"/>-->
<!--        </k-btn>-->
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改报表样式" data-functype="POPUP" data-size="mini"
               data-target="editReportQueryPopup">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" @click.native.stop="deleteRow(scope.row.row)"
               data-type="danger" data-descript="删除报表样式">
          <md-icon>delete_sweep</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <div>
      <k-btn class="btn-custom-primary" :data-handler="saveData">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
      </k-btn>
      <k-btn class="btn-custom-primary" :data-handler="backtrack">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>返回
      </k-btn>
    </div>

    <!--    添加弹出框   -->
    <k-popup ref="addReportQueryPopup" data-title="新增" :dataDialogDrag="true">
      <EditComp @editGrid="addGrid" ref="addComp" :info="{}" :disabledVal="false"/>
    </k-popup>

    <!--    修改弹出框   -->
    <k-popup ref="editReportQueryPopup" data-title="修改" :dataDialogDrag="true">
      <EditComp  @editGrid="updGrid" ref="editComp" :info="formData" :disabledVal="true"/>
    </k-popup>



  </div>
</template>

<script>
  import Tools from "@/utils/tools.js";
  import httpUtil from "@/frame/httpUtil";
  import EditComp from "./edit/ReportQueryEdit";

  export default {
    name: "ReportQuery",
    components: {EditComp},
    data() {
      return {
        formData: {},
        gridRows: {
          rows: [],
          total: 0
        },
      };
    },

    mounted() {
      this.loadGridData()
    },
    methods: {
      //修复增删改bug --axin
      updGrid(val){
        let arr = this.gridRows.rows;
        let index = arr.findIndex((arr)=> arr.id === val.id);
        this.$delete(this.gridRows.rows,index);
        this.gridRows.rows.splice(index,0,val);
        this.$refs.editReportQueryPopup.close();
      },

      addGrid(val){
        this.gridRows.total += 1;
        this.gridRows.rows.push(val);
        this.$refs.addReportQueryPopup.close();
      },

      deleteRow(row) {
        this.$confirm('确定删除吗?', '提示', {}).then(confirm => {
          if (confirm) {
            let arr = this.gridRows.rows;
            let index = arr.findIndex((arr)=> arr.id === row.id);
            this.$delete(this.gridRows.rows,index);
          }
        }).catch(error => {
          console.log('取消删除')
        })
      },


      loadGrid(val){
        this.$refs.addReportQueryPopup.close();
        this.$refs.editReportQueryPopup.close();
        this.loadGridData();
        // this.$refs.reportCssGrid.load();
      },

      selectRow(row, column, event) {
        this.formData = Object.assign({}, row)
      },
      resetFormData() {
        this.formData = {}
      },
      loadGridData() {
        httpUtil.comnQuery({
          action: 'ReportCss.findReportCss',
          params: {
            forTable: this.$route.query.forTable,
          }
        }).then(res => {

          this.$set(this.gridRows, "rows", res.rows)
          this.$set(this.gridRows, "total", res.rows.length)
          for(let i = 0;i < this.gridRows.rows.length; i++){
            this.$set(this.gridRows.rows[i],'row_index',i)
          }
          this.$refs.reportCssGrid.initData()
        })
      },
      swapRows(rows, index1, index2) {
        rows[index1] = rows.splice(index2, 1, rows[index1])[0]
        return rows
      },
      moveUpRow(row) {
        if (row.row_index !== 0) {
          this.swapRows(this.gridRows.rows, row.row_index, row.row_index - 1)
        } else {
          Tools.alert("已经是最上面那个了！", "warning")
        }
      },
      moveDownRow(row) {
        if (row.row_index + 1 !== this.gridRows.total) {
          this.swapRows(this.gridRows.rows, row.row_index, row.row_index + 1)
        } else {
          Tools.alert("已经是最下面那个了！", "warning")
        }
      },


      saveData() {
        httpUtil.comnUpdate({
          action: 'ReportCss.addReportCss',
          params: {
            forTable: this.$route.query.forTable,
            reportCssList: JSON.stringify(this.gridRows.rows)
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
  };
</script>
