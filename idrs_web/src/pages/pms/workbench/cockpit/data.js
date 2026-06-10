// 饼图
const pieData = {
  unit: '亿元',
  data: [
    { value: 32, name: '阳光金', amount: 80 },
    { value: 32, name: '阳光红', amount: 80 },
    { value: 28, name: '阳光橙', amount: 70 },
    { value: 26, name: '阳光黄', amount: 50 },
    { value: 22, name: '阳光绿', amount: 40 },
    { value: 18, name: '阳光蓝', amount: 30 },
    { value: 8, name: '阳光青', amount: 20 }
  ]
}
// 柱形图： 创设、发行、报备
const createData =  {
  color: ['#eb4e7a'],
  axisData: ['阳光金', '阳光红', '阳光橙', '阳光黄', '阳光绿', '阳光青', '阳光蓝'],
  seriesData: [{
    name: '创设',
    type: 'bar',
    data: [120, 200, 150, 80, 150, 100, 60],
    unit: '只'
  }]
}
// 柱形图： 产品统计
const productStastic = {
  color: ['#7bede8'],
  axisData: ['产品创设', '产品报备', '产品发行', '产品运营', '产品到期'],
  seriesData: [
    {
      type: 'bar',
      data: [110, 200, 50, 60, 100]
    }
  ]
}
// 仪表盘：销售目标达成情况
const saleInfoList = [
  {
    name: '季末目标',
    totalAmount: 52700,
    amount: 2700,
    percent: 5.12,
    unit: '亿元'
  },
  {
    name: '年末目标',
    totalAmount: 152700,
    amount: 52700,
    percent: 34.51,
    unit: '亿元'
  },
  {
    name: '月末目标',
    totalAmount: 12700,
    amount: 13800,
    percent: 108.66,
    unit: '亿元'
  }
]
// 去年同比分析
const cardList = [
  {
    title: '理财规模',
    unit: '亿元',
    value: "20,015",
    status: 'up',
    percent: '8',
    tip: '同比去年底'
  },
  {
    title: '理财产品',
    unit: '只',
    value: "356",
    status: 'down',
    percent: '8',
    tip: '同比去年底'
  },
  {
    title: '本年发行产品',
    unit: '只',
    value: "589",
    status: 'up',
    percent: '8',
    tip: '同比去年同期',
    totalAmount: '',
    unitMoney: ''
  },
  {
    title: '本年到期产品',
    unit: '只',
    value: "356",
    status: 'down',
    percent: '8',
    tip: '同比去年同期'
  }
]
export const chartDataAction = {
  prodScaleAction: 'DirectMode.findExistingScaleInfo',
  prodCountAction: 'DirectMode.findExistingScalefindProdSize',
  prodStasticAction: 'DirectMode.findExistingScaleProdStage',
  saleTargetAction: 'DirectMode.findSalesTarget',
  createAction: 'DirectMode.findProdStageOfCreate',
  publishAction: 'DirectMode.findProdStageOfIssue',
  reportAction: 'DirectMode.findProdStageOfApply',
  yearCompareAction: 'DirectMode.findComparedWithLastYearInfo',
  nextYearAction: 'DirectMode.findFutureExpectationsInfo',
  salesTargetAction: 'T8ProdSalesTarget.findT8ProdSalesTargets',
}
export const tableAction = {
  prodScaleAction: 'ScaleMode.findExistingScaleDetailsInfo',
  prodStasticAction: 'ScaleMode.findExistingScaleProdStageDetailsInfo',
  prodSalesAction: 'DirectMode.findSalesTargetDetailsInfo',
  prodSalesTargetAction: 'DirectMode.findComparedSalesTargetDetailsInfo',
  prodStageAction: 'DirectMode.findStageDetailsInfo',
  nextYearAction: 'DirectMode.findFutureExpectationsDetailsInfo',
}
export const tableHeaderInfo = {
  prodScaleHeader: [
    {
      label: '产品代码',
      prop: 'prodCode'
    },
    {
      label: '产品名称',
      prop: 'prodName'
    },
    {
      label: '数据类型',
      prop: 'dataName'
    },
    {
      label: '金额（元）',
      prop: 'amount'
    },
  ],
  prodStasticHeader: [
    {
      label: '产品代码',
      prop: 'prodCode'
    },
    {
      label: '产品名称',
      prop: 'prodName'
    },
    {
      label: '数据类型',
      prop: 'dataName'
    },
    {
      label: '金额（元）',
      prop: 'amount'
    },
  ],
  prodSalesTargetHeader: [
    {
      label: '产品代码',
      prop: 'prodCode'
    },
    {
      label: '产品名称',
      prop: 'prodName'
    },
    {
      label: '系列名称',
      prop: 'seriesName'
    },
    {
      label: '金额（元）',
      prop: 'amount'
    },
  ],
  prodCountTargetHeader: [
    {
      label: '产品代码',
      prop: 'prodCode'
    },
    {
      label: '产品名称',
      prop: 'prodName'
    },
    {
      label: '系列名称',
      prop: 'seriesName'
    },
    {
      label: '产品状态',
      prop: 'prodStatus'
    },
    {
      label: '产品子状态',
      prop: 'prodSonStatus'
    },
  ],
  prodFutureHeader: [
    {
      label: '日期',
      prop: 'systemDate'
    },
    {
      label: '募集产品数量（只）',
      prop: 'riseAmount'
    },
    {
      label: '开放产品数量（只）',
      prop: 'openAmount'
    },
    {
      label: '到期产品数量（只）',
      prop: 'endAmount'
    },
  ]
}
