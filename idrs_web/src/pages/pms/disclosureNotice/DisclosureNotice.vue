<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="DisclosureNotice" data-target="disclosureNoticeGrid"
                               v-model="prodSearchParam">
        <k-form-item label="信披类型">
          <k-field-select v-model="prodSearchParam.disclosureType" :data-data="DocTypeDict" data-value-field="value"
                          data-display-field="text" @data-on-change="changeXpType"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型"
                     v-if="prodSearchParam.disclosureType=='5'||prodSearchParam.disclosureType=='6' ||  prodSearchParam.disclosureType=='1'||  prodSearchParam.disclosureType=='9'">
          <k-field-select v-model="prodSearchParam.disclosureSonType" :data-data="addDocTypeDict"
                          data-value-field="value" data-display-field="text"></k-field-select>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8ProdInfo.getProdInfosZG"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"></k-field-select>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="基准日期">
          <k-field-date v-model="prodSearchParam.prodBaseDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="计划发布日期">
          <k-field-date v-model="prodSearchParam.planFbDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="实际发布日期">
          <k-field-date v-model="prodSearchParam.realFbDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="公告状态">
          <k-field-select v-model="prodSearchParam.disclosureStatus" data-dict="xp_disclosure_notice_status">
          </k-field-select>
        </k-form-item>
        <k-form-item label="复核状态">
          <k-field-select v-model="prodSearchParam.reviewStatus" data-dict="xp_disclosure_check_status">
          </k-field-select>
        </k-form-item>
       <k-form-item label="托管行">
         <k-field-text v-model="prodSearchParam.trusteeName" data-validate-type="text" />
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP"  data-target="noticePublishPopup" :data-handler="checkBatchPublishData"
               style="width: 90px;" v-if="global.isShowAuthorityButton('DisclosureNotice.batchPublishChannel')">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>批量发布
        </k-btn>

        <k-btn slot="button" class="btn-custom-primary" data-functype="SUBMIT" ref="batchDownloadButton"
               :data-handler="batchDownLoad"
               v-if="global.isShowAuthorityButton('DisclosureNotice.batchDownLoad')" style="width: 90px;">
          <md-icon>cloud_download</md-icon>
          批量下载
        </k-btn>

        <k-btn slot="button" class="md-danger" data-functype="SUBMIT" ref="batchCancelButton"
               :data-handler="batchCancel" style="width: 110px;"
               :data-confirm="false" v-if="global.isShowAuthorityButton('DisclosureNotice.batchCancel')">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>批量取消发布
        </k-btn>

        <k-btn slot="button" class="md-success" data-functype="POPUP" data-target="noticeStatusChangePopup"
               :data-handler="checkStatus"
               style="width: 90px;" v-if="global.isShowAuthorityButton('DisclosureNotice.batchChangeNoticeStatus')">
          <i class="icon-reset"/>状态变更
        </k-btn>
        <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="disclosureNoticeGrid"
               :data-export-name="'信披公告管理'">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
      </k-form-search-customize>
    </div>

    <div>
      <k-grid ref="disclosureNoticeGrid" @data-row-select="selectRow"
              data-action="DisclosureNotice.findDisclosureNoticesAuth" data-fixed="right"
              @init="(grid)=>{this.$kgrid = grid}" :data-autoload="false" :data-checkbox="true" data-checkbox-id="id"
              data-tree-id="id" :data-reserve-selection="true">
        <k-grid-column data-align="left" data-header="公告id" data-name="id" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告版本id" data-name="noticeVersionId"
                       :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type"
                       data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type"
                       data-width="130"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCode" data-width="110"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称" data-name="prodName" data-width="230"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板名称" data-name="modName" data-width="100" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告标题" data-name="noticeTitle" data-width="600"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板版本号" data-name="modVersion" data-width="60" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="基准日期" data-name="prodBaseDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告版本号" data-name="disclosureVersion" data-width="100" ></k-grid-column>
        <k-grid-column data-align="left" data-header="公告状态" data-name="disclosureStatus" data-dict="xp_disclosure_notice_status" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="复核状态" data-name="reviewStatus" data-dict="xp_disclosure_check_status" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="托管行" data-name="trusteeName"  data-width="230"></k-grid-column>
        <k-grid-column data-align="left" data-header="计划发布日期" data-name="planFbDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="实际发布日期" data-name="realFbDate" data-type="date" data-width="100"></k-grid-column>


        <!--        <k-grid-column data-align="left" data-header="产品形态" data-name="prodForm" data-dict="xp_prod_form" data-hidden="true"  data-export="true" ></k-grid-column>
                <k-grid-column data-align="left" data-header="销售对象" data-name="prodObj"  data-dict="xp_target_customer" data-hidden="true" data-export="true"></k-grid-column>
                <k-grid-column data-align="left" data-header="募集方式" data-name="prodClcMth" data-dict="xp_raise_type" data-hidden="true" data-export="true"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品投资性质" data-name="prodInvTyp" data-dict="xp_prod_invest_nature" data-hidden="true" data-export="true"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品系列" data-name="prodSerNm" data-hidden="true" data-export="true"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资周期长度" data-name="invPrdLen" data-hidden="true" data-export="true"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资周期维度" data-name="invPrdDime" data-dict="xp_cycle_dimension" data-hidden="true" data-export="true"></k-grid-column>-->

        <!-- 下面为隐藏字段   -->
        <k-grid-column data-align="center" data-header="模板版本文件格式" data-name="suffix" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披规则id" data-name="t8DisclosureRuleId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披模板id" data-name="t8DisclosureModId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披模板版本id" data-name="t8DisclosureModVersionId" :data-hidden="true"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="报告类型" data-name="reportType" :data-hidden="true"></k-grid-column>-->
        <k-grid-column data-align="center" data-header="信披任务id" data-name="taskId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="计划审批日期" data-name="planSpDate" data-type="date" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="计划补录日期" data-name="planBlDate" data-type="date" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="实际补录日期" data-name="realBlDate" data-type="date" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="实际审批日期" data-name="realSpDate" data-type="date" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="文件存放路径" data-name="filePath" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="文件名" data-name="fileName" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="文件发送路径" data-name="crtPath" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建日期" data-name="crtDate" data-render="renderDateTimeCreate" data-type="date" data-width="150"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime" data-type="time" :data-hidden="true"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="创建人" data-name="crtUserId" :data-hidden="true"></k-grid-column>-->
        <k-grid-column data-align="center" data-header="创建人名称" data-name="crtUserName" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="更新日期" data-name="updDate" data-type="date" data-render="renderDateTimeUpd" data-width="150"></k-grid-column>
        <k-grid-column data-align="center" data-header="更新时间" data-name="updTime" data-type="time" :data-hidden="true"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="更新人" data-name="updUserId" :data-hidden="true"></k-grid-column>-->
        <k-grid-column data-align="center" data-header="更新人名称" data-name="updUserName" :data-hidden="true"></k-grid-column>

        <template slot="operate" slot-scope="props" width="500px">
          <k-btn class="btn-custom-text" :data-model="props.row.row" @click="popupEdit(props.row.row)" data-descript="公告查看" data-functype="POPUP" data-size="mini"
                 data-target="editDisclosureNoticePopup" v-if="props.row.row.disclosureStatus != '9'">
            查看
          </k-btn>
        </template>

      </k-grid>
    </div>

    <k-popup ref="noticeStatusChangePopup" title="批量状态变更">
      <k-form ref="noticeStatusChangeForm" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px" >

        <k-form-item label="公告状态"  :data-col="2" data-input-width="80px" >
          <k-field-select v-model="statusChangeData.disclosure_status" :data-data="statusChangeList"  data-display-field="text" data-value-field="value" :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="变更原因"  :data-col="2">
          <k-field-text v-model="statusChangeData.change_reason"  input-type="textarea" :rows="1"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="disclosureNoticeGrid" ref="submitBtn" data-from="noticeStatusChangeForm" :data-model="statusChangeData" @click="batchChangeNoticeStatus">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!-- 批量发布公告   -->
    <k-popup ref="noticePublishPopup" title="批量发布公告" >
      <k-form ref="noticePublishForm" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px" >

<!--        <k-form-item label="期望发布日期"  :data-col="2" data-input-width="80px" >-->
<!--          <k-field-date v-model="PublishformData.pub_date" data-type="date" data-date-format="yyyy-MM-dd"/>-->
<!--        </k-form-item>-->

        <k-form-item label="确定发布吗？"  :data-col="2" data-input-width="80px" >
          <k-field-date v-model="PublishformData.pub_date" data-type="date" data-date-format="yyyy-MM-dd" v-show="false"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitPublishBtn" data-from="noticePublishForm" :data-model="PublishformData" @click="batchPublishChannel">
            <span v-show="showSubmitBtn">发布</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>


    <k-popup ref="filePopup" title="上传托管机构附件">
      <k-form ref="fileForm" data-ui="element">
        <k-form-item label="托管机构附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-change="onCompareFileChange"
                          :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false"
                          data-upload-url="/upload/server/PmsApp/disclosure/uploadApproval.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="fileSubmitBtn"
                 data-from="fileForm" :data-model="truteeApproval" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
import Tools from "@/utils/tools";
import moment from "moment"
import httpUtil from "../../../frame/httpUtil";
export default {
  name: "DisclosureNotice",
  data() {
    return {
      DocTypeDict: {},
      addDocTypeDict:{},
      $kgrid: null,
      formData: {},
      PublishformData: {},
      statusChangeData: {
        disclosure_status:'',
      },
      prodSearchParam: {
        prodCode: '',
        prodName: '',
        disclosureStatus: '',
        disclosureType: '',
        disclosureSonType: '',
        prodBaseDate:'',
        reviewStatus:'',
        trusteeName:''
      },
      submitData: {},
      selectRowData: {},
      currentUserRoles: '',
      userId: '',
      showSubmitBtn:true,
      prodCode:'',
      roleList:[],
      userList:[],
      disclosureNotice:{},
      truteeApproval:{},
      disclosureNoticeChannel:[],
      DisclosureNoticeProcess:{
        t8DisclosureNoticeId:'',
        $RatGrid: null,
      },
      xpSonTypeList:[],
      statusChangeList: {},
    };
  },
  computed: {
    queryParam() {
      return {
        'prodName': this.prodSearchParam.prodName,
        'prodCode': this.prodSearchParam.prodCode,
        'disclosureStatus': this.prodSearchParam.disclosureStatus,
        'disclosureType': this.prodSearchParam.disclosureType,
        'disclosureSonType': this.prodSearchParam.disclosureSonType,
        'prodBaseDate': this.prodSearchParam.prodBaseDate,
        'reviewStatus': this.prodSearchParam.reviewStatus,
        'trusteeName': this.prodSearchParam.trusteeName,
      }
    },
  },

  created() {
    //接收路由中的参数
    this.$nextTick(()=>{
      if(this.$route.query.dealId!=null && this.$route.query.dealId!='' && this.$route.query.dealId!=undefined){
        this.$refs.disclosureNoticeGrid.load({id: this.$route.query.dealId})
      }else{
        this.changeXpType(this.$route.query.disclosureType);
        this.$set(this.prodSearchParam,"disclosureType",this.$route.query.disclosureType);
        this.$set(this.prodSearchParam,"disclosureStatus",this.$route.query.disclosureStatus);
        this.$set(this.prodSearchParam,"disclosureSonType",this.$route.query.disclosureSonType);
        this.$set(this.prodSearchParam,"planFbDate",this.$route.query.planFbDate);
        this.$refs.disclosureNoticeGrid.load(this.prodSearchParam)
      }

    });

    // this.prodSearchParam = {};
    this.xpType();
    this.httpUtil.comnQuery({
      action: "User.getAllUser",
      params: null
    }).then(data => {
      if(data.rows.length>0){
        this.userList = data.rows;
      }
    }).catch({
    });
    this.httpUtil.comnQuery({
      action: "Role.findAll",
      params: null
    }).then(data => {
      if(data.rows.length>0){
        this.roleList = data.rows;
      }
    }).catch({
    })
  },
  methods: {
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeInProd2",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    statusChange() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.statusChangeList",
        params: null
      }).then(data => {
        this.statusChangeList = data.rows;
        this.$refs.disclosureNoticeGrid.load(this.queryParam);
      }).catch({})
    },
    //检查查询条件框是否为空
    checkConditionParams: function(){
      let flag = false;
      if(this.prodSearchParam.prodName!=null && this.prodSearchParam.prodName!=''&&this.prodSearchParam.prodName!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.disclosureType!=null && this.prodSearchParam.disclosureType!=''&&this.prodSearchParam.disclosureType!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.disclosureSonType!=null && this.prodSearchParam.disclosureSonType!=''&&this.prodSearchParam.disclosureSonType!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.prodBaseDate!=null && this.prodSearchParam.prodBaseDate!=''&&this.prodSearchParam.prodBaseDate!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.prodCode!=null && this.prodSearchParam.prodCode!=''&&this.prodSearchParam.prodCode!=undefined){
        flag=true;
      }
      if(this.prodSearchParam.disclosureStatus!=null && this.prodSearchParam.disclosureStatus!=''&&this.prodSearchParam.disclosureStatus!=undefined){
        flag=true;
      }
      return flag;
    },
    //检查选中数据是否满足可发布状态
    checkBatchPublishData () {
      let pass = true;
      const _this = this;
      const list = _this.$kgrid.getSelected();

      let check = this.checkConditionParams();
      if(list.length === 0){
        Tools.alertTime( "请先勾选信披公告复选框!", "danger",5000);
        return false;
      }

      for(let i=0; i<list.length; i++){//当没有选中时不会进入
        if(list[i].disclosureStatus !== '2' && list[i].disclosureStatus !== '-1'){//仅生成待发布状态可进行发布操作
          pass = false;
        }
      }

      if(!pass){
        Tools.alert("执行批量的公告必须为[生成待发布]或[发布失败]状态!");
        this.$refs.disclosureNoticeGrid.setSelected([]);
        return false;
      }
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    renderDateTimeUpd(row) {
      return Tools.formatDateTime(row.updDate, row.updTime);
    },
    checkSelect(list){ //多选框触发限制
      if(list.length>0){
        for (let i = 0; i < list.length; i++) {
          if(list[i].disclosureType=='1'){
            Tools.alert("售前信息登记无需发布","info")
            this.$kgrid.clearAll()
          }
        }
      }
    },
    changeXpType(disclosureType) {
      this.$set(this.prodSearchParam, 'disclosureSonType', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
      }).catch({})
    },
    //检查信披公告是否可以批量变更状态
    checkStatus () {
      let pass = true;
      const check_list = this.$refs.disclosureNoticeGrid.getSelected();

      if(check_list.length === 0){
        Tools.alert("请先勾选信披公告复选框!");
        return false;
      }

      for(let i=0; i<check_list.length; i++){
        if(check_list[i].disclosureStatus !== '8' && check_list[i].disclosureStatus !== '-2'){
          pass = false;
        }
      }

      if(!pass){
        Tools.alert("只有[发布成功]或[取消发布]状态的公告才能变更状态");
        this.$refs.disclosureNoticeGrid.setSelected([]);
        return false;
      }

      this.statusChange();
    },
    //批量状态变更
    batchChangeNoticeStatus () {
      const _this = this;
      const list = _this.$kgrid.getSelected();
      let form_data = this.statusChangeData;
      if(''===this.statusChangeData.disclosure_status){
        Tools.alert("请选择公告状态!");
        return false;
      }
      this.httpUtil.comnUpdate({
        action: 'DisclosureNotice.batchChangeNoticeStatus',
        params: {list: JSON.stringify(list), 'change_reason':form_data.change_reason, 'disclosure_status':form_data.disclosure_status},
        successAlert: true,
      }).then(data =>{
        this.$refs.disclosureNoticeGrid.load(this.queryParam);//重新加载公告列表
        this.$refs.noticeStatusChangePopup.close();//关闭弹窗
        this.$refs.noticeStatusChangeForm.reset();//清空表单
        this.$refs.disclosureNoticeGrid.setSelected([]);//刷新复选框
      })
    },
    //批量取消发布
    batchCancel(){
      const _this = this;
      const list = _this.$kgrid.getSelected();
      let pass = true;

      let check = this.checkConditionParams();
      if(list.length === 0){
        Tools.alertTime( "请先勾选信披公告复选框!", "danger",5000);
        return false;
      }

      for(let i=0; i<list.length; i++){
        if(list[i].disclosureStatus === '8'){
          pass = false;
        }
      }

      if(!pass){
        Tools.alert("不能取消状态为[发布成功]的公告");
        this.$refs.disclosureNoticeGrid.setSelected([]);
        return false;
      }

      this.$confirm("是否确认取消公告？公告取消后将不再发布到对应渠道", "操作提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.httpUtil.comnUpdate({
          action: 'DisclosureNotice.batchCancel',
          params: {list: JSON.stringify(list)},
          successAlert: true,
        }).then(data => {
          this.$refs.disclosureNoticeGrid.setSelected([]);//刷新复选框
          this.$refs.disclosureNoticeGrid.load(this.queryParam);
          this.$refs.batchCancelButton.setIconStyle(1, []);//按钮恢复
        });
      }).catch(() => {
        this.$refs.batchCancelButton.setIconStyle(1, []);//按钮恢复
      });
    },
    //批量发布信披公告
    batchPublishChannel () {
      const _this = this;
      const list = _this.$kgrid.getSelected();
      for(let i=0; i<list.length; i++){
        if(list[i].disclosureType == '1' || list[i].disclosureType == '11'){
          Tools.alert("售前信息登记和手工报告无需发布,请勿勾选!");
          return false;
        }
      }
      this.showSubmitBtn = false;
      this.httpUtil.comnUpdate({
        action: 'DisclosureNotice.batchPublishChannel',
        params: {list: JSON.stringify(list),
          'prodName': this.prodSearchParam.prodName,//查询条件
          'prodCode': this.prodSearchParam.prodCode,
          'disclosureType': this.prodSearchParam.disclosureType,
          'disclosureSonType': this.prodSearchParam.disclosureSonType,
          'prodBaseDate': this.prodSearchParam.prodBaseDate,
          'disclosureStatus': this.prodSearchParam.disclosureStatus,
          'pubDate': this.PublishformData.pub_date
        },
        successAlert: true,
      }).then(data => {
        this.showSubmitBtn = true;
        this.$refs.noticeForm.reset();
        this.$refs.noticePopup.close();
        this.$refs.disclosureNoticeGrid.load(this.queryParam);
        this.$refs.disclosureNoticeGrid.setSelected([]);//刷新复选框
      }).catch(() => {
        this.showSubmitBtn = true;
        this.$refs.noticePublishPopup.close();
        this.$refs.disclosureNoticeGrid.load(this.queryParam);
        this.$refs.disclosureNoticeGrid.setSelected([]);//刷新复选框
      });
    },
    submitUploadParam(){
      const _this = this;
      const list = _this.$kgrid.getSelected();
      this.httpUtil.comnUpdate({
        action: 'DisclosureNotice.batchApproval',
        params: {list: JSON.stringify(list),
          'prodName': this.prodSearchParam.prodName,
          'prodCode': this.prodSearchParam.prodCode,
          'valuationAccountingId': this.prodSearchParam.valuationAccountingId,
          'publishStatus': this.prodSearchParam.publishStatus,
          'stage': this.prodSearchParam.stage,
          'disclosureType': this.prodSearchParam.disclosureType,
          'currentStageStatus': this.prodSearchParam.currentStageStatus,
          'disclosureSonType': this.prodSearchParam.disclosureSonType,
          'prodBaseDate': this.prodSearchParam.prodBaseDate,
          'operatingAgency': this.prodSearchParam.operatingAgency,
          'truteeApprovalResult': this.truteeApproval.truteeApprovalResult,
          'attachmentUrl': this.truteeApproval.attachmentUrl,
          'truteeApprovalResultDesc': this.truteeApproval.truteeApprovalResultDesc},
        successAlert: true,
      }).then(data => {
        this.$refs.disclosureNoticeGrid.load();
        this.$refs.submitBtn.setIconStyle(1, []);
        this.$refs.addForm.reset();
      });
    },
    batchDownLoad(){
      const _this = this;
      const listInfo = _this.$kgrid.getSelected();
      let list = listInfo.map(item => ({
        t8DisclosureNoticeId: item.id,
        id:item.noticeVersionId,
        fileName:item.fileName
      }))
      if(list.length<1&&this.prodSearchParam.prodCode==''&&this.prodSearchParam.prodName==''&&this.prodSearchParam.prodBaseDate==''&&this.prodSearchParam.disclosureType==''&&this.prodSearchParam.disclosureSonType==''&&this.prodSearchParam.disclosureStatus==''&&this.prodSearchParam.trusteeName==''){

        Tools.alert("未输入查询条件或未选择公告","danger");
        this.$refs.disclosureNoticeGrid.load(this.queryParam);
        this.$refs.batchDownloadButton.setIconStyle(1, []);
        return false;
      }
      var fileName =  moment().format('YYYYMMDD');
      this.httpUtil.download({
        url: "/download/server/PmsApp/notice/batchDownLoad.json",
        params: {list: JSON.stringify(list),
          "prodSearchParam":JSON.stringify(this.prodSearchParam)},
        callback: response => {
          Tools.alert("下载完成");
          this.$refs.disclosureNoticeGrid.load(this.queryParam);
          this.$refs.batchDownloadButton.setIconStyle(1, [])
          this.$refs.disclosureNoticeGrid.setSelected([]);//刷新复选框
        }
      }, fileName);

    },
    findRoleUser(row){
      this.httpUtil.comnQuery({
        action: "User.getRoleUser",
        params: {roleId:row.roleId},
      }).then(data => {
        if(data.rows.length>0){
          this.roleList = data.rows;
          return this.roleList;
        }
      }).catch({
      })
    },
    versionChangeFnc(val){
      this.DisclosureNoticeProcess.t8DisclosureNoticeId = val.id;
    },
    //公告详情跳转
    popupEdit(row){
      let pathUrl = '';

      pathUrl = '/main/pms/disclosureNotice/DisclosureNoticeDetail';

      this.$nextTick(() => {
        this.$router.push({
          path: pathUrl,
          query: {id:row.id,prodCode:row.prodCode,noticeVersionId:row.noticeVersionId},
        });
      });
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row);
      this.formData = Object.assign({}, row);
      this.PublishformData = Object.assign({}, row);
    },
    onSubmitDocSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.disclosureNoticeGrid.load();
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
      this.showSubmitBtn = true;
    },
    changeFlag(val) {
      if (val == 0) {
        this.truteeApproval.truteeApprovalResultDesc = '同意申请,材料齐全。';
      } else {
        this.truteeApproval.truteeApprovalResultDesc = '材料有误,请核对。';
      }
    },
    onCompareFileChange(file) {
      let fileName = file.name
      this.uploadFileName = fileName;
    },
    onFileSubmitError() {
      this.truteeApproval.uploadFileName = '';
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileSubmitBtn.setIconStyle(1, []);
    },
    onFileSubmitSuccess(res) {

      this.$set(this.truteeApproval, "uploadFileName", this.uploadFileName);
      this.$set(this.truteeApproval, "attachmentUrl", res.response.returnmsg);
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup.close();
    },
    fileSubmitUploadParam() {
      let formData = this.truteeApproval;

      this.$refs.fileUploadRef.upload(formData);
    },


  }
};
</script>
<style scoped>
>>> .el-table__cell {
  padding: 1px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
