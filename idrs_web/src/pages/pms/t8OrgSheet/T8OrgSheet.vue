<template>
  <div class="py-page">
    <div>
      <k-form-search-customize v-model="queryParam" data-target="t8OrgSheetGrid" data-label-width="140px">
        <k-form-item label="统一社会信用编码" >
          <k-field-text v-model="queryParam.csldSocCrdCd"  ></k-field-text>
        </k-form-item>
        <k-form-item label="机构名称">
          <k-field-text v-model="queryParam.orgFullName"></k-field-text>
        </k-form-item>
        <k-form-item label="机构种类">
          <k-field-select v-model="queryParam.orgTyp" data-dict="org_type" ></k-field-select>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" slot="button" :data-handler="addGetAction" data-target="addT8OrgSheetPopup" v-if="global.isShowAuthorityButton('T8OrgSheet.addT8OrgSheet')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>

      <k-grid ref="t8OrgSheetGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="300px" data-action="T8OrgSheet.findT8OrgSheets"
              v-if="global.isShowAuthorityButton('T8OrgSheet.findT8OrgSheets')">
        <k-grid-column data-align="left" data-header="机构编码" data-name="orgNbrExt"></k-grid-column>
        <k-grid-column data-align="left" data-header="机构名称" data-name="orgFullName"></k-grid-column>
        <k-grid-column data-align="left" data-header="统一社会信用编码" data-name="csldSocCrdCd"></k-grid-column>
        <k-grid-column data-align="left" data-header="机构种类" data-name="orgTyp"  data-dict="org_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="同业机构类型" data-name="samBusOrgTyp" data-dict="samBusOrgTyp"></k-grid-column>
        <k-grid-column data-align="left" data-header="版本号" data-name="version"  data-width="60"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改机构信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('T8OrgSheet.updateT8OrgBaseSheet')"
                 data-target="editT8OrgSheetPopup" :data-handler="editGetAction">
            修改
            </k-btn>
          <k-btn class="btn-custom-text" data-descript="补录" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('T8OrgSheet.updateT8OrgSheet')"
                 data-target="collectT8OrgSheetPopup">
            补录
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="详情" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('T8OrgSheet.detailT8OrgSheets')"
                 data-target="detailT8OrgSheetPopup">
            详情
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="T8OrgSheet.deleteT8OrgSheet" data-size="mini" v-if="global.isShowAuthorityButton('T8OrgSheet.deleteT8OrgSheet')"
                 data-type="danger" data-target="t8OrgSheetGrid" :data-confirm="true" data-descript="删除机构信息">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="collectT8OrgSheetPopup" data-title="补录" >
      <t8-org-sheet-collection  :info="formData" :disabledVal="true"
      ></t8-org-sheet-collection>
    </k-popup>

    <k-popup ref="detailT8OrgSheetPopup" data-title="详情" >
      <T8OrgSheetDetail  :info="formData" :disabledVal="true"  :isDetailShow="true"
      ></T8OrgSheetDetail>
    </k-popup>


    <!--    修改T8OrgSheet弹出框   -->
    <k-popup ref="editT8OrgSheetPopup" data-title="修改">
      <T8OrgSheetEdict :info="formData" :action="action" :disabledVal="true"
      ></T8OrgSheetEdict>
    </k-popup>

    <!--    修改T8OrgSheet弹出框   -->
    <k-popup ref="addT8OrgSheetPopup" data-title="新增">
      <T8OrgSheetEdict :info="formData" :action="action" :disabledVal="false"
      ></T8OrgSheetEdict>
    </k-popup>
  </div>
</template>

<script>
import T8OrgSheetCollection from "@/pages/pms/t8OrgSheet/T8OrgSheetCollection";
import T8OrgSheetDetail from "@/pages/pms/t8OrgSheet/T8OrgSheetDetail";
import T8OrgSheetEdict from "@/pages/pms/t8OrgSheet/T8OrgSheetEdict";
export default {
  name: "T8OrgSheet",
  components: {T8OrgSheetCollection,T8OrgSheetDetail,T8OrgSheetEdict},
  data() {
    return {
      formData: {},
      selectRowData: {},
      queryParam:{},
      action:'',
    };
  },

  created() {
  },
  methods: {
    editGetAction(){
      this.action='T8OrgSheet.updateT8OrgBaseSheet';
    },
    addGetAction(){
      this.formData={};
      this.action='T8OrgSheet.addT8OrgSheet';
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
  }
};
</script>
