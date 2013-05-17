package jp.rainbowdevil.bbslibrary.parser;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.BoardGroup;

import org.junit.Before;
import org.junit.Test;
public class NichannelParserTest {
	
	private NichannelParser parser;
	
	@Before
	public void setUp(){
		parser = new NichannelParser();
	}
	
	@Test
	public void parseBbsMenu() throws IOException{
		List<BoardGroup> boardGroups = parser.parseBbsMenu(getClass().getResourceAsStream("/bbsmenu.html"));
		
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
		
		/*
		for(BoardGroup boardGroup2:boardGroups){
			System.out.println("BoardGourp title="+boardGroup2.getTitle() + " size="+boardGroup2.getBoards().size());
			for(Board board2:boardGroup2.getBoards()){
				System.out.println("  Board title="+board2.getTitle()+" url="+board2.getUrl());
			}
		}
		*/
	}

}
