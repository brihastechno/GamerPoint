package com.instagram.app;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import com.custom.classes.ImageLoader;
import com.social.gamerpoint.R;

import JsonParser.JSONARRAY;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_Instagram_Follower_List extends Activity {

	private static final String Username = "username";
	private static final String Profile_Picture = "profile_picture";

	ArrayList<String> mArrayList_username;
	ArrayList<String> mArrayList_profile_pictures, mArrayList_Full_Name;
	ListView mView;

	ProgressDialog progress;

	Button mButton_following, mButton_you;
	ListView mListView;
	String instagram_id, string_token, followed;
	public static Bitmap mbitmap[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instagram_follower_list);
		mView = (ListView) findViewById(R.id.list_View);
		progress = new ProgressDialog(Activity_Instagram_Follower_List.this);
		instagram_id = getIntent().getStringExtra("string_id");
		string_token = getIntent().getStringExtra("string_token");
		Log.e("<><><><><><><><><><><><<>", "" + string_token);
		Log.e("<><><><><><><><><><><><<>", "" + instagram_id);

		mArrayList_username = new ArrayList<String>();
		mArrayList_Full_Name = new ArrayList<String>();

		mArrayList_profile_pictures = new ArrayList<String>();

		if (mArrayList_username.size() != 0
				&& mArrayList_profile_pictures.size() != 0) {

			mArrayList_username.clear();
			mArrayList_profile_pictures.clear();
			mArrayList_Full_Name.clear();

		}
		followed = "follows";

		mButton_following = (Button) findViewById(R.id.follower_button_instagram);
		mButton_you = (Button) findViewById(R.id.you_button_instagram);

		// button action
		mButton_following.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				followed = "followed-by";
				if (mArrayList_username.size() != 0
						&& mArrayList_profile_pictures.size() != 0) {

					mArrayList_username.clear();
					mArrayList_profile_pictures.clear();
					mArrayList_Full_Name.clear();

				}
				mButton_you.setTextColor(Color.parseColor("#2D2D2D"));
				mButton_following.setTextColor(Color.parseColor("#4B4B4B"));
				progress = new ProgressDialog(
						Activity_Instagram_Follower_List.this);
				new List(Activity_Instagram_Follower_List.this).execute();

			}
		});

		mButton_you.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				followed = "follows";
				if (mArrayList_username.size() != 0
						&& mArrayList_profile_pictures.size() != 0) {

					mArrayList_username.clear();
					mArrayList_profile_pictures.clear();
					mArrayList_Full_Name.clear();

				}
				mButton_following.setTextColor(Color.parseColor("#2D2D2D"));
				mButton_you.setTextColor(Color.parseColor("#4B4B4B"));
				progress = new ProgressDialog(
						Activity_Instagram_Follower_List.this);
				new List(Activity_Instagram_Follower_List.this).execute();
			}
		});
		new List(Activity_Instagram_Follower_List.this).execute();

	}

	private void send_data_toAPI() {
		String url_fetch = "https://api.instagram.com/v1/users/" + instagram_id
				+ "/" + followed + "?access_token=" + string_token;
		// Creating JSON Parser instance
		JSONARRAY jParser = new JSONARRAY();
		// getting JSON string from URL

		String response = jParser.getJSONFromUrl(url_fetch);

		try {
			JSONObject mJsonObject = new JSONObject(response);

			JSONArray mjsonArray = mJsonObject.getJSONArray("data");
			for (int j = 0; j < mjsonArray.length(); j++) {
				JSONObject mJsonObject2 = mjsonArray.getJSONObject(j);
				String user_name = mJsonObject2.getString(Username);
				String full_name = mJsonObject2.getString("full_name");
				mArrayList_username.add(user_name);
				if (full_name.length() > 1)
					mArrayList_Full_Name.add("" + full_name);
				else
					mArrayList_Full_Name.add("" + user_name);
				Log.e("user_name", "" + user_name);

				if (mJsonObject2.getString(Profile_Picture) != null) {
					mArrayList_profile_pictures.add(mJsonObject2.getString(
							Profile_Picture).replace("\\", ""));
				}
				Log.e("mArrayList_profile_pictures====size", "=="
						+ mArrayList_profile_pictures.size());
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
			progress = ProgressDialog.show(context, "Loading ",
					"Loading all home Data", false, true);

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
					Activity_Instagram_Follower_List.this,
					mArrayList_profile_pictures, mArrayList_username,
					mArrayList_Full_Name);
			mView.setAdapter(adapter);
			progress.cancel();

		}

	}

	// custom list view
	// ==========================================================================
	public class LazyAdapterFeed extends BaseAdapter {
		private Activity_Instagram_Follower_List mFollower_List_Activity;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;
		ArrayList<String> _username;
		ArrayList<String> _profile;
		ArrayList<String> _fullname;

		public LazyAdapterFeed(Activity_Instagram_Follower_List a,
				ArrayList<String> mArrayList_profile_pictures,
				ArrayList<String> mArrayList_username,
				ArrayList<String> mArrayList_Full_Name) {
			mFollower_List_Activity = a;
			inflater = (LayoutInflater) mFollower_List_Activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(
					mFollower_List_Activity.getApplicationContext());
			_profile = mArrayList_profile_pictures;
			_username = mArrayList_username;
			_fullname = mArrayList_Full_Name;
			Log.e("_profile", "<><><?<><=" + _profile.size());
			Log.e("_username", "<><><?<><=" + _username.size());
		}

		public int getCount() {
			return _profile.size();
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
				vi = inflater.inflate(R.layout.custom_instagram_follower_list,
						null);
				holder = new ViewHolder();
				holder.name = (TextView) vi
						.findViewById(R.id.textView_username);
				holder.mImageView = (ImageView) vi
						.findViewById(R.id.imageView_profile_image);
				holder.fullname = (TextView) vi
						.findViewById(R.id.textView_fullname);
				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}
			holder.name.setId(position);
			holder.mImageView.setId(position);
			holder.fullname.setId(position);
			imageLoader.DisplayImage(_profile.get(position), holder.mImageView);
			holder.name.setText(_username.get(position));
			holder.fullname.setText(_fullname.get(position));

			return vi;
		}
	}

	public class ViewHolder {
		public TextView name, fullname;
		public ImageView mImageView;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		progress.cancel();

	}

}
