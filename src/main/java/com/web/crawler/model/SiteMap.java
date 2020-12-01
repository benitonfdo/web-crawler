package com.web.crawler.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteMap {
	
	private Link home;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Set<String> commonLinks;
	
	private Integer timeTakenInSecs;

	public SiteMap(Link home) {
		super();
		this.home = home;
	}
	
	
}
