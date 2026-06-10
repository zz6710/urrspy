<template>
  <div>
    <k-field-code>
      <textarea>
data-value：设置默认值
data-allowblank：true/false，是否允许为空
data-validate：指定一个JS函数，用于输入验证
data-on-change：定义修改事件
data-on-focus：定义获取焦点事件
data-on-blur：定义失去焦点事件
data-disabled：true/false，输入控件是否不可用
data-data：直接指定下拉数据
data-action：指定数据来源action
data-graphql：指定数据来源graphql
data-params：指定固定的查询参数值
data-url：指定数据来源url
data-value-field：指定查询数据中作为值的字段名称
data-display-field：指定查询数据中作为文本显示的字段名称，可以指定多个字段名（以半角逗号分隔），那么显示的时候就以data-display-separator分隔符串起来
data-display-separator：指定显示多个字段值的时候字段值之间的分隔符，默认为“-”
data-on-beforeload：指定加载数据之前触发调用的JS函数
data-on-afterload：指定加载数据之后触发调用的JS函数
data-placeholder: 占位文本
          </textarea>
    </k-field-code>

    <k-field-select v-model="selectValue" data-action="User.findUsers" data-display-field="username,userid"
                    data-value-field="userid" @data-on-change="change" :data-multiple="false"
                    data-params="{user:23,name:'wewe'}"></k-field-select>
    <k-field-code>
      <textarea>
代码：
<k-field-select v-model="selectValue2" data-action="User.findUsers" data-display-field="username"
data-value-field="userid" @data-on-change="change" :data-multiple="true"></k-field-select>
      </textarea>
    </k-field-code>
    <k-field-select v-model="selectValue2" data-action="User.findUsers" data-display-field="username"
                    data-value-field="userid"
                    @data-on-change="change" :data-multiple="true"></k-field-select>

    <k-field-code>
      <textarea>
  代码：
 <k-field-select v-model="selectValue2" data-action="User.findUsers" data-display-field="username" data-value-field="userid"
      @data-on-change="change" :data-multiple="true"></k-field-select>
        </textarea>
    </k-field-code>

    <k-field-select v-model="selectValue3" data-placeholder="请选择用户"
                    data-graphql='{queryUser(action:"findUsers") {rows{  userid, username},results}}'
                    data-display-field="username" data-value-field="userid" @data-on-change="change"></k-field-select>

    <k-field-code>
      <textarea>
  代码：
 <k-field-select v-model="selectValue3" data-graphql='{queryUser(action:"findUsers") {rows{  userid, username},results}}'
   data-display-field="username" data-value-field="userid" @data-on-change="change"></k-field-select>
        </textarea>
    </k-field-code>
    <k-field-select v-model="selectValue4" :data-data="options" @data-on-change="change"></k-field-select>


    <k-field-code>
      <textarea>
  代码：
  <k-field-select v-model="selectValue4" :data-data="options" @data-on-change="change"></k-field-select>

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
        </textarea>
    </k-field-code>

    <k-field-select v-model="selectValue5" data-dict="agent_id_type"></k-field-select>
    <k-field-code>
      <textarea>
    数字字典案例：
        <k-field-select v-model="selectValue5" data-dict="agent_id_type"></k-field-select>
          </textarea>
    </k-field-code>
  </div>
</template>

<script>
import kayak from '@/frame/kayak.js'

export default {
  props: {
    dataContentType: {
      type: String
    }
  },
  data() {
    return {
      selectValue: "10258",
      selectValue2: "10258,10262",
      selectValue3: "",
      selectValue4: "",
      selectValue5: "",
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
    }
  },
  methods: {
    change(value) {
      console.info(value);
    },

  }
}
</script>
<style>
</style>
