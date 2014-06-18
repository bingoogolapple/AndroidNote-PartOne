package com.bingo.mp3player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bingo.bean.Mp3Info;
import com.bingo.utils.FileUtils;

public class LocalMp3ListActivity extends ListActivity {
	private List<Mp3Info> mp3Infos = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_mp3_list);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	FileUtils fileUtils = new FileUtils();
    	mp3Infos = fileUtils.getMp3Files("mp3");
    	List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
    	for(Mp3Info mp3Info : mp3Infos) {
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("mp3_name", mp3Info.getMp3Name());
    		map.put("mp3_size", mp3Info.getMp3Size());
    		list.add(map);
    	}
    	SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3_info_item, new String[]{"mp3_name","mp3_size"}, new int[]{R.id.mp3_name, R.id.mp3_size});
    	setListAdapter(simpleAdapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	if(mp3Infos != null) {
    		Mp3Info mp3Info = mp3Infos.get(position);
    		Intent intent = new Intent();
    		intent.putExtra("mp3Info", mp3Info);
    		intent.setClass(this, PlayerActivity.class);
    		startActivity(intent);
    	}
    }
}
