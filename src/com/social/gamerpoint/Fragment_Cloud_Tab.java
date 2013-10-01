package com.social.gamerpoint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Fragment_Cloud_Tab extends Fragment implements OnClickListener {
	ImageView imageView_Cloud;
	LinearLayout linear_layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_cloud_tab_layout,
				container, false);
		imageView_Cloud = (ImageView) view
				.findViewById(R.id.imageView_cloud_logo);
		linear_layout = (LinearLayout) view
				.findViewById(R.id.linear_info_layout);
		imageView_Cloud.setOnClickListener(this);
		return view;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int key = v.getId();
		switch (key) {
		case R.id.imageView_cloud_logo:
			linear_layout.setVisibility(View.VISIBLE);
			imageView_Cloud.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}
}
