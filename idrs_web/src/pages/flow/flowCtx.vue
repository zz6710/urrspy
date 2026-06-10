<template>
  <div>
    <k-form-search-customize data-target="grid" v-model="queryParam">
      <k-form-item label="英文名">
        <k-field-text v-model="queryParam.name"></k-field-text>
      </k-form-item>
      <k-form-item label="中文名">
        <k-field-text v-model="queryParam.displayName"></k-field-text>
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" :data-handler="()=>this.formData={}"
             data-target="addPopup">
        <md-icon md-src="/static/svg/add.svg"/>
        新增
      </k-btn>
    </k-form-search-customize>

    <k-grid
      ref="grid"
      @data-row-select="selectRow"
      data-url='/wf/ctx/list.json'>
      <k-grid-column data-align="center"
                     data-header="英文名"
                     data-name="name"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="中文名"
                     data-name="displayName"
      ></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="编辑" data-functype="POPUP" data-size="mini"
               data-target="editPopup" :data-handler="queryDetail">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
               data-url="/wf/ctx/remove.json" data-size="mini" data-type="danger" data-target="grid"
               :data-confirm="true" data-descript="删除">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" data-title="字段配置" @data-close="()=>this.fields=[]">
      <k-form ref="form1" data-total-width="988px">
        <k-form-item label="表单字段英文名">
          <k-field-text v-model="formData.name" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="表单字段中文名">
          <k-field-text v-model="formData.displayName" :data-allowblank="false"></k-field-text>
        </k-form-item>
      </k-form>
      <k-form ref="form2" v-for="(item,index) in fields" :key="index" :data-col="5" data-input-width="150px"
              data-label-width="62px" data-total-width="1018px">
        <k-form-item label="name">
          <k-field-text v-model="item.name" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="显示值">
          <k-field-text v-model="item.displayName" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="类型">
          <k-field-select v-model="item.fieldType" :data-data="filedTypeOptions"
                          :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="数据字典">
          <k-field-text v-model="item['data-dict']"></k-field-text>
        </k-form-item>
        <k-btn class="md-success md-just-icon md-simple" style="top: 13px;" data-descript="删除"
               :data-handler="()=>fields.splice(index,1)">
          <md-icon md-src="/static/svg/delete.svg"/>
        </k-btn>
      </k-form>
      <k-btn class="btn-custom-primary" :data-handler="()=>fields.push({})">
        <md-icon md-src="/static/svg/add.svg"/>
        添加
      </k-btn>
      <k-btn class="md-danger" :data-handler="()=>fields.splice(0, fields.length)">
        <md-icon md-src="/static/svg/delete_white.svg"/>
        清空
      </k-btn>
      <el-divider></el-divider>

      <div style="margin: 0 auto;width: 255px;">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-model="formData" data-url="/wf/ctx/save.json"
               :data-handler="submitHandle" :data-after-success="addSuccess"
               data-target="grid">
          <md-icon md-src="/static/svg/confirm.svg"/>
          保存
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"/>
          取消
        </k-btn>
      </div>
    </k-popup>

    <k-popup ref="editPopup" data-title="字段配置" @data-close="()=>this.fields=[]">
      <k-form ref="form1" data-total-width="988px">
        <k-form-item label="表单字段英文名">
          <k-field-text v-model="formData.name" :data-disabled="true" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="表单字段中文名">
          <k-field-text v-model="formData.displayName" :data-disabled="true" :data-allowblank="false"></k-field-text>
        </k-form-item>
      </k-form>
      <k-form ref="form2" v-for="(item,index) in fields" :key="index" :data-col="5" data-input-width="150px"
              data-label-width="62px" data-total-width="1018px">
        <k-form-item label="name">
          <k-field-text v-model="item.name" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="显示值">
          <k-field-text v-model="item.displayName" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="类型">
          <k-field-select v-model="item.fieldType" :data-data="filedTypeOptions"
                          :data-allowblank="false"></k-field-select>
        </k-form-item>
        <k-form-item label="数据字典">
          <k-field-text v-model="item['data-dict']"></k-field-text>
        </k-form-item>
        <k-btn class="md-success md-just-icon md-simple" style="top: 13px;" data-descript="删除"
               :data-handler="()=>fields.splice(index,1)">
          <md-icon md-src="/static/svg/delete.svg"/>
        </k-btn>
      </k-form>
      <k-btn class="btn-custom-primary" :data-handler="()=>fields.push({})">
        <md-icon md-src="/static/svg/add.svg"/>
        添加
      </k-btn>
      <k-btn class="md-danger" :data-handler="()=>fields.splice(0, fields.length)">
        <md-icon md-src="/static/svg/delete_white.svg"/>
        清空
      </k-btn>
      <el-divider></el-divider>

      <div style="margin: 0 auto;width: 255px;">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-model="formData" data-url="/wf/ctx/update.json"
               :data-handler="submitHandle" :data-after-success="editSuccess"
               data-target="grid">
          <md-icon md-src="/static/svg/confirm.svg"/>
          保存
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"/>
          取消
        </k-btn>
      </div>
    </k-popup>
  </div>

</template>

<script>

  import {assign} from "lodash";

  export default {
    name: "flowCtx",
    data() {
      return {
        queryParam: {
          name: '',
          displayName: ''
        },
        formData: {},
        selectRowData: {},
        fields: [],
        editInputWidth: '100px',
        filedTypeOptions: [
          {
            "label": "text",
            "value": "k-field-text"
          },
          {
            "label": "select",
            "value": "k-field-select"
          },
          {
            "label": "time",
            "value": "k-field-time"
          },
          {
            "label": "date",
            "value": "k-field-date"
          }
        ],
        disabledOptions: [
          {
            "label": "是",
            "value": true
          },
          {
            "label": "否",
            "value": false
          }
        ]
      };
    },
    computed: {},
    methods: {
      editSuccess() {
        this.$refs.editPopup.close();
      },
      addSuccess() {
        this.$refs.addPopup.close();
      },
      queryDetail() {
        setTimeout(() => {
          this.httpUtil
            .ajax({
              url: "/wf/ctx/get.json",
              params: {"id": this.selectRowData.id}
            })
            .then(res => {
              let data = res.data;
              this.formData = data;
              if (data.json) {
                this.fields = JSON.parse(data.json).fieldsConf;
              }
            });
        }, 50);
      },
      submitHandle(value) {
        let result = true;
        result = this.$refs.form1.validate();

        let form2s = this.$refs.form2;
        if (form2s && form2s.length > 0) {
          for (let i = 0; i < form2s.length; i++) {
            result = result && form2s[i].validate();
          }
        }

        if (result === false) {
          return false;
        }

        if (this.fields && this.fields.length > 0) {
          value.json = JSON.stringify({"fieldsConf": this.fields})
        }
      },
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row)
        if (this.formData && this.formData.json) {
          this.formData.json = JSON.parse(this.formData.json)
        }
      }
    }
  };
</script>

<style scoped>
  /deep/ .k-form-body {
    height: 63px;
  }
  /deep/ .el-dialog {margin-right: 10%}
</style>
