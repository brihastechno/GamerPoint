package com.googlesearch.api;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.googlesearch.bean.Result;
import com.social.gamerpoint.Activity_Edit_User_Profile;
import com.social.gamerpoint.Activity_MainHome_Activity;
import com.social.gamerpoint.Edit_User_Profile;
import com.social.gamerpoint.R;

public class Fragment_GoogleSearch_Class extends Fragment implements
		OnClickListener, OnItemClickListener {

	static int count_google;

	private final Search search = new Search();
	private ImageButton ibDoSearch;
	private TextView tvSearch;
	private ListView lvResults;
	private Result result = new Result();
	final Handler handler = new Handler();
	final Runnable updateItems = new Runnable() {
		public void run() {
			updateItemsInUI();
		}
	};
	long login_time, logout_time;
	String Email, Password;
	// ImageView user_image;
	Bitmap bitmap, targetBitmap;
	String image_path;
	Button back;

	// private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		login_time = System.currentTimeMillis();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_googlesearch_api,
				container, false);
		// user_image = (ImageView) view.findViewById(R.id.user_shop_image);
		lvResults = (ListView) view.findViewById(R.id.lvResults);
		lvResults.setOnItemClickListener(this);
		tvSearch = (TextView) view.findViewById(R.id.tvSeach);
		ibDoSearch = (ImageButton) view.findViewById(R.id.ibSearch);
		ibDoSearch.setOnClickListener((android.view.View.OnClickListener) this);
		/*
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
		 * fm.popBackStack(); } } });
		 */
		return view;
	}

	protected Result updateResult(String keywords) {
		Result newResult = new Result();
		try {
			newResult = search.doSearch(keywords);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newResult;
	}

	protected void updateItemsInUI() {
		if (0 == result.getItems().size()) {
			Toast.makeText(getActivity(), "No individual found no result",
					Toast.LENGTH_SHORT).show();
		}
		TutorialsInfoAdapter tutorialsInfoAdapter = new TutorialsInfoAdapter(
				getActivity(), result.getItems());
		lvResults.setAdapter(tutorialsInfoAdapter);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ibSearch:
			final String textToSearch = tvSearch.getText().toString().trim();
			Thread t = new Thread() {
				public void run() {
					result = updateResult(textToSearch);
					handler.post(updateItems);
				}
			};
			Toast.makeText(getActivity(), "Performing Search",
					Toast.LENGTH_SHORT).show();
			t.start();
			hideInputMethod();
			break;
		}
	}

	private void hideInputMethod() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(tvSearch.getWindowToken(), 0);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result
				.getItems().get(arg2).getLink()));
		startActivity(intent);
	}

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
		Cursor mCursor = mdatabase.get_all_slots_with_data("google_seraching",
				formattedDate);
		mCursor.moveToFirst();
		if (mCursor.getCount() > 0) {
			while (!mCursor.isAfterLast()) {
				existing_time = mCursor.getLong(2);
				int column_id = mCursor.getInt(3);
				mdatabase.update_details_day(column_id, "google_seraching",
						formattedDate, (store_time + existing_time));
				mCursor.moveToNext();

			}
			mCursor.close();
		} else {
			mdatabase.insert_details_day("google_seraching", formattedDate,
					store_time);
		}
		mdatabase.close();

	}

}