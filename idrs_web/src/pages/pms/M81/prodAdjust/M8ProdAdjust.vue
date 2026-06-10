<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="queryParam">

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
<!--      <k-form-item label="代码回收">
        <k-field-select v-model="queryParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>-->
    </k-form-search-customize>


    <k-grid ref="prodInfoGrid" data-action="T8ProdAllInfoAdjust.findT8ProdInfoAdjust1" @data-row-select="selectRow" :data-page-size=10
            data-operate-column-position="end" data-align="center" data-operate-data-width="300px"
            data-operate-column="true" >
      <k-grid-column data-header="产品id" data-name="id" :data-hidden="true"/>
      <k-grid-column data-header="产品id" data-name="t8ProdInfoId" :data-hidden="true"/>
      <k-grid-column data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-header="产品形态" data-name="prodMode" data-dict="t8_prod_create_type"/>
      <k-grid-column data-header="产品分类" data-name="prodClassify" data-dict="t8_prod_classify"/>
      <k-grid-column data-header="成立日" data-name="establishDate"  data-type="date"/>
      <k-grid-column data-header="到期日" data-name="endDate"  data-type="date"/>
      <k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status" />
      <k-grid-column data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status" />

      <template slot="operate" slot-scope="props">

        <k-btn class="md-info md-just-icon md-simple" data-descript="调整产品信息" data-functype="POPUP" data-size="mini"
               data-target="editProdInfo" v-if="global.getProdIfUser(props.row.row.id)&&
               global.isShowAuthorityButton('T8ProdAllInfoAdjust.addT8ProdInfoAdjust')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple"
               :data-model="props.row.row" @click="toEditProdInfo(props.row.row)" v-if="global.getProdIfUser(props.row.row.id)"
               :prodInfoId="props.row.row.id" data-descript="产品信息详情">
          <md-icon>library_books</md-icon>
        </k-btn>

      </template>
    </k-grid>

    <k-grid ref="T8ProdInfoLibrary" :data-operate-="false" :data-autoload="false"
            data-action="ProdStatusChange.findProdStatusChange" :data-operate-column="false">
      <k-grid-column data-align="center" data-header="调整组件" data-name="assemblyDesc"/>
      <k-grid-column data-align="center" data-header="调整日期" data-name="crtDate" data-type="date"/>
      <k-grid-column data-align="center" data-header="调整人" data-name="crtUser"/>
      <k-grid-column data-align="center" data-header="调整原因" data-name="adjustCause"/>
      <!--      <template slot="operate" slot-scope="props">-->
      <!--        <k-btn data-functype="POPUP" data-target="libraryBon" class="md-warning md-just-icon md-simple" data-descript="查看"-->
      <!--               v-if="global.getProdIfUser(props.row.row.t8ProdInfoId)" @click="getDataParams(props.row.row)" >-->
      <!--          <md-icon md-src="/static/svg/log.svg" />-->
      <!--        </k-btn>-->
      <!--      </template>-->
    </k-grid>



    <k-popup ref="editProdInfo" data-title="产品信息调整">
      <k-form ref="editProdForm" :data-col="2">
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode"  :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName"  :data-disabled="true" />
        </k-form-item>

<!--        <k-form-item label="调整组件" v-show="formData.prodStatus >= 6">
          <k-field-select v-model="formData.assemblyId" :data-allowblank="false"
                          data-action="T8ProdAssembly.findProdAdjustRuler" :data-params="{prodCode:formData.prodCode}"
                          data-display-field="assemblyDesc" data-value-field="assemblyId"/>
        </k-form-item>-->
        <!-- 组件调整逻辑调整:
             修改前:产品状态>=6 可用调整部分组件  产品状态<6 可以调整所有组件
             修改后:去除产品状态限制,可以调整所有组件信息 -->
        <!--    v-show="formData.prodStatus < 6 "   -->
        <k-form-item label="调整组件">
          <k-field-select v-model="formData.assemblyId" :data-allowblank="false"
                          data-action="T8ProdAssembly.findProdAdjustFront" :data-params="{prodCode:formData.prodCode}"
                          data-display-field="assembly_desc" data-value-field="assembly_id"/>
        </k-form-item>
        <k-form-item label="调整原因" data-input-width="590px">
          <k-field-text v-model="formData.adjustCause " :data-allowblank="false" :data-max-length="200" inputType="textarea" :rows="1"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn data-functype="SUBMIT" data-size="mini" class="btn-custom-primary" data-from="editProdForm"
                 :data-model="formData"  data-descript="调整产品信息" :data-handler = "findDataBye">
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

    export default {
      data() {
        return {
          queryParam:{},
          formData: {},
          selectRowData: {},
          prodCode: "",
          prodMode: "",

        };
      },
      created() {
        //获取页面跳转参数
        let prodCode=this.$route.query.prodCode;
        if(prodCode!=''&&prodCode!=undefined){
          this.$nextTick(() => {
            this.$refs.prodInfoGrid.load({prodCode:prodCode})
          });

        }
        this.global.getProdUser('');
      },
      methods: {
        getDataParams(){
         /* this.httpUtil.comnQuery({
            action: 'ProdStatusChange.findDataParams',
            params: {

            }
          }).then(data => {

          });*/

        },
        toEditProdInfo(row){
          let pathUrl = '/main/pms/M81/prodDisplay/M81001display';
          this.$router.push({
            path: pathUrl,
            query: {prodMode: row.prodMode,prodInfoId:row.id,prodCode: row.prodCode,assemblyMenuType:'1',menuName:'M8ProdAdjust'},
          });

        },
        findDataBye(){
          if(this.$refs.editProdForm.validate()){
           //this.$set(this.formData.showPanels, 'shareSort','show');
            this.$router.push({
              path: '/main/pms/M81/prodAdjust/M81001ProdAdjust',
              query: this.formData,
            });
            this.$refs.editProdInfo.close();
          }
        },
        selectRow(row, column, event) {
          this.selectRowData = assign({}, row);
          this.formData = assign({}, row);
          this.$refs.T8ProdInfoLibrary.load({t8ProdInfoId: row.id});
        },
      }
    }
</script>

<style scoped>

</style>
