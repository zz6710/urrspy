import Tools from "@/utils/tools";

export default {
  data() {
    return {
      addUrl: '/wf/wf/attachment/add.json',
      templateUrl: 'wf/businessProcess/getTemplate.json',
      updateData: null,
      beforeUpdateValue: true,
      queryData: null,
      processId: null,
    }
  },
  methods: {
    submitVerify(row, action, extraParams) {
      const otherParams = {hasConfirmDialog: false,successAlert: false, failAlert: false}
      Object.assign(otherParams, extraParams)
      if (otherParams.hasConfirmDialog) {
        this.$confirm('确认发起审核吗？', '操作提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          this.handleUpdate(row, action, otherParams)
        })
      } else {
        this.handleUpdate(row, action, otherParams)
      }
    },
    async handleUpdate(row, action, otherParams) {
      this.updateAction = action || this.updateAction
      // 各页面更新前操作
      if (this.beforeHandleUpdate)  {
        this.beforeUpdateValue = await this.beforeHandleUpdate(row)
      }
      if (!this.beforeUpdateValue) return;
      // 更新
      this.updateData = await this.httpUtil.comnUpdate({
        action: this.updateAction,
        params: row,
        successAlert: otherParams.successAlert || false
      })
      if (this.updateData && this.updateData.hasOwnProperty("returndata") && this.updateData.returndata.hasOwnProperty("processId")) {
        // 各页面更新后操作
        this.afterHandleUpdate && this.afterHandleUpdate(this.updateData)
        this.handleProcess(this.updateData, otherParams);
      }else if(this.updateData && this.updateData.success && this.updateData.hasOwnProperty("returnmsg") ){
        this.afterHandleUpdate && this.afterHandleUpdate(this.updateData)
        Tools.alert(this.updateData.returnmsg);
      }
    },
    async handleProcess(updateData, otherParams) {
      this.processId = updateData.returndata.processId;
      if (this.processId) {
        // 各页面查询处理
        if (this.handleQuery) {
          this.queryData = await this.handleQuery()
        }
        // 各页面文件处理
        this.handleFile && await this.handleFile(this.queryData || updateData)
        this.handleGetTemplate(updateData)
      } else {
        if (otherParams.failAlert) {
          Tools.alert("未找到审批流配置信息,请核实！","danger");
        }
        this.$parent.refreshPage && this.$parent.refreshPage();
        this.refreshPage && this.refreshPage();
      }
    },
    async handleGetTemplate(updateData) {
      const data = await this.httpUtil.ajax({
        url: this.templateUrl,
        params: {
          server: updateData.returndata.server
        }
      })
      if (data) {
        let dataList = [];
        dataList.push(data.data)
        dataList.push(updateData.returndata.processId)
        dataList.push(updateData.returndata.server)
        dataList.push(this.dataList)
        dataList.push(this.tabList)
        this.formList = dataList
        this.$nextTick(()=>{
          this.$flowJumpPopup({
            formList: this.formList,
            context: this,
            ok: ()=>{
              this.$parent.refreshPage && this.$parent.refreshPage();
              this.refreshPage && this.refreshPage(this);
            }
          })
        })
      }
    }
  }
}
