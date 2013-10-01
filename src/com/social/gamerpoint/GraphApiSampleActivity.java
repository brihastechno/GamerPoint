package com.social.gamerpoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.*;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphObject;

import java.util.ArrayList;
import java.util.List;

public class GraphApiSampleActivity extends Activity {
	static final String applicationId = "265307690218720";
	static final String PENDING_REQUEST_BUNDLE_KEY = "com.facebook.samples.graphapi:PendingRequest";

	Button buttonRequest;
	EditText editRequests;
	TextView textViewResults;
	Session session;
	boolean pendingRequest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.buttonRequest = (Button) findViewById(R.id.buttonRequest);
		this.buttonRequest.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Log.e("!@@@@@@@", "");
				Log.e("!@@@@@@@", "");
				onClickRequest();
			}
		});
		this.editRequests = (EditText) findViewById(R.id.editRequests);
		this.textViewResults = (TextView) findViewById(R.id.textViewResults);

		this.session = createSession();
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (this.session.onActivityResult(this, requestCode, resultCode, data)
				&& pendingRequest && this.session.getState().isOpened()) {
			sendRequests();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		pendingRequest = savedInstanceState.getBoolean(
				PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
	}

	private void onClickRequest() {
		String requestIdsText = editRequests.getText().toString();
		String[] requestIds = requestIdsText.split(",");

		if (this.session.isOpened()) {
			sendRequests();
		} else {
			StatusCallback callback = new StatusCallback() {
				public void call(Session session, SessionState state,
						Exception exception) {
					if (exception != null) {
						new AlertDialog.Builder(GraphApiSampleActivity.this)
								.setTitle("Login")
								.setMessage(exception.getMessage())
								.setPositiveButton("OK", null).show();
						GraphApiSampleActivity.this.session = createSession();
					}
				}
			};
			pendingRequest = true;
			this.session.openForRead(new Session.OpenRequest(this)
					.setCallback(callback));
		}
	}

	private void sendRequests() {
		textViewResults.setText("");

		String requestIdsText = editRequests.getText().toString();
		String[] requestIds = requestIdsText.split(",");
		Log.e("" + requestIdsText, "" + requestIds);

		List<Request> requests = new ArrayList<Request>();
		for (final String requestId : requestIds) {
			requests.add(new Request(session, requestId, null, null,
					new Request.Callback() {
						public void onCompleted(Response response) {
							GraphObject graphObject = response.getGraphObject();
							FacebookRequestError error = response.getError();
							String s = textViewResults.getText().toString();
							if (graphObject != null) {
								if (graphObject.getProperty("id") != null) {
									s = s
											+ String.format(
													"%s: %s\n",
													graphObject
															.getProperty("id"),
													graphObject
															.getProperty("name"));
								} else {
									s = s
											+ String.format(
													"%s: <no such id>\n",
													requestId);
								}
							} else if (error != null) {
								s = s
										+ String.format("Error: %s",
												error.getErrorMessage());
							}
							textViewResults.setText(s);
						}
					}));
		}
		pendingRequest = false;
		Request.executeBatchAndWait(requests);
	}

	private Session createSession() {
		Session activeSession = Session.getActiveSession();
		if (activeSession == null || activeSession.getState().isClosed()) {
			activeSession = new Session.Builder(this).setApplicationId(
					applicationId).build();
			Session.setActiveSession(activeSession);
		}
		return activeSession;
	}
}
