package com.kayak.xsql.parameter;

import com.kayak.core.dao.DaoService;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.Tools;

public class FieldGetterArray extends FieldGetter {
	private int index;

	public FieldGetterArray(int index) {
		this.index = index;
	}

	@Override
	public Object get(Object params) {
		if(params==null)
			return null;
		Object[] array = (Object[]) params;
		
		if(array.length<index)
			return null;
		return array[index];
	}

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量
	 * 
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public String newId(String tablename) throws Exception {
		return newId(tablename, null, 1);
	}

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量
	 * 
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public String newId(String tablename, Integer padLeft) throws Exception {
		return newId(tablename, padLeft, 1);
	}

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量
	 * 
	 * @param tablename
	 * @param padLeft   Integer 如果padLeft>0，则为返回的ID值左补0（padLeft指定位数）
	 * @return
	 * @throws Exception
	 */
	public String newId(String tablename, Integer padLeft, int step) throws Exception {
		long maxid = newIdLong(tablename, step);
		String ret;
		if (padLeft != null && padLeft > 0)// 左补0
		{
			ret = Tools.padLeft(String.valueOf(maxid), padLeft, '0');
		} else {
			ret = String.valueOf(maxid);
		}
		return ret;
	}

	/**
	 * 根据表名称自动生成一个新ID<br />
	 * 如果tablename传""或null则系统生成一个系统自增量
	 * 
	 * @param tablename
	 * @param padLeft   Integer 如果padLeft>0，则为返回的ID值左补0（padLeft指定位数）
	 * @return
	 * @throws Exception
	 */
	public Long newIdLong(final String tablename, final int step) throws Exception {

		DaoService daoService = SysBeans.getBean("daoService");

		// 选择主数据源
		daoService.selectDataSource(0);

		daoService.doTrans(() -> {
			String tname = tablename;
			if (Tools.strIsEmpty(tname)) {// 空表名称取系统自增量
				tname = "BIZR2SYSREQUENCETABLE";
			} else {
				tname = tname.trim().toLowerCase();
			}

			// 先查询出sqlid对应的查询配置信息
			String sql = " UPDATE sys_sequence SET maxid=maxid+" + step + " WHERE tablename=$S{tablename} ";

			int num = daoService.update(sql, tname).getEffect();
			long maxid;
			if (num == 0) {// 没有table的sequence记录，则添加
				/* 取出表中的最大id */
				maxid = step;
				sql = " INSERT INTO sys_sequence (tablename,maxid) VALUES ($S{tablename},$N{nextID}) ";
				daoService.update(sql, tname, maxid);

			} else {
				sql = " SELECT maxid FROM sys_sequence WHERE tablename=$S{tablename} ";
				maxid = daoService.query(Long.class, sql, tname);
			}

		});

		return null;
	}
}
