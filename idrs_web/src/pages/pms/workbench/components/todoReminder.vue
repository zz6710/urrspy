<template>
  <div class="myFlow">
    <template v-if="currentTabLabel == '信披待办'">
      <div class="myFlow_block01">
        <div class="myFlow_block01_line1">
          <div>披露计划:</div>
          <div
            class="tagBlock"
            :class="{ 'tagBlock-active': isActive2 === item.id }"
            v-for="item in flowClassification"
            :key="item.id"
            @click="changeTab(1, item.id)"
          >
            {{ item.value }}
          </div>
        </div>
        <div class="myFlow_block01_line1" >
          <span style="color: #F56C6C">*</span>
          <div>计划发布日期:</div>
          <div style="width: 15%;margin-left: 10px">
            <k-field-date v-model="searchParam.planFbDate" data-value-format="yyyyMMdd"  :data-default-value="currentDate" @data-on-change="changeTab()">
            </k-field-date>
          </div>
        </div>
      </div>
      <div class="myFlow_block02">
        <div>
          <k-grid
            ref="disclosureNoticeGrid"
            @data-row-select="selectRow"
            data-action="DeskTopModel.findDisclosureNDetails"
            @init="(grid) => {this.$kgrid = grid;}" key="disclosureNoticeGrid" :data-autoload="false">
            <k-grid-column data-header="id" data-name="id" :data-hidden="true"></k-grid-column>
            <k-grid-column data-align="center" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" data-width="150"></k-grid-column>
            <k-grid-column data-align="center" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type" data-width="200"></k-grid-column>
            <k-grid-column data-align="center" data-header="计划发布日期" data-type="date" data-name="planFbDate" > </k-grid-column>
            <k-grid-column data-align="center" data-header="公告状态" data-name="disclosureStatus" data-dict="xp_disclosure_notice_status"> </k-grid-column>
            <k-grid-column data-align="center" data-header="数量" data-name="disclosureCount" > </k-grid-column>

            <template slot="operate" slot-scope="scope">
              <k-btn
                class="btn-custom-plain"
                data-descript="查看详情"
                data-functype="popup"
                data-size="mini"
                :data-model="scope.row.row"
                :data-handler="openPage"
              >
                查看详情
              </k-btn>
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

export default  {
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
          id: 6,
          value: "数据补录",
        },
        {
          id: 7,
          value: "发送托管行",
        },
        {
          id: 8,
          value: "发起审批",
        },
        {
          id: 9,
          value: "公告发布",
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
          id: 0,
          value: "今日披露",
        },
        {
          id: 1,
          value: "延期披露",
        },
        {
          id: 2,
          value: "明日披露",
        },

      ],
      flowClassification02: [
        {
          id: 4,
          value: "运作公告",
        },
        {
          id: 7,
          value: "重大事项报告",
        },
        {
          id: 8,
          value: "临时公告",
        },
        {
          id: 9,
          value: "净值报告",
        },
        {
          id: 1,
          value: "产品销售文件",
        },
      ],
      operationTypeId: "-1",
      disclosureTypeId: "-1",
      currentTabLabel: '信披待办',
      status:'', //待办状态


      // ----------------  产品待办对象  status  ----------------
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
          id: -1,
          value: "不限",
        },
        {
          id: 1,
          value: "封闭净值",
        },
        {
          id: 2,
          value: "天天净值",
        },
        {
          id: 3,
          value: "定开净值",
        },
        {
          id: 4,
          value: "现金管理类",
        },
      ],
      // ----------------  产品待办对象  end  ----------------
      userid:localStorage.getItem("userid"),

      searchParam:{},
      currentDate:'',//定义当前日期回显使用
    };
  },
  created() {
    this.isActive1 = this.flowStatus[0].id;
    this.isActive2 = this.flowClassification[0].id;
    this.httpUtil.sysDate().then(res => {
      if (res) {
        this.$set(this.searchParam, 'planFbDate', res.toString());
      }
    })
    this.$nextTick(() => {
      this.changeTab(1,0);
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
    renderProcessInstanceDateTime(row) {
      return Tools.formatDateTime(row.processInstanceCreateDate, row.processInstanceCreateTime);
    },
    renderTaskCreateDateTime(row) {
      return Tools.formatDateTime(row.taskCreateDate, row.taskCreateTime);
    },
    //================================================产品待办方法 -- status -- axin=====================================

    //-------------------------------  跳转详情 ------------------------------------------
    prodOpenPage(val){
      let param = {};
      let url = val.url;
      let operateType = val.operationType;
      if (operateType == '1') {  //文档协作编辑

      } else if (operateType == '2') {  //创设提交上会
        param = {prodCode: val.prodCode}
      } else if (operateType == '3') {  //决议结果表决
        param = {topicId: val.dealId,meetingId:val.remark}
      } else if (operateType == '4') {  //生成决议单
        this.httpUtil.comnQuery({
          action:'TopicManagerHis.findTopicInfoHisList',
          params:{topicId: val.dealId},
          successAlert:false
        }).then(data => {
          if (data.rows.length >0) {
            let row = data.rows[0];
            // row.$set(row,'showAllStatus',true);
            // row.$set(row,'btnDisabled',false);
            row.showAllStatus = true;
            row.btnDisabled = false;
            console.log("row",row);

            this.httpUtil.comnQuery({
              action:'T8ProdCreateMeeting.findMeetingInfoById',
              params:{
                id:row.meetingId,
              },
              successAlert:false
            }).then(res=>{
              console.log('会议数据',res);
              let formData= {}
              if (res.success){
                formData = res.returndata.data;
              }
              this.$router.push({
                path: val.url,
                query: {
                  'row':row,
                  'localData':formData
                }
              })
            })
          }
        })
      } else if (operateType == '5') {  //产品申报登记
        param = {prodCode:val.prodCode}
      } else if (operateType == '6') {  //数据补录
        param.prodCode = val.prodCode;
        param.id = val.dealId;
        this.$router.push({
          path: url,     //'/main/pms/bonus/prodBonusRuleRegular',
          query:param
        })
      } else if (operateType == '7') {  //发送托管行

      } else if (operateType == '8') {  //发起审批
        param.prodCode = val.prodCode;
        param.id = val.dealId;
        this.$router.push({
          path: url,     //'/main/pms/bonus/prodBonusRuleRegular',
          query:param
        })

      } else if (operateType == '9') {  //公告发布

      }

      if (operateType != '4') {
        this.$router.push({
          path: val.url,
          query: param
        })
      }

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
    //点击发起清盘
    clickSubmitProcess(row){
      this.$router.push({
        path:"/main/pms/prodLiquidation/prodLiquidation",
        query:{
          prodCode:row.prodCode,
        }
      })
    },
    //-------------------------------  重新加载列表 ------------------------------------------
    changeTabProd(flag, id) {
      if (flag === 'taskStatus') {
        this.taskStatus = id;
      } else if (flag === 'phaseId') {
        this.phaseId = id;
      } else if(flag === 'prodMode') {
        this.prodMode = id;
      }
      let params = {
        taskStatus:this.taskStatus,
      };
      if(this.phaseId !== -1){
        this.$set(params,'phaseId',this.phaseId);
      }
      if(this.prodMode !== -1){
        this.$set(params,'prodMode',this.prodMode);
      }

      this.$refs.prodNoticeGrid.load(params);
    },
    //=============================================上面是产品的方法 --- END -- axin  ============================

    change(name) {
      this.currentTabLabel = name;
      this.$nextTick(()=>{
        if(name=="产品待办"){
          this.$refs.prodNoticeGrid.load();
        }else if (name=="信披待办"){
          this.$refs.disclosureNoticeGrid.load({status:'0'});
        } else {
          // console.log("清盘代办=:>",this.currentTabLabel);
          // this.$refs.disclosureNoticeGrid.load();
        }
      })
    },
    handleParams(operationTypeId, disclosureTypeId) {
      let params = {};
      if (operationTypeId == -1 && disclosureTypeId == -1) {
        params = {};
      } else if (operationTypeId != -1 && disclosureTypeId != -1) {
        params.operationType = operationTypeId;
        params.disclosureType = disclosureTypeId;
      } else if (operationTypeId == -1) {
        params.disclosureType = disclosureTypeId;
      } else if (disclosureTypeId == -1) {
        params.operationType = operationTypeId;
      }
      return params;
    },
    changeTab(flag, id) {
      if (this.searchParam.planFbDate === null ){
        Tools.alert("请选择计划发布日期！","danger");
        return false;
      }
      let params = {};
      let planFbDate ='';
      if (id == 0) {
        this.isActive2 = id;
        this.isActiveMore = false;
        params.disclosureFlag =  id;
      } else if (id == 1) {
        this.isActive2 = id;
        this.isActiveMore02 = false;
        params.disclosureFlag = id;
      } else if(id == 2) {
        this.isActive2 = id;
        this.status = id;
        params.disclosureFlag =  id;
      }
      this.httpUtil.sysDate().then(res => {
        if (res) {
          if (this.searchParam.planFbDate === undefined) {
            planFbDate = res.toString();
          } else {
            planFbDate = this.searchParam.planFbDate;
          }
          params.planFbDate = planFbDate;
          this.$refs.disclosureNoticeGrid.load(params);
        }
      })
    },


    selectRow(row, column, event) {
      this.selectRowData = assign({}, row);
    },
    handleCommand(command) {
      this.isActiveMore = true;
      this.isActive1 = null;
      this.moreName = command.value;
      this.operationTypeId = command.id;
      let params = this.handleParams(this.operationTypeId, this.disclosureTypeId);
      this.$refs.disclosureNoticeGrid.load(params);
    },
    handleCommand02(command) {
      this.isActiveMore02 = true;
      this.isActive2 = null;
      this.moreName02 = command.value;
      this.disclosureTypeId = command.id;
      let params = this.handleParams(this.operationTypeId, this.disclosureTypeId);
      params.status = this.status;
      this.$refs.disclosureNoticeGrid.load(params);
    },
    openPage(value) {
      let pathUrl = '/main/pms/disclosureNotice/DisclosureNotice';
      this.$router.push({
        path: pathUrl,
        query: value,
      });
      console.log("value=:>>>>",value);

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
    margin-right: 20px;
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
