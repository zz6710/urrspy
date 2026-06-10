<template>
  <div style=" background: #FFFFFF;height: 800px">
    <div style="margin-left: 20%">
      <k-form ref="addForm">
        <template v-for="(item, index) in formFieldList">
          <!--<k-form-item :key="index" :label="item.displayName">
            <component v-if="item.fieldType==='k-field-text'"
              :is="item.fieldType"
              v-model="filFormData[item.name]"
              :data-default-value="item.value"
              :data-allowblank="false"
              :data-dict="item['data-dict']"
              :data-type="item['dataType']"
              :data-digits="item['dataDigits']"
              :data-validate-type="item['dataValidateType']"
              :data-max-length="item['dataMaxLength']"
              v-bind="item.condition"
            ></component>
            <component v-else
                       :is="item.fieldType"
                       v-model="filFormData[item.name]"
                       :data-default-value="item.value"
                       :data-allowblank="false"
                       :data-dict="item['data-dict']"
            ></component>
          </k-form-item>-->
          <k-form-item :key="index" :label="item.columnLabel" v-if="item.isdisplay ==='1'">
            <component v-if="item.functype==='k-field-text'"
                       :is="item.functype"
                       v-model="filFormData[item.columnKey]"
                       :data-default-value="item.value"
                       :data-allowblank="true"
                       :data-disabled="isEdit(item)"
                       :data-dict="item['dict']"
                       :data-type="item['dataType']"
                       :data-digits="item['dataDigits']"
                       :data-validate-type="item['dataValidateType']"
                       :data-max-length="item['dataMaxLength']"
                       v-bind="item.condition"
            ></component>
            <component v-else
                       :is="item.functype"
                       v-model="filFormData[item.columnKey]"
                       :data-disabled="isEdit(item)"
                       :data-default-value="item.value"
                       :data-allowblank="true"
                       :data-dict="item['dict']"
            ></component>
          </k-form-item>
        </template>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="editBtn"
                 data-from="addForm" :data-model="filFormData" :data-handler="beforeSubmit">确定
          </k-btn>
        </k-form-footer>
      </k-form>
    </div>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name: "SupplementaryRecord",
    data() {
      return {
        filFormData: {},
        currentUserRoles: "",
        formFieldList: []
        /*formFieldList: [{displayName: "测试一",value:"1", fieldType: "k-field-text", name: "managerCode",dataMaxLength:5}, {
          displayName: "测试二",
          fieldType: "k-field-text",
          value:"1",
          name: "managerCode2"
        }, {displayName: "测试3", fieldType: "k-field-select", name: "managerCode3",value:"01", "data-dict": "t8_raise_type"},
          {displayName: "测试4", fieldType: "k-field-date",value:"20210421", name: "managerCode4"},
          {displayName: "测试5", fieldType: "k-field-time",value:"170000", name: "managerCode5"},
          {displayName: "测试6", fieldType: "k-field-text",value:"默认值", name: "managerCode6"},
          {displayName: "测试7", fieldType: "k-field-text", name: "managerCode7",dataType:"money",dataDigits:"2",dataValidateType:"money"}]*/
      }
    },
    created() {
      this.httpUtil.comnQuery({
        action: 'DisclosureModColumn.findSupplementaryRecord',
        params: {},
      }).then(data => {
        this.formFieldList = data.rows
        //获取系统当前用户
        Tools.getLoginUser().then(res => {
          this.currentUserRoles = res.roleids
          console.log(this.currentUserRoles)
          /*  var roleids = this.formFieldList[].roleids.split(',')
            for (var id in roleids) {
              var index = roleid.indexOf(id);
              if (index != -1) {
                  this.isEdit = false;
              }
            }*/
        })
      });
    },
    methods: {
      isEdit(item) {
        if (this.currentUserRoles === '0') {
          return false;
        }
        //判断当期用户是否有输入权限
        var roleids = item.roleids.split(',')
         if (roleids && roleids.length > 0) {
           for (var id in roleids) {
             var index = this.currentUserRoles.indexOf(roleids[id]);
             if (index != -1) {
               return false;
             }
           }
         }
        return true;
      },
      beforeSubmit(row) {
        let re = this.$refs.addForm.validate();
        if (re === true) {
          var param = {}
          param.jsonData = JSON.stringify(row);
          this.httpUtil.comnUpdate({
            action: "DisclosureWordDate.addSupplementaryRecord",
            params: param,
            mask: false
          }).then(res => {
            this.$refs.editBtn.setIconStyle(1, [])
          });
        }

      },
    },
  }
</script>

<style scoped>

</style>
