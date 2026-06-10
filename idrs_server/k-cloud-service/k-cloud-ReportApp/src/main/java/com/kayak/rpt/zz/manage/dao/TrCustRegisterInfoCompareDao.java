package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.rpt.zz.manage.model.TrCustRegisterInfo;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrCustRegisterInfoCompareDao extends ComnDao {


	/**
	 * 赋予初始化的标识
	 * @param p1
	 * @param list0
	 * @return
	 */
	public TrCustRegisterInfo compareFlag(TrCustRegisterInfo p1, List<TrCustRegisterInfo> list0){
		TrCustRegisterInfo p3 = new TrCustRegisterInfo();
		String dataType = p1.getDataType();
		if(dataType.equals("01") || dataType.equals("02")){
			p3 = initInfo(p1);
		}else{
			for(int j = 0;j<list0.size();j++){
				TrCustRegisterInfo p2 = list0.get(j);
				if(p1.getCustNo().equals(p2.getCustNo())){
					p3 = compare(p1,p2);
				}
			}
		}
		if(StringUtils.isBlank(p3.getCustNo())){
			p3 = initInfo(p1);
		}
		return p3;
	}

	/**
	 * 比较两个对象，将对象字段值全部置为0和1
	 * @param p0
	 * @param p1
	 * @return
	 */
	public TrCustRegisterInfo compare(TrCustRegisterInfo p0,TrCustRegisterInfo p1){
		TrCustRegisterInfo p2 = new TrCustRegisterInfo();
		if(p0 != null && p1 != null){
			ObjectMapper o1 = new ObjectMapper();
			Map<String,Object> m0 = o1.convertValue(p0,Map.class);
			Map<String,Object> m1 = o1.convertValue(p1,Map.class);
			Map<String,Object> m2 = new HashMap<>();
			for(String key : m0.keySet()){
				boolean iscompare = false;//该key是否需要比较
					if(p0.getDataType().equals("03") ){
						if(key.equals("isBelong") || key.equals("issBankName")|| key.equals("issBankCode")|| key.equals("inOutSign")
								|| key.equals("issCountry") || key.equals("custType") || key.equals("personalIdType")|| key.equals("organizationIdType")
								|| key.equals("idCode")|| key.equals("spvOpenBank")|| key.equals("otherOpenBank") || key.equals("custName")|| key.equals("sex")){
							iscompare = true;
						}
					}else if(p0.getDataType().equals("04") ){
						if(key.equals("riskLevel") || key.equals("moble")|| key.equals("telPhone")|| key.equals("email")){
							iscompare = true;
						}
					}
					if(iscompare == true){
						if(m0.get(key) != null && m1.get(key) != null && !m0.get(key).toString().equals(m1.get(key).toString())){
							m2.put(key,"1");
						}else if(m0.get(key) != null && m1.get(key) == null){
							m2.put(key,"1");
						}else if(m0.get(key) == null && m1.get(key) != null){
							m2.put(key,"1");
						}else{
							m2.put(key,"0");
						}
					}else{
						m2.put(key,"0");
					}

			}
			m2.put("custNo",p0.getCustNo());
			m2.put("dataType",p0.getDataType());
			m2.put("id",p0.getId());
			m2.put("reportDate",p0.getRegisterDate());
			m2.put("registerSerno",p0.getRegisterSerno());
			p2 = o1.convertValue(m2,TrCustRegisterInfo.class);//反序列化
		}
		return p2;
	}
	/**
	 * 转换为新的数据对象--1.0版本直接初始化全部未0
	 * @param p0 需要初始化的数据
	 * @return
	 */
	public TrCustRegisterInfo initInfo(TrCustRegisterInfo p0){

		ObjectMapper o1 = new ObjectMapper();
		Map<String,Object> map = o1.convertValue(p0,Map.class);
		Map<String,Object> map01 = new HashMap<>();
		for(String key : map.keySet()){
			map01.put(key,"0");
		}
		map01.put("custNo",map.get("custNo"));
		map01.put("dataType",map.get("dataType"));
		map01.put("registerSerno",map.get("registerSerno"));
		map01.put("id",map.get("id"));
		map01.put("reportDate",map.get("reportDate"));
		TrCustRegisterInfo p1 = o1.convertValue(map01,TrCustRegisterInfo.class);
		return p1;
	}

}
