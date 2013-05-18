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


	/**
	 * 板一覧のパース
	 * 
	 * パースに失敗した場合はBbsPerseExceptionをthrowする。
	 * パースした結果、板一覧が見つからなかった場合もBbsPerseExceptionをthrowする。
	 * 
	 * @param inputStream
	 */
	@Override 
	public List<BoardGroup> parseBbsMenu(InputStream inputStream) throws IOException, BbsPerseException {
		String text = IOUtils.toString(inputStream, CHARACTER_CODE);
		String[] lines = text.split("\n");
		List<BoardGroup> boardGroups = new ArrayList<BoardGroup>();
		BoardGroup boardGroup = null;
		for(String line:lines){
			if (line.indexOf(PREFIX_BOARDGROUP) == 0){
				String title = line.substring(PREFIX_BOARDGROUP.length());
				title = title.replaceAll("</B><BR>", "");
				boardGroup = new BoardGroup();
				boardGroup.setTitle(title);
				boardGroups.add(boardGroup);
			}
			if (line.indexOf(PREFIX_BOARD) == 0 && boardGroup != null){
				try{
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
				}catch(IndexOutOfBoundsException e){
					throw new BbsPerseException("板のパースに失敗 line="+line, e);
				}
			}
				
		}
		
		if (boardGroups.size() == 0){
			throw new BbsPerseException("パースした結果、板が見つかりませんでした。");
		}
			
		return boardGroups;
	}

	
	@Override
	public List<MessageThread> parseMessageThreadList(InputStream inputStream) throws IOException , BbsPerseException{
		String text = IOUtils.toString(inputStream, CHARACTER_CODE);
		String[] lines = text.split("\n");
		List<MessageThread> messageThreads = new ArrayList<MessageThread>();
		for(String line:lines){
			int index = line.indexOf("<>");
			if (index == -1){
				throw new BbsPerseException("スレ一覧パース中に例外発生。 <>が存在しなかった。 line="+line);
			}
			String filename = line.substring(0, index);
			String title = line.substring(index+2);
			int startSizeIndex = title.lastIndexOf("(");
			int endSizeIndex = title.lastIndexOf(")");
			if (startSizeIndex == -1 || endSizeIndex == -1){
				throw new BbsPerseException("スレッド一覧でコメ数が見つからなかった。 line="+line);
			}
			int size = -1;
			try{
				size = Integer.parseInt(title.substring(startSizeIndex+1, endSizeIndex));
			}catch(NumberFormatException e){
				throw new BbsPerseException("コメ数のIntegerパースに失敗 start="+startSizeIndex+" end="+endSizeIndex+" line="+line,e);
			}
			title = title.substring(0, startSizeIndex-1);
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
	public List<Message> parseMessageList(InputStream inputStream) throws IOException , BbsPerseException{
		String text = IOUtils.toString(inputStream, CHARACTER_CODE);
		String[] lines = text.split("\n");
		List<Message> messages = new ArrayList<Message>();
		for(String line:lines){
			String[] data = line.split("<>");
			if (data.length < 4){
				throw new BbsPerseException("DATファイルのパースに失敗 line="+line);
			}
			String dateString = data[2];
			String userHash = null;
			String submittedDate = null;
			int idIndex = dateString.indexOf("ID:");
			int beIndex = dateString.indexOf("BE:");
			
			// 投稿日取得
			if (idIndex == -1 && beIndex == -1){
				submittedDate = dateString;
			}else{
				// IDがあり、BE-IDが無い
				if (idIndex != -1 && beIndex == -1){
					submittedDate = dateString.substring(0,idIndex -1);
					
				// IDが無く、BE-IDがある
				}else if (idIndex == -1 && beIndex != -1){
					submittedDate = dateString.substring(0,beIndex-1);
				
				// IDもBE-IDもある(ありえる？)
				}else{
					submittedDate = dateString.substring(0,idIndex -1);
				}				
			}
			
			// ID取得
			if (idIndex != -1){
				if (beIndex != -1){
					userHash = dateString.substring(idIndex + 3,beIndex);
				}else{
					userHash = dateString.substring(idIndex + 3);
				}
			}
			Message message = new Message();
			message.setUserName(data[0]);
			message.setEmail(data[1]);
			message.setSubmittedDateString(submittedDate);
			message.setUserHash(userHash);
			message.setBody(data[3]);
			messages.add(message);
		}
		return messages;
	}

}
