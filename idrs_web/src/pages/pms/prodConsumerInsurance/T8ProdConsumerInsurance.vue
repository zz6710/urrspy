<template>
  <div>
    <div>
      <k-form-search-customize data-target="t8ProdConsumerInsuranceGrid" v-model="prodSearchParam">

        <k-form-item label="产品代码">
          <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="initFormData"
               data-target="addT8ProdConsumerInsurancePopup" v-show="showCreate"
               v-if="global.isShowAuthorityButton('T8ProdConsumerInsurance.addT8ProdConsumerInsurance')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
        <k-btn slot="button" class="btn-custom-primary" style="width: 100px" data-functype="POPUP"
               data-target="uploadPopup"
               v-if="global.isShowAuthorityButton('PrintTemp.savePrintTempInfo')">
          <md-icon md-src="/static/svg/add.svg"/>
          上传产品消保
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="t8ProdConsumerInsuranceGrid" @data-row-select="selectRow"
              data-action="T8ProdConsumerInsurance.findT8ProdConsumerInsurances1">
        <k-grid-column data-header="id" data-name="id" :data-hidden="true"/>
        <k-grid-column data-header="产品代码" data-name="prodCode"/>
        <k-grid-column data-header="产品名称" data-name="prodName" data-width="300"/>
        <k-grid-column data-hidden="true" data-header="填报日期" data-name="filledDate" data-type="date"/>
        <k-grid-column data-hidden="true" data-header="填报部门" data-name="filledDeptName"/>
        <k-grid-column data-hidden="true" data-header="填报人" data-name="filledUserName"/>
        <k-grid-column data-hidden="true" data-header="审核人部门" data-name="approvalDeptName"/>
        <k-grid-column data-hidden="true" data-header="审核人" data-name="approvalUserName"/>
        <k-grid-column data-header="是否为上传文件" data-name="isAddUpload" data-dict="1yes0no"/>
        <k-grid-column data-hidden="true" data-header="是否提示收益区间" data-name="isIncomeRange"/>
        <k-grid-column data-hidden="true" data-header="是否冷静期" data-name="isCoolingOff"/>
        <k-grid-column data-hidden="true" data-header="关键词提示及详细解释" data-name="keyWords"/>
        <k-grid-column data-hidden="true" data-header="特征/属性" data-name="featuresAttributes"/>
        <k-grid-column data-hidden="true" data-header="风险状况及对应客户风险承受能力" data-name="riskLevel"/>
        <k-grid-column data-hidden="true" data-header="风险提示" data-name="riskStatement"/>
        <k-grid-column data-hidden="true" data-header="收益分配" data-name="incomeDistribution"/>
        <k-grid-column data-hidden="true" data-header="后续争议解决途径" data-name="disputeResolution"/>
        <k-grid-column data-hidden="true" data-header="减轻或免除自身责任条款/限制或排除消费者主要权利的条款" data-name="liabilityClause"/>
        <k-grid-column data-hidden="true" data-header="消费者个人金融信息安全保护条款" data-name="protectionClause"/>
        <k-grid-column data-hidden="true" data-header="融资单位和项目名称" data-name="financeName"/>
        <k-grid-column data-hidden="true" data-header="期限规模" data-name="termScale"/>
        <k-grid-column data-hidden="true" data-header="交易结构" data-name="transactionStructure"/>
        <k-grid-column data-hidden="true" data-header="到期收益分配" data-name="yieldOfMaturity"/>
        <k-grid-column data-hidden="true" data-header="测算依据和测算方式" data-name="guessAndEstimate"/>
        <k-grid-column data-hidden="true" data-header="允许消费者在有效时段对购买行为做出反悔决定" data-name="isGoBack"/>
        <k-grid-column data-hidden="true" data-header="收费标准" data-name="chargingStandard"/>
        <k-grid-column data-hidden="true" data-header="主要依据" data-name="mainBasis"/>
        <k-grid-column data-hidden="true" data-header="成立条件" data-name="establishMethod"/>
        <k-grid-column data-hidden="true" data-header="收取方式" data-name="collectionMethod"/>
        <k-grid-column data-hidden="true" data-header="可能采取的优惠" data-name="possibleBenefits"/>
        <k-grid-column data-hidden="true" data-header="审批状态" data-name="approvalStatus"/>
        <k-grid-column data-header="创建日期" data-name="createDate" data-render="renderDateTimeCreate"/>
        <k-grid-column data-header="更新日期" data-name="updateDate" data-render="renderDateTimeUpdate"/>
        <k-grid-column data-header="创建人名称" data-name="createUserName"/>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品消保审核信息表" data-functype="POPUP"
                 data-size="mini" @click="getUserId(scope.row.row)" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('T8ProdConsumerInsurance.updateT8ProdConsumerInsurance')"
                 v-show="showUpdate">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('T8ProdConsumerInsurance.deleteT8ProdConsumerInsurance')"
                 data-action="T8ProdConsumerInsurance.deleteT8ProdConsumerInsurance" data-size="mini"
                 data-type="danger" data-target="t8ProdConsumerInsuranceGrid" :data-confirm="true"
                 data-descript="删除产品消保审核信息表"
                 v-show="showDelete">
            <md-icon>close</md-icon>
          </k-btn>
<!--          <k-btn class="md-info md-just-icon md-simple" data-descript="下载产品消保审核信息表" data-functype="DOWNLOAD"
                 data-size="mini" :data-download-name="scope.row.row.prodName+'消保审核意见表.doc'" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
                 data-url="/download/server/PmsApp/prod/downloadProdConsumerInsurance.json" v-model="scope.row.row">
            <md-icon>cloud_download</md-icon>
          </k-btn>-->
<!--          <k-btn class="md-info md-just-icon md-simple" data-descript="生成消保审核信息表" data-functype="SUBMIT" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
                 data-size="mini" data-action="T8ProdDocumentVersion.generateEscrowAgreementByProdCode3" v-model="scope.row.row">
            <md-icon>cloud_download</md-icon>
          </k-btn>-->
          <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.confirmStatus=='0'" :data-download-name="changeName(scope.row.row)"
                 data-descript="下载消保审核信息表" data-functype="DOWNLOAD" data-size="small" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
                 data-url="/download/server/PmsApp/prodDocument/downloadOnlineEditT8ProdDocumentVersion2.json" v-model="scope.row.row"
                 v-show="showDownload">
            <md-icon>cloud_download</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="uploadPopup" data-title="上传">
      <k-form dataInputWidth="300px" ref="uploadT8ProdConsumerInsuranceForm" :data-col="2">
        <k-form-item label="产品代码" :data-col="2">
          <k-field-select v-model="form.prodCode" data-action="T8Dict.findEstablishProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          @data-on-change="getProdNameByCode2"
                          :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="产品名称" v-show="false">
          <k-field-text v-model="form.prodName" :data-disabled="true"
                        :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/T8ProdConsumerInsuranceAction.json">
          </k-field-upload>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="t8ProdConsumerInsuranceGrid" ref="fileSubmitBtn"
                 data-from="uploadT8ProdConsumerInsuranceForm" :data-model="form" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--    添加产品消保审核信息表弹出框   -->
    <k-popup ref="addT8ProdConsumerInsurancePopup" data-title="新增" >
      <k-form dataInputWidth="300px" ref="addT8ProdConsumerInsuranceForm" :data-col="2">
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel"  >

            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="基本信息"/>
            </div>
            <k-form ref="infoForm" data-input-width="240px">
            <k-form-item label="产品代码">
              <k-field-select v-model="formData.prodCode" data-action="T8Dict.findEstablishProdInfos"
                              data-display-field="prodCode,prodName" data-value-field="prodCode"
                              @data-on-change="getProdNameByCode1"
                              :dataAllowblank="false"/>
            </k-form-item>
            <k-form-item label="产品名称">
              <k-field-text v-model="formData.prodName" :data-disabled="true"
                            :dataAllowblank="false"/>
            </k-form-item>
            <k-form-item label="填报日期">
              <k-field-date v-model="formData.filledDate"
                            :data-max-length="8"
                            :data-allowblank="false"/>
            </k-form-item>
            <k-form-item label="填报部门">
              <k-field-cascader style="width:100%" v-model="formData.filledDept" data-diffcondition="deptno,parentdeptno" @data-on-change="getFilledDept1"
                                :data-graphql="querydeptGraphql" data-display-child="children" data-check-strictly data-show-num
                                :data-props="{ expandTrigger: 'hover'}" data-size="medium" data-placeholder="请选择所属部门" data-clearable
                                data-fileterable data-display-field="deptname" data-value-field="deptno" :data-allowblank="false">
              </k-field-cascader>
            </k-form-item>
            <k-form-item label="填报人">
              <k-field-select v-model="formData.filledUser"   data-display-field="username" :dataAllowblank="false"
                              :data-data="filledUserData" data-value-field="userid"  />
            </k-form-item>
            <k-form-item label="审核人部门">
              <k-field-cascader style="width:100%" v-model="formData.approvalDept" data-diffcondition="deptno,parentdeptno" @data-on-change="getApprovalDept1"
                                :data-graphql="querydeptGraphql" data-display-child="children" data-check-strictly data-show-num
                                :data-props="{ expandTrigger: 'hover'}" data-size="medium" data-placeholder="请选择所属部门" data-clearable
                                data-fileterable data-display-field="deptname" data-value-field="deptno" :data-allowblank="false">
              </k-field-cascader>
            </k-form-item>
            <k-form-item label="审核人">
              <k-field-select v-model="formData.approvalUser"   data-display-field="username" :data-allowblank="false"
                              :data-data="approvalUserData" data-value-field="userid"  />
            </k-form-item>
            </k-form>
          </div>
        </div>

        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" ges>
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="审批要素"/>
            </div>
            <k-form ref="elementForm" dataLabelWidth="240px">
            <k-form-item label="关键词提示及详细解释" data-input-width="550px">
              <k-field-text :data-allowblank="false" v-model="formData.keyWords" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="内容摘要"/>
            </div>
            <k-form ref="abstractForm" dataLabelWidth="240px">
            <k-form-item label="特征/属性" data-input-width="550x">
              <k-field-text :dataAllowblank="false" v-model="formData.featuresAttributes" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="风险状况及对应客户风险承受能力" data-input-width="550px">
              <k-field-text :dataAllowblank="false" v-model="formData.riskLevel" inputType="textarea" :data-max-length="500" :rows="1"/>
            </k-form-item>
            <k-form-item label="风险提示" data-input-width="550px">
              <k-field-text :dataAllowblank="false" v-model="formData.riskStatement" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="收益分配" data-input-width="550px">
              <k-field-text :dataAllowblank="false" v-model="formData.incomeDistribution" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="后续争议解决途径" data-input-width="550px">
              <k-field-text :dataAllowblank="false" v-model="formData.disputeResolution"inputType="textarea" :data-max-length="500" :rows="1"/>
            </k-form-item>
            <k-form-item label="减轻或免除自身责任条款/限制或排除消费者主要权利的条款" data-input-width="550px">
              <k-field-text :dataAllowblank="false" v-model="formData.liabilityClause" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="消费者个人金融信息安全保护条款"/>
            </div>
            <k-form ref="protectionForm" dataLabelWidth="240px">
            <k-form-item label="消费者个人金融信息安全保护条款" data-input-width="550px">
              <k-field-text :dataAllowblank="false" v-model="formData.protectionClause" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="是否提示收益区间"/>
            </div>
            <k-form ref="profitForm" dataLabelWidth="240px">
            <k-form-item label="是否提示收益区间">
<!--              <k-field-radio  v-model="formData.isIncomeRange" :data-data="options"/>-->
              <k-field-radio v-model="formData.radioValue2" :data-data="options1" :data-default-value="'1'" data-value-field="value" data-display-field="username" @data-on-change="change2" />
            </k-form-item>
            <k-form-item label="融资单位和项目名称" data-input-width="550px">
              <k-field-text  v-model="formData.financeName" inputType="textarea" :data-max-length="500" :rows="1"/>
            </k-form-item>
            <k-form-item label="期限规模" data-input-width="550px">
              <k-field-text   v-model="formData.termScale" inputType="textarea" :data-max-length="500" :rows="1"/>
            </k-form-item>
            <k-form-item label="交易结构" data-input-width="550px">
              <k-field-text v-model="formData.transactionStructure" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="到期收益分配" data-input-width="550px">
              <k-field-text   v-model="formData.yieldOfMaturity" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="测算依据和测算方式" data-input-width="550px">
              <k-field-text  v-model="formData.guessAndEstimate" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="是否冷静期"/>
            </div>
            <k-form ref="coolingPeriodForm" dataLabelWidth="240px">
            <k-form-item label="是否设置冷静期或募集期" data-input-width="550px">
<!--              <k-field-radio  v-model="formData.isCoolingOff" :data-data="options"/>{{ formData.isCoolingOff }}-->
              <k-field-radio v-model="formData.radioValue" :data-data="options1" :data-default-value="'1'" data-value-field="value" data-display-field="username" @data-on-change="change" />
            </k-form-item>
            <k-form-item label="冷静期或募集期说明" v-show="formData.radioValue=='1'" data-input-width="550px">
              <k-field-text  v-model="formData.coolingOffDesc" :dataAllowblank="formData.radioValue=='0'" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="允许消费者在有效时段对购买行为做出反悔决定"/>
            </div>
            <k-form ref="estoppelDecisionForm" dataLabelWidth="310px">
            <k-form-item label="允许消费者在有效时段对购买行为做出反悔决定" data-input-width="485px">
              <k-field-text  v-model="formData.isGoBack" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="收费"/>
            </div>
            <k-form ref="chargeForm" dataLabelWidth="230px">
            <k-form-item label="收费标准" data-input-width="550px">
              <k-field-text  v-model="formData.chargingStandard":data-max-length="500"  inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="主要依据" data-input-width="550px">
              <k-field-text  v-model="formData.mainBasis" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="成立条件" data-input-width="550px">
              <k-field-text  v-model="formData.establishMethod" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="收取方式" data-input-width="550px">
              <k-field-text  v-model="formData.collectionMethod" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            <k-form-item label="可能采取的优惠" data-input-width="550px">
              <k-field-text  v-model="formData.possibleBenefits" :data-max-length="500" inputType="textarea" :rows="1"/>
            </k-form-item>
            </k-form>

          </div>
        </div>


        <k-form-footer data-align="center">
          <k-btn ref="addSubmitBtn" class="btn-custom-primary" data-functype="SUBMIT"
                 :data-handler="updateDistributorInfo"
                 data-from="addT8ProdConsumerInsuranceForm"
                 :data-model="formData" data-target="t8ProdConsumerInsuranceGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>





    <!--    修改上传产品消保审核信息表弹出框   -->
    <k-popup ref="editT8ProdConsumerInsurancePopup" data-title="修改">
      <k-form ref="editT8ProdConsumerInsuranceForm" dataInputWidth="300px" :data-col="2">
            <k-form ref="editUploadForm" dataInputWidth="240px">
            <k-form-item label="id" v-if="false">
              <k-field-text v-model="editFormData.id" />
            </k-form-item>
              <k-form-item label="产品代码" :data-col="2">
                <k-field-select v-model="editFormData.prodCode" data-action="T8Dict.findEstablishProdInfos"
                                data-display-field="prodCode,prodName" data-value-field="prodCode"
                                @data-on-change="getProdNameByCode3"
                                :dataAllowblank="false"/>
              </k-form-item>
              <k-form-item label="产品名称" v-show="false">
                <k-field-text v-model="editFormData.prodName" :data-disabled="true"
                              :dataAllowblank="false"/>
              </k-form-item>
            </k-form>
            <k-form-item label="附件" data-ui="element" data-input-width="500px">
                <k-field-upload data-type="file" ref="editUploadRef" :data-multiple="false" :data-limit=1
                                :data-error="onEditSubmitError" :data-success="onEditSubmitSuccess"
                                :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/T8ProdConsumerInsuranceAction.json">
                </k-field-upload>
              </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="t8ProdConsumerInsuranceGrid" ref="editSubmitBtn"
                 data-from="editT8ProdConsumerInsuranceForm" :data-model="editFormData" :data-handler="submitEditUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>





    <!--    修改新增产品消保审核信息表弹出框   -->
    <k-popup ref="editAddT8ProdConsumerInsurancePopup" data-title="修改" >
      <k-form dataInputWidth="300px" ref="editAddT8ProdConsumerInsuranceForm" :data-col="2">
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel"  >

            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="基本信息"/>
            </div>
            <k-form ref="editAddInfoForm" data-input-width="240px">
              <k-form-item label="产品代码">
                <k-field-select v-model="editAddData.prodCode" data-action="T8Dict.findEstablishProdInfos"
                                data-display-field="prodCode,prodName" data-value-field="prodCode"
                                @data-on-change="getProdNameByCode1"
                                :dataAllowblank="false"/>
              </k-form-item>
              <k-form-item label="产品名称">
                <k-field-text v-model="editAddData.prodName" :data-disabled="true"
                              :dataAllowblank="false"/>
              </k-form-item>
              <k-form-item label="填报日期">
                <k-field-date v-model="editAddData.filledDate"
                              :data-max-length="8"
                              :data-allowblank="false"/>
              </k-form-item>
              <k-form-item label="填报部门">
                <k-field-cascader style="width:100%" v-model="editAddData.filledDept" data-diffcondition="deptno,parentdeptno" @data-on-change="getFilledDept4"
                                  :data-graphql="querydeptGraphql" data-display-child="children" data-check-strictly data-show-num
                                  :data-props="{ expandTrigger: 'hover'}" data-size="medium" data-placeholder="请选择所属部门" data-clearable
                                  data-fileterable data-display-field="deptname" data-value-field="deptno" :data-allowblank="false">
                </k-field-cascader>
              </k-form-item>
              <k-form-item label="填报人">
                <k-field-select v-model="editAddData.filledUser"   data-display-field="username" :dataAllowblank="false"
                                :data-data="editAddFilledUserData" data-value-field="userid"  />
              </k-form-item>
              <k-form-item label="审核人部门">
                <k-field-cascader style="width:100%" v-model="editAddData.approvalDept" data-diffcondition="deptno,parentdeptno" @data-on-change="getApprovalDept4"
                                  :data-graphql="querydeptGraphql" data-display-child="children" data-check-strictly data-show-num
                                  :data-props="{ expandTrigger: 'hover'}" data-size="medium" data-placeholder="请选择所属部门" data-clearable
                                  data-fileterable data-display-field="deptname" data-value-field="deptno" :data-allowblank="false">
                </k-field-cascader>
              </k-form-item>
              <k-form-item label="审核人">
                <k-field-select v-model="editAddData.approvalUser"   data-display-field="username" :data-allowblank="false"
                                :data-data="editAddApprovalUserData" data-value-field="userid"  />
              </k-form-item>
            </k-form>
          </div>
        </div>

        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" ges>
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="审批要素"/>
            </div>
            <k-form ref="editAddElementForm" dataLabelWidth="240px">
              <k-form-item label="关键词提示及详细解释" data-input-width="550px">
                <k-field-text :data-allowblank="false" v-model="editAddData.keyWords" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="内容摘要"/>
            </div>
            <k-form ref="editAddAbstractForm" dataLabelWidth="240px">
              <k-form-item label="特征/属性" data-input-width="550x">
                <k-field-text :dataAllowblank="false" v-model="editAddData.featuresAttributes" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="风险状况及对应客户风险承受能力" data-input-width="550px">
                <k-field-text :dataAllowblank="false" v-model="editAddData.riskLevel" inputType="textarea" :data-max-length="500" :rows="1"/>
              </k-form-item>
              <k-form-item label="风险提示" data-input-width="550px">
                <k-field-text :dataAllowblank="false" v-model="editAddData.riskStatement" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="收益分配" data-input-width="550px">
                <k-field-text :dataAllowblank="false" v-model="editAddData.incomeDistribution" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="后续争议解决途径" data-input-width="550px">
                <k-field-text :dataAllowblank="false" v-model="editAddData.disputeResolution"inputType="textarea" :data-max-length="500" :rows="1"/>
              </k-form-item>
              <k-form-item label="减轻或免除自身责任条款/限制或排除消费者主要权利的条款" data-input-width="550px">
                <k-field-text :dataAllowblank="false" v-model="editAddData.liabilityClause" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="消费者个人金融信息安全保护条款"/>
            </div>
            <k-form ref="editAddProtectionForm" dataLabelWidth="240px">
              <k-form-item label="消费者个人金融信息安全保护条款" data-input-width="550px">
                <k-field-text :dataAllowblank="false" v-model="editAddData.protectionClause" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="是否提示收益区间"/>
            </div>
            <k-form ref="editAddProfitForm" dataLabelWidth="240px">
              <k-form-item label="是否提示收益区间">
                <!--              <k-field-radio  v-model="formData.isIncomeRange" :data-data="options"/>-->
                <k-field-radio v-model="editAddData.radioValue2" :data-data="options1" :data-default-value="'1'" data-value-field="value" data-display-field="username" @data-on-change="change2" />
              </k-form-item>
              <k-form-item label="融资单位和项目名称" data-input-width="550px">
                <k-field-text  v-model="editAddData.financeName" inputType="textarea" :data-max-length="500" :rows="1"/>
              </k-form-item>
              <k-form-item label="期限规模" data-input-width="550px">
                <k-field-text   v-model="editAddData.termScale" inputType="textarea" :data-max-length="500" :rows="1"/>
              </k-form-item>
              <k-form-item label="交易结构" data-input-width="550px">
                <k-field-text v-model="editAddData.transactionStructure" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="到期收益分配" data-input-width="550px">
                <k-field-text   v-model="editAddData.yieldOfMaturity" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="测算依据和测算方式" data-input-width="550px">
                <k-field-text  v-model="editAddData.guessAndEstimate" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="是否冷静期"/>
            </div>
            <k-form ref="editCoolingPeriodForm" dataLabelWidth="240px">
              <k-form-item label="是否设置冷静期或募集期" data-input-width="550px">
                <!--              <k-field-radio  v-model="editAddData.isCoolingOff" :data-data="options"/>{{ editAddData.isCoolingOff }}-->
                <k-field-radio v-model="editAddData.radioValue" :data-data="options1" :data-default-value="'1'" data-value-field="value" data-display-field="username" @data-on-change="change" />
              </k-form-item>
              <k-form-item label="冷静期或募集期说明" v-show="editAddData.radioValue=='1'" data-input-width="550px">
                <k-field-text  v-model="editAddData.coolingOffDesc" :dataAllowblank="editAddData.radioValue=='0'" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="允许消费者在有效时段对购买行为做出反悔决定"/>
            </div>
            <k-form ref="editEstoppelDecisionForm" dataLabelWidth="310px">
              <k-form-item label="允许消费者在有效时段对购买行为做出反悔决定" data-input-width="485px">
                <k-field-text  v-model="editAddData.isGoBack" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
            </k-form>
          </div>
        </div>
        <div style="padding-top: 2px;">
          <div class="form-item prod-panel" >
            <div class="title" >
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="收费"/>
            </div>
            <k-form ref="editAddChargeForm" dataLabelWidth="230px">
              <k-form-item label="收费标准" data-input-width="550px">
                <k-field-text  v-model="editAddData.chargingStandard":data-max-length="500"  inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="主要依据" data-input-width="550px">
                <k-field-text  v-model="editAddData.mainBasis" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="成立条件" data-input-width="550px">
                <k-field-text  v-model="editAddData.establishMethod" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="收取方式" data-input-width="550px">
                <k-field-text  v-model="editAddData.collectionMethod" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
              <k-form-item label="可能采取的优惠" data-input-width="550px">
                <k-field-text  v-model="editAddData.possibleBenefits" :data-max-length="500" inputType="textarea" :rows="1"/>
              </k-form-item>
            </k-form>

          </div>
        </div>


        <k-form-footer data-align="center">
          <k-btn ref="editAddSubmitBtn" class="btn-custom-primary" data-functype="SUBMIT"
                 :data-handler="editAddSubmit"
                 data-from="editAddT8ProdConsumerInsuranceForm"
                 :data-model="editAddData" data-target="t8ProdConsumerInsuranceGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";

  export default {
   name: "T8ProdConsumerInsurance",
    data() {
      return {
        formData: {
          prodCode:'',
          prodName:'',
          filledDate:'',
          filledDept:'',
          filledUser:'',
          approvalDept:'',
          approvalUser:'',
          isIncomeRange:'',
          isCoolingOff:'',
          coolingOffDesc:'',
          keyWords:'',
          featuresAttributes:'',
          riskLevel:'',
          riskStatement:'',
          incomeDistribution:'',
          disputeResolution:'',
          liabilityClause:'',
          protectionClause:'',
          financeName:'',
          termScale:'',
          transactionStructure:'',
          yieldOfMaturity:'',
          guessAndEstimate:'',
          isGoBack:'',
          chargingStandard:'',
          mainBasis:'',
          establishMethod:'',
          collectionMethod:'',
          possibleBenefits:''
        },
        form:{
          prodCode:'',
          prodName:'',
          filledDate:'',
          filledDept:'',
          filledUser:'',
          approvalDept:'',
          approvalUser:'',
        },
        editFormData:{
          prodCode:'',
          prodName:'',
          filledDate:'',
          filledDept:'',
          filledUser:'',
          approvalDept:'',
          filledUserName:'',
          approvalUser:'',
          approvalUserName:'',
        },
        editAddData: {
          prodCode:'',
          prodName:'',
          filledDate:'',
          filledDept:'',
          filledUser:'',
          filledUserName:'',
          approvalDept:'',
          approvalUser:'',
          approvalUserName:'',
          isIncomeRange:'',
          isCoolingOff:'',
          coolingOffDesc:'',
          keyWords:'',
          featuresAttributes:'',
          riskLevel:'',
          riskStatement:'',
          incomeDistribution:'',
          disputeResolution:'',
          liabilityClause:'',
          protectionClause:'',
          financeName:'',
          termScale:'',
          transactionStructure:'',
          yieldOfMaturity:'',
          guessAndEstimate:'',
          isGoBack:'',
          chargingStandard:'',
          mainBasis:'',
          establishMethod:'',
          collectionMethod:'',
          possibleBenefits:''
        },
        selectRowData: {},
        prodSearchParam:{
          prodCode:''
        },
        editAddFilledUserData:{},
        editAddApprovalUserData:{},
        filledUserData:{},
        formUserData:{},
        approvalUserData:{},
        formApprovalUserData:{},
        editFilledUserData:{},
        editApprovalUserData:{},
        options:[{
            value: '1',
            label: '是',
          }, {
            value: '0',
            label: '否'
          }],
        options1: [{
          value: '1',
          username: '是',
        }, {
          value: '0',
          username: '否',
        }],
        showCreate:true,//是否显示新增按钮
        showUpdate:true,//是否显示修改按钮
        showDelete:true,//是否显示删除按钮
        showDownload:true,//是否显示下载按钮
      };
    },
    computed: {
      querydeptGraphql() {
        return "{queryDept(action:\"find\") {rows{deptno, deptname, parentdeptno, deptid},results}}"
      }
    },
    created() {
      this.global.getProdUser('');
      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
        let prodCode = this.$route.query.prod_code;
        if(prodCode !=''&&prodCode!=undefined){
          this.$refs.t8ProdConsumerInsuranceGrid.load({prodCode:prodCode});
        }
      });
    },
    methods: {
      renderDateTimeCreate(row) {
        return Tools.formatDateTime(row.createDate, row.createTime);
      },
      renderDateTimeUpdate(row) {
        return Tools.formatDateTime(row.updateDate, row.updateTime);
      },
      editAddSubmit() {
        let _this = this;
        if (this.$refs.editAddInfoForm.validate() & this.$refs.editAddElementForm.validate() & this.$refs.editAddAbstractForm.validate()
          & this.$refs.editAddProtectionForm.validate() & this.$refs.editAddProfitForm.validate() & this.$refs.editCoolingPeriodForm.validate()
          & this.$refs.editEstoppelDecisionForm.validate() & this.$refs.editAddChargeForm.validate() === true) {
          this.httpUtil.comnUpdate({
            action: 'T8ProdConsumerInsurance.updateT8ProdConsumerInsurance',
            params: _this.editAddData
          }).then(data => {
            this.$refs.t8ProdConsumerInsuranceGrid.load()
            this.$refs.editAddT8ProdConsumerInsurancePopup.close();
            return true;
          });
        } else {
          this.$refs.t8ProdConsumerInsuranceGrid.load()
          return false;
        }
      },
      getUserId(value) {
        let _this = this;
        //通过id获取当前行数据
        this.httpUtil.comnQuery({
          action: "T8ProdConsumerInsurance.findT8ProdConsumerInsurancesById",
          params: {id: value.id}
        }).then(data => {
        if(value.isAddUpload ==="1"){
          this.editFormData = data.rows[0];
          this.httpUtil.comnQuery({
            action: "User.getUser",
            params: {deptno: value.filledDept}
          }).then(data => {
            this.editFilledUserData = data.rows;
          }).catch({});
          this.httpUtil.comnQuery({
            action: "User.getUser",
            params: {deptno: value.approvalDept}
          }).then(data => {
            this.editApprovalUserData = data.rows;
          }).catch({});
          this.$refs.editT8ProdConsumerInsurancePopup.popup();
        } else {
          this.editAddData = data.rows[0];
          this.httpUtil.comnQuery({
            action: "User.getUser",
            params: {deptno: value.filledDept}
          }).then(data => {
            this.editAddFilledUserData = data.rows;
          }).catch({});
          this.httpUtil.comnQuery({
            action: "User.getUser",
            params: {deptno: value.approvalDept}
          }).then(data => {
            this.editAddApprovalUserData = data.rows;
          }).catch({});
          this.$refs.editAddT8ProdConsumerInsurancePopup.popup();
        }
        }).catch({});
      },
      validateData() {
        return this.$refs.infoForm.validate() & this.$refs.elementForm.validate() & this.$refs.abstractForm.validate() & this.$refs.protectionForm.validate()
          & this.$refs.profitForm.validate() & this.$refs.coolingPeriodForm.validate() & this.$refs.estoppelDecisionForm.validate()
          & this.$refs.chargeForm.validate();
      },
      updateDistributorInfo() {
       let  result= this.validateData();
       let _this = this;

        if(result==1){
          this.httpUtil.comnUpdate({
            action: 'T8ProdConsumerInsurance.addT8ProdConsumerInsurance',
            params: _this.formData
          }).then(data => {
            this.$refs.t8ProdConsumerInsuranceGrid.load()
            this.$refs.addT8ProdConsumerInsurancePopup.close();
            return true;
          });
        } else{
          this.$refs.addSubmitBtn.loading = false;
           return false;
        }
      },
      changeName(row){
        //console.log(row)
        let name = row.fileName;
        let prodName = row.prodName;
        if(name){
          let arrName = name.split(".")
          name = prodName+"-理财产品消保审核表"+"."+arrName[1];
          return name;
        }else {
          return prodName+"-理财产品消保审核表"+".docx";
        }

      },
      onFileSubmitError(){
        this.$refs.fileUploadRef.doReset();
        this.$refs.fileSubmitBtn.setIconStyle(1, []);
        this.$refs.t8ProdConsumerInsuranceGrid.load()
      },
      onFileSubmitSuccess() {
        this.$refs.fileUploadRef.doReset();
        this.$refs.uploadT8ProdConsumerInsuranceForm.reset();
        this.$refs.uploadPopup.close();
        this.$refs.t8ProdConsumerInsuranceGrid.load()
      },
      fileSubmitUploadParam(){
        let formData = this.form;
        this.$refs.fileUploadRef.upload(formData);
      },
      onEditSubmitError(){
        this.$refs.editUploadRef.doReset();
        this.$refs.editSubmitBtn.setIconStyle(1, [])
      },
      onEditSubmitSuccess(){
        this.$refs.editUploadRef.doReset();
        this.$refs.editT8ProdConsumerInsuranceForm.reset();
        this.$refs.editT8ProdConsumerInsurancePopup.close();
        this.$refs.t8ProdConsumerInsuranceGrid.load()
      },
      submitEditUploadParam(){
        if(this.$refs.editUploadForm.validate()==true){
          let formData = this.editFormData;
          this.$refs.editUploadRef.upload(formData);
          this.$refs.t8ProdConsumerInsuranceGrid.load()
          return true;
        } else {
          return false;
        }
      },
      change(value) {
        //console.info('事件测试:');
        //console.log(value);
        this.formData.isCoolingOff = value;
        //alert("this.formData.isCoolingOff=:>>>>>"+this.formData.isCoolingOff);
      },
      change2(value) {
        //console.info('事件测试:');
        //console.log(value);
        this.formData.isIncomeRange = value;
        //alert("this.formData.isCoolingOff=:>>>>>"+this.formData.isIncomeRange);
      },
      handleInput(value) {
        //console.info('事件测试:')
        //console.log(value)
      },
      valuemethod(row){
        return !!(row['go'] === '2')
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row);
        this.formData = Object.assign({}, row)
      },
      getFilledDept4() {
        this.httpUtil.comnQuery({
          action: "User.getUser",
          params: {deptno: this.editAddData.filledDept}
        }).then(data => {
          this.editAddFilledUserData = data.rows;
          this.$nextTick(()=>{
            this.$set(this.editAddData,"filledUser","")
          })
        }).catch({})
      },
      getFilledDept3() {
        this.httpUtil.comnQuery({
          action: "User.getUser",
          params: {deptno: this.editFormData.filledDept}
        }).then(data => {
          this.editFilledUserData = data.rows;
          this.$nextTick(()=>{
            this.$set(this.editFormData,"filledUser","")
          })
        }).catch({})
      },
      getFilledDept2() {
        this.httpUtil.comnQuery({
          action: "User.getUser",
          params: {deptno: this.form.filledDept}
        }).then(data => {
          this.formUserData = data.rows;
          this.$nextTick(()=>{
            this.$set(this.form,"filledUser","")
          })
        }).catch({})
      },
      getFilledDept1() {
        this.httpUtil.comnQuery({
          action: "User.getUser",
          params: {deptno: this.formData.filledDept}
        }).then(data => {
          this.filledUserData = data.rows;
          this.$nextTick(()=>{
            this.$set(this.formData,"filledUser","")
          })
        }).catch({})
      },
      getApprovalDept4() {
        this.httpUtil.comnQuery({
          action: "User.getUser",
          params: {deptno: this.editAddData.approvalDept}
        }).then(data => {
          this.editAddApprovalUserData = data.rows;
          this.$nextTick(()=>{
            this.$set(this.editAddData,"approvalUser","")
          })
        }).catch({})
      },
      getApprovalDept3() {
        this.httpUtil.comnQuery({
          action: "User.getUser",
          params: {deptno: this.editFormData.approvalDept}
        }).then(data => {
          this.editApprovalUserData = data.rows;
          this.$nextTick(()=>{
            this.$set(this.editFormData,"approvalUser","")
          })
        }).catch({})
      },
      getApprovalDept2() {
        this.httpUtil.comnQuery({
          action: "User.getUser",
          params: {deptno: this.form.approvalDept}
        }).then(data => {
          this.formApprovalUserData = data.rows;
          this.$nextTick(()=>{
            this.$set(this.form,"approvalUser","")
          })
        }).catch({})
      },
      getApprovalDept1() {
        this.httpUtil.comnQuery({
          action: "User.getUser",
          params: {deptno: this.formData.approvalDept}
        }).then(data => {
          this.approvalUserData = data.rows;
          this.$nextTick(()=>{
            this.$set(this.formData,"approvalUser","")
          })
        }).catch({})
      },
      getProdNameByCode3(){
        this.httpUtil.comnQuery({
          action: "T8ProdInfo.getProdNameByProdCode",
          params: {prodCode: this.editFormData.prodCode}
        }).then(data => {
          this.$set(this.editFormData,"prodName",data.rows[0].prodName);
        }).catch({})
      },
      getProdNameByCode2(){
        this.httpUtil.comnQuery({
          action: "T8ProdInfo.getProdNameByProdCode",
          params: {prodCode: this.form.prodCode}
        }).then(data => {
          this.$set(this.form,"prodName",data.rows[0].prodName);
        }).catch({})
      },
      getProdNameByCode1(){
        this.httpUtil.comnQuery({
          action: "T8ProdInfo.getProdNameByProdCode",
          params: {prodCode: this.formData.prodCode}
        }).then(data => {
          this.$set(this.formData,"prodName",data.rows[0].prodName);
        }).catch({})
      },
      initFormData(){
        this.formData = {};
        this.$set(this.formData,"keyWords","是。说明书第三部分名词释义需要进行约定")
        this.$set(this.formData,"featuresAttributes","是。说明书第一部分理财产品产品要素需要约定：产品类型、产品收益类型、产品运作模式、产品募集方式")
        this.$set(this.formData,"riskLevel","是。说明书第一部分理财产品要素需要约定：“产品内部风险评级”及“销售对象”")
        this.$set(this.formData,"riskStatement","是。风险揭示第一部分风险揭示需要约定")
        this.$set(this.formData,"incomeDistribution","是。说明书收益分配需要约定")
        this.$set(this.formData,"disputeResolution","是。说明书小节争议解决需要约定")
        this.$set(this.formData,"liabilityClause","否。")
        this.$set(this.formData,"protectionClause","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"financeName","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"termScale","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"transactionStructure","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"yieldOfMaturity","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"guessAndEstimate","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"isGoBack","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"chargingStandard","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"mainBasis","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"establishMethod","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"collectionMethod","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"possibleBenefits","是。协议书协议条款信息保密协议需要约定此内容")
        this.$set(this.formData,"isIncomeRange","1")
        this.$set(this.formData,"isCoolingOff","0")
      },
    }
  };
</script>
<style lang="scss" scoped>
  .prod-panel {
    width: 80%;
    min-width: 1000px;
    max-width: 80%;
    margin-left: 50px;
  }
  .form-item {
    display: flex;
    flex-direction: column;
    margin-bottom: 25px;
    margin-top: 25px;
    margin-left: 50px;
  }
  .prod-items {
    background: #41A0EB;
    border-radius: 1px;
    width: 5px;
    height: 15px;
  }
  .title-desc {
    font-family: PingFangSC-Semibold;
    font-size: 16px;
    color: #3B4858;
    letter-spacing: 0;
    margin-left: 5px;
    font-weight: 500;
  }
  .formPanel::-webkit-scrollbar {
    display: none;
  }

  .formPanel{
    min-height: calc(100vh - 85px);
  }
</style>
