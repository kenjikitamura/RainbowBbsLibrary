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
		// setup
		InputStream inputStream = IOUtils.stringToInputStream("", "shift-jis");
		
		// Excercise
		try{
			List<BoardGroup> boardGroups = parser.parseBbsMenu(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerserExceptionが発生すること
		}
	}
	
	@Test
	public void 板一覧のパース_不完全なデータ() throws IOException{
		// setup
		InputStream inputStream = IOUtils.stringToInputStream(
				"<A HREF=http://www.2ch.net/ TARGET=\"_top\">2chの入り口</A><br>\n" +
				"<A HREF=http://info.2ch.net/guide/>2ch総合案内</A>\n" +
				"\n" +
				"<BR><BR><B>地震</B><BR>\n" +
				"<A HREF=http://headline.2ch.net/bbynamazu/>地震", "shift-jis");
		
		// Excercise
		try{
			List<BoardGroup> boardGroups = parser.parseBbsMenu(inputStream);
			fail();
		}catch(BbsPerseException e){
			// BbsPerserExceptionが発生すること
		}
	}
}
