package com.simoncherry.shuihuccg.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.Random;

import com.nineoldandroids.view.ViewHelper;
import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.ui.Rotate3d;
import com.simoncherry.shuihuccg.R;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class BattleFragment extends Fragment {
	
	public interface BattleFragmentListener{
		public void showTargetFragment(int frag);
    }
	BattleFragmentListener battle_listener;
	
	private GameGlobalTools globalTools;
	
	private ImageView img_player_hand;
	private ImageView img_player_card_1;
	private ImageView img_player_card_2;
	private ImageView img_npc_hand;
	private ImageView img_npc_card_1;
	private ImageView img_npc_card_2;
	private ImageView img_hand_wave;
	private ImageView img_hand_background;
	private ImageView img_battle_background;
	private Button btn_battle;
	private ViewGroup layout_player_hand;
	private ViewGroup layout_npc_hand;
	private ImageView img_battle_result;
	
	private ObjectAnimator player_hand_push_anim;
	private ObjectAnimator npc_hand_push_anim;
	private ObjectAnimator player_hand_back_anim;
	private ObjectAnimator npc_hand_back_anim;

	private AnimatorSet player_card_1_anim_set;
	private AnimatorSet player_card_2_anim_set;
	private AnimatorSet npc_card_1_anim_set;
	private AnimatorSet npc_card_2_anim_set;
	
	private Animation hand_wave_anim;
	private Animation battle_result_anim;
	
	private float player_hand_posX;
	private float npc_hand_posX;
	private float player_card_1_posX;
	private float player_card_1_posY;
	private float player_card_2_posX;
	private float player_card_2_posY;
	private float npc_card_1_posX;
	private float npc_card_1_posY;
	private float npc_card_2_posX;
	private float npc_card_2_posY;
	
	private boolean player_card_side = true;
	private boolean npc_card_side = true;
	private int player_card_index = 4;
	private int npc_card_index = 61;
	final static int PLAYER_CARD_1 = 1;
	final static int PLAYER_CARD_2 = 2;
	final static int NPC_CARD_1 = 3;
	final static int NPC_CARD_2 = 4;
	
	private boolean is_battle_over = false;
	private int npc_chara_index;
	

	public BattleFragment() {
		// Required empty public constructor
	}
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            battle_listener = (BattleFragmentListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getName()
                    +" must implements interface BattleFragmentListener");
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
		return inflater.inflate(R.layout.fragment_battle, container, false);
	}
	
	@Override
	public void onResume() {  
        super.onResume();
        layout_player_hand = (ViewGroup) getActivity().findViewById(R.id.layout_player_hand);
        img_player_hand = (ImageView) getActivity().findViewById(R.id.img_player_hand);
		img_player_card_1 = (ImageView) getActivity().findViewById(R.id.img_player_card1);
		img_player_card_2 = (ImageView) getActivity().findViewById(R.id.img_player_card2);
		layout_npc_hand = (ViewGroup) getActivity().findViewById(R.id.layout_npc_hand);
		img_npc_hand = (ImageView) getActivity().findViewById(R.id.img_npc_hand);
		img_npc_card_1 = (ImageView) getActivity().findViewById(R.id.img_npc_card1);
		img_npc_card_2 = (ImageView) getActivity().findViewById(R.id.img_npc_card2);
		img_hand_wave = (ImageView) getActivity().findViewById(R.id.img_hand_wave);
		img_hand_background = (ImageView) getActivity().findViewById(R.id.img_hand_background);
		img_battle_background = (ImageView) getActivity().findViewById(R.id.img_battle_background);
		img_battle_result = (ImageView) getActivity().findViewById(R.id.img_battle_result);
		btn_battle = (Button) getActivity().findViewById(R.id.btn_battle);
		
		btn_battle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				img_battle_result.clearAnimation();
				img_battle_result.setVisibility(View.INVISIBLE);
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				
				if(is_battle_over == false){
					
					resetViewPosition();
					player_hand_push_anim.start();
					npc_hand_push_anim.start();
					
					btn_battle.setClickable(false);
					btn_battle.setVisibility(View.INVISIBLE);
				}else{
					is_battle_over = false;
					btn_battle.setText("拍");
					btn_battle.setClickable(true);
					btn_battle.setVisibility(View.VISIBLE);
					
					resetViewPosition();
					battle_listener.showTargetFragment(globalTools.SHOW_NEGOTIATE);
				}
			}
		});
		
		player_card_index = globalTools.getPlayerBattleCardIndex();
		npc_card_index = globalTools.getNpcBattleCardIndex();
		npc_chara_index = globalTools.getBattleNpcCharaIndex();
		initImg(player_card_index, npc_card_index);
		applyRotation(0,30);
		loadAnimation();
		int game_scene = globalTools.getScene();
        setBattleBackground(game_scene);
		
		((MainActivity)getActivity()).setLoadingImg(0, false);
	}
	
	private void initImg(int index1, int index2){
		String img_name = null;
		int img_id = 0;
		Resources resources = getResources();
		
		img_name = "front" + String.valueOf(index1);
		img_id = resources.getIdentifier(img_name, "drawable", this.getActivity().getPackageName());
		img_player_card_1.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 60, 90));
		img_player_card_2.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 80, 120));
		
		img_name = "back" + String.valueOf(index2);
		img_id = resources.getIdentifier(img_name, "drawable", this.getActivity().getPackageName());
		img_npc_card_1.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 60, 90));
		img_npc_card_2.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 80, 120));
		
		img_name = "player_hand";
		img_id = resources.getIdentifier(img_name, "drawable", this.getActivity().getPackageName());
		img_player_hand.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 150, 210));
		
		img_name = "enemy_hand";
		img_id = resources.getIdentifier(img_name, "drawable", this.getActivity().getPackageName());
		img_npc_hand.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 150, 210));
		
		img_hand_wave.setVisibility(View.INVISIBLE);
	}
	
	private void applyRotation(float start, float end) {
		final float centerX = img_player_card_1.getWidth() / 2.0f;  
		final float centerY = img_player_card_1.getHeight() / 2.0f;  
		final Rotate3d rotation =  
		        new Rotate3d(start, end, centerX, centerY, 0, true);  
		rotation.setDuration(0);
		rotation.setFillAfter(true);
		img_player_card_1.startAnimation(rotation);
		img_npc_card_1.startAnimation(rotation);
	}
	
	private void loadAnimation(){
		//layout_player_hand.setAnimationCacheEnabled(false);
		//layout_enemy_hand.setAnimationCacheEnabled(false);
		layout_player_hand.setDrawingCacheBackgroundColor(0xffff0000);
		layout_npc_hand.setDrawingCacheBackgroundColor(0xffff0000);
		layout_player_hand.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		layout_npc_hand.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		
		player_hand_posX = img_player_hand.getX();
		npc_hand_posX = img_npc_hand.getX();
		player_card_1_posX = img_player_card_1.getX();
		player_card_1_posY = img_player_card_1.getY();
		player_card_2_posX = img_player_card_2.getX();
		player_card_2_posY = img_player_card_2.getY();
		npc_card_1_posX = img_npc_card_1.getX();
		npc_card_1_posY = img_npc_card_1.getY();
		npc_card_2_posX = img_npc_card_2.getX();
		npc_card_2_posY = img_npc_card_2.getY();
		
		player_hand_push_anim = ObjectAnimator.ofFloat(
				layout_player_hand, "translationX", 
				player_hand_posX, player_hand_posX+100);
		player_hand_push_anim.setDuration(300);
		player_hand_push_anim.addListener(new AnimatorListener(){
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				player_hand_back_anim.start();
				player_card_1_anim_set.start();
				
				globalTools.PlaySystemClickSE(globalTools.SE_WAVE);
				
				img_hand_wave.setVisibility(View.VISIBLE);
				img_hand_wave.startAnimation(hand_wave_anim);
				img_hand_wave.setVisibility(View.INVISIBLE);
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		
		npc_hand_push_anim = ObjectAnimator.ofFloat(
				layout_npc_hand, "translationX",
				npc_hand_posX, npc_hand_posX-100);
		npc_hand_push_anim.setDuration(300);
		npc_hand_push_anim.addListener(new AnimatorListener(){
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				npc_hand_back_anim.start();
				npc_card_1_anim_set.start();
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		
		player_hand_back_anim = ObjectAnimator.ofFloat(
				img_player_hand, "translationX",
				player_hand_posX, player_hand_posX-100);
		player_hand_back_anim.setDuration(300);
		
		npc_hand_back_anim = ObjectAnimator.ofFloat(
				img_npc_hand, "translationX",
				npc_hand_posX, npc_hand_posX+100);
		npc_hand_back_anim.setDuration(300);
		
		player_card_1_anim_set = new AnimatorSet();
		player_card_1_anim_set.playTogether(
				ObjectAnimator.ofFloat(
						img_player_card_1, "translationY", 
						player_card_1_posY, player_card_1_posY+500),
				ObjectAnimator.ofFloat(
						img_player_card_1, "rotation",
						0f, 360f));
		player_card_1_anim_set.setDuration(700);
		player_card_1_anim_set.addListener(new AnimatorListener(){
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO
				Random randomno = new Random(System.nanoTime());
				boolean value = randomno.nextBoolean();
				player_card_side = value;
				
				String img_name;
				if(value == true){
					img_name = "front" + String.valueOf(player_card_index);
				}else{
					img_name = "back" + String.valueOf(player_card_index);
				}
				setCardImg(PLAYER_CARD_2, img_name);
				
				player_card_2_anim_set.start();
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		
		npc_card_1_anim_set = new AnimatorSet();
		npc_card_1_anim_set.playTogether(
				ObjectAnimator.ofFloat(
						img_npc_card_1, "translationY", 
						npc_card_1_posY, npc_card_1_posY+500),
				ObjectAnimator.ofFloat(
						img_npc_card_1, "rotation",
						0f, 360f));
		npc_card_1_anim_set.setDuration(600);
		npc_card_1_anim_set.addListener(new AnimatorListener(){
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO
				Random randomno = new Random(System.currentTimeMillis());
				boolean value = randomno.nextBoolean();
				npc_card_side = value;
				
				String img_name;
				if(value == true){
					img_name = "front" + String.valueOf(npc_card_index);
				}else{
					img_name = "back" + String.valueOf(npc_card_index);
				}
				setCardImg(NPC_CARD_2, img_name);
				
				npc_card_2_anim_set.start();
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		
		player_card_2_anim_set = new AnimatorSet();
		player_card_2_anim_set.playTogether(
				ObjectAnimator.ofFloat(
						img_player_card_2, "translationX",
						player_card_2_posX, player_card_2_posX-100),
				ObjectAnimator.ofFloat(
						img_player_card_2, "translationY",
						player_card_2_posY, player_card_2_posY+350),
				ObjectAnimator.ofFloat(img_player_card_2, "rotation",
						0f, 200f),
				ObjectAnimator.ofFloat(img_player_card_2, "scaleX",
						1.0f, 0.6f),
				ObjectAnimator.ofFloat(img_player_card_2, "scaleY",
						1.0f, 0.6f));
		player_card_2_anim_set.setDuration(500);
		
		npc_card_2_anim_set = new AnimatorSet();
		npc_card_2_anim_set.playTogether(
				ObjectAnimator.ofFloat(
						img_npc_card_2, "translationX",
						npc_card_2_posX, npc_card_2_posX+100),
				ObjectAnimator.ofFloat(
						img_npc_card_2, "translationY", 
						npc_card_2_posY, npc_card_2_posY+400),
				ObjectAnimator.ofFloat(
						img_npc_card_2, "rotation",
						0f, 260f),
				ObjectAnimator.ofFloat(img_npc_card_2, "scaleX",
						1.0f, 0.6f),
				ObjectAnimator.ofFloat(img_npc_card_2, "scaleY",
						1.0f, 0.6f));
		npc_card_2_anim_set.setDuration(600);
		npc_card_2_anim_set.addListener(new AnimatorListener(){
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				btn_battle.setVisibility(View.VISIBLE);
				
				if(player_card_side == true && npc_card_side == false){
					
					globalTools.PlaySystemClickSE(globalTools.SE_BATTLE_WIN);
					
					img_battle_result.setImageResource(R.drawable.battle_result_win);
					img_battle_result.setVisibility(View.VISIBLE);
					img_battle_result.startAnimation(battle_result_anim);
					is_battle_over = true;
					btn_battle.setText("返回");
					
					// TODO
//					battle_listener.changeCardList(0, enemy_card_index, 1);
//					battle_listener.changeCardList(enemy_index, enemy_card_index, -1);
//					battle_listener.setSelectCard(1, 0);
					globalTools.changeCardList(0, npc_card_index, 1);
					globalTools.changeCardList(npc_chara_index, npc_card_index, -1);
					globalTools.setNpcBattleCardIndex(0);
					
				}else if(player_card_side == false && npc_card_side == true){
					
					globalTools.PlaySystemClickSE(globalTools.SE_BATTLE_LOSE);
					
					img_battle_result.setImageResource(R.drawable.battle_result_lose);
					img_battle_result.setVisibility(View.VISIBLE);
					img_battle_result.startAnimation(battle_result_anim);
					is_battle_over = true;
					btn_battle.setText("返回");
					
					// TODO
//					battle_listener.changeCardList(0, player_card_index, -1);
//					battle_listener.changeCardList(enemy_index, player_card_index, 1);
//					battle_listener.setSelectCard(0, 0);
					globalTools.changeCardList(0, player_card_index, -1);
					globalTools.changeCardList(npc_chara_index, player_card_index, 1);
					globalTools.setPlayerBattleCardIndex(0);
					
				}else{
					globalTools.PlaySystemClickSE(globalTools.SE_BATTLE_DRAW);
					
					img_battle_result.setImageResource(R.drawable.battle_result_draw);
					img_battle_result.setVisibility(View.VISIBLE);
					img_battle_result.startAnimation(battle_result_anim);
				}
				
				btn_battle.setClickable(true);
				btn_battle.setVisibility(View.VISIBLE);
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
		
		hand_wave_anim = new AlphaAnimation(1.0f, 0.0f);
		hand_wave_anim.setDuration(300);
		
		battle_result_anim = new ScaleAnimation(3.0f, 1.0f, 3.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		battle_result_anim.setFillAfter(true);
		battle_result_anim.setDuration(1000);
	}
	
	public void resetViewPosition(){
		
		ViewHelper.setTranslationX(layout_player_hand, player_hand_posX);
		ViewHelper.setTranslationX(layout_npc_hand, npc_hand_posX);
		
		ViewHelper.setTranslationX(img_player_hand, player_hand_posX);
		ViewHelper.setTranslationX(img_npc_hand, npc_hand_posX);
		
		ViewHelper.setTranslationY(img_player_card_1, player_card_1_posY);
		ViewHelper.setTranslationY(img_npc_card_1, npc_card_1_posY);
		
		ViewHelper.setTranslationX(img_player_card_2, player_card_2_posX);
		ViewHelper.setTranslationX(img_npc_card_2, npc_card_2_posX);
		ViewHelper.setTranslationY(img_player_card_2, player_card_2_posY);
		ViewHelper.setTranslationY(img_npc_card_2, npc_card_2_posY);
		
		applyRotation(0,30);
	}
	
	public void setCardImg(int which, String img_name){
		Resources resources = getResources();
		int img_id = resources.getIdentifier(img_name, "drawable", this.getActivity().getPackageName());
		
		switch(which){
			case PLAYER_CARD_1 :
				img_player_card_1.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 60, 90));
				break;
			case NPC_CARD_1 :
				img_npc_card_1.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 60, 90));
				break;
			case PLAYER_CARD_2 :
				img_player_card_2.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 120, 180));
				break;
			case NPC_CARD_2 :
				img_npc_card_2.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 120, 180));
				break;
		}
	}
	
	public void changeBattleEnemy(int index){
		npc_chara_index = index;
	}
	
	public void changeCardImg(int which, int index){
		String img_name = null;
		
		if(which == 0){
			this.player_card_index = index;
			img_name = "front" + String.valueOf(player_card_index);
			setCardImg(PLAYER_CARD_1, img_name);
		}else{
			this.npc_card_index = index;
			img_name = "front" + String.valueOf(npc_card_index);
			setCardImg(NPC_CARD_1, img_name);
		}
		
		applyRotation(0,30);
	}
	
	public void setBattleBackground(int scene){
		if(scene == globalTools.SCENE_EXCHANGE_CLASSROOM){
			img_hand_background.setImageResource(R.drawable.hand_background_classroom);
			img_battle_background.setImageResource(R.drawable.battle_desktop);
		}else if(scene == globalTools.SCENE_EXCHANGE_SHOP){
			img_hand_background.setImageResource(R.drawable.hand_background_shop);
			img_battle_background.setImageResource(R.drawable.battle_road);
		}else if(scene == globalTools.SCENE_CRAM_SCHOOL_MORNING){
			img_hand_background.setImageResource(R.drawable.hand_background_cramschool);
			img_battle_background.setImageResource(R.drawable.battle_desktop);
		}else if(scene == globalTools.SCENE_EXCHANGE_SHOP_SUNDAY){
			img_hand_background.setImageResource(R.drawable.hand_background_shop);
			img_battle_background.setImageResource(R.drawable.battle_road);
		}
	}

}
