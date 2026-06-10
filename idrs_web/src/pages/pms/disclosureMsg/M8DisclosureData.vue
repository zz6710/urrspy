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
      </k-form-search-customize>
      <k-grid ref="t8ObjectGrid" :dataData="dataList" >
        <k-grid-column data-header="产品代码" data-name="prodCode"/>
        <k-grid-column data-header="产品名称" data-name="prodName"/>
        <k-grid-column data-header="公告名称" data-name="noticeName"/>
        <k-grid-column data-header="当前阶段" data-name="currPhase"/>
        <k-grid-column data-header="信披类型" data-name="type"/>
        <k-grid-column data-header="基准日期 T" data-name="baseDate"/>
        <k-grid-column data-header="系统生成日期" data-name="crtDate"/>
        <k-grid-column data-header="补录状态" data-name="supplementStatus"/>
        <k-grid-column data-header="审批状态" data-name="approvalStatus"/>
        <k-grid-column data-header="补录人员" data-name="supplementUser"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-size="mini" data-target="addPopup"
                 class="btn-custom-text" data-descript="补录" :data-handler="openAddPopup(scope.row.row)">
            补录
          </k-btn>
          <k-btn data-functype="DOWNLOAD" data-size="mini"
                 class="btn-custom-text" data-descript="下载"
                 data-url="/download/server/PmsApp/print/downloadDoc1.json"
                 data-download-name="天利01产品发行公告.docx">
            下载
          </k-btn>
        </template>
      </k-grid>


      <k-popup ref="addPopup" data-title="信披公告">
        <k-form ref="addForm" :data-col="2">
          <span style="font-size: 15px;font-weight:bold">基本信息</span>
          <k-form-item label="公告标题" :data-col="2">
            <k-field-text v-model="noticeFormData.title" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品ID">
            <k-field-text v-model="noticeFormData.prodCode" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披类型">
            <k-field-text v-model="noticeFormData.type" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品名称">
            <k-field-text v-model="noticeFormData.prodName" :data-disabled="true"/>
          </k-form-item>
          <!-- <k-form-item label="信披子类">
            <k-field-text v-model="noticeFormData.itemType" :data-disabled="true"/>
          </k-form-item> -->
          <k-form-item label="信披模板" :data-col="2">
            <k-field-text v-model="noticeFormData.model" :data-disabled="true" style="width: 80%;"/>
          </k-form-item>
          <k-form-item label="信披公告" :data-col="2">
            <k-field-text v-model="noticeFormData.notice" :data-disabled="true" style="width: 65%;"/>
            <k-btn class="btn-custom-plain" data-descript="在线编辑" style="margin-top: -0px;"
                   data-size="small" @click="onlineEditHandler({})"
                   data-functype="POPUP" data-target="onlineEditPopup">
              在线编辑
            </k-btn>
          </k-form-item>
          <k-form-item label="公告完成度" :data-col="2">
            <k-field-text v-model="noticeFormData.completion" :data-disabled="true"/>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="POPUP"
                   data-target="addPopup3">
              转发补录
            </k-btn>
            <k-btn class="btn-custom-primary"
                    :data-handler="saveRule"
                   data-from="addForm" :data-model="noticeFormData"
                   data-target="t8ObjectGrid">
              提交
            </k-btn>
          </k-form-footer>
          <span style="font-size: 15px;font-weight:bold">补录说明</span>
          <k-form-item label=" " :data-col="2">
            <el-table :data="tableData" border style="width: 820px;">
              <el-table-column prop="column1" label="操作时间" ></el-table-column>
              <el-table-column prop="column2" label="发起人"></el-table-column>
              <el-table-column prop="column3" label="计划完成日期"></el-table-column>
              <el-table-column prop="column4" label="补录说明"></el-table-column>
              <el-table-column prop="column5" label="附件"></el-table-column>
            </el-table>
          </k-form-item>
        </k-form>
      </k-popup>

      <k-popup ref="addPopup2" data-title="信披数据收集 ">
        <k-form ref="addForm2" :data-col="2">
          <span style="font-size: 15px;font-weight:bold">基本信息</span>
          <k-form-item label="公告标题" :data-col="2">
            <k-field-text v-model="formData.title" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品ID">
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
            <k-field-text v-model="formData.model" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="信披公告" :data-col="2">
            <k-field-text v-model="formData.notice" :data-disabled="true" style="width: 80%;"/>
            <k-btn class="btn-custom-plain" data-descript="在线编辑" style="margin-top: -0px;"
                   data-size="small" @click="onlineEditHandler({})"
                   data-functype="POPUP" data-target="onlineEditPopup">
              在线编辑123
            </k-btn>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                    :data-handler="saveRule"
                   data-from="addForm" :data-model="formData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>提交
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

      <k-popup ref="addPopup3" data-title="转发用户 ">
        <k-form ref="addForm3" :data-col="1">
          <k-form-item label="选择转发用户">
            <k-field-select v-model="formData.leadername" data-action="User.findUsers"
                          data-value-field="userid"
                          data-display-field="username"  :dataAllowblank="false"  />
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                    :data-handler="saveRule"
                   data-from="addForm" :data-model="formData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>提交
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
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
  name: "M8DisclosureData",
  data() {
    return {
      tableData: [{
                column1: '2021-03-29',
                column2:'系统',
                column3:'2021-03-29',
                column4:'系统生成待补录',
                column5:'',
              }],
      prodSearchParam: {
        prodCode: '',
        status:'',
        msgType:'',
      },
      noticeFormData: {

      },
      viewUrl:'',
      onlineEditData:{},
      formData:{

      },
      modelData:[
        {text:'1',value:"待提供数据"},
      ],
      dataList:{
        rows:[
          {prodCode:'EB2048',prodName:'天利01',noticeName:'天利01产品发行公告',currPhase:'03-报告检查',type:'发行公告',baseDate:'2020/1/26',crtDate:'2020/1/27',supplementStatus:'未补录',approvalStatus:'未审批',supplementUser:'admin'}
        ]
      },
    }
  },
  watch:{
  },
  created() {
  },
  methods: {
    openAddPopup(row){
      this.noticeFormData=row;
      this.noticeFormData.title='天利01产品发行公告';
      this.noticeFormData.model='发行公告模板（定期开放类)';
      this.noticeFormData.notice='天利01产品发行公告.docx';
      this.noticeFormData.completion='12/12';
    },
    saveRule(params){
      Tools.alert("操作成功","success");
      this.$refs.addPopup3.close();
      this.$refs.addPopup.close();
      return false;
    },
    saveEditData(){
      Tools.alert("保存成功","success");
      this.$refs.onlineEditPopup.close();
      return false;
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
    },
    itemChange(value,key){
      document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_"+key+"']").forEach(item=>{
        item.innerHTML=value
      })
    },
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
