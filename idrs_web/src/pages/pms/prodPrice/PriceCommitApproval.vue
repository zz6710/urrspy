PriceCommit<template>
  <div class="md-card k-card md-theme-default parent-div" @scroll="scrollEvent">
    <div class="md-card-header md-card-header-text md-card-header-green" id="mainPanel">

      <div class="display-flex">
        <div class="menu-container" style="top: 87px;">
          <!--  目录渲染  -->
          <div class="md-message-line">

            <div class="menu-list">
              <div class="menu" v-for="(item,index) in menuItems" :key="index">
                <div class="icon-container">
                  <div class="icon-image" :class="item.iconClass"></div>
                </div>
                <div class="desc-container " @click="changeTab(index)"
                     @mouseenter="mouseOnItem(item) "
                     @mouseleave="mouseOutItem(item)"
                     :class="[item.mouseOver, item.alive ? item.activeClass: '']">

                  <span class="desc">{{ item.desc }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="share-container">
          <div @click="commit()" size="mini" class="pd-button"
               v-if="global.isShowAuthorityButton('PriceCommit.beforeCommit')">
            <i v-show="saveLoading" class="el-icon-loading"/>
            <md-icon md-src="/static/images/create/save5.svg" v-show="!saveLoading" class="pd-icon-20"/>
            <div v-show="!saveLoading" style="margin-top: 4px;font-size: 8px;padding-left: 13px;">送审</div>
          </div>
        </div>

        <div class="formPanel" ref="formPanel">
          <div class="form-item prod-panel" id="query">
            <div class="title">
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="查询信息"></k-field-display>
            </div>
            <div>
              <k-form class="my-form">
                <k-form-item label="产品代码">
                  <k-field-select v-model="prodCode" data-action="T8Dict.findTaProdInfos"
                                  data-display-field="prodCode,prodName" data-value-field="prodCode"
                                  :data-allowblank="false"
                                  @data-on-change="changeProCode"/>
                </k-form-item>
                <k-form-item label="成立/开放日">
                  <k-field-select
                    v-model="prodDate"
                    :data-params="{prodCode:prodCode}"
                    data-action="T8ProdWorkdays.findProdOpenDays"
                    :data-allowblank="false"
                    data-display-field="changeDate"
                    data-value-field="changeDate"
                    ref="prodDates"
                  />
                </k-form-item>
              </k-form>
            </div>
          </div>
          <div class="form-item prod-panel" id="quota">
            <div class="title">
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="额度信息"></k-field-display>
            </div>
            <quota :prodCode="prodCode" :prodDate="prodDate"></quota>
          </div>
          <div class="form-item prod-panel" id="price">
            <div class="title">
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="业绩基准信息"></k-field-display>
            </div>
            <price :prodCode="prodCode" :prodDate="prodDate"></price>
          </div>
          <div class="form-item prod-panel" id="fee">
            <div class="title">
              <div class="prod-items"></div>
              <k-field-display class="title-desc" value="费用优惠信息"></k-field-display>
            </div>
            <fee :prodCode="prodCode" :prodDate="prodDate"></fee>
          </div>
          <div class="form-item prod-panel" style="height:600px;"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Quota from './ProdQuotaDetail.vue'
import Price from './PriceDetail.vue'
import Fee from './FeeconcessionDetail.vue'
import Tools from "@/utils/tools";
export default {
  name: "PriceCommitApproval",
  components: {Quota, Price, Fee},
  data() {
    return {
      prodCode: '',//产品代码
      prodDate: '',//成立/开放日
      menuItems: [
        {
          id: 'query',
          desc: '查询条件',
          alive: true,
          validate: true,
          iconClass: 'item-base',
          mouseOver: '',
          activeClass: 'selected-base'
        },
        {
          id: 'quota',
          desc: '额度信息',
          alive: false,
          validate: true,
          iconClass: 'item-apy-rdm',
          mouseOver: '',
          activeClass: 'selected-apy-rdm'
        },
        {
          id: 'price',
          desc: '业绩基准',
          alive: false,
          validate: true,
          iconClass: 'item-tailing-commision',
          mouseOver: '',
          activeClass: 'selected-tailing-commision'
        },
        {
          id: 'fee',
          desc: '费用优惠',
          alive: false,
          validate: true,
          iconClass: 'item-sale-service',
          mouseOver: '',
          activeClass: 'selected-sale-service'
        },
      ],//菜单渲染数组  desc id  activeClass  mouseOver  iconClass alive  validate
      saveLoading: false,
    }
  },
  created() {
    //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
    this.global.getHideButtons(this);
    this.prodCode = this.$route.query.prod_code;

  },
  methods: {
    //查询条件产品代码改变事件
    changeProCode() {
      this.prodDate = '';
      this.$refs.prodDates.load({prodCode: this.prodCode});
    },
    //点击查询按钮
    queryData() {
      if (this.prodCode != '' && this.prodDate != '') {

      }
    },
    //滚动事件
    scrollEvent() {
      this.menuItems.forEach((item, i) => {
        let heigthTop = document.getElementById(item.id).getBoundingClientRect().top
        if (heigthTop <= 100) {
          item.alive = true;
        } else {
          item.alive = false;
        }
        if (i != 0 && item.alive == true) {
          this.menuItems[i - 1].alive = false;
        }
      });
    },
    //目录点击事件
    changeTab: function (index) {

      this.menuItems.forEach((item, i) => {

        if (i == index) {
          this.$nextTick(() => {
            // 滑动到目的地址
            document.getElementById(item.id).scrollIntoView({
              block: 'start',
              inline: 'nearest',
              behavior: 'smooth'
            })
          })
        }
      })
    },
    // 目录渲染
    mouseOnItem(item) {
      item.mouseOver = item.activeClass
    },

    // 目录渲染
    mouseOutItem(item) {
      item.mouseOver = ''
    },
    //点击送审按钮
    commit() {
      if (this.prodCode == '' || this.prodDate == '') {
        Tools.alert("未输入必选查询条件！", "danger");
        return false;
      }
      this.saveLoading = true;

      this.httpUtil.comnUpdate({
        action: 'PriceCommit.beforeCommit',
        params: {prodCode: this.prodCode, prodDate: this.prodDate},
        successAlert: false
      }).then(data => {
        this.$nextTick(()=>{
          let res = data.returndata.result.toString();
          console.log("data=:>>>>>", data);
          console.log("res=:>>>>>",res);
          if(res==="success"){
            this.httpUtil.comnUpdate({
              action: 'PriceCommit.commit',
              params: {prodCode: this.prodCode, prodDate: this.prodDate},
              successAlert: true
            }).then(data => {
              this.saveLoading = false;
            });
          }
          this.saveLoading = false;
        });
      });
    }
  }
}
</script>

<style lang="scss" scoped>

@import "../../../styles/T81001.scss";

</style>
