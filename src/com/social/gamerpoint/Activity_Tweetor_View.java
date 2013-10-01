package com.social.gamerpoint;

import java.util.ArrayList;
import java.util.Date;

import oauth.signpost.OAuth;
import twitter.PrepareRequestTokenActivity;
import twitter.TwitterUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_Tweetor_View extends Activity {
	public static SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	private TextView loginStatus;
	Button show_tweets, button_login, tweet, clearCredentials;
	ArrayList<String> arraylist_text = new ArrayList<String>();
	ListView listview;

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			showAlert("Tweet", " Successfully Tweet Posted");
		}
	};
	ProgressDialog dialog;
	public int TOKEN = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweetor_layout);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		dialog = new ProgressDialog(this);
		loginStatus = (TextView) findViewById(R.id.login_status);
		tweet = (Button) findViewById(R.id.btn_tweet);
		show_tweets = (Button) findViewById(R.id.btn_fetch_tweets);
		listview = (ListView) findViewById(R.id.listview);
		clearCredentials = (Button) findViewById(R.id.btn_clear_credentials);
		button_login = (Button) findViewById(R.id.btn_tweet_Login);
		tweet.setOnClickListener(new View.OnClickListener() {
			/**
			 * Send a tweet. If the user hasn't authenticated to Tweeter yet,
			 * he'll be redirected via a browser to the twitter login page. Once
			 * the user authenticated, he'll authorize the Android application
			 * to send tweets on the users behalf.
			 */
			public void onClick(View v) {
				if (TwitterUtils.isAuthenticated(prefs)) {
					sendTweet();
				} else {
					Intent i = new Intent(getApplicationContext(),
							PrepareRequestTokenActivity.class);
					i.putExtra("tweet_msg", getTweetMsg());
					startActivity(i);
				}
			}
		});

		clearCredentials.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				clearCredentials();
				updateLoginStatus();
			}
		});
		show_tweets.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				arraylist_text.clear();
				arraylist_text = TwitterUtils.showTweetsAbout(prefs,
						"Social Platform");
				Custom_Feed_Adapter custom_adpater = new Custom_Feed_Adapter(
						getApplicationContext(), arraylist_text);
				custom_adpater.notifyDataSetChanged();
				listview.setAdapter(custom_adpater);

			}
		});
		button_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						PrepareRequestTokenActivity.class);
				i.putExtra("tweet_msg", getTweetMsg());
				startActivityForResult(i, TOKEN);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		super.onResume();
		new ProgressTaskSec().execute();
	}

	public void updateLoginStatus() {
		loginStatus.setText("Logged into Twitter : "
				+ TwitterUtils.isAuthenticated(prefs));
		boolean boolean_login = TwitterUtils.isAuthenticated(prefs);

	}

	private String getTweetMsg() {
		return "Tweeting from Android App at " + new Date().toLocaleString();
	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					TwitterUtils.sendTweet(prefs, getTweetMsg());
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}

	private void showAlert(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(R.string.ok, null).show();
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
			// TODO Auto-generated method stub
			// arr = tlist.getTopicImages(topic_id);
			updateLoginStatus();

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// this.dialog.dismiss();
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