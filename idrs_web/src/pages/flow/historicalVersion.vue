<template>
  <div>
    <k-grid ref="historicalVersionGrid"  :data-params="{id:id,server:server}"
            :data-action="url" :dataPopupAppendToBody="true" :data-page-size="0" data-height="400px"  >

      <k-grid-column data-align="center" data-header="id" data-name="id" data-width="200px" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="服务菜单" data-name="server" data-width="200px"  :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="审批流名称" data-name="displayName" data-width="200px" />
      <k-grid-column data-align="center" data-header="版本号" data-name="version" data-width="100px"/>
      <k-grid-column data-align="center" data-header="操作人" data-name="creator" data-width="150px"/>
      <k-grid-column data-align="center" data-header="操作时间" data-name="createTime" data-width="150px"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="btn-custom-text"  @click="findHistoricalVersion(scope.row.row)"
               data-descript="查看审批流" data-functype="POPUP" data-size="small">
         查看审批流
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="DisplayHistoricalVersionPopup" data-title="查看审批流">
      <DisplayHistoricalVersion ref="DisplayHistoricalVersion" :formData="formList"  ></DisplayHistoricalVersion>
      <div style="text-align: center" v-show="!isShow">
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
      </div>
    </k-popup>


  </div>
</template>

<script>
  import DisplayHistoricalVersion from "@/pages/flow/DisplayHistoricalVersion";
  import Tools from "@/utils/tools";

  export default {
    components: {
      DisplayHistoricalVersion,
    },
    name: "historicalVersion",
    mounted() {

    },
    props:{
      id:'',
      server:'',
      url:'',
    },

    data() {
      return {
        formData:{},
        formList:{},

      }
    },
    created() {
      console.log(this.server)

    },
    methods: {
      findHistoricalVersion(value){
        // console.log('value',value)
        this.httpUtil.ajax({
          url: 'wf/hisTemplate/getHisTemplateInfo.json',
          params: {server:value.server , processId : value.id },
        }).then(data => {
          // console.log('data',data)
          this.formList = data.data;
          // console.log('参数赋值',this.formList)
          this.$refs.DisplayHistoricalVersionPopup.popup();
        });
      },

      passDataSuccess(){
        this.$emit('submitClose', '1')
      },

    },
    watch:{

    }
  }
</script>

<style scoped>

</style>
