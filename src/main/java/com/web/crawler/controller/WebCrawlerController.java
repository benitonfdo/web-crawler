package com.web.crawler.controller;

import java.net.URISyntaxException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.crawler.service.WebCrawlerService;

@RestController
@Validated
public class WebCrawlerController {

	@Autowired
	WebCrawlerService webCrawlerService;

	@GetMapping(path = "sitemap")
	public ResponseEntity<?> getSiteMap(@RequestParam @NotBlank @Valid String url) {
		try {
			return ResponseEntity.ok(webCrawlerService.getSiteMap(url, false));
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Unable to get the response for the provided URL");
		}
	}

	@GetMapping(path = "sitemap/parallel/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSiteMapFetchParallel(@RequestParam @NotBlank @Valid String url) {
		try {
			return ResponseEntity.ok(webCrawlerService.getSiteMap(url, true));
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Unable to get the response for the provided URL");
		}
	}


}
