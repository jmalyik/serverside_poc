package hu.scarlet.rest.web.dto;

import java.io.Serializable;

/**
 * 
 * @author jmalyik
 *
 * @param <T>
 */
public class BaseResponse<T> implements Serializable{
	private static final long serialVersionUID = 1994474335223262919L;
	private ResponseStatus status = ResponseStatus.OK;
	private String responseMessage;
	private T data;
	public ResponseStatus getStatus() {
		return status;
	}
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
