package com.simoncherry.shuihuccg.fragment;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.adapter.AuctionListAdapter;
import com.simoncherry.shuihuccg.bean.AuctionListBean;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.ui.SelectCardDialog;
import com.simoncherry.shuihuccg.R;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AuctionFragment extends Fragment {
	
	public interface AuctionFragmentListener{
		public void showTargetFragment(int frag);
	}
	
	AuctionFragmentListener auction_listener;
	
	private GameGlobalTools globalTools;
	
	private ListView auction_list;
	private List<AuctionListBean> list;
	private AuctionListAdapter adapter;
	
	private ViewGroup layout_auction;
	private ImageView img_auction_chara;
	private ImageView img_auction_card;
	private ImageView img_new_logo;
	private TextView tv_auction_dialog;
	private TextView tv_auction_list_loading;
	private TextView tv_card_value;
	private Button btn_auction;
	private Button btn_ignore;
	private Animation auction_dialog_anim;
	
	private final int STEP_HOST_SELECT_CARD = 0;
	private final int STEP_PLAYER_SELECT_CARD = 1;
	private final int STEP_NPC_SELECT_CARD = 2;
	private final int STEP_HOST_DEAL = 3;
	private final int STEP_THIS_DEAL_OVER = 4;
	private final int STEP_ALL_DEAL_OVER = 5;
	private int auction_step = 0;
	
	private int auction_round = 0;
	private int auction_host_index;
	private int auction_card_index;
	private boolean isPlayerAsHost = false;
	private Boolean isHostWantToQuit = false;
	
	List<Integer> compareValueList = new ArrayList<Integer>();
	HashMap<Integer, Integer> valueCharaMap = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> charaIndexMap = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> IndexPosMap = new HashMap<Integer, Integer>();
	

	public AuctionFragment() {
		// Required empty public constructor
	}
	
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            auction_listener = (AuctionFragmentListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getClass().getName()
                    +" must implements interface AuctionFragmentListener");
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
		return inflater.inflate(R.layout.fragment_auction, container, false);
	}
	
	@Override
	public void onResume() {  
        super.onResume();
        
        layout_auction = (ViewGroup) getActivity().findViewById(R.id.layout_auction);
        auction_list = (ListView) getActivity().findViewById(R.id.auction_list);
        img_auction_chara = (ImageView) getActivity().findViewById(R.id.img_auction_chara);
        img_auction_card = (ImageView) getActivity().findViewById(R.id.img_auction_card);
        img_new_logo = (ImageView) getActivity().findViewById(R.id.img_new_logo);
        tv_auction_dialog = (TextView) getActivity().findViewById(R.id.tv_auction_dialog);
        tv_auction_list_loading = (TextView) getActivity().findViewById(R.id.tv_auction_list_loading);
        tv_card_value = (TextView) getActivity().findViewById(R.id.tv_card_value);
        btn_auction = (Button) getActivity().findViewById(R.id.btn_auction);
        btn_ignore = (Button)getActivity().findViewById(R.id.btn_ignore);
        
        btn_auction.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isPlayerAsHost == true){
					
					switch(auction_step){
					
						case STEP_NPC_SELECT_CARD :
							
							if(auction_card_index != 0){
								if(auction_round == 0){
									tv_auction_dialog.setText("谁有兴趣？");
									tv_auction_dialog.setVisibility(View.VISIBLE);
									tv_auction_dialog.startAnimation(auction_dialog_anim);
									btn_auction.setText("不满意");
									btn_ignore.setText("取消");
								}else{
									tv_auction_dialog.setText("有更好的吗？");
									tv_auction_dialog.setVisibility(View.VISIBLE);
									tv_auction_dialog.startAnimation(auction_dialog_anim);
								}
								auction_round++;
								
							}else{
								Toast.makeText(getContext(), "还没选择卡片！", Toast.LENGTH_SHORT).show();
							}
							break;
							
						case STEP_THIS_DEAL_OVER :
							
							auction_round = 0;
							auction_card_index = 0;
							setAuctionCard(auction_card_index);
							tv_card_value.setText(String.valueOf(0));
							tv_auction_list_loading.setVisibility(View.INVISIBLE);
							tv_auction_dialog.setVisibility(View.INVISIBLE);
							btn_auction.setText("开拍");
							btn_ignore.setText("退出");
							btn_ignore.setVisibility(View.VISIBLE);
							
							if(list != null){
								list.clear();
								adapter.notifyDataSetChanged();
							}
							auction_step = STEP_NPC_SELECT_CARD;
							break;
					}
					
				}else{
					
					switch(auction_step){
					
						case STEP_HOST_SELECT_CARD :
							
							boolean isHostContinueAuction = false;
							for(int i=1; i<=7; i++){
								int rare_card_count = globalTools.getRareCardCount(auction_host_index);
								if(rare_card_count > 0 && isHostWantToQuit == false){
									setAuctionHostAvatar(auction_host_index);
									isHostContinueAuction = true;
									break;
								}
								if(auction_host_index <= 6){
									auction_host_index++;
									isHostWantToQuit = false;
								}
							}
							
							if(isHostContinueAuction == true){
								
								auction_round=1;
								auction_card_index = hostSelectCardToAuction(auction_host_index);
								setAuctionCard(auction_card_index);
								
								if(globalTools.getThisCardCount(0, auction_card_index) == 0){
									img_new_logo.setVisibility(View.VISIBLE);
								}
								
								int card_value = globalTools.calcCardValue(auction_card_index)/1000;
								tv_card_value.setText(String.valueOf(card_value));	
								
								tv_auction_dialog.setText("谁有兴趣？");
								tv_auction_dialog.setVisibility(View.VISIBLE);
								tv_auction_dialog.startAnimation(auction_dialog_anim);
								btn_auction.setText("参与竞拍");
								btn_ignore.setText("不参与");	
								auction_step = STEP_PLAYER_SELECT_CARD;
								
							}else{
								btn_auction.setVisibility(View.INVISIBLE);
								tv_auction_list_loading.setText("没有新的卡片了，竞拍结束");
								tv_auction_list_loading.setVisibility(View.VISIBLE);
								auction_step = STEP_ALL_DEAL_OVER;
							}
							
							break;
							
						case STEP_PLAYER_SELECT_CARD :
							
							final SelectCardDialog.Builder builder = new SelectCardDialog.Builder(getContext());
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog, int which) {
									int player_card_index = builder.select_card_index;
									
									list = new ArrayList<AuctionListBean>();
									setAdapter(list);
									addAuctionItemToList(0, player_card_index, true);
									
									if(globalTools.getThisCardCount(auction_host_index, player_card_index) == 0){
										int value = globalTools.calcCardValue(player_card_index);
										compareValueList.add(value);
										valueCharaMap.put(value, 0);
										charaIndexMap.put(0, player_card_index);
										IndexPosMap.put(player_card_index, auction_list.getCount()-1);
									}
									
									btn_auction.setText("选好了");
									btn_ignore.setText("更改卡片");

									auction_step = STEP_NPC_SELECT_CARD;
									dialog.dismiss();
								}
							});
							
							builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							// 设置玩家的卡片列表
							builder.setEnemyCardList(auction_host_index);
							builder.setWhichCardList(0);
							builder.create().show();
							break;
							
						case STEP_NPC_SELECT_CARD :
							
							tv_auction_dialog.setText("Show Me Your Card~");
							tv_auction_dialog.setVisibility(View.VISIBLE);
							tv_auction_dialog.startAnimation(auction_dialog_anim);
							btn_auction.setText("开始竞价");
							btn_ignore.setVisibility(View.INVISIBLE);
							break;
							
						case STEP_HOST_DEAL :
							
							boolean deal_success = hostSelecCardToDeal(auction_host_index, auction_card_index, true);
							if(deal_success == false){
								tv_auction_list_loading.setText("啊哦，没有适合的卡片");
								tv_auction_list_loading.setVisibility(View.VISIBLE);
								isHostWantToQuit = true;
							}
							btn_auction.setText("下一轮");
							btn_ignore.setVisibility(View.INVISIBLE);
							auction_step = STEP_THIS_DEAL_OVER;
							break;
							
						case STEP_THIS_DEAL_OVER :
							
							if(list != null){
								list.clear();
								adapter.notifyDataSetChanged();
							}
							compareValueList.clear();
							valueCharaMap.clear();
							charaIndexMap.clear();
							IndexPosMap.clear();
							
							auction_card_index = 0;
							setAuctionCard(auction_card_index);
							img_new_logo.setVisibility(View.INVISIBLE);
							tv_card_value.setText(String.valueOf(0));
							
							btn_auction.setText("开始");
							btn_ignore.setText("退出");
							btn_ignore.setVisibility(View.VISIBLE);
							
							auction_step = STEP_HOST_SELECT_CARD;
							
							int rare_card_count = globalTools.getRareCardCount(auction_host_index);
							if(rare_card_count >= 1 && isHostWantToQuit == false){
								tv_auction_dialog.setText("还有别的卡哟");
								tv_auction_dialog.startAnimation(auction_dialog_anim);
							}else{
								btn_auction.setText("换其他人");
								tv_auction_dialog.setText("没有卡片了");
								tv_auction_dialog.startAnimation(auction_dialog_anim);
							}
							break;
							
						case STEP_ALL_DEAL_OVER :
							break;
					}
				}
			}
        });
        
        btn_ignore.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isPlayerAsHost == true){
					
					if(auction_round != 0){
						auction_round = 0;
						auction_card_index = 0;
						setAuctionCard(auction_card_index);
						tv_card_value.setText(String.valueOf(0));
						tv_auction_list_loading.setVisibility(View.INVISIBLE);
						tv_auction_dialog.setVisibility(View.INVISIBLE);
						btn_auction.setText("开拍");
						btn_ignore.setText("退出");
						btn_ignore.setVisibility(View.VISIBLE);
						
						if(list != null){
							list.clear();
							adapter.notifyDataSetChanged();
						}
					}else{
						auction_listener.showTargetFragment(globalTools.SHOW_MAP);
					}
				}else{
					
					switch(auction_step){
					
						case STEP_HOST_SELECT_CARD :
							
							tv_auction_list_loading.setText("等待剩余拍卖完成...");
							tv_auction_list_loading.setVisibility(View.VISIBLE);
							btn_auction.setVisibility(View.INVISIBLE);
							btn_ignore.setVisibility(View.INVISIBLE);
							new Handler().postDelayed(new Runnable(){    
							    public void run() {
							    	FinishRemainingAuction();
							    	if(list != null){
										list.clear();
										adapter.notifyDataSetChanged();
									}
							    	tv_auction_dialog.setVisibility(View.INVISIBLE);
									compareValueList.clear();
									valueCharaMap.clear();
									charaIndexMap.clear();
									IndexPosMap.clear();
							    	tv_auction_list_loading.setText("所有剩余拍卖已完成");
							    	btn_ignore.setText("退出");
							    	btn_ignore.setVisibility(View.VISIBLE);
							    	
							    	auction_step = STEP_ALL_DEAL_OVER;
							    }    
							 }, 100);
							break;
							
						case STEP_PLAYER_SELECT_CARD :
							
							tv_auction_list_loading.setText("等待该轮拍卖完成...");
							btn_auction.setVisibility(View.INVISIBLE);
							btn_ignore.setVisibility(View.INVISIBLE);
							
							new Handler().postDelayed(new Runnable(){    
							    public void run() {
									boolean deal_success = auctionWithoutPlayer(auction_host_index, auction_card_index);
									if(deal_success == false){
										tv_auction_list_loading.setText("啊哦，没有适合的卡片");
										isHostWantToQuit = true;
									}else{
										tv_auction_list_loading.setVisibility(View.INVISIBLE);
									}
									btn_auction.setText("下一轮");
									btn_auction.setVisibility(View.VISIBLE);
									btn_ignore.setVisibility(View.INVISIBLE);

									auction_step = STEP_THIS_DEAL_OVER;
							    }    
							 }, 100);
							break;
							
						case STEP_NPC_SELECT_CARD :
							
							if(list != null){
								list.clear();
								adapter.notifyDataSetChanged();
							}
							compareValueList.clear();
							valueCharaMap.clear();
							charaIndexMap.clear();
							IndexPosMap.clear();
							
							final SelectCardDialog.Builder builder = new SelectCardDialog.Builder(getContext());
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog, int which) {
									int player_card_index = builder.select_card_index;
									
									list = new ArrayList<AuctionListBean>();
									setAdapter(list);
									addAuctionItemToList(0, player_card_index, true);
									
//									int value = globalTools.calcCardValue(player_card_index);
//									compareValueList.add(value);
//									valueCharaMap.put(value, 0);
//									charaIndexMap.put(0, player_card_index);
//									IndexPosMap.put(player_card_index, auction_list.getCount()-1);
									if(globalTools.getThisCardCount(auction_host_index, player_card_index) == 0){
										int value = globalTools.calcCardValue(player_card_index);
										compareValueList.add(value);
										valueCharaMap.put(value, 0);
										charaIndexMap.put(0, player_card_index);
										IndexPosMap.put(player_card_index, auction_list.getCount()-1);
									}
									
									btn_auction.setText("选好了");
									btn_ignore.setText("更改卡片");
									dialog.dismiss();
								}
							});
							
							builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							// 设置玩家的卡片列表
							builder.setEnemyCardList(auction_host_index);
							builder.setWhichCardList(0);
							builder.create().show();
							
							break;
							
						case STEP_HOST_DEAL :
							break;
							
						case STEP_THIS_DEAL_OVER :
							break;
							
						case STEP_ALL_DEAL_OVER :
							auction_listener.showTargetFragment(globalTools.SHOW_MAP);
							break;
					}
				}
			}
        });
        
        img_auction_card.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				final SelectCardDialog.Builder builder = new SelectCardDialog.Builder(getContext());
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						auction_card_index = builder.select_card_index;
						setAuctionCard(auction_card_index);
						
						int card_value = globalTools.calcCardValue(auction_card_index)/1000;
						tv_card_value.setText(String.valueOf(card_value));
						tv_auction_list_loading.setVisibility(View.INVISIBLE);
						tv_auction_dialog.setText("谁有兴趣？");
						btn_auction.setText("开拍");
						btn_ignore.setText("退出");
						
						if(list != null){
							list.clear();
							adapter.notifyDataSetChanged();
						}
						
						dialog.dismiss();
					}
				});
				
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				// 设置玩家的卡片列表
				builder.setWhichCardList(auction_host_index);
				builder.create().show();
			}
        });
        
        initAnim();
        
        auction_host_index = globalTools.getAuctionHostIndex();
        isPlayerAsHost = setAuctionHostAvatar(auction_host_index);
        if(isPlayerAsHost == true){
        	img_auction_card.setClickable(true);
        	auction_step = STEP_NPC_SELECT_CARD;
        }else{
        	img_auction_card.setClickable(false);
        	auction_step = STEP_HOST_SELECT_CARD;
        }
        
        int game_scene = globalTools.getScene();
        setAuctionBackground(game_scene);
        setAuctionCard(0);
        
        ((MainActivity)getActivity()).setLoadingImg(0, false);
	}
	
	private void initAnim(){
		auction_dialog_anim = new ScaleAnimation(
				0f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 1f);
		auction_dialog_anim.setDuration(500);
		auction_dialog_anim.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				if(isPlayerAsHost == true){
					if(auction_step == STEP_NPC_SELECT_CARD){
						if(list != null){
							list.clear();
							adapter.notifyDataSetChanged();
						}					
						tv_auction_list_loading.setText("小伙伴们考虑中");
						tv_auction_list_loading.setVisibility(View.VISIBLE);
						
						new Thread(new Runnable() {
					        public void run() {
					        	auction_list.post(new Runnable() {
					                public void run() {
					                	list = new ArrayList<AuctionListBean>();
										setAdapter(list);
					                	createAuctionList(
					                			auction_host_index,
					                			auction_card_index,
					                			auction_round);
					                }
					            });
					        }
					    }).start();
					}else{
						// TODO
					}
					
				}else{
					if(auction_step == STEP_PLAYER_SELECT_CARD){
						tv_auction_list_loading.setText("有兴趣的话请选择卡片");
						tv_auction_list_loading.setVisibility(View.VISIBLE);
						
					}else if(auction_step == STEP_NPC_SELECT_CARD){
						tv_auction_list_loading.setText("小伙伴们考虑中");
						tv_auction_list_loading.setVisibility(View.VISIBLE);
						
						new Thread(new Runnable() {
					        public void run() {
					        	auction_list.post(new Runnable() {
					                public void run() {
					                	createAuctionList(
					                			auction_host_index,
					                			auction_card_index,
					                			auction_round);
					                }
					            });
					        }
					    }).start();
						
					}else {
						// TODO
					}
				}
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
	}
	
	public boolean setAuctionHostAvatar(int which){
		
		Resources resources = getResources();
		String img_name = "chara_" + String.valueOf(which);
		int img_id = resources.getIdentifier(img_name, "drawable", this.getActivity().getPackageName());
		img_auction_chara.setImageBitmap(
				globalTools.decodeSampledBitmapFromResource(
						getResources(), img_id, 100, 155));

		if(which == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public void setAuctionCard(int index){
		Resources resources = getResources();
		String card_name = "front" + String.valueOf(index);
		int img_id = resources.getIdentifier(card_name, "drawable", this.getActivity().getPackageName());
		img_auction_card.setImageBitmap(
				globalTools.decodeSampledBitmapFromResource(
						getResources(), img_id, 80, 120));
	}
	
	public void setAuctionBackground(int scene){
		if(scene == globalTools.SCENE_EXCHANGE_CLASSROOM){
			layout_auction.setBackgroundResource(R.drawable.map_classroom_morning);
		}else if(scene == globalTools.SCENE_EXCHANGE_SHOP){
			layout_auction.setBackgroundResource(R.drawable.map_shop_dusk);
		}else if(scene == globalTools.SCENE_EXCHANGE_SHOP_SUNDAY){
			layout_auction.setBackgroundResource(R.drawable.map_shop_morning);
		}
	}
	
	private void setAdapter(List<AuctionListBean> list){
		adapter = new AuctionListAdapter(getActivity(), AuctionFragment.this, list);
		auction_list.setAdapter(adapter);
	}
	
	
	// TODO NPC Auction Method
	public void createAuctionList(int chara, int index, int round){

		npcSelectCardToAuction(chara, index, round, true);
		
		if(list.size() > 0){
			tv_auction_list_loading.setVisibility(View.INVISIBLE);
		}else{
			tv_auction_list_loading.setText("杯具，没人感兴趣");
		}
		
		tv_auction_dialog.setVisibility(View.INVISIBLE);
		auction_step = STEP_HOST_DEAL;
	}
	
	public void addAuctionItemToList(int chara_index, int card_index, boolean isRefreshList){
		AuctionListBean listbean = new AuctionListBean();
		
		String chara_name = "";
		if(chara_index == 0){
			chara_name = "【王小明】";
		}else{
			chara_name = "【电脑" + String.valueOf(chara_index) + "】";
		}
		int img_id = globalTools.getCardImgId(card_index);
		String epithet = globalTools.getCardInformation("epithet", card_index);
		String name = globalTools.getCardInformation("name", card_index);
		String card_name = "No." + String.valueOf(card_index)
						+ "\n" + epithet + "\n" + name;
		int value = globalTools.calcCardValue(card_index)/1000;
		
		listbean.setAuctionCharaIndex(chara_index);
		listbean.setAuctionCardIndex(card_index);
		listbean.setAuctionCardImgId(img_id);
		listbean.setAuctionChara(chara_name);
		listbean.setAuctionCardName(card_name);
		listbean.setAuctionValue(value);
		list.add(listbean);
		if(isRefreshList == true){
			adapter.notifyDataSetChanged();
		}
	}
	
	public void npcSelectCardToAuction(int host, int index, int round, boolean isRefreshList){
		int targetValue = globalTools.calcCardValue(index);
		for(int i=1; i<=7; i++){
			if(host != i && globalTools.getThisCardCount(i, index) == 0){
				List<Integer> valueList = new ArrayList<Integer>();
				HashMap<Integer, Integer> valueMap = new HashMap<Integer, Integer>();
				
				for(int j=1; j<=108; j++){
					if(globalTools.getThisCardCount(host, j) == 0 && globalTools.getThisCardCount(i, j) > 1){
						int cardValue = globalTools.calcCardValue(j);
						if(targetValue >= cardValue){
							valueList.add(cardValue);
							valueMap.put(cardValue, j);
						}
					}
				}

				if(valueList.size() > 0 && valueList.size() >= round){
					Collections.sort(valueList);
					int min_value_index = 0;
					if(isPlayerAsHost == true){
						min_value_index = valueMap.get(valueList.get(round-1));
					}else{
						min_value_index = valueMap.get(valueList.get(valueList.size()-round));
					}

					addAuctionItemToList(i, min_value_index, isRefreshList);
					
					int value = globalTools.calcCardValue(min_value_index);
					compareValueList.add(value);
					valueCharaMap.put(value, i);
					charaIndexMap.put(i, min_value_index);
					IndexPosMap.put(min_value_index, auction_list.getCount()-1);
				}else{
					compareValueList.add(0);
				}
			}else{
				compareValueList.add(0);
			}
		}
	}
	
	public int hostSelectCardToAuction(int host){
		List<Integer> valueList = new ArrayList<Integer>();
		HashMap<Integer, Integer> valueMap = new HashMap<Integer, Integer>();
		
		for(int index=1; index<=108; index++){
			
			if(globalTools.getThisCardCount(host, index) > 1){
				boolean isRare = true;
				int exist = 0;
				
				for(int chara=0; chara<=7; chara++){
					if(host != chara){
						if(globalTools.getThisCardCount(chara, index) != 0){
							exist++;
						}
					}
				}
				
				if(exist >= 7){
					isRare = false;
				}
				
				if(isRare == true){
					valueList.add(globalTools.calcCardValue(index));
					valueMap.put(globalTools.calcCardValue(index), index);
				}
			}
		}
		
		if(valueList.size() >= 1){

			Collections.sort(valueList);
			int max_value_index = valueMap.get(
					valueList.get( valueList.size()-1) );
			
			return max_value_index;
		}

		return 0;
	}
	
	private boolean hostSelecCardToDeal(int host_chara, int host_card, boolean isShowToPlayer){
		boolean deal_success = false;
		
		Collections.sort(compareValueList);
		if(valueCharaMap.get(compareValueList.get(compareValueList.size()-1)) != null){
			int deal_chara = valueCharaMap.get(compareValueList.get(compareValueList.size()-1));
			int deal_index = charaIndexMap.get(deal_chara);
			int deal_pos = IndexPosMap.get(deal_index);
			
			//if(globalTools.getThisCardCount(host_chara, deal_index) == 0)
			{
				globalTools.changeCardList(host_chara, host_card, -1);
				globalTools.changeCardList(host_chara, deal_index, 1);
				globalTools.changeCardList(deal_chara, host_card, 1);
				globalTools.changeCardList(deal_chara, deal_index, -1);
		
				if(isShowToPlayer == true){
					list.get(deal_pos).setItemHint(true);
					adapter.notifyDataSetChanged();
					auction_list.smoothScrollToPosition(deal_pos);
					
					tv_auction_dialog.setText("我选择\n No." + String.valueOf(deal_index));
					tv_auction_dialog.setVisibility(View.VISIBLE);
					tv_auction_dialog.startAnimation(auction_dialog_anim);
				}
				
				deal_success = true;
				
			}
//			else{
//				deal_success = false;
//			}
			
		}else{
			deal_success = false;
		}
		
		compareValueList.clear();
		valueCharaMap.clear();
		charaIndexMap.clear();
		IndexPosMap.clear();
		
		return deal_success;
	}
	
	private boolean auctionWithoutPlayer(int chara, int index){
		list = new ArrayList<AuctionListBean>();
		setAdapter(list);
		npcSelectCardToAuction(chara, index, 1, true);
		boolean deal_success = hostSelecCardToDeal(chara, index, true);
		return deal_success;
	}
	
	public void FinishRemainingAuction(){
		compareValueList.clear();
		valueCharaMap.clear();
		charaIndexMap.clear();
		IndexPosMap.clear();
		
		for(int chara=1; chara<=7; chara++){
			
			int rareCount = globalTools.getRareCardCount(chara);
			if(rareCount > 0){
				for(int round=0; round<rareCount; round++){
					int index = hostSelectCardToAuction(chara);
					list = new ArrayList<AuctionListBean>();
					setAdapter(list);
					npcSelectCardToAuction(chara, index, 1, false);
					hostSelecCardToDeal(chara, index, false);
				}
			}
		}
		Toast.makeText(getContext(), "finish all auction!", Toast.LENGTH_LONG).show();
	}
	
	public void playerSelectDeal(int pos){
		int enemy_chara_index = list.get(pos).getAuctionCharaIndex();
		int enemy_card_index = list.get(pos).getAuctionCardIndex();
		Toast.makeText(getContext(),
				"deal: Chara " + String.valueOf(enemy_chara_index)
				+ "  Index: " + String.valueOf(enemy_card_index), Toast.LENGTH_SHORT).show();
		
		globalTools.changeCardList(0, auction_card_index, -1);
		globalTools.changeCardList(0, enemy_card_index, 1);
		globalTools.changeCardList(enemy_chara_index, auction_card_index, 1);
		globalTools.changeCardList(enemy_chara_index, enemy_card_index, -1);
		
		list.get(pos).setItemHint(true);
		adapter.notifyDataSetChanged();
		
		tv_auction_dialog.setText("我选择\n No." + String.valueOf(enemy_card_index));
		tv_auction_dialog.setVisibility(View.VISIBLE);
		tv_auction_dialog.startAnimation(auction_dialog_anim);
		
		btn_auction.setText("下一轮");
		btn_ignore.setVisibility(View.INVISIBLE);
		//isDealOK = true;
		auction_step = STEP_THIS_DEAL_OVER;
	}

}
