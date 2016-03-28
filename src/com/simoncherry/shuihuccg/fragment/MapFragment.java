package com.simoncherry.shuihuccg.fragment;

import java.util.ArrayList;
import java.util.Random;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.ui.TypeTextView;
import com.simoncherry.shuihuccg.ui.TypeTextView.OnTypeViewListener;
import com.simoncherry.shuihuccg.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MapFragment extends Fragment {
	public interface MapFragmentListener{
		public void showTargetFragment(int frag);
		public void showNoteNum(int count, boolean visible);
	}
	
	MapFragmentListener map_listener;
	
	private ViewGroup layout_map_bg;
	private ViewGroup layout_map_title;
	private ViewGroup layout_dialog;
	private ViewGroup layout_dialog_text;
	private ViewGroup layout_selector;
	private Button selector_1;
	private Button selector_2;
	private Button selector_3;
	private Button selector_4;
	private ImageView dialog_player_avatar;
	private ImageView dialog_computer_avatar;
	private TypeTextView dialog_text;
	private TextView dialog_chara_name;
	private TextView dialog_clickable_hint;
	private TextView tv_map_scene;
	private TextView tv_map_date;
	private TextView tv_event_result;
	
	private Animation map_title_show_anim;
	private Animation map_title_hide_anim;
	private Animation transition_show_anim;
	private Animation transition_hide_anim;
	private Animation dialog_show_anim;
	private Animation selector_show_anim;
	private Animation dialog_clickable_anim;
	private Animation event_result_show_anim;
	private Animation event_result_hide_anim;
	
	final int SCENE_BEDROOM_WEEKDAYS = 0;
	final int SCENE_HOME_WEEKDAYS = 1;
	final int SCENE_SCHOOLGATE = 2;
	final int SCENE_CLASSROOM_MORNING = 3;
	final int SCENE_CLASSROOM_DUSK = 4;
	final int SCENE_SHOP_MORNING = 5;
	final int SCENE_SHOP_DUSK = 6;
	final int SCENE_COLLECTION = 7;
	final int SCENE_TALK_CLASSROOM = 8;
	final int SCENE_EXCHANGE_CLASSROOM = 9;
	final int SCENE_AUCTION_CLASSROOM = 10;
	final int SCENE_TALK_SHOP = 11;
	final int SCENE_EXCHANGE_SHOP = 12;
	final int SCENE_AUCTION_SHOP = 13;
	final int SCENE_HOME_SATURDAY = 14;
	final int SCENE_CRAM_SCHOOL_MORNING = 15;
	final int SCENE_CRAM_SCHOOL_DUSK = 16;
	final int SCENE_TALK_CRAMSCHOOL = 17;
	final int SCENE_EXCHANGE_CRAMSCHOOL = 18;
	final int SCENE_ANOTHER_SHOP = 19;
	final int SCENE_BEDROOM_SATURDAY = 20;
	final int SCENE_HOME_SUNDAY = 21;
	final int SCENE_BEDROOM_SUNDAY = 22;
	final int SCENE_SHOP_SUNDAY = 23;
	final int SCENE_EXCHANGE_SHOP_SUNDAY = 24;
	final int SCENE_AUCTION_SHOP_SUNDAY = 25;
	final int SCENE_ALLEY = 26;
	final int SCENE_CHECK_COMPLETION = 27;
	
	final static int EVENT_NONE = 0;
	final static int EVENT_WEEKLY_MONEY = 1;
	final static int EVENT_LATE_FOR_SCHOOL = 2;
	final static int EVENT_AT_TIME_SCHOOL = 3;
	final static int EVENT_TALK_TO_CLASSMATE = 4;
	final static int EVENT_BACK_HOME_FIRST = 5;
	final static int EVENT_BACK_HOME_LATER = 6;
	final static int EVENT_DO_EXAM = 7;
	final static int EVENT_EXAM_GOOD = 8;
	final static int EVENT_EXAM_PASS = 22;
	final static int EVENT_EXAM_BAD = 9;
	final static int EVENT_ADD_INCOME = 10;
	final static int EVENT_SUB_INCOME = 11;
	final static int EVENT_NO_EXAM_CLASSROOM_1 = 12;
	final static int EVENT_NO_EXAM_CLASSROOM_2 = 13;
	final static int EVENT_NO_EXAM_AFTER_SCHOOL_1 = 14;
	final static int EVENT_NO_EXAM_AFTER_SCHOOL_2 = 15;
	final static int EVENT_CRAMSCHOOL_MORNING = 16;
	final static int EVENT_CRAMSCHOOL_DUSK = 17;
	final static int EVENT_PLAY_SUNDAY = 18;
	final static int EVENT_STUDY_SUNDAY = 19;
	final static int EVENT_GIFT_CARD = 20;
	final static int EVENT_BADGUY = 21;
	final static int EVENT_CHECK_COMPLETION = 23;
	
	final static int AVATAR_PLAYER = R.drawable.chara_0;
	final static int AVATAR_DEAN = R.drawable.chara_dean;
	final static int AVATAR_TEACHER = R.drawable.chara_teacher;
	final static int AVATAR_MOTHER = R.drawable.chara_mother;
	final static int AVATAR_GIRL = R.drawable.chara_girl;
	final static int AVATAR_BADGUY = R.drawable.chara_badguy;
	
	private GameGlobalTools globalTools;
	private int game_scene = 0;
	private int dialog_count = 0;
	private int dialog_index = 0;
	private int new_note_count = 0;
	private boolean is_show_event_result = false;
	
	final static int TYPE_NONE = 0;
	final static int TYPE_STUDY = 1;
	final static int TYPE_CARD_BONUS = 5;
	final static int TYPE_CARD_LOSE = 6;
	private int status_type = TYPE_NONE;
	private int status_change = 0;
	
	private ArrayList<String> list_dialog = new ArrayList<String>();
	private ArrayList<String> list_name = new ArrayList<String>();
	private ArrayList<Integer> list_avatar = new ArrayList<Integer>();
	
	public MapFragment() {
		// Required empty public constructor
	}
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            map_listener = (MapFragmentListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getName()
                    +" must implements interface MapFragmentListener");
        }
    }
	
	@Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        globalTools = (GameGlobalTools) getActivity().getApplication();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_map, container, false);
	}
	
	@Override
	public void onResume() {  
        super.onResume(); 

        layout_map_bg = (ViewGroup) getActivity().findViewById(R.id.layout_map_bg);
        layout_map_title = (ViewGroup) getActivity().findViewById(R.id.layout_map_title);
        layout_dialog = (ViewGroup) getActivity().findViewById(R.id.layout_dialog);
        layout_dialog_text = (ViewGroup) getActivity().findViewById(R.id.layout_dialog_text);
        layout_selector = (ViewGroup) getActivity().findViewById(R.id.layout_select);
        selector_1 = (Button) getActivity().findViewById(R.id.select_1);
		selector_2 = (Button) getActivity().findViewById(R.id.select_2);
		selector_3 = (Button) getActivity().findViewById(R.id.select_3);
		selector_4 = (Button) getActivity().findViewById(R.id.select_4);
		dialog_player_avatar = (ImageView) getActivity().findViewById(R.id.dialog_player_avatar);
		dialog_computer_avatar = (ImageView) getActivity().findViewById(R.id.dialog_computer_avatar);
		dialog_text = (TypeTextView) getActivity().findViewById(R.id.dialog_text);
		dialog_chara_name = (TextView) getActivity().findViewById(R.id.dialog_chara_name);
		dialog_clickable_hint = (TextView) getActivity().findViewById(R.id.dialog_clickable_hint);
		tv_map_scene = (TextView) getActivity().findViewById(R.id.tv_map_scene);
        tv_map_date = (TextView) getActivity().findViewById(R.id.tv_map_date);
        tv_event_result = (TextView) getActivity().findViewById(R.id.tv_event_result);
        
		initSystem();
		
    	game_scene = globalTools.getScene();
    	int game_week = globalTools.getWeek();
        setMapScene(game_scene);
        setSelector(game_scene);
        setMapDate(game_week);
        
	    ((MainActivity)getActivity()).setLoadingImg(0, false);
	    
	    showPrologue(game_scene, EVENT_NONE);
    }
	
	private void setMapScene(int scene){
		switch(scene){
			case SCENE_BEDROOM_WEEKDAYS :
			case SCENE_BEDROOM_SATURDAY :
			case SCENE_BEDROOM_SUNDAY :
				layout_map_bg.setBackgroundResource(R.drawable.map_bedroom);
				tv_map_scene.setText(R.string.map_bedroom);
				break;
			case SCENE_HOME_WEEKDAYS :
			case SCENE_HOME_SATURDAY :
			case SCENE_HOME_SUNDAY :
				layout_map_bg.setBackgroundResource(R.drawable.map_home);
				tv_map_scene.setText(R.string.map_home);
				break;
			case SCENE_SCHOOLGATE :
				layout_map_bg.setBackgroundResource(R.drawable.map_schoolgate);
				tv_map_scene.setText(R.string.map_schoolgate);
				break;
			case SCENE_CLASSROOM_MORNING :
			case SCENE_TALK_CLASSROOM :
			case SCENE_EXCHANGE_CLASSROOM :
			case SCENE_AUCTION_CLASSROOM :
				layout_map_bg.setBackgroundResource(R.drawable.map_classroom_morning);
				tv_map_scene.setText(R.string.map_classroom_morning);
				break;
			case SCENE_CLASSROOM_DUSK :
				layout_map_bg.setBackgroundResource(R.drawable.map_classroom_dusk);
				tv_map_scene.setText(R.string.map_classroom_dusk);
				break;
			case SCENE_SHOP_MORNING :
			case SCENE_SHOP_SUNDAY :
			case SCENE_EXCHANGE_SHOP_SUNDAY :
			case SCENE_AUCTION_SHOP_SUNDAY :
				layout_map_bg.setBackgroundResource(R.drawable.map_shop_morning);
				tv_map_scene.setText(R.string.map_shop_morning);
				break;
			case SCENE_SHOP_DUSK :
			case SCENE_TALK_SHOP :
			case SCENE_EXCHANGE_SHOP :
			case SCENE_AUCTION_SHOP :
				layout_map_bg.setBackgroundResource(R.drawable.map_shop_dusk);
				tv_map_scene.setText(R.string.map_shop_dusk);
				break;
			case SCENE_CRAM_SCHOOL_MORNING :
				layout_map_bg.setBackgroundResource(R.drawable.map_cram_school_morning);
				tv_map_scene.setText(R.string.map_cramschool_morning);
				break;
			case SCENE_CRAM_SCHOOL_DUSK :
				layout_map_bg.setBackgroundResource(R.drawable.map_cram_school_dusk);
				tv_map_scene.setText(R.string.map_cramschool_dusk);
				break;
			case SCENE_ANOTHER_SHOP :
				layout_map_bg.setBackgroundResource(R.drawable.map_another_shop);
				tv_map_scene.setText(R.string.map_another_shop);
				break;
			case SCENE_ALLEY :
				layout_map_bg.setBackgroundResource(R.drawable.map_alley);
				tv_map_scene.setText(R.string.map_alley);
				break;
		}
	}
	
	private void setMapDate(int week){
		String temp = "week" + String.valueOf(week);
		int tv_id = getResources().getIdentifier(temp, "string", getActivity().getPackageName());
		tv_map_date.setText(tv_id);
	}
	
	private void initSystem(){
		
		OnClickListener selector_listener = new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				setSelectorClickable(false);
				int selector_id = v.getId();
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				// TODO select
				switch(game_scene){
					case SCENE_BEDROOM_WEEKDAYS :
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectToSleep();
								break;
							case R.id.select_3 :
								selectCheckCompletion();
								break;
							case R.id.select_4 :
								break;
						}
						break;
					
					case SCENE_HOME_WEEKDAYS :
						new Thread(new Runnable() {
					        public void run() {
					        	globalTools.npcBuyCard();
					        }
					    }).start();
						
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectToSchoolFirst();
								break;
							case R.id.select_3 :
								selectToShopFirst();
								break;
							case R.id.select_4 :
								break;
						}
						break;
						
					case SCENE_SCHOOLGATE :
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectToClassRoom();
								break;
							case R.id.select_3 :
								break;
							case R.id.select_4 :
								break;
						}
						break;
						
					case SCENE_SHOP_MORNING :
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								map_listener.showTargetFragment(globalTools.SHOW_SHOP);
								break;
							case R.id.select_3 :
								selectToSchoolLater();
								break;
							case R.id.select_4 :
								break;
						}
						break;
						
					case SCENE_CLASSROOM_MORNING :
						switch(selector_id){
							case R.id.select_1 :
								selectToTalkInClassRoom();
								break;
							case R.id.select_2 :
								selectToExchangeInClassRoom();
								break;
							case R.id.select_3 :
								selectToWaitClassOver();
								break;
							case R.id.select_4 :
								break;
						}
						break;
						
					case SCENE_EXCHANGE_CLASSROOM :
						switch(selector_id){
							case R.id.select_1 :
								globalTools.setAuctionHostIndex(0);
								map_listener.showTargetFragment(globalTools.SHOW_AUCTION);
								break;
							case R.id.select_2 :
								globalTools.setAuctionHostIndex(1);
								map_listener.showTargetFragment(globalTools.SHOW_AUCTION);
								break;
							case R.id.select_3 :
								map_listener.showTargetFragment(globalTools.SHOW_NEGOTIATE);
								break;
							case R.id.select_4 :
								selectBackToClassRoom();
								break;
						}
						break;
						
					case SCENE_CLASSROOM_DUSK :
						new Thread(new Runnable() {
					        public void run() {
					        	globalTools.npcBuyCard();
					        }
					    }).start();
						
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectToShopAfterSchool();
								break;
							case R.id.select_3 :
								selectToHomeFirst();
								break;
							case R.id.select_4 :
								break;
						}
						break;
						
					case SCENE_SHOP_DUSK :
						switch(selector_id){
							case R.id.select_1 :
								selectToTalkInShop();
								break;
							case R.id.select_2 :
								map_listener.showTargetFragment(globalTools.SHOW_SHOP);
								break;
							case R.id.select_3 :
								selectToExchangeInShop();
								break;
							case R.id.select_4 :
								selectToHomeLater();
								break;
						}
						break;
						
					case SCENE_EXCHANGE_SHOP :
						switch(selector_id){
							case R.id.select_1 :
								globalTools.setAuctionHostIndex(0);
								map_listener.showTargetFragment(globalTools.SHOW_AUCTION);
								break;
							case R.id.select_2 :
								globalTools.setAuctionHostIndex(1);
								map_listener.showTargetFragment(globalTools.SHOW_AUCTION);
								break;
							case R.id.select_3 :
								map_listener.showTargetFragment(globalTools.SHOW_NEGOTIATE);
								break;
							case R.id.select_4 :
								selectBackToShop();
								break;
						}
						break;
						
					case SCENE_HOME_SATURDAY :
						new Thread(new Runnable() {
					        public void run() {
					        	globalTools.npcBuyCard();
					        }
					    }).start();
						
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectToCramSchool();
								break;
							case R.id.select_3 :
								break;
							case R.id.select_4 :
								break;
						}
						break;
						
					case SCENE_CRAM_SCHOOL_MORNING :
						switch(selector_id){
							case R.id.select_1 :
								selectToTalkInCramSchool();
								break;
							case R.id.select_2 :
								map_listener.showTargetFragment(globalTools.SHOW_NEGOTIATE);
								break;
							case R.id.select_3 :
								selectWaitCramSchoolOver();
								break;
							case R.id.select_4 :
								break;
						}
						break;
						
					case SCENE_CRAM_SCHOOL_DUSK :
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectToAnotherShop();
								break;
							case R.id.select_3 :
								selectBackHomeFirstSaturday();
								break;
							case R.id.select_4 :
								break;
						}
						break;
						
					case SCENE_ANOTHER_SHOP :
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								map_listener.showTargetFragment(globalTools.SHOW_SHOP);
								break;
							case R.id.select_3 :
								selectBackHomeLaterSaturday();
								break;
							case R.id.select_4 :
								break;
						}
						break;
					
					case SCENE_ALLEY :
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectBackHomeFromAlley();
								break;
							case R.id.select_3 :
								break;
							case R.id.select_4 :
								break;
						}
					break;
						
					case SCENE_BEDROOM_SATURDAY :
						switch(selector_id){
						case R.id.select_1 :
							break;
						case R.id.select_2 :
							selectToSleep();
							break;
						case R.id.select_3 :
							selectCheckCompletion();
							break;
						case R.id.select_4 :
							break;
					}
					break;	
					
					case SCENE_HOME_SUNDAY :
						new Thread(new Runnable() {
					        public void run() {
					        	globalTools.npcBuyCard();
					        }
					    }).start();
						
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectToStudyAtHome();
								break;
							case R.id.select_3 :
								selectToShopSunday();
								break;
							case R.id.select_4 :
								break;
						}
					break;
					
					case SCENE_SHOP_SUNDAY :
						switch(selector_id){
							case R.id.select_1 :
								selectToTalkSunday();
								break;
							case R.id.select_2 :
								map_listener.showTargetFragment(globalTools.SHOW_SHOP);
								break;
							case R.id.select_3 :
								selectToExchangeInShopSunday();
								break;
							case R.id.select_4 :
								selectBackHomeSunday();
								break;
						}
					break;
					
					case SCENE_EXCHANGE_SHOP_SUNDAY :
						switch(selector_id){
							case R.id.select_1 :
								globalTools.setAuctionHostIndex(0);
								map_listener.showTargetFragment(globalTools.SHOW_AUCTION);
								break;
							case R.id.select_2 :
								globalTools.setAuctionHostIndex(1);
								map_listener.showTargetFragment(globalTools.SHOW_AUCTION);
								break;
							case R.id.select_3 :
								map_listener.showTargetFragment(globalTools.SHOW_NEGOTIATE);
								break;
							case R.id.select_4 :
								selectBackToShopSunday();
								break;
						}
					break;
						
					
					case SCENE_BEDROOM_SUNDAY :
						switch(selector_id){
							case R.id.select_1 :
								break;
							case R.id.select_2 :
								selectToSleep();
								break;
							case R.id.select_3 :
								selectCheckCompletion();
								break;
							case R.id.select_4 :
								break;
					}
					break;
				}
			}
		};
		
		selector_1.setOnClickListener(selector_listener);
		selector_2.setOnClickListener(selector_listener);
		selector_3.setOnClickListener(selector_listener);
		selector_4.setOnClickListener(selector_listener);
		
		dialog_text.setOnTypeViewListener(new OnTypeViewListener(){
			@Override
			public void onTypeStart() {
				layout_dialog_text.setClickable(false);
			}
			@Override
			public void onTypeOver() {
				layout_dialog_text.setClickable(true);
				dialog_clickable_hint.setVisibility(View.VISIBLE);
				dialog_clickable_hint.startAnimation(dialog_clickable_anim);
			}
		});
		
		layout_dialog_text.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View v) {
				dialog_clickable_hint.clearAnimation();
				dialog_clickable_hint.setVisibility(View.INVISIBLE);
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				
				if(dialog_index < dialog_count){
					if(list_avatar.get(dialog_index) == R.drawable.chara_0){
						dialog_player_avatar.setImageResource(list_avatar.get(dialog_index));
						dialog_player_avatar.setVisibility(View.VISIBLE);
						dialog_computer_avatar.setVisibility(View.INVISIBLE);
					}else{
						dialog_player_avatar.setVisibility(View.INVISIBLE);
						dialog_computer_avatar.setImageResource(list_avatar.get(dialog_index));
						dialog_computer_avatar.setVisibility(View.VISIBLE);
					}
					dialog_chara_name.setText("【" + list_name.get(dialog_index) + "】");
					dialog_text.start(list_dialog.get(dialog_index));
					dialog_index++;
				}else{
					dialog_text.setText("");
					layout_dialog.setVisibility(View.INVISIBLE);
					dialog_player_avatar.setVisibility(View.VISIBLE);
					dialog_computer_avatar.setVisibility(View.INVISIBLE);
					dialog_player_avatar.setImageResource(R.drawable.chara_0);
					dialog_chara_name.setText("【" + getString(R.string.chara_name_0) + "】");
					layout_dialog_text.setClickable(false);
					// TODO
					if(is_show_event_result == false){
						layout_selector.startAnimation(selector_show_anim);
					}else{
						tv_event_result.startAnimation(event_result_show_anim);
					}
				}
			}
		});
		
		dialog_chara_name.setText("【" + getString(R.string.chara_name_0) + "】");
		
		InitAnimation();
	}
	
	private void InitAnimation(){
		dialog_clickable_anim = new AlphaAnimation(1.0f, 0.0f);
		//dialog_clickable_anim.setRepeatMode(Animation.RESTART);
		dialog_clickable_anim.setRepeatMode(Animation.REVERSE);
		dialog_clickable_anim.setRepeatCount(Animation.INFINITE);
		dialog_clickable_anim.setDuration(1);
		dialog_clickable_anim.setStartOffset(500);
		
		transition_show_anim = new AlphaAnimation(1.0f, 0.0f);
		transition_show_anim.setDuration(250);
		transition_show_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
				((MainActivity)getActivity()).findViewById(R.id.layout_tab).setVisibility(View.INVISIBLE);
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				layout_selector.setVisibility(View.INVISIBLE);
				layout_map_title.startAnimation(map_title_show_anim);
				setMapScene(game_scene);
				setSelector(game_scene);
				layout_map_bg.startAnimation(transition_hide_anim);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		transition_hide_anim  = new AlphaAnimation(0.0f, 1.0f);
		transition_hide_anim.setDuration(250);
		transition_hide_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				layout_map_title.startAnimation(map_title_hide_anim);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		map_title_show_anim = new TranslateAnimation(0.0f, -400.0f, 0.0f, 0.0f);
		map_title_show_anim.setDuration(0);
		map_title_show_anim.setFillAfter(true);
		
		map_title_hide_anim = new TranslateAnimation(-400.0f, 0.0f, 0.0f, 0.0f);
		map_title_hide_anim.setDuration(700);
		map_title_hide_anim.setFillAfter(true);
		map_title_hide_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {	
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				((MainActivity)getActivity()).findViewById(R.id.layout_tab).setVisibility(View.VISIBLE);
				layout_dialog.startAnimation(dialog_show_anim);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		dialog_show_anim = new AlphaAnimation(0.0f, 1.0f);
		dialog_show_anim.setStartOffset(250);
		dialog_show_anim.setDuration(250);
		dialog_show_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {	
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				layout_dialog.setVisibility(View.VISIBLE);
				showPrologue(game_scene, EVENT_NONE);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		selector_show_anim = new AlphaAnimation(0.0f, 1.0f);
		selector_show_anim.setStartOffset(250);
		selector_show_anim.setDuration(250);
		selector_show_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {	
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				layout_selector.setVisibility(View.VISIBLE);
				setSelectorClickable(true);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		event_result_show_anim = new AlphaAnimation(0.0f, 1.0f);
		event_result_show_anim.setStartOffset(250);
		event_result_show_anim.setDuration(250);
		event_result_show_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {	
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO
				switch(status_type){
					case TYPE_STUDY :
						int study = globalTools.getCharaStudy(0);
						globalTools.setCharaStudy(0, study + status_change);
						break;
					case TYPE_CARD_BONUS :
						int count1 = globalTools.getThisCardCount(0, status_change);
						globalTools.changeCardList(0, status_change, count1+1);
						break;
					case TYPE_CARD_LOSE :
						int count2 = globalTools.getThisCardCount(0, status_change);
						globalTools.changeCardList(0, status_change, count2-1);
						break;
				}
				
				tv_event_result.setVisibility(View.VISIBLE);
				tv_event_result.startAnimation(event_result_hide_anim);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		event_result_hide_anim = new AlphaAnimation(1.0f, 0.0f);
		event_result_hide_anim.setStartOffset(700);
		event_result_hide_anim.setDuration(250);
		event_result_hide_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {	
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				tv_event_result.setVisibility(View.INVISIBLE);
				is_show_event_result = false;
				layout_selector.startAnimation(selector_show_anim);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
	}
	
	private void setSelector(int scene){
		switch(scene){
			case SCENE_BEDROOM_WEEKDAYS :
			case SCENE_BEDROOM_SATURDAY :
			case SCENE_BEDROOM_SUNDAY :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.bedroom_select_2);
				selector_3.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.bedroom_select_3);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_HOME_WEEKDAYS :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.home_weekdays_select_2);
				selector_3.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.home_weekdays_select_3);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_HOME_SATURDAY :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.home_saturday_select_2);
				selector_3.setVisibility(View.INVISIBLE);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_HOME_SUNDAY :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setText(R.string.home_sunday_select_2);
				selector_2.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.home_sunday_select_3);
				selector_3.setVisibility(View.VISIBLE);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_SCHOOLGATE :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.schoolgate_select_2);
				selector_3.setVisibility(View.INVISIBLE);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_CLASSROOM_MORNING :
				selector_1.setVisibility(View.VISIBLE);
				selector_1.setText(R.string.classroom_morning_select_1);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.classroom_morning_select_2);
				selector_3.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.classroom_morning_select_3);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_CLASSROOM_DUSK :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setText(R.string.classroom_dusk_select_2);
				selector_2.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.classroom_dusk_select_3);
				selector_3.setVisibility(View.VISIBLE);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_CRAM_SCHOOL_MORNING :
				selector_1.setVisibility(View.VISIBLE);
				selector_1.setText(R.string.cramschool_morning_select_1);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.cramschool_morning_select_2);
				selector_3.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.cramschool_morning_select_3);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_CRAM_SCHOOL_DUSK :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setText(R.string.cramschool_dusk_select_2);
				selector_2.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.cramschool_dusk_select_3);
				selector_3.setVisibility(View.VISIBLE);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_SHOP_MORNING :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setText(R.string.shop_morning_select_2);
				selector_2.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.shop_morning_select_3);
				selector_3.setVisibility(View.VISIBLE);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_SHOP_DUSK :
				selector_1.setVisibility(View.VISIBLE);
				selector_1.setText(R.string.shop_dusk_select_1);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.shop_dusk_select_2);
				selector_3.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.shop_dusk_select_3);
				selector_4.setVisibility(View.VISIBLE);
				selector_4.setText(R.string.shop_dusk_select_4);
				break;
			case SCENE_SHOP_SUNDAY :
				selector_1.setVisibility(View.VISIBLE);
				selector_1.setText(R.string.shop_sunday_select_1);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.shop_sunday_select_2);
				selector_3.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.shop_sunday_select_3);
				selector_4.setVisibility(View.VISIBLE);
				selector_4.setText(R.string.shop_sunday_select_4);
				break;
			case SCENE_ANOTHER_SHOP :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setText(R.string.another_shop_select_2);
				selector_2.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.another_shop_select_3);
				selector_3.setVisibility(View.VISIBLE);
				selector_4.setVisibility(View.INVISIBLE);
				break;
			case SCENE_EXCHANGE_CLASSROOM :
			case SCENE_EXCHANGE_SHOP : 
			case SCENE_EXCHANGE_SHOP_SUNDAY : 
				selector_1.setVisibility(View.VISIBLE);
				selector_1.setText(R.string.exchange_select_1);
				selector_2.setVisibility(View.VISIBLE);
				selector_2.setText(R.string.exchange_select_2);
				selector_3.setVisibility(View.VISIBLE);
				selector_3.setText(R.string.exchange_select_3);
				selector_4.setVisibility(View.VISIBLE);
				selector_4.setText(R.string.exchange_select_4);
				break;
			case SCENE_ALLEY :
				selector_1.setVisibility(View.INVISIBLE);
				selector_2.setText(R.string.alley_select_2);
				selector_2.setVisibility(View.VISIBLE);
				selector_3.setVisibility(View.INVISIBLE);
				selector_4.setVisibility(View.INVISIBLE);
				break;
		}
	}
	
	private void showPrologue(int scene, int event){
		switch(scene){
			case SCENE_BEDROOM_WEEKDAYS:
				dialog_text.start(getString(R.string.prologue_bedroom_weekdays));
				break;	
			case SCENE_BEDROOM_SATURDAY :
				dialog_text.start(getString(R.string.prologue_bedroom_saturday));
				break;
			case SCENE_BEDROOM_SUNDAY :
				dialog_text.start(getString(R.string.prologue_bedroom_sunday));
				break;	
			case SCENE_HOME_WEEKDAYS:
				dialog_text.start(getString(R.string.prologue_home_weekdays));
				break;
			case SCENE_HOME_SATURDAY :
				dialog_text.start(getString(R.string.prologue_home_saturday));
				break;
			case SCENE_HOME_SUNDAY :
				dialog_text.start(getString(R.string.prologue_home_sunday));
				break;
			case SCENE_SCHOOLGATE:
				dialog_text.start(getString(R.string.prologue_schoolgate));
				break;
			case SCENE_CLASSROOM_MORNING:
				dialog_text.start(getString(R.string.prologue_classroom_morning));
				break;
			case SCENE_CLASSROOM_DUSK:
				dialog_text.start(getString(R.string.prologue_classroom_dusk));
				break;
			case SCENE_SHOP_MORNING:
				dialog_text.start(getString(R.string.prologue_shop_morning));
				break;
			case SCENE_SHOP_DUSK:
				dialog_text.start(getString(R.string.prologue_shop_dusk));
				break;
			case SCENE_SHOP_SUNDAY :
				dialog_text.start(getString(R.string.prologue_shop_sunday));
				break;
			case SCENE_TALK_CLASSROOM :
			case SCENE_TALK_SHOP :
			case SCENE_TALK_CRAMSCHOOL :
				dialog_text.start(getString(R.string.prologue_talk_news));
				break;
			case SCENE_EXCHANGE_CLASSROOM :
			case SCENE_EXCHANGE_SHOP:
			case SCENE_EXCHANGE_CRAMSCHOOL :
			case SCENE_EXCHANGE_SHOP_SUNDAY :
				dialog_text.start(getString(R.string.prologue_exchange));
				break;
			case SCENE_CRAM_SCHOOL_MORNING :
				dialog_text.start(getString(R.string.prologue_cramschool_morning));
				break;
			case SCENE_CRAM_SCHOOL_DUSK :
				dialog_text.start(getString(R.string.prologue_cramschool_dusk));
				break;
			case SCENE_ANOTHER_SHOP :
				dialog_text.start(getString(R.string.prologue_another_shop));
				break;
			case SCENE_ALLEY :
				dialog_text.start(getString(R.string.prologue_alley));
				break;	
			case SCENE_CHECK_COMPLETION:
				dialog_text.start(getString(R.string.prologue_completion));
				break;
		}
	}
	
	private int setEventDialogText(int event){
		list_name.clear();
		list_dialog.clear();
		list_avatar.clear();
		switch(event){
			case EVENT_WEEKLY_MONEY :
				int income = globalTools.getPlayerIncome();
				list_avatar.add(AVATAR_MOTHER);
				list_name.add(getString(R.string.chara_name_mother));
				list_dialog.add("今天的零花钱  " + String.valueOf(income) + "¥");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("谢谢");
				break;
			case EVENT_AT_TIME_SCHOOL :
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("时间还早，回课室预习下吧 ");
				list_avatar.add(AVATAR_DEAN);
				list_name.add(getString(R.string.chara_name_dean));
				list_dialog.add("没有迟到，很好");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("废话，老子外号江南西小速龙");
				list_avatar.add(AVATAR_DEAN);
				list_name.add(getString(R.string.chara_name_dean));
				list_dialog.add("快进教室 ");
				break;
			case EVENT_LATE_FOR_SCHOOL :
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("不知道迟到了没 ");
				list_avatar.add(AVATAR_DEAN);
				list_name.add(getString(R.string.chara_name_dean));
				list_dialog.add("你很沙胆啵，竟敢迟到");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("迟到总好过没到 ");
				list_avatar.add(AVATAR_DEAN);
				list_name.add(getString(R.string.chara_name_dean));
				list_dialog.add("快滚进教室");
				break;
			case EVENT_TALK_TO_CLASSMATE :
				// TODO
				new_note_count = talkAboutNewCard();
				break;
			case EVENT_BACK_HOME_FIRST :
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("还有时间，复习下吧 ");	
				break;
			case EVENT_BACK_HOME_LATER :
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("不够时间，随便应付了 ");
				break;
			case EVENT_DO_EXAM :
				list_avatar.add(AVATAR_TEACHER);
				list_name.add(getString(R.string.chara_name_teacher));
				list_dialog.add("下堂课要测验，请做好准备 ");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("哎呀求考神庇佑... ");
				list_avatar.add(R.drawable.chara_7);
				list_name.add(getString(R.string.chara_name_7));
				list_dialog.add("呵呵呵");
				list_avatar.add(R.drawable.chara_6);
				list_name.add(getString(R.string.chara_name_6));
				list_dialog.add("中文没学好，题目都看不懂");
				break;
			case EVENT_EXAM_GOOD :
				list_avatar.add(AVATAR_TEACHER);
				list_name.add(getString(R.string.chara_name_teacher));
				list_dialog.add("...王小明拿测验卷，考得不错");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("谢谢老师教导有方");
				break;
			case EVENT_EXAM_PASS :
				list_avatar.add(AVATAR_TEACHER);
				list_name.add(getString(R.string.chara_name_teacher));
				list_dialog.add("...王小明拿测验卷，及格");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("我会争取提高成绩");
				break;
			case EVENT_EXAM_BAD :
				list_avatar.add(AVATAR_TEACHER);
				list_name.add(getString(R.string.chara_name_teacher));
				list_dialog.add("...王小明拿测验卷，不及格");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("哦...");
				break;
			case EVENT_ADD_INCOME :
				list_avatar.add(AVATAR_MOTHER);
				list_name.add(getString(R.string.chara_name_mother));
				list_dialog.add("测验成绩不错，增加零用钱");
				int income2 = globalTools.getPlayerIncome();
				list_avatar.add(AVATAR_MOTHER);
				list_name.add(getString(R.string.chara_name_mother));
				list_dialog.add("今天的零花钱  " + String.valueOf(income2) + "¥");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("欸嘿嘿");
				break;
			case EVENT_SUB_INCOME :
				list_avatar.add(AVATAR_MOTHER);
				list_name.add(getString(R.string.chara_name_mother));
				list_dialog.add("考试不及格！扣零用钱！");
				int income3 = globalTools.getPlayerIncome();
				list_avatar.add(AVATAR_MOTHER);
				list_name.add(getString(R.string.chara_name_mother));
				list_dialog.add("今天的零花钱  " + String.valueOf(income3) + "¥");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("哦...");
				break;
			case EVENT_NO_EXAM_CLASSROOM_1 :
				list_avatar.add(R.drawable.chara_1);
				list_name.add(getString(R.string.chara_name_1));
				list_dialog.add("今天抽到点新卡");
				list_avatar.add(R.drawable.chara_3);
				list_name.add(getString(R.string.chara_name_3));
				list_dialog.add("什么卡，快来拿瞧瞧");
				break;
			case EVENT_NO_EXAM_CLASSROOM_2 :
				list_avatar.add(R.drawable.chara_6);
				list_name.add(getString(R.string.chara_name_6));
				list_dialog.add("这张No.108长得像咱歪果仁");
				list_avatar.add(R.drawable.chara_4);
				list_name.add(getString(R.string.chara_name_4));
				list_dialog.add("但是比你帅");
				break;
			case EVENT_NO_EXAM_AFTER_SCHOOL_1 :
				list_avatar.add(R.drawable.chara_2);
				list_name.add(getString(R.string.chara_name_2));
				list_dialog.add("去小卖部吗");
				list_avatar.add(R.drawable.chara_7);
				list_name.add(getString(R.string.chara_name_7));
				list_dialog.add("不了，我回家复习");
				list_avatar.add(R.drawable.chara_3);
				list_name.add(getString(R.string.chara_name_3));
				list_dialog.add("想做学霸么");
				break;
			case EVENT_NO_EXAM_AFTER_SCHOOL_2 :
				list_avatar.add(R.drawable.chara_4);
				list_name.add(getString(R.string.chara_name_4));
				list_dialog.add("去小卖部吗");
				list_avatar.add(R.drawable.chara_5);
				list_name.add(getString(R.string.chara_name_5));
				list_dialog.add("当然，走起");
				list_avatar.add(R.drawable.chara_1);
				list_name.add(getString(R.string.chara_name_1));
				list_dialog.add("小明来不来");
				break;
			case EVENT_CRAMSCHOOL_MORNING :
				list_avatar.add(R.drawable.chara_8);
				list_name.add(getString(R.string.chara_name_8));
				list_dialog.add("你那个区的卡片我们都没有呢");
				list_avatar.add(R.drawable.chara_9);
				list_name.add(getString(R.string.chara_name_9));
				list_dialog.add("今天有没有啥新卡片啊");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("Talk is cheap, Show the card");
				break;
			case EVENT_CRAMSCHOOL_DUSK :
				list_avatar.add(R.drawable.chara_10);
				list_name.add(getString(R.string.chara_name_10));
				list_dialog.add("哦耶，哥们下周见啦");
				list_avatar.add(R.drawable.chara_11);
				list_name.add(getString(R.string.chara_name_11));
				list_dialog.add("记得多带点新卡片来呀");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("好说好说");
				break;
			case EVENT_STUDY_SUNDAY :
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("把别人集卡片的时间都用在学习上");
				break;
			case EVENT_PLAY_SUNDAY :
				list_avatar.add(R.drawable.chara_2);
				list_name.add(getString(R.string.chara_name_2));
				list_dialog.add("靠，学霸你也来");
				list_avatar.add(R.drawable.chara_7);
				list_name.add(getString(R.string.chara_name_7));
				list_dialog.add("呵呵呵，劳逸结合");
				list_avatar.add(R.drawable.chara_3);
				list_name.add(getString(R.string.chara_name_3));
				list_dialog.add("人家是学神啦，不复习也高分");
				break;
			case EVENT_GIFT_CARD :
				list_avatar.add(AVATAR_GIRL);
				list_name.add(getString(R.string.chara_name_girl));
				list_dialog.add("喂，小明");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("咋了");
				list_avatar.add(AVATAR_GIRL);
				list_name.add(getString(R.string.chara_name_girl));
				list_dialog.add("我不集卡片，这张送你吧");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("哎哟谢谢");
				break;
			case EVENT_BADGUY :
				list_avatar.add(AVATAR_BADGUY);
				list_name.add(getString(R.string.chara_name_badguy));
				list_dialog.add("站住");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("老兄有何贵干...");
				list_avatar.add(AVATAR_BADGUY);
				list_name.add(getString(R.string.chara_name_badguy));
				list_dialog.add("老子最近也喜欢集卡片");
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("这张送给老兄吧...");
				break;
			case EVENT_CHECK_COMPLETION :
				int kinds = globalTools.getCardKinds(0);
				list_avatar.add(AVATAR_PLAYER);
				list_name.add(getString(R.string.chara_name_0));
				list_dialog.add("已经收集了 " + String.valueOf(kinds) + " 张");
				if(kinds < 108){
					list_avatar.add(AVATAR_PLAYER);
					list_name.add(getString(R.string.chara_name_0));
					list_dialog.add("还差 " + String.valueOf(108-kinds) + " 张呢");
				}else{
					list_avatar.add(AVATAR_PLAYER);
					list_name.add(getString(R.string.chara_name_0));
					list_dialog.add("哇哈哈，集齐全部108将啦！");
				}
				break;
		}
		
		int count = list_dialog.size();
		return count;
	}
	
	private void setSelectorClickable(boolean clickable){
		selector_1.setClickable(clickable);
		selector_2.setClickable(clickable);
		selector_3.setClickable(clickable);
		selector_4.setClickable(clickable);
	}

	
	// TODO select
	private void selectToSleep(){
		int week = globalTools.getWeek();
		week++;
		if(week > 7) week=1;
		setMapDate(week);
		globalTools.setWeek(week);
		
		if(week == 1){
			globalTools.setCardSetIndex(globalTools.getCardSetIndex()+1);
		}

		if(globalTools.getExamEvent() == false){
			dialog_count = setEventDialogText(EVENT_WEEKLY_MONEY);
		}else{
			int study = globalTools.getCharaExamResult(0);
			
			if(study >= 80){
				globalTools.setPlayerIncome(globalTools.getPlayerIncome()+1);
				dialog_count = setEventDialogText(EVENT_ADD_INCOME);
			}else if(study < 60){
				globalTools.setPlayerIncome(globalTools.getPlayerIncome()-1);
				dialog_count = setEventDialogText(EVENT_SUB_INCOME);
			}
			globalTools.setExamEvent(false);
		}
		dialog_index = 0;
		
		new Thread(new Runnable() {
	        public void run() {
	        	int money = globalTools.getPlayerMoney();
	        	int income = globalTools.getPlayerIncome();
	        	globalTools.setPlayerMoney(money + income);
	        	globalTools.npcGetWeeklyMoney();
	        }
	    }).start();
		
		if(week == 6){
			game_scene = SCENE_HOME_SATURDAY;
		}else if(week == 7){
			game_scene = SCENE_HOME_SUNDAY;
		}else{
			game_scene = SCENE_HOME_WEEKDAYS;
		}
		
		//game_scene = SCENE_HOME;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectCheckCompletion(){
		dialog_count = setEventDialogText(EVENT_CHECK_COMPLETION);
		dialog_index = 0;
		
		layout_selector.setVisibility(View.INVISIBLE);
		layout_dialog.setVisibility(View.VISIBLE);
		showPrologue(SCENE_CHECK_COMPLETION, EVENT_NONE);
	}
	
	private void selectToSchoolFirst(){
		status_type = TYPE_STUDY;
		status_change = 1;
		
		tv_event_result.setText("学力 +1");
		is_show_event_result = true;
		
		dialog_count = setEventDialogText(EVENT_AT_TIME_SCHOOL);
		dialog_index = 0;
		
		game_scene = SCENE_SCHOOLGATE;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToShopFirst(){
		game_scene = SCENE_SHOP_MORNING;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToSchoolLater(){
		if(lateForSchoolOrNot() == true){
			dialog_count = setEventDialogText(EVENT_LATE_FOR_SCHOOL);
		}else{
			dialog_count = setEventDialogText(EVENT_AT_TIME_SCHOOL);
		}
		dialog_index = 0;
		game_scene = SCENE_SCHOOLGATE;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToClassRoom(){
		if(doExamOrNot() == true){
			dialog_count = setEventDialogText(EVENT_DO_EXAM);
			dialog_index = 0;
			globalTools.setExamEvent(true);
		}else{
//			Random random_boolean = new Random();
//			if( random_boolean.nextBoolean()){
//				dialog_count = setEventDialogText(EVENT_NO_EXAM_CLASSROOM_1);
//			}else{
//				dialog_count = setEventDialogText(EVENT_NO_EXAM_CLASSROOM_2);
//			}
			
			Random random_int = new Random();
			int temp = random_int.nextInt(21)+1;
			if(temp <= 9){
				dialog_count = setEventDialogText(EVENT_NO_EXAM_CLASSROOM_1);
			}else if(temp <= 18){
				dialog_count = setEventDialogText(EVENT_NO_EXAM_CLASSROOM_2);
			}else{
				status_type = TYPE_CARD_BONUS;
				status_change = random_int.nextInt(108)+1;
				
				tv_event_result.setText("获得No." + String.valueOf(status_change));
				is_show_event_result = true;
				
				dialog_count = setEventDialogText(EVENT_GIFT_CARD);
			}
			
			dialog_index = 0;
		}

		game_scene = SCENE_CLASSROOM_MORNING;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToTalkInClassRoom(){
		dialog_count = setEventDialogText(EVENT_TALK_TO_CLASSMATE);
		dialog_index = 0;
		
		layout_selector.setVisibility(View.INVISIBLE);
		layout_dialog.setVisibility(View.VISIBLE);
		showPrologue(SCENE_TALK_CLASSROOM, EVENT_NONE);
		
		if(new_note_count > 0){
			map_listener.showNoteNum(new_note_count, true);
			new_note_count = 0;
		}
	}
	
	private void selectToExchangeInClassRoom(){
		game_scene = SCENE_EXCHANGE_CLASSROOM;
		globalTools.setScene(game_scene);
		setSelector(game_scene);
		layout_selector.startAnimation(selector_show_anim);
	}
	
	private void selectBackToClassRoom(){
		game_scene = SCENE_CLASSROOM_MORNING;
		globalTools.setScene(game_scene);
		setSelector(game_scene);
		layout_selector.startAnimation(selector_show_anim);
	}
	
	private void selectToWaitClassOver(){
		if(globalTools.getExamEvent() == true){
			
			int study = globalTools.getCharaStudy(0);
			globalTools.setCharaExamResult(0, study);
			if(study >= 80){
				dialog_count = setEventDialogText(EVENT_EXAM_GOOD);
			}else if(study >= 60){
				dialog_count = setEventDialogText(EVENT_EXAM_PASS);
			}else{
				dialog_count = setEventDialogText(EVENT_EXAM_BAD);
			}
			
			dialog_index = 0;
			
		}else{
			Random random_boolean = new Random();
			if( random_boolean.nextBoolean()){
				dialog_count = setEventDialogText(EVENT_NO_EXAM_AFTER_SCHOOL_1);
			}else{
				dialog_count = setEventDialogText(EVENT_NO_EXAM_AFTER_SCHOOL_2);
			}
			dialog_index = 0;
		}
		
		game_scene = SCENE_CLASSROOM_DUSK;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
		
		new Thread(new Runnable() {
	        public void run() {
	        	globalTools.npcExchangeCard();
	        }
	    }).start();
	}
	
	private void selectToShopAfterSchool(){
		game_scene = SCENE_SHOP_DUSK;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToHomeFirst(){
		status_type = TYPE_STUDY;
		status_change = 1;
		
		tv_event_result.setText("学力 +1");
		is_show_event_result = true;
		
		dialog_count = setEventDialogText(EVENT_BACK_HOME_FIRST);
		dialog_index = 0;
		
		game_scene = SCENE_BEDROOM_WEEKDAYS;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);

		new Thread(new Runnable() {
	        public void run() {
	        	globalTools.npcExchangeCard();
	        }
	    }).start();
	}
	
	private void selectToTalkInShop(){
		dialog_count = setEventDialogText(EVENT_TALK_TO_CLASSMATE);
		dialog_index = 0;
		
		layout_selector.setVisibility(View.INVISIBLE);
		layout_dialog.setVisibility(View.VISIBLE);
		showPrologue(SCENE_TALK_SHOP, EVENT_NONE);
		
		if(new_note_count > 0){
			map_listener.showNoteNum(new_note_count, true);
			new_note_count = 0;
		}
	}
	
	private void selectToExchangeInShop(){
		game_scene = SCENE_EXCHANGE_SHOP;
		globalTools.setScene(game_scene);
		setSelector(game_scene);
		layout_selector.startAnimation(selector_show_anim);
	}
	
	private void selectBackToShop(){
		game_scene = SCENE_SHOP_DUSK;
		globalTools.setScene(game_scene);
		setSelector(game_scene);
		layout_selector.startAnimation(selector_show_anim);
	}
	
	private void selectToHomeLater(){
		status_type = TYPE_STUDY;
		status_change = -1;
		
		tv_event_result.setText("学力 -1");
		is_show_event_result = true;
		
		dialog_count = setEventDialogText(EVENT_BACK_HOME_LATER);
		dialog_index = 0;
		
		game_scene = SCENE_BEDROOM_WEEKDAYS;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
		
		new Thread(new Runnable() {
	        public void run() {
	        	globalTools.npcExchangeCard();
	        }
	    }).start();
	}
	
	private void selectToCramSchool(){
		dialog_count = setEventDialogText(EVENT_CRAMSCHOOL_MORNING);
		dialog_index = 0;
		
		game_scene = SCENE_CRAM_SCHOOL_MORNING;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToTalkInCramSchool(){
		dialog_count = setEventDialogText(EVENT_TALK_TO_CLASSMATE);
		dialog_index = 0;
		
		layout_selector.setVisibility(View.INVISIBLE);
		layout_dialog.setVisibility(View.VISIBLE);
		showPrologue(SCENE_TALK_CRAMSCHOOL, EVENT_NONE);
		
		if(new_note_count > 0){
			map_listener.showNoteNum(new_note_count, true);
			new_note_count = 0;
		}
	}
	
	private void selectWaitCramSchoolOver(){
		dialog_count = setEventDialogText(EVENT_CRAMSCHOOL_DUSK);
		dialog_index = 0;
		
		game_scene = SCENE_CRAM_SCHOOL_DUSK;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToAnotherShop(){
		game_scene = SCENE_ANOTHER_SHOP;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectBackHomeFirstSaturday(){
		game_scene = SCENE_BEDROOM_SATURDAY;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectBackHomeLaterSaturday(){
		if(encounterBadGuyOrNot() == true){
			status_type = TYPE_CARD_LOSE;
			status_change = globalTools.getOneExistCard(0);
			
			tv_event_result.setText("失去No." + String.valueOf(status_change));
			is_show_event_result = true;
			
			dialog_count = setEventDialogText(EVENT_BADGUY);
			dialog_index = 0;
			
			game_scene = SCENE_ALLEY;
		}else{
			game_scene = SCENE_BEDROOM_SATURDAY;
		}
		
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectBackHomeFromAlley(){
		game_scene = SCENE_BEDROOM_SATURDAY;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToStudyAtHome(){
		status_type = TYPE_STUDY;
		status_change = 1;
		
		tv_event_result.setText("学力 +1");
		is_show_event_result = true;
		
		dialog_count = setEventDialogText(EVENT_STUDY_SUNDAY);
		dialog_index = 0;
		
		game_scene = SCENE_BEDROOM_SUNDAY;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
		
		new Thread(new Runnable() {
	        public void run() {
	        	globalTools.npcExchangeCard();
	        }
	    }).start();
	}
	
	private void selectToShopSunday(){
		dialog_count = setEventDialogText(EVENT_PLAY_SUNDAY);
		dialog_index = 0;
		
		game_scene = SCENE_SHOP_SUNDAY;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
	}
	
	private void selectToTalkSunday(){
		dialog_count = setEventDialogText(EVENT_TALK_TO_CLASSMATE);
		dialog_index = 0;
		
		layout_selector.setVisibility(View.INVISIBLE);
		layout_dialog.setVisibility(View.VISIBLE);
		showPrologue(SCENE_TALK_CRAMSCHOOL, EVENT_NONE);
		
		if(new_note_count > 0){
			map_listener.showNoteNum(new_note_count, true);
			new_note_count = 0;
		}
	}
	
	private void selectToExchangeInShopSunday(){
		game_scene = SCENE_EXCHANGE_SHOP_SUNDAY;
		globalTools.setScene(game_scene);
		setSelector(game_scene);
		layout_selector.startAnimation(selector_show_anim);
	}
	
	private void selectBackToShopSunday(){
		game_scene = SCENE_SHOP_SUNDAY;
		globalTools.setScene(game_scene);
		setSelector(game_scene);
		layout_selector.startAnimation(selector_show_anim);
	}
	
	private void selectBackHomeSunday(){
		game_scene = SCENE_BEDROOM_SUNDAY;
		globalTools.setScene(game_scene);
		layout_map_bg.startAnimation(transition_show_anim);
		
		new Thread(new Runnable() {
	        public void run() {
	        	globalTools.npcExchangeCard();
	        }
	    }).start();
	}
	
	
	// TODO
	private int talkAboutNewCard(){
		int new_note_count = 0;
		String search_string = null;
		int npc_upper_bounds;
		int npc_lower_bounds; 
		
		if(globalTools.getWeek() != 6){
        	npc_lower_bounds = 1;
        	npc_upper_bounds = 7;
        }else{
        	npc_lower_bounds = 8;
        	npc_upper_bounds = 11;
        }

		for(int i=npc_lower_bounds; i<=npc_upper_bounds; i++){
			String lib_name = "chara" + String.valueOf(i);
			SharedPreferences mySharedPreferences = getContext().getSharedPreferences(
					lib_name, Activity.MODE_PRIVATE);
			Editor editor = mySharedPreferences.edit();
			
			search_string = "new_buy_count";
			int new_buy_count = mySharedPreferences.getInt(search_string, 0); 
			editor.putInt(search_string, 0);
			editor.commit();
			
			if(new_buy_count > 0){
				int chara_avatar = globalTools.getCharaAvatar(i);
				//String chara_name = "【电脑" + String.valueOf(i) + "】";
				String chara_name = globalTools.getCharaName(i);
				
				ArrayList<String> list_new_card_name = new ArrayList<String>();
				ArrayList<String> list_lack_card_name = new ArrayList<String>();
				
				for(int j=0; j<new_buy_count; j++){
					search_string = "new_buy_index" + String.valueOf(j);
					int index = mySharedPreferences.getInt(search_string, 0);
					
					String temp =  "No." + String.valueOf(index) 
					+ " 【" + globalTools.getCardInformation("epithet", index)
					+ "·" + globalTools.getCardInformation("name", index) + "】";
					list_new_card_name.add(temp +"\n");

					int player_card_count = globalTools.getThisCardCount(0, index);
					
					if(player_card_count == 0){
						list_lack_card_name.add(temp);
						
						int note_count = globalTools.getNoteCount();
						String note_key = "note" + String.valueOf(note_count + 1);
						String code_text = "new|" + String.valueOf(i) + "|" + String.valueOf(index);
						globalTools.setNoteText(note_key, code_text);
						globalTools.setNoteCount(note_count+1);
						
						new_note_count++;
					}
				}

				if(list_lack_card_name.size() > 0){
					String new_card_name_text = getString(R.string.note_new_card);
					
					for(int k=0; k<list_lack_card_name.size(); k++){
						new_card_name_text += list_lack_card_name.get(k);
						new_card_name_text += "\n";
					}

					list_avatar.add(chara_avatar);
					list_name.add(chara_name);
					list_dialog.add(new_card_name_text);
				}
			}
		}
		
		if(new_note_count == 0){
			list_avatar.add(AVATAR_PLAYER);
			list_name.add(getString(R.string.chara_name_0));
			list_dialog.add(getString(R.string.note_no_news));
		}
		
		return new_note_count;
	}
	
	private boolean lateForSchoolOrNot(){
		Random random_boolean = new Random();
		return random_boolean.nextBoolean();
	}
	
	private boolean doExamOrNot(){
		//Random random_boolean = new Random();
		//return random_boolean.nextBoolean();
		Random random_int = new Random();
		int temp = random_int.nextInt(21)+1;
		if(temp >= 18){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean encounterBadGuyOrNot(){
		Random random_boolean = new Random();
		return random_boolean.nextBoolean();
	}
	
}
