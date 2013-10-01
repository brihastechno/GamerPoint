package com.custom.classes;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.social.gamerpoint.Activity_List_followed;
import com.social.gamerpoint.R;

public class LazyAdapter extends BaseAdapter {
	private Activity_List_followed activity;
	private ArrayList<Friend> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public LazyAdapter(Activity_List_followed a, ArrayList<Friend> friends) {
		activity = a;
		data = friends;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
		Log.d("sss", "" + data.size());

	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.custom_layout_list, null);
			holder = new ViewHolder();
			holder.name = (TextView) vi.findViewById(R.id.textView_name);
			holder.image = (ImageView) vi
					.findViewById(R.id.imageView_profile_pic);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		Friend friend = data.get(position);
		holder.name.setId(position);
		holder.image.setId(position);

		holder.name.setText(friend.fullname);
		imageLoader.DisplayImage(friend.pic_url, holder.image);

		return vi;
	}

	public static class ViewHolder {
		public TextView name;

		public ImageView image;

	}
}