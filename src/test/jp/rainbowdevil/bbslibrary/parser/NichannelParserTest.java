package jp.rainbowdevil.bbslibrary.parser;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.BoardGroup;
import jp.rainbowdevil.bbslibrary.model.Message;
import jp.rainbowdevil.bbslibrary.model.MessageThread;
import jp.rainbowdevil.bbslibrary.utils.IOUtils;

import org.junit.Before;
import org.junit.Test;
public class NichannelParserTest {
	private NichannelParser parser;
	
	@Before
	public void setUp(){
		parser = new NichannelParser();
	}
	
	@Test
	public void 板一覧のパース正常系() throws IOException, BbsPerseException{
		// Exercise
		List<BoardGroup> boardGroups = parser.parseBbsMenu(getClass().getResourceAsStream("/bbsmenu.html"));
		
		// Verify
		assertThat(boardGroups, notNullValue());
		assertThat(boardGroups.size(), is(46));
		
		BoardGroup boardGroup = boardGroups.get(0);
		assertThat(boardGroup, notNullValue());
		assertThat(boardGroup.getTitle(), is("地震"));
		assertThat(boardGroup.getBoards().size(), is(5));
		
		Board board = boardGroup.getBoards().get(0);
		assertThat(board.getTitle(), is("地震headline"));
		assertThat(board.getUrl().toString(), is("http://headline.2ch.net/bbynamazu/"));
		
		board = boardGroup.getBoards().get(2);
		assertThat(board.getTitle(), is("臨時地震"));
		assertThat(board.getUrl().toString(), is("http://hayabusa.2ch.net/eq/"));
	}
	
	@Test
	public void 板一覧のパース_空文字列の入力() throws IOException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream("", "shift-jis");
		
		// Exercise
		try{
			List<BoardGroup> boardGroups = parser.parseBbsMenu(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerserExceptionが発生すること
		}
	}
	
	@Test
	public void 板一覧のパース_不完全なデータ_板グループ名の終端が無い() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream(
				"<A HREF=http://www.2ch.net/ TARGET=\"_top\">2chの入り口</A><br>\n" +
				"<A HREF=http://info.2ch.net/guide/>2ch総合案内</A>\n" +
				"\n" +
				"<BR><BR><B>地震</B>\n", "shift-jis");
		
		// Exercise
		List<BoardGroup> boardGroups = parser.parseBbsMenu(inputStream);
		
		// Verify
		assertThat(boardGroups.size(), is(1));
		assertThat(boardGroups.get(0).getTitle(), is("地震</B>")); // <BR><BR><B>以降の文字列から</B><BR>を削除するので、不完全な場合はゴミが残る
	}
	
	@Test
	public void 板一覧のパース_不完全なデータ_板名の終端が無い() throws IOException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream(
				"<A HREF=http://www.2ch.net/ TARGET=\"_top\">2chの入り口</A><br>\n" +
				"<A HREF=http://info.2ch.net/guide/>2ch総合案内</A>\n" +
				"\n" +
				"<BR><BR><B>地震</B><BR>\n" +
				"<A HREF=http://headline.2ch.net/bbynamazu/>地震", "shift-jis");
		
		// Exercise
		try{
			List<BoardGroup> boardGroups = parser.parseBbsMenu(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerserExceptionが発生すること
		}
	}
	
	@Test
	public void スレッド一覧のパース_正常系() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = getClass().getResourceAsStream("/subject.txt");
		
		// Exercise
		List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
		
		// Verify
		assertThat(messageThreads, is(notNullValue()));
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
	}
	
	@Test
	public void スレッド一覧のパース_不完全なデータ_空文字列() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream("", "shift-jis");
		
		// Exercise
		try{
			List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
	
	@Test
	public void スレッド一覧のパース_不完全なデータ_行の途中1() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream("1368272247.dat<>これは葉の汁を吸うアブラムシ♪　グンマーで激増 (49)\n" +
				"1368710541.dat<>【政治】自分か", "shift-jis");
		
		// Exercise
		try{
			List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
	
	@Test
	public void スレッド一覧のパース_不完全なデータ_行の途中2() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream("1368272247.dat<>これは葉の汁を吸うアブラムシ♪　グンマーで激増 (49)\n" +
				"1368710541.da", "shift-jis");
		
		// Exercise
		try{
			List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
	
	@Test
	public void スレッド一覧のパース_不完全なデータ_行の途中3() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream("1368272247.dat<>これは葉の汁を吸うアブラムシ♪　グンマーで激増 (49)\n" +
				"1368710541.dat<>【政治】自分か(", "shift-jis");
		
		// Exercise
		try{
			List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
	
	@Test
	public void スレッド一覧のパース_不完全なデータ_行の途中4() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream("1368272247.dat<>これは葉の汁を吸うアブラムシ♪　グンマーで激増 (49)\n" +
				"1368710541.dat<>【政治】自分か)", "shift-jis");
		
		// Exercise
		try{
			List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
	
	@Test
	public void スレッド一覧のパース_不完全なデータ_コメ数が不正() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream("1368272247.dat<>これは葉の汁を吸うアブラムシ♪　グンマーで激増 (49)\n" +
				"1368710541.dat<>【政治】自分か(FUGA)\n", "shift-jis");
		
		// Exercise
		try{
			List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
	
	@Test
	public void スレッド一覧のパース_不完全なデータ1() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream("1368272247.dat<>これは葉の汁を吸うアブラムシ♪　グンマーで激増 (49)\n" +
				"<>\n", "shift-jis");
		
		// Exercise
		try{
			List<MessageThread> messageThreads = parser.parseMessageThreadList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
	
	@Test
	public void レス一覧のパース_正常系() throws IOException, BbsPerseException{
		// Setup
		InputStream inputStream = getClass().getResourceAsStream("/9241203701.dat");
		
		// Exercise
		List<Message> messages = parser.parseMessageList(inputStream);
		
		// Verify
		assertThat(messages, notNullValue());
		assertThat(messages.size(), is(73));
		
		Message message;
		message = messages.get(0);
		assertThat(message.getUserName(), is("西郷 ★"));
		assertThat(message.getEmail(), is(""));
		assertThat(message.getBody(), is(" 2011/3/11 その時２ちゃんねるは・・・ <br> 全てが克明に記録されています。 <br>  <br> その時最初にたったスレがこれです、いわし ★(http://be.2ch.net/test/p.php?i=99850526)さんGJ <br>  <br> 地震 <br> 1 ：名無しさん＠涙目です。(栃木県)：2011/03/11(金) 14:47:28.80 ID:3ssQKiB30 ?PLT(18072) ポイント特典 <br>   <br> 緊急地震速報  <br>  <br>  <br> http://hato.2ch.net/test/read.cgi/news/1299822448/ <br>  "));
	}
	
	@Test
	public void レス一覧のパース_正常系2() throws IOException, BbsPerseException {
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream(
				"モルモットさん（金）</b>(地震なし)<b><>sage<>2012/03/05(月) 00:04:17.23 ID:7yWZ2l3x0<> ありゃりゃ <>\n" +
				"モルモットさん（金）</b>(地震なし)<b><><>2012/03/05(月) 00:07:08.50 ID:LH6rWB8b0<> ＜●＞＜●＞ <>\n" +
				"モルモットさん（金）</b>(地震なし)<b><><>2012/03/05(月) 00:08:09.54 ID:OU5SkL5g0<> 924にするぞー <>\n"
				, "shift-jis");

		// Exercise
		List<Message> messages = parser.parseMessageList(inputStream);

		// Verify
		assertThat(messages, notNullValue());
		assertThat(messages.size(), is(3));

		Message message;
		message = messages.get(0);
		assertThat(message.getUserName(), is("モルモットさん（金）</b>(地震なし)<b>"));
		assertThat(message.getEmail(), is("sage"));
		assertThat(message.getSubmittedDateString(), is("2012/03/05(月) 00:04:17.23"));
		assertThat(message.getUserHash(), is("7yWZ2l3x0"));
		assertThat(message.getBody(), is(" ありゃりゃ "));
	}
	
	@Test
	public void レス一覧のパース_データが不完全1() throws IOException, BbsPerseException {
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream(
				"モルモットさん（金）</b>(地震なし)<b><>sage<>2012/03/05(月) 00:04:17.23 ID:7yWZ2l3x0<> ありゃりゃ <>\n" +
				"モルモットさん（金）</b>(地震\n" +
				"モルモットさん（金）</b>(地震なし)<b><><>2012/03/05(月) 00:08:09.54 ID:OU5SkL5g0<> 924にするぞー <>\n"
				, "shift-jis");

		// Exercise
		try{
			List<Message> messages = parser.parseMessageList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
	
	@Test
	public void レス一覧のパース_データが不完全2() throws IOException, BbsPerseException {
		// Setup
		InputStream inputStream = IOUtils.stringToInputStream(
				"モルモットさん（金）</b>(地震なし)<b><>sage<>2012/03/05(月) 00:04:17.23 ID:7yWZ2l3x0<> ありゃりゃ <>\n" +
				"モルモットさん（金）</b>(地震なし)<b><><>2012/03/05(月) 00:07:08.50 ID:LH6rWB8b0 ＜●＞＜●＞ \n" +
				"モルモットさん（金）</b>(地震なし)<b><><>2012/03/05(月) 00:08:09.54 ID:OU5SkL5g0<> 924にするぞー <>\n"
				, "shift-jis");

		// Exercise
		try{
			List<Message> messages = parser.parseMessageList(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerseExceptionが発生すること
		}
	}
}
