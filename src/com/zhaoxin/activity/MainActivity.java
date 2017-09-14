package com.zhaoxin.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoxin.adapter.HistoryAdapter;
import com.zhaoxin.database.App_DBManager;
import com.zhaoxin.football.FootballBean;
import com.zhaoxin.football.MyView;
import com.zhaoxin.football.MyView.OnRemoveListener;
import com.zhaoxin.football.SaveTable;

public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private TextView save;
	private RelativeLayout football_court;
	private LinearLayout list, historyLinear;
	private DbManager dbManager;
	private ListView listHistory;

	private int personNum = 13;
	private List<String> members;
	List<SaveTable> savas;
	private float size = 0;// 足球的半径
	private int width;// 足球场的宽度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		dbManager = x.getDb(App_DBManager.getInstance().getDaoConfig());
		football_court = (RelativeLayout) findViewById(R.id.football_courts);
		save = (TextView) findViewById(R.id.save);
		list = (LinearLayout) findViewById(R.id.list);
		historyLinear = (LinearLayout) findViewById(R.id.historyLinear);
		listHistory = (ListView) findViewById(R.id.list_history);
		save.setOnClickListener(this);
		findViewById(R.id.history).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
		findViewById(R.id.clear).setOnClickListener(this);

		LayoutParams lp = football_court.getLayoutParams();
		int w = getWindow().getWindowManager().getDefaultDisplay().getWidth() * 100 / 100;
		lp.width = w;
		lp.height = lp.width * 105 / 68;
		initList();
	}

	private void initList() {
		members = new ArrayList<String>();
		for (int i = 0; i < personNum; i++) {
			members.add(i + 1 + "号");
		}
		for (int i = 0; i < members.size(); i++) {
			addMember(i);
		}
	}

	/**
	 * 在球场上添加球员
	 * 
	 * @param i
	 */
	private void addMember(final int i) {
		final TextView member = new TextView(this);
		member.setText(members.get(i));
		member.setBackgroundResource(R.drawable.item_bg);
		member.setPadding(15, 15, 15, 25);
		android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(7, 0, 10, 0);
		member.setLayoutParams(lp);
		list.addView(member);
		member.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MyView view = new MyView(MainActivity.this, members.get(i),
						football_court.getWidth(), football_court.getHeight());
				view.setOnRemoveListener(new OnRemoveListener() {

					@Override
					public void onRemove(String name) {
						addMember(i);
					}
				});
				football_court.addView(view);
				list.removeView(member);
			}
		});
		if(size == 0){
			member.
		}
	}

	/**
	 * 保存战术到数据库
	 */

	private void saveLocationToDB(String nameS) {
		try {
			width = football_court.getWidth();
			size = football_court.getChildAt(0).getWidth() / 2;
			SaveTable saveBean = new SaveTable();
			saveBean.name = nameS;
			JSONArray arr = new JSONArray();
			for (int i = 0; i < football_court.getChildCount(); i++) {
				View view = football_court.getChildAt(i);
				FootballBean bean = new FootballBean();
				bean.x = (view.getLeft() + size) / width;
				bean.y = (view.getTop() + size) / width;
				bean.name = ((TextView) view.findViewById(11111)).getText()
						.toString();
				JSONObject obj = new JSONObject();
				obj.put("name", bean.name);
				obj.put("x", bean.x);
				obj.put("y", bean.y);
				arr.put(obj);
			}
			saveBean.json = arr.toString();
			dbManager.save(saveBean);
		} catch (DbException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取保存的战术列表
	 */
	private void getSaveFromDB() {
		try {
			savas = dbManager.findAll(SaveTable.class);
			if (savas == null || savas.size() == 0) {
				Toast.makeText(getApplicationContext(), "暂无历史",
						Toast.LENGTH_LONG).show();
				return;
			}
			HistoryAdapter adapter = new HistoryAdapter(savas,
					getApplicationContext());
			listHistory.setAdapter(adapter);
			listHistory.setOnItemClickListener(this);
			historyLinear.setVisibility(View.VISIBLE);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示保存的战术
	 * 
	 * @param saveTable
	 */
	public void showSave(SaveTable saveTable) {
		try {
			width = football_court.getWidth();
			football_court.removeAllViews();
			JSONArray arr = new JSONArray(saveTable.json);
			for (int i = 0; i < members.size(); i++) {
				final int k = i;
				for (int j = 0; j < arr.length(); j++) {
					String name = arr.getJSONObject(j).getString("name");
					if (members.get(i).equals(name)) {
						MyView view = new MyView(MainActivity.this, arr
								.getJSONObject(j).getString("name"),
								football_court.getWidth(),
								football_court.getHeight());
						view.setOnRemoveListener(new OnRemoveListener() {

							@Override
							public void onRemove(String name) {
								addMember(k);
							}
						});
						football_court.addView(view);
						football_court.postInvalidate();
						view.setPosition(arr.getJSONObject(j).getDouble("x")
								* width - size,
								arr.getJSONObject(j).getDouble("y") * width
										- size);
						break;
					}
				}
			}
			// 重新初始化底部球员列表
			list.removeAllViews();
			for (int i = 0; i < members.size(); i++) {
				addMember(i);
			}
			// 去掉列表中已经添加到球场的球员
			for (int j = 0; j < arr.length(); j++) {
				String name = arr.getJSONObject(j).getString("name");
				for (int i = 0; i < list.getChildCount(); i++) {
					TextView tv = (TextView) list.getChildAt(i);
					if (tv.getText().toString().equals(name)) {
						list.removeView(tv);
						break;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save:
			if (football_court.getChildCount() == 0) {
				return;
			}

			showAlert();
			break;
		case R.id.history:
			getSaveFromDB();
			break;
		case R.id.cancel:
			historyLinear.setVisibility(View.GONE);
			break;
		case R.id.clear:
			clear();
			break;
		}
	}

	/**
	 * 清除本地保存战术数据
	 */
	AlertDialog dialogRemind;//

	private void clear() {
		// TODO Auto-generated method stub
		if (dialogRemind == null)
			dialogRemind = new AlertDialog.Builder(this).create();
		dialogRemind.show();
		dialogRemind.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		View v = getLayoutInflater().inflate(R.layout.remind_clear, null);
		dialogRemind.setContentView(v);
		TextView cancel = (TextView) v.findViewById(R.id.cancel);
		TextView goon = (TextView) v.findViewById(R.id.goon);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogRemind.dismiss();
			}
		});
		goon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					dbManager.delete(SaveTable.class);
					dialogRemind.dismiss();
					Toast.makeText(getApplicationContext(), "删除成功",
							Toast.LENGTH_LONG).show();
					historyLinear.setVisibility(View.GONE);
				} catch (DbException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "清理失败",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	AlertDialog dialogSave;//
	EditText savename;

	/**
	 * 弹出保存提示框
	 */
	private void showAlert() {
		// TODO Auto-generated method stub
		if (dialogSave == null)
			dialogSave = new AlertDialog.Builder(this).create();
		dialogSave.show();
		dialogSave.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		View v = getLayoutInflater().inflate(R.layout.remind, null);
		dialogSave.setContentView(v);
		TextView cancel = (TextView) v.findViewById(R.id.wifi_cancel);
		TextView goon = (TextView) v.findViewById(R.id.wifi_goon);
		savename = (EditText) v.findViewById(R.id.savename);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogSave.dismiss();
			}
		});
		goon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveLocationToDB(savename.getText().toString());
				dialogSave.dismiss();
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showSave(savas.get(position));
		historyLinear.setVisibility(View.GONE);
	}
}
