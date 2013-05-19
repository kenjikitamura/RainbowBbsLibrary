package jp.rainbowdevil.bbslibrary.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.Message;
import jp.rainbowdevil.bbslibrary.model.MessageThread;

/**
 * 各種掲示板のデータをパースし、インスタンスを生成する。
 * 
 * @author Kenji Kitamura
 *
 */
public interface IBbsParser {
	
	public List<Board> parseBbsMenu(InputStream inputStream) throws IOException, BbsPerseException;
	public List<MessageThread> parseMessageThreadList(InputStream inputStream) throws IOException, BbsPerseException;
	public List<Message> parseMessageList(InputStream inputStream) throws IOException, BbsPerseException;

}
