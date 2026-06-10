import Tools from "@/utils/tools";
export default {
	methods: {
		handleItemDiff(field) {
			const newVal = this.formData[field];
			const oldVal = this.formDataCopy[field];
			let flag = false;
			if (newVal != null && oldVal != null && !isNaN(newVal) && !isNaN(oldVal)) {
				if (Number(newVal) != Number(oldVal)) {
					flag = true;
				}
			} else {
				if (newVal != oldVal) {
					flag = true;
				}
				if ((newVal == '' && oldVal == null) || (newVal == null && oldVal == '')) {
					flag = false;
				}
			}
			return flag ? "form-item-diff" : "";
		},
		formNoChangeCb(params = { tip: true }) {
			if (this.formNoChange) {
				if (params.tip) {
					Tools.alert("无数据修改！", "warning");
				}
				return true;
			}
			return false;
		},
	},
	computed: {
		formNoChange() {
			const index = Object.keys(this.formData).findIndex((item) => {
				const newVal = this.formData[item];
				const oldVal = this.formDataCopy[item];
				let flag = false;
				if (newVal != null && oldVal != null && !isNaN(newVal) && !isNaN(oldVal)) {
					if (Number(newVal) != Number(oldVal)) {
						flag = true;
					}
				} else {
					if (newVal != oldVal) {
						flag = true;
					}
					if ((newVal == '' && oldVal == null) || (newVal == null && oldVal == '')) {
						flag = false;
					}
				}
				return flag;
			});
			if (index > -1) {
				return false;
			}
			return true;
		},
	},
};
