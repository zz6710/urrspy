<template>
	<div>
		<div>
			<span>角色ID:{{ roleId }}</span>
			<span style="margin-left: 70px">角色名称:{{ roleName }}</span>
			<br />
		</div>
		<el-tree :data="data" show-checkbox node-key="id" ref="tree" highlight-current :props="setProps"> </el-tree>
		<k-btn class="btn-custom-primary" :data-handler="save" :disabled="showSubmitBtn === false">
			<i class="icon-confirm" v-if="showSubmitBtn" />
			<i v-if="!showSubmitBtn" class="el-icon-loading" />
			<span>确定</span>
		</k-btn>

		<k-btn class="btn-custom-plain" data-functype="CLOSE">
			<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
		>
	</div>
</template>

<script>
export default {
	methods: {
		save() {
			this.showSubmitBtn = false;
			const _this = this;
			let checkedNodes = _this.$refs.tree.getCheckedNodes(false, true);
			let params;
			params = { roleId: _this.roleId, id: "", type: "" };
			if (checkedNodes && checkedNodes.length > 0) {
				checkedNodes.forEach((n) => {
					params.id = params.id + n.id + ",";
					params.type = params.type + n.type + ",";
				});
			}

			_this.httpUtil
				.comnUpdate({
					action: "RoleAuthoritySave.save",
					params: params,
					mask: false,
				})
				.then((data) => {
					this.showSubmitBtn = true;
					_this.loadTree(1);
				})
				.catch((e) => {
					_this.loadTree(1);
				});
		},
		loadTree(v) {
			this.httpUtil
				.graphqlQurey({
					graphql:
						'{queryRoleAuthority(action:"find",id:"' +
						this.roleId +
						"\") {rows{id, parentId, type, name},results}}treeConfig{queryRoleAuthority:{diffcondition: 'id,parentId'}}",
					params: null,
				})
				.then((data) => {
					this.data = data["queryRoleAuthority"].rows;
					if (this.roleId != 0) {
						this.httpUtil
							.comnQuery({
								action: "RoleAuthority.findAlreadyOwned",
								params: { id: this.roleId },
							})
							.then((data) => {
								this.$refs.tree.setCheckedKeys(data.rows.map((r) => r.id));
							});
					}
					if (v == 1) {
						this.$parent.$parent.close();
					}
				})
				.catch((e) => {
					this.data = [];
				});
		},
	},
	watch: {
		roleId() {
			this.loadTree();
		},
	},
	props: {
		roleId: {
			type: String,
			default: "",
		},
		roleName: "",
	},
	data() {
		return {
			data: [],
			setProps: {
				label: "name",
				children: "children",
			},
			showSubmitBtn: true,
		};
	},
};
</script>
