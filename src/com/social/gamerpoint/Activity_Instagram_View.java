package com.social.gamerpoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.instagram.InstagramApp;
import com.instagram.InstagramApp.OAuthAuthenticationListener;
import com.instagram.app.Activity_Instagram_Application_Tab;
import com.instagram.app.Instagram_Application_Data;

public class Activity_Instagram_View extends Activity {

	public static final String APIURL = "https://api.instagram.com/v1";
	public static String CALLBACKURL = "instagram://connect";
	String authURLString, tokenURLString;
	String request_token;

	Button button_login;

	InstagramApp mInstagramApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instagram_layout);

		mInstagramApp = new InstagramApp(this,
				Instagram_Application_Data.CLIENT_ID,
				Instagram_Application_Data.CLIENT_SECRET,
				Instagram_Application_Data.CALLBACK_URL);
		mInstagramApp.setListener(listener);
		button_login = (Button) findViewById(R.id.login_button);
		button_login.setText("Log In");

		if (mInstagramApp.hasAccessToken()) {
			Intent it = new Intent(Activity_Instagram_View.this,
					Activity_Instagram_Application_Tab.class);
			it.putExtra("is_follow", false);
			it.putExtra("string_id", mInstagramApp.getId());
			it.putExtra("string_token", mInstagramApp.get_access_token());
			Log.e("====================", "" + mInstagramApp.get_access_token());
			startActivity(it);
			finish();
		}

		button_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mInstagramApp.hasAccessToken()) {
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							Activity_Instagram_View.this);
					builder.setMessage("Disconnect from Instagram?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											mInstagramApp.resetAccessToken();
											button_login.setText("Log In");

										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					final AlertDialog alert = builder.create();
					alert.show();
				} else {
					mInstagramApp.authorize();
				}
			} // TODO Auto-generated method stub

		});

	}

	OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {
		@Override
		public void onSuccess() {

			button_login.setText("Log Out");
			Intent it = new Intent(Activity_Instagram_View.this,
					Activity_Instagram_Application_Tab.class);
			it.putExtra("is_follow", false);
			it.putExtra("string_id", mInstagramApp.getId());
			it.putExtra("string_token", mInstagramApp.get_access_token());
			Log.e("====================", "" + mInstagramApp.get_access_token());
			startActivity(it);
			finish();

		}

		@Override
		public void onFail(String error) {
			Toast.makeText(Activity_Instagram_View.this, error,
					Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// mInstagramApp.resetAccessToken();
	}

}
