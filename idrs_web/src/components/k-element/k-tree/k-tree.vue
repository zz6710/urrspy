<template>
  <el-tree
    :data="options"
    :empty-text="dataEmptyText"
    :node-key="dataNodeKey"
    :props="props"
    :render-after-expand="dataRenderAfterExpand"
    :load="(this.dataLazy === true ||this.dataLazy === 'true') ? dataLoad ? dataLoad : showNode : undefined"
    :render-content="dataRenderContent"
    :highlight-current="dataHighlightCurrent === true || dataHighlightCurrent === 'true'"
    :default-expand-all="dataDefaultExpandAll === true || dataDefaultExpandAll === 'true'"
    :expand-on-click-node="dataExpandOnClickNode === true || dataExpandOnClickNode === 'true'"
    :check-on-click-node="dataCheckOnCkickNode === true || dataCheckOnCkickNode === 'true'"
    :auto-expand-parent="dataAutoExpandParent === true || dataAutoExpandParent === 'true'"
    :default-expanded-keys="dataDefaultExpandedKeys"
    :show-checkbox="dataShowCheckbox === true || dataShowCheckbox === 'true'"
    :check-strictly="dataCheckStrictly === true || dataCheckStrictly === 'true'"
    :default-checked-keys="dataDefaultCheckedKeys"
    :current-node-key="dataCurrentNodeKye"
    :filter-node-method="dataFilterNodeMethod"
    :accordion="dataAccordion === true || dataAccordion === 'true'"
    :indent="dataIndent"
    :icon-class="dataIconClass"
    :lazy="this.dataLazy === true ||this.dataLazy === 'true'"
    :draggable="dataDraggable === true || dataDraggable === 'true'"
    :allow-drag="dataAllowDrag"
    :allow-drop="dataAllowDrop"
    @node-click="handleNodeClick"
    @node-contextmenu="handlerNodeContextMenu"
    @check="handlerCheck"
    @check-change="handleCheckChange"
    @current-change="handleCurrentChange"
    @node-expand="handlerNodeExpand"
    @node-collapse="handlerNodeCollapse"
    @node-drag-start="handlerNodeDragStart"
    @node-drag-enter="handlerNodeDragEnter"
    @node-drag-leave="handlerNodeDragLeave"
    @node-drag-over="handlerNodeDragOver"
    @node-drag-end="handlerNodeDragEnd"
    @node-drop="handlerNodeDragDrop"
    ref="tree"
  >
    <slot/>
  </el-tree>
</template>

<script>
  import props from '@/components/k-element/common/k-field-props.js'
  import event from '@/components/k-element/common/k-field-event.js'
  import treent from '@/components/k-element/common/k-field-treent.js'
  import Tools from '@/utils/tools.js'
  export default {
    name: 'KTree',
    componentName: 'KTree',
    mixins: [props(), event(), treent],
    props: {
      dataEmptyText: {
        type: String,
        default: undefined
      },
      dataNodeKey: {
        type: String,
        default: undefined
      },
      dataRenderAfterExpand: {
        type: Function,
        default: undefined
      },
      dataRenderContent: {
        type: Function,
        default: undefined
      },
      dataHighlightCurrent: {
        type: [Boolean, String],
        default: false
      },
      dataDefaultExpandAll: {
        type: [Boolean, String],
        default: undefined
      },
      dataExpandOnClickNode: {
        type: [Boolean, String],
        default: true
      },
      dataCheckOnCkickNode:{
        type: [Boolean, String],
        default: undefined
      },
      dataAutoExpandParent: {
        type: [Boolean, String],
        default: true
      },
      dataDefaultExpandedKeys: {
        type: Array,
        default: undefined
      },
      dataShowCheckbox: {
        type: [Boolean, String],
        default: undefined
      },
      dataCheckStrictly: {
        type: [Boolean, String],
        default: undefined
      },
      dataDefaultCheckedKeys: {
        type: Array,
        default: undefined
      },
      dataCurrentNodeKye: {
        type: [String, Number],
        default: undefined
      },
      dataFilterNodeMethod: {
        type: Function,
        default: undefined
      },
      dataAccordion: {
        type: [Boolean, String],
        default: undefined
      },
      dataIndent: {
        type: Number,
        default: undefined
      },
      dataIconClass: {
        type: String,
        default: undefined
      },
      dataLazy: {
        type: [Boolean, String],
        default: undefined
      },
      dataDraggable: {
        type: [Boolean, String],
        default: undefined
      },
      dataAllowDrag: {
        type: Function,
        default: undefined
      },
      dataAllowDrop:{
        type: Function,
        default: undefined
      },
      dataValueDisabled: {
        type: String,
        default: undefined
      },
      dataValueMethod: {
        type: Function,
        default: undefined
      },
      dataLoad: {
        type: Function,
        default: undefined
      },
      dataDisplayField:{
        type:String,
        default: undefined
      },
      //值字段
      dataValueField: {
        type:String,
        default: undefined
      },
      //字段下标
      dataDisplayChild: {
        type:String,
        default: undefined
      },
      //父节点
      dataParentField: {
        type:String,
        default: undefined
      },
      dataDiffcondition:{
        type:String,
        default: undefined
      },
      dataDisplaySeparator: {
        type: String,
        default: "-"
      },
      dataOnBeforeload: {
        type: Function,
        default: undefined
      },
      dataOnAfterload: {
        type: Function,
        default: undefined
      },
    },
    data() {
      return {
        options: [],
        params: {},
        props: {},
        graphql: String,
        dataType: false
      }
    },
    created() {
      // 配置验证
      if ((this.dataAction || this.dataGraphql) && (this.dataLazy === true ||this.dataLazy === 'true' ) && !this.dataParentField) {
        console.error('没有定义data-parent-field,数据加载失败!')
        return
      }

      if (this.dataParams) {
        this.params = typeof this.dataParams === 'string' ? Tools.str2Json(this.dataParams) : this.dataParams === undefined ? {} : this.dataParams
      }

      if ((this.dataAction || this.dataGraphql) && (this.dataLazy === true ||this.dataLazy === 'true' ) && !this.params.hasOwnProperty(this.dataParentField)) {
        console.error('data-params中没有定义data-parent-field属性，数据加载失败!')
        return
      }

      if ((this.dataAction || this.dataGraphql) && !(this.dataLazy === true ||this.dataLazy === 'true' ) && !this.dataDiffcondition) {
        console.error('没有定义data-diffcondition参数,加载失败!')
        return
      }

      if (this.dataAction && !(this.dataLazy === true ||this.dataLazy === 'true' ) && this.dataDiffcondition) {
        this.params.diffcondition = this.dataDiffcondition
      }

      this.props.value = !this.dataValueField ? 'value' : this.dataValueField
      this.props.label = !this.dataDisplayField ? 'label' : this.dataDisplayField
      this.props.children = !this.dataDisplayChild ? 'children' : this.dataDisplayChild
      this.props.disabled = 'disabled'

      this.dataType = this.dataData && this.dataValueField && this.dataDisplayField && this.dataDisplayChild

      if (this.dataAutoLoad === true || this.dataAutoLoad === 'true') {
        this.loadData();
      }
    },
    methods: {
      loadData() {
        if (this.dataOnBeforeload) {
           this.dataOnBeforeload(this.params)
        }
        if (this.dataData) {
          this.options = this.dataType ? this.handleData(this.dataData) : this.dataData
        } else if (this.dataAction) {
          (this.dataLazy === true ||this.dataLazy === 'true') ?  this.loadActionData() : this.loadActionDataTree()
        } else if (this.dataGraphql) {
          this.graphql = (this.dataLazy === true ||this.dataLazy === 'true') ? this.dataGraphql : this.getGraphqlUrl(this.dataGraphql,this.dataDiffcondition)
          this.loadGraphqlData()
        } else {
          console.error('不支持的数据获取方式，请检查k-field-cascader的元素属性配置')
        }
        if (this.dataOnAfterload) {
          this.dataOnAfterload(this.options)
        }
      },
      filterData(value) {
        this.$refs.tree.filter(value)
      },
      filterNode(value, data) {
        if (!value) return true;
        return data[this.props.label].indexOf(value) !== -1;
      },
      handleNodeClick(data, node, obj) {
        this.$emit('data-on-node-click', data, node, obj)
      },
      handleCheckChange(data, node, obj) {
        this.$emit('data-on-check-change', data, node, obj)
      },
      handleCurrentChange(data, node) {
        this.$emit('data-on-current-change', data, node)
      },
      handlerNodeExpand(data, node, obj) {
        this.$emit('data-on-node-expand', data, node, obj)
      },
      handlerNodeCollapse(data, node, obj) {
        this.$emit('data-on-node-collapse', data, node, obj)
      },
      handlerNodeContextMenu(event,data,node,obj){
        this.$emit('data-on-node-contextmenu', data, node, obj)
      },
      handlerCheck(data,node){
        this.$emit('data-on-check', data, node)
      },
      handlerNodeDragStart(node,event){
        this.$emit('data-node-drag-start', node, event)
      },
      handlerNodeDragEnter(node1,node2,event){
        this.$emit('data-node-drag-end', node1, node2, event)
      },
      handlerNodeDragLeave(node1,node2,event){
        this.$emit('data-node-drag-leave', node1, node2, event)
      },
      handlerNodeDragOver(node1,node2,event){
        this.$emit('data-node-drag-over', node1, node2, event)
      },
      handlerNodeDragEnd(node1,node2,type,event){
        this.$emit('data-node-drag-end', node1, node2,type, event)
      },
      handlerNodeDragDrop(node1,node2,type,event){
        this.$emit('data-node-drag-end', node1, node2,type, event)
      }
    }
  }
</script>

<style scoped>

</style>
