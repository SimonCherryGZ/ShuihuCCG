package com.simoncherry.shuihuccg.ui;

import com.simoncherry.shuihuccg.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItemShowDialog extends Dialog {
	
	public ItemShowDialog(Context context) {  
        super(context);  
    }
	
	public ItemShowDialog(Context context, int theme) {  
        super(context, theme);  
    }
	
	public static class Builder {
		private Context context;  
        private String item_name;  
        private int item_count = 1;  
        private int item_price = 1;
        private int my_money = 0;
        private String positiveButtonText;  
        private String negativeButtonText;  
        private View contentView;  
        private TextView calc_unit_price;
        private TextView calc_item_count;
        private TextView calc_total_price;
        private DialogInterface.OnClickListener positiveButtonClickListener;  
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private DialogInterface.OnClickListener addButtonClickListener;
        private DialogInterface.OnClickListener subButtonClickListener;
        
        public Builder(Context context) {  
            this.context = context;  
        }
        
        public Builder setItemName(String item_name){
        	this.item_name = item_name;
        	return this;
        }
        
        public Builder setItemName(int item_name) {  
            this.item_name = (String) context.getText(item_name);  
            return this;  
        }
        
        public Builder setItemCount(int item_count){
        	this.item_count = item_count;
        	return this;
        }
        
        public Builder setItemPrice(int item_price){
        	this.item_price = item_price;
        	return this;
        }
        
        public Builder setContentView(View v) {  
            this.contentView = v;  
            return this;  
        }
        
        public Builder setAddButton(DialogInterface.OnClickListener listener){
        	this.addButtonClickListener = listener;
        	return this;
        }
        
        public Builder setSubButton(DialogInterface.OnClickListener listener){
        	this.subButtonClickListener = listener;
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
        
        public Builder setMyMoney(int my_money){
        	this.my_money = my_money;
        	return this;
        }
        
        public int getTotalPrice(){
        	return (item_price * item_count);
        }
        
        public int getItemCount(){
        	return item_count;
        }
        
        public ItemShowDialog create() {
        	LayoutInflater inflater = (LayoutInflater) context  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	final ItemShowDialog dialog = new ItemShowDialog(context);
        	View layout = inflater.inflate(R.layout.dialog_item_show, null);
        	
        	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        	dialog.setCanceledOnTouchOutside(false);
        	
        	dialog.addContentView(layout, new LayoutParams(  
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        	
        	((TextView) layout.findViewById(R.id.item_name)).setText(item_name);
        	calc_unit_price = (TextView) layout.findViewById(R.id.calc_unit_price);
        	calc_unit_price.setText(String.valueOf(item_price) + "¥");
        	calc_item_count = (TextView) layout.findViewById(R.id.calc_count);
        	calc_item_count.setText(String.valueOf(item_count));
        	calc_total_price = (TextView) layout.findViewById(R.id.calc_total);
        	calc_total_price.setText(String.valueOf(item_price * item_count) + "¥");
        	
        	if (addButtonClickListener != null) {
        		((Button) layout.findViewById(R.id.btn_add))
        			.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(item_count <= 98){
								item_count++;
							}
							calc_item_count.setText(String.valueOf(item_count));
							calc_total_price.setText(String.valueOf(item_price * item_count) + "¥");
						}
					});
        	}
        	
        	if (subButtonClickListener != null) {
        		((Button) layout.findViewById(R.id.btn_sub))
        			.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(item_count >= 2){
								item_count--;
							}
							calc_item_count.setText(String.valueOf(item_count));
							calc_total_price.setText(String.valueOf(item_price * item_count) + "¥");
						}
					});
        	}
        	
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
        	
            dialog.setContentView(layout);
			return dialog;
        }
	}
}
