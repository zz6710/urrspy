<template>
  <k-form-item class="widget-view"
    v-if="element && element.key"
    :key="element.key"
    :class="{ active: kFormDesign.widgetFormSelect.key == element.key }"
    v-bind="element.formItem"
    @click.native.stop="kFormDesign.selectComponentByKey(element.key)"
  >
    <template>
      <component :is="element.type" v-bind="element.options"></component>
    </template>
    <template slot="design" v-if="kFormDesign.widgetFormSelect.key == element.key">
      <div class="widget-view-action">
        <i class="iconfont icon-icon_clone" @click.stop="kFormDesign.cloneComponentByKey(element.key)"></i>
        <i class="iconfont icon-trash" @click.stop="kFormDesign.deleteComponentByKey(element.key)"></i>
      </div>
      <div class="widget-view-drag">
        <i class="iconfont icon-drag drag-widget"></i>
        <!-- <i class="iconfont icon-drag " v-if="isAbsLayout" @mousedown="handleMouseDownOnShape($event, element)"></i> -->
      </div>
    </template>
  </k-form-item>
</template>

<script>
import Draggable from 'vuedraggable'

export default {
  components: {
    Draggable,
  },
  props: ['element'],
  inject: ['kFormDesign'],
  data () {
    return {
    }
  },
  created (){
  },
  mounted () {
  },
  methods: {
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../styles/cover.scss';
  @import '../styles/index.scss';

  .widget-view /deep/.md-layout {
    &::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      z-index: 2;
    }
  }

</style>
