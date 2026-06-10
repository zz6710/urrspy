<template>
  <div>

    <k-form-search-customize data-target="indexInfoGrid" v-model="queryParam">
      <k-form-item label="指数名称">
        <k-field-select v-model="queryParam.id"  data-action="T8IndexInfo.find"
                        data-display-field="indexCode,indexName" data-value-field="id" ></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup"
             v-if="global.isShowAuthorityButton('T8IndexInfo.add')">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search-customize>


    <k-grid ref="indexInfoGrid" data-action="T8IndexInfo.find1" @data-row-select="selectRow">
      <k-grid-column data-align="center" data-header="指数代码" data-name="indexCode"/>
      <k-grid-column data-align="center" data-header="指数名称" data-name="indexName"/>
      <k-grid-column data-align="center" data-header="基准日期" data-type="date" data-name="baseDate"/>
      <k-grid-column data-align="center" data-header="基准点数" data-type="double" data-name="basePoints"/>
      <k-grid-column data-align="center" data-header="成分股数量" data-name="constituentStocks"/>
      <k-grid-column data-align="center" data-header="创建日期" data-type="date" data-name="createDate"/>
      <k-grid-column data-align="center" data-header="创建时间" data-type="time" data-name="createTime"/>
      <k-grid-column data-align="center" data-header="更新日期" data-type="date" data-name="updateDate"/>
      <k-grid-column data-align="center" data-header="更新时间" data-type="time" data-name="updateTime"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改指数信息" data-functype="POPUP" data-size="mini"
               data-target="editPopup" :data-handler="editHandler"
               v-if="global.isShowAuthorityButton('T8IndexInfo.update')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8IndexInfo.delete"
               data-size="mini"
               data-type="danger" data-target="indexInfoGrid" :data-confirm="true" data-descript="删除"
               v-if="global.isShowAuthorityButton('T8IndexInfo.delete')">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" data-title="新增">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="指数代码">
          <k-field-text :data-allowblank="false"  v-model="formData.indexCode" :data-max-length="22"/>
        </k-form-item>
        <k-form-item label="指数名称">
          <k-field-text :data-allowblank="false" :data-max-length="128" v-model="formData.indexName"/>
        </k-form-item>
        <k-form-item label="基准日期">
          <k-field-date  :data-max-length="8" v-model="formData.baseDate"/>
        </k-form-item>
        <k-form-item label="基准点数">
          <k-field-text  v-model="formData.basePoints" data-validate-type="number"
                         data-type="number" :data-max-length="22"/>
        </k-form-item>
        <k-form-item label="成分股数量">
          <k-field-text v-model="formData.constituentStocks" :data-max-length="128"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8IndexInfo.add" data-target="indexInfoGrid"
                 data-from="addForm" :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="editPopup" data-title="修改">
      <k-form ref="editForm" data-ui="element">
        <k-form-item label="指数代码">
          <k-field-text :data-disabled="true" v-model="editFormData.indexCode" :data-max-length="22"/>
        </k-form-item>
        <k-form-item label="指数名称">
          <k-field-text :data-allowblank="false" :data-max-length="128" v-model="editFormData.indexName"/>
        </k-form-item>
        <k-form-item label="基准日期">
          <k-field-date v-model="editFormData.baseDate"/>
        </k-form-item>
        <k-form-item label="基准点数">
          <k-field-text  v-model="editFormData.basePoints" data-validate-type="number"
                         data-type="number" :data-max-length="22"/>
        </k-form-item>
        <k-form-item label="成分股数量">
          <k-field-text  v-model="editFormData.constituentStocks" :data-max-length="128"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8IndexInfo.update" data-target="indexInfoGrid"
                 data-from="editForm" :data-model="editFormData">
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
  import {assign} from "lodash";

  export default {
    name: "",
    data() {
      return {
        queryParam:{},
        formData: {
          indexCode: '',
          indexName: '',
          baseDate: '',
          basePoints: '',
          constituentStocks: ''
        },
        editFormData: {
          indexCode: '',
          indexName: '',
          baseDate: '',
          basePoints: '',
          constituentStocks: ''
        }
      }
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row)
      },
      editHandler(value) {
        this.editFormData = value;
      }
    }
  }
</script>

<style scoped>

</style>
