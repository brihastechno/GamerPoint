package com.social.gamerpoint;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import JsonParser.JSONARRAY;
import JsonParser.JSONParserPost;
import JsonParser.JsonDeletePost;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.custom.classes.Facebook_feed;
import com.custom.classes.ImageLoader;
import com.facebook.Session;

public class Activity_news_feeds extends Activity {
	String url;
	ListView listview;
	ProgressDialog dialog;
	String User_ID;
	ArrayList<Facebook_feed> feed = new ArrayList<Facebook_feed>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feeds);
		listview = (ListView) findViewById(R.id.listView);
		url = getIntent().getStringExtra("url");
		User_ID = getIntent().getStringExtra("user_id");
		dialog = new ProgressDialog(this);
		new ProgressTaskSec().execute();
	}

	public class ProgressTaskSec extends AsyncTask<String, Void, String> {
		public ProgressTaskSec() {
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
			String response = jParser.getJSONFromUrl(url);
			JSONObject json;
			try {
				json = new JSONObject(response);
				// Getting Array of Contacts

				JSONArray result_array = json.getJSONArray("data");
				Log.d("result_array", "" + result_array.length());
				for (int i = 0; i < result_array.length(); i++) {
					JSONObject json2 = result_array.getJSONObject(i);
					String post_id = json2.getString("id");
					JSONObject name_id = json2.getJSONObject("from");
					String _Name = name_id.getString("name");
					String _Id = name_id.getString("id");
					// String picture=json2.getString("picture");
					Facebook_feed face = new Facebook_feed();
					face.id = post_id;

					face.name = _Name;
					face.profile_url = "http://graph.facebook.com/" + _Id
							+ "/picture?width=50&height=50";

					try {
						face.pic_url = json2.getString("picture").replace("_s",
								"_n");
						// Log.d("!@@@@@@@Image_url"+i, "" + face.pic_url);
					} catch (JSONException e) {
						face.pic_url = "";
						// Log.d("!@@@@@@@Image_url in Catch"+i, "" +
						// face.pic_url);
					}
					try {
						face.message = json2.getString("message");
					} catch (JSONException e) {
						face.message = "";
					}
					try {
						String story = json2.getString("story");
						face.story = story.replace(_Name, "");
					} catch (JSONException e) {
						face.story = "";
					}
					try {
						face.created = json2.getString("created_time");
					} catch (JSONException e) {
						face.created = "";
					}

					try {
						JSONObject json_like = json2.getJSONObject("likes");
						JSONArray json_data = json_like.getJSONArray("data");
						JSONObject json_object_data = json_data
								.getJSONObject(0);
						if (json_object_data.getString("id").equalsIgnoreCase(
								User_ID)) {
							face.like = true;
							// Log.i("@@@@Json _Array " + User_ID, "" +
							// face.like);

						} else {
							face.like = false;
							// Log.i("@@@@Json _Array In Else " + User_ID, ""
							// + face.like);

						}

					} catch (JSONException e) {
						face.like = false;
						// Log.i("Json _Array InException  " + _Id, "@@@@@ "
						// + face.like);
					}

					feed.add(face);

				}
			} catch (Exception e) {
				return;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// this.dialog.dismiss();
			LazyAdapterFeed adapter = new LazyAdapterFeed(
					Activity_news_feeds.this, feed);
			listview.setAdapter(adapter);
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

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			dialog.cancel();
		}

	}

	public class LazyAdapterFeed extends BaseAdapter {
		private Activity_news_feeds activity;
		private ArrayList<Facebook_feed> data;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;
		ArrayList<String> sparse = new ArrayList<String>();

		public LazyAdapterFeed(Activity_news_feeds a,
				ArrayList<Facebook_feed> friends) {
			activity = a;
			data = friends;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity.getApplicationContext());
			Log.d("sss", "" + data.size());
			for (int i = 0; i < data.size(); i++) {
				Facebook_feed f = data.get(i);
				Log.i("pic_url" + i, "" + f.pic_url);
				Log.i(" profile_url" + i, "" + f.profile_url);
				if (f.like)
					sparse.add("UnLike");

				else
					sparse.add("Like");

			}

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

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			View vi = convertView;
			if (convertView == null) {
				vi = inflater.inflate(R.layout.custom_facefook_list_view, null);
				holder = new ViewHolder();
				holder.name = (TextView) vi.findViewById(R.id.textView_name);
				holder.image = (ImageView) vi
						.findViewById(R.id.imageView_profile);
				holder.image_pic = (ImageView) vi
						.findViewById(R.id.imageView_pic);
				holder.text_story = (TextView) vi
						.findViewById(R.id.textView_story);
				holder.message = (TextView) vi
						.findViewById(R.id.textView_message);
				holder.text_created = (TextView) vi
						.findViewById(R.id.textView_created);
				holder.like = (Button) vi.findViewById(R.id.button_like);
				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}
			Facebook_feed friend = data.get(position);
			holder.name.setId(position);
			holder.image.setId(position);
			holder.message.setId(position);
			holder.image_pic.setId(position);
			holder.text_created.setId(position);
			holder.text_story.setId(position);
			holder.like.setId(position);

			holder.name.setText(friend.name);
			holder.message.setText(friend.message);
			holder.text_created.setText(friend.created);
			holder.text_story.setText(friend.story);

			imageLoader.DisplayImage(friend.pic_url, holder.image);
			if (friend.pic_url.length() > 1) {
				imageLoader.DisplayImage(friend.pic_url, holder.image_pic);
			}
			holder.like.setText(sparse.get(position));
			holder.like.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final Session session = Session.getActiveSession();
					String session_token = session.getAccessToken();
					Facebook_feed friend = data.get(position);
					Log.d("The Active Access Token", "" + session_token);
					if (friend.like) {
						String url = "https://graph.facebook.com/" + friend.id
								+ "/likes?access_token=" + session_token;
						// Creating JSON Parser instance
						JsonDeletePost jParser = new JsonDeletePost();
						// getting JSON string from URL
						JSONObject json = jParser.getJSONFromUrl(url);
						friend.like = false;
						sparse.set(position, "Like");
						holder.like.setText(sparse.get(position));

					} else {
						String url = "https://graph.facebook.com/" + friend.id
								+ "/likes";
						ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair(
								"access_token", session_token));
						// Creating JSON Parser instance
						JSONParserPost jParser = new JSONParserPost();
						// getting JSON string from URL
						JSONObject json = jParser.getJSONFromUrl(url,
								nameValuePairs);
						friend.like = true;
						sparse.set(position, "Unlike");
						holder.like.setText(sparse.get(position));
					}

				}
			});

			return vi;
		}
	}

	public class ViewHolder {
		public TextView name, message, text_created, text_story;
		public ImageView image, image_pic;
		public Button like;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dialog.cancel();
	}

}
