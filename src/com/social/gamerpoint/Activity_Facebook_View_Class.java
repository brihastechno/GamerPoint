package com.social.gamerpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PlacePickerFragment;
import com.facebook.widget.ProfilePictureView;

/**
 * Activity_Facebook_View_Class open the Facebook layouts...
 * 
 * @author lenovo
 * 
 */
public class Activity_Facebook_View_Class extends FragmentActivity {
	// setting the permissions for facebook...
	private static final List<String> PERMISSIONS = Arrays.asList(
			"publish_actions", "user_likes", "user_status", "user_photos",
			"read_requests", "read_stream", "share_item", "status_update",
			"video_upload");
	ProgressDialog dialog;
	String id;

	// Location static for pic places
	private static final Location SEATTLE_LOCATION = new Location("") {
		{
			setLatitude(47.6097);
			setLongitude(-122.3331);
		}
	};
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	// Activity button....
	private Button postStatusUpdateButton, postPhotoButton, pickFriendsButton,
			pickPlaceButton;

	// Facebook Login Button..
	private LoginButton loginButton;
	// Facebook Profile PictureView..
	private ProfilePictureView profilePictureView;

	private TextView greeting;
	private PendingAction pendingAction = PendingAction.NONE;
	private ViewGroup controlsContainer;
	private GraphUser user;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	// Used For Mananging The Session....
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	LinearLayout layout;
	Button button_news_feed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = new ProgressDialog(Activity_Facebook_View_Class.this);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}
		setContentView(R.layout.activity_facebook_view);

		layout = (LinearLayout) findViewById(R.id.layout);

		// Initialize Facebook LoginButton and set Permission To Login And
		// Profile Picture
		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setPublishPermissions(PERMISSIONS);
		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);

		// initialize Activity Components Button And TextView...
		greeting = (TextView) findViewById(R.id.greeting);
		postStatusUpdateButton = (Button) findViewById(R.id.postStatusUpdateButton);
		postPhotoButton = (Button) findViewById(R.id.postPhotoButton);
		pickPlaceButton = (Button) findViewById(R.id.pickPlaceButton);
		pickFriendsButton = (Button) findViewById(R.id.pickFriendsButton);
		button_news_feed = (Button) findViewById(R.id.button_news_feed);
		button_news_feed.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Session session = Session.getActiveSession();
				String session_token = session.getAccessToken();
				Log.d("The Active Access Token", "" + session_token);

				final String url = "https://graph.facebook.com/me/home?access_token="
						+ session_token;
				// Log.i(TAG, "Popping a browser with the authorize URL : " +
				// url);
				boolean enableButtons = (session != null && session.isOpened());
				Log.i("User_id", "" + id);
				if (enableButtons && user != null) {
					Intent intent = new Intent(
							Activity_Facebook_View_Class.this,
							Activity_news_feeds.class);
					intent.putExtra("url", url);
					intent.putExtra("user_id", user.getId());

					intent.putExtra("token", session_token);
					startActivity(intent);
				} else {

				}

			}
		});

		// Using The Fragment Manager For Other LAyouts Shows In Activity
		final FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_container);

		// set CallBack on userInfo Changed....
		loginButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						Activity_Facebook_View_Class.this.user = user;
						// Update The UI (Enable And Disable Button)
						updateUI();

						// It's possible that we were waiting for this.user to
						// be populated in order to post a
						// status update.
						handlePendingAction();
					}
				});

		postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickPostStatusUpdate();
			}
		});

		postPhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickPostPhoto();
			}
		});

		pickFriendsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickPickFriends();
			}
		});

		pickPlaceButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				onClickPickPlace();
			}
		});

		controlsContainer = (ViewGroup) findViewById(R.id.main_ui_container);

		if (fragment != null) {
			// If we're being re-created and have a fragment, we need to a) hide
			// the main UI controls and
			// b) hook up its listeners again.
			controlsContainer.setVisibility(View.GONE);
			if (fragment instanceof FriendPickerFragment) {
				setFriendPickerListeners((FriendPickerFragment) fragment);
			} else if (fragment instanceof PlacePickerFragment) {
				setPlacePickerListeners((PlacePickerFragment) fragment);
			}
		}

		// Listen for changes in the back stack so we know if a fragment got
		// popped off because the user
		// clicked the back button.
		fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				if (fm.getBackStackEntryCount() == 0) {
					// We need to re-show our UI.
					controlsContainer.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		// Update UI
		dialog.show();
		updateUI();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		dialog.cancel();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	/**
	 * Manage The SessionStatteChange USing Session.StatusCallback callback
	 * 
	 * @param session
	 * @param state
	 * @param exception
	 */
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(Activity_Facebook_View_Class.this)
					.setTitle(R.string.cancelled)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		// Update UI
		updateUI();
	}

	/**
	 * Update UI Get
	 * 
	 */

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());
		if (enableButtons) {
			layout.setVisibility(View.VISIBLE);
			
		} else {
			layout.setVisibility(View.GONE);
			
			dialog.cancel();
		}
		;

		postStatusUpdateButton.setEnabled(enableButtons);
		postPhotoButton.setEnabled(enableButtons);
		pickFriendsButton.setEnabled(enableButtons);
		pickPlaceButton.setEnabled(enableButtons);
		if (enableButtons && user != null) {
			profilePictureView.setProfileId(user.getId());
			id = user.getId();
			Log.e("Id ", "id" + id);
			dialog.cancel();

			greeting.setText(getString(R.string.hello_user, user.getFirstName()));
		} else {
			profilePictureView.setProfileId(null);
			greeting.setText(null);
		}

	}

	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			postPhoto();
			break;
		case POST_STATUS_UPDATE:
			postStatusUpdate();
			break;

		}

	}

	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}

	private void showPublishResult(String message, GraphObject result,
			FacebookRequestError error) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = getString(R.string.success);
			String id = result.cast(GraphObjectWithId.class).getId();
			alertMessage = getString(R.string.successfully_posted_post,
					message, id);
		} else {
			title = getString(R.string.error);
			alertMessage = error.getErrorMessage();
		}

		new AlertDialog.Builder(this).setTitle(title).setMessage(alertMessage)
				.setPositiveButton(R.string.ok, null).show();
	}

	private void onClickPostStatusUpdate() {
		performPublish(PendingAction.POST_STATUS_UPDATE);
	}

	private void postStatusUpdate() {
		if (user != null && hasPublishPermission()) {
			final String message = getString(R.string.status_update,
					user.getFirstName(), (new Date().toString()));

			Request request = Request.newStatusUpdateRequest(
					Session.getActiveSession(), message,
					new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							showPublishResult(message,
									response.getGraphObject(),
									response.getError());
						}
					});
			request.executeAsync();
		} else {
			pendingAction = PendingAction.POST_STATUS_UPDATE;
		}
	}

	private void onClickPostPhoto() {
		performPublish(PendingAction.POST_PHOTO);
	}

	private void postPhoto() {
		if (hasPublishPermission()) {
			Bitmap image = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.com_facebook_profile_picture_blank_portrait);

			Request request = Request.newUploadPhotoRequest(
					Session.getActiveSession(), image, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							showPublishResult(getString(R.string.photo_post),
									response.getGraphObject(),
									response.getError());
						}
					});
			request.executeAsync();
		} else {
			pendingAction = PendingAction.POST_PHOTO;
		}
	}

	private void showPickerFragment(PickerFragment<?> fragment) {
		fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
			@Override
			public void onError(PickerFragment<?> pickerFragment,
					FacebookException error) {
				showAlert(getString(R.string.error), error.getMessage());
			}
		});

		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.fragment_container, fragment)
				.addToBackStack(null).commit();
		controlsContainer.setVisibility(View.GONE);
		// We want the fragment fully created so we can use it immediately.
		fm.executePendingTransactions();
		fragment.loadData(false);
	}

	private void onClickPickFriends() {
		final FriendPickerFragment fragment = new FriendPickerFragment();
		setFriendPickerListeners(fragment);
		showPickerFragment(fragment);
	}

	private void setFriendPickerListeners(final FriendPickerFragment fragment) {
		fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
			@Override
			public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
				onFriendPickerDone(fragment);
			}
		});
	}

	private void onFriendPickerDone(FriendPickerFragment fragment) {
		FragmentManager fm = getSupportFragmentManager();
		fm.popBackStack();
		String results = "";
		Collection<GraphUser> selection = fragment.getSelection();
		if (selection != null && selection.size() > 0) {
			ArrayList<String> names = new ArrayList<String>();
			for (GraphUser user : selection) {
				names.add(user.getName());
			}
			results = TextUtils.join(", ", names);
		} else {
			results = getString(R.string.no_friends_selected);
		}
		showAlert(getString(R.string.you_picked), results);
	}

	private void onPlacePickerDone(PlacePickerFragment fragment) {
		FragmentManager fm = getSupportFragmentManager();
		fm.popBackStack();
		String result = "";
		GraphPlace selection = fragment.getSelection();
		if (selection != null) {
			result = selection.getName();
		} else {
			result = getString(R.string.no_place_selected);
		}
		showAlert(getString(R.string.you_picked), result);
	}

	/**
	 * Check The _Place
	 * 
	 */

	private void onClickPickPlace() {
		final PlacePickerFragment fragment = new PlacePickerFragment();
		fragment.setLocation(SEATTLE_LOCATION);
		fragment.setTitleText(getString(R.string.pick_seattle_place));
		setPlacePickerListeners(fragment);
		showPickerFragment(fragment);
	}

	/**
	 * Handle The PlacePicker Fragment
	 * 
	 * @param fragment
	 */
	private void setPlacePickerListeners(final PlacePickerFragment fragment) {
		fragment.setOnDoneButtonClickedListener(new PlacePickerFragment.OnDoneButtonClickedListener() {
			@Override
			public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
				onPlacePickerDone(fragment);
			}
		});
		fragment.setOnSelectionChangedListener(new PlacePickerFragment.OnSelectionChangedListener() {
			@Override
			public void onSelectionChanged(PickerFragment<?> pickerFragment) {
				if (fragment.getSelection() != null) {
					onPlacePickerDone(fragment);
				}
			}
		});
	}

	/**
	 * 
	 * showing Alert Message...ssss
	 * 
	 * @param title
	 * @param message
	 */
	private void showAlert(String title, String message) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(R.string.ok, null).show();
	}

	/**
	 * check the session and permissions...
	 * 
	 * @return boolean
	 */
	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null
				&& session.getPermissions().contains("publish_actions");
	}

	/**
	 * 
	 * publish on Facebook Walls
	 * 
	 * @param action
	 */
	private void performPublish(PendingAction action) {
		Session session = Session.getActiveSession();
		if (session != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction();
			} else {
				// We need to get new permissions, then complete the action when
				// we get called back.
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
						this, PERMISSIONS));
			}
		}
	}

}
