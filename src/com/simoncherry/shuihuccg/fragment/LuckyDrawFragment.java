package com.simoncherry.shuihuccg.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.Random;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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
public class LuckyDrawFragment extends Fragment {
	
	public interface LuckyDrawFragmentListener{
		public void showTargetFragment(int frag);
	}
	
	LuckyDrawFragmentListener luckydraw_listener;
	
	private GameGlobalTools globalTools;
	
	private ViewGroup layout_luckydraw;
	private ViewGroup layout_card_detail;
	private ImageView img_noodle_bag;
	private ImageView img_card;
	private ImageView img_card_detail;
	private ImageView img_new_logo;
	private TextView tv_card_index;
	private TextView tv_card_star;
	private TextView tv_card_epithet;
	private TextView tv_card_name;
	private TextView tv_bag_count;
	private Button btn_back;
	private Animation bag_anim;
	private Animation card_anim;
	private Animation reset_anim;
	private Animation new_anim;
	
	private boolean isExistCard = false;
	

	public LuckyDrawFragment() {
		// Required empty public constructor
	}
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            luckydraw_listener = (LuckyDrawFragmentListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getName()
                    +" must implements interface LuckyDrawFragmentListener");
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
		return inflater.inflate(R.layout.fragment_luckydraw, container, false);
	}
	
	@Override
	public void onResume() {  
        super.onResume();
        layout_luckydraw = (ViewGroup) getActivity().findViewById(R.id.layout_luckydraw);
        layout_card_detail = (ViewGroup) getActivity().findViewById(R.id.layout_card_detail);
        img_noodle_bag = (ImageView) getActivity().findViewById(R.id.img_noodle_bag);
        img_card = (ImageView) getActivity().findViewById(R.id.img_card);
        img_card_detail = (ImageView) getActivity().findViewById(R.id.img_card_detail);
        img_new_logo = (ImageView) getActivity().findViewById(R.id.img_new_logo);
        tv_card_index = (TextView) getActivity().findViewById(R.id.tv_card_index);
        tv_card_star = (TextView) getActivity().findViewById(R.id.tv_card_star);
        tv_card_epithet = (TextView) getActivity().findViewById(R.id.tv_card_epithet);
        tv_card_name = (TextView) getActivity().findViewById(R.id.tv_card_name);
        tv_bag_count = (TextView) getActivity().findViewById(R.id.tv_bag_count);
        btn_back = (Button) getActivity().findViewById(R.id.btn_back);
        
        btn_back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				luckydraw_listener.showTargetFragment(globalTools.SHOW_SHOP);
			}
        });
        
        img_noodle_bag.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.PlaySystemClickSE(globalTools.SE_SYSTEM_CLICK);
				
				int bag_count = globalTools.getBagCount();
				if(bag_count > 0){
					bag_count--;
					tv_bag_count.setText("x " + String.valueOf(bag_count));
					globalTools.setBagCount(bag_count);
					if(bag_count == 0){
						btn_back.setVisibility(View.VISIBLE);
						btn_back.setClickable(true);
					}
					
					img_card.clearAnimation();
					img_card.invalidate();
					img_card.setVisibility(View.INVISIBLE);
					layout_card_detail.setVisibility(View.INVISIBLE);
					img_noodle_bag.startAnimation(bag_anim);
					
				}else{
					Toast.makeText(getContext(), "干脆面抽光了", Toast.LENGTH_SHORT).show();
				}
			}
        });
        
        initAnim();
        
        int bag_count = globalTools.getBagCount();
        tv_bag_count.setText("x " + String.valueOf(bag_count));
        
        btn_back.setVisibility(View.INVISIBLE);
        btn_back.setClickable(false);
        
        int game_scene = globalTools.getScene();
        setLuckyDrawBackground(game_scene);
        
        ((MainActivity)getActivity()).setLoadingImg(0, false);
	}
	
	private void initAnim(){
		bag_anim = new ScaleAnimation(1.0f, 1.5f, 1.0f, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		bag_anim.setRepeatCount(1);
		bag_anim.setRepeatMode(Animation.REVERSE);
		bag_anim.setDuration(500);
		bag_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation animation) {
				LoadNewCard();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		
		card_anim = new TranslateAnimation(0, 0, 0, -270); 
		card_anim.setDuration(1000);
		card_anim.setFillAfter(true);
		card_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation animation) {
				layout_card_detail.setVisibility(View.VISIBLE);
				
				if(isExistCard == false){
					isExistCard = true;
					
					globalTools.PlaySystemClickSE(globalTools.SE_NEW_CARD);
					
					img_new_logo.setVisibility(View.VISIBLE);
					img_new_logo.startAnimation(new_anim);
				}else{
					img_new_logo.setVisibility(View.INVISIBLE);
				}

			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationStart(Animation animation) {
			}	
		});
		
		reset_anim = new TranslateAnimation(0, 0, 0, 0); 
		reset_anim.setDuration(0);
		reset_anim.setFillAfter(true);
		
		new_anim = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		new_anim.setDuration(500);
	}
	
	private void LoadNewCard(){
		Random random = new Random(System.currentTimeMillis());
		//int index = random.nextInt(108) + 1;
		
		int set;
		if(globalTools.getWeek() == 6){
		    set = 13 - globalTools.getCardSetIndex();
		}else{
			set = globalTools.getCardSetIndex();
		}
		
		int chances = random.nextInt(10) + 1;
		if(chances <= 3){
			set--;
			if(set < 0) set = 13;
		}
		
		int len = globalTools.card_set[set].length;
		int temp = random.nextInt(len);
		int index = globalTools.card_set[set][temp];
		
		SharedPreferences mySharedPreferences = 
				this.getActivity().getSharedPreferences("chara0", Activity.MODE_PRIVATE);
		
		String search_string = "card_count" + String.valueOf(index);
		int card_count = mySharedPreferences.getInt(search_string, 0);
		if(card_count > 0){
			isExistCard = true;
		}else{
			isExistCard = false;
		}
		
		if(card_count <= 98){
			card_count++;
		}
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt(search_string, card_count);
		editor.commit();
		
		Resources resources = getResources();

		tv_card_index.setText(getText(R.string.hero_index) + String.valueOf(index));
		tv_card_star.setText(getText(R.string.hero_star) + globalTools.getCardInformation("star", index));
		tv_card_epithet.setText(getText(R.string.hero_epithet) + globalTools.getCardInformation("epithet", index));
		tv_card_name.setText(getText(R.string.hero_name) + globalTools.getCardInformation("name", index));
		
		String card_name = "front" + String.valueOf(index);
		int img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		
		img_card.setVisibility(View.VISIBLE);
		img_card.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 70, 105));
		img_card.startAnimation(card_anim);
		img_card_detail.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 120, 170));
	}
	
	public void setLuckyDrawBackground(int scene){
		if(scene == globalTools.SCENE_SHOP_MORNING){
			layout_luckydraw.setBackgroundResource(R.drawable.map_shop_morning);
		}else if(scene == globalTools.SCENE_SHOP_DUSK){
			layout_luckydraw.setBackgroundResource(R.drawable.map_shop_dusk);
		}else if(scene == globalTools.SCENE_ANOTHER_SHOP){
			layout_luckydraw.setBackgroundResource(R.drawable.map_another_shop);
		}else if(scene == globalTools.SCENE_SHOP_SUNDAY){
			layout_luckydraw.setBackgroundResource(R.drawable.map_shop_morning);
		}
	}

}
