<template>
  <div>
    <k-popup :dataAppendToBody="dataPopupAppendToBody" ref="kGridDisplayPopup" data-title="详情" data-width="80%">
      <k-form ref="displayForm" :data-col="0" isFormBodyScreen :dataLabelWidth="dataLabelWidth">
        <k-form-item v-for="(value,key,index) in gridColumnOptions" v-if="value.dataHidden==='false' || value.dataHidden===''|| value.dataHidden===undefined" :key="index" :label="value.dataHeader + '：'" :class="{'column99': value.dataHeader === 'sql语句'}">
          <k-field-display v-model="row[value.dataName]" :data-dict="value.dataDict" :data-type="value.dataType" :data-clearable="false"/>
        </k-form-item>
      </k-form>
    </k-popup>
  </div>
</template>



<script>

  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";

export default {
  name: 'KGridDisplay',
  mixins: [props(), event(), emitter()],
  props: {
    row: {
      type: Object
    },
    gridColumnOptions: {
      type: Array
    },
    dataPopupAppendToBody:{
      type:[Boolean, String],
      default:false
    },
    dataLabelWidth: {
      type: String,
      default: '170px'
    }
  },
  methods: {
    popup() {
      this.$refs.kGridDisplayPopup.popup()
    }
  }
}
</script>

<style lang="scss" scoped>
  ::v-deep .md-field:after, .md-field:before {
    bottom: 10px;
    border: 1px solid white;
  }
  .el-form {
    .el-form-item {
      width: 24%;
      margin-right: 1%;
      &.column99 {
        width: 99%;
      }
      /deep/ {
        .el-form-item__label, .el-form-item__content {
          line-height: 1.5;
        }
      }
    }
  }
</style>

