import moment from "moment";
export default {
  computed: {
    lastDayReportDate() {
      if (this.reportDate) {
        return moment([this.reportDate.split("-")[0], this.reportDate.split("-")[1] - 1])
          .endOf("month")
          .format("YYYYMMDD");
      }
      return "";
    },
    lastDayBeginDate() {
      if (this.beginDate) {
        return moment([this.beginDate.split("-")[0], this.beginDate.split("-")[1] - 1])
          .endOf("month")
          .format("YYYYMMDD");
      }
      return "";
    },
    filterLastDayOfMonth() {
			if (this.prodSearchParam.reportDate) {
				return moment([this.prodSearchParam.reportDate.slice(0,4), Number(this.prodSearchParam.reportDate.slice(4,6)) - 1])
					.endOf("month")
					.format("YYYYMMDD");
			}
			return "";
		}
  },
  methods: {
    creatZipFile(type) {
      if (!this.validForm()) {
          return;
      }
      this.$refs[`assets${type}Send`].setLoading(true)
      let params = {...this.prodSearchParam,  tableStr: type}
      if (['ZG02', 'ZG03'].includes(type)) {
        Object.assign(params, {beginDate: this.queryParam.beginDate, queryDate: this.queryParam.queryDate})
      }else if (['ZG01'].includes(type)){
        Object.assign(params, {beginDate: this.queryParam.reportBeginDate, queryDate: this.queryParam.reportEndDate})
      }
      this.httpUtil.download({
          url: '/download/server/RptApp/rhzg/download.json',
          params: { ...params },
          callback: () => {
            this.$refs[`assets${type}Send`].setLoading(false)
          }
      })
    },
    validForm() {
      return this.$refs.searchRef.$refs.searchForm.validate()
    },
    exportName(type) {
      let date = ''
      if (type == 'ZG01') {
        date = this.queryParam.reportEndDate
      } else if (['ZG02', 'ZG03'].includes(type)) {
        date = this.queryParam.queryDate
      } else {
        date = this.filterLastDayOfMonth
      }
      return `Z7006931000016_${type}_${date}`
    }
  }
}
