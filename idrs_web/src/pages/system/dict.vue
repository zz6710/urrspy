<template>
  <div class="py-page">
    <k-form-search data-target="dictGrid" data-model-name="Dict" data-label-width="80px" @click="() => this.$refs.dictItemGrid.list = []">
      
    </k-form-search>
    <div>
      <!--    添加Dict弹出框    -->
      <k-popup ref="addDictPopup" data-title="新增">
        <k-form ref="addDictForm" :data-col="1">
          <k-form-item label="字典标识">
            <k-field-text v-model="formData.dict" :data-allowblank="false"  :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字" @data-on-change="checkDict('add')"/>
          </k-form-item>
          <k-form-item label="字典名称">
            <k-field-text v-model="formData.dictname" :data-allowblank="false" :data-max-length="66" />
          </k-form-item>
<!--          <k-form-item label="组别">
            <k-field-select v-model="formData.groupdict" data-dict="moduleid" :data-allowblank="false" :data-max-length="32" />
          </k-form-item>-->
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Dict.add" data-from="addDictForm" :data-model="formData"
                   data-target="dictGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>
    </div>

    <div class="py-page-container" >
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
             data-target="addDictPopup" v-if="global.isShowAuthorityButton('Dict.add')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <div class="table-box-row">

      
     
      <div class="table-box-column">

        <k-grid :data-checkbox="true" ref="dictGrid" dataPaginationLayout="total,prev, pager, next" data-action="Dict.find1"
                data-operate-width="80px" @data-row-select="selectDictRow">
          <k-grid-column data-header="字典标识" data-name="dict"></k-grid-column>
          <k-grid-column data-header="字典名称" data-name="dictname"></k-grid-column>
<!--          <k-grid-column data-header="组别" data-name="groupdict" data-dict="moduleid" data-width="70"></k-grid-column>-->
          <template slot="operate">
            <k-btn class="md-info md-just-icon md-simple" data-descript="新增数据字典子项" data-functype="POPUP" data-size="mini"
                   data-target="addDictItemPopup" v-if="global.isShowAuthorityButton('DictItem.add')">
              <md-icon>add</md-icon>
            </k-btn>
           <!-- <k-btn class="md-info md-just-icon md-simple" data-descript="修改数据字典" data-functype="POPUP" data-size="mini"
                   data-target="editDictPopup">
              <md-icon>edit</md-icon>
            </k-btn>-->
            <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="Dict.delete" data-size="mini"
                   data-type="danger" :data-after-success="()=>{this.initAll()}" data-target="roleGrid" :data-confirm="true"
                   :data-model="selectRowData" data-descript="删除字典及其子项">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
      </div>
      <!--    添加DictItem弹出框    -->
      <k-popup ref="addDictItemPopup" data-title="新增数据字典子项">
        <k-form ref="addDictItemForm" :data-col="1">
          <k-form-item label="字典标识">
            <k-field-text v-model="formData.dict" data-disabled data-clearable="false"  :data-max-length="32"/>
          </k-form-item>
          <k-form-item label="数据键">
            <k-field-text v-model="formData.itemkey" :data-max-length="100" :data-allowblank="false" data-regx="^[a-zA-Z\d]+$" data-regx-text="数据键只能包含数字或英文" @data-on-change="checkDictItemValue" />
          </k-form-item>
          <k-form-item label="数据值">
            <k-field-text v-model="formData.itemval" :data-max-length="66" :data-allowblank="false" />
          </k-form-item>
          <k-form-item label="渲染样式">
            <k-field-text v-model="formData.itemrender" :data-max-length="16" />
          </k-form-item>
          <k-form-item label="排序">
            <k-field-text data-allowblank v-model="formData.itemorder" :data-max-length="16" />
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DictItem.add" data-from="addDictItemForm"
                   :data-model="formData" data-target="dictItemGrid" >
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>
      <!--    修改Dict弹出框   -->
      <k-popup ref="editDictPopup" data-title="修改">
        <k-form ref="editDictForm" :data-col="1">
          <k-form-item label="字典标识">
            <k-field-text v-model="formData.dict" data-clearable="false" data-disabled  :data-max-length="32" data-regx="^[A-Za-z_0-9]*$" data-regx-text="请输入字母、下划线与数字" @data-on-change="checkDict('edit')"/>
          </k-form-item>
          <k-form-item label="字典名称">
            <k-field-text v-model="formData.dictname" :data-max-length="66" :data-allowblank="false" />
          </k-form-item>
<!--          <k-form-item label="组别">
            <k-field-text v-model="formData.groupdict" :data-max-length="32" :data-allowblank="false" />
          </k-form-item>-->
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="Dict.edit" data-from="editDictForm"
                   :data-model="formData" data-target="dictGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>
      <!-- 字典子项 -->
      <div class="table-box-column">
        <k-grid ref="dictItemGrid" data-pagination-layout="total, sizes, prev, pager, next" data-action="DictItem.findWithoutParam"
                @data-row-select="selectDictItemRow">
          <k-grid-column data-header="字典标识" data-name="dict"></k-grid-column>
          <k-grid-column data-header="数据键" data-name="itemkey"></k-grid-column>
          <k-grid-column data-header="数据值" data-name="itemval"></k-grid-column>
          <k-grid-column data-header="渲染样式" data-name="itemrender"></k-grid-column>
          <k-grid-column data-header="排序" data-name="itemorder"></k-grid-column>
          <template slot="operate">
            <k-btn class="md-info md-just-icon md-simple" data-descript="修改子项" data-functype="POPUP" data-size="mini"
                   data-target="editDictItemPopup" v-if="global.isShowAuthorityButton('DictItem.edit')">
              <md-icon>edit</md-icon>
            </k-btn>
            <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="DictItem.delete"
                   data-size="mini" data-type="danger" data-target="dictItemGrid"
                   :data-confirm="true" :data-model="selectRowData" data-descript="删除子项">
              <md-icon>close</md-icon>
            </k-btn>
          </template>
        </k-grid>
      </div>
      </div>
      <!--    修改DictItem弹出框   -->
      <k-popup ref="editDictItemPopup" data-title="修改">
        <k-form ref="editDictItemForm" :data-col="1">
          <k-form-item label="字典标识">
            <k-field-text v-model="formData.dict" data-disabled data-clearable="false"  :data-max-length="32" />
          </k-form-item>
          <k-form-item label="数据键">
            <k-field-text v-model="formData.itemkey" data-disabled data-clearable="false"  :data-max-length="100" data-regx="^[a-zA-Z\d]+$" data-regx-text="数据键只能包含数字或英文" @data-on-change="checkDictItemValue" />
          </k-form-item>
          <k-form-item label="数据值">
            <k-field-text v-model="formData.itemval" :data-max-length="66" :data-allowblank="false" />
          </k-form-item>
          <k-form-item label="渲染样式">
            <k-field-text data-allowblank v-model="formData.itemrender" :data-max-length="16"  />
          </k-form-item>
          <k-form-item label="排序">
            <k-field-text data-allowblank v-model="formData.itemorder" :data-max-length="16"  />
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DictItem.edit" data-from="editDictItemForm"
                   :data-model="formData" data-target="dictItemGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>
    </div>
  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  import Tools from '@/utils/tools.js';
  import {
    assign
  } from "lodash";

  export default {
    name:"dict",
    data() {
      return {
        formData: {},
        selectRowData: {}
      };
    },

    methods: {

      selectDictItemRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.initFormData(row)
      },
      selectDictRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        let dictItemGrid = _this.$refs.dictItemGrid
        if (typeof dictItemGrid.gridOptions.dataParams == "undefined") {
          dictItemGrid.gridOptions.dataParams = {
            "dict": row.dict
          }
        } else {
          dictItemGrid.gridOptions.dataParams.dict = row.dict
        }
        dictItemGrid.load(row)
        _this.initFormData(row)
      },
      initFormData(row) {
        const _this = this
        _this.formData = assign({}, row)
      },
      initAll() {
        this.$refs.dictGrid.load()
        this.$refs.dictItemGrid.load()
      },
      // updSuccess(pop) {
      //   this.initAll()
      //   pop.close()
      // }
      checkDict(option){
        if(!this.formData.dict){
          return ;
        }
        this.httpUtil.comnQuery({
          action:"Dict.findDict",
          params:{dict:this.formData.dict}
        }).then(data => {
          if( !data.rows[0] || (option==='edit' && data.rows[0].dict==this.formData.dict)){
            return ;
          }
          Tools.alert("字典标识已存在！","danger");
        }).catch({

        })
      },
      checkDictItemValue(option){
        if(!this.formData.itemkey){
          return ;
        }
        this.httpUtil.comnQuery({
          action:"DictItem.findDictItemOnly",
          params:{itemkey:this.formData.itemkey,dict:this.formData.dict}
        }).then(data => {
          if(!data.rows[0]){
            return;
          }
          Tools.alert("数据键已存在！","danger");
        }).catch({

        })
      }
    }
  };
</script>
