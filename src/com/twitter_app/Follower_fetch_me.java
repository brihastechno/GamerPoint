package com.twitter_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import com.social.gamerpoint.R;
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
import android.widget.ListView;
import android.widget.TextView;

public class Follower_fetch_me extends Activity {
	private static final String ID = "id";
	private static final String ScreenName = "screen_name";
	private static final String Name = "name";
	private static final String profile_image_url = "profile_image_url";

	ListAdapter listadapter;
	ProgressDialog progress;

	ArrayList<String> mArrayList_id;
	ArrayList<String> mArrayList_Screen_name;
	ArrayList<String> mArrayList_name;
	ArrayList<String> mArrayList_Profile_image;
	ListView list;

	// ProgressBar progress_bar;

	ImageView image;
	public static Bitmap mbitmap[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fetch_list_view);

		progress = new ProgressDialog(Follower_fetch_me.this);
		list = (ListView) findViewById(R.id.listView_fetch_list);

		mArrayList_id = new ArrayList<String>();
		mArrayList_Screen_name = new ArrayList<String>();
		mArrayList_name = new ArrayList<String>();
		mArrayList_Profile_image = new ArrayList<String>();

		if (mArrayList_id.size() != 0 && mArrayList_Screen_name.size() != 0
				&& mArrayList_name.size() != 0
				&& mArrayList_Profile_image.size() != 0) {

			mArrayList_id.clear();
			mArrayList_Screen_name.clear();
			mArrayList_name.clear();
			mArrayList_Profile_image.clear();
		}

		new List(Follower_fetch_me.this).execute();
	}

	// hit the
	// link===============================================================================
	public String readTwitterFeed() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://api.twitter.com/1.1/statuses/followers/levy_levy1234.json");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	// Bipmap work=======================================

	Bitmap bitmap[];
	URL url;
	HttpURLConnection conn = null;

	public Bitmap[] ConvertBitmap() {
		bitmap = new Bitmap[mArrayList_Profile_image.size()];
		Log.e("", "bitmap    " + mArrayList_Profile_image.size());

		for (int i = 0; i < mArrayList_Profile_image.size(); i++) {
			try {
				url = new URL(mArrayList_Profile_image.get(i).toString());

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			try {
				// Log.e("", "Image");
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.connect();

				InputStream is = conn.getInputStream();
				bitmap[i] = BitmapFactory.decodeStream(is);

			} catch (Exception ex) {
			}

		}

		return bitmap;
	}

	// custom list view
	// ==========================================================================
	class ListAdapter extends BaseAdapter {
		int textViewResourceId;

		public ListAdapter(Context context, int textViewResourceId) {

			this.textViewResourceId = textViewResourceId;

		}

		public int getCount() {

			return mArrayList_id.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			ImageView iview;
			TextView tview, mTextView_name, mTextView_screen_name;
			if (convertView == null) {
				LayoutInflater in = getLayoutInflater();
				v = in.inflate(textViewResourceId, null);

				// mbitmap=ConvertBitmap();
				iview = (ImageView) v.findViewById(R.id.image_view);
				tview = (TextView) v.findViewById(R.id.text_id);
				mTextView_name = (TextView) v.findViewById(R.id.text_name);
				mTextView_screen_name = (TextView) v
						.findViewById(R.id.text_screen_name);
				iview.setImageBitmap(mbitmap[position]);
				// tview.setText(array.get(position));

				// Log.e("Array value is >>  ",array.get(position)+"<aray.size is >"+array.size());

			} else {
				// convertView = null;
				LayoutInflater in = getLayoutInflater();
				v = in.inflate(textViewResourceId, null);

				// mbitmap=ConvertBitmap();
				iview = (ImageView) v.findViewById(R.id.image_view);
				tview = (TextView) v.findViewById(R.id.text_id);
				mTextView_name = (TextView) v.findViewById(R.id.text_name);
				mTextView_screen_name = (TextView) v
						.findViewById(R.id.text_screen_name);

				// Log.e("Array value is >>  ",array.get(position)+"<aray.size is >"+array.size());

			}
			iview.setImageBitmap(mbitmap[position]);
			tview.setText(mArrayList_id.get(position));
			mTextView_name.setText(mArrayList_name.get(position));
			mTextView_screen_name.setText(mArrayList_Screen_name.get(position));

			return v;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

	}

	// Asynctasks perform===========================================

	private class List extends AsyncTask<Void, Void, Boolean> {
		Context context;

		public List(Context mainActivity) {
			// TODO Auto-generated constructor stub

			context = mainActivity;
		}

		@Override
		protected void onPreExecute() {
			Log.e("within onPreExecute ", "within onPreExecute<>  ");

			progress = ProgressDialog.show(context, "Loading ",
					"Loading all Follower Data", false, true);
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			String readTwitterFeed = readTwitterFeed();

			// Log.e("within doInBackground ","within doInBackground<>  ");

			try {
				JSONArray jsonArray = new JSONArray(readTwitterFeed);

				/*
				 * Log.i(MainActivity.class.getName(), "Number of entries " +
				 * jsonArray.length());
				 */

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
					mArrayList_name.add("Name: " + name);
					mArrayList_Screen_name.add("screen_name: " + Screen_Name);

					if (jsonObject.getString("profile_image_url") != null) {
						mArrayList_Profile_image.add(jsonObject
								.getString("profile_image_url"));
					}

				}
				mbitmap = ConvertBitmap();

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			Log.e(mArrayList_id.size() + "within onPostExecute ",
					"within onPostExcut<> result is  " + result);
			Log.e("arraylist size id=========", "" + mArrayList_id.size());
			Log.e("arraylist size name=========", "" + mArrayList_name.size());
			Log.e("arraylist size  screen_name=========", ""
					+ mArrayList_Screen_name.size());
			if (result) {

				progress.dismiss();
				listadapter = new ListAdapter(context, R.layout.custom);
				list.setAdapter(listadapter);

			}

		}

	}

}
