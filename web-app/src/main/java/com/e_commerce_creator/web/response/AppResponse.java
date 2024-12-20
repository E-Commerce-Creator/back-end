package com.e_commerce_creator.web.response;

import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResponse<D> {
    private D data;
    private Error error;
    private Map<String, Object> metaInfo = new HashMap<>();
    private String location;
    @JsonIgnore
    private HttpStatus httpStatus;

    public AppResponse() {
    }

    public AppResponse(Map<String, Object> metaInfo, D data, Error error, HttpStatus httpStatus, String location) {
        this.data = data;
        this.metaInfo = metaInfo;
        this.httpStatus = httpStatus;
        this.error = error;
        this.location = location;
    }

    public void setData(D data) {
        this.data = data;
    }

    public void setError(Error e) {
        error = e;
    }

    public Error getError() {
        return error;
    }

    @JsonIgnore
    public ResponseEntity<AppResponse<D>> getResponseEntity() {
        return new ResponseEntity<AppResponse<D>>(this, this.getHttpStatus());
    }

    public static <D> ResponseBuilder<D> builder() {
        return new ResponseBuilder<>();
    }
    public static class ResponseBuilder<D> {

        private final Map<String, Object> info = new HashMap<>();
        private ResponseCode responseCode = ResponseCode.SUCCESS;

        private D data;
        private String location;


        public ResponseBuilder<D> info(String key, Object value) {
            if (key != null && value != null) info.put(key, value);
            return this;
        }

        public ResponseBuilder<D> location(String location) {
            if (location != null) this.location = location;
            return this;
        }

        public ResponseBuilder<D> data(D data) {
            if (data != null) {
                this.data = data;
            }
            return this;
        }

        public ResponseBuilder<D> status(ResponseCode responseCode) {
            if (responseCode != null && responseCode != ResponseCode.SUCCESS) this.responseCode = responseCode;
            return this;
        }

        public AppResponse<D> build() {
            Error error = null;
            if (responseCode.getHttpStatus() != HttpStatus.OK) error = new Error(responseCode);
            return new AppResponse<D>(info, data, error, responseCode.getHttpStatus(), location);
        }
    }
}
