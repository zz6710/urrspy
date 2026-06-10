<template>
  <div>
    <div style="min-height:225px;">
      <k-grid ref="t8FeeProdGrid"  data-action="ProdWorkdayAdjust.findProdWorkdayAdjusts" :data-operate-column="false" :data-params="{'prodCode':fromData.prodCode}">
        <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
        <k-grid-column data-header="调整原因" data-name="adjustReason"></k-grid-column>
        <k-grid-column data-header="调整人员" data-name="crtUser"></k-grid-column>
        <k-grid-column data-header="调整时间" data-name="crtDate"></k-grid-column>
      </k-grid>
    </div>

  </div>
</template>

<script>
  import Tools from '@/utils/tools.js';
  import moment from 'moment';

  export default {
    computed: {},
    model: {
      prop: 'fromData',
      event: 'input'
    },
    props: {
      fromData: {},
    },
    data() {
      return {


      }
    },

    methods: {

    },
    created() {
    },
    mounted() {
    },
    watch: {



    },

  }
</script>
