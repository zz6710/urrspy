<template>
  <div>
    <div>
        <k-form-customize>
          <k-btn class="btn-custom-primary" style="width: 130px"  data-functype="SUBMIT" ref="navOperationButen"
                 @click="download()" v-if="global.isShowAuthorityButton('T8ProdNavOperation.netWorthOperation') && this.$route.query.prodType == '1'">
            <md-icon md-src="/static/svg/add.svg" />生成同利运作公告</k-btn>
          <k-btn class="btn-custom-primary" style="width: 130px"  data-functype="SUBMIT" ref="navOperationButen2"
                 @click="download2()" v-if="global.isShowAuthorityButton('T8ProdNavOperation.financialAnnouncement') && this.$route.query.prodType == '3'">
            <md-icon md-src="/static/svg/add.svg" />生成亲子理财公告</k-btn>
        </k-form-customize>

    </div>
    <div>
      <k-grid ref="t8ProdNavOperDetail" @data-row-select="selectRow" :data-autoload="false"
              data-action="T8ProdNavOperation.findT8ProdNavOperationsDetail">
        <k-grid-column data-header="产品代码" data-name="prodCode" v-if="prodType == '1'"/>
        <k-grid-column data-header="产品名称" data-name="prodName" data-width="300" v-if="prodType == '1'"/>
        <k-grid-column data-header="日期" data-name="vldate" data-width="300" v-if="prodType == '3'"/>
        <k-grid-column data-header="当日每万份基金收益" data-name="tenthsincome" data-width="300" v-if="prodType == '3'"/>
        <k-grid-column data-header="单位净值" data-name="nav" data-width="300" v-if="prodType == '1'"/>
      </k-grid>
    </div>
  </div>
</template>

<script>
    import T8ProdNavOperation from "./T8ProdNavOperation.vue";
    import Tools from "@/utils/tools";
    export default {
        components: {T8ProdNavOperation},
        created() {
            this.prodCode = this.$route.query.prodCode;
            this.discCutDate = this.$route.query.discCutDate;
            this.prodType = this.$route.query.prodType;
            this.releaseDate = this.$route.query.releaseDate;
        },
        activated() {
            this.prodCode = this.$route.query.prodCode;
            this.discCutDate = this.$route.query.discCutDate;
            this.prodType = this.$route.query.prodType;
            this.releaseDate = this.$route.query.releaseDate;
            this.$nextTick(()=>{
                this.$refs.t8ProdNavOperDetail.load({"prodCode":this.prodCode,
                    "prodType":this.prodType,
                    "discCutDate":this.discCutDate,
                    "releaseDate":this.releaseDate});
            });
        },
        data() {
            return {
                formData: {},
                selectRowData: {},
                prodType: '',
                discCutDate: '',
                releaseDate: '',
                prodCode: '',
            };
        },
        methods: {
            selectRow(row, column, event) {
                this.selectRowData = Object.assign({}, row)
                this.formData = Object.assign({}, row)
            },
            download(){
              console.log(this.releaseDate);
                this.httpUtil.comnQuery({
                    action: "T8ProdNavOperation.findDisclosureVersion",
                    params: {noticeTitle:'1'}
                }).then(data => {
                    if(data.rows.length>0){
                        this.httpUtil.download({
                            url: "/download/server/PmsApp/netWorthOperation/downloadNetWorthOperation.json",
                            params: {noticeTitle:'1',releaseDate:this.releaseDate},
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
            download1(){
                this.httpUtil.comnQuery({
                    action: "T8ProdNavOperation.findDisclosureVersion",
                    params: {noticeTitle:'2'}
                }).then(data => {
                    if(data.rows.length>0){
                        this.httpUtil.download({
                            url: "/download/server/PmsApp/netWorthOperation/generateMaturityAnnouncement.json",
                            params: {noticeTitle:'2'},
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
            download2(){
                this.httpUtil.comnQuery({
                    action: "T8ProdNavOperation.isexistProdCode",
                    params: {prodCode:this.prodCode}
                }).then(data => {
                    if(data.rows.length>0){
                        this.httpUtil.comnQuery({
                            action: "T8ProdNavOperation.findDisclosureVersion",
                            params: {noticeTitle:'3'}
                        }).then(data => {
                            if(data.rows.length>0){
                                this.httpUtil.download({
                                    url: "/download/server/PmsApp/netWorthOperation/generateFinancialAnnouncement.json",
                                    params: {noticeTitle:'3',prodCode : this.prodCode,releaseDate:this.releaseDate},
                                    callback: response => {
                                        this.$refs.navOperationButen2.setIconStyle(1, []);
                                        Tools.alert("下载完成");
                                    }
                                }, this.prodCode+'业绩表现.docx');
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
        }
    }
</script>
<style scoped>
  .md-primary {
    /*text-align:center;*/
    margin:auto;
  }

</style>
