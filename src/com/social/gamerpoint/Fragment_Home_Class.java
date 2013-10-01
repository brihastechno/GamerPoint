package com.social.gamerpoint;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Data.databased.Datasource_Handler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_Home_Class extends Fragment implements OnClickListener {
	SharedPreferences myPrefs;
	NumberFormat number_format;
	LinearLayout linear_earnings, linear_social, linear_store_shop,
			linear_game_played, linear_total_time, linear_google_serach;
	TextView textView_earnings, textView_social_media, textView_store,
			textView_game_played, trextView_total_time, textView_searching,
			home;
	Button button_Home, button_Social, button_Games, button_More,
			button_Stores, button_Clouds, button_Videos;
	Button back;
	// ImageView user_image;
	Datasource_Handler mDatabase;
	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;
	public long login_time, logout_time;
	String Email, Password;
	SessionManager session;
	Bitmap bitmap, targetBitmap;
	String image_path;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mDatabase = new Datasource_Handler(getActivity());
		Activity_MainHome_Activity.update_total_time();
		// login_time = System.currentTimeMillis();
		// login_time = System.currentTimeMillis();

		View view = inflater.inflate(R.layout.fragment_home_layout_, container,
				false);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/HelveticaNeue.ttf");
		number_format = NumberFormat.getNumberInstance();
		number_format.setGroupingUsed(true);
		number_format.setMaximumFractionDigits(2);

		linear_earnings = (LinearLayout) view
				.findViewById(R.id.linear_earnings);
		linear_total_time = (LinearLayout) view
				.findViewById(R.id.linear_total_time_spent);
		linear_social = (LinearLayout) view
				.findViewById(R.id.linear_social_media);
		linear_store_shop = (LinearLayout) view
				.findViewById(R.id.linear_store_shopp);
		linear_game_played = (LinearLayout) view
				.findViewById(R.id.linear_game_played);
		linear_google_serach = (LinearLayout) view
				.findViewById(R.id.linear_google_search);

		// back = (Button) view.findViewById(R.id.Back);
		// user_image = (ImageView) view.findViewById(R.id.user_shop_image);
		// home = (TextView) view.findViewById(R.id.Home);
		textView_earnings = (TextView) view.findViewById(R.id.textView_earning);
		textView_social_media = (TextView) view
				.findViewById(R.id.textView_social_media);
		textView_store = (TextView) view
				.findViewById(R.id.textView_store_shopp);
		textView_game_played = (TextView) view
				.findViewById(R.id.textView_game_played);
		trextView_total_time = (TextView) view
				.findViewById(R.id.textView_total_time_spent);
		textView_searching = (TextView) view.findViewById(R.id.textView_search);

		// back.setTypeface(typeFace);
		// home.setTypeface(typeFace);
		textView_earnings.setTypeface(typeFace);
		textView_social_media.setTypeface(typeFace);
		textView_store.setTypeface(typeFace);
		textView_game_played.setTypeface(typeFace);
		trextView_total_time.setTypeface(typeFace);
		textView_searching.setTypeface(typeFace);

		textView_earnings.setOnClickListener(this);
		textView_social_media.setOnClickListener(this);
		textView_store.setOnClickListener(this);
		textView_game_played.setOnClickListener(this);
		trextView_total_time.setOnClickListener(this);
		textView_searching.setOnClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("onResume", "onResume");
		if (isVisible()) {
			Log.d("isVisible", "isVisible");

		}
		myPrefs = getActivity().getSharedPreferences("fragment_home", 0);
		if (myPrefs != null) {
			linear_total_time.setVisibility(myPrefs.getInt("total_tab", 8));
			linear_game_played.setVisibility(myPrefs.getInt("game_tab", 8));
			linear_google_serach.setVisibility(myPrefs.getInt("search_tab", 8));
			linear_social.setVisibility(myPrefs.getInt("social_tab", 8));
			linear_store_shop.setVisibility(myPrefs.getInt("store_tab", 8));
			linear_earnings.setVisibility(myPrefs.getInt("what_tab", 8));
			layout_include(linear_earnings, "earning");
			layout_include(linear_social, "social");
			layout_include(linear_store_shop, "searching");
			layout_include(linear_game_played, "gaming");
			layout_include(linear_total_time, "total");
			layout_include(linear_google_serach, "google_seraching");
		}

		/*
		 * Email = Activity_MainHome_Activity.Email;
		 * Log.d("Activity_MainHome_ActivityEmail", "" + Email);
		 * Datasource_Handler mdatabase = new Datasource_Handler(getActivity());
		 * mdatabase.open(); Cursor mCursor = mdatabase.get_image(Email);
		 * 
		 * mCursor.moveToFirst(); Log.d("mCursor", "" + mCursor.getCount()); if
		 * (mCursor.getCount() > 0) { Log.d("path+++++++++", "" +
		 * mCursor.getString(3)); image_path = mCursor.getString(3);
		 * Log.d("image_path", "" + image_path); mCursor.moveToNext(); }
		 * mCursor.close();
		 * 
		 * mdatabase.close(); if (image_path != null) { BitmapFactory.Options
		 * optsDownSample = new BitmapFactory.Options();
		 * optsDownSample.inSampleSize = 3;
		 * 
		 * bitmap = BitmapFactory.decodeFile(image_path, optsDownSample);
		 * getRoundedShape(bitmap); //user_image.setImageBitmap(targetBitmap); }
		 */
	}

	/*
	 * public Bitmap getRoundedShape(Bitmap scaleBitmapImage) { // TODO
	 * Auto-generated method stub int targetWidth = 200; int targetHeight = 200;
	 * targetBitmap = Bitmap.createBitmap(targetWidth,
	 * 
	 * targetHeight, Bitmap.Config.ARGB_8888);
	 * 
	 * Canvas canvas = new Canvas(targetBitmap); Path path = new Path();
	 * path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1)
	 * / 2, (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
	 * Path.Direction.CCW);
	 * 
	 * canvas.clipPath(path); Bitmap sourceBitmap = scaleBitmapImage;
	 * canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
	 * sourceBitmap.getHeight()), new Rect(0, 0, targetWidth, targetHeight),
	 * null); return targetBitmap; }
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int key = v.getId();
		switch (key) {
		case R.id.textView_earning:
			if (linear_earnings.getVisibility() == View.VISIBLE) {
				linear_earnings.setVisibility(View.GONE);
				// update_total_time();
			} else {
				linear_earnings.setVisibility(View.VISIBLE);
				linear_social.setVisibility(View.GONE);
				linear_store_shop.setVisibility(View.GONE);
				linear_game_played.setVisibility(View.GONE);
				linear_total_time.setVisibility(View.GONE);
				linear_google_serach.setVisibility(View.GONE);
				layout_include(linear_earnings, "earning");
				// update_total_time();
				login_time = System.currentTimeMillis();

			}
			break;

		case R.id.textView_social_media:
			if (linear_social.getVisibility() == View.VISIBLE) {
				linear_social.setVisibility(View.GONE);
				// update_total_time();

			} else {
				linear_social.setVisibility(View.VISIBLE);
				linear_earnings.setVisibility(View.GONE);
				linear_store_shop.setVisibility(View.GONE);
				linear_game_played.setVisibility(View.GONE);
				linear_total_time.setVisibility(View.GONE);
				linear_google_serach.setVisibility(View.GONE);
				layout_include(linear_social, "social");
				// update_total_time();
				// login_time = System.currentTimeMillis();
			}
			break;
		case R.id.textView_store_shopp:
			if (linear_store_shop.getVisibility() == View.VISIBLE) {
				linear_store_shop.setVisibility(View.GONE);
				// update_total_time();
			} else {
				linear_earnings.setVisibility(View.GONE);
				linear_social.setVisibility(View.GONE);
				linear_game_played.setVisibility(View.GONE);
				linear_total_time.setVisibility(View.GONE);
				linear_store_shop.setVisibility(View.VISIBLE);
				linear_google_serach.setVisibility(View.GONE);
				layout_include(linear_store_shop, "searching");
				// update_total_time();
				// login_time = System.currentTimeMillis();
			}
			break;
		case R.id.textView_game_played:
			if (linear_game_played.getVisibility() == View.VISIBLE) {
				linear_game_played.setVisibility(View.GONE);
				// update_total_time();
			} else {
				linear_earnings.setVisibility(View.GONE);
				linear_social.setVisibility(View.GONE);
				linear_store_shop.setVisibility(View.GONE);
				linear_total_time.setVisibility(View.GONE);
				linear_game_played.setVisibility(View.VISIBLE);
				linear_google_serach.setVisibility(View.GONE);
				layout_include(linear_game_played, "gaming");

				// update_total_time();
				// login_time = System.currentTimeMillis();
			}
			break;
		case R.id.textView_total_time_spent:
			if (linear_total_time.getVisibility() == View.VISIBLE) {
				linear_total_time.setVisibility(View.GONE);
				Activity_MainHome_Activity.update_total_time();
				;
			} else {
				linear_total_time.setVisibility(View.VISIBLE);
				linear_earnings.setVisibility(View.GONE);
				linear_social.setVisibility(View.GONE);
				linear_store_shop.setVisibility(View.GONE);
				linear_game_played.setVisibility(View.GONE);
				linear_google_serach.setVisibility(View.GONE);
				Activity_MainHome_Activity.update_total_time();
				layout_include(linear_total_time, "total");

			}
			break;
		case R.id.textView_search:
			if (linear_google_serach.getVisibility() == View.VISIBLE) {
				linear_google_serach.setVisibility(View.GONE);
				// update_total_time();
				// login_time = System.currentTimeMillis();
			} else {
				linear_earnings.setVisibility(View.GONE);
				linear_social.setVisibility(View.GONE);
				linear_game_played.setVisibility(View.GONE);
				linear_total_time.setVisibility(View.GONE);
				linear_store_shop.setVisibility(View.GONE);
				linear_google_serach.setVisibility(View.VISIBLE);
				layout_include(linear_google_serach, "google_seraching");

			}

			break;

		default:
			break;
		}
	}

	public void layout_include(LinearLayout linear_layout, String layout_id) {
		long time_stamp = 0;
		long time_stamp_today = 0;
		long time_stamp_month = 0;

		mDatabase.open();
		Cursor mCursor = mDatabase.get_all_slots(layout_id);

		mCursor.moveToFirst();
		int i = 0;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format_with_name = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format_with_name.format(calendar.getTime());

		Log.e("Value of Time Stamp", "" + mCursor.getCount());
		while (!mCursor.isAfterLast()) {

			time_stamp += mCursor.getLong(2);
			String date = mCursor.getString(1);

			Log.e("Value of Time Stamp " + time_stamp, "" + date
					+ " formattedDate " + formattedDate);
			if (date.equalsIgnoreCase(formattedDate)) {
				time_stamp_today = time_stamp;
				Log.e("in IFfff ", "in IFFFFFFF");
			} else {
				// time_stamp_today = 0;
				Log.e("in elsee ", "in elseeee");
			}
			if (i == 29) {
				time_stamp_month = time_stamp;
			}
			i++;
			mCursor.moveToNext();

		}
		mCursor.close();
		mDatabase.close();

		if (time_stamp_month == 0) {
			time_stamp_month = time_stamp;
		}

		LayoutInflater linflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/HelveticaNeue.ttf");
		// remove all view every time when update methods call...
		linear_layout.removeAllViews();
		final View customView = linflater.inflate(
				R.layout.custom_home_tab_list, null);

		TextView Total_today_days = (TextView) customView
				.findViewById(R.id.textView_total_time_day);
		TextView today_days_label = (TextView) customView
				.findViewById(R.id.textView_today_day_label);
		TextView today_hours_label = (TextView) customView
				.findViewById(R.id.textView_today_hour_label);
		TextView today_minute_label = (TextView) customView
				.findViewById(R.id.textView_today_minute_label);
		TextView today_second_label = (TextView) customView
				.findViewById(R.id.textView_today_second_label);
		TextView today_days = (TextView) customView
				.findViewById(R.id.textView_today_day);
		TextView today_hours = (TextView) customView
				.findViewById(R.id.textView_today_hour);
		TextView today_minutes = (TextView) customView
				.findViewById(R.id.textView_today_minutes);
		TextView today_seconds = (TextView) customView
				.findViewById(R.id.textView_today_seconds);

		TextView Total_month_today = (TextView) customView
				.findViewById(R.id.textView_total_time_month);
		TextView today_month_label = (TextView) customView
				.findViewById(R.id.textView_month_day_label);
		TextView month_hours_label = (TextView) customView
				.findViewById(R.id.textView_month_hour_label);
		TextView month_minute_label = (TextView) customView
				.findViewById(R.id.textView_month_minute_label);
		TextView month_second_label = (TextView) customView
				.findViewById(R.id.textView_month_second_label);
		TextView month_day = (TextView) customView
				.findViewById(R.id.textView_last_month_days);
		TextView month_hours = (TextView) customView
				.findViewById(R.id.textView_last_month_hours);
		TextView month_minutes = (TextView) customView
				.findViewById(R.id.textView_last_month_minutes);
		TextView month_seconds = (TextView) customView
				.findViewById(R.id.textView_last_month_seconds);

		TextView Total_year_today = (TextView) customView
				.findViewById(R.id.textView_total_time_year);
		TextView today_year_label = (TextView) customView
				.findViewById(R.id.textView_year_day_label);
		TextView year_hours_label = (TextView) customView
				.findViewById(R.id.textView_year_hour_label);
		TextView year_minute_label = (TextView) customView
				.findViewById(R.id.textView_year_minute_label);
		TextView year_second_label = (TextView) customView
				.findViewById(R.id.textView_year_second_label);
		TextView year_day = (TextView) customView
				.findViewById(R.id.textView_year_days);
		TextView year_hours = (TextView) customView
				.findViewById(R.id.textView_year_hours);
		TextView year_minutes = (TextView) customView
				.findViewById(R.id.textView_year_minutes);
		TextView year_seconds = (TextView) customView
				.findViewById(R.id.textView_year_seconds);

		// set Fonts of text

		Total_today_days.setTypeface(typeFace);
		today_days_label.setTypeface(typeFace);
		today_hours_label.setTypeface(typeFace);
		today_minute_label.setTypeface(typeFace);
		today_second_label.setTypeface(typeFace);
		today_days.setTypeface(typeFace);
		today_hours.setTypeface(typeFace);
		today_minutes.setTypeface(typeFace);
		today_seconds.setTypeface(typeFace);

		Total_month_today.setTypeface(typeFace);
		today_month_label.setTypeface(typeFace);
		month_hours_label.setTypeface(typeFace);
		month_minute_label.setTypeface(typeFace);
		month_second_label.setTypeface(typeFace);
		month_day.setTypeface(typeFace);
		month_hours.setTypeface(typeFace);
		month_minutes.setTypeface(typeFace);
		month_seconds.setTypeface(typeFace);

		Total_year_today.setTypeface(typeFace);
		today_year_label.setTypeface(typeFace);
		year_hours_label.setTypeface(typeFace);
		year_minute_label.setTypeface(typeFace);
		year_second_label.setTypeface(typeFace);
		year_day.setTypeface(typeFace);
		year_hours.setTypeface(typeFace);
		year_minutes.setTypeface(typeFace);
		year_seconds.setTypeface(typeFace);

		if (time_stamp_today > 0) {

			// TODO: this is the value in ms
			long ms = time_stamp_today;
			if (ms > DAY) {
				// text.append(ms / DAY).append(" days ");
				today_days.setText("" + ms / DAY);
				ms %= DAY;
			}
			if (ms > HOUR) {
				// text.append(ms / HOUR).append(" hours ");
				today_hours.setText("" + ms / HOUR);
				ms %= HOUR;
			}
			if (ms > MINUTE) {
				// text.append(ms / MINUTE).append(" minutes ");
				today_minutes.setText("" + ms / MINUTE);
				ms %= MINUTE;
			}
			if (ms > SECOND) {
				// text.append(ms / SECOND).append(" seconds ");
				today_seconds.setText("" + ms / SECOND);
				ms %= SECOND;
			}
			// text.append(ms + " ms");
			// System.out.println(text.toString());
		} else {
			today_days.setText("0");
			today_hours.setText("0");
			today_minutes.setText("0");
			today_seconds.setText("0");
		}

		if (time_stamp_month > 0) {

			// TODO: this is the value in ms
			long ms = time_stamp_month;
			if (ms > DAY) {
				// text.append(ms / DAY).append(" days ");
				month_day.setText("" + ms / DAY);
				ms %= DAY;
			}
			if (ms > HOUR) {
				// text.append(ms / HOUR).append(" hours ");
				month_hours.setText("" + ms / HOUR);
				ms %= HOUR;
			}
			if (ms > MINUTE) {
				// text.append(ms / MINUTE).append(" minutes ");
				month_minutes.setText("" + ms / MINUTE);
				ms %= MINUTE;
			}
			if (ms > SECOND) {
				// text.append(ms / SECOND).append(" seconds ");
				month_seconds.setText("" + ms / SECOND);
				ms %= SECOND;
			}
			// text.append(ms + " ms");
			// System.out.println(text.toString());
		} else {
			month_day.setText("0");
			month_hours.setText("0");
			month_minutes.setText("0");
			month_seconds.setText("0");
		}

		if (time_stamp > 0) {

			// TODO: this is the value in ms
			long ms = time_stamp;
			if (ms > DAY) {
				// text.append(ms / DAY).append(" days ");
				year_day.setText("" + ms / DAY);
				ms %= DAY;
			}
			if (ms > HOUR) {
				// text.append(ms / HOUR).append(" hours ");
				year_hours.setText("" + ms / HOUR);
				ms %= HOUR;
			}
			if (ms > MINUTE) {
				// text.append(ms / MINUTE).append(" minutes ");
				year_minutes.setText("" + ms / MINUTE);
				ms %= MINUTE;
			}
			if (ms > SECOND) {
				// text.append(ms / SECOND).append(" seconds ");
				year_seconds.setText("" + ms / SECOND);
				ms %= SECOND;
			}
			// text.append(ms + " ms");
			// System.out.println(text.toString());
		} else {
			year_day.setText("0");
			year_hours.setText("0");
			year_minutes.setText("0");
			year_seconds.setText("0");
		}

		linear_layout.addView(customView);

	}

	public void update_total_time() {
		long existing_time = 0;
		logout_time = System.currentTimeMillis();
		Activity_MainHome_Activity.login_time = logout_time;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format_with_name = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format_with_name.format(calendar.getTime());
		long store_time = (logout_time - login_time);
		Datasource_Handler mdatabase = new Datasource_Handler(getActivity());
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

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Activity_MainHome_Activity.update_total_time();
		login_time = System.currentTimeMillis();

		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putInt("total_tab", linear_total_time.getVisibility());
		prefsEditor.putInt("social_tab", linear_social.getVisibility());
		prefsEditor.putInt("game_tab", linear_game_played.getVisibility());
		prefsEditor.putInt("search_tab", linear_google_serach.getVisibility());
		prefsEditor.putInt("store_tab", linear_store_shop.getVisibility());
		prefsEditor.putInt("what_tab", linear_earnings.getVisibility());

		prefsEditor.commit();
	}

}
