<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="BaseExMapModel" v-model="searchParam" data-target="BaseExMapModelGrid">
        <k-form-item label="来源系统">
          <k-field-select v-model="searchParam.moduleid" data-dict="interface_type"/>
        </k-form-item>
        <k-form-item label="本地字典查询">
          <k-field-select v-model="searchParam.dict" data-action="BaseExMapModel.findDictByNm" :dataRemote="true"
                          data-display-field="dict,dictname" data-value-field="dict"/>
        </k-form-item>

        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addBaseExMapModelPopup"  slot="button">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="BaseExMapModelGrid" @data-row-select="selectRow" data-action="BaseExMapModel.findBaseExMapModels" data-fixed="right">
        <k-grid-column data-header="id" data-name="id" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="来源系统" data-name="moduleid" data-dict="interface_type"></k-grid-column>
        <k-grid-column data-header="本地字典描述" data-name="dictname"></k-grid-column>
        <k-grid-column data-header="本地字典名" data-name="dict"></k-grid-column>
        <k-grid-column data-header="本地字典KEY" data-name="itemkey"></k-grid-column>
        <k-grid-column data-header="本地字典VAL" data-name="itemval"></k-grid-column>
        <k-grid-column data-header="来源字典KEY" data-name="outValue"></k-grid-column>
        <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改字典映射配置" data-functype="POPUP" data-size="mini"
                 data-target="editBaseExMapModelPopup">
            修改
          </k-btn>
          <k-btn class="md-danger" data-functype="SUBMIT" data-action="BaseExMapModel.deleteBaseExMapModel"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="BaseExMapModelGrid" data-descript="删除字典映射配置">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加接口sql配置弹出框   -->
    <k-popup ref="addBaseExMapModelPopup" data-title="新增">
      <k-form ref="addBaseExMapModelForm" :data-col="2">
        <k-form-item label="来源系统">
          <k-field-select v-model="formData.moduleid" data-dict="interface_type" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="来源字典KEY">
          <k-field-text v-model="formData.outValue" :data-allowblank="false" />
        </k-form-item>

        <k-form-item label="本地字典名称" >

          <k-field-select v-model="formData.dict" data-action="BaseExMapModel.findDictByNm" :dataRemote="true"  :data-allowblank="false"  @data-on-change="findSysDictItemInfo"
                          data-display-field="dict,dictname" data-value-field="dict"/>
        </k-form-item>
        <k-form-item label="本地字典KEY">
          <k-field-select v-model="formData.itemkey"
                          :data-data="formData.itemkeyList"
                          data-display-field="itemkey,itemval" data-value-field="itemkey"
                          :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseExMapModel.addBaseExMapModel" data-from="addBaseExMapModelForm"
                 :data-model="formData" data-target="BaseExMapModelGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改接口sql配置弹出框   -->
    <k-popup ref="editBaseExMapModelPopup" data-title="修改">
      <k-form ref="editBaseExMapModelForm" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text  v-model="formData.id" />
        </k-form-item>
        <k-form-item label="来源系统">
          <k-field-select v-model="formData.moduleid" data-dict="interface_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="来源字典KEY">
          <k-field-text v-model="formData.outValue" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="本地字典名称">
          <k-field-text v-model="formData.dictname" :data-allowblank="false" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="本地字典标识">
          <k-field-select v-model="formData.dict" data-action="BaseExMapModel.findDictByNm" :dataRemote="true"  :data-allowblank="false"  @data-on-change="findSysDictItemInfo"
                          data-display-field="dict,dictname" data-value-field="dict"/>
        </k-form-item>
        <k-form-item label="本地字典KEY">
          <k-field-select v-model="formData.itemkey"
                          :data-data="formData.itemkeyList"  @data-on-change="findSysDictName"  @data-on-focus="findSysDictItemInfo"
                          data-display-field="itemkey,itemval" data-value-field="itemkey"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="本地字典VAL">
          <k-field-text v-model="formData.itemval"  :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseExMapModel.updateBaseExMapModel" data-from="editBaseExMapModelForm"
                 :data-model="formData" data-target="BaseExMapModelGrid">
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
  name:"BaseExMapModel",
  data() {
    return {
      formData: {
        id:'',
        moduleid:'',
        dictname:'',
        dict:'',
        itemkey:'',
        itemval:'',
        outValue:'',
        remark:'',
        itemkeyList:{},
      },
      selectRowData: {},
      searchParam:{},
      bool : false,
    };
  },
  methods: {
    findSysDictItemInfo() {
      this.httpUtil.comnQuery({
        action: "BaseExMapModel.findSysDictItemInfo",
        params: {dict: this.formData.dict},
      }).then(data => {
        this.$set(this.formData,"itemkey",'');
        this.$set(this.formData,"itemkeyList",'');
        this.$set(this.formData,"itemkeyList",data.rows);
      });
    },
    findSysDictName() {
      this.httpUtil.comnQuery({
        action: "BaseExMapModel.findSysDictName",
        params: {dict: this.formData.dict,itemkey:this.formData.itemkey},
      }).then(data => {
        this.$set(this.formData,"dictname",data.rows[0].dictname);
        this.$set(this.formData,"itemval",data.rows[0].itemval);
      });
    },

    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    }
  }
};
</script>
