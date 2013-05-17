package jp.rainbowdevil.bbslibrary.model;

import java.util.Date;

public class Message {
	private int no;
	private String userName;
	private String email;
	private Date submittedDate;
	private String submittedDateString;
	private String userHash;
	private String body;
	private MessageThread parentMessageThread;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getSubmittedDate() {
		return submittedDate;
	}
	public void setSubmittedDate(Date submitedDate) {
		this.submittedDate = submitedDate;
	}
	public String getUserHash() {
		return userHash;
	}
	public void setUserHash(String userHash) {
		this.userHash = userHash;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public MessageThread getParentMessageThread() {
		return parentMessageThread;
	}
	public void setParentMessageThread(MessageThread parentMessageThread) {
		this.parentMessageThread = parentMessageThread;
	}
	public String getSubmittedDateString() {
		return submittedDateString;
	}
	public void setSubmittedDateString(String submittedDateString) {
		this.submittedDateString = submittedDateString;
	}
}
