package com.social.gamerpoint;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.custom.classes.ImageLoader;

public class Fragment_ShopSec_Class extends Fragment {
	ListView list_view;
	int[] bitmap_path = new int[] { R.drawable.image_thum1,
			R.drawable.image_thumb2, R.drawable.image_thumb3,
			R.drawable.image_thumb4 };
	String[] title = new String[] { "MERCIES", "SPACED OUT SPRING SHOWCASE",
			"COLD BLOOD CLUB 'WHITE BOYZ' VIDEO RELEASE PARTY", "THE RUBY SUNS" };
	String[] artist = new String[] {
			"LILY & THE PARLOUR TRICKSTHE HOLLOWSAT SEA",
			"TR!BE GVNGPERRIONSAM SIEGELSALOMON FAYECOOPER RIVERSWATI HERUM.WILL",
			"KNITTING FACTORY", "PAINTED PALMSNORTH HIGHLANDS" };
	String[] time = new String[] { "DOORS: 7:00 PM / SHOW: 7:30 PM",
			"DOORS: 9:00 PM / SHOW: 9:00 PM", "DOORS: 6:00 PM / SHOW: 7:00 PM",
			"DOORS: 11:55 PM / SHOW: 11:55 PM" };
	String[] price = new String[] { "30$", "40$", "10$", "15$" };
	ListAdapter mListadapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_shop_layoutsec,
				container, false);
		list_view = (ListView) view.findViewById(R.id.listView1);
		mListadapter = new ListAdapter(getActivity(), title, artist, price,
				time, bitmap_path);
		list_view.setAdapter(mListadapter);

		return view;
	}

	public class ListAdapter extends BaseAdapter {
		private Activity activity;
		private String[] title, artist, time, price;
		private int[] bitmap_path;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;

		public ListAdapter(Activity a, String[] title, String[] artists,
				String[] price, String[] time, int[] bitmap) {
			activity = a;
			this.title = title;
			this.artist = artists;
			this.time = time;
			this.bitmap_path = bitmap;
			this.price = price;

			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		public int getCount() {
			return title.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			View vi = convertView;
			if (convertView == null) {
				vi = inflater.inflate(R.layout.custom_shop_list, null);
				holder = new ViewHolder();
				holder.title = (TextView) vi.findViewById(R.id.textView_title);
				holder.artist = (TextView) vi
						.findViewById(R.id.textView_artist);
				holder.time = (TextView) vi.findViewById(R.id.textView_time);
				holder.price = (TextView) vi.findViewById(R.id.textView_price);
				holder.image = (ImageView) vi
						.findViewById(R.id.imageView_profile_pic);

				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}

			holder.title.setId(position);
			holder.time.setId(position);
			holder.artist.setId(position);
			holder.price.setId(position);
			holder.image.setId(position);

			holder.title.setText(title[position]);
			holder.price.setText(price[position]);
			holder.artist.setText(artist[position]);
			holder.time.setText(time[position]);
			holder.image.setImageResource(bitmap_path[position]);

			return vi;
		}

		public class ViewHolder {
			public TextView title, artist, time, price;

			public ImageView image;

		}
	}

}
