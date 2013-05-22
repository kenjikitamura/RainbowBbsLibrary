package jp.rainbowdevil.bbslibrary;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import jp.rainbowdevil.bbslibrary.model.Bbs;
import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.Message;
import jp.rainbowdevil.bbslibrary.model.MessageThread;
import jp.rainbowdevil.bbslibrary.net.IDownloader;
import jp.rainbowdevil.bbslibrary.net.WebDownloader;
import jp.rainbowdevil.bbslibrary.parser.BbsPerseException;
import jp.rainbowdevil.bbslibrary.parser.NichannelParser;

public class BbsConnector {
	
	private Bbs bbs;
	private IDownloader downloader;
	private ConnectorConfig connectorConfig;
	
	public BbsConnector(Bbs bbs){
		this.bbs = bbs;
		downloader = new WebDownloader();
		connectorConfig = new ConnectorConfig();
	}
	
	public List<Board> getBoardGroup() throws IOException, BbsPerseException{
		setupDownloader();
		InputStream inputStream = downloader.getContents(new URL(bbs.getUrl()));
		NichannelParser parser = new NichannelParser();
		List<Board> boardGroups = parser.parseBbsMenu(inputStream);		
		return boardGroups;
	}
	
	public List<MessageThread> getMessageThreadList(Board board) throws IOException, BbsPerseException{
		setupDownloader();
		InputStream inputStream = downloader.getContents(new URL(board.getUrl()+"subject.txt"));
		NichannelParser parser = new NichannelParser();
		List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
		for(MessageThread messageThread:messageThreads){
			messageThread.setParentBoard(board);
		}
		return messageThreads;
	}
	
	public List<Message> getMessageList(MessageThread messageThread,Board board) throws IOException, BbsPerseException{
		setupDownloader();
		InputStream inputStream = downloader.getContents(new URL(board.getUrl()+"dat/"+messageThread.getFilename()));
		NichannelParser parser = new NichannelParser();
		List<Message> messages = parser.parseMessageList(inputStream);	
		for(Message message:messages){
			message.setParentMessageThread(messageThread);
		}
		return messages;
	}
	
	private void setupDownloader(){
		downloader.setProxyServer(connectorConfig.getProxyServer());
		downloader.setProxyPort(connectorConfig.getProxyPort());
	}

	IDownloader getDownloader() {
		return downloader;
	}

	void setDownloader(IDownloader downloader) {
		this.downloader = downloader;
	}

	public ConnectorConfig getConnectorConfig() {
		return connectorConfig;
	}

	public void setConnectorConfig(ConnectorConfig connectorConfig) {
		this.connectorConfig = connectorConfig;
	}

}
