<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ObjectGrid" v-model="prodSearchParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-btn slot="button"  class="btn-custom-primary" >
          <!-- <md-icon>copy</md-icon> -->
          导出
        </k-btn>
      </k-form-search-customize>
      <k-grid ref="t8ObjectGrid" :dataData="dataList">
        <k-grid-column data-header="公告编号" data-name="noticeNo"/>
        <k-grid-column data-header="产品代码" data-name="prodCode"/>
        <k-grid-column data-header="产品名称" data-name="prodName"/>
        <k-grid-column data-header="公告名称" data-name="noticeName"/>
        <k-grid-column data-header="发起方式" data-name="launchMode" />
        <k-grid-column data-header="当前阶段" data-name="currPhase" />
        <k-grid-column data-header="基准日期 T" data-name="baseDate"/>
        <k-grid-column data-header="初始确认" data-name="initStatus">
          <template slot-scope="props">
            <i class="el-icon-success" style="color: #00d400;font-size: 20px;"></i>
          </template>
        </k-grid-column>
        <k-grid-column data-header="数据收集" data-name="dataCollection">
          <template slot-scope="props">
              <div style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"></div>
            </template>
          </k-grid-column>
        <k-grid-column data-header="公告审批" data-name="noticeApproval">
          <template slot-scope="props">
              <div style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"></div>
            </template>
          </k-grid-column>
        <k-grid-column data-header="系统发布" data-name="systemRelease">
          <template slot-scope="props">
              <div style="margin: auto;border-radius: 50%;width: 18px;height: 18px;background-color: white; border: solid 2px; border-color: #d3d3d3;"></div>
            </template>
          </k-grid-column>
        <k-grid-column data-header="系统生成时间" data-name="crtDate"/>
        <k-grid-column data-header="计划-补录完成日期" data-name="supplementDate"/>
        <k-grid-column data-header="计划-审批完成日期" data-name="approvalDate"/>
        <k-grid-column data-header="计划-公告发布日期" data-name="releaseDate"/>
        <k-grid-column data-header="实际-补录完成日期" data-name="actualSupplementDate"/>
        <k-grid-column data-header="实际-审批完成日期" data-name="actualApprovalDate"/>
        <k-grid-column data-header="实际-公告发布日期" data-name="actualReleaseDate"/>
      </k-grid>


      <k-popup ref="addPopup" data-title="复制信披规则">
        <k-form ref="addForm" :data-col="1">
          <k-form-item label="选择产品(多选)">
            <k-field-select v-model="formData.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                            data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"/>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                   :data-handler="saveRule"
                   data-from="addForm" :data-model="formData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

    </div>
  </div>


</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "M8DisclosureRuleReport",
  data() {
    return {
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        prodCode: '',
        prodName: '',
        feeJson:'',
      },
      dataList:{
        rows:[
          {noticeNo:'00001',prodCode:'EB2048',prodName:'天利01',noticeName:'天利01发行公告',launchMode:'时规则方式',currPhase:'补录',baseDate:'2020/1/26',initStatus:'是',dataCollection:'否',noticeApproval:'否',systemRelease:'否',crtDate:'2020/1/27',supplementDate:'2020/1/27',approvalDate:'2020/1/27',releaseDate:'2020/1/28',actualSupplementDate:'2020/1/27',actualApprovalDate:'',actualReleaseDate:''}
        ]
      },
    }
  },
  watch:{
  },
  created() {
  },
  methods: {
    saveRule(params){
      Tools.alert("保存成功","success");
      this.$refs.addPopup.close();
      return false;
    }
  }
}
</script>

<style scoped>

</style>
