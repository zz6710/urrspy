<template>
  <div class="fixed-plugin" v-click-outside="closeDropDown">
    <div class="dropdown show-dropdown" :class="{ show: isOpen }">
      <a data-toggle="dropdown" @click="toggleDropDown">
        <md-icon class="fix-icon" md-src="/static/svg/setting.svg"></md-icon>
      </a>
      <ul class="dropdown-menu sfs" :class="{ show: isOpen }">
        <li class="header-title">菜单主题</li>
        <li class="adjustments-line text-center">
          <span v-for="item in sidebarColors" :key="item.color" class="badge filter" :class="[`badge-${item.color}`, { active: item.active }]"
            :data-color="item.color" @click="changeSidebarBackground(item)">
          </span>
        </li>
        <li class="header-title">菜单背景色</li>
        <li class="adjustments-line text-center">
          <span v-for="item in sidebarBg" :key="item.colorBg" class="badge filter" :class="[`badge-${item.colorBg}`, { active: item.active }]"
            :data-color="item.colorBg" @click="changeSidebarBg(item)">
          </span>
        </li>
        <li class="adjustments-line sidebar-mini">
          菜单最小化
          <md-switch :value="!sidebarMini" @change="val => updateValue('sidebarMini', val)"></md-switch>
        </li>
        <li class="adjustments-line sidebar-img">
          菜单背景图
          <md-switch :value="!sidebarImg" @change="val => updateValueImg('sidebarImg', val)"></md-switch>
        </li>

        <li class="header-title">背景图</li>
        <li v-for="item in sidebarImages" :key="item.image" :class="{ active: item.active }" @click="changeSidebarImage(item)">
          <a class="img-holder switch-trigger">
            <img :src="item.image" alt="" />
          </a>
        </li>

        <li class="header-title fix-one-menu">
          <div class="title">默认一级菜单</div>
          <div class="default-menu">
            <el-select ref="elSelect"
                       popper-class="highest-priority"
                       v-model="defaultLevel1Menu"
                       @change="chaneLevel1Menu">
              <el-option v-for="(item,index) in level1Menu" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled">
              </el-option>
            </el-select>
          </div>
        </li>
<!--        <li class="adjustments-line adjustments-line2"/>-->
      </ul>
    </div>
  </div>
</template>
<script>
  import Vue from "vue";
  export default {
    props: {
      sidebarMini: Boolean,
      sidebarImg: Boolean
    },
    computed:{
      level1Menu(){
        let a=[]
        this.$store.state.system.level1Menu.forEach((item)=>{
          a.push({
              label:item.menuname,
              value:item.menuname
            }
          )
        })
        return a
      }
    },
    data() {
      return {
        defaultLevel1Menu:"",
        sidebarMini2: this.sidebarMini,
        sidebarImg2: this.sidebarImg,
        documentationLink: "https://demos.creative-tim.com/vue-material-dashboard-pro/documentation",
        shareUrl: "https://www.creative-tim.com/product/vue-material-dashboard-pro",
        buyUrl: "",
        proUrl: "https://www.creative-tim.com/product/vue-material-dashboard-pro",
        freeUrl: "https://www.creative-tim.com/product/vue-material-dashboard",
        isOpen: false,
        backgroundImage: "./img/sidebar-2.jpg",
        sidebarColors: [{
            color: "purple",
            code: "#9c27b0",
            active: false
          },
          {
            color: "azure",
            code: "#00bcd4",
            active: false
          },
          {
            color: "green",
            code: "#4caf50",
            active: true
          },
          {
            color: "orange",
            code: "#ff9800",
            active: false
          },
          {
            color: "rose",
            code: "#e91e63",
            active: false
          },
          {
            color: "danger",
            code: "#f44336",
            active: false
          }
        ],
        sidebarBg: [{
            colorBg: "black",
            active: true
          },
          {
            colorBg: "white",
            active: false
          },
          {
            colorBg: "red",
            active: false
          }
        ],
        sidebarImages: [{
            image: "./static/images/sidebar/sidebar-1.jpg",
            active: false
          },
          {
            image: "./static/images/sidebar/sidebar-2.jpg",
            active: true
          },
          {
            image: "./static/images/sidebar/sidebar-3.jpg",
            active: false
          },
          {
            image: "./static/images/sidebar/sidebar-4.jpg",
            active: false
          }
        ]
      };
    },
    created() {
      this.defaultLevel1Menu=localStorage.getItem("defaultLevel1Menu")
      //加载主题背景色
      let themeColor = localStorage.getItem("themeColor");
      let sidebarColor;
      if (themeColor) {
        this.sidebarColors.map(item => {
          if (item.color == themeColor) {
            sidebarColor = item;
          }
        });
        if (sidebarColor) {
          this.changeSidebarBackground(sidebarColor, true);
        }
      }

      //加载菜单背景色
      let menuColor = localStorage.getItem("menuColor");
      let _sidebarBg;
      if (menuColor) {
        this.sidebarBg.map(item => {
          if (item.colorBg == menuColor) {
            _sidebarBg = item;
          }
        });
        if (_sidebarBg) {
          this.changeSidebarBg(_sidebarBg, true)
        }
      }

      //加载背景图
      let menuBg = localStorage.getItem("menuBg");

      let sidebarImage;
      if (menuBg) {
        this.sidebarImages.map(item => {
          if (item.image == menuBg) {
            sidebarImage = item;
          }
        });
        if (sidebarImage) {
          this.changeSidebarImage(sidebarImage, true)
        }
      }

      //加载是否显示菜单背景图
      let _sidebarImg = localStorage.getItem("sidebarImg");
      this.updateValueImg('sidebarImg', _sidebarImg == '1', true);

      //加载菜单是否最小化
      let menuMin = localStorage.getItem("menuMin");

      this.updateValue('sidebarMini', menuMin != '0', true);

      this.loadIndividuation();
    },
    methods: {
      chaneLevel1Menu(val){
        localStorage.setItem("defaultLevel1Menu",val)
      },
      loadIndividuation() {
        this.httpUtil.comnQuery({
          action: "Individuation.findIndividuations"
        }).then(data => {
          let rows = data.rows;
          if (rows && rows.length == 1) {
            let row = rows[0];
            //加载主题背景色
            let themeColor = localStorage.getItem("themeColor");
            if (row.themeColor != themeColor) {
              localStorage.setItem("themeColor", row.themeColor);
              let sidebarColor;
              this.sidebarColors.map(item => {
                if (item.color == row.themeColor) {
                  sidebarColor = item;
                }
              });
              if (sidebarColor) {
                this.changeSidebarBackground(sidebarColor, true);
              }
            }

            //加载菜单背景色
            let menuColor = localStorage.getItem("menuColor");
            let _sidebarBg;
            if (row.menuColor != menuColor) {
              localStorage.setItem("menuColor", row.menuColor);
              this.sidebarBg.map(item => {
                if (item.colorBg == row.menuColor) {
                  _sidebarBg = item;
                }
              });
              if (_sidebarBg) {
                this.changeSidebarBg(_sidebarBg, true)
              }
            }

            //加载背景图
            let menuBg = localStorage.getItem("menuBg");

            let sidebarImage;
            if (row.menuBg != menuBg) {
              localStorage.setItem("menuBg", row.menuBg);
              this.sidebarImages.map(item => {
                if (item.image == row.menuBg) {
                  sidebarImage = item;
                }
              });
              if (sidebarImage) {
                this.changeSidebarImage(sidebarImage, true)
              }
            }

            //加载是否显示背景图
            let _sidebarImg = localStorage.getItem("sidebarImg");
            if (row.menuBgShow != _sidebarImg) {
              localStorage.setItem("sidebarImg", row.menuBgShow);
              this.updateValueImg('sidebarImg', row.menuBgShow == '1', true);
            }

            //加载菜单是否最小化
            let menuMin = localStorage.getItem("menuMin");
            if (row.menuMin != menuMin) {
              localStorage.setItem("menuMin", row.menuMin);
              this.updateValue('sidebarMini', menuMin == '1', true);
            }

          }
        });
      },
      saveIndividuation() {
        let params = {};

        this.sidebarColors.map(item => {
          if (item.active) {
            params.themeColor = item.color;
          }
        });

        this.sidebarBg.map(item => {
          if (item.active) {
            params.menuColor = item.colorBg;
          }
        });

        this.sidebarImages.map(item => {
          if (item.active) {
            params.menuBg = item.image;
          }
        });

        params.menuMin = this.sidebarMini2 ? "1" : "0";
        params.menuBgShow = this.sidebarImg2 ? "1" : "0";

        localStorage.setItem("themeColor", params.themeColor);
        localStorage.setItem("menuColor", params.menuColor);
        localStorage.setItem("menuBg", params.menuBg);
        localStorage.setItem("sidebarImg", params.menuBgShow);
        localStorage.setItem("menuMin", params.menuMin);

        this.httpUtil.comnUpdate({
          action: "Individuation.updateIndividuation",
          params: params,
          successAlert: false
        }).then(data => {

        });
      },
      updateToggleList() {
        this.sidebarMini2 = document.body.classList.value.indexOf("sidebar-mini") == -1;
        this.updateValue('sidebarMini', this.sidebarMini2);
        this.saveIndividuation();
      },
      toggleDropDown() {
        this.isOpen = !this.isOpen;
      },
      closeDropDown() {
        this.isOpen = false;
      },
      toggleList(list, itemToActivate) {
        list.forEach(listItem => {
          listItem.active = false;
        });
        itemToActivate.active = true;
      },
      updateValue(name, val, noSave) {
        this.$emit(`update:${name}`, val);
        this.sidebarMini2 = val;
        if (!noSave) {
          this.saveIndividuation();
        }
      },
      updateValueImg(name, val, noSave) {
        this.$emit(`update:${name}`, val);

        if (val === true) {
          this.sidebarImg2 = true;
          document.body.classList.toggle("sidebar-image");
          this.$emit("update:image", this.backgroundImage);
        } else {
          this.sidebarImg2 = false;
          document.body.classList.toggle("sidebar-image");
          this.$emit("update:image", "");
        }
        if (!noSave) {
          this.saveIndividuation();
        }
      },
      changeSidebarBackground(item, noSave) {
        this.$emit("update:color", item.color);
        this.$store.commit("system/setBackground", item.code);
        this.toggleList(this.sidebarColors, item);
        if (!noSave) {
          this.saveIndividuation();
        }
      },
      changeSidebarBg(item, noSave) {
        this.$emit("update:colorBg", item.colorBg);
        this.toggleList(this.sidebarBg, item);
        if (!noSave) {
          this.saveIndividuation();
        }
      },
      changeSidebarImage(item, noSave) {
        if (this.sidebarImg) {
          this.$emit("update:image", item.image);
        }
        this.backgroundImage = item.image;
        this.toggleList(this.sidebarImages, item);
        if (!noSave) {
          this.saveIndividuation();
        }
      }
    }
  };
</script>
<style lang="scss">
  .centered-row {
    display: flex;
    height: 100%;
    align-items: center;
  }

  .button-container .btn {
    margin-right: 10px;
  }

  .centered-buttons {
    display: flex;
    justify-content: center;
  }

  .fix-icon {
    width: 28px;
    height: 28px;
    margin-top: 10px;
    margin-bottom: 10px;
    margin-left: 18px;
  }

  .sfs{
    height: 470px !important;
  }
  .fix-one-menu{
    border-top: 1px solid #ddd;
    height: 50px !important;
    margin-top: 10px;
    padding: 5px 2px !important;
    .title{
      float: left;
      text-align: left;
      font-weight: 400;
      font-size: 0.95em;
      width: 100px;
      margin-top: 10px;
    }
    .default-menu{
      width: 125px;
      float: right;
      margin-top: 8px;
      margin-right: 6px;
      .md-theme-default{
        margin:5px;
      }
    }
  }


</style>
