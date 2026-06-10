<template>
  <div>
    <k-field-code>
      <textarea>
data-validate：指定一个JS函数，用于输入验证
data-type: 显示类型 year/month/date/week/datetime  默认datetime
data-date-format：指定date控件显示的日期格式，默认为yyyy-dd-mm
data-value-format：指定date控件显示的日期格式，默认为yyyyddmm
data-on-change：定义修改事件
data-on-focus：定义获取焦点事件
data-on-blur：定义失去焦点事件
data-max-value：指定字段最大值，默认为小于等于，如果需要设置成小于，在设置值的右边加上半角右括号“)”，例：data-max-value="20140101)"表示小于2014-01-01的日期
data-min-value：指定字段最大值，默认为大于等于，如果需要设置成大于，在设置值的左边加上半角左括号“(”，例：data-min-value="(20140101"表示大于2014-01-01的日期
data-disabled：true/false，输入控件是否不可用
data-workday：true则表示只能选择系统工作日日期，其他值则可直接指定工作日方案代码
          </textarea>
    </k-field-code>

    {{dateValue1}}
    <k-field-date v-model="dateValue1" :data-max-value="dateValue2"></k-field-date>
    {{dateValue2}}
    <k-field-date v-model="dateValue2" :data-min-value="dateValue1"></k-field-date>

    <k-field-code>
      <textarea>
代码：
      <k-field-date v-model="dateValue1" :data-max-value="dateValue2"></k-field-date>
      <k-field-date v-model="dateValue2" :data-min-value="dateValue1"></k-field-date>
      </textarea>
    </k-field-code>

    <k-field-date v-model="dateValue3" @data-on-change="change" :data-disabled="true"></k-field-date>

     <k-field-code>
      <textarea>
代码：
        <k-field-date v-model="dateValue3" @data-on-change="change" :data-disabled="true"></k-field-date>

      </textarea>
    </k-field-code>

    {{dateValue4}}
    <k-field-date v-model="dateValue4" @data-on-change="change" data-type="date" data-date-format="yyyyMMdd" data-value-format="yyyy-M-d"></k-field-date>

     <k-field-code>
      <textarea>
代码：
        <k-field-date v-model="dateValue4" @data-on-change="change" data-type="date" data-date-format="yyyyMMdd" data-value-format="yyyy-M-d"></k-field-date>

      </textarea>
    </k-field-code>

    {{dateValue5}}
    <k-field-date v-model="dateValue5" @data-on-change="change" data-type="datetime" data-date-format="yyyy-MM-dd"></k-field-date>

     <k-field-code>
      <textarea>
代码：
        <k-field-date v-model="dateValue5" @data-on-change="change" data-type="datetime" data-date-format="yyyy-MM-dd"></k-field-date>

      </textarea>
    </k-field-code>

  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  export default {
    data() {
      return {
        dateValue1: '',
        dateValue2: '',
        dateValue3: '',
        dateValue4: '',
        dateValue5: '',
      }
    },
    methods: {
      change(value) {
        console.info("change被调用了" + value);
      },

    }
  }
</script>
<style>
</style>
