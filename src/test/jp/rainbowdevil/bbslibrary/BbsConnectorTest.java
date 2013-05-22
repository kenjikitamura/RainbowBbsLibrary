package jp.rainbowdevil.bbslibrary;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import jp.rainbowdevil.bbslibrary.model.Bbs;
import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.Message;
import jp.rainbowdevil.bbslibrary.model.MessageThread;
import jp.rainbowdevil.bbslibrary.net.TestDownloader;
import jp.rainbowdevil.bbslibrary.parser.BbsPerseException;
import jp.rainbowdevil.bbslibrary.parser.NichannelParser;

import org.junit.Before;
import org.junit.Test;

public class BbsConnectorTest {
	
	private Bbs bbs;
	private BbsConnector bbsConnector;
	private TestDownloader downloader;
	private NichannelParser parser;
	
	@Before
	public void setup() throws IOException{
		bbs = new Bbs();
		bbs.setTitle("テストBBS");
		bbs.setUrl("http://example.com");
		bbsConnector = new BbsConnector(bbs);
		downloader = new TestDownloader();
		bbsConnector.setDownloader(downloader);
		parser = new NichannelParser();
	}
	
	@Test
	public void getBoardList() throws IOException, BbsPerseException{
		downloader.inputStream = getClass().getResourceAsStream("/bbsmenu.html");
		List<Board> boardGroups = parser.parseBbsMenu(bbsConnector.getBoardGroup());
		assertThat(boardGroups, notNullValue());
		assertThat(boardGroups.size(), is(46));
	}
	
	@Test
	public void getMessageThread() throws IOException, BbsPerseException{
		Board board = new Board();
		board.setTitle("テスト板");
		board.setUrl("http://hoge");
		downloader.inputStream = getClass().getResourceAsStream("/subject.txt");
		List<MessageThread> messageThreads = bbsConnector.getMessageThreadList(board);
		assertThat(messageThreads, notNullValue());
		assertThat(messageThreads.size(), is(725));
		
		MessageThread thread = null;
		thread = messageThreads.get(0);
		assertThat(thread.getTitle(), is("「中華レストランでは人肉を調理する」スペインの女性司会者発言が問題に・・「華人たちの隠し事をこの番組で暴いてやる」徹底抗戦の構え"));
		assertThat(thread.getFilename(), is("1368614187.dat"));
		assertThat(thread.getSize(), is(73));
		
		thread = messageThreads.get(6);
		assertThat(thread.getTitle(), is("【看過できない】集めればいいんだろ？コンプガチャの実態とは、★２"));
		assertThat(thread.getFilename(), is("9241205101.dat"));
		assertThat(thread.getSize(), is(9));
		
		//System.out.println("size="+messageThreads.size());
		//for(MessageThread messageThread:messageThreads){
		//	System.out.println(" title="+messageThread.getTitle()+" url="+messageThread.getFilename()+" size="+messageThread.getSize()+" createdDate="+messageThread.getCreatedDate().toString());
		//}
	}
	
	@Test
	public void getMessageList() throws IOException, BbsPerseException{
		MessageThread messageThread = new MessageThread();
		messageThread.setCreatedDate(new Date());
		messageThread.setFilename("test.dat");
		messageThread.setNo(1);
		Board board = new Board();
		board.setUrl("http://example.com/");
		downloader.inputStream = getClass().getResourceAsStream("/9241203701.dat");
		List<Message> messages = bbsConnector.getMessageList(messageThread, board);
		assertThat(messages, notNullValue());
		assertThat(messages.size(), is(73));
		
		Message message;
		message = messages.get(0);
		assertThat(message.getUserName(), is("西郷 ★"));
		assertThat(message.getEmail(), is(""));
		assertThat(message.getBody(), is(" 2011/3/11 その時２ちゃんねるは・・・ <br> 全てが克明に記録されています。 <br>  <br> その時最初にたったスレがこれです、いわし ★(http://be.2ch.net/test/p.php?i=99850526)さんGJ <br>  <br> 地震 <br> 1 ：名無しさん＠涙目です。(栃木県)：2011/03/11(金) 14:47:28.80 ID:3ssQKiB30 ?PLT(18072) ポイント特典 <br>   <br> 緊急地震速報  <br>  <br>  <br> http://hato.2ch.net/test/read.cgi/news/1299822448/ <br>  "));
				
		//System.out.println("size="+messages.size());
		//for(Message message1:messages){
		//	System.out.println("name="+message1.getUserName()+" date="+message1.getSubmittedDateString()+" body="+message1.getBody()+" mail="+message1.getEmail());
		//}
	}
}
