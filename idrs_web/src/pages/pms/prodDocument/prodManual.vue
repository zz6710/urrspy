<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="prodSearchParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="prodSearchParam.prodStatus" data-dict="t8_prod_status"/>
      </k-form-item>
      <k-form-item label="是否存在说明书">
        <k-field-select v-model="prodSearchParam.isHave" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="prodInfoGrid" data-action="T8ProdManualVersion.findProdManualListByProdCodeOrProdNames1">
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="产品成立日" data-type="date" data-name="establishDate"/>
      <k-grid-column data-align="center" data-header="产品到期日" data-type="date" data-name="endDate"/>
      <k-grid-column data-align="center" data-header="文档类型" data-dict="t8_temp_type" data-name="documentType"/>
      <k-grid-column data-align="center" data-header="产品状态" data-dict="t8_prod_status" data-name="prodStatus"/>
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple" :data-model="scope.row.row.id"
               @click="popupEdit(scope.row.row)"  data-descript="产品说明书版本管理">
          <md-icon>weekend</md-icon>
        </k-btn>
      </template>
    </k-grid>
  </div>
</template>

<script>
  import {assign} from "lodash";
  import Tools from "@/utils/tools";
  export default {
    name: "prodManual",
    mounted() {
      window.addEventListener('message', (e)=>{
        if(e.data.key){
          let refName=e.data.key
          this.$refs[refName][0].focus()
        }
      })
    },
    data() {
      return {
        prodSearchParam:{
          prodCode:''
        },
        formData:{
          prodCode:'',
          prodName:'',
          documentType:'',
          version:'',
          remark:'',
          docType:''
        },
        filFormData:{
          prodCode:'',
          prodName:'',
          documentType:'',
          version:'',
          isTemplateFile:''
        },
        lastVersion:'',
        TopNewVersion: '',
        addPopupTitle: '',
        onlineEditData: {},
        viewUrl: '',
        showGenerate: true,//是否显示生成按钮
        showEditOnline: true,//是否显示在线编辑按钮
        showConfirm: true,//是否显示确认按钮
        showUploadLaw: true,//是否显示上传法审版按钮
        showFinalize: true,//是否显示定稿按钮
        onlineParams: {},//走审批用
        showUploadManual: true,//是否显示上传说明书按钮
      }
    },
    created() {
      this.global.getProdUser('');
      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
        let prodCode = this.$route.query.prod_code;
        if(prodCode !=''&&prodCode!=undefined){
          this.$refs.prodInfoGrid.load({prodCode:prodCode});
        }
      });
    },
    methods: {
      popupEdit(row){
        localStorage.setItem("prodManualData", JSON.stringify(row));
        let pathUrl = '/main/pms/prodDocument/prodManualVersion';
        this.$router.push({
          path: pathUrl,
          query: {lastVersion: this.lastVersion,prodManualData: row},
        });
      },
    }
  }
</script>


<style lang="scss" scoped>
  .edit{
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 600px;
    .word{
      width: 70%;
      iframe{
        width: 100%;
        height: 100%;
      }
    }
    .form{
      padding-left: 20px;
      width: 40%;
      overflow-y:auto;
      .form-item{
        display: flex;
        align-items: center;
        margin-bottom: 10px;
        .form-item-span{
          margin-right: 5px;
          width: 100px;
          text-align: left;
        }
        .k-field-text{
          margin-left: 5px;
          width: 300px;
          height: 30px;
        }
      }
    }
  }

/*  .onLineClass ::v-deep .el-dialog {
    position: fixed !important;  // 浮动
    z-index: 999999 !important;
  }*/

</style>
