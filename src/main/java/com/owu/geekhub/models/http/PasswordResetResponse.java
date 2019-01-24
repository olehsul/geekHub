package com.owu.geekhub.models.http;

import com.owu.geekhub.jwtmessage.response.ResponseMessage;
import org.springframework.http.HttpStatus;

public class PasswordResetResponse {
    private ResponseMessage responseMessage;
    private HttpStatus httpStatus;

    public PasswordResetResponse(ResponseMessage responseMessage, HttpStatus httpStatus) {
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
