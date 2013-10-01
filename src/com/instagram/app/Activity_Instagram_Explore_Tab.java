package com.instagram.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import JsonParser.JSONARRAY;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.custom.classes.ImageLoader;
import com.custom.classes.Instagram_Class;
import com.social.gamerpoint.R;

public class Activity_Instagram_Explore_Tab extends Activity {
	ProgressDialog progress;
	String string_token;
	ArrayList<String> image_url;
	GridView gridview;
	ArrayList<Instagram_Class> pic_view;
	TextView textview_username, textview_title;
	ImageView image_View_profile_pic, image_View_images, imageView_back;
	ImageLoader imageloader;
	ScrollView scroll_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instagram_explore_tab);
		string_token = getIntent().getStringExtra("string_token");
		image_url = new ArrayList<String>();
		gridview = (GridView) findViewById(R.id.gridview_images_explore);
		scroll_view = (ScrollView) findViewById(R.id.display_dialog);
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		textview_title = (TextView) findViewById(R.id.textView_title);
		imageView_back.setVisibility(View.GONE);
		scroll_view.setVisibility(View.GONE);
		pic_view = new ArrayList<Instagram_Class>();
		imageloader = new ImageLoader(getApplicationContext());
		textview_username = (TextView) findViewById(R.id.textView_username);
		image_View_profile_pic = (ImageView) findViewById(R.id.imageView_profile_image);
		image_View_images = (ImageView) findViewById(R.id.share_image);

		new List(Activity_Instagram_Explore_Tab.this).execute();

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				gridview.setVisibility(View.GONE);
				scroll_view.setVisibility(View.VISIBLE);
				textview_title.setVisibility(View.GONE);
				imageView_back.setVisibility(View.VISIBLE);
				Instagram_Class instagram = pic_view.get(position);
				textview_username.setText("" + instagram.username);
				imageloader.DisplayImage(instagram.profile_url,
						image_View_profile_pic);
				imageloader.DisplayImage(instagram.pic_url, image_View_images);

			}

		});

		imageView_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textview_title.setVisibility(View.VISIBLE);
				gridview.setVisibility(View.VISIBLE);
				scroll_view.setVisibility(View.GONE);
				imageView_back.setVisibility(View.GONE);
			}
		});
	}

	private void send_data_toAPI() {
		String url_fetch = "https://api.instagram.com/v1/media/popular?access_token="
				+ string_token;
		// Creating JSON Parser instance
		JSONARRAY jParser = new JSONARRAY();
		// getting JSON string from URL

		String response = jParser.getJSONFromUrl(url_fetch);
		try {
			JSONObject mJsonObject = new JSONObject(response);

			JSONArray mjsonArray = mJsonObject.getJSONArray("data");
			for (int j = 0; j < mjsonArray.length(); j++) {
				Instagram_Class instagram = new Instagram_Class();
				JSONObject mJsonObject2 = mjsonArray.getJSONObject(j);
				JSONObject mJsonObject3 = mJsonObject2.getJSONObject("user");
				JSONObject mJsonObject4 = mJsonObject2.getJSONObject("images");
				JSONObject mJsonObject5 = mJsonObject4
						.getJSONObject("thumbnail");
				JSONObject mJsonObject6 = mJsonObject4
						.getJSONObject("standard_resolution");

				String username = mJsonObject3.getString("username");
				String profile_pic = mJsonObject3.getString("profile_picture");
				String image_standard = mJsonObject6.getString("url").replace(
						"\\", "");

				instagram.username = username;
				instagram.profile_url = profile_pic;
				instagram.pic_url = image_standard;
				pic_view.add(instagram);

				if (mJsonObject5.getString("url") != null) {
					String st = mJsonObject5.getString("url").replace("\\", "");
					image_url.add(st);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// Asynctasks perform===========================================

	private class List extends AsyncTask<Void, Void, String> {
		Context context;

		public List(Context mainActivity) {
			// TODO Auto-generated constructor stub

			context = mainActivity;
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(context, "", "", false, true);

			// progress = ProgressDialog.show(context,
			// "Loading ","Loading all home Data",false,true);
		}

		@Override
		protected String doInBackground(Void... params) {

			send_data_toAPI();
			return null;
		}

		protected void onPostExecute(String result) {

			LazyAdapterFeed adapter = new LazyAdapterFeed(
					Activity_Instagram_Explore_Tab.this, image_url);
			gridview.setAdapter(adapter);
			progress.cancel();

		}

	}

	// custom list view
	// ==========================================================================
	public class LazyAdapterFeed extends BaseAdapter {
		private Activity_Instagram_Explore_Tab mFollower_List_Activity;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;

		ArrayList<String> _images;

		public LazyAdapterFeed(Activity_Instagram_Explore_Tab a,
				ArrayList<String> images) {
			mFollower_List_Activity = a;

			inflater = (LayoutInflater) mFollower_List_Activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(
					mFollower_List_Activity.getApplicationContext());

			_images = images;

		}

		public int getCount() {
			return _images.size();
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
				vi = inflater.inflate(R.layout.custom_instagram_explore, null);
				holder = new ViewHolder();

				holder.mImageView = (ImageView) vi
						.findViewById(R.id.imageView_image);

				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}

			holder.mImageView.setId(position);
			imageLoader.DisplayImage(_images.get(position), holder.mImageView);
			return vi;
		}
	}

	public class ViewHolder {
		public ImageView mImageView;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		progress.cancel();
	}

}
