<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="disclosurePubEmailGrid">
        <k-form-item label="渠道名称">
          <k-field-text v-model="searchParam.channelName" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="状态" dataLabelWidth="70px" dataInputWidth="100px">
          <k-field-select   v-model="searchParam.status" data-dict="xp_status" />
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addDisclosureProdRulePopup" slot="button" v-if="global.isShowAuthorityButton('DisclosureChannel.insertDisclosureChannel')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosurePubEmailGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="230px"
              data-action="DisclosureChannel.findDisclosureChannelAuth">
        <k-grid-column data-header="渠道id" data-name="id" :data-sortable="true" data-default-sort="DESC" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="渠道名称" data-name="channelName" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否对接" data-name="isDocking" data-dict="xp_if_ok" data-width="80" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="主机" data-name="hostIp" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="协议" data-name="protocol"  data-dict="xp_protocol" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="端口号" data-name="portCode" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="文件路径" data-name="filePath" ></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="remark" ></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="最近维护时间" data-name="crtDate" data-render="renderDateTimeCreate" data-width="160" data-hidden="true"></k-grid-column>-->
<!--        <k-grid-column data-align="center" data-header="最近维护人名称" data-name="crtUserName" data-hidden="true"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="状态" data-name="status" data-dict="xp_status" data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope">
<!--          <k-field-bswitch data-on-value="1" data-off-value="0" v-model="scope.row.row.status" data-on-action="DisclosureChannel.enableChannel"
                           data-off-action="DisclosureChannel.stopChannel" :data-params=scope.row.row :data-confirm="true" data-on-confirm-info="启用"
                           data-off-confirm-info="停用" :data-after-handler="flashing"/>-->
          <k-btn class="md-success"  data-functype="SUBMIT" data-size="mini" data-action="DisclosureChannel.enableChannel"
                 data-target="disclosurePubEmailGrid" :data-confirm="true" v-if="scope.row.row.status == '0' && global.isShowAuthorityButton('DisclosureChannel.enableChannel')">
            启用
          </k-btn>
          <k-btn class="md-rose" data-functype="SUBMIT" data-action="DisclosureChannel.stopChannel" v-if="scope.row.row.status == '1' && global.isShowAuthorityButton('DisclosureChannel.stopChannel')"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="disclosurePubEmailGrid" >
            停用
          </k-btn>
          <k-btn class="btn-custom-plain" data-descript="修改" data-functype="POPUP" data-size="mini"
                 data-target="editDisclosureSourcePopup" v-if="global.isShowAuthorityButton('DisclosureChannel.updateDisclosureChannel')"
          >
            修改
          </k-btn>
          <k-btn class="md-danger" data-functype="SUBMIT" data-action="DisclosureChannel.deleteDisclosureChannelById"
                 data-size="mini" v-if="global.isShowAuthorityButton('DisclosureChannel.deleteDisclosureChannelById')"
                 data-type="danger" data-target="disclosurePubEmailGrid" :data-confirm="true" data-descript="删除" v-show="true"  >
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>



    <!--    添加产品信披渠道弹出框   -->
    <k-popup ref="addDisclosureProdRulePopup" data-title="新增">
      <k-form ref="addDisclosureProdRuleForm" :data-col="2" :isFormBodyScreen="true">
        <k-form-item label="序号"  v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="渠道名称">
          <k-field-text v-model="formData.channelName" :data-allowblank="false" :data-max-length="100"/>
        </k-form-item>

        <k-form-item label="是否对接" >
          <k-field-select  v-model="formData.isDocking" data-dict="xp_if_ok" :data-allowblank="false" :data-default-value="'0'"/>
        </k-form-item>
        <k-form-item label="对接方式"  v-if="formData.isDocking === '1'">
          <k-field-select v-model="formData.dockingWay" data-dict="xp_docking_way" :data-allowblank="dockingChange" :data-disabled="true" data-default-value="02"/>
        </k-form-item>
        <k-form-item label="主机"  v-if="formData.isDocking === '1'">
<!--          <k-field-text v-model="formData.hostIp" :data-allowblank="dockingChange" :data-max-length="15" data-regx="^((25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))$" data-regx-text="请输入正确ip"/>-->
          <k-field-text v-model="formData.hostIp" :data-allowblank="dockingChange" :data-max-length="50" />
        </k-form-item>
        <k-form-item label="协议"  v-if="formData.isDocking === '1'">
          <k-field-select v-model="formData.protocol" data-dict="xp_protocol" :data-allowblank="dockingChange"/>
        </k-form-item>
        <k-form-item label="端口号"   v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.portCode" :data-allowblank="dockingChange" :data-max-length="4" data-regx="^\+?[1-9][0-9]*$" data-regx-text="请输入正确端口号"/>
        </k-form-item>
        <k-form-item label="文件路径"  v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.filePath" :data-allowblank="dockingChange" :data-max-length="256"  data-regx-text="请输入正确路径"/>
        </k-form-item>
        <k-form-item label="用户名"  v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.userName" :data-allowblank="dockingChange" :data-max-length="100"/>
        </k-form-item>

        <k-form-item label="密码"  v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.password" :data-allowblank="dockingChange" data-show-password="true" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="确认密码"  v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.confirmPassword" :data-allowblank="dockingChange" data-show-password="true" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="备注" :data-col="2" >
          <k-field-text v-model="formData.remark" :data-allowblank="true"
                        :data-max-length="256" inputType="textarea" :rows="3"/>
        </k-form-item>

      </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="addPasswordCheck" data-from="addDisclosureProdRuleForm"
                 :data-model="formData" data-target="disclosurePubEmailGrid"  ref="subBtn" data-action="DisclosureChannel.insertDisclosureChannel">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
    </k-popup>


    <!--    修改信披字段弹出框   -->
    <k-popup ref="editDisclosureSourcePopup" data-title="修改" @data-opened="editOpened()">
      <k-form ref="editDisclosureSourceForm" :data-col="2" :isFormBodyScreen="true">
        <k-form-item label="序号"  v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="渠道名称">
          <k-field-text v-model="formData.channelName" :data-allowblank="false"  :data-max-length="100"/>
        </k-form-item>

        <k-form-item label="是否对接">
          <k-field-select v-model="formData.isDocking" data-dict="xp_if_ok" :data-allowblank="false"  />
        </k-form-item>
        <k-form-item label="对接方式" v-if="formData.isDocking === '1'">
          <k-field-select v-model="formData.dockingWay" data-dict="xp_docking_way" :data-allowblank="dockingChange" :data-disabled = "true" data-default-value = "02"/>
        </k-form-item>
        <k-form-item label="主机" v-if="formData.isDocking === '1'">
<!--          <k-field-text v-model="formData.hostIp" :data-allowblank="dockingChange" :data-max-length="15" data-regx="^((25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))$" data-regx-text="请输入正确ip"/>-->
           <k-field-text v-model="formData.hostIp" :data-allowblank="dockingChange"/>
        </k-form-item>
        <k-form-item label="协议" v-if="formData.isDocking === '1'">
          <k-field-select v-model="formData.protocol" data-dict="xp_protocol" :data-allowblank="dockingChange"/>
        </k-form-item>
        <k-form-item label="端口号" v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.portCode" :data-allowblank="dockingChange" :data-max-length="4" data-regx="^\+?[1-9][0-9]*$" data-regx-text="请输入正确端口号"/>
        </k-form-item>
        <k-form-item label="文件路径"  v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.filePath" :data-allowblank="dockingChange" :data-max-length="256"  data-regx-text="请输入正确路径"/>
        </k-form-item>
        <k-form-item label="用户名" v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.userName" :data-allowblank="dockingChange" :data-max-length="100"/>
        </k-form-item>

        <k-form-item label="密码" v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.password" :data-allowblank="dockingChange" data-show-password="true" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="确认密码" v-if="formData.isDocking === '1'">
          <k-field-text v-model="formData.confirmPassword" :data-default-value="this.formData.password" :data-allowblank="dockingChange" data-show-password="true" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="初始密码" v-if="false">
          <k-field-text v-model="formData.initPassword" />
        </k-form-item>
        <k-form-item label="备注" :data-col="2">
          <k-field-text v-model="formData.remark" :data-allowblank="true"
                        :data-max-length="256" inputType="textarea" :rows="3"/>
        </k-form-item>
      </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="addPasswordCheck" data-from="editDisclosureSourceForm"
                 :data-model="formData" data-target="disclosurePubEmailGrid" ref="ediBtn" data-action="DisclosureChannel.updateDisclosureChannel">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name:"M8DisclosurePubEmail",
  data() {
    return {
      formData: {
        password: '',
        confirmPassword: '',
        isDocking:'',
      },
      dockingChange: false,
      selectRowData: {},
      searchParam: {},
    };
  },
  watch:{
    'formData.isDocking'(){
      if (this.formData.isDocking ==='1'){
        this.dockingChange = false;
      }else {
        this.$set(this.formData, 'dockingWay', '');
        this.$set(this.formData, 'hostIp', '');
        this.$set(this.formData, 'protocol', '');
        this.$set(this.formData, 'filePath', '');
        this.$set(this.formData, 'userName', '');
        this.$set(this.formData, 'password', '');
        this.$set(this.formData, 'confirmPassword', '');
        this.$set(this.formData, 'portCode', '');
        this.dockingChange = true;
      }
    }
  },
  methods: {
    flashing(){
      this.$refs.disclosurePubEmailGrid.load();
    },
    addPasswordCheck(){
      if(this.formData.password!==this.formData.confirmPassword && this.formData.isDocking ==='1'){
        Tools.alert("密码与确认密码不一致！","danger");
        return false;
      }
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    pwdShowEditBtn(value){

    },
    pwdShowAddBtn(value){

    },
    editOpened(){
      this.$set(this.formData, 'initPassword', this.formData.password);
    }
  }
};
</script>
<style lang="scss" scoped>
.md-switch {
  position: relative;
  display: inline-block;
  margin: auto;
}

>>> .el-table__cell {
  padding: 0px 0 !important;
}

>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
