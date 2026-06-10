
import _ from 'lodash';

let ParamUtils = {};

/**
 * 获取组件方法名列表
 * @returns 
 */
ParamUtils.getMethodList = function(methods){
  if(methods && methods.length > 0){
    let methodsList = methods.map( v=> {
      if(v.functionName){
        return v.functionName;
      }
    });
    return _.uniq(methodsList)
  } else {
    return []
  }
}

export default ParamUtils;
