package jp.rainbowdevil.bbslibrary.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 便利機能いろいろ
 * @author kitamura
 *
 */
public class IOUtils {

	/**
	 * InputStreamをStringに変換する。
	 * 
	 * @param inputStream
	 * @param characterCode
	 * @return
	 * @throws IOException
	 */
	public static String toString(InputStream inputStream, String characterCode) throws IOException{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream),characterCode));
		String line = null;
		while((line = br.readLine()) != null){
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * InputStreamをbyte配列に変換する。
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream inputStream) throws IOException{
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		byte[] buffer = new byte[10240];// 10kごと読み込み
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int len = 0;
		while((len = bufferedInputStream.read(buffer)) != -1){
			byteArrayOutputStream.write(buffer,0,len);
		}
		return byteArrayOutputStream.toByteArray();
	}
	
	/**
	 * Fileを読み込み、Stringを返す。
	 * 
	 * @param file
	 * @param characterCode
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file, String characterCode) throws IOException{		
		return toString(new FileInputStream(file), characterCode);
	}
	
	/**
	 * FileにStringを書き出す。
	 * 
	 * @param file
	 * @param text
	 * @param characterCode
	 * @throws IOException
	 */
	public static void writeFile(File file, String text, String characterCode) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),characterCode));
		writer.write(text);
		writer.close();
	}
	
	/**
	 * リソースからファイルを読み込み、Stringを返す。
	 * 
	 * @param filename
	 * @param characterCode
	 * @return
	 * @throws IOException
	 */
	public static String readResource(String filename, String characterCode) throws IOException{
		return toString(IOUtils.class.getResourceAsStream(filename), characterCode);
	}
	
	/**
	 * StringをInputStreamに変換する。
	 * @param text
	 * @param characterCode
	 * @return
	 * @throws IOException 
	 */
	public static InputStream stringToInputStream(String text, String characterCode) throws IOException{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, characterCode));
		printWriter.write(text);
		printWriter.close();
		byte[] bytes = outputStream.toByteArray();
		return new ByteArrayInputStream(bytes);
	}
}
