<template>
  <div>
    <div style="min-height:225px;">
      <div class="add-btn-div-29">
        <div class="add-btn"  @click="addHandler" >+
        </div>
      </div>
      <k-grid id = "TaProdInterestGrid"
              ref="TaProdInterestGrid"
              data-action='T82012.findTa2012'
              @data-row-select="selectRow"
              @init="(grid)=>{this.$kgrid = grid}"
              data-operate-column-position="end"
              data-operate-width="300px"
              :data-autoload="false">
          <k-grid-column data-header="文件类型名称" data-name="fileName"  data-width="200px" ></k-grid-column>
          <k-grid-column data-header="业务编码" data-name="busiCode" ></k-grid-column>
          <k-grid-column data-header="索引表index" data-name="indexNo" data-width="100px" ></k-grid-column>
          <k-grid-column data-header="文件名称" data-name="nameRules"  data-width="200px"></k-grid-column>
          <k-grid-column data-header="是否导出" data-name="effectStatus" data-dict="1yes0no"></k-grid-column>
          <k-grid-column data-header="明细文件类型" data-name="fileType"  data-dict="fileTypeTa"></k-grid-column>
          <k-grid-column data-header="数据源名称" data-name="datasource" data-dict="datasourceTa"></k-grid-column>

          <template slot="operate">

            <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
                  data-target="updateTaIntfPopup" data-descript="修改中登明细文件" @submit.native.prevent>
              <md-icon>edit</md-icon>
            </k-btn>

            <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除中登明细文件"
                  data-target="TaProdInterestGrid" data-action="T82012.deleteTa2012" data-confirm data-type="danger" >
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

        <k-form-item label="文件类型名称">
          <k-field-text v-model="rowData.fileName"  ref="fileName" :dataAllowblank='false' :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="业务编码">
          <k-field-text v-model="rowData.busiCode"  ref="busiCode" :dataAllowblank='false' :data-max-length="5"/>
        </k-form-item>
        <k-form-item label="索引号">
          <k-field-select v-model="rowData.indexNo"  ref="indexNo" data-action="T82010.findTa2010"
                          data-display-field="indexNo,indexName"  data-value-field="indexNo" :dataAllowblank='false'
                          :data-params='{"distributorCode":rowData.distributorCode}'/>
        </k-form-item>
        <k-form-item label="文件名称">
          <k-field-text v-model="rowData.nameRules" ref="nameRules" :dataAllowblank='false' :data-max-length="100" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="是否导出">
          <k-field-radio v-model="rowData.effectStatus"  data-dict="lyesOno" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="明细文件类型">
          <k-field-select v-model="rowData.fileType"  data-dict="fileTypeTa" :data-allowblank="false"/>
        </k-form-item>
       <k-form-item label="数据源名称">
          <k-field-radio v-model="rowData.datasource" data-dict="datasourceTa" :data-allowblank="false" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82012.addTa2012"
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
        <k-form-item label="销售商代码" v-show="false">
          <k-field-select v-model="rowData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                          data-display-field="distributorName"  data-value-field="distributorCode"  :dataAllowblank='false' />
        </k-form-item>

        <k-form-item label="文件类型名称">
          <k-field-text v-model="rowData.fileName"  ref="fileName" :dataAllowblank='false' :data-max-length="100" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="业务编码">
          <k-field-text v-model="rowData.busiCode"  ref="busiCode" :dataAllowblank='false' :data-max-length="5" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="索引号">
          <k-field-select v-model="rowData.indexNo"  ref="indexNo" data-action="T82010.findTa2010"
                          data-display-field="indexNo,indexName"  data-value-field="indexNo" :dataAllowblank='false'
                          :data-params='{"distributorCode":rowData.distributorCode}' :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文件名称">
          <k-field-text v-model="rowData.nameRules" ref="nameRules" :dataAllowblank='false' :data-max-length="100" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="是否导出">
          <k-field-radio v-model="rowData.effectStatus"  data-dict="lyesOno" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="明细文件类型">
          <k-field-select v-model="rowData.fileType"  data-dict="fileTypeTa" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="数据源名称">
          <k-field-radio v-model="rowData.datasource" data-dict="datasourceTa" :data-allowblank="false"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T82012.updateTa2012"
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
        this.$$refs.TaProdInterestGrid.load({"distributorCode": this.distributorCode});*/
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
