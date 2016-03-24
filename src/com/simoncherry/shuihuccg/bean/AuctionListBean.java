package com.simoncherry.shuihuccg.bean;


public class AuctionListBean{
	private int auction_card_img_id;
	private String auction_chara_name;
	private String auction_card_name;
	private int auction_value;
	private boolean isHint = false;
	private int auction_chara_index;
	private int auction_card_index;
	
	public void setAuctionCardImgId(int id){
		this.auction_card_img_id = id;
	}
	
	public int getAuctionCardImgId(){
		return this.auction_card_img_id;
	}
	
	public void setAuctionChara(String name){
		this.auction_chara_name = name;
	}
	
	public String getAuctionChara(){
		return this.auction_chara_name;
	}
	
	public void setAuctionCardName(String card){
		this.auction_card_name = card;
	}
	
	public String getAuctionCardName(){
		return this.auction_card_name;
	}
	
	public void setAuctionValue(int value){
		this.auction_value = value;
	}
	
	public int getAuctionValue(){
		return this.auction_value;
	}
	
	public void setItemHint(boolean isHint){
		this.isHint = isHint;
	}
	
	public boolean getItemHint(){
		return this.isHint;
	}
	
	public void setAuctionCharaIndex(int index){
		this.auction_chara_index = index;
	}
	
	public int getAuctionCharaIndex(){
		return this.auction_chara_index;
	}
	
	public void setAuctionCardIndex(int index){
		this.auction_card_index = index;
	}
	
	public int getAuctionCardIndex(){
		return this.auction_card_index;
	}
}