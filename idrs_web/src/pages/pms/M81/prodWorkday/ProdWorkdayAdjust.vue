<template>
  <div>
    <k-form class="my-form " ref="prodWorkdayAdjust" :data-col="2" >

      <k-form-item label="产品代码 ">
        <k-field-display v-model="fromData.prodCode" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="id" v-show="false">
        <k-field-display v-model="fromData.id" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品成立日">
        <k-field-display  v-model="fromData.establishDate" data-type="date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="开放起始日">
        <k-field-display v-model="fromData.openStartDate" data-type="date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="开放结束日" >
        <k-field-display v-model="fromData.openEndDate" data-type="date" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="产品到期日">
        <k-field-display v-model="fromData.endDate" data-type="date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="调整原因" data-input-width="550px">
        <k-field-text v-model="fromData.adjustReason" :data-allowblank="false" :data-max-length="200" inputType="textarea" :rows="1"/>
      </k-form-item>

      <k-form-footer data-align="center">
<!--        <k-btn class="md-info md-just-icon md-simple" data-descript="调整产品工作日" :data-handler="()=> this.showListProdWorkDay = true"-->
<!--               data-functype="POPUP" data-size="mini" data-target="workdayPopup">-->
<!--          <md-icon md-src="/static/svg/workday.svg" />-->
<!--        </k-btn>-->

        <k-btn class="btn-custom-primary" data-functype="POPUP"  data-size="mini" data-target="workdayPopup">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>


    </k-form>
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
      // validateData() {
      //   return this.$refs.prodInfo.validate();
      // },

    },
    created() {
    },
    mounted() {
    },
    watch: {



    },

  }
</script>

<style>

</style>
