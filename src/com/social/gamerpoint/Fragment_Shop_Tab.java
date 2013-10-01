package com.social.gamerpoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Data.databased.Datasource_Handler;
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
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.picture.slides.ReflectingImageAdapter;
import com.picture.slides.ResourceImageAdapter_Shop;

public class Fragment_Shop_Tab extends Fragment {
	Button button_facebook, button_tweetor, button_instagram,
			button_pinterrest, back;
	static int image;
	Fragment_Inside_Shop mFragment_Inside_Shop;
	FragmentTransaction mFragmentTransaction;

	// private TextView textView;
	int[] bitmap_path = new int[] { R.drawable.image_thum1,
			R.drawable.image_thumb2, R.drawable.image_thumb3,
			R.drawable.image_thumb4, R.drawable.image_thumb2, };
	String[] title = new String[] { "MERCIES", "SPACED OUT SPRING SHOWCASE",
			"COLD BLOOD CLUB 'WHITE BOYZ' VIDEO RELEASE PARTY",
			"THE RUBY SUNS", "The Mask" };
	String[] artist = new String[] {
			"LILY & THE PARLOUR TRICKSTHE HOLLOWSAT SEA",
			"TR!BE GVNGPERRIONSAM SIEGELSALOMON FAYECOOPER RIVERSWATI HERUM.WILL",
			"KNITTING FACTORY", "PAINTED PALMSNORTH HIGHLANDS",
			"KNITTING FACTORY" };
	String[] time = new String[] { "DOORS: 7:00 PM / SHOW: 7:30 PM",
			"DOORS: 9:00 PM / SHOW: 9:00 PM", "DOORS: 6:00 PM / SHOW: 7:00 PM",
			"DOORS: 11:55 PM / SHOW: 11:55 PM",
			"DOORS: 9:00 PM / SHOW: 9:00 PM" };
	String[] price = new String[] { "30$", "40$", "10$", "15$", "30$" };

	long login_time, logout_time;
	// private TextView textView;
	SharedPreferences myPrefs;
	Bitmap bitmap, targetBitmap;
	String image_path, Email, Password;

	// ImageView user_image;

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
		 * Email = Activity_MainHome_Activity.Email; Datasource_Handler
		 * mdatabase = new Datasource_Handler(getActivity()); mdatabase.open();
		 * Cursor mCursor = mdatabase.get_image(Email);
		 * 
		 * mCursor.moveToFirst(); Log.d("mCursor", "" + mCursor.getCount()); if
		 * (mCursor.getCount() > 0) { Log.d("path+++++++++", "" +
		 * mCursor.getString(3)); image_path = mCursor.getString(3);
		 * mCursor.moveToNext(); } mCursor.close();
		 * 
		 * mdatabase.close(); if (image_path != null) { BitmapFactory.Options
		 * optsDownSample = new BitmapFactory.Options();
		 * optsDownSample.inSampleSize = 3; bitmap =
		 * BitmapFactory.decodeFile(image_path, optsDownSample);
		 * getRoundedShape(bitmap);
		 * Activity_MainHome_Activity.mImageView_set_profile_image
		 * .setImageBitmap(targetBitmap); }
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

		View view = inflater.inflate(R.layout.fragment_shop_layoutsec,
				container, false);
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
		 * fm.popBackStack(); Log.e("shop_count===", "" + shop_count); } } });
		 */

		final CoverFlow coverFlow1 = (CoverFlow) view.findViewById(this
				.getResources().getIdentifier("coverflow", "id",
						"com.social.gamerpoint"));
		setupCoverFlow(coverFlow1, false);
		return view;
	}

	/**
	 * Setup cover flow.
	 * 
	 * @param mCoverFlow
	 *            the m cover flow
	 * @param reflect
	 *            the reflect
	 */
	private void setupCoverFlow(final CoverFlow mCoverFlow,
			final boolean reflect) {
		BaseAdapter coverImageAdapter;
		if (reflect) {
			coverImageAdapter = new ReflectingImageAdapter(
					new ResourceImageAdapter_Shop(getActivity()));
		} else {
			coverImageAdapter = new ResourceImageAdapter_Shop(getActivity());
		}
		mCoverFlow.setAdapter(coverImageAdapter);
		mCoverFlow.setSelection(2, true);
		setupListeners(mCoverFlow);
	}

	/**
	 * Sets the up listeners.
	 * 
	 * @param mCoverFlow
	 *            the new up listeners
	 */
	private void setupListeners(final CoverFlow mCoverFlow) {
		mCoverFlow.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {

				int key = position;
				/*
				 * Intent intent_list = new Intent(getActivity(),
				 * Activity_Shop_List.class);
				 */
				switch (key) {
				case 0:
					image = R.drawable.icon_shop;

					mFragment_Inside_Shop = new Fragment_Inside_Shop();

					mFragmentTransaction = getActivity()
							.getSupportFragmentManager().beginTransaction();

					mFragmentTransaction.replace(R.id.main_fragment,
							mFragment_Inside_Shop);
					mFragmentTransaction.addToBackStack(null);

					mFragmentTransaction.commit();

					break;
				case 1:
					image = R.drawable.icon_shop1;

					mFragment_Inside_Shop = new Fragment_Inside_Shop();

					mFragmentTransaction = getActivity()
							.getSupportFragmentManager().beginTransaction();

					mFragmentTransaction.replace(R.id.main_fragment,
							mFragment_Inside_Shop);
					mFragmentTransaction.addToBackStack(null);

					mFragmentTransaction.commit();
					break;
				case 2:
					image = R.drawable.icon_shop;

					mFragment_Inside_Shop = new Fragment_Inside_Shop();

					mFragmentTransaction = getActivity()
							.getSupportFragmentManager().beginTransaction();

					mFragmentTransaction.replace(R.id.main_fragment,
							mFragment_Inside_Shop);
					mFragmentTransaction.addToBackStack(null);

					mFragmentTransaction.commit();
					break;
				case 3:
					image = R.drawable.icon_shop2;

					mFragment_Inside_Shop = new Fragment_Inside_Shop();

					mFragmentTransaction = getActivity()
							.getSupportFragmentManager().beginTransaction();

					mFragmentTransaction.replace(R.id.main_fragment,
							mFragment_Inside_Shop);
					mFragmentTransaction.addToBackStack(null);

					mFragmentTransaction.commit();
					break;
				case 4:
					image = R.drawable.icon_shop;

					mFragment_Inside_Shop = new Fragment_Inside_Shop();

					mFragmentTransaction = getActivity()
							.getSupportFragmentManager().beginTransaction();

					mFragmentTransaction.replace(R.id.main_fragment,
							mFragment_Inside_Shop);
					mFragmentTransaction.addToBackStack(null);

					mFragmentTransaction.commit();
					break;
				default:
					break;
				}
				// startActivity(intent_list);
			}

		});
		mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				// textView.setText("Item selected! : " + id);

			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
				// textView.setText("Nothing clicked!");
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("On destroyed Call", "Destroyed Call");
		long existing_time = 0;
		logout_time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format_with_name = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format_with_name.format(calendar.getTime());
		long store_time = (logout_time - login_time);
		Datasource_Handler mdatabase = new Datasource_Handler(getActivity());
		mdatabase.open();
		Cursor mCursor = mdatabase.get_all_slots_with_data("searching",
				formattedDate);
		mCursor.moveToFirst();
		if (mCursor.getCount() > 0) {
			while (!mCursor.isAfterLast()) {
				existing_time = mCursor.getLong(2);
				int column_id = mCursor.getInt(3);
				mdatabase.update_details_day(column_id, "searching",
						formattedDate, (store_time + existing_time));
				mCursor.moveToNext();

			}
			mCursor.close();
		} else {
			mdatabase
					.insert_details_day("searching", formattedDate, store_time);
		}
		mdatabase.close();
	}

}
