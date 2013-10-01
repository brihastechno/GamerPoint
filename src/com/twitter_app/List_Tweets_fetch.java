package com.twitter_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import oauth.signpost.OAuth;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter.Constants;
import twitter.PrepareRequestTokenActivity;
import JsonParser.JSONARRAY;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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

public class List_Tweets_fetch extends Activity {

	public static final String ENCODING = "UTF-8";
	private static final String postURL = "http://api.twitter.com/1/statuses/update.json";
	private static final String getHomeTimelineURL = "https://api.twitter.com/1.1/followers/list.json";

	// Consumer & Access Keys & Secrets
	private static final String consumerKey = Constants.CONSUMER_KEY;
	private static final String consumerSecret = Constants.CONSUMER_SECRET;
	private static String accessToken = "";
	private static String accessTokenSecret = "";

	// OAuth Header Keys
	private static final String oauth_consumer_key = "oauth_consumer_key";
	private static final String oauth_nonce = "oauth_nonce";
	private static final String oauth_signature = "oauth_signature";
	private static final String oauth_signature_method = "oauth_signature_method";
	private static final String oauth_timestamp = "oauth_timestamp";
	private static final String oauth_token = "oauth_token";
	private static final String oauth_version = "oauth_version";
	private static final String status = "status";
	ProgressDialog dialog;

	private ListView list;
	private LazyAdapterFeed adapter;

	private class Tweet {
		String name;
		String screenName;
		String text;
		String date;
		String profile_pic;

		private Tweet(String name, String screenName, String text, String date,
				String profile_pic) {
			this.name = name;
			this.screenName = screenName;
			this.text = text;
			this.date = date;
			this.profile_pic = profile_pic;
		}

	}

	TextView textview_title;
	Button button_twit_post;
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_follower);
		accessToken = PrepareRequestTokenActivity.prefs.getString(
				OAuth.OAUTH_TOKEN, "");
		accessTokenSecret = PrepareRequestTokenActivity.prefs.getString(
				OAuth.OAUTH_TOKEN_SECRET, "");

		dialog = new ProgressDialog(this);
		textview_title = (TextView) findViewById(R.id.textView_title);
		Boolean b = getIntent().getBooleanExtra("call", false);
		button_twit_post = (Button) findViewById(R.id.button1);
		if (b) {
			findViewById(R.id.relative).setVisibility(View.VISIBLE);
			textview_title.setText("Tweets");

		}
		button_twit_post.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it_post = new Intent(List_Tweets_fetch.this,
						Post_Activity.class);
				startActivity(it_post);

			}
		});

		list = (ListView) findViewById(R.id.listView);

	}

	@Override
	protected void onResume() {
		super.onResume();
		list.setVisibility(View.GONE);
		new Progress_Fetch_Tweets().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		dialog.cancel();

	}

	public void updateTweets(View button) {
		getHomeTimeline();
	}

	private void alertUser(String str) {
		new AlertDialog.Builder(this).setMessage(str)
				.setNeutralButton("OK", null).show();
	}

	public void post(View button) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(postURL);
		try {
			String message = "ghghj".toString();
			addAuthorizationHeader(message, "POST", postURL, post);
			post.setEntity(new StringEntity(percentEncode(status) + "="
					+ percentEncode(message)));
			HttpResponse response = client.execute(post);
			parsePostResponse(response);
		} catch (Exception e) {
			Log.e(Twitter_Home_TimeLine.class.getName(), e.getMessage());
			alertUser("Error: " + e.getMessage());
		}
	}

	private void getHomeTimeline() {
		class TwitterTask extends AsyncTask<Void, Void, HttpResponse> {
			@Override
			protected HttpResponse doInBackground(Void... params) {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(getHomeTimelineURL);
					addAuthorizationHeader(null, "GET", getHomeTimelineURL, get);
					return client.execute(get);
				} catch (final Exception e) {
					Log.e("", e.getMessage());

					return null;
				}
			}

			@Override
			protected void onPostExecute(HttpResponse response) {
				super.onPostExecute(response);
				if (response != null) {
					try {
						parseGetHomeTimelineResponse(response);
						adapter = new LazyAdapterFeed(List_Tweets_fetch.this,
								tweets);
						list.setAdapter(adapter);
					} catch (Exception e) {
						Log.e("", e.getMessage());

					}
				}
			}
		}
		new TwitterTask().execute();
	}

	private void addAuthorizationHeader(String tweet, String method,
			String url, HttpRequestBase request) throws Exception {
		if (!method.equals("GET") && !method.equals("POST")) {
			throw new IllegalArgumentException();
		}
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		String nonce = base64Encode((UUID.randomUUID().toString()
				.replaceAll("-", "").getBytes()));
		String signatureMethod = "HMAC-SHA1";
		String timeStamp = String.valueOf(new Date().getTime() / 1000);
		String version = "1.0";

		// Generating the Parameter String:
		// Add request parameters and status message alphabetically (DO NOT ADD
		// SIGNATURE)
		// Encode keys and values while adding
		// Insert = character between each key and its value
		// Add an ampersand (&) to the end if there are more parameters
		String parameterString = percentEncode(oauth_consumer_key) + "="
				+ percentEncode(consumerKey) + "&" + percentEncode(oauth_nonce)
				+ "=" + percentEncode(nonce) + "&"
				+ percentEncode(oauth_signature_method) + "="
				+ percentEncode(signatureMethod) + "&"
				+ percentEncode(oauth_timestamp) + "="
				+ percentEncode(timeStamp) + "&" + percentEncode(oauth_token)
				+ "=" + percentEncode(accessToken) + "&"
				+ percentEncode(oauth_version) + "=" + percentEncode(version);

		if (tweet != null) {
			parameterString += "&" + percentEncode(status) + "="
					+ percentEncode(tweet);
			;
		}

		// Generate the SignatureBaseString
		String signatureBaseString = method + "&" + percentEncode(url) + "&"
				+ percentEncode(parameterString);

		// Generate the SigningKey
		String signingKey = percentEncode(consumerSecret) + "&"
				+ percentEncode(accessTokenSecret);

		// Generate HMAC-MD5 signature
		String signature = generateHmacSHA1(signingKey, signatureBaseString);

		// Build the HTTP Header
		String oauthHeader = "OAuth " + percentEncode(oauth_consumer_key)
				+ "=\"" + percentEncode(consumerKey) + "\", "
				+ percentEncode(oauth_nonce) + "=\"" + percentEncode(nonce)
				+ "\", " + percentEncode(oauth_signature) + "=\""
				+ percentEncode(signature) + "\", "
				+ percentEncode(oauth_signature_method) + "=\""
				+ percentEncode(signatureMethod) + "\", "
				+ percentEncode(oauth_timestamp) + "=\""
				+ percentEncode(timeStamp) + "\", "
				+ percentEncode(oauth_token) + "=\""
				+ percentEncode(accessToken) + "\", "
				+ percentEncode(oauth_version) + "=\"" + percentEncode(version)
				+ "\"";

		request.addHeader("Authorization", oauthHeader);
	}

	private String generateHmacSHA1(String key, String value) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(ENCODING),
				"HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(keySpec);
		byte[] result = mac.doFinal(value.getBytes(ENCODING));
		return base64Encode(result);
	}

	private void parsePostResponse(HttpResponse response)
			throws IllegalStateException, IOException, JSONException {
		StringBuilder sb = getResponseBody(response);
		if (response.getStatusLine().getStatusCode() == 200) {
			getHomeTimeline();
			// alertUser("Tweet Successful!\nID: " + new
			// JSONObject(sb.toString()).get("id"));
		}
		// Not OK
		else {
			Log.e(Twitter_Home_TimeLine.class.getName(), "Response Code: "
					+ response.getStatusLine().getStatusCode() + "\nResponse: "
					+ sb.toString());
			alertUser("Error Code: " + response.getStatusLine().getStatusCode()
					+ "\n" + new JSONObject(sb.toString()).getString("error"));
		}
	}

	private void parseGetHomeTimelineResponse(HttpResponse response)
			throws IllegalStateException, IOException, JSONException {
		StringBuilder sb = getResponseBody(response);
		if (response.getStatusLine().getStatusCode() == 200) {
			tweets.clear();
			JSONArray jsTweets = new JSONArray(sb.toString());
			if (jsTweets.length() == 0) {

			} else {

				for (int i = 0; i < jsTweets.length(); i++) {
					JSONObject jsTweet = jsTweets.getJSONObject(i);
					JSONObject jsUser = jsTweet.getJSONObject("user");
					Tweet tweet = new Tweet(jsUser.getString("name"),
							jsUser.getString("screen_name"),
							jsTweet.getString("text"),
							jsTweet.getString("created_at"),
							jsUser.getString("profile_image_url"));
					tweets.add(tweet);
				}
			}
		}
		// Not OK
		else {
			Log.e(Twitter_Home_TimeLine.class.getName(), "Response Code: "
					+ response.getStatusLine().getStatusCode() + "\nResponse: "
					+ sb.toString());
			alertUser("Error Code: " + response.getStatusLine().getStatusCode()
					+ "\n" + new JSONObject(sb.toString()).getString("error"));
		}
	}

	private static StringBuilder getResponseBody(HttpResponse response)
			throws IllegalStateException, IOException {
		InputStream is = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb;
	}

	private String percentEncode(String s) throws UnsupportedEncodingException {
		// This could be done faster with more hand-crafted code.
		return URLEncoder.encode(s, ENCODING)
		// OAuth encodes some characters differently:
				.replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
	}

	private static String base64Encode(byte[] array) {
		return Base64.encodeToString(array, Base64.NO_WRAP);
	}

	public class LazyAdapterFeed extends BaseAdapter {
		private List_Tweets_fetch activity;
		private LayoutInflater inflater = null;
		public ImageLoader imageLoader;
		ArrayList<String> _tweet_text;
		ArrayList<String> _username;
		ArrayList<String> _profile;
		ArrayList<Tweet> tweet_feed;

		public LazyAdapterFeed(List_Tweets_fetch list_Tweets_fetch,
				ArrayList<Tweet> tweet_feed) {
			activity = list_Tweets_fetch;

			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity.getApplicationContext());

			this.tweet_feed = tweet_feed;
			// TODO Auto-generated constructor stub
		}

		public int getCount() {
			return tweet_feed.size();
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
				holder.screen_name = (TextView) vi
						.findViewById(R.id.textView_screenname);
				holder.image = (ImageView) vi.findViewById(R.id.image_view);
				holder.message = (TextView) vi.findViewById(R.id.textView_text);

				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}
			Tweet tweet = tweet_feed.get(position);
			holder.name.setId(position);
			holder.image.setId(position);
			holder.message.setId(position);
			holder.screen_name.setId(position);

			holder.name.setText(tweet.name);
			holder.message.setText(tweet.text);
			holder.screen_name.setText("@" + tweet.screenName);
			imageLoader.DisplayImage(tweet.profile_pic, holder.image);

			return vi;
		}
	}

	public class ViewHolder {
		public TextView name, message, screen_name;
		public ImageView image;

	}

	public class Progress_Fetch_Tweets extends
			AsyncTask<Void, Void, HttpResponse> {
		@Override
		protected HttpResponse doInBackground(Void... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(getHomeTimelineURL);
				addAuthorizationHeader(null, "GET", getHomeTimelineURL, get);
				return client.execute(get);
			} catch (final Exception e) {
				Log.e("", e.getMessage());

				return null;
			}
		}

		@Override
		protected void onPostExecute(HttpResponse response) {
			super.onPostExecute(response);
			if (response != null) {
				try {
					parseGetHomeTimelineResponse(response);
					adapter = new LazyAdapterFeed(List_Tweets_fetch.this,
							tweets);
					list.setAdapter(adapter);
					list.setVisibility(View.VISIBLE);
					dialog.cancel();
				} catch (Exception e) {
					Log.e("", e.getMessage());
					dialog.cancel();

				}
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}
	}

}