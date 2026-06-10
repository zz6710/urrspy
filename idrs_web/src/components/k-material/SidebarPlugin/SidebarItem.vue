<template>
  <component :is="baseComponent" :to="link.url ? link.url : '/'" :class="{ active: isActive }" tag="li">
    <a v-if="link.children||link.level==2" :href="link.url?'#/main/'+link.url:'javascript:void(0);'" class="nav-link sidebar-menu-item" :aria-expanded="!collapsed" data-toggle="collapse"
      @click="collapseMenu">
      <md-icon :md-src="'/static/svg/'+link.icon+'.svg'"></md-icon>
      <p>
        {{ link.menuname }}
        <b class="caret" v-if="link.children"></b>
      </p>
    </a>

    <collapse-transition>
      <div v-if="$slots.default || link.children" v-show="!collapsed">
        <ul class="nav">
          <slot></slot>
        </ul>
      </div>
    </collapse-transition>
    <slot name="title" v-if="!link.children && !$slots.default&&! link.level">
      <component :to="'/main/'+link.url" @click.native="linkClick" :is="elementType(link, false)" :class="{ active: link.active }"
        class="nav-link" :target="link.target" :href="'/main/'+link.path">
        <template>
          <span class="sidebar-mini sidebar-short-title">{{ link.shortname? link.shortname : link.menuname.substring(0,2) }}</span>
          <span class="sidebar-normal">{{ link.menuname }}</span>
        </template>
      </component>
    </slot>
  </component>
</template>
<script>
  export default {
    name: "sidebar-item",
    props: {
      menu: {
        type: Boolean,
        default: false
      },
      link: {
        type: Object,
        default: () => {
          return {
            url: "",
            children: []
          };
        }
      }
    },
    data() {
      return {
        children: [],
        collapsed: true
      };
    },
    computed: {
      baseComponent() {
        return this.isMenu || this.link.isRoute ? "li" : "router-link";
      },
      linkPrefix() {
        if (this.link.menuname) {
          let words = this.link.menuname.split(" ");
          return words.map(word => word.substring(0, 1)).join("");
        }
        return false;
      },
      isMenu() {
        return this.children || this.menu === true;
      },
      isActive() {
        if (this.$route && this.$route.path) {
          if (this.link.children) {
            let matchingRoute = this.link.children.find(c =>
              this.$route.path.startsWith('/main/' + c.url)
            );
            if (matchingRoute !== undefined) {
              this.collapsed = false;
              return true;
            }else{
              this.collapsed = true;
            }
          }

          if (this.$route.path == ('/main/' + this.link.url)) {
            return true;
          }
        }
        return false;
      }
    },
    methods: {
      addChild(item) {
        const index = this.$slots.default.indexOf(item.$vnode);
        this.children.splice(index, 0, item);
      },
      removeChild(item) {
        const tabs = this.children;
        const index = tabs.indexOf(item);
        tabs.splice(index, 1);
      },
      elementType(link, isParent = true) {
        if (link.isRoute === false) {
          return isParent ? "li" : "a";
        } else {
          return "router-link";
        }
      },
      linkAbbreviation(name) {
        const matches = name.match(/\b(\w)/g);
        return matches.join("");
      },
      linkClick() {
        // if (!this.link.children) {
        //   // this.$sidebar.collapseAllMenus();
        // }
        // if (
        //   this.autoClose &&
        //   this.$sidebar &&
        //   this.$sidebar.showSidebar === true
        // ) {
        //   this.$sidebar.displaySidebar(false);
        // }
      },
      collapseMenu() {
        if (this.collapsed) {
          this.$sidebar.addSidebarLink(this);
          this.$sidebar.collapseAllMenus();
        }
        this.collapsed = !this.collapsed;
      },
      collapseSubMenu(link) {
        link.collapsed = !link.collapsed;
      }
    },
    mounted() {
      if (this.link.collapsed !== undefined) {
        this.collapsed = this.link.collapsed;
      }
      if (this.isActive && this.isMenu) {
        this.collapsed = false;
      }
    },
    destroyed() {
      this.$sidebar.removeSidebarLink(this);
      if (this.$el && this.$el.parentNode) {
        this.$el.parentNode.removeChild(this.$el);
      }
      if (this.removeLink) {
        this.removeLink(this);
      }
    }
  };
</script>
<style>
  .sidebar-menu-item {
    cursor: pointer;
  }
</style>
