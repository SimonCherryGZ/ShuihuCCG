package com.simoncherry.shuihuccg.fragment;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectionFragment extends Fragment {
	private TextView page_pointer;
	private ImageButton btn_left;
	private ImageButton btn_right;
	private View card_view1;
	private View card_view2;
	private View card_view3;
	private View card_view4;
	private View card_view5;
	private View card_view6;
	private ImageView card_img1;
	private ImageView card_img2;
	private ImageView card_img3;
	private ImageView card_img4;
	private ImageView card_img5;
	private ImageView card_img6;
	private TextView card_numero1;
	private TextView card_numero2;
	private TextView card_numero3;
	private TextView card_numero4;
	private TextView card_numero5;
	private TextView card_numero6;
	private TextView card_count_text1;
	private TextView card_count_text2;
	private TextView card_count_text3;
	private TextView card_count_text4;
	private TextView card_count_text5;
	private TextView card_count_text6;
	
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
		return inflater.inflate(R.layout.fragment_collection, container, false);
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
	
	@Override
	public void onResume() {  
        super.onResume();
        
        globalTools = (GameGlobalTools)getActivity().getApplication();
		
		page_pointer = (TextView) getActivity().findViewById(R.id.page_pointer);
		btn_left = (ImageButton) getActivity().findViewById(R.id.arrow_left);
		btn_right = (ImageButton) getActivity().findViewById(R.id.arrow_right);
		
		card_view1 = (View) getActivity().findViewById(R.id.include1);
		card_img1 = (ImageView)card_view1.findViewById(R.id.card_img);
		card_numero1 = (TextView)card_view1.findViewById(R.id.numero);
		card_count_text1 = (TextView)card_view1.findViewById(R.id.card_count);
		
		card_view2 = (View) getActivity().findViewById(R.id.include2);
		card_img2 = (ImageView)card_view2.findViewById(R.id.card_img);
		card_numero2 = (TextView)card_view2.findViewById(R.id.numero);
		card_count_text2 = (TextView)card_view2.findViewById(R.id.card_count);
		
		card_view3 = (View) getActivity().findViewById(R.id.include3);
		card_img3 = (ImageView)card_view3.findViewById(R.id.card_img);
		card_numero3 = (TextView)card_view3.findViewById(R.id.numero);
		card_count_text3 = (TextView)card_view3.findViewById(R.id.card_count);
		
		card_view4 = (View) getActivity().findViewById(R.id.include4);
		card_img4 = (ImageView)card_view4.findViewById(R.id.card_img);
		card_numero4 = (TextView)card_view4.findViewById(R.id.numero);
		card_count_text4 = (TextView)card_view4.findViewById(R.id.card_count);
		
		card_view5 = (View) getActivity().findViewById(R.id.include5);
		card_img5 = (ImageView)card_view5.findViewById(R.id.card_img);
		card_numero5 = (TextView)card_view5.findViewById(R.id.numero);
		card_count_text5 = (TextView)card_view5.findViewById(R.id.card_count);
		
		card_view6 = (View) getActivity().findViewById(R.id.include6);
		card_img6 = (ImageView)card_view6.findViewById(R.id.card_img);
		card_numero6 = (TextView)card_view6.findViewById(R.id.numero);
		card_count_text6 = (TextView)card_view6.findViewById(R.id.card_count);
		
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
				changeCardImg(page_num);
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
				changeCardImg(page_num);
			}
		});
		
		changePagePointer(page_num);
		changeCardImg(page_num);
		
		((MainActivity)getActivity()).setLoadingImg(0, false);
		globalTools.PlaySystemClickSE(globalTools.SE_BOOK_FILP);
	}
	
	private void changePagePointer(int num){
		page_pointer.setText(String.valueOf(num) + " / 18");
	}
	
	private void changeCardImg(int num){
		int index1 = (num-1)*6 + 1;
		int index2 = (num-1)*6 + 2;
		int index3 = (num-1)*6 + 3;
		int index4 = (num-1)*6 + 4;
		int index5 = (num-1)*6 + 5;
		int index6 = (num-1)*6 + 6;
		
		card_numero1.setText("No." + String.valueOf(index1));
		card_numero2.setText("No." + String.valueOf(index2));
		card_numero3.setText("No." + String.valueOf(index3));
		card_numero4.setText("No." + String.valueOf(index4));
		card_numero5.setText("No." + String.valueOf(index5));
		card_numero6.setText("No." + String.valueOf(index6));
		
		String card_name = null;
		int img_id = 0;
		Resources resources = getResources();
		
		SharedPreferences mySharedPreferences = 
				this.getActivity().getSharedPreferences("chara0", Activity.MODE_PRIVATE);
		String search_string = null;
		int card_count = 0;
		
		search_string = "card_count" + String.valueOf(index1);
		card_count = mySharedPreferences.getInt(search_string, 0);
		card_count_text1.setText(String.valueOf(card_count));
		if(card_count > 0){
			card_name = "front" + String.valueOf(index1);
		}else{
			card_name = "front0";
		}
		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		card_img1.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
		
		
		search_string = "card_count" + String.valueOf(index2);
		card_count = mySharedPreferences.getInt(search_string, 0);
		card_count_text2.setText(String.valueOf(card_count));
		if(card_count > 0){
			card_name = "front" + String.valueOf(index2);
		}else{
			card_name = "front0";
		}	
		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		card_img2.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
		
		
		search_string = "card_count" + String.valueOf(index3);
		card_count = mySharedPreferences.getInt(search_string, 0);
		card_count_text3.setText(String.valueOf(card_count));
		if(card_count > 0){
			card_name = "front" + String.valueOf(index3);
		}else{
			card_name = "front0";
		}
		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		card_img3.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
		
		
		search_string = "card_count" + String.valueOf(index4);
		card_count = mySharedPreferences.getInt(search_string, 0);
		card_count_text4.setText(String.valueOf(card_count));
		if(card_count > 0){
			card_name = "front" + String.valueOf(index4);
		}else{
			card_name = "front0";
		}
		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		card_img4.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
		
		
		search_string = "card_count" + String.valueOf(index5);
		card_count = mySharedPreferences.getInt(search_string, 0);
		card_count_text5.setText(String.valueOf(card_count));
		if(card_count > 0){
			card_name = "front" + String.valueOf(index5);
		}else{
			card_name = "front0";
		}
		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		card_img5.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
		
		
		search_string = "card_count" + String.valueOf(index6);
		card_count = mySharedPreferences.getInt(search_string, 0);
		card_count_text6.setText(String.valueOf(card_count));
		if(card_count > 0){
			card_name = "front" + String.valueOf(index6);
		}else{
			card_name = "front0";
		}
		img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		card_img6.setImageBitmap(globalTools.decodeSampledBitmapFromResource(getResources(), img_id, 100, 150));
	}

}
