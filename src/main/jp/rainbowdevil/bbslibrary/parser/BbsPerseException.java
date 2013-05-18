package jp.rainbowdevil.bbslibrary.parser;

public class BbsPerseException extends Exception{
	public BbsPerseException(String message, Throwable throwable){
		super(message, throwable);
	}
	public BbsPerseException(String message){
		super(message);
	}
}
