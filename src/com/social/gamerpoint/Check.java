package com.social.gamerpoint;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.youtube.player.internal.x;

public class Check extends Activity {

	WebView webView1;
	MyJavaScriptInterface myJavaScriptInterface1;

	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_facebook_view);
		webView1 = (WebView) findViewById(R.id.webview);
		myJavaScriptInterface1 = new MyJavaScriptInterface(this);

		webView1.addJavascriptInterface(myJavaScriptInterface1,
				"AndroidFunction");
		EnableJavaScriptAndMultitouch(webView1);
		webView1.loadUrl("file:///android_asset/P_001.html");
	}

	public void EnableJavaScriptAndMultitouch(WebView mWebView) {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
	}

	public void onLongPress(MotionEvent e) {
		webView1.loadUrl("javascript:SelectAyaInPosition("
				+ String.valueOf(e.getX()) + "," + String.valueOf(e.getY())
				+ ", 'black', 'red'");
	}

	// //////////////////////////////
	public class MyJavaScriptInterface {
		Context mContext;

		MyJavaScriptInterface(Context c) {
			mContext = c;
		}

		public void getElementID(String ID) {
			Toast.makeText(mContext, ID, Toast.LENGTH_SHORT).show();
		}
	}

}
