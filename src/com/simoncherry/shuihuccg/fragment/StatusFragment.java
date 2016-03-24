package com.simoncherry.shuihuccg.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.adapter.ItemListAdapter;
import com.simoncherry.shuihuccg.bean.ItemListBean;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class StatusFragment extends Fragment {
	
	private GameGlobalTools globalTools;
	
	private ViewGroup layout_status_bg;
	private TextView tv_status_mood;
	private TextView tv_status_popularity;
	private TextView tv_status_study;
	private TextView tv_status_behavior;
	private TextView tv_status_money;
	private TextView tv_item_empty;
	private ListView item_list;
	private List<ItemListBean> list = new ArrayList<ItemListBean>();
	private ItemListAdapter adapter;
	private Button btn_clear_data;
	
	final static int item_kind = 5;
	int[] item_count = new int[item_kind];
	int[] item_position = new int[item_kind];

	public StatusFragment() {
		// Required empty public constructor
	}
	
	@Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        globalTools = (GameGlobalTools) getActivity().getApplication();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_status, container, false);
	}
	
	@Override
	public void onResume() {  
        super.onResume(); 
        
        layout_status_bg = (ViewGroup) getActivity().findViewById(R.id.layout_status_bg);
        tv_status_mood = (TextView) getActivity().findViewById(R.id.tv_status_mood);
		tv_status_popularity = (TextView) getActivity().findViewById(R.id.tv_status_popularity);
		tv_status_study = (TextView) getActivity().findViewById(R.id.tv_status_study);
		tv_status_behavior = (TextView) getActivity().findViewById(R.id.tv_status_behavior);
		tv_status_money = (TextView) getActivity().findViewById(R.id.tv_status_money);
		tv_item_empty = (TextView) getActivity().findViewById(R.id.tv_item_empty);
		item_list = (ListView) getActivity().findViewById(R.id.item_list);
		btn_clear_data = (Button) getActivity().findViewById(R.id.btn_clear_data);
		
		btn_clear_data.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				for(int i=1; i<=item_kind-1; i++){
					globalTools.setItemCount(i, 0);
					globalTools.setItemPosition(i, -1);
				}
				globalTools.setItemKinds(0);
				
				list.clear();
				adapter.notifyDataSetChanged();
				Toast.makeText(getContext(), "清除完毕", Toast.LENGTH_SHORT).show();
			}
		});
		
		item_list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showItemUseDialog(getContext(), position);
			}
		});
		
		initItemList();
		if(list.size() > 0){
			tv_item_empty.setVisibility(View.INVISIBLE);
		}
		
		int money = globalTools.getPlayerMoney();
		tv_status_money.setText("￥" + String.valueOf(money));
		int study = globalTools.getCharaStudy(0);
		tv_status_study.setText("学力： " + String.valueOf(study));
		
		int game_scene = globalTools.getScene();
		setStatusBackground(game_scene);
		
		((MainActivity)getActivity()).setLoadingImg(0, false);
	}
	
	private void showItemUseDialog(Context context, final int position){
		int which = 0;
		
		for(int i=1; i<=item_kind-1; i++){
			if(globalTools.getItemPosition(i) == position){
				which = i;
				break;
			}
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		String res_name;
		int res_id;
		res_name = "item_" + String.valueOf(which);
		res_id = getResources().getIdentifier(res_name, "drawable", getContext().getPackageName());
		builder.setIcon(res_id);
		res_name = "item_" + String.valueOf(which);
		res_id = getResources().getIdentifier(res_name, "string", getContext().getPackageName());
		builder.setTitle(res_id);
		res_name = "item_" + String.valueOf(which) + "_effect";
		res_id = getResources().getIdentifier(res_name, "string", getContext().getPackageName());
		builder.setMessage(getContext().getResources().getText(res_id) + " 确定使用吗？");
		
		if(which > 0){
			final int which_item = which;
			
			builder.setPositiveButton("确定",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) { 
	                    	// TODO
	                    	int count = globalTools.getItemCount(which_item)-1;
	                    	globalTools.setItemCount(which_item, count);
	                    	modifyItemCount(which_item, position, count);
	                    	
	                    	globalTools.PlaySystemClickSE(globalTools.SE_EAT);
	                    }  
	                });
			builder.setNegativeButton("取消",  
	                new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	dialog.dismiss();
	                    }  
	                }); 
			builder.show();  
		}
	}

	private void setAdapter(List<ItemListBean> list){
		adapter = new ItemListAdapter(getActivity(), list);
		item_list.setAdapter(adapter);
	}
	
	private void refreshItemStatus(){
		for(int i=1; i<=item_kind-1; i++){
			item_count[i] = globalTools.getItemCount(i);
			item_position[i] = globalTools.getItemPosition(i);
		}
	}
	
	private void initItemList(){
		//list = new ArrayList<ItemListBean>();
		list.clear();
		setAdapter(list);
		
		refreshItemStatus();
		
		int[] array_position = new int[item_kind-1];
		for(int i=0; i<=item_kind-2; i++){
			array_position[i] = item_position[i+1];
		}
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i=0; i<array_position.length; i++){
			map.put(array_position[i], i);
		}
		Arrays.sort(array_position);
		for(int i=0; i<array_position.length; i++){
			if(array_position[i] >= 0){
				whetherAddToList(map.get(array_position[i])+1);
			}
		}
		
		//setAdapter(list);
	}
	
	private void whetherAddToList(int which){
		
		refreshItemStatus();
		
		if(item_count[which] > 0){
			String res_name;
			int res_id;
			ItemListBean listbean = new ItemListBean();
			
			res_name = "item_" + String.valueOf(which);
			res_id = getResources().getIdentifier(res_name, "drawable", getContext().getPackageName());
			listbean.setItemImg(res_id);
			
			res_name = "item_" + String.valueOf(which);
			res_id = getResources().getIdentifier(res_name, "string", getContext().getPackageName());
			listbean.setItemName(getResources().getString(res_id));
			
			res_name = "item_" + String.valueOf(which) + "_effect";
			res_id = getResources().getIdentifier(res_name, "string", getContext().getPackageName());
			listbean.setItemEffect(getResources().getString(res_id));
			
			listbean.setItemCount(item_count[which]);
			
			list.add(listbean);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void addItemToList(int which, int item_img, String item_name, String item_effect, int item_count){
		ItemListBean listbean = new ItemListBean();
		listbean.setItemImg(item_img);
		listbean.setItemName(item_name);
		listbean.setItemEffect(item_effect);
		listbean.setItemCount(item_count);
		list.add(listbean);	
		adapter.notifyDataSetChanged();

		globalTools.setItemCount(which, item_count);
		globalTools.setItemPosition(which, list.size()-1);
	}
	
	public void modifyItemCount(int which, int position, int count){

		globalTools.setItemCount(which, count);
		
		if(count > 0){
			list.get(position).setItemCount(count);
		}else{
			
			for(int i=1; i<=item_kind-1; i++){
				int temp = globalTools.getItemPosition(i);
				if(temp == position){
					globalTools.setItemPosition(i, -1);
				}else if(temp > position){
					globalTools.setItemPosition(i, temp-1);
				}
			}
			
			for(int i=position; i<list.size()-1; i++){
				list.set(i, list.get(i+1));
			}
			list.remove(list.size()-1);
			
			globalTools.setItemKinds(globalTools.getItemKinds()-1);
		}
		adapter.notifyDataSetChanged();
		
		if(list.size() == 0){
			tv_item_empty.setVisibility(View.VISIBLE);
		}
	}
	
	private void setStatusBackground(int scene){
		switch(scene){
			case 0 :
			case 20 :
    		case 22 :
				layout_status_bg.setBackgroundResource(R.drawable.map_bedroom);
				break;
			case 1 :
			case 14 :
			case 21 :
				layout_status_bg.setBackgroundResource(R.drawable.map_home);
				break;
			case 2 :
				layout_status_bg.setBackgroundResource(R.drawable.map_schoolgate);
				break;
			case 3 :
			case 8 :
			case 9 :
			case 10 :
				layout_status_bg.setBackgroundResource(R.drawable.map_classroom_morning);
				break;
			case 4 :
				layout_status_bg.setBackgroundResource(R.drawable.map_classroom_dusk);
				break;
			case 5 :
			case 23 :
			case 24 :
			case 25 :
				layout_status_bg.setBackgroundResource(R.drawable.map_shop_morning);
				break;
			case 6 :
			case 11 :
			case 12 :
			case 13 :
				layout_status_bg.setBackgroundResource(R.drawable.map_shop_dusk);
				break;
			case 7 :
				layout_status_bg.setBackgroundResource(R.drawable.loading_book);
				break;
			case 15 :
			case 17 :
			case 18 :
				layout_status_bg.setBackgroundResource(R.drawable.map_cram_school_morning);
				break;
			case 16 :
				layout_status_bg.setBackgroundResource(R.drawable.map_cram_school_dusk);
				break;	
			case 19 :
				layout_status_bg.setBackgroundResource(R.drawable.map_another_shop);
				break;
			case 26 :
				layout_status_bg.setBackgroundResource(R.drawable.map_alley);
				break;
			default :
				layout_status_bg.setBackgroundResource(R.drawable.map_classroom_morning);
				break;
		}
	}

}
