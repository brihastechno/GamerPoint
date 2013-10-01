package com.social.gamerpoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Edit_User_Profile extends Activity {
	String Email, Password;
	ImageView manage_account, finish, mImageView_logout, mImageView_shop_card;
	LinearLayout linear_earnings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_user_profile);
		manage_account = (ImageView) findViewById(R.id.Manage_Account);
		finish = (ImageView) findViewById(R.id.finish);
		mImageView_logout = (ImageView) findViewById(R.id.imageView_logout);
		mImageView_shop_card = (ImageView) findViewById(R.id.imageView_shop_card);
		linear_earnings = (LinearLayout) findViewById(R.id.linear_layout);
		Typeface typeFace = Typeface.createFromAsset(this.getAssets(),
				"fonts/HelveticaNeue.ttf");

		manage_account.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Email = Activity_MainHome_Activity.Email;
				Password = Activity_MainHome_Activity.Password;
				Intent it = new Intent(Edit_User_Profile.this,
						Activity_Edit_User_Profile.class);
				it.putExtra("Email", Email);
				it.putExtra("Password", Password);
				startActivity(it);
				finish();
			}
		});
		finish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mImageView_logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent mIntent_Activity_Login_Class = new Intent(
						Edit_User_Profile.this, Activity_Login_Class.class);
				startActivity(mIntent_Activity_Login_Class);
			}
		});
		mImageView_shop_card.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (linear_earnings.getVisibility() == View.VISIBLE) {
					linear_earnings.setVisibility(View.GONE);
					layout_include(linear_earnings);

				} else {
					linear_earnings.setVisibility(View.VISIBLE);

				}
			}
		});

	}

	public void layout_include(LinearLayout linear_layout) {

		LayoutInflater linflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface typeFace = Typeface.createFromAsset(this.getAssets(),
				"fonts/HelveticaNeue.ttf");
		// remove all view every time when update methods call...
		linear_layout.removeAllViews();
		final View customView = linflater.inflate(R.layout.card_shop_layout,
				null);
		ImageView mImageView_shop_card = (ImageView) customView
				.findViewById(R.id.shop_image);
		mImageView_shop_card
				.setImageResource(Fragment_Inside_Shop.selling_product_image);

		linear_layout.addView(customView);

	}

}
