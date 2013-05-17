package jp.rainbowdevil.bbslibrary.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TestDownloader implements IDownloader{
	
	//public String contentsString = "";
	//public byte[] bytes;
	public InputStream inputStream;
	
	@Override
	public InputStream getContents(URL url) throws IOException {		
		return inputStream;
	}

	@Override
	public void setProxyPort(int proxyPort) {
	}

	@Override
	public void setProxyServer(String proxyServer) {
	}
}
