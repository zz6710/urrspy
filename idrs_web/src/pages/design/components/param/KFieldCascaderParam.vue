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
      <el-form-item label="多选" v-if="element.selectOptions.indexOf('dataMultiple') > -1">
        <el-select v-model="element.options.dataMultiple" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="样式" v-if="element.selectOptions.indexOf('dataUiType') > -1">
        <el-select v-model="element.options.dataUiType" placeholder="请选择">
          <el-option label="cascader" value="cascader"></el-option>
          <el-option label="panel" value="panel"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="组件大小" v-if="element.selectOptions.indexOf('dataSize') > -1">
        <el-select v-model="element.options.dataSize" placeholder="请选择">
          <el-option label="mini" value="mini"></el-option>
          <el-option label="small" value="small"></el-option>
          <el-option label="medium" value="medium"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示全部等级" v-if="element.selectOptions.indexOf('dataShowAllLevels') > -1">
        <el-select v-model="element.options.dataShowAllLevels" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="多选模式折叠tag" v-if="element.selectOptions.indexOf('dataCollapseTags') > -1">
        <el-select v-model="element.options.dataCollapseTags" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文本分隔符" v-if="element.selectOptions.indexOf('dataValueSeparator') > -1">
        <el-input v-model="element.options.dataValueSeparator"></el-input>
      </el-form-item>
      <el-form-item label="值字段名称" v-if="element.selectOptions.indexOf('dataValueField') > -1">
        <el-input v-model="element.options.dataValueField"></el-input>
      </el-form-item>
      <el-form-item label="显示字段名称" v-if="element.selectOptions.indexOf('dataDisplayField') > -1">
        <el-input v-model="element.options.dataDisplayField"></el-input>
      </el-form-item>
      <el-form-item label="子节点字段名称" v-if="element.selectOptions.indexOf('dataDisplayChild') > -1">
        <el-input v-model="element.options.dataDisplayChild"></el-input>
      </el-form-item>
      <el-form-item label="父节点字段名称" v-if="element.selectOptions.indexOf('dataParentField') > -1">
        <el-input v-model="element.options.dataParentField"></el-input>
      </el-form-item>
      <el-form-item label="上级级别节点" v-if="element.selectOptions.indexOf('dataDiffcondition') > -1">
        <el-input v-model="element.options.dataDiffcondition"></el-input>
      </el-form-item>
      <el-form-item label="搜索" v-if="element.selectOptions.indexOf('dataFileterable') > -1">
        <el-select v-model="element.options.dataFileterable" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="搜索防抖延迟(ms)" v-if="element.selectOptions.indexOf('dataDebounce') > -1">
        <el-input-number v-model="element.options.dataDebounce"></el-input-number>
      </el-form-item>
      <el-form-item label="浮层类名" v-if="element.selectOptions.indexOf('dataType') > -1">
        <el-input v-model="element.options.dataPopperClass"></el-input>
      </el-form-item>
      <el-form-item label="显示数量" v-if="element.selectOptions.indexOf('dataShowNum') > -1">
        <el-select v-model="element.options.dataShowNum" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="字段分隔符" v-if="element.selectOptions.indexOf('dataDisplaySeparator') > -1">
        <el-input v-model="element.options.dataDisplaySeparator"></el-input>
      </el-form-item>
      <el-form-item label="显示数量" v-if="element.selectOptions.indexOf('dataShowNum') > -1">
        <el-select v-model="element.options.dataShowNum" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="菜单展开方式" v-if="element.selectOptions.indexOf('dataExpandTrigger') > -1">
        <el-select v-model="element.options.dataExpandTrigger" placeholder="请选择">
          <el-option label="click" value="click"></el-option>
          <el-option label="hover" value="hover"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否返回数组" v-if="element.selectOptions.indexOf('dataEmitPath') > -1">
        <el-select v-model="element.options.dataEmitPath" placeholder="请选择">
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
      <el-form-item label="返回对象" v-if="element.selectOptions.indexOf('dataOnObject') > -1">
        <el-select v-model="element.options.dataOnObject" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="自定义数据" label-width="auto" v-if="element.selectOptions.indexOf('dataData') > -1">
        <el-table
          :data="element.options.dataData"
          style="width: 100%"
          row-key="value"
          border
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
          <el-table-column
            prop="value"
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
              <!-- <el-button @click="handleOptionsRemove(index)" circle plain type="" size="mini" icon="el-icon-plus" style="padding: 4px;margin-left: 5px;"></el-button> -->
              <el-button @click="handleOptionsRemove(scope.row.value)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px;margin-left: 5px;"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="查询参数" v-if="element.selectOptions.indexOf('dataParams') > -1">
        <el-input v-model="element.options.dataParams"></el-input>
      </el-form-item>
      <el-form-item label="数据来源Action" v-if="element.selectOptions.indexOf('dataAction') > -1">
        <el-input v-model="element.options.dataAction"></el-input>
      </el-form-item>
      <el-form-item label="数据来源graphql" v-if="element.selectOptions.indexOf('dataGraphql') > -1">
        <el-input v-model="element.options.dataGraphql"></el-input>
      </el-form-item>
      <el-form-item label="提交请求的地址(dataUrl)" v-if="element.selectOptions.indexOf('dataUrl') > -1">
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
      <el-form-item label="加载动态数据的方法" v-if="element.selectOptions.indexOf('dataLazyLoad') > -1">
        <el-select v-model="element.options.dataLazyLoad" placeholder="请选择">
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
  
  
    <el-dialog title="添加节点" :visible.sync="dialogFormVisible"
      :append-to-body="true"
      width="30%"
      :modal='false'
    >
      <el-form :model="form" 
        label-width="80px"
        ref="form"
        >
        <el-form-item label="上级节点" prop="parentKey" >
          <el-cascader
            v-model="form.parentKey"
            :options="element.options.dataData"
            :props="{ expandTrigger: 'hover' , emitPath: false, checkStrictly: true}"
            clearable
            ></el-cascader>
        </el-form-item>
        <el-form-item label="key" prop="key" >
          <el-input v-model="form.key" ></el-input>
        </el-form-item>
        <el-form-item label="label" prop="label" >
          <el-input v-model="form.label" ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAddData(element.options.dataData)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import KCodeEditor from '../KCodeEditor.vue';
import SelectOptions from '../base/SelectOptions.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import ParamUtils from '../../utils/param.js';
import FormCustomAttr from '../base/FormCustomAttr.vue';
import KFormItemParam from './KFormItemParam.vue';

export default {
  components: {
    Draggable,
    KCodeEditor,
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
      form: {
        parentKey: '',
        key: '',
        label: ''
      }
    }
  },
  created (){
  },
  mounted () {
    // document.body.ondrop = function (event) {
    //   let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
    //   if (isFirefox) {
    //     event.preventDefault()
    //     event.stopPropagation()
    //   }
    // }
  },
  methods: {
    // 根据key删除组件
    handleOptionsRemove: function(value, data=this.element.options.dataData){
      data.forEach((v, index) => {
        if(v.value == value){
          this.$nextTick(() => {
            data.splice(index, 1);
            console.log(" 匹配到KEY并删除 ", v, v.value, value);
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
        this.element.options.dataData.push({value: this.form.key, label: this.form.label, children: []});
        this.$refs['form'].resetFields();
        this.dialogFormVisible = false;
      } else {
        data.forEach((v, index) => {
          if(v.value == this.form.parentKey){
            console.log(" 匹配到 ", v, data);
            if(!v.children){
              v.children = [];
            }
            this.$set(v.children, v.children.length, {value: this.form.key, label: this.form.label, children: []});
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
