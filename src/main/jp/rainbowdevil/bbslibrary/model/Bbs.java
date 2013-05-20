package jp.rainbowdevil.bbslibrary.model;


public class Bbs {
	/** 画面表示用 */
	private String title;
	
	/** 内部用ID */
	private String id;
	
	/** 掲示板URL */
	private String url;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
