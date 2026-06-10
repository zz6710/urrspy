<template>
  <div>
    <k-form-search-customize data-target="seriesGrid" v-model="prodSearchParam">
      <k-form-item label="系列代码">
        <k-field-text v-model="prodSearchParam.seriesCode"/>
      </k-form-item>
      <k-form-item label="系列名称">
        <k-field-text v-model="prodSearchParam.seriesName"/>
      </k-form-item>

      <k-btn slot="button" class="btn-custom-primary" style="width: 100px" data-functype="POPUP"
             :data-handler="()=>this.formData={}" data-target="addDeptPopup">
        <md-icon md-src="/static/svg/add.svg" />新增系列
      </k-btn>
      <k-btn slot="button" class="btn-custom-primary" style="width: 100px" data-functype="POPUP"
             :data-handler="()=>this.formData={}" data-target="addDeptPopup2">
        <md-icon md-src="/static/svg/add.svg" />新增子系列
      </k-btn>
    </k-form-search-customize>

    <k-grid ref="seriesGrid" id="seriesGrid" @data-row-select="selectRow" :data-before-load="dataBeforeLoad"
            data-action='T8ProdSeries.find'
            data-diffcondition="seriesCode,parentCode" :data-show-tree="true" dataTreeId="seriesCode"
            data-operate-width="120px">
      <k-grid-column data-header="系列代码" data-name="seriesCode"></k-grid-column>
      <k-grid-column data-header="系列名称" data-name="seriesName"></k-grid-column>
      <k-grid-column data-header="上级系列" data-name="parentSeriesName"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="系列修改" data-functype="POPUP" data-size="mini"
               data-target="editDeptPopup"
               v-if="global.isShowAuthorityButton('T8ProdSeries.updateT8ProdSeries')&&scope.row.row.parentSeriesName==''">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="子系列修改" data-functype="POPUP" data-size="mini"
               data-target="editDeptPopup2"
               v-if="global.isShowAuthorityButton('T8ProdSeries.updateT8ProdSeries')&&scope.row.row.parentSeriesName!=''">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <!--    添加系列弹出框   -->
    <k-popup ref="addDeptPopup" data-title="新增系列">
      <k-form ref="addSeriesForm" :data-col="1">
        <k-form-item label="系列代码">
          <k-field-text v-model="formData.seriesCode" :dataAllowblank='false' :data-max-length="20"
                        data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字"
                        @data-on-blur="isSeriesCode(formData.seriesCode)"/>
        </k-form-item>
        <k-form-item label="系列名称">
          <k-field-text v-model="formData.seriesName" :dataAllowblank='false' :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="系列品牌" v-show="false">
          <k-field-select v-model="formData.prodBrand"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSeries.addT8ProdSeries"
                 data-target="seriesGrid" data-from="addSeriesForm"
                 :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    添加子系列弹出框   -->
    <k-popup ref="addDeptPopup2" data-title="新增子系列">
      <k-form ref="addSeriesForm2" :data-col="1">
        <k-form-item label="上级系列">
          <k-field-tree :data-multiple="false" :dataAllowblank='false' :data-flat="false" v-model="formData.parentCode"
                        data-diffcondition="seriesCode,parentCode" data-action='T8ProdSeries.find'
                        data-display-child="children" data-placeholder="请选择父级系列" data-display-field="seriesName"
                        data-value-field="seriesCode">
          </k-field-tree>
        </k-form-item>
        <k-form-item label="子系列代码">
          <k-field-text v-model="formData.seriesCode" :dataAllowblank='false' :data-max-length="20"
                        data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字"
                        @data-on-blur="isSeriesCode(formData.seriesCode)"/>
        </k-form-item>
        <k-form-item label="子系列名称">
          <k-field-text v-model="formData.seriesName" :dataAllowblank='false' :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="子系列品牌" v-show="false">
          <k-field-select v-model="formData.prodBrand"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSeries.addT8ProdSonSeries"
                 data-target="seriesGrid" data-from="addSeriesForm2"
                 :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改系列弹出框   -->
    <k-popup ref="editDeptPopup" data-title="系列修改">
      <k-form ref="editSeriesForm" :data-col="1">
        <k-form-item label="系列代码">
          <k-field-text v-model="formData.seriesCode" :dataAllowblank='false' :data-max-length="20"
                        data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字"
                        @data-on-blur="isSeriesCode(formData.seriesCode)"/>
        </k-form-item>
        <k-form-item label="系列名称">
          <k-field-text v-model="formData.seriesName" :dataAllowblank='false' :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="系列品牌" v-show="false">
          <k-field-select v-model="formData.prodBrand"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSeries.updateT8ProdSeries"
                 data-target="seriesGrid" data-from="editSeriesForm"
                 :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改子系列弹出框   -->
    <k-popup ref="editDeptPopup2" data-title="子系列修改">
      <k-form ref="editSeriesForm2" :data-col="1">
        <k-form-item label="上级系列">
          <k-field-tree :data-multiple="false" :dataAllowblank='false' :data-flat="false" v-model="formData.parentCode"
                        data-diffcondition="seriesCode,parentCode" data-action='T8ProdSeries.find'
                        data-display-child="children" data-placeholder="请选择父级系列" data-display-field="seriesName"
                        data-value-field="seriesCode">
          </k-field-tree>
        </k-form-item>
        <k-form-item label="子系列代码">
          <k-field-text v-model="formData.seriesCode" :dataAllowblank='false' :data-max-length="20"
                        data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字"
                        @data-on-blur="isSeriesCode(formData.seriesCode)"/>
        </k-form-item>
        <k-form-item label="子系列名称">
          <k-field-text v-model="formData.seriesName" :dataAllowblank='false' :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="子系列品牌" v-show="false">
          <k-field-select v-model="formData.prodBrand"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSeries.updateT8ProdSeries"
                 data-target="seriesGrid" data-from="editSeriesForm2"
                 :data-model="formData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
    import Tools from '@/utils/tools.js';
    import {
        assign
    } from "lodash";

    export default {
        name:"M81013",
        name: "T8ProdSeries",
        data() {
            return {
                prodSearchParam: {},//查询参数
                formData: {},
                cascaderValue: '',
                selectRowData: {}
            };
        },
        computed: {
            queryParam() {
                return {
                    'seriesCode': this.prodSearchParam.seriesCode,
                    'seriesName': this.prodSearchParam.seriesName,
                }
            }
        },

        methods: {
            selectRow(row, column, event) {
                const _this = this
                _this.selectRowData = assign({}, row)
                _this.formData = assign({}, row)
                console.log(row)
            },
            dataBeforeLoad(data) {
                data.excSeriesCode = "ROOT";
                return data;
            },
            isSeriesCode(value) {
                this.httpUtil.comnQuery({
                    action: 'T8ProdSeries.isExistSeriesCodeNotAin',
                    params: {
                        seriesCode: value
                    }
                }).then(data => {
                    if (data.rows.length > 0) {
                        Tools.alert("系列代码已存在,请重新输入！", "danger");
                        this.formData.seriesCode = null;
                        return false;
                    }
                });
            }
        }
    };
</script>
