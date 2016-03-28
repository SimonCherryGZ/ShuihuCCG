package com.simoncherry.shuihuccg.adapter;

import java.util.List;

import com.simoncherry.shuihuccg.R;
import com.simoncherry.shuihuccg.bean.CollectionListBean;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectionListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<CollectionListBean> list;
	private Context ctx;
	private GameGlobalTools globalTools;
	
	public CollectionListAdapter(Context context, List<CollectionListBean> list){
		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		globalTools = (GameGlobalTools)ctx.getApplicationContext();
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
			convertView = inflater.inflate(R.layout.collection_item, null);
			
			holder = new ViewHolder();
			holder.img_card = (ImageView)convertView.findViewById(R.id.card_img);
			holder.tv_card_numero = (TextView)convertView.findViewById(R.id.numero);
			holder.tv_card_count = (TextView)convertView.findViewById(R.id.card_count);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		CollectionListBean collectionlist = list.get(position);
		int img_id;
		int card_numero = collectionlist.getCardNumero();
		int card_count = collectionlist.getCardCount();
		
		if(card_count == 0){
			img_id = ctx.getResources().getIdentifier("front0", "drawable", ctx.getPackageName());
		}else{
			img_id = collectionlist.getCardImgId();
		}
		holder.img_card.setImageBitmap(globalTools.decodeSampledBitmapFromResource(ctx.getResources(), img_id, 80, 120));
		holder.tv_card_numero.setText("No." + String.valueOf(card_numero));
		holder.tv_card_count.setText(String.valueOf(card_count));

		return convertView;
	}
	
	private static class ViewHolder {
		private ImageView img_card;
		private TextView tv_card_numero;
		private TextView tv_card_count;
	}
	
}