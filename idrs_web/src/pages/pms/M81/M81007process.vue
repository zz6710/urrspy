<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="queryParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="queryParam.prodCode"  data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="queryParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品形态">
        <k-field-select v-model="queryParam.prodMode" data-dict="t8_prod_create_type"></k-field-select>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="queryParam.prodStatus" data-dict="t8_prod_status"></k-field-select>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="queryParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="prodInfoGrid" data-action="T8ProdInfo.findTaProdInfoProcess" @data-row-select="selectRow"
            data-operate-column-position="end" data-align="center" data-operate-data-width="300px" data-operate-column="false" :dataAutoload="false">
      <k-grid-column data-header="id"
                     data-hidden="true"
                     data-name="id">
      </k-grid-column>
      <k-grid-column data-header="产品代码"
                     data-name="prodCode">
      </k-grid-column>
      <k-grid-column data-header="产品名称"
                     data-name="prodName">
      </k-grid-column>
      <k-grid-column data-header="产品形态"
                     data-name="prodMode" data-dict="t8_prod_create_type">
      </k-grid-column>
      <k-grid-column data-header="产品成立日"
                     data-name="establishDate" data-type="date" >
      </k-grid-column>
      <k-grid-column data-header="产品到期日"
                     data-name="endDate" data-type="date">
      </k-grid-column>
      <k-grid-column data-header="产品状态"
                     data-name="prodStatus" data-dict="t8_prod_status">
      </k-grid-column>
      <k-grid-column data-header="产品子状态"
                     data-name="prodSonStatus" data-dict="t8_prod_son_status">
      </k-grid-column>
      <template slot="expand" slot-scope="props" ref="expands">
        <div class="tool">
          <div class="main">
            <div v-if="showGn" class="step-content">
              <div class="step">
                <el-steps :active="stepActiveIndex" :space="200">
                  <div @click="clickStep(item.json,index)"  v-for="(item,index) in stepList">
                    <el-step :title="item.phaseName"></el-step>
                  </div>
                </el-steps>
              </div>
              <div class="content">
                <gn2 v-if="showGn"  :height="700" :data="editorData" :isSkip="true" :t8_prod_info_id="props.row.id" :prod_code="props.row.prodCode"></gn2>
              </div>
            </div>
          </div>
        </div>
      </template>

    </k-grid>
  </div>
</template>
<script>
  import KFormItem from "../../../components/k-element/k-from/k-form-item";
  import gn2 from "@/pages/flow/G6Editor/gn2.vue"
  import kayak from '@/frame/kayak.js';
  import Tools from '@/utils/tools.js';
  import { assign } from "lodash";
  import AddComp from "./M81001add"
  import httpUtil from "../../../frame/httpUtil";
  import addtemplateComp from "./prodInfoAddAssembly";
  import addUserComp from "./addUserComp";
  export default {
    name: 'M81007process',
    components: {addtemplateComp, gn2, addUserComp},
    data() {
      return {
        prodGroupUserFormData: {
          groupId: ''
        },
        queryParam: {},
        showGroupName: false,
        prodGroupUserItems: [],
        queryRoleGraphql: "{queryRole(action:\"find\",roleids:\"0\") {rows{roleid, rolename, parentroleid, descript},results}}",
        ProgressRecordPopup:{},
        prodProgressRecord_params:{},
        prodProcessForm:{},
        prodProgressRecord:[],
        stepActiveIndex:1,
        stepList:[],
        showGn:false,
        editorData:{},
        prodRiskRat:{
          prodInfoId:"",//产品ID
          prodName:"",//产品名称
          t8_risk_template_id:"",//模板ID
          $RatGrid:null,//风险评分表格对象
        },
        prodCreateInfo:{},
        formData: {},
        selectRowData: {},
        expands: [],
        prodCard: [],
        prodCode: "",
        prodMode: "",
      };
    },
    methods: {
      selectRow(row, column, event) {
        const _this = this
        _this.formData = assign({}, row)
        this.seeDetail(row.prodCode)
      },
      clickStep(json,index){
        this.stepActiveIndex=index+1
        this.showGn=false
        this.editorData.processData=json?JSON.parse(json).orignal:{}
        this.$nextTick(()=>{
          this.showGn=true
        })
      },
      seeDetail(prodCode){
        this.stepList = [],
          this.showGn = false
        this.httpUtil.ajax({
          url: 'wf/flowTemplate/findTemplateItemByProdCode.json',
          params: {
            prodCode: prodCode
          },
        }).then(res => {
          this.stepList = res.data
          this.stepActiveIndex = 1
          // this.editorData.processData = res.data.length>0? res.data[0].json?JSON.parse(res.data[0].json).orignal:{}:{};
          // this.showGn=true
          //选中当前阶段
          if(res.data.length > 0){
            let curr_phase=res.data[0].curr_phase;//当前阶段
            for(let i=0;i<res.data.length;i++){
              if(res.data[i].phase == curr_phase){
                //设置当前阶段
                this.stepActiveIndex=i;
                //默认加载当前阶段
                this.clickStep(res.data[i].json,this.stepActiveIndex);
                break;
              }
            }
          }
        });
      },
    },
    created() {
      this.global.getProdUser('');
      //接收路由中的参数，加载grid
      this.$nextTick(() => {
        this.$refs.prodInfoGrid.load({
          prodCode: this.$route.query.findProdCode,
          prodName: this.$route.query.findProdName,
          prodMode: this.$route.query.findProdMode
        });
      })

    },
    activated() {
      let prodCode = '';//产品代码
      let prodName = '';//产品名称
      let prodMode = '';//产品形态
      let prodStatus = '';//产品状态
      let isRecycleCode = '';//代码回收
      //查询条件产品代码不为空
      if (this.queryParam.prodCode != '' && this.queryParam.prodCode != undefined) {
        prodCode = this.queryParam.prodCode
      }
      if (this.queryParam.prodName != '' && this.queryParam.prodName != undefined) {
        prodName = this.queryParam.prodName
      }
      if (this.queryParam.prodMode != '' && this.queryParam.prodMode != undefined) {
        prodMode = this.queryParam.prodMode
      }
      if (this.queryParam.prodStatus != '' && this.queryParam.prodStatus != undefined) {
        prodStatus = this.queryParam.prodStatus
      }
      if (this.queryParam.isRecycleCode != '' && this.queryParam.isRecycleCode != undefined) {
        isRecycleCode = this.queryParam.isRecycleCode
      }
      //路由中产品代码不为空，以路由中的产品代码为准 （产品开启流程后跳转过来或者首页工作流待办跳转过来）
      if (this.$route.query.findProdCode != undefined && this.$route.query.findProdCode != '') {
        prodCode = this.$route.query.findProdCode;
      }
      this.$set(this.queryParam, 'prodCode', prodCode);
      this.$refs.prodInfoGrid.load({
        prodCode: prodCode,
        prodName: prodName,
        prodMode: prodMode,
        prodStatus: prodStatus,
        isRecycleCode: isRecycleCode
      });
    },
  }

</script>

<style>
  .el-icon-color{
    color: #FF8C00;
  }
</style>
<style lang="scss" scoped>
  ::v-deep.step-content{
    display: flex;
    flex-direction: column;
    align-items: center;
    ::v-deep.el-step{
      cursor: pointer;
      width: 100px;
      .el-step__line{
        display: block !important;
      }
      .el-step__title{
        width: 100px;
      }
    }
  }
  ::v-deep .dropdown-menu{
    margin-top: 10px;
    right: auto;
  }
  ::v-deep .k-card{
    z-index:0;
  }

  .el-table__expanded-cell {
    background-color: #F9F9F9 !important;
  }
  .el-table__expanded-cell:hover{
    background-color: #F9F9F9 !important;
  }

  .tool{
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: -15px;
    margin-bottom: -20px;
    padding-top: 60px;
  }

  .row-tools{
    background: #FFFFFF;
    box-shadow: 0 8px 12px 0 rgba(0,0,0,0.06);
    border-radius: 24px;
    height: 45px;
    //display: inline-block;
    align-items: center;
    //margin-top: 20px;
    text-align: center;
    //margin-left: -690px;
    position: absolute;
    left: 0;
    margin-top: -45px;
    margin-left: 30px;
  }
  .tools-text{
    font-family: PingFangSC-Regular;
    font-size: 10px;
    letter-spacing: 0;
    /*font-weight: 500;*/
    /*    margin-top: 15px;*/
    color: #707E8F;
  }
  .tool-item{
    margin-left: 25px;
    margin-right: 25px;
    margin-top: -1px;
    float: left;
  }
  .step-tools{
    margin-top: 60px;
    margin-bottom: 20px;
    align-items: center;
    display: inline-block;
  }
  .tool-item .md-icon{
    width: 15px;
    height: 15px;
    margin-top: -1px;
    margin-bottom: 6px;
  }
  .test{
    width: 0;
    height: 0;
    border-top: 70px solid transparent;
    border-right: 140px solid #6bbf20;
    border-bottom: 70px solid transparent;
  }
  .steps{
    display: flex;
    flex-direction: row;
    align-items: center;
    overflow-x: auto;
  }

  .step{
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-left: 2px;
    padding-right: 4px;
  }
  .my-line{
    background-image: linear-gradient(90deg, #7FC7FF 0%, #35A7EF 100%);
    border-radius: 0 0 0 0;
    width: 156px;
    height: 6px;
    margin-top: 12px;
  }
  .my-content{
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: -12px;
  }
  .my-number-content{
    top:0;
    left:-15px ;
    text-align: center;
    display: inline-block;
    height: 18px;
    width: 18px;
    color: #ffffff;
    background-color: #b9b9b9;
    line-height: 18px;
    border-radius: 50%;
    text-align: center;
    /*  border:1px solid;*/
    background-color: #4CA7EE;

  }
  .my-number{
    font-family: Arial-BoldMT;
    font-size: 12px;
    color: #ffffff;
    letter-spacing: 0;
    z-index: 2;
    /*margin-top: -2px;*/
    font-weight : 500;
  }
  .my-title{
    font-weight: 500;
    color: #3B4858;
    margin-top: 4px;
    font-size: 14px;
    font-family: PingFangSC-Medium;
  }
  .my-desc{
    height: 24px;
    color: #999999;
    margin-top: 1px;
  }
  .last-step-content{
    display: flex;
    flex-direction: row;
  }
  .my-delta{
    height: 10px;
    width: 0px;
    position: absolute;
    margin-left: 155px;
    margin-top: 8px;
    border-bottom: 10px solid #4CA7EE;
    border-right: 16px solid transparent;
  }
  .back-line-content{
    margin-bottom: -18px;
    margin-left: -250px;
    margin-right: -450px;
  }
  .my-back-line{
    background: #EDEDED;
    border-radius: 0px 0px 0px 0px;
    width: 100%;
    height: 6px;
    margin-top: -25px;
  }
  .popover-container{
    display: flex;
    flex-direction: column;
    margin-left: 10px;
  }
  .template{
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  .template-desc{
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  .template-btn{
    display: flex;
    flex-direction: row;
    margin-left: 10px;
  }

  .module{
    border: 1px solid #41A0EB;
    margin-left: 10px;
    padding: 1px 15px;
    border-radius: 2px;
    color: #41A0EB;
  }

  .task{
    display: flex;
    flex-direction: row;
    margin-top: 20px;
  }
  .task-item{

    display: inline-block;
    max-width: 420px;
    border-radius: 2px;
    border-radius: 2px;
    //width: 100%;
    height: 203px;
    //margin: 0 auto;
    margin: 0 5px 0 5px;
    text-align: left;
  }
  .task-box{
    margin-left: 7.5px;
    margin-right: 7.5px;
    width: 89.4px;
    height: 23px;
    position: relative;
    display: inline-block;
    // display: flex;
    // flex-direction: column;
    // height: 200px;
    //box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.14);
    border-radius: 2px;
    text-align: center;
    line-height: 25px;
    margin-top: 10px;
    background-repeat: no-repeat;
  }
  .task-desc{
    font-family: PingFangSC-Regular;
    font-size: 12px;
    color: #FFFFFF;
  }
  .el-popover .el-popper{
    top: 180px;
  }

  .tool-item:hover{
    cursor:pointer;
  }

  .tool-item:hover ::v-deep.md-icon svg >path{
    fill: #41A0EB;
  }
  .tool-item:hover span{
    color: #41A0EB;
  }

  .tool-disable,
  .tool-disable:hover{
    cursor: default;
  }

  .tool-disable ::v-deep.md-icon svg >path,
  .tool-disable:hover ::v-deep.md-icon svg >path{
    fill: #cccccc;
  }
  .tool-disable span,
  .tool-disable:hover span{
    color: #cccccc;
  }

  .prodModeCursor :hover{
    cursor: pointer;
  }

  .el-icon-circle-plus:before{
    margin-left: -15px;
    padding-right: 10px;
    //margin-top: 10px;
    padding-top: 3px;
  }

  ::v-deep.el-step{
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

    .center-content {
      .pz {
        display: flex;
        flex-direction: row-reverse;
      }
    }
  }

  .row-expand-cover .el-table__expand-icon {
    visibility: hidden;
  }
</style>
