package com.twitter_app;

import twitter.PrepareRequestTokenActivity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.custom.classes.ImageLoader;
import com.social.gamerpoint.R;

public class Me_Profile_Activity extends Activity {
	Button mButton_follower, mButton_tweets;
	ImageView profile_pic;
	ImageLoader imageloader;
	TextView text_username;
	ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_profile);
		imageloader = new ImageLoader(getApplicationContext());
		text_username = (TextView) findViewById(R.id.textView_user_name);
		dialog = new ProgressDialog(Me_Profile_Activity.this);
		mButton_follower = (Button) findViewById(R.id.button_follower_me);
		mButton_tweets = (Button) findViewById(R.id.tweet_button);
		profile_pic = (ImageView) findViewById(R.id.profile_me_image);
		mButton_follower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent mIntent_follower = new Intent(Me_Profile_Activity.this,
						List_Follower_Show.class);
				mIntent_follower.putExtra("call", true);
				startActivity(mIntent_follower);
			}
		});
		mButton_tweets.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent mIntent_tweets = new Intent(Me_Profile_Activity.this,
						List_Tweets_fetch.class);
				mIntent_tweets.putExtra("call", true);
				startActivity(mIntent_tweets);

			}
		});
		new Progress_Fetch_Profile_pic().execute();

	}

	public class Progress_Fetch_Profile_pic extends
			AsyncTask<String, Void, String> {
		User user = null;

		public Progress_Fetch_Profile_pic() {
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Twitter twitter = new TwitterFactory().getInstance();
			try {
				user = twitter.showUser(PrepareRequestTokenActivity.prefs
						.getString("screen_name", null));
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			text_username.setText("" + user.getName());
			imageloader.DisplayImage(user.getProfileImageURL().toString(),
					profile_pic);
			dialog.dismiss();

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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dialog.cancel();
	}

}
