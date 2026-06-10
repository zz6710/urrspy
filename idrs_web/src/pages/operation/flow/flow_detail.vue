<template>
  <el-container class="container">
    <el-header>
      <el-page-header @back="goBack">
        <template slot="content">
          <k-btn class="btn-custom-plain" @click="save" style="margin-left: 10px;">保存</k-btn>
          <k-btn class="md-success" data-functype="POPUP" @click="clickSubmit" style="margin-left: 10px;">提交</k-btn>
          <k-btn class="btn-custom-primary" @click="end" style="margin-left: 10px;">流程终止</k-btn>
          <k-btn class="btn-custom-plain" data-functype="POPUP" data-target="transferPopup" style="margin-left: 10px;">转交</k-btn>
<!--          <k-btn class="btn-custom-plain" @click="back" style="margin-left: 10px;">回退上一步</k-btn>-->
        </template>
      </el-page-header>
    </el-header>

    <el-main>
      <template v-for="item in forms">
        <el-divider></el-divider>
        <div class="form-title">
          <div class="color-block"></div>
          <div>{{item.formName}}</div>
        </div>
        <template v-if="item.formType=='0'">
          <!-- 参数表单，用k-form展示 -->
          <k-form :ref="item.formId" :key="item.formId" :data-col="2" data-label-width="200px" >
<!--            <k-form-item label="产品代码">-->
<!--              <k-field-select v-model="prodCode" data-disabled="true" data-action="T8Dict.findTaProdInfos"-->
<!--                              data-display-field="prodCode,prodName" data-value-field="prodCode"/>-->
<!--            </k-form-item>-->
            <template v-for="(param, paramIndex) in item.params">
              <!--当依赖对象的值存在于依赖关系中，即达成依赖条件-->
              <template
                v-if="relations[item.formId] && relations[item.formId][param.paramCode] && relations[item.formId][param.paramCode].find(relation=>(formData[item.formId][relation.paramCode] == relation.paramValue || (formData[item.formId][relation.paramCode] == undefined && !relation.paramValue)))">
                <template v-for="relation in relations[item.formId][param.paramCode]">
                  <k-form-item :data-col="2" v-if="formData[item.formId][relation.paramCode] == relation.paramValue || (!formData[item.formId][relation.paramCode] && !relation.paramValue)" :label="param.paramName">
                    <component v-model="formData[item.formId][param.paramCode]" v-bind="inputOptions(param,relation)" @data-on-change="dataChange(item.formId,param.paramCode)" :is="funcTypeMap[param.funcType]"></component>
                  </k-form-item>
                </template>
              </template>
              <k-form-item v-else :label="param.paramName" :key="param.paramCode" :data-col="2">
                <component v-model="formData[item.formId][param.paramCode]" v-bind="inputOptions(param)" @data-on-change="dataChange(item.formId, param.paramCode)" :is="funcTypeMap[param.funcType]"></component>
              </k-form-item>
            </template>
          </k-form>
        </template>

        <!-- 组件类型表单 -->
        <template v-if="item.formType=='1'">
          <component :ref="item.formId" v-if="item.compPath" :is="item.compPath"></component>
        </template>
      </template>
    </el-main>

    <k-popup ref="addPopup" data-title="提交审批任务" data-width="60%" >
      <callWorkFlow ref="flowProcessConfigurationJump" :fromData="formList" @submitClose="popupClose" />
    </k-popup>
    <k-popup ref="transferPopup" data-title="转交流程">
      <k-form ref="transferForm" :data-col="2">
        <k-form-item label="转交人员">
          <k-field-select v-model="transferUserid" data-multiple="false" data-action="User.findUsersWithQY" :data-allowblank="false"
                          :data-params="{userstatus:'N'}" data-value-field="userid" data-display-field="username"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" @click="clickTransferFlow" >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>
    <!--流程详情弹框-->
    <k-popup ref="submitPopup" data-title="流程详情" data-width="60%">
      <div id="pdfDom">
        <flow-detail-approval/>
        <div>
          <el-main>
            <template v-for="item in forms">
              <el-divider></el-divider>
              <div class="form-title">
                <div class="color-block"></div>
                <div>{{item.formName}}</div>
              </div>
              <template v-if="item.formType=='0'">
                <!-- 参数表单，用k-form展示 -->
                <k-form :ref="item.formId" :key="item.formId" :data-col="2" data-label-width="200px" >
                  <template v-for="(param, paramIndex) in item.params">
                    <!--当依赖对象的值存在于依赖关系中，即达成依赖条件-->
                    <template v-if="relations[item.formId] && relations[item.formId][param.paramCode] && relations[item.formId][param.paramCode].find(relation=>(formData[item.formId][relation.paramCode] == relation.paramValue || (formData[item.formId][relation.paramCode] == undefined && !relation.paramValue)))">
                      <template v-for="relation in relations[item.formId][param.paramCode]">
                        <k-form-item :data-col="2" v-if="formData[item.formId][relation.paramCode] == relation.paramValue || (!formData[item.formId][relation.paramCode] && !relation.paramValue)" :label="param.paramName">
                          <component v-model="formData[item.formId][param.paramCode]" v-bind="inputOptions(param,relation)" @data-on-change="dataChange(item.formId,param.paramCode)" :is="funcTypeMap[param.funcType]"></component>
                        </k-form-item>
                      </template>
                    </template>
                    <k-form-item v-else :label="param.paramName" :key="param.paramCode" :data-col="2">
                      <div style="width:100%;border: 1px solid #DCDFE6;background-color: #f7f7f7;border-radius: 4px;line-height: 32px;font-size: 13px;display: inline-block;padding: 0 15px;"
                           v-if="param.paramCode==='settlement_date_desc'||param.paramCode==='assignment_date_desc'">
                        <span>{{formData[item.formId][param.paramCode]}}</span>
                      </div>
                      <component v-else v-model="formData[item.formId][param.paramCode]" v-bind="inputOptions(param)" @data-on-change="dataChange(item.formId, param.paramCode)" :is="funcTypeMap[param.funcType]"></component>
                    </k-form-item>
                  </template>
                </k-form>
              </template>
              <!-- 组件类型表单 -->
              <template v-if="item.formType=='1'">
                <component :ref="item.formId" v-if="item.compPath" :is="item.compPath"></component>
              </template>
            </template>
          </el-main>
        </div>
      </div>
      <div style="text-align: center">
        <k-btn class="btn-custom-primary" @click="submit">提交</k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </div>
    </k-popup>
  </el-container>
</template>

<script>
  import Tools from '@/utils/tools.js';
  import callWorkFlow from '@/pages/operation/flow/callWorkFlow'
  import {v4} from 'uuid'
  import FlowHistoryDetail from "@/pages/operation/flow/flow_history_detail";
  import FlowDetailApproval from "@/pages/operation/flow/flowDetailApproval";

  export default {
    name: "flowDetail",
    data() {
      return {
        transferUserid:'',//被转交人员id
        prevPagePath: '',
        processId: '',
        processVersion: '',
        processInstanceId: '',
        taskId: undefined,
        nodeName: '',
        busiId: '',
        nextNode: undefined,
        forms: [],
        formData: {},
        submitFormData: {},
        submitFlowData:{},
        // 输入类型映射组件
        funcTypeMap: {
          text: 'k-field-text',
          number: 'k-field-text',
          int: 'k-field-text',
          select: 'k-field-select',
          radio: 'k-field-radio',
          checkbox: 'k-field-checkbox',
          date: 'k-field-date',
          time: 'k-field-time',
          tree: 'k-field-tree',
          multiple: 'k-field-select',
          textarea: 'k-field-text'
        },
        // 表单参数依赖关系
        relations: {},
        beRely: {},
        formList: [],
        //第一次提交进入审批
        type:'',
        prodCode:'',
        dataList:[],
        endBtnStatus:false,//终止按钮控制
        backBtnStatus:false,//回退按钮控制
        tempFormData:{},
        initFormData:{},
        submitFiles: {},
      };
    },
    components: {
      FlowDetailApproval,
      FlowHistoryDetail,
      callWorkFlow,
      Grid(resolve) {
        require(["@/pages/operation/components/flow_test.vue"], resolve);
      },
    },
    computed: {
      inputOptions: () => {
        return (param, relation) => {
          let options = {};
          options.ref = param.formId + param.paramCode;
          // 是否可空
          options['data-allowblank'] = relation ? relation.blankFlag == '0' : param.blankFlag=='0';
          // 是否可编辑
          options['data-disabled'] = relation ? relation.editFlag=='0' : param.editFlag=='0';
          if (param.funcType=='textarea') {
            options['input-type'] = 'textarea';
          }
          // 长度
          if (param.fieldLength) {
            options['data-max-length'] = parseInt(param.fieldLength);
          }
          // 校验类型、精度
          if (param.funcType=='int') {
            options['data-validate-type'] = 'int';
          } else if (param.funcType=='number') {
            options['data-validate-type'] = 'number';
            if (param.fieldPrecision) {
              options['data-digits'] = parseFloat(param.fieldPrecision);
            }
          }
          // 数据字典
          if (param.dataWay=='2') {
              options['data-action'] = param.dict;
          } else {
            options['data-dict'] = relation ? relation.dict : param.dict;
          }
          // 最小最大值
          if (param.minValue || param.minValue==0) {
            options['data-min-value'] = param.minValue;
          }
          if (param.maxValue || param.maxValue==0) {
            options['data-max-value'] = param.maxValue;
          }
          // 空白提示
          options.dataPlaceholder = param.placeholder;
          if (param.funcType=='multiple') {
            options.dataFuncType = true;
          }
          return options;
        }
      }
    },
    beforeRouteEnter (to, from, next) {
      next(vm => {
        vm.prevPagePath = from.path;
      });
    },
    created() {
      let nodeInfo = this.$route.query.nodeInfo;
      let type = this.$route.query.type;
      let prodCode = this.$route.query.prodCode;
      console.log('详情获取节点信息',nodeInfo);
      console.log('获取清盘类型',type);
      console.log('获取清盘的代码',prodCode);
      this.initFormData = this.$route.query.row;
      console.log('获取行数据',this.initFormData);
      if (nodeInfo) {
        this.firstLoad(nodeInfo);
        if (type=='1') {
          console.log('发起清盘流程');
          this.endBtnStatus = true;
        }else {
          this.endBtnStatus = false;
        }
      } else {
        this.load();
      }
      // 查询功能配置的表单
      this.busiId && this.httpUtil.comnQuery({
        action: 'OpBusiForm.find',
        params: {
          busiId: this.busiId
        }
      }).then(res => {
        let formIdList = res.rows.map(row => {
          this.$set(this.formData, row.formId, {});
          return row.formId
        });
        this.taskId && this.loadFormData();
        this.loadRelations(formIdList);
        // 查询表单详情
        this.httpUtil.comnQuery({
          action: 'OpFormInfo.findOpFormParam',
          params: {
            formIdList: JSON.stringify(formIdList)
          }
        }).then(res1 => {
          this.forms.push(...res1.rows);
          if (this.initFormData){
            for (let i = 0; i <this.forms.length; i++) {
              console.log('this.forms',this.forms);
              this.$set(this.formData[this.forms[i].formId], 'prod_code', this.initFormData.prodCode);
              this.$set(this.formData[this.forms[i].formId], 'prodName', this.initFormData.prodName);
              this.$set(this.formData[this.forms[i].formId], 'endDate', this.initFormData.endDate);
            }
          }else {
            //this.processInstanceId
            //获取查询的数据
            this.httpUtil.comnQuery({
              action:'ProdLiquidation.findProdLiquidationByProcessId',
              params:{
                processId:this.processInstanceId
              }
            }).then(res=>{
              console.log('查询清盘流程数据',res.rows[0])
              for (let i = 0; i <this.forms.length; i++) {
                console.log('this.forms',this.forms);
                this.prodCode = res.rows[0].prodCode;
                this.$set(this.formData[this.forms[i].formId], 'prod_code', res.rows[0].prodCode);
                this.$set(this.formData[this.forms[i].formId], 'prodCode', res.rows[0].prodCode);
                this.$set(this.formData[this.forms[i].formId], 'prodName', res.rows[0].prodName);
                this.$set(this.formData[this.forms[i].formId], 'prod_real_close_date', res.rows[0].prodRealCloseDate);
                this.$set(this.formData[this.forms[i].formId], 'settlement_date_desc', res.rows[0].settlementDateDesc);
                this.$set(this.formData[this.forms[i].formId], 'assignment_date_desc', res.rows[0].assignmentDateDesc);
                this.$set(this.formData[this.forms[i].formId], 'account_day', res.rows[0].accountDay);
                this.$set(this.formData[this.forms[i].formId], 'total_assets', res.rows[0].totalAssets);
                this.$set(this.formData[this.forms[i].formId], 'is_account', res.rows[0].isAccount);
                this.$set(this.formData[this.forms[i].formId], 'liquidate', res.rows[0].liquidate);
                this.$set(this.formData[this.forms[i].formId], 'corpus', res.rows[0].corpus);
                this.$set(this.formData[this.forms[i].formId], 'total_amount', res.rows[0].totalAmount);
                this.$set(this.formData[this.forms[i].formId], 'income', res.rows[0].income);
              }
            })

          }
        })
      });

      this.httpUtil.comnQuery({
        action: 'Process.findNextNode',
        params: {
          name: this.nodeName,
          processId: this.processId,
          version: this.processVersion
        }
      }).then(res => {
        this.nextNode = res.returndata.nextNode;
      })
    },
    methods: {
      firstLoad(nodeInfo) {
        this.nodeName = nodeInfo.name;
        this.busiId = nodeInfo.busiId;
        this.processId = this.$route.query.processId;
        this.processVersion = this.$route.query.version;

        this.prodCode = this.$route.query.prodCode;
        this.type = this.$route.query.type;
      },
      load() {
        this.nodeName = this.$route.query.taskName;
        this.busiId = this.$route.query.busiId;
        this.processId = this.$route.query.processId;
        this.processInstanceId = this.$route.query.processInstanceId;
        this.taskId = this.$route.query.taskId;
        this.processVersion = this.$route.query.processVersion;

        this.prodCode = this.$route.query.prodCode;
        this.type = this.$route.query.type;
      },
      loadFormData() {
        this.httpUtil.comnQuery({
          action: 'SubmitParams.getSubmitParamsByTask',
          params: {
            taskId: this.taskId
          }
        }).then(res => {
          if (res.data) {
            Object.assign(this.formData, JSON.parse(res.data.submitParams));
          }
        });
      },
      // 保存
      save() {
        // 包装数据
        let formData = this.packFormData();
        // 调用保存
        this.httpUtil.comnUpdate({
          action: 'ProcessInstance.save',
          params: {
            processId: this.processId,
            processVersion: this.processVersion,
            processInstanceId: this.processInstanceId,
            submitParams: JSON.stringify(formData),
            nodeName: this.nodeName,
            taskId: this.taskId,
            prodCode:this.prodCode
          }
        })
      },
      clickSubmit(){
        if (!this.checkForm()) {
          return;
        }
        this.$refs.submitPopup.popup();
      },
      /**
       * 1.点击提交审批将流程明细生成PDF文件
       * 2.提交审批
       */
      submit() {
        // 校验
        if (!this.checkForm()) {
          return;
        }
        if (!this.processInstanceId) {
          this.processInstanceId = v4().replace(/[-]/g, '') + "-"
        }
        console.log('清盘流程实例id',this.processInstanceId)
        if (this.processInstanceId.length==30){
          console.log('非第一次生成流程实例')
          this.pdfDownload(this.processInstanceId);
        } else {
          let processInstanceId = this.processInstanceId.substring(0, this.processInstanceId.length - 1)
          console.log('第一次生成流程实例',processInstanceId);
          this.pdfDownload(processInstanceId);
        }
      },
      //提交审批
      submitApproval(){
        // 包装数据
        let formData = this.packFormData();
        console.log('包装的数据-------',formData)
        this.processId = this.$route.query.processId;
        console.log('获取操作流流程id',this.processId)
        if (this.nextNode && this.nextNode.workflow) {
          this.httpUtil.comnQuery({
            action: "ProcessInstance.existsTask",
            params: {
              processId: this.processId,
              processVersion: this.processVersion,
              currentNode: this.nodeName,
              nextNode: this.nextNode.name,
              processInstanceId: this.processInstanceId
            }
          }).then(resp => {
            // 不存在进行的任务，才发审批申请
            if (!resp.returndata.exists) {
              let submitFlowData = {};
              for (const form of this.forms) {
                submitFlowData = formData[form.formId]
              }

              console.log('提交审批的表单参数',submitFlowData);
              let arr = this.nextNode.workflow.split("-");
              let row = {
                opProcessId: this.processId,
                processVersion: this.processVersion,
                opProcessInstance: this.processInstanceId,
                currentNode: this.nextNode.name,
                prodCode: this.prodCode,
                type: this.type,
                submitFlowData:JSON.stringify(submitFlowData),
              }
              this.tempFormData = row;
              let action = arr[0].substring(0, arr[0].length-7) + "." + arr[1];
              this.submitVerify(row,action,{failAlert: false});
            } else {
              //执行提交
              this.doSubmit();
            }
          })
        } else {
          this.doSubmit();
        }
      },
      refreshPage(){
        console.log('执行了方法()refreshPage',this.tempFormData)
        this.doSubmit1(this.tempFormData);
        Tools.closeCurrentWindow(this);
        this.$router.push({
          path: '/main/desktop'
        });
      },
      doSubmit1(val) {
        // 提交数据，执行表单对应提交步骤的sql，流转该任务
        this.httpUtil.comnUpdate({
          action: 'ProcessInstance.submit',
          params: {
            processId: val.opProcessId,
            processVersion: this.processVersion,
            processInstanceId: this.processInstanceId,
            submitParams: JSON.stringify(this.submitFormData),
            nodeName: this.nodeName,
            taskId: this.taskId
          },
          successAlert:false,
        }).then(res => {
        })
      },
      doSubmit() {
        // 提交数据，执行表单对应提交步骤的sql，流转该任务
        console.log('获取操作流流程id',this.processId)
        this.httpUtil.comnUpdate({
          action: 'ProcessInstance.submit',
          params: {
            processId: this.processId,
            processVersion: this.processVersion,
            processInstanceId: this.processInstanceId,
            submitParams: JSON.stringify(this.submitFormData),
            nodeName: this.nodeName,
            taskId: this.taskId,
            prodCode:this.prodCode
          }
        }).then(res => {
          this.closePage();
        })
      },
      //===============流程终止START====================
      end() {
        if (this.endBtnStatus) {
          Tools.alert('第一次发起流程,不支持终止流程！','danger');
          return false;
        }
        // 包装数据
        let formData = this.packFormData();
        // 提交数据，执行表单对应提交步骤的sql，流转该任务
        Tools.confirm(()=>{
          this.httpUtil.comnUpdate({
            action: 'ProcessInstance.end',
            params: {
              processId: this.processId,
              processVersion: this.processVersion,
              processInstanceId: this.processInstanceId,
              submitParams: JSON.stringify(formData),
              nodeName: this.nodeName,
              taskId: this.taskId
            }
          }).then(res => {
            this.closePage();
          })
        },'请确认是否终止该流程','流程终止操作','warning');
      },
      //===============流程终止END====================
      //================转交START====================
      transfer(){
        this.$refs.transferPopup.popup();
      },
      clickTransferFlow(){
        console.log('获取转交的用户',this.transferUserid)
        let validate = this.$refs.transferForm.validate();
        if (validate) {
          Tools.confirm(()=>{
            this.httpUtil.comnUpdate({
              action: 'ProcessInstance.transfer',
              params: {
                processId: this.processId,
                processVersion: this.processVersion,
                processInstanceId: this.processInstanceId,
                submitParams: JSON.stringify(this.submitFormData),
                nodeName: this.nodeName,
                taskId: this.taskId,
                transferUserid:this.transferUserid,
                prodCode: this.prodCode,
              }
            }).then(res => {
              // Tools.alert('任务转成功！','success');
              if (res.success) {
                this.closePage();
              }
            })
          },'请确认是否将该清盘流程转交！','流程转交确认？','warning');
        }
      },
      //================转交END======================
      //================生成文件START======================
      pdfDownload(val) {
        console.log('获取流程实例id',val)
        this.getPdf('清盘',false).then(res=>{
          this.UploadPdf(res,val);
        })
      },
      UploadPdf(res,val) {
        //res拿到base64的pdf
        let pdfBase64Str = res;
        let title = this.prodCode+"-清盘流程明细.pdf"
        let file = this.dataURLtoFile(pdfBase64Str, title + ".pdf");//调用一下下面的转文件流函数
        let fileData = new FormData();
        fileData.append('files',file);
        let uploadData = {};
        console.log('表单参数',this.formData)
        uploadData.prodCode = this.prodCode;
        uploadData.fileName = title;
        uploadData.liquidationId = val;
        fileData.append('params',JSON.stringify(uploadData));
        this.httpUtil.upload({
          url:"/upload-files/server/PmsApp/liquidationAttachment/upload.json",
          formData: fileData
        }).then(data=>{
          if (data.status=='200') {
            this.submitFiles.fileName = data.data.returndata.fileName;
            this.submitFiles.filePath = data.data.returndata.filePath;
            console.log('执行提交审批的方法1',data)
            this.submitApproval();
          }
        });
      },
      //将base64转换为文件,接收2个参数，第一是base64，第二个是文件名字最后返回文件对象
      dataURLtoFile(dataUrl, filename) {
        let arr = dataUrl.split(","),
          mime = arr[0].match(/:(.*?);/)[1],
          bstr = atob(arr[1]),
          n = bstr.length,
          u8arr = new Uint8Array(n);
        while (n--) {
          u8arr[n] = bstr.charCodeAt(n);
        }
        return new File([u8arr], filename, { type: mime });
      },
      //================生成文件END======================
      //================审批页面提交文件==================
      handleFile(updateData) {
        console.log('执行提交审批的方法2',updateData,this.submitFiles);
          this.httpUtil.ajax({
            url: "/wf/wf/attachment/add.json",
            params: {
              "upload_name": this.submitFiles.fileName,
              "upload_code": '1',
              "processId": updateData.returndata.processId,
              "fileType": '2',
              "upload_path": this.submitFiles.filePath
            }
          }).then(res => {
          });
      },
      // 回退到上一步
      back() {
        if (this.backBtnStatus) {
          Tools.alert('第一次发起流程,不支持流程回退！','danger');
          return false;
        }
        // 包装数据
        Tools.confirm(()=>{
          let formData = this.packFormData();
          // 回退，执行表单对应回退步骤的sql，流转该任务
          this.httpUtil.comnUpdate({
            action: 'ProcessInstance.back',
            params: {
              processId: this.processId,
              processVersion: this.processVersion,
              processInstanceId: this.processInstanceId,
              submitParams: JSON.stringify(formData),
              nodeName: this.nodeName,
              taskId: this.taskId
            }
          }).then(res => {
            this.closePage();
          });
        },'请确认是否回退该流程','流程回退操作','warning')
      },
      closePage() {
        Tools.closeCurrentWindow(this);
        // this.$router.push({path: '/main/operation/flow/flow_todo'})
        this.$router.push({path: '/main/desktop'})
      },
      checkForm() {
        for (const form of this.forms) {
          let checkForms = this.$refs[form.formId];
          // 如果是自定义组件，则需要实现validate方法，否则不校验
          if (checkForms && checkForms[0] && checkForms[0].validate) {
            let checkResult = checkForms[0].validate();
            if (checkResult !== true) {
              Tools.alert(checkResult||"表单校验不通过！", "danger");
              return false;
            }
          }
        }
        return true;
      },
      packFormData() {
        this.submitFormData = {};
        for (const form of this.forms) {
          this.submitFormData[form.formId] = {};
          if (form.formType == '1') {
            let components = this.$refs[form.formId];
            // 如果是自定义组件，则需要实现returnData方法
            if (components && components[0] && components[0].returnData) {
              this.submitFormData[form.formId] = components[0].returnData();
            }
          } else {
            this.submitFormData[form.formId] = this.formData[form.formId];
          }
        }
        return this.submitFormData;
      },
      packFormData1() {
        this.submitFlowData = {};
        for (const form of this.forms) {
          if (form.formType == '1') {
            let components = this.$refs[form.formId];
            // 如果是自定义组件，则需要实现returnData方法
            if (components && components[0] && components[0].returnData) {
              this.submitFlowData = components[0].returnData();
            }
          }
        }
        return this.submitFlowData;
      },
      // 产品参数的通用change事件
      dataChange(formId, paramCode) {
        // 存在被依赖关系，修改依赖者的值
        if (this.beRely[formId] && this.beRely[formId][paramCode]) {
          for (const item of this.beRely[formId][paramCode]) {
            if (this.formData[formId][paramCode] == item.paramValue) {
              // 通过set设置参数，解决下拉框组件绑定值不改变的问题
              this.$set(this.formData[formId], item.linkParamCode, item.defaultValue);
            }
            // 等待计算属性更新参数的数据字典之后，刷新下拉框选项
            this.$nextTick(() => {
              if (this.$refs[item.formId + item.linkParamCode] instanceof Array) {
                this.$refs[item.formId + item.linkParamCode][0].load();
              } else {
                this.$refs[item.formId + item.linkParamCode].load();
              }
            });
          }
        }
      },
      loadRelations(formIdList) {
        this.httpUtil.comnQuery({
          action: 'OpFormParamRelation.findRelationsByFormId',
          params: {
            formId: formIdList.join(',')
          }
        }).then(res => {
          res.rows.forEach(relation => {
            // 依赖集合
            if (!this.relations[relation.formId]) {
              this.relations[relation.formId] = {};
            }
            if (!this.relations[relation.linkParamCode]) {
              this.relations[relation.formId][relation.linkParamCode] = [];
            }
            // 被依赖集合
            if (!this.beRely[relation.formId]) {
              this.beRely[relation.formId] = {};
            }
            if (!this.beRely[relation.paramCode]) {
              this.beRely[relation.formId][relation.paramCode] = [];
            }
            if (relation.paramValue != null && relation.paramValue != undefined) {
              let relationArr = relation.paramValue.split(",");
              relationArr.forEach(relationVal => {
                // 后台存的是中文的"空值"
                if (relationVal == '空值') {
                  relationVal = '';
                }
                let _relation = Object.assign({}, relation, {paramValue: relationVal});
                this.relations[relation.formId][relation.linkParamCode].push(_relation);
                this.beRely[relation.formId][relation.paramCode].push(_relation);
              })
            }
          })
        });
      },
      goBack() {
        Tools.closeCurrentWindow(this);
        this.$router.push({
          path: this.prevPagePath
        });
      },
      popupClose() {
        console.log('执行了流程提交的方法------')
        this.$refs.addPopup.close();
        // 提交数据，执行表单对应提交步骤的sql，流转该任务
        this.doSubmit();
      },
    }
  };
</script>

<style scoped>
  .container {
    background-color: white;
    min-height: calc(100% - 10px);
  }
/deep/ .el-page-header {
  height: 65px;
  padding-top: 20px;
}
 /deep/ .el-page-header__left {
  align-items: center;
}
.form-title {
  display: flex;
  align-items: center;
  padding: 10px 0;
  font-weight: 600;
}
.color-block {
  width: 5px;
  height: 15px;
  background-color: #ff9400;
  margin: 0 10px;
}
</style>
