<template>
  <k-form ref="addCustomerInfoForm" :data-col="2" dataLabelWidth="140px" dataInputWidth="190px">
    <k-form-item label="人物类型">
      <k-field-select v-model="value.custType" @data-on-change="isBlank(value.custType)" :dataAllowblank="false"
                      data-dict="t8_cust_type"/>
    </k-form-item>
    <k-form-item label="工号">
      <k-field-text v-model="value.jobno" :data-max-length="20" :data-allowblank="false"
                    @data-on-blur="inputJobno(value.jobno)"/>
    </k-form-item>
    <k-form-item label="姓名">
      <k-field-text v-model="value.custName" :dataAllowblank="false" @data-on-blur="inputCustName(value.custName)" :data-max-length="32"/>
    </k-form-item>

    <k-form-item label="人员简介" :data-col="2">
      <k-field-text v-model="value.brief" :data-allowblank="flag"
                    :data-max-length="2000" inputType="textarea" :rows="5"/>
    </k-form-item>

    <k-form-item label="证件类型">
      <k-field-select v-model="value.idType" :dataAllowblank="value.custType=='3'" data-dict="t8_id_type"/>
    </k-form-item>
    <k-form-item label="证件号码">
      <k-field-text v-model="value.idCode" :dataAllowblank="value.idType=='8'?true:value.custType=='3'"
                    :data-max-length="32" :data-min-length="6"/>
    </k-form-item>
    <k-form-item label="手机">
      <k-field-text v-model="value.mobile" :data-max-length="11" data-validate-type="telephone"
                    :dataAllowblank="value.custType!='3'"/>
    </k-form-item>
    <k-form-item label="座机">
      <k-field-text v-model="value.homeTel" :data-max-length="20" :dataAllowblank="true"/>
    </k-form-item>
    <k-form-item label="电子邮箱">
      <k-field-text v-model="value.email"
                    :data-regx="'^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$'"
                    data-regx-text="请输入正确的邮箱" :data-max-length="64" :dataAllowblank="value.custType!='3'"/>
    </k-form-item>
    <k-form-item label="地址" :data-col="2">
      <k-field-text v-model="value.address" inputType="textarea" :data-max-length="128"/>
    </k-form-item>
    <k-form-item label="从业年限">
      <k-field-text v-model="value.employmentTerm" :data-max-length="2" :data-allowblank="flag"/>
    </k-form-item>
    <k-form-item label="投资管理从业年限">
      <k-field-text v-model="value.investEmploymentTerm" :data-max-length="2" :data-allowblank="flag"/>
    </k-form-item>
    <k-form-item label="过往从业经历" :data-col="2">
      <k-field-text v-model="value.pastEmploymentExp" inputType="textarea" :data-max-length="255"
                    :data-allowblank="flag"/>
    </k-form-item>
    <k-form-item label="是否被处罚">
      <k-field-select v-model="value.isPunish" data-dict="is_punish" :data-max-length="1" :data-allowblank="flag"/>
    </k-form-item>
    <k-form-item label="国籍">
      <k-field-select v-model="value.nationality" data-dict="t8_nation_code" :data-default-value="'CHN'"
                      :data-max-length="128" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="学位学历">
      <k-field-text v-model="value.education" :data-max-length="128" :data-allowblank="flag"/>
    </k-form-item>
    <k-form-item label="是否登记">
      <k-field-select v-model="value.isRegister" data-dict="is_register" :data-max-length="1" :data-allowblank="flag"/>
    </k-form-item>
    <k-form-item label="登记编号">
      <k-field-text v-model="value.register" :data-max-length="128" :data-allowblank="true"/>
    </k-form-item>
    <k-form-item label="公司入职日期">
      <k-field-date v-model="value.postDate" :data-allowblank="flag"/>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="addBut"
             data-from="addCustomerInfoForm" :data-model="value" data-target="customerInfoGrid"
             :data-handler="idCodeData">
        <md-icon md-src="/static/svg/confirm.svg"/>
        确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"/>
        取消
      </k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
import kayak from '@/frame/kayak.js'
//import ListWorkDay from "./ListWorkDay";
import Tools from "@/utils/tools";

export default {
  props: {
    updSuccess: Function
  },
  data() {
    return {
      flag: true
    };
  },
  methods: {
    isBlank(val) {
      if (val == 2) {
        this.flag = false
      } else {
        this.flag = true
      }
      this.httpUtil.comnQuery({
        action: "T8ProdCustomerInfo.findCustomerInfoByJobNoAndType",
        params: {custType: val},
        successAlert: false
      }).then(data => {
        if (data.returndata.data.length != 0) {
          this.value.jobno = ''
          .alert("该工号下的人物类型已存在", "danger");
        }

      })


    },
    inputJobno(value) {
      if (this.value.jobno != '' && this.value.custType != '') {
        this.httpUtil.comnQuery({
          action: "T8ProdCustomerInfo.findCustomerInfoByJobNoAndType",
          params: {custType: this.value.custType},
          successAlert: false
        }).then(data => {
          if (data.returndata.data.length != 0) {
            //this.value.jobno = ''
            this.$set(this.value,'jobno','')
            Tools.alert("该工号下的人物类型已存在", "danger");
            return
          } else {
            //获取角色
            this.httpUtil.comnQuery({
              action: "T8ProdCustomerInfo.getRoleName",
              successAlert: false
            }).then(data => {
              if(JSON.stringify(data.returndata.roleName).indexOf('超级管理员') > -1){
                //超级管理员放行
                this.getUserInfo(value)
              } else {
                if (value != data.returndata.loginname) {
                  Tools.alert("只能维护自己的信息", "danger");
                  this.value.jobno = '';
                  return
                } else {
                  //修改自己信息
                  this.getUserInfo(value)
                }

              }

            })
          }

        })
      }


    },
    inputCustName(name) {
      if (name != '' && name != undefined && name != null) {
        if(this.value.brief == null || this.value.brief == '' ||this.value.brief == undefined){
          let str = name+","
          //console.log("str=:>>",str)
          this.$set(this.value, 'brief',str);
          //console.log("this.value.brief",this.value.brief);
        }
      }
    },
    getUserInfo(value) {
      this.httpUtil.comnQuery({
        action: "User.findUserByLoginName",
        params: {loginname: value},
        successAlert: false
      }).then(data => {
        if (data.rows.length > 0) {
          if (data.rows[0].username != '') {
            this.$set(this.value, 'custName', data.rows[0].username)
          }
          this.value.custName = data.rows[0].username
          if (data.rows[0].idtype != '') {
            this.$set(this.value, 'idType', data.rows[0].idtype)
          }
          if (data.rows[0].idno != null) {
            this.$set(this.value, 'idCode', data.rows[0].idno)
          }
        }

      })
    },
    idCodeData(val) {
      //让按钮处于加载状态
      this.$refs.addBut.setIconStyle(0, []);
      //表单验证
      let re = this.$refs.addCustomerInfoForm.validate();
      if (re === false) {
        return;
      }
      if (val.idType == 0) {
        let StrNo = val.idCode.toString();
        let errors = [];
        if (StrNo.length == 15) {
          if (!Tools.isValidDate("19" + StrNo.substr(6, 2) + StrNo.substr(8, 2) + StrNo.substr(10, 2))) {
            errors.push('身份证号码错误：出生日期不正确');
            //return '身份证号码错误：出生日期不正确';
          }
        } else if (StrNo.length == 18) {
          if (!Tools.isValidDate(StrNo.substr(6, 4) + StrNo.substr(10, 2) + StrNo.substr(12, 2))) {

            errors.push('身份证号码错误：出生日期不正确');
            //return '身份证号码错误：出生日期不正确';
          }
        } else {
          errors.push('身份证号码必须为15位或者18位');
          //return '身份证号码必须为15位或者18位';
        }

        if (StrNo.length == 18) {
          let a, b, c;
          if (!Tools.isNumber(StrNo.substr(0, 17))) {
            errors.push('身份证号码错误：前17位不能含有英文字母');
            //return '身份证号码错误：前17位不能含有英文字母';
          }
          a = parseInt(StrNo.substr(0, 1)) * 7 + parseInt(StrNo.substr(1, 1)) * 9 + parseInt(StrNo.substr(2, 1)) * 10;
          a = a + parseInt(StrNo.substr(3, 1)) * 5 + parseInt(StrNo.substr(4, 1)) * 8 + parseInt(StrNo.substr(5, 1)) * 4;
          a = a + parseInt(StrNo.substr(6, 1)) * 2 + parseInt(StrNo.substr(7, 1)) * 1 + parseInt(StrNo.substr(8, 1)) * 6;
          a = a + parseInt(StrNo.substr(9, 1)) * 3 + parseInt(StrNo.substr(10, 1)) * 7 + parseInt(StrNo.substr(11, 1)) * 9;
          a = a + parseInt(StrNo.substr(12, 1)) * 10 + parseInt(StrNo.substr(13, 1)) * 5 + parseInt(StrNo.substr(14, 1)) * 8;
          a = a + parseInt(StrNo.substr(15, 1)) * 4 + parseInt(StrNo.substr(16, 1)) * 2;
          b = a % 11;
          if (b == 2) {   //最后一位为校验位
            c = StrNo.substr(17, 1).toUpperCase();   //转为大写X
          } else {
            c = parseInt(StrNo.substr(17, 1));
          }
          switch (b) {
            case 0:
              if (c != 1) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：1';*/
              }
              break;
            case 1:
              if (c != 0) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：0";*/
              }
              break;
            case 2:
              if (c != "X") {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：X";*/
              }
              break;
            case 3:
              if (c != 9) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：9";*/
              }
              break;
            case 4:
              if (c != 8) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：8";*/
              }
              break;
            case 5:
              if (c != 7) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：7";*/
              }
              break;
            case 6:
              if (c != 6) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：6";*/
              }
              break;
            case 7:
              if (c != 5) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*最后一位应该为：5";*/
              }
              break;
            case 8:
              if (c != 4) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：4";*/
              }
              break;
            case 9:
              if (c != 3) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：3";*/
              }
              break;
            case 10:
              if (c != 2) {
                errors.push('身份证号码校验位错');/*return '身份证号码校验位错';/*：最后一位应该为：2";*/
              }
          }
        } else {//15位身份证号
          if (!Tools.isNumber(StrNo)) {
            errors.push('身份证号码错误：前15位不能含有英文字母');
          }
        }
        if (errors.length > 0) {
          Tools.alert(errors[0], "danger");
          return false;
        }
      }

      //add zhangchangsi 20220126 前台先判断数据库是否存在相同的数据
     var _this = this;
      this.httpUtil.comnQuery({
        action: 'T8ProdCustomerInfo.checkDateCompliance',
        params: val,
        successAlert: false,
      }).then(data => {
        if (data.success) {
          //验证通过执行ajax
          this.httpUtil.comnUpdate({
            action: "T8ProdCustomerInfo.addT8ProdInfo",
            params: val
          }).then(data => {
            //让按钮取消加载状态
            this.$refs.addBut.setIconStyle(1, []);
            if (data.success) {
              _this.$emit("closeAddPopup", data);
            }else {
              //让按钮取消加载状态
              this.$refs.addBut.setIconStyle(1, []);
            }
          });
        } else {
          //让按钮取消加载状态
          this.$refs.addBut.setIconStyle(1, []);
        }
      })

      return false;
    },

  },
  computed: {
    value() {
      return this.$attrs.value
    }
  },
  watch: {
    'value.jobno': function (newval, oldVal) {

    }
  }
};
</script>
