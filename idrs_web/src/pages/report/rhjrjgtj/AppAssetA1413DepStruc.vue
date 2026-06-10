<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchRef"
				data-model-name="AppAssetA1413DepStruc"
				data-label-width="80px"
				v-model="searchParam"
				data-target="appAssetA1413DepStrucGrid"
				:handleConfirm="handleConfirm"
			>
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-btn slot="button" ref="downloadRef" class="btn-custom-plain" :data-handler="creatZipFile">
					<md-icon>cloud_download</md-icon>
					下载报送文件
				</k-btn>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<iframe :src="iframeUrl" frameborder="0"></iframe>
		</div>
	</div>
</template>

<script>
import moment from "moment";

export default {
	name: "AppAssetA1413DepStruc",
	data() {
		return {
			searchParam: {
				reportDate: "",
			},
			iframeUrl: "",
			key: 0,
		};
	},
	created() {
		this.searchParam.reportDate = moment().subtract(1, "months").format("yyyy-MM");
		this.getSrc();
	},
	mounted() {},
	methods: {
		getSrc() {
			const { baseUrl } = getURL();
			this.key += 1;
			this.iframeUrl = baseUrl + "jmreport/view/M07ZY006?reportDate=" + this.searchParam.reportDate + "&key=" + this.key;
		},
		handleConfirm() {
			const v = this.$refs.searchRef.$refs.searchForm.validate();
			if (v) {
				this.getSrc();
			}
			return false;
		},
		creatZipFile() {
			if(this.searchParam.reportDate == '' || this.searchParam.reportDate == null) {
				return this.$message.error("请选择需要下载报送文件的数据日期！");
			}

			this.$refs.downloadRef.setIconStyle(0);
			this.$refs.downloadRef.setLoading(true);
			this.httpUtil.download({
				url: "/download/server/RptApp/JmReport/importTemplate/downloadA1413.json",
				params: {
					reportDate: moment([this.searchParam.reportDate.split("-")[0], this.searchParam.reportDate.split("-")[1] - 1]).endOf("month").format("YYYYMMDD")
				},
				callback: () => {
					this.$refs.downloadRef.setIconStyle(1);
					this.$refs.downloadRef.setLoading(false);
				},
			});
		},
	},
};
</script>
<style lang="scss" scoped>
/deep/ .k-grid {
	width: 40%;
}
</style>
