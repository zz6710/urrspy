// 获取token
function getToken() {
  var str = document.cookie;
  var arr = str.split(";");
  var token;
  for (var i = 0; i < arr.length; i++) {
    var value = arr[i].split("=");
    if (value[0].trim() == 'Authorization') {
      token = value[1];
    }
  }
  return token;
}

function get_random() {
  let d = new Date();
  let str = '';
  str += d.getFullYear();
  str += d.getMonth() + 1;
  str += d.getDate();
  str += d.getHours();
  str += d.getMinutes();
  str += d.getSeconds();
  let r = (parseInt(1000 * Math.random())).toString();
  return str + r;

}

/**
 * 获取URL上指定参数
 *
 * @param variable
 * @returns {string}
 */
function getUrlParams(variable) {
  let search = window.location.search.substring(1);
  let params = decodeURI(decodeURI(search)).split("&")
  for (let i = 0; i < params.length; i++) {
    let item = [params[i].substring(0, params[i].indexOf("=")), params[i].substring(params[i].indexOf("=") + 1)]
    if (item[0] === variable) {
      return item[1]
    }
  }
}

/**
 * 报表发送请求
 *
 * @param data
 * @returns {Promise<unknown>}
 */
function sendRequest(data) {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest();
    xhr.open(data.type ? data.type : "POST", data.url);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.setRequestHeader('Authorization', getToken());
    let val = "";
    Object.keys(data.params).map((key, index) => {
      if (index > 0) {
        val += "&" + key + "=" + data.params[key]
      } else {
        val = key + "=" + data.params[key]
      }
    });
    xhr.send(val);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        let resData = JSON.parse(xhr.response)
        if(data.callback){
          data.callback(resData)
        }
        resolve(resData)
      }
    }
  })
}

/**
 * 构造查询URL
 *
 * @param baseUrl
 * @param exeid
 * @param params
 * @returns {string}
 */
function setXmlUrl(baseUrl, exeid, params) {
  let str = exeid;
  for (let key in params) {
    str += "&" + key + "=" + encodeURI(encodeURI(params[key]));
  }
  return serverUrl + "getResultData.json?exeid=" + str + "&id=" + get_random();
}

/**
 * 获取报表xml
 *
 * @param baseUrl
 * @param forTable
 * @returns {Promise<unknown>}
 */
function getXmlInfo(data) {
  return new Promise(resolve => {
    sendRequest({
      url: data.baseUrl + "commQuery/ReportXml/findXmlInfo.json",
      params: {
        forTable: data.forTable
      }
    }).then(res => {
      if (res.token_freshen) {
        document.cookie = "Authorization=" + res.token + ";path=/"
        getXmlInfo(data)
        return
      }
      if(res.rows && res.rows.length > 0){
        resolve(res.rows[0].xml)
      } else {
        if(data.errorback){
          data.errorback()
        }
      }
    })
  })
}

/**
 * 获取查询项对应exeid
 *
 * @param baseUrl
 * @param forTable
 * @returns {Promise<unknown>}
 */
function getXmlSql(data) {
  return new Promise((resolve => {
    sendRequest({
      url: data.baseUrl + "commQuery/ReportXmlSql/findXmlSqlInfo.json",
      params: {
        forTable: data.forTable
      }
    }).then(res => {
      if (res.token_freshen) {
        document.cookie = "Authorization=" + res.token + ";path=/"
        getXmlInfo(data)
        return
      }
      if(res.rows && res.rows.length > 0){
        resolve(res.rows)
      }
    })
  }))
}

/**
 * 获取treeList的tableSort
 *
 * @param baseUrl
 * @param forTable
 * @returns {Promise<unknown>}
 */
function getTreeListTableSort(data) {
  return new Promise((resolve => {
    sendRequest({
      url: data.baseUrl + "commQuery/ReportXml/findTreeListTableSort.json",
      params: {
        forTable: data.forTable
      }
    }).then(res => {
      if (res.token_freshen) {
        document.cookie = "Authorization=" + res.token + ";path=/"
        getXmlInfo(data)
        return
      }
      if(res.rows && res.rows.length > 0){
        resolve(res.rows[0].tableSort)
      }
    })
  }))
}




