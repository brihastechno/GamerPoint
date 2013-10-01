package com.social.gamerpoint;

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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Fragment_More_Tab extends Fragment {

	long login_time, logout_time;
	// private TextView textView;
	SharedPreferences myPrefs;
	ImageView user_image;
	String Email, Password;
	Bitmap bitmap, targetBitmap;
	String image_path;
	Button back;
	static int more_count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		login_time = System.currentTimeMillis();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_shop_layout, container,
				false);
		/*
		 * user_image = (ImageView) view.findViewById(R.id.user_shop_image);
		 * user_image.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent it = new Intent(getActivity(), Edit_User_Profile.class);
		 * startActivity(it); } });
		 * 
		 * back = (Button) view.findViewById(R.id.Back);
		 * back.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub FragmentManager fm = getActivity().getSupportFragmentManager();
		 * Log.d("fm.getBackStackEntryCount()", "" +
		 * fm.getBackStackEntryCount()); if (fm.getBackStackEntryCount() > 0) {
		 * fm.popBackStack(); Log.e("more_count===", "" + more_count); } } });
		 */

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("On FragmentCall", "FragmentCall Call");
		long existing_time = 0;
		logout_time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format_with_name = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format_with_name.format(calendar.getTime());
		long store_time = (logout_time - login_time);
		Datasource_Handler mdatabase = new Datasource_Handler(getActivity());
		mdatabase.open();
		Cursor mCursor = mdatabase.get_all_slots_with_data("earning",
				formattedDate);
		mCursor.moveToFirst();
		if (mCursor.getCount() > 0) {
			while (!mCursor.isAfterLast()) {
				existing_time = mCursor.getLong(2);
				int column_id = mCursor.getInt(3);
				mdatabase.update_details_day(column_id, "earning",
						formattedDate, (store_time + existing_time));
				mCursor.moveToNext();

			}
			mCursor.close();
		} else {
			mdatabase.insert_details_day("earning", formattedDate, store_time);
		}
		mdatabase.close();
	}
}
