package com.social.gamerpoint;

import java.util.ArrayList;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Custom_Feed_Adapter extends BaseAdapter {
	ArrayList<String> feed_text;

	Context context;
	private static LayoutInflater inflater = null;

	public Custom_Feed_Adapter(Context applicationContext,
			ArrayList<String> arraylist_feed_list) {
		// TODO Auto-generated constructor stub

		feed_text = arraylist_feed_list;

		this.context = applicationContext;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return feed_text.size();
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
			vi = inflater.inflate(R.layout.custom_list_show, null);
			holder = new ViewHolder();
			holder.textview_feed = (TextView) vi
					.findViewById(R.id.textView_feed);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		holder.textview_feed.setId(position);

		holder.textview_feed.setText(feed_text.get(position));

		return vi;
	}

	public static class ViewHolder {
		public TextView textview_feed;

	}

}