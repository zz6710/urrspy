<template>
  <div>
    <k-form-search data-model-name="ProdOriginality" data-target="principleGrid">
      <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.ProdOriginality={}" data-target="addOriginalityPopup">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
    </k-form-search>

    <!--  grid模板列表  -->
    <k-grid ref="principleGrid" @data-row-select="selectRow" data-action="ProdOriginality.findOriginality"
            @init="(grid)=>{this.$kgrid = grid}">

      <k-grid-column data-align="center" data-header="创意名称" data-name="originalityName"/>
      <k-grid-column data-align="center" data-header="系列名称" data-name="seriesName" />
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="创意发明人" data-name="username" />
      <k-grid-column data-align="center" data-header="创意日期" data-name="uploaddate"/>
      <k-grid-column data-align="center" data-header="状态" data-name="isState"  data-dict="t8_originality_status"/>
      <k-grid-column data-align="center" data-header="审批状态" data-name="runningStatus" data-dict="process_status"/>


      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP" data-size="mini"
               data-target="editOriginalityPopup">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ProdOriginality.deleteProdOriginality" data-size="mini"
               data-type="danger" data-target="principleGrid" :data-confirm="true" data-descript="删除" >
          <md-icon>close</md-icon>
        </k-btn>

        <k-btn class="md-danger md-just-icon md-simple" data-descript="提交签字" data-functype="SUBMIT" data-action="ProdOriginality.deleteProdOriginality"
               data-size="mini" data-type="danger" data-target="principleGrid" :data-confirm="true" >
          <md-icon md-src="/static/svg/op-log.svg" />
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="附件详情" data-functype="POPUP" data-size="mini"
               data-target="setOriginalityPopup">
          <md-icon>library_books</md-icon>
        </k-btn>

        <k-btn class="md-danger md-just-icon md-simple" data-descript="未上会" data-functype="SUBMIT" data-action="ProdOriginality.updateIsCommunicate"
               data-size="mini" data-type="danger" data-target="principleGrid" :data-confirm="true">
          <md-icon>undo</md-icon>
        </k-btn>
      </template>
    </k-grid>



    <!--    添加  -->
    <k-popup ref="addOriginalityPopup" data-title="新增">
      <k-form ref="addOriginalityForm" :data-col="2">

        <k-form-item label="创意名称">
          <k-field-text v-model="AddDataFrom.originalityName"  :data-max-length="100"  :dataAllowblank="false"  />
        </k-form-item>

        <k-form-item label="系列名称">
          <k-field-select v-model="AddDataFrom.t8ProdSeriesId"  :dataAllowblank="false" data-action="ProdOriginality.getProdSeries" data-display-field="seriesName" data-value-field="t8ProdSeriesId" />
        </k-form-item>

        <k-form-item label="产品名称">
          <k-field-select v-model="AddDataFrom.t8ProdInfoId" data-action="ProdOriginality.getProdInfo" data-display-field="prodCode,prodName" data-value-field="t8ProdInfoId" />
        </k-form-item>

        <k-form-item label="创意发明人">
          <k-field-select v-model="AddDataFrom.sysUserUserid"  data-action="ProdOriginality.getUser" data-display-field="username" data-value-field="sysUserUserid"  :dataAllowblank="false"  data-default-value=""/>
        </k-form-item>

        <k-form-item label="创意描述">
          <k-field-text v-model="AddDataFrom.originalityDescribe" :data-max-length="255" />
        </k-form-item>

        <k-form-item label="是否沟通完成">
          <k-field-select v-model="AddDataFrom.isCommunicate"    :dataAllowblank="false"  data-dict="t8_is_communicate" />
        </k-form-item>

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef"  :data-limit=5
                          :data-error="onSubmitError" :data-success="onSubmitSuccess" :data-multiple="true"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/prodOriginality/uploadAdd.json">
          </k-field-upload>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="principleGrid" ref="submitBtn"
                 data-from="addOriginalityForm" :data-model="AddDataFrom" :data-handler="submitUploadParam">确定
          </k-btn>


          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>



    <!--    修改  -->
    <k-popup ref="editOriginalityPopup" data-title="修改">
      <k-form ref="editOriginalityForm" :data-col="2">

        <k-form-item label="创意名称">
          <k-field-text v-model="ProdOriginality.originalityName"  :data-max-length="100"  :dataAllowblank="false"  data-disabled/>
        </k-form-item>

        <k-form-item label="系列名称">
          <k-field-select v-model="ProdOriginality.t8ProdSeriesId" :dataAllowblank="false" data-action="ProdOriginality.getProdSeries" data-display-field="seriesName" data-value-field="t8ProdSeriesId" />
        </k-form-item>

        <k-form-item label="产品名称">
          <k-field-select v-model="ProdOriginality.t8ProdInfoId" data-action="ProdOriginality.getProdInfo" data-display-field="prodCode,prodName" data-value-field="t8ProdInfoId"  />
        </k-form-item>

        <k-form-item label="创意发明人">
          <k-field-select v-model="ProdOriginality.sysUserUserid"   data-action="ProdOriginality.getUser" data-display-field="username" data-value-field="sysUserUserid"  :dataAllowblank="false" />
        </k-form-item>

        <k-form-item label="创意描述">
          <k-field-text v-model="ProdOriginality.originalityDescribe" :data-max-length="255" />
        </k-form-item>

        <k-form-item label="是否沟通完成">
          <k-field-select v-model="ProdOriginality.isCommunicate"     :dataAllowblank="false"  data-dict="t8_is_communicate"  />
        </k-form-item>

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="updateRef"  :data-limit=5
                          :data-error="onUpdateError" :data-success="onUpdateSuccess" :data-multiple="true"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/prodOriginality/uploadUpdate.json">
          </k-field-upload>
        </k-form-item>

         <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="principleGrid" ref="updateBtn"
                 data-from="editOriginalityForm" :data-model="ProdOriginality" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
















  </div>

</template>

<script>
import {assign} from "lodash";
import MD5 from "@/frame/md5";

export default {
  data() {
    return {
      selectRowData: {},
      ProdOriginality:{},
      AddDataFrom:{}
    };
  },

  computed: {
    // queryOrgGraphql() {
    //   return "{queryOrg(action:\"findChildren\",orgno:\"" + this.userOrgno +
    //     "\") {rows{orgid, orgname, parentorgno, orgno},results}}"
    // },
    // querydeptGraphql() {
    //   return "{queryDept(action:\"find\") {rows{deptno, deptname, parentdeptno, deptid},results}}"
    // }
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = assign({}, row)
      this.ProdOriginality = assign({}, row)

    },

    submitUploadParam() {
      let AddDataFrom = this.AddDataFrom;
      this.$refs.uploadRef.upload(AddDataFrom);
    },

    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
    },
    onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addOriginalityForm.reset();
      this.$refs.addOriginalityPopup.close();
      this.$refs.principleGrid.load();
    },





    updateUploadParam() {
      let ProdOriginality = this.ProdOriginality;
      this.$refs.updateRef.upload(ProdOriginality);
    },

    onUpdateError() {
      this.$refs.updateRef.doReset();
      this.$refs.updateBtn.setIconStyle(1, []);
    },
    onUpdateSuccess() {
      this.$refs.updateRef.doReset();
      this.$refs.editOriginalityForm.reset();
      this.$refs.editOriginalityPopup.close();
      this.$refs.principleGrid.load();
    },
















    stopDistributor(params) {
      alert(params.isState);
      if (params.isState == "1") {
        return false;
      } else {
        return true;
      }
    },



  }
};
</script>

<style scoped>

</style>
