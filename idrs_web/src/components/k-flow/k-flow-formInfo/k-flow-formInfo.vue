<template>
  <div>
    <h4>表单信息</h4>
    <!-- 自定义表单组件 -->
    <component v-if="taskFormComponentName" :is="taskFormComponentName"
               :task-info="taskInfo" :submit-data="submitData"/>
    <!-- 界面配置表单 -->
    <k-form v-else-if="taskFormId">
			<k-form-item v-for="(item, index) in formFieldList" :key="index" :label="item.displayName">
				<component
					:is="item.fieldType"
					v-model="item.value"
					:data-allowblank="false"
					:data-dict="item['data-dict']"
          :data-disabled="true"
				></component>
			</k-form-item>
			<k-form-footer></k-form-footer>
		</k-form>
    <!-- 表单提交字段 -->
    <k-form v-else>
      <k-form-item v-for="(item,index) in businessInfo" :key="index" :label="item.label">
        <k-field-display v-model="item.value" :data-dict="item.dict"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  export default {
    name: "KFlowFormInfo",
    props: {
      taskInfo: {}
    },
    data() {
      return {
        taskFormComponentName: null,
        taskFormId: '',
        businessInfo: [],
        submitData: {},
        formFieldList: null,
      };
    },
    methods: {
      initFormLabelInfo() {
        const latestData = this.submitData;
        this.httpUtil
          .ajax({
            url: "/wf/processInstance/label/info.json",
            params: {processInstanceId: this.taskInfo.processInstanceId}
          })
          .then(res => {
            let labelInfoData = res.data;
            let businessInfo = [];
            for (let field in latestData) {
              if(labelInfoData){
                let info = labelInfoData[field];
                if (!info || !info.label) {
                  continue;
                }
                info.value = latestData[field];
                businessInfo.push(info);
              }
            }
            this.businessInfo = businessInfo;
          });
      },

      initFormFieldInfo(){
        let latestData = this.submitData;
        let formFieldList = [];
        // 设置了表单
        this.httpUtil
          .ajax({
            url: "/wf/conf/queryCtxById.json",
            params: {"formId": this.taskFormId}
          })
          .then(res => {
            let data = res.data;
            if (data) {
              data = JSON.parse(data);
              if (data.fieldsConf && data.fieldsConf.length > 0) {
                for(let index in data.fieldsConf){
                  let field = data.fieldsConf[index];
                  field.value = latestData[field.name];
                  formFieldList.push(field);
                }
                this.formFieldList = formFieldList;
              }
            }
          });
      }
    },
    created() {
      // 获取当前流程表单配置
      this.httpUtil
        .ajax({
          url: "/wf/conf/getFormConf.json",
          params: {processId: this.taskInfo.processId}
        })
        .then(res => {
          let formConfData = res.data;
          if (formConfData && formConfData.length > 0) {
            // 默认取申请节点的表单组件，可以被覆盖
            this.taskFormComponentName = formConfData[0].formUrl; // 申请节点表单组件
            this.taskFormId = formConfData[0].formId;           // 申请节点表单配置

            // 任务节点的表单优先级高于申请节点
            // 当没有taskName时，说明审批已经结束，取申请节点的表单url配置
            console.log("   this.taskInfo : ", this.taskInfo);
            if (this.taskInfo && this.taskInfo.taskName) {
              for (let i = 1; i < formConfData.length; i++) {
                console.log(" ### i : ", formConfData[i]);
                if(formConfData[i].taskName == this.taskInfo.taskName){
                  if(formConfData[i].formUrl){
                    this.taskFormComponentName = formConfData[i].formUrl;
                  }
                  if(formConfData[i].formId){
                    this.taskFormId = formConfData[i].formId;
                  }
                }
              }
            }

            // 获取表单数据
            this.httpUtil
              .ajax({
                url: "/wf/processInstance/data/latest.json",
                params: {processInstanceId: this.taskInfo.processInstanceId}
              })
              .then(res => {
                this.submitData = res.data;
                if(this.taskFormComponentName){ // 表单组件
                  console.log(" 使用表单组件");
                }else if (this.taskFormId){     // 表单
                  this.initFormFieldInfo();     // 设置了表单
                }else {                         // 使用管理台默认字段（后台交易没有数据）
                  this.initFormLabelInfo();     // 未设置表单
                }
              });
          }
        });
    }
  };
</script>
