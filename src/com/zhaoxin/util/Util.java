package com.zhaoxin.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class Util {
	/**
	 * 将view转换成bitmap
	 * @param v
	 * @return
	 */
	public static Bitmap loadBitmapFromView(View v) {
		int w = v.getWidth();
		int h = v.getHeight();
		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		c.drawColor(Color.WHITE);
		/** 如果不设置canvas画布为白色，则生成透明 */
		// v.layout(0, 0, w, h);
		v.draw(c);
		return bmp;
	}
}
