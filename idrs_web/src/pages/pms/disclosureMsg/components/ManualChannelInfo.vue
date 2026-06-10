<template>
  <div class="k-channel">
    <div class="k-channel-btns">
<!--      <k-btn class="btn-custom-plain" @click="disabled=false">修改</k-btn>-->
<!--      <k-btn class="btn-custom-plain" @click="save">保存</k-btn>-->
<!--      <k-btn class="btn-custom-plain" @click="updateChannel">更新发布渠道</k-btn>-->
    </div>
    <md-card class="k-channel-container k-card">
      <md-card-header class="md-card-header-text md-card-header-green">
        <div class="card-icon" :style="iconStyle">
          <md-icon>assignment</md-icon>
        </div>
      </md-card-header>
      <div class="table-box">
        <el-table :data="dataList" ref="channelGrid">
          <el-table-column align="center" prop="name" label="渠道名称">
            <template slot-scope="scope">
              <div class="flex">
                <el-checkbox :disabled="disabled" v-model="scope.row.channelName.checked" @change="handleClickChannelChange(scope.row)">
                  {{scope.row.channelName.name}}
                </el-checkbox>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="发布方式">
            <template slot-scope="scope">
              <div class="flex publish-list">
                <div class="publish-item" v-for="(item, index) in scope.row.publishStyle" :key="index">
                  <el-checkbox :disabled="item.disabled" v-model="item.checked" @change="handleClickChange(scope.row)">{{item.name}}</el-checkbox>
                </div>
              </div>
            </template>
          </el-table-column>
<!--          <el-table-column align="center" label="发布">-->
<!--            <template slot-scope="scope">-->
<!--              <div class="flex">-->
<!--                <k-btn class="btn-custom-plain" @click="publishRow(scope.row)">发布</k-btn>-->
<!--              </div>-->
<!--            </template>-->
<!--          </el-table-column>-->
        </el-table>
      </div>
    </md-card>
  </div>
</template>
<script>
import Tools from "@/utils/tools";

export default {
  props: {
    disclosureNoticeData: {},

  },
  data() {
    return {
      disabled: false,
      dataList: [],
    }
  },
  computed: {
    iconStyle() {
      let iconStyle = {};
      iconStyle.background = this.$store.state.system.cardBackground
      return iconStyle;
    }
  },
  created() {
    console.log("channelinfo:disclosureNoticeData>",this.disclosureNoticeData);
    console.log("channelinfo:channelInfoList>",this.channelInfoList);
    // this.$nextTick(()=>{
    //   this.findChannelInfo();
    // })
  },
  activated() {
    //  this.$nextTick(()=>{
    //   this.findChannelInfo();
    // })
  },
  watch:{
    'channelInfoList'(value) {
      console.log("111",this.channelInfoList);
      this.$nextTick(() => {
        this.channelInfoList.forEach(c => {
          this.dataList.push(c);
        });
      })
      console.log('watch_dataList',this.dataList)
    }
  },
  methods: {
    publishRow(row) {
      this.$set(row,'disclosureNoticeId',this.$route.query.id);
     this.httpUtil.comnUpdate({
        action: 'DisclosureNoticeChannel.sendChannelsEmails',
        params: row,
        successAlert: true
      }).then(data => {
        //刷新
          this.$parent.tabSelect2(3);
      });

    },
    findChannelInfo(){
      console.log(this.disclosureNoticeData.id);
      console.log("channelInfoList",this.channelInfoList);
        this.httpUtil.comnQuery({
          action: 'DisclosureNoticeChannel.findChannelRecordByProdCode',
          params: {
            'prodCode': this.disclosureNoticeData.prodCode,
            'disclosureType':this.disclosureNoticeData.disclosureType,
            'disclosureSonType':this.disclosureNoticeData.disclosureSonType,
          },
          successAlert: false
        }).then(data => {
          console.log("data=:>>>",data.returndata.channelList);
          this.dataList = data.returndata.channelList;
        });

    },
    save() {
      this.disabled = true;
      let res = Tools.json2str(this.dataList);
      console.log("res=?>>>>>",res);
      this.httpUtil.comnUpdate({
        action: 'DisclosureNoticeChannel.updateChannelRecord',
        params: {dataList: res,disclosureNoticeId: this.disclosureNoticeData.id},
      }).then(data => {
        this.$parent.tabSelect2(3);
      });
    },
    updateChannel() {

      this.httpUtil.comnUpdate({
        action: 'T8DisclosureNoticeChannel.autoUpdate',
        params: {'disclosureNoticeId': this.$route.query.id},
        successAlert: true
      }).then(data => {
      this.$parent.tabSelect2(3);
      });
    },
    handleClickChange(row){
      console.log("row=>>>>>",row);
      let j=0;
      for(let i=0;i<row.publishStyle.length; i++){
        if(row.publishStyle[i].checked){
          row.channelName.checked=true;
          j=j+1;
        }
      }
      if(j>0){
        row.channelName.checked=true;
      }else{
        row.channelName.checked=false;
      }
    },
    handleClickChannelChange(row){
      console.log("row=>>>>>",row);
      if(row.channelName.checked==false){
        for(let i=0;i<row.publishStyle.length; i++){
          row.publishStyle[i].checked=false;
        }
      }
    },
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
