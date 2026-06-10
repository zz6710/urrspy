<template>
  <div>

    <k-form class="my-form" ref="performanceInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

      <k-form-item label="t8ProdInfoId" v-show="false">
        <k-field-text v-model="T8ProdPerformance.t8ProdInfoId"/>
      </k-form-item>
      <k-form-item label="prodCode" v-show="false">
        <k-field-text v-model="T8ProdPerformance.prodCode"/>
      </k-form-item>
      <k-form-item label="基准类型">
        <k-field-select v-model="T8ProdPerformance.baseType" data-dict="t8_base_type" :data-allowblank="false"
                        @data-on-change="inlitFrom"/>
      </k-form-item>
      <div class="k-form-body" v-if="this.isShowTable && T8ProdPerformance.baseType != '0'">

        <k-form-item label="基准利率%" v-if="T8ProdPerformance.baseType == '1' && isShareSort ==='0'" key="baseRate">
          <k-field-text v-model="T8ProdPerformance.baseRate" :data-max-length="8"
                        data-min-value="0" data-integer-length="3" :data-allowblank="!(T8ProdPerformance.baseType == '1' && isShareSort ==='0')" data-validate-type="money" data-max-value="100"
                        data-type="money" />
        </k-form-item>

        <k-form-item label="基准利率下限%" v-if="T8ProdPerformance.baseType == '2' && isShareSort ==='0'">
          <k-field-text v-model="T8ProdPerformance.baseMinRate" :data-max-length="8" key="baseMinRate"
                        data-min-value="0" data-integer-length="3" data-validate-type="money"
                        :data-max-value="T8ProdPerformance.baseMaxRate" :data-allowblank="!(T8ProdPerformance.baseType == '2' && isShareSort ==='0')"
                        data-type="money" data-digits="2"/>
        </k-form-item>
        <k-form-item label="基准利率上限%" v-if="T8ProdPerformance.baseType == '2' && isShareSort ==='0'">
          <k-field-text v-model="T8ProdPerformance.baseMaxRate" :data-max-length="8" key="baseMaxRate"
                        :data-min-value="T8ProdPerformance.baseMinRate" :data-allowblank="!(T8ProdPerformance.baseType == '2' && isShareSort ==='0')"
                        data-integer-length="3" data-validate-type="money" data-max-value="100"
                        data-type="money" data-digits="2"/>
        </k-form-item>
        <k-form-item label="市场利率类型" v-if="T8ProdPerformance.baseType == '4' && isShareSort ==='0'">
          <k-field-select v-model="T8ProdPerformance.marketRate" data-dict="t8_market_rate"
          />
        </k-form-item>

        <k-form ref="addForm2" v-for="(item,index) in envItems" :key="index"
                v-if="(T8ProdPerformance.baseType == '3' || T8ProdPerformance.baseType == '5') && isShareSort === '0'"
                :data-col="6" data-input-width="120px" data-label-width="100px" data-total-width="1300px">
          <div class="md-addForm2" style="display: flex;justify-content: space-evenly;align-items: center;width:1118px;text-align: left;float: left">
            <k-form-item :label="index=='0'?'':'操作符'">
              <k-field-select v-model="item.operator" data-dict="t8_performance_operator" data-display-field="itemval"  data-value-field="itemkey"
                              :data-allowblank="false" :data-default-value="index==0?'1':''"></k-field-select>
            </k-form-item>
            <k-form-item :label="'数据类型'" v-if="T8ProdPerformance.baseType == '5'" key="ratioType">
              <k-field-select v-model="item.ratioType" data-dict="t8_performance_ratio_type" data-display-field="itemval"  data-value-field="itemkey"
                              :data-allowblank="false"></k-field-select>
            </k-form-item>
            <k-form-item :label="'数据类型'" v-if="T8ProdPerformance.baseType == '3'" key="ratioType1">
              <k-field-select v-model="item.ratioType" data-dict="t8_performance_ratio_type" data-display-field="itemval"  data-value-field="itemkey"
                              data-default-value="1" data-disabled="true"
                              :data-allowblank="false" ></k-field-select>
            </k-form-item>
            <k-form-item :label="'指数名称'" v-if="item.ratioType == '1' || item.ratioType == null || item.ratioType == ''" key="ratioIndex">
              <k-field-select v-model="item.ratioIndex" data-value-field="indexCode" :data-allowblank="false"
                              data-display-field="indexName" data-action="T8IndexInfo.find"/>
            </k-form-item>
            <k-form-item :label="'自定义利率'" v-if="item.ratioType == '2'" key="fixedValue">
              <k-field-text v-model="item.fixedValue" :data-allowblank="false" :data-max-length="8"
                            data-min-value="0"  data-validate-type="money"
                            data-type="money" data-digits="2"/>
            </k-form-item>
            <k-form-item :label="'市场利率'" v-if="item.ratioType == '3'" key="marketRate">
              <k-field-select v-model="item.marketRate" :data-allowblank="false"
                            data-dict="t8_market_rate" data-display-field="itemval"  data-value-field="itemkey"/>
            </k-form-item>
            <k-form-item :label="'系数%'">
              <k-field-text v-model="item.coefficient" :data-allowblank="false" :data-max-length="8"
                            data-min-value="0" data-max-value="100" data-integer-length="3" data-validate-type="money"
                            data-type="money" data-digits="2"/>
            </k-form-item>
          </div>
          <k-btn class="md-info md-just-icon md-simple" style="display: inline-block;" data-descript="新增"
                 @click="()=>envItems.push({})" v-show="T8ProdPerformance.baseType == '3' || T8ProdPerformance.baseType == '5'">
            <md-icon>add</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" style="display: inline-block;" data-descript="删除当前行"
                 @click="deleteEvent(index)" v-show="T8ProdPerformance.baseType == '3' || T8ProdPerformance.baseType == '5'">
            <md-icon md-src="/static/svg/delete.svg"/>
          </k-btn>
        </k-form>
        <k-form-item label="是否存在超额业绩报酬">
          <k-field-radio v-model="T8ProdPerformance.excessStatus" :data-data="options" data-on-object
                         @data-on-change="excessChange" :data-allowblank="false"
          />
        </k-form-item>
        <k-form-item label="业绩报酬提取比例%" v-if="T8ProdPerformance.excessStatus=='true'" key="performanceOut">
          <k-field-text v-model="T8ProdPerformance.performanceOut" :data-max-length="8"
                        data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"
                        data-type="money" data-digits="2" :data-allowblank="false" />
        </k-form-item>
<!--        <k-form-item label="自定义利率%" v-if="(T8ProdPerformance.baseType != '5' || T8ProdPerformance.baseType != '3') && isShareSort ==='0'" key="custom">-->
<!--          <k-field-text v-model="T8ProdPerformance.custom" :data-max-length="8"-->
<!--                        data-min-value="0" data-max-value="100" :data-allowblank="!(T8ProdPerformance.baseType == '5' && isShareSort ==='0')" data-integer-length="3" data-validate-type="money"-->
<!--                        data-type="money" data-digits="2"/>-->
<!--        </k-form-item>-->

        <!--      <k-form-item label="计提比例%" v-if="!switchSegmentValue  && T8ProdPerformance.baseType == '2'">-->
        <!--        <k-field-text v-model="T8ProdPerformance.indexProfit" :data-max-length="8"-->
        <!--                      data-min-value="0" data-integer-length="3" data-validate-type="money"-->
        <!--                      data-type="money" data-digits="2"/>-->
        <!--      </k-form-item>-->
        <k-form-item label="超额业绩报酬（产品要素表格使用）" :data-col="2" v-if="T8ProdPerformance.excessStatus=='true'">
          <k-field-text v-model="T8ProdPerformance.excessPerfExplain" :data-max-length="2000" inputType="textarea"
                        :rows="5" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="超额业绩报酬（产品费用及税收使用）" :data-col="2" v-if="T8ProdPerformance.excessStatus=='true'">
          <k-field-text v-model="T8ProdPerformance.excessPerfMethod" :data-max-length="2000" inputType="textarea" :data-allowblank="false"
                        :rows="5" @data-on-change="handleExcessPerfMethod"/>
        </k-form-item>
        <k-form-item label="超额业绩报酬计提基准" :data-col="2" v-if="T8ProdPerformance.excessStatus=='true'">
          <k-field-text v-model="T8ProdPerformance.performanceProvision" :data-max-length="2000" inputType="textarea"
                        :rows="5" :data-allowblank="false"/>
        </k-form-item>
        <!-- @data-on-change="handleStrexcessPerfExplain"-->

        <k-form-item label="业绩比较基准（产品要素、投资管理使用）" :data-col="2" v-if="isShareSort ==='0' && (T8ProdPerformance.baseType != '0' && T8ProdPerformance.baseType != '1' && T8ProdPerformance.baseType != '2')">
          <k-field-text v-model="T8ProdPerformance.perfMethodExplain" :data-max-length="2000" inputType="textarea"
                        :rows="5" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="业绩基准测算依据" :data-col="2">
          <k-field-text v-model="T8ProdPerformance.prodAchievement" :data-max-length="2000" inputType="textarea"
                        :rows="5" :data-allowblank="false" />
          <!-- @data-on-change="handleStrprodAchievement"-->
        </k-form-item>
      </div>
    </k-form>

    <div v-if="this.isShowTable && T8ProdPerformance.baseType != '0'">
      <div class="my-container" v-if="T8ProdPerformance.baseType == '2' && isShareSort ==='0'">
        <div class="my-item2">
          <div style="width: 135px !important">
            <md-switch v-model="switchSegmentValue" class="md-info" @change="changeSegmentType">分段计提</md-switch>
          </div>
          <div class="my-item2-chips">
            <md-chips v-model="moneyList" class="md-primary" style="padding-top: 4px !important;"
                      md-placeholder="请输入基准利率区间,并按回车确认"
                      md-input-type="number"
                      @md-delete="deleteMoney" @md-insert="insertMoney"
                      v-show="switchSegmentValue && T8ProdPerformance.baseType == '2'"></md-chips>
          </div>
        </div>
      </div>

      <div label="分段计提展示" class="my-table" v-if="switchSegmentValue  && T8ProdPerformance.baseType == '2' && isShareSort === '0'">
        <md-table>
          <md-table-row>
            <md-table-head v-show="moneyList.length > 0  && T8ProdPerformance.baseType == '2'">基准利率区间%</md-table-head>
            <md-table-head v-show="moneyList.length > 0  && T8ProdPerformance.baseType == '2'"> {{feeTableHead}}
            </md-table-head>
          </md-table-row>

          <!--没有数据时才展示这一行 -->
          <md-table-row v-show="timeList.length == 0 && moneyList.length == 0 && T8ProdPerformance.baseType == '2'">
            <md-table-cell :colspan="3" style="text-align:center">
              <p> 暂无数据</p>
            </md-table-cell>
          </md-table-row>

          <md-table-row v-show="timeList.length > 0 || moneyList.length > 0 && T8ProdPerformance.baseType == '2'"
                        v-for="(item,index) in tailingCommisionList" :key="index">
            <md-table-cell v-show="item.showMoneyTd && T8ProdPerformance.baseType == '2'" :rowspan="item.moneyRowspan">
              {{ item.moneyDesc }}
            </md-table-cell>
            <md-table-cell v-show="item.showTimeTd && T8ProdPerformance.baseType == '2'">
              {{ item.timeDesc }}
            </md-table-cell>
            <md-table-cell>
              <md-field>
                <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.rate"
                          type="number"></md-input>
                <span class="md-suffix">%</span>
              </md-field>
            </md-table-cell>
          </md-table-row>
        </md-table>
      </div>


<!--      <k-form ref="addForm2" v-for="(item,index) in envItems" :key="index"-->
<!--              v-if="(T8ProdPerformance.baseType == '3' || T8ProdPerformance.baseType == '5') && isShareSort === '0'"-->
<!--              :data-col="6" data-input-width="120px" data-label-width="100px" data-total-width="1300px">-->
<!--        <div class="md-addForm2" style="display: flex;justify-content: space-evenly;align-items: center;width:1118px;text-align: left;float: left">-->
<!--          <k-form-item :label="index=='0'?'':'操作符'" >-->
<!--            <k-field-select v-model="item.operator" data-dict="t8_performance_operator" data-display-field="itemval"  data-value-field="itemkey"-->
<!--                            :data-allowblank="false" v-if="index != '0'"></k-field-select>-->
<!--          </k-form-item>-->
<!--          <k-form-item :label="'数据类型'" v-if="T8ProdPerformance.baseType == '3'">-->
<!--            <k-field-select v-model="item.ratioType" data-dict="t8_performance_ratio_type" data-display-field="itemval"  data-value-field="itemkey"-->
<!--                            data-default-value="1" data-disabled="true"-->
<!--                            :data-allowblank="false" ></k-field-select>-->
<!--          </k-form-item>-->
<!--          <k-form-item :label="'数据类型'" v-if="T8ProdPerformance.baseType == '5'">-->
<!--            <k-field-select v-model="item.ratioType" data-dict="t8_performance_ratio_type" data-display-field="itemval"  data-value-field="itemkey"-->
<!--                            :data-allowblank="false"></k-field-select>-->
<!--          </k-form-item>-->
<!--          <k-form-item :label="'指数名称'" v-if="item.ratioType == '1' || item.ratioType == null || item.ratioType == ''">-->
<!--            <k-field-select v-model="item.ratioIndex" data-value-field="indexCode" :data-allowblank="false"-->
<!--                            data-display-field="indexName" data-action="T8IndexInfo.find"/>-->
<!--          </k-form-item>-->
<!--          <k-form-item :label="'自定义利率'" v-if="item.ratioType == '2'">-->
<!--            <k-field-text v-model="item.fixedValue" :data-allowblank="false" :data-max-length="8"-->
<!--                          data-min-value="0"  data-validate-type="money"-->
<!--                          data-type="money" data-digits="2"/>-->
<!--          </k-form-item>-->
<!--          <k-form-item :label="'市场利率'" v-if="item.ratioType == '3'">-->
<!--            <k-field-text v-model="item.marketRate" :data-allowblank="false"-->
<!--                          data-dict="t8_market_rate" data-display-field="itemval"  data-value-field="itemkey"/>-->
<!--          </k-form-item>-->
<!--            <k-form-item :label="'系数%'">-->
<!--              <k-field-text v-model="item.coefficient" :data-allowblank="false" :data-max-length="8"-->
<!--                            data-min-value="0" data-max-value="100" data-integer-length="3" data-validate-type="money"-->
<!--                            data-type="money" data-digits="2"/>-->
<!--            </k-form-item>-->
<!--        </div>-->
<!--        <k-btn class="md-info md-just-icon md-simple" style="display: inline-block;" data-descript="新增"-->
<!--               @click="()=>envItems.push({})" v-show="T8ProdPerformance.baseType == '3' || T8ProdPerformance.baseType == '5'">-->
<!--          <md-icon>add</md-icon>-->
<!--        </k-btn>-->
<!--        <k-btn class="md-info md-just-icon md-simple" style="display: inline-block;" data-descript="删除当前行"-->
<!--               @click="deleteEvent(index)" v-show="T8ProdPerformance.baseType == '3' || T8ProdPerformance.baseType == '5'">-->
<!--          <md-icon md-src="/static/svg/delete.svg"/>-->
<!--        </k-btn>-->
<!--      </k-form>-->

    </div>

    <k-form dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-footer data-align="center" v-show="menuName == 'M81007'">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdPerformance.addT8ProdPerformance"
               :data-model="T8ProdPerformance" :data-handler="submitHandle" :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
    </k-form>


  </div>
</template>

<script>
    import Tools from "@/utils/tools";
    import eventBus from "@/utils/eventBus";

    export default {
        computed: {
            switchSegmentValue2() {
                return this.T8ProdPerformance.switchSegmentValue
            }
        },
        model: {
            prop: 'T8ProdPerformance',
            event: 'input'
        },
        props: {
            menuName: '',
            T8ProdPerformance: {
                excessStatus:'',
                type: Object,
                default: {
                    baseType: "1",
                    moneyList: [],
                    switchSegmentValue: false,
                    tailingCommisionList: [],
                    tailingCommisionMoneyList: [],
                },
            },
            prodIsShareSort: {
                type: Boolean,
                default: true
            },
            prodCode: {
                type: String,
                default: ''
            },
            t8ProdInfoId: {
                type: String,
                default: ''
            },
        },
        data() {
            return {

                options:[{
                    value: 'true',
                    label: '是',
                },{
                    value: 'false',
                    label: '否',
                }],
                envItems: [{"ratioIndex": ''}, {"coefficient": ''}, {"operator": ""}, {"ratioType": ""},{"ratioIndex":""},{"marketRate":""}],

                userid: localStorage.getItem("userid"),
                formData: {
                    t8PrjFeeLists: '',
                    rateType: '0',
                    constantRate: null
                },
                switchSegmentValue: false,   //金额分段切换按钮的值
                constantRatesSitch: true,     //固定切换按钮的值
                moneyList: [],
                timeList: [],
                tailingCommisionList: [],
                tailingCommisionMoneyList: [],
                tailingCommisionTimeList: [],
                feeTableHead: "计提比例%",
                maxMoneyValue: 99999999999999.99,
                maxRateValue: 100,
                currentWorkday: null,
                isShareSort: '0',
                isShowTable:true,
            }
        },
        methods: {
            excessChange(value){
               if(value=='false'){
                    this.$set(this.T8ProdPerformance,'performanceOut','');
                }
            },
            passDataSuccess() {
                this.$emit('isShowButton', '1')
            },
            validateData() {
                return this.$refs.performanceInfo.validate();
            },
            forceUpdate() {
                this.$forceUpdate();
            },

            deleteEvent(index) {
                if (this.envItems.length > 1) {
                    this.envItems.splice(index, 1)
                }
            },

            inlitFrom: function (value) {
              this.$emit('getBaseType',value)
              this.envItems=[{"ratioIndex": ''}, {"coefficient": ''}, {"operator": ""}, {"ratioType": ""},{"ratioIndex":""},{"marketRate":""}];
                let temp = this.T8ProdPerformance.baseType;
                if(!temp || temp==='0'){
                    this.isShowTable=false;
                }else{
                    this.isShowTable=true;
                }
                //表单制空
                this.$set(this.T8ProdPerformance, "custom", '');
                this.$set(this.T8ProdPerformance, "baseMinRate", '');
                this.$set(this.T8ProdPerformance, "baseMaxRate", '');
                this.$set(this.T8ProdPerformance, "marketRate", '');
                this.$set(this.T8ProdPerformance, "indexProfit", '');
                this.$set(this.T8ProdPerformance, "performanceOut", '');
                this.$set(this.T8ProdPerformance, "baseRate", '');
                this.changeSegmentType();
            },

            changeSegmentType: function (value) {

                //金额分段
                if (value) {
                    this.$set(this.T8ProdPerformance, "indexProfit", '');
                    this.constantRatesSitch = false;
                } else if (value == false) {
                    //分段都为空，则固定费率不能为空
                    this.constantRatesSitch = true;
                }

                this.moneyList = []
                this.formData.constantRate = null
                this.formData.rateType = this.formData.rateType == '0' ? '1' : '0'
                if (this.T8ProdPerformance.baseType == '3' || this.T8ProdPerformance.baseType == '5') {
                    this.envItems.length = 1;
                }

                //重新生成列表
                this.buildTimeTable();
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
            handleExcessPerfMethod(val){
                if(val.indexOf("\\n")){
                    let arrys=[];
                    arrys = val.split("\n");
                    var str ="";
                    for(var i = 0; i<arrys.length;i++){
                        if(i<arrys.length-1){
                            str = str+"\u3000\u3000"+arrys[i].toString().trim()+"\n"
                        }else{
                            str = str+"\u3000\u3000"+arrys[i].toString().trim()
                        }

                    }
                    this.T8ProdPerformance.excessPerfMethod = str;
                }
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


            submitHandle(value) {
                console.log("123123");
                this.$set(value, 'assemblyMenuType', 'performanceInfo');
                this.$set(value, 'prodIsShareSort', this.prodIsShareSort);
                //验证
                let result = true;
                let result1 = true;
                let result2 = true;
                result1 = this.$refs.performanceInfo.validate();
                if (this.isShareSort == '0' && (this.T8ProdPerformance.baseType=='3' || this.T8ProdPerformance.baseType=='5')) {
                    for (var i = 0;i< this.envItems.length;i++) {
                        result2 = this.$refs.addForm2[i].validate();
                        if (!result2) {
                            break;
                        }
                    }
                }
                if (result1 && result2) {
                    result = true;
                } else {
                    result = false;
                }

                if (!result) {
                    return false;
                }
                if (this.T8ProdPerformance.baseType == '3' || this.T8ProdPerformance.baseType == '5') {
                    value["t8ProdPerformanceRatios"] = JSON.stringify(this.envItems);
                }
                if (this.T8ProdPerformance.baseType == '2') {
                    value["t8PrjTailingCommisionList"] = JSON.stringify(this.tailingCommisionList);
                    value["t8PrjFeeLists"] = JSON.stringify(this.tailingCommisionList);

                }

                return value;
            },
            handleStrexcessPerfExplain(val){

                if(val.indexOf("\\n")){
                    let arrys=[];
                    arrys = val.split("\n");
                    var str ="";
                    for(var i = 0; i<arrys.length;i++){
                        if(i<arrys.length-1){
                            str = str+"\u3000\u3000"+arrys[i].toString().trim()+"\n"
                        }else{
                            str = str+"\u3000\u3000"+arrys[i].toString().trim()
                        }

                    }
                    this.T8ProdPerformance.excessPerfExplain = str;
                }
            },
            handleStrprodAchievement(val){

                if(val.indexOf("\\n")){
                    let arrys=[];
                    arrys = val.split("\n");
                    var str ="";
                    for(var i = 0; i<arrys.length;i++){
                        if(i<arrys.length-1){
                            str = str+"\u3000\u3000"+arrys[i].toString().trim()+"\n"
                        }else{
                            str = str+"\u3000\u3000"+arrys[i].toString().trim()
                        }

                    }
                    this.T8ProdPerformance.prodAchievement = str;
                }
            },
            initTable: function (rows) {

                rows.forEach((e, index) => {
                    if (!(e.dimension2Min == null || e.dimension2Min == '')) {
                        if (e.dimension2Min != "0" && (this.timeList.indexOf(e.dimension2Min) === -1)) {
                            this.timeList.push(e.dimension2Min);
                        }
                        this.switchTimeValue = true;
                    } else {
                        this.switchTimeValue = false;
                    }
                })
                rows.forEach((e, index) => {
                    let showMoneyFlag = false;
                    if (this.switchSegmentValue && this.switchTimeValue && e.dimension2Min == "0") {
                        //如果是二维，则只有第一个时间分段展示金额的描述
                        showMoneyFlag = true;
                    } else if (this.switchSegmentValue && !this.switchTimeValue) {
                        //只是金额分段,最小值不为0就要展示
                        if (e.dimension1Min != null) {
                            showMoneyFlag = true;
                        }
                    } else {
                        //其他情况金额分段描述都不用展示
                        showMoneyFlag = false;
                    }

                    this.tailingCommisionList.push({
                        dimension1Min: e.dimension1Min,
                        dimension1Max: e.dimension1Max,
                        dimension2Min: e.dimension2Min,
                        dimension2Max: e.dimension2Max,
                        moneyDesc: e.dimension1Min == null ? null : (e.dimension1Min + ' <= 金额 < ' + (e.dimension1Max == -1 ? '∞' : e.dimension1Max)),
                        timeDesc: e.dimension2Min == null ? null : (e.dimension2Min + ' <= 天数 < ' + (e.dimension2Max == -1 ? '∞' : e.dimension2Max)),
                        rate: e.rate,
                        moneyRowspan: e.dimension2Min == "0" ? this.timeList.length + 1 : 1,    //2维的时候金额分段只要一条，所以这里选第一条来占行数，后面的都不展示
                        showMoneyTd: showMoneyFlag,
                        showTimeTd: e.dimension2Min == null ? false : true
                    })
                })
            },
        },


        created() {

            // this.T8ProdPerformance.t8ProdInfoId = this.T8ProdPerformance.t8ProdInfoId;
            // this.value = this.t8ProdInfoId;
            //console.log("performance--this.prodIsShareSort=:>>",this.prodIsShareSort);
            //bus通信控制份额分类组件的显示与隐藏
            eventBus.$on('shareSortChange', item => {
                if (item.shareSort === '1') {
                    this.isShareSort = '1'
                } else {
                    this.isShareSort = '0'
                }
            })
        },
        watch: {
            switchSegmentValue2(value) {
                this.switchSegmentValue = value;
                this.changeSegmentType(value)
            },
            'T8ProdPerformance.baseType':{
                handler(newVal, oldVal){
                    eventBus.$emit('baseTypeChange', {'baseType': newVal})
                },
                immediate:true
            },


        },
        mounted() {
        }
    }
</script>

<style scoped>
</style>

