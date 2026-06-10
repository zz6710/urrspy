<template>
  <div class="form-body">
    <div v-for="(item, index) in forms" :key="index" class="form-box">
      <div class="el-dialog__headerbtn" @click="deleteForm(index)">
        <i class="el-dialog__close el-icon el-icon-close" />
      </div>
      <k-form data-ui="material" ref="form">
        <k-form-item>
          <k-field-select :ref="'select'+index" v-model="item.formId" :data-allowblank="false" :data-data="formDict" data-display-field="formName" data-value-field="formId" @data-on-focus="focusForm(item.formId)" @data-on-change="changeForm(item.formId, index)" data-placeholder="请选择表单" />
        </k-form-item>
      </k-form>
      <template v-if="item.formType=='0'">
        <!-- 参数表单，用k-form展示 -->
        <k-form style="margin-top: 20px" data-total-width="910px">
          <Draggable ref="draggableObj" v-bind="draggableOption" @remove="onRemove($event)" @start="drag = true" @end="drag = false" @update="updateDrag($event)" >
            <transition-group style="display: flex;flex-wrap: wrap;">
              <template v-for="(param, paramIndex) in item.params">
                <k-form-item :label="param.paramName" :key="param.paramCode" style="position: relative;margin-right: 100px;">
                  <div class="dragClass" slot="label">{{param.paramName}}</div>
                  <k-field-text v-model="param.defaultValue" v-if="param.funcType=='text' || param.funcType=='int' || param.funcType=='number'" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-select v-model="param.defaultValue" v-else-if="param.funcType=='select' && param.dataWay=='1'" :data-dict="param.dict" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-select v-model="param.defaultValue" v-else-if="param.funcType=='select' && param.dataWay=='2'" :data-action="param.dict" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-select v-model="param.defaultValue" v-else-if="param.funcType=='multiple'" :data-multiple="true" :data-dict="param.dict" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-tree v-model="param.defaultValue" v-else-if="param.funcType=='tree'" :data-dict="param.dict" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-radio v-model="param.defaultValue" v-else-if="param.funcType=='radio'" :data-dict="param.dict" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-checkbox v-model="param.defaultValue" v-else-if="param.funcType=='checkbox'" :data-action="param.dict" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-date v-model="param.defaultValue" v-else-if="param.funcType=='date'" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-time v-model="param.defaultValue" v-else-if="param.funcType=='time'" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
                  <k-field-text v-model="param.defaultValue" v-else :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
                </k-form-item>
              </template>
            </transition-group>
          </Draggable>
        </k-form>
      </template>

      <!-- 组件类型表单 -->
      <template v-if="item.formType=='1'">
        <component v-if="item.compPath" :is="item.compPath"></component>
      </template>
    </div>

    <k-form data-ui="material">
      <k-form-item>
        <k-field-select v-model="selectFormId" :data-data="formDict" data-display-field="formName" data-value-field="formId" @data-on-change="addForm" data-placeholder="请选择添加表单" dataAppendToBody />
      </k-form-item>
    </k-form>

    <!-- 右边悬浮操作区 -->
    <div class="share-container" v-show="true">
      <div @click="save" class="pd-button">
        <md-icon md-src="/static/images/create/save4.svg" class="pd-icon-20"/>
        <div class="pd-text">保存</div>
      </div>
      <div @click="back2Page" class="pd-button">
        <md-icon md-src="/static/images/create/back2.svg" class="pd-icon-20"/>
        <div class="pd-text">返回</div>
      </div>
    </div>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  import Draggable from "vuedraggable";
  import {assign, cloneDeep} from "lodash"

  export default {
    name: "opBusiForm",
    components: {
      Draggable,
      Grid(resolve) {
        require(["@/pages/operation/components/flow_test.vue"], resolve);
      },
    },
    data() {
      return {
        busiId: '',
        // 表单下拉框数据字典
        formDict: [],
        formDictMap: {},
        // 已选择的表单id
        selectFormId: '',
        beforeChangeFormId: '',
        // 已添加的表单
        forms: [],
        // 拖拽
        drag: false,
        draggableOption: {
          forceFallback: false,
          group: "params",
          animation: 1000,
          handle: ".dragClass"
        },
      };
    },
    mounted() {
    },
    created() {
      this.busiId = this.$route.query.busiId;
      this.loadFormDict();
      this.loadForms();
    },
    computed: {
    },
    methods: {
      focusForm(formId) {
        console.log(formId)
        this.beforeChangeFormId = formId;
      },
      loadForms() {
        this.httpUtil.comnQuery({
          action: "OpBusiForm.find",
          params: {
            busiId: this.busiId
          }
        }).then(res => {
          let formIdList = res.rows.map(item => item.formId);
          this.httpUtil.comnQuery({
            action: 'OpFormInfo.findOpFormParam',
            params: {
              formIdList: JSON.stringify(formIdList)
            }
          }).then(res1 => {
            this.forms.push(...res1.rows);
            this.$nextTick(()=>{
              for (const i in this.forms) {
                this.$refs['select'+i][0].refreshData(this.formDict);
              }
            });
          });
        });
      },
      loadFormDict() {
        this.httpUtil.comnQuery({
          action: "OpFormInfo.findAsDict"
        }).then(res => {
          this.formDict = res.rows;
          for (const form of this.formDict) {
            this.formDictMap[form.formId] = form;
          }
        });
      },
      // 添加表单
      addForm(formId, form) {
        for (const item of this.forms) {
          if (item.formId == formId) {
            Tools.alert("表单【"+form.formName+"】已存在！", "danger");
            this.selectFormId = '';
            return;
          }
        }
        let newForm = cloneDeep(form);
        this.forms.push(newForm);
        this.$nextTick(()=>{
          this.$refs['select'+(this.forms.length-1)][0].refreshData(this.formDict);
        });
        if (form.formType == '0') {
          if (this.formDictMap[formId].params) {
            this.$set(newForm, "params", cloneDeep(this.formDictMap[formId].params));
          } else {
            this.httpUtil.comnQuery({
              action: 'OpFormParam.findOpFormParam',
              params: {
                formId: formId
              }
            }).then(res => {
              this.formDictMap[formId].params = res.rows;
              this.$set(newForm, "params", cloneDeep(this.formDictMap[formId].params));
            });
          }
        }
        this.selectFormId = '';
      },
      changeForm(formId, index) {
        // 校验
        for (const i in this.forms) {
          let item = this.forms[i];
          if (index!=i && formId && item.formId == formId) {
            Tools.alert("该表单已存在！", "danger");
            // 还原
            this.$nextTick(() => {
              this.forms[index].formId = this.beforeChangeFormId||'';
              if (!this.beforeChangeFormId) {
                this.forms[index].params = [];
              }
            });
            return;
          }
        }
        // 选择完下拉框并没有失去焦点，所以再点击就不会触发focus事件（也就不会记录原值），因此选择完后要记录原值
        this.beforeChangeFormId = formId;
        // 重新赋值
        this.forms[index] = assign({}, this.formDictMap[formId]);
        this.$forceUpdate();
        // 查询参数
        if (this.forms[index].formType == '0') {
          if (this.formDictMap[formId].params) {
            this.$set(this.forms[index], "params", cloneDeep(this.formDictMap[formId].params));
            this.$forceUpdate();
          } else {
            this.httpUtil.comnQuery({
              action: 'OpFormParam.findOpFormParam',
              params: {
                formId: formId
              }
            }).then(res => {
              this.formDictMap[formId].params = res.rows;
              this.$set(this.forms[index], "params", cloneDeep(this.formDictMap[formId].params));
              this.$forceUpdate();
            });
          }
        }
      },
      save() {
        if (this.forms.length == 0) {
          Tools.alert("请添加表单", "danger");
          return;
        }
        for (const form of this.$refs.form) {
          if (!form.validate()) {
            Tools.alert("请选择表单！", "danger");
            return;
          }
        }

        // 给每个参数上序
        let orderNo = 0;
        let list = [];
        let formIdMap = {};
        for (const form of this.forms) {
          list.push({
            busiId: this.busiId,
            formId: form.formId,
            orderNo: orderNo++
          });
          if (!formIdMap[form.formId]) {
            formIdMap[form.formId] = true;
          } else {
            Tools.alert("表单重复！", "danger");
            return;
          }
        }
        this.httpUtil.comnUpdate({
          action: "OpBusiForm.save",
          params: {
            busiId: this.busiId,
            list: JSON.stringify(list)
          },
          successAlert: true,
        }).then(data => {
          this.back2Page();
        });
      },
      deleteForm(index) {
        Tools.confirm(() => {
          this.forms.splice(index, 1);
        }, "确定删除表单["+(this.forms[index].formName||"")+"]?");
      },
      // 参数拖拽
      updateDrag(evt) {
        var newIndex = evt.newIndex;
        var oldIndex = evt.oldIndex;
        if (newIndex == oldIndex) {
          return;
        } else {
          let tempArr = this.forms;
          let tempItem = tempArr[oldIndex];
          if (newIndex > oldIndex) {
            this.forms = tempArr.slice(0, oldIndex).concat(tempArr.slice(oldIndex+1, newIndex+1), tempItem, tempArr.slice(newIndex+1, tempArr.length));
          } else {
            this.forms = tempArr.slice(0, newIndex).concat(tempItem, tempArr.slice(newIndex, oldIndex), tempArr.slice(oldIndex + 1, tempArr.length));
          }
          this.$forceUpdate();
        }
      },
      onRemove(evt) {
        var newIndex = evt.newIndex;
        var oldIndex = evt.oldIndex;
        this.forms.splice(newIndex, 0, this.forms[oldIndex])
        this.forms.splice(oldIndex, 1);
      },
      //返回
      back2Page() {
        Tools.closeCurrentWindow(this);
        let backPath = '/main/operation/busi';
        this.$router.push({
          path: backPath
        });
      }
    }
  };
  </script>

  <style scoped>
    .form-body {
      display: block;
      background-color: white;
      padding: 20px 50px 0;
      min-height: 700px;
    }
    .form-header-box {
      position: absolute;
      height: 75px;
      border-bottom: 1px solid #ccc;
      background-color: white;
      width: 70%;
      vertical-align: bottom;
      display: flex;
      align-items: end;
      z-index: 1;
    }
    .form-header {
      display: flex;
      align-items: center;
      font-size: 30px;
      padding-bottom: 10px;
    }
    .form-name {
      margin-right: 20px;
    }
    .dragClass {
      display: inline;
    }
    .form-box {
      position: relative;
      border-bottom: 2px solid #ccc;
      margin-bottom: 40px;
      box-shadow: 0 8px 5px 0 rgba(0,0,0,.2), 0 10px 15px 0 rgba(0,0,0,.22);
      padding: 0 20px 20px 20px;
    }
    .share-container {
      position: fixed;
      display: flex;
      top: 160px;
      right: 32px;
      z-index: 300;
      width: 120px;
      height: 52px;
      display: flex;
      justify-content: center;
      align-items: center;
      background: #FFFFFF;
      box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.14);
      border-radius: 26px 0px 0px 26px;
    }
    .pd-button{
      display: block;
      width: 50px;
      max-width: 50px;
      min-width: 50px;
    }
    .pd-button:hover{
      background-color:#f3e9e9;
      border-radius: 3px;
      cursor: pointer;
    }
    .pd-icon-20 {
      max-width: 20px;
      min-width: 20px;
    }
    .pd-text {
      font-size: 10px;
      padding-left: 15px;
      margin-top: -7px;
    }
  </style>
