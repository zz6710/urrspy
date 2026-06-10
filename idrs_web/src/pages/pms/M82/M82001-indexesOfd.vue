<template>
  <div>
    <div style="min-height:225px;">
      <div class="add-btn-div-29">
        <div class="add-btn"  @click="addHandler" >+
        </div>
      </div>
      <k-grid id = "TaProdInterestGrid"
              ref="TaProdInterestGrid"
              data-action='T82011.findTa2011'
              @data-row-select="selectRow"
              @init="(grid)=>{this.$kgrid = grid}"
              data-operate-column-position="end"
              data-operate-width="300px"
              :data-autoload="false">
        <k-grid-column data-header="销售商代码" data-name="distributorCode" ></k-grid-column>
        <k-grid-column data-header="索引文件类型" data-name="indexesFileType" data-dict="indexesFileType"></k-grid-column>
        <k-grid-column data-header="OFD文件类型" data-name="ofdFileType" data-dict="ofdFileType" ></k-grid-column>
        <k-grid-column data-header="是否跳过" data-name="isSkip" data-dict="1yes0no"></k-grid-column>
        <k-grid-column data-header="是否有确认文件" data-name="isVaildatingProfile" data-dict="1yes0no"></k-grid-column>
        <template slot="operate">

          <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                 data-target="updateTaIntfPopup" data-descript="修改OFD文件" @submit.native.prevent>
            <md-icon>edit</md-icon>
          </k-btn>

          <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除OFD文件"
                 data-target="TaProdInterestGrid" data-action="T82011.deleteTa2011" data-confirm data-type="danger" >
            <md-icon>close</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </div>

    <!--    添加弹出框   -->
    <k-popup ref="addPopup" data-title="新增">
      <k-form ref="addTaIntfForm" :data-col="2">

        <k-form-item label="销售商代码" v-show="false">
          <k-field-select v-model="rowData.distributorCode" :dataAllowblank='false' />
        </k-form-item>

        <k-form-item label="索引文件类型">
          <k-field-select v-model="rowData.indexesFileType" :dataAllowblank='false' data-dict="indexesFileType"/>
        </k-form-item>
        <k-form-item label="OFD文件类型">
          <k-field-select v-model="rowData.ofdFileType" data-dict="ofdFileType" :dataAllowblank='false'/>
        </k-form-item>

        <k-form-item label="是否跳过">
          <k-field-radio v-model="rowData.isSkip" data-dict="1yes0no"/>
        </k-form-item>

        <k-form-item label="是否有确认文件">
          <k-field-radio v-model="rowData.isVaildatingProfile" data-dict="1yes0no"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82011.addTa2011"
                 data-from="addTaIntfForm" :data-model="rowData" :data-handler="addConfirmHandler"
                 data-target="TaProdInterestGrid">
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

        <k-form-item label="销售商代码" >
          <k-field-select v-model="rowData.distributorCode" data-action="T8Dict.findTaDistributorInfos"  :data-multiple="false"
                        data-display-field="distributorName"  data-value-field="distributorCode"  :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="产品代码" data-input-width="600px" v-show="false">
          <k-field-select v-model="rowData.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName"  data-value-field="prodCode" />
        </k-form-item>

        <k-form-item label="索引文件类型">
          <k-field-text v-model="rowData.indexesFileType" :data-disabled="true" data-dict="indexesFileType"/>
        </k-form-item>
        <k-form-item label="OFD文件类型">
          <k-field-text v-model="rowData.ofdFileType" data-dict="ofdFileType" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="是否跳过">
          <k-field-radio v-model="rowData.isSkip" data-dict="1yes0no"/>
        </k-form-item>

        <k-form-item label="是否有确认文件">
          <k-field-radio v-model="rowData.isVaildatingProfile" data-dict="1yes0no"/>
        </k-form-item>


        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82011.updateTa2011"
                 data-from="updateTaIntfForm" :data-model="rowData"
                 data-target="TaProdInterestGrid">
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
      distributorCode:String,
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
        if(params.distributorCode==null || params.distributorCode==''){
          Tools.alert("未录入销售商代码:","danger");
          return false;
        }
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


  ::v-deep #TaProdInterestGrid > div:first-child{
    box-shadow: none;
  }

  ::v-deep #TaProdInterestGrid > div:first-child > div:first-child{
    display: none;
  }

  ::v-deep #TaProdInterestGrid .el-table--scrollable-x .el-table__body-wrapper{
    overflow-x: hidden;
  }


</style>
