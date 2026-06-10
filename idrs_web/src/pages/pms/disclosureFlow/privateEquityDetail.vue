<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8PrivateEquityDetailGrid" v-model="queryParam">
        <k-form-item label="产品代码  ">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"></k-field-select>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
<!--        <k-form-item label="数据日期">-->
<!--          <k-field-date v-model="queryParamDateRange" data-type="daterange"/>-->
<!--        </k-form-item>-->
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" data-target="addT8PrivateEquityDetailPopup"
               :data-handler="addT8PrivateEquityDetailPopup" v-if="global.isShowAuthorityButton('T8PrivateEquityDetail.addT8PrivateEquityDetail')">
          <md-icon md-src="/static/svg/add.svg" />
          新增
        </k-btn>
        <k-btn slot="button" class="btn-custom-plain" data-functype="PAGE"  data-target="/main/pms/disclosureFlow/disclosureAssetPlan">
          <i class="icon-reset" />返回</k-btn>

<!--        </k-btn>-->
<!--        <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-primary"-->
<!--               data-target="addPopup">-->
<!--          <md-icon>cloud_upload</md-icon>-->
<!--          批量导入数据-->
<!--        </k-btn>-->
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8PrivateEquityDetailGrid" data-action="T8PrivateEquityDetail.findT8PrivateEquityDetails1" :data-autoload="false">
        <k-grid-column data-header="主键id" data-name="id" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="理财产品代码" data-name="prodCode" data-width="110"></k-grid-column>
        <k-grid-column data-align="center" data-header="理财产品名称" data-name="prodName" data-width="260"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="私募资管产品代码" data-name="privateProdCode" data-width="140"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="私募资管产品名称" data-name="privateProdName" data-width="180"></k-grid-column>-->
        <k-grid-column data-align="center" data-header="固定收益投资" data-name="fixedIncomeInvest" data-width="110"></k-grid-column>
        <k-grid-column data-align="center" data-header="权益投资" data-name="equityInvest" data-width="120"></k-grid-column>
        <k-grid-column data-align="center" data-header="金融衍生品投资" data-name="financialInvest" data-width="120"></k-grid-column>
        <k-grid-column data-align="center" data-header="商品及其他投资" data-name="otherInvest" data-width="120"></k-grid-column>
        <k-grid-column data-align="center" data-header="公募资管产品" data-name="publicProdInvest" data-width="120"></k-grid-column>
        <k-grid-column data-align="center" data-header="私募资管产品资产合计" data-name="privateTotal" data-width="140"></k-grid-column>
        <k-grid-column data-align="center" data-header="理财产品持有私募资管计划份额" data-name="planShare" data-width="120"></k-grid-column>
        <k-grid-column data-align="center" data-header="理财产品持有私募资管计划净值" data-name="planNet" data-width="120"></k-grid-column>
        <k-grid-column data-align="center" data-header="数据日期" data-name="dataDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建日期" data-name="createDate" data-type="date" :data-hidden="false" data-width="100"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建时间" data-name="createTime" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建人id" data-name="createUserId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建人姓名" data-name="createUserName" :data-hidden="false" data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改私募资管产品明细" data-functype="POPUP"
                 data-size="mini" v-if="global.isShowAuthorityButton('T8PrivateEquityDetail.updateT8PrivateEquityDetail')"
                 data-target="editT8PrivateEquityDetailPopup" :data-handler="editHandler">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8PrivateEquityDetail.deleteT8PrivateEquityDetail" data-size="mini"
                 data-type="danger" data-target="t8PrivateEquityDetailGrid" :data-confirm="true"
                 data-descript="删除私募资管产品明细">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加私募资管产品明细弹出框   -->
    <k-popup ref="addT8PrivateEquityDetailPopup" data-title="新增" class="popClass">
      <k-form ref="addT8PrivateEquityDetailForm" :data-col="2" data-input-width="200px" data-label-width="300px"
              data-total-width="1100px">
        <k-form-item label="主键id" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="理财产品代码">
          <!-- <k-field-text v-model="formData.prodCode" :data-allowblank="false"/>-->
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                          :data-allowblank="false" data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <!--    <k-form-item label="理财产品名称">
              <k-field-text v-model="formData.prodName" :data-allowblank="false"/>
            </k-form-item>-->
        <k-form-item label="私募资管计划代码">
          <k-field-text v-model="formData.privateProdCode" :data-allowblank="false" :data-max-length="32" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="私募资管计划名称">
          <k-field-text v-model="formData.privateProdName" :data-allowblank="false" :data-max-length="128" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="数据日期">
          <k-field-date v-model="formData.dataDate" :data-allowblank="false" data-date-format="yyyy-MM-dd" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="固定收益投资">
          <k-field-text v-model="formData.fixedIncomeInvest"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="12"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="权益投资">
          <k-field-text v-model="formData.equityInvest"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="12"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="金融衍生品投资">
          <k-field-text v-model="formData.financialInvest"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="12"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="商品及其他投资">
          <k-field-text v-model="formData.otherInvest"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="12"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="公募资管产品">
          <k-field-text v-model="formData.publicProdInvest"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="12"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="私募资管产品资产合计">
          <k-field-text v-model="formData.privateTotal"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number" data-disabled/>
        </k-form-item>
        <k-form-item label="理财产品持有私募资管计划份额">
          <k-field-text v-model="formData.planShare"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="理财产品持有私募资管计划净值">
          <k-field-text v-model="formData.planNet"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="创建日期" v-show="false">
          <k-field-text v-model="formData.createDate"/>
        </k-form-item>
        <k-form-item label="创建时间" v-show="false">
          <k-field-text v-model="formData.createTime"/>
        </k-form-item>
        <k-form-item label="创建人id" v-show="false">
          <k-field-text v-model="formData.createUserId"/>
        </k-form-item>
        <k-form-item label="创建人姓名" v-show="false">
          <k-field-text v-model="formData.createUserName"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8PrivateEquityDetail.addT8PrivateEquityDetail"
                 data-from="addT8PrivateEquityDetailForm"
                 :data-model="formData" data-target="t8PrivateEquityDetailGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改私募资管产品明细弹出框   -->
    <k-popup ref="editT8PrivateEquityDetailPopup" data-title="修改" class="popClass">
      <k-form ref="editT8PrivateEquityDetailForm" :data-col="2" data-input-width="200px" data-label-width="300px"
              data-total-width="1100px">
        <k-form-item label="主键id" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="理财产品代码">
          <!-- <k-field-text v-model="formData.prodCode" :data-allowblank="false"/>-->
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                          :data-disabled="true" :data-allowblank="false" data-display-field="prodCode,prodName"
                          data-value-field="prodCode"/>
        </k-form-item>
        <!--     <k-form-item label="理财产品名称">
               <k-field-text v-model="formData.prodName" :data-allowblank="false"/>
             </k-form-item>-->
        <k-form-item label="私募资管计划代码">
          <k-field-text v-model="formData.privateProdCode" :data-allowblank="false" :data-max-length="32" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="私募资管计划名称">
          <k-field-text v-model="formData.privateProdName" :data-allowblank="false" :data-max-length="128" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="数据日期">
          <k-field-date v-model="formData.dataDate" :data-allowblank="false" data-date-format="yyyy-MM-dd" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="固定收益投资">
          <k-field-text v-model="formData.fixedIncomeInvest"
                        :data-allowblank="false"
                        :data-disabled="true"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number" @data-on-change="changeAmount"/>
        </k-form-item>
        <k-form-item label="权益投资">
          <k-field-text v-model="formData.equityInvest"
                        :data-allowblank="false"
                        :data-disabled="true"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="金融衍生品投资">
          <k-field-text v-model="formData.financialInvest"
                        :data-allowblank="false"
                        :data-disabled="true"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="商品及其他投资">
          <k-field-text v-model="formData.otherInvest"
                        :data-allowblank="false"
                        :data-disabled="true"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="公募资管产品">
          <k-field-text v-model="formData.publicProdInvest"
                        :data-allowblank="false"
                        :data-disabled="true"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="私募资管产品资产合计">
          <k-field-text v-model="formData.privateTotal"
                        :data-allowblank="false"
                        :data-disabled="true"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="理财产品持有私募资管计划份额">
          <k-field-text v-model="formData.planShare"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="理财产品持有私募资管计划净值">
          <k-field-text v-model="formData.planNet"
                        :data-allowblank="false"
                        data-regx-text="请输入正确的数字" :data-max-length="17"
                        data-digits="2" data-integer-length="14"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="创建日期" v-show="false">
          <k-field-text v-model="formData.createDate"/>
        </k-form-item>
        <k-form-item label="创建时间" v-show="false">
          <k-field-text v-model="formData.createTime"/>
        </k-form-item>
        <k-form-item label="创建人id" v-show="false">
          <k-field-text v-model="formData.createUserId"/>
        </k-form-item>
        <k-form-item label="创建人姓名" v-show="false">
          <k-field-text v-model="formData.createUserName"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="T8PrivateEquityDetail.updateT8PrivateEquityDetail"
                 data-from="editT8PrivateEquityDetailForm"
                 :data-model="formData" data-target="t8PrivateEquityDetailGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!-- 模板上传 -->
    <k-popup ref="addPopup" title="上传Excels">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="picture" ref="uploadRef" :data-multiple="false" :data-limit=1
                                :data-error="onSubmitError" :data-success="onSubmitSuccess" data-accept=".xlsx,.xls"
                                :data-auto-upload="false"
                                data-upload-url="/upload/server/PmsApp/prod/privateEquityDetail/comn-upload.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="t8PrivateEquityDetailGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import KFieldExcelUpload from '@/components/k-element/k-field-excel-upload/k-field-excel-upload.vue'
import Tools from "@/utils/tools";

export default {
  name: "PrivateEquityDetail",
  components: {KFieldExcelUpload},
  data() {
    return {
      addformData: {
        privateTotal: 0,
        fixedIncomeInvest: 0,
        equityInvest: 0,
        otherInvest: 0,
        publicProdInvest: 0,
        financialInvest: 0
      },
      formData: {
        privateTotal: 0,
        fixedIncomeInvest: 0,
        equityInvest: 0,
        otherInvest: 0,
        publicProdInvest: 0,
        financialInvest: 0
      },
      queryParamDateRange: [],
      prodSearchParam: {
        prodCode: ''
      },
      selectRowData: {},
      privateProdName:'',
      privateProdCode:'',
      dataDate:'',
    };
  },
  computed: {
    queryParam() {
      return {
        'prodName': this.prodSearchParam.prodName,
        'prodCode': this.prodSearchParam.prodCode,
        'dataDateForQuery1': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        'dataDateForQuery2': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        'privateProdCode': this.privateProdCode,
        'privateProdName': this.privateProdName,
        'dataDate':this.dataDate,
      }
    }
  },
  methods: {
    addT8PrivateEquityDetailPopup() {
      this.formData = {};
      this.$set(this.formData,'privateTotal','0');
      this.$set(this.formData,'fixedIncomeInvest','0');
      this.$set(this.formData,'equityInvest','0');
      this.$set(this.formData,'otherInvest','0');
      this.$set(this.formData,'publicProdInvest','0');
      this.$set(this.formData,'financialInvest','0');
      this.$set(this.formData,'dataDate',this.dataDate);
      this.$set(this.formData,'privateProdCode',this.privateProdCode);
      this.$set(this.formData,'privateProdName',this.privateProdName);
    },
    changeAmount(value) {
      //formData.fixedIncomeInvest
      console.log("value=:>>>>", value);
    },
    editHandler(params) {
      this.formData = params;
      return params;
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },

    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      this.$refs.t8PrivateEquityDetailGrid.load({
        'privateProdCode': this.privateProdCode,
        'privateProdName': this.privateProdName,
        'dataDate':this.dataDate,});
    },
    submitUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = this.formData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadRef.upload(formData);
          this.showSubmitBtn = true;
        } else {
          Tools.alert("上传文件不能为空!", "danger");
          this.showSubmitBtn = true;
          return false;
        }
      }
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    }
  },
  created() {
    this.privateProdCode = this.$route.query.privateProdCode;
    this.privateProdName = this.$route.query.privateProdName;
    this.dataDate = this.$route.query.dataDate;
  },
  activated() {
    this.$nextTick(()=>{
      this.$refs.t8PrivateEquityDetailGrid.load({
        'privateProdCode': this.privateProdCode,
        'privateProdName': this.privateProdName,
        'dataDate':this.dataDate,
      });
    });
  },
  mounted() {
    //console.log("mounted。。。")
    //重新加载表格
    this.$refs.t8PrivateEquityDetailGrid.load({
      'privateProdCode': this.privateProdCode,
      'privateProdName': this.privateProdName,
      'dataDate':this.dataDate,
    })
  },
  watch: {
    'formData.prodCode': function (newdata, oldVal) {
      //console.log("PordCode：", newdata)
      this.httpUtil.comnUpdate({
        action: "T8Dict.findAllTaProdInfosByCode",
        params: {
          prodCode:newdata
        },successAlert: false

      }).then(data => {
        //console.log("请求成功",data.returndata.prodeName)
        this.formData.prodName=data.returndata.prodeName
      });
    },
    //这里是监听固定收益  做累加
    'formData.fixedIncomeInvest': function (fixedIncomeInvest, oldVal) {
      if (fixedIncomeInvest == '') {
        this.formData.fixedIncomeInvest = 0
        this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
      } else {
        this.formData.privateTotal = (parseFloat(fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
      }
    },
    'formData.equityInvest': function (equityInvest, oldVal) {
      if (equityInvest == '') {
        this.formData.equityInvest = 0
        this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
      } else {
        this.formData.privateTotal = (parseFloat(equityInvest) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
      }
    },
    'formData.otherInvest': function (otherInvest, oldVal) {
      if (otherInvest == '') {
        this.formData.otherInvest = 0
        this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
      } else {
        this.formData.privateTotal = (parseFloat(otherInvest) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.publicProdInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
      }
    },
    'formData.publicProdInvest': function (publicProdInvest, oldVal) {
      if (publicProdInvest == '') {
        this.formData.publicProdInvest = 0
        this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
      } else {
        this.formData.privateTotal = (parseFloat(publicProdInvest) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.financialInvest)).toFixed(2)
      }
    },
    'formData.financialInvest': function (financialInvest, oldVal) {
      if (financialInvest == '') {
        this.formData.financialInvest = 0
        this.formData.privateTotal = (parseFloat(0) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest)).toFixed(2)
      } else {
        this.formData.privateTotal = (parseFloat(financialInvest) + parseFloat(this.formData.fixedIncomeInvest) + parseFloat(this.formData.equityInvest) + parseFloat(this.formData.otherInvest) + parseFloat(this.formData.publicProdInvest)).toFixed(2)
      }
    },
    'formData.privateTotal': function (privateTotal, oldVal) {
      if (isNaN(privateTotal)) {
        this.formData.privateTotal = 0
      }
    }
  }
};
</script>
<style scoped>
.popClass ::v-deep .el-dialog {
  margin-right: 7%;
}
</style>
