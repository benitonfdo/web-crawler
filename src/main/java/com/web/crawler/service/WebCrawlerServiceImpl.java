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

@Service
public class WebCrawlerServiceImpl implements WebCrawlerService {

	private static final String DOMAIN = "##domain##";
	
	private static final String LINKS_BASED_ON_DOMAIN = "a[href*=##domain##]";
	
	private static final String HREF = "href";
	
	private static final String SRC = "src";
	
	private static final String LINKS_QUERY = "a[href]";
	
	private static final String MEDIA_QUERY = "[src]";
	
	private static final String IMPORTS_QUERY = "link[href]";

	@SneakyThrows
	@Override
	public SiteMap getSiteMap(String webURL, boolean fetchParallel) {
		SiteMap siteMap = new SiteMap(new Link("Home", webURL));
		Set<String> linksToBeVisited = (fetchParallel) ? new CopyOnWriteArraySet<>() :new HashSet<>() ;
		String domain = new URI(webURL).getHost();
		long time = System.currentTimeMillis();
		setLinksFromHTMLContent(siteMap.getHome(), domain, webURL, linksToBeVisited, fetchParallel);
		Long timeTaken = (System.currentTimeMillis() - time) / 1000;
		siteMap.setTimeTakenInSecs(timeTaken.intValue());
		return siteMap;
	}

	@SneakyThrows
	private void setLinksFromHTMLContent(Link home, String domain, String url, Set<String> linksToBeVisited,
			boolean fetchParallel) {
		Document document = Jsoup.connect(url).get();
		setDocumentInformation(document, home);
		String linksQuery = LINKS_BASED_ON_DOMAIN.replaceAll(DOMAIN, domain);
		Elements linkElements = document.select(linksQuery);
		linkElements.stream().filter(element -> {
			String absURL = element.absUrl(HREF);
			String name = element.text();
			boolean isSameDomain = absURL.startsWith(domain, 7) || absURL.startsWith(domain, 8);
			return isSameDomain && StringUtils.isNotEmpty(name);
		}).forEach(element -> {
			String absURL = element.absUrl(HREF);
			Link subLink;
			if (!linksToBeVisited.contains(absURL)) {
				linksToBeVisited.add(absURL);
				subLink = new Link(element.text(), element.absUrl(HREF));
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
		Elements links = document.select(LINKS_QUERY);
		Elements media = document.select(MEDIA_QUERY);
		Elements imports = document.select(IMPORTS_QUERY);
		links.stream().forEach(linkElement -> {
			link.getLinks().add(linkElement.absUrl(HREF));
		});
		media.stream().forEach(mediaElement -> {
			link.getMedia().add(mediaElement.absUrl(SRC));
		});
		imports.stream().forEach(importElement -> {
			link.getImports().add(importElement.absUrl(HREF));
		});
	}

}
