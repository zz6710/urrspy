<template>
  <div>
    <k-form-search-customize data-target="prodGrid" v-model="queryParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="queryParam.t8ProdInfoId" data-action="T8ProdInfo.findT8ProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="id"></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="queryParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="风险评级">
        <k-field-select v-model="queryParam.prodRiskLevel" data-dict="risklevel"></k-field-select>
      </k-form-item>

      <k-form-item label="风险星级">
        <k-field-select v-model="queryParam.riskLevel" data-dict="prod_risk_level"></k-field-select>
      </k-form-item>

      <k-form-item label="风险评级状态">
        <k-field-select v-model="queryParam.riskScoreStatus" data-dict="t8_risk_score_status"></k-field-select>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="queryParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" v-show="showCreate"
             data-target="addProdRiskRatPopup"
             :data-handler="openRiskRatPopup"
             v-if="global.isShowAuthorityButton('T8ProdRiskScore.saveRatItem')">
        <md-icon md-src="/static/svg/add.svg"/>
        新增
      </k-btn>
    </k-form-search-customize>
    <k-grid ref="prodGrid" data-action="T8ProdRiskScore.findProdRiskScoreStart" @data-row-select="selectRow"
            data-operate-width="210px">
      <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
      <k-grid-column data-header="审批状态" data-name="approvalStatus" data-dict="t8_approval_status"></k-grid-column>
      <k-grid-column data-header="风险评级" data-name="prodRiskLevel" data-dict="risklevel"></k-grid-column>
      <k-grid-column data-header="风险星级" data-name="prodRiskLevel" data-dict="prod_risk_level"></k-grid-column>
      <k-grid-column data-header="风险评级状态" data-name="riskScoreStatus" data-dict="t8_risk_score_status"></k-grid-column>
      <k-grid-column data-header="创建时间" data-name="crtDate" data-render="renderDateTimeCreate"></k-grid-column>
      <k-grid-column data-header="更新时间" data-name="updDate" data-render="renderDateTimeUpdate"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple"
               :data-disabled="scope.row.row.riskScoreStatus=='3'||scope.row.row.riskScoreStatus=='4'"
               data-functype="POPUP" data-target="updateProdRiskRatPopup" data-size="mini" v-show="showUpdate"
               data-descript="修改评分" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('T8ProdRiskScore.T8ProdRiskScore')"
               :data-handler="openProdRiskRat">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-target="updatePopup" v-model="scope.row.row"
               data-descript="上传签字版风险评分"
               data-functype="POPUP" data-size="small" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('T8ProdRiskScore.addDocumentAttachment')"
               v-show="showUpload">
          <md-icon>backup</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
               data-descript="下载附件" data-functype="POPUP" data-target="downListPOPUP" v-model="scope.row.row">
          <md-icon>weekend</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="下载风险评分"
               data-size="mini"  v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
               @click="downloadProdRiskRat(scope.row.row)" >
          <md-icon>cloud_download</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.riskScoreStatus=='3'||scope.row.row.riskScoreStatus=='4'"
               data-target="riskScorePopup"  v-model="scope.row.row" data-descript="调整风险评级"
               data-functype="POPUP" data-size="small" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('T8ProdRiskScore.updateprodRiskLevel')"
               v-show="showAdjustScore">
          <md-icon>edit_attributes</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <!--新增风险评分-->
    <k-popup ref="addProdRiskRatPopup" data-title="理财产品风险评分" @data-open="setProdRiskRat" :data-dialog-drag="true">
      <k-form ref="addProdRiskRatForm" :data-col="3">
        <k-form-item label="理财产品名称:">
          <k-field-select v-model="prodRiskRat.t8ProdInfoId" data-action="T8ProdInfo.findT8ProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="id" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="模板:">
          <k-field-select v-model="prodRiskRat.t8RiskTemplateVersionId" data-action="ProdRiskTemplate.getTemplateList"
                          data-value-field="id" data-display-field="templateName" @data-on-change="versionChangeFnc"/>
        </k-form-item>
      </k-form>
      <k-grid ref="addGrid" :dataRowStyle="setBGcolor" data-action="ProdRiskRat.addQueryRatItem" :data-page-size="0" data-height="500px" data-operate-column="false"
              @init="(grid)=>{this.prodRiskRat.$RatGrid = grid}" data-display="false" style="height: 600px; overflow: auto;">
        <k-grid-column data-header="项目" data-name="riskProject" data-width="250">
          <template slot-scope="scope">
            <div style="text-align: center;" v-if="scope.row.row.coefficient != '-' && scope.row.row.coefficient != ''">{{scope.row.row.riskProject}}</div>
            <div style="margin-left: 40px;" v-if="scope.row.row.coefficient == ''">{{scope.row.row.riskProject}}</div>
            <div v-if="scope.row.row.coefficient == '-'">{{scope.row.row.riskProject}}</div>
          </template>
        </k-grid-column>
        <k-grid-column data-header="风险系数(模板)" data-name="coefficient" data-width="150"></k-grid-column>
        <k-grid-column data-header="风险系数(产品)" data-name="coefficientProd" data-width="150">
          <template slot-scope="scope" v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'">
            <el-input v-model="scope.row.row.coefficientProd" :disabled="scope.row.row.isDisabled == '0'" @change="checkDateRange(scope.row.row)"></el-input>
          </template>
        </k-grid-column>
        <k-grid-column data-header="权重" data-name="weight" data-width="150"></k-grid-column>
        <k-grid-column data-header="判断（0-1）" data-name="judge" data-width="150">
          <template slot-scope="scope" v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'">
            <el-input v-model="scope.row.row.judge" @input="sunIntegral(scope.row.row,'judge')"></el-input>
          </template>
        </k-grid-column>
        <k-grid-column data-header="积分" data-name="integral" data-width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.row.integral" :disabled="true"
                      v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'"></el-input>
            <font size="5" v-if="scope.row.row.riskProject == '产品评级得分'">{{scope.row.row.integral}}</font>
          </template>
        </k-grid-column>
      </k-grid>

      <k-form ref="addProdRiskRatForm" :data-col="3">
        <div style="margin-left: 10px">
          <k-form-item label="产品评分备注:" :data-col="2.8">
            <k-field-text v-model="prodRiskRemark" inputType="textarea" :data-max-length="4000"/>
          </k-form-item>
        </div>
      </k-form>


      <div style="text-align: right;">
        <k-btn class="btn-custom-primary" :data-handler="saveProdRiskRat" :disabled="showSubmitBtn === false">
          <span v-show="showSubmitBtn">暂存</span>
          <i v-show="!showSubmitBtn" class="el-icon-loading"/>
        </k-btn>
        <k-btn class="btn-custom-primary" :data-handler="prodRiskRatApprove" :disabled="showSubmitBtn === false">
          <span v-if="showSubmitBtn">提交审批</span>
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </div>
    </k-popup>

    <!--修改风险评分-->
    <k-popup ref="updateProdRiskRatPopup" data-title="理财产品风险评分" @data-opened="dataEcho">
      <k-form ref="updateProdRiskRatForm" :data-col="3">
        <k-form-item label="理财产品名称:">
          <k-field-select v-model="prodRiskRat.t8ProdInfoId" data-action="T8ProdInfo.findT8ProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="id" data-disabled/>
        </k-form-item>
        <k-form-item label="模板:">
          <k-field-select v-model="prodRiskRat.t8RiskTemplateVersionId" data-action="ProdRiskTemplate.getTemplateList"
                          data-value-field="id" data-display-field="templateName" data-disabled/>
        </k-form-item>
      </k-form>
      <k-grid ref="updateGrid" :dataRowStyle="setBGcolor" data-action="ProdRiskRat.queryRatItem" :data-page-size="0" data-height="500px" data-operate-column="false"
              @init="(grid)=>{this.prodRiskRat.$RatGrid = grid}" data-display="false" style="height: 600px; overflow: auto;">
        <k-grid-column data-header="项目" data-name="riskProject" data-width="250">
          <template slot-scope="scope">
            <div style="text-align: center;" v-if="scope.row.row.coefficient != '-' && scope.row.row.coefficient != ''">{{scope.row.row.riskProject}}</div>
            <div style="margin-left: 40px;" v-if="scope.row.row.coefficient == ''">{{scope.row.row.riskProject}}</div>
            <div v-if="scope.row.row.coefficient == '-'">{{scope.row.row.riskProject}}</div>
          </template>
        </k-grid-column>
        <k-grid-column data-header="风险系数(模板)" data-name="coefficient" data-width="150"></k-grid-column>
        <k-grid-column data-header="风险系数(产品)" data-name="coefficientProd" data-width="150">
          <template slot-scope="scope" v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'">
            <el-input v-model="scope.row.row.coefficientProd" :disabled="scope.row.row.isDisabled == '0'" @change="checkDateRange(scope.row.row)"></el-input>
          </template>
        </k-grid-column>
        <k-grid-column data-header="权重" data-name="weight" data-width="150"></k-grid-column>
        <k-grid-column data-header="判断（0-1）" data-name="judge" data-width="150">
          <template slot-scope="scope" v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != ''  && scope.row.row.isShowInput=='1'">
            <el-input v-model="scope.row.row.judge" @input="sunIntegral(scope.row.row,'judge')"></el-input>
          </template>
        </k-grid-column>
        <k-grid-column data-header="积分" data-name="integral" data-width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.row.integral"  :disabled="true"
                      v-if="scope.row.row.coefficient != null && scope.row.row.coefficient != '' && scope.row.row.isShowInput=='1'"></el-input>
            <font size="5" v-if="scope.row.row.riskProject === '产品评级得分'">{{scope.row.row.integral}}</font>
          </template>
        </k-grid-column>
      </k-grid>
      <k-form ref="addProdRiskRatForm" :data-col="3">
        <div style="margin-left: 10px">
          <k-form-item label="产品评分备注:" :data-col="2.8">
            <k-field-text v-model="prodRiskRemark" inputType="textarea" :data-max-length="4000"/>
          </k-form-item>
        </div>
      </k-form>
      <div style="text-align: right;">
        <k-btn class="btn-custom-primary" :data-handler="saveProdRiskRat" :disabled="showSubmitBtn === false">
          <span v-if="showSubmitBtn">暂存</span>
          <i v-if="!showSubmitBtn" class="el-icon-loading"/>
        </k-btn>
        <k-btn class="btn-custom-primary" :data-handler="prodRiskRatApprove" :disabled="showSubmitBtn === false">
          <span v-if="showSubmitBtn">提交审批</span>
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </div>
    </k-popup>

    <k-popup ref="updatePopup" title="上传文件">
      <k-form ref="updateForm" data-ui="element">
        <k-form-item label="上传签字版风险评分" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          data-accept=".pdf"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/RiskScore/uploadProdRiskRat.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="submitBtn"
                 data-from="updateForm" :data-model="formData" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="downListPOPUP" title="附件信息列表" @data-opened="loadAttachmentInfo">
      <k-grid ref="downloadAgencyAgreementGrid"  :data-autoload="false"
              data-action="DocumentAttachment.getAttachmentInfo" :dataPopupAppendToBody="true">
        <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"/>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"/>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.fileName"
                 data-descript="下载" data-functype="DOWNLOAD" data-size="small"
                 data-url="/download/server/PmsApp/documentAttachment/downAttachment.json" v-model="scope.row.row">
            <md-icon>cloud_download</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除附件" data-functype="SUBMIT"  data-confirm data-type="danger"
                 data-target="downloadAgencyAgreementGrid"
                 data-action="T8ProdRiskScore.deleteFile"
                 v-if="global.isShowAuthorityButton('T8ProdRiskScore.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </k-popup>

    <k-popup ref="riskScorePopup" title="调整风险评级">
      <k-form ref="riskScoreForm" data-ui="element">
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品风险评级">
          <k-field-select v-model="formData.prodRiskLevel" data-dict="risklevel" :data-allowblank="false" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="submitBtn"
                 data-from="riskScoreForm" :data-model="formData" data-target="prodGrid" data-action="T8ProdRiskScore.updateprodRiskLevel" >确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  import { assign } from "lodash";
  export default {
    name: "Riskscore",
    data() {
      return {
        scoreId:'',
        queryParam:{
          prodCode: '',
          prodRiskLevel: '',
          riskScoreStatus: ''
        },
        formData: {},
        prodRiskRemark: '',
        selectRowData: {},
        prodRiskRat: {
          prodName: '',
          t8ProdInfoId: '',
          t8RiskTemplateVersionId: '',
          $RatGrid: null,//风险评分表格对象
        },
        showSubmitBtn:true,
        showCreate:true,//新增
        showUpdate:true,//修改评分
        showUpload:true,//上传签字版风险评分
        showAdjustScore:true,//调整风险评级
      };
    },
    created() {
      this.global.getProdUser('');
      this.$nextTick(()=> {
        //产品运营导航跳转过来，接收参数并加载表格数据 rnn 20210311
        if (this.$route.query.t8_prod_info_id != '') {
          this.$refs.prodGrid.load({t8ProdInfoId: this.$route.query.t8_prod_info_id});
          //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
          this.global.getHideButtons(this);
        }
      });
    },
    methods: {
      renderDateTimeCreate(row) {
        return Tools.formatDateTime(row.crtDate, row.crtTime);
      },
      renderDateTimeUpdate(row) {
        return Tools.formatDateTime(row.updDate, row.updTime);
      },
      openRiskRatPopup() {
        //打开添加弹窗  id制空
        this.scoreId = ''
      },
      prodRiskRatApprove() {
        if (!this.prodRiskRat.t8ProdInfoId) {
          Tools.alert("请先选择产品!", "danger")
          return false
        }
        var length = this.prodRiskRat.$RatGrid.list.length;
        var score = this.prodRiskRat.$RatGrid.list[length - 1].integral
        this.showSubmitBtn = false
        //注意权限，是否要走审批流及提示信息 axin
        this.httpUtil.comnUpdate({
          action: "T8ProdRiskScore.prodRiskRatFlow",
          params: {
            t8ProdInfoId:this.prodRiskRat.t8ProdInfoId,
            datas: JSON.stringify(this.prodRiskRat.$RatGrid.list),
            integral: score,
            t8RiskTemplateVersionId:this.prodRiskRat.t8RiskTemplateVersionId,
            id:this.scoreId,
            riskRemark:this.prodRiskRemark
          },
          mask: true,
          successAlert: false,
        }).then(data => {
          //判断是否进入了审批流
          if (data.returndata != undefined && data.returndata.data != undefined
            && data.returndata.data.processInstanceId != undefined) {
            this.$set(this.formData, 'processInstanceId', data.returndata.data.processInstanceId);//获取进入审批流后的实列id
            //  str = data.returnmsg;//成功后的返回信息
          }
          if (data.success) {//（进入审批流且成功或者没有审批流），并且有按钮权限
            Tools.alert(data.returnmsg)
          }
        });
        this.showSubmitBtn = true
        this.$refs.updateProdRiskRatPopup.close();
        this.$refs.addProdRiskRatPopup.close();
        this.$refs.prodGrid.load();
      },
      //根据参数判断本行是否设置背景颜色
      setBGcolor(row){
        let stylejson={};
        if(this.getTitle1(row.row.riskProject)){
          stylejson.backgroundColor="#CC99FF";
        }else if(this.getTitle2(row.row.riskProject)){
          stylejson.backgroundColor="#FFFF00";
        }
        return stylejson;
      },
      //紫色标题
      getTitle1(val){
        let title=['投资价值风险','流动性风险','操作风险','汇率风险','其他风险','投资策略调减项**','投资策略调增项**'];
        for(let i=0;i<=title.length;i++){
          if(title[i] == val){
            return true;
          }
        }
        return false;
      },
      //黄色标题
      getTitle2(val){
        let title=['产品类型','融资主体性质','公开市场评级','增信措施','资产区域分布情况','资产层级情况','投资范围','募集方式','杠杆比例（总资产/净资产）','分级比例（优先级/劣后级）','开放式产品','封闭式产品','估值方式','操作程序','币种'];
        for(let i=0;i<=title.length;i++){
          if(title[i] == val){
            return true;
          }
        }
        return false;
      },
      setProdRiskRat(){
        this.prodRiskRat.prodName = ''
        this.prodRiskRat.t8ProdInfoId = ''
        this.prodRiskRat.t8RiskTemplateVersionId = ''
        this.prodRiskRat.$RatGrid = null
        this.prodRiskRemark = '';
      },
      /* 加载评分数据*/
      dataEcho(){
        const _this = this
        _this.prodRiskRat.t8RiskTemplateVersionId = ''
        this.httpUtil.comnQuery({
          action: 'ProdRiskTemplateVersion.getTemplateVersionId',
          params: {
            id : this.formData.t8ProdInfoId
          }
        }).then(data => {
          if (data.rows.length > 0) {
            this.$set(_this.prodRiskRat,"t8RiskTemplateVersionId",data.rows[0].id);
            //重新加载表格
            this.prodRiskRat.$RatGrid.load({t8RiskTemplateVersionId: _this.prodRiskRat.t8RiskTemplateVersionId, t8ProdInfoId: _this.prodRiskRat.t8ProdInfoId});
          }
        });
        //查询风险频分备注
        this.httpUtil.comnQuery({
          action: 'ProdRiskRemark.queryRemark',
          params: {
            t8ProdInfoId : this.formData.t8ProdInfoId
          }
        }).then(data => {
          if (data.rows.length > 0) {
            this.prodRiskRemark = data.rows[0].riskRemark;
          }
        });

      },
      /*风险系数输入项校验*/
      checkDateRange(rows) {
        //coefficientProd
        var coefficient = rows.coefficient
        var coefficients = []
        /*保存分割的字符串*/
        var splitStr = '';
        if (coefficient) {
          //判断风险系数是否包含字符'-','—','~','>=','<=',如果有这类字符则进行分割。
          if (coefficient.indexOf(">=") !== -1) {
            coefficients = coefficient.split(">=");
            splitStr = ">=";
          } else if (coefficient.indexOf("<=") !== -1) {
            coefficients = coefficient.split("<=");
            splitStr = "<=";
          } else if (coefficient.indexOf("—") !== -1) {
            coefficients = coefficient.split("—");
            splitStr = "—";
          } else if (coefficient.indexOf("~") !== -1) {
            coefficients = coefficient.split("~");
            splitStr = "~";
          } else {
            coefficients = coefficient.split("-");
            splitStr = "-";
          }
        }
        //风险系数包含字符'-','—','~'的判断
        if (coefficients.length === 2 && (splitStr === '-' || splitStr === '~' || splitStr === '—')) {
            if (parseInt(coefficients[0]) < parseInt(coefficients[1])) {
              if (parseInt(coefficients[0]) > parseInt(rows.coefficientProd)) {
                rows.coefficientProd = ''
                Tools.alert("该项需要大于或等于" + parseInt(coefficients[0]), 'warning')
              } else if (parseInt(coefficients[1]) < parseInt(rows.coefficientProd)) {
                rows.coefficientProd = ''
                Tools.alert("该项需要小于或等于" + parseInt(coefficients[1]), 'warning')
              }
            } else if (parseInt(coefficients[0]) > parseInt(coefficients[1])) {
              if (parseInt(coefficients[1]) > parseInt(rows.coefficientProd)) {
                rows.coefficientProd = ''
                Tools.alert("该项需要大于或等于" + parseInt(coefficients[1]), 'warning')
              } else if (parseInt(coefficients[0]) < parseInt(rows.coefficientProd)) {
                rows.coefficientProd = ''
                Tools.alert("该项需要小于或等于" + parseInt(coefficients[0]), 'warning')
              }
            }
        } else if (splitStr === '>=') {
          if (parseInt(coefficients[1]) > parseInt(rows.coefficientProd)) {
            rows.coefficientProd = ''
            Tools.alert("该项需要大于" + coefficients[1], 'warning')
          }
        } else if (splitStr === '<=') {
          if (parseInt(coefficients[1]) < parseInt(rows.coefficientProd)) {
            rows.coefficientProd = ''
            Tools.alert("该项需要小于" + coefficients[1], 'warning')
          }
        }
        //调用计算方法
        this.sunIntegral(rows,'judge')
      },
      riskHandler(val){
        if (val.riskScore){
          if (parseInt(val.riskScore) > 0 && parseInt(val.riskScore) <= 1){
            return '一星级';
          }else if(parseInt(val.riskScore) > 1 && parseInt(val.riskScore) <= 2){
            return '二星级';
          }else if(parseInt(val.riskScore) > 2 && parseInt(val.riskScore) <= 3){
            return '三星级';
          }else if(parseInt(val.riskScore) > 3 && parseInt(val.riskScore) <= 4){
            return '四星级';
          }else{
            return '五星级';
          }
        }
      },
      selectRow(row, column, event){
        const _this = this;
        _this.formData = assign({}, row);
      },
      updateriskScore(val) {
        var _this = this;
        var _val = val;
        this.$confirm('确认改评分状态?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.httpUtil.comnUpdate({
            action: 'T8ProdInfo.updateriskScoreStatus',
            params: {prodCode:_val,riskScoreStatus:'3'},
            successAlert: true,
          }).then(data => {
            if (data.success){
              this.$refs.prodGrid.load();
            }

          });
        }).catch(error => {
         Tools.alert("确认失败!", "danger");
        });
      },
      //打开理财产品评分窗口
      openProdRiskRat(params){
        //拿到主id
        this.scoreId=params.id
        //产品ID
        this.prodRiskRat.t8ProdInfoId=params.t8ProdInfoId;
        //产品名称
        this.prodRiskRat.prodName=params.prodName;
      },
      //版本号变动
      versionChangeFnc(val){
        this.httpUtil.comnQuery({
          action: 'T8ProdRiskScore.findProdRiskScoreByCode',
          params: {t8ProdInfoId:this.prodRiskRat.t8ProdInfoId},
          successAlert: false,
        }).then(data => {
          if(data.rows.length>0){
            //alert(val+"===="+);
            if(val==data.rows[0].id){
              Tools.alert("已存在该模板风险评级!请勿重复添加!!", "danger");
              return false;
            }else{
              Tools.confirm(() => {
                  this.prodRiskRat.$RatGrid.load({t8RiskTemplateVersionId: this.prodRiskRat.t8RiskTemplateVersionId, t8ProdInfoId: this.prodRiskRat.t8ProdInfoId});
                },
                "已存在其他模板风险评级!继续添加将会删除存在模板,确定继续添加吗?"
              )

              /*return false;
              Tools.confirm("已存在该模板风险评级!请勿重复添加!!", "danger");*/
            }
          }else{
            if(val != ""){
              //重新加载表格
              this.prodRiskRat.$RatGrid.load({t8RiskTemplateVersionId: this.prodRiskRat.t8RiskTemplateVersionId, t8ProdInfoId: this.prodRiskRat.t8ProdInfoId});
            }
          }
        });

      },
      //合计积分
      sunIntegral(data,key){
        //1.判断judge值是否小于0大于1
        if (data.judge < 0 || data.judge > 1) {
          data.judge="";
          data.integral = "";
          Tools.alert("输入错误,该列只能输入0到1之间的数字,请重新输入!","danger");
          return;
        }
        //如果是输入判断值，则自动计算出积分值
        if(key === "judge"){
          //删除判断值时积分设置成0
          if(!data.judge || !data.coefficientProd || !data.weight ){
            data.integral="";
          }else{
            //判断风险系数是否是正常数字
            if(data.coefficientProd &&!isNaN(data.coefficientProd)){
                  let arr = data.judge.toString().split('.');
                  if(arr.length>1){
                    let len = arr[1].length;
                    if(len>5){
                      data.judge="";
                      data.integral = "";
                      Tools.alert("输入错误,该列只能输入0到1之间的数字,保留小数点后5位,请重新输入!","danger");
                      return;
                    }else{
                      //积分=风险系数*权重*判断
                      let v1 = Tools.accMul(parseFloat(data.coefficientProd),parseFloat(data.weight));
                      let v2 = parseFloat(data.judge)
                      //解决存在科学计数法问题,小数位最多能有20位
                      let sunCount= Number(Tools.accMul(v1, v2)).toFixed(8);
                      data.integral=Tools.toFixed(sunCount,5);
                    }
                  }else{
                    //积分=风险系数*权重*判断
                    let v1 = Tools.accMul(parseFloat(data.coefficientProd),parseFloat(data.weight));
                    let v2 = parseFloat(data.judge)
                    //解决存在科学计数法问题
                    let sunCount= Number(Tools.accMul(v1, v2)).toFixed(8);
                    data.integral=Tools.toFixed(sunCount,5);
                  }
            }else{
                let arr = data.judge.toString().split('.');
                if(arr.length>1){
                  let len = arr[1].length;
                  if(len>5){
                    data.judge="";
                    data.integral = "";
                    Tools.alert("输入错误,该列只能输入0到1之间的数字,保留小数点后5位,请重新输入!","danger");
                    return;
                  }
                }
            }
          }
        }
        //总数
        let num=0.00;
        //循环数据集合汇总积分
        for(var i = 0; i<  this.prodRiskRat.$RatGrid.list.length; i++){
          //产品评级得分不参与汇总
          if(this.prodRiskRat.$RatGrid.list[i].integral && !isNaN(this.prodRiskRat.$RatGrid.list[i].integral) && this.prodRiskRat.$RatGrid.list[i].riskProject !== '产品评级得分'){
            num+=parseFloat(this.prodRiskRat.$RatGrid.list[i].integral);
          }
          //给产品评级得分设置值（方便保存的时候取数）
          if(i === this.prodRiskRat.$RatGrid.list.length-1){
            this.prodRiskRat.$RatGrid.list[i].integral=Tools.toFixed(num,5);
          }
        }
        this.sunIntegralCount=Tools.toFixed(num,5);
      },
      //保存风险评分
      saveProdRiskRat(){
        if (!this.prodRiskRat.t8ProdInfoId) {
          Tools.alert("请先选择产品!", "danger")
          return false
        }
        var length = this.prodRiskRat.$RatGrid.list.length;
        var score = this.prodRiskRat.$RatGrid.list[length -1].integral
        this.showSubmitBtn = false
        this.httpUtil.comnUpdate({
          action: "ProdRiskRat.saveRatItem",
          params: {
            t8ProdInfoId:this.prodRiskRat.t8ProdInfoId,
            datas: JSON.stringify(this.prodRiskRat.$RatGrid.list),
            integral: score,
            t8RiskTemplateVersionId:this.prodRiskRat.t8RiskTemplateVersionId,
            riskRemark: this.prodRiskRemark
          },
          mask: true
        }).then(data => {
          this.showSubmitBtn = true
          if(data.success){
            this.$refs.updateProdRiskRatPopup.close();
            this.$refs.addProdRiskRatPopup.close();
            this.$refs.prodGrid.load();
          }
        });
      },
      //下载理财产品风险评分表
      downloadProdRiskRat(rows){
        var filieName = rows.prodName + "_风险评分.xls";
        this.httpUtil.download({
          url: "/download/server/PmsApp/RiskScore/downloadProdRiskRat.json",
          params: {
            prodInfoId:rows.t8ProdInfoId,
            prodName:rows.prodName
          },
          callback: response => {
          }
        }, filieName);
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, []);
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.updateForm.reset();
        this.$refs.updatePopup.close();
        this.$refs.prodGrid.load();
      },
      submitUploadParam() {
        this.$refs.uploadRef.upload(this.formData);
      },
      loadAttachmentInfo(){
        this.$refs.downloadAgencyAgreementGrid.load({
          attachmentType: '3',
          prodCode: this.formData.prodCode
        })
      }
    }
  }
</script>
<style scoped>
</style>
