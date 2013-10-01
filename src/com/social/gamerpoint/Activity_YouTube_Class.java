package com.social.gamerpoint;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import JsonParser.JSONARRAY;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.custom.classes.ImageLoader;
import com.social.gamerpoint.Activity_YouTube_Class.ProgressTaskSec.Youtyube_data;
import com.youtube.api.FullscreenDemoActivity;

public class Activity_YouTube_Class extends Activity {
	ListView mlistview;
	ArrayList<Youtyube_data> arraylist_youtube;
	ProgressDialog dialog;

	String url = "http://gdata.youtube.com/feeds/mobile/videos?alt=json&v=2&safeSearch=none&time=all_time&uploader=partner";
	EditText edittext_serach;
	ImageView image_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youtube_layout);
		mlistview = (ListView) findViewById(R.id.listView1);
		edittext_serach = (EditText) findViewById(R.id.editText_search);
		edittext_serach
				.setOnEditorActionListener(new DoneOnEditorActionListener());
		image_search = (ImageView) findViewById(R.id.imageView_search);
		dialog = new ProgressDialog(this);
		arraylist_youtube = new ArrayList<Activity_YouTube_Class.ProgressTaskSec.Youtyube_data>();
		new ProgressTaskSec().execute();
		image_search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				serach_function();
			}
		});

	}

	public void serach_function() {
		// TODO Auto-generated method stub
		String search_text = edittext_serach.getText().toString().trim();
		Log.d("serach_text", "" + search_text);
		if (edittext_serach.getText().toString().length() > 1) {
			String s = null;
			try {
				s = URLEncoder.encode(search_text, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("serach_text url", "" + s);
			url = "http://gdata.youtube.com/feeds/mobile/videos/-/" + s
					+ "?alt=json&v=2";
			arraylist_youtube.clear();
			new ProgressTaskSec().execute();
		} else {
			url = "http://gdata.youtube.com/feeds/mobile/videos?alt=json&v=2&safeSearch=none&time=all_time&uploader=partner";
			arraylist_youtube.clear();
			new ProgressTaskSec().execute();
		}
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

				JSONArray result_array = json.getJSONObject("feed")
						.getJSONArray("entry");
				Log.d("result_array", "" + result_array.length());
				for (int i = 0; i < result_array.length(); i++) {
					JSONObject json2 = result_array.getJSONObject(i);
					String video_id = json2.getJSONObject("id").getString("$t");
					int k = video_id.indexOf("video:");
					String vido = video_id.substring(k + 6);
					Log.d("String id " + video_id, "" + vido);
					String video_title = json2.getJSONObject("title")
							.getString("$t");

					// String picture=json2.getString("picture");
					Youtyube_data youtube = new Youtyube_data();
					youtube.video_id = vido;
					youtube.video_title = video_title;
					youtube.video_url = "http://img.youtube.com/vi/" + vido
							+ "/2.jpg";

					arraylist_youtube.add(youtube);

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
			if (arraylist_youtube.size() < 1) {
				Toast.makeText(Activity_YouTube_Class.this,
						"No serach result Found", Toast.LENGTH_SHORT).show();
			}
			LazyAdapterFeed adapter = new LazyAdapterFeed(
					Activity_YouTube_Class.this, arraylist_youtube);
			mlistview.setAdapter(adapter);
			dialog.cancel();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			dialog.cancel();
		}

		public class LazyAdapterFeed extends BaseAdapter {
			private Activity_YouTube_Class activity;
			private ArrayList<Youtyube_data> data;
			private LayoutInflater inflater = null;
			public ImageLoader imageLoader;

			public LazyAdapterFeed(Activity_YouTube_Class a,
					ArrayList<Youtyube_data> friends) {
				activity = a;
				data = friends;
				inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				imageLoader = new ImageLoader(activity.getApplicationContext());

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
					vi = inflater.inflate(R.layout.custom_youtube_list, null);
					holder = new ViewHolder();
					holder.video_title = (TextView) vi
							.findViewById(R.id.textView_title);
					holder.video_pic = (ImageView) vi
							.findViewById(R.id.imageView_video);
					vi.setTag(holder);
				} else {
					holder = (ViewHolder) vi.getTag();
				}
				Youtyube_data friend = data.get(position);
				holder.video_title.setId(position);

				holder.video_pic.setId(position);
				holder.video_title.setText(friend.video_title);
				imageLoader.DisplayImage(friend.video_url, holder.video_pic);
				vi.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Youtyube_data friend = data.get(position);
						Intent i = new Intent(Activity_YouTube_Class.this,
								FullscreenDemoActivity.class);
						i.putExtra("video_id", friend.video_id);
						startActivity(i);
					}
				});
				return vi;
			}
		}

		public class ViewHolder {
			public TextView video_title;
			public ImageView video_pic;

		}

		public class Youtyube_data {
			String video_id;
			String video_title;
			String video_url;
		}

	}

	class DoneOnEditorActionListener implements OnEditorActionListener {
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH
					|| actionId == EditorInfo.IME_ACTION_DONE) {

				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				serach_function();
				return true;
			}
			return false;
		}

	}
}
