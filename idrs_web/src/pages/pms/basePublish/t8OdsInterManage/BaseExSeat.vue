<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="baseExSeat" v-model="searchParam" data-label-width="80px" data-target="baseExSeatList">
        <k-form-item label="包格式代码">
          <k-field-text v-model="searchParam.exfmtid" data-validate-type="text"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.portInfoData={}" slot="button"
            data-target="addBaseExSeatPopup" >
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>

        </div>
      </div>
      <k-grid ref="baseExSeatList" @data-row-select="selectRow"  data-operate-width="300px"  data-action="BaseExSeatModel.findBaseExSeats"
              @init="(id)=>{this.$kgrid = id}" :data-checkbox="false" data-checkbox-id="id" >
        <k-grid-column data-align="left" data-header="id" data-name="id" ></k-grid-column>
        <k-grid-column data-align="left" data-header="包格式代码" data-name="exfmtid" ></k-grid-column>
        <k-grid-column data-align="left" data-header="源机构代码"  data-name="fcode" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="包id" data-name="extpid" data-width="210" ></k-grid-column>
        <k-grid-column data-align="left" data-header="包模式" data-name="exmode" data-width="180"></k-grid-column>
        <k-grid-column data-header="包文件名模式 " data-name="fnmfmt" ></k-grid-column>
        <k-grid-column data-align="left" data-header="对应数据表名" data-name="extab" ></k-grid-column>
        <k-grid-column data-align="center" data-header="接口方向"   data-name="tcode"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改" data-functype="POPUP" data-size="mini"
                 :data-handler="selectRow" data-target="editBaseExSeatPopup">
            修改
          </k-btn>

          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="BaseExSeatModel.delBaseExSeat"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="baseExSeatList" data-descript="删除">
          	删除
    	    </k-btn>

          <k-btn class="btn-custom-text" data-descript="新增" data-functype="POPUP" data-size="small"
                           data-target="addPortFieldManageInfoPopup">
                      字段新增
         </k-btn>
<!--          <k-field-bswitch data-on-value="N" data-off-value="P" v-model="scope.row.row.status" data-on-action="T8PortInfoModel.recoverStatus"-->
<!--                           data-off-action="T8PortInfoModel.stopStatus" :data-params=scope.row.row :data-confirm="true" data-on-confirm-info="启用"-->
<!--                           data-off-confirm-info="停用" data-target="baseExSeatList"/>-->
        </template>
      </k-grid>
    </div>

    <div class="py-page-container">
      <k-grid ref="portFieldManageInfoGrid" @data-row-select="setExeidBool" data-operate-width="150px" data-action="BaseExFmtModel.findBaseExFmts" >

        <k-grid-column data-header="id " data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="包格式代码" data-name="exfmtid"></k-grid-column>
        <k-grid-column data-header="包数据项名称" data-name="itmnm"></k-grid-column>
        <k-grid-column data-header="字段长度" data-name="itmprc"></k-grid-column>
        <k-grid-column data-header="小数位数" data-name="itmscl"></k-grid-column>
        <k-grid-column data-header="数据项类型" data-name="itmtp"></k-grid-column>
        <k-grid-column data-header="数据项描述" data-name="itmdsc"></k-grid-column>
        <k-grid-column data-header="数据字典" data-name="itmdic"></k-grid-column>
        <k-grid-column data-header="分级数据项父节点" data-name="itmup"></k-grid-column>
        <k-grid-column data-header="系统字段代码" data-name="fld" ></k-grid-column>
        <k-grid-column data-header="顺序号" data-name="sn"  ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改接口文件字段信息" data-functype="POPUP" data-size="mini"
                 :data-handler="setExeidBool" data-target="editBaseExFmtPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="BaseExFmtModel.delBaseExFmt"
                 :data-confirm="true" data-size="mini" data-target="portFieldManageInfoGrid" data-descript="删除字段信息">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加接口文件字段信息弹出框   -->
    <k-popup ref="addPortFieldManageInfoPopup" data-title="字段新增">
      <k-form ref="addPortFieldManageInfoForm" :data-col="2">
         <k-form-item label="id" v-show="false">
           <k-field-text v-model="formData.id" data-validate-type="text" :disabled = "true"/>
         </k-form-item>

          <k-form-item label="包格式代码">
           <k-field-text v-model="formData.exfmtid" :data-max-length="10"  :data-allowblank="false" :data-disabled = "true"/>
         </k-form-item>

         <k-form-item label="包数据项名称">
           <k-field-text v-model="formData.itmnm" :data-max-length="512" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
         </k-form-item>

         <k-form-item label="字段长度">
           <k-field-text v-model="formData.itmprc" data-type="number" data-validate-type="number" :data-allowblank="false" :disabled = "false"/>
         </k-form-item>

         <k-form-item label="小数位数">
           <k-field-text v-model="formData.itmscl"  data-type="number" :data-validate-type="number"  :data-allowblank="false" :disabled = "false"/>
         </k-form-item>
         <k-form-item label="数据项类型">
           <k-field-text v-model="formData.itmtp" :data-max-length="3"    :data-allowblank="false" :disabled = "false" />
         </k-form-item>
         <k-form-item label="数据项描述" >
           <k-field-text v-model="formData.itmdsc" :data-max-length="80" :data-allowblank="false" :disabled = "false"/>
         </k-form-item>
         <k-form-item label="数据字典" >
           <k-field-text v-model="formData.itmdic" :data-max-length="30"   :data-allowblank="false" />
         </k-form-item>
         <k-form-item label="分级数据项父节点">
           <k-field-text v-model="formData.itmup" :data-max-length="60"  :disabled = "false"/>
         </k-form-item>

         <k-form-item label="系统字段代码"  >
           <k-field-text v-model="formData.fld"  :data-max-length="60" :data-allowblank="false" :disabled = "false"/>
         </k-form-item>

         <k-form-item label="顺序号"  >
           <k-field-text v-model="formData.sn"  data-type="number" data-validate-type="number"   :data-allowblank="false"/>
         </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseExFmtModel.addBaseExFmt" data-from="addPortFieldManageInfoForm"
                 :data-model="formData" data-target="portFieldManageInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改明显字段信息弹出框   -->
    <k-popup ref="editBaseExFmtPopup" data-title="修改明显字段">
      <k-form ref="editPortFieldManageInfoForm" :data-col="2">
          <k-form-item label="id" v-show="false">
                  <k-field-text v-model="formData.id" data-validate-type="text" :disabled = "true"/>
                </k-form-item>

                <k-form-item label="包格式代码">
                  <k-field-text v-model="formData.exfmtid" :data-max-length="10"  :data-allowblank="false" :data-disabled = "true"/>
                </k-form-item>

                <k-form-item label="包数据项名称">
                  <k-field-text v-model="formData.itmnm" :data-max-length="512" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
                </k-form-item>

                <k-form-item label="字段长度">
                  <k-field-text v-model="formData.itmprc" data-type="number" data-validate-type="number" :data-allowblank="false" :disabled = "false"/>
                </k-form-item>

                <k-form-item label="小数位数">
                  <k-field-text v-model="formData.itmscl"    data-validate-type="number"   :data-allowblank="false" :disabled = "false"/>
                </k-form-item>
                <k-form-item label="数据项类型">
                  <k-field-text v-model="formData.itmtp" :data-max-length="3"    :data-allowblank="false" :disabled = "false" />
                </k-form-item>
                <k-form-item label="数据项描述" >
                  <k-field-text v-model="formData.itmdsc" :data-max-length="80" :data-allowblank="false" :disabled = "false"/>
                </k-form-item>
                <k-form-item label="数据字典" >
                  <k-field-text v-model="formData.itmdic" :data-max-length="30"   :data-allowblank="false" />
                </k-form-item>
                <k-form-item label="分级数据项父节点">
                  <k-field-text v-model="formData.itmup" :data-max-length="60"  :disabled = "false"/>
                </k-form-item>

                <k-form-item label="系统字段代码"  >
                  <k-field-text v-model="formData.fld"  :data-max-length="60" :data-allowblank="false" :disabled = "false"/>
                </k-form-item>

                <k-form-item label="顺序号"  >
                  <k-field-text v-model="formData.sn"  data-type="number" data-validate-type="number"   :data-allowblank="false"/>
                </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseExFmtModel.upBaseExFmt" data-from="editPortFieldManageInfoForm"
                 :data-model="formData" data-target="portFieldManageInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    新增接口信息弹出框   -->
    <k-popup ref="addBaseExSeatPopup" data-title="新增" style="max-height: 100%;">
      <k-form ref="addInterfaceForm" :data-col="2" style="max-height: 100%;">
        <k-form-item label="包格式代码">
          <k-field-text v-model="portInfoData.exfmtid" :data-max-length="10"  :data-allowblank="false" :disabled = "false"/>
        </k-form-item>
        <k-form-item label="源机构代码">
          <k-field-text v-model="portInfoData.fcode" :data-max-length="20" data-dict="interface_type" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="包id">
          <k-field-text v-model="portInfoData.extpid" :data-max-length="3" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="包模式">
          <k-field-text v-model="portInfoData.exmode"  :data-max-length="1" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="包文件名模式">
          <k-field-text v-model="portInfoData.fnmfmt"  :data-max-length="50"  :data-allowblank="false" :disabled = "false"/>
        </k-form-item>

        <k-form-item label="对应数据表名">
          <k-field-text v-model="portInfoData.extab"  :data-max-length="100"  :data-allowblank="false" :disabled = "false" />
        </k-form-item>

        <k-form-item label="接口方向" >
          <k-field-text v-model="portInfoData.tcode" :data-max-length="6"  :data-allowblank="false" :disabled = "false"  />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="addBaseExSeatPopup" data-action="BaseExSeatModel.addBaseExSeat"
                 :data-model="portInfoData" data-target="baseExSeatList">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--    修改弹出框   -->
    <k-popup ref="editBaseExSeatPopup" data-title="修改" style="max-height: 100%;">
      <k-form ref="editPortInfoForm" :data-col="2" style="max-height: 100%;">
<k-form-item label="id" v-show="false">
                  <k-field-text v-model="portInfoData.id" data-validate-type="text" :data-disabled = "true"/>
                </k-form-item>
               <k-form-item label="源机构代码">
                <k-field-text v-model="portInfoData.fcode" :data-max-length="20" data-dict="interface_type" :data-allowblank="false" :disabled = "false"/>
              </k-form-item>

              <k-form-item label="包id">
                <k-field-text v-model="portInfoData.extpid" :data-max-length="3" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
              </k-form-item>
              <k-form-item label="包格式代码">
                      <k-field-text v-model="portInfoData.exfmtid" :data-max-length="10"  :data-allowblank="false" :disabled = "false"/>
               </k-form-item>
              <k-form-item label="包模式">
                <k-field-text v-model="portInfoData.exmode"  :data-max-length="1" data-validate-type="text" :data-allowblank="false" :disabled = "false"/>
              </k-form-item>

              <k-form-item label="包文件名模式">
                <k-field-text v-model="portInfoData.fnmfmt"  :data-max-length="50"  :data-allowblank="false" :disabled = "false"/>
              </k-form-item>

              <k-form-item label="对应数据表名">
                <k-field-text v-model="portInfoData.extab"  :data-max-length="100"  :data-allowblank="false" :disabled = "false" />
              </k-form-item>

              <k-form-item label="接口方向" >
                <k-field-text v-model="portInfoData.tcode" :data-max-length="6"  :data-allowblank="false" :disabled = "false"  />
              </k-form-item>


        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="editPortPopup" data-action="BaseExSeatModel.upBaseExSeat"
                 :data-model="portInfoData" data-target="baseExSeatList">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>








  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name:"baseExSeat",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        portInfoData:{},
        downloadInfoData:{},
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.formData = Object.assign({}, row);
        this.portInfoData = Object.assign({}, row);
        this.$refs.portFieldManageInfoGrid.load({exfmtid:row.exfmtid});
      },
      selectRow2(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.formData = Object.assign({}, row);
        this.portInfoData = Object.assign({}, row);
        this.$refs.portFieldManageInfoGrid.load({exfmtid:row.exfmtid});
      },
      setExeidBool(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },






     }
  };
</script>
<style lang="scss" scoped>

/deep/  .k-form-body {
  max-height: 100%;
}
</style>
