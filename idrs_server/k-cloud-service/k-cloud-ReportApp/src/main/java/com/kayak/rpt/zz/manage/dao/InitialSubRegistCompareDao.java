package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.rpt.zz.manage.model.InitialSubRegistInfo;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InitialSubRegistCompareDao extends ComnDao {


	/**
     * 赋予初始化的标识
	 * @param p1
     * @param list0
     * @return
     */
	public InitialSubRegistInfo compareFlag(InitialSubRegistInfo p1, List<InitialSubRegistInfo> list0){
		InitialSubRegistInfo p3 = new InitialSubRegistInfo();
		String p1Version = p1.getSysDataVersion();
		if(StringUtils.isNotBlank(p1Version)){
			if(p1Version.equals("1.0")){
				p3 = initInfo(p1);
			}else{
				String p2Version = ProdRegistFilingCompareDao.getNewVersion(p1Version);
				for(int j = 0;j<list0.size();j++){
					InitialSubRegistInfo p2 = list0.get(j);
					if(p1.getProdCode().equals(p2.getProdCode())){
						if(p2Version.equals(p2.getSysDataVersion())){
							p3 = compare(p1,p2);
						}
					}
				}
			}
		}
		if(StringUtils.isBlank(p3.getProdCode())){
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
	public InitialSubRegistInfo compare(InitialSubRegistInfo p0,InitialSubRegistInfo p1){
		InitialSubRegistInfo p2 = new InitialSubRegistInfo();
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
			m2.put("prodCode",p0.getProdCode());
			m2.put("sysDataVersion",p0.getSysDataVersion());
			m2.put("registerSerno",p0.getRegisterSerno());
			p2 = o1.convertValue(m2,InitialSubRegistInfo.class);//反序列化
		}
		return p2;
	}
	/**
	 * 获得新的版本号
	 * @param version
	 * @return
	 */
	/*public static String getNewVersion(String version){
		Double v1 = Double.valueOf(version);
		Double v2 = v1 - 0.1;
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(v2);
	}*/

	/**
	 * 转换为新的数据对象--1.0版本直接初始化全部未0
	 * @param p0 需要初始化的数据
	 * @return
	 */
	public InitialSubRegistInfo initInfo(InitialSubRegistInfo p0){

		ObjectMapper o1 = new ObjectMapper();
		Map<String,Object> map = o1.convertValue(p0,Map.class);
		Map<String,Object> map01 = new HashMap<>();
		for(String key : map.keySet()){
			map01.put(key,"0");
		}
		map01.put("prodCode",map.get("prodCode"));
		map01.put("sysDataVersion",map.get("sysDataVersion"));
		map01.put("registerSerno",map.get("registerSerno"));
		map01.put("id",map.get("id"));
		InitialSubRegistInfo p1 = o1.convertValue(map01,InitialSubRegistInfo.class);
		return p1;
	}
}
