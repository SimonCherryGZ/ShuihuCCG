package com.simoncherry.shuihuccg.bean;


public class SelectCardListBean{
	private int card_index;
	private int card_count;
	private boolean isHint;
	
	public void setCardIndex(int index){
		this.card_index = index;
	}
	
	public int getCardIndex(){
		return this.card_index;
	}
	
	public void setCardCount(int count){
		this.card_count = count;
	}
	
	public int getCardCount(){
		return this.card_count;
	}
	
	public void setItemHint(boolean isHint){
		this.isHint = isHint;
	}
	
	public boolean getItemHint(){
		return this.isHint;
	}
}