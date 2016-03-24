package com.simoncherry.shuihuccg.fragment;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.ui.SelectCardDialog;
import com.simoncherry.shuihuccg.R;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NegotiateFragment extends Fragment {
	public interface NegotiateFragmentListener{
		public void showTargetFragment(int frag);
	}
	
	NegotiateFragmentListener negotiate_listener;
	
	private GameGlobalTools globalTools;
	
	private ViewGroup layout_negotiate;
	private ImageView img_player_avatar;
	private ImageView img_npc_avatar;
	private ImageView img_player_card;
	private ImageView img_npc_card;
	private ImageView btn_select_up;
	private ImageView btn_select_down;
	private TextView tv_player_dialog;
	private TextView tv_npc_dialog;
	private Button btn_back;
	private Button btn_exchange;
	private Button btn_gamble;
	private Button btn_random_card;
	private Button btn_add_card;
	
	private Animation player_card_anim;
	private Animation npc_card_anim;
	private Animation player_dialog_anim;
	private Animation npc_dialog_anim;
	
	private int npc_chara_index = 1;
	private int player_card_index = 0;
	private int npc_card_index = 0;
	
	final static int ACTION_EXCHANGE = 1;
	final static int ACTION_GAMBLE = 2;
	private int exchange_or_gamble = ACTION_EXCHANGE;
	
	final static int ANSWER_YES = 1;
	final static int ANSWER_NO = 2;
	private int yes_or_no = ANSWER_NO;
	
	private int npc_upper_bounds = 7;
	private int npc_lower_bounds = 1;
	
 
	public NegotiateFragment() {
		// Required empty public constructor
	}
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            negotiate_listener = (NegotiateFragmentListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getName()
                    +" must implements interface NegotiateFragmentListener");
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
		return inflater.inflate(R.layout.fragment_negotiate, container, false);
	}
	
	@Override
	public void onResume() {  
        super.onResume();
        
        layout_negotiate = (ViewGroup) getActivity().findViewById(R.id.layout_negotiate);
        img_player_avatar = (ImageView) getActivity().findViewById(R.id.img_player_avatar);
        img_npc_avatar = (ImageView) getActivity().findViewById(R.id.img_npc_avatar);
        img_player_card = (ImageView) getActivity().findViewById(R.id.img_player_card);
        img_npc_card = (ImageView) getActivity().findViewById(R.id.img_npc_card);
        btn_select_up = (ImageView) getActivity().findViewById(R.id.btn_select_up);
        btn_select_down = (ImageView) getActivity().findViewById(R.id.btn_select_down);
        tv_player_dialog = (TextView) getActivity().findViewById(R.id.tv_player_dialog);
        tv_npc_dialog = (TextView) getActivity().findViewById(R.id.tv_npc_dialog);
        btn_back = (Button) getActivity().findViewById(R.id.btn_back);
        btn_exchange = (Button) getActivity().findViewById(R.id.btn_exchange);
        btn_gamble = (Button) getActivity().findViewById(R.id.btn_gamble);
        btn_random_card = (Button) getActivity().findViewById(R.id.btn_random);
        btn_add_card = (Button) getActivity().findViewById(R.id.btn_add);
        
        btn_back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.setBattleNpcCharaIndex(1);
				globalTools.setPlayerBattleCardIndex(0);
				globalTools.setNpcBattleCardIndex(0);
				negotiate_listener.showTargetFragment(globalTools.SHOW_MAP);
			}
        });
        
        btn_select_up.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				npc_chara_index--;
				if(npc_chara_index < npc_lower_bounds){
					npc_chara_index = npc_upper_bounds;
				}
				setAvatar(npc_chara_index);
				npc_card_index = 0;
			}
		});
		
		btn_select_down.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				npc_chara_index++;
				if(npc_chara_index > npc_upper_bounds){
					npc_chara_index = npc_lower_bounds;
				}
				setAvatar(npc_chara_index);
				npc_card_index = 0;
			}
		});

		img_player_card.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final SelectCardDialog.Builder builder = new SelectCardDialog.Builder(getContext());
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						player_card_index = builder.select_card_index;
						setSelectCard(0, player_card_index);
						dialog.dismiss();
					}
				});
				
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				builder.setEnemyCardList(npc_chara_index);
				builder.setWhichCardList(0);
				builder.create().show();
			}
		});
		
		img_npc_card.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final SelectCardDialog.Builder builder = new SelectCardDialog.Builder(getContext());
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						npc_card_index = builder.select_card_index;
						setSelectCard(1, npc_card_index);
						dialog.dismiss();
					}
				});
				
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				builder.setEnemyCardList(0);
				builder.setWhichCardList(npc_chara_index);
				builder.create().show();
			}
		});
		
		btn_exchange.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				
				if(player_card_index != 0 && npc_card_index != 0){
					
					exchange_or_gamble = ACTION_EXCHANGE;
					tv_player_dialog.setText("要交换吗？");
					tv_player_dialog.setVisibility(View.VISIBLE);
					tv_player_dialog.startAnimation(player_dialog_anim);
					
				}else{
					Toast.makeText(getContext(), "还没选好卡片", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_gamble.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				
				if(player_card_index != 0 && npc_card_index != 0){			
					
					exchange_or_gamble = ACTION_GAMBLE;
					tv_player_dialog.setText("要赌卡吗？");
					tv_player_dialog.setVisibility(View.VISIBLE);
					tv_player_dialog.startAnimation(player_dialog_anim);	
					
				}else{
					Toast.makeText(getContext(), "还没选好卡片", Toast.LENGTH_SHORT).show();
				}
			}	
		});
		
		btn_random_card.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				randomNpcCardList(npc_chara_index);
			}
		});
		
		btn_add_card.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				addNpcCard(npc_chara_index);
			}
		});
        
        initAnimation();
        player_card_index = globalTools.getPlayerBattleCardIndex();
		npc_card_index = globalTools.getNpcBattleCardIndex();
		npc_chara_index = globalTools.getBattleNpcCharaIndex();
		
		if(globalTools.getWeek() != 6){
        	npc_lower_bounds = 1;
        	npc_upper_bounds = 7;
        }else{
        	npc_lower_bounds = 8;
        	npc_upper_bounds = 11;
        	
        	if(npc_chara_index == 1){
        		npc_chara_index = 8;
        	}
        }
		
        setAvatar(npc_chara_index);
        setSelectCard(0, player_card_index);
        setSelectCard(npc_chara_index, npc_card_index);
        
        int game_scene = globalTools.getScene();
        setNegotiateBackground(game_scene);
        
        
        
        ((MainActivity)getActivity()).setLoadingImg(0, false);
	}
	
	
	private void initAnimation(){
		// n_x: 160  n_y: 220
		// p_x: 400  p_y: 770
		player_card_anim = new TranslateAnimation(
				Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, -240,
				Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, -550);
		player_card_anim.setDuration(700);
		player_card_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				setSelectCard(0, player_card_index);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		npc_card_anim = new TranslateAnimation(
				Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 240,
				Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 550);
		npc_card_anim.setDuration(700);
		npc_card_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				setSelectCard(1, npc_card_index);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		player_dialog_anim = new ScaleAnimation(
				0f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 1f);
		player_dialog_anim.setDuration(500);
		player_dialog_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				
				new Handler().postDelayed(new Runnable(){    
				    public void run() {    
				    	
				    	int player_card_count = globalTools.getThisCardCount(npc_chara_index, player_card_index);
				    	int npc_card_count = globalTools.getThisCardCount(npc_chara_index, npc_card_index);
				    	
				    	if(player_card_count == 0 && npc_card_count > 1){
				    		yes_or_no = ANSWER_YES;
				    	}else{
				    		yes_or_no = ANSWER_NO;
				    	}
				    	
				    	if(exchange_or_gamble == ACTION_EXCHANGE){
				    		
				    		if(yes_or_no == ANSWER_YES){
				    			tv_npc_dialog.setText("行，成交");
				    		}else{
				    			tv_npc_dialog.setText("呃，不要");
				    		}
				    		tv_npc_dialog.setVisibility(View.VISIBLE);
				    		tv_npc_dialog.startAnimation(npc_dialog_anim);
				    		
				    	}else if(exchange_or_gamble == ACTION_GAMBLE){
				    		
				    		if(yes_or_no == ANSWER_YES){
				    			tv_npc_dialog.setText("好，来吧！");
				    		}else{
				    			tv_npc_dialog.setText("呃，不要");
				    		}
				    		tv_npc_dialog.setVisibility(View.VISIBLE);
				    		tv_npc_dialog.startAnimation(npc_dialog_anim);
				    	}
				    }    
				 }, 1500);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		npc_dialog_anim = new ScaleAnimation(
				0f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 1f);
		npc_dialog_anim.setDuration(500);
		npc_dialog_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				
				new Handler().postDelayed(new Runnable(){    
				    public void run() {    
				    	tv_player_dialog.setVisibility(View.INVISIBLE);
						tv_npc_dialog.setVisibility(View.INVISIBLE); 
						
						if(yes_or_no == ANSWER_YES){
							if(exchange_or_gamble == ACTION_EXCHANGE){
								
								globalTools.changeCardList(0, player_card_index, -1);
								globalTools.changeCardList(0, npc_card_index, 1);
								globalTools.changeCardList(npc_chara_index, player_card_index, 1);
								globalTools.changeCardList(npc_chara_index, npc_card_index, -1);
								
								int temp = npc_card_index;
								npc_card_index = player_card_index;
								player_card_index = temp;
								
								img_player_card.startAnimation(player_card_anim);
								img_npc_card.startAnimation(npc_card_anim);		
								
							} else if(exchange_or_gamble == ACTION_GAMBLE){
								// TODO
//								negotiateListener.showTargetFragment(global_data.JUMP_BATTLE);
//								negotiateListener.changeBattleCard(0, player_select_index);
//								negotiateListener.changeBattleCard(1, enemy_select_index);
//								negotiateListener.changeBattleEnemy(enemy_index);
//								negotiateListener.changeBattleBackground(which_scene);
								globalTools.setPlayerBattleCardIndex(player_card_index);
								globalTools.setNpcBattleCardIndex(npc_card_index);
								globalTools.setBattleNpcCharaIndex(npc_chara_index);
								negotiate_listener.showTargetFragment(globalTools.SHOW_BATTLE);
							}
						}
						
				    }    
				 }, 1500);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
	}
	
	private void setAvatar(int chara){
		String img_name = null;
		int img_id = 0;
		Resources resources = getResources();
		
		img_name = "chara_" + String.valueOf(chara);
		img_id = resources.getIdentifier(img_name, "drawable", this.getActivity().getPackageName());
		if(chara == 0){
			img_player_avatar.setImageResource(img_id);
			img_player_card.setImageResource(R.drawable.front0);
		}else{
			img_npc_avatar.setImageResource(img_id);
			img_npc_card.setImageResource(R.drawable.front0);
		}
		
	}
	
	public void setSelectCard(int chara, int index){
		String card_name = null;
		int img_id = 0;
		Resources resources = getResources();
		
		card_name = "front" + String.valueOf(index);
		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		if(chara == 0){
			img_player_card.setImageBitmap(
					globalTools.decodeSampledBitmapFromResource(
							getResources(), img_id, 80, 120));
		}else{
			img_npc_card.setImageBitmap(
					globalTools.decodeSampledBitmapFromResource(
							getResources(), img_id, 80, 120));
		}
	}
	
	private void randomNpcCardList(int which){
		String lib_name = "chara" + String.valueOf(which);
		SharedPreferences mySharedPreferences = getContext().getSharedPreferences(
				lib_name, Activity.MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		editor.clear();
		
		Random  r = new Random();
        List<Integer> list = new ArrayList<Integer>();
        int index;
        while(list.size() < 10){
        	index = r.nextInt(109)+1;
            if(!list.contains(index)){
                list.add(index);
            }
        }
        
        String search_string = null;
        for(int i=0; i<10; i++){
        	search_string = "card_count" + String.valueOf(list.get(i));
        	editor.putInt(search_string, 1);
        }
        editor.commit();
	}
	
	private void addNpcCard(int which){
		String lib_name = "chara" + String.valueOf(which);
		SharedPreferences mySharedPreferences = getContext().getSharedPreferences(
				lib_name, Activity.MODE_PRIVATE);
		Editor editor = mySharedPreferences.edit();
		
		String search_string = null;
        for(int i=1; i<=108; i++){
        	search_string = "card_count" + String.valueOf(i);
        	int card_count = mySharedPreferences.getInt(search_string, 0);
        	if(card_count > 0 && card_count <= 98){
        		editor.putInt(search_string, card_count+1);
        	}
        }
        editor.commit();
	}
	
	public void setNegotiateBackground(int scene){
		if(scene == globalTools.SCENE_EXCHANGE_CLASSROOM){
			layout_negotiate.setBackgroundResource(R.drawable.map_classroom_morning);
		}else if(scene == globalTools.SCENE_EXCHANGE_SHOP){
			layout_negotiate.setBackgroundResource(R.drawable.map_shop_dusk);
		}else if(scene == globalTools.SCENE_CRAM_SCHOOL_MORNING){
			layout_negotiate.setBackgroundResource(R.drawable.map_cram_school_morning);
		}else if(scene == globalTools.SCENE_EXCHANGE_SHOP_SUNDAY){
			layout_negotiate.setBackgroundResource(R.drawable.map_shop_morning);
		}
	}

}
