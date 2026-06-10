package com.kayak.dps.valtabimp.repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.valtabimp.model.DataParam;
import com.kayakwise.kcloud.db.Dbop;
import com.kayakwise.kcloud.db.util.ParamMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

//表示数据层的bean定义
@Slf4j
@Repository(value = "valTabImpDataDao")
public class ValTabImpDataDao {


	@Autowired
	private ComnDao comnDao;

	@Autowired
	private Dbop dbop;

	/**
	 *
	 * 描述 : 估值表设置参数数据插入记录表
	 * @throws SQLException
	 *
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addvalparamset(List<Map<String, Object>> list,String t8_val_reporttab_id) throws Exception {

		String sqldel="delete from base_fa_reporttab_parset where 1=1 and t8_val_reporttab_id = "+t8_val_reporttab_id;
		comnDao.update(sqldel);

		String sql = "insert into base_fa_reporttab_parset (id,t8_val_reporttab_id,param_type,param_code,param_name,param_data_type,param_value,param_condition,note,inputuser,crt_date,crt_time,order_num)\n" +
				"\t\t\tvalues(\n" +
				"\t\t\t $AUTOIDS{id},\n" +
				"\t\t\t  $S{t8_val_reporttab_id},\n" +
				"\t\t\t  $S{param_type},\n" +
				"\t\t\t  $S{param_code},\n" +
				"\t\t\t  $S{param_name},\n" +
				"\t\t\t  $S{param_data_type},\n" +
				"\t\t\t  $S{param_value},\n" +
				"\t\t\t  $S{param_condition},\n" +
				"\t\t\t  $S{note},\n" +
				"\t\t\t  $S{sys_user_loginname},\n" +
				"\t\t\t  $S{SYSDATE},\n" +
				"\t\t\t  $S{SYSTIME},\n" +
				"\t\t\t  $S{order_num})";
		for (Map<String, Object> map:list) {
			comnDao.update(sql, map);
		}
	}


	/**
	 *
	 * 插入解析的估值数据
	 * @throws SQLException
	 *
	 */
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addvaldata(List<Map<String, Object>> list,String asset_code, String nowdate,String isprodorasset) throws Exception {

		String sqldel="delete from ods_fa_readassets where 1=1 and asset_code = '"+asset_code +"' and change_date='"+nowdate+"' and isprodorasset='"+isprodorasset+"'";
		comnDao.update(sqldel);

			List<ParamMap> paramMaps = new ArrayList<>();
	        int i =0 ;
			for(Map<String, Object> map:list) {
				ParamMap paramMap = new ParamMap().on("asset_code", asset_code)
						.on("prod_name", "")
						.on("change_date", nowdate)
						.on("isprodorasset", isprodorasset)
						.on("ftool_code", map.get("ftool_code")==null?"":map.get("ftool_code").toString())
						.on("ftool_name", map.get("ftool_name")==null?"":map.get("ftool_name").toString())
						.on("t8_sys_adtype_id", map.get("t8_sys_adtype_id")==null?"":map.get("t8_sys_adtype_id").toString())
						.on("market", map.get("market")==null?"":map.get("market").toString())
						.on("positionbln", map.get("positionbln")==null||"".equals(map.get("positionbln"))?0:map.get("positionbln").toString())
						.on("interestbln", map.get("interestbln")==null||"".equals(map.get("interestbln"))?0:map.get("interestbln").toString())
						.on("accruedincomebln", map.get("accruedincomebln")==null||"".equals(map.get("accruedincomebln"))?0:map.get("accruedincomebln").toString())
						.on("npamountbln", map.get("npamountbln")==null||"".equals(map.get("npamountbln"))?0:map.get("npamountbln").toString())
						.on("feepaybln", map.get("feepaybln")==null||"".equals(map.get("feepaybln"))?0:map.get("feepaybln").toString())
						.on("fairvaluebln", map.get("fairvaluebln")==null||"".equals(map.get("fairvaluebln"))?0:map.get("fairvaluebln").toString())
						.on("taxfeebln", map.get("taxfeebln")==null||"".equals(map.get("taxfeebln"))?0:map.get("taxfeebln").toString())
						.on("pay_taxbln", map.get("pay_taxbln")==null||"".equals(map.get("pay_taxbln"))?0:map.get("pay_taxbln").toString())
						.on("accruedpaybln", map.get("accruedpaybln")==null||"".equals(map.get("accruedpaybln"))?0:map.get("accruedpaybln").toString())
						.on("securitiesliquidationbln", map.get("securitiesliquidationbln")==null||"".equals(map.get("securitiesliquidationbln"))?0:map.get("securitiesliquidationbln").toString())
						.on("principalbln", map.get("principalbln")==null||"".equals(map.get("principalbln"))?0:map.get("principalbln").toString())
						.on("jrjs_value", map.get("jrjs_value")==null||"".equals(map.get("jrjs_value"))?0:map.get("jrjs_value").toString())
						.on("balance", map.get("balance")==null||"".equals(map.get("balance"))?0:map.get("balance").toString())
						.on("account_type", map.get("account_type")==null||"".equals(map.get("account_type"))?0:map.get("account_type").toString())
						.on("net_price", map.get("net_price")==null||"".equals(map.get("net_price"))?0:map.get("net_price").toString())
						.on("holding_direction", map.get("holding_direction")==null||"".equals(map.get("holding_direction"))?0:map.get("holding_direction").toString());
				paramMaps.add(paramMap);
				i++;
			}

			dbop.updateBatch("M080710", paramMaps);

		//数据合并擦表
		Map<String, Object> map = new HashMap<>();
		map.put("asset_code", asset_code);
		map.put("change_date", nowdate);
		map.put("isprodorasset", isprodorasset);
		//查询合并后数据
    	List<SqlRow> sqlRows;
		String sql = "select " +
				     "       t.asset_code,t.change_date,t.ftool_code,t.ftool_name,t.t8_sys_adtype_id,t.market, " +
				     "       sum(positionbln) as positionbln ,sum(interestbln) as interestbln ,sum(accruedincomebln) as accruedincomebln, " +
				     "       sum(npamountbln) as npamountbln,sum(feepaybln) as feepaybln,sum(fairvaluebln) as fairvaluebln ,sum(taxfeebln) as taxfeebln, " +
				     "       sum(pay_taxbln) as pay_taxbln ,sum(accruedpaybln)as accruedpaybln,sum(securitiesliquidationbln) as securitiesliquidationbln, " +
				     "       sum(principalbln) as principalbln, " +
				     "       sum(jrjs_value) as jrjs_value, " +
				     "       sum(t.balance) as balance, " +
				     "       t.account_type, t.net_price, t.holding_direction " +
				     "  from ods_fa_readassets t " +
				     " where 1=1 " +
				     "   and (isnull($S{asset_code}) or t.asset_code = $S{asset_code}) " +
				     "   and (isnull($S{prod_name}) or t.prod_name = $S{prod_name}) " +
				     "   and (isnull($S{isprodorasset}) or t.isprodorasset = $S{isprodorasset}) " +
				     "   and t.change_date = $S{change_date} " +
				     " group by t.asset_code,t.change_date,t.ftool_code,t.t8_sys_adtype_id,t.market,t.ftool_name,t.holding_direction";
		try {
			sqlRows = comnDao.findRows(sql, map);


			String mergeDel = "delete from ods_fa_readassets_merge where 1=1 and asset_code = '"+asset_code +"' and change_date='"+nowdate+"' and isprodorasset='"+isprodorasset+"'";
			comnDao.update(mergeDel);
			//交易1抵押券信息
	        List<ParamMap> paramMaList = new ArrayList<>();
	        i = 0;
			dbop.starttrans();
			for(SqlRow sr : sqlRows) {
				ParamMap paramMap = new ParamMap().on("asset_code", asset_code)
						.on("prod_name", "")
						.on("change_date", nowdate)
						.on("isprodorasset", isprodorasset)
						.on("ftool_code", sr.getString("ftool_code"))
						.on("ftool_name", sr.getString("ftool_name"))
						.on("t8_sys_adtype_id", sr.getString("t8_sys_adtype_id"))
						.on("market", sr.getString("market"))
						.on("positionbln", sr.getString("positionbln"))
						.on("interestbln", sr.getString("interestbln"))
						.on("accruedincomebln", sr.getString("accruedincomebln"))
						.on("npamountbln", sr.getString("npamountbln"))
						.on("feepaybln", sr.getString("feepaybln"))
						.on("fairvaluebln", sr.getString("fairvaluebln"))
						.on("taxfeebln", sr.getString("taxfeebln"))
						.on("pay_taxbln", sr.getString("pay_taxbln"))
						.on("accruedpaybln", sr.getString("accruedpaybln"))
						.on("securitiesliquidationbln", sr.getString("securitiesliquidationbln"))
						.on("principalbln", sr.getString("principalbln"))
						.on("jrjs_value", sr.getString("jrjs_value"))
						.on("balance", sr.getString("balance"))
						.on("account_type", sr.getString("account_type"))
						.on("net_price",sr.getString("net_price"))
						.on("holding_direction",sr.getString("holding_direction"));
				paramMaList.add(paramMap);
				i++;
			}
	        dbop.updateBatch("M080711", paramMaList);
			dbop.commit();
		} catch (Exception e) {
			log.error("-----初始化化估值表解析参数异常",e );
			try {
				dbop.rollback();
			} catch (SQLException ex) {
				log.error(" 批量入库回滚失败: " + ex.getMessage(), ex);
			}
		}
	}


	/**
	 * 插入解析的估值数据---自动导入
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addvaldataByauto(List<Map<String, Object>> list,String prod_name, String nowdate,String isprodorasset) throws Exception {

		prod_name=prod_name.toLowerCase().replaceAll(".xls", "");
		prod_name=prod_name.toUpperCase();//转大写
		prod_name=prod_name.replaceAll(nowdate,"");//把估值日期替换成空字符
		String sqldel="delete from ods_fa_readassets where 1=1 and prod_name = '"+prod_name +"' and change_date='"+nowdate+"' and isprodorasset='"+isprodorasset+"'";
		comnDao.update(sqldel);
		//获取id组
		List<Integer> IDlis = getid("ods_fa_readassets",list.size());

		String[] ods_fa_readassets_id=new String[list.size()];
		String[] ftool_code =new String[list.size()];
        String[] ftool_name =new String[list.size()];
        String[] t8_sys_adtype_id =new String[list.size()];
        String[] market =new String[list.size()];
        String[] positionbln = new String[list.size()];
        String[] interestbln = new String[list.size()];
        String[] accruedincomebln = new String[list.size()];
        String[] npamountbln = new String[list.size()];
        String[] feepaybln = new String[list.size()];
        String[] fairvaluebln = new String[list.size()];
        String[] taxfeebln = new String[list.size()];
        String[] pay_taxbln = new String[list.size()];
        String[] accruedpaybln = new String[list.size()];
        String[] securitiesliquidationbln = new String[list.size()];
        String[] principalbln = new String[list.size()];
        String[] jrjs_value = new String[list.size()];
        String[] balance = new String[list.size()];
        int i =0 ;
		for(Map<String, Object> map:list) {
			ods_fa_readassets_id[i]=IDlis.get(i).toString();//id
			ftool_code[i]=map.get("ftool_code")==null?"":map.get("ftool_code").toString();
			ftool_name[i]=map.get("ftool_name")==null?"":map.get("ftool_name").toString();
			t8_sys_adtype_id[i]=map.get("t8_sys_adtype_id")==null?"":map.get("t8_sys_adtype_id").toString();
			market[i]=map.get("market")==null?"":map.get("market").toString();
			positionbln[i]=map.get("positionbln")==null?"":map.get("positionbln").toString();
			interestbln[i]=map.get("interestbln")==null?"":map.get("interestbln").toString();
			accruedincomebln[i]=map.get("accruedincomebln")==null?"":map.get("accruedincomebln").toString();
			npamountbln[i]=map.get("npamountbln")==null?"":map.get("npamountbln").toString();
			feepaybln[i]=map.get("feepaybln")==null?"":map.get("feepaybln").toString();
			fairvaluebln[i]=map.get("fairvaluebln")==null?"":map.get("fairvaluebln").toString();
			taxfeebln[i]=map.get("taxfeebln")==null?"":map.get("taxfeebln").toString();
			pay_taxbln[i]=map.get("pay_taxbln")==null?"":map.get("pay_taxbln").toString();
			accruedpaybln[i]=map.get("accruedpaybln")==null?"":map.get("accruedpaybln").toString();
			securitiesliquidationbln[i]=map.get("securitiesliquidationbln")==null?"":map.get("securitiesliquidationbln").toString();
			principalbln[i]=map.get("principalbln")==null?"":map.get("principalbln").toString();
			jrjs_value[i]=map.get("jrjs_value")==null?"":map.get("jrjs_value").toString();
			balance[i]=map.get("balance")==null?"":map.get("balance").toString();
			i++;
		}

		Map<String,Object> insertMap = new HashMap<>();
		//查找系统真实理财产品ID
		String sqlStr="select b.id,b.prod_code ,b.prod_name from ods_amng_prod_base b inner join t8_prod_mapp_val v on v.prod_name=b.prod_name where v.val_prod_name='"+prod_name+"'";
		List<SqlRow> sqlRows = comnDao.findRows(sqlStr);
		String t8_prod_base_id="";
		for (SqlRow sr : sqlRows) {
			t8_prod_base_id=sr.getString("id");
		}
		insertMap.put("t8_prod_base_id", t8_prod_base_id);
		insertMap.put("prod_name", prod_name);
        insertMap.put("change_date", nowdate);
        insertMap.put("isprodorasset", isprodorasset);

        insertMap.put("ods_fa_readassets_id",ods_fa_readassets_id);
        insertMap.put("ftool_code",ftool_code);
        insertMap.put("ftool_name",ftool_name);
        insertMap.put("t8_sys_adtype_id",t8_sys_adtype_id);
        insertMap.put("market",market);
        insertMap.put("positionbln",positionbln);
        insertMap.put("interestbln",interestbln);
        insertMap.put("accruedincomebln",accruedincomebln);
        insertMap.put("npamountbln",npamountbln);
        insertMap.put("feepaybln",feepaybln);
        insertMap.put("fairvaluebln",fairvaluebln);
        insertMap.put("taxfeebln",taxfeebln);
        insertMap.put("pay_taxbln",pay_taxbln);
        insertMap.put("accruedpaybln",accruedpaybln);
        insertMap.put("securitiesliquidationbln",securitiesliquidationbln);
        insertMap.put("principalbln",principalbln);
        insertMap.put("jrjs_value",jrjs_value);
        insertMap.put("balance",balance);



        comnDao.update("insert into ods_fa_readassets (id,t8_prod_base_id,prod_name,isprodorasset,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,positionbln,accruedincomebln,\n" +
				"\t\t\tnpamountbln,FAIRVALUEBLN,FEEPAYBLN,TAXFEEBLN,PAY_TAXBLN,ACCRUEDPAYBLN,SECURITIESLIQUIDATIONBLN,INTERESTBLN,PRINCIPALBLN,jrjs_value,inputuser,crt_date,crt_time,balance)\n" +
				"\t\t\tvalues(\n" +
				"\t\t\t $S{ods_fa_readassets_id},\n" +
				"\t\t\t  ifnull($S{t8_prod_base_id},null),\n" +
				"\t\t\t  ifnull($S{prod_name},null),\n" +
				"\t\t\t  ifnull($S{isprodorasset},1),/*默认产品*/\n" +
				"\t\t\t  ifnull($S{change_date},null),\n" +
				"\t\t\t  ifnull($S{ftool_code},null),\n" +
				"\t\t\t  ifnull($S{ftool_name},null),\n" +
				"\t\t\t  ifnull($S{t8_sys_adtype_id},null),\n" +
				"\t\t\t  ifnull($S{market},null),\n" +
				"\t\t\t  ifnull($S{positionbln},null),\n" +
				"\t\t\t  ifnull($S{accruedincomebln},null),\n" +
				"\t\t\t  ifnull($S{npamountbln},null),\n" +
				"\t\t\t  ifnull($S{fairvaluebln},null),\n" +
				"\t\t\t  ifnull($S{feepaybln},null),\n" +
				"\t\t\t  ifnull($S{taxfeebln},null),\n" +
				"\t\t\t  ifnull($S{pay_taxbln},null),\n" +
				"\t\t\t  ifnull($S{accruedpaybln},null),\n" +
				"\t\t\t  ifnull($S{securitiesliquidationbln},null),\n" +
				"\t\t\t  ifnull($S{interestbln},null),\n" +
				"\t\t\t  ifnull($S{principalbln},null),\n" +
				"\t\t\t  ifnull($S{jrjs_value},null),\n" +
				"\t\t\t  $S{sys_user_loginname},\n" +
				"\t\t\t  $S{SYSDATE},\n" +
				"\t\t\t  $S{SYSTIME},\n" +
				"\t\t\t  ifnull($S{balance},null))", insertMap);



		//数据合并擦表
		Map<String, Object> map=new HashMap<>();
		map.put("prod_name", prod_name);
		map.put("change_date", nowdate);
		map.put("isprodorasset", isprodorasset);

		//查询合并后数据
    	List<SqlRow> sqlRowList;

		try {
			sqlRowList = comnDao.findRows("M8G05EQ006_01", map);
			 Integer countnum=sqlRowList.size();
	         ftool_code =new String[countnum];
	         ftool_name =new String[countnum];
	         t8_sys_adtype_id =new String[countnum];
	         market =new String[countnum];
	         positionbln = new String[countnum];
	         interestbln = new String[countnum];
	         accruedincomebln = new String[countnum];
	         npamountbln = new String[countnum];
	         feepaybln = new String[countnum];
	         fairvaluebln = new String[countnum];
	         taxfeebln = new String[countnum];
	         pay_taxbln = new String[countnum];
	         accruedpaybln = new String[countnum];
	         securitiesliquidationbln = new String[countnum];
	         principalbln = new String[countnum];
	         jrjs_value = new String[countnum];
			balance = new String[countnum];
	         String[] ods_fa_readassets_merge_id=new String[countnum];

	         //id获取
	         IDlis=getid("ods_fa_readassets_merge",countnum);
	        i =0 ;
			for (SqlRow sr : sqlRowList) {

				ods_fa_readassets_merge_id[i]=IDlis.get(i).toString();//id

				ftool_code[i]=sr.getString("ftool_code");
				ftool_name[i]=sr.getString("ftool_name");
				t8_sys_adtype_id[i]=sr.getString("t8_sys_adtype_id");
				market[i]=sr.getString("market");
				positionbln[i]=sr.getString("positionbln");
				interestbln[i]=sr.getString("interestbln");
				accruedincomebln[i]=sr.getString("accruedincomebln");
				npamountbln[i]=sr.getString("npamountbln");
				feepaybln[i]=sr.getString("feepaybln");
				fairvaluebln[i]=sr.getString("fairvaluebln");
				taxfeebln[i]=sr.getString("taxfeebln");
				pay_taxbln[i]=sr.getString("pay_taxbln");
				accruedpaybln[i]=sr.getString("accruedpaybln");
				securitiesliquidationbln[i]=sr.getString("securitiesliquidationbln");
				principalbln[i]=sr.getString("principalbln");
				jrjs_value[i]=sr.getString("jrjs_value");
				balance[i]=sr.getString("balance");
				i++;
			}

			//交易1抵押券信息
	        insertMap = new HashMap<>();
	        insertMap.put("prod_name", prod_name);
	        insertMap.put("change_date", nowdate);
	        insertMap.put("isprodorasset", isprodorasset);
	        insertMap.put("t8_prod_base_id", t8_prod_base_id);

	        insertMap.put("ods_fa_readassets_merge_id",ods_fa_readassets_merge_id);
	        insertMap.put("ftool_code",ftool_code);
	        insertMap.put("ftool_name",ftool_name);
	        insertMap.put("t8_sys_adtype_id",t8_sys_adtype_id);
	        insertMap.put("market",market);
	        insertMap.put("positionbln",positionbln);
	        insertMap.put("interestbln",interestbln);
	        insertMap.put("accruedincomebln",accruedincomebln);
	        insertMap.put("npamountbln",npamountbln);
	        insertMap.put("feepaybln",feepaybln);
	        insertMap.put("fairvaluebln",fairvaluebln);
	        insertMap.put("taxfeebln",taxfeebln);
	        insertMap.put("pay_taxbln",pay_taxbln);
	        insertMap.put("accruedpaybln",accruedpaybln);
	        insertMap.put("securitiesliquidationbln",securitiesliquidationbln);
	        insertMap.put("principalbln",principalbln);
	        insertMap.put("jrjs_value",jrjs_value);
	        insertMap.put("balance",balance);


	        comnDao.update("DELETE FROM ods_fa_readassets_merge t where 1=1 and ($isnull{t8_prod_base_id} or t.T8_PROD_BASE_ID=$S{t8_prod_base_id}) and ($isnull{prod_name} or t.prod_name=$S{prod_name})  and  t.change_date=$S{change_date}", insertMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//log.error("-----初始化化估值表解析参数异常"+e.getLocalizedMessage() );
		}

	}

	/**
	 * 获取单个ID
	 * @param tablesname
	 * @return
	 */
	public Integer getid(String tablesname){
		List<Integer> s=getid(tablesname, 1);

		Integer id=s.get(0);

		return id;
	}

	/**
	 * 获取一组ID
	 * @param tablesname
	 * @param leng
	 * @return
	 */
	public synchronized List<Integer> getid(String tablesname,int leng){
		List<Integer> lis=new ArrayList<>();
		//转小写
		tablesname=tablesname.toLowerCase();
		//初始ID
		Integer maxid = 0;
		//判断缓存是否存在tablename
		if(!DataParam.sysSequenceMap.containsKey(tablesname)){
			//不存在则获取最大ID并加入到缓存中
			DataParam.sysSequenceMap.put(tablesname,this.getSysSequence(tablesname));
		}
		maxid=DataParam.sysSequenceMap.get(tablesname);//获取缓存maxid
		DataParam.sysSequenceMap.remove(tablesname);//释放掉原有的值
		DataParam.sysSequenceMap.put(tablesname,maxid+leng);//重新设置maxid
		DataParam.alreadyUseSequenceMap.put(tablesname,maxid+leng);//存放到已使用集合中
		for (int i = 1; i <= leng; i++) {
			lis.add(maxid + i);
		}
		return lis;
	}

	/**
	 * 序列初始化方法
	 * @return
	 */
	public Integer getSysSequence(String tablename){
		Integer maxid=0;
		try {
			List<SqlRow> sqlRows =comnDao.findRows("select tablename ,maxid  from sys_sequence where tablename='"+tablename+"'");
			for (SqlRow sr : sqlRows) {
				maxid=sr.getInteger("maxid");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return maxid;
	}
}

