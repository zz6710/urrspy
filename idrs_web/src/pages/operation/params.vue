<template>
  <div class="form-body">
    <div class="form-header-box">
      <div class="form-header">
        <div class="form-name">{{formName}}</div>
        <k-btn v-if="formType=='0'" class="btn-custom-plain" data-functype="POPUP" data-target="addParamDailog" @click.native="beforeAddParam">
          添加参数
        </k-btn>
        <k-btn v-if="formType=='0'" class="md-success" @click.native="save" style="margin-left: 20px;">
          保存
        </k-btn>
        <k-btn @click.native="back2Page" style="margin-left: 20px;">
          返回
        </k-btn>
      </div>
    </div>
    <div v-if="formType=='0'" class="empty-box">
      <div v-show="params.length==0">
        <!-- 暂无参数 -->
        <el-empty description="暂无参数"></el-empty>
      </div>
    </div>
    <!-- 参数表单，用k-form展示 -->
    <k-form v-if="formType=='0'" class="form-box" data-total-width="1000px">
      <Draggable ref="draggableObj" v-bind="draggableOption" @remove="onRemove($event)" @start="drag = true" @end="drag = false" @update="updateDrag($event)" >
        <transition-group style="display: flex;flex-wrap: wrap;">
          <template v-for="(param, paramIndex) in params">
            <k-form-item :label="param.paramName" :key="param.paramCode" style="position: relative;margin-right: 100px;">
              <div class="dragClass" slot="label">{{param.paramName}}</div>
              <k-field-text v-model="param.defaultValue" v-if="param.funcType=='text' || param.funcType=='int' || param.funcType=='number'" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
              <k-field-select v-model="param.defaultValue" v-else-if="param.funcType=='select' && param.dataWay=='1'" :data-dict="param.dict" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" dataAppendToBody />
              <k-field-select v-model="param.defaultValue" v-else-if="param.funcType=='select' && param.dataWay=='2'" :data-action="param.dict" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" dataAppendToBody />
              <k-field-select v-model="param.defaultValue" v-else-if="param.funcType=='multiple'" :data-multiple="true" :data-dict="param.dict" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" dataAppendToBody />
              <k-field-tree v-model="param.defaultValue" v-else-if="param.funcType=='tree'" :data-dict="param.dict" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
              <k-field-radio v-model="param.defaultValue" v-else-if="param.funcType=='radio'" :data-dict="param.dict" :data-allowblank="param.blankFlag!='1'" />
              <k-field-checkbox v-model="param.defaultValue" v-else-if="param.funcType=='checkbox'" :data-action="param.dict" :data-allowblank="param.blankFlag!='1'" />
              <k-field-date v-model="param.defaultValue" v-else-if="param.funcType=='date'" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
              <k-field-time v-model="param.defaultValue" v-else-if="param.funcType=='time'" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
              <k-field-text v-model="param.defaultValue" v-else-if="param.funcType=='textarea'" :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" input-type="textarea"/>
              <k-field-text v-model="param.defaultValue" v-else :data-placeholder="param.placeholder" :data-allowblank="param.blankFlag!='1'" />
              <div style="position: absolute;top:0;right:-100px;">
                <k-btn
                  v-if="param.funcType!='component'"
                  class="md-info md-just-icon md-simple"
                  data-descript="设置参数关系"
                  @click.native="setRelation(param, paramIndex)"
                  data-size="mini">
                  <md-icon v-if="param.relations && param.relations.length > 0" md-src="/static/svg/prod/link.svg" class="pd-icon-20"/>
                  <md-icon v-else md-src="/static/svg/prod/unlink.svg" class="pd-icon-20"/>
                </k-btn>
                <k-btn
                  class="md-info md-just-icon md-simple"
                  data-descript="编辑参数"
                  data-functype="POPUP" data-target="addParamDailog" @click.native="beforeEditParam(paramIndex)"
                  data-size="mini">
                  <i class="el-icon-edit"></i>
                </k-btn>
                <k-btn
                  class="md-danger md-just-icon md-simple"
                  data-descript="删除参数"
                  @click.native="deleteParam(paramIndex)"
                  data-size="mini">
                  <i class="el-icon-remove-outline"></i>
                </k-btn>
              </div>
            </k-form-item>
          </template>
        </transition-group>
      </Draggable>
    </k-form>
    <!-- 组件类型表单 -->
    <template v-if="formType=='1'">
      <component v-if="component" :is="component" class="component-box"></component>
    </template>

    <k-popup ref="addParamDailog" :data-title="editFlag?'修改参数':'添加参数'">
      <k-form ref="addParamForm">
        <k-form-item label="参数代码">
          <k-field-text v-model="addFormData.paramCode" :data-allowblank="false" :data-disabled="editFlag" :data-max-length="32"
                        data-regx="^[0-9a-zA-Z_]{1,}$" data-regx-text="该项由字母、数字、下划线组成"/>
        </k-form-item>
        <k-form-item label="参数名称">
          <k-field-text v-model="addFormData.paramName" :data-allowblank="false" :data-max-length="64"/>
        </k-form-item>
        <k-form-item label="参数输入类型">
          <k-field-select v-model="addFormData.funcType" :data-allowblank="false" :data-data="functypeDict" @data-on-change="changeFuncType"/>
        </k-form-item>
        <k-form-item label="空白提示">
          <k-field-text v-model="addFormData.placeholder" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="参数字段长度" v-if="typeWithoutLength.indexOf(addFormData.funcType) < 0">
          <k-field-text v-model="addFormData.fieldLength" :data-allowblank="false" data-min-value="1"
                        data-validate-type="int" :data-max-length="4"/>
        </k-form-item>
        <k-form-item label="参数字段精度" v-if="addFormData.funcType == 'number'">
          <k-field-text
            v-model="addFormData.fieldPrecision"
            :data-allowblank="false"
            data-min-value="1"
            data-validate-type="int"
            :data-max-length="4"
          />
        </k-form-item>

        <k-form-item label="最小值" v-if="rangeType.includes(addFormData.funcType)">
          <k-field-text v-model="addFormData.minValue"
                        :data-validate-type="addFormData.funcType" :data-max-length="16"/>
        </k-form-item>

        <k-form-item label="最大值" v-if="rangeType.includes(addFormData.funcType)">
          <k-field-text v-model="addFormData.maxValue"
                        :data-validate-type="addFormData.funcType" :data-max-length="16"/>
        </k-form-item>

        <k-form-item label="参数选项来源" v-show="dictInType.includes(addFormData.funcType)">
          <k-field-radio v-model="addFormData.dataWay" :data-data="[{label:'数据字典',value:'1'},{label:'接口查询',value:'2'}]" :data-allowblank="!dictInType.includes(addFormData.funcType)"
                          @data-on-change="changeWay"/>
        </k-form-item>

        <k-form-item label="参数数据字典" v-if="dictInType.includes(addFormData.funcType) && addFormData.dataWay == '1'">
          <k-field-select
            ref="dictSelect"
            v-model="addFormData.dict"
            :data-allowblank="false"
            data-action="Dict.find1"
            data-display-field="dictname,dict"
            data-value-field="dict"
            :data-paging="true"
            data-paging-params-name="dictname"
          />
        </k-form-item>

        <k-form-item label="参数请求方法" v-if="dictInType.includes(addFormData.funcType) && addFormData.dataWay == '2'">
          <!-- <k-field-select
            ref="actionSelect"
            v-model="addFormData.dict"
            :data-allowblank="false"
            :data-dict="actionMap[systemNo]||'fina_query_action'"
            data-value-field="itemkey"
            data-display-field="itemval,itemkey"/> -->

          <k-field-text
            ref="actionSelect"
            v-model="addFormData.dict"
            :data-allowblank="false"
            :data-dict="actionMap[systemNo]||'fina_query_action'"
            data-value-field="itemkey"
            data-display-field="itemval,itemkey"/>
        </k-form-item>

        <k-form-item label="是否可编辑" v-if="addFormData.funcType != 'component'">
          <k-field-radio v-model="addFormData.editFlag" data-dict="1yes0no" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="是否必填" v-if="addFormData.funcType != 'component'">
          <k-field-radio v-model="addFormData.blankFlag" data-dict="1yes0no" :data-allowblank="false"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            :data-handler="editFlag?editParam:addParam"
          >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!-- 设置参数关系-->
    <el-dialog :visible.sync="dialogVisible" width="70%" center>
      <div style="height:420px; overflow: auto">
        <div>
          <el-row>
            <span style="color:#409eff;font-weight:bold">{{this.formData.paramName+' ['+this.formData.paramCode+']'}}</span>为
            <el-select v-model="chooseData" value-key="itemkey" multiple collapse-tabs>
              <el-option value="空值">空值</el-option>
              <el-option v-for="item in paraDictList" :key="item.itemkey" :label="item.itemval" :value="item"></el-option>
            </el-select>&emsp;时
            <el-button @click="addRelation" round type="success">添加联动场景</el-button>
          </el-row>
          <div class="panel-left" style="margin-top:10px">
            <el-tabs class="panel-left-tabs" v-model="currentEditTab" tab-position="left" type="card" stretch closable @tab-remove="removetab">
              <el-tab-pane
                  v-for="(item, ind) in tabs"
                  :key="item.content"
                  :name="item.name"
              >
                <el-tooltip class="item-tabs" effect="light" placement="right" slot="label">
                  <div slot="content">
                  <span>{{item.content}}</span>
                  </div>
                  <span>{{item.title | ellipsis}}</span>
                </el-tooltip>
                <el-form :model="tabs[ind]" style="padding-left:10px;margin-bottom: 30px; border-left: 1px solid #A9A9A9;">
                  <el-row :gutter="16" class="para-header">
                    <el-col class="para-req" :span="4">关联字段<span class="red-star">*</span></el-col>
                    <el-col class="para-req" :span="3">是否显示<span class="red-star">*</span></el-col>
                    <el-col class="para-req" :span="3">是否必填<span class="red-star">*</span></el-col>
                    <el-col :span="5">数据字典</el-col>
                    <el-col :span="4">默认值</el-col>
                    <el-col class="para-req" :span="3">是否可编辑<span class="red-star">*</span></el-col>
                    <el-col :span="1"><el-button @click="addRow(ind)" style="border:0px; color:#409EFF;font-size:16px" icon="el-icon-plus" circle></el-button></el-col>
                  </el-row>
                  <el-row :gutter="16" v-for="(row, index) in tabs[ind].rowlist" :key="item.content+index">
                    <el-col :span="4">
                      <el-form-item>
                        <el-select v-model="row.linkParamCode" filterable placeholder="请选择" clearable @change="changeparamName(row, ind)">
                          <el-option v-for="item in paramList" :key="item.paramCode" :label="item.paramName" :value="item.paramCode">
                            <span style="float: left">{{ item.paramName }}</span>
                            <span style="float: right; color: #8492a6; font-size: 13px">{{item.paramCode}}</span>
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="3">
                      <el-select v-model="row.showFlag" placeholder="请选择" @change="changeShowFlag(row.showFlag, ind, index)">
                        <el-option v-for="item in optionYesOrNot" :key="item.value" :label="item.label" :value="item.value"></el-option>
                      </el-select>
                    </el-col>
                    <el-col :span="3">
                      <el-select v-model="row.blankFlag" placeholder="请选择">
                        <el-option v-for="item in optionYesOrNot" :key="item.value" :label="item.label" :value="item.value"></el-option>
                      </el-select>
                    </el-col>
                    <el-col :span="5">
                      <k-field-select data-action="Dict.find1" v-model="row.dict"
                        data-display-field="dictname,dict" data-value-field="dict" :data-paging="true"
                        data-paging-params-name="dictname" :data-clearable="true" @data-on-change="changeDict(row)"/>
                    </el-col>
                    <el-col :span="4">
                      <el-select v-model="row.defaultVal" placeholder="请选择默认值" @change="$forceUpdate()"  clearable>
                        <el-option v-for="item in row.itemList" :key="item.itemkey" :label="item.itemval" :value="item.itemkey">
                          <span style="float: left">{{ item.itemval }}</span>
                          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.itemkey }}</span>
                        </el-option>
                      </el-select>
                    </el-col>
                    <el-col :span="3">
                      <el-select v-model="row.editFlag" placeholder="请选择">
                        <el-option v-for="item in optionYesOrNot" :key="item.value" :label="item.label" :value="item.value"></el-option>
                      </el-select>
                    </el-col>
                    <el-col :span="1">
                      <el-button @click="deleteRow(ind, index)" style="border:0px; color:red;font-size:16px" icon="el-icon-close" circle></el-button>
                    </el-col>
                  </el-row>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </div>
      </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveParaRelation()">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  import Draggable from "vuedraggable";

  export default {
    name: "opFormParam",
    components: {
      Draggable,
      Grid(resolve) {
        require(["@/pages/demo/k-field-bswitch"], resolve);
      }
    },
    data() {
      return {
        formName: '',
        formId: '',
        formType: '',
        component: null,
        // 联动关系弹框
        dialogVisible: false,
        // 参数编辑标识。共用一个表单弹框，所以用来区分
        editFlag: false,
        // 表单参数
        params: [],
        currentEditParam: '',
        addFormData: {
          paramCode: '',
          paramName: '',
          funcType: '',
          placeholder: '',
          fieldLength: '',
          fieldPrecision: '',
          dataWay: '',
          dict: '',
          editFlag: '',
          blankFlag: '',
          defaultValue: '',
        },
        typeWithoutLength: ['', undefined, null, 'select', 'radio', 'date', 'time', 'checkbox', 'multiple'],
        dictInType: ['select', 'radio', 'checkbox', 'multiple'],
        rangeType: ['number', 'int'],
        functypeDict: [{
          value: 'text',
          label: '文本框'
        }, {
          value: 'int',
          label: '整数'
        }, {
          value: 'number',
          label: '浮点数'
        }, {
          value: 'select',
          label: '下拉框'
        }, {
          value: 'radio',
          label: '单选框'
        }, {
          value: 'checkbox',
          label: '复选框'
        }, {
          value: 'multiple',
          label: '多选下拉框'
        }, {
          value: 'date',
          label: '日期'
        }, {
          value: 'time',
          label: '时间'
        }, {
          value: 'textarea',
          label: '文本域'
        }],
        // 依赖关系
        formData: {},
        paraDictList: [],
        chooseData: [],
        tabs: [],
        currentEditTab: '0',
        optionYesOrNot: [{
          value: '1',
          label: '是'
        }, {
          value: '0',
          label: '否'
        }],
        dictCache: {},
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
      this.formName = this.$route.query.formName;
      this.formId = this.$route.query.formId;
      this.formType = this.$route.query.formType;
      if (this.formType == '0') {
        this.loadFormParams();
      } else if (this.formType == '1' && this.$route.query.compPath) {
        // TODO 解决组件加载问题
        console.log(this.$route.query.compPath)
        let path = this.$route.query.compPath;
        // this.component = (resolve => require([this.$route.query.compPath], resolve));
        this.component = this.$route.query.compPath;
      }
    },
    computed: {
      paramList() {
        return this.params.filter(item => item.paramCode!=this.formData.paramCode);
      }
    },
    filters: {
      ellipsis(value){
        if (!value) return ''
        if (value.length > 10) {
            return value.slice(0,10) + '...'
        }
        return value
      }
    },
    methods: {
      loadFormParams() {
        this.httpUtil.comnQuery({
          action: 'OpFormParam.findOpFormParam',
          params: {
            formId: this.formId
          }
        }).then(res => {
          this.params = res.rows;
        });
      },
      save() {
        if (this.params.length == 0) {
          Tools.alert("请添加参数", "danger");
          return;
        }
        let orderNo = 0;
        // 给每个参数上序
        this.params.forEach(param => {
          param.orderNo = orderNo++;
          param.formId = this.formId;
        });
        this.httpUtil.comnUpdate({
          action: "OpFormParam.save",
          params: {
            formId: this.formId,
            params: JSON.stringify(this.params)
          },
          successAlert: true,
        }).then(data => {
          if (data.success) {
            this.back2Page();
          }
        });
      },
      beforeAddParam() {
        this.editFlag = false;
        Object.keys(this.addFormData).forEach(key => {
          this.addFormData[key] = '';
        });
      },
      beforeEditParam(paramIndex) {
        console.log("edit..")
        this.editFlag = true;
        Object.assign(this.addFormData, this.params[paramIndex]);
        // 记录当前编辑项的下标，点击确定时，通过下标找到对应参数
        this.currentEditParam = paramIndex;
      },
      addParam() {
        if (!this.$refs.addParamForm.validate()) {
          return;
        }
        for (const param of this.params) {
          if (param.paramCode == this.addFormData.paramCode) {
            this.$message({message:"该参数代码已存在！", type:"error"});
            return;
          }
        }
        this.params.push(Object.assign({}, this.addFormData));
        this.$refs.addParamDailog.close();
      },
      editParam() {
        if (!this.$refs.addParamForm.validate()) {
          return;
        }
        Object.assign(this.params[this.currentEditParam], this.addFormData);
        this.$refs.addParamDailog.close();
      },
      deleteParam(paramIndex) {
        Tools.confirm(() => {
          this.params.splice(paramIndex, 1);
        }, "确定删除参数["+this.params[paramIndex].paramName+"]?");
      },
      changeWay(newWay) {
        if (this.oldWay != newWay) {
          this.addFormData.dict = '';
          this.$nextTick(() => {
            if (this.$refs['actionSelect']) {
              this.$refs['actionSelect'].clears()
              this.$refs['actionSelect'].load();
            } else if (this.$refs['dictSelect']) {
              this.$refs['dictSelect'].clears()
              this.$refs['dictSelect'].load();
            }
          });
          this.oldWay = newWay;
        }
      },
      changeFuncType() {
        this.$forceUpdate();
      },
      // 将数据进行选择情况分组
      tabsGroupBy(arr) {
        let hashMap = {};
        var map = {};
        this.paraDictList.forEach(item => {
          map[item.itemkey] = item.itemkey+'-'+item.itemval;
        });
        this.tabs = []
        arr.forEach(item => {
          if (!hashMap[item.paramValue]) {
            let content = '';
            item.paramValue.split(",").forEach(title => {
              content += title?(map[title]+";"):"空值";
            });
            hashMap[item.paramValue] = { name: this.tabs.length+'', title: item.paramValue, content: content, rowlist: [] };
            this.tabs.push(hashMap[item.paramValue]);
          }
          hashMap[item.paramValue].rowlist.push(item);
        })

        if (this.tabs.length > 0) {
          this.currentEditTab = '0';
          for (const tab of this.tabs) {
            for (const row of tab.rowlist) {
              if (row.dict) {
                this.getDictItem(row.dict).then(res => {
                  row.itemList = res;
                });
              }
            }
          }
        }
      },
      // 设置参数之间关系
      setRelation(param, paramIndex){
        if (!param.relations) {
          param.relations = [];
        }
        this.tabs = param.relations;
        this.paraDictList = [];
        if (param.dict) {
          // 查询数据字典
          this.getDictItem(param.dict).then((res) => {
            this.paraDictList.push(...res);
            this.tabsGroupBy(param.relations);
            this.$forceUpdate();
          });
        } else {
          this.tabsGroupBy(param.relations);
        }
        this.formData = param;
        // 清空选值
        this.chooseData = [];
        this.dialogVisible = true;
        // 记录当前编辑项下标
        this.currentEditParam = paramIndex;
      },
      // 添加联动场景
      addRelation() {
        if (this.chooseData.length>0) {
          let itemkeys = this.chooseData.map(item => item.itemkey||item);
          for (const tab of this.tabs) {
            let titles = tab.title.split(",");
            // 数组汇总去重，判断是否有重复元素
            if (new Set([...titles,...itemkeys]).size != itemkeys.length+titles.length) {
              Tools.alert("该选值场景已存在！", "danger");
              this.chooseData = [];
              return;
            }
          }
          var title = '';
          var content = '';
          this.chooseData.forEach(item => {
            if (item == '空值') {
              title = title + item +",";
              content = content + item +",";
            } else {
              title = title + item.itemkey+","
              content = content + item.itemkey+'-'+item.itemval+","
            }
          });
          title = title.substr(0, title.length-1).split(",").sort().toString();
          content = content.substr(0, content.length-1).split(",").sort().join(";");
          let newTabName = this.tabs.length + '';
          this.tabs.push({
            name: newTabName,
            title: '' + title,
            content: content,
            rowlist: [{
              formId: this.formId,
              paramCode: this.formData.paramCode,
              paramValue: '' + title,
              linkParamCode: '',
              showFlag: '',
              blankFlag: '',
              dict: '',
              efaultVal: '',
              editFlag: ''
            }]
          });
          // 当前编辑项
          this.currentEditTab = newTabName;
          this.chooseData = []
        }
      },
      // 添加一行
      addRow(index){
        this.tabs[index].rowlist.push({formId: this.formId, paramCode: this.formData.paramCode, paramValue: this.tabs[index].title, linkParamCode:'',showFlag:'',blankFlag:'',dict:'',defaultVal:'',editFlag:''});
      },
      // 删除一行
      deleteRow(ind,index) {
        this.tabs[ind].rowlist.splice(index, 1);
      },
      // 删除取值情况
      removetab(targetName){
        if (this.currentEditTab === targetName) {
          for (let i = 0; i<this.tabs.length; i++) {
            let tab = this.tabs[i];
            if (tab.name === targetName) {
              let nextTab = this.tabs[i + 1] || this.tabs[i - 1];
              if (nextTab) {
                // 更改当前选中tab
                this.currentEditTab = nextTab.name;
              }
              // 删除该tab
              this.tabs.splice(i, 1);
              break;
            }
          }
        }
      },
      // 监听关联参数
      changeparamName(row, ind){
        row.dict = '';
        row.defaultVal = '';
        // 判断当前取值情况下，是否已经存在该关联参数
        if (row.linkParamCode && this.tabs[ind].rowlist.filter(item => item.linkParamCode == row.linkParamCode).length>1) {
          Tools.alert("参数已存在！", "danger");
          row.linkParamCode = '';
        }
        // this.changeDict(row);
      },
      changeShowFlag(value, ind, index) {
        if (value=='0') {
          this.tabs[ind].rowlist[index].editFlag = '0'
        } else {
          this.tabs[ind].rowlist[index].editFlag = ''
        }
      },
      //监听数据字典的改变
      changeDict(row){
        this.getDictItem(row.dict).then(res => {
          row.defaultVal = '';
          row.itemList = res;
          this.$forceUpdate();
        });
      },
      //保存设置参数关系
      saveParaRelation() {
        let relations = [];
        this.tabs.forEach(item => {
          relations.push(...item.rowlist);
        });
        if (relations.length > 0) {
          for (let i = 0; i < relations.length; i++) {
            const relation = relations[i];
            if (!relation.linkParamCode) {
              this.$message({message:"关联字段不能为空", type:"error"});
              return;
            } else if (!relation.showFlag) {
              this.$message({message:"参数是否显示不能为空", type:"error"});
              return;
            } else if (!relation.blankFlag) {
              this.$message({message:"参数是否必填不能为空", type:"error"});
              return;
            } else if (!relation.editFlag) {
              this.$message({message:"参数是否可编辑不能为空", type:"error"});
              return;
            }
          }
        }
        this.params[this.currentEditParam].relations = relations;
        this.dialogVisible = false;
      },
      getDictItem(dict) {
        if (this.dictCache[dict]) {
          return new Promise((resolve, reject)=>{resolve(this.dictCache[dict])})
        }
        return new Promise((resolve, reject) => {
            this.httpUtil.comnQuery({
            action: "DictItem.find",
            params: {
              dict: dict,
            },
          }).then((res) => {
            this.dictCache[dict] = res.rows;
            resolve(res.rows);
          });
        });
      },
      // 参数拖拽
      updateDrag(evt) {
        var newIndex = evt.newIndex;
        var oldIndex = evt.oldIndex;
        if (newIndex == oldIndex) {
          return;
        } else {
          let tempArr = this.params;
          let tempItem = tempArr[oldIndex];
          if (newIndex > oldIndex) {
            this.params = tempArr.slice(0, oldIndex).concat(tempArr.slice(oldIndex+1, newIndex+1), tempItem, tempArr.slice(newIndex+1, tempArr.length));
          } else {
            this.params = tempArr.slice(0, newIndex).concat(tempItem, tempArr.slice(newIndex, oldIndex), tempArr.slice(oldIndex + 1, tempArr.length));
          }
          this.$forceUpdate();
        }
      },
      onRemove(evt) {
        var newIndex = evt.newIndex;
        var oldIndex = evt.oldIndex;
        this.params.splice(newIndex, 0, this.params[oldIndex])
        this.params.splice(oldIndex, 1);
      },
      //返回
      back2Page() {
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
    .form-body {
      position: relative;
      display: block;
      background-color: white;
      padding: 0 50px;
      min-height: 300px;
      height: 100%;
      overflow-y: hidden;
    }
    .form-header-box {
      position: absolute;
      height: 75px;
      border-bottom: 1px solid #ccc;
      background-color: white;
      width: 90%;
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
    .empty-box {
      padding-top: 100px;
    }
    .empty-desc {
      font-size: 50px;
      color: #eee;
      font-weight: 700;
      height: 200px;
      line-height: 200px;
      width: 800px;
      text-align: center;
      border: 2px dashed #eee;
      border-radius: 5px;
      user-select: none;
    }
    .form-box {
      overflow-y: auto;
    }
    .component-box {
      padding: 30px;
      position: absolute;
      top: 100px;
      overflow-y: auto;
      width: 93%;
      max-height: calc(100vh - 275px);
    }
    .dragClass {
      display: inline;
    }
  </style>
