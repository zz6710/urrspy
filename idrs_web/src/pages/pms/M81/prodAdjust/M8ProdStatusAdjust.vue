<template>
  <div>
  <k-form-search-customize data-target="prodStateAdjustGrid" v-model="prodSearchParam">

    <k-form-item label="产品代码">
      <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                      data-display-field="prodCode,prodName" data-value-field="prodCode"/>
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
    </k-form-item>
    <k-form-item label="代码回收">
      <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
    </k-form-item>
  </k-form-search-customize>
  <k-grid ref="prodStateAdjustGrid" data-action="ProdStatusChange.findAdjustProdInfoList1" @data-row-select="selectRow">
    <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
    <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" />
    <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>
    <k-grid-column data-align="center" data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status"/>
    <k-grid-column data-align="center" data-header="是否代码回收" data-name="isRecycleCode" data-hidden="true"/>
    <template slot="operate" slot-scope="scope">
      <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP" data-target="changeStatusPopup"
             data-descript="调整产品状态至发行失败"   data-size="small"   v-if="global.getProdIfUser(scope.row.row.id)&&
             global.isShowAuthorityButton('ProdStatusChange.adjustProductStatus')"
             @click="changeStatusValue(scope.row.row,1,5,13,1)"
             :data-disabled="scope.row.row.prodSonStatus=='13' || scope.row.row.prodStatus=='9'"
      >
        <md-icon>edit_attributes</md-icon>
      </k-btn>
      <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP" data-target="changeStatusPopup"
             data-descript="调整产品状态至产品终止" :data-disabled="scope.row.row.prodStatus=='6' || scope.row.row.prodStatus=='7' || scope.row.row.prodStatus=='8' || scope.row.row.prodStatus=='9'"
             data-size="small" v-if="global.getProdIfUser(scope.row.row.id)&&
             global.isShowAuthorityButton('ProdStatusChange.adjustProductStatus2')"
             @click="changeStatusValue(scope.row.row,0,9,18,2)">
        <md-icon>edit_attributes</md-icon>
      </k-btn>
      <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP" data-target="recycleCodeList"
             data-descript="产品代码回收记录" @click="popHandler(scope.row.row)" data-size="small">
        <md-icon>library_books</md-icon>
      </k-btn>
    </template>
  </k-grid>
    <k-grid ref="changeInfoGrid" :data-autoload="false" data-action="ProdStatusChange.findProdStatusChange" data-operate-column="false">
      <k-grid-column data-align="center" data-header="调整类型" data-name="adjustType" data-dict="t8_adjust_type"></k-grid-column>
      <k-grid-column data-align="center" data-header="调整原因" data-name="adjustCause"></k-grid-column>
      <k-grid-column data-align="center" data-header="调整日期" data-name="crtDate" data-type="date"></k-grid-column>
      <k-grid-column data-align="center" data-header="调整时间" data-name="crtTime" data-type="time"></k-grid-column>
      <k-grid-column data-align="center" data-header="调整人" data-name="crtUser"></k-grid-column>
    </k-grid>




   <k-popup ref="changeStatusPopup" data-title="调整产品状态">
      <k-form ref="changeStatusForm">
        <k-form-item label="是否回收代码" v-show="this.isRecycle&&(this.nowProdStatus==='1'||this.nowProdStatus==='2')">
          <k-field-radio v-model="ProdStatusChange.isRecycle" @data-on-change="showRecycle" data-dict="1yes0no" data-default-value="0" :dataAllowblank="false" />
        </k-form-item>
        <k-form-item label="原产品代码" v-show="this.isShowRecycle">
          <k-field-text v-model="ProdStatusChange.prodCodeOld" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称" v-show="this.isShowRecycle">
          <k-field-text v-model="ProdStatusChange.prodName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="新产品代码" v-show="this.isShowRecycle">
          <k-field-text v-model="ProdStatusChange.prodCodeNew" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="新产品名称" v-show="this.isShowRecycle">
          <k-field-text v-model="ProdStatusChange.prodNameNew" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="调整原因" :dataCol="2">
          <k-field-text v-model="ProdStatusChange.adjustCause" :data-allowblank="false" :rows="3"
                        inputType="textarea" :data-max-length="200"></k-field-text>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="ProdStatusChange.insertProdStatusChange"
                 data-from="changeStatusForm" :data-model="ProdStatusChange"
                 data-target="prodStateAdjustGrid" :data-after-success="refresh">
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
   </k-popup>

    <k-popup ref="recycleCodeList" title="代码回收记录"  :data-dialog-drag="true" data-width="1200px">
      <k-grid ref="recycleGrid"  data-action="RecycleCode.findRecycleCodes" :data-autoload="false" :data-page-size="0" data-height="500px" data-operate-column="false"
              data-display="false" style="height: 600px; overflow: auto;">
        <k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="原产品代码" data-name="prodCodeOld"></k-grid-column>
        <k-grid-column data-header="新产品代码" data-name="prodCodeNew"></k-grid-column>
        <k-grid-column data-header="原产品ID" data-name="t8ProdInfoId"></k-grid-column>
        <k-grid-column data-header="产品创建人id" data-name="codeCrtUserId" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="产品创建人姓名" data-name="codeCrtUserName"></k-grid-column>
        <k-grid-column data-header="操作人id" data-name="crtUserId" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="操作人姓名" data-name="crtUserName"></k-grid-column>
        <k-grid-column data-header="操作日期" data-name="crtDate" data-type="date"></k-grid-column>
        <k-grid-column data-header="操作时间" data-name="crtTime" data-type="time"></k-grid-column>
        <k-grid-column data-header="备注" data-name="adjustCause"></k-grid-column>
        </k-grid>
    </k-popup>

  </div>
</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";
export default {
  name: "ProdInfoList.vue",
  data(){
    return{
      prodSearchParam: {
        prodCode: ''
      },
      formData:{
        prodCode:'',
        prodName:'',
      },
      ProdStatusChange:{},
      t8ProdInfoId:'',
      isRecycle:false,
      isShowRecycle:false,
      rowData:{},
      nowProdStatus:1,
    }
  },
  created() {
    this.global.getProdUser('');
  },
  methods:{
    popHandler(value){
      this.$refs.recycleCodeList.popup();
      this.$nextTick(()=>{
        if("1"===value.isRecycleCode){
          this.$refs.recycleGrid.load({prodCodeNew : value.prodCode});
        }else{
          this.$refs.recycleGrid.load({prodCodeOld : value.prodCode});
        }
      });
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    /* 加代码回收记录*/
    dataEcho(value){
      const _this = this;

      //重新加载表格
      this.$refs.recycleGrid.load({t8RiskTemplateVersionId: _this.prodRiskRat.t8RiskTemplateVersionId, t8ProdInfoId: _this.prodRiskRat.t8ProdInfoId});
      },
    /**
     * 展示代码回收相关字段
     */
    showRecycle(value)  {
      if('0'===value){
        this.isShowRecycle=false;
      }else{
        this.isShowRecycle=true;
      }
    },
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      this.t8ProdInfoId = _this.selectRowData.id;
      this.$refs.changeInfoGrid.load({t8ProdInfoId:this.t8ProdInfoId})
    },
    //点击发行失败
    changeStatusValue(value,isRecycle,prodStatus,prodSonStatus,adjustType){
      this.ProdStatusChange={};
      this.isRecycle=false;
      this.isShowRecycle=false;
      this.httpUtil.comnQuery({
        action: 'RecycleCode.findNewRecycleCodes',
        params: {
        }
      }).then(data => {
        if(data.rows!=null &&data.rows.length>0){
          let newCode = data.rows[0].prodCodeNew;
          let code = newCode.split("_")[0];
          let r = code.match(/\d+/); //s.match(/^([^\d]+)([\d]+)([^\d]+.+)$/);
          if(r){
            let num = parseInt(r[0])+1;
            let str = num.toString().padStart(5, '0');
            console.log("str=:>>",str);
            if(1===isRecycle){
              this.isRecycle=true;
              //this.ProdStatusChange.prodName = value.prodName;
              //this.ProdStatusChange.prodCodeNew = "Fail"+value.prodCode;
              this.ProdStatusChange.prodCodeNew = "Fail"+str+"_"+value.prodCode;
              this.ProdStatusChange.prodNameNew = value.prodName+"("+str+"已回收)";
            }else{
              this.isRecycle=false;
            }
          }else{
            if(1===isRecycle){
              this.isRecycle=true;
            }else{
              this.isRecycle=false;
            }
            this.ProdStatusChange.prodCodeNew = "Fail00001_"+value.prodCode;
            this.ProdStatusChange.prodNameNew = value.prodName+"(0001已回收)";
          }
        }else{
          if(1===isRecycle){
            this.isRecycle=true;
          }else{
            this.isRecycle=false;
          }
          //this.ProdStatusChange.prodCodeNew = "EF00001"
          this.ProdStatusChange.prodCodeNew = "Fail00001_"+value.prodCode;
          this.ProdStatusChange.prodNameNew = value.prodName+"(0001已回收)";
        }
      });
      console.log("value=:>>>>>>",value);
      this.ProdStatusChange.prodCodeOld = value.prodCode;
      this.ProdStatusChange.prodName = value.prodName;
      this.nowProdStatus = value.prodStatus;
      this.ProdStatusChange.prodCode = value.prodCode;
      this.ProdStatusChange.prodName = value.prodName;
      this.ProdStatusChange.t8ProdInfoId = value.id;
      this.ProdStatusChange.prodStatus =prodStatus;
      this.ProdStatusChange.prodSonStatus=prodSonStatus;
      this.ProdStatusChange.adjustType=adjustType;
    },
    //刷新二级查询
    refresh(){
      this.$refs.changeInfoGrid.load({t8ProdInfoId:this.t8ProdInfoId})
    }
  },
}
</script>

<style scoped>

</style>
