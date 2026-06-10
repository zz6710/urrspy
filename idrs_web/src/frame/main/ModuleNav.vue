<template>
  <div class="user">
    <div class="photo">
      <img :src="'/static/svg/'+icon+'.svg'" alt="avatar" />
    </div>
    <div class="user-info">
      <a data-toggle="collapse" :aria-expanded="!isClosed" @click.stop="toggleMenu" @click.capture="clicked">
        <span>
          {{ title }}
          <b class="caret"></b>
        </span>
      </a>

      <collapse-transition>
        <div v-show="!isClosed">
          <ul class="nav">
            <slot>
              <li v-for="menu in menus" @click="clickModuleNav(menu)">
                <a href="javascript:void(0);">
                  <span class="sidebar-mini sidebar-short-title" v-text="menu.shortname">系统</span>
                  <span class="sidebar-normal" v-text="menu.menuname">系统管理</span>
                </a>
              </li>
            </slot>
          </ul>
        </div>
      </collapse-transition>
    </div>
  </div>
</template>
<script>
  export default {
    props: {
      avatar: {
        type: String,
        default: "./img/faces/avatar.jpg"
      },
      menus:{
        type:Array,
        default:[]
      }
    },
    watch:{
      menus(newVal){
        let a=false
        newVal.forEach(item=>{
          if(item.menuname==localStorage.getItem("defaultLevel1Menu")){
            this.clickModuleNav(item)
            a=true
          }
        })
        if(!a){
          this.clickModuleNav(newVal[0])
        }
      }
    },
    data() {
      return {
        title:"",
        icon:"",
        isClosed: false,
      };
    },
    methods: {
      clickModuleNav(menu){
        this.title=menu.menuname
        this.icon=menu.icon
        this.toggleMenu()
        this.$emit("clickModuleNav",menu.children?menu.children:[])
      },
      clicked: function(e) {
        e.preventDefault();
      },
      toggleMenu: function() {
        this.isClosed = !this.isClosed;
      }
    }
  };
</script>
<style>
  .collapsed {
    transition: opacity 1s;
  }
</style>
