package jp.rainbowdevil.bbslibrary.model;


public class Board {
	private String title;
	private String url;
	private Bbs parentBbs;
	private BoardGroup parentBoardGroup; 
	
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
	public BoardGroup getParentBoardGroup() {
		return parentBoardGroup;
	}
	public void setParentBoardGroup(BoardGroup parentBoardGroup) {
		this.parentBoardGroup = parentBoardGroup;
	}
}
