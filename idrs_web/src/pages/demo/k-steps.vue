<template>
  <div>
    <k-field-code>
      <textarea>
k-steps:
属性：
-	data-direction：显示方向，vertical/horizontal， 默认horizontal
-	data-active: 设置当前激活步骤，类型number（点击"完成"按钮后，会触发所有步骤的校验，如果通过data-active跳过步骤导致最终校验不通过，会出现点击"完成"无响应）
-	data-next-show: 是否显示下一步按钮，默认true
-	data-submit-show: 是否显示提交按钮，默认true
-	data-button-align: 默认按钮对齐方式，left/center/right 默认center

表单属性：
-   data-col: 每一行展示的k-form-item数量
提交按钮属性，类型为SUBMIT,详细示例见k-btn组件示例：
-	data-action：提交请求的地址
-	data-graphql：提交请求的地址
-	data-confirm：点击提交按钮时是否弹出确认提示框，为true则以按钮的descript为提示内容弹出确认提示，为其他字符串则以这个字符串为提示内容弹出确认提示


事件：
-	data-active-change: 步骤条变动事件，参数，newVal，oldVal

函数：
pre();上一步
next();下一步


k-step:
-	data-icon：自定义图标
-	data-title: 自定义标题
-	data-description: 自定义描述性文字

          </textarea>
    </k-field-code>
    <el-divider/>
    <k-steps :data-active="2"
             @data-active-change="dataActiveChange"
             data-button-align="right"
             :data-col="2"
             data-action="User.findUsers"
             :data-confirm="true">
      <k-step data-title="标题1" data-description="描述1" data-icon="el-icon-food">
        <k-form-item label="食物">
          <k-field-select v-model="selectValue1" :data-data="options"/>
        </k-form-item>
      </k-step>
      <k-step data-title="标题2" data-description="描述2" data-icon="el-icon-food">
        <k-form-item label="食物">
          <k-field-select v-model="selectValue2" :data-data="options"/>
        </k-form-item>
        <k-form-item label="食物">
          <k-field-select v-model="selectValue3" :data-data="options" :data-allowblank="false"/>
        </k-form-item>
      </k-step>
      <k-step data-title="标题3" data-description="描述3" data-icon="el-icon-food">
        <k-form-item label="食物">
          <k-field-select v-model="selectValue4" :data-data="options"/>
        </k-form-item>
        <k-form-item label="食物">
          <k-field-select v-model="selectValue5" :data-data="options" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="食物">
          <k-field-select v-model="selectValue6" :data-data="options" :data-allowblank="false"/>
        </k-form-item>
      </k-step>
      <k-step data-title="标题4" data-description="描述4" data-icon="el-icon-food">
        <k-form-item label="食物">
          <k-field-select v-model="selectValue7" :data-data="options"/>
        </k-form-item>
        <k-form-item label="食物">
          <k-field-select v-model="selectValue8" :data-data="options" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="食物">
          <k-field-select v-model="selectValue9" :data-data="options" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="食物">
          <k-field-select v-model="selectValue10" :data-data="options" :data-allowblank="false"/>
        </k-form-item>
      </k-step>
    </k-steps>


    <k-field-code>
      <textarea>
    代码示例：
        <template>
          <div>
            <k-steps :data-active="2"
                     @data-active-change="dataActiveChange"
                     data-button-align="right"
                     :data-col="2"
                     data-action="User.findUsers"
                     :data-confirm="true">
              <k-step data-title="标题1" data-description="描述1" data-icon="el-icon-food">
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue1" :data-data="options"/>
                </k-form-item>
              </k-step>
              <k-step data-title="标题2" data-description="描述2" data-icon="el-icon-food">
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue2" :data-data="options"/>
                </k-form-item>
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue3" :data-data="options" :data-allowblank="false"/>
                </k-form-item>
              </k-step>
              <k-step data-title="标题3" data-description="描述3" data-icon="el-icon-food">
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue4" :data-data="options"/>
                </k-form-item>
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue5" :data-data="options" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue6" :data-data="options" :data-allowblank="false"/>
                </k-form-item>
              </k-step>
              <k-step data-title="标题4" data-description="描述4" data-icon="el-icon-food">
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue7" :data-data="options"/>
                </k-form-item>
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue8" :data-data="options" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue9" :data-data="options" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="食物">
                  <k-field-select v-model="selectValue10" :data-data="options" :data-allowblank="false"/>
                </k-form-item>
              </k-step>
            </k-steps>
          </div>

          <script>
            import kayak from '@/frame/kayak.js'

            export default {
              data() {
                return {
                  selectValue1: '',
                  selectValue2: '',
                  selectValue3: '',
                  selectValue4: '',
                  selectValue5: '',
                  selectValue6: '',
                  selectValue7: '',
                  selectValue8: '',
                  selectValue9: '',
                  selectValue10: '',
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
                  }]
                }
              },
              methods: {
                dataActiveChange(newVal, oldVal) {
                  console.log('dataActiveChange=====>', newVal, oldVal)
                }
              }
            }
          </script>
        </template>
      </textarea>
    </k-field-code>
    <el-divider/>
  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'

  export default {
    data() {
      return {
        selectValue1: '',
        selectValue2: '',
        selectValue3: '',
        selectValue4: '',
        selectValue5: '',
        selectValue6: '',
        selectValue7: '',
        selectValue8: '',
        selectValue9: '',
        selectValue10: '',
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
        }]
      }
    },
    methods: {
      dataActiveChange(newVal, oldVal) {
        console.log('dataActiveChange=====>', newVal, oldVal)
      }
    }
  }
</script>
