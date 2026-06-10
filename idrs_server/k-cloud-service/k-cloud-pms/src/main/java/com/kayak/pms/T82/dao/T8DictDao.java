package com.kayak.pms.T82.dao;

import com.google.common.base.Strings;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.T82.model.T8Dict;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class T8DictDao extends ComnDao {

    /**
     * 产品代码字典信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findTaProdInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT id as t8_prod_info_id,prod_code,prod_name FROM t8_prod_info ", params);
    }

    /**
     * 产品代码字典信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findDpbProdInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT prod_cd prod_code,prod_nm prod_name FROM app_prd_bas_inf ", params);
    }

    public SqlResult<T8Dict> findDpbProdSerInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT prod_ser_cd prod_code,prod_nm prod_name FROM app_prd_bas_inf WHERE prod_cd='"+params.getModel().getProdCode()+"' ", params);
    }

    /**
     * 接口名称字典信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findPortInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT port_code, port_name FROM base_port_manage WHERE port_state ='1' ORDER BY port_code ", params);
    }

    /**
     * 产品子份额代码字典信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findSonProdInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT id as t8_prod_info_id,sales_code as prod_code,sales_name as prod_name FROM t8_prod_share_sort ", params);
    }

    /**
     * 发布渠道字典信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findT8ChannelInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("select channel_name,id from idb_disclosure_channel_info where channel_type!='0'", params);
    }

    /**
     * 产品排期产品代码字典信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findScheduleProdInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT id as t8_prod_info_id,prod_code,prod_name FROM t8_prod_info where prod_son_status = '1'", params);
    }

    /**
     * 已发行成立的产品数据字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findEstablishProdInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("select id as t8_prod_info_id,t.prod_code, t.prod_name " +
                "from t8_prod_info t ", params);
        //TODO 暂时取消消保审核查询条件,以供测试后续业务逻辑,后续会添加产品查询限制  "where t.prod_status in ('6', '7')"
    }

    /**
     * 所有已成立无托管邮箱产品代码字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findEstablishProdInfosForCustEmail(SqlParam<T8Dict> params) throws Exception {
    return super.findRows(
        "SELECT\n"
            + "\tid AS t8_prod_info_id,\n"
            + "\tt.prod_code,\n"
            + "\tt.prod_name \n"
            + "FROM\n"
            + "\tt8_prod_info t \n"
            + "\twhere t.prod_code not in(select prod_code from t8_prod_custodian_email) and (t.is_recycle_code is null or t.is_recycle_code='0') ",
        params);
        //TODO 暂时取消消保审核查询条件,以供测试后续业务逻辑,后续会添加产品查询限制  "where t.prod_status in ('6', '7')"
    }

    /**
     * 查询所有未成立产品代码字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findNotEstablishProdInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("select id as t8_prod_info_id,t.prod_code, t.prod_name " +
                "from t8_prod_info t  order by t.id desc", params);
    }
    public SqlResult<T8Dict> findProdInfosByCustNo(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("select tpi.id as t8_prod_info_id,tpi.prod_code as prod_code,tpi.prod_name as prod_name " +
                "from t8_prod_sale  tps left join t8_prod_info tpi on tps.t8_prod_info_id = tpi.id  " +
                "where FIND_IN_SET($S{distributorCode},tps.distributor_code) order by tpi.id desc", params);
    }


    /**
     * 查询所有产品代码字典用于获取产品相应的托管协议和代销协议
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findDocumentInfoByProdCode(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("select id as t8_prod_info_id,t.prod_code, t.prod_name,  t.prod_status " +
                "from t8_prod_info t order by t.id desc", params);
    }

    /**
     * 产品代码字典信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findAllTaProdInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT prod_cd as prod_code,prod_nm as prod_name FROM dwd_prd_prd_bas_inf where call_dt >= '" + SysUtil.getSystemParamsByParaid("10004") + "' order by found_dt", DataSourceProperty.PUB, params);
    }

    /**
     * 产品所有用户信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findAllUserInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("select userid as user_id,username as user_name from sys_user where userid!='admin'", params);
    }

    /**
     * 根据code查产品代码字典信息
     *
     * @param
     * @return
     * @throws Exception
     */
    public String findAllTaProdInfosByCode(String prodCode) throws Exception {
        return super.findRow(String.class,"SELECT prod_nm as prodName FROM dwd_prd_prd_bas_inf WHERE prod_cd='"+prodCode+"'", DataSourceProperty.PUB,null);
    }

    /**
     * 功能：查询创设会可选择产品  请勿随意修改
     * 作者：rennannan
     * 日期：20210409
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findMeetProds(SqlParam<T8Dict> params) throws Exception {
        StringBuffer sql;
        if ("1".equals(params.getModel().getT8DecisionType())){
                     sql = new StringBuffer("SELECT prod.id as t8_prod_info_id,prod.prod_code,prod.prod_name " +
                    "     FROM t8_prod_info prod " +
                    "     WHERE not EXISTS (select 1 from t8_create_meeting_prod meetProd " +
                    "                       where meetProd.t8_prod_info_id = prod.id AND meetProd.t8_decision_type ='1' AND meetProd.meeting_result ='2'");
            sql.append(") order by id desc");
        }else{
            sql = new StringBuffer("SELECT\n" +
                    "\tprod.id AS t8_prod_info_id,\n" +
                    "\tprod.prod_code,\n" +
                    "\tprod.prod_name \n" +
                    "FROM\n" +
                    "\tt8_prod_info prod ");
            sql.append(" order by id desc");
        }

        return super.findRows(sql.toString(), params);
    }

    public List<T8Dict> findMeetProds1() throws Exception {
        String sql = "SELECT prod.id as t8_prod_info_id,prod.prod_code,prod.prod_name " +
                "     FROM t8_prod_info prod " +
                "     WHERE EXISTS (select 1 from t8_create_meeting_prod meetProd " +
                "                       where meetProd.t8_prod_info_id = prod.id AND meetProd.t8_decision_type ='1' AND meetProd.meeting_result ='2')";
        return super.findRows(T8Dict.class,sql,0,null);
    }

    /**
     * 查询销售商已设置产品信息的产品信息字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findTaDistributorInfoSetProd(SqlParam<T8Dict> params) throws Exception {

        return super.findRows("SELECT prod_code FROM t8_prod_distributor WHERE distributor_code=$S{distributorCode} ", params);
    }

    /**
     * 查询销售商已设置产品信息的产品信息字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findDisclosureChannel(SqlParam<T8Dict> params) throws Exception {

        return super.findRows("select id,channel_code,channel_name from idb_disclosure_channel_info where status = 'N' and channel_type in ('0','1')", params);
    }





    /**
     * 07产品代码字典信息
     *
     * @param params
     * @return
     * @throws Exception
     */

    public SqlResult<T8Dict> findTaProdInfos07(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT DISTINCT fundcode as prod_code,   fundname as prod_name FROM T8_FUND_OFI_OFDCFDAT07", params);
    }

    /**
     * 获取所有销售商名字字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findTaDistributorInfos(SqlParam<T8Dict> params) throws Exception {

        String sql = "SELECT distributor_code,distributor_name FROM T8_DISTRIBUTOR_INFO";

        return (SqlResult<T8Dict>) super.findRows(sql, params);
    }

    /**
     * 获取投资经理名字字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findT8InvestManagerInfos(SqlParam<T8Dict> params) throws Exception {

        String sql = "SELECT c.jobno,c.ID_CODE as invest_manage_idcard_no,c.cust_name  from ods_amng_cust_info c where not EXISTS( SELECT m.new_invest_id FROM t8_prod_invest_manager m  WHERE m.new_invest_id = c.jobno and	m.prod_code = $S{prodCode} AND m.STATUS = '0') and CUST_TYPE='2'";
        if (!Strings.isNullOrEmpty(params.getModel().getJobno())) {
        	 sql = "SELECT c.jobno,c.ID_CODE as invest_manage_idcard_no,c.cust_name  from ods_amng_cust_info c where not EXISTS( SELECT m.new_invest_id FROM t8_prod_invest_manager m  WHERE m.new_invest_id = c.jobno and	m.prod_code = $S{prodCode} AND m.STATUS = '0') and CUST_TYPE='2' and jobno =$S{jobno}";
        }
        return (SqlResult<T8Dict>) super.findRows(sql, params);
    }

    /**
     * 获取产品关联所有销售商名字字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findProdDistributorInfos(SqlParam<T8Dict> params) throws Exception {

        String sql = "SELECT tdi.distributor_code,tdi.distributor_name FROM T8_DISTRIBUTOR_INFO tdi " +
                "LEFT JOIN t8_prod_sale tps ON FIND_IN_SET(tdi.DISTRIBUTOR_CODE,tps.distributor_code) " +
                "WHERE tps.prod_code=$S{prodCode}";

        return (SqlResult<T8Dict>) super.findRows(sql, params);
    }

    /**
     * 产品页面销售商名字字典
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findTaProdDistributorCode(SqlParam<T8Dict> params) throws Exception {


        String sql =
                "			SELECT\r\n" +
                        "				distributor_code\r\n" +
                        "			FROM\r\n" +
                        "				t8_prod_sale\r\n" +
                        "			WHERE t8_prod_info_id = $S{t8ProdInfoId} or prod_code = $S{prodCode} ";
        return super.findRows(sql, params);
    }


    public SqlResult<T8Dict> findTaProdDistributorInfos(SqlParam<T8Dict> params) throws Exception {
        String code ="'',";
        if(StringUtils.isNotEmpty(params.getModel().getDistributorCode())){
            String arry[] = params.getModel().getDistributorCode().split(",");
            for(String str :arry) {
                code+="'"+str+"',";
            }

        }
        code = code.substring(0, code.length()-1);
        String sql =  "SELECT distributor_code,distributor_name FROM T8_DISTRIBUTOR_INFO  " +
                 "WHERE DISTRIBUTOR_CODE in("+code+")" ;

        return super.findRows(sql, params);
    }


    /**
     * 获取全部销售商
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findTaDisWithoutDefault(SqlParam<T8Dict> params) throws Exception {
        return (SqlResult<T8Dict>) super.findRows("SELECT distributor_code,distributor_name FROM T8_DISTRIBUTOR_INFO", params);
    }

    /**
     * 查询销售商未设置产品信息的产品信息字典
     *
     * @param params
     * @return
     * @throws Exception
     */

    public SqlResult<T8Dict> findTaDistributorNoSetProdInfo(SqlParam<T8Dict> params) throws Exception {

        return (SqlResult<T8Dict>) super.findRows(" SELECT  a.id as t8_prod_info_id,a.prod_code,a.prod_name" +
                " FROM t8_prod_info a WHERE a.data_status = 'E' AND a.prod_code NOT IN" +
                " (SELECT p.prod_code FROM t8_prod_info p  LEFT JOIN  t8_prod_distributor b" +
                " ON p.prod_code = b.prod_code WHERE b.distributor_code =$S{distributor_code})" +
                " AND a.PROD_LIFECYCLE not in ('0','3','7','8')" +
                " AND ( (SELECT d.BATCH_NO FROM T8_DISTRIBUTOR_INFO d WHERE d.distributor_code =$S{distributor_code}) = '0' OR" +
                " a.prod_code NOT IN (SELECT c.PROD_CODE FROM T8_PROD_OPEN c WHERE c.DATA_STATUS = 'E' " +
                "AND (c.REDEEM_CFM_M = '0' OR    c.APPLY_CFM_M = '0' OR c.SUBS_CFM_N = '0')))", params);
    }

    public SqlResult<T8Dict> findTaOpenProdInfos(SqlParam<T8Dict> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("SELECT prod_nm as prod_name,prod_cd prod_code FROM dwd_prd_prd_bas_inf where period_type = '0' ",params);

    }

    public SqlResult<T8Dict> findDictTrutee(SqlParam<T8Dict> params) throws Exception {
        params.setMakeSql(false);
        return super.findRows("select CONCAT(max(itemkey)+1) itemkey from sys_dict_item where dict = 't8_trutee_bank' and itemkey <> '99'",params);

    }


    public UpdateResult addDictTrutee(T8Dict params) throws Exception {
        return super.update("INSERT INTO sys_dict_item (dict,  itemval, itemkey) " +
                        "VALUES ($S{dict},  $S{itemval}, $S{itemkey})",
                params );
    }

    public UpdateResult deleteDictTrutee(T8Dict params) throws Exception {
        return super.update("delete from sys_dict_item where dict = $S{dict} and itemkey = $S{itemkey} and itemval <> '99'",
                params );
    }


    public UpdateResult updateDictTrutee(T8Dict params) throws Exception {
        return super.update("update sys_dict_item set  itemval = $S{itemval} where dict = $S{dict} and itemkey = $S{itemkey} and itemval <> '99'",
                params );
    }



    public  SqlResult<Map<String, Object>> findTempType(String dict) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start","0");
        map.put("limit","30");
        String sql = "select itemval,itemkey from sys_dict_item where dict in ('t8_temp_type_qt','"+dict+"') order by itemkey+0 ";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, map, this);
    }


    public SqlRow findTempTypeByDict(String dict,String itemval) throws Exception {

        String sql = "select itemval,itemkey from sys_dict_item where dict = '"+dict+"' and itemval='"+itemval+"' order by itemkey+0 ";
        return super.findRow(sql, DataSourceProperty.PUB, this);
    }

    public  SqlResult<Map<String, Object>> t8PrintDoc() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start","0");
        map.put("limit","30");
        String sql = "select itemval,itemkey from sys_dict_item where dict = 't8_print_doc' and itemkey <> '9' order by itemkey+0 ";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, map, this);
    }

    /**
     * 功能：查询非封闭式的产品  请勿擅自改动
     * 作者：rennannan
     * 日期：20210309
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findOpenInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT prod_cd as prod_code,prod_nm as prod_name FROM dwd_prd_prd_bas_inf where prod_mode > 1", params);
    }
    /**
     * 功能：查询非封闭式的产品  请勿擅自改动
     * 作者：rennannan
     * 日期：20210309
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<T8Dict> findSeriesInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT series_code,series_name FROM ods_amng_prod_series where son_flag='0' order by id desc", params);
    }
    public SqlResult<T8Dict> findSonSeriesInfos(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT series_code,series_name FROM ods_amng_prod_series where son_flag='1' order by id desc", params);
    }

    public SqlResult<T8Dict> findSonSeriesInfos1(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT series_code,series_name FROM ods_amng_prod_series WHERE son_flag='1' AND series_code NOT IN " +
                "(SELECT t8_series_id FROM t8_create_meeting_prod WHERE meeting_result='2' AND t8_series_id !='')", params);
    }

    public SqlResult<T8Dict> findSeriesExplain(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT t8_prod_series_id,series_explain FROM t8_create_relation where t8_prod_series_id = '"+params.getModel().getT8ProdSeriesId()+"'", params);
    }
    
    public  SqlResult<Map<String, Object>> XPPrintDoc() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start","0");
        map.put("limit","20");
        String sql = "select itemval,itemkey from sys_dict_item where dict = 'xp_doc_type'  order by itemkey+0 ";
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, map, this);
    }


    public SqlResult<T8Dict> findBaseDate(SqlParam<T8Dict> params) throws Exception {
        String itemkey = params.getModel().getItemkey();
        SqlResult<T8Dict> rows =null;
        if(StringUtils.isNotEmpty(itemkey)&&itemkey.contains(",")){
            rows = super.findRows("select itemval,itemkey from sys_dict_item where dict=$S{dict} and itemkey in("+params.getModel().getItemkey()+")", params);
        }else {
           rows = super.findRows("select itemval,itemkey from sys_dict_item where dict=$S{dict} and itemkey in($S{itemkey})", params);
        }
        return rows;
    }

    public SqlResult<T8Dict> findProdMod(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT prod_cd as prod_code, prod_nm as prod_mode FROM dwd_prd_prd_bas_inf where prod_cd=$S{prodCode}", params);
    }

	public SqlResult<T8Dict> findOperatingAgency(SqlParam<T8Dict> params)  throws Exception {
		 return super.findRows("SELECT operating_agency FROM t8_prod_custodian_email group by operating_agency ", params);
		
	}

    //获取子系列
    public SqlResult<T8Dict> getProdSonSeries(SqlParam<T8Dict> params) throws Exception {
        return super.findRows("SELECT series_code,series_name FROM ods_amng_prod_series WHERE parent_code = $S{id} and son_flag = '1'", params);
    }

    }
