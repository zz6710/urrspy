<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="RmsTableInfo" data-label-width="80px" v-model="searchParam" data-target="rmsTableInfoGrid">
      <k-form-item label="表名称">
<!--        <k-field-select data-action="RmsTableInfo.findRmsTableInfoDict" v-model="searchParam.tableName"-->
<!--                        data-display-field="tableName,comment" data-value-field="tableName"/>-->
        <k-field-text v-model="searchParam.tableName" />
      </k-form-item>
      <k-form-item label="表所属层级">
        <k-field-select data-dict="table_owner" v-model="searchParam.owner"/>
      </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
      <div class="left">
        <k-btn class="btn-custom-primary" data-functype="PAGE" :data-handler="handleEdit">
          <md-icon md-src="/static/svg/edit.svg" />修改表血缘关系</k-btn>
        <k-btn class="btn-custom-plain" @click="getSqlLineage">
          <i v-show="!loading"><md-icon md-src="/static/svg/confirm.svg"/></i>
          <i v-show="loading" class="el-icon-loading"/>
          手动解析</k-btn>
        <div style="width:120px;">
          执行状态: <strong>{{loading? "运行中" : "已完成"}}</strong>
        </div>
        <k-btn class="btn-custom-icon" @click="getRunStatus">
          <md-icon md-src="/static/svg/reset.svg"></md-icon></k-btn>
      </div>
		</div>
      <k-grid ref="rmsTableInfoGrid" @data-row-select="selectRow" data-action="RmsTableInfo.findRmsTableInfos" data-operate-width="350px">
		<k-grid-column data-header="主键" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="表id" data-name="tableInfoId" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="库名" data-name="databaseName"></k-grid-column>
		<k-grid-column data-header="表名" data-name="tableName"></k-grid-column>
		<k-grid-column data-header="表注释" data-name="comment"></k-grid-column>
		<k-grid-column data-header="表所属层级" data-name="owner" data-dict="table_owner"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-size="mini" @click="showTableLineage(scope.row.row, 0)">
            上游血缘关系
          </k-btn>
          <k-btn class="btn-custom-text" data-size="mini" @click="showTableLineage(scope.row.row, 1)">
            下游血缘关系
          </k-btn>
          <k-btn class="btn-custom-text" data-size="mini" @click="showTableLineage(scope.row.row, 2)">
            全血缘关系
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加表基础信息弹出框   -->
	<k-popup ref="addRmsTableInfoPopup" data-title="新增">
    	<k-form ref="addRmsTableInfoForm" :data-col="2">
			<k-form-item label="表id">
	        	<k-field-text v-model="formData.tableInfoId"/>
	     	</k-form-item>
			<k-form-item label="库名">
	        	<k-field-text v-model="formData.databaseName"/>
	     	</k-form-item>
			<k-form-item label="表名">
	        	<k-field-text v-model="formData.tableName"/>
	     	</k-form-item>
			<k-form-item label="表注释">
	        	<k-field-text v-model="formData.comment"/>
	     	</k-form-item>
			<k-form-item label="表所属层级">
	        	<k-field-text v-model="formData.owner"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="md-primary" data-functype="SUBMIT" data-action="RmsTableInfo.addRmsTableInfo" data-from="addRmsTableInfoForm"
		               :data-model="formData" data-target="rmsTableInfoGrid">
		          <i class="icon-confirm" />确定
		        </k-btn>
		        <k-btn class="md-info" data-functype="CLOSE">
		          <i class="icon-cancel" />取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改表基础信息弹出框   -->
	<k-popup ref="editRmsTableInfoPopup" data-title="修改">
	  <k-form ref="editRmsTableInfoForm" :data-col="2">
		<k-form-item label="表id">
        	<k-field-text v-model="formData.tableInfoId"/>
     	</k-form-item>
		<k-form-item label="库名">
        	<k-field-text v-model="formData.databaseName"/>
     	</k-form-item>
		<k-form-item label="表名">
        	<k-field-text v-model="formData.tableName"/>
     	</k-form-item>
		<k-form-item label="表注释">
        	<k-field-text v-model="formData.comment"/>
     	</k-form-item>
		<k-form-item label="表所属层级">
        	<k-field-text v-model="formData.owner"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="RmsTableInfo.updateRmsTableInfo" data-from="editRmsTableInfoForm"
	        :data-model="formData" data-target="rmsTableInfoGrid">
			  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
			  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
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
        loading: false,
        formData: {},
        selectRowData: {},
	      searchParam: {},
        timer: null
      };
    },

    beforeDestroy() {
      clearInterval(this.timer);
    },

    mounted() {
    },

    deactivated() {
      this.removeTimer();
    },

    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      getSqlLineage() {
        if (this.loading) {
          Tools.alert("正在执行血缘解析，稍后重试！","danger");
          return;
        }
        this.setTimer();
        this.loading = true;
        this.httpUtil.comnUpdate({
          action: 'TableLineage.getSqlLineage',
          params: {},
          successAlert: true,
        }).then(data => {
          if (!data.success) {
            this.loading = false;
            this.removeTimer();
          }
        });
      },
			handleEdit() {
				this.$router.push({
					path: '/main/pms/sqlFlow/tableLineage',
				})
			},

      getRunStatus() {
        this.httpUtil.comnQuery({
          action: 'TableLineage.getRunStatus',
          params: {},
        }).then(data => {
          if (data.success && data.returnmsg === '0') {//已完成
            this.loading = false;
            this.removeTimer();
          } else {
            this.loading = true;
            this.setTimer();
          }
        });
      },

      // 定时查询任务状态
      setTimer() {
        const that = this;
        if (this.timer) {
          return ;
        }
        this.timer = setInterval(() => {
          that.getRunStatus();
        }, 5000);
      },

      removeTimer() {
        clearInterval(this.timer);
      },

      // 展示血缘关系图
      showTableLineage(row, number) {
        let action = null;
        if (number === 0) {
          action = 'TableLineage.findUpStreamTableLineage'
        } else if (number === 1) {
          action = 'TableLineage.findDownStreamTableLineage'
        } else {
          action = 'TableLineage.findTableLineage'
        }
        this.httpUtil.comnUpdate({
          action: action,
          params: row,
          successAlert: true,
        }).then(data => {
          if (data.success) {
            this.$router.push({
              name: "血缘关系图",
              params: {data: data.returndata}
            });
          }
        });
      },
    }
  };
</script>
<style lang="scss" scoped>
.md-button {
  &.btn-custom-icon {
    background: #fff !important;
    border: 1px solid #fff !important;
    color: #417FFFFF !important;
    box-shadow: none;
    /deep/ path {
      fill: #417FFFFF !important;
    }
    /deep/ i {
      color: #417FFFFF !important;
    }
    &:hover {
      background: #fff !important;
      border-color: #fff !important;
      box-shadow: none;
      color: #417FFFFF !important;
      path {
        fill: #417FFFFF !important;
      }
      i {
        color: #417FFFFF !important;
      }
    }
    &:active,
    &:focus {
      border-color: #fff !important;
      box-shadow: none;
      color: #417FFFFF !important;
      path {
        fill: #417FFFFF !important;
      }
      i {
        color: #417FFFFF !important;
      }
    }
  }
}
</style>
