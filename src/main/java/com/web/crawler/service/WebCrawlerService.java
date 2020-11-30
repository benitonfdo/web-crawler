package com.web.crawler.service;

import com.web.crawler.model.SiteMap;

import reactor.core.publisher.Mono;

public interface WebCrawlerService {

	Mono<SiteMap> getSiteMap(String webURL, boolean fetchParallel);

}
