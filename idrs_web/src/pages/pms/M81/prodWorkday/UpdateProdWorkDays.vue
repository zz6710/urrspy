<template>
  <div v-loading="false">

    <!--    <div class="pc-main-container">-->
    <!--      <div class="pc-calendar">-->
    <div style="margin-top: -35px;
                    width: 100%;
                    justify-content: center;
                    display: flex;
                    align-items: center;
                    color: #ff003b;
                    font-size: 13px;"> {{validateResult}}</div>

    <k-field-text v-show="false" v-model="T8ProdCalendar.establishDate"></k-field-text>
    <k-field-text v-show="false" v-model="T8ProdCalendar.endDate"></k-field-text>
    <k-field-text v-show="false" v-model="T8ProdCalendar.prodCode"></k-field-text>


    <div class="md-layout">
      <div class="md-layout-item"
           style="margin-top: 14px; margin-bottom: 5px; display: grid; width: 100%; align-items: center;">

        <div ref="periodPanel" v-show="T8ProdCalendar.prodMode == '3'" v-for="(openItem, openItemIndex) in openRuleItems" v-bind:key="openItemIndex"
             class="fc-button-group" style="float:left; padding: 15px 0px 0px 8px;">

          <!-- 每NXYZ，遇节假日“D” -->
          <!-- N是数字 -->
          <!-- X下拉单选：天、周、月 -->
          <!-- Y是下拉多选：X是“天”就置灰，X是“周”为一到日，X是“月”为1-28 -->
          <span style="padding-top: 7px" >每</span>
          <!-- N -->
          <k-field-text v-model="openItem.number" class="md-padding-left-10"
                        style="width: 65px;"></k-field-text>

          <!-- X -->
          <k-field-select v-model="openItem.openRule" :data-data="openRuleOptions"
                          class="md-padding-left-10"
                          @data-on-change="changedPeriod(openItem)"
                          style="width: 70px;"></k-field-select>

          <!-- Y 最好采用这种选择模式，否则容易出现问题，删除第一行之后会保留第一行的字典-->
          <k-field-select v-show="openItem.openRule == '1'" v-model="openItem.openDate"
                          :data-disabled="openItem.disabled" class="md-padding-left-10"
                          style="width: 90px;"></k-field-select>
          <k-field-select v-show="openItem.openRule == '2'" v-model="openItem.openDate" :data-data="openWeeklyOptions"
                          class="md-padding-left-10"
                          style="width: 90px;"></k-field-select>
          <k-field-select v-show="openItem.openRule == '3'" v-model="openItem.openDate" :data-data="openMonthlyOptions"
                          class="md-padding-left-10"
                          style="width: 90px;"></k-field-select>


          <k-field-select v-model="openItem.cycleType" data-disabled="true" :data-data="openTypeOptions"
                          :data-default-value="'1'"  class="md-padding-left-10" style="width: 90px;"></k-field-select>

          <k-field-text v-model="openItem.periodNumber" class="md-padding-left-10"
                        style="width: 65px;"></k-field-text>
          <span style="padding-top: 7px" >天，遇节假日</span>

          <k-field-select v-model="T8ProdCalendar.weekendRule" :data-data="weekendRuleOptions" :data-default-value="'1'" class="md-padding-left-10"
                          style="width: 160px;" ></k-field-select>
          <k-field-select v-model="openItem.weekendRule" v-show="false" :data-data="weekendRuleOptions"
                          class="md-padding-left-10"
                          style="width: 160px;" ></k-field-select>


        </div>

      </div>
    </div>
    <k-form  refs="date" :data-col="2"  dataLabelWidth="170px" dataInputWidth="300px" >
      <!--      <k-form-item label="产品代码"   v-show="false" >-->
      <k-form-item label="产品代码"   >
        <k-field-text v-model="T8ProdCalendar.prodCode"  :data-disabled="true"
        />
      </k-form-item>
      <k-form-item label="产品工作日方案">
        <k-field-select v-model="T8ProdCalendar.pgmno"  :data-default-value="'00000'"
                        data-action="WorkdayProgram.find" :data-disabled="true"
                        data-display-field="pgmno,pgmname" data-value-field="pgmno" />
      </k-form-item>

      <k-form-item label="认购起始日" :data-disabled="true">
        <k-field-date v-model="T8ProdCalendar.applyStartDate" data-date-format="yyyy-MM-dd" :dataDisabled="true"></k-field-date>
      </k-form-item>
      <k-form-item label="认购结束日" :data-disabled="true">
        <k-field-date v-model="T8ProdCalendar.applyEndDate" data-date-format="yyyy-MM-dd" :dataDisabled="true" ></k-field-date>
      </k-form-item>
      <k-form-item label="成立日" :data-disabled="true">
        <k-field-date v-model="T8ProdCalendar.establishDate" data-date-format="yyyy-MM-dd" :dataDisabled="true"></k-field-date>
      </k-form-item>
      <k-form-item label="开放开始日" >
        <k-field-date v-model="T8ProdCalendar.openStartDate" data-date-format="yyyy-MM-dd" :dataDisabled="false"></k-field-date>
      </k-form-item>
      <k-form-item label="开放结束日" >
        <k-field-date v-model="T8ProdCalendar.openEndDate" data-date-format="yyyy-MM-dd" :dataWorkday="true" :dataDisabled="false"
                      :dataWorkdayPgmno="this.T8ProdCalendar.pgmno" :dataMinValue="T8ProdCalendar.openStartDate"  :dataMaxValue="T8ProdCalendar.endDate"
                      :dataAllowblank="false" :key="loadDate" ></k-field-date>
      </k-form-item>
      <k-form-item label="到期日" :data-disabled="true">
        <k-field-date v-model="T8ProdCalendar.endDate" data-date-format="yyyy-MM-dd" :dataDisabled="true" ></k-field-date>
      </k-form-item>
      <k-form-item label="清盘日" :data-disabled="true">
        <k-field-date v-model="T8ProdCalendar.liquidate" data-date-format="yyyy-MM-dd" :dataDisabled="true"></k-field-date>
      </k-form-item>
      <k-form-item label="调整原因">
        <k-field-text v-model="T8ProdCalendar.adjustReason" :data-allowblank="false" />
      </k-form-item>


      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdCalendar.updateT8ProdWorkdayCalendar"
               :data-handler="beforeSubmit" :data-model="T8ProdCalendar" data-target="prodWorkdayProgramGrid" >
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>

    </k-form>






  </div>
</template>

<script>

  import moment from 'moment';
  import Tools from "@/utils/tools";

  export default {
    computed: {},
    model: {
      prop: 'T8ProdCalendar',
      event: 'input'
    },
    props:{
      T8ProdCalendar: {
      },

    },
    data() {
      return {
        validateResult: "",
        loadDate:new Date().getTime(),
        openRuleItems:[
          { // 周期型产品规则
            number: '',          // 周期值
            openRule: '',      // 开放周期规则： 1-天，2-周，3-月
            weekendRule: '',   // 非工作日处理规则：1-顺延到下一工作日，2-提前到上一工作日， 3-节假日取消
            openDate: '',       // 开放日，如果开放规则是周，则为周一...周天，如果开放规则是月，则为1号...28号，如果开放规则是日，则该值无效
            openType: '1',       // 开放类型： 1-开放、 2-申购、 3-赎回
            periodNumber:'',
            openDateOptions:[],
            disabled: true,
          }
        ],

        weekendRuleOptions: [{
          value: '1',
          label: '顺延到下一工作日'
        }, {
          value: '2',
          label: '提前到上一工作日'
        }, {
          value: '3',
          label: '节假日取消'
        }],
        openWeeklyOptions: [
          {value: '1', label: '周一'},
          {value: '2', label: '周二'},
          {value: '3', label: '周三'},
          {value: '4', label: '周四'},
          {value: '5', label: '周五'},
          {value: '6', label: '周六'},
          {value: '0', label: '周日'},

        ],
        openMonthlyOptions: [
          {value: '1',  label: '1号' },
          {value: '2',  label: '2号' },
          {value: '3',  label: '3号' },
          {value: '4',  label: '4号' },
          {value: '5',  label: '5号' },
          {value: '6',  label: '6号' },
          {value: '7',  label: '7号' },
          {value: '8',  label: '8号' },
          {value: '9',  label: '9号' },
          {value: '10', label: '10号'},
          {value: '11', label: '11号'},
          {value: '12', label: '12号'},
          {value: '13', label: '13号'},
          {value: '14', label: '14号'},
          {value: '15', label: '15号'},
          {value: '16', label: '16号'},
          {value: '17', label: '17号'},
          {value: '18', label: '18号'},
          {value: '19', label: '19号'},
          {value: '20', label: '20号'},
          {value: '21', label: '21号'},
          {value: '22', label: '22号'},
          {value: '23', label: '23号'},
          {value: '24', label: '24号'},
          {value: '25', label: '25号'},
          {value: '26', label: '26号'},
          {value: '27', label: '27号'},
          {value: '28', label: '28号'},
        ],
        openRuleOptions: [{
          value: '1',
          label: '天'
        }, {
          value: '2',
          label: '周'
        }, {
          value: '3',
          label: '月'
        }],
        openTypeOptions: [{
          value: '1',
          label: '开放'
        }, {
          value: '2',
          label: '申购'
        }, {
          value: '3',
          label: '赎回'
        }],

        weeklyItems:[
          {name: '一', itemClass: "thead-th"},
          {name: '二', itemClass: "thead-th"},
          {name: '三', itemClass: "thead-th"},
          {name: '四', itemClass: "thead-th"},
          {name: '五', itemClass: "thead-th"},
          {name: '六', itemClass: "thead-th weekend"},
          {name: '日', itemClass: "thead-th weekend"},
        ],


        legendItems:[         // 用于显示周期信息中的legend
          {name: '认购', legendClass: "pic-cycle-item1"},
          {name: '成立', legendClass: "pic-cycle-item2"},
          {name: '起息', legendClass: "pic-cycle-item8"},
          {name: '申购', legendClass: "pic-cycle-item3"},
          {name: '赎回', legendClass: "pic-cycle-item4"},
          {name: '开放', legendClass: "pic-cycle-item5"},
          {name: '到期', legendClass: "pic-cycle-item6"},
          {name: '清盘', legendClass: "pic-cycle-item7"},
          // {name: '兑付', legendClass: "pic-cycle-item8"},
        ],

        rang:{
          applyStartDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          applyEndDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false ,disabled:false},
          establishDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          valueDate:{min:moment().format('YYYYMMDD'),max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          openStartDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          openEndDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          endDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          liquidate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
        },


      }

    },

    methods: {

      beforeSubmit : function(value){
        value.cycleOpenTerm = this.openRuleItems[0].number;
        value.cycleOpenType = this.openRuleItems[0].openRule;
        value.orderOpenDays = this.openRuleItems[0].openDate;
        value.openPeriodDays = this.openRuleItems[0].periodNumber;
        value.postponeRule = this.openRuleItems[0].weekendRule;
        value.t8ProdInfoId = value.id;
          // this.$set(this.T8ProdCalendar,"cycleOpenTerm",this.openRuleItems[0].number);
          // this.$set(this.T8ProdCalendar,"cycleOpenType",this.openRuleItems[0].openRule);
          // this.$set(this.T8ProdCalendar,"orderOpenDays",this.openRuleItems[0].openDate);
          // this.$set(this.T8ProdCalendar,"openPeriodDays",this.openRuleItems[0].periodNumber);
          // this.$set(this.T8ProdCalendar,"postponeRule",this.openRuleItems[0].weekendRule);
          // this.T8ProdCalendar.t8ProdInfoId = this.T8ProdCalendar.id;
        return value;
      },




      changedPeriod(item){
        item.openDateOptions.splice(0,item.openDateOptions.length);
        let openRule = item.openRule;
        switch (openRule) {
          case '1':
            item.disabled = true;
            item.openDate = '';
            break;
          case '2':
            item.disabled = false;
            item.openDate = '';
            this.openWeeklyOptions.map(option=>{
              item.openDateOptions.push(option);
            });
            break;
          case '3':
            item.disabled = false;
            item.openDate = '';
            this.openMonthlyOptions.map(option=>{
              item.openDateOptions.push(option);
            });
            break;
          default:
            break;
        }

      },



    },


    created(){

    },

    mounted(){
      this.httpUtil.comnQuery({
        action: 'T8ProdCalendar.findT8ProdCalendars',
        params: {
          prodCode : this.T8ProdCalendar.prodCode ,
        },
        successAlert: false,
      }).then(data => {
        if(data.rows.length > 0 ){
          let row = data.rows[0];

          row.number            =row.cycleOpenTerm;
          row.openRule          =row.cycleOpenType;
          row.openDate          =row.orderOpenDays;
          row.weekendRule       =row.postponeRule;
          row.periodNumber      =row.openPeriodDays;
          row.cycleType     ='1';
          this.openRuleItems.length=0;
          this.openRuleItems.push(row);
          this.changedPeriod(row.openRule);
        }
      });


    },

    watch: {

    },

  }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001-ProdCalendar.scss";

</style>
