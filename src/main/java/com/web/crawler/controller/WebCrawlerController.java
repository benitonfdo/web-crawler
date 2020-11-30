package com.web.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.crawler.service.WebCrawlerService;

import reactor.core.publisher.Mono;

@RestController
public class WebCrawlerController {

	@Autowired
	WebCrawlerService webCrawlerService;

	@GetMapping(path = "sitemap", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<?>> getSiteMap(@RequestParam String url) {
		return Mono.just(ResponseEntity.ok(webCrawlerService.getSiteMap(url, false)));
	}

	@GetMapping(path = "sitemap/parallel/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<?>> getSiteMapFetchParalle(@RequestParam String url) {
		return Mono.just(ResponseEntity.ok(webCrawlerService.getSiteMap(url, true)));
	}

}
