<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ObjectGrid" v-model="prodSearchParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-form-item label="公告状态">
          <k-field-select v-model="prodSearchParam.status" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-form-item label="信息类型">
          <k-field-select v-model="prodSearchParam.msgType" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary">
          <!-- <md-icon>copy</md-icon> -->
          导出
        </k-btn>
        <k-btn slot="button" class="btn-custom-primary">
          <!-- <md-icon>copy</md-icon> -->
          批量初始确认
        </k-btn>
        <k-btn slot="button" class="btn-custom-primary">
          <!-- <md-icon>copy</md-icon> -->
          公告取消
        </k-btn>
      </k-form-search-customize>
      <k-grid ref="t8ObjectGrid" data-action="" :data-checkbox="true" data-checkbox-id="prodCode"
        :dataData="dataList" >
        <k-grid-column data-header="产品代码" data-name="prodCode"/>
        <k-grid-column data-header="产品名称" data-name="prodName"/>
        <k-grid-column data-header="公告名称" data-name="noticeName"/>
        <k-grid-column data-header="当前阶段" data-name="currPhase"/>
        <k-grid-column data-header="信披类型" data-name="type"/>
        <k-grid-column data-header="基准日期 T" data-name="baseDate"/>
        <k-grid-column data-header="系统生成日期" data-name="crtDate"/>
        <k-grid-column data-header="状态" data-name="status"/>
        <k-grid-column data-header="确认人" data-name="confirmUser"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-size="mini" data-target="addPopup"
                 class="btn-custom-plain" data-descript="初始确认" @click="initconfrim(scope.row.row)">
            初始确认
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="addPopup2"
                 class="btn-custom-plain" data-descript="发送托管行审批" style="width: 90px;"
                 @click="sendtg(scope.row.row)" >
            发送托管行审批
          </k-btn>
        </template>
      </k-grid>


      <k-popup ref="addPopup" data-title="信披公告初始确认 ">
        <k-form ref="addForm" :data-col="2">
          <span style="font-size: 15px;font-weight:bold">基本信息</span>
          <k-form-item label="公告标题" :data-col="2">
            <k-field-text v-model="initFormData.title" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品代码">
            <k-field-text v-model="initFormData.prodCode" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披类型">
            <k-field-text v-model="initFormData.type" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品名称">
            <k-field-text v-model="initFormData.prodName" :data-disabled="true"/>
          </k-form-item>
          <!-- <k-form-item label="信披子类">
            <k-field-text v-model="initFormData.itemType" :data-disabled="true"/>
          </k-form-item> -->
          <k-form-item label="信披模板" :data-col="2">
            <k-field-text v-model="initFormData.modelName" :data-disabled="true" style="width: 70%;"/>
            <k-btn class="btn-custom-plain" style="margin-top: -0px;">
              重新生成
            </k-btn>
          </k-form-item>
          <k-form-item label="信披公告" :data-col="2">
            <k-field-text v-model="initFormData.notice" :data-disabled="true" style="width: 70%;"/>
            <k-btn class="btn-custom-plain" data-descript="在线编辑" style="margin-top: -0px;"
                   data-size="small" @click="onlineEditHandler({})"
                   data-functype="POPUP" data-target="onlineEditPopup">
              在线编辑
            </k-btn>
            <k-btn data-functype="DOWNLOAD" data-size="mini" style="margin-top: -0px;"
                   class="btn-custom-plain" data-descript="下载"
                   data-url="/download/server/PmsApp/print/downloadDoc1.json"
                   data-download-name="天利01产品发行公告.docx">
              下载
            </k-btn>
          </k-form-item>
          <k-form-item label="公告完成度" :data-col="2">
            <k-field-text v-model="initFormData.completion" :data-disabled="true"/>
          </k-form-item>
          <span style="font-size: 15px;font-weight:bold">渠道信息</span>
          <k-form-item label="发件邮箱" :data-col="2">
            <k-field-text v-model="initFormData.sendEmail" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披渠道" :data-col="2">
            <el-table :data="tableData" border style="width: 620px;">
              <el-table-column prop="name" label="名称"></el-table-column>
              <el-table-column prop="email" label="邮箱地址"></el-table-column>
              <el-table-column label="操作" width="80">
                <k-btn data-functype="POPUP" data-size="mini" data-target=""
                       class="md-danger md-just-icon md-simple" data-descript="删除">
                  <md-icon>close</md-icon>
                </k-btn>
                <k-btn data-functype="POPUP" data-size="mini" data-target=""
                       class="md-info md-just-icon md-simple" data-descript="添加一行">
                  <md-icon>add</md-icon>
                </k-btn>
              </el-table-column>
            </el-table>
          </k-form-item>
          <span style="font-size: 15px;font-weight:bold">托管行审批</span>
          <k-form-item label=""></k-form-item><!-- 占位 -->
          <k-form-item label="是否需托管行审批">
            <k-field-text v-model="initFormData.isApproval" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="管行审批状态">
            <k-field-text v-model="initFormData.approvalStatus" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披托管行名称">
            <k-field-select v-model="initFormData.trusteeshipName" data-action="T82006.findTaCustodianBanks"
                            data-display-field="truteeName" data-value-field="id"/>
          </k-form-item>
          <k-form-item label="托管行信披邮箱">
            <k-field-text v-model="initFormData.sendTrustEmail" :data-disabled="true"/>
          </k-form-item>


          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                    :data-handler="saveRule"
                   data-from="addForm" :data-model="initFormData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>初始确认
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>公告取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

      <k-popup ref="addPopup2" data-title="托管行审批 ">
        <k-form ref="addForm2" :data-col="2">
          <span style="font-size: 15px;font-weight:bold">发送托管行审批</span>
          <k-form-item label=""></k-form-item><!-- 占位 -->
          <k-form-item label="是否需托管行审批">
            <k-field-text v-model="formData.isApproval" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="管行审批状态">
            <k-field-text v-model="formData.approvalStatus" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披托管行名称">
            <k-field-select v-model="formData.trusteeshipName" data-action="T82006.findTaCustodianBanks"
                            data-display-field="truteeName" data-value-field="id"/>
          </k-form-item>
          <k-form-item label="托管行信披邮箱">
            <k-field-text v-model="formData.sendTrustEmail" :data-disabled="true"/>
          </k-form-item>
          <div style="width: 100%;text-align: center;">
            <k-btn class="btn-custom-plain">
              发送托管行
            </k-btn>
          </div>
          <span style="font-size: 15px;font-weight:bold">托管行审批意见</span>
          <k-form-item label="托管行审批意见" :data-col="2">
            <el-radio v-model="formData.approvalOpinion" label="1">同意</el-radio>
            <el-radio v-model="formData.approvalOpinion" label="2">不同意</el-radio>
          </k-form-item>
          <k-form-item label="托管机构意见" :data-col="2">
            <k-field-text v-model="formData.trusteeshipOpinion" />
          </k-form-item>
          <k-form-item label="托管机构报告" :data-col="2">
            <k-field-text v-model="formData.trusteeshipReport" input-type="textarea" :rows="3"/>
            <span>* 定期报告-半年报、定期报告-年报  托管机构报告为必填</span>
          </k-form-item>
          <div style="width: 100%;text-align: center;">
            <k-btn class="btn-custom-plain">
              确认
            </k-btn>
          </div>
          <span style="font-size: 15px;font-weight:bold">基本信息</span>
          <k-form-item label="公告标题" :data-col="2">
            <k-field-text v-model="formData.title" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品代码">
            <k-field-text v-model="formData.prodCode" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披类型">
            <k-field-text v-model="formData.type" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品名称">
            <k-field-text v-model="formData.prodName" :data-disabled="true"/>
          </k-form-item>
          <!-- <k-form-item label="信披子类">
            <k-field-text v-model="formData.itemType" :data-disabled="true"/>
          </k-form-item> -->
          <k-form-item label="信披模板" :data-col="2">
            <k-field-text v-model="formData.modelName" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披公告" :data-col="2">
            <k-field-text v-model="formData.notice" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="公告完成度" :data-col="2">
            <k-field-text v-model="formData.completion" :data-disabled="true"/>
          </k-form-item>
          <span style="font-size: 15px;font-weight:bold">渠道信息</span>
          <k-form-item label="发件邮箱" :data-col="2">
            <k-field-text v-model="formData.sendEmail" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披渠道" :data-col="2">
            <el-table :data="tableData" border style="width: 620px;">
              <el-table-column prop="name" label="名称"></el-table-column>
              <el-table-column prop="email" label="邮箱地址"></el-table-column>
              <el-table-column label="操作" width="80">
                <k-btn data-functype="POPUP" data-size="mini" data-target=""
                       class="md-danger md-just-icon md-simple" data-descript="删除">
                  <md-icon>close</md-icon>
                </k-btn>
                <k-btn data-functype="POPUP" data-size="mini" data-target=""
                       class="md-info md-just-icon md-simple" data-descript="添加一行">
                  <md-icon>add</md-icon>
                </k-btn>
              </el-table-column>
            </el-table>
          </k-form-item>
          <span style="font-size: 15px;font-weight:bold">提请审批流</span>
          <k-form-item label="">

          </k-form-item>
          <k-form-item label="是否需提请审批">
            <k-field-text v-model="formData.isApproval" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="审批状态">
            <k-field-text v-model="formData.approvalStatus" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="流程审批条件">
            <k-field-text v-model="formData.approvalCondition" :data-disabled="true"/>
          </k-form-item>
        </k-form>
      </k-popup>

      <!--在线编辑弹出框-->
      <k-popup ref="onlineEditPopup" data-width="100%">
        <div class="edit">
          <div class="word">
            <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
          </div>
          <div class="form">
            <div>
              <k-btn data-functype="SUBMIT" :data-handler="saveEditData" class="btn-custom-primary"
                     data-form="setRoleForm">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存数据
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭
              </k-btn>
            </div>
            <div class="form-item" v-for="(item,index) in onlineEditData" :key="index">
              <span class="form-item-span">{{item.wordComment+":"}}</span>
              <k-field-text v-show="item.isDisabled=='1'"  :ref="item.wordKey" @input="itemChange($event,item.wordKey)" v-model="item.wordValue"/>
              <k-field-display v-show="item.isDisabled=='0'"  :value="item.wordValue"/>
            </div>
          </div>
        </div>
      </k-popup>

    </div>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "",
  data() {
    return {
      tableData: [{
                name: '光大银行',
                email:'xxxxxx001@gd.com'
              },{
                name: '代销机构',
                email:'xxxxxx002@gd.com'
              },{
                name: '专户机构',
                email:'xxxxxx003@gd.com'
              }],
      prodSearchParam: {
        prodCode: '',
      },
      viewUrl:'',
      onlineEditData:{},
      initFormData:{
        notice:''
      },
      formData: {
        title: '',
      },
      dataList:{
        rows:[
          {prodCode:'EB2048',prodName:'天利01',noticeName:'天利01产品发行公告',currPhase:'公告生成',type:'发行公告',baseDate:'2021-01-26',crtDate:'2021-01-27',status:'待发布',confirmUser:'信披经理'}
        ]
      },
    }
  },
  watch:{
  },
  created() {
  },
  methods: {
    initconfrim(row){
      this.initFormData.title=row.noticeName;
      this.initFormData.notice=row.noticeName;
      this.initFormData.prodCode=row.prodCode;
      this.initFormData.prodName=row.prodName;
      this.initFormData.type=row.type;
      this.initFormData.completion="12/12";
      this.initFormData.modelName="发行公告模板（定期开放类).docx";
      this.initFormData.sendEmail="xxxxxx@gd.com";
    },
    sendtg(row){
      this.formData.title=row.noticeName;
      this.formData.notice=row.noticeName;
      this.formData.prodCode=row.prodCode;
      this.formData.prodName=row.prodName;
      this.formData.type=row.type;
      this.formData.completion="12/12";
      this.formData.modelName="发行公告模板（定期开放类).docx";
      this.formData.sendEmail="xxxxxx@gd.com";
    },
    saveRule(params){
      Tools.alert("保存成功","success");
      this.$refs.addPopup.close();
      return false;
    },
    saveEditData(){
      Tools.alert("保存成功","success");
      this.$refs.onlineEditPopup.close();
      return false;
    },
    itemChange(value,key){
      document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_"+key+"']").forEach(item=>{
        item.innerHTML=value
      })
    },
    onlineEditHandler(value){
      this.httpUtil.comnQuery({
        action: 'T8OnlineWordValue.getT8OnlineWordValueList',
        params: {
          t8ProdDocumentVersionId:287,
        }
      }).then(data => {
        if (data.rows.length > 0) {
          this.viewUrl = data.rows[0].viewUrl;
          this.onlineEditData = data.rows;
          setTimeout(() => {
            for (let i = 0; i < this.onlineEditData.length; i++) {
              let data = this.onlineEditData[i];
              document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_" + data.wordKey + "']").forEach(item => {
                if (data.wordValue != null && data.wordValue !='' && data.wordValue != 'null'){
                  item.innerHTML = data.wordValue;
                } else if (data.wordValue == 'null') {
                  item.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                }
              })
            }
          }, 3000)
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
  .edit{
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 800px;
    .word{
      width: 70%;
      iframe{
        width: 100%;
        height: 100%;
      }
    }
    .form{
      padding-left: 30px;
      width: 30%;
      overflow-y:auto;
      .form-item{
        display: flex;
        align-items: center;
        margin-bottom: 10px;
        .form-item-span{
          margin-right: 5px;
          width: 100px;
          text-align: left;
        }
        .k-field-text{
          margin-left: 5px;
          width: 200px;
          height: 30px;
        }
      }
    }
  }
</style>
