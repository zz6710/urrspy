<template>
    <div>
        <k-form-search>
            <k-btn data-functype="POPUP" data-target="ff" :data-handler="setNode">添加产品流程</k-btn>
        </k-form-search>

        <k-popup ref="ff">
             <gn :data="editData"></gn>
        </k-popup>
    </div>
</template>

<script>
    import gn from "./G6Editor/gn.vue"
    export default {
        name: "FlowDesign2",
        components: {gn},
        data(){
          return{
            editData:{

            }
          }
        },
        methods:{
          setNode(){
            let initData = {"nodes":[{"name":"任务节点","label":this.global.wf_application_en_task_name,
                "size":["120","50"],"type":"node","x":432,"y":103,"shape":"customNode","color":"#1890ff",
                "inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],"dragItemConfig":{"width":80,"height":44},
                "attrs":{"type":"task","name":"applicationTask","displayName":"申请","btns-checkedKeys":[],
                  "enableAttachment":"0","useRole":"1","rejectTaskNames":[],"actors-checkedKeys":[],"roles-checkedKeys":[],
                  "updateEnvTask":[],"formId":""},"offsetX":40,"offsetY":17,"displayName":"applicationTask","id":"node3"},
                {"name":"开始节点","size":["50","35"],"type":"node","x":266,"y":103,"shape":"customNode","color":"#1890ff",
                  "imageWidth":20,"imageHeight":20,"inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],
                  "dragItemConfig":{"width":42,"height":42},"attrs":{"type":"startevent-none","name":"start"},
                  "offsetX":23,"offsetY":18,"id":"node14"},{"name":"结束","size":["50 "," 35"],"type":"node","x":621,
                  "y":103,"shape":"customNode","color":"#1890ff","inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],
                  "imageWidth":20,"imageHeight":20,"dragItemConfig":{"width":42,"height":42},"attrs":{"type":"endevent-none",
                    "name":"end"},"offsetX":32,"offsetY":11,"id":"node77"}],"edges":[{"id":"edge76","source":"node14",
                "target":"node3","sourceId":"node14","targetId":"node3","start":{"x":25,"y":0},"end":{"x":-60,"y":0},"label":"",
                "anchorPoints":[[0,0.5],[0.5,0],[1,0.5],[0.5,1]],"labelCfg":{"position":"center","autoRotate":true,
                  "style":{"stroke":"white","lineWidth":5,"fill":"#722ed1"}},"style":{"radius":10,"offset":30,"endArrow":true,
                  "stroke":"#b8c3ce","lineAppendWidth":10,"lineWidth":2},"attrs":{"name":"edge75","type":"edge","displayName":"",
                  "envTransitionExpr":""},"shape":"polyline","type":"edge","startPoint":{"x":291.5,"y":103,"index":1,"anchorIndex":1,
                  "id":"291.5-103"},"endPoint":{"x":371.5,"y":103,"index":0,"anchorIndex":0,"id":"371.5-103"}},{"id":"edge119",
                "source":"node3","target":"node77","sourceId":"node3","targetId":"node77","start":{"x":60,"y":0},"end":{"x":-25,
                  "y":0},"label":"","anchorPoints":[[0,0.5],[0.5,0],[1,0.5],[0.5,1]],"labelCfg":{"position":"center","autoRotate":true,
                  "style":{"stroke":"white","lineWidth":5,"fill":"#722ed1"}},"style":{"radius":10,"offset":30,"endArrow":true,"stroke":"#b8c3ce",
                  "lineAppendWidth":10,"lineWidth":2},"attrs":{"name":"edge118","type":"edge","displayName":"","envTransitionExpr":""},
                "shape":"polyline","type":"edge","startPoint":{"x":492.5,"y":103,"index":1,"anchorIndex":1,"id":"492.5-103"},"endPoint":{"x":595.5,
                  "y":103,"index":0,"anchorIndex":0,"id":"595.5-103"}}],"groups":[]};
            let editorData = {};
            editorData.processData = initData;
            editorData.processPopup = this.$refs.processPopup;
            editorData.$grid = this.$refs.processGrid;
            editorData.update = false;
            editorData.type = this.global.wf_process_default_type;
            this.editData=editorData
          }
        }
    }
</script>

<style scoped>

</style>
