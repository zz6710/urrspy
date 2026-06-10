import moment from "moment";
export default {
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			nowDate: "",
			reportDate: "",
			regionList: [],
			socialCreditCode: "91310000MA7GTQK786",
			formNameList: [
				{
					key: "CLTYCK",
					name: "存量同业存款信息",
				},
				{
					key: "TYCKFS",
					name: "同业存款发生额信息",
				},
				{
					key: "CLZQTZ",
					name: "存量债券投资信息",
				},
				{
					key: "ZQTZFS",
					name: "债券投资发生额信息",
				},
				{
					key: "SPVTZX",
					name: "存量特定目的载体投资信息",
				},
				{
					key: "SPVFSX",
					name: "特定目的载体投资发生额信息",
				},
			],
		};
	},
	created() {
		this.getNowDate();
	},
	computed: {
		lastDay() {
			if (this.reportDate) {
				return moment([this.reportDate.split("-")[0], this.reportDate.split("-")[1] - 1])
					.endOf("month")
					.format("YYYYMMDD");
			}
			return "";
		},
		filterLastDayOfMonth() {
			if (this.searchParam.reportDate) {
				return moment([this.searchParam.reportDate.slice(0,4), Number(this.searchParam.reportDate.slice(4,6)) - 1])
					.endOf("month")
					.format("YYYYMMDD");
			}
			return "";
		}
	},
	methods: {
		submitUploadParam() {
			//文件上传校验
			let validate = this.$refs.addForm.validate();
			if (validate) {
				let formData = { reportDate: this.lastDay };
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					this.$refs.uploadRef.upload(formData);
				} else {
					this.$message.error("上传文件不能为空!");
					return false;
				}
			}
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.addPopup.close();
			if (this.searchParam.reportDate) {
				this.$refs.tableGrid.load(this.searchParam);
			}
		},
		onSubmitError() {
			this.$refs.uploadRef.doReset();
			this.$refs.submitBtn.setIconStyle(1, []);
		},
		uploadOpened() {
			this.reportDate = "";
		},
		validForm() {
      return this.$refs.searchRef.$refs.searchForm.validate()
    },
		creatZipFile(type) {
			if (!this.validForm()) {
				this.$message.error("请选择数据日期");
				return;
			}

			this.$refs.assetSend.setLoading(true);

			this.httpUtil.download({
				url: "/download/server/RptApp/rhzy/download.json",
				params: { ...this.searchParam, tableStr: type },
				callback: () => {
					this.$refs.assetSend.setLoading(false);
				},
			});
		},
		getNowDate() {
			const timeOne = new Date();
			const year = timeOne.getFullYear();
			let month = timeOne.getMonth() + 1;
			let day = timeOne.getDate();
			month = month < 10 ? "0" + month : month;
			day = day < 10 ? "0" + day : day;
			this.nowDate = year + "" + month + "" + day;
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		getRegionList() {
			this.httpUtil
				.comnQuery({
					action: "BondInvestInfo.addclcSourceZonCdDict",
					params: { TEXT: "" },
				})
				.then((data) => {
					this.regionList = data.rows;
				});
		},
		getRegionText(v) {
			return v + " " + ((this.regionList.find((item) => item.VALUE == v) || {}).TEXT || '');
		},
		getFileName(v) {
			return `${this.socialCreditCode}_${v}_${this.filterLastDayOfMonth}`;
		},
	},
};
