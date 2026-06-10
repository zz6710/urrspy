package com.kayak.rpt.zz.audit.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.zz.audit.model.ReportAuditResult;
import com.kayak.rpt.zz.audit.service.ReportAuditResultService;
import com.kayak.rpt.zz.manage.model.*;
import com.kayak.rpt.zz.manage.service.AssetDebtRegisterInfoService;
import com.kayak.rpt.zz.manage.service.CheckDataForVueService;
import com.kayak.rpt.zz.manage.service.ProdRegistFilingInfoService;
import com.kayak.rpt.zz.manage.service.TrCustRegisterInfoService;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ty
 * @since 2023-05-18 10:51:33
 */
@RestController
@RequestMapping(value = "/audit")
public class ReportAuditResultController extends BaseController {

    @Autowired
    CheckDataForVueService checkDataForVueService;

    CheckDataParams checkDataParams = new CheckDataParams();
    @Autowired
    private ReportAuditResultService auditResultService;
    @Autowired
    TrCustRegisterInfoService trCustRegisterInfoService;
    @Autowired
    AssetDebtRegisterInfoService assetDebtRegisterInfoService;
    @Autowired
    ProdRegistFilingInfoService prodRegistFilingInfoService;
    @PostMapping(value = "/status")
    public String audit(@RequestBody ReportAuditResult reportAuditResult) throws Exception {
        auditResultService.updateAuditResult(reportAuditResult);
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    @PostMapping(value = "/indexstatus")
    public String getIndexStatus(@RequestBody ReportAuditResult reportAuditResult) throws Exception {
        int mark = auditResultService.getIndexStatus(reportAuditResult);
        if("app_prod_regist_filing_info".equals(reportAuditResult.getTableId())){
            int flag = auditResultService.getCheckTable(reportAuditResult);
            if(flag>0){
                return RequestSupport.updateReturnJson(true, "存在九大附件缺失的产品", null).toString();
            }
        }
        if(mark>0){
            return RequestSupport.updateReturnJson(true, "存在指标校验未通过数据", null).toString();
        }else{
            return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
        }

    }

    /**
     * 合法性校验--交易登记
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkprodTransRegistInfo")
    public String checkprodTransRegistInfo(@RequestBody ProdTransRegistInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.prodTransRegistInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }
    /**
     * 合法性校验--资产持仓
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkassetRegistInfo")
    public String checkassetRegistInfo(@RequestBody AssetRegistInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.assetRegistInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 合法性校验--底层资产持仓
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkunderAssetRegistInfo")
    public String checkunderAssetRegistInfo(@RequestBody UnderAssetRegistInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.underAssetRegistInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 合法性校验--资产负债登记
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkassetDebtRegisterInfo")
    public String checkassetDebtRegisterInfo(@RequestBody AssetDebtRegisterInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.assetDebtRegisterInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 合法性校验--产品申报登记信息管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkprodRegistFilingInfo")
    public String checkprodRegistFilingInfo(@RequestBody ProdRegistFilingInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.prodRegistFilingInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 合法性校验--产品发行登记信息管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkProdIssuanceRegistInfo")
    public String checkProdIssuanceRegistInfo(@RequestBody ProdIssuanceRegistInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.prodIssuanceRegistInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }
    /**
     * 合法性校验--募集总量登记管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkInitialSubRegistInfo")
    public String checkInitialSubRegistInfo(@RequestBody InitialSubRegistInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.initialSubRegistInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }
    /**
     * 合法性校验--产品存续期登记管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkSubseqSubscrRegistInfo")
    public String checkSubseqSubscrRegistInfo(@RequestBody SubseqSubscrRegistInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.subseqSubscrRegistInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }
    /**
     * 合法性校验--净值信息登记
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkAppNavInfoReg")
    public String checkAppNavInfoReg(@RequestBody AppNavInfoReg params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.appNavInfoRegCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }
    /**
     * 合法性校验--产品状态管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkProdStateRegistInfo")
    public String checkProdStateRegistInfo(@RequestBody ProdStateRegistInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.prodStateRegistInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }
    /**
     * 合法性校验--产品终止登记管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkTrTerminationRegistInfo")
    public String checkTrTerminationRegistInfo(@RequestBody TrTerminationRegistInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.terminationRegistInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 合法性校验--投资者持有信息登记管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkTrCustVolRegisterInfo")
    public String checkTrCustVolRegisterInfo(@RequestBody TrCustVolRegisterInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.trCustVolRegisterInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 合法性校验--投资者明细信息登记管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkTrCustTransInfo")
    public String checkTrCustTransInfo(@RequestBody TrCustTransInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.trCustTransInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 合法性校验--投资者身份信息登记管理
     * @param params
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/checkTrCustRegisterInfo")
    public String checkTrCustRegisterInfo(@RequestBody TrCustRegisterInfo params) throws Exception {
        checkDataParams.initDataNoDict();
        String whiteregex = CheckDataParams.whiteregex;
        String whitereForCode = CheckDataParams.whitereForCode;
        String checkErr = checkDataForVueService.trCustRegisterInfoCheckForVue(whiteregex,whitereForCode,params);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
            return RequestSupport.updateReturnJson(false,  "处理失败！错误信息为：\n"+checkErr, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 产品申报登记删除前校验 是否有已发行登记产品，有则不允许删除
     * @param params
     * @return
     */
    @RequestMapping(value = "/prodRegistFilingCheckEffective")
    public String  prodRegistFilingCheckEffective(@RequestBody ProdRegistFilingInfo params) throws Exception {
        if(params!=null && (params.getIdentCode()==null||"".equals(params.getIdentCode()))){
            //行内标识码 为空时，不校验
            return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
        }
        //删除前先校验产品是否已发行登记（有过发行登记的记录），若是，不允许删除
        int checkInt = prodRegistFilingInfoService.findProdRegistFilingInfoEffective(params);
        if(checkInt>0){
            return RequestSupport.updateReturnJson(false,  "该产品已发行登记，不允许删除！", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }
    /**
     * 投资者身份信息删除前校验 是否有已发行登记产品记录，有则不允许删除
     * @param params
     * @return
     */
    @RequestMapping(value = "/trCustRegisterCheckEffective")
    public String  trCustRegisterCheckEffective(@RequestBody TrCustRegisterInfo params) throws Exception {
        if(params!=null && (params.getCustNo()==null||"".equals(params.getCustNo()))){
            //识别标识为空时，不校验
            return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
        }
        //根据识别标识字段，检查投资者明细和投资者持有，是否存在数据，若存在数据，弹出“该投资者已存在投资者明细信息或投资者持有信息，不允许删除！”
        int custVolEffect = trCustRegisterInfoService.findProdTrCustVolEffective(params);
        if(custVolEffect>0){
            return RequestSupport.updateReturnJson(false,  "该投资者已存在投资者持有信息，不允许删除！", null).toString();
        }
        int custTransEffect = trCustRegisterInfoService.findProdTrCustTransEffective(params);
        if(custTransEffect>0){
            return RequestSupport.updateReturnJson(false,  "该投资者已存在投资者明细信息，不允许删除！", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }

    /**
     * 资产负债要素信息删除时，后台以“行内资产/负债编码“字段，检查交易信息登记、资产持仓登记、底层资产持仓登记中是否存在资产，若存在数据，弹出”该资产已存在交易信息登记/资产持仓登记/底层资产持仓登记，不允许删除！“
     * @param params
     * @return
     */
    @RequestMapping(value = "/assetDebtRegisterCheckEffective")
    public String  assetDebtRegisterCheckEffective(@RequestBody AssetDebtRegisterInfo params) throws Exception {
        if(params!=null && (params.getAssetCode()==null||"".equals(params.getAssetCode()))){
            //行内资产/负债编码为空时，不校验
            return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
        }
        int transEffevtive = assetDebtRegisterInfoService.findProdTransRegistInfosEffective(params);
        if(transEffevtive>0){
            return RequestSupport.updateReturnJson(false,  "该资产已存在交易信息登记记录，不允许删除！", null).toString();
        }
        int assetEffevtive = assetDebtRegisterInfoService.findAssetRegistInfosEffective(params);
        if(assetEffevtive>0){
            return RequestSupport.updateReturnJson(false,  "该资产已存在资产持仓登记记录，不允许删除！", null).toString();
        }
        int underEffevtive = assetDebtRegisterInfoService.findUnderAssetRegistInfosEffective(params);
        if(underEffevtive>0){
            return RequestSupport.updateReturnJson(false,  "该资产已存在底层资产持仓登记记录，不允许删除！", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "验证通过", null).toString();
    }
}
