<template>
  <div>
    <div style="min-height:225px;">
      <div class="add-btn-div">
        <div class="add-btn" @click="addHandler">+</div>
      </div>
      <k-grid ref="formProdShareSortGrid">
        <k-grid-column data-header="numId" data-name="numId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="t8ProdInfoId" data-name="t8ProdInfoId" :data-hidden="true"></k-grid-column>
        <k-grid-column data-header="份额名称" data-name="shareName" data-dict="t8_share_name"></k-grid-column>
        <k-grid-column data-header="销售代码" data-name="salesCode"></k-grid-column>
        <k-grid-column data-header="销售名称" data-name="salesName"></k-grid-column>
        <k-grid-column data-header="销售客群" data-name="salesGroup"></k-grid-column>
        <k-grid-column data-header="销售份额状态" data-name="salesShareStatus" data-dict="t8_prod_status"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="详情" data-functype="POPUP" data-size="mini"
                 @click="detailHandler(scope.row.row)">
            <md-icon>library_books</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改" data-functype="POPUP"
                 @click="updateHandler(scope.row.row)" data-size="mini">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="POPUP" :data-handler="deleteShareSort"
                 data-type="danger" data-target="formProdShareSortGrid" :data-confirm="true" data-descript="删除">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--业绩报酬及销售服务费率-->
    <k-grid ref="formProdSectionGrid" :data-operate-column="false">
      <k-grid-column data-header="numId" data-name="numId" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="t8ProdInfoId" data-name="t8ProdInfoId" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="份额名称" data-name="shareName" data-dict="t8_share_name"></k-grid-column>
      <k-grid-column data-header="基准类型" data-name="baseType" data-dict="t8_base_type"></k-grid-column>
      <k-grid-column data-header="基准利率%" data-name="baseRate"></k-grid-column>
      <k-grid-column data-header="基准利率下限%" data-name="baseMinRate"></k-grid-column>
      <k-grid-column data-header="基准利率上限%" data-name="baseMaxRate"></k-grid-column>
<!--      <k-grid-column data-header="业绩报酬提取比例%" data-name="performanceOut"></k-grid-column>-->
      <k-grid-column data-header="销售服务费率%" data-name="salesFeeRate"></k-grid-column>
    </k-grid>

    <!--业绩报酬及销售服务费率-->
    <k-grid ref="formProdMoneyGrid" :data-operate-column="false">
      <k-grid-column data-header="numId" data-name="numId" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="t8ProdInfoId" data-name="t8ProdInfoId" :data-hidden="true"></k-grid-column>
      <k-grid-column data-header="份额名称" data-name="shareName" data-dict="t8_share_name"></k-grid-column>
      <k-grid-column data-header="起点金额(元)" data-name="minAmount"></k-grid-column>
      <k-grid-column data-header="递增金额(元)" data-name="stepAmount"></k-grid-column>
      <k-grid-column data-header="认购追加金额" data-name="subAppendAmount"></k-grid-column>
      <k-grid-column data-header="申购追加金额" data-name="redeemAppendAmount"></k-grid-column>
      <k-grid-column data-header="单笔最小赎回份额" data-name="minRedeemVol"></k-grid-column>
      <k-grid-column data-header="持有份额下限" data-name="minHoleVol"></k-grid-column>
      <k-grid-column data-header="持有份额上限" data-name="maxHoleVol"></k-grid-column>
    </k-grid>

    <k-form dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="shareSortSave" v-show="menuName == 'M81007'"
               data-action="T8ProdAllInfo.addShareSortInfo"
               :data-model="updateParam">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
    </k-form>
    <k-popup ref="detailPopup">
      <prod-share-sort-detail :formProdShareSort="formProdShareSort" :envItems="envItems"
                              :tailingCommisionList="tailingCommisionList"
                              :moneyList="moneyList"></prod-share-sort-detail>
    </k-popup>
    <!--    修改和新增份额分类信息弹出框   -->
    <k-popup ref="addShareSortPopup" :data-title="popupTitle" :data-dialog-drag="true" data-width="1200px">
      <k-form ref="addShareSortForm" :data-col="2" data-label-width="300px">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formProdShareSort.t8ProdInfoId"/>
        </k-form-item>
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formProdShareSort.prodCode"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="prodCode" :data-allowblank="true" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="prodName" :data-allowblank="true"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="参考份额分类">
          <k-field-select v-model="formProdShareSort.shareSort" :data-data="options" @data-on-change="shareSortChange"
                          :data-disabled="formType === 'update'"/>
        </k-form-item>
        <k-form-item label="份额名称">
          <k-field-select v-model="formProdShareSort.shareName" data-dict="t8_share_name" :data-allowblank="false"
                          :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="销售名称">
          <k-field-text v-model="formProdShareSort.salesName" @data-on-blur="isExistsProd(formProdShareSort.salesName,formProdShareSort.id)" :data-allowblank="false" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="销售代码">
          <k-field-text v-model="formProdShareSort.salesCode" @data-on-blur="isExistsProd1(formProdShareSort.salesCode,formProdShareSort.id)" :data-allowblank="false" :data-max-length="60"/>
        </k-form-item>
        <k-form-item label="销售客群">
          <k-field-text v-model="formProdShareSort.salesGroup" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item>
        </k-form-item>
        <div class="tableLine"><span class="midText">份额分类信息</span></div>

        <k-form-item label="起点金额">
          <k-field-text v-model="formProdShareSort.minAmount" data-validate-type="money" data-type="money"
                        data-min-value="(0"
                        :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)"
                        data-show-gbmoney="true"/>
        </k-form-item>
        <k-form-item label="递增金额">
          <k-field-text v-model="formProdShareSort.stepAmount" data-validate-type="money" data-type="money"
                        data-min-value="(0"
                        :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)"
                        data-show-gbmoney="true"/>
        </k-form-item>
        <k-form-item label="认购追加金额">
          <k-field-text v-model="formProdShareSort.subAppendAmount" data-validate-type="money" data-type="money"
                        data-min-value="(0"
                        :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)"
                        data-show-gbmoney="true"/>
        </k-form-item>
        <k-form-item label="申购追加金额"  v-if="this.popShow">
          <k-field-text v-model="formProdShareSort.redeemAppendAmount" data-validate-type="money" data-type="money"
                        data-min-value="(0"
                        :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)"
                        data-show-gbmoney="true"/>
        </k-form-item>
        <k-form-item label="单笔最小赎回份额" v-if="this.popShow">
          <k-field-text v-model="formProdShareSort.minRedeemVol" data-validate-type="money" data-type="money"
                        data-min-value="(0"
                        :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(份)"/>
        </k-form-item>
        <k-form-item label="持有份额下限">
          <k-field-text v-model="formProdShareSort.minHoleVol" data-validate-type="money" data-type="money"
                        data-min-value="(0"
                        :data-max-value="formProdShareSort.maxHoleVol===''?9999999999999999.99:formProdShareSort.maxHoleVol"
                        :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(份)"/>
        </k-form-item>
        <k-form-item label="持有份额上限">
          <k-field-text v-model="formProdShareSort.maxHoleVol" data-validate-type="money" data-type="money"
                        :data-min-value="formProdShareSort.minHoleVol===''?1:formProdShareSort.minHoleVol"
                        :data-max-length="18" data-digits="2" data-placeholder="单位(份)"/>
        </k-form-item>
        <div class="tableLine"><span class="midText">销售服务费信息</span></div>

        <k-form-item label="销售服务费率(%)">
          <k-field-text v-model="formProdShareSort.salesFeeRate" :data-allowblank="false" data-validate-type="number"
                        data-type="number" data-min-value="0" data-max-value="100" data-digits="2"
                        @data-on-change="changeRate"/>
        </k-form-item>
        <k-form-item label="付费规则">
          <k-field-select v-model="formProdShareSort.feeRules" data-dict="t8_payment_rules"
                          :data-allowblank="allowRateBlank"/>
        </k-form-item>
        <k-form-item label="计提基数">
          <k-field-select v-model="formProdShareSort.raiseBase" data-dict="t8_charging_index"
                          :data-allowblank="allowRateBlank"/>
        </k-form-item>
        <k-form-item label="计提规则">
          <k-field-select v-model="formProdShareSort.raiseRules" data-dict="t8_raise_rules"
                          :data-allowblank="allowRateBlank"/>
        </k-form-item>
        <div class="tableLine"><span class="midText">业绩基准信息</span></div>

        <k-form-item label="基准类型">
          <k-field-select v-model="formProdShareSort.baseType" data-dict="t8_base_type" :data-disabled="true"
                          :data-allowblank="false"/>
        </k-form-item>
        <div>
          <k-form ref="addForm2" v-for="(item,index) in envItems" :key="index"
                  v-if="formProdShareSort.baseType == '3' || formProdShareSort.baseType == '5'"
                  :data-col="2" data-input-width="100px" data-label-width="120px" data-total-width="1500px">
            <k-form-item :label="index=='0'?'':'操作符'" >
              <k-field-select v-model="item.operator" data-dict="t8_performance_operator" data-display-field="itemval"  data-value-field="itemkey"
                              :data-allowblank="false" :data-default-value="index==0?'1':''"></k-field-select>
            </k-form-item>
            <k-form-item :label="'数据类型'" v-if="formProdShareSort.baseType == '5'" key="ratioType">
              <k-field-select v-model="item.ratioType" data-dict="t8_performance_ratio_type" data-display-field="itemval"  data-value-field="itemkey"
                              :data-allowblank="false" style="width: 120px"></k-field-select>
            </k-form-item>
            <k-form-item :label="'数据类型'" v-if="formProdShareSort.baseType == '3'" key="ratioType1">
              <k-field-select v-model="item.ratioType" data-dict="t8_performance_ratio_type" data-display-field="itemval"  data-value-field="itemkey"
                              data-default-value="1" data-disabled="true"
                              :data-allowblank="false" style="width: 120px"></k-field-select>
            </k-form-item>
            <k-form-item :label="'指数名称'" v-if="item.ratioType == '1' || item.ratioType == null || item.ratioType == ''" key="ratioIndex">
              <k-field-select v-model="item.ratioIndex" data-value-field="indexCode" :data-allowblank="false"
                              data-display-field="indexName" data-action="T8IndexInfo.find" style="width: 160px"/>
            </k-form-item>
            <k-form-item :label="'自定义利率'" v-if="item.ratioType == '2'" key="fixedValue">
              <k-field-text v-model="item.fixedValue" :data-allowblank="false" :data-max-length="8"
                            data-min-value="0"  data-validate-type="money"
                            data-type="money" data-digits="2" style="width: 160px"/>
            </k-form-item>
            <k-form-item :label="'市场利率'" v-if="item.ratioType == '3'" key="marketRate">
              <k-field-select v-model="item.marketRate" :data-allowblank="false"
                              data-dict="t8_market_rate" data-display-field="itemval"  data-value-field="itemkey" style="width: 160px"/>
            </k-form-item>
            <k-form-item :label="'系数%'">
              <k-field-text v-model="item.coefficient" :data-allowblank="false" :data-max-length="8"
                            data-min-value="0" data-max-value="100" data-integer-length="3" data-validate-type="money"
                            data-type="money" data-digits="2" style="width:70%;"/>
            </k-form-item>
            <k-btn class="md-info md-just-icon md-simple"  data-descript="新增"
                   @click="()=>envItems.push({})" v-show="formProdShareSort.baseType == '3' || formProdShareSort.baseType == '5'">
              <md-icon>add</md-icon>
            </k-btn>
            <k-btn class="md-info md-just-icon md-simple"  data-descript="删除当前行"
                   @click="deleteEvent(index)" v-show="formProdShareSort.baseType == '3'|| formProdShareSort.baseType == '5'">
              <md-icon md-src="/static/svg/delete.svg"/>
            </k-btn>

          </k-form>
        </div>
        <!--        <k-form-item label="业绩报酬提取比例%">-->
        <!--          <k-field-text v-model="formProdShareSort.performanceOut" :data-max-length="8"-->
        <!--                        data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"-->
        <!--                        data-type="money" data-digits="2"/>-->
        <!--        </k-form-item>-->
        <k-form-item label="基准利率%" v-if="formProdShareSort.baseType == '1'">
          <k-field-text v-model="formProdShareSort.baseRate" :data-max-length="8"
                        :data-allowblank="formProdShareSort.baseType != '1'"
                        data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"
                        data-type="money" data-digits="2"/>
        </k-form-item>
        <k-form-item label="基准利率下限%" v-if="formProdShareSort.baseType == '2' ">
          <k-field-text v-model="formProdShareSort.baseMinRate" :data-max-length="8"
                        :data-allowblank="formProdShareSort.baseType != '2'"
                        data-min-value="0" data-integer-length="3" data-validate-type="money"
                        :data-max-value="formProdShareSort.baseMaxRate===''?'100':formProdShareSort.baseMaxRate"
                        data-type="money" data-digits="2"/>
        </k-form-item>
        <k-form-item label="基准利率上限%" v-if="formProdShareSort.baseType == '2' ">
          <k-field-text v-model="formProdShareSort.baseMaxRate" :data-max-length="8"
                        :data-allowblank="formProdShareSort.baseType != '2'"
                        :data-min-value="formProdShareSort.baseMinRate===''?'0':formProdShareSort.baseMinRate"
                        data-integer-length="3" data-validate-type="money" data-max-value="100"
                        data-type="money" data-digits="2"/>
        </k-form-item>
        <k-form-item label="市场利率类型" v-if="formProdShareSort.baseType == '4' ">
          <k-field-select v-model="formProdShareSort.marketRate" data-dict="t8_market_rate"
                          :data-allowblank="formProdShareSort.baseType != '4'"
          />
        </k-form-item>
        <k-form-item label="业绩比较基准（产品要素、投资管理使用）" :data-col="2" v-if="formProdShareSort.baseType != '0'">
          <k-field-text v-model="formProdShareSort.perfMethodExplain" :data-max-length="2000" inputType="textarea"
                        :rows="5" :data-allowblank="false"/>
        </k-form-item>
<!--        <k-form-item label="自定义利率%" v-if="formProdShareSort.baseType == '5' ">-->
<!--          <k-field-text v-model="formProdShareSort.custom" :data-max-length="8"-->
<!--                        data-min-value="0" data-max-value="100" data-integer-length="3" data-validate-type="money"-->
<!--                        data-type="money" data-digits="2"/>-->
<!--        </k-form-item>-->
        <div style="width:100%">
          <div class="my-container" v-if="formProdShareSort.baseType == '2' ">
            <div class="my-item2">
              <div style="width: 135px !important">
                <md-switch v-model="switchSegmentValue" class="md-info" @change="changeSegmentType">分段计提</md-switch>
              </div>
              <div class="my-item2-chips">
                <md-chips v-model="moneyList" class="md-primary" style="padding-top: 4px !important;"
                          md-placeholder="请输入基准利率区间,并按回车确认"
                          md-input-type="number"
                          @md-delete="deleteMoney" @md-insert="insertMoney"
                          v-show="switchSegmentValue && formProdShareSort.baseType == '2'"></md-chips>
              </div>
            </div>
          </div>

          <div label="分段计提展示" class="my-table" v-if="switchSegmentValue  && formProdShareSort.baseType == '2' ">
            <md-table>
              <md-table-row>
                <md-table-head v-show="moneyList.length > 0  && formProdShareSort.baseType == '2'">基准利率区间%
                </md-table-head>
                <md-table-head v-show="moneyList.length > 0  && formProdShareSort.baseType == '2'"> 计提比例%
                </md-table-head>
              </md-table-row>

              <!--没有数据时才展示这一行 -->
              <md-table-row v-show="timeList.length == 0 && moneyList.length == 0 && formProdShareSort.baseType == '2'">
                <md-table-cell :colspan="3" style="text-align:center">
                  <p> 暂无数据</p>
                </md-table-cell>
              </md-table-row>

              <md-table-row v-show="timeList.length > 0 || moneyList.length > 0 && formProdShareSort.baseType == '2'"
                            v-for="(item,index) in tailingCommisionList" :key="index">
                <md-table-cell v-show="formProdShareSort.baseType == '2'"
                               :rowspan="item.moneyRowspan">
                  {{ item.moneyDesc }}
                </md-table-cell>
                <md-table-cell v-show="item.showTimeTd && formProdShareSort.baseType == '2'">
                  {{ item.timeDesc }}
                </md-table-cell>
                <md-table-cell>
                  <md-field style="width: 50%">
                    <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.rateAccrual"
                              type="number"></md-input>
                    <span class="md-suffix">%</span>
                  </md-field>
                </md-table-cell>
              </md-table-row>
            </md-table>
          </div>
        </div>



        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" ref="shareSubmitBtn"
                 data-from="addShareSortForm" :data-model="formProdShareSort" :data-handler="submitHandle"
                 :data-after-success="initGrid">
            确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


  </div>
</template>

<script>
import Tools from "@/utils/tools";
import ProdShareSortDetail from "@/pages/pms/shareSort/ProdShareSortDetail";

export default {
  name: "M81001-ShareSort",
  components: {ProdShareSortDetail},
  model: {
    prop: 'prodShareSort',
    event: 'input'
  },
  props: {
    prodShareSort: {},
    t8ProdInfoId: '',
    menuName: '',
    baseType: '',
    salesShareStatus: '',//状态
    prodCode:'',
    prodName:'',
    },
    data() {
      return {
        envItems: [{}],
        moneyList: [],
        timeList: [],
        popShow:true,
        constantRatesSitch: true,     //固定切换按钮的值
        switchSegmentValue: true,//是否分段计提
        tailingCommisionList: [],
        tailingCommisionMoneyList: [],
        tailingCommisionTimeList: [],
        tableParams: [],
        updateParam: [],
        formProdShareSort: {},
        popupTitle: '',
        formType: '',
        options: [],
        allowRateBlank: false,
      }
    },
    created() {
      this.initGrid();
      this.$set(this.formProdShareSort, "salesShareStatus", this.salesShareStatus)
      this.$set(this.formProdShareSort, "baseType", this.baseType);
      this.$set(this.formProdShareSort, "t8ProdInfoId", this.t8ProdInfoId);
    },
    methods: {
      //修改框中费率发生变化
      changeRate(value) {
        if (value == 0) {
          this.allowRateBlank = true;
        } else {
          this.allowRateBlank = false;
        }
      },
      isExistsProd(code, id) {
        console.log(code);
        console.log(id);
        this.httpUtil.comnQuery({
          action: "ProdShareSort.isExistsShareSort",
          params: {
            salesName: code, id: id
          },
        }).then(data => {
          if (data.rows.length > 0) {
            Tools.alert("销售名称已存在，请核对!","danger");
            this.$set(this.formProdShareSort,'salesName','');
            this.$set(this.formProdShareSort,'salesCode','');

            return false;
          }
        });
      },
      isExistsProd1(code,id){
        console.log(code);
        console.log(id);
        this.httpUtil.comnQuery({
          action: "ProdShareSort.isExistsShareSort",
          params: {
            salesCode:code,id:id
          },
        }).then(data => {
          if(data.rows.length > 0){
            Tools.alert("销售代码已存在，请核对!","danger");
            this.$set(this.formProdShareSort,'salesName','');
            this.$set(this.formProdShareSort,'salesCode','');

            return false;
          }
        });
      },
      //初始化表格数据
      initGrid() {
        let _this = this;
        this.httpUtil.comnQuery({
          action: 'ProdShareSort.findProdShareSortsNoAuth',
          params: {
            t8ProdInfoId: this.t8ProdInfoId,
          }
        }).then(data => {
          if (data.rows.length > 0) {
            //console.log('这是啥：',data.rows)
            for (let i = 0; i < data.rows.length; i++) {
              if(this.baseType){
                data.rows[i].baseType=this.baseType
              }
              if (data.rows[i].baseType === '2') {
                //baseType等于2时是区间基准  需要把单基准值删除
                data.rows[i].baseRate=''
              }else if(data.rows[i].baseType === '1'){
                //baseType等于1单基准  需要把baseMaxRate baseMinRate清空
                data.rows[i].baseMaxRate=''
                data.rows[i].baseMinRate=''
              }else{
                //选择其它也会清空
                data.rows[i].baseRate=''
                data.rows[i].baseMaxRate=''
                data.rows[i].baseMinRate=''
              }
            }
            //console.log('这是啥：',data.rows)
            _this.uptDataParams(data.rows);
          }
        });
      },
      shareSortChange(value) {
          var val = parseInt(value);
          if(isNaN(val)){
            return
          }
          //不能直接将this.tableParams[val]赋值给formProdShareSort,直接将对象地址赋值会有问题，所以这里生产新对象赋值
          var obj = JSON.parse(JSON.stringify(this.tableParams[val]))
          obj.shareSort=value
          this.formProdShareSort = obj
          this.formProdShareSort.shareName = ''
          this.formProdShareSort.salesName = ''
          this.formProdShareSort.salesCode = ''
          this.formProdShareSort.salesGroup = ''
          //this.formProdShareSort.shareSort = value
          this.formProdShareSort.id = '';
          if (obj.prodShareRatio) {
            this.envItems = obj.prodShareRatio;
          } else {
            this.envItems = [{}];
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

            this.moneyList = moneyList2;
            this.tailingCommisionList = array;
          }
      },
      uptDataParams(dataParams) {
        this.options = [];
        for (let i = 0; i < dataParams.length; i++) {
          //设置份额分类选项
          var option = {}
          option.label = dataParams[i].shareName + '类份额';
          option.value = i;
          this.options.push(option);
          this.$set(dataParams[i], 'numId', i)
        }
        this.tableParams = dataParams;
        this.$set(this.prodShareSort, 'dataParams', dataParams);
        this.$set(this.$refs.formProdShareSortGrid, 'list', this.tableParams);
        this.$set(this.$refs.formProdSectionGrid, 'list', this.tableParams);
        this.$set(this.$refs.formProdMoneyGrid, 'list', this.tableParams);
        // this.envItems.forEach((value,index) => {
        //     value['baseType'] = this.formProdShareSort.baseType;
        //     value['perfMethodExplain'] = this.formProdShareSort.perfMethodExplain;
        // });
        //   this.$set(this.$refs.formPerfBenchGrid, 'list', this.envItems);
        //   console.log(this.envItems);
      },
      /*打开新增弹出框*/
      addHandler() {
        this.envItems = [{}];
        this.formProdShareSort = {};
        this.allowRateBlank = false;
        this.formType = 'add'
        this.formProdShareSort.t8ProdInfoId = this.t8ProdInfoId;
        this.formProdShareSort.baseType = this.baseType;
        this.formProdShareSort.salesShareStatus = this.salesShareStatus;
        this.popupTitle = '新增份额分类';

        this.httpUtil.comnQuery({
          action: 'T8ProdInfo.findT8ProdInfos',
          params: {
            id: this.t8ProdInfoId,
        },
        successAlert: false,
      }).then(data => {
        if(data.rows.length > 0 ){
          let row = data.rows[0];
          if(row.prodMode=='1'){
            this.popShow=false;
          }

        }
        this.$refs.addShareSortPopup.popup();
      });

      },
      /*打开修改弹出框*/
      updateHandler(val) {
        this.formType = 'update'
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
          this.tailingCommisionMoneyList = array;
        }
        this.popupTitle = '修改份额分类';

        this.formProdShareSort.baseType = this.baseType;
        this.httpUtil.comnQuery({
          action: 'T8ProdInfo.findT8ProdInfos',
          params: {
            id: this.t8ProdInfoId,
          },
          successAlert: false,
        }).then(data => {
          if(data.rows.length > 0 ){
            let row = data.rows[0];
            if(row.prodMode=='1'){
              this.popShow=false;
            }

          }
          this.$refs.addShareSortPopup.popup();
        });

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
          this.tailingCommisionMoneyList = array;
        }
        this.formProdShareSort.baseType = this.baseType;
        this.$refs.detailPopup.popup();
      },
      /*删除*/
      deleteEvent(index) {
        if (this.envItems.length > 1) {
          this.envItems.splice(index, 1)
        }
      },
      changeSegmentType: function (value) {
        this.moneyList = []

        if (this.formProdShareSort.baseType == '3' || this.formProdShareSort.baseType == '5') {
          this.envItems.length = 1;
        }
        //重新生成列表
        this.buildTimeTable();
      },
      deleteMoney: function (text, index) {
        //是否已经删完
        if (this.moneyList.length == 0) {
          Tools.alert("已经没有数据", "danger");
          //没有数据，要按时间维度重新刷新列表
          this.buildTimeTable();
          return false;
        }
        //inde-下标，从0开始
        if (index != this.moneyList.length) {
          Tools.alert("请顺序删除", "danger");
          //在指定位置添加元素,第一个参数指定位置,第二个参数指定要删除的元素,如果为0,则追加
          this.moneyList.splice(index, 0, text);
          return false;
        }
        this.buildMoneyTable();
      },
      insertMoney: function (value) {
        //插入后，已经插入了
        if (value <= 0 || parseInt(this.moneyList[this.moneyList.length - 2]) > parseInt(value)) {
          this.moneyList.pop();
          Tools.alert("利率不能小于等于0，不能小于上次输入利率", "danger");
          return false;
        }
        this.buildMoneyTable();
        return value;
      },
      buildTimeTable: function () {
        this.tailingCommisionList = [];
        //先按利率组合，再按持有时间
        this.tailingCommisionTimeList = [];
        for (var i = 0; i < this.timeList.length; i++) {

          //1-利率，2-时间
          this.tailingCommisionTimeList.push(
            {
              dimension1Min: null,
              dimension1Max: null,
              dimension2Min: i == 0 ? 0 : this.timeList[i - 1],
              dimension2Max: this.timeList[i],
              moneyDesc: null,
              timeDesc: (i == 0 ? 0 : this.timeList[i - 1]) + ' <= 天数 < ' + this.timeList[i],
              rate: null,
              constantFee: null,
              minFee: null,
              maxFee: null,
              dataStatus: 'E',
              moneyRowspan: 1,
              showMoneyTd: false,
              showTimeTd: true
            }
          )
        }
        var lastTime = {
          dimension1Min: null,
          dimension1Max: null,
          dimension2Min: this.timeList[this.timeList.length - 1],
          dimension2Max: '-1',
          moneyDesc: null,
          timeDesc: this.timeList[this.timeList.length - 1] + ' <= 天数 < ∞',
          rate: null,
          constantFee: null,
          minFee: null,
          maxFee: null,
          dataStatus: 'E',
          moneyRowspan: 1,
          showMoneyTd: false,
          showTimeTd: true
        };
        this.tailingCommisionTimeList.push(lastTime);
        if (this.moneyList == null || this.moneyList.length == 0) {
          //为空只有一个维度
          this.tailingCommisionList = this.tailingCommisionTimeList;
        } else {
          //不为空，两个维度
          for (let i = 0; i <= this.moneyList.length; i++) {
            for (let j = 0; j < this.tailingCommisionTimeList.length; j++) {
              let temp = Object.assign({}, this.tailingCommisionTimeList[j]);
              temp.showMoneyTd = j != 0 ? false : true;
              temp.moneyRowspan = j == 0 ? this.tailingCommisionTimeList.length : 1;
              temp.moneyDesc = (i == 0 ? 0 : this.moneyList[i - 1]) + ' <= 利率 < ' + (this.moneyList.length == i ? ' ∞ ' : this.moneyList[i]);
              temp.dimension1Min = i == 0 ? 0 : this.moneyList[i - 1];
              temp.dimension1Max = i == this.moneyList.length ? '-1' : this.moneyList[i];
              this.tailingCommisionList.push(temp);
            }
          }
        }
      },
      buildMoneyTable: function () {
        this.tailingCommisionList = [];
        //先按金额组合，再按持有时间
        this.tailingCommisionMoneyList = [];

        for (var i = 0; i < this.moneyList.length; i++) {

          //1-金额，2-时间
          this.tailingCommisionMoneyList.push(
            {
              dimension1Min: i == 0 ? 0 : this.moneyList[i - 1],
              dimension1Max: this.moneyList[i],
              dimension2Min: null,
              dimension2Max: null,
              moneyDesc: (i == 0 ? 0 : this.moneyList[i - 1]) + ' <= 利率 < ' + this.moneyList[i],
              timeDesc: null,
              rate: null,
              constantFee: null,
              minFee: null,
              maxFee: null,
              dataStatus: 'E',
              moneyRowspan: 1,
              showMoneyTd: true,
              showTimeTd: false
            }
          )
        }
        var lastMoney = {
          dimension1Min: this.moneyList[this.moneyList.length - 1],
          dimension1Max: '-1',
          dimension2Min: null,
          dimension2Max: null,
          moneyDesc: this.moneyList[this.moneyList.length - 1] + ' <= 利率 < ∞',
          timeDesc: null,
          rate: null,
          constantFee: null,
          minFee: null,
          maxFee: null,
          dataStatus: 'E',
          moneyRowspan: 1,
          showMoneyTd: true,
          showTimeTd: false
        };
        this.tailingCommisionMoneyList.push(lastMoney);
        if (this.timeList == null || this.timeList.length == 0) {
          //为空只有一个维度
          this.tailingCommisionList = this.tailingCommisionMoneyList;
        } else {
          //不为空，两个维度
          for (let i = 0; i < this.tailingCommisionMoneyList.length; i++) {
            for (var j = 0; j <= this.timeList.length; j++) {
              let temp = Object.assign({}, this.tailingCommisionMoneyList[i]);
              temp.showTimeTd = true;
              temp.showMoneyTd = j == 0 ? true : false;
              temp.moneyRowspan = j == 0 ? this.timeList.length + 1 : 1;
              temp.timeDesc = (j == 0 ? 0 : this.timeList[j - 1]) + ' <= 天数 < ' + (j == this.timeList.length ? ' 100 ' : this.timeList[j]);
              temp.dimension2Min = j == 0 ? 0 : this.timeList[j - 1];
              temp.dimension2Max = j == this.timeList.length ? '-1' : this.timeList[j];
              this.tailingCommisionList.push(temp);
            }
          }
        }
      },
      submitHandle(val) {

        //手动验证表单
        let validate = this.$refs.addShareSortForm.validate();
        let result = true;
        if (validate) {
          //验证是否已经存在相同的份额名称，如果存在进行提示
          for (let i = 0; i < this.tableParams.length; i++) {
            if (val.shareName == this.tableParams[i].shareName) {
              if (this.formType === 'update') {
                if (val.numId != this.tableParams[i].numId) {
                  this.$refs.shareSubmitBtn.loading = false;
                  Tools.alert("已存在相同的份额名称！", "danger");
                  result = false;
                  break;
                }
              } else {
                this.$refs.shareSubmitBtn.loading = false;
                Tools.alert("已存在相同的份额名称！", "danger");
                result = false;
                break;
              }
            }
          }
          if (result == true) {
            //如果是指数类型，需要验证指数数组是否为空
            if (val.baseType == '3' || val.baseType == '5') {
              let commitForm = this.$refs.addForm2;
              if (commitForm && commitForm.length > 0) {
                for (let i = 0; i < commitForm.length; i++) {
                  result = commitForm[i].validate();
                  if (result === false) {
                    break;
                  }
                }
              }
            }
            if (result == true) {
              //如果是修改先删除原有的数据然后再将新数据保存
              if (this.formType === 'update') {
                this.$delete(this.tableParams, val.numId);
              }
              if (val.baseType === '3' || val.baseType === '5') {
                val.prodShareRatio = this.envItems
                //选择其它也会清空
                val.baseRate=''
                val.baseMaxRate=''
                val.baseMinRate=''
              }

              if (val.baseType === '2') {
                val.prodShareSection = this.tailingCommisionList;
                //baseType等于2时是区间基准  需要把单基准值删除
                val.baseRate=''
              }else if(val.baseType === '1'){
                //baseType等于1单基准  需要把baseMaxRate baseMinRate清空
                val.baseMaxRate=''
                val.baseMinRate=''
              }else{
                //选择其它也会清空
                val.baseRate=''
                val.baseMaxRate=''
                val.baseMinRate=''
              }
              //console.log("份额分类",val)
              this.tableParams.push(val);
              this.uptDataParams(this.tableParams);
              this.$refs.addShareSortPopup.close();
            }
          }
        }
      },
      deleteShareSort(val) {
        for (var i = 0; i < this.tableParams.length; i++) {
          if (this.tableParams[i].numId === val.numId) {
            //删除
            this.tableParams.splice(i, 1);
            break;
          }
        }
      },
      shareSortSave(val) {
        val["prodShareSorts"] = JSON.stringify(this.tableParams);
        val["t8ProdInfoId"] = this.t8ProdInfoId;
        val["prodCode"] = this.prodCode;
        console.log(this.prodCode)
      }
    }
  }
</script>

<style scoped>
  .add-btn-div {
    position: relative;
    z-index: 1;
  }

  .add-btn {
    background-color: #4caf50;
    border-radius: 20px;
    box-shadow: 0 4px 5px 0 rgba(76, 175, 80, 0.14), 0 1px 10px 0 rgba(76, 175, 80, 0.12), 0 2px 4px -1px rgba(76, 175, 80, 0.2);
    width: 20px;
    height: 20px;
    line-height: 20.5px;
    font-size: 23px;
    font-weight: 400;
    cursor: pointer;
    color: #FFF;
    text-align: center;
  }

  .tableLine {
    margin: 30px 8px 20px 6px;
    border-top: 1px dotted #C0C0C0;
    width: 900px;
    position: relative;
    text-align: center;
    font-size: 14px;
  }

  .midText {
    position: absolute;
    left: 50%;
    background-color: #ffffff;
    font-weight: 300;
    padding: 0 15px;
    transform: translateX(-50%) translateY(-50%);
  }
</style>
