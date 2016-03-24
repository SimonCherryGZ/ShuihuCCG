package com.simoncherry.shuihuccg.adapter;

import java.util.List;

import com.simoncherry.shuihuccg.bean.AuctionListBean;
import com.simoncherry.shuihuccg.fragment.AuctionFragment;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AuctionListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<AuctionListBean> list;
	private Context ctx;
	private AuctionFragment fragment_context;
	private GameGlobalTools global_data;
	private Animation hint_anim;
	
	public AuctionListAdapter(Context context, AuctionFragment fragment_context, List<AuctionListBean> list){
		this.ctx = context;
		this.fragment_context = fragment_context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		
		initAnim();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.auction_card_item, null);
			global_data = (GameGlobalTools)ctx.getApplicationContext();
			
			holder = new ViewHolder();
			holder.auction_chara_name = (TextView) convertView.findViewById(R.id.auction_chara_name);
			holder.auction_card_img = (ImageView) convertView.findViewById(R.id.auction_card_img);
			holder.auction_card_name = (TextView) convertView.findViewById(R.id.auction_card_name);
			holder.auction_value = (TextView) convertView.findViewById(R.id.auction_value);
			holder.auction_btn_deal = (Button) convertView.findViewById(R.id.auction_btn_deal);
			holder.auction_item_bg = (View) convertView.findViewById(R.id.auction_item_bg);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		AuctionListBean auction_list = list.get(position);
		
		holder.auction_chara_name.setText(auction_list.getAuctionChara());
		holder.auction_card_img.setImageBitmap(
				global_data.decodeSampledBitmapFromResource(
						ctx.getResources(), auction_list.getAuctionCardImgId(), 40, 40));
		
		holder.auction_card_name.setText(auction_list.getAuctionCardName());
		holder.auction_value.setText(String.valueOf(auction_list.getAuctionValue()));
		holder.auction_btn_deal.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(ctx, "Deal!", Toast.LENGTH_SHORT).show();
				fragment_context.playerSelectDeal(position);
			}
		});
		
		if(auction_list.getItemHint() == true){
			holder.auction_item_bg.setVisibility(View.VISIBLE);
			holder.auction_item_bg.startAnimation(hint_anim);
		}else{
			holder.auction_item_bg.clearAnimation();
			holder.auction_item_bg.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		private ImageView auction_card_img;
		private TextView auction_chara_name;
		private TextView auction_card_name;
		private TextView auction_value;
		private Button auction_btn_deal;
		private View auction_item_bg;
	}
	
	private void initAnim(){
		hint_anim = new AlphaAnimation(0.0f, 1.0f);
		hint_anim.setRepeatMode(Animation.REVERSE);
		//hint_anim.setRepeatMode(4);
		hint_anim.setRepeatCount(Animation.INFINITE);
		hint_anim.setDuration(1);
		hint_anim.setStartOffset(500);
	}
	
}