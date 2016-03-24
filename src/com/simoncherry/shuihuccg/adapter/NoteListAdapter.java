package com.simoncherry.shuihuccg.adapter;

import java.util.List;

import com.simoncherry.shuihuccg.bean.NoteListBean;
import com.simoncherry.shuihuccg.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<NoteListBean> list;
	private Context ctx;
	
	public NoteListAdapter(Context context, List<NoteListBean> list){
		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.note_item, null);
			holder = new ViewHolder();
			holder.information_text = (TextView) convertView.findViewById(R.id.information_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		NoteListBean informationlist = list.get(position);
		String text = informationlist.getText();
		holder.information_text.setText(text);
		
		return convertView;
	}
	
	private static class ViewHolder {
		private TextView information_text;
	}
	
}