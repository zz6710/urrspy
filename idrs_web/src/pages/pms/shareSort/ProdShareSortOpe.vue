<template>
  <div>
    <k-form-search-customize data-target="prodShareSortGrid" v-model="queryParam">
      <k-form-item label="份额名称">
        <k-field-select v-model="queryParam.id" data-action="ProdShareSort.findSortsAndBakSortsNoPage"
                        data-display-field="realShareName" data-value-field="id"
                        :data-params="{t8ProdInfoId:t8ProdInfoId}"/>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" v-if="global.getProdIfUser(t8ProdInfoId)&&
             global.isShowAuthorityButton('ProdShareSortBak.insertProdShareSortBak')"
             :data-handler="addHandler">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="approvalHandler"
             v-if="global.getProdIfUser(t8ProdInfoId)&&
             global.isShowAuthorityButton('ProdShareSortBak.approvalShareSort')">
        送审
      </k-btn>
            <k-btn slot="button" class="btn-custom-plain" data-functype="PAGE"  data-target="/main/pms/shareSort/ProdShareSort">
              <i class="icon-reset" />返回</k-btn>
    </k-form-search-customize>
    <k-grid ref="prodShareSortGrid" data-action="ProdShareSort.findSortsAndBakSorts" data-operate-column="true" data-fixed="right"
            :data-params="{t8ProdInfoId:t8ProdInfoId}" data-operate-width="120px">
      <k-grid-column data-header="产品代码" data-name="prodCode" data-width="150"></k-grid-column>
      <k-grid-column data-header="产品名称" data-name="prodName" data-width="300"></k-grid-column>
      <k-grid-column data-header="份额名称" data-name="shareName" data-dict="t8_share_name" data-width="120"></k-grid-column>
      <k-grid-column data-header="销售代码" data-name="salesCode" data-width="120"></k-grid-column>
      <k-grid-column data-header="销售名称" data-name="salesName" data-width="300"></k-grid-column>
      <k-grid-column data-header="销售客群" data-name="salesGroup" data-width="150"></k-grid-column>
      <k-grid-column data-header="份额状态" data-name="salesShareStatus" data-dict="t8_prod_status"></k-grid-column>
      <k-grid-column data-header="是否已确认" data-name="confirmStatus" data-dict="1yes0no"></k-grid-column>
      <k-grid-column data-header="基准类型" data-name="baseType" data-dict="t8_base_type" data-width="110"></k-grid-column>
      <k-grid-column data-header="基准利率下限%" data-name="baseMinRate" data-width="110"></k-grid-column>
      <k-grid-column data-header="基准利率上限%" data-name="baseMaxRate" data-width="110"></k-grid-column>
      <k-grid-column data-header="基准利率%" data-name="baseRate"></k-grid-column>
      <k-grid-column data-header="业绩基准说明" data-name="perfMethodExplain" data-width="150"></k-grid-column>
      <k-grid-column data-header="销售服务费率%" data-name="salesFeeRate" data-width="110"></k-grid-column>
      <k-grid-column data-header="起点金额(元)" data-name="minAmount" data-width="110"></k-grid-column>
      <k-grid-column data-header="递增金额(元)" data-name="stepAmount" data-width="110"></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="详情" data-functype="POPUP" data-size="mini"
               @click="detailHandler(scope.row.row)">
          <md-icon>library_books</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP"
               @click="updateHandler(scope.row.row)" data-size="mini" v-if="global.getProdIfUser(t8ProdInfoId)&&
             global.isShowAuthorityButton('ProdShareSortBak.insertProdShareSortBak')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
               data-action="ProdShareSort.delSortAndBakSort" data-size="mini" v-if="global.getProdIfUser(t8ProdInfoId)&&
             global.isShowAuthorityButton('ProdShareSort.delSortAndBakSort')"
               data-type="danger" data-target="prodShareSortGrid" :data-confirm="true" data-descript="删除"
        >
          <md-icon>close</md-icon>
        </k-btn>

      </template>
    </k-grid>
    <k-popup ref="addProdShareSortPopup" :data-dialog-drag="true"  data-width="1200px">
      <ProdShareSortAdd ref="sortAdd" :formProdShareSort="formProdShareSort" @changeShare="changeShare"
                        :allowRateBlank="allowRateBlank"
                        :t8ProdInfoId="t8ProdInfoId" :options="options" :prodName="prodName" :prodCode="prodCode"></ProdShareSortAdd>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" ref="addSubmitBtn" data-functype="SUBMIT" :data-handler="beforeSaveCommit"
               data-from="addShareSortForm" :data-model="formProdShareSort"
               data-target="prodShareSortGrid"
        >
          确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </k-form-footer>
    </k-popup>
    <k-popup ref="editPopup">
      <ProdShareSortAdd ref="sortEdit" :formProdShareSort="formProdShareSort" :allowRateBlank="allowRateBlank"
                        :t8ProdInfoId="t8ProdInfoId" :options="options" :formType="formType" :prodName="prodName" :prodCode="prodCode"></ProdShareSortAdd>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" ref="editSubmitBtn" :data-handler="beforeEditSaveCommit"
               data-from="addShareSortForm" :data-model="formProdShareSort" data-functype="SUBMIT"
               data-target="prodShareSortGrid"
        >
          确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </k-form-footer>
    </k-popup>
    <k-popup ref="detailPopup" data-width="1200px">
      <prod-share-sort-detail :formProdShareSort="formProdShareSort" :envItems="envItems"
                              :tailingCommisionList="tailingCommisionList" :t8ProdInfoId="this.t8ProdInfoId"
                              :moneyList="moneyList"></prod-share-sort-detail>
    </k-popup>
    <k-popup ref="approvalPopup">
      <DisplayProdShareSort :dataParams="dataParams" :t8ProdInfoId="t8ProdInfoId"></DisplayProdShareSort>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT"
               :data-model="updateParam" :data-handler="beforeApprovalCommit"
               data-action="ProdShareSortBak.approvalShareSort"
        >
          提交
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
      </k-form-footer>
    </k-popup>
  </div>
</template>
<script>
import ProdShareSortAdd from "@/pages/pms/shareSort/ProdShareSortAdd";
import ProdShareSortDetail from "@/pages/pms/shareSort/ProdShareSortDetail";
import DisplayProdShareSort from "@/pages/pms/M81/prodDisplay/DisplayProdShareSort";
import Tools from "@/utils/tools";
export default {
  name: 'ProdShareSortOpe',
  components: {ProdShareSortAdd, ProdShareSortDetail, DisplayProdShareSort},
  data() {
    return {
      t8ProdInfoId: '',
      queryParam: {},//查询参数
      formData: {},
      formProdShareSort: {},
      envItems: [{}],
      moneyList: [],
      tailingCommisionList: [],
      baseType: '',
      dataParams: [],//送审页面需要展示的数据
      updateParam: [],
      options: [],//参照份额
      existsSort: [],//存在份额
      formType: 'add',
      prodStatus: '',
      allowRateBlank: false,
      prodName:'',
      prodCode:'',
    }
  },
  created() {
    this.t8ProdInfoId = this.$route.query.t8ProdInfoId;
    this.baseType = this.$route.query.baseType;
    this.prodStatus = this.$route.query.prodStatus;
    this.prodCode = this.$route.query.prodCode;
    this.prodName = this.$route.query.prodName;
  },
  activated() {
    this.t8ProdInfoId = this.$route.query.t8ProdInfoId;
    this.baseType = this.$route.query.baseType;
    //获取基准类型
    this.httpUtil.comnQuery({
      action: 'T8ProdInfo.findT8ProdInfos',
      params: {id: this.t8ProdInfoId},
    }).then(data => {
      console.log(data)
      if(data.rows.length>0){
        this.baseType=data.rows[0].baseType;
      }
    });
    this.prodStatus = this.$route.query.prodStatus;
  },
  methods: {
    changeShare(value) {
      let val = parseInt(value);
      //不能直接将this.tableParams[val]赋值给formProdShareSort,直接将对象地址赋值会有问题，所以这里生产新对象赋值
      let obj = this.existsSort[val];
      this.formProdShareSort = obj
      this.formProdShareSort.shareName = ''
      this.formProdShareSort.salesName = ''
      this.formProdShareSort.salesCode = ''
      this.formProdShareSort.salesGroup = ''
      this.formProdShareSort.shareSort = value
      this.formProdShareSort.id = '';
      if (obj.prodShareRatio) {
        this.$refs.sortAdd.envItems = obj.prodShareRatio;
      } else {
        this.$refs.sortAdd.envItems = [{}];
      }
      if (obj.prodShareSection) {
        let moneyList2 = [];
        let array = [];
        for (let j = 0; j < obj.prodShareSection.length; j++) {
          array.push(obj.prodShareSection[j]);
          if (j < obj.prodShareSection.length - 1) {
            moneyList2.push(parseInt(obj.prodShareSection[j].dimension1Max));
          }
        }

        this.$refs.sortAdd.moneyList = moneyList2;
        this.$refs.sortAdd.tailingCommisionList = array;
      }
    },
    //打开新增弹出框
    addHandler() {
      this.allowRateBlank = false;
      this.options = [];
      this.formProdShareSort = {'baseType': this.baseType, 'salesShareStatus': this.prodStatus}
      //查询所有已经存在的份额
      this.httpUtil.comnQuery({
        action: 'ProdShareSort.findSortsAndBakSortsNoPage',
        params: {
          t8ProdInfoId: this.t8ProdInfoId,
        }
      }).then(data => {
        if (data.rows.length > 0) {
          let dataParams = data.rows;
          this.existsSort = dataParams;
          for (let i = 0; i < dataParams.length; i++) {
            //设置份额分类选项
            let option = {}
            option.label = dataParams[i].shareName + '类份额';
            option.value = i;
            this.options.push(option);
          }
        }
        this.$refs.addProdShareSortPopup.popup();
      });

    },
    //保存提交前操作
    beforeSaveCommit(val) {
      //验证必填项
      let validate = this.$refs.sortAdd.$refs.addShareSortForm.validate();
      let result = true;
      if (validate) {
        //查询是否已经存在相同的份额名称的数据
        this.httpUtil.comnQuery({
          action: 'ProdShareSort.findExistsSorts',
          params: {
            t8ProdInfoId: this.t8ProdInfoId,
            shareName: val.shareName
          }
        }).then(data => {
          if (data.rows.length > 0) {
            Tools.alert("已存在相同的份额名称！", 'danger');
            result = false;
            this.$refs.addSubmitBtn.loading = false;
          } else {
            if (val.baseType === '3' || val.baseType === '5') {
              let commitForm = this.$refs.sortAdd.$refs.addForm2;
              if (commitForm && commitForm.length > 0) {
                for (let i = 0; i < commitForm.length; i++) {
                  result = commitForm[i].validate();
                  if (result === false) {
                    this.$refs.addSubmitBtn.loading = false;
                    break;
                  }
                }
              }
              val.prodShareRatio = JSON.stringify(this.$refs.sortAdd.envItems);
            }

            if (val.baseType === '2') {
              val.prodShareSection = JSON.stringify(this.$refs.sortAdd.tailingCommisionList);
            }
            val.t8ProdInfoId = this.t8ProdInfoId;
            if (result == true) {
              this.httpUtil.comnUpdate({
                action: "ProdShareSortBak.insertProdShareSortBak",
                params: val,
                mask: true
              }).then(data => {
                this.$refs.addSubmitBtn.loading = false;
                this.$refs.addProdShareSortPopup.close();
                this.$refs.prodShareSortGrid.load({'t8ProdInfoId': this.t8ProdInfoId});
              });
            }
          }

        });
      } else {
        result = false;
        this.$refs.addSubmitBtn.loading = false;
      }
      return result;
    },
    //修改保存前操作
    beforeEditSaveCommit(val) {
      //验证必填项
      let validate = this.$refs.sortEdit.$refs.addShareSortForm.validate();
      let result = true;
      if (validate) {
        this.httpUtil.comnQuery({
          action: 'ProdShareSort.findExistsSorts',
          params: {
            t8ProdInfoId: this.t8ProdInfoId,
            shareName: val.shareName
          }
        }).then(data => {

          if (val.baseType === '3' || val.baseType === '5') {
            val.prodShareRatio = JSON.stringify(this.$refs.sortEdit.envItems);
          }
          if (val.baseType === '2') {
            val.prodShareSection = JSON.stringify(this.$refs.sortEdit.tailingCommisionList);
          }
          val.t8ProdInfoId = this.t8ProdInfoId;
          if (data.rows.length > 0) {
            if (val.id != data.rows[0].id) {
              Tools.alert("已存在相同的份额名称！", 'danger');
              this.$refs.editSubmitBtn.loading = false;
              result = false;
            } else {
              if (val.baseType === '3' || val.baseType === '5') {
                let commitForm = this.$refs.sortEdit.$refs.addForm2;
                if (commitForm && commitForm.length > 0) {
                  for (let i = 0; i < commitForm.length; i++) {
                    result = commitForm[i].validate();
                    if (result === false) {
                      this.$refs.editSubmitBtn.loading = false;
                      break;
                    }
                  }
                }
              }
              if (result == true) {
                this.httpUtil.comnUpdate({
                  action: "ProdShareSortBak.insertProdShareSortBak",
                  params: val,
                  mask: true
                }).then(data => {
                  this.$refs.editSubmitBtn.loading = false;
                  this.$refs.editPopup.close();
                  this.$refs.prodShareSortGrid.load({'t8ProdInfoId': this.t8ProdInfoId});
                });
              }
            }
          } else {
            if (val.baseType === '3' || val.baseType === '5') {
              let commitForm = this.$refs.sortEdit.$refs.addForm2;
              if (commitForm && commitForm.length > 0) {
                for (let i = 0; i < commitForm.length; i++) {
                  result = commitForm[i].validate();
                  if (result === false) {
                    this.$refs.editSubmitBtn.loading = false;
                    break;
                  }
                }
              }
            }
            if (result == true) {
              this.httpUtil.comnUpdate({
                action: "ProdShareSortBak.insertProdShareSortBak",
                params: val,
                mask: true
              }).then(data => {
                this.$refs.editSubmitBtn.loading = false;
                this.$refs.editPopup.close();
                this.$refs.prodShareSortGrid.load({'t8ProdInfoId': this.t8ProdInfoId});
              });
            }
          }
        });

      } else {
        result = false;
        this.$refs.editSubmitBtn.loading = false;
      }
      return result;
    },
    //提交审批前操作
    beforeApprovalCommit(val) {
      val["prodShareSorts"] = JSON.stringify(this.dataParams);
      return val;
    },
    //打开修改弹出框
    updateHandler(val) {
      this.formType = 'update';
      this.formProdShareSort = Object.assign({}, val);
      if (this.formProdShareSort.salesFeeRate > 0) {
        this.allowRateBlank = false;
      } else {
        this.allowRateBlank = true;
      }
      if (this.formProdShareSort.prodShareRatio) {
        if (this.formProdShareSort.prodShareRatio.length > 0) {
          this.envItems = this.formProdShareSort.prodShareRatio;
        } else {
          this.envItems = [{}];
        }
      } else {
        this.envItems = [{}];
      }
      if (this.formProdShareSort.prodShareSection) {
        let moneyList2 = [];
        let array = [];
        for (let j = 0; j < this.formProdShareSort.prodShareSection.length; j++) {
          array.push(this.formProdShareSort.prodShareSection[j]);
          if (j < this.formProdShareSort.prodShareSection.length - 1) {
            moneyList2.push(parseInt(this.formProdShareSort.prodShareSection[j].dimension1Max));
          }
        }

        this.moneyList = moneyList2;
        this.tailingCommisionList = array;

      }
      this.$nextTick(() => {
        this.$refs.sortEdit.envItems = this.envItems;
        this.$refs.sortEdit.moneyList = this.moneyList;
        this.$refs.sortEdit.tailingCommisionList = this.tailingCommisionList;
      });
      this.formProdShareSort.baseType = this.baseType;
      this.$refs.editPopup.popup();
    },
    //打开详情弹出框
    detailHandler(val) {
      this.formProdShareSort = Object.assign({}, val);
      if (this.formProdShareSort.prodShareRatio) {
        this.envItems = this.formProdShareSort.prodShareRatio;
      } else {
        this.envItems = [{}];
      }
      if (this.formProdShareSort.prodShareSection) {
        let moneyList2 = [];
        let array = [];
        for (let j = 0; j < this.formProdShareSort.prodShareSection.length; j++) {
          array.push(this.formProdShareSort.prodShareSection[j]);
          if (j < this.formProdShareSort.prodShareSection.length - 1) {
            moneyList2.push(parseInt(this.formProdShareSort.prodShareSection[j].dimension1Max));
          }
        }

        this.moneyList = moneyList2;
        this.tailingCommisionList = array;
      }
      this.formProdShareSort.baseType = this.baseType;
      this.$refs.detailPopup.popup();
    },
    //打开送审弹出框
    approvalHandler() {
      //查询需要送审的信息  bak表中查询
      this.httpUtil.comnQuery({
        action: 'ProdShareSortBak.findNeedApprovalInfo',
        params: {
          t8ProdInfoId: this.t8ProdInfoId,
        }
      }).then(data => {
        if (data.rows.length > 0) {
          this.dataParams = data.rows;
        }
        this.$refs.approvalPopup.popup();
      });
    }
  }
}
</script>
