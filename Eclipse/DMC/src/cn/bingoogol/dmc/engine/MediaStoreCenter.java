package cn.bingoogol.dmc.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogol.dmc.App;
import cn.bingoogol.dmc.engine.MediaScannerCenter.IMediaScanListener;
import cn.bingoogol.dmc.engine.MediaScannerCenter.MediaType;
import cn.bingoogol.util.FileHelper;

public class MediaStoreCenter implements IMediaScanListener {
	private static final String TAG = MediaStoreCenter.class.getSimpleName();
	private static MediaStoreCenter mInstance = new MediaStoreCenter();
	private String mShareRootPath = "";
	private String mImageFolderPath = "";
	private String mVideoFolderPath = "";
	private String mAudioFolderPath = "";

	public Map<String, String> mMediaStoreMap = new HashMap<String, String>();

	private MediaStoreCenter() {
		mShareRootPath = App.getInstance().getFilesDir().getAbsolutePath() + File.separator + "rootFolder";
		mImageFolderPath = mShareRootPath + File.separator + "Image";
		mVideoFolderPath = mShareRootPath + File.separator + "Video";
		mAudioFolderPath = mShareRootPath + File.separator + "Audio";
	}

	public static MediaStoreCenter getInstance() {
		return mInstance;
	}

	public String getRootDir() {
		return mShareRootPath;
	}

	public void startScanMedia() {
		FileHelper.deleteDirectory(mShareRootPath);
		FileHelper.createDirectory(mShareRootPath);
		FileHelper.createDirectory(mImageFolderPath);
		FileHelper.createDirectory(mVideoFolderPath);
		FileHelper.createDirectory(mAudioFolderPath);
		MediaScannerCenter.getInstance().startScanThread(this);
	}

	public void stopScanMedia() {
		mMediaStoreMap.clear();
		FileHelper.deleteDirectory(mShareRootPath);

		MediaScannerCenter.getInstance().stopScanThread();
		while (!MediaScannerCenter.getInstance().isThreadOver()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mediaScan(MediaType mediaType, String mediaPath, String mediaName) {
		switch (mediaType) {
		case AUDIO_TYPE:
			mapAudio(mediaPath, mediaName);
			break;
		case VIDEO_TYPE:
			mapVideo(mediaPath, mediaName);
			break;
		case IMAGE_TYPE:
			mapImage(mediaPath, mediaName);
			break;
		default:
			break;
		}

	}

	private void mapAudio(String mediaPath, String mediaName) {
		String webPath = mAudioFolderPath + File.separator + mediaName;
		mMediaStoreMap.put(mediaPath, webPath);
		softLinkMode(mediaPath, webPath);
	}

	private void mapVideo(String mediaPath, String mediaName) {
		String webPath = mVideoFolderPath + File.separator + mediaName;
		mMediaStoreMap.put(mediaPath, webPath);
		softLinkMode(mediaPath, webPath);
	}

	private void mapImage(String mediaPath, String mediaName) {
		String webPath = mImageFolderPath + File.separator + mediaName;
		mMediaStoreMap.put(mediaPath, webPath);
		softLinkMode(mediaPath, webPath);
	}

	private boolean softLinkMode(String localPath, String webPath) {
		try {
			String cmd = "ln -s " + localPath + " " + webPath;
			Process p = Runtime.getRuntime().exec(cmd);
			releaseProcessStream(p);
			return p.waitFor() == 0 ? true : false;
		} catch (Exception e) {
			return false;
		}
	}

	private void releaseProcessStream(Process process) throws IOException {
		InputStream stderr = process.getErrorStream();
		InputStreamReader isr = new InputStreamReader(stderr);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			// Logger.e(TAG, line);
		}
	}
}
