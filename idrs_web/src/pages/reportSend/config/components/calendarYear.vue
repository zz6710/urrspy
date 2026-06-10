<template>
	<div>
		<el-row>
			<el-col :span="6" v-for="item in months" :key="item.month">
				<el-calendar v-model="item.value"></el-calendar>
			</el-col>
		</el-row>
	</div>
</template>
<script>
import moment from "moment";
export default {
	data() {
		return {
			months: [],
		};
	},
	mounted() {
		this.months = new Array(12).fill("").map((item, index) => {
			const m = index + 1 < 10 ? "0" + (index + 1) : index + 1;
			const lastDay = moment([moment().format("YYYY"), index])
				.endOf("month")
				.format("YYYYMMDD");
			return {
				month: m,
				value: moment(lastDay)._d,
			};
		});
	},
	methods: {
		getAllDate() {
			return this.months.map(item=>moment(item.value).format("YYYYMMDD"))
		}
	},
};
</script>
<style lang="scss" scoped>
/deep/ {
	.el-calendar__button-group {
		display: none;
	}
	.el-calendar__body {
		padding: 5px 20px 5px;
	}
	.el-calendar-table {
		.el-calendar-day {
			height: auto;
		}
	}
}
</style>
