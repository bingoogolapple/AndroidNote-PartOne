package cn.bingoogol.dmc.ui.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bingoogol.dmc.R;
import cn.bingoogol.dmc.engine.ItemHelper;
import cn.bingoogol.dmc.engine.MultiPointController;
import cn.bingoogol.dmc.model.DlnaItem;
import cn.bingoogol.dmc.model.DlnaItemChild;
import cn.bingoogol.dmc.model.MediaRenderDevices;
import cn.bingoogol.dmc.model.MediaServerDevices;
import cn.bingoogol.util.DlnaUtils;
import cn.bingoogol.util.Logger;

public class FileListActivity extends BaseActivity {
	private static final String TAG = FileListActivity.class.getSimpleName();
	private ExpandableListView mFileListElv;
	private FileListAdapter mFileListAdapter;
	private List<DlnaItem> mItems;
	private MultiPointController mMultiPointController;

	public BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (ItemHelper.NEW_DEVICES_FOUND.equals(intent.getAction())) {
				if (mItems != null && mItems.size() != 0)
					return;
				if (MediaServerDevices.getInstance().getSelectedDevice() != null) {
					mItems = ItemHelper.getItems(0);
					mFileListAdapter.notifyDataSetChanged();
				}
			}
		}
	};

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_filelist);
		registerReceiver(mReceiver, new IntentFilter(ItemHelper.NEW_DEVICES_FOUND));
		mFileListElv = (ExpandableListView) findViewById(R.id.elv_filelist_filelist);
		mFileListAdapter = new FileListAdapter();
		mFileListElv.setAdapter(mFileListAdapter);
	}

	@Override
	protected void setListener() {
		findViewById(R.id.btn_filelist_pause).setOnClickListener(this);
		findViewById(R.id.btn_filelist_stop).setOnClickListener(this);
		findViewById(R.id.btn_filelist_addvoice).setOnClickListener(this);
		findViewById(R.id.btn_filelist_subvoice).setOnClickListener(this);
		findViewById(R.id.btn_filelist_mute).setOnClickListener(this);
		findViewById(R.id.btn_filelist_mute_cancel).setOnClickListener(this);
		findViewById(R.id.btn_filelist_seek).setOnClickListener(this);
		findViewById(R.id.btn_filelist_goon).setOnClickListener(this);
		mFileListElv.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
				if (mItems.get(groupPosition).childItems == null) {
					String stringId = mItems.get(groupPosition).id;
					new AsyncTask<String, String, List<DlnaItemChild>>() {

						@Override
						protected List<DlnaItemChild> doInBackground(String... params) {
							return ItemHelper.getItemChild(params[0]);
						}

						@Override
						protected void onPostExecute(List<DlnaItemChild> result) {
							if (result.size() > 0) {
								mItems.get(groupPosition).childItems = result;
								mFileListAdapter.notifyDataSetChanged();
							}
						}

					}.execute(stringId);

				}
				return false;
			}
		});

		mFileListElv.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				DlnaItemChild infos = mItems.get(groupPosition).childItems.get(childPosition);
				String res = infos.res;
				String ip = DlnaUtils.getIP();
				res = res.replaceAll("http://[0-9.]+:", "http://" + ip + ":");
				MultiPointController.metaData = getMetaData(infos, res);
				new Thread(new MyRun(res)).start();
				return false;
			}
		});
	}

	public String getMetaData(DlnaItemChild infos, String res) {
		StringBuffer sb = new StringBuffer();
		sb.append("<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" ");
		sb.append("xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" ");
		sb.append("xmlns:dc=\"http://purl.org/dc/elements/1.1/\" ");
		sb.append("xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" ");
		sb.append("xmlns:sec=\"http://www.sec.co.kr/\">");
		sb.append("<item id=\"" + infos.id + "\" parentID=\"" + infos.parentID + "\" restricted=\"1\">");
		sb.append("<upnp:class>" + infos.objectClass + "</upnp:class>");
		sb.append("<dc:title>" + infos.title + "</dc:title>");
		sb.append("<dc:creator>Unknown</dc:creator>");
		sb.append("<upnp:artist>Unknown</upnp:artist>");
		sb.append("<upnp:albumArtURI>" + res + "</upnp:albumArtURI>");
		sb.append("<upnp:album>Unknown</upnp:album>");
		sb.append("<res size=\"" + infos.size + "\" protocolInfo=\"" + infos.protocolInfo + "\">" + res + "</res>");
		sb.append("</item>" + "</DIDL-Lite>");
		return sb.toString();
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		mMultiPointController = new MultiPointController();
		if (MediaServerDevices.getInstance().getSelectedDevice() != null) {
			new AsyncTask<Integer, Integer, List<DlnaItem>>() {

				@Override
				protected List<DlnaItem> doInBackground(Integer... params) {
					return ItemHelper.getItems(0);
				}

				@Override
				protected void onPostExecute(List<DlnaItem> result) {
					mItems = result;
					mFileListAdapter.notifyDataSetChanged();
				}

			}.execute(0);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_filelist_pause:
			new Thread(new Runnable() {

				@Override
				public void run() {
					mMultiPointController.pause(MediaRenderDevices.getInstance().getSelectedDevice());
				}
			}).start();
			break;
		case R.id.btn_filelist_stop:
			new Thread(new Runnable() {

				@Override
				public void run() {
					mMultiPointController.stop(MediaRenderDevices.getInstance().getSelectedDevice());
				}
			}).start();
			break;
		case R.id.btn_filelist_addvoice:
			new Thread(new Runnable() {

				@Override
				public void run() {
					int currentVoice = mMultiPointController.getVoice(MediaRenderDevices.getInstance().getSelectedDevice());
					Logger.i(TAG, "currentVoice:" + currentVoice);
					int minVoice = mMultiPointController.getMinVolumeValue(MediaRenderDevices.getInstance().getSelectedDevice());
					Logger.i(TAG, "minVoice:" + minVoice);
					int maxVoice = mMultiPointController.getMaxVolumeValue(MediaRenderDevices.getInstance().getSelectedDevice());
					Logger.i(TAG, "maxVoice:" + maxVoice);
					mMultiPointController.setVoice(MediaRenderDevices.getInstance().getSelectedDevice(), currentVoice + 5);
				}
			}).start();
			break;
		case R.id.btn_filelist_subvoice:
			new Thread(new Runnable() {

				@Override
				public void run() {
					int voice = mMultiPointController.getVoice(MediaRenderDevices.getInstance().getSelectedDevice());
					mMultiPointController.setVoice(MediaRenderDevices.getInstance().getSelectedDevice(), voice - 5);
				}
			}).start();
			break;
		case R.id.btn_filelist_mute:
			new Thread(new Runnable() {

				@Override
				public void run() {
					mMultiPointController.setMute(MediaRenderDevices.getInstance().getSelectedDevice(), "1");
				}
			}).start();
			break;
		case R.id.btn_filelist_mute_cancel:
			new Thread(new Runnable() {

				@Override
				public void run() {
					mMultiPointController.setMute(MediaRenderDevices.getInstance().getSelectedDevice(), "0");
				}
			}).start();
			break;
		case R.id.btn_filelist_seek:
			new Thread(new Runnable() {

				@Override
				public void run() {
					String positionInfo = mMultiPointController.getPositionInfo(MediaRenderDevices.getInstance().getSelectedDevice());
					Logger.i(TAG, "positionInfo:" + positionInfo);
					String transportState = mMultiPointController.getTransportState(MediaRenderDevices.getInstance().getSelectedDevice());
					Logger.i(TAG, "transportState:" + transportState);
					mMultiPointController.seek(MediaRenderDevices.getInstance().getSelectedDevice(), "00:00:10");
				}
			}).start();
			break;
		case R.id.btn_filelist_goon:
			new Thread(new Runnable() {

				@Override
				public void run() {
					Logger.i(TAG, "goon");
					mMultiPointController.goon(MediaRenderDevices.getInstance().getSelectedDevice(), "00:00:08");
				}
			}).start();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	public class MyRun implements Runnable {
		String res;

		public MyRun(String res) {
			this.res = res;
		}

		@Override
		public void run() {
			mMultiPointController.play(MediaRenderDevices.getInstance().getSelectedDevice(), res);
		}
	}

	public class FileListAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return mItems == null ? 0 : mItems.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (mItems == null || mItems.get(groupPosition).childItems == null)
				return 0;
			return mItems.get(groupPosition).childItems.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(FileListActivity.this, R.layout.item_file_group, null);
			}
			TextView groupNameTv = (TextView) convertView.findViewById(R.id.tv_item_file_group_name);
			groupNameTv.setText(mItems.get(groupPosition).title);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(FileListActivity.this, R.layout.item_file_child, null);
				viewHolder.chileNameTv = (TextView) convertView.findViewById(R.id.tv_item_file_child_name);
				viewHolder.childIconIv = (ImageView) convertView.findViewById(R.id.iv_item_file_child_icon);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.chileNameTv.setText(mItems.get(groupPosition).childItems.get(childPosition).title);

			// String path = getPath(mItems.get(groupPosition).childItems.get(childPosition).title);
			// String objectClass = mItems.get(groupPosition).childItems.get(childPosition).objectClass;
			// if (objectClass.equals("object.item.imageItem.photo")) {
			// viewHolder.childIconIv.setImageBitmap(ThumbnailUtil.getImageThumbnail(path, 50, 50));
			// } else if (objectClass.equals("object.item.videoItem")) {
			// viewHolder.childIconIv.setImageBitmap(ThumbnailUtil.getVideoThumbnail(path, 50, 50, Images.Thumbnails.MICRO_KIND));
			// } else {
			// viewHolder.childIconIv.setImageResource(R.drawable.ic_launcher);
			// }
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	private static class ViewHolder {
		public TextView chileNameTv;
		public ImageView childIconIv;
	}
}