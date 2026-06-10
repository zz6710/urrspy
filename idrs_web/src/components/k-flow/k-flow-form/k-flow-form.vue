<template>
  <div style="text-align: center">
    <k-form ref="approvalForm" :data-col="4">
      <k-form-item label="审批意见: " :data-col="3">
        <k-field-text v-model="formData.opinion" inputType="textarea" :rows="3" :dataMaxLength="500"/>
      </k-form-item>
      <!-- <k-form-item v-for="(item, index) in formFieldConfs" :key="index" :label="item.displayName">
        <component
          :is="item.fieldType"
          v-model="item.value"
          :data-allowblank="false"
          :data-dict="item['data-dict']"
        ></component>
      </k-form-item> -->
      <k-form-footer></k-form-footer>
    </k-form>
    <k-form ref="rejectForm" :data-col="1">
      <k-form-item label="驳回节点" v-if="btns.indexOf('2')>-1">
        <k-field-select
          data-url="/wf/conf/queryRejectTaskSelect.json"
          v-model="formData.taskName"
          :data-allowblank="false"
          :data-params='{"processId": taskInfo.processId, "taskName": this.taskInfo.taskName}'
          data-display-field="text"
          data-value-field="id"
        ></k-field-select>
      </k-form-item>
      <k-form-footer></k-form-footer>
    </k-form>
    <k-btn
      class="btn-custom-primary"
      data-functype="SUBMIT"
      v-if="btns.indexOf('3')>-1"
      data-url="/wf/approval/doApproval.json"
      :data-handler="passDataHandler"
      :data-after-success="passDataSuccess"
      data-target="taskGrid"
      :data-model="formData"
      data-from="approvalForm"
      data-params='{"result":"5","isSurrogate": "true"}'
    >
      <md-icon md-src="/static/svg/confirm.svg"></md-icon>通过
    </k-btn>
    <k-btn
      class="md-danger"
      data-functype="SUBMIT"
      v-if="btns.indexOf('1')>-1"
      data-url="/wf/approval/doApproval.json"
      :data-after-success="passDataSuccess"
      :data-validate-form="false"
      data-target="taskGrid"
      :data-model="formData"
      data-from="approvalForm"
      data-params='{"result":"3","isSurrogate": "true"}'
    >
      <md-icon md-src="/static/svg/cancel.svg"></md-icon>拒绝
    </k-btn>
    <k-btn
      class="md-warning"
      data-functype="SUBMIT"
      v-if="btns.indexOf('2')>-1"
      data-url="/wf/approval/doApproval.json"
      :data-after-success="passDataSuccess"
      data-target="taskGrid"
      :data-model="formData"
      data-from="rejectForm"
      data-params='{"result":"2","isSurrogate": "true"}'
    >
      <md-icon md-src="/static/svg/block_white.svg" />驳回
    </k-btn>
  </div>
</template>

<script>
  import Tools from "@/utils/tools.js";

  export default {
    name: 'KFlowForm',
    props: {
      taskInfo: {},
    },
    data() {
      return {
        formData: {},
        btns: '',
        enableAttachment: false,
        attachmentIds: '',
        taskFormComponentName: null,
        formFieldConfs: [],
        resultDict: {
          "1": "开始",
          "2": "驳回",
          "3": "拒绝",
          "4": "驳回到开始申请节点",
          "5": "通过",
          "6": "重新提交申请",
          "7": "审批中",
          "9": "完成"
        }
      };
    },
    created() {
      this.formData.processInstanceId = this.taskInfo.processInstanceId;
      this.formData.processId = this.taskInfo.processId;
      this.httpUtil
        .ajax({
          url: "/wf/wf/attachment/enableAttachment.json",
          params: {"processId": this.taskInfo.processId}
        })
        .then(res => {
          this.enableAttachment = res.data == 1 ? true : false;
        });

      this.httpUtil
        .ajax({
          url: "/wf/conf/getTaskBtns.json",
          params: {"processId": this.taskInfo.processId}
        })
        .then(res => {
          if (res && res.data && res.data.length > 0) {
            let data = res.data;
            for (let i = 0; i < data.length; i++) {
              if (this.taskInfo.taskName == data[i].taskName) {
                let btns = data[i].btns;
                if (btns) {
                  this.btns = btns;
                }
                break;
              }
            }
          }
        });

      this.httpUtil
        .ajax({
          url: "/wf/conf/queryOptionFormConfig.json",
          params: {"processId": this.taskInfo.processId, "taskName": this.taskInfo.taskName}
        })
        .then(res => {
          let data = res.data;
          if (data) {
            data = JSON.parse(data);
            if (data.fieldsConf && data.fieldsConf.length > 0) {
              this.formFieldConfs = data.fieldsConf
            }
          }
        });

      if (this.taskInfo) {
        this.formData.taskId = this.taskInfo.taskId
      }
    },
    methods: {
      passDataHandler(value) {
        if (this.formFieldConfs && this.formFieldConfs.length > 0) {
          let extraData = []
          this.formFieldConfs.forEach(o => {
            let data = {}
            data.name = o.name;
            data.value = o.value;
            data.displayName = o.displayName;
            extraData.push(data)
          })

          value.extraData = JSON.stringify(extraData);
        }
        if (this.enableAttachment) {
          value.attachmentIds = this.attachmentIds;
        }
      },

      passDataSuccess(){
        this.$emit('submitClose', '1')
      }
    },
    mounted(){
      this.bus.$on('change',(msg) => {
        this.attachmentIds = msg
      })
    }
  }
</script>
