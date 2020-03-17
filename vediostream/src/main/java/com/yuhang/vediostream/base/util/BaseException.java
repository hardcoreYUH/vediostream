package com.yuhang.vediostream.base.util;

public class BaseException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public BaseException(String message) {
		super(message);
	}

	public BaseException(Throwable e) {
		super(e);
	}

	public BaseException() {
		super();
	}
}
