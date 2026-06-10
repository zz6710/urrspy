<template>
    <div>
      <k-form ref="prodGroupUserForm1" :data-col="3" data-input-width="180px" data-label-width="100px" data-total-width="500px">
        <k-form-item label="用户组名" >
          <k-field-select v-model="prodGroupUserFormData.groupId" data-action="T8GroupInfo.groupInfoDict"
                          @data-on-change="onGroupChange"
                          data-display-field="groupName"  data-value-field="id" />
        </k-form-item>
      </k-form>
      <k-form ref="prodGroupUserForm2" v-for="(item, index) in prodGroupUserItems" :key="index" :data-col="6" data-input-width="120px"
              data-label-width="90px" data-total-width="900px" >
        <k-form-item label="角色" >
          <k-field-select v-model="item.roleId" :data-graphql='queryRoleGraphql'
                          data-display-field="rolename" @data-on-change="onRoleChange(item,index)" data-value-field="roleid"/>
        </k-form-item>
        <k-form-item label="A角用户" :key="item.roleId+item.index">
          <k-field-select v-model="item.useridA" ref="useridA" :data-params="{roleId:item.roleId}" data-action="User.getUserByRoleId2"
                          @data-on-change="useridAChange(item,index)"  data-display-field="username"
                          data-value-field="userid" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="B角用户">
          <k-field-select v-model="item.useridB" ref="useridB" :data-params="{roleId:item.roleId,userid:item.useridA}"
                          data-action="User.getUserByRoleId2" data-display-field="username"
                          data-value-field="userid"/>
        </k-form-item>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增" @click="() => prodGroupUserItems.push({})">
          <md-icon>add</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行" @click="deleteEvent(index)">
          <md-icon md-src="/static/svg/delete.svg" />
        </k-btn>
      </k-form>
      <div style="margin: 0 auto; width: 255px">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="submitHandle"
               data-url="/server/form/PmsApp/t8prodUser/save.json" data-target="prodInfoGrid"
               :data-model="prodGroupUserFormData">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>

    </div>
</template>

<script>
    import Tools from "@/utils/tools";

    export default {
      name: "M8ProdGroupUser",
      props: {
        t8ProdInfoId:'',
      },

      data() {
        return {
          showGroupName:false,
          prodGroupUserFormData : {groupId:''},
          prodGroupUserItems:[],
          queryRoleGraphql: "{queryRole(action:\"find\",roleids:\"0\") {rows{roleid, rolename, parentroleid, descript},results}}",
        }
      },
      created() {
        this.prodGroupUserFormData.t8ProdInfoId=this.t8ProdInfoId;
        this.prodGroupUserFormData.groupId='';
        this.httpUtil.comnQuery({
          action: 'T8ProdUser.getProdUserByProdId',
          params: {
            t8ProdInfoId : this.t8ProdInfoId,
          }
        }).then(data => {
          if(data != null && data.rows.length >0){
            if (data.rows.length>0){
              this.showGroupName = false;
            }else {
              this.showGroupName = true;
            }
          }
        });
        this.httpUtil.comnQuery({
          action: 'T8ProdUser.countProdUserByProdId',
          params: {
            t8ProdInfoId : this.t8ProdInfoId,
          }
        }).then(data => {
          //console.log("data2=:>>>>>>>>>",data);
          if(data != null && data.rows.length >0){
            if (data.rows[0].cont>0){
              this.showGroupName = false;
              this.httpUtil.comnQuery({
                action: 'T8ProdUser.getProdUserByProdId',
                params: {
                  t8ProdInfoId : this.t8ProdInfoId,
                }
              }).then(data => {
                console.log(data)
                if(data.rows.length>1){
                  console.log(data.rows.length)
                  for (let i =0;i<data.rows.length;i++){
                    this.prodGroupUserItems.push(data.rows[i]);

                      }
                } else {
                  console.log(data.rows.length)
                  this.prodGroupUserItems = [{roleId:'3'},{roleId:'14'},{roleId:'4'},{roleId:'10'},{roleId:'11'},{roleId:'8'}];
                    for (let i =0;i<data.rows.length;i++){
                      if(data.rows[i].roleId == '3'){
                        this.$set(this.prodGroupUserItems[i],'useridA',data.rows[i].useridA);
                      }
                    }

                }
                // for(let j = 0;j<this.prodGroupUserItems.length;j++){
                //   for (let i =0;i<data.rows.length;i++){
                //     if(data.rows[i].roleId == this.prodGroupUserItems[j].roleId){
                //       this.$set(this.prodGroupUserItems[j],'useridA',data.rows[i].useridA);
                //     }
                //   }
                // }
              });
            }else {
              this.showGroupName = true;
            }
          } else {
            this.prodGroupUserItems = [{roleId:'3'},{roleId:'14'},{roleId:'4'},{roleId:'10'},{roleId:'11'},{roleId:'8'}];
          }
        });

      },
      methods: {
        submitHandle(value) {
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
          if(this.prodGroupUserItems.length<1){
            Tools.alert("未获取到信息，请稍后 !","danger")
            return false;
          }
          if (this.prodGroupUserItems && this.prodGroupUserItems.length > 0) {
            value.json = JSON.stringify(this.prodGroupUserItems);
          }
          this.setProdUser();
        },

        setProdUser(){
          let userid = localStorage.getItem("userid");
          let T8ProdList = localStorage.getItem('T8ProdList');
          let t8ProdInfoId = this.prodGroupUserFormData.t8ProdInfoId;
          let bool = false;
          for(let i = 0 ; i < this.prodGroupUserItems.length ; i++){
            let row = this.prodGroupUserItems[i];
            if(row.useridA == userid || row.useridB == userid){
              bool = true;
              T8ProdList += t8ProdInfoId;
              T8ProdList += ',';
              localStorage.setItem("T8ProdList", T8ProdList);
            }
          }
          if(!bool){
            T8ProdList=T8ProdList.slice(0,T8ProdList.length-1);
            let T8ProdArr = T8ProdList.split(',');
            let T8ProdListNew = '';
            for(let i = 0 ; i < T8ProdArr.length ; i++){
              if(T8ProdArr[i] != t8ProdInfoId){
                T8ProdListNew += T8ProdArr[i];
                T8ProdListNew += ',';
              }
            }
            localStorage.setItem("T8ProdList", T8ProdListNew);
          }

        },

        useridAChange(value,index) {
          this.$set(value, "useridB", '');
          this.$refs.useridB[index].load({roleId: value.roleId, userid: value.useridA});
        },

        deleteEvent(index) {
          if (this.prodGroupUserItems.length > 1) {
            this.prodGroupUserItems.splice(index, 1);
          }
        },

        onRoleChange(value,index){
          this.$set(value,"useridA",'');
          this.$set(value,"useridB",'');
          this.$refs.useridA[index].load({roleId:value.roleId});
        },

        onGroupChange(value){
          this.httpUtil.comnQuery({
            action: 'T8GroupUser.findT8GroupUserByGroupId',
            params: {
              groupId : value,
            }
          }).then(data => {
            this.prodGroupUserItems = data.rows;
          });
        },
      }

    }
</script>

<style scoped>

</style>
