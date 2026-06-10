<template>
  <div style="background-color: white;padding: 50px 20px;">
    <el-timeline>
      <el-timeline-item
        v-for="(logData, index) in logDatas"
        :key="index"
        size="large"
        :icon="operationTypeDict[logData.operationType].icon"
        :color="operationTypeDict[logData.operationType].color"
        :timestamp="logData.createDate" placement="top" hide-timestamp>
        <div :style="{color: operationTypeDict[logData.operationType].color, fontSize: '20px'}">{{logData.displayName}}</div>
        <div style="font-size: 16px;margin-top:5px;">
          <template v-if="logData.operationType==9">
            <span :style="{color: operationTypeDict[logData.operationType].color}">完成</span>
          </template>
          <template v-else-if="logData.operationType==8">
            <span style="margin-right: 10px;font-weight: bold"><i class="el-icon-user-solid"/>{{logData.username}}</span><span :style="{color: operationTypeDict[logData.operationType].color}">待操作</span>
          </template>
          <template v-else>
            <span style="color: #606266;margin-right: 10px;"><i class="el-icon-user-solid"/>{{logData.operator}}</span>
            <span :style="{color: operationTypeDict[logData.operationType].color}">({{operationTypeDict[logData.operationType].label}})</span>
            <span style="margin: 0 10px;">于</span>
            <span style="color: #909399;">{{logData.createDate}}</span>
            <br>
            <span style="color: #909399;font-size: 14px;">{{logData.deptname}}</span>
          </template>
        </div>
        <el-divider></el-divider>
        <!-- 功能表单 -->
        <div v-if="logData.operationType<'4'">
          <template v-for="item in logData.forms">
            <div class="form-title">
              <div class="color-block"></div>
              <div>{{item.formName}}</div>
            </div>
            <template v-if="item.formType=='0'">
              <!-- 参数表单，用k-form展示 -->
              <k-form :ref="item.formId" :key="item.formId" :data-col="2">
                <template v-for="(param, paramIndex) in item.params">
                  <k-form-item :label="param.paramName" :key="param.paramCode" :data-col="2">
                    <component v-model="logData.formData[item.formId][param.paramCode]" v-bind="inputOptions(param)" :is="funcTypeMap[param.funcType]"></component>
                  </k-form-item>
                </template>
              </k-form>
            </template>

            <!-- 组件类型表单 -->
            <template v-else-if="item.formType=='1'">
              <component :ref="item.formId" v-if="item.compPath" :is="item.compPath" disabled></component>
            </template>
          </template>
        </div>
        <!-- 审批意见 -->
        <div v-else-if="logData.operationType<'6'">
          <el-card class="box-card">
            <div class="card-title">审批详情</div>
            <div class="card-content" v-for="(item,index) in logData.approvalFlows" v-if="item.nodeLevel&&item.nodeLevel!=='999'">
              <div style="margin-top: 20px;">
                <span style="color: #606266;margin-right: 10px;margin-top: 10px"><i class="el-icon-user-solid"/>{{item.operator}}</span>
                <span style="margin: 0 10px;">于</span>
                <span style="color: #909399;">{{formatDateTimeStr(item.dateTime)}}</span>
                <span style="color: #0202ff;font-weight: bold;margin: 0 10px;">{{item.result}}</span>
                <span style="font-weight: bold;">审批意见：</span>
                <span style="color: #909399;">{{item.opinion}}</span>
              </div>
              <div>
                <span style="color: #909399;font-size: 14px;">{{item.nodeName}}</span>
              </div>
              <el-divider></el-divider>
            </div>
          </el-card>
        </div>
        <div v-if="logData.operationType=='10'" key="transfer">
<!--          <el-card class="box-card">-->
<!--            <div class="card-title">转交信息</div>-->
<!--          </el-card>-->
          <span style="color: #8389FF;font-weight: bold;">{{logData.operator}} {{logData.remark}}</span>
        </div>
      </el-timeline-item>
    </el-timeline>
  </div>

</template>

<script>
  import Tools from '@/utils/tools.js';

  export default {
    name: "flowHistoryDetail",
    data() {
      return {
        logDatas: [],
        operationTypeDict: {
          0: {
            label: '保存',
            icon: 'el-icon-check',
            color: '#409EFF'
          },
          1: {
            label: '提交',
            icon: 'el-icon-check',
            color: '#409EFF'
          },
          2: {
            label: '终止',
            icon: 'el-icon-switch-button',
            color: '#F56C6C'
          },
          3: {
            label: '回退',
            icon: 'el-icon-refresh-left',
            color: '#E6A23C'
          },
          4: {
            label: '审批通过',
            icon: 'el-icon-check',
            color: '#67C23A'
          },
          5: {
            label: '审批拒绝',
            icon: 'el-icon-close',
            color: '#F56C6C'
          },
          8: {
            label: '待操作',
            icon: 'el-icon-time',
            color: '#909399'
          },
          9: {
            label: '完成',
            icon: 'el-icon-finished',
            color: '#8340FF'
          },
          10:{
            label: '转交',
            icon: 'el-icon-d-arrow-right',
            color: '#8389FF'
          }
        },
        // 输入类型映射组件
        funcTypeMap: {
          text: 'k-field-text',
          number: 'k-field-text',
          int: 'k-field-text',
          select: 'k-field-select',
          radio: 'k-field-radio',
          checkbox: 'k-field-checkbox',
          date: 'k-field-date',
          time: 'k-field-time',
          tree: 'k-field-tree',
          multiple: 'k-field-select',
          textarea: 'k-field-text'
        },
        timelineStyle: {}
      };
    },
    components: {
      Grid(resolve) {
        require(["@/pages/operation/components/flow_test.vue"], resolve);
      },
    },
    props:{
      processInstanceId:'',
    },
    computed: {
      inputOptions: () => {
        return (param) => {
          let options = {};
          options.ref = param.formId + param.paramCode;
          // 是否可空
          options['data-allowblank'] = param.blankFlag=='0';
          // 是否可编辑
          options['data-disabled'] = true;
          if (param.funcType=='textarea') {
            options['input-type'] = 'textarea';
          }
          // 长度
          if (param.fieldLength) {
            options['data-max-length'] = parseInt(param.fieldLength);
          }
          // 校验类型、精度
          if (param.funcType=='int') {
            options['data-validate-type'] = 'int';
          } else if (param.funcType=='number') {
            options['data-validate-type'] = 'number';
            if (param.fieldPrecision) {
              options['data-digits'] = parseFloat(param.fieldPrecision);
            }
          }
          // 数据字典
          if (param.dataWay=='2') {
              options['data-action'] = param.dict;
          } else {
            options['data-dict'] = param.dict;
          }
          // 最小最大值
          if (param.minValue || param.minValue==0) {
            options['data-min-value'] = param.minValue;
          }
          if (param.maxValue || param.maxValue==0) {
            options['data-max-value'] = param.maxValue;
          }
          // 空白提示
          options.dataPlaceholder = param.placeholder;
          if (param.funcType=='multiple') {
            options.dataFuncType = true;
          }
          return options;
        }
      }
    },
    created() {
      let processInstanceId = this.$route.query.processInstanceId;
      let prodCode = this.$route.query.prodCode;
      if (!processInstanceId){
        processInstanceId = this.processInstanceId;
      }
      if (processInstanceId) {
        this.httpUtil.comnQuery({
          action: 'OpfLog.findLogs',
          params: {
            processInstanceId: processInstanceId,
            prodCode:prodCode,
          }
        }).then(res => {
          this.logDatas = res.rows;
          // 查询功能表单、提交的表单数据，用于展示
          this.logDatas.forEach(logData => {
            this.$set(logData, 'forms', [])
            logData.formData = {};
            if (logData.operationType<'4' && logData.busiId) {
              // 根据功能查询表单列表
              this.httpUtil.comnQuery({
                action: 'OpBusiForm.find',
                params: {
                  busiId: logData.busiId
                }
              }).then(res => {
                // 查询表单详情
                this.httpUtil.comnQuery({
                  action: 'OpFormInfo.findOpFormParam',
                  params: {
                    formIdList: JSON.stringify(res.rows.map(row => row.formId))
                  }
                }).then(res1 => {
                  logData.forms.push(...res1.rows);
                });
              }).then(res => {
                // 查询任务关联的表单数据
                logData.taskId && this.httpUtil.comnQuery({
                  action: 'SubmitParams.getSubmitParamsByTask',
                  params: {
                    taskId: logData.taskId
                  }
                }).then(res => {
                  if (res.data) {
                    Object.assign(logData.formData, JSON.parse(res.data.submitParams));
                  }
                  this.$forceUpdate();
                })
              })
            }
            this.$set(logData, 'approvalFlows', [])
            if (logData.operationType<'6' && logData.remark){
              this.httpUtil.ajax({
                  url: "/wf/approvalTask/listAllApprovalTasks.json",
                  params: {
                    processId: logData.remark
                  }
                }).then(res => {
                  logData.approvalFlows.push(...res.data);
                });
            }
          })

        })
      }
    },
    methods: {
      formatDateTimeStr (datetime) {
        if (!datetime) {
          return "";
        }
        let result = datetime.substring(0, 4) + "-" + datetime.substring(4, 6) + "-" + datetime.substring(6, 8) + " "+datetime.substring(9, 11) + ":" + datetime.substring(11, 13) + ":" + datetime.substring(13, 15);
        return result;
      }
    }
  };
</script>

<style lang="scss" scoped>
.form-title {
  display: flex;
  align-items: center;
  padding: 10px 0;
  font-weight: 600;
}
.color-block {
  width: 5px;
  height: 15px;
  background-color: #ff9400;
  margin: 0 10px;
}
.el-divider--horizontal {
  margin: 10px 0;
}

/deep/ .el-timeline-item__node--large {
    left: -15px;
    width: 30px;
    height: 30px;
    i {
      font-size: 16px;
    }
}
/deep/ .el-timeline-item__tail {
  left: -2px;
}
.box-card {
  margin-top: 30px;
  width: 60%;
  .card-title {
    position: absolute;
    top: 90px;
    left: 40px;
    background: white;
    padding: 7px;
    font-size: 18px;
  }
  .card-content {
    margin-left: 20px;
    font-size: 16px;
    margin-top: 5px;
    color: #606266;
  }
}
</style>
