<template>
  <div>
    <k-form-search-customize data-target="prodMeetingGrid" v-model="queryParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="queryParam.prodCode"  data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="queryParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品形态">
        <k-field-select v-model="queryParam.prodMode" data-dict="t8_prod_create_type"></k-field-select>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="queryParam.prodStatus" data-dict="t8_prod_status"></k-field-select>
      </k-form-item>
      <k-form-item label="是否完成发行参数确认" data-input-width="164px" data-label-width="180px">
        <k-field-select v-model="queryParam.isCompleteConfirm" data-dict="is_default"></k-field-select>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="prodMeetingGrid" data-action='T8ProdAllAdjustConfirm.findT8ProdInfoAdjustConfirms1' @data-row-select="selectRow"
            data-operate-column-position="end" data-operate-width="200px">
      <k-grid-column data-header="产品id" data-name="id" :data-hidden="true"/>
      <k-grid-column data-header="产品代码" data-name="prodCode" />
      <k-grid-column data-header="产品名称" data-name="prodName" />
      <k-grid-column data-header="产品形态" data-name="prodMode" data-dict="t8_prod_create_type" />
      <k-grid-column data-header="募集方式" data-name="raiseType" data-dict="t8_raise_type" />
      <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status" />
      <k-grid-column data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status" />
      <k-grid-column data-header="确认人" data-name="issueCrtUser"  />
      <k-grid-column data-header="确认时间" data-name="issueCrtDate" data-type="date" data-render="renderdeclaraCrtDate" data-width="150"/>
      <template slot="operate" slot-scope="props">
        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(props.row.row.id)"
               :data-model="props.row.row" @click="toEditProdInfo(props.row.row)"
               :prodInfoId="props.row.row.id" data-descript="产品信息详情">
          <md-icon>library_books</md-icon>
        </k-btn>
        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(props.row.row.id)&&
               global.isShowAuthorityButton('T8ProdAllAdjustConfirm.parameterConfirmation')"
               :data-model="props.row.row" @click="popupEdit(props.row.row)" :prodInfoId="props.row.row.id" data-descript="产品发行参数确认"
               v-show="showConfirm">
          <md-icon>done</md-icon>
        </k-btn>
      </template>
    </k-grid>

  </div>
</template>

<script>
  import { assign } from "lodash";
  import Tools from "@/utils/tools";


  export default {
    name: "ProdIssueAdjustList",
    data() {
      return {
        queryParam:{},
        formData: {
        },
        cascaderValue: [],
        selectRowData: {},
        showConfirm:true,//是否显示确认按钮
      };
    },
    created() {
      this.global.getProdUser('');
      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
        let prodCode = this.$route.query.prod_code;
        if(prodCode !=''&&prodCode!=undefined){
          this.$refs.prodMeetingGrid.load({prodCode:prodCode});
        }
      });
    },
    methods: {
      renderdeclaraCrtDate(row) {
        return Tools.formatDateTime(row.issueCrtDate, row.issueCrtTime);
      },
      toEditProdInfo(row){
        let pathUrl = '/main/pms/M81/prodDisplay/M81001display';
        this.$router.push({
          path: pathUrl,
          query: {prodMode: row.prodMode,prodInfoId:row.id,prodCode: row.prodCode,assemblyMenuType:'3',menuName:'ProdIssueAdjustList'},
        });

      },

      popupEdit(row){
        let pathUrl = '/main/pms/M81/prodInfoGD/M81001add';
        this.$router.push({
          path: pathUrl,
          query: {prodMode: row.prodMode,prodModeId: row.prodModeId,findProdCode:row.prodCode, findProdName:row.prodCode,
            findProdMode:row.prodMode, prodInfoId:row.id,prodCode: row.prodCode,assemblyMenuType:'3',assembly_id:'prodIssueAdjust',
          menuName:'ProdIssueAdjustList'},
        });

      },

      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row)
      },
    },
    computed: {
      value() {
        return this.$attrs.value
      }
    }
  };
</script>
