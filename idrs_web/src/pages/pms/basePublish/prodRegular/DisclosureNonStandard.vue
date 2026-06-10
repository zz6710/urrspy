<template>
  <div>
   
    <div>
      <k-grid ref="disclosureNonStandardGrid" @data-row-select="selectRow" data-action="T8ProdNonStandard.findT8ProdNonStandard" :data-autoload="false" :data-operate-column="showFBZC">
		<k-grid-column data-header="id" data-name="id" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="产品定期报告数据表id" data-name="t8DisclosureNoticeId" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="项目名称" data-name="entryName"></k-grid-column>
		<k-grid-column data-header="融资人" data-name="financier"></k-grid-column>
		<k-grid-column data-header="剩余期限" data-name="term"></k-grid-column>
		<k-grid-column data-header="风险状况" data-name="riskStatus"></k-grid-column>
		<k-grid-column data-header="到期收益分配" data-name="incomeDistribution"></k-grid-column>
		<k-grid-column data-header="交易结构" data-name="transactionStructure"></k-grid-column>
        <template slot="operate" slot-scope="props">
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdNonStandard.delNonStandard" data-size="mini"
               data-type="danger" data-target="disclosureNonStandardGrid" :data-confirm="true" data-descript="删除非标资产情况">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>
  </div>
</template>

<script>
  export default {
    model: {
    
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
		showFBZC:{
      		type:Boolean,
			default:false,
		}
     
    },
    data() {
      return {
        formData: {},
        selectRowData: {},
         canDel:true
      };
    },
    created() {
        //console.log("DisclosureRegularMajorAsset=>>>>>",this.DisclosureRegularMajorAsset);
      this.$nextTick(()=>{
        this.$refs.disclosureNonStandardGrid.load({t8DisclosureNoticeId:this.t8DisclosureNoticeId});
            this.httpUtil.comnQuery({
            action: 'DisclosureNotice.findDisclosureNotices',
            params: {
              id: this.t8DisclosureNoticeId,
            }
          }).then(data => {
            if (data.rows.length > 0) {
              var flag = data.rows[0].currentStageStatus;
              if(flag=='5'||flag=='6'||flag=='8')
                                  
                this.canDel = false;
            }
        });
      });
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    }
  };
</script>
