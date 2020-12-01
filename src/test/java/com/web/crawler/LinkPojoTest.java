package com.web.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.web.crawler.model.Link;

public class LinkPojoTest {
	private final static Link link = new Link("Some link", "url");

	@BeforeAll
	public static void setup() {
		link.getSubDomainLinks().add(new Link("Link1", "URL"));
		link.getSubDomainLinks().add(new Link("Link2", "URL"));
		link.getSubDomainLinks().add(new Link("Link3", "URL"));
		link.getSubDomainLinks().add(new Link("Link3", "URL"));

		Link link4 = new Link("Link4", "URL");

		link.getLinks().add("asd");
		link.getLinks().add("asd");
		link.getLinks().add("asd");
		link.getLinks().add("adf");

		link.getMedia().add("1.jpg");
		link.getMedia().add("2.jpg");
		link.getMedia().add("1.jpg");

		link.getImports().add("someLink");
		
		link.getSubDomainLinks().add(link4);

	}

	@Test
	public void create() {
		Assertions.assertEquals(link.getName(), "Some link");
		Assertions.assertEquals(link.getUrl(), "url");
		
		Assertions.assertEquals(link.getSubDomainLinks().size(), 4);
		Assertions.assertEquals(link.getLinks().size(), 2);
		Assertions.assertEquals(link.getMedia().size(), 2);
		Assertions.assertEquals(link.getImports().size(), 1);
		
	}
}
