<template>
	<div class="right" v-show="vShow">
    &nbsp;&nbsp;生成开始时间：<span class="detail">{{ startTime }}</span>
    &nbsp;&nbsp;生成结束时间：<span class="detail">{{ endTime }}</span>
    &nbsp;&nbsp;生成数据日期：<span class="detail">{{ reportDate }}</span>
    &nbsp;&nbsp;生成结果：<a href="javascript:void(0)" class="detail1" @click="handleClick">{{ resultStatus }}</a>
    <k-btn class="btn-custom-icon" @click="queryRelust"> <md-icon md-src="/static/svg/reset.svg"></md-icon></k-btn>
	</div>
</template>

<style lang="scss" scoped>
.right {
	font-size: 12px;
  font-weight: bold;
	.detail {
		margin: 0 2px;
		font-size: 12px;
		font-weight: normal;
	}
  .detail1 {
		margin: 0 2px;
		font-size: 12px;
		font-weight: normal;
    color: #417fffff;
	}
  .btn-custom-icon {
    background: #fff !important;
    border: 1px solid #fff !important;
    color: #417fffff !important;
    box-shadow: none;
    /deep/ path {
      fill: #417fffff !important;
    }
  }
}
</style>

<script>
  import Tools from "@/utils/tools";
  export default {
    props: {
      formData: {
        type: Object,
        default: () => {
          return {};
        },
		  },
      menuId: String,
      buttonName: String
	  },
    data() {
      return {
        vShow: false,
        startTime: "",
        endTime: "",
        reportDate: "",
        resultStatus: "",
        resultInfo: ""
      };
    },
    methods: {
      handleReports(reportDate) {
        if (this.$parent.$refs.handleTaskAppForm.validate()) {
          this.startTime = Tools.getCurrentTime("/", ":");
          this.endTime = "";
          this.reportDate = reportDate;
          this.resultStatus = "正在"+this.buttonName+"中";
          this.resultInfo = "";

          this.$parent.$refs.reloadBtnRef.setIconStyle(0);
				  this.httpUtil
					.comnUpdate({
						action: "DwsProdTTRDBef.updateTaskAppQuery",
						async: true,
						params: {
              menuId: this.menuId,
              buttonName: this.buttonName,
              reportDate: reportDate
            },
            successAlert: false
					})
					.then((data) => {
            this.queryRelust();
						this.$parent.$refs.reloadBtnRef.setIconStyle(1);
					})
					.catch((err) => {
						console.log(err, "err");
            this.queryRelust();
						this.$parent.$refs.reloadBtnRef.setIconStyle(1);
					});
          setTimeout(() => {
					  this.$parent.$refs.handleTaskPopup.close();
				  }, 300);
			  }
		  },
      queryRelust() {
        this.startTime = "";
        this.endTime = "";
        this.reportDate = "";
        this.resultStatus = "";
        this.resultInfo = "";

        this.httpUtil.comnQuery({
          action: "BaseReportReloadLog.findBaseReportReloadLogs",
          params: {
            menuId: this.menuId
          }
        }).then(data => {
          var rows = data.rows;
          if(rows.length>0) {
            this.vShow = true;
            this.startTime = rows[0].startTime;
            this.endTime = rows[0].endTime;
            this.reportDate = rows[0].reportDate;
            this.resultStatus = rows[0].resultStatus;
            this.resultInfo = rows[0].resultInfo;
          } else {
            this.vShow = false;
          }
        }).catch({});
      },
      handleClick() {
        if (this.resultInfo) {
          if (this.resultStatus.indexOf("成功") > 0) {
            Tools.alertTime(this.resultStatus + "，具体报表如下：<br>" + this.resultInfo, "success", 5000);
          } else if (this.resultStatus.indexOf("失败") > 0) {
            Tools.alertTime(this.resultStatus + "，具体原因如下：<br>" + this.resultInfo, "danger", 5000);
          }
        } else {
          if (this.resultStatus.indexOf("中") > 0) {
            Tools.alertTime(this.resultStatus + "，请稍后", "warning", 5000);
          }
        }
      },
    },
    created() {
      this.queryRelust();
	  },
  };
</script>
