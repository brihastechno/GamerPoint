package com.social.gamerpoint;

import Data.databased.Datasource_Handler;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity_Login_Class handle sign In, Sign Up, Database fetch, Session
 * management classes link {@link Activity_MainHome_Activity }
 * {@link Activity_Signup_User},{@link SessionManager },
 * {@link Datasource_Handler }
 * 
 * @author lenovo
 * 
 */
public class Activity_Login_Class extends Activity {
	Button button_login;
	TextView button_signup;
	EditText email, password;
	SessionManager session;
	public static String User_Name, Email, Password, image_path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// set The layout View...
		setContentView(R.layout.activity_login_layout);
		// UI Components...
		button_signup = (TextView) findViewById(R.id.textView_signup);
		button_login = (Button) findViewById(R.id.button_login);

		email = (EditText) findViewById(R.id.edit_user_email);
		password = (EditText) findViewById(R.id.edit_user_password);

		/**
		 * Click Listener on Sign In Button..
		 */
		button_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				session = new SessionManager(getApplicationContext());
				// TODO Auto-generated method stub
				// get text From Edit Text View....
				User_Name = email.getText().toString();
				Email = email.getText().toString();
				Password = password.getText().toString();
				// Get Information form Database ...
				Datasource_Handler mdatabase = new Datasource_Handler(
						getApplicationContext());
				// open the database
				mdatabase.open();
				// fetch cursor with data w.r.t. Email and password in database
				Cursor mCursor = mdatabase.get_all_data(Email, Password);

				mCursor.moveToFirst();
				Log.d("mCursor", "" + mCursor.getCount());
				if (mCursor.getCount() > 0) {
					Log.d("path+++++++++", "" + mCursor.getString(2));
					image_path = mCursor.getString(3);
					Intent i = new Intent(Activity_Login_Class.this,
							Activity_MainHome_Activity.class);
					i.putExtra("image_path", image_path);
					i.putExtra("Email", Email);
					i.putExtra("Password", Password);
					// fetch data and redirect user to main Home Screen...
					startActivity(i);
					// creating Login Session For User and store in Session
					// Manage Class..
					session.createLoginSession(Email, Password, image_path);
					// Log.d("Email_login", "" + Email);
					// Log.d("Password_login", "" + Password);
					// Log.d("image_path", "" + image_path);

					mCursor.moveToNext();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Invalid Email or Username and Password",
							Toast.LENGTH_SHORT).show();
				}
				mCursor.close();

				mdatabase.close();

			}
		});
		/**
		 * Sign up button Listener....redirect the user for Sign Up Screen..
		 */
		button_signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Start The Activity _Sign_Upp...
				Intent it = new Intent(Activity_Login_Class.this,
						Activity_Signup_User.class);
				startActivity(it);

			}
		});
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		return;
	}

}
