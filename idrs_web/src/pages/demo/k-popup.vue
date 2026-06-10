<template>
  <div>
    <k-field-code>
      <textarea>

属性：
  data-mask：是否显示遮罩，默认true
  data-title: 表单标题
  data-title-align: dialog弹出框标题对齐方式：left/center 默认center，仅对data-type=dialog
  data-type: 弹窗类型。dialog/drawer，默认dialog
  data-direction: 抽屉打开的方向，top/bottom/left/right 默认right，仅对data-type=drawer有效
  data-confirm-close: 弹窗关闭提醒，默认false
  data-confirm-describe: 弹窗关闭提醒内容，默认：是否确认关闭
  data-width: 弹窗宽度
  data-width-percent：弹窗宽度比例
  data-fullscreen: 是否全屏展示



事件：
  data-before-close: 关闭前事件，如果有返回内容，则弹窗提示内容，并且不关闭
  data-open: 弹窗打开事件
  data-close: 弹窗关闭事件


函数：
    popup(): 打开弹窗
    close(): 关闭弹窗
          </textarea>
    </k-field-code>
    <el-divider/>

    <h1>dialog弹出框：</h1>
    <br/>data-title-align:
    <br/><k-field-radio  v-model="titleAlign" :data-data="titleAlignOptions" />

    <br/>data-mask:
    <br/><k-field-radio  v-model="maskValue" :data-data="maskOptions" />

    <br/>data-confirm-close:
    <br/><k-field-radio  v-model="confirmCloseValue" :data-data="confirmCloseOptions" />

    <br/>data-width:
    <br/><k-field-radio  v-model="widthValue" :data-data="widthOptions" />

    <br/>data-width-percent:
    <br/><k-field-radio  v-model="widthPercentValue" :data-data="widthPercentOptions" />

    <br/>data-fullscreen:
    <br/><k-field-radio  v-model="fullscreenValue" :data-data="fullscreenOptions" />

    <br/>
    <k-btn data-functype="POPUP" data-target="dialog">弹出</k-btn>
    <k-popup ref="dialog" data-title="弹出框dialog"
             :data-mask="maskValue"
             data-confirm-describe="是否关闭弹出框dialog"
             :data-title-align="titleAlign"
             @data-open="open"
             :data-confirm-close = "confirmCloseValue"
             @data-close="close"
             :data-width="widthValue"
             :data-width-percent="widthPercentValue"
            :data-fullscreen="fullscreenValue">
      <k-form :data-col="1" :data-model="formData">
        <k-form-item label="食物1">
          <k-field-select v-model="formData.selectValue1" :data-data="options"></k-field-select>
        </k-form-item>
        <k-form-item label="食物2">
          <k-field-select v-model="formData.selectValue2" :data-data="options"></k-field-select>
        </k-form-item>
        <k-form-footer>
          <k-btn data-functype="CLOSE">关闭</k-btn>
          <k-btn data-functype="SUBMIT" data-action="User.findUsers" :data-confirm="true">提交</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <el-divider/>

    <h1>drawer抽屉：</h1>
    <br/>data-direction:
    <br/><k-field-radio  v-model="directionValue" :data-data="directionOptions" />
    <br/>data-mask:
    <br/><k-field-radio  v-model="maskValue" :data-data="maskOptions" />

    <br/>data-confirm-close:
    <br/><k-field-radio  v-model="confirmCloseValue" :data-data="confirmCloseOptions" />

    <br/>data-width:
    <br/><k-field-radio  v-model="widthValue" :data-data="widthOptions" />

    <br/>data-width-percent:
    <br/><k-field-radio  v-model="widthPercentValue" :data-data="widthPercentOptions" />

    <br/>data-fullscreen:
    <br/><k-field-radio  v-model="fullscreenValue" :data-data="fullscreenOptions" />

    <br/>
    <k-btn data-functype="POPUP" data-target="drawer">弹出</k-btn>
    <k-popup ref="drawer" data-title="drawer"
             :data-mask="maskValue"
             data-type="drawer"
             :data-direction="directionValue"
             :data-confirm-close="confirmCloseValue"
             data-confirm-describe="是否关闭drawer"
             data-title-align="center"
             :data-width="widthValue"
             :data-width-percent="widthPercentValue"
             :data-fullscreen="fullscreenValue"
             @data-open="open"
             @data-close="close">
      <k-form :data-col="1" :data-model="formData">
        <k-form-item label="食物1">
          <k-field-select v-model="formData.selectValue1" :data-data="options"></k-field-select>
        </k-form-item>
        <k-form-item label="食物2">
          <k-field-select v-model="formData.selectValue2" :data-data="options"></k-field-select>
        </k-form-item>
        <k-form-footer>
          <k-btn data-functype="CLOSE">关闭</k-btn>
          <k-btn data-functype="SUBMIT" data-action="User.findUsers" :data-confirm="true">提交</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <el-divider/>

    <k-field-code>
      <textarea>
<template>
  <div>

    <h1>dialog弹出框：</h1>
    <br/>data-title-align:
    <br/><k-field-radio  v-model="titleAlign" :data-data="titleAlignOptions" />

    <br/>data-mask:
    <br/><k-field-radio  v-model="maskValue" :data-data="maskOptions" />

    <br/>data-confirm-close:
    <br/><k-field-radio  v-model="confirmCloseValue" :data-data="confirmCloseOptions" />

    <br/>data-width:
    <br/><k-field-radio  v-model="widthValue" :data-data="widthOptions" />

    <br/>data-width-percent:
    <br/><k-field-radio  v-model="widthPercentValue" :data-data="widthPercentOptions" />

    <br/>data-fullscreen:
    <br/><k-field-radio  v-model="fullscreenValue" :data-data="fullscreenOptions" />

    <br/>
    <k-btn data-functype="POPUP" data-target="dialog">弹出</k-btn>
    <k-popup ref="dialog" data-title="弹出框dialog"
             :data-mask="maskValue"
             data-confirm-describe="是否关闭弹出框dialog"
             :data-title-align="titleAlign"
             @data-open="open"
             :data-confirm-close = "confirmCloseValue"
             @data-close="close"
             :data-width="widthValue"
             :data-width-percent="widthPercentValue"
             :data-fullscreen="fullscreenValue">
      <k-form :data-col="1" :data-model="formData">
        <k-form-item label="食物1">
          <k-field-select v-model="formData.selectValue1" :data-data="options"></k-field-select>
        </k-form-item>
        <k-form-item label="食物2">
          <k-field-select v-model="formData.selectValue2" :data-data="options"></k-field-select>
        </k-form-item>
        <k-form-footer>
          <k-btn data-functype="CLOSE">关闭</k-btn>
          <k-btn data-functype="SUBMIT" data-action="User.findUsers" :data-confirm="true">提交</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <h1>drawer抽屉1：</h1>
    <br/>data-direction:
    <br/><k-field-radio  v-model="directionValue" :data-data="directionOptions" />
    <br/>data-mask:
    <br/><k-field-radio  v-model="maskValue" :data-data="maskOptions" />

    <br/>data-confirm-close:
    <br/><k-field-radio  v-model="confirmCloseValue" :data-data="confirmCloseOptions" />

    <br/>data-width:
    <br/><k-field-radio  v-model="widthValue" :data-data="widthOptions" />

    <br/>data-width-percent:
    <br/><k-field-radio  v-model="widthPercentValue" :data-data="widthPercentOptions" />

    <br/>data-fullscreen:
    <br/><k-field-radio  v-model="fullscreenValue" :data-data="fullscreenOptions" />

    <br/>
    <k-btn data-functype="POPUP" data-target="drawer">弹出</k-btn>
    <k-popup ref="drawer" data-title="drawer"
             :data-mask="maskValue"
             data-type="drawer"
             :data-direction="directionValue"
             :data-confirm-close="confirmCloseValue"
             data-confirm-describe="是否关闭drawer"
             data-title-align="center"
             :data-width="widthValue"
             :data-width-percent="widthPercentValue"
             :data-fullscreen="fullscreenValue"
             @data-open="open"
             @data-close="close">
      <k-form :data-col="1" :data-model="formData">
        <k-form-item label="食物1">
          <k-field-select v-model="formData.selectValue1" :data-data="options"></k-field-select>
        </k-form-item>
        <k-form-item label="食物2">
          <k-field-select v-model="formData.selectValue2" :data-data="options"></k-field-select>
        </k-form-item>
        <k-form-footer>
          <k-btn data-functype="CLOSE">关闭</k-btn>
          <k-btn data-functype="SUBMIT" data-action="User.findUsers" :data-confirm="true">提交</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>
        <script>
  import kayak from '@/frame/kayak.js'

  export default {
    data() {
      return {
        dataVisible: false,
        selectValue1: '',
        selectValue2: '',
        // data-title-align
        titleAlign: '',
        titleAlignOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: 'left',
            label: '靠左'
          },
          {
            value: 'center',
            label: '居中'
          }
        ],
        // data-direction
        directionValue: '',
        directionOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: 'left',
            label: '从左往右'
          },
          {
            value: 'right',
            label: '从右往左'
          },
          {
            value: 'top',
            label: '从上往下'
          },
          {
            value: 'bottom',
            label: '从下往上'
          },
        ],
        // data-mask
        maskValue: '',
        maskOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: false,
            label: 'false'
          },
          {
            value: true,
            label: true
          }
        ],
        // data-confirm-close
        confirmCloseValue: '',
        confirmCloseOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: false,
            label: 'false'
          },
          {
            value: true,
            label: 'true'
          }
        ],
        // data-width
        widthValue: '',
        widthOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: '500px',
            label: '500px'
          },
          {
            value: '900px',
            label: '900px'
          }
        ],
        // data-width-percent
        widthPercentValue: '',
        widthPercentOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: '50%',
            label: '50%'
          },
          {
            value: '80%',
            label: '80%'
          }
        ],
        // data-fullscreen
        fullscreenValue: '',
        fullscreenOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: false,
            label: 'false'
          },
          {
            value: true,
            label: 'true'
          }
        ],


        options: [{
          value: '选项1',
          label: '黄金糕'
        }, {
          value: '选项2',
          label: '双皮奶'
        }, {
          value: '选项3',
          label: '蚵仔煎'
        }, {
          value: '选项4',
          label: '龙须面'
        }, {
          value: '选项5',
          label: '北京烤鸭'
        }],
        formData: {}
      }
    },
    methods: {
      beforeClose(callback) {
        console.log('before close========>')
        callback("beforeClose callback value")
      },
      open() {
        console.log('open========>')
      },
      close() {
        console.log('close========>')
      }
    }
  }
</script>
<style>
</style>
          </textarea>
    </k-field-code>

  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'

  export default {
    data() {
      return {
        dataVisible: false,
        selectValue1: '',
        selectValue2: '',
        // data-title-align
        titleAlign: '',
        titleAlignOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: 'left',
            label: '靠左'
          },
          {
            value: 'center',
            label: '居中'
          }
        ],
        // data-direction
        directionValue: '',
        directionOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: 'left',
            label: '从左往右'
          },
          {
            value: 'right',
            label: '从右往左'
          },
          {
            value: 'top',
            label: '从上往下'
          },
          {
            value: 'bottom',
            label: '从下往上'
          },
        ],
        // data-mask
        maskValue: '',
        maskOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: false,
            label: 'false'
          },
          {
            value: true,
            label: true
          }
        ],
        // data-confirm-close
        confirmCloseValue: '',
        confirmCloseOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: false,
            label: 'false'
          },
          {
            value: true,
            label: 'true'
          }
        ],
        // data-width
        widthValue: '',
        widthOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: '500px',
            label: '500px'
          },
          {
            value: '900px',
            label: '900px'
          }
        ],
        // data-width-percent
        widthPercentValue: '',
        widthPercentOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: '50%',
            label: '50%'
          },
          {
            value: '80%',
            label: '80%'
          }
        ],
        // data-fullscreen
        fullscreenValue: false,
        fullscreenOptions: [
          {
            value: null,
            label: '空'
          },
          {
            value: false,
            label: 'false'
          },
          {
            value: true,
            label: 'true'
          }
        ],


        options: [{
          value: '选项1',
          label: '黄金糕'
        }, {
          value: '选项2',
          label: '双皮奶'
        }, {
          value: '选项3',
          label: '蚵仔煎'
        }, {
          value: '选项4',
          label: '龙须面'
        }, {
          value: '选项5',
          label: '北京烤鸭'
        }],
        formData: {}
      }
    },
    methods: {
      beforeClose(callback) {
        console.log('before close========>')
        callback("beforeClose callback value")
      },
      open() {
        console.log('open========>')
      },
      close() {
        console.log('close========>')
      }
    }
  }
</script>
<style>
</style>
