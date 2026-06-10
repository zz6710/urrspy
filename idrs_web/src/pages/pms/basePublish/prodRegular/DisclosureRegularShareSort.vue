<template>
  <div>
    <div v-show="isShow==='1'">
      <k-form-search data-model-name="DisclosureRegularShareSort" data-target="disclosureShareSortGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addDisclosureRegularShareSortPopup">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="disclosureRegularShareSortGrid" @data-row-select="selectRow"
              data-action="DisclosureRegularShareSort.findDisclosureShareSorts1" :data-autoload="false" :data-operate-column="this.showButton">
        <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="公告id" data-name="noticeId"  data-hidden="true"></k-grid-column>
        <k-grid-column data-header="母产品代码" data-name="parentProdCode"></k-grid-column>
        <k-grid-column data-header="份额名称" data-align="center" data-name="shareName"></k-grid-column>
        <k-grid-column data-header="销售代码" data-name="salesCode"></k-grid-column>
        <k-grid-column data-header="销售名称" data-name="salesName"></k-grid-column>
        <k-grid-column data-header="披露状态" data-name="isDeleted" data-render="renderIsDeleted"></k-grid-column>
        <k-grid-column data-header="份额净值" data-name="shareValue"></k-grid-column>
        <k-grid-column data-header="份额累计净值" data-name="shareTotalValue"></k-grid-column>
        <k-grid-column data-header="资产净值" data-name="assetValue"></k-grid-column>
        <k-grid-column data-header="报告期内净值增长率" data-name="durNetGrowth"></k-grid-column>
        <k-grid-column data-header="存续期间净值增长率" data-name="surNetGrowth"></k-grid-column>
        <k-grid-column data-header="期末产品份额净值" data-name="endShareValue"></k-grid-column>
        <k-grid-column data-header="期末产品份额累计净值" data-name="endTotalValue"></k-grid-column>
        <k-grid-column data-header="期末产品份额" data-name="endProdShare"></k-grid-column>
        <k-grid-column data-header="期末资产净值" data-name="endAssetValue"></k-grid-column>
        <k-grid-column data-header="报告期期初产品份额总额" data-name="beginTotalShare"></k-grid-column>
        <k-grid-column data-header="报告期期间产品总申购份额" data-name="durPurchShare"></k-grid-column>
        <k-grid-column data-header="报告期期间产品总赎回份额" data-name="durRedemShare"></k-grid-column>
        <k-grid-column data-header="报告期期末产品份额总额" data-align="center" data-name="endTotalShare"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="修改日期" data-name="updDate" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="修改时间" data-name="updTime" data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改份额分类" data-functype="POPUP" data-size="mini"
                 data-target="editDisclosureRegularShareSortPopup">
            <md-icon>edit</md-icon>
          </k-btn>
            <k-btn class="md-info md-just-icon md-simple" data-descript="披露" data-functype="SUBMIT"
                   :data-disabled="scope.row.row.isDeleted == '0'"
                   data-action="DisclosureRegularShareSort.startStatus" :data-confirm="true"
                   data-target="disclosureRegularShareSortGrid"
                   data-size="mini">
                <md-icon>work</md-icon>
            </k-btn>
            <k-btn class="md-info md-just-icon md-simple" data-descript="不披露"
                   :data-disabled="scope.row.row.isDeleted=='1'"
                   data-action="DisclosureRegularShareSort.stopStatus" :data-confirm="true"
                   data-target="disclosureRegularShareSortGrid" data-functype="SUBMIT" data-size="mini"
            >
                <md-icon>work_off</md-icon>
            </k-btn>
<!--          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"-->
<!--                 data-action="DisclosureRegularShareSort.deleteDisclosureShareSort" data-size="mini"-->
<!--                 data-type="danger" data-target="disclosureRegularShareSortGrid" :data-confirm="true" data-descript="删除份额分类" >-->
<!--            <md-icon>close</md-icon>-->
<!--          </k-btn>-->
        </template>
      </k-grid>
    </div>

    <!--    添加定期报告份额表弹出框   -->
    <k-popup ref="addDisclosureRegularShareSortPopup" data-title="新增">
      <k-form ref="addDisclosureRegularShareSortForm" :data-col="2">
<!--        <k-form-item label="id">-->
<!--          <k-field-text v-model="formData.id"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="公告id">-->
<!--          <k-field-text v-model="formData.noticeId"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="母产品代码">-->
<!--          <k-field-text v-model="formData.parentProdCode"/>-->
<!--        </k-form-item>-->
        <k-form-item label="销售代码">
          <k-field-text v-model="formData.salesCode"/>
        </k-form-item>
        <k-form-item label="销售名称">
          <k-field-text v-model="formData.salesName"/>
        </k-form-item>
        <k-form-item label="份额净值">
          <k-field-text v-model="formData.shareValue"/>
        </k-form-item>
        <k-form-item label="份额累计净值">
          <k-field-text v-model="formData.shareTotalValue"/>
        </k-form-item>
        <k-form-item label="资产净值">
          <k-field-text v-model="formData.assetValue"/>
        </k-form-item>
        <k-form-item label="报告期内净值增长率">
          <k-field-text v-model="formData.durNetGrowth"/>
        </k-form-item>
        <k-form-item label="存续期间净值增长率">
          <k-field-text v-model="formData.surNetGrowth"/>
        </k-form-item>
        <k-form-item label="期末产品份额净值">
          <k-field-text v-model="formData.endShareValue"/>
        </k-form-item>
        <k-form-item label="期末产品份额累计净值">
          <k-field-text v-model="formData.endTotalValue"/>
        </k-form-item>
        <k-form-item label="期末产品份额">
          <k-field-text v-model="formData.endProdShare"/>
        </k-form-item>
        <k-form-item label="期末资产净值">
          <k-field-text v-model="formData.endAssetValue"/>
        </k-form-item>
        <k-form-item label="本期已实现收益">
          <k-field-text v-model="formData.earnings"/>
        </k-form-item>
        <k-form-item label="本期利润">
          <k-field-text v-model="formData.currentProfit"/>
        </k-form-item>
        <k-form-item label="报告期期初产品份额总额">
          <k-field-text v-model="formData.beginTotalShare"/>
        </k-form-item>
        <k-form-item label="报告期期间产品总申购份额">
          <k-field-text v-model="formData.durPurchShare"/>
        </k-form-item>
        <k-form-item label="报告期期间产品总赎回份额">
          <k-field-text v-model="formData.durRedemShare"/>
        </k-form-item>
        <k-form-item label="报告期期末产品份额总额">
          <k-field-text v-model="formData.endTotalShare"/>
        </k-form-item>
<!--        <k-form-item label="创建日期">-->
<!--          <k-field-text v-model="formData.crtDate"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="创建时间">-->
<!--          <k-field-text v-model="formData.crtTime"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="修改日期">-->
<!--          <k-field-text v-model="formData.updDate"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="修改时间">-->
<!--          <k-field-text v-model="formData.updTime"/>-->
<!--        </k-form-item>-->

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureRegularShareSort.addDisclosureShareSort"
                 data-from="addDisclosureRegularShareSortForm"
                 :data-model="formData" data-target="disclosureRegularShareSortGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改定期报告份额表弹出框   -->
    <k-popup ref="editDisclosureRegularShareSortPopup" data-title="修改" data-width="1200px">
      <k-form ref="editDisclosureRegularShareSortForm" :data-col="2"
               data-label-width="300px">
<!--        <k-form-item label="id">-->
<!--          <k-field-text v-model="formData.id"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="公告id">-->
<!--          <k-field-text v-model="formData.noticeId"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="母产品代码">-->
<!--          <k-field-text v-model="formData.parentProdCode"/>-->
<!--        </k-form-item>-->
          <k-form-item label="份额名称">
              <k-field-text v-model="formData.shareName" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="销售代码">
              <k-field-text v-model="formData.salesCode" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="销售名称">
              <k-field-text v-model="formData.salesName" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="份额净值">
              <k-field-text v-model="formData.shareValue" data-validate-type="number" :data-digits="4" :data-max-length="16"
                            :data-default-value="formData.shareValue == null || formData.shareValue == '' ?'0' :formData.shareValue"/>
          </k-form-item>
          <k-form-item label="份额累计净值">
              <k-field-text v-model="formData.shareTotalValue" data-validate-type="number" :data-digits="4" :data-max-length="16"
                            :data-default-value="formData.shareTotalValue == null || formData.shareTotalValue == ''  ? '0' : formData.shareTotalValue"/>
          </k-form-item>
          <k-form-item label="资产净值">
              <k-field-text v-model="formData.assetValue" data-validate-type="number" :data-digits="2" :data-max-length="16"
                            :data-default-value="formData.assetValue == null || formData.assetValue == '' ? '0' : formData.assetValue"/>
          </k-form-item>
          <k-form-item label="报告期内净值增长率">
              <k-field-text v-model="formData.durNetGrowth" data-validate-type="number" :data-digits="2" :data-max-length="10"
                            :data-default-value="formData.durNetGrowth == null || formData.durNetGrowth == '' ?'0': formData.durNetGrowth"/>
          </k-form-item>
          <k-form-item label="存续期间净值增长率">
              <k-field-text v-model="formData.surNetGrowth" data-validate-type="number" :data-digits="2" :data-max-length="10"
                            :data-default-value="formData.surNetGrowth == null || formData.surNetGrowth == '' ? '0' : formData.surNetGrowth"/>
          </k-form-item>
          <k-form-item label="期末产品份额净值">
              <k-field-text v-model="formData.endShareValue" data-validate-type="number" :data-digits="4" :data-max-length="16"
                            :data-default-value="formData.endShareValue ==null || formData.endShareValue == '' ? '0' : formData.endShareValue"/>
          </k-form-item>
          <k-form-item label="期末产品份额累计净值">
              <k-field-text v-model="formData.endTotalValue" data-validate-type="number" :data-digits="4" :data-max-length="16"
                            :data-default-value="formData.endTotalValue == null || formData.endTotalValue == '' ? '0' : formData.endTotalValue"/>
          </k-form-item>
          <k-form-item label="期末产品份额">
              <k-field-text v-model="formData.endProdShare" data-validate-type="number" :data-digits="2" :data-max-length="16"
                            :data-default-value="formData.endProdShare == null || formData.endProdShare == '' ? '0' : formData.endProdShare"/>
          </k-form-item>
          <k-form-item label="期末资产净值">
              <k-field-text v-model="formData.endAssetValue" data-validate-type="number" :data-digits="2" :data-max-length="16"
                            :data-default-value="formData.endAssetValue == null || formData.endAssetValue == '' ? '0' : formData.endAssetValue"/>
          </k-form-item>
          <!--        <k-form-item label="本期已实现收益">-->
          <!--          <k-field-text v-model="formData.earnings"/>-->
          <!--        </k-form-item>-->
          <!--        <k-form-item label="本期利润">-->
          <!--          <k-field-text v-model="formData.currentProfit"/>-->
          <!--        </k-form-item>-->
          <k-form-item label="报告期期初产品份额总额">
              <k-field-text v-model="formData.beginTotalShare" data-validate-type="number" :data-digits="2" :data-max-length="16"
                            :data-default-value="formData.beginTotalShare == null || formData.beginTotalShare =='' ? '0' : formData.beginTotalShare"/>
          </k-form-item>
          <k-form-item label="报告期期间产品总申购份额">
              <k-field-text v-model="formData.durPurchShare" data-validate-type="number" :data-digits="2" :data-max-length="16"
                            :data-default-value="formData.durPurchShare == null || formData.durPurchShare == '' ? '0' : formData.durPurchShare"/>
          </k-form-item>
          <k-form-item label="报告期期间产品总赎回份额">
              <k-field-text v-model="formData.durRedemShare" data-validate-type="number" :data-digits="2" :data-max-length="16"
                            :data-default-value="formData.durRedemShare == null || formData.durRedemShare == '' ? '0' : formData.durRedemShare"/>
          </k-form-item>
          <k-form-item label="报告期期末产品份额总额">
              <k-field-text v-model="formData.endTotalShare" data-validate-type="number" :data-digits="2" :data-max-length="16"
                            :data-default-value="formData.endTotalShare == null || formData.endTotalShare == '' ? '0' : formData.endTotalShare"/>
          </k-form-item>
<!--        <k-form-item label="创建日期">-->
<!--          <k-field-text v-model="formData.crtDate"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="创建时间">-->
<!--          <k-field-text v-model="formData.crtTime"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="修改日期">-->
<!--          <k-field-text v-model="formData.updDate"/>-->
<!--        </k-form-item>-->
<!--        <k-form-item label="修改时间">-->
<!--          <k-field-text v-model="formData.updTime"/>-->
<!--        </k-form-item>-->
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureRegularShareSort.updateDisclosureShareSort"
                 data-from="editDisclosureRegularShareSortForm"
                 :data-model="formData" data-target="disclosureRegularShareSortGrid">
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
        model: {
            prop: 'DisclosureRegularShareSort',
            event: 'input'
        },
        props: {
            isShow: {
                type: String,
                default: ''
            },
            prodCode: {
                type: String,
                default: ''
            },
            dataDate: {
                type: String,
                default: ''
            },
            t8ProdInfoId: {
                type: String,
                default: '',
            },
            t8DisclosureNoticeId: {
                type: String,
                default: ''
            },
            DisclosureRegularShareSort: {},
          showButton: {
              type: Boolean,
              default: false
          }
        },
        data() {
            return {
                formData: {},
                selectRowData: {},
                showBtn:true
            };
        },
        methods: {
            selectRow(row, column, event) {
                this.selectRowData = Object.assign({}, row)
                this.formData = Object.assign({}, row)
            },
            commitCheck() {
                this.$emit('isShowButton', '1')
            },
            selectRow(row, column, event) {
                this.selectRowData = Object.assign({}, row)
                this.formData = Object.assign({}, row)
            },
            renderIsDeleted(row) {
                if (row.isDeleted == '0') {
                    return "披露";
                } else {
                    return "不披露";
                }
            }
        },
        created() {
            this.$nextTick(() => {
                this.$refs.disclosureRegularShareSortGrid.load({
                    t8DisclosureNoticeId: this.t8DisclosureNoticeId,
                    t8ProdInfoId: this.t8ProdInfoId,
                    'baseDate': this.dataDate,
                    prodCode: this.prodCode
                });

                //   this.httpUtil.comnQuery({
                //     action: 'DisclosureNotice.findDisclosureNotices',
                //     params: {
                //       id: this.t8DisclosureNoticeId,
                //     }
                //   }).then(data => {
                //     if (data.rows.length > 0) {
                //       var flag = data.rows[0].currentStageStatus;
                //       if(flag=='5'||flag=='6'||flag=='7'||flag=='8')
                //
                //         this.showBtn = false;
                //     }
                // });
            });

          
        },
    };
</script>
