package com.bingo.mp3player.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.bingo.bean.AppConstant;
import com.bingo.bean.Mp3Info;
import com.bingo.utils.HttpDownloader;

public class DownloadService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
		DownloadThread downloadThread = new DownloadThread(mp3Info);
		new Thread(downloadThread).start();
		return super.onStartCommand(intent, flags, startId);
	}
	class DownloadThread implements Runnable {
		private Mp3Info mp3Info = null;
		public DownloadThread(Mp3Info mp3Info) {
			this.mp3Info = mp3Info;
		}
		public void run() {
			String mp3Url = AppConstant.URL.BASE_URL + "file/" + mp3Info.getMp3Name();
			HttpDownloader httpDownloader = new HttpDownloader();
			int result = httpDownloader.downFile(mp3Url, "mp3", mp3Info.getMp3Name());
			String resultMessage = null;
			if(result == -1) {
				resultMessage = "下载失败";
			} else if(result == 1) {
				resultMessage = "文件已经存在，不需要重复下载";
			} else if(result == 0) {
				resultMessage = "文件下载成功";
			}
			System.out.println("操作提示：" + resultMessage);
		}
		
	}
}
