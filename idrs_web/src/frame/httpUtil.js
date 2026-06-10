import auth from "@/utils/auth.js";
import Tools from '@/utils/tools.js';
import axios from 'axios';
import ElementUI from 'element-ui';
import Qs from 'qs';
import kayak from '@/frame/kayak.js'
import moment from "moment";

let httpUtil = {};
//访问gateway地址
// const basePath = "http://127.0.0.1:7888/";
// const onlineUrl = "http://10.1.20.88:8888/";
//const basePath = "http://139.198.178.98:8888/";
let basePath=""
let onlineUrl ="";
let healthyPath ="";
try {
  basePath = getURL().baseUrl;
  onlineUrl = getURL().onlineUrl;
  healthyPath =getURL().healthyUrl;
}catch (e) {

}


httpUtil.basePath = basePath
httpUtil.onlineUrl = onlineUrl

httpUtil.comnUpdate = function(data) {
  let action = data.action;
  if (!action || action.indexOf(".") == -1) {
    return;
  }
  if (data.successAlert !== false ) {
    data.successAlert = true;
  }
  let actions = action.split(".");
  data.url = "commUpdate/" + actions[0] + "/" + actions[1] + ".json";

  return httpUtil.ajax(data);
};

httpUtil.update = function(data) {
  if (data.successAlert !== false) {
    data.successAlert = true;
  }
  return httpUtil.ajax(data);
};

httpUtil.query = function(data) {
  data.mask = false;
  return httpUtil.ajax(data);
};

httpUtil.graphqlQurey = function(data) {
  let graphql = data.graphql;
  if (data.params) {
    let graphqlFirst = graphql.substring(0, graphql.indexOf(")"));
    let graphqlSecond = graphql.substring(graphql.indexOf(")"));
    for (var key in data.params) {
      if (!data.params[key] && data.params[key] !== 0) {
        continue;
      }
      if (typeof data.params[key] == "string") {
        graphqlFirst += "," + key + ": \"" + Tools.enterConvert(data.params[key], data.replaceQuotation) + "\"";
      } else {
        graphqlFirst += "," + key + ":" + data.params[key];
      }
    }
    graphql = graphqlFirst + graphqlSecond;
  }

  data.params = graphql;

  data.url = "graphql.json";
  data.mask = false;
  return httpUtil.ajaxJson(data);
};

httpUtil.graphqlUpdate = function(data) {
  let graphql = data.graphql;

  if (data.params) {
    let graphqlFirst = graphql.substring(0, graphql.indexOf(")"));
    let graphqlSecond = graphql.substring(graphql.indexOf(")"));
    for (var key in data.params) {
      if (!data.params[key] && data.params[key] !== 0) {
        continue;
      }
      if (typeof data.params[key] == "string") {
        graphqlFirst += "," + key + ": \"" + Tools.enterConvert(data.params[key], data.replaceQuotation) + "\"";
      } else {
        graphqlFirst += "," + key + ":" + data.params[key];
      }
    }
    graphql = graphqlFirst + graphqlSecond;
  }

  data.params = graphql;

  data.url = "graphql.json";
  data.mask = false;
  data.successAlert = true;
  return httpUtil.ajaxJson(data);
};

httpUtil.comnQuery = function(data) {
  let action = data.action;

  if (!action || action.indexOf(".") == -1) {

    return;
  }

  let actions = action.split(".");

  data.url = "commQuery/" + actions[0] + "/" + actions[1] + ".json";
  data.mask = false;
  return httpUtil.ajax(data);
};

httpUtil.comnQueryTree = function(data) {
  let action = data.action;
  if (!action || action.indexOf(".") == -1) {
    return;
  }
  let actions = action.split(".");
  data.url = "commTreeQuery/" + actions[0] + "/" + actions[1] + ".json";
  return httpUtil.ajax(data);
};

const return_code = '0000'

httpUtil.ajax = function(data) {

  return new Promise((resolve, reject) => {
    axios({
      method: "POST",
      url: basePath + data.url,
      data: Qs.stringify(data.params),
      headers: {
        'Content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
        'Authorization': auth.getToken()
      }
    }).then(res => {
      let reData = res.data;
      if (data.callback) {
        data.callback(reData);
      }

      if (reData.success || reData.status === '200' || reData.rtnCode === return_code || reData.rows||reData.data) {
        if (reData.rtnCode) {
          Tools.alert(reData.rtnDesc || "操作成功");
        } else if (data.successAlert) {
          Tools.alert(reData.returnmsg || "操作成功");
        }

        if (data.dataAfterSuccess) {
          data.dataAfterSuccess(reData);
        }
        resolve(reData);
      } else {
        if (reData.login) {
          kayak.app.$router.push({
            path: '/loginAuth'
          });
          return;
        }
        if (reData.token_freshen) {
          auth.setToken(reData.token);
          httpUtil.ajax(data).then(_data => {
            resolve(_data);
          });
          return;
        }
        if (data.errCallback) {
          data.errCallback(reData);
        } else {
          Tools.alertTime(reData.returnmsg || reData.rtnDesc || "操作失败", "danger", 0);
          resolve(reData);
        }
      }
    }).catch(function(err) {
      if (data.callback) {
        data.callback(err);
      }
      Tools.alert("服务器异常，请稍后尝试", "danger");
      console.log(err);

    });
  });
};

/**
 * 进行一个文件上传请求，同时下载一个处理好的文件
 * @param {*} reqUrl 
 * @param {*} reqData 
 * @returns 
 */
httpUtil.uploadDownload = function(reqUrl, reqData){
  return new Promise((resolve, reject) =>{
    axios({
      method: "POST",
      url: basePath + reqUrl,
      data: reqData,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': auth.getToken()
      },
      responseType: "blob" //这行很关键,下载附件必须指定responseType为blob类型
    }).then(resp => {
      resolve(resp);
    });
  }); 
};

httpUtil.ajaxJson = function(data) {
  return new Promise((resolve, reject) => {
    axios({
      method: "POST",
      url: basePath + data.url,
      data: data.params,
      headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Authorization': auth.getToken()
      }
    }).then(res => {
      let reData = res.data;
      if (data.callback) {
        data.callback(reData);
      }
      if (reData.success || reData.rtnCode === return_code || reData.rows) {
        if (reData.rtnCode) {
          Tools.alert(reData.rtnDesc || "操作成功");
        } else if (data.successAlert) {
          Tools.alert(reData.returnmsg || "操作成功");
        }
        if (data.dataAfterSuccess) {
          data.dataAfterSuccess(reData);
        }
        resolve(reData);
      } else {

        if (reData.login) {
          kayak.app.$router.push({
            path: '/loginAuth'
          });
          return;
        }

        if (reData.token_freshen) {
          auth.setToken(reData.token);
          httpUtil.ajaxJson(data).then(_data => {
            resolve(_data);
          });
          return;
        }


        if (data.errCallback) {
          data.errCallback(reData);
        } else {
          Tools.alertTime(reData.returnmsg || reData.rtnDesc || "操作失败", "danger", 0);
        }
      }
    }).catch(function(err) {
      if (data.callback) {
        data.callback(err);
      }
      Tools.alert(reData.returnmsg || "服务器异常，请稍后尝试", "danger");
      console.log(err);
    });
  });
};

// excel导入专用（临时测试）
httpUtil.uploadForExcel = function (data, paramData) {
  return new Promise((resolve, reject) => {
    let formData = new FormData();
    for (let i = 0; i < data.files.length; i++) {
      formData.append("file", data.files[i]);
    }

    // 临时写死数据用于测试
    formData.append("asset_code", paramData.asset_code);
    formData.append("isprodorasset", paramData.isprodorasset);

    axios({
      method: "POST",
      url: getURL().baseUrl + data.url,
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data;charset=UTF-8',
        'Authorization': auth.getToken()
      }
    }).then(res => {
      if (res.token_freshen) {
        auth.setToken(res.token);
        httpUtil.upload(data).then(_data => {
          resolve(data);
        });
        return;
      }
      let reData = res.data;
      if (reData.success || reData.status === '200' || reData.rtnCode === return_code || reData.rows) {
        if (reData.rtnCode) {
          Tools.alert(reData.rtnDesc || "操作成功");
        } else if (data.successAlert) {
          Tools.alert(reData.returnmsg || "操作成功");
        }
        if (data.dataAfterSuccess) {
          data.dataAfterSuccess(reData);
        }
        resolve(reData);
      } else {
        if (data.errCallback) {
          data.errCallback(reData);
        } else {
          Tools.alertTime(reData.returnmsg || reData.rtnDesc || "操作失败", "danger", 0);
        }
      }
    }).catch(err => {
      if (data.callback) {
        data.callback(err);
      }
      console.log(err)
      Tools.alert("服务器异常，请稍后尝试", "danger");
    })
  })
}


// 报表上传专用
httpUtil.uploadForReport = function (data) {
  return new Promise((resolve, reject) => {
    let formData = new FormData();
    for (let i = 0; i < data.files.length; i++) {
      formData.append("files", data.files[i]);
    }
    axios({
      method: "POST",
      url: basePath + data.url,
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data;charset=UTF-8',
        'Authorization': auth.getToken()
      }
    }).then(res => {
      if (res.token_freshen) {
        auth.setToken(response.token);
        httpUtil.upload(data).then(_data => {
          resolve(data);
        });
        return;
      }
      let reData = res.data;
      if (reData.success || reData.status === '200' || reData.rtnCode === return_code || reData.rows) {
        if (reData.rtnCode) {
          Tools.alert(reData.rtnDesc || "操作成功");
        } else if (data.successAlert) {
          Tools.alert(reData.returnmsg || "操作成功");
        }
        if (data.dataAfterSuccess) {
          data.dataAfterSuccess(reData);
        }
        resolve(reData);
      } else {
        if (data.errCallback) {
          data.errCallback(reData);
        } else {
          Tools.alertTime(reData.returnmsg || reData.rtnDesc || "操作失败", "danger", 0);
        }
      }
    }).catch(err => {
      if (data.callback) {
        data.callback(err);
      }
      Tools.alert("服务器异常，请稍后尝试", "danger");
    })
  })
}

// 预览PDF专用
httpUtil.preview = function(data, fileName) {

  let _ajaxUrl;
  if (data.url.indexOf("http") == -1) {
    _ajaxUrl = basePath + data.url;
  } else {
    _ajaxUrl = data.url;
  }

  if (!data.params) {
    data.params = {}
  }

  axios({
    method: 'post',
    url: _ajaxUrl,
    data: Qs.stringify(data.params),
    responseType: 'blob',
    headers: {
      'Content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'Authorization': auth.getToken()
    }
  }).then(response => {

    if (response.headers['login']) {
      kayak.app.$router.push({
        path: '/loginAuth'
      });
      return;
    }
    if (response.headers['token_freshen']) {
      auth.setToken(response.headers['token_freshen']);
      httpUtil.download(data,fileName)
      return;
    }

    if (data.callback) {
      data.callback(response);
    }
    if (!response) {
      return
    }
    let newPage = window.open(
      window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf;charset=utf-8' }))
    )
    setTimeout(function () { newPage.document.title = fileName }, 300)
  }).catch((error) => {
    if (data.callback) {
      data.callback();
    }
    console.info(error);
  })
};


httpUtil.download = function(data, fileName) {

  let _ajaxUrl;
  if (data.url.indexOf("http") == -1) {
    _ajaxUrl = basePath + data.url;
  } else {
    _ajaxUrl = data.url;
  }

  if (!data.params) {
    data.params = {}
  }

  axios({
    method: 'post',
    url: _ajaxUrl,
    data: Qs.stringify(data.params),
    responseType: 'blob',
    headers: {
      'Content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
      'Authorization': auth.getToken()
    }
  }).then(response => {

    if (response.headers['login']) {
      kayak.app.$router.push({
        path: '/loginAuth'
      });
      return;
    }
    if (response.headers['token_freshen']) {
      auth.setToken(response.headers['token_freshen']);
      httpUtil.download(data,fileName)
      return;
    }

    if (data.callback) {
      data.callback(response);
    }
    if (!response) {
      return
    }
    let url = window.URL.createObjectURL(new Blob([response.data]));
    let link = document.createElement('a');
    link.style.display = 'none';
    link.href = url;

    let suffix = "";
    if (response.headers['filename']) {
      suffix = response.headers['filename'].substring(response.headers['filename'].lastIndexOf("."), response.headers[
        'filename'].length);
    }
    let cd = response.headers['content-disposition']
    let loadFileName
    if (cd && cd.indexOf('filename=') !== -1) {
      loadFileName = cd.substring(cd.indexOf('filename=')).substring(cd.substring(cd.indexOf('filename=')).indexOf('=') + 1)
    }
    if (fileName) {
      if(fileName.lastIndexOf(".")==-1)
        link.setAttribute('download', decodeURI(fileName + suffix));
        else
        link.setAttribute('download', decodeURI(fileName ));
    } else {
      link.setAttribute('download', decodeURI(loadFileName));
    }

    document.body.appendChild(link);
    link.click();

  }).catch((error) => {
    if (data.callback) {
      data.callback();
    }
    console.info(error);
  })
};
httpUtil.upload=function(data, fileName) {
  let _ajaxUrl;
  if (data.url.indexOf("http") == -1) {
    _ajaxUrl = basePath + data.url;
  } else {
    _ajaxUrl = data.url;
  }

  if (!data.params) {
    data.params = {}
  }

  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: _ajaxUrl,
      data: data.formData,
      headers: {
        'Content-Type':'multipart/form-data;charset=UTF-8',
        'Authorization': auth.getToken()
      }
    }).then(response => {
      let reData = response.data;
      if (reData.login) {
        kayak.app.$router.push({
          path: '/loginAuth'
        });
        return;
      }

      if (reData.token_freshen) {
        auth.setToken(reData.token);
        httpUtil.upload(data,fileName).then(_data => {
          resolve(_data);
        });
        return;
      }
      resolve(response);
    }).catch((error) => {
      if (data.callback) {
        data.callback();
      }
      console.info(error);
    })
  });

};

httpUtil.sysparam = function(paraid, moduleid = '0') {
  return new Promise((resolve, reject) => {
    httpUtil.comnQuery({
      action: "SystemParam.find",
      params: {
        moduleid: moduleid,
        paraid: paraid,
      }
    }).then(data => {
      if (data.rows && data.rows[0]) {
        resolve(data.rows[0].paravalue);
      } else {
        resolve();
      }
    }).catch(e => {
      reject(e);
    })
  });
};


//系统工作日  axin
httpUtil.sysDate = function() {
  return new Promise((resolve, reject) => {
    httpUtil.sysparam('10006', '0').then(res=>{
      let date = moment().format('YYYYMMDD');
      if (res) {
        if(res == '1') {
          httpUtil.sysparam('10004', '0').then(resDate=>{
            if (resDate) {
              resolve(resDate);
            }else{
              resolve(date);
            }
          });
        }else{
          resolve(date);
        }
      }
    })
  });

};

httpUtil.dictCache = {
};

httpUtil.getHealthyApp=function(data){
  return new Promise((resolve, reject) => {
      axios({
        method: "POST",
        url: healthyPath + data.url,
        data: data.params,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
        }
      }).then(res=>{
        resolve(res);
      })
    }
  )
}

httpUtil.dict = function(dict) {
  return new Promise((resolve, reject) => {
    let dt = httpUtil.dictCache[dict];
    if (dt == null) {
      let url = "base/dict/" + dict + ".json";
      httpUtil.ajax({
        url: url,
        mask: false
      }).then(data => {
        if (data.rows && data.rows.length > 0) {
          httpUtil.dictCache[dict] = data.rows;
        }
        return resolve(data.rows);
      });
    } else {
      return resolve(dt);
    }
  });
}

// 取sql动态字典数据
httpUtil.sqlInfo = function (sqlInfo) {
  return new Promise((resolve, reject) => {
    let url = "base/sqlInfo/sqlInfo.json";
    httpUtil.ajax({
      url: url,
      params: {"id": sqlInfo},
      mask: false
    }).then(data => {
      return resolve(data.rows);
    });
  });
}

httpUtil.dictTransfer = function(dict, key) {
  return new Promise((resolve, reject) => {
    if (dict === null || dict === '' || key === null || key === '') {
      return resolve('');
    }
    httpUtil.dict(dict).then(rows => { //取得字典数据
      if (!rows || rows.length == 0) { //取不到字典数据
        return key;
      }

      let vals = [];
      let keys = String(key).split(',');
      let len = keys.length;

      keys.map(key => {
        let value = "";
        rows.map(row => {
          if (row.itemkey == key) {
            value = row.itemval;
          }
        });
        vals.push(value);
      });

      return resolve(vals.join('，'));
    });
  });
}

export default httpUtil;
