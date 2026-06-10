<template>
  <div class="py-page">
    <k-form-search-customize data-target="grid" v-model="queryParam">
      <k-form-item label="流程名">
        <k-field-select v-model="queryParamProcessName" data-display-field="text" data-value-field="id"
                        data-url="/wf/process/select2/allProcessNames.json"/>
      </k-form-item>
      <k-form-item label="状态">
        <k-field-select v-model="queryParamStatus"
                        data-dict="surrogate_status" />
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" :data-handler="()=>this.formData={'status': '1'}"
             data-target="addPopup">
        <md-icon md-src="/static/svg/add.svg"/>
        新增
      </k-btn>
    </k-form-search-customize>
    <div class="py-page-container">
    <k-grid ref="grid" @data-row-select="selectRow" data-url='/wf/surrogate/list.json'>
      <k-grid-column data-header="流程名" data-name="processDisplayName"></k-grid-column>
      <k-grid-column data-header="开始时间" data-name="startDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="结束时间" data-name="endDate" data-type="date"></k-grid-column>
      <k-grid-column data-header="授权人" data-name="creator"></k-grid-column>
      <k-grid-column data-header="代理人" data-name="surrogate"></k-grid-column>
      <k-grid-column data-header="状态" data-name="status" data-dict="surrogate_status"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="编辑" data-functype="POPUP" data-size="mini"
               data-target="editPopup">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
               data-url="/wf/surrogate/remove.json" data-size="mini" data-type="danger" data-target="grid"
               :data-confirm="true" data-descript="删除">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>
    </div>

    <k-popup ref="editPopup" data-title="编辑转审批" @data-opened="changeUsernameToUserId()">
      <k-form ref="editForm" :data-col="1">
        <k-form-item label="流程名">
          <k-field-select v-model="formData.processName" ref="processSelect"
                          data-url="/wf/process/listAllProcess.json"
                          data-params="{start:0,limit: 10000000}"
                          data-display-field="displayName"
                          data-value-field="name"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="代理人">
          <k-field-select v-model="formData.surrogate" ref="surrogateSelect" data-action="User.findUsersWithQY"
                          data-display-field="username"
                          data-value-field="userid"
                          :data-params='{"userstatus" : "N"}'
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="开始时间">
          <k-field-date v-model="formData.startDate" :data-allowblank="false" :data-max-value="formData.endDate"/>
        </k-form-item>
        <k-form-item label="结束时间">
          <k-field-date v-model="formData.endDate" :data-allowblank="false" :data-min-value="formData.startDate"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select data-dict="surrogate_status" v-model="formData.status"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-url="/wf/surrogate/update.json" data-target="grid"
                 data-from="addForm" :data-model="formData"  :data-handler="submitDataHandler">
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

    <k-popup ref="addPopup" data-title="新增转审批">
      <k-form ref="addForm" :data-col="1">
        <k-form-item label="流程名">
          <k-field-select v-model="formData.processName" ref="processSelect"
                          data-url="/wf/process/listAllProcess.json"
                          data-params="{start:0,limit: 10000000}"
                          data-display-field="displayName"
                          data-value-field="name"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="代理人">
          <k-field-select v-model="formData.surrogate" ref="surrogateSelect" data-action="User.findUsersWithQY"
                          data-display-field="username"
                          data-value-field="userid"
                          :data-params='{"userstatus" : "N"}'
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="开始时间">
          <k-field-date v-model="formData.startDate" :data-allowblank="false" :data-max-value="formData.endDate"/>
        </k-form-item>
        <k-form-item label="结束时间">
          <k-field-date v-model="formData.endDate" :data-allowblank="false" :data-min-value="formData.startDate"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select data-dict="surrogate_status" v-model="formData.status" :data-disabled="true"
                          :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-url="/wf/surrogate/save.json" data-target="grid"
                 data-from="addForm" :data-model="formData" :data-handler="submitDataHandler">
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
    name: "surrogate",
    data() {
      return {
        formData: {},
        envItems: [],
        selectRowData: {},
        queryParamProcessName: '',
        queryParamStatus: '',
      };
    },
    computed: {
      editItemFormData() {
        return {
          "items": JSON.stringify(this.envItems)
        }
      },
      queryParam() {
        return {
          'processName': this.queryParamProcessName,
          'status': this.queryParamStatus,
        }
      }
    },
    methods: {
      changeUsernameToUserId() {
        this.httpUtil
          .ajax({
            url: "/wf/surrogate/getUserIdByUser.json",
            params: {"userName": this.selectRowData.surrogate}
          })
          .then(data => {
            this.formData.surrogate = data.data
          });
      },
      submitDataHandler(value) {
        let processRows = this.$refs.processSelect.rows;
        if (processRows && processRows.length > 0) {
          for (let index in processRows) {
            if (processRows[index].id == value.processName) {
              value.processName_displaylable = processRows[index].text;
              break;
            }
          }
        }
      },
      findItem() {
        this.envItems = [];
        setTimeout(() => {
          this.httpUtil
            .ajax({
              url: "/wf/env/listEnvItemById.json",
              params: {"id": this.selectRowData.id}
            })
            .then(data => {
              this.envItems = data.data
              if (!this.envItems) {
                this.envItems = []
              }
              if (this.envItems.length == 0) {
                this.envItems.push({"id": this.selectRowData.id})
              }
            });
        }, 50)
      },
      addItemRow(index) {
        this.envItems.splice(index + 1, 0, {"id": this.selectRowData.id})
      },
      delIemRow(index) {
        this.envItems.splice(index, 1)
      },
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      }
    }
  };
</script>

<style>
</style>
