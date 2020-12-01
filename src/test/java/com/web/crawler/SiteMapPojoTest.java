package com.web.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.web.crawler.model.Link;
import com.web.crawler.model.SiteMap;

public class SiteMapPojoTest {

	private final SiteMap siteMap = new SiteMap();
	private final Link link = new Link("Some link", "url");
	private final SiteMap siteMapWithLink = new SiteMap(link);

	@Test
	public void create() throws Exception {

		Assertions.assertEquals(this.siteMap.getTimeTakenInSecs(),null);
		Assertions.assertEquals(this.siteMap.getHome(), null);
		Assertions.assertEquals(this.siteMapWithLink.getHome(), link);

	}
}
