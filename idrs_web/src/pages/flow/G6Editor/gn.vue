<template>
  <div
    id="mountNode"
    :style="{width:width}"
  >
    <div class="editor">
      <toolbar2 :data="data"/>
      <div style="height: 42px;"></div>
      <div class="bottom-container">
        <item-panel2 :height="height"/>
        <page2
          :height="height"
          :width="width"
          :data="data"
        />
        <detail-panel2 :height="height"
          :data="data"/>
      </div>
    </div>
    <Flow />
  </div>
</template>

<script>
  import Toolbar2 from './Toolbar/toolbar2'
  import ItemPanel2 from './ItemPanel/index2'
  import DetailPanel2 from './DetailPanel/detailPanel2'
  import Page2 from './Page/page2'
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
      Page2,
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
      }
    },
    created () {
      // let initData = {"nodes":[{"name":"任务节点","label":this.global.wf_application_en_task_name,"size":["120","50"],"type":"node","x":432,"y":103,"shape":"customNode","color":"#1890ff","inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],"dragItemConfig":{"width":80,"height":44},"attrs":{"type":"task","name":"applicationTask","displayName":"申请","btns-checkedKeys":[],"enableAttachment":"0","useRole":"1","rejectTaskNames":[],"actors-checkedKeys":[],"roles-checkedKeys":[],"updateEnvTask":[],"formId":""},"offsetX":40,"offsetY":17,"displayName":"applicationTask","id":"node3"},{"name":"开始节点","size":["50","35"],"type":"node","x":266,"y":103,"shape":"customNode","color":"#1890ff","imageWidth":20,"imageHeight":20,"inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],"dragItemConfig":{"width":42,"height":42},"attrs":{"type":"startevent-none","name":"start"},"offsetX":23,"offsetY":18,"id":"node14"},{"name":"结束","size":["50 "," 35"],"type":"node","x":621,"y":103,"shape":"customNode","color":"#1890ff","inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],"imageWidth":20,"imageHeight":20,"dragItemConfig":{"width":42,"height":42},"attrs":{"type":"endevent-none","name":"end"},"offsetX":32,"offsetY":11,"id":"node77"}],"edges":[{"id":"edge76","source":"node14","target":"node3","sourceId":"node14","targetId":"node3","start":{"x":25,"y":0},"end":{"x":-60,"y":0},"label":"","anchorPoints":[[0,0.5],[0.5,0],[1,0.5],[0.5,1]],"labelCfg":{"position":"center","autoRotate":true,"style":{"stroke":"white","lineWidth":5,"fill":"#722ed1"}},"style":{"radius":10,"offset":30,"endArrow":true,"stroke":"#b8c3ce","lineAppendWidth":10,"lineWidth":2},"attrs":{"name":"edge75","type":"edge","displayName":"","envTransitionExpr":""},"shape":"polyline","type":"edge","startPoint":{"x":291.5,"y":103,"index":1,"anchorIndex":1,"id":"291.5-103"},"endPoint":{"x":371.5,"y":103,"index":0,"anchorIndex":0,"id":"371.5-103"}},{"id":"edge119","source":"node3","target":"node77","sourceId":"node3","targetId":"node77","start":{"x":60,"y":0},"end":{"x":-25,"y":0},"label":"","anchorPoints":[[0,0.5],[0.5,0],[1,0.5],[0.5,1]],"labelCfg":{"position":"center","autoRotate":true,"style":{"stroke":"white","lineWidth":5,"fill":"#722ed1"}},"style":{"radius":10,"offset":30,"endArrow":true,"stroke":"#b8c3ce","lineAppendWidth":10,"lineWidth":2},"attrs":{"name":"edge118","type":"edge","displayName":"","envTransitionExpr":""},"shape":"polyline","type":"edge","startPoint":{"x":492.5,"y":103,"index":1,"anchorIndex":1,"id":"492.5-103"},"endPoint":{"x":595.5,"y":103,"index":0,"anchorIndex":0,"id":"595.5-103"}}],"groups":[]};
      // let editorData = {};
      // editorData.processData = initData;
      // editorData.processPopup = this.$refs.processPopup;
      // editorData.$grid = this.$refs.processGrid;
      // editorData.update = false;
      // editorData.type = this.global.wf_process_default_type;
      // this.data=editorData
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

<style lang="scss" scoped>
  .editor {
    position: relative;
    width: 100%;
    font-size: 12px;
    user-select: none;
    -moz-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
  }
  /*/deep/.bottom-container {*/
  /*  !*position: relative;*!*/
  /*  display: flex !important;*/
  /*  flex-direction: row !important;*/
  /*}*/
</style>
