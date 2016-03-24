package com.simoncherry.shuihuccg.fragment;

import java.util.ArrayList;
import java.util.List;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.adapter.ShopListAdapter;
import com.simoncherry.shuihuccg.bean.ShopListBean;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ShopFragment extends Fragment {
	public interface ShopFragmentListener{
		public void showTargetFragment(int frag);
	}
	
	ShopFragmentListener shop_listener;
	
	private ListView shop_list;
	private List<ShopListBean> list;
	private ShopListAdapter adapter;
	
	private ViewGroup layout_shop;
	private TextView tv_shop_money;
	private TextView tv_shopkeeper_dialog;
	private ImageView list_pointer_top;
	private ImageView list_pointer_bottom;
	private ImageView img_shopkeeper_avatar;
	private Button btn_back;
	
	private GameGlobalTools globalTools;
	
	public ShopFragment() {
		// Required empty public constructor
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            shop_listener = (ShopFragmentListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getName()
                    +" must implements interface ShopFragmentListener");
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
		return inflater.inflate(R.layout.fragment_shop, container, false);
	}
	
	@Override
	public void onResume() {  
        super.onResume();
        
        shop_list = (ListView) getActivity().findViewById(R.id.shop_list);
        layout_shop = (ViewGroup) getActivity().findViewById(R.id.layout_shop);
        tv_shop_money = (TextView) getActivity().findViewById(R.id.tv_shop_money);
        tv_shopkeeper_dialog = (TextView) getActivity().findViewById(R.id.tv_shopkeeper_dialog);
        list_pointer_top = (ImageView) getActivity().findViewById(R.id.list_pointer_top);
        list_pointer_bottom = (ImageView) getActivity().findViewById(R.id.list_pointer_bottom);
        img_shopkeeper_avatar = (ImageView) getActivity().findViewById(R.id.img_shopkeeper_avatar);
        btn_back = (Button) getActivity().findViewById(R.id.btn_back);
        
        shop_list.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(view.getChildAt(0) != null){
					int top = view.getChildAt(0).getTop();
					if(firstVisibleItem == 0 && top == 0){
						list_pointer_top.setVisibility(View.INVISIBLE);
						list_pointer_bottom.setVisibility(View.VISIBLE);
					}			
				}
				
				if(view.getChildAt(visibleItemCount-1) != null){
					if(view.getLastVisiblePosition() == (view.getCount()-1) && view.getChildAt(visibleItemCount-1).getBottom() == view.getHeight() ){
						list_pointer_top.setVisibility(View.VISIBLE);
						list_pointer_bottom.setVisibility(View.INVISIBLE);
					}
				}
			}
        });
        
        btn_back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int bag_count = globalTools.getBagCount();
				if(bag_count == 0){
					shop_listener.showTargetFragment(globalTools.SHOW_MAP);
				}else{
					shop_listener.showTargetFragment(globalTools.SHOW_LUCKYDRAW);
				}
			}
        });
        
        initShopList();
        
        int money = globalTools.getPlayerMoney();
        int game_scene = globalTools.getScene();
        tv_shop_money.setText(String.valueOf(money));
        setShopBackground(game_scene);
        
        ((MainActivity)getActivity()).setLoadingImg(0, false);
	}
	
	private void setAdapter(List<ShopListBean> list){
		adapter = new ShopListAdapter(getActivity(), ShopFragment.this, list);
		shop_list.setAdapter(adapter);
	}
	
	private void initShopList(){
		list = new ArrayList<ShopListBean>();
		
			ShopListBean listbean0 = new ShopListBean();
			listbean0.setItemImg(R.drawable.item_0);
			listbean0.setItemName(getResources().getString(R.string.item_0));
			listbean0.setItemEffect(getResources().getString(R.string.item_0_effect));
			listbean0.setItemCost(1);
			list.add(listbean0);
		
			ShopListBean listbean1 = new ShopListBean();
			listbean1.setItemImg(R.drawable.item_1);
			listbean1.setItemName(getResources().getString(R.string.item_1));
			listbean1.setItemEffect(getResources().getString(R.string.item_1_effect));
			listbean1.setItemCost(1);
			list.add(listbean1);
			
			ShopListBean listbean2 = new ShopListBean();
			listbean2.setItemImg(R.drawable.item_2);
			listbean2.setItemName(getResources().getString(R.string.item_2));
			listbean2.setItemEffect(getResources().getString(R.string.item_2_effect));
			listbean2.setItemCost(2);
			list.add(listbean2);
			
			ShopListBean listbean3 = new ShopListBean();
			listbean3.setItemImg(R.drawable.item_3);
			listbean3.setItemName(getResources().getString(R.string.item_3));
			listbean3.setItemEffect(getResources().getString(R.string.item_3_effect));
			listbean3.setItemCost(3);
			list.add(listbean3);
			
			ShopListBean listbean4 = new ShopListBean();
			listbean4.setItemImg(R.drawable.item_4);
			listbean4.setItemName(getResources().getString(R.string.item_4));
			listbean4.setItemEffect(getResources().getString(R.string.item_4_effect));
			listbean4.setItemCost(4);
			list.add(listbean4);
		
		if(list.size() > 0){
			setAdapter(list);
		}
	}
	
	public void setShopkeeperDialog(String text){
		tv_shopkeeper_dialog.setText(text);
	}
	
	public void setShopBackground(int scene){
		if(scene == globalTools.SCENE_SHOP_MORNING){
			layout_shop.setBackgroundResource(R.drawable.map_shop_morning);
			img_shopkeeper_avatar.setImageResource(R.drawable.chara_storekeeper);
		}else if(scene == globalTools.SCENE_SHOP_DUSK){
			layout_shop.setBackgroundResource(R.drawable.map_shop_dusk);
			img_shopkeeper_avatar.setImageResource(R.drawable.chara_storekeeper);
		}else if(scene == globalTools.SCENE_ANOTHER_SHOP){
			layout_shop.setBackgroundResource(R.drawable.map_another_shop);
			img_shopkeeper_avatar.setImageResource(R.drawable.chara_salesclerk);
		}else if(scene == globalTools.SCENE_SHOP_SUNDAY){
			layout_shop.setBackgroundResource(R.drawable.map_shop_morning);
		}
	}
	
	public void refreshShopMoney(){
        int money = globalTools.getPlayerMoney();
        tv_shop_money.setText(String.valueOf(money));
	}

}
