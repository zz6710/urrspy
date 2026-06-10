<<template>
  <div>

      <k-form-search-customize data-target="prodSchedule" v-model="queryParam">

        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode"  data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="查询日期">
          <k-field-date v-model="queryParamDateRange" data-type="daterange" />
        </k-form-item>

        <k-form-item label="产品定位">
          <k-field-select v-model="prodSearchParam.prodPosition"  data-dict="t8_schedule_position"/>
        </k-form-item>

        <k-form-item label="状态">
          <k-field-select v-model="prodSearchParam.prodStatus" data-dict="t8_schedule_status"/>
        </k-form-item>
        <k-form-item label="代码回收">
          <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
        </k-form-item>
<!--        <k-form-item label="当期进度">
          <k-field-select v-model="prodSearchParam.currentProgress" />
        </k-form-item>-->

        <k-btn slot="button" class="md-rose" data-functype="POPUP" :data-handler="initForm" data-target="addSchedule"
               v-if="global.isShowAuthorityButton('T8ProdSchedule.addProdSchedule')">
          <md-icon md-src="/static/svg/add.svg"/>
          新增
        </k-btn>
      </k-form-search-customize>


      <k-grid ref="prodSchedule" @data-row-select="selectRow" data-action="T8ProdSchedule.findProdSchedule1" >
        <k-grid-column data-header="销售月份"        data-name="sellMonth" />
        <k-grid-column data-header="产品代码"        data-name="prodCode" />
        <k-grid-column data-header="产品名称"        data-name="prodName" />
        <k-grid-column data-header="产品状态" 	      data-name="prodStatus" data-dict="t8_schedule_status"/>
        <k-grid-column data-header="当前进度" 	    data-name="currentProgress" data-dict="t8_prod_son_status"/>
        <k-grid-column data-header="产品排期状态" 	      data-name="prodScheduleStatus" data-dict="t8_prod_schedule_status"/>
        <k-grid-column data-header="产品定位" 	    data-name="prodPosition"  data-dict="t8_schedule_position"/>
        <k-grid-column data-header="预约认/申购起始日期" 	data-name="applyStartDate" data-width="120"  data-type="date"/>
        <k-grid-column data-header="预约认/申购结束日期" 	data-name="applyEndDate" data-width="120" data-type="date"/>
        <k-grid-column data-header="成立日/开放日" 	  data-name="establishOpenDate" data-width="120" data-type="date" />
        <k-grid-column data-header="封闭投资起始日期" 	    data-name="closeStartDate"  data-width="120" data-type="date" />
        <k-grid-column data-header="封闭投资结束日期" 	      data-name="closeEndDate"  data-width="120" data-type="date" />
        <k-grid-column data-header="拟定业绩基准" 	  data-name="perfMethodExplain"/>
        <k-grid-column data-header="本期额度(元)" 	    data-type="money"  data-name="currentQuota" data-width="150"/>
        <k-grid-column data-header="产品规模" 	     data-type="money" data-name="currentScale" data-width="150"/>
        <k-grid-column data-header="投资经理" 	  data-name="investManageName" />
        <k-grid-column data-header="产品经理" 	  data-name="prodManageName"/>
        <k-grid-column data-header="销售商代码" 	  data-name="distributorCode" data-hidden="true"/>
        <k-grid-column data-header="销售商名称" 	  data-name="distributorName"/>
        <k-grid-column data-header="产品端发行日期" 	  data-name="issueDate"  data-width="120" data-type="date" />
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdSchedule.confirmSchedule" data-size="mini"
                 data-type="danger" data-target="prodSchedule" :data-disabled="scope.row.row.prodScheduleStatus !== '01'" :data-confirm="true" data-descript="确认"
                 v-if="global.isShowAuthorityButton('T8ProdSchedule.confirmSchedule')">
            <md-icon>done</md-icon>
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="updateSchedule"
                 class="md-info md-just-icon md-simple"
                 data-descript="修改产品排期" v-if="global.isShowAuthorityButton('T8ProdSchedule.updateProdSchedule')"  @click="queryCalendar(scope.row.row)">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdSchedule.delProdSchedule" data-size="mini"
                 data-type="danger" data-target="prodSchedule" :data-confirm="true" data-descript="删除" v-if="global.isShowAuthorityButton('T8ProdSchedule.delProdSchedule')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>

    <k-popup ref="addSchedule" data-title="新增产品排期"  :dataDialogDrag="true">
<!--      <k-form ref="addScheduleFrom" :data-col="2">-->
<!--      <k-form-item label="产品模型">-->
<!--        <k-field-select id="prod_mode" v-model="prodMode"-->
<!--                        data-dict="t8_prod_create_type"  :data-allowblank="false" />-->
<!--      </k-form-item>-->
<!--      </k-form>-->
      <div class="md-layout" style="margin-top:-25px;margin-left:45px;margin-bottom:20px;">
        <div class="md-layout-item"
             style="margin-top: 14px; margin-bottom: 5px; display: grid; width: 100%; align-items: center;">
          <div ref="periodPanel" v-if="prodMode == '3'" v-for="(openItem, openItemIndex) in openRuleItems" v-bind:key="openItemIndex"
               class="fc-button-group" style="float:left; padding: 15px 0px 0px 8px;">
            <!-- 每NXYZ，遇节假日“D” -->
            <!-- N是数字 -->
            <!-- X下拉单选：天、周、月 -->
            <!-- Y是下拉多选：X是“天”就置灰，X是“周”为一到日，X是“月”为1-28 -->
            <span style="padding-top: 7px" >每</span>
            <div class="redStar" style="display: inline-block;"><span>*</span></div>
            <!-- N -->
            <k-field-text v-model="formData.cycleOpenTerm" class="md-padding-left-10" @data-on-change="resetOpenDate"
                          style="width: 65px;" :data-max-length="4"
                          data-digits="0"  data-integer-length="4"
                          data-validate-type="number" data-type="number" :data-allowblank="prodMode != '3'"></k-field-text>
            <div class="redStar" style="display: inline-block;"><span>*</span></div>
            <!-- X -->
            <k-field-select v-model="formData.cycleOpenType" :data-data="openRuleOptions"
                            class="md-padding-left-10"
                            @data-on-change="changedPeriod(openItem)"
                            style="width: 70px;display: inline-block;" :data-allowblank="prodMode != '3'"></k-field-select>
            <!-- Y 最好采用这种选择模式，否则容易出现问题，删除第一行之后会保留第一行的字典-->
            <div class="redStar" v-show="formData.cycleOpenType == '2'"><span>*</span></div>
            <div class="redStar" v-show="formData.cycleOpenType == '3'"><span>*</span></div>
            <k-field-select v-show="formData.cycleOpenType == '2'" v-model="formData.orderOpenDays" :data-data="openWeeklyOptions" @data-on-change="resetOpenDate"
                            class="md-padding-left-10"
                            style="width: 90px;" :data-allowblank="formData.cycleOpenType != '2'"></k-field-select>
            <k-field-select v-show="formData.cycleOpenType == '3'" v-model="formData.orderOpenDays" :data-data="openMonthlyOptions" @data-on-change="resetOpenDate"
                            class="md-padding-left-10"
                            style="width: 90px;" :data-allowblank="formData.cycleOpenType != '3'"></k-field-select>
            <k-field-select v-model="openItem.cycleOpenType" v-show="false" data-disabled="true" :data-data="openTypeOptions"
                            :data-default-value="'1'"  class="md-padding-left-10" style="width: 90px;"></k-field-select>
            <span style="padding-top: 7px" >开放，遇节假日</span>
            <div class="redStar" style="display: inline-block;"><span>*</span></div>
            <k-field-select v-model="formData.postponeRule" :data-data="weekendRuleOptions" :data-default-value="'1'" class="md-padding-left-10"
                            style="width: 160px;" :data-allowblank="prodMode != '3'"></k-field-select>
            <k-field-select v-model="openItem.weekendRule" v-show="false" :data-data="weekendRuleOptions" @data-on-change="resetOpenDate"
                            class="md-padding-left-10"
                            style="width: 160px;" ></k-field-select>
          </div>
        </div>
      </div>
      <k-form ref="addScheduleFrom" :data-col="2">
        <k-form-item label="产品名称">
          <k-field-select v-model="formData.prodCode" :data-allowblank="false" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"  @data-on-change="changeCalendar"/>
        </k-form-item>

        <k-form-item label="产品状态" v-show="false">
          <k-field-select  v-model="formData.prodStatus"
                          data-dict="t8_schedule_status"
                          :data-allowblank="false" :data-default-value="'01'"/>
        </k-form-item>
         <k-form-item label="产品工作日方案">
        <k-field-select v-model="formData.pgmno"
                        data-action="WorkdayProgram.find" data-default-value="002"
                        data-display-field="pgmno,pgmname" data-value-field="pgmno" />
      </k-form-item>

        <k-form-item label="产品定位">
          <k-field-select  v-model="formData.prodPosition"
                           data-dict="t8_schedule_position"
                           :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="预约认/申购起始日期">
          <k-field-date  v-model="formData.applyStartDate" :dataWorkday="true"
                         :dataWorkdayPgmno="this.formData.pgmno" :dataMinValue="today"
                         :dataMaxValue="formData.applyEndDate?formData.applyEndDate:endDay"
                         :data-allowblank="false" />
        </k-form-item>

        <k-form-item label="预约认/申购结束日期">
          <k-field-date  v-model="formData.applyEndDate" :dataWorkday="true"
                         :dataWorkdayPgmno="this.formData.pgmno"
                         :dataMinValue="formData.applyStartDate?formData.applyStartDate:today"
                         :dataMaxValue="formData.establishDate?formData.establishDate:endDay"
                         :data-allowblank="false" />
        </k-form-item>

        <k-form-item label="成立日">
          <k-field-date  v-model="formData.establishDate"
                         :dataMinValue="formData.applyEndDate"
                         :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="首次开放日" v-if="this.prodMode=='1'?false:true">
        <k-field-date v-model="formData.openStartDate" data-date-format="yyyy-MM-dd" :dataWorkday="true"
                      :dataWorkdayPgmno="this.formData.pgmno"
                      :dataMinValue="formData.establishDate?formData.establishDate:today"
                      :dataAllowblank="this.prodMode=='1'?true:false" ></k-field-date>
      </k-form-item>
      <k-form-item label="到期日">
        <k-field-date v-model="formData.endDate" data-date-format="yyyy-MM-dd"
                      :dataWorkdayPgmno="this.formData.pgmno"
                      :data-default-value="prodMode === '1'?'':'20991231'"
                      :data-min-value="formData.establishDate? '('+formData.establishDate : today"
                      :dataMaxValue="formData.liquidate?formData.liquidate:endDay"
                      :dataAllowblank="false"></k-field-date>
      </k-form-item>
       <k-form-item label="申赎确认日" v-if="this.prodMode != '1'">
        <k-field-select v-model="formData.redempDate" :data-allowblank="this.prodMode == '1'"  :data-on-change="getOpenEndDate(formData.endDate,formData.redempDate)" data-dict="t8_prod_tn" :data-default-value="'3'"/>
      </k-form-item>
      <k-form-item label="清盘日" :data-disabled="true">
        <k-field-date v-model="formData.liquidate" data-date-format="yyyy-MM-dd"
                      :dataWorkdayPgmno="this.formData.pgmno"
                      :dataMinValue="formData.endDate?formData.endDate:formData.establishDate"
                      :key="loadDate"></k-field-date>
      </k-form-item>

        <k-form-item label="本期业绩比较基准">
          <k-field-text  v-model="formData.perfMethodExplain"
                         :data-allowblank="true"/>
        </k-form-item>

        <k-form-item label="销售商">
          <k-field-select  v-model="formData.distributorCode"
                           data-action="T8Dict.findTaDistributorInfos" :dataAllowblank='false'
                           data-display-field="distributorName"  data-value-field="distributorCode"  :data-multiple="true"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="addHandler" data-action="T8ProdSchedule.addProdSchedule" data-from="addScheduleFrom"
                 :data-model="formData" data-target="prodSchedule" >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="updateSchedule" data-title="修改产品排期"  :dataDialogDrag="true">
      <div class="md-layout" style="margin-top:-25px;margin-left:45px;margin-bottom:20px;">
        <div class="md-layout-item"
             style="margin-top: 14px; margin-bottom: 5px; display: grid; width: 100%; align-items: center;">
          <div ref="periodPanel" v-if="prodMode == '3'" v-for="(openItem, openItemIndex) in openRuleItems" v-bind:key="openItemIndex"
               class="fc-button-group" style="float:left; padding: 15px 0px 0px 8px;">
            <!-- 每NXYZ，遇节假日“D” -->
            <!-- N是数字 -->
            <!-- X下拉单选：天、周、月 -->
            <!-- Y是下拉多选：X是“天”就置灰，X是“周”为一到日，X是“月”为1-28 -->
            <span style="padding-top: 7px" >每</span>
            <div class="redStar" style="display: inline-block;"><span>*</span></div>
            <!-- N -->
            <k-field-text v-model="formData.cycleOpenTerm" class="md-padding-left-10" @data-on-change="resetOpenDate"
                          style="width: 65px;" :data-max-length="4"
                          data-digits="0"  data-integer-length="4"
                          data-validate-type="number" data-type="number" :data-allowblank="prodMode != '3'"></k-field-text>
            <div class="redStar" style="display: inline-block;"><span>*</span></div>
            <!-- X -->
            <k-field-select v-model="formData.cycleOpenType" :data-data="openRuleOptions"
                            class="md-padding-left-10"
                            @data-on-change="changedPeriod(openItem)"
                            style="width: 70px;display: inline-block;" :data-allowblank="prodMode != '3'"></k-field-select>
            <!-- Y 最好采用这种选择模式，否则容易出现问题，删除第一行之后会保留第一行的字典-->

            <div class="redStar" v-show="formData.cycleOpenType == '2'"><span>*</span></div>
            <div class="redStar" v-show="formData.cycleOpenType == '3'"><span>*</span></div>
            <k-field-select v-show="formData.cycleOpenType == '2'" v-model="formData.orderOpenDays" :data-data="openWeeklyOptions" @data-on-change="resetOpenDate"
                            class="md-padding-left-10"
                            style="width: 90px;" :data-allowblank="formData.cycleOpenType != '2'"></k-field-select>
            <k-field-select v-show="formData.cycleOpenType == '3'" v-model="formData.orderOpenDays" :data-data="openMonthlyOptions" @data-on-change="resetOpenDate"
                            class="md-padding-left-10"
                            style="width: 90px;" :data-allowblank="formData.cycleOpenType != '3'"></k-field-select>
            <k-field-select v-model="openItem.cycleOpenType" v-show="false" data-disabled="true" :data-data="openTypeOptions"
                            :data-default-value="'1'"  class="md-padding-left-10" style="width: 90px;"></k-field-select>
            <span style="padding-top: 7px" >开放，遇节假日</span>
            <div class="redStar" style="display: inline-block;"><span>*</span></div>
            <k-field-select v-model="formData.postponeRule" :data-data="weekendRuleOptions" :data-default-value="'1'" class="md-padding-left-10"
                            style="width: 160px;" :data-allowblank="prodMode != '3'"></k-field-select>
            <k-field-select v-model="openItem.weekendRule" v-show="false" :data-data="weekendRuleOptions" @data-on-change="resetOpenDate"
                            class="md-padding-left-10"
                            style="width: 160px;" ></k-field-select>
          </div>
        </div>
      </div>
      <k-form ref="udpateScheduleFrom" :data-col="2">
        <k-form-item label="产品名称">
          <k-field-select v-model="formData.prodCode" :data-allowblank="false" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="产品状态">
          <k-field-select  v-model="formData.prodStatus"
                           data-dict="t8_schedule_status"
                           :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
      <k-form-item label="产品工作日方案">
        <k-field-select v-model="formData.pgmno"
                        data-action="WorkdayProgram.find" data-default-value="002"
                        data-display-field="pgmno,pgmname" data-value-field="pgmno" />
      </k-form-item>

        <k-form-item label="产品定位">
          <k-field-select  v-model="formData.prodPosition"
                           data-dict="t8_schedule_position"
                           :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="预约认/申购起始日期">
          <k-field-date  v-model="formData.applyStartDate" :dataWorkday="true"
                         :dataWorkdayPgmno="this.formData.pgmno" :dataMinValue="today"
                         :dataMaxValue="formData.applyEndDate?formData.applyEndDate:endDay"
                         :data-allowblank="false" :key="loadDate"/>
        </k-form-item>

        <k-form-item label="预约认/申购结束日期">
          <k-field-date  v-model="formData.applyEndDate" :dataWorkday="true"
                         :dataWorkdayPgmno="this.formData.pgmno"
                         :dataMinValue="formData.applyStartDate?formData.applyStartDate:today"
                         :dataMaxValue="formData.establishDate?formData.establishDate:endDay"
                         :data-allowblank="false" :key="loadDate"/>
        </k-form-item>
        <k-form-item label="成立日">
          <k-field-date  v-model="formData.establishDate"
                         :data-allowblank="false"/>
        </k-form-item>
      <k-form-item label="首次开放日" v-if="this.prodMode=='1'?false:true">
        <k-field-date v-model="formData.openStartDate" data-date-format="yyyy-MM-dd" :dataWorkday="true"
                      :dataWorkdayPgmno="this.formData.pgmno"
                      :dataMinValue="getMinDate()"
                      :dataAllowblank="this.prodMode=='1'?true:false" :key="loadDate" ></k-field-date>
      </k-form-item>
      <k-form-item label="到期日" key="addEndDate">
        <k-field-date v-model="formData.endDate" data-date-format="yyyy-MM-dd"
                      :dataWorkdayPgmno="this.formData.pgmno"
                      :data-min-value="formData.establishDate? '('+formData.establishDate : today"
                      :dataMaxValue="formData.liquidate?formData.liquidate:endDay"
                      :dataAllowblank="false"  ></k-field-date>
      </k-form-item>
       <k-form-item label="申赎确认日" v-if="this.prodMode != '1'">
        <k-field-select v-model="formData.redempDate" :data-allowblank="this.prodMode == '1'"  :data-on-change="getOpenEndDate(formData.endDate,formData.redempDate)" data-dict="t8_prod_tn" :data-default-value="'3'"/>
      </k-form-item>
      <k-form-item label="清盘日" :data-disabled="true">
        <k-field-date v-model="formData.liquidate" data-date-format="yyyy-MM-dd"
                      :dataWorkdayPgmno="this.formData.pgmno"
                      :dataMinValue="formData.endDate?formData.endDate:formData.establishDate"
                      :key="loadDate"></k-field-date>
      </k-form-item>

        <k-form-item label="本期业绩比较基准">
          <k-field-text  v-model="formData.perfMethodExplain"
                         :data-allowblank="true"/>
        </k-form-item>

        <k-form-item label="销售商">
          <k-field-select  v-model="formData.distributorCode"
                           data-action="T8Dict.findTaDistributorInfos" :dataAllowblank='false'
                           data-display-field="distributorName"  data-value-field="distributorCode"  :data-multiple="true"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSchedule.updateProdSchedule" data-from="udpateScheduleFrom"
                 :data-model="formData" data-target="prodSchedule">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>
<script>
  import kayak from '@/frame/kayak.js';

  import { assign } from "lodash";
  import moment from "moment";
  import Tools from "@/utils/tools";
  import eventBus from "@/utils/eventBus";

  export default {
    name:"M84015",
    data() {
      return {
        endDay:'29991231',
        today: moment().format('YYYYMMDD'),
        currentYear:moment().format('YYYY'),
        months: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        MONTHS: {},
        monthlyDays: [],      // 用于存在月视图
        lstWorkday:[],        // workday只加载一年的
        lstDayIndexs: {},     // 日期索引， 主要记录日期的位置
        nextPeriodItem: {}, // 当前设置日期
        loadDate:new Date().getTime(),
        prodCode: "",
        prodMode:"1", //产品模式模式为1
        prodSearchParam:{

        },
        queryParamDateRange:[],
        formData: {
          cycleOpenTerm:'',// 每几个
          cycleOpenType:'',//开放频率类型 1-天，2-周，3-月
          orderOpenDays:'',//开放时间 哪一天/周几  开放

          openPeriodDays:'',//开放时长 开放时长
          postponeRule:'',//顺延规则
        },
        T8ProdCalendar1: {
          cycleOpenTerm:'',// 每几个
          cycleOpenType:'',//开放频率类型 1-天，2-周，3-月
          orderOpenDays:'',//开放时间 哪一天/周几  开放

          openPeriodDays:'',//开放时长 开放时长
          postponeRule:'',//顺延规则
        },
        selectRowData: {},
        openRuleItems:[
          { // 周期型产品规则
            number: 2,          // 周期值
            openRule: '1',      // 开放周期规则： 1-天，2-周，3-月
            weekendRule: '1',   // 非工作日处理规则：1-顺延到下一工作日，2-提前到上一工作日， 3-节假日取消
            openDate: '',       // 开放日，如果开放规则是周，则为周一...周天，如果开放规则是月，则为1号...28号，如果开放规则是日，则该值无效
            openType: '',       // 开放类型： 1-开放、 2-申购、 3-赎回
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
        prodModeItems:{ // 产品形态(1：封闭净值 2：开放净值 3：货币净值 4：周期净值)
          "1": [ //封闭净值
            {suffix:"subsBeginDate",  name: '认购开始',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: '', maxDate:'subsEndDate,establishDate,endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: true},
            {suffix:"subsEndDate",    name: '认购结束',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: 'subsBeginDate', maxDate:'establishDate,endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: true},
            {suffix:"establishDate",  name: '成立日',     selected: false, itemClass: 'date-alive-item2', allowWeekend: false, minDate: 'subsEndDate,subsBeginDate', maxDate:'endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: false},
            {suffix:"endDate",        name: '到期日',     selected: false, itemClass: 'date-alive-item6', allowWeekend: false, minDate: 'establishDate,subsEndDate,subsBeginDate', maxDate:'windingDate,payDate', includeMinDate: false, includeMaxDate: true},
            {suffix:"windingDate",    name: '清盘日',     selected: false, itemClass: 'date-alive-item7', allowWeekend: false, minDate: 'endDate,establishDate,subsEndDate,subsBeginDate', maxDate:'payDate', includeMinDate: true, includeMaxDate: true},
          ],
          "2": [ // 开放净值
            {suffix:"subsBeginDate",  name: '认购开始',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: '', maxDate:'subsEndDate, establishDate,openBeginDate,openEndDate,endDate,windingDate,payDate',       includeMinDate: true, includeMaxDate: true},
            {suffix:"subsEndDate",    name: '认购结束',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: 'subsBeginDate', maxDate:'establishDate,openBeginDate,openEndDate,,endDate,windingDate,payDate',      includeMinDate: true, includeMaxDate: true},
            {suffix:"establishDate",  name: '成立日',     selected: false, itemClass: 'date-alive-item2', allowWeekend: false, minDate: 'subsEndDate,subsBeginDate', maxDate:',openBeginDate,openEndDate,endDate,windingDate,payDate',        includeMinDate: true, includeMaxDate: false},
            {suffix:"openBeginDate",  name: '开放开始',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'establishDate,subsEndDate,subsBeginDate', maxDate:'openEndDate,endDate,windingDate,payDate',         includeMinDate: false, includeMaxDate: true},
            {suffix:"openEndDate",    name: '开放结束',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'openBeginDate,establishDate,subsEndDate,subsBeginDate,today', maxDate:'endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: false},
            {suffix:"endDate",        name: '到期日',     selected: false, itemClass: 'date-alive-item6', allowWeekend: false, minDate: 'openEndDate,openBeginDate,establishDate,subsEndDate,subsBeginDate', maxDate:'windingDate,payDate',   includeMinDate: false, includeMaxDate: true},
            {suffix:"windingDate",    name: '清盘日',     selected: false, itemClass: 'date-alive-item7', allowWeekend: false, minDate: 'endDate,openEndDate,openBeginDate,establishDate,subsEndDate,subsBeginDate', maxDate:'payDate',       includeMinDate: true, includeMaxDate: true},
          ],
          "3": [ // 货币净值
            {suffix:"subsBeginDate",  name: '认购开始',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: '', maxDate:'subsEndDate, establishDate,valueDate,openBeginDate,openEndDate,endDate,windingDate,payDate',       includeMinDate: true, includeMaxDate: true},
            {suffix:"subsEndDate",    name: '认购结束',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: 'subsBeginDate', maxDate:'establishDate,valueDate,openBeginDate,openEndDate,,endDate,windingDate,payDate',      includeMinDate: true, includeMaxDate: true},
            {suffix:"establishDate",  name: '成立日',     selected: false, itemClass: 'date-alive-item2', allowWeekend: false, minDate: 'subsEndDate,subsBeginDate', maxDate:'valueDate,openBeginDate,openEndDate,endDate,windingDate,payDate',         includeMinDate: true, includeMaxDate: true},
            {suffix:"valueDate",      name: '起息日',     selected: false, itemClass: 'date-alive-item8', allowWeekend: false, minDate: 'establishDate,subsEndDate,subsBeginDate', maxDate:'openBeginDate,openEndDate,endDate,windingDate,payDate',     includeMinDate: true, includeMaxDate: false},
            {suffix:"openBeginDate",  name: '开放开始',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'valueDate,establishDate,subsEndDate,subsBeginDate', maxDate:'openEndDate,endDate,windingDate,payDate',         includeMinDate: false, includeMaxDate: true},
            {suffix:"openEndDate",    name: '开放结束',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'openBeginDate,valueDate,establishDate,subsEndDate,subsBeginDate,today', maxDate:'endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: false},
            {suffix:"endDate",        name: '到期日',     selected: false, itemClass: 'date-alive-item6', allowWeekend: false, minDate: 'openEndDate,openBeginDate,valueDate,establishDate,subsEndDate,subsBeginDate', maxDate:'windingDate,payDate',   includeMinDate: false, includeMaxDate: true},
            {suffix:"windingDate",    name: '清盘日',     selected: false, itemClass: 'date-alive-item7', allowWeekend: false, minDate: 'endDate,openEndDate,openBeginDate,valueDate,establishDate,subsEndDate,subsBeginDate', maxDate:'payDate',       includeMinDate: true, includeMaxDate: true},
          ],
          "4": [ // 周期净值
            {suffix:"subsBeginDate",  name: '认购开始',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: '', maxDate:'subsEndDate, establishDate,openBeginDate,openEndDate,endDate,windingDate,payDate',       includeMinDate: true, includeMaxDate: true},
            {suffix:"subsEndDate",    name: '认购结束',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: 'subsBeginDate', maxDate:'establishDate,openBeginDate,openEndDate,,endDate,windingDate,payDate',      includeMinDate: true, includeMaxDate: true},
            {suffix:"establishDate",  name: '成立日',     selected: false, itemClass: 'date-alive-item2', allowWeekend: false, minDate: 'subsEndDate,subsBeginDate', maxDate:',openBeginDate,openEndDate,endDate,windingDate,payDate',        includeMinDate: true, includeMaxDate: false},
            {suffix:"openBeginDate",  name: '开放开始',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'establishDate,subsEndDate,subsBeginDate', maxDate:'openEndDate,endDate,windingDate,payDate',         includeMinDate: false, includeMaxDate: true},
            {suffix:"openEndDate",    name: '开放结束',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'openBeginDate,establishDate,subsEndDate,subsBeginDate,today', maxDate:'endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: false},
            {suffix:"endDate",        name: '到期日',     selected: false, itemClass: 'date-alive-item6', allowWeekend: false, minDate: 'openEndDate,openBeginDate,establishDate,subsEndDate,subsBeginDate', maxDate:'windingDate,payDate',   includeMinDate: false, includeMaxDate: true},
            {suffix:"windingDate",    name: '清盘日',     selected: false, itemClass: 'date-alive-item7', allowWeekend: false, minDate: 'endDate,openEndDate,openBeginDate,establishDate,subsEndDate,subsBeginDate', maxDate:'payDate',       includeMinDate: true, includeMaxDate: true},
          ]

        },
        rang:{
          subsBeginDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          subsEndDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false ,disabled:false},
          establishDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          valueDate:{min:moment().format('YYYYMMDD'),max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          openBeginDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          openEndDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          endDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          windingDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
        },
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
      };
    },

    computed: {
      queryParam() {
        return {
          'prodStatus': this.prodSearchParam.prodStatus,
          'prodCode': this.prodSearchParam.prodCode,
          'prodName': this.prodSearchParam.prodName,
          'isRecycleCode': this.prodSearchParam.isRecycleCode,
          'prodPosition': this.prodSearchParam.prodPosition,
          'queryStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'queryEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        }
      }
    },
    created() {

      let prodCode = this.$route.query.prodCode;
      //console.log("prodCode=:>>",prodCode);
      if(prodCode!==null && prodCode!=="" && prodCode!==undefined){
        //初始化数据
        this.$nextTick(()=>{
          this.$refs.prodSchedule.load({prodCode : prodCode});
        })
      }
    },
    methods: {
      initialCalendar(){
        // this.loadDate = new Date().getTime();
        this.queryProdProgram();
      },
      initData(){
        this.lstPeriodDate  = {}; // 设置日期参数，例如{"subsBeginDate": "20200202"}
        this.lstSettedDate = {};// 设置日期事件，例如{"20200202": [{name:认购日}]}
        this.calendarInfo=[];
        this.MONTHS = {};
      },
      queryProdProgram(){
        this.lstWorkday = [];
        this.httpUtil.comnQuery({
          action: 'WorkdayItem.find',
          params: {pgmno: this.formData.pgmno}
        }).then(data => {
          let rows_ = data.rows;
          // 先将工作日存入到lstWorkday的数组中，方便后期再判断
          this.lstWorkday = rows_.map(function(item){
            return item.workday;
          });
          this.createYearCalendar();
          this.loadPeriodInfo();
          this.changeRang();
        });
      },
      /**
       * 通过传过来的清盘日加载年视图，今年
       */
      createYearCalendar(){
        let endYear = this.windingDate;
        if(!this.windingDate|| this.windingDate === ''||endYear.substring(0,4)<this.currentYear ){
          endYear = this.currentYear;
        }

        if(this.lstWorkday.length > 0){
          let workYear = moment(this.lstWorkday[this.lstWorkday.length - 1]).format('YYYY') ;
          endYear = endYear < workYear ? workYear : endYear;
        }

        for(let year = this.currentYear; year <=  endYear.substring(0,4); year = moment(year).add(1, 'years').format('YYYY')) {
          if(!this.MONTHS[year]) {
            this.MONTHS[year] = {};
            this.months.forEach(month => {
              let currentYearMonth = moment(year + '-' + month).format('YYYY-MM');
              this.createMonthlyCalendar(currentYearMonth, month);
              this.MONTHS[year][month] = this.monthlyDays;
            });
          }
        }

        //设置工作日循环
        this.loadCalendar = true;
        for(let year in this.MONTHS){
          this.months.forEach(month =>{ // 年，循环每月
            this.MONTHS[year][month].forEach(week => { // 月，循环每周
              week.forEach(date => { // 周， 循环每日
                date.isWorkday = this.lstWorkday.indexOf(date.wholeDay) >= 0;
              });
            });
          });
        }
        setTimeout(() => {
          this.loadCalendar = false;
        }, 500);
      },
      /**
       * 创建月视图
       * @param currentYearMonth
       */
      createMonthlyCalendar(currentYearMonth, month) {
        let currentYear = currentYearMonth.substring(0,4);
        this.monthlyDays = [];
        // 获取当月的一号是星期几 以便来生成上月的日期 填补够42个格子
        const monthFirstDay = moment(currentYearMonth + '-01', 'YYYY-MM-DD')
        // 获得一号与第一个格子内应该有的天数距离 这里需要注意的是 weekday 是从周日 为 0 开始的
        let firstDayWeekday = moment(monthFirstDay).weekday()
        if (firstDayWeekday === 0) {
          firstDayWeekday = 7
        }
        let daysDistance = 1 - firstDayWeekday
        for (let weeks = 0; weeks < 6; weeks++) {
          this.monthlyDays.push([])
          for (let weekday = 0; weekday < 7; weekday++) {
            // 该对象有两个属性 一个是class属性 还有一个就是日期
            let date = {}
            date.day = moment(monthFirstDay).add(daysDistance, 'days')
            date.isAlive = '';
            date.wholeDay = moment(date.day).format('YYYYMMDD')
            const dayMonth = moment(date.day).month()
            // 是这个月的日期
            if (dayMonth === moment(monthFirstDay).month()) {
              date.class = 'current-month '
              date.dayID = 'ID-' + date.wholeDay
              // 设置日期索引
              if (this.lstDayIndexs[currentYear] == null){
                this.lstDayIndexs[currentYear] = {};
              }
              let dayIndex = {"month": month, "weeks": weeks, "weekday": weekday};
              this.lstDayIndexs[currentYear][date.wholeDay] = dayIndex;
              // 日期是今天的高亮
              if (moment(date.day).isSame(this.today, 'days')) {
                date.class += 'today '
              } else {
                date.disable = true;
              }
            } else {
              date.class = 'not-current-month '
            }
            let dayIdx = date.day.day();
            if (dayIdx == 0 || dayIdx == 6){ // 设置周六、周天
              date.class += 'weekend '
            }
            date.day = moment(date.day).format('D')
            this.monthlyDays[weeks].push(date);
            daysDistance++
          }
        }
      },
      loadPeriodInfo(){
        if (this.prodCode != null && this.prodCode != ''){
          // 判断产品代码非空，如果非空，则查询产品日历表
          // 查询产品周期信息
          this.httpUtil.comnQuery({
            action: 'T8ProdPeriod.findT8ProdPeriods',
            params: {prodCode: this.prodCode}
          }).then(data => {
            let rows_ = data.rows;
            if(rows_[0]&&rows_[0].isequalDate == 1 ){
              if(this.establishDateFunction){
                this.establishDateFunction();
                this.establishDateFunction = null;
              }
              if(this.endDateFunction){
                this.endDateFunction();
                this.endDateFunction = null;
              }
              if(this.valueDateFunction){
                this.valueDateFunction();
                this.valueDateFunction = null;
              }
            }
            rows_.forEach(periodBo => {
              this.lstPeriodDate = periodBo;
              this.$nextTick(()=>{
                if(this.lstPeriodDate["openBeginDate"] && this.lstPeriodDate["openEndDate"]){
                  this.cancelPeriodDate( this.lstPeriodDate["openBeginDate"], this.lstPeriodDate["openEndDate"], "openDate");
                }
                // if(this.lstPeriodDate["subsBeginDate"] && this.lstPeriodDate["subsEndDate"]){
                //   this.cancelPeriodDate(this.lstPeriodDate["subsBeginDate"], this.lstPeriodDate["subsEndDate"], "subsDate");
                // }
                this.loadCalendarInfo();
              })
            });
          });
        }
      },
      /**
       *控制日历控件边界值和是否可以修改
       */
      changeRang(){
        for(let item of this.prodModeItems[this.prodMode]) {//这是外循环，控制谁需要改范围。也是该产品有什么周期
          let minDate = this.rang[item.suffix].min;//获取默认值或者上一次的值
          let maxDate = this.rang[item.suffix].max;//获取默认值或者上一次的值
          let wdLength =  this.lstWorkday.length;
          if(wdLength >0){
            maxDate = this.lstWorkday[wdLength - 1];
          }
          let minDateArray = item.minDate.split(",");//获取应该比对的边界值  小于
          let maxDateArray = item.maxDate.split(",");//获取应该比对的边界值  大于
          let includeMin = item.includeMinDate;//获取是否可以与边界值相等
          let includeMax = item.includeMaxDate;//获取是否可以与边界值相等
          for(let key of minDateArray){
            if(this.lstPeriodDate.isequalDate && this.lstPeriodDate.isequalDate == 1 &&( key == "endDate" || key == "establishDate"|| key == "valueDate")){//隐藏时不作为边界
              continue;
            }
            if(this.lstPeriodDate[key]&&this.lstPeriodDate[key] > moment().format('YYYYMMDD') ){//查询边界值最小值
              minDate = this.lstPeriodDate[key];
              break;
            }
          }
          for(let key of maxDateArray){//查询边界值最大值
            if(this.lstPeriodDate.isequalDate && this.lstPeriodDate.isequalDate == 1 &&( key == "endDate" || key == "establishDate"|| key == "valueDate" )){//隐藏时不作为边界
              continue;
            }
            if(this.lstPeriodDate[key]){
              maxDate = this.lstPeriodDate[key];
              break;
            }
          }
          if(this.lstPeriodDate[item.suffix] && this.lstPeriodDate[item.suffix].length>7 &&this.lstPeriodDate[item.suffix] < moment().format('YYYYMMDD')){//小于今天不让修改日历
            this.rang[item.suffix].disabled = true;
          }
          this.rang[item.suffix].min = includeMin == false && minDate.length == 8  ?"(" + minDate: minDate ;
          this.rang[item.suffix].max = includeMax == false && maxDate.length == 8  ? maxDate + ")" : maxDate ;
          //是否展示开放日日历 star
          let showCalFlag = true;
          for(let item of this.prodModeItems[this.prodMode]){
            if(!this.lstPeriodDate[item.suffix]){
              showCalFlag = false;
              break;
            }
          }
          if(this.prodMode == '1'){
            showCalFlag = false;
          }
          this.showCalendarFlag = showCalFlag;
          //是否展示开放日日历 end
        }
        //是否展示成立日与到期日
        if(this.lstPeriodDate.isequalDate && this.lstPeriodDate.isequalDate == 1 ){
          this.rang.openBeginDate.showStatus=false;
          this.rang.openEndDate.showStatus = false;
          this.rang.valueDate.showStatus = false;
          this.rang.establishDate.max = this.rang.openBeginDate.max;
          this.rang.endDate.min = this.rang.openEndDate.min;
        }
      },
      addHandler(val) {//添加产品排期前的处理
        if(this.prodMode == '3'){
          if(val.cycleOpenTerm==null || val.cycleOpenTerm==""){
            Tools.alert("请选择周期开发形式,及顺延规则!","danger")
            return false;
          }
          if(val.cycleOpenType==null||val.cycleOpenType==""){
            Tools.alert("请选择周期开发形式,及顺延规则!","danger")
            return false;
          }
          if(val.cycleOpenType == '2'||val.cycleOpenType == '3'){
            if(val.orderOpenDays==null||val.orderOpenDays==""){
              Tools.alert("请选择周期开发形式,及顺延规则!","danger")
              return false;
            }
          }

          /*if(this.T8ProdCalendar.openPeriodDays==null||this.T8ProdCalendar.openPeriodDays==""){
            Tools.alert("请检选择周期开发形式,及顺延规则!","danger")
            return false;
          }*/
          if(val.postponeRule==null||val.postponeRule==""){
            Tools.alert("请选择周期开发形式,及顺延规则!","danger")
            return false;
          }
        }

      },
      getMinDate(){
        if(this.formData.applyEndDate==null && this.formData.establishDate==null){
          return moment().format('YYYYMMDD');
        }else if(this.formData.establishDate!=null){
          return this.formData.establishDate;
        }else {
          return this.formData.applyEndDate;
        }
      },
      getOpenEndDate(endndDate,redempDate){
        //modify by zhangchangsi 20211130 修改产品开放结束日算法
        if (endndDate) {
          var openEndDate =  this.timePushForward(Tools.formatDate(endndDate), redempDate);
          //对日期进行格式化
          openEndDate = Tools.dateFormat("YYYYmmdd",new Date(openEndDate));
          this.$set(this.formData,'openEndDate',openEndDate);
        }
      },
      timePushForward(selectTime, days){
        let myDate = new Date(selectTime); //传入的日期
        myDate.setDate(myDate.getDate() - days);
        return myDate.toLocaleDateString('fr-CA');
      },
      changedPeriod(item){
        item.openDateOptions.splice(0,item.openDateOptions.length);

        this.$set(this.formData,'orderOpenDays','');
        let openRule = item.openRule;
        switch (openRule) {
          case '1':
            item.disabled = true;
            item.openDate = '';
            //     this.orderOpenDays='';
            break;
          case '2':
            item.disabled = false;
            item.openDate = '';
            this.openWeeklyOptions.map(option=>{
              item.openDateOptions.push(option);
            });
            //this.orderOpenDays=item.openDateOptions.get(0);
            break;
          case '3':
            item.disabled = false;
            item.openDate = '';
            this.openMonthlyOptions.map(option=>{
              item.openDateOptions.push(option);
            });
            //this.orderOpenDays=item.openDateOptions.get(0);
            break;
          default:
            break;
        }
      },
      resetOpenDate(){
        if(this.lstPeriodDate){
          let openBeginDate = this.lstPeriodDate['openBeginDate'];  // 设置的开放开始日
          let openEndDate = this.lstPeriodDate['openEndDate'];      // 设置的开放结束日
          if(openBeginDate && openEndDate){
            this.cancelPeriodDate(openBeginDate, openEndDate, "openDate");
            this.$nextTick(()=> { this.setOpenDate(null, openBeginDate, openEndDate);})
          }
        }
      },
      // 删除只有一个日期的，例如成立日、清盘日，支付日
      /**
       * @param date 日期
       * @param periodSuffix 周期识别码，例如：subsBeginDate, openBeginDate,
       * @param dateSuffix  日期识别码，例如：subsDate, openDate
       */
      cancelOnceDate(date, dateSuffix, periodSuffix){
        // 删除该日已设置的值
        if (periodSuffix != null && periodSuffix != ''){
          if (periodSuffix == 'establishDate') {
            this.formData.establishDate = '';
          } else if (periodSuffix == 'endDate'){
            this.formData.endDate = '';
          } else if (periodSuffix == 'valueDate'){
            this.formData.valueDate = '';
          }
          this.lstPeriodDate[periodSuffix] = null;
        }
        let targetDate = date.wholeDay;
        // 从日期中的日历事件中删除该事件，且事件减1
        for(let calendarIndex in this.lstSettedDate[targetDate]){
          if (this.lstSettedDate[targetDate][calendarIndex].dateSuffix == dateSuffix){
            this.lstSettedDate[targetDate].splice(calendarIndex, 1);
            this.lstSettedDate[targetDate].calendarNumber--;
          }
        }
        // 删除CalendarBo
        this.cancelCalendarBo(targetDate, dateSuffix);
        this.removeCalendarStyle(targetDate, date.calendarStyle);
        //this.addCalendarStyle(targetDate,this.getClassName(targetDate,""));
        if (this.lstSettedDate[targetDate]&&this.lstSettedDate[targetDate].length == 0){
          // 如果没有事件了，则设置为空
          date.calendarStyle = '';
          date.top = '';
        }
        for(let item of this.prodModeItems[this.prodMode]){
          if (item.suffix == periodSuffix){
            item.selected = false;
            date.isAlive = '';
            date.disable = false; // 此处设置为可以选择，说明关键日期已经更改
            break;
          }
        }
      },
      // 删除周期，包括认购日和开放日（申购日/赎回日）
      cancelPeriodDate(startDate, endDate, periodSuffix){
        let days = 1;
        while(true){
          let periodDate = moment(startDate).add(days, 'days').format('YYYYMMDD');
          if (periodDate >= endDate){
            break;
          }
          days++;
          if (periodDate < this.today){
            continue;
          }
          if (this.lstWorkday.indexOf(periodDate) > 0
            && this.lstSettedDate[periodDate] != null
            && this.lstSettedDate[periodDate].length > 0){
            for(let settedDate of this.lstSettedDate[periodDate]){
              if (settedDate.periodSuffix == periodSuffix){
                let periodYear = periodDate.substring(0,4);
                let dayIndex = this.lstDayIndexs[periodYear][periodDate];
                let cancelDate = this.MONTHS[periodYear][dayIndex.month][dayIndex.weeks][dayIndex.weekday];
                this.cancelOnceDate(cancelDate, periodSuffix);
              }
            }
          }
        }
      },
      initForm() {
        this.formData = assign({}, {})
      },
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      },
    queryCalendar(val){
      /*获取产品形态*/
      this.httpUtil.comnQuery({
        action: 'T8ProdInfo.getProdNameByProdCode',
        params: {
          prodCode : val.prodCode ,
        },
        successAlert: false,
      }).then(data => {
        if (data.rows) {
          //设置产品形态
          this.prodMode = data.rows[0].prodMode;
        }
      });

       this.httpUtil.comnQuery({
        action: 'T8ProdCalendar.findT8ProdCalendars',
        params: {
          prodCode : val.prodCode ,
        },
        successAlert: false,
      }).then(data => {
        if(data.rows.length > 0 ){
          let row = data.rows[0];
          this.$set(this.formData,'cycleOpenTerm',row.cycleOpenTerm);
          this.$set(this.formData,'cycleOpenType',row.cycleOpenType);
          this.$set(this.formData,'orderOpenDays',row.orderOpenDays);
          this.$set(this.formData,'postponeRule',row.postponeRule);
          this.$set(this.formData,'pgmno',row.pgmno);
          this.$set(this.formData,'establishDate',row.establishDate);
          this.$set(this.formData,'openStartDate',row.openStartDate);
          this.$set(this.formData,'openEndDate',row.openEndDate);
          this.$set(this.formData,'endDate',row.endDate);
          this.$set(this.formData,'liquidate',row.liquidate);
          this.$set(this.formData,'redempDate',row.redempDate);
          this.$set(this.formData,'distributorCode',val.distributorCode);

        }
      });
    },
     changeCalendar(){
        /*获取产品形态*/
       this.httpUtil.comnQuery({
         action: 'T8ProdInfo.getProdNameByProdCode',
         params: {
           prodCode : this.formData.prodCode ,
         },
         successAlert: false,
       }).then(data => {
         if (data.rows) {
           //设置产品形态
           this.prodMode = data.rows[0].prodMode;

         }
       });

    },

    },
    watch: {
      "formData.pgmno": function(){
        this.httpUtil.sysDate().then(res=>{
          if (res) {
            this.today = res;
            let strDate = res.slice(0, 4) + "-" + res.slice(4, 6) + "-" + res.slice(6, 8);
            this.loadDate = new Date(strDate).getTime();
          }
          this.initData ();
          this.initialCalendar();
        });
        //console.log("watch pgmno value is: " + this.T8ProdCalendar.pgmno);

      }
    }
  }

</script>
<style lang="scss">
@import "../../../components/k-element/k-from/k-form-item.scss";
.redStar{
  width: 20px;
  display: inline-block;
  justify-content: center;
  align-items: center;
  padding-top: 10px;
  padding-right: 6px;
  color: red;
}
</style>
