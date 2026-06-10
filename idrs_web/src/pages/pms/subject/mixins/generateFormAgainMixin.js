import GenerateFormAgainDialog from "@/pages/pms/subject/components/GenerateFormAgainDialog.vue";

export default {
	components: {
		GenerateFormAgainDialog,
	},
	methods: {
		handleTaskApp() {
			this.$refs.formAgainRef.popup();
		},
	},
};
