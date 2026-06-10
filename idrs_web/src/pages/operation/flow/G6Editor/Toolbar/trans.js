import cloneDeep from "lodash/cloneDeep";

export default function(json) {
	if (!json) {
		return json;
	}
	let nodeNameMapping = {};
	json.nodes.forEach(node => {
		nodeNameMapping[node.id] = node.attrs["name"];
	});
	let cloneData = cloneDeep(json);
	let res = {};

	let nodesInfo = (res.nodesInfo = {});
	cloneData.nodes.forEach(node => {
		let type = node.attrs.type;
		delete node.attrs.type;
		if (type === "task"||type === "operation") {
			if (node.attrs["btns-checkedKeys"] && node.attrs["btns-checkedKeys"] instanceof Array) {
				node.attrs["btns-checkedKeys"] = node.attrs["btns-checkedKeys"].join(",");
			}

			if (node.attrs.updateEnvTask) {
				node.attrs.updateEnvTask = node.attrs.updateEnvTask;
			}

			if (node.attrs["rejectTaskNames"]) {
			  let checkedKeys = [];
			  let checkedValues = [];
        node.attrs["rejectTaskNames"].forEach(r=>{
          let s = r.split('&');
          checkedKeys.push(s[0]);
          checkedValues.push(s[1]);
        })
				node.attrs["rejectTaskNames-checkedKeys"] = checkedKeys.join(",");
				node.attrs["rejectTaskNames-checkedValues"] = checkedValues.join(",");
			}

			if (node.attrs["roles-checkedKeys"] && node.attrs["roles-checkedKeys"] instanceof Array) {
				node.attrs["roles-checkedKeys"] = node.attrs["roles-checkedKeys"].join(",");
			}

			if (node.attrs["actors-checkedKeys"] && node.attrs["actors-checkedKeys"] instanceof Array) {
				node.attrs["actors-checkedKeys"] = node.attrs["actors-checkedKeys"].join(",");
			}
		}
		nodesInfo[node.attrs.name] = {
			type: type,
			attrCache: node.attrs
		};
	});

	let connectionsInfo = (res.connectionsInfo = {});
	cloneData.edges.forEach(edge => {
		let name = edge.attrs.name;
		let connectionInfo = (connectionsInfo[name] = {});
		connectionInfo.from = nodeNameMapping[edge.source];
		connectionInfo.to = nodeNameMapping[edge.target];
		let attrCache = (connectionInfo.attrCache = {});
		attrCache.name = name;
		attrCache.displayName = edge.attrs.displayName;
		attrCache.envTransitionExpr = edge.attrs.envTransitionExpr;
	});

	return res;
}
