<template>
  <div
    id="mountNode"
    :style="{width:width}"
  >
    <div class="editor">
      <toolbar :data="data"/>
      <div style="height: 42px;"></div>
      <div class="bottom-container">
        <item-panel />
        <!--detailpannel-->
        <detail-panel :data="data"/>
        <!-- <minimap /> -->
        <!--page-->
        <page
          :height="height"
          :width="width"
          :data="data"
        />
      </div>
    </div>
    <Flow />
  </div>
</template>

<script>
import Toolbar from './Toolbar/toolbar'
import ItemPanel from './ItemPanel'
import DetailPanel from './DetailPanel/detailPanel'
import Page from './Page/page'
import Flow from './Flow'
import Editor from './Base/Editor'
import Minimap from './Minimap'
import Command from './command'
import testData from './test'
export default {
  name: 'G6Editor',
  components: {
    Toolbar,
    ItemPanel,
    DetailPanel,
    Page,
    Flow,
    Minimap
  },
  props: {
    height: {
      type: Number,
      default: 700
    },
    width: {
      type: Number,
      default: 1592
    }
  },
  created () {
    this.init()
  },
  data () {
    return {
      editor: {},
      command: null,
      // initData: testData
      initData: {},
      data: {}
    }
  },
  methods: {
    init () {
      if (this.$route.query.editorData) {
        this.data = JSON.parse(this.$route.query.editorData);
      }
      this.editor = new Editor()
      this.command = new Command(this.editor)
    }
  }
}
</script>

<style scoped>
  #mountNode {
    height: 100%;
  }
.editor {
  position: relative;
  width: 100%;
  height: 100%;
  font-size: 12px;
  user-select: none;
  -moz-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
}
.bottom-container {
  position: relative;
  height: calc(100% - 42px);
}
</style>
