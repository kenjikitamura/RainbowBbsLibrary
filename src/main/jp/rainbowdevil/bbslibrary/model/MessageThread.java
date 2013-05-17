package jp.rainbowdevil.bbslibrary.model;

import java.util.Date;

public class MessageThread {
	private int no;
	private String title;
	private String filename;
	private int size;
	private Date createdDate;
	private Board parentBoard;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Board getParentBoard() {
		return parentBoard;
	}
	public void setParentBoard(Board parentBoard) {
		this.parentBoard = parentBoard;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
