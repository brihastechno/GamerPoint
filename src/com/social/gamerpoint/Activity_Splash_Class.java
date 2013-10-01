package com.social.gamerpoint;

/**
 * Activity Splash Screen for first screen Layouts
 */
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * First Class Splash Screen Display.. The class linked with Classes
 * {@link Activity_Login_Class} {@link Activity_MainHome_Activity}
 * {@link SessionManager} and use the Animation..
 * 
 * @author lenovo
 * 
 */
public class Activity_Splash_Class extends Activity {
	ObjectAnimator objectAnimator;
	TextView textView_title;
	ImageView image_logo;
	SessionManager session;
	String Email, Password, image_path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// set layout View...
		setContentView(R.layout.activity_splash_screen_layout);
		// UI components
		textView_title = (TextView) findViewById(R.id.textview_title);
		image_logo = (ImageView) findViewById(R.id.imageView1);
		// Call to animate the ImageView of Logo...
		show_animation_to_TextView(image_logo);

	}

	/**
	 * 
	 * @param ImageView
	 *            function USe for animated the Logo Image on Splash Screen
	 */
	private void show_animation_to_TextView(final ImageView textview) {
		// TODO Auto-generated method stub
		objectAnimator = ObjectAnimator.ofFloat(textview, "alpha", 0f, 1f);
		objectAnimator.setDuration(7500);
		objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
		objectAnimator.setRepeatCount(1);
		ObjectAnimator mover = ObjectAnimator.ofFloat(textview, "scaleX", 0.5f,
				1f);
		mover.setDuration(7000);
		mover.setRepeatMode(ValueAnimator.REVERSE);
		mover.setRepeatCount(1);
		ObjectAnimator mover_2 = ObjectAnimator.ofFloat(textview, "scaleY",
				0.5f, 1f);
		mover_2.setDuration(7000);
		mover_2.setRepeatMode(ValueAnimator.REVERSE);
		mover_2.setRepeatCount(1);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(objectAnimator).with(mover).with(mover_2);

		animatorSet.start();
		objectAnimator.addListener(new AnimatorListener() {
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
			}

			/**
			 * On Animatiion END...
			 */
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				// Check The Session if exist of any user....
				session = new SessionManager(getApplicationContext());
				HashMap<String, String> user = session.getUserDetails();
				// Getting email and password from session...
				Email = user.get(SessionManager.KEY_Email);
				Password = user.get(SessionManager.KEY_Password);
				image_path = user.get(SessionManager.KEY_image_path);
				// check for session exist//check in SeesionManager class
				// if(true..redirects to Login Page..session not exist
				if (session.checkLogin()) {

				}
				// Start Activity_SocialHome_Activity class With sending
				// existing user information through Intent
				else {

					Intent intent_social_home = new Intent(
							Activity_Splash_Class.this,
							Activity_MainHome_Activity.class);
					intent_social_home.putExtra("Email", Email);
					intent_social_home.putExtra("Password", Password);
					intent_social_home.putExtra("image_path", image_path);

					// start Activity...
					startActivity(intent_social_home);
				}
				// finish splash activity not going back to this screen after
				finish();
			}

			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
			}
		});

	}
}
