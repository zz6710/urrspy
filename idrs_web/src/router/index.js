import NotFound from '@/frame/404.vue'
import Login from '@/frame/Login.vue'
import LoginAuth from '@/frame/LoginAuth.vue'
import Test from '@/frame/Test.vue'

import ResetPwd from '@/frame/ResetPwd.vue'
import MainLayout from '@/frame/main/MainLayout.vue'
import Healthy from '@/frame/healthy'
import EiamLogin from '@/frame/eiamLogin.vue'
import Privilege from '@/frame/privilege.vue'
import Vue from 'vue'
import Router from 'vue-router'
import operation from './router-operation'


const includPush = Router.prototype.push

Router.prototype.push = function push(location) {
  return includPush.call(this, location).catch(err => err)

}

Vue.use(Router)

export default new Router({
  routes: [{
    path: '/login',
    component: Login,
    name: '',
    hidden: true
  }, {
       path: '/LoginAuth',
       component: LoginAuth,
       name: '',
       hidden: true
     }, {
    path: '/test',
    component: Test,
    name: '',
    hidden: true
  }, {
    path: '/healthy',
    component: Healthy,
    name: '',
    hidden: true
  }, {
    path: '/eiamLogin',
    component: EiamLogin,
    name: '',
    hidden: true
  },
    {
      path: '/resetPwd',
      component: ResetPwd,
      name: '',
      hidden: true
    }, {
      path: '/404',
      component: NotFound,
      name: '',
      hidden: true
    }, {
      path: '/',
      hidden: true,
      redirect: {
        path: '/login'
      }
    }, {
      path: '*',
      hidden: true,
      redirect: {
        path: '/404'
      }
    }, {
      path: "/main",
      component: MainLayout,
      redirect: "/main/desktop",
      name: "desktop",
      children: [...operation, {
        path: "desktop",
        name: "首页",
        meta: {
          notKeepAlive: false,
          componentName: "desktop"
        },
        //component: () => import('@/pages/desktop.vue')
        component: () => import('@/pages/pms/workbench/personalWorkbench')
      },
        {
          path: "pms/workbench/cockpit/index",
          name: "运营情况统计分析",
          meta: {
            notKeepAlive: false,
            componentName: "cockpit"
          },
          component: () => import('@/pages/pms/workbench/cockpit/operation.vue')
        },
        {
          path: "system/org",
          name: "机构管理",
          meta: {
            notKeepAlive: false,
            componentName: "org"
          },
          component: () => import('@/pages/system/org.vue')
        },
        {
          path: "flow/gn",
          name: "功能流程设计",
          meta: {
            notKeepAlive: false,
            componentName: "FlowDesign2"
          },
          component: () => import('@/pages/flow/FlowDesign2')
        },
        {
          path: "system/dept",
          name: "部门管理",
          meta: {
            notKeepAlive: false,
            needAlive: false,
            componentName: "dept"
          },
          component: () => import('@/pages/system/dept.vue')
        },
        /*{
          path: "system/liable",
          name: "部门责任人管理",
          meta:{
            needAlive:true,
            componentName:"liable"
          },
          component: () => import('@/pages/system/liable.vue')
        },*/

        {
          path: "system/dict",
          name: "数据字典",
          meta: {
            notKeepAlive: false,
            componentName: "dict"
          },
          component: () => import('@/pages/system/dict.vue')
        },
        {
          path: "system/system-params",
          name: "系统参数",
          meta: {
            notKeepAlive: false,
            componentName: "business-params"
          },
          component: () => import('@/pages/system/system-params.vue')
        },
        {
          path: "system/workday/work-day",
          name: "工作日管理",
          meta: {
            notKeepAlive: false,
            componentName: "work-day"
          },
          component: () => import('@/pages/system/workday/work-day.vue')
        },
        {
          path: "system/user",
          name: "用户管理",
          meta: {
            notKeepAlive: false,
            componentName: "user"
          },
          component: () => import('@/pages/system/user.vue')
        },
        {
          path: "system/role/role",
          name: "角色管理",
          meta: {
            notKeepAlive: false,
            componentName: "role"
          },
          component: () => import('@/pages/system/role/role.vue')
        },
        {
          path: "system/announce",
          name: "公告管理",
          meta: {
            notKeepAlive: false,
            componentName: "announce"
          },
          component: () => import('@/pages/system/announce.vue')
        },
        {
          path: "system/op-log",
          name: "操作日志",
          meta: {
            notKeepAlive: false,
            componentName: "op-log"
          },
          component: () => import('@/pages/system/op-log.vue')
        },
      {
        path: "bak/bakConfig",
        name: "归档配置",
        meta:{
          notKeepAlive: false,
          componentName:"bakConfig"
        },
        component: () => import('@/pages/bak/bakConfig.vue')
      },
      {
        path: "bak/bakCollection",
        name: "归档仓库",
        meta:{
          notKeepAlive: false,
          componentName:"bakCollection"
        },
        component: () => import('@/pages/bak/bakCollection.vue')
      },
      {
        path: "bak/bakLog",
        name: "归档记录",
        meta:{
          notKeepAlive: false,
          componentName:"bakLog"
        },
        component: () => import('@/pages/bak/bakLog.vue')
      },

        {//产品信披渠道邮箱
          path: "pms/prod/custodianEmail/T8DisclosureNoticeChannel",
          name: "信披渠道发布记录",
          meta: {
            notKeepAlive: false,
            componentName: "T8DisclosureNoticeChannel"
          },
          component: () => import('@/pages/pms/prod/custodianEmail/T8DisclosureNoticeChannel.vue')
        },
        {
          path: "pms/M81/M81011",
          name: "产品从业人员信息",
          meta: {
            notKeepAlive: false,
            componentName: "M81011"
          },
          component: () => import('@/pages/pms/M81/M81011.vue')
        },
        {
          path: "pms/M81/prodInfoGD/M81001add",
          name: "产品参数维护",
          meta: {
            notKeepAlive: false,
            componentName: "prodInfoGDM81001add"
          },
          component: () => import('@/pages/pms/M81/prodInfoGD/M81001add.vue')
        },
        {
          path: "pms/M81/M81013",
          name: "产品系列配置",
          meta: {
            notKeepAlive: false,
            componentName: "M81013"
          },
          component: () => import('@/pages/pms/M81/M81013.vue')
        },
        {
          path: "pms/M81/prodShareBonus",
          name: "分红规则设置",
          meta: {
            notKeepAlive: false,
            componentName: "T8ProdShareBonus"
          },
          component: () => import('@/pages/pms/bonus/T8ProdShareBonus.vue')
        },
        {
          path: "pms/bonus/bonusTaskInfo",
          name: "分红提醒设置",
          meta: {
            notKeepAlive: false,
            componentName: "T8ProdBonusTask"
          },
          component: () => import('@/pages/pms/bonus/T8ProdBonusTask.vue')
        },
        // {
        //   path: "pms/M81/M81011add",
        //   name: "添加产品人员",
        //   component: () => import('@/pages/pms/M81/M81011add.vue')
        // },
        // {
        //   path: "pms/M81/M81011edit",
        //   name: "编辑产品人员",
        //   component: () => import('@/pages/pms/M81/M81011edit.vue')
        // },
        {
          path: "pms/M81/M85001",
          name: "产品模型配置",
          meta: {
            notKeepAlive: false,
            componentName: "M85001"
          },
          component: () => import('@/pages/pms/M81/M85001.vue')
        },
        {
          path: "pms/M81/M81014Add",
          name: "更新产品登记编码",
          meta: {
            notKeepAlive: false,
            componentName: "M81014Add"
          },
          component: () => import('@/pages/pms/M81/M81014Add.vue')
        },
        {
          path: "pms/M81/M81014",
          name: "申报登记结果维护",
          meta: {
            notKeepAlive: false,
            componentName: "M81014"
          },
          component: () => import('@/pages/pms/M81/M81014.vue')
        },
        {
          path: "pms/M81/M81007",
          name: "产品创设",
          meta: {
            notKeepAlive: false,
            componentName: "M81007"
          },
          component: () => import('@/pages/pms/M81/M81007.vue')
        },
        {
          path: "pms/M81/M81005",
          name: "货币分红配置",
          meta: {
            notKeepAlive: false,
            componentName: "M81005"
          },
          component: () => import('@/pages/pms/M81/M81005.vue')
        },
        {
          path: "pms/M81/M81004",
          name: "净值分红配置",
          meta: {
            notKeepAlive: false,
            componentName: "M81004"
          },
          component: () => import('@/pages/pms/M81/M81004.vue')
        },
        {
          path: "pms/M81/M81012",
          name: "产品用户组配置",
          meta: {
            notKeepAlive: false,
            componentName: "M81012"
          },
          component: () => import('@/pages/pms/M81/M81012.vue')
        },
        {
          path: "pms/M81/M81012add",
          name: "添加产品用户组",
          meta: {
            notKeepAlive: false,
            componentName: "M81012add"
          },
          component: () => import('@/pages/pms/M81/M81012add.vue')
        },
        {
          path: "pms/M81/M81012addPeo",
          name: "添加产品用户组人员",
          meta: {
            notKeepAlive: false,
            componentName: "M81012addPeo"
          },
          component: () => import('@/pages/pms/M81/M81012addPeo.vue')
        },
        {
          path: "pms/M81/M81012edit",
          name: "修改产品用户组",
          meta: {
            notKeepAlive: false,
            componentName: "M81012edit"
          },
          component: () => import('@/pages/pms/M81/M81012edit.vue')
        },
        {
          path: "pms/M81/M81012editPeo",
          name: "修改产品用户组人员",
          meta: {
            notKeepAlive: false,
            componentName: "M81012editPeo"
          },
          component: () => import('@/pages/pms/M81/M81012editPeo.vue')
        },
        {
          path: "pms/M82/M82001",
          name: "销售商信息管理",
          meta: {
            notKeepAlive: false,
            componentName: "M82001"
          },
          component: () => import('@/pages/pms/M82/M82001.vue')
        },
        {
          path: "pms/M82/M82001add",
          name: "销售商信息添加",
          meta: {
            notKeepAlive: false,
            componentName: "M82001add"
          },
          component: () => import('@/pages/pms/M82/M82001add')
        },
        {
          path: "pms/M84/M84010",
          name: "分红下发查询",
          meta: {
            notKeepAlive: false,
            componentName: "M84010"
          },
          component: () => import('@/pages/pms/M84/M84010.vue')
        },
        {
          path: "pms/M81/M81006",
          name: "工作日方案",
          meta: {
            notKeepAlive: false,
            componentName: "M81006"
          },
          component: () => import('@/pages/pms/M81/M81006.vue')
        },
        {
          path: "pms/M81/prodCalendar/prodOpenCalendar",
          name: "产品日历",
          meta: {
            notKeepAlive: false,
            componentName: "prodOpenCalendar"
          },
          component: () => import('@/pages/pms/M81/prodCalendar/prodOpenCalendar.vue')
        },
        {
          path: "pms/M85/M85002",
          name: "清算流程",
          meta: {
            notKeepAlive: false,
            componentName: "M85002"
          },
          component: () => import('@/pages/pms/M85/M85002.vue')
        },
        {
          path: "develop/backend-code",
          name: "后端代码生成",
          meta: {
            notKeepAlive: false,
            componentName: "backend-code"
          },
          component: () => import('@/pages/develop/backend-code.vue')
        },
        /*
        {
          path: "pms/printTemp/printTempInfo",
          name: "产品文档模板维护",
          meta: {
            notKeepAlive: false,
            componentName: "printTempInfo"
          },
          component: () => import('@/pages/pms/printTemp/printTempInfo.vue')
        },
        {
          path: "pms/printTemp/printTempData",
          name: "文档模板数据源管理",
          meta: {
            notKeepAlive: false,
            componentName: "printTempData"
          },
          component: () => import('@/pages/pms/printTemp/printTempData.vue')
        },
        {
          path: "pms/printTemp/staticTempInfo",
          name: "静态文档管理",
          meta: {
            notKeepAlive: false,
            componentName: "staticTempInfo"
          },
          component: () => import('@/pages/pms/printTemp/staticTempInfo.vue')
        },
*/
         {
           path: "test/Privilege",
           name: "权限测试",
           meta: {
             notKeepAlive: false,
             componentName: "privilege"
           },
           component: () => import('@/pages/privilege/privilege.vue')
         },{
          path: "pms/disclosureMsg/M8DisclosureRule",
          name: "产品信披规则管理",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosureRule"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureRule.vue')
        },
        {
          path: "pms/disclosureMsg/M8RuleAdmin",
          name: "信披规则管理",
          meta: {
            notKeepAlive: false,
            componentName: "M8RuleAdmin"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8RuleAdmin.vue')
        },
        {
          path: "pms/disclosureMsg/M8DisclosureProdGroup",
          name: "信披产品组维护",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosureProdGroup"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureProdGroup.vue')
        },
        // {
        //   path: "pms/disclosureMsg/M8DisclosureModel",
        //   name: "信披模板维护",
        //   component: () => import('@/pages/pms/disclosureMsg/M8DisclosureModel.vue')
        // },
        {
          path: "pms/disclosureMsg/M8DisclosurePubEmail",
          name: "渠道管理",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosurePubEmail"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosurePubEmail.vue')
        },
        {
          path: "pms/disclosureMsg/M8DisclosureRuleConsole",
          name: "信披规则控制台",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosureRuleConsole"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureRuleConsole.vue')
        },
        {
          path: "pms/disclosureMsg/M8DisclosureRuleReport",
          name: "信披台账管理",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosureRuleReport"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureRuleReport.vue')
        },
        {
          path: "pms/disclosureMsg/M8DisclosureTask",
          name: "信披待办任务",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosureTask"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureTask.vue')
        },
        /*      {
                path: "pms/disclosureMsg/M8DisclosureRuleNotice",
                name: "信披公告管理",
                component: () => import('@/pages/pms/disclosureMsg/M8DisclosureRuleNotice.vue')
              },*/
        {
          path: "pms/disclosureMsg/M8DisclosureData",
          name: "信披数据管理",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosureData"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureData.vue')
        },
        {
          path: "pms/basePublish/fieldsDispose",
          name: "信披字段配置",
          meta: {
            notKeepAlive: false,
            componentName: "FieldsDispose"
          },
          component: () => import('@/pages/pms/basePublish/fieldsDispose.vue')
        }
        ,
        {
          path: "pms/basePublish/printTemp",
          name: "信披模板配置",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosureModel"
          },
          component: () => import('@/pages/pms/basePublish/M8DisclosureModel.vue')
        },
        {
          path: "pms/basePublish/M8DisclosureModelVersion",
          name: "信披模板文档版本管理",
          meta: {
            notKeepAlive: false,
            needAlive: false,
            componentName: "M8DisclosureModelVersion"
          },
          component: () => import('@/pages/pms/basePublish/M8DisclosureModelVersion.vue')
        },
        {
          path: "pms/basePublish/supplementaryRecord",
          name: "通用补录界面",
          meta: {
            notKeepAlive: false,
            componentName: "SupplementaryRecord"
          },
          component: () => import('@/pages/pms/basePublish/SupplementaryRecord.vue')
        },

        {
          path: "pms/basePublish/ProdRegular",
          name: "报告数据补录",
          meta: {
            notKeepAlive: false,
            componentName: "ProdRegular"
          },
          component: () => import('@/pages/pms/basePublish/prodRegular/ProdRegular.vue')
        },
        {
          path: "pms/disclosureNotice/DisclosureNotice",
          name: "信披公告管理",
          meta: {
            notKeepAlive: false,
            componentName: "DisclosureNotice"
          },
          component: () => import('@/pages/pms/disclosureNotice/DisclosureNotice.vue')
        },
        {
          path: "pms/disclosureNotice/M8DisclosureManualNotice",
          name: "手工公告管理",
          meta:{
            notKeepAlive:false,
            componentName:"M8DisclosureManualNotice"
          },
          component: () => import('@/pages/pms/disclosureNotice/M8DisclosureManualNotice.vue')
        },
        {
          path: "pms/disclosureMsg/M8DisclosureManual",
          name: "新增手工公告",
          meta: {
            notKeepAlive: false,
            componentName: "M8DisclosureManual"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureManual.vue')
        },
        {//手工公告
          path: "pms/disclosureMsg/M8DisclosureManualDisplay",
          name: "公告详情",
          meta:{
            notKeepAlive:false,
            componentName:"M8DisclosureManualDisplay"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureManualDisplay.vue')
        },
        {//手工公告修改
          path: "pms/disclosureMsg/M8DisclosureManualEdit",
          name: "公告详情",
          meta:{
            notKeepAlive:false,
            componentName:"M8DisclosureManualEdit"
          },
          component: () => import('@/pages/pms/disclosureMsg/M8DisclosureManualEdit.vue')
        },
        {
          path: "pms/disclosureNotice/DisclosureNoticeDetail",
          name: "公告详情",
          meta: {
            notKeepAlive: false,
            componentName: "DisclosureNoticeDetail"
          },
          component: () => import('@/pages/pms/disclosureNotice/DisclosureNoticeDetail.vue')
        },


        {
          path: "pms/basePublish/channelRule",
          name: "信披渠道配置",
          meta: {
            notKeepAlive: false,
            componentName: "ChannelRuleList"
          },
          component: () => import('@/pages/pms/basePublish/DisclosureChannelRule/ChannelRuleList')
        },
        {
          path: "pms/basePublish/disclosureRule",
          name: "信披生成规则配置",
          meta: {
            notKeepAlive: false,
            componentName: "DisclosureRuleList"
          },
          component: () => import('@/pages/pms/basePublish/DisclosureRule/DisclosureRuleList')
        },
        {
          path: "pms/disclosureControl/prodDisclosureRule",
          name: "产品信披规则",
          meta: {
            notKeepAlive: false,
            componentName: "DisclosureProdRuleList"
          },
          component: () => import('@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleList')
        },
        {
          path: "pms/disclosureControl/regularDisProdConfirm",
          name: "定期报告台账",
          meta: {
            notKeepAlive: false,
            componentName: "RegularDisProdConfirmList"
          },
          component: () => import('@/pages/pms/disclosureControl/regularDisProdConfirm/RegularDisProdConfirmList')
        },
        {
          path: "pms/disclosureControl/prodDisclosureTask",
          name: "信披任务管理",
          meta: {
            notKeepAlive: false,
            componentName: "ProdDisclosureTaskList"
          },
          component: () => import('@/pages/pms/disclosureControl/prodDisclosureTask/ProdDisclosureTaskList')
        },
        {
          path: "pms/disclosureControl/quartz",
          name: "批量调度控制台",
          meta: {
            notKeepAlive: false,
            componentName: "QuartzInfoList"
          },
          component: () => import('@/pages/pms/disclosureControl/quartz/QuartzInfoList')
        },
        {
          path: "pms/disclosureNotice/DisclosureNoticeVersion",
          name: "信披公告版本管理",
          meta: {
            notKeepAlive: false,
            componentName: "NoticeVersion"
          },
          component: () => import('@/pages/pms/disclosureNotice/DisclosureNoticeVersion')
        },
        {
          path: "pms/netValue/T8ProdNetValueTask",
          name: "净值披露任务管理",
          meta: {
            notKeepAlive: false,
            componentName: "T8ProdNetValueTask"
          },
          component: () => import('@/pages/pms/netValue/task/T8ProdNetValueTask')
        },
        {
          path: "pms/netValue/T8ProdNetValueTaskNotice",
          name: "产品净值信息",
          meta: {
            notKeepAlive: false,
            componentName: "T8ProdNetValueTaskNotice"
          },
          component: () => import('@/pages/pms/netValue/task/T8ProdNetValueTaskNotice.vue')
        },
        {
          path: "pms/basePublish/t8OdsInterManage",
          name: "接口信息管理",
          meta: {
            notKeepAlive: false,
            componentName: "interfaceManagement"
          },
          component: () => import('@/pages/pms/basePublish/t8OdsInterManage/T8interfaceManage.vue')
        },
        {
          path: "pms/basePublish/t8OdsTbSet",
          name: "接口信息管理",
          meta: {
            notKeepAlive: false,
            componentName: "interfaceManagement"
          },
          component: () => import('@/pages/pms/basePublish/t8OdsTbSet/T8OdsTbSet.vue')
        },
        {
          path: "pms/basePublish/portFieldManage",
          name: "接口文件字段管理",
          meta: {
            notKeepAlive: false,
            componentName: "PortFieldManageInfo"
          },
          component: () => import('@/pages/pms/basePublish/PortFieldManage/PortFieldManageInfo.vue')
        },
        {
          path: "pms/t8ParamInterConfig/T8ParamConfigModel",
          name: "接口参数配置",
          meta: {
            notKeepAlive: false,
            componentName: "T8ParamConfigModel"
          },
          component: () => import('@/pages/pms/basePublish/t8ParamInterConfig/T8ParamConfigModel.vue')
        },
        {
          path: "pms/t8SqlInterConfig/T8SQLConfigModel",
          name: "接口SQL配置",
          meta: {
            notKeepAlive: false,
            componentName: "T8SQLConfigModel"
          },
          component: () => import('@/pages/pms/basePublish/t8SqlInterConfig/T8SQLConfigModel.vue')
        },
        {
          path: "pms/t8SqlInterConfig/T8SqlParamInfo",
          name: "接口SQL参数配置",
          meta: {
            notKeepAlive: false,
            componentName: "T8SqlParamInfo"
          },
          component: () => import('@/pages/pms/basePublish/t8SqlInterConfig/T8SqlParamInfo.vue')
        },
        {
          path: "pms/basePublish/t8PortLogManage",
          name: "接口日志管理",
          meta: {
            notKeepAlive: false,
            componentName: "InterfaceLogInfo"
          },
          component: () => import('@/pages/pms/basePublish/t8PortLogManage/interfaceLogManage.vue')
        },
        {
          path: "reportSend/dataHandle/reportDataValRes",
          name: "报送数据校验",
          meta: {
            notKeepAlive: false,
            componentName: "reportDataValRes"
          },
          component: () => import('@/pages/reportSend/dataHandle/reportDataValRes.vue')
        },
        {
          path: "reportSend/maintain/reportMenuMaintain",
          name: "报表菜单维护",
          meta: {
            notKeepAlive: false,
            componentName: "reportMenuMaintain"
          },
          component: () => import('@/pages/reportSend/maintain/reportMenuMaintain.vue')
        },
        {
          path: "reportSend/config/reportIndexConfig",
          name: "校验指标配置",
          meta: {
            notKeepAlive: false,
            componentName: "reportIndexConfig"
          },
          component: () => import('@/pages/reportSend/config/reportIndexConfig.vue')
        },
        {
          path: "reportSend/config/reportTimingConfig",
          name: "报送时点配置",
          meta: {
            notKeepAlive: false,
            componentName: "reportTimingConfig"
          },
          component: () => import('@/pages/reportSend/config/reportTimingConfig.vue')
        },
        {
          path: "reportSend/config/ReportDataLockConfig",
          name: "报送任务管理",
          meta: {
            notKeepAlive: false,
            componentName: "ReportDataLockConfig"
          },
          component: () => import('@/pages/reportSend/config/ReportDataLockConfig.vue')
        },
        {
          path: "pms/bond/BondInfoModel",
          name: "债券信息",
          meta: {
            notKeepAlive: false,
            componentName: "BondInfoModel"
          },
          component: () => import('@/pages/pms/asset/bond/BondInfoModel.vue')
        },
        {
          path: "pms/directFusionTool/MidDirectFusion",
          name: "理财直融工具信息",
          meta: {
            notKeepAlive: false,
            componentName: "MidDirectFusion"
          },
          component: () => import('@/pages/pms/asset/directFusionTool/MidDirectFusion.vue')
        },
        {
          path: "pms/fund/FundInfoModel",
          name: "公募基金信息",
          meta: {
            notKeepAlive: false,
            componentName: "FundInfoModel"
          },
          component: () => import('@/pages/pms/asset/fund/FundInfoModel.vue')
        },
        {
          path: "pms/fund/FundPrivateInfoModel",
          name: "私募基金信息",
          meta: {
            notKeepAlive: false,
            componentName: "FundPrivateInfoModel"
          },
          component: () => import('@/pages/pms/asset/fund/FundPrivateInfoModel.vue')
        },
        {
          path: "pms/netvalSPV/NetValSPVInfoModel",
          name: "净值SPV信息",
          meta: {
            notKeepAlive: false,
            componentName: "NetValSPVInfoModel"
          },
          component: () => import('@/pages/pms/asset/netvalSPV/NetValSPVInfoModel.vue')
        },
        {
          path: "pms/nonStand/NonStandInfoModel",
          name: "非标债权信息",
          meta: {
            notKeepAlive: false,
            componentName: "NonStandInfoModel"
          },
          component: () => import('@/pages/pms/asset/nonStand/NonStandInfoModel.vue')
        }, {
          path: "report/develop/edit/ReportEdit",
          name: "report报表编辑",
          meta: {
            notKeepAlive: false,
            componentName: "ReportEdit"
          },
          component: () => import('@/pages/report/develop/edit/ReportEdit.vue')
        }, {
          path: "report/develop/edit/TreeListEdit",
          name: "TreeList报表标记",
          meta: {
            notKeepAlive: false,
            componentName: "TreeListEdit"
          },
          component: () => import('@/pages/report/develop/edit/TreeListEdit.vue')
        }, {
          path: "report/develop/ReportCondition",
          name: "报表开发",
          meta: {
            notKeepAlive: false,
            componentName: "reportCondition"
          },
          component: () => import('@/pages/report/develop/ReportCondition.vue')
        }, {
          path: "report/reportTemplate/reportCenter",
          name: "报表工作台",
          meta: {
            notKeepAlive: false,
            componentName: "reportCenter"
          },
          component: () => import('@/pages/report/reportTemplate/reportCenter.vue')
        }, {
          path: "report/develop/ReportData",
          name: "数据源编辑",
          meta: {
            notKeepAlive: false,
            componentName: "reportData"
          },
          component: () => import('@/pages/report/develop/ReportData.vue')
        }, {
          path: "report/develop/ReportQuery",
          name: "条件编辑",
          meta: {
            notKeepAlive: false,
            componentName: "ReportQuery"
          },
          component: () => import('@/pages/report/develop/ReportQuery.vue')
        },
        {
          path: "pms/prodWroth/T8ProdWorth",
          name: "产品净值信息",
          meta: {
            notKeepAlive: false,
            componentName: "T8ProdWorth",
          },
          component: () => import('@/pages/pms/prodWroth/T8ProdWorth.vue')
        },
        {
          path: "pms/t8AffiliatedPerson/T8AffiliatedPerson",
          name: "关联法人信息",
          meta: {
            notKeepAlive: false,
            componentName: "T8AffiliatedPerson",
          },
          component: () => import('@/pages/pms/t8AffiliatedPerson/T8AffiliatedPerson.vue')
        },
        {
          path: "pms/t8OrgSheet/T8OrgSheet",
          name: "机构信息",
          meta: {
            notKeepAlive: false,
            componentName: "T8OrgSheet",
          },
          component: () => import('@/pages/pms/t8OrgSheet/T8OrgSheet.vue')
        },
        {
          path: "pms/trustee/trustee",
          name: "托管行信息",
          meta: {
            notKeepAlive: false,
            componentName: "trustee",
          },
          component: () => import('@/pages/pms/trustee/trustee.vue')
        },
        {
          path: "pms/asset/right/assetRight",
          name: "股权信息",
          meta: {
            notKeepAlive: false,
            componentName: "assetRight",
          },
          component: () => import('@/pages/pms/asset/right/assetRight.vue')
        },
        {
          path: "batch/TA5010",
          name: "清算组件管理",
          nmeta: {
            notKeepAlive: false,
            componentName: "TA5010",
          },
          component: () => import('@/pages/batch/TA5010.vue')
        }, {
          path: "batch/TA5014",
          name: "清算流程",
          meta: {
            notKeepAlive: false,
            componentName: "TA5014"
          },
          component: () => import('@/pages/batch/TA5014.vue')
        }, {
          path: "batch/TA5015Edit",
          name: "清算流程配置",
          meta: {
            notKeepAlive: false,
            componentName: "TA5015Edit",
          },
          component: () => import('@/pages/batch/TA5015Edit.vue')
        }, {
          path: "batch/TA5016Edit",
          name: "实时清算流程配置",
          meta: {
            notKeepAlive: false,
            componentName: "TA5016Edit",
          },
          component: () => import('@/pages/batch/TA5016Edit.vue')
        }, {
          path: "batch/TA5004",
          name: "清算日志",
          meta: {
            notKeepAlive: false,
            componentName: "TA5004",
          },
          component: () => import('@/pages/batch/TA5004.vue')
        }, {
          path: "report/tree/:id",
          name: "报表",
          meta: {
            notKeepAlive: false,
            componentName: "M87Tree",
          },
          component: () => import('@/pages/report/template/M87Tree.vue')
        },
        {
          path: "report/:id",
          name: "报表",
          meta: {
            notKeepAlive: false,
            componentName: "",
            projectName: true,
            isIframe: true,
          },
          component: () => import('@/pages/report/template/M87Report.vue')
        },
        {
          path: "report/rhzj/M07RHZJ01",
          name: "报表",
          meta: {
            notKeepAlive: false,
            componentName: "M07RHZJ01",
            projectName: true
          },
          component: () => import('@/pages/report/rhzj/M07RHZJ01.vue')
        },
        {
          path: "report/rhzj/M07RHZJ02",
          name: "报表",
          meta: {
            notKeepAlive: false,
            componentName: "M07RHZJ02",
            projectName: true
          },
          component: () => import('@/pages/report/rhzj/M07RHZJ02.vue')
        },
        {
          path: "report/rhzj/M07RHZJ03",
          name: "报表",
          meta: {
            notKeepAlive: false,
            componentName: "M07RHZJ03",
            projectName: true
          },
          component: () => import('@/pages/report/rhzj/M07RHZJ03.vue')
        },
        {
          path: "exceimport/ImportInfo",
          name: "估值表信息解析",
          meta: {
            notKeepAlive: false,
            componentName: "ImportInfo",
          },
          component: () => import('@/pages/exceimport/ImportInfo.vue')
        }, {
          path: "exceimport/ImportConfig",
          name: "估值表信息配置",
          meta: {
            notKeepAlive: false,
            componentName: "ImportConfig",
          },
          component: () => import('@/pages/exceimport/ImportConfig.vue')
        },
        {
          path: "exceimport/AppBottomDeal",
          name: "底层资产管理",
          meta: {
            notKeepAlive: false,
            componentName: "AppBottomDeal",
          },
          component: () => import('@/pages/exceimport/AppBottomDeal.vue')
        },
        {
          path: "zz/errorInfo/PractyRegistInfoErr",
          name: '从业人员登记错误',
          meta: {
            notKeepAlive: false,
            componentName: "PractyRegistInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/PractyRegistInfoErr.vue')
        },
        {
          path: "zz/errorInfo/AssetDebtRegisterInfoErr",
          name: '资产要素登记错误',
          meta: {
            notKeepAlive: false,
            componentName: "AssetDebtRegisterInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/AssetDebtRegisterInfoErr.vue')
        },
        {
          path: "zz/errorInfo/ProdRgFlInfoErr",
          name: '产品报告登记错误',
          meta: {
            notKeepAlive: false,
            componentName: "ProdRgFlInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/ProdRgFlInfoErr.vue')
        },
        {
          path: "zz/errorInfo/ProdIssRgInfoErr",
          name: '产品发行登记错误',
          meta: {
            notKeepAlive: false,
            componentName: "ProdIssRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/ProdIssRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/InitialSubRgInfoErr",
          name: '产品募集总量错误',
          meta: {
            notKeepAlive: false,
            componentName: "InitialSubRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/InitialSubRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/SeqScrRgInfoErr",
          name: '产品存续期错误',
          meta: {
            notKeepAlive: false,
            componentName: "SeqScrRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/SeqScrRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/ProdTransRgInfoErr",
          name: '交易信息错误',
          meta: {
            notKeepAlive: false,
            componentName: "ProdTransRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/ProdTransRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/AppraiseRgInfoErr",
          name: '估值信息错误',
          meta: {
            notKeepAlive: false,
            componentName: "AppraiseRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/AppraiseRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/UnderAssetRgInfoErr",
          name: '底层资产错误',
          meta: {
            notKeepAlive: false,
            componentName: "UnderAssetRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/UnderAssetRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/AssetRgInfoErr",
          name: '资产持仓错误',
          meta: {
            notKeepAlive: false,
            componentName: "AssetRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/AssetRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/ProdStateRgInfoErr",
          name: '产品状态错误',
          meta: {
            notKeepAlive: false,
            componentName: "ProdStateRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/ProdStateRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/AppNavInfoRegErr",
          name: '净值信息登记错误信息',
          meta: {
            notKeepAlive: false,
            componentName: "AppNavInfoRegErr",
          },
          component: () => import('@/pages/zz/errorInfo/AppNavInfoRegErr.vue')
        },
        {
          path: "zz/errorInfo/TerminationRgInfoErr",
          name: '产品终止登记错误',
          meta: {
            notKeepAlive: false,
            componentName: "TerminationRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/TerminationRgInfoErr.vue')
        },
        {
          path: "zz/errorInfo/CustRegisterInfoErr",
          name: '投资者登记错误',
          meta: {
            notKeepAlive: false,
            componentName: "CustRegisterInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/CustRegisterInfoErr.vue')
        },
        {
          path: "zz/errorInfo/CustTransInfoErr",
          name: '投资者明细错误',
          meta: {
            notKeepAlive: false,
            componentName: "CustTransInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/CustTransInfoErr.vue')
        },
        {
          path: "zz/errorInfo/CustVolRgInfoErr",
          name: '投资者持有错误',
          meta: {
            notKeepAlive: false,
            componentName: "CustVolRgInfoErr",
          },
          component: () => import('@/pages/zz/errorInfo/CustVolRgInfoErr.vue')
        },
        {
          path: "zz/historyInfo/AppraiseRegistInfoh",
          name: '估值信息历史',
          meta: {
            notKeepAlive: false,
            componentName: "AppraiseRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/AppraiseRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/AssetDebtRegisterInfoh",
          name: '资产负债历史',
          meta: {
            notKeepAlive: false,
            componentName: "AssetDebtRegisterInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/AssetDebtRegisterInfoh.vue')
        },
        {
          path: "zz/historyInfo/CustRegisterInfoh",
          name: '投资者登记历史',
          meta: {
            notKeepAlive: false,
            componentName: "CustRegisterInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/CustRegisterInfoh.vue')
        },
        {
          path: "zz/historyInfo/CustTransInfoh",
          name: '投资者明细历史',
          meta: {
            notKeepAlive: false,
            componentName: "CustTransInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/CustTransInfoh.vue')
        },
        {
          path: "zz/historyInfo/CustVolRegisterInfoh",
          name: '投资者持有历史',
          meta: {
            notKeepAlive: false,
            componentName: "CustVolRegisterInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/CustVolRegisterInfoh.vue')
        },
        {
          path: "zz/historyInfo/InitialSubRegistInfoh",
          name: '产品募集总量历史',
          meta: {
            notKeepAlive: false,
            componentName: "InitialSubRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/InitialSubRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/PractyRegistInfoh",
          name: '从业人员登记历史',
          meta: {
            notKeepAlive: false,
            componentName: "PractyRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/PractyRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/ProdRegistFilingInfoh",
          name: '产品报告登记历史',
          meta: {
            notKeepAlive: false,
            componentName: "ProdRegistFilingInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/ProdRegistFilingInfoh.vue')
        },
        {
          path: "zz/historyInfo/ProdTransRegistInfoh",
          name: '交易信息历史',
          meta: {
            notKeepAlive: false,
            componentName: "ProdTransRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/ProdTransRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/SubseqSubscrRegistInfoh",
          name: '产品存续期历史',
          meta: {
            notKeepAlive: false,
            componentName: "SubseqSubscrRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/SubseqSubscrRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/TerminationRegistInfoh",
          name: '产品终止登记历史',
          meta: {
            notKeepAlive: false,
            componentName: "TerminationRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/TerminationRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/UnderAssetRegistInfoh",
          name: '底层资产历史',
          meta: {
            notKeepAlive: false,
            componentName: "UnderAssetRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/UnderAssetRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/AssetRegistInfoh",
          name: '资产持仓历史',
          meta: {
            notKeepAlive: false,
            componentName: "AssetRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/AssetRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/ProdStateRegistInfoh",
          name: '状态登记历史',
          meta: {
            notKeepAlive: false,
            componentName: "ProdStateRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/ProdStateRegistInfoh.vue')
        },
        {
          path: "zz/historyInfo/AppNavInfoRegh",
          name: '净值信息登记历史信息',
          meta: {
            notKeepAlive: false,
            componentName: "AppNavInfoRegh",
          },
          component: () => import('@/pages/zz/historyInfo/AppNavInfoRegh.vue')
        },
        {
          path: "zz/historyInfo/ProdIssuanceRegistInfoh",
          name: '产品发行登记历史',
          meta: {
            notKeepAlive: false,
            componentName: "ProdIssuanceRegistInfoh",
          },
          component: () => import('@/pages/zz/historyInfo/ProdIssuanceRegistInfoh.vue')
        },
        {
          path: "zz/manage/ProdRegistFilingInfo",
          name: '产品申报登记信息管理',
          meta: {
            notKeepAlive: false,
            componentName: "ProdRegistFilingInfo",
          },
          component: () => import('@/pages/zz/manage/ProdRegistFilingInfo.vue')
        },
        {
          path: "zz/manage/ProdIssuanceRegistInfo",
          name: '产品发行登记信息管理',
          meta: {
            notKeepAlive: false,
            componentName: "ProdIssuanceRegistInfo",
          },
          component: () => import('@/pages/zz/manage/ProdIssuanceRegistInfo.vue')
        },
        {
          path: "zz/manage/InitialSubRegistInfo",
          name: '募集总量登记管理',
          meta: {
            notKeepAlive: false,
            componentName: "InitialSubRegistInfo",
          },
          component: () => import('@/pages/zz/manage/InitialSubRegistInfo.vue')
        },
        {
          path: "zz/manage/SubseqSubscrRegistInfo",
          name: '产品存续期登记管理',
          meta: {
            notKeepAlive: false,
            componentName: "SubseqSubscrRegistInfo",
          },
          component: () => import('@/pages/zz/manage/SubseqSubscrRegistInfo.vue')
        },
        {
          path: "zz/manage/AssetDebtRegisterInfo",
          name: '资产要素登记管理',
          meta: {
            notKeepAlive: false,
            componentName: "AssetDebtRegisterInfo",
          },
          component: () => import('@/pages/zz/manage/AssetDebtRegisterInfo.vue')
        },
        {
          path: "zz/manage/ProdTransRegistInfo",
          name: '交易信息登记管理',
          meta: {
            notKeepAlive: false,
            componentName: "ProdTransRegistInfo",
          },
          component: () => import('@/pages/zz/manage/ProdTransRegistInfo.vue')
        },
        {
          path: "zz/manage/AppraiseRegistInfo",
          name: '估值信息登记管理',
          meta: {
            notKeepAlive: false,
            componentName: "AppraiseRegistInfo",
          },
          component: () => import('@/pages/zz/manage/AppraiseRegistInfo.vue')
        },
        {
          path: "zz/manage/UnderAssetRegistInfo",
          name: '底层资产持仓管理',
          meta: {
            notKeepAlive: false,
            componentName: "UnderAssetRegistInfo",
          },
          component: () => import('@/pages/zz/manage/UnderAssetRegistInfo.vue')
        },
        {
          path: "zz/manage/UnderAssetRegistInfo_day",
          name: '底层资产持仓管理（日表）',
          meta: {
            notKeepAlive: false,
            componentName: "UnderAssetRegistInfo",
          },
          component: () => import('@/pages/zz/manage/UnderAssetRegistInfo_day.vue')
        },
        {
          path: "zz/manage/AssetRegistInfo",
          name: '资产持仓管理',
          meta: {
            notKeepAlive: false,
            componentName: "AssetRegistInfo",
          },
          component: () => import('@/pages/zz/manage/AssetRegistInfo.vue')
        },
        {
          path: "zz/manage/AssetRegistInfo_day",
          name: '资产持仓管理（日表）',
          meta: {
            notKeepAlive: false,
            componentName: "AssetRegistInfo",
          },
          component: () => import('@/pages/zz/manage/AssetRegistInfo_day.vue')
        },
        {
          path: "zz/manage/ProdStateRegistInfo",
          name: '产品状态管理',
          meta: {
            notKeepAlive: false,
            componentName: "ProdStateRegistInfo",
          },
          component: () => import('@/pages/zz/manage/ProdStateRegistInfo.vue')
        },
        {
          path: "pms/collection/AssetCollection",
          name: "资产补录页面",
          meta: {
            notKeepAlive: false,
            componentName: "AssetCollection"
          },
          component: () => import('@/pages/pms/asset/collection/AssetCollection.vue')
        },
        {
          path: "zz/operateInfo/practyRegist",
          name: '从业人员登记信息操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "practyRegist",
          },
          component: () => import('@/pages/zz/operateInfo/PractyRegist.vue')
        },
        {
          path: "zz/operateInfo/terminationRegist",
          name: '产品终止登记操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "terminationRegist",
          },
          component: () => import('@/pages/zz/operateInfo/TerminationRegist.vue')
        },
        {
          path: "zz/operateInfo/custRegistMark",
          name: '投资者身份登记操作记录查询',
          meta: {
            notKeepAlive: false,
            componentName: "custRegistMark",
          },
          component: () => import('@/pages/zz/operateInfo/CustRegistMark.vue')
        },
        {
          path: "zz/operateInfo/custVolRegister",
          name: '投资者持有信息登记操作',
          meta: {
            notKeepAlive: false,
            componentName: "custVolRegister",
          },
          component: () => import('@/pages/zz/operateInfo/CustVolRegister.vue')
        },
        {
          path: "zz/operateInfo/investorHoldMark",
          name: '投资者持有信息(子产品)操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "investorHoldMark",
          },
          component: () => import('@/pages/zz/operateInfo/InvestorHoldMark.vue')
        },
        {
          path: "zz/operateInfo/custTransMark",
          name: '投资者明细信息操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "custTransMark",
          },
          component: () => import('@/pages/zz/operateInfo/CustTransMark.vue')
        },
        {
          path: "zz/operateInfo/prodRegistFiling",
          name: '产品申报登记信息操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "prodRegistFiling",
          },
          component: () => import('@/pages/zz/operateInfo/ProdRegistFiling.vue')
        },
        {
          path: "zz/operateInfo/prodIssuanceRegist",
          name: '产品发行登记信息操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "prodIssuanceRegist",
          },
          component: () => import('@/pages/zz/operateInfo/ProdIssuanceRegist.vue')
        },
        {
          path: "zz/operateInfo/initialSubRegist",
          name: '募集总量登记操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "initialSubRegist",
          },
          component: () => import('@/pages/zz/operateInfo/InitialSubRegist.vue')
        },
        {
          path: "zz/operateInfo/subseqSubscrRegist",
          name: '产品存续期登记操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "subseqSubscrRegist",
          },
          component: () => import('@/pages/zz/operateInfo/SubseqSubscrRegist.vue')
        },
        {
          path: "zz/operateInfo/assetDebtRegist",
          name: '资产要素登记操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "assetDebtRegist",
          },
          component: () => import('@/pages/zz/operateInfo/AssetDebtRegist.vue')
        },
        {
          path: "zz/operateInfo/prodTransRegist",
          name: '交易信息登记操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "prodTransRegist",
          },
          component: () => import('@/pages/zz/operateInfo/ProdTransRegist.vue')
        },
        {
          path: "zz/operateInfo/appraiseRegist",
          name: '估值信息登记操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "appraiseRegist",
          },
          component: () => import('@/pages/zz/operateInfo/AppraiseRegist.vue')
        },
        {
          path: "zz/operateInfo/underAssetRegist",
          name: '底层资产持仓操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "underAssetRegist",
          },
          component: () => import('@/pages/zz/operateInfo/UnderAssetRegist.vue')
        },
        {
          path: "zz/operateInfo/assetRegist",
          name: '资产持仓操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "assetRegist",
          },
          component: () => import('@/pages/zz/operateInfo/AssetRegist.vue')
        },
        {
          path: "zz/operateInfo/ProdStateRegist",
          name: '产品状态操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "ProdStateRegist",
          },
          component: () => import('@/pages/zz/operateInfo/ProdStateRegist.vue')
        },
        {
          path: "zz/operateInfo/AppNavReg",
          name: '产品净值登记操作记录',
          meta: {
            notKeepAlive: false,
            componentName: "AppNavReg",
          },
          component: () => import('@/pages/zz/operateInfo/AppNavReg.vue')
        },

        {
          path: "chinaBondSubmit/reportingOperation/clearStep",
          name: '清算日志查询',
          meta: {
            notKeepAlive: false,
            componentName: "clearStep",
          },
          component: () => import('@/pages/chinaBondSubmit/reportingOperation/clearStep.vue')
        },

        {
          path: "chinaBondSubmit/reportingOperation/TrClearLog",
          name: '清算日志查询',
          meta: {
            notKeepAlive: false,
            componentName: "TrClearLog",
          },
          component: () => import('@/pages/chinaBondSubmit/reportingOperation/TrClearLog.vue')
        },
        {
          path: "chinaBondSubmit/reportingOperation/TrFileresults",
          name: '理财中心反馈信息查询',
          meta: {
            notKeepAlive: false,
            componentName: "TrFileresults",
          },
          component: () => import('@/pages/chinaBondSubmit/reportingOperation/TrFileresults.vue')
        },
        {
          path: "chinaBondSubmit/reportingOperation/ReportDataAudit",
          name: '报送数据复核管理',
          meta: {
            notKeepAlive: false,
            componentName: "ReportDataAudit",
          },
          component: () => import('@/pages/chinaBondSubmit/reportingOperation/ReportDataAudit.vue')
        },
        {
          path: "chinaBondSubmit/reportingOperation/ZzCodeApplyHistory",
          name: '理财产品审阅结果查询',
          meta: {
            notKeepAlive: false,
            componentName: "ZzCodeApplyHistory",
          },
          component: () => import('@/pages/chinaBondSubmit/reportingOperation/ZzCodeApplyHistory.vue')
        },
        {
          path: "pms/basePublish/t8OdsInterManage/BaseExSeat",
          name: '报送接口字段配置',
          meta: {
            notKeepAlive: false,
            componentName: "BaseExSeatModel",
          },
          component: () => import('@/pages/pms/basePublish/t8OdsInterManage/BaseExSeat.vue')
         },
        {
          path: "chinaBondSubmit/reportingOperation/DataClearManage",
          name: '报备运营管理',
          meta: {
            notKeepAlive: false,
            componentName: "DataClearManage",
          },
          component: () => import('@/pages/chinaBondSubmit/reportingOperation/DataClearManage.vue')
        },
        {
          path: "chinaBondSubmit/reportingOperation/DataReportManage",
          name: '理财中心文件查询',
          meta: {
            notKeepAlive: false,
            componentName: "DataReportManage",
          },
          component: () => import('@/pages/chinaBondSubmit/reportingOperation/DataReportManage.vue')
        },

        {
          path: "zz/manage/TrPractyRegistInfo",
          name: '从业人员登记信息管理',
          meta: {
            notKeepAlive: false,
            componentName: "TrPractyRegistInfo",
          },
          component: () => import('@/pages/zz/manage/TrPractyRegistInfo.vue')
        },
        {
          path: "zz/manage/TrCustRegisterInfo",
          name: '投资者身份信息管理',
          meta: {
            notKeepAlive: false,
            componentName: "TrCustRegisterInfo",
          },
          component: () => import('@/pages/zz/manage/TrCustRegisterInfo.vue')
        },
        {
          path: "zz/manage/TrCustTransInfo",
          name: '投资者明细信息管理',
          meta: {
            notKeepAlive: false,
            componentName: "TrCustTransInfo",
          },
          component: () => import('@/pages/zz/manage/TrCustTransInfo.vue')
        },
        {
          path: "zz/manage/ProdConsignmentSales",
          name: '委托销售产品编码导入',
          meta: {
            notKeepAlive: false,
            componentName: "ProdConsignmentSales",
          },
          component: () => import('@/pages/zz/manage/ProdConsignmentSales.vue')
        },
        {
          path: "zz/manage/ProdConsignmentSalesStop",
          name: '委托销售产品编码导入',
          meta: {
            notKeepAlive: false,
            componentName: "ProdConsignmentSalesStop",
          },
          component: () => import('@/pages/zz/manage/ProdConsignmentSalesStop.vue')
        },
        {
          path: "zz/manage/BaseReportExportLog",
          name: '报表导出审批',
          meta: {
            notKeepAlive: false,
            componentName: "BaseReportExportLog",
          },
          component: () => import('@/pages/zz/manage/BaseReportExportLog.vue')
        },
        {
          path: "zz/manage/TrCustVolRegisterInfo",
          name: '投资者持有信息管理',
          meta: {
            notKeepAlive: false,
            componentName: "TrCustVolRegisterInfo",
          },
          component: () => import('@/pages/zz/manage/TrCustVolRegisterInfo.vue')
        },
        {
          path: "zz/manage/ProdRegistRelationInfo",
          name: '产品登记编码管理',
          meta: {
            notKeepAlive: false,
            componentName: "ProdRegistRelationInfo",
          },
          component: () => import('@/pages/zz/manage/ProdRegistRelationInfo.vue')
        },
        {
          path: "zz/manage/TrTerminationRegistInfo",
          name: '产品终止登记管理',
          meta: {
            notKeepAlive: false,
            componentName: "TrTerminationRegistInfo",
          },
          component: () => import('@/pages/zz/manage/TrTerminationRegistInfo.vue')
        },
        {
          path: "zz/manage/AppSonShareInfoReg",
          name: '子份额信息登记',
          meta: {
            notKeepAlive: false,
            componentName: "AppSonShareInfoReg",
          },
          component: () => import('@/pages/zz/manage/AppSonShareInfoReg.vue')
        },
        {
          path: "zz/manage/AppSonShareDelReg",
          name: '子份额登记删除',
          meta: {
            notKeepAlive: false,
            componentName: "AppSonShareDelReg",
          },
          component: () => import('@/pages/zz/manage/AppSonShareDelReg.vue')
        },
        {
          path: "zz/manage/AppNavInfoReg",
          name: '净值信息登记',
          meta: {
            notKeepAlive: false,
            componentName: "AppNavInfoReg",
          },
          component: () => import('@/pages/zz/manage/AppNavInfoReg.vue')
        },
        {
          path: "pms/chinaDebtValuation/ChinaDebtValuation",
          name: "中债登估值",
          meta: {
            notKeepAlive: false,
            componentName: "ChinaDebtValuation",
          },
          component: () => import('@/pages/pms/chinaDebtValuation/ChinaDebtValuation.vue')
        },
        {
          path: "pms/securitiesValuationInformation/SecuritiesValuationInformation",
          name: "中证估值",
          meta: {
            notKeepAlive: false,
            componentName: "SecuritiesValuationInformation",
          },
          component: () => import('@/pages/pms/securitiesValuationInformation/SecuritiesValuationInformation.vue')
        },
        {
          path: "pms/valution/FundNavInfoModel",
          name: "基金估值",
          meta: {
            notKeepAlive: false,
            componentName: "FundNavInfoModel",
          },
          component: () => import('@/pages/pms/valuation/FundNavInfoModel.vue')

        },
        {
          path: "pms/valution/AssetManagePlanInfo",
          name: "净值SPV估值信息",
          meta: {
            notKeepAlive: false,
            componentName: "AssetManagePlanInfo",
          },
          component: () => import('@/pages/pms/valuation/AssetManagePlanInfo.vue')

        },
        {
          path: "pms/downLoad/DownLoadFile",
          name: "下载数据",
          meta: {
            notKeepAlive: false,
            componentName: "DownLoadFile",
          },
          component: () => import('@/pages/pms/download/DownLoadFile.vue')

        },

        {
          path: "pms/counterParty/CounterParty",
          name: "交易对手",
          meta: {
            notKeepAlive: false,
            componentName: "CounterPartyModel",
          },
          component: () => import('@/pages/pms/counterParty/CounterParty.vue')

      },
      {
        path: "pms/dataSupplement/DataSupplement",
        name: "人行2-1表内数据补录",
        meta: {
          notKeepAlive: false,
          componentName: "DataSupplement",
        },
        component: () => import('@/pages/pms/dataSupplement/DataSupplement.vue')
      },

        {
          path: "pms/baseExMap/BaseExMap",
          name: "字典映射配置",
          meta: {
            notKeepAlive: false,
            componentName: "BaseExMapModel",
          },
          component: () => import('@/pages/pms/baseExMap/BaseExMap.vue')

        },

        {
          path: "report/rhzg/M07RHZG01",
          name: "资管产品基本信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG01",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG01.vue')
        },

        {
          path: "report/rhzg/M07RHZG02",
          name: "资管产品初始募集信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG02",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG02.vue')
        },

        {
          path: "report/rhzg/M07RHZG03",
          name: "资管产品终止信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG03",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG03.vue')
        },

        {
          path: "report/rhzg/M07RHZG04",
          name: "资管产品存续期募集信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG04",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG04.vue')
        },

        {
          path: "report/rhzg/M07RHZG05",
          name: "资管产品资产负债信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG05",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG05.vue')
        },

        {
          path: "report/rhzg/M07RHZG06",
          name: "资产收益权明细信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG06",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG06.vue')
        },

        {
          path: "report/rhzg/M07RHZG07",
          name: "除回购和拆借外贷款明细信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG07",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG07.vue')
        },

        {
          path: "report/rhzg/M07RHZG08",
          name: "特定目的载体交易对手明细信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG08",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG08.vue')
        },

        {
          path: "report/rhzg/M07RHZG09",
          name: "资产负债剩余期限信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG09",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG09.vue')
        },

        {
          path: "report/rhzg/M07RHZG10",
          name: "债券等资产配置情况信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG10",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG10.vue')
        },

        {
          path: "report/rhzg/M07RHZG11",
          name: "企业债券分行业和企业规模情况信息",
          meta: {
            KeepAlive: false,
            componentName: "M07RHZG11",
            projectName: true
          },
          component: () => import('@/pages/report/rhzg/M07RHZG11.vue')
        },
        {
                  path: "report/rhzg/M07RHZG12",
                  name: "除资产收益权外其他债权信息",
                  meta: {
                    KeepAlive: false,
                    componentName: "M07RHZG12",
                    projectName: true
                  },
                  component: () => import('@/pages/report/rhzg/M07RHZG12.vue')
         },
         {
                   path: "report/rhzg/M07RHZG13",
                   name: "其他股权明细信息",
                   meta: {
                     KeepAlive: false,
                     componentName: "M07RHZG13",
                     projectName: true
                   },
                   component: () => import('@/pages/report/rhzg/M07RHZG13.vue')
         },
        {
          path: "report/rhlc/SaleMonInvest",
          name: "销售月度统计-分投资者类型",
          meta: {
            KeepAlive: false,
            componentName: "SaleMonInvest",
          },
          component: () => import('@/pages/report/rhlc/SaleMonInvest.vue')
        },
        {
          path: "report/rhlc/SaleMonRegion",
          name: "销售月度统计-分投地区类型",
          meta: {
            KeepAlive: false,
            componentName: "SaleMonRegion",
          },
          component: () => import('@/pages/report/rhlc/SaleMonRegion.vue')
        },
        {
          path: "report/rhlc/SaleMonChannel",
          name: "销售月度统计-分销售渠道",
          meta: {
            KeepAlive: false,
            componentName: "SaleMonChannel",
          },
          component: () => import('@/pages/report/rhlc/SaleMonChannel.vue')
        },
        {
          path: "report/rhlc/AppOverseasInvInfo",
          name: "资产管理机构境外投资情况表",
          meta: {
            notKeepAlive: false,
            componentName: "AppOverseasInvInfo",
          },
          component: () => import('@/pages/report/rhlc/AppOverseasInvInfo/index.vue')
        },

        {
          path: "pms/outLands/outLandsRaise",
          name: "境外募集余额",
          meta: {
            notKeepAlive: false,
            componentName: "outLandsRaise",
          },
          component: () => import('@/pages/pms/outLands/outLandsRaise.vue')
        },
        {
          path: "pms/outLands/outLandsCash",
          name: "境外募集及兑付发生额",
          meta: {
            notKeepAlive: false,
            componentName: "outLandsCash",
          },
          component: () => import('@/pages/pms/outLands/outLandsCash.vue')
        },
      {
        path: "pms/stockQuotationInformation/StockNavInfoModel",
        name: "股票行情信息",
        meta: {
          notKeepAlive: false,
          componentName: "StockNavInfoModel",
        },
        component: () => import('@/pages/pms/stockQuotationInformation/StockNavInfoModel.vue')

      },
      {
        path: "pms/disclosureData/GridFbassetHoldFrontten",
        name: "前十项资产数据",
        meta: {
          notKeepAlive: false,
          componentName: "GridFbassetHoldFrontten"
        },
        component: () => import('@/pages/pms/disclosureData/GridFbassetHoldFrontten.vue')
      },
      {
        path: "pms/disclosureData/GridFbassetHoldAnalysis",
        name: "非标资产持仓情况",
        meta: {
          notKeepAlive: false,
          componentName: "GridFbassetHoldAnalysis"
        },
        component: () => import('@/pages/pms/disclosureData/GridFbassetHoldAnalysis.vue')
      },
      {
        path: "pms/disclosureData/GridCsmBndInvRltPty",
        name: "关联方发行及承销的证券",
        meta: {
          notKeepAlive: false,
          componentName: "GridCsmBndInvRltPty"
        },
        component: () => import('@/pages/pms/disclosureData/GridCsmBndInvRltPty.vue')
      },
      {
        path: "pms/disclosureData/GridRltPtyOsd",
        name: "交易对手为关联方数据",
        meta: {
          notKeepAlive: false,
          componentName: "GridRltPtyOsd"
        },
        component: () => import('@/pages/pms/disclosureData/GridRltPtyOsd.vue')
      },
      {
        path: "pms/disclosureData/GridAffiliateOtherAnalysis",
        name: "其他交易关联方数据",
        meta: {
          notKeepAlive: false,
          componentName: "GridAffiliateOtherAnalysis"
        },
        component: () => import('@/pages/pms/disclosureData/GridAffiliateOtherAnalysis.vue')
      },
      {
        path: "pms/disclosureData/GridCombineRiskAnalysis",
        name: "投资组合资产配置情况及流动性风险分析数据",
        meta: {
          notKeepAlive: false,
          componentName: "GridAffiliateOtherAnalysis"
        },
        component: () => import('@/pages/pms/disclosureData/GridCombineRiskAnalysis.vue')
      },
      {
        path: "pms/disclosureData/GridAffiliateFeePay",
        name: "支付关联方费用数据",
        meta: {
          notKeepAlive: false,
          componentName: "GridAffiliateFeePay"
        },
        component: () => import('@/pages/pms/disclosureData/GridAffiliateFeePay.vue')
      },
        {
          path: "pms/disclosureNotice/DisclosureChangeNoticeStatus",
          name: "信披状态变更记录",
          meta: {
            notKeepAlive: false,
            componentName: "disclosureChangeNoticeStatus"
          },
          component: () => import('@/pages/pms/disclosureNotice/DisclosureChangeNoticeStatus')
        },
        {
          path: "flow/flowProcessConfiguration",
          name: "审批流配置",
          meta: {
            notKeepAlive: false,
            componentName: "flowProcessConfiguration"
          },
          component: () => import('@/pages/flow/flowProcessConfiguration.vue')
        },
        {
          path: "flow/flowActiveTaskNew",
          name: "待审批任务",
          meta: {
            notKeepAlive: false,
            componentName: "flowActiveTaskNew"
          },
          component: () => import('@/pages/flow/flowActiveTaskNew.vue')
        },
        {
          path: "flow/startTask",
          name: "我发起的任务",
          meta: {
            notKeepAlive: false,
            componentName: "startTask"
          },
          component: () => import('@/pages/flow/startTask.vue')
        },
        {
          path: "flow/processTracking",
          name: "流程追踪",
          meta: {
            notKeepAlive: false,
            componentName: "processTracking"
          },
          component: () => import('@/pages/flow/processTracking.vue')
        },
        {
          path: "flow/historyTask",
          name: "抄送历史任务",
          meta: {
            notKeepAlive: false,
            componentName: "historyTask"
          },
          component: () => import('@/pages/flow/historyTask.vue')
        },
        {
          path: "flow/approvedTask",
          name: "已审批任务",
          meta: {
            notKeepAlive: false,
            componentName: "approvedTask"
          },
          component: () => import('@/pages/flow/approvedTask.vue')
        },
        {
          path: "flowable/flowFormField",
          name: "表单配置",
          component: () => import('@/pages/flowable/formField/formField.vue')
        },
        {
          path: "flowable/flowModel",
          name: "流程模型",
          component: () => import('@/pages/flowable/model/model.vue')
        },
        {
          path: "flowable/flowParam",
          name: "流程参数",
          component: () => import('@/pages/flowable/env/env.vue')
        },
        {
          path: "flowable/flowDeploy",
          name: "部署管理",
          component: () => import('@/pages/flowable/deploy/deploy.vue')
        },
        {
          path: "flowable/flowBusinessConfig",
          name: "业务配置",
          component: () => import('@/pages/flowable/businessConfig/businessConfig.vue')
        },
        {
          path: "flowable/flowTrack",
          name: "流程追踪",
          component: () => import('@/pages/flowable/track/flowTrack.vue')
        },
        {
          path: "flowable/flowUserToDoTask",
          name: "待审核任务",
          component: () => import('@/pages/flowable/userToDoTask/userToDoTask.vue')
        },
        {
          path: "flowable/flowSurrogate",
          name: "转审批",
          component: () => import('@/pages/flowable/surrogate/surrogate.vue')
        },
        {
          path: "flowable/flowBusinessStatus",
          name: "业务流程状态",
          component: () => import('@/pages/flowable/businessStatus/businessStatus.vue')
        },
        {
          path: "flowable/flowCopy",
          name: "抄送任务",
          component: () => import('@/pages/flowable/copy/copy.vue')
        },
        {
          path: "pms/basePublish/DeclareDocument/MaterialTemplate",
          name: "申报模板管理",
          component: () => import('@/pages/pms/basePublish/DeclareDocument/MaterialTemplate.vue')
        },
        {
          path: "pms/basePublish/DeclareDocument/MaterialDocument",
          name: "申报材料管理",
          component: () => import('@/pages/pms/basePublish/DeclareDocument/MaterialDocument.vue')
        },
        {
          path: "pms/asset/deposit/MidTrmDpsInf",
          name: "定期存款管理",
          component: () => import('@/pages/pms/asset/deposit/MidTrmDpsInf.vue')
        },
        {
          path: "pms/asset/stock/MidAssAsharedescription",
          name: "股票信息管理",
          meta: {
            notKeepAlive: false,
            componentName: "MidAssAsharedescription"
          },
          component: () => import('@/pages/pms/asset/stock/MidAssAsharedescription.vue')
        },
        {
          path: "pms/asset/codeManagement/AssetCodeManageModel",
          name: "资产代码管理",
          component: () => import('@/pages/pms/asset/codeManagement/AssetCodeManageModel.vue')
        },
        {
          path: "pms/prodWroth/NetReportRules",
          name: "净值报送规则配置",
          component: () => import('@/pages/pms/prodWroth/NetReportRules.vue')
        },
        {
          path: "exceimport/UnderFondInfo",
          name: "私募基金底层持仓信息",
          component: () => import('@/pages/exceimport/UnderFundInfo.vue')
        },
        {
          path: "pms/prod/ProdInfoOds1",
          name: "产品信息",
          component: () => import('@/pages/pms/prod/ProdInfoOds.vue')
        },
        {
          path: "pms/prod/ProdInfoOds",
          name: "产品基本信息",
          meta: {
            notKeepAlive: false,
            componentName: "ProdInfoOds",
          },
          component: () => import('@/pages/pms/prod/ProdInfoOds/index.vue')
        },
        {
          path: "pms/prodAccountInfo/T8ProdAccountInfoSearch",
          name: "产品账户信息",
          meta: {
            notKeepAlive: false,
            componentName: "T8ProdAccountInfo",
          },
          component: () => import('@/pages/pms/prodAccountInfo/T8ProdAccountInfoSearch.vue')
        },
        {
          path: "pms/dataNaturalKey/DataNaturalKey",
          name: "各层级业务主键",
          meta: {
            notKeepAlive: false,
            componentName: "DataNaturalKeyModel",
          },
          component: () => import('@/pages/pms/dataNaturalKey/DataNaturalKey.vue')
        },
        {
          path: "pms/sourceDataConfig/SourceDataConfig",
          name: "源数据配置信息",
          meta: {
            notKeepAlive: false,
            componentName: "SourceDataConfigModel",
          },
          component: () => import('@/pages/pms/sourceDataConfig/SourceDataConfig.vue')
        },

        {
          path: "pms/sourceDataChgInfo/SourceDataChgInfo",
          name: "产品源数据变化信息",
          meta: {
            notKeepAlive: false,
            componentName: "SourceDataChgInfoModel",
          },
          component: () => import('@/pages/pms/sourceDataChgInfo/SourceDataChgInfo.vue')
        },

        {
          path: "pms/sourceDataChgInfo/AssetSourceDataChgInfo",
          name: "资产源数据变化信息",
          meta: {
            notKeepAlive: false,
            componentName: "SourceDataChgInfoModel",
          },
          component: () => import('@/pages/pms/sourceDataChgInfo/AssetSourceDataChgInfo.vue')
        },

        {
          path: "pms/importTemplateManage/ImportTemplateManage",
          name: "模板维护",
          meta: {
            notKeepAlive: false,
            componentName: "ImportTemplateManage",
          },
          component: () => import('@/pages/pms/importTemplateManage/ImportTemplateManage.vue')
        },

        {
          path: "pms/importTemplateManage/ImportTemplateDataManage",
          name: "报送数据导入",
          meta: {
            notKeepAlive: false,
            componentName: "ImportTemplateDataManage",
          },
          component: () => import('@/pages/pms/importTemplateManage/ImportTemplateDataManage.vue')
        },
        {
          path: "report/reportTemplate/addMenu",
          name: "报表开发",
          meta: {
            notKeepAlive: false,
            componentName: "addMenu",
          },
          component: () => import('@/pages/report/reportTemplate/addMenu.vue')
        },
        {
          path: "report/reportTemplate/configReport",
          name: "配置报表",
          meta: {
            notKeepAlive: false,
            componentName: "configReport",
          },
          component: () => import('@/pages/report/reportTemplate/configReport.vue')
        },
        {
          path: "report/reportTemplate/queryItemConfig",
          name: "查询条件配置",
          meta: {
            notKeepAlive: false,
            componentName: "queryItemConfig",
          },
          component: () => import('@/pages/report/reportTemplate/queryItemConfig.vue')
        },
        {
          path: "report/reportTemplate/reportQuery",
          name: "报表查询",
          meta: {
            notKeepAlive: false,
            componentName: "reportQuery",
          },
          component: () => import('@/pages/report/reportTemplate/reportQuery.vue')
        },
        {
          path: "report/reportTemplate/editReport",
          name: "报表编辑",
          meta: {
            componentName: "editReport",
            isIframe: true
          },
          component: () => import('@/pages/report/reportTemplate/editReport.vue')
        },
        {
          path: "pms/chart/relationship",
          name: "血缘关系图",
          meta: {
            notKeepAlive: false,
            componentName: "relationship",
          },
          component: () => import('@/pages/pms/chart/relationship.vue')
        },
        {
          path: "report/reportTemplate/reportView/:id",
          name: "报表预览",
          meta: {
            componentName: "reportView",
            isIframe: true,
          },
          component: () => import('@/pages/report/reportTemplate/reportView.vue')
        },
        {
          path: "system/files/AmsFilesInfo",
          name: "文件管理",
          meta: {
            notKeepAlive: false,
            componentName: "AmsFilesInfo",
          },
          component: () => import('@/pages/system/files/AmsFilesInfo.vue')
        },
        {
          path: "pms/rmsOdsZyG06b/RmsOdsZyG06b",
          name: "G06B期末余额维护",
          meta: {
            notKeepAlive: false,
            componentName: "RmsOdsZyG06b",
          },
          component: () => import('@/pages/pms/rmsOdsZyG06b/RmsOdsZyG06b.vue')
        },
        {
          path: "pms/sqlFlow/rmsTableInfo",
          name: "表级别血缘关系",
          meta: {
            notKeepAlive: false,
            componentName: "RmsTableInfo",
          },
          component: () => import('@/pages/pms/sqlFlow/RmsTableInfo.vue')
        },
        {
          path: "pms/sqlFlow/rmsFieldInfo",
          name: "字段级别血缘关系",
          meta: {
            notKeepAlive: false,
            componentName: "RmsFieldInfo",
          },
          component: () => import('@/pages/pms/sqlFlow/RmsFieldInfo.vue')
        },
        {
          path: "pms/sqlFlow/tableLineage",
          name: "表血缘关系修改",
          meta: {
            notKeepAlive: false,
            componentName: "TableLineage",
          },
          component: () => import('@/pages/pms/sqlFlow/TableLineage.vue')
        },
        {
          path: "pms/sqlFlow/fieldLineage",
          name: "字段血缘关系修改",
          meta: {
            notKeepAlive: false,
            componentName: "FieldLineage",
          },
          component: () => import('@/pages/pms/sqlFlow/FieldLineage.vue')
        },
        {
          path: "pms/sqlFlow/relationshipTask",
          name: "清算血缘关系",
          meta: {
            notKeepAlive: false,
            componentName: "RelationshipTask",
          },
          component: () => import('@/pages/pms/sqlFlow/RelationshipTask.vue')
        },
        {
          path: "pms/rmsOdsZyKb02/RmsOdsZyKb02",
          name: "KB02主要经济指标表（企月快02表）",
          meta: {
            notKeepAlive: false,
            componentName: "RmsOdsZyKb02",
          },
          component: () => import('@/pages/pms/rmsOdsZyKb02/RmsOdsZyKb02.vue')
        },
        {
          path: "pms/dwsAssetA1413DepStruc/DwsAssetA1413DepStruc",
          name: "A1413存款期限结构及相关业务情况补录表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsAstAllocationDtl",
          },
          component: () => import('@/pages/pms/dwsAssetA1413DepStruc/DwsAssetA1413DepStruc.vue')
        },
        {
          path: "pms/subject/BaseSubjectMap",
          name: "资产负债分类配置",
          meta: {
            notKeepAlive: false,
            componentName: "BaseSubjectMap",
          },
          component: () => import('@/pages/pms/subject/BaseSubjectMap.vue')
        },
        {
          path: "pms/subject/DwdLinkedTransMapping",
          name: "关联交易映射表",
          meta: {
            notKeepAlive: false,
            componentName: "DwdLinkedTransMapping",
          },
          component: () => import('@/pages/pms/subject/DwdLinkedTransMapping.vue')
        },
        {
          path: "pms/subject/AccountMerge",
          name: "客户账户合并",
          meta: {
            notKeepAlive: false,
            componentName: "CustomerDataMergeModel",
          },
          component: () => import('@/pages/pms/subject/AccountMerge.vue')
        },
        {
          path: "pms/subject/FormCheck",
          name: "报表数据核对",
          meta: {
            notKeepAlive: false,
            componentName: "FormCheck",
          },
          component: () => import('@/pages/pms/subject/FormCheck.vue')
        },
        {
          path: "pms/subject/AssetHoldRegistCheck",
          name: "资产持仓登记校验表",
          meta: {
            notKeepAlive: false,
            componentName: "AssetHoldRegistCheck",
          },
          component: () => import('@/pages/pms/subject/AssetHoldRegistCheck.vue')
        },
        {
          path: "pms/subject/TradeRegistCheck",
          name: "交易登记校验表",
          meta: {
            notKeepAlive: false,
            componentName: "TradeRegistCheck",
          },
          component: () => import('@/pages/pms/subject/TradeRegistCheck.vue')
        },
        {
          path: "pms/investor/InvestorIdInfo",
          name: "全量投资者身份信息",
          meta: {
            notKeepAlive: false,
            componentName: "InvestorBaseInfo",
          },
          component: () => import('@/pages/pms/investor/investorIdInfo.vue')
        },
        {
          path: "pms/investor/InvestorHoldInfo",
          name: "投资者持有信息（子产品）",
          meta: {
            notKeepAlive: false,
            componentName: "InvestorSubHoldInfo",
          },
          component: () => import('@/pages/pms/investor/investorHoldInfo.vue')
        },
        {
          path: "pms/subject/DwsMonthInvRaise",
          name: "月度募集信息中间表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsMonthInvRaise",
          },
          component: () => import('@/pages/pms/subject/DwsMonthInvRaise.vue')
        },
        {
          path: "pms/subject/DwsMonthNavInf",
          name: "月度净值信息中间表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsMonthNavInf",
          },
          component: () => import('@/pages/pms/subject/DwsMonthNavInf.vue')
        },
        {
          path: "pms/subject/DwsMonthPrdDtl",
          name: "产品明细月中间表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsMonthPrdDtl",
          },
          component: () => import('@/pages/pms/subject/DwsMonthPrdDtl.vue')
        },
        {
          path: "pms/subject/DwsDailyPrdDtl",
          name: "产品明细日中间表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsDailyPrdDtl",
          },
          component: () => import('@/pages/pms/subject/DwsDailyPrdDtl.vue')
        },
        {
          path: "pms/subject/DwsPrdSlrFeeDtl",
          name: "产品销售商费用明细表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsPrdSlrFeeDtl",
          },
          component: () => import('@/pages/pms/subject/DwsPrdSlrFeeDtl.vue')
        },
        {
          path: "pms/subject/DwsZyConcentrationCust",
          name: "客户集中度排序",
          meta: {
            notKeepAlive: false,
            componentName: "DwsZyConcentrationCust",
          },
          component: () => import('@/pages/pms/subject/DwsZyConcentrationCust.vue')
        },
        {
          path: "pms/subject/DwsZyShcommonCust",
          name: "上海国际集团共同客户名录",
          meta: {
            notKeepAlive: false,
            componentName: "DwsZyShcommonCust",
          },
          component: () => import('@/pages/pms/subject/DwsZyShcommonCust.vue')
        },
        {
          path: "pms/subject/DwdSpvInvPrdRft",
          name: "人行资管产品统计编码信息",
          meta: {
            notKeepAlive: false,
            componentName: "DwdSpvInvPrdRft",
          },
          component: () => import('@/pages/pms/subject/DwdSpvInvPrdRft.vue')
        },
        {
          path: "pms/subject/DwdAsstBondComprat",
          name: "主体评级",
          meta: {
            notKeepAlive: false,
            componentName: "DwdAsstBondComprat",
          },
          component: () => import('@/pages/pms/subject/DwdAsstBondComprat.vue')
        },
        {
          path: "pms/subject/DwsAstPrdItmBalSmr",
          name: "月度资产负债信息中间表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsAstPrdItmBalSmr",
          },
          component: () => import('@/pages/pms/subject/DwsAstPrdItmBalSmr.vue')
        },
        {
          path: "pms/subject/DwsAstMngPlanInfo",
          name: "公开spv信息",
          meta: {
            notKeepAlive: false,
            componentName: "DwsAstMngPlanInfo",
          },
          component: () => import('@/pages/pms/subject/DwsAstMngPlanInfo.vue')
        },
        {
          path: "pms/subject/DwsProdTTRDBefOri",
          name: "G06穿透前报表（调整前）",
          meta: {
            notKeepAlive: false,
            componentName: "DwsProdTTRDBefOri",
          },
          component: () => import('@/pages/pms/subject/DwsProdTTRDBefOri.vue')
        },
        {
          path: "pms/subject/DwsProdTTRDBef",
          name: "G06穿透前报表（调整后）",
          meta: {
            notKeepAlive: false,
            componentName: "DwsProdTTRDBef",
          },
          component: () => import('@/pages/pms/subject/DwsProdTTRDBef.vue')
        },
        {
          path: "pms/subject/DwsCounterPartyInfo",
          name: "月度交易对手中间表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsCounterPartyInfo",
          },
          component: () => import('@/pages/pms/subject/DwsCounterPartyInfo.vue')
        },
        {
          path: "pms/subject/DwsAstEquInfo",
          name: "贷款明细和收益权明细中间表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsAstEquInfo",
          },
          component: () => import('@/pages/pms/subject/DwsAstEquInfo.vue')
        },
        {
          path: "pms/subject/DwsAstIsuIdtDtl",
          name: "企业债按行业、企业规模统计明细表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsAstIsuIdtDtl",
          },
          component: () => import('@/pages/pms/subject/DwsAstIsuIdtDtl.vue')
        },
        {
          path: "pms/subject/DwsAstDebPbnkDtl",
          name: "资产负债剩余期限明细表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsAstDebPbnkDtl",
          },
          component: () => import('@/pages/pms/subject/DwsAstDebPbnkDtl.vue')
        },
        {
          path: "pms/subject/DwsAstAllocationDtl",
          name: "资产配置情况明细表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsAstAllocationDtl",
          },
          component: () => import('@/pages/pms/subject/DwsAstAllocationDtl.vue')
        },
        {
          path: "pms/subject/DwsG06BIIFbAssetInfo",
          name: "G06b_II非标资产明细表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsG06BIIFbAssetInfo",
          },
          component: () => import('@/pages/pms/subject/DwsG06BIIFbAssetInfo.vue')
        },
        {
          path: "pms/subject/DwsG06BIIDerivateInfo",
          name: "G06b_II衍生品明细表",
          meta: {
            notKeepAlive: false,
            componentName: "DwsG06BIIDerivateInfo",
          },
          component: () => import('@/pages/pms/subject/DwsG06BIIDerivateInfo.vue')
        },
        {
          path: "pms/subject/SimsValuationDataInfo",
          name: '接入SIMS底层估值明细表（调整前）',
          meta: {
            notKeepAlive: false,
            componentName: "SimsValuationDataInfo",
          },
          component: () => import('@/pages/pms/subject/SimsValuationDataInfo.vue')
        },
        {
        path: "pms/subject/SimsValuationDataBInfo",
        name: '接入SIMS底层估值明细表（调整后）',
        meta: {
          notKeepAlive: false,
          componentName: "SimsValuationDataBInfo",
        },
        component: () => import('@/pages/pms/subject/SimsValuationDataBInfo.vue')
        },
        {
          path: "pms/subject/ProdConfigurationScale",
          name: '理财产品专户配置比例表',
          meta: {
            notKeepAlive: false,
            componentName: "ProdConfigurationScale",
          },
          component: () => import('@/pages/pms/subject/ProdConfigurationScale.vue')
        },
        {
          path: "pms/subject/ProdInvestDetails",
          name: '理财产品投资情况明细表',
          meta: {
            notKeepAlive: false,
            componentName: "ProdInvestDetails",
          },
          component: () => import('@/pages/pms/subject/ProdInvestDetails.vue')
        },
        {
          path: "pms/subject/OutsourceAssetMonitorInfo",
          name: '委外资产监测表',
          meta: {
            notKeepAlive: false,
            componentName: "OutsourceAssetMonitorInfo",
          },
          component: () => import('@/pages/pms/subject/OutsourceAssetMonitorInfo.vue')
        },
        {
          path: "pms/subject/AssetEndDate",
          name: '资产到期日期清单',
          meta: {
            notKeepAlive: false,
            componentName: "AssetEndDate",
          },
          component: () => import('@/pages/pms/subject/AssetEndDate.vue')
        },
        {
          path: "pms/subject/BaseReportFileManage",
          name: "产品报告文件管理",
          meta: {
            notKeepAlive: false,
            componentName: "BaseReportFileManage",
          },
          component: () => import('@/pages/pms/subject/BaseReportFileManage.vue')
        },
        {
          path: "report/convert/ReportConvert",
          name: "报表模板转换",
          meta: {
            notKeepAlive: false,
            componentName: "ReportConvert",
          },
          component: () => import('@/pages/report/convert/ReportConvert.vue')
        },
        {
          path: "report/exportAllExcelByTemplate/ExcelByTemplate",
          name: "二维报表报送数据导出",
          meta: {
            notKeepAlive: false,
            componentName: "ExcelByTemplate",
          },
          component: () => import('@/pages/report/exportAllExcelByTemplate/ExcelByTemplate.vue')
        },
        {
          path: "report/rhzy/InterbankDepositInfo",
          name: "存量同业存款信息",
          meta: {
            notKeepAlive: false,
            componentName: "InterbankDepositInfo",
          },
          component: () => import('@/pages/report/rhzy/InterbankDepositInfo.vue')
        },
        {
          path: "report/rhzy/InterbankDepositAmountInfo",
          name: "同业存款发生额信息",
          meta: {
            notKeepAlive: false,
            componentName: "InterbankDepositAmountInfo",
          },
          component: () => import('@/pages/report/rhzy/InterbankDepositAmountInfo.vue')
        },
        {
          path: "report/rhzy/BondInvestInfo",
          name: "存量债券投资信息",
          meta: {
            notKeepAlive: false,
            componentName: "BondInvestInfo",
          },
          component: () => import('@/pages/report/rhzy/BondInvestInfo.vue')
        },
        {
          path: "report/rhzy/BondInvestAmountInfo",
          name: "债券投资发生额信息",
          meta: {
            notKeepAlive: false,
            componentName: "BondInvestAmountInfo",
          },
          component: () => import('@/pages/report/rhzy/BondInvestAmountInfo.vue')
        },
        {
          path: "report/rhzy/SpvInvestInfo",
          name: "存量特定目的载体投资信息",
          meta: {
            notKeepAlive: false,
            componentName: "SpvInvestInfo",
          },
          component: () => import('@/pages/report/rhzy/SpvInvestInfo.vue')
        },
        {
          path: "report/rhzy/SpvInvestAmountInfo",
          name: "特定目的载体投资发生额信息",
          meta: {
            notKeepAlive: false,
            componentName: "SpvInvestAmountInfo",
          },
          component: () => import('@/pages/report/rhzy/SpvInvestAmountInfo.vue')
        },
        {
          path: "report/gsix/SumButtomAsset",
          name: "底层估值明细表（调整前）",
          meta: {
            notKeepAlive: false,
            componentName: "SumButtomAsset",
          },
          component: () => import('@/pages/report/gsix/SumButtomAsset.vue')
        },
        {
          path: "report/gsix/SumButtomAssetAft",
          name: "底层估值明细表（调整后）",
          meta: {
            notKeepAlive: false,
            componentName: "SumButtomAssetAft",
          },
          component: () => import('@/pages/report/gsix/SumButtomAssetAft.vue')
        },
        {
          path: "report/rhjrjgtj/AppAssetA1413DepStruc",
          name: "A1413存款期限结构及相关业务情况表",
          meta: {
            notKeepAlive: false,
            componentName: "AppAssetA1413DepStruc",
          },
          component: () => import('@/pages/report/rhjrjgtj/AppAssetA1413DepStruc.vue')
        },
        {
          path: "report/rhzj/ReportOverseasInvInfo",
          name: "境外投资情况明细表",
          meta: {
            notKeepAlive: false,
            componentName: "ReportOverseasInvInfo",
          },
          component: () => import('@/pages/report/rhzj/ReportOverseasInvInfo.vue')
        },
        {
          path: "report/rhlc/AssetUnincorporatedEntity",
          name: "非法人财务数据报送报表",
          meta: {
            notKeepAlive: false,
            componentName: "AppAssetUnincorporatedEntity",
          },
          component: () => import('@/pages/report/rhlc/AppAssetUnincorporatedEntity.vue')
        },
        {
          path: "pms/subject/DwdProdFsfaConfirm",
          name: "日间导入估值表数据查询",
          meta: {
            notKeepAlive: false,
            componentName: "DwdProdFsfaConfirm",
          },
          component: () => import('@/pages/pms/subject/DwdProdFsfaConfirm.vue')
        },
      ]
    }]
})
