<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="t8ProdNetValueTaskGrid">
        <k-form-item label="披露日期">
          <k-field-date v-model="queryParamDateRange" data-type="daterange"/>
        </k-form-item>
        <k-form-item label="公告状态">
          <k-field-select v-model="searchParam.status" data-dict="xp_disclosure_task_status"></k-field-select>
        </k-form-item>
        <k-btn class="btn-custom-primary" slot="button" :data-handler="generateTask" style="width: 90px;" v-if="global.isShowAuthorityButton('DisclosureProdTask.addTaskRightControl')">
          <md-icon md-src="/static/svg/add.svg" />
          生成任务
        </k-btn>
        <k-btn class="btn-custom-primary" slot="button" :data-handler="generateReportData" style="width: 110px;" v-if="global.isShowAuthorityButton('DisclosureProdTask.batchGenerateData')">
          <md-icon md-src="/static/svg/add.svg" />
          <span v-show="showSubmitBtn">
            生成报告数据</span>
          <i v-show="!showSubmitBtn" class="el-icon-loading"/>
        </k-btn>
        <k-btn class="md-success" slot="button" :data-handler="updateReportData" style="width: 110px;" v-if="global.isShowAuthorityButton('DisclosureProdTask.batchGenerateData')">
          <i class="icon-reset" />
          <span v-show="showSubmitBtn1">
          报告数据更新</span>
          <i v-show="!showSubmitBtn1" class="el-icon-loading"/>
        </k-btn>
        <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="t8ProdNetValueTaskGrid"
               :data-export-name="'净值披露任务'">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8ProdNetValueTaskGrid" @data-row-select="selectRow" :data-checkbox="true" data-checkbox-id="prodBaseDate" data-operate-width="200px"
              @init="(grid)=>{this.$kgrid = grid}" :data-reserve-selection="false" data-tree-id="prodBaseDate"
              data-action="T8ProdNetValueTask.findT8ProdNetValueTasks" :data-operate-column="true" data-fixed="right">
        <k-grid-column data-header="任务id" data-name="id" :data-sortable="true" data-default-sort="DESC" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="披露日期" data-name="prodBaseDate" data-type="date" ></k-grid-column>
        <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告文件名称" data-name="noticeTitle"  data-width="250px"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品数量" data-name="count" ></k-grid-column>
<!--        <k-grid-column data-align="left" data-header="模板名称" data-name="modDocName" ></k-grid-column>
        <k-grid-column data-align="left" data-header="版本" data-name="modVersionNumber"  ></k-grid-column>-->
        <k-grid-column data-align="left" data-header="公告状态" data-name="status" data-dict="xp_disclosure_task_status" ></k-grid-column>
        <k-grid-column data-align="left" data-header="任务来源" data-name="dataSource" data-dict="xp_disclosure_task_source"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="信披状态" data-name="disclosureStatus" data-dict="xp_disclosure_notice_status" data-width="150" data-hidden="true" ></k-grid-column>-->

        <k-grid-column data-align="left" data-header="产品形态" data-name="prodForm" data-dict="xp_prod_form" data-hidden="true" data-export="true"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="销售对象" data-name="prodObj"  data-dict="xp_target_customer" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="募集方式" data-name="prodClcMth" data-dict="xp_raise_type" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="产品投资性质" data-name="prodInvTyp" data-dict="xp_prod_invest_nature" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="产品系列" data-name="prodSerNm" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="投资周期长度" data-name="invPrdLen" data-hidden="true" data-export="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="投资周期维度" data-name="invPrdDime" data-dict="xp_cycle_dimension" data-hidden="true" data-export="true"></k-grid-column>-->


        <k-grid-column data-align="left" data-header="创建日期" data-name="crtDate" data-type="date" ></k-grid-column>
        <k-grid-column data-align="left" data-header="创建人" data-name="crtUserName" ></k-grid-column>

        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="新增产品净值" data-functype="POPUP" data-size="mini"
                 @click="popupAdd(scope.row.row)"
          v-if="global.isShowAuthorityButton('T8ProdWorth.addT8ProdWorth')">
            数据新增
          </k-btn>
<!--          <k-btn class="md-info md-just-icon md-simple" data-functype="SUBMIT"-->
<!--                 :data-disabled="scope.row.row.taskStatus=='1'"-->
<!--                 data-action="T8ProdNetValueTask.confirmNetValueTask" data-size="mini"-->
<!--                 :data-confirm="true" data-descript="发起审批" data-target="t8ProdNetValueTaskGrid">-->
<!--            <md-icon>near_me</md-icon>-->
<!--          </k-btn>-->
<!--          <k-btn class="md-info md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8ProdNetValueTask.generateSendEmails" data-size="mini"
                 data-target="t8ProdNetValueTaskGrid" data-descript="发送邮件">
            <md-icon>close</md-icon>
          </k-btn>-->
<!--          <k-btn class="btn-custom-plain" :data-download-name="scope.row.row.taskDate+'净值公告.xls'"  data-descript="导出净值文件" data-size="mini"-->
<!--                 @click="confirmAfterSuccess(scope.row.row)" v-model="scope.row.row">-->
<!--            导出-->
<!--          </k-btn>-->
          <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="t8ProdWorthGrid"
                 :data-export-name="'净值文件'" v-show="false">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>

          <k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="产品净值信息">
            净值信息
          </k-btn>

        </template>
      </k-grid>
    </div>
    <k-popup ref="generateTaskPopup" :data-dialog-drag="true">
      <k-form ref="generateForm" :data-col="2">
        <k-form-item label="信披类型">
          <k-field-select v-model="generateData.disclosureType"  data-default-value="9"  data-dict="xp_doc_type"
                          :dataAllowblank="false" :data-disabled="true"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="generateData.disclosureType==='9'">
          <k-field-select v-model="generateData.disclosureSonType" :data-allowblank="false" data-default-value="0903"
                          :data-disabled="true" data-dict="xp_son_type"
                           key="disclosureSonType"/>
        </k-form-item>
<!--        <k-form-item label="计划生成日区间" v-if="generateData.disclosureType=='13'">-->
<!--          <k-field-date v-model="queryParamDateRanger" data-type="daterange" data-date-format="yyyyMMdd"-->
<!--                        data-value-format="yyyyMMdd" :dataAllowblank="generateData.disclosureType!='13'"/>-->
<!--        </k-form-item>-->
        <k-form-item label="基准日期" >
          <k-field-date v-model="generateData.prodBaseDate"
                        :dataAllowblank="false"></k-field-date>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureProdTask.generateTaskManual"
                 data-from="generateForm"
                 :data-model="generateData" data-target="disclosureProdTaskGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    添加净值披露数据弹出框   -->
    <k-popup ref="addNetNoticePopup" data-title="新增">
      <k-form ref="addNetNoticeForm" :data-col="2">
        <net-value-notice-ope-add :formData="formData"></net-value-notice-ope-add>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" ref="subbtn" data-functype="SUBMIT" :data-handler="addNetVal"
                 data-from="addNetNoticeForm" :data-after-success="refreshGrid"
                 :data-model="formData" data-target="t8ProdNoticeGrid">
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
import {assign} from "lodash";
import NetValueNoticeOpeAdd from "@/pages/pms/netValue/task/NetValueNoticeOpeAdd";
import T8ProdWorth from "@/pages/pms/prodWroth/T8ProdWorth";
import Tools from "@/utils/tools";

export default {
  name:"T8ProdNetValueTask",
  components: {NetValueNoticeOpeAdd,T8ProdWorth},
  data() {
    return {
      $kgrid: null,
      formData: {
        addDocTypeDict:'',
      },
      DocTypeDict: {},
      generateData: {
        startMonth: '',
        endMonth: '',
        startDate: '',
        endDate: '',
        disclosureType: '',
        disclosureSonType: '',
      },
      selectRowData: {},
      searchParam: {},
      queryParamDateRange: [],
      taskStatus: '',//任务状态
      netvalDate:'',
      prodCode:'',
      disclosureDate:'',
      showSubmitBtn: true,
      showSubmitBtn1: true,
      routerAdd: true,
    };
  },
  created() {
    this.xpType();
    this.disclosureTypeChange();
  },
  watch: {
    'generateData.disclosureType'(){
      this.disclosureTypeChange();
    },
    queryParamDateRange() {
      console.log("this.queryParamDateRange=:>",this.queryParamDateRange);
      if(this.queryParamDateRange!=null&&this.queryParamDateRange!=''&&this.queryParamDateRange!=undefined){
        this.$set(this.searchParam, 'startDate', this.queryParamDateRange[0]);
        this.$set(this.searchParam, 'endDate', this.queryParamDateRange[1]);
      }else{
        this.$set(this.searchParam, 'startDate', null);
        this.$set(this.searchParam, 'endDate', null);
      }
    }
  },
  // created() {
  //   //接收路由中的参数
  //   this.$nextTick(()=>{
  //     //this.$refs.t8ProdNetValueTaskGrid.load({id: this.$route.query.id})
  //   });
  // },
  methods: {
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPType",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    //信披类型发生改变
    disclosureTypeChange(value) {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: value}
      }).then(data => {
        this.formData.addDocTypeDict = data.rows;
      }).catch({})
    },
    //点击生成任务按钮
    generateTask() {
      this.$set(this.formData, 'addDocTypeDict', '');
      this.$set(this.generateData, 'disclosureSonType', '');
      this.$set(this.generateData, 'disclosureType', '');
      this.xpType();
      this.$refs.generateTaskPopup.popup();
    },
    //生成报告数据
    generateReportData() {
      //表格被选中的记录
      const _this = this
      const selectedRec = _this.$kgrid.getSelected();
      if (selectedRec.length === 0) {
        Tools.alert('请勾选需要生成报告数据的任务！', 'danger');
        return;
      }
      this.showSubmitBtn = false;
      for(let i=0; i<selectedRec.length; i++){//当没有选中时不会进入
        let pass = true;
        let disclosureType = "";
        let disclosureSonType = "";
        let prodBaseDate = "";
        let noticeTitle = "";
        if(selectedRec[i].status === '2'){//已生成公告的任务
          pass = false;
          disclosureType = selectedRec[i].disclosureType//信披类型
          disclosureSonType = selectedRec[i].disclosureSonType//信披类型
          prodBaseDate = selectedRec[i].prodBaseDate//基准日期
        }
        if(!pass){
          Tools.alert(" 披露日 ["+prodBaseDate+"] 的公告已生成，请勿重复操作",'danger');
          _this.$kgrid.setSelected([]);
          this.showSubmitBtn = true;
          return false;
        }
      }
      //提交到后台
      //如果复选项不为空，生成勾选的报告数据
      if (selectedRec.length > 0) {
        this.httpUtil.comnUpdate({
          action: "DisclosureProdTask.batchGenerateData",
          params: {list: JSON.stringify(selectedRec),update: false}
        }).then(data => {
          this.showSubmitBtn = true;
        })
      }
    },
    //报告数据更新
    updateReportData() {
      //表格被选中的记录
      const _this = this
      const selectedRec = _this.$kgrid.getSelected();
      if (selectedRec.length === 0) {
        Tools.alert('请勾选需要更新报告数据的任务！', 'danger');
        return;
      }
      this.showSubmitBtn1 = false;
      for(let i=0; i<selectedRec.length; i++){//当没有选中时不会进入
        let pass = true;
        let disclosureType = "";
        let disclosureSonType = "";
        let prodBaseDate = "";
        if(selectedRec[i].status === '1'){//未生成公告的任务
          pass = false;
          disclosureType = selectedRec[i].disclosureType//信披类型
          disclosureSonType = selectedRec[i].disclosureSonType//信披类型
          prodBaseDate = selectedRec[i].prodBaseDate//基准日期
        }
        if(!pass){
          Tools.alert("披露日 ["+prodBaseDate+"] 的公告还未生成，公告生成后可支持数据更新",'danger');
          _this.$kgrid.setSelected([]);
          this.showSubmitBtn1 = true;
          return false;
        }
        if(selectedRec[i].disclosureStatus==='8'){//已成功发布的公告
          pass = false;
          disclosureType = selectedRec[i].disclosureType//信披类型
          disclosureSonType = selectedRec[i].disclosureSonType//信披类型
          prodBaseDate = selectedRec[i].prodBaseDate//基准日期
        }
        if(!pass){
          Tools.alert("披露日 ["+prodBaseDate+"] 的公告已成功发布，不支持数据更新，公告状态变更后可支持数据更新",'danger');
          _this.$kgrid.setSelected([]);
          this.showSubmitBtn1 = true;
          return false;
        }
      }
      //提交到后台
      //如果复选项不为空，更新勾选的报告数据
      if (selectedRec.length > 0) {
        this.httpUtil.comnUpdate({
          action: "DisclosureProdTask.batchGenerateData",
          params: {list: JSON.stringify(selectedRec),update: true}
        }).then(data => {
          this.showSubmitBtn1 = true;
        })
      }
    },








    popupEdit(row){
      let pathUrl = '/main/pms/prodWroth/T8ProdWorth';
      sessionStorage.setItem('prodCodes', row.prodCodes)
      //当期任务需要发布净值的产品集，','号分隔；
      this.$router.push({
        path: pathUrl,
        query: {
          prodBaseDate: row.prodBaseDate
        },
      });
    },
    popupAdd(){
      let pathUrl = '/main/pms/prodWroth/T8ProdWorth';
      //当期任务需要发布净值的产品集，','号分隔；
      this.$router.push({
        path: pathUrl,
        query: {
          routerAdd: this.routerAdd,
        },
      });
    },
    renderDateTimeUpdate(row) {
      return Tools.formatDateTime(row.confirmDate, row.confirmTime);
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    addNetVal(params){
      //console.log("params=:>>>>",params);
      let count = 0;
      this.httpUtil.comnQuery({
        action: "T8ProdNetValueNotice.findT8ProdNetValueNoticeList",
        params: {"prodCode":params.prodCode,"netvalDate":params.netvalDate},
        successAlert:false,
      }).then(data => {
        if(data.rows!=null&&data.rows!=undefined){
          //console.log("data.rows=:>>>>",data.rows);
          count = data.rows.length;
          if(count>0){
            this.$refs.subbtn.loading=false;
            Tools.alert("该产品相同日期净值数据已存在","danger");
          }else{
            this.httpUtil.comnUpdate({
              action: 'T8ProdNetValueNotice.addT8ProdNetValueNotice',
              params: params,
              successAlert: true,
            }).then(data => {
              this.$refs.subbtn.loading=false;
              this.$refs.addNetNoticePopup.close();
            });
          }
        }
      })
    },
    refreshGrid(){
      this.$refs.t8ProdNetValueTaskGrid.load();
    },
    confirmAfterSuccess(row){
      //console.log("taskDate=:>>>>>",row.taskDate);
      let fileName = row.taskDate+"净值公告.xls";
      this.httpUtil.download({
        url: "/download/server/PmsApp/netValue/downloadNetValue.json",
        params: row,
        callback: response => {
          //console.log(response)
          this.httpUtil.comnQuery({
            action: "T8ProdNetValueTask.getNavProduct",
            params: {"taskDate":row.taskDate},
            successAlert:false,
          }).then(data => {

            if(data.returndata.prodCode!=undefined){
              Tools.alertTime("<div style='word-wrap: break-word'>下载完成,以下产品净值数据不完整:" + data.returndata.prodCode + "</div>", "danger", '5000');
            }else{
              Tools.alert("下载完成");
            }

          })
        }
      }, fileName);
    },
    //一级查询被选中
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      _this.formData = Object.assign({}, row)
      this.taskStatus = _this.formData.taskStatus;
      this.$set(_this.formData, 't8DisclosureTaskId', _this.formData.id);
      this.$set(_this.formData, 'disclosureDate', _this.formData.taskDate);

    },
  }
};
</script>
