<template>
  <div class="myFlow tab-page">
    <div class="myFlow_block01">
      <div class="myFlow_block01_line1">
        <div>报送情况:</div>
        <div class="tagBlock" :class="{ 'tagBlock-active': isActive1 === item.id }" v-for="item in infoStatus"
          :key="item.id" @click="changeTab(1, item.id)">
          {{item.value}}
        </div>
      </div>
      <div class="myFlow_block01_line1" >
        <span style="color: #F56C6C">*</span>
        <div>数据日期:</div>
        <div style="width: 15%;margin-left: 10px">
          <k-field-date v-model="searchParam.theoryReportStartDate" data-value-format="yyyyMMdd"  :data-default-value="currentDate" v-if="isActive1!=='2'" @data-on-change="changeTab()"></k-field-date>
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" v-if="isActive1==='2'" @data-on-change="changeTab()"></k-field-date>
        </div>
<!--        <div style="width: 15%;margin-left: 10px" v-if="isActive1==='2'">-->
<!--          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" ></k-field-date>-->
<!--        </div>-->
      </div>
    </div>
    <!-- <div class="myFlow_block02"> -->
      <k-grid ref="T8ProdAccountFindInfo" data-action="DeskTopModel.findReportResultInfo"
        @data-row-select="selectRow" :data-autoload="false">
        <!-- :data-operate-column="false" -->
        <k-grid-column data-header="报表大类" data-name="reportType" data-dict="report_type"/>
        <k-grid-column data-header="报表名称" data-name="reportTableName" />
        <k-grid-column data-header="行内报送日" data-name="theoryReportStartDate" />
        <k-grid-column data-header="监管报送截止日" data-name="theoryReportEndDate" />
        <k-grid-column data-header="总数量" data-name="total" />
        <k-grid-column data-header="待报送数量" :data-hidden="isActive1==='3'" data-name="needTotal" />
        <k-grid-column data-header="报送成功数量" :data-hidden="isActive1!=='3'" data-name="reportSuccessNumber" />
        <k-grid-column data-header="整体状态" data-name="status" data-dict="allStatus"/>
        <template slot="operate" slot-scope="scope">
          <k-btn
            class="btn-custom-plain"
            data-descript="查看详情"
            data-functype="POPUP"
            data-size="mini"
            @click="readMessage(scope.row.row)"
          >
            查看详情
          </k-btn>
        </template>
      </k-grid>
    <!-- </div> -->

    <k-popup ref="messageDetailPopup" data-title="详情">
      <k-form ref="messageDetailForm" :data-col="2">
        <k-form-item label="提醒类型">
          <k-field-select v-model="detailData.remindTypeName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="发起人">
          <k-field-text v-model="detailData.originator" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="消息内容" data-col="2">
          <k-field-text v-model="detailData.messageContent" :data-disabled="true" inputType="textarea"/>
        </k-form-item>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import {
    assign
  } from "lodash";
  import Tools from "@/utils/tools";

  export default {
    props: {
      tabAuthorityInfo: Object
    },
    filters: {
      filterAuth: (val) => {
        return val > 99 ? '99+' : val
      }
    },
    watch: {
      // 查询导入日期
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'theoryReportStartDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'theoryReportEndDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
      'searchParam.theoryReportStartDate': {
        handler(v) {
          if (v) {
            if (this.isActive1 == 1) {
              this.$emit("handleUpdateCount", v)
            }
          }
        },
        immediate: true
      }
    },
    data() {
      return {
        BreathDay:[],
        isActive1: null,
        prodCode: "",
        selectRowData: {},
        detailData:{},
        infoStatus: [
          {
            id: "1",
            value: "今日待报送",
          },
          // {
          //   id: "2",
          //   value: "延期待报送",
          // },
          {
            id: "3",
            value: "今日已报送",
          },
          {
            id: "4",
            value: "今日报送截止",
          },
        ],
        userId:localStorage.userid,
        searchParam:{},
        currentDate:'',//定义当前日期回显使用
      };
    },
    created() {
      //获取当前系统用户ID
      this.cuuserid=localStorage.getItem('userid');
      this.isActive1 = this.infoStatus[0].id;
      this.httpUtil.sysDate().then(res => {
        if (res) {
          this.$set(this.searchParam, 'theoryReportStartDate', res.toString());
        }
      })
      this.$nextTick(() => {
        this.changeTab(1,'1');
      });
    },
    activated() {
      this.isActive1 = this.infoStatus[0].id;
      this.$nextTick(() => {
        this.changeTab(1,'1');
      });
    },
    methods: {
      changeTab(flag, id) {
        if (this.searchParam.theoryReportStartDate === null ){
          Tools.alert("请选择数据日期！","danger");
          return false;
        }
        flag == 1 ? (this.isActive1 = id) :'';
        let params = {};
        let theoryReportStartDate ='';
        // params.name = this.isActive1;
        params.remindUserId = this.userId;
        if (this.isActive1 == '1') {  //今日待报送
          params.checkType=this.isActive1;
          this.$emit("changeTab")
        } else if (this.isActive1 == '2') {  //延期待报送
          params.checkType=this.isActive1;
        } else if (this.isActive1 == '3'){  //今日已报送
          params.checkType=this.isActive1;
        } else if (this.isActive1 == '4'){  //今日报送截止
          params.checkType=this.isActive1;
        }
        this.httpUtil.sysDate().then(res => {
          if (res) {
            if (this.searchParam.theoryReportStartDate === undefined) {
            // if (this.searchParam.theoryReportStartDate!==undefined && this.searchParam.theoryReportStartDate!=='' && this.searchParam.theoryReportStartDate!=null) {
              theoryReportStartDate = res.toString();
            } else {
              theoryReportStartDate = this.searchParam.theoryReportStartDate;
            }
            params.theoryReportStartDate = theoryReportStartDate;
            if (this.searchParam.theoryReportEndDate!==undefined && this.searchParam.theoryReportEndDate!=='' && this.searchParam.theoryReportEndDate!=null) {
              params.theoryReportEndDate = this.searchParam.theoryReportEndDate;
            }
            this.$refs.T8ProdAccountFindInfo.load(params);
          }
        })
      },
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row);
        this.detailData = assign({}, row);
      },
      readMessage(value) {
        let pathUrl = '/main/reportSend/task/reportTask';
        this.$router.push({
          path: pathUrl,
          query: value,
        });
      },
      collectMessage(row) {
        this.httpUtil.comnUpdate({
          action:'T8RemindMessage.collectRemindMessage',
          params:row
        }).then(data => {
            let params = {};
          params.remindUserId = localStorage.userid;
          if (this.isActive1 == '1') {
              params.isRead = '0';
            }
          if (this.isActive1 == '2') {
            params.isRead = '1';
            params.isCollect = '0';
          }
          if (this.isActive1 == '3') {
            params.isRead = '1';
            params.isCollect = '1';
          }
          if (this.isActive1 == '4') {
            params.isRead = '1';
            params.isCollect = '1';
          }

          this.$refs.T8ProdAccountFindInfo.load(params);
        })
      },

      cancelCollectMessage(row) {
        this.httpUtil.comnUpdate({
          action:'T8RemindMessage.cancelCollectRemindMessage',
          params:row
        }).then(data => {
          let params = {};
          params.remindUserId = localStorage.userid;
          if (this.isActive1 == '1') {
            params.isRead = '0';
          }
          if (this.isActive1 == '2') {
            params.isRead = '1';
            params.isCollect = '0';
          }
          if (this.isActive1 == '3') {
            params.isRead = '1';
            params.isCollect = '1';
          }
          if (this.isActive1 == '4') {
            params.isRead = '1';
            params.isCollect = '1';
          }

          this.$refs.T8ProdAccountFindInfo.load(params);
        })
      },
      //首页我的消息刷新
      refreshMyDesktop(){
        this.$emit('refreshDesktop', '1')
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
  .myFlow_block01 {
    margin-bottom: 15px;
  }

  .count {
    position: absolute;
    right: 1px;
    top: 1px;
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
