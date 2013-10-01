package com.social.gamerpoint;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import JsonParser.JSONARRAY;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.custom.classes.Friend;
import com.custom.classes.LazyAdapter;

public class Activity_List_followed extends Activity {
	ListView list_view;
	boolean isFollowed;
	String instagram_id, string_token, followed;
	TextView text_titled;
	ArrayList<Friend> arraylist_friend = new ArrayList<Friend>();
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_followed);
		dialog = new ProgressDialog(this);
		arraylist_friend.clear();
		text_titled = (TextView) findViewById(R.id.textView_title);
		isFollowed = getIntent().getBooleanExtra("is_follow", false);
		instagram_id = getIntent().getStringExtra("string_id");
		string_token = getIntent().getStringExtra("string_token");
		if (isFollowed) {
			followed = "follows";
			text_titled.setText("Follows User");
		} else {
			followed = "followed-by";
			text_titled.setText("Followed-By");
		}
		new ProgressTaskFetch().execute();

	}

	public class ProgressTaskFetch extends AsyncTask<String, Void, String> {

		public ProgressTaskFetch() {

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			send_data_toAPI();

			return null;
		}

		private void send_data_toAPI() {
			// TODO Auto-generated method stub

			String url = "https://api.instagram.com/v1/users/" + instagram_id
					+ "/" + followed + "?access_token=" + string_token;
			// URL url = new URL(mTokenUrl + "&code=" + code);
			JSONARRAY jParser = new JSONARRAY();
			String response = jParser.getJSONFromUrl(url);
			Log.d("response getttte", "" + response);
			JSONObject json;
			try {
				json = new JSONObject(response);
				// Getting Array of Contacts
				JSONArray result_array = json.getJSONArray("data");
				Log.d("Length of JJJdd", "" + result_array.length());
				for (int i = 0; i < result_array.length(); i++) {
					JSONObject jsond = result_array.getJSONObject(i);
					Friend f = new Friend();
					f.fullname = jsond.getString("username");
					f.pic_url = jsond.getString("profile_picture");
					arraylist_friend.add(f);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// this.dialog.dismiss();
			list_view = (ListView) findViewById(R.id.listView1);
			LazyAdapter lazyadapter = new LazyAdapter(
					Activity_List_followed.this, arraylist_friend);
			list_view.setAdapter(lazyadapter);
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

}
