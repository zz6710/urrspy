export default [{
	path: "operation/form",
	name: "功能表单",
	meta:{
		notKeepAlive:false,
		componentName:"form"
	},
	component: () => import('@/pages/operation/form')
}, {
	path: "operation/params",
	name: "表单参数配置",
	meta: {
		notKeepAlive: true,
		componentName: "opFormParam"
	},
	component: () => import('@/pages/operation/params')
}, {
	path: "operation/busi",
	name: "功能配置",
	meta:{
		notKeepAlive:false,
		componentName:"business"
	},
	component: () => import('@/pages/operation/business')
}, {
	path: "operation/busi_form",
	name: "功能表单配置",
	meta: {
		notKeepAlive: true,
		componentName: "opBusiForm"
	},
	component: () => import('@/pages/operation/busi_form')
}, {
	path: "operation/form_sql",
	name: "表单sql配置",
	meta: {
		notKeepAlive: false,
		componentName: "opSqlConfig"
	},
	component: () => import('@/pages/operation/form_sql')
}, {
	path: "operation/flow/flow_design",
	name: "操作流程图配置",
	meta:{
		notKeepAlive:false,
		componentName:"flow_design"
	},
	component: () => import('@/pages/operation/flow/flow_design')
}, {
	path: "operation/flow/G6Editor",
	name: "操作流程图配置",
	meta: {
		notKeepAlive: true,
		componentName: "G6Editor"
	},
	component: () => import('@/pages/operation/flow/G6Editor')
}, {
	path: "operation/flow/flow_start",
	name: "流程发起",
	meta:{
		notKeepAlive:false,
		componentName:"flow_start"
	},
	component: () => import('@/pages/operation/flow/flow_start')
}, {
	path: "operation/flow/flow_todo",
	name: "流程待办",
	meta:{
		notKeepAlive:false,
		componentName:"flow_todo"
	},
	component: () => import('@/pages/operation/flow/flow_todo')
}, {
	path: "operation/flow/flow_history",
	name: "流程追踪",
	meta:{
		notKeepAlive:false,
		componentName:"flow_history"
	},
	component: () => import('@/pages/operation/flow/flow_history')
}, {
	path: "operation/flow/flow_detail",
	name: "流程详细",
	meta: {
		notKeepAlive: true,
		componentName: "flowDetail"
	},
	component: () => import('@/pages/operation/flow/flow_detail')
}, {
	path: "operation/flow/flow_history_detail",
	name: "流程追踪详情",
	meta: {
		notKeepAlive: true,
		componentName: "flowHistoryDetail"
	},
	component: () => import('@/pages/operation/flow/flow_history_detail')
}]
