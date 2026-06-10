<template>
  <div class="k-channel">
    <div class="k-channel-btns">
      <k-btn class="btn-custom-plain" @click="disabled=false">修改</k-btn>
      <k-btn class="btn-custom-plain" @click="save">保存</k-btn>
      <k-btn class="btn-custom-plain" @click="updateChannel">更新渠道</k-btn>
    </div>
    <md-card class="k-channel-container k-card">
      <md-card-header class="md-card-header-text md-card-header-green">
        <div class="card-icon" :style="iconStyle">
          <md-icon>assignment</md-icon>
        </div>
      </md-card-header>
      <div class="table-box">
        <el-table :data="dataList">
          <el-table-column align="center" prop="name" label="渠道名称">
            <template slot-scope="scope">
              <div class="flex">
                <el-checkbox :disabled="disabled" v-model="scope.row.channelName.checked">
                  {{scope.row.channelName.name}}
                </el-checkbox>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="发布方式">
            <template slot-scope="scope">
              <div class="flex publish-list">
                <div class="publish-item" v-for="(item, index) in scope.row.publishStyle" :key="index">
                  <el-checkbox :disabled="disabled" v-model="item.checked">{{item.name}}</el-checkbox>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="发布">
            <template slot-scope="scope">
              <div class="flex">
                <k-btn class="btn-custom-plain" @click="publishRow(scope.row)">发布</k-btn>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </md-card>
  </div>
</template>
<script>
export default {
  props: {
    dataList: {
      type: Array,
      default: ()=>{
        return [
          {
            id: 1,
            channelName: {
              name: '中国光大银行',
              checked: false
            },
            publishStyle: [
              {
                name: '发送邮件',
                checked: false
              },
              {
                name: '系统对接',
                checked: true
              }
            ]
          },
          {
            id: 2,
            channelName: {
              name: '中国光大银行',
              checked: false
            },
            publishStyle: [
              {
                name: '发送邮件',
                checked: false
              },
              {
                name: '系统对接',
                checked: true
              }
            ]
          }
        ]
      }
    }
  },
  data() {
    return {
      disabled: true
    }
  },
  computed: {
    iconStyle() {
      let iconStyle = {};
      iconStyle.background = this.$store.state.system.cardBackground
      return iconStyle;
    }
  },
  methods: {
    publishRow(row) {
      console.log(row);
    },
    save() {
      this.disabled = true
    },
    updateChannel() {}
  }
}
</script>
<style scoped lang="scss">
.k-channel {
  .k-channel-btns {
    margin-bottom: 30px;
  }
  .k-channel-container {
    .table-box {
      padding: 15px 20px;
    }
  }
  .publish-list {
    .publish-item {
      margin: 0 15px 0 0;
      &:last-child {
        margin-right: 0;
      }
    }
  }
  .el-table {
    &::before {
      height: 0;
    }
    /deep/ td.el-table__cell {
      border: 0;
    }
    /deep/ th.el-table__cell.is-leaf {
      border: 0;
    }
  }
}
.flex {
  display: flex;
  justify-content: center;
}

.el-checkbox {
  font-weight: normal;
  /deep/ .el-checkbox__input.is-checked+.el-checkbox__label {
    color: #606266 !important;
  }
}

</style>
