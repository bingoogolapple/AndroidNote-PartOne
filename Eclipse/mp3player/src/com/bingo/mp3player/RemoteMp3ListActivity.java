package com.bingo.mp3player;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bingo.bean.AppConstant;
import com.bingo.bean.Mp3Info;
import com.bingo.mp3player.service.DownloadService;
import com.bingo.utils.HttpDownloader;
import com.bingo.xml.Mp3ListContentHandler;

public class RemoteMp3ListActivity extends ListActivity {
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	private List<Mp3Info> mp3Infos = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_mp3_list);
        updateListView();
    }
    /**
     * 在用户点击MENU按钮之后，会调用该方法，我们可以在这个方法当中加入自己的按钮控件
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, UPDATE, 1, R.string.list_update);
    	menu.add(0, ABOUT, 2, R.string.list_about);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	System.out.println("itemId---->" + item.getItemId());
    	if(item.getItemId() == UPDATE) {
    		//用户点击了更新列表按钮
    		updateListView();
    	} else if(item.getItemId() == ABOUT) {
    		//用户点击了关于按钮
    	}
    	return super.onOptionsItemSelected(item);
    }
    private void updateListView() {
    	//下载包含所有mp3基本信息的xml文件
		String xmlStr = downloadXML(AppConstant.URL.BASE_URL + "resources.xml");
		mp3Infos = parse(xmlStr);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3Infos);
		setListAdapter(simpleAdapter);
    }
    private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos) {
    	List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
    	for(Mp3Info mp3Info : mp3Infos) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			list.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3_info_item, new String[]{"mp3_name","mp3_size"}, new int[]{R.id.mp3_name, R.id.mp3_size});
		return simpleAdapter;
    }
    private String downloadXML(String urlStr) {
    	HttpDownloader httpDownloader = new HttpDownloader();
    	String result = httpDownloader.download(urlStr);
    	return result;
    }
    private List<Mp3Info> parse(String xmlStr) {
    	SAXParserFactory factory = SAXParserFactory.newInstance();
    	List<Mp3Info> list = new ArrayList<Mp3Info>();
    	Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(list);
    	try {
			XMLReader xmlReader = factory.newSAXParser().getXMLReader();
			xmlReader.setContentHandler(mp3ListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return list;
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	//根据用户点击列表中的位置来得到响应的Mp3Info对象
    	Mp3Info mp3Info = mp3Infos.get(position);
    	//生成Intent对象
    	Intent intent = new Intent();
    	//将Mp3Info对象存入到Intent对象中
    	intent.putExtra("mp3Info", mp3Info);
    	intent.setClass(this, DownloadService.class);
    	//启动Service
    	startService(intent);
    }
}
