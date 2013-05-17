package jp.rainbowdevil.bbslibrary.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WebDownloaderTest {
	//@Test
	public void test() throws MalformedURLException, IOException{
		WebDownloader webDownloader = new WebDownloader();
		webDownloader.setProxyServer(null);
		webDownloader.setProxyPort(8080);
		InputStream is = webDownloader.getContents(new URL("http://menu.2ch.net/bbsmenu.html"));
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(is),"shift-jis"));
		String line = null;
		while((line = br.readLine()) != null){
			sb.append(line);
			sb.append("\n");
		}
		//System.out.println(sb.toString());
		File file = new File("./bbsmenu.html");
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
		outputStream.write(sb.toString().getBytes());
		outputStream.close();
		
	}
}
