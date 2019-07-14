package com.owu.geekhub.jwtmessage.response;

import lombok.Data;

@Data
public class ResponseMessage {
	private String message;
	private Object object;

	public ResponseMessage(String message, Object object) {
		this.message = message;
		this.object = object;
	}

	public ResponseMessage(String message) {
		this.message = message;
	}
}
