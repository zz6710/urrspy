<template>
	<md-card class="box-card" style="overflow: visible; position: unset">
		<!-- <md-card-header class="md-card-header-text md-card-header-green" style="margin-right: 0;">
      <div class="search-header">
        <div class="card-icon" :style="iconStyle">
          <md-icon md-src="/static/svg/form.svg"></md-icon>
        </div>
        <div>
          <i class="el-icon-d-caret" @click="show"></i>
        </div>
      </div>
    </md-card-header> -->
		<div slot="header" class="clearfix" style="text-align: right"></div>
		<div class="show-form" id="show-form">
			<k-form ref="searchForm" :data-col="0" :data-label-width="dataLabelWidth">
				<slot></slot>
				<k-form-item>
					<k-btn class="btn-custom-primary" @click.native="query">
						<md-icon md-src="/static/svg/search.svg"></md-icon>查询
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="RESET" data-type="warning" data-from="searchForm" @click.native="handleReset">
						<md-icon md-src="/static/svg/reset.svg"></md-icon>重置
					</k-btn>
					<slot name="button"></slot>
				</k-form-item>
			</k-form>
			<!-- <div class="k-form-search-footer">
        <k-btn class="btn-custom-primary" @click.native="query">
          <md-icon md-src="/static/svg/search.svg"></md-icon>查询
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="RESET" data-type="warning" data-from="searchForm">
          <md-icon md-src="/static/svg/reset.svg"></md-icon>重置
        </k-btn>
        <slot  name="button"></slot>
      </div> -->
		</div>
	</md-card>
</template>

<script>
import emitter from "@/components/k-element/common/k-emitter.js";
import props from "@/components/k-element/common/k-field-props.js";

export default {
	name: "kFormSearchCustomize",
	mixins: [props(), emitter()],
	props: {
		dataTarget: {
			type: String,
		},
		flashingTarget: {
			type: String,
		},
		flashingData: {
			type: String,
		},
		handleConfirm: {
			type: Function
		}
	},
	data() {
		return {
			extends: true,
		};
	},
	computed: {
		iconStyle() {
			let iconStyle = {};
			iconStyle.background = this.$store.state.system.cardBackground;
			return iconStyle;
		},
	},
	created() {
		// window.addEventListener('keydown', this.handleKeyDown, true)//开启监听键盘按下事件
	},
	methods: {
		handleReset() {
			this.$emit("handleReset")
		},
		handleKeyDown(e) {
			let key = null;
			if (window.event === undefined) {
				key = e.keyCode;
			} else {
				key = window.event.keyCode;
			}
			if (key === 13) {
				this.query();
			}
		},
		query() {
			this.$emit("click");
			if (this.handleConfirm) {
				const r = this.handleConfirm();
				if (!r) {
					return;
				}
			}
			if (!this.dataTarget) {
				console.error("k-from-search需要指定data-target.");
			}
			let target = this.getParentRef(this.dataTarget);
			if (target && target.$options.name === "KGrid") {
				let params = {};

				for (let field in this.value) {
					let valueElement = this.value[field];
					if (valueElement) {
						params[field] = valueElement;
					}
				}

				let re = this.$refs.searchForm.validate();
				if (re === false) {
					return;
				}

				target.load(params);
			} else if (target && target.$options.name === "KReport") {
				let params = {};

				for (let field in this.value) {
					let valueElement = this.value[field];
					if (valueElement) {
						params[field] = valueElement;
					}
				}

				let re = this.$refs.searchForm.validate();
				if (re === false) {
					return;
				}

				this.$emit("loadDataForTable", params);
			} else {
				console.error("data-target不存在或data-target不是KGrid组件.");
			}
			this.$emit("dataLoadAfter");
			if (this.flashingTarget) {
				let flashingTarget = this.getParentRef(this.flashingTarget);
				if (this.flashingData) {
					let params = {};
					for (let flashingData in this.value) {
						let valueElement = this.flashingData[flashingData];
						if (valueElement && this.flashingData === flashingData) {
							params[flashingData] = valueElement;
						}
					}
					flashingTarget.load(params);
				}
			}
		},
		show() {
			let e = document.getElementById("show-form");
			if (this.extends) {
				e.style.display = "none";
			} else {
				e.style.display = "";
			}
			this.extends = !this.extends;
		},
	},
};
</script>

<style lang="scss">
@import "./k-form-search.scss";
</style>
