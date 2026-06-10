<template>
    <div class="main">
      <div class="left-step">
        <el-steps :active="stepActiveIndex" :space="200">
          <div @click="clickStep(item.id,index)"  v-for="(item,index) in templateItemList">
            <el-step :title="item.phaseName"></el-step>
          </div>
         </el-steps>
      </div>

      <div v-if="showDetail" class="center-content">
        <div class="pz">
          <k-btn class="md-danger" :data-handler="gobank">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>返回</k-btn>
          <k-btn class="btn-custom-primary" v-show="this.$route.query.prod_code != undefined"
            :data-model="openProcessTaskParams" data-functype="SUBMIT" data-action="TaskFuncConfig.openProcessTask" :data-after-success="goNavigator">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>开启流程</k-btn>
          <k-btn class="btn-custom-plain" data-functype="POPUP" data-target="gn" :data-handler="setTemplate">配置</k-btn>
        </div>

        <div>
          <gn2 :data="editorData" :is_skip="false">

          </gn2>
        </div>
      </div>

      <k-popup ref="gn" @data-close="closeGn" :dataFullscreen="true">
        <gn :data="editorData"></gn>
      </k-popup>
    </div>
</template>

<script>
    import gn from "./G6Editor/gn";
    import gn2 from "./G6Editor/gn2";
    import pageDetail from "./G6Editor/Page/pageDetail";
    import flowTemplate from "../../store/modules/flowTemplate";
    export default {
        name: "flowTemplateItem",
      components: {gn,pageDetail,gn2},
      props:{
        height: {
          type: Number,
          default: 460
        },
        width: {
          type: Number,
          default:1392
        },
      },
      data(){
          return{
             templateItemList:[],
             editorData:{},
             showDetail:false,
             stepActiveIndex:1,
             openProcessTaskParams:{
               prod_code:'',
               wf_flow_template_id:''
             }
          }
        },
        created() {
          this.openProcessTaskParams.prod_code=this.$route.query.prod_code;
          this.openProcessTaskParams.wf_flow_template_id=this.$route.query.id;
          this.needClose= this.$route.query.needClose;
          let templateId = this.$route.query.id
          this.httpUtil.ajax({
            url: 'wf/flowTemplate/findByTemplateId.json',
            params: {
              templateId:templateId
            },
          }).then(data => {
            this.templateItemList = data.data
            this.clickStep(this.templateItemList[0].id,0)
          });
        },
        methods:{
          closeGn(){
            this.$store.commit("flowTemplate/setNodeStatus","no-edit")
            this.seeDetail(this.editorData.id)
          },
          gobank(){
            if(this.needClose=='1'){
              let backPath = '/main/pms/M81/M81007';
              this.$router.push({
                path: backPath,
                query: {},
              });
            }else{
              this.$router.go(-1);
            }
          },
          setTemplate(){
            this.showDetail=false
            this.$nextTick(()=>{
              this.editorData.templatePopup= this.$refs.gn
              this.editorData.templateItem= this
              this.$store.commit("flowTemplate/setNodeStatus","edit")
              this.$refs.gn.popup();
            })
          },
          clickStep(id,index){
             this.stepActiveIndex=index+1
             this.seeDetail(id)
          },
          seeDetail(id){
            this.httpUtil.ajax({
              url: 'wf/flowTemplate/findTemplateItemById.json',
              params: {
                id:id
              },
            }).then(res => {
              let data = res.data;
              let editorData = {};
              editorData.processData = data.json?JSON.parse(data.json).orignal:{};
              this.editorData = editorData;
              this.editorData.id=id
              this.showDetail=true
            });
          },
          //开启流程后跳转到产品运营导航页面
          goNavigator(){
            this.$router.push({
              path:'/main/pms/M81/M81007process',
              query:{findProdCode:this.$route.query.prod_code},
          });
          }
        }
    }
</script>

<style lang="scss" scoped>
   /deep/.el-step{
     cursor: pointer;
     width: 100px;
     .el-step__line{
       display: block !important;
     }
     .el-step__title{
       width: 100px;
     }
   }
  .main{
    display: flex;
    flex-direction: column;
    align-items: center;
    .left-step{
      width: 800px;
      display: flex;
      justify-content: center;
    }
    .center-content{
      .pz{
        display: flex;
        flex-direction: row-reverse;
      }
    }
  }
</style>
