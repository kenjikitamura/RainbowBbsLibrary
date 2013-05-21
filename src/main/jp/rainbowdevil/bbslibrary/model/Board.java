package jp.rainbowdevil.bbslibrary.model;

import java.util.ArrayList;
import java.util.List;


public class Board {
	private String title;
	private String url;
	private String id;
	private Bbs parentBbs;
	private List<Board> children;
	private Board parentBoard;
	
	public Board(){
		children = new ArrayList<Board>();
	}
	
	@Override
	public String toString() {
		return title;
	}
	
	public List<Board> getChildren() {
		return children;
	}
	public void setChildren(List<Board> children) {
		this.children = children;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Bbs getParentBbs() {
		return parentBbs;
	}
	public void setParentBbs(Bbs parentBbs) {
		this.parentBbs = parentBbs;
	}
	public Board getParentBoard() {
		return parentBoard;
	}

	public void setParentBoard(Board parentBoard) {
		this.parentBoard = parentBoard;
	}
	public boolean hasChildren(){
		if (children == null || children.size() == 0){
			return false;
		}else{
			return true;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
