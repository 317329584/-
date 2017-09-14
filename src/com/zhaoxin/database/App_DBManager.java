package com.zhaoxin.database;

import org.xutils.DbManager;

/**
 * Created by 鍗㈣瀺闇� on 2017/8/29.
 */

public class App_DBManager {

	public static App_DBManager app_dbManager;
	private final String DB_NAME = "Football_DB";
	private DbManager.DaoConfig daoConfig;

	public static App_DBManager getInstance() {
		if (app_dbManager == null) {
			app_dbManager = new App_DBManager();
		}
		return app_dbManager;
	}

	public void initDB() {
		daoConfig = new DbManager.DaoConfig().setDbName(DB_NAME)
				.setDbVersion(100)
				.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
					@Override
					public void onUpgrade(DbManager dbManager, int i, int i1) {
					}
				});
	}

	public DbManager.DaoConfig getDaoConfig() {
		return daoConfig;
	}
}
