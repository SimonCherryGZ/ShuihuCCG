package com.simoncherry.shuihuccg.ui;

import java.util.ArrayList;
import java.util.List;

import com.simoncherry.shuihuccg.adapter.SelectCardListAdapter;
import com.simoncherry.shuihuccg.bean.SelectCardListBean;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectCardDialog extends Dialog {
	
	public SelectCardDialog(Context context) {  
        super(context);  
    }
	
	public SelectCardDialog(Context context, int theme) {  
        super(context, theme);  
    }
	
	public static class Builder {
		private Context context;  
		
        private String positiveButtonText;  
        private String negativeButtonText;   
        private View contentView;  
        private GameGlobalTools globalTools;
        public int select_card_index = 0;
        public int which_card_list = 0;
        public int which_enemy_card_list = 0;
        
        private ImageView card_img;
        private TextView card_index;
        private TextView card_star;
        private TextView card_epithet;
        private TextView card_name;
        private TextView card_value;
        private ListView select_card_list;
        
        private List<SelectCardListBean> list;
        private SelectCardListAdapter adapter;
        
        private DialogInterface.OnClickListener positiveButtonClickListener;  
        private DialogInterface.OnClickListener negativeButtonClickListener;
        
        public Builder(Context context) {  
            this.context = context;  
        }
        
//        public Builder setItemName(String item_name){
//        	this.item_name = item_name;
//        	return this;
//        }
//        
//        public Builder setItemName(int item_name) {  
//            this.item_name = (String) context.getText(item_name);  
//            return this;  
//        }
//        
//        public Builder setItemCount(int item_count){
//        	this.item_count = item_count;
//        	return this;
//        }
//        
//        public Builder setItemPrice(int item_price){
//        	this.item_price = item_price;
//        	return this;
//        }
        
//        public Builder setSelectCardIndex(int index){
//        	this.select_card_index = index;
//        	return this;
//        }
        
        public Builder setWhichCardList(int which){
        	this.which_card_list = which;
        	return this;
        }
        
        public Builder setEnemyCardList(int which){
        	this.which_enemy_card_list = which;
        	return this;
        }
        
        public Builder setContentView(View v) {  
            this.contentView = v;  
            return this;  
        }
        
        public Builder setPositiveButton(int positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = (String) context  
                    .getText(positiveButtonText);  
            this.positiveButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setPositiveButton(String positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = positiveButtonText;  
            this.positiveButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setNegativeButton(int negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = (String) context  
                    .getText(negativeButtonText);  
            this.negativeButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setNegativeButton(String negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = negativeButtonText;  
            this.negativeButtonClickListener = listener;  
            return this;  
        }
        
        public SelectCardDialog create() {
        	LayoutInflater inflater = (LayoutInflater) context  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	final SelectCardDialog dialog = new SelectCardDialog(context, R.style.selectCardDialog);
        	View layout = inflater.inflate(R.layout.dialog_select_card, null);
        	globalTools = (GameGlobalTools)context.getApplicationContext();
        	
        	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        	dialog.setCanceledOnTouchOutside(false);
        	
        	dialog.addContentView(layout, new LayoutParams(  
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        	
        	card_img = (ImageView) layout.findViewById(R.id.card_big);
        	card_index = (TextView) layout.findViewById(R.id.index);
        	card_star = (TextView) layout.findViewById(R.id.star);
        	card_epithet = (TextView) layout.findViewById(R.id.epithet);
        	card_name = (TextView) layout.findViewById(R.id.name);
        	card_value = (TextView) layout.findViewById(R.id.value);
        	select_card_list = (ListView) layout.findViewById(R.id.select_card_list);
        	
        	if (positiveButtonText != null) {  
                ((Button) layout.findViewById(R.id.btn_positive))  
                        .setText(positiveButtonText);  
                if (positiveButtonClickListener != null) {  
                    ((Button) layout.findViewById(R.id.btn_positive))  
                            .setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                    positiveButtonClickListener.onClick(dialog,  
                                            DialogInterface.BUTTON_POSITIVE);  
                                }  
                            });  
                }  
            } else {   
                layout.findViewById(R.id.btn_positive).setVisibility(  
                        View.GONE);  
            }  
 
            if (negativeButtonText != null) {  
                ((Button) layout.findViewById(R.id.btn_negative))  
                        .setText(negativeButtonText);  
                if (negativeButtonClickListener != null) {  
                    ((Button) layout.findViewById(R.id.btn_negative))  
                            .setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                    negativeButtonClickListener.onClick(dialog,  
                                            DialogInterface.BUTTON_NEGATIVE);  
                                }  
                            });  
                }  
            } else {   
                layout.findViewById(R.id.btn_negative).setVisibility(  
                        View.GONE);  
            }
            
            select_card_list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					int index = list.get(position).getCardIndex();
					//TODO
					select_card_index = index;
					
					setCardDetail(index);
				} 	
            });
            
            initCardList(which_card_list);
        	
            dialog.setContentView(layout);
			return dialog;
        }
        
        private void initCardList(int which){
        	list = new ArrayList<SelectCardListBean>();
        	SharedPreferences mySharedPreferences = null;
        	
//        	if(which == 0){
//	        	mySharedPreferences = context.getSharedPreferences(
//	        			"shuihu", Activity.MODE_PRIVATE);
//        	}else{
//        		String lib_name = "enemy_cardlist_" + String.valueOf(which);
//        		mySharedPreferences = context.getSharedPreferences(
//        				lib_name, Activity.MODE_PRIVATE);
//        	}
        	String lib_name = "chara" + String.valueOf(which);
    		mySharedPreferences = context.getSharedPreferences(
    				lib_name, Activity.MODE_PRIVATE);
        	
        	int card_count = 0;
        	String search_string = null;
        	
        	for(int i=1; i<=108; i++){
        		search_string = "card_count" + String.valueOf(i);
        		card_count = mySharedPreferences.getInt(search_string, 0);
        		
        		if(card_count > 0){
	        		SelectCardListBean listbean = new SelectCardListBean();
	        		listbean.setCardIndex(i);
	        		listbean.setCardCount(card_count);
	        		
	        		//if(which_enemy_card_list != 0)
	        		{
	        			if(globalTools.getThisCardCount(which_enemy_card_list, i) == 0){
	        				listbean.setItemHint(true);
	        			}
	        		}
	        		
	        		list.add(listbean);
        		}
        	}
        	
        	if(list.size() > 0){
    			setAdapter(list);
    		}
        }
        
        private void setAdapter(List<SelectCardListBean> list){
        	adapter = new SelectCardListAdapter(context, list);
        	select_card_list.setAdapter(adapter);
        }
        
        private void setCardDetail(int index){
        	Resources resources = context.getResources();
    		String img_filename = "front" + String.valueOf(index);
    		int img_id = resources.getIdentifier(img_filename, "drawable", context.getPackageName());
	
    		//card_img.setImageResource(img_id);
    		card_img.setImageBitmap(
    				globalTools.decodeSampledBitmapFromResource(
    						context.getResources(), img_id, 120, 170));
    		
    		card_index.setText(context.getText(R.string.hero_index) + String.valueOf(index));
    		card_star.setText(context.getText(R.string.hero_star) + globalTools.getCardInformation("star", index));
    		card_epithet.setText(context.getText(R.string.hero_epithet) + globalTools.getCardInformation("epithet", index));
    		card_name.setText(context.getText(R.string.hero_name) + globalTools.getCardInformation("name", index));
    		card_value.setText("稀有度：" + String.valueOf(globalTools.calcCardValue(index)/1000));
        }
	}
}
