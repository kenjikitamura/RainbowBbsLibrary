package jp.rainbowdevil.bbslibrary;

import jp.rainbowdevil.bbslibrary.model.Bbs;

public class BbsManager {
	public BbsConnector createBbsConnector(Bbs bbs){
		BbsConnector bbsConnector = new BbsConnector(bbs);
		return bbsConnector;
	}
}
