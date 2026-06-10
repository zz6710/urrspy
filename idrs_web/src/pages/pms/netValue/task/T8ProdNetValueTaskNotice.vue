<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ProdNoticeGrid" v-model="queryParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="searchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="searchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="是否份额分类">
          <k-field-select v-model="searchParam.isShareSort" data-dict="1yes0no" />
        </k-form-item>
        <k-form-item label="净值日期">
          <k-field-date v-model="searchParam.netvalDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-form-item label="披露日期">
          <k-field-date v-model="searchParam.disclosureDate" data-type="date" data-date-format="yyyy-MM-dd"/>
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="PAGE" data-target="/main/pms/netValue/T8ProdNetValueTask">
          返回</k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8ProdNoticeGrid"
              data-action="T8ProdNetValueNotice.findT8ProdNetValueNotices"
              @data-row-select="selectNotice"
              data-operate-column="true"
              :data-display="false">
        <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="110"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="260"></k-grid-column>
        <k-grid-column data-align="center" data-header="披露日期" data-name="disclosureDate"></k-grid-column>
        <k-grid-column data-align="center" data-header="净值日期" data-name="netvalDate"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品总净值" data-name="totalNet"></k-grid-column>
        <k-grid-column data-align="center" data-header="产品总份额" data-name="totalVol"></k-grid-column>
        <k-grid-column data-align="center" data-header="当日收益" data-name="navProfit"></k-grid-column>
        <k-grid-column data-align="center" data-header="单位净值" data-name="nav" data-width="120"></k-grid-column>
        <k-grid-column data-align="center" data-header="累计净值" data-name="totalNav"></k-grid-column>
        <k-grid-column data-align="center" data-header="单位万份收益" data-name="tenThousandIncomeAmt" data-width="105"></k-grid-column>
        <k-grid-column data-align="center" data-header="近七日年化收益率" data-name="sevenDaysIncomeRate" data-width="150"></k-grid-column>
        <k-grid-column data-header="状态" data-name="status" data-dict="t8_net_notice_status" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否份额分类" data-name="isShareSort" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="是否为母产品" data-name="isParentProd" data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品净值披露数据" data-functype="POPUP" data-size="mini"
                 data-target="editNetNoticePopup" :data-disabled="taskStatus=='1'" v-show="scope.row.row.isParentProd == '1'"
                 v-if="global.isShowAuthorityButton('T8ProdNetValueNotice.updateT8ProdNetValueNotice')">
            <md-icon>edit</md-icon>
          </k-btn>

          <k-btn class="md-info md-just-icon md-simple" data-descript="修改子份额净值披露数据" data-functype="POPUP" data-size="mini"
                 data-target="editNetNoticePopup" :data-disabled="taskStatus=='1'" v-show="scope.row.row.isParentProd == '0'"
                 v-if="global.isShowAuthorityButton('NetWorth.updatenetWorth')">
            <md-icon>edit</md-icon>
          </k-btn>

          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" :data-disabled="taskStatus=='1'"
                 data-action="T8ProdNetValueNotice.deleteT8ProdNetValueNotice" data-size="mini" v-show="scope.row.row.isParentProd == '1'"
                 data-type="danger" data-target="t8ProdNoticeGrid" :data-after-success="refreshGrid" :data-confirm="true" data-descript="删除产品净值披露数据"
                 v-if="global.isShowAuthorityButton('T8ProdNetValueNotice.deleteT8ProdNetValueNotice')">
            <md-icon>close</md-icon>
          </k-btn>

          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" :data-disabled="taskStatus=='1'"
                 data-action="NetWorth.deletenetWorth" data-size="mini" v-show="scope.row.row.isParentProd == '0'"
                 data-type="danger" data-target="t8ProdNoticeGrid" :data-after-success="refreshGrid" :data-confirm="true" data-descript="删除子份额净值披露数据"
                 v-if="global.isShowAuthorityButton('NetWorth.deletenetWorth')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="editNetNoticePopup" data-title="修改">
      <k-form ref="editNetNoticeForm" :data-col="2">
        <net-value-notice-ope :formData="formData"></net-value-notice-ope>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdNetValueNotice.updateT8ProdNetValueNotice"
                 data-from="editNetNoticeForm" v-show="this.isParentProd == '1'"
                 :data-model="formData" data-target="t8ProdNoticeGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="NetWorth.updatenetWorth"
                 data-from="editNetNoticeForm" v-show="this.isParentProd == '0' "
                 :data-model="formData" data-target="t8ProdNoticeGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
import {assign} from "lodash";
import NetValueNoticeOpe from "@/pages/pms/netValue/task/NetValueNoticeOpe";
import Tools from "@/utils/tools";

export default {
  name:"T8ProdNetValueTaskNotice",
  components: {NetValueNoticeOpe},
  data() {
    return {
      formData: {
        isParentProd:''
      },
      selectRowData: {},
      searchParam: {
        t8DisclosureTaskId : this.t8DisclosureTaskId,
        prodName: '',
        prodCode: '',
        netvalDate: '',
        disclosureDate: ''
      },
      queryParamDateRange: [],
      taskStatus: '',//任务状态
      netvalDate:'',
      prodCode:'',
      disclosureDate:'',
      t8DisclosureTaskId:'',
      isParentProd:''
    };
  },

  watch: {
    queryParamDateRange() {
      console.log("this.queryParamDateRange=:>",this.queryParamDateRange);
      if(this.queryParamDateRange!=null&&this.queryParamDateRange!=''&&this.queryParamDateRange!=undefined){
        this.$set(this.searchParam, 'startDate', this.queryParamDateRange[0]);
        this.$set(this.searchParam, 'endDate', this.queryParamDateRange[1]);
      }else{
        this.$set(this.searchParam, 'startDate', null);
        this.$set(this.searchParam, 'endDate', null);
      }
    }
  },
  created() {
    this.t8DisclosureTaskId = this.$route.query.t8DisclosureTaskId;
    console.log("created",this.t8DisclosureTaskId );

    //console.log("rowId=:>>>>",this.$route.query.id);
    //console.log("rowId=:>>>>111111111");
    //接收路由中的参数
    this.$nextTick(()=>{
      this.$refs.t8ProdNoticeGrid.load({t8DisclosureTaskId: this.t8DisclosureTaskId})
      //this.$refs.t8ProdNetValueTaskGrid.load({id: this.$route.query.id})
    });
  },
  methods: {

    beforePopupLoad(params){
      params.disclosureDate = this.disclosureDate;
      params.netvalDate = this.netvalDate;
      params.prodCode=this.prodCode;
      params.t8DisclosureTaskId = this.t8DisclosureTaskId;
      params.prodName = this.searchParam.prodName;

      return params;
    },
    refreshGrid(){
      //this.$refs.t8ProdNoticeGrid.load(this.queryParam());
    },
    //二级查询被选中
    selectNotice(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      _this.formData = Object.assign({}, row)
      //console.log(_this.formData);
      //是否为母产品
      this.isParentProd = row.isParentProd;
    },
  },
  computed: {
    queryParam() {
      return {
        't8DisclosureTaskId' : this.t8DisclosureTaskId,
        'prodName': this.searchParam.prodName,//产品名称
        'prodCode': this.searchParam.prodCode,//产品代码
        'netvalDate': this.searchParam.netvalDate,//净值日期
        'disclosureDate': this.searchParam.disclosureDate ,//披露日期
        'isShareSort': this.searchParam.isShareSort ,//是否份额分类

      }
    }
  },
  activated() {
    console.log("activated",this.t8DisclosureTaskId);
    this.t8DisclosureTaskId = this.$route.query.t8DisclosureTaskId;
    this.$refs.t8ProdNoticeGrid.load({t8DisclosureTaskId: this.t8DisclosureTaskId})

  },
};
</script>
