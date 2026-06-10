<template>
  <div>
    <k-field-code>
      <textarea>
        -	data-functype：按钮类型，可选值如下
         	-   RESET
         	-   SUBMIT
         	-   EXPORT
         	-   POPUP
         	-   PAGE,
          -   DOWNLOAD
        -	data-menuid：当data-functype=”PAGE”的时候，指定跳转到的页面的menuid
        -	data-menuname：当data-functype=”PAGE”的时候，指定跳转到的页面的title
        -	data-icon：当data-functype=”PAGE”的时候，指定跳转到的页面的icon
        -	data-from：可配置一个CSS选择器字符串，按钮提交时将获取该选择器下所有输入字段作为参数提交
        -	data-target：操作目标，根据按钮类型有不同的作用
         	-   RESET：重置目标下的所有表单字段值
         	-   SUBMIT：提交去刷新的grid列表
         	-   EXPORT：导出目标列表数据
         	-   POPUP： 弹出的目标
        -	data-action：提交请求的地址
        -	data-graphql：提交请求的地址
        -	data-confirm：点击提交按钮时是否弹出确认提示框，为true则以按钮的descript为提示内容弹出确认提示，为其他字符串则以这个字符串为提示内容弹出确认提示
        -	data-params：指定提交的固定参数值
        -	data-disable-condition：不可用条件JS函数
        -	data-handler：参数处理函数，JS函数里返回false阻止按钮动作
        -	data-after-success：提交成功之后调用
        -	data-disabled：是否不可用
        - data-export-name:当data-functype类型为EXOPORT时,可以指定导出文件的名称
        - data-download-name:当data-DOWNLOAD,可以指定下载文件的名称
      </textarea>
    </k-field-code>

    <k-form :data-col="3">
      <k-form-item label="输入项1">
        <k-field-select v-model="fromData.pass" :data-allowblank="false" data-action="User.findUsers"
          data-display-field="username,userid" data-value-field="userid"></k-field-select>
      </k-form-item>
      <k-form-item label="输入项2">
        <k-field-select v-model="fromData.checkPass" data-action="User.findUsers" data-display-field="username,userid"
          data-value-field="userid" data-default-value="10260"></k-field-select>
      </k-form-item>
      <k-form-item label="输入项3">
        <k-field-select v-model="fromData.age" data-action="User.findUsers" data-display-field="username"
          data-value-field="userid" :data-validate="validateTest"></k-field-select>
      </k-form-item>
      <k-form-footer>
        <k-btn data-functype="SUBMIT" data-action="User.addUser" data-size="medium" :data-confirm="true" :data-model="fromData"
          data-descript="添加用户">提交</k-btn>
        <k-btn data-functype="RESET">重置</k-btn>
      </k-form-footer>
    </k-form>
    <k-field-code>
      <textarea>
        按钮在表单下，通过自动获取表单对象与对象信息:
        <k-form :data-col="1" data-model="fromData">
          <k-form-item label="输入项1">
            <k-field-select v-model="fromData.params1" :data-allowblank="false" data-action="User.findUsers"
              data-display-field="username,userid" data-value-field="userid"></k-field-select>
          </k-form-item>
          <k-form-item label="输入项2">
            <k-field-select v-model="fromData.params2" data-action="User.findUsers" data-display-field="username,userid"
              data-value-field="userid" data-default-value="10260"></k-field-select>
          </k-form-item>
          <k-form-item label="输入项3">
            <k-field-select v-model="fromData.params3" data-action="User.findUsers" data-display-field="username"
              data-value-field="userid" :data-validate="validateTest"></k-field-select>
          </k-form-item>
          <k-form-footer>
            <k-btn data-functype="SUBMIT" data-action="User.addUser" data-descript="添加用户">提交</k-btn>
            <k-btn data-functype="RESET">重置</k-btn>
          </k-form-footer>
        </k-form>
      </textarea>
    </k-field-code>

    <k-form :data-col="1" data-model="fromData" ref="myForm">
      <k-form-item label="输入项1">
        <k-field-select v-model="fromData.pass" :data-allowblank="false" data-action="User.findUsers"
          data-display-field="username,userid" data-value-field="userid"></k-field-select>
      </k-form-item>
      <k-form-item label="输入项2">
        <k-field-select v-model="fromData.checkPass" data-action="User.findUsers" data-display-field="username,userid"
          data-value-field="userid" data-default-value="10260"></k-field-select>
      </k-form-item>
      <k-form-item label="输入项3">
        <k-field-select v-model="fromData.age" data-action="User.findUsers" data-display-field="username"
          data-value-field="userid" :data-validate="validateTest"></k-field-select>
      </k-form-item>
    </k-form>
    <k-btn data-functype="SUBMIT" data-action="User.addUser" data-from="myForm" data-descript="添加用户">提交</k-btn>
    <k-btn data-functype="RESET" data-from="myForm">重置</k-btn>
    <k-field-code>
      <textarea>
        按钮在表单外面，通过指定表单对象与对象信息:
        <k-form :data-col="1" data-model="fromData" ref="myForm">
          <k-form-item label="输入项1">
            <k-field-select v-model="fromData.params1" :data-allowblank="false" data-action="User.findUsers"
              data-display-field="username,userid" data-value-field="userid"></k-field-select>
          </k-form-item>
          <k-form-item label="输入项2">
            <k-field-select v-model="fromData.params2" data-action="User.findUsers" data-display-field="username,userid"
              data-value-field="userid" data-default-value="10260"></k-field-select>
          </k-form-item>
          <k-form-item label="输入项3">
            <k-field-select v-model="fromData.params3" data-action="User.findUsers" data-display-field="username"
              data-value-field="userid" :data-validate="validateTest"></k-field-select>
          </k-form-item>
          <k-form-footer>

          </k-form-footer>
        </k-form>
        <k-btn data-functype="SUBMIT" data-action="User.addUser" data-from="myForm" data-descript="添加用户">提交</k-btn>
        <k-btn data-functype="RESET" data-from="myForm">重置</k-btn>
      </textarea>
    </k-field-code>

    <k-btn data-functype="PAGE" data-target="demo/k-field-radio">打开新页面</k-btn>

  </div>
</template>

<script>
  export default {
    data() {

      return {
        selectValue: "10258",
        fromData: {
          pass: '',
          checkPass: '',
          age: ''
        },
      }
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
          } else {
            console.error('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      validateTest(value) {
        if (value != "10258") {
          return "只能选择审判员";
        }
        return true;
      },

    }
  }
</script>
<style>
</style>
