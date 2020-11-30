package com.web.crawler;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.web.crawler.model.Link;
import com.web.crawler.model.LinkType;
import com.web.crawler.model.SiteMap;
import com.web.crawler.service.WebCrawlerService;

import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class WebCrawlerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	WebCrawlerService webCrawlerService;

	@SneakyThrows
	@Test
	void getSiteMap() {
		String webURL = "someurl";
		
		SiteMap siteMap = new SiteMap(new Link("Home", LinkType.URL, webURL, true));
		Link link1 = new Link("Link name1", LinkType.URL, "url",true);
		Link link2 = new Link("Link name2", LinkType.URL, "url",true);
		Link link3 = new Link("Link name3", LinkType.URL, "url",true);
		Link link4 = new Link("Link name4", LinkType.URL, "url",false);
		link2.getSubDomainLinks().add(link4);

		siteMap.getHome().getSubDomainLinks().add(link1);
		siteMap.getHome().getSubDomainLinks().add(link2);
		siteMap.getHome().getSubDomainLinks().add(link3);

		when(webCrawlerService.getSiteMap(webURL, true)).thenReturn(Mono.just(siteMap));

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/sitemap?url=someurl").contentType(MediaType.APPLICATION_JSON_VALUE));
		
		result.andExpect(jsonPath("$.home.links", hasSize(3))).andDo(print());
	}

}
