<template>
	<div>
		<div class="panel-body" ref="addFormDiv">
			<k-form ref="addForm">
				<k-form-item label="上级菜单ID">
					<k-field-text v-model="formData.upperid" :data-allowblank="false" :data-disabled="dataType == 'update'" />
				</k-form-item>

				<k-form-item label="菜单名称">
					<k-field-text type="text" :data-allowblank="false" v-model="formData.menuname" />
				</k-form-item>

				<k-form-item label="菜单ID">
					<k-field-text
						style="width: 120px"
						type="text"
						name="menuid"
						:data-allowblank="false"
						v-model="formData.menuid"
						:data-disabled="dataType == 'update'"
					/>
					<div id="maxmenuid"></div>
				</k-form-item>
			</k-form>
		</div>
		<k-form-footer data-align="center">
			<k-btn
				class="btn-custom-primary"
				data-action=""
				:data-model="formData"
				data-target="tableGrid"
				data-from="addForm"
				data-descript="保存菜单信息"
				data-functype="SUBMIT"
        @click="submit_info"
			>
				<md-icon md-src="/static/svg/confirm.svg" />确定
			</k-btn>
			<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg" />取消 </k-btn>
		</k-form-footer>
	</div>
</template>
<script>
import Tools from "@/utils/tools";

export default {
	props: {
		dataData: {
			type: Object,
			default: () => {
				return {};
			},
		},
		dataType: String,
	},
	watch: {
		dataData: {
			handler(v) {
				if (v) {
					this.formData = v;
				}
			},
			immediate: true,
		},
	},
	data() {
		return {
			formData: {},
		};
	},
	  methods: {
    submit_info() {
      let upperid = this.formData.upperid;
      let menuname = this.formData.menuname;
      let menuid = this.formData.menuid;
      let type = this.dataType;

      this.httpUtil.ajax({
        url: 'server/form/RptApp/JmReport/saveMenuInfo.json',
        params: {
          upperid: upperid,
          menuname: menuname,
          menuid: menuid,
          type: type
        }
      }).then(res => {
        if(res.success) {
          Tools.alert(res.returndata.msg);
        }
      })
    },
  },
};
</script>
