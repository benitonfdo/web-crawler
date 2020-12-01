package com.web.crawler.service;

import java.net.URISyntaxException;

import com.web.crawler.model.SiteMap;

public interface WebCrawlerService {

	SiteMap getSiteMap(String webURL, boolean fetchParallel) throws URISyntaxException;
	
	SiteMap someMethod(String url);

}
