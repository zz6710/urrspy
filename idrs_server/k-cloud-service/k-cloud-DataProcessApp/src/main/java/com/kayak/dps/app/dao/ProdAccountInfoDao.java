package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.model.ProdAccountInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @description: 产品账户信息Dao
 */
@Repository
public class ProdAccountInfoDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(ProdAccountInfoDao.class);

    public int findProdAccountInfoCount(ProdAccountInfo prodAccountInfo) throws Exception {
        String sql = "select count(*) as count " +
                "from ods_prod_account_info tpai " +
                "LEFT JOIN ods_prod_account_info_correlation tpaic ON tpai.id = tpaic.ods_prod_account_info_id " +
                "where 1 = 1 ";
        if (StringUtils.isNotEmpty(prodAccountInfo.getAccountType())){
            sql += " and   tpai.account_type = $S{accountType} ";
        }

        return super.findRow(sql, prodAccountInfo).getInteger("count");
    }

    public int findProdAccountInfoCountUpdate(ProdAccountInfo prodAccountInfo) throws Exception {
        String sql = "select count(*) as count from ods_prod_account_info t where 1 = 1 ";
        if (StringUtils.isNotEmpty(prodAccountInfo.getAccountType())){
            sql += " and   t.account_type = $S{accountType} ";
        }
        if (StringUtils.isNotEmpty(prodAccountInfo.getAccountCode())){
            sql += " and   t.account_code=$S{accountCode} ";
        }
        if (StringUtils.isNotEmpty(prodAccountInfo.getProdCode())){
            sql += " and   t.prod_code = $S{prodCode} ";
        }
        //排除当前id
        if (StringUtils.isNotBlank(prodAccountInfo.getId())) {
            sql += " and  t.id != $S{id} ";
        }

        return super.findRow(sql, prodAccountInfo).getInteger("count");
    }

    public SqlResult<ProdAccountInfo> findAccountInfo(SqlParam<ProdAccountInfo> params) throws Exception {
        String sql = "select t.id," +
                " t.account_code," +
                " t.account_name," +
                " t.trustee_name_sub," +
                " t.trustee_name," +
                " t.account_type," +
                " t.prod_code," +
                " t2.prod_name prod_nm," +
                " t.distributor_code," +
                " t1.seller_name," +
                " t.bank_acc_num," +
                " t.email," +
                " t.faxno," +
                " t.call_person," +
                " t.telphone_no," +
                " t.address," +
                " t.acc_crt_date," +
                " t.note," +
                " t.open_bank_addr," +
                " t.account_acnt_bank," +
                " t.account_province," +
                " t.account_city," +
                " t.deal_date" +
                " from ods_prod_account_info t left join rms_stg_tbsellerinfo t1 on t.distributor_code = t1.seller_code" +
                " left join ods_prod_base_info t2 on t2.PROD_CODE = t.prod_code" +
                " where 1 = 1 and t.account_type <>'' ";
        if(StringUtils.isNotEmpty(params.getModel().getProdCd())){
            sql+="and t.prod_code like '%$U{prodCd}%'";
        }
        if (StringUtils.isNotEmpty(params.getModel().getAccountType())){
            sql += " and   t.account_type = $S{accountType} ";
        }
        if(StringUtils.isNotEmpty(params.getModel().getAccountCode())){
            sql+="and t.account_code like '%$U{accountCode}%'";
        }
        if(StringUtils.isNotEmpty(params.getModel().getSellerName())){
            sql+="and t1.seller_name like '%$U{sellerName}%'";
        }
        if(StringUtils.isNotEmpty(params.getModel().getTrusteeNameSub())){
            sql+="and t.trustee_name_sub like '%$U{trusteeNameSub}%'";
        }
        if(StringUtils.isNotEmpty(params.getModel().getAccountAcntBank())){
            sql+="and t.account_acnt_bank like '%$U{accountAcntBank}%'";
        }
        return super.findRows(sql, params);
    }

    public int updateAccountInfo(SqlParam<ProdAccountInfo> params) throws Exception {
        ProdAccountInfo prodAccountInfo = params.getModel();
        Date now = new Date();
        SqlRow sqlRow=super.findRow("select distinct  t.check_inon from ods_prod_base_info t where t.prod_code =$S{prodCode}",params.getModel());
        prodAccountInfo.setCheckInon(sqlRow.getString("check_inon"));
        prodAccountInfo.setUpdDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        prodAccountInfo.setUpdTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));
        return super.update("update ods_prod_account_info t " +
                    "set t.account_code =$S{accountCode}," +
                    " t.account_name =$S{accountName}," +
                    " t.trustee_name_sub =$S{trusteeNameSub}," +
                    " t.trustee_name =$S{trusteeName}," +
                    " t.account_type =$S{accountType}," +
                    " t.prod_code =$S{prodCode}," +
                    " t.bank_acc_num =$S{bankAccNum}," +
                    " t.email =$S{email}," +
                    " t.faxno =$S{faxno}," +
                    " t.call_person =$S{callPerson}," +
                    " t.telphone_no =$S{telphoneNo}," +
                    " t.address =$S{address}," +
                    " t.acc_crt_date =$S{accCrtDate}," +
                    " t.note =$S{note}," +
                    " t.open_bank_addr =$S{openBankAddr}," +
                    " t.account_acnt_bank =$S{accountAcntBank}," +
                    " t.account_province =$S{accountProvince}," +
                    " t.account_city =$S{accountCity}," +
                    "    t.upd_date=$S{updDate}, " +
                    "    t.upd_time=$S{updTime}," +
                    "t.check_inon=$S{checkInon}" +
                    "where t.id=$S{id}; ", params.getModel()).getEffect();
    }
    public int addAccountInfo(SqlParam<ProdAccountInfo> params) throws Exception {
        ProdAccountInfo prodAccountInfo = params.getModel();
        Date now = new Date();
        SqlRow sqlRow=super.findRow("select distinct  t.check_inon from ods_prod_base_info t where t.prod_code =$S{prodCode}",params.getModel());
        prodAccountInfo.setCheckInon(sqlRow.getString("check_inon"));
        prodAccountInfo.setCrtDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        prodAccountInfo.setCrtTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));
        prodAccountInfo.setUpdDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        prodAccountInfo.setUpdTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));
        return super.update("insert into ods_prod_account_info (" +
                "account_type,DISTRIBUTOR_CODE,trustee_name_sub,account_code,account_name,prod_code,bank_acc_num,account_acnt_bank,check_inon)" +
                "values($S{accountType},$S{distributorCode},$S{trusteeNameSub},$S{accountCode},$S{accountName},$S{prodCode},$S{bankAccNum},$S{accountAcntBank},$S{checkInon})"
            , params.getModel()).getEffect();
    }
    public int deleteAccountInfo(SqlParam<ProdAccountInfo> params) throws Exception {
        return super.update("delete from  ods_prod_account_info where id=$S{id}"
                , params.getModel()).getEffect();
    }

    public SqlResult<ProdAccountInfo> findProdCdAndNm(SqlParam<ProdAccountInfo> params) throws Exception {
        String sql = "SELECT * FROM (SELECT DISTINCT prod_cd,prod_nm  FROM DWD_PRD_PRD_BAS_INF union all SELECT DISTINCT prod_cd,prod_nm  FROM DWD_PRD_PRD_BAS_INF_SUB ) T WHERE  1=1 ";
        if (StringUtils.isNotEmpty(params.getModel().getProdCd())){
            sql += " and  prod_cd = $S{prodCd} ";
        }
        return super.findRows(sql, params);
    }
    public SqlResult<ProdAccountInfo> findSellerCdAndNm(SqlParam<ProdAccountInfo> params) throws Exception {
        String sql = "select t.seller_code distributor_code ,t.seller_name  from rms_stg_tbsellerinfo t where 1=1";
        return super.findRows(sql, params);
    }

}
