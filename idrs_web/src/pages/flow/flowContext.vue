<template>
  <div>
    <k-btn data-functype="POPUP" class="btn-custom-primary" :data-handler="()=>this.formData={}" data-target="addPopup">
      <md-icon md-src="/static/svg/add.svg"/>
      新增
    </k-btn>
    <k-grid
      ref="grid"
      @data-row-select="selectRow"
      data-url='/wf/env/list.json'>
      <k-grid-column data-align="center"
                     data-header="英文名"
                     data-name="name"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="中文名"
                     data-name="displayName"
      ></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="编辑上下文" data-functype="POPUP" data-size="mini"
               data-target="editPopup">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="编辑上下文项" data-functype="POPUP" data-size="mini"
               data-target="editItemPopup" :data-handler="findItem">
          <md-icon>format_align_left</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
               data-url="/wf/env/removeEnv.json" data-size="mini" data-type="danger" data-target="grid"
               :data-confirm="true" data-descript="删除">
          <md-icon>close</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="editPopup" data-title="编辑上下文">
      <k-form ref="editForm" :data-col="1">
        <k-form-item label="中文名">
          <k-field-text v-model="formData.displayName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-url="/wf/env/updateEnv.json" data-target="grid"
                 data-from="editForm" :data-model="formData">
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

    <k-popup ref="editItemPopup" class="edit_item" data-title="编辑上下文项" data-width="746px">
      <div>
        <div class="FlowContext__tips">
          <span class="FlowContext__tips__describe">用户id：</span>
          <span>userid</span>
          <span class="FlowContext__tips__describe">当前日期：</span>
          <span>current_date</span>
          <span class="FlowContext__tips__describe">流程实例id：</span>
          <span>process_instance_id</span>
          <span class="FlowContext__tips__describe">数据表：</span>
          <span>wf_form_data</span>
          <span class="FlowContext__tips__describe">key字段名：</span>
          <span>field_name</span>
          <span class="FlowContext__tips__describe">value字段名：</span>
          <span>field_value</span>
        </div>
      </div>
      <k-form ref="editItemForm" v-for="(item,index) in envItems" :key="index" :data-col="3" data-input-width="220px"
              data-label-width="62px" data-total-width="690px">
        <k-form-item label="键">
          <k-field-text v-model="item.key" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="值">
          <k-field-text v-model="item.value" :data-allowblank="false"/>
        </k-form-item>
        <div>
          <k-btn class="md-info md-just-icon md-simple" data-descript="添加" :data-handler="() => addItemRow(index)"
                 style="top: 13px;">
            <md-icon>add</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-descript="删除" :data-handler="() => delIemRow(index)"
                 style="top: 13px;">
            <md-icon md-src="/static/svg/delete.svg"/>
          </k-btn>
        </div>
      </k-form>
      <div style="margin: 0 auto;width: 255px;">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-url="/wf/env/updateEnvItem.json" data-target="grid"
               :data-model="editItemFormData" :data-handler="submitEditItemHandler">
          <md-icon md-src="/static/svg/confirm.svg"/>
          确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"/>
          取消
        </k-btn>
      </div>
    </k-popup>

    <k-popup ref="addPopup" data-title="新增上下文">
      <k-form ref="addForm" :data-col="1">
        <k-form-item label="英文名">
          <k-field-text v-model="formData.name" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="中文名">
          <k-field-text v-model="formData.displayName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-url="/wf/env/saveEnv.json" data-target="grid"
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
    name: "flowContext",
    data() {
      return {
        formData: {},
        envItems: [],
        selectRowData: {}
      };
    },
    computed: {
      editItemFormData() {
        return {
          "items": JSON.stringify(this.envItems)
        }
      }
    },
    methods: {
      submitEditItemHandler(value) {
        let form = this.$refs.editItemForm;
        let result = true;
        if (form && form.length > 0) {
          for (let i = 0; i < form.length; i++) {
            result = result && form[i].validate();
          }
        }

        if (result === false) {
          return false;
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

<style scoped>
  .edit_item /deep/ .k-form-body {
    height: 63px;
  }

  .FlowContext__tips {
    display: grid;
    grid-template-columns: 14% 18% 14% 18% 14% 18%;
  }

  .FlowContext__tips__describe {
    text-align: right;
  }
</style>
