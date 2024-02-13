package com.amway.booking;

/**
 * 业务异常
 * 
 * @author lisy
 * 
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 5606288089716222981L;

	private String errorMsg;

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message) {
		super(message);
		this.setErrorMsg(message);
	}

	public BusinessException(String message,Exception e) {
		super(e);
		this.setErrorMsg(message);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
