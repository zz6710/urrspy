<template>
  <form class="el-form k-form" :class="[
    labelPosition ? 'el-form--label-' + labelPosition : '',
    { 'el-form--inline': inline === true || inline === 'true' }
  ]"
    :style="formStyle">
    <div class="k-form-body" :style="bodyStyle">
      <slot></slot>
    </div>
    <slot name="footer"></slot>
  </form>
</template>
<script>
  import emitter from "@/components/k-element/common/k-emitter.js";
  export default {
    name: 'KForm',

    mixins: [emitter()],

    provide() {
      return {
        kForm: this
      };
    },

    props: {
      labelPosition: String,
      dataLabelWidth: {
        type: String,
        default: "120px"
      },
      dataLabelPosition: {
        type: String,
        default: "horizontal"
      },
      dataInputWidth: {
        type: String,
        default: "225px"
      },
      dataItemMargin: {
        type: String,
        default: "5px"
      },
      dataCol: {
        type: Number,
        default: 2
      },
      inline: Boolean,
      dataUi: {
        type: String,
        default: "element"
      },
      dataTotalWidth: {
        type: String,
        default: null
      },
      isFormBodyScreen: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        formItems: []
      };
    },
    created() {
      this.$on('k.form.addFormItem', (field) => {
        if (field) {
          this.formItems.push(field);
        }
      });

      this.$on('k.form.removeFormItem', (field) => {
        if (field) {
          this.formItems.splice(this.formItems.indexOf(field), 1);
        }
      });
    },
    computed: {
      bodyStyle() {
        return this.isFormBodyScreen ? {overflowY: 'auto'} : {}
      },
      formStyle() {
        const ret = {};
        if(this.dataCol > 0){
          let dataLabelWidthInt = 0;
          if (this.dataTotalWidth) {
            dataLabelWidthInt = parseInt(this.dataTotalWidth.replace("px", ""));
          } else {
            let itemWidth = 0
            if (this.dataUi != "material" || this.dataLabelPosition == 'horizontal') {
              itemWidth = parseInt(this.dataLabelWidth.replace("px", ""));
            }
            itemWidth += parseInt(this.dataInputWidth.replace("px", ""));
            itemWidth += parseInt(this.dataItemMargin.replace("px", "")) * 2;
            dataLabelWidthInt = itemWidth * this.dataCol;
          }
          ret.width = dataLabelWidthInt + 20 + "px";
        }else{
          ret.width = "100%";
        }
        return ret;
      },
    },
    methods: {
      //校验函数
      validate() {
        let re = true;
        let focused=false
        this.formItems.map(formItem => {
          if (!formItem.validate()) {
            re = false;
            //将第一个不满足的item聚焦
            if(focused==false&&formItem.field.hasOwnProperty('focus')){
              formItem.field.focus()
              focused=true
            }
          }
        });
        return re;
      },
      reset() {
        this.formItems.map(formItem => {
          formItem.reset()
        });
      }
    },
    mounted() {
      this.broadcast('KBtn', 'k.form.addForm', this);
      this.dispatch('KPopup', 'k.form.addForm', this);
    },
  };
</script>

<style lang="scss">
  @import "./k-form.scss";
</style>
