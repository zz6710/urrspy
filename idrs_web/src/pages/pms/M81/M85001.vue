<template >
  <div class="my-container" style="display: inline-block;">
    <div class="my-card">
      <div class="add-icon">
        <md-button @click="()=>this.$refs.addPopup.popup()" class=" md-fab md-just-icon add-btn"
                   v-if="global.isShowAuthorityButton('T8ProdModeInfo.insertT8ProdModeInfo')">
          <i class="icon-plus"></i>
        </md-button>
      </div>
      <div class="add-text">
        <span class="md-content">添加产品模型</span>
      </div>
    </div>
    <div  class="my-card" v-for="prodModeinfo in prodModeinfos" :key="prodModeinfo.prodMode">
      <div>
        <div @click="deleteMode(prodModeinfo)"  style="float:right;"
             v-if="global.isShowAuthorityButton('T8ProdModeInfo.deleteT8ProdModeInfo')">
          <md-avatar class="md-small infoRemove" >
            <md-icon>close</md-icon>
          </md-avatar>
        </div>
        <div @click="popupEdit(prodModeinfo)" style="float:right;"
             v-if="global.isShowAuthorityButton('T8ProdModeInfo.updateT8ProdModeInfo')">
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
      <div class="dis-text" :title="prodModeinfo.prodModeName">
        <span class="md-content">{{prodModeinfo.prodModeName}}</span>
      </div>

      <div>
        <div style=" width:50%; display:inline" >
<!--          <md-button class="md-dense md-raised md-info md-round btn-info"-->
<!--                     @click="popupAddTaskPanel(prodModeinfo)" >模型配置</md-button>-->
          <md-button class="md-dense md-raised md-info md-round btn-info"
                     @click="popupAddTemplate(prodModeinfo)"
                     v-if="global.isShowAuthorityButton('T8ProdAssemblyList.addAssemblyProdModeList')">组件配置
          </md-button>
        </div>
      </div>
    </div>

    <k-popup ref="addPopup" data-title="新增">
      <AddComp taskType="1" @reload-data="reloadGroupData" style="width: 702px;"/>
    </k-popup>
    <k-popup ref="updatePopup" data-title="修改" :data-dialog-drag="true">
      <updComp :modValue="msg" @reload-data="reloadGroupData"/>
    </k-popup>
    <!--    <k-popup ref="addTaskPopup" data-title="配置产品模型" data-width="1156px" class="addTaskPopup">
          <addTaskComp  :modValue="msg" />
        </k-popup>-->
    <k-popup ref="addtemplatePopup" data-title="组件配置" data-width="1156px" class="addTaskPopup">
      <addtemplateComp :modValue="msg"/>
    </k-popup>
  </div>
</template>

<script scoped>
import kayak from '@/frame/kayak.js';
import AddComp from "./M85001AddMode";
import updComp from "./M85001UpdateMode";
import addTaskComp from "./M85001AddTask";
import addtemplateComp from "./prodModeAddAssembly";

export default {
  name:"M85001",
  components: {
    AddComp,updComp,addTaskComp,addtemplateComp,
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
    popupEdit:function(info){
      this.$refs.updatePopup.popup();
      this.msg = info;
    },

    popupAddTaskPanel:function(info){
      this.$refs.addTaskPopup.popup();
      this.msg = info;
    },

    popupAddTemplate:function(info){
      this.$refs.addtemplatePopup.popup();
      this.msg = info;
    },

    deleteMode: function(info){

      this.$confirm('确认要删除该产品模型?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.httpUtil.comnUpdate({
          action: 'T8ProdModeInfo.deleteT8ProdModeInfo',
          params: {prodMode:info.prodMode},
          successAlert: true,
        }).then(data => {
          this.reloadGroupData();
        });
      });
    },

    reloadGroupData(){
      this.httpUtil.comnQuery({
        action: 'T8ProdModeInfo.findT8ProdModeInfos',
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

@import "../../../styles/self-card.scss";

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
