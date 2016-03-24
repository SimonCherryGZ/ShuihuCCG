package com.simoncherry.shuihuccg.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.simoncherry.shuihuccg.R;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Looper;
import android.view.View;
import android.widget.Toast; 

public class GameGlobalTools extends Application{

	public SoundPool soundPool;
	public HashMap<Integer, Integer> soundPoolMap;
	public float playVolume;
	
	public final int SHOW_MAP = 0;
	public final int SHOW_STATUS = 1;
	public final int SHOW_NOTE = 2;
	public final int SHOW_COLLECTION = 3;
	public final int SHOW_SHOP = 4;
	public final int SHOW_LUCKYDRAW = 5;
	public final int SHOW_NEGOTIATE = 6;
	public final int SHOW_BATTLE = 7;
	public final int SHOW_AUCTION = 8;
	public final int SHOW_BOOK = 9;
	
	public final int SCENE_BEDROOM = 0;
	public final int SCENE_HOME_WEEKDAYS = 1;
	public final int SCENE_SCHOOLGATE = 2;
	public final int SCENE_CLASSROOM_MORNING = 3;
	public final int SCENE_CLASSROOM_DUSK = 4;
	public final int SCENE_SHOP_MORNING = 5;
	public final int SCENE_SHOP_DUSK = 6;
	public final int SCENE_COLLECTION = 7;
	public final int SCENE_TALK_CLASSROOM = 8;
	public final int SCENE_EXCHANGE_CLASSROOM = 9;
	public final int SCENE_AUCTION_CLASSROOM = 10;
	public final int SCENE_TALK_SHOP = 11;
	public final int SCENE_EXCHANGE_SHOP = 12;
	public final int SCENE_AUCTION_SHOP = 13;
	public final int SCENE_HOME_HOLIDAY = 14;
	public final int SCENE_CRAM_SCHOOL_MORNING = 15;
	public final int SCENE_CRAM_SCHOOL_DUSK = 16;
	public final int SCENE_TALK_CRAMSCHOOL = 17;
	public final int SCENE_EXCHANGE_CRAMSCHOOL = 18;
	public final int SCENE_ANOTHER_SHOP = 19;
	public final int SCENE_BEDROOM_SATURDAY = 20;
	public final int SCENE_HOME_SUNDAY = 21;
	public final int SCENE_BEDROOM_SUNDAY = 22;
	public final int SCENE_SHOP_SUNDAY = 23;
	public final int SCENE_EXCHANGE_SHOP_SUNDAY = 24;
	public final int SCENE_AUCTION_SHOP_SUNDAY = 25;
	final int SCENE_ALLEY = 26;
	
	public final int SE_BOOK_FILP = 1;
	public final int SE_CLICK_BAG = 2;
	public final int SE_EAT = 3;
	public final int SE_NEW_CARD = 4;
	public final int SE_SHOP_CHARGE = 5;
	public final int SE_WALK = 6;
	public final int SE_WAVE = 7;
	public final int SE_BATTLE_DRAW = 8;
	public final int SE_BATTLE_LOSE = 9;
	public final int SE_BATTLE_WIN = 10;
	public final int SE_SCHOOL_CLOCK = 11;
	public final int SE_SYSTEM_CLICK = 12;
	public final int SE_OPTION_CLICK = 13;
	
	// TODO
	List<Integer> compareValueList = new ArrayList<Integer>();
	HashMap<Integer, Integer> valueCharaMap = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> charaIndexMap = new HashMap<Integer, Integer>();
	
	// TODO
//	public int[] card_set_0 = {4,5,6,7,9,13,14,17,19,22,23,36};
//	public int[] card_set_1 = {59,80,94,95,103,107};
//	public int[] card_set_2 = {12,18,20,25,34,35};
//	public int[] card_set_3 = {3,11,21,24,32,33};
//	public int[] card_set_4 = {26,27,28,29,30,31};
//	public int[] card_set_5 = {61,67,72,73,99,106};
//	public int[] card_set_6 = {64,65,102,104,105,108};
//	public int[] card_set_7 = {48,71,97,98,100,101};
//	public int[] card_set_8 = {62,63,75,79,81,85};
//	public int[] card_set_9 = {40,41,86,90,91,93};
//	public int[] card_set_10 = {39,87,88,89,92,96};
//	public int[] card_set_11 = {47,50,58,60,74,76};
//	public int[] card_set_12 = {68,69,78};
//	public int[] card_set_13 = {1,2,8,10,15,16,37,38,42,43,44,45,46,49,51,52,53,54,55,56,57,66,70,77,82,83,84};
	
	public int[][] card_set = {
			{4,5,6,7,9,13,14,17,19,22,23,36},
			{59,80,94,95,103,107},
			{12,18,20,25,34,35},
			{3,11,21,24,32,33},
			{26,27,28,29,30,31},
			{61,67,72,73,99,106},
			{64,65,102,104,105,108},
			{48,71,97,98,100,101},
			{62,63,75,79,81,85},
			{40,41,86,90,91,93},
			{39,87,88,89,92,96},
			{47,50,58,60,74,76},
			{68,69,78},
			{1,2,8,10,15,16,37,38,42,43,44,45,46,49,51,52,53,54,55,56,57,66,70,77,82,83,84}
	};
	
	
	
	public void setCardSetIndex(int index){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		if(index > 13){
			index = 0;
		}
		
		editor.putInt("cardset", index);
		editor.commit();
	}
	
	public int getCardSetIndex(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("cardset", 13);
	}
	
	public void setScene(int scene){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt("scene", scene);
		editor.commit();
	}
	
	public int getScene(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("scene", 0);
	}
	
	public void setWeek(int week){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt("week", week);
		editor.commit();
	}
	
	public int getWeek(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("week", 1);
	}
	
	public void setItemKinds(int kinds){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt("item_kinds", kinds);
		editor.commit();
	}
	
	public int getItemKinds(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("item_kinds", 0);
	}
	
	public void setItemCount(int index, int item_count){
		if(item_count > 99) item_count = 99;
		
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		String temp = "item_" + String.valueOf(index) + "_count";
		editor.putInt(temp, item_count);
		editor.commit();
	}
	
	public int getItemCount(int index){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE); 
		String temp = "item_" + String.valueOf(index) + "_count";
		return mySharedPreferences.getInt(temp, 0);
	}
	
	public void setItemPosition(int index, int item_position){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		String temp = "item_" + String.valueOf(index) + "_position";
		editor.putInt(temp, item_position);
		editor.commit();
	}
	
	public int getItemPosition(int index){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE); 
		String temp = "item_" + String.valueOf(index) + "_position";
		return mySharedPreferences.getInt(temp, -1);
	}
	
	public void setBagCount(int bag_count){
		if(bag_count > 99) bag_count = 99;
		
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("bag_count", bag_count);
		editor.commit();
	}
	
	public int getBagCount(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("bag_count", 0);
	}
	
	public void setNoteCount(int note_count){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("note_count", note_count);
		editor.commit();
	}
	
	public int getNoteCount(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("note_count", 0);
	}
	
	public void setNoteText(String note_key, String note_text){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putString(note_key, note_text);
		editor.commit();
	}
	
	public String getNoteText(String note_key){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getString(note_key, "");
	}
	
	public void setPlayerBattleCardIndex(int index){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("player_battle_card", index);
		editor.commit();
	}
	
	public int getPlayerBattleCardIndex(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("player_battle_card", 0);
	}
	
	public void setNpcBattleCardIndex(int index){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("npc_battle_card", index);
		editor.commit();
	}
	
	public int getNpcBattleCardIndex(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("npc_battle_card", 0);
	}
	
	public void setBattleNpcCharaIndex(int index){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("battle_npc_chara", index);
		editor.commit();
	}
	
	public int getBattleNpcCharaIndex(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("battle_npc_chara", 1);
	}
	
	public void setAuctionHostIndex(int index){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("auction_host", index);
		editor.commit();
	}
	
	public int getAuctionHostIndex(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getInt("auction_host", 0);
	}
	
	public void setPlayerMoney(int money){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("chara0", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("money", money);
		editor.commit();
	}
	
	public int getPlayerMoney(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("chara0", MODE_PRIVATE);
		return mySharedPreferences.getInt("money", 0);
	}
	
	public void setPlayerIncome(int income){
		if(income < 1){
			income = 1;
		}
		
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("chara0", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("income", income);
		editor.commit();
	}
	
	public int getPlayerIncome(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("chara0", MODE_PRIVATE);
		return mySharedPreferences.getInt("income", 5);
	}
	
	public void setCharaStudy(int index, int study){
		String lib = "chara" + String.valueOf(index);
		SharedPreferences mySharedPreferences = 
				getSharedPreferences(lib, MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		
		if(study < 0) study = 0;
		if(study > 100) study = 100;
		
		editor.putInt("study", study);
		editor.commit();
	}
	
	public int getCharaStudy(int index){
		String lib = "chara" + String.valueOf(index);
		SharedPreferences mySharedPreferences = 
				getSharedPreferences(lib, MODE_PRIVATE);
		return mySharedPreferences.getInt("study", 0);
	}
	
	public void setExamEvent(boolean exam){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putBoolean("exam", exam);
		editor.commit();
	}
	
	public boolean getExamEvent(){
		SharedPreferences mySharedPreferences = 
				getSharedPreferences("global", MODE_PRIVATE);
		return mySharedPreferences.getBoolean("exam", false);
	}
	
	public void setCharaExamResult(int index, int exam){
		String lib = "chara" + String.valueOf(index);
		SharedPreferences mySharedPreferences = 
				getSharedPreferences(lib, MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.putInt("exam_result", exam);
		editor.commit();
	}
	
	public int getCharaExamResult(int index){
		String lib = "chara" + String.valueOf(index);
		SharedPreferences mySharedPreferences = 
				getSharedPreferences(lib, MODE_PRIVATE);
		return mySharedPreferences.getInt("exam_result", 55);
	}
	
	// TODO
	public void LoadSoundPool(){
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>(); 
		
		soundPoolMap.put(1, soundPool.load(getBaseContext(),
				R.raw.book_flip, 1));
		soundPoolMap.put(2, soundPool.load(getBaseContext(),
				R.raw.click_bag, 1));
		soundPoolMap.put(3, soundPool.load(getBaseContext(),
				R.raw.eat, 1));
		soundPoolMap.put(4, soundPool.load(getBaseContext(),
				R.raw.new_card, 1));
		soundPoolMap.put(5, soundPool.load(getBaseContext(),
				R.raw.shop_charge, 1));
		soundPoolMap.put(6, soundPool.load(getBaseContext(),
				R.raw.walk, 1));
		soundPoolMap.put(7, soundPool.load(getBaseContext(),
				R.raw.wave, 1));
		soundPoolMap.put(8, soundPool.load(getBaseContext(),
				R.raw.battle_draw, 1));
		soundPoolMap.put(9, soundPool.load(getBaseContext(),
				R.raw.battle_lose, 1));
		soundPoolMap.put(10, soundPool.load(getBaseContext(),
				R.raw.battle_win, 1));
		soundPoolMap.put(11, soundPool.load(getBaseContext(),
				R.raw.school_clock, 1));
		soundPoolMap.put(12, soundPool.load(getBaseContext(),
				R.raw.system_click, 1));
		soundPoolMap.put(13, soundPool.load(getBaseContext(),
				R.raw.option_click, 1));
		
		AudioManager am = (AudioManager) this.getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        float currentSound = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxSound = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        playVolume = currentSound / maxSound;
	}
	
	public void PlaySystemClickSE(int which){
		soundPool.play(soundPoolMap.get(which), 
				playVolume, playVolume, 1, 0, 1.0f);
	}
	
	public int calculateInSampleSize(BitmapFactory.Options options, 
	        int reqWidth, int reqHeight) { 
	    // 源图片的高度和宽度 
	    final int height = options.outHeight; 
	    final int width = options.outWidth; 
	    int inSampleSize = 1; 
	    if (height > reqHeight || width > reqWidth) { 
	        // 计算出实际宽高和目标宽高的比率 
	        final int heightRatio = Math.round((float) height / (float) reqHeight); 
	        final int widthRatio = Math.round((float) width / (float) reqWidth); 
	        // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高 
	        // 一定都会大于等于目标的宽和高。 
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio; 
	    } 
	    return inSampleSize; 
	}
	
	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, 
	        int reqWidth, int reqHeight) { 
	    // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小 
	    final BitmapFactory.Options options = new BitmapFactory.Options(); 
	    options.inJustDecodeBounds = true; 
	    BitmapFactory.decodeResource(res, resId, options); 
	    // 调用上面定义的方法计算inSampleSize值 
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); 
	    
	    // 2016.03.08
	    options.inPreferredConfig = Bitmap.Config.RGB_565;
	    
	    // 使用获取到的inSampleSize值再次解析图片 
	    options.inJustDecodeBounds = false; 
	    return BitmapFactory.decodeResource(res, resId, options); 
	}
	
	public int getCharaAvatar(int index){
		Resources resources = getResources();
		String img_name = "chara_" + String.valueOf(index);
		int img_id = resources.getIdentifier(img_name, "drawable", getPackageName());
		return img_id;
	}
	
	public String getCharaName(int index){
		Resources resources = getResources();
		
		String search_name = "chara_name_" + String.valueOf(index);
		int search_id = resources.getIdentifier(search_name, "string", getPackageName());
		search_name = String.valueOf(getText(search_id));
		
		return search_name;
	}
	
	public String getCardInformation(String type, int index){
		Resources resources = getResources();
		
		String search_name = type + String.valueOf(index);
		int search_id = resources.getIdentifier(search_name, "string", getPackageName());
		search_name = String.valueOf(getText(search_id));
		
		return search_name;
	}
	
	public int getCardImgId(int index){
		Resources resources = getResources();
		String img_filename = "front" + String.valueOf(index);
		int img_id = resources.getIdentifier(img_filename, "drawable", getPackageName());
		return img_id;
	}
	
	public int calcCardValue(int index){
		int value = 0;
		int target_count = 0;
		int total_count = 0;
		
		for(int i=0; i<=11; i++){
			target_count += getThisCardCount(i, index);
			total_count += getAllCardCount(i);
		}
		
		//value = total_count / target_count;
		value = (total_count / target_count) * 1000 + (109-index);
		
		return value;
	}
	
	public int getAllCardCount(int which){
		int total_count = 0;
		String lib_name = "chara" + String.valueOf(which);
		
		SharedPreferences mySharedPreferences = getSharedPreferences(
				lib_name, Activity.MODE_PRIVATE);
		
		for(int i=1; i<=108; i++){
			String search_string = "card_count" + String.valueOf(i);
			int card_count = mySharedPreferences.getInt(search_string, 0);
			total_count += card_count;
		}
		
		return total_count;
	}
	
	public int getThisCardCount(int which, int index){
		String lib_name = "";
		lib_name = "chara" + String.valueOf(which);
		SharedPreferences mySharedPreferences = getSharedPreferences(
				lib_name, Activity.MODE_PRIVATE);
		
		String search_string = "card_count" + String.valueOf(index);
		int card_count = mySharedPreferences.getInt(search_string, 0);
		return card_count;
	}
	
	public int getRareCardCount(int host){
		List<Integer> valueList = new ArrayList<Integer>();
		
		for(int index=1; index<=108; index++){
			
			if(getThisCardCount(host, index) > 1){
				boolean isRare = true;
				int exist = 0;
				
				for(int chara=0; chara<=7; chara++){
					if(host != chara){
						if(getThisCardCount(chara, index) != 0){
							//isRare = false;
							//break;
							exist++;
						}
					}
				}
				
				if(exist >= 7){
					isRare = false;
				}
				
				if(isRare == true){
					valueList.add(calcCardValue(index));
				}
			}
		}
		
		return valueList.size();
	}
	
	public void changeCardList(int chara, int index, int extra_count){
		String lib_name = null;
		String search_string = null;
		int card_count = 0;
		int total_count = 0;
		
		lib_name = "chara" + String.valueOf(chara);
		
		SharedPreferences mySharedPreferences = getBaseContext().getSharedPreferences(
				lib_name, Activity.MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		
		search_string = "card_count" + String.valueOf(index);
		card_count = mySharedPreferences.getInt(search_string, 0);
		
		total_count = card_count + extra_count;
		if(total_count < 0){
			Toast.makeText(getBaseContext(), "卡牌数量不足！", Toast.LENGTH_SHORT).show();
		}else{
			if(total_count > 99){
				total_count = 99;
				Toast.makeText(getBaseContext(), 
						"No." + String.valueOf(index) + " 卡牌数量已达到最大值！",
						Toast.LENGTH_SHORT).show();
			}
			
			editor.putInt(search_string, total_count);
			editor.commit();
		}
	}
	
	public int getCardKinds(int chara){
		String lib_name = null;
		String search_string = null;
		int kinds = 0;
		
		lib_name = "chara" + String.valueOf(chara);
		
		SharedPreferences mySharedPreferences = getBaseContext().getSharedPreferences(
				lib_name, Activity.MODE_PRIVATE);
		
		for(int i=1; i<=108; i++){
			search_string = "card_count" + String.valueOf(i);
			if(mySharedPreferences.getInt(search_string, 0) > 0){
				kinds++;
			}
		}
		
		return kinds;
	}
	
	public void npcGetWeeklyMoney(){
		String lib_name = null;
		
		for(int i=1; i<=7; i++){
			lib_name = "chara" + String.valueOf(i);
			SharedPreferences mySharedPreferences = getBaseContext().getSharedPreferences(
					lib_name, Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			
			int money = mySharedPreferences.getInt("money", 0);
			int income = mySharedPreferences.getInt("income", 5);
			editor.putInt("money", money+income);
			editor.commit();
		}
	}
	
	public void npcBuyCard(){
		
		if(getWeek() == 6){
			
			for(int i=8; i<=11; i++){
				String lib_name = "chara" + String.valueOf(i);
				SharedPreferences mySharedPreferences = getSharedPreferences(
						lib_name, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				String search_string = null;
				//editor.clear();
				
				int count = 3;
				search_string = "new_buy_count";
				editor.putInt(search_string, count);
				editor.commit();
				
				for(int j=0; j<count; j++){
					
					Random random_index = new Random(System.currentTimeMillis());
					//int index = random_index.nextInt(108) + 1;
					int set = 13 - getCardSetIndex();
					int len = card_set[set].length;
					int temp = random_index.nextInt(len);
					int index = card_set[set][temp];
					
					search_string = "new_buy_index" + String.valueOf(j);
					editor.putInt(search_string, index);
					editor.commit();
					
					search_string = "card_count" + String.valueOf(index);
					editor.putInt(search_string, 2);
					editor.commit();
				}

				editor.commit();
			}
			
		}else{
		
			for(int i=1; i<=7; i++){
				Random random_boolean = new Random();
				boolean buy_or_not = random_boolean.nextBoolean();
				
				String lib_name = "chara" + String.valueOf(i);
				SharedPreferences mySharedPreferences = getSharedPreferences(
						lib_name, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				String search_string = null;
				
				search_string = "new_buy_count";
				editor.putInt(search_string, 0);
				editor.commit();
				
				int money = mySharedPreferences.getInt("money", 0);
				
				//if(buy_or_not == true && money > 0){	
				if( money > 0){	
					Random random_count = new Random();
					int count = 0;
					if(money <= 3){
						//count = random_count.nextInt(money) + 1;
						count = money;
					}else{
						//count = random_count.nextInt(3) + 1;
						count = 3;
					}
					search_string = "new_buy_count";
					editor.putInt(search_string, count);
					editor.commit();
					
					for(int j=0; j<count; j++){
						
						Random random_index = new Random(System.currentTimeMillis());
						//int index = random_index.nextInt(108) + 1;
						int set = getCardSetIndex();
						int chances = random_index.nextInt(10) + 1;
						if(chances <= 3){
							set--;
							if(set < 0) set = 13;
						}
						
						int len = card_set[set].length;
						int temp = random_index.nextInt(len);
						int index = card_set[set][temp];
						
						search_string = "new_buy_index" + String.valueOf(j);
						editor.putInt(search_string, index);
						editor.commit();
						
						search_string = "card_count" + String.valueOf(index);
						int card_count = mySharedPreferences.getInt(search_string, 0);
						if(card_count <= 98){
							card_count++;
						}
						editor.putInt(search_string, card_count);
						editor.commit();
					}
					
					editor.putInt("money", money-count);
					editor.commit();
				}
			}
		}
	}
	
	public void clearCharaData(int chara){
		String lib_name = "chara" + String.valueOf(chara);
		SharedPreferences mySharedPreferences = 
				getBaseContext().getSharedPreferences(lib_name, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	public void clearGlobalData(){
		String lib_name = "global";
		SharedPreferences mySharedPreferences = 
				getBaseContext().getSharedPreferences(lib_name, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	public void unlockAllCard(int chara, int count){
		String lib_name = "chara" + String.valueOf(chara);
		SharedPreferences mySharedPreferences = 
				getBaseContext().getSharedPreferences(lib_name, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		for(int i=1; i<=108; i++){
			String search_string = "card_count" + String.valueOf(i);
			if(count > 99){
				count = 99;
			}
			editor.putInt(search_string, count);
		}
		
		editor.commit();
	}
	
	public void unlockHalfCard(int chara, int count){
		String lib_name = "chara" + String.valueOf(chara);
		SharedPreferences mySharedPreferences = 
				getBaseContext().getSharedPreferences(lib_name, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		editor.clear();
		
		for(int i=1; i<=108; i+=2){
			String search_string = "card_count" + String.valueOf(i);
			if(count > 99){
				count = 99;
			}
			editor.putInt(search_string, count);
		}
		
		editor.commit();
	}
	
	
	// TODO
	public void npcExchangeCard(){
		compareValueList.clear();
		valueCharaMap.clear();
		charaIndexMap.clear();
		
		for(int chara=1; chara<=7; chara++){
			int rareCount = getRareCardCount(chara);
			if(rareCount > 0){
				for(int round=0; round<rareCount; round++){
					int index = hostSelectCardToAuction(chara);
					if(index > 0){
						npcSelectCardToAuction(chara, index, 1, false);
						hostSelecCardToDeal(chara, index, false);
					}else{
						compareValueList.clear();
						valueCharaMap.clear();
						charaIndexMap.clear();
					}
				}
			}
		}
		Looper.prepare();
		Toast.makeText(getApplicationContext(), "npc exchange card ok!", Toast.LENGTH_LONG).show();
		Looper.loop();
	}
	
	public int hostSelectCardToAuction(int host){
		List<Integer> valueList = new ArrayList<Integer>();
		HashMap<Integer, Integer> valueMap = new HashMap<Integer, Integer>();
		
		for(int index=1; index<=108; index++){
			
			if(getThisCardCount(host, index) > 1){
				boolean isRare = true;
				int exist = 0;
				
				for(int chara=1; chara<=7; chara++){
					if(host != chara){
						if(getThisCardCount(chara, index) != 0){
							exist++;
						}
					}
				}
				
				if(exist >= 6){
					isRare = false;
				}
				
				if(isRare == true){
					valueList.add(calcCardValue(index));
					valueMap.put(calcCardValue(index), index);
				}
			}
		}
		
		if(valueList.size() >= 1){

			Collections.sort(valueList);
			
			if(valueMap.get( valueList.get( valueList.size()-1) ) != null){
				
				int max_value_index = valueMap.get(
						valueList.get( valueList.size()-1) );
				
				return max_value_index;
			}
		}

		return 0;
	}
	
	public void npcSelectCardToAuction(int host, int index, int round, boolean isRefreshList){
		int targetValue = calcCardValue(index);
		
		for(int i=1; i<=7; i++){
			if(host != i && getThisCardCount(i, index) == 0){
				List<Integer> valueList = new ArrayList<Integer>();
				HashMap<Integer, Integer> valueMap = new HashMap<Integer, Integer>();
				
				for(int j=1; j<=108; j++){
					if(getThisCardCount(host, j) == 0 && getThisCardCount(i, j) > 1){
						int cardValue = calcCardValue(j);
						if(targetValue >= cardValue){
							valueList.add(cardValue);
							valueMap.put(cardValue, j);
						}
					}
				}

				if(valueList.size() > 0 && valueList.size() >= round){
					Collections.sort(valueList);
					int min_value_index = valueMap.get(valueList.get(valueList.size()-round));
					int value = calcCardValue(min_value_index);
					compareValueList.add(value);
					valueCharaMap.put(value, i);
					charaIndexMap.put(i, min_value_index);
				}else{
					compareValueList.add(0);
				}
			}else{
				compareValueList.add(0);
			}
		}
	}
	
	private boolean hostSelecCardToDeal(int host_chara, int host_card, boolean isShowToPlayer){
		boolean deal_success = false;
		
		Collections.sort(compareValueList);
		if(valueCharaMap.get(compareValueList.get(compareValueList.size()-1)) != null){
			int deal_chara = valueCharaMap.get(compareValueList.get(compareValueList.size()-1));
			int deal_index = charaIndexMap.get(deal_chara);

			changeCardList(host_chara, host_card, -1);
			changeCardList(host_chara, deal_index, 1);
			changeCardList(deal_chara, host_card, 1);
			changeCardList(deal_chara, deal_index, -1);
			
			deal_success = true;
		}else{
			deal_success = false;
		}
		
		compareValueList.clear();
		valueCharaMap.clear();
		charaIndexMap.clear();
		
		return deal_success;
	}
	
	public int getOneExistCard(int chara){
		String lib_name = "chara" + String.valueOf(chara);
		SharedPreferences mySharedPreferences = 
				getBaseContext().getSharedPreferences(lib_name, Activity.MODE_PRIVATE);
		
		List<Integer> existList = new ArrayList<Integer>();
		
		for(int i=1; i<=108; i++){
			if(getThisCardCount(chara, i) > 0){
				existList.add(i);
			}
		}
		
		Random random_int = new Random();
		int index = random_int.nextInt(existList.size());
		
		return existList.get(index);
	}
	
}