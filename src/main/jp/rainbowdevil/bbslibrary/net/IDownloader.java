package jp.rainbowdevil.bbslibrary.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;

public interface IDownloader {
	public InputStream getContents(URL url) throws IOException;
	//public void setProxyServer(String proxyServer);
	//public void setProxyPort(int proxyPort);
	public void setProxy(Proxy proxy);
}
