<template>
  <div class="tab-page">
    <k-form-search data-target="SystemParamGrid" data-model-name="SystemParam"  data-label-width="70px" />

    <k-grid ref="SystemParamGrid" data-action='SystemParam.findSysParams1' :data-autoload="true" :dataPopupAppendToBody="true"
            @data-row-select="selectRow" data-operate-column-position="end" data-operate-width="300px">
      <k-grid-column data-header="参数ID" data-name="paraid"  data-align="left"></k-grid-column>
      <k-grid-column data-header="参数名称" data-name="paraname" ></k-grid-column>
      <k-grid-column data-header="参数值" data-name="paravalue"  data-align="center"></k-grid-column>
<!--      <k-grid-column data-header="分组ID" data-name="groupparaid"></k-grid-column>-->

      <template slot="operate">
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple"
               data-target="editSystemParamPopup" data-descript="修改参数" v-if="global.isShowAuthorityButton('SystemParam.updateSysParam')">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <!--   修改   -->
    <k-popup ref="editSystemParamPopup" data-title="修改">
      <k-form ref="editSystemParamForm" :data-col="2">

        <k-form-item label="参数名称">
          <k-field-text v-model="formData.paraname" :data-max-length="128"   data-disabled data-clearable="false" />
        </k-form-item>

        <k-form-item label="参数值"  v-if="formData.functype==='select'">
          <k-field-select v-model="formData.paravalue" :data-dict="formData.dict" :dataAllowblank="false"
                          :data-max-length="256"  v-bind="formData.confoption" />
        </k-form-item>

        <k-form-item label="参数值" v-else-if="formData.functype==='radio'">
          <k-field-radio v-model="formData.paravalue" :data-dict="formData.dict"  :dataAllowblank="false"
                         :data-max-length="256"  v-bind="formData.confoption" />
        </k-form-item>

        <k-form-item label="参数值" v-else-if="formData.functype==='time'">
          <k-field-time v-model="formData.paravalue" :dataAllowblank="false" :data-max-length="256"
                        v-bind="formData.confoption" />
        </k-form-item>

        <k-form-item label="参数值" v-else-if="formData.functype==='date'">
          <k-field-date v-model="formData.paravalue"  :dataAllowblank="false" :data-max-length="256"
                        v-bind="formData.confoption" />
        </k-form-item>

        <k-form-item label="参数值" v-else-if="formData.functype==='text'">
          <k-field-text v-model="formData.paravalue"  :dataAllowblank="false" :data-max-length="256"
                        v-bind="formData.confoption" />
        </k-form-item>

        <k-form-item label="参数值" v-else>
          <k-field-text v-model="formData.paravalue"  :dataAllowblank="false" :data-max-length="256"
                        :data-validate-type="formData.functype"  v-bind="formData.confoption" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SystemParam.updateSysParam"
                 data-from="editSystemParamForm" :data-model="formData" :data-handler="changeConfigOptionToStr"
                 data-target="SystemParamGrid" :data-after-success="refreshSysWorkDay">
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
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  inject: ['reload'],
  name: "tabSysParam.vue",
  props: {
    prodCode: '',
    prodDate: '',
  },
  data() {
    return {
      formData: {},
      cascaderValue: []
    }
  },
  methods: {
    changeConfigOptionToStr(o) {
      let confoption = o.confoption;
      if (confoption) {
        o.confoption = JSON.stringify(confoption);
      }
      return o;
    },
    gridValueRender(row, item) {
      if (row.dictValue){
        return row.dictValue;
      } else {
        return row.paravalue;
      }
    },
    selectRow(row) {
      let temp = assign({}, row)
      let confoption = temp.confoption;

      if (confoption) {
        temp.confoption = JSON.parse(confoption)
      }
      this.formData = temp
    },
    dataBeforeLoad() {
      return {"excOrgno":"ROOT"}
    },
    updSuccess(pop) {
      this.$refs.SystemParamGrid.load()
      pop.close()
    },
    //刷新当前页面时间及登录用户
    refreshSysWorkDay (){
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.currentWorkday = Tools.formatDate(res)
        }
      })
      this.reload();//刷新页面
    },
  }
}
</script>

<style scoped>

</style>
