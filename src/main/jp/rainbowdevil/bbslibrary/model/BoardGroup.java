package jp.rainbowdevil.bbslibrary.model;

import java.util.ArrayList;
import java.util.List;

public class BoardGroup {
	private String title;
	private List<Board> boards;
	private Bbs parentBbs;
	
	public BoardGroup(){
		boards = new ArrayList<Board>();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Board> getBoards() {
		return boards;
	}
	public void setBoards(List<Board> boards) {
		this.boards = boards;
	}
	public Bbs getParentBbs() {
		return parentBbs;
	}
	public void setParentBbs(Bbs parentBbs) {
		this.parentBbs = parentBbs;
	}
	

}
