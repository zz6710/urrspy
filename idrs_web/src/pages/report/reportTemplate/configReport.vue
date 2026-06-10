<template>
	<div class="py-page">
		<div class="py-page-container">	
			<div class="table-top-btns">
				<div class="left">
					<k-btn class="btn-custom-plain" data-functype="POPUP" data-target="addDialog" data-descript="配置积木报表ID">
						配置积木报表ID
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="POPUP" :data-handler="addCheckApi" data-descript="新增查询API">
						新增查询API
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="tableGrid"
				:data-operate-column="true"
        data-action="JmSqlDictInfo.findJmSqlDictInfo"
				data-operate-width="280px"
        :data-params="dataParams"
				@data-row-select="selectRow"
			>
				<k-grid-column data-header="报表菜单ID" data-name="id"></k-grid-column>
				<k-grid-column data-header="报表名称" data-name="menuname"></k-grid-column>
        <k-grid-column data-header="积木报表ID" data-name="jimuReportId"></k-grid-column>
				<k-grid-column data-header="更新时间" data-name="upttime"></k-grid-column>
				<k-grid-column data-header="SQL语句" data-name="reportSql"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-functype="POPUP"
						data-size="mini"
						data-target="editDialog"
					>
						编辑
					</k-btn>
					<k-btn
						class="btn-custom-text"
						:data-handler="editReport"
						data-size="mini"
						data-type="danger"
					>
						编辑报表
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="AppraiseRgInfoErr.deleteAppraiseRgInfoErr"
						data-size="mini"
						data-type="danger"
						data-target="M8605P005"
						:data-confirm="true"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>
		<!--配置查询SQL-->
		<k-popup data-title="新增" ref="addDialog">
			<AddDialog :dataData="formData" data-type="insert"/>
		</k-popup>
		<!--编辑-->
		<k-popup data-title="修改" ref="editDialog">
			<AddDialog :dataData="formData" data-type="update" />
		</k-popup>
	</div>
</template>
<script>
import AddDialog from "@/pages/report/reportTemplate/components/configReport/addDialog.vue";
export default {
	components: {
		AddDialog,
	},
	data() {
		return {
			tableData: {
				rows: [
					{
						menuid: "001",
						menuname: "ee",
					},
				],
				total: {},
			},
			selectRowData: {},
			formData: {},
      dataParams:{
        menuid:'',
        jimuReportId:''
      }
		};
	},
  created() {
    this.dataParams.menuid = this.$route.query.menuid
    this.dataParams.jimuReportId = this.$route.query.jimuReportId
  },
  watch:{
	  '$route.query.menuid': {
	    handler(v) {
        if (v) {
          this.dataParams.menuid = v
          this.$nextTick(()=>{
            this.$refs.tableGrid.load(this.dataParams)
          })
        }
      }
    }
  },
  methods: {
		addCheckApi() {
			this.$router.push({
				path: "/main/report/reportTemplate/queryItemConfig",
			});
		},
		editReport(row) {
			this.$router.push({
				path: "/main/report/reportTemplate/editReport",
				query: {
					src: encodeURIComponent(row.jmreportSrc)
				}
			});
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
	},
};
</script>
<style lang="scss" scoped>
.wrapper {
	position: relative;
}
#input {
	position: absolute;
	top: 0;
	left: 0;
	opacity: 0;
	z-index: -10;
}
</style>
