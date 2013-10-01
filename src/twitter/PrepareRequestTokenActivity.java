package twitter;

import java.util.ArrayList;

import com.social.gamerpoint.R;
import com.twitter_app.Actvity_twitter_Tab_Main_Class;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.http.HttpParameters;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Prepares a OAuthConsumer and OAuthProvider
 * 
 * OAuthConsumer is configured with the consumer key & consumer secret.
 * OAuthProvider is configured with the 3 OAuth endpoints.
 * 
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the
 * request.
 * 
 * After the request is authorized, a callback is made here.
 * 
 */
public class PrepareRequestTokenActivity extends Activity {
	Button mButton_login;
	ArrayList<String> array;
	boolean token_access = false;

	public static SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	String mString_post;
	ProgressDialog dialog;
	final String TAG = getClass().getName();
	private OAuthConsumer consumer;
	private OAuthProvider provider;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tweet);
		dialog = new ProgressDialog(this);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		dialog.show();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (TwitterUtils.isAuthenticated(prefs)) {
			Intent mIntent_tabs = new Intent(PrepareRequestTokenActivity.this,
					Actvity_twitter_Tab_Main_Class.class);
			dialog.cancel();
			startActivity(mIntent_tabs);
			finish();

		} else {

			call_for_Token();
		}

	}

	//
	// private void logout() {
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(this);
	// final Editor edit = prefs.edit();
	// edit.remove(OAuth.OAUTH_TOKEN);
	// edit.remove(OAuth.OAUTH_TOKEN_SECRET);
	// edit.commit();
	// }

	public void call_for_Token() {
		try {
			this.consumer = new CommonsHttpOAuthConsumer(
					Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
			this.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,
					Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
		} catch (Exception e) {
			Log.e(TAG, "Error creating consumer / provider", e);
		}

		Log.i(TAG, "Starting task to retrieve request token.");

		new OAuthRequestTokenTask(this, consumer, provider).execute();

	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the
	 * request token). The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		dialog.show();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		if (uri != null
				&& uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i(TAG, "Callback received : " + uri);
			Log.i(TAG, "Retrieving Access Token");
			new RetrieveAccessTokenTask(this, consumer, provider, prefs)
					.execute(uri);

		}
	}

	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

		private Context context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;

		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,
				OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs = prefs;
		}

		/**
		 * Retrieve the oauth_verifier, and store the oauth and
		 * oauth_token_secret for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri... params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri
					.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);
				HttpParameters params1 = provider.getResponseParameters();
				String userName = params1.getFirst("screen_name");
				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET,
						consumer.getTokenSecret());
				edit.putString("screen_name", userName);
				edit.commit();
				Log.i(TAG, "OATH TOKEN RECIEVED");
				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

				consumer.setTokenWithSecret(token, secret);

				// context.startActivity(new
				// Intent(context,TwitterpostActivity.class));

				token_access = true;

				Log.i(TAG, "OAuth - Access Token Retrieved");

			} catch (Exception e) {
				token_access = false;
				Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (token_access) {
				Intent mIntent_tabs = new Intent(
						PrepareRequestTokenActivity.this,
						Actvity_twitter_Tab_Main_Class.class);
				dialog.cancel();
				startActivity(mIntent_tabs);
				finish();

			} else {
				dialog.cancel();
				Toast.makeText(getApplicationContext(), "Unable To Login",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

	}

}
