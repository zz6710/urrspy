<template>
  <div class="widget-param"
    v-if="element && element.key"
    :key="element.key"
  >
    <template v-if="element.inForm">
      <el-divider content-position="center">k-form-item</el-divider>
      <KFormItemParam :element="element.formItem"></KFormItemParam>
    </template>
    
    <el-divider content-position="center">组件属性</el-divider>
    <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions>
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="数据字段">
        <el-input v-model="element.model"></el-input>
      </el-form-item>
      <el-form-item label="允许为空" v-if="element.selectOptions.indexOf('dataAllowblank') > -1">
        <el-select v-model="element.options.dataAllowblank" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="禁用" v-if="element.selectOptions.indexOf('dataDisabled') > -1">
        <el-select v-model="element.options.dataDisabled" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否可以清空" v-if="element.selectOptions.indexOf('dataClearable') > -1">
        <el-select v-model="element.options.dataClearable" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="占位文本" v-if="element.selectOptions.indexOf('dataPlaceholder') > -1">
        <el-input v-model="element.options.dataPlaceholder"></el-input>
      </el-form-item>
      <el-form-item label="阻止选择分支节点" v-if="element.selectOptions.indexOf('dataDisableBranchNodes') > -1">
        <el-select v-model="element.options.dataDisableBranchNodes" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="启用搜索功能" v-if="element.selectOptions.indexOf('dataSearchAble') > -1">
        <el-select v-model="element.options.dataSearchAble" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示字段" v-if="element.selectOptions.indexOf('dataDisplayField') > -1">
        <el-input v-model="element.options.dataDisplayField"></el-input>
      </el-form-item>
      <el-form-item label="值字段名称" v-if="element.selectOptions.indexOf('dataValueField') > -1">
        <el-input v-model="element.options.dataValueField"></el-input>
      </el-form-item>
      <el-form-item label="字段分隔符" v-if="element.selectOptions.indexOf('dataDisplaySeparator') > -1">
        <el-input v-model="element.options.dataDisplaySeparator"></el-input>
      </el-form-item>
      <el-form-item label="子节点字段名称" v-if="element.selectOptions.indexOf('dataDisplayChild') > -1">
        <el-input v-model="element.options.dataDisplayChild"></el-input>
      </el-form-item>
      <el-form-item label="父节点字段名称" v-if="element.selectOptions.indexOf('dataParentField') > -1">
        <el-input v-model="element.options.dataParentField"></el-input>
      </el-form-item>
      <el-form-item label="子节点字段名称" v-if="element.selectOptions.indexOf('dataChildField') > -1">
        <el-input v-model="element.options.dataChildField"></el-input>
      </el-form-item>
      <el-form-item label="上级级别节点" v-if="element.selectOptions.indexOf('dataDiffcondition') > -1">
        <el-input v-model="element.options.dataDiffcondition"></el-input>
      </el-form-item>
      <el-form-item label="多选" v-if="element.selectOptions.indexOf('dataMultiple') > -1">
        <el-select v-model="element.options.dataMultiple" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="动态加载子节点" v-if="element.selectOptions.indexOf('dataLazy') > -1">
        <el-select v-model="element.options.dataLazy" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="平面模式" v-if="element.selectOptions.indexOf('dataFlat') > -1">
        <el-select v-model="element.options.dataFlat" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="自动选择后代" v-if="element.selectOptions.indexOf('dataAutoSelectDescendants') > -1">
        <el-select v-model="element.options.dataAutoSelectDescendants" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="自动取消选择后代" v-if="element.selectOptions.indexOf('dataAutoDeselectDescendants') > -1">
        <el-select v-model="element.options.dataAutoDeselectDescendants" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="自动选择祖先" v-if="element.selectOptions.indexOf('dataAutoDeselectAncestors') > -1">
        <el-select v-model="element.options.dataAutoDeselectAncestors" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="自动取消选择祖先" v-if="element.selectOptions.indexOf('dataAutoSelectAncestors') > -1">
        <el-select v-model="element.options.dataAutoSelectAncestors" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分支节点展开级数" v-if="element.selectOptions.indexOf('dataDefaultExpandLevel') > -1">
        <el-input-number v-model="element.options.dataDefaultExpandLevel" ></el-input-number>
      </el-form-item>
      <el-form-item label="未匹配到结果文本" v-if="element.selectOptions.indexOf('dataNoResultsText') > -1">
        <el-input v-model="element.options.dataNoResultsText" ></el-input>
      </el-form-item>
      <el-form-item label="无选项时显示文本" v-if="element.selectOptions.indexOf('dataNoOptionsText') > -1">
        <el-input v-model="element.options.dataNoOptionsText" ></el-input>
      </el-form-item>
      <el-form-item label="显示数量" v-if="element.selectOptions.indexOf('dataShowCount') > -1">
        <el-select v-model="element.options.dataShowCount" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="搜索时显示数量" v-if="element.selectOptions.indexOf('dataShowCountOnSearch') > -1">
        <el-select v-model="element.options.dataShowCountOnSearch" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="只显示搜索结果" v-if="element.selectOptions.indexOf('dataOnlyShowResults') > -1">
        <el-select v-model="element.options.dataOnlyShowResults" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="菜单始终打开" v-if="element.selectOptions.indexOf('dataAlwaysOpen') > -1">
        <el-select v-model="element.options.dataAlwaysOpen" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择后清除搜索输入" v-if="element.selectOptions.indexOf('dataClearOnSelect') > -1">
        <el-select v-model="element.options.dataClearOnSelect" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="自定义数据" label-width="auto">
        <el-table
          :data="element.options.dataData"
          style="width: 100%"
          row-key="id"
          border
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
          <el-table-column
            prop="id"
            label="值"
            width="130">
          </el-table-column>
          <el-table-column
            prop="label"
            label="标签"
            width="90">
          </el-table-column>
          <el-table-column
            align="left">
            <template slot="header" slot-scope="scope">
              <div>
                <el-button type="text" @click="dialogFormVisible = true">添加节点</el-button>
              </div>
            </template>
            <template slot-scope="scope">
              <el-button @click="handleOptionsRemove(scope.row.id)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px;margin-left: 5px;"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item> -->
      <el-form-item label="查询参数" v-if="element.selectOptions.indexOf('dataParams') > -1">
        <el-input v-model="element.options.dataParams"></el-input>
      </el-form-item>
      <el-form-item label="数据来源Action" v-if="element.selectOptions.indexOf('dataAction') > -1">
        <el-input v-model="element.options.dataAction"></el-input>
      </el-form-item>
      <el-form-item label="数据来源graphql" v-if="element.selectOptions.indexOf('dataGraphql') > -1">
        <el-input v-model="element.options.dataGraphql"></el-input>
      </el-form-item>
      <el-form-item label="数据来源dataUrl" v-if="element.selectOptions.indexOf('dataUrl') > -1">
        <el-input v-model="element.options.dataUrl"></el-input>
      </el-form-item>

      <el-form-item label="自定义搜索逻辑" v-if="element.selectOptions.indexOf('dataFilterMethod') > -1">
        <el-select v-model="element.options.dataFilterMethod" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="筛选之前的钩子" v-if="element.selectOptions.indexOf('dataBeforeFilter') > -1">
        <el-select v-model="element.options.dataBeforeFilter" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="加载数据前回调函数" v-if="element.selectOptions.indexOf('dataOnBeforeload') > -1">
        <el-select v-model="element.options.dataOnBeforeload" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="加载数据后回调函数" v-if="element.selectOptions.indexOf('dataOnAfterload') > -1">
        <el-select v-model="element.options.dataOnAfterload" placeholder="请选择">
          <el-option :label="item" :value="item" v-for="(item, index) in getMethodList(element.methods)" :key="item + '_' + index"></el-option>
        </el-select>
      </el-form-item>

      <el-divider content-position="center">自定义属性</el-divider>
      <FormCustomAttr :customAttrs="element.customAttrs"></FormCustomAttr>

      <el-divider content-position="center">函数</el-divider>
      <FormCodeEditor v-for="(item, index) in element.methods" :key="'func_'+index" :item="item" :remove="() => { element.methods.splice(index, 1); }"></FormCodeEditor>
      <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.methods.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加函数</el-button>

      <el-divider content-position="center">事件</el-divider>
      <FormCodeEditor v-for="(item, index) in element.events" :key="'event_'+index" :item="item" event :remove="() => { element.events.splice(index, 1); }"></FormCodeEditor>
      <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.events.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加事件</el-button>

    </el-form>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import SelectOptions from '../base/SelectOptions.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import ParamUtils from '../../utils/param.js';
import FormCustomAttr from '../base/FormCustomAttr.vue';
import KFormItemParam from './KFormItemParam.vue';

export default {
  components: {      
    Draggable,
    SelectOptions,
    FormCodeEditor,
    FormCustomAttr,
    KFormItemParam
  },
  props: ['element', 'selectWidget'],
  inject: ['kFormDesign'],
  data () {
    return {
      dialogFormVisible: false,
    }
  },
  created (){
  },
  mounted () {
  },
  methods: {
    handleOptionsRemove: function(id, data=this.element.options.dataData){
      data.forEach((v, index) => {
        if(v.id == id){
          this.$nextTick(() => {
            data.splice(index, 1);
            console.log(" 匹配到KEY并删除 ", v, v.id, id);
          })
          return false;
        } else if (v.children && v.children.length > 0) {
          this.handleOptionsRemove(value, v.children);
        }
      });
    },
    handleAddData(data) {
      console.log(" 添加节点 ", data, this.form);
      if(!this.form.parentKey){
        this.element.options.dataData.push({id: this.form.key, label: this.form.label, children: []});
        this.$refs['form'].resetFields();
        this.dialogFormVisible = false;
      } else {
        data.forEach((v, index) => {
          if(v.id == this.form.parentKey){
            console.log(" 匹配到 ", v, data);
            if(!v.children){
              v.children = [];
            }
            this.$set(v.children, v.children.length, {id: this.form.key, label: this.form.label, children: []});
            this.$refs['form'].resetFields();
            this.dialogFormVisible = false;
            return false;
          } else if (v.children && v.children.length > 0) {
            this.handleAddData(v.children);
          } else {
            this.dialogFormVisible = false;
          }
        });
      }
    },
    getMethodList: ParamUtils.getMethodList
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

</style>
