package com.googlesearch.util;

import java.net.URLEncoder;

public class UrlFactory {

	private static final String url = "https://www.googleapis.com/customsearch/v1";
	private static final String cx = "001724863785134922356:5gud0zueojy";
	private static final String key = "AIzaSyA7EV8Yde9EXxPnZV6oWHgrPkmYdK4ephs";

	public static String buildUrl(String keywords) throws Exception {
		return url + "?key=" + key + "&cx=" + cx + "&q="
				+ URLEncoder.encode(keywords) + "&alt=json";
	}

}
