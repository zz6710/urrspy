<template>
  <md-switch v-if="isShow" :model="_value" @change="change"/>
</template>

<script>
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";
  import auth from "@/utils/auth.js"

  export default {
    name: 'KFieldBswitch',
    mixins: [props(), event(), emitter()],
    props: {
      dataOnValue: {
        type: [Boolean, String, Number],
        default: true
      },
      dataOffValue: {
        type: [Boolean, String, Number],
        default: false
      },
      dataConfirm: {
        type: [Boolean, String],
        default: false
      },
      dataOnAction: {
        type: String
      },
      dataOffAction: {
        type: String
      },
      dataOnConfirmInfo: {
        type: String,
        default: "开启"
      },
      dataOffConfirmInfo: {
        type: String,
        default: "关闭"
      },
      dataBeforeHandler: {
        type: Function
      },
      dataAfterHandler: {
        type: Function
      }
    },
    data() {
      return {
        isShow: true,
        openPermission: true,
        closePermission: true
      };
    },
    computed: {
      _value: function () {
        return this.value === this.dataOnValue;
      },
      confirmInfo: function () {
        if (this.value === this.dataOnValue) {
          return this.dataOffConfirmInfo;
        } else {
          return this.dataOnConfirmInfo;
        }
      }
    },
    created() {
      /**
       * 判断权限是否显示或者隐藏
       * 满足一个就会展示
       */
      if (this.dataOnAction && this.dataOffAction) {
        this.openPermission = auth.check(this.dataOnAction);
        this.closePermission = auth.check(this.dataOffAction);
        this.isShow = this.openPermission || this.closePermission;
      }
    },
    methods: {
      validate(value) {
        this.$emit('data-validate', value);
      },
      change(value) {
        if (value) {
          this.doChange(this.openPermission, this.dataOnValue, this.dataOnAction);
          this.handleChange(this.dataOnValue)
        } else {
          this.doChange(this.closePermission, this.dataOffValue, this.dataOffAction);
          this.handleChange(this.dataOffValue)
        }
      },
      doChange(permission, dataValue, dataAction) {
        if (permission) {
          if (!this.doBeforeHandler(dataValue)) {
            return;
          }
          if (this.dataConfirm === 'true' || this.dataConfirm === true) {
            this.$confirm("确认" + this.confirmInfo + "吗？", '操作提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              cancelButtonClass: 'el-button--info',
              type: 'warning',
              beforeClose: (action, instance, done) => {
                if (action === 'confirm') {
                  instance.confirmButtonLoading = true;
                  instance.confirmButtonText = '执行中...';

                  if (dataAction) {
                    this.request(dataAction, dataValue, () => {
                      done();
                      instance.confirmButtonLoading = false;
                    })
                  } else {
                    this.handleInput(dataValue);
                    done();
                    instance.confirmButtonLoading = false;
                    if (this.dataAfterHandler) {
                      this.dataAfterHandler(dataValue);
                    }
                  }
                } else {
                  done();
                }
              }
            })
          } else {
            if (dataAction) {
              this.request(dataAction, dataValue)
            } else {
              this.handleInput(dataValue);
              if (this.dataAfterHandler) {
                this.dataAfterHandler(dataValue);
              }
            }
          }
        } else {
          this.$message.warning("权限不足")
          return;
        }
      },
      doBeforeHandler(dataValue) {
        if (this.dataBeforeHandler) {
          let res = this.dataBeforeHandler(dataValue, this.dataParams);

          if (res === false) { //不做任何处理
            return false;
          }
        }
        return true;
      },
      request(action, dataValue, callback) {
        if (!action) {
          return;
        }
        this.httpUtil.comnUpdate({
          action: action,
          params: this.dataParams,
          successAlert: true,
        }).then(data => {
          this.handleInput(dataValue);
          if (callback) {
            callback();
          }
          if (this.dataAfterHandler) {
            this.dataAfterHandler(dataValue, data);
          }
        });
      }
    }
  };
</script>
