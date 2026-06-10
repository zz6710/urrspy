import cloneDeep from "lodash/cloneDeep";

export default function(json) {
	if (!json) {
		return json;
	}
	// let nodeNameMapping = {};
	// console.log(json.nodesInfo)

	// json.nodes.forEach(node => {
	// 	console.log(node)
	// 	nodeNameMapping[node.id] = node.attrs["name"];
	// });
	// json.nodesInfo.forEach(node => {
	// 	console.log(node)
	// })
	let cloneData = cloneDeep(json);
	let orignal = {
		name: '',
		displayName: '',
		nodes: [],
		edges: [],
		groups: []
	};

	let nodes = (orignal.nodes = []);
	let edges = (orignal.edges = []);

	orignal.name = cloneData.name
	orignal.displayName = cloneData.displayName

	for (const key in cloneData.nodesInfo) {
			let element = cloneData.nodesInfo[key];
			if (element.type) {
			if (element.type === 'startevent-none') {
				element.type = 'node'
				element.shape = 'customNode'
				element.color = '#1890ff'
				element.inPoints = [[0,0.5],[0.5,0]]
				element.outPoints = [[1,0.5],[0.5,1]]
				element.attrs = {type: 'startevent-none', name: 'start'}
				element.name = '开始节点'
				element.imageWidth = 20
				element.imageHeight = 20
				element.dragItemConfig = {width: 42, height: 42}
				element.offsetX = 36
				element.offsetY = 20
				element.size = [50,35]
				element.x = 13
				element.y = 33
				element.id = 'start'
			} else if (element.type === 'endevent-none') {
				element.type = 'node'
				element.shape = 'customNode'
				element.color = '#1890ff'
				element.inPoints = [[0,0.5],[0.5,0]]
				element.outPoints = [[1,0.5],[0.5,1]]
				element.attrs = {type: 'endevent-none', name: 'end'}
				element.name = '结束节点'
				element.imageWidth = 20
				element.imageHeight = 20
				element.dragItemConfig = {width: 42, height: 42}
				element.offsetX = 36
				element.offsetY = 20
				element.size = [50,35]
				element.x = 870
				element.y = 186
				element.id = 'end'
			} else if (element.type === 'task') {
				element.type = 'node'
				element.shape = 'customNode'
				element.color = '#1890ff'
				element.inPoints = [[0,0.5],[0.5,0]]
				element.outPoints = [[1,0.5],[0.5,1]]
				element.name = element.attrCache.name
				element.displayName = element.attrCache.displayName
				element.imageWidth = 20
				element.imageHeight = 20
				element.label = element.attrCache.displayName
				element.attrs = {type: 'task', name: element.attrCache.name, displayName: element.attrCache.displayName}
				element.dragItemConfig = {width: 80, height: 44}
				element.offsetX = 36
				element.offsetY = 26
				element.size = [120,50]
				element.x = 389
				element.y = 165
				element.id = element.attrCache.name
			} else if (element.type === 'gateway-exclusive') {
				element.type = 'node'
				element.name = '排他网关'
				element.shape = 'customNode'
				element.color = '#1890ff'
				element.inPoints = [[0,0.5],[0.5,0]]
				element.outPoints = [[1,0.5],[0.5,1]]
				element.imageWidth = 40
				element.imageHeight = 40
				element.attrs = {type: 'gateway-exclusive', name: element.attrCache.name, displayName: element.attrCache.displayName}
				element.dragItemConfig = {width: 48, height: 48}
				element.offsetX = 33
				element.offsetY = 30
				element.size = [70,50]
				element.x = 300
				element.y = 130
				element.id = element.attrCache.name
			} else if (element.type === 'gateway-fork') {
				element.type = 'node'
				element.name = '并行网关'
				element.shape = 'customNode'
				element.color = '#1890ff'
				element.inPoints = [[0,0.5],[0.5,0]]
				element.outPoints = [[1,0.5],[0.5,1]]
				element.imageWidth = 40
				element.imageHeight = 40
				element.attrs = {type: 'gateway-fork', name: element.attrCache.name, displayName: element.attrCache.displayName}
				element.dragItemConfig = {width: 48, height: 48}
				element.offsetX = 33
				element.offsetY = 30
				element.size = [70,50]
				element.x = 200
				element.y = 120
				element.id = element.attrCache.name
			} else if (element.type === 'gateway-join') {
				element.type = 'node'
				element.name = '合并网关'
				element.shape = 'customNode'
				element.color = '#1890ff'
				element.inPoints = [[0,0.5],[0.5,0]]
				element.outPoints = [[1,0.5],[0.5,1]]
				element.imageWidth = 40
				element.imageHeight = 40
				element.attrs = {type: 'gateway-join', name: element.attrCache.name, displayName: element.attrCache.displayName}
				element.dragItemConfig = {width: 48, height: 48}
				element.offsetX = 33
				element.offsetY = 30
				element.size = [70,50]
				element.x = 100
				element.y = 110
				element.id = element.attrCache.name
			}
			orignal.nodes.push(element)
		}
	}

	var id = 0;

	for (const key in cloneData.connectionsInfo) {

		let element = cloneData.connectionsInfo[key];
		element.source = element.from
		element.target = element.to
		element.sourceId = element.from
		element.targetId = element.to
		// element.shape = 'polyline'
		element.type = 'edge'
		element.labelConfig = {position: 'center', autoRotate: true, style: {stroke: 'white', lineWidth: 5, fill: '#722ed1'}}
		element.label = element.attrCache.displayName
		element.style = {
			"radius": 10,
			"offset": 30,
			"endArrow": true,
			"stroke": "#b8c3ce",
			"lineAppendWidth": 10,
			"lineWidth": 2
		}
		element.start = {x: 0, y: 25}
		element.end = {x: 0, y: -25}
		element.anchorPoints = [
			[
				0,
				0.5
			],
			[
				0.5,
				0
			],
			[
				1,
				0.5
			],
			[
				0.5,
				1
			]
		]
		element.startPoint = {
				"x": 127.89999389648438,
				"y": 188.84999084472656,
				"index": 1,
				"anchorIndex": 1,
				"id": "127.89999389648438-188.84999084472656" + id++
		}
		element.endPoint = {
				"x": 304.9796951741682,
				"y": 191.7376726644735,
				"index": 0,
				"anchorIndex": 0,
				"id": "304.9796951741682-191.7376726644735" + id++
		}
		element.attrs = element.attrCache
		element.attrs.type = 'edge'
		// eval("element.attrs." + "type" + "='" + "edge" + "'")
		// element.attrs['type'] = 'edge'
		// for(let p in element){
		// 	if( !( p == 'source' || p == "target" || p=='type' || p == 'style')){
		// 		delete element[p]
		// 	}
		// }


		orignal.edges.push(element)
	}
	// console.log(orignal)
	// cloneData.nodes.forEach(node => {
	// 	let type = node.attrs.type;
	// 	delete node.attrs.type;
	// 	if (type === "task") {
	// 		if (node.attrs["btns-checkedKeys"]) {
	// 			node.attrs["btns-checkedKeys"] = node.attrs["btns-checkedKeys"].join(",");
	// 		}

	// 		if (node.attrs["rejectTaskNames"]) {
	// 		  let checkedKeys = [];
	// 		  let checkedValues = [];
    //     node.attrs["rejectTaskNames"].forEach(r=>{
    //       let s = r.split('&');
    //       checkedKeys.push(s[0]);
    //       checkedValues.push(s[1]);
    //     })
	// 			node.attrs["rejectTaskNames-checkedKeys"] = checkedKeys.join(",");
	// 			node.attrs["rejectTaskNames-checkedValues"] = checkedValues.join(",");
	// 		}

	// 		if (node.attrs["roles-checkedKeys"]) {
	// 			node.attrs["roles-checkedKeys"] = node.attrs["roles-checkedKeys"].join(",");
	// 		}

	// 		if (node.attrs["actors-checkedKeys"]) {
	// 			node.attrs["actors-checkedKeys"] = node.attrs["actors-checkedKeys"].join(",");
	// 		}
	// 	}
	// 	nodesInfo[node.attrs.name] = {
	// 		type: type,
	// 		attrCache: node.attrs
	// 	};
	// });

	// let connectionsInfo = (res.connectionsInfo = {});
	// cloneData.edges.forEach(edge => {
	// 	let name = edge.id;
	// 	let connectionInfo = (connectionsInfo[name] = {});
	// 	connectionInfo.from = nodeNameMapping[edge.source];
	// 	connectionInfo.to = nodeNameMapping[edge.target];
	// 	let attrCache = (connectionInfo.attrCache = {});
	// 	attrCache.name = name;
	// 	attrCache.displayName = edge.attrs.displayName;
	// 	attrCache.envTransitionExpr = edge.attrs.envTransitionExpr;
	// });

	return orignal;
	// return res;
}
