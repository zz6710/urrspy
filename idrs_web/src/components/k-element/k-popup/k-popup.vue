<template>
  <el-dialog class="kk-popup" :modal-append-to-body="false" :append-to-body="dataAppendToBody==true||dataAppendToBody=='true'" v-dialogDrag="dataDialogDrag" ref="elDialog" v-if="dataType=='dialog'" :visible.sync="dataVisible" :width="width" :modal="dataMask === true || dataMask === 'true'"
    :fullscreen="dataFullscreen === true || dataFullscreen === 'true'" :before-close="handleBeforeClose" @open="handleOpen"
    @close="handleClose" @opened="handleOpened" @closed="handleClosed" :center="dialogTitleCentered" :close-on-click-modal="dataCloseOnClickModal" :top="top">
    <slot name="title" slot="title">
      <!-- <div class="dialog-icon" :style="iconStyle">
        <md-icon :md-src="dataIcon"></md-icon>
      </div> -->
      <span class="el-dialog__title">{{dataTitle}}</span>
    </slot>
    <slot v-if="show" class="ee"></slot>
    <div class="kk-mask" v-if="dataMask2 === true || dataMask2 === 'true'"></div>
  </el-dialog>
  <el-drawer ref="elDrawer" v-else-if="dataType=='drawer'" :title="dataTitle" :visible.sync="dataVisible" :size="width"
    :direction="direction" :modal="dataMask === true || dataMask === 'true'" :before-close="handleBeforeClose" :wrapperClosable="dataCloseOnClickModal"
    destroy-on-close @open="handleOpen" @close="handleClose" @opened="handleOpened" @closed="handleClosed">
    <slot v-if="show"></slot>
  </el-drawer>
</template>

<script>
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";


  export default {
    name: 'KPopup',
    mixins: [props(), event(), emitter()],
    props: {
      dataAppendToBody:{
        type:[Boolean, String],
        default:false
      },
      dataDialogDrag:{
        type:[Boolean, String],
        default:false
      },
      dataTitle: {
        type: String
      },
      dataWidthPercent: {
        type: String
      },
      dataWidth: {
        type: String
      },
      dataIcon: {
        type: String,
        default: "/static/svg/form.svg"
      },
      dataFullscreen: {
        type: [Boolean, String],
        default: false
      },
      dataDirection: {
        type: String,
        default: "right"
      },
      dataConfirmDescribe: {
        type: String,
        default: "是否确认关闭"
      },
      dataConfirmClose: {
        type: [Boolean, String],
        default: false
      },
      dataMask: {
        type: [Boolean, String],
        default: true
      },
      dataMask2: {
        type: [Boolean, String],
        default: true
      },
      dataType: {
        type: String,
        default: "dialog"
      },
      dataTitleAlign: {
        type: String,
        default: "left"
      },
      dataCloseOnClickModal:{
        type:[Boolean,String],
        default:false
      }
    },
    data() {
      return {
        dataVisible: false,
        dataBeforeCloseExecuted: false,
        form: null,
        show: false,
        top:   "50px",
        popupHeight: 0,
      }
    },
    computed: {
      width() {
        if (this.dataFullscreen == true || this.dataFullscreen == 'true') {
          return '100%'
        }

        const dataWidthPercent = this.dataWidthPercent
        if (dataWidthPercent) {
          return dataWidthPercent;
        }

        const dataWidth = this.dataWidth
        if (dataWidth) {
          return dataWidth
        }

        if (this.form) {
          if (this.dataType == 'drawer' && (this.dataDirection == 'top' || this.dataDirection == 'bottom')) {
            return null
          }
          console.log(this.form, this.form.formStyle, 'pp--==');
          return parseInt(this.form.formStyle.width.match(/[0-9]*/)[0]) + 50 + 'px'
        }

        return '70%'
      },
      direction() {
        const dataDirection = this.dataDirection
        if (!dataDirection) {
          return "rtl"
        }

        if ("right" === dataDirection) {
          return "rtl"
        } else if ("left" === dataDirection) {
          return "ltr"
        } else if ("top" === dataDirection) {
          return "ttb"
        } else if ("bottom" === dataDirection) {
          return "btt"
        }
      },
      dialogTitleCentered() {
        if ("center" == this.dataTitleAlign) {
          return true
        } else {
          return false
        }
      },
      iconStyle() {
        let iconStyle = {};
        iconStyle.background = this.$store.state.system.cardBackground;
        return iconStyle;
      }
    },
    methods: {
      handleBeforeClose(done) {
        this.$emit("data-before-close", (value) => {
          this.dataBeforeCloseExecuted = true
          if (this.dataConfirmClose === true || this.dataConfirmClose === 'true') {
            let dataConfirmDescribe = value ? value : this.dataConfirmDescribe
            this.$confirm(dataConfirmDescribe)
              .then(_ => {
                done();
              })
              .catch(_ => {});
          } else {
            done()
          }
        })
        if (!this.dataBeforeCloseExecuted) {
          this.dataBeforeCloseExecuted = false
          if (this.dataConfirmClose === true || this.dataConfirmClose === 'true') {
            this.$confirm(this.dataConfirmDescribe)
              .then(_ => {
                done();
              })
              .catch(_ => {});
          } else {
            done()
          }
        }

      },
      handleOpen() {
        this.show = true
        this.$emit("data-open", this.value);
        window.onresize = ()=>{
          this.changeTop();
        };
      },
      handleClose() {
        this.show = false
        this.$emit("data-close", this.value)
      },
      handleOpened() {
        this.$emit("data-opened", this.value)
      },
      handleClosed() {
        this.$emit("data-closed", this.value)
      },
      popup() {
        this.dataVisible = true
        if (!this.form) {
          setTimeout(() => {
            const forms = this.getChildrens("KForm");
            this.popupHeight = this.$refs.elDialog.$el.children[0].clientHeight * 1.3;
            this.changeTop();
            if (forms && forms.length > 0) {
              this.form = forms[0];
            }
          }, 50);
        }

      },
      changeTop(){
        let height = document.body.clientHeight - this.popupHeight;
        this.top = (height/2 > 50 ? height /2 :50) + 'px';
      },
      close() {
        if (this.dataType === 'drawer') {
          this.$refs.elDrawer.closeDrawer()
        } else if (this.dataType === 'dialog') {
          this.$refs.elDialog.handleClose();
        }
        this.reset()
      },
      doreset() {
        if (this.form) {
          this.form.reset()
        }
      }
    }
  };
</script>

<style lang="scss" scoped>
  @import './k-popup.scss';

  ::v-deep .k-form-body{
    overflow-y: auto;
  }

  ::v-deep .k-form-body::-webkit-scrollbar {
    display: none;
  }

  ::v-deep.el-dialog__wrapper{
    width: calc(100% - 260px);
    position: fixed;
    top: 45px;
    left: 260px ;
    overflow: auto;
    margin: 0;
    z-index: 2000;
  }


</style>
