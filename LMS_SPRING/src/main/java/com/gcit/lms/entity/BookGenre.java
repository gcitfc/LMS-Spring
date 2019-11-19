package com.gcit.lms.entity;

public class BookGenre {
	
	private Integer bookId;
	private Integer genreId;

	public BookGenre() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the bookId
	 */
	public Integer getBookId() {
		return bookId;
	}

	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	/**
	 * @return the genreId
	 */
	public Integer getGenreId() {
		return genreId;
	}

	/**
	 * @param genreId the genreId to set
	 */
	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}

}
