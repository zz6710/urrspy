<template>
  <div>
    组件属性:
    <k-field-code>
      <textarea>
        data-allowblank：true/false，是否允许为空
        data-size: 组件大小,默认是mini，可选 medium / small / mini
        data-disabled：true/false，输入控件是否不可用
        data-text-color: 按钮形式的Radio 激活时的文本颜色,默认是#ffffff（默认是白色），button时候有效
        data-fill-color: 按钮形式的 Radio 激活时的填充色和边框色，默认是#409EFF（蓝色），button时候有效
        data-border: 只对reaio有效，是否显示边框默认为false
        data-ui-type：选择样式，redio或者button，默认值是redio

        data-dict：获取数据字典内容信息
        data-data: 内置自定义数据,默认取value,label,如果设置data-value-field，data-display-field则取自定义的数据格式
        data-params：指定固定的查询参数值
        data-action：指定数据来源action
        data-graphql：指定数据来源graphql

        data-value-field：指定查询数据中作为值的字段名称
        data-display-field：指定查询数据中作为文本显示的字段名称，可以指定多个字段名（以半角逗号分隔），那么显示的时候就以data-display-separator分隔符串起来
        data-display-separator：指定显示多个字段值的时候字段值之间的分隔符，默认为“-”
        data-value-disabled: 指定结果中是否可以选择,当为true,'true','disabled',则当前项不能进行选择,data-data原数据方式不参与数据处理
        data-on-object: 如果配置则data-on-change触发返回的是对象,否则返回当前选中的vulue值

        ------事件------
        data-on-change：定义修改事件
        data-on-beforeload：指定加载数据之前触发调用的JS函数
        data-on-afterload：指定加载数据之后触发调用的JS函数
        data-value-method: 自定义指定结果选择条件，当满足条件时则屏蔽选择条件,传入一个是数据row对象，必须返回true才能条件成立function(row)

      </textarea>
    </k-field-code>
    <br>

    基本用法:
    <k-field-radio v-model="radioValue4" :data-data="options" data-on-object  @data-on-change="change" /> {{ radioValue4 }}
    <k-field-code>
      <textarea>
       代码：<k-field-radio  v-model="radioValue4" :data-data="options" data-on-object @data-on-change="change"></k-field-radio>
     </textarea>
    </k-field-code>
    <br>

    自定义数据格式button用法:
    <k-field-radio v-model="radioValue" :data-data="options1" data-ui-type="button" data-fill-color="#ffb932" data-value-disabled="go" :data-value-method="valuemethod" data-value-field="userid" data-display-field="username" @data-on-change="change" /> {{ radioValue }}
    <k-field-code>
      <textarea>
        代码：<k-field-radio  v-model="radioValue" :data-data="options1" data-ui-type="button" data-fill-color="#ffb932" data-value-field="userid" data-display-field="username" @data-on-change="change"/>
      </textarea>
    </k-field-code>
    <br>

    不可输入非绑定变量:
    <k-field-radio v-model="radioValue4" :data-data="options" data-disabled />
    <k-field-code>
      <textarea>
        代码: <k-field-radio v-model="radioValue4" :data-data="options" data-disabled  ></k-field-radio>
      </textarea>
    </k-field-code>
    <br>

    自定义data-action用法:
    <k-field-radio v-model="radioValue2" data-action="User.findUsers" data-on-object data-display-field="username,userid,orgno" data-value-field="userid" @data-on-change="change" /> {{ radioValue2 }}
    <k-field-code>
      <textarea>
       代码: <k-field-radio  v-model="radioValue2" data-action="User.findUsers" data-on-object data-display-field="username,userid" data-value-field="userid" @data-on-change="change" ></k-field-radio>
      </textarea>
    </k-field-code>
    <br>

    自定义data-graphql用法:
    <k-field-radio v-model="radioValue3" data-on-object data-graphql="{queryUser(action:&quot;findUsers&quot;){rows{userid,username,orgno},results}}" data-display-field="userid,username" data-value-field="userid" @data-on-change="change" /> {{ radioValue3 }}
    <k-field-code>
      <textarea>
        代码：<k-field-radio  v-model="radioValue3" data-on-object data-graphql='{queryUser(action:"findUsers"){rows{userid,username,orgno},results}}' data-display-field="userid,username" data-value-field="userid" @data-on-change="change" ></k-field-radio>
      </textarea>
    </k-field-code>
    <br>

    data-dict的用法:
    <k-field-radio v-model="radioValue5" data-dict="agent_id_type" />
    <k-field-code>
      <textarea>
        代码：<k-field-radio v-model="radioValue5" data-dict="agent_id_type"></k-field-radio>
      </textarea>
    </k-field-code>

  </div>
</template>

<script>

export default {
  data() {
    return {
      radioValue: '选项2',
      radioValue2: '10258',
      radioValue3: '',
      radioValue4: "选项2",
      radioValue5: '',
      options: [{
        value: '选项1',
        label: '黄金糕',
        disabled: true
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
      options1: [{
        userid: '选项1',
        username: '黄金糕',
        go:'1'
      }, {
        userid: '选项2',
        username: '双皮奶',
        go:'1'
      }, {
        userid: '选项3',
        username: '蚵仔煎',
        go:'1'
      }, {
        userid: '选项4',
        username: '龙须面',
        go:'2'
      }, {
        userid: '选项5',
        username: '北京烤鸭',
        go:'1'
      }]
    }
  },
  methods: {
    change(value) {
      console.info('事件测试:')
      console.log(value)
    },
    handleInput(value) {
      console.info('事件测试:')
      console.log(value)
    },
    valuemethod(row){
      return !!(row['go'] === '2')
    }
  }
}
</script>

    <style scoped>

    </style>
