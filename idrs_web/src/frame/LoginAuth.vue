<template>
  <div class="full-page" :class="{ 'nav-open': $sidebar.showSidebar }">
    <md-toolbar md-elevation="0" class="md-transparent md-toolbar-absolute">
       <img :src="LogoImg" style="width:288px;">
    </md-toolbar>
    <div class="wrapper wrapper-full-page" @click="toggleSidebarPage">
      <div class="page-header header-filter" :class="setPageClass" filter-color="black" :style="setBgImage">
        <div class="container md-offset">
          <zoom-center-transition :duration="pageTransitionDuration" mode="out-in">
            <LoginBox :loginConfig="loginConfig"></LoginBox>
          </zoom-center-transition>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import LoginBox from "./LoginBox1";
  import LogoImg from '@/assets/logo.png'

  export default {
    components: {
      LoginBox
    },
    props: {
      backgroundColor: {
        type: String,
        default: "black"
      }
    },
    inject: {
      autoClose: {
        default: true
      }
    },
    data() {
      return {
        responsive: false,
        showMenu: false,
        menuTransitionDuration: 250,
        pageTransitionDuration: 300,
        year: new Date().getFullYear(),
        loginConfig: {},
        LogoImg
      };
    },
    computed: {
      setBgImage() {
        let images = {
          LoginBox: "/static/images/loginbg1.jpg"
        };
        return {
          backgroundImage: `url(${images['LoginBox']})`
        };
      },
      setPageClass() {
        return `Login-page`.toLowerCase();
      }
    },
    created() {
      this.getLoginConfig();
    },
    methods: {
      getLoginConfig() {
        this.httpUtil.query({
          url: "getLoginConfig.json"
        }).then(data => {
          this.loginConfig = data;
          localStorage.setItem("systemName", this.loginConfig.systemName);
          document.title = this.loginConfig.systemName;
        });;
      },
      toggleSidebarPage() {
        if (this.$sidebar.showSidebar) {
          this.$sidebar.displaySidebar(false);
        }
      },
      linkClick() {
        if (
          this.autoClose &&
          this.$sidebar &&
          this.$sidebar.showSidebar === true
        ) {
          this.$sidebar.displaySidebar(false);
        }
      },
      toggleSidebar() {
        this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
      },
      toggleNavbar() {
        document.body.classList.toggle("nav-open");
        this.showMenu = !this.showMenu;
      },
      closeMenu() {
        document.body.classList.remove("nav-open");
        this.showMenu = false;
      },
      onResponsiveInverted() {
        if (window.innerWidth < 991) {
          this.responsive = true;
        } else {
          this.responsive = false;
        }
      }
    },
    mounted() {
      this.onResponsiveInverted();
      window.addEventListener("resize", this.onResponsiveInverted);
    },
    beforeDestroy() {
      this.closeMenu();
      window.removeEventListener("resize", this.onResponsiveInverted);
    },
    beforeRouteUpdate(to, from, next) {
      // Close the mobile menu first then transition to next page
      if (this.showMenu) {
        this.closeMenu();
        setTimeout(() => {
          next();
        }, this.menuTransitionDuration);
      } else {
        next();
      }
    }
  };
</script>
<style lang="scss" scoped>
  $scaleSize: 0.1;
  $zoomOutStart: 0.7;
  $zoomOutEnd: 0.46;

  @keyframes zoomIn8 {
    from {
      opacity: 0;
      transform: scale3d($scaleSize, $scaleSize, $scaleSize);
    }

    100% {
      opacity: 1;
    }
  }

  .wrapper-full-page {
    overflow: hidden;
  }

  .wrapper-full-page .zoomIn {
    animation-name: zoomIn8;
  }

  @keyframes zoomOut8 {
    from {
      opacity: 1;
      transform: scale3d($zoomOutStart, $zoomOutStart, $zoomOutStart);
    }

    to {
      opacity: 0;
      transform: scale3d($zoomOutEnd, $zoomOutEnd, $zoomOutEnd);
    }
  }

  .wrapper-full-page .zoomOut {
    animation-name: zoomOut8;
  }

  @media (min-width: 576px) {
    .md-offset {
      max-width: 576px;
    }
  }

  @media (min-width: 768px) {
    .md-offset {
      max-width: 768px;
    }
  }

  @media (min-width: 992px) {
    .md-offset {
      max-width: 992px;
    }
  }

  @media (min-width: 1200px) {
    .md-offset {
      max-width: 1200px;
    }
  }
  .container {
    position: absolute;
    top: 20.5%;
    left: 70%;
    transform: translateX(-50%);
  }
  .md-toolbar-absolute {
    padding-left: 0;
  }
</style>
