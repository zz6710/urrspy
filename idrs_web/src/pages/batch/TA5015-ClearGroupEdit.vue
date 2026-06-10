<template>
  <div id="body">
    <div class="bg-bottom" :class="{'noTime':noTime}" :style="'background-color: ' +bgColor">
      <div class="buttonBottom" v-if="!noTime" :style="'background-color: ' +bgColor">
        <div class="triangle"></div>
      </div>
      <div class="buttonRight" v-if="!noTime" :style="'background-color: ' +bgColor">
        <div class="triangle"></div>
      </div>
      <div class="text delete" :class="available? '':'disabled'" @click="minuGroup(isFirstDay,y,x,noTime,clearGroup)">×</div>
      <div class="edit" :class="available? '':'disabled'" @click="editGroup(clearGroup)">
        ✎
      </div>
      <div class="bigTitle">
        <div class="iconDiv">
          <md-icon v-if="this.clearGroup.execTaskType == 1" md-src="/static/svg/clear/settingIcon.svg"></md-icon>
          <md-icon v-if="this.clearGroup.execTaskType == 2" md-src="/static/svg/clear/productIcon.svg"></md-icon>
          <md-icon v-if="this.clearGroup.execTaskType == 3||this.clearGroup.execTaskType == 4" md-src="/static/svg/clear/distributorIcon.svg"></md-icon>
          <md-icon v-if="this.clearGroup.execTaskType == 5||this.clearGroup.execTaskType == 6" md-src="/static/svg/clear/ZGIcon.svg"></md-icon>
        </div>
        <el-tooltip effect="dark" :content="clearGroup.taskGroupName" placement="top-start">
          <div class="title text iconText">{{ clearGroup.taskGroupName | filterName }}</div>
        </el-tooltip>
      </div>
      <div class="editField">
        <div class="editButton text" v-if="clearGroup.execTaskType!=='2'"
             @click="taskPopup(clearGroup)">任务
        </div>
        <div class="editButton text" v-if="'3,4,5,6'.indexOf(clearGroup.execTaskType) >= 0"
             @click="memberPopup(clearGroup)">成员
        </div>
      </div>
      <div :class="available? 'plusGloup':'plusGloupDisable'" @click="addClearGroup(false,y,x+1,isFirstDay)" slot="reference" v-if="!noTime"></div>
    </div>

  </div>
</template>

<script>
// import KBtn from "@/components/k-element/k-btn/k-btn";
import Tools from "@/utils/tools";

export default {
  name: "ClearGroupEdit",
  props: {
    taskPopup: Function,
    memberPopup: Function,
    editGroup: Function,
    available: {
      type: Boolean,
      default: false
    },
    minuGroup: {
      type: Function
    },
    clearGroup: {
      type: Object
    },
    isFirstDay: {
      type: Boolean
    },
    x: {
      type: Number
    },
    y: {
      type: Number
    },
    noTime: {
      type: Boolean
    },
    addClearGroup: {
      type: Function
    },
    editActive2: {
      type: Function
    }
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      taskGroupName: '',
      execTaskType: '1',
      bgColor: '#00BCD4'
    }
  },
  methods: {},
  filters: {
    filterName(name) {
      if (name.length > 6) {
        return name.substr(0, 6) + "...";
      } else {
        return name;
      }
    }
  },
  created() {
    switch (this.clearGroup.execTaskType){
      case '1':
        this.bgColor= '#FBBB56';
        break;
      case '2':
        this.bgColor= '#ff8240';
        break;
      case '3':
        this.bgColor= '#b7d28d';
        break;
      case '4':
        this.bgColor= '#b7d28d';
        break;
      case '5':
        this.bgColor= '#d9b8f1';
        break;
      case '6':
        this.bgColor= '#d9b8f1';
        break;
    }
  }
}
</script>

<style scoped>

.bg-bottom {
  width: 136px;
  height: 70px;
  margin-right: 0;
  box-shadow: 0px 1px 4px 0px rgba(117, 223, 237, 0.74);
  border-radius: 5px;
  position: relative;
}

.editField {
  position: absolute;
  bottom: 5px;
  height: 20px;
  width: 100%;
  text-align: center;
  z-index: 2;
  line-height: 20px;
}

.editButton {
  margin-right: 3px;
  margin-left: 3px;
  border-radius: 3px;
  color: #00BCD4;
  width: 50px;
  height: 20px;
  background-color: #ffffff;
  display: inline-block;
}

.editButton:hover {
  background-color: #19A6EB;
  color: #ffffff;
  cursor: pointer;
}

.noTime {
  margin-left: 10px;
}

.plusGloup {
  position: absolute;
  right: -73px;
  width: 20px;
  height: 20px;
  border: 2.5px solid #00BCD4;
  background: #FFFFFF;
  border-radius: 10px;
  z-index: 3;
}

.plusGloup::before {
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid #00BCD4;
}

.plusGloup::after {
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid #00BCD4;
}

.plusGloupDisable {
  position: absolute;
  right: -73px;
  width: 20px;
  height: 20px;
  border: 2.5px solid #00BCD4;
  background: #FFFFFF;
  border-radius: 10px;
  z-index: 3;
  cursor: not-allowed;
}

.plusGloupDisable::before {
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid #00BCD4;
}

.plusGloupDisable::after {
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid #00BCD4;
}

.plusGloup:hover {
  width: 20px;
  height: 20px;
  border-radius: 10px;
  border: 0;
  background-color: #00BCD4;
  cursor: pointer;
}

.plusGloup:hover::after {
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  height: 10px;
  border-left: 2.5px solid white;
}

#body {

}

.plusGloup:hover::before {
  content: '';
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 10px;
  border-top: 2.5px solid white;
}

.buttonBottom {
  width: 24px;
  height: 12px;
  border-bottom-left-radius: 24px;
  border-bottom-right-radius: 24px;
  position: absolute;
  bottom: -12px;
  left: 50%;
  transform: translateX(-50%);
}

.buttonRight {
  width: 12px;
  height: 24px;
  border-top-right-radius: 24px;
  border-bottom-right-radius: 24px;
  position: absolute;
  right: -12px;
  top: 50%;
  transform: translateY(-50%);
}

.buttonBottom .triangle {
  border-style: solid;
  border-width: 7px 7px 7px 7px;
  border-color: #FFFFFF transparent transparent transparent;
  width: 0;
  height: 0;
  position: absolute;
  left: 50%;
  transform: translateX(-45%);
  top: 1.5px;
}

.buttonRight .triangle {
  border-style: solid;
  border-width: 7px 7px 7px 7px;
  border-color: transparent transparent transparent #FFFFFF;
  width: 0;
  height: 0;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  left: 1.5px;
}

.buttonRight, .buttonBottom {
  cursor: pointer;
}

.delete {
  border: 0;
  position: absolute;
  z-index: 3;
  right: 3px;
  top: 3px;
  width: 13px;
  height: 13px;
  line-height: 14px;
  text-align: center;
  border-radius: 6.5px;
  font-size: 16px;
  color: #00BCD4;
  background-color: white;
}

.disabled {
  cursor: not-allowed !important;
}

.edit {
  border: 0;
  position: absolute;
  z-index: 3;
  right: 20px;
  background-color: #FFFFFF;
  color: #00BCD4;
  top: 3px;
  width: 13px;
  height: 13px;
  line-height: 14px;
  font-size: 3px;
  text-align: center;
  border-radius: 6.5px;
}

.edit:hover {
  color: #000000;
  cursor: pointer;
}

.delete:hover {
  color: red;
  /*background-color: white;*/
  cursor: pointer;
}

.titleIcon {
  display: inline-block;
  width: 13px;
  height: 15px;
  transform: translateY(-2px);
}

.title {
  color: white;
  display: inline-block;
  font-size: 15px;
  line-height: 25px;
}

.bigTitle {
  height: 25px;
  width: 135px;
  position: absolute;
  top: 16px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  z-index: 2;
}

.iconText {
  vertical-align: top;
}

.iconDiv {
  border-radius: 50%;
  display: inline-block;
  background-color: #ffffff;
  height: 25px;
  width: 25px;
}


.text {
  /*文字不能被选中*/
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  cursor: auto;
}

</style>
