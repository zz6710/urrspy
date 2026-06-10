<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="quartzInfoGrid">
        <k-form-item label="任务名称">
          <k-field-text v-model="searchParam.jobName"/>
        </k-form-item>
        <k-form-item label="任务分组">
          <k-field-text v-model="searchParam.jobGroup"/>
        </k-form-item>
        <k-form-item label="任务状态">
          <k-field-select v-model="searchParam.status" data-dict="t8_quartzInfo_status"></k-field-select>
        </k-form-item>
        <k-form-item label="任务运行规则">
          <k-field-select v-model="searchParam.quartzRule" data-dict="t8_quartzInfo_rule"></k-field-select>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" slot="button"
               data-target="addQuartzInfoPopup" v-if="global.isShowAuthorityButton('QuartzInfo.addQuartzInfo')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
        <k-btn class="btn-custom-primary" data-functype="PAGE" data-size="mini" slot="button" style="width: 120px;"
               @click="popupEdit()" >
          <md-icon md-src="/static/svg/add.svg" />任务执行记录
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="quartzInfoGrid" @data-row-select="selectRow" data-action="QuartzInfo.findQuartzInfos" data-fixed="right">
        <k-grid-column data-align="center" data-header="任务ID" data-name="id" data-width="80"></k-grid-column>
        <k-grid-column data-align="center" data-header="任务名称" data-name="jobName" data-width="200"></k-grid-column>
        <k-grid-column data-align="center" data-header="任务类路径" data-name="jobClasspath"></k-grid-column>
        <k-grid-column data-align="center" data-header="任务分组" data-name="jobGroup" data-width="160"></k-grid-column>
        <k-grid-column data-align="center" data-header="任务状态" data-name="status" data-dict="t8_quartzInfo_status"></k-grid-column>
        <k-grid-column data-align="center" data-header="任务运行规则" data-name="quartzRule" data-dict="t8_quartzInfo_rule" data-width="110"></k-grid-column>
        <k-grid-column data-align="center" data-header="任务运行表达式" data-name="cronExpression" data-width="120"></k-grid-column>
        <k-grid-column data-align="center" data-header="任务描述" data-name="description" data-width="110"></k-grid-column>
        <k-grid-column data-align="center" data-header="前置任务ID" data-name="preTaskId"></k-grid-column>
        <k-grid-column data-align="center" data-header="执行ip" data-name="execIp"></k-grid-column>


        <template slot="operate" slot-scope="scope">
           <k-btn class="md-success"  data-functype="SUBMIT" data-size="mini" data-action="QuartzInfo.updateStatusOnEnable"
                   data-target="quartzInfoGrid"  :data-confirm="true" v-if="scope.row.row.status == '1'">
             启动
           </k-btn>
           <k-btn class="md-rose" data-functype="SUBMIT" data-action="QuartzInfo.updateStatusOnStop" v-if="scope.row.row.status == '0'"
                  :data-confirm="true" data-size="mini" data-type="danger" data-target="quartzInfoGrid" >
             停用
           </k-btn>
           <k-btn class="btn-custom-plain" data-descript="修改任务调度配置表" data-functype="POPUP" data-size="mini"
                  :data-handler="selectRow" data-target="editQuartzInfoPopup" v-if="global.isShowAuthorityButton('QuartzInfo.updateQuartzInfo')">
             修改
           </k-btn>
           <k-btn class="md-danger" data-functype="SUBMIT" data-action="QuartzInfo.deleteQuartzInfo" :data-disabled="scope.row.row.status=='1'"
                  :data-confirm="true" data-size="mini" data-type="danger" data-target="quartzInfoGrid" data-descript="删除任务调度配置表">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加任务调度配置表弹出框   -->
    <k-popup ref="addQuartzInfoPopup" data-title="新增">
      <k-form ref="addQuartzInfoForm" :data-col="2">
        <k-form-item label="任务分组">
          <k-field-text v-model="formData.jobGroup" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="任务名称">
          <k-field-text v-model="formData.jobName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="任务描述">
          <k-field-text v-model="formData.description"/>
        </k-form-item>
        <k-form-item label="任务类路径" >
          <k-field-text v-model="formData.jobClasspath" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="任务运行规则" >
          <k-field-select v-model="formData.quartzRule" data-dict="t8_quartzInfo_rule" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="前置任务ID">
          <k-field-text v-model="formData.preTaskId"/>
        </k-form-item>
         <k-form-item label="执行ip">
          <k-field-text v-model="formData.execIp" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="时间">
          <k-field-text v-model="formData.taskTime" :data-allowblank="taskTimeBlank" :data-disabled="taskTimeDisabled"/>
        </k-form-item>

        <k-form-item label="时间间隔（秒）">
          <k-field-text v-model="formData.freqSecond" :data-allowblank="freqSecondBlank"
                        :data-disabled="freqSecondDisabled"/>
        </k-form-item>

        <k-form-item label="时间间隔（分）">
          <k-field-text v-model="formData.freqMinute" :data-allowblank="freqMinuteBlank"
                        :data-disabled="freqMinuteDisabled"/>
        </k-form-item>

        <k-form-item label="时间间隔（时）">
          <k-field-text v-model="formData.freqHour" :data-allowblank="freqHourBlank" :data-disabled="freqHourDisabled"/>
        </k-form-item>

        <k-form-item label="日期">
          <k-field-select v-model="formData.day" data-dict="t8_quartzInfo_days" :data-allowblank="dayBlank"
                          :data-disabled="dayDisabled"/>
        </k-form-item>

        <k-form-item label="星期">
          <k-field-select v-model="formData.week" data-dict="t8_quartzInfo_week" :data-allowblank="weekBlank"
                          :data-disabled="weekDisabled"/>
        </k-form-item>

        <k-form-item label="季度">
          <k-field-select v-model="formData.quarter" :data-data="quarterData" data-display-field="label"
                          data-value-field="value" :data-allowblank="quarterBlank" :data-disabled="quarterDisabled"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="QuartzInfo.addQuartzInfo"
                 data-from="addQuartzInfoForm"
                 :data-model="formData" data-target="quartzInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改任务调度配置表弹出框   -->
    <k-popup ref="editQuartzInfoPopup" data-title="修改">
      <k-form ref="editQuartzInfoForm" :data-col="2">
        <k-form-item label="任务分组">
          <k-field-text v-model="formData.jobGroup" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="任务名称">
          <k-field-text v-model="formData.jobName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="任务描述">
          <k-field-text v-model="formData.description"/>
        </k-form-item>
        <k-form-item label="任务类路径">
          <k-field-text v-model="formData.jobClasspath"  :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="任务运行规则" >
          <k-field-select v-model="formData.quartzRule" data-dict="t8_quartzInfo_rule" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="前置任务ID">
          <k-field-text v-model="formData.preTaskId"/>
        </k-form-item>
       <k-form-item label="执行ip">
          <k-field-text v-model="formData.execIp" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="时间">
          <k-field-text v-model="formData.taskTime" :data-allowblank="taskTimeBlank" :data-disabled="taskTimeDisabled"/>
        </k-form-item>

        <k-form-item label="时间间隔（秒）">
          <k-field-text v-model="formData.freqSecond" :data-allowblank="freqSecondBlank"
                        :data-disabled="freqSecondDisabled"/>
        </k-form-item>

        <k-form-item label="时间间隔（分）">
          <k-field-text v-model="formData.freqMinute" :data-allowblank="freqMinuteBlank"
                        :data-disabled="freqMinuteDisabled"/>
        </k-form-item>

        <k-form-item label="时间间隔（时）">
          <k-field-text v-model="formData.freqHour" :data-allowblank="freqHourBlank" :data-disabled="freqHourDisabled"/>
        </k-form-item>

        <k-form-item label="日期">
          <k-field-select v-model="formData.day" data-dict="t8_quartzInfo_days" :data-allowblank="dayBlank"
                          :data-disabled="dayDisabled"/>
        </k-form-item>

        <k-form-item label="星期">
          <k-field-select v-model="formData.week" data-dict="t8_quartzInfo_week" :data-allowblank="weekBlank"
                          :data-disabled="weekDisabled"/>
        </k-form-item>

        <k-form-item label="季度">
          <k-field-select v-model="formData.quarter" :data-data="quarterData" data-display-field="label"
                          data-value-field="value" :data-allowblank="quarterBlank" :data-disabled="quarterDisabled"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="QuartzInfo.updateQuartzInfo"
                 data-from="editQuartzInfoForm"
                 :data-model="formData" data-target="quartzInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
export default {
  name:"QuartzInfoList",
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam: {},
      quarterData: [{value: '2', label: '一季度'}, {value: '6', label: '二季度'}, {value: '9', label: '三季度'}, {
        value: '12',
        label: '四季度'
      }],
      taskTimeDisabled: false,
      taskTimeBlank: false,
      freqSecondDisabled: true,
      freqSecondBlank: true,
      freqMinuteDisabled: true,
      freqMinuteBlank: true,
      freqHourDisabled: true,
      freqHourBlank: true,
      dayDisabled: true,
      dayBlank: true,
      weekDisabled: true,
      weekBlank: true,
      quarterDisabled: true,
      quarterBlank: true,
    };
  },
  watch: {
    'formData.quartzRule'(value) {
      if (value == '0') {//按每月
        this.taskTimeDisabled = false;
        this.taskTimeBlank = false;
        this.dayDisabled = false;
        this.dayBlank = false;
        this.weekDisabled = true;
        this.weekBlank = true;
        this.quarterDisabled = true;
        this.quarterBlank = true;
        this.hiddenFreq();
      } else if (value == '1') {//按每周
        this.taskTimeDisabled = false;
        this.taskTimeBlank = false;
        this.dayDisabled = true;
        this.dayBlank = true;
        this.weekDisabled = false;
        this.weekBlank = false;
        this.quarterDisabled = true;
        this.quarterBlank = true;
        this.hiddenFreq();
      } else if (value == '2') {//按每天
        this.taskTimeDisabled = false;
        this.taskTimeBlank = false;
        this.dayDisabled = true;
        this.dayBlank = true;
        this.weekDisabled = true;
        this.weekBlank = true;
        this.quarterDisabled = true;
        this.quarterBlank = true;
        this.hiddenFreq();
      } else if (value == '3') {//按季度
        this.taskTimeDisabled = false;
        this.taskTimeBlank = false;
        this.dayDisabled = true;
        this.dayBlank = true;
        this.weekDisabled = true;
        this.weekBlank = true;
        this.quarterDisabled = false;
        this.quarterBlank = false;
        this.hiddenFreq();
      } else if (value == '4') {//按频率(秒)
        this.hiddenNoFreq();
        this.freqSecondDisabled = false;
        this.freqSecondBlank = false;
        this.freqMinuteDisabled = true;
        this.freqMinuteBlank = true;
        this.freqHourDisabled = true;
        this.freqHourBlank = true;
      } else if (value == '5') {//按频率(分)
        this.hiddenNoFreq();
        this.freqSecondDisabled = true;
        this.freqSecondBlank = true;
        this.freqMinuteDisabled = false;
        this.freqMinuteBlank = false;
        this.freqHourDisabled = true;
        this.freqHourBlank = true;
      } else if (value == '6') {//按频率(小时)
        this.hiddenNoFreq();
        this.freqSecondDisabled = true;
        this.freqSecondBlank = true;
        this.freqMinuteDisabled = true;
        this.freqMinuteBlank = true;
        this.freqHourDisabled = false;
        this.freqHourBlank = false;
      }
    }
  },
  methods: {
    popupEdit(row){
      let pathUrl = '/main/pms/basePublish/t8OdsTbSet/T8OdsTaskLog';
      this.$router.push({
        path: pathUrl,
        query: {},
      });
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    //时间、日期、星期、季度设置为不可编辑、可为空
    hiddenNoFreq() {
      this.taskTimeDisabled = true;
      this.taskTimeBlank = true;
      this.dayDisabled = true;
      this.dayBlank = true;
      this.weekDisabled = true;
      this.weekBlank = true;
      this.quarterDisabled = true;
      this.quarterBlank = true;
    },
    //间隔字段隐藏
    hiddenFreq() {
      this.freqSecondDisabled = true;
      this.freqSecondBlank = true;
      this.freqMinuteDisabled = true;
      this.freqMinuteBlank = true;
      this.freqHourDisabled = true;
      this.freqHourBlank = true;
    }
  }
};
</script>
