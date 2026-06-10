<template>
  <div>
    <k-field-code>
      <textarea>
data-on-value：选中的值
data-off-value：不选中的值
data-on-change：修改事件
          </textarea>
    </k-field-code>

   <k-field-bswitch v-model="bswitch" @data-on-change="change" data-on-value="open" data-off-value="close"></k-field-bswitch>
     <k-field-code>
      <textarea>
代码：
<k-field-bswitch v-model="bswitch" @data-on-change="change" data-on-value="open" data-off-value="close"></k-field-bswitch>
      </textarea>
    </k-field-code>
  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'
  export default {
    data() {
      return {
        bswitch: 100
      }
    },
    methods: {
      change(value) {
        console.info("change被调用了" + value);
      }
    }
  }
</script>
<style>
</style>
