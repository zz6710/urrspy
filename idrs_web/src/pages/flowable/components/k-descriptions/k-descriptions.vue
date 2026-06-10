<template>
  <div :class="['k-descriptions',dataBorder ? '':'k-descriptions-simple']">
    <div class="k-desc-title" v-if="dataTitle">
      <svg-icon :icon-class="dataIconClass" :width="dataIconWidth" :height="dataIconHeight"></svg-icon>
      <span>{{ dataTitle }}</span>
    </div>
    <div class="k-desc-content">
      <div class="content-null-text" v-if="contentNullText" v-html="dataNullText"></div>
      <slot></slot>
      <div class="k-desc-data" v-if="dataData" v-for="(item,index) in dataData" :key="index"
           @click="rowClick(item)" @mouseenter.stop="rowEnter(index)" @mouseleave.stop="rowLeave">
        <transition name="desc">
          <div class="desc-operate-first" v-if="operateIndex === index">
            <slot v-if="dataOperate && dataOperatePosition === 'first'" name="desc-operate" :row="item"></slot>
          </div>
        </transition>

        <slot name="desc" :item="item"></slot>
        <div class="desc-line" v-if="dataLine"></div>

        <transition name="desc">
          <div class="desc-operate-end" v-if="operateIndex === index">
            <slot v-if="dataOperate && dataOperatePosition === 'end'" name="desc-operate" :row="item"></slot>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "KDescriptions",
    props: {
      dataTitle: {
        type: String,
      },
      dataCol: {
        type: [Number, String],
        default: 2
      },
      dataLabelWidth: {
        type: String,
        default: "100px"
      },
      dataValueWidth: {
        type: String,
        default: "200px"
      },
      dataData: {
        type: Array,
        default: () => []
      },
      dataNullText: {
        type: String,
        default: "暂无数据"
      },
      dataOperate: {
        type: Boolean,
        default: false
      },
      dataOperatePosition: {
        type: String,
        default: "end"
      },
      dataLine: {
        type: Boolean,
        default: false
      },
      dataBorder: {
        type: Boolean,
        default: true
      },
      dataIconClass:{
        type: String,
        default: "dot"
      },
      dataIconWidth:{
        type: String,
        default: "20px"
      },
      dataIconHeight:{
        type: String,
        default: "20px"
      }
    },
    data() {
      return {
        contentNullText: false,
        operateIndex: -1,
      }
    },
    mounted() {
      if ((this.$slots["desc"] || this.$scopedSlots['desc']) && this.dataData.length === 0) {
        this.contentNullText = true
      }
    },
    methods: {
      rowClick(row) {
        this.$emit("data-row-click", row)
      },
      rowEnter(index) {
        this.operateIndex = index
      },
      rowLeave() {
        this.operateIndex = -1
      }
    }
  }
</script>

<style scoped lang="scss">
  @import "k-descriptions";
</style>
