package com.web.crawler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
	
	@Bean
	public WebClient createWebClient() {
		WebClient webClient = WebClient.builder()
				.exchangeStrategies(ExchangeStrategies.builder().codecs(this::acceptedCodecs).build())
				.build();
		return webClient;
	}
	
	private void acceptedCodecs(ClientCodecConfigurer configurer) {
		configurer.defaultCodecs().maxInMemorySize(16*1024*1024);
	}

}
