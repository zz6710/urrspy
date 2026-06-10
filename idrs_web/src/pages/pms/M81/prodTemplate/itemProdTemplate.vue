<template>
  <div  class="md-card k-card md-theme-default parent-div" @scroll="scrollEvent" >
    <div class="md-card-header md-card-header-text md-card-header-green" id="mainPanel">
      <!-- <div class="card-icon">
        <i class="md-icon md-icon-font md-theme-default">assignment</i>
      </div> -->

      <div class="display-flex">
        <div class="menu-container" style="top: 87px;">
          <!--  目录渲染  -->
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

          <div @click="saveProduct" size="mini" :loading="saveLoading" class="pd-button">
            <md-icon md-src="/static/images/create/save5.svg" class="pd-icon-20"/>
            <div class="pd-text">保存</div>
          </div>

          <div @click="back2Page" size="mini" class="pd-button" style="margin-left: 11px;">
            <md-icon md-src="/static/images/create/back4.svg" class="pd-icon-20"/>
            <div class="pd-text">返回</div>
          </div>

        </div>

        <div class="formPanel" ref="formPanel">


          <!--   组件引入-->
        </div>

      </div>


    </div>

  </div>


</template>

<script>

 // import ProdInfo         from "./M81001-ProdInfo"

  export default {
    components: {
 //     ProdInfo,

    },
    props:{
      formClass: {
        type: String,
        default: 'height: 500px;'
      },

    },

    computed: {},
    data() {
      return {
        updateProduct : false,//状态。新增(false)or修改
        menuItems:[],//菜单渲染数组
        showPanels:{},//表单渲染
        prodTemplateCode : '', //模板代码
        saveLoading: false,//保存验证
      }
    },
    methods: {
      //目录点击事件
      changeTab: function (index) {

        this.menuItems.forEach((item, i) => {

          if (i == index) {
            // 滑动到目的地址
            document.getElementById(item.id).scrollIntoView({
              block: 'start',
              inline: 'nearest',
              behavior: 'smooth'
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

      //返回按钮点击事件
      back2Page(item) {
        let backPath = '/main/pms/M81/prodTemplate/T8ProdTemplate';
        this.$router.push({
          path: backPath,
          query: {},
        });

      },

      //@scroll
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

      //保存
      saveProduct() {
//1.判断新增还是修改
// 2.校验是否均通过
      },

    },

      created() {
        this.prodTemplateCode = this.$route.query.prodTemplateCode;               // 产品模板代码
        if(this.prodTemplateCode){this.updateProduct=true;}

        if(this.prodTemplateCode){//修改，查询所有数据反显
          if (this.showPanels.prodInfo){
            this.httpUtil.comnQuery({
              action: 'T8ProdTemplate.findProdTemplateAll',
              params: {
                prodTemplateCode : this.prodTemplateCode
              }
            }).then(data => {
              if(data.rows.length > 0 ){


              }
            });
          }

        }

      },


      mounted() {
        this.$refs.formPanel.style['height'] = (document.body.clientHeight - 112) + 'px';
      },
      model: {
        prop: 'prodTemplateCode',
        event: 'input'
      },
      watch: {},

 }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001.scss";

</style>
