<template>
    <div>
        <div>
            <k-form-search-customize v-model="searchParam" data-target="t8ProdShareBonusGrid">
                <k-form-item label="产品代码">
                    <k-field-select v-model="searchParam.prodCode" data-action="T8ProdInfo.getProdInfos"
                                    data-value-field="prodCode"
                                    data-display-field="prodCode,prodName"></k-field-select>
                </k-form-item>
                <k-form-item label="分红频率">
                    <k-field-select v-model="searchParam.bonusModel" data-dict="t8_prod_share_bonus_frequency"></k-field-select>
                </k-form-item>
                <k-form-item label="是否启用">
                    <k-field-select v-model="searchParam.isEnabled" data-dict="1yes0no"></k-field-select>
                </k-form-item>
                <k-btn class="btn-custom-primary" slot="button" :data-handler="openProdShareBonusPopup" v-if="global.isShowAuthorityButton('T8ProdShareBonus.findT8ProdShareBonus1')">
                    新增
                </k-btn>
            </k-form-search-customize>
        </div>
        <div>
            <k-grid ref="t8ProdShareBonusGrid" @data-row-select="selectRow"
                    data-action="T8ProdShareBonus.findT8ProdShareBonus1">
                <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
                <k-grid-column data-header="产品名称" data-name="prodName"></k-grid-column>
                <k-grid-column data-header="分红频率" data-name="bonusModel"
                               data-dict="t8_prod_share_bonus_frequency"></k-grid-column>
                <k-grid-column data-header="间隔月数" data-name="cycleMonth"></k-grid-column>
                <k-grid-column data-header="当月日期" data-name="day" data-data="openMonthlyOptions"></k-grid-column>
                <k-grid-column data-header="起始日期" data-name="startDate" data-type="date"></k-grid-column>
                <k-grid-column data-header="是否启用" data-name="isEnabled" data-dict="1yes0no"></k-grid-column>
                <k-grid-column data-header="创建人" data-name="crtUser"></k-grid-column>
                <k-grid-column data-header="创建时间" data-name="crtDate"
                               data-render="renderDateTimeCreate"></k-grid-column>
                <!--		<k-grid-column data-header="创建时间" data-name="crtTime"></k-grid-column>-->
                <template slot="operate" slot-scope="scope">
                    <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品分红设置" data-functype="POPUP"
                           data-size="mini"
                           data-target="editT8ProdShareBonusPopup"
                           @click="editT8ProdBonus(scope.row.row)">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                           data-action="T8ProdShareBonus.deleteT8ProdShareBonus" data-size="mini"
                           data-type="danger" data-target="t8ProdShareBonusGrid" :data-confirm="true"
                           data-descript="删除产品分红设置">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </div>

        <!--    添加产品分红设置实体类弹出框   -->
        <k-popup ref="addT8ProdShareBonusPopup" data-title="新增">
            <k-form ref="addT8ProdShareBonusForm" :data-col="2">

                <k-form-item label="产品代码">
                    <k-field-select v-model="formData.prodCode"
                                    data-action='T8ProdShareBonus.findProdInfoBySeries'
                                    data-value-field="prodCode"
                                    data-display-field="prodCode,prodName"
                                    :dataAllowblank="false"
                                    @data-on-change="prodCodeChange"/>
                </k-form-item>
                <k-form-item label="产品名称">
                    <k-field-text v-model="formData.prodName" :data-disabled="true"
                                    :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="id" v-if="false">
                    <k-field-text v-model="formData.t8ProdInfoId"/>
                </k-form-item>
                <k-form-item label="分红频率">
                    <k-field-select v-model="formData.bonusModel" data-dict="t8_prod_share_bonus_frequency"
                                    :data-allowblank="false"  @data-on-change="changeBonusModel"/>
                </k-form-item>
                <k-form-item label="产品成立日" v-if="formData.bonusModel == 4" key="establishDate">
                    <k-field-date v-model="formData.establishDate" :dataAllowblank="false" data-type="date" :data-disabled="true"></k-field-date>
                </k-form-item>

                <k-form-item label="首次分红日" v-if="formData.bonusModel == 5" key="startDate">
                    <k-field-date v-model="formData.startDate" :dataAllowblank="false" data-type="date" @data-on-change="changeStartDate" :data-workday="true" :dataWorkdayPgmno="this.workday"/>
                </k-form-item>
                <div class="fc-button-group" style="float:left; padding: 0px 0px 20px 60px;" v-if="formData.bonusModel == 5 || formData.bonusModel == 4">
                    <span>间隔</span>
                    <span style="color: red">*</span>
                    <k-field-text v-model="formData.cycleMonth" style="width: 65px;" data-validate-type="int"
                                  data-min-value="1" data-max-value="100" :data-max-length="3" key="cycleMonth"></k-field-text>
                    <span>月，</span>
                    <span style="color: red">*</span>
                    <k-field-select v-model="formData.day" :data-data="openMonthlyOptions" style="width: 80px;"
                                    data-display-field="label" data-value-field="value" key="cycleDay"/>
                    <span>分红</span>
                </div>
                <div  class="fc-button-group1" style="float:left; padding: 0px 0px 20px 60px;" v-if="formData.bonusModel == 6">
                    <span>分红日</span>
                    <template>
                        <k-btn class="md-info md-just-icon md-simple" data-descript="分红日设置" :data-handler="()=> this.showListWorkDay = true"
                               data-functype="POPUP" data-size="big" data-target="workdayPopup">
                            <!--						<md-icon md-src="/static/svg/workday.svg" />-->
                            <md-icon>add_to_queue</md-icon>
                        </k-btn>
                    </template>
                </div>

                <!-- 工作日弹出框 -->
                <div class="popClass">
                    <k-popup @data-closed="()=>{this.selectPgmno='';this.$refs.shareBonusDay.hackReset=false}"
                             @data-opened="()=>{this.$refs.shareBonusDay.hackReset=true;this.selectPgmno='1'}"
                             ref="workdayPopup" data-width="80%">
                        <div style="overflow: auto;padding-top: 20px">
                            <ShareBonusDay ref="shareBonusDay" :pgmno="selectPgmno" :t8ProdInfoId="formData.t8ProdInfoId" :isShowSave="true"></ShareBonusDay>
                        </div>
                    </k-popup>
                </div>


                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdShareBonus.addT8ProdShareBonus"
                           data-from="addT8ProdShareBonusForm"
                           :data-model="formData" data-target="t8ProdShareBonusGrid" :data-handler="saveProdShareBonus">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                    </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">
                        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
                    </k-btn>
                </k-form-footer>
            </k-form>
        </k-popup>

        <!--    修改产品分红设置实体类弹出框   -->
        <k-popup ref="editT8ProdShareBonusPopup" data-title="修改">
            <k-form ref="editT8ProdShareBonusForm" :data-col="2">
                <k-form-item label="产品代码">
                    <k-field-select v-model="formData.prodCode"
                                    data-action='T8ProdShareBonus.findProdInfoBySeries'
                                    data-value-field="prodCode"
                                    data-display-field="prodCode,prodName"
                                    :dataAllowblank="false"
                                    @data-on-change="prodCodeChange"/>
                </k-form-item>
                <k-form-item label="产品名称">
                    <k-field-text v-model="formData.prodName" :data-disabled="true" :dataAllowblank="false"/>
                </k-form-item>
                <k-form-item label="产品id" v-if="false">
                    <k-field-text v-model="formData.t8ProdInfoId"/>
                </k-form-item>
                <k-form-item label="分红频率">
                    <k-field-select v-model="formData.bonusModel" :dataAllowblank="false"
                                    data-dict="t8_prod_share_bonus_frequency"
                                    @data-on-change="changeBonusModel"/>
                </k-form-item>
                <k-form-item label="产品成立日" v-if="formData.bonusModel == 4" key="establishDate">
                    <k-field-date v-model="formData.establishDate" :dataAllowblank="false" data-type="date" :data-disabled="true"></k-field-date>
                </k-form-item>
                <k-form-item label="起始日" v-if="formData.bonusModel == 5" key="startDate">
                    <k-field-date v-model="formData.startDate" :dataAllowblank="false" data-type="date"></k-field-date>
                </k-form-item>
                <div class="fc-button-group" style="float:left; padding: 0px 0px 20px 60px;" v-if="formData.bonusModel == 5 || formData.bonusModel == 4">
                    <span>间隔</span>
                    <span style="color: red">*</span>
                    <k-field-text v-model="formData.cycleMonth" style="width: 65px;" data-validate-type="number"
                                  data-min-value="1" data-max-value="100" :data-max-length="3" key="cycleMonth"></k-field-text>
                    <span>月，</span>
                    <span style="color: red">*</span>
                    <k-field-select v-model="formData.day" :data-data="openMonthlyOptions" style="width: 80px;"
                                    data-display-field="label" data-value-field="value" key="cycleDay"/>
                    <span>分红</span>
                </div>
                <div  class="fc-button-group1" style="float:left; padding: 0px 0px 20px 60px;" v-if="formData.bonusModel == 6">
                    <span>分红日</span>
                    <template>
                        <k-btn class="md-info md-just-icon md-simple" data-descript="分红日设置" :data-handler="()=> this.showListWorkDay = true"
                               data-functype="POPUP" data-size="big" data-target="workdayPopup">
                            <!--						<md-icon md-src="/static/svg/workday.svg" />-->
                            <md-icon>add_to_queue</md-icon>
                        </k-btn>
                    </template>
                </div>
                <!-- 工作日弹出框 -->
                <div class="popClass">
                    <k-popup @data-closed="()=>{this.selectPgmno='';this.$refs.shareBonusDay.hackReset=false}"
                             @data-opened="()=>{this.$refs.shareBonusDay.hackReset=true;this.selectPgmno='1'}"
                             ref="workdayPopup" data-width="80%">
                        <div style="overflow: auto;padding-top: 20px">
                            <ShareBonusDay ref="shareBonusDay" :pgmno="selectPgmno" :t8ProdInfoId="formData.t8ProdInfoId" :isShowSave="true"></ShareBonusDay>
                        </div>
                    </k-popup>
                </div>

                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                           data-action="T8ProdShareBonus.updateT8ProdShareBonus" data-from="editT8ProdShareBonusForm"
                           :data-model="formData" data-target="t8ProdShareBonusGrid"  :data-handler="saveProdShareBonus">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                    </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">
                        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
                    </k-btn>
                </k-form-footer>
            </k-form>
        </k-popup>
    </div>
</template>

<script>
    import Tools from "@/utils/tools";
    import ShareBonusDay from  "../../../pages/pms/M81/prodInfoGD/ShareBonusDay.vue";

    export default {
        name:"T8ProdShareBonus",
        components: {ShareBonusDay},
        data() {
            return {
                formData: {
                    t8ProdInfoId:'',
                    prodName:'',
                },
                searchParam: {},
                selectRowData: {},
                prodList: [],
                openMonthlyOptions: [
                    {value: '01',  label: '1号' },
                    {value: '02',  label: '2号' },
                    {value: '03',  label: '3号' },
                    {value: '04',  label: '4号' },
                    {value: '05',  label: '5号' },
                    {value: '06',  label: '6号' },
                    {value: '07',  label: '7号' },
                    {value: '08',  label: '8号' },
                    {value: '09',  label: '9号' },
                    {value: '10', label: '10号'},
                    {value: '11', label: '11号'},
                    {value: '12', label: '12号'},
                    {value: '13', label: '13号'},
                    {value: '14', label: '14号'},
                    {value: '15', label: '15号'},
                    {value: '16', label: '16号'},
                    {value: '17', label: '17号'},
                    {value: '18', label: '18号'},
                    {value: '19', label: '19号'},
                    {value: '20', label: '20号'},
                    {value: '21', label: '21号'},
                    {value: '22', label: '22号'},
                    {value: '23', label: '23号'},
                    {value: '24', label: '24号'},
                    {value: '25', label: '25号'},
                    {value: '26', label: '26号'},
                    {value: '27', label: '27号'},
                    {value: '28', label: '28号'},
                ],
                selectPgmno: '',
                t8ProdInfoId:'',
                workday:'',
                establishDate:'',
            };
        },
        methods: {
            selectRow(row, column, event) {
                this.selectRowData = Object.assign({}, row)
                this.formData = Object.assign({}, row)
            },
            renderDateTimeCreate(row) {
                return Tools.formatDateTime(row.crtDate, row.crtTime);
            },
            changeBonusModel(val) {
                this.$set(this.formData,"cycleMonth","");
                this.$set(this.formData,"day","");
                this.$set(this.formData,"startDate","");
                this.$set(this.formData,"establishDate",this.establishDate);
                if(val == '4') {
                    let day = this.formData.establishDate.substring(6);
                    this.$set(this.formData,"day",day);
                }
            },
            changeStartDate(val) {
                let day = this.formData.startDate.substring(6);
                this.$set(this.formData,"day",day);
            },
            saveProdShareBonus() {
                let result = this.$refs.addT8ProdShareBonusPopup.validate();
                let r1 = true;
                if (this.formData.bonusModel == '4' || this.formData.bonusModel == '5') {
                    if (this.formData.cycleMonth == '' || this.formData.day == '') {
                        Tools.alert("间隔月数和间隔日期不能为空!","danger")
                        r1 = false;
                    }
                }

                if (result && r1) {
                    return true;
                }
                return false;
            },
            openProdShareBonusPopup() {
                this.formData = {
                    prodCode:'',
                    bonusModel: '',
                    cycleMonth:'',
                    day:'',
                    startDate:'',
                }
                this.$refs.addT8ProdShareBonusPopup.popup();
            },
            prodCodeChange(val){
                this.httpUtil.comnQuery({
                    action: "T8ProdInfo.findProdDividendInfo",
                    params: {prodCode: val}
                }).then(data => {
                    this.$set(this.formData,"t8ProdInfoId",data.rows[0].id);
                    this.$set(this.formData,"prodName",data.rows[0].prodName);
                    this.$set(this.formData,"establishDate",data.rows[0].establishDate);
                    this.establishDate = data.rows[0].establishDate;
                    this.workday = data.rows[0].pgmno;
                }).catch({});
            },
            editT8ProdBonus(val) {
                this.httpUtil.comnQuery({
                    action: "T8ProdInfo.findProdDividendInfo",
                    params: {prodCode: val.prodCode}
                }).then(data =>{
                    this.$set(this.formData,"establishDate",data.rows[0].establishDate);
                    this.establishDate = data.rows[0].establishDate;
                })
            },
        }
    };
</script>
