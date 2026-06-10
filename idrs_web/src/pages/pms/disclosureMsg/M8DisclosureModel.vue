<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ObjectGrid" v-model="prodSearchParam">
        <k-form-item label="模板名称">
          <k-field-text v-model="prodSearchParam.modelName" />
        </k-form-item>
        <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" style="width: 120px;"
               data-target="addPopup" :data-handler="openAddPage">
          信披模板新建
        </k-btn>
      </k-form-search-customize>
      <k-grid ref="t8ObjectGrid" data-action="" :dataData="dataList" @data-row-select="tableDataSelect"
        @data-db-click="tableDataDbClick" :data-display="false">
        <k-grid-column data-header="模板ID" data-name="id"/>
        <k-grid-column data-header="模板名称" data-name="name"/>
        <k-grid-column data-header="版本数" data-name="versionNo"/>
        <k-grid-column data-header="创建时间" data-name="crtDate"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="btn-custom-plain" data-descript="上传新模板">
            上传新模板
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="md-danger" data-descript="停用" >
            停用
          </k-btn>
        </template>
      </k-grid>

      <k-grid ref="t8ObjectGrid2" :dataData="dataList2">
        <k-grid-column data-header="文件ID" data-name="id"/>
        <k-grid-column data-header="文件名称" data-name="name"/>
        <k-grid-column data-header="版本" data-name="versionNo"/>
        <k-grid-column data-header="状态" data-name="status"/>
        <k-grid-column data-header="上传时间" data-name="uploadDate"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="DOWNLOAD" data-size="mini"
                 class="btn-custom-text" data-descript="下载"
                 data-url="/download/server/PmsApp/print/downloadDoc2.json"
                 data-download-name="发行公告模板（定期开放类）.docx">
            下载
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="btn-custom-text" data-descript="生效" :data-disabled="true">
            生效
          </k-btn>
          <k-btn data-functype="POPUP" data-size="mini" data-target="editPopup"
                 class="btn-custom-text" data-descript="停用">
            停用
          </k-btn>
        </template>
      </k-grid>


      <k-popup ref="addPopup" data-title="信披模板新建">
        <k-form ref="addForm" :data-col="2">
          <k-form-item label="产品类型" v-show="false">
            <k-field-select v-model="formData.docType" :data-allowblank="false" data-action="T8Dict.t8PrintDoc"
                            data-display-field="itemval" data-value-field="itemkey"/>
          </k-form-item>

          <k-form-item label="模板类型" v-show="false">
            <k-field-select v-model="formData.tempType" :data-allowblank="false" :data-data="addDocTypeDict"
                            data-value-field="value" data-display-field="value,text"/>
          </k-form-item>
          <k-form-item label="模板ID">
            <k-field-text v-model="formData.id" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="创建时间">
            <k-field-text v-model="formData.crtDate" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="模板名称" :data-col="2">
            <k-field-text v-model="formData.name"/>
          </k-form-item>
          <k-form-item label="版本">
            <k-field-text v-model="formData.versionNo" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="附件" data-ui="element" :data-col="2">
            <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                             data-accept=".docx"
                            :data-auto-upload="false" :data-change="onUploadFileChange"
                            data-upload-url="/upload/server/PmsApp/onlineEdit/uploadPrintTemp.json">
            </k-field-upload>
          </k-form-item>
          <!-- <k-form-item label="文件名称" :data-col="2">
            <k-field-text v-model="formData.fileName" :data-disabled="true"/>
          </k-form-item> -->
          <k-form-item label="信息披露模板审批表" :data-col="2">
            <k-field-text v-model="formData.approvalTable" />
          </k-form-item>
          <k-form-item label="模板字段解析" :data-col="2">
            <k-btn class="btn-custom-plain" style="float: right;" :data-handler="clickLoad">
              字段检测
            </k-btn>
            <k-grid ref="t8ObjectGrid5" :dataData="dataList5" :data-operate-column="false" style="margin-left: -100px;">
              <k-grid-column data-header="序号" data-name="num"/>
              <k-grid-column data-header="字段英文" data-name="englishName"/>
              <k-grid-column data-header="取值类型" data-name="valType"/>
              <k-grid-column data-header="中文名称" data-name="chineseName"/>
              <k-grid-column data-header="补录角色" data-name="supplementRole"/>
              <k-grid-column data-header="填写-参考" data-name="example"/>
            </k-grid>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                    :data-handler="submitUploadParam"
                   data-from="addForm" :data-model="formData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

      <k-popup ref="addDesc" data-title="信披模板详情">
        <k-form ref="addForm" :data-col="2">
          <k-form-item label="产品类型" v-show="false">
            <k-field-select v-model="descformData.docType" :data-allowblank="false" data-action="T8Dict.t8PrintDoc"
                            data-display-field="itemval" data-value-field="itemkey"/>
          </k-form-item>

          <k-form-item label="模板类型" v-show="false">
            <k-field-select v-model="descformData.tempType" :data-allowblank="false" :data-data="addDocTypeDict"
                            data-value-field="value" data-display-field="value,text"/>
          </k-form-item>
          <k-form-item label="模板ID">
            <k-field-text v-model="descformData.id" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="创建时间">
            <k-field-text v-model="descformData.crtDate" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="模板名称" :data-col="2">
            <k-field-text v-model="descformData.name" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="版本">
            <k-field-text v-model="descformData.versionNo" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="附件" data-ui="element" :data-col="2">
            <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                             data-accept=".docx"
                            :data-auto-upload="false" :data-change="onUploadFileChange"
                            data-upload-url="/upload/server/PmsApp/onlineEdit/uploadPrintTemp.json">
            </k-field-upload>
          </k-form-item>
          <!-- <k-form-item label="文件名称" :data-col="2">
            <k-field-text v-model="descformData.fileName" :data-disabled="true"/>
          </k-form-item> -->
          <k-form-item label="信息披露模板审批表" :data-col="2">
            <k-field-text v-model="descformData.approvalTable" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="模板字段解析" :data-col="2">
            <k-btn class="btn-custom-plain" style="float: right;">
              字段检测
            </k-btn>
            <k-grid ref="t8ObjectGrid3" :dataData="dataList3" :data-operate-column="false" style="margin-left: -100px;">
              <k-grid-column data-header="序号" data-name="num"/>
              <k-grid-column data-header="字段英文" data-name="englishName"/>
              <k-grid-column data-header="取值类型" data-name="valType"/>
              <k-grid-column data-header="中文名称" data-name="chineseName"/>
              <k-grid-column data-header="补录角色" data-name="supplementRole"/>
              <k-grid-column data-header="填写-参考" data-name="example"/>
            </k-grid>
          </k-form-item>
        </k-form>
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
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        prodCode: '',
        prodName: '',
        feeJson:'',
        id:'',
        crtDate:'',
        versionNo:'',
        docType: '',
        tempType: '',
        distributorCode: '',
        t8TruteeInfoId: '',
        t8MeetCreateId: '',
        remark: '',
        version: 0,
        onlineUrl: '',
      },
      descformData: {
        prodCode: '',
        prodName: '',
        feeJson:'',
        id:'',
        crtDate:'',
        versionNo:'',
        docType: '',
        tempType: '',
        distributorCode: '',
        t8TruteeInfoId: '',
        t8MeetCreateId: '',
        remark: '',
        version: 0,
        onlineUrl: '',
      },
      dataList:{
        rows:[
          {id:'1',name:'发行公告模板（定期开放类)',versionNo:'V1.0',crtDate:'2021-03-21'},
          {id:'2',name:'定期报告-季报',versionNo:'V1.0',crtDate:'2021-03-23'},
          {id:'3',name:'丰利系列净值报告',versionNo:'V1.0',crtDate:'2021-03-24'},
          {id:'4',name:'公司年报',versionNo:'V1.0',crtDate:'2021-03-26'},
        ]
      },
      dataList2:{
        rows:[
        ]
      },
      dataList3:{
        rows:[
          {num:'1',englishName:'prodName',valType:'自动取值',chineseName:'产品名称',supplementRole:'',example:''},
          {num:'2',englishName:'prodName',valType:'自动取值',chineseName:'产品名称',supplementRole:'',example:''},
          {num:'3',englishName:'setupDate',valType:'自动取值',chineseName:'成立日期',supplementRole:'',example:''},
          {num:'4',englishName:'prodName',valType:'自动取值',chineseName:'产品名称',supplementRole:'',example:''},
          {num:'5',englishName:'prodCode',valType:'自动取值',chineseName:'产品编号',supplementRole:'',example:''},
          {num:'6',englishName:'prodRegister',valType:'自动取值',chineseName:'理财产品登记编码',supplementRole:'',example:''},
          {num:'7',englishName:'prodManager',valType:'自动取值',chineseName:'管理人',supplementRole:'',example:''},
          {num:'8',englishName:'trustee',valType:'自动取值',chineseName:'托管人',supplementRole:'',example:''},
          {num:'9',englishName:'trusteeAccount',valType:'自动取值',chineseName:'托管账户信息',supplementRole:'',example:''},
          {num:'10',englishName:'riskLevel',valType:'自动取值',chineseName:'产品风险星级',supplementRole:'',example:''},
          {num:'11',englishName:'prodType',valType:'自动取值',chineseName:'产品类型',supplementRole:'',example:''},
          {num:'12',englishName:'revenueType',valType:'自动取值',chineseName:'产品收益类型',supplementRole:'',example:''},
          {num:'13',englishName:'operateType',valType:'自动取值',chineseName:'产品运作模式',supplementRole:'',example:''},
          {num:'14',englishName:'collectType',valType:'自动取值',chineseName:'产品募集方式',supplementRole:'',example:''},
          {num:'15',englishName:'investScope ',valType:'需补录',chineseName:'投资者范围',supplementRole:'产品专员',example:'本产品面向不特定社会公众销售，其中个人投资者为经我行风险评估评定为稳健型及以上的个人客户。'},
          {num:'16',englishName:'currencyType',valType:'自动取值',chineseName:'募集币种',supplementRole:'',example:''},
          {num:'17',englishName:'performanceBenchmark ',valType:'自动取值',chineseName:'业绩比较基准',supplementRole:'',example:''},
          {num:'18',englishName:'startBuyPersonal',valType:'自动取值',chineseName:'个人投资者_起点金额',supplementRole:'',example:''},
          {num:'19',englishName:'addUnitPersonal',valType:'自动取值',chineseName:'个人投资者_递增金额',supplementRole:'',example:''},
          {num:'20',englishName:'startBuyOrganization',valType:'自动取值',chineseName:'机构投资者_起点金额',supplementRole:'',example:''},
          {num:'21',englishName:'addUnitOrganization',valType:'自动取值',chineseName:'机构投资者_递增金额',supplementRole:'',example:''},
          {num:'22',englishName:'addUnitPersonal',valType:'自动取值',chineseName:'个人投资者_认/申购追加金额',supplementRole:'',example:''},
          {num:'23',englishName:'addUnitOrganization',valType:'自动取值',chineseName:'机构投资者_认/申购追加金额',supplementRole:'',example:''},
          {num:'24',englishName:'shareRedemptionPersonal',valType:'自动取值',chineseName:'个人投资者_单笔最小赎回份额',supplementRole:'',example:''},
          {num:'25',englishName:'shareRedemptionOrganization',valType:'自动取值',chineseName:'机构投资者_单笔最小赎回份额',supplementRole:'',example:''},
          {num:'26',englishName:'collectStartDate',valType:'自动取值',chineseName:'产品募集期_开始日期',supplementRole:'',example:''},
          {num:'27',englishName:'collectEndDate',valType:'自动取值',chineseName:'产品募集期_结束日期',supplementRole:'',example:''},
          {num:'28',englishName:'setupDate',valType:'自动取值',chineseName:'产品成立日',supplementRole:'',example:''},
          {num:'29',englishName:'collectScale',valType:'自动取值',chineseName:'募集规模',supplementRole:'',example:''},
          {num:'30',englishName:'investmentCycle',valType:'自动取值',chineseName:'投资周期',supplementRole:'',example:''},
          {num:'31',englishName:'fisrtOpenDate',valType:'自动取值',chineseName:'首次开放日',supplementRole:'',example:''},
          {num:'32',englishName:'investmentCycle',valType:'自动取值',chineseName:'开放周期',supplementRole:'',example:''},
          {num:'33',englishName:'receiveDate',valType:'需补录',chineseName:'资金到账日',supplementRole:'运营专员',example:'资者赎回金额于产品开放日后5个工作日内到账，产品开放日至投资者资金到账日期间不计利息。'},
          {num:'34',englishName:'productDuration',valType:'自动取值',chineseName:'产品存续期',supplementRole:'',example:''},
          {num:'35',englishName:'largeRedemptionRate',valType:'自动取值',chineseName:'巨额赎回比例',supplementRole:'',example:''},
          {num:'36',englishName:'subscriptionFee',valType:'自动取值',chineseName:'认购费',supplementRole:'',example:''},
          {num:'37',englishName:'buyShareFee',valType:'自动取值',chineseName:'申购费',supplementRole:'',example:''},
          {num:'38',englishName:'redemptionFee',valType:'自动取值',chineseName:'赎回费',supplementRole:'',example:''},
          {num:'39',englishName:'manageFee',valType:'自动取值',chineseName:'管理费',supplementRole:'',example:''},
          {num:'40',englishName:' trusteeFee',valType:'自动取值',chineseName:'托管费',supplementRole:'',example:''},
          {num:'41',englishName:'dividendWay',valType:'自动取值',chineseName:'分红方式',supplementRole:'',example:''},
          {num:'42',englishName:'elseMemo ',valType:'需补录',chineseName:'其他',supplementRole:'产品专员',example:''},
        ]
      },
      dataList5:{
        rows:[

        ]
      },
    }
  },
  watch:{
  },
  created() {
  },
  methods: {
    clickLoad(){
      this.dataList5.rows.splice(0);
      for (var i = 0; i < this.dataList3.rows.length; i++) {
        this.dataList5.rows.push(this.dataList3.rows[i]);
      }
    },
    tableDataDbClick(row, column, event){
      this.descformData=row;
      this.$refs.addDesc.popup();
    },
    tableDataSelect(row, column, event){
      this.dataList2.rows.splice(0);
      if(row.id == 1){
        this.dataList2.rows.push({id:'1001',name:'发行公告模板（定期开放类).docx',versionNo:'V1.0',status:'有效',uploadDate:'2021-03-21'});
      }else if(row.id == 2){
        this.dataList2.rows.push({id:'1002',name:'定期报告-季报.docx',versionNo:'V1.0',status:'有效',uploadDate:'2021-03-23'});
      }else if(row.id == 3){
        this.dataList2.rows.push({id:'1003',name:'丰利系列净值报告.docx',versionNo:'V1.0',status:'有效',uploadDate:'2021-03-24'});
      }else if(row.id == 4){
        this.dataList2.rows.push({id:'1004',name:'公司年报.docx',versionNo:'V1.0',status:'有效',uploadDate:'2021-03-26'});
      }
    },
    saveRule(params){
      Tools.alert("保存成功","success");
      this.$refs.addPopup.close();
      return false;
    },
    openAddPage(){
      this.dataList5.rows.splice(0);
      this.formData.id='系统自动生成';
      this.formData.crtDate=new Date().toLocaleString();//当前时间
      this.formData.versionNo="V1.1";
      this.formData.docType = '封闭净值公墓';
      this.formData.tempType = '30001';
      this.formData.remark = '';
      this.formData.distributorCode = '';
      this.formData.t8TruteeInfoId = '';
      this.formData.t8MeetCreateId = '';
      this.formData.version = 'V1.0';
    },
    onUploadFileChange(file) {
      let fileName = file.name
      let suffix = fileName.substr(fileName.lastIndexOf('.') + 1);
      if ('docx' != suffix) {
        Tools.alert("只能上传格式为docx类型的文档!","danger");
        this.$refs.uploadRef.doReset();
        return false;
      }
    },
    submitUploadParam() {
      let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
      let docPath = "8080"; //服务器相对地址 8201/xxx/xxx
      let index = urlPath.indexOf(docPath);
      let serverPath = urlPath.substring(0, index);
      console.log("serverPath=:>>>>>",serverPath+"8080");
      let onlineUrl = this.httpUtil.onlineUrl;
      console.log("onlineUrl=:>>>>>",onlineUrl);
      if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
        this.formData.onlineUrl = onlineUrl;
        console.log("onlineUrl=:>>>>>",onlineUrl);
      }else{
        this.formData.onlineUrl = serverPath+"8080";
        console.log("serverPath=:>>>>>",serverPath+"8080");
      }
      let formData = this.formData;
      console.log("formData=:>>>>>",formData);
      let temp = document.getElementsByClassName('upload-demo');
      let lis = temp[0].childNodes[1].childNodes.length;
      if(lis>0){
        this.$refs.uploadRef.upload(formData);
      }else{
        Tools.alert("上传附件不能为空!","danger");
        return false;
      }
    },
  }
}
</script>

<style scoped>

</style>
