package com.gcit.lms.entity;

import java.util.List;

public class Publisher {
	
	private List<Book> books;
	private Integer pubId;
	private String pubAddress;
	private String pubName;
	private String pubPhone;
	/**
	 * @return the books
	 */
	public List<Book> getBooks() {
		return books;
	}
	/**
	 * @param books the books to set
	 */
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	/**
	 * @return the pubId
	 */
	public Integer getPubId() {
		return pubId;
	}
	/**
	 * @param pubId the pubId to set
	 */
	public void setPubId(Integer pubId) {
		this.pubId = pubId;
	}
	/**
	 * @return the pubName
	 */
	public String getPubName() {
		return pubName;
	}
	/**
	 * @param pubName the pubName to set
	 */
	public void setPubName(String pubName) {
		this.pubName = pubName;
	}
	/**
	 * @return the pubPhone
	 */
	public String getPubPhone() {
		return pubPhone;
	}
	/**
	 * @param pubPhone the pubPhone to set
	 */
	public void setPubPhone(String pubPhone) {
		this.pubPhone = pubPhone;
	}
	/**
	 * @return the pubAddress
	 */
	public String getPubAddress() {
		return pubAddress;
	}
	/**
	 * @param pubAddress the pubAddress to set
	 */
	public void setPubAddress(String pubAddress) {
		this.pubAddress = pubAddress;
	}

}
