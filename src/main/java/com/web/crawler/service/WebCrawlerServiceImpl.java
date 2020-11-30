package com.web.crawler.service;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.web.crawler.model.Link;
import com.web.crawler.model.LinkType;
import com.web.crawler.model.SiteMap;

import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

@CommonsLog
@Service
public class WebCrawlerServiceImpl implements WebCrawlerService {

	private static final String DOMAIN = "##domain##";

	@Autowired
	WebClient webClient;

	@SneakyThrows
	@Override
	public Mono<SiteMap> getSiteMap(String webURL, boolean fetchParallel) {
		SiteMap siteMap = new SiteMap(new Link("Home", LinkType.URL, webURL));
		Map<String, Link> visitedLinks = new ConcurrentHashMap<>();
		String domain = new URI(webURL).getHost();
		long time = System.currentTimeMillis();
		setLinksFromHTMLContent(siteMap.getHome(), domain, webURL, visitedLinks, fetchParallel);
		log.info("Time taken (secs) : " + (System.currentTimeMillis() - time) / 1000);
		return Mono.just(siteMap);
	}

	@SneakyThrows
	private void setLinksFromHTMLContent(Link home, String domain, String url, Map<String, Link> visitedLinks,
			boolean fetchParallel) {
		visitedLinks.put(url, home);
		Document document = Jsoup.connect(url).get();
		setDocumentInformation(document, home);
		String linksQuery = "a[href*=##domain##]".replaceAll(DOMAIN, domain);
		Elements linkElements = document.select(linksQuery);
		linkElements.stream().filter(element -> {
			String absURL = element.absUrl("href");
			String name = element.text();
			boolean isSameDomain = absURL.startsWith(domain, 7) || absURL.startsWith(domain, 8);
			return isSameDomain && StringUtils.isNotEmpty(name) && visitedLinks.get(absURL) == null;
		}).forEach(element -> {
			Link subLink = new Link(element.text(), LinkType.URL, element.absUrl("href"));
			home.getSubDomainLinks().add(subLink);
		});
		if (fetchParallel) {
			home.getSubDomainLinks().parallelStream().forEach(subLink -> {
				setLinksFromHTMLContent(subLink, domain, subLink.getUrl(), visitedLinks, true);
			});
		} else {
			home.getSubDomainLinks().stream().forEach(subLink -> {
				setLinksFromHTMLContent(subLink, domain, subLink.getUrl(), visitedLinks, false);
			});
		}
	}

	private void setDocumentInformation(Document document, Link link) {
		Elements links = document.select("a[href]");
		Elements media = document.select("[src]");
		Elements imports = document.select("link[href]");
		links.stream().forEach(linkElement -> {
			link.getLinks().add(linkElement.absUrl("href"));
		});
		media.stream().forEach(mediaElement -> {
			link.getMedia().add(mediaElement.absUrl("src"));
		});
		imports.stream().forEach(importElement -> {
			link.getImports().add(importElement.absUrl("href"));
		});
	}
}
