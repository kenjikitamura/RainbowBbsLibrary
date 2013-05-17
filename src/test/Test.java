import java.io.IOException;
import java.util.List;

import jp.rainbowdevil.bbslibrary.BbsConnector;
import jp.rainbowdevil.bbslibrary.BbsManager;
import jp.rainbowdevil.bbslibrary.ConnectorConfig;
import jp.rainbowdevil.bbslibrary.model.Bbs;
import jp.rainbowdevil.bbslibrary.model.Board;
import jp.rainbowdevil.bbslibrary.model.BoardGroup;
import jp.rainbowdevil.bbslibrary.model.Message;
import jp.rainbowdevil.bbslibrary.model.MessageThread;


/**
 * ライブラリを使用して実際に取得してみるテスト
 * @author Kenji Kitamura
 *
 */
public class Test {
	
	public static void main(String[] args){
		Test test = new Test();
		try {
			test.start();
		} catch (IOException e) {
			System.err.println("エラー発生");
			e.printStackTrace();
		}
	}
	
	private void start() throws IOException{
		Bbs bbs = new Bbs();
		bbs.setUrl("http://menu.2ch.net/bbsmenu.html");
		BbsManager bbsManager = new BbsManager();
		ConnectorConfig config = new ConnectorConfig();
		config.setProxyServer(null);
		config.setProxyPort(8080);
		BbsConnector connector = bbsManager.createBbsConnector(bbs);
		connector.setConnectorConfig(config);
		List<BoardGroup> boardGroups = connector.getBoardGroup();
		if (boardGroups.size() == 0){
			System.out.println("板一覧所得失敗");
			return;
		}
		System.out.println("板一覧取得成功 size="+boardGroups.size() );
		
		BoardGroup boardGroup = boardGroups.get(3);
		Board board = boardGroup.getBoards().get(0);
		
		System.out.println("スレッド一覧取得開始 板="+board.getTitle()+" url="+board.getUrl());
		
		List<MessageThread> messageThreads = connector.getMessageThreadList(board);
		if (messageThreads.size() == 0){
			System.out.println("スレッド一覧取得失敗");
			return;
		}
		System.out.println("スレッド一覧取得成功");
		
		MessageThread messageThread = messageThreads.get(0);
		List<Message> messages = connector.getMessageList(messageThread, board);
		if (messages.size() == 0){
			System.out.println("メッセージ一覧取得失敗");
			return ;
		}
		
		System.out.println("メッセージ一覧取得成功 size="+messages.size() );
		
		Message message = messages.get(0);
		System.out.println("名前="+message.getUserName());
		System.out.println("メール="+message.getEmail());
		System.out.println("本文="+message.getBody());
	}
}
