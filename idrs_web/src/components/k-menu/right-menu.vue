<template>
	<div class="right-menu" :class="{ hide: $sidebar.isMinimized }">
		<ul class="nav">
			<li
				class="nav-li"
				:class="{ 'nav-li-active': currentMenu2Index == index, 'nav-li-unactive': currentMenu2Index != index }"
				v-for="(m2, index) in menus2"
				:key="m2.menuid"
			>
				<component
					class="nav-link"
					:is="elementType(m2)"
					:title="m2.menuname"
					:class="{
						'nav-link-active': currentMenu2Index == index,
						'nav-li-unactive': currentMenu2Index != index,
						'route-active': isActive(m2),
						'children-route-active': isChildrenActive(m2),
					}"
					:to="'/main/' + m2.url + '?menuName=' + m2.menuname"
					:href="m2.url ? '#/main/' + m2.url : 'javascript:void(0);'"
					@click="clickMenu2Item(m2, index)"
				>
					<template>
						<!-- <md-icon :md-src="'../../static/svg/' + m2.icon + '.svg'"></md-icon> -->
						<span class="span-icon"></span>
						<span
							class="menuname"
							style="
								white-space: nowrap;
								overflow: hidden;
								text-overflow: ellipsis;
								width: 150px;
								display: block;
							"
							>{{ m2.menuname }}</span
						>
						<img
							v-show="m2.children && !m2.url && backgroundColor == 'black'"
							class="cert"
							src="../../assets/svg/k-menu/down.svg"
						/>
						<img
							v-show="m2.children && !m2.url && backgroundColor == 'red'"
							class="cert"
							src="../../assets/svg/k-menu/down.svg"
						/>
						<img
							v-show="m2.children && !m2.url && backgroundColor == 'white'"
							class="cert"
							src="../../assets/svg/k-menu/menu-down-black.svg"
						/>
					</template>
				</component>

				<div class="nav-li-menu">
					<ul class="nav-li-menu-nav" :style="setStyle(index, m2)">
						<li v-for="m3 in m2.children" :key="m3.menuid" class="nav-li-menu-nav-li" style="height: 45px">
							<component
								class="nav-li-menu-nav-link"
								:class="{ 'route-active': isActive(m3) }"
								:title="m3.menuname"
								:is="elementType(m3)"
								:to="'/main/' + m3.url + '?menuName=' + m3.menuname"
								:href="m3.url ? '#/main/' + m3.url + '?menuName=' + m3.menuname : 'javascript:void(0);'"
							>
								<template>
									<span
										class="menuname"
										style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis"
										>{{ m3.menuname }}</span
									>
									<img
										v-if="m3.children && !m3.url"
										class="cert"
										src="../../assets/svg/k-menu/down.svg"
									/>
								</template>
							</component>
						</li>
					</ul>
				</div>
			</li>
		</ul>
	</div>
</template>
<script>
export default {
	props: {
		menus2: {
			type: Array,
			default: () => {
				return [];
			},
		},
		currentMenu2Index: {
			type: Number,
		},
    backgroundColor: String
	},
	computed: {
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
	methods: {
    
		clickMenu2Item(m2,index) {
      this.$emit("clickMenu2Item", m2, index)
    },
		elementType(m2, isParent = true) {
			if (m2.url) {
				return "router-link";
			} else {
				return "a";
			}
		},
		//三级菜单是否被激活
		isActive(m3) {
			//之前用的startWith，导致路由前半部分一样的会被同时选中，因此改为==
			if (m3.url && this.$route.path == "/main/" + m3.url) {
				return true;
			} else {
				return false;
			}

			return false;
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
	},
 
};
</script>
<style lang="scss" scoped>
.span-icon {
	width: 20px;
}
</style>