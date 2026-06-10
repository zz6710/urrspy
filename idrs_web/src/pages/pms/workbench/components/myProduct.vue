<template>
  <div class="myFlow">
    <div class="tabs">
      <div
          class="tab-item" v-if="global.isShowAuthorityButton('DataCheckModel.findIndicatorCheckRemind')"
          :class="{active: currentTabLabel == '指标校验结果'}"
          @click="change('指标校验结果')"
      >指标校验结果

      </div>
      <div
        class="tab-item" v-if="global.isShowAuthorityButton('DataCheckModel.findSubmitRemind')"
        :class="{active: currentTabLabel == '基础数据补录提醒'}"
        @click="change('基础数据补录提醒')"
      >基础数据补录提醒

      </div>
    </div>
    <template v-if="currentTabLabel == '指标校验结果'">
      <div class="myFlow_block01">
        <div class="myFlow_block01_line1">
          <div>报表大类:</div>
          <div
            class="tagBlock"
            :class="{ 'tagBlock-active': isActive2 == item.id }"
            v-for="item in flowClassification"
            :key="item.id"
            @click="changeTab('reportCatgory', item.id)"
          >
            {{ item.value }}
          </div>
        </div>
        <div class="myFlow_block01_line1" ref="myFlowBlock01Line1">
          <div>校验结果:</div>
          <div
            class="tagBlock"
            :class="{ 'tagBlock-active': isActive1 == item.id }"
            v-for="item in prodModeData"
            :key="item.id"
            @click="changeTab('validateResult', item.id)"
          >
            {{ item.value }}
          </div>
        </div>
        <div class="myFlow_block01_line1" >
          <span style="color: #F56C6C">*</span>
          <div>校验日期:</div>
          <div style="width: 15%;margin-left: 10px">
            <k-field-date v-model="searchParam.createDate" data-value-format="yyyyMMdd"  :data-default-value="currentDate" @data-on-change="changeTab()" >
            </k-field-date>
          </div>
        </div>
      </div>
      <div class="myFlow_block02">
        <div>
          <k-grid
            ref="disclosureNoticeGrid"
            @data-row-select="selectRow"
            data-action="DataCheckModel.findIndicatorCheckRemind"
            v-if="global.isShowAuthorityButton('DataCheckModel.findIndicatorCheckRemind')"
            @init="(grid) => {this.$kgrid = grid;}" key="disclosureNoticeGrid" :data-autoload="false">
            <k-grid-column data-align="center" data-header="报表大类" data-name="reportCatgory" data-dict="report_type"></k-grid-column>
            <k-grid-column data-align="center" data-header="报表名称" data-name="tableName"></k-grid-column>
            <k-grid-column data-align="center" data-header="报表名称" data-name="validateTable" data-hidden = "true"></k-grid-column>
            <k-grid-column data-align="center" data-header="校验结果" data-name="validateResult" data-dict="rpt_validate_result"> </k-grid-column>
            <k-grid-column data-align="center" data-header="数据日期" data-name="dealDate" > </k-grid-column>
            <k-grid-column data-align="center" data-header="校验日期" data-name="createDate" > </k-grid-column>
            <k-grid-column data-align="center" data-header="数量" data-name="dataNum"> </k-grid-column>
            <template slot="operate" slot-scope="scope">
              <div class="templateDiv">
                <k-btn
                  class="btn-custom-plain"
                  data-descript="查看详情"
                  data-functype="POPUP"
                  data-size="mini"
                  :data-model="scope.row.row"
                  :data-handler="openPage"
                >
                  查看详情
                </k-btn>
              </div>
            </template>
          </k-grid>
        </div>
      </div>
    </template>


    <template v-if="currentTabLabel == '基础数据补录提醒'">
      <div class="myFlow_block01">
        <div class="myFlow_block01_line1" style="display: none">
          <div>补录页面:</div>
          <div class="tagBlock" :class="{ 'tagBlock-active': phaseId === item.id }"
               v-for="item in flowClassification02" :key="item.id" @click="changeTabPage('reportCatgory', item.id)" >
            {{ item.value }}
          </div>
        </div>
        <div class="myFlow_block01_line1" >
          <span style="color: #F56C6C">*</span>
          <div>持仓日期:</div>
          <div style="width: 15%;margin-left: 10px">
            <k-field-date v-model="searchParam.holdingDate" data-value-format="yyyyMMdd"  :data-default-value="currentDate" @data-on-change="changeTabPage()" >
            </k-field-date>
          </div>
        </div>
      </div>
      <div class="myFlow_block02">
        <div>
          <k-grid ref="prodNoticeGrid" @data-row-select="selectRow" data-action="DataCheckModel.findSubmitRemind"
                  @init=" (grid) => { this.$kgrid = grid; } " key="prodNoticeGrid" :data-autoload="false">
            <k-grid-column data-align="center" data-header="补录页面" data-name="page" data-dict="page"/>
            <k-grid-column data-align="center" data-header="持仓日期" data-name="holdingDate" />
            <k-grid-column data-align="center" data-header="数量" data-name="dataNum" />
            <template slot="operate" slot-scope="scope">
              <div class="templateDiv">
                <k-btn class="btn-custom-plain" data-descript="查看详情" data-functype="POPUP" data-size="mini"
                       :data-model="scope.row.row" :data-handler="prodOpenPage" >
                  查看详情
                </k-btn>
              </div>
            </template>
          </k-grid>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { assign } from "lodash";
import Tools from "@/utils/tools";
import global from "@/frame/global";
export default {
  props: {
    tabAuthorityInfo: Object
  },
  data() {
    return {
      $kgrid: null,
      isActiveMore: false,
      isActiveMore02: false,
      moreName: "",
      moreName02: "",
      isActive1: null,
      isActive2: null,
      isActive3: 0,
      selectRowData: {},
      waitStatus: [
        {
          id: 0,
          value: "待处理",
        },
        {
          id: 1,
          value: "已完成",
        }
      ],
      flowStatus: [
        {
          id: -1,
          value: "不限",
        },
        {
          id: 0,
          value: "初始化",
        },
        {
          id: 2,
          value: "校验成功",
        },
        {
          id: 1,
          value: "校验失败",
        },
        {
          id: 3,
          value: "报送成功",
        },
        {
          id: 4,
          value: "报送失败",
        },
      ],
      flowStatus02: [
        {
          id: 15,
          value: "公告发布",
        },
        {
          id: 10,
          value: "复核托管意见",
        },
        {
          id: 12,
          value: "底层数据导入",
        },
        {
          id: 13,
          value: "信披模板审批",
        },
        {
          id: 14,
          value: "审批被拒绝",
        },
        {
          id: 9,
          value: "填写托管意见",
        },
        {
          id: 6,
          value: "托管拒绝后补录",
        },
        {
          id: 5,
          value: "托管拒绝后分发",
        },
        {
          id: 4,
          value: "审批拒绝后补录",
        },
        {
          id: 3,
          value: "审批拒绝分发",
        },
        {
          id: 1,
          value: "投资经理披露确认",
        },
      ],
      flowClassification: [
        {
          id: '-1',
          value: "不限",
        },
        {
          id: '01',
          value: "中债三期",
        },
        {
          id: '02',
          value: "中债一二期",
        },
        {
          id: '03',
          value: "银保监报表",
        },

       {
          id: '05',
          value: "金融机构资管产品报告",
        },
        {
          id: '07',
          value: "人行资管产品采集",
        },
      ],
      flowClassification02: [
        {
          id: -1,
          value: "不限",
        },
        {
          id: '1',
          value: "债券信息补录",
        },
        {
          id: '2',
          value: "基金信息补录",
        },
        {
          id: '3',
          value: "股票信息补录",
        },
        {
          id: '4',
          value: "非标债券信息补录",
        },
        {
          id: '5',
          value: "资产管理产品补录",
        },
        {
          id: '6',
          value: "机构信息补录",
        },
        {
          id: '7',
          value: "交易对手信息补录",
        },
      ],
      operationTypeId: "-1",
      disclosureTypeId: "-1",
      currentTabLabel: '指标校验结果',
      status:'', //待办状态


      // ----------------  基础数据补录提醒对象  status  ----------------
      taskStatus: 1,
      phaseId: -1,
      prodMode: -1,
      taskStatusData: [
        {
          id: 1,
          value: "待处理",
        },
        {
          id: 2,
          value: "已完成",
        },
        // {
        //   id: 3,
        //   value: "已跳过",
        // }
      ],
      flowProcessData: [
        {
          id: 1,
          value: "发起清盘",
        },
        {
          id: 2,
          value: "清盘流程",
        },
      ],
      flowProcessShow:true,//发起流程列表
      phaseIdData: [
        {
          id: -1,
          value: "不限",
        },
        {
          id: 0,
          value: "产品研讨",
        },
        {
          id: 1,
          value: "产品创设",
        },
        {
          id: 2,
          value: "产品申报",
        },
        {
          id: 3,
          value: "产品发行",
        },
        {
          id: 5,
          value: "产品存续",
        },
        {
          id: 6,
          value: "产品到期",
        },
      ],
      prodModeData: [
        {
          id: 0,
          value: "不限",
        },
        {
          id: 1,
          value: "通过",
        },
        {
          id: -1,
          value: "不通过",
        },
        {
          id: 2,
          value: "校验预警",
        },
      ],
      // ----------------  基础数据补录提醒对象  end  ----------------
      userid:localStorage.getItem("userid"),

      theoryTime:[],
      theoryStartDate:'',
      theoryEndDate:'',
      realTime:[],
      realStartDate:'',
      realEndDate:'',
      searchParam:{},
      currentDate:'',//定义当前日期回显使用
    };
  },
  created() {
    this.isActive1 = this.prodModeData[0].id;
    this.isActive2 = this.flowClassification[0].id;
    this.httpUtil.sysDate().then(res => {
      if (res) {
        this.$set(this.searchParam, 'createDate', res.toString());
        this.$set(this.searchParam, 'holdingDate', res.toString());
      }
    })
    this.$nextTick(() => {
      this.changeTab();
      this.changeTabPage();
    });
  },
  filters: {
    fliterDate: function (value) {
      return value.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3");
    },
    fliterTime: function (value) {
      return value.replace(/^(\d{2})(\d{2})(\d{2})$/, "$1:$2:$3");
    },
    filterAuth: (val) => {
      return val > 99 ? '99+' : val
    }
  },

  methods: {
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.createDate, row.creatTime);
    },
    renderProcessInstanceDateTime(row) {
      return Tools.formatDateTime(row.processInstanceCreateDate, row.processInstanceCreateTime);
    },
    renderTaskCreateDateTime(row) {
      return Tools.formatDateTime(row.taskCreateDate, row.taskCreateTime);
    },
    //================================================基础数据补录提醒方法 -- status -- axin=====================================

    //-------------------------------  跳转详情 ------------------------------------------
    prodOpenPage(row){
      let pathUrl ='';
      if(row.page =='1'){
        pathUrl ='/main/report/M74R?menuName=债券信息补录数据';
      }else if(row.page =='2'){
        pathUrl ='/main/report/M74Q?menuName=基金信息补录数据';
      }else if(row.page =='3'){
        pathUrl ='/main/pms/asharedescription/Asharedescription?menuName=股票信息';
      }else if(row.page =='4'){
        pathUrl ='/main/pms/nonStand/NonStandInfoModel?menuName=非标债权信息';
      }else if(row.page =='5'){
        pathUrl ='/main/netvalSPV/NetValSPVInfoModel?menuName=资产管理产品';
      }else if(row.page =='6'){
        pathUrl ='';
      }else if(row.page =='7'){
        pathUrl ='/main/report/M74P?menuName=回购拆借持仓明细查询';
      }
      this.$router.push({
        path: pathUrl,
        query: row,
      });
    },
    //-------------------------------  清盘流程 ---------------------------------------------
    changeTabFlow(flag, id) {
      if (flag === 'flowStatus') {
        if (id === 1) {
          //发起清盘
          this.taskStatus = id;
          this.flowProcessShow = true;
          this.$refs.prodLiquidationGrid.load({processStatus: '1'})
        }else if (id === 2) {
          //清盘流程待办
          this.taskStatus = id;
          this.flowProcessShow = false;
          this.$refs.taskGridFlow.load({userid:this.userid,taskType:'0'});
        }
      }

    },
    //-------------------------------  重新加载列表 ------------------------------------------
    changeTabPage(flag, id) {
      if (this.searchParam.holdingDate === null ){
        Tools.alert("请选持仓日期！","danger");
        return false;
      }
      let params = {};
      let holdingDate ='';
      if (flag === 'reportCatgory') {
        this.phaseId = id;
      }
      if(this.phaseId !== -1){
        this.$set(params,'reportCatgory',this.phaseId);
      }
      this.httpUtil.sysDate().then(res => {
        if (res) {
          if (this.searchParam.holdingDate === undefined) {
            holdingDate = res.toString();
          } else {
            holdingDate = this.searchParam.holdingDate;
          }
          params.holdingDate = holdingDate;
          this.$refs.prodNoticeGrid.load(params);
        }
      })
    },
    //=============================================上面是产品的方法 --- END -- axin  ============================

    change(name) {
      let params = {};
      this.currentTabLabel = name;
      this.$nextTick(()=>{
        if(name=="基础数据补录提醒"){
          if(this.phaseId !==-1){
            this.$set(params,'reportCatgory',this.phaseId)
          }
          this.$set(params,'holdingDate',this.searchParam.holdingDate)
          this.$refs.prodNoticeGrid.load(params);
        }else if (name=="指标校验结果"){
          if (this.isActive2 !== '-1') {
            this.$set(params,'reportCatgory',this.isActive2);
          }
          if (this.isActive1 !== 0) {
            this.$set(params,'validateResult',this.isActive1)
          }
          this.$set(params,'createDate',this.searchParam.createDate)
          this.$refs.disclosureNoticeGrid.load(params);
        }
      })
    },
    changeTab(flag, id) {
      if (this.searchParam.createDate === null ){
        Tools.alert("请选择校验日期！","danger");
        return false;
      }
      let params = {};
      let createDate ='';
      if (flag === 'reportCatgory') {
        this.isActive2 = id;
        if (id !== '-1') {
          this.$set(params,'reportCatgory',id);
        }
      } else if (flag === 'validateResult') {
        this.isActive1 = id;
        if (id !== 0) {
          this.$set(params,'validateResult',id)
        }
      }
      this.httpUtil.sysDate().then(res => {
        if (res) {
          if (this.searchParam.createDate === undefined) {
            createDate = res.toString();
          } else {
            createDate = this.searchParam.createDate;
          }
          params.createDate = createDate;
          this.$refs.disclosureNoticeGrid.load(params);
        }
      })
    },


    selectRow(row, column, event) {
      this.selectRowData = assign({}, row);
    },
    openPage(row) {
      let pathUrl = '/main/reportSend/dataHandle/reportDataValRes';
      this.$router.push({
        path: pathUrl,
        query: row,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.myFlow_block01_line1 {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-wrap: wrap;
  margin: 15px 0;
}
.row_drop {
  .el-icon-d-arrow-right {
    font-size: 20px;
    transform: rotate(90deg);
  }
}
.myFlow_block01 {
  margin-bottom: 50px;
}
.myFlow_block02_state {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.dorp {
  width: 8px;
  height: 8px;
  background: #666;
  border-radius: 50%;
  margin-right: 10px;
}
.dorpColor1 {
  background: #e53929;
}
.dorpColor2 {
  background: #00bcd4;
}
.dorpColor3 {
  background: #4caf50;
}
.tabs {
  display: flex;
  margin: 10px 0 30px 0;
  .tab-item {
    position: relative;
    font-size: 14px;
    font-weight: bold;
    line-height: 30px;
    margin-right: 50px;
    border-bottom: 2px solid transparent;
    text-align: center;
    cursor: pointer;
    &.active {
      color: #5475ad;
      border-color: #5475ad;
    }
  }
}
.count {
  position: absolute;
  left: 100%;
  top: 0;
  background: #ff9e00;
  border-radius: 10px;
  color: #fff;
  padding: 0 4px;
  height: 14px;
  line-height: 14px;
  text-align: center;
  font-weight: normal;
  font-size: 12px;
}

</style>
