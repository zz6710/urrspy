package com.kayak.base.dao.util;

import com.kayak.core.dao.DaoService;
import com.kayak.core.dao.Trans;
import com.kayak.core.system.SysBeans;

public class DaoUtil {

	private static DaoService daoService;

	/**
	 * 事务操作
	 * 
	 * @param trans
	 * @throws Exception
	 */
	public static void doTrans(Trans trans) throws Exception {
		if (daoService == null) {
			synchronized (DaoUtil.class) {
				if (daoService == null) {
					daoService = SysBeans.getBean("daoService");
				}
			}
		}
		daoService.doTrans(trans);
	}

	/**
	 * 事务操作
	 * 
	 * @param trans
	 * @throws Exception
	 */
	public static void doTrans(Trans trans, int sharding) throws Exception {
		if (daoService == null) {
			synchronized (DaoUtil.class) {
				if (daoService == null) {
					daoService = SysBeans.getBean("daoService");
				}
			}
		}
		daoService.doTrans(trans, sharding);
	}

	public static void doTrans(Trans trans, String dbName) throws Exception {
		if (daoService == null) {
			synchronized (DaoUtil.class) {
				if (daoService == null) {
					daoService = SysBeans.getBean("daoService");
				}
			}
		}
		daoService.doTrans(trans, daoService.getSharding(dbName));
	}
}
