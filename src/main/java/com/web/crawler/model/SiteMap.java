package com.web.crawler.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteMap implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Link home;
	private Integer timeTakenInSecs;

	public SiteMap(Link home) {
		super();
		this.home = home;
	}
	
	
}
