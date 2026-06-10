<template>
  <div
    :class="[
      { open: isOpen },
      { dropdown: direction === 'down' },
      { dropup: direction === 'up' }
    ]"
    @click="toggleDropDown"
    @contextmenu.prevent="toggleContextMenuDropDown"
    v-click-outside="closeDropDown"
  >
    <slot name="title">
      <a
        class="dropdown-toggle"
        data-toggle="dropdown"
        href="javascript:void(0)"
      >
        <i :class="icon"></i>
        <p class="notification">
          {{ title }}
          <b class="caret"></b>
        </p>
      </a>
    </slot>
    <slot></slot>
  </div>
</template>
<script>
export default {
  name: "drop-down",
  props: {
    direction: {
      type: String,
      default: "down"
    },
    multiLevel: {
      type: Boolean,
      default: false
    },
    toggleType: {
      type: String,
      default: "click"
    },
    title: String,
    icon: String
  },
  data() {
    return {
      isOpen: false
    };
  },
  methods: {
    toggleDropDown() {
      if(this.toggleType == "click"){
        if (this.multiLevel) {
          this.isOpen = true;
        } else {
          this.isOpen = !this.isOpen;
        }
      }
    },
    toggleContextMenuDropDown() {
      if(this.toggleType == "contextmenu"){
        if (this.multiLevel) {
          this.isOpen = true;
        } else {
          this.isOpen = !this.isOpen;
        }
      }
    },
    closeDropDown() {
      this.isOpen = false;
    }
  }
};
</script>
