<template>
  <div>
    <k-form-search data-target="announceGrid" data-model-name="Announce">
      <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
    </k-form-search>
    <k-grid ref="announceGrid" data-action="Announce.find" @data-row-select="selectRow">
      <k-grid-column data-header="公告标题" data-name="title" />
      <k-grid-column data-header="发布用户" data-name="createuserName" />
      <k-grid-column data-header="生效日期" data-type="date" data-name="effectiveDate" />
      <k-grid-column data-header="失效日期" data-type="date" data-name="invalidDate" />
      <k-grid-column data-header="发布日期" data-type="date" data-name="createdate" />
      <k-grid-column data-header="发布时间" data-type="time" data-name="createtime" />
      <k-grid-column data-header="附件名称" data-name="annfilename" />
      <template slot="operate">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改公告" data-functype="POPUP" data-size="mini"
               data-target="editOrgPopup" :data-handler="findAnnounceRoles">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="Announce.delete" data-size="mini"
               data-type="danger" data-target="roleGrid" :data-confirm="true" data-descript="删除">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="editOrgPopup" data-title="修改" data-width="800px">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="标题" data-input-width="500px">
          <k-field-text :data-allowblank="false" v-model="formData.title" />
        </k-form-item>
        <k-form-item label="内容" data-ui="element" data-input-width="500px">
          <k-field-rich :data-allowblank="false" :data-menus='["bold","fontSize","fontName","italic","underline","strikeThrough","foreColor",
                                                          "backColor","link","justify","quote","emoticon","undo","redo"]'
                        v-model="formData.content" />
        </k-form-item>
        <k-form-item label="开始日期" data-ui="element" data-input-width="500px">
          <k-field-date :data-allowblank="false" v-model="formData.effectiveDate" :data-max-value="formData.invalidDate"></k-field-date>
        </k-form-item>
        <k-form-item label="结束日期" data-ui="element" data-input-width="500px">
          <k-field-date v-model="formData.invalidDate" :data-allowblank="false" :data-min-value="formData.effectiveDate"></k-field-date>
        </k-form-item>
        <k-form-item label="原附件" data-ui="element" data-input-width="500px">
          <k-field-display v-model="formData.annfilename"></k-field-display>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload v-model="formData.sucData" data-type="picture" :data-multiple="false" :data-limit=1
                          :data-auto-upload="true" />
        </k-form-item>
        <k-form-item label="角色">
          <k-field-select v-model="formData.roleIds" :data-graphql='queryRoleGraphql' :dataMultiple="true"
                          data-display-field="rolename" data-value-field="roleid"></k-field-select>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AnnounceInfo.edit" data-target="announceGrid"
                 data-from="addForm" :data-model="formData" :data-handler="submitBeforeHandler">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <k-popup ref="addPopup" data-title="新增" data-width="800px">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="标题" data-input-width="500px">
          <k-field-text :data-allowblank="false" v-model="formData.title" />
        </k-form-item>
        <k-form-item label="内容" data-ui="element" data-input-width="500px">
          <k-field-rich :data-allowblank="false" :data-menus='["bold","fontSize","fontName","italic","underline","strikeThrough","foreColor",
                                                          "backColor","link","justify","quote","emoticon","undo","redo"]'
                        v-model="formData.content" />
        </k-form-item>
        <k-form-item label="开始日期" data-ui="element" data-input-width="500px">
          <k-field-date :data-allowblank="false" v-model="formData.effectiveDate" :data-max-value="formData.invalidDate"></k-field-date>
        </k-form-item>
        <k-form-item label="结束日期" data-ui="element" data-input-width="500px">
          <k-field-date v-model="formData.invalidDate" :data-allowblank="false" :data-min-value="formData.effectiveDate"></k-field-date>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload v-model="formData.sucData" data-type="picture" :data-multiple="false" :data-limit=1
                          :data-auto-upload="true" />
        </k-form-item>
        <k-form-item label="角色">

          <k-field-select v-model="formData.roleIds" :data-graphql='queryRoleGraphql' :dataMultiple="true"
                          data-display-field="rolename" data-value-field="roleid"></k-field-select>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AnnounceInfo.add" data-target="announceGrid"
                 data-from="addForm" :data-model="formData" :data-handler="submitBeforeHandler">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import {
    assign,
    merge
  } from "lodash";
  import httpUtil from "../../frame/httpUtil";
  import Tools from "../../utils/tools";

  export default {
    name:"announce",
    data() {
      return {
        selectRowData: {},
        formData: {},
        queryRoleGraphql: "{queryRole(action:\"find\",roleids:\"0\") {rows{roleid, rolename, parentroleid, descript},results}}"
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row)
      },
      findAnnounceRoles() {
        setTimeout(() => {
          this.httpUtil
            .comnQuery({
              action: "AnnounceRole.find",
              params: {
                "annid": this.selectRowData.annid
              }
            })
            .then(data => {
              if (data.rows && data.rows.length > 0) {
                let roleIds = ''
                data.rows.forEach(r => roleIds = roleIds + ',' + r.roleid)
                roleIds = roleIds.substr(1);
                this.$set(this.formData, 'roleIds', roleIds)
              }
            });
        }, 50)
      },
      submitBeforeHandler(params) {
        if (params.sucData && params.sucData[0].response.success == true) {
          params.annfilename = params.sucData[0].response.returndata.upload_name;
          params.annfilepath = params.sucData[0].response.returndata.upload_path;
          params.annfilecode = params.sucData[0].response.returndata.upload_code;
          this.$delete(params, "sucData")
        }
        let roleIds = params.roleIds;
        if (roleIds && roleIds.length > 0) {
          params.roleIds = JSON.stringify(roleIds.split(","))
        }
      }
    }
  };
</script>
