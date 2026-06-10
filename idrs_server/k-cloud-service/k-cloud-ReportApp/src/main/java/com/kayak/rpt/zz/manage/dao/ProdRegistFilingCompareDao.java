package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.rpt.zz.manage.model.ProdRegistFilingInfo;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProdRegistFilingCompareDao extends ComnDao {


	/**
     * 赋予初始化的标识
	 * @param p1
     * @param list0
     * @return
     */
	public ProdRegistFilingInfo compareFlag(ProdRegistFilingInfo p1, List<ProdRegistFilingInfo> list0){
		ProdRegistFilingInfo p3 = new ProdRegistFilingInfo();
		String p1Version = p1.getSysDataVersion();
		if(p1Version.equals("1.0")){
			p3 = initInfo(p1);
		}else{
			String p2Version = getNewVersion(p1Version);
			for(int j = 0;j<list0.size();j++){
				ProdRegistFilingInfo p2 = list0.get(j);
				if(p1.getIdentCode().equals(p2.getIdentCode())){
					if(p2Version.equals(p2.getSysDataVersion())){
						p3 = compare(p1,p2);
					}
				}
			}
		}
		if(StringUtils.isBlank(p3.getIdentCode())){
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
	public ProdRegistFilingInfo compare(ProdRegistFilingInfo p0,ProdRegistFilingInfo p1){
		ProdRegistFilingInfo p2 = new ProdRegistFilingInfo();
		if(p0 != null && p1 != null){
			ObjectMapper o1 = new ObjectMapper();
			Map<String,Object> m0 = o1.convertValue(p0,Map.class);
			Map<String,Object> m1 = o1.convertValue(p1,Map.class);
			Map<String,Object> m2 = new HashMap<>();
			for(String key : m0.keySet()){
				if(key.equals("id")){
					m2.put(key,m0.get("id"));
				}else{
					if(m0.get(key) != null && m1.get(key) != null && !m0.get(key).toString().equals(m1.get(key).toString())){
						m2.put(key,"1");
					}else if(m0.get(key) != null && m1.get(key) == null){
						m2.put(key,"1");
					}else if(m0.get(key) == null && m1.get(key) != null){
						m2.put(key,"1");
					}else{
						m2.put(key,"0");
					}
				}
			}
			m2.put("identCode",p0.getIdentCode());
			m2.put("sysDataVersion",p0.getSysDataVersion());
			m2.put("registerSerno",p0.getRegisterSerno());
			p2 = o1.convertValue(m2,ProdRegistFilingInfo.class);//反序列化
		}
		return p2;
	}
	/**
	 * 获得新的版本号
	 * @param version
	 * @return
	 */
	public static String getNewVersion(String version){
		Double v1 = Double.valueOf(version);
		Double v2 = v1 - 0.1;
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(v2);
	}

	/**
	 * 转换为新的数据对象--1.0版本直接初始化全部未0
	 * @param p0 需要初始化的数据
	 * @return
	 */
	public ProdRegistFilingInfo initInfo(ProdRegistFilingInfo p0){

		ObjectMapper o1 = new ObjectMapper();
		Map<String,Object> map = o1.convertValue(p0,Map.class);
		Map<String,Object> map01 = new HashMap<>();
		for(String key : map.keySet()){
			map01.put(key,"0");
		}
		map01.put("identCode",map.get("identCode"));
		map01.put("sysDataVersion",map.get("sysDataVersion"));
		map01.put("registerSerno",map.get("registerSerno"));
		map01.put("id",map.get("id"));
		ProdRegistFilingInfo p1 = o1.convertValue(map01,ProdRegistFilingInfo.class);
		return p1;
	}
}
