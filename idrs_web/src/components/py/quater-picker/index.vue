<template>
	<div class="quarter-picker">
		<el-popover placement="bottom-start" title="" width="290" trigger="click" v-model="pickerVisible">
			<div class="quarter-picker-container">
				<div class="year-header">
					<button class="el-picker-panel__icon-btn el-date-picker__prev-btn el-icon-d-arrow-left" @click="handleYearPrev"></button>
					<span>{{ year }} 年</span>
					<button class="el-picker-panel__icon-btn el-date-picker__next-btn el-icon-d-arrow-right" @click="handleYearNext"></button>
				</div>
				<div class="quarter-box">
					<div
						class="box"
						:class="{ current: item == quarter && year == currentYear, 'default-active': year == defaultYear && item == defaultQuarter }"
						v-for="item in quarterArr"
						:key="item"
						@click="handleSel(item)"
					>
						{{ item }}
					</div>
				</div>
			</div>
			<el-input
				slot="reference"
				v-model="value"
				:readonly="readonly"
				:disabled="pickerDisabled"
				@mouseenter.native="handleMouseEnter"
				@mouseleave.native="showClose = false"
				@blur="handleBlur"
			>
				<i slot="prefix" class="el-input__icon el-icon-date"> </i>
				<i slot="suffix" class="el-input__icon" @click="handleClickIcon" :class="[showClose ? '' + clearIcon : '']"> </i>
			</el-input>
		</el-popover>
	</div>
</template>
<script>
import dayjs from "dayjs";
var quarterOfYear = require("dayjs/plugin/quarterOfYear");
dayjs.extend(quarterOfYear);

export default {
	props: {
		clearIcon: {
			type: String,
			default: "el-icon-circle-close",
		},
		readonly: {
			type: Boolean,
			default: false,
		},
		pickerDisabled: {
			type: Boolean,
			default: false,
		},
		clearable: {
			type: Boolean,
			default: true,
		},
	},
	data() {
		return {
			quarterArr: ["Q1", "Q2", "Q3", "Q4"],
			value: "",
			showClose: false,
			year: "",
			currentYear: "",
			pickerVisible: false,
			quarter: "",
			minYear: 2000,
			defaultQuarter: "",
			defaultYear: "",
		};
	},
	created() {
		this.year = dayjs().year();
		this.defaultYear = dayjs().year();
		this.defaultQuarter = "Q" + dayjs().quarter();
		console.log(this.defaultYear, this.defaultQuarter, "this.defaultQuarter");
	},
	methods: {
		handleYearPrev() {
			if (this.year <= this.minYear) {
				this.year = this.minYear;
			} else {
				this.year -= 1;
			}
		},
		handleYearNext() {
			this.year = Number(this.year) + 1;
		},
		handleClickIcon(event) {
			if (this.showClose) {
				this.currentYear = "";
				this.quarter = "";
				this.setValue();
				event.stopPropagation();
				this.showClose = false;
				this.pickerVisible = false;
			}
		},
		handleSel(v) {
			this.quarter = v || this.quarter;
			this.currentYear = this.year;
			this.setValue();
			this.pickerVisible = false;
		},
		setValue() {
			if (this.currentYear && this.quarter) {
				this.value = this.currentYear + "-" + this.quarter;
			} else {
				this.value = "";
			}
		},
		handleMouseEnter() {
			if (this.readonly || this.pickerDisabled) return;
			if (this.value && this.clearable) {
				this.showClose = true;
			}
		},
		handleBlur() {
			const arr = this.value.split("-");
			if (/^\d{4}-Q[1-4]$/.test(this.value) && arr[0] >= this.minYear) {
				this.year = arr[0];
				this.currentYear = arr[0];
				this.quarter = arr[1];
			}
			this.setValue();
		},
	},
};
</script>
<style lang="scss" scoped>
.quarter-picker {
}
.quarter-picker-container {
	user-select: none;
	.year-header {
		text-align: center;
		line-height: 30px;
		border-bottom: 1px solid rgb(235, 238, 245);
		padding-bottom: 12px;
		.el-date-picker__prev-btn {
			float: left;
		}
		.el-date-picker__next-btn {
			float: right;
		}
	}
	.quarter-box {
		display: flex;
		line-height: 64px;
		.box {
			flex: 1;
			text-align: center;
			&:hover {
				color: rgb(64, 158, 255);
			}
			&.current {
				color: rgb(64, 158, 255);
			}
			&.default-active {
				color: rgb(64, 158, 255);
				font-weight: 500;
			}
		}
	}
}
</style>