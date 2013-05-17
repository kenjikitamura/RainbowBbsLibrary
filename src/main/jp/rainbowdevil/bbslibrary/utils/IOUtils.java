package jp.rainbowdevil.bbslibrary.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

	/**
	 * InputStreamをStringに変換する。
	 * 
	 * @param inputStream
	 * @param encode
	 * @return
	 * @throws IOException
	 */
	static public String toString(InputStream inputStream, String encode) throws IOException{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream),encode));
		String line = null;
		while((line = br.readLine()) != null){
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Fileを読み込み、Stringを返す。
	 * 
	 * @param file
	 * @param encode
	 * @return
	 * @throws IOException
	 */
	static public String readFile(File file, String encode) throws IOException{		
		return toString(new FileInputStream(file), encode);
	}
	
	/**
	 * リソースからファイルを読み込み、Stringを返す。
	 * 
	 * @param filename
	 * @param encode
	 * @return
	 * @throws IOException
	 */
	static public String readResource(String filename, String encode) throws IOException{
		return toString(IOUtils.class.getResourceAsStream(filename), encode);
	}
}
