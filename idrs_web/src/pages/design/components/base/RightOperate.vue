<template>
  <el-aside class="right-operate">
    <el-tabs :stretch="true" v-model="activeName">
      <el-tab-pane label="组件属性" name="widget">
        <component v-if="kFormDesign.widgetFormSelect.paramType" :is="kFormDesign.widgetFormSelect.paramType" :element="kFormDesign.widgetFormSelect"></component>
        <template v-else >
          <el-button type="info" @click="() => { $refs['data'].open() }">编辑</el-button>
          <el-button type="primary" @click="updateData">更新节点数据</el-button>
          <KCodeEditor ref="data" v-model="widgetFormSelectStr" height="500px"/>
        </template>
      </el-tab-pane>
      <el-tab-pane label="页面属性" name="global">
        <page-param :element="kFormDesign.currPage"></page-param>
      </el-tab-pane>
      <el-tab-pane label="整体配置" name="config">
        <config-param :element="kFormDesign.config"></config-param>
      </el-tab-pane>
    </el-tabs>
  </el-aside>
</template>

<script>
import KFieldTextParam from '@/pages/design/components/param/KFieldTextParam';
import KFieldBswitchParam from '@/pages/design/components/param/KFieldBswitchParam';
import KFieldRadioParam from '@/pages/design/components/param/KFieldRadioParam';
import KFieldCheckboxParam from '@/pages/design/components/param/KFieldCheckboxParam';
import KFieldTimeParam from '@/pages/design/components/param/KFieldTimeParam';
import KFieldDateParam from '@/pages/design/components/param/KFieldDateParam';
import KFieldSelectParam from '@/pages/design/components/param/KFieldSelectParam';
import KFieldCascaderParam from '@/pages/design/components/param/KFieldCascaderParam';
import KFieldRichParam from '@/pages/design/components/param/KFieldRichParam';
import KFieldUploadParam from '@/pages/design/components/param/KFieldUploadParam';
import KStepsParam from '@/pages/design/components/param/KStepsParam';
import ElTabsParam from '@/pages/design/components/param/ElTabsParam';
import ElCollapseParam from '@/pages/design/components/param/ElCollapseParam';
import ElDividerParam from '@/pages/design/components/param/ElDividerParam';
import KCustomerParam from '@/pages/design/components/param/KCustomerParam';

import KFieldTreeParam from '@/pages/design/components/param/KFieldTreeParam';
import ElRowParam from '@/pages/design/components/param/ElRowParam';
import KFormParam from '@/pages/design/components/param/KFormParam';
import KFormSearchParam from '@/pages/design/components/param/KFormSearchParam';
import KFormSearchCustomizeParam from '@/pages/design/components/param/KFormSearchCustomizeParam';
import KGridParam from '@/pages/design/components/param/KGridParam';
import KBtnParam from '@/pages/design/components/param/KBtnParam';
import PageParam from '@/pages/design/components/param/PageParam';
import ConfigParam from '@/pages/design/components/param/ConfigParam';
import BaseParam from '@/pages/design/components/param/BaseParam';

import KCodeEditor from '@/pages/design/components/KCodeEditor.vue';

export default {
  components: {
    KFieldTextParam,
    KFieldBswitchParam,
    KFieldRadioParam,
    KFieldCheckboxParam,
    KFieldTimeParam,
    KFieldDateParam,
    KFieldSelectParam,
    KFieldCascaderParam,
    KFieldRichParam,
    KFieldUploadParam,
    KStepsParam,
    ElTabsParam,
    ElCollapseParam,
    ElDividerParam,
    KFieldTreeParam,
    ElRowParam,
    KFormParam,
    KFormSearchParam,
    KFormSearchCustomizeParam,
    KGridParam,
    KBtnParam,
    PageParam,
    ConfigParam,
    KCodeEditor,
    BaseParam,
    KCustomerParam,
  },
  props: {
  },
  inject: ['kFormDesign'],
  data () {
    return {
      activeName: 'config',
      widgetFormSelectStr: '',
    }
  },
  created() {
  },
  mounted () {
  },
  methods: {
    updateData: function(){
      Object.assign(this.kFormDesign.widgetFormSelect, JSON.parse(this.widgetFormSelectStr));
    }
  },
  watch: {
    'kFormDesign.widgetFormSelect': {
      handler() {
        if(this.kFormDesign.widgetFormSelect.key) {
          this.activeName = 'widget'
        } else {
          this.activeName = 'config'
        }
        this.widgetFormSelectStr = JSON.stringify(this.kFormDesign.widgetFormSelect, null, 4);
      },
      deep: true
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/pages/design/styles/variable.scss';

.right-operate {
  // height: calc(100vh - #{$top-height});
  // overflow: hidden;
  position: relative;

  /deep/ .el-tabs__header {
    margin-bottom: 0;
  }
  /deep/ .el-tabs__content {
    height: calc(100vh - #{$top-height-tab});
    overflow: auto;


    /* 设置滚动条的样式 */
    &::-webkit-scrollbar {
      width: 8px;
    }
    /* 滚动槽 */
    &::-webkit-scrollbar-track {
      -webkit-box-shadow: rgb(255,255,255);
      border-radius:10px;
    }
    /* 滚动条滑块 */
    &::-webkit-scrollbar-thumb {
      border-radius:10px;
      background:rgba(0,0,0,0.1);
      -webkit-box-shadow:inset006pxrgba(0,0,0,0.2);
    }
    &::-webkit-scrollbar-thumb:window-inactive {
      background:rgba(0,0,0,0.2);
    }
  }

  .el-header{
    border-bottom: solid 2px #e4e7ed;
    padding: 0 5px;
  }

  .config-tab{
    height: 45px;
    line-height: 45px;
    display: inline-block;
    width: 145px;
    text-align: center;
    font-size: 14px;
    font-weight: 500;
    position: relative;
    cursor: pointer;

    &.active{
      border-bottom: solid 2px $primary-color;
    }
  }

  .config-content{
    padding: 10px;

    .el-form-item__label{
      padding: 0;
      font-weight: 500;
    }

    .el-form-item {
      border-bottom: solid 1px #e1e1e1;
      padding-bottom: 10px;
    }
  }

  .ghost{
    background: #fff;
    border: 1px dashed $primary-color;

    &::after{
      background: #fff;
      display: block;
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
    }
  }

  ul{
    margin: 0;
    padding: 0;
  }

  li.ghost{
    list-style: none;
    font-size: 0;
    display: block;
    position: relative;
  }
}
</style>
