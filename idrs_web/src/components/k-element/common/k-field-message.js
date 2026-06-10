export default function() {
  //测试专用
  return {
    methods: {
      succmessage(message) {
        this.$message.success(message)
      },
      errmessage(message) {
        this.$message.error(message)
      },
      warnmessage(message) {
        this.$message.warning(message)
      },
      infomessage(message) {
        this.$message.info(message)
      },

    }
  };
}
