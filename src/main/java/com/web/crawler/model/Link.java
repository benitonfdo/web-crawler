package com.web.crawler.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {

	private String name;
	private LinkType linkType;
	private String url;
	
	@EqualsAndHashCode.Exclude
	private final Set<Link> subDomainLinks = new HashSet<>();
	
	@EqualsAndHashCode.Exclude
	private final Set<String> links = new HashSet<>();
	
	@EqualsAndHashCode.Exclude
	private final Set<String> media = new HashSet<>();
	
	@EqualsAndHashCode.Exclude
	private final Set<String> imports = new HashSet<>();

}
