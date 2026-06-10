<template>
  <div >
    <md-card class="box-card" style="overflow: visible;position: unset">
      <!-- <md-card-header class="md-card-header-text md-card-header-green" style="margin-right: 0;"
                      v-if="this.dataShowSubscript === null || this.dataShowSubscript">
        <div class="search-header">
          <div class="card-icon" :style="iconStyle">
            <md-icon md-src="/static/svg/form.svg"></md-icon>
          </div>
          <div>
            <i class="el-icon-search" @click="popup"></i>
            <i class="el-icon-d-caret" @click="show"></i>
          </div>
        </div>

      </md-card-header> -->

      <div slot="header" class="clearfix" style="text-align:right">

      </div>
      <div class="show-form" id="show-form">
        <k-form ref="searchForm" :data-col="0" :data-model="searchData" :data-label-width="dataLabelWidth">
          <k-form-item v-for="item in formList" v-bind:key="item.label" :label="item.label">
            <k-field-date v-if="item.inputHtml == 'KFieldDate' && item.inputConfig['data-type'] == 'daterange'" v-model="item.data"
              data-type="daterange" @data-startdate-change="item.startDate = $event" @data-enddate-change="item.endDate = $event"></k-field-date>
            <component v-else v-model="item.data" :is="item.inputHtml" v-bind="item.inputConfig" />
          </k-form-item>
          <k-form-item>
            <k-btn class="btn-custom-primary" :data-handler="query">
              <md-icon md-src="/static/svg/search.svg"></md-icon>
              查询
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="RESET" data-type="warning" data-from="searchForm">
              <md-icon md-src="/static/svg/reset.svg"></md-icon>
              重置
            </k-btn>
            <slot></slot>
          </k-form-item>
        </k-form>
      </div>

      <!-- <div class="k-form-search-footer">
        <k-btn class="md-success" :data-handler="query">
          <i class="icon-search" />
          查询
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="RESET" data-type="warning" data-from="searchForm">
          <i class="icon-reset" />
          重置
        </k-btn>
        <slot></slot>
      </div> -->
    </md-card>
    <k-popup data-width-percent="40%" ref="dialog" data-title="动态搜索" :data-mask="true" :data-confirm-close="false" data-confirm-describe="是否关闭弹出框"
      data-title-align="center" @data-close="closeDialog">
      <k-form dataTotalWidth="auto" :data-col="1" :data-model="formData">
        <k-form-item  label="查询条件" data-ui="element">
          <k-field-checkbox v-model="formData.selectValue" :data-data="this.list" data-display-field="label"
            data-value-field="label" :data-multiple="true"></k-field-checkbox>
        </k-form-item>
        <k-form-footer  dataAlign="center">
          <k-btn class="btn-custom-primary" data-functype="CLOSE" data-type="primary" :dataHandler="()=>dataConfirm()">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import {
    assign
  } from "lodash";
  import emitter from "@/components/k-element/common/k-emitter.js";
  import props from "@/components/k-element/common/k-field-props.js";


  import Tools from "@/utils/tools.js";
  import event from "@/components/k-element/common/k-field-event";

  export default {
    name: 'kFormSearch',
    mixins: [props(), emitter()],
    props: {
      dataModelName: {
        type: String
      },
      dataTarget: {
        type: String
      }
    },
    data() {
      return {
        extends: true,
        list: [],
        formList: [],
        obj: {
          label: '',
          value: '',
          inputConfig: {},
        },
        formData: {
          selectValue: ""
        },
        tempSelectValue: "",
        searchData: {},
        dataVisible: false,
        // 必填项默认展示，数据加载完成后添加到展示列表
        notAllowBlankList: [],
        // 用于保存时去掉不为空选项
        notAllowBlanks: {}
      }
    },
    computed: {
      iconStyle() {
        let iconStyle = {};
        iconStyle.background = this.$store.state.system.cardBackground
        return iconStyle;
      }
    },
    methods: {
      query() {
        this.$emit("click")
        if (!this.dataTarget) {
          console.error("k-from-search需要指定data-target.")
        }
        let target = this.getParentRef(this.dataTarget);
        if (target && target.$options.name == "KGrid") {
          let params = {};

          this.formList.forEach(item => {
            if (item.inputConfig['data-type'] == "daterange") { //日期区间处理
              if (item.startDate) {
                params[item.field] = item.startDate;
              }
              if (item.endDate) {
                params[item.inputConfig['endDateFeild']] = item.endDate;
              }
            } else {
              if (item.data && item.data.trim()) {
                params[item.field] = item.data
              }
            }
          })

          let re = this.$refs.searchForm.validate();
          if (re === false) {
            return;
          }

          target.load(params);
        } else {
          console.error("data-target不存在或data-target不是KGrid组件.")
        }

      },
      popup() {
        let dialog = this.$refs.dialog
        if (dialog.dataVisible == true) {
          dialog.close()
        } else if (dialog.dataVisible == false) {
          this.tempSelectValue = this.formData.selectValue;
          dialog.popup()
        }
      },
      closeDialog() {
        this.formData.selectValue = this.tempSelectValue;
      },
      show() {
        let e = document.getElementById('show-form')
        if (this.extends) {
          e.style.display = "none"
        } else {
          e.style.display = ""
        }
        this.extends = !this.extends
      },

      loadDefaultSearch() {
        this.httpUtil.query({
          url: "graphql/searchDefault.json",
          params: {
            modelName: this.dataModelName
          }
        }).then(data => {
          this.formList = [];
          this.formData.selectValue = "";
          let rows = data.rows;
          let temp = {};
          rows.map(model => {
            //名称和组件没配置不显示
            if (!model.label || !model.inputHtml) {
              return;
            }

            if (model.inputConfig) {
              model.inputConfig = Tools.str2Json(model.inputConfig);
            } else {
              model.inputConfig = {};
            }

            temp[model.field] = true;

            this.formList.push(model);
          });
          for (let i = 0; i < this.notAllowBlankList.length; i++) {
            let model = this.notAllowBlankList[i];
            if (temp[model.field]) {
              continue;
            } else {
              this.formList.unshift(model);
            }
          }

          for (let i = 0; i < this.formList.length; i++) {
            let model = this.formList[i];
            if (this.formData.selectValue) {
              this.formData.selectValue += "," + model.label
            } else {
              this.formData.selectValue = model.label
            }
          }
        });
      },
      loadAllSearch() {
        this.httpUtil.query({
          url: "graphql/search.json",
          params: {
            modelName: this.dataModelName
          }
        }).then(data => {
          this.notAllowBlankList = [];
          let rows = data.rows;
          rows.map(model => {
            if (!model.label) { //label没配置不显示
              return;
            }

            if (model.inputConfig) {
              model.inputConfig = Tools.str2Json(model.inputConfig);
            } else {
              model.inputConfig = {};
            }

            if (model.inputConfig.hasOwnProperty('data-allowblank') || model.inputConfig.hasOwnProperty(
                'dataAllowblank')) {
              if (model.inputConfig['data-allowblank'] === false || model.inputConfig['data-allowblank'] ===
                'false' ||
                model.inputConfig['dataAllowblank'] === false || model.inputConfig['dataAllowblank'] ===
                'false') {
                model.disabled = true
                this.notAllowBlankList.push(model);
                this.notAllowBlanks[model.label] = model;
              }
            }

            this.list.push(model);
          });
          this.loadDefaultSearch();
        });
      },
      dataConfirm() {
        let values = this.formData.selectValue.split(",");
        if (values && values.length > 0) {
          let selectValue = "";
          for (let i = 0; i < values.length; i++) {
            let value = values[i];
            if (!this.notAllowBlanks[value]) {
              selectValue += "," + value;
            }
          }
          if (selectValue.length > 0) {
            this.formData.selectValue = selectValue.substring(1);
          } else {
            this.formData.selectValue = "";
          }

          console.log("selectValue====>", selectValue)

        }
        this.tempSelectValue = this.formData.selectValue;
        this.httpUtil.update({
          url: "graphql/updateSearchDefault.json",
          params: {
            modelName: this.dataModelName,
            searchFields: this.formData.selectValue
          }
        }).then(data => {
          this.loadDefaultSearch();
        });
      },
    },
    mounted() {
      this.loadAllSearch();
    }

  }
</script>

<style lang="scss">
  @import './k-form-search.scss';
</style>
