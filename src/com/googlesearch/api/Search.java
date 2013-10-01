package com.googlesearch.api;

import com.googlesearch.bean.Result;
import com.googlesearch.util.HttpRequest;
import com.googlesearch.util.ResultDeserializer;
import com.googlesearch.util.UrlFactory;

public class Search {

	public Result doSearch(String keywords) throws Exception {
		final String urlRequest = UrlFactory.buildUrl(keywords);
		final String jsonObjet = HttpRequest.doRequest(urlRequest);
		return ResultDeserializer.deserialize(jsonObjet);
	}
}
