<template>
  <div>
    <k-form-search-customize data-target="processGrid" v-model="queryParam">
      <k-form-item label="英文名">
        <k-field-text v-model="queryParam.name"></k-field-text>
      </k-form-item>
      <k-form-item label="中文名">
        <k-field-text v-model="queryParam.displayName"></k-field-text>
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" data-target="processPopup" :data-handler="handleAddBtn">
        <md-icon md-src="/static/svg/add.svg"/>
        添加流程
      </k-btn>
    </k-form-search-customize>

    <k-grid ref="processGrid" data-url="wf/process/listAllProcess.json" data-operate-width="200px"
      @init="(grid)=>{this.$kgrid = grid}" >
      <k-grid-column data-header="流程中文名" data-name="displayName"/>
      <k-grid-column data-header="流程英文名" data-name="name"/>
      <k-grid-column data-header="版本" data-name="version"/>
      <k-grid-column data-header="创建人" data-name="creator"/>
      <k-grid-column data-header="创建时间" data-name="createTime"/>
      <k-grid-column data-header="更新时间" data-name="updateTime"/>
      <template slot="operate">
        <k-btn class="md-info md-just-icon md-simple" data-descript="发布" data-functype="POPUP" data-size="mini"
               :data-handler="handlePublish" >
          <md-icon>send</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="编辑" data-functype="POPUP" data-size="mini"
               data-target="processPopup" :data-handler="handleEditBtn">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-descript="删除" data-functype="POPUP" data-size="mini"
               :data-handler="handleDelBtn">
          <md-icon>delete</md-icon>
        </k-btn>
<!--        <k-btn class="md-info md-just-icon md-simple" data-descript="转换" data-functype="POPUP" data-size="mini"-->
<!--               :data-handler="handleTransBtn">-->
<!--          <md-icon>edit</md-icon>-->
<!--        </k-btn>-->
      </template>
    </k-grid>
    <k-popup ref="processPopup" class="popClass" :data-title="title" data-width="1200px" >
      <G6Editor :data="editorData"></G6Editor>
    </k-popup>
  </div>

</template>

<script>
  import G6Editor from "./G6Editor";
  import {findComponentsDownward, findComponentUpward} from "./assist.js";
  import Tools from '@/utils/tools.js';
  import transOA2this from "./G6Editor/Toolbar/transOA2this.js";

  export default {
    name: "flowDesign",
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
      handleAddBtn() {
        let initData = {"nodes":[{"name":"任务节点","label":this.global.wf_application_en_task_name,"size":["120","50"],"type":"node","x":432,"y":103,"shape":"customNode","color":"#1890ff","inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],"dragItemConfig":{"width":80,"height":44},"attrs":{"type":"task","name":"applicationTask","displayName":"申请","btns-checkedKeys":[],"enableAttachment":"0","useRole":"1","rejectTaskNames":[],"actors-checkedKeys":[],"roles-checkedKeys":[],"updateEnvTask":[],"formId":""},"offsetX":40,"offsetY":17,"displayName":"applicationTask","id":"node3"},{"name":"开始节点","size":["50","35"],"type":"node","x":266,"y":103,"shape":"customNode","color":"#1890ff","imageWidth":20,"imageHeight":20,"inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],"dragItemConfig":{"width":42,"height":42},"attrs":{"type":"startevent-none","name":"start"},"offsetX":23,"offsetY":18,"id":"node14"},{"name":"结束","size":["50 "," 35"],"type":"node","x":621,"y":103,"shape":"customNode","color":"#1890ff","inPoints":[[0,0.5],[0.5,0]],"outPoints":[[1,0.5],[0.5,1]],"imageWidth":20,"imageHeight":20,"dragItemConfig":{"width":42,"height":42},"attrs":{"type":"endevent-none","name":"end"},"offsetX":32,"offsetY":11,"id":"node77"}],"edges":[{"id":"edge76","source":"node14","target":"node3","sourceId":"node14","targetId":"node3","start":{"x":25,"y":0},"end":{"x":-60,"y":0},"label":"","anchorPoints":[[0,0.5],[0.5,0],[1,0.5],[0.5,1]],"labelCfg":{"position":"center","autoRotate":true,"style":{"stroke":"white","lineWidth":5,"fill":"#722ed1"}},"style":{"radius":10,"offset":30,"endArrow":true,"stroke":"#b8c3ce","lineAppendWidth":10,"lineWidth":2},"attrs":{"name":"edge75","type":"edge","displayName":"","envTransitionExpr":""},"shape":"polyline","type":"edge","startPoint":{"x":291.5,"y":103,"index":1,"anchorIndex":1,"id":"291.5-103"},"endPoint":{"x":371.5,"y":103,"index":0,"anchorIndex":0,"id":"371.5-103"}},{"id":"edge119","source":"node3","target":"node77","sourceId":"node3","targetId":"node77","start":{"x":60,"y":0},"end":{"x":-25,"y":0},"label":"","anchorPoints":[[0,0.5],[0.5,0],[1,0.5],[0.5,1]],"labelCfg":{"position":"center","autoRotate":true,"style":{"stroke":"white","lineWidth":5,"fill":"#722ed1"}},"style":{"radius":10,"offset":30,"endArrow":true,"stroke":"#b8c3ce","lineAppendWidth":10,"lineWidth":2},"attrs":{"name":"edge118","type":"edge","displayName":"","envTransitionExpr":""},"shape":"polyline","type":"edge","startPoint":{"x":492.5,"y":103,"index":1,"anchorIndex":1,"id":"492.5-103"},"endPoint":{"x":595.5,"y":103,"index":0,"anchorIndex":0,"id":"595.5-103"}}],"groups":[]};
        let editorData = {};
        editorData.processData = initData;
        editorData.processPopup = this.$refs.processPopup;
        editorData.$grid = this.$refs.processGrid;
        editorData.update = false;
        editorData.type = this.global.wf_process_default_type;
        this.editorData = editorData;
      },
      handleDelBtn(row) {
        this.$confirm("确认删除吗？", "操作提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.httpUtil.comnQuery({
              action: 'WfBusinessConfig.find',
              params: {"processName": row.name}
            }).then(data=> {
              if (data.rows.length > 0) {
                Tools.alert("流程「" + row.displayName + "」绑定了业务，无法删除", this.global.notify_type.warning);
              } else {
                this.doDelete(row);
              }
            })

          })
          .catch(() => {
          });
      },
      doDelete(row) {
        this.httpUtil
          .ajax({
            url: "/wf/process/removeProcess.json",
            params: {
              processId: row.id
            }
          })
          .then(res => {
            this.$kgrid.load();
          });
      },
      // 新增OA数据转换按钮
      handleTransBtn(row) {
        this.update = true;
        this.$refs.processPopup.popup();
        let self = this;
        this.httpUtil
          .ajax({
            url: "/wf/process/getOAProcessById.json",
            params: {
              processId: row.id
            }
          })
          .then(res => {
            let data = res.data;

            let editorData = {};
            let oAjson = JSON.parse(data.oAjson);
            let processData = transOA2this(oAjson);
            editorData.processData = processData;
            editorData.name = processData.name;
            editorData.displayName = processData.displayName;
            editorData.envId = data.envId;
            editorData.update = true;
            editorData.isTrans = true;
            editorData.processId = row.id;
            editorData.processPopup = self.$refs.processPopup;
            editorData.$grid = self.$refs.processGrid;
            self.editorData = editorData;
          });
      },
      handleEditBtn(row) {
        this.update = true;
        let self = this;
        this.httpUtil
          .ajax({
            url: "/wf/process/getProcessById.json",
            params: {
              processId: row.id
            }
          })
          .then(res => {
            let data = res.data;
            let editorData = {};
            editorData.processData = JSON.parse(data.json).orignal;
            editorData.name = data.name;
            editorData.displayName = data.displayName;
            editorData.type = data.type;
            editorData.envId = data.envId;
            editorData.isJudgeProdUser = data.isJudgeProdUser;
            editorData.update = true;
            editorData.processId = row.id;
            editorData.processPopup = self.$refs.processPopup;
            editorData.$grid = self.$refs.processGrid;
            self.editorData = editorData;
          });
      },
      handlePublish(row) {
        this.$confirm("确认发布吗？", "操作提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.httpUtil
              .ajax({
                url: "/wf/process/deploy.json",
                params: {
                  processId: row.id
                }
              })
              .then(res => {
                if (res.status) {
                  Tools.alert("发布成功");
                } else {
                  Tools.alert("发布失败", "danger");
                }
                this.$kgrid.load();
              });
          })
          .catch(() => {
          });
      }
    }
  };
</script>

<style scoped>
  .popClass /deep/ .el-dialog {
    padding-top: 24px;
    margin-right: 5%;
  }

</style>
