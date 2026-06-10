package com.kayak.rpt.zz.manage.action;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.FileUtil;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.rhlc.model.AppAssetUnincorporatedEntity;
import com.kayak.rpt.rhlc.service.AppAssetUnincorporatedEntityService;
import com.kayak.rpt.rhzj.util.ExcelParse;
import com.kayak.rpt.zz.manage.ManageInfoUtil;
import com.kayak.rpt.zz.manage.model.*;
import com.kayak.rpt.zz.manage.service.*;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.subject.service.SimsValuationDataBInfoService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

import com.kayak.rpt.zz.manage.model.ProdRegistFilingInfo;

@RestController
@RequestMapping(value = "/reportManage")
public class ManageInfoAction extends BaseController {

    @Autowired
    AppraiseRegistInfoService appraiseRegistInfoService;

    @Autowired
    ProdRegistFilingInfoService prodRegistFilingInfoService;

    @Autowired
    ProdIssuanceRegistInfoService prodIssuanceRegistInfoService;

    @Autowired
    InitialSubRegistInfoService initialSubRegistInfoService;

    @Autowired
    SubseqSubscrRegistInfoService subseqSubscrRegistInfoService;

    @Autowired
    ProdTransRegistInfoService prodTransRegistInfoService;

    @Autowired
    UnderAssetRegistInfoService underAssetRegistInfoService;

    @Autowired
    ProdStateRegistInfoService prodStateRegistInfoService;

    @Autowired
    AssetRegistInfoService assetRegistInfoService;

    @Autowired
    TrTerminationRegistInfoService terminationRegistInfoService;

    @Autowired
    TrCustRegisterInfoService trCustRegisterInfoService;

    @Autowired
    TrCustTransInfoService trCustTransInfoService;

    @Autowired
    investorSubHoldService investorSubHoldService;

    @Autowired
    TrCustVolRegisterInfoService trCustVolRegisterInfoService;

    @Autowired
    AssetDebtRegisterInfoService assetDebtRegisterInfoService;

    @Autowired
    TrPractyRegistInfoService trPractyRegistInfoService;

    @Autowired
    SimsValuationDataBInfoService simsValuationDataBInfoService;

    @Autowired
    ReportTimeConfigImportService reportTimeConfigImportService;

    @Autowired
    AppSonShareInfoRegService appSonShareInfoRegService;

    @Autowired
    AppAssetUnincorporatedEntityService appAssetUnincorporatedEntityService;

    @Autowired
    CompareHeaderService compareHeaderService;

    @Autowired
    CheckDataForInfoService checkDataForInfoService;

    @Autowired
    AppNavInfoRegService appNavInfoRegService;

    @Autowired
    CheckDataForVueService checkDataForVueService;

    @Autowired
    DwsScrThemeIndInfService dwsScrThemeIndInfService;

    @Autowired
    BaseFiveTypeCmpInfService baseFiveTypeCmpInfService;

    @Autowired
    BaseCfetsBondInfService baseCfetsBondInfService;

    @Autowired
    BaseFiveNonstandEntityService baseFiveNonstandEntityService;

    @Autowired
    BaseFiveNonstandFintechService baseFiveNonstandFintechService;

    @Autowired
    DwsZyScrThemeIndInfService dwsZyScrThemeIndInfService;

    CheckDataParams checkDataParams = new CheckDataParams();
    @Autowired
    private ComnDao comnDao;

    @RequestMapping(value = "/appraiseImport.json", produces = {"application/json;charset=UTF-8"})
    // 估值信息导入
    public String appraiseImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("beginDate", request.getParameter("beginDate"));
            params.put("queryDate", request.getParameter("queryDate"));
            List<AppraiseRegistInfo> reportAppraise = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, AppraiseRegistInfo.class, true, null);

            // 移除第一标题行
            reportAppraise.remove(0);
            for (AppraiseRegistInfo appraiseRegistInfo : reportAppraise) {
                appraiseRegistInfo.setValuationDate(StringUtils.isEmpty(appraiseRegistInfo.getValuationDate()) ? null : appraiseRegistInfo.getValuationDate().replace("-", ""));
                appraiseRegistInfo.setImpDate(StringUtils.isEmpty(appraiseRegistInfo.getImpDate()) ? null : appraiseRegistInfo.getImpDate().replace("-", ""));
                appraiseRegistInfo.setRegisterDate(StringUtils.isEmpty(appraiseRegistInfo.getRegisterDate()) ? null : appraiseRegistInfo.getRegisterDate().replace("-", ""));
                appraiseRegistInfo.setTheoryReportStartDate(StringUtils.isEmpty(appraiseRegistInfo.getValuationDate()) ? null : appraiseRegistInfo.getValuationDate().replace("-", ""));
            }

            appraiseRegistInfoService.importAppraiseRegistInfo(reportAppraise, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/assetDebtRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 资产要素信息导入
    public String assetDebtRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "资产负债要素登记导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_asset_debt_register_info",1);
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "资产负债要素登记导入失败，文件与资产负债要素登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            Map<String, Object> params = new HashMap<>();
            params.put("beginDate", request.getParameter("beginDate"));
            params.put("queryDate", request.getParameter("queryDate"));
            //表头所在行 2
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_asset_debt_register_info");
            //仅读取一行标题 +其他数据
            List<List<String>> datas = ExcelParse.ReadExcelRowData(file.getInputStream(), 0, dataStartRow-1, dataStartRow-1, true, null);
            // 移除第一标题行
            datas.remove(0);
            List<AssetDebtRegisterInfo> infos = ManageInfoUtil.initDebtRegister(datas, params);
            if(infos == null || infos.size()==0){
                return RequestSupport.updateReturnJson(false, "资产负债要素登记导入失败，请先确认数据不可为空", null).toString();
            }else if(infos != null && infos.size() < 1){ //已经移除一行标题，此处判定仅按最小数据条数判断
                return RequestSupport.updateReturnJson(false, "资产负债要素登记导入失败，请先确认数据不可为空", null).toString();
            }
            checkDataParams.initData();
            //校验数据
            int i = dataStartRow;
            for (AssetDebtRegisterInfo assetDebtRegisterInfo : infos) {
                i++;
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErrImport = checkDataForVueService.assetDebtRegisterInfoCheckForImport(assetDebtRegisterInfo);
                assetDebtRegisterInfo.setCcIssRatePart(StringUtils.isEmpty(assetDebtRegisterInfo.getCcIssRatePart()) ? null : assetDebtRegisterInfo.getCcIssRatePart().split(" ")[0]);
                assetDebtRegisterInfo.setCcSpecificBondType(StringUtils.isEmpty(assetDebtRegisterInfo.getCcSpecificBondType()) ? null : assetDebtRegisterInfo.getCcSpecificBondType().split(" ")[0]);
                assetDebtRegisterInfo.setCcIssModeBond(StringUtils.isEmpty(assetDebtRegisterInfo.getCcIssModeBond()) ? null : assetDebtRegisterInfo.getCcIssModeBond().split(" ")[0]);
                assetDebtRegisterInfo.setCcIndustryIssuer(StringUtils.isEmpty(assetDebtRegisterInfo.getCcIndustryIssuer()) ? null : assetDebtRegisterInfo.getCcIndustryIssuer().split(" ")[0]);
                assetDebtRegisterInfo.setMmManagerType(StringUtils.isEmpty(assetDebtRegisterInfo.getMmManagerType()) ? null : assetDebtRegisterInfo.getMmManagerType().split(" ")[0]);
                assetDebtRegisterInfo.setMmIndustryInvest(StringUtils.isEmpty(assetDebtRegisterInfo.getMmIndustryInvest()) ? null : assetDebtRegisterInfo.getMmIndustryInvest().split(" ")[0]);
                String checkErr = checkDataForVueService.assetDebtRegisterInfoCheckForVue(whiteregex, whitereForCode, assetDebtRegisterInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！第"+i+"行错误信息为：\n" + checkErr+checkErrImport, null).toString();
                }
           }

            assetDebtRegisterInfoService.importAssetDebtRegisterInfo(infos, params);
        } catch (Exception e) {
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/assetUnincorporatedEntityImport.json", produces = {"application/json;charset=UTF-8"})
    // 非法人财务数据导入
    public String assetUnincorporatedEntityImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", DateUtil.getNowDate());
            List<AppAssetUnincorporatedEntity> appAssetUnincorporatedEntities = ExcelParse.readExcelData(file.getInputStream(), 0, 1, 2, AppAssetUnincorporatedEntity.class, true, null);
            appAssetUnincorporatedEntityService.impAppAssetUnincorporatedEntity(appAssetUnincorporatedEntities, params);
        } catch (Exception e) {
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/importDwsScrThemeIndInf.json",produces = { "application/json;charset=UTF-8"})
    // 资产维度打标中间表导入
    public String importDwsScrThemeIndInf( @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,  HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", request.getParameter("reportDate"));
            List<DwsScrThemeIndInf> dwsScrThemeIndInfs = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 1,  DwsScrThemeIndInf.class, true, null);
            if (CollectionUtil.isEmpty(dwsScrThemeIndInfs)) {
                return updateFailure("导入文件内容不能为空，请检查后重试！ ");
            }
            for (DwsScrThemeIndInf dwsScrThemeIndInf : dwsScrThemeIndInfs) {
                dwsScrThemeIndInf.setAssDebtType(StringUtils.isEmpty(dwsScrThemeIndInf.getAssDebtType())?null:dwsScrThemeIndInf.getAssDebtType().split(" ")[0]);
                dwsScrThemeIndInf.setIsfintech(StringUtils.isEmpty(dwsScrThemeIndInf.getIsfintech())?null:dwsScrThemeIndInf.getIsfintech().split(" ")[0]);
                dwsScrThemeIndInf.setIsgreen(StringUtils.isEmpty(dwsScrThemeIndInf.getIsgreen())?null:dwsScrThemeIndInf.getIsgreen().split(" ")[0]);
                dwsScrThemeIndInf.setIsinclusive(StringUtils.isEmpty(dwsScrThemeIndInf.getIsinclusive())?null:dwsScrThemeIndInf.getIsinclusive().split(" ")[0]);
                dwsScrThemeIndInf.setIspension(StringUtils.isEmpty(dwsScrThemeIndInf.getIspension())?null:dwsScrThemeIndInf.getIspension().split(" ")[0]);
                dwsScrThemeIndInf.setIsdigital(StringUtils.isEmpty(dwsScrThemeIndInf.getIsdigital())?null:dwsScrThemeIndInf.getIsdigital().split(" ")[0]);
                dwsScrThemeIndInf.setIspollution(StringUtils.isEmpty(dwsScrThemeIndInf.getIspollution())?null:dwsScrThemeIndInf.getIspollution().split(" ")[0]);
                dwsScrThemeIndInf.setFintechTyp5(StringUtils.isEmpty(dwsScrThemeIndInf.getFintechTyp5())?null:dwsScrThemeIndInf.getFintechTyp5().split(" ")[0]);
                dwsScrThemeIndInf.setFintechTyp6(StringUtils.isEmpty(dwsScrThemeIndInf.getFintechTyp6())?null:dwsScrThemeIndInf.getFintechTyp6().split(" ")[0]);
                dwsScrThemeIndInf.setFintechTyp7(StringUtils.isEmpty(dwsScrThemeIndInf.getFintechTyp7())?null:dwsScrThemeIndInf.getFintechTyp7().split(" ")[0]);
                dwsScrThemeIndInf.setFintechTyp8(StringUtils.isEmpty(dwsScrThemeIndInf.getFintechTyp8())?null:dwsScrThemeIndInf.getFintechTyp8().split(" ")[0]);
            }
            dwsScrThemeIndInfService.importDwsScrThemeIndInf(dwsScrThemeIndInfs, params);
        } catch (Exception e) {
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/importBaseFiveTypeCmpInf.json",produces = { "application/json;charset=UTF-8"})
    // 额外打标主体名单表导入
    public String importBaseFiveTypeCmpInf( @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,  HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", request.getParameter("reportDate"));
            params.put("fiveType", request.getParameter("fiveType"));
            params.put("crtDt", DateUtil.getNowDate());
            List<BaseFiveTypeCmpInf> baseFiveTypeCmpInfs = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 1,  BaseFiveTypeCmpInf.class, true, null);
            if (CollectionUtil.isEmpty(baseFiveTypeCmpInfs)) {
                return updateFailure("导入文件内容不能为空，请检查后重试！ ");
            }
            baseFiveTypeCmpInfService.importBaseFiveTypeCmpInf(baseFiveTypeCmpInfs, params);
        } catch (Exception e) {
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/importBaseCfetsBondInf.json",produces = { "application/json;charset=UTF-8"})
    // CFETS债券指数样本券数据导入
    public String importBaseCfetsBondInf( @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,  HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", request.getParameter("reportDate"));
            params.put("crtDt", DateUtil.getNowDate());
            List<BaseCfetsBondInf> baseCfetsBondInfs = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 1,  BaseCfetsBondInf.class, true, null);
            if (CollectionUtil.isEmpty(baseCfetsBondInfs)) {
                return updateFailure("导入文件内容不能为空，请检查后重试！ ");
            }
            baseCfetsBondInfService.importBaseCfetsBondInf(baseCfetsBondInfs, params);
        } catch (Exception e) {
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/importBaseFiveNonstandEntity.json",produces = { "application/json;charset=UTF-8"})
    // 非标融资主体主题表导入
    public String importBaseFiveNonstandEntity( @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,  HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            List<BaseFiveNonstandEntity> baseFiveNonstandEntities = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 1,  BaseFiveNonstandEntity.class, true, null);
            if (CollectionUtil.isEmpty(baseFiveNonstandEntities)) {
                return updateFailure("导入文件内容不能为空，请检查后重试！ ");
            }
            Set<String> dateSet = new HashSet<>();
            for (BaseFiveNonstandEntity baseFiveNonstandEntity : baseFiveNonstandEntities) {
                dateSet.add(baseFiveNonstandEntity.getPeriod());
            }
            if (dateSet.size() > 1) {
                return updateFailure("导入文件中【报告期】包含多天，请检查后重试！ ");
            }
            params.put("period", dateSet.stream().iterator().next());
            baseFiveNonstandEntityService.importBaseFiveNonstandEntity(baseFiveNonstandEntities, params);
        } catch (Exception e) {
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/importBaseFiveNonstandFintech.json",produces = { "application/json;charset=UTF-8"})
    // 非标融资主体行业表导入
    public String importBaseFiveNonstandFintech( @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,  HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            List<BaseFiveNonstandFintech> baseFiveNonstandFinteches = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 1,  BaseFiveNonstandFintech.class, true, null);
            if (CollectionUtil.isEmpty(baseFiveNonstandFinteches)) {
                return updateFailure("导入文件内容不能为空，请检查后重试！ ");
            }
            Set<String> dateSet = new HashSet<>();
            for (BaseFiveNonstandFintech baseFiveNonstandFintech : baseFiveNonstandFinteches) {
                dateSet.add(baseFiveNonstandFintech.getPeriod());
            }
            if (dateSet.size() > 1) {
                return updateFailure("导入文件中【报告期】包含多天，请检查后重试！ ");
            }
            params.put("period", dateSet.stream().iterator().next());
            baseFiveNonstandFintechService.importBaseFiveNonstandFintech(baseFiveNonstandFinteches, params);
        } catch (Exception e) {
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/importDwsZyScrThemeIndInf.json",produces = { "application/json;charset=UTF-8"})
    // 自营资产打标中间表导入
    public String importDwsZyScrThemeIndInf( @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,  HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", request.getParameter("reportDate"));
            List<DwsZyScrThemeIndInf> dwsZyScrThemeIndInfs = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 1,  DwsZyScrThemeIndInf.class, true, null);
            if (CollectionUtil.isEmpty(dwsZyScrThemeIndInfs)) {
                return updateFailure("导入文件内容不能为空，请检查后重试！ ");
            }
            for (DwsZyScrThemeIndInf dwsZyScrThemeIndInf : dwsZyScrThemeIndInfs) {
                dwsZyScrThemeIndInf.setAssDebtType(StringUtils.isEmpty(dwsZyScrThemeIndInf.getAssDebtType())?null:dwsZyScrThemeIndInf.getAssDebtType().split(" ")[0]);
                dwsZyScrThemeIndInf.setScrTrm(StringUtils.isEmpty(dwsZyScrThemeIndInf.getScrTrm())?null:dwsZyScrThemeIndInf.getScrTrm().split(" ")[0]);
                dwsZyScrThemeIndInf.setRateLevel(StringUtils.isEmpty(dwsZyScrThemeIndInf.getRateLevel())?null:dwsZyScrThemeIndInf.getRateLevel().split(" ")[0]);
                dwsZyScrThemeIndInf.setSpcBondF(StringUtils.isEmpty(dwsZyScrThemeIndInf.getSpcBondF())?null:dwsZyScrThemeIndInf.getSpcBondF().split(" ")[0]);
                dwsZyScrThemeIndInf.setIsoverdue(StringUtils.isEmpty(dwsZyScrThemeIndInf.getIsoverdue())?null:dwsZyScrThemeIndInf.getIsoverdue().split(" ")[0]);
                dwsZyScrThemeIndInf.setIsfintech(StringUtils.isEmpty(dwsZyScrThemeIndInf.getIsfintech())?null:dwsZyScrThemeIndInf.getIsfintech().split(" ")[0]);
                dwsZyScrThemeIndInf.setIsgreen(StringUtils.isEmpty(dwsZyScrThemeIndInf.getIsgreen())?null:dwsZyScrThemeIndInf.getIsgreen().split(" ")[0]);
                dwsZyScrThemeIndInf.setIsinclusive(StringUtils.isEmpty(dwsZyScrThemeIndInf.getIsinclusive())?null:dwsZyScrThemeIndInf.getIsinclusive().split(" ")[0]);
                dwsZyScrThemeIndInf.setIspension(StringUtils.isEmpty(dwsZyScrThemeIndInf.getIspension())?null:dwsZyScrThemeIndInf.getIspension().split(" ")[0]);
                dwsZyScrThemeIndInf.setIsdigital(StringUtils.isEmpty(dwsZyScrThemeIndInf.getIsdigital())?null:dwsZyScrThemeIndInf.getIsdigital().split(" ")[0]);
                dwsZyScrThemeIndInf.setIspollution(StringUtils.isEmpty(dwsZyScrThemeIndInf.getIspollution())?null:dwsZyScrThemeIndInf.getIspollution().split(" ")[0]);
                dwsZyScrThemeIndInf.setFintechTyp5(StringUtils.isEmpty(dwsZyScrThemeIndInf.getFintechTyp5())?null:dwsZyScrThemeIndInf.getFintechTyp5().split(" ")[0]);
                dwsZyScrThemeIndInf.setFintechTyp6(StringUtils.isEmpty(dwsZyScrThemeIndInf.getFintechTyp6())?null:dwsZyScrThemeIndInf.getFintechTyp6().split(" ")[0]);
                dwsZyScrThemeIndInf.setFintechTyp7(StringUtils.isEmpty(dwsZyScrThemeIndInf.getFintechTyp7())?null:dwsZyScrThemeIndInf.getFintechTyp7().split(" ")[0]);
                dwsZyScrThemeIndInf.setFintechTyp8(StringUtils.isEmpty(dwsZyScrThemeIndInf.getFintechTyp8())?null:dwsZyScrThemeIndInf.getFintechTyp8().split(" ")[0]);
            }
            dwsZyScrThemeIndInfService.importDwsZyScrThemeIndInf(dwsZyScrThemeIndInfs, params);
        } catch (Exception e) {
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/custVolRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资者持有信息导入
    public String custVolRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "投资者持有信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_cust_vol_register_info",0);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "投资者持有信息导入失败，文件与投资者持有信息模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", DateUtil.getSysWordDay());
//            params.put("reportDate", request.getParameter("reportDate"));
            List<TrCustVolRegisterInfo> custVolRegisterInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrCustVolRegisterInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_cust_vol_register_info");
            if(custVolRegisterInfos == null || custVolRegisterInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "投资者持有信息导入失败，请先确认数据不可为空", null).toString();
            }else if (custVolRegisterInfos.size() < dataStartRow+1) {
                return RequestSupport.updateReturnJson(false, "投资者持有信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            custVolRegisterInfos.remove(0);
            //i值 计算了表头行
            int i = dataStartRow;
            for (TrCustVolRegisterInfo custVolRegisterInfo : custVolRegisterInfos) {
                i++;
                String cur = custVolRegisterInfo.getCur();
                custVolRegisterInfo.setCur(StringUtils.isEmpty(cur) ? null : cur.split(" ")[0]);
                String holdDate = StringUtils.isEmpty(custVolRegisterInfo.getHoldDate()) ? "" : custVolRegisterInfo.getHoldDate().replace("-", "");
                custVolRegisterInfo.setHoldDate(holdDate);
                custVolRegisterInfo.setTheoryReportStartDate(holdDate);
                custVolRegisterInfo.setReportDate(holdDate);
                //校验数据
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErr = checkDataForVueService.trCustVolRegisterInfoCheckForVue(whiteregex, whitereForCode, custVolRegisterInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n" + "第" + i + "行" + checkErr, null).toString();
                }

            }
            trCustVolRegisterInfoService.importCustVolRegisterInfo(custVolRegisterInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/custVolRegistUpdateImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资者持有信息批量修改导入  登记表
    public String custVolRegistUpdateImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "投资者持有信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_cust_vol_register_info",0);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "投资者持有信息批量修改导入失败，文件与投资者持有信息模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", request.getParameter("reportDate"));
            List<TrCustVolRegisterInfo> custVolRegisterInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrCustVolRegisterInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_cust_vol_register_info");
            if(custVolRegisterInfos == null || custVolRegisterInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "投资者持有信息批量修改导入失败，请先确认数据不可为空", null).toString();
            }else if(custVolRegisterInfos != null && custVolRegisterInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "投资者持有信息批量修改导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            custVolRegisterInfos.remove(0);
            for (TrCustVolRegisterInfo custVolRegisterInfo : custVolRegisterInfos) {
                String cur = custVolRegisterInfo.getCur();
                custVolRegisterInfo.setCur(StringUtils.isEmpty(cur) ? null : cur.split(" ")[0]);
                custVolRegisterInfo.setTheoryReportStartDate(StringUtils.isEmpty(custVolRegisterInfo.getHoldDate()) ? null : custVolRegisterInfo.getHoldDate().replace("-", ""));
                //校验数据
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErr = checkDataForVueService.trCustVolRegisterInfoCheckForVue(whiteregex, whitereForCode, custVolRegisterInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n" + checkErr, null).toString();
                }

            }
            trCustVolRegisterInfoService.updateImportCustVolRegisterInfo(custVolRegisterInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/custVolRegistModifyImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资者持有信息变更导入  历史表 app_cust_vol_register_info_modify
    public String custVolRegistModifyImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<TrCustVolRegisterInfo> custVolRegisterInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrCustVolRegisterInfo.class, true, null);

            // 移除第一标题行
            custVolRegisterInfos.remove(0);
            for (TrCustVolRegisterInfo custVolRegisterInfo : custVolRegisterInfos) {
                String cur = custVolRegisterInfo.getCur();
                String holdDate = StringUtils.isEmpty(custVolRegisterInfo.getHoldDate()) ? null : custVolRegisterInfo.getHoldDate().replace("-", "");
                custVolRegisterInfo.setCur(StringUtils.isEmpty(cur) ? null : cur.split(" ")[0]);
                custVolRegisterInfo.setTheoryReportStartDate(holdDate);
                custVolRegisterInfo.setReportDate(holdDate);
            }
            trCustVolRegisterInfoService.importModifyCustVolRegisterInfo(custVolRegisterInfos);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/custTransImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资者明细信息导入
    public String custTransImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "投资者明细信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_cust_trans_info",0);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "投资者明细信息导入失败，文件与投资者明细信息模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            String sysWorkDay = DateUtil.getSysWordDay();
            checkDataParams.initData();
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", sysWorkDay);
            List<TrCustTransInfo> custTransInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrCustTransInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_cust_trans_info");
            if(custTransInfos == null || custTransInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "投资者明细信息导入失败，请先确认数据不可为空", null).toString();
            }else if (custTransInfos.size() < dataStartRow+1) {
                return RequestSupport.updateReturnJson(false, "投资者明细信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            custTransInfos.remove(0);
            // 处理数据  i值计算了表头行
            int i = dataStartRow;
            for (TrCustTransInfo custTransInfo : custTransInfos) {
                i++;
                //导入时的 组合字典值（“01 柜面”）校验
                String checkErrImport = checkDataForVueService.trCustTransInfoCheckDictForImport(custTransInfo);
                //校验前赋值 需要处理的数据
                custTransInfo.setAckTime(StringUtils.isEmpty(custTransInfo.getAckTime()) ? null : custTransInfo.getAckTime().replace(":", ""));
                custTransInfo.setAckDate(StringUtils.isEmpty(custTransInfo.getAckDate()) ? null : custTransInfo.getAckDate().replace("-", ""));
                custTransInfo.setAcctLocCode(StringUtils.isEmpty(custTransInfo.getAcctLocCode()) ? null : custTransInfo.getAcctLocCode().split(" ")[0]);
                custTransInfo.setIsAgent(StringUtils.isEmpty(custTransInfo.getIsAgent()) ? null : custTransInfo.getIsAgent().split(" ")[0]);
                custTransInfo.setAgentReguCode(StringUtils.isEmpty(custTransInfo.getAgentReguCode()) ? null : custTransInfo.getAgentReguCode().split(" ")[0]);
                custTransInfo.setBusiCode(StringUtils.isEmpty(custTransInfo.getBusiCode()) ? null : custTransInfo.getBusiCode().split(" ")[0]);
                custTransInfo.setBusiReguCode(StringUtils.isEmpty(custTransInfo.getBusiReguCode()) ? null : custTransInfo.getBusiReguCode().split(" ")[0]);
                custTransInfo.setChannelFlag(StringUtils.isEmpty(custTransInfo.getChannelFlag()) ? null : custTransInfo.getChannelFlag().split(" ")[0]);
                custTransInfo.setSpeChannelFlag(StringUtils.isEmpty(custTransInfo.getSpeChannelFlag()) ? null : custTransInfo.getSpeChannelFlag().split(" ")[0]);
                custTransInfo.setCur(StringUtils.isEmpty(custTransInfo.getCur()) ? null : custTransInfo.getCur().split(" ")[0]);
//                custTransInfo.setTheoryReportStartDate(StringUtils.isEmpty(ackDate)?null:ackDate.replace("-",""));
//                custTransInfo.setReportDate(StringUtils.isEmpty(ackDate)?null:ackDate.replace("-",""));
                custTransInfo.setTheoryReportStartDate(sysWorkDay);
                custTransInfo.setReportDate(sysWorkDay);

                //校验数据
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErr = checkDataForVueService.trCustTransInfoCheckForVue(whiteregex, whitereForCode, custTransInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr+checkErrImport)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n" + "第" + i + "行" + checkErr+checkErrImport, null).toString();
                }

            }
            trCustTransInfoService.importCustTransInfo(custTransInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/custTransUpdateImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资者明细信息批量修改导入  当前表
    public String custTransUpdateImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "投资者明细信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_cust_trans_info",0);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "投资者明细信息批量修改导入失败，文件与投资者明细信息模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", request.getParameter("reportDate"));
            List<TrCustTransInfo> custTransInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrCustTransInfo.class, true, null);
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_cust_trans_info");
            if(custTransInfos == null || custTransInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "投资者明细信息批量修改导入失败，请先确认数据不可为空", null).toString();
            }else if(custTransInfos != null && custTransInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "投资者明细信息批量修改导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            custTransInfos.remove(0);
            // 处理数据
            for (TrCustTransInfo custTransInfo : custTransInfos) {
                String speChannelFlag = custTransInfo.getSpeChannelFlag();
                //导入时的 组合字典值（“01 柜面”）校验
                String checkErrImport = checkDataForVueService.trCustTransInfoCheckDictForImport(custTransInfo);
                custTransInfo.setAckTime(StringUtils.isEmpty(custTransInfo.getAckTime()) ? null : custTransInfo.getAckTime().replace(":", ""));
                custTransInfo.setAckDate(StringUtils.isEmpty(custTransInfo.getAckDate()) ? null : custTransInfo.getAckDate().replace("-", ""));
                custTransInfo.setAcctLocCode(StringUtils.isEmpty(custTransInfo.getAcctLocCode()) ? null : custTransInfo.getAcctLocCode().split(" ")[0]);
                custTransInfo.setIsAgent(StringUtils.isEmpty(custTransInfo.getIsAgent()) ? null : custTransInfo.getIsAgent().split(" ")[0]);
                custTransInfo.setAgentReguCode(StringUtils.isEmpty(custTransInfo.getAgentReguCode()) ? null : custTransInfo.getAgentReguCode().split(" ")[0]);
                custTransInfo.setBusiCode(StringUtils.isEmpty(custTransInfo.getBusiCode()) ? null : custTransInfo.getBusiCode().split(" ")[0]);
                custTransInfo.setBusiReguCode(StringUtils.isEmpty(custTransInfo.getBusiReguCode()) ? null : custTransInfo.getBusiReguCode().split(" ")[0]);
                custTransInfo.setChannelFlag(StringUtils.isEmpty(custTransInfo.getChannelFlag()) ? null : custTransInfo.getChannelFlag().split(" ")[0]);
                custTransInfo.setCur(StringUtils.isEmpty(custTransInfo.getCur()) ? null : custTransInfo.getCur().split(" ")[0]);
                custTransInfo.setTheoryReportStartDate(StringUtils.isEmpty(custTransInfo.getAckTime()) ? null : custTransInfo.getAckTime().replace("-", ""));
                custTransInfo.setSpeChannelFlag(StringUtils.isEmpty(speChannelFlag) ? null : speChannelFlag.split(" ")[0]);
                //校验数据
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErr = checkDataForVueService.trCustTransInfoCheckForVue(whiteregex, whitereForCode, custTransInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr+checkErrImport)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n" + checkErr+checkErrImport, null).toString();
                }

            }
            trCustTransInfoService.updateImportCustTransInfo(custTransInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/custTransModifyImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资者明细信息变更导入    历史表 app_cust_trans_info_modify
    public String custTransModifyImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<TrCustTransInfo> custTransInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrCustTransInfo.class, true, null);
            // 移除第一标题行
            custTransInfos.remove(0);
            // 处理数据
            for (TrCustTransInfo custTransInfo : custTransInfos) {
                String acctLocCode = custTransInfo.getAcctLocCode();
                String isAgent = custTransInfo.getIsAgent();
                String agentReguCode = custTransInfo.getAgentReguCode();
                String busiCode = custTransInfo.getBusiCode();
                String busiReguCode = custTransInfo.getBusiReguCode();
                String cur = custTransInfo.getCur();
                String speChannelFlag = custTransInfo.getSpeChannelFlag();
                String channelFlag = custTransInfo.getChannelFlag();
                String ackDate = custTransInfo.getAckDate();
                String ackTime = custTransInfo.getAckTime();
                String ackDate1 = StringUtils.isEmpty(ackDate) ? null : ackDate.replace("-", "");
                custTransInfo.setAckTime(StringUtils.isEmpty(ackTime) ? null : ackTime.replace(":", ""));
                custTransInfo.setAckDate(StringUtils.isEmpty(ackDate) ? null : ackDate.replace("-", ""));
                custTransInfo.setAcctLocCode(StringUtils.isEmpty(acctLocCode) ? null : acctLocCode.split(" ")[0]);
                custTransInfo.setIsAgent(StringUtils.isEmpty(isAgent) ? null : isAgent.split(" ")[0]);
                custTransInfo.setAgentReguCode(StringUtils.isEmpty(agentReguCode) ? null : agentReguCode.split(" ")[0]);
                custTransInfo.setBusiCode(StringUtils.isEmpty(busiCode) ? null : busiCode.split(" ")[0]);
                custTransInfo.setBusiReguCode(StringUtils.isEmpty(busiReguCode) ? null : busiReguCode.split(" ")[0]);
                custTransInfo.setChannelFlag(StringUtils.isEmpty(channelFlag) ? null : channelFlag.split(" ")[0]);
                custTransInfo.setCur(StringUtils.isEmpty(cur) ? null : cur.split(" ")[0]);
                custTransInfo.setSpeChannelFlag(StringUtils.isEmpty(speChannelFlag) ? null : speChannelFlag.split(" ")[0]);
                custTransInfo.setTheoryReportStartDate(ackDate1);
                custTransInfo.setReportDate(ackDate1);
            }
            trCustTransInfoService.importModifyCustTransInfo(custTransInfos);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/invSubHoldModifyImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资者持有信息登记（子产品）变更导入
    public String invSubHoldModifyImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<InvestorSubHoldInfo> investorSubHoldInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, InvestorSubHoldInfo.class, true, null);
            // 移除第一标题行
            investorSubHoldInfos.remove(0);
            // 处理数据
            for (InvestorSubHoldInfo investorSubHoldInfo : investorSubHoldInfos) {
                String cur = investorSubHoldInfo.getCur();
                String custType = investorSubHoldInfo.getCustType();
                String personalIdType = investorSubHoldInfo.getPersonalIdType();
                String holdDate = StringUtils.isEmpty(investorSubHoldInfo.getHoldDate()) ? null : investorSubHoldInfo.getHoldDate().replace("-", "");
                String organizationIdType = investorSubHoldInfo.getOrganizationIdType();
                investorSubHoldInfo.setCur(StringUtils.isEmpty(cur) ? null : cur.split(" ")[0]);
                investorSubHoldInfo.setCustType(StringUtils.isEmpty(custType) ? null : custType.split(" ")[0]);
                investorSubHoldInfo.setPersonalIdType(StringUtils.isEmpty(personalIdType) ? null : personalIdType.split(" ")[0]);
                investorSubHoldInfo.setOrganizationIdType(StringUtils.isEmpty(organizationIdType) ? null : organizationIdType.split(" ")[0]);
                investorSubHoldInfo.setReportDate(holdDate);
            }
            investorSubHoldService.importModifyInvSubHoldInfo(investorSubHoldInfos);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/custRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资者身份登记信息导入
    public String custRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "投资者身份登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_cust_register_info",0);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "投资者身份登记信息导入失败，文件与投资者身份信息模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", request.getParameter("reportDate"));
            List<TrCustRegisterInfo> custRegisterInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrCustRegisterInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_cust_register_info");
            if(custRegisterInfos == null || custRegisterInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "投资者身份登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(custRegisterInfos != null && custRegisterInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "投资者身份登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            custRegisterInfos.remove(0);
            // 数据处理   i值 计算了表头行
            int i = dataStartRow;
            for (TrCustRegisterInfo custRegisterInfo : custRegisterInfos) {
                i++;
                String isBelong = custRegisterInfo.getIsBelong();
                String inOutSign = custRegisterInfo.getInOutSign();
                String issCountry = custRegisterInfo.getIssCountry();
                String dataType = custRegisterInfo.getDataType();
                String custType = custRegisterInfo.getCustType();
                String personalIdType = custRegisterInfo.getPersonalIdType();
                String organizationIdType = custRegisterInfo.getOrganizationIdType();
                String spvOpenBank = custRegisterInfo.getSpvOpenBank();
                String sex = custRegisterInfo.getSex();
                String riskLevel = custRegisterInfo.getRiskLevel();
                String moble = custRegisterInfo.getMoble();
                String idCode = custRegisterInfo.getIdCode();
                String telPhone = custRegisterInfo.getTelPhone();
                String checkErrImport = checkDataForVueService.trCustRegisterInfoDictCheckForImport(custRegisterInfo);

                // 数据字典处理
                custRegisterInfo.setIsBelong(StringUtils.isEmpty(isBelong) ? null : isBelong.split(" ")[0]);
                custRegisterInfo.setInOutSign(StringUtils.isEmpty(inOutSign) ? null : inOutSign.split(" ")[0]);
                custRegisterInfo.setIssCountry(StringUtils.isEmpty(issCountry) ? null : issCountry.split(" ")[0]);
                custRegisterInfo.setDataType(StringUtils.isEmpty(dataType) ? null : dataType.split(" ")[0]);
                custRegisterInfo.setCustType(StringUtils.isEmpty(custType) ? null : custType.split(" ")[0]);
                custRegisterInfo.setPersonalIdType(StringUtils.isEmpty(personalIdType) ? null : personalIdType.split(" ")[0]);
                custRegisterInfo.setOrganizationIdType(StringUtils.isEmpty(organizationIdType) ? null : organizationIdType.split(" ")[0]);
                custRegisterInfo.setSpvOpenBank(StringUtils.isEmpty(spvOpenBank) ? null : spvOpenBank.split(" ")[0]);
                custRegisterInfo.setSex(StringUtils.isEmpty(sex) ? null : sex.split(" ")[0]);
                custRegisterInfo.setRiskLevel(StringUtils.isEmpty(riskLevel) ? null : riskLevel.split(" ")[0]);
                custRegisterInfo.setReportDate(String.valueOf(params.get("reportDate")));
                custRegisterInfo.setTheoryReportStartDate(String.valueOf(params.get("reportDate")));
                custRegisterInfo.setMoble(StringUtils.isEmpty(moble) ? null : reverseToString(moble));
                custRegisterInfo.setTelPhone(StringUtils.isEmpty(telPhone) ? null : telPhone);
                if(!StringUtils.isEmpty(telPhone)){
                    custRegisterInfo.setTelPhone(telPhone.indexOf("E")!=-1?reverseToString(telPhone):telPhone);
                }
                //校验数据
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErr = checkDataForVueService.trCustRegisterInfoCheckForVue(whiteregex, whitereForCode, custRegisterInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr+checkErrImport)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n" + "第" + i + "行" + checkErr+checkErrImport, null).toString();
                }
            }
            trCustRegisterInfoService.importCustRegisterInfo(custRegisterInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/custRegistUpdateImport.json", produces = {"application/json;charset=UTF-8"})
    // 投资人身份登记信息批量修改导入  当前表
    public String custRegistUpdateImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "投资人身份登记信息批量修改导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_cust_register_info",0);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "投资人身份登记信息批量修改导入失败，文件与投资者身份信息模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            Map<String, Object> params = new HashMap<>();
            params.put("reportDate", request.getParameter("reportDate"));
            List<TrCustRegisterInfo> custRegisterInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrCustRegisterInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_cust_register_info");
            if(custRegisterInfos == null || custRegisterInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "投资人身份登记信息批量修改导入失败，请先确认数据不可为空", null).toString();
            }else if(custRegisterInfos != null && custRegisterInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "投资人身份登记信息批量修改导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            custRegisterInfos.remove(0);
            // 数据处理 i值 计算了表头行
            int i = dataStartRow;
            for (TrCustRegisterInfo custRegisterInfo : custRegisterInfos) {
                i++;
                String isBelong = custRegisterInfo.getIsBelong();
                String inOutSign = custRegisterInfo.getInOutSign();
                String issCountry = custRegisterInfo.getIssCountry();
                String dataType = custRegisterInfo.getDataType();
                String custType = custRegisterInfo.getCustType();
                String personalIdType = custRegisterInfo.getPersonalIdType();
                String organizationIdType = custRegisterInfo.getOrganizationIdType();
                String spvOpenBank = custRegisterInfo.getSpvOpenBank();
                String sex = custRegisterInfo.getSex();
                String riskLevel = custRegisterInfo.getRiskLevel();
                String moble = custRegisterInfo.getMoble();
                String idCode = custRegisterInfo.getIdCode();
                String telPhone = custRegisterInfo.getTelPhone();
                String checkErrImport = checkDataForVueService.trCustRegisterInfoDictCheckForImport(custRegisterInfo);
                // 数据字典处理
                custRegisterInfo.setIsBelong(StringUtils.isEmpty(isBelong) ? null : isBelong.split(" ")[0]);
                custRegisterInfo.setInOutSign(StringUtils.isEmpty(inOutSign) ? null : inOutSign.split(" ")[0]);
                custRegisterInfo.setIssCountry(StringUtils.isEmpty(issCountry) ? null : issCountry.split(" ")[0]);
                custRegisterInfo.setDataType(StringUtils.isEmpty(dataType) ? null : dataType.split(" ")[0]);
                custRegisterInfo.setCustType(StringUtils.isEmpty(custType) ? null : custType.split(" ")[0]);
                custRegisterInfo.setPersonalIdType(StringUtils.isEmpty(personalIdType) ? null : personalIdType.split(" ")[0]);
                custRegisterInfo.setOrganizationIdType(StringUtils.isEmpty(organizationIdType) ? null : organizationIdType.split(" ")[0]);
                custRegisterInfo.setSpvOpenBank(StringUtils.isEmpty(spvOpenBank) ? null : spvOpenBank.split(" ")[0]);
                custRegisterInfo.setSex(StringUtils.isEmpty(sex) ? null : sex.split(" ")[0]);
                custRegisterInfo.setRiskLevel(StringUtils.isEmpty(riskLevel) ? null : riskLevel.split(" ")[0]);
                custRegisterInfo.setReportDate(String.valueOf(params.get("reportDate")));
                custRegisterInfo.setMoble(StringUtils.isEmpty(moble) ? null : moble);
                custRegisterInfo.setTelPhone(StringUtils.isEmpty(telPhone) ? null : telPhone);
                //校验数据
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErr = checkDataForVueService.trCustRegisterInfoCheckForVue(whiteregex, whitereForCode, custRegisterInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr+checkErrImport)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n" + "第" + i + "行" + checkErr+checkErrImport, null).toString();
                }
            }
            trCustRegisterInfoService.updateImportCustRegisterInfo(custRegisterInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/reportTimeConfigImport.json", produces = {"application/json;charset=UTF-8"})
    //报送时点配置导入
    public String reportTimeConfigImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            List<ReportTimeConfigInfo> list = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportTimeConfigInfo.class, true, null);
            // 移除第一标题行
            list.remove(0);
            // 数据处理
            for (ReportTimeConfigInfo item : list) {
                String reportType = item.getReportType();
                String reportTable = item.getReportTable();
                String baseType = item.getBaseType();

                // 数据字典处理
                item.setReportType(StringUtils.isEmpty(reportType) ? null : reportType.split(" ")[0]);
                item.setReportTable(StringUtils.isEmpty(reportTable) ? null : reportTable.split(" ")[0]);
                item.setBaseType(StringUtils.isEmpty(baseType) ? null : baseType.split(" ")[0]);
            }
            reportTimeConfigImportService.importReportTimeConfigList(list);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/terminationRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 产品终止信息导入
    public String terminationRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();

            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "产品终止登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_termination_regist_info",0);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "产品终止登记信息导入失败，文件与产品终止登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;

            params.put("reportDate", request.getParameter("reportDate"));
            List<TrTerminationRegistInfo> terminationRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrTerminationRegistInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_termination_regist_info");
            if(terminationRegistInfos == null || terminationRegistInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "产品终止登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(terminationRegistInfos != null && terminationRegistInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "产品终止登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            terminationRegistInfos.remove(0);
            // 处理数据
            int i =dataStartRow;
            for (TrTerminationRegistInfo terminationRegistInfo : terminationRegistInfos) {
                i++;
                terminationRegistInfo.setAnnualReturnClient(StringUtils.isEmpty(terminationRegistInfo.getAnnualReturnClientStr()) ? null : Double.valueOf(reverseToString(terminationRegistInfo.getAnnualReturnClientStr())));
                terminationRegistInfo.setAnnualReturnProd(StringUtils.isEmpty(terminationRegistInfo.getAnnualReturnProdStr()) ? null : Double.valueOf(reverseToString(terminationRegistInfo.getAnnualReturnProdStr())));

                String checkErr = checkDataForInfoService.terminationRegistInfoCheckInfo(whiteregex, whitereForCode, terminationRegistInfo);
                if (StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！\n第"+i+"行:" + terminationRegistInfo.getProdCode() + "所在行的错误信息为：\n" + checkErr, null).toString();
                }

                String actualProdTerDate = terminationRegistInfo.getActualProdTerDate();
                String interestPayment = terminationRegistInfo.getInterestPayment();
                String payment = terminationRegistInfo.getPayment();
                String deliveredVol = terminationRegistInfo.getDeliveredVol();
                String otherManageFee = terminationRegistInfo.getOtherManageFee();
                String otherSalesComm = terminationRegistInfo.getOtherSalesComm();
                String consultFee = terminationRegistInfo.getConsultFee();
                String otherProdFee = terminationRegistInfo.getOtherProdFee();
                terminationRegistInfo.setActualProdTerDate(StringUtils.isEmpty(actualProdTerDate)?null:actualProdTerDate.replace("-",""));
                terminationRegistInfo.setRegisterStatus(StringUtils.isEmpty(terminationRegistInfo.getRegisterStatus())?null:terminationRegistInfo.getRegisterStatus().split(" ")[0]);
                terminationRegistInfo.setAnnualReturnClient(StringUtils.isEmpty(terminationRegistInfo.getAnnualReturnClientStr())?null:Double.valueOf(reverseToString(terminationRegistInfo.getAnnualReturnClientStr())));
                terminationRegistInfo.setAnnualReturnProd(StringUtils.isEmpty(terminationRegistInfo.getAnnualReturnProdStr())?null:Double.valueOf(reverseToString(terminationRegistInfo.getAnnualReturnProdStr())));
                terminationRegistInfo.setInterestPayment(StringUtils.isEmpty(interestPayment)?null:reverseToString(interestPayment));
                terminationRegistInfo.setPayment(StringUtils.isEmpty(payment)?null:reverseToString(payment));
                terminationRegistInfo.setDeliveredVol(StringUtils.isEmpty(deliveredVol)?null:reverseToString(deliveredVol));
                terminationRegistInfo.setOtherManageFee(StringUtils.isEmpty(otherManageFee)?null:reverseToString(otherManageFee));
                terminationRegistInfo.setOtherSalesComm(StringUtils.isEmpty(otherSalesComm)?null:reverseToString(otherSalesComm));
                terminationRegistInfo.setConsultFee(StringUtils.isEmpty(consultFee)?null:reverseToString(consultFee));
                terminationRegistInfo.setOtherProdFee(StringUtils.isEmpty(otherProdFee)?null:reverseToString(otherProdFee));
                terminationRegistInfo.setReportDate(String.valueOf(params.get("reportDate")));
            }
            terminationRegistInfoService.importTrTerminationRegistInfo(terminationRegistInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }


    @RequestMapping(value = "/underAssetRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 底层持仓信息导入
    public String underAssetRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();

            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "底层资产持仓登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_under_asset_regist_info",0);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "底层资产持仓登记信息导入失败，文件与底层资产持仓登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }


            params.put("beginDate", request.getParameter("beginDate"));
            params.put("queryDate", request.getParameter("queryDate"));
            List<UnderAssetRegistInfo> underAssetRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, UnderAssetRegistInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_under_asset_regist_info");
            if(underAssetRegistInfos == null || underAssetRegistInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "底层资产持仓登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(underAssetRegistInfos != null && underAssetRegistInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "底层资产持仓登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            underAssetRegistInfos.remove(0);
            int i =dataStartRow;
            for (UnderAssetRegistInfo underAssetRegistInfo : underAssetRegistInfos) {
                i++;
                //underAssetRegistInfo.setRegisterStatus(StringUtils.isEmpty(underAssetRegistInfo.getRegisterStatus())?null:underAssetRegistInfo.getRegisterStatus().split(" ")[0]);
                underAssetRegistInfo.setReportDate(StringUtils.isEmpty(underAssetRegistInfo.getReportDate()) ? null : underAssetRegistInfo.getReportDate().replace("-", ""));
                underAssetRegistInfo.setImpDate(StringUtils.isEmpty(underAssetRegistInfo.getImpDate()) ? null : underAssetRegistInfo.getImpDate().replace("-", ""));
                underAssetRegistInfo.setTheoryReportStartDate(StringUtils.isEmpty(underAssetRegistInfo.getReportDate()) ? null : underAssetRegistInfo.getReportDate().replace("-", ""));


                //校验数据
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErr = checkDataForVueService.underAssetRegistInfoCheckForVue(whiteregex, whitereForCode, underAssetRegistInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n第"+i+"行:" + checkErr, null).toString();
                }
            }
            underAssetRegistInfoService.importUnderAssetRegistInfo(underAssetRegistInfos,params);


        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/assetRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 资产持仓信息导入
    public String assetRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> parameters = RequestSupport.getParameters();
            String assetimportmodel = parameters.get("assetimportmodel").toString();
            if(assetimportmodel == null ||"".equals(assetimportmodel)){
                return RequestSupport.updateReturnJson(false, "资产持仓登记信息导入失败，请先选择导入类型！",null).toString();
            }
            Map<String, Object> params = new HashMap<>();

            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "资产持仓登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_asset_regist_info",1);;
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "资产持仓登记信息导入失败，文件与资产持仓登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
//            params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//            params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_asset_regist_info");
            //仅读取一行表头及全部数据
            List<AssetRegistInfo> assetRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, dataStartRow-1, dataStartRow-1, AssetRegistInfo.class, true, null);
            if(assetRegistInfos == null || assetRegistInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "资产持仓登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(assetRegistInfos != null && assetRegistInfos.size() < dataStartRow){
                return RequestSupport.updateReturnJson(false, "资产持仓登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            assetRegistInfos.remove(0);
            int i=dataStartRow;
            for (AssetRegistInfo assetRegistInfo : assetRegistInfos) {
                i++;
                assetRegistInfo.setHoldingType(StringUtils.isEmpty(assetRegistInfo.getHoldingType()) ? null : assetRegistInfo.getHoldingType().split(" ")[0]);
                assetRegistInfo.setInvestedAsset(StringUtils.isEmpty(assetRegistInfo.getInvestedAsset()) ? null : assetRegistInfo.getInvestedAsset().split(" ")[0]);
                String holdDate = StringUtils.isEmpty(assetRegistInfo.getHoldingDate()) ? "" : assetRegistInfo.getHoldingDate().replace("-", "");
                params.put("theoryReportStartDate",holdDate);
                assetRegistInfo.setHoldingDate(holdDate);
//                assetRegistInfo.setImpDate(StringUtils.isEmpty(assetRegistInfo.getImpDate())?null:assetRegistInfo.getImpDate().replace("-",""));
                assetRegistInfo.setTheoryReportStartDate(holdDate);
                assetRegistInfo.setReportDate(holdDate);
                //校验数据
                checkDataParams.initDataNoDict();
                String whiteregex = CheckDataParams.whiteregex;
                String whitereForCode = CheckDataParams.whitereForCode;
                String checkErr = checkDataForVueService.assetRegistInfoCheckForVue(whiteregex, whitereForCode, assetRegistInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n第"+i+"行:" + checkErr, null).toString();
                }
            }
            if(assetimportmodel.equals("01")){
                //"01"增量导入
                assetRegistInfoService.addCoverImportAssetRegistInfo(assetRegistInfos,params);
            }else{
                //"02"全量导入
                assetRegistInfoService.importAssetRegistInfo(assetRegistInfos,params);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/prodStatusRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 产品状态信息导入
    public String prodStatusRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "产品状态登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            //传1读取的excel第二行
            String isCheck = compareHeaderService.compare(file, "app_prod_state_regist_info",1);
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "产品状态登记信息导入失败，文件与产品状态登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;

            Map<String, Object> params = new HashMap<>();
//            params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//            params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
            //表头所在第几行，以1为初始值
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_prod_state_regist_info");
            //两行表头，一行数据：此处读取到 2条：一条表头行，一行数据记录        ；一行表头，一行数据，此处读取到2条：一条表头行，一行数据记录
            List<ProdStateRegistInfo> prodStateRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, dataStartRow-1, dataStartRow-1, ProdStateRegistInfo.class, true, null);
//            dataStartRow = 1;
            if(prodStateRegistInfos == null || prodStateRegistInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "产品状态登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(prodStateRegistInfos != null && prodStateRegistInfos.size() < dataStartRow){
                return RequestSupport.updateReturnJson(false, "产品状态登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            prodStateRegistInfos.remove(0);
            int i = dataStartRow;
            for (ProdStateRegistInfo prodStateRegistInfo : prodStateRegistInfos) {
                i++;
                if(i==dataStartRow+1){ //取的prodStateRegistInfos中第一行数据
                    params.put("theoryReportStartDate", StringUtils.isEmpty(prodStateRegistInfo.getValdate()) ? DateUtil.getLastMonthEndDay() : prodStateRegistInfo.getValdate().replace("-", ""));
                }
//                prodStateRegistInfo.setImpDate(StringUtils.isEmpty(prodStateRegistInfo.getImpDate())?null:prodStateRegistInfo.getImpDate().replace("-",""));
                prodStateRegistInfo.setTheoryReportStartDate(StringUtils.isEmpty(prodStateRegistInfo.getValdate()) ? DateUtil.getLastMonthEndDay() : prodStateRegistInfo.getValdate().replace("-", ""));
//                prodStateRegistInfo.setTheoryReportStartDate(String.valueOf(params.get("beginDate")));
                prodStateRegistInfo.setValdate(StringUtils.isEmpty(prodStateRegistInfo.getValdate()) ? "" : prodStateRegistInfo.getValdate().replace("-", ""));
                String checkErr = checkDataForVueService.prodStateRegistInfoCheckForVue(whiteregex, whitereForCode, prodStateRegistInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n第"+i+"行:" + checkErr, null).toString();
                }
            }
            prodStateRegistInfoService.importProdStateRegistInfo(prodStateRegistInfos, params);


        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/appNavInfoRegImport.json", produces = {"application/json;charset=UTF-8"})
    // 净值信息登记导入
    public String appNavInfoRegImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "净值信息登记导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_nav_info_reg",0);
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "净值信息登记导入失败，文件与净值信息登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;

            params.put("reportDate", request.getParameter("reportDate"));
            List<AppNavInfoReg> appNavInfoRegs = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, AppNavInfoReg.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_nav_info_reg");
            if(appNavInfoRegs == null || appNavInfoRegs.size()==0){
                return RequestSupport.updateReturnJson(false, "净值信息登记导入失败，请先确认数据不可为空", null).toString();
            }else if(appNavInfoRegs != null && appNavInfoRegs.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "净值信息登记导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            appNavInfoRegs.remove(0);
            // 处理数据字典格式
            int i = dataStartRow;
            for (AppNavInfoReg appNavInfoReg : appNavInfoRegs) {
                i++;
                String checkErrImport = checkDataForVueService.appNavInfoRegCheckForImport(appNavInfoReg);
                appNavInfoReg.setReportDate(String.valueOf(params.get("reportDate")));
                appNavInfoReg.setNavRegType(StringUtils.isEmpty(appNavInfoReg.getNavRegType()) ? null : appNavInfoReg.getNavRegType().split(" ")[0]);
                appNavInfoReg.setCny(StringUtils.isEmpty(appNavInfoReg.getCny()) ? null : appNavInfoReg.getCny().split(" ")[0]);
                appNavInfoReg.setNavCalType(StringUtils.isEmpty(appNavInfoReg.getNavCalType()) ? null : appNavInfoReg.getNavCalType().split(" ")[0]);
                String checkErr = checkDataForVueService.appNavInfoRegCheckForVue(whiteregex, whitereForCode, appNavInfoReg);
                appNavInfoReg.setNavDate(StringUtils.isEmpty(appNavInfoReg.getNavDate()) ? "" : appNavInfoReg.getNavDate().replace("-", ""));
                appNavInfoReg.setDisclosureDate(StringUtils.isEmpty(appNavInfoReg.getDisclosureDate()) ? "" : appNavInfoReg.getDisclosureDate().replace("-", ""));
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n第"+i+"行:" + checkErr+checkErrImport, null).toString();
                }
            }

            appNavInfoRegService.importAppNavInfoReg(appNavInfoRegs, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/prodTransRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 交易信息批量修改导入
    public String prodTransRegistImport(@RequestParam(value = "file", required = false) MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> returndata = new HashMap<>();
        try {
            // 文件放在本地并记录下文件地址
            if (multipartFile == null) {
                return updateFailure("批量修改导入文件为空！ ");
            }
            // 转换File
            File tmpFile = FileUtil.multipartFileToFile(multipartFile);
            File file = new File(tmpFile.getAbsolutePath());

            // 存入oss
            String remoteFile;
            String fileName = multipartFile.getOriginalFilename();
            String remotePath = SysUtil.getSystemParamsByParaid("90000051312");
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + DateUtil.getTimestamp17() + "." + extension;

            if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
                remoteFile = remotePath + "/" + DateUtil.getNowDate() + "/" + newFileName;
            } else {
                remoteFile = remotePath + DateUtil.getNowDate() + "/" + newFileName;
            }

            FileTransfer transfer = FileTransferHelpler.getTransfer();
            transfer.uploadFileAndDisconnect(file.getAbsolutePath(), remoteFile);

            Map<String, Object> params = new HashMap<>();
            List<SqlRow> sqlRows = prodTransRegistInfoService.findImportMenuFileManageId(params);

            int nextId = 0;
            if (CollectionUtil.isNotEmpty(sqlRows)) {
                String id = (String) sqlRows.get(0).get("id");
                nextId = Integer.parseInt(id) + 1;
            }

            params.put("id", nextId);
            params.put("menuId", "M0866");
            params.put("fileName", fileName);
            params.put("localFilePath", remoteFile);
            params.put("status", "0");
            prodTransRegistInfoService.addImportMenuFileManage(params);
            returndata.put("id", nextId);
            FileUtil.delFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("批量修改导入文件上传到审批流程成功失败！ " + e.getMessage(), returndata);
        }
        return updateSuccess("批量修改导入文件上传到审批流程成功！", returndata);
    }


    @RequestMapping(value = "/subseqSubscrRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 产品存续期登记信息导入
    public String subseqSubscrRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "产品存续期登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_subseq_subscr_regist_info",0);
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "产品存续期登记信息导入失败，文件与产品存续期登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;
            params.put("reportDate", request.getParameter("reportDate"));
            List<SubseqSubscrRegistInfo> subseqSubscrRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, SubseqSubscrRegistInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_subseq_subscr_regist_info");
            if(subseqSubscrRegistInfos == null || subseqSubscrRegistInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "产品存续期登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(subseqSubscrRegistInfos != null && subseqSubscrRegistInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "产品存续期登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            subseqSubscrRegistInfos.remove(0);
            // 处理数据
            int i= dataStartRow;
            for (SubseqSubscrRegistInfo subseqSubscrRegistInfo : subseqSubscrRegistInfos) {
                i++;
                String checkErr = checkDataForInfoService.subseqSubscrRegistInfoCheckInfo(whiteregex, whitereForCode, subseqSubscrRegistInfo);
                if (StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！\n第"+i+"行:" + subseqSubscrRegistInfo.getProdCode() + "所在行的错误信息为：\n" + checkErr, null).toString();
                }
                subseqSubscrRegistInfo.setRealizedAnnualReturn(StringUtils.isEmpty(subseqSubscrRegistInfo.getRealizedAnnualReturnStr()) ? null : Double.valueOf(reverseToString(subseqSubscrRegistInfo.getRealizedAnnualReturnStr())));
                subseqSubscrRegistInfo.setExpectedAnnualReturn(StringUtils.isEmpty(subseqSubscrRegistInfo.getExpectedAnnualReturnStr()) ? null : Double.valueOf(reverseToString(subseqSubscrRegistInfo.getExpectedAnnualReturnStr())));
                // 处理数据字典格式
                subseqSubscrRegistInfo.setRegisterStatus(StringUtils.isEmpty(subseqSubscrRegistInfo.getRegisterStatus())?null:subseqSubscrRegistInfo.getRegisterStatus().split(" ")[0]);
                subseqSubscrRegistInfo.setInitialNav(StringUtils.isEmpty(subseqSubscrRegistInfo.getInitialNav()) ?null:reverseToString(subseqSubscrRegistInfo.getInitialNav()));
                subseqSubscrRegistInfo.setNav(StringUtils.isEmpty(subseqSubscrRegistInfo.getNav()) ?null:reverseToString(subseqSubscrRegistInfo.getNav()));
                subseqSubscrRegistInfo.setAggregateNav(StringUtils.isEmpty(subseqSubscrRegistInfo.getAggregateNav()) ?null:reverseToString(subseqSubscrRegistInfo.getAggregateNav()));
                subseqSubscrRegistInfo.setNavCur(StringUtils.isEmpty(subseqSubscrRegistInfo.getNavCur())?null:subseqSubscrRegistInfo.getNavCur().split(" ")[0]);
                subseqSubscrRegistInfo.setConvertRmbNav(StringUtils.isEmpty(subseqSubscrRegistInfo.getConvertRmbNav()) ?null:reverseToString(subseqSubscrRegistInfo.getConvertRmbNav()));
                subseqSubscrRegistInfo.setConvertRmbAggNav(StringUtils.isEmpty(subseqSubscrRegistInfo.getConvertRmbAggNav()) ?null:reverseToString(subseqSubscrRegistInfo.getConvertRmbAggNav()));
                subseqSubscrRegistInfo.setRealizedAnnualReturn(StringUtils.isEmpty(subseqSubscrRegistInfo.getRealizedAnnualReturnStr()) ?null:Double.valueOf(reverseToString(subseqSubscrRegistInfo.getRealizedAnnualReturnStr())));
                subseqSubscrRegistInfo.setExpectedAnnualReturn(StringUtils.isEmpty(subseqSubscrRegistInfo.getExpectedAnnualReturnStr()) ?null:Double.valueOf(reverseToString(subseqSubscrRegistInfo.getExpectedAnnualReturnStr())));
                subseqSubscrRegistInfo.setInconmeBank(StringUtils.isEmpty(subseqSubscrRegistInfo.getInconmeBank()) ?null:reverseToString(subseqSubscrRegistInfo.getInconmeBank()));
                subseqSubscrRegistInfo.setSubscribedLatestVol(StringUtils.isEmpty(subseqSubscrRegistInfo.getSubscribedLatestVol()) ?null:reverseToString(subseqSubscrRegistInfo.getSubscribedLatestVol()));
                subseqSubscrRegistInfo.setRedeemedLatestVol(StringUtils.isEmpty(subseqSubscrRegistInfo.getRedeemedLatestVol()) ?null:reverseToString(subseqSubscrRegistInfo.getRedeemedLatestVol()));
                subseqSubscrRegistInfo.setUnitsBonus(StringUtils.isEmpty(subseqSubscrRegistInfo.getUnitsBonus()) ?null:reverseToString(subseqSubscrRegistInfo.getUnitsBonus()));
                subseqSubscrRegistInfo.setCashBonus(StringUtils.isEmpty(subseqSubscrRegistInfo.getCashBonus()) ?null:reverseToString(subseqSubscrRegistInfo.getCashBonus()));
                subseqSubscrRegistInfo.setProdAmt(StringUtils.isEmpty(subseqSubscrRegistInfo.getProdAmt()) ?null:reverseToString(subseqSubscrRegistInfo.getProdAmt()));
                subseqSubscrRegistInfo.setProdVol(StringUtils.isEmpty(subseqSubscrRegistInfo.getProdVol()) ?null:reverseToString(subseqSubscrRegistInfo.getProdVol()));
                // 处理日期格式
                subseqSubscrRegistInfo.setBusinessEndDate(StringUtils.isEmpty(subseqSubscrRegistInfo.getBusinessEndDate())?null:subseqSubscrRegistInfo.getBusinessEndDate().replace("-",""));
                subseqSubscrRegistInfo.setBusinessStartDate(StringUtils.isEmpty(subseqSubscrRegistInfo.getBusinessStartDate())?null:subseqSubscrRegistInfo.getBusinessStartDate().replace("-",""));
                subseqSubscrRegistInfo.setNavDt(StringUtils.isEmpty(subseqSubscrRegistInfo.getNavDt())?null:subseqSubscrRegistInfo.getNavDt().replace("-",""));
                subseqSubscrRegistInfo.setReportDate(String.valueOf(params.get("reportDate")));
            }
            subseqSubscrRegistInfoService.subseqSubscrRegistInfoService(subseqSubscrRegistInfos,params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/initialSubRegistImport.json", produces = {"application/json;charset=UTF-8"})
    // 募集总量导入
    public String initialSubRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "募集期总量登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_initial_sub_regist_info",0);
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "募集期总量登记信息导入失败，文件与募集期总量登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;

            params.put("reportDate", request.getParameter("reportDate"));
            List<InitialSubRegistInfo> initialSubRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, InitialSubRegistInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_initial_sub_regist_info");
            if(initialSubRegistInfos == null || initialSubRegistInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "募集期总量登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(initialSubRegistInfos != null && initialSubRegistInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "募集期总量登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            initialSubRegistInfos.remove(0);
            // 处理数据字典格式
            int i= dataStartRow;
            for (InitialSubRegistInfo initialSubRegistInfo : initialSubRegistInfos) {
                i++;
                String checkErr = checkDataForInfoService.initialSubRegistInfoCheckInfo(whiteregex, whitereForCode, initialSubRegistInfo);
                if (StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！\n第"+i+"行:" + initialSubRegistInfo.getProdCode() + "所在行的错误信息为：\n" + checkErr, null).toString();
                }

                initialSubRegistInfo.setRegisterStatus(StringUtils.isEmpty(initialSubRegistInfo.getRegisterStatus()) ? null : initialSubRegistInfo.getRegisterStatus().split(" ")[0]);
                initialSubRegistInfo.setOtherDistributAgents(StringUtils.isEmpty(initialSubRegistInfo.getOtherDistributAgents()) ? null : initialSubRegistInfo.getOtherDistributAgents().split(" ")[0]);
                initialSubRegistInfo.setNumberIndivInvest(StringUtils.isEmpty(initialSubRegistInfo.getNumberIndivInvest()) ? null : reverseToString(initialSubRegistInfo.getNumberIndivInvest()));
                initialSubRegistInfo.setNumberCorporInvest(StringUtils.isEmpty(initialSubRegistInfo.getNumberCorporInvest()) ? null : reverseToString(initialSubRegistInfo.getNumberCorporInvest()));
                initialSubRegistInfo.setNumberUcorInvest(StringUtils.isEmpty(initialSubRegistInfo.getNumberUcorInvest()) ? null : reverseToString(initialSubRegistInfo.getNumberUcorInvest()));
                initialSubRegistInfo.setActualSubscribedAmt(StringUtils.isEmpty(initialSubRegistInfo.getActualSubscribedAmt()) ? null : reverseToString(initialSubRegistInfo.getActualSubscribedAmt()));
                initialSubRegistInfo.setSubscribedVol(StringUtils.isEmpty(initialSubRegistInfo.getSubscribedVol()) ? null : reverseToString(initialSubRegistInfo.getSubscribedVol()));
                initialSubRegistInfo.setAmtOtherDbAgents(StringUtils.isEmpty(initialSubRegistInfo.getAmtOtherDbAgents()) ? null : reverseToString(initialSubRegistInfo.getAmtOtherDbAgents()));
                initialSubRegistInfo.setProdCcy(StringUtils.isEmpty(initialSubRegistInfo.getProdCcy()) ? null : initialSubRegistInfo.getProdCcy().replace("-", ""));
                initialSubRegistInfo.setReportDate(String.valueOf(params.get("reportDate")));
            }
            initialSubRegistInfoService.importInitialSubRegistInfo(initialSubRegistInfos,params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    // 产品发行登记导入
    @RequestMapping(value = "/prodIssuanceRegistImport.json", produces = {"application/json;charset=UTF-8"})
    public String prodIssuanceRegistImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "产品发行登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_prod_issuance_regist_info",0);
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "产品发行登记信息导入失败，文件与发行登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;
            params.put("reportDate", StringUtils.isNotBlank(request.getParameter("reportDate")) ? request.getParameter("reportDate") : "");

            List<ProdIssuanceRegistInfo> prodIssuanceRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ProdIssuanceRegistInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_prod_issuance_regist_info");
            if(prodIssuanceRegistInfos == null || prodIssuanceRegistInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "产品发行登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(prodIssuanceRegistInfos != null && prodIssuanceRegistInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "产品发行登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            prodIssuanceRegistInfos.remove(0);
            // 处理数据字典格式
            int i = dataStartRow;
            for (ProdIssuanceRegistInfo prodIssuanceRegistInfo : prodIssuanceRegistInfos) {
                i++;
                prodIssuanceRegistInfo.setUpLimitPerRate(StringUtils.isEmpty(prodIssuanceRegistInfo.getUpLimitPerRateStr()) ? null : prodIssuanceRegistInfo.getUpLimitPerRateStr());
                prodIssuanceRegistInfo.setLowLimitPerRate(StringUtils.isEmpty(prodIssuanceRegistInfo.getLowLimitPerRateStr()) ? null : prodIssuanceRegistInfo.getLowLimitPerRateStr());
                prodIssuanceRegistInfo.setAverageOpenNo(StringUtils.isEmpty(prodIssuanceRegistInfo.getAverageOpenNoStr()) ? null : prodIssuanceRegistInfo.getAverageOpenNoStr());
                prodIssuanceRegistInfo.setClsfSto(StringUtils.isEmpty(prodIssuanceRegistInfo.getClsfStoStr()) ? null : Double.valueOf(prodIssuanceRegistInfo.getClsfStoStr()));

                String checkErr = checkDataForInfoService.prodIssuanceRegistInfoCheckInfo(whiteregex, whitereForCode, prodIssuanceRegistInfo);
                if (StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！\n第"+i+"行:" + prodIssuanceRegistInfo.getProdCode() + "所在行的错误信息为：\n" + checkErr, null).toString();
                }

                prodIssuanceRegistInfo.setRegisterStatus(StringUtils.isEmpty(prodIssuanceRegistInfo.getRegisterStatus()) ? null : prodIssuanceRegistInfo.getRegisterStatus().split(" ")[0]);
                prodIssuanceRegistInfo.setManagementMethod(StringUtils.isEmpty(prodIssuanceRegistInfo.getManagementMethod()) ? null : prodIssuanceRegistInfo.getManagementMethod().split(" ")[0]);
                prodIssuanceRegistInfo.setStructuredProd(StringUtils.isEmpty(prodIssuanceRegistInfo.getStructuredProd()) ? null : prodIssuanceRegistInfo.getStructuredProd().split(" ")[0]);
                prodIssuanceRegistInfo.setOpeningMode(StringUtils.isEmpty(prodIssuanceRegistInfo.getOpeningMode()) ? null : prodIssuanceRegistInfo.getOpeningMode().split(" ")[0]);
                prodIssuanceRegistInfo.setRegularOpenPeriod(StringUtils.isEmpty(prodIssuanceRegistInfo.getRegularOpenPeriod()) ? null : prodIssuanceRegistInfo.getRegularOpenPeriod().split(" ")[0]);
                prodIssuanceRegistInfo.setHolidayOpenType(StringUtils.isEmpty(prodIssuanceRegistInfo.getHolidayOpenType()) ? null : prodIssuanceRegistInfo.getHolidayOpenType().split(" ")[0]);
                prodIssuanceRegistInfo.setBusiOpenPeriod(StringUtils.isEmpty(prodIssuanceRegistInfo.getBusiOpenPeriod()) ? null : prodIssuanceRegistInfo.getBusiOpenPeriod().split(" ")[0]);

                // 处理日期格式
                String subscriptionStartDate = prodIssuanceRegistInfo.getSubscriptionStartDate();
                String subscriptionEndDate = prodIssuanceRegistInfo.getSubscriptionEndDate();
                String prodValueDate = prodIssuanceRegistInfo.getProdValueDate();
                String prodMaturityDate = prodIssuanceRegistInfo.getProdMaturityDate();
                String firstOpenDay = prodIssuanceRegistInfo.getFirstOpenDay();

                String reportDate = params.get("reportDate").toString();

                prodIssuanceRegistInfo.setSubscriptionStartDate(StringUtils.isEmpty(subscriptionStartDate) ? null : subscriptionStartDate.replace("-", ""));
                prodIssuanceRegistInfo.setReportDate(StringUtils.isEmpty(reportDate) ? null : reportDate.replace("-", ""));
                prodIssuanceRegistInfo.setTheoryReportStartDate(StringUtils.isEmpty(subscriptionStartDate) ? null : subscriptionStartDate.replace("-", ""));
                prodIssuanceRegistInfo.setSubscriptionEndDate(StringUtils.isEmpty(subscriptionEndDate) ? null : subscriptionEndDate.replace("-", ""));
                prodIssuanceRegistInfo.setProdValueDate(StringUtils.isEmpty(prodValueDate) ? null : prodValueDate.replace("-", ""));
                prodIssuanceRegistInfo.setProdMaturityDate(StringUtils.isEmpty(prodMaturityDate) ? null : prodMaturityDate.replace("-", ""));
                prodIssuanceRegistInfo.setFirstOpenDay(StringUtils.isEmpty(firstOpenDay) ? null : firstOpenDay.replace("-", ""));

            }
            prodIssuanceRegistInfoService.importProdIssuanceRegistInfo(prodIssuanceRegistInfos,params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }


    // 从业人员登记导入
    @RequestMapping(value = "/TrPractyRegistInfoImport.json", produces = {"application/json;charset=UTF-8"})
    public String TrPractyRegistInfoImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("beginDate", request.getParameter("beginDate"));
            params.put("queryDate", request.getParameter("queryDate"));
            List<TrPractyRegistInfo> TrPractyRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, TrPractyRegistInfo.class, true, null);
            // 移除第一标题行
            TrPractyRegistInfos.remove(0);
            // 处理数据字典格式
            for (TrPractyRegistInfo trPractyRegistInfo : TrPractyRegistInfos) {
                trPractyRegistInfo.setRegisterStatus(StringUtils.isEmpty(trPractyRegistInfo.getRegisterStatus())?null:trPractyRegistInfo.getRegisterStatus().split(" ")[0]);
                trPractyRegistInfo.setProfession(StringUtils.isEmpty(trPractyRegistInfo.getProfession())?null:trPractyRegistInfo.getProfession().split(" ")[0]);
                trPractyRegistInfo.setName(StringUtils.isEmpty(trPractyRegistInfo.getName())?null:trPractyRegistInfo.getName().split(" ")[0]);
                trPractyRegistInfo.setJobnumber(StringUtils.isEmpty(trPractyRegistInfo.getJobnumber())?null:trPractyRegistInfo.getJobnumber().split(" ")[0]);
                trPractyRegistInfo.setSex(StringUtils.isEmpty(trPractyRegistInfo.getSex())?null:trPractyRegistInfo.getSex().split(" ")[0]);
                trPractyRegistInfo.setIssBranchType(StringUtils.isEmpty(trPractyRegistInfo.getIssBranchType())?null:trPractyRegistInfo.getIssBranchType().split(" ")[0]);
                trPractyRegistInfo.setRegion(StringUtils.isEmpty(trPractyRegistInfo.getRegion())?null:trPractyRegistInfo.getRegion().split(" ")[0]);
                trPractyRegistInfo.setEducation(StringUtils.isEmpty(trPractyRegistInfo.getEducation())?null:trPractyRegistInfo.getEducation().split(" ")[0]);
                trPractyRegistInfo.setDegree(StringUtils.isEmpty(trPractyRegistInfo.getDegree())?null:clearUpString(trPractyRegistInfo.getDegree()));
                trPractyRegistInfo.setProfessQualyLevel(StringUtils.isEmpty(trPractyRegistInfo.getProfessQualyLevel())?null:trPractyRegistInfo.getProfessQualyLevel().split(" ")[0]);
                trPractyRegistInfo.setRegistType(StringUtils.isEmpty(trPractyRegistInfo.getRegistType())?null:trPractyRegistInfo.getRegistType().split(" ")[0]);
                trPractyRegistInfo.setMobile(StringUtils.isEmpty(trPractyRegistInfo.getMobile())?null:reverseToString(trPractyRegistInfo.getMobile()));
                trPractyRegistInfo.setRegisterClassify(StringUtils.isEmpty(trPractyRegistInfo.getRegisterClassify())?null:trPractyRegistInfo.getRegisterClassify().split(" ")[0]);
                if(!StringUtils.isEmpty(trPractyRegistInfo.getTelphone())){
                    trPractyRegistInfo.setTelphone(trPractyRegistInfo.getTelphone().indexOf("E")!=-1?reverseToString(trPractyRegistInfo.getTelphone()):trPractyRegistInfo.getTelphone());
                }
            }
            trPractyRegistInfoService.importtrPractyRegistInfo(TrPractyRegistInfos,params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }


    // 产品申报登记导入
    @RequestMapping(value = "/prodRegistFilingImport.json", produces = {"application/json;charset=UTF-8"})
    public String prodRegistFilingImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "产品申报登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_prod_regist_filing_info",0);
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "产品申报登记信息导入失败，文件与申报登记模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initData();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;
            List<ProdRegistFilingInfo> prodRegistFilingInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ProdRegistFilingInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_prod_regist_filing_info");
            if(prodRegistFilingInfos == null || prodRegistFilingInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "产品申报登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(prodRegistFilingInfos != null && prodRegistFilingInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "产品申报登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            prodRegistFilingInfos.remove(0);
            List<String> newList = new ArrayList<>();
            List<ProdRegistFilingInfo> list = prodRegistFilingInfoService.findProdRegistFilingInfos1(new FetcherData<ProdRegistFilingInfo>(new HashMap<>(), ProdRegistFilingInfo.class)).getRows();
            for (ProdRegistFilingInfo prodRegistFilingInfo : list) {
                newList.add(prodRegistFilingInfo.getIdentCode());
            }

            // 处理数据字典格式
            int i = dataStartRow;
            for (ProdRegistFilingInfo prodRegistFilingInfo : prodRegistFilingInfos) {
                i++;
                if (newList.contains(prodRegistFilingInfo.getIdentCode())) {
                    return RequestSupport.updateReturnJson(false, "导入失败！第" +i+ "行\n系统内已存在产品：" + prodRegistFilingInfo.getIdentCode(), null).toString();
                }
                prodRegistFilingInfo.setProdBenchmark(StringUtils.isEmpty(prodRegistFilingInfo.getProdBenchmarkStr()) ? null : prodRegistFilingInfo.getProdBenchmarkStr());
                prodRegistFilingInfo.setInvestThreshold(StringUtils.isEmpty(prodRegistFilingInfo.getInvestThresholdStr()) ? null : prodRegistFilingInfo.getInvestThresholdStr());
                prodRegistFilingInfo.setSalesCommissionRate(StringUtils.isEmpty(prodRegistFilingInfo.getSalesCommissionRateStr()) ? null : prodRegistFilingInfo.getSalesCommissionRateStr());
                prodRegistFilingInfo.setManageFeeRate(StringUtils.isEmpty(prodRegistFilingInfo.getManageFeeRateStr()) ? null : prodRegistFilingInfo.getManageFeeRateStr());
                prodRegistFilingInfo.setCdFeeRate(StringUtils.isEmpty(prodRegistFilingInfo.getCdFeeRateStr()) ? null : prodRegistFilingInfo.getCdFeeRateStr());
                String checkErr = checkDataForInfoService.prodRegistFilingInfoCheckInfo(whiteregex, whitereForCode, prodRegistFilingInfo);
                if (StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！\n 第" +i+ "行：错误信息为：\n" + checkErr, null).toString();
                }

                prodRegistFilingInfo.setProdName(StringUtils.isEmpty(prodRegistFilingInfo.getProdName()) ? null : prodRegistFilingInfo.getProdName());
                prodRegistFilingInfo.setIdentCode(StringUtils.isEmpty(prodRegistFilingInfo.getIdentCode()) ? null : prodRegistFilingInfo.getIdentCode());
                prodRegistFilingInfo.setProdBrand(StringUtils.isEmpty(prodRegistFilingInfo.getProdBrand()) ? null : prodRegistFilingInfo.getProdBrand());
                prodRegistFilingInfo.setProdTermNo(StringUtils.isEmpty(prodRegistFilingInfo.getProdTermNo()) ? null : prodRegistFilingInfo.getProdTermNo());
                prodRegistFilingInfo.setBankCode(StringUtils.isEmpty(prodRegistFilingInfo.getBankCode()) ? null : prodRegistFilingInfo.getBankCode());
                prodRegistFilingInfo.setProdAprvNm(StringUtils.isEmpty(prodRegistFilingInfo.getProdAprvNm()) ? null : prodRegistFilingInfo.getProdAprvNm());
                prodRegistFilingInfo.setApproverIdCode(StringUtils.isEmpty(prodRegistFilingInfo.getApproverIdCode()) ? null : prodRegistFilingInfo.getApproverIdCode());
                prodRegistFilingInfo.setProdDsnNm(StringUtils.isEmpty(prodRegistFilingInfo.getProdDsnNm()) ? null : prodRegistFilingInfo.getProdDsnNm());
                prodRegistFilingInfo.setDesignerIdCode(StringUtils.isEmpty(prodRegistFilingInfo.getDesignerIdCode()) ? null : prodRegistFilingInfo.getDesignerIdCode());
                prodRegistFilingInfo.setInvMngNm(StringUtils.isEmpty(prodRegistFilingInfo.getInvMngNm()) ? null : prodRegistFilingInfo.getInvMngNm());
                prodRegistFilingInfo.setManagerIdCode(StringUtils.isEmpty(prodRegistFilingInfo.getManagerIdCode()) ? null : prodRegistFilingInfo.getManagerIdCode());
                prodRegistFilingInfo.setContactName(StringUtils.isEmpty(prodRegistFilingInfo.getContactName()) ? null : prodRegistFilingInfo.getContactName());
                prodRegistFilingInfo.setContactTelphone(StringUtils.isEmpty(prodRegistFilingInfo.getContactTelphone()) ? null : prodRegistFilingInfo.getContactTelphone());
                prodRegistFilingInfo.setContactMobile(StringUtils.isEmpty(prodRegistFilingInfo.getContactMobile()) ? null : prodRegistFilingInfo.getContactMobile());
                prodRegistFilingInfo.setContactEmail(StringUtils.isEmpty(prodRegistFilingInfo.getContactEmail()) ? null : prodRegistFilingInfo.getContactEmail());
                prodRegistFilingInfo.setMinHoldDay(StringUtils.isEmpty(prodRegistFilingInfo.getMinHoldDay()) ? null : prodRegistFilingInfo.getMinHoldDay());
                prodRegistFilingInfo.setAcManaName(StringUtils.isEmpty(prodRegistFilingInfo.getAcManaName()) ? null : prodRegistFilingInfo.getAcManaName());
                prodRegistFilingInfo.setCooperator(StringUtils.isEmpty(prodRegistFilingInfo.getCooperator()) ? null : prodRegistFilingInfo.getCooperator());
                prodRegistFilingInfo.setInvestTypeRatio(StringUtils.isEmpty(prodRegistFilingInfo.getInvestTypeRatio()) ? null : prodRegistFilingInfo.getInvestTypeRatio());
                prodRegistFilingInfo.setProdSalesRegion(StringUtils.isEmpty(prodRegistFilingInfo.getProdSalesRegion()) ? null : getProdSaleArea(prodRegistFilingInfo.getProdSalesRegion()));
                prodRegistFilingInfo.setDcCdIdentCode(StringUtils.isEmpty(prodRegistFilingInfo.getDcCdIdentCode()) ? null : prodRegistFilingInfo.getDcCdIdentCode());
                prodRegistFilingInfo.setSeasCdName(StringUtils.isEmpty(prodRegistFilingInfo.getSeasCdName()) ? null : prodRegistFilingInfo.getSeasCdName());
                prodRegistFilingInfo.setDetails(StringUtils.isEmpty(prodRegistFilingInfo.getDetails()) ? null : prodRegistFilingInfo.getDetails());

                prodRegistFilingInfo.setTypeCollect(StringUtils.isEmpty(prodRegistFilingInfo.getTypeCollect()) ? null : prodRegistFilingInfo.getTypeCollect().split(" ")[0]);
                prodRegistFilingInfo.setProdRetrunType(StringUtils.isEmpty(prodRegistFilingInfo.getProdRetrunType()) ? null : prodRegistFilingInfo.getProdRetrunType().split(" ")[0]);
                prodRegistFilingInfo.setProdTerm(StringUtils.isEmpty(prodRegistFilingInfo.getProdTerm()) ? null : prodRegistFilingInfo.getProdTerm().split(" ")[0]);
                prodRegistFilingInfo.setProdTerm(StringUtils.isEmpty(prodRegistFilingInfo.getProdTerm()) ? null : prodRegistFilingInfo.getProdTerm().split(" ")[0]);
                prodRegistFilingInfo.setFiancialExclusive(StringUtils.isEmpty(prodRegistFilingInfo.getFiancialExclusive()) ? null : prodRegistFilingInfo.getFiancialExclusive().split(" ")[0]);
                if (StringUtils.isNotEmpty(prodRegistFilingInfo.getInvertRegion())) {
                    String invertRegion = "";
                    String value = prodRegistFilingInfo.getInvertRegion();
                    if (value.contains(",")) {
                        String[] valueArr = value.split(",");
                        for (String str : valueArr) {
                            if (StringUtils.isEmpty(invertRegion)) {
                                invertRegion = str.split(" ")[0];
                            } else {
                                invertRegion += "," + str.split(" ")[0];
                            }
                        }
                    } else {
                        invertRegion = value.split(" ")[0];
                    }
                    prodRegistFilingInfo.setInvertRegion(invertRegion);
                }
                prodRegistFilingInfo.setInvertCountry(StringUtils.isEmpty(prodRegistFilingInfo.getInvertCountry()) ? null : clearUpString(prodRegistFilingInfo.getInvertCountry()));
                prodRegistFilingInfo.setServiceMode(StringUtils.isEmpty(prodRegistFilingInfo.getServiceMode()) ? null : prodRegistFilingInfo.getServiceMode().split(" ")[0]);
                prodRegistFilingInfo.setOperationMode(StringUtils.isEmpty(prodRegistFilingInfo.getOperationMode()) ? null : prodRegistFilingInfo.getOperationMode().split(" ")[0]);
                prodRegistFilingInfo.setMinHoldPeriod(StringUtils.isEmpty(prodRegistFilingInfo.getMinHoldPeriod()) ? null : prodRegistFilingInfo.getMinHoldPeriod().split(" ")[0]);
                prodRegistFilingInfo.setOptionRedemptPeriod(StringUtils.isEmpty(prodRegistFilingInfo.getOptionRedemptPeriod()) ? null : prodRegistFilingInfo.getOptionRedemptPeriod().split(" ")[0]);
                prodRegistFilingInfo.setCashManager(StringUtils.isEmpty(prodRegistFilingInfo.getCashManager()) ? null : prodRegistFilingInfo.getCashManager().split(" ")[0]);
                prodRegistFilingInfo.setAssetAcMethod(StringUtils.isEmpty(prodRegistFilingInfo.getAssetAcMethod()) ? null : prodRegistFilingInfo.getAssetAcMethod().split(" ")[0]);
                prodRegistFilingInfo.setProdManaMode(StringUtils.isEmpty(prodRegistFilingInfo.getProdManaMode()) ? null : prodRegistFilingInfo.getProdManaMode().split(" ")[0]);
                prodRegistFilingInfo.setPriceMethod(StringUtils.isEmpty(prodRegistFilingInfo.getPriceMethod()) ? null : prodRegistFilingInfo.getPriceMethod().split(" ")[0]);
                prodRegistFilingInfo.setInvestType(StringUtils.isEmpty(prodRegistFilingInfo.getInvestType()) ? null : prodRegistFilingInfo.getInvestType().split(" ")[0]);
                prodRegistFilingInfo.setCooperateMode(StringUtils.isEmpty(prodRegistFilingInfo.getCooperateMode()) ? null : prodRegistFilingInfo.getCooperateMode().split(" ")[0]);
                prodRegistFilingInfo.setProdBenchmark(StringUtils.isEmpty(prodRegistFilingInfo.getProdBenchmarkStr()) ? null : reverseToString(prodRegistFilingInfo.getProdBenchmarkStr()));
                if (StringUtils.isNotEmpty(prodRegistFilingInfo.getRiskLevel())) {
                    String riskLevel = "";
                    String value = prodRegistFilingInfo.getRiskLevel();
                    if (value.contains(",")) {
                        String[] valueArr = value.split(",");
                        for (String str : valueArr) {
                            if (StringUtils.isEmpty(riskLevel)) {
                                riskLevel = str.split(" ")[0];
                            } else {
                                riskLevel += "," + str.split(" ")[0];
                            }
                        }
                    } else {
                        riskLevel = value.split(" ")[0];
                    }
                    prodRegistFilingInfo.setRiskLevel(riskLevel);
                }
                prodRegistFilingInfo.setFundCur(StringUtils.isEmpty(prodRegistFilingInfo.getFundCur()) ? null : prodRegistFilingInfo.getFundCur().split(" ")[0]);
                prodRegistFilingInfo.setPrincipalCur(StringUtils.isEmpty(prodRegistFilingInfo.getPrincipalCur()) ? null : prodRegistFilingInfo.getPrincipalCur().split(" ")[0]);
                prodRegistFilingInfo.setIncomeCur(StringUtils.isEmpty(prodRegistFilingInfo.getIncomeCur()) ? null : prodRegistFilingInfo.getIncomeCur().split(" ")[0]);
                prodRegistFilingInfo.setInvestThreshold(StringUtils.isEmpty(prodRegistFilingInfo.getInvestThresholdStr()) ? null : reverseToString(prodRegistFilingInfo.getInvestThresholdStr()));
                prodRegistFilingInfo.setPlanFundAmt(StringUtils.isEmpty(prodRegistFilingInfo.getPlanFundAmt()) ? null : reverseToString(prodRegistFilingInfo.getPlanFundAmt()));
                prodRegistFilingInfo.setStartDateEarliest(StringUtils.isEmpty(prodRegistFilingInfo.getStartDateEarliest()) ? null : prodRegistFilingInfo.getStartDateEarliest().replace("-", ""));
                prodRegistFilingInfo.setStartDateLatest(StringUtils.isEmpty(prodRegistFilingInfo.getStartDateLatest()) ? null : prodRegistFilingInfo.getStartDateLatest().replace("-", ""));
                prodRegistFilingInfo.setPrincipalDueDate(StringUtils.isEmpty(prodRegistFilingInfo.getPrincipalDueDate()) ? null : prodRegistFilingInfo.getPrincipalDueDate().split(" ")[0]);
                prodRegistFilingInfo.setIncomeDueDate(StringUtils.isEmpty(prodRegistFilingInfo.getIncomeDueDate()) ? null : prodRegistFilingInfo.getIncomeDueDate().split(" ")[0]);
                prodRegistFilingInfo.setSalesCommissionRate(StringUtils.isEmpty(prodRegistFilingInfo.getSalesCommissionRateStr()) ? null : reverseToString(prodRegistFilingInfo.getSalesCommissionRateStr()));
                prodRegistFilingInfo.setManageFeeRate(StringUtils.isEmpty(prodRegistFilingInfo.getManageFeeRateStr()) ? null : reverseToString(prodRegistFilingInfo.getManageFeeRateStr()));
                prodRegistFilingInfo.setDcCdName(StringUtils.isEmpty(prodRegistFilingInfo.getDcCdName()) ? null : prodRegistFilingInfo.getDcCdName().split(" ")[0]);
                prodRegistFilingInfo.setSeasCdNation(StringUtils.isEmpty(prodRegistFilingInfo.getSeasCdNation()) ? null : prodRegistFilingInfo.getSeasCdNation().split(" ")[0]);
                prodRegistFilingInfo.setCdFeeRate(StringUtils.isEmpty(prodRegistFilingInfo.getCdFeeRateStr()) ? null : reverseToString(prodRegistFilingInfo.getCdFeeRateStr()));
                prodRegistFilingInfo.setRiskRate(StringUtils.isEmpty(prodRegistFilingInfo.getRiskRate()) ? null : prodRegistFilingInfo.getRiskRate().split(" ")[0]);
                prodRegistFilingInfo.setEarlyTnOption(StringUtils.isEmpty(prodRegistFilingInfo.getEarlyTnOption()) ? null : prodRegistFilingInfo.getEarlyTnOption().split(" ")[0]);
                prodRegistFilingInfo.setInvestRdmOption(StringUtils.isEmpty(prodRegistFilingInfo.getInvestRdmOption()) ? null : prodRegistFilingInfo.getInvestRdmOption().split(" ")[0]);
                prodRegistFilingInfo.setProdCrtEnhance(StringUtils.isEmpty(prodRegistFilingInfo.getProdCrtEnhance()) ? null : prodRegistFilingInfo.getProdCrtEnhance().split(" ")[0]);
                prodRegistFilingInfo.setCrtInsType(StringUtils.isEmpty(prodRegistFilingInfo.getCrtInsType()) ? null : clearUpString(prodRegistFilingInfo.getCrtInsType()));
                if (StringUtils.isNotEmpty(prodRegistFilingInfo.getCrtInsType())) {
                    String crtInsType = "";
                    String value = prodRegistFilingInfo.getCrtInsType();
                    if (value.contains(",")) {
                        String[] valueArr = value.split(",");
                        for (String str : valueArr) {
                            if (StringUtils.isEmpty(crtInsType)) {
                                crtInsType = str.split(" ")[0];
                            } else {
                                crtInsType += "," + str.split(" ")[0];
                            }
                        }
                    } else {
                        crtInsType = value.split(" ")[0];
                    }
                    prodRegistFilingInfo.setCrtInsType(crtInsType);
                }
                prodRegistFilingInfo.setProdCrtMethod(StringUtils.isEmpty(prodRegistFilingInfo.getProdCrtMethod()) ? null : prodRegistFilingInfo.getProdCrtMethod().split(" ")[0]);
                prodRegistFilingInfo.setRegisterStatus(StringUtils.isEmpty(prodRegistFilingInfo.getRegisterStatus()) ? null : prodRegistFilingInfo.getRegisterStatus().split(" ")[0]);
                prodRegistFilingInfo.setNewProd(StringUtils.isEmpty(prodRegistFilingInfo.getNewProd()) ? null : prodRegistFilingInfo.getNewProd().split(" ")[0]);
                if (StringUtils.isNotEmpty(prodRegistFilingInfo.getProdEspPrpt())) {
                    String prodEspPrpt = "";
                    String value = prodRegistFilingInfo.getProdEspPrpt();
                    if (value.contains(",")) {
                        String[] valueArr = value.split(",");
                        for (String str : valueArr) {
                            if (StringUtils.isEmpty(prodEspPrpt)) {
                                prodEspPrpt = str.split(" ")[0];
                            } else {
                                prodEspPrpt += "," + str.split(" ")[0];
                            }
                        }
                    } else {
                        prodEspPrpt = value.split(" ")[0];
                    }
                    prodRegistFilingInfo.setProdEspPrpt(prodEspPrpt);
                }
            }
            prodRegistFilingInfoService.importProdRegistFilingInfo(prodRegistFilingInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    // 子份额信息登记导入
    @RequestMapping(value = "/sonShareInfoImport.json", produces = {"application/json;charset=UTF-8"})
    public String sonShareInfoImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            List<AppSonShareInfoReg> sonShareInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, AppSonShareInfoReg.class, true, null);
            // 移除第一标题行
            sonShareInfos.remove(0);

            List<String> newList = new ArrayList<>();
            List<AppSonShareInfoReg> list = appSonShareInfoRegService.findAppSonShareInfoRegs(new FetcherData<AppSonShareInfoReg>(new HashMap<>() ,AppSonShareInfoReg.class)).getRows();
            for (AppSonShareInfoReg appSonShareInfoReg : list) {
                newList.add(appSonShareInfoReg.getSonShareCode());
            }
            // 处理数据字典格式
            for (AppSonShareInfoReg sonShareInfo : sonShareInfos) {
                sonShareInfo.setSonShareCode(StringUtils.isEmpty(sonShareInfo.getSonShareCode()) ? null : sonShareInfo.getSonShareCode());
                sonShareInfo.setSonShareRegEnc(StringUtils.isEmpty(sonShareInfo.getSonShareRegEnc()) ? null : sonShareInfo.getSonShareRegEnc());
            }
            appSonShareInfoRegService.importAppSonShareInfo(sonShareInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    // 多选处理
    private String clearUpString(String s) {
        String[] splits = s.split(",");
        StringBuilder sb = new StringBuilder();
        if (splits.length == 1) {
            return sb.append(s.split(" ")[0]).toString();
        }
        for (String split : splits) {
            sb.append(split.split(" ")[0]);
            sb.append(",");
        }
        return sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : "";
    }

    // 币种处理
    private String dealCur(String s) {
        String[] splits = s.split(",");
        StringBuilder sb = new StringBuilder();
        if (splits.length == 1) {
            return sb.append(s).toString();
        }
        for (String split : splits) {
            sb.append(split.substring(0, split.indexOf(" ")));
            sb.append(",");
        }
        return sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : "";
    }

    // 读取到科学计数法转化
    private String reverseToString(String actualProdTerDate) {
        BigDecimal bd = new BigDecimal(actualProdTerDate);
        return bd.toPlainString();
    }

    //拼接产品销售区域
    private String getProdSaleArea(String prodSalesRegion) {
        String area = "";
        if (prodSalesRegion.length() > 0 && prodSalesRegion.contains(" ")) {
            String[] areaArray = prodSalesRegion.split(",");
            for (String region : areaArray) {
                area += ("," + region.split(" ")[0]);
            }
            area = area.substring(1);
        }
        return area;
    }
    //交易信息登记信息导入
    @RequestMapping(value = "/prodTransRegistInfoImport.json", produces = {"application/json;charset=UTF-8"})
    public String prodTransRegistInfoImport(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            String filename = file.getOriginalFilename();
            if (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")) {
                return RequestSupport.updateReturnJson(false, "交易信息登记信息导入失败，文件格式有误，文件后缀必须以xls或xlsx结尾!", null).toString();
            }
            String isCheck = compareHeaderService.compare(file, "app_prod_trans_regist_info",0);
            log.info("isCheck>>>:" + isCheck);
            if (!isCheck.equals("0")) {
                return RequestSupport.updateReturnJson(false, "交易信息登记信息导入失败，文件与交易信息登记信息模板不一致!具体错误信息为：" + isCheck, null).toString();
            }
            checkDataParams.initDataNoDict();
            checkDataParams.initData();
            String whiteregex = CheckDataParams.whiteregex;
            String whitereForCode = CheckDataParams.whitereForCode;
            params.put("reportDate", StringUtils.isNotBlank(request.getParameter("reportDate")) ? request.getParameter("reportDate") : "");

            List<ProdTransRegistInfo> prodTransRegistInfos = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ProdTransRegistInfo.class, true, null);
            //表头所在行
            int dataStartRow = compareHeaderService.getTempalteDataStartRow("app_prod_trans_regist_info");
            if(prodTransRegistInfos == null || prodTransRegistInfos.size()==0){
                return RequestSupport.updateReturnJson(false, "交易信息登记信息导入失败，请先确认数据不可为空", null).toString();
            }else if(prodTransRegistInfos != null && prodTransRegistInfos.size() < dataStartRow+1){
                return RequestSupport.updateReturnJson(false, "交易信息登记信息导入失败，请先确认数据不可为空", null).toString();
            }
            // 移除第一标题行
            prodTransRegistInfos.remove(0);
            // 处理数据字典格式
            int i=dataStartRow;
            for (ProdTransRegistInfo prodTransRegistInfo : prodTransRegistInfos) {
                i++;
                String checkErrImport = checkDataForVueService.prodTransRegistInfoCheckForImport(prodTransRegistInfo);
                prodTransRegistInfo.setCashType(StringUtils.isEmpty(prodTransRegistInfo.getCashType()) ? null : prodTransRegistInfo.getCashType().split(" ")[0]);
                prodTransRegistInfo.setCur(StringUtils.isEmpty(prodTransRegistInfo.getCur()) ? null : prodTransRegistInfo.getCur().split(" ")[0]);
                prodTransRegistInfo.setRelatedPartyTrans(StringUtils.isEmpty(prodTransRegistInfo.getRelatedPartyTrans()) ? null : prodTransRegistInfo.getRelatedPartyTrans().split(" ")[0]);
                prodTransRegistInfo.setCounterType(StringUtils.isEmpty(prodTransRegistInfo.getCounterType()) ? null : prodTransRegistInfo.getCounterType().split(" ")[0]);
                prodTransRegistInfo.setQuantity(StringUtils.isEmpty(prodTransRegistInfo.getQuantity()) ? null : reverseToString(prodTransRegistInfo.getQuantity()));
                prodTransRegistInfo.setMethodAssetMeasure(StringUtils.isEmpty(prodTransRegistInfo.getMethodAssetMeasure()) ? null : prodTransRegistInfo.getMethodAssetMeasure().split(" ")[0]);
                prodTransRegistInfo.setConvertRmb(StringUtils.isEmpty(prodTransRegistInfo.getConvertRmb()) ? null : reverseToString(prodTransRegistInfo.getConvertRmb()));
                prodTransRegistInfo.setAmt(StringUtils.isEmpty(prodTransRegistInfo.getAmt()) ? null : reverseToString(prodTransRegistInfo.getAmt()));
                prodTransRegistInfo.setUnitPriceFull(StringUtils.isEmpty(prodTransRegistInfo.getUnitPriceFull()) ? null : reverseToString(prodTransRegistInfo.getUnitPriceFull()));
                prodTransRegistInfo.setUnitPriceNet(StringUtils.isEmpty(prodTransRegistInfo.getUnitPriceNet()) ? null : reverseToString(prodTransRegistInfo.getUnitPriceNet()));
                prodTransRegistInfo.setRateAnnualReturn(StringUtils.isEmpty(prodTransRegistInfo.getRateAnnualReturnStr()) ? null : Double.valueOf(reverseToString(prodTransRegistInfo.getRateAnnualReturnStr())));
                prodTransRegistInfo.setTheoryReportStartDate(StringUtils.isEmpty(prodTransRegistInfo.getTradeDate()) ? null : prodTransRegistInfo.getTradeDate().replace("-", ""));
                prodTransRegistInfo.setTradeDate(StringUtils.isEmpty(prodTransRegistInfo.getTradeDate()) ? null : prodTransRegistInfo.getTradeDate().replace("-", ""));
                prodTransRegistInfo.setRegisterStatus("0");
                prodTransRegistInfo.setReportDate(params.get("reportDate").toString());
                String checkErr = checkDataForVueService.prodTransRegistInfoCheckForVue(whiteregex, whitereForCode, prodTransRegistInfo);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
                    return RequestSupport.updateReturnJson(false, "导入失败！错误信息为：\n第"+i+"行:" + checkErr+checkErrImport, null).toString();
                }

            }
            prodTransRegistInfoService.importAddProdTransRegistInfo(prodTransRegistInfos, params);
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ " + e.getMessage());
        }
        return updateSuccess("导入成功！");
    }



}
