<template>
  <div>
    <k-form-search-customize data-target="groupUserInfoGrid" v-model="findFormData">
      <k-form-item label="用户组">
        <k-field-select v-model="findFormData.groupId"  data-action="T8GroupInfo.find"
                        data-display-field="groupName" data-value-field="id" ></k-field-select>
      </k-form-item>
      <k-form-item label="用户">
        <k-field-select v-model="findFormData.userId"  data-action="T8Dict.findAllUserInfos"
                        data-display-field="userName" data-value-field="userId" ></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="openBox" data-target="addPopup"
             v-if="global.isShowAuthorityButton('T8GroupUser.add')">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search-customize>
    <k-grid ref="groupUserInfoGrid" data-action='T8GroupUser.find1'
            data-operate-width="320px">
      <k-grid-column data-header="id" data-name="id" data-hidden="true"/>
      <k-grid-column data-header="用户组ID" data-hidden="true" data-name="groupId"/>
      <k-grid-column data-header="用户组名称" data-name="groupName"/>
      <k-grid-column data-header="A角用户id" data-hidden="true" data-name="useridA"/>
      <k-grid-column data-header="A角用户" data-name="userIdAName"/>
      <k-grid-column data-header="B角用户id" data-hidden="true" data-name="useridB"/>
      <k-grid-column data-header="B角用户" data-name="userIdBName"/>
      <k-grid-column data-header="所属A角" data-hidden="true" data-name="upperid"/>
      <k-grid-column data-header="A角状态" data-name="statuA" data-dict="userstatus"/>
<!--      <k-grid-column data-header="创建人" data-name="inputuser"/>-->
<!--      <k-grid-column data-header="创建日期" data-type="date" data-name="crtDate"/>-->
<!--      <k-grid-column data-header="创建时间" data-type="time" data-name="crtTime"/>-->
<!--      <k-grid-column data-header="更新日期" data-type="date" data-name="updDate"/>-->
<!--      <k-grid-column data-header="更新时间" data-type="time" data-name="updTime"/>-->
      <!--      <k-grid-column data-header="备注" data-name="remark"/>-->
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-handler="editHandler" data-descript="修改产品用户组人员信息"
               data-functype="POPUP" data-size="mini"
               data-target="addPopup"
               v-if="global.isShowAuthorityButton('T8GroupUser.upd')">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" :data-title="title" :data-dialog-drag="true">
      <k-form ref="addForm1" :data-col="3" data-input-width="220px" data-label-width="82px" data-total-width="988px">
        <k-form-item label="用户组名">
          <k-field-text v-model="formData.groupName" :data-max-length="100" :data-allowblank="false"/>
        </k-form-item>
      </k-form>
      <k-form
        ref="addForm2"
        v-for="(item, index) in envItems"
        :key="index"
        :data-col="6"
        data-input-width="120px"
        data-label-width="90px"
        data-total-width="1118px"
      >
        <k-form-item label="角色">
          <k-field-select v-model="item.roleId" :data-graphql='queryRoleGraphql'
                          data-display-field="rolename" @data-on-change="onRoleChange(item,index)" data-value-field="roleid"/>
        </k-form-item>
        <k-form-item label="A角用户id">
          <k-field-select v-model="item.useridA" ref="useridA" :data-params="{roleId:item.roleId}" data-action="User.getUserByRoleId2"  @data-on-change="useridAChange(item,index)"  data-display-field="username"
                          data-value-field="userid" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="B角用户id">
          <k-field-select v-model="item.useridB" ref="useridB" :data-params="{roleId:item.roleId,userid:item.useridA}" data-action="User.getUserByRoleId2" data-display-field="username"
                          data-value-field="userid"/>
        </k-form-item>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增" @click="() => envItems.push({})">
          <md-icon>add</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行" @click="deleteEvent(index)">
          <md-icon md-src="/static/svg/delete.svg" />
        </k-btn>
      </k-form>
      <div style="margin: 0 auto; width: 255px">
        <k-btn
          class="btn-custom-primary"
          data-functype="SUBMIT"
          :data-handler="submitHandle"
          data-url="/server/form/PmsApp/t8GroupInfo/save.json"
          data-target="groupUserInfoGrid"
          :data-model="formData"
        >
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>
    </k-popup>

  </div>
</template>

<script>
import kayak from '@/frame/kayak.js'
import {
  assign
} from "lodash";

export default {
  name:"M81012",
  data() {
    return {
      findFormData:{},
      formData: {
        groupName: ''
      },
      editFormData: {},
      selectRowData: {},
      selectPgmno: '',
      showListWorkDay: false,
      userGroupId: "",
      groupModeType: "",
      queryRoleGraphql: "{queryRole(action:\"find\",roleids:\"0\") {rows{roleid, rolename, parentroleid, descript},results}}",
      envItems: [],
      title: "",
    };
  },
  methods: {
    yy(row){
      console.log(row,"修改")
      this.editFormData=row
    },
    tt(row){
      this.userGroupId=row.id;
      this.groupModeType = row.groupMod;
    },
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.formData = assign({}, row)
    },
    selectGroupInfoRow(row, column, event){
      const _this = this;
      _this.selectRowData = assign({}, row);
      _this.formData = assign({}, row);
      this.$refs.groupUserInfoGrid.load({groupId: _this.selectRowData.id});
    },
    editHandler(value){
      this.title = "修改";
      this.formData.groupName = value.groupName;
      this.formData.groupId= value.groupId;
      this.httpUtil.comnQuery({
        action: 'T8GroupUser.findT8GroupUserByGroupId',
        params: {
          groupId : value.groupId,
        }
      }).then(data => {
        this.envItems = data.rows;
      });
    },
    deleteEvent(index) {
      if (this.envItems.length > 1) {
        this.envItems.splice(index, 1);
      }
    },
    refreshUserData(value) {
      value.upperid = value.useridA
      this.httpUtil.comnQuery({
        action: "User.getRevUser",
        params: {userid: this.formData.useridA}
      }).then(data => {
        this.userData = data.rows;
        this.$nextTick(() => {
          this.$set(this.formData, "username", "")
        })
      }).catch({})
    },
    openBox() {
      this.formData={};
      this.envItems = [{}];
      this.title = "添加";
    },
    submitHandle(value) {
      let result = true;
      result = this.$refs.addForm1.validate();
      let form2s = this.$refs.addForm1;
      if (form2s && form2s.length > 0) {
        for (let i = 0; i < form2s.length; i++) {
          result = result && form2s[i].validate();
        }
      }
      if (result === false) {
        return false;
      }
      if (this.envItems && this.envItems.length > 0) {
        value.json = JSON.stringify(this.envItems);
      }
    },
    onRoleChange(value,index){
      this.$set(value,"useridA",'');
      this.$set(value,"useridB",'');
      this.$refs.useridA[index].load({roleId:value.roleId});
    },
    useridAChange(value,index) {
      this.$set(value, "useridB", '');
      this.$refs.useridB[index].load({roleId: value.roleId, userid: value.useridA});
    }
  }
};
</script>
<style lang="scss" scoped>
::v-deep .el-dialog {
  padding-top: 35px;
}
</style>

