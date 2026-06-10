<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="disclosureRuleGrid">
        <k-form-item label="信披规则名称">
          <k-field-text v-model="searchParam.ruleName" :data-max-length="255"></k-field-text>
        </k-form-item>
<!--        <k-form-item label="任务发起方式">-->
<!--          <k-field-select v-model="searchParam.startRule" data-dict="xp_disclosure_start_rule"></k-field-select>-->
<!--        </k-form-item>-->
        <k-form-item label="状态" dataLabelWidth="70px" dataInputWidth="100px">
          <k-field-select v-model="searchParam.status" data-dict="xp_status"></k-field-select>
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="searchParam.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" @data-on-change="onDocTypeChange"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="searchParam.disclosureType=='5'||searchParam.disclosureType=='6' ||  searchParam.disclosureType=='1'||  searchParam.disclosureType=='9'">
          <k-field-select v-model="searchParam.disclosureSonType" data-value-field="value" data-display-field="text" :data-data="addDocTypeDict"/>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" slot="button"
               data-target="addDisclosureRulePopup" v-if="global.isShowAuthorityButton('DisclosureRule.addDisclosureRule')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
        <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="disclosureRuleGrid"
               :data-export-name="'信披生成规则'" v-if="global.isShowAuthorityButton('DisclosureRule.exportDisclosureRuleRightControl')">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureRuleGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="200px"
              data-action="DisclosureRule.findDisclosureRulesAuth">
        <k-grid-column data-header="规则id" data-name="id" :data-sortable="true" data-default-sort="DESC" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披规则名称" data-name="ruleName" data-width="230"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告标题" data-name="noticeTitle" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板文件名称" data-name="docName" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板名称" data-name="t8DisclosureModName" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板版本id" data-name="disclosureModVersionId" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否需要补录" data-name="ifClearing" data-dict="xp_if_ok" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否复核" data-name="ifCondition" data-dict="if_ok" data-width="50"></k-grid-column>
        <k-grid-column data-align="left" data-header="任务发起方式" data-name="startRule" data-dict="xp_disclosure_start_rule" data-hidden="true" data-export="false"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="公告负责角色" data-name="noticeRoleid" data-hidden="true" data-export="false"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="备注" data-name="remark" data-hidden="true" data-export="false"></k-grid-column>



        <k-grid-column data-align="left" data-header="产品形态" data-name="prodForm" data-dict="xp_prod_form" data-hidden="true" data-export="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="销售对象" data-name="prodObj"  data-dict="xp_target_customer" data-hidden="true" data-export="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集方式" data-name="prodClcMth" data-dict="xp_raise_type" data-hidden="true" data-export="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品投资性质" data-name="prodInvTyp" data-dict="xp_prod_invest_nature" data-hidden="true" data-export="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品系列" data-name="prodSerNm" data-hidden="true" data-export="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资周期长度" data-name="invPrdLen" data-hidden="true" data-export="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资周期维度" data-name="invPrdDime" data-dict="xp_cycle_dimension" data-hidden="true" data-export="true"></k-grid-column>



        <k-grid-column data-align="left" data-header="基准日期" data-name="baseDate" data-dict="xp_disclosure_base_date" data-width="130"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建时间" data-name="crtDate" data-type="date" data-render="renderDateTimeCreate" data-width="125"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建人" data-name="crtUserName" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="状态" data-name="status" data-dict="xp_status" data-width="60"></k-grid-column>
        <template slot="operate" slot-scope="scope">
<!--          updateStatusOnStop getProdDisRule updateStatusOnEnable-->
<!--          <k-field-bswitch data-on-value="1" data-off-value="0" v-model="scope.row.row.status" data-on-action="DisclosureRule.updateStatusOnEnable"
                           data-off-action="DisclosureRule.updateStatusOnStop" :data-params=scope.row.row :data-confirm="true" data-on-confirm-info="启用"
                           data-off-confirm-info="停用" :data-after-handler="flashing" />-->
          <k-btn class="md-success specialClass"  data-functype="SUBMIT" data-size="mini" data-action="DisclosureRule.updateStatusOnEnable"
                 style="min-width:40px;" data-target="disclosureRuleGrid" :data-confirm="true" v-if="scope.row.row.status == '0' && global.isShowAuthorityButton('DisclosureRule.updateStatusOnEnable')">
            启用
          </k-btn>
          <k-btn class="md-rose specialClass" data-functype="SUBMIT" data-action="DisclosureRule.updateStatusOnStop" v-if="scope.row.row.status == '1' && global.isShowAuthorityButton('DisclosureRule.updateStatusOnStop')"
                 style="min-width:40px;" :data-confirm="true" data-size="mini" data-type="danger" data-target="disclosureRuleGrid" >
            停用
          </k-btn>
          <k-btn data-functype="POPUP"  data-size="mini" class="md-info specialClass"
                 data-descript="详情" style="min-width:40px;"
                 data-target="detailDisclosureRulePopup">
            详情
          </k-btn>
          <k-btn class="md-info specialClass" data-descript="修改信披规则" data-functype="POPUP" data-size="mini"
                 data-target="editDisclosureRulePopup" v-if="global.isShowAuthorityButton('DisclosureRule.updateDisclosureRule')"
                 style="min-width:40px;" :data-disabled="scope.row.row.status=='1'">
            修改
          </k-btn>
          <k-btn class="md-danger specialClass" data-functype="SUBMIT"
                 data-action="DisclosureRule.deleteDisclosureRule" data-size="mini" style="min-width:40px;"
                 data-type="danger" data-target="disclosureRuleGrid" :data-confirm="true" data-descript="删除信披规则"
                 v-if="global.isShowAuthorityButton('DisclosureRule.deleteDisclosureRule')">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加信披规则实体类弹出框   -->
    <k-popup ref="addDisclosureRulePopup" data-title="新增" :data-dialog-drag="true" data-width="760px">
      <disclosure-rule-operate :formData="formData" ref="addDisclosureRule"></disclosure-rule-operate>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureRule.addDisclosureRule"
                 data-from="addDisclosureRuleForm" :data-handler="addValidate"
                 :data-model="formData" data-target="disclosureRuleGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE" >
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改信披规则实体类弹出框   -->
    <k-popup ref="editDisclosureRulePopup" data-title="修改" :data-dialog-drag="true" data-width="760px">
      <disclosure-rule-operate :formData="formData"  ref="editDisclosureRule" :isEditer="true"
      ></disclosure-rule-operate>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureRule.updateDisclosureRule"
                 data-from="addDisclosureRuleForm" :data-model="formData"
                 data-target="disclosureRuleGrid" :data-handler="editValidate">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="detailDisclosureRulePopup" data-title="详情" :data-dialog-drag="true" data-width="760px">
      <disclosure-rule-detail :formData="formData" :disclosureType="formData.disclosureType"></disclosure-rule-detail>
    </k-popup>
  </div>
</template>

<script>
import DisclosureRuleOperate from "@/pages/pms/basePublish/DisclosureRule/DisclosureRuleOperate";
import DisclosureRuleDetail from "@/pages/pms/basePublish/DisclosureRule/DisclosureRuleDetail";
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name:"DisclosureRuleList",
  components: {DisclosureRuleOperate, DisclosureRuleDetail},
  data() {
    return {
      addDocTypeDict: {},
      formData: {},
      searchParam: {},//查询条件
      DocTypeDict: {}
    };

  },
  created() {
    this.xpType();
    this.queryRoleIdId();
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
      this.$refs.disclosureRuleGrid.load();
    },
    changeBefore(val){
      if (val==='1'){
        this.changeButStatusOn(val);
      }else if (val==='0'){
        this.changeButStatusStop(val);
      }
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    //行被选中
    selectRow(row, column, event) {
      const _this = this;
      _this.formData = assign({}, row);
    },
    //新增保存验证
    addValidate() {
      return this.$refs.addDisclosureRule.$refs.addDisclosureRuleForm.validate();
    },
    //修改保存验证
    editValidate() {
      return this.$refs.editDisclosureRule.$refs.addDisclosureRuleForm.validate();
    },
    changeButStatusOn(val){
      this.$confirm("确认要启用吗？", "操作提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.httpUtil.comnUpdate({
          action: "DisclosureRule.getProdDisRule",
          params: val,
          mask: true
        }).then(data => {
          if("操作成功"==data.returnmsg){
            //更新按钮颜色
            let id=val.id
            let list=this.$refs.disclosureRuleGrid.list
            for (let i = 0; i < list.length; i++) {
              if(id==list[i].id){
                list[i].status=1
              }
            }
            this.$refs.disclosureRuleGrid.list=list
          }
        });
      }).catch(() => {});

    },
    onDocTypeChange(disclosureType) {
      //刷新查询框子类型字典值
      this.$set(this.searchParam, 'disclosureSonType', '');
      this.$set(this.searchParam, 'disclosureSonType', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
        console.log(data.rows);
      }).catch({})
    },
    changeButStatusStop(val){
      this.$confirm("确认要停用吗？", "操作提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.httpUtil.comnUpdate({
          action: "DisclosureRule.updateStatusOnStop",
          params: val,
          mask: true
        }).then(data => {
          if("操作成功"==data.returnmsg){
            //更新按钮颜色
            let id=val.id
            let list=this.$refs.disclosureRuleGrid.list
            for (let i = 0; i < list.length; i++) {
              if(id==list[i].id){
                list[i].status=0
              }
            }
            this.$refs.disclosureRuleGrid.list=list
          }

        });
      }).catch(() => {});

    }
  },
};
</script>
<style scoped>
>>> .el-table__cell {
  padding: 0px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
