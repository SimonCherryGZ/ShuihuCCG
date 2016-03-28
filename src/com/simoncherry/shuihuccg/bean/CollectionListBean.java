package com.simoncherry.shuihuccg.bean;

public class CollectionListBean {
	private int card_imgID;
	private int card_numero;
	private int card_count;
	
	public void setCardImgId(int id){
		this.card_imgID = id;
	}
	
	public int getCardImgId(){
		return this.card_imgID;
	}
	
	public void setCardNumero(int numero){
		this.card_numero = numero;
	}
	
	public int getCardNumero(){
		return this.card_numero;
	}
	
	public void setCardCount(int count){
		this.card_count = count;
	}
	
	public int getCardCount(){
		return this.card_count;
	}
}