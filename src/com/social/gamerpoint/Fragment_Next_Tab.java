package com.social.gamerpoint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;

import com.picture.slides.ReflectingImageAdapter;
import com.picture.slides.ResourceImageAdapter;
import com.picture.slides.ResourceImageAdapter_Next;

public class Fragment_Next_Tab extends Fragment {
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
					new ResourceImageAdapter_Next(getActivity()));
		} else {
			coverImageAdapter = new ResourceImageAdapter_Next(getActivity());
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

					break;
				case 1:

					break;
				case 2:

					break;
				case 3:

					break;
				case 4:

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

}
