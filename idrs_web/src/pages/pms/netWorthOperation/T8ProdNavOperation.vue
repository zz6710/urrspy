<template>
    <div>
        <div>
            <k-form-search-customize data-model-name="T8ProdNavOperation" data-target="t8ProdNavOperationGrid"
                           v-model="queryParam">
                <k-form-item label="公告标题">
                    <k-field-select v-model="prodSearchParam.prodType" data-dict="t8_nav_operation_type"
                                    data-display-field="itemval" data-value-field="itemkey"/>
                </k-form-item>
            </k-form-search-customize>
        </div>
        <div>
            <k-grid ref="t8ProdNavOperationGrid" @data-row-select="selectRow"
                    data-action="T8ProdNavOperation.findT8ProdNavOperations">
                <k-grid-column data-align="center" data-header="主键id" data-name="id" data-hidden="true"></k-grid-column>
                <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"></k-grid-column>
                <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"></k-grid-column>
                <k-grid-column data-align="center" data-header="公告标题" data-name="prodType"
                               data-dict="t8_nav_operation_type"></k-grid-column>
<!--                <k-grid-column data-header="产品成立日期" data-name="prodCrtDate"></k-grid-column>-->
                <k-grid-column data-align="center" data-header="基准日期" data-name="discCutDate"></k-grid-column>
                <k-grid-column data-align="center" data-header="信披日期" data-name="releaseDate"></k-grid-column>

                <template slot="operate" slot-scope="props">
                    <k-btn class="md-info md-just-icon md-simple" :data-model="props.row.row"
                           @click="toAddPage(props.row.row)" data-descript="详情" data-functype="POPUP" data-size="mini"
                           data-target="detail8ProdNavOperationPopup">
                        <md-icon>library_books</md-icon>
                    </k-btn>

                    <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品净值运作信息" data-functype="POPUP"
                           data-size="mini"
                           data-target="editT8ProdNavOperationPopup">
                        <md-icon>edit</md-icon>
                    </k-btn>
                    <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                           data-action="T8ProdNavOperation.deleteT8ProdNavOperation" data-size="mini"
                           data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true"
                           data-descript="删除产品净值运作信息">
                        <md-icon>close</md-icon>
                    </k-btn>
                </template>
            </k-grid>
        </div>

        <!--    添加产品净值运作信息弹出框   -->
        <!--	<k-popup ref="addT8ProdNavOperationPopup" data-title="新增">-->
        <!--    	<k-form ref="addT8ProdNavOperationForm" :data-col="2">-->
        <!--        <k-form-item label="主键id>-->
        <!--	        	<k-field-text v-model="formData.id"/>-->
        <!--	     	</k-form-item>-->
        <!--			<k-form-item label="产品代码">-->
        <!--	        	<k-field-text v-model="formData.prodCode"/>-->
        <!--	     	</k-form-item>-->
        <!--			<k-form-item label="">-->
        <!--	        	<k-field-text v-model="formData.prodName"/>-->
        <!--	     	</k-form-item>-->

        <!--	      	<k-form-footer data-align="center">-->
        <!--		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdNavOperation.addT8ProdNavOperation" data-from="addT8ProdNavOperationForm"-->
        <!--		               :data-model="formData" data-target="t8ProdNavOperationGrid">-->
        <!--		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定-->
        <!--		        </k-btn>-->
        <!--		        <k-btn class="btn-custom-plain" data-functype="CLOSE">-->
        <!--		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>-->
        <!--	      	</k-form-footer>-->
        <!--    	</k-form>-->
        <!--	</k-popup>-->

        <!--    修改产品净值运作信息弹出框   -->
        <k-popup ref="editT8ProdNavOperationPopup" data-title="修改">
            <k-form ref="editT8ProdNavOperationForm" :data-col="2">
                <k-form-item label="主键id" v-if="false">
                    <k-field-text v-model="formData.id"/>
                </k-form-item>
                <k-form-item label="产品代码">
                    <k-field-text v-model="formData.prodCode"/>
                </k-form-item>
                <k-form-item label="产品名称">
                    <k-field-text v-model="formData.prodName"/>
                </k-form-item>
                <k-form-item label="产品类型">
                    <k-field-select v-model="formData.prodType" data-dict="t8_nav_operation_type"
                                    :data-allowblank="false"/>
                </k-form-item>
<!--                <k-form-item label="产品成立日期">-->
<!--                    <k-field-date v-model="formData.prodCrtDate"-->
<!--                                  :data-max-value="this.formData.discCutDate == null || this.formData.discCutDate == '' ? '29991230' : this.formData.discCutDate"-->
<!--                                  :data-min-value="'19700101'"/>-->
<!--                </k-form-item>-->
                <k-form-item label="基准日期">
                    <k-field-date v-model="formData.discCutDate" :data-max-value="'29991230'"
                                  :data-min-value="'19700101'" :data-allowblank="false"/>
                </k-form-item>
                <k-form-item label="信披日期">
                    <k-field-date v-model="formData.releaseDate" :data-max-value="'29991230'"
                                  :data-min-value="'19700101'" :data-allowblank="false"/>
                </k-form-item>
                <k-form-footer data-align="center">
                    <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                           data-action="T8ProdNavOperation.updateT8ProdNavOperation"
                           data-from="editT8ProdNavOperationForm"
                           :data-model="formData" data-target="t8ProdNavOperationGrid">
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
        data() {
            return {
                prodSearchParam: {
                    prodType: ''
                },
                formData: {},
                selectRowData: {}
            };
        },
        methods: {
            toAddPage(row) {
                this.$router.push({
                    path: '/main/pms/netWorthOperation/T8ProdNavOperDetail',
                    query: {
                        prodCode: row.prodCode,
                        prodType: row.prodType,
                        discCutDate: row.discCutDate,
                        releaseDate: row.releaseDate
                    }
                })
            },
            download() {
                this.httpUtil.comnQuery({
                    action: "T8ProdNavOperation.findDisclosureVersion",
                    params: {noticeTitle: '1'}
                }).then(data => {
                    if (data.rows.length > 0) {
                        this.httpUtil.download({
                            url: "/download/server/PmsApp/netWorthOperation/downloadNetWorthOperation.json",
                            params: {noticeTitle: '1'},
                            callback: response => {
                                this.$refs.navOperationButen.setIconStyle(1, []);
                                Tools.alert("下载完成");
                            }
                        }, '同利产品净值运作公告（每周三公布全量同利产品净值，遇节假日顺延）.docx');
                    } else {
                        this.$refs.navOperationButen.setIconStyle(1, []);
                        Tools.alert("请设置对应的信披规则")

                    }
                }).catch({})
            },
            download1() {
                this.httpUtil.comnQuery({
                    action: "T8ProdNavOperation.findDisclosureVersion",
                    params: {noticeTitle: '2'}
                }).then(data => {
                    if (data.rows.length > 0) {
                        this.httpUtil.download({
                            url: "/download/server/PmsApp/netWorthOperation/generateMaturityAnnouncement.json",
                            params: {noticeTitle: '2'},
                            callback: response => {
                                this.$refs.navOperationButen1.setIconStyle(1, []);
                                Tools.alert("下载完成");
                            }
                        }, '同利系列产品到期收益一览表.docx');
                    } else {
                        this.$refs.navOperationButen1.setIconStyle(1, []);
                        Tools.alert("请设置对应的信披规则")
                    }
                }).catch({})
            },
            download2(value) {
                this.httpUtil.comnQuery({
                    action: "T8ProdNavOperation.isexistProdCode",
                    params: {prodCode: '4469894'}
                }).then(data => {
                    if (data.rows.length > 0) {
                        this.httpUtil.comnQuery({
                            action: "T8ProdNavOperation.findDisclosureVersion",
                            params: {noticeTitle: '3'}
                        }).then(data => {
                            if (data.rows.length > 0) {
                                this.httpUtil.download({
                                    url: "/download/server/PmsApp/netWorthOperation/generateFinancialAnnouncement.json",
                                    params: {noticeTitle: '3', prodCode: value},
                                    callback: response => {
                                        this.$refs.navOperationButen2.setIconStyle(1, []);
                                        Tools.alert("下载完成");
                                    }
                                }, value + '业绩表现.docx');
                            } else {
                                this.$refs.navOperationButen2.setIconStyle(1, []);
                                Tools.alert("请设置对应的信披规则")
                            }
                        }).catch({})
                    } else {
                        this.$refs.navOperationButen2.setIconStyle(1, []);
                        Tools.alert("净值表无该代码,请重新选择")
                    }
                }).catch({})
            },
            selectRow(row, column, event) {
                this.selectRowData = Object.assign({}, row)
                this.formData = Object.assign({}, row)
            },
            generateNowNav() {
                this.httpUtil.comnQuery({
                    action: "T8ProdNavOperation.generateNowNav",
                    params: {prodSeries: '11'}
                })
            }
        },
        computed: {
            queryParam() {
                return {
                    'prodType': this.prodSearchParam.prodType,
                }
            }
        }
    };
</script>
