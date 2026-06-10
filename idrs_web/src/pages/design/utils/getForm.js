import Tools from '@/utils/tools.js';
import httpUtil from '@/frame/httpUtil.js';

export const findConfigById = (id) => {
  return new Promise((resolve, reject) => {
    httpUtil.comnQuery({
      action: "LowCodeConfig.findConfigById",
      params: { id: id },
    }).then((data) => {
      if (!data.success || JSON.stringify(data.returndata) == '{}') {
        setTimeout(() => {Tools.alert('表单内容为空', "danger");}, 500)
      }
      resolve(data.returndata)
    });
  })
}
