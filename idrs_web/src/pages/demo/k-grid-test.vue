<template>
  <div>
    <k-grid
      class="k-grid"
      data-checkbox-id="userid"
      data-align="rigth"
      data-db-click="handleRowDbClick"
      data-before-load="handleBeforeLoad"
      data-after-load="handleAfterLoad"
      data-graphql='{queryTest(action:"findTests") {rows{testid,testname,parentid},results}}'
	  data-tree-id="testid"
	  data-operate-width="200"
	  data-diffcondition="testid,parentid"
      @data-row-select="handleRowSelect"
      @init="(grid)=>{this.$kgrid = grid}"
    >
      <k-grid-column
        data-header="标志"
        data-name="id"
      ></k-grid-column>
      <k-grid-column
        data-header="机构名"
        data-name="name"
      ></k-grid-column>
      <k-grid-column
        data-header="价格"
        data-name="money"
        data-type="money"
      ></k-grid-column>
      <k-grid-column
        data-header="name"
        data-name="username"
      ></k-grid-column>
      <template slot="operate">
        <el-button @click="handleClickRowBtn($event)">show</el-button>
      </template>
    </k-grid>
    <el-button @click="handleClick()">设置</el-button>
    <el-button @click="handleClearAll">清空</el-button>
    <el-button @click="handleSelectAll">全选</el-button>
    <el-button @click="handleGetSelected">获取</el-button>
  </div>
</template>

<script>
import kayak from "@/frame/kayak.js";

//  data-action="User.findUsers"
//  data-graphql='{queryUser(action:"findUsers") {rows{  userid, username},results}}'
export default {
	data() {
		return {
			$kgrid: null
		};
	},
	computed: {
		dataParams() {
			/* 			return {
				k1: "v1"
			}; */
		}
	},
	methods: {
		handleRowSelect(row, column, event) {
			// console.log("row: %o, column: %o,  event: %o", row, column, event);
		},
		handleBeforeLoad() {
			/* 			return {
				k2: "v2"
			}; */
			return {};
		},
		handleAfterLoad(data) {
			console.log(data);
		},
		handleRender(row) {
			return row.author;
		},
		handleClickRowBtn(ev) {
			console.log(this.$kgrid.getRowData(ev));
			console.log(this.$kgrid.getColumnOptions())
		},
		handleClick() {
			// reload
			// this.$kgrid.load({
			// 	page: 2
			// });

			// getSelected
			// console.log(this.$kgrid.getSelected())

			// setSelected
			this.$kgrid.setSelected(["10258"]);
		},
		handleClick2() {
			this.$kgrid.setSelected([2]);
		},
		handleClearAll() {
			this.$kgrid.clearAll();
		},
		handleSelectAll() {
			this.$kgrid.selectAll();
		},
		handleGetSelected() {
			console.log(this.$kgrid.getSelected());
		},
		handleEdit(row) {
			console.log("eidt: ", row);
		},
		handleAdd(row) {
			console.log("eidt: ", row);
		},
		handleHideEdit(row) {
			if (row.id === 2) {
				return false;
			}
			return false;
		},
		handleDisable(row) {
			if (row.id === 1) {
				return true;
			}
			return false;
		}
	},
	mounted() {}
};
</script>
<style>
</style>
