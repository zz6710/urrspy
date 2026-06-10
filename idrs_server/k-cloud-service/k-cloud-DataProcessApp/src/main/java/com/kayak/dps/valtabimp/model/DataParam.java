package com.kayak.dps.valtabimp.model;


import com.kayak.dps.valtabimp.Share_Expr;

import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author 林波
 * <br>
 * 日期：2015-06-16
 * <br>
 * 说明：全局静态参数
 */
public class DataParam {
	public static String client_code="";
	public static Integer isRecord = 0;//是否记录跑批日志
	public static String messageAll = "";
	public static String messageErrorAll = "";
	public static Boolean ISCharging = false;//是否正在记账
	public static String begintime="";

	public static Map<Integer,T8prodbase> prodbaseMap=new HashMap<Integer, T8prodbase>();
	public static Map<Integer,T8prodbasesub> prodbasesubMap=new HashMap<Integer, T8prodbasesub>();

	public static Map<Integer,List<Vwprodpositionbln>> prodpositionbln=new  HashMap<Integer, List<Vwprodpositionbln>>();

	// 交易类型跑批事件配置参数缓存
	public static Map<String, List<Map<String,String>>> dealEventParam = new HashMap<String, List<Map<String,String>>>();
	// 资产负债品种表 T8_SYS_ADTYPE 中ID 与 AD_LEVEL 关系映射
	public static Map<Object,String> adLevelParam = new HashMap<Object,String>();
	/**
	 * 货币对的缓存对象，
	 * key规则:货币对代码_发生时间 String   value: 直接放RATE Double
	 */
	public static Map<String,Double>  MoneyRateAll=new HashMap<String,Double>();

	public static Integer is_coutpa=0;//参数

	public static Integer is_sure=0;//参数是否自动到期 ["0","手动确认"],["1","自动确认"]

	public static Map configFieldnames = new HashMap();//配置字段  MAp集合 key为name
	public static Map<Integer,Vwsysaccevent> listevents=new HashMap<Integer, Vwsysaccevent>();//变动类型
	public static Map<String,List<Dictitem>> resultMap=new HashMap<String, List<Dictitem>>();//数据字典
	public static Hashtable<String,Integer> sysSequenceMap=new Hashtable<>();//序列ID
	public static Hashtable<String,Integer> alreadyUseSequenceMap=new Hashtable<>();//估值、跑批已经被使用的序列ID
	public static Hashtable<Integer, Hashtable<String,Hashtable<String, Double>>> accountSubject=new Hashtable<>();//科目余额存储（跑完批、估完值后清掉）

	public static Map<Integer,Vwsysadtype> listadtypes=new HashMap<Integer, Vwsysadtype>();

	public static Integer isRepbondFL=1;//回购分录是按主交易还是抵押券
	public static Integer isVirtualSell=0;//资产是否虚拟卖出
	public static Integer isGenerationLoss=0;//是否产生核心损益分录
	public static Integer isdeposit=0;//是否出托管计提
	/*Modi by：zzl   ,   date：2019/1/8     ,   reason：   计提截止日期*/
	public static Date Provision_EndDate=null;
	/*Modi by：zzl   ,   date：2019/1/10     ,   reason：  计提间隔天数机制是否开启 0：关闭  1:开启*/
	public static final Integer Is_Provision_Days=1;

	/*Modi by：zzl   ,   date：2019/06/11     ,   reason：   系统工作日*/
	public static Date SYS_WORK_DATE=null;

	/**
	 * 估值重跑计提日期
	 */
	public static Date ProdValuation_Date = null;

	public static String loginname;
	public static DecimalFormat df = new DecimalFormat("0.00");
	public static HashMap<String, VwchangeData> RepoJtjgHmap = new HashMap<String, VwchangeData>();
	public static Map<String, Object> viewparams=new HashMap<String, Object>();//前台传递的参数值
	//public static Share_Expr share;

//	public static DataArchiveServerUtil dataArchiveServerUtil=null;

	public static String jobs="0";//0表示正常,1表示是后台自动跑批

	public static List<Vwsysfieldcfg> lisfldcfg=null;//配置字段

	public static Date endDealdate=null;//如果验证超卖最后，持仓正好为0·那么就需要用最后交易时间截取

	public static Date taxPayDate=null;//起始缴税日期

	public static Integer isgzpp=0;//是否公允价跑批

	public static Map<String,String> bondPropMap = new HashMap<String,String>();//债券属性
	public static Map<String ,String> bondMatureInfo=new  HashMap<String, String>();//债券到期日

	public static Map<String ,Double> bondpubprice=new  HashMap<String, Double>();//债券发行价格


	public static Map<String ,Integer> noassetbasedays=new  HashMap<String, Integer>();//非标计息基数

	public static Map<Integer ,Integer> prodisFx=new  HashMap<Integer, Integer>();//判断理财产品是否发生自定义的中途付息

	public static Map<String ,String> assetpropinfo=new  HashMap<String, String>();//判断非标资产属性

	public static Map<String ,String> fundpropinfo=new  HashMap<String, String>();//判断非标资产属性

	//费用收益率集合
	public static Map<String,Map<String,Double>> yeillist=new HashMap<String,Map<String,Double>>();

	//非标资产是否是最后计提
	public  static Map<String, String> assetendinfo =new  HashMap<String,String>();

	public static Integer proRedeemIn=0;//产品赎回利息是否可透支

	/**
	 * 实体英文字段为KEY值
	 */
	@SuppressWarnings("rawtypes")
	public static Map configFields = new HashMap();//配置字段 Map集合  key为code


	//托管行收益率集合
	public static Map<String,Map<String,Double>> custyeillist=new HashMap<String,Map<String,Double>>();

	//产品本金余额集合
	public static Map<String,Map<String,Double>> prodblnlist=new HashMap<String,Map<String,Double>>();

	//产品赎回业绩报酬金额集合
	public static Map<String,Map<String,Double>> prodYjbclist=new HashMap<String,Map<String,Double>>();

	//记录产品的最大估值日期
	public static Map<String,String> prod_MAX_VALDATE=new HashMap<String,String>();

	// 税费重算时，缓存每个产品的最小业务日期
	public static Map<String, Map<String, String>> prod_min_settle_date = new HashMap<String, Map<String, String>>();

	/**
	 * 用于每一次汇整的集合
	 */
	public static void InitDateforJs(){
		//初始化 参数
		getComputMsgList().clear();//初始化日志
		getFlList().clear();
		is_coutpa=0;
		isRecord ++;
		getFlListkexin().clear();
		//bondMatureInfo.clear();
		//bondPropMap.clear();
		//bondpubprice.clear();
		//noassetbasedays.clear();
		//prodisFx.clear();
		//assetpropinfo.clear();
		//fundpropinfo.clear();
		//assetendinfo.clear();
	}

	/**
	 * 清楚所有跑批前置缓存
	 */
	public static void clearALLDataParam(){

		DataParam.resultMap.clear();
		DataParam.resultMap=null;
		DataParam.resultMap=new HashMap<String, List<Dictitem>>();
		DataParam.listevents.clear();
		DataParam.listevents=null;
		DataParam.listevents=new HashMap<Integer, Vwsysaccevent>();
		DataParam.listadtypes.clear();
		DataParam.listadtypes=null;
		DataParam.listadtypes=new HashMap<Integer, Vwsysadtype>();


		DataParam.configFields.clear();
		DataParam.configFields=null;
		DataParam.configFields=new HashMap<Integer, Vwsysadtype>();
		DataParam.configFieldnames.clear();
		DataParam.configFieldnames=null;
		DataParam.configFieldnames=new HashMap<Integer, Vwsysadtype>();


		DataParam.prodbaseMap.clear();
		DataParam.prodbaseMap=null;
		DataParam.prodbaseMap=new HashMap<Integer, T8prodbase>();

		DataParam.prodbasesubMap.clear();
		DataParam.prodbasesubMap=null;
		DataParam.prodbasesubMap=new HashMap<Integer, T8prodbasesub>();

		DataParam.prodpositionbln.clear();
		DataParam.prodpositionbln=null;
		DataParam.prodpositionbln=new HashMap<Integer, List<Vwprodpositionbln>>();

		//收益率集合

		DataParam.yeillist.clear();
		DataParam.yeillist=null;
		DataParam.yeillist=new HashMap<String, Map<String,Double>>();

		DataParam.MoneyRateAll.clear();

		//收益率集合

		DataParam.custyeillist.clear();
		DataParam.custyeillist=null;
		DataParam.custyeillist=new HashMap<String, Map<String,Double>>();

		DataParam.prod_MAX_VALDATE.clear();
		DataParam.prod_MAX_VALDATE=null;
		DataParam.prod_MAX_VALDATE=new HashMap<String,String>();

		bondMatureInfo.clear();
		bondPropMap.clear();
		bondpubprice.clear();
		noassetbasedays.clear();
		prodisFx.clear();
		assetpropinfo.clear();
		fundpropinfo.clear();
		assetendinfo.clear();

		prodblnlist.clear();
		DataParam.prodblnlist.clear();
		DataParam.prodblnlist=null;
		DataParam.prodblnlist=new HashMap<String, Map<String,Double>>();

		prodYjbclist.clear();
		DataParam.prodYjbclist.clear();
		DataParam.prodYjbclist=null;
		DataParam.prodYjbclist=new HashMap<String, Map<String,Double>>();
	}

	//清除产品科目余额集合
	public static void clearAccountSubject(){
		for (Integer prodidKey : accountSubject.keySet()){
			for (String dataKey : accountSubject.get(prodidKey).keySet()){
				accountSubject.get(prodidKey).get(dataKey).clear();
			}
			accountSubject.get(prodidKey).clear();
		}
		accountSubject.clear();
	}


	public static ThreadLocal<List<Vwcomputlog>> computMsgList=new ThreadLocal<List<Vwcomputlog>>();//初始化日志;
	public static void setComputMsgList(List<Vwcomputlog> exp){
		computMsgList.set(exp);
	}

	public static List<Vwcomputlog> getComputMsgList(){
		List<Vwcomputlog> exp = computMsgList.get();
		if(exp == null){
			exp = new ArrayList<Vwcomputlog>();
			computMsgList.set(exp);
		}
		return computMsgList.get();
	}

	public static ThreadLocal<List<Map<String,Object>>> flList=new ThreadLocal<List<Map<String,Object>>>();//分录集合
	public static void setFlList(List<Map<String,Object>> exp){
		flList.set(exp);
	}

	public static List<Map<String,Object>> getFlList(){
		List<Map<String,Object>> exp = flList.get();
		if(exp == null){
			exp = new ArrayList<Map<String,Object>>();
			flList.set(exp);
		}
		return flList.get();
	}

	public static ThreadLocal<List<Map<String,Object>>> flListkexin=new ThreadLocal<List<Map<String,Object>>>();//核心分录集合
	public static void setFlListkexin(List<Map<String,Object>> exp){
		flListkexin.set(exp);
	}

	public static List<Map<String,Object>> getFlListkexin(){
		List<Map<String,Object>> exp = flListkexin.get();
		if(exp == null){
			exp = new ArrayList<Map<String,Object>>();
			flListkexin.set(exp);
		}
		return flListkexin.get();
	}

	private static ThreadLocal<Share_Expr> shares = new ThreadLocal<Share_Expr>();
	public static void setShare(Share_Expr exp){
		shares.set(exp);
	}

	public static Share_Expr getShare(){
		Share_Expr share_Expr = shares.get();
		if(share_Expr == null){
			share_Expr = new Share_Expr();
			shares.set(share_Expr);
		}
		share_Expr.configFieldnames=DataParam.configFieldnames;
		return shares.get();
	}
}
