<template>
  <div>
    <div style="margin-left: 20px;margin-bottom: 10px">
      请选择人员：
    </div>
    <div style="display: flex;flex-direction: row">
      <div style="width: 40%;margin-left: 20px;">
        <div style="margin-bottom: 10px;">
          <el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
        </div>

        <el-tree
          class="filter-tree"
          :data="dataList"
          node-key="id"
          show-checkbox
          :props="defaultProps"
          :filter-node-method="filterNode"
          ref="tree">
        </el-tree>
      </div>
      <div class="select-btn">
        <div style="display: flex;flex-direction: column">
          <el-button style="margin-left: 10px;" @click="getCheckedNodes">确认 ></el-button>
          <el-button style="margin-top: 10px;" @click="resetChecked">清空</el-button>
        </div>
        <div>

        </div>
      </div>

      <div class="user" >
        <span>已选人员：</span>
        <div style="margin-top: 10px">
          <span class="user-tag" v-for="(item,index) in selectedUser">
            {{item.name}}
            <i class="el-tag__close el-icon-close" @click="deleteSelectedUser(index)"></i>
          </span>
          <div style="margin-top: 10px">

          </div>
        </div>
      </div>
    </div>

    <div style="text-align: center;margin-top: 40px">
      <k-btn class="md-success" data-functype="POPUP" data-descript="分享" data-size="mini" @click="confirmShare()">
<!--        <md-icon md-src="/static/svg/confirm.svg"></md-icon>-->
        确认分享</k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
    </div>
  </div>

</template>

<script>
import Tools from '@/utils/tools.js';
export default {
  name: "PersonTree",
  props:{
    processData:{
      type:Array,
      required:true
    }
  },
  data() {
    return {
      selectedUser: [],
      filterText: '',
      dataList:[],
      defaultProps: {
        children: 'userList',
        label: 'name'
      },
      currentUserId:localStorage.getItem("userid")
    };
  },
  created() {
    this.getDeptUserTree();
  },
  methods: {
    confirmShare(){
      let users = this.selectedUser;
      let processInstanceId;
      if(this.processData.processId){
        processInstanceId = this.processData.processId;
      }else{
        processInstanceId = this.processData.id;
      }
      let user = this.currentUserId;
      console.log('获取的流程实例id',processInstanceId)
      let userList = [];
      for (let i = 0; i<users.length;i++){
        userList.push(users[i].userId);
      }
      console.log('选中的用户：',userList);
      if (users.length===0){
        Tools.alert('请选择分享人员，并确认！',"danger");
      }else {
        users = userList.toString()
        //分享方法
        this.httpUtil
          .ajax({
            url: "wf/wf/share/saveShare.json",
            params: {
              processInstanceId:processInstanceId,
              users: users,
              user:user
            },
          }).then(res => {
            if (res.status==='200'){
              Tools.alert('分享成功')
              this.shareSuccess();
            }else {
              Tools.alert('分享失败，请重试！')
            }
        });
      }
    },
    shareSuccess(){
      this.$emit('submitClose', '1')
    },
    getDeptUserTree(){
      this.httpUtil
        .ajax({
          url: "wf/wf/department/getAllDeptAndUser.json",
          params: {
          },
        }).then(res => {
          this.dataList = res.rows;
          console.log(this.dataList);
      });
    },
    //获取选择的节点
    getCheckedNodes() {
      this.selectedUser = [];
      let selectedData = [];
      selectedData = this.$refs.tree.getCheckedNodes();
      console.log(selectedData, 'ss')
      for (let i=0;i<selectedData.length;i++){
        let deptNo = selectedData[i].deptNo;
        if (deptNo===undefined){
          this.selectedUser.push(selectedData[i]);
        }
      }
    },
    //清空
    resetChecked() {
      this.$refs.tree.setCheckedKeys([],true);
      this.selectedUser = [];
    },
    //根据条件过滤
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    deleteSelectedUser(index) {
      this.selectedUser.splice(index, 1)
      this.$refs.tree.setCheckedKeys(this.selectedUser.map(item=>item.id),true);
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
}
</script>

<style scoped>
  .select-btn{
    width: 10%;
    margin-left: 20px;
    margin-right: 10px;
    display: flex;
    justify-self: center;
    align-items: center;
  }
  .user{
    border: 1px solid #c0c0c0;
    width: 40%;
    border-radius: 15px;
    padding: 10px;
    background-color: white;
  }
  .user-tag{
    line-height: 20px;
    display: inline-block;
    /*height: 60px;*/
    /*width: 80px;*/
    border: 1px solid skyblue;
    margin-left: 5px;
    margin-right: 5px;
    font-size: 14px;
    /*padding: 5px;*/
    padding-left: 5px;
    padding-right: 5px;
    margin-bottom: 5px;
    background-color: #0b9ce3;
    color: white;
    border-radius: 10px;
  }
  .el-icon-close {
    cursor: pointer;
  }
</style>
