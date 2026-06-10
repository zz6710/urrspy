<template>
  <div>
<!--   <k-form-search data-target="prodMeetingGrid" data-model-name="T8ProdInfo">
    </k-form-search>-->
    <k-form-search-customize data-target="prodMeetingGrid" v-model="T8ProdInfo">

      <k-form-item label="产品代码">
        <k-field-select v-model="T8ProdInfo.prodCode" data-action="T8ProdInfo.findT8ProdInfos" data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="T8ProdInfo.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="T8ProdInfo.prodStatus"  data-dict="t8_prod_status"></k-field-select>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="T8ProdInfo.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="prodMeetingGrid" data-action='T8ProdAllMeetingConfirm.findT8ProdInfoMeetingConfirms1' @data-row-select="selectRow"
      data-operate-column-position="end" data-operate-width="200px">
      <k-grid-column data-header="产品id" data-name="id" :data-hidden="true"/>
      <k-grid-column data-header="产品代码" data-name="prodCode" />
      <k-grid-column data-header="产品名称" data-name="prodName" />
      <k-grid-column data-header="产品形态" data-name="prodMode" data-dict="t8_prod_create_type" />
      <k-grid-column data-header="募集方式" data-name="raiseType" data-dict="t8_raise_type" />
      <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status" />
      <k-grid-column data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status" />

      <template slot="operate" slot-scope="props">
        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple"
               :data-model="props.row.row" @click="toEditProdInfo(props.row.row)" v-if="global.getProdIfUser(props.row.row.id)"
               :prodInfoId="props.row.row.id" data-descript="产品信息详情">
          <md-icon>library_books</md-icon>
        </k-btn>
        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(props.row.row.id)&&
               global.isShowAuthorityButton('T8ProdAllMeetingConfirm.informationConfirmation')"
              :data-model="props.row.row" @click="popupEdit(props.row.row)" :prodInfoId="props.row.row.id" data-descript="产品信息会后确认" v-show="showConfirm">
            <md-icon>done</md-icon>
        </k-btn>
      </template>
    </k-grid>

  </div>
</template>

<script>
  import { assign } from "lodash";

  export default {
    name: "ProdMeetingList",
    data() {
      return {
        formData: {},
        T8ProdInfo: {},
        cascaderValue: [],
        selectRowData: {},
        showConfirm: true,//是否显示产品参数确认按钮
      };
    },
    created() {
      this.global.getProdUser('');
      this.$nextTick(() => {
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
        let prodCode = this.$route.query.prod_code;
        if (prodCode != '' && prodCode != undefined) {
          this.$refs.prodMeetingGrid.load({prodCode: prodCode});
        }
      });
    },
    methods: {
      toEditProdInfo(row) {
        let pathUrl = '/main/pms/M81/prodDisplay/M81001display';
        this.$router.push({
          path: pathUrl,
          query: {
            prodMode: row.prodMode,
            prodInfoId: row.id,
            prodCode: row.prodCode,
            assemblyMenuType: '1',
            menuName: 'ProdMeetingList'
          },
        });

      },


      popupEdit(row){
        let pathUrl = '/main/pms/M81/prodInfoGD/M81001add';
        this.$router.push({
          path: pathUrl,
          query: {prodMode: row.prodMode,prodModeId: row.prodModeId,findProdCode:row.prodCode, findProdName:row.prodCode,
            findProdMode:row.prodMode, prodInfoId:row.id,prodCode: row.prodCode,assemblyMenuType:'1',assembly_id:'prodInfo',
            menuName:'ProdMeetingList'},
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
