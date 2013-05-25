package jp.rainbowdevil.bbslibrary;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;

import jp.rainbowdevil.bbslibrary.model.Bbs;
import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.MessageThread;
import jp.rainbowdevil.bbslibrary.net.IDownloader;
import jp.rainbowdevil.bbslibrary.net.WebDownloader;
import jp.rainbowdevil.bbslibrary.parser.BbsPerseException;

public class BbsConnector {
	
	private Bbs bbs;
	private IDownloader downloader;
	private Proxy proxy;
	//private ConnectorConfig connectorConfig;
	
	public BbsConnector(Bbs bbs){
		this.bbs = bbs;
		downloader = new WebDownloader();
		//connectorConfig = new ConnectorConfig();
	}
	
	public InputStream getBoardGroup() throws IOException, BbsPerseException{
		setupDownloader();
		InputStream inputStream = downloader.getContents(new URL(bbs.getUrl()));
		return inputStream;
	}
	

	public InputStream getMessageThreadList(Board board) throws IOException, BbsPerseException{
		setupDownloader();
		InputStream inputStream = downloader.getContents(new URL(board.getUrl()+"subject.txt"));
		//NichannelParser parser = new NichannelParser();
		//List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
		//for(MessageThread messageThread:messageThreads){
		//	messageThread.setParentBoard(board);
		//}
		return inputStream;
	}
	
	public InputStream getMessageList(MessageThread messageThread,Board board) throws IOException, BbsPerseException{
		setupDownloader();
		InputStream inputStream = downloader.getContents(new URL(board.getUrl()+"dat/"+messageThread.getFilename()));
		return inputStream;
		//NichannelParser parser = new NichannelParser();
		//List<Message> messages = parser.parseMessageList(inputStream);	
		//for(Message message:messages){
		//	message.setParentMessageThread(messageThread);
		//}
		//return messages;
	}
	
	private void setupDownloader(){
		downloader.setProxy(proxy);
	}

	IDownloader getDownloader() {
		return downloader;
	}

	void setDownloader(IDownloader downloader) {
		this.downloader = downloader;
	}
/*
	public ConnectorConfig getConnectorConfig() {
		return connectorConfig;
	}

	public void setConnectorConfig(ConnectorConfig connectorConfig) {
		this.connectorConfig = connectorConfig;
	}
*/

	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
}
