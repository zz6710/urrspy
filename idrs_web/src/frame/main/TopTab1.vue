<template>
	<div class="top-tab" id="top-tab" :data-color="activeColor">
		<div class="icon-arrow prev" @click="handlePrev">
			<i class="el-icon-d-arrow-left"></i>
		</div>
		<div class="tab" id="tab">
			<div class="tab-box" :style="{ transform: 'translateX(' + translateX + ')' }">
				<div
					class="item"
					:ref="'item' + index"
					:class="{ 'item-selected': item.active }"
					v-for="(item, index) in tab"
					:key="index"
					@click="clickTab(item)"
				>
					<span class="item-name"> {{ item.name }}</span>
					<div class="close" v-if="index != 0" @click.stop="removeTab(item, index)">×</div>
				</div>
			</div>
		</div>
		<div class="icon-arrow next" @click="handleNext">
			<i class="el-icon-d-arrow-right"></i>
		</div>
		<div class="down" id="down">
			<el-dropdown @command="handleClick" placement="bottom">
				<i class="el-icon-error"></i>
				<el-dropdown-menu slot="dropdown">
					<el-dropdown-item command="a">关闭当前页面</el-dropdown-item>
					<el-dropdown-item command="b">关闭全部页面</el-dropdown-item>
					<el-dropdown-item command="c">关闭其他页面</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</div>
	</div>
</template>

<script>
export default {
	name: "TopTab",
	props: {
		activeColor: {
			type: String,
			default: "green",
		},
	},
	data() {
		return {
			currentIndex: 0,
			showClose: false,
			currentTab: {},
			oldTab: {},
			showArrow: false,
			translateX: 0,
			arrowBtnWidth: 0,
		};
	},
	computed: {
		tab() {
			let tabs;
			let data = this.$store.state.system.tab;
			tabs = typeof data === "string" ? JSON.parse(data) : data;
			return tabs;
		},
	},
	watch: {
		tab(newVal) {
			// if(newVal) {
			// 	localStorage.setItem("openMenuList", JSON.stringify(newVal))
			// } else {
			// 	localStorage.setItem("openMenuList", JSON.stringify([]))
			// }
		},
		//监听路由的变化来实现添加或切换tab
		$route: {
			handler: function (to, oldTo) {
				this.$nextTick(() => {
					let flag = false;
					if (to.path) {
						for (let i = 0; i < this.tab.length; i++) {
							if (this.tab[i].path == to.path && to.path == "/main/desktop") {
								flag = true;
								this.$store.commit("system/setTabActive", { index: 0, active: true, query: to.query });
							} else {
								if (this.tab[i].path && this.tab[i].name === to.query.menuName) {
									flag = true;
									this.$store.commit("system/setTabActive", {
										index: i,
										active: true,
										query: to.query,
									});
								} else {
									this.$store.commit("system/setTabActive", { index: i, active: false });
								}
							}
						}

						if (!flag || to.path.indexOf("/main/desktop") > -1) {
							let data = {
								path: to.path,
								name: to.query.menuName || to.name,
								query: to.query,
								active: true,
								meta: to.meta,
								params: to.params,
							};
							if (to.meta.notKeepAlive && to.meta.notKeepAlive == true) {
								this.setExInclude(data, 1);
							} else {
								this.setExInclude(data, 2);
							}
							to.path.indexOf("/main/desktop") < 0 ? this.$store.commit("system/setTabPush", data) : "";
						}
						this.$nextTick(() => {
							this.setTopTab();
						});
					}
				});
			},
			immediate: true,
		},
	},
	mounted() {
		if (null != sessionStorage.getItem("kk-tab") && undefined != sessionStorage.getItem("kk-tab")) {
			this.$store.commit("system/setTab", JSON.parse(sessionStorage.getItem("kk-tab")));
		}
		this.setTopTab();
		window.onresize = () => {
			this.setTopTab();
		};
		window.addEventListener("beforeunload", () => {
			sessionStorage.setItem("kk-tab", JSON.stringify(this.tab));
		});
	},
	methods: {
		handleClick(v) {
			if (v == "a") {
				this.closeCurrentTab();
			} else if (v == "b") {
				this.closeAllTab();
			} else if (v == "c") {
				this.closeOtherTab();
			}
		},
		isActive(item) {
			if (item.path == this.$route.path && JSON.stringify(item.query) == JSON.stringify(this.$route.query)) {
				return true;
			} else {
				return false;
			}
		},
		clickTab(tab) {
			this.$router.push({
				path: tab.path,
				query: tab.query,
			});
		},
		closeCurrentTab() {
			if (this.tab.length == 1) {
				return;
			}

			this.tab.forEach((item, index) => {
				if (item.active) {
					this.removeTab(item, index);
				}
			});
		},
		closeAllTab() {
			if (this.tab.length == 1) {
				return;
			}

			this.tab.forEach((item, index) => {
				if (index != 0) {
					this.setExInclude(item, 1);
				}
			});

			let newArr = [];
			newArr.push(this.tab[0]);
			this.$store.commit("system/setTab", newArr);
			this.$router.push({
				path: this.tab[0].path,
				query: this.tab[0].query,
			});
			this.setTopTab();
		},
		closeOtherTab() {
			if (this.tab.length == 1) {
				return;
			}

			let activeIndex = -1;
			let istabIndex = true;
			this.tab.forEach((item, index) => {
				if (item.active) {
					activeIndex = index;
					istabIndex = true;
				}

				if (index != 0 || item.active != true) {
					this.setExInclude(item, 1);
				}
			});

			let newArr = [];
			newArr.push(this.tab[0]);
			if (istabIndex) {
				newArr.push(this.tab[activeIndex]);
			}
			this.$store.commit("system/setTab", newArr);
			this.setTopTab();
		},
		removeTab(tab, index) {
			this.setExInclude(tab, 1);
			this.$store.commit("system/setTabSplice", { index: index, count: 1 });
			if (tab.active) {
				this.$router.push({
					path: this.tab[index - 1].path,
					query: this.tab[index - 1].query,
				});
			}
			this.setTopTab();
		},
		setExInclude(tab, type) {
			if (tab.meta && tab.meta.componentName) {
				//设置为不缓存
				if (type == 1) {
					let e = this.$store.state.system.exincludeList;
					let a = false;
					e.every((item) => {
						if (item == tab.meta.componentName) {
							a = true;
							return false;
						} else {
							return true;
						}
					});
					if (!a) {
						e.push(tab.meta.componentName);
						this.$store.commit("system/setExincludeList", e);
					}
				} else {
					//设置为缓存
					let e = this.$store.state.system.exincludeList;
					let newArray = e.filter((item) => {
						return item != tab.meta.componentName;
					});
					this.$store.commit("system/setExincludeList", newArray);
				}
			}
		},
		handlePrev() {
			this.translateX = this.arrowBtnWidth + "px";
		},
		getScrollWidth() {
			const arr = document.getElementById("tab").getElementsByClassName("item");
			let w = 0;
			Array.from(arr).forEach((item) => {
				w += item.offsetWidth;
			});
			return w;
		},
		handleNext() {
			const tabElem = document.getElementById("tab");
			const { offsetWidth } = tabElem;
			const scrollWidth = this.getScrollWidth();
			if (scrollWidth > offsetWidth) {
				this.translateX = -(scrollWidth - offsetWidth + this.arrowBtnWidth) + "px";
			}
		},
		setTopTab() {
			setTimeout(() => {
				const tabElem = document.getElementById("tab");
				const { offsetWidth } = tabElem;
				const scrollWidth = this.getScrollWidth();
				const idx = this.tab.findIndex((item) => item.path == this.$route.path);
				const activeElem = this.$refs[`item${idx}`][0];
				const activeElemPos = activeElem.offsetLeft;
				const midPos = offsetWidth / 2;
				if (scrollWidth > offsetWidth) {
					this.showArrow = true;
					if (idx == this.tab.length - 1) {
						this.translateX = -(scrollWidth - offsetWidth + this.arrowBtnWidth) + "px";
					} else {
						if (activeElemPos > midPos + this.arrowBtnWidth) {
							const maxDis = scrollWidth - offsetWidth;
							const activeDis = activeElemPos - midPos;
							if (activeDis > maxDis) {
								this.translateX = -maxDis - this.arrowBtnWidth + "px";
							} else {
								this.translateX = -activeDis + "px";
							}
						} else {
							this.translateX = this.arrowBtnWidth + "px";
						}
					}
				} else {
					this.showArrow = false;
					this.translateX = 0;
				}
			}, 200);
		},
	},
};
</script>

<style lang="scss" scoped>
@import "TopTab";
</style>
