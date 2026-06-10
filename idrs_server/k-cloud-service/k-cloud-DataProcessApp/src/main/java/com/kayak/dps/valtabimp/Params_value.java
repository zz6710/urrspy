package com.kayak.dps.valtabimp;


import com.kayak.dps.valtabimp.model.DataParam;

import java.util.HashMap;
import java.util.Map;


public class Params_value {

	
	
	public static Map<String, String> code_name_map=new HashMap<>();
	
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
		share_Expr.configFieldnames= DataParam.configFieldnames;
		return shares.get();
	}
	
	
	
	
	public final static String VF_VOUCHERDETAIL_STR="VF_VOUCHERDETAIL";//凭证表
	public final static String VN_BM_BAL_STR="VN_BM_BAL";//余额表
	public final static String VN_FINANCIAL_VAL_STR="VN_FINANCIAL_VAL";//估值表
}
