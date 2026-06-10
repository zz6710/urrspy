<template>
  <div>
    <k-form-search-customize data-target="prodWorkdayProgramGrid" v-model="prodSearchParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="prodWorkdayProgramGrid" data-action='T8ProdWorkdays.findT8ProdWorkday1' @data-row-select="selectRow" >
      <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="产品代码 " data-name="prodCode" ></k-grid-column>
      <k-grid-column data-header="产品名称 " data-name="prodName"></k-grid-column>
      <k-grid-column data-header="产品形态" data-name="prodMode" data-dict="t8_prod_mode"></k-grid-column>
      <k-grid-column data-header="产品系列" data-name="prodSeries"  data-hidden="true"></k-grid-column>
      <k-grid-column data-header="认购起始日" data-name="applyStartDate" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="认购结束日" data-name="applyEndDate" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="产品成立日" data-name="establishDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="开放起始日" data-name="openStartDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="开放结束日" data-name="openEndDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="产品到期日" data-name="endDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="产品清盘日" data-name="liquidate" data-hidden="true"></k-grid-column>
      <k-grid-column data-header="产品系列" data-name="seriesName" ></k-grid-column>
      <k-grid-column data-header="产品周期状态" data-name="prodStatus" data-dict="t8_prod_status"></k-grid-column>
      <k-grid-column data-header="周期子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status"></k-grid-column>
      <k-grid-column data-header="工作日方案类型" data-name="pgmno"  data-hidden="true"></k-grid-column>
      <k-grid-column data-header="工作日方案类型" data-name="pgmname"></k-grid-column>
      <template slot="operate"  slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="调整产品开放日"  v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('T8ProdWorkdays.upAddT8ProdWorkdays')"
               data-functype="POPUP" data-size="mini" data-target="prodWorkdayAdjustPopup">
          <md-icon md-src="/static/svg/workday.svg" />
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="查看记录"  v-if="global.getProdIfUser(scope.row.row.id)"
               data-functype="POPUP" data-size="mini" data-target="prodWorkdayPopup">
          <md-icon>library_books</md-icon>
        </k-btn>


      </template>
    </k-grid>

    <k-popup ref="prodWorkdayPopup" data-title="查看记录" data-width="60%">
      <ProdWorkdayLog ref="prodWorkdayLog" :fromData="fromData"></ProdWorkdayLog>
    </k-popup>

    <k-popup ref="prodWorkdayAdjustPopup" data-title="开放日调整" data-width="55%" :data-dialog-drag="true">
      <ProdWorkdayAdjust ref="prodWorkdayAdjust" :fromData="fromData"></ProdWorkdayAdjust>
    </k-popup>

    <div class="popClass" >
    <k-popup @data-closed="()=>{this.selectPgmno='';this.$refs.ListProdWorkDay.hackReset=false;this.prodCode='';
                this.openStartDate='';this.openEndDate=''; this.id='';}"
             @data-opened="()=>{this.$refs.ListProdWorkDay.hackReset=true;this.selectPgmno=selectRowData.pgmno;
             this.prodCode=selectRowData.prodCode;this.openStartDate=selectRowData.openStartDate;
             this.openEndDate=selectRowData.openEndDate; this.id=selectRowData.id;     }"
             ref="workdayPopup" data-width="80%">
      <div style="overflow: auto;padding-top: 20px">
      <ListProdWorkDay ref="ListProdWorkDay" :pgmno="selectPgmno" :prodCode="prodCode" :fromData="fromData" @submitClose="popupClose"
                       :openStartDate="openStartDate" :openEndDate="openEndDate" :prodInfoId="id"></ListProdWorkDay>
      </div>
    </k-popup>
    </div>
  </div>
</template>

<script>
  import ListProdWorkDay from "./ListProdWorkDay";
  import ProdWorkdayAdjust from "./ProdWorkdayAdjust";
  import ProdWorkdayLog from "./ProdWorkDayLog";
  import {
    assign
  } from "lodash";

  export default {
    name: "ProdWorkday",
    components: {
      ListProdWorkDay,
      ProdWorkdayAdjust,
      ProdWorkdayLog,
    },
    data() {
      return {
        prodSearchParam: {
          prodCode: ''
        },
        selectRowData: {},
        fromData:{},

        openEndDate: '',
        openStartDate: '',
        selectPgmno: '',
        prodCode:'',
        prodMode:'',
        id:'',
        pgmno:'',
        showListProdWorkDay: false,
      };
    },
    created() {
      this.global.getProdUser('');
    },
    methods: {
      popupClose(){
        this.$refs.workdayPopup.close();
        this.$refs.prodWorkdayAdjustPopup.close();
      },


      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row),
          _this.fromData = assign({}, row)
      }
    }
  };
</script>
<style scoped>
   .popClass ::v-deep .el-dialog {padding-top: 35px;margin-top: 35px !important;margin-right: 10px}

</style>
