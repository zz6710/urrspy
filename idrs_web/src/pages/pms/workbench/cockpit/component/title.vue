<template>
  <div class="large-screen-title">
    <div class="title-name">
      <img :src="require('@/assets/img/cockpit/title-bg.png')" alt="">
      <div class="name">{{name}}</div>
    </div>
    <div class="title-line"></div>
  </div>
</template>
<script>
export default {
  props: {
    name: {
      type: String,
      default: '运营情况统计分析'
    }
  }
}
</script>
<style lang="scss" scoped>
.large-screen-title {
  padding: 22px 0 0;
  background: linear-gradient(to right, #000, #050d58, #22183d);
  .title-name {
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    height: 24px;
    .name {
      font-size: 24px;
      font-weight: bold;
      color: #fff;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
    img {
      width: 45%;
    }
  }
  .title-line {
    width: 100%;
    height: 3px;
    background: linear-gradient(to right, transparent, #53b4fb, transparent);
    border-radius: 50%;
    margin-top: 16px;
  }
}
</style>
