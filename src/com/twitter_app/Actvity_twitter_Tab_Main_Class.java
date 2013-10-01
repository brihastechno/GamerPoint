package com.twitter_app;

import android.app.TabActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.social.gamerpoint.R;

@SuppressWarnings("deprecation")
public class Actvity_twitter_Tab_Main_Class extends TabActivity {
	Button mButton_post;
	TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_tab_main);
		mButton_post = (Button) findViewById(R.id.button1);
		mButton_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it_post = new Intent(
						Actvity_twitter_Tab_Main_Class.this,
						Post_Activity.class);
				startActivity(it_post);

			}
		});
		// tab working
		tabHost = getTabHost();
		setTabs();
		tabHost.setCurrentTab(0);

	}

	private void setTabs() {
		Intent intent_fetch_twitter = new Intent().setClass(this,
				Twitter_Home_TimeLine.class);
		addTab("Home", R.drawable.twitter_tab_home, intent_fetch_twitter);
		Intent intent_list_follower = new Intent().setClass(this,
				List_Follower_Show.class);
		addTab("Followers", R.drawable.twitter_tab_follower,
				intent_list_follower);
		Intent intent_profile = new Intent().setClass(this,
				Me_Profile_Activity.class);
		addTab("Me", R.drawable.twitter_tab_profile, intent_profile);
	}

	private void addTab(String labelId, int drawableId, Intent intent) {
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.custom_tab_layout, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);

	}
}
