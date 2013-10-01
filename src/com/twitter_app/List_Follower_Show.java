package com.twitter_app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import twitter.PrepareRequestTokenActivity;

import JsonParser.JSONARRAY;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.custom.classes.ImageLoader;
import com.social.gamerpoint.R;

public class List_Follower_Show extends Activity {
	private static final String ID = "id";
	private static final String ScreenName = "screen_name";
	private static final String Name = "name";

	ProgressDialog dialog;

	ArrayList<String> mArrayList_id;
	ArrayList<String> mArrayList_Screen_name;
	ArrayList<String> mArrayList_name;
	ArrayList<String> mArrayList_Profile_image;
	ListView followers_list;
	String fetch_url;

	// ProgressBar progress_bar;

	ImageView image;
	public static Bitmap mbitmap[];
	TextView textview_title;
	Button button_twit_post;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_follower);
		dialog = new ProgressDialog(List_Follower_Show.this);
		followers_list = (ListView) findViewById(R.id.listView);
		fetch_url = "http://api.twitter.com/1.1/statuses/followers/"
				+ PrepareRequestTokenActivity.prefs.getString("screen_name",
						null) + ".json";
		Log.e("fetch_url=====================", ""+fetch_url);
		textview_title = (TextView) findViewById(R.id.textView_title);
		Boolean b = getIntent().getBooleanExtra("call", false);
		button_twit_post = (Button) findViewById(R.id.button1);
		if (b) {
			findViewById(R.id.relative).setVisibility(View.VISIBLE);
			textview_title.setText("Followers");

		}
		mArrayList_id = new ArrayList<String>();
		mArrayList_Screen_name = new ArrayList<String>();
		mArrayList_name = new ArrayList<String>();
		mArrayList_Profile_image = new ArrayList<String>();

		button_twit_post.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it_post = new Intent(List_Follower_Show.this,
						Post_Activity.class);
				startActivity(it_post);

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mArrayList_id.size() != 0 && mArrayList_Screen_name.size() != 0
				&& mArrayList_name.size() != 0
				&& mArrayList_Profile_image.size() != 0) {

			mArrayList_id.clear();
			mArrayList_Screen_name.clear();
			mArrayList_name.clear();
			mArrayList_Profile_image.clear();
			followers_list.setVisibility(View.GONE);
		}

		new Progress_Fetch_Tweets().execute();
	}

	public class Progress_Fetch_Tweets extends AsyncTask<String, Void, String> {
		public Progress_Fetch_Tweets() {
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			send_data_toAPI();

			return null;
		}

		private void send_data_toAPI() {
			// Creating JSON Parser instance
			JSONARRAY jParser = new JSONARRAY();
			// getting JSON string from URL
			String response = jParser.getJSONFromUrl(fetch_url);

			try {
				JSONArray jsonArray = new JSONArray(response);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String id = jsonObject.getString(ID);
					String Screen_Name = jsonObject.getString(ScreenName);
					String name = jsonObject.getString(Name);

					Log.e("id", "" + id);
					Log.e("Screen_Name", "" + Screen_Name);
					Log.e("name", "" + name);

					// array.add("name: "+Screen_Name+"/n"+"id: "+id+"/n"+"Name: "+name);
					mArrayList_id.add("id: " + id);
					mArrayList_name.add(name);
					mArrayList_Screen_name.add("@" + Screen_Name);
					if (jsonObject.getString("profile_image_url") != null) {
						mArrayList_Profile_image.add(jsonObject
								.getString("profile_image_url"));
					}

				}

			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// this.dialog.dismiss();

			LazyAdapterFeed adapter = new LazyAdapterFeed(
					List_Follower_Show.this, mArrayList_Screen_name,
					mArrayList_Profile_image, mArrayList_name);
			followers_list.setAdapter(adapter);
			followers_list.setVisibility(View.VISIBLE);
			dialog.cancel();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

		}

	}

	public class LazyAdapterFeed extends BaseAdapter {
		private List_Follower_Show activity;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;
		ArrayList<String> _tweet_text;
		ArrayList<String> _username;
		ArrayList<String> _profile;

		public LazyAdapterFeed(List_Follower_Show list_Follower_Show,
				ArrayList<String> mArrayList_Text,
				ArrayList<String> mArrayList_Profile_image,
				ArrayList<String> mArrayList_User_Name) {
			activity = list_Follower_Show;

			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity.getApplicationContext());

			_tweet_text = mArrayList_Text;
			_username = mArrayList_User_Name;
			_profile = mArrayList_Profile_image;
			// TODO Auto-generated constructor stub
		}

		public int getCount() {
			return _tweet_text.size();
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
				vi = inflater.inflate(R.layout.tweets, null);
				holder = new ViewHolder();
				holder.name = (TextView) vi
						.findViewById(R.id.textView_username);
				holder.image = (ImageView) vi.findViewById(R.id.image_view);
				holder.message = (TextView) vi.findViewById(R.id.textView_text);

				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}

			holder.name.setId(position);
			holder.image.setId(position);
			holder.message.setId(position);
			holder.name.setText(_username.get(position));
			holder.message.setText(_tweet_text.get(position));
			imageLoader.DisplayImage(_profile.get(position), holder.image);
			if (_profile.get(position).length() > 1) {
				imageLoader.DisplayImage(_profile.get(position), holder.image);
			}
			return vi;
		}
	}

	public class ViewHolder {
		public TextView name, message;
		public ImageView image;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dialog.cancel();
	}
}
