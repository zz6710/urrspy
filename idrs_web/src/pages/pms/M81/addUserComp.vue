<template>
  <div>
    <div>
      <k-form-search data-model-name="T8ProdUser" data-target="T8ProdUserGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT8ProdUserPopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="T8ProdUserGrid" @data-row-select="selectRow" data-action="T8ProdUser.findT8ProdUsers" >
        <k-grid-column data-header="" data-name="id"></k-grid-column>
        <k-grid-column data-header="t8_prod_base_id" data-name="t8ProdBaseId"></k-grid-column>
        <k-grid-column data-header="角色Id" data-name="roleId"></k-grid-column>
        <k-grid-column data-header="B角用户id" data-name="useridB"></k-grid-column>
        <k-grid-column data-header="所属A角" data-name="upperid"></k-grid-column>
        <k-grid-column data-header="A角状态" data-name="statuA"></k-grid-column>
        <k-grid-column data-header="提交人员" data-name="inputuser"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime"></k-grid-column>
        <k-grid-column data-header="更新日期" data-name="updDate"></k-grid-column>
        <k-grid-column data-header="更新时间" data-name="updTime"></k-grid-column>
        <k-grid-column data-header="A角用户id" data-name="useridA"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品用户" data-functype="POPUP" data-size="mini"
                 data-target="editT8ProdUserPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdUser.deleteT8ProdUser" data-size="mini"
                 data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除产品用户">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加产品用户弹出框   -->
    <k-popup ref="addT8ProdUserPopup" data-title="新增">
      <k-form ref="addT8ProdUserForm" :data-col="2">
        <k-form-item label="">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="t8_prod_base_id">
          <k-field-text v-model="formData.t8ProdBaseId"/>
        </k-form-item>
        <k-form-item label="角色Id">
          <k-field-text v-model="formData.roleId"/>
        </k-form-item>
        <k-form-item label="B角用户id">
          <k-field-text v-model="formData.useridB"/>
        </k-form-item>
        <k-form-item label="所属A角">
          <k-field-text v-model="formData.upperid"/>
        </k-form-item>
        <k-form-item label="A角状态">
          <k-field-text v-model="formData.statuA"/>
        </k-form-item>
        <k-form-item label="提交人员">
          <k-field-text v-model="formData.inputuser"/>
        </k-form-item>
        <k-form-item label="创建日期">
          <k-field-text v-model="formData.crtDate"/>
        </k-form-item>
        <k-form-item label="创建时间">
          <k-field-text v-model="formData.crtTime"/>
        </k-form-item>
        <k-form-item label="更新日期">
          <k-field-text v-model="formData.updDate"/>
        </k-form-item>
        <k-form-item label="更新时间">
          <k-field-text v-model="formData.updTime"/>
        </k-form-item>
        <k-form-item label="A角用户id">
          <k-field-text v-model="formData.useridA"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdUser.addT8ProdUser" data-from="addT8ProdUserForm"
                 :data-model="formData" data-target="T8ProdUserGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改产品用户弹出框   -->
    <k-popup ref="editT8ProdUserPopup" data-title="修改">
      <k-form ref="editT8ProdUserForm" :data-col="2">
        <k-form-item label="">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="t8_prod_base_id">
          <k-field-text v-model="formData.t8ProdBaseId"/>
        </k-form-item>
        <k-form-item label="角色Id">
          <k-field-text v-model="formData.roleId"/>
        </k-form-item>
        <k-form-item label="B角用户id">
          <k-field-text v-model="formData.useridB"/>
        </k-form-item>
        <k-form-item label="所属A角">
          <k-field-text v-model="formData.upperid"/>
        </k-form-item>
        <k-form-item label="A角状态">
          <k-field-text v-model="formData.statuA"/>
        </k-form-item>
        <k-form-item label="提交人员">
          <k-field-text v-model="formData.inputuser"/>
        </k-form-item>
        <k-form-item label="创建日期">
          <k-field-text v-model="formData.crtDate"/>
        </k-form-item>
        <k-form-item label="创建时间">
          <k-field-text v-model="formData.crtTime"/>
        </k-form-item>
        <k-form-item label="更新日期">
          <k-field-text v-model="formData.updDate"/>
        </k-form-item>
        <k-form-item label="更新时间">
          <k-field-text v-model="formData.updTime"/>
        </k-form-item>
        <k-form-item label="A角用户id">
          <k-field-text v-model="formData.useridA"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdUser.updateT8ProdUser" data-from="editT8ProdUserForm"
                 :data-model="formData" data-target="T8ProdUserGrid">
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
  export default {
    data() {
      return {
        formData: {},
        selectRowData: {}
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    }
  };
</script>
