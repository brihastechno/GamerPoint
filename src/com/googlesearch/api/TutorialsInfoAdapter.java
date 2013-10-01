package com.googlesearch.api;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlesearch.bean.Item;
import com.social.gamerpoint.R;
import com.social.gamerpoint.Custom_Feed_Adapter.ViewHolder;

public class TutorialsInfoAdapter extends BaseAdapter {

	private Context context;
	private final List<Item> items;
	LayoutInflater inflater;

	public TutorialsInfoAdapter(Context context, List<Item> items) {
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.custom_google_serachlist, null);
			holder = new ViewHolder();
			holder.textview_tv_title = (TextView) vi.findViewById(R.id.tvTitle);
			holder.textview_tv_snippet = (TextView) vi
					.findViewById(R.id.tvSnippet);
			holder.textview_tvLink = (TextView) vi.findViewById(R.id.tvLink);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		holder.textview_tv_title.setId(position);
		holder.textview_tv_snippet.setId(position);
		holder.textview_tvLink.setId(position);
		Item item = items.get(position);

		holder.textview_tv_title.setText("" + item.getTitle());
		holder.textview_tv_snippet.setText("" + item.getSnippet());
		holder.textview_tvLink.setText("" + item.getLink());

		return vi;
	}

	public static class ViewHolder {
		public TextView textview_tv_title, textview_tv_snippet,
				textview_tvLink;

	}

}
