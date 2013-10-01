package com.twitter_app;

import java.util.Date;

import twitter.PrepareRequestTokenActivity;
import twitter.TwitterUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.social.gamerpoint.R;

public class Post_Activity extends Activity {
	Button mButton_cancel, mButton_tweet;
	// private SharedPreferences prefs;
	EditText mEditText;
	String mString_post;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.posts_in_tweets);
		mButton_cancel = (Button) findViewById(R.id.button_cancel);
		mButton_tweet = (Button) findViewById(R.id.button_tweet);
		mEditText = (EditText) findViewById(R.id.editText_tweet_post);
		// cancel button
		mButton_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		// tweet button=================
		mButton_tweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (TwitterUtils
						.isAuthenticated(PrepareRequestTokenActivity.prefs)) {

					sendTweet();
					showAlert("Tweet",
							" Successfully Tweet Posted in your Account");
				} else {
					Toast.makeText(getApplicationContext(), "Firstly Login",
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	private String getTweetMsg() {
		mString_post = mEditText.getText().toString();
		return mString_post + "     " + new Date().toLocaleString();
	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					TwitterUtils.sendTweet(PrepareRequestTokenActivity.prefs,
							getTweetMsg());
					// mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	private void showAlert(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(R.string.ok, null).show();
	}

}
