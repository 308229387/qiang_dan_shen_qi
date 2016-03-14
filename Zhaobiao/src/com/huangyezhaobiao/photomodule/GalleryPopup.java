package com.huangyezhaobiao.photomodule;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.huangyezhaobiao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class GalleryPopup extends PopupWindow {
	private View convertViews;
	private Context context;
	private ListView id_list_dir;
	private MyAdapter adapter;
	private List<GalleryDirInfo> daInfos = new ArrayList<GalleryDirInfo>();
	private OnPopupItemClickListener listener;

	public void setOnPopupItemClickListener(OnPopupItemClickListener listener) {
		this.listener = listener;
	}

	public GalleryPopup(Context context, List<GalleryDirInfo> daInfos) {
		this.context = context;
		convertViews = LayoutInflater.from(context).inflate(R.layout.list_dir,
				null);
		this.setContentView(convertViews);
		// 设置宽高
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);

		setBackgroundDrawable(new BitmapDrawable());
		this.daInfos = daInfos;
		id_list_dir = (ListView) convertViews.findViewById(R.id.id_list_dir);
		id_list_dir.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (listener != null)
					listener.OnPopupItemClick(position);
			}
		});
		adapter = new MyAdapter();
		id_list_dir.setAdapter(adapter);
		setFocusable(true);
		setOutsideTouchable(true);
		setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismiss();
					return true;
				}
				return false;
			}
		});
		update();

	}

	public void setDatas(List<GalleryDirInfo> daInfos) {
		this.daInfos = daInfos;
		id_list_dir = (ListView) convertViews.findViewById(R.id.id_list_dir);
		adapter = new MyAdapter();
		id_list_dir.setAdapter(adapter);
	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			Log.d("popup", "size:" + daInfos.size());
			return daInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return daInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("popups", "pos:" + position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_dir_item, parent, false);
				holder.id_dir_item_image = (ImageView) convertView
						.findViewById(R.id.id_dir_item_image);
				holder.id_dir_item_count = (TextView) convertView
						.findViewById(R.id.id_dir_item_count);
				holder.id_dir_item_name = (TextView) convertView
						.findViewById(R.id.id_dir_item_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			GalleryDirInfo item = daInfos.get(position);
			holder.id_dir_item_name.setText(item.getName());
			if (TextUtils.isEmpty(item.getName()))
				holder.id_dir_item_name.setText("所有照片");
			holder.id_dir_item_count.setText("" + item.getCount());
			ImageLoader.getInstance().displayImage("file://"+item.getFirst_pic_path(),
					holder.id_dir_item_image);
			Log.e("shenjjj", "firstPath:" + item.getFirst_pic_path());
			return convertView;
		}

		class ViewHolder {
			public ImageView id_dir_item_image;
			public TextView id_dir_item_name;
			private TextView id_dir_item_count;
		}
	}

	public interface OnPopupItemClickListener {
		public void OnPopupItemClick(int pos);
	}
}
