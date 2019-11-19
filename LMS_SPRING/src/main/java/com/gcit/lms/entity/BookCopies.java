package com.gcit.lms.entity;

public class BookCopies {
	
	private Book book;
	private Branch branch;
	private Integer noOfCopies;

	public BookCopies() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * @param book the book to set
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	/**
	 * @return the branch
	 */
	public Branch getBranch() {
		return branch;
	}

	/**
	 * @param branch the branch to set
	 */
	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	/**
	 * @return the copies
	 */
	public Integer getNoOfCopies() {
		return noOfCopies;
	}

	/**
	 * @param copies the copies to set
	 */
	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	
	

}
