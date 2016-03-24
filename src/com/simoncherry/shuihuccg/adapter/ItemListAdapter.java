package com.simoncherry.shuihuccg.adapter;

import java.util.List;

import com.simoncherry.shuihuccg.bean.ItemListBean;
import com.simoncherry.shuihuccg.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<ItemListBean> list;
	private Context ctx;
	
	public ItemListAdapter(Context context, List<ItemListBean> list){
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
			convertView = inflater.inflate(R.layout.status_item, null);
			holder = new ViewHolder();
			holder.item_img = (ImageView)convertView.findViewById(R.id.item_img);
			holder.item_name = (TextView)convertView.findViewById(R.id.item_name);
			holder.item_effect = (TextView)convertView.findViewById(R.id.item_effect);
			holder.item_count = (TextView)convertView.findViewById(R.id.item_count);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ItemListBean itemlist = list.get(position);
		int item_img_id = itemlist.getItemImg();
		String item_name = itemlist.getItemName();
		String item_effect = itemlist.getItemEffect();
		int item_count = itemlist.getItemCount();
		
		holder.item_img.setImageResource(item_img_id);
		holder.item_name.setText(item_name);
		holder.item_effect.setText(item_effect);
		holder.item_count.setText("x " + String.valueOf(item_count));
		
		return convertView;
	}
	
	private static class ViewHolder {
		private ImageView item_img;
		private TextView item_name;
		private TextView item_effect;
		private TextView item_count;
	}
	
}