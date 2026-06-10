<template>
  <div>
    <div>
      <k-form-search data-model-name="T8ProdNetValueNotice" data-target="t8ProdNetValueNoticeGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addT8ProdNetValueNoticePopup">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="t8ProdNetValueNoticeGrid" @data-row-select="selectRow"
              data-action="T8ProdNetValueNotice.findT8ProdNetValueNotices">
        <k-grid-column data-header="id" data-name="id"></k-grid-column>
        <k-grid-column data-header="披露任务ID" data-name="t8DisclosureTaskId"></k-grid-column>
        <k-grid-column data-header="产品代码 " data-name="prodCode"></k-grid-column>
        <k-grid-column data-header="净值日期" data-name="netvalDate"></k-grid-column>
        <k-grid-column data-header="披露日期" data-name="disclosureDate"></k-grid-column>
        <k-grid-column data-header="产品总净值" data-name="totalNet"></k-grid-column>
        <k-grid-column data-header="产品总份额" data-name="totalVol"></k-grid-column>
        <k-grid-column data-header="产品单位净值" data-name="nav"></k-grid-column>
        <k-grid-column data-header="当日收益" data-name="navProfit"></k-grid-column>
        <k-grid-column data-header="单位万份收益" data-name="tenThousandIncomeAmt"></k-grid-column>
        <k-grid-column data-header="近七日年化收益率" data-name="sevenDaysIncomeRate"></k-grid-column>
        <k-grid-column data-header="产品累计净值" data-name="totalNav"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUserId"></k-grid-column>
        <k-grid-column data-header="创建人名称" data-name="crtUserName"></k-grid-column>
        <k-grid-column data-header="更新日期" data-name="updDate"></k-grid-column>
        <k-grid-column data-header="更新时间" data-name="updTime"></k-grid-column>
        <k-grid-column data-header="更新人" data-name="updUserId"></k-grid-column>
        <k-grid-column data-header="更新人名称" data-name="updUserName"></k-grid-column>
        <k-grid-column data-header="备注" data-name="remark"></k-grid-column>
<!--        <k-grid-column data-header="状态" data-name="status"></k-grid-column>-->
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改净值披露数据" data-functype="POPUP" data-size="mini"
                 data-target="editT8ProdNetValueNoticePopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8ProdNetValueNotice.deleteT8ProdNetValueNotice" data-size="mini"
                 data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除净值披露数据">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加净值披露数据弹出框   -->
    <k-popup ref="addT8ProdNetValueNoticePopup" data-title="新增">
      <k-form ref="addT8ProdNetValueNoticeForm" :data-col="2">
        <k-form-item label="id">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="披露任务ID">
          <k-field-text v-model="formData.t8DisclosureTaskId"/>
        </k-form-item>
        <k-form-item label="产品代码 ">
          <k-field-text v-model="formData.prodCode"/>
        </k-form-item>
        <k-form-item label="净值日期">
          <k-field-text v-model="formData.netvalDate"/>
        </k-form-item>
        <k-form-item label="披露日期">
          <k-field-text v-model="formData.disclosureDate"/>
        </k-form-item>
        <k-form-item label="产品总净值">
          <k-field-text v-model="formData.totalNet"/>
        </k-form-item>
        <k-form-item label="产品总份额">
          <k-field-text v-model="formData.totalVol"/>
        </k-form-item>
        <k-form-item label="产品单位净值">
          <k-field-text v-model="formData.nav"/>
        </k-form-item>
        <k-form-item label="当日收益">
          <k-field-text v-model="formData.navProfit"/>
        </k-form-item>
        <k-form-item label="单位万份收益">
          <k-field-text v-model="formData.tenThousandIncomeAmt"/>
        </k-form-item>
        <k-form-item label="近七日年化收益率">
          <k-field-text v-model="formData.sevenDaysIncomeRate"/>
        </k-form-item>
        <k-form-item label="产品累计净值">
          <k-field-text v-model="formData.totalNav"/>
        </k-form-item>
        <k-form-item label="创建日期">
          <k-field-text v-model="formData.crtDate"/>
        </k-form-item>
        <k-form-item label="创建时间">
          <k-field-text v-model="formData.crtTime"/>
        </k-form-item>
        <k-form-item label="创建人">
          <k-field-text v-model="formData.crtUserId"/>
        </k-form-item>
        <k-form-item label="创建人名称">
          <k-field-text v-model="formData.crtUserName"/>
        </k-form-item>
        <k-form-item label="更新日期">
          <k-field-text v-model="formData.updDate"/>
        </k-form-item>
        <k-form-item label="更新时间">
          <k-field-text v-model="formData.updTime"/>
        </k-form-item>
        <k-form-item label="更新人">
          <k-field-text v-model="formData.updUserId"/>
        </k-form-item>
        <k-form-item label="更新人名称">
          <k-field-text v-model="formData.updUserName"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
<!--        <k-form-item label="状态">
          <k-field-text v-model="formData.status"/>
        </k-form-item>-->

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdNetValueNotice.addT8ProdNetValueNotice"
                 data-from="addT8ProdNetValueNoticeForm"
                 :data-model="formData" data-target="t8ProdNetValueNoticeGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改净值披露数据弹出框   -->
    <k-popup ref="editT8ProdNetValueNoticePopup" data-title="修改">
      <k-form ref="editT8ProdNetValueNoticeForm" :data-col="2">
        <k-form-item label="id">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="披露任务ID">
          <k-field-text v-model="formData.t8DisclosureTaskId"/>
        </k-form-item>
        <k-form-item label="产品代码 ">
          <k-field-text v-model="formData.prodCode"/>
        </k-form-item>
        <k-form-item label="净值日期">
          <k-field-text v-model="formData.netvalDate"/>
        </k-form-item>
        <k-form-item label="披露日期">
          <k-field-text v-model="formData.disclosureDate"/>
        </k-form-item>
        <k-form-item label="产品总净值">
          <k-field-text v-model="formData.totalNet"/>
        </k-form-item>
        <k-form-item label="产品总份额">
          <k-field-text v-model="formData.totalVol"/>
        </k-form-item>
        <k-form-item label="产品单位净值">
          <k-field-text v-model="formData.nav"/>
        </k-form-item>
        <k-form-item label="当日收益">
          <k-field-text v-model="formData.navProfit"/>
        </k-form-item>
        <k-form-item label="单位万份收益">
          <k-field-text v-model="formData.tenThousandIncomeAmt"/>
        </k-form-item>
        <k-form-item label="近七日年化收益率">
          <k-field-text v-model="formData.sevenDaysIncomeRate"/>
        </k-form-item>
        <k-form-item label="产品累计净值">
          <k-field-text v-model="formData.totalNav"/>
        </k-form-item>
        <k-form-item label="创建日期">
          <k-field-text v-model="formData.crtDate"/>
        </k-form-item>
        <k-form-item label="创建时间">
          <k-field-text v-model="formData.crtTime"/>
        </k-form-item>
        <k-form-item label="创建人">
          <k-field-text v-model="formData.crtUserId"/>
        </k-form-item>
        <k-form-item label="创建人名称">
          <k-field-text v-model="formData.crtUserName"/>
        </k-form-item>
        <k-form-item label="更新日期">
          <k-field-text v-model="formData.updDate"/>
        </k-form-item>
        <k-form-item label="更新时间">
          <k-field-text v-model="formData.updTime"/>
        </k-form-item>
        <k-form-item label="更新人">
          <k-field-text v-model="formData.updUserId"/>
        </k-form-item>
        <k-form-item label="更新人名称">
          <k-field-text v-model="formData.updUserName"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
<!--        <k-form-item label="状态">
          <k-field-text v-model="formData.status"/>
        </k-form-item>-->
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdNetValueNotice.updateT8ProdNetValueNotice"
                 data-from="editT8ProdNetValueNoticeForm"
                 :data-model="formData" data-target="t8ProdNetValueNoticeGrid">
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
export default {
  data() {
    return {
      formData: {},
      selectRowData: {}
    };
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    }
  }
};
</script>
