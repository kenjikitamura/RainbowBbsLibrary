package jp.rainbowdevil.bbslibrary.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class WebDownloader implements IDownloader{
	private Proxy proxy;
	//private String proxyServer;
	//private int proxyPort;
	
	@Override
	public InputStream getContents(URL url) throws IOException {

		HttpURLConnection conn = null;
		if (proxy == null){
			conn = (HttpURLConnection)url.openConnection();
		}else{
			conn = (HttpURLConnection)url.openConnection(proxy);
		}
		
		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK){
			throw new IOException("ResponseCode is "+responseCode);
		}
		InputStream is = conn.getInputStream();
		return is;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
}
