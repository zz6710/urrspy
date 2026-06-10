<template>
  <treeselect ref="treeselect" :value="dataValue" :multiple="dataMultiple" :placeholder="dataPlaceholder" :options="options"
              :normalizer="normalizer"
              :allowSelectingDisabledDescendants="false"
              :disable-branch-nodes="dataDisableBranchNodes === true || dataDisableBranchNodes === 'true'"
              :flat="dataFlat"
              :searchable="dataSearchAble"
              :showCount="dataShowCount"
              :showCountOnSearch="dataShowCountOnSearch"
              :branchNodesFirst="true"
              :flattenSearchResults="dataOnlyShowResults"
              :autoSelectDescendants="dataAutoSelectDescendants"
              :autoDeselectDescendants="dataAutoDeselectDescendants"
              :autoDeselectAncestors="dataAutoDeselectAncestors"
              :autoSelectAncestors="dataAutoSelectAncestors"
              :defaultExpandLevel="dataDefaultExpandLevel"
              :noOptionsText="dataNoOptionsText"
              :noResultsText="dataNoResultsText"
              :load-options="loadOptions"
              :alwaysOpen="dataAlwaysOpen"
              :clearOnSelect="dataClearOnSelect"
              @input="_handleInput"
              @open="open"
              @close="close"
              @select="select"
              @deselect="deselect"
              @search-change="searchChange">
<!--    <slot name="label"></slot>-->
    <label slot="option-label" slot-scope="{ node, shouldShowCount, count, labelClassName, countClassName }">
                {{ node.label }}
      <span v-if="shouldShowCount" :class="countClassName">({{ count }})</span>
    </label>
  </treeselect>
</template>

<script>
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import props from '@/components/k-element/common/k-field-props.js'
  import event from '@/components/k-element/common/k-field-event.js'
  import treent from '@/components/k-element/common/k-field-treent.js'
  import emitter from "@/components/k-element/common/k-emitter.js";
    export default {
        name: "k-field-tree",
        components:{Treeselect},
        mixins: [props(), event(), emitter(), treent],
        props:{
          value: {
            type: [Array, String, Number],
            default:null
          },
          dataUiType: {
            type: String,
            default: 'cascader'
          },
          dataDisableBranchNodes: {
            type: [Boolean, String],
            default: false
          },
          dataFilterMethod: {
            type: Function,
            default: undefined
          },
          dataSearchAble:{
            type:Boolean,
            default:true
          },
          dataBeforeFilter: {
            type: Function,
            default: undefined
          },
          // 显示字段，可同时多个
          dataDisplayField: {
            type: String,
            default: undefined
          },
          // 值字段
          dataValueField: {
            type: String,
            default: undefined
          },
          dataPlaceholder:{
            type:String,
            default:"Select..."
          },
          // 字段下标
          dataDisplayChild: {
            type: String,
            default: undefined
          },
          // 父节点
          dataParentField: {
            type: String,
            default: undefined
          },
          // 父节点
          dataChildField: {
            type: String,
            default: undefined
          },
          //上级级别节点
          dataDiffcondition: {
            type: String,
            default: undefined
          },
          dataOnBeforeload: {
            type: Function,
            default: undefined
          },
          dataOnAfterload: {
            type: Function,
            default: undefined
          },
          dataMultiple: {
            type: [Boolean, String],
            default: true
          },
          dataLazy: {
            type: [Boolean, String],
            default: undefined
          },
          dataFlat:{
            type:Boolean,
            default:true,
          },
          dataAutoSelectDescendants:{
            type:Boolean,
            default:false,
          },
          dataAutoDeselectDescendants:{
            type:Boolean,
            default:false,
          },
          dataAutoDeselectAncestors:{
            type:Boolean,
            default:false,
          },
          dataAutoSelectAncestors:{
            type:Boolean,
            default:false
          },
          dataDefaultExpandLevel:{
            type:Number,
            default:0
          },
          dataNoResultsText:{
            type:String,
            default:"没有匹配的结果"
          },
          dataNoOptionsText:{
            type:String,
            default:"没有内容"
          },
          dataShowCount:{
            type:Boolean,
            default:false
          },
          dataShowCountOnSearch:{
            type:Boolean,
            default:false
          },
          //只显示搜索结果
          dataOnlyShowResults:{
            type:Boolean,
            default:false
          },
          dataAlwaysOpen:{
            type:Boolean,
            default:false
          },
          dataClearOnSelect:{
            type:Boolean,
            default:true
          }
        },
        data(){
          return{
            value2:"",
            options: [ {
              id: 'success',
              label: 'With children',
              // Declare an unloaded branch node.
              children: null,
            }, {
              id: 'no-children',
              label: 'With no children',
              children: null,
            }, {
              id: 'failure',
              label: 'Demonstrates error handling',
              children: null,
            } ],
            params: {},
            graphql: String,
            props: {},
            dataType: false
          }
        },
      computed:{
          dataValue(){
             return this.value
          }
      },
      watch:{
      },
      created() {
        // 配置验证
        if ((this.dataAction || this.dataGraphql) && (this.dataLazy === true || this.dataLazy === 'true') && !this.dataParentField) {
          console.error('没有定义data-parent-field,数据加载失败!')
          return
        }
        if ((this.dataAction || this.dataGraphql) && (this.dataLazy === true || this.dataLazy === 'true') && !this.dataChildField) {
          console.error('没有定义data-child-field,数据加载失败!')
          return
        }
        if (this.dataParams) {
          this.params = typeof this.dataParams === 'string' ? Tools.str2Json(this.dataParams) : this.dataParams === undefined ? {} : this.dataParams
        }

        if ((this.dataAction || this.dataGraphql) && (this.dataLazy === true || this.dataLazy === 'true')  && !this.params.hasOwnProperty(this.dataParentField)) {
          console.error('data-params中没有定义data-parent-field属性，数据加载失败!')
          return
        }

        if ((this.dataAction || this.dataGraphql) && !(this.dataLazy === true || this.dataLazy === 'true') && !this.dataDiffcondition) {
          console.error('没有定义data-diffcondition参数,加载失败!')
          return
        }

        if (this.dataAction && !(this.dataLazy === true || this.dataLazy === 'true') && this.dataDiffcondition) {
          this.params.diffcondition = this.dataDiffcondition
        }
        if (this.dataAutoLoad === true || this.dataAutoLoad === 'true') {
          this.loadData()
        }
      },
      methods:{
        focus(){
          document.getElementsByClassName("vue-treeselect__input")[0].focus()
        },
        loadData() {
          if (this.dataOnBeforeload) {
            this.dataOnBeforeload(this.params)
          }
          this.options = []
          if (this.dataAction) { // 通过Action加载
            this.dataLazy ?  this._loadActionData() : this.loadActionDataTree()
          } else if (this.dataGraphql) { // 通过Graphql加载
            this.graphql = this.dataLazy ? this.dataGraphql : this.getGraphqlUrl(this.dataGraphql, this.dataDiffcondition)
            if(this.dataLazy){
              this._loadGraphqlData()
            }else{
              this.loadGraphqlData()
            }
          } else if (this.dataData) { // 直接指定源数据
            this.options = this.dataType ? this.handleData(this.dataData) : this.dataData
          } else {
            console.error('不支持的数据获取方式，请检查k-field-cascader的元素属性配置')
          }
          if (this.dataOnAfterload) {
            this.dataOnAfterload(this.options)
          }
        },
        loadOptions({ action, parentNode, callback }) {
          if (action === "LOAD_CHILDREN_OPTIONS") {
                this.params[this.dataParentField]=parentNode[this.dataChildField]
                if(this.dataAction){
                  this.httpUtil.comnQuery({
                    action: this.dataAction,
                    params: this.params
                  }).then(data => {
                    let childs = this.handleData(data.rows);
                    if(childs.length==0){
                      parentNode[this.dataDisplayChild]=undefined
                      callback()
                      return
                    }
                    childs.map(child=>{
                      this.$set(child,this.dataDisplayChild,null)
                    })
                    parentNode[this.dataDisplayChild] = []
                    parentNode[this.dataDisplayChild].push(...childs)
                    callback()
                  });
                }else{
                  this.httpUtil.graphqlQurey({
                    graphql: this.graphql,
                    params: this.params
                  }).then(data => {
                    let graphqlFirst = this.graphql.substring(this.graphql.indexOf("{") + 1, this.graphql.indexOf("("));
                    let childs = this.handleData(data[graphqlFirst].rows);
                    if(childs.length==0){
                      parentNode[this.dataDisplayChild]=undefined
                      callback()
                      return
                    }
                    childs.map(child=>{
                      this.$set(child,this.dataDisplayChild,null)
                    })
                    parentNode[this.dataDisplayChild] = []
                    parentNode[this.dataDisplayChild].push(...childs)
                    callback()
                  });
                }
          }
        },
        _loadActionData(){
          this.httpUtil.comnQuery({
            action: this.dataAction,
            params: this.params
          }).then(data => {
            this.options = this.handleData(data.rows);
            this.options.map(option=>{
              this.$set(option,this.dataDisplayChild,null)
            })
          });
        },
        _loadGraphqlData(){
          this.httpUtil.graphqlQurey({
            graphql: this.graphql,
            params: this.params
          }).then(data => {
            let graphqlFirst = this.graphql.substring(this.graphql.indexOf("{") + 1, this.graphql.indexOf("("));
            this.options = this.handleData(data[graphqlFirst].rows);
            this.options.map(option=>{
              this.$set(option,this.dataDisplayChild,null)
            })
          });
        },
        open(instanceId){
          this.$emit('data-on-open')
        },
        close(value, instanceId){
          this.$emit('data-on-close', value)
        },
        _handleInput(value, instanceId){
          this.handleInput(value)
        },
        select(node, instanceId){
          this.$emit('data-on-select',node)
        },
        deselect(node, instanceId){
          this.$emit('data-on-deselect',node)
        },
        searchChange(searchQuery, instanceId){
          this.$emit('data-on-search-hange',searchQuery)
        },
        normalizer(node) {
          return {
            id: node[this.dataValueField],
            label: node[this.dataDisplayField],
            children: node[this.dataDisplayChild] ,
          }
        },
        clear(){
          if(this.dataMultiple==true||this.dataMultiple=="true"){
            this.handleInput([]);
          }
          else{
            this.handleInput(null);
          }
        },
      }
    }
</script>

<style lang="scss" scoped>
  ::v-deep .vue-treeselect__option--highlight{
    background-color: #9c27b0 !important;
    label{
      color: white !important;
    }
  }

  ::v-deep .vue-treeselect__list-item{
    margin: 6px 0px;
    padding: 0 2px;
  }

  ::v-deep .vue-treeselect__option{
     height: 30px;
     border-radius: 5px;
    ::v-deep.vue-treeselect__option-arrow-container{
      width: 35px !important;
      ::v-deep.vue-treeselect__option-arrow{
        width: 11px !important;
        height: 11px !important;
      }
    }

    ::v-deep .vue-treeselect__label-container {
      margin-top: 5px;
    }
  }

  //选中的文样式
  ::v-deep .vue-treeselect__multi-value-item{
    height: 20px;
    padding: 0px;
    border-color: #e9e9eb;
    border-radius: 4px;
    border-style: solid;
    border-width: 1px;
    background: #f4f4f5 !important;
    color: #909399;
    display: flex;
    justify-content: center;
    align-items: center;
   //选中的×样式
    ::v-deep .vue-treeselect__value-remove{
      width: 16px;
      height: 16px;
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 50%;
      background-color: #C0C4CC;
      color: #909399 !important;
    }

    ::v-deep .vue-treeselect__multi-value-label{
      display: block;
      max-width: 100px;
      overflow:hidden; /*超出部分隐藏*/
      white-space:nowrap; /*让文本强制在一行不换行*/
      text-overflow:ellipsis;/*显示省略号来代表被修剪的文本*/
    }

    ::v-deep .vue-treeselect__memu{
      display: flex !important;
      z-index: 999 !important;
    }
  }


  ::v-deep .vue-treeselect__menu{
    overflow-x: hidden;
    overflow-y: auto;
    color: #000;
    border-color: #ebeef5;
    border-bottom-right-radius: 8px;
    border-bottom-left-radius: 8px;
    font-size: .7rem;
    font-family: "\5FAE\8F6F\96C5\9ED1",Helvetica,"黑体",Arial,Tahoma;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  }
  /*滚动条样式*/
  ::v-deep .vue-treeselect__menu::-webkit-scrollbar {
    width: 4px;
    /*height: 4px;*/
  }
  ::v-deep .vue-treeselect__menu::-webkit-scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
    background: rgba(0,0,0,0.2);
  }
  ::v-deep .vue-treeselect__menu::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
    border-radius: 0;
    background: rgba(0,0,0,0.1);

  }

  ::v-deep .vue-treeselect__menu{
    label{
      position: static;
      font-size: 14px !important;
      display: block;
      width: 300px;
      overflow:hidden; /*超出部分隐藏*/
      white-space:nowrap; /*让文本强制在一行不换行*/
      text-overflow:ellipsis;/*显示省略号来代表被修剪的文本*/
    }
  }
</style>
