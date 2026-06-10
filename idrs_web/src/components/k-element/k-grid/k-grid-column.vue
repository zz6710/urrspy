<template>

  <el-table-column :fixed="dataFixed" :prop="dataName" :label="dataHeader" :sortable="(dataSortable === 'true' || dataSortable === true)? 'custom' : false"
    :sort-method="dataSortMethod" v-if="dataHidden === 'false' || dataHidden === false" :width="dataWidth" :align="dataAlign"
    :show-overflow-tooltip="dataOverflow?dataOverflow==='true':dataOverflow">
    <template v-if="!$slots.default || $slots.default.length == 0 || ($slots.default[0].componentOptions && $slots.default[0].componentOptions.tag !='k-grid-column')"
      slot-scope="{row, $index}">
      <div :class="[diffClass(row, $index)]"> 
        <div v-if="dataRender" v-html="htmlRender(row)" />
        <div v-else-if="$scopedSlots.default">
          <slot :row="{row}"></slot>
        </div>
        <span v-else>{{format(row)}}</span>
      </div>
    </template>
    <slot v-if="$slots.default && $slots.default.length>0 && $slots.default[0].componentOptions && $slots.default[0].componentOptions.tag == 'k-grid-column'"></slot>
  </el-table-column>

</template>

<script>
  import moment from "moment";
  import emitter from "@/components/k-element/common/k-emitter.js";
  import Tools from "../../../utils/tools";
  const RENDER_PLACEHOLDER_REG = /#{}/g

  export default {
    name: "KGridColumn",
    mixins: [emitter()],
    props: {
      dataHeader: {
        type: String,
        default: ""
      },
      dataName: {
        type: String,
        default: ""
      },
      dataSortable: {
        type: [Boolean, String],
        default: false
      },
      dataHidden: {
        type: [Boolean, String],
        default: false
      },
      dataFixed: {
        type: [Boolean, String],
        default: false
      },
      dataSortMethod: {
        type: Function
      },
      dataWidth: {
        type: String,
        default: ""
      },
      dataAlign: {
        type: String,
        default: "left"
      },
      dataOverflow: {
        type: [Boolean, String],
        default: 'true'
      },
      dataRender: {
        type: String
      },
      dataDict: {
        type: String
      },
      dataType: {
        type: String
      },
      dataDictType: {
        type: String,
        default: ""
      },
      dataDigits: {
        type: String,
        default: ""
      }
    },
    created() {
      if (!this.$slots.default || this.$slots.default.length == 0 || !this.$slots.default[0].componentOptions || this.$slots.default[0].componentOptions.tag !=
        'k-grid-column') {
        // console.info(this);
      }
    },
    data() {
      return {};
    },
    computed: {
      compareList() {
        return this.getParent("KGrid").compareList || []
      }
    },
    methods: {
      diffClass(row, $index) {
        if (this.compareList.length) {
          return this.compareList[$index][this.dataName] === '1' ? 'col-diff' : ''
        } else {
          return ''
        }
      },
      format(row) {
        if (!row) {
          return "";
        }
        // 格式化的顺序 dataRender dataType
        if (this.dataRender) {
          return this.getParent("KGrid").$parent[this.dataRender](row);
        }
        return this.doFormat(row);
      },
      htmlRender(row) {
        if (!this.dataRender) {
          console.error("请检查data-render配置。")
          return;
        }
        let html = this.getParent("KGrid").$parent[this.dataRender](row);
        if (this.dataDict || this.dataType) {
          return html.replace(RENDER_PLACEHOLDER_REG, this.doFormat(row));
        } {
          return html;
        }
      },
      doFormat(row) {
        let val = row[this.dataName];
        if (this.dataDict) {
          val = this.getParent("KGrid").dictTransfer(this.dataDict, val, this.dataDictType);
        } else if (this.dataDigits) {
          if (val || val == 0) {
            val = Number(val).toFixed(this.dataDigits);
          }
        } else {
          if (this.dataType) {
            if (this.dataType === "date") {
              val = Tools.formatDate(val);
            }
            if (this.dataType === "time") {
              val = Tools.formatTime(val);
            }
            if (this.dataType === "money") {
              val = Tools.formatMoney(val);
            }
            if (this.dataType === "timestamp" && val) {
              val = moment(val).format("YYYY-MM-DD HH:mm:ss");
            }
          }
        }
        return val;
      },
    }
  };
</script>

<style lang="scss">
  @import './k-grid.scss';
</style>
