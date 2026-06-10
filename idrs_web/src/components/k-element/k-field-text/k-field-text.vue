<template>
  <el-tooltip v-if="dataShowGbmoney &&  dataValidateType =='money'" class="item" effect="light" :content="dataDescript" placement="bottom" :visible-arrow="false" popper-class="tooltipStyle">
    <el-input type="text" class="k-field-text" :style="itemStyle"  :placeholder="dataPlaceholder" :value="dataValue"
              :minlength="dataMinLength" :show-word-limit="dataShowWordLimit === true || dataShowWordLimit === 'true'" :disabled=" dataDisabled=== true || dataDisabled === 'true'"
              :show-password="dataShowPassword === true || dataShowPassword === 'true'" :class="[dataValidateType === 'code' ? 'text-align-left' : 'text-align-right',(dataShowPassword&&passwordLeft)?'suffixIcon':'']"
              @input="_handleInput" @change="handleChange" @focus="_handleFocus" @blur="_handleBlur">
    </el-input>
  </el-tooltip>
  <el-input v-else ref="elInput" :type="inputType" :rows="rows" class="k-field-text" :style="itemStyle"  :placeholder="dataPlaceholder" :value="dataValue"
            :minlength="dataMinLength" :show-word-limit="dataShowWordLimit === true || dataShowWordLimit === 'true'" :disabled=" dataDisabled=== true || dataDisabled === 'true'"
            :show-password="dataShowPassword === true || dataShowPassword === 'true'" :class="[dataValidateType === 'code' ? 'text-align-left' : 'text-align-right',(dataShowPassword&&passwordLeft)?'suffixIcon':'']"
            @input="_handleInput" @change="handleChange" @focus="_handleFocus" @blur="_handleBlur" :autosize="dataAutosize">
  </el-input>
</template>

<script>
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";
  import Tools from '@/utils/tools.js'

  const emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
  const codeReg = /^[0-9]+$/;
  const intReg = /^[+-]?(0|[1-9][0-9]*)$/;
  const numberReg = /^-?([1-9]*\.?|[1-9]\d*|[1-9]\d*\.\d*|0\.\d*|0)$/;
  const telephoneReg = /^0?(13|14|15|16|18|17|19)[0-9]{9}$/;
  const telephoneReg1 = /^0?(11|12|13|14|15|16|18|17|19)[0-9]{9}$/;
  const postcodeReg = /\d{6}/;
  const codeLetterReg = /^[0-9a-zA-Z]+$/;
  const codeLetterLineReg = /^[0-9a-zA-Z\-]+$/;

  export default {
    name: "KFieldText",
    mixins: [props(), event(), emitter()],
    props: {
      dataShowGbmoney:{
        type: [Boolean, String],
        default: false
      },
      dataMaxLength: {
        type: Number,
        default: null
      },
      dataMinLength: {
        type: Number,
        default: null
      },
      dataShowWordLimit: {
        type: [Boolean, String],
        default: false
      },
      dataShowPassword: {
        type: [Boolean, String],
        default: false
      },
      dataValidateType: {
        type: String,
        default: null
      },
      dataRegx: {
        type: String,
        default: null
      },
      dataPlaceholder: {
        type: String,
        default: ""
      },
      inputType: {
        type: String,
        default: "text"
      },
      rows: {
        type: Number,
        default: 3
      },
      dataRegxText: {
        type: String,
        default: null
      },
      dataMaxValue: {
        type: String
      },
      dataMinValue: {
        type: String
      },
      dataDigits: {
        type: [String,Number],
        default: null
      },
      dataIntegerLength:{
        type: [String,Number],
        default: null
      },
      dataAutosize: {
        type: [Object, Boolean],
        default: false
      }
    },
    data() {
      return {
        digitsRsg : null,
        dataValue: null,
        isFocus:false,
        passwordLeft:false,
      };
    },
    computed: {
      dataDescript:{
        get(){
          return Tools.numToCny(this.value)
        },
        set(){
          this._handleInput(this.value)
        }
      },
      itemStyle(){
        let ret={}
        if(this.$parent.$options.name === "MdField"){
          if(this.dataShowPassword&&this.passwordLeft){
            ret.paddingRight="60px"
          }else{
            ret.paddingRight="40px"
          }
          return ret
        }

      }
    },
    watch:{
      value(newVal){
        if (this.dataValidateType === "money"&&!this.isFocus) {
          if(newVal){
            newVal=this.moneyToMicrometer(newVal)
          }
        }
        this.dataValue=newVal
      }
    },
    created(){
      this.dataValue = this.value;
      if(this.dataDigits){
        this.digitsRsg =  new RegExp("^-?([1-9]*\\.?|[1-9]\\d*|[1-9]\\d*\\.\\d{0,"+this.dataDigits+"}|0\\.\\d{0,"+this.dataDigits+"}|0)$");
      } else if (this.dataValidateType === 'money'){
        let dataDigits = "2";
        this.digitsRsg =  new RegExp("^-?([1-9]*\\.?|[1-9]\\d*|[1-9]\\d*\\.\\d{0,"+dataDigits+"}|0\\.\\d{0,"+dataDigits+"}|0)$");
      }
    },
    methods: {
      focus(){
        this.$refs.elInput.focus()
      },
      clear() {
        this.dataValue = '';
        this.handleInput('');
        this.handleChange('');
      },
      _handleFocus(value) {
        this.isFocus=true
        this.dataValue = this.value;
        this.handleFocus(value);
      },
      _handleBlur(value) {
        this.isFocus=false
        if (this.dataValidateType === "number" || this.dataValidateType === "money") {
          this.removeEndPoint()

          if (this.dataValue) {
            let newVal = this.dataValue
            newVal = this.replaceIfGreaterMaximum(Number(newVal))
            newVal = this.replaceIfLessMinimum(Number(newVal))

            this.handleInput(newVal)
            if (this.dataValidateType === "money") {
              newVal=this.moneyToMicrometer(newVal)
            }
            this.dataValue = newVal;
          }
        }
        this.handleBlur(value)
      },
      _handleInput(newVal) {
        newVal = newVal + '';
        if (this.dataMaxLength) {
          let length = Tools.strByteLength(newVal);
          if (length > this.dataMaxLength) {
            newVal = this.dataValue;
          }
        }

        //校验整数部分是否超长
        if(newVal && this.dataIntegerLength && (this.dataValidateType === "number" || this.dataValidateType ==="money")){
          let tempVal = newVal.split(".");
          let length = Tools.strByteLength(tempVal[0]);
          if (length > Number(this.dataIntegerLength)) {
            newVal = this.dataValue;
          }
        }

        if (newVal && (this.dataValidateType === "int" || this.dataValidateType === "number")) {
          if (!(intReg.test(newVal) || numberReg.test(newVal))) {
            newVal = this.dataValue
          }

          if(this.dataDigits){
            //小数位数校验
            if(!this.digitsRsg.test(newVal)){
              newVal = this.dataValue
            }
          }
        } else if (newVal && this.dataValidateType === "money") {
          if(!this.digitsRsg.test(newVal)){
            newVal = this.dataValue
          }
        } else if (newVal && (this.dataValidateType === "codeLetter" || this.dataValidateType === "codeletter") && newVal.length>0){
          if (!codeLetterReg.test(newVal)) {
            newVal = this.dataValue
          }
        }else if (newVal && (this.dataValidateType === "codeLetterLine" || this.dataValidateType === "codeletterline") && newVal.length>0){
          if (!codeLetterLineReg.test(newVal)) {
            newVal = this.dataValue
          }
        }

        this.dataValue = newVal
        this.handleInput(newVal)
      },
      replaceIfGreaterMaximum(value) {
        if (!this.value || !this.dataMaxValue) {
          return value;
        }
        if (this.dataMaxValue.endsWith(")")) {
          let _dataMaxValue = this.dataMaxValue.substring(0, this.dataMaxValue.length - 1);
          let max = Number(_dataMaxValue);
          if (value >= max) {
            return String(max - 1);
          }
        } else {
          let max = Number(this.dataMaxValue);
          if (value > max) {
            return String(max);
          }
        }
        return String(value);
      },
      replaceIfLessMinimum(value) {
        if (!this.value || !this.dataMinValue) {
          return value;
        }
        if (this.dataMinValue.startsWith("(")) {
          let _dataMinValue = this.dataMinValue.substring(1, this.dataMinValue.length);
          let min = Number(_dataMinValue);
          if (value <= min) {
            return String(min + 1);
          }
        } else {
          let min = Number(this.dataMinValue);
          if (value < min) {
            return String(min);
          }
        }
        return String(value);
      },
      removeEndPoint() {
        let dataValue = this.dataValue;
        if (dataValue) {
          dataValue = dataValue + ""
          if (dataValue.charAt(dataValue.length - 1) === ".") {
            this.handleInput(dataValue.substring(0, dataValue.length - 1))
          }
        }
      },
      removeMoneyEnd0(money){
        if(money.indexOf(".")==-1){
          return money
        }else{
          if(money.lastIndexOf("0")!=(money.length-1)){
            if(money.indexOf(".")==(money.length-1)){
              return money.substring(0,money.length-1)
            }else{
              return money
            }
          }else{
            return this.removeMoneyEnd0(money.substring(0,money.length-1))
          }
        }
      },
      moneyToMicrometer(s){
        if (!isNaN(s)) {
          s =s+""
          var l = s.split(".")[0].split("").reverse(), r = s.indexOf(".") >= 0 ? "."
            + s.split(".")[1]
            : "";
          let t = "";
          for (var i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
          }
          return t.split("").reverse().join("") + r;

        } else {
          return s;
        }
      },
      validate() {
        // 正则优先级高
        if ((this.dataAllowblank === false || this.dataAllowblank === 'false')
          && (this.value ==="" || this.value ==null)) {
          return "该项不允许为空";
        }
        //调用自定义验证函数
        if (this.dataValidate) {
          return this.dataValidate(this.value);
        }

        if (this.dataMinLength && this.value && this.value.length > 0) {
          let length = this.value.length;
          if (length < this.dataMinLength) {
            return "至少输入" + this.dataMinLength + "位";
          }
        }
        if (this.value && this.dataRegx) {
          if ((new RegExp(this.dataRegx)).test(this.value)) {
            return true;
          }
          return this.dataRegxText;
        }

        let newVal = this.value;
        let errorMsg = "";
        const type = this.dataValidateType;
        if (!type) {
          return true;
        }
        if (!newVal) {
          return true
        }
        // - code：限制只能输入数字文本，左对齐
        // - int： 验证整型数，右对齐
        // - number：验证包含小数点的数字，右对齐
        // - money：验证金额，右对齐
        // - text:允许输入中文，数字和英文字母，右对齐
        let valid = true;
        switch (type) {
          case "email":
            valid = emailReg.test(newVal);
            errorMsg = "请填写正确的邮箱地址";
            break;
          case "telephone":
            valid = telephoneReg.test(newVal);
            errorMsg = "请填写正确的手机号";
            break;
          case "telephone1":
            valid = telephoneReg1.test(newVal);
            errorMsg = "请填写正确的手机号";
            break;
          case "postcode":
            valid = postcodeReg.test(newVal);
            errorMsg = "请填写正确的邮政编码";
            break;
          case "code":
            valid = codeReg.test(newVal);
            errorMsg = "请输入数字文本";
            break;
          case "int":
            valid = intReg.test(newVal);
            errorMsg = "请输入整数";
            break;
          case "number":
            valid = numberReg.test(newVal);
            errorMsg = "请输入数字";
            break;
          case "money":
            valid = numberReg.test(newVal);
            errorMsg = "请输入金额";
            break;
          case "codeletter":
            valid = codeLetterReg.test(newVal);
            errorMsg = "允许输入数字字母或";
          case "codeletterline":
            valid = codeLetterLineReg.test(newVal);
            errorMsg = "允许输入数字字母或-(横线）";
          default:
            // default text
            break;
        }
        return valid ? true : errorMsg;
      }
    }
  };
</script>

<style lang="scss">
  .el-textarea.is-disabled .el-textarea__inner {
    background-color: #F5F7FA;
    border-color: #E4E7ED;
    color: #383737;
    cursor: not-allowed;
  }

  .k-field-text.text-align-left .el-input__inner {
    text-align: left;
  }

  // todo
  .k-field-text.text-align-right .el-input__inner {
    text-align: left;
  }
  .suffixIcon{
    .el-input__suffix{
      top:6px;
      right: 35px;
    }
  }
  .tooltipStyle {
    background: #FFE4C4 !important;
  }
</style>
