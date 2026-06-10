<template>
  <div class="py-page">
		<div>
			<k-form-search-customize data-model-name="DataReportManage" v-model="searchParam" data-target="tableGrid">
				<k-form-item label="登记日期">
					<k-field-date v-model="searchParam.workdate"/>
				</k-form-item>
				<k-form-item label="报文类型">
					<k-field-select v-model="searchParam.filetype" data-dict="zz_msg_typ"/>
				</k-form-item>
				<k-form-item label="发送文件名称">
					<k-field-text v-model="searchParam.origfilename"/>
				</k-form-item>
			</k-form-search-customize>
		</div>
    <div class="py-page-container">
			<k-grid ref="tableGrid" @data-row-select="selectRow" data-action="DataReportManage.findDataReportManages" data-operate-column="true" data-operate-width="300px">
				<k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
				<k-grid-column data-header="登记日期" data-name="workdate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
				<k-grid-column data-header="报文类型" data-name="filetype" data-dict="zz_msg_typ"></k-grid-column>
				<k-grid-column data-header="报文" data-name="msgtype" data-hidden="true" ></k-grid-column>
				<k-grid-column data-header="发送文件名称" data-name="origfilename" ></k-grid-column>
				<k-grid-column data-header="状态" data-name="status" data-dict="report_file_status"></k-grid-column>
				<k-grid-column data-header="反馈文件流水" data-name="fileid"  data-validate-type="number" data-type="number" ></k-grid-column>
				<k-grid-column data-header="反馈文件名称" data-name="filename"></k-grid-column>
				<k-grid-column data-header="总数量" data-name="totalcount"  data-validate-type="number" data-type="number"></k-grid-column>
				<k-grid-column data-header="成功数量" data-name="successcount" data-validate-type="number" data-type="number"></k-grid-column>
				<k-grid-column data-header="失败数量" data-name="failedcount" data-validate-type="number" data-type="number" ></k-grid-column>
				<k-grid-column data-header="返回码" data-name="errorcode"  data-validate-type="number" data-type="number" ></k-grid-column>
				<k-grid-column data-header="返回信息" data-name="errortext"></k-grid-column>
        <k-grid-column data-header="入库时间" data-name="crtTime"></k-grid-column>]
      <template slot="operate" slot-scope="scope">
      					<k-btn
      						class="btn-custom-text"
      						data-descript="下载发送文件"
      						@click="send_download(scope.row.row)"
      						data-size="mini"
      					>
      						下载发送文件
      					</k-btn>
      					<k-btn
      						class="btn-custom-text"
      						data-descript="下载反馈文件"
      						@click="receive_download(scope.row.row)"
      						data-size="mini"
      					>
      						下载反馈文件
      					</k-btn>
      				</template>
			</k-grid>
		</div>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
export default {
	name: "DataReportManage",
	data() {
		return {
			formData: {},
			selectRowData:{},
			searchParam: {},
		};
	},
	methods: {
	send_download(row) {
  			let origfilename = row.origfilename;
  			let workdate = row.workdate;
  			if (origfilename === null || origfilename === "") {
  				Tools.alert("请选择一条记录！", "danger");
  				return;
  			}
  			this.loading = true;
  			this.httpUtil.download({
  				url: "download/server/DpsApp/dataReportManage/send_download.json",
  				params: {
  					origfilename: origfilename,
  					workdate:workdate,
  				},
  				callback: () => {
  					this.loading = false;
  				}
  			}, row.origfilename);
  		},
  	receive_download(row) {
      			let origfilename = row.filename;
      			let workdate = row.workdate;
      			if (origfilename === null || origfilename === "") {
      				Tools.alert("请选择一条记录！", "danger");
      				return;
      			}
      			this.loading = true;
      			this.httpUtil.download({
      				url: "download/server/DpsApp/dataReportManage/receive_download.json",
      				params: {
      					origfilename: origfilename,
      					workdate:workdate,
      				},
      				callback: () => {
      					this.loading = false;
      				}
      			}, row.filename);
      		},
		selectRow(row, column, event) {
			this.selectRowData = assign({}, row);
      this.formData = assign({}, row);
		},
	},
};
</script>

