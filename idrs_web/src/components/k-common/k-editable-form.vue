<template>
  <div>
    <md-card class="k-card">
      <md-card-header class="md-card-header-text md-card-header-blue">
        <div class="card-icon" :style="iconStyle">
          <md-icon>assignment</md-icon>
        </div>
      </md-card-header>
      <md-card-content>
        <div>
          <el-form ref="form" :model="formObj">
<!--            border-->
            <el-table  stripe size="small" :data="formObj.tableData">
              <el-table-column prop="date" label="日期" align="center">
                <template slot-scope="scope">
                  <div class="required">
                    <div
                      class="xing"
                      v-if="rules[scope.column.property][0].required"
                    >
                      *
                    </div>
                    <el-form-item
                      :prop="`tableData.${scope.$index}.${scope.column.property}`"
                      :rules="rules[scope.column.property]"
                    >
                      <el-date-picker
                        v-model="scope.row.date"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        type="date"
                        placeholder="选择日期"
                        size="mini"
                        :ref="`date${scope.$index}`"
                        @focus="dateFocus(`date${scope.$index}`)"
                      >
                      </el-date-picker>
                    </el-form-item>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="jobType" label="工作类型" align="center">
                <template slot-scope="scope">
                  <div class="flex">
                    <div class="required">
                      <div
                        class="xing"
                        v-if="rules[scope.column.property][0].required"
                      >
                        *
                      </div>
                      <el-form-item
                        :prop="`tableData.${scope.$index}.${scope.column.property}`"
                        :rules="rules[scope.column.property]"
                      >
                        <el-select size="mini" v-model="scope.row.jobType">
                          <el-option
                            v-for="item in jobTypeOptions"
                            :key="item.truteeCode"
                            :value="item.truteeCode"
                            :label="item.truteeName"
                          ></el-option>
                        </el-select>
<!--                        <k-field-select v-model="jobTypeOptions" data-dict="1yes0no"></k-field-select>-->
                      </el-form-item>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="jobContent" label="工作内容" align="center">
                <template slot-scope="scope">
                  <div class="flex">
                    <div class="required">
                      <div
                        class="xing"
                        v-if="rules[scope.column.property][0].required"
                      >
                        *
                      </div>
                      <el-form-item
                        :prop="`tableData.${scope.$index}.${scope.column.property}`"
                        :rules="rules[scope.column.property]"
                      >
                        <el-input
                          size="mini"
                          v-model="scope.row.jobContent"
                          @input="
                        inputInput(
                          scope.row[scope.column.property],
                          `tableData.${scope.$index}.${scope.column.property}`
                        )
                      "
                          @blur="
                        inputInput(
                          scope.row[scope.column.property],
                          `tableData.${scope.$index}.${scope.column.property}`
                        )
                      "
                        ></el-input>
                      </el-form-item>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="workHours" label="工时(小时)" align="center">
                <template slot-scope="scope">
                  <div class="flex">
                    <div class="required">
                      <div
                        class="xing"
                        v-if="rules[scope.column.property][0].required"
                      >
                        *
                      </div>
                      <el-form-item
                        :prop="`tableData.${scope.$index}.${scope.column.property}`"
                        :rules="rules[scope.column.property]"
                      >
                        <el-tooltip
                          placement="top"
                          content="工时必须大于等于0小于等于8.0"
                          manual
                          v-model="scope.row.workHoursTip"
                        >
                          <el-input
                            size="mini"
                            v-model="scope.row.workHours"
                            @input="
                          inputInput(
                            scope.row[scope.column.property],
                            `tableData.${scope.$index}.${scope.column.property}`
                          )
                        "
                            @blur="
                          inputInput(
                            scope.row[scope.column.property],
                            `tableData.${scope.$index}.${scope.column.property}`
                          )
                        "
                          ></el-input>
                        </el-tooltip>
                      </el-form-item>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="travelStatus" label="出差状态" align="center">
                <template slot-scope="scope">
                  <div class="flex">
                    <div class="required">
                      <div
                        class="xing"
                        v-if="rules[scope.column.property][0].required"
                      >
                        *
                      </div>
                      <el-form-item
                        :prop="`tableData.${scope.$index}.${scope.column.property}`"
                        :rules="rules[scope.column.property]"
                      >
                        <el-select size="mini" v-model="scope.row.travelStatus">
                          <el-option
                            v-for="item in travelStatusOptions"
                            :key="item.value"
                            :value="item.value"
                            :label="item.label"
                          ></el-option>
                        </el-select>
                      </el-form-item>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="approvalStatus" label="审批状态" align="center">
              </el-table-column>
              <el-table-column prop="operate" label="操作" align="center">
                <template slot-scope="scope">
                  <div class="flex">
<!--                    <i-->
<!--                      class="add-btn el-icon-circle-plus"-->
<!--                      @click="addRow(scope.row, scope.$index)"-->
<!--                    ></i>-->
                    <k-btn class="md-simple" data-descript="添加" data-functype="POPUP" data-size="mini"
                           @click="addRow(scope.row, scope.$index)" >
                      <font color="#00bcd4" style="margin-left: -5px; ">添加</font>
                    </k-btn>
                    <k-btn class="md-simple" data-descript="删除" data-functype="POPUP" data-size="mini"
                           @click="removeRow(scope.row, scope.$index)" v-if="scope.$index > 0">
                      <font color="#00bcd4" style="margin-left: -5px; ">删除</font>
                    </k-btn>
<!--                    <i-->
<!--                      class="remove-btn el-icon-remove"-->
<!--                      v-if="scope.$index > 0"-->
<!--                      @click="removeRow(scope.row, scope.$index)"-->
<!--                    ></i>-->

                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
          <div class="operate-btns">
            <el-button size="small" type="primary" @click="save">保存</el-button>
            <el-button size="small" type="default">取消</el-button>
          </div>
          <hr/>
          <div>
<!--            <k-form>-->
<!--              <k-grid :data-data="{'rows':formObj.tableData}">-->
<!--                <k-grid-column data-align="center"-->
<!--                               data-header="日期"-->
<!--                               data-name="date" />-->
<!--              </k-grid>-->
<!--              <k-form-footer>-->
<!--                <div class="operate-btns">-->
<!--                  <el-button size="small" type="primary" @click="save">保存</el-button>-->
<!--                  <el-button size="small" type="default">取消</el-button>-->
<!--                </div>-->
<!--              </k-form-footer>-->
<!--            </k-form>-->
          </div>
        </div>
      </md-card-content>
    </md-card>
    <el-dialog
      :title="currentType == 'subProjectName' ? '子项目名称' : '宿舍名称'"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <div v-if="currentType == 'subProjectName'">
        <div style="margin-bottom:20px">
          <el-radio v-model="radioValue" label="项目1" border>项目1</el-radio>
        </div>
        <div style="margin-bottom:20px">
          <el-radio v-model="radioValue" label="项目2" border>项目2</el-radio>
        </div>
      </div>
      <div v-if="currentType == 'roomName'">
        <div style="margin-bottom:20px">
          <el-radio v-model="roomValue" label="宿舍1" border>宿舍1</el-radio>
        </div>
        <div style="margin-bottom:20px">
          <el-radio v-model="roomValue" label="宿舍2" border>宿舍2</el-radio>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'KEditableForm',
  data() {
    const workHoursValidate = (rule, value, callback) => {
      if (!value) {
        callback(new Error());
        this.formObj.tableData[rule.field.split(".")[1]].workHoursTip = false;
      } else if (value <= 8) {
        callback();
        this.formObj.tableData[rule.field.split(".")[1]].workHoursTip = false;
      } else {
        callback(new Error());
        this.formObj.tableData[rule.field.split(".")[1]].workHoursTip = true;
      }
    };
    return {
      formObj: {
        tableData: [
          {
            date: "2022-10-25",
            subProjectName: "",
            jobType: "",
            jobContent: "",
            workHours: "",
            workHoursTip: false,
            workProvince: "",
            workCity: "",
            travelStatus: "",
            roomName: "",
            approvalStatus: "待审核"
          }
        ]
      },
      rules: {
        date: [{ required: true, message: " ", trigger: "change" }],
        subProjectName: [{ required: true, message: " ", trigger: "change" }],
        jobType: [{ required: true, message: " ", trigger: "change" }],
        jobContent: [{ required: true, message: " ", trigger: "change" }],
        workHours: [
          {
            required: true,
            message: " ",
            trigger: "change",
            validator: workHoursValidate
          }
        ],
        workProvince: [{ required: true, message: " ", trigger: "change" }],
        workCity: [{ required: true, message: " ", trigger: "change" }],
        travelStatus: [{ required: true, message: " ", trigger: "change" }],
        roomName: [{ required: true, message: " ", trigger: "change" }]
      },
      jobTypeOptions: [
        // {
        //   value: "培训",
        //   label: "培训"
        // },
        // {
        //   value: "出差",
        //   label: "出差"
        // }
      ],
      travelStatusOptions: [
        {
          value: "未出差",
          label: "未出差"
        },
        {
          value: "出差",
          label: "出差"
        }
      ],
      dialogVisible: false,
      radioValue: "",
      roomValue: "",
      currentRow: 0,
      currentType: "",
      truteeCode:''

    };
  },
  created(){
    this.getDept();
  },
  mounted() {},
  methods: {
    iconStyle() {
      let iconStyle = {};
      iconStyle.background = this.$store.state.system.cardBackground
      return iconStyle;
    },
    addRow(row) {
      this.formObj.tableData.push(JSON.parse(JSON.stringify(row)));
    },
    removeRow(row, index) {
      console.log(row, index);
      this.formObj.tableData.splice(index, 1);
    },
    save() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message({
            type: "success",
            message: "提交成功"
          });
        }
      });
    },
    inputInput(value, field) {
      if (!value) {
        setTimeout(() => {
          this.$refs.form.clearValidate([field]);
        });
      }
    },
    dialogSelect(index, type) {
      this.currentRow = index;
      this.currentType = type;
      this.dialogVisible = true;
    },
    dialogConfirm() {
      if (this.currentType == "subProjectName") {
        this.formObj.tableData[
          this.currentRow
          ].subProjectName = this.radioValue;
      } else if (this.currentType == "roomName") {
        this.formObj.tableData[this.currentRow].roomName = this.roomValue;
      }
      this.dialogVisible = false;
    },
    dateFocus(refVal) {
      setTimeout(() => {
        const currenDatePicker = this.$refs[refVal];
        const datePickers = document.querySelectorAll(".el-date-picker");
        const currenDatePickerProps = currenDatePicker.$el.getBoundingClientRect();
        datePickers[datePickers.length - 1].style.top =
          currenDatePickerProps.top + currenDatePickerProps.height + "px";
      });
    },
    //
    getDept(){
      this.httpUtil.comnQuery({
        action: 'T82006.findTaCustodianBanks2',
        params: {
        }
      }).then(data => {
        if (data.rows.length > 0) {
          this.jobTypeOptions = data.rows;
          console.log('this.jobTypeOptions',this.jobTypeOptions)
        }
      });
    },
  }
};
</script>
<style lang="scss" scoped>
@import '../k-element/k-grid/k-grid.scss';
.oa-page {
  width: 100%;
}
.flex {
  display: flex;
  white-space: nowrap;
}
.select-projectName-button {
  margin-left: 5px;
}
.add-btn {
  font-size: 20px;
  margin-right: 5px;
  cursor: pointer;
  color: #67c23a;
}
.remove-btn {
  font-size: 20px;
  cursor: pointer;
  color: #f56c6c;
}
.provivnce-select {
  margin-right: 5px;
}
.required {
  position: relative;
  .xing {
    color: red;
    position: absolute;
    z-index: 1;
    top: 50%;
    left: 5px;
    font-size: 14px;
    font-weight: bold;
    transform: translateY(-50%);
  }
}
.operate-btns {
  margin-top: 50px;
  text-align: center;
}
.el-form-item {
  margin: 0;
  ::v-deep .el-form-item__content {
    line-height: 0 !important;
  }
}
.readonly.el-input {
  /deep/ input {
    background-color: #f5f7fa;
    color: #c0c4cc;
    cursor: not-allowed;
  }
}
.el-date-editor {
  &.el-input {
    width: 100%;
  }
  /deep/ .el-input__prefix {
    left: 8px;
  }
}


.el-table .el-table__body tr:hover td {
  background-color: #eeffae;
}
</style>
