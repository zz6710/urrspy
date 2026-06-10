<template>
    <div>
      <k-form-search data-model-name="Liable" data-target="liableGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addLiablePopup" v-if="global.isShowAuthorityButton('Liable.add')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search>

      <k-grid ref="liableGrid" @data-row-select="selectRow"  data-action='Liable.find1'>
        <k-grid-column data-header="部门名称" data-name="deptname"></k-grid-column>
        <k-grid-column data-header="id" data-name="liableid" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="部门负责人" data-name="username"></k-grid-column>
        <k-grid-column data-header="分管领导" data-name="leadername" data-action="User.findUsers" data-value-field="userid"
                       data-display-field="username"></k-grid-column>
        <k-grid-column data-header="备注" data-name="remarks" ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改责任人信息" data-functype="POPUP" data-size="mini"
                 data-target="editLiablePopup"  :data-handler="getUserId" v-if="global.isShowAuthorityButton('Liable.update')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="Liable.delete" data-size="mini"
                 data-type="danger" data-target="liableGrid" :data-confirm="true" data-descript="删除责任人信息">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>

      <!--    添加责任人信息弹出框   -->
      <k-popup ref="addLiablePopup" data-title="新增">
        <k-form ref="addLiableForm" :data-col="2">
          <k-form-item label="部门名称">
            <k-field-cascader style="width:100%" v-model="formData.deptno" data-diffcondition="deptno,parentdeptno" @data-on-change="getDeptno"
                              :data-graphql="querydeptGraphql" data-display-child="children" data-check-strictly data-show-num
                              :data-props="{ expandTrigger: 'hover'}" data-size="medium" data-placeholder="请选择所属部门" data-clearable
                              data-fileterable data-display-field="deptname" data-value-field="deptno" :data-allowblank="false">
            </k-field-cascader>
          </k-form-item>

          <k-form-item label="部门责任人">
            <k-field-select v-model="formData.userid"   data-display-field="username" :dataAllowblank="false"
                            data-action="User.findUsers" data-value-field="userid"  />
          </k-form-item>
          <k-form-item label="分管领导">
            <k-field-select v-model="formData.leadername" data-action="User.findUsers"
                          data-value-field="userid"
                          data-display-field="username"/>
          </k-form-item>
          <k-form-item label="备注">
            <k-field-text v-model="formData.remarks"   :data-max-length="255"/>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="liableGrid" data-action="Liable.add" data-from="addLiableForm"
                   :data-model="formData">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>



      <!--    修改责任人信息弹出框   -->
      <k-popup ref="editLiablePopup" data-title="修改">
        <k-form ref="editLiableForm" :data-col="2">
          <k-form-item label="部门名称">
            <k-field-cascader style="width:100%" v-model="formData.deptno" data-diffcondition="deptno,parentdeptno" @data-on-change="getDeptno"
                              :data-graphql="querydeptGraphql" data-display-child="children" data-check-strictly data-show-num data-disabled
                              :data-props="{ expandTrigger: 'hover'}" data-size="medium" data-placeholder="请选择所属部门" data-clearable
                              data-fileterable data-display-field="deptname" data-value-field="deptno" :data-allowblank="false">
            </k-field-cascader>
          </k-form-item>

          <k-form-item label="部门责任人">
            <k-field-select v-model="formData.userid"   data-display-field="username" :dataAllowblank="false"
                            data-action="User.findUsers" data-value-field="userid"  />
          </k-form-item>
          <k-form-item label="分管领导">
            <k-field-select v-model="formData.leadername" data-action="User.findUsers"
                          data-value-field="userid"
                          data-display-field="username"/>
          </k-form-item>
          <k-form-item label="备注">
            <k-field-text v-model="formData.remarks"  :data-max-length="255" />
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="liableGrid" data-action="Liable.update" data-from="editLiableForm"
                   :data-model="formData">
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

import {assign} from "lodash";

export default {
  data() {
    return {
      formData: {},
      userData: {}
    };
  },

  computed: {
    querydeptGraphql() {
      return "{queryDept(action:\"find\") {rows{deptno, deptname, parentdeptno, deptid},results}}"
    }

  },
  methods: {
    selectRow(row, column, event) {
      const _this = this
      _this.formData = assign({}, row)
    },

    getDeptno() {
      this.httpUtil.comnQuery({
        action: "User.getUser",
        params: {deptno: this.formData.deptno}
      }).then(data => {
        this.userData = data.rows;
        this.$nextTick(()=>{
          this.$set(this.formData,"userid","")
        })
      }).catch({})

    },



    getUserId(dataData) {
      this.httpUtil.comnQuery({
        action: "Liable.find1",
        params: {liableid: dataData.liableid}
      }).then(data => {
        this.userData = data.rows;
        // this.$refs.editLiablePopup.popup()
        this.$nextTick(()=>{
          this.$set(this.formData,"userid",dataData.userid);
          this.$set(this.formData,"leadername",data.rows[0].leaderid);
        })
      }).catch({})
    },



  }
};
</script>

<style lang="scss" scoped>
.md-switch {
  position: relative;
  display: inline-block;
  margin: auto;
}
</style>
