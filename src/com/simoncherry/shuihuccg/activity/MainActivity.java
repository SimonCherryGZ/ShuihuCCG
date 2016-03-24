package com.simoncherry.shuihuccg.activity;

import java.io.IOException;
import java.util.Random;

import com.simoncherry.shuihuccg.fragment.AuctionFragment;
import com.simoncherry.shuihuccg.fragment.BattleFragment;
import com.simoncherry.shuihuccg.fragment.CollectionFragment;
import com.simoncherry.shuihuccg.fragment.LuckyDrawFragment;
import com.simoncherry.shuihuccg.fragment.MapFragment;
import com.simoncherry.shuihuccg.fragment.NegotiateFragment;
import com.simoncherry.shuihuccg.fragment.NoteFragment;
import com.simoncherry.shuihuccg.fragment.ShopFragment;
import com.simoncherry.shuihuccg.fragment.StatusFragment;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
	MapFragment.MapFragmentListener, ShopFragment.ShopFragmentListener,
	LuckyDrawFragment.LuckyDrawFragmentListener,
	NegotiateFragment.NegotiateFragmentListener,
	BattleFragment.BattleFragmentListener,
	AuctionFragment.AuctionFragmentListener{
	
	private ViewGroup layout_main_menu;
	private ViewGroup layout_loading;
	private ViewGroup layout_tab;
	private ImageView img_loading;
	private ImageView img_note_num;
	private TextView tv_loading;
	private TextView tv_note_num;
	private Button btn_start;
	private Button btn_collection;
	private ImageButton tab_1;
	private ImageButton tab_2;
	private ImageButton tab_3;
	private ImageButton tab_4;
	

	private Button btn_unlock;
	private Button btn_money;
	private Button btn_clear;
	private ImageView img_menu_bag;
	private ImageView img_menu_card_l;
	private ImageView img_menu_card_r;
	private TextView tv_menu_title;
	
	private Animation tab_btn_anim;
	private Animation bag_anim;
	private Animation card_l_rotate_anim;
	private Animation card_r_rotate_anim;
	private Animation card_l_scale_anim;
	private Animation card_r_scale_anim;
	private AnimationSet card_l_anim_set;
	private AnimationSet card_r_anim_set;
	
	private GameGlobalTools globalTools;
	private boolean isMainMenu = true;
	private String fragTag = "";
	private int game_scene;
	
	MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		globalTools = (GameGlobalTools) getApplication();
		
		layout_main_menu = (ViewGroup) findViewById(R.id.layout_main_menu);
		layout_loading = (ViewGroup) findViewById(R.id.layout_loading);
		layout_tab = (ViewGroup) findViewById(R.id.layout_tab);
		img_loading = (ImageView) findViewById(R.id.img_loading);
		img_note_num = (ImageView) findViewById(R.id.img_note_num);
		tv_loading = (TextView) findViewById(R.id.tv_loading);
		tv_note_num = (TextView) findViewById(R.id.tv_note_num);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_collection = (Button) findViewById(R.id.btn_collection);
		tab_1 = (ImageButton)findViewById(R.id.tab_1);
		tab_2 = (ImageButton)findViewById(R.id.tab_2);
		tab_3 = (ImageButton)findViewById(R.id.tab_3);
		tab_4 = (ImageButton)findViewById(R.id.tab_4);
		
		img_menu_bag = (ImageView) findViewById(R.id.img_menu_bag);
		img_menu_card_l = (ImageView) findViewById(R.id.img_menu_card_l);
		img_menu_card_r = (ImageView) findViewById(R.id.img_menu_card_r);
		tv_menu_title = (TextView) findViewById(R.id.tv_menu_title);
		
		btn_start.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				game_scene = globalTools.getScene();
				fragTag = showFragment(fragTag, globalTools.SHOW_MAP, game_scene);
			}
		});
		
		btn_collection.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				fragTag = showFragment(fragTag, globalTools.SHOW_BOOK, globalTools.SCENE_COLLECTION);
			}
		});
		
		btn_unlock = (Button) findViewById(R.id.btn_unlock);
		btn_unlock.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//globalTools.unlockAllCard(0, 99);
				globalTools.unlockHalfCard(0, 2);
				Toast.makeText(getApplicationContext(), "Unlock All Card!", Toast.LENGTH_SHORT).show();
			}
		});
		
		btn_money = (Button) findViewById(R.id.btn_money);
		btn_money.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.setPlayerMoney(50);
				globalTools.npcGetWeeklyMoney();
				Toast.makeText(getApplicationContext(), "50￥!", Toast.LENGTH_SHORT).show();
			}
		});
		
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				for(int i=0; i<=11; i++){
					globalTools.clearCharaData(i);
				}
				globalTools.clearGlobalData();
				globalTools.setScene(0);
				globalTools.setWeek(7);
				globalTools.setCharaStudy(0, 55);
				
				Toast.makeText(getApplicationContext(), "clear player data!", Toast.LENGTH_SHORT).show();
			}
		});
		
		tab_1.setOnClickListener(new TabOnClickListener(0));
		tab_2.setOnClickListener(new TabOnClickListener(1));
		tab_3.setOnClickListener(new TabOnClickListener(2));
		tab_4.setOnClickListener(new TabOnClickListener(3));
		
		initAnim();
		initImg();
		initMedia();
	}
	
	@Override  
    protected void onResume() {  
        super.onResume(); 
        
        showNoteNum(0, false);
        globalTools.LoadSoundPool();
        tab_1.startAnimation(tab_btn_anim);
        
        if(mediaPlayer != null)  
            mediaPlayer.start();  
        
        if(isMainMenu == true){
        	img_menu_bag.startAnimation(bag_anim);
        }
    }
	
	@Override  
    protected void onPause() {  
        super.onPause(); 
        
        if(mediaPlayer != null){  
            mediaPlayer.pause();  
            if(isFinishing()){  
                mediaPlayer.stop();  
                mediaPlayer.release();  
            }  
        }
        
        if(isMainMenu == true){
        	img_menu_card_l.clearAnimation();
        	img_menu_card_l.setVisibility(View.INVISIBLE);
        	img_menu_card_r.clearAnimation();
        	img_menu_card_r.setVisibility(View.INVISIBLE);
        	btn_start.setVisibility(View.INVISIBLE);
			btn_collection.setVisibility(View.INVISIBLE);
			tv_menu_title.setVisibility(View.INVISIBLE);
        }
    }
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
        	if (isMainMenu == true){
	            AlertDialog isExit = new AlertDialog.Builder(this).create();  
	            isExit.setTitle(getString(R.string.sys_general_title));  
	            isExit.setMessage(getString(R.string.sys_exit_game_warning));
	            isExit.setButton(DialogInterface.BUTTON_POSITIVE,
	            		getString(R.string.btn_general_positive), listener);
	            isExit.setButton(DialogInterface.BUTTON_NEGATIVE,
	            		getString(R.string.btn_general_negative), listener);
	            isExit.show();  
        	} else {
        		AlertDialog isExit = new AlertDialog.Builder(this).create();  
	            isExit.setTitle(getString(R.string.sys_general_title));  
	            isExit.setMessage(getString(R.string.sys_back_menu_warning));
	            isExit.setButton(DialogInterface.BUTTON_POSITIVE,
	            		getString(R.string.btn_general_positive), listener);
	            isExit.setButton(DialogInterface.BUTTON_NEGATIVE,
	            		getString(R.string.btn_general_negative), listener);
	            isExit.show();
        	}
        }  
        return false;    
    }
	
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
    {  
        public void onClick(DialogInterface dialog, int which)  
        {  
            switch (which)  
            {  
	            case AlertDialog.BUTTON_POSITIVE :
	            	if (isMainMenu == true){
	            		MainActivity.this.finish();  
	            	} else {
	            		layout_tab.setVisibility(View.INVISIBLE);
	            		clearTabAnim();
						tab_1.setImageResource(R.drawable.icon_scene_pressed);
						tab_1.startAnimation(tab_btn_anim);
						tab_2.setImageResource(R.drawable.icon_status_default);
						tab_3.setImageResource(R.drawable.icon_note_default);
						tab_4.setImageResource(R.drawable.icon_collection_default);
						
	            		FragmentManager fragmentManager = getSupportFragmentManager();
	    		        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    		        fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragTag));
	    		        fragmentTransaction.commit();
	    		        setMainMenuVisibility(true);
	    		        fragTag = "";
	    		        dialog.dismiss();
	            	}
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE :
	            	dialog.dismiss();
	                break;  
	            default:  
	                break;  
            }  
        }  
    };
    
    private void initMedia(){
		setVolumeControlStream(AudioManager.STREAM_MUSIC);  
        mediaPlayer = new MediaPlayer();
        try{  
            AssetManager assetManager = getAssets();  
            AssetFileDescriptor descriptor = assetManager.openFd("bgm.mp3");  
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(),  
                    descriptor.getStartOffset(), descriptor.getLength());  
            mediaPlayer.prepare();  
            mediaPlayer.setLooping(true);  
        }catch(IOException e){  
        	Toast.makeText(getApplicationContext(), "initMedia error", Toast.LENGTH_LONG).show();
            mediaPlayer = null;  
        }
	}
    
    public void setLoadingImg(int which, boolean visibility){
    	if(visibility == true){
    		switch(which){
	    		case 0 :
	    		case 20 :
	    		case 22 :
					tv_loading.setVisibility(View.VISIBLE);
	    			img_loading.setImageResource(R.drawable.map_bedroom);
	    			break;
	    		case 1 :
	    		case 14 :
	    		case 21 :
					tv_loading.setVisibility(View.VISIBLE);
	    			img_loading.setImageResource(R.drawable.map_home);
	    			break;
	    		case 2 :
					tv_loading.setVisibility(View.VISIBLE);
	    			img_loading.setImageResource(R.drawable.map_schoolgate);
	    			break;
    			case 3 :
    			case 8 :
    			case 9 :
    			case 10 :
    				tv_loading.setVisibility(View.VISIBLE);
	    			img_loading.setImageResource(R.drawable.map_classroom_morning);
	    			break;
    			case 4 :
    				tv_loading.setVisibility(View.VISIBLE);
    				img_loading.setImageResource(R.drawable.map_classroom_dusk);
    				break;
    			case 5 :
    			case 23 :
    			case 24 :
    			case 25 :
    				tv_loading.setVisibility(View.VISIBLE);
    				img_loading.setImageResource(R.drawable.map_shop_morning);
    				break;
    			case 6 :
    			case 11 :
    			case 12 :
    			case 13 :
    				tv_loading.setVisibility(View.VISIBLE);
    				img_loading.setImageResource(R.drawable.map_shop_dusk);
    				break;
    			case 7 :
    				tv_loading.setVisibility(View.INVISIBLE);
    				img_loading.setImageResource(R.drawable.loading_book);
    				break;
    			case 15 :
    			case 17 :
    			case 18 :
    				tv_loading.setVisibility(View.VISIBLE);
    				img_loading.setImageResource(R.drawable.map_cram_school_morning);
    				break;
    			case 16 :
    				tv_loading.setVisibility(View.VISIBLE);
    				img_loading.setImageResource(R.drawable.map_cram_school_dusk);
    				break;	
    			case 19 :
    				tv_loading.setVisibility(View.VISIBLE);
    				img_loading.setImageResource(R.drawable.map_another_shop);
    				break;
    			case 26 :
	    			tv_loading.setVisibility(View.VISIBLE);
					img_loading.setImageResource(R.drawable.map_alley);
					break;
    			default :
    				tv_loading.setVisibility(View.VISIBLE);
    				img_loading.setImageResource(R.drawable.map_classroom_morning);
    				break;
    		}
    		layout_loading.setVisibility(View.VISIBLE);
    	}else{
    		layout_loading.setVisibility(View.INVISIBLE);
    	}
    }
    
    public void setMainMenuVisibility(boolean visibility){
    	if(visibility == true){
    		layout_main_menu.setVisibility(View.VISIBLE);
    		btn_start.setClickable(true);
    		isMainMenu = true;
    	}else{
    		layout_main_menu.setVisibility(View.INVISIBLE);
    		btn_start.setClickable(false);
    		isMainMenu = false;
    	}
    }
    
    public String showFragment(final String frag_last, final int frag_index, int scene){
    	String frag_now = "";
    	boolean isTabShow = false;
		
		switch(frag_index){
			case 0 :
				frag_now = "map";
				isTabShow = true;
				break;
			case 1 :
				frag_now = "status";
				isTabShow = true;
				break;
			case 2 :
				frag_now = "note";
				isTabShow = true;
				break;
			case 3 :
				frag_now = "collection";
				isTabShow = true;
				break;
			case 4 :
				frag_now = "shop";
				isTabShow = false;
				break;
			case 5 :
				frag_now = "luckydraw";
				isTabShow = false;
				break;
			case 6 :
				frag_now = "negotiate";
				isTabShow = false;
				break;
			case 7 :
				frag_now = "battle";
				isTabShow = false;
				break;
			case 8 :
				frag_now = "auction";
				isTabShow = false;
				break;
			case 9 :
				frag_now = "collection";
				isTabShow = false;
		}
		
		if(frag_last.equals(frag_now) == false){
			
			setLoadingImg(scene, true);
			setMainMenuVisibility(false);
		
			if(isTabShow == true){
				layout_tab.setVisibility(View.VISIBLE);
			}else{
				layout_tab.setVisibility(View.INVISIBLE);
			}
			
			new Handler().postDelayed(new Runnable(){    
			    public void run() {
					FragmentManager fragmentManager = getSupportFragmentManager();
			        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			        
			        switch(frag_index){
			    		case 0 :
			    			MapFragment fragment0 = new MapFragment();
					        if(frag_last.equals("") == false)
					        	fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment0, "map");
			    			break;
			    		case 1 :
			    			StatusFragment fragment1 = new StatusFragment();
			    			if(frag_last.equals("") == false)
					        	fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment1, "status");
			    			break;
			    		case 2 :
			    			NoteFragment fragment2 = new NoteFragment();
			    			if(frag_last.equals("") == false)
			    				fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment2, "note");
			    			break;
			    		case 3 :
			    		case 9 :
			    			CollectionFragment fragment3 = new CollectionFragment();
			    			if(frag_last.equals("") == false)
			    				fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment3, "collection");
			    			break;
			    		case 4 :
			    			game_scene = globalTools.getScene();
		    				Bundle myBundle4=new Bundle();
					        myBundle4.putInt("scene", game_scene);
					        
			    			ShopFragment fragment4 = new ShopFragment();
			    			fragment4.setArguments(myBundle4);
			    			if(frag_last.equals("") == false)
			    				fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment4, "shop");
					        break;
			    		case 5:
			    			LuckyDrawFragment fragment5 = new LuckyDrawFragment();
			    			if(frag_last.equals("") == false)
			    				fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment5, "luckydraw");
					        break;
			    		case 6 :
			    			NegotiateFragment fragment6 = new NegotiateFragment();
			    			if(frag_last.equals("") == false)
			    				fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment6, "negotiate");
			    			break;
			    		case 7 :
			    			BattleFragment fragment7 = new BattleFragment();
			    			if(frag_last.equals("") == false)
			    				fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment7, "battle");
			    			break;
			    		case 8 :
			    			AuctionFragment fragment8 = new AuctionFragment();
			    			if(frag_last.equals("") == false)
			    				fragmentTransaction.remove(fragmentManager.findFragmentByTag(frag_last));
					        fragmentTransaction.add(R.id.fragment_container, fragment8, "auction");
			    			break;
			    			
			        }
	
			        fragmentTransaction.commit();
			    }    
			 }, 100);
			
		}
		
		return frag_now;
    }
    
    private void initAnim(){
		tab_btn_anim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 1.0f);
		tab_btn_anim.setFillAfter(true);
		tab_btn_anim.setDuration(500);
		
		bag_anim = new RotateAnimation(0, 359, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		bag_anim.setDuration(1000);
		bag_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				img_menu_card_l.setVisibility(View.VISIBLE);
				img_menu_card_r.setVisibility(View.VISIBLE);
				//img_menu_card_l.startAnimation(card_l_anim);
				//img_menu_card_r.startAnimation(card_r_anim);
				img_menu_card_l.startAnimation(card_l_anim_set);
				img_menu_card_r.startAnimation(card_r_anim_set);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		card_l_scale_anim = new ScaleAnimation(
				0f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 1f);
		card_l_scale_anim.setDuration(500);
		
		card_r_scale_anim = new ScaleAnimation(
				0f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 1f);
		card_r_scale_anim.setDuration(500);
		
		card_l_rotate_anim = new RotateAnimation(
				0, -30, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		card_l_rotate_anim.setFillAfter(true);
		card_l_rotate_anim.setDuration(0);
		
		card_r_rotate_anim = new RotateAnimation(
				0, 20, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		card_r_rotate_anim.setFillAfter(true);
		card_r_rotate_anim.setDuration(0);
		
		card_l_anim_set = new AnimationSet(true);
		card_l_anim_set.addAnimation(card_l_scale_anim);
		card_l_anim_set.addAnimation(card_l_rotate_anim);
		card_l_anim_set.setFillAfter(true);
		card_r_anim_set = new AnimationSet(true);
		card_r_anim_set.addAnimation(card_r_scale_anim);
		card_r_anim_set.addAnimation(card_r_rotate_anim);
		card_r_anim_set.setFillAfter(true);
		card_r_anim_set.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				btn_start.setVisibility(View.VISIBLE);
				btn_collection.setVisibility(View.VISIBLE);
				tv_menu_title.setVisibility(View.VISIBLE);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
	}
    
    private void initImg(){
    	int img_id;
    	img_id = getResources().getIdentifier("img_menu_bag", "drawable", this.getPackageName());
    	img_menu_bag.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 150, 192));
    	
    	String card1 = "front14";
    	String card2 = "front13";
    	Random random = new Random();
    	int temp = random.nextInt(3);
    	switch(temp){
    		case 0 :
    			card1 = "front14";
    			card2 = "front13";
    			break;
    		case 1 :
    			card1 = "front18";
    			card2 = "front9";
    			break;
    		case 2 :
    			card1 = "front7";
    			card2 = "front5";
    			break;
    	}
    	img_id = getResources().getIdentifier(card1, "drawable", this.getPackageName());
    	img_menu_card_l.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
    	img_id = getResources().getIdentifier(card2, "drawable", this.getPackageName());
    	img_menu_card_r.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
    	
    	img_menu_card_l.setVisibility(View.INVISIBLE);
    	img_menu_card_r.setVisibility(View.INVISIBLE);
    }
    
    private void clearTabAnim(){
		tab_1.clearAnimation();
		tab_2.clearAnimation();
		tab_3.clearAnimation();
		tab_4.clearAnimation();
	}
    
    private class TabOnClickListener implements OnClickListener {
		private int index = 0;

		public TabOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			globalTools.PlaySystemClickSE(globalTools.SE_OPTION_CLICK);
			switch (index) {
				case 0:
					clearTabAnim();
					tab_1.setImageResource(R.drawable.icon_scene_pressed);
					tab_1.startAnimation(tab_btn_anim);
					tab_2.setImageResource(R.drawable.icon_status_default);
					tab_3.setImageResource(R.drawable.icon_note_default);
					tab_4.setImageResource(R.drawable.icon_collection_default);
					break;
				case 1:
					clearTabAnim();
					tab_1.setImageResource(R.drawable.icon_scene_default);
					tab_2.setImageResource(R.drawable.icon_status_pressed);
					tab_2.startAnimation(tab_btn_anim);
					tab_3.setImageResource(R.drawable.icon_note_default);
					tab_4.setImageResource(R.drawable.icon_collection_default);
					break;
				case 2:
					clearTabAnim();
					tab_1.setImageResource(R.drawable.icon_scene_default);
					tab_2.setImageResource(R.drawable.icon_status_default);
					tab_3.setImageResource(R.drawable.icon_note_pressed);
					tab_3.startAnimation(tab_btn_anim);
					tab_4.setImageResource(R.drawable.icon_collection_default);
					break;
				case 3:
					clearTabAnim();
					tab_1.setImageResource(R.drawable.icon_scene_default);
					tab_2.setImageResource(R.drawable.icon_status_default);
					tab_3.setImageResource(R.drawable.icon_note_default);
					tab_4.setImageResource(R.drawable.icon_collection_pressed);
					tab_4.startAnimation(tab_btn_anim);
					break;
			}
			
			// TODO
			switch(index) {
				case 0:
					game_scene = globalTools.getScene();
					fragTag = showFragment(fragTag, globalTools.SHOW_MAP, game_scene);
					break;
				case 1:
					game_scene = globalTools.getScene();
					fragTag = showFragment(fragTag, globalTools.SHOW_STATUS, game_scene);
					break;
				case 2:
					showNoteNum(0, false);
					game_scene = globalTools.getScene();
					fragTag = showFragment(fragTag, globalTools.SHOW_NOTE, game_scene);
					break;
				case 3:
					fragTag = showFragment(fragTag, globalTools.SHOW_COLLECTION, globalTools.SCENE_COLLECTION);
					break;
			}
		}
	}

	@Override
	public void showTargetFragment(int frag_index) {
		game_scene = globalTools.getScene();
		fragTag = showFragment(fragTag, frag_index, game_scene);
	}

	@Override
	public void showNoteNum(int count, boolean visible) {
		tv_note_num.setText(String.valueOf(count));
		if(visible == true){
			tv_note_num.setVisibility(View.VISIBLE);
			img_note_num.setVisibility(View.VISIBLE);
		}else{
			tv_note_num.setVisibility(View.INVISIBLE);
			img_note_num.setVisibility(View.INVISIBLE);
		}
	}
	
}
