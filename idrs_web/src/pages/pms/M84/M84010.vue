<<template>
  <div>

    <k-form-search-customize data-target="taCustBonusIssuGrid" v-model="queryParam">
      <k-form-item label="产品名称">
        <k-field-select v-model="queryParam.prodCode"  data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="taCustBonusIssuGrid" data-export-name="分红下发数据信息表">
        <i class="icon-export" />导出
      </k-btn>
    </k-form-search-customize>
    <k-grid ref="taCustBonusIssuGrid" data-action="T84010.findTaCustBonusIssus1" @data-row-select="selectRow"
            data-operate-data-width="300px" data-operate-column="false" data-align="center">

      <k-grid-column data-header="产品代码"        data-name="prodCode" data-width="150"/>
      <k-grid-column data-header="产品名称" 	      data-name="prodName" data-width="180"/>
      <k-grid-column data-header="销售商代码" 	    data-name="distributorCode" data-width="150"/>
      <k-grid-column data-header="销售商名称" 	    data-name="distributorName" data-width="180"/>
      <k-grid-column data-header="理财账号" 	      data-name="taAcctNo"      data-width="120"/>
      <k-grid-column data-header="理财交易账号" 	  data-name="transAcctNo"   data-width="150"/>
      <k-grid-column data-header="权益登记日" 	    data-name="registerDate"  data-type="date" 	class="center" data-width="120"/>
      <k-grid-column data-header="登记份额" 	      data-name="registerVol"   data-type="money" 	class="text-right" data-width="160"/>
      <k-grid-column data-header="原分红下发日" 	  data-name="oriPaidDate"   data-type="date" 	data-width="100" class="center"/>
      <k-grid-column data-header="实际分红下发日"   data-name="execDate"      data-type="date" 	data-width="120" 	class="center"/>
      <k-grid-column data-header="分红方式" 	      data-name="divMethod" 	  data-dict="div_method" 	class="center" />
      <k-grid-column data-header="红利总金额" 	    data-name="bonusAmt" 	    data-type="money"  	data-width="160"/>
      <k-grid-column data-header="现金分红金额" 	  data-name="bonusAmtCash" 	data-type="money"  	data-width="160"/>
      <k-grid-column data-header="实际下发红利金额" data-name="issuedAmt" 	  data-type="money"  	data-width="180"/>
      <k-grid-column data-header="红利冻结金额" 	  data-name="frzAmt" 	      data-type="money"  	data-width="160"/>
      <k-grid-column data-header="红利再投资金额" 	data-name="bonusAmtReinv" data-type="money"  	data-width="160"/>
      <k-grid-column data-header="红利再投资份额" 	data-name="issuedVol" 	  data-type="money"  	data-width="160"/>
      <k-grid-column data-header="红利冻结份额" 	  data-name="frzVolSum" 	  data-type="money"  	data-width="160"/>
      <k-grid-column data-header="除权日" 	        data-name="navDate" 	    data-type="date" />
      <k-grid-column data-header="红利再投投资价格" data-name="nav"   	      data-width="160"/>
      <!-- <k-grid-column data-header="再投资确认日" 	data-name="oriPaidDate" 	data-type="date" 	data-width="130"  />-->
      <k-grid-column data-header="冻结编号" 	      data-name="frznNo" data-width="150">  </k-grid-column>


    </k-grid>

  </div>
</template>
<script>
  import kayak from '@/frame/kayak.js';

  import { assign } from "lodash";

  export default {
    name:"M84010",
    data() {
      return {
        queryParam:{},
        formData: {},
        selectRowData: {},

      };
    },
    methods: {
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      },

    }
  }

</script>
