<template>
  <div  class="md-card k-card md-theme-default parent-div"  @scroll="scrollEvent">
    <div class="md-card-header md-card-header-text md-card-header-green" id="mainPanel">

        <div class="display-flex">
          <div class="menu-container" style="top: 105px;">
            <div class="md-message-line">
              <div class="menu-list">
                <div class="menu" v-for="(item,index) in menuItems" :key="index">
                  <div class="icon-container">
                    <div class = "icon-image" :class="item.iconClass" ></div>
                  </div>
                  <div class="desc-container " @click="changeTab(index)"
                    @mouseenter="mouseOnItem(item) "
                    @mouseleave="mouseOutItem(item)"
                    :class="[item.mouseOver, item.alive ? item.activeClass: '']">
                    <span class="desc">{{item.desc}}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="share-container">
            <div @click="saveDisInfos" size="mini" :loading="saveLoading" class="pd-button">
              <md-icon md-src="/static/images/create/save5.svg" class="pd-icon-20"/>
              <div class="pd-text">保存</div>
            </div>

            <div @click="back2Page" size="mini" class="pd-button" style="margin-left: 11px;">
              <md-icon md-src="/static/images/create/back4.svg" class="pd-icon-20"/>
              <div class="pd-text">返回</div>
            </div>
          </div>


          <div  class="formPanel" ref="formPanel" >
            <!-- 销售商基本信息 -->
            <div style="padding-top: 2px;">
              <div class="form-item prod-panel"  id="baseInfo" >
                  <div class="title" >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="基本信息"></k-field-display>
                  </div>
                  <baseInfo v-model="formData.baseInfoData" :addFlag="addFlag" ref="baseInfo"/>
              </div>
            </div>

            <!-- 销售商技术信息 -->
<!--            <div style="padding-top: 2px;">
              <div class="form-item prod-panel"  id="techInfo" >
                  <div class="title" >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="技术信息"></k-field-display>
                  </div>
                  <techInfo v-model="formData.baseInfoData" :addFlag="addFlag" ref="techInfo" />
              </div>
            </div>-->

            <!-- 产品权限 -->
            <div style="padding-top: 2px;">
              <div class="form-item prod-panel"  id="prodInterest" >
                  <div class="title" >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="产品权限"></k-field-display>
                  </div>
                  <prodInterest v-model="formData.prodInterestData" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>

            <!-- 尾随佣金 -->
<!--            <div style="padding-top: 2px;" >
              <div class="form-item prod-panel"  id="prjTailing">
                  <div class="title" >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="尾随佣金"></k-field-display>
                  </div>
                  <prjTailing v-model="formData.prjTailingData" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>-->

            <!-- 费用分成 -->
            <div style="padding-top: 2px;">
              <div class="form-item prod-panel"  id="prjFeeDivide">
                  <div class="title" >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="费用分成"></k-field-display>
                  </div>
                  <prjFeeDivide v-model="formData.prjFeeDivideData" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>

            <!-- 折扣率上限 -->
<!--            <div style="padding-top: 2px;">
              <div class="form-item prod-panel"  id="feeDiscount">
                  <div class="title" >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="折扣率上限"></k-field-display>
                  </div>
                  <feeDiscount v-model="formData.feeDiscountData" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>-->

            <!-- 垫资户 -->
<!--            <div style="padding-top: 2px;">
              <div class="form-item prod-panel" id="capitalAcct">
                  <div class="title"  >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="垫资户"></k-field-display>
                  </div>
                  <capitalAcct v-model="formData.capitalAcctData" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>-->

<!--            &lt;!&ndash; 导出索引 &ndash;&gt;
            <div style="padding-top: 2px;">
              <div class="form-item prod-panel" id="indexesFile">
                  <div class="title"  >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="导出索引"></k-field-display>
                  </div>
                  <indexesFile v-model="formData.indexesFileData" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>

            &lt;!&ndash; 导出文件明细 &ndash;&gt;
            <div style="padding-top: 2px;">
              <div class="form-item prod-panel" id="sendManger">
                <div class="title"  >
                  <div class="prod-items"></div>
                  <k-field-display class="title-desc" value="导出文件明细"></k-field-display>
                </div>
                <sendManger v-model="formData.sendManger" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>

            &lt;!&ndash; 导入索引 &ndash;&gt;
            <div style="padding-top: 2px;">
              <div class="form-item prod-panel" id="impIndexesFile">
                <div class="title"  >
                  <div class="prod-items"></div>
                  <k-field-display class="title-desc" value="导入索引"></k-field-display>
                </div>
                <impIndexesFile v-model="formData.impIndexesFileData" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>

            &lt;!&ndash; 导入文件明细 &ndash;&gt;
            <div style="padding-top: 2px;">
              <div class="form-item prod-panel" id="indexesOfd">
                  <div class="title"  >
                    <div class="prod-items"></div>
                    <k-field-display class="title-desc" value="导入文件明细"></k-field-display>
                  </div>
                  <indexesOfd v-model="formData.indexesOfdData" :distributorCode="formData.baseInfoData.distributorCode" />
              </div>
            </div>-->



          </div>
        </div>
    </div>
  </div>


</template>

<script>
  import baseInfo from "./M82001-distributorInfo";
  /*import techInfo from "./T82001-techInfo";*/
  import prjFeeDivide from "./M82001-prjFeeDivide";
  import capitalAcct from "./M82001-advanceFund";
  import prjTailing from "./M82001-prjTailing";
  import feeDiscount from "./M82001-feeDiscount";
  import prodInterest from "./M82001-prodInterest";
  /*import indexesFile from "./M82001-indexesFile";
  import indexesOfd from "./M82001-indexesOfd";*/
  import impIndexesFile from "./M82001-impIndexesFile";
  import sendManger from "./M82001-sendManger";


/*techInfo,indexesFile,indexesOfd,capitalAcct,*/
  export default {
    name:"M82001add",
    components: {
      prjTailing,baseInfo,prjFeeDivide,feeDiscount,prodInterest,impIndexesFile,sendManger
    },
    computed: {},
    data() {
      return {
        saveLoading: false,
        menuItems: [
          { desc: "基本信息", id: 'baseInfo',   	alive: true,   mouseOver: '', iconClass: 'item-base',  		activeClass: "selected-base",  validate: true 	},
          /*{ desc: "技术信息", id: 'techInfo', 	alive: false,  mouseOver: '', iconClass: 'item-period',		activeClass: "selected-period", validate: true	},*/
          { desc: "产品权限", id: 'prodInterest', 		alive: false,  mouseOver: '', iconClass: 'item-limit', 		activeClass: "selected-limit", validate: false		},
          /*{ desc: "尾随佣金", id: 'prjTailing', 	alive: false,  mouseOver: '', iconClass: 'item-capital', 	activeClass: "selected-capital", validate: false},*/
          { desc: "费用分成", id: 'prjFeeDivide', 			alive: false,  mouseOver: '', iconClass: 'item-nav', 			activeClass: "selected-nav", validate: false	},
          /*{ desc: "折扣率上限", id: 'feeDiscount', 	alive: false,  mouseOver: '', iconClass: 'item-other', 	activeClass: "selected-other", validate: false 	},
          { desc: "垫资户", id: 'capitalAcct', 	alive: false,  mouseOver: '', iconClass: 'item-trutee', 	activeClass: "selected-trutee", validate: false},
          { desc: "导出索引", id: 'indexesFile', 	alive: false,  mouseOver: '', iconClass: 'item-tailing-commision', 	activeClass: "selected-tailing-commision", validate: false},
          { desc: "导出文件明细", id: 'sendManger', 	alive: false,  mouseOver: '', iconClass: 'item-trade-fee', 	activeClass: "selected-trade-fee", validate: false},
          { desc: "导入索引", id: 'impIndexesFile', 	alive: false,  mouseOver: '', iconClass: 'item-period', 	activeClass: "selected-period", validate: false},
          { desc: "导入文件明细", id: 'indexesOfd', 	alive: false,  mouseOver: '', iconClass: 'item-sale-fee-pay', 	activeClass: "selected-sale-fee-pay", validate: false},*/
        ],
        formData: {
          prjTailingData: {},
          baseInfoData :{},
          prjFeeDivideData:{},
          /*capitalAcctData:{},*/
          feeDiscountData:{},
          prodInterestData:{},
          /*indexesFileData:{},
          indexesOfdData:{},*/
          sendManger:{},
          impIndexesFile:{},
        },
        distributorCode:'',
        addFlag : true,
      }
    },
    methods: {

      changeTab: function(index) {
        this.menuItems.forEach((item, i) => {
          if (i == index) {
            // 滑动到目的地址，由鼠标事件去切换左边图标的样式
            document.getElementById(item.id).scrollIntoView({
              block: 'start',
              inline: 'nearest',
              behavior: 'smooth'
            })
          }
        })
      },

      mouseOnItem(item){
        item.mouseOver = item.activeClass
      },


      mouseOutItem(item){
        item.mouseOver = ''
      },

      saveDisInfos(params) {
        this.saveLoading = true;

        let isComplite = true; // 校验是否均通过
        // 验输入参数是否符合要求
        for (let menuItem of this.menuItems) {
          if (menuItem.validate){
            let validateResult = this.$refs[menuItem.id].validateData()
            if (!validateResult && isComplite) { // 验证所有的表格
              this.saveLoading = false;
              isComplite = false;
              // 滑动到目的地址
              document.getElementById(menuItem.id).scrollIntoView({
                block:'start',
                inline:'nearest',
                behavior:'smooth'
              })
            }
          }
        }

        if(!isComplite){
          return ;
        }

        if(this.addFlag){
          this.httpUtil.comnUpdate({
            action: 'T82001.addTaDistributorInfo',
            params: this.formData.baseInfoData
          }).then(data => {
            this.saveLoading = false;
            this.back2Page();
          });
        }else{
          this.httpUtil.comnUpdate({
            action: 'T82001.updateTaDistributorInfo',
            params: this.formData.baseInfoData
          }).then(data => {
            this.saveLoading = false;
            this.back2Page();
          });
        }

      },

      saveAfterSuccess(){
        this.$router.push({
          path: "/main/pms/M82/M82001",
          query: {},
        });
      },

      back2Page(){
        this.$router.push({
          path: "/main/pms/M82/M82001",
          query: {},
        });
      },
      scrollEvent () {
          this.menuItems.forEach((item, i) => {
            let heigthTop = document.getElementById(item.id).getBoundingClientRect().top
            if(heigthTop<=120){
              item.alive = true;
            }else{
              item.alive = false;
            }
            if(i!=0 && item.alive == true){
              this.menuItems[i-1].alive = false;
            }
          });
      }
    },
    created() {

      this.addFlag = this.$route.query.addFlag===true || this.$route.query.addFlag==="true" ? true:false;

      //addFlag 为'1'是新增，'0'是修改
      if(this.addFlag===false || this.addFlag=="false"){

        this.httpUtil.comnQuery({
          action: 'T82001.findTaDistributorInfos',
          params: {distributorCode : this.$route.query.distributorCode}
        }).then(data => {
          if(data.rows.length > 0 ){
            this.formData.baseInfoData = data.rows[0];
          }
        });

        /*//导入批次
        this.httpUtil.comnQuery({
          action: 'T8ClearGroupMember.queryTaClearGroupMember',
          params: {groupMember : this.$route.query.distributorCode,  execTaskType:"3"}
        }).then(data => {
          if(data.rows.length > 0 ){
            this.formData.baseInfoData.impTaskGroup=data.rows[0].taskGroup;
          }
        });*/

        /*//导出批次
        this.httpUtil.comnQuery({
                action: 'T8ClearGroupMember.queryTaClearGroupMember',
                params: {groupMember : this.$route.query.distributorCode,  execTaskType:"4"}
              }).then(data => {
                if(data.rows.length > 0 ){
                  this.formData.baseInfoData.expTaskGroup=data.rows[0].taskGroup;
                }
              });*/

      }else{
        this.addFlag=true;
      }

    },

    mounted() {
      this.$refs.formPanel.style['height'] = (document.body.clientHeight - 112)+ 'px';
    },
    watch: {

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../styles/T81001.scss";

::v-deep .prod-panel > div > form > div.k-form-body{
  margin-left: auto;
  max-height: none !important;
  width: 100%;
}

.main-panel>.content{
  padding: 30px 15px 0 15px;
}
</style>
