package com.simoncherry.shuihuccg.adapter;

import java.util.List;

import com.simoncherry.shuihuccg.bean.ShopListBean;
import com.simoncherry.shuihuccg.fragment.ShopFragment;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.ui.ItemShowDialog;
import com.simoncherry.shuihuccg.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShopListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<ShopListBean> list;
	private Context ctx;
	private ShopFragment fragment_ctx;
	private GameGlobalTools globalTools;
	
	public ShopListAdapter(Context context, ShopFragment fragment_context, List<ShopListBean> list){
	//public ShopListAdapter(Context context, List<ShopListBean> list){
		this.ctx = context;
		this.fragment_ctx = fragment_context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.shop_item, null);
			
			holder = new ViewHolder();
			holder.item_img = (ImageView)convertView.findViewById(R.id.item_img);
			holder.item_name = (TextView)convertView.findViewById(R.id.item_name);
			holder.item_effect = (TextView)convertView.findViewById(R.id.item_effect);
			holder.item_cost = (TextView)convertView.findViewById(R.id.item_cost);
			holder.btn_buy = (Button)convertView.findViewById(R.id.btn_buy);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ShopListBean itemlist = list.get(position);
		final int item_img_id = itemlist.getItemImg();
		final String item_name = itemlist.getItemName();
		final String item_effect = itemlist.getItemEffect();
		final int item_cost = itemlist.getItemCost();
		
		holder.item_img.setImageResource(item_img_id);
		holder.item_name.setText(item_name);
		holder.item_effect.setText(item_effect);
		holder.item_cost.setText("￥" + String.valueOf(item_cost));
		holder.btn_buy.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);

				final ItemShowDialog.Builder builder = new ItemShowDialog.Builder(ctx);
				builder.setItemName(item_name);
				builder.setItemPrice(item_cost);
				builder.setPositiveButton(R.string.btn_general_positive, new DialogInterface.OnClickListener() {  
		            public void onClick(DialogInterface dialog, int which) {  
 	
		            	int my_money = globalTools.getPlayerMoney();
		            	int total_price = builder.getTotalPrice();
		            	if(total_price > my_money){
		            		fragment_ctx.setShopkeeperDialog(ctx.getString(R.string.shop_no_enough_money));
		            		
		            	} else{
		            		globalTools.setPlayerMoney(my_money - total_price);
		            		fragment_ctx.refreshShopMoney();
		            		fragment_ctx.setShopkeeperDialog(ctx.getString(R.string.shop_thanks));
		            		
		            		globalTools.PlaySystemClickSE(globalTools.SE_SHOP_CHARGE);
		            		
		            		int count = builder.getItemCount();
		            		
		            		if(position == 0){
		            			int exist_bag_count = globalTools.getBagCount();
		            			globalTools.setBagCount(exist_bag_count + count);
		            		}else{
		            			//fragment_ctx.addItemToStatus(position, item_img_id, item_name, item_effect, count);
		            			
		            			int exist_item_kinds = globalTools.getItemKinds();
		            			int exist_item_count = globalTools.getItemCount(position);
		            			if(exist_item_count > 0){
		            				globalTools.setItemCount(position, exist_item_count + count);
		            			}else{
		            				globalTools.setItemPosition(position, exist_item_kinds);
		            				exist_item_kinds++;
		            				globalTools.setItemKinds(exist_item_kinds);
		            				globalTools.setItemCount(position, exist_item_count + count);
		            			}
		            			
		            		}
		            	}
		            	
		                dialog.dismiss();  
		            }  
		        });  
		  
		        builder.setNegativeButton(R.string.btn_general_negative, new android.content.DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int which) {
                    	fragment_ctx.setShopkeeperDialog(ctx.getString(R.string.shop_welcome));
                        dialog.dismiss();  
                    }  
                });
		        
		        builder.setAddButton(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(ctx, "add_btn", Toast.LENGTH_LONG).show();
					}
				});
		        
		        builder.setSubButton(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(ctx, "sub_btn", Toast.LENGTH_LONG).show();
					}
				});
		        
		        builder.create().show(); 
				//Toast.makeText(ctx, "click button "+String.valueOf(position), Toast.LENGTH_LONG).show();
			}
		});
		
		return convertView;
	}
	
	private static class ViewHolder {
		private ImageView item_img;
		private TextView item_name;
		private TextView item_effect;
		private TextView item_cost;
		private Button btn_buy;
	}
	
}