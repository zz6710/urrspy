package com.kayak.subject.service;

import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.cache.dao.CacheDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.DwdPrdPrdBasInfDao;
import com.kayak.subject.model.DwsAstEquInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.kayak.subject.dao.DwsMonthPrdDtlDao;
import com.kayak.subject.model.DwsMonthPrdDtl;

@Service
@APIDefine(desc = "产品明细月中间表服务", model = DwsMonthPrdDtl.class)
@Slf4j
public class DwsMonthPrdDtlService implements ExcelImportService<DwsMonthPrdDtl> {

	@Autowired
	private DwsMonthPrdDtlDao dwsMonthPrdDtlDao;
    @Autowired
    private DwdPrdPrdBasInfDao dwdPrdPrdBasInfDao;

	@API(desc = "查询产品明细月中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsMonthPrdDtl> findDwsMonthPrdDtls(SqlParam<DwsMonthPrdDtl> params) throws Exception {
		return dwsMonthPrdDtlDao.findDwsMonthPrdDtls(params);
	}

	@API(desc = "添加产品明细月中间表", params = "id,deal_date,prdc_cd,tot_raise_tt,net_raise_tt,bal_et,cur_pay_tt,inv_acv_yield_tt,bnk_acv_yield_tt,raise_type,inv_prop,opt_mod,prd_trm,inv_hld_ntr,inv_hld_lg_org,inv_hld_non_fnc_org,inv_hld_bnk_fnc_org,inv_hld_isr_fnc_org,inv_hld_tst_cpn,inv_hld_sct_cpn,inv_hld_fnd_cpn,inv_hld_otr_fnc_org,inv_hld_fnc_org_prd,cur_cny,cur_usd,cur_eur,cur_otr,csh_mng_f,mng_mth,vltn_mthd,blg_fin_sam_bus_f,avg_rmn_trm,is_prod_tsf,tsf_fnd_amt,pen_inv_prd_f,per_pen_inv_prod_f,mth_anl_yield,wgt_price,rsk_lev,is_seal_prd_past,found_dt,mtu_dt,crt_dt,crt_tm,ywrq", auth = APIAuth.NO)
	public int addDwsMonthPrdDtl(SqlParam<DwsMonthPrdDtl> params) throws Exception {
        params.getModel().setCrtDt(DateUtil.getNowDate());
        params.getModel().setCrtTm(DateUtil.getNowTime());
		return dwsMonthPrdDtlDao.addDwsMonthPrdDtl(params).getEffect();
	}
	
	@API(desc = "修改产品明细月中间表", params = "id,deal_date,prdc_cd,tot_raise_tt,net_raise_tt,bal_et,cur_pay_tt,inv_acv_yield_tt,bnk_acv_yield_tt,raise_type,inv_prop,opt_mod,prd_trm,inv_hld_ntr,inv_hld_lg_org,inv_hld_non_fnc_org,inv_hld_bnk_fnc_org,inv_hld_isr_fnc_org,inv_hld_tst_cpn,inv_hld_sct_cpn,inv_hld_fnd_cpn,inv_hld_otr_fnc_org,inv_hld_fnc_org_prd,cur_cny,cur_usd,cur_eur,cur_otr,csh_mng_f,mng_mth,vltn_mthd,blg_fin_sam_bus_f,avg_rmn_trm,is_prod_tsf,tsf_fnd_amt,pen_inv_prd_f,per_pen_inv_prod_f,mth_anl_yield,wgt_price,rsk_lev,is_seal_prd_past,found_dt,mtu_dt,crt_dt,crt_tm,ywrq", auth = APIAuth.NO)
	public int updateDwsMonthPrdDtl(SqlParam<DwsMonthPrdDtl> params) throws Exception {
		return dwsMonthPrdDtlDao.updateDwsMonthPrdDtl(params).getEffect();
	}
	
	@API(desc = "删除产品明细月中间表", params = "id,deal_date,prdc_cd,tot_raise_tt,net_raise_tt,bal_et,cur_pay_tt,inv_acv_yield_tt,bnk_acv_yield_tt,raise_type,inv_prop,opt_mod,prd_trm,inv_hld_ntr,inv_hld_lg_org,inv_hld_non_fnc_org,inv_hld_bnk_fnc_org,inv_hld_isr_fnc_org,inv_hld_tst_cpn,inv_hld_sct_cpn,inv_hld_fnd_cpn,inv_hld_otr_fnc_org,inv_hld_fnc_org_prd,cur_cny,cur_usd,cur_eur,cur_otr,csh_mng_f,mng_mth,vltn_mthd,blg_fin_sam_bus_f,avg_rmn_trm,is_prod_tsf,tsf_fnd_amt,pen_inv_prd_f,per_pen_inv_prod_f,mth_anl_yield,wgt_price,rsk_lev,is_seal_prd_past,found_dt,mtu_dt,crt_dt,crt_tm,ywrq", auth = APIAuth.NO)
	public int deleteDwsMonthPrdDtl(SqlParam<DwsMonthPrdDtl> params) throws Exception {
		return dwsMonthPrdDtlDao.deleteDwsMonthPrdDtl(params).getEffect();
	}

    public int deleteDwsMonthPrdDtl(DwsMonthPrdDtl params) throws Exception {
        return dwsMonthPrdDtlDao.deleteDwsMonthPrdDtl(params).getEffect();
    }

	public String importDwsMonthPrdDtl(MultipartFile file, Map<String, Object> params) throws Exception {
    		long startTime = System.currentTimeMillis();
    		log.info("导入产品明细月中间表【{}】开始",file.getOriginalFilename());
    		final DwsMonthPrdDtlService dwsMonthPrdDtlService = this;
    		ExcelImportListener<DwsMonthPrdDtl> excelImportListener = new ExcelImportListener<DwsMonthPrdDtl>(params) {
    			@Override
    			protected ExcelImportService<DwsMonthPrdDtl> getImportService() {
    				return dwsMonthPrdDtlService;
    			}
    		};
    		try {
    			EasyExcel.read(file.getInputStream())
    					.head(DwsMonthPrdDtl.class)
    					.registerReadListener(excelImportListener)
    					.sheet()
    					.doRead();
    		} catch (Exception e) {
    			throw new Exception(excelImportListener.getStopMsg());
    		}
    		log.info("导入产品明细月中间表【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
    		return excelImportListener.getStopMsg();
    	}

    @Autowired
    private ComnDao comnDao;

    @Override
    public void importFile(List<DwsMonthPrdDtl> list, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "REPLACE INTO dws_month_prd_dtl (deal_date,prdc_cd,tot_raise_tt,net_raise_tt,bal_et,cur_pay_tt,inv_acv_yield_tt,bnk_acv_yield_tt,raise_type,inv_prop,opt_mod,prd_trm,inv_hld_ntr,inv_hld_lg_org,inv_hld_non_fnc_org,inv_hld_bnk_fnc_org,inv_hld_isr_fnc_org,inv_hld_tst_cpn,inv_hld_sct_cpn,inv_hld_fnd_cpn,inv_hld_otr_fnc_org,inv_hld_fnc_org_prd,cur_cny,cur_usd,cur_eur,cur_otr,csh_mng_f,mng_mth,vltn_mthd,blg_fin_sam_bus_f,avg_rmn_trm,is_prod_tsf,tsf_fnd_amt,pen_inv_prd_f,per_pen_inv_prod_f,mth_anl_yield,wgt_price,rsk_lev,is_seal_prd_past,found_dt,mtu_dt,crt_dt,crt_tm,ywrq)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (list == null || list.isEmpty()) {
            throw new PromptException("没有数据");
        }

        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                for (DwsMonthPrdDtl info : list) {
                    ps.setString(1, map.get("dealDate").toString());
                    ps.setString(2, info.getPrdcCd());
                    ps.setString(3, info.getTotRaiseTt());
                    ps.setString(4, info.getNetRaiseTt());
                    ps.setString(5, info.getBalEt());
                    ps.setString(6, info.getCurPayTt());
                    ps.setString(7, info.getInvAcvYieldTt());
                    ps.setString(8, info.getBnkAcvYieldTt());
                    ps.setString(9, info.getRaiseType());
                    ps.setString(10, info.getInvProp());
                    ps.setString(11, info.getOptMod());
                    ps.setString(12, info.getPrdTrm());
                    ps.setString(13, info.getInvHldNtr());
                    ps.setString(14, info.getInvHldLgOrg());
                    ps.setString(15, info.getInvHldNonFncOrg());
                    ps.setString(16, info.getInvHldBnkFncOrg());
                    ps.setString(17, info.getInvHldIsrFncOrg());
                    ps.setString(18, info.getInvHldTstCpn());
                    ps.setString(19, info.getInvHldSctCpn());
                    ps.setString(20, info.getInvHldFndCpn());
                    ps.setString(21, info.getInvHldOtrFncOrg());
                    ps.setString(22, info.getInvHldFncOrgPrd());
                    ps.setString(23, info.getCurCny());
                    ps.setString(24, info.getCurUsd());
                    ps.setString(25, info.getCurEur());
                    ps.setString(26, info.getCurOtr());
                    ps.setString(27, info.getCshMngF());
                    ps.setString(28, info.getMngMth());
                    ps.setString(29, info.getVltnMthd());
                    ps.setString(30, info.getBlgFinSamBusF());
                    ps.setString(31, info.getAvgRmnTrm());
                    ps.setString(32, info.getIsProdTsf());
                    ps.setString(33, info.getTsfFndAmt());
                    ps.setString(34, info.getPenInvPrdF());
                    ps.setString(35, info.getMthAnlYield());
                    ps.setString(36, info.getWgtPrice());
                    ps.setString(37, info.getRskLev());
                    ps.setString(38, info.getIsSealPrdPast());
                    ps.setString(39, info.getFoundDt());
                    ps.setString(40, info.getMtuDt());
                    ps.setString(41, DateUtil.getNowDate());
                    ps.setString(42, DateUtil.getNowTime());
                    ps.setString(43, info.getYwrq());
                    ps.setString(44, info.getPerPenInvProdF());
                    ps.addBatch();
                }
                ps.executeBatch();
                log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                log.error("导入报表指标科目映射异常!", e);
                throw new Exception(e.getMessage());
            } finally {
                ps.close();
            }
        });
    }

}
