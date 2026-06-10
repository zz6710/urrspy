<template>
  <div>
    <k-field-code>
      <textarea>
data-allowblank：true/false，是否允许为空
data-validate：指定一个JS函数，用于输入验证
data-on-change：定义修改事件
data-on-focus：定义获取焦点事件
data-on-blur：定义失去焦点事件
data-max-value：指定字段最大值，默认为小于等于，如果需要设置成小于，在设置值的右边加上半角右括号“)”，例：data-max-value="121000)"表示小于12:10:00的时间
data-min-value：指定字段最大值，默认为大于等于，如果需要设置成大于，在设置值的左边加上半角左括号“(”，例：data-min-value="(120000"表示大于12:00:00的时间
data-disabled：true/false，输入控件是否不可用
data-value-format: 日期值格式 默认HHmmss(HH-24小时制补0，mm-分钟补0,ss-秒补0)
          </textarea>
    </k-field-code>
    <el-divider/>

    {{timeValue1}}
    <k-field-time v-model="timeValue1" @data-on-change="change" @data-on-focus="focus"
                  @data-on-blur="blur" data-value-format="mmHHss"/>
    <k-field-code>
      <textarea>
代码1（data-on-change | data-on-focus | data-on-blur | data-value-format）：

    <k-field-time v-model="timeValue1" @data-on-change="change" @data-on-focus="focus"
                  @data-on-blur="blur" data-value-format="mmHHss"/>
      </textarea>
    </k-field-code>
    <el-divider/>

    {{timeValue2}}
    <k-field-time v-model="timeValue2" data-min-value='(124502' data-max-value='133040'/>
    <k-field-code>
      <textarea>
代码2（data-min-value | data-max-value）：
<k-field-time v-model="timeValue2" data-min-value='(124502' data-max-value='133040'/>
      </textarea>
    </k-field-code>
    <el-divider/>

    {{timeValue3}}
    <k-field-time v-model="timeValue3" :data-disabled="dataDisabled3" />
    <k-field-code>
      <textarea>
代码3（data-disabled）：
<k-field-time v-model="timeValue3" :data-disabled="dataDisabled3" />
      </textarea>
    </k-field-code>
    <el-divider/>


    {{timeLinkageValue1}}
    <k-field-time v-model="timeLinkageValue1" :data-max-value="timeLinkageValue2"/>
    {{timeLinkageValue2}}
    <k-field-time v-model="timeLinkageValue2" :data-min-value="timeLinkageValue1" />
    <k-field-code>
      <textarea>
代码4（时间区间联动）：
<k-field-time v-model="timeLinkageValue1" :data-max-value="timeLinkageValue2"/>
<k-field-time v-model="timeLinkageValue2" :data-min-value="timeLinkageValue1" />
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
        timeValue1: '',
        timeValue2: '',
        timeValue3: '',
        dataDisabled3: true,
        timeLinkageValue1: '',
        timeLinkageValue2: ''
      }
    },
    methods: {
      change(value) {
        console.log('data-on-change========>', value)
      },
      focus(value) {
        console.log('data-on-focus========>', value)
      },
      blur(value) {
        console.log('data-on-blur========>', value)
      }
    }
  }
</script>
<style>
</style>
