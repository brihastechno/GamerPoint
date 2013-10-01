package com.googlesearch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class HttpRequest {

	public static synchronized String doRequest(String urlRequest)
			throws Exception {
		String jsonReply = "";

		jsonReply = getJSONFromUrl(urlRequest);
		return jsonReply;
	}

	public static String getJSONFromUrl(String url) {
		// Making HTTP request
		InputStream is = null;
		String json = "";

		try {
			// defaultHttpClient
			HttpParams httpParams = new BasicHttpParams();
			int some_reasonable_timeout = (int) (70 * 1000);
			HttpConnectionParams.setConnectionTimeout(httpParams,
					some_reasonable_timeout);
			HttpConnectionParams.setSoTimeout(httpParams,
					some_reasonable_timeout);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpGet httpPost = new HttpGet(url);

			HttpResponse response = null;
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 102400);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "");
				Log.e("", line);
			}

			// Log.d("value response", sb.toString());
			json = sb.toString();
			Log.d("String Response", sb.toString());
			// is.close();
			// jObj = new JSONObject(json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		// try parse the string to a JSON object
		// return JSON String
		return json;

	}

}
