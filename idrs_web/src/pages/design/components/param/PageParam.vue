<template>
  <div class="widget-param">
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <template v-if="element.type == 'home'">

        <el-card header="props">
          <KCodeEditor ref="props" v-model="element.props" height="300px"/>
        </el-card>
        <el-card header="页面参数">
          <KCodeEditor ref="data" v-model="element.data" height="300px"/>
        </el-card>
        <el-card header="生命周期函数">
          <FormCodeEditor v-for="(item, index) in element.cycles" :key="'event_'+index" :item="item" :remove="() => { element.cycles.splice(index, 1); }"></FormCodeEditor>
          <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.cycles.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加函数</el-button>
        </el-card>
        <el-card header="CSS增强">
          <el-form-item label="CSS样式">
            <div slot="label">
              <label>CSS样式</label>
              <el-button size="mini" @click="() => {this.$refs.css.open()}">编辑</el-button>
            </div>
            <KCodeEditor ref="css" v-model="element.css"/>
          </el-form-item>
        </el-card>
      </template>

      <template v-if="element.type != 'home'">
        <el-card header="POPUP属性">
          <el-form-item label="ref">
            <el-input v-model="element.ref"></el-input>
          </el-form-item>
          <el-form-item label="页面类型" label-width="auto">
            <el-select v-model="element.options.dataType" placeholder="请选择">
              <el-option label="弹框" value="dialog"></el-option>
              <el-option label="抽屉" value="drawer"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="element.options.dataType=='dialog'?'弹框':'抽屉'+'标题'">
            <el-input type="text" v-model="element.options.dataTitle"></el-input>
          </el-form-item>
          <el-form-item label="宽度比例">
            <el-input type="text" v-model="element.options.dataWidthPercent" placeholder="例如:50%"></el-input>
          </el-form-item>
          <el-form-item label="宽度">
            <el-input type="text" v-model="element.options.dataWidth" placeholder="例如:500px"></el-input>
          </el-form-item>
          <el-form-item label="是否全屏展示">
            <el-select v-model="element.options.dataFullscreen" placeholder="请选择">
              <el-option label="是" :value="true"></el-option>
              <el-option label="否" :value="false"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关闭提醒">
            <el-select v-model="element.options.dataConfirmClose" placeholder="请选择">
              <el-option label="是" :value="true"></el-option>
              <el-option label="否" :value="false"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关闭提醒内容">
            <el-input type="text" v-model="element.options.dataConfirmDescribe" placeholder="默认：是否确认关闭" ></el-input>
          </el-form-item>
          <el-form-item label="抽屉打开方向" v-if="element.options.dataType == 'drawer'">
            <el-select v-model="element.options.dataDirection" placeholder="请选择">
              <el-option label="top" value="top"></el-option>
              <el-option label="bottom" value="bottom"></el-option>
              <el-option label="left" value="left"></el-option>
              <el-option label="right" value="right"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否显示遮罩">
            <el-select v-model="element.options.dataMask" placeholder="请选择">
              <el-option label="是" :value="true"></el-option>
              <el-option label="否" :value="false"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="标题对齐方式" v-if="element.options.dataType == 'dialog'">
            <el-select v-model="element.options.dataTitleAlign" placeholder="请选择">
              <el-option label="left" value="left"></el-option>
              <el-option label="center" value="center"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="点击遮罩层关闭">
            <el-select v-model="element.options.dataCloseOnClickModal" placeholder="请选择">
              <el-option label="是" :value="true"></el-option>
              <el-option label="否" :value="false"></el-option>
            </el-select>
          </el-form-item>
        </el-card>
        <el-card header="自定义属性">
          <FormCustomAttr :customAttrs="element.customAttrs"></FormCustomAttr>
        </el-card>
        <el-card header="事件">
          <FormCodeEditor v-for="(item, index) in element.events" :key="'event_'+index" :item="item" event :remove="() => { element.events.splice(index, 1); }"></FormCodeEditor>
          <el-button type="primary" icon="el-icon-add" style="width:100%" @click="() => { element.events.push({functionName: '', body: '方法名: function(){\n}', propertyName: ''}) }">添加事件</el-button>
        </el-card>
      </template>
    </el-form>
  </div>
</template>

<script>
import Draggable from 'vuedraggable'
import KCodeEditor from '../KCodeEditor.vue';
import FormCodeEditor from '../base/FormCodeEditor.vue';
import FormCustomAttr from '../base/FormCustomAttr.vue';

export default {
  components: {
    Draggable,
    KCodeEditor,
    FormCodeEditor,
    FormCustomAttr
  },
  props: ['element', 'selectWidget'],
  inject: ['kFormDesign'],
  data () {
    return {
    }
  },
  created: function(){
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
    handleOptionsRemove (index) {
      this.element.columns.splice(index, 1)
    },
    handleAddColumn () {
      this.element.columns.push({
        span: '',
        list: []
      })
    },
  },
  watch: {

  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）
  @import '../../styles/cover.scss';
  @import '../../styles/index.scss';

</style>
