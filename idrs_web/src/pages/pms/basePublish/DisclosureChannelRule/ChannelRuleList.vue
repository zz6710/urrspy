<template>
  <div>
    <div>
      <k-form-search-customize data-target="channelRuleGrid" v-model="prodSearchParam" >
        <k-form-item label="渠道规则名称">
          <k-field-text v-model="prodSearchParam.channelRuleName" :data-max-length="100"></k-field-text>
        </k-form-item>
        <k-form-item label="渠道名称">
          <k-field-text v-model="prodSearchParam.channelName" :data-max-length="100"></k-field-text>
        </k-form-item>
        <k-form-item label="状态" dataLabelWidth="70px" dataInputWidth="100px">
          <k-field-select   v-model="prodSearchParam.status" data-dict="xp_status" />
        </k-form-item>
        <k-form-item label="信披类型" >
          <k-field-select v-model="prodSearchParam.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" @data-on-change="queryProd"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="prodSearchParam.disclosureType=='5'||prodSearchParam.disclosureType=='6'||prodSearchParam.disclosureType=='1'||prodSearchParam.disclosureType=='9'">
          <k-field-select v-model="prodSearchParam.disclosureSonType" :data-data="addDocTypeDict" data-value-field="value" data-display-field="text"></k-field-select>
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="openAddBox"
               data-target="addChannelRulePopup" v-if="global.isShowAuthorityButton('DisclosureChannelRule.insertChannelRule')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search-customize>
    </div>
    <k-grid ref="channelRuleGrid" data-action="DisclosureChannelRule.findChannelRuleAuth" @data-row-select="selectRow" data-operate-width="200px"
            data-fixed="right">
      <k-grid-column data-header="渠道配置id" data-name="id" :data-sortable="true" data-default-sort="DESC" data-hidden="true"></k-grid-column>
      <k-grid-column data-align="left" data-header="渠道规则名称" data-name="channelRuleName" data-width="230"/>
      <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type"  data-width="80"/>
      <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type" data-width="150"/>
      <k-grid-column data-align="left" data-header="渠道名称" data-name="channelName" data-width="120"/>
      <k-grid-column data-align="left" data-header="渠道文件类型" data-name="uploadFileType" data-dict="xp_upload_file_type" data-hidden="true"></k-grid-column>
      <k-grid-column data-align="left" data-header="确认文件后缀" data-name="suffixFileName"></k-grid-column>
      <k-grid-column data-align="left" data-header="文件名称格式" data-name="uploadFileNameType" data-hidden="true"></k-grid-column>
      <k-grid-column data-align="left" data-header="备注" data-name="remark" data-hidden="true"></k-grid-column>
      <k-grid-column data-align="left" data-header="产品形态" data-name="prodForm" data-dict="xp_prod_form" data-width="100"/>
      <k-grid-column data-align="left" data-header="销售对象" data-name="prodObj" data-dict="xp_target_customer" data-width="120"/>
      <k-grid-column data-align="left" data-header="募集方式" data-name="prodClcMth" data-dict="xp_raise_type" data-width="100"/>
      <k-grid-column data-align="left" data-header="产品投资性质" data-name="prodInvTyp" data-dict="xp_prod_invest_nature" data-hidden="true" />
      <k-grid-column data-align="left" data-header="产品系列" data-name="prodSerNm" data-width="100"/>
      <k-grid-column data-align="left" data-header="状态" data-name="status" data-dict="xp_status" data-width="60"/>
      <k-grid-column data-align="left" data-header="创建时间" data-name="crtDate"  data-render="renderDateTimeCreate" data-width="130"/>
      <k-grid-column data-align="left" data-header="创建人" data-name="crtUserName" data-width="120"/>
      <template slot="operate" slot-scope="scope" system-aligen>
<!--        <k-field-bswitch data-on-value="1" data-off-value="0" v-model="scope.row.row.status" data-on-action="DisclosureChannelRule.updateStatusOnEnable"
                         data-off-action="DisclosureChannelRule.updateStatusOnStop" :data-params=scope.row.row :data-confirm="true" data-on-confirm-info="启用"
                         data-off-confirm-info="停用" :data-after-handler="flashing"/>-->

        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info specialClass"
               data-descript="详情" style="min-width:40px;"
               data-target="detailChannelRulePopup">
          详情
        </k-btn>
        <k-btn class="md-success specialClass"  data-functype="SUBMIT" data-size="mini" data-action="DisclosureChannelRule.updateStatusOnEnable"
               style="min-width:40px;" data-target="channelRuleGrid" :data-confirm="true" v-if="scope.row.row.status == '0' && global.isShowAuthorityButton('DisclosureChannelRule.updateStatusOnEnable')  ">
          启用
        </k-btn>
        <k-btn class="md-rose specialClass" data-functype="SUBMIT" data-action="DisclosureChannelRule.updateStatusOnStop" v-if="scope.row.row.status == '1' && global.isShowAuthorityButton('DisclosureChannelRule.updateStatusOnStop')  "
               style="min-width:40px;" :data-confirm="true" data-size="mini" data-type="danger" data-target="channelRuleGrid" >
          停用
        </k-btn>
        <k-btn data-functype="POPUP" data-size="mini" style="min-width:40px;"
               class="md-info specialClass" v-if="global.isShowAuthorityButton('DisclosureChannelRule.updateChannelRule')"
               data-descript="修改" :data-disabled="scope.row.row.status == '1'"
               data-target="editChannelRulePopup" >
          修改
        </k-btn>
        <k-btn class="md-danger specialClass" data-functype="SUBMIT" style="min-width:40px;"
               data-action="DisclosureChannelRule.deleteChannelRule" data-size="mini"
               data-target="channelRuleGrid" :data-confirm="true" data-descript="删除渠道规则"
               v-if="global.isShowAuthorityButton('DisclosureChannelRule.deleteChannelRule')">
          删除
        </k-btn>
      </template>
    </k-grid>
    <k-popup ref="detailChannelRulePopup" data-title="详情" :data-dialog-drag="true" data-width="690px">
      <channel-rule-detail v-model="formData" :formData="formData"
                           ></channel-rule-detail>
    </k-popup>
    <k-popup ref="addChannelRulePopup" data-title="新增" :data-dialog-drag="true" data-width="690px" @data-opened="changePop">

        <channel-rule-operate v-model="formData" :formData="formData" ref="addChannelRule" :DocTypeDict="DocTypeDict"
                               :oldSelectProds="oldSelectProds" :isAddChannelRulePopup= "isAddChannelRulePopup"
                              :oldSelectChannels="oldSelectChannels"></channel-rule-operate>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureChannelRule.insertChannelRule"
                  data-from="addChannelRuleForm" :data-handler="addValidate"
                 :data-model="formData" data-target="channelRuleGrid" ref="sureBtn">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>
    <k-popup ref="editChannelRulePopup" data-title="修改" :data-dialog-drag="true" @data-opened="queryCustomChannels" data-width="690px">

        <channel-rule-operate v-model="formData" :formData="formData" ref="editChannelRule"
                               :oldSelectProds="oldSelectProds" :isEditChannelRulePopup= "isEditChannelRulePopup"
                              :DocTypeDict= "DocTypeDict"
                              :change="true"
                              :oldSelectChannels="oldSelectChannels"></channel-rule-operate>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureChannelRule.updateChannelRule"
                 :data-handler="editValidate" data-from="addChannelRuleForm"
                 :data-model="formData" data-target="channelRuleGrid" ref="sureEditBtn">
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
import ChannelRuleOperate from "./ChannelRuleOperate"
import ChannelRuleDetail from "./ChannelRuleDetail";
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "ChannelRuleList",
  components: {ChannelRuleOperate, ChannelRuleDetail},
  data() {
    return {
      ruleSearchParam: {},//查询参数
      formData: {},//被选中的数据
      prodSearchParam: {},
      oldSelectProds: '',//原来被选中的产品
      oldSelectChannels: '',//原来被选中的渠道
      addDocTypeDict:{},//子类型数据载体
      DocTypeDict:{},//类型数据载体
      isEditChannelRulePopup : false,
      isAddChannelRulePopup: false,
    }
  },
  created() {
    this.xpType();
    this.oldSelectProdArr = this.oldSelectProds.split(',');
    this.oldSelectChannelArr = this.oldSelectChannels.split(',');
    this.queryProd(this.prodSearchParam.disclosureType);
  },
  methods: {
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPType",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    flashing(){
      this.$refs.channelRuleGrid.load();
    },
    queryProd(disclosureType){
      this.$set(this.prodSearchParam, 'disclosureSonType', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
      }).catch({})
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    //点击新增事件
    openAddBox() {
      this.formData = {};
      this.oldSelectProds = '';
      this.oldSelectChannels = '';
    },
    //行被选中事件
    selectRow(row) {
      const _this = this;
      _this.formData = assign({}, row);
      // _this.oldSelectProds = _this.formData.t8ProdInfoIds;
      _this.oldSelectChannels = _this.formData.channelIds;
      this.queryCustomChannels();
    },
    //保存表单校验
    addValidate() {
      return this.$refs.addChannelRule.$refs.addChannelRuleForm.validate();
    },
    onChildSubmitSuccess() {
      this.$refs.addChannelRule.doReset();
      this.$refs.addChannelRulePopup.close();
      this.$refs.channelRuleGrid.load();
    },
    //修改保存校验
    editValidate() {
      return this.$refs.editChannelRule.$refs.addChannelRuleForm.validate();
    },
    //根据渠道id查询自定义渠道信息
    queryCustomChannels() {
      this.xpType();
      this.isEditChannelRulePopup = true;
    },
    changePop(){
      this.xpType();
      this.isAddChannelRulePopup = true;
    }
  },
}
</script>

<style scoped>
>>> .el-table__cell {
  padding: 0px 0 !important;
}

>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
