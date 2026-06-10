<template>
    <p class="page-container">404 page not found</p>
</template>
<script>
  export default {
    mounted(){
      console.log("开始跳转到主页");
      // setTimeout(()=>{
      //   this.$router.push({ path: '/login' })
      // },2000);
    }
  }
</script>

<style lang="scss" scoped>
.page-container {
  font-size: 20px;
  text-align: center;
  color: rgb(192, 204, 218);
}
</style>
