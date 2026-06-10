<template>
    <div>
        <div>
            <k-form-search-customize v-model="searchParam" data-target="t8ProdBonusTaskGrid">
                <k-form-item label="产品代码">
                    <k-field-select v-model="searchParam.prodCode" data-action="T8ProdInfo.getProdInfos"
                                    data-value-field="prodCode"
                                    data-display-field="prodCode,prodName"></k-field-select>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-date v-model="searchParam.shareBaseDate"></k-field-date>
                </k-form-item>
                <k-form-item label="产品经理">
                    <k-field-select v-model="searchParam.productManagerName" data-action="User.getAllUser"
                                    data-display-field="label"
                                    data-value-field="label"></k-field-select>
                </k-form-item>
                <k-form-item label="投资经理">
                    <k-field-select v-model="searchParam.investManagerName" data-action="User.getAllUser"
                                    data-display-field="label"
                                    data-value-field="label"></k-field-select>
                </k-form-item>
                <k-form-item label="分红频率">
                    <k-field-select v-model="searchParam.bonusModel" data-dict="t8_prod_share_bonus_frequency">
                    </k-field-select>
                </k-form-item>
            </k-form-search-customize>
        </div>
        <div>
            <k-grid ref="t8ProdBonusTaskGrid" @data-row-select="selectRow"
                    data-action="T8ProdBonusTask.findT8ProdBonusTasks1">
                <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
                <k-grid-column data-header="产品名称" data-name="prodName" data-width="150"></k-grid-column>
                <k-grid-column data-header="分红频率" data-name="bonusModel"
                               data-dict="t8_prod_share_bonus_frequency" data-width="150"></k-grid-column>
                <k-grid-column data-header="分红基准日" data-name="shareBaseDate" data-type="date"></k-grid-column>
                <k-grid-column data-header="提醒开始日期" data-name="remindStartDate" data-type="date"></k-grid-column>
                <k-grid-column data-header="提醒结束日期" data-name="remindEndDate" data-type="date"></k-grid-column>
                <k-grid-column data-header="已提醒次数" data-name="remindNumber"></k-grid-column>
                <k-grid-column data-header="是否提醒" data-name="remindStatus" data-dict="1yes0no"></k-grid-column>
                <k-grid-column data-header="产品经理id" data-name="productManagerId" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="产品经理" data-name="productManagerName"></k-grid-column>
                <k-grid-column data-header="产品经理邮箱" data-name="productManagerEmail" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="投资经理id" data-name="investManagerId" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="投资经理" data-name="investManagerName"></k-grid-column>
                <k-grid-column data-header="投资经理邮箱" data-name="investManagerEmail" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="创建日期" data-name="crtDate" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="创建时间" data-name="crtTime"
                               data-render="renderDateTimeCreate" data-width="150"></k-grid-column>
                <k-grid-column data-header="创建人id" data-name="crtUserId" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="创建人名称" data-name="crtUserName" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="更新日期" data-name="updDate" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="更新时间" data-name="updTime" data-render="renderDateTimeUpdate" data-width="150"></k-grid-column>
                <k-grid-column data-header="更新人id" data-name="updUserId" data-hidden="true"></k-grid-column>
                <k-grid-column data-header="更新人名称" data-name="updUserName" data-hidden="true"></k-grid-column>
                <template slot="operate" slot-scope="scope">
                    <k-btn class="md-info md-just-icon md-simple" data-descript="启用" data-functype="SUBMIT"
                           :data-disabled="scope.row.row.remindStatus == '1'"
                           data-action="T8ProdBonusTask.updateStatusOnEnable"
                           :data-confirm="true"
                           data-target="t8ProdBonusTaskGrid"
                           data-size="mini">
                        <md-icon>lock_open</md-icon>
                    </k-btn>
                    <k-btn class="md-info md-just-icon md-simple" data-descript="停用"
                           :data-disabled="scope.row.row.remindStatus=='0'"
                           data-action="T8ProdBonusTask.updateStatusOnStop" :data-confirm="true"
                           data-target="t8ProdBonusTaskGrid" data-functype="SUBMIT" data-size="mini"
                    >
                        <md-icon>lock</md-icon>
                    </k-btn>
<!--                    <k-btn class="md-info md-just-icon md-simple" data-descript="修改分红提醒任务" data-functype="POPUP"-->
<!--                           data-size="mini"-->
<!--                           data-target="editT8ProdBonusTaskPopup">-->
<!--                        <md-icon>edit</md-icon>-->
<!--                    </k-btn>-->
<!--                    <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"-->
<!--                           data-action="T8ProdBonusTask.deleteT8ProdBonusTask" data-size="mini"-->
<!--                           data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true"-->
<!--                           data-descript="删除分红提醒任务">-->
<!--                        <md-icon>close</md-icon>-->
<!--                    </k-btn>-->
                </template>
            </k-grid>
        </div>

        <!--    添加分红提醒任务弹出框   -->
        <k-popup ref="addT8ProdBonusTaskPopup" data-title="新增">
            <k-form ref="addT8ProdBonusTaskForm" :data-col="2">
                <k-form-item label="id">
                    <k-field-text v-model="formData.id"/>
                </k-form-item>
                <k-form-item label="产品代码">
                    <k-field-text v-model="formData.prodCode"/>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-text v-model="formData.shareBaseDate"/>
                </k-form-item>
                <k-form-item label="提醒开始日期">
                    <k-field-text v-model="formData.remindStartDate"/>
                </k-form-item>
                <k-form-item label="提醒结束日期">
                    <k-field-text v-model="formData.remindEndDate"/>
                </k-form-item>
                <k-form-item label="已提醒次数">
                    <k-field-text v-model="formData.remindNumber"/>
                </k-form-item>
                <k-form-item label="是否提醒 0-否 1-是">
                    <k-field-text v-model="formData.remindStatus"/>
                </k-form-item>
                <k-form-item label="产品经理id">
                    <k-field-text v-model="formData.productManagerId"/>
                </k-form-item>
                <k-form-item label="产品经理名字">
                    <k-field-text v-model="formData.productManagerName"/>
                </k-form-item>
                <k-form-item label="产品经理邮箱">
                    <k-field-text v-model="formData.productManagerEmail"/>
                </k-form-item>
                <k-form-item label="投资经理id">
                    <k-field-text v-model="formData.investManagerId"/>
                </k-form-item>
                <k-form-item label="投资经理名字">
                    <k-field-text v-model="formData.investManagerName"/>
                </k-form-item>
                <k-form-item label="投资经理邮箱">
                    <k-field-text v-model="formData.investManagerEmail"/>
                </k-form-item>
                <k-form-item label="创建日期">
                    <k-field-text v-model="formData.crtDate"/>
                </k-form-item>
                <k-form-item label="创建时间">
                    <k-field-text v-model="formData.crtTime"/>
                </k-form-item>
                <k-form-item label="创建人id">
                    <k-field-text v-model="formData.crtUserId"/>
                </k-form-item>
                <k-form-item label="创建人名称">
                    <k-field-text v-model="formData.crtUserName"/>
                </k-form-item>
                <k-form-item label="更新日期">
                    <k-field-text v-model="formData.updDate"/>
                </k-form-item>
                <k-form-item label="更新时间">
                    <k-field-text v-model="formData.updTime"/>
                </k-form-item>
                <k-form-item label="更新人id">
                    <k-field-text v-model="formData.updUserId"/>
                </k-form-item>
                <k-form-item label="更新人名称">
                    <k-field-text v-model="formData.updUserName"/>
                </k-form-item>

                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdBonusTask.addT8ProdBonusTask"
                           data-from="addT8ProdBonusTaskForm"
                           :data-model="formData" data-target="t8ProdBonusTaskGrid">
                        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                    </k-btn>
                    <k-btn class="btn-custom-plain" data-functype="CLOSE">
                        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
                    </k-btn>
                </k-form-footer>
            </k-form>
        </k-popup>

        <!--    修改分红提醒任务弹出框   -->
        <k-popup ref="editT8ProdBonusTaskPopup" data-title="修改">
            <k-form ref="editT8ProdBonusTaskForm" :data-col="2">
                <k-form-item label="id">
                    <k-field-text v-model="formData.id"/>
                </k-form-item>
                <k-form-item label="产品代码">
                    <k-field-text v-model="formData.prodCode"/>
                </k-form-item>
                <k-form-item label="分红基准日">
                    <k-field-text v-model="formData.shareBaseDate"/>
                </k-form-item>
                <k-form-item label="提醒开始日期">
                    <k-field-text v-model="formData.remindStartDate"/>
                </k-form-item>
                <k-form-item label="提醒结束日期">
                    <k-field-text v-model="formData.remindEndDate"/>
                </k-form-item>
                <k-form-item label="已提醒次数">
                    <k-field-text v-model="formData.remindNumber"/>
                </k-form-item>
                <k-form-item label="是否提醒 0-否 1-是">
                    <k-field-text v-model="formData.remindStatus"/>
                </k-form-item>
                <k-form-item label="产品经理id">
                    <k-field-text v-model="formData.productManagerId"/>
                </k-form-item>
                <k-form-item label="产品经理名字">
                    <k-field-text v-model="formData.productManagerName"/>
                </k-form-item>
                <k-form-item label="产品经理邮箱">
                    <k-field-text v-model="formData.productManagerEmail"/>
                </k-form-item>
                <k-form-item label="投资经理id">
                    <k-field-text v-model="formData.investManagerId"/>
                </k-form-item>
                <k-form-item label="投资经理名字">
                    <k-field-text v-model="formData.investManagerName"/>
                </k-form-item>
                <k-form-item label="投资经理邮箱">
                    <k-field-text v-model="formData.investManagerEmail"/>
                </k-form-item>
                <k-form-item label="创建日期">
                    <k-field-text v-model="formData.crtDate"/>
                </k-form-item>
                <k-form-item label="创建时间">
                    <k-field-text v-model="formData.crtTime"/>
                </k-form-item>
                <k-form-item label="创建人id">
                    <k-field-text v-model="formData.crtUserId"/>
                </k-form-item>
                <k-form-item label="创建人名称">
                    <k-field-text v-model="formData.crtUserName"/>
                </k-form-item>
                <k-form-item label="更新日期">
                    <k-field-text v-model="formData.updDate"/>
                </k-form-item>
                <k-form-item label="更新时间">
                    <k-field-text v-model="formData.updTime"/>
                </k-form-item>
                <k-form-item label="更新人id">
                    <k-field-text v-model="formData.updUserId"/>
                </k-form-item>
                <k-form-item label="更新人名称">
                    <k-field-text v-model="formData.updUserName"/>
                </k-form-item>
                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdBonusTask.updateT8ProdBonusTask"
                           data-from="editT8ProdBonusTaskForm"
                           :data-model="formData" data-target="t8ProdBonusTaskGrid">
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

    export default {
        name:"T8ProdBonusTask",
        data() {
            return {
                formData: {},
                selectRowData: {},
                searchParam: {},
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
            renderDateTimeUpdate(row) {
                return Tools.formatDateTime(row.updDate, row.updTime);
            },
        }
    };
</script>
