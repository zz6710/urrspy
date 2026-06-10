<template>
  <div class="widget-param"
    v-if="element && element.key"
    :key="element.key"
  >
    <SelectOptions v-model="element.selectOptions" :type="element.type"></SelectOptions>
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-form-item label="ref">
        <el-input v-model="element.ref"></el-input>
      </el-form-item>
      <el-form-item label="动态表头模块加载" v-if="element.selectOptions.indexOf('dataServer') > -1">
        <span>所属服务：</span>
        <el-select style="width: 214px" v-model="element.options.dataServer" placeholder="请选择" @change="changeSelect" filterable>
          <el-option v-for="item in sysLists" :key="item.app_name" :value="item.app_name">{{item.app_name}}</el-option>
        </el-select>
        <span v-if="element.options.dataServer">所属模块：</span>
        <el-select v-if="element.options.dataServer" style="width: 214px;margin-top: 5px;" v-model="element.options.dataEntity" placeholder="请选择" filterable>
          <el-option v-for="item in moduleLists" :key="item.model_name" :value="item.model_name">{{item.model_name}}</el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="表格名称" v-if="element.selectOptions.indexOf('dataTitle') > -1">
        <el-input v-model="element.options.dataTitle"></el-input>
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
      <el-form-item label="固定参数" v-if="element.selectOptions.indexOf('dataParams') > -1">
        <el-input v-model="element.options.dataParams"></el-input>
      </el-form-item>
      <el-form-item label="纵向边框" v-if="element.selectOptions.indexOf('dataBorder') > -1">
        <el-select v-model="element.options.dataBorder" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示标题" v-if="element.selectOptions.indexOf('dataShowSubscript') > -1">
        <el-select v-model="element.options.dataShowSubscript" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签文本宽度" v-if="element.selectOptions.indexOf('dataRender') > -1">
        <el-input v-model="element.options.dataRender"></el-input>
      </el-form-item>
      <el-form-item label="对齐方式" v-if="element.selectOptions.indexOf('dataAlign') > -1">
        <el-select v-model="element.options.dataAlign" placeholder="请选择">
          <el-option label="left" value="left"></el-option>
          <el-option label="center" value="center"></el-option>
          <el-option label="right" value="right"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="每页记录数" v-if="element.selectOptions.indexOf('dataPageSize') > -1">
        <el-input-number v-model="element.options.dataPageSize"></el-input-number>
      </el-form-item>
      <el-form-item label="自动加载数据" v-if="element.selectOptions.indexOf('dataAutoload') > -1">
        <el-select v-model="element.options.dataAutoload" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="操作列" v-if="element.selectOptions.indexOf('dataOperateColumn') > -1">
        <el-select v-model="element.options.dataOperateColumn" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="操作列位置" v-if="element.selectOptions.indexOf('dataOperateColumnPosition') > -1">
        <el-select v-model="element.options.dataOperateColumnPosition" placeholder="请选择">
          <el-option label="第一列" value="first"></el-option>
          <el-option label="最后一列" value="end"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="行详情" v-if="element.selectOptions.indexOf('dataDisplay') > -1">
        <el-select v-model="element.options.dataDisplay" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="操作列固定" v-if="element.selectOptions.indexOf('dataFixed') > -1">
        <el-select v-model="element.options.dataFixed" placeholder="请选择">
          <el-option label="left" value="left"></el-option>
          <el-option label="right" value="right"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="树表格" v-if="element.selectOptions.indexOf('dataShowTree') > -1">
        <el-select v-model="element.options.dataShowTree" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="显示复选框" v-if="element.selectOptions.indexOf('dataCheckbox') > -1">
        <el-select v-model="element.options.dataCheckbox" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="复选框宽度" v-if="element.selectOptions.indexOf('dataCheckboxWidth') > -1">
        <el-input v-model="element.options.dataCheckboxWidth"></el-input>
      </el-form-item>
      <el-form-item label="多选" v-if="element.selectOptions.indexOf('dataCheckboxMultiple') > -1">
        <el-select v-model="element.options.dataCheckboxMultiple" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="斑马纹" v-if="element.selectOptions.indexOf('dataStripe') > -1">
        <el-select v-model="element.options.dataStripe" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="展开所有行" v-if="element.selectOptions.indexOf('dataExpandAll') > -1">
        <el-select v-model="element.options.dataExpandAll" placeholder="请选择">
          <el-option label="是" :value="true"></el-option>
          <el-option label="否" :value="false"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="表格高度" v-if="element.selectOptions.indexOf('dataHeight') > -1">
        <el-input v-model="element.options.dataHeight"></el-input>
      </el-form-item>
      <el-form-item label="树表格ID字段" v-if="element.selectOptions.indexOf('dataTreeId') > -1">
        <el-input v-model="element.options.dataTreeId"></el-input>
      </el-form-item>
      <el-form-item label="分页功能菜单" v-if="element.selectOptions.indexOf('dataPaginationLayout') > -1">
        <el-select v-model="element.options.dataPaginationLayout" multiple placeholder="请选择">
          <el-option label="total" value="total"></el-option>
          <el-option label="sizes" value="sizes"></el-option>
          <el-option label="prev" value="prev"></el-option>
          <el-option label="pager" value="pager"></el-option>
          <el-option label="next" value="next"></el-option>
          <el-option label="jumper" value="jumper"></el-option>
        </el-select>
      </el-form-item>

      <el-divider content-position="center">表格字段参数</el-divider>
      <el-form-item label="" label-width="auto">
          <draggable tag="ul" :list="element.list"
            v-bind="{group:{ name:'options'}, ghostClass: 'ghost',handle: '.drag-item'}"
            handle=".drag-item"
            class="column_ul"
          >
            <li v-for="(item, index) in element.list" :key="index">
              <i class="drag-item" style="font-size: 16px;margin: 0 5px;cursor: move;"><i class="iconfont icon-icon_bars"></i></i>
              <label style="margin-right: 5px;" v-if="item.key">
                <el-input style="width:90px" size="mini" placeholder="中文名" v-model="item.options.dataHeader"></el-input>
                <el-input style="width:90px;" size="mini" placeholder="英文名" v-model="item.options.dataName"></el-input>
                <el-input style="width:90px;" size="mini" placeholder="宽度" v-model="item.options.dataWidth"></el-input>
              </label>
              <!-- <el-button @click="handleOptionsRemove(index)" circle plain type="danger" size="mini" icon="el-icon-minus" style="padding: 4px;margin-left: 5px;"></el-button> -->
              <!-- <k-btn class="md-primary md-just-icon md-simple" >
                <md-icon>edit</md-icon>
              </k-btn> -->
              <el-button type="primary" size="mini" icon="el-icon-edit" circle @click="handleOptionsEdit(item)"></el-button>
              <el-button type="danger" size="mini" icon="el-icon-delete" circle @click="handleOptionsRemove(index)"></el-button>
              <!-- <k-btn class="md-danger md-just-icon md-simple" @click="handleOptionsRemove(index)">
                <md-icon>delete</md-icon>
              </k-btn> -->
              <!-- </el-radio> -->
            </li>
          </draggable>
        <!-- </el-radio-group> -->
        <!-- <div style="margin-left: 22px;">
          <el-button type="text" @click="handleAddOption">添加选项</el-button>
        </div> -->
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

    <!-- 字段信息修改 -->
    <k-grid-column-param
      v-if="showDialog"
      :showDialog.sync="showDialog"
      :element="current"
    />



  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import KGridColumnParam from './KGridColumnParam'

import SelectOptions from '../base/SelectOptions.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import ParamUtils from '../../utils/param.js';
import FormCustomAttr from '../base/FormCustomAttr.vue';
import { clone } from "lodash";

export default {
  components: {
    Draggable,
    KGridColumnParam,
    SelectOptions,
    FormCodeEditor,
    FormCustomAttr
  },
  props: ['element', 'selectWidget'],
  inject: ['kFormDesign'],
  data () {
    return {
      demoData: "",
      showDialog: false,
      current: {},
      sysLists: [],
      selectSys: '',
      moduleLists: [],
    }
  },
  created (){
    if(this.element.options.dataData){
      this.demoData = JSON.stringify(this.element.options.dataData);
    }
    this.httpUtil.ajax({
      url: "/base/ServerModel/getAppNames.json",
    }).then(res => {
      if(res.success) {
        this.sysLists = clone(res.rows)
      }
    })
    if(this.element.options.dataEntity) {
      this.httpUtil.ajax({
        url: "/base/ServerModel/getModelNames.json",
        params: {appName: this.element.options.dataServer}
      }).then(res => {
        if(res.success) {
          this.moduleLists = clone(res.rows)
        }
      })
    }
  },
  mounted () {
    document.body.ondrop = function (event) {
      let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
      if (isFirefox) {
        event.preventDefault()
        event.stopPropagation()
      }
    }
  },
  methods: {
    changeSelect(val) {
      this.$set(this.element.options, 'dataEntity', null)
      // this.element.options.dataEntity
      this.httpUtil.ajax({
        url: "/base/ServerModel/getModelNames.json",
        params: {appName: val}
      }).then(res => {
        if(res.success) {
          this.moduleLists = clone(res.rows)
        }
      })

    },
    handleOptionsEdit: function(item){
      console.log(" item ", item);
      this.current = item;
      this.showDialog = true;
    },
    updateDemoData: function(){
      console.log( this.element.options.dataData );
      this.element.options.dataData = JSON.parse(this.demoData);
      console.log( this.element.options.dataData );


      let target = this.getParentRef(this.element.ref);
      console.log(target);
      if (target && target.$options.name == "KGrid") {
        let params = {};

        for (let field in this.value) {
          let valueElement = this.value[field];
          if (valueElement) {
            params[field] = valueElement;
          }
        }

        let re = this.$refs.searchForm.validate();
        if (re === false) {
          return;
        }

        target.load(params)
      } else {
        console.error("data-target不存在或data-target不是KGrid组件.")
      }
    },

    //根据引用名获取父引用对象
    getParentRef(ref) {
      var parent = this.$parent || this.$root;
      var $ref = parent.$refs[ref];

      while (parent && !$ref) {
        parent = parent.$parent;

        if (parent) {
          $ref = parent.$refs[ref];
        }
      }
      return $ref;
    },

    handleOptionsRemove (index) {
      console.log(" remove index ", index);
      this.element.list.splice(index, 1)
    },
    // handleAddOption () {
    //   this.element.options.dataData.push({
    //     value:  'key_' + Date.parse(new Date()) + '_' + Math.ceil(Math.random() * 99999),
    //     label: "新选项"
    //   })
    // },
  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

  .column_ul {
    overflow-x: auto;
		white-space:nowrap;
  }

</style>
