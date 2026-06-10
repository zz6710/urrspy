<template>
  <div>
    <el-transfer
      filterable
      :filter-method="filterMethod"
      filter-placeholder="请输入成员名称"
      :titles="titlesArr"
      v-model="value"
      :data="transferData">
    </el-transfer>

    <div class="btn-class">
<!--     <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TaClearGroupMemberList.addClearGroupMember" :data-handler="confirmHandler" :data-model="commitParams">-->
     <k-btn v-if="isShow" class="btn-custom-primary" :data-handler="confirmHandler" :data-model="commitParams">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn v-if="isShow" class="btn-custom-plain"  data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
      </k-btn>
      <k-btn v-if="!isShow" class="btn-custom-primary"  data-functype="CLOSE">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
    </div>
  </div>
</template>

<script>
  export default {

     props: {
      groupValue : {
        type:Object
      },
       available: {
         type: Boolean,
         default: false
       },
       closePopupMethod: {
        type: Function
       },
       editActive4: {
        type: Function
       }
    },

    data() {

      return {
        titlesArr:[],
        transferData: [],
        value: [],
        commitParams : {},
        isShow: false,
        filterMethod(query, item) {
          return item.label.indexOf(query) > -1 || item.groupMember.indexOf(query) > -1;
        }
      };
    },

    created(){

    },

    methods:{

      confirmHandler:function(params){

        //在这里构建后台入参对象
        // params["taskGroup"] = this.groupValue.taskGroup;

        // let memberList = [];
        //将选中的数据存入memberList
        this.groupValue.member.forEach(res => {
          res.isGroupMember = '0';
        });
        for(let index=0;index<this.value.length;index++){
          // memberList.push(this.transferData[this.value[index]]);
          this.groupValue.member[this.value[index]].isGroupMember='1';
        }

        // params["memberList"] = JSON.stringify(memberList);
        // return params;
        this.closePopupMethod("memberPopup");
        this.editActive4();
      }

    },

    mounted() {
      this.formData = this.groupValue;
      this.isShow = this.available || (this.groupValue.execTaskType == '6') || (this.groupValue.execTaskType == '5');

      /*this.httpUtil.comnQuery({
          action: 'TaClearGroupMember.findTaClearGroupMembers',
          params: this.groupValue
      }).then(result => {*/
          let allData = this.groupValue.member;
          //循环查询结果，将数据全部放入到数据源中
          for(let i=0;i<allData.length;i++){
            let memberInfo = {
              label: allData[i].groupMemberName,
              key: i,
              groupMember: allData[i].groupMember,
              execTaskType: allData[i].execTaskType,
              taskGroup: allData[i].taskGroup,
              disabled: !this.isShow
            };

            this.transferData.push(memberInfo);

            if(allData[i].isGroupMember=="1"){
              //如果已经加入到组内，则初始化到已选择的成员里
              this.value.push(i);
            }
          }

      // });

      if(this.groupValue.execTaskType == "2"){
        //产品批次
        this.titlesArr=["可选产品","已选产品"];
      }else if(this.groupValue.execTaskType == "3" || this.groupValue.execTaskType == "4"){
        //销售商批次
        this.titlesArr=["可选销售商","已选销售商"];
      }else if(this.groupValue.execTaskType == "5" || this.groupValue.execTaskType == "6"){
        //资管批次
        this.titlesArr=["可选接口文件","已选接口文件"];
      }


    },

  };
</script>
<style lang="scss" scoped>
  .el-transfer-panel {
    width:320px;
    margin-left: 6px;
  };

  .btn-class {
    margin-top: 20px;
    text-align: center;
  }

  ::v-deep .el-transfer{
    width: 762px;
    margin: 0 auto;
  }

  ::v-deep .el-transfer-panel{
    width: 300px;
  }

  ::v-deep .el-transfer-panel__body{
    height: 350px;
  }

  ::v-deep .el-transfer-panel__body > .el-transfer-panel__list{
    height: 300px;
  }

  ::v-deep .el-checkbox{
    margin-left: auto;
  }
</style>
