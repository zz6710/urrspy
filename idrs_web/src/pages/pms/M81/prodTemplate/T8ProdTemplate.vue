<template >
  <div class="my-container" style="display: inline-block;">
    <div class="my-card">
      <div class="add-icon">
        <md-button @click="selectProdTemplate('')" class=" md-fab md-just-icon add-btn">
          <i class="icon-plus"></i>
        </md-button>
      </div>
      <div class="add-text">
        <span class="md-content">添加产品模板</span>
      </div>
    </div>
    <div  class="my-card" v-for="prodModeinfo in prodModeinfos" :key="prodModeinfo.prodTemplateCode">

      <div>
        <div @click="deleteMode(prodModeinfo)"  style="float:right;">
          <md-avatar class="md-small infoRemove" >
            <md-icon>close</md-icon>
          </md-avatar>
        </div>
        <div @click="selectProdTemplate(prodModeinfo.prodTemplateCode)" style="float:right;">
          <md-avatar class="md-small infoModify" style="float:right" >
            <md-icon>edit</md-icon>
          </md-avatar>
        </div>
      </div>

      <div class="dis-icon">
        <md-avatar class="md-avatar-icon md-large md-info">
          <i class="dis-img"></i>
        </md-avatar>
      </div>
      <div class="dis-text" :title="prodModeinfo.prodTemplateName">
        <span class="md-content">{{prodModeinfo.prodTemplateName}}</span>
      </div>

<!--      <div>-->
<!--        <div style=" width:50%; display:inline" >-->
<!--          <md-button class="md-dense md-raised md-info md-round btn-info"-->
<!--                     @click="popupAddTaskPanel(prodModeinfo)" >详情</md-button>-->
<!--        </div>-->
<!--      </div>-->
    </div>

    <k-popup ref="addTaskPopup" data-title="配置产品组件" data-width="1156px" class="addTaskPopup">
      <addTaskComp  :modValue="msg" />
    </k-popup>
  </div>
</template>

<script scoped>
  import addTaskComp from "./templateAddTask";

  export default {
    components: {
      addTaskComp,
    },
    data() {
      return {
        params:{},
        prodModeinfos:[],
        prodModeinfo:{},
        value: [],
        msg : {}
      };
    },
    methods: {
//跳转要素页面
      selectProdTemplate(item){
        let pathUrl = '/main/pms/M81/prodTemplate/itemProdTemplate';
        this.$router.push({
          path: pathUrl,
          query: {prodTemplateCode:item},
        });

      },
//组件配置
      popupAddTaskPanel:function(info){
        this.$refs.addTaskPopup.popup();
        this.msg = info;
      },
//删除
      deleteMode: function(info){

        this.$confirm('确认要删除该产品模板?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.httpUtil.comnUpdate({
            action: 'T8ProdTemplate.deleteProdTemplateInfo',
            params: {prodTemplateCode:info.prodTemplateCode},
            successAlert: true,
          }).then(data => {
            this.reloadGroupData();
          });
        });
      },


      //查询
      reloadGroupData(){
        this.httpUtil.comnQuery({
          action: 'T8ProdTemplate.findProdTemplateInfo',
          params: {},
        }).then(data => {
          this.prodModeinfos = data.rows;

        });
      }

    },
    created(){
      this.reloadGroupData();
    }
  };
</script>

<style lang="scss" scoped>

  @import "../../../../styles/self-card.scss";

  .add-text{
    padding-bottom: 50px;
  }
  .add-icon{
    padding-top: 70px;
    padding-bottom: 20px;
  }

  .dis-text{
    padding-bottom: 5px;
    padding-top: 10px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    width: 150px;
    margin: 0 auto;
  }
  .dis-icon{
    padding-top: 30px;
    padding-bottom: 5px;
  }

  .infoRemove{
    cursor: pointer;
    color: #909399;
  }

  .infoModify{
    cursor: pointer;
    color: #909399;
  }

  .md-icon:hover {
    color: #409EFF;
  }

  .md-icon:hover {
    color: #409EFF;
  }

  .addTaskPopup .el-dialog__body {
    padding: 8px 0 0 0;
  }

  .dis-img, .icon-plus {
    background-repeat: no-repeat;
    background-position: center;
    width: 50px;
    height: 50px;

  }

  .dis-img{
    background-image: url('/static/svg/product.svg');
  }

  .icon-plus{
    background-image: url('/static/svg/clear/plus.svg');
  }

  ::v-deep .md-just-icon .md-ripple {
    margin: 0 auto;
  }
</style>
