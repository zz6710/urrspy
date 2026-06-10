<template>
  <div class="k-descriptions-item" :style="itemStyle" @click="itemClick(dataValue)">
    <div v-if="dataLabel" class="desc-item-label" :style="labelStyle">{{ dataLabel + '：' }}</div>
    <div class="desc-item-value" :style="valueStyle">
      <span v-if="(dataDict || dataType) && dataSucceedValue === undefined" v-html="dataDisplayValue"></span>
      <span v-else-if="(dataDict || dataType) && dataSucceedValue">
        <md-icon v-bind:style="iconClass">
        {{ icon }}
      </md-icon> {{ dataDisplayValue }}
      </span>
      <span v-else>{{ dataValue }}</span>
      <slot></slot>
    </div>
  </div>
</template>

<script>
import Tools from "@/utils/tools.js";
import emitter from "@/components/k-element/common/k-emitter.js";

export default {
  name: "KDescriptionsItem",
  mixins: [emitter()],
  props: {
    dataLabel: {
      type: String
    },
    dataValue: {
      type: [String, Number]
    },
    dataDict: {
      type: String
    },
    dataType: {
      type: String
    },
    dataValueStyle: {
      type: Object,
      default: () => {
      }
    },
    dataCol: {
      type: [Number, String],
      default: 1
    },
    dataLabelWidth: {
      type: String,
    },
    dataValueWidth: {
      type: String,
    },
    dataSucceedValue: {
      type: [String, Number, Boolean],
      default: undefined
    },
    dataSucceedIconClass: {
      type: Object,
    },
    dataErrorIconClass: {
      type: Object,
    },
    dataSucceedIcon: {
      type: String
    },
    dataErrorIcon: {
      type: String
    }
  },
  data() {
    return {
      dataDisplayValue: '',
      itemWidth: '',
      icon: '',
      iconClass: {}
    }
  },
  computed: {
    itemStyle() {
      const ret = {};
      let kDescriptions = this.getParent('KDescriptions');
      if (kDescriptions && kDescriptions.dataCol > 0) {
        ret.width = 1 / parseInt(kDescriptions.dataCol) * 100 * parseInt(this.dataCol) + '%'
      } else {
        ret.width = "100%";
      }
      return ret;
    },
    labelStyle() {
      const ret = {};
      let kDescriptions = this.getParent('KDescriptions');
      const dataLabelWidth = this.dataLabelWidth || kDescriptions.dataLabelWidth;
      if (dataLabelWidth) {
        ret.width = dataLabelWidth;
      }
      if (this.dataValueStyle && Object.keys(this.dataValueStyle).length > 0) {
        Object.keys(this.dataValueStyle).forEach(key => {
          ret[key] = this.dataValueStyle[key]
        })
      }
      return ret;
    },
    valueStyle() {
      const ret = {};
      let kDescriptions = this.getParent('KDescriptions');
      const dataValueWidth = this.dataValueWidth || kDescriptions.dataValueWidth;
      ret.width = parseInt(this.dataCol) > 1 ? (this.itemWidth * parseInt(this.itemStyle.width.replace('%', '')) / 100 - 180) + 'px' : dataValueWidth
      if (this.dataValueStyle && Object.keys(this.dataValueStyle).length > 0) {
        Object.keys(this.dataValueStyle).forEach(key => {
          ret[key] = this.dataValueStyle[key]
        })
      }
      return ret;
    }
  },
  watch: {
    dataValue(val) {
      this.formatData(val)
    }
  },
  mounted() {
    this.itemWidth = this.$parent.$el.clientWidth;
    this.formatData(this.dataValue)
  },
  methods: {
    formatData(val) {
      if (this.dataDict) {
        this.httpUtil.dictTransfer(this.dataDict, val).then(data => {
          this.dataDisplayValue = data;
          if (this.dataSucceedValue) {
            if (this.dataSucceedValue === val) {
              this.icon = this.dataSucceedIcon;
              this.iconClass = this.dataSucceedIconClass;
            } else {
              this.icon = this.dataErrorIcon;
              this.iconClass = this.dataErrorIconClass;
            }
          }
        });
      } else {
        if (this.dataType) {
          if (this.dataType === "date") {
            this.dataDisplayValue = Tools.formatDate(val);
          } else if (this.dataType === "time") {
            this.dataDisplayValue = Tools.formatTime(val);
          } else if (this.dataType === "money") {
            this.dataDisplayValue = Tools.formatMoney(val);
          } else if (this.dataType === "textarea") {
            if (val) {
              val = val.replace(new RegExp("\n", "g"), "<br/>");
              val = val.replace(new RegExp(" ", "g"), "&nbsp;");
              this.dataDisplayValue = val
            }
          } else {
            if (val) {
              this.dataDisplayValue = val;
            }
          }
        } else {
          if (val) {
            this.dataDisplayValue = val;
          }
        }
      }
    },
    itemClick(item) {
      this.$emit("data-item-click", item)
    }
  }

}
</script>

<style scoped>

</style>
