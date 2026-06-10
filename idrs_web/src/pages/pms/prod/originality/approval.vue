<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="queryParam">
      <k-form-item label="会议名称">
        <k-field-text v-model="queryParam.meetingName" data-action="T8ProdCreateMeeting.findAll2"
                      data-display-field="meetingName" data-value-field="meetingName"></k-field-text>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-select v-model="queryParam.t8ProdInfoId"
                        data-action="T8Dict.findTaProdInfos"
                        data-value-field="t8ProdInfoId"
                        data-display-field="prodCode,prodName">
        </k-field-select>
      </k-form-item>
      <k-form-item label="产品系列">
        <k-field-select v-model="queryParam.prodSeriesId" data-action="T8ProdSeries.findSeriesInfos"
                        data-display-field="seriesName" data-value-field="seriesCode"></k-field-select>
      </k-form-item>
      <k-form-item label="会议日期">
        <k-field-date v-model="queryParam.meetingDate" data-value-format="yyyy-MM-dd"></k-field-date>
      </k-form-item>
      <k-form-item label="创建人">
        <k-field-select v-model="queryParam.crtUserName" data-action="T8ProdCreateMeeting.getProdCreatMeetingCreUser"
                        data-display-field="crtUserName" data-value-field="crtUserName"></k-field-select>
      </k-form-item>
      <k-form-item label="更新人">
        <k-field-select v-model="queryParam.updUserName" data-action="T8ProdCreateMeeting.getProdCreatMeetingUpdUser"
                        data-display-field="updUserName" data-value-field="updUserName"></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
             data-target="editTable" v-show="showCreate"
             v-if="global.isShowAuthorityButton('T8ProdCreateMeeting.addProdCreateMeeting2')">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search-customize>


    <k-grid ref="prodInfoGrid" data-action="T8ProdCreateMeeting.findAll21" @data-row-select="selectRow"
            data-operate-column-position="end" data-align="center" data-operate-data-width="300px"
            data-operate-column="true" :dataAutoload="false">
      <k-grid-column data-header="会议名称" data-name="meetingName"/>
      <k-grid-column data-header="会议地点" data-name="meetingAddress"/>
      <k-grid-column data-header="会议日期时间" data-name="meetingDatetime"/>
      <k-grid-column data-header="会议状态" data-name="meetingStatus" data-dict="t8_meeting_state" data-hidden="true"/>
      <k-grid-column data-header="是否有会议结果" data-name="isMeetingResult" data-hidden="true"/>
      <k-grid-column data-header="产品系列id" data-name="prodSeriesId" data-hidden="true"/>
      <k-grid-column data-header="与会人" data-name="participant"/>
      <k-grid-column data-header="创建人" data-name="crtUserName"/>
      <k-grid-column data-header="创建时间" data-name="crtDate" data-type="date"/>
      <k-grid-column data-header="更新人" data-name="updUserName"/>
      <k-grid-column data-header="更新时间" data-name="updDate" data-type="date"/>
      <template slot="operate" slot-scope="scope">
        <!--  去除添加产品按钮控制  :data-disabled="scope.row.row.allowEdit=='0'?false:true"    -->
        <k-btn data-functype="POPUP" data-confirm data-size="mini"
               class="md-info md-just-icon md-simple" :data-handler="editHandler"
               data-target="editApprovalPopup" data-descript="修改会议信息" v-show="showUpdate"
               v-if="global.isShowAuthorityButton('T8ProdCreateMeeting.updateProdCreateMeeting2')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple"
               v-show="showUpload"
               data-target="addPopup" data-descript="上传会议附件资料"
               v-if="global.isShowAuthorityButton('T8ProdCreateMeeting.addDocumentAttachmentInfoList')">
          <md-icon>backup</md-icon>
        </k-btn>
        <!-- <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.meetingName+'会议材料.zip'" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               data-target="prodInfoGrid" data-url="/download/server/PmsApp/prod/meeting/downloadprodCreateMeeting.json" data-descript="下载会议附件资料">
          <md-icon>cloud_download</md-icon>
        </k-btn> -->
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple"
               data-target="editAttachmentTable" :data-handler="toParams"
               data-descript="管理会议附件信息">
          <md-icon>weekend</md-icon>
        </k-btn>
        <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除会议信息"
               v-show="showDelete"
               data-target="prodInfoGrid" data-action="T8ProdCreateMeeting.deleteProdCreateMeeting" data-confirm
               data-type="danger"
               :data-disabled="scope.row.row.allowEdit=='0'?false:true"
               v-if="global.isShowAuthorityButton('T8ProdCreateMeeting.deleteProdCreateMeeting')">
          <md-icon>close</md-icon>
        </k-btn>

      </template>
    </k-grid>


    <k-grid ref="meetProdInfoGrid" :data-autoload="false"
            data-action="T8ProdCreateMeetingProd.findT8ProdCreateMeetingProds">
      <k-grid-column data-align="center" data-header="id" data-name="id" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="会议名称" data-name="meetingName"/>
      <k-grid-column data-align="center" data-header="决议类型" data-name="t8DecisionType" data-dict="t8_decision_type"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="产品系列" data-name="t8ProdSeriesId"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="meetingStatus" data-dict="t8_prod_status"/>
      <k-grid-column data-align="center" data-header="会议结果" data-name="meetingResult" data-dict="t8_meeting_result"/>
      <k-grid-column data-align="center" data-header="产品子状态" data-name="prodSonStatus" data-dict="t8_prod_son_status"
                     data-hidden="true"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="上会通过" data-functype="POPUP" data-confirm
               data-type="danger"
               data-target="meetAdoptPopup" v-if="
               global.isShowAuthorityButton('T8ProdCreateMeetingProd.updateT8ProdCreateMeetingProdStatus')"
               :data-handler="updateStatuTG" v-show="showApproval">
          <md-icon>done</md-icon>
        </k-btn>

        <k-btn class="md-danger md-just-icon md-simple" data-descript="上会拒绝" data-functype="POPUP" data-confirm
               data-type="danger"
               data-target="meetRefusePopup" :data-handler="updateStatuJJ"
               v-if="
               global.isShowAuthorityButton('T8ProdCreateMeetingProd.updateT8ProdCreateMeetingProdStatus2')"
               v-show="showReject">
          <md-icon>close</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="未上会" data-functype="POPUP" data-confirm
               data-type="danger"
               data-target="NotMeetPopup"
               :data-handler="updateStatuNM" v-if="
               global.isShowAuthorityButton('T8ProdCreateMeetingProd.updateDownMeetingProdStatus')"
               v-show="showNotInMeet">
          <md-icon>undo</md-icon>
        </k-btn>

      </template>
    </k-grid>

    <k-popup ref="meetAdoptPopup" data-title="上会通过" :dataDialogDrag="true">
      <DisplayProdMeetConfirm v-model="formDataCon" :formDataCon="formDataCon" ref="meetAdoptForm"/>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="T8ProdCreateMeetingProd.updateT8ProdCreateMeetingProdStatus"
                 data-from="meetAdoptForm" :data-model="formDataCon" data-target="meetProdInfoGrid"
                 :data-after-success="onSubmitSuccess2">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="meetRefusePopup" data-title="上会拒绝" :dataDialogDrag="true">
      <DisplayProdMeetConfirm v-model="formDataCon" :formDataCon="formDataCon" ref="meetRefuseForm"
                              :data-after-success="onSubmitSuccess2"/>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="T8ProdCreateMeetingProd.updateT8ProdCreateMeetingProdStatus"
                 data-from="meetRefuseForm" :data-model="formDataCon" data-target="meetProdInfoGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="NotMeetPopup" data-title="未上会" :dataDialogDrag="true">
      <DisplayProdMeetConfirm v-model="formDataCon" :formDataCon="formDataCon" ref="NotMeetForm"/>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="T8ProdCreateMeetingProd.updateDownMeetingProdStatus"
                 data-from="NotMeetForm" :data-model="formDataCon" data-target="meetProdInfoGrid"
                 :data-after-success="onSubmitSuccess2">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="editTable" data-title="新增">
      <AddComp v-model="formData" @closeAddPopup="closeAddPopup" style="width: 1200px"
               :updSuccess="()=>{this.$refs.editTable.close();this.$refs.queryTable.load()}"/>
    </k-popup>
    <!--    修改会议弹出框   -->
    <k-popup ref="editApprovalPopup" data-title="修改">
      <div>
        <k-form ref="editApprovalForm" :data-col="2" data-total-width="888px">
          <k-form-item label="会议名称">
            <k-field-text v-model="formData.meetingName" :data-max-length="32" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="会议地点">
            <k-field-text v-model="formData.meetingAddress" :data-max-length="250" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="会议日期">
            <k-field-date v-model="formData.meetingDate" @data-on-change="change" data-type="date"
                          data-date-format="yyyy-MM-dd" data-value-format="yyyy-MM-dd"
                          :data-allowblank="false"></k-field-date>
          </k-form-item>
          <k-form-item label="会议时间">
            <k-field-time v-model="formData.meetingTime" @data-on-change="change" @data-on-focus="focus"
                          @data-on-blur="blur" data-value-format="HH" :data-allowblank="false"/>
          </k-form-item>
          <!--<k-form-item label="会议类型">
            <k-field-select v-model="formData.meetingType" data-dict="t8_meeting_type" data-default-value="2" data-disabled="true" :data-allowblank="false"  />
          </k-form-item>-->
          <!--        <k-form-item label="产品代码" data-input-width="600px">-->
          <!--          <k-field-select v-model="formData.t8ProdInfoId" data-action="T8Dict.findMeetProds"-->
          <!--                          :data-params="{prodSonStatus:'2,3',meetId:formData.id}"-->
          <!--                          data-display-field="prodCode,prodName" data-value-field="t8ProdInfoId"-->
          <!--                          :data-multiple="true" :data-allowblank="false"/>-->
          <!--        </k-form-item>-->
          <k-form-item label="与会人">
            <k-field-select v-model="formData.participant" data-multiple="true" data-action="User.findUsers"
                            data-value-field="username"
                            data-display-field="username" :data-allowblank="false" :data-max-length="255">
            </k-field-select>
          </k-form-item>
          <k-form ref="updateProdCreateMeetingForm2" v-for="(item, index) in prodMeetingItems" :key="index"
                  :data-col="2"
                  data-input-width="150px"
                  data-label-width="100px" data-total-width="1100px">
            <k-form-item label="会议类型">
              <k-field-select v-model="item.t8DecisionType" ref="t8DecisionType" data-dict="t8_decision_type"
                              :dataAllowblank='false' @data-on-change="selectProdCodes(item,index)"
                              :data-disabled="formData.isMeetingResult + 0 > 0?true:false"/>
            </k-form-item>
            <k-form-item label="产品代码" data-input-width="360px">
              <k-field-select v-model="item.t8ProdInfoId" data-action="T8Dict.findMeetProds"
                              data-display-field="prodCode,prodName"
                              data-value-field="t8ProdInfoId" :data-multiple="true"
                              :data-disabled="formData.isMeetingResult + 0 > 0?true:false || item.t8DecisionType == 3 ? true:false"/>
            </k-form-item>
            <k-form-item label="产品系列">
              <k-field-select v-model="item.t8ProdSeriesId" data-action="T8Dict.findSonSeriesInfos1"
                              data-display-field="seriesName" data-value-field="seriesCode" ref="t8ProdSeriesId"
                              @data-on-change="changeSeriesExplain1(item)"
                              :data-disabled="formData.isMeetingResult + 0 > 0?true:false || (item.t8DecisionType == 1 || item.t8DecisionType == 2 ? true:false)" />
            </k-form-item>
            <k-form-item label="系列说明"
                         v-if="item.t8ProdSeriesId != null && item.t8ProdSeriesId != undefined && item.t8ProdSeriesId !=''"
                         data-input-width="360px">
              <k-field-text v-model="item.seriesExplain" inputType="textarea" :rows="1"
                            :data-disabled="formData.isMeetingResult + 0 > 0?true:false || (item.t8DecisionType == 1 || item.t8DecisionType == 2 ? true:false)"/>
            </k-form-item>
            <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="新增"
                   v-show="formData.isMeetingResult + 0 > 0?false:true"
                   @click="() => prodMeetingItems.push({})">
              <md-icon>add</md-icon>
            </k-btn>
            <k-btn class="md-info md-just-icon md-simple" style="top: 15px" data-descript="删除当前行"
                   v-show="formData.isMeetingResult + 0 > 0?false:true"
                   @click="deleteEvent(index)">
              <md-icon md-src="/static/svg/delete.svg"/>
            </k-btn>
          </k-form>
          <k-form-footer data-align="center">

            <k-btn class="btn-custom-primary" ref="editBtn" data-functype="SUBMIT"
                   data-from="editApprovalForm" :data-model="formData" :data-handler="setUpdateParams"
                   data-target="prodInfoGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </k-form-footer>


        </k-form>
      </div>
    </k-popup>

    <k-popup ref="editAttachmentTable" title="管理附件列表" data-width="60%">
      <k-grid ref="editAttachmentGrid"
              data-action="DocumentAttachment.getCreateMeetingAttachmentInfos"
              @data-row-select="selectRow"
              :data-before-load="beforePopupLoad"
              data-operate-column-position="end"
              data-align="center" data-operate-data-width="300px"
              data-operate-column="true"
              :data-display="false">
        <k-grid-column data-align="center" data-header="id" data-name="id"/>
        <k-grid-column data-align="center" data-header="父级id" data-name="parentId" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="附件名称" data-name="fileName" :data-max-length="255"/>
        <k-grid-column data-align="center" data-header="附件类型" data-name="attachment_type" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"/>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.fileName" data-confirm data-size="mini"
                 class="md-info md-just-icon md-simple"
                 data-target="prodInfoGrid"
                 data-url="/download/server/PmsApp/documentCreatMeetingAttachment/downAttachment.json"
                 data-descript="下载会议附件资料">
            <md-icon>cloud_download</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-descript="删除附件" data-functype="SUBMIT" data-confirm
                 data-type="danger"
                 data-target="editAttachmentGrid"
                 data-action="T8ProdCreateMeeting.deleteFile"
                 v-if="global.isShowAuthorityButton('T8ProdCreateMeeting.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </k-popup>

    <!--  模板上传  -->
    <k-popup ref="addPopup" title="上传文档模板">
      <k-form ref="addForm" data-ui="element">
        <k-form-item style="display:none" label="id">
          <k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">

          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true" :data-limit=10
                          :data-error="onSubmitError" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>


        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn"
                 data-from="addForm" :data-model="uploadData" @click="submit">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>


</template>
<script>
    import Tools from '@/utils/tools.js';
    import {assign} from "lodash";
    import AddComp from "./approvaladd"
    import httpUtil from "../../../../frame/httpUtil";
    import DisplayProdMeetConfirm from "../../M81/prodDisplay/meetCreate/DisplayProdMeetConfirm.vue"

    export default {
        name: "approval",
        components: {
            AddComp,
            DisplayProdMeetConfirm,
        },
        data() {
            return {
                queryParam: {},
                prodRiskRat: {
                    prodInfoId: "",//产品ID
                    prodName: "",//产品名称
                    t8_risk_template_id: "",//模板ID
                    $RatGrid: null,//风险评分表格对象
                },
                attachments: {
                    parentId: "",//父级id
                },
                formData: {},
                formDataCon: {},
                selectRowData: {},
                expands: [],
                prodCard: [],
                prodCode: "",
                prodMode: "",
                fileData: '',
                unChange: false,
                uploadData: {
                    id: '',
                    meetName: ''
                },
                findValue: {
                    findProdCode: "",
                    findProdName: "",
                    findProdMode: "",
                    findProdLifecycle: "",
                },
                showSubmitBtn: true,
                queryParentId: '',
                showCreate: true,//是否显示新增按钮
                showUpdate: true,//是否显示修改会议按钮
                showUpload: true,//是否显示上传附件按钮
                showDelete: true,//是否显示删除会议按钮
                showApproval: true,//是否显示上会通过按钮
                showReject: true,//是否显示上会拒绝按钮
                showNotInMeet: true,//是否显示未上会按钮
                prodMeetingItems: [
                    {"t8ProdInfoId": ''},
                    {"t8DecisionType": ''},
                    {"t8ProdSeriesId": ''},
                    {"seriesExplain": ''}
                ],
                prodInfo: {},
            };
        },

        methods: {
            closeAddPopup(data) {
                this.$refs.editTable.close();
            },
            setUpdateParams: function (value) {
                let flag = false;
                let index = -1;
                for (let i = 0; i < this.prodMeetingItems.length; i++) {
                    if ((this.prodMeetingItems[i].t8ProdInfoId == null || this.prodMeetingItems[i].t8ProdInfoId == '' || this.prodMeetingItems[i].t8ProdInfoId == undefined) && (this.prodMeetingItems[i].t8ProdSeriesId == null || this.prodMeetingItems[i].t8ProdSeriesId == '' || this.prodMeetingItems[i].t8ProdSeriesId == undefined)) {
                        flag = true;
                        if (index = -1) {
                            index = i + 1;
                        }
                    }
                    if (!flag) {
                        flag = !this.$refs.updateProdCreateMeetingForm2[i].validate()
                    }
                }
                if (flag) {
                    this.$nextTick(() => {
                        this.$refs.editBtn.setIconStyle(1, []);
                        Tools.alert("第" + index + "行，请选择产品或者系列！", "danger");
                        return false;
                    });
                } else {

                    value["t8CreateRelations"] = JSON.stringify(this.prodMeetingItems);
                    this.httpUtil.comnUpdate({
                        action: 'T8ProdCreateMeeting.updateProdCreateMeeting2',
                        params: value,
                        successAlert: true,
                    }).then(data => {
                        this.$refs.editBtn.setIconStyle(1, [])
                        this.$refs.editApprovalPopup.close();
                    });
                }

                /*let a = val.t8ProdInfoId.split(",");
                let t8ProdCreateMeetingProds = [];
                if (a.length > 1) {
                    for (let i = 0; i < a.length; i++) {
                        let t8ProdCreateMeetingProd = {};
                        this.$set(t8ProdCreateMeetingProd, "t8ProdInfoId", a[i]);
                        t8ProdCreateMeetingProds.push(t8ProdCreateMeetingProd);
                    }
                } else {
                    let T8ProdCreateMeetingProd = {};
                    this.$set(T8ProdCreateMeetingProd, "t8ProdInfoId", val.t8ProdInfoId);
                    t8ProdCreateMeetingProds.push(T8ProdCreateMeetingProd);
                }
                val["t8ProdCreateMeetingProds"] = JSON.stringify(t8ProdCreateMeetingProds);*/
            },
            toParams: function (row) {
                //console.log("row=:",row)
                this.attachments.parentId = row.id;
                //console.log("this.attachments.parentId=:",this.attachments.parentId);
                this.queryParentId = this.attachments.parentId;
            },
            beforeDelete: function (row) {
                this.httpUtil.comnQuery({
                    action: 'T8ProdCreateMeetingProd.findT8ProdCreateMeetingProds',
                    params: {
                        t8CreateMeetingId: row.t8CreateMeetingId,
                        t8ProdInfoId: row.t8ProdInfoId,
                        t8SeriesId: row.t8SeriesId,
                    }
                }).then(data => {
                    this.formDataCon = data.rows[0];
                    this.$set(this.formDataCon, 'prodSonStatus', row.prodSonStatus);
                });
            },
            beforePopupLoad(params) {
                params.parentId = this.queryParentId;
                return params;
            },
            updateStatuNM: function (val) {
                this.formDataCon = {};
                val.prodSonStatus = '2';
                this.beforeDelete(val);
            },

            updateStatuTG: function (val) {
                this.formDataCon = {};
                val.prodSonStatus = '4';
                val.meetingResult = '2';
                this.beforeDeleteTG(val);
            },
            beforeDeleteTG: function (row) {
                this.httpUtil.comnQuery({
                    action: 'T8ProdCreateMeetingProd.findT8ProdCreateMeetingProds',
                    params: {
                        t8CreateMeetingId: row.t8CreateMeetingId,
                        t8ProdInfoId: row.t8ProdInfoId,
                        t8SeriesId: row.t8SeriesId,
                    }
                }).then(data => {
                    this.formDataCon = data.rows[0];
                    this.$set(this.formDataCon, 'prodSonStatus', row.prodSonStatus);
                    this.$set(this.formDataCon, 'meetingResult', '2');
                });
            },
            updateStatuJJ: function (val) {
                this.formDataCon = {};
                val.prodSonStatus = '5';
                val.meetingResult = '1';
                this.beforeDeleteJJ(val);
            },
            beforeDeleteJJ: function (row) {
                this.httpUtil.comnQuery({
                    action: 'T8ProdCreateMeetingProd.findT8ProdCreateMeetingProds',
                    params: {
                        t8CreateMeetingId: row.t8CreateMeetingId,
                        t8ProdInfoId: row.t8ProdInfoId,
                        t8SeriesId: row.t8SeriesId,
                    }
                }).then(data => {
                    this.formDataCon = data.rows[0];
                    this.$set(this.formDataCon, 'prodSonStatus', row.prodSonStatus);
                    this.$set(this.formDataCon, 'meetingResult', '1');
                });
            },
            change(val) {
                //console.log(val);
            },
            check(link) {
                if (link.length < 8) {
                    return '插入失败,链接最小长度为8'
                } else {
                    return true
                }
            },
            blur() {
                //console.log("失焦啦")
            },
            focus() {
                //console.log("聚焦啦")
            },
            selectProdMode(item) {

                let pathUrl = '';
                if (item.url == "" || item.url == null) {
                    pathUrl = '/main/pms/M81/M81001add';
                } else {
                    pathUrl = item.url;
                }

                this.$router.push({
                    path: pathUrl,
                    query: {
                        prodMode: item.prodMode,
                        findProdCode: '',
                        findProdName: '',
                        findProdMode: '',
                        findProdLifecycle: '',
                    },
                });

            },
            onSubmitError() {
                this.$refs.uploadRef.doReset();
                this.showSubmitBtn = true;
            },
            onSubmitSuccess() {
                this.$refs.uploadRef.doReset();
                this.$refs.addForm.reset();
                this.$refs.addPopup.close();
                //this.$refs.prodInfoGrid.load();
            },
            onSubmitSuccess2() {
                this.$refs.prodInfoGrid.load();
            },
            onUploadChange(file, fileList) {
                this.fileList = fileList;
            },
            submitUploadParam() {
                let formData = this.formData;
                this.$refs.uploadRef.upload(formData);
            },
            httpRequest(file) {
                this.fileData.append('files', file.file);
            },
            renderFinishDateTime(row) {
                //console.log(">>",row.updTime);
                let date = Tools.formatDateTime(row.updTime);
                if (date == "") {
                    return '-'
                }
                return date;
            },
            // 收取页面搜索框的值
            findData() {
                let valueFind = {};
                this.$refs.prodInfo.formList.map(item => {
                    if (item.field === 'meetingName' || item.field === 'meetingDate') {
                        valueFind[item.field] = item;
                    }
                });
                this.findValue.findMeetingName = valueFind.meetingName.data;
                this.findValue.findMeetingDate = valueFind.meetingDate.data;
            },

            // 往get请求传 搜索框存下的值
            findDataBye(params) {
                params.findProdCode = this.findValue.findProdCode;
                params.findProdName = this.findValue.findProdName;
                params.findProdMode = this.findValue.findProdMode;
                params.findProdLifecycle = this.findValue.findProdLifecycle;
                return params;
            },
            submit() {
                this.uploadData.id = this.formData.id;
                this.uploadData.meetName = this.formData.meetingName;
                let uploadData = this.uploadData;
                this.showSubmitBtn = false;
                this.fileData = new FormData();
                this.$refs.uploadRef.upload();
                this.fileData.append('params', JSON.stringify(uploadData));
                this.httpUtil.upload({
                    //url:"/upload-files/server/PmsApp/prodInfo/uploadT8ProdPayBack.json",
                    url: "/upload-files/server/PmsApp/meetingAttachment/upload.json",
                    formData: this.fileData
                }).then(res => {
                    this.showSubmitBtn = true;
                    Tools.alert(res.data.returnmsg)
                    this.onSubmitSuccess()
                })
            },
            selectRow(row, column, event) {
                const _this = this
                _this.selectRowData = assign({}, row)
                _this.formData = assign({}, row)
                this.$refs.meetProdInfoGrid.load({t8CreateMeetingId: _this.selectRowData.id});
            },
            selectRow2(row, column, event) {
                const _this = this
                _this.selectRowData = assign({}, row)
                _this.formData = assign({}, row)
                this.$refs.editAttachmentGrid.load({parentId: _this.selectRowData.id});
            },
            changeTool: function (index, rows) {

            },
            getProdMode: function () {
                /*this.httpUtil.comnQuery({
                  action: 'T8ProdModeInfo.findT8ProdModeInfos',
                  params: {}
                }).then(data => {
                  // console.log(data)
                  if (data.rows.length > 0) {
                    data.rows.forEach((e, index) => {
                      let bo = {prodMode: e.prodMode,
                        desc: e.prodModeName.length > 6 ? e.prodModeName.substring(0, 6) + '...' : e.prodModeName,
                        url: e.createUrl,
                        allDesc: e.prodModeName
                      }

                      this.prodCard.push(bo)

                      //console.log("this.prodCard.length: " + this.prodCard.length)
                    })
                  }
                });*/
            },
            dataRender(row) {
                let vol = row.vol;
                let sumVol = row.maxRaiseAmt;
                let volColor = '';
                if (vol == null) {
                    vol = 0;
                }

                if (vol == 0) {
                    volColor = "grey";
                } else if (vol * 3 <= sumVol) {
                    volColor = "green";
                } else if (vol * 2 <= sumVol) {
                    volColor = "blue";
                } else {
                    volColor = "red";
                }

                return `<p style="height: 10px;color: black">募集规模:${sumVol}</p><p style="height: 10px;color: ${volColor}">当前规模:${vol}</p>`;
            },
            editHandler(value) {
                this.formData.id = value.id;
                this.formData.isMeetingResult = value.isMeetingResult;
                this.httpUtil.comnQuery({
                    action: 'T8ProdCreateMeeting.findT8CreateRelationByMeetingId',
                    params: {
                        id: value.id,
                    }
                }).then(data => {
                    this.prodMeetingItems = data.rows;
                    value["t8CreateRelations"] = JSON.stringify(this.prodMeetingItems);
                });


            },
            deleteEvent(index) {
                if (this.prodMeetingItems.length > 1) {
                    this.prodMeetingItems.splice(index, 1);
                }
            },
            changeSeriesExplain1(value) {
                this.$set(value,'seriesExplain','');
            },
            //根据 决议类型 查询 产品代码
            selectProdCodes(value,index) {
                this.httpUtil.comnQuery({
                    action: "T8Dict.findMeetProds",
                    params: {
                        t8ProdInfoId:this.t8ProdInfoId,
                        t8DecisionType:value.t8DecisionType,
                    }
                }).then(data => {
                    this.prodInfo = data.rows;
                    this.$set(value, "t8ProdInfoId" ,'');
                    this.$set(value, "t8ProdSeriesId" ,'');
                    this.$set(value, "seriesExplain" ,'');
                });

            },
        },
        created() {
            this.global.getProdUser('');
            this.getProdMode();
            this.$nextTick(() => {
                this.$refs.prodInfoGrid.load({
                    prodCode: this.$route.query.findProdCode,
                    prodName: this.$route.query.findProdName,
                    prodLifecycle: this.$route.query.findProdLifecycle
                });
                //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
                this.global.getHideButtons(this);
            })
        }
    }
</script>

<style>
  .el-icon-color {
    color: #FF8C00;
  }
</style>
<style lang="scss" scoped>
  ::v-deep .dropdown-menu {
    margin-top: 10px;
    right: auto;
  }

  ::v-deep .k-card {
    z-index: 0;
  }

  .el-table__expanded-cell {
    background-color: #F9F9F9 !important;
  }

  .el-table__expanded-cell:hover {
    background-color: #F9F9F9 !important;
  }

  .tool {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: -15px;
    margin-bottom: -20px;
    padding-top: 60px;
  }

  .row-tools {
    background: #FFFFFF;
    box-shadow: 0 8px 12px 0 rgba(0, 0, 0, 0.06);
    border-radius: 24px;
    height: 45px;
    //display: inline-block;
    align-items: center;
    //margin-top: 20px;
    text-align: center;
    //margin-left: -690px;
    position: absolute;
    left: 0;
    margin-top: -45px;
    margin-left: 30px;
  }

  .tools-text {
    font-family: PingFangSC-Regular;
    font-size: 10px;
    letter-spacing: 0;
    /*font-weight: 500;*/
    /*    margin-top: 15px;*/
    color: #707E8F;
  }

  .tool-item {
    margin-left: 25px;
    margin-right: 25px;
    margin-top: -1px;
    float: left;
  }

  .step-tools {
    margin-top: 60px;
    margin-bottom: 20px;
    align-items: center;
    display: inline-block;
  }

  .tool-item .md-icon {
    width: 15px;
    height: 15px;
    margin-top: -1px;
    margin-bottom: 6px;
  }

  .test {
    width: 0;
    height: 0;
    border-top: 70px solid transparent;
    border-right: 140px solid #6bbf20;
    border-bottom: 70px solid transparent;
  }

  .steps {
    display: flex;
    flex-direction: row;
    align-items: center;
    overflow-x: auto;
  }

  .step {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-left: 2px;
    padding-right: 4px;
  }

  .my-line {
    background-image: linear-gradient(90deg, #7FC7FF 0%, #35A7EF 100%);
    border-radius: 0 0 0 0;
    width: 156px;
    height: 6px;
    margin-top: 12px;
  }

  .my-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: -12px;
  }

  .my-number-content {
    top: 0;
    left: -15px;
    text-align: center;
    display: inline-block;
    height: 18px;
    width: 18px;
    color: #ffffff;
    background-color: #b9b9b9;
    line-height: 18px;
    border-radius: 50%;
    text-align: center;
    /*  border:1px solid;*/
    background-color: #4CA7EE;

  }

  .my-number {
    font-family: Arial-BoldMT;
    font-size: 12px;
    color: #ffffff;
    letter-spacing: 0;
    z-index: 2;
    /*margin-top: -2px;*/
    font-weight: 500;
  }

  .my-title {
    font-weight: 500;
    color: #3B4858;
    margin-top: 4px;
    font-size: 14px;
    font-family: PingFangSC-Medium;
  }

  .my-desc {
    height: 24px;
    color: #999999;
    margin-top: 1px;
  }

  .last-step-content {
    display: flex;
    flex-direction: row;
  }

  .my-delta {
    height: 10px;
    width: 0px;
    position: absolute;
    margin-left: 155px;
    margin-top: 8px;
    border-bottom: 10px solid #4CA7EE;
    border-right: 16px solid transparent;
  }

  .back-line-content {
    margin-bottom: -18px;
    margin-left: -250px;
    margin-right: -450px;
  }

  .my-back-line {
    background: #EDEDED;
    border-radius: 0px 0px 0px 0px;
    width: 100%;
    height: 6px;
    margin-top: -25px;
  }

  .popover-container {
    display: flex;
    flex-direction: column;
    margin-left: 10px;
  }

  .template {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .template-desc {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .template-btn {
    display: flex;
    flex-direction: row;
    margin-left: 10px;
  }

  .module {
    border: 1px solid #41A0EB;
    margin-left: 10px;
    padding: 1px 15px;
    border-radius: 2px;
    color: #41A0EB;
  }

  .task {
    display: flex;
    flex-direction: row;
    margin-top: 20px;
  }

  .task-item {

    display: inline-block;
    max-width: 420px;
    border-radius: 2px;
    border-radius: 2px;
    //width: 100%;
    height: 203px;
    //margin: 0 auto;
    margin: 0 5px 0 5px;
    text-align: left;
  }

  .task-box {
    margin-left: 7.5px;
    margin-right: 7.5px;
    width: 89.4px;
    height: 23px;
    position: relative;
    display: inline-block;
    // display: flex;
    // flex-direction: column;
    // height: 200px;
    //box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.14);
    border-radius: 2px;
    text-align: center;
    line-height: 25px;
    margin-top: 10px;
    background-repeat: no-repeat;
  }

  .task-desc {
    font-family: PingFangSC-Regular;
    font-size: 12px;
    color: #FFFFFF;
  }

  .el-popover .el-popper {
    top: 180px;
  }

  .tool-item:hover {
    cursor: pointer;
  }

  .tool-item:hover ::v-deep .md-icon svg > path {
    fill: #41A0EB;
  }

  .tool-item:hover span {
    color: #41A0EB;
  }

  .tool-disable,
  .tool-disable:hover {
    cursor: default;
  }

  .tool-disable ::v-deep .md-icon svg > path,
  .tool-disable:hover ::v-deep .md-icon svg > path {
    fill: #cccccc;
  }

  .tool-disable span,
  .tool-disable:hover span {
    color: #cccccc;
  }

  .prodModeCursor :hover {
    cursor: pointer;
  }

  .el-icon-circle-plus:before {
    margin-left: -15px;
    padding-right: 10px;
    //margin-top: 10px;
    padding-top: 3px;
  }

</style>
