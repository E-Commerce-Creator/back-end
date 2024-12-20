package com.e_commerce_creator.web.response;

import com.e_commerce_creator.common.enums.response.ResponseCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Error {
    private int status;
    private String message;

    public Error(ResponseCode responseCode) {
        if (responseCode != null && responseCode != ResponseCode.SUCCESS) {
            this.status = responseCode.getCode();
            this.message = responseCode.getMessage();
        }
    }

    public Error(ResponseCode responseCode, String message) {
        if (responseCode != null && responseCode != ResponseCode.SUCCESS) {
            this.status = responseCode.getCode();
            this.message = message;
        }
    }
}
