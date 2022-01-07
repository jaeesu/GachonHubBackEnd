package com.example.gachonhub.payload.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<DefaultResponse<T>> success(T t) {
        DefaultResponse<T> tDefaultResponse = new DefaultResponse<>(HttpStatus.OK, t, true);
        return new ResponseEntity(tDefaultResponse, HttpStatus.OK);
    }

    public static class DefaultResponse<T> {
        private int status;
        private T data;
        private Boolean success;

        public DefaultResponse(HttpStatus status, T data, Boolean success) {
            this.status = status.value();
            this.data = data;
            this.success = success;
        }
    }
}
