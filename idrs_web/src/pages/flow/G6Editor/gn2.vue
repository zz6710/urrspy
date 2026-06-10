<template>
  <div
    id="mountNode"
    :style="{width:width}"
  >
    <div class="editor">
        <page3
          :height="height"
          :width="width"
          :data="data"
          :isSkip="isSkip"
          :t8_prod_info_id="t8_prod_info_id"
          :prod_code="prod_code"
        />
    </div>
    <Flow />
  </div>
</template>

<script>
  import Toolbar2 from './Toolbar/toolbar2'
  import ItemPanel2 from './ItemPanel/index2'
  import DetailPanel2 from './DetailPanel/detailPanel2'
  import Page3 from './Page/page3'
  import Flow from './Flow'
  import Editor from './Base/Editor'
  import Minimap from './Minimap'
  import Command from './command'
  import testData from './test'
  export default {
    name: 'G6Editor',
    components: {
      Toolbar2,
      ItemPanel2,
      DetailPanel2,
      Page3,
      Flow,
      Minimap
    },
    props: {
      height: {
        type: Number,
        default: 466
      },
      width: {
        type: Number,
        default:1392
      },
      data: {
        type: Object,
        default: () => {}
      },
      isSkip:{
        type:Boolean,
      },
      t8_prod_info_id:{
        type:String,
      },
      prod_code:{
        type:String,
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
        initData: {}
      }
    },
    methods: {
      init () {
        this.editor = new Editor()
        this.command = new Command(this.editor)
      }
    }
  }
</script>

<style scoped>
  .editor {
    position: relative;
    width: 100%;
    font-size: 12px;
    user-select: none;
    -moz-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
  }
  .bottom-container {
    position: relative;
  }
</style>
