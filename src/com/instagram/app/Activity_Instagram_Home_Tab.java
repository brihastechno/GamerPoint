package com.instagram.app;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.custom.classes.ImageLoader;
import com.custom.classes.Instagram_Class;
import com.social.gamerpoint.R;

import JsonParser.JSONARRAY;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_Instagram_Home_Tab extends Activity {

	private static final String ScreenName = "full_name";
	ListAdapter listadapter;
	ProgressDialog progress;
	String instagram_id, string_token, followed;

	ArrayList<String> mArrayList_url_share_image;
	ArrayList<String> mArrayList_Screen_name;

	ArrayList<String> mArrayList_Profile_image;
	ListView list;

	// ProgressBar progress_bar;

	ImageView image;
	public static Bitmap mbitmap[], mbitmap2[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instagram_home_tab);

		instagram_id = getIntent().getStringExtra("string_id");
		string_token = getIntent().getStringExtra("string_token");
		Log.e("<><><><><><><><><><><><<>", "" + string_token);
		progress = new ProgressDialog(Activity_Instagram_Home_Tab.this);
		list = (ListView) findViewById(R.id.listView_home);
		mArrayList_Screen_name = new ArrayList<String>();
		mArrayList_Profile_image = new ArrayList<String>();
		mArrayList_url_share_image = new ArrayList<String>();

		if (mArrayList_url_share_image.size() != 0
				&& mArrayList_Screen_name.size() != 0
				&& mArrayList_Profile_image.size() != 0) {

			mArrayList_url_share_image.clear();
			mArrayList_Screen_name.clear();

			mArrayList_Profile_image.clear();
		}

		new List(Activity_Instagram_Home_Tab.this).execute();

	}

	private void send_data_toAPI() {
		String url_fetch = "https://api.instagram.com/v1/users/self/feed?access_token="
				+ string_token;

		// Creating JSON Parser instance
		JSONARRAY jParser = new JSONARRAY();
		// getting JSON string from URL

		String response = jParser.getJSONFromUrl(url_fetch);
		try {
			JSONObject mJsonObject = new JSONObject(response);

			JSONArray mjsonArray = mJsonObject.getJSONArray("data");
			for (int j = 0; j < mjsonArray.length(); j++) {
				JSONObject mJsonObject2 = mjsonArray.getJSONObject(j);
				JSONObject mJsonObject3 = mJsonObject2.getJSONObject("user");
				JSONObject mJsonObject4 = mJsonObject2.getJSONObject("images");
				JSONObject mJsonObject5 = mJsonObject4
						.getJSONObject("standard_resolution");

				String Screen_Name = mJsonObject3.getString(ScreenName);
				Log.e("Screen_Name", "" + Screen_Name);

				mArrayList_Screen_name.add(Screen_Name);

				if (mJsonObject3.getString("profile_picture") != null) {
					mArrayList_Profile_image.add(mJsonObject3.getString(
							"profile_picture").replace("\\", ""));
				}
				if (mJsonObject5.getString("url") != null) {
					mArrayList_url_share_image.add(mJsonObject5
							.getString("url").replace("\\", ""));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// Asynctasks perform===========================================

	private class List extends AsyncTask<Void, Void, String> {
		Context context;

		@SuppressWarnings("unused")
		public List(Context mainActivity) {
			// TODO Auto-generated constructor stub

			context = mainActivity;
		}

		@Override
		protected void onPreExecute() {
			Log.e("within onPreExecute ", "within onPreExecute<>  ");

			progress = ProgressDialog.show(context, "Loading ",
					"Loading all home Data", false, true);
		}

		@Override
		protected String doInBackground(Void... params) {

			send_data_toAPI();

			return null;
		}

		protected void onPostExecute(String result) {

			LazyAdapterFeed adapter = new LazyAdapterFeed(
					Activity_Instagram_Home_Tab.this, mArrayList_Profile_image,
					mArrayList_Screen_name, mArrayList_url_share_image);
			list.setAdapter(adapter);
			progress.cancel();
			Log.e("arraylist size  screen_name=========", ""
					+ mArrayList_Screen_name.size());
			Log.e("arraylist size  Profile_imagee=========", ""
					+ mArrayList_Profile_image.size());
			Log.e("arraylist size url_share_iamge=========", ""
					+ mArrayList_url_share_image.size());
			Log.e("arraylist size url_share_iamge=========", ""
					+ mArrayList_url_share_image);

		}

	}

	// ccccccccccccccccccc

	public class LazyAdapterFeed extends BaseAdapter {
		private Activity_Instagram_Home_Tab mHome_List_Activity;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;

		ArrayList<String> _username;
		ArrayList<String> _share_image;
		ArrayList<String> _profile;

		public LazyAdapterFeed(Activity_Instagram_Home_Tab a,
				ArrayList<String> mArrayList_Profile_image,
				ArrayList<String> mArrayList_User_Name,
				ArrayList<String> mArrayList_url_share_image) {
			mHome_List_Activity = a;

			inflater = (LayoutInflater) mHome_List_Activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(getApplicationContext());

			_profile = mArrayList_Profile_image;
			_username = mArrayList_User_Name;

			_share_image = mArrayList_url_share_image;
			Log.e("_profile", "<?<?<??<==" + _profile.size());
			Log.e("_share_image", "<?<?<??<==" + _share_image.size());
			Log.e("_username", "<?<?<??<==" + _username.size());
		}

		public int getCount() {
			return _username.size();
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
				vi = inflater.inflate(
						R.layout.custom_instagram_home_list_layout, null);
				holder = new ViewHolder();
				holder.name = (TextView) vi
						.findViewById(R.id.textView_username);
				holder.image = (ImageView) vi
						.findViewById(R.id.imageView_profile_image);

				holder.mImageView = (ImageView) vi
						.findViewById(R.id.share_image);

				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}

			holder.name.setId(position);
			holder.image.setId(position);
			holder.mImageView.setId(position);

			holder.name.setText(_username.get(position));
			imageLoader.DisplayImage(_profile.get(position), holder.image);
			imageLoader.DisplayImage(_share_image.get(position),
					holder.mImageView);

			return vi;
		}
	}

	public class ViewHolder {
		public TextView name;
		public ImageView image, mImageView;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		progress.cancel();
	}

}
