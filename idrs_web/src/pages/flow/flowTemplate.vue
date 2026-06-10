<template >
  <div class="my-container" style="display: inline-block;">
    <div class="my-card">
      <div class="add-icon">
        <md-button @click="()=>this.$refs.addPopup.popup()" class=" md-fab md-just-icon add-btn">
          <i class="icon-plus"></i>
        </md-button>
      </div>
      <div class="add-text">
        <span class="md-content">添加</span>
      </div>
    </div>
    <div  class="my-card" v-for="item in flowTemplateList" :key="item.id">
      <div>
        <div @click="deleteMode(item)"  style="float:right;">
          <md-avatar class="md-small infoRemove" >
            <md-icon>close</md-icon>
          </md-avatar>
        </div>
        <div @click="popupCopy(item)" style="float:right;">
          <md-avatar class="md-small infoModify" style="float:right" >
            <i class="el-icon-copy-document"></i>
          </md-avatar>
        </div>
        <div @click="popupEdit(item)" style="float:right;">
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
      <div class="dis-text" :title="item.name">
        <span class="md-content">{{item.name}}</span>
      </div>

      <div>
        <div style=" width:50%; display:inline" >
          <md-button class="md-dense md-raised md-info md-round btn-info"
                     @click="popupAddTaskPanel(item)" >配置</md-button>
        </div>
      </div>
    </div>

    <k-popup ref="addPopup" data-title="新增">
      <k-form ref="addUserForm" :data-col="1">
        <k-form-item label="模板名称">
          <k-field-text v-model="formData.name"  :data-allowblank="false" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$"
                        data-regx-text="请输入字母、下划线与数字" />
        </k-form-item>
        <k-form-item label="产品阶段">
          <k-field-select v-model="formData.phase" data-multiple="true" data-dict="flow_template_phase"></k-field-select>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" :data-model="formData" data-functype="SUBMIT" :data-handler="addFlowTemplate" data-from="addUserForm">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
   <k-popup ref="updatePopup" data-title="修改">
     <k-form ref="updateUserForm" :data-col="1">
       <k-form-item label="模板名称">
         <k-field-text v-model="updateFormData.name"  :data-allowblank="false" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$"
                       data-regx-text="请输入字母、下划线与数字" />
       </k-form-item>
       <k-form-item label="产品阶段">
         <k-field-select v-model="updateFormData.phase" data-multiple="true" data-dict="flow_template_phase"></k-field-select>
       </k-form-item>

       <k-form-footer data-align="center">
         <k-btn class="btn-custom-primary" :data-model="updateFormData" data-functype="SUBMIT" :data-handler="updateFlowTemplate" data-from="updateUserForm">
           <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
         </k-btn>
         <k-btn class="btn-custom-plain" data-functype="CLOSE">
           <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
       </k-form-footer>
     </k-form>
   </k-popup>

   <k-popup ref="copyPopup" data-title="复制">
     <k-form ref="copyUserForm" :data-col="1">
       <k-form-item label="模板名称">
         <k-field-text v-model="copyFormData.name"  :data-allowblank="false" :data-max-length="32" data-regx="^[A-Za-z_0-9]*$"
                       data-regx-text="请输入字母、下划线与数字" />
       </k-form-item>
       <k-form-footer data-align="center">
         <k-btn class="btn-custom-primary" :data-model="copyFormData" data-functype="SUBMIT" :data-handler="copyFlowTemplate" data-from="copyUserForm">
           <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
         </k-btn>
         <k-btn class="btn-custom-plain" data-functype="CLOSE">
           <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
       </k-form-footer>
     </k-form>
   </k-popup>
<!--    <k-popup ref="addTaskPopup" data-title="配置产品模型" data-width="1156px" class="addTaskPopup">-->
<!--      <addTaskComp  :modValue="msg" />-->
<!--    </k-popup>-->
  </div>
</template>

<script scoped>
  import kayak from '@/frame/kayak.js';
  // import AddComp from "./TA5001AddMode";
  // import updComp from "./TA5001UpdateMode";
  // import addTaskComp from "./TA5001AddTask";

  export default {
    components: {
      // AddComp,updComp,addTaskComp,
    },
    data() {
      return {
        formData:{},
        params:{},
        prodModeinfos:[],
        prodModeinfo:{},
        flowTemplateList:[],
        value: [],
        updateFormData : {
          id:"",
          name:"",
          phase:""
        },
        copyFormData : {
          id:"",
          name:"",
        }
      };
    },
    methods: {
      popupEdit:function(info){
        this.$refs.updatePopup.popup();
        this.updateFormData.id = info.id;
        this.updateFormData.name = info.name;
        this.updateFormData.phase = info.phase;
      },
      popupCopy:function(info){
        this.$refs.copyPopup.popup();
        this.copyFormData.id = info.id;
        this.copyFormData.name = "";
      },
      popupAddTaskPanel(template){
           this.$router.push({
             path:"/main/flow/flowTemplateItem",
             query:{
               id:template.id
             }
           })
      },

      addFlowTemplate(data){
        let result = true;
        let templateForm = this.$refs.addUserForm;
        result = templateForm.validate();
        if (result === false) {
          return false;
        }
        this.httpUtil.ajax({
          url: 'wf/flowTemplate/save.json',
          params: data,
          successAlert: true,
        }).then(data => {
          this.$refs.addPopup.close();
          this.reloadGroupData();
        });
      },

      updateFlowTemplate(data) {
        let result = true;
        let templateForm = this.$refs.updateUserForm;
        result = templateForm.validate();
        if (result === false) {
          return false;
        }
        this.httpUtil.ajax({
          url: 'wf/flowTemplate/update.json',
          params: data,
          successAlert: true,
        }).then(data => {
          this.$refs.updatePopup.close();
          this.reloadGroupData();
        });
      },
      copyFlowTemplate(data) {
        let result = true;
        let templateForm = this.$refs.copyUserForm;
        result = templateForm.validate();
        if (result === false) {
          return false;
        }
        this.httpUtil.ajax({
          url: 'wf/flowTemplate/copy.json',
          params: data,
          successAlert: true,
        }).then(data => {
          this.$refs.copyPopup.close();
          this.reloadGroupData();
        });
      },

      deleteMode: function(info){
        this.$confirm('确认要删除该产品模型?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.httpUtil.ajax({
            url: 'wf/flowTemplate/delete.json',
            params: {id:info.id},
            successAlert: true,
          }).then(data => {
            this.reloadGroupData();
          });
        });
      },

      reloadGroupData(){
        this.httpUtil.ajax({
          url: 'wf/flowTemplate/find.json',
          params: {},
        }).then(data => {
            this.flowTemplateList=data.data
        });
      }

    },
    created(){
      this.reloadGroupData();
    }
  };
</script>

<style lang="scss" scoped>

  @import "@/styles/self-card.scss";

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

  /deep/ .md-just-icon .md-ripple {
    margin: 0 auto;
  }
</style>
