package cn.bingoogol.dmc.engine;

import android.database.Cursor;
import android.provider.MediaStore;
import cn.bingoogol.dmc.App;

public class MediaScannerCenter {
	private static MediaScannerCenter mInstance = new MediaScannerCenter();
	private String AUDIO_PATH = MediaStore.Audio.AudioColumns.DATA;
	private String AUDIO_DISPLAYHNAME = MediaStore.Audio.AudioColumns.DISPLAY_NAME;
	private String AUDIO_COLUMN_STRS[] = { AUDIO_PATH, AUDIO_DISPLAYHNAME };

	private String VIDEO_PATH = MediaStore.Video.VideoColumns.DATA;
	private String VIDEO_DISPLAYHNAME = MediaStore.Video.VideoColumns.DISPLAY_NAME;
	private String VIDEO_COLUMN_STRS[] = { VIDEO_PATH, VIDEO_DISPLAYHNAME };

	private String IMAGE_PATH = MediaStore.Images.ImageColumns.DATA;
	private String IMAGE_DISPLAYHNAME = MediaStore.Images.ImageColumns.DISPLAY_NAME;
	private String IMAGE_COLUMN_STRS[] = { IMAGE_PATH, IMAGE_DISPLAYHNAME };

	private ScanMediaThread mMediaThread;

	private MediaScannerCenter() {
	}

	public static MediaScannerCenter getInstance() {
		return mInstance;
	}

	public synchronized boolean startScanThread(IMediaScanListener listener) {
		if (mMediaThread == null || !mMediaThread.isAlive()) {
			mMediaThread = new ScanMediaThread(listener);
			mMediaThread.start();
		}
		return true;
	}

	public synchronized void stopScanThread() {
		if (mMediaThread != null) {
			if (mMediaThread.isAlive()) {
				mMediaThread.exit();
			}
			mMediaThread = null;
		}
	}

	public synchronized boolean isThreadOver() {
		if (mMediaThread != null && mMediaThread.isAlive()) {
			return false;
		}
		return true;
	}

	private boolean scanMusic(IMediaScanListener listener, ICancelScanMedia cancelObser) throws Exception {
		Cursor cursor = App.getInstance().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_COLUMN_STRS, null, null, AUDIO_DISPLAYHNAME);
		if (cursor != null) {
			if (cursor.getCount() != 0) {
				int _name_index = cursor.getColumnIndexOrThrow(AUDIO_DISPLAYHNAME);
				int _dir_index = cursor.getColumnIndexOrThrow(AUDIO_PATH);
				if (cursor.moveToFirst()) {
					do {
						if (cancelObser.ifCancel()) {
							return false;
						}
						String srcpath = cursor.getString(_dir_index);
						String name = cursor.getString(_name_index);
						listener.mediaScan(MediaType.AUDIO_TYPE, srcpath, name);

					} while (cursor.moveToNext());
				}
			}
			cursor.close();
			return true;
		}
		return false;
	}

	private boolean scanVideo(IMediaScanListener listener, ICancelScanMedia cancelObser) throws Exception {
		Cursor cursor = App.getInstance().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_COLUMN_STRS, null, null, VIDEO_DISPLAYHNAME);
		if (cursor != null) {
			if (cursor.getCount() != 0) {
				int _name_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
				int _dir_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
				if (cursor.moveToFirst()) {
					do {
						if (cancelObser.ifCancel()) {
							return false;
						}
						String srcpath = cursor.getString(_dir_index);
						String name = cursor.getString(_name_index);
						listener.mediaScan(MediaType.VIDEO_TYPE, srcpath, name);
					} while (cursor.moveToNext());
				}
			}
			cursor.close();
			return true;
		}
		return false;
	}

	private boolean scanImage(IMediaScanListener listener, ICancelScanMedia cancelObser) throws Exception {
		Cursor cursor = App.getInstance().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_COLUMN_STRS, null, null, IMAGE_DISPLAYHNAME);
		if (cursor != null) {
			if (cursor.getCount() != 0) {
				int _name_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
				int _dir_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.moveToFirst()) {
					do {
						if (cancelObser.ifCancel()) {
							return false;
						}
						String srcpath = cursor.getString(_dir_index);
						String name = cursor.getString(_name_index);
						listener.mediaScan(MediaType.IMAGE_TYPE, srcpath, name);
					} while (cursor.moveToNext());
				}
			}
			cursor.close();
			return true;
		}
		return false;
	}

	public class ScanMediaThread extends Thread implements ICancelScanMedia {
		IMediaScanListener mListener;
		boolean exitFlag = false;

		public ScanMediaThread(IMediaScanListener listener) {
			mListener = listener;
		}

		public void exit() {
			exitFlag = true;
		}

		@Override
		public void run() {
			try {
				scanMusic(mListener, this);
				scanVideo(mListener, this);
				scanImage(mListener, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.run();
		}

		@Override
		public boolean ifCancel() {
			return exitFlag;
		}
	}

	public interface ICancelScanMedia {
		public boolean ifCancel();
	}

	public interface IMediaScanListener {
		public void mediaScan(MediaType mediaType, String mediaPath, String mediaName);
	}

	public enum MediaType {
		IMAGE_TYPE, VIDEO_TYPE, AUDIO_TYPE
	}

}
