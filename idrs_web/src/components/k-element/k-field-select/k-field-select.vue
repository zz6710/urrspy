<template>
  <el-select ref="elSelect" v-click-outside="clickOutSide"
             :value="dataValue"
             :multiple="this.dataMultiple === true || this.dataMultiple === 'true'"
             :clearable="dataClearable === true || dataClearable === 'true'"
             :placeholder="dataPlaceholder"
             :disabled="dataDisabled === true || dataDisabled === 'true'"
             :remote="dataRemote === 'true' || dataRemote === true"
             :remote-method="remoteMethod"
             :filterable="dataFilterable === true || dataFilterable === 'true'"
             :filter-method="dataPaging===true?filterMethod:null"
             :popper-append-to-body="true"
             @input="handleInput"
             @change="handleChange"
             @blur="handleBlur"
             @focus="handleFocus"
             @visible-change="handleVisibleChange">
    <el-option v-for="(item,index) in options" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled">
    </el-option>
    <div class="list-more" @click="loadMore" v-if="dataPaging===true&&page.pageNo<page.totalPage">查看更多</div>
  </el-select>
</template>

<script>
import props from "@/components/k-element/common/k-field-props.js";
import event from "@/components/k-element/common/k-field-event.js";
import emitter from "@/components/k-element/common/k-emitter.js";
import Tools from "@/utils/tools.js";

export default {
  name: 'KFieldSelect',
  mixins: [props(), event(), emitter()],
  props: {
    dataMultiple: {
      type: [Boolean, String],
      default: false
    },
    dataFilterable: {
      type: [Boolean, String],
      default: true
    },
    //显示字段，可同时多个
    dataDisplayField: {
      type: String,
      default: "itemval"
    },
    //值字段
    dataValueField: {
      type: String,
      default: "itemkey"
    },
    //显示字段分隔符
    dataValueSeparator: {
      type: String,
      default: "-"
    },
    dataPlaceholder: {
      type: String,
      default: "请选择"
    },
    dataAfterLoad: {
      type: Function
    },
    dataRemote: {
      type: [Boolean, String],
      default: false
    },
    dataRemoteField: {
      type: String
    },
    dataRemoteDataSize: {
      type: Number,
      default: 10
    },
    dataReloadRef: {
      type: String,
      default: ""
    },
    dataReloadName: {
      type: String,
      default: ""
    },
    dataPaging:{
      type:Boolean,
      default: false
    },
    dataPagingParamsName:{
      type: String,
      default: ""
    },
    dataContentType: {
      type: String,
      default: 'form'
    },
    dataDictType: {
			type: String,
			default: "" // 1:   key value
		},
    dataDictFilter: {
			type: Array,
			default: () => {
        return []
      }
		},
    dataDictExcludeFilter: {
			type: Array,
			default: () => {
        return []
      }
		},
    dataRemotePaging: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      page:{
        pageNo:1,
        pageSize:10,
        totalPage:0,
      },
      options: [],
      rows: [],
      cacheParams:{}
    }
  },
  computed: {
    dataValue() {
      if (this.dataMultiple === true || this.dataMultiple === 'true') {
        let values = [];
        if (this.value) {
          values = this.value.split(",");
        }
        return values;
      } else {
        return this.value;
      }
    }
  },
  watch:{
    dataData(newVal){
      this.options=[]
      this.handleData(newVal)
    },
    value(newVal,oldVal){
      if(this.dataReloadRef){
        this.dataReloadRef.split(",").forEach(item=>{
          this.getParentRef(item)['cacheParams'][this.dataReloadName]=newVal
          this.getParentRef(item).load(this.getParentRef(item).getDataParams())
          this.getParentRef(item).handleInput('')
        })
      }
    },
  },
  created() {
    let params=this.getDataParams()
    if (this.dataPaging===true && (this.dataRemote === true || this.dataRemote === 'true')) {
      console.error("远程搜索和自定义分页搜索不能同时设置")
      return;
    }
    if (this.dataAutoLoad === true || this.dataAutoLoad === 'true') {
      if (this.dataRemote === true || this.dataRemote === 'true') {
        let dataDisplayFields = this.dataDisplayField.split(",");
        for (let i = 0; i < dataDisplayFields.length; i++) {
          params[dataDisplayFields[i]] = this.value;
        }
      }
      this.load(params);
    }
  },
  methods: {
    clickOutSide(){
      this.$refs.elSelect.blur()
    },
    remoteMethod(query) {
      let params;
      if (typeof(this.dataParams) === "string") {
        params = Tools.str2Json(this.dataParams);
      } else {
        params = this.dataParams;
      }

      if (!params) {
        params = {}
      }

      let dataDisplayFields = this.dataDisplayField.split(",");

      // 远程搜索限制返回
      params.start = 0;
      params.limit = this.dataRemoteDataSize;
      if (this.dataRemoteField) {
        params[this.dataRemoteField] = query;
      } else {
        for (let i = 0; i < dataDisplayFields.length; i++) {
          params[dataDisplayFields[i]] = query;
        }
      }

      // if (query !== '') {
      //   this.load(params)
      // } else {
      //   this.options = [];
      // }
      this.load(params);
    },
    getSelectObject() {
      if (!this.value || !this.rows || this.rows.length === 0) {
        return null;
      }

      for (let i = 0; i < this.rows.length; i++) {
        if (this.rows[i][this.dataValueField] === this.value) {
          return this.rows[i];
        }
      }
      return null;
    },
    getDataParams(){
      let params;
      if (typeof(this.dataParams) === "string") {
        params = Tools.str2Json(this.dataParams);
      } else {
        params = this.dataParams;
      }
      params={
        ...params,
        ...this.cachedParams
      }
      if(this.dataPaging || this.dataRemotePaging){
        params["start"]=(this.page.pageNo-1)*this.page.pageSize
        params["limit"]=this.page.pageSize
      }
      return params
    },
    handleVisibleChange(visible){
      if(visible){
        if(this.dataPaging===true){
          this.filterMethod()
        }
      }
    },
    //查看更多
    loadMore(){
      this.page.pageNo++
      let params=this.getDataParams()
      this.load(params)
    },
    load(params) {
      if(this.dataPaging!==true){
        this.rows=[]
        this.options = []
      }
      if (this.dataDict) { //加载数字字典
        this.loadDictData();
      } else if (this.dataAction) { //通过Action加载
        this.loadActionData(params);
      } else if (this.dataGraphql) { //通过Graphql加载
        this.loadGraphqlData(params);
      } else if (this.dataUrl) { //通过Url加载
        this.loadUrlData(params);
      } else if (this.dataData) { //直接指定源数据
        this.handleData(this.dataData)
      } else if (this.dataSqlinfo && this.id) { //直接执行SQL
        this.loadSqlInfoData(params);
      } else {
        console.error("未指定数据获取方式，请检查select元素属性配置");
      }
    },
    loadSqlInfoData() {
      this.httpUtil.sqlInfo(this.id).then(rows => {
        this.handleData(rows);
      });
    },
    filterMethod(query){
      if(!this.dataPagingParamsName){
        return
      }
      this.rows=[]
      this.options=[]
      this.page={
        pageNo:1,
        pageSize:5,
        totalPage:0,
      }
      let params=this.getDataParams();
      if(query){
        params[this.dataPagingParamsName]=query
      }
      this.load(params)
    },
    loadDictData() {
      this.httpUtil.dict(this.dataDict).then(rows => {
        this.handleData(rows);
      });
    },
    loadActionData(params) {
      this.httpUtil.comnQuery({
        action: this.dataAction,
        params: params
      }).then(data => {
        let rows = data.rows;
        this.handleData(rows);
        this.handlePage(data)
      });
    },
    loadGraphqlData(params) {
      this.httpUtil.graphqlQurey({
        graphql: this.dataGraphql,
        params: params
      }).then(data => {
        //获取请求头
        let graphqlFirst = this.dataGraphql.substring(this.dataGraphql.indexOf("{") + 1, this.dataGraphql.indexOf(
          "("));
        this.handleData(data[graphqlFirst].rows);
        this.handlePage(data[graphqlFirst])
      });
    },
    loadUrlData(params) {
      if (this.dataContentType === 'json') {
        this.httpUtil.ajaxJson({
          url: this.dataUrl,
          params: params,
        })
          .then((data) => {
            let rows = data.rows;
            this.handleData(rows);
            this.handlePage(data)
          });
      }else{
        this.httpUtil.ajax({
          url: this.dataUrl,
          params: params
        }).then(data => {
          let rows = data.rows;
          this.handleData(rows);
          this.handlePage(data)
        });
      }
    },
    handlePage(data){
      if(!this.dataPaging){
        return
      }
      let totalPage=(data.results%this.page.pageSize)==0?data.results/this.page.pageSize:(parseInt(data.results/this.page.pageSize)+1)
      this.page.totalPage=totalPage
    },
    //处理数据
    handleData(rows) {
      if(!Array.isArray(rows))
        return
      this.rows = JSON.parse(JSON.stringify(rows));
      this.rows.sort((a,b) => a.itemorder - b.itemorder);
      const options = [];
      this.rows.map(row => {
        //获取值
        let valueField = row[this.dataValueField];
        //获取显示值
        let displayField;
        let dataDisplayFields = this.dataDisplayField.split(",");

        for (let i = 0; i < dataDisplayFields.length; i++) {
          if (i == 0) {
            displayField = row[dataDisplayFields[i]];
          } else {
            displayField += this.dataValueSeparator + row[dataDisplayFields[i]];
          }
        }
        if (this.dataDictType == "1") {
          if (this.dataDictFilter.length) {
            if (this.dataDictFilter.includes(valueField)) {
              options.push({
                value: valueField,
                label: valueField+" "+displayField
              });
            }
          } else {
            if (this.dataDictExcludeFilter.length) {
              if (!this.dataDictExcludeFilter.includes(valueField)) {
                options.push({
                  value: valueField,
                  label: valueField+" "+displayField
                });
              }
            } else {
              options.push({
                value: valueField,
                label: valueField+" "+displayField
              });
            }
          }
        } else {
          if (this.dataDictFilter.length) {
            if (this.dataDictFilter.includes(valueField)) {
              options.push({
                value: valueField,
                label: displayField
              });
            }
          } else {
            if (this.dataDictExcludeFilter.length) {
              if (!this.dataDictExcludeFilter.includes(valueField)) {
                options.push({
                  value: valueField,
                  label: displayField
                });
              }
            } else {
              options.push({
                value: valueField,
                label: displayField
              });
            }
          }
        }
      });
      this.options = options;
      //排序数据字典 -- axin
      // this.options.sort((a , b) => a.value - b.value);

      this.$nextTick(()=> {
        if (this.dataAfterLoad) {
          this.dataAfterLoad()
        }
      })
    },
    handleChange(value){
      let optionItem
      this.rows.some((item,index)=>{
        if(item[this.dataValueField]==value){
          optionItem=item
          return true
        }
      })
      if (this.formatValue) {
        value = this.formatValue(value);
      }
      this.$emit('data-on-change', value,optionItem);
    },
    //数据格式化函数，再返回前做处理
    formatValue(value) {
      if (this.dataMultiple === true || this.dataMultiple === 'true') {
        let valueStr = "";
        if (value) {
          for (let i = 0; i < value.length; i++) {
            if (i == 0) {
              valueStr = value[i];
            } else {
              valueStr += "," + value[i];
            }
          }
        }
        return valueStr;
      } else {
        return value;
      }
    },
    doBlur(){
      this.$refs.elSelect.blur()
    },
    //校验函数
    doValidate() {
      return true;
    },
    //重置函数
    doReset() {

    }
  }
};
</script>
<style lang="scss" scoped>
.list-more {
  text-align: center;
  color: #417fff;
}
</style>