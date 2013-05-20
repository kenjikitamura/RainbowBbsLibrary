package jp.rainbowdevil.bbslibrary.repository;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.rainbowdevil.bbslibrary.model.Bbs;
import jp.rainbowdevil.bbslibrary.model.MessageThread;

/**
 * 掲示板情報の保存用クラス
 * 
 * メモ
 * ローカルに複数の掲示板、板、スレッドの情報を保存できるようにする。
 * 
 * DATファイルの保存先は以下のようにする。
 *  保存パス/掲示板名/板名/DATファイル名
 * 例:
 *  .iris/log/2ch/diplomacy/12345.dat
 *  
 *  
 *  
 * @author Kenji Kitamura
 *
 */
public class BbsRepository {
	
	public static final String DEFAULT_REPOSITORY_PATH = ".iris/log/";
	
	public static final String BOARD_LIST_FILE = "/boards.dat";
	
	/** 掲示板情報を保存するパス */
	private String bbsRepositoryPath = DEFAULT_REPOSITORY_PATH;
	
	/**
	 * 掲示板レポジトリの初期化を行う。
	 */
	public void init(){
		setupDirectory();
	}
	
	/**
	 * レポジトリパスのディレクトリを作成する。
	 */
	public void setupDirectory(){
		File dir = new File(bbsRepositoryPath);
		if (!dir.exists()){
			boolean ret = dir.mkdirs();
		}
	}
	
	/**
	 * 板一覧を保存する。
	 * 
	 * パスの例
	 * log/2ch/boards.html
	 * 
	 * @param bbs
	 * @param boards
	 * 
	 * @throws IOException 
	 */
	public void writeBoardList(Bbs bbs, byte[] data) throws IOException{
		String path = getBoardListFilePath(bbs);
		write(new File(path), data);
	}
	
	/**
	 * 保存した板一覧のInputStreamを取得する。
	 * @param bbs
	 * @return
	 * @throws FileNotFoundException 
	 */
	public InputStream loadBoardList(Bbs bbs) throws FileNotFoundException{
		String path = getBoardListFilePath(bbs);
		FileInputStream fileInputStream = new FileInputStream(new File(path));
		return fileInputStream;
	}
	
	String getBoardListFilePath(Bbs bbs){ 
		String path = getBbsRepositoryPath() + bbs.getId()+ BOARD_LIST_FILE;
		return path;
	}
	
	/**
	 * スレッドを保存する。
	 * 
	 * @param messageThread
	 */
	public void writeMessageThread(MessageThread messageThread, byte[] data){
		
	}

	public String getBbsRepositoryPath() {
		return bbsRepositoryPath;
	}

	public void setBbsRepositoryPath(String bbsRepositoryPath) {
		this.bbsRepositoryPath = bbsRepositoryPath;
	}
	
	private void write(File file,byte[] data) throws IOException{
		File dir = file.getParentFile();
		if (!dir.exists()){
			dir.mkdirs();
		}
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
		outputStream.write(data);
		outputStream.close();		
	}

}
