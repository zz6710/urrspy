<template>
  <div>
    <div>
      <k-form-search data-model-name="T8OnlineWordTableColumns" data-target="t8OnlineWordTableColumnsGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addT8OnlineWordTableColumnsPopup"
               v-if="global.isShowAuthorityButton('T8OnlineWordTableColumns.addT8OnlineWordTableColumns')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="t8OnlineWordTableColumnsGrid" @data-row-select="selectRow"
              data-action="T8OnlineWordTableColumns.findT8OnlineWordTableColumns1">
        <k-grid-column data-hidden="true" data-header="ID" data-name="id"/>
        <k-grid-column data-header="表名" data-name="tableName"/>
        <k-grid-column data-header="数据源字段" data-name="columnName"/>
        <k-grid-column data-header="文档映射字段" data-name="docColumn"/>
        <k-grid-column data-header="列注释" data-name="columnComment"/>
        <k-grid-column data-header="默认值" data-name="defaultValue"/>
        <k-grid-column data-header="数据字典" data-name="dict"/>
        <k-grid-column data-header="是否可编辑" data-name="isDisabled" data-dict="1yes0no"/>
        <k-grid-column data-header="数据状态" data-name="status" data-dict="t8_enable_status"/>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改文档在线编辑表映射" data-functype="POPUP"
                 data-size="mini"
                 data-target="editT8OnlineWordTableColumnsPopup"
                 v-if="global.isShowAuthorityButton('T8OnlineWordTableColumns.updateT8OnlineWordTableColumns')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8OnlineWordTableColumns.deleteT8OnlineWordTableColumns" data-size="mini"
                 data-type="danger" data-target="t8OnlineWordTableColumnsGrid" :data-confirm="true" data-descript="删除文档在线编辑表映射"
                 v-if="global.isShowAuthorityButton('T8OnlineWordTableColumns.deleteT8OnlineWordTableColumns')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加文档在线编辑表映射弹出框   -->
    <k-popup ref="addT8OnlineWordTableColumnsPopup" data-title="新增">
      <k-form ref="addT8OnlineWordTableColumnsForm" :data-col="2">
        <k-form-item label="表名">
          <k-field-text v-model="formData.tableName"/>
        </k-form-item>
        <k-form-item label="数据源字段">
          <k-field-text v-model="formData.columnName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="文档映射字段">
          <k-field-text v-model="formData.docColumn" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="文档字段描述">
          <k-field-text v-model="formData.columnComment" :data-allowblank="false"/>
        </k-form-item>
        <!--如果默认值不为空，对对应对应字段从默认值取值，如文档映射字段:person_qd-->
        <k-form-item label="默认值">
          <k-field-text v-model="formData.defaultValue" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="为空时默认显示">
          <k-field-text v-model="formData.emptyDefaultVal" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.dataType" data-dict="t8_data_type"/>
        </k-form-item>
        <k-form-item label="保留小数">
          <k-field-select v-model="formData.dataDigits" data-dict="t8_data_digits"/>
        </k-form-item>
        <k-form-item label="数据字典">
          <k-field-text v-model="formData.dict" />
        </k-form-item>
        <k-form-item label="是否可编辑">
          <k-field-select v-model="formData.isDisabled" data-dict="1yes0no" :data-allowblank="false"/>
        </k-form-item>
<!--        <k-form-item label="是否启用">
          <k-field-select v-model="formData.status" data-dict="t8_enable_status" :data-allowblank="false"/>
        </k-form-item>-->
        <k-form-item label="使用单独数据源"  :data-col="2">
          <k-field-text v-model="formData.sqlInfo" inputType="textarea" :rows="5"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="T8OnlineWordTableColumns.addT8OnlineWordTableColumns"
                 data-from="addT8OnlineWordTableColumnsForm"
                 :data-model="formData" data-target="t8OnlineWordTableColumnsGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改文档在线编辑表映射弹出框   -->
    <k-popup ref="editT8OnlineWordTableColumnsPopup" data-title="修改">
      <k-form ref="editT8OnlineWordTableColumnsForm" :data-col="2">
        <k-form-item label="表名">
          <k-field-text v-model="formData.tableName"/>
        </k-form-item>
        <k-form-item label="数据源字段">
          <k-field-text v-model="formData.columnName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="文档映射字段">
          <k-field-text v-model="formData.docColumn" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="文档字段描述">
          <k-field-text v-model="formData.columnComment" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="默认值">
          <k-field-text v-model="formData.defaultValue" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="为空时默认显示">
          <k-field-text v-model="formData.emptyDefaultVal" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="formData.dataType" data-dict="t8_data_type"/>
        </k-form-item>
        <k-form-item label="保留小数">
          <k-field-select v-model="formData.dataDigits" data-dict="t8_data_digits"/>
        </k-form-item>
        <k-form-item label="数据字典">
          <k-field-text v-model="formData.dict" />
        </k-form-item>
        <k-form-item label="是否可编辑">
          <k-field-select v-model="formData.isDisabled" data-dict="1yes0no" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="使用单独数据源"  :data-col="2">
          <k-field-text v-model="formData.sqlInfo" inputType="textarea" :rows="5"/>
        </k-form-item>
    <!--    <k-form-item label="是否启用">
          <k-field-select v-model="formData.status" data-dict="t8_enable_status" :data-allowblank="false"/>
        </k-form-item>-->
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="T8OnlineWordTableColumns.updateT8OnlineWordTableColumns"
                 data-from="editT8OnlineWordTableColumnsForm"
                 :data-model="formData" data-target="t8OnlineWordTableColumnsGrid">
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
  import KFieldBswitch from "../../../components/k-element/k-field-bswitch/k-field-bswitch";
  export default {
    components: {KFieldBswitch},
    data() {
      return {
        formData: {
          tableName:'',
          columnName:'',
          columnComment:'',
          defaultValue:'',
          isDisabled:'',
          emptyDefaultVal:'',
          dataType:'',
          dataDigits:'',
          docColumn:'',
          sqlInfo:''
        },
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
