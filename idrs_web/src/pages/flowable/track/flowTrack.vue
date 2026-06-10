<!--流程追踪 -->
<template>
  <div class="py-page">
    <div class="py-page-container">
      <el-tabs v-model="activeName">
        <el-tab-pane label="我发起的" name="myLaunch">
          <myLaunch></myLaunch>
        </el-tab-pane>
        <el-tab-pane label="我审批的" name="myApproval">
          <myApproval></myApproval>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import myApproval from "./myApproval.vue";
import myLaunch from "./myLaunch.vue";

export default {
	name: "track",
	components: { myApproval, myLaunch },
	data() {
		return {
			activeName: "myLaunch",
		};
	},
};
</script>

