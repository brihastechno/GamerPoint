package com.social.gamerpoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Data.databased.Datasource_Handler;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlesearch.api.Fragment_GoogleSearch_Class;

@SuppressLint("SimpleDateFormat")
public class Activity_MainHome_Activity extends FragmentActivity implements
		OnClickListener {
	// Menus Button At bottom...and top bar
	Button button_Home, button_Social, button_Games, button_More,
			button_Stores, button_Clouds, button_Videos, mButton_back,
			button_Next, button_Music;

	TextView mTextView_fragment;
	Bitmap bitmap, targetBitmap;

	static FrameLayout frame_Home, frame_Social, frame_Games, frame_More,
			frame_Stores, frame_Clouds, frame_Videos, frame_Next, frame_Music;
	int selcted_tab = 1;
	SharedPreferences myPrefs;
	public static long login_time, logout_time;
	static Datasource_Handler mdatabase;

	// Fragments USe for Display Onn Screen w.r.t Button Click..
	Fragment_Home_Class fragment_home;
	Fragment_SocialTab_Class fragment_social;
	Fragment_Shop_Tab fragment_shop;
	Fragment_GoogleSearch_Class fragment_google;
	Fragment_Cloud_Tab fragment_cloud;
	Fragment_Game_Tab fragment_game;
	Fragment_Video mFragment_Video;
	Fragment_Next_Tab fragment_Next;
	Fragment_Music_Tab fragment_Music;

	ImageView mImageView_set_profile_image;

	public static String image_path;
	public static String Email;
	public static String Password;
	FragmentManager my_fragment_manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// set Layout To Screen..
		setContentView(R.layout.activity_mainhome_layout);

		// get Data from Intent..Email,PAssword, Image Path...
		image_path = getIntent().getStringExtra("image_path");
		Email = getIntent().getStringExtra("Email");
		Password = getIntent().getStringExtra("Password");

		// UI COmponents...
		initialize_components();
		// For Time Tracker get The Login Time....
		login_time = System.currentTimeMillis();
		// Database Objec..
		mdatabase = new Datasource_Handler(this);
		// Fragment Manager interact with Different Fragments In Activities..
		my_fragment_manager = getSupportFragmentManager();
		// set The Change Fragment CHange Listener...As Change in Fragment We
		// have to change the Menus BAckground.. and Text
		my_fragment_manager
				.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged() {
						// TODO Auto-generated method stub
						// call Back Button..
						Call_back_button();
					}
				});

		// imageview====set in action bar============
		mImageView_set_profile_image
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// start Activity for Handle USer To change The
						// Edit_User_Profile
						Intent it = new Intent(Activity_MainHome_Activity.this,
								Edit_User_Profile.class);
						startActivity(it);
					}
				});
		// back button============
		mButton_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// On BAck Button Press add pop stack back that it handle by
				// FragmentManager.OnBackStackChangedListener
				my_fragment_manager.popBackStack();
				if (fragment_home.isVisible()) {
					// finish the activity if Home Screen..
					finish();
				}

			}
		});

		if (findViewById(R.id.main_fragment) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				selcted_tab = savedInstanceState.getInt("selected", 1);
				return;
			}

			// Create an instance of ExampleFragment
			fragment_home = new Fragment_Home_Class(); // home
			fragment_social = new Fragment_SocialTab_Class(); // social
			fragment_game = new Fragment_Game_Tab(); // game
			fragment_shop = new Fragment_Shop_Tab(); // shop or store
			fragment_google = new Fragment_GoogleSearch_Class(); // google
																	// search
			fragment_cloud = new Fragment_Cloud_Tab(); // more or clouds
			mFragment_Video = new Fragment_Video(); // video

			fragment_Next = new Fragment_Next_Tab();
			fragment_Music = new Fragment_Music_Tab();

			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			// fragment_home.setArguments(getIntent().getExtras());

			Change_Fragment_Tab(fragment_home);
			_Handle_Home_Fragment();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Check that the activity is using the layout version with
		// the fragment_container FrameLayout
		// initialize_components();
		select_tab(selcted_tab);

		Email = Activity_MainHome_Activity.Email;
		Datasource_Handler mdatabase = new Datasource_Handler(this);
		mdatabase.open();
		Cursor mCursor = mdatabase.get_image(Email);

		mCursor.moveToFirst();
		Log.d("mCursor", "" + mCursor.getCount());
		if (mCursor.getCount() > 0) {
			Log.d("path+++++++++", "" + mCursor.getString(3));
			image_path = mCursor.getString(3);
			mCursor.moveToNext();
		}
		mCursor.close();

		mdatabase.close();
		if (image_path != null) {
			BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
			optsDownSample.inSampleSize = 3;
			bitmap = BitmapFactory.decodeFile(image_path, optsDownSample);
			getRoundedShape(bitmap);
			mImageView_set_profile_image.setImageBitmap(targetBitmap);
		}

	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		// TODO Auto-generated method stub
		int targetWidth = 200;
		int targetHeight = 200;
		targetBitmap = Bitmap.createBitmap(targetWidth,

		targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

	/**
	 * Initialize UI Components...
	 */
	public void initialize_components() {
		mImageView_set_profile_image = (ImageView) findViewById(R.id.user_shop_image);

		mTextView_fragment = (TextView) findViewById(R.id.Home);
		mButton_back = (Button) findViewById(R.id.Back);

		button_Home = (Button) findViewById(R.id.button_home);
		button_Social = (Button) findViewById(R.id.button_social);
		button_Games = (Button) findViewById(R.id.button_games);
		button_Stores = (Button) findViewById(R.id.button_store);
		button_More = (Button) findViewById(R.id.button_more);
		button_Clouds = (Button) findViewById(R.id.button_clouds);
		button_Videos = (Button) findViewById(R.id.button_video);
		button_Music = (Button) findViewById(R.id.button_music);
		button_Next = (Button) findViewById(R.id.button_next);

		frame_Games = (FrameLayout) findViewById(R.id.frame_games);
		frame_Home = (FrameLayout) findViewById(R.id.frame_home);
		frame_More = (FrameLayout) findViewById(R.id.frame_more);
		frame_Stores = (FrameLayout) findViewById(R.id.frame_store);
		frame_Social = (FrameLayout) findViewById(R.id.frame_social);
		frame_Clouds = (FrameLayout) findViewById(R.id.frame_clouds);
		frame_Videos = (FrameLayout) findViewById(R.id.frame_videos);
		frame_Music = (FrameLayout) findViewById(R.id.frame_music);
		frame_Next = (FrameLayout) findViewById(R.id.frame_next);

		button_Home.setOnClickListener(this);
		button_Social.setOnClickListener(this);
		button_Games.setOnClickListener(this);
		button_Stores.setOnClickListener(this);
		button_More.setOnClickListener(this);
		button_Clouds.setOnClickListener(this);
		button_Videos.setOnClickListener(this);
		button_Music.setOnClickListener(this);
		button_Next.setOnClickListener(this);

		frame_Games.setOnClickListener(this);
		frame_Home.setOnClickListener(this);
		frame_More.setOnClickListener(this);
		frame_Social.setOnClickListener(this);
		frame_Stores.setOnClickListener(this);
		frame_Videos.setOnClickListener(this);
		frame_Clouds.setOnClickListener(this);
		frame_Music.setOnClickListener(this);
		frame_Next.setOnClickListener(this);

	}

	/**
	 * Change The Fragment rplace with New One...
	 * 
	 * @param newFragment
	 */
	public void Change_Fragment_Tab(Fragment newFragment) {
		// Create fragment and give it an argument specifying the article it
		// should show
		if (newFragment.isVisible()) {
			Log.e("fragment _is Visible", "fragment is visibble");
		} else {
			Log.e("fragment _is not Visible", "fragment is not visibble");

			newFragment.setRetainInstance(true);
			FragmentTransaction transaction = my_fragment_manager
					.beginTransaction();
			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack so the user can
			// navigate
			// back
			transaction.replace(R.id.main_fragment, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}

	}

	@Override
	public void onBackPressed() {
		my_fragment_manager.popBackStack();
		if (fragment_home.isVisible()) {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int key = v.getId();
		switch (key) {
		case R.id.button_home:
			_Handle_Home_Fragment();
			break;

		case R.id.frame_home:
			_Handle_Home_Fragment();
			break;

		case R.id.button_social:
			_Handle_Social_Fragment();

			break;
		case R.id.frame_social:
			_Handle_Social_Fragment();
			break;

		case R.id.button_games:
			_Handle_Game_Fragment();
			break;

		case R.id.frame_games:
			_Handle_Game_Fragment();
			break;

		case R.id.button_store:
			_Handle_Shop_Fragment();
			break;

		case R.id.frame_store:
			_Handle_Shop_Fragment();
			break;

		case R.id.button_more:
			_Handle_Google_Fragment();
			break;

		case R.id.frame_more:
			_Handle_Google_Fragment();
			break;

		case R.id.button_clouds:
			_Handle_Cloud_Fragment();
			break;

		case R.id.frame_clouds:
			_Handle_Cloud_Fragment();
			break;

		case R.id.button_video:
			_Handle_Video_Fragment();
			break;

		case R.id.frame_videos:
			_Handle_Video_Fragment();
			break;
		case R.id.frame_next:
			_Handle_Next_Fragment();
			break;
		case R.id.button_next:
			_Handle_Next_Fragment();
			break;
		case R.id.button_music:
			_Handle_Music_Fragment();
			break;
		case R.id.frame_music:
			_Handle_Music_Fragment();
			break;

		default:
			break;
		}
	}

	private void select_tab(int selcted_tab2) {
		// TODO Auto-generated method stub
		switch (selcted_tab2) {
		case 1:
			frame_Home.setBackgroundResource(R.drawable.icon_back_black);
			frame_More.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
			break;
		case 2:
			frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
			frame_More.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Social.setBackgroundResource(R.drawable.icon_back_black);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
			break;
		case 3:
			frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
			frame_More.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Games.setBackgroundResource(R.drawable.icon_back_black);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
			break;
		case 4:
			frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
			frame_More.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_black);
			frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
			break;
		case 5:
			frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
			frame_More.setBackgroundResource(R.drawable.icon_back_black);
			frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
			break;
		case 6:
			frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
			frame_More.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_black);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		case 7:
			frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
			frame_More.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_black);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
			break;
		case 8:
			frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
			frame_More.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_black);
			break;
		case 9:
			frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
			frame_More.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
			frame_Next.setBackgroundResource(R.drawable.icon_back_black);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("selected", selcted_tab);
		// Toast.makeText(getApplicationContext(), "OnsvaedInstance ",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("On destroyed Call", "Destroyed Call");
		long existing_time = 0;
		logout_time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format_with_name = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format_with_name.format(calendar.getTime());
		long store_time = (logout_time - login_time);
		Datasource_Handler mdatabase = new Datasource_Handler(this);
		mdatabase.open();
		Cursor mCursor = mdatabase.get_all_slots_with_data("total",
				formattedDate);
		mCursor.moveToFirst();
		if (mCursor.getCount() > 0) {
			while (!mCursor.isAfterLast()) {
				existing_time = mCursor.getLong(2);
				int column_id = mCursor.getInt(3);
				mdatabase.update_details_day(column_id, "total", formattedDate,
						(store_time + existing_time));
				mCursor.moveToNext();

			}
			mCursor.close();
		} else {
			mdatabase.insert_details_day("total", formattedDate, store_time);
		}
		mdatabase.close();
		myPrefs = getSharedPreferences("fragment_home", 0);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putInt("total_tab", 8);
		prefsEditor.putInt("social_tab", 8);
		prefsEditor.putInt("game_tab", 8);
		prefsEditor.putInt("search_tab", 8);
		prefsEditor.putInt("store_tab", 8);
		prefsEditor.putInt("what_tab", 8);

		prefsEditor.commit();

	}

	public static void update_total_time() {
		Log.d("On destroyed Call", "Destroyed Call");
		long existing_time = 0;
		logout_time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format_with_name = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format_with_name.format(calendar.getTime());
		long store_time = (logout_time - login_time);
		login_time = logout_time;

		mdatabase.open();
		Cursor mCursor = mdatabase.get_all_slots_with_data("total",
				formattedDate);
		mCursor.moveToFirst();
		if (mCursor.getCount() > 0) {
			while (!mCursor.isAfterLast()) {
				existing_time = mCursor.getLong(2);
				int column_id = mCursor.getInt(3);
				mdatabase.update_details_day(column_id, "total", formattedDate,
						(store_time + existing_time));
				mCursor.moveToNext();

			}
			mCursor.close();
		} else {
			mdatabase.insert_details_day("total", formattedDate, store_time);
		}
		mdatabase.close();

	}

	public void Call_back_button() {

		if (fragment_home.isVisible()) {
			Log.e("selcted_tab====111111==== ", "1");

			_Handle_Home_Fragment();
		} else if (fragment_social.isVisible()) {
			Log.e("selcted_tab==2222222===== ", "2");
			_Handle_Social_Fragment();

		} else if (fragment_game.isVisible()) {
			Log.e("selcted_tab==3333333===== ", "3");
			_Handle_Game_Fragment();
		}

		else if (fragment_shop.isVisible()) {
			_Handle_Shop_Fragment();

		} else if (fragment_google.isVisible()) {
			Log.e("selcted_tab===555555==== ", "5");
			_Handle_Google_Fragment();
		}

		else if (fragment_cloud.isVisible()) {
			Log.e("selcted_tab====6666666====== ", "6");
			_Handle_Cloud_Fragment();

		} else if (mFragment_Video.isVisible()) {
			Log.e("selcted_tab====7777777==== ", "7");
			_Handle_Video_Fragment();
		} else if (fragment_Music.isVisible()) {
			Log.e("selcted_tab====7777777==== ", "8");
			_Handle_Music_Fragment();
		} else if (fragment_Next.isVisible()) {
			Log.e("selcted_tab====7777777==== ", "9");
			_Handle_Next_Fragment();
		}

	}

	/**
	 * Functions for Change The Fragment...
	 */

	public void _Handle_Home_Fragment() {
		selcted_tab = 1;
		mTextView_fragment.setText("Home");
		frame_Home.setBackgroundResource(R.drawable.icon_back_black);
		frame_More.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		Change_Fragment_Tab(fragment_home);
	}

	public void _Handle_Social_Fragment() {
		selcted_tab = 2;
		mTextView_fragment.setText("Social");
		frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
		frame_More.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Social.setBackgroundResource(R.drawable.icon_back_black);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);

		frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		Change_Fragment_Tab(fragment_social);
	}

	public void _Handle_Game_Fragment() {

		selcted_tab = 3;
		mTextView_fragment.setText("Game");
		frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
		frame_More.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Games.setBackgroundResource(R.drawable.icon_back_black);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		Change_Fragment_Tab(fragment_game);
	}

	public void _Handle_Shop_Fragment() {
		selcted_tab = 4;
		mTextView_fragment.setText("Store");
		frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
		frame_More.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_black);
		frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		Change_Fragment_Tab(fragment_shop);
	}

	public void _Handle_Google_Fragment() {
		selcted_tab = 5;
		mTextView_fragment.setText("Search");
		frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
		frame_More.setBackgroundResource(R.drawable.icon_back_black);
		frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		Change_Fragment_Tab(fragment_google);
	}

	public void _Handle_Cloud_Fragment() {
		selcted_tab = 6;
		mTextView_fragment.setText("Cloud");
		frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
		frame_More.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_black);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		Change_Fragment_Tab(fragment_cloud);
	}

	public void _Handle_Video_Fragment() {

		selcted_tab = 7;
		mTextView_fragment.setText("GamerPointsTV");
		frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
		frame_More.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_black);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Music.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		Change_Fragment_Tab(mFragment_Video);
	}

	public void _Handle_Music_Fragment() {

		selcted_tab = 8;
		mTextView_fragment.setText("Music");
		frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
		frame_More.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);

		frame_Next.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Music.setBackgroundResource(R.drawable.icon_back_black);
		Change_Fragment_Tab(fragment_Music);
	}

	public void _Handle_Next_Fragment() {

		selcted_tab = 9;
		mTextView_fragment.setText("Next");
		frame_Home.setBackgroundResource(R.drawable.icon_back_grey);
		frame_More.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Social.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Stores.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Games.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Videos.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Clouds.setBackgroundResource(R.drawable.icon_back_grey);
		frame_Music.setBackgroundResource(R.drawable.icon_back_grey);

		frame_Next.setBackgroundResource(R.drawable.icon_back_black);
		Change_Fragment_Tab(fragment_Next);
	}

}
