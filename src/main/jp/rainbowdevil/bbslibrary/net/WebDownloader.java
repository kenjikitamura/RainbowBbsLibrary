package jp.rainbowdevil.bbslibrary.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class WebDownloader implements IDownloader{
	
	private String proxyServer;
	private int proxyPort;
	
	@Override
	public InputStream getContents(URL url) throws IOException {

		HttpURLConnection conn = null;
		if (proxyServer == null){
			conn = (HttpURLConnection)url.openConnection();
		}else{
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyServer, proxyPort));
			conn = (HttpURLConnection)url.openConnection(proxy);
		}
		
		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK){
			throw new IOException("ResponseCode is "+responseCode);
		}
		InputStream is = conn.getInputStream();
		return is;
	}

	public String getProxyServer() {
		return proxyServer;
	}

	public void setProxyServer(String proxyServer) {
		this.proxyServer = proxyServer;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
}
