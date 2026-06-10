<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="disclosureDataSetGrid">
        <k-form-item label="信披类型">
          <k-field-select v-model="searchParam.disclosureType" data-dict="xp_doc_type"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型">
          <k-field-select v-model="searchParam.disclosureSonType" :data-data="searchSonTypes" data-display-field="label"
                          data-value-field="value"></k-field-select>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" slot="button"
               data-target="addDisclosureDataSetPopup" v-if="global.isShowAuthorityButton('DisclosureDataSet.addDisclosureDataSet')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureDataSetGrid" @data-row-select="selectRow"
              data-action="DisclosureDataSet.findDisclosureDataSetsAuth">
        <k-grid-column data-align="center" data-header="id" data-name="id"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type"></k-grid-column>
        <k-grid-column data-align="center" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type"></k-grid-column>
        <k-grid-column data-align="center" data-header="状态" data-name="status" data-dict="t8_disclosure_dataset_status"></k-grid-column>
        <k-grid-column data-align="center" data-header="执行条件" data-name="conditions"></k-grid-column>
        <k-grid-column data-align="center" data-header="执行顺序" data-name="execOrder"></k-grid-column>
        <k-grid-column data-align="center" data-header="备注" data-name="remark" data-width="150"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建人姓名" data-name="crtUserName"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"></k-grid-column>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="详情" data-functype="POPUP" data-size="mini"
                 data-target="viewDisclosureDataSetPopup">
            <md-icon>library_books</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP" data-size="mini"
                 data-target="editDisclosureDataSetPopup" :data-disabled="scope.row.row.status == 'N'" v-if="global.isShowAuthorityButton('DisclosureDataSet.updateDisclosureDataSet')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="启用" data-functype="SUBMIT"
                 :data-disabled="scope.row.row.status == 'N'"
                 data-action="DisclosureDataSet.enableDisclosureData" :data-confirm="true"
                 data-target="disclosureDataSetGrid"
                 data-size="mini">
            <md-icon>lock_open</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="停用"
                 :data-disabled="scope.row.row.status=='P'"
                 data-action="DisclosureDataSet.unableDisclosureData" :data-confirm="true"
                 data-target="disclosureDataSetGrid" data-functype="SUBMIT" data-size="mini"
          >
            <md-icon>lock</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="DisclosureDataSet.deleteDisclosureDataSet" data-size="mini"
                 :data-disabled="scope.row.row.status == 'N'"
                 data-type="danger" data-target="disclosureDataSetGrid" :data-confirm="true" data-descript="删除">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加数据集视转换弹出框   -->
    <k-popup ref="addDisclosureDataSetPopup" data-title="新增" :dataDialogDrag="true">
      <k-form ref="addDisclosureDataSetForm" :data-col="2">
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="信披子类型" v-show="showDiscloSonType">
          <k-field-select v-model="formData.disclosureSonType" :data-allowblank="sonTypeAllowBlank"
                          :data-data="disclosureSonTypes"
                          data-display-field="label" data-value-field="value"/>
        </k-form-item>
        <k-form-item label="执行条件">
          <k-field-text v-model="formData.conditions" :data-allowblank="false" :dataMaxLength="200"/>
        </k-form-item>
        <k-form-item label="查询字段" :dataCol="2">
          <k-field-text v-model="formData.selectColumns" inputType="textarea" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="执行顺序">
          <k-field-text v-model="formData.execOrder" data-validate-type="int" :data-allowblank="false"
                        :dataMaxLength="2"/>
        </k-form-item>
        <k-form-item label="查询SQL语句" :dataCol="2">
          <k-field-text v-model="formData.selectSqls" inputType="textarea" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="目标表删除SQL语句" :dataCol="2">
          <k-field-text v-model="formData.deleteSqls" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="目标表插入SQL语句" :dataCol="2">
          <k-field-text v-model="formData.insertSqls" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="目标表更新SQL语句" :dataCol="2">
          <k-field-text v-model="formData.updateSqls" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.remark" inputType="textarea"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureDataSet.addDisclosureDataSet"
                 data-from="addDisclosureDataSetForm"
                 :data-model="formData" data-target="disclosureDataSetGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改数据集视转换弹出框   -->
    <k-popup ref="editDisclosureDataSetPopup" data-title="修改" :dataDialogDrag="true">
      <k-form ref="editDisclosureDataSetForm" :data-col="2">
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="信披子类型" v-show="showDiscloSonType">
          <k-field-select v-model="formData.disclosureSonType" :data-allowblank="sonTypeAllowBlank"
                          :data-data="disclosureSonTypes"
                          data-display-field="label" data-value-field="value"/>
        </k-form-item>
        <k-form-item label="执行条件">
          <k-field-text v-model="formData.conditions" :data-allowblank="false" :dataMaxLength="200"/>
        </k-form-item>
        <k-form-item label="执行顺序">
          <k-field-text v-model="formData.execOrder" data-validate-type="int" :data-allowblank="false"
                        :dataMaxLength="2"/>
        </k-form-item>
        <k-form-item label="查询字段" :dataCol="2">
          <k-field-text v-model="formData.selectColumns" inputType="textarea" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="查询SQL语句" :dataCol="2">
          <k-field-text v-model="formData.selectSqls" inputType="textarea" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="目标表删除SQL语句" :dataCol="2">
          <k-field-text v-model="formData.deleteSqls" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="目标表插入SQL语句" :dataCol="2">
          <k-field-text v-model="formData.insertSqls" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="目标表更新SQL语句" :dataCol="2">
          <k-field-text v-model="formData.updateSqls" inputType="textarea"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.remark" inputType="textarea"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureDataSet.updateDisclosureDataSet"
                 data-from="editDisclosureDataSetForm"
                 :data-model="formData" data-target="disclosureDataSetGrid" :data-handler="updateHandler">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    数据集视转换详情弹出框   -->
    <k-popup ref="viewDisclosureDataSetPopup" data-title="详情" :dataDialogDrag="true">
      <k-form ref="editDisclosureDataSetForm" :data-col="2">
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type"
                          :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披子类型" v-show="showDiscloSonType">
          <k-field-select v-model="formData.disclosureSonType"
                          :data-data="disclosureSonTypes" :data-disabled="true"
                          data-display-field="label" data-value-field="value"/>
        </k-form-item>
        <k-form-item label="执行条件">
          <k-field-text v-model="formData.conditions" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="执行顺序">
          <k-field-text v-model="formData.execOrder" data-validate-type="int"
                        :data-disabled="true"
                        :dataMaxLength="2"/>
        </k-form-item>
        <k-form-item label="查询字段" :dataCol="2">
          <k-field-text v-model="formData.selectColumns" inputType="textarea" :data-disabled="true" :rows="6"/>
        </k-form-item>
        <k-form-item label="查询SQL语句" :dataCol="2">
          <k-field-text v-model="formData.selectSqls" inputType="textarea"
                        :data-disabled="true" :rows="10"/>
        </k-form-item>
        <k-form-item label="目标表删除SQL语句" :dataCol="2">
          <k-field-text v-model="formData.deleteSqls" inputType="textarea" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="目标表插入SQL语句" :dataCol="2">
          <k-field-text v-model="formData.insertSqls" inputType="textarea" :data-disabled="true" :rows="10"/>
        </k-form-item>
        <k-form-item label="目标表更新SQL语句" :dataCol="2">
          <k-field-text v-model="formData.updateSqls" inputType="textarea" :data-disabled="true" :rows="10"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol="2">
          <k-field-text v-model="formData.remark" inputType="textarea" :data-disabled="true"/>
        </k-form-item>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam: {},//查询条件
      showDiscloSonType: false,//是否展示信披子类型
      sonTypeAllowBlank: true,
      disclosureSonTypes: {},//信披子类型
      searchSonTypes: {},
    };
  },
  watch: {
    //监听信披类型
    'formData.disclosureType'(value) {
      this.disclosureTypeChange(value);
    },
    //查询条件监听信披类型
    'searchParam.disclosureType'(value) {
      this.$set(this.searchParam, 'disclosureSonType', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: value}
      }).then(data => {
        this.searchSonTypes = data.rows;
      }).catch({})
    },
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    //信披类型发生改变
    disclosureTypeChange(value) {
      if (value == '5' || value == '6') {
        this.showDiscloSonType = true;
        this.findDisSonType(value);
        this.sonTypeAllowBlank = false;
      } else {
        this.showDiscloSonType = false;
        this.sonTypeAllowBlank = true;
        this.$set(this.formData, "disclosureSonType", "");
      }
    },
    //根据信披类型查询信披子类型
    findDisSonType(value) {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: value}
      }).then(data => {
        this.disclosureSonTypes = data.rows;
      }).catch({})
    },
    updateHandler() {
      //如果父类型不是 定期报告和整体报告 将子类型置空
      if (this.formData.disclosureType != '5' && this.formData.disclosureType != '6') {
        this.formData.disclosureSonType = '';
      }
    }
  }
};
</script>
