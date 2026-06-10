<template>
  <div>

    <k-form ref="prodGroupUserForm1" :data-col="3" data-input-width="160px" data-label-width="80px" data-total-width="1188px">
      <k-form-item label="业务操作" >
        <k-field-select v-model="formDat.server"  data-action="WfBusinessConfig.findServerMethodTree" data-display-field="name"
                        data-value-field="id" :data-allowblank="false" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="审批任务名称" data-label-width="180px" >
        <k-field-text v-model="formDat.displayName"   data-display-field="name" :data-allowblank="false" :data-disabled="true"
                      data-value-field="id" />
      </k-form-item>

      <k-form-item label="是否在移动端显示" data-label-width="180px">
        <k-field-select v-model="formDat.appDisplay" data-dict="1yes0no" :data-allowblank="false"  :data-disabled="true" />
      </k-form-item>

      <k-form-item label="是否允许审批中增加节点" data-label-width="180px">
        <k-field-select v-model="formDat.addNode" data-dict="1yes0no" :data-allowblank="false"  :data-disabled="true" />
      </k-form-item>
      <k-form-item label="是否绑定产品" data-label-width="180px">
        <k-field-select v-model="formDat.bindProd" data-dict="1yes0no" :data-allowblank="false"  :data-disabled="true" />
      </k-form-item>
    </k-form>
    <div style="border:solid 1px" ></div><br>


    <k-form ref="prodGroupUserForm2" v-for="(item,index) in envItems" :key="index"  :data-col="2" data-input-width="90px"
            data-label-width="90px" data-total-width="1118px" >
      <k-form-item label="节点名称" >
        <k-field-text v-model="item.nodeName" :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="审批类型" >
        <k-field-select v-model="item.nodeType"  data-dict="approval_type"  :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="审批角色" v-if="item.roleId">
        <k-field-select v-model="item.roleId "  data-display-field="rolename"  :data-multiple="true" :data-graphql='queryRoleGraphql'
                        data-value-field="roleid" :data-allowblank="true" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="审批等级" :hidden="true">
        <k-field-text v-model="item.nodeLevel  = index+1 " />
      </k-form-item>
      <k-form-item label="审批人员" v-if="!item.roleId">
        <k-field-select v-model="item.actorId" data-action="User.getUserByRoleId2" data-display-field="username" :data-params="{roleId:'3'}" :data-multiple="true" :data-disabled="true"
                        data-value-field="userid" />
      </k-form-item>
      <k-form-item label="是否部门领导审批" data-label-width="80px" >
        <k-field-select v-model="item.leaderApproval" data-dict="1yes0no" :data-disabled="true"/>
      </k-form-item>
    </k-form>

    <div style="border:solid 1px" ></div><br>

    <k-form ref="prodGroupUserForm3" :data-col="5" data-input-width="120px"
            data-label-width="90px" data-total-width="1118px" >
      <k-form-item label="节点名称" >
        <k-field-text v-model="formDat.nodeName = '抄送'" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="审批类型" >
        <k-field-text v-model="formDat.nodeType = '抄送'"  :data-disabled="true"  />
      </k-form-item>
      <k-form-item label="抄送人员">
        <k-field-select v-model="formDat.actorId" data-action="User.getUserByRoleId2" data-display-field="username" :data-params="{roleId:'3'}" :data-multiple="true" :data-disabled="true"
                        data-value-field="userid"/>
      </k-form-item>
    </k-form>

  </div>
</template>

    <script>
    import Tools from "@/utils/tools";

    export default {
      name: "DisplayHistoricalVersion",
      props: {
        formData:{},
      },

      data() {
        return {
          userList:{},
          showGroupName:false,
          formDat:{},
          nodeList:[],
          prodGroupUserItems:[],
          envItems: [{"ratioIndex": ''}, {"coefficient": ''}],
          num: 1,
          queryRoleGraphql: "{queryRole(action:\"find\",roleids:\"0\") {rows{roleid, rolename, parentroleid, descript},results}}",
        }
      },
      created() {
        console.log('this.formData',this.formData)
        this.formDat = this.formData.hisTemplate;
        this.envItems = this.formData.nodeList;
      },
      methods: {


        passDataSuccess(){
          this.$emit('submitClose', '1')
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


      }
    }
</script>

<style scoped>

</style>
