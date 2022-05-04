package com.example.gachonhub.common.ui.out;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseUtil {

    public static <T> ResponseEntity<DefaultResponse<T>> success(T t) {

        DefaultResponse<T> tDefaultResponse = new DefaultResponse(HttpStatus.OK, t, "success");
        return new ResponseEntity(tDefaultResponse, HttpStatus.OK);
    }

    public static ResponseEntity<DefaultResponse<String>> fail(@NotNull HttpStatus status, String message) {
        DefaultResponse<String> tDefaultResponse = new DefaultResponse(status, message);
        return new ResponseEntity(tDefaultResponse, status);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DefaultResponse<T> {
        private int status;
        private T data;
        private String message;

        public DefaultResponse(HttpStatus status, T data, String message) {
            this.status = status.value();
            this.data = data;
            this.message = message;
        }

        public DefaultResponse(HttpStatus status, String message) {
            this.status = status.value();
            this.message = message;
        }
    }
}
