
<script>
import httpUtil from "@/frame/httpUtil";
export default {
  name: "AssetComFunction",
  /**
   * 字典值域
   * @param item
   * @param field 值域存储对象
   * @param dict
   * @param itemKeys 区域字典值，多个字典值逗号隔开，并且必须是数字
   * @param like 是否模糊
   * @return VALUE,TEXT
   */
  areaDict(item,field,dict,itemKeys,like){
    let url="";
    if (like){
      url='BondInfoModel.findDictLike'
    }else {
      url='BondInfoModel.findDict'
    }
    httpUtil.comnQuery({
      action: url,
      params: {dict: dict,itemkey: itemKeys}
    }).then(data => {
      item.$set(item.$data,field,data.rows);
    }).catch({})
  },
  /**
   * 转换空默认值为数值
   * @param val
   */
  dealTransInt(val){
    if (!val){ val='0';}
    return val;
  },
  /**
   * 补录字段默认值处理
   * @param val
   */
  dealDefaultVal(field,defaultVal){
    if (!field){ field=defaultVal;}
    return field;
  },
  /**
   * 资产修改补录字段权限控制
   * @param item
   * @param formData
   * @param field
   * @param page
   * @param fieldType
   * @param isDetailShow 是否仅为了展示字段详情
   */
  checkColumn(item,formData,field,page,fieldType,isDetailShow) {
    let formDatas = {...item[formData]};
    for (let f in formDatas) {
      formDatas[f + field] = true
    }
    httpUtil.comnQuery({
      action: "AssetCollection.findColumns",
      params: {page: page,fieldType: fieldType}
    }).then(data => {
      if (data && data.rows.length > 0){
        let arr = data.rows[0].label.split(',')
        if (arr.length > 0 && !isDetailShow){
          arr.forEach(a =>{
            formDatas[a + field] = false
          })
        }
      }
      item[formData] = {...formDatas};
    }).catch({})
  },

  /**
   * 垃圾v-if判断不显示label后，不清除文本框内容;
   * 该方法处理这不合理的问题，选框、文本框添加id属性与v-model相同,如果需要不显示label但框内保持不清空，用v-show;
   * @param item
   * @param formData
   * @param itemForm
   * @param values
   */
  removeVifRubbish(item,formData,itemForm,values) {
    let aaa = Object.keys(values);
    a: for(let j = 0; j < aaa.length; j++){
      let oldLabel = aaa[j].toString();
      for (let i = 0; i < item.$refs[itemForm].formItems.length; i++) {
        let newLabel = item.$refs[itemForm].formItems[i].field.id;
        if (oldLabel===newLabel){
          continue a;
        }
      }
      item.$set(values,oldLabel, '');
    }
  },
  /**
   * 将item[formData]中与values中key相同的元素，赋值value
   * @param item
   * @param formData
   * @param values
   */
  dataCover(item,formData,values) {
    let formDatas = {...item[formData]};
    for (let key in formDatas) {
      if (values.hasOwnProperty(key)) {
        values[key] = formDatas[key];
      }
    }
  },
  /**
   * 去掉item[formData] 中与values中一样key的元素
   * @param item
   * @param formData
   * @param values
   */
  dataFilter(item,formData,values) {
    let formDataFilter = { ...item[formData]};
    Object.keys(values).forEach(key => formDataFilter.hasOwnProperty(key) && delete formDataFilter[key]);
    item[formData] = { ...formDataFilter}
  },
  /**
   * 去掉item[formData] 中与values中一样key的元素
   * @param item
   * @param formData
   * @param values
   */
  dataMerger(item,formData,values) {
    item[formData] = { ...item[formData], ...values };
  },
}
</script>
