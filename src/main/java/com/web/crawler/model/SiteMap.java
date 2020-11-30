package com.web.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteMap {
	
	private Link home;
	
	private Integer timeTakenInSecs;

	public SiteMap(Link home) {
		super();
		this.home = home;
	}
	
	
}
