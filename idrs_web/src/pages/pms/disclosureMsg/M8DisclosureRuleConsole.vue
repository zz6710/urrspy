<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ObjectGrid" v-model="prodSearchParam">
        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" data-target="addPopup">
          <!-- <md-icon>copy</md-icon> -->
          生成任务
        </k-btn>
        <k-btn slot="button" class="btn-custom-primary">
          <!-- <md-icon>copy</md-icon> -->
          任务导出
        </k-btn>
      </k-form-search-customize>
      <k-grid ref="t8ObjectGrid" data-action="" :data-checkbox="true" data-checkbox-id="prodCode" :data-operate-column="false"
        :dataData="dataList">
        <k-grid-column data-align="center" data-header="任务ID" data-name="taskId"/>
        <k-grid-column data-align="center" data-header="任务创建时间" data-name="crtDate"/>
        <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
        <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
        <k-grid-column data-align="center" data-header="公告类型" data-name="noticeType" />
        <k-grid-column data-align="center" data-header="公告标题" data-name="title" />
        <k-grid-column data-align="center" data-header="发起方式" data-name="launchMode"/>
        <k-grid-column data-align="center" data-header="公告基准日" data-name="noticeRule"/>
        <k-grid-column data-align="center" data-header="计划系统生成日" data-name="planDate"/>
        <k-grid-column data-align="center" data-header="任务所属月份" data-name="ascriptionMonth"/>
      </k-grid>


      <k-popup ref="addPopup" data-title="生成任务" >
        <k-form ref="addForm" :data-col="2" style="height: 300px;margin-top: 50px;">
          <k-form-item label="计划系统生成所在月份" :data-col="2">
            <el-date-picker
                  v-model="formData.ascriptionMonth"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始月份"
                  end-placeholder="结束月份">
                </el-date-picker>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary"
                    :data-handler="saveRule"
                   data-from="addForm" :data-model="formData"
                   data-target="t8ObjectGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定生成
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
  name: "M8DisclosureRuleConsole",
  data() {
    return {
      prodSearchParam: {
        prodCode: '',
      },
      formData: {
        ascriptionMonth: '',
      },
      dataList:{
        rows:[
          {taskId:'10001',crtDate:'20210104',prodCode:'EB2048',prodName:'天利01',noticeType:'发行公告',title:'天利01产品发行公告',launchMode:'自动',noticeRule:'成立日',planDate:'T+1',ascriptionMonth:'1月'}
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
      Tools.alert("生成成功","success");
      this.$refs.addPopup.close();
      return false;
    }
  }
}
</script>

<style scoped>

</style>
