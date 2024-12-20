package com.e_commerce_creator.common.exception;

import com.e_commerce_creator.common.enums.response.ResponseCode;

public class ECCException extends Exception {
    ResponseCode responseCode;

    public ECCException(ResponseCode code) {
        super();
        this.responseCode = code;
    }


    public ECCException(String message, Throwable cause, ResponseCode code) {
        super(message, cause);
        this.responseCode = code;
    }

    public ECCException(String message, ResponseCode code) {
        super(message);
        this.responseCode = code;
    }

    public ECCException(Throwable cause, ResponseCode code) {
        super(cause);
        this.responseCode = code;
    }

    public ResponseCode getCode() {
        return this.responseCode;
    }
}
