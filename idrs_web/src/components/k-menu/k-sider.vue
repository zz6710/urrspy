<template>
	<div
		class="k-sidebar"
		:data-color="'blue'"
		:data-image="backgroundImage"
		data-background-color="blue"
		@mouseenter="onMouseenter"
		@mouseleave="onMouseleave"
	>
		<div class="logo">
			<div class="logo-body">
				<template v-if="!$sidebar.isMinimized">
					<div class="logo-img">
						<img src="../../assets/img/kkws_logo2.png" alt="" />
					</div>
					<div class="logo-right">
						<div class="r1"></div>
						<div class="r2"></div>
					</div>
				</template>
				<div class="logo-img" v-else>
					<img class="logo-icon-svg" src="../../assets/logo-icon.svg" alt="" />
				</div>
			</div>
			<div class="logo-footer">
				<div class="f1"></div>
				<div class="f2"></div>
				<div class="f3"></div>
			</div>
		</div>

		<div class="sidebar-wrapper" ref="sidebarScrollArea">
			<div class="left-menu">
				<div
					v-for="(m1, index) in menus"
					:key="index"
					class="item"
					:class="{ 'item-selected': currentMenu1Index == index }"
					@click="clickMenuItem(m1, index)"
				>
					<div class="item-content">
						<md-icon :md-src="'/static/svg/asideMenu/' + m1.icon + '.svg'"></md-icon>
						<span>{{ m1.shortname }}</span>
					</div>
					<div class="fg"></div>
				</div>
			</div>
			<RightMenu
				:menus2="menus2"
				:currentMenu2Index="currentMenu2Index"
				:class="{ 'hover-menu': hoverMenuShow, 'no-hover': showNoHover }"
				@clickMenu2Item="clickMenu2Item"
				:backgroundColor="backgroundColor"
			/>
		</div>
		<div class="sidebar-footer" @click="minimizeSidebar">
			<img
				class="switch-icon"
				src="/static/svg/asideMenu/arrow.svg"
				alt=""
				:class="{ open: $sidebar.isMinimized }"
			/>{{ $sidebar.isMinimized ? "" : " 收起" }}
		</div>
	</div>
</template>
<script>
import eventBus from "@/utils/eventBus";
import RightMenu from "@/components/k-menu/right-menu.vue";

export default {
	name: "k-sider",
	components: {
		RightMenu,
	},
	props: {
		title: {
			type: String,
		},
		activeColor: {
			type: String,
			default: "green",
			validator: (value) => {
				let acceptedValues = ["", "purple", "azure", "green", "orange", "danger", "rose"];
				return acceptedValues.indexOf(value) !== -1;
			},
		},
		backgroundImage: {
			type: String,
			default: "./img/sidebar-2.jpg",
		},
		backgroundColor: {
			type: String,
			default: "black",
			validator: (value) => {
				let acceptedValues = ["", "black", "white", "red"];
				return acceptedValues.indexOf(value) !== -1;
			},
		},
		logo: {
			type: String,
			default: "./../assets/img/menuLogo.png",
		},
		sidebarLinks: {
			type: Array,
			default: () => [],
		},
		autoClose: {
			type: Boolean,
			default: true,
		},
		menus: {
			type: Array,
			default: () => [],
		},
	},
	computed: {
		sidebarStyle() {
			return {
				backgroundImage: `url(${this.backgroundImage})`,
			};
		},
		setStyle() {
			return function (index, m2) {
				if (index == this.currentMenu2Index) {
					return {
						height: m2.children ? m2.children.length * 45 + m2.children.length * 5 - 10 + "px" : 0,
					};
				} else {
					return {
						height: 0,
					};
				}
			};
		},
	},
	provide() {
		return {
			autoClose: this.autoClose,
		};
	},
	data() {
		return {
			currentMenu1Index: -1,
			currentMenu2Index: -1,
			m1: {},
			menus2: [],
			hoverMenuShow: false,
			showNoHover: false,
		};
	},
	watch: {
		menus(newVal) {
			let a = false;
			newVal.forEach((item, index) => {
				if (item.menuname == localStorage.getItem("defaultLevel1Menu")) {
					this.clickMenuItem(item, index);
					a = true;
				}
			});
			if (!a) {
				this.clickMenuItem(newVal[0], 0);
			}
		},
		$route: {
			handler(v, o) {
				if (this.$sidebar.isMinimized) {
					if (v.path != o.path) {
						this.hoverMenuShow = false;
					}
				}
			},
		},
	},
	created() {},
	methods: {
		handleDocClick(e) {
			if (this.$sidebar.isMinimized) {
				if (!e.target.closest(".sidebar-wrapper")) {
					this.hoverMenuShow = false;
				}
			}
		},
		onMouseenter: function () {
			if (this.$sidebar.isMinimized) {
				console.log("鼠标进入");
				eventBus.$emit("updateReportPosition", true);
			}
		},
		onMouseleave: function () {
			if (this.$sidebar.isMinimized) {
				console.log("鼠标出去");
				eventBus.$emit("updateReportPosition", false);
			}
		},

		isChildrenActive(m2) {
			let a = false;
			if (m2.children) {
				m2.children.forEach((item) => {
					if (item.url && this.$route.path.startsWith("/main/" + item.url)) {
						a = true;
					}
				});
			}
			return a;
		},
		//三级菜单是否被激活
		isActive(m3) {
			//之前用的startWith，导致路由前半部分一样的会被同时选中，因此改为==
			if (m3.url && this.$route.path == "/main/" + m3.url) {
				return true;
			} else {
				return false;
			}
		},
		elementType(m2, isParent = true) {
			if (m2.url) {
				return "router-link";
			} else {
				return "a";
			}
		},
		clickMenuItem(m1, index) {
			if (this.currentMenu1Index != index) {
				this.currentMenu2Index = -1;
				this.currentMenu1Index = index;
				this.m1 = m1;
				this.menus2 = m1.children;
			}
			if (this.$sidebar.isMinimized) {
				this.hoverMenuShow = true;
			}
		},
		clickMenu2Item(m2, index) {
			this.currentMenu2Index = this.currentMenu2Index == index ? -1 : index;
		},
		minimizeSidebar() {
			if (this.$sidebar) {
				this.$sidebar.toggleMinimize();
				eventBus.$emit("updateReportPosition", null);
				if (!this.$sidebar.isMinimized) {
					this.hoverMenuShow = false;
					this.showNoHover = false;
				} else {
					setTimeout(() => {
						this.showNoHover = true;
					}, 200);
				}
			}
		},
	},
	beforeDestroy() {
		if (this.$sidebar.showSidebar) {
			this.$sidebar.showSidebar = false;
		}
	},
	mounted() {
		document.addEventListener("click", this.handleDocClick);
	},
	destroyed() {
		document.removeEventListener("click", this.handleDocClick);
	},
};
</script>
<style lang="scss" scoped>
.logo-normal2 {
	display: block;
	overflow: hidden;

	// transition: all 400ms ease;
}
.logo-body {
	display: flex;
	.logo-img {
		flex: 1;
		height: 80px;
		display: flex;
		align-items: center;
		justify-content: center;
		.logo-svg {
			width: 70%;
		}
		.logo-icon-svg {
			width: 70%;
			max-width: 45px;
		}
	}
	.logo-right {
		display: flex;
		& > div {
			width: 15px;
			height: 100%;
		}
		.r1 {
			background: #33a1db;
		}
		.r2 {
			background: #a7cbec;
		}
	}
}
.logo-footer {
	height: 10px;
	display: flex;
	& > div {
		flex: 1;
	}
	.f1 {
		background: #ac011d;
	}
	.f2 {
		background: #de1627;
	}
	.f3 {
		background: #eb8790;
	}
}
.switch-icon {
	&.open {
		transform: rotateY(180deg);
	}
}
</style>
