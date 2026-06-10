<template>
  <div>
    <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup">
      <md-icon md-src="/static/svg/add.svg"/>新增</k-btn>
    <k-grid
      ref="grid"
      data-action='Test.find'
      @data-row-select="selectRow">
      <k-grid-column data-header="id" data-name="id"></k-grid-column>
      <k-grid-column data-header="值1" data-name="v1"></k-grid-column>
      <k-grid-column data-header="值2" data-name="v2"></k-grid-column>
      <template slot="operate">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP" data-size="mini"
               data-target="updatePopup">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" data-title="新增测试数据">
      <k-form ref="addForm" :data-col="1">
        <k-form-item label="值1">
          <k-field-text v-model="formData.v1" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="值2">
          <k-field-text v-model="formData.v2" :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Test.add" data-target="grid"
                 data-from="addForm" :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"/>
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"/>
            取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="updatePopup" data-title="修改测试数据">
      <k-form ref="addForm" :data-col="1">
        <k-form-item label="id">
          <k-field-text v-model="formData.id" :data-disabled="true" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="值1">
          <k-field-text v-model="formData.v1" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="值2">
          <k-field-text v-model="formData.v2" :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Test.update" data-target="grid"
                 data-from="addForm" :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"/>
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"/>
            取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


  </div>

</template>

<script>

  import {assign} from "lodash";

  export default {
    name: "test",
    data() {
      return {
        formData: {},
        selectRowData: {}
      };
    },
    methods: {
      selectRow(row) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row)
      }
    }
  };
</script>

<style>
</style>
