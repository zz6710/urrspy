<template>
  <k-form ref="editGroupUserForm" :data-col="1">
    <k-form-item label="用户组ID">
      <k-field-text v-model="value.groupId"/>
    </k-form-item>
    <k-form-item label="A角用户id">
      <k-field-select v-model="value.useridA" data-action="User.getRavUser" data-display-field="username"
                      data-value-field="userid" :data-multiple="false" @data-on-change="refreshUserData" :dataAllowblank="false"></k-field-select>
    </k-form-item>
    <k-form-item label="B角用户id">
      <k-field-select v-model="value.useridB"  :data-data="userData" data-display-field="username"
                      data-value-field="userid"  :data-multiple="false"></k-field-select>
    </k-form-item>
    <k-form-item label="所属A角">
      <k-field-text v-model="value.upperid" data-value-field="formData.groupMod"/>
    </k-form-item>
    <!--    <k-form-item label="A角状态">
          <k-field-text v-model="formData.statuA"/>
          <k-field-bswitch data-on-value="N" data-off-value="D" default="N" v-model="formData.statuA"/>
        </k-form-item>-->
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8GroupUser.updateT8GroupUser"
             data-from="editGroupUserForm" :data-model="value" data-target="GroupUserGrid">
        <md-icon md-src="/static/svg/confirm.svg"/>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"/>取消
      </k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
import kayak from '@/frame/kayak.js'
//import ListWorkDay from "./ListWorkDay";

export default {
  name:"M81012editPeo",
  props: {
    updSuccess: Function,
    kk:{
      type:Object,
    }
  },
  data() {
    return {
      userData: {},
      formData:{
        useridA:"",
        upperid:""
      }
    };
  },
  created(){
    this.httpUtil.comnQuery({
      action: "User.getRevUser",
      params: {userid: this.value.useridA}
    }).then(data => {
      this.userData = data.rows;
      this.$nextTick(() => {
        this.$set(this.formData, "username", "")
      })
    }).catch({})
  },
  methods: {
    refreshUserData() {
      this.$set(this.formData,"upperid",this.formData.useridA)
      console.log("formdata",this.formData)
      this.httpUtil.comnQuery({
        action: "User.getRevUser",
        params: {userid: this.value.useridA}
      }).then(data => {
        this.userData = data.rows;
        this.$nextTick(() => {
          this.$set(this.formData, "username", "")
        })
      }).catch({})
    }
  },
  computed: {
    value() {
      return this.$attrs.value
    }
  }
};
</script>
