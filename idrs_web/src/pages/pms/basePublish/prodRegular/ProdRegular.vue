<template>
  <div class="md-card k-card md-theme-default parent-div">
    <div class="md-card-header md-card-header-text md-card-header-green" id="mainPanel" style="height:2700px;">
      <div class="display-flex">

        <div :class="state ? 'share-container' : 'share-containerAdd'" v-show="showButton">
          <!--          <div @click="saveProduct1('DisclosureWordDate.addInformation')" size="mini" class="pd-button"-->
          <!--               v-show="true" :data-disabled="saveLoading1">-->
          <!--            <i v-show="saveLoading1" class="el-icon-loading"/>-->
          <!--            <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading1" class="pd-icon-20"/>-->
          <!--            <div v-show="!saveLoading1" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">暂存</div>-->
          <!--          </div>-->

          <div @click="saveProduct('DisclosureWordDate.updateDisclosureModColumn')" size="mini" class="pd-button"
               v-show="true" :data-disabled="saveLoading">
            <i v-show="saveLoading" class="el-icon-loading"/>
            <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading" class="pd-icon-20"/>
            <div v-show="!saveLoading" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">提交</div>
          </div>
        </div>

      </div>
      <div class="formPanel" ref="formPanel">
        <div class="form-item prod-panel" style="display:-webkit-box;" id="baseInfo">
          <div class="title">
            <div class="prod-items"></div>
            <k-field-display class="title-desc" value="模板字段维护"></k-field-display>
          </div>
          <k-form ref="baseInfoForm" :data-col="2" data-input-width="200px" data-label-width="220px">
            <template v-for="(item, index) in formFieldList">
              <k-form-item :key="index" :label="item.columnLabel" v-show="item.isdisplay ==='1' && !(item.columnLabel===null||item.columnLabel===undefined||item.columnLabel==='')">
<!--                <component v-if="item.functype==='k-field-text' && item.dataType != 'textarea'"-->
<!--                           :is="item.functype"-->
<!--                           v-model="filFormData[item.columnKey]"-->
<!--                           :data-default-value="item.columnValue"-->
<!--                           :data-allowblank="isEdit(item)"-->
<!--                           :data-disabled="isEdit(item)"-->
<!--                           :data-dict="item['dict']"-->
<!--                           :data-type="item['dataType']"-->
<!--                           :data-digits="item['dataDigits']"-->
<!--                           :data-validate-type="item['dataType']"-->
<!--                           :data-max-length="item['dataMaxLength']"-->
<!--                           v-bind="item.condition">-->
<!--                </component>-->
<!--                <component v-else-if="item.functype==='k-field-text' && item.dataType==='textarea'"-->
<!--                           :is="item.functype"-->
<!--                           v-model="filFormData[item.columnKey]"-->
<!--                           :data-default-value="item.columnValue"-->
<!--                           :data-allowblank="isEdit(item)"-->
<!--                           :data-disabled="isEdit(item)"-->
<!--                           :input-type="item['dataType']"-->
<!--                           :data-dict="item['dict']"-->
<!--                           :rows="inputType(item)"-->
<!--                           :data-type="item['dataType']"-->
<!--                           :data-digits="item['dataDigits']"-->
<!--                           :data-validate-type="item['dataType']"-->
<!--                           :data-max-length="item['dataMaxLength']"-->
<!--                           v-bind="item.condition">-->
<!--                </component>-->
                <component
                           :is="item.functype"
                           v-model="filFormData[item.columnKey]"
                           :data-allowblank="false"
                           :data-disabled="item.isSysvalue==='1'"
                           :data-default-value="item.columnValue"
                           :data-dict="item['dict']">
                </component>
              </k-form-item>
            </template>
            <k-form-footer data-align="center" v-show="false">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="editBtn"
                     data-from="baseInfoForm" :data-model="filFormData" :data-handler="beforeSubmit">确定
              </k-btn>
            </k-form-footer>
          </k-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DisclosureRegularMajorAsset from "./DisclosureRegularMajorAsset"
import DisclosureNonStandard from "./DisclosureNonStandard"
import DisclosureRegularAsset from "./DisclosureRegularAsset"
import DisclosureRegularShareSort from "./DisclosureRegularShareSort"
import Tools from "@/utils/tools";
import { Decimal } from 'decimal.js'

export default {
  name: "ProdRegular",
  components: {
    DisclosureRegularMajorAsset, DisclosureRegularAsset,DisclosureNonStandard,DisclosureRegularShareSort},
  data() {
    return {
      eidtNonStandard:{},
      eidtFormData:{},
      isShowSelect:false,
      isFormalRole:{roleids:''},
      nonStandardDescRole:{roleids:''},
      isSingle:{roleids:''},
      nonStandardTermDesc:{nonStandardTermDesc:''},
      DisclosureRegularAssetList: [],
      DisclosureNonStandardList: [],
      oldDisclosureRegularAssetList: [],
      DisclosureRegularShareSort: [],
      disclosureList: [],
      amount: 0,//资产总额
      DisclosureRegularAssetGrid: {
        $RatGrid: null//批量修改表格对象
      },
      disclosureNonStandardGrid: {
        $RatGrid: null//批量修改表格对象
      },

      batchUpdateNonStandardGrid: {
        $RatGrid: null//批量修改表格对象
      },
      batchUpdateDisclosureGrid: {
        $RatGrid: null//批量修改表格对象
      },
      formData: {
        DisclosureRegularMajorAsset: {},
        DisclosureRegularAsset: {},
        DisclosureNonStandard: {},
        DisclosureRegularShareSort: {},
      },
      showSubmitBtn: true,
      asset: {
        $AssetGrid: null,
      },
      assetInfo: {
        $AssetInfoGrid: null,
      },
      investmentInfo: {},
      formFieldList: [],
      filFormData: {},
      formFieldInvestList: [],
      filInvestFormData: {is_formal:'',non_standard_desc:''},
      t8DisclosureNoticeId: '',
      t8DisclosureVersionId: '',
      saveLoading: false,//保存按钮是否可点击
      saveLoading1:false,
      state: false,
      //continue: true,
      t8ProdInfoId: '',
      uploadFileName: '',
      realImagePath: '',
      realImagePath2: '',
      prodBaseDate: '',
      prodCode: '',
      currentUserRoles: [],
      inGroup: '',
      prodMode:'',
      showButton: true,
      showInput:true,
      isShareSort: '',
      shareInvestFormData: {},
      shareImagePath: '',
      shareImagePath1: '',
      shareSortList:[],
      shareSortList1:[],
      salesName: '',
      salesCode:'',
      isShowShareImage:true,

      // 单一资产、私募封闭固收、私募封闭非固收的模板出报告时不需要展示对比图
      templateArr:[
        {modelName:'单一资产理财产品',prodDesc:false,investManage:false,asset:false,assetDetail:false,image:false,showtext:false},
        {modelName:'销售文档',prodDesc:false,investManage:false,asset:false,assetDetail:false,image:false,showtext:false},
        {modelName:'公募封闭非固收',prodDesc:true,investManage:true,asset:true,assetDetail:true,image:true,showtext:true},
        {modelName:'公募封闭固收',prodDesc:true,investManage:true,asset:true,assetDetail:true,image:true,showtext:true},
        {modelName:'公募开放非固收',prodDesc:true,investManage:true,asset:true,assetDetail:true,image:true,showtext:true},
        {modelName:'公募开放固收',prodDesc:true,investManage:true,asset:true,assetDetail:true,image:true,showtext:true},
        {modelName:'私募封闭非固收',prodDesc:false,investManage:false,asset:true,assetDetail:false,image:false,showtext:false},
        {modelName:'私募封闭固收',prodDesc:false,investManage:false,asset:true,assetDetail:false,image:false,showtext:false},
        {modelName:'私募开放非固收',prodDesc:true,investManage:true,asset:true,assetDetail:true,image:true,showtext:true},
        {modelName:'私募开放固收',prodDesc:true,investManage:true,asset:true,assetDetail:true,image:true,showtext:true},
        {modelName:'现金模板',prodDesc:true,investManage:true,asset:true,assetDetail:true,image:true,showtext:true}
      ],
      prodDescShow:false,//是否显示产品简介，默认不显示
      investManageShow:false,//是否显示投资经理，默认不显示
      assetShow:false,//是否显示资产配置，默认不显示
      assetDetailShow:false,//是否显示期末十大资产配置，默认不显示
      imagesShow:false,//是否显示净值表现与业绩基准对比，默认不显示
      canUpdate:true, //可修改非标字段
      canFixQX:true,
      showSDZC:false,//十大资产，份额分类权限控制
      showZCPZ:false,//资产配置  权限控制
      showFBZC:false, //非标资产  权限控制
      showtext:true,
      envItems: [{}],
      envItems1:[{}],
    };
  },
  computed: {
    queryParam() {
      return {
        'prodCode': this.prodSearchParam.prodCode,
        'publishStatus': this.prodSearchParam.publishStatus,
        'stage': this.prodSearchParam.stage,
        'disclosureType': this.prodSearchParam.disclosureType,
      }
    }
  },
  created() {
    /*---start--add by zhangchangsi 20220126 显示隐藏模板某些字段*/
    //获取模板名称
    // var templateName = this.$route.query.ruleDocName;
    // console.log('当前模板名称', templateName);

    var modName = this.$route.query.modName;
    console.log('当前模板名称', modName);
    //循环判断那些内容需要显示
    for (var i = 0; i < this.templateArr.length; i++) {
      if (modName.toString().indexOf(this.templateArr[i].modelName) !== -1) {
        console.log('this.templateArr[i]',this.templateArr[i])
        this.prodDescShow = true
        // this.templateArr[i].prodDesc;
        this.investManageShow = true;
        // this.templateArr[i].investManage;
        this.assetShow = true;
        // this.templateArr[i].asset;
        this.assetDetailShow = true;
        // this.templateArr[i].assetDetail;
        this.imagesShow = true;
        // this.templateArr[i].image;
        this.showtext =  true;
        // this.templateArr[i].showtext;
      }
    }

    let t8ProdInfoId = this.$route.query.t8ProdInfoId;
    this.t8ProdInfoId = t8ProdInfoId;
    this.prodMode = this.$route.query.prodMode;
    this.prodBaseDate = this.$route.query.prodBaseDate;
    this.prodCode = this.$route.query.prodCode;
    let disclosureStatus = this.$route.query.disclosureStatus;
    this.disclosureStatus = disclosureStatus;
    let t8DisclosureNoticeId = this.$route.query.t8DisclosureNoticeId;
    this.t8DisclosureNoticeId = t8DisclosureNoticeId;
    let t8DisclosureVersionId = this.$route.query.t8DisclosureVersionId;
    this.t8DisclosureVersionId = t8DisclosureVersionId;
    let disclosureVersion = this.$route.query.disclosureVersion;
    this.disclosureVersion = disclosureVersion;

    //获取系统当前用户
    Tools.getLoginUser().then(res => {
      this.userId = res.userid;
      this.$nextTick(() => {
        //查询可以补录的角色  process表中userid或则touserid为当前登录人的，状态不为1的
        this.httpUtil.comnQuery({
          action: 'DisclosureNotice.findUserInGroup',
          params: {id: this.t8DisclosureVersionId, crtUserId: this.userId},
        }).then(data => {
          if (data.rows.length > 0) {
            data.rows.forEach(e => {
                this.currentUserRoles.push(e.role_id);
              }
            )
          }else{
            this.showButton = true;
            this.showInput  = true;
          }
        });
        this.$nextTick(() => {
          this.httpUtil.comnQuery({
            action: 'DisclosureModColumn.findSupplementaryRecordForDisclosureInfo',
            params: {noticeVersionId: this.t8DisclosureVersionId, isSysvalue: ""},//isSysvalue:2-手工维护 1-自动维护
          }).then(data => {
            if(data!=undefined && data !=null && data !=''){
              this.formFieldList = data.rows

            }
          });

        });

      });
    })
  },
  watch: {
  },
  methods: {

    assetsScaleChange(value,index) {
      let key = this.delcommafy(value.assetsScale);
      //保留两位小数
      key = key.toFixed(2)
      this.$set(this.envItems[index], "assetsScale", key);
      value = key;
      if(this.amount==null){
        this.envItems[index].assetRatio = null;
        Tools.alert("资产配置总资产为空！请配置总资产！")
      } else {
        //科学计数转化
        let assetRatio = Tools.toolNumber(value/this.amount) * 100;
        this.$set(this.envItems[index],'assetRatio',Number(assetRatio).toFixed(2))
        //this.envItems[index].assetRatio = Number(assetRatio).toFixed(2);
        if(isNaN(this.envItems[index].assetRatio)){
          this.envItems[index].assetRatio = null;
        }
        //updateGrid
        //this.$refs.addUpdateDisclosure.refresh();
      }
    },
    updateAssetsScaleChange(value,index) {
      let key = this.delcommafy(value.assetsScale);
      //保留两位小数
      key = key.toFixed(2)
      this.$set(this.envItems1[index], "assetsScale", key);
      value = key;
      if(this.amount==null){
        this.envItems1[index].assetRatio = null;
        Tools.alert("资产配置总资产为空！请配置总资产！")
      } else {
        //科学计数转化
        let assetRatio = Tools.toolNumber(value/this.amount) * 100;
        this.$set(this.envItems1[index],'assetRatio',Number(assetRatio).toFixed(2))
        //this.envItems[index].assetRatio = Number(assetRatio).toFixed(2);
        if(isNaN(this.envItems1[index].assetRatio)){
          this.envItems1[index].assetRatio = null;
        }
      }
    },
    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 50
      if(!isLt10M) {
        Tools.alert("上传文件大小不能超过 50MB!", "danger")
        this.$refs.fileSubmitBtn.setIconStyle(1, []);
        return false;
      }
      return isLt10M;
    },


    isShow(value){
      if(value){
        return true;
      }
      return false;
    },
    putItmValue(item) {
      this.salesName = item.salesName;
      this.salesCode = item.salesCode;
    },
    resetForam(){
      if(this.amount == null || isNaN(this.amount) || this.amount==0){
        Tools.alert("请先配置总资产！","danger")
        return false;
      }
      //点击按钮前重置
      this.eidtFormData = {};
      this.envItems = [{}];
    },
    iefreshInvestment() {
      this.httpUtil.comnUpdate({
        action: "T8ConsignmentFee.findAgencyFee",
        params: {
          id: this.t8DisclosureNoticeId,
          isShareSort: this.isShareSort,
        }
      }).then(data => {
        this.$set(this.filInvestFormData,'gd_sale_fee',data.returndata.money)
      })
    },
    closeBatchUpdateDisclosure() {
      this.$refs.prodBonus.$refs.disclosureRegularMajorAssetGrid.load({t8DisclosureNoticeId: this.t8DisclosureNoticeId})
    },
    closeBatchUpdateDisclosureRegularAsset() {
      this.$refs.RegularMajorAssetGrid.$refs.disclosureRegularAssetGrid.load({
        t8DisclosureNoticeId: this.t8DisclosureNoticeId,
        dataDate: this.prodBaseDate,
        prodCode: this.prodCode
      })
    },
    toPrivateEquityDetail() {
      //跳转到私募资管页面
      this.$router.push({
        path: '/main/pms/disclosureFlow/privateEquityDetail',
        query: {
          prodBaseDate: this.prodBaseDate,
          prodCode: this.prodCode
        }
      });
    },
    saveDisclosureRegularAsset() {
      var list = this.DisclosureRegularAssetGrid.$RatGrid.list;
      //保存私募资管产品金额
      var totalCount = 0;
      //保存私募资管明细金额
      var detailCount = 0;
      for (var i = 0; i < list.length; i++) {
        //获取金额
        var num = Number(list[i].amount);
        if (list[i].assetsType === '私募资管产品') {
          if (!isNaN(num)) {
            totalCount = num;
          }
        }
        if (parseInt(list[i].rowNumbers) > 6) {
          if (!isNaN(num)) {
            //精确计算相加
            detailCount = Tools.numberAdd(detailCount,num);
          }
        }
      }
      if (totalCount != detailCount) {
        Tools.alertTime("私募资管产品金额与私募资管产品明细金额总和不相等,请重新修改填写","danger", 6000)
        return false;
      }
      this.httpUtil.comnUpdate({
        action: "DisclosureRegularAsset.batchUpdateDisclosureRegularAsset",
        params: {
          datas: JSON.stringify(list),
          t8DisclosureNoticeId: this.t8DisclosureNoticeId
        },
        mask: true
      }).then(data => {
        this.showSubmitBtn = true
        if (data.success) {
          //这里时关闭弹窗
          this.$refs.batchUpdateDisclosureRegularAsset.close();
          //重新加载表格数据
          this.$refs.RegularMajorAssetGrid.initData();
        }
      });
    },

    amountInput: function (row) {
      let key = this.delcommafy(row.amount);
      //保留两位小数
      key = key.toFixed(2)
      row.amount = key;
      //资产配置
      if (row.assetsType === '总资产') {
        if (isNaN(row.amount)) {
          this.amount = 0
          return
        }
        this.amount = parseFloat(row.amount)
        //重新计算所有比例
        for (let i = 0; i < this.DisclosureRegularAssetList.length; i++) {
          let temp1 = Number(Number(this.DisclosureRegularAssetList[i].amount / this.amount).toFixed(4) * 100).toFixed(2)
          let temp = Number(temp1).toFixed(2);
          if (isNaN(temp)) {
            this.DisclosureRegularAssetList[i].assetRatio = 0.00
          } else {
            this.DisclosureRegularAssetList[i].assetRatio = temp
          }
        }
        //拿到十大资产初始数据
        this.disclosureList = this.$refs.prodBonus.$refs.disclosureRegularMajorAssetGrid.list
        //如果选择这个总资产 需要动态修改十大资产的百分比
        for (let i = 0; i < this.disclosureList.length; i++) {
          var temp1 = Number(Number(this.disclosureList[i].assetsScale / this.amount).toFixed(4) * 100).toFixed(2)
          let temp = Number(temp1).toFixed(2);
          if (isNaN(temp)) {
            this.disclosureList[i].assetRatio = 0.00
          } else {
            this.disclosureList[i].assetRatio = temp;
          }
        }
      } else {
        //如果选择的不是总资产 需要对总资产就行加减
        this.amount = 0;
        /*for (let i = 0; i < this.oldDisclosureRegularAssetList[0].length; i++) {
          if (row.assetsType === this.oldDisclosureRegularAssetList[0][i].assetsType) {
            //在这里拿到老数据 与新数据进行对比
            var oldAmount = this.oldDisclosureRegularAssetList[0][i].amount
            if (oldAmount < rowAmount) {
              //输入的数据比老数据大 进行累加
              amount += rowAmount - oldAmount
              this.amount = amount
              //将当前输入的值作为老数据
              this.oldDisclosureRegularAssetList[0][i].amount = rowAmount
            } else if (oldAmount > rowAmount) {
              //输入的数据比老数据小  递减
              amount -= oldAmount - rowAmount
              this.amount = amount
              //将当前输入的值作为老数据
              this.oldDisclosureRegularAssetList[0][i].amount = rowAmount
            }
          }
        }*/
        for (let i = 0; i < this.DisclosureRegularAssetList.length; i++) {
          if (this.DisclosureRegularAssetList[i].assetsType !== '总资产' && parseInt(this.DisclosureRegularAssetList[i].rowNumbers) < 7) {
            let tempAmout = this.DisclosureRegularAssetList[i].amount;
            let num = Number(tempAmout);
            if (!isNaN(num)) {
              this.amount += num;
            }
          }
        }
        //把计算好的钱继续绑定到列表 在这里重新赋值
        for (let i = 0; i < this.DisclosureRegularAssetList.length; i++) {
          if (this.DisclosureRegularAssetList[i].assetsType === '总资产') {
            //this.amount = amount
            let tempAmount = Number(this.amount).toFixed(2);
            this.DisclosureRegularAssetList[i].amount = Number(tempAmount).toFixed(2)
          }
        }
        //重新计算所有比例
        for (let i = 0; i < this.DisclosureRegularAssetList.length; i++) {
          //对可能存在的科学计数进行处理
          var assetRatioTemp = Tools.toolNumber(this.DisclosureRegularAssetList[i].amount / this.amount);
          var temp = Number( assetRatioTemp  * 100).toFixed(2)
          if (isNaN(temp) || temp === 'Infinity') {
            this.DisclosureRegularAssetList[i].assetRatio = 0.00
          } else {
            this.DisclosureRegularAssetList[i].assetRatio = temp
          }
        }
      }
    },
    assetRatioChange(row) {
      let key = this.delcommafy(row.assetRatio);
      //保留两位小数
      key = key.toFixed(2)
      row.assetRatio = key;
    },
    delcommafy(val) {//将千分位字符串转为数字返回转化后的数字，非法数字返回0
      if (val) {
        let a = (val +"").replace(/,/g,"");
        let num = Number(a);
        let flag = isNaN(num); //判断是否是非法数字
        if (flag) {
          return 0
        }
        return Number(a)
      }else {
        return 0
      }
    },
    openBatchUpdateDisclosureRegularAsset() {
      //这里是给资产配置弹出框赋值,使用新对象赋值
      this.DisclosureRegularAssetList = JSON.parse(JSON.stringify(this.$refs.RegularMajorAssetGrid.assetEdit));
      //在这里进行对象拷贝
      let tempdata = JSON.parse(JSON.stringify(this.DisclosureRegularAssetList))
      this.oldDisclosureRegularAssetList.push(tempdata)
    },
    openBatchUpdateNonStandard() {
      this.DisclosureNonStandardList = this.$refs.prodBonus1.$refs.disclosureNonStandardGrid.list;

    },
    saveImages() {
      this.$set(this.filInvestFormData, "prodBaseDate", this.prodBaseDate);
      this.$set(this.filInvestFormData, "prodCode", this.prodCode);
      this.$set(this.filInvestFormData, "t8DisclosureNoticeId", this.t8DisclosureNoticeId);
      this.httpUtil.comnQuery({
        action: 'DisclosureWordDate.saveNoticeImages',
        params: this.filInvestFormData,
      }).then(data => {
        let str = data.returndata.realPath;
        if (str != null && str != "" && str != undefined) {
          this.realImagePath = str;
          let imagePath = "";
          if (str.indexOf("/") > -1) {
            let path = str.split("/");
            imagePath = path[path.length - 1];
          } else {
            let path = str.split("\\");
            imagePath = path[path.length - 1];
          }
          //将图片加入缓存
          localStorage.setItem("image_path" + this.t8DisclosureNoticeId, imagePath)
          this.$set(this.filInvestFormData, "image_path", imagePath);
        }
        this.$refs.imageSubmitBtn.setIconStyle(1, []);
      });
    },
    saveImages2() {
      this.$set(this.filInvestFormData, "prodBaseDate", this.prodBaseDate);
      this.$set(this.filInvestFormData, "prodCode", this.prodCode);
      this.$set(this.filInvestFormData, "t8DisclosureNoticeId", this.t8DisclosureNoticeId);
      this.httpUtil.comnQuery({
        action: 'DisclosureWordDate.saveNoticeAreaImages',
        params: this.filInvestFormData,
      }).then(data => {
        let str = data.returndata.realPath;
        if (str != null && str != "" && str != undefined) {
          this.realImagePath2 = str;
          let imagePath2 = "";
          if (str.indexOf("/") > -1) {
            let path = str.split("/");
            imagePath2 = path[path.length - 1];
          } else {
            let path = str.split("\\");
            imagePath2 = path[path.length - 1];
          }
          //将图片加入缓存
          localStorage.setItem("image_path2" + this.t8DisclosureNoticeId, imagePath2)
          this.$set(this.filInvestFormData, "image_path2", imagePath2);
        }
        this.$refs.imageSubmitBtn2.setIconStyle(1, []);
      });
    },
    saveImages3(value) {
      this.httpUtil.comnQuery({
        action: 'DisclosureWordDate.saveShareSortNoticeImages',
        params: {"prodBaseDate":this.prodBaseDate,"prodCode":value.salesCode,"t8DisclosureNoticeId":this.t8DisclosureNoticeId,"shareName":value.salesName}
      }).then(data => {
        for (var i=0;i<this.shareSortList.length;i++) {
          if (value.salesCode == this.shareSortList[i].salesCode) {
            let str = data.returndata.realPath;
            let imagePath = "";
            if (str != null && str != "" && str != undefined) {
              // this.realImagePath = str;
              if (str.indexOf("/") > -1) {
                let path = str.split("/");
                imagePath = path[path.length - 1];
              } else {
                let path = str.split("\\");
                imagePath = path[path.length - 1];
              }
            }
            this.$set(this.shareSortList[i],"shareImagePath",str);
            this.$set(this.shareSortList[i], "share_image_name", imagePath);
          }
          this.$refs.imageSubmitBtn3[i].loading=false;
        }
      });
    },
    saveImages4() {
      this.httpUtil.comnQuery({
        action: 'DisclosureWordDate.saveShareSortNoticeAreaImages',
        params: {"prodBaseDate":this.prodBaseDate,"prodCode":this.salesCode,"t8DisclosureNoticeId":this.t8DisclosureNoticeId,"shareName":this.salesName}
      }).then(data => {
        //将图片加入缓存
        // localStorage.setItem("image_path" + this.t8DisclosureNoticeId, imagePath)
        // this.$set(this.filInvestFormData, "image_path", imagePath);
        for (var i=0;i<this.shareSortList1.length;i++) {
          if (this.salesCode == this.shareSortList1[i].salesCode) {
            let str = data.returndata.realPath;
            let imagePath = "";
            if (str != null && str != "" && str != undefined) {
              //this.realImagePath = str;
              if (str.indexOf("/") > -1) {
                let path = str.split("/");
                imagePath = path[path.length - 1];
              } else {
                let path = str.split("\\");
                imagePath = path[path.length - 1];
              }
            }
            this.$set(this.shareSortList1[i],"shareImagePath2",str);
            this.$set(this.shareSortList1[i], "share_image_name2", imagePath);
          }
          this.$refs.imageSubmitBtn4[i].loading=false;
        }
      });
    },
    onFileSubmitError() {
      //this.filInvestFormData.imagePath='';
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileSubmitBtn.setIconStyle(1, []);
    },
    onFileSubmitSuccess(data) {
      this.realImagePath = data.response.returndata.imagePath;
      this.$set(this.filInvestFormData, "image_path", data.response.returndata.fileName);
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup.close();
    },
    onCompareFileChange(file) {
      let fileName = file.name
      this.uploadFileName = fileName;
    },
    onFileSubmitSuccess2(data) {
      this.realImagePath2 = data.response.returndata.imagePath;
      this.$set(this.filInvestFormData, "image_path2", data.response.returndata.fileName);
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup2.close();
    },
    onFileSubmitSuccess3(data) {
      var fileName = data.response.returndata.fileName;
      var imagePath = data.response.returndata.imagePath;
      for (var i=0;i<this.shareSortList.length;i++) {
        if (this.shareSortList[i].salesName == this.salesName) {
          this.$set(this.shareSortList[i],"shareImagePath",imagePath);
          this.$set(this.shareSortList[i], "share_image_name", fileName);
          break;
        }
      }

      this.$refs.fileUploadRef1.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup3.close();
    },
    onFileSubmitSuccess4(data) {
      var fileName = data.response.returndata.fileName;
      var imagePath = data.response.returndata.imagePath;
      for (var i=0;i<this.shareSortList1.length;i++) {
        if (this.shareSortList1[i].salesName == this.salesName) {
          this.$set(this.shareSortList1[i],"shareImagePath2",imagePath);
          this.$set(this.shareSortList1[i], "share_image_name2", fileName);
          break;
        }
      }

      this.$refs.fileUploadRef2.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup4.close();
    },
    onCompareFileChange2(file) {
      let fileName = file.name
      this.uploadFileName = fileName;
    },
    onCompareFileChange3(file) {
      let fileName = file.name
      this.uploadFileName = fileName;
    },
    onCompareFileChange4(file) {
      let fileName = file.name
      this.uploadFileName = fileName;
    },
    fileSubmitUploadParam() {
      var time = Date.parse(new Date());
      this.filInvestFormData.timePoint = time;
      this.filInvestFormData.disclosureNoticeId = this.t8DisclosureNoticeId;
      let formData = this.filInvestFormData;
      this.$refs.fileUploadRef.upload(formData);
    },
    fileSubmitUploadParam1(){
      var time = Date.parse(new Date());
      this.shareInvestFormData.timePoint = time;
      this.shareInvestFormData.disclosureNoticeId = this.t8DisclosureNoticeId;
      this.shareInvestFormData.areaSection = '0';
      this.shareInvestFormData.shareName = this.salesName;
      let formData = this.shareInvestFormData;
      this.$refs.fileUploadRef1.upload(formData);
    },
    fileSubmitUploadParam2(){
      var time = Date.parse(new Date());
      this.shareInvestFormData.timePoint = time;
      this.shareInvestFormData.disclosureNoticeId = this.t8DisclosureNoticeId;
      this.shareInvestFormData.areaSection = '1';
      this.shareInvestFormData.shareName = this.salesName;
      let formData = this.shareInvestFormData;
      this.$refs.fileUploadRef2.upload(formData);
    },
    addHandler() {
      this.addDisclosureRuleForm = {};
      this.$refs.addDisclosureRulePopup.popup();
    },
    assetsScaleInput(row) {
      let key = this.delcommafy(row.assetsScale);
      //保留两位小数
      key = key.toFixed(2)
      row.assetsScale = key;
      if(this.amount == null){
        row.assetRatio = null;
      } else {
        //实时计算十大 比例
        let temp = Number(Number(row.assetsScale / this.amount).toFixed(4) * 100).toFixed(2);
        row.assetRatio = Number(temp).toFixed(2);
      }

    },
    openUpdateGrid() {
      if(this.amount == null || isNaN(this.amount) || this.amount==0){
        Tools.alert("请先配置总资产！","danger")
        return false;
      }
      //这里是给弹出框赋值  十大资产弹出框
      // this.disclosureList = this.$refs.prodBonus.$refs.disclosureRegularMajorAssetGrid.list;
      // console.log("this.disclosureList=:>>",this.disclosureList);
      this.envItems1 = this.$refs.prodBonus.$refs.disclosureRegularMajorAssetGrid.list;
    },
    //保存非标
    updateNonStandard() {
      this.httpUtil.comnUpdate({
        action: "T8ProdNonStandard.batchUpdateNonStandard",
        params: {
          datas: JSON.stringify(this.batchUpdateNonStandardGrid.$RatGrid.list),
          t8DisclosureNoticeId: this.t8DisclosureNoticeId
        },
        mask: true
      }).then(data => {
        this.showSubmitBtn = true
        if (data.success) {
          //这里时关闭弹窗
          this.$refs.batchUpdateNonStandard.close();
        }
      });
    },

    saveDisclosure() {
      //手动验证表单
      let validate = true;
      //校验新增十大资产数组
      for (let i=0;i<this.envItems1.length;i++) {
        let flag = this.$refs.updateShareSortForm[i].validate();
        if (!flag) {
          validate = false;
          break;
        }
      }
      if (validate) {
        this.httpUtil.comnUpdate({
          action: "DisclosureRegularMajorAsset.batchUpdateDisclosure",
          params: {
            datas: JSON.stringify(this.envItems1),
            t8DisclosureNoticeId: this.t8DisclosureNoticeId
          },
          mask: true
        }).then(data => {
          this.showSubmitBtn = true
          if (data.success) {
            //这里时关闭弹窗
            this.$refs.prodBonus.$refs.disclosureRegularMajorAssetGrid.load({t8DisclosureNoticeId: this.t8DisclosureNoticeId});
            this.$refs.batchUpdateDisclosure.close();
          }
        });
      }
    },
    backDisclosure() {
      //点击取消重新加载表格
      this.$refs.prodBonus.$refs.disclosureRegularMajorAssetGrid.load({t8DisclosureNoticeId: this.t8DisclosureNoticeId})
    },
    saveProduct(url) {
      this.saveLoading = true;
      let flag = false;
      flag = this.$refs.baseInfoForm.validate();
      if (!flag)  {
        this.saveLoading = false;
        Tools.alert("数据未补录完整,请检查后再提交!", "danger")
        return false;
      }

      // if(this.currentUserRoles.length>0){
      //私募金额校验通过，则进行提交流程
      let arr = [];
      let filFormData = this.filFormData;
      arr["userId"] = this.userId;
      arr["prodCode"] = this.prodCode;
      arr["t8DisclosureNoticeId"] = this.t8DisclosureNoticeId;
      arr["t8DisclosureVersionId"] = this.t8DisclosureVersionId;
      arr["disclosureVersion"] = this.disclosureVersion;
      arr['filFormData'] = JSON.stringify(filFormData);
      this.httpUtil.comnUpdate({
        action: url,
        params: arr,
      }).then(data => {
        this.saveLoading = false;
        if (data.success === true) {

          let tabs;
          let data = this.$store.state.system.tab;
          let pathUrl = '/main/pms/disclosureNotice/DisclosureNoticeDetail';

          /**跳转回详情页并关闭补录页*/
          this.$router.push({
            path: pathUrl,
            query: {id:this.$route.query.id,t8ProdInfoId:this.t8ProdInfoId,prodCode:this.prodCode,inGroup:this.inGroup,isInvestmentManager:this.isInvestmentManager},
          });
          tabs = typeof data === 'string' ? JSON.parse(data) : data;
          tabs.forEach((item,index)=>{
            if(item.name === "定期报告数据补录"){
              this.removeTab(item,index)
            }
          })
        }
      });
      // } else{
      //   this.saveLoading = false;
      //   Tools.alert("无权限提交")
      // }
    },
    saveProduct1(url) {
      this.saveLoading1 = true;
      let arr = [];
      let filFormData = this.filFormData;
      arr["userId"] = this.userId;
      arr["t8ProdInfoId"] = this.t8ProdInfoId;
      arr["t8DisclosureNoticeId"] = this.t8DisclosureNoticeId;
      arr['filFormData'] = JSON.stringify(filFormData);
      this.$set(this.filInvestFormData,"realImagePath2",this.realImagePath2);
      this.$set(this.filInvestFormData,"realImagePath",this.realImagePath);
      let filInvestFormData = this.filInvestFormData;
      arr['filInvestFormData'] = JSON.stringify(filInvestFormData);
      this.httpUtil.comnUpdate({
        action: url,
        params: arr,
        successAlert: true
      }).then(data => {
        this.saveLoading1 = false;
      });
    },
    removeTab(tab, index) {
      this.setExInclude(tab,1)
      this.$store.commit("system/setTabSplice",{index:index,count:1});
      if (tab.active) {
        this.$router.push(
          {
            path: this.tab[index - 1].path,
            query: this.tab[index - 1].query
          }
        );
      }

      if(this.tab2.length>0){
        this.$store.commit("system/setTabPush", this.tab2[0]);
        this.$store.commit("system/setTab2Splice", {index:0,count:1});
      }
    },
    setExInclude(tab,type){
      if(tab.meta&&tab.meta.componentName){
        //设置为不缓存
        if(type==1){
          let e=this.$store.state.system.exincludeList
          let a=false
          e.every((item)=>{
            if(item==tab.meta.componentName){
              a=true
              return false
            }else{
              return true
            }
          })
          if(!a){
            e.push(tab.meta.componentName)
            this.$store.commit("system/setExincludeList",e);
          }
        }else{
          //设置为缓存
          let e=this.$store.state.system.exincludeList
          let newArray=e.filter((item)=>{
            return item!=tab.meta.componentName
          })
          this.$store.commit("system/setExincludeList",newArray);
        }
      }
    },
    isEdit(item) {
      if (this.currentUserRoles.length < 0) {
        return true;
      }
      if(item.roleids==null||item.roleids===''||item.roleids===undefined){
        return false;
      }
      //判断当期用户是否有输入权限
      var roleids = item.roleids.split(',');
      //process表中必须有相关人员记录
      if (item.columnKey === 'prod_code') {
        return true;
      }
      if (item.columnKey === 'prod_name') {
        return true;
      }
      if (item.columnKey === 'product_operation_mode') {
        return true;
      }
      if (item.columnKey === 'raise_type') {
        return true;
      }
      if (roleids.some(r => this.currentUserRoles.indexOf(r) >= 0)) {
        return false;
      } else {
        return true;
      }
      if(item.columnLabel===null||item.columnLabel===undefined||item.columnLabel===''){
        return true;
      }
    },


    isEdit1(item) {
      if (this.currentUserRoles.length < 0) {
        return true;
      }

      if(item.roleids==null||item.roleids==''||item.roleids==undefined){
        return false;
      }
      //判断当期用户是否有输入权限
      var roleids = item.roleids.split(',');


      if (roleids.some(r => this.currentUserRoles.indexOf(r) >= 0)) {

        return false;
      } else {

        return true;
      }
    },

    changeData(){
      if(this.filInvestFormData.is_formal=='0'){
        this.$set(this.filInvestFormData,'non_standard_desc','');
      }
    },
    changeSingle(){
      if(this.filInvestFormData.is_single=='0'){
        this.$set(this.filInvestFormData,'non_standard_term_desc','');
      }
    },
    inputType(item){
      if(item['dataType'] == 'textarea'){
        return 5;
      }
    },
    dataInputWidth(item){
      if(item['dataType'] == 'textarea'){
        return "740px";
      }
    },
    beforeSubmit(row) {
      let re = this.$refs.baseInfoForm.validate();
      if (re === true) {
        var param = {}
        param.jsonData = JSON.stringify(row);
        param.t8DisclosureNoticeId = this.t8DisclosureNoticeId;
        this.httpUtil.comnUpdate({
          action: "DisclosureWordDate.updateDisclosureModColumn",
          params: param,
          mask: false
        }).then(res => {
          this.$refs.editBtn.setIconStyle(1, [])
        });
      }
    },
    loadData(id) {
      this.asset.$AssetGrid.load({id: id});
      this.t8DisclosureProdRegularId = id;
      this.httpUtil.comnQuery({
        action: 'DisclosureProdRegular.findInvestmentInfo',
        params: {
          id: id,
        }
      }).then(data => {
        if (data.rows.length > 0) {
          this.investmentInfo = data.rows[0]
        }
      });
    },
    investmentShow(item) {
      /*判断投资信息的字段是否展示*/
      if (item.columnLabel === '产品简介') {
        if (!this.prodDescShow) {
          return false;
        }
      }
      if (item.columnLabel === '投资经理简介') {
        if (!this.investManageShow) {
          return false;
        }
      }
      return item.isdisplay ==='1' && item.columnKey !=='image_path' && item.columnKey !=='image_path2';
    },
    /*删除*/
    deleteEvent(index) {
      if (this.envItems.length > 1) {
        this.envItems.splice(index, 1)
      }
    },
  }
};
</script>
<style lang="scss" scoped>

@import "../../../../styles/T81001.scss";

.add-btn-div {
  position: relative;
  z-index: 1;
}

.add-btn {
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76, 175, 80, 0.14), 0 1px 10px 0 rgba(76, 175, 80, 0.12), 0 2px 4px -1px rgba(76, 175, 80, 0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}

::v-deep .k-form-body {
  max-height: 100%;
}
</style>
