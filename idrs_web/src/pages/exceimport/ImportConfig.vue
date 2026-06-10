<template>
  <div class="py-page">
    <k-form-search-customize data-target="valReportTabGrid1" data-model-name="valReportTab" v-model="findFormData">
      <k-form-item label="估值表">
        <k-field-select v-model="findFormData.id"  data-action="ValReportTab.findValReportTabs"
                        data-display-field="id,reporttabName" data-value-field="id" />
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <k-btn slot="button" class="btn-custom-plain" data-functype="POPUP" :data-handler="()=>this.formData={}"
             data-target="addValReportTabPopup">
          <md-icon md-src="/static/svg/add.svg"/>新增主体配置</k-btn>
      </div>
    
      <k-grid ref="valReportTabGrid1" @data-row-select="selectRow"
              data-operate-width="380px" data-action="ValReportTab.findValReportTabs" :data-page-size="5">
        <k-grid-column data-header="序列" data-name="id"></k-grid-column>
        <k-grid-column data-header="估值表名称" data-name="reporttabName"></k-grid-column>
        <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <k-grid-column data-header="录入柜员" data-name="inputuser"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate"></k-grid-column>
        <!--        <k-grid-column data-header="创建时间" data-name="crtTime"></k-grid-column>-->
        <template slot="operate" slot-scope="scope">

          <k-btn class="btn-custom-text" data-functype="POPUP" :data-handler="setFormData" data-target="addValReportTabParsetPopup" style="width:100px">
            新增解析配置</k-btn>
          <k-btn class="btn-custom-text" data-functype="POPUP" :data-handler="setFormData" data-target="addAssetsReportPopup" style="width:100px">
            新增资产配置</k-btn>
          <k-btn class="btn-custom-text" data-descript="修改估值表主体" data-functype="POPUP" data-size="mini"
                data-target="editValReportTabPopup" >
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ValReportTab.deleteValReportTab" data-size="mini"
                data-type="danger" data-target="valReportTabGrid1" :data-confirm="true" data-descript="删除估值表主体">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <div class="py-page-container">
      <k-grid ref="valReportTabGrid2" :data-autoload="false" @data-row-select="selectRow2"
              data-operate-width="180px" data-action="ValReportTabParset.findValReportTabParsets" :data-page-size="5" >
        <k-grid-column data-header="估值表名称" data-name="reporttabName"/>
        <k-grid-column data-header="参数类型" data-name="paramType" data-dict="val_params_type"/>
        <k-grid-column data-header="加载顺序" data-name="orderNum"/>
        <k-grid-column data-header="参数代码" data-name="paramCode"/>
        <k-grid-column data-header="参数名称" data-name="paramName"/>
        <k-grid-column data-header="参数数据类型" data-name="paramDataType" data-dict="val_params_data_type"/>
        <k-grid-column data-header="参数值/解析公式" data-name="paramValue"/>
        <k-grid-column data-header="参数条件" data-name="paramCondition"/>
        <k-grid-column data-header="备注" data-name="note"/>
        <template slot="operate" slot-scope="scope">

          <k-btn class="btn-custom-text" data-descript="修改配置编辑" data-functype="POPUP" data-size="mini"
                data-target="editValReportTabParsetPopup" >
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ValReportTabParset.deleteValReportTabParset"
                data-type="danger" data-target="valReportTabGrid2" :data-confirm="true" data-descript="删除配置编辑">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <div class="py-page-container">
      <k-grid ref="valReportTabGrid3" :data-autoload="false" @data-row-select="selectRow3"
              data-operate-width="180px" data-action="OdsReadAssetsReport.findOdsReadAssetsReports" :data-page-size="5" >
        <k-grid-column data-header="估值表名称" data-name="reporttabName"/>
        <k-grid-column data-header="关联类型" data-name="isprodorasset" data-dict="base_isprodorasset"/>
        <k-grid-column data-header="资产/产品代码" data-name="assetCode" />
        <k-grid-column data-header="备注" data-name="note"/>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改资产配置" data-functype="POPUP" data-size="mini"
                data-target="editAssetsReportPopup" >
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="OdsReadAssetsReport.deleteOdsReadAssetsReport" data-size="mini"
                data-type="danger" data-target="valReportTabGrid3" :data-confirm="true" data-descript="删除资产配置">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加估值表主体弹出框   -->
    <k-popup ref="addValReportTabPopup" data-title="新增主体配置">
      <k-form ref="addValReportTabForm" :data-col="2">
        <k-form-item label="估值表名称">
          <k-field-text v-model="formData.reporttabName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
        <k-form-item label="录入柜员" v-show="false">
          <k-field-text v-model="formData.inputuser"/>
        </k-form-item>
        <k-form-item label="创建日期" v-show="false">
          <k-field-text v-model="formData.crtDate"/>
        </k-form-item>
        <k-form-item label="创建时间" v-show="false">
          <k-field-text v-model="formData.crtTime"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="addValReportTabForm" :data-handler="beforeSubmit" ref="addSubmitBtn"
                 :data-model="formData" data-target="valReportTabGrid1">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改估值表主体弹出框   -->
    <k-popup ref="editValReportTabPopup" data-title="修改估值表主体">
      <k-form ref="editValReportTabForm" :data-col="2">
        <k-form-item label="序列">
          <k-field-text v-model="formData.id" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="估值表名称">
          <k-field-text v-model="formData.reporttabName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
        <k-form-item label="录入柜员" v-show="false">
          <k-field-text v-model="formData.inputuser" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="editValReportTabForm" :data-handler="beforeEditSubmit" ref="editSubmitBtn"
                 :data-model="formData" data-target="valReportTabGrid1">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <!--    添加配置编辑弹出框   -->
    <k-popup ref="addValReportTabParsetPopup" data-title="新增解析配置">
      <k-form ref="addValReportTabParsetForm" :data-col="2">
        <k-form-item label="估值表名称">
          <k-field-text v-model="formData.reporttabName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="参数类型">
          <k-field-select v-model="formData.paramType" :data-allowblank="false" data-dict="val_params_type"/>
        </k-form-item>
        <k-form-item label="加载顺序">
          <k-field-text v-model="formData.orderNum" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="参数代码">
          <k-field-text v-model="formData.paramCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="参数名称">
          <k-field-text v-model="formData.paramName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="参数数据类型">
          <k-field-select v-model="formData.paramDataType" :data-allowblank="false" data-dict="val_params_data_type"/>
        </k-form-item>
        <k-form-item label="参数值/解析公式">
          <k-field-text v-model="formData.paramValue" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="参数条件">
          <k-field-text v-model="formData.paramCondition"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.note"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ValReportTabParset.addValReportTabParset" data-from="addValReportTabParsetForm"
                 :data-model="formData" data-target="valReportTabGrid2">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改配置编辑弹出框   -->
    <k-popup ref="editValReportTabParsetPopup" data-title="修改配置编辑">
      <k-form ref="editValReportTabParsetForm" :data-col="2">
        <k-form-item label="ID" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="估值表名称">
          <k-field-text v-model="formData.reporttabName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="参数类型">
          <k-field-select v-model="formData.paramType" :data-allowblank="false" data-dict="val_params_type"/>
        </k-form-item>
        <k-form-item label="加载顺序">
          <k-field-text v-model="formData.orderNum" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="参数代码">
          <k-field-text v-model="formData.paramCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="参数名称">
          <k-field-text v-model="formData.paramName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="参数数据类型">
          <k-field-select v-model="formData.paramDataType" :data-allowblank="false" data-dict="val_params_data_type"/>
        </k-form-item>
        <k-form-item label="参数值/解析公式">
          <k-field-text v-model="formData.paramValue" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="参数条件">
          <k-field-text v-model="formData.paramCondition" />
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.note"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ValReportTabParset.updateValReportTabParset" data-from="editValReportTabParsetForm"
                 :data-model="formData" data-target="valReportTabGrid2">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>



    <k-popup ref="addAssetsReportPopup" data-title="新增资产配置">
      <k-form ref="addAssetsReportForm" :data-col="2">
        <k-form-item label="估值表名称">
          <k-field-text v-model="formData.reporttabName" :data-allowblank="false"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="关联类型">
          <k-field-select v-model="formData.isprodorasset" data-dict="base_isprodorasset" :data-allowblank="false"  data-default-value="2" />
        </k-form-item>
        <k-form-item label="资产/产品代码">
          <k-field-text v-model="formData.assetCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.note"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="OdsReadAssetsReport.addOdsReadAssetsReport"
                 data-from="addAssetsReportForm" :data-model="formData" data-target="valReportTabGrid3">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="editAssetsReportPopup" data-title="修改">
      <k-form ref="editAssetsReportForm" :data-col="2">
        <k-form-item label="估值表名称">
          <k-field-text v-model="formData.reporttabName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="关联类型">
          <k-field-select v-model="formData.isprodorasset" data-dict="base_isprodorasset" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="资产/产品代码">
          <k-field-text v-model="formData.assetCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.note"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="OdsReadAssetsReport.updateOdsReadAssetsReport"
                 data-from="editAssetsReportForm" :data-model="formData" data-target="valReportTabGrid3">
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
import Tools from '@/utils/tools.js';
import httpUtil from "@/frame/httpUtil";

export default {
  name: "ImportConfig",
  data() {
    return {
      formData: {},
      selectRowData: {},
      findFormData :{},
    };
  },
  methods: {
    setFormData(){
      this.formData = {};
    },
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = Object.assign({}, row);
      _this.formData = Object.assign({}, row);
      this.$refs.valReportTabGrid2.load({t8ValReporttabId: this.formData.id})
      this.$refs.valReportTabGrid3.load({t8ValReporttabId: this.formData.id})
    },

    selectRow2(row, column, event) {
      const _this = this;
      _this.formData = Object.assign({}, row);
    },

    selectRow3(row, column, event) {
      const _this = this;
      _this.formData = Object.assign({}, row);
    },
    beforeSubmit(value){
      if(this.formData.reporttabName==null||this.formData.reporttabName==undefined){
        Tools.alert("估值表名称不能为空！");
        return false;
      }
      this.$set(value,"reporttabName",this.formData.reporttabName);
      this.httpUtil.comnQuery({
        action: "OdsReadAssetsReport.findOnlyOdsReadAssetsReports",
        params: {reporttabName: this.formData.reporttabName},
        successAlert: false
      }).then(data => {
        console.log(data)
        if(data.rows.length>0){
          Tools.alert("该估值表名称已经存在！","danger");
          this.$refs.addSubmitBtn.loading = false;
        } else {
          this.httpUtil.comnUpdate({
            action: "ValReportTab.addValReportTab",
            params: this.formData,
          }).then(data => {
            this.$refs.addValReportTabPopup.close();
            this.$refs.valReportTabGrid1.load();
          });
        }


      });
    },
    beforeEditSubmit(value){
      if(this.formData.reporttabName==null||this.formData.reporttabName==undefined){
        Tools.alert("估值表名称不能为空！");
        return false;
      }
      this.$set(value,"reporttabName",this.formData.reporttabName);
      this.httpUtil.comnQuery({
        action: "OdsReadAssetsReport.findOnlyOdsReadAssetsReports",
        params: {reporttabName: this.formData.reporttabName,id:this.formData.id},
        successAlert: false
      }).then(data => {
        console.log(data)
        if(data.rows.length>0){
          Tools.alert("该估值表名称已经存在！","danger");
          this.$refs.editSubmitBtn.loading = false;
        } else {
          this.httpUtil.comnUpdate({
            action: "ValReportTab.updateValReportTab",
            params: this.formData,
          }).then(data => {
            this.$refs.editValReportTabPopup.close();
            this.$refs.valReportTabGrid1.load();
          });
        }


      });
    },

  }
};
</script>
