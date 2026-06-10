<template>
  <div>
    <k-grid ref="prodInfoGrid" data-action="T8ProdInfo.findTaProdInfo1" @data-row-select="selectRow"
            data-operate-column-position="end" data-align="center" data-operate-data-width="300px"
            data-operate-column="true" :dataAutoload="true">
      <k-grid-column data-header="id" data-hidden="true" data-name="id"/>
      <k-grid-column data-header="审批流名称" data-name="prodManageName"/>
      <k-grid-column data-header="版本号" data-name="productInvManager"/>
      <k-grid-column data-header="操作人" data-name="isShareSort" data-dict="1yes0no"/>
      <k-grid-column data-header="操作时间" data-name="prodCode"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="查看审批流" data-functype="POPUP" data-size="small"
               data-target="shareBulletBox" v-show="scope.row.row.isShareSort==1" @click="getShareNameList(scope.row.row.id)">
          <md-icon>brightness_low</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="查看表单配置" data-functype="POPUP" data-size="small"
               data-target="prodManualPanal" v-show="showGenerate" @click="prodManual1(scope.row.row)"
               v-if="global.isShowAuthorityButton('T8ProdManualVersion.generateProdManualVersionByProdCode')"
               :data-disabled="scope.row.row.approvalStatus === '1'">
          <md-icon>add_circle</md-icon>
        </k-btn>

      </template>
    </k-grid>
  </div>
</template>

<script>

    export default {
        name: "flowHistoryVersion",

      data() {
        return {
          state:false,
          processId:'',
          dataList:[],
          prodGroupUserItems:[],
          formData:{},
          params:{},
          flowTemplateList:[],
          value: [],
          copyFormData : {
            checkList: [],
          },
          envItems: [{"ratioIndex": ''}, {"coefficient": ''}],
          historyVersionForm:{},
        };
      },

      methods:{
    // /getTemplateByServerOrId.json
      },


    }

</script>

<style scoped>

</style>
