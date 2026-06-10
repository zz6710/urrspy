<template>
  <div>
    <div style="min-height:225px;">
      <div class="add-btn-div-29">
        <div class="add-btn"  @click="addHandler" >+
        </div>
      </div>
      <k-grid id = "TaImpIndexesFile"
              ref="TaImpIndexesFileRef"
              data-action='T82016.findTa2016'
              @data-row-select="selectRow"
              @init="(grid)=>{this.$kgrid = grid}"
              data-operate-column-position="end"
              data-operate-width="300px"
              :data-autoload="false">
          <k-grid-column data-header="索引文件名" data-name="indexesFileName"  data-width="200px"  ></k-grid-column>
          <k-grid-column data-header="索引文件类型" data-name="indexesFileType"  data-dict="indexesFileType"></k-grid-column>
          <k-grid-column data-header="是否有确认文件" data-name="isVaildatingProfile" data-dict="lyesOno" ></k-grid-column>
        <template slot="operate">

          <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                 data-target="updateTaIntfPopup" data-descript="修改索引导入文件" @submit.native.prevent>
            <md-icon>edit</md-icon>
          </k-btn>

          <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除索引导入文件"
                 data-target="TaImpIndexesFileRef" data-action="T82016.deleteTa2016" data-confirm data-type="danger" >
            <md-icon>close</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </div>

    <!--    添加弹出框   -->
    <k-popup ref="addPopup" data-title="新增">
      <k-form ref="addTaIntfForm" :data-col="2">

        <k-form-item label="销售商代码" v-show="false">
          <k-field-select v-model="rowData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                        data-display-field="distributorName"  data-value-field="distributorCode"  :dataAllowblank='false' />
        </k-form-item>
        <k-form-item label="索引文件名">
          <k-field-text v-model="rowData.indexesFileName"  ref="indexesFileName" :dataAllowblank='false' :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="索引文件类型">
          <k-field-select v-model="rowData.indexesFileType" data-dict="indexesFileType" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="是否有确认文件">
          <k-field-radio v-model="rowData.isVaildatingProfile"  data-dict="lyesOno" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82016.addTa2016"
                 data-from="addTaIntfForm" :data-model="rowData" :data-handler="addConfirmHandler"
                 data-target="TaImpIndexesFileRef">
            <md-icon md-src="/static/svg/confirm.svg" />确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg" />取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>



    <!--    修改弹出框   -->
    <k-popup ref="updateTaIntfPopup" data-title="修改">
      <k-form ref="updateTaIntfForm" :data-col="2">
        <k-form-item label="销售商代码" v-show="false">
          <k-field-select v-model="rowData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                          data-display-field="distributorName"  data-value-field="distributorCode"  :dataAllowblank='false' />
        </k-form-item>
        <k-form-item label="索引文件名">
          <k-field-text v-model="rowData.indexesFileName"  ref="indexesFileName" :dataAllowblank='false' :data-max-length="100" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="索引文件类型">
          <k-field-select v-model="rowData.indexesFileType" data-dict="indexesFileType"  :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="是否有确认文件">
          <k-field-radio v-model="rowData.isVaildatingProfile"  data-dict="lyesOno" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82016.updateTa2016"
                 data-from="updateTaIntfForm" :data-model="rowData"
                 data-target="TaImpIndexesFileRef">
            <md-icon md-src="/static/svg/confirm.svg" />确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg" />取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>


  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  import Tools from '@/utils/tools.js'
  import { assign } from "lodash"

  export default {
    props: {
      updSuccess: Function,
      infos:{},
      distributorCode:{
        type:String,
        default:'',
      },
    },
    data() {
      return {
        $kgrid : null,
        rowData:{},
        gridQueryData:{},
        interestData:{},
        row: null,
      };
    },
    mounted(){
      if(this.distributorCode != ''){
        this.gridQueryData.distributorCode = this.distributorCode;
       /* this.$kgrid.load(this.gridQueryData);
        this.$$refs.TaImpIndexesFile.load({"distributorCode": this.distributorCode});*/
      }

    },
    methods: {
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.rowData = assign({}, row)

        console.log(row);
      },
      addHandler(){
        this.rowData={
          distributorCode : this.distributorCode
        }
        this.$refs.addPopup.popup();
      },
      addConfirmHandler(params){
        params.distributorCode = this.distributorCode;
        console.log(this.rowData);
        if(params.distributorCode==null || params.distributorCode==''){
          Tools.alert("未录入销售商代码:","danger");
          return false;
        }
        return true;
      },

    },
    watch: {
      //父页面传递的 销售商值变化时 更新此页面
      distributorCode: function(){
        this.gridQueryData.distributorCode  = this.distributorCode;
        this.$kgrid.load(this.gridQueryData);

      },
      'gridQueryData.prodCode'(){
        this.$kgrid.load(this.gridQueryData);
      },
    },

  };
</script>


<style lang="scss" scoped>

  @import "src/styles/T82001";


  ::v-deep #TaImpIndexesFile > div:first-child{
    box-shadow: none;
  }

  ::v-deep #TaImpIndexesFile > div:first-child > div:first-child{
    display: none;
  }

  ::v-deep #TaImpIndexesFile .el-table--scrollable-x .el-table__body-wrapper{
    overflow-x: hidden;
  }


</style>
