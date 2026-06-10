<template>
  <div
    class="large-screen-btn"
    :class="type"
    :style="{minWidth: type == 'type2' ? 'auto' : minWidth}">
    <div class="name">{{name}}</div>
    <div class="date" v-if="name == '产品存续规模'">{{date}}</div>
  </div>
</template>
<script>
import moment from 'moment'
import Tools from "@/utils/tools";
export default {
  props: {
    type: {
      type: String,
      default: 'type1'
    },
    name: {
      type: String,
      default: '存续规模'
    },
    minWidth: {
      type: String,
      default: '200px'
    }
  },
  data() {
    return {
      date: moment().format('YYYY-MM-DD')
    }
  },
  created() {
    // 系统日期
    /*
    this.httpUtil.sysImpDate().then((res) => {
      if (res) {
        console.log("res=:>>>>>>>>>>>>>>>>>>>>>>>>>",res);
        this.date = Tools.formatDate(res);
      }
    });

    */
    this.date = Tools.formatDate('20221121');
  },
}
</script>
<style lang="scss" scoped>
.large-screen-btn {
  background: linear-gradient(to right, #4869ea, transparent);
  padding: 2px;
  border-radius: 20px;
  display: inline-block;
  position: relative;
  .name {
    color: #fff;
    font-size: 12px;
    font-weight: bold;
    padding: 5px 10px;
    border-radius: 20px;
  }
  &.type1 {
    .name {
      background: linear-gradient(to right, #1f1b49, #14203f);
    }
  }
  &.type2 {
    background: none;
    box-shadow: inset 0px 0px 16px 3px #58c4d0;
    padding: 0 10px;
    border: 2px solid #58c4d0;
  }
  .date {
    color: #fff;
    position: absolute;
    top: 50%;
    right: 20px;
    transform: translate(0,-50%);
  }
}
</style>
