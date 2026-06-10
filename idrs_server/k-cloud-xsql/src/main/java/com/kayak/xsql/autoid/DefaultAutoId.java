package com.kayak.xsql.autoid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kayak.core.dao.DaoService;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.Tools;

public class DefaultAutoId implements AutoId {

	private static final Pattern patternAutoidParamName = Pattern.compile("([\\w]+)[,]([\\d]+)[,]?([\\w.]*)");

	private ThreadLocal<String> localAutoId = new ThreadLocal<>();

	public String getAutoId(String table, String pname) throws Exception {
		String autoid = localAutoId.get();
		localAutoId.remove();
		return autoid;
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
	public String newId(String tablename, Integer padLeft) throws Exception {
		long maxid = newIdLong(tablename);

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
	public synchronized long  newIdLong(final String table) throws Exception {

		DaoService daoService = SysBeans.getBean("daoService");

		// TODO 获取新连接 雪花算法 支持多种自定义实现配置
		long[] maxid = new long[1];

		if (daoService.isTrans(0)) {
			// 先查询出sqlid对应的查询配置信息
			String sql = " UPDATE sys_sequence SET maxid=maxid+1 WHERE tablename=$S{tablename} ";
			int num = daoService.update(sql, table).getEffect();

			if (num == 0) {// 没有table的sequence记录，则添加
				/* 取出表中的最大id */
				maxid[0] = 1;
				sql = " INSERT INTO sys_sequence (tablename,maxid) VALUES ($S{tablename},$L{nextID}) ";
				daoService.update(sql, new Object[] { table, maxid[0] });
			} else {
				sql = " SELECT maxid FROM sys_sequence WHERE tablename=$S{tablename} ";
				maxid[0] = daoService.query(Long.class, sql, table);
			}
		} else {
			try (AutoCloseable ds = daoService.selectDataSource(0)) {
				daoService.getConnection();
				daoService.begin();
				// 先查询出sqlid对应的查询配置信息
				String sql = " UPDATE sys_sequence SET maxid=maxid+1 WHERE tablename=$S{tablename} ";
				int num = daoService.update(sql, table).getEffect();

				if (num == 0) {// 没有table的sequence记录，则添加
					/* 取出表中的最大id */
					maxid[0] = 1;
					sql = " INSERT INTO sys_sequence (tablename,maxid) VALUES ($S{tablename},$L{nextID}) ";
					daoService.update(sql, new Object[] { table, maxid[0] });
				} else {
					sql = " SELECT maxid FROM sys_sequence WHERE tablename=$S{tablename} ";
					maxid[0] = daoService.query(Long.class, sql, table);
				}
				daoService.commit();
			} catch (Throwable e) {
				daoService.end();
			}
		}

		return maxid[0];

	}

	@Override
	public void prepare(String table, String pname) throws Exception {
		if (Tools.strIsEmpty(table))// 空表名称取系统自增量
			table = "BIZR2SYSREQUENCETABLE";
		else
			table = table.trim().toLowerCase();

		Integer padLeft = null;
		String prefix = null;
		if (!Tools.strIsEmpty(pname)) {
			Matcher mth = patternAutoidParamName.matcher(pname);
			if (mth.find())// autoid指定了左补0位数
			{
				pname = mth.group(1);// 截取参数名称
				padLeft = Tools.str2Int(mth.group(2));// 截取左补0位数
				prefix = mth.group(3);// ID前缀

				if (prefix != null && padLeft != null) {
					padLeft -= prefix.length();
				}
			}
		}

		String newid = newId(table, padLeft);
		if (prefix != null) {// 为ID添加前缀
			newid = prefix + newid;
		}

		localAutoId.set(newid);

	}

}
