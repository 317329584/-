package com.zhaoxin.util;

import org.xutils.x;

import android.app.Application;

import com.zhaoxin.database.App_DBManager;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);
		App_DBManager.getInstance().initDB();
	}
}
