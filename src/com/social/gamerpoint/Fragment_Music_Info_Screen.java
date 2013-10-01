package com.social.gamerpoint;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Fragment_Music_Info_Screen extends Fragment {
	MediaPlayer mMediPlayer_Audio;
	ImageView imageView_top_image;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_music_info_screen,
				container, false);
		imageView_top_image = (ImageView) view
				.findViewById(R.id.fragment_image);
		imageView_top_image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					if (mMediPlayer_Audio != null) {
						if (mMediPlayer_Audio.isPlaying())
							mMediPlayer_Audio.start();
						else
							mMediPlayer_Audio.stop();
					} else {
						start_Player();
					}
				} catch (IllegalStateException e) {

				} catch (NullPointerException e) {

				}

			}
		});
		return view;
	}

	/**
	 * start_the Player
	 */
	private void start_Player() {
		mMediPlayer_Audio = new MediaPlayer();

		try {
			AssetFileDescriptor audiopath = getActivity().getAssets().openFd(
					"ring3.mp3");
			mMediPlayer_Audio.setDataSource(audiopath.getFileDescriptor(),
					audiopath.getStartOffset(), audiopath.getLength());
			mMediPlayer_Audio.prepare();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediPlayer_Audio.setLooping(false);
		mMediPlayer_Audio.setVolume(100, 100);
		mMediPlayer_Audio.start();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d("mMediPlayer_Audio ", "In On Destroy ");
		try {
			if (mMediPlayer_Audio != null) {
				if (mMediPlayer_Audio.isPlaying())
					mMediPlayer_Audio.stop();

				mMediPlayer_Audio.release();

			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
