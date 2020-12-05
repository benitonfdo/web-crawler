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

import com.web.crawler.model.SiteMap;
import com.web.crawler.service.WebCrawlerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Validated
public class WebCrawlerController {

	@Autowired
	WebCrawlerService webCrawlerService;

	@Operation(summary = "Gets site map sequentially.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get site map using stream and parses it using jsoup.", content = @Content(schema = @Schema(implementation = SiteMap.class))), })
	@GetMapping(path = "sitemap", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSiteMap(
			@Parameter(description = "Valid web URL.", allowEmptyValue = false, example = "https://wiprodigital.com") @RequestParam @NotBlank @Valid String url) {
		try {
			return ResponseEntity.ok(webCrawlerService.getSiteMap(url, false));
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Unable to get the response for the provided URL");
		}
	}

	@Operation(summary = "Gets site map parallel.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get site map using parallel stream and parses it using jsoup.", content = @Content(schema = @Schema(implementation = SiteMap.class))), })
	@GetMapping(path = "sitemap/parallel/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSiteMapFetchParallel(
			@Parameter(description = "Valid web URL.", allowEmptyValue = false, example = "https://wiprodigital.com") @RequestParam @NotBlank @Valid String url) {
		try {
			return ResponseEntity.ok(webCrawlerService.getSiteMap(url, true));
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Unable to get the response for the provided URL");
		}
	}

}
