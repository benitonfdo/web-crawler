package com.web.crawler.service;

import com.web.crawler.model.SiteMap;

public interface WebCrawlerService {

	SiteMap getSiteMap(String webURL, boolean fetchParallel, boolean accumulateCommonLinks);

}
