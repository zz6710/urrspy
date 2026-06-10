<template>
  <div class="container-box">
    <el-page-header @back="goBack">
      <template slot="content">
        {{formName}}
        <k-btn class="md-success" data-functype="POPUP" data-target="sqlConfigPopup" :data-handler="resetFormData" style="margin-left: 10px;">添加一行</k-btn>
        <k-btn class="btn-custom-primary" @click="save" style="margin-left: 10px;">保存</k-btn>
      </template>
    </el-page-header>
    <el-tabs class="tab-box" type="border-card" v-model="activeName" @tab-click="handleClick">
      <el-tab-pane v-for="(item,index) in tabPanes" :key="item.name" :name="item.name">
        <span slot="label"><i :class="item.icon"></i> {{item.title}}</span>
        <k-grid :data-data="stepDatas[item.name]" :key="stepKey" :data-display="false">
          <template slot="expand" slot-scope="props">
            <el-row style="text-align: center;border-bottom: 1px solid #eee;font-weight: 700;">
              <el-col :span="4">校验类型</el-col>
              <el-col :span="4">校验字段</el-col>
              <el-col :span="4">校验SQL</el-col>
              <el-col :span="4">校验条件</el-col>
              <el-col :span="4">校验目标值</el-col>
              <el-col :span="4">校验提示</el-col>
            </el-row>
            <el-row v-if="!props.row.checkData || props.row.checkData.length == 0" style="text-align: center;">
              <el-col>暂无数据</el-col>
            </el-row>
            <el-row v-else v-for="(check,index) in props.row.checkData" :key="index" style="text-align: center;border-bottom: 1px solid #eee;">
              <el-col :span="4" class="col-line">{{checkTypeMap[check.checkType]}}</el-col>
              <el-col :span="4" class="col-line">{{check.checkField}}</el-col>
              <el-col :span="4" class="col-line">
                <el-tooltip effect="dark" :content="check.checkSql" placement="top">
                  <span>{{check.checkSql}}</span>
                </el-tooltip>
                </el-col>
              <el-col :span="4" class="col-line">{{checkSignMap[check.checkSign]}}</el-col>
              <el-col :span="4" class="col-line">{{check.checkTarget}}</el-col>
              <el-col :span="4" class="col-line">
                <el-tooltip effect="dark" :content="check.checkMsg" placement="top"><span>{{check.checkMsg}}</span></el-tooltip>
              </el-col>
            </el-row>
          </template>

          <k-grid-column data-header="SQL名称" data-name="sqlName" />
          <k-grid-column data-header="SQL语句" data-name="sqlStatement" data-width="300" />
          <k-grid-column data-header="数据源" data-name="datasource" />
          <k-grid-column data-header="数据库类型" data-name="dbType" />
          <k-grid-column data-header="是否记录日志" data-name="logPrint" data-dict="1yes0no" />
          <template slot="operate" slot-scope="scope">
            <k-btn class="md-info md-simple" data-functype="POPUP" data-target="sqlConfigPopup" :data-handler="beforeEdit">修改</k-btn>
            <k-btn class="md-danger md-simple" @click="delRow1(scope.row.row, stepData)">删除</k-btn>
            <k-btn class="md-info md-simple" data-functype="POPUP" data-target="sqlCheckPopup" :data-handler="beforeCheckEdit">校验配置</k-btn>
          </template>
        </k-grid>
      </el-tab-pane>
    </el-tabs>

    <k-popup ref="sqlConfigPopup" data-title="sql配置">
      <k-form ref="sqlConfigForm">
        <k-form-item label="SQL名称">
          <k-field-text v-model="addFormData.sqlName" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="数据库类型">
          <k-field-select v-model="addFormData.dbType" :data-allowblank="false" :data-data="dbTypeDict"></k-field-select>
        </k-form-item>
        <k-form-item label="SQL语句" :data-col="2">
          <k-field-text input-type="textarea" v-model="addFormData.sqlStatement" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="数据源">
          <k-field-text v-model="addFormData.datasource" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="日志打印">
          <k-field-radio v-model="addFormData.logPrint" data-dict="1yes0no" :data-allowblank="false"></k-field-radio>
        </k-form-item>
        <div slot="footer" style="text-align: center;">
          <k-btn class="md-success" @click="addStepData()"><md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </div>
      </k-form>

    </k-popup>

    <k-popup ref="sqlCheckPopup" data-title="校验规则配置">
      <el-table :data="checkData" border>
        <el-table-column label="校验类型" width="160" align="center">
          <template slot-scope="scope">
            <div style="display: flex;align-items: center;">
              <span class="red-star">*</span>
              <k-field-select v-for="i in 1" :key="i" ref="cellCheck" v-model="scope.row.checkType" :data-data="checkTypeDict" :data-allowblank="false" />
            </div>
            <span v-show="scope.row.errs[0]" class="err-msg" v-text="scope.row.errs[0]"></span>
          </template>
        </el-table-column>

        <el-table-column label="校验字段" width="160" align="center">
          <template slot-scope="scope">
            <div style="display: flex;align-items: center;">
              <span v-if="scope.row.checkType=='1'" class="red-star">*</span>
              <k-field-text v-for="i in 1" :key="i" ref="cellCheck" v-model="scope.row.checkField" :data-allowblank="scope.row.checkType!='1'" />
            </div>
            <span v-show="scope.row.errs[1]" class="err-msg">{{scope.row.errs[1]}}</span>
          </template>
        </el-table-column>

        <el-table-column label="校验sql" width="160" align="center">
          <template slot-scope="scope">
            <div style="display: flex;align-items: center;">
              <span v-if="scope.row.checkType=='2'" class="red-star">*</span>
              <k-field-text v-for="i in 1" :key="i" ref="cellCheck" v-model="scope.row.checkSql" :data-allowblank="scope.row.checkType!='2'" />
            </div>
            <span v-show="scope.row.errs[2]" class="err-msg">{{scope.row.errs[2]}}</span>
          </template>
        </el-table-column>

        <el-table-column label="校验条件" width="160" align="center">
          <template slot-scope="scope">
            <div style="display: flex;align-items: center;">
              <span class="red-star">*</span>
              <k-field-select v-for="i in 1" :key="i" ref="cellCheck" v-model="scope.row.checkSign" :data-data="checkSignDict" :data-allowblank="false" />
            </div>
            <span v-show="scope.row.errs[3]" class="err-msg">{{scope.row.errs[3]}}</span>
          </template>
        </el-table-column>

        <el-table-column label="校验目标值" width="160" align="center">
          <template slot-scope="scope">
            <k-field-text v-for="i in 1" :key="i" ref="cellCheck" v-model="scope.row.checkTarget" />
            <span v-show="scope.row.errs[4]" class="err-msg">{{scope.row.errs[4]}}</span>
          </template>
        </el-table-column>

        <el-table-column label="校验提示" width="160" align="center">
          <template slot-scope="scope">
            <k-field-text v-for="i in 1" :key="i" ref="cellCheck" v-model="scope.row.checkMsg" :data-allowblank="false" />
            <span v-show="scope.row.errs[5]" class="err-msg">{{scope.row.errs[5]}}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="160">
          <template slot-scope="scope">
            <k-btn class="md-success md-just-icon md-simple" data-descript="下方添加一行" @click="addRow(scope, checkData)"><i class="el-icon-circle-plus-outline"/></k-btn>
            <k-btn v-if="checkData.length>1" class="md-danger md-just-icon md-simple" data-descript="删除此行" @click="delRow(scope, checkData)"><i class="el-icon-circle-close"/></k-btn>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer">
        <k-btn class="md-success" @click="saveToStepData"><i class="icon-confirm"></i>确定</k-btn>
        <k-btn data-functype="CLOSE"><i class="icon-cancel"></i>取消</k-btn>
      </div>
    </k-popup>
  </div>
</template>

<script>
  import {
    assign,
    cloneDeep
  } from "lodash";
  import Tools from "@/utils/tools";

  export default {
    name: "opSqlConfig",
    data() {
      return {
        formName: '',
        formId: '',
        activeName: '1',
        editFlag: false,
        editIndex: 0,
        //隐藏保存配置
      tabPanes: [
        {
          name: '0',
          title: '保存',
          icon: 'el-icon-document-add'
        },
        {
          name: '1',
          title: '提交',
          icon: 'el-icon-finished'
        }, {
          name: '2',
          title: '流程终止',
          icon: 'el-icon-s-check'
        }
        // , {
        //   name: '3',
        //   title: '回退上一步',
        //   icon: 'el-icon-refresh-left'
        // }
        ],
        addFormData: {},
        // 表格数据
        stepDatas: {
          '0': {rows:[]},
          '1': {rows:[]},
          '2': {rows:[]},
          '3': {rows:[]},
        },
        checkData: [{}],
        checkSignDict: [{label:'小于',value:'lt'},{label:'小于等于',value:'lte'},{label:'等于',value:'eq'},{label:'不等于',value:'neq'},{label:'大于',value:'gt'},{label:'大于等于',value:'gte'}],
        checkSignMap: {
          'lt': '小于',
          'lte': '小于等于',
          'eq': '等于',
          'neq': '不等于',
          'gt': '大于',
          'gte': '大于等于',
        },
        dbTypeDict: [{label:'mysql',value:'mysql'},{label:'db2',value:'db2'},{label:'oracle',value:'oracle'}],
        checkTypeDict: [{label:'字段校验',value:'1'}, {label:'sql校验',value:'2'}],
        checkTypeMap: {
          '1': '字段校验',
          '2': 'sql校验'
        },
        // 表格列配置
        tableColumns: [{
          label: 'SQL名称',
          width: '160',
          field: 'sqlName',
          dataRender: (value) => { return value||'--'; },
          inputConfig: {
            type: 'text'
          }
        }, {
          label: 'SQL语句',
          width: '300',
          field: 'sqlStatement',
          dataRender: (value) => { return value||'--'; },
          inputConfig: {
            type: 'textarea'
          }
        }, {
          label: '数据源',
          width: '160',
          field: 'datasource',
          dataRender: (value) => { return value||'--'; },
          inputConfig: {
            type: 'text'
          }
        }, {
          label: '数据库类型',
          width: '160',
          field: 'dbType',
          dataRender: (value) => { return value||'--'; },
          inputConfig: {
            type: 'select',
            dictData: [{label:'mysql',value:'mysql'},{label:'db2',value:'db2'},{label:'oracle',value:'oracle'}]
          }
        }, {
          label: '日志打印',
          width: '160',
          field: 'logPrint',
          dataRender: (value) => { return this.formatYesNo(value); },
          inputConfig: {
            type: 'radio',
            dict: '1yes0no'
          }
        }]
      };
    },
    created() {
      this.formId = this.$route.query.formId;
      this.formName = this.$route.query.formName;
      this.loadStepDatas();
    },
    computed: {
    },
    methods: {
      loadStepDatas() {
        this.httpUtil.comnQuery({
          action: 'OpSqlConfig.find',
          params: {
            formId: this.formId
          }
        }).then(res => {
          res.rows.forEach(item => {
            this.stepDatas[item.actionType].rows.push(item);
          })
        })
      },
      save() {
        let arr = [];
        for (const actionType in this.stepDatas) {
          let stepData = this.stepDatas[actionType];
          let orderNo = 0;
          stepData.rows.forEach(item => {
            item.formId = this.formId;
            item.actionType = actionType;
            item.orderNo = orderNo;
            item.sqlId = (this.formId+'-') + (++orderNo+'').padStart(4, 0);
            let checkOrder = 0;
            item.checkData && item.checkData.forEach(item1 => {
              item1.sqlId = item.sqlId;
              item1.orderNo = checkOrder;
              item1.checkId = (item.sqlId+'-') + (++checkOrder+'').padStart(4, 0);
            })
          });
          arr.push(...stepData.rows);
        }
        this.httpUtil.comnUpdate({
          action: 'OpSqlConfig.save',
          params: {
            formId: this.formId,
            list: JSON.stringify(arr)
          }
        }).then(res => {
          if (res.success) {
            this.goBack();
          }
        })
      },
      beforeEdit(row) {
        this.editFlag = true;
        this.editIndex = row.row_index;
        this.addFormData = assign(row);
      },
      beforeCheckEdit(row) {
        this.editIndex = row.row_index;
        if (!row.checkData || row.checkData.length==0) {
          row.checkData = [];
          this.checkData = [{errs:{}}];
        } else {
          this.checkData = cloneDeep(row.checkData);
          this.checkData.forEach(item => item.errs = {})
        }
      },
      saveToStepData() {
        // 校验每个单元格内的输入
        let flag = false;
        for (const i in this.$refs.cellCheck) {
          let rowIndex = parseInt(i/6);
          let colIndex = i%6;
          let kfield = this.$refs.cellCheck[i];
          let checkResult = kfield.validate();
          if (!this.checkData[rowIndex].errs) {
            this.$set(this.checkData[rowIndex], 'errs', {});
          }
          if (checkResult!==true) {
            kfield.focus();
            this.$set(this.checkData[rowIndex].errs, colIndex, checkResult);
            flag = true;
          } else {
            this.$set(this.checkData[rowIndex].errs, colIndex, '');
          }
        }
        if (flag) {
          return;
        }
        this.stepDatas[this.activeName].rows[this.editIndex].checkData = this.checkData;
        this.$refs.sqlCheckPopup.close();
      },
      resetFormData() {
        this.editFlag = false;
        this.addFormData = {
          sqlName: '',
          sqlStatement: '',
          datasource: '0',
          dbType: '',
          logPrint: '1'
        };
      },
      addStepData() {
        if (!this.$refs.sqlConfigForm.validate()) {
          return;
        }
        if (this.editFlag) {
          Object.assign(this.stepDatas[this.activeName].rows[this.editIndex], this.addFormData);
        } else {
          this.stepDatas[this.activeName].rows.push(this.addFormData);
        }
        this.$refs.sqlConfigPopup.close();
      },
      addRow(scope, stepData) {
        stepData.splice(scope.$index+1, 0, {errs:{}});
      },
      delRow(scope, stepData) {
        Tools.confirm(() => {
          stepData.splice(scope.$index, 1);
        }, "确定要删除吗？")
      },
      delRow1(row, stepData) {
        Tools.confirm(() => {
          stepData.rows.splice(row.row_index, 1);
          Tools.alert("删除成功");
        }, "确定要删除第"+(row.row_index+1)+"行吗？")
      },
      setCheck(scope, stepData) {
        this.checkData = stepData.checkData || [{}];
      },
      formatYesNo(value) {
        return value=='0'?'否':value=='1'?'是':(value||'--');
      },
      handleClick(tab, event) {},
      // 单元格单击事件，编辑标识为true
      cellclick(row, column, cell, event) {
        this.$set(row, 'isEdit_'+column.label, true);
        // 可编辑后，还得获取焦点
        this.$nextTick(() => {
          // 显示文本框并获取焦点
          if (this.$refs[column.label]) {
            this.$refs[column.label] instanceof Array ? this.$refs[column.label][0].focus() : this.$refs[column.label].focus();
          }
        })
      },
      // 失去焦点，编辑标识为false，隐藏文本框
      blurCell({ row, column }) {
        this.$set(row, 'isEdit_'+column.label, false);
      },
      // 输入内容改变事件
      changeData({ row }) {
      },
      goBack() {
        Tools.closeCurrentWindow(this);
        let backPath = '/main/operation/form';
        this.$router.push({
          path: backPath
        });
      }
    }
  };
</script>

<style scoped>
  .container-box {
    display: block;
    padding: 30px;
    min-height: 300px;
    height: 100%;
    background-color: white;
  }
  .tab-box {
    width: 100%;
    min-height: 300px;
    max-height: calc(100vh - 100px);
    margin-top: 30px;
  }

  /deep/ .el-page-header__left {
    align-items: center;
  }
  /deep/ .el-page-header__content {
    display: flex;
    align-items: center;
  }
  /deep/ .el-dialog__footer {
    text-align: center;
  }
  /deep/ .el-table__expanded-cell {
    padding-left: 50px;
  }
  /deep/ .el-col {
    border: 1px solid transparent;
  }
  .err-msg {
    color: red;
  }
  .red-star {
    color: red;
    padding-right: 10px;
  }
  .col-line {
    overflow: hidden;
    white-space: nowrap;
  }
</style>
