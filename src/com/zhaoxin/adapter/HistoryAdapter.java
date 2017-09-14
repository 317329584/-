package com.zhaoxin.adapter;

import java.util.List;

import com.zhaoxin.activity.R;
import com.zhaoxin.football.SaveTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {

	private List<SaveTable> datas;
	private Context con;

	public HistoryAdapter(List<SaveTable> datas, Context con) {
		this.datas = datas;
		this.con = con;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int p, View v, ViewGroup parent) {
		Holder hold;

		if (v == null) {
			hold = new Holder();
			v = LayoutInflater.from(con).inflate(R.layout.item_history, null);
			hold.name = (TextView) v.findViewById(R.id.name);
			v.setTag(hold);
		} else {
			hold = (Holder) v.getTag();
		}

		hold.name.setText(datas.get(p).name);

		return v;
	}

	class Holder {
		TextView name;
	}

}
