<template>
  <div>
    <k-form-search data-target="processGrid" data-model-name="Process">
      <k-btn data-functype="PAGE" class="btn-custom-primary" data-target="/main/operation/flow/G6Editor"
             :data-handler="handleAddBtn">
        添加流程
      </k-btn>
    </k-form-search>

    <k-grid
      ref="processGrid"
      data-action="Process.listAllProcess"
      data-operate-width="200px"
      @init="(grid)=>{this.$kgrid = grid}"
    >
      <k-grid-column data-header="流程中文名" data-name="displayName"/>
      <k-grid-column data-header="流程英文名" data-name="name" />
      <k-grid-column data-header="版本" data-name="version" />
      <k-grid-column data-header="已发布到最新" data-name="deployStatus" data-dict="1yes0no" />
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="发布" data-size="mini"
            data-functype="SUBMIT" data-action="Process.deploy" data-target="processGrid" data-confirm>
          <md-icon>send</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="编辑" data-functype="PAGE" data-size="mini"
            data-target="/main/operation/flow/G6Editor" :data-handler="handleEditBtn">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-size="mini" data-descript="删除" data-confirm
            data-functype="SUBMIT" data-action="Process.removeProcess" data-target="processGrid" >
          <md-icon>delete</md-icon>
        </k-btn>
      </template>
    </k-grid>
  </div>

</template>

<script>
  import G6Editor from "./G6Editor";
  import {findComponentsDownward, findComponentUpward} from "./assist.js";
  import Tools from '@/utils/tools.js';
  import transOA2this from "./G6Editor/Toolbar/transOA2this.js";

  export default {
    name: "flow_design",
    data() {
      return {
        editorData: {},
        update: false,
        $kgrid: null,
        searchData: {},
        queryParam: {
          name: '',
          displayName: ''
        },
      };
    },
    components: {
      G6Editor
    },
    computed: {
      title() {
        return this.editorData.update ? "编辑流程" : "新增流程";
      }
    },
    methods: {
      query() {
        let target = this.$refs.processGrid;
        if (!target.gridOptions.dataParams) {
          target.gridOptions.dataParams = {}
        }
        this.formList.forEach(item => {
          if (item.data && item.data.trim()) {
            target.gridOptions.dataParams[item.field] = item.data
          } else {
            this.$delete(target.gridOptions.dataParams, item.field)
          }
        })

        let re = this.$refs.searchForm.validate();
        if (re === false) {
          return;
        }

        target.load()
      },
      handleAddBtn(param) {
        let initData = {
          nodes:[{
            "name":"功能节点",
            "label":" ",
            "size":["120","50"], "type":"node", "x":432, "y":103, "shape":"customNode", "color":"#1890ff",
            "inPoints":[[0,0.5],[0.5,0]], "outPoints":[[1,0.5],[0.5,1]],
            "dragItemConfig":{"width":44,"height":44},
					  imageWidth: 20, imageHeight: 36,
            "attrs":{
              "type": "operation",
              "name": "busi000",
              "displayName":"",
              "btns-checkedKeys":[],
              "enableAttachment":"0",
              "useRole":"1",
              "rejectTaskNames":[],
              "actors-checkedKeys":[],
              "roles-checkedKeys":[],
              "updateEnvTask":[],
              "formId":""
            },
            "offsetX":40, "offsetY":17, "displayName":"", "id":"node3"
          }, {
            "name":"开始节点", "size":["50","35"], "type":"node", "x":266, "y":103, "shape":"customNode", "color":"#1890ff","imageWidth":20, "imageHeight":20,
            "inPoints":[[0,0.5],[0.5,0]],
            "outPoints":[[1,0.5],[0.5,1]],
            "dragItemConfig":{ "width":42, "height":42 },
            "attrs":{"type":"startevent-none","name":"start"},
            "offsetX":23,"offsetY":18,"id":"nodeStart"
          }, {
            "name":"结束", "size":["50 "," 35"], "type":"node", "x":621,"y":103, "shape":"customNode", "color":"#1890ff",
            "inPoints":[[0,0.5],[0.5,0]],
            "outPoints":[[1,0.5],[0.5,1]],
            "imageWidth":20, "imageHeight":20,
            "dragItemConfig":{"width":42,"height":42},
            "attrs":{"type":"endevent-none","name":"end"},
            "offsetX":32,"offsetY":11,"id":"nodeEnd"
          }],
          edges: [{
            "id":"edge76","source":"nodeStart","target":"node3","sourceId":"nodeStart","targetId":"node3",
            "start":{"x":25,"y":0},
            "end":{"x":-60,"y":0},
            "label":"",
            "anchorPoints":[[0,0.5],[0.5,0],[1,0.5],[0.5,1]],
            "labelCfg":{
              "position":"center","autoRotate":true,
              "style":{"stroke":"white","lineWidth":5,"fill":"#722ed1"}
            },
            "style":{"radius":10,"offset":30,"endArrow":true,"stroke":"#b8c3ce","lineAppendWidth":10,"lineWidth":2},
            "attrs":{"name":"edge75","type":"edge","displayName":"","envTransitionExpr":""},
            "shape":"polyline","type":"edge",
            "startPoint":{"x":291.5,"y":103,"index":1,"anchorIndex":1,"id":"291.5-103"},
            "endPoint":{"x":371.5,"y":103,"index":0,"anchorIndex":0,"id":"371.5-103"}
          }, {
            "id":"edge119","source":"node3","target":"nodeEnd","sourceId":"node3","targetId":"nodeEnd",
            "start":{"x":60,"y":0},
            "end":{"x":-25,"y":0},
            "label":"","anchorPoints":[[0,0.5],[0.5,0],[1,0.5],[0.5,1]],
            "labelCfg":{"position":"center","autoRotate":true,"style":{"stroke":"white","lineWidth":5,"fill":"#722ed1"}},
            "style":{"radius":10,"offset":30,"endArrow":true,"stroke":"#b8c3ce","lineAppendWidth":10,"lineWidth":2},
            "attrs":{"name":"edge118","type":"edge","displayName":"","envTransitionExpr":""},
            "shape":"polyline","type":"edge",
            "startPoint":{"x":492.5,"y":103,"index":1,"anchorIndex":1,"id":"492.5-103"},
            "endPoint":{"x":595.5,"y":103,"index":0,"anchorIndex":0,"id":"595.5-103"}
          }],
          "groups":[]
        };
        let editorData = {};
        editorData.processData = initData;
        // editorData.$grid = this.$refs.processGrid;
        editorData.update = false;
        editorData.type = this.global.wf_process_default_type;
        this.editorData = editorData;
        param.editorData = JSON.stringify(editorData);
      },
      handleEditBtn(param) {
        this.update = true;
        let editorData = {};
        editorData.processData = JSON.parse(param.json).orignal;
        editorData.processId = param.processId;
        editorData.version = param.version;
        editorData.name = param.name;
        editorData.displayName = param.displayName;
        editorData.update = true;
        param.editorData = JSON.stringify(editorData);
        param.json = '';
        console.log(editorData.processData);
      },
    }
  };
</script>

<style>

</style>
