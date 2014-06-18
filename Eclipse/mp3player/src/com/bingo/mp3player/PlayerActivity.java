package com.bingo.mp3player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.bingo.bean.AppConstant;
import com.bingo.bean.Mp3Info;
import com.bingo.mp3player.service.PlayerService;

public class PlayerActivity extends Activity {
	private Button bPlay = null;
	private Button bPause = null;
	private Button bStop = null;
	private Mp3Info mp3Info = null;
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        bPlay = (Button) this.findViewById(R.id.bPlay);
        bPause = (Button) this.findViewById(R.id.bPause);
        bStop = (Button) this.findViewById(R.id.bStop);
    }
    public void mediaplay(View v) {
    	Intent intent = new Intent();
    	intent.setClass(PlayerActivity.this, PlayerService.class);
    	intent.putExtra("mp3Info", mp3Info);
    	if(v.getId() == bPlay.getId()) {
    		intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
    	} else if(v.getId() == bPause.getId()) {
    		intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
    	} else if(v.getId() == bStop.getId()) {
    		intent.putExtra("MSG", AppConstant.PlayerMsg.STOP_MSG);
    	}
    	startService(intent);
    }
}