export default function() {
  return {
    created() {
      this.setMdFieldValue();
      this.setMdFieldRequired();
      this.setMdFieldDisabled();
      this.setDefaultValue();
    },
    inject: {
      MdField: {
        default: null
      },
      KFormItem: {
        default: null
      }
    },
    methods: {
      handleBlur(value) {
        if (this.MdField) {
          this.MdField.focused = false;
        }
        if (this.formatValue) {
          value = this.formatValue(value);
        }
        this.$emit('data-on-blur', value);
      },
      handleFocus(value) {
        if (this.MdField) {
          this.MdField.focused = true;
        }
        if (this.formatValue) {
          value = this.formatValue(value);
        }

        this.$emit('data-on-focus', value);
        if(this.dataRemote && this.remoteMethod) {
          this.remoteMethod();
        }
      },
      handleInput(value) {
        if (this.formatValue) {
          value = this.formatValue(value);
        }
        this.$emit('input', value);
        if (this.handleInputCallBack) {
          this.handleInputCallBack(value);
        }
      },

      handleChange(value) {
        if (this.formatValue) {
          value = this.formatValue(value);
        }
        this.$emit('data-on-change', value);
        if(this.KFormItem && (this.dataAutoValidate === true || this.dataAutoValidate === 'true')) {
          this.KFormItem.validate()
        }
      },
      handleClear(value) {
        this.$emit('clear')
      },
      disable() {
        this.dataDisabled = true;
      },
      enable() {
        this.dataDisabled = false;
      },
      //验证方法
      validate() {
        //验证是否为空
        if ((this.dataAllowblank === false || this.dataAllowblank === 'false') &&
          (!this.value ||
              (typeof this.value == 'object' && Object.keys(this.value).length == 0) ||
              (typeof this.value == 'array' && this.value.length == 0))
        ) {
          return "该项不允许为空";
        }

        //调用自定义验证函数
        if (this.dataValidate) {
          return this.dataValidate(this.value);
        }

        //调用控件验证函数
        if (this.doValidate) {
          return this.doValidate();
        }

        return true
      },
      //重置方法
      reset() {
        if (this.doRest) { //已经自定义了重置方法，调用自定义的重置方法
          this.doRest();
        } else { //调用默认的重置方法
          this.handleInput(this.dataDefaultValue);
        }
      },
      setMdFieldValue() {
        if (this.MdField) {
          if (this.$options.name == 'KFieldCheckbox' || this.$options.name == 'KFieldRadio') {
            this.MdField.value = 'KField';
          } else {
            this.MdField.value = this.value;
          }

        }
      },
      setMdFieldRequired() {
        if (this.MdField) {
          this.MdField.required = this.dataAllowblank === false || this.dataAllowblank === 'false';
        }
      },
      setMdFieldDisabled() {
        if (this.MdField) {
          this.MdField.disabled = this.dataDisabled === true || this.dataDisabled === 'true';
        }
      },
      setDefaultValue() {
        if (this.dataDefaultValue) {
          this.handleInput(this.dataDefaultValue);
        }
      },
      clear() {
        this.handleInput('');
        this.handleChange('')
      }
    },
    watch: {
      value() {
        this.setMdFieldValue();
      },
      dataAllowblank() {
        this.setMdFieldRequired();
      },
      dataDisabled() {
        this.setMdFieldDisabled();
      }
    },
    mounted() {
      if (this.dispatch) {
        this.dispatch('KFormItem', 'k.form.addField', this);
      }
    },
    beforeDestroy() {
      if (this.dispatch) {
        this.dispatch('KFormItem', 'k.form.removeField', this);
      }
    }
  };
}
