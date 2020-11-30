package com.web.crawler.service;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.web.crawler.model.Link;
import com.web.crawler.model.SiteMap;

import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

@Service
public class WebCrawlerServiceImpl implements WebCrawlerService {

	private static final String DOMAIN = "##domain##";

	@SneakyThrows
	@Override
	public Mono<SiteMap> getSiteMap(String webURL, boolean fetchParallel) {
		SiteMap siteMap = new SiteMap(new Link("Home", webURL, true));
		Set<String> linksToBeVisited;
		if (fetchParallel) {
			linksToBeVisited = new CopyOnWriteArraySet<>();
		} else {
			linksToBeVisited = new HashSet<>();
		}
		String domain = new URI(webURL).getHost();
		long time = System.currentTimeMillis();
		setLinksFromHTMLContent(siteMap.getHome(), domain, webURL, linksToBeVisited, fetchParallel);
		Long timeTaken = (System.currentTimeMillis() - time) / 1000;
		siteMap.setTimeTakenInSecs(timeTaken.intValue());
		return Mono.just(siteMap);
	}

	@SneakyThrows
	private void setLinksFromHTMLContent(Link home, String domain, String url, Set<String> linksToBeVisited,
			boolean fetchParallel) {
		if (!home.getCanFetchContent()) {
			return;
		}
		Document document = Jsoup.connect(url).get();
		setDocumentInformation(document, home);
		String linksQuery = "a[href*=##domain##]".replaceAll(DOMAIN, domain);
		Elements linkElements = document.select(linksQuery);
		linkElements.stream().filter(element -> {
			String absURL = element.absUrl("href");
			String name = element.text();
			boolean isSameDomain = absURL.startsWith(domain, 7) || absURL.startsWith(domain, 8);
			return isSameDomain && StringUtils.isNotEmpty(name);
		}).forEach(element -> {
			String absURL = element.absUrl("href");
			Link subLink;
			if (!linksToBeVisited.contains(absURL)) {
				linksToBeVisited.add(absURL);
				subLink = new Link(element.text(), element.absUrl("href"), true);
				home.getSubDomainLinks().add(subLink);
			}
		});
		if (fetchParallel) {
			home.getSubDomainLinks().parallelStream().forEach(subLink -> {
				setLinksFromHTMLContent(subLink, domain, subLink.getUrl(), linksToBeVisited, true);
			});
		} else {
			home.getSubDomainLinks().stream().forEach(subLink -> {
				setLinksFromHTMLContent(subLink, domain, subLink.getUrl(), linksToBeVisited, false);
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
