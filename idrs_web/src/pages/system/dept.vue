<template>
  <div class="py-page">
    <k-form-search-customize data-target="deptGrid" v-model="prodSearchParam" data-label-width="80px">
      <k-form-item label="上级部门">
        <k-field-text v-model="prodSearchParam.parentDeptName" />
      </k-form-item>
      <k-form-item label="部门代码">
        <k-field-text v-model="prodSearchParam.deptno" />
      </k-form-item>
      <k-form-item label="部门名称">
        <k-field-text v-model="prodSearchParam.deptname" />
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
             data-target="addDeptPopup" v-if="global.isShowAuthorityButton('Dept.add')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="deptGrid" id="deptGrid" @data-row-select="selectRow" :data-before-load="dataBeforeLoad" data-action='Dept.find1'
        data-diffcondition="deptno,parentdeptno" :data-show-tree="true" dataTreeId="deptno" data-operate-width="210px">
        <k-grid-column data-header="部门代码" data-name="deptno"></k-grid-column>
        <k-grid-column data-header="部门名称" data-name="deptname"></k-grid-column>
        <k-grid-column data-header="上级部门" data-name="parentDeptName"></k-grid-column>
        <k-grid-column data-header="状态" data-name="deptStatus" data-dict="dept_status"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改部门" data-functype="POPUP" data-size="mini"
            data-target="editDeptPopup" v-if="global.isShowAuthorityButton('Dept.update')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="Dept.delete" data-size="mini"
            data-type="danger" data-target="deptGrid" :data-confirm="true" data-descript="删除部门">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加部门弹出框   -->
    <k-popup ref="addDeptPopup" data-title="新增">
      <k-form ref="addDeptForm" :data-col="1">
        <k-form-item label="父级部门">
          <k-field-tree :data-multiple="false" :data-flat="false" v-model="formData.parentdeptno" data-diffcondition="deptno,parentdeptno" data-graphql='{queryDept(action:"find") {rows{deptno, deptname, parentdeptno, deptid},results}}'
                        data-display-child="children" data-placeholder="请选择父级部门" data-display-field="deptname"
                        data-value-field="deptno">
          </k-field-tree>
        </k-form-item>
        <k-form-item label="部门代码">
          <k-field-text v-model="formData.deptno" :dataAllowblank='false' :data-max-length="20" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字"  @data-on-blur="isDeptNo(formData.deptno)"/>
        </k-form-item>
        <k-form-item label="部门名称">
          <k-field-text v-model="formData.deptname" :dataAllowblank='false' :data-max-length="32" />
        </k-form-item>
        <k-form-item label="部门级别" v-show="false">
          <k-field-select v-model="formData.deptlevel" data-dict="deptlevel" />
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="formData.deptStatus" data-default-value="1" :dataAllowblank='false' data-dict="dept_status" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Dept.add" data-target="deptGrid" data-from="addDeptForm"
            :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改部门弹出框   -->
    <k-popup ref="editDeptPopup" data-title="修改">
      <k-form ref="editDeptForm" :data-col="1">
        <k-form-item label="部门代码">
          <k-field-text v-model="formData.deptno" data-disabled data-clearable="false"  :data-max-length="20" />
        </k-form-item>
        <k-form-item label="父级部门">
           <k-field-text v-model="formData.parentdeptno" data-disabled data-clearable="false"  />
        </k-form-item>
        <k-form-item label="部门名称">
          <k-field-text v-model="formData.deptname" :dataAllowblank='false' :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="部门级别" v-show="false">
          <k-field-select v-model="formData.deptlevel" data-dict="deptlevel"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="formData.deptStatus"  :dataAllowblank='false' data-dict="dept_status" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Dept.update" data-target="deptGrid" data-from="editDeptForm"
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
  import Tools from '@/utils/tools.js';
  import {
    assign
  } from "lodash";
  export default {
    name:"dept",
    name:"dept",
    data() {
      return {
         prodSearchParam: {},//查询参数
        formData: {},
        cascaderValue: '',
        selectRowData: {}
      };
    },
    computed: {
    queryParam() {
      return {
        'deptno': this.prodSearchParam.deptno,
        'parentDeptName': this.prodSearchParam.parentDeptName,
        'deptname': this.prodSearchParam.deptname,
      }
    }
  },

    methods: {
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
        console.log(row)
      },
      dataBeforeLoad(data) {
        data.excDeptno="ROOT";
        return data;
      },

      isDeptNo(value){
        this.httpUtil.comnQuery({
          action: 'Dept.isExistDeptNoNotAin',
          params: {
            deptno : value
          }
        }).then(data => {
          if(data.rows.length > 0 ){
            Tools.alert("部门代码已存在", "warning");
            this.formData.deptno = null;
          }
        });
      },
      isDeptname(value){
        this.httpUtil.comnQuery({
          action: 'Dept.isExistDeptnameNotAin',
          params: {
            deptname : this.formData.deptname
          }
        }).then(data => {
          if(data.rows.length > 0 ){
            Tools.alert("部门名称已存在", "warning");
            this.formData.deptname = null;
            return false;
          }
        });
      },
    }
  };
</script>
