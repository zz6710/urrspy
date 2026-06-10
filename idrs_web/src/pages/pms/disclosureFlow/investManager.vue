<template>
  <div>
    <k-form-search-customize data-target="investManagerGrid" v-model="queryForm">
      <k-form-item label="产品代码">
        <k-field-select v-model="queryForm.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="queryForm.prodName" data-validate-type="text"/>
      </k-form-item>


      <k-form-item label="产品状态">
        <k-field-select v-model="queryForm.prodStatus" data-dict="t8_prod_status"/>
      </k-form-item>
<!--      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="openBox" data-target="addTable">
        <md-icon md-src="/static/svg/add.svg" />
        新增
      </k-btn>-->
    </k-form-search-customize>

    <k-grid ref="investManagerGrid" data-action="T8ProdInvestManager.findT8ProdInfoInfos1" @data-row-select="selectStaticTemp" data-operate-width="200px">
      <k-grid-column data-header="序号" data-name="t8ProdInfoId" :data-hidden="true" />
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="180"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="280"/>
      <k-grid-column data-align="center" data-header="产品形态" data-name="prodMode" data-dict="t8_prod_create_type" data-width="140"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>

      <k-grid-column data-align="center" data-header="产品管理人" data-name="managerCode" data-width="280"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="新增" data-functype="POPUP"
               data-size="mini" data-target="addTable" :data-handler="addHandler" v-if="global.isShowAuthorityButton('T8ProdInvestManager.addInvestManager')">
          <md-icon>add</md-icon>
        </k-btn>
      </template>
    </k-grid>
    <k-grid ref="investManagerInfoGrid" @data-row-select="selectRow" data-action="T8ProdInvestManager.findInvestManagerInfos" :data-autoload="false">
      <k-grid-column data-header="序号" data-name="id" :data-hidden="true" ></k-grid-column>
      <k-grid-column data-align="center" data-header="新任投资经理id" data-name="newInvestId" :data-hidden="true" />
      <k-grid-column data-align="center" data-header="新任投资经理姓名" data-name="newInvestName" data-width="120"/>
      <k-grid-column data-align="center" data-header="管理本产品开始日" data-name="postDate" data-type="date" data-width="120"/>
      <k-grid-column data-align="center" data-header="离任经理姓名" data-name="leaveInvestName" />
      <k-grid-column data-align="center" data-header="离任经理id" data-name="leavingManagerId"  :data-hidden="true" />
      <k-grid-column data-align="center" data-header="离任原因" data-name="leavingReason" />
      <k-grid-column data-align="center" data-header="管理本产品结束日" data-name="leavingDate" data-type="date" data-width="120"/>
      <k-grid-column data-align="center" data-header="转岗说明" data-name="changeJobDesc" />
      <k-grid-column data-align="center" data-header="人员类型" data-name="mangerType" data-dict="t8_manager_type"/>
      <k-grid-column data-align="center" data-header="更新日期" data-name="updateDate" :data-hidden="true" data-type="date"></k-grid-column>
      <k-grid-column data-align="center" data-header="更新时间" data-name="updateTime" :data-hidden="true" data-type="time"></k-grid-column>
      <k-grid-column data-align="center" data-header="更新日期" data-name="createDate" data-type="date"></k-grid-column>
      <k-grid-column data-align="center" data-header="更新时间" data-name="createTime" data-type="time"></k-grid-column>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改"
               data-functype="POPUP" data-size="mini" :data-handler="editHandler"
               data-target="editT8ProdInvestManagerPopup" v-if="global.isShowAuthorityButton('T8ProdInvestManager.updateInvestManager')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="变更"
               data-functype="POPUP" data-size="mini" :data-handler="changeHandler"
               data-target="changeTable" v-if="global.isShowAuthorityButton('T8ProdInvestManager.changeInvestManager')">
          <md-icon>done</md-icon>
        </k-btn>
      </template>
    </k-grid>
    <k-popup ref="editT8ProdInvestManagerPopup" data-title="修改">
      <k-form ref="addForm2" :data-col="2" data-input-width="220px" data-label-width="360px" data-total-width="1288px">
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true" :data-max-length="128"></k-field-text>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true" :data-max-length="64"></k-field-text>
        </k-form-item>
        <k-form-item label="产品管理人名称">
          <k-field-text v-model="formData.prodManagerId" :data-disabled="true" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="共同管理本产品的其他投资经理姓名">
          <k-field-text v-model="formData.otherManagerId" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="共同管理本产品的其他投资经理姓名" v-show="false">
          <k-field-text v-model="formData.otherManagerName" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="新任投资经理姓名">
          <k-field-select v-model="formData.newInvestName" :data-disabled="true"  :data-params="{'prodCode':formData.prodCode}" data-action="T8Dict.findT8InvestManagerInfos"
                          data-display-field="custName" data-value-field="jobno" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="从业年限">
          <k-field-text v-model="formData.employmentTerm" :data-max-length="2" data-min-value="1" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="投资管理从业年限">
          <k-field-text v-model="formData.investEmploymentTerm" :data-max-length="2" data-min-value="1" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="过往从业经历">
          <k-field-text v-model="formData.pastEmploymentExp" :data-max-length="254" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="国籍">
          <k-field-select v-model="formData.nationality" data-dict="t8_nation_code" :data-default-value="'CHN'" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="是否已按规定在银行业理财登记托管中心注册">
          <k-field-select v-model="formData.isRegister" data-dict="t8_prod_isok" :data-default-value="'1'" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="学历、学位">
          <k-field-text v-model="formData.education" :data-max-length="254" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="是否曾被监管机构予以行政处罚或采取行政监管措施">
          <k-field-select v-model="formData.isPunish" :data-allowblank="false" :data-default-value="'0'" data-dict="t8_prod_isok"></k-field-select>
        </k-form-item>
        <k-form-item label="登记编号">
          <k-field-text v-model="formData.register" :data-max-length="254" :data-allowblank="true"></k-field-text>
        </k-form-item>
      </k-form>
      <div style="margin: 0 auto; width: 255px; text-align: center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="editSubmitHandle"
               data-action="T8ProdInvestManager.updateInvestManager" data-target="investManagerGrid" :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>
    </k-popup>

    <k-popup ref="addTable" data-title="新增">
      <k-form ref="addForm1" :data-col="2" data-input-width="220px" data-label-width="360px" data-total-width="1288px">
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true" :data-max-length="128"></k-field-text>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true" :data-max-length="64"></k-field-text>
        </k-form-item>
        <k-form-item label="产品管理人名称">
          <k-field-text v-model="formData.managerCode" :data-disabled="true" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="共同管理本产品的其他投资经理姓名">
          <k-field-text v-model="formData.otherManagerId" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="共同管理本产品的其他投资经理姓名" v-show="false">
          <k-field-text v-model="formData.otherManagerName" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="新任投资经理姓名">
          <k-field-select v-model="formData.newInvestId" @data-on-change="getInvestInfo(formData)" :data-params="{'prodCode':formData.prodCode}" data-action="T8Dict.findT8InvestManagerInfos"
                        data-display-field="custName" data-value-field="jobno" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="从业年限">
          <k-field-text v-model="formData.employmentTerm" :data-max-length="2" data-min-value="1" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="投资管理从业年限">
          <k-field-text v-model="formData.investEmploymentTerm" :data-max-length="2" data-min-value="1" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="过往从业经历">
          <k-field-text v-model="formData.pastEmploymentExp" :data-max-length="254" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="管理本产品开始日">
          <k-field-date v-model="formData.postDate" :data-allowblank="false"></k-field-date>
        </k-form-item>
        <k-form-item label="国籍">
          <k-field-select v-model="formData.nationality" data-dict="t8_nation_code" :data-default-value="'CHN'" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="学历、学位">
          <k-field-text v-model="formData.education" :data-max-length="254" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="是否已按规定在银行业理财登记托管中心注册">
          <k-field-select v-model="formData.isRegister" data-dict="t8_prod_isok" :data-default-value="'1'" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="登记编号">
          <k-field-text v-model="formData.register" :data-max-length="254" :data-allowblank="true"></k-field-text>
        </k-form-item>
        <k-form-item label="是否曾被监管机构予以行政处罚或采取行政监管措施">
          <k-field-select v-model="formData.isPunish" :data-allowblank="false" :data-default-value="'0'" data-dict="t8_prod_isok"></k-field-select>
        </k-form-item>
      </k-form>
      <div style="margin: 0 auto; width: 255px; text-align: center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="addSubmitHandle"
               data-action="T8ProdInvestManager.addInvestManager" data-target="investManagerGrid" :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>
    </k-popup>

    <k-popup ref="changeTable" data-title="变更">
      <k-form ref="addForm3" :data-col="2" data-input-width="220px" data-label-width="360px" data-total-width="1288px">
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true" :data-max-length="128"></k-field-text>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true" :data-max-length="64"></k-field-text>
        </k-form-item>
        <k-form-item label="产品管理人名称">
          <k-field-text v-model="formData.managerCode" :data-disabled="true" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="共同管理本产品的其他投资经理姓名">
          <k-field-text v-model="formData.otherManagerId" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="共同管理本产品的其他投资经理姓名" v-show="false">
          <k-field-text v-model="formData.otherManagerName" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="离任投资经理姓名">
          <k-field-select v-model="formData.leavingManagerId" :data-disabled="true" data-action="T8Dict.findT8InvestManagerInfos"
                          data-display-field="custName" data-value-field="jobno" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="是否交接">
          <k-field-select v-model="formData.status" data-dict="1yes0no" :data-default-value="'1'" :data-allowblank="false" :data-disabled="this.isUpdate">
          </k-field-select>
        </k-form-item>
        <k-form-item label="离任原因" v-if="formData.status == 1">
          <k-field-text v-model="formData.leavingReason" :data-max-length="254" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="转岗说明" v-if="formData.status == 1">
          <k-field-text v-model="formData.changeJobDesc" :data-max-length="254" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="管理本产品结束日" v-if="formData.status == 1">
          <k-field-date v-model="formData.leavingDate" :data-allowblank="false"></k-field-date>
        </k-form-item>
        <k-form-item label="新任投资经理姓名" v-if="formData.status == 1">
          <k-field-select v-model="formData.newInvestId" @data-on-change="getInvestInfo(formData)"  :data-params="{'prodCode':formData.prodCode}" data-action="T8Dict.findT8InvestManagerInfos"
                          data-display-field="custName" data-value-field="jobno" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="从业年限" v-if="formData.status == 1">
          <k-field-text v-model="formData.employmentTerm" :data-max-length="2" data-min-value="1" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="投资管理从业年限" v-if="formData.status == 1">
          <k-field-text v-model="formData.investEmploymentTerm" :data-max-length="2" data-min-value="1" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="过往从业经历" v-if="formData.status == 1">
          <k-field-text v-model="formData.pastEmploymentExp" :data-max-length="254" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="国籍" v-if="formData.status == 1">
          <k-field-select v-model="formData.nationality" data-dict="t8_nation_code" :data-default-value="'CHN'" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="学历、学位" v-if="formData.status == 1">
          <k-field-text v-model="formData.education" :data-max-length="254" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="是否已按规定在银行业理财登记托管中心注册" v-if="formData.status == 1">
          <k-field-select v-model="formData.isRegister" data-dict="t8_prod_isok" :data-default-value="'1'" :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="管理本产品开始日" v-if="formData.status == 1">
          <k-field-date v-model="formData.postDate" :data-allowblank="false"></k-field-date>
        </k-form-item>
        <k-form-item label="是否曾被监管机构予以行政处罚或采取行政监管措施" v-if="formData.status == 1">
          <k-field-select v-model="formData.isPunish" :data-allowblank="false" :data-default-value="'0'" data-dict="t8_prod_isok"></k-field-select>
        </k-form-item>
      </k-form>
      <div style="margin: 0 auto; width: 300px; text-align: center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="changeSubmitHandle"
               data-action="T8ProdInvestManager.changeInvestManager" data-target="investManagerInfoGrid" :data-model="formData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </div>
    </k-popup>

  </div>
</template>

<script>
import { assign } from "lodash";
import Tools from "@/utils/tools";
export default {
  data() {
    return {
      queryForm: {
        investManageIdcardNo: "",
        prodCode: "",
        prodStatus: "",
      },
      formData: {otherManagerName:''},
      extraData: {},
      fileList: [],
      envItems: [],
      fileData: "",
      uploadData: {
        id: "",
      },
      showSubmitBtn: true,
      queryParentId:'',
      attachmentType:'',
      isUpdate:false,
    };
  },
  methods: {
    changeHandler(value){
        this.httpUtil.comnQuery({
            action:'T8ProdInvestManager.countOtherInvestManager1',
            params:{
                t8ProdInfoId:value.t8ProdInfoId,
                'newInvestId':value.newInvestId
            }
        }).then(data => {
            if (data.rows.length == 0)
                this.isUpdate = true;
        });
      this.httpUtil.comnQuery({
        action: 'T8ProdInvestManager.findT8ProdInfoInfosById',
        params: {
          id: value.id,
        }
      }).then(data => {
        this.$nextTick(()=>{
          if (data.rows.length>0){
            this.$set(this.formData,"prodName",data.rows[0].prodName);
            this.$set(this.formData,"prodCode",data.rows[0].prodCode);
            this.$set(this.formData,"managerCode",data.rows[0].prodManagerId);
            this.httpUtil.comnQuery({
              action: 'T8ProdInvestManager.findAllT8ProdInfoInfos',
              params: {
                t8ProdInfoId: value.t8ProdInfoId,
                id: value.id
              }
            }).then(data => {
              this.$nextTick(()=>{
                if (data.rows.length>0){
                  this.$set(this.formData,"otherManagerId",data.rows[0].newInvestName);
                  this.$set(this.formData,"otherManagerName",data.rows[0].custNo);
                }
              })
            });
            this.$set(this.formData,"leavingManagerId",data.rows[0].newInvestId);
            this.$set(this.formData,"newInvestId",'');
            this.$set(this.formData,"leavingReason",'');
            this.$set(this.formData,"leavingDate",'');
            this.$set(this.formData,"changeJobDesc",'');
            this.$set(this.formData,"employmentTerm",'');
            this.$set(this.formData,"investEmploymentTerm",'');
            this.$set(this.formData,"pastEmploymentExp",'');
            this.$set(this.formData,"education",'');
            this.$set(this.formData,"postDate",'');
            this.$set(this.formData,"register",'');
            this.$set(this.formData,"isPunish",'');
            this.$set(this.formData,"nationality",'');
            this.$set(this.formData,"isRegister",'');

          }
        })
      });
    },
    editHandler(value){
      this.httpUtil.comnQuery({
        action: 'T8ProdInvestManager.findT8ProdInfoInfosById',
        params: {
          id: value.id,
        }
      }).then(data => {
        this.$nextTick(()=>{
          if (data.rows.length>0){
            this.$set(this.formData,"prodName",data.rows[0].prodName);
            this.$set(this.formData,"prodCode",data.rows[0].prodCode);
            this.$set(this.formData,"managerCode",data.rows[0].prodManagerId);
            this.httpUtil.comnQuery({
              action: 'T8ProdInvestManager.findAllT8ProdInfoInfos',
              params: {
                t8ProdInfoId: value.t8ProdInfoId,
                 id: value.id
              }
            }).then(data => {
              this.$nextTick(()=>{
                if (data.rows.length>0){
                  this.$set(this.formData,"otherManagerId",data.rows[0].newInvestName);
                  this.$set(this.formData,"otherManagerName",data.rows[0].jobno);
                }
              })
            });
            this.$set(this.formData,"otherManagerId",data.rows[0].newInvestName);
            this.$set(this.formData,"otherManagerName",data.rows[0].jobno);
            this.$set(this.formData,"employmentTerm",data.rows[0].employmentTerm);
            this.$set(this.formData,"investEmploymentTerm",data.rows[0].investEmploymentTerm);
            this.$set(this.formData,"pastEmploymentExp",data.rows[0].pastEmploymentExp);
            this.$set(this.formData,"education",data.rows[0].education);
            this.$set(this.formData,"postDate",data.rows[0].postDate);
            this.$set(this.formData,"register",data.rows[0].register);
            if(data.rows[0].isPunish!=undefined&&data.rows[0].isPunish!=null&&data.rows[0].isPunish!=''){
              this.$set(this.formData,"isPunish",data.rows[0].isPunish);
            }else{
              this.$set(this.formData,"isPunish",'0');
            }
            if(data.rows[0].nationality!=undefined&&data.rows[0].nationality!=null&&data.rows[0].nationality!=''){
              this.$set(this.formData,"nationality",data.rows[0].nationality);
            }else{
              this.$set(this.formData,"nationality",'CHN');
            }
            if(data.rows[0].isRegister!=undefined&&data.rows[0].isRegister!=null&&data.rows[0].isRegister!=''){
              this.$set(this.formData,"isRegister",data.rows[0].isRegister);
            }else{
              this.$set(this.formData,"isRegister",'1');
            }
          }
        })
      });
    },
    addHandler(value){
      this.httpUtil.comnQuery({
        action: 'T8ProdInvestManager.findAllT8ProdInfoInfos',
        params: {
          t8ProdInfoId: value.t8ProdInfoId,
        }
      }).then(data => {
        this.$nextTick(()=>{
          if (data.rows.length>0){
            this.$set(this.formData,"otherManagerId",data.rows[0].newInvestName);
            this.$set(this.formData,"otherManagerName",data.rows[0].custNo);
          }
        })
      });
    },
    getInvestInfo(value){
      let existUser = this.formData.otherManagerName;
      let newUser = this.formData.newInvestId;
      if(existUser!=undefined&&existUser!=null&&existUser!=''){
        let arr = existUser.split(",")
        let flag=false;
        for(let i=0;i<arr.length;i++){
          if(newUser==arr[i]){
            flag=true;
          }
        }
        if(flag){
          Tools.alert("该产品已关联此投资经理,请重新选择!","danger")
          return false;
        }
      }
        this.httpUtil.comnQuery({
          action: 'T8ProdCustomerInfo.findT8ProdCustomerInfoByJobNo',
          params: {
            jobno: value.newInvestId,
          }
        }).then(data => {

          this.$nextTick(()=>{
            if (data.rows.length>0){
              this.$set(value,"employmentTerm",data.rows[0].employmentTerm);
              this.$set(value,"investEmploymentTerm",data.rows[0].investEmploymentTerm);
              this.$set(value,"pastEmploymentExp",data.rows[0].pastEmploymentExp);
              this.$set(value,"education",data.rows[0].education);
              this.$set(value,"postDate",data.rows[0].postDate);
              this.$set(value,"register",data.rows[0].register);
              if(data.rows[0].isPunish!=undefined&&data.rows[0].isPunish!=null&&data.rows[0].isPunish!=''){
                this.$set(value,"isPunish",data.rows[0].isPunish);
              }else{
                this.$set(value,"isPunish",'0');
              }
              if(data.rows[0].nationality!=undefined&&data.rows[0].nationality!=null&&data.rows[0].nationality!=''){
                this.$set(value,"nationality",data.rows[0].nationality);
              }else{
                this.$set(value,"nationality",'CHN');
              }
              if(data.rows[0].isRegister!=undefined&&data.rows[0].isRegister!=null&&data.rows[0].isRegister!=''){
                this.$set(value,"isRegister",data.rows[0].isRegister);
              }else{
                this.$set(value,"isRegister",'1');
              }

            }
          })
        });


    },
    initAddForm() {
      this.formData = {};
    },
    //一级查询被选中
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      _this.formData = assign({}, row);
    },
    selectStaticTemp(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      _this.formData = assign({}, row);
      this.$refs.investManagerInfoGrid.load({ t8ProdInfoId: _this.selectRowData.t8ProdInfoId });
    },
    beforePopupLoad(params){
      params.parentId = this.queryParentId;
      params.attachmentType=this.attachmentType;
      return params;
    },
    toParams : function(row){
      //console.log("row=:",row)
      // this.attachments.parentId=row.id;
      // console.log("this.attachments.parentId=:",this.attachments.parentId);
      this.attachmentType='10003';
      this.queryParentId = row.id;
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.showSubmitBtn = true;
    },
    onUploadChange(file, fileList) {
      this.fileList = fileList;
    },
    dataChange(file, fileList) {
      this.fileList = fileList;
    },
    httpRequest(file) {
      this.fileData.append("files", file.file);
    },
    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.minutesOfMeetingForm.reset();
      this.$refs.minutesOfMeetingPopup.close();
      this.$refs.minutesOfSeminarForm.reset();
      this.$refs.minutesOfSeminarPopup.close();

      this.$refs.investManagerGrid.load();
    },
    submitUploadParam() {
      this.fileData = new FormData();
      this.$refs.upload.upload(this.extraData);
      this.httpUtil
        .upload({
          url: "/upload-files/server/BaseServer/excel/upload.json",
          formData: this.fileData,
        })
        .then((res) => {
          this.$refs.upload.doReset();
        });
    },
    /*上传会议附件*/
    submitMettingFile() {
      this.uploadData.id = this.formData.id;
      let uploadData = this.uploadData;
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      this.$refs.uploadRef.upload();
      this.fileData.append("params", JSON.stringify(uploadData));
      this.httpUtil
        .upload({
          url: "/upload-files/server/PmsApp/seminarAttachment/upload.json",
          formData: this.fileData,
        })
        .then((res) => {
          Tools.alert(res.data.returnmsg);
          this.showSubmitBtn = true;
          this.onSubmitSuccess();
        }).catch(res => {
        this.showSubmitBtn = true;
      });
    },
    /*上传创意附件*/
    submitSeminaFile() {
      this.uploadData.id = this.formData.id;
      let uploadData = this.uploadData;
      this.showSubmitBtn = false;
      this.fileData = new FormData();
      this.$refs.uploadRef.upload();
      this.fileData.append("params", JSON.stringify(uploadData));
      this.httpUtil
        .upload({
          url: "/upload-files/server/PmsApp/creativeProjectAttachment/upload.json",
          formData: this.fileData,
        }).then((res) => {
        Tools.alert(res.data.returnmsg);
        this.$refs.minutesOfSeminarPopup.close();
        this.showSubmitBtn = true;
        this.onSubmitSuccess();
      }).catch(res => {
        this.showSubmitBtn = true;
      });
    },
    deleteEvent(index) {
      if (this.envItems.length > 1) {
        this.envItems.splice(index, 1);
      }
    },
    openBox() {
      this.formData = {};
      this.envItems = [{}];
    },
    submitHandle(value) {

      let result = true;
      result = this.$refs.addForm1.validate();
      let form2s = this.$refs.addForm1;
      if (form2s && form2s.length > 0) {
        for (let i = 0; i < form2s.length; i++) {
          result = result && form2s[i].validate();
        }
      }
      if (result === false) {
        return false;
      }
      if (this.envItems && this.envItems.length > 0) {
        for (let i = 0; i < this.$refs.addForm2.length; i++) {
          result = this.$refs.addForm2[i].validate();
        }
        if (result === false) {
          return false;
        }
        value.json = JSON.stringify({ envItemsConf: this.envItems });
      }
    },
    editSubmitHandle(value) {
      let result = true;
      result = this.$refs.addForm2.validate();
      let form2s = this.$refs.addForm2;
      if (form2s && form2s.length > 0) {
        for (let i = 0; i < form2s.length; i++) {
          result = result && form2s[i].validate();
        }
      }
      if (result === false) {
        return false;
      }
    },
    addSubmitHandle(value) {
      let result = true;
      result = this.$refs.addForm1.validate();
      let form2s = this.$refs.addForm1;
      if (form2s && form2s.length > 0) {
        for (let i = 0; i < form2s.length; i++) {
          result = result && form2s[i].validate();
        }
      }
      if (result === false) {
        return false;
      }
    },
    changeSubmitHandle(value) {
      let result = true;
      result = this.$refs.addForm3.validate();
      let form2s = this.$refs.addForm3;
      if (form2s && form2s.length > 0) {
        for (let i = 0; i < form2s.length; i++) {
          result = result && form2s[i].validate();
        }
      }
      if (result === false) {
        return false;
      }
    },
  },

  created() {
    // this.global.getProdUser('');
  },

};
</script>

<style scoped>
</style>
