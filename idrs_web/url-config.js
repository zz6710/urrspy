function getURL(){
  let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
  let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
  let index = urlPath.indexOf(docPath);
  let serverPath = urlPath.substring(0, index);
  return {
    onlineUrl :"http://127.0.0.1/",
    /*onlineUrl :"http://127.0.0.1/",*/
    baseUrl :"http://127.0.0.1:8202/",
    rptUrl :"http://127.0.0.1:38890/",
  }
}
