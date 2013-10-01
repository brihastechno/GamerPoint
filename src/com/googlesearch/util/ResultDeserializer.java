package com.googlesearch.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.googlesearch.bean.Item;
import com.googlesearch.bean.Result;

public class ResultDeserializer {

	public static Result deserialize(String object) throws JSONException {
		Result result = new Result();

		final List<Item> items = new ArrayList<Item>();

		JSONObject jSonObject = new JSONObject(object);
		JSONArray jSonObjectArray = jSonObject.getJSONArray("items");
		Log.d("", "" + jSonObjectArray.length());
		for (int count = 0; count < jSonObjectArray.length(); count++) {
			JSONObject jsonItem = (JSONObject) jSonObjectArray.get(count);
			Item item = new Item();
			item.setTitle(jsonItem.getString("title"));
			Log.d("titledd " + count, "" + jsonItem.getString("title"));
			item.setHtmlTitle(jsonItem.getString("htmlTitle"));
			Log.d("htmlTitle " + count, "" + jsonItem.getString("htmlTitle"));
			item.setLink(jsonItem.getString("link"));
			Log.d("link " + count, "" + jsonItem.getString("link"));
			item.setDisplayLink(jsonItem.getString("displayLink"));
			Log.d("displayLink " + count,
					"" + jsonItem.getString("displayLink"));
			item.setSnippet(jsonItem.getString("snippet"));
			Log.d("snippet " + count, "" + jsonItem.getString("snippet"));
			item.setHtmlSnippet(jsonItem.getString("htmlSnippet"));
			Log.d("htmlSnippet " + count,
					"" + jsonItem.getString("htmlSnippet"));

			items.add(item);
		}

		result.setItems(items);

		return result;
	}
}
