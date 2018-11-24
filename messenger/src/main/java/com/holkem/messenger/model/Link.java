package com.holkem.messenger.model;

/* holkem: this is a custom Link class but there is an
 * official Link class in the jax-rs */
public class Link {
	private String url;
	private String rel;
	
	public Link(String url, String rel) {
		this.url = url;
		this.rel = rel;
	}

	public Link() { }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}
}
