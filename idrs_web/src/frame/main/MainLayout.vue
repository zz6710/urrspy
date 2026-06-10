<template>
	<div class="wrapper app-wrapper" :class="[{ 'nav-open': $sidebar.showSidebar }]">
		<k-sider class="menu-container" :title="title" :menus="menus" @toggleMinimize="sideToggleMinimize()"> </k-sider>
		<div class="main-panel">
			<div class="main-panel-header">
				<top-tab :active-color="sidebarBackground"></top-tab>
				<top-navbar class="top-navbar" :subMenus="subMenus"></top-navbar>
			</div>

			<!-- 
      <fixed-plugin ref="fixedPlugin" :color.sync="sidebarBackground" :colorBg.sync="sidebarBackgroundColor" :sidebarMini.sync="sidebarMini"
        :sidebarImg.sync="sidebarImg" :image.sync="sidebarBackgroundImage">
      </fixed-plugin> -->

			<div class="k-work-content" :class="{ content: !$route.meta.hideContent }" @click="toggleSidebar">
				<!--        <zoom-center-transition :duration="200" mode="out-in">-->
				<!--&lt;!&ndash;          &lt;!&ndash; your content here &ndash;&gt;&ndash;&gt;-->
				<!--&lt;!&ndash;          <router-view></router-view>&ndash;&gt;-->
				<!--          <tab-components :subMenus="subMenus"></tab-components>-->
				<!--        </zoom-center-transition>-->

				<!--        axin 修改是否使用緩存  20220728 注釋三行代碼 ，新增5行代碼 -->
				<!--        <keep-alive :exclude="$store.state.system.exincludeList">-->
				<!--              <router-view/>-->
				<!--        </keep-alive>-->

				<!--        <keep-alive :exclude="$store.state.system.exincludeList">
          <router-view v-if="!$route.meta.notKeepAlive&&!$route.meta.isIframe">
          </router-view>
        </keep-alive>-->
				<!--        <template v-if="$route.meta.isIframe">-->
				<!--          <router-view v-show="$route.name=='报表'"/>-->
				<!--        </template>-->
				<keep-alive :exclude="$store.state.system.exincludeList.join(',')">
					<template v-if="!$route.meta.isIframe">
						<router-view />
					</template>
				</keep-alive>

				<!-- <div class="keep_alive_report">
					<div>
						<M87Report
							:key="item"
							v-for="item in reportArr"
							:ref="item"
							:style="{ position: $route.params.id == item ? 'relative' : 'absolute' }"
						></M87Report>
					</div>
				</div> -->
				<template v-for="item in $store.state.system.tab">
					<iframe
						v-if="item.meta && item.meta.isIframe"
						frameborder="0"
						:key="item.path"
						:src="getSrc(item)"
						v-show="item.path == $route.path"
					></iframe>
				</template>
			</div>
			<!-- <content-footer v-if="!$route.meta.hideFooter"></content-footer> -->
		</div>
	</div>
</template>
<script>
import M87Report from "@/pages/report/template/M87Report.vue";
/* eslint-disable no-new */
import PerfectScrollbar from "perfect-scrollbar";
import "perfect-scrollbar/css/perfect-scrollbar.css";
import TabComponents from "./TabComponents";

function hasElement(className) {
	return document.getElementsByClassName(className).length > 0;
}

function setScollerV() {
	var agent = navigator.userAgent.toLowerCase();

	var regStr_ie = /msie [\d.]+;/gi;
	var regStr_ff = /firefox\/[\d.]+/gi;
	var regStr_chrome = /chrome\/[\d.]+/gi;
	var regStr_saf = /safari\/[\d.]+/gi;

	//IE
	if (agent.indexOf("msie") > 0) {
		return 0.2;
	}

	//firefox
	if (agent.indexOf("firefox") > 0) {
		return 0.2;
	}

	//Chrome
	if (agent.indexOf("chrome") > 0) {
		return 0.08;
	}

	//Safari
	if (agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0) {
		return 0.2;
	}

	return 0.2;
}

function initScrollbar(className) {
	if (hasElement(className)) {
		new PerfectScrollbar(`.${className}`, {
			wheelSpeed: setScollerV(),
		});
	} else {
		// try to init it later in case this component is loaded async
		setTimeout(() => {
			initScrollbar(className);
		}, 100);
	}
}

function reinitScrollbar() {
	let docClasses = document.body.classList;
	let isWindows = navigator.platform.startsWith("Win");
	if (isWindows) {
		// if we are on windows OS we activate the perfectScrollbar function
		initScrollbar("sidebar");
		// initScrollbar("sidebar-wrapper");
		// initScrollbar("main-panel");

		docClasses.add("perfect-scrollbar-on");
	} else {
		docClasses.add("perfect-scrollbar-off");
	}
}

import TopNavbar from "./TopNavbar1.vue";
import ContentFooter from "./ContentFooter.vue";
import FixedPlugin from "./FixedPlugin.vue";
import ModuleNav from "./ModuleNav.vue";
import KSider from "../../components/k-menu/k-sider";
import TopTab from "./TopTab1";

export default {
	components: {
		TopNavbar,
		ContentFooter,
		FixedPlugin,
		ModuleNav,
		TabComponents,
		KSider,
		TopTab,
		M87Report,
	},
	data() {
		return {
			sidebarBackgroundColor: "black",
			sidebarBackground: "green",
			sidebarBackgroundImage: "./static/images/sidebar/sidebar-2.jpg",
			sidebarMini: true,
			sidebarImg: true,
			leftMenuLevel: "",
			menus: [],
			menus2: [],
			subMenus: [],
			title: localStorage.getItem("systemName"),
			reportArr: [],
			flag: false,
		};
	},
	created() {
		window.sessionStorage.removeItem("reportArr");
		this.loadMenu();
	},
	methods: {
		getSrc(v) {
			const { baseUrl } = getURL();
			return v.path.indexOf("editReport") > -1
				? baseUrl + "jmreport/index/" + v.query.id
				: baseUrl + "jmreport/view/" + v.params.id;
		},
		/** 加载菜单 */
		loadMenu() {
			//第一步查询系统参数-侧边栏菜单级别
			this.httpUtil
				.comnQuery({
					action: "SystemParam.find",
					params: {
						paraid: "10005",
					},
				})
				.then((res) => {
					this.leftMenuLevel = res.rows[0].paravalue;
					this.subMenus = [];
					this.httpUtil
						.query({
							url: "sys/findMenus.json",
						})
						.then((data) => {
							if (this.leftMenuLevel == "2") {
								this.menus2 = data.rows;
							} else {
								this.menus = data.rows;
								this.$store.commit("system/setLevel1Menu", this.menus);
							}
							this.setSubMenus(data.rows);
						});
				});
		},
		setSubMenus(menus) {
			menus.forEach((item) => {
				if (item.children && !item.url) {
					this.setSubMenus(item.children);
				} else {
					this.subMenus.push(item);
				}
			});
		},
		toggleSidebar() {
			if (this.$sidebar.showSidebar) {
				this.$sidebar.displaySidebar(false);
			}
		},
		minimizeSidebar() {
			if (this.$sidebar) {
				this.$sidebar.toggleMinimize();
			}
		},
		sideToggleMinimize() {
			this.$refs.fixedPlugin.updateToggleList();
		},
		clickModuleNav(menus) {
			this.menus2 = menus;
			this.menus2.map((item) => {
				item.level = 2;
			});
		},
	},
	updated() {
		reinitScrollbar();
	},
	mounted() {
		reinitScrollbar();
	},
	watch: {
		$route(data) {
			// if (JSON.parse(window.sessionStorage.getItem("reportArr"))) {
			// 	this.reportArr = JSON.parse(window.sessionStorage.getItem("reportArr"));
			// }
			// if (data.meta.isIframe) {
			// 	let tag = false;
			// 	this.reportArr.forEach((v, i) => {
			// 		if (v == this.$route.params.id) {
			// 			this.$refs[this.$route.params.id][0].$refs["report"].showHandler();
			// 			tag = true;
			// 		} else {
			// 			this.$refs[v][0].$refs["report"].hideHandler();
			// 		}
			// 	});
			// 	if (!tag) {
			// 		this.reportArr[this.reportArr.length] = this.$route.params.id;
			// 	}
			// } else {
			// 	this.reportArr.forEach((v, i) => {
			// 		this.$refs[v][0].$refs["report"].hideHandler();
			// 	});
			// }
			// window.sessionStorage.setItem("reportArr", JSON.stringify(this.reportArr));
		},
		sidebarMini() {
			let isMini = document.body.classList.value.indexOf("sidebar-mini") == -1;
			if (this.sidebarMini != isMini) {
				this.minimizeSidebar();
			}
		},
	},
};
</script>
<style lang="scss">
$scaleSize: 0.95;

@keyframes zoomIn95 {
	from {
		opacity: 0;
		transform: scale3d($scaleSize, $scaleSize, $scaleSize);
	}

	to {
		opacity: 1;
	}
}
.sidebar {
	z-index: 3001 !important;
}
.top-navbar {
	// height: 40px;
	display: flex;
	justify-content: center;
	align-items: center;
	.md-toolbar {
		padding: 0;
	}
}
.main-panel .zoomIn {
	animation-name: zoomIn95;
}

@keyframes zoomOut95 {
	from {
		opacity: 1;
	}

	to {
		opacity: 0;
		transform: scale3d($scaleSize, $scaleSize, $scaleSize);
	}
}

.main-panel .zoomOut {
	animation-name: zoomOut95;
}

.app-wrapper {
	overflow: hidden;
	display: flex;
}

.k-work-content {
	// max-height: calc(100vh - 100px) !important;
}

.top-navbar {
	position: relative;
	margin: 0 0 0 170px;
}

.main-panel-header {
	display: flex;
	background: #eff4fe;
	box-shadow: 0 0 16px #a3c2ff;
	padding-right: 20px;
}
iframe {
	width: 100%;
	height: 100% !important;
}
</style>
