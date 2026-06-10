import kayak from '@/frame/kayak.js'
import httpUtil from '@/frame/httpUtil.js'
import moment from "moment";
import {MessageBox} from "element-ui";
import global from "@/frame/global";
import store from "@/store/modules/system"

let Tools = {};

Tools.numToCny = function (money) {
  // 汉字的数字
  let cnNums = new Array('零', '壹', '贰', '叁', '肆', '伍', '陆',
    '柒', '捌', '玖');
  // 基本单位
  let cnIntRadice = new Array('', '拾', '佰', '仟');
  // 对应整数部分扩展单位
  let cnIntUnits = new Array('', '万', '亿','万亿','兆');
  // 对应小数部分单位
  let cnDecUnits = new Array('角', '分', '毫', '厘');
  // 整数金额时后面跟的字符
  let cnInteger = '整';
  // 整型完以后的单位
  let cnIntLast = '元';
  // 最大处理的数字
  let maxNum = 999999999999999.9999;
  // 金额整数部分
  let integerNum;
  // 金额小数部分
  let decimalNum;
  // 输出的中文金额字符串
  let chineseStr = '';
  // 分离金额后用的数组，预定义
  let parts;
  if (money == '') {
    return '';
  }
  money = parseFloat(money);
  if (money >= maxNum) {
    // 超出最大处理数字
    return '';
  }
  if (money == 0) {
    chineseStr = cnNums[0] + cnIntLast + cnInteger;
    return chineseStr;
  }
  // 转换为字符串
  money = money.toString();
  if (money.indexOf('.') == -1) {
    integerNum = money;
    decimalNum = '';
  } else {
    parts = money.split('.');
    integerNum = parts[0];
    decimalNum = parts[1].substr(0, 4);
  }
  // 获取整型部分转换
  if (parseInt(integerNum, 10) > 0) {
    let zeroCount = 0;
    let IntLen = integerNum.length;
    for (let i = 0; i < IntLen; i++) {
      let n = integerNum.substr(i, 1);
      let p = IntLen - i - 1;
      let q = p / 4;
      let m = p % 4;
      if (n == '0') {
        zeroCount++;
      } else {
        if (zeroCount > 0) {
          chineseStr += cnNums[0];
        }
        // 归零
        zeroCount = 0;
        chineseStr += cnNums[parseInt(n)]
          + cnIntRadice[m];
      }
      if (m == 0 && zeroCount < 4) {
        chineseStr += cnIntUnits[q];
      }
    }
    chineseStr += cnIntLast;
  }
  // 小数部分
  if (decimalNum != '') {
    let decLen = decimalNum.length;
    for (let i = 0; i < decLen; i++) {
      let n = decimalNum.substr(i, 1);
      if (n != '0') {
        chineseStr += cnNums[Number(n)] + cnDecUnits[i];
      }
    }
  }
  if (chineseStr == '') {
    chineseStr += cnNums[0] + cnIntLast + cnInteger;
  } else if (decimalNum == '') {
    chineseStr += cnInteger;
  }
  return chineseStr;
}

Tools.getPreviousMonth = function () {
  var now = new Date();
  // 获取当前月份
  var currentMonth = now.getMonth();
  // 如果是1月，则上个月是去年的12月
  if (currentMonth === 0) {
    now.setFullYear(now.getFullYear() - 1);
    now.setMonth(11); // 12月
  } else {
    now.setMonth(currentMonth - 1);
  }
  // 格式化为 YYYYMM
  var year = now.getFullYear();
  var month = now.getMonth() + 1; // getMonth() 返回的月份是从0开始的
  return `${year}${String(month).padStart(2, '0')}`;


}
Tools.getCurrentTime = function () {
  var date=new Date();
  var year=date.getFullYear();
  /* 在日期格式中，月份是从0开始的，因此要加0
   * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
   * */
  var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
  var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
  var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
  var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
  var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
  // 拼接
  return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;


}
//获取时间 182200格式
Tools.getCurrentTime1 = function () {
  var date=new Date();
  var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
  var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
  var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
  // 拼接
  return hours+""+minutes+""+seconds;
}
Tools.str2Json = function (str) {
  if (!str) {
    return null;
  }
  var json = null;
  json = eval("(" + str + ")");
  return json;
}

Tools.isValidDate = function(v) {
  v = String(v);
  if(v.length==8){
    v = v.substr(0,4)+'-'+v.substr(4,2)+'-'+v.substr(6,2);
  }
  let r = v.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
  if(r==null){
    return false;
  }
  let d = new Date(r[1], r[3]-1,r[4]);
  return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
};

Tools.isNumber = function(oNum) {
  if(!oNum) return false;
  let strP=/^\d+(\.\d+)?$/;
  if(!strP.test(oNum)) return false;
  try{
    if(parseFloat(oNum)!=oNum) return false;
  }
  catch(ex)
  {
    return false;
  }
  return true;
};

Tools.enterConvert = function (fieldval, replaceQuotation) {
  if (fieldval) {
    fieldval = fieldval.replace(/\n/g, "@#n#@");
    if (replaceQuotation === false) {
      fieldval = fieldval.replace(/\"/g, "##");
    }
  }
  return fieldval;
}

/**
 * 格式化日期
 * @param fmt 格式化类型
 * @param date 日期对象
 * @returns {*}
 */
Tools.dateFormat = function(fmt, date) {
  let ret;
  const opt = {
    "Y+": date.getFullYear().toString(),        // 年
    "m+": (date.getMonth() + 1).toString(),     // 月
    "d+": date.getDate().toString(),            // 日
    "H+": date.getHours().toString(),           // 时
    "M+": date.getMinutes().toString(),         // 分
    "S+": date.getSeconds().toString()          // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  };
  for (let k in opt) {
    ret = new RegExp("(" + k + ")").exec(fmt);
    if (ret) {
      fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
    }
  }
  return fmt;
}

Tools.formatDate = function (date) {
  if (!date) {
    return "";
  }
  date = date + ''
  return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
}

Tools.formatMonth = function (date) {
  if (!date) {
    return "";
  }
  date = date + ''
  return date.substring(0, 4) + "年" + Number(date.substring(4, 6)) + "月";
}

Tools.formatTimeLong = function (time) {
  if (!time) {
    return "";
  }
  time = time + ''
  return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);;
}

Tools.formatDateTime = function (date, time) {
  if (!date || !time) {
    return "";
  }
  date = date + ''
  time = time + ''

  let result = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " ";
  if (time.length == 6) {
    return result + time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
  } else {
    return result + time.substring(0, 2) + ":" + time.substring(2, 4);
  }
}

Tools.formatDateTimeStr = function (datetime) {
  if (!datetime) {
    return "";
  }

  let result = datetime.substring(0, 4) + "-" + datetime.substring(4, 6) + "-" + datetime.substring(6, 8) + " "+datetime.substring(8, 10) + ":" + datetime.substring(10, 12) + ":" + datetime.substring(12, 14);
  return result;
}

Tools.diffDateTime = function (date1, time1, date2, time2) {
  let moment1 = moment(date1 + ' ' + time1);
  let moment2 = moment(date2 + ' ' + time2);

  let time = moment2.diff(moment1, 'second');
  let day = Math.floor(time/86400);
  time = time%86400;

  let hour = Math.floor(time/3600);
  time = time%3600;

  let minute = Math.floor(time/60);
  time = time%60;

  let second = time;

  return day + '天' + hour + '时' + minute + '分' + second + '秒';
}

Tools.formatTime = function (time) {
  if (!time) {
    return "";
  }
  time = time + ''
  return time.substring(0, 2) + ":" + time.substring(2, 4);
}

function formatMoney(s) {
  let dot = ','
  s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
  var l = s.split(".")[0].split("").reverse(),
    r = s.split(".")[1];
  t = "";
  for (i = 0; i < l.length; i++) {
    t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? dot : "");
  }
  return t.split("").reverse().join("") + "." + r;
}

Tools.formatMoney = function formatMoney(s) {
  if (!s) {
    return ""
  }
  let dot = ','
  s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";
  let l = s.split(".")[0].split("").reverse(),
    r = s.split(".")[1];
  let t = "";
  for (let i = 0; i < l.length; i++) {
    t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? dot : "");
  }
  return t.split("").reverse().join("") + "." + r;
}

Tools.apply = function (o, c) {
  if (o == null) {
    o = {};
  }
  if (o && c && typeof c == 'object') {
    for (var p in c) {
      o[p] = c[p];
    }
  }
  return o;
};


/**
 * 将数组转换成字符串
 */
Tools.array2str = function (arr, includeFunction) {
  if (arr == null) {
    return 'null';
  }
  var s = [],
    v, type;
  for (var i = 0; i < arr.length; i++) {
    v = arr[i], type = (typeof v);
    switch (type) {
      case 'function': //function对象不添加
        if (includeFunction === true) {
          v = '"' + String(v).replace(/[\n\r\t]/g, '').replace(/[\t]/g, ' ') + '"';
        } else {
          continue;
        }
        break;
      case 'string': //字符串添加双引号
        v = '"' + v + '"';
        break;
      case 'object':
        if (Tools.isArray(v)) {
          v = Tools.array2str(v, includeFunction);
        } else {
          v = Tools.json2str(v, includeFunction);
        }
        break;
    }
    s.push(v);
  }
  return '[' + s.join(',') + ']';
};

/**
 * 将JSON对象转换成字符串
 * 参数：includeFunction boolean类型，true则包含function值的属性也转成string，其他值则不转function属性，默认为false
 */
Tools.json2str = function (json, includeFunction, isConfOption) {
  if (json == null) {
    return 'null';
  }
  if (Tools.isArray(json)) {
    return Tools.array2str(json, includeFunction, isConfOption);
  }
  var s = [],
    k, v, type;
  for (k in json) {
    v = json[k], type = (typeof v);
    switch (type) {
      case 'function': //function对象不添加
        if (includeFunction === true) {
          //避免出现脚本错误，需要把一些特殊字符做替换
          v = String(v).replace(new RegExp('\/\/.*[\n\r]', 'g'), ' ') // 需要将单行注释给替换掉
            //换行和制表符
            .replace(new RegExp('[\n\r]', 'g'), '').replace(new RegExp('[\t]', 'g'), ' ');
          if (isConfOption === true) { //在confOption里
          } else {
            v = '"' + v + '"';
          }
        } else {
          continue;
        }
        break;
      case 'string': //字符串添加双引号
        var reg = new RegExp('"', "g"); //创建正则RegExp对象
        v = v.replace(reg, '\\"');
        v = '"' + v + '"';
        break;
      case 'object':
        if (Tools.isArray(v)) {
          v = Tools.array2str(v, includeFunction, isConfOption);
        } else {
          if (k == 'confOption') {
            v = '"' + Tools.json2str(v, includeFunction, true).replace(/["]/g, '\\"') + '"';
          } else {
            v = Tools.json2str(v, includeFunction, isConfOption);
          }
        }
        break;
    }
    s.push('"' + k + '":' + v);
  }
  return '{' + s.join(', ') + '}';
};

/**
 * 判断对象是否数组
 */
Tools.isArray = function (object) {
  if (object == null) {
    return false;
  }
  return object != null && typeof object == "object" &&
    object.splice != null && object.join != null && object.length != null;
};


Tools.downloadFileByParams = function (download_path, download_name, download_code) {
  $("#downloadForm").remove();
  var downloadFormHtml =
    '<form id="downloadForm" action="base/comn-download.action" method="post" class="k-hidden" target="_self">';
  downloadFormHtml += '<input type="text" name="download_path" value=' + download_path + ' />';
  downloadFormHtml += '<input type="text" name="download_name" value=' + download_name + ' />';
  downloadFormHtml += '<input type="text" name="download_code" value=' + download_code + ' />';
  downloadFormHtml += '</form>';

  var $downloadForm = $(downloadFormHtml);
  $downloadForm.appendTo($("body"));
  $downloadForm.submit();
}


Tools.download = function (path, filename) {
  httpUtil
    .download({
      url: "/base/comn-download.json",
      params: {"path": path}
    },filename)
}

Tools.batch_download = function (paths, filename) {
  httpUtil
    .download({
      url: "/base/comn-batch-download.json",
      params: {"paths": paths}
    },filename)
}

Tools.alert = function (message, type = global.notify_type.success, icon = "add_alert", horizontalAlign = "center", verticalAlign = "top") {
  kayak.app.$notify({
    timeout: 2500,
    message: message,
    icon: icon,
    horizontalAlign: horizontalAlign,
    verticalAlign: verticalAlign,
    type: type
  });
}
Tools.alertTime = function (message, type = global.notify_type.success, time, icon = "add_alert", horizontalAlign = "center", verticalAlign = "top") {
  kayak.app.$notify({
    timeout: time,
    message: message,
    icon: icon,
    horizontalAlign: horizontalAlign,
    verticalAlign: verticalAlign,
    type: type
  });
}

Tools.alertDiy = function (message, type = global.notify_type.success, icon = "add_alert", horizontalAlign = "center", verticalAlign = "top") {
  kayak.app.$notify({
    timeout: 5000,
    message: message,
    icon: icon,
    horizontalAlign: horizontalAlign,
    verticalAlign: verticalAlign,
    type: type
  });
}

Tools.confirm = function (callback, message, title, type, confirmButtonText, cancelButtonText, buttonClass) {
  let dataDescribe = message ? message : '确定执行该操作吗？'
  MessageBox.confirm(dataDescribe, title ? title : '操作提示', {
    confirmButtonText: confirmButtonText? confirmButtonText : '确定',
    cancelButtonText: cancelButtonText ? cancelButtonText : '取消',
    cancelButtonClass: buttonClass ? buttonClass : 'el-button--info',
    type: type ? type : 'warning',
    beforeClose: (action, instance, done) => {
      if (action === 'confirm') {
        instance.confirmButtonLoading = true;
        instance.confirmButtonText = '执行中...';

        callback();
        done();
        instance.confirmButtonLoading = false;
      } else {
        done();
      }
    }
  }).catch(() => {});
}
/**
 * 获取用户登录信息(当期用户id,当前用户id所拥有的角色,菜单配置的接口权限)
 */
Tools.getLoginUser = function () {
  return new Promise((resolve, reject) => {
    if (!Tools.loginUser) {
      httpUtil.ajax({
        url: 'getLoginUser.json',
        dataAfterSuccess: function (json) {
          Tools.loginUser = json.returndata;
          resolve(Tools.loginUser);
        }
      });
    } else {
      resolve(Tools.loginUser);
    }
  });
}

/**
 * 计算字符串字节长度(汉字两个字节)
 * @param str 目标字符串
 * @returns {number} 字节长度
 */
Tools.strByteLength = function(str) {  //获取字符串的字节数，扩展string类型方法
  if (!str || str.length == 0) {
    return 0;
  }
  let b = 0;
  let l = str.length;  //初始化字节数递加变量并获取字符串参数的字符个数
  b=l;
/*    for(let i = 0; i < l; i ++) {  //遍历字符串，枚举每个字符
      if(str.charCodeAt(i) > 255) {  //字符编码大于255，说明是双字节字符
        b += 2;  //则累加2个
      }else {
        b ++;  //否则递加一次
      }
    }*/
    return b;  //返回字节数
}

/**
 * 加法函数，用来得到精确的加法结果
 * @param arg1
 * @param arg2
 * @returns {number}
 */
Tools.accAdd =function(arg1, arg2) {
  var r1, r2, m, c;
  try {
    r1 = arg1.toString().split(".")[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split(".")[1].length;
  } catch (e) {
    r2 = 0;
  }
  c = Math.abs(r1 - r2);
  m = Math.pow(10, Math.max(r1, r2));
  if (c > 0) {
    var cm = Math.pow(10, c);
    if (r1 > r2) {
      arg1 = Number(arg1.toString().replace(".", ""));
      arg2 = Number(arg2.toString().replace(".", ""));
    } else {
      arg1 = Number(arg1.toString().replace(".", ""));
      arg2 = Number(arg2.toString().replace(".", ""));
    }
  } else {
    arg1 = Number(arg1.toString().replace(".", ""));
    arg2 = Number(arg2.toString().replace(".", ""));
  }
  return (arg1 + arg2) / m;
}

Tools.numberAdd = function(arg1, arg2) {
  var r1, r2, m, n;
  try {
    r1 = arg1.toString().split(".")[1].length
  } catch (e) {
    r1 = 0
  }
  try {
    r2 = arg2.toString().split(".")[1].length
  } catch (e) {
    r2 = 0
  }
  m = Math.pow(10, Math.max(r1, r2))
  n = (r1 >= r2) ? r1 : r2;
  return ((arg1 * m + arg2 * m) / m).toFixed(n);
}

/**
 ** 减法函数，用来得到精确的减法结果
 ** 说明：javascript的减法结果会有误差，在两个浮点数相减的时候会比较明显。这个函数返回较为精确的减法结果。
 ** 调用：accSub(arg1,arg2)
 ** 返回值：arg1减去arg2的精确结果
 **/
Tools.accSub=function(arg1, arg2) {
  var r1, r2, m, n;
  try {
    r1 = arg1.toString().split(".")[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split(".")[1].length;
  } catch (e) {
    r2 = 0;
  }
  m = Math.pow(10, Math.max(r1, r2)); //last modify by deeka //动态控制精度长度
  n = (r1 >= r2) ? r1 : r2;
  return ((arg1 * m - arg2 * m) / m).toFixed(n);
}

/**
 ** 乘法函数，用来得到精确的乘法结果
 ** 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
 ** 调用：accMul(arg1,arg2)
 ** 返回值：arg1乘以 arg2的精确结果
 **/
Tools.accMul = function(arg0, arg1) {
  var m = 0, s1 = arg0.toString(), s2 = arg1.toString();
  try {
    m += s1.split(".")[1].length
  } catch (e) {
  }
  try {
    m += s2.split(".")[1].length
  } catch (e) {
  }
  return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
}

/**
 ** 除法函数，用来得到精确的除法结果
 ** 说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
 ** 调用：accDiv(arg1,arg2)
 ** 返回值：arg1除以arg2的精确结果
 **/
Tools.accDiv = function(arg1,arg2){
  var t1=0,t2=0,r1,r2;
  try{t1=arg1.toString().split(".")[1].length}catch(e){}
  try{t2=arg2.toString().split(".")[1].length}catch(e){}

  r1=Number(arg1.toString().replace(".",""));

  r2=Number(arg2.toString().replace(".",""));
  return (r1/r2)*Math.pow(10,t2-t1);
}

/**
 *
 * @param num
 * @param d 保留几位小数
 * @returns {string}
 */
Tools.toFixed = function (num, d){
  /*  var flag = 1
    if (num < 0) {
      flag = -1
      num *= -1
    }

    num = Math.round(num * Math.pow(10, n)) / Math.pow(10, n)+ Math.pow(10, -(n + 1));
    return (num*flag).toFixed(n);*/
  var s=num+"";

  if(!d)d=0;

  if(s.indexOf(".")==-1)s+=".";

  s+=new Array(d+1).join("0");

  if(new RegExp("^(-|\\+)?(\\d+(\\.\\d{0,"+(d+1)+"})?)\\d*$").test(s)){

    var s="0"+RegExp.$2,pm=RegExp.$1,a=RegExp.$3.length,b=true;

    if(a==d+2){

      a=s.match(/\d/g);

      if(parseInt(a[a.length-1])>4){

        for(var i=a.length-2;i>=0;i--){

          a[i]=parseInt(a[i])+1;

          if(a[i]==10){

            a[i]=0;

            b=i!=1;

          }else break;

        }
      }
      s=a.join("").replace(new RegExp("(\\d+)(\\d{"+d+"})\\d$"),"$1.$2");
    }if(b)s=s.substr(1);

    return (pm+s).replace(/\.$/,"");

  }return this+"";
}
/**
 *
 * @param num_str 处理科学计数法问题
 * @returns num
 */
Tools.toolNumber = function(num_str) {
  num_str = num_str.toString();
  if (num_str.indexOf("+") != -1) {
    num_str = num_str.replace("+", "");
  }
  if (num_str.indexOf("E") != -1 || num_str.indexOf("e") != -1) {
    var resValue = "",
      power = "",
      result = null,
      dotIndex = 0,
      resArr = [],
      sym = "";
    var numStr = num_str.toString();
    if (numStr[0] == "-") {
      //如果为负数，转成正数处理，先去掉‘-’号，并保存‘-’.
      numStr = numStr.substr(1);
      sym = "-";
    }
    if (numStr.indexOf("E") != -1 || numStr.indexOf("e") != -1) {
      var regExp = new RegExp(
        "^(((\\d+.?\\d+)|(\\d+))[Ee]{1}((-(\\d+))|(\\d+)))$",
        "ig"
      );
      result = regExp.exec(numStr);
      if (result != null) {
        resValue = result[2];
        power = result[5];
        result = null;
      }
      if (!resValue && !power) {
        return false;
      }
      dotIndex = resValue.indexOf(".") == -1 ? 0 : resValue.indexOf(".");
      resValue = resValue.replace(".", "");
      resArr = resValue.split("");
      if (Number(power) >= 0) {
        var subres = resValue.substr(dotIndex);
        power = Number(power);
        //幂数大于小数点后面的数字位数时，后面加0
        for (var i = 0; i <= power - subres.length; i++) {
          resArr.push("0");
        }
        if (power - subres.length < 0) {
          resArr.splice(dotIndex + power, 0, ".");
        }
      } else {
        power = power.replace("-", "");
        power = Number(power);
        //幂数大于等于 小数点的index位置, 前面加0
        for (var i = 0; i < power - dotIndex; i++) {
          resArr.unshift("0");
        }
        var n = power - dotIndex >= 0 ? 1 : -(power - dotIndex);
        resArr.splice(n, 0, ".");
      }
    }
    resValue = resArr.join("");
    return sym + resValue;
  } else {
    return num_str;
  }
}
/**
 * 关闭当前页面
 */
Tools.closeCurrentWindow = function (t) {
  let tabs;
  let data = t.$store.state.system.tab;
  tabs = typeof data === 'string' ? JSON.parse(data) : data;
  tabs.forEach((item, index) => {
    if (item.active) {
      Tools.removeTab(item, index, t)
    }
  })
}
Tools.removeTab = function (tab, index, t) {
  Tools.setExInclude(tab, 1, t)
  t.$store.commit("system/setTabSplice", {index: index, count: 1});
}
Tools.setExInclude = function (tab, type, t) {

  if (tab.meta && tab.meta.componentName) {
    let e = t.$store.state.system.exincludeList
    //设置为不缓存
    if (type == 1) {
      let a = false
      e.every((item) => {
        if (item == tab.meta.componentName) {
          a = true
          return false
        } else {
          return true
        }
      })
      if (!a) {
        e.push(tab.meta.componentName)
        t.$store.commit("system/setExincludeList", e);
      }
    } else {
      //设置为缓存
      let newArray = e.filter((item) => {
        return item != tab.meta.componentName
      })
      t.$store.commit("system/setExincludeList", newArray);
    }
  }
}


Tools.formatDateStr = function (date) {
  if (!date) {
    return "";
  }
  date = date + ''
  return date.replace("-","");
}

Tools.zeroFill = function (i) {
  if (i >= 0 && i <= 9) {
    return "0" + i;
  } else {
    return i;
  }
}

Tools.getCurrentDate = function (reg) {
  const now = new Date();
  const timeOne = new Date(now.getTime());
  const year = timeOne.getFullYear();
  let month = timeOne.getMonth() + 1;
  let day = timeOne.getDate();
  month = month < 10 ? '0' + month : month;
  day = day < 10 ? '0' + day : day;
  let currentDate = year + reg + month + reg + day;
  return currentDate;
}

Tools.getCurrentTime = function (reg1, reg2) {
  var date = new Date();//当前时间
  var month = Tools.zeroFill(date.getMonth() + 1);//月
  var day = Tools.zeroFill(date.getDate());//日
  var hour = Tools.zeroFill(date.getHours());//时
  var minute = Tools.zeroFill(date.getMinutes());//分
  var second = Tools.zeroFill(date.getSeconds());//秒
  //当前时间
  var curTime = date.getFullYear() + reg1 + month + reg1 + day
    + " " + hour + reg2 + minute + reg2 + second;
  return curTime;
}

export default Tools;
