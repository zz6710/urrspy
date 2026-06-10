export default {
  methods: {
    handleExport(action, name, fn) {
    debugger;
			let headers = ''
			this.tableData.tableHeader.forEach(item=>{
				headers += `${item.label}:${item.prop}:,`
			})
			let params = {
				"headers": headers,
				"action_params": JSON.stringify(this.actionParams),
				"action": action,
				"unToDict": null,
				"type": action ? 0 : 1,
			};
			this.httpUtil.download({
				url: "excel/download.json",
				params: params,
				callback: () => {
					setTimeout(()=>{
						fn && fn()
					}, 1000)
				}
			}, name)
		}
  }
}
