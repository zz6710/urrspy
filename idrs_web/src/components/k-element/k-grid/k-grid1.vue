<template>
	<div class="grid-custom">
		<md-card v-if="dataCard || dataCard === 'true'" class="k-card" :class="{ stickyStyle: dataSticky }">
			<md-card-header class="md-card-header-text md-card-header-green" v-if="showSubscript">
				<!-- <div class="card-icon" :style="iconStyle">
          <md-icon>assignment</md-icon>
        </div> -->
				<slot name="search"></slot>
				<el-popover style="float: right" placement="bottom" width="180" trigger="click" v-if="dataEntity">
					<ul class="dropdown-column">
						<li v-for="(column, index) in girdColumns" :key="index">
							<span>
								<md-checkbox v-model="columnData" :value="column.dataName">{{
									column.dataHeader
								}}</md-checkbox>
							</span>
						</li>
					</ul>
					<k-btn slot="reference" class="md-button md-simple">
						<md-icon md-src="/static/svg/icon/settings.svg"></md-icon>
						<span>设置</span>
					</k-btn>
				</el-popover>
			</md-card-header>
			<div v-if="dataShowTree && dataOpenExpandBtn" class="expandStyle">
				<span>全部展开</span>
				<k-field-bswitch v-model="isExpndFlag" @data-on-change="changeExpand"></k-field-bswitch>
			</div>
			<md-card-content>
				<div class="k-grid">
					<div class="table-box" ref="tableBox">
						<el-table
							v-if="!headerHide"
							v-loading="loading"
							ref="kgrid"
							:data="list"
							:row-key="tableOptions.dataTreeId"
							:height="tableOptions.dataHeight || '100'"
							:stripe="true"
							:expand-row-keys="dataExpendRow"
							:max-height="dataMaxHeight"
							:border="true"
							:default-expand-all="
								tableOptions.dataExpandAll === true || tableOptions.dataExpandAll === 'true'
							"
							:lazy="dataLazy"
							:load="dataLoad"
							@row-click="handleRowClick"
							@row-dblclick="handleDbRowClick"
							@selection-change="handleSelectionChange"
							style="width: 100%"
							@sort-change="sortChange"
							:default-sort="defaultSort"
							:row-class-name="tableRowClassName"
							:header-row-class-name="headerRowClassName"
							@header-dragend="setWidth"
						>
							<el-table-column align="center" label="序号" width="60" v-if="dataColumnIndex">
								<template slot-scope="scope">
									<span>{{ (start - 1) * dataPageSize + scope.$index + 1 }}</span>
								</template>
							</el-table-column>

							<el-table-column v-if="treeGridOptions.length > 0" :fixed="dataFixed" type="expand">
								<template slot-scope="props">
									<el-form label-position="left" inline class="k-tree-expand">
										<el-form-item
											v-for="item in treeGridOptions"
											:key="item.header"
											:label="item.header"
										>
											<span>{{ props.row[item.name] }}</span>
										</el-form-item>
									</el-form>
								</template>
							</el-table-column>

							<el-table-column
								:fixed="dataFixed"
								v-if="this.$slots['expand'] || this.$scopedSlots['expand']"
								type="expand"
							>
								<template slot-scope="{ row }">
									<slot name="expand" :row="row"></slot>
								</template>
							</el-table-column>

							<el-table-column
								v-if="
									(tableOptions.dataCheckbox === true || tableOptions.dataCheckbox === 'true') &&
									treeGridOptions.length === 0
								"
								type="selection"
								:width="tableOptions.dataCheckboxWidth"
								:fixed="true"
								align="right"
								:reserve-selection="dataReserveSelection"
							>
							</el-table-column>

							<el-table-column
								:width="operateWidth"
								:fixed="dataFixed"
								label="操作"
								v-if="
									(dataOperateColumn === true || dataOperateColumn === 'true') &&
									dataOperateColumnPosition == 'first'
								"
							>
								<template slot-scope="{ row }">
									<slot name="operate" :row="{ row }"></slot>
								</template>
							</el-table-column>

							<template v-for="(column, index) in girdColumns">
								<k-grid-column
									v-if="column.dataShow"
									:key="index"
									:data-header="column.dataHeader"
									:data-name="column.dataName"
									v-bind="column.dataExt"
								></k-grid-column>
							</template>

							<slot></slot>

							<el-table-column
								:width="operateWidth"
								:fixed="dataFixed"
								label="操作"
								v-if="
									(dataOperateColumn === true || dataOperateColumn === 'true') &&
									dataOperateColumnPosition == 'end'
								"
							>
								<template slot-scope="{ row }">
									<slot name="operate" :row="{ row }"></slot>
								</template>
							</el-table-column>

							<div slot="empty" class="empty-box">
								<img src="/static/svg/all/table-empty.svg" alt="" />
								<span>暂无数据</span>
							</div>
						</el-table>
					</div>

					<k-pagination
						v-if="dataPageSize > 0 && total > 0 && !this.dataShowTree"
						:total="total"
						:page.sync="start"
						:limit.sync="limit"
						@pagination="handlePagination"
						:layout="tableOptions.dataPaginationLayout"
					/>
				</div>
			</md-card-content>
		</md-card>
		<div v-else class="k-grid">
			<slot name="search"></slot>
			<div v-if="dataShowTree && dataOpenExpandBtn" class="expandStyle">
				<span>全部展开</span>
				<k-field-bswitch v-model="isExpndFlag" @data-on-change="changeExpand"></k-field-bswitch>
			</div>
			<el-table
				v-if="!headerHide"
				v-loading="loading"
				ref="kgrid"
				:data="list"
				:row-key="tableOptions.dataTreeId"
				:height="tableOptions.dataHeight || '100'"
				:stripe="tableOptions.dataStripe"
				:border="tableOptions.dataBorder === true || tableOptions.dataBorder === 'true'"
				:default-expand-all="tableOptions.dataExpandAll === true || tableOptions.dataExpandAll === 'true'"
				:lazy="dataLazy"
				:load="dataLoad"
				@row-click="handleRowClick"
				@row-dblclick="handleDbRowClick"
				@selection-change="handleSelectionChange"
				style="width: 100%"
				@sort-change="sortChange"
				:default-sort="defaultSort"
				:row-class-name="tableRowClassName"
				@header-dragend="setWidth"
			>
				<el-table-column v-if="treeGridOptions.length > 0" :fixed="dataFixed" type="expand">
					<template slot-scope="props">
						<el-form label-position="left" inline class="k-tree-expand">
							<el-form-item v-for="item in treeGridOptions" :key="item.header" :label="item.header">
								<span>{{ props.row[item.name] }}</span>
							</el-form-item>
						</el-form>
					</template>
				</el-table-column>

				<el-table-column
					:fixed="dataFixed"
					v-if="this.$slots['expand'] || this.$scopedSlots['expand']"
					type="expand"
				>
					<template slot-scope="{ row }">
						<slot name="expand" :row="row"></slot>
					</template>
				</el-table-column>

				<el-table-column
					v-if="
						(tableOptions.dataCheckbox === true || tableOptions.dataCheckbox === 'true') &&
						treeGridOptions.length === 0
					"
					type="selection"
					:width="tableOptions.dataCheckboxWidth"
					:fixed="dataFixed"
					align="right"
					:reserve-selection="dataReserveSelection"
				>
				</el-table-column>

				<el-table-column
					:width="operateWidth"
					:fixed="dataFixed"
					label="操作"
					v-if="
						(dataOperateColumn === true || dataOperateColumn === 'true') &&
						dataOperateColumnPosition == 'first'
					"
				>
					<template slot-scope="{ row }">
						<slot name="operate" :row="{ row }"></slot>
					</template>
				</el-table-column>

				<template v-for="(column, index) in girdColumns">
					<k-grid-column
						v-if="column.dataShow"
						:key="index"
						:data-header="column.dataHeader"
						:data-name="column.dataName"
					></k-grid-column>
				</template>

				<slot></slot>

				<el-table-column
					:width="operateWidth"
					:fixed="dataFixed"
					label="操作"
					v-if="
						(dataOperateColumn === true || dataOperateColumn === 'true') &&
						dataOperateColumnPosition == 'end'
					"
				>
					<template slot-scope="{ row }">
						<slot name="operate" :row="{ row }"></slot>
					</template>
				</el-table-column>

				<div slot="empty" class="empty-box">
					<img src="/static/svg/all/table-empty.svg" alt="" />
					<span>暂无数据</span>
				</div>
			</el-table>

			<k-pagination
				v-if="dataPageSize > 0 && total > 0 && !this.dataShowTree"
				:total="total"
				:page.sync="start"
				:limit.sync="limit"
				@pagination="handlePagination"
				:layout="tableOptions.dataPaginationLayout"
			/>
		</div>

		<k-grid-display ref="kGridDisplay" :gridColumnOptions="gridColumnOptions" :row="doubleClickRow" />
	</div>
</template>

<script>
import props from "@/components/k-element/common/k-field-props.js";
import event from "@/components/k-element/common/k-field-event.js";
import emitter from "@/components/k-element/common/k-emitter.js";
import moment from "moment";
import sticky from "./table-sticky";
import { camelCase, forEach, merge } from "lodash";
import Tools from "@/utils/tools.js";

import KPagination from "./k-grid-pagination";

const descReg = /^[Dd][Ee][Ss][Cc]$/;
const ascReg = /^[Aa][Ss][Cc]$/;

export default {
	name: "KGrid",
	mixins: [props(), event(), emitter(), sticky],
	components: {
		KPagination,
	},
	props: {
		dataBorder: {
			type: [Boolean, String],
			default: false,
		},
		dataRender: {
			type: String,
			default: "",
		},
		dataParams: {
			type: Object,
		},
		dataAlign: {
			type: String,
			default: "left",
		},
		dataBeforeLoad: {
			type: Function,
		},
		dataPageSize: {
			type: Number,
			default: 10,
		},
		dataAutoload: {
			type: [Boolean, String],
			default: true,
		},
		dataOperateColumn: {
			type: [Boolean, String],
			default: true,
		},
		dataOperateColumnPosition: {
			type: String,
			default: "end",
		},
		dataDisplay: {
			type: [Boolean, String],
			default: true,
		},
		dataCard: {
			type: [Boolean, String],
			default: true,
		},
		dataFixed: {
			type: [Boolean, String],
			default: false,
		},
		dataOverflow: {
			type: [Boolean, String],
			default: true,
		},
		dataShowTree: {
			type: Boolean,
			default: false,
		},
		dataReserveSelection: {
			type: Boolean,
			default: false,
		},
		dataCheckboxMultiple: {
			type: Boolean,
			default: true,
		},
		dataExpendRow: {
			type: Array,
		},
		// 双击重载详情
		dataDbClickLoad: {
			type: [Boolean, String],
			default: false,
		},
		// 主键字段
		dataKeys: {
			type: String,
			default: "",
		},
		dataEntity: {
			type: String,
			default: "",
		},
		dataMaxHeight: {
			type: Number,
			default: undefined,
		},
		dataSticky: {
			type: Boolean,
			default: false,
		},
		dataColumnIndex: {
			type: Boolean,
			default: false,
		},
		dataLazy: {
			type: Boolean,
			default: false,
		},
		dataLoad: {
			type: Function,
			default: undefined,
		},
		// 是否显示树形展开按钮
		dataOpenExpandBtn: {
			type: Boolean,
			default: false,
		},
		dataContentType: {
			type: String,
			default: "form",
		},
		dataHeight: {
			type: String,
			default: "100",
		},
		handleDataFun: {
			type: Function
		},
		dataDictType: {
			type: String,
			default: "" // 1:   key:value
		},
		customRowClass: {
			type: Function
		}
	},
	created() {
		// buttons
		this.initMtd();
	},
	data() {
		return {
			start: 1,
			limit: this.dataPageSize,
			dir: "",
			sort: "",
			gridColumnOptions: [],
			treeGridOptions: [],
			gridOptions: {},
			btnOptions: {},
			selectRows: [],
			list: [],
			total: 0,
			compareList: [],
			cache: {},
			dictKv: {},
			cachedParams: {},
			doubleClickRow: {},
			selected: [],
			cancerSelecteds: [],
			autoSelected: false,
			loadParams: {},
			parent: "k-grid",
			girdColumns: [],
			columnData: [],
			headerHide: false,
			isExpndFlag: true,
			tableHeight: "200px",
			loading: false,
		};
	},
	computed: {
		showSubscript() {
			if (this.dataShowSubscript != null) {
				return this.dataShowSubscript;
			}
			let parent = this.getParent("KPopup");
			return !parent;
		},
		tableColumnOptions() {
			return this.gridColumnOptions.filter(
				(option) => option.dataHidden == false || (option.dataHidden !== "true" && option.dataHidden != true)
			);
		},
		tableOptions() {
			return this.gridOptions;
		},
		treeProps() {
			return this.tableOptions.dataTreeProps;
		},
		operateWidth() {
			return this.gridOptions.dataOperateWidth;
		},
		defaultSort() {
			for (let i = 0; i < this.tableColumnOptions.length; i++) {
				let o = this.tableColumnOptions[i];
				if (descReg.test(o.dataDefaultSort)) {
					return {
						prop: o.dataName,
						order: "descending",
					};
				} else if (ascReg.test(o.dataDefaultSort)) {
					return {
						prop: o.dataName,
						order: "ascending",
					};
				}
			}
			return {
				prop: "",
				order: "",
			};
		},
		iconStyle() {
			let iconStyle = {};
			iconStyle.background = this.$store.state.system.cardBackground;
			return iconStyle;
		},
	},
	watch: {
		dataEntity(newVal) {
			this.initConfig(() => {});
		},
		list(newVal, oldVal) {
			this.handleDefaultSelected();
			this.handleCancerSelected();
		},
		columnData: {
			handler(data) {
				this.girdColumns.map((column) => {
					column.dataShow = !!data.includes(column.dataName);
				});

				let gridFields = "";

				data.map((row) => {
					if (gridFields) {
						gridFields += "," + row;
					} else {
						gridFields = row;
					}
				});
				this.httpUtil
					.ajax({
						url: "/graphql/updateGridDefault.json",
						params: {
							modelName: this.dataEntity,
							gridFields: gridFields,
						},
						successAlert: false,
					})
					.then((data) => {});
			},
		},
		// total: {
		// 	handler(v) {
		// 		if (v) {
		// 			this.$nextTick(() => {
		// 				this.onPageSize();
		// 			});
		// 		}
		// 	},
		// },
	},
	methods: {
		initMtd() {
			this.initConfig((dicts) => {
				let btnOptions = [];
				if (this.$slots.default) {
					this.$slots.default
						.filter((column) => {
							return column.tag && column.tag == "k-grid-btn";
						})
						.forEach((column) => {
							let res = {};
							res["text"] = column.children ? column.children[0].text : "button";
							forEach(column.data.attrs, function (value, key) {
								res[camelCase(key)] = value;
							});
							btnOptions.push(res);
						});
				}

				this.btnOptions = btnOptions;

				// gridOptions
				let gridOptions = {};
				let mergedOption = merge({}, this.$props, this.$attrs);
				forEach(mergedOption, function (value, key) {
					gridOptions[camelCase(key)] = value;
				});
				this.gridOptions = gridOptions;

				// gridColumnOptions
				let gridColumnOptions = [];
				if (this.$slots.default) {
					this.$slots.default
						.filter((column) => {
							return column.componentOptions && column.componentOptions.tag == "k-grid-column";
						})
						.forEach((column) => {
							this.loadColumnConfig(gridColumnOptions, dicts, column);
						});
					this.gridColumnOptions = gridColumnOptions;
				}

				// treeGridOptions
				let treeGridOptions = [];

				let dataInnerHeaders = this.tableOptions.dataInnerHeaders;
				let dataInnerNames = this.tableOptions.dataInnerNames;
				if (dataInnerHeaders && dataInnerNames) {
					let innerHeaderArr = dataInnerHeaders.split(",");
					let innerNamesArr = dataInnerNames.split(",");
					if (innerHeaderArr.length !== innerNamesArr.length) {
						throw new Error("tree-grid 的配置不正确");
					}
					innerHeaderArr.forEach((item, idx) => {
						treeGridOptions.push({
							header: innerHeaderArr[idx],
							name: innerNamesArr[idx],
						});
					});
				}
				this.treeGridOptions = treeGridOptions;

				if (this.dataShowTree && this.dataOpenExpandBtn) {
					// 全部展开按钮值初始化
					if (this.tableOptions.dataExpandAll) {
						this.isExpndFlag = this.tableOptions.dataExpandAll;
					} else {
						this.isExpndFlag = false;
					}
				}
				this.$emit("init", this);

				if (dicts.length > 0) {
					let promiseAll = [];
					dicts.forEach((item) => {
						promiseAll.push(this.httpUtil.dict(item));
					});

					let _this = this;
					Promise.all(promiseAll).then(function (values) {
						let res = {};
						for (let i = 0; i < values.length; i++) {
							if (values[i].length === 0) {
								console.error("请检查数据字典是否存在");
								continue;
							}
							let kv = (res[values[i][0].dict] = {});
							values[i].forEach((item) => {
								kv[item.itemkey] = item.itemval;
							});
						}

						_this.dictKv = res;
						_this.initData();
					});
				} else {
					this.initData();
				}
			});
		},
		/**
		 * 初始化表格列配置
		 */
		initConfig(callback) {
			let dicts = [];
			if (this.dataEntity) {
				this.headerHide = true;
				this.httpUtil
					.ajax({
						url: "/graphql/grid.json",
						params: { modelName: this.dataEntity },
					})
					.then((data) => {
						if (data.success) {
							// 显示设置列表
							this.girdColumns = [];
							this.gridColumnOptions = [];
							data.rows.forEach((itemData) => {
								this.girdColumns.push({
									dataHeader: itemData.label,
									dataName: itemData.field,
									dataShow: itemData.dataShow,
								});
								this.gridColumnOptions.push({
									dataHeader: itemData.label,
									dataName: itemData.field,
								});
							});
						}
						this.httpUtil
							.ajax({
								url: "/graphql/gridDefault.json",
								params: { modelName: this.dataEntity },
							})
							.then((res) => {
								if (res.success) {
									let rows = res.rows;
									this.columnData = [];
									rows.forEach((item) => {
										this.columnData.push(item.field);
									});
								}
								callback(dicts);
								this.headerHide = false;
							})
							.catch(() => {
								this.headerHide = false;
							});
					})
					.catch(() => {
						this.headerHide = false;
					});
			} else {
				callback(dicts);
			}
		},
		initData() {
			this.$nextTick(() => {
				// this.onPageSize();
				if (this.dataData && Object.keys(this.dataData).length != 0) {
					this.list = this.dataData.rows;
					this.total = this.dataData.total;
					this.loading = false;
				} else if (this.gridOptions.dataAutoload === true || this.gridOptions.dataAutoload === "true") {
					this.firstLoad();
				}
			});
		},
		setWidth() {
			if (this.dataSticky) this.headerDragend();
		},
		firstLoad() {
			if (this.defaultSort) {
				this.sortChange(this.defaultSort);
			} else {
				this._load();
			}
		},
		sortChange(value) {
			this.dir = value.order == "ascending" ? "asc" : "desc";
			this.sort = value.prop;
			this._load();
		},
		getColumnOptions() {
			return merge({}, this.gridColumnOptions);
		},
		getSelected() {
			return Array.from(this.selectRows);
		},
		getRow(id) {
			let dataCheckboxId = this.gridOptions.dataCheckboxId;
			if (!dataCheckboxId) {
				throw new Error("请指定行主键");
			}
			for (let i = 0; i < this.list.length; i++) {
				let item = this.list[i];
				if (item[dataCheckboxId] === id) {
					return item;
				}
			}
			return null;
		},
		getRowData(ev) {
			if (!ev) {
				return null;
			}

			let idx = null;
			let currentRow = ev.target.closest(".el-table__row");
			if (!currentRow) {
				return null;
			}

			let rows = this.$refs.kgrid.$el.querySelectorAll(".el-table__row");
			for (let index = 0; index < rows.length; index++) {
				const item = rows[index];
				if (currentRow["rowIndex"] === item["rowIndex"]) {
					idx = index;
					break;
				}
			}
			return idx != null ? Object.assign({}, this.flattenTableDataDeep()[idx]) : null;
		},
		setSelected(ids) {
			this.$refs.kgrid.clearSelection();
			(ids || []).forEach((id) => {
				let row = this.getRow(id);
				row && this.$refs.kgrid.toggleRowSelection(row);
			});
		},
		clearAll() {
			this.$refs.kgrid.clearSelection();
		},
		selectAll() {
			this.$refs.kgrid.toggleAllSelection();
		},
		getCachedParams() {
			return this.cachedParams;
		},
		handlePagination() {
			this._load();
		},
		//设置默认
		handleDefaultSelected() {
			for (var index = 0; index < this.selected.length; index++) {
				var item = this.selected[index];
				this.list.map((item2) => {
					if (item[this.gridOptions.dataTreeId] == item2[this.gridOptions.dataTreeId]) {
						this.autoSelected = true;
						this.$refs.kgrid.toggleRowSelection(item2);
						this.selected.splice(index, 1);
						index--;
					}
				});
			}
			// this.selected.map((item, index) => {
			//   this.list.map(item2 => {
			//     if (item[this.gridOptions.dataTreeId] == item2[this.gridOptions.dataTreeId]) {
			//       console.log(index,item2.userid)
			//       this.autoSelected = true
			//       this.$refs.kgrid.toggleRowSelection(item2);
			//       this.selected.splice(index, 1)
			//     }
			//   })
			// })
		},
		handleCancerSelected() {
			this.cancerSelecteds.map((item, index) => {
				this.list.map((item2) => {
					if (item == item2[this.gridOptions.dataTreeId]) {
						this.autoSelected = true;
						this.$nextTick(() => {
							this.$refs.kgrid.toggleRowSelection(item2, false);
							this.cancerSelecteds.splice(index, 1);
						});
					}
				});
			});
		},
		cancerSelected(value, valueField) {
			let a = true;
			this.selected.map((item, index) => {
				if (item[valueField] == value) {
					this.selected.splice(index, 1);
					a = false;
				}
			});

			if (a == false) {
				return;
			}

			// this.list.map(item => {
			//   if (item[valueField] == value) {
			//     this.autoSelected = true
			//     this.$refs.kgrid.toggleRowSelection(item, false);
			//     a = false
			//   }
			// })

			if (a) {
				this.cancerSelecteds.push(value);
				this.handleCancerSelected();
			}
		},
		load(params = {}, start) {
			this.loading = true;
			if (!start) {
				this.start = 1;
			}
			this.cachedParams = params;
			this._load();
		},
		loadData(list) {
      this.list = list;
      this.total = list.length;
			this.loading = false;
    },
		buildQueryParams() {
			let reqParams = merge({}, this.cachedParams);

			if (this.dataParams) {
				reqParams = merge(reqParams, this.dataParams);
			}

			if (this.dataBeforeLoad) {
				reqParams = this.dataBeforeLoad(reqParams);
				if (reqParams === false) {
					return;
				}
			}

			if (!this.gridOptions.dataShowTree && this.limit > 0) {
				reqParams.limit = this.limit;
				// 修改分页
				reqParams.start = (this.start - 1) * this.limit;
			}

			if (this.sort && this.dir) {
				reqParams.sort = this.sort;
				reqParams.dir = this.dir;
			}
			return reqParams;
		},
		//此方法是针对k-field-grid写的
		loadFieldGridData(params, valueField) {
			let gridOptions = this.gridOptions;
			let result;
			let reqParams = merge({}, params);

			if (this.dataParams) {
				reqParams = merge(reqParams, this.dataParams);
			}

			let $this = this;

			if (gridOptions.dataGraphql && gridOptions.dataAction) {
				throw new Error("请设置唯一的请求地址 [data-graphql |  data-action]");
			}

			// 普通 graph grid
			if (gridOptions.dataGraphql) {
				this.httpUtil
					.graphqlQurey({
						graphql: this.gridOptions.dataGraphql,
						params: reqParams,
					})
					.then((data) => {
						//获取请求头
						let graphqlFirst = this.getGraphKey();
						result = data[graphqlFirst].rows;
						if (result.length > 0) {
							let a = true;
							this.list.map((item) => {
								if (item[valueField] == result[0][valueField]) {
									this.autoSelected = true;
									a = false;
									this.$refs.kgrid.toggleRowSelection(item);
								}
							});
							if (a) {
								this.selected.push(result[0]);
							}
							this.$emit("dd", result);
						}
					});
				return;
			}

			// 普通  action  grid
			if (gridOptions.dataAction) {
				let action = this.gridOptions.dataAction;
				if (action.indexOf("/wf/") > -1 || action.indexOf("wf/") > -1) {
					this.httpUtil
						.ajax({
							url: action,
							params: reqParams,
						})
						.then((data) => {
							result = data.rows;
							if (result.length > 0) {
								let a = true;
								this.list.map((item) => {
									if (item[valueField] == result[0][valueField]) {
										this.autoSelected = true;
										a = false;
										// this.$refs.kgrid.toggleRowSelection(item);
									}
								});
								if (a) {
									this.selected.push(result[0]);
									this.handleDefaultSelected();
								}
								this.$emit("dd", result);
							}
						});
				} else {
					this.httpUtil
						.comnQuery({
							action: this.gridOptions.dataAction,
							params: reqParams,
						})
						.then((data) => {
							result = data.rows;
							if (result.length > 0) {
								let a = true;
								this.list.map((item) => {
									if (item[valueField] == result[0][valueField]) {
										this.autoSelected = true;
										a = false;
										//
										// this.$refs.kgrid.toggleRowSelection(item);
									}
								});
								if (a) {
									this.selected.push(result[0]);
									this.handleDefaultSelected();
								}
								this.$emit("dd", result);
							}
						});
				}
			}
		},
		_load() {
			let gridOptions = this.gridOptions;
			let reqParams = this.buildQueryParams();

			let $this = this;

			function cb(data) {
				// gridOptions.dataAfterLoad && $this.$parent[gridOptions.dataAfterLoad] && $this.$parent[gridOptions
				//   .dataAfterLoad]
				// (data);
				gridOptions.dataAfterLoad && gridOptions.dataAfterLoad(data);
			}

			if (gridOptions.dataGraphql && gridOptions.dataAction) {
				throw new Error("请设置唯一的请求地址 [data-graphql |  data-action]");
			}

			this.loadParams = reqParams;

			//通 graph treegrid
			if (gridOptions.dataShowTree && gridOptions.dataGraphql) {
				this.validateTreetree();
				this.loadGraphqlTreeData(reqParams, cb);
				return;
			}

			//通 action treegrid
			if (gridOptions.dataShowTree && gridOptions.dataAction) {
				this.validateTreetree();
				this.loadActionTreeData(reqParams, cb);
				return;
			}

			//通 url treegrid
			if (gridOptions.dataShowTree && gridOptions.dataUrl) {
				this.validateTreetree();
				this.loadUrlTreeData(reqParams, cb);
				return;
			}

			// 普通 graph grid
			if (gridOptions.dataGraphql) {
				this.loadGraphqlData(reqParams, cb);
				return;
			}

			// 普通  action  grid
			if (gridOptions.dataAction) {
				this.loadActionData(reqParams, cb);
				return;
			}

			// 普通  url  grid
			if (gridOptions.dataUrl) {
				this.loadUrlData(reqParams, cb);
				return;
			}
		},
		validateTreetree() {
			if (!this.gridOptions.dataTreeId) {
				throw new Error("请配置 data-tree-id");
			}
			if (!this.gridOptions.dataDiffcondition) {
				throw new Error("请配置 data-diffcondition");
			}
		},
		loadGraphqlTreeData(params, cb) {
			let graphql = this.getTreeGraphqlUrl();
			this.httpUtil
				.graphqlQurey({
					graphql: graphql,
					params: params,
				})
				.then((data) => {
					//获取请求头
					let graphqlFirst = this.getGraphKey();
					this.list = data[graphqlFirst].rows;
					this.loading = false;
					cb && cb(merge({}, this.list));
				});
		},
		loadActionTreeData(params, cb) {
			params.diffcondition = this.gridOptions.dataDiffcondition;
			this.httpUtil
				.comnQueryTree({
					action: this.gridOptions.dataAction,
					params: params,
				})
				.then((data) => {
					this.list = data.rows;
					this.loading = false;
					cb && cb(merge({}, data.rows));
				});
		},
		loadUrlTreeData(params, cb) {
			params.diffcondition = this.gridOptions.dataDiffcondition;
			this.httpUtil
				.ajax({
					url: this.gridOptions.dataUrl,
					params: params,
				})
				.then((data) => {
					this.list = data.rows;
					this.loading = false;
					cb && cb(merge({}, data.rows));
				});
		},
		loadGraphqlData(params, cb) {
			this.httpUtil
				.graphqlQurey({
					graphql: this.gridOptions.dataGraphql,
					params: params,
				})
				.then((data) => {
					//获取请求头
					let graphqlFirst = this.getGraphKey();
					this.list = data[graphqlFirst].rows;
					this.total = data[graphqlFirst].results;
					this.loading = false;
					cb && cb(merge({}, this.list));
				});
		},
		loadActionData(params, cb) {
			let action = this.gridOptions.dataAction;
			if (action.indexOf("/wf/") > -1 || action.indexOf("wf/") > -1) {
				this.httpUtil
					.ajax({
						url: action,
						params: params,
					})
					.then((data) => {
						this.list = data.rows;
						this.total = data.total;
						this.compareList = data.rowsList1;
						this.loading = false;
						cb && cb(merge({}, data.rows));
					});
			} else {
				this.httpUtil
					.comnQuery({
						action: this.gridOptions.dataAction,
						params: params,
					})
					.then((data) => {
						let _data = data.rows;
						if (this.handleDataFun) {
							_data = this.handleDataFun(data.rows);
						}
						this.list = _data;
						this.total = data.results;
						this.compareList = data.rowsList1;
						this.loading = false;
						cb && cb(merge({}, data.rows));
					});
			}
		},
		loadUrlData(params, cb) {
			if (this.dataContentType === "json") {
				this.httpUtil
					.ajaxJson({
						url: this.gridOptions.dataUrl,
						params: params,
					})
					.then((data) => {
						this.list = data.rows;
						this.total = data.results;
						this.loading = false;
						cb && cb(merge({}, data.rows));
					});
			} else {
				this.httpUtil
					.ajax({
						url: this.gridOptions.dataUrl,
						params: params,
					})
					.then((data) => {
						this.list = data.rows;
						this.total = data.results;
						this.loading = false;
						cb && cb(merge({}, data.rows));
					});
			}
		},
		getTreeGraphqlUrl() {
			let gridOptions = this.gridOptions;
			let dataGraphql = gridOptions.dataGraphql;
			let dataDiffcondition = gridOptions.dataDiffcondition;
			let data_graphql = dataGraphql.trim();
			let queryName = data_graphql.substring(data_graphql.indexOf("{") + 1, data_graphql.indexOf("("));
			data_graphql += "treeConfig{";
			data_graphql += queryName;
			data_graphql += ":{";
			data_graphql += "diffcondition: '" + dataDiffcondition + "'";
			data_graphql += "}";
			data_graphql += "}";
			return data_graphql;
		},
		getGraphKey() {
			let graphql = this.gridOptions.dataGraphql;
			if (graphql) {
				return graphql.substring(graphql.indexOf("{") + 1, graphql.indexOf("("));
			}
			return "";
		},
		handleSelectionChange(selection, row) {
			if (!this.autoSelected) {
				//如果为多选
				if (this.dataCheckboxMultiple) {
					this.selectRows = selection;
					selection = selection.concat(this.selected);
					this.$emit("data-select-change", selection);
				} //如果为单选
				else {
					if (selection.length == 0 || selection.length == 1) {
						{
							this.selectRows = selection;
							this.$emit("data-select-change", selection);
						}
					} else {
						this.$refs.kgrid.clearSelection();
						this.$refs.kgrid.toggleRowSelection(selection[selection.length - 1]);
					}
				}
			} else {
				this.autoSelected = false;
			}
		},
		tableRowClassName({ row, rowIndex }) {
			row.row_index = rowIndex + 1;
			if (this.customRowClass) {
				return this.customRowClass(row);
			}
			return '';
		},
		headerRowClassName({ row, rowIndex }) {
			return `header${rowIndex}`
		},
		handleRowClick(row, column, event) {
			if (this.dataDbClickLoad && row.row_index > 1) {
				let gridOptions = this.gridOptions;
				let params = {};
				this.dataKeys.split(",").map((key) => {
					params[key] = row[key];
				});
				let reqParams = merge(this.loadParams, params);
				if (gridOptions.dataAction) {
					this.httpUtil
						.comnQuery({
							action: gridOptions.dataAction,
							params: reqParams,
						})
						.then((data) => {
							this._handleRowClick(data.rows[0], column, event);
						});
				}
				if (gridOptions.dataUrl) {
					this.httpUtil
						.ajax({
							url: gridOptions.dataUrl,
							params: reqParams,
						})
						.then((data) => {
							this._handleRowClick(data.rows[0], column, event);
						});
				}
			} else {
				this._handleRowClick(row, column, event);
			}
		},
		_handleRowClick(row, column, event) {
			this.$emit("data-row-select", row, column, event);
			if (this.$slots["expand"] || this.$scopedSlots["expand"]) {
				this.$refs.kgrid.toggleRowExpansion(row);
			}
		},
		handleDbRowClick(row, column, event) {
			if (this.dataDbClickLoad && row.row_index > 1) {
				let gridOptions = this.gridOptions;
				let params = {};
				this.dataKeys.split(",").map((key) => {
					params[key] = row[key];
				});
				let reqParams = merge(this.loadParams, params);
				if (gridOptions.dataAction) {
					this.httpUtil
						.comnQuery({
							action: gridOptions.dataAction,
							params: reqParams,
						})
						.then((data) => {
							this._handleDbRowClick(data.rows[0], column, event);
						});
				}
				if (gridOptions.dataUrl) {
					this.httpUtil
						.ajax({
							url: gridOptions.dataUrl,
							params: reqParams,
						})
						.then((data) => {
							this._handleDbRowClick(data.rows[0], column, event);
						});
				}
			} else {
				this._handleDbRowClick(row, column, event);
			}
		},
		_handleDbRowClick(row, column, event) {
			this.doubleClickRow = row;
			if (this.dataDisplay === true || this.dataDisplay === "true") {
				setTimeout(() => {
					this.$refs.kGridDisplay.popup();
				}, 50);
			}
			this.$emit("data-db-click", row, column, event);
		},
		_handleBtnClick(row, option, vm) {
			if (option.dataClick) {
				this.$parent[option.dataClick](row);
			}
		},
		handleBtnDisabled(row, option) {
			if (option.dataDisableHandler) {
				return this.$parent[option.dataDisableHandler](row);
			}
			return false;
		},
		handleBtnShow(row, option) {
			if (option.dataHidenHandler) {
				return !this.$parent[option.dataHidenHandler](row);
			}
			return true;
		},
		dictTransfer(dict, dictVal, dataDictType) {
			if (dict && dictVal) {
				let dictVals;
				let regex = ",";
				if (dictVal.indexOf("/") >= 0) {
					regex = "/";
				}
				dictVals = dictVal.toString().split(regex);
				let transferValue;
				for (let i = 0; i < 1; i++) {
					if (i == 0) {
						transferValue = this.doDictTransfer(dict, dictVals, dataDictType, regex);
						if (transferValue) {
							transferValue = transferValue.toString();
						}
					} else {
						if (regex == "/") {
							transferValue += "/" + this.doDictTransfer(dict, dictVals, dataDictType, regex);
						} else {
							transferValue += "，" + this.doDictTransfer(dict, dictVals, dataDictType, regex);
						}
					}
				}
				return transferValue;
			}
			return dictVal;
		},
		doDictTransfer(dict, dictVal, dataDictType, regex) {
			if (this.dictKv[dict]) {
				if (dictVal.length > 1) {
					let dictVals = "";
					dictVal.forEach((val) => {
						// const prev = (this.dataDictType == "1" || dataDictType == "1") ? val+" " : "";
						// let str = "";
						// if (dataDictType) {
						// 	if (dataDictType == "0") {
						// 		str = "";
						// 	} else if (dataDictType == "1") {
						// 		str = val + " ";
						// 	}
						// } else if(this.dataDictType == "1") {
						// 	str = val + " ";
						// }
						const prev = this.getDictLabel(dataDictType, val);
						if (regex == "/") {
							dictVals += prev+this.dictKv[dict][val] + "/";
						} else {
							dictVals += prev+this.dictKv[dict][val] + "，";
						}
					});
					let reg;
					if (regex == "/") {
						reg = /\/$/gi;
					} else {
						reg = /，$/gi;
					}
					dictVals = dictVals.replace(reg, "");
					return dictVals;
				}
				// const prev = (this.dataDictType == "1" || dataDictType == "1") ? dictVal+" " : "";
				const prev = this.getDictLabel(dataDictType, dictVal);
				return this.dictKv[dict][dictVal] ? prev+this.dictKv[dict][dictVal] : "";
			}
			return dictVal;
		},
		getDictLabel(dataDictType, v) {
			let str = "";
			if (dataDictType) {
				if (dataDictType == "0") {
					str = "";
				} else if (dataDictType == "1") {
					str = v + " ";
				}
			} else if(this.dataDictType == "1") {
				str = v + " ";
			}
			return str;
		},
		flattenDeepHelper(tableData, res) {
			for (let i = 0; i < tableData.length; i++) {
				let item = tableData[i];
				res.push(item);
				if (item.children) {
					this.flattenDeepHelper(item.children, res);
				}
			}
		},
		flattenTableDataDeep() {
			let res = [];
			this.flattenDeepHelper(this.list, res);
			return res;
		},
		/**
		 * 加载表头配置
		 */
		loadColumnConfig(gridColumnOptions, dicts, column) {
			if (column.componentOptions && column.componentOptions.tag == "k-grid-column") {
				let res = {};
				forEach(column.data.attrs, function (value, key) {
					if (camelCase(key) === "dataDict") {
						dicts.push(value);
					}
					res[camelCase(key)] = value;
				});
				forEach(column.componentOptions.propsData, function (value, key) {
					if (camelCase(key) === "dataDict") {
						dicts.push(value);
					}
					res[camelCase(key)] = value;
				});
				gridColumnOptions.push(res);

				let childrens = column.componentOptions.children;
				if (childrens && childrens.length > 0) {
					childrens.map((children) => {
						this.loadColumnConfig(gridColumnOptions, dicts, children);
					});
				}
			}
		},
		changeExpand(flag) {
			// 全部展开/全部收起功能
			let $kgrid = this.$refs.kgrid;
			expandFunc(this.list, flag);
			function expandFunc(list, flag) {
				list.forEach((row) => {
					$kgrid.toggleRowExpansion(row, flag);
					if (row.children) {
						expandFunc(row.children, flag);
					}
				});
			}
		},
		// setTableHeight() {
		// 	if (this.dataTableHeight) {
		// 		this.tableHeight = this.dataTableHeight + "px";
		// 	} else {
		// 		this.tableHeight = this.$refs.tableBox.clientHeight + "px";
		// 	}
		// 	console.log(this.tableHeight, 'thhhhh',this.$refs.tableBox);
		// },
		// onPageSize() {
		// 	console.log('sss',this.$refs.tableBox,this.$refs.tableBox.clientHeight);
		// 	if (this.$refs.tableBox.clientHeight <= 0) {
		// 		return
		// 	}
		// 	window.onresize = null;
		// 	this.setTableHeight();
		// 	const _this = this;
		// 	window.onresize = function () {
		// 		_this.setTableHeight();
		// 	};
		// },
	},
	// destroyed() {
	// 	window.onresize = null;
	// },
	// activated() {
	// 	this.onPageSize();
	// },
	// deactivated() {
	// 	window.onresize = null;
	// }
	activated() {
		this.$nextTick(() => {
			setTimeout(() => {
				const mainPanel = document.querySelector(".main-panel");
				mainPanel.style.height = mainPanel.clientHeight - 0.1 + "px";
				setTimeout(() => {
					mainPanel.style.height = "100%";
				}, 30);
			}, 100);
		});
	},
};
</script>
<style lang="scss" scoped>
.empty-box {
	img {
		display: block;
		width: 64px;
		margin: 0 auto;
	}
}
</style>
