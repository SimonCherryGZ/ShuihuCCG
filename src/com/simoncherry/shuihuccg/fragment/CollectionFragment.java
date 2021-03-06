package com.simoncherry.shuihuccg.fragment;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.adapter.CollectionListAdapter;
import com.simoncherry.shuihuccg.bean.CollectionListBean;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.ui.Rotate3d;

import java.util.ArrayList;
import java.util.List;

import com.simoncherry.shuihuccg.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionFragment extends Fragment{
	private TextView page_pointer;
	private ImageButton btn_left;
	private ImageButton btn_right;
//	private View card_view1;
//	private View card_view2;
//	private View card_view3;
//	private View card_view4;
//	private View card_view5;
//	private View card_view6;
//	private ImageView card_img1;
//	private ImageView card_img2;
//	private ImageView card_img3;
//	private ImageView card_img4;
//	private ImageView card_img5;
//	private ImageView card_img6;
//	private TextView card_numero1;
//	private TextView card_numero2;
//	private TextView card_numero3;
//	private TextView card_numero4;
//	private TextView card_numero5;
//	private TextView card_numero6;
//	private TextView card_count_text1;
//	private TextView card_count_text2;
//	private TextView card_count_text3;
//	private TextView card_count_text4;
//	private TextView card_count_text5;
//	private TextView card_count_text6;
	private ViewGroup layout_card_detail;
	private ImageView img_card_detail;
	private ImageView img_vague_bg;
	private GridView gridview;
	private List<CollectionListBean> list;
	private CollectionListAdapter adapter;
	
	private boolean isRotate = false;
	private int click_card_index = 0;
	private int page_num = 1;
	private GameGlobalTools globalTools;
	
//	public static CollectionFragment newInstance() {
//		CollectionFragment fragment = new CollectionFragment();
//		return fragment;
//	}
//
//	public CollectionFragment() {
//		// Required empty public constructor
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//return inflater.inflate(R.layout.fragment_collection, container, false);
		return inflater.inflate(R.layout.fragment_collection_gridview, container, false);
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
	
	@Override
	public void onResume() {  
        super.onResume();
        
        globalTools = (GameGlobalTools) getActivity().getApplication();
		
		page_pointer = (TextView) getActivity().findViewById(R.id.page_pointer);
		btn_left = (ImageButton) getActivity().findViewById(R.id.arrow_left);
		btn_right = (ImageButton) getActivity().findViewById(R.id.arrow_right);
		
//		card_view1 = (View) getActivity().findViewById(R.id.include1);
//		card_img1 = (ImageView)card_view1.findViewById(R.id.card_img);
//		card_numero1 = (TextView)card_view1.findViewById(R.id.numero);
//		card_count_text1 = (TextView)card_view1.findViewById(R.id.card_count);
//		
//		card_view2 = (View) getActivity().findViewById(R.id.include2);
//		card_img2 = (ImageView)card_view2.findViewById(R.id.card_img);
//		card_numero2 = (TextView)card_view2.findViewById(R.id.numero);
//		card_count_text2 = (TextView)card_view2.findViewById(R.id.card_count);
//		
//		card_view3 = (View) getActivity().findViewById(R.id.include3);
//		card_img3 = (ImageView)card_view3.findViewById(R.id.card_img);
//		card_numero3 = (TextView)card_view3.findViewById(R.id.numero);
//		card_count_text3 = (TextView)card_view3.findViewById(R.id.card_count);
//		
//		card_view4 = (View) getActivity().findViewById(R.id.include4);
//		card_img4 = (ImageView)card_view4.findViewById(R.id.card_img);
//		card_numero4 = (TextView)card_view4.findViewById(R.id.numero);
//		card_count_text4 = (TextView)card_view4.findViewById(R.id.card_count);
//		
//		card_view5 = (View) getActivity().findViewById(R.id.include5);
//		card_img5 = (ImageView)card_view5.findViewById(R.id.card_img);
//		card_numero5 = (TextView)card_view5.findViewById(R.id.numero);
//		card_count_text5 = (TextView)card_view5.findViewById(R.id.card_count);
//		
//		card_view6 = (View) getActivity().findViewById(R.id.include6);
//		card_img6 = (ImageView)card_view6.findViewById(R.id.card_img);
//		card_numero6 = (TextView)card_view6.findViewById(R.id.numero);
//		card_count_text6 = (TextView)card_view6.findViewById(R.id.card_count);
		
		layout_card_detail = (ViewGroup) getActivity().findViewById(R.id.layout_card_detail);
		img_card_detail = (ImageView) getActivity().findViewById(R.id.img_card_detail);
		img_vague_bg = (ImageView) getActivity().findViewById(R.id.img_vague_bg);
		gridview = (GridView) getActivity().findViewById(R.id.gridview_card);
		
		gridview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if(img_card_detail.getVisibility() == View.INVISIBLE
						&& list.get(position).getCardCount() > 0){
					click_card_index = (page_num-1)*6 + position+1;
					img_card_detail.setImageBitmap(
							globalTools.decodeSampledBitmapFromResource(getResources(), 
									globalTools.getCardFrontImgId(click_card_index),
									200, 300));
					img_vague_bg.setVisibility(View.VISIBLE);
					img_vague_bg.setClickable(true);
					img_card_detail.setVisibility(View.VISIBLE);
				}
			}
		});
		
		img_vague_bg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(img_card_detail.getVisibility() == View.VISIBLE){
					layout_card_detail.clearAnimation();
					img_vague_bg.setVisibility(View.INVISIBLE);
					img_vague_bg.setClickable(false);
					img_card_detail.setVisibility(View.INVISIBLE);
					isRotate = false;
				}
			}
		});
		
		img_card_detail.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isRotate == false){
					isRotate = true;
					applyRotateToBack(500, click_card_index);
				}else{
					isRotate = false;
					applyRotateToFront(500, click_card_index);
				}
			}
		});
		
		btn_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.soundPool.play(
						globalTools.soundPoolMap.get(globalTools.SE_BOOK_FILP), 
						globalTools.playVolume, globalTools.playVolume, 1, 0, 1.0f);
				
				if(page_num >= 2){
					page_num--;
				}else{
					page_num = 18;
				}
				changePagePointer(page_num);
				//changeCardImg(page_num);
				setCardList(page_num);
			}
		});
		
		btn_right.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				globalTools.soundPool.play(
						globalTools.soundPoolMap.get(globalTools.SE_BOOK_FILP), 
						globalTools.playVolume, globalTools.playVolume, 1, 0, 1.0f);
				
				if(page_num <= 17){
					page_num++;
				}else{
					page_num = 1;
				}
				changePagePointer(page_num);
				//changeCardImg(page_num);
				setCardList(page_num);
			}
		});
		
		changePagePointer(page_num);
		//changeCardImg(page_num);
		setCardList(page_num);
		
		((MainActivity)getActivity()).setLoadingImg(0, false);
		globalTools.PlaySystemClickSE(globalTools.SE_BOOK_FILP);
	}
	
	private void changePagePointer(int num){
		page_pointer.setText(String.valueOf(num) + " / 18");
	}
	
//	private void changeCardImg(int num){
//		int index1 = (num-1)*6 + 1;
//		int index2 = (num-1)*6 + 2;
//		int index3 = (num-1)*6 + 3;
//		int index4 = (num-1)*6 + 4;
//		int index5 = (num-1)*6 + 5;
//		int index6 = (num-1)*6 + 6;
//		
//		card_numero1.setText("No." + String.valueOf(index1));
//		card_numero2.setText("No." + String.valueOf(index2));
//		card_numero3.setText("No." + String.valueOf(index3));
//		card_numero4.setText("No." + String.valueOf(index4));
//		card_numero5.setText("No." + String.valueOf(index5));
//		card_numero6.setText("No." + String.valueOf(index6));
//		
//		String card_name = null;
//		int img_id = 0;
//		Resources resources = getResources();
//		
//		SharedPreferences mySharedPreferences = 
//				this.getActivity().getSharedPreferences("chara0", Activity.MODE_PRIVATE);
//		String search_string = null;
//		int card_count = 0;
//		
//		search_string = "card_count" + String.valueOf(index1);
//		card_count = mySharedPreferences.getInt(search_string, 0);
//		card_count_text1.setText(String.valueOf(card_count));
//		if(card_count > 0){
//			card_name = "front" + String.valueOf(index1);
//		}else{
//			card_name = "front0";
//		}
//		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
//		card_img1.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
//		
//		
//		search_string = "card_count" + String.valueOf(index2);
//		card_count = mySharedPreferences.getInt(search_string, 0);
//		card_count_text2.setText(String.valueOf(card_count));
//		if(card_count > 0){
//			card_name = "front" + String.valueOf(index2);
//		}else{
//			card_name = "front0";
//		}	
//		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
//		card_img2.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
//		
//		
//		search_string = "card_count" + String.valueOf(index3);
//		card_count = mySharedPreferences.getInt(search_string, 0);
//		card_count_text3.setText(String.valueOf(card_count));
//		if(card_count > 0){
//			card_name = "front" + String.valueOf(index3);
//		}else{
//			card_name = "front0";
//		}
//		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
//		card_img3.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
//		
//		
//		search_string = "card_count" + String.valueOf(index4);
//		card_count = mySharedPreferences.getInt(search_string, 0);
//		card_count_text4.setText(String.valueOf(card_count));
//		if(card_count > 0){
//			card_name = "front" + String.valueOf(index4);
//		}else{
//			card_name = "front0";
//		}
//		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
//		card_img4.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
//		
//		
//		search_string = "card_count" + String.valueOf(index5);
//		card_count = mySharedPreferences.getInt(search_string, 0);
//		card_count_text5.setText(String.valueOf(card_count));
//		if(card_count > 0){
//			card_name = "front" + String.valueOf(index5);
//		}else{
//			card_name = "front0";
//		}
//		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
//		card_img5.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
//		
//		
//		search_string = "card_count" + String.valueOf(index6);
//		card_count = mySharedPreferences.getInt(search_string, 0);
//		card_count_text6.setText(String.valueOf(card_count));
//		if(card_count > 0){
//			card_name = "front" + String.valueOf(index6);
//		}else{
//			card_name = "front0";
//		}
//		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
//		card_img6.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
//	}
	
	private void setAdapter(List<CollectionListBean> list){
		adapter = new CollectionListAdapter(getActivity(), list);
		gridview.setAdapter(adapter);
	}
	
	private void setCardList(int page){
		list = new ArrayList<CollectionListBean>();
		
		Resources resources = getResources();
		SharedPreferences mySharedPreferences = 
				this.getActivity().getSharedPreferences("chara0", Activity.MODE_PRIVATE);
		
		for(int i=1; i<=6; i++){
			int card_numero = (page-1)*6 + i;
			
			String search_string = "card_count" + String.valueOf(card_numero);
			int card_count = mySharedPreferences.getInt(search_string, 0);

			String card_name;
			if(card_count > 0){
				 card_name = "front" + String.valueOf(card_numero);
			}else{
				card_name = "front0";
			}
			int img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
			
			CollectionListBean listbean = new CollectionListBean();
			listbean.setCardImgId(img_id);
			listbean.setCardNumero(card_numero);
			listbean.setCardCount(card_count);
			list.add(listbean);
		}
		
		setAdapter(list);
	}
	
	private void applyRotateToBack(int duration, final int index) {
		final float centerX = img_card_detail.getWidth() / 2.0f;  
		final float centerY = img_card_detail.getHeight() / 2.0f;
		
		final Rotate3d rotateTo180 =  
		        new Rotate3d(270, 360, centerX, centerY, 0, true);  
		rotateTo180.setDuration(duration);
		rotateTo180.setFillAfter(true);
		
		final Rotate3d rotateTo270 =  
		        new Rotate3d(90, 270, centerX, centerY, 0, true);  
		rotateTo180.setDuration(0);
		rotateTo180.setFillAfter(true);
		
		final Rotate3d rotateTo90 =  
		        new Rotate3d(0, 90, centerX, centerY, 0, true);  
		rotateTo90.setDuration(duration);
		rotateTo90.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				layout_card_detail.startAnimation(rotateTo270);
				img_card_detail.setImageBitmap(
						globalTools.decodeSampledBitmapFromResource( getResources(), 
								globalTools.getCardBackImgId(index), 200, 300));
				
				layout_card_detail.startAnimation(rotateTo180);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		layout_card_detail.startAnimation(rotateTo90);
	}
	
	private void applyRotateToFront(int duration, final int index) {
		final float centerX = img_card_detail.getWidth() / 2.0f;  
		final float centerY = img_card_detail.getHeight() / 2.0f;
		
		final Rotate3d rotateTo180 =  
		        new Rotate3d(-270, -360, centerX, centerY, 0, true);  
		rotateTo180.setDuration(duration);
		rotateTo180.setFillAfter(true);
		
		final Rotate3d rotateTo270 =  
		        new Rotate3d(-90, -270, centerX, centerY, 0, true);  
		rotateTo180.setDuration(0);
		rotateTo180.setFillAfter(true);
		
		final Rotate3d rotateTo90 =  
		        new Rotate3d(0, -90, centerX, centerY, 0, true);  
		rotateTo90.setDuration(duration);
		rotateTo90.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				layout_card_detail.startAnimation(rotateTo270);
				img_card_detail.setImageBitmap(
						globalTools.decodeSampledBitmapFromResource( getResources(), 
								globalTools.getCardFrontImgId(index), 200, 300));
				
				layout_card_detail.startAnimation(rotateTo180);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		
		layout_card_detail.startAnimation(rotateTo90);
	}

}
