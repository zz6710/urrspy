
<template>
  <div>
    <k-form-search-customize data-target="processGrid" v-model="processParam">

      <k-form-item label="审批流名称">
        <k-field-text v-model="processParam.processNameLike"></k-field-text>
      </k-form-item>

      <k-form-item label="状态" >
        <k-field-select v-model="processParam.status" data-dict="business_enable"  />
      </k-form-item>

      <k-form-item label="创建人" >
        <k-field-select v-model="processParam.creator" :data-params="{roleId:'3'}" data-action="User.getUserByRoleId2" data-display-field="username" data-value-field="userid"/>
      </k-form-item>

      <k-form-item label="创建日期" >
        <k-field-date v-model="processParam.createStartDate"  data-type="daterange" />
      </k-form-item>

<!--      <k-form-item label="创建日期" :data-col="2" >-->
<!--        <k-field-date v-model="processParam.createStartDate" data-type="date"  style="width: 150px;"/>-->
<!--        - -->
<!--        <k-field-date v-model="processParam.createEndDate" data-type="date"   style="width: 150px;" />-->
<!--      </k-form-item>-->

      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP"  data-target="addProcess"
             v-if="global.isShowAuthorityButton('NewWfBusinessConfig.add')">
        <md-icon md-src="/static/svg/add.svg"/>
        新增
      </k-btn>

      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP"  data-target="testProcess">
        <md-icon md-src="/static/svg/add.svg"/>
        测试审批流
      </k-btn>

    </k-form-search-customize>
    <k-grid ref="processGrid" data-url="/wf/businessProcess/getAllProcess.json"  @data-row-select="selectRow" >
      <k-grid-column data-align="center" data-header="id" data-name="id" data-width="200px" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="表单ID" data-name="formId" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="服务菜单" data-name="server" data-width="200px"  :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="审批流名称" data-name="displayName" data-width="200px" />
      <k-grid-column data-align="center" data-header="版本号" data-name="version" data-width="100px"/>
      <k-grid-column data-align="center" data-header="状态" data-name="status"  data-dict="business_enable" data-width="150px"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="creator" data-width="150px"/>
      <k-grid-column data-align="center" data-header="创建时间" data-name="createTime" data-width="200px"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="btn-custom-text" data-descript="详情" data-functype="POPUP"  data-size="mini" @click="processEdit(scope.row.row,'detail')"  data-target="">
          详情
        </k-btn>
        <k-btn class="btn-custom-text" :class="isDisabled(scope.row.row)?'md-disabled':''"  data-descript="启用" :disabled="isDisabled(scope.row.row)" data-size="mini" data-functype="POPUP"
               data-target="grid"  @click="getEnableStatus(scope.row.row)"
               v-if="global.isShowAuthorityButton('NewWfBusinessConfig.turnOn')">
          启用
        </k-btn>
        <k-btn class="btn-custom-text" :class="scope.row.row.status=='0'?'md-disabled':''" data-descript="停用" :disabled="scope.row.row.status=='0'" data-size="mini" data-functype="POPUP"
               data-target="grid"  @click="getEnableStatus(scope.row.row)"
               v-if="global.isShowAuthorityButton('NewWfBusinessConfig.turnDown')">
          停用
        </k-btn>
        <k-btn class="btn-custom-text" :class="isDisabled(scope.row.row)?'md-disabled':''" data-descript="设置审批流" data-functype="POPUP" :disabled="isDisabled(scope.row.row)" data-size="mini" @click="processEdit(scope.row.row,'edit')"
               data-target=""
               v-if="global.isShowAuthorityButton('NewWfBusinessConfig.edit')">
          修改
        </k-btn>
        <k-btn class="btn-custom-text" :class="isDisabled(scope.row.row)?'md-disabled':''" data-descript="表单设置" data-functype="POPUP" :disabled="isDisabled(scope.row.row)" data-size="mini" @click="formSetInit(scope.row.row)"
               data-target="formProcessPopup"
               v-if="global.isShowAuthorityButton('NewWfBusinessConfig.formEdit')">
          表单
        </k-btn>
        <k-btn class="btn-custom-text" data-descript="历史版本" data-functype="POPUP"  data-size="mini"
               data-target="historicalVersionPopup">
          历史版本
        </k-btn>
        <k-btn data-functype="SUBMIT" data-size="mini" class="btn-custom-text" :class="isDisabled(scope.row.row)?'md-disabled':''" :disabled="isDisabled(scope.row.row)" data-descript="删除" :data-after-success="refreshList"
               data-target="grid" data-url="/wf/businessProcess/deleteProcessByProcessId.json" data-confirm data-type="danger"
               v-if="global.isShowAuthorityButton('NewWfBusinessConfig.delete')">
          删除
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="updateProcess" data-title="修改" :dataDialogDrag="true">
      <k-form ref="prodGroupUserForm1" :data-col="3" data-input-width="160px" data-label-width="80px" data-total-width="1188px">
        <k-form-item label="业务操作" >
          <k-field-select v-model="formDataList.server"  data-action="WfBusinessConfig.findServerMethodTree" data-display-field="name"
                          data-value-field="id" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="审批任务名称" data-label-width="180px">
          <k-field-text v-model="formDataList.displayName"  :data-allowblank="false" />
        </k-form-item>
<!--        <k-form-item label="是否在移动端显示" data-label-width="180px">
          <k-field-select v-model="formDataList.appDisplay"  data-dict="1yes0no"   />
        </k-form-item>
        <k-form-item label="是否允许审批中增加节点" data-label-width="180px">
          <k-field-select v-model="formDataList.addNode"   data-dict="1yes0no" :data-allowblank="false" />
        </k-form-item>-->
        <k-form-item label="是否绑定产品" data-label-width="180px">
          <k-field-select v-model="formDataList.bindProd" data-dict="1yes0no" :data-allowblank="false"/>
        </k-form-item>
      </k-form>
      <div style="border:solid 1px" ></div><br>


      <k-form ref="prodGroupUserForm2" v-for="(item,index) in envItems" :key="index"  style="display: inline-block" :data-col="2"
              data-input-width="103px" data-label-width="90px" data-total-width="1188px" >
        <k-form-item label="节点名称" data-input-width="125px">
          <k-field-text v-model="item.nodeName" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="审批类型" >
          <k-field-select v-model="item.nodeType"  data-dict="approval_type"  :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="审批角色" v-if="!item.actorId">
          <k-field-select v-model="item.roleId "  data-display-field="rolename"  :data-multiple="true" :data-graphql='queryRoleGraphql'
                          data-value-field="roleid" :data-allowblank="true"/>
        </k-form-item>
        <k-form-item label="审批等级" :hidden="true">
          <k-field-text v-model="item.nodeLevel = index+1 " />
        </k-form-item>
        <k-form-item label="审批人员" v-if="!item.roleId">
          <k-field-select v-model="item.actorId" data-action="User.getAllUser" data-display-field="label" :data-multiple="true" :data-allowblank="true"
                          data-value-field="value" />
        </k-form-item>
<!--        <k-form-item label="是否部门领导审批" data-label-width="80px" data-input-width="90px" v-if="item.nodeType!=='3'">
          <k-field-select v-model="item.leaderApproval" data-dict="1yes0no" />
        </k-form-item>-->
<!--        <k-form-item label="详情是否上传附件" >
          <k-field-select v-model="item.isUploadFile"  data-dict="1yes0no"  :data-allowblank="true" />
        </k-form-item>
        <k-form-item label="执行接口" data-input-width="340px">
          <k-field-text v-model="item.actionMethod"/>
        </k-form-item>
        <k-form-item label="节点条件" data-input-width="340px" >
          <k-field-text v-model="item.addCondition" :data-allowblank="true" />
        </k-form-item>-->
        <k-form-item label="是否可撤销" >
          <k-field-select v-model="item.isCancle"  data-dict="1yes0no"  :data-allowblank="true" />
        </k-form-item>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增" @click="()=>envItems.push({})" >
          <md-icon>add</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行" @click="deleteEvent(index)">
          <md-icon md-src="/static/svg/delete.svg" />
        </k-btn>
      </k-form>

      <div style="border:solid 1px" ></div><br>

      <k-form ref="prodGroupUserForm3" :data-col="5" data-input-width="120px"
              data-label-width="90px" data-total-width="1188px" >
        <k-form-item label="节点名称" >
          <k-field-text v-model="formData.nodeName = '抄送'" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="审批类型" >
          <k-field-text v-model="formData.nodeType = '抄送'"  :data-disabled="true"  />
        </k-form-item>
        <k-form-item label="抄送人员">
          <k-field-select v-model="formData.readonlyActor" data-action="User.getAllUser" data-display-field="label" :data-multiple="true" :data-allowblank="true"
                          data-value-field="value" />
        </k-form-item>
        <k-form-item label="全流程推送">
          <k-field-select v-model="formData.isPush" data-dict="1yes0no" :data-disabled="false"/>
        </k-form-item>
      </k-form>

      <div style="margin: 0 auto; width: 255px">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT"  :data-handler="submitHandle"
               data-target="prodInfoGrid" :data-model="formData,formDataList">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>
    </k-popup>

    <k-popup ref="infoProcess" data-title="详情" :dataDialogDrag="true">
      <k-form ref="prodGroupUserForm1" :data-col="3" data-input-width="160px" data-label-width="80px" data-total-width="1188px">
        <k-form-item label="业务操作" >
          <k-field-select v-model="formDataList.server"  data-action="WfBusinessConfig.findServerMethodTree" data-display-field="name"
                          data-value-field="id" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="审批任务名称" data-label-width="180px">
          <k-field-text v-model="formDataList.displayName"  :data-allowblank="false"  :data-disabled="true" />
        </k-form-item>
<!--        <k-form-item label="是否允许审批中增加节点" data-label-width="180px">
          <k-field-select v-model="formDataList.addNode"   data-dict="1yes0no" :data-allowblank="false"   :data-disabled="true"/>
        </k-form-item>-->
        <k-form-item label="是否绑定产品" data-label-width="180px">
          <k-field-select v-model="formDataList.bindProd" data-dict="1yes0no" :data-allowblank="false"  :data-disabled="true"/>
        </k-form-item>
      </k-form>
      <div style="border: 1px dashed" ></div>


      <k-form ref="prodGroupUserForm2" v-for="(item,index) in envItems" :key="index"  :data-col="2"
              data-input-width="103px" data-label-width="90px" data-total-width="1118px" >
        <k-form-item label="节点名称" data-input-width="125px">
          <k-field-text v-model="item.nodeName" :data-allowblank="false"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="审批类型" >
          <k-field-select v-model="item.nodeType"  data-dict="approval_type"  :data-allowblank="false"  :data-disabled="true" />
        </k-form-item>
        <k-form-item label="审批角色" v-if="!item.actorId" data-input-width="110px">
          <k-field-select v-model="item.roleId "  data-display-field="rolename"  :data-multiple="true" :data-graphql='queryRoleGraphql'
                          data-value-field="roleid" :data-allowblank="true"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="审批等级" :hidden="true">
          <k-field-text v-model="item.nodeLevel = index+1 "   :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="审批人员" v-if="!item.roleId">
          <k-field-select v-model="item.actorId" data-action="User.getAllUser" data-display-field="label" :data-multiple="true" :data-allowblank="true"
                          data-value-field="value" :data-disabled="true"/>
        </k-form-item>
<!--        <k-form-item label="是否部门领导审批" data-label-width="80px" >
          <k-field-select v-model="item.leaderApproval" data-dict="1yes0no"  :data-disabled="true" />
        </k-form-item>
        <k-form-item label="详情是否上传附件" >
          <k-field-select v-model="item.isUploadFile"  data-dict="1yes0no"  :data-allowblank="true" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="执行接口" data-input-width="340px">
          <k-field-text v-model="item.actionMethod" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="节点条件" data-input-width="340px" >
          <k-field-text v-model="item.addCondition" data-disabled="true"/>
        </k-form-item>-->
        <k-form-item label="是否可撤销" >
          <k-field-select v-model="item.isCancle"  data-dict="1yes0no"  :data-allowblank="true" :data-disabled="true"/>
        </k-form-item>
      </k-form>

      <div style="border: 1px dashed" ></div>

      <k-form ref="prodGroupUserForm3" :data-col="5" data-input-width="120px"
              data-label-width="90px" data-total-width="1118px" >
        <k-form-item label="节点名称" >
          <k-field-text v-model="formData.nodeName = '抄送'" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="审批类型" >
          <k-field-text v-model="formData.nodeType = '抄送'"  :data-disabled="true"  />
        </k-form-item>
        <k-form-item label="抄送人员">
          <k-field-select v-model="formData.readonlyActor" data-action="User.getAllUser" data-display-field="label" :data-multiple="true" :data-allowblank="true"
                          data-value-field="value" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="全流程推送">
          <k-field-select v-model="formData.isPush" data-dict="1yes0no" :data-disabled="true"/>
        </k-form-item>
      </k-form>

     <!-- <div style="margin: 0 auto; width: 255px">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT"  :data-handler="submitHandle"
               data-target="prodInfoGrid" :data-model="formData,formDataList">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </div>-->
      <div style="text-align: center">
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
      </div>
    </k-popup>


    <k-popup ref="addProcess"  data-title="设置审批流" :dataDialogDrag="true">
        <div style="overflow: auto">
          <flowProcessConfigurationAdd ref="flowProcessConfigurationAdd" @submitClose="popupClose"/>
        </div>
    </k-popup>

    <k-popup ref="testProcess" data-title="表单设置" :dataDialogDrag="true">
      <k-form ref="testProcessform" >
        <k-form-item label="文本输入">
          <k-field-text v-model="formData.textContent"/>
        </k-form-item>
        <k-form-item label="是否选择" >
          <k-field-select v-model="formData.yesOrNo" data-dict="1yes0no"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" :data-model="formData" data-functype="SUBMIT" :data-handler="testProcessMethod" data-from="testProcessform">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

<!--    <k-popup ref="updateProcess" data-title="修改审批流" :data-dialog-drag="true">-->
<!--      <flowProcessConfigurationUpdate ref="flowProcessConfigurationUpdate"   @submitClose="updatePopupClose"/>-->
<!--    </k-popup>-->

    <k-popup ref="formProcessPopup" data-title="表单设置" :dataDialogDrag="true">
      <k-form ref="formPopup" >
        <k-form-item label="业务操作">
          <k-field-select v-model="copyFormData.server" data-action="WfBusinessConfig.findServerMethodTree" data-display-field="name"
                          data-value-field="id" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="表单方式" :hidden="false">
          <k-field-select v-model="copyFormData.formType" data-dict="formType"  @data-on-change="change"/>
        </k-form-item>

        <k-form-item label="指定页面URL" v-if="copyFormData.formType == '2'">
          <k-field-text v-model="copyFormData.formUrl"  :data-allowblank="copyFormData.formType == '2'"/>
        </k-form-item>

        <el-checkbox-group v-model="copyFormData.checkList" v-if="copyFormData.formType == '1'" >
          <el-checkbox v-for="list in dataList" v-if="list.displayName"  :label="list"  :key="list">{{list.displayName}}</el-checkbox>
        </el-checkbox-group>


        <k-form ref="prodGroupUserForm2"  v-for="(item,index) in envItems" v-if="copyFormData.formType == '3'"  :data-col="2" data-input-width="380px" :key="index"
                data-label-width="190px" data-total-width="1118px" >
          <k-form-item label="自定义字段配置" >
            <k-field-text v-model="item.displayName" :data-allowblank="copyFormData.formType == '3'" inputType="textarea"  :data-max-length="200" :rows="1"/>
          </k-form-item>
          <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增" @click="()=>envItems.push({})" >
            <md-icon>add</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行" @click="deleteEvent(index)">
            <md-icon md-src="/static/svg/delete.svg" />
          </k-btn>
        </k-form>



        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" :data-model="copyFormData" data-functype="SUBMIT" :data-handler="copyFlowTemplate" data-from="prodGroupUserForm2">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="historicalVersionPopup" data-title="历史版本">
      <historicalVersion ref="historicalVersion" :server="valueData.server" :id="valueData.id"
                         url="/wf/hisTemplate/queryHisTemplates.json" @submitClose="historicalVersionPopupClose"></historicalVersion>
      <div style="text-align: center">
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
      </div>
    </k-popup>


  </div>
</template>

<script>
  import flowProcessConfigurationAdd from "@/pages/flow/flowProcessConfigurationAdd";
  import flowProcessConfigurationUpdate from "@/pages/flow/flowProcessConfigurationUpdate";
  import flowHistoryVersion from "@/pages/flow/flowHistoryVersion";
  import Tools from "@/utils/tools";
  import historicalVersion from "@/pages/flow/historicalVersion";
  import { assign } from "lodash";

  export default {
    name:'flowProcessConfiguration',
    components: {
      flowProcessConfigurationAdd,
      flowProcessConfigurationUpdate,
      flowHistoryVersion,
      historicalVersion,
    },
    data() {
      return {
        state: false,
        processId: '',
        dataList: [],
        formData: {},
        formList: {},
        params: {},
        formId:"",
        flowTemplateList: [],
        value: [],
        copyFormData: {
          checkList: [],
        },
        envItems: [{"ratioIndex": ''}, {"coefficient": ''}],
        queryRoleGraphql: "{queryRole(action:\"find\",roleids:\"0\") {rows{roleid, rolename, parentroleid, descript},results}}",
        checkedList: [],
        processParam: {},
        formDataList:{},
        selectRowData:{},
        valueData:{},
        num:'',

      };
    },
    created() {
      this.envItems.length = 1;
      this.reloadGroupData();

    },
    methods: {

      historicalVersionPopupClose(){
        this.$refs.historicalVersionPopup.close();
      },

      selectRow(row, column, event) {
        this.selectRowData = assign({}, row)
        this.valueData = assign({}, row)
      },

      refreshLoad(){
        location.reload();
      },

      processHistorical(value){
        console.log('获取参数',value)
        this.httpUtil.ajax({
          url: 'wf/hisTemplate/queryHisTemplates.json',
          params: {id : value.id ,server:value.server},
        }).then(data => {
          console.log('查询参数',data)
          this.dataList = data.rows;
        });

      },


      refreshList(){
        this.$refs.processGrid.load();
      },

      testProcessMethod(value){
        this.submitVerify(value, "ProdFlow.testProcessMethod", {failAlert: false});
      },

      submitHandle(value) {
        console.log('获取参数',value)

        let result = true;

        result = this.$refs.prodGroupUserForm1.validate();
        let form2s = this.$refs.prodGroupUserForm2;
        if (form2s && form2s.length > 0) {
          for (let i = 0; i < form2s.length; i++) {
            result = result && form2s[i].validate();
          }
        }
        if (result === false) {
          return false;
        }
        if(this.envItems.length<1){
          Tools.alert("未获取到信息，请稍后 !","danger")
          return false;
        }
        if (this.envItems && this.envItems.length > 0) {
          // console.log('获取参数11111',this.envItems)
          value.nodeList = JSON.stringify(this.envItems);
          // console.log("传递参数",value.nodeList)
        }
        let len = this.envItems.length
        let list = JSON.parse(value.nodeList);
        if (list.length > 1){
          let k = 0;
          for (let i = k; i < list.length; i++) {
            let nodeName = list[i].nodeName;
            k++
            console.log('i==>',k)
            for (let j = k; j < list.length; j++) {
              if (list[j].nodeName == nodeName){
                Tools.alert("节点名称不能重复", "danger");
                return false;
              }
            }
          }
        }
        this.httpUtil.ajax({
          url:"wf/businessProcess/updateTemplateProcess.json" ,
          params: {server:value.busnessProcess.server , nodeList: value.nodeList , appDisplay:value.busnessProcess.appDisplay , actorId:value.actorId ,highestLevel: len , actorId:value.readonlyActor,
            displayName : value.busnessProcess.displayName , addNode : value.busnessProcess.addNode ,id :value.busnessProcess.id,bindProd:value.busnessProcess.bindProd,
            isPush: value.isPush},
        }).then(data => {
          console.log('执行参数',data)
          if (data.data = '更新流程成功'){
            Tools.alert("更新流程成功 ")
            this.$refs.updateProcess.close();
            this.reloadGroupData();
            this.$refs.processGrid.load();
          }else{
            Tools.alert("更新流程失败 ","danger")
          }

        });
      },

      getEnableStatus(value){
        console.log('启停',value)
        let enableStatus = value.status;
        if (enableStatus == '0'){
          enableStatus = '1'
          this.httpUtil.ajax({
            url: 'wf/busiConfigNew/setBusiConfigNNewStatus.json',
            params: {status : enableStatus,server:value.server},
          }).then(data => {
            console.log('启用',data)
            if (data.data == '审批流模板启用成功') {
              Tools.alert("审批流模板启用成功");
              this.reloadGroupData();
              this.$refs.processGrid.load();
            }
          });
        }else {
          enableStatus = '0'
          this.httpUtil.ajax({
            url: 'wf/busiConfigNew/setBusiConfigNNewStatus.json',
            params: {status : enableStatus ,server:value.server},
          }).then(data => {
            console.log('停用',data)
            if (data.data == '审批流模板停用成功') {
              Tools.alert("审批流模板停用成功");
              this.reloadGroupData();
              this.$refs.processGrid.load();
            }
          });
        }
      },
      isDisabled(row){
        if (row.status=='1'){
          return true;
        }else {
          return false;
        }
      },
      popupClose() {
        this.$refs.addProcess.close();
        this.reloadGroupData();
        //location.reload();
      },


      reloadGroupData() {
        this.httpUtil.ajax({
          url: 'wf/businessProcess/getAllProcess.json',
          params: {},
        }).then(data => {
          console.log('查询结果', data);
          this.flowTemplateList = data.data
          this.$refs.processGrid.load();
        });
      },
      //查询表单字段
      findModelFieldMap(server) {
        // console.log('传递参数',server)
        let arrayList = [];
        let cnPattern = /[\u4E00-\u9FA5]/;
        // console.log('正则校验....',cnPattern.test('产品风险评分状态'))
        if (server) {
          this.httpUtil.ajax({
            url: 'server/form/PmsApp/getFormModel/getModelFieldMap.json',
            params: {server: server},
          }).then(data => {
            // console.log('data数据',data)
            if (data.success == false) {
              return false;
            } else {
              let list = data.formModel;
              for (let i = 0; i < list.length; i++) {
                if (cnPattern.test(list[i].displayName) == true){
                  arrayList.push(list[i])
                }
              }
              // console.log("arrayLit数据",arrayList)
              this.dataList = arrayList;
            }
          });
        } else {
          return false;
        }
      },

      //表单类型
      copyList(value) {
        console.log('点击获取', value)
        let len = value.length;
        if (len > 0) {
          this.copyFormData.formUrl = '',
            this.state = true
        } else {
          this.state = false
        }
      },

      change(data){
        this.envItems.length=1;
        this.envItems[0].displayName = '';
        this.copyFormData.formUrl="";
        if(data=="1"){
          this.findModelFieldMap(this.copyFormData.server);
        }
      },

      processEdit:function(info,type){
        console.log('点击获取,修改',info)
        this.httpUtil.ajax({
          url:"/wf/businessProcess/getTemplateByServerOrId.json" ,
          params: {server:info.server , processId : info.id},
        }).then(data => {
          console.log('执行参数',data)
          this.formData = data.data;
          let isPush = data.data.busnessProcess.isPush;
          this.$set(this.formData,"isPush",isPush);
          console.log('this.formData.isPush',this.formData.isPush)
          this.envItems = data.data.nodeList;
          this.formDataList = data.data.busnessProcess;
          // console.log('执行参数',this.formList)
          if(type=='detail'){
            this.$refs.infoProcess.popup();
          }else if(type=='edit'){
            this.$refs.updateProcess.popup();
          }
        });
      },

      //审批流执行后刷新
      refreshPage(){
        this.$refs.testProcess.close();
      },

      copyFlowTemplate(data) {

        console.log('获取canshu', data)

        let result = true;

        result = this.$refs.formPopup.validate();
        let form2s = this.$refs.prodGroupUserForm2;
        if (form2s && form2s.length > 0) {
          for (let i = 0; i < form2s.length; i++) {
            result = result && form2s[i].validate();
          }
        }
        if (result === false) {
          return false;
        }
        if (this.envItems.length < 1) {
          Tools.alert("未获取到信息，请稍后 !", "danger")
          return false;
        }
        if (this.envItems && this.envItems.length > 0) {
          console.log('获取参数11111',this.envItems)
          // let list = this.envItems;
          // for (let i = 0; i < list.length; i++) {
          //   displayName = list[i].displayName
          // }
          data.nodeList = JSON.stringify(this.envItems);
          console.log('传递参数',data.nodeList)
          // this.nodeList = value.json;
          // console.log("传递参数",data.nodeList)
        }
        let list = JSON.parse(data.nodeList);
        if (list.length > 1){
          let k = 0;
          for (let i = k; i < list.length; i++) {
            let displayName = list[i].displayName;
            k++
            console.log('i==>',k)
            for (let j = k; j < list.length; j++) {
              if (list[j].displayName == displayName){
                Tools.alert("自定义字段不能重复", "danger");
                return false;
              }
            }
          }
        }
        let json = null;
        if (data.checkList.length > 0) {
          json = JSON.stringify(data.checkList);
        } else if (data.nodeList) {
          json = JSON.stringify(data.nodeList);
        }
        console.log('json', json)

        this.httpUtil.ajax({
          url: 'wf/form/design/newSave.json',
          params: {
            processId: this.processId,
            server: data.server,
            formUrl: data.formUrl,
            formType: data.formType,
            json: json
          },
          successAlert: true,
        }).then(data => {
          console.log('data', data)
          this.$refs.formProcessPopup.close();
          this.reloadGroupData();
          location.reload();
        });
      },

      formSetInit:function(info){
        // this.copyFormData.formUrl = '';
        console.log('info-------->',info);
        this.$set(this.copyFormData,'server',info.server);
        this.processId = info.id;
        if (info.formId && info.formId !="" && info.formId !=null){
          //初始化页面表单配置为1的数据
          this.findModelFieldMap(info.server);
          let a = [];
          let b = [];
          this.formId=info.formId;
          this.httpUtil.ajax({
            url:"wf/conf/queryformJson.json" ,
            params: {formId : info.formId},
          }).then(data => {
            // console.log('查询参数',data)
            let value = data.data;
            // console.log('value',value.json)
            if (value.json){
              if (value.formType == '1'){
                this.$set(this.copyFormData,'formType',value.formType)
                this.checkedList = JSON.parse(value.json);
                for (let j = 0; j <this.dataList.length; j++) {
                  for (let i = 0; i < this.checkedList.length; i++) {
                    // console.log('this.checkedList',this.checkedList[i].displayName)
                    if (this.checkedList[i].displayName == this.dataList[j].displayName){
                      this.$set(this.dataList[j],'state',true)
                      b.push(this.dataList[j])
                    }else{
                      this.$set(this.dataList[j],'state',false)
                    }
                  }
                  a.push(this.dataList[j])
                }
                this.copyFormData.checkList = b
                this.dataList = a
                // console.log('获取参数aaaaaaaaaaaa',this.dataList)
                // console.log('获取参数bbbbbbbbbb', this.copyFormData.checkList)
              }else if (value.formType == '2'){
                this.$set(this.copyFormData,'formType',value.formType)
                this.$set(this.copyFormData,'formUrl',value.formUrl)
                // this.copyFormData.formUrl = value.formUrl
                this.envItems.length=1;
                this.envItems[0].displayName = '';
              }else if (value.formType == '3'){
                this.$set(this.copyFormData,'formType',value.formType)
                this.envItems =JSON.parse(JSON.parse(value.json));
                console.log('this.envItems',this.envItems.length);
                this.copyFormData.formUrl="";
              }
            }
          });
        }
        else{
          this.envItems.length=1;
          this.envItems[0].displayName = '';
          this.copyFormData.formUrl="";
          this.formId="";
          this.$set(this.copyFormData,"formType","3");//默认为自定义
        }
      },
      deleteEvent(index) {
        index = this.num;
        if (this.envItems.length > 1) {
          this.envItems.splice(index, 1);
        }
      },

    },
    watch: {
      "docVersions": function (val) {
        //console.log('文档版本',val)
      },
      envItems:function (value) {
        // console.log('监听行数',value.length)
        this.num = value.length - 1;
      },
      formPopup:function (data) {
        console.log('监听弹窗是否关闭',data)
      }
    }
  };
</script>


<style lang="scss" scoped>

</style>

