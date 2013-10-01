package com.social.gamerpoint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class Fragment_Inside_Shop extends Fragment {
	ImageView mImageView, mImageView_inside_frame_imageview;
	Button mButton_next, mButton_prvoius, mButton_click_image;
	ViewFlipper viewFlipper;
	int count;
	static int selling_product_image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		count = 0;
		View view = inflater.inflate(R.layout.fragment_inside_shop, container,
				false);
		mImageView = (ImageView) view.findViewById(R.id.fragment_image);
		mButton_next = (Button) view.findViewById(R.id.next_click);
		viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
		mButton_prvoius = (Button) view.findViewById(R.id.previous_click);
		mButton_click_image = (Button) view.findViewById(R.id.click_image);
		mImageView_inside_frame_imageview = (ImageView) view
				.findViewById(R.id.inside_frame_imageview);
		mImageView.setImageResource(Fragment_Shop_Tab.image);

		mButton_click_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (count == 0) {
					Log.e("count====case 0=====", "" + count);
					selling_product_image = R.drawable.ads;

				} else if (count == 1) {
					Log.e("count====case 1=====", "" + count);
					selling_product_image = R.drawable.watch;
				} else if (count == 2) {
					Log.e("count====case 2=====", "" + count);
					selling_product_image = R.drawable.horse;
				}
			}
		});

		mButton_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				viewFlipper.setInAnimation(getActivity(),
						R.anim.view_transition_in_left);
				viewFlipper.setOutAnimation(getActivity(),
						R.anim.view_transition_out_left);

				if (count != 2) {
					viewFlipper.showNext();
					count++;
				}

			}
		});

		mButton_prvoius.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				viewFlipper.setInAnimation(getActivity(),
						R.anim.view_transition_in_right);
				viewFlipper.setOutAnimation(getActivity(),
						R.anim.view_transition_out_right);
				if (count != 0) {
					viewFlipper.showPrevious();
					count--;
				}
			}
		});

		return view;
	}

}
