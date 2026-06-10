<template>
  <div>

    <k-form ref="prodGroupUserForm1" :data-col="3" data-input-width="220px" data-label-width="180px" data-total-width="988px">
      <k-form-item label="业务操作" >
        <k-field-select v-model="fromData.server"  data-action="WfBusinessConfig.findServerMethodTree" data-display-field="name"
                        data-value-field="id" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="审批任务名称" >
        <k-field-text v-model="fromData.displayName"  :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="是否在移动端显示" >
        <k-field-select v-model="fromData.appDisplay" :data-default-value="this.appDisplayParam" data-dict="1yes0no"   />
      </k-form-item>
      <k-form-item label="是否允许审批中增加节点" data-label-width="180px">
        <k-field-select v-model="fromData.addNode" :data-default-value="this.addNodeList"  data-dict="1yes0no" :data-allowblank="false" />
      </k-form-item>
    </k-form>
    <div style="border: 1px dashed" ></div>


    <k-form ref="prodGroupUserForm2" v-for="(item,index) in envItems" :key="index"  :data-col="2" data-input-width="90px"
            data-label-width="90px" data-total-width="1118px" >
      <k-form-item label="节点名称" >
        <k-field-text v-model="item.nodeName" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="审批类型" >
        <k-field-select v-model="item.nodeType"  data-dict="approval_type"  :data-allowblank="false" />
      </k-form-item>
      <k-form-item label="审批角色" v-if="!item.actorId">
        <k-field-select v-model="item.roleId "  data-display-field="rolename"  :data-multiple="true" :data-graphql='queryRoleGraphql'
                        data-value-field="roleid" :data-allowblank="true"/>
      </k-form-item>
      <k-form-item label="审批等级" :hidden="true">
        <k-field-text v-model="item.highestLevel = index+1 " />
      </k-form-item>
      <k-form-item label="审批人员" v-if="!item.roleId">
        <k-field-select v-model="item.actorId" data-action="User.getUserByRoleId2" data-display-field="username" :data-params="{roleId:'3'}" :data-multiple="true"
                        data-value-field="userid" />
      </k-form-item>
      <k-form-item label="是否部门领导审批" data-label-width="80px" >
        <k-field-select v-model="item.leaderApproval" data-dict="1yes0no"/>
      </k-form-item>
      <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增" @click="()=>envItems.push({})" >
        <md-icon>add</md-icon>
      </k-btn>
      <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行" @click="deleteEvent(index)">
        <md-icon md-src="/static/svg/delete.svg" />
      </k-btn>
    </k-form>

    <div style="border: 1px dashed" ></div>

    <k-form ref="prodGroupUserForm3" :data-col="5" data-input-width="120px"
            data-label-width="90px" data-total-width="1118px" >
      <k-form-item label="节点名称" >
        <k-field-text v-model="fromData.nodeName = '抄送'" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="审批类型" >
        <k-field-text v-model="fromData.nodeType = '抄送'"  :data-disabled="true"  />
      </k-form-item>
      <k-form-item label="抄送人员">
        <k-field-select v-model="fromData.readonlyActor" data-action="User.getUserByRoleId2" data-display-field="username" :data-params="{roleId:'3'}" :data-multiple="true"
                        data-value-field="userid"/>
      </k-form-item>
    </k-form>

    <div style="margin: 0 auto; width: 255px">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT"  :data-handler="submitHandle"
             data-target="prodInfoGrid" :data-model="fromData">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
    </div>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name: "flowProcessConfigurationUpdate",
    props: {
      fromData:{},
    },

    data() {
      return {
        showGroupName:false,
        nodeList:[],
        userList:{},
        prodGroupUserItems:[],
        envItems: [{"ratioIndex": ''}, {"coefficient": ''}],
        num: 1,
        queryRoleGraphql: "{queryRole(action:\"find\",roleids:\"0\") {rows{roleid, rolename, parentroleid, descript},results}}",
        highestLevel:{},
        processId:{},
        addNodeList:{},
        appDisplayParam:{},
        formData1:[{key:1,value:'是'},{key:0,value:'否'}],
      }
    },
    created() {
      // this.envItems.length = 1;
      // this.queryformByProcessId();
    },
    methods: {

      // queryformByProcessId(value){
      //   console.log('获取参数',value)
      //   if (value){
      //     this.httpUtil.ajax({
      //       url:"/wf/conf/getActorByRoles.json" ,
      //       params: {roleIds:value},
      //     }).then(data => {
      //       console.log('执行参数',data.data)
      //       this.userList = data.data
      //
      //     });
      //   }else {
      //     this.httpUtil.comnQuery({
      //       action: 'User.getUserByRoleId2',
      //       params: {roleId:'3'}
      //     }).then(data => {
      //       console.log('查询审批人员',data)
      //       this.userList = data.rows
      //
      //     });
      //   }
      //
      //
      // },

      submitHandle(value) {
        // console.log('获取参数',value)

        let result = true;

        result = this.$refs.prodGroupUserForm1.validate();
        let form2s = this.$refs.prodGroupUserForm2;
        if (form2s && form2s.length > 0) {
          for (let i = 0; i < form2s.length; i++) {
            result = result && form2s[i].validate();
          }
        }
        if (result === false) {
          return false;
        }
        if(this.envItems.length<1){
          Tools.alert("未获取到信息，请稍后 !","danger")
          return false;
        }
        if (this.envItems && this.envItems.length > 0) {
          // console.log('获取参数11111',this.envItems)
          value.nodeList = JSON.stringify(this.envItems);
          // console.log("传递参数",value.nodeList)
        }
        let len = this.envItems.length
        this.httpUtil.ajax({
          url:"/wf/businessProcess/updateTemplateProcess.json" ,
          params: {server:value.server , nodeList: value.nodeList , appDisplay:value.appDisplay , actorId:value.actorId ,highestLevel: len , actorId:value.readonlyActor ,
            processId : this.processId ,displayName : value.displayName , addNode : value.addNode},
        }).then(data => {
          // console.log('执行参数',data)
          this.reloadGroupData();
        });
        this.passDataSuccess()
      },

      passDataSuccess(){
        this.$emit('submitClose', '1')
      },


      deleteEvent(index) {
        if (this.envItems.length > 1) {
          this.envItems.splice(index, 1);
        }
      },

      reloadGroupData(){
        this.httpUtil.ajax({
          url: 'wf/businessProcess/getAllProcess.json',
          params: {},
        }).then(data => {
          // console.log('参数类型',data)
          this.flowTemplateList=data.data
        });
      },
    },
    watch:{
      fromData : function(value) {
        console.log('监听参数',value)
        this.fromData.nodeName = '抄送'
        this.fromData.nodeType = '抄送'
        this.fromData.readonlyActor = value.readonlyActor
        this.appDisplayParam = value.busnessProcess.appDisplay
        console.log('appDisplayParam',this.appDisplayParam)
        this.processId = value.nodeList[0].processId
        this.addNodeList = value.busnessProcess.addNode
        console.log('addNodeList',this.addNodeList)
        this.envItems = value.nodeList;
        this.highestLevel = value.busnessProcess.highestLevel
        this.fromData.displayName = value.busnessProcess.displayName;
        this.fromData.server = value.busnessProcess.server

      }

    },
  }
</script>

<style scoped>

</style>
