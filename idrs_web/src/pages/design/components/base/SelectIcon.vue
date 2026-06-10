<template>
  <div class="">
    <el-collapse v-model="activeNames" accordion>
      <el-collapse-item title="icon图标" name="1">
        <template v-for="icon in iconArr">
          <i :key="icon" v-if="icon" v-bind:class="icon" class="show_icon" @click="handleSelect({type:'icon', value: icon})"></i>
        </template>
      </el-collapse-item>
      <el-collapse-item title="MD图标" name="2">
        <div v-for="icon in mdIconArr" @click="handleSelect({type:'md', value: icon})" :key="icon" style="display:inline-block">
          <md-icon class="show_icon" style="padding:20px" >{{icon}}</md-icon>
        </div>
      </el-collapse-item>
      <el-collapse-item title="Svg图标" name="3">
          <div v-for="icon in mdIconSvgArr" :key="icon" @click="handleSelect({type:'svg', value: icon})" style="display:inline-block">
            <md-icon :key="icon" :md-src="icon" style="display:inline-block;margin:10px" />
          </div>
      </el-collapse-item>
      <el-collapse-item title="手动设置" name="4">
        <el-form ref="form2" :model="icon" label-width="80px">
          <el-form-item label="图标类型" prop="type">
            <el-select v-model="icon.type" placeholder="请选择图标类型">
              <el-option label="icon图标" value="icon"></el-option>
              <el-option label="MD图标" value="md"></el-option>
              <el-option label="Svg图标" value="svg"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="图标名称" prop="value">
            <el-input v-model="icon.value" placeholder="请输入图标名称或路径"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSelect(icon)">确定</el-button>
            <!-- <el-button @click="resetForm('form2')">重置</el-button> -->
          </el-form-item>
        </el-form>        
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>

export default {
  props: {

  },
  data () {
    return {
      activeNames: ['1'],
      iconArr: ["el-icon-question","el-icon-info","el-icon-remove","el-icon-circle-plus","el-icon-success","el-icon-error","el-icon-zoom-in","el-icon-zoom-out","el-icon-remove-outline","el-icon-circle-plus-outline","el-icon-circle-check","el-icon-circle-close","el-icon-s-help","el-icon-help","el-icon-minus","el-icon-plus","el-icon-check","el-icon-close","el-icon-picture","el-icon-picture-outline","el-icon-picture-outline-round","el-icon-upload","el-icon-upload2","el-icon-download","el-icon-camera-solid","el-icon-camera","el-icon-video-camera-solid","el-icon-video-camera","el-icon-message-solid","el-icon-bell","el-icon-s-cooperation","el-icon-s-order","el-icon-s-platform","el-icon-s-fold","el-icon-s-unfold","el-icon-s-operation","el-icon-s-promotion","el-icon-s-home","el-icon-s-release","el-icon-s-ticket","el-icon-s-management","el-icon-s-open","el-icon-s-shop","el-icon-s-marketing","el-icon-s-flag","el-icon-s-comment","el-icon-s-finance","el-icon-s-claim","el-icon-s-custom","el-icon-s-opportunity","el-icon-s-data","el-icon-s-check","el-icon-s-grid","el-icon-menu","el-icon-share","el-icon-d-caret","el-icon-caret-left","el-icon-caret-right","el-icon-caret-bottom","el-icon-caret-top","el-icon-bottom-left","el-icon-bottom-right","el-icon-back","el-icon-right","el-icon-bottom","el-icon-top","el-icon-top-left","el-icon-top-right","el-icon-arrow-left","el-icon-arrow-right","el-icon-arrow-down","el-icon-arrow-up"],
      mdIconArr: ["add", "search", "delete", "edit", "cancel"],
      mdIconSvgArr: ["/static/svg/icon/add.svg", "/static/svg/icon/settings.svg"],
      icon: {
        type: "icon",
        value: "",
      }
    }
  },
  created() {
  },
  mounted () {
  },
  methods: {
    handleSelect: function(selectIcon){
      console.log("select icon ", selectIcon);
      this.$emit("select", selectIcon);
    }
  },
  watch: {

  }
}
</script>

<style lang="scss" scoped>
.show_icon{
  font-size:20px;
  padding:10px;

  &:hover {
    color: #5cb6ff;
  }
}

</style>
