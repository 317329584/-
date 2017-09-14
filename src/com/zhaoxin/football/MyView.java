package com.zhaoxin.football;

import org.xutils.common.util.LogUtil;

import com.zhaoxin.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyView extends LinearLayout {

	private int xPosition = 0;//
	private int yPosition = 0;//
	private String name;// ��Ա����
	private OnRemoveListener listener;
	private int parentWidth;
	private int parentHeight;
	private int ballSize = 25;//����ͼƬ��С  dp

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public MyView(Context context, String name,int parentWidth,int parentHeight) {
		super(context);
		this.name = name;
		this.parentWidth = parentWidth;
		this.parentHeight = parentHeight;
		init();
	}

	private void init() {
		android.widget.RelativeLayout.LayoutParams viewLP = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		setLayoutParams(viewLP);
		setOrientation(1);
		setGravity(Gravity.CENTER);
		xPosition = parentWidth /2 - dip2px(15);
		yPosition = parentHeight - dip2px(60); 
		
		ImageView iv = new ImageView(getContext());
		android.widget.LinearLayout.LayoutParams ivLP = new android.widget.LinearLayout.LayoutParams(
				dip2px(ballSize), dip2px(ballSize));
		iv.setLayoutParams(ivLP);
		iv.setBackgroundResource(R.drawable.football);
		addView(iv);
		
		TextView tv = new TextView(getContext());
		android.widget.LinearLayout.LayoutParams tvLP = new android.widget.LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv.setText(name);
		tv.setTextColor(Color.WHITE);
		tv.setLayoutParams(tvLP);
		tv.setId(11111);
		addView(tv);
	}

	
	public void setOnRemoveListener(OnRemoveListener lis) {
		this.listener = lis;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_UP:
			int parentTop = ((ViewGroup) getParent()).getTop();
			int parentLeft = ((ViewGroup) getParent()).getLeft();
			float rawX = event.getRawX();
			float rawY = event.getRawY();
			xPosition = (int) (rawX - parentLeft - getWidth() / 2);
			yPosition = (int) (rawY - parentTop - getHeight());
			int right = xPosition + getWidth();
			int bottom = yPosition + getHeight();
			ViewGroup vg = (ViewGroup) getParent();
			int xMargain = getWidth() / 5 * 3;
			int yMargain = getHeight() / 5 * 3;
			if (yPosition < -xMargain || xPosition < -yMargain
					|| right > ((ViewGroup) getParent()).getWidth() + xMargain
					|| bottom > ((ViewGroup) getParent()).getHeight() + yMargain) {
				vg.removeView(this);
				if(listener != null){
					listener.onRemove(name);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			move(event.getRawX(), event.getRawY());
			// moveByParams((int) event.getRawX(), (int) event.getRawY());
			break;
		}
		return true;
	}

	private void move(float rawX, float rawY) {
		int parentTop = ((ViewGroup) getParent()).getTop();
		int parentLeft = ((ViewGroup) getParent()).getLeft();
		xPosition = (int) (rawX - parentLeft - getWidth() / 2);
		yPosition = (int) (rawY - parentTop - getHeight());
		int right = xPosition + getWidth();
		int bottom = yPosition + getHeight();
		layout(xPosition, yPosition, right, bottom);
	}

	public void setPosition(float x, float y) {
		this.xPosition = (int) x;
		this.yPosition = (int) y;
		layout(xPosition, yPosition, xPosition + getWidth(), yPosition
				+ getHeight());
	}
	
	public void setPosition(double x, double y) {
		LogUtil.e("===="+x +"  "+ y);
		this.xPosition = (int) x;
		this.yPosition = (int) y;
		layout(xPosition, yPosition, xPosition + getWidth(), yPosition
				+ getHeight());
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (l == 0 && t == 0 && (xPosition > 0 || yPosition > 0)) {
			layout(xPosition, yPosition, xPosition + getWidth(), yPosition
					+ getHeight());
		}
	}

	public int dip2px(int dip) {
		float d = getResources().getDisplayMetrics().density;
		return (int) (dip * d + 0.5);
	}

	public interface OnRemoveListener {
		public void onRemove(String name);
	}

}
