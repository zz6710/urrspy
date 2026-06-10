<template>
  <div>
      <k-form-search-customize data-target="netWorthGrid" v-model="netWorthSearchParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="netWorthSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="netWorthSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="净值日期"  data-input-width="225px" data-label-width="120px">
          <k-field-date v-model="navDateRange" data-type="daterange"/>
        </k-form-item>
        <k-form-item label="是否份额分类">
          <k-field-select v-model="netWorthSearchParam.isShareSort" data-dict="1yes0no" />
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addnetWorthPopup" v-if="global.isShowAuthorityButton('NetWorth.addnetWorth')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>

        <k-btn slot="button" class="btn-custom-primary" style="width: 100px" data-functype="POPUP"
               data-target="syncNetWorthPopup" @click="findSystemTime" v-if="global.isShowAuthorityButton('NetWorth.syncNav')">
          <md-icon md-src="/static/svg/add.svg" />净值同步</k-btn>
        <k-btn slot="button" class="btn-custom-primary" style="width: 100px" data-functype="POPUP"
               data-target="addPopup" v-if="global.isShowAuthorityButton('NetWorth.syncNav')">
          <md-icon md-src="/static/svg/add.svg" />上传净值文件</k-btn>
      </k-form-search-customize>

    <div>
      <k-grid ref="netWorthGrid" @data-row-select="selectRow" data-action="NetWorth.findnetWorths1" >
    <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="98"></k-grid-column>
    <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="260"></k-grid-column>
		<k-grid-column data-align="center" data-header="净值日期" data-name="navDate" data-width="98"></k-grid-column>
		<k-grid-column data-align="center" data-header="基金总净值" data-name="totalNet" data-width="148"></k-grid-column>
		<k-grid-column data-align="center" data-header="基金总份额" data-name="totalVol" data-width="148"></k-grid-column>
		<k-grid-column data-align="center" data-header="基金单位净值" data-name="nav" data-width="148"></k-grid-column>
		<k-grid-column data-align="center" data-header="基金收益" data-name="navProfit" data-width="148"></k-grid-column>
		<k-grid-column data-align="center" data-header="万份基金收益" data-name="tenThousandIncomeAmt" data-width="148"></k-grid-column>
		<k-grid-column data-align="center" data-header="近七日年化收益率" data-name="sevenDaysIncomeRate" data-width="148"></k-grid-column>
		<k-grid-column data-align="center" data-header="销售服务费" data-name="saleServiceFee" data-width="88"></k-grid-column>
		<k-grid-column data-align="center" data-header="基金累计净值" data-name="totalNav" data-width="98"></k-grid-column>
		<k-grid-column data-align="center" data-header="净值增长率" data-name="navGrowthRate" data-width="108"></k-grid-column>
        <template slot="operate" slot-scope="scope">

          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品净值" data-functype="POPUP" data-size="mini"
                 data-target="editnetWorthPopup" v-if="global.isShowAuthorityButton('NetWorth.updatenetWorth')"
                 v-show="scope.row.row.isParentProd == '1'">
            <md-icon>edit</md-icon>
          </k-btn>

          <k-btn class="md-info md-just-icon md-simple" data-descript="修改子份额净值" data-functype="POPUP" data-size="mini"
                 data-target="editnetWorthPopup" v-if="global.isShowAuthorityButton('NetWorth.updatenetWorth')"
                 v-show="scope.row.row.isParentProd == '0'">
            <md-icon>edit</md-icon>
          </k-btn>

          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="NetWorth.deletenetWorth" data-size="mini"
               data-type="danger" data-target="netWorthGrid" :data-confirm="true" data-descript="删除产品净值" v-show="scope.row.row.isParentProd == '1'">
          	<md-icon>close</md-icon>
    	  </k-btn>

          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="NetWorth.deletenetWorth" data-size="mini"
                 data-type="danger" data-target="netWorthGrid" :data-confirm="true" data-descript="删除子份额净值" v-show="scope.row.row.isParentProd == '0'">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加净值表描述弹出框   -->
	<k-popup ref="addnetWorthPopup" data-title="新增">

    	<k-form ref="addnetWorthForm" :data-col="2">
        <k-form-item label="是否份额分类">
          <k-field-select v-model="formData.isShareSort" data-dict="1yes0no" data-default-value="0" @data-on-change="changedCode"/>
        </k-form-item>
        <k-form-item label="产品名称" v-if="!shareFlag">
            <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos" :key="'prodCode1'"
                            data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false" :disabled = "true"/>
        </k-form-item>
        <k-form-item label="产品名称" v-if="shareFlag">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findSonProdInfos" :key="'prodCode2'"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false" :disabled = "true"/>
        </k-form-item>
        <k-form-item label="净值日期">
          <k-field-date v-model="formData.navDate" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
        </k-form-item>

        <k-form-item label="基金总净值">
          <k-field-text v-model="formData.totalNet"
                        data-regx-text="请输入正确的数字" :data-allowblank="false"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="基金总份额">
          <k-field-text v-model="formData.totalVol"
                        data-regx-text="请输入正确的数字" :data-allowblank="false"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="基金单位净值">
          <k-field-text v-model="formData.nav"
                        data-regx-text="请输入正确的数字" :data-allowblank="false"
                        data-digits="6"  data-integer-length="9"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="基金收益">
          <k-field-text v-model="formData.navProfit"
                        data-regx-text="请输入正确的数字"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="万份基金收益">
          <k-field-text v-model="formData.tenThousandIncomeAmt"
                        data-regx-text="请输入正确的数字"
                        data-digits="4"  data-integer-length="11"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="近七日年化收益率">
          <k-field-text v-model="formData.sevenDaysIncomeRate"
                        data-regx-text="请输入正确的数字" :data-max-length="10"
                        data-digits="4"  data-integer-length="4"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="销售服务费">
          <k-field-text v-model="formData.saleServiceFee"
                        data-regx-text="请输入正确的数字"
                        data-digits="2"  data-integer-length="13"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="基金累计净值">
          <k-field-text v-model="formData.totalNav"
                        data-regx-text="请输入正确的数字"
                        data-digits="4"  data-integer-length="11"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
        <k-form-item label="净值增长率">
          <k-field-text v-model="formData.navGrowthRate"
                        data-regx-text="请输入正确的数字"
                        data-digits="6"  data-integer-length="1"
                        data-validate-type="number" data-type="number"/>
        </k-form-item>
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="NetWorth.addnetWorth" data-from="addnetWorthForm"
		               :data-model="formData" data-target="netWorthGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

    <!--    选择净值日期弹出框   -->
    <k-popup ref="syncNetWorthPopup" data-title="净值日期">
     <k-form ref="syncNetWorthForm" :data-col="2" :rules="rules">

      <k-form-item label="净值日期">
        <k-field-date v-model="formData.navDate" data-date-format="yyyy-MM-dd" :data-allowblank="false" :data-default-value="date.navDate"/>
      </k-form-item>
       <k-form-footer data-align="center">
         <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ScheduleNav.syncNav" data-from="syncNetWorthForm"
                :data-model="formData" data-target="netWorthGrid" :data-confirm="true" data-descript="同步当前净值日期数据">
           <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
         </k-btn>
         <k-btn class="btn-custom-plain" data-functype="CLOSE">
           <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
       </k-form-footer>
     </k-form>
    </k-popup>

    <!--上传附件弹出框-->
    <k-popup ref="addPopup" data-title="上传净值文件">
      <k-form ref="addForm" data-ui="element" >
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/netWorth/uploadNetWorth.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="submitBtn"
                 data-from="addForm" :data-model="formData" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

	<!--    修改净值表描述弹出框    -->
	<k-popup ref="editnetWorthPopup" data-title="修改">
	  <k-form ref="editnetWorthForm" :data-col="2">
    <k-form-item label="产品名称">
        <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
		<k-form-item label="净值日期">
        	<k-field-date v-model="formData.navDate" :data-allowblank="false" data-date-format="yyyy-MM-dd" :data-disabled="true"/>
     	</k-form-item>

      <k-form-item label="基金总净值">
        <k-field-text v-model="formData.totalNet"
                      data-regx-text="请输入正确的数字" :data-allowblank="false"
                      data-digits="2"  data-integer-length="13"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="基金总份额">
        <k-field-text v-model="formData.totalVol"
                      data-regx-text="请输入正确的数字" :data-allowblank="false"
                      data-digits="2"  data-integer-length="13"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="基金单位净值">
        <k-field-text v-model="formData.nav"
                      data-regx-text="请输入正确的数字" :data-allowblank="false"
                      data-digits="2"  data-integer-length="13"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="基金收益">
        <k-field-text v-model="formData.navProfit"
                      data-regx-text="请输入正确的数字"
                      data-digits="2"  data-integer-length="13"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="万份基金收益">
        <k-field-text v-model="formData.tenThousandIncomeAmt"
                      data-regx-text="请输入正确的数字"
                      data-digits="4"  data-integer-length="4"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="近七日年化收益率">
        <k-field-text v-model="formData.sevenDaysIncomeRate"
                      data-regx-text="请输入正确的数字" :data-max-length="10"
                      data-digits="4"  data-integer-length="4"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="销售服务费">
        <k-field-text v-model="formData.saleServiceFee"
                      data-regx-text="请输入正确的数字"
                      data-digits="2"  data-integer-length="13"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="基金累计净值">
        <k-field-text v-model="formData.totalNav"
                      data-regx-text="请输入正确的数字"
                      data-digits="4"  data-integer-length="11"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="净值增长率">
        <k-field-text v-model="formData.navGrowthRate"
                      data-regx-text="请输入正确的数字"
                      data-digits="6"  data-integer-length="1"
                      data-validate-type="number" data-type="number"/>
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="NetWorth.updatenetWorth" data-from="editnetWorthForm"
	        :data-model="formData" data-target="netWorthGrid">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    data() {
      return {
        disabled:true,
        rules: {
          navDate: [
            {required: true, message: '请选择净值日期', trigger: 'blur'},
            {min: 0, max: 8, message: '长度不能超过 8 个字符', trigger: 'blur'}
          ]
        },
        netWorthSearchParam: {
          prodCode:'',
          navDate:''
        },
        prodSearchParam: {},
        formData: {},
        date: {
          navDate: ''
        },
        selectRowData: {},
        dateShow: false,
        disclosureDate:'',
        netvalDate:'',
        prodCode:'',
        navDateRange:[],
        shareFlag:false,
      };
    },
    watch: {
      navDateRange() {
        //console.log("this.netWorthSearchParam=:>",this.navDateRange);
        if(this.navDateRange!=null&&this.navDateRange!=''&&this.navDateRange!=undefined){
          this.$set(this.netWorthSearchParam, 'navDateStart', this.navDateRange[0]);
          this.$set(this.netWorthSearchParam, 'navDateEnd', this.navDateRange[1]);
        }else{
          this.$set(this.netWorthSearchParam, 'navDateStart', null);
          this.$set(this.netWorthSearchParam, 'navDateEnd', null);
        }
      }
    },
    methods: {
      changedCode(value){
        //alert("value=:>>>>"+value);
        if(value==='1'){
          this.shareFlag=true;
        }else{
          this.shareFlag=false;
        }
        //console.log(this.isTruteeCheckFlag==='1');
      },
      popupEdit(row){
        let pathUrl = '/main/pms/netWorth/shareSortNetWorth';
        this.$router.push({
          path: pathUrl,
          query: {prodCode: row.prodCode,navDate:row.navDate},
        });
      },
      submitUploadParam() {
        let formData = this.formData;
        this.$refs.uploadRef.upload(formData);
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, []);
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        this.$refs.netWorthGrid.load();
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      findSystemTime() {
        this.httpUtil.comnQuery({
          action:"SystemParam.findSystemTime",
        }).then(data=>{
          console.log(data)
          if(data.rows[0].paravalue == "1"){
            this.date.navDate = data.rows[1].paravalue;
            console.log( this.date.navDate)
          }
          let aData = new Date()
          let Mo = aData.getMonth() + 1;
          let Da = aData.getDate();
          Mo = Mo < 10 ? "0" + Mo : Mo;
          Da = Da < 10? "0" + Da : Da;
          this.date.navDate = aData.getFullYear() + "" + Mo + "" + Da
          console.log( this.date.navDate)
        })
      }
    }
  };
</script>
