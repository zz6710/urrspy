<template>
  <div class="py-page">
    <div>
      <k-form-search data-target="roleGrid" data-model-name="Role" data-label-width="80px">
      </k-form-search>
    </div>
    <!--    添加role弹出框   -->
    <k-popup ref="addRolePopup" data-title="添加角色">
      <k-form ref="addRoleForm" :data-col="1" data-input-width="300px">
          <k-form-item label="父角色名">
          <k-field-select v-model="formData.parentroleid" data-action="Role.findParents" :data-allowblank="false"
                          data-display-field="roleid,rolename" data-value-field="roleid" />
        </k-form-item>
        <k-form-item label="角色名">
          <k-field-text v-model="formData.rolename" :data-allowblank="false" :data-max-length="30" />
        </k-form-item>
        <k-form-item label="角色描述">
          <k-field-text v-model="formData.descript" :data-max-length="128"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Role.addRole" data-from="addRoleForm"
                  :data-model="formData" data-target="roleGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <div class="py-page-container">
      <div class="table-top-btns">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addRolePopup" v-if="global.isShowAuthorityButton('Role.addRole')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </div>
      <k-grid ref="roleGrid" v-if="showGrid" @data-row-select="selectRow" data-action="Role.findChildren1"
              :data-tree-show="true" :data-params='{"roleids":this.userRole}'
              data-operate-width="300px" :dataOperateFixed="false">
        <slot slot="header-operate">
          <k-btn class="md-clear md-sm" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addRolePopup">
            <md-icon md-src="/static/svg/add.svg" />添加角色
          </k-btn>
        </slot>
        <k-grid-column data-header="角色名" data-name="rolename"></k-grid-column>
        <k-grid-column data-header="父角色名称" data-name="parentRoleName"></k-grid-column>
        <k-grid-column data-header="角色描述" data-name="descript"></k-grid-column>
        <template slot="operate">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改角色" data-functype="POPUP" data-size="mini"
                data-target="editRolePopup" v-if="global.isShowAuthorityButton('Role.updateRole')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-warning md-just-icon md-simple" data-descript="权限设置" data-functype="POPUP" data-size="mini"
                data-target="setRolePermissionPopup" v-if="global.isShowAuthorityButton('RoleAuthority.save')">
            <md-icon md-src="/static/svg/auth.svg" />
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="Role.deleteRole" data-size="mini"
                data-type="danger" data-target="roleGrid" :data-confirm="true" data-descript="删除">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    权限设置弹出框   -->
    <k-popup ref="setRolePermissionPopup" data-width="500px" data-title="权限设置" @data-closed="()=>{this.selectRoleId=''}"
             @data-opened="()=>{this.selectRoleId=selectRowData.roleid}" >
      <RoleAuthority ref="roleAuthority" :roleId="selectRoleId" :roleName="selectRowData.rolename" :click-call-back="closePopup"/>
    </k-popup>

    <!--    修改角色弹出框   -->
    <k-popup ref="editRolePopup" data-title="修改">
      <k-form ref="editRoleForm" :data-col="1">
          <k-form-item label="父角色名">
            <k-field-select v-model="formData.parentroleid" data-action="Role.findParents" :data-allowblank="false" data-check-strictly data-show-num :data-props="{ expandTrigger: 'hover'}"
                            data-display-field="roleid,rolename" data-value-field="roleid"  data-size="medium" data-placeholder="请选择机构" />
        </k-form-item>
        <k-form-item label="角色名">
          <k-field-text v-model="formData.rolename"  :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="角色描述">
          <k-field-text v-model="formData.descript" data-allowblank :data-max-length="128" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Role.updateRole" data-from="editRoleForm"
                 :data-model="formData" data-target="roleGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import RoleAuthority from "./role-authority";
  import {
    assign
  } from "lodash";
  import Tools from "../../../utils/tools";

  export default {
    name:"role",
    components: {
      RoleAuthority,
    },
    data() {
      return {
        userRole: '',
        formData: {},
        cascaderValue: [],
        selectRowData: {},
        dialogOpen: false,
        selectRoleId: '',
        showGrid: false
      };
    },
    created() {
      Tools.getLoginUser().then(res => {
        this.userRole = res.roleids;
        this.showGrid = true
      })
    },

    computed: {
      queryRoleGraphql() {
        return "{queryRole(action:\"find\",roleids:\"" + this.userRole +
          "\") {rows{roleid, rolename, parentroleid, descript},results}}"
      }
    },
    methods: {
      closePopup(){
        this.$refs.setRolePermissionPopup.close()
      },
      dataBeforeLoad() {
        return {
          "roleids": this.userRole
        }
      },
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      }
    }
  };
</script>
