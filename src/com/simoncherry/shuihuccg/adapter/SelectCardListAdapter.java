package com.simoncherry.shuihuccg.adapter;

import java.util.List;

import com.simoncherry.shuihuccg.bean.SelectCardListBean;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectCardListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<SelectCardListBean> list;
	private Context ctx;
	private GameGlobalTools global_data;
	
	public SelectCardListAdapter(Context context, List<SelectCardListBean> list){
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
			convertView = inflater.inflate(R.layout.select_card_item, null);
			global_data = (GameGlobalTools)ctx.getApplicationContext();
			
			holder = new ViewHolder();
			holder.layout_card_item = (RelativeLayout) convertView.findViewById(R.id.layout_card_item);
			holder.card_img = (ImageView) convertView.findViewById(R.id.card_img);
			holder.card_index = (TextView) convertView.findViewById(R.id.card_index);
			holder.card_epithet = (TextView) convertView.findViewById(R.id.card_epithet);
			holder.card_name = (TextView) convertView.findViewById(R.id.card_name);
			holder.card_count = (TextView) convertView.findViewById(R.id.card_count);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		SelectCardListBean cardlist = list.get(position);
		int index = cardlist.getCardIndex();
		int count = cardlist.getCardCount();

		Resources resources = ctx.getResources();
		String img_filename = "front" + String.valueOf(index);
		int img_id = resources.getIdentifier(img_filename, "drawable", ctx.getPackageName());
		
		String hero_name = "name" + String.valueOf(index);
		String hero_epithet = "epithet" + String.valueOf(index);
		int name_id = resources.getIdentifier(hero_name, "string", ctx.getPackageName());
		int epithet_id = resources.getIdentifier(hero_epithet, "string", ctx.getPackageName());
		hero_name = String.valueOf(ctx.getText(name_id));
		hero_epithet = String.valueOf(ctx.getText(epithet_id));
		
		holder.card_img.setImageBitmap(
				global_data.decodeSampledBitmapFromResource(
						ctx.getResources(), img_id, 40, 40));
		holder.card_index.setText("No." + String.valueOf(index));
		holder.card_epithet.setText(hero_epithet);
		holder.card_name.setText(hero_name);
		holder.card_count.setText("x " + String.valueOf(count));
		
		if(cardlist.getItemHint() == true){
			//holder.layout_card_item.setBackgroundColor(0x8056c5ff);
			holder.layout_card_item.setBackgroundResource(R.color.item_hint_color);
		}else{
			//holder.layout_card_item.setBackgroundColor(0xffE8E8E7);
			holder.layout_card_item.setBackgroundResource(0);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		private RelativeLayout layout_card_item;
		private ImageView card_img;
		private TextView card_index;
		private TextView card_epithet;
		private TextView card_name;
		private TextView card_count;
	}
	
}