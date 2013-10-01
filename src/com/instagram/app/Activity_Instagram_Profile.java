package com.instagram.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.custom.classes.ImageLoader;
import com.instagram.app.Activity_Instagram_Explore_Tab.LazyAdapterFeed;
import com.instagram.app.Activity_Instagram_Explore_Tab.ViewHolder;
import com.social.gamerpoint.R;

import JsonParser.JSONARRAY;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Instagram_Profile extends Activity {
	String user_id, access_token;
	String user_name, full_name, media_count, follower_count, following_count,
			profile_pic;
	ImageLoader imageloader;
	TextView textview_media_count, textview_followers_count,
			textview_following_count, textview_fullname;
	ImageView imageview_profile;
	ProgressDialog dialog;
	TextView textview_username;
	GridView gridview;
	ArrayList<String> image_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instagram_profile);
		imageview_profile = (ImageView) findViewById(R.id.imageView_profile);
		user_id = getIntent().getStringExtra("string_id");
		access_token = getIntent().getStringExtra("string_token");
		textview_media_count = (TextView) findViewById(R.id.textView_media_count);
		textview_fullname = (TextView) findViewById(R.id.textView_full_name);
		textview_followers_count = (TextView) findViewById(R.id.textView_followers_count);
		textview_following_count = (TextView) findViewById(R.id.textView_following_count);
		textview_username = (TextView) findViewById(R.id.textView_username);

		imageloader = new ImageLoader(getApplicationContext());
		dialog = new ProgressDialog(this);
		gridview = (GridView) findViewById(R.id.gridview_images_explore);
		image_url = new ArrayList<String>();
		new Fetch_Profile_information().execute();
		new List(Activity_Instagram_Profile.this).execute();
	}

	class Fetch_Profile_information extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			send_data_toAPI();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			textview_followers_count.setText(follower_count);
			textview_following_count.setText(following_count);
			textview_fullname.setText(full_name);
			textview_media_count.setText(media_count);
			textview_username.setText(user_name.toUpperCase());
			imageloader.DisplayImage(profile_pic, imageview_profile);
			dialog.cancel();

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		private void send_data_toAPI() {
			String url_fetch = "https://api.instagram.com/v1/users/" + user_id
					+ "?access_token=" + access_token;
			// Creating JSON Parser instance
			JSONARRAY jParser = new JSONARRAY();
			// getting JSON string from URL
			String response = jParser.getJSONFromUrl(url_fetch);
			try {
				JSONObject mJsonObject = new JSONObject(response);
				JSONObject mjson_data = mJsonObject.getJSONObject("data");
				user_name = mjson_data.getString("username");
				full_name = mjson_data.getString("full_name");
				profile_pic = mjson_data.getString("profile_picture");
				JSONObject count_object = mjson_data.getJSONObject("counts");
				media_count = count_object.getString("media");
				follower_count = count_object.getString("followed_by");
				following_count = count_object.getString("follows");

			} catch (Exception e) {
				e.printStackTrace();

			}
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

		}

		@Override
		protected String doInBackground(Void... params) {

			send_data_toAPI_Images();
			return null;
		}

		protected void onPostExecute(String result) {

			LazyAdapterFeed adapter = new LazyAdapterFeed(
					Activity_Instagram_Profile.this, image_url);
			gridview.setAdapter(adapter);

		}

		private void send_data_toAPI_Images() {
			String url_fetch = "https://api.instagram.com/v1/users/" + user_id
					+ "/media/recent?access_token=" + access_token;
			// Creating JSON Parser instance
			JSONARRAY jParser = new JSONARRAY();
			// getting JSON string from URL

			String response = jParser.getJSONFromUrl(url_fetch);
			try {
				JSONObject mJsonObject = new JSONObject(response);

				JSONArray mjsonArray = mJsonObject.getJSONArray("data");
				for (int j = 0; j < mjsonArray.length(); j++) {
					JSONObject mJsonObject2 = mjsonArray.getJSONObject(j);

					JSONObject mJsonObject4 = mJsonObject2
							.getJSONObject("images");
					JSONObject mJsonObject5 = mJsonObject4
							.getJSONObject("thumbnail");

					if (mJsonObject5.getString("url") != null) {
						String st = mJsonObject5.getString("url").replace("\\",
								"");
						image_url.add(st);
					}

				}

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

	}

	// custom list view
	// ==========================================================================
	public class LazyAdapterFeed extends BaseAdapter {
		private Activity_Instagram_Profile mFollower_List_Activity;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;

		ArrayList<String> _images;

		public LazyAdapterFeed(Activity_Instagram_Profile a,
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
		dialog.cancel();
	}

}
