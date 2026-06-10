export default {
	data() {
		return {
			exportType: 1,
		};
	},
	methods: {
		handleConfirmExport(params, type) {
		this.$confirm('确认并导出吗？', '操作提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(()=>{
             	const formParams = {}
             			let p = {}
             			if (type == "custom") {
             				p = params
             			} else {
             				p = this.queryParam;
             			}
             			Object.keys(p).forEach(item=>{
             				if (p[item]) {
             					formParams[item] = p[item]
             				}
             			})
             			this.exportType = 2;
             			this.httpUtil
             				.comnQuery({
             					action: this.abnormalAction,
             					params: formParams,
             					successAlert: false,
             				})
             				.then((data) => {
             					if (data.success) {
             						this.setConfirmExportParam();
             						this.$nextTick(() => {
             							this.$refs.exportRef.handleExport(this.comfirmExportParam);
             							this.comfirmExportParam = {};
             						});
             					}
             				});
             			return false;
            })
		},
		downSuccess() {
			if (this.exportType == "2") {
				this.updateStatus();
			}
		},
		handleBefore() {
			return this.$refs.searchFormRef.$refs.searchForm.validate();
		},
		updateStatus() {
			this.httpUtil
				.comnUpdate({
					action: this.updateStatusAction,
					params: this.queryParam,
				})
				.then((data) => {
					if (data.success) {
					}
				});
		},
		handleExport() {
			this.exportType = 1;
			return true;
		},
	},
};
