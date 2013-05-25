package jp.rainbowdevil.bbslibrary.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jp.rainbowdevil.bbslibrary.model.Bbs;
import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.Message;
import jp.rainbowdevil.bbslibrary.model.MessageThread;
import jp.rainbowdevil.bbslibrary.parser.BbsPerseException;
import jp.rainbowdevil.bbslibrary.parser.NichannelParser;
import jp.rainbowdevil.bbslibrary.utils.IOUtils;

import org.junit.Before;
import org.junit.Test;

public class BbsRepositoryTest {
	
	private BbsRepository repository;
	private String TEST_REPOSITORY_PATH = "testrepo/log/";

	@Before
	public void setUp() throws Exception {
		repository = new BbsRepository();
		repository.setBbsRepositoryPath(TEST_REPOSITORY_PATH);
	}
	
	@Test
	public void 板一覧の保存() throws IOException{
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("abc");
		String html = "<html>hoge</html>\n";
		byte[] bytes = html.getBytes();
		
		// Exercise
		repository.init();
		repository.writeBoardList(bbs, bytes);
		
		// Verify
		File file = new File(TEST_REPOSITORY_PATH+bbs.getId()+"/"+BbsRepository.BOARD_LIST_FILE);
		assertThat(file.exists(), is(true));
		String readHtml = IOUtils.readFile(file, "shift-jis");
		assertThat(readHtml, is(html));
		
		// 後片付け
		file.delete();
	}
	
	@Test
	public void 板一覧の読み込み_存在しない場合() throws FileNotFoundException{
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("abcd");
				
		// Exercise
		repository.init();
		try{
			InputStream inputStream = repository.loadBoardList(bbs);
			fail();
		}catch(FileNotFoundException e){
			
		}
	}
	
	@Test
	public void 保存した板一覧の読み込み() throws IOException, BbsPerseException{
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("abc");
		String html = "<BR><BR><B>地震</B><BR>\n" +
				"<A HREF=http://headline.2ch.net/bbynamazu/>地震headline</A><br>\n" +
				"<A HREF=http://anago.2ch.net/namazuplus/>地震速報</A><br>\n";
		File file = new File(TEST_REPOSITORY_PATH+bbs.getId()+"/"+BbsRepository.BOARD_LIST_FILE);
		IOUtils.writeFile(file, html, "shift-jis");
		NichannelParser nichannelParser = new NichannelParser();
				
		// Exercise
		repository.init();
		InputStream inputStream = repository.loadBoardList(bbs);
		List<Board> boards = nichannelParser.parseBbsMenu(inputStream, bbs);
				
		// Verify
		assertThat(boards, is(notNullValue()));
		assertThat(boards.size(), is(1));
		assertThat(boards.get(0).getTitle(), is("地震"));
				
		// 後片付け
		file.delete();
	}
	
	@Test
	public void スレッド一覧ファイル保存先パスの確認(){
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("bbs");
		Board board = new Board();
		board.setId("board");
		board.setParentBbs(bbs);
		
		// Exercise
		String actual = repository.getMessageThreadListFilePath(board);
		
		// Verify
		String expected = TEST_REPOSITORY_PATH+"bbs"+File.separator+"board" + File.separator + "subject.txt";
		assertThat(actual, is(expected));
	}
	
	@Test
	public void スレッド一覧ファイル保存先パスの確認_nullpo(){
		// Setup
		Board board = new Board();
		board.setId("board");
		
		// Exercise
		try{
			String actual = repository.getMessageThreadListFilePath(board);
			fail();
		}catch(NullPointerException e){
			// 親BBSを設定しないBoardのgetMessageThreadListFilePathを実行するとNullPointerExceptionが発生する。 
		}
	}
	
	@Test
	public void スレッド一覧ファイルの保存テスト() throws IOException{
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("bbs");
		Board board = new Board();
		board.setId("board");
		board.setParentBbs(bbs);
		byte[] data = "hogehoge\n".getBytes();
		File file = new File(TEST_REPOSITORY_PATH+"bbs"+File.separator+"board" + File.separator + "subject.txt");
		file.delete();
		
		// Exercise
		repository.writeMessageThreadList(board, data);
		
		// Verify
		assertThat(file.exists(), is(true));
		String readHtml = IOUtils.readFile(file, "shift-jis");
		assertThat(readHtml, is("hogehoge\n"));
		
		// TearDown
		file.delete();
	}
	
	@Test
	public void 保存したスレッド一覧の読み込み() throws IOException, BbsPerseException{
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("abc");
		Board board = new Board();
		board.setId("board");
		board.setParentBbs(bbs);
		String html = "1368614187.dat<>あいう (73)\n" +
				"1368099304.dat<>小学生のガキがが書いた“ごんぎつね”の感想文「こそこそした罪滅ぼしは身勝手で自己満足、撃たれて当たり前」 (585)\n" +
				"1368274602.dat<>韓国人に俳句を教えてみた (234)\n";
		File file = new File(TEST_REPOSITORY_PATH+bbs.getId()+File.separator+board.getId()+File.separator+"subject.txt");
		file.getParentFile().mkdirs();
		IOUtils.writeFile(file, html, "shift-jis");
		NichannelParser nichannelParser = new NichannelParser();
				
		// Exercise
		repository.init();
		InputStream inputStream = repository.loadMessageThreadList(board);
		List<MessageThread> messageThreads = nichannelParser.parseMessageThreadList(inputStream);
				
		// Verify
		assertThat(messageThreads, is(notNullValue()));
		assertThat(messageThreads.size(), is(3));
		assertThat(messageThreads.get(0).getTitle(), is("あいう"));
		assertThat(messageThreads.get(0).getSize(), is(73));
				
		// TearDown
		file.delete();
	}
	
	@Test
	public void DATファイル保存テスト() throws IOException{
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("bbs");
		Board board = new Board();
		board.setId("board");
		board.setParentBbs(bbs);
		MessageThread messageThread = new MessageThread();
		messageThread.setFilename("123.dat");
		messageThread.setParentBoard(board);
		byte[] data = "hogehoge\n".getBytes();
		File file = new File(TEST_REPOSITORY_PATH+"bbs"+File.separator+"board" + File.separator + messageThread.getFilename());
		file.delete();
		assertThat(file.exists(), is(false));
				
		// Exercise
		repository.writeMessageThread(messageThread, data);
				
		// Verify
		assertThat(file.exists(), is(true));
		String readHtml = IOUtils.readFile(file, "shift-jis");
		assertThat(readHtml, is("hogehoge\n"));
			
		// TearDown
		file.delete();
	}
	
	@Test
	public void DATファイル保存先パスの確認(){
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("bbs");
		Board board = new Board();
		board.setId("board");
		board.setParentBbs(bbs);
		MessageThread messageThread = new MessageThread();
		messageThread.setFilename("hoge.dat");
		messageThread.setParentBoard(board);
		
		// Exercise
		String actual = repository.getMessageThreadFilePath(messageThread);
		
		// Verify
		String expected = TEST_REPOSITORY_PATH+"bbs"+File.separator+"board"+ File.separator +"hoge.dat";
		assertThat(actual, is(expected));
	}
	
	@Test
	public void 保存したDATファイルの読み込み() throws IOException, BbsPerseException{
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("abc");
		Board board = new Board();
		board.setId("board");
		board.setParentBbs(bbs);
		MessageThread messageThread = new MessageThread();
		messageThread.setFilename("hoge.dat");
		messageThread.setParentBoard(board);
		String html = "モルモットさん（金）</b>(地震なし)<b><><>2012/03/05(月) 00:07:08.50 ID:LH6rWB8b0<> ＜●＞＜●＞ <>\n" +
				"モルモットさん（金）</b>(地震なし)<b><><>2012/03/05(月) 00:08:09.54 ID:OU5SkL5g0<> 924にするぞー <>\n" +
				" </b>【IQ5】<b> </b>(地震なし)<b><>sage<>2012/03/05(月) 00:08:41.70 ID:BO5Ht+6h0<> 海洋観察しよう <>\n";
		File file = new File(TEST_REPOSITORY_PATH+bbs.getId()+File.separator+board.getId()+File.separator+messageThread.getFilename());
		file.getParentFile().mkdirs();
		IOUtils.writeFile(file, html, "shift-jis");
		NichannelParser nichannelParser = new NichannelParser();
				
		// Exercise
		repository.init();
		InputStream inputStream = repository.loadMessageThread(messageThread);
		List<Message> messages = nichannelParser.parseMessageList(inputStream);
				
		// Verify
		assertThat(messages, is(notNullValue()));
		assertThat(messages.size(), is(3));
		assertThat(messages.get(0).getUserName(), is("モルモットさん（金）</b>(地震なし)<b>"));
		assertThat(messages.get(0).getBody(), is(" ＜●＞＜●＞ "));
				
		// TearDown
		file.delete();
	}
}
