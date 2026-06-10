<template>
  <div>
    <div :style="dataDirection == 'horizontal' ? (stepsWidth ? {'width' : stepsWidth} : '') : ''">
      <el-steps :active="active" ref="steps" :direction="dataDirection">
        <el-step v-for="(step, index) in steps" :key="index"
                 :title="step.dataTitle" :description="step.dataDescription"
                  :icon="step.dataIcon"/>
      </el-steps>
    </div>
    <div class="stepsFormDiv" :style="stepsWidth ? {'width' : stepsWidth} : ''">
      <k-form ref="stepForm" style="margin: 0 auto;" :data-col="dataCol">
        <slot></slot>
      </k-form>
      <div :style="{'text-align': dataButtonAlign}">
        <k-btn class="btn-custom-primary" v-show="!(active == 1)" :data-handler="pre">上一步</k-btn>
        <k-btn class="btn-custom-primary" v-show="(dataNextShow === true || dataNextShow === 'true') && !(steps.length == active)" :data-handler="next">下一步</k-btn>
        <k-btn class="btn-custom-primary" v-show="(dataSubmitShow === true || dataSubmitShow === 'true') && steps.length == active" data-from="stepForm" data-functype="SUBMIT"
                :data-action="dataAction" :data-graphql="dataGraphql" :data-descript="dataSubmitDescription"
                :data-model="dataModel"
                :data-confirm="dataConfirm === true || dataConfirm === 'true'">完成</k-btn>
      </div>
    </div>
  </div>
</template>

<script>
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";
  import Migrating from 'element-ui/src/mixins/migrating';

  export default {
    name: 'KSteps',
    mixins: [Migrating, props(), event(), emitter()],

    props: {
      dataActive: {
        type: Number
      },
      dataDirection: {
        type: String,
        default: 'horizontal'
      },
      dataNextShow: {
        type: [Boolean, String],
        default: true
      },
      dataSubmitShow: {
        type: [Boolean, String],
        default: true
      },
      dataButtonAlign: {
        type: String,
        default: 'center'
      },
      // 表单属性
      dataCol: {
        type: Number
      },
      // SUBMIT 按钮属性
      dataAction: {
        type: String
      },
      dataGraphql: {
        type: String
      },
      dataConfirm: {
        type: [Boolean, String]
      },
      dataSubmitDescription: {
        type: String
      },
      dataModel: {
        type: Object
      }
    },
    data() {
      return {
        active: this.dataActive,
        form: null,
        steps: [],
        stepsWidth: null
      };
    },
    mounted: function () {
      const forms = this.getChildrens('KForm');
      if (forms && forms.length > 0) {
        this.stepsWidth = parseInt(forms[0].formStyle.width.match(/[0-9]*/)[0]) + 50 + 'px'
      }

      this.changeStepShow()
    },
    methods: {
      next() {
        let step = this.getCurrentStep()
        let validate = true
        if (step) {
          let formItems = step.getChildrens('KFormItem');
          if (formItems && formItems.length > 0) {
            formItems.forEach(formItem => {
              validate = formItem.validate() && validate
            })
          }
        }

        if (validate) {
          if (this.active++ > this.$refs.steps.steps.length) {
            this.active = 1;
          }
          this.changeStepShow()
        }
      },
      pre() {
        if (this.active-- < 1) {
          this.active = 1;
        }
        this.changeStepShow()
      },
      getCurrentStep() {
        return this.steps[this.active - 1]
      },
      changeStepShow() {
        const steps = this.steps
        const showIndex = this.active - 1
        for (let i = 0; i < steps.length; i++) {
          if (i == showIndex) {
            steps[i].show = true
          } else {
            steps[i].show = false
          }
        }
      }
    },

    watch: {
      active(newVal, oldVal) {
        this.$emit('data-active-change', newVal, oldVal);
      }
    }
  };
</script>
<style>
  .stepsFormDiv {
    text-align: center;
    margin-top: 10px;
  }
</style>
