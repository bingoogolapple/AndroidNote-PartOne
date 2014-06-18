package com.bingo.mp3player.service;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.bingo.bean.AppConstant;
import com.bingo.bean.Mp3Info;

public class PlayerService extends Service {
	private boolean isPlaying = false;
	private boolean isPause = false;
	private boolean isReleased = false;
	private MediaPlayer mediaPlayer = null;
	private long begin = 0;
	private long pauseTimeMills = 0;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
		int MSG = intent.getIntExtra("MSG", 0);
		if(mp3Info != null) {
			if(MSG == AppConstant.PlayerMsg.PLAY_MSG) {
				play(mp3Info);
			}
		} else {
			if(MSG == AppConstant.PlayerMsg.PAUSE_MSG) {
				pause();
			} else if(MSG == AppConstant.PlayerMsg.STOP_MSG) {
				stop();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private void play(Mp3Info mp3Info) {
		System.out.println("播放");
		if(!isPlaying) {
			try {
				String path = getMp3Path(mp3Info);
				System.out.println("path:" + path);
				//Uri前面必须有一个协议
				if(mediaPlayer == null) {
					mediaPlayer = MediaPlayer.create(this, Uri.parse("file://" + path));
				}
//				mediaPlayer.reset();//把各项参数回复到初始状态
				mediaPlayer.setLooping(false);
//				mediaPlayer.prepare();
//				mediaPlayer.setOnPreparedListener(new PrepareListener(1));
				mediaPlayer.start();
				isPlaying = true;
				isReleased = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private final class PrepareListener implements OnPreparedListener {

		private int position;
		public PrepareListener(int position) {
			this.position = position;
		}

		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();//开始播放
			if(position>0) {
				mediaPlayer.seekTo(position);
			}
		}
		
	}
	private void pause() {
		if(isPlaying) {
			mediaPlayer.pause();
			pauseTimeMills = System.currentTimeMillis();
		} else {
			mediaPlayer.start();
			begin = System.currentTimeMillis() - pauseTimeMills + begin;
		}
		isPlaying = isPlaying ? false : true;
	}
	private void stop() {
		if(mediaPlayer != null) {
			if(isPlaying) {
				if(!isReleased) {
					mediaPlayer.stop();
					mediaPlayer.release();
					isReleased = true;
				}
				isPlaying = false;
			}
		}
	}
	private String getMp3Path(Mp3Info mp3Info) {
    	String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    	String path = SDCardRoot + File.separator + "mp3" + File.separator + mp3Info.getMp3Name();
    	return path;
    }
}
