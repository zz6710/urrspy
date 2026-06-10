<template>
  <div>
    <div class="query-div" >
      <k-form ref="t8ProdDocInfoSelect" :data-col="3" :data-model="T8ProdDocMods">
        <k-form-item data-input-width="150px" v-show="false">
          <k-field-text v-model="T8ProdDocMods.prodCode"  data-placeholder="产品代码"/>
        </k-form-item>
        <k-form-item data-input-width="150px" v-show="false">
          <k-field-text v-model="T8ProdDocMods.t8ProdInfoId"  data-placeholder="id"/>
        </k-form-item>
      </k-form>
    </div>
    <div style="min-height:225px;">
      <div class="add-btn-div">
        <div class="add-btn"  @click="addHandler">+</div>
      </div>

      <k-grid ref="t8ProdDocInfoGrid" @data-row-select="selectRow">
        <k-grid-column data-header="numId" data-name="numId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="t8ProdInfoId" data-name="t8ProdInfoId" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="prodCode" data-name="prodCode" :data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="prodName" data-name="prodName"  :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="文档类型" data-name="docType"  :data-hidden="true"></k-grid-column>
<!--        <k-grid-column data-header="会议" data-name="t8MeetCreateId" :data-hidden="true"></k-grid-column>-->
        <k-grid-column data-header="模板子版本" data-name="t8PrintTempVersionId" :data-hidden="true"></k-grid-column>


        <k-grid-column data-header="文档类型" data-name="itemval" ></k-grid-column>
<!--        <k-grid-column data-header="会议" data-name="meetName" ></k-grid-column>-->
        <k-grid-column data-header="模板子版本" data-name="tempName"></k-grid-column>
        <k-grid-column data-header="文档版本" data-name="docVersion"></k-grid-column>
        <k-grid-column data-header="文档描述" data-name="docDesc"></k-grid-column>
        <k-grid-column data-header="关联模板操作时间" data-name="correlationTime"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP" data-size="mini"
                 @click="updateData(scope.row.row)" >
<!--         data-target="editT8ProdDocInfoPopup"     :data-handler="updateData"-->
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="POPUP"  :data-handler="deleteProdDocMods"
                 data-type="danger" data-target="t8ProdDocInfoGrid" :data-confirm="true" data-descript="删除">
            <md-icon>close</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple"
                 data-descript="生成文档预览"
                 data-size="small"

                 @click="previewCreatePrintTemp(scope.row.row)"

                 v-model="scope.row.row">
            <md-icon>library_books</md-icon>
          </k-btn>
         <!-- v-show="docType=='10001'||docType=='20001'||docType=='30001'||docType=='40001'||docType=='50001'||docType=='60001'||docType=='70001'
          ||docType=='10007'||docType=='20007'||docType=='30007'||docType=='40007'||docType=='50007'||docType=='60007'||docType=='70007'" -->
          <k-btn class="md-info md-just-icon md-simple"
                 data-descript="预览文档模板信息"
                 data-size="small"
                 :data-handler="previewPrintTempVersion"
                 data-functype="SUBMIT"
                 v-model="scope.row.row">
            <md-icon>zoom_in</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加产品文档关联表弹出框   -->
    <k-popup ref="addT8ProdDocInfoPopup" data-title="新增">
      <k-form ref="addT8ProdDocInfoForm" :data-col="2" dataLabelWidth="140px" dataInputWidth="210px">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="T8ProdDocModsIn.t8ProdInfoId" />
        </k-form-item>
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="T8ProdDocModsIn.prodCode" />
        </k-form-item>
      <k-form-item label="是否为通用模板" >
        <k-field-radio v-model="T8ProdDocModsIn.isCurrencyTemplate"  :data-data="options" data-on-object  @data-on-change="tempChange"/>
      </k-form-item>
        <!-- 通用模板-->
        <k-form-item label="通用文档类型" v-if="isCurrencyTemplate == 'true'" key="isCurrencyTemplate">
          <k-field-select v-model="T8ProdDocModsIn.docType"
                          data-action="T8Dict.findTempType" :data-params="{'dict':'t8_temp_type_ty'}"
                          data-display-field="itemkey,itemval"  data-value-field="itemkey"
                          @data-on-change="getVersionId(T8ProdDocModsIn.docType)" :data-allowblank="false"></k-field-select>
        </k-form-item>

        <k-form-item label="文档类型" v-if="isCurrencyTemplate == 'false'" key="noCurrencyTemplate">
          <k-field-select v-model="T8ProdDocModsIn.docType"
                          data-action="T8Dict.findTempType" :data-params="{'dict':T8ProdDocMods.docTypeDict}"
                          data-display-field="itemkey,itemval"  data-value-field="itemkey"
                          @data-on-change="getVersionId(T8ProdDocModsIn.docType)" :data-allowblank="false"></k-field-select>
        </k-form-item>

        <k-form-item label="文档类型" v-show="false">
          <k-field-text  v-model="T8ProdDocModsIn.itemval"/>
        </k-form-item>


        <k-form-item label="销售商代码" v-if="distributorCodeBool">
          <k-field-select v-model="T8ProdDocModsIn.distributorCode" data-display-field="distributorCode,distributorName" @data-on-change="getVersionByDistributor"
                          data-value-field="distributorCode"  data-action="T8Dict.findTaProdDistributorInfos" :data-params="{'t8ProdInfoId':T8ProdDocMods.t8ProdInfoId}"
                          :data-allowblank="!distributorCodeBool"/>
        </k-form-item>

        <k-form-item label="托管行" v-if="t8TruteeInfoIdBool">
          <k-field-select v-model="T8ProdDocModsIn.t8TruteeInfoId" data-action="T82006.findTaCustodianBankProd"
                          :data-params="{'t8ProdInfoId':T8ProdDocMods.t8ProdInfoId}" @data-on-change="getVersionByTruteeInfoId"
                        data-display-field="truteeCode,truteeName"  data-value-field="t8TruteeInfoId" :data-allowblank="!t8TruteeInfoIdBool"/>
        </k-form-item>

        <k-form-item label="模板子版本">
          <k-field-select v-model="T8ProdDocModsIn.t8PrintTempVersionId" :data-allowblank="false" :data-data="t8PrintTempVersionIds"
                        data-display-field="tempName"  data-value-field="id"
                          @data-on-change="getDocVersion(T8ProdDocModsIn.t8PrintTempVersionId)"/>
        </k-form-item>

        <k-form-item label="模板子版本名称">
          <k-field-text v-model="T8ProdDocModsIn.tempName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文档版本">
          <k-field-text v-model="T8ProdDocModsIn.docVersion" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文档描述" data-input-width="590px">
          <k-field-text v-model="T8ProdDocModsIn.docDesc" :data-max-length="254" inputType="textarea" :rows="5"/>
        </k-form-item>

        <k-form-footer data-align="center" >
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler = "saveProdDocModsAdd"
                 data-from="addT8ProdDocInfoForm"
                 :data-model="T8ProdDocModsIn" data-target="t8ProdDocInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改产品文档关联表弹出框   -->
    <k-popup ref="editT8ProdDocInfoPopup" data-title="修改">
      <k-form ref="editT8ProdDocInfoForm" :data-col="2" dataLabelWidth="130px" dataInputWidth="220px">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="T8ProdDocModsIn.t8ProdInfoId" />
        </k-form-item>
        <k-form-item label="prodCode" v-show="false">
          <k-field-text v-model="T8ProdDocModsIn.prodCode" />
        </k-form-item>
        <k-form-item label="是否为通用模板" >
        <k-field-radio v-model="T8ProdDocModsIn.isCurrencyTemplate"  :data-data="options" data-on-object  @data-on-change="tempChange"/>
      </k-form-item>
        <!-- 通用模板-->
        <k-form-item label="通用文档类型" v-if="isCurrencyTemplate == 'true'" key="isCurrencyTemplate">
          <k-field-select v-model="T8ProdDocModsIn.docType"
                          data-action="T8Dict.findTempType" :data-params="{'dict':'t8_temp_type_ty'}"
                          data-display-field="itemkey,itemval"  data-value-field="itemkey"
                          @data-on-change="getVersionId(T8ProdDocModsIn.docType)" :data-allowblank="false"></k-field-select>
        </k-form-item>

        <k-form-item label="文档类型" v-if="isCurrencyTemplate == 'false'" key="noCurrencyTemplate">
          <k-field-select v-model="T8ProdDocModsIn.docType"
                          data-action="T8Dict.findTempType" :data-params="{'dict':T8ProdDocMods.docTypeDict}"
                          data-display-field="itemkey,itemval"  data-value-field="itemkey"
                          @data-on-change="getVersionId(T8ProdDocModsIn.docType)" :data-allowblank="false"></k-field-select>
        </k-form-item>

        <k-form-item label="文档类型" v-show="false">
          <k-field-text  v-model="T8ProdDocModsIn.itemval" />
        </k-form-item>
        <k-form-item label="销售商代码"  v-if="distributorCodeBool" >
          <k-field-select v-model="T8ProdDocModsIn.distributorCode" data-display-field="distributorCode,distributorName" @data-on-change="getVersionByDistributor"
                          data-action="T8Dict.findTaProdDistributorInfos" :data-params="{'t8ProdInfoId':T8ProdDocMods.t8ProdInfoId}"
                          data-value-field="distributorCode" :data-allowblank="!distributorCodeBool"/>
        </k-form-item>

        <k-form-item label="托管行"   v-if="t8TruteeInfoIdBool">
          <k-field-select v-model="T8ProdDocModsIn.t8TruteeInfoId" data-action="T82006.findTaCustodianBankProd"
                          :data-params="{'t8ProdInfoId':T8ProdDocMods.t8ProdInfoId}" @data-on-change="getVersionByTruteeInfoId"
                          data-display-field="truteeCode,truteeName"  data-value-field="t8TruteeInfoId" :data-allowblank="!t8TruteeInfoIdBool"/>
        </k-form-item>
        <k-form-item label="模板子版本">
          <k-field-select v-model="T8ProdDocModsIn.t8PrintTempVersionId" :data-allowblank="false" :data-data="t8PrintTempVersionIds"
                          data-display-field="tempName"  data-value-field="id"
                          @data-on-change="getDocVersion(T8ProdDocModsIn.t8PrintTempVersionId)"/>
        </k-form-item>
        <k-form-item label="模板子版本名称">
          <k-field-text v-model="T8ProdDocModsIn.tempName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文档版本">
          <k-field-text v-model="T8ProdDocModsIn.docVersion" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文档描述" data-input-width="590px">
          <k-field-text v-model="T8ProdDocModsIn.docDesc" :data-max-length="254" inputType="textarea" :rows="5"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler = "saveProdDocModsUpt"
                 data-from="editT8ProdDocInfoForm"
                 :data-model="T8ProdDocModsIn" data-target="t8ProdDocInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="onlineEditPopup" data-width="60%"  >
      <div class="edit">
        <div class="word">
          <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
        </div>
      </div>
    </k-popup>





    <k-form  dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-footer data-align="center"  v-show="menuName == 'M81007'">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdAllInfo.updateProdDocMods" :data-handler = "saveProdDocMods"
               :data-model="dataParams" data-target=""  :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
    </k-form>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  export default {
    computed: {
    },
    model: {
      prop: 'T8ProdDocMods',
      event: 'input'
    },
    props:{
      T8ProdDocMods: {},
      prodCode: {
        type: String,
        default: ''
      },
      prodMode: {
        type: String,
        default: ''
      },
      raiseType:{
        type: String,
        default: ''
      },
      menuName:''

    },
    data() {

      return {
        options:[{
          value: 'true',
          label: '是',
        },{
          value: 'false',
          label: '否',
        }],
        viewUrl: '',
        onlineEditData:{},
        T8ProdDocModsIn: {},
        selectRowData: {},
        t8PrintTempVersionIds :[],
        distributorCode:[],
        distributorCodes: {},
        dataParams:[],
        distributorCodeBool : false,
        t8TruteeInfoIdBool:false,
        isCurrencyTemplate:''
      };
    },


    mounted(){

    },
    methods: {
      passDataSuccess(){
        this.$emit('isShowButton', '1')
      },

      getItemval:function(val){
        this.httpUtil.comnQuery({
          action: "T8ProdDocInfo.getMeetName",
          params: {docType: val}
        }).then(data => {
          this.$set(this.T8ProdDocModsIn,"itemval",data.rows[0].itemval);
        })
      },

      updateData : function(val){
        this.t8PrintTempVersionIds=[];

        this.t8TruteeInfoIdBool = false;
        this.distributorCodeBool = false;
        this.getBool(val.docType);
        let params = {
          tempType : val.docType,
        };
        if(val.distributorCode){
          this.$set(params,'distributorCode',val.distributorCode);
        }else if (val.t8TruteeInfoId){
          this.$set(params,'t8TruteeInfoId',val.t8TruteeInfoId);
        }
        this.httpUtil.comnQuery({
          action: "PrintTempVersion.getPrintTempVersionName",
          params: params
        }).then(data => {
          this.t8PrintTempVersionIds = data.rows;
          this.$nextTick(()=>{
            this.$set(this.T8ProdDocModsIn,"t8PrintTempVersionId",val.t8PrintTempVersionId);
            this.$set(this.T8ProdDocModsIn,"docVersion",val.docVersion);
          })
        }).catch({})
        this.T8ProdDocModsIn.t8ProdInfoId = this.T8ProdDocMods.t8ProdInfoId;
        this.T8ProdDocModsIn.prodCode = this.T8ProdDocMods.prodCode;
        this.T8ProdDocModsIn.prodNode = this.T8ProdDocMods.prodNode;
        this.$refs.editT8ProdDocInfoPopup.popup();
      },


      getVersionByDistributor(){
        this.t8PrintTempVersionIds=[];
        this.httpUtil.comnQuery({
          action: "PrintTempVersion.getPrintTempVersionName",
          params: {
            tempType: this.T8ProdDocModsIn.docType ,
            distributorCode:this.T8ProdDocModsIn.distributorCode
          }
        }).then(data => {
          this.t8PrintTempVersionIds = data.rows;
          this.$nextTick(()=>{
            this.$set(this.T8ProdDocModsIn,"t8PrintTempVersionId","");
            this.$set(this.T8ProdDocModsIn,"docVersion","");
          })
        })
      },




      getVersionByTruteeInfoId(){
        this.t8PrintTempVersionIds=[];
        this.httpUtil.comnQuery({
          action: "PrintTempVersion.getPrintTempVersionName",
          params: {
            tempType: this.T8ProdDocModsIn.docType ,
            t8TruteeInfoId:this.T8ProdDocModsIn.t8TruteeInfoId
          }
        }).then(data => {
          this.t8PrintTempVersionIds = data.rows;
          this.$nextTick(()=>{
            this.$set(this.T8ProdDocModsIn,"t8PrintTempVersionId","");
            this.$set(this.T8ProdDocModsIn,"docVersion","");
          })
        })
      },




      getDocVersion(val) {
       if(val==null||val==''){
         return false;
       }
        this.httpUtil.comnQuery({
          action: "PrintTempVersion.getPrintTempVersionName",
          params: {id: val}
        }).then(data => {
          this.$set(this.T8ProdDocModsIn,"docVersion",data.rows[0].docVersion);
          this.$set(this.T8ProdDocModsIn,"tempName",data.rows[0].tempName);
          this.$set(this.T8ProdDocModsIn,"docDesc",data.rows[0].remark);
        }).catch({

        })
      },

      getVersionId(val) {
        this.t8PrintTempVersionIds=[];
        this.httpUtil.comnQuery({
          action: "PrintTempVersion.getPrintTempVersionName",
          params: {tempType: val}
        }).then(data => {
          this.t8PrintTempVersionIds = data.rows;
          this.$nextTick(()=>{
            this.$set(this.T8ProdDocModsIn,"t8PrintTempVersionId","");
            this.$set(this.T8ProdDocModsIn,"docVersion","");
          })
        }).catch({})
        this.t8TruteeInfoIdBool = false;
        this.distributorCodeBool = false;
        this.$set(this.T8ProdDocModsIn,"distributorCode","");
        this.$set(this.T8ProdDocModsIn,"t8TruteeInfoId","");

        this.getBool(val);
        this.getItemval(val);
      },



      getBool(val) {
        if(val == '10003' || val == '20003' || val == '30003' ||
          val == '40003' || val == '50003' || val == '60003' ||
          val == '70003' || val == '90002'|| val == '10103'){
          this.distributorCodeBool = true;
        }

        if(val == '10002' || val == '20002' || val == '30002' ||
          val == '40002' || val == '50002' || val == '60002' ||
          val == '70002' || val == '90001'|| val == '10102'){
          this.t8TruteeInfoIdBool = true;
        }

      },

      previewCreatePrintTemp(value){

      this.httpUtil.comnQuery({
          action: 'T8OnlineWordValue.valadationRiskNum',
          params: {
            prodCode: value.prodCode,
            t8PrintTempVersionId:value.t8PrintTempVersionId,
            docType: value.docType
          }
        }).then(data => {
          if(data.success==false){
             Tools.alert(data.returnmsg,"danger");
          }else{
             //data-functype="POPUP" data-target="onlineEditPopup"
             this.$refs.onlineEditPopup.popup();
      this.httpUtil.comnQuery({
          action: 'T8OnlineWordValue.findDataInfo',
          params: {
            prodCode: value.prodCode,
            t8PrintTempVersionId:value.t8PrintTempVersionId,
            docType: value.docType
          }
        }).then(data => {
          if (data.rows.length > 0) {
            this.viewUrl = data.rows[0].viewUrl;
            this.onlineEditData = data.rows;

           setTimeout(() => {
              for (let i = 0; i < this.onlineEditData.length; i++) {
                let data = this.onlineEditData[i];
                console.log(data.wordKey+"---------------------"+data.wordValue);
                document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_" + data.wordKey + "']").forEach(item => {
                  var val = data.wordValue;

                  if (val != null && val.trim() !='' && val != 'null'){
                    //将java换行符替换成html换行符
                    //val = val.replaceAll("\n","<br/>");
                    /!*使用半角空格替换java的空格*!/
                    //val = val.replaceAll(" ", "&ensp;");
                    var reg = new RegExp( "\n" , "g" );
                    var reg1 = new RegExp( " " , "g" );
                    val = val.replace(reg, "<br/>");
                    val = val.replace(reg1, "&ensp;");

                    item.innerHTML = val;
                  } else{
                    item.innerHTML = "";
                  }
                })
              }
             }, 3000)
          }
        });
          }
        })


      },

previewPrintTempVersion(value){
        this.httpUtil.comnQuery({
          action:'T8OnlineWordValue.getMaxT8OnlineWordValueByT8PrintTempVersionId1',
          params: {t8PrintTempVersionId:value.t8PrintTempVersionId,}
        }).then(data => {
          this.$nextTick(()=>{
            if (data != null && data.rows.length > 0){
              let url = data.rows[0].viewUrl;
              window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
            }
          })
        }).catch({

        })
        return false;

      },

      validateData() {
        return this.$refs.addT8ProdDocInfoForm.validate();
      },
      addHandler(){
        if(this.T8ProdDocMods.prodCode == '' || this.T8ProdDocMods.prodCode == null){
          Tools.alert("正在获取产品信息，稍后重试 !","danger")
          return false;
        }

        this.T8ProdDocModsIn = {};
        this.T8ProdDocModsIn.t8ProdInfoId = this.T8ProdDocMods.t8ProdInfoId;
        this.T8ProdDocModsIn.prodCode = this.T8ProdDocMods.prodCode;
        this.T8ProdDocModsIn.prodNode = this.T8ProdDocMods.prodNode;
        this.$refs.addT8ProdDocInfoPopup.popup();
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.T8ProdDocModsIn = Object.assign({}, row)
      },


     tempChange(value){
        this.isCurrencyTemplate=value
        this.T8ProdDocModsIn.isCurrencyTemplate=value
       this.$set(this.T8ProdDocModsIn,"docType","");
        this.$set(this.T8ProdDocModsIn,"itemval","");
        this.$set(this.T8ProdDocModsIn,"distributorCode","");
        this.$set(this.T8ProdDocModsIn,"t8TruteeInfoId","");
        this.$set(this.T8ProdDocModsIn,"t8PrintTempVersionId","");
        this.$set(this.T8ProdDocModsIn,"tempName","");
        this.$set(this.T8ProdDocModsIn,"docVersion","");
        this.$set(this.T8ProdDocModsIn,"docDesc","");
      },


      saveProdDocMods(val){
        val["assemblyMenuType"] = 'prodDocInfo';
        val["t8ProdDocMods"] = JSON.stringify(this.dataParams);
        val["prodCode"] = this.prodCode;
        val["t8ProdInfoId"] = this.T8ProdDocMods.t8ProdInfoId;
        console.log(this.prodCode)
      },

      saveProdDocModsAdd(val){
        let validateResult = this.$refs.addT8ProdDocInfoForm.validate();
        if(!validateResult){
          return false;
        }
        //let aData = new Date()
        // console.log(dateTime)
        // let Mo = aData.getMonth() + 1;
        // let Da = aData.getDate();
        // let Ho =aData.getHours(); //获取系统时，
        // let Mi =aData.getMinutes(); //分
        // let Se =aData.getSeconds(); //秒
        // Mo = Mo < 10 ? "0" + Mo : Mo;
        // Da = Da < 10? "0" + Da : Da;
        //val.correlationTime=aData.getFullYear() + "-" + Mo + "-" + Da + " " + Ho + ":" + Mi + ":" +Se
        val.correlationTime=new Date().Format("yyyy-MM-dd hh:mm:ss");
        console.log(val)
        this.dataParams.push(val);
        this.uptDataParams(this.dataParams);
        this.$refs.addT8ProdDocInfoPopup.close();
      },

      saveProdDocModsUpt(val){
        let validateResult = this.$refs.editT8ProdDocInfoForm.validate();
        if(!validateResult){
          return false;
        }
        this.$delete(this.dataParams,val.numId);
        this.dataParams.push(val);
        this.uptDataParams(this.dataParams);
        this.$refs.editT8ProdDocInfoPopup.close();
      },

      deleteProdDocMods(val){
        this.$delete(this.dataParams,val.numId);
        this.uptDataParams(this.dataParams);
      },



      uptDataParams(dataParams){
        for(let i = 0;i < dataParams.length; i++){
          this.$set(dataParams[i],'numId',i)
        }
        this.dataParams = dataParams;
        this.$set(this.$refs.t8ProdDocInfoGrid,'list',this.dataParams);
      },

    },

    watch: {
      'T8ProdDocMods.dataParams' : function (value) {
        this.uptDataParams(value);
      }
    },
  };
</script>
<style lang="scss" scoped>
.add-btn-div{
  position: relative;
  z-index: 1;
}
.add-btn{
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76,175,80,0.14), 0 1px 10px 0 rgba(76,175,80,0.12), 0 2px 4px -1px rgba(76,175,80,0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}
.edit{
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 600px;
  .word{
    width: 70%;
    iframe{
      width: 100%;
      height: 100%;
    }
  }
  .form{
    padding-left: 20px;
    width: 40%;
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
        width: 300px;
        height: 30px;
      }
    }
  }
}
</style>
