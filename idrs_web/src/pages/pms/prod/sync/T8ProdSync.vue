<template>
  <div>
    <div>
      <k-form-search data-model-name="T8ProdSync" data-target="t8ProdSyncGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="showAdd"
               data-target="addT8ProdSyncPopup">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="t8ProdSyncGrid" @data-row-select="selectRow" data-action="T8ProdSync.findT8ProdSyncs">
        <k-grid-column data-header="id" data-name="id"></k-grid-column>
        <k-grid-column data-header="表名" data-name="tableName"></k-grid-column>
        <!--        <k-grid-column data-header="字段" data-name="fieldName"></k-grid-column>-->
        <k-grid-column data-header="创建日期" data-name="crtDate"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUser"></k-grid-column>
        <k-grid-column data-header="更新日期" data-name="updDate"></k-grid-column>
        <k-grid-column data-header="更新时间" data-name="updTime"></k-grid-column>
        <k-grid-column data-header="更新人" data-name="updUser"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改同步表数据" data-functype="POPUP" data-size="mini"
                 data-target="editT8ProdSyncPopup" :data-handler="showEdit(scope)">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8ProdSync.deleteT8ProdSync" data-size="mini"
                 data-type="danger" data-target="t8ProdSyncGrid" :data-confirm="true" data-descript="删除同步表数据">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加同步表数据弹出框   -->
    <k-popup ref="addT8ProdSyncPopup" data-title="新增">
      <k-form ref="addT8ProdSyncForm" :data-col="2">

        <k-form-item label="表名">
          <k-field-text v-model="formData.tableName" :dataAllowblank="false"
                        @data-on-blur="getFiedName(formData.tableName)"/>
        </k-form-item>

        <k-form-item label="表字段" :data-col="2">
          <!--          <k-field-select v-model="formData.fieldName" data-multiple="true" :dataAllowblank="false"
                                    :data-data="fieldNameList"
                                    data-display-field="fieldName"
                                    data-value-field="fieldName"/>-->
          <k-field-text v-model="formData.fieldName" inputType="textarea" :dataAllowblank="false"></k-field-text>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSync.addT8ProdSync"
                 data-from="addT8ProdSyncForm"
                 :data-model="formData" data-target="t8ProdSyncGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改同步表数据弹出框   -->
    <k-popup ref="editT8ProdSyncPopup" data-title="修改">
      <k-form ref="editT8ProdSyncForm" :data-col="2">
        <k-form-item label="表名">
          <k-field-text v-model="formData.tableName" :dataAllowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="表字段" :data-col="2">
          <!--          <k-field-select v-model="formData.fieldName" data-multiple="true" :dataAllowblank="false"
                                    :data-data="fieldNameList"
                                    data-display-field="fieldName"
                                    data-value-field="fieldName"/>-->
          <k-field-text v-model="formData.fieldName" inputType="textarea" :dataAllowblank="false"></k-field-text>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSync.updateT8ProdSync"
                 data-from="editT8ProdSyncForm" :data-handler="clear"
                 :data-model="formData" data-target="t8ProdSyncGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE" :data-handler="clear">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  data() {
    return {
      fieldNameList: {},
      formData: {},
      selectRowData: {}
    };
  },
  methods: {
    clear() {
      this.formData = {};
      this.fieldNameList = []
    },
    showEdit(rows) {
      this.getFiedName()
    },
    showAdd() {
      this.clear()
      this.getFiedName()
    },
    getFiedName(val) {
      console.log("zheshisah", val)
      if (val != undefined && val != '') {
        //如果数据库表存在则进行查询字段
        this.httpUtil.comnQuery({
          action: "T8ProdSync.getTableField",
          params: {tableName: val},
          successAlert: false
        }).then(data => {
          if (data.returnmsg == '查询成功') {
            this.fieldNameList = data.returndata.data.rows
          } else {
            this.fieldNameList = []
            Tools.alert("输入表名不存在", "danger");
            this.formData.tableName = ''
          }
        })
      }


    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    }
  }

};
</script>
