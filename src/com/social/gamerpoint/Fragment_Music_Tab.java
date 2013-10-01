package com.social.gamerpoint;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.picture.slides.ReflectingImageAdapter;
import com.picture.slides.ResourceImageAdapter_Music;

public class Fragment_Music_Tab extends Fragment {
	ImageView imageView_top_image;
	Fragment_Music_Info_Screen mFragment_music_info;
	FragmentTransaction mFragmentTransaction;

	/**
	 * 
	 * @author lenovo Interface To Communicate With Activity
	 */
	public interface Inteface_Show_info {
		/**
		 * 
		 * @param boolean value
		 */
		public void hide_layout(boolean value);

	}

	Inteface_Show_info mcallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_next_tab, container,
				false);

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
					new ResourceImageAdapter_Music(getActivity()));
		} else {
			coverImageAdapter = new ResourceImageAdapter_Music(getActivity());
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
				switch (key) {
				case 0:
					display_demo_info();
					break;
				case 1:
					display_demo_info();
					break;
				case 2:
					display_demo_info();
					break;
				case 3:
					display_demo_info();
					break;
				case 4:
					display_demo_info();
					break;
				default:
					break;
				}
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

	public void display_demo_info() {
		mFragment_music_info = new Fragment_Music_Info_Screen();

		mFragmentTransaction = getActivity().getSupportFragmentManager()
				.beginTransaction();

		mFragmentTransaction.replace(R.id.main_fragment, mFragment_music_info);
		mFragmentTransaction.addToBackStack(null);

		mFragmentTransaction.commit();

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mcallback = (Inteface_Show_info) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

}
