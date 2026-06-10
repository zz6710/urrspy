import moment from "moment";
export default {
  data() {
    return {
      formData: {},
      formDataCopy: {},
      selectRowData: {},
      searchParam: {}
    };
  },
  computed: {
		lastDay() {
			if (this.formData.actDt) {
				return moment([this.formData.actDt.split("-")[0], this.formData.actDt.split("-")[1] - 1])
					.endOf("month")
					.format("YYYYMMDD");
			}
			return "";
		},
    lastDayDeal() {
			if (this.formData.dealDate) {
				return moment([this.formData.dealDate.split("-")[0], this.formData.dealDate.split("-")[1] - 1])
					.endOf("month")
					.format("YYYYMMDD");
			}
			return "";
		},
		lastDayTTRDDeal() {
    			if (this.formData.reportDate) {
    				return moment([this.formData.reportDate.split("-")[0], this.formData.reportDate.split("-")[1] - 1])
    					.endOf("month")
    					.format("YYYYMMDD");
    			}
    			return "";
    		},
		formDataTransfer() {
			return {
				...this.formData,
				actDt: this.lastDay,
			};
		},
	},
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
      this.formDataCopy = Object.assign({}, row)
    },

    onSubmitSuccess() {
        if (this.searchParam.actDt) {
          this.$refs.tableGrid.load(this.searchParam);
        }
        this.$refs.uploadBtnRef.setIconStyle(1)
      },

      onSubmitError() {
        this.$refs.uploadBtnRef.setIconStyle(1)
      },

      submitUploadParam(){
        //文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            let formData = { dealDate: this.lastDayDeal };
            this.$refs.uploadRef.upload(formData);
            this.$refs.uploadBtnRef.setIconStyle(0)
            setTimeout(()=>{
              this.$refs.uploadPopup.close();
            }, 300)
          } else {
            this.$message.error("上传文件不能为空!");
            return false;
          }
        }
        return false
      },
  }
};
