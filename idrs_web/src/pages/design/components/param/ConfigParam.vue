<template>
  <div class="widget-param">
    <el-form ref="form" :model="element" label-position="top" label-width="80px">
      <el-card header="">
        <el-form-item label="功能名称">
          <el-input v-model="element.name"></el-input>
        </el-form-item>
        <el-form-item label="加载模板">
          <el-select style="width: 212px" v-model="selectVal" placeholder="请选择">
             <el-option
              v-for="(item, index) in funcLists"
              :key="item.id"
              :label="item.name"
              :value="index">
            </el-option>
          </el-select>
          <el-button @click="loadModule">加载</el-button>
        </el-form-item>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import { clone } from "lodash";
import Draggable from 'vuedraggable'
import KCodeEditor from '../KCodeEditor.vue';

export default {
  components: {
    Draggable,
    KCodeEditor
  },
  props: ['element', 'selectWidget'],
  inject: ['kFormDesign'],
  data () {
    return {
      funcLists: [],
      selectVal: ''
    }
  },
  mounted () {
    this.getFuncList();
    document.body.ondrop = function (event) {
      let isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1
      if (isFirefox) {
        event.preventDefault()
        event.stopPropagation()
      }
    }
  },
  methods: {
    loadModule() {
      if(this.selectVal !== '') {
        this.$confirm("此操作将清空当前页面组件，并加载对应模板内容，是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .then(() => {
          // console.log(this.funcLists[this.selectVal]);
          // console.log(this.kFormDesign);
          this.$set(this.kFormDesign, 'config', this.funcLists[this.selectVal])
          this.$set(this.kFormDesign, 'pageList', JSON.parse(this.kFormDesign.config.json))
          this.$set(this.kFormDesign, 'currPage', this.kFormDesign.pageList[0])
          // this.config = JSON.parse(configStr);
          // this.pageList = JSON.parse(this.config.json);
          // this.currPage = this.pageList[0];

        })
        .catch(() => {});
      } else {
        this.$message.warning('请选择加载模板')
      }

    },
    async getFuncList() {
      // console.log(this.kFormDesign.sysVersion);
      // console.log(this.kFormDesign.moduleId);
			let res = await this.httpUtil.comnQuery({
				action: "LowCodeConfig.page",
				params: {
					sysVersion: this.kFormDesign.sysVersion,
					moduleId: this.kFormDesign.moduleId,
          template: 1,
				},
			});
			this.funcLists = clone(res.rows);
		},
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
