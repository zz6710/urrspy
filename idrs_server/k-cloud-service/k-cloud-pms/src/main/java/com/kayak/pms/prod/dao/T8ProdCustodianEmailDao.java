package com.kayak.pms.prod.dao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.prod.model.T8ProdCustodianEmail;
import org.springframework.stereotype.Repository;
import com.kayak.base.dao.ComnDao;

import java.util.HashMap;
import java.util.List;


@Repository
public class T8ProdCustodianEmailDao extends ComnDao {

    public SqlResult<T8ProdCustodianEmail> findT8ProdCustodianEmails(SqlParam<T8ProdCustodianEmail> params) throws Exception {
        return super.findRows("SELECT id,opinion,prod_code,prod_name,accounting_manager,operating_agency,handling_phone,handling_mailbox,import_date,review_status,crt_user_id,crt_user_name,upd_user_id,upd_user_name FROM t8_prod_custodian_email", params);
    }

    public UpdateResult addT8ProdCustodianEmail(SqlParam<T8ProdCustodianEmail> params) throws Exception {
        return super.update("INSERT INTO t8_prod_custodian_email(id,prod_code,prod_name,accounting_manager,operating_agency,handling_phone,handling_mailbox,import_date,review_status,crt_user_id,crt_user_name,upd_user_id,upd_user_name) VALUES($AUTOIDS{id},$S{prodCode},$S{prodName},$S{accountingManager},$S{operatingAgency},$S{handlingPhone},$S{handlingMailbox},$S{importDate},$S{reviewStatus},$S{crtUserId},$S{crtUserName},$S{updUserId},$S{updUserName})",
                params.getModel());
    }

    public UpdateResult updateT8ProdCustodianEmail(SqlParam<T8ProdCustodianEmail> params) throws Exception {
        return super.update("UPDATE t8_prod_custodian_email SET opinion = $S{opinion},prod_code=$S{prodCode} ,prod_name=$S{prodName} ,accounting_manager=$S{accountingManager} ,operating_agency=$S{operatingAgency} ,handling_phone=$S{handlingPhone} ,handling_mailbox=$S{handlingMailbox} ,import_date=$S{importDate} ,review_status=$S{reviewStatus} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName}  WHERE  id=$S{id} ",
                params.getModel());
    }

    public UpdateResult deleteT8ProdCustodianEmail(SqlParam<T8ProdCustodianEmail> params) throws Exception {
        return super.update("DELETE FROM t8_prod_custodian_email WHERE  id=$S{id} ",
                params.getModel());
    }

    public String findProdInfoByName(String prodName) throws Exception {
        String sql = "SELECT\n" +
                "\tt8_prod_info.prod_code \n" +
                "FROM\n" +
                "\tt8_prod_info \n" +
                "WHERE\n" +
                "\tt8_prod_info.prod_name = '" + prodName + "'";
        SqlParam<T8ProdCustodianEmail> param = new FetcherData<>(new HashMap<>(), T8ProdCustodianEmail.class);
        return super.findRow(String.class, sql, 0, param);

    }

    public void batchAdd(List<T8ProdCustodianEmail> t8ProdCustodianEmails) throws Exception {
        for (T8ProdCustodianEmail t8ProdCustodianEmail : t8ProdCustodianEmails) {
            String sql = "INSERT INTO t8_prod_custodian_email (`id`, `prod_code`, `prod_name`, `accounting_manager`, `operating_agency`, `handling_phone`, `handling_mailbox`, `import_date`, `review_status`, `crt_user_id`, `crt_user_name`, `upd_user_id`, `upd_user_name`, `opinion`) VALUES ($AUTOIDS{id}, '" + t8ProdCustodianEmail.getProdCode() + "', '" + t8ProdCustodianEmail.getProdName() + "', '" + t8ProdCustodianEmail.getAccountingManager() + "', '" + t8ProdCustodianEmail.getOperatingAgency() + "', '" + t8ProdCustodianEmail.getHandlingPhone() + "', '" + t8ProdCustodianEmail.getHandlingMailbox() + "', '" + t8ProdCustodianEmail.getImportDate() + "', '" + t8ProdCustodianEmail.getReviewStatus() + "', '" + t8ProdCustodianEmail.getCrtUserId() + "', '" + t8ProdCustodianEmail.getCrtUserName() + "', NULL, NULL, NULL)";
            super.update(sql);
        }
    }

    public T8ProdCustodianEmail findT8ProdCustodianEmailsById(SqlParam<T8ProdCustodianEmail> params) throws Exception {
        return super.findRow(T8ProdCustodianEmail.class, "SELECT id,opinion,prod_code,prod_name,accounting_manager,operating_agency,handling_phone,handling_mailbox,import_date,review_status,crt_user_id,crt_user_name,upd_user_id,upd_user_name FROM t8_prod_custodian_email where id='" + params.getModel().getId() + "'", 0, params);
    }

    public boolean findT8ProdCustodianEmail(T8ProdCustodianEmail t8ProdCustodianEmail) throws Exception {
        SqlParam<T8ProdCustodianEmail> params = new FetcherData<>(new HashMap<>(), T8ProdCustodianEmail.class);
        T8ProdCustodianEmail t8ProdCustodianEmail1 = super.findRow(T8ProdCustodianEmail.class, "SELECT id,opinion,prod_code,prod_name,accounting_manager,operating_agency,handling_phone,handling_mailbox,import_date,review_status,crt_user_id,crt_user_name,upd_user_id,upd_user_name FROM t8_prod_custodian_email where prod_code=$S{prodCode}", 0, t8ProdCustodianEmail);
        return t8ProdCustodianEmail1 == null;
    }

    public T8ProdCustodianEmail findT8ProdCustodianEmailByCode(String prodCode) throws Exception {
        SqlParam<T8ProdCustodianEmail> params=new FetcherData<>(new HashMap<>(),T8ProdCustodianEmail.class);
        return super.findRow(T8ProdCustodianEmail.class, "SELECT id,opinion,prod_code,prod_name,accounting_manager,operating_agency,handling_phone,handling_mailbox,import_date,review_status,crt_user_id,crt_user_name,upd_user_id,upd_user_name FROM t8_prod_custodian_email where prod_code='" + prodCode + "'", 0, params);

    }
    /**
    * @Description: 根据prodcode 和 prodname编辑
    * @Param: [editCustodianEmailsList]
    * @return: void
    * @Author: XIEZEDONG🐼
    * @Date: 2021/8/27
    */
    public void batchEdit(List<T8ProdCustodianEmail> editCustodianEmailsList) throws Exception {
        for (T8ProdCustodianEmail t8ProdCustodianEmail : editCustodianEmailsList) {
            super.update("UPDATE t8_prod_custodian_email SET prod_code=$S{prodCode} ,prod_name=$S{prodName} ,accounting_manager=$S{accountingManager} ,operating_agency=$S{operatingAgency} ,handling_phone=$S{handlingPhone} ,handling_mailbox=$S{handlingMailbox} ,import_date=$S{importDate} ,review_status=$S{reviewStatus} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName}  WHERE  prod_code=$S{prodCode}",
                    t8ProdCustodianEmail);
        }
    }
}
