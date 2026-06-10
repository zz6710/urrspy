<template>
  <div class="myAssetSource tab-page">
    <div class="myAssetSource_block01">
      <div class="myAssetSource_block01_line1">
        <div>确认状态:</div>
        <div class="tagBlock" :class="{ 'tagBlock-active': isActive1 === item.id }" v-for="item in flowStatus"
             :key="item.id" @click="changeTab(1, item.id)">
          {{ item.value }}
        </div>
      </div>
      <div class="myAssetSource_block01_line1">
<!--        <span style="color: #F56C6C">*</span>-->
        <div>变更日期:</div>
        <div style="width: 15%;margin-left: 10px">
<!--          <k-field-date v-model="searchParam.dealDate" data-value-format="yyyyMMdd"  :data-default-value="currentDate" @data-on-change="changeTab()" ></k-field-date>-->
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"  @data-on-change="changeTab()"></k-field-date>
        </div>
      </div>
    </div>
    <!-- <div class="myAssetSource_block02"> -->
      <k-grid ref="assetAourceDataChgInfo" data-action="SourceDataChgInfoModel.findAssetSourceDataChgInfoModelForOne" @data-row-select="selectRow" :data-autoload="false" data-operate-width="200px">
        <k-grid-column data-align="left" data-header="数据库表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-align="left" data-header="主键信息" data-name="naturalKeys" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="主键信息名" data-name="naturalKeysName"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据涉及报表" data-name="reportName"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据状态" data-name="status" data-dict="sourceDataStatus"></k-grid-column>
        <k-grid-column data-align="left" data-header="变更日期" data-name="dealDate"></k-grid-column>
        <k-grid-column data-align="left" data-header="确认日期" data-name="updDt"></k-grid-column>
<!--        <template slot="operate" slot-scope="scope">-->
<!--          <k-btn class="btn-custom-plain" data-descript="查看详情" data-functype="POPUP" data-size="mini"-->
<!--                 :data-model="scope.row.row" :data-handler="openSourceChgPage"  v-if="isbtnShow">-->
<!--            查看详情-->
<!--          </k-btn>-->
<!--        </template>-->
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="确认源数据变化信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('SourceDataChgInfoModel.confirmSourceDataChgInfoModel')"
                 :data-model="scope.row.row" data-target="confirmSourceDataChgInfoPopup" :data-disabled="scope.row.row.status === '1'">
            确认
          </k-btn>
          <k-btn class="btn-custom-plain" data-descript="查看源数据变化信息" data-functype="POPUP" v-if="global.isShowAuthorityButton('SourceDataChgInfoModel.confirmSourceDataChgInfoModel')"
                 data-target="msgSourceDataChgInfoPopup">
            详情
          </k-btn>
        </template>
      </k-grid>
    <!-- </div> -->
    <!--    确认SourceDataChginfo弹出框   -->
    <k-popup ref="confirmSourceDataChgInfoPopup" data-title="确认">
      <EditComp ref="editComp" @loadGriding="loadGriding"
                :info="formData"
                :sourceDataGrid="sourceDataChgs"
                :disabledVal="false"/>
    </k-popup>
    <!--    SourceDataChginfo详情弹出框   -->
    <k-popup ref="msgSourceDataChgInfoPopup" data-title="详情">
      <MsgComp ref="msgComp" @loadGriding="loadGriding"
               :info="formData"
               :sourceDataGrid="sourceDataChgs"
               :disabledVal="false"/>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
import {assign} from "lodash";
import EditComp from "@/pages/pms/sourceDataChgInfo/ConfirmSourceData";
import MsgComp from "@/pages/pms/sourceDataChgInfo/MsgSourceData";

export default {
  name: "myAssetSource",
  components: {EditComp,MsgComp},
  watch: {
    // 查询导入日期
    BreathDay() {
      console.log(this.BreathDay);
      this.$set(this.searchParam, 'theoryReportStartDate', this.BreathDay == null ? '' : this.BreathDay[0]);
      this.$set(this.searchParam, 'theoryReportEndDate', this.BreathDay == null ? '' : this.BreathDay[1]);
    },
  },
  data() {
    return{
      BreathDay: [],
      isActive1: null,
      searchParam:{},
      currentDate:'',//定义当前日期回显使用
      selectRowData: {},
      isbtnShow:true,
      formData: {},
      flowStatus: [
        {
          id: 0,
          value: "未确认"
        },
        {
          id: 1,
          value: "已确认"
        }
      ],
      sourceDataChgs: {}
    }
  },
  created() {
    this.isActive1 = this.flowStatus[0].id;
    // this.httpUtil.sysDate().then(res => {
    //   if (res) {
    //     this.$set(this.searchParam, 'dealDate', res.toString());
    //   }
    // })
    this.$nextTick(() => {
      this.changeTab(1,0);
    });
  },
  activated() {
    this.isActive1 = this.flowStatus[0].id;
    //this.loadCount();
    this.$nextTick(() => {
      this.changeTab(1,0);
    });
  },
  methods: {
    loadGriding(val){
      this.$refs.confirmSourceDataChgInfoPopup.close();
      let params = {};
      //params.dealDate = this.searchParam.dealDate;
      params.status = this.isActive1;
      this.$refs.assetAourceDataChgInfo.load(params);
    },
    openSourceChgPage(value){
      let pathUrl = '/main/pms/sourceDataChgInfo/SourceDataChgInfo';
      this.$router.push({
        path: pathUrl,
        query: value,
      });
      console.log("value=:>>>>",value);
    },
    changeTab(flag, id) {
      // if (this.searchParam.dealDate === null ){
      //   Tools.alert("请选择数据日期！","danger");
      //   return false;
      // }
      flag == 1 ? (this.isActive1 = id) : (this.isActive1 = this.isActive1);
      if (this.isActive1 == 1) { // 已确认
        this.isbtnShow = false;
      }else if(this.isActive1 == 0){
        this.isbtnShow = true; // 未确认
      }
      let params = {};
      let dealDate ='';
      // this.httpUtil.sysDate().then(res => {
      //   if (res) {
      //     if (this.searchParam.dealDate === undefined) {
      //       dealDate = res.toString();
      //     } else {
      //       dealDate = this.searchParam.dealDate;
      //     }
      //     params.dealDate = dealDate;
      //     params.status = this.isActive1;
      //     this.$refs.sourceDataChgInfo.load(params);
      //   }
      // })
      if (this.searchParam.theoryReportStartDate!==undefined && this.searchParam.theoryReportStartDate!=='' && this.searchParam.theoryReportStartDate!=null) {
        params.theoryReportStartDate = this.searchParam.theoryReportStartDate;
      }
      if (this.searchParam.theoryReportEndDate!==undefined && this.searchParam.theoryReportEndDate!=='' && this.searchParam.theoryReportEndDate!=null) {
        params.theoryReportEndDate = this.searchParam.theoryReportEndDate;
      }
      // dealDate = this.searchParam.dealDate;
      // params.dealDate = dealDate;
      params.status = this.isActive1;
      this.$refs.assetAourceDataChgInfo.load(params);
    },
    selectRow(row, column, event) {
      this.selectRowData = assign({}, row);
      this.formData = assign({}, row)
    },
  }
}
</script>

<style lang="scss" scoped>
  .myAssetSource_block01 {
      margin-bottom: 15px;
  }

  .myAssetSource_block01_line1 {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-wrap: wrap;
    margin: 15px 0;
  }

  .myAssetSource_block02_state {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
</style>
