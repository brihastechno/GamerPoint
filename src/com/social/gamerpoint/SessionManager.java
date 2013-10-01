package com.social.gamerpoint;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "Login_status";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_Email = "Email";
	public static final String KEY_User_Name = "User_Name";

	// Email address (make variable public to access from outside)
	public static final String KEY_Password = "Password";
	public static final String KEY_image_path = "image_path";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();

	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String email, String password,
			String image_path) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref

		editor.putString(KEY_Email, email);
		editor.commit();
		// Storing email in pref
		editor.putString(KEY_Password, password);
		// commit changes
		editor.commit();
		editor.putString(KEY_image_path, image_path);
		editor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public Boolean checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, Activity_Login_Class.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
			return true;
		}
		return false;

	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();

		user.put(KEY_Email, pref.getString(KEY_Email, null));

		// user email id
		user.put(KEY_Password, pref.getString(KEY_Password, null));
		user.put(KEY_image_path, pref.getString(KEY_image_path, null));

		Log.d("pref.getString(KEY_Email, null)",
				"" + pref.getString(KEY_Email, null));
		Log.d("pref.getString(KEY_Password, null)",
				"" + pref.getString(KEY_Password, null));
		Log.d("pref.getString(KEY_image_path, null)",
				"" + pref.getString(KEY_image_path, null));
		// return user
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, Activity_Login_Class.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
