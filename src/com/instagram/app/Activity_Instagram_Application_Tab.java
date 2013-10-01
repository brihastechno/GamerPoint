package com.instagram.app;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.social.gamerpoint.R;

public class Activity_Instagram_Application_Tab extends TabActivity {
	String instagram_id, string_token, followed;
	TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instagram_application_tab);
		instagram_id = getIntent().getStringExtra("string_id");
		string_token = getIntent().getStringExtra("string_token");
		Log.e("<><><><><><><><><><><><<>", "" + string_token);
		tabHost = getTabHost();
		setTabs();
		tabHost.setCurrentTab(0);
	}

	private void setTabs() {
		Intent intent_home = new Intent().setClass(this,
				Activity_Instagram_Home_Tab.class);
		intent_home.putExtra("string_token", string_token);
		addTab("", R.drawable.icon_instagram_home, intent_home);

		Intent intent_explore = new Intent().setClass(this,
				Activity_Instagram_Explore_Tab.class);
		intent_explore.putExtra("string_token", string_token);
		addTab("", R.drawable.icon_instagram_explore, intent_explore);

		Intent intent_camera = new Intent().setClass(this,
				Activity_Instagram_Camera.class);
		addTab("", R.drawable.icon_instagram_camera, intent_camera);

		// follower data tab
		Intent intent_follower = new Intent().setClass(this,
				Activity_Instagram_Follower_List.class);
		intent_follower.putExtra("string_token", string_token);
		intent_follower.putExtra("string_id", instagram_id);
		addTab("", R.drawable.icon_instagram_follower, intent_follower);

		Intent intent_profile = new Intent().setClass(this,
				Activity_Instagram_Profile.class);
		intent_profile.putExtra("string_token", string_token);
		intent_profile.putExtra("string_id", instagram_id);
		addTab("", R.drawable.icon_instagram_profile, intent_profile);
	}

	private void addTab(String labelId, int drawableId, Intent intent) {
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.custom_instagram_tab_layout, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);

	}

}
