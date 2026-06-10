<template>
  <div class="myFlow">
    <div class="myFlow_block01">
      <div class="myFlow_block01_line1">
        <div>流程阶段:</div>
        <div class="tagBlock" :class="{ 'tagBlock-active': isActive1 === item.id }" v-for="item in flowStage"
          :key="item.id" @click="changeTab(1, item.id)">
          <div class="spot" v-if="item.showSpot">
            <span style="display: inline-block;transform: scale(0.8);">{{ item.num }}</span>
          </div>
          {{ item.value }}
        </div>
      </div>
      <div class="myFlow_block01_line1">
        <div>产品分类:</div>
        <div class="tagBlock" :class="{ 'tagBlock-active': isActive2 === item.id }" v-for="item in flowClassification"
          :key="item.id" @click="changeTab(2, item.id)">
          {{ item.value }}
        </div>
      </div>
      <div class="myFlow_block01_line1">
        <div>流程状态:</div>
        <div class="tagBlock" :class="{ 'tagBlock-active': isActive3 === item.id }" v-for="item in flowStatus"
          :key="item.id" @click="changeTab(3, item.id)">
          <div class="spot" v-if="item.showSpot">
            <span style="display: inline-block;transform: scale(0.8);">{{ item.num }}</span>
          </div>
          {{ item.value }}
        </div>
        <k-form :data-col="1">
          <k-form-item label="产品代码">
            <k-field-select v-model="prodCode" data-action="DirecpPodProcess.findDirecpPodProcessProdCode"
              data-display-field="prodCode,prodName" data-value-field="prodCode" @data-on-change="changeTab()" />
          </k-form-item>
        </k-form>

      </div>
    </div>
    <div class="myFlow_block02">
      <k-grid ref="T8ProdAccountFindInfo" data-action="DirecpPodProcess.findDirecpPodProcess"
        @data-row-select="selectRow" data-operate-width="200px" :data-operate-column="false">
        <!-- :data-operate-column="false" -->
        <k-grid-column data-header="产品代码" data-name="prodCode" />
        <k-grid-column data-header="流程阶段" data-name="name" />
        <k-grid-column data-header="当前节点" data-name="taskGroupName" />
        <k-grid-column data-header="产品名称" data-name="prodName" />
        <k-grid-column data-header="产品分类" data-name="prodClassify" data-dict="t8_prod_classify" />
        <k-grid-column data-header="流程状态">
          <template slot-scope="scope">
            <span v-if="scope.row.row.execTaskType == '1'" style="color: green;font-size: 12px;">办结</span>
            <k-btn v-if="scope.row.row.execTaskType == '2'" class="md-simple"
              :data-descript="'办结'+scope.row.row.prodName+'产品的'+scope.row.row.taskGroupName+'流程节点'"
              data-functype="SUBMIT" data-size="mini" :data-confirm="true" data-target="T8ProdAccountFindInfo"
              data-url="server/form/PmsApp/home/workflow/updateUserWorkFlow.json">
              <font color="#00bcd4">待办</font>
            </k-btn>
          </template>
        </k-grid-column>
      </k-grid>
    </div>
  </div>
</template>

<script>
  import {
    assign
  } from "lodash";

  export default {
    data() {
      return {
        isActive1: "00",
        isActive2: "00",
        isActive3: "00",
        prodCode: "",
        selectRowData: {},
        flowStage: [{
            id: "00",
            value: "不限",
          },
          {
            id: "产品创设",
            value: "产品创设",
          },
          {
            id: "产品报备",
            value: "产品报备",
          },
          {
            id: "产品发行",
            value: "产品发行",
          },
          {
            id: "产品运营",
            value: "产品运营"
          },
          {
            id: "产品到期",
            value: "产品到期"
          },
        ],
        flowStatus: [{
            id: "00",
            value: "不限",
          },
          {
            id: "2",
            value: "待办"
          },
          {
            id: "1",
            value: "办结"
          }
        ],
        flowClassification: [{
            id: "00",
            value: "不限",
          },
          {
            id: "01",
            value: "固收产品",
          },
          {
            id: "02",
            value: "权益产品",
          },
          {
            id: "03",
            value: "商品及衍生品",
          },
          {
            id: "04",
            value: "混合类的产品",
          },
          {
            id: "05",
            value: "无关产品",
          },
        ],
        userid: localStorage.userid,
      };
    },

    methods: {
      changeTab(flag, id) {
        if (flag == 1) {
          this.isActive1 = id;
        } else if (flag == 2) {
          this.isActive2 = id;
        } else if (flag == 3) {
          this.isActive3 = id;
        }

        let params = {};

        if (this.isActive1 != "00") {
          params.name = this.isActive1;
        }

        if (this.isActive2 != "00") {
          params.prodClassify = this.isActive2;
        }

        if (this.isActive3 != "00") {
          params.execTaskType = this.isActive3;
        }

        if (this.prodCode) {
          params.prodCode = this.prodCode;
        }

        this.$refs.T8ProdAccountFindInfo.load(params);
      },
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row);
      },
      openFlowPage() {
        if (this.selectRowData.processType == 2 && this.selectRowData.processStatus == 1 && this.selectRowData.result ==
          "1") { //待办

        } else { //打开详情
          console.log('表单数据参数', this.selectRowData.processId);
          this.httpUtil.ajax({
            url: "/wf/formInfo/getFormInfo.json",
            params: {
              processId: this.selectRowData.processId
            },
          }).then(res => {
            console.log('表单数据', res)
            this.taskDetail = res.data;
            if (this.taskDetail && this.taskDetail.length != 0) {
              this.formType = this.taskDetail[0].formType;
            } else {
              this.formType = '2';
            }

            this.$refs.detailPopup.popup();

          })
        }
        console.log(123);
        return false;
      },
      loadCount() {

      }
    },
  };
</script>

<style lang="scss" scoped>
  .myFlow_block01_line1 {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-wrap: wrap;
    margin: 15px 0;
  }

  .spot {
    position: absolute;
    width: 14px;
    height: 14px;
    border-radius: 50%;
    top: 5px;
    background: #ff9e00;
    font-size: 12px;
    color: #fff;
    right: 2px;
    font-size: 12px;
    line-height: 14px;
  }

  .row_drop {
    .el-icon-d-arrow-right {
      font-size: 20px;
      transform: rotate(90deg);
    }
  }

  .myFlow_block01 {
    margin-bottom: 50px;
  }

  .myFlow_block02_state {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .dorp {
    // 已完成
    width: 8px;
    height: 8px;
    background: #666;
    border-radius: 50%;
    margin-right: 10px;
  }

  .dorpColor0 {
    // 我发起
    background: #00bcd4;
  }

  .dorpColor1 {
    // 代办
    background: #e53929;
  }

  .dorpColor2 {
    // 处理中
    background: #4caf50;
  }
</style>
