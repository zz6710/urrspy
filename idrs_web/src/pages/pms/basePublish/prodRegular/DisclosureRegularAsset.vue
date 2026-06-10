<template>
  <div>
    <div v-show="isShow==='1'">
      <k-form-search data-model-name="DisclosureRegularAsset" data-target="disclosureRegularAssetGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addDisclosureRegularAssetPopup">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="disclosureRegularAssetGrid" @data-row-select="selectRow"
              :data-autoload="false"
              :data-data="{'rows': this.assetShow}"
              :data-page-size="0"
              data-operate-column="false">
        <k-grid-column data-header="id" data-name="id" :data-hidden="true" :data-sortable="true" data-default-sort="ASC"></k-grid-column>
        <k-grid-column data-header="序号" data-name="index" ></k-grid-column>
        <k-grid-column data-header="产品定期报告数据表id" data-name="t8DisclosureNoticeId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="资产种类" data-name="assetsType"></k-grid-column>
        <k-grid-column data-header="金额" data-name="amount"></k-grid-column>
        <k-grid-column data-header="占产品总资产比例(%)" data-name="assetRatio"></k-grid-column>
        <k-grid-column data-header="序号" data-name="rowNumbers" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="创建日期" data-name="crtDate" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="创建时间" data-name="crtTime" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="创建人" data-name="crtUserId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="创建人名称" data-name="crtUserName" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="更新日期" data-name="updDate" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="更新时间" data-name="updTime" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="更新人" data-name="updUserId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="更新人名称" data-name="updUserName" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="备注" data-name="remark" :data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改定期报告资产配置" data-functype="POPUP"
                 data-size="mini"
                 data-target="editDisclosureRegularAssetPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="DisclosureRegularAsset.deleteDisclosureRegularAsset" data-size="mini"
                 data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除定期报告资产配置">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加定期报告资产配置弹出框   -->
    <k-popup ref="addDisclosureRegularAssetPopup" data-title="新增">
      <k-form ref="addDisclosureRegularAssetForm" :data-col="2">
        <k-form-item label="id">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="产品定期报告数据表id">
          <k-field-text v-model="formData.t8DisclosureProdRegularId"/>
        </k-form-item>
        <k-form-item label="资产种类">
          <k-field-text v-model="formData.assetsType"/>
        </k-form-item>
        <k-form-item label="金额">
          <k-field-text v-model="formData.amount"/>
        </k-form-item>
        <k-form-item label="占产品总资产比例">
          <k-field-text v-model="formData.assetRatio"/>
        </k-form-item>
        <k-form-item label="序号">
          <k-field-text v-model="formData.rowNumbers"/>
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

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="DisclosureRegularAsset.addDisclosureRegularAsset"
                 data-from="addDisclosureRegularAssetForm"
                 :data-model="formData" data-target="disclosureRegularAssetGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改定期报告资产配置弹出框   -->
    <k-popup ref="editDisclosureRegularAssetPopup" data-title="修改">
      <k-form ref="editDisclosureRegularAssetForm" :data-col="2">
        <k-form-item label="id">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="产品定期报告数据表id">
          <k-field-text v-model="formData.t8DisclosureProdRegularId"/>
        </k-form-item>
        <k-form-item label="资产种类">
          <k-field-text v-model="formData.assetsType"/>
        </k-form-item>
        <k-form-item label="金额">
          <k-field-text v-model="formData.amount"/>
        </k-form-item>
        <k-form-item label="占产品总资产比例">
          <k-field-text v-model="formData.assetRatio"/>
        </k-form-item>
        <k-form-item label="序号">
          <k-field-text v-model="formData.rowNumbers"/>
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
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-action="DisclosureRegularAsset.updateDisclosureRegularAsset"
                 data-from="editDisclosureRegularAssetForm"
                 :data-model="formData" data-target="disclosureRegularAssetGrid">
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
    prop: 'DisclosureRegularAsset',
    event: 'input'
  },
  props: {
    isShow: {
      type: String,
      default: ''
    },
    t8DisclosureNoticeId: {
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
    DisclosureRegularMajorAsset: {},
  },
  data() {
    return {
      formData: {},
      selectRowData: {},
      assetShow: [],
      assetEdit: [],
    };
  },
  methods: {
    commitCheck(){
      this.$emit('isShowButton', '1')
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    initData() {
      this.httpUtil.comnQuery({
        action: 'DisclosureRegularAsset.findDisclosureRegularAssets',
        params: {
          t8DisclosureNoticeId: this.t8DisclosureNoticeId,
          dataDate: this.dataDate,
          prodCode: this.prodCode
        },
      }).then(data => {
        this.assetEdit = data.rows;
        //将当前数组复制为一个新的对象,防止展示的数组与修改的数组相互影响
        var rows = JSON.parse(JSON.stringify(data.rows))
        this.dataListHandler(rows);
      });
    },
    dataListHandler(rows) {
      //初始化数组
      this.assetShow = [];
      var num = 0;
      if (!rows) {
        this.$refs.disclosureRegularAssetGrid.list = [];
        return
      }
      for (var i = 0; i < rows.length; i++) {
        if (parseInt(rows[i].rowNumbers) > 6) {
          var tempNum = parseInt(rows[i].amount);
          if (isNaN(tempNum)) {
            num += 0;
          } else {
            num += tempNum;
          }
        }
      }
      //如果公募资管产品金额为0则不显示,如果所有明细都为0则所有明细都不显示
      /**1.rowNumbers小于7的数据全都显示，
       * 2.私募资管产品明细金额总合num为0时明细数据都不展示,不为0时都显示
       * 3.公募资管产品金额为0时不显示,不为0显示(特殊处理)
       */
      for (let i = 0; i < rows.length; i++) {
        if (parseInt(rows[i].rowNumbers) < 7) {
           this.assetShow.push(rows[i]);
        }else if (num > 0) {
        //rows[i].assetsType === '公募资管产品' && (rows[i].amount.trim() === '' || rows[i].amount.trim() === '0'
          if (rows[i].assetsType !== '公募资管产品'){
            this.assetShow.push(rows[i]);
          } else if ( rows[i].assetsType === '公募资管产品' && parseInt(rows[i].amount) > 0) {
            this.assetShow.push(rows[i]);
          }
       }

     }
      this.$refs.disclosureRegularAssetGrid.list = this.assetShow;
    },
  },
  created() {
    this.$nextTick(() => {
       this.initData();
    });

  },
};
</script>
