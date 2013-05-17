package jp.rainbowdevil.bbslibrary.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.BoardGroup;
import jp.rainbowdevil.bbslibrary.model.Message;
import jp.rainbowdevil.bbslibrary.model.MessageThread;
import jp.rainbowdevil.bbslibrary.utils.IOUtils;

/**
 * にちゃんねる用パーサ
 * @author Kenji Kitamura
 *
 */
public class NichannelParser implements IBbsParser{
	
	private static String PREFIX_BOARDGROUP = "<BR><BR><B>";
	private static String PREFIX_BOARD = "<A HREF=";
	private static final String CHARACTER_CODE = "shift-jis";


	@Override
	public List<BoardGroup> parseBbsMenu(InputStream inputStream) throws IOException {
		String text = IOUtils.toString(inputStream, CHARACTER_CODE);
		String[] lines = text.split("\n");
		List<BoardGroup> boardGroups = new ArrayList<BoardGroup>();
		BoardGroup boardGroup = null;
		for(String line:lines){
			//System.out.println("line="+line);
			if (line.indexOf(PREFIX_BOARDGROUP) == 0){
				String title = line.substring(PREFIX_BOARDGROUP.length());
				title = title.replaceAll("</B><BR>", "");
				boardGroup = new BoardGroup();
				boardGroup.setTitle(title);
				boardGroups.add(boardGroup);
			}
			if (line.indexOf(PREFIX_BOARD) == 0 && boardGroup != null){
				String data = line.substring(PREFIX_BOARD.length());
				Board board = new Board();
				int index = data.indexOf(">");				
				String url = data.substring(0, index);
				url = url.replaceAll(" TARGET=_blank", "");
				String title = data.substring(index+1,data.indexOf("</A>"));
				board.setTitle(title);
				board.setUrl(url);
				board.setParentBbs(null);
				board.setParentBoardGroup(boardGroup);
				boardGroup.getBoards().add(board);
			}
		}
		return boardGroups;
	}

	
	@Override
	public List<MessageThread> parseMessageThreadList(InputStream inputStream) throws IOException {
		String text = IOUtils.toString(inputStream, CHARACTER_CODE);
		String[] lines = text.split("\n");
		List<MessageThread> messageThreads = new ArrayList<MessageThread>();
		for(String line:lines){
			int index = line.indexOf("<>");
			if (index == -1){
				throw new IllegalStateException("スレ一覧パース中に例外発生。 <>が存在しなかった。 line="+line);
			}
			String filename = line.substring(0, index);
			String title = line.substring(index+2);
			int size = Integer.parseInt(title.substring(title.lastIndexOf("(")+1, title.lastIndexOf("")-1));
			title = title.substring(0, title.lastIndexOf("(")-1);
			long date = Long.parseLong(filename.substring(0, filename.lastIndexOf(".")));  
			MessageThread messageThread = new MessageThread();
			messageThread.setTitle(title);
			messageThread.setFilename(filename);
			messageThread.setSize(size);
			messageThread.setCreatedDate(new Date(date));
			messageThreads.add(messageThread);
		}
		return messageThreads;
	}

	@Override
	public List<Message> parseMessageList(InputStream inputStream) throws IOException {
		String text = IOUtils.toString(inputStream, CHARACTER_CODE);
		String[] lines = text.split("\n");
		List<Message> messages = new ArrayList<Message>();
		for(String line:lines){
			String[] data = line.split("<>");
			Message message = new Message();
			message.setUserName(data[0]);
			message.setEmail(data[1]);
			message.setSubmittedDateString(data[2]);
			message.setBody(data[3]);
			messages.add(message);
		}
		return messages;
	}

}
