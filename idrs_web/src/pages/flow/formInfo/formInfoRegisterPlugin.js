let formInfoRegisterPlugin = {};
//创设的需要重写
//import FlowComponentExample from "@/pages/flow/formInfo/flowComponentExample";
//产品提交审批，参数确认等需要所有组件提交时
//import ProdInfoAllFlow from "@/pages/pms/flowProdFrom/M81/ProdInfoAllFlow";
//产品提交审批，调整单组件提交时
//import ProdInfoAdjustFlow from "@/pages/pms/flowProdFrom/M81/ProdInfoAdjustFlow";
//产品提交审批，创设单组件提交时
//import ProdInfoFlow from "@/pages/pms/flowProdFrom/M81/ProdInfoFlow";
//初创
//import ProdInfoCreateFlow from "@/pages/pms/flowProdFrom/M81/ProdInfoCreateFlow";
//产品开放日调整
//import ProdWorkdayAdjustFlow from "@/pages/pms/flowProdFrom/M81/ProdWorkDayAdjustFlow";

//申报模板
import ProdDeclaraModelParamsFlow from "@/pages/pms/flowProdFrom/M81/ProdDeclaraModelParamsFlow";
//费用分成（新增，修改）,费用分成确认
import FeeDivideFlow from "@/pages/pms/flowProdFrom/M81/FeeDivideFlow";
//产品状态调整
import ProdStatusAdjustFlow from "@/pages/pms/flowProdFrom/M81/ProdStatusAdjustFlow";
//产品到期
import ProdExpireFlow from "@/pages/pms/flowProdFrom/M81/ProdExpireFlow";
//产品延期
import ProdDelayFlow from "@/pages/pms/flowProdFrom/M81/ProdDelayFlow";
//产品清盘方案
import ProdLiquidationFlow from "@/pages/pms/flowProdFrom/M81/ProdLiquidationFlow";
//产品分红
import DividendPlanFlow from "@/pages/pms/flowProdFrom/M81/DividendPlanFlow";
//自定义创设会
import MeetCreateFlow from "@/pages/pms/flowProdFrom/M81/MeetCreateFlow";
//账户信息
import AccountInfoFlow from "@/pages/pms/flowProdFrom/M81/AccountInfoFlow";
//产品从业人员
import EmployeesFlow from "@/pages/pms/flowProdFrom/M81/EmployeesFlow";
//业绩基准需求确认及发行前业绩基准需求
import PriceConfirmFlow from "@/pages/pms/flowProdFrom/M81/PriceConfirmFlow";
//发行说明用印
import IssueSealFlow from "@/pages/pms/flowProdFrom/M81/IssueSealFlow";
//托管协议用印
import TrustSealFlow from "@/pages/pms/flowProdFrom/M81/TrustSealFlow";
//代销协议用印
import ConsignmentSealFlow from "@/pages/pms/flowProdFrom/M81/ConsignmentSealFlow";
//产品创设会审核
import ProdMeetConfirmFlow from "@/pages/pms/flowProdFrom/M81/ProdMeetConfirmFlow";
//产品创设会
import ProdMeetCreateFlow from "@/pages/pms/flowProdFrom/M81/ProdMeetCreateFlow";
//风险评级新增，修改，风险确认，修改
import RiskScoreFlow from "@/pages/pms/flowProdFrom/M81/RiskScoreFlow";
//调整风险等级
import RiskGradeFlow from "@/pages/pms/flowProdFrom/M81/RiskGradeFlow";
//产品费用优惠确认 及 费用优惠
import FeeConcessionConfirmFlow from "@/pages/pms/flowProdFrom/M81/FeeConcessionConfirmFlow";
//交易费用优惠新增，修改，确认
import FeeDealDiscountFlow from "@/pages/pms/flowProdFrom/M81/FeeDealDiscountFlow";
//额度管理确认 及 额度需求管理
import QuotaConfirmFlow from "@/pages/pms/flowProdFrom/M81/QuotaConfirmFlow";
//销售商调整额度调整
import DistriQuotaFlow from "@/pages/pms/flowProdFrom/M81/DistriQuotaFlow";
//消保审核
import ProdConsumerInsuranceFlow from "@/pages/pms/flowProdFrom/M81/ProdConsumerInsuranceFlow";
//文档模板
import PrintTempVersionServiceFlow from "@/pages/pms/flowProdFrom/M81/PrintTempVersionServiceFlow";
//新增创意登记
import AddSeminarFlow from "@/pages/pms/flowProdFrom/M81/AddSeminarFlow";
//修改创意会议
import UpdCreativeFlow from "@/pages/pms/flowProdFrom/M81/UpdCreativeFlow";
//修改创意登记
import UpdSeminarFlow from "@/pages/pms/flowProdFrom/M81/UpdSeminarFlow";
//信披投资经理
import InvestManagerFlow from "@/pages/pms/flowProdFrom/M81/InvestManagerFlow";


//文档模板上传
import PrintTempUploadFlow from "@/pages/pms/flowProdFrom/M81/documentFlow/PrintTempUploadFlow";

//文档子模版上传
import PrintSubTempUploadFlow from "@/pages/pms/flowProdFrom/M81/documentFlow/PrintSubTempUploadFlow";

//文档在线编辑（所有）
import T8OnlineWordValueFlow from "@/pages/pms/flowProdFrom/M81/documentFlow/T8OnlineWordValueFlow";

//说明书审批（未完成）
import InstructionsSealFlow from "@/pages/pms/flowProdFrom/M81/InstructionsSealFlow";
//定价一览表审批
import PriceFlow from "@/pages/pms/flowProdFrom/M81/PriceFlow"

//信披公告审批
import DisclosureNoticeFlow from "@/pages/pms/flowProdFrom/M81/DisclosureNoticeFlow";

//信披托管行复核审批
import DisclosureNoticeTrusteeFlow from "@/pages/pms/flowProdFrom/M81/DisclosureNoticeTrusteeFlow";

//净值披露公告审批
import DisclosureNoticeNetValueFlow from "@/pages/pms/flowProdFrom/M81/DisclosureNoticeNetValueFlow";
//托管意见确认
import TruteeApprovalOpinionFlow from "@/pages/pms/flowProdFrom/M81/TruteeApprovalOpinionFlow";

//分红公告审批
import BonusNoticeFlow from "@/pages/pms/flowProdFrom/M81/BonusNoticeFlow";

//存续份额审批
import DuringEstablishSortFlow from "@/pages/pms/flowProdFrom/M81/DuringEstablishSortFlow";

import ProdInfoServiceFlow from "@/pages/pms/flowProdFrom/M81/ProdInfoServiceFlow";



//产品清盘-产品经理录入审批
import ProdManagerInputApproval from "@/pages/flow/ProdManagerInputApproval";

const components = [
 // FlowComponentExample,
 // ProdInfoAllFlow,
 // ProdInfoAdjustFlow,
 // ProdInfoFlow,
 // ProdInfoCreateFlow,
 // ProdWorkdayAdjustFlow,
  ProdInfoServiceFlow,
  ProdDeclaraModelParamsFlow,
  FeeDivideFlow,
  ProdStatusAdjustFlow,
  ProdDelayFlow,
  ProdExpireFlow,
  ConsignmentSealFlow,
  ProdLiquidationFlow,
  DividendPlanFlow,
  MeetCreateFlow,
  AccountInfoFlow,
  EmployeesFlow,
  FeeConcessionConfirmFlow,
  FeeDealDiscountFlow,
  QuotaConfirmFlow,DistriQuotaFlow,
  PriceConfirmFlow,
  IssueSealFlow,
  TrustSealFlow,
  InstructionsSealFlow,
  AddSeminarFlow,
  UpdCreativeFlow,
  UpdSeminarFlow,
  InvestManagerFlow,
  ProdMeetCreateFlow, ProdMeetConfirmFlow,
  RiskScoreFlow, RiskGradeFlow,
  ProdConsumerInsuranceFlow,
  PrintTempVersionServiceFlow,
  PrintTempUploadFlow,
  PrintSubTempUploadFlow,
  T8OnlineWordValueFlow,
  PriceFlow,
  DisclosureNoticeFlow,
  DisclosureNoticeTrusteeFlow,
  DisclosureNoticeNetValueFlow,
  DuringEstablishSortFlow,
  TruteeApprovalOpinionFlow,
  BonusNoticeFlow,
  ,ProdManagerInputApproval
];


formInfoRegisterPlugin.install = function(Vue, options) {
  //注册全局组件
  components.forEach(component => {
    Vue.component(component.name, component);
  });

}

export default formInfoRegisterPlugin;
