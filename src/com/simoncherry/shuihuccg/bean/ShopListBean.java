package com.simoncherry.shuihuccg.bean;



public class ShopListBean {
	private int item_img_id;
	private String item_name;
	private String item_effect;
	private int item_cost;


	public void setItemImg(int item_img_id){
		this.item_img_id = item_img_id;
	}

	public int getItemImg(){
		return item_img_id;
	}
	
	public void setItemName(String item_name){
		this.item_name = item_name;
	}
	
	public String getItemName(){
		return item_name;
	}
	
	public void setItemEffect(String item_effect){
		this.item_effect = item_effect;
	}
	
	public String getItemEffect(){
		return item_effect;
	}
	
	public void setItemCost(int item_count){
		this.item_cost = item_count;
	}
	
	public int getItemCost(){
		return item_cost;
	}

}