package com.simoncherry.shuihuccg.fragment;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.simoncherry.shuihuccg.activity.MainActivity;
import com.simoncherry.shuihuccg.adapter.NoteListAdapter;
import com.simoncherry.shuihuccg.bean.NoteListBean;
import com.simoncherry.shuihuccg.tools.GameGlobalTools;
import com.simoncherry.shuihuccg.R;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NoteFragment extends Fragment {
	
	private GameGlobalTools globalTools;
	
	private ViewGroup layout_note;
	private ListView note_list;
	private List<NoteListBean> list;
	private NoteListAdapter adapter;
	private Button btn_clear_note;
	
	private int select_pos;

	public NoteFragment() {
		// Required empty public constructor
	}
	
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
    {  
        public void onClick(DialogInterface dialog, int which)  
        {  
            switch (which)  
            {  
	            case AlertDialog.BUTTON_POSITIVE :
	            	delNoteFromList(select_pos);
	            	dialog.dismiss();
	            	break;
	            case AlertDialog.BUTTON_NEGATIVE :
	            	dialog.dismiss();
	            	break;
            }
        }
    };
	
	@Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        globalTools = (GameGlobalTools) getActivity().getApplication();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_note, container, false);
	}
	
	@Override
	public void onResume() {  
        super.onResume(); 
        
        layout_note = (ViewGroup) getActivity().findViewById(R.id.layout_note);
        note_list = (ListView) getActivity().findViewById(R.id.note_list);
        btn_clear_note = (Button) getActivity().findViewById(R.id.btn_clear_note);
        
        btn_clear_note.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				clearAllNote();
				list.clear();
				adapter.notifyDataSetChanged();
			}
        });
        
        note_list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				select_pos = position;
				int count = globalTools.getNoteCount();
				
				AlertDialog isExit = new AlertDialog.Builder(getContext()).create();  
	            isExit.setTitle(getString(R.string.sys_general_title));  
	            isExit.setMessage(getString(R.string.note_del_warning));
	            isExit.setButton(DialogInterface.BUTTON_POSITIVE,
	            		getString(R.string.btn_general_positive), listener);
	            isExit.setButton(DialogInterface.BUTTON_NEGATIVE,
	            		getString(R.string.btn_general_negative), listener);
	            isExit.show();
			}
        });
        
        initList();
        int game_scene = globalTools.getScene();
        setNoteBackground(game_scene);
        
        ((MainActivity)getActivity()).setLoadingImg(0, false);
	}
	
	private void setAdapter(List<NoteListBean> list){
		adapter = new NoteListAdapter(getActivity(), list);
		note_list.setAdapter(adapter);
	}
	
	private void initList(){
		list = new ArrayList<NoteListBean>();
		setAdapter(list);
		
		int note_count = globalTools.getNoteCount();
		
		if(note_count > 0){
			String search_string = null;
			for(int i=1; i<=note_count; i++){
				search_string = "note" + String.valueOf(i);
				String code_text = globalTools.getNoteText(search_string);
				addNoteToList(decodeNoteFromFile(code_text));
			}
		}
	}
	
	public void addNoteToList(String note){
		NoteListBean listbean = new NoteListBean();
		listbean.setText(note);
		list.add(listbean);
		adapter.notifyDataSetChanged();
	}
	
	public void delNoteFromList(int pos){
		int note_count = globalTools.getNoteCount();
		if(note_count > 1){

			for(int i=pos; i<note_count; i++){
				String key1 = "note" + String.valueOf(i+1);
				String key2 = "note" + String.valueOf(i+2);
				String text = globalTools.getNoteText(key2);
				globalTools.setNoteText(key1, text);
			}
		}
		
		note_count--;
		globalTools.setNoteCount(note_count);
		
		list.remove(pos);
		adapter.notifyDataSetChanged();
	}
	
	private String decodeNoteFromFile(String code){
		int sign1 = 0;
		int sign2 = 0;
		int length = 0;
		String type = "";
		String who = "";
		String index = "";
		String decode = "";
		
		length = code.length();
		sign1 = code.indexOf("|");
		sign2 = code.indexOf("|", sign1+1);
		type = code.substring(0, sign1);
		who = code.substring(sign1+1, sign2);
		index = code.substring(sign2+1, length);
		
		if(type.equals("new")){
			decode = 
//					"【电脑" + who + "】 " + "抽到了\n  No." + index + " 【"
//					+ globalTools.getCardInformation("epithet", Integer.parseInt(index))
//					+ "·" + globalTools.getCardInformation("name", Integer.parseInt(index))
//					+ "】";
					"【" + globalTools.getCharaName(Integer.parseInt(who)) + "】 " 
					+ getString(R.string.note_new_card2) + index + " 【"
					+ globalTools.getCardInformation("epithet", Integer.parseInt(index))
					+ "·" + globalTools.getCardInformation("name", Integer.parseInt(index))
					+ "】";				
		}else{
			decode = type + " " + who + " " + index;
		}
		
		return decode;
	}
	
	private void clearAllNote(){
		SharedPreferences playerSharedPreferences = getActivity().getSharedPreferences(
				"global", getActivity().MODE_PRIVATE);
		Editor editor = playerSharedPreferences.edit();
		editor.putInt("note_count", 0);
		editor.commit();
	}
	
	private void setNoteBackground(int scene){
		switch(scene){
		case 0 :
		case 20 :
		case 22 :
			layout_note.setBackgroundResource(R.drawable.map_bedroom);
			break;
		case 1 :
		case 14 :
		case 21 :
			layout_note.setBackgroundResource(R.drawable.map_home);
			break;
		case 2 :
			layout_note.setBackgroundResource(R.drawable.map_schoolgate);
			break;
		case 3 :
		case 8 :
		case 9 :
		case 10 :
			layout_note.setBackgroundResource(R.drawable.map_classroom_morning);
			break;
		case 4 :
			layout_note.setBackgroundResource(R.drawable.map_classroom_dusk);
			break;
		case 5 :
		case 23 :
		case 24 :
		case 25 :
			layout_note.setBackgroundResource(R.drawable.map_shop_morning);
			break;
		case 6 :
		case 11 :
		case 12 :
		case 13 :
			layout_note.setBackgroundResource(R.drawable.map_shop_dusk);
			break;
		case 7 :
			layout_note.setBackgroundResource(R.drawable.loading_book);
			break;
		case 15 :
		case 17 :
		case 18 :
			layout_note.setBackgroundResource(R.drawable.map_cram_school_morning);
			break;
		case 16 :
			layout_note.setBackgroundResource(R.drawable.map_cram_school_dusk);
			break;	
		case 19 :
			layout_note.setBackgroundResource(R.drawable.map_another_shop);
			break;
		case 26 :
			layout_note.setBackgroundResource(R.drawable.map_alley);
			break;
		default :
			layout_note.setBackgroundResource(R.drawable.map_classroom_morning);
			break;
	}
	}

}
